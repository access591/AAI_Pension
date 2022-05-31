package aims.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class OCIConnectionPool
{
	public static void main(String[] args) {
		getOCIConnection();
	}
  public static Connection getOCIConnection()
  {
	  System.out.println("88888888888888888888");
    Connection con = null;
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      //con = DriverManager.getConnection(url, user, password)
      con = DriverManager.getConnection("jdbc:oracle:oci:olcms/olcms@aims11");
      System.out.println("88888888888888888888"+con);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (con != null) {
      return con;
    }
    return null;
  }
}