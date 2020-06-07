package com.expressgift.page.utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/*
 * Used to get configuration file data based on key.
 * @author Varun
 */
public class GetConfig {
	@SuppressWarnings("finally")
	public static String getConfigProperty(String property) {
		String propertyValue = "";
		// If no inputs passed in, look for a configuration file
		String configFile = System.getProperty("user.dir") + "/Configuration.cnf";
		try {
			InputStream configFileStream = new FileInputStream(configFile);
			Properties p = new Properties();
			p.load(configFileStream);
			configFileStream.close();

			propertyValue = (String) p.get(property);
			
		} catch (Exception e) { // IO or NullPointer exceptions possible in
								// block above
			System.out.println("Useful message");
			System.exit(1);
		}
		finally {
			return propertyValue;
		}
	}

}
