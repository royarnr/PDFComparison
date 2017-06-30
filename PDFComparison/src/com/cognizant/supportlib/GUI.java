package com.cognizant.supportlib;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

public class GUI {
	

	private JFrame frame;
	private JTextArea textarea;
	private static GUI form = new GUI();
	
	private GUI()
	{
		
	}
	
	public static GUI getInstance()
	{
		return form;
	}
	
	public void closeForm()
	{
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.setVisible(false);
	}
	
	public void launch()
	{
		frame = new JFrame("PDF Comparison Execution Logs");
		textarea = new JTextArea("PDF Comparison Execution Logs...\n", 20,20);
		JScrollPane scroll = new JScrollPane(textarea);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		DefaultCaret caret = (DefaultCaret)textarea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		frame.add(scroll);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 400));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void writeLog(String s)
	{
		textarea.append(new SimpleDateFormat("hh:mm:ss a").format(new Date())+" - "+s+"\n");
		textarea.setCaretPosition(textarea.getDocument().getLength());
	}
	

}
