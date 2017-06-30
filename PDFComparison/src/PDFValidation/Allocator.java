package PDFValidation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.cognizant.framework.PDFCompare.PDFHighlight;
import com.cognizant.framework.PDFCompare.PDFPageExtractor;
import com.cognizant.framework.Report.DetailReport;
import com.cognizant.framework.Report.SummaryReport;
import com.cognizant.supportlib.GUI;
import com.cognizant.supportlib.Logger;
import com.cognizant.supportlib.ResultsSummary;
import com.cognizant.supportlib.RunTestCases;
import com.cognizant.supportlib.Settings;
import com.cognizant.supportlib.TestParameters;

public class Allocator {
	private static List<TestParameters> testinstancestorun;
	private static ResultsSummary report;
	private static String overallteststatus;
	private static List<String> textlines;
	private static GUI uiForm = GUI.getInstance();
	private static Properties properties;
	private static Logger log = Logger.getInstance();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		uiForm.launch();
		log.initialize();
		properties = Settings.getInstance();
		RunTestCases runner = new RunTestCases();
		testinstancestorun = new ArrayList<>();
		textlines = new ArrayList<>();
		uiForm.writeLog("Getting the test cases from the Run Manager");
		log.writeLog("Getting the test cases from the Run Manager");
		testinstancestorun = runner.gettestinstancestoRun();
		report = ResultsSummary.getInstance();
		uiForm.writeLog("Initialize report...");
		log.writeLog("Initialize report...");
		overallteststatus = "Passed";
		SummaryReport summaryreport = new SummaryReport(report.getReportPath());


