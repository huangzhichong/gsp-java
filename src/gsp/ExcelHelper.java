package gsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelHelper {
	private static Logger log = Logger.getLogger(ExcelHelper.class);
	public static String imgList;
	public static String errorMsg;
	public static boolean status;

	public static void logInfo(String msg) {
		log.info(msg);
	}

	/**
	 * Log exception with define format.
	 * 
	 * @param msg
	 * @param t
	 */
	public static void logException(String msg, Throwable t) {
		log.error("==================Exception=================");
		log.error(msg, t);
		log.error("====================next====================");
	}

	/**
	 * Log error that occur during the case run. not *Exception*.
	 * 
	 * @param msg
	 */
	public static void logError(String msg) {
		log.error("====================Error===================");
		log.error(msg);
		log.error("====================next====================");
	}

	/**
	 * Example: upperFirst("test") => "Test"
	 * 
	 * @param target
	 * @return format string
	 */
	public static String upperFirst(String target) {
		String temp = target.substring(0, 1);
		return temp.toUpperCase() + target.substring(1);
	}

	public static List<List<String>> readCSV(String filePath) {
		List<List<String>> list = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String data = null;
			try {
				data = br.readLine();
				if (null != data) {
					list = new ArrayList<List<String>>();
					while (data != null) {
						list.add(Arrays.asList(data.split(",")));
						data = br.readLine();
					}
				} else {
					ExcelHelper.logError("Target file has nothing!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			ExcelHelper.logException("Cannot find file or file cannot be reading!", e.fillInStackTrace());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				ExcelHelper.logException("Cannot close input stream!", e.fillInStackTrace());
			}
		}
		return list;
	}

	public static List<Map<String, String>> readExcel(String filePath) {
		File f = new File(filePath);
		List<List<String>> list = null;
		InputStream is = null;
		HSSFWorkbook wb = null;
		ArrayList<Map<String, String>> hashResult = new ArrayList<Map<String, String>>();

		try {
			is = new FileInputStream(f);
			wb = new HSSFWorkbook(is);
		} catch (Exception e) {
			ExcelHelper.logException("Cannot find file or file cannot be reading!", e.fillInStackTrace());
		} finally {
			try {
				if (null != is)
					is.close();
			} catch (IOException e) {
				ExcelHelper.logException("Cannot close input stream!", e.fillInStackTrace());
			}
		}
		if (null != wb) {
			list = new ArrayList<List<String>>();
			HSSFSheet sheet = wb.getSheetAt(wb.getFirstVisibleTab());
			int rowNum = sheet.getLastRowNum();
			HSSFRow headerRow = sheet.getRow(0);
			List<String> headers = new ArrayList<String>();
			int cellNum = headerRow.getLastCellNum();

			for (int k = 0; k < cellNum; k++) {
				Cell c = headerRow.getCell(k);
				headers.add(c.getStringCellValue());
			}

			for (int j = 1; j < rowNum; j++) {
				HSSFRow row = sheet.getRow(j);
				List<String> args = new ArrayList<String>();
				Map<String, String> line_map = new HashMap<String, String>();
				for (int k = 0; k < cellNum; k++) {
					Cell c = row.getCell(k);
					args.add(c.getStringCellValue());
					line_map.put(headers.get(k), c.getStringCellValue());
				}
				list.add(args);
				hashResult.add(line_map);
			}

		} else {
			ExcelHelper.logError("Excel file has nothing!");
		}
		return hashResult;
	}

}
