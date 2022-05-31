package aims.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserAccessRights {

	/**
	 * @param args
	 */
	static Log log = new Log(UserAccessRights.class); 
	 
	public static  String getAccess(String keyFieldValue,String keyFieldValue1) 
	{
		Connection con=null;
		Statement st = null;
		String status = "";
		ResultSet rs = null;
		String sqlQuery=""; 
		try{ 
		con=DBUtils.getConnection();
		st = con.createStatement();
		sqlQuery="select * from PENSION_USER_ACCESS_MT where USERID ='"+keyFieldValue+"' AND ACCESS_CD='"+keyFieldValue1+"' ";
		System.out.println("---In getAccess()-----"+sqlQuery);
		rs = st.executeQuery(sqlQuery);
		if(rs.next())
		{
			status= "Y";
		}
		else
		{
			status = "N";
		}
		if(rs!=null) rs.close();
		if(st!=null) st.close();
		if(con!=null) con.close();
		}catch (Exception e){	
			e.printStackTrace();
		}
		
		return(status);
	}
	
	public static  Map  getUserAccessRights(String userId) 
	{
		Connection con=null;
		Statement st = null;	 
		ResultSet rs = null;
		String sqlQuery="";
		int i=0;
		Map userRights = new LinkedHashMap();
		try{ 
		con=DBUtils.getConnection();
		st = con.createStatement();
		sqlQuery="select ACCESS_CD from PENSION_USER_ACCESS_MT where USERID ='"+userId+"' ";
		System.out.println("---In getUserAccessRights()-----"+sqlQuery);
		rs = st.executeQuery(sqlQuery);
		if(rs.next())
		{
			userRights.put(Integer.toString(i),rs.getString("ACCESS_CD"));	
		}
		if(rs!=null) rs.close();
		if(st!=null) st.close();
		if(con!=null) con.close();
		}catch (Exception e){	
			e.printStackTrace();
		}
		
		return userRights;
	}

 

}
