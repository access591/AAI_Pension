package aims.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import aims.bean.DashBoardDetails;
import aims.bean.DashBoardInfo;
import aims.common.CommonUtil;
import aims.common.DBUtils;
import aims.common.Log;

public class DashBoardDAO {
	Log log = new Log(DashBoardDAO.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();
	FinancialReportDAO frDAO = new FinancialReportDAO();
	public DashBoardDetails getDashBoardInfo(String  finyear,String monthYear){
		log.info("DashBoardDAO:getDashBoardInfo Entering method");
		String month="--";
		if(!monthYear.equals("--")){
		month=monthYear.substring(3,6);
		}
		//log.info("month"+month);
		ArrayList dashBoardInfoList = new ArrayList();
		String sqlQuery = "",remarks="";
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection con=null;
		DashBoardInfo dbinfo=null;
		DashBoardDetails dbDetails=null;
		long emoluments=0l,epf=0l,vpf=0l,principal=0l,interest=0l;
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		//StringBuffer groupBy = new StringBuffer();
		sqlQuery="select round(sum(emoluments),0) as emoluments,round(sum(epf),0) as epf,round(sum(vpf),0) as vpf,round(sum(principal),0) as principal," +
				"round(sum(interest),0) as interest,round(sum(pf),0) as pf,round(sum(pensioncontri),0) as pensioncontri,a.region as region from (select * from epis_dashboard_data) a," +
				"(select * from epis_dashboard_finyear) b where a.finid = b.edid and b.edactive = 'Y' and b.edfinyear ='"+finyear+"'";
			
		if (!monthYear.equals("--")) { 
			//log.info("whereClause"+monthYear);
			whereClause.append(" a.monthyear = '"
					+ monthYear +"'");
		}
		query.append(sqlQuery);
		if (!monthYear.equals("--")){
		query.append(" AND ");
		}
		String whereqry=whereClause.toString();
		log.info("whereqry"+whereqry);
		query.append(whereqry);
		query.append(" group by a.region order by region");
		log.info("FinanceReportDAO::getDashBoardInfo" + query.toString());
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query.toString());
			boolean dataflag=false;
			while(rs.next()){
				dataflag=true;
				dbinfo=new DashBoardInfo();
				if(rs.getString("emoluments")!=null){
					dbinfo.setEmoluments(Long.parseLong(rs.getString("emoluments")));
				}
				if(rs.getString("epf")!=null){
					dbinfo.setEpf(Long.parseLong(rs.getString("epf")));
				}
				if(rs.getString("vpf")!=null){
					dbinfo.setVpf(Long.parseLong(rs.getString("vpf")));
				}
				if(rs.getString("principal")!=null){
					dbinfo.setPrincipal(Long.parseLong(rs.getString("principal")));
				}
				if(rs.getString("interest")!=null){
					dbinfo.setInterest(Long.parseLong(rs.getString("interest")));
				}
				if(rs.getString("region")!=null){
					dbinfo.setRegion(rs.getString("region"));
				}else{
					dbinfo.setRegion("---");
				}
				dbinfo.setRemarks(remarks);
				emoluments=emoluments+Long.parseLong(rs.getString("emoluments"));
				epf=epf+Long.parseLong(rs.getString("epf"));
				vpf=vpf+Long.parseLong(rs.getString("vpf"));
				principal=principal+Long.parseLong(rs.getString("principal"));
				interest=interest+Long.parseLong(rs.getString("interest"));
				dashBoardInfoList.add(dbinfo);	
			}
			if(dataflag==false){
				remarks="Data Not Available";
				String sqlQuery1 ="select distinct(region) as region from employee_unit_master order by region";
				rs1=st.executeQuery(sqlQuery1);
				while(rs1.next()){
					    dbinfo=new DashBoardInfo();
					if(rs1.getString("region")!=null){
						dbinfo.setRegion(rs1.getString("region"));
					}else{
						dbinfo.setRegion("---");
					}
					dbinfo.setEpf(epf);
					dbinfo.setVpf(vpf);
					dbinfo.setPrincipal(principal);
					dbinfo.setInterest(interest);
					dbinfo.setRemarks(remarks);
					dashBoardInfoList.add(dbinfo);
				}
			}
			dbDetails = new DashBoardDetails();
			dbDetails.setEmolumentstot(emoluments);
			dbDetails.setEpftot(epf);
			dbDetails.setVpftot(vpf);
			dbDetails.setPrincipal(principal);
			dbDetails.setInterest(interest);
			dbDetails.setMonthyear(month);
			dbDetails.setFinyear(finyear);
			dbDetails.setList(dashBoardInfoList);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs1);
			commonDB.closeConnection(null, st, rs);	
		}
		log.info("DashBoardDAO:getDashBoardInfo Leaving method");
		return dbDetails;
	}
	public DashBoardDetails getDashBoardPFWInfo(String  pfwfinyear,String pfwmonthYear,String paymentStatus){
		log.info("DashBoardDAO:getDashBoardPFWInfo Entering method");
		String pfwmonth="--";
		if(!pfwmonthYear.equals("--")){
		pfwmonth=pfwmonthYear.substring(3,6);
		}
		log.info("pfwmonth"+pfwmonth);
		ArrayList dashBoardInfoList = new ArrayList();
		String sqlQuery = "",remarks="";
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection con=null;
		DashBoardInfo dbinfo=null;
		DashBoardDetails dbDetails=null;
		long pfwcontriamt=0l,pfwsubamt=0l,advance=0l;
		long pfwcontriamtTot=0l,pfwsubamtTot=0l,advanceTot=0l;
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
	//	StringBuffer groupBy = new StringBuffer();
		sqlQuery="select round(sum(pfwsubamt),0) as pfwsubamt,round(sum(pfwcontriamt),0) as pfwcontriamt,round(sum(advance),0) as advance,a.region as region from (select * from epis_dashboard_data) a," +
				"(select * from epis_dashboard_finyear where edactive = 'Y') b where a.finid = b.edid and b.edfinyear = '"+pfwfinyear+"' and a.paymentsts = '"+paymentStatus+"'";
			
		if (!pfwmonthYear.equals("--")) { 
			//log.info("whereClause"+monthYear);
			whereClause.append(" AND a.monthyear = '"
					+ pfwmonthYear +"'");
		}
		query.append(sqlQuery);
		String whereqry=whereClause.toString();
		log.info("whereqry"+whereqry);
		query.append(whereqry);
		query.append(" group by a.region order by region");
		log.info("FinanceReportDAO::getDashBoardPFWInfo" + query.toString());
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query.toString());
			boolean dataflag=false;
			while(rs.next()){
				dataflag=true;
				pfwcontriamt=0l;pfwsubamt=0l;advance=0l;
				dbinfo=new DashBoardInfo();
				if(rs.getString("pfwsubamt")!=null){
					pfwsubamt=Long.parseLong(rs.getString("pfwsubamt"));
					dbinfo.setPfwsubamt(pfwsubamt);
				}
				if(rs.getString("pfwcontriamt")!=null){
					pfwcontriamt=Long.parseLong(rs.getString("pfwcontriamt"));
					dbinfo.setPfwcontriamt(pfwcontriamt);
				}
				if(rs.getString("advance")!=null){
					advance=Long.parseLong(rs.getString("advance"));
					dbinfo.setAdvance(advance);
				}
			
				if(rs.getString("region")!=null){
					dbinfo.setRegion(rs.getString("region"));
				}else{
					dbinfo.setRegion("---");
				}
				dbinfo.setRemarks(remarks);
				pfwsubamtTot=pfwsubamtTot+pfwsubamt;
				log.info(pfwsubamtTot+"  " +pfwsubamt);
				pfwcontriamtTot=pfwcontriamtTot+pfwcontriamt;
				advanceTot=advanceTot+advance;
				dashBoardInfoList.add(dbinfo);
			}
			if(dataflag==false){
				remarks="Data Not Available";
				String sqlQuery1 ="select distinct(region) as region from employee_unit_master order by region";
				rs1=st.executeQuery(sqlQuery1);
				while(rs1.next()){
					    dbinfo=new DashBoardInfo();
					if(rs.getString("region")!=null){
						dbinfo.setRegion(rs1.getString("region"));
					}else{
						dbinfo.setRegion("---");
					}
					dbinfo.setPfwsubamt(pfwsubamt);
					dbinfo.setPfwcontriamt(pfwcontriamt);
					dbinfo.setAdvance(advance);
					dbinfo.setRemarks(remarks);
					dashBoardInfoList.add(dbinfo);
				}
			}
			dbDetails = new DashBoardDetails();
			dbDetails.setPfwsubamt(pfwsubamtTot);
			dbDetails.setPfwcontriamt(pfwcontriamtTot);
			dbDetails.setAdvanceTot(advanceTot);
			dbDetails.setMonthyear(pfwmonth);
			dbDetails.setFinyear(pfwfinyear);
			dbDetails.setPaymentStatus(paymentStatus);
			dbDetails.setList(dashBoardInfoList);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs1);
			commonDB.closeConnection(null, st, rs);
		}
		log.info("DashBoardDAO:getDashBoardPFWInfo Leaving method");
		return dbDetails;
	}
	public DashBoardDetails getDashBoardFinalInfo(String  finalfinyear,String finalmonthYear,String finalpaymentStatus){
		log.info("DashBoardDAO:getDashBoardFinalInfo Entering method");
		String finalmonth="--";
		if(!finalmonthYear.equals("--")){
			finalmonth=finalmonthYear.substring(3,6);
		}
		log.info("pfwmonth"+finalmonth);
		ArrayList dashBoardInfoList = new ArrayList();
		String sqlQuery = "",remarks="";
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection con=null;
		DashBoardInfo dbinfo=null;
		DashBoardDetails dbDetails=null;
		long finalcontriamt=0l,finalsubamt=0l;
		long finalcontriamtTot=0l,finalsubamtTot=0l;
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
	//	StringBuffer groupBy = new StringBuffer();
		sqlQuery="select round(sum(FPSUBAMT),0) as FPSUBAMT,round(sum(FPCONTRIAMT),0) as FPCONTRIAMT,a.region as region from (select * from epis_dashboard_data) a," +
				"(select * from epis_dashboard_finyear where edactive = 'Y') b where a.finid = b.edid and b.edfinyear = '"+finalfinyear+"' and a.paymentsts = '"+finalpaymentStatus+"'";
			
		if (!finalmonthYear.equals("--")) { 
			//log.info("whereClause"+monthYear);
			whereClause.append(" AND a.monthyear = '"
					+ finalmonthYear +"'");
		}
		query.append(sqlQuery);
		String whereqry=whereClause.toString();
		log.info("whereqry"+whereqry);
		query.append(whereqry);
		query.append(" group by a.region order by region");
		log.info("FinanceReportDAO::getDashBoardFinalInfo" + query.toString());
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query.toString());
			boolean dataflag=false;
			while(rs.next()){
				finalcontriamt=0l;finalsubamt=0l;
				dataflag=true;
				dbinfo=new DashBoardInfo();
				if(rs.getString("FPSUBAMT")!=null){
					finalsubamt=Long.parseLong(rs.getString("FPSUBAMT"));
					dbinfo.setFinalsubamt(finalsubamt);
				}
				if(rs.getString("FPCONTRIAMT")!=null){
					finalcontriamt=Long.parseLong(rs.getString("FPCONTRIAMT"));
					dbinfo.setFinalcontriamt(finalcontriamt);
				}
			
				if(rs.getString("region")!=null){
					dbinfo.setRegion(rs.getString("region"));
				}else{
					dbinfo.setRegion("---");
				}
				dbinfo.setRemarks(remarks);
				finalsubamtTot=finalsubamtTot+finalsubamt;
				finalcontriamtTot=finalcontriamtTot+finalcontriamt;
		
				dashBoardInfoList.add(dbinfo);
			}
			if(dataflag==false){
				remarks="Data Not Available";
				String sqlQuery1 ="select distinct(region) as region from employee_unit_master order by region";
				rs1=st.executeQuery(sqlQuery1);
				while(rs1.next()){
					    dbinfo=new DashBoardInfo();
					if(rs.getString("region")!=null){
						dbinfo.setRegion(rs1.getString("region"));
					}else{
						dbinfo.setRegion("---");
					}
					dbinfo.setFinalsubamt(finalsubamt);
					dbinfo.setFinalcontriamt(finalcontriamt);
				
					dbinfo.setRemarks(remarks);
					dashBoardInfoList.add(dbinfo);
				}
			}
			dbDetails = new DashBoardDetails();
			dbDetails.setFinalsubamtTot(finalsubamtTot);
			dbDetails.setFinalcontriamtTot(finalcontriamtTot);
	
			dbDetails.setMonthyear(finalmonth);
			dbDetails.setFinyear(finalfinyear);
			dbDetails.setFinalPaymentStatus(finalpaymentStatus);
			dbDetails.setList(dashBoardInfoList);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs1);
			commonDB.closeConnection(null, st, rs);
		}
		log.info("DashBoardDAO:getDashBoardFinalInfo Leaving method");
		return dbDetails;
	}
	String sTokenFormat(StringBuffer stringBuffer) {
		StringBuffer whereStr = new StringBuffer();
		StringTokenizer st = new StringTokenizer(stringBuffer.toString());
		int count = 0;
		int stCount = st.countTokens();
		// && && count<=st.countTokens()-1st.countTokens()-1
		while (st.hasMoreElements()) {
			count++;
			if (count == stCount)
				break;
			whereStr.append(st.nextElement());
			whereStr.append(" ");
		}
		return whereStr.toString();
	}
}
