package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import aims.common.DBUtils;

public class MyTest {

	public Statement  getConetion() throws Exception { 
		
		DBUtils commonDB = new DBUtils();
		Connection con = commonDB.getConnection();
		
		Statement pst = con.createStatement();
		
		return pst;
	}
	
	public static void main(String[] arg) {
		
		new MyTest().testQuery();
		
	}
	
	
	public void testQuery() {
		String qur = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-mm-yyyy')AS REIREMENTDATE from employee_personal_info where to_char(pensionno)='1124'";
		
		
		String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yy\r\n" + 
				"yy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('\r\n" + 
				"1-Jan-2017','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon\r\n" + 
				"-RRRR')+1 as days from employee_personal_info where to_char(pensionno)='1124'";
		
		try {
			ResultSet rs = getConetion().executeQuery(qur);
			while(rs.next()) {
				System.out.println("re value : " + rs.getString(1));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
