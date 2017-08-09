package com.cognizant.supportlib;

import java.io.File;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultsSummary {
	
	private String rootreportpath = "";
	private String reportpathfortestcase = "";
	private String file1imagespath = "";
	private String file2imagepath = "";

	public String testresultimages="";
	private static String starttime="";
	private static String endtime = "";
	private static Date starttimedate;
	private static Date endtimedate;
	
	public static ResultsSummary report = new ResultsSummary();
	
	private ResultsSummary()
	{
		if (report==null)
		{
		
		Format format1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
		Format format = new SimpleDateFormat("dd-MMM-yyyy_hhmmssaa");
		starttimedate = new Date();
		starttime = format1.format(starttimedate);
		rootreportpath = Paths.get(System.getProperty("user.dir"), "src", "Results","Results"+format.format(new Date())).toString();
		if (!new File(rootreportpath).exists())
		{
			new File(rootreportpath).mkdirs();
			System.out.println("Results folder created");
		}
		}
		
		
	}
	
	public static ResultsSummary getInstance()
	{
		return report;
	}
	
	public String getReportPath()
	{
		return rootreportpath;
	}
	
	public void setfilepathsforimages(String testcasename, String inputfilename1, String inputfilename2)
	{
		testresultimages = Paths.get(rootreportpath, testcasename, "TestResultImages").toString();
		file1imagespath = Paths.get(rootreportpath, testcasename, "File1_Pages_Images").toString();
		file2imagepath = Paths.get(rootreportpath, testcasename, "File2_Pages_Images").toString();
		if (!new File(testresultimages).exists())
		{
			new File(testresultimages).mkdirs();
		}
		
		if (!new File(file1imagespath).exists())
		{
			new File(file1imagespath).mkdirs();
		}
		
		if (!new File(file2imagepath).exists())
		{
			new File(file2imagepath).mkdirs();
		}
		
		
	}
	
	public String getpathforfile1()
	{
		return file1imagespath;
	}
	
	
	public String getpathforfile2()
	
	{
		return file2imagepath;
	}
	
	public String getpathfortestresultimages()
	{
		return testresultimages;
	}
	
	public String getendtime()
	{
		endtimedate = new Date();
		endtime = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa").format(endtimedate);
		return endtime;
	}
	
	public String getstarrttime()
	{
		
		return starttime;
	}

	public long getelapsedtimeinseconds()
	{
		return (endtimedate.getTime() - starttimedate.getTime())/1000;
	}
	
	

}
