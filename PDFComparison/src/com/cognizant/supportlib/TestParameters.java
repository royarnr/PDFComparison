package com.cognizant.supportlib;

public class TestParameters {
	
	private String testcasename;
	private String inputfile1;
	private String inputfile2;
	
	public TestParameters()
	{
		this.testcasename = "";
		this.inputfile1 = "";
		this.inputfile2 = "";
	}
	
	public void settestcaseName(String testcasename)
	{
		this.testcasename = testcasename;
	}
	
	public void setinputfile1(String inputfile1)
	{
		this.inputfile1 = inputfile1;
	}
	
	public void setinputfile2(String inputfile2)
	{
		this.inputfile2 = inputfile2;
	}
	
	public String gettestcasename()
	{
		return this.testcasename;
	}
	
	public String getinputfile1name()
	{
		return this.inputfile1;
	}
	
	public String getinputfile2name()
	{
		return this.inputfile2;
	}
}
