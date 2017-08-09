package com.cognizant.supportlib;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadFilesfromFolder {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<File> listoffiles = new ArrayList<>();
		String path = "D:\\testfile";
		
		File[] listofimages = new File(path).listFiles();
		
		for (File file : listofimages) {
			listoffiles.add(file);
		}
		
		Iterator<File> iterator = listoffiles.iterator();
		while(iterator.hasNext())
		{
			System.out.println(iterator.next().getAbsolutePath());
		}
	}

}
