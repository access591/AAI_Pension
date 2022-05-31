package aims.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import aims.bean.EmpMasterBean;
import aims.bean.FinacialDataBean;
import aims.dao.CommonDAO;
import aims.dao.FinancialReportDAO;
import aims.dao.ImportDao;
/* 
##########################################
#Date					Developed by			Issue description
#24-Feb-2012			Radha					Correcting the changes due to seperation of methods  from FinancialReportDAO to CommonDAO
#########################################
*/
public class Pccalcualtion2010_11 {
	Log log = new Log(Pccalcualtion2010_11.class);	
public static void runPcprocess() throws Exception{  
	//public static void main(String args[]) throws Exception{
	ImportDao IDAO=new ImportDao();
	Log log = new Log(Pccalcualtion2010_11.class);
	for(int i=1;i<23975;i++){
		log.info("PFID is "+i);
		IDAO.pentionContributionProcess2008to11("", String.valueOf(i));
		
	}}
	
	public static void main(String args[]) throws Exception{
	CommonUtil commonUtil = new CommonUtil();
		 ArrayList list=new ArrayList();
		// list.add("01-Apr-2010");
		// list.add("01-May-2010");
		// list.add("01-Jun-2010");
		// list.add("01-Jul-2010");
		// list.add("01-Aug-2010");
		// list.add("01-Sep-2010");
		// list.add("01-Oct-2010");
		// list.add("01-Nov-2010");
		// list.add("01-Dec-2010");
		// list.add("01-Jan-2011");
		// list.add("01-Feb-2011");
		// list.add("01-Mar-2011");
		 list.add("01-Apr-2011");
		// list.add("01-May-2011");
		 
		 DBUtils commonDB = new DBUtils();
			Connection conn = commonDB.getConnection();
			ResultSet rs=null;
			Statement st = conn.createStatement();
		 for(int i=0;i<list.size();i++){
		String salaryMonth=list.get(i).toString();
		String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('"+salaryMonth+"','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days,to_char(add_months(dateofbirth, 697),'dd-Mon-yyyy')AS calPensionupto from employee_personal_info where  pensionno in(select pensionno from employee_pension_validate where monthyear between '"+salaryMonth+"' and LAST_DAY('"+salaryMonth+"') and edittrans='N' and region='CHQNAD' and empflag='Y') and wetheroption='A'  order by pensionno ";
		System.out.println(checkPFID);
		rs = st.executeQuery(checkPFID);
		while (rs.next()) {
		EmpMasterBean bean = null;
		String retirementDate="",dateofbirth="";
		String days = "0",pensionno="",wetheroption="";
		String calPensionupto = "";
		if (rs.getString("pensionno") != null) {
			pensionno = rs.getString("pensionno");
		} else {
			pensionno = "";
		}
		System.out.println("pensionno "+pensionno);
		 if (rs.getString("WETHEROPTION") != null) {
			wetheroption=rs.getString("WETHEROPTION");
		} else {
			wetheroption="";
		}
		if (rs.getString("REIREMENTDATE") != null) {
			retirementDate = rs.getString("REIREMENTDATE");
		} else {
			retirementDate = "";
		}
		if (rs.getString("dateofbirth") != null) {
			dateofbirth = rs.getString("dateofbirth");
		} else {
			dateofbirth = "";
		}
		
		if (rs.getString("days") != null) {
			days = rs.getString("days");
		} else {
			days = "0";
		}
		
		if (rs.getString("calPensionupto") != null) {
			calPensionupto = rs.getString("calPensionupto");
		} else {
			calPensionupto = "0";
		}
		
		 FinacialDataBean fdbean = getEmolumentsBean(conn,salaryMonth.trim(),pensionno);
		FinancialReportDAO fdao = new FinancialReportDAO();
		CommonDAO commonDAO = new CommonDAO();
		double calculatedPension = 0.00,pf=0.00;
		calculatedPension = commonDAO.calclatedPF(salaryMonth,
				calPensionupto, dateofbirth, String.valueOf(Double.parseDouble(fdbean.getEmpPfStatuary())*100/12),
				wetheroption, "", days,"1");
		calculatedPension =Math.round(calculatedPension);
		pf = Double.parseDouble(fdbean.getEmpPfStatuary().toString())- calculatedPension;
		String updatequery="update employee_pension_validate set pf='"+pf+"',pensioncontri='"+calculatedPension+"' where pensionno='"+pensionno+"' and monthyear between '"+salaryMonth+"' and LAST_DAY('"+salaryMonth+"') and airportcode='"+fdbean.getAirportCode()+"' and edittrans='N' ";
       // st.executeUpdate(updatequery);
		System.out.println(updatequery);
		st.addBatch(updatequery);
      }
		st.executeBatch();
		 }
}	

public static FinacialDataBean getEmolumentsBean(Connection con, String salaryMonth,String Pensionno) {

	String foundEmpFlag = "";
	Statement st = null;
	ResultSet rs = null;
	CommonUtil commonUtil = new CommonUtil();
	FinacialDataBean bean = new FinacialDataBean();
	try {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

		String transMonthYear = commonUtil.converDBToAppFormat(salaryMonth
				.trim(), "dd-MMM-yyyy", "-MMM-yy");
		String query="";
				query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,airportcode from employee_Pension_validate where to_char(monthyear,'dd-Mon-yy') like '%"
				+ transMonthYear
				+ "' and pensionno='"
				+ Pensionno
				+ "'  and empflag='Y' ";	
		st = con.createStatement();
		rs = st.executeQuery(query);

		if (rs.next()) {
			if (rs.getString("emoluments") != null) {
				bean.setEmoluments(rs.getString("emoluments"));
			}
			if (rs.getString("EMPPFSTATUARY") != null) {
				bean.setEmpPfStatuary(rs.getString("EMPPFSTATUARY"));
			}
			if (rs.getString("EMPVPF") != null) {
				bean.setEmpVpf(rs.getString("EMPVPF"));
			}
			if (rs.getString("EMPADVRECPRINCIPAL") != null) {
				bean.setPrincipal(rs.getString("EMPADVRECPRINCIPAL"));
			}
			if (rs.getString("EMPADVRECINTEREST") != null) {
				bean.setInterest(rs.getString("EMPADVRECINTEREST"));
			}
			if (rs.getString("PENSIONCONTRI") != null) {
				bean.setPenContri(rs.getString("PENSIONCONTRI"));
			}
			if (rs.getString("airportcode") != null) {
				bean.setAirportCode(rs.getString("airportcode"));
			}
		}
	} catch (SQLException e) {
		// e.printStackTrace();
		//log.printStackTrace(e);
	} catch (Exception e) {
		//log.printStackTrace(e);
		// e.printStackTrace();
	} finally {

		if (rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException se) {
				System.out.println("Problem in closing Resultset ");
			}
		}

		if (st != null) {
			try {
				st.close();
				st = null;
			} catch (SQLException se) {
				System.out.println("Problem in closing Statement.");
			}
		}

		// this.closeConnection(con, st, rs);
	}
	// log.info("PensionDAO :checkPensionDuplicate() leaving method");
	return bean;
}
}
