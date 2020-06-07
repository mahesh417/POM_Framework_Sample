package com.expressgift.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.opencsv.CSVReader;


/*Used to read data from the csv file, input csv file should be combination of Test class name and Test class method name.
 * @author Varun
 * @return Map instance of data set for the execution. 
 */
public class CSVdataProvider {
	
	@DataProvider(name = "CsvDataProvider")
	public static Iterator<Object[]> proviceData(Method method){
		List<Object[]> list=new ArrayList<Object[]>();
		String pathname = "src" +File.separator + "test" + File.separator + "resources" + File.separator + "test_data" + File.separator +method.getDeclaringClass().getSimpleName() + "_" + method.getName() +".csv";
		File file = new File(pathname);
		
		try {
			CSVReader reader = new CSVReader(new FileReader(file));
			String[] columnHeader= reader.readNext();
			if (columnHeader !=null) {
				String[] columnData;
				while((columnData = reader.readNext())!=null) {
					Map<String, String> testData = new HashMap<String, String>();
					for (int i = 0; i < columnHeader.length; i++) {
						testData.put(columnHeader[i], columnData[i]);
					}
					list.add(new Object[] { testData });
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File" + pathname + "was not found." + e.getStackTrace().toString());
		} catch (IOException e) {
			throw new RuntimeException("Cound not read" + pathname + "was not found." + e.getStackTrace().toString());
		}
		return list.iterator();
	}
}