		for (TestParameters testparameter : testinstancestorun) {
			//for comparing the pages between two files
			uiForm.writeLog("Starting execution of "+testparameter.gettestcasename());
			log.writeLog("Starting execution of "+testparameter.gettestcasename());
			report.setfilepathsforimages(testparameter.gettestcasename(), testparameter.getinputfile1name(), testparameter.getinputfile2name());
			DetailReport detailreport = new DetailReport(testparameter.gettestcasename());

			String testcasestatus = "Passed";
			List<File> imagesfromfile1 = new ArrayList<>();
			List<File> imagesfromfile2 = new ArrayList<>();
			try {
				//				System.out.println(testparameter.getinputfile1name());
				//				System.out.println(testparameter.getinputfile2name());
				uiForm.writeLog("Copying file : "+testparameter.getinputfile1name());
				log.writeLog("Copying file : "+testparameter.getinputfile1name());
				String filesrcfile1 = testparameter.getinputfile1name();
				String filedestfile1 = report.getReportPath()+File.separator+testparameter.gettestcasename()+File.separator+"File1"+File.separator+new File(testparameter.getinputfile1name()).getName();
				FileUtils.copyFile(new File(filesrcfile1), new File(filedestfile1));
				uiForm.writeLog("Copying file : "+testparameter.getinputfile2name());
				log.writeLog("Copying file : "+testparameter.getinputfile2name());
				String filesrcfile2 = testparameter.getinputfile2name();
				String filedestfile2 = report.getReportPath()+File.separator+testparameter.gettestcasename()+File.separator+"File2"+File.separator+new File(testparameter.getinputfile2name()).getName();
				FileUtils.copyFile(new File(filesrcfile2), new File(filedestfile2));



				PDFPageExtractor pageextractor1 = new PDFPageExtractor(report.getReportPath()+File.separator+testparameter.gettestcasename()+File.separator+"File1"+File.separator+new File(testparameter.getinputfile1name()).getName());
				PDFPageExtractor pageextractor2 = new PDFPageExtractor(report.getReportPath()+File.separator+testparameter.gettestcasename()+File.separator+"File2"+File.separator+new File(testparameter.getinputfile2name()).getName());

				uiForm.writeLog("Extracting pages from file : "+testparameter.getinputfile1name());
				log.writeLog("Extracting pages from file : "+testparameter.getinputfile1name());
				imagesfromfile1 = pageextractor1.getPDFpageImages(report.getpathforfile1());
				uiForm.writeLog("Extracting pages from file : "+testparameter.getinputfile1name());
				log.writeLog("Extracting pages from file : "+testparameter.getinputfile1name());
				imagesfromfile2 = pageextractor2.getPDFpageImages(report.getpathforfile2());

				if (imagesfromfile1.size()!=imagesfromfile2.size())
				{
					uiForm.writeLog("Number of pages do not match");
					System.out.println("Number of pages of these 2 PDFs does not match");

					/*
					 * Case 1 - No of pages in file 1 is greater than 
					 * that in File 2
					 */

					if (imagesfromfile1.size()>imagesfromfile2.size())
					{

						uiForm.writeLog("Number of pages from "+testparameter.getinputfile1name()+" greater than that in "+testparameter.getinputfile2name());
						for (int i=0; i<imagesfromfile2.size();i++)
						{

							uiForm.writeLog("Copmaring Page "+(i+1));
							PDFHighlight highlight = new PDFHighlight(imagesfromfile1.get(i).getAbsolutePath(), imagesfromfile2.get(i).getAbsolutePath(), report.testresultimages+File.separator+"TestResultPage_"+i+".png");

							//System.out.println("Page "+i+" matches");
							String comparedimagefile = highlight.highlightDifferenes();
							String teststepstatus = highlight.pageimagematch();
							uiForm.writeLog("Match Result for Page "+(i+1)+" "+teststepstatus);
							uiForm.writeLog("Updating results in report for page "+(i+1));
							detailreport.createRow();
//							detailreport.insertimagefile(imagesfromfile1.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
//							detailreport.insertimagefile(imagesfromfile2.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
							
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File1_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File1_Pages_Images\\Page_"+i+".png");
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File2_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File2_Pages_Images\\Page_"+i+".png");
							if (teststepstatus.equalsIgnoreCase("Passed"))
							{
								detailreport.passStatements("Passed");

							}
							else
							{
								detailreport.failStatements("Failed");
								testcasestatus = teststepstatus;
							}
//							detailreport.insertimagefile(comparedimagefile, "Page #"+(i+1), "comparedimage");
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\TestResultImages\\TestResultPage_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - TestResultImages\\TestResultPage_"+i+".png");
							detailreport.endRow();

						}

						for (int i = imagesfromfile2.size(); i < imagesfromfile1.size(); i++) 
						{
							uiForm.writeLog("Updating report for Page "+(i+1));
							detailreport.createRow();
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File1_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File1_Pages_Images\\Page_"+i+".png");
//							detailreport.insertimagefile(imagesfromfile1.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
							detailreport.infoStatements("No image correspponding to page :"+(i+1));
							detailreport.failStatements("Failed");
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File1_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File2_Pages_Images\\Page_"+i+".png");
//							detailreport.insertimagefile(imagesfromfile1.get(i).getAbsolutePath(), "Page #"+(i+1), "compareimage"+i);
							detailreport.endRow();

						}




					}

					/*
					 * Case 2 - No of pages in File 2 in greater than 
					 * that in File 1
					 */
					else
					{
						for (int i=0; i<imagesfromfile1.size();i++)
						{
							uiForm.writeLog("Pages in "+testparameter.getinputfile2name()+" greater than that in "+testparameter.getinputfile1name());
							PDFHighlight highlight = new PDFHighlight(imagesfromfile1.get(i).getAbsolutePath(), imagesfromfile2.get(i).getAbsolutePath(), report.testresultimages+File.separator+"TestResultPage_"+i+".png");

							//System.out.println("Page "+i+" matches");
							uiForm.writeLog("Copmaring Page "+(i+1));
							String comparedimagefile = highlight.highlightDifferenes();
							String teststepstatus = highlight.pageimagematch();
							uiForm.writeLog("Match Result for Page "+(i+1)+" "+teststepstatus);
							uiForm.writeLog("Updating results in report for page "+(i+1));
							detailreport.createRow();
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File1_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File1_Pages_Images\\Page_"+i+".png");
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File2_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File2_Pages_Images\\Page_"+i+".png");
							
//							detailreport.insertimagefile(imagesfromfile1.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
//							detailreport.insertimagefile(imagesfromfile2.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
							if (teststepstatus.equalsIgnoreCase("Passed"))
							{
								detailreport.passStatements("Passed");
								testcasestatus = teststepstatus;

							}
							else
							{
								detailreport.failStatements("Passed");
							}
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\TestResultImages\\TestResultPage_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - TestResultImages\\TestResultPage_"+i+".png");
//							detailreport.insertimagefile(comparedimagefile, "Page #"+(i+1), "comparedimage");
							detailreport.endRow();

						}

						for (int i = imagesfromfile1.size(); i < imagesfromfile2.size(); i++) 
						{
							uiForm.writeLog("Updating report for Page "+(i+1));
							detailreport.createRow();
							detailreport.infoStatements("No image correspponding to page :"+(i+1));
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File2_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "imageofpage"+(i+1));
//							detailreport.insertimagefile(imagesfromfile2.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);

							detailreport.failStatements("Failed");
							detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File2_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "imageofpage"+(i+1));
//							detailreport.insertimagefile(imagesfromfile2.get(i).getAbsolutePath(), "Page #"+(i+1), "compareimage"+i);
							detailreport.endRow();
						}
					}

					uiForm.writeLog("Get the detailed report for "+testparameter.gettestcasename());
					detailreport.createReport(report.getReportPath());

					summaryreport.createRow();
					summaryreport.testcaseLink(report.getReportPath()+File.separator+testparameter.gettestcasename()+".html", testparameter.gettestcasename());
					summaryreport.infoStatements(new File(testparameter.getinputfile1name()).getName());
					summaryreport.infoStatements(new File(testparameter.getinputfile2name()).getName());

					summaryreport.failStatements("FAILED");
					summaryreport.endRow();

				}
				else{

					uiForm.writeLog("Pages in file "+testparameter.getinputfile1name()+" equals to that of "+testparameter.getinputfile2name());
					for (int i=0; i<imagesfromfile1.size();i++)
					{

						uiForm.writeLog("Copmaring Page "+(i+1));
						PDFHighlight highlight = new PDFHighlight(imagesfromfile1.get(i).getAbsolutePath(), imagesfromfile2.get(i).getAbsolutePath(), report.testresultimages+File.separator+"TestResultPage_"+i+".png");

						//System.out.println("Page "+i+" matches");

						String comparedimagefile = highlight.highlightDifferenes();
						String teststepstatus = highlight.pageimagematch();
						uiForm.writeLog("Match Result for Page "+(i+1)+" "+teststepstatus);
						uiForm.writeLog("Updating results in report for page "+(i+1));
						detailreport.createRow();
						detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File1_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File1_Pages_Images\\Page_"+i+".png");
						detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\File2_Pages_Images\\Page_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - File2_Pages_Images\\Page_"+i+".png");
//						detailreport.insertimagefile(imagesfromfile1.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
//						detailreport.insertimagefile(imagesfromfile2.get(i).getAbsolutePath(), "Page #"+(i+1), "imageofpage"+i);
						if (teststepstatus.equalsIgnoreCase("Passed"))
						{
							detailreport.passStatements("Passed");

						}
						else
						{
							detailreport.failStatements("Failed");
							testcasestatus = teststepstatus;
						}
//						detailreport.insertimagefile(comparedimagefile, "Page #"+(i+1), "comparedimage");
						detailreport.insertimagefile(".\\"+testparameter.gettestcasename()+"\\TestResultImages\\TestResultPage_"+i+".png", "Page #"+(i+1), "Page "+(i+1)+"Image Path - TestResultImages\\TestResultPage_"+i+".png");
						detailreport.endRow();

					}

					//				}
					imagesfromfile1 = null;
					imagesfromfile2 = null;
					/*
					 * For PDF Text Mismatch 
					 */
					if (testcasestatus.equalsIgnoreCase("failed"))
					{
						uiForm.writeLog("Getting the text from the PDF(s) for text match"); 
						PDFHighlight texthighlight = new PDFHighlight(testparameter.getinputfile1name(), testparameter.getinputfile2name());
						textlines = texthighlight.getmismatchintext();

						Iterator<String> iterator = textlines.iterator();
						if (textlines.size()>0)
						{
							detailreport.createRow();

							detailreport.headings("Mismatch Occurence");
							detailreport.headings("File 1 Text");
							detailreport.headings("File 2 Text");
							detailreport.headings("Status");
							detailreport.endRow();
							int i=1;
							while(iterator.hasNext())
							{

								String mismatchline = iterator.next(); 
								String [] mismatch = mismatchline.split("~");
								detailreport.createRow();
								detailreport.infoStatements("Mismatch Occurence # "+i);
								try{
								detailreport.infoStatements(mismatch[0]);
								detailreport.infoStatements(mismatch[1]);
								}
								catch (ArrayIndexOutOfBoundsException e)
								{
									detailreport.infoStatements(" ");
								}
								detailreport.failStatements("Failed");
								//System.out.println(iterator.next());
								detailreport.endRow();
								i++;
								testcasestatus = "Failed";

							}
						}
					}
					uiForm.writeLog("Get report for test case "+testparameter.gettestcasename());
					detailreport.createReport(report.getReportPath());
					//detailreport.body="";
					uiForm.writeLog("Detailed report for test case "+testparameter.gettestcasename()+" created");
					uiForm.writeLog("Summary Report entry for test case "+testparameter.gettestcasename());
					summaryreport.createRow();
					summaryreport.testcaseLink(report.getReportPath()+File.separator+testparameter.gettestcasename()+".html", testparameter.gettestcasename());
					summaryreport.infoStatements(new File(testparameter.getinputfile1name()).getName());
					summaryreport.infoStatements(new File(testparameter.getinputfile2name()).getName());
					if (testcasestatus.equalsIgnoreCase("Passed"))
					{
						
						summaryreport.passStatements("Passed");
					}
					else{

						summaryreport.failStatements("Failed");
					}
					summaryreport.endRow();
					uiForm.writeLog("Summary Report entry for test case "+testparameter.gettestcasename()+" created");

					if (testcasestatus.equalsIgnoreCase("Failed"))
					{
						overallteststatus = testcasestatus;
					}


					/*
					 * ALM Upload
					 */

					if (properties.getProperty("ALMUploadAutomatic").equalsIgnoreCase("On")){

						uiForm.writeLog("Upload results to HP ALM for test case "+testparameter.gettestcasename());
						updatetestresultstatusinALM(testparameter.gettestcasename(), testcasestatus);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//				e.printStackTrace();
				System.out.println("Error while extracting images from the PDF input files");
				uiForm.writeLog(e.getMessage());
			}



		}

