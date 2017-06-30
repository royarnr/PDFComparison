package com.cognizant.supportlib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {
	

	private String filename;
	private ResultsSummary results;
	private static Logger log = new Logger();
	
	private Logger()
	{
		
	}
	
	public static Logger getInstance()
	{
		return log;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	
	public void initialize()
	{
		results = ResultsSummary.getInstance();
		filename = results.getReportPath()+File.separator+"Log"+new SimpleDateFormat("MMddyyyy_hhmm").format(new Date())+".txt";
	
	}
	
	public void writeLog(String s)
	{
		
		BufferedWriter writer = null;
		try 
		{
			writer = new BufferedWriter(new FileWriter(new File(Logger.getInstance().getFilename()), true));
			writer.write(new SimpleDateFormat("hh:mm:ss a").format(new Date())+" - "+s);
			writer.newLine();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
	}
	
	

}
