/*
 * Copyright 2014 - 2017 Cognizant Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognizant.cognizantits.engine.execution.run;

import com.cognizant.cognizantits.datalib.component.Scenario;
import com.cognizant.cognizantits.datalib.component.TestCase;
import com.cognizant.cognizantits.datalib.component.TestStep;
import com.cognizant.cognizantits.engine.constants.SystemDefaults;
import com.cognizant.cognizantits.engine.execution.data.DataProcessor;
import com.cognizant.cognizantits.engine.execution.data.Parameter;
import com.cognizant.cognizantits.engine.execution.exception.DriverClosedException;
import com.cognizant.cognizantits.engine.execution.exception.UnKnownError;
import com.cognizant.cognizantits.engine.execution.exception.data.DataNotFoundException;
import com.cognizant.cognizantits.engine.support.Status;
import com.cognizant.cognizantits.engine.support.Step;
import com.cognizant.cognizantits.engine.support.reflect.MethodExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestStepRunner {

    private static final Logger LOG = Logger.getLogger(TestStepRunner.class.getName());

    private final TestStep testStep;
    private final Parameter parameter;

    public TestStepRunner(TestStep testStep, Parameter parameter) {
        this.parameter = parameter;
        this.testStep = testStep;
    }

    public TestStepRunner() {
        this.parameter = null;
        this.testStep = null;
    }

    public void run(TestCaseRunner context) throws DataNotFoundException, DriverClosedException {
        if (this.parameter != null && this.testStep != null) {
            if (context.executor().isDebugExe()) {
                checkForDebug();
            }
            switch (getStep().getObject()) {
                case "Execute":
                    execute(context);
                    break;
                default:
                    executeStep(context);
                    break;
            }
        } else {
            throw new RuntimeException("Not enough data to run a step");
        }
    }

    private void checkForDebug() {
        SystemDefaults.nextStepflag.set(true);
        SystemDefaults.pauseExecution.set(getStep().hasBreakPoint()
                || SystemDefaults.pauseExecution.get());
        while (SystemDefaults.pauseExecution.get() && SystemDefaults.nextStepflag.get()
                && !SystemDefaults.stopExecution.get()) {
            SystemDefaults.pollWait();
        }
    }

    private int getSubIterationFromInput(TestCaseRunner context) {
        if (!getStep().getInput().isEmpty()) {
            try {
                return Integer.valueOf(DataProcessor.resolve(getStep().getInput(), context,
                        String.valueOf(parameter.getSubIteration())));
            } catch (Exception ex) {
                System.err.println("Unable to resolve subIteration for reusable!!");
                LOG.log(Level.WARNING, ex.getMessage(), ex);
                return 1;
            }
        }
        return parameter.getSubIteration();
    }

    private TestStep getStep() {
        return testStep;
    }

    private void execute(TestCaseRunner context) throws DataNotFoundException {
        if (getStep().getAction().matches(".*:.*")) {
            String scenario = getStep().getAction().split(":", 2)[0];
            String testcase = getStep().getAction().split(":", 2)[1];
            try {
                Scenario scn = context.project().getScenarioByName(scenario);
                if (scn != null) {
                    TestCase stc = scn.getTestCaseByName(testcase);
                    if (stc != null) {
                        executeTestCase(context, stc);
                        return;
                    }
                }
            } catch (DataNotFoundException ex) {
                throw ex;
            } catch (Exception ex) {
                LOG.log(Level.WARNING, "Unable to load reusable", ex);
            }
        }
    }

    private void executeTestCase(TestCaseRunner context, TestCase stc) throws DataNotFoundException {
        try {
            parameter.setSubIteration(getSubIterationFromInput(context));
            context.getReport().startComponent(getStep().getAction(), getStep().getDescription());
            new TestCaseRunner(context, stc, parameter).run();
        } finally {
            context.getReport().endComponent(getStep().getAction());
        }
    }

    private void executeStep(TestCaseRunner context) throws DataNotFoundException, DriverClosedException {
        try {
            Step curr = new Step(testStep, context);
            Annotation ann = new Annotation(context.getControl());
            ann.beforeStepExecution();
            executeStep(context, curr);
            ann.afterStepExecution();
        } catch (DataNotFoundException | DriverClosedException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new UnKnownError(ex);
        }
    }

    private void executeStep(TestCaseRunner context, Step curr) throws Throwable {
        context.getReport().updateStepDetails(curr.printStep());
        context.getControl().sync(curr, String.valueOf(parameter.getSubIteration()));
        executeAction(context, curr.Action);
    }

    public void executeAction(TestCaseRunner context, String action) throws Throwable {
        if (!MethodExecutor.executeMethod(action, context.getControl())) {
            System.out.println("[ERROR][Could not find Action:" + action + "]");
            context.getReport().updateTestLog(action, "[Could not find Action]",
                    Status.DEBUG);
        }
    }

}
