package aims.common;

import java.sql.Connection;
import java.sql.Statement;

import aims.dao.CommonDAO;
import aims.dao.FinancialReportDAO;
/* 
##########################################
#Date					Developed by			Issue description
#24-Feb-2012			Radha					Correcting the changes due to seperation of methods  from FinancialReportDAO to CommonDAO
#########################################
*/
public class TestMapping {
	FinancialReportDAO dao = new FinancialReportDAO();
	CommonDAO   commonDAO = new CommonDAO();
	//public static void main(String args[]) throws Exception {
	public static void mapping() throws Exception{
		TestMapping maping = new TestMapping();
		DBUtils commonDB = new DBUtils();

		Connection conn = commonDB.getConnection();
		Statement st = conn.createStatement();
		maping.getMapping(conn, st);
	}

	public void getMapping(Connection con, Statement st) {
		for (int i = 1; i <= 23000; i++) {
			String pfid = String.valueOf(i);

			String pfidWithRegion = commonDAO.getEmployeeMappingPFInfo(con, pfid,
					"", "");
			if (!pfidWithRegion.trim().equals("---")) {
				String[] pfIDLists = pfidWithRegion.split("=");
				System.out.println(pfIDLists);
				for (int j = 0; j < pfIDLists.length; j++) {
					String cpfacnowithRegion = pfIDLists[j].toString();
					System.out.println(cpfacnowithRegion.toString());
					if (cpfacnowithRegion != null && cpfacnowithRegion != "") {
						String cpfaccno = cpfacnowithRegion.substring(0,
								cpfacnowithRegion.indexOf(','));
						String region = cpfacnowithRegion.substring(
								cpfacnowithRegion.indexOf(',') + 1,
								cpfacnowithRegion.length());
						System.out.println("cpfaccno " + cpfaccno + "region "
								+ region);
						String updatequery = "update employee_pension_validate set pensionno="
								+ pfid
								+ " where cpfaccno='"
								+ cpfaccno.trim()
								+ "' and region='"
								+ region.trim()
								+ "' and pensionno is null and monthyear between '01-Apr-1995' and '31-Mar-2008'";
						try {
							st.executeUpdate(updatequery);
						} catch (Exception e) {

						}
					}
				}
			}
		}
	}
}
