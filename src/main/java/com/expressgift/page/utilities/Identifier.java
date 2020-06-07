package com.expressgift.page.utilities;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Element identifier file to get property file data based on keys.
 * @author Varun
 */
public class Identifier {

	private Properties property;

	private boolean useCompoundProperties = false;

	private HashMap<String, String> compoundPropertyCache = new HashMap<>();

	private final String className;

	private static Pattern compoundPropertyRegex = Pattern.compile("([^.]*(?:\\.[^.]*)*)(\\.[^.]*)");

	public Identifier(Class<?> app) {
		this(app, app.getName().subSequence(app.getName().lastIndexOf('.')+1, app.getName().length()).toString().toLowerCase()+"_Identifier.properties");
	}

	public Identifier(Class<?> app, String propertiesFile) {

		property = new Properties();

		className = app.getName();

		InputStream input = app.getResourceAsStream(propertiesFile);

		try {

			property.load(input);

		} catch (Exception e) {

			System.err.println("Could not load " + propertiesFile + " for application " + app.getName());

		}

	}

	/**
	 * 
	 * Returns the property with the given name. If you set useCompoundProperties to
	 * true, special lookup rules will be
	 * 
	 * applied to properties containing dots ('.'). For example, if you have the
	 * following prpoerties defined:
	 *
	 * 
	 * 
	 * <pre>
	
	* Foo=Foo1
	
	* Foo.Bar=Bar2
	
	* Foo.Bar.Baz=Baz3
	 * 
	 * </pre>
	 *
	 * 
	 * 
	 * then the result of calling getProperty("Foo.Bar.Baz") will be "Foo1Bar2Baz3".
	 * 
	 * <p>
	 * 
	 * "Foo.Bar" is called a direct subproperty, as is "Foo.Bar.Baz". The lookup
	 * rules go as follows:
	 * 
	 * <ol>
	 * 
	 * <li>If the propertyName contains no dots '.', then get the property
	 * normally.</li>
	 * 
	 * <li>Recursively call getProperty on the propertyName with the last
	 * dot-separated section removed.</li>
	 * 
	 * <li>Check if the full propertyName exists as a property. If so, append it to
	 * the result of the recursive call and
	 * 
	 * return</li>
	 * 
	 * <li>If not, check for indirect subproperty.</li>
	 * 
	 * <li>If not, check for global sunproperty.</li> TODO: finish
	 *
	 * 
	 * 
	 * @param propertyName
	 * 
	 * @return
	 * 
	 */

	public String getProperty(String propertyName) {

		if (propertyName == null) {

			return null;

		} else if (useCompoundProperties && propertyName.contains(".")) {

			if (compoundPropertyCache.containsKey(propertyName)) {

				return compoundPropertyCache.get(propertyName);

			} else {

				Matcher propSplit = compoundPropertyRegex.matcher(propertyName);

				String result = null;

				if (propSplit.find()) {

					/*
					 * 
					 * Example: for propertyName of "Foo.Bar.Baz", parent = "Foo.Bar" lastPart =
					 * ".Baz"
					 * 
					 */

					String parent = propSplit.group(1);

					String lastPart = propSplit.group(2);

					String parentValue = parent.isEmpty() ? "" : getProperty(parent);

					if (property.containsKey(propertyName)) {

						result = parentValue + property.getProperty(propertyName);

					} else {

						/*
						 * 
						 * Check for sub-property Example: for propertyName "Foo.Bar.Baz", Firt check
						 * for
						 * 
						 * "Foo.Bar..Baz", then "Foo..Baz", then finally ".Baz"
						 * 
						 */

						parent += ".";

						String subProp = parent + lastPart;

						while (!property.containsKey(subProp)) {

							if (parent.isEmpty()) {

								throw new RuntimeException("Cannot find compound property \"" + propertyName + "\".");

							}

							parent = parent.replaceFirst("[^.]+\\.$", "");

							subProp = parent + lastPart;

						}

						result = parentValue + property.getProperty(subProp);

					}

				} else {

					throw new RuntimeException("Cannot parse compound property \"" + propertyName + "\".");

				}

				compoundPropertyCache.put(propertyName, result);

				return result;

			}

		} else {

			return property.getProperty(propertyName);

		}

	}

	public boolean isUseCompoundProperties() {

		return useCompoundProperties;

	}

	public void setUseCompoundProperties(boolean useCompoundProperties) {

		this.useCompoundProperties = useCompoundProperties;

	}

	/**
	 * 
	 * @return the className
	 * 
	 */

	public String getClassName() {

		return className;

	}

}
