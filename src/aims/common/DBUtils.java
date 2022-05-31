/**
  * File       : DBUtils.java
  * Date       : 08/07/2007
  * Author     : AIMS 
  * Description: 
  * Copyright (2007) by the Navayuga Infotech, all rights reserved.
  */
package aims.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.sql.DataSource;

public class DBUtils {

	public static final int TODAY = 1;

	public static final int LAST_WEEK = 2;

	public static final int LAST_MONTH = 3;

	public static final int LAST_YEAR = 4;

	public static final int DATE_RANGE = 5;

	/**
	 * It'll get connection from driver class.
	 * 
	 * @return
	 */
	public static Connection getConnection() throws Exception {
		Log log = new Log(DBUtils.class);
		Connection conn = null;
		try {
			
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.DbProperties");
			String driverClassName = bundle.getString("oracle.drivers");
			String url = bundle.getString("oracle.access.url");
			String username = bundle.getString("oracle.access.user");
			String pwd = bundle.getString("oracle.access.password");

			Class.forName(driverClassName.trim());
			conn = DriverManager.getConnection(url.trim(), username.trim(), pwd
					.trim());
		

		} catch (ClassNotFoundException ex) {
			System.out.println(ex.toString());

		} catch (SQLException ex) {
			System.out.println(ex.toString());

		} catch (Exception ex) {
			System.out.println(ex.toString());

		}

		return conn;
	}

	/**
	 * It'll get connection from datasource
	 * 
	 * @param dataSource
	 * @return
	 */
	public static Connection getConnection(DataSource dataSource)
			throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException ex) {

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return conn;
	}

	/**
	 * It'll close connection.
	 * 
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {

			// throw new
			// UtilityException("2010",CommonUtil.getExceptionMessage("2010"));
		} catch (Exception ex) {

			// throw new
			// UtilityException("1013",CommonUtil.getExceptionMessage("1013")+ex.getMessage());
		}
	}
	public void closeConnection(Connection con, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException se) {
				System.out.println("Problem in closing Resultset ");
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException se) {
				System.out.println("Problem in closing Statement.");
			}
		}

		try {
			if (con != null && !con.isClosed()) {
				con.close();
				con = null;
			}
		} catch (SQLException se) {
			System.out.println("Problem in closing Connection.");
		}

	}
}
