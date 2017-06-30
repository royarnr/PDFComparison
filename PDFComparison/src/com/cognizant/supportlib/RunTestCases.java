package com.cognizant.supportlib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RunTestCases {
	
	public List<TestParameters> testinstancestorun;
	private String filename = "";
	
	public RunTestCases()
	{
		//this.filename = Paths.get(System.getProperty("user.dir"), "src","RunManager.xlsx").toString();
		this.filename = Paths.get(System.getProperty("user.dir"), "RunManager.xlsx").toString();
	}
	
	public List<TestParameters> gettestinstancestoRun()
	{
		Workbook workbook = null;
		Sheet worksheet = null;
		testinstancestorun  = new ArrayList<>();
		try {
			if (filename.contains(".xlsx"))
			{
			workbook = new XSSFWorkbook(new File(filename).toString());
			}
			else
			{
				workbook = new HSSFWorkbook(new FileInputStream(new File(filename)));
			}
			worksheet = workbook.getSheet("Results");
			
			for (int row = 1; row <= worksheet.getLastRowNum(); row++)
			{
				
				
				if (worksheet.getRow(row).getCell(3).toString().equalsIgnoreCase("Yes"))
				{
					TestParameters testparameter = new TestParameters();
					worksheet.getRow(row).getCell(0).setCellType(CellType.STRING);
					testparameter.settestcaseName(worksheet.getRow(row).getCell(0).getStringCellValue());
					worksheet.getRow(row).getCell(1).setCellType(CellType.STRING);
					testparameter.setinputfile1(worksheet.getRow(row).getCell(1).getStringCellValue());
					worksheet.getRow(row).getCell(2).setCellType(CellType.STRING);
					testparameter.setinputfile2(worksheet.getRow(row).getCell(2).toString());
					testinstancestorun.add(testparameter);
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return testinstancestorun;

	}

}
