package com.cognizant.framework.Report;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class SummaryReport {
	
	public static String body;
	private static String reportpath;
	private static String headertable;
	
	public SummaryReport(String reportpath){
		
		this.reportpath = reportpath;
		this.body = "";
		this.headertable = "";
	}
	
	public void createReport() {
        
        try {
        	
//            Date date = new Date();
//            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh mm ss");
//            String formattedDate = sdf.format(date);
//            System.out.println(formattedDate);
        
            OutputStream htmlfile = new FileOutputStream(new File(reportpath+File.separator+"Summary.html"));
            PrintStream printhtml = new PrintStream(htmlfile);
	
            String temp = "";
	
            String htmlheader="<html><h1 style=\"text-align:center\">XML Comparision Validation Summary</h1><head>";
            htmlheader+="<title>Report Summary HTML</title><style type=\"text/css\">#maintable {  width: 800px;  margin: 0 auto; } #headertable {  width: 800px;  margin: 0 auto; [blue]margin-bottom:50px;[/blue] } </style>";
            
            htmlheader+="<style>body {background-color:lightgrey}</style>";
            htmlheader+="</head><body>";
            
            htmlheader+= "<table border=\"1\" style=\"width:100%\" id=\"headertable\">";
            htmlheader+="<tr><th bgcolor = \"#ffb000\" style= \"color:blue\">Execution Start Time"
            		+"</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Execution End Time"
                    +"</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Execution Elapsed Time"
                    +"</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Overall Status"
                    +"</th></tr>";
            htmlheader+=headertable;
            htmlheader+="</table>";
            htmlheader+="<br><br>";
            String htmlfooter="</table></body></html>";
            
            temp = htmlheader;
            temp += "<table border=\"1\" style=\"width:100%\" id=\"maintable\">";
//            temp += "<tr><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution Start Time : "
//                    +starttime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution End Time : "
//                    +endtime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution Elapsed Time : "
//                    +elapsedtime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Overall Status :  "
//                    +overallstatus+"</td></tr>";
            temp += "<tr><th bgcolor = \"#ffb000\" style= \"color:blue\">Test Case Name"+
                    "</th><th bgcolor = \"#ffb000\" style= \"color:blue\">XML Input File Name (1)"
                    +"</th><th bgcolor = \"#ffb000\" style= \"color:blue\">XML Input File Name (2)"
                    +"</th><th bgcolor = \"#ffb000\" style= \"color:blue\">Execution Status"
                    +"</th></tr>";
//            temp += "<tr><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution Start Time : "
//            +starttime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution End Time : "
//            +endtime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Execution Elapsed Time : "
//            +elapsedtime+"</td><td bgcolor = \"#FCF3CF\" style= \"color:blue\">Overall Status :  "
//            +overallstatus+"</td></tr>";
           
            
            temp += body;
            temp += htmlfooter;
            printhtml.println(temp);
	
	        printhtml.close();
	        htmlfile.close();
	        File htmlFile = new File(reportpath+File.separator+"Summary.html");
	        Desktop.getDesktop().browse(htmlFile.toURI());
        
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
	
public  void headings(String headingtext) {
    
	 body += "<th bgcolor = \"#00E5EE\">" + headingtext + "</th>";
 }

 public  void passStatements(String status) {
        
	 body += "<td bgcolor = \"#58D68D\" style=\"font-weight:bold\" align=\"center\" ><p>" + status + "</p></td>";
 }
 

 public  void failStatements(String status) {
     
	 body += "<td bgcolor = \"red\" align = \"center\" style=\"font-weight:bold\" > <p>" + status + "</p></td>";
 }
 
 public  void infoStatements(String text) {
        
	 body += "<td bgcolor = \"#85C1E9\">" + text + "</td>";
 }
 
 public  void testcaseLink(String pathtotestcasefile, String testcasename) {
     
	
	 body += "<td bgcolor = \"#85C1E9\">"+
			 "<a href=\""+pathtotestcasefile+"\" target = \"_blank\">"+testcasename
	+"</a>"+"</td>";
 }
 
 
 public void warningStatements(String text) {
	 
	 body += "<td bgcolor = \"#F0E68C\">" + text + "</td>";
        
 }
 
 public  void createRowSummary() {
     
	 headertable += "<tr>";
 }

 public  void endRowSummary() {
        
	 headertable += "</tr>";
 }
 
 public  void addSummarydata(String text) {
     
	 headertable += "<td bgcolor = \"#85C1E9\" align = \"center\" color = \"blue\" style=\"font-weight:bold\" >" + text + "</td>";
 }
 
 public  void addSummaryPassedstatus(String status) {
     
	 headertable += "<td bgcolor = \"#58D68D\" align = \"center\" style=\"font-weight:bold\" >" + status + "</td>";
 }
 
 public  void addSummaryFailedstatus(String status) {
     
	 headertable += "<td bgcolor = \"red\" align = \"center\" style=\"font-weight:bold\" >" + status + "</td>";
 }
 


}
