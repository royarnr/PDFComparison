package com.cognizant.framework.Report;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailReport {
	
	private String testcasename;
	public static String body;
	
	public DetailReport(String testcasename)
	
	{
		this.testcasename = testcasename;
		this.body = "";
	}
	
	public void createReport(String reportpath) {
        
        try {
        	
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh mm ss");
            String formattedDate = sdf.format(date);
            System.out.println(formattedDate);
        
            OutputStream htmlfile = new FileOutputStream(new File(reportpath+File.separator+testcasename+".html"));
            PrintStream printhtml = new PrintStream(htmlfile);
	
            String temp = "";
	
            String htmlheader="<html><h1 style=\"text-align:center\" color=\"blue\">"+testcasename+" Details</h1><head>";
            htmlheader+="<title>Report HTML</title><style type=\"text/css\">#maintable {  width: 800px;  margin: 0 auto; }</style>";
            htmlheader+="<style>body {background-color:lightgrey}</style>";
            htmlheader+="</head><body>";
            String htmlfooter="</table></body></html>";
            
            temp = htmlheader;
            temp += "<table border=\"1\" style=\"width:100%\" id=\"maintable\">";
            temp+= "<tr><th bgcolor = \"#ffb000\" style= \"color:blue\">File 1 Image</th><th bgcolor = \"#ffb000\" style= \"color:blue\">File 2 Image</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Status</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Validation Image</th></tr>";
            
            temp += body;
            temp += htmlfooter;
            printhtml.println(temp);
	
	        printhtml.close();
	        htmlfile.close();
	        File htmlFile = new File(reportpath+File.separator+testcasename+".html");
//	        Desktop.getDesktop().browse(htmlFile.toURI());
        
        } catch (Exception pce) {
        	pce.printStackTrace();
        }
	 }

 public  void createRow() {
        
	 body += "<tr>";
 }

 public  void endRow() {
        
	 body += "</tr>";
 }
	
public void headings(String text) {
    
	 body += "<th bgcolor = \"#ffb000\" style=\"font-weight:bold\">" + text + "</th>";
 }

 public void passStatements(String status) {
        
	 body += "<td bgcolor = \"#58D68D\" align = \"center\" style=\"font-weight:bold\"><p>" + status + "</p></td>";
 }
 

 public void infoStatements(String text) {
        
	 body += "<td bgcolor = \"#85C1E9\" style=\"font-weight:bold\">" + text + "</td>";
 }
 
 public void failStatements(String status) {
        
	 body += "<td bgcolor = \"red\" align = \"center\" style=\"font-weight:bold\">" + status + "</td>";
 }
 
 public void insertimagefile(String pathtofile, String pageno, String alttext) {
      
	 body += "<td bgcolor = \"#85C1E9\" style=\"font-weight:bold\">"+"<p>"+pageno+"</p>"+
			 "<a href=\""+pathtofile+"\">"+
	"<img src=\""+pathtofile+"\" alt=\""+alttext+"\" style=\"width:300px;height:200px;\"></a>"
	 +"</td>";
	 
 }
 
 public void warningStatements(String text) {
	 
	 body += "<td bgcolor = \"#F0E68C\">" + text + "</td>";
        
 }



}
