package com.cognizant.supportlib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Settings {
	private static Properties properties = loadFromPropertiesFile();

	private Settings() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the {@link Properties}
	 * object
	 * 
	 * @return Instance of the {@link Properties} object
	 */
	public static Properties getInstance() {
		return properties;
	}


	private static Properties loadFromPropertiesFile() {

		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream(System.getProperty("user.dir")
					+ "\\"
					+ "GlobalSettings.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

}
