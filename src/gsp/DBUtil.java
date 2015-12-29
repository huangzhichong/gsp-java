package gsp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	private static DBUtil instance = null;

	private String driverName = "net.sourceforge.jtds.jdbc.Driver";
	private String URL = "jdbc:jtds:sqlserver://10.109.0.161:1433;DatabaseName=MEMSDB";
	private String userName = "sa";
	private String userPwd = "@ctive123";
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	
	/**
	 * Used to get the database utilities instance.
	 * 
	 * @return
	 */
	public static DBUtil getInstance() {
		if (null == instance) {
			instance = new DBUtil();
		}

		return instance;
	}

	public static DBUtil getInstance(String host, String user, String password) {
		if (null == instance) {
			String URL = "jdbc:jtds:sqlserver://" + host + ":1433;DatabaseName=MEMSDB";
			System.out.println(URL);
			System.out.println(user);
			System.out.println(password);
			instance = new DBUtil(URL, user, password);
		}

		return instance;
	}

	/**
	 * Close the database connect session.
	 * 
	 * @return if close success.
	 */
	public boolean closeSession() {
		boolean flag = false;
		try {
			if (null != rs) {
				rs.close();
			}
			if (null != st) {
				st.close();
			}
			if (null != conn) {
				conn.close();
			}
			flag = true;
		} catch (SQLException e) {
			e.fillInStackTrace();
		}
		return flag;
	}

	/**
	 * Execute update,delete,insert action.
	 * 
	 * @param sql
	 * @return true if the first result is a ResultSet object; false if it is an
	 *         update count or there are no results
	 */
	public boolean execute(String sql) {
		boolean result = false;
		try {
			st = conn.createStatement();
			result = st.execute(sql);
			st.close();
		} catch (SQLException e) {
			e.fillInStackTrace();
		}
		return result;
	}

	/**
	 * Executes the given SQL statement, which returns a single result which in a
	 * Map style.
	 * 
	 * @param sql
	 * @return a Map style record. which contains column name and value.
	 */
	public Map<String, Object> exceuteQueryOne(String sql) {
		List<Map<String, Object>> list = executeQuery(sql);
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * Executes the given SQL statement, which returns the query results every one
	 * in a Map style.
	 * 
	 * @param sql
	 * @return a List of Map which contain the column name and value of the table
	 *         data that contains the data produced by the given query;
	 */
	public List<Map<String, Object>> executeQuery(String sql) {
		List<Map<String, Object>> result = null;
		try {
			result = new ArrayList<Map<String, Object>>();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					String name = metaData.getColumnName(i);
					Object value = rs.getObject(name);
					map.put(name, value);
				}
				result.add(map);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.fillInStackTrace();
		}
		return result;
	}

	
	private DBUtil() {
		System.out.println(URL);
		System.out.println(userName);
		System.out.println(userPwd);
		if (null == conn) {
			try {
				Class.forName(driverName);
				conn = DriverManager.getConnection(URL,userName,userPwd);
			} catch (ClassNotFoundException e) {
				e.fillInStackTrace();
			} catch (SQLException e) {
				e.fillInStackTrace();
			}
		}
	}

	private DBUtil(String connString, String user, String password) {
		
		System.out.println(connString);
		System.out.println(user);
		System.out.println(password);
		if (null == conn) {
			try {
				Class.forName(driverName);
				conn = DriverManager.getConnection(connString, user, password);
			} catch (ClassNotFoundException e) {
				e.fillInStackTrace();
			} catch (SQLException e) {
				e.fillInStackTrace();
			}
		}
	}

}
