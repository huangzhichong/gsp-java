package gsp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helper {
	/**
	 * @return a string type timeStamp
	 * @throws InterruptedException
	 */
	public static String timeStamp() throws InterruptedException {
		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(currentDate);
		Thread.sleep(1);
		return date;
	}

	public static String timeStamp(String timeFormat) throws InterruptedException {
		java.util.Date currentDate = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(timeFormat);
		String date = dateFormat.format(currentDate);
		Thread.sleep(1);
		return date;
	}

	public static Properties getProperties() throws IOException {
		InputStream inputStream;
		Properties prop = new Properties();
		String propFileName = "config.properties";
		inputStream = GSPDemo.class.getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		return prop;
	}

}