		System.out.println(report.getstarrttime());
		System.out.println(report.getendtime());
		System.out.println(report.getelapsedtimeinseconds()+" seconds");
		uiForm.writeLog("Generating the summary report...");
		summaryreport.createRowSummary();
		summaryreport.addSummarydata(report.getstarrttime());
		summaryreport.addSummarydata(report.getendtime());
		summaryreport.addSummarydata(report.getelapsedtimeinseconds()+ " seconds");
		if (overallteststatus.equalsIgnoreCase("Passed"))
		{
			summaryreport.addSummaryPassedstatus("Passed");

		}
		else{

			summaryreport.addSummaryFailedstatus("Failed");;
		}

		summaryreport.endRowSummary();
		summaryreport.createReport();
		uiForm.writeLog("Summary Report Generated");
		uiForm.writeLog("Execution ends.");
		uiForm.closeForm();
	}

	private static void updatetestresultstatusinALM(String testcasename, String status)
	{

		String currenttestcase = testcasename;
		String parameterstopassforALMUpload = properties.getProperty("ALMServer")
				+" "
				+properties.getProperty("ALMUsername")
				+" "
				+properties.getProperty("ALMPassword")
				+" "
				+"\""
				+properties.getProperty("ALMDomain")
				+"\""
				+" "
				+"\""
				+properties.getProperty("ALMProject")
				+"\""
				+" "
				+"\""
				+properties.getProperty("ALMTestSetFolderPath")
				+"\""
				+" "
				+"\""
				+properties.getProperty("ALMTestSetName")
				+"\""
				+" "
				+"\""
				+testcasename
				+"\""
				+" "
				+status
				+" "
				+"\""
				+report.getReportPath()+File.separator+testcasename+".html"
				+"\""
				+" "
				+"\""
				+report.getReportPath()+File.separator+testcasename+File.separator+"File1_Pages_Images"
				+"\""
				+" "
				+"\""
				+report.getReportPath()+File.separator+testcasename+File.separator+"File2_Pages_Images"
				+"\""
				+" "
				+"\""
				+report.getReportPath()+File.separator+testcasename+File.separator+"TestResultImages"
				+"\"";

		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		String pathofvbscript = relativePath+File.separator+"ALMConnUpload.vbs";

		try {

			Runtime.getRuntime().exec("C:\\Windows\\SysWOW64\\cscript "+"\""+pathofvbscript+"\""+" "+parameterstopassforALMUpload);


		} catch (IOException e) {
			// TODO Auto-generated catch block

			System.err.println("Vb script trigger failed");
			e.printStackTrace(System.err);
		}
	}


}
