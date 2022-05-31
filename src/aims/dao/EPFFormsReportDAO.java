package aims.dao;

import java.io.File;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import aims.bean.StationWiseRemittancebean;
import aims.bean.epfforms.AAIEPFReportBean;
import aims.bean.epfforms.AaiEpfForm11Bean;
import aims.bean.epfforms.AaiEpfform3Bean;
import aims.bean.epfforms.ControlAccountForm2Info;
import aims.bean.epfforms.RemittanceBean;
import aims.common.CommonUtil;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;

/*
 ##########################################
 #Date					Developed by			Issue description
 #09-Oct-2012 			Radha 					For adding -ve pensionContri values given in Supppli data in normal  wages  side IN getForm6AEChallanReport()
 #14-Jun-2012 			Prasad					Getting original Emoluments for EDLI Inspection Charges 
 #07-Jun-2012			Radha		 			For getting   NCPDAYS in ECR Report
 #01-Jun-2012			Radha		 			For Making Region & Station Wise ECR Report   
 #30-May-2012           Prasad	                Added station wise remittanc screen
 #21-May-2012           Radha                   Changed as ECHALLAN REPORT AS PER New Joinee,SAU With Stations added CONDITONs
 #16-May-2012           Radha                   Changed as ECHALLAN REPORT AS PER AAI EPF-4 AND ADDED CONDITON
 #02-May-2012           Suresh                  Changed as ECHALLAN REPORT AS PER AAI EPF-3 AND ADDED CONDITON
 #30-Apr-2012           Suresh                  Added new fields in  RPFC E-FORM-6A 
 #27-Apr-2012           Suresh                  Added new report for  RPFC E-FORM-6A 
 #19-Apr-2012           Suresh                  Added new condition  RPFC FORM-6A i..e,after 58 we can pick the arrears data
 #10-Apr-2012           Suresh                  Changed AAI EPF-5 Query
 #15-Mar-2012           Suresh                  Rectify the delay of form-6a report
 #24-Feb-2012			Radha					Correcting the changes due to seperation of methods  from FinancialReportDAO to CommonDAO
 #31-Jan-2012			Prasad					For Added uploaded date in AAIEPF-3 Suppliblock
 #23-Dec-2011  		    Prasad P                Add uploaded time in supplementary tab raised by shika jain
 #23-Dec-2011  		    Radha                   For getting advanceAmnt_withoutkey  value in AAIEPFForm8SummaryReport
 #15-Dec-2011			Radha                   AAI EPF Form 8 Summary Report For Tallying Advances,Loans & Final Setlement Amnts from RPFC to Cask Book
 #07-Dec-2011        	Radha			        Form2 Separating from cntrl acc relavent changes 
 #########################################
 */
public class EPFFormsReportDAO {
	Log log = new Log(EPFFormsReportDAO.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();

	FinancialReportDAO finReportDAO = new FinancialReportDAO();

	public ArrayList getEPFForm3Report(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", fromMnth = "", toMnth = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form3DataList = new ArrayList();
		String regionDesc = "", stationDesc = "", mergerFlag = "", suppliFlag = "", pensionoNo = "", monthYear = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
		selPerQuery = this.buildQueryEmpPFInfo(range, regionDesc, stationDesc,
				empNameFlag, empName, sortingOrder, frmPensionno);
		selTransQuery = this.buildQueryEmpPFTransForm3Info(regionDesc,
				stationDesc, sortingOrder, fromDate, toDate);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE,EPV.MERGEFLAG AS MERGEFLAG,EPV.SUPPLIFLAG as SUPPLIFLAG, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.ADDITIONALCONTRI AS ADDITIONALCONTRI FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y'  ORDER BY EMPFID."
				+ sortingOrder;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm3Report===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();

				if (rs.getString("SUPPLIFLAG") != null) {
					suppliFlag = rs.getString("SUPPLIFLAG");
				} else {
					suppliFlag = "N";
				}
				if (rs.getString("MONTHYEAR") != null) {
					monthYear = CommonUtil.getDatetoString(rs
							.getDate("MONTHYEAR"), "dd-MMM-yyyy");

				}
				if (rs.getString("PENSIONNO") != null) {
					pensionoNo = rs.getString("PENSIONNO");

				}

				if (suppliFlag.equals("Y")) {

					epfForm3Bean = this.getEPFForm3EmpSuppliData(pensionoNo,
							monthYear, epfForm3Bean, con);

				} else {
					epfForm3Bean = this
							.fillEPFForm3Properties(rs, epfForm3Bean);
				}

				if (epfForm3Bean != null) {
					pfstatutury = Double.parseDouble(epfForm3Bean
							.getEmppfstatury());
					subTotal = new Double(df.format(Double
							.parseDouble(epfForm3Bean.getEmppfstatury().trim())
							+ Double.parseDouble(epfForm3Bean.getAdditionalContri().trim())
							+ Double.parseDouble(epfForm3Bean.getEmpvpf()
									.trim())
							+ Double.parseDouble(epfForm3Bean.getPrincipal()
									.trim())
							+ Double.parseDouble(epfForm3Bean.getInterest()
									.trim()))).doubleValue();
					epfForm3Bean
							.setSubscriptionTotal(Double.toString(subTotal));
					conTotal = new Double(df.format(Double
							.parseDouble(epfForm3Bean.getPensionContribution()
									.trim())
							+ Double.parseDouble(epfForm3Bean.getPf().trim())))
							.doubleValue();

					epfForm3Bean
							.setContributionTotal(Double.toString(conTotal));
					diff = pfstatutury - conTotal;
					epfForm3Bean.setFormDifference(Double.toString(Math
							.round(diff)));
					if (diff != 0) {
						epfForm3Bean.setHighlightedColor("bgcolor='lightblue'");
					} else {
						epfForm3Bean.setHighlightedColor("");
					}

					form3DataList.add(epfForm3Bean);
				}

			}
			form3DataList = this.getEPFForm3MergerData(form3DataList, con,
					selPerQuery, fromDate, toDate, regionDesc, stationDesc,
					sortingOrder, frmPensionno);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}
	
	public ArrayList getExecutiveReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", fromMnth = "", toMnth = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		ArrayList form3DataList = new ArrayList();
		
	log.info("airprotcode:"+airprotcode+"region:"+region);
		
		selQuery = "select i.employeeno as employeeno,i.newempcode as newempcode,i.department as discipline,i.employeename as employeename,i.pensionno as pensionno,i.dateofbirth as dateofbirth,i.dateofjoining as dateofjoining," +
				"i.dateofseperation_date as dateofseperation_date,i.dateofseperation_reason as dateofseperation_reason," +
				"v.monthyear as monthyear,v.emoluments as emoluments,(100+d.darate) as basicdarete,d.darate as darate,round(nvl(v.emoluments,0)-((d.darate/(100+d.darate))*nvl(v.emoluments,0))) as basic," +

				"v.desegnation as desegnation,i.desegnation as currentDesegnation,i.emp_level as emp_level,v.airportcode as airportcode,v.region as region,v.arrearflag as arrear from employee_pension_validate v, employee_personal_info i," +
				"employee_darates d where v.pensionno = i.pensionno and v.empflag = 'Y' " +
				//"and i.emp_level in ('E-02', 'E-08', 'E-1', 'E-2', 'E-3', 'E-4', 'E-5','E-6', 'E-7', 'E-8', 'E-9','E-m','')" +
				" and" +

				//"v.desegnation as desegnation,i.desegnation as currentDesegnation,i.emp_level as emp_level,v.airportcode as airportcode,v.region as region,v.arrearflag as arrear from employee_pension_validate v, employee_personal_info i," +
				//"employee_darates d where v.pensionno = i.pensionno and v.empflag = 'Y' and i.emp_level in ('E-02', 'E-08', 'E-1', 'E-2', 'E-3', 'E-4', 'E-5','E-6', 'E-7', 'E-8', 'E-9') and" +

				" v.monthyear=d.monthyear and v.monthyear between '"+fromDate+"' and '"+toDate+"' and ( i.dateofseperation_date is null or i.dateofseperation_date>'30-Apr-2010') " ;
		
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !frmPensionno.equals("")) {
				whereClause.append("i.PENSIONNO='" + frmPensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		if (!airprotcode.equals("NO-SELECT")) {
			whereClause.append("v.airportcode='" + airprotcode + "' ");
			whereClause.append(" AND ");
		}
		if (!region.equals("NO-SELECT")) {
			whereClause.append("v.region='" + region + "' ");
			whereClause.append(" AND ");
		}
		query.append(selQuery);

		
		if (!airprotcode.equals("NO-SELECT") || !region.equals("NO-SELECT") || empNameFlag.equals("true")) {

			query.append(" and ");
		}
			query.append(commonDAO.sTokenFormat(whereClause));
		

		String orderBy = " ORDER BY " + sortingOrder;
		query.append(orderBy);
		selQuery = query.toString();
		
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm3Report===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
		
			while (rs.next()) {
			
				epfForm3Bean = new AaiEpfform3Bean();
				
				if(rs.getString("employeeno")!=null){
					epfForm3Bean.setEmployeeNo(rs.getString("employeeno"));
				}else{
					epfForm3Bean.setEmployeeNo("");
				}
				
				if(rs.getString("newempcode")!=null){
					epfForm3Bean.setEmpCode(rs.getString("newempcode"));
				}else{
					epfForm3Bean.setEmpCode("");
				}
				
				if(rs.getString("discipline")!=null){
					epfForm3Bean.setDiscipline(rs.getString("discipline"));
				}else{
					epfForm3Bean.setDiscipline("");
				}
				
				if(rs.getString("employeename")!=null){
					epfForm3Bean.setEmployeeName(rs.getString("employeename"));
				}else{
					epfForm3Bean.setEmployeeName("");
				}

				if(rs.getString("pensionno")!=null){
					epfForm3Bean.setPfID(rs.getString("pensionno"));
				}else{
					epfForm3Bean.setPfID("");
				}
				if(rs.getString("arrear")!=null){
					epfForm3Bean.setArrearflag(rs.getString("arrear"));
				}else{
					epfForm3Bean.setArrearflag("");
				}
				
				
			   if (rs.getString("dateofbirth") != null) {
					epfForm3Bean.setDateOfBirth( CommonUtil.getDatetoString(rs
							.getDate("dateofbirth"), "dd-MMM-yyyy"));

				}else
				{
					epfForm3Bean.setDateOfBirth("");
				}
			   
			   if (rs.getString("dateofjoining") != null) {
					epfForm3Bean.setDateofJoining( CommonUtil.getDatetoString(rs
							.getDate("dateofjoining"), "dd-MMM-yyyy"));

				}else
				{
					epfForm3Bean.setDateofJoining("");
				}
			   
			   
			   if (rs.getString("dateofseperation_date") != null) {
					epfForm3Bean.setSeperationDate( CommonUtil.getDatetoString(rs
							.getDate("dateofseperation_date"), "dd-MMM-yyyy"));

				}else
				{
					epfForm3Bean.setSeperationDate("");
				}
			   
			   if (rs.getString("dateofseperation_reason") != null) {
					epfForm3Bean.setSeperationreason(rs.getString("dateofseperation_reason"));

				}else
				{
					epfForm3Bean.setSeperationreason("");
				}
			   
			   if (rs.getString("monthyear") != null) {
					epfForm3Bean.setMonthyear( CommonUtil.getDatetoString(rs
							.getDate("monthyear"), "dd-MMM-yyyy"));

				}else
				{
					epfForm3Bean.setMonthyear("");
				}
			   
			   if (rs.getString("emoluments") != null) {
					epfForm3Bean.setEmoluments(rs.getString("emoluments"));

				}else
				{
					epfForm3Bean.setEmoluments("");
				}
			   if (rs.getString("basicdarete") != null) {
					epfForm3Bean.setBasicRate(rs.getString("basicdarete"));

				}else
				{
					epfForm3Bean.setBasicRate("");
				}
			   
			   if (rs.getString("darate") != null) {
					epfForm3Bean.setDarate(rs.getString("darate"));

				}else
				{
					epfForm3Bean.setDarate("");
				}
			   
			   if (rs.getString("basic") != null) {
					epfForm3Bean.setBasic(rs.getString("basic"));

				}else
				{
					epfForm3Bean.setBasic("");
				}
			  
			   if (rs.getString("desegnation") != null) {
					epfForm3Bean.setDesignation(rs.getString("desegnation"));

				}else
				{
					epfForm3Bean.setDesignation("");
				}
			   
			   if (rs.getString("currentDesegnation") != null) {
					epfForm3Bean.setCurrentDesg(rs.getString("currentDesegnation"));

				}else
				{
					epfForm3Bean.setCurrentDesg("");
				}
			   
			   if (rs.getString("emp_level") != null) {
					epfForm3Bean.setEmplevel(rs.getString("emp_level"));

				}else
				{
					epfForm3Bean.setEmplevel("");
				}
			   
			   if (rs.getString("airportcode") != null) {
					epfForm3Bean.setStation(rs.getString("airportcode"));

				}else
				{
					epfForm3Bean.setStation("");
				}
			   
			   if (rs.getString("region") != null) {
					epfForm3Bean.setRegion(rs.getString("region"));

				}else
				{
					epfForm3Bean.setRegion("");
				}
			   
			   		form3DataList.add(epfForm3Bean);
				}

			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}
	public ArrayList getEPFForm4Report(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno,String select_month) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", fromMnth = "", toMnth = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm4Bean = null;
		ArrayList form4DataList = new ArrayList();
		String regionDesc = "", stationDesc = "", mergerFlag = "", suppliFlag = "", pensionoNo = "", monthYear = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
		selPerQuery = this.buildQueryEmpPFInfo(range, regionDesc, stationDesc,
				empNameFlag, empName, sortingOrder, frmPensionno);
		selTransQuery = this.buildQueryEmpPFTransForm4Info(regionDesc,
				stationDesc, sortingOrder, fromDate, toDate,select_month);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE,'' AS MERGEFLAG,EPV.SUPPLIFLAG as SUPPLIFLAG, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,'0' as ADDITIONALCONTRI FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y'  ORDER BY EMPFID."
				+ sortingOrder;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm3Report===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				double receievedOtherOrgsubTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm4Bean = new AaiEpfform3Bean();

				if (rs.getString("MONTHYEAR") != null) {
					monthYear = CommonUtil.getDatetoString(rs
							.getDate("MONTHYEAR"), "dd-MMM-yyyy");

				}
				if (rs.getString("PENSIONNO") != null) {
					pensionoNo = rs.getString("PENSIONNO");
				}

				epfForm4Bean = this.fillEPFForm3Properties(rs, epfForm4Bean);

				if (epfForm4Bean != null) {
					pfstatutury = Double.parseDouble(epfForm4Bean
							.getEmppfstatury());
					receievedOtherOrgsubTotal = new Double(df.format(Double
							.parseDouble(epfForm4Bean.getEmppfstatury().trim())
							+ Double.parseDouble(epfForm4Bean.getEmpvpf()
									.trim())
							+ Double.parseDouble(epfForm4Bean.getPrincipal()
									.trim())
							+ Double.parseDouble(epfForm4Bean.getInterest()
									.trim())
							+ Double.parseDouble(epfForm4Bean.getPf().trim())))
							.doubleValue();

					epfForm4Bean.setOtherOrgSubTotal(Double
							.toString(receievedOtherOrgsubTotal));
					conTotal = new Double(df.format(Double
							.parseDouble(epfForm4Bean.getPensionContribution()
									.trim())
							+ Double.parseDouble(epfForm4Bean.getPf().trim())))
							.doubleValue();

					epfForm4Bean
							.setContributionTotal(Double.toString(conTotal));
					diff = pfstatutury - conTotal;
					epfForm4Bean.setFormDifference(Double.toString(Math
							.round(diff)));
					if (diff != 0) {
						epfForm4Bean.setHighlightedColor("bgcolor='lightblue'");
					} else {
						epfForm4Bean.setHighlightedColor("");
					}

					form4DataList.add(epfForm4Bean);
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form4DataList;
	}
	//By radha on 29-Apr-2012 for getting  data >58 years also
	public ArrayList getForm6AReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", dateOfRetriment = "", days = "",selTransArrearsQuery="",selPerQry1="";
		long transMntYear = 0, empRetriedDt = 0;
		boolean chkDOBFlag = false;
		double retriredEmoluments = 0.0;
		int getDaysBymonth = 0;

		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		AaiEpfform3Bean epfForm3Bean = null;
		String calEmoluments = "";
		ArrayList form3DataList = new ArrayList();
		String regionDesc = "", stationDesc = "",remarksAfter58=">58 Years",empRecovryRemarks="";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
	/*	selPerQuery = this.buildQueryForm6AEmpPFInfo(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				fromDate);*/
		selPerQuery = this.buildQueryForm6AEmpPFInfo(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				fromDate);
		selTransQuery = this.buildQueryEmpPFTransInfo(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, NVL(EMPFID.FHFLAG,'-')  as FHFLAG, EMPFID.GENDER as GENDER, EPV.EMPLOYEENO as EMPLOYEENO,EMPFID.DATEOFENTITLE AS DATEOFENTITLE, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.EMPRECOVERYSTS AS EMPRECOVERYSTS,EPV.ARREARFLAG AS ARREARFLAG FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFInfo===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

				}

				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				if (rs.getString("ARREARFLAG") != null) {
					epfForm3Bean.setArrearflag(rs.getString("ARREARFLAG"));
				} else {
					epfForm3Bean.setDesignation("N");
				}
				if (rs.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(personalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {

					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				// log.info("Pension
				// No"+epfForm3Bean.getPensionno()+"Emoluments"
				// +epfForm3Bean.getEmoluments()+"Pension Contribution"+
				// epfForm3Bean.getPensionContribution());

				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs.getString("EMPRECOVERYSTS") != null) {
					if(rs.getString("EMPRECOVERYSTS").equals("CPF")){
						empRecovryRemarks="Regular";
					}else if(rs.getString("EMPRECOVERYSTS").equals("DEP")){
						empRecovryRemarks="Deputation";
					}else if(rs.getString("EMPRECOVERYSTS").equals("RET")){
						empRecovryRemarks="Retirement";
					}
					epfForm3Bean.setRemarks(empRecovryRemarks);
				} else{
					epfForm3Bean.setRemarks("");
				}
				calEmoluments = finReportDAO.calWages(epfForm3Bean
						.getEmoluments(), epfForm3Bean.getMonthyear(),
						epfForm3Bean.getWetherOption().trim(), false, false,
						"1");
				transMntYear = Date.parse(epfForm3Bean.getMonthyear());
				dateOfRetriment = commonDAO.getRetriedDate(epfForm3Bean
						.getDateOfBirth());

				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear == empRetriedDt) {

					chkDOBFlag = true;
				}
				if (chkDOBFlag == true) {
					String[] dobList = epfForm3Bean.getDateOfBirth().split("-");
					days = dobList[0];

					getDaysBymonth = commonDAO.getNoOfDays(epfForm3Bean
							.getDateOfBirth());

					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
				}
				epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
						.parseDouble(calEmoluments))));
				chkDOBFlag=false;
				form3DataList.add(epfForm3Bean);

			}
			
			//for >58 data
			String selPerQryAftr58 = this.buildQueryForm6AEmpPFAfter58Info(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
			selTransArrearsQuery = this.buildQueryEmpPFTransArrearInfo(regionDesc, stationDesc,
					sortingOrder, fromDate, toDate);
			
			selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, NVL(EMPFID.FHFLAG,'-') as FHFLAG, EMPFID.GENDER as GENDER, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.ARREARFLAG AS ARREARFLAG  FROM ( "
				+ selTransArrearsQuery
				+ ") EPV,("
				+ selPerQryAftr58
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
	 
			 
			st1 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFInfo== For SuppliData=selQuery"
					+ selQuery);
			rs1 = st1.executeQuery(selQuery);
		 
			while (rs1.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs1.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs1.getString("PENSIONNO"));

				}

				if (rs1.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs1.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}
				if (rs1.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs1.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs1.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs1.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				if (rs1.getString("ARREARFLAG") != null) {
					epfForm3Bean.setArrearflag(rs1.getString("ARREARFLAG"));
				} else {
					epfForm3Bean.setDesignation("N");
				}
				if (rs1.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs1.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs1.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs1.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs1.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs1.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs1.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs1.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs1.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs1.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs1.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs1.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs1.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(CommonUtil.converDBToAppFormat(rs1
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs1.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs1.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0");
				}
				if (rs1.getString("EMPPFSTATUARY") != null) {

					epfForm3Bean.setEmppfstatury(rs1.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				if (rs1.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs1.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs1.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs1
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs1.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs1.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs1.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs1
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				// log.info("Pension
				// No"+epfForm3Bean.getPensionno()+"Emoluments"
				// +epfForm3Bean.getEmoluments()+"Pension Contribution"+
				// epfForm3Bean.getPensionContribution());

				if (rs1.getString("PF") != null) {
					epfForm3Bean.setPf(rs1.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				if (rs1.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs1.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs1.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs1.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}

				calEmoluments = finReportDAO.calWages(epfForm3Bean
						.getEmoluments(), epfForm3Bean.getMonthyear(),
						epfForm3Bean.getWetherOption().trim(), false, false,
						"1");
				transMntYear = Date.parse(epfForm3Bean.getMonthyear());
				dateOfRetriment = commonDAO.getRetriedDate(epfForm3Bean
						.getDateOfBirth());

				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear == empRetriedDt) {

					chkDOBFlag = true;
				}
				if (chkDOBFlag == true) {
					String[] dobList = epfForm3Bean.getDateOfBirth().split("-");
					days = dobList[0];

					getDaysBymonth = commonDAO.getNoOfDays(epfForm3Bean
							.getDateOfBirth());

					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
				}
				epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
						.parseDouble(calEmoluments))));
				chkDOBFlag=false;
				
				epfForm3Bean.setRemarks(remarksAfter58);
				form3DataList.add(epfForm3Bean);

			}
			
			
			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}
//	 By Radha on 17-Jul-2012 for getting   Freezed ECR Report
	public ArrayList getFreezedForm6AEChallanReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", dateOfRetriment = "", days = "",selTransArrearsQuery="",selPerQry1="";
		long transMntYear = 0, empRetriedDt = 0;
		boolean chkDOBFlag = false;
		double retriredEmoluments = 0.0;
		int getDaysBymonth = 0;

		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;		 
		ResultSet rs = null;
		 
		AaiEpfform3Bean epfForm3Bean = null;
		String calEmoluments = "";
		ArrayList form3DataList = new ArrayList();
		ArrayList seperationDataList = new ArrayList();
		ArrayList stationList = new ArrayList();
		String regionDesc = "", staionsStr="", stationDesc = "",remarksAfter58=">58 Years",empRecovryRemarks="",accountType="",station="",pensionCmplteDate="";
		double dojAge=0, pensionAge =0;
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			if(!regionDesc.equals("")){
				staionsStr = commonUtil.getRAUAirports(regionDesc.toUpperCase());
					if((staionsStr.equals("")) && (regionDesc.toUpperCase().equals("CHQNAD"))){
						stationDesc="CHQNAD";
					}else{
						stationDesc=  staionsStr.substring(0, staionsStr.lastIndexOf("','"));
					}
				}else{
			stationDesc = "";
			}
		}
		
		log.info("==region=="+region+"==airprotcode=="+airprotcode+"=regionDesc="+regionDesc+"==stationDesc=="+stationDesc);
		selQuery = this.buildQueryFreezedChallan(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				fromDate);		 
		 
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getFreezedForm6AEChallanReport===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				station="";
				String regularSalFlag="N";
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0,emoluments=0,pf=0,pensionContri=0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

				}

				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}				 
				if (rs.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}				 
				if (rs.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFENTITLE")));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(personalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
					epfForm3Bean.setOriginalEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setOriginalEmoluments("0");
					epfForm3Bean.setEmoluments("0");
				}
				 
				if (rs.getString("AEMOLUMENTS") != null) {
					epfForm3Bean.setNonCPFEmoluments(rs.getString("AEMOLUMENTS"));
				} else {
					epfForm3Bean.setNonCPFEmoluments("0.00");
				}
				 
				if (rs.getString("APENSIONCONTRI") != null) {
					epfForm3Bean.setNonCPFPC(rs.getString("APENSIONCONTRI"));
				} else {
					epfForm3Bean.setNonCPFPC("0.00");
				}
				 

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				pensionContri = Double.parseDouble(epfForm3Bean.getPensionContribution());
				 
				 
				 
				if (rs.getString("REASONFORLEAVING") != null) {
					epfForm3Bean.setSeperationreason(rs.getString("REASONFORLEAVING"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				  
				 
				 
				if (rs.getString("NCPDAYS") != null) {
					epfForm3Bean.setNCPDays(rs.getString("NCPDAYS"));

				}else{
					epfForm3Bean.setNCPDays("0");
				}
				
				epfForm3Bean.setFormDifference("0");
				form3DataList.add(epfForm3Bean);

			}
			
		  
			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			 
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}
	public Map getEDLIInspectionChargesReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", dateOfRetriment = "", days = "",selTransArrearsQuery="",selPerQry1="";
		long transMntYear = 0, empRetriedDt = 0;
		boolean chkDOBFlag = false;
		double retriredEmoluments = 0.0;
		int getDaysBymonth = 0;

		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		AaiEpfform3Bean epfForm3Bean = null;
		String calEmoluments = "";
		ArrayList form3DataList = new ArrayList();
		ArrayList seperationDataList = new ArrayList();
		ArrayList seperationList = new ArrayList();
		ArrayList newJoinList = new ArrayList();
		Map map = new LinkedHashMap();
		String regionDesc = "", staionsStr="", stationDesc = "",remarksAfter58=">58 Years",empRecovryRemarks="",accountType="",station="",pensionCmplteDate="";
		double dojAge=0, pensionAge =0;
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			if(!regionDesc.equals("")){
				staionsStr = commonUtil.getRAUAirports(regionDesc.toUpperCase());
					if((staionsStr.equals("")) && (regionDesc.toUpperCase().equals("CHQNAD"))){
						stationDesc="CHQNAD";
					}else{
						stationDesc=  staionsStr.substring(0, staionsStr.lastIndexOf("','"));
					}
				}else{
			stationDesc = "";
			}
		}
		
		log.info("==region=="+region+"==airprotcode=="+airprotcode+"=regionDesc="+regionDesc+"==stationDesc=="+stationDesc+"fromDate=="+fromDate+"toDate"+toDate);
		selPerQuery = this.buildQueryForm6AEmpEDLIInfo(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				fromDate);
		selTransQuery = this.buildQueryEmpPFTransChallan(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, NVL(EMPFID.FHFLAG,'-')  as FHFLAG, EMPFID.GENDER as GENDER, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME,EMPFID.DATEOFSEPERATION_REASON as DATEOFSEPERATION_REASON,EMPFID.DATEOFSEPERATION_DATE as DATEOFSEPERATION_DATE, EMPFID.DATEOFJOINING as DATEOFJOINING,(case when(add_months(to_date(EMPFID.DATEOFJOINING),1) between '"+fromDate+"' and last_day('"+fromDate+"'))then 'Y' else 'N' end) as newjoinflag, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, EMPFID.ACCOUNTTYPE AS  ACCOUNTTYPE ,"
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF,EMPFID.DATEOFENTITLE AS DATEOFENTITLE, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF,EPV.AEMOLUMENTS as AEMOLUMENTS,EPV.AEMPPFSTATUARY as AEMPPFSTATUARY,EPV.APENSIONCONTRI as APENSIONCONTRI,EPV.APF as APF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.EMPRECOVERYSTS AS EMPRECOVERYSTS,EPV.ARREARFLAG AS ARREARFLAG,EMPFID.PENSIONAGE AS PENSIONAGE,EMPFID.PENSIONCMPLTEDATE AS PENSIONCMPLTEDATE, EMPFID.DOJAGE AS DOJAGE, EPV.RECSTS AS RECSTS,EPV.NCPDAYS AS NCPDAYS FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO(+)  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getForm6AEChallanReport===selQuery"
					+ selQuery);
			FileWriter f=new FileWriter(new File("D:/Prasad.txt"));
			f.write(selQuery);
			f.flush();
			f.close();
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				station="";
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

				}

				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				if (rs.getString("ARREARFLAG") != null) {
					epfForm3Bean.setArrearflag(rs.getString("ARREARFLAG"));
				} else {
					epfForm3Bean.setDesignation("N");
				}
				if (rs.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(personalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
					epfForm3Bean.setOriginalEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setOriginalEmoluments("0");
					epfForm3Bean.setEmoluments("0");
				}
				
				if (rs.getString("EMPPFSTATUARY") != null) {

					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}
				
				if (rs.getString("AEMOLUMENTS") != null) {
					epfForm3Bean.setNonCPFEmoluments(rs.getString("AEMOLUMENTS"));
				} else {
					epfForm3Bean.setNonCPFEmoluments("0.00");
				}
				if (rs.getString("AEMPPFSTATUARY") != null) {
					epfForm3Bean.setNonCPFemppfstatury(rs.getString("AEMPPFSTATUARY"));
				} else {
					epfForm3Bean.setNonCPFemppfstatury("0.00");
				}
				if (rs.getString("APENSIONCONTRI") != null) {
					epfForm3Bean.setNonCPFPC(rs.getString("APENSIONCONTRI"));
				} else {
					epfForm3Bean.setNonCPFPC("0.00");
				}
				if (rs.getString("APF") != null) {
					epfForm3Bean.setNonCPFPF(rs.getString("APF"));
				} else {
					epfForm3Bean.setNonCPFPF("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				// log.info("Pension
				// No"+epfForm3Bean.getPensionno()+"Emoluments"
				// +epfForm3Bean.getEmoluments()+"Pension Contribution"+
				// epfForm3Bean.getPensionContribution());

				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				
				if (rs.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs.getString("DATEOFSEPERATION_REASON"));
				
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				//using type for segregation of StillInService & ExitOfService
				//if (rs.getString("DATEOFSEPERATION_REASON") !="---" && rs.getString("DATEOFSEPERATION_DATE") != "---") {
					if(epfForm3Bean.getSeperationreason().equals("Resignation") ||epfForm3Bean.getSeperationreason().equals("Retirement")||epfForm3Bean.getSeperationreason().equals("Death")||epfForm3Bean.getSeperationreason().equals("Termination") ){
						epfForm3Bean.setType("ExitOfService");
					}else if(rs.getString("newjoinflag").equals("Y")){
						epfForm3Bean.setType("NewJoin");
					}
					else
					{
						epfForm3Bean.setType("StillInService");
					}	
				//}
				
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs.getString("ACCOUNTTYPE");
					
				}  
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						station=epfForm3Bean.getStation();
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				}
				 
				if (rs.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs.getString("PENSIONAGE"));
					pensionAge = Double.parseDouble(rs.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("");
				}
				if (rs.getString("PENSIONCMPLTEDATE") != null) {				 
					pensionCmplteDate = rs.getString("PENSIONCMPLTEDATE");
				} else {
					pensionCmplteDate="---";
				}
				
				
				
				if (rs.getString("DOJAGE") != null) {
					dojAge  = Double.parseDouble(rs.getString("DOJAGE"));
				} 
				
				if (rs.getString("EMPRECOVERYSTS") != null) {
					if(rs.getString("EMPRECOVERYSTS").equals("CPF")){
						empRecovryRemarks="Regular";
					}else if(rs.getString("EMPRECOVERYSTS").equals("DEP")){
						empRecovryRemarks="Deputation";
					}else if(rs.getString("EMPRECOVERYSTS").equals("RET")){
						empRecovryRemarks="Retirement";
					}
					epfForm3Bean.setRemarks(empRecovryRemarks);
				} else{
					epfForm3Bean.setRemarks("");
				} 
				
				if (rs.getString("RECSTS") != null) {					 
					epfForm3Bean.setRecoveryStatus(rs.getString("RECSTS"));
				} else{
					epfForm3Bean.setRecoveryStatus("");
				}
				
				calEmoluments = finReportDAO.calWages(epfForm3Bean
						.getEmoluments(), epfForm3Bean.getMonthyear(),
						epfForm3Bean.getWetherOption().trim(), false, false,
						"1");
				transMntYear = Date.parse(epfForm3Bean.getMonthyear());
				dateOfRetriment = commonDAO.getRetriedDate(epfForm3Bean
						.getDateOfBirth());

				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear == empRetriedDt) {

					chkDOBFlag = true;
				}
				if (chkDOBFlag == true) {
					String[] dobList = epfForm3Bean.getDateOfBirth().split("-");
					days = dobList[0];

					getDaysBymonth = commonDAO.getNoOfDays(epfForm3Bean
							.getDateOfBirth());

					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
				}
				epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
						.parseDouble(calEmoluments))));
				chkDOBFlag=false;
				
				// By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				
				// Changed on 15-May-2012 as per Sehgal 
				if(!(dojAge==0 || pensionAge==58 )){
					epfForm3Bean.setDateOfBirth("---");
					epfForm3Bean.setDateofJoining("---");
					epfForm3Bean.setFhName("---");					 
					epfForm3Bean.setGender("---");
					epfForm3Bean.setFhflag("---");
					epfForm3Bean.setDateofentitle("---");
				}
				
				if(dojAge==0){					 
					epfForm3Bean.setSeperationreason("---");
					epfForm3Bean.setSeperationDate("---");
				}
//				if(pensionAge==58){					 
//					epfForm3Bean.setSeperationreason("SuperAnnuation");
//					//Added on 15-Jun-2012 as per Seghal
//					epfForm3Bean.setSeperationDate(pensionCmplteDate);
//				}
				epfForm3Bean.setDaysBymonth(String.valueOf(getDaysBymonth));
				if (rs.getString("NCPDAYS") != null) {
					epfForm3Bean.setNCPDays(rs.getString("NCPDAYS"));

				}else{
					epfForm3Bean.setNCPDays("0");
				}
				
				
				
				if(epfForm3Bean.getType().equals("StillInService")){
				form3DataList.add(epfForm3Bean);
				}else if(epfForm3Bean.getType().equals("NewJoin")){
					newJoinList.add(epfForm3Bean);
				}else{
				if(rs.getString("DATEOFSEPERATION_REASON") !=null && rs.getString("DATEOFSEPERATION_DATE") != null) {	
				seperationDataList.add(epfForm3Bean);
				seperationList.add(epfForm3Bean.getPensionno());
				}
				}
				

			}
			log.info("newJoinList==size"+newJoinList.size());
			
			//New Joinees
			commonDB.closeConnection(null,st1,rs1);
			String newJoineeFlag="";
			String selPerNewJoinee = this.buildQueryForm6AEmpPFNewJoineeInfo(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
				st1 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFNewJoineeInfo ==  For New Joiness =selQuery"
					+ selPerNewJoinee);
			rs1 = st1.executeQuery(selPerNewJoinee);
		 
			while (rs1.next()) {
				station="";
				 	epfForm3Bean = new AaiEpfform3Bean();
				if (rs1.getString("PENSIONNO") != null){
					epfForm3Bean.setPensionno(rs1.getString("PENSIONNO"));
					epfForm3Bean.setType("NewJoin");
				}
				if (rs1.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs1.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				
				if (rs1.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs1.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs1.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs1.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				
				if (rs1.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs1.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs1.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs1.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs1.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs1.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs1.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs1.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs1.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs1.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs1.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs1.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				
					epfForm3Bean.setMonthyear("---");
				
			
					epfForm3Bean.setEmoluments("0.00");
					epfForm3Bean.setOriginalEmoluments("0.00");
					epfForm3Bean.setNonCPFEmoluments("0.00");
				
					epfForm3Bean.setEmppfstatury("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
				
			
					epfForm3Bean.setEmpvpf("0.00");
					epfForm3Bean.setPrincipal("0.00");
					epfForm3Bean.setInterest("0.00");
					epfForm3Bean.setSubscriptionTotal("0.00");

					epfForm3Bean.setPensionContribution("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
					epfForm3Bean.setPf("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
			
				if (rs1.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs1.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs1.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs1
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				
				
				if (rs1.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs1.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs1.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs1.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs1.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs1.getString("ACCOUNTTYPE");
					
				}  
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						station=epfForm3Bean.getStation();
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				}
				if (rs1.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs1.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("");
				}
				if (rs1.getString("DTRECEVIEDNEWJOINEE") != null) {
					newJoineeFlag=rs1.getString("DTRECEVIEDNEWJOINEE");
				} else {
					newJoineeFlag="Y";
				}
				
				
				epfForm3Bean.setEmoluments("0.00");
				epfForm3Bean.setOriginalEmoluments("0.00");
				
				epfForm3Bean.setFormDifference("0.00");
				epfForm3Bean.setRemarks(remarksAfter58);      
				
				//By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				//Changed on 15-May-2012 as per Sehgal 
				epfForm3Bean.setDateOfBirth("---");
				epfForm3Bean.setDateofJoining("---");
				epfForm3Bean.setFhName("---");					 
				epfForm3Bean.setGender("---");
				epfForm3Bean.setFhflag("---"); 					 
				epfForm3Bean.setRecoveryStatus("");
				epfForm3Bean.setNCPDays("0");
				epfForm3Bean.setDaysBymonth(String.valueOf(getDaysBymonth));
				 
//					if(!epfForm3Bean.getSeperationDate().equals("---")){
//						seperationDataList.add(epfForm3Bean.getPensionno());
//					}
					
					if (newJoineeFlag.equals("N")){
						newJoinList.add(epfForm3Bean);
					}
					
				

			}
			
			
			//death cases
//			 Form 5 Data
			selQuery = this.buildQueryForm6ASeperationInfo(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
			st2 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6ASeperationInfo==  For Seperation=selQuery"
					+ selQuery);
			rs2 = st2.executeQuery(selQuery);
		 
			while (rs2.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				String pensionno="",cpfAccNo="",employeename="",seperationDate="",seperationReason="",wetheroption="" ;
				epfForm3Bean = new AaiEpfform3Bean();
				station="";
				if (rs2.getString("PENSIONNO") != null) {
					pensionno = rs2.getString("PENSIONNO") ;

				}
				if (rs2.getString("CPFACNO") != null) {
					cpfAccNo = rs2.getString("CPFACNO") ;
				} else {
					cpfAccNo = "---";
				}
				
				if (rs2.getString("EMPLOYEENAME") != null) {
					employeename =rs2.getString("EMPLOYEENAME");
				} else {
					employeename = "---";
				}
				
				if (rs2.getString("AIRPORTCODE") != null) {
					station = rs2.getString("AIRPORTCODE") ;
				} else {
					station = "---" ;
				}
				if (rs2.getString("WETHEROPTION") != null) {
					wetheroption=rs2.getString("WETHEROPTION");
				} else {
					wetheroption="---";
				}
				if (rs2.getString("REGION") != null) {
					region = rs2.getString("REGION");
				} else {
					region =  "---" ;
				}
				if (rs2.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs2.getString("ACCOUNTTYPE");
					
				}  
				
				//By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				//On 15-Jun-2012 as per Sehgal
				AaiEpfform3Bean  form5Data = new AaiEpfform3Bean();  
				form5Data = getEmolumentsFrmChalan(fromDate,pensionno);
				if(form5Data!=null){
					calEmoluments = finReportDAO.calWages(form5Data.getEmoluments(), fromDate,
							wetheroption.trim(), false, false,"1");
					epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
							.parseDouble(calEmoluments))));					 
					epfForm3Bean.setPensionContribution(form5Data.getPensionContribution());
				}else{
					epfForm3Bean.setEmoluments("0");
					epfForm3Bean.setPensionContribution("0.00");
				}
				 
				epfForm3Bean.setPensionno(pensionno);
				epfForm3Bean.setCpfaccno(cpfAccNo);
				epfForm3Bean.setEmployeeName(employeename);
				//epfForm3Bean.setDateofentitle("---");
				epfForm3Bean.setGender("---");
				epfForm3Bean.setFhflag("---");
				epfForm3Bean.setArrearflag("N");
				epfForm3Bean.setEmployeeNo("---");
				epfForm3Bean.setDesignation("---");
				epfForm3Bean.setFhName("---");
				epfForm3Bean.setWetherOption(wetheroption);
				 
				
				
				
				epfForm3Bean.setOriginalEmoluments("0");
				epfForm3Bean.setEmppfstatury("0.00");
				epfForm3Bean.setNonCPFEmoluments("0.00");
				epfForm3Bean.setEmpvpf("0.00");
				epfForm3Bean.setPrincipal("0.00");
				epfForm3Bean.setInterest("0.00");
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));
				
				epfForm3Bean.setNonCPFPC("0.00");
				epfForm3Bean.setPf("0.00");
				epfForm3Bean.setNonCPFPF("0.00");
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));					 
				epfForm3Bean.setFormDifference(Double.toString(Math.round(diff)));
				
				epfForm3Bean.setHighlightedColor("");
				
				epfForm3Bean.setRegion(region);
				epfForm3Bean.setStation(station);
				
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				} 
				
				if (rs2.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs2.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				epfForm3Bean.setDateofJoining("---"); 
				 
					epfForm3Bean.setMonthyear("---");
				 
				
				if (rs2.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs2.getString("DATEOFENTITLE"));
					//EDLI report purpose
					epfForm3Bean.setEdliDateOfJoining(rs2.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs2.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs2.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs2.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(CommonUtil.converDBToAppFormat(rs2
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				//epfForm3Bean.setEmoluments("0.00");
				epfForm3Bean.setOriginalEmoluments("0.00");

				epfForm3Bean.setRemarks(""); 
				if (rs2.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs2.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("0");
				}
				epfForm3Bean.setDaysBymonth("0");
				epfForm3Bean.setRecoveryStatus("");
				epfForm3Bean.setNCPDays("0");
				
//				By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				//epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				boolean ChkFlag=false;
				for(int i=0;i<seperationList.size();i++){
					log.info("seperationList.get(i)"+seperationList.get(i));
					if(seperationList.get(i).equals(pensionno)){
						ChkFlag=true;
					}
				}
				log.info("=====In ChkFlag ======"+ChkFlag+"===pensionno====="+pensionno);
				if(ChkFlag==false){
					seperationDataList.add(epfForm3Bean);
				}
				

			}
			
			map.put("StillInService",form3DataList );
			map.put("ExitOfService",seperationDataList );
			map.put("newJoin",newJoinList );
		
			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st1, rs1);
			commonDB.closeConnection(null, st2, rs2);
			commonDB.closeConnection(con, st, rs);
		}
		return map;
	}
	public String rpfcForm6ECR(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) throws InvalidDataException{
		List form6AList=new ArrayList();
		AaiEpfform3Bean epfForm3Bean;
		StringBuffer buffer=new StringBuffer();
		CommonUtil util=new CommonUtil();
		String selectedmonth="",path="",recoveryStatus="",seperationReason="";
		DecimalFormat df = new DecimalFormat("#########0");
		form6AList=getForm6AEChallanReport( range,  region,
				 airprotcode,  empName,  empNameFlag,
				 frmSelctedYear,  sortingOrder,  frmPensionno);
		for(int i=0;i<form6AList.size();i++){
			epfForm3Bean=(AaiEpfform3Bean)form6AList.get(i);
			try {
				if(selectedmonth.equals("")){
					selectedmonth=util.converDBToAppFormat(epfForm3Bean.getMonthyear(),"dd-MMM-yyyy","MMM");
				}
				
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 if(!epfForm3Bean.getRecoveryStatus().equals("")){
				 recoveryStatus = epfForm3Bean.getRecoveryStatus();	
				 seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;	
				 if(!epfForm3Bean.getSeperationreason().equals("---")){
				  seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;
				 }else{
				 seperationReason = recoveryStatus;
				 }
				 }else{
				  seperationReason = epfForm3Bean.getSeperationreason();
				  }
			buffer.append(commonUtil.leadingZeros(5,epfForm3Bean.getPensionno()));
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getEmployeeName());
			buffer.append("#~#");
			buffer.append("");
			buffer.append("#~#");
			buffer.append(df.format(Double.parseDouble(epfForm3Bean.getEmoluments())));
			buffer.append("#~#");
			buffer.append("");//SL.NO:5
			buffer.append("#~#");
			buffer.append("");//SL.NO:6
			buffer.append("#~#");
			buffer.append(df.format(Double.parseDouble(epfForm3Bean.getPensionContribution())));//SL.NO:7
			buffer.append("#~#");
			buffer.append(df.format(Double.parseDouble(epfForm3Bean.getPensionContribution())));//SL.NO:8
			buffer.append("#~#");
			buffer.append("");//SL.NO:9 DIFF EPF AND EPS Contribution (ER Share)  due
			buffer.append("#~#");
			buffer.append("");//SL.NO:10 DIFF EPF AND EPS Contribution (ER Share) being remited
			buffer.append("#~#");
			buffer.append(df.format(Double.parseDouble(epfForm3Bean.getNCPDays())));//SL.NO:11 NCP Days
			buffer.append("#~#");
			buffer.append("");//SL.NO:12 Refund Advances
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getNonCPFEmoluments());//SL.NO:13  Arrear EPF Wages
			buffer.append("#~#");
			buffer.append("");//SL.NO:14  Arrear EPF EE Share
			buffer.append("#~#");
			buffer.append("");//SL.NO:15    Arrear EPF ER Share
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getNonCPFPC());//SL.NO:16         Arrear EPS
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getFhName());//SL.NO:17
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getFhflag());//SL.NO:18 RELATIONSHIP WITH MEMBER
			buffer.append("#~#");
			if (!epfForm3Bean.getDateOfBirth().equals("---")){
				buffer.append(util.converDBToAppFormat(epfForm3Bean.getDateOfBirth(),"dd-MMM-yyyy","dd/mm/yyyy"));//SL.NO:19 DATE OF BIRTH  FOR NEW MEMBERS	
			}else{
				buffer.append("");//SL.NO:19 DATE OF BIRTH  FOR NEW MEMBERS
			}
			
			buffer.append("#~#");
			buffer.append(epfForm3Bean.getGender());//SL.NO:20 GENDER  FOR NEW MEMBERS
			buffer.append("#~#");
			if (!epfForm3Bean.getDateofentitle().equals("---")){
				buffer.append(util.converDBToAppFormat(epfForm3Bean.getDateofentitle(),"dd-MMM-yyyy","dd/mm/yyyy"));//SL.NO:21 DATEOF JOINING EPF  FOR NEW MEMBERS
			}else{
				buffer.append("");//SL.NO:21 DATEOF JOINING EPF  FOR NEW MEMBERS
			}
			buffer.append("#~#");
			if (!epfForm3Bean.getDateofentitle().equals("---")){
				buffer.append(util.converDBToAppFormat(epfForm3Bean.getDateofentitle(),"dd-MMM-yyyy","dd/mm/yyyy"));//SL.NO:22 DATE OF JOINING EPS  FOR NEW MEMBERS	
			}else{
				buffer.append("");//SL.NO:22 DATE OF JOINING EPS  FOR NEW MEMBERS	
			}
			buffer.append("#~#");
			
			if (!epfForm3Bean.getSeperationDate().equals("---")){
			buffer.append(util.converDBToAppFormat(epfForm3Bean.getSeperationDate(),"dd-MMM-yyyy","dd/mm/yyyy"));//SL.NO:23 DATE OF EXIT FROM EPF FOR EXISTING MEMBER
			}else{
				buffer.append("");//SL.NO:23 DATE OF EXIT FROM EPF FOR EXISTING MEMBER
			}
			buffer.append("#~#");
			if (!epfForm3Bean.getSeperationDate().equals("---")){
				buffer.append(util.converDBToAppFormat(epfForm3Bean.getSeperationDate(),"dd-MMM-yyyy","dd/mm/yyyy"));//SL.NO:24 DATE OF EXIT FROM EPS FOR EXISTING MEMBER
			}else{
					buffer.append("");//SL.NO:24 DATE OF EXIT FROM EPS FOR EXISTING MEMBER
			}
			buffer.append("#~#");
			buffer.append(seperationReason);//SL.NO:25 REASON FOR LEAVING //C-CESSATION,S-SUPER ANNUATION,R-RETRIMENT,D-DEATH OF SERVICE,P-Permanent Disablement
			buffer.append("\n");
		}
	
		path=util.createFile("ECR_"+selectedmonth,".txt", buffer.toString());
		return path;
	}

	public String buildQueryEmpPFInfo(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno) {
		log.info("EPFFormsReportDAO::buildQueryEmpPFInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT CPFACNO,EMPLOYEENO,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
				+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
				+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE FROM EMPLOYEE_PERSONAL_INFO ";
		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		/*
		 * if (!region.equals("")) { whereClause.append(" REGION ='" + region +
		 * "'"); whereClause.append(" AND "); }
		 */
		/*
		 * if (!airportcode.equals("")) { whereClause.append(" AIRPORTCODE ='" +
		 * airportcode + "'"); whereClause.append(" AND "); }
		 */
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if (/* (region.equals("")) && (airportcode.equals("")) && */(range
				.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" WHERE ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFInfo Leaving Method");
		return dynamicQuery;
	}
 //By Radha on 13-Nov-2012 For gettind monthhly data of employee who death exeact on 58 years
	public String buildQueryForm6AEmpPFInfo(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String fromDate) {
		log.info("EPFFormsReportDAO::buildQueryForKrishnam6AEmpPFInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT CPFACNO,EMPLOYEENO,uanno,newempcode,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,to_date(DATEOFBIRTH) as DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,to_date(to_char(DATEOFSEPERATION_DATE,'dd-Mon-yyyy')) as DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
			+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,EPI.REGION AS REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
			+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12,1)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
			+ "ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE,nvl(FHFLAG,'F') as FHFLAG, (ROUND(months_between('"+fromDate+"', (case when to_char(dateofbirth,'dd')='01' then dateofbirth else last_day(dateofbirth) end)) / 12,1)) AS PENSIONAGE,(ROUND(months_between('"+fromDate+"', last_day(dateofjoining)) / 12, 1)) AS DOJAGE,  to_char(add_months(dateofbirth-1,696),'dd-Mon-yyyy')  as PENSIONCMPLTEDATE ,EUM.ACCOUNTTYPE AS ACCOUNTTYPE,(select f.freshpensionoption from employee_pension_freshoption f where deleteflag = 'N' and pensionno = epi.pensionno) as freshoption FROM EMPLOYEE_PERSONAL_INFO EPI ,EMPLOYEE_UNIT_MASTER EUM  WHERE  UPPER(EUM.REGION) = UPPER(EPI.REGION)  AND UPPER(EUM.UNITNAME) = UPPER(AIRPORTCODE) AND "
			+ "((ROUND(months_between('"+ fromDate + "', last_day(dateofbirth)) / 12,1)<=58 AND (DATEOFSEPERATION_REASON NOT IN ('Death','Resignation','VRS','Termination') OR DATEOFSEPERATION_REASON IS NULL)) OR (DATEOFSEPERATION_REASON IN ('Death','Resignation','VRS','Termination') AND to_date(to_char(ADD_MONTHS(NVL(DATEOFSEPERATION_DATE,last_day('"+fromDate+"')),1),'dd-Mon-yyyy'))>=TO_DATE(last_day('"
			+ fromDate + "')))OR (ROUND(months_between('"+ fromDate + "', last_day(dateofbirth)) / 12, 1)  = 58 AND (DATEOFSEPERATION_REASON   IN ('Death')))) ";

		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		 
		/*if (!region.equals("")) { 
			whereClause.append("UPPER(EUM.REGION) = '" + region.toUpperCase() + "'");
			whereClause.append(" AND "); 
	   }
		
		if (!airportcode.equals("")) { 
				whereClause.append("UPPER(EUM.UNITNAME) IN ('" + airportcode + "')");
				whereClause.append(" AND "); 
		}*/
		query.append(sqlQuery);
		if ( /*(region.equals("")) && (airportcode.equals("")) &&*/ (range
				.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFInfo Leaving Method");
		return dynamicQuery;
	}
	public String buildQueryForm6AEmpEDLIInfo(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String fromDate) {
		log.info("EPFFormsReportDAO::buildQueryForm6AEmpEDLIInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT CPFACNO,EMPLOYEENO,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,to_date(DATEOFBIRTH) as DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,to_date(to_char(DATEOFSEPERATION_DATE,'dd-Mon-yyyy')) as DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
			+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,EPI.REGION AS REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
			+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12,1)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
			+ "ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE,FHFLAG, (ROUND(months_between('"+fromDate+"', (case when to_char(dateofbirth,'dd')='01' then dateofbirth else last_day(dateofbirth) end)) / 12,1)) AS PENSIONAGE,(ROUND(months_between('"+fromDate+"', last_day(dateofjoining)) / 12, 1)) AS DOJAGE,  to_char(add_months(dateofbirth,696),'dd-Mon-yyyy')  as PENSIONCMPLTEDATE ," 
			+"EUM.ACCOUNTTYPE AS ACCOUNTTYPE FROM EMPLOYEE_PERSONAL_INFO EPI ,EMPLOYEE_UNIT_MASTER EUM  WHERE  UPPER(EUM.REGION) = UPPER(EPI.REGION)  AND UPPER(EUM.UNITNAME) = UPPER(AIRPORTCODE) and (DATEOFSEPERATION_DATE is null or DATEOFSEPERATION_DATE <'"+fromDate+"')  and"
			//AND DATEOFJOINING not between '"+fromDate+"' and last_day('"+fromDate+"')
			+ "((ROUND(months_between('"+ fromDate + "', last_day(dateofbirth)) / 12,1)<=60 AND  ((DATEOFSEPERATION_REASON NOT IN('Death', 'Resignation', 'VRS', 'Termination')  and (to_date(to_char(ADD_MONTHS(NVL(DATEOFSEPERATION_DATE,last_day('"+fromDate+"')),1),'dd-Mon-yyyy')) BETWEEN '"+fromDate+"' AND TO_DATE(last_day('"+fromDate+"')))) OR DATEOFSEPERATION_REASON IS NULL)) OR (DATEOFSEPERATION_REASON IN ('Death','Resignation','VRS','Termination') AND to_date(to_char(ADD_MONTHS(NVL(DATEOFSEPERATION_DATE,last_day('"+fromDate+"')),1),'dd-Mon-yyyy')) BETWEEN '"+fromDate+"' AND TO_DATE(last_day('"
			+ fromDate + "')))) ";
		
		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		 
		/*if (!region.equals("")) { 
			whereClause.append("UPPER(EUM.REGION) = '" + region.toUpperCase() + "'");
			whereClause.append(" AND "); 
	   }
		
		if (!airportcode.equals("")) { 
				whereClause.append("UPPER(EUM.UNITNAME) IN ('" + airportcode + "')");
				whereClause.append(" AND "); 
		}*/
		query.append(sqlQuery);
		if ( /*(region.equals("")) && (airportcode.equals("")) &&*/ (range
				.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpEDLIInfo Leaving Method");
		return dynamicQuery;
	}
	public String buildQueryForm6AEmpPFAfter58Info(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String fromDate) {
		log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFAfter58Info-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT CPFACNO,EMPLOYEENO,uanno as uanno,newempcode,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,to_date(DATEOFBIRTH) as DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
			+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,EPI.REGION AS EPI,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
			+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12,1)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
			+ "ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE,Nvl(FHFLAG,'F') as FHFLAG, (ROUND(months_between('"+fromDate+"', last_day(dateofbirth)) / 12, 1)) AS PENSIONAGE, EUM.ACCOUNTTYPE AS ACCOUNTTYPE,(select f.freshpensionoption from employee_pension_freshoption f where deleteflag = 'N' and pensionno = epi.pensionno) as freshoption FROM EMPLOYEE_PERSONAL_INFO EPI, EMPLOYEE_UNIT_MASTER EUM WHERE UPPER(EUM.REGION)= UPPER(EPI.REGION) AND UPPER(EUM.UNITNAME)=UPPER(AIRPORTCODE) AND   (((ROUND(months_between('"
			+ fromDate + "', last_day(dateofbirth-1)) / 12,1)>58  and (DATEOFSEPERATION_REASON is null)) OR (DATEOFSEPERATION_REASON IN ('Death','Resignation','Retirement','VRS','Termination') ) AND ADD_MONTHS(NVL(DATEOFSEPERATION_DATE,last_day('"+fromDate+"')),1)<=TO_DATE(LAST_DAY('"
				+ fromDate + "')))) ";

		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		
		 
		/*if (!region.equals("")) { 
			whereClause.append("UPPER(EUM.REGION) = '" + region.toUpperCase() + "'");
			whereClause.append(" AND "); 
	   }
		
		if (!airportcode.equals("")) { 
				whereClause.append("UPPER(EUM.UNITNAME) IN ('" + airportcode + "') ");
				whereClause.append(" AND "); 
		}*/
		
		query.append(sqlQuery);
		if ( /*(region.equals("")) && (airportcode.equals("")) && */ (range
				.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFAfter58Info Leaving Method");
		return dynamicQuery;
	}
	// By Radha on 23-Apr-2012 for getting  EMPRECOVERYSTS
	public String buildQueryEmpPFTransInfo(String region, String airportcode,
			String sortedColumn, String fromDate, String toDate) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, CPF, round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, round(AAICONPF) AS AAICONPF, ROUND(CPFADVANCE) AS CPFADVANCE, ROUND(DEDADV) AS DEDADV, ROUND(REFADV) AS REFADV, ROUND(PENSIONCONTRI) AS PENSIONCONTRI, ROUND(PF) AS PF, AIRPORTCODE, "
				+ "REGION, EMPFLAG, CPFACCNO,PENSIONNO,EMPLOYEENO ,EMPRECOVERYSTS,ARREARFLAG FROM EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL  and MONTHYEAR between '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo Leaving Method");
		return dynamicQuery;
	}
	//By Radha On 11-Jul-2012 for Including Regular Salry Submited as Suppli in Normal Wages in ECR
//	 By Radha on 07-Jun-2012 for getting   NCPDAYS 
	public String buildQueryEmpPFTransChallan(String region, String airportcode,
			String sortedColumn, String fromDate, String toDate) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransChallan-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, '0' AS EMPVPF, '0' AS CPF, '0' AS EMPADVRECPRINCIPAL,"
				+ "'0' AS EMPADVRECINTEREST, '0' AS AAICONPF, '0' AS CPFADVANCE, '0' AS DEDADV,'Y' AS EMPFLAG,'N' AS ARREARFLAG,'0' AS REFADV, ROUND(PENSIONCONTRI) AS PENSIONCONTRI,ROUND(nvl(CPFADDTIONALCONTRI,0)+nvl(Aaddcontri,0)) AS CPFADDTIONALCONTRI,ROUND(Aaddcontri) AS Aaddcontri, ROUND(PF) AS PF,AEMOLUMENTS as AEMOLUMENTS,AEMPPFSTATUARY,APENSIONCONTRI,APF, AIRPORTCODE, "
				+ "REGION, '', '',PENSIONNO,'' ,'','','N' as EMPRECOVERYSTS,'' AS CPFACCNO,'' as EMPLOYEENO, RECSTS AS RECSTS,NCPDAYS AS NCPDAYS,REGSALFLAG AS REGSALFLAG  FROM V_EMP_ECHALLAN WHERE MONTHYEAR between '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" UPPER(REGION) ='" + region.toUpperCase() + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" UPPER(AIRPORTCODE) IN('" + airportcode + "')");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFTransChallan Leaving Method");
		return dynamicQuery;
	}
	public AAIEPFReportBean AAIEPFForm1Report(String range, String region,
			String airportcode, String frmSelectedDts, String empNameFlag,
			String empName, String sortedColumn, String pensionno) {

		log.info("AAIEPFReportDAO::AAIEPFForm1Report");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String fromYear = "", toYear = "", dateOfRetriment = "", regionString = "", airportcodeString = "";
		AAIEPFReportBean AAIEPFBean = new AAIEPFReportBean();
		AAIEPFReportBean AAIEPFBean1 = null;
		AAIEPFReportBean AAIEPFBean2 = null;
		AAIEPFReportBean AAIEPFBean3 = null;
		ArrayList AAIEPFReportList1 = new ArrayList();
		ArrayList AAIEPFReportPendingList1 = new ArrayList();
		ArrayList AAIEPFReportList2 = new ArrayList();
		ArrayList AAIEPFReportList = new ArrayList();

		log.info("......frmSelectedDts in DAO......." + frmSelectedDts);

		if (!frmSelectedDts.equals("")) {

			String[] dateArr = frmSelectedDts.split(",");
			fromYear = dateArr[0];
			toYear = dateArr[1];
		}
		if (!region.equals("NO-SELECT")) {
			regionString = region;
		} else {
			regionString = "";
		}
		if (!airportcode.equals("NO-SELECT")) {
			airportcodeString = airportcode;
		} else {
			airportcodeString = "";
		}
		String form1Query = this.buildQueryAAIEPFForm1Report(range,
				regionString, airportcodeString, fromYear, toYear, empNameFlag,
				empName, sortedColumn, pensionno, "Y");
		log.info("-------form1Query------" + form1Query);

		String form1Qry = this.buildQueryforAAIEPFForm1Report(range,
				regionString, airportcodeString, fromYear, toYear, empNameFlag,
				empName, sortedColumn, pensionno);
		log.info("-------form1Qry------" + form1Qry);

		String form1Pending = this.buildQueryAAIEPFForm1Report(range,
				regionString, airportcodeString, fromYear, toYear, empNameFlag,
				empName, sortedColumn, pensionno, "N");
		try {

			con = commonDB.getConnection();
			st = con.createStatement();

			rs = st.executeQuery(form1Query);

			while (rs.next()) {
				AAIEPFBean1 = new AAIEPFReportBean();

				/*
				 * if (rs.getString("PENSIONNO") != null) {
				 * AAIEPFBean.setPensionNo(rs.getString("PENSIONNO")); } else {
				 * AAIEPFBean.setPensionNo("--"); }
				 */

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean1.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean1.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean1.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean1.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean1.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean1.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean1.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean1.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean1.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean1.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean1.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean1.setDateOfBirth("--");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					AAIEPFBean1.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					AAIEPFBean1.setAirportCode("--");
				}

				if (rs.getString("REGION") != null) {
					AAIEPFBean1.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean1.setRegion("--");
				}

				// log.info("-----Pension
				// No----------"+AAIEPFBean.getPensionNo()
				// );

				if (rs.getString("PENSIONNO") != null) {
					AAIEPFBean1.setPensionNo(commonDAO.getPFID(AAIEPFBean1
							.getEmployeeName(), AAIEPFBean1.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
				} else {
					AAIEPFBean1.setPensionNo("--");
				}

				if (rs.getString("SUBSCRIPTIONAMT") != null) {
					AAIEPFBean1.setSubscriptionAmt(rs
							.getString("SUBSCRIPTIONAMT"));
				} else {
					AAIEPFBean1.setSubscriptionAmt("0.00");
				}

				if (rs.getString("CONTRIBUTIONAMT") != null) {
					AAIEPFBean1.setContributionamt(rs
							.getString("CONTRIBUTIONAMT"));
				} else {
					AAIEPFBean1.setContributionamt("0.00");
				}

				if (rs.getString("OUTSTANDADV") != null) {
					AAIEPFBean1.setOutstandingAmt(rs.getString("OUTSTANDADV"));
				} else {
					AAIEPFBean1.setOutstandingAmt("0.00");
				}

				if (rs.getString("OBYEAR") != null) {
					AAIEPFBean1.setObYear(rs.getString("OBYEAR"));
				} else {
					AAIEPFBean1.setObYear("--");
				}

				AAIEPFReportList1.add(AAIEPFBean1);
			}

			rs2 = st.executeQuery(form1Qry);

			while (rs2.next()) {
				AAIEPFBean2 = new AAIEPFReportBean();

				if (rs.getString("CPFACCNO") != null) {
					AAIEPFBean2.setCpfAccno(rs.getString("CPFACCNO"));
				} else {
					AAIEPFBean2.setCpfAccno("--");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean2.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean2.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean2.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean2.setDesignation("--");
				}
				if (rs2.getString("SUBSCRIPTIONAMT") != null) {
					AAIEPFBean2.setSubscriptionAmt(rs2
							.getString("SUBSCRIPTIONAMT"));
				} else {
					AAIEPFBean2.setSubscriptionAmt("0.00");
				}

				if (rs2.getString("CONTRIBUTIONAMT") != null) {
					AAIEPFBean2.setContributionamt(rs2
							.getString("CONTRIBUTIONAMT"));
				} else {
					AAIEPFBean2.setContributionamt("0.00");
				}

				if (rs2.getString("OUTSTANDADV") != null) {
					AAIEPFBean2.setOutstandingAmt(rs2.getString("OUTSTANDADV"));
				} else {
					AAIEPFBean2.setOutstandingAmt("0.00");
				}

				if (rs2.getString("OBYEAR") != null) {
					AAIEPFBean2.setObYear(rs2.getString("OBYEAR"));
				} else {
					AAIEPFBean2.setObYear("--");
				}

				AAIEPFReportList2.add(AAIEPFBean2);

			}
			commonDB.closeConnection(null, st, rs);
			st = con.createStatement();
			rs = st.executeQuery(form1Pending);

			while (rs.next()) {
				AAIEPFBean3 = new AAIEPFReportBean();

				/*
				 * if (rs.getString("PENSIONNO") != null) {
				 * AAIEPFBean.setPensionNo(rs.getString("PENSIONNO")); } else {
				 * AAIEPFBean.setPensionNo("--"); }
				 */

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean3.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean3.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean3.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean3.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean3.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean3.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean3.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean3.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean3.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean3.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean3.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean3.setDateOfBirth("--");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					AAIEPFBean3.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					AAIEPFBean3.setAirportCode("--");
				}

				if (rs.getString("REGION") != null) {
					AAIEPFBean3.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean3.setRegion("--");
				}

				// log.info("-----Pension
				// No----------"+AAIEPFBean.getPensionNo()
				// );

				if (rs.getString("PENSIONNO") != null) {
					AAIEPFBean3.setPensionNo(commonDAO.getPFID(AAIEPFBean3
							.getEmployeeName(), AAIEPFBean3.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
				} else {
					AAIEPFBean3.setPensionNo("--");
				}

				if (rs.getString("SUBSCRIPTIONAMT") != null) {
					AAIEPFBean3.setSubscriptionAmt(rs
							.getString("SUBSCRIPTIONAMT"));
				} else {
					AAIEPFBean3.setSubscriptionAmt("0.00");
				}

				if (rs.getString("CONTRIBUTIONAMT") != null) {
					AAIEPFBean3.setContributionamt(rs
							.getString("CONTRIBUTIONAMT"));
				} else {
					AAIEPFBean3.setContributionamt("0.00");
				}

				if (rs.getString("OUTSTANDADV") != null) {
					AAIEPFBean3.setOutstandingAmt(rs.getString("OUTSTANDADV"));
				} else {
					AAIEPFBean3.setOutstandingAmt("0.00");
				}

				if (rs.getString("OBYEAR") != null) {
					AAIEPFBean3.setObYear(rs.getString("OBYEAR"));
				} else {
					AAIEPFBean3.setObYear("--");
				}

				AAIEPFReportPendingList1.add(AAIEPFBean3);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
			commonDB.closeConnection(null, null, rs2);
		}

		log.info("====AAIEPFReportList1 Size in DAO====="
				+ AAIEPFReportList1.size());
		log.info("====AAIEPFReportList2 Size in DAO====="
				+ AAIEPFReportList2.size());

		AAIEPFBean.setReportList1(AAIEPFReportList1);
		AAIEPFBean.setReportList2(AAIEPFReportList2);
		AAIEPFBean.setReportList3(AAIEPFReportPendingList1);
		return AAIEPFBean;

	}

	public String buildQueryforAAIEPFForm1Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno) {
		log
				.info("AAIEPFReportDAO::buildQueryforAAIEPFForm1Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;

		sqlQuery = "select CPFACCNO,EMPLOYEENAME,DESEGNATION,EMPNETOB AS SUBSCRIPTIONAMT,AAINETOB AS CONTRIBUTIONAMT,OUTSTANDADV,OBYEAR AS OBYEAR  FROM  employee_pension_ob EPO WHERE PENSIONNO IS NULL AND NEWJOINEEREMARKS='N' AND EMPFLAG='Y' ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}

		if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {
		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		dynamicQuery = query.toString();
		log
				.info("AAIEPFReportDAO::buildQueryforAAIEPFForm1Report Leaving Method");
		return dynamicQuery;

	}

	public String buildQueryAAIEPFForm1Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, String sepertionflag) {
		log
				.info("AAIEPFReportDAO::buildQueryAAIEPFForm1Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;

		sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB AS SUBSCRIPTIONAMT,EPO.AAINETOB AS CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR  FROM employee_personal_info EPI, employee_pension_ob EPO WHERE EPI.PENSIONNO = EPO.PENSIONNO AND EPO.OBSEPERATIONFLAG='"
				+ sepertionflag
				+ "' AND EPO.NEWJOINEEREMARKS='N'  AND EPO.EMPFLAG='Y' and EPO.OBYEAR BETWEEN '"
				+ fromYear + "' AND last_day('" + toYear + "') ";

		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  EPI.PENSIONNO >=" + startIndex
					+ " AND EPI.PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		if (!region.equals("")) {
			whereClause.append(" EPO.REGION ='" + region + "'");
			whereClause.append(" AND ");
		}

		if (!airportcode.equals("")) {
			whereClause.append(" EPO.AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))
				&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}
		orderBy = " ORDER BY  EPI." + sortedColumn + " ASC";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("AAIEPFReportDAO::buildQueryAAIEPFForm1Report Leaving Method");
		return dynamicQuery;

	}

	public AAIEPFReportBean AAIEPFForm8Report(String range, String region,
			String airportcode, String frmSelectedDts, String empNameFlag,
			String empName, String sortedColumn, String pensionno) {

		log.info("AAIEPFReportDAO::AAIEPFForm8Report");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String fromYear = "", toYear = "", dateOfRetriment = "";
		AAIEPFReportBean AAIEPFBean = new AAIEPFReportBean();
		ArrayList AAIEPFReportList = new ArrayList();
		ArrayList AdvanceList = new ArrayList();
		ArrayList LoansList = new ArrayList();
		ArrayList FinalSettlementList = new ArrayList();

		ArrayList AdvanceList2 = new ArrayList();
		ArrayList LoansList2 = new ArrayList();
		ArrayList FinalSettlementList2 = new ArrayList();

		log.info("......frmSelectedDts in DAO......." + frmSelectedDts);

		if (!frmSelectedDts.equals("")) {

			String[] dateArr = frmSelectedDts.split(",");
			fromYear = dateArr[0];
			toYear = dateArr[1];
		}

		try {

			con = commonDB.getConnection();
			AdvanceList = this.buildQueryAdvanceForm8Report(range, region,
					airportcode, fromYear, toYear, empNameFlag, empName,
					sortedColumn, pensionno, con);
			log.info("-------AdvanceList Size in DAO------"
					+ AdvanceList.size());

			LoansList = this.buildQueryLoansForm8Report(range, region,
					airportcode, fromYear, toYear, empNameFlag, empName,
					sortedColumn, pensionno, con);
			log.info("-------LoansList Size in DAO------" + LoansList.size());

			FinalSettlementList = this.buildQueryFinalSttlementForm8Report(
					range, region, airportcode, fromYear, toYear, empNameFlag,
					empName, sortedColumn, pensionno, con);
			log.info("-------FinalSettlementList Size in DAO------"
					+ FinalSettlementList.size());

			AdvanceList2 = this.buildQuery2AdvanceForm8Report(range, region,
					airportcode, fromYear, toYear, empNameFlag, empName,
					sortedColumn, pensionno, con);
			log.info("----/////////---AdvanceList2 Size in DAO------"
					+ AdvanceList2.size());

			LoansList2 = this.buildQuery2LoansForm8Report(range, region,
					airportcode, fromYear, toYear, empNameFlag, empName,
					sortedColumn, pensionno, con);
			log.info("---///////----LoansList2 Size in DAO------"
					+ LoansList2.size());

			FinalSettlementList2 = this.buildQuery2FinalSttlementForm8Report(
					range, region, airportcode, fromYear, toYear, empNameFlag,
					empName, sortedColumn, pensionno, con);
			log.info("--////////-----FinalSettlementList2 Size in DAO------"
					+ FinalSettlementList2.size());

			AAIEPFBean.setAdvancesList(AdvanceList);
			AAIEPFBean.setLonesList(LoansList);
			AAIEPFBean.setFinalSettlementList(FinalSettlementList);

			AAIEPFBean.setAdvancesList2(AdvanceList2);
			AAIEPFBean.setLonesList2(LoansList2);
			AAIEPFBean.setFinalSettlementList2(FinalSettlementList2);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		// log.info("====AAIEPFReportList Size in DAO====="+
		// AAIEPFReportList.size());
		return AAIEPFBean;

	}

	public ArrayList buildQuery2AdvanceForm8Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {

		log
				.info("AAIEPFReportDAO::buildQuery2AdvanceForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList advancesList = new ArrayList();

		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();

			sqlQuery = "select CPFACCNO,EMPLOYEENO,EMPLOYEENAME,ADVPURPOSE AS PURPOSE,AMOUNT AS AMOUNT,ADVTRANSDATE AS ADVTRANSDATE  FROM  employee_pension_advances where PENSIONNO is null and ADVTRANSDATE BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))) {
			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}
			dynamicQuery = query.toString();

			log.info("-------buildQuery2AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACCNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACCNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("PURPOSE") != null) {
					AAIEPFBean.setAdvPurpose(rs.getString("PURPOSE"));
				} else {
					AAIEPFBean.setAdvPurpose("--");
				}

				if (rs.getString("AMOUNT") != null) {
					AAIEPFBean.setAdvAmt(rs.getString("AMOUNT"));
				} else {
					AAIEPFBean.setAdvAmt("--");
				}

				if (rs.getString("ADVTRANSDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("ADVTRANSDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				advancesList.add(AAIEPFBean);

			}

		} catch (SQLException e) {
			log.printStackTrace(new Exception("------SQL-----" + e));
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------advancesList------*********"
				+ advancesList.size());
		log
				.info("AAIEPFReportDAO::buildQuery2AdvanceForm8Report Leaving Method");
		return advancesList;

	}

	public ArrayList buildQuery2LoansForm8Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {
		log
				.info("AAIEPFReportDAO::buildQuery2LoansForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList loansList = new ArrayList();

		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();

			sqlQuery = "select CPFACCNO,EMPLOYEENO,EMPLOYEENAME,LOANPURPOSE AS LOANPURPOSE,SUB_AMT AS EMPSHARE,CONT_AMT AS AAISHARE,LOANDATE AS LOANDATE  FROM  employee_pension_loans EPL WHERE PENSIONNO IS NULL and LOANDATE BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" EPL.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" EPL.AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}
			// orderBy = " ORDER BY EPI."+sortedColumn+" ASC";
			// query.append(orderBy);
			dynamicQuery = query.toString();

			log.info("-------buildQuery2AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACCNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACCNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("LOANPURPOSE") != null) {
					AAIEPFBean.setLoanPurpose(rs.getString("LOANPURPOSE"));
				} else {
					AAIEPFBean.setLoanPurpose("--");
				}

				if (rs.getString("EMPSHARE") != null) {
					AAIEPFBean.setEmpShare(rs.getString("EMPSHARE"));
				} else {
					AAIEPFBean.setEmpShare("--");
				}

				if (rs.getString("AAISHARE") != null) {
					AAIEPFBean.setAAIShare(rs.getString("AAISHARE"));
				} else {
					AAIEPFBean.setAAIShare("--");
				}

				if (rs.getString("LOANDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("LOANDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				loansList.add(AAIEPFBean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------loansList size------*********"
				+ loansList.size());
		log.info("AAIEPFReportDAO::buildQuery2LoansForm8Report Leaving Method");
		return loansList;

	}

	public ArrayList buildQuery2FinalSttlementForm8Report(String range,
			String region, String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {
		log
				.info("AAIEPFReportDAO::buildQuery2FinalSttlementForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList settlementList = new ArrayList();

		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();

			sqlQuery = "select CPFACCNO,EMPLOYEENO,EMPLOYEENAME,PURPOSE AS REASON,FINEMP AS EMPSHARE,FINAAI AS AAISHARE,PENCON AS PENSIONCONTRIBUTION,NETAMOUNT AS NETAMOUNT,SETTLEMENTDATE AS SETTLEMENTDATE  FROM  employee_pension_finsettlement  WHERE PENSIONNO IS NULL and SETTLEMENTDATE  BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}

			dynamicQuery = query.toString();

			log.info("-------buildQuery2AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACCNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACCNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("REASON") != null) {
					AAIEPFBean.setSettlementReason(rs.getString("REASON"));
				} else {
					AAIEPFBean.setSettlementReason("--");
				}

				if (rs.getString("EMPSHARE") != null) {
					AAIEPFBean.setEmpShare(rs.getString("EMPSHARE"));
				} else {
					AAIEPFBean.setEmpShare("--");
				}

				if (rs.getString("AAISHARE") != null) {
					AAIEPFBean.setAAIShare(rs.getString("AAISHARE"));
				} else {
					AAIEPFBean.setAAIShare("--");
				}

				if (rs.getString("PENSIONCONTRIBUTION") != null) {
					AAIEPFBean.setPensionContribution(rs
							.getString("PENSIONCONTRIBUTION"));
				} else {
					AAIEPFBean.setPensionContribution("--");
				}

				if (rs.getString("NETAMOUNT") != null) {
					AAIEPFBean.setNetAmt(rs.getString("NETAMOUNT"));
				} else {
					AAIEPFBean.setNetAmt("--");
				}

				if (rs.getString("SETTLEMENTDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("SETTLEMENTDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				settlementList.add(AAIEPFBean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------settlementList size------*********"
				+ settlementList.size());
		log
				.info("AAIEPFReportDAO::buildQuery2FinalSttlementForm8Report Leaving Method");
		return settlementList;

	}

	public ArrayList buildQueryAdvanceForm8Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {
		log
				.info("AAIEPFReportDAO::buildQuery1AAIEPFForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList advancesList = new ArrayList();
		if (!sortedColumn.equals("pensionno")) {
			sortedColumn = "ADVTRANSDATE";
		}
		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();
			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPA.EMPLOYEENO,EPA.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPA.AIRPORTCODE,EPA.REGION,EPA.ADVPURPOSE AS PURPOSE,EPA.AMOUNT AS AMOUNT,EPA.ADVTRANSDATE AS ADVTRANSDATE  FROM employee_personal_info EPI, employee_pension_advances EPA WHERE EPI.PENSIONNO = EPA.PENSIONNO and EPA.ADVTRANSDATE BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  EPI.PENSIONNO >=" + startIndex
						+ " AND EPI.PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" EPA.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" EPA.AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))
					&& (range.equals("NO-SELECT") && (empNameFlag
							.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}
			orderBy = " ORDER BY  " + sortedColumn + " ASC";
			query.append(orderBy);
			dynamicQuery = query.toString();

			log.info("-------buildQuery1AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setDateOfBirth("--");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					AAIEPFBean.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					AAIEPFBean.setAirportCode("--");
				}

				if (rs.getString("REGION") != null) {
					AAIEPFBean.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean.setRegion("--");
				}

				if (rs.getString("PENSIONNO") != null) {
					AAIEPFBean.setPensionNo(commonDAO.getPFID(AAIEPFBean
							.getEmployeeName(), AAIEPFBean.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
				} else {
					AAIEPFBean.setPensionNo("--");
				}
				log.info("Employeeno" + AAIEPFBean.getEmployeeName()
						+ "Pensionno" + rs.getString("PENSIONNO"));

				if (rs.getString("PURPOSE") != null) {
					AAIEPFBean.setAdvPurpose(rs.getString("PURPOSE"));
				} else {
					AAIEPFBean.setAdvPurpose("--");
				}

				if (rs.getString("AMOUNT") != null) {
					AAIEPFBean.setAdvAmt(rs.getString("AMOUNT"));
				} else {
					AAIEPFBean.setAdvAmt("0.00");
				}

				if (rs.getString("ADVTRANSDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("ADVTRANSDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				advancesList.add(AAIEPFBean);

			}

		} catch (SQLException e) {
			log.printStackTrace(new Exception("------SQL-----" + e));
		} catch (Exception e) {
			log.printStackTrace(e);
			e.printStackTrace();
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------advancesList------*********"
				+ advancesList.size());
		log
				.info("AAIEPFReportDAO::buildQuery1AAIEPFForm8Report Leaving Method");
		return advancesList;

	}

	public ArrayList buildQueryLoansForm8Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {
		log
				.info("AAIEPFReportDAO::buildQuery2AAIEPFForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList loansList = new ArrayList();
		if (!sortedColumn.equals("pensionno")) {
			sortedColumn = "LOANDATE";
		}

		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();
			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPL.EMPLOYEENO,EPL.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPL.AIRPORTCODE,EPL.REGION,EPL.LOANPURPOSE AS LOANPURPOSE,EPL.SUB_AMT AS EMPSHARE,EPL.CONT_AMT AS AAISHARE,EPL.LOANDATE AS LOANDATE  FROM employee_personal_info EPI, employee_pension_loans EPL WHERE EPI.PENSIONNO = EPL.PENSIONNO and EPL.LOANDATE BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  EPI.PENSIONNO >=" + startIndex
						+ " AND EPI.PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" EPL.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" EPL.AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))
					&& (range.equals("NO-SELECT") && (empNameFlag
							.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}
			orderBy = " ORDER BY  " + sortedColumn + " ASC";
			query.append(orderBy);
			dynamicQuery = query.toString();

			log.info("-------buildQuery2AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setDateOfBirth("--");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					AAIEPFBean.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					AAIEPFBean.setAirportCode("--");
				}

				if (rs.getString("REGION") != null) {
					AAIEPFBean.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean.setRegion("--");
				}

				// log.info("-----Pension
				// No----------"+AAIEPFBean.getPensionNo()
				// );

				if (rs.getString("PENSIONNO") != null) {
					AAIEPFBean.setPensionNo(commonDAO.getPFID(AAIEPFBean
							.getEmployeeName(), AAIEPFBean.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
				} else {
					AAIEPFBean.setPensionNo("--");
				}

				log.info("Employeeno" + AAIEPFBean.getEmployeeName()
						+ "Pensionno" + rs.getString("PENSIONNO"));

				if (rs.getString("LOANPURPOSE") != null) {
					AAIEPFBean.setLoanPurpose(rs.getString("LOANPURPOSE"));
				} else {
					AAIEPFBean.setLoanPurpose("--");
				}

				if (rs.getString("EMPSHARE") != null) {
					AAIEPFBean.setEmpShare(rs.getString("EMPSHARE"));
				} else {
					AAIEPFBean.setEmpShare("0.00");
				}

				if (rs.getString("AAISHARE") != null) {
					AAIEPFBean.setAAIShare(rs.getString("AAISHARE"));
				} else {
					AAIEPFBean.setAAIShare("0.00");
				}

				if (rs.getString("LOANDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("LOANDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				loansList.add(AAIEPFBean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------loansList size------*********"
				+ loansList.size());
		log
				.info("AAIEPFReportDAO::buildQuery2AAIEPFForm8Report Leaving Method");
		return loansList;

	}

	public ArrayList buildQueryFinalSttlementForm8Report(String range,
			String region, String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, Connection con) {
		log
				.info("AAIEPFReportDAO::buildQuery3AAIEPFForm8Report-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "";
		int startIndex = 0, endIndex = 0;
		Statement st = null;
		ResultSet rs = null;
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList settlementList = new ArrayList();
		if (!sortedColumn.equals("pensionno")) {
			sortedColumn = "SETTLEMENTDATE";
		}
		// sqlQuery=
		// "select
		// EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EPO.EMPNETOB
		// AS SUBSCRIPTIONAMT,EPO.AAINETOB AS
		// CONTRIBUTIONAMT,EPO.OUTSTANDADV,EPO.OBYEAR AS OBYEAR FROM
		// employee_personal_info EPI, employee_pension_ob EPO WHERE
		// EPI.PENSIONNO = EPO.PENSIONNO and EPO.OBYEAR BETWEEN '"
		// +fromYear+"' AND last_day('"+toYear+"') ";
		try {
			st = con.createStatement();
			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPF.EMPLOYEENO,EPF.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPF.AIRPORTCODE,EPF.REGION,EPF.PURPOSE AS REASON,EPF.FINEMP AS EMPSHARE,EPF.FINAAI AS AAISHARE,EPF.PENCON AS PENSIONCONTRIBUTION,EPF.NETAMOUNT AS NETAMOUNT,EPF.SETTLEMENTDATE AS SETTLEMENTDATE  FROM employee_personal_info EPI, employee_pension_finsettlement EPF WHERE EPI.PENSIONNO = EPF.PENSIONNO and EPF.SETTLEMENTDATE  BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";

			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  EPI.PENSIONNO >=" + startIndex
						+ " AND EPI.PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (!region.equals("NO-SELECT")) {
				whereClause.append(" EPF.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (!airportcode.equals("NO-SELECT")) {
				whereClause.append(" EPF.AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT"))
					&& (airportcode.equals("NO-SELECT"))
					&& (range.equals("NO-SELECT") && (empNameFlag
							.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}
			orderBy = " ORDER BY  " + sortedColumn + " ASC";
			query.append(orderBy);
			dynamicQuery = query.toString();

			log.info("-------buildQuery3AAIEPFForm8Report:sqlQuery---------"
					+ dynamicQuery);

			rs = st.executeQuery(dynamicQuery);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setDateOfBirth("--");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					AAIEPFBean.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					AAIEPFBean.setAirportCode("--");
				}

				if (rs.getString("REGION") != null) {
					AAIEPFBean.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean.setRegion("--");
				}

				// log.info("-----Pension
				// No----------"+AAIEPFBean.getPensionNo()
				// );

				if (rs.getString("PENSIONNO") != null) {
					AAIEPFBean.setPensionNo(commonDAO.getPFID(AAIEPFBean
							.getEmployeeName(), AAIEPFBean.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
					log.info("Employeeno" + AAIEPFBean.getEmployeeName()
							+ "Pensionno" + rs.getString("PENSIONNO"));
				} else {
					AAIEPFBean.setPensionNo("--");
				}

				if (rs.getString("REASON") != null) {
					AAIEPFBean.setSettlementReason(rs.getString("REASON"));
				} else {
					AAIEPFBean.setSettlementReason("--");
				}

				if (rs.getString("EMPSHARE") != null) {
					AAIEPFBean.setEmpShare(rs.getString("EMPSHARE"));
				} else {
					AAIEPFBean.setEmpShare("0.00");
				}

				if (rs.getString("AAISHARE") != null) {
					AAIEPFBean.setAAIShare(rs.getString("AAISHARE"));
				} else {
					AAIEPFBean.setAAIShare("0.00");
				}

				if (rs.getString("PENSIONCONTRIBUTION") != null) {
					AAIEPFBean.setPensionContribution(rs
							.getString("PENSIONCONTRIBUTION"));
				} else {
					AAIEPFBean.setPensionContribution("0.00");
				}

				if (rs.getString("NETAMOUNT") != null) {
					AAIEPFBean.setNetAmt(rs.getString("NETAMOUNT"));
				} else {
					AAIEPFBean.setNetAmt("0.00");
				}

				if (rs.getString("SETTLEMENTDATE") != null) {
					AAIEPFBean.setTransDate(CommonUtil.getDatetoString(rs
							.getDate("SETTLEMENTDATE"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setTransDate("--");
				}

				settlementList.add(AAIEPFBean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			// commonDB.closeConnection(con, st, rs);
		}

		log.info("*********------settlementList size------*********"
				+ settlementList.size());
		log
				.info("AAIEPFReportDAO::buildQuery3AAIEPFForm8Report Leaving Method");
		return settlementList;

	}

	// By Radha on 23-Dec-2011 for getting advanceAmnt_withoutkey value
	// By Radha on 15-Dec-2011 for AAI EPF Form 8 Summary Report For Tallying
	// Advances,Loans & Final Setlement Amnts from RPFC to Cask Book
	public ArrayList AAIEPFForm8SummaryReport(String range, String region,
			String airportcode, String frmSelectedDts, String empNameFlag,
			String empName, String sortedColumn, String pensionno) {

		log.info("AAIEPFReportDAO::AAIEPFForm8SummaryReport");

		Connection con = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		String fromYear = "", toYear = "", dateOfRetriment = "", form8SummaryReportProc = "";
		String[] dateArr = null;
		ArrayList form8SummaryList = new ArrayList();
		ARRAY array;
		Object vals[] = null;
		double advanceAmnt = 0.00, advanceAmnt_withoutkey = 0.00, loanAmnt = 0.00, loanAmnt_withoutkey = 0.00, finalsettlemntAmnt = 0.00, finalsettlemntAmnt_withoutkey = 0.00;
		double loanContriAmnt = 0.00, loanContriAmntwithoutkey = 0.00, fpcontriamt = 0.00, fpcontriwithoutkey = 0.00;
		double cbamount = 0.00, cbpfwsubamount = 0.00, cbpfwcontriamount = 0.00, cbfpaymentsubamount = 0.00, cbfpaymentcontriamount = 0.00;
		log.info("......frmSelectedDts in DAO......." + frmSelectedDts);

		if (!frmSelectedDts.equals("")) {

			dateArr = frmSelectedDts.split(",");
			fromYear = dateArr[0];
			toYear = dateArr[1];
		}

		try {

			con = DBUtils.getConnection();

			form8SummaryReportProc = "{  call SUMMARY_OF_FORM_8(?,?,?) }";
			cst = con.prepareCall(form8SummaryReportProc);
			cst.setString(1, fromYear);
			cst.setString(2, toYear);
			cst.registerOutParameter(3, OracleTypes.ARRAY, "FORM8LIST");
			cst.executeQuery();
			array = (ARRAY) cst.getArray(3);
			rs = array.getResultSet();
			while (rs.next()) {
				oracle.sql.STRUCT obj = (STRUCT) rs.getObject(2);
				vals = obj.getAttributes();
			}
			advanceAmnt = Double.parseDouble(vals[0].toString());
			advanceAmnt_withoutkey = Double.parseDouble(vals[1].toString());
			loanAmnt = Double.parseDouble(vals[2].toString());
			loanAmnt_withoutkey = Double.parseDouble(vals[3].toString());
			loanContriAmnt = Double.parseDouble(vals[4].toString());
			loanContriAmntwithoutkey = Double.parseDouble(vals[5].toString());
			finalsettlemntAmnt = Double.parseDouble(vals[6].toString());
			finalsettlemntAmnt_withoutkey = Double.parseDouble(vals[7]
					.toString());
			fpcontriamt = Double.parseDouble(vals[8].toString());
			fpcontriwithoutkey = Double.parseDouble(vals[9].toString());
			cbamount = Double.parseDouble(vals[10].toString());
			cbpfwsubamount = Double.parseDouble(vals[11].toString());
			cbpfwcontriamount = Double.parseDouble(vals[12].toString());
			cbfpaymentsubamount = Double.parseDouble(vals[13].toString());
			cbfpaymentcontriamount = Double.parseDouble(vals[14].toString());

			form8SummaryList.add(String.valueOf(advanceAmnt));
			form8SummaryList.add(String.valueOf(advanceAmnt_withoutkey));
			form8SummaryList.add(String.valueOf(loanAmnt));
			form8SummaryList.add(String.valueOf(loanAmnt_withoutkey));
			form8SummaryList.add(String.valueOf(loanContriAmnt));
			form8SummaryList.add(String.valueOf(loanContriAmntwithoutkey));
			form8SummaryList.add(String.valueOf(finalsettlemntAmnt));
			form8SummaryList.add(String.valueOf(finalsettlemntAmnt_withoutkey));
			form8SummaryList.add(String.valueOf(fpcontriamt));
			form8SummaryList.add(String.valueOf(fpcontriwithoutkey));
			form8SummaryList.add(String.valueOf(cbamount));
			form8SummaryList.add(String.valueOf(cbpfwsubamount));
			form8SummaryList.add(String.valueOf(cbpfwcontriamount));
			form8SummaryList.add(String.valueOf(cbfpaymentsubamount));
			form8SummaryList.add(String.valueOf(cbfpaymentcontriamount));
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (cst != null) {
				try {
					cst.close();
					cst = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			commonDB.closeConnection(con, null, rs);
		}

		// log.info("====AAIEPFReportList Size in DAO====="+
		// AAIEPFReportList.size());
		return form8SummaryList;

	}

	// Modified By Prasad On 25-Jul-2011 For displaying Supplimentary Detials
	public ArrayList getEPFForm5Report(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selQuery = "", fromDate = "", toDate = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form5DataList = new ArrayList();
		String regionDesc = "", stationDesc = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}

		selQuery = this.buildQueryForm5TransInfo(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate, empNameFlag, empName,
				frmPensionno);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm5Report===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0;
				epfForm3Bean = new AaiEpfform3Bean();

				if (rs.getString("NOOFSUBSCRIBERS") != null) {
					epfForm3Bean.setTotalSubscribers(rs
							.getString("NOOFSUBSCRIBERS"));
				} else {
					epfForm3Bean.setTotalSubscribers("---");
				}

				if (rs.getString("ORDMONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(rs.getString("ORDMONTHYEAR"));
				} else {
					epfForm3Bean.setMonthyear("---");
				}

				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}

				form5DataList.add(epfForm3Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form5DataList;
	}

	// Modified By Prasad On 25-Jul-2011 For displaying Supplimentary Detials
	public String buildQueryForm5TransInfo(String region, String airportcode,
			String sortedColumn, String fromDate, String toDate,
			String empNameFlag, String empName, String pensionno) {
		log
				.info("EPFFormsReportDAO::buildQueryForm5TransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer whereClause1 = new StringBuffer();
		StringBuffer whereClause2 = new StringBuffer();
		StringBuffer query = new StringBuffer();
		StringBuffer query1 = new StringBuffer();
		StringBuffer query2 = new StringBuffer();
		StringBuffer query3 = new StringBuffer();
		String dynamicQuery = "", sqlQuery1 = "", sqlQuery2 = "", selQuery = "",sqlQuery3="";
		log.info("empNameFlag" + empNameFlag + "frmPensionno" + pensionno
				+ "empName" + empName);
		selQuery = "SELECT AIRPORTCODE,sum(NOOFSUBSCRIBERS) as NOOFSUBSCRIBERS,ORDMONTHYEAR,SUM(EMOLUMENTS) as EMOLUMENTS,SUM(EMPPFSTATUARY) AS EMPPFSTATUARY,SUM(EMPVPF) AS EMPVPF,SUM(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,SUM(EMPADVRECINTEREST) AS EMPADVRECINTEREST,SUM(PENSIONCONTRI) AS PENSIONCONTRI, SUM(PF) AS PF FROM ((";
		sqlQuery1 = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(trans.monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
				+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
				+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF FROM EMPLOYEE_PENSION_VALIDATE TRANS "
				+ "WHERE EMPFLAG = 'Y' AND SUPPLIFLAG = 'N' AND MERGEFLAG='N'  AND PENSIONNO IS NOT NULL AND  MONTHYEAR BETWEEN '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";
		sqlQuery2 = "SELECT S.AIRPORTCODE AS AIRPORTCODE,COUNT(S.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(S.monthyear, 'Mon-yyyy') AS MONTHYEAR,('01-' || to_char(s.monthyear, 'Mon-yyyy')) as ORDMONTHYEAR,"
				+ "SUM(round(S.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(S.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(S.EMPVPF)) AS EMPVPF,SUM(round(S.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
				+ "SUM(round(S.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(S.PENSIONCONTRI)) AS PENSIONCONTRI,SUM(round(S.PF)) AS PF FROM EMPLOYEE_SUPPLIMENTORY_DATA S,EMPLOYEE_PENSION_VALIDATE PVAL  "
				+ "WHERE PVAL.PENSIONNO=S.PENSIONNO AND PVAL.Monthyear = S.Monthyear and PVAL.SUPPLIFLAG='Y' AND S.EMPFLAG=PVAL.EMPFLAG AND S.EMPFLAG = 'Y' AND S.SUPPLIFLAG = 'N' AND S.PENSIONNO IS NOT NULL AND S.Monthyear BETWEEN '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";
		sqlQuery3 =  "SELECT mergers.AIRPORTCODE AS AIRPORTCODE,COUNT(mergers.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(mergers.monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(mergers.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
			+ "SUM(round(mergers.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(mergers.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(mergers.EMPVPF)) AS EMPVPF,SUM(round(mergers.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
			+ "SUM(round(mergers.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(mergers.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(mergers.PF)) AS PF FROM EMPLOYEE_PENSION_VALIDATE TRANS1,emp_pension_validate_merger mergers "
			+ "WHERE trans1.EMPFLAG = 'Y' AND trans1.MERGEFLAG = 'Y' AND  mergers.pensionno = trans1.pensionno and mergers.monthyear = trans1.monthyear and TRANS1.SUPPLIFLAG = 'N'   AND mergers.PENSIONNO IS NOT NULL AND  mergers.MONTHYEAR BETWEEN '"
			+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
			whereClause1.append(" S.REGION ='" + region + "'");
			whereClause1.append(" AND ");
			whereClause2.append(" mergers.REGION ='" + region + "'");
			whereClause2.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
			whereClause1.append(" upper(S.AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause1.append(" AND ");
			whereClause2.append(" upper(mergers.AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause2.append(" AND ");
			
		}
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
				whereClause1.append(" S.PENSIONNO='" + pensionno + "' ");
				whereClause1.append(" AND ");
				whereClause2.append(" mergers.PENSIONNO='" + pensionno + "' ");
				whereClause2.append(" AND ");
			}
		}

		query1.append(sqlQuery1);
		query2.append(sqlQuery2);
		query3.append(sqlQuery3);
		if ((region.equals("")) && (empNameFlag.equals("false"))
				&& (airportcode.equals(""))) {

		} else {
			query1.append(" AND ");
			query1.append(commonDAO.sTokenFormat(whereClause));
			query2.append(" AND ");
			query2.append(commonDAO.sTokenFormat(whereClause1));
			query3.append(" AND ");
			query3.append(commonDAO.sTokenFormat(whereClause2));
		}
		
	

	
		
		
	
		String groupBy1 = "GROUP BY TRANS.AIRPORTCODE,to_char(trans.monthyear,'Mon-yyyy')";
		String groupBy2 = "GROUP BY S.AIRPORTCODE,to_char(S.monthyear,'Mon-yyyy')";
		String groupBy3 = "GROUP BY mergers.AIRPORTCODE,to_char(mergers.monthyear,'Mon-yyyy')";
		String orderBy = ")) GROUP BY AIRPORTCODE, ORDMONTHYEAR ORDER BY " + sortedColumn
				+ " ,to_date(ORDMONTHYEAR) ";
		String unionAll = ") UNION ALL (";

		query1.append(groupBy1);
		query2.append(groupBy2);
		query3.append(groupBy3);
		query.append(selQuery);
		query.append(query1);
		query.append(unionAll);
		query.append(query2);
		query.append(unionAll);
		query.append(query3);
		query.append(orderBy);

		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryForm5TransInfo Leaving Method");
		return dynamicQuery;

	}

	public ArrayList getEPFForm5SuppliBlockReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selQuery = "", fromDate = "", toDate = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList suppliForm5List = new ArrayList();
		String regionDesc = "", stationDesc = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}

		selQuery = this.buildQueryForm5SuppliInfo(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate, empNameFlag, empName,
				frmPensionno);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm5SuppliReport===pppquery"
					+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0;
				epfForm3Bean = new AaiEpfform3Bean();

				if (rs.getString("NOOFSUBSCRIBERS") != null) {
					epfForm3Bean.setTotalSubscribers(rs
							.getString("NOOFSUBSCRIBERS"));
				} else {
					epfForm3Bean.setTotalSubscribers("---");
				}

				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(rs.getString("MONTHYEAR"));
				} else {
					epfForm3Bean.setMonthyear("---");
				}

				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}

				suppliForm5List.add(epfForm3Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return suppliForm5List;
	}

	public String buildQueryForm5SuppliInfo(String region, String airportcode,
			String sortedColumn, String fromDate, String toDate,
			String empNameFlag, String empName, String pensionno) {
		log
				.info("EPFFormsReportDAO::buildQueryForm5suppliInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		log.info("empNameFlag" + empNameFlag + "frmPensionno" + pensionno
				+ "empName" + empName);
		sqlQuery = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(trans.monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
				+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
				+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF FROM Employee_Supplimentory_Data TRANS "
				+ "WHERE EMPFLAG = 'Y' AND SUPPLIFLAG = 'S'  AND PENSIONNO IS NOT NULL AND  PAIDDATE BETWEEN '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
		}
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (empNameFlag.equals("false"))
				&& (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String groupBy = "GROUP BY TRANS.AIRPORTCODE,to_char(trans.monthyear,'Mon-yyyy')";
		String orderBy = "ORDER BY TRANS." + sortedColumn
				+ " ,to_date(ORDMONTHYEAR) ";
		query.append(groupBy + orderBy);
		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryForm5suppliInfo4444444444444 Leaving Method");
		return dynamicQuery;
	}

	public ArrayList getEPFForm5ArrearBlockReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selQuery = "", fromDate = "", toDate = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList arrearForm5List = new ArrayList();
		String regionDesc = "", stationDesc = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}

		selQuery = this.buildQueryForm5ArrearInfo(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate, empNameFlag, empName,
				frmPensionno);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm5ArrearReport===pppquery"
					+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0;
				epfForm3Bean = new AaiEpfform3Bean();

				if (rs.getString("NOOFSUBSCRIBERS") != null) {
					epfForm3Bean.setTotalSubscribers(rs
							.getString("NOOFSUBSCRIBERS"));
				} else {
					epfForm3Bean.setTotalSubscribers("---");
				}

				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(rs.getString("MONTHYEAR"));
				} else {
					epfForm3Bean.setMonthyear("---");
				}

				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}

				arrearForm5List.add(epfForm3Bean);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return arrearForm5List;
	}

	public String buildQueryForm5ArrearInfo(String region, String airportcode,
			String sortedColumn, String fromDate, String toDate,
			String empNameFlag, String empName, String pensionno) {
		log
				.info("EPFFormsReportDAO::buildQueryForm5suppliInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		log.info("empNameFlag" + empNameFlag + "frmPensionno" + pensionno
				+ "empName" + empName);
		sqlQuery = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(trans.monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
				+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
				+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF FROM Employee_Supplimentory_Data TRANS "
				+ "WHERE EMPFLAG = 'Y' AND SUPPLIFLAG = 'A'  AND PENSIONNO IS NOT NULL AND  MONTHYEAR BETWEEN '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
		}
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (empNameFlag.equals("false"))
				&& (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String groupBy = "GROUP BY TRANS.AIRPORTCODE,to_char(trans.monthyear,'Mon-yyyy')";
		String orderBy = "ORDER BY TRANS." + sortedColumn
				+ " ,to_date(ORDMONTHYEAR) ";
		query.append(groupBy + orderBy);
		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryForm5suppliInfo4444444444444 Leaving Method");
		return dynamicQuery;
	}

	public ArrayList getMissingTransPFIDs(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form3DataList = new ArrayList();
		String regionDesc = "", stationDesc = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}

		selQuery = this.buildQueryTransMissingPFIDsInfo(regionDesc,
				stationDesc, sortingOrder, fromDate, toDate);

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getMissingTransPFIDs===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

				} else {
					epfForm3Bean.setPensionno("---");
					epfForm3Bean.setPfID("---");
				}

				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}

				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				form3DataList.add(epfForm3Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}

	public String buildQueryTransMissingPFIDsInfo(String region,
			String airportcode, String sortedColumn, String fromDate,
			String toDate) {
		log
				.info("EPFFormsReportDAO::buildQueryTransMissingPFIDsInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT MONTHYEAR, EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, CPF, round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, round(AAICONPF) AS AAICONPF, ROUND(CPFADVANCE) AS CPFADVANCE, ROUND(DEDADV) AS DEDADV, ROUND(REFADV) AS REFADV, PENSIONCONTRI, PF, AIRPORTCODE, "
				+ "REGION, EMPFLAG, CPFACCNO,PENSIONNO,DESEGNATION,EMPLOYEENO,EMPLOYEENAME FROM EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG = 'Y' AND PENSIONNO IS NULL  and MONTHYEAR between '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY CPFACCNO,TO_DATE(MONTHYEAR)";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryTransMissingPFIDsInfo Leaving Method");
		return dynamicQuery;
	}

	// Modified By Radha on 29-Jul-2011 for showing the differenciation for
	// Active & Deactivated Employees information
	public ArrayList getEPFForm11Report(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno,
			String status) {

		String selQuery = "";
		AaiEpfForm11Bean form11Bean = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form11DataList = new ArrayList();
		String regionDesc = "", stationDesc = "", seperationReason = "", seperationDate = "", fromYear = "", toYear = "", resettlmenetDate = "", finalSettlementDate = "";
		int nextYear = 0;
		FinancialReportDAO reportDAO = new FinancialReportDAO();
		double rateOfIntrest = 0.0;
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
		String[] splitYear = frmSelctedYear.split("-");
		if (splitYear.length != 0) {
			rateOfIntrest = commonDAO.getRateOfInterest(splitYear[0]);
		}
		fromYear = "01-Apr-" + splitYear[0];
		nextYear = Integer.parseInt(splitYear[0]) + 1;
		toYear = "31-Mar-" + nextYear;
		selQuery = this.buildQueryEmpYearTotalInfo(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				frmSelctedYear, rateOfIntrest, status);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm11Report===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {

				form11Bean = new AaiEpfForm11Bean();
				if (rs.getString("PENSIONNO") != null) {
					form11Bean.setPensionNo(commonUtil.leadingZeros(5, rs
							.getString("PENSIONNO")));

				}
				// reportDAO.ProcessforAdjOb(form11Bean.getPensionNo(),true);
				if (rs.getString("CPFACNO") != null) {
					form11Bean.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					form11Bean.setCpfAccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					form11Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					form11Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					form11Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					form11Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					form11Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					form11Bean.setDesignation("---");
				}
				if (rs.getString("FHNAME") != null) {
					form11Bean.setFhName(rs.getString("FHNAME"));
				} else {
					form11Bean.setFhName("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					form11Bean.setDateOfBirth(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					form11Bean.setDateOfBirth("---");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					seperationDate = commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE"));
				} else {
					seperationDate = "---";
				}

				if (rs.getString("RESETTLEMENTDATE") != null) {
					resettlmenetDate = commonUtil.converDBToAppFormat(rs
							.getDate("RESETTLEMENTDATE"));
				} else {
					resettlmenetDate = "---";
				}

				if (rs.getString("FINALSETTLMENTDT") != null) {
					finalSettlementDate = commonUtil.converDBToAppFormat(rs
							.getDate("FINALSETTLMENTDT"));
				} else {
					finalSettlementDate = "---";
				}

				// form11Bean=this.getFinalSettlmentInformation(con,form11Bean,
				// form11Bean
				// .getPensionNo(),fromYear,toYear,finalSettlementDate,
				// resettlmenetDate);
				if (!form11Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(form11Bean
							.getEmployeeName(), form11Bean.getDateOfBirth(),
							form11Bean.getPensionNo());
					form11Bean.setPfID(personalPFID);
				} else {
					form11Bean.setPfID(form11Bean.getPensionNo());
				}

				if (rs.getString("WETHEROPTION") != null) {
					form11Bean.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					form11Bean.setWetherOption("---");
				}

				if (rs.getString("EMPNETOB") != null) {
					form11Bean.setObEmpSub(rs.getString("EMPNETOB"));
				} else {
					form11Bean.setObEmpSub("0.00");
				}
				if (rs.getString("AAINETOB") != null) {
					form11Bean.setObAAISub(rs.getString("AAINETOB"));
				} else {
					form11Bean.setObAAISub("0.00");
				}
				if (rs.getString("OUTSTANDADV") != null) {
					form11Bean.setOutStndAdv(rs.getString("OUTSTANDADV"));
				} else {
					form11Bean.setOutStndAdv("0.00");
				}

				if (rs.getString("PRINCIPALOB") != null) {
					form11Bean.setObPrincipal(rs.getString("PRINCIPALOB"));
				} else {
					form11Bean.setObPrincipal("0.00");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					form11Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					form11Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					form11Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					form11Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					form11Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					form11Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					form11Bean.setPrincipal(rs.getString("EMPADVRECPRINCIPAL"));
				} else {
					form11Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					form11Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					form11Bean.setInterest("0.00");
				}
				if (rs.getString("TOTAL") != null) {
					form11Bean.setEmptotal(rs.getString("TOTAL"));
				} else {
					form11Bean.setEmptotal("0.00");
				}
				if (rs.getString("PF") != null) {
					form11Bean.setPf(rs.getString("PF"));
				} else {
					form11Bean.setPf("0.00");
				}
				if (rs.getString("PENSIONCONTRI") != null) {
					form11Bean.setPensinonContr(rs.getString("PENSIONCONTRI"));
				} else {
					form11Bean.setPensinonContr("0.00");
				}

				if (rs.getString("AAITOTAL") != null) {
					form11Bean.setAaiTotal(rs.getString("AAITOTAL"));
				} else {
					form11Bean.setAaiTotal("0.00");
				}
				if (rs.getString("CPFTOTAL") != null) {
					form11Bean.setAdjCPF(rs.getString("CPFTOTAL"));
				} else {
					form11Bean.setAdjCPF("0.00");
				}
				if (rs.getString("CPFINTEREST") != null) {
					form11Bean.setAdjCPFInt(rs.getString("CPFINTEREST"));
				} else {
					form11Bean.setAdjCPFInt("0.00");
				}
				if (rs.getString("ADJPENSIONTOTAL") != null) {
					form11Bean.setAdjPension(rs.getString("ADJPENSIONTOTAL"));
				} else {
					form11Bean.setAdjPension("0.00");
				}

				if (rs.getString("ADJEMPSUBTOTAL") != null) {
					form11Bean
							.setAdjEmpSubTotal(rs.getString("ADJEMPSUBTOTAL"));
				} else {
					form11Bean.setAdjEmpSubTotal("0.00");
				}
				if (rs.getString("ADJEMPSUBINTEREST") != null) {
					form11Bean.setAdjEmpSubInterest(rs
							.getString("ADJEMPSUBINTEREST"));
				} else {
					form11Bean.setAdjEmpSubInterest("0.00");
				}

				if (rs.getString("ADJPENSIONINTEREST") != null) {
					form11Bean.setAdjPensionInt(rs
							.getString("ADJPENSIONINTEREST"));
				} else {
					form11Bean.setAdjPensionInt("0.00");
				}
				if (rs.getString("PFTOTAL") != null) {
					form11Bean.setAdjPF(rs.getString("PFTOTAL"));
				} else {
					form11Bean.setAdjPF("0.00");
				}
				if (rs.getString("PFINTEREST") != null) {
					form11Bean.setAdjPFInt(rs.getString("PFINTEREST"));
				} else {
					form11Bean.setAdjPFInt("0.00");
				}
				if (rs.getString("SUB_AMT") != null) {
					form11Bean.setPfwSubscr(rs.getString("SUB_AMT"));
				} else {
					form11Bean.setPfwSubscr("0.00");
				}
				if (rs.getString("CONT_AMT") != null) {
					form11Bean.setPfwContr(rs.getString("CONT_AMT"));
				} else {
					form11Bean.setPfwContr("0.00");
				}
				if (rs.getString("REFADVPFWNRW") != null) {
					form11Bean.setRefAdvPFW(rs.getString("REFADVPFWNRW"));
				} else {
					form11Bean.setRefAdvPFW("0.00");
				}
				if (rs.getString("ADVANCE") != null) {
					form11Bean.setAdvanceAmnt(rs.getString("ADVANCE"));
				} else {
					form11Bean.setAdvanceAmnt("0.00");
				}
				if (rs.getString("EMPINTERSTCR") != null) {
					form11Bean
							.setInterestCredited(rs.getString("EMPINTERSTCR"));
				} else {
					form11Bean.setInterestCredited("0.00");
				}
				if (rs.getString("AAIINTERSTCRD") != null) {
					form11Bean.setAaiIntersetCredited(rs
							.getString("AAIINTERSTCRD"));
				} else {
					form11Bean.setAaiIntersetCredited("0.00");
				}
				if (rs.getString("CLBAL") != null) {
					form11Bean.setClosingBal(rs.getString("CLBAL"));
				} else {
					form11Bean.setClosingBal("0.00");
				}
				if (rs.getString("AAICLBL") != null) {
					form11Bean.setAaiClosingBal(rs.getString("AAICLBL"));
				} else {
					form11Bean.setAaiClosingBal("0.00");
				}

				if (rs.getString("ADJEMPSUB") != null) {
					form11Bean.setAdjEmpSub(rs.getString("ADJEMPSUB"));
				} else {
					form11Bean.setAdjEmpSub("0.00");
				}
				if (rs.getString("ADJAAICONTR") != null) {
					// log.info("Pension
					// No"+form11Bean.getPensionNo()+"Sign"+rs.
					// getString("ADJSIGNAAICONTR")+"ADJAAICONTR"+rs.getString(
					// "ADJAAICONTR"));
					if (rs.getString("ADJSIGNAAICONTR") != null) {
						form11Bean.setAdjAaiContr(rs.getString("ADJAAICONTR"));
					} else {
						form11Bean.setAdjAaiContr("-"
								+ rs.getString("ADJAAICONTR"));
					}

				} else {
					form11Bean.setAdjAaiContr("0.00");
				}
				form11Bean.setFinalPayment("0.00");
				form11Bean.setTnsfrOthrOrgEmpShare("0.00");
				if (rs.getString("AIRPORTCODE") != null) {
					form11Bean.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					form11Bean.setAirportCode("---");
				}
				if (rs.getString("REGION") != null) {
					form11Bean.setRegion(rs.getString("REGION"));
				} else {
					form11Bean.setRegion("---");
				}
				if (rs.getString("ADJEMPSUB") != null) {
					form11Bean.setAdjEmpSub(rs.getString("ADJEMPSUB"));
				} else {
					form11Bean.setAdjEmpSub("0.00");
				}
				if (rs.getString("FINEMP") != null) {
					form11Bean.setFinalEmpSubscr(rs.getString("FINEMP"));
				} else {
					form11Bean.setFinalEmpSubscr("0.00");
				}
				if (rs.getString("FINAAI") != null) {
					form11Bean.setFinalAAIContr(rs.getString("FINAAI"));
				} else {
					form11Bean.setFinalAAIContr("0.00");
				}

				if (rs.getString("PENSIONCONTRIADJ") != null) {
					form11Bean.setPensionContriAdj(rs
							.getString("PENSIONCONTRIADJ"));
				} else {
					form11Bean.setPensionContriAdj("0.00");
				}
				String chkMonthStatus = "";
				if (rs.getString("MONTHSTATUS") != null) {
					chkMonthStatus = rs.getString("MONTHSTATUS");
					if (chkMonthStatus.equals("B")) {
						form11Bean.setHighlightedColor("bgcolor='lightblue'");
					} else {
						form11Bean.setHighlightedColor("");
					}

				}
				if (!seperationDate.equals("---")) {
					if (this.compareDates(fromYear, "01-Apr-" + nextYear,
							seperationDate) == true) {
						if (rs.getString("DATEOFSEPERATION_REASON") != null) {
							seperationReason = rs
									.getString("DATEOFSEPERATION_REASON");
							if (!seperationReason.equals("Other")) {
								if (seperationReason.equals("Death")) {
									form11Bean
											.setRemarks("Expired & PF Settled");
								} else if (seperationReason
										.equals("Retirement")) {
									form11Bean
											.setRemarks("Retired & PF Settled");
								} else {
									form11Bean.setRemarks(seperationReason);
								}

							} else {
								form11Bean.setRemarks("---");
							}

						} else {
							form11Bean.setRemarks("---");
						}
					} else {
						form11Bean.setRemarks("---");
					}
				} else {
					form11Bean.setRemarks("---");
				}

				// this.getFinalSettlement(con,form11Bean.getPensionNo(),
				// frmSelctedYear,form11Bean);
				form11DataList.add(form11Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form11DataList;
	}

	// Modified By Radha on 14-Sep-2011 for showing the PENSIONCONTRIADJ amounts
	// Modified By Radha on 27-Jul-2011 for showing the signs properly for
	// adjustments amounts
	// Modified By Radha on 29-Jul-2011 for showing the differenciation for
	// Active & Deactivated Employees information
	public String buildQueryEmpYearTotalInfo(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String frmSelectedYear,
			double rateOfInterest, String status) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpYearTotalInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0, frmYear = 0;
		String[] splitYear = frmSelectedYear.split("-");
		frmYear = Integer.parseInt(splitYear[0]);
		if (frmYear <= 2009) {
			sqlQuery = "SELECT PENSIONNO, CPFACNO, EMPLOYEENO, EMPLOYEENAME , DESEGNATION , DATEOFBIRTH , DATEOFJOINING, DATEOFSEPERATION_DATE,RESETTLEMENTDATE,FINALSETTLMENTDT,"
					+ "DATEOFSEPERATION_REASON , WETHEROPTION, FHNAME, AIRPORTCODE ,REGION , EMPNETOB, AAINETOB, OUTSTANDADV, PRINCIPALOB , "
					+ "EMOLUMENTS  , EMPPFSTATUARY , EMPVPF  , EMPADVRECPRINCIPAL, EMPADVRECINTEREST , TOTAL , PENSIONCONTRI ,"
					+ "PF,AAITOTAL , FINYEAR,SUB_AMT, CONT_AMT, CPFTOTAL,CPFINTEREST ,ADJEMPSUB,ADJEMPSUBINTEREST,NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) AS ADJEMPSUBTOTAL"
					+ ",ADJPENSIONTOTAL, ADJPENSIONINTEREST,NVL((ADJPENSIONTOTAL+ADJPENSIONINTEREST),0.00) AS ADJAAICONTR,SIGN((ADJPENSIONTOTAL+ADJPENSIONINTEREST)) AS ADJSIGNAAICONTR, PFTOTAL, PFINTEREST,  PARTAMT, AMOUNT , ADVANCE, REFADVPFWNRW , (EMPINTERSTCR+NVL(NETCLOSINGEMPNET,0.00)) AS EMPINTERSTCR,(NVL(EMPNETOB,0.00) + NVL((ADJEMPSUB+ADJEMPSUBINTEREST),0.00)+ NVL(TOTAL,0.00) - NVL(REFADVPFWNRW,0.00) "
					+ "+NVL(EMPINTERSTCR,0.00)-NVL(FINEMP,0.00)+NVL(NETCLOSINGEMPNET,0.00)) AS CLBAL,(NVL(NETCLOSINGAAINET,0.00)+AAIINTERSTCR) AS AAIINTERSTCRD,(NVL(AAINETOB,'0.00')-NVL((ADJPENSIONTOTAL+ADJPENSIONINTEREST),0.00)+NVL(PF,0.00)-NVL(CONT_AMT,0.00)+NVL(AAIINTERSTCR,0.00)-NVL(FINAAI,0.00)+NVL(NETCLOSINGAAINET,0.00)) AS AAICLBL,NVL(FINEMP,0.00) AS FINEMP, "
					+ "NVL(NETCLOSINGEMPNET,0.00) AS NETCLOSINGEMPNET,NVL(ARREAREMPNET,0.00) AS ARREAREMPNET,NVL(ARREAREMPNETINT,0.00) AS ARREAREMPNETINT,NVL(FINAAI,0.00) AS FINAAI,NVL(NETCLOSINGAAINET,0.00) AS NETCLOSINGAAINET,NVL(ARREARAAINET,0.00) AS ARREARAAINET,NVL(ARREARAAINETINT,0.00) AS ARREARAAINETINT,MONTHSTATUS,PENSIONCONTRIADJ  FROM V_EMP_PENSION_YEAR_TOTALS";
		} else {
			sqlQuery = "SELECT PENSIONNO, CPFACNO, EMPLOYEENO, EMPLOYEENAME , DESEGNATION , DATEOFBIRTH , DATEOFJOINING, DATEOFSEPERATION_DATE,RESETTLEMENTDATE,FINALSETTLMENTDT,"
					+ "DATEOFSEPERATION_REASON , WETHEROPTION, FHNAME, AIRPORTCODE ,REGION , EMPNETOB, AAINETOB, OUTSTANDADV, PRINCIPALOB , "
					+ "EMOLUMENTS  , EMPPFSTATUARY , EMPVPF  , EMPADVRECPRINCIPAL, EMPADVRECINTEREST , TOTAL , PENSIONCONTRI ,"
					+ "PF,AAITOTAL , FINYEAR,SUB_AMT, CONT_AMT, CPFTOTAL,CPFINTEREST ,ADJEMPSUB,ADJEMPSUBINTEREST,NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) AS ADJEMPSUBTOTAL"
					+ ",ADJPENSIONTOTAL, ADJPENSIONINTEREST,-1*(NVL((ADJPENSIONTOTAL+ADJPENSIONINTEREST),0.00)) AS ADJAAICONTR,SIGN((ADJPENSIONTOTAL+ADJPENSIONINTEREST)) AS ADJSIGNAAICONTR, PFTOTAL, PFINTEREST,  PARTAMT, AMOUNT , ADVANCE, REFADVPFWNRW , (EMPINTERSTCR+NVL(NETCLOSINGEMPNET,0.00)) AS EMPINTERSTCR,(NVL(EMPNETOB,0.00) + NVL((ADJEMPSUB+ADJEMPSUBINTEREST),0.00)+ NVL(TOTAL,0.00) - NVL(REFADVPFWNRW,0.00) "
					+ "+NVL(EMPINTERSTCR,0.00)-NVL(FINEMP,0.00)+NVL(NETCLOSINGEMPNET,0.00)) AS CLBAL,(NVL(NETCLOSINGAAINET,0.00)+AAIINTERSTCR) AS AAIINTERSTCRD,(NVL(AAINETOB,'0.00')-NVL((ADJPENSIONTOTAL+ADJPENSIONINTEREST),0.00)+NVL(PF,0.00)-NVL(CONT_AMT,0.00)+NVL(AAIINTERSTCR,0.00)-NVL(FINAAI,0.00)+NVL(NETCLOSINGAAINET,0.00)) AS AAICLBL,NVL(FINEMP,0.00) AS FINEMP, "
					+ "NVL(NETCLOSINGEMPNET,0.00) AS NETCLOSINGEMPNET,NVL(ARREAREMPNET,0.00) AS ARREAREMPNET,NVL(ARREAREMPNETINT,0.00) AS ARREAREMPNETINT,NVL(FINAAI,0.00) AS FINAAI,NVL(NETCLOSINGAAINET,0.00) AS NETCLOSINGAAINET,NVL(ARREARAAINET,0.00) AS ARREARAAINET,NVL(ARREARAAINETINT,0.00) AS ARREARAAINETINT,MONTHSTATUS,PENSIONCONTRIADJ  FROM V_EMP_PENSION_YEAR_TOTALS";
		}

		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}
		if (!frmSelectedYear.equals("")) {
			whereClause.append(" FINYEAR ='" + frmSelectedYear + "'");
			whereClause.append(" AND ");
		}
		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" LOWER(AIRPORTCODE) ='"
					+ airportcode.toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		if (!status.equals("")) {
			whereClause.append(" EMPACTSTS ='" + status + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals(""))
				&& (frmSelectedYear.equals(""))
				&& (airportcode.equals(""))
				&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")) && (status
						.equals("")))) {

		} else {
			query.append(" WHERE ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryEmpYearTotalInfo Leaving Method");
		return dynamicQuery;
	}

	public ArrayList getEPFForm6Report(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selPersonalQuery = "", sqlQuery = "";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form6DataList = new ArrayList();
		String regionDesc = "", stationDesc = "", regionQryString = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
		if (!regionDesc.equals("")) {
			if (!stationDesc.equals("")) {
				regionQryString = " AND REGION='" + regionDesc
						+ "' AND AIRPORTCODE='" + stationDesc + "' ";
			} else {
				regionQryString = " AND REGION='" + regionDesc + "'";
			}

		} else {
			regionQryString = "";
		}
		selPersonalQuery = this.buildQueryForm6PersonalInfo(range, regionDesc,
				stationDesc, empName, empNameFlag, frmPensionno);
		/* if(!regionDesc.equals("")){ */
		if (!regionDesc.equals("CHQIAD")) {
			sqlQuery = "SELECT TRANS.PENSIONNO, PERSONAL.CPFACCNO, PERSONAL.DATEOFBIRTH,PERSONAL.EMPLOYEENO, PERSONAL.EMPLOYEENAME, PERSONAL.DESEGNATION,TRANS.EMOLUMENTS, TRANS.EMPPFSTATUARY,"
					+ "TRANS.EMPVPF, TRANS.EMPADVRECPRINCIPAL, TRANS.EMPADVRECINTEREST,TRANS.TOTAL, TRANS.PENSIONCONTRI, TRANS.PF, TRANS.AAITOTAL, TRANS.FINYEAR, PERSONAL.AIRPORTCODE, PERSONAL.REGION"
					+ " FROM ("
					+ selPersonalQuery
					+ ") PERSONAL,(SELECT TRANS.PENSIONNO, SUM(ROUND(NVL(TRANS.EMOLUMENTS,'0.00'))) AS EMOLUMENTS,SUM(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) AS EMPPFSTATUARY, SUM(ROUND(NVL(TRANS.EMPVPF,'0.00'))) AS EMPVPF, "
					+ "SUM(ROUND(NVL(TRANS.EMPADVRECPRINCIPAL,'0.00'))) AS EMPADVRECPRINCIPAL, SUM(ROUND(NVL(TRANS.EMPADVRECINTEREST,'0.00'))) AS EMPADVRECINTEREST,SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00'))) AS PENSIONCONTRI, SUM(ROUND(NVL(TRANS.PF,'0.00'))) AS PF,"
					+ "SUM(TO_NUMBER(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) + TO_NUMBER(ROUND(NVL(TRANS.EMPVPF,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECPRINCIPAL,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECINTEREST,'0.00')))) AS TOTAL, SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00')) + ROUND(NVL(TRANS.PF,'0.00'))) AS AAITOTAL"
					+ ",FINYEAR,REGION FROM EMPLOYEE_PENSION_VALIDATE TRANS WHERE EMPFLAG='Y' AND PENSIONNO IS NOT NULL AND FINYEAR = '"
					+ frmSelctedYear
					+ "'"
					+ regionQryString
					+ "  GROUP BY PENSIONNO, FINYEAR,REGION) TRANS WHERE PERSONAL.PENSIONNO = TRANS.PENSIONNO AND TRANS.PENSIONNO IS NOT NULL "
					+ " ORDER BY PERSONAL." + sortingOrder;
		} else {
			sqlQuery = "SELECT TRANS.PENSIONNO, PERSONAL.CPFACCNO, PERSONAL.DATEOFBIRTH,PERSONAL.EMPLOYEENO, PERSONAL.EMPLOYEENAME, PERSONAL.DESEGNATION,TRANS.EMOLUMENTS, TRANS.EMPPFSTATUARY,"
					+ "TRANS.EMPVPF, TRANS.EMPADVRECPRINCIPAL, TRANS.EMPADVRECINTEREST,TRANS.TOTAL, TRANS.PENSIONCONTRI, TRANS.PF, TRANS.AAITOTAL, TRANS.FINYEAR, PERSONAL.AIRPORTCODE, PERSONAL.REGION"
					+ " FROM ("
					+ selPersonalQuery
					+ ") PERSONAL,(SELECT TRANS.PENSIONNO, SUM(ROUND(NVL(TRANS.EMOLUMENTS,'0.00'))) AS EMOLUMENTS,SUM(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) AS EMPPFSTATUARY, SUM(ROUND(NVL(TRANS.EMPVPF,'0.00'))) AS EMPVPF, "
					+ "SUM(ROUND(NVL(TRANS.EMPADVRECPRINCIPAL,'0.00'))) AS EMPADVRECPRINCIPAL, SUM(ROUND(NVL(TRANS.EMPADVRECINTEREST,'0.00'))) AS EMPADVRECINTEREST,SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00'))) AS PENSIONCONTRI, SUM(ROUND(NVL(TRANS.PF,'0.00'))) AS PF,"
					+ "SUM(TO_NUMBER(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) + TO_NUMBER(ROUND(NVL(TRANS.EMPVPF,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECPRINCIPAL,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECINTEREST,'0.00')))) AS TOTAL, SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00')) + ROUND(NVL(TRANS.PF,'0.00'))) AS AAITOTAL"
					+ ",FINYEAR,REGION FROM EMPLOYEE_PENSION_VALIDATE TRANS WHERE EMPFLAG='Y' AND PENSIONNO IS NOT NULL AND FINYEAR = '"
					+ frmSelctedYear
					+ "'"
					+ regionQryString
					+ "  GROUP BY PENSIONNO, FINYEAR,AIRPORTCODE,REGION) TRANS WHERE PERSONAL.PENSIONNO = TRANS.PENSIONNO AND TRANS.PENSIONNO IS NOT NULL "
					+ " ORDER BY PERSONAL." + sortingOrder;
		}

		/*
		 * }else{sqlQuery= "SELECT TRANS.PENSIONNO, PERSONAL.CPFACCNO,
		 * PERSONAL.DATEOFBIRTH,PERSONAL.EMPLOYEENO, PERSONAL.EMPLOYEENAME,
		 * PERSONAL.DESEGNATION,TRANS.EMOLUMENTS, TRANS.EMPPFSTATUARY," +
		 * "TRANS.EMPVPF, TRANS.EMPADVRECPRINCIPAL,
		 * TRANS.EMPADVRECINTEREST,TRANS.TOTAL, TRANS.PENSIONCONTRI, TRANS.PF,
		 * TRANS.AAITOTAL, TRANS.FINYEAR, PERSONAL.AIRPORTCODE, PERSONAL.REGION" +"
		 * FROM ("+selPersonalQuery+ ") PERSONAL,(SELECT TRANS.PENSIONNO,
		 * SUM(TRANS.EMOLUMENTS) AS EMOLUMENTS,SUM(TRANS.EMPPFSTATUARY) AS
		 * EMPPFSTATUARY, SUM(TRANS.EMPVPF) AS EMPVPF, " +
		 * "SUM(TRANS.EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,
		 * SUM(TRANS.EMPADVRECINTEREST) AS
		 * EMPADVRECINTEREST,SUM(TRANS.PENSIONCONTRI) AS PENSIONCONTRI,
		 * SUM(TRANS.PF) AS PF," + "SUM(TO_NUMBER(TRANS.EMPPFSTATUARY) +
		 * TO_NUMBER(TRANS.EMPVPF) + TO_NUMBER(EMPADVRECPRINCIPAL) +
		 * TO_NUMBER(EMPADVRECINTEREST)) AS TOTAL, SUM(TRANS.PENSIONCONTRI +
		 * TRANS.PF) AS AAITOTAL" + ",FINYEAR FROM EMPLOYEE_PENSION_VALIDATE
		 * TRANS WHERE PENSIONNO IS NOT NULL AND FINYEAR = '" +frmSelctedYear+ "'
		 * GROUP BY PENSIONNO, FINYEAR) TRANS WHERE PERSONAL.PENSIONNO =
		 * TRANS.PENSIONNO(+) AND TRANS.PENSIONNO IS NOT NULL " + " ORDER BY
		 * PERSONAL."+sortingOrder; }
		 */

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm6Report===sqlQuery"
					+ sqlQuery);
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {

				epfForm3Bean = new AaiEpfform3Bean();

				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));
				} else {
					epfForm3Bean.setPensionno("---");
				}
				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(personalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}
				if (rs.getString("TOTAL") != null) {
					epfForm3Bean.setSubscriptionTotal(rs.getString("TOTAL"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				if (rs.getString("AAITOTAL") != null) {
					epfForm3Bean.setContributionTotal(rs.getString("AAITOTAL"));
				} else {
					epfForm3Bean.setPf("0.00");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				epfForm3Bean.setRemarks("---");
				form6DataList.add(epfForm3Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form6DataList;
	}

	public String buildQueryForm6PersonalInfo(String range, String region,
			String airportcode, String empName, String empNameFlag,
			String frmPensionno) {
		log
				.info("EPFFormsReportDAO::buildQueryForm6PersonalInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT CPFACNO AS CPFACCNO, EMPLOYEENO, EMPLOYEENAME,DESEGNATION,AIRPORTCODE,DATEOFBIRTH, REGION, PENSIONNO FROM EMPLOYEE_PERSONAL_INFO ";
		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);
			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");
		}

		/*
		 * if (!region.equals("")) { whereClause.append(" REGION ='" + region +
		 * "'"); whereClause.append(" AND "); } if (!airportcode.equals("")) {
		 * whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
		 * whereClause.append(" AND "); }
		 */

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !frmPensionno.equals("")) {
				whereClause.append("PENSIONNO='" + frmPensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		query.append(sqlQuery);
		if (/* (region.equals("")) && */(range.equals("NO-SELECT") && (empNameFlag
				.equals("false")) /* && (airportcode.equals("")) */)) {

		} else {
			query.append(" WHERE ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryForm6PersonalInfo Leaving Method");
		return dynamicQuery;
	}

	// By Radha On 16-Sep-2011 for Seperation of Active & InActive Cases
	public ArrayList AAIEPFForm2Report(String range, String region,
			String airportcode, String frmSelectedDts, String empNameFlag,
			String empName, String sortedColumn, String pensionno,
			String adjtype, String statusFlag, String statusType) {

		log.info("AAIEPFReportDAO::AAIEPFForm2Report");

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String fromYear = "", toYear = "", dateOfRetriment = "";
		AAIEPFReportBean AAIEPFBean = null;
		ArrayList AAIEPFReportList = new ArrayList();

		log.info("......frmSelectedDts in DAO......." + frmSelectedDts);

		if (!frmSelectedDts.equals("")) {

			String[] dateArr = frmSelectedDts.split(",");
			fromYear = dateArr[0];
			toYear = dateArr[1];
		}

		String form2Query = this.buildQueryAAIEPFForm2Report(range, region,
				airportcode, fromYear, toYear, empNameFlag, empName,
				sortedColumn, pensionno, adjtype, statusFlag, statusType);

		log.info("-------form2Query------" + form2Query);

		try {

			con = commonDB.getConnection();
			st = con.createStatement();

			rs = st.executeQuery(form2Query);

			while (rs.next()) {
				AAIEPFBean = new AAIEPFReportBean();

				if (rs.getString("CPFACNO") != null) {
					AAIEPFBean.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					AAIEPFBean.setCpfAccno("--");
				}

				if (rs.getString("EMPLOYEENO") != null) {
					AAIEPFBean.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					AAIEPFBean.setEmployeeNumber("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					AAIEPFBean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					AAIEPFBean.setEmployeeName("--");
				}

				if (rs.getString("DESEGNATION") != null) {
					AAIEPFBean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					AAIEPFBean.setDesignation("--");
				}

				if (rs.getString("FHNAME") != null) {
					AAIEPFBean.setFhName(rs.getString("FHNAME"));
				} else {
					AAIEPFBean.setFhName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					AAIEPFBean.setDateOfBirth(CommonUtil.getDatetoString(rs
							.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setDateOfBirth("--");
				}
				/*
				 * if (rs.getString("AIRPORTCODE") != null) {
				 * AAIEPFBean.setAirportCode(rs.getString("AIRPORTCODE")); }
				 * else { AAIEPFBean.setAirportCode("--"); }
				 */

				if (rs.getString("REGION") != null) {
					AAIEPFBean.setRegion(rs.getString("REGION"));
				} else {
					AAIEPFBean.setRegion("--");
				}

				// log.info("-----Pension
				// No----------"+AAIEPFBean.getPensionNo()
				// );

				if (!AAIEPFBean.getDateOfBirth().equals("--")) {
					AAIEPFBean.setPensionNo(commonDAO.getPFID(AAIEPFBean
							.getEmployeeName(), AAIEPFBean.getDateOfBirth(),
							commonUtil.leadingZeros(5, rs
									.getString("PENSIONNO"))));
				} else {
					AAIEPFBean.setPensionNo(rs.getString("PENSIONNO"));
				}

				if (rs.getString("ADJOBYEAR") != null) {
					AAIEPFBean.setObYear(CommonUtil.getDatetoString(rs
							.getDate("ADJOBYEAR"), "dd-MMM-yyyy"));
				} else {
					AAIEPFBean.setObYear("");
				}

				if (rs.getString("SUBSCRIPTIONAMT") != null) {
					AAIEPFBean.setSubscriptionAmt(rs
							.getString("SUBSCRIPTIONAMT"));
				} else {
					AAIEPFBean.setSubscriptionAmt("0.00");
				}

				if (rs.getString("SUBSCRIPTIONINTEREST") != null) {
					AAIEPFBean.setSubscriptionInterest(rs
							.getString("SUBSCRIPTIONINTEREST"));
				} else {
					AAIEPFBean.setSubscriptionInterest("0.00");
				}

				if (rs.getString("CONTRIBUTIONAMT") != null) {
					AAIEPFBean.setContributionamt(rs
							.getString("CONTRIBUTIONAMT"));
				} else {
					AAIEPFBean.setContributionamt("0.00");
				}

				if (rs.getString("PENSIONINTEREST") != null) {
					AAIEPFBean.setPensionInterest(rs
							.getString("PENSIONINTEREST"));
				} else {
					AAIEPFBean.setPensionInterest("0.00");
				}
				if (rs.getString("PENSIONCONTRIADJ") != null) {
					AAIEPFBean.setAdjPensioncontri(rs
							.getString("PENSIONCONTRIADJ"));
				} else {
					AAIEPFBean.setAdjPensioncontri("0.00");
				}
				if (rs.getString("OUTSTANDADV") != null) {
					AAIEPFBean.setOutstandingAmt(rs.getString("OUTSTANDADV"));
				} else {
					AAIEPFBean.setOutstandingAmt("--");
				}
				if (rs.getString("REMARKS") != null) {
					AAIEPFBean.setRemarks(rs.getString("REMARKS"));
				} else {
					AAIEPFBean.setRemarks("--");
				}
				if (rs.getString("REQUESTEDBY") != null) {
					AAIEPFBean.setRequestedBy(rs.getString("REQUESTEDBY"));
				} else {
					AAIEPFBean.setRequestedBy("--");
				}
				AAIEPFReportList.add(AAIEPFBean);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		log.info("====AAIEPFReportList Size in DAO====="
				+ AAIEPFReportList.size());
		return AAIEPFReportList;
	}

	// By Radha On 07-Dec-2011 for Seperating Control Acc reelated Active &In
	// Active Cases specification
	// By Radha On 16-Sep-2011 for Seperation of Active & InActive Cases
	public String buildQueryAAIEPFForm2Report(String range, String region,
			String airportcode, String fromYear, String toYear,
			String empNameFlag, String empName, String sortedColumn,
			String pensionno, String adjtype, String statusFlag,
			String statusType) {
		log
				.info("AAIEPFReportDAO::buildQueryAAIEPFForm2Report-- Entering Method"
						+ adjtype);
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "", finYear = "", finToYear = "";
		int startIndex = 0, endIndex = 0;
		String finFrmYear[] = fromYear.split("-");
		finYear = finFrmYear[2] + "-" + (Integer.parseInt(finFrmYear[2]) + 1);
		if (statusFlag.equals("Y")) {
			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EAO.REGION,EAO.ADJOBYEAR AS ADJOBYEAR,EAO.EMPSUB AS SUBSCRIPTIONAMT,EAO.EMPSUBINTEREST  AS  SUBSCRIPTIONINTEREST,EAO.PENSIONTOTAL AS CONTRIBUTIONAMT,EAO.PENSIONINTEREST AS PENSIONINTEREST,EAO.OUTSTANDADV AS OUTSTANDADV,EAO.REMARKS as REMARKS,EAO.REQUESTEDBY  as REQUESTEDBY,EAO.PENSIONCONTRIADJ as PENSIONCONTRIADJ "
					+ " FROM employee_personal_info EPI, employee_adj_ob EAO, v_emp_personal_info VEMP WHERE EPI.PENSIONNO = EAO.PENSIONNO    AND EAO.PENSIONNO = VEMP.PENSIONNO AND VEMP.FINYEAR='"
					+ finYear
					+ "' AND  EAO.ADJOBYEAR BETWEEN '"
					+ fromYear
					+ "' AND last_day('" + toYear + "') ";
		} else {
			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EAO.REGION,EAO.ADJOBYEAR AS ADJOBYEAR,EAO.EMPSUB AS SUBSCRIPTIONAMT,EAO.EMPSUBINTEREST  AS  SUBSCRIPTIONINTEREST,EAO.PENSIONTOTAL AS CONTRIBUTIONAMT,EAO.PENSIONINTEREST AS PENSIONINTEREST,EAO.OUTSTANDADV AS OUTSTANDADV,EAO.REMARKS as REMARKS,EAO.REQUESTEDBY  as REQUESTEDBY,EAO.PENSIONCONTRIADJ as PENSIONCONTRIADJ "
					+ " FROM employee_personal_info EPI, employee_adj_ob EAO  WHERE EPI.PENSIONNO = EAO.PENSIONNO  AND  EAO.ADJOBYEAR BETWEEN '"
					+ fromYear + "' AND last_day('" + toYear + "') ";
		}

		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  EPI.PENSIONNO >=" + startIndex
					+ " AND EPI.PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}
		if (!adjtype.equals("")) {
			if (adjtype.equals("M")) {
				whereClause.append(" EAO.MANUALS ='Y'");
				whereClause.append(" AND ");
			} else if (adjtype.equals("S")) {
				whereClause.append(" EAO.MANUALS ='N'");
				whereClause.append(" AND ");
			}
		}
		log.info("---statusFlag-------" + statusFlag + "------statusType-----"
				+ statusType);
		if (statusFlag.trim().equals("Y")) {
			log.info("---statusFlag-------" + statusFlag + "------statusType-----"
					+ statusType);
			whereClause.append("VEMP.STATUS ='" + statusType + "'");
			whereClause.append(" AND ");
		}

		if (!region.equals("NO-SELECT")) {
			whereClause.append(" EAO.REGION ='" + region + "'");
			whereClause.append(" AND ");
		}

		if (!airportcode.equals("NO-SELECT")) {
			whereClause.append(" EPI.AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if ((statusFlag.equals("N")) &&(region.equals("NO-SELECT")) && (adjtype.equals(""))
				&& (airportcode.equals("NO-SELECT"))
				&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}
		orderBy = " ORDER BY  EPI." + sortedColumn + " ASC";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("AAIEPFReportDAO::buildQueryAAIEPFForm2Report Leaving Method");
		return dynamicQuery;
	}

	public ArrayList getEPFForm7Report(String region, String airprotcode,
			String frmSelctedYear, String sortingOrder) {
		String sqlQuery = "", regionDesc = "", stationDesc = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form6DataList = new ArrayList();
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}

		sqlQuery = this.buildQueryForm7TransInfo(regionDesc, stationDesc,
				sortingOrder, frmSelctedYear);

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm7Report===sqlQuery"
					+ sqlQuery);
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {

				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("TOTALSUBSCRIBERS") != null) {
					epfForm3Bean.setTotalSubscribers(rs
							.getString("TOTALSUBSCRIBERS"));
				} else {
					epfForm3Bean.setTotalSubscribers("0");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}
				if (rs.getString("TOTAL") != null) {
					epfForm3Bean.setSubscriptionTotal(rs.getString("TOTAL"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				if (rs.getString("AAITOTAL") != null) {
					epfForm3Bean.setContributionTotal(rs.getString("AAITOTAL"));
				} else {
					epfForm3Bean.setPf("0.00");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				epfForm3Bean.setRemarks("---");

				form6DataList.add(epfForm3Bean);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form6DataList;
	}

	public String buildQueryForm7TransInfo(String region, String airportcode,
			String sortedColumn, String frmSelectedYear) {
		log
				.info("EPFFormsReportDAO::buildQueryForm7TransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT REGION,AIRPORTCODE,COUNT(*) AS TOTALSUBSCRIBERS, SUM(ROUND(NVL(TRANS.EMOLUMENTS,'0.00'))) AS EMOLUMENTS, SUM(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) AS EMPPFSTATUARY,"
				+ "SUM(ROUND(NVL(TRANS.EMPVPF,'0.00'))) AS EMPVPF, SUM(ROUND(NVL(TRANS.EMPADVRECPRINCIPAL,'0.00'))) AS EMPADVRECPRINCIPAL,SUM(ROUND(NVL(TRANS.EMPADVRECINTEREST,'0.00'))) AS EMPADVRECINTEREST, SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00'))) AS PENSIONCONTRI,"
				+ "SUM(ROUND(NVL(TRANS.PF,'0.00'))) AS PF, SUM(TO_NUMBER(ROUND(NVL(TRANS.EMPPFSTATUARY,'0.00'))) + TO_NUMBER(ROUND(NVL(TRANS.EMPVPF,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECPRINCIPAL,'0.00'))) + TO_NUMBER(ROUND(NVL(EMPADVRECINTEREST,'0.00')))) AS TOTAL, "
				+ "SUM(ROUND(NVL(TRANS.PENSIONCONTRI,'0.00')) + ROUND(NVL(TRANS.PF,'0.00'))) AS AAITOTAL, FINYEAR FROM EMPLOYEE_PENSION_VALIDATE TRANS WHERE EMPFLAG='Y' AND PENSIONNO IS NOT NULL AND FINYEAR = '"
				+ frmSelectedYear + "'";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String groupBy = "GROUP BY AIRPORTCODE,REGION, FINYEAR";
		String orderBy = "  ORDER BY TRANS." + sortedColumn;
		query.append(groupBy + orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryForm7TransInfo Leaving Method");
		return dynamicQuery;
	}

	public ArrayList getFinalSettlement(Connection con, String pensionno,
			String finYear, AaiEpfForm11Bean aaiEPFForm11Bean) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		String finalEmpSub = "", finalAAICont = "";
		boolean availFlag = false;
		Long finalStlmentEmpNet = null, finalStlmentAAICon = null;
		double closingEmpSub = 0.0, closingAAINet = 0.0;
		ArrayList finalSettlementList = new ArrayList();
		sqlQuery = "SELECT PENSIONNO, SUM(FINEMP) AS FINEMP,SUM((FINAAI - NVL(PENCON, '0.00'))) AS FINAAI FROM EMPLOYEE_PENSION_FINSETTLEMENT  WHERE PENSIONNO = "
				+ pensionno
				+ "  AND FINYEAR='"
				+ finYear
				+ "' AND SETTLEMENTDATE IS NOT NULL    GROUP BY PENSIONNO";
		log.info("getFinalSettlement" + sqlQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				if (rs.getString("FINEMP") != null) {
					finalEmpSub = rs.getString("FINEMP");
					aaiEPFForm11Bean.setFinalEmpSubscr(finalEmpSub);
				} else {
					finalEmpSub = "0";
					aaiEPFForm11Bean.setFinalEmpSubscr(finalEmpSub);
				}
				if (rs.getString("FINAAI") != null) {
					finalAAICont = rs.getString("FINAAI");
					aaiEPFForm11Bean.setFinalAAIContr(finalAAICont);
				} else {
					finalAAICont = "0";
					aaiEPFForm11Bean.setFinalAAIContr(finalAAICont);
				}
				availFlag = true;
			} else {
				aaiEPFForm11Bean.setFinalEmpSubscr("0");
				aaiEPFForm11Bean.setFinalAAIContr("0");
			}
			if (availFlag == true) {
				finalStlmentEmpNet = new Long(Long
						.parseLong((String) finalEmpSub));
				finalStlmentAAICon = new Long(Long
						.parseLong((String) finalAAICont));
				closingEmpSub = Double.parseDouble(aaiEPFForm11Bean
						.getClosingBal());
				closingAAINet = Double.parseDouble(aaiEPFForm11Bean
						.getAaiClosingBal());

				long netcloseEmpNet = (new Double(closingEmpSub).longValue())
						+ (-finalStlmentEmpNet.longValue());
				long netcloseNetAmount = (new Double(closingAAINet).longValue())
						+ (-finalStlmentAAICon.longValue());
				aaiEPFForm11Bean.setAaiClosingBal(Long
						.toString(netcloseNetAmount));
				aaiEPFForm11Bean.setClosingBal(Long.toString(netcloseEmpNet));
				log.info("Before Updated Values closingEmpSub" + closingEmpSub
						+ "closingAAINet" + closingAAINet);
				log.info("After Updated Values netcloseEmpNet" + netcloseEmpNet
						+ "netcloseNetAmount" + netcloseNetAmount);
			}

			log.info("EMPNET Closing Bal" + aaiEPFForm11Bean.getClosingBal()
					+ aaiEPFForm11Bean.getAaiClosingBal());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return finalSettlementList;
	}

	public boolean compareDates(String fromDate, String todate,
			String finalsettlementDate) {
		Date fromYear = new Date();
		Date toYear = new Date();
		Date fnlDate = new Date();
		boolean finalDateFlag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		System.out.println("fromDate" + fromDate + "todate" + todate
				+ "finalsettlementDate" + finalsettlementDate);
		try {
			fromYear = dateFormat.parse(fromDate);
			toYear = dateFormat.parse(todate);
			fnlDate = dateFormat.parse(finalsettlementDate);
			if (fnlDate.after(fromYear) && fnlDate.before(toYear)) {
				finalDateFlag = true;
				log.info(fromDate + "In between" + finalsettlementDate
						+ " years" + todate);
			} else {
				finalDateFlag = false;
				log.info(fromDate + "In out " + finalsettlementDate + " years"
						+ todate);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return finalDateFlag;
	}

	public ArrayList getEPFForm3MergerData(ArrayList list, Connection con,
			String selPerQuery, String fromDate, String toDate, String region,
			String airportcode, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selQuery = "", selTransQuery = "";
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		selTransQuery = this.buildQueryEmpPFTransMergerInfo(region,
				airportcode, sortingOrder, fromDate, toDate);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE , EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
		try {

			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEPFForm3MergerData===selQuery"
					+ selQuery);
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();
				epfForm3Bean = this.fillEPFForm3Properties(rs, epfForm3Bean);
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("bgcolor='lightblue'");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				list.add(epfForm3Bean);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
	}

	public String buildQueryEmpPFTransMergerInfo(String region,
			String airportcode, String sortedColumn, String fromDate,
			String toDate) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransMergerInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT MERGER.MONTHYEAR AS MONTHYEAR, round(MERGER.EMOLUMENTS) as EMOLUMENTS, round(MERGER.EMPPFSTATUARY) AS EMPPFSTATUARY, round(MERGER.EMPVPF) AS EMPVPF, MERGER.CPF AS CPF, round(MERGER.EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(MERGER.EMPADVRECINTEREST) AS EMPADVRECINTEREST, round(MERGER.AAICONPF) AS AAICONPF, ROUND(MERGER.CPFADVANCE) AS CPFADVANCE, ROUND(MERGER.DEDADV) AS DEDADV, ROUND(MERGER.REFADV) AS REFADV, ROUND(MERGER.PENSIONCONTRI) AS PENSIONCONTRI, ROUND(MERGER.PF) AS PF, MERGER.AIRPORTCODE AS AIRPORTCODE, "
				+ "MERGER.REGION AS REGION, MERGER.EMPFLAG AS EMPFLAG, MERGER.CPFACCNO AS CPFACCNO,MERGER.PENSIONNO AS PENSIONNO,MERGER.EMPLOYEENO AS EMPLOYEENO FROM EMP_PENSION_VALIDATE_MERGER MERGER,EMPLOYEE_PENSION_VALIDATE VAL WHERE VAL.MERGEFLAG='Y' AND VAL.PENSIONNO=MERGER.PENSIONNO AND VAL.MONTHYEAR=MERGER.MONTHYEAR  AND MERGER.EMPFLAG = 'Y' AND MERGER.PENSIONNO IS NOT NULL  and MERGER.MONTHYEAR between '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" MERGER.REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" MERGER.AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY MERGER." + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransMergerInfo Leaving Method");
		return dynamicQuery;
	}

	private AaiEpfform3Bean fillEPFForm3Properties(ResultSet rs,
			AaiEpfform3Bean epfForm3Bean) throws SQLException {

		if (rs.getString("PENSIONNO") != null) {
			epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

		}

		if (rs.getString("CPFACCNO") != null) {
			epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
		} else {
			epfForm3Bean.setCpfaccno("---");
		}

		if (rs.getString("EMPLOYEENO") != null) {
			epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
		} else {
			epfForm3Bean.setEmployeeNo("---");
		}
		if (rs.getString("EMPLOYEENAME") != null) {
			epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
		} else {
			epfForm3Bean.setEmployeeName("---");
		}
		if (rs.getString("DESEGNATION") != null) {
			epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
		} else {
			epfForm3Bean.setDesignation("---");
		}
		if (rs.getString("FHNAME") != null) {
			epfForm3Bean.setFhName(rs.getString("FHNAME"));
		} else {
			epfForm3Bean.setFhName("---");
		}
		if (rs.getString("DATEOFBIRTH") != null) {
			epfForm3Bean.setDateOfBirth(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFBIRTH")));
		} else {
			epfForm3Bean.setDateOfBirth("---");
		}
		if (!epfForm3Bean.getDateOfBirth().equals("---")) {
			String personalPFID = commonDAO.getPFID(epfForm3Bean
					.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
					commonUtil.leadingZeros(5, epfForm3Bean.getPensionno()));
			epfForm3Bean.setPfID(personalPFID);
		} else {
			epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
		}
		if (rs.getString("DATEOFJOINING") != null) {
			epfForm3Bean.setDateofJoining(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			epfForm3Bean.setDateofJoining("---");
		}
		if (rs.getString("WETHEROPTION") != null) {
			epfForm3Bean.setWetherOption(rs.getString("WETHEROPTION"));
		} else {
			epfForm3Bean.setWetherOption("---");
		}
		if (rs.getString("MONTHYEAR") != null) {
			epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
					.getDate("MONTHYEAR")));
		} else {
			epfForm3Bean.setMonthyear("---");
		}
		if (rs.getString("EMOLUMENTS") != null) {
			epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
		} else {
			epfForm3Bean.setEmoluments("0.00");
		}
		if (rs.getString("EMPPFSTATUARY") != null) {

			epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
		} else {
			epfForm3Bean.setEmppfstatury("0.00");
		}

		if (rs.getString("EMPVPF") != null) {
			epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
		} else {
			epfForm3Bean.setEmpvpf("0.00");
		}
		if (rs.getString("EMPADVRECPRINCIPAL") != null) {
			epfForm3Bean.setPrincipal(rs.getString("EMPADVRECPRINCIPAL"));
		} else {
			epfForm3Bean.setPrincipal("0.00");
		}
		if (rs.getString("EMPADVRECINTEREST") != null) {
			epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
		} else {
			epfForm3Bean.setInterest("0.00");
		}

		if (rs.getString("PENSIONCONTRI") != null) {
			epfForm3Bean.setPensionContribution(rs.getString("PENSIONCONTRI"));
		} else {
			epfForm3Bean.setPensionContribution("0.00");
		}
		if (rs.getString("PF") != null) {
			epfForm3Bean.setPf(rs.getString("PF"));
		} else {
			epfForm3Bean.setPf("0.00");
		}

		if (rs.getString("AIRPORTCODE") != null) {
			epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
		} else {
			epfForm3Bean.setStation("---");
		}
		if (rs.getString("REGION") != null) {
			epfForm3Bean.setRegion(rs.getString("REGION"));
		} else {
			epfForm3Bean.setRegion("---");
		}
		if(rs.getString("ADDITIONALCONTRI")!=null) {
			epfForm3Bean.setAdditionalContri(rs.getString("ADDITIONALCONTRI"));			
		}
		else {
			epfForm3Bean.setAdditionalContri("0.00");
		}
		return epfForm3Bean;
	}

	// By Radha on 11-Aug-2011 for seperating Form 3 to Form 4 using orgother
	// flag
	public String buildQueryEmpPFTransForm3Info(String region,
			String airportcode, String sortedColumn, String fromDate,
			String toDate) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, CPF, round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, round(AAICONPF) AS AAICONPF, ROUND(CPFADVANCE) AS CPFADVANCE, ROUND(DEDADV) AS DEDADV, ROUND(REFADV) AS REFADV, ROUND(PENSIONCONTRI) AS PENSIONCONTRI, ROUND(PF) AS PF, AIRPORTCODE, "
				+ "REGION, EMPFLAG, CPFACCNO,PENSIONNO,EMPLOYEENO,suppliflag,MERGEFLAG,round(ADDITIONALCONTRI) as ADDITIONALCONTRI FROM EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG = 'Y' AND MERGEFLAG='N' AND OTHERORG = 'N' AND PENSIONNO IS NOT NULL  and MONTHYEAR between '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo Leaving Method");
		return dynamicQuery;
	}

	public String buildQueryEmpPFTransForm4Info(String region,
			String airportcode, String sortedColumn, String fromDate,
			String toDate,String select_month) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		/*
		 * sqlQuery = "SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS,
		 * round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, CPF,
		 * round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL," +
		 * "round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, round(AAICONPF) AS
		 * AAICONPF, ROUND(CPFADVANCE) AS CPFADVANCE, ROUND(DEDADV) AS DEDADV,
		 * ROUND(REFADV) AS REFADV, ROUND(PENSIONCONTRI) AS PENSIONCONTRI,
		 * ROUND(PF) AS PF, AIRPORTCODE, " + "REGION, EMPFLAG,
		 * CPFACCNO,PENSIONNO,EMPLOYEENO,suppliflag,MERGEFLAG FROM
		 * EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG = 'Y' AND MERGEFLAG='N' AND
		 * OTHERORG = 'Y' AND PENSIONNO IS NOT NULL and MONTHYEAR between '" +
		 * fromDate + "' and LAST_DAY('" + toDate + "') ";
		 */

		sqlQuery = "SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, ROUND(PENSIONCONTRI) AS PENSIONCONTRI, ROUND(PF) AS PF, AIRPORTCODE, "
				+ "REGION, EMPFLAG, CPFACCNO,PENSIONNO,EMPLOYEENO,suppliflag FROM employee_supplimentory_data where EMPFLAG = 'Y' AND SUPPLIFLAG='F' AND PENSIONNO IS NOT NULL   ";
		if (!select_month.equals("")) {
			if(Integer.parseInt(select_month)<4){
				select_month="01-"+select_month+"-"+(Integer.parseInt(fromDate.substring(7))+1) ;
				
			}else{
				select_month="01-"+select_month+"-"+fromDate.substring(7);
			}
			log.info("monrhyaer"+select_month);
			
			whereClause.append(" to_char(monthyear,'dd-mm-yyyy')='" + select_month + "'");
			whereClause.append(" AND ");
		}else{
			whereClause.append("MONTHYEAR between '"
			+ fromDate + "' and LAST_DAY('" + toDate + "')");
			whereClause.append(" AND ");
		}
		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {
			query.append(" AND ");
			if(!select_month.equals("")){
				query.append(commonDAO.sTokenFormat(whereClause));
			}else if(select_month.equals("")) {
				query.append(commonDAO.sTokenFormat(whereClause));
			}
		} else{
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY " + sortedColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo Leaving Method");
		return dynamicQuery;
	}

	public ArrayList loadRemitanceInfo(String salaryMonth, String region,
			String airportcode, String remitanceType, String accountType) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList remitanceList = new ArrayList();
		String table = "", condition = "", query = "";
		if (remitanceType.trim().equals("aaiepf3Suppl")) {
			table = "employee_supplimentory_data";
			condition = "AND paiddate BETWEEN '" + salaryMonth
					+ "' and last_day('" + salaryMonth
					+ "') and SUPPLIFLAG='S'";
			query = "select sum(round(nvl(emoluments,0))) as emoluments,sum(round(nvl(emppfstatuary,0))) as emppfstatuary,sum(round(nvl(EMPvpf,0))) as EMPvpf,sum(round(nvl(Empadvrecprincipal,0))) as Empadvrecprincipal ,sum(round(nvl(Empadvrecinterest,0))) as Empadvrecinterest,sum(nvl(pf,0)) as pf,sum(nvl(pensioncontri,0))as pensioncontri,sum(round(nvl(CALCEMOLUMENTS,0))) as CALCEMOLUMENTS FROM "
					+ table
					+ " where  empflag='Y' and region='"
					+ region
					+ "' and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' " + condition + " ";
		} else if (remitanceType.trim().equals("aiepf3Arr")) {
			table = "employee_supplimentory_data";
			condition = "AND MONTHYEAR BETWEEN '" + salaryMonth
					+ "' and last_day('" + salaryMonth
					+ "') and SUPPLIFLAG='A'";
			query = "select sum(round(nvl(emoluments,0))) as emoluments,sum(round(nvl(emppfstatuary,0))) as emppfstatuary,sum(round(nvl(EMPvpf,0))) as EMPvpf,sum(round(nvl(Empadvrecprincipal,0))) as Empadvrecprincipal ,sum(round(nvl(Empadvrecinterest,0))) as Empadvrecinterest,sum(nvl(pf,0)) as pf,sum(nvl(pensioncontri,0))as pensioncontri,sum(round(nvl(CALCEMOLUMENTS,0))) as CALCEMOLUMENTS FROM "
					+ table
					+ " where  empflag='Y' and region='"
					+ region
					+ "' and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' " + condition + " ";
		} else {
			table = "employee_pension_validate";
			condition = "AND MONTHYEAR BETWEEN '" + salaryMonth
					+ "' and last_day('" + salaryMonth
					+ "')  and SUPPLIFLAG='N'";
			query = "select (tab1.emoluments +nvl(tab2.emoluments,0)) as emoluments,(tab1.emppfstatuary + nvl(tab2.emppfstatuary,0)) as emppfstatuary,(tab1.EMPvpf + nvl(tab2.EMPvpf,0)) as EMPvpf,(tab1.Empadvrecprincipal + nvl(tab2.Empadvrecprincipal,0)) as Empadvrecprincipal,(tab1.Empadvrecinterest + nvl(tab2.Empadvrecinterest,0)) as Empadvrecinterest,(tab1.pf + nvl(tab2.pf,0)) as pf,(tab1.pensioncontri + nvl(tab2.pensioncontri,0)) as pensioncontri,(tab1.CALCEMOLUMENTS + nvl(tab2.CALCEMOLUMENTS,0)) as CALCEMOLUMENTS	  from "
					+ "(select sum(round(nvl(emoluments, 0))) as emoluments, sum(round(nvl(emppfstatuary, 0))) as emppfstatuary, sum(round(nvl(EMPvpf, 0))) as EMPvpf, sum(round(nvl(Empadvrecprincipal, 0))) as Empadvrecprincipal,sum(round(nvl(Empadvrecinterest, 0))) as Empadvrecinterest, sum(nvl(pf, 0)) as pf, sum(nvl(pensioncontri, 0)) as pensioncontri,sum(round(nvl(CALCEMOLUMENTS, 0))) as CALCEMOLUMENTS  FROM employee_pension_validate    where empflag = 'Y'  and region = '"
					+ region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND MONTHYEAR BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') and SUPPLIFLAG = 'N') tab1,"
					+ "(select sum(round(nvl(emoluments, 0))) as emoluments, sum(round(nvl(emppfstatuary, 0))) as emppfstatuary, sum(round(nvl(EMPvpf, 0))) as EMPvpf, sum(round(nvl(Empadvrecprincipal, 0))) as Empadvrecprincipal, sum(round(nvl(Empadvrecinterest, 0))) as Empadvrecinterest,sum(nvl(pf, 0)) as pf, sum(nvl(pensioncontri, 0)) as pensioncontri, sum(round(nvl(CALCEMOLUMENTS, 0))) as CALCEMOLUMENTS   FROM employee_supplimentory_data    where empflag = 'Y'    and region = '"
					+ region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND MONTHYEAR BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "')  and SUPPLIFLAG = 'N') tab2";
		}
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			// log.info("EPFFormsReportDAO::loadRemitanceInfo===Query " +
			// query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				AaiEpfform3Bean remitancebean = new AaiEpfform3Bean();
				if (rs.getString("emoluments") != null) {
					remitancebean.setEmoluments(rs.getString("emoluments"));
				} else {
					remitancebean.setEmoluments("0.00");
				}
				if (rs.getString("CALCEMOLUMENTS") != null) {
					remitancebean.setPensionContriEmoluments(rs
							.getString("CALCEMOLUMENTS"));
				} else {
					remitancebean.setPensionContriEmoluments("0.00");
				}
				if (rs.getString("emppfstatuary") != null) {
					remitancebean
							.setEmppfstatury(rs.getString("emppfstatuary"));
				} else {
					remitancebean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPvpf") != null) {
					remitancebean.setEmpvpf(rs.getString("EMPvpf"));
				} else {
					remitancebean.setEmpvpf("0.00");
				}
				if (rs.getString("Empadvrecprincipal") != null) {
					remitancebean.setPrincipal(rs
							.getString("Empadvrecprincipal"));
				} else {
					remitancebean.setPrincipal("0.00");
				}
				if (rs.getString("Empadvrecinterest") != null) {
					remitancebean
							.setInterest(rs.getString("Empadvrecinterest"));
				} else {
					remitancebean.setInterest("0.00");
				}
				if (rs.getString("pf") != null) {
					remitancebean.setPf(rs.getString("pf"));
				} else {
					remitancebean.setPf("0.00");
				}
				if (rs.getString("pensioncontri") != null) {
					remitancebean.setPensionContribution(rs
							.getString("pensioncontri"));
				} else {
					remitancebean.setPensionContribution("0.00");
				}
				String pfaccretion = String.valueOf(Math.round(Double
						.parseDouble(remitancebean.getEmppfstatury())
						+ Double.parseDouble(remitancebean.getEmpvpf())
						+ Double.parseDouble(remitancebean.getPrincipal())
						+ Double.parseDouble(remitancebean.getInterest())
						+ Double.parseDouble(remitancebean.getPf())));
				System.out.println("pfaccretion " + pfaccretion);
				remitancebean.setEpf3Accretion(pfaccretion);
				remitancebean.setStation(airportcode);
				remitancebean.setRegion(region);
				remitanceList.add(remitancebean);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return remitanceList;
	}

	public ArrayList loadRemitanceTableInfo(String salaryMonth, String region,
			String airportcode, String remitanceType, String inputfrom,
			String accountType) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList remitanceTableList = new ArrayList();
		ArrayList remitanceList = new ArrayList();

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String condition = "";
			String unittableCondition = "";
			if (!region.equals("") && !airportcode.equals("")) {
				condition = " region='" + region + "' and lower(airportcode)='"
						+ airportcode.toLowerCase() + "' AND ";
				unittableCondition = " region='" + region
						+ "' and lower(unitname)='" + airportcode.toLowerCase()
						+ "'";
				if (!accountType.trim().equals("")) {
					unittableCondition = " region='" + region
							+ "' and lower(unitname)='"
							+ airportcode.toLowerCase() + "' AND accounttype='"
							+ accountType + "'";
				}
			} else if (!region.equals("") && airportcode.equals("")) {
				condition = " region='" + region + "' AND ";
				unittableCondition = " region='" + region + "'";
				if (!accountType.trim().equals("")) {
					unittableCondition = unittableCondition
							+ " and accounttype='" + accountType + "'";
				}
			}
			if (remitanceType.trim().equals("aaiepf3Suppl")) {
				condition = condition + " REMITFLAG='S' AND";
			} else if (remitanceType.trim().equals("aiepf3Arr")) {
				condition = condition + " REMITFLAG='R' AND";
			} else {
				condition = condition + " REMITFLAG='A' AND";
			}
			String airportcode1 = "";
			String checkQuery = "select airportcode from EMPLOYEE_REMITTANCEINFO where  region='"
					+ region
					+ "' and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' AND SALARYDT BETWEEN '"
					+ salaryMonth + "' and last_day('" + salaryMonth + "')";
			log.info("checkQuery" + checkQuery);
			st = con.createStatement();
			rs = st.executeQuery(checkQuery);

			if (rs.next()) {
				if (rs.getString("airportcode") != null) {
					airportcode1 = rs.getString("airportcode");
				} else {
					airportcode1 = "";
				}
			}
			String query = "";
			if (inputfrom.equals("screen")) {
				if (accountType.equals("RAU") && airportcode1.equals("")) {
					condition = " region='" + region
							+ "' AND AIRPORTCODE='NO-SELECT' AND ";
				}
				query = "select VRNO,to_char(VRDT,'dd-Mon-yyyy') as VRDT,PFACCRETION,PFREMITTERBANKNM,PFREMITTERBANKACNO,PFNOOFEMPLOYEES,EMOLUMENTS,INSPECTIONCHARGES,PCNOOFEMPLOYEES,PCEMOLUMENTS,PENSIONCONTRIBUTION,PCREMITTERBANKNM,PCREMITTERBANKACNO,to_char(SALARYDT,'dd-Mon-yyyy') as SALARYDT ,AIRPORTCODE,REGION,PCEMOLUMENTS,EPF3ACCRETION,EPF3PENSIONCONTRI,epf3Emoluments,REMITFLAG,BILLREFNO,to_char(BILLDATE,'dd-Mon-yyyy') as BILLDATE,CHEQUENOFROM,CHEQUENOTO,to_char(CHEQUEDT,'dd-Mon-yyyy') as CHEQUEDT,PREPAREDBY,CHECKEDBY,PASSEDBY,RECEIVEDBY FROM EMPLOYEE_REMITTANCEINFO where  "
						+ condition
						+ " SALARYDT BETWEEN '"
						+ salaryMonth
						+ "' and last_day('"
						+ salaryMonth
						+ "') order by region";
			} else {
				query = " select * from (select VRNO,to_char(VRDT, 'dd-Mon-yyyy') as VRDT, PFACCRETION,PFREMITTERBANKNM,PFREMITTERBANKACNO, PFNOOFEMPLOYEES,EMOLUMENTS,INSPECTIONCHARGES,PCNOOFEMPLOYEES,PENSIONCONTRIBUTION, PCREMITTERBANKNM, PCREMITTERBANKACNO,to_char(SALARYDT, 'dd-Mon-yyyy') as SALARYDT, AIRPORTCODE,PCEMOLUMENTS,EPF3ACCRETION,EPF3PENSIONCONTRI,epf3Emoluments, REMITFLAG,BILLREFNO,to_char(BILLDATE,'dd-Mon-yyyy') as BILLDATE,CHEQUENOFROM,CHEQUENOTO,to_char(CHEQUEDT,'dd-Mon-yyyy') as CHEQUEDT,PREPAREDBY,CHECKEDBY,PASSEDBY,RECEIVEDBY FROM EMPLOYEE_REMITTANCEINFO where "
						+ condition
						+ " SALARYDT BETWEEN '"
						+ salaryMonth
						+ "' and last_day('"
						+ salaryMonth
						+ "') order by region) tab1,(select  unitname,region,ACCOUNTTYPE,VACCOUNTNO,NEWUNITCODE from employee_unit_master where"
						+ unittableCondition
						+ " group by region,ACCOUNTTYPE,unitname,VACCOUNTNO,NEWUNITCODE) tab2 where tab2.unitname=tab1.airportcode(+) order by tab2.region,tab2.ACCOUNTTYPE,tab2.unitname ";
			}
			log.info("loadRemitanceTableInfo===Query " + query);
			rs = st.executeQuery(query);
			int i = 0;
			while (rs.next()) {
				RemittanceBean remitancebean = new RemittanceBean();
				if (rs.getString("VRNO") != null) {
					remitancebean.setVrNo(rs.getString("VRNO"));
				}
				if (rs.getString("VRDT") != null) {
					remitancebean.setVrDt(rs.getString("VRDT"));
				}
				if (rs.getString("PFACCRETION") != null) {
					remitancebean.setPfAccretion(Double.parseDouble(rs
							.getString("PFACCRETION")));
				} else {
					remitancebean.setPfAccretion(0);
				}
				if (rs.getString("PFREMITTERBANKNM") != null) {
					remitancebean.setPfRemitterBankNM(rs
							.getString("PFREMITTERBANKNM"));
				} else {
					remitancebean.setPfRemitterBankNM("");
				}
				if (rs.getString("PFREMITTERBANKACNO") != null) {
					remitancebean.setPfRemitterBankACNo(rs
							.getString("PFREMITTERBANKACNO"));
				} else {
					remitancebean.setPfRemitterBankACNo("");
				}
				if (rs.getString("PFNOOFEMPLOYEES") != null) {
					remitancebean.setPfnoofEmployees(Integer.parseInt(rs
							.getString("PFNOOFEMPLOYEES")));
				} else {
					remitancebean.setPfnoofEmployees(0);
				}
				if (rs.getString("EMOLUMENTS") != null) {
					remitancebean.setEmoluments(Double.parseDouble(rs
							.getString("EMOLUMENTS")));
				} else {
					remitancebean.setEmoluments(0);
				}

				if (rs.getString("INSPECTIONCHARGES") != null) {
					remitancebean.setInspectionCharges(Double.parseDouble(rs
							.getString("INSPECTIONCHARGES")));
				} else {
					remitancebean.setInspectionCharges(0);
				}
				if (rs.getString("PCNOOFEMPLOYEES") != null) {
					remitancebean.setPcNoofEmployees(Integer.parseInt(rs
							.getString("PCNOOFEMPLOYEES")));
				} else {
					remitancebean.setPcNoofEmployees(0);
				}
				if (rs.getString("PCEMOLUMENTS") != null) {
					remitancebean.setPcEmoluments(Double.parseDouble(rs
							.getString("PCEMOLUMENTS")));
				} else {
					remitancebean.setPcEmoluments(0);
				}
				if (rs.getString("PENSIONCONTRIBUTION") != null) {
					remitancebean.setPensionContribution(Double.parseDouble(rs
							.getString("PENSIONCONTRIBUTION")));
				} else {
					remitancebean.setPensionContribution(0);
				}
				if (rs.getString("PCREMITTERBANKNM") != null) {
					remitancebean.setPcRemitterBankNM(rs
							.getString("PCREMITTERBANKNM"));
				} else {
					remitancebean.setPcRemitterBankNM("");
				}
				if (rs.getString("PCREMITTERBANKACNO") != null) {
					remitancebean.setPcRemitterBankAcNo(rs
							.getString("PCREMITTERBANKACNO"));
				} else {
					remitancebean.setPcRemitterBankAcNo("");
				}
				if (rs.getString("SALARYDT") != null) {
					remitancebean.setSalMonth(rs.getString("SALARYDT"));
				} else {
					remitancebean.setSalMonth("");
				}
				if (rs.getString("BILLREFNO") != null) {
					remitancebean.setBillRefno(rs.getString("BILLREFNO"));
					log.info("BILLREFNOif" + rs.getString("BILLREFNO"));
					log.info("BILLREFNOif" + remitancebean.getBillRefno());
				} else {
					log.info("BILLREFNOelse" + rs.getString("BILLREFNO"));
					remitancebean.setBillRefno("");
				}
				log.info("BILLREFNO" + remitancebean.getBillRefno());
				if (rs.getString("BILLDATE") != null) {
					remitancebean.setBilldate(rs.getString("BILLDATE"));
				} else {
					remitancebean.setBilldate("");
				}
				if (rs.getString("CHEQUENOFROM") != null) {
					remitancebean.setChequenofrom(rs.getString("CHEQUENOFROM"));
				} else {
					remitancebean.setChequenofrom("");
				}
				if (rs.getString("CHEQUENOTO") != null) {
					remitancebean.setChequenoto(rs.getString("CHEQUENOTO"));
				} else {
					remitancebean.setChequenoto("");
				}
				if (rs.getString("CHEQUEDT") != null) {
					remitancebean.setChequedt(rs.getString("CHEQUEDT"));
				} else {
					remitancebean.setChequedt("");
				}
				if (rs.getString("PREPAREDBY") != null) {
					remitancebean.setPreparedby(rs.getString("PREPAREDBY"));
				} else {
					remitancebean.setPreparedby("");
				}
				if (rs.getString("CHECKEDBY") != null) {
					remitancebean.setCheckedby(rs.getString("CHECKEDBY"));
				} else {
					remitancebean.setCheckedby("");
				}
				if (rs.getString("PASSEDBY") != null) {
					remitancebean.setPassedby(rs.getString("PASSEDBY"));
				} else {
					remitancebean.setPassedby("");
				}
				log.info("PASSEDBY" + remitancebean.getPassedby());
				if (rs.getString("RECEIVEDBY") != null) {
					remitancebean.setReceivedby(rs.getString("RECEIVEDBY"));
				} else {
					remitancebean.setReceivedby("");
				}

				if (!inputfrom.equals("screen")) {
					if (rs.getString("unitname") != null) {
						remitancebean.setAirportcode(rs.getString("unitname"));
					} else {
						remitancebean.setAirportcode("");
					}
					if (rs.getString("REGION") != null) {
						remitancebean.setRegion(rs.getString("REGION"));
					} else {
						remitancebean.setRegion("");
					}
					if (rs.getString("ACCOUNTTYPE") != null) {
						remitancebean.setAccountType(rs
								.getString("ACCOUNTTYPE"));
					} else {
						remitancebean.setAccountType("");
					}
					if (rs.getString("VACCOUNTNO") != null) {
						remitancebean.setVAccountNo(rs.getString("VACCOUNTNO"));
					} else {
						remitancebean.setVAccountNo("");
					}
					if (rs.getString("NEWUNITCODE") != null) {
						remitancebean.setNewUnitCode(rs
								.getString("NEWUNITCODE"));
					} else {
						remitancebean.setNewUnitCode("--");
					}
				}
				if (rs.getString("PCEMOLUMENTS") != null) {
					remitancebean.setPcEmoluments(rs.getDouble("PCEMOLUMENTS"));
				} else {
					remitancebean.setPcEmoluments(0);
				}
				if (rs.getString("EPF3ACCRETION") != null) {
					remitancebean.setEpf3Accretion(rs
							.getDouble("EPF3ACCRETION"));
				} else {
					remitancebean.setEpf3Accretion(0);
				}
				if (rs.getString("EPF3PENSIONCONTRI") != null) {
					remitancebean.setEpf3Pensioncontri(rs
							.getDouble("EPF3PENSIONCONTRI"));
				} else {
					remitancebean.setEpf3Pensioncontri(0);
				}
				if (rs.getString("epf3Emoluments") != null) {
					remitancebean.setEpf3Emoluments(rs
							.getDouble("epf3Emoluments"));
				} else {
					remitancebean.setEpf3Emoluments(0);
				}
				// log.info("in remit table
				// "+remitancebean.getEpf3Emoluments());

				remitanceList = this.loadRemitanceInfo(salaryMonth,
						remitancebean.getRegion(), remitancebean
								.getAirportcode(), remitanceType, "");
				if (remitanceList.size() > 0) {
					AaiEpfform3Bean epf3bean = (AaiEpfform3Bean) remitanceList
							.get(0);
					remitancebean.setEpf3Accretion(Math.round(Double
							.parseDouble(epf3bean.getEpf3Accretion())));
					remitancebean.setEpf3Pensioncontri(Math.round(Double
							.parseDouble(epf3bean.getPensionContribution())));
					remitancebean
							.setEpf3InspectionCharges(Double
									.parseDouble(epf3bean.getEmppfstatury()) * 100 / 12 * 0.0018);
					remitancebean.setEpf3Emoluments(Double.parseDouble(epf3bean
							.getEmppfstatury()) * 100 / 12);
				}
				// log.info("in Epf3 table "+remitancebean.getEpf3Emoluments());
				remitanceTableList.add(remitancebean);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return remitanceTableList;
	}

	public void inserRemittanceInfo(RemittanceBean rbean, String username,
			String computername) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String remitflag = "", movingrecordVRNOUpdate = "";
		System.out.println(rbean.getRemitanceType().trim());
		try {
			if (rbean.getRemitanceType().trim().equals("aaiepf3Suppl")) {
				remitflag = "S";
			} else if (rbean.getRemitanceType().trim().equals("aiepf3Arr")) {
				remitflag = "R";
			} else {
				remitflag = "A";
			}
			con = commonDB.getConnection();
			st = con.createStatement();
			String queryRemitInsert = "insert into EMPLOYEE_REMITTANCEINFO(VRNO,VRDT,PFACCRETION,PFREMITTERBANKNM,PFREMITTERBANKACNO,PFNOOFEMPLOYEES,EMOLUMENTS,INSPECTIONCHARGES,PCNOOFEMPLOYEES,PENSIONCONTRIBUTION,PCREMITTERBANKNM,PCREMITTERBANKACNO,SALARYDT,airportcode,region,username,ipaddress,PCEMOLUMENTS,EPF3ACCRETION,EPF3PENSIONCONTRI,epf3Emoluments,remitflag,BILLREFNO ,BILLDATE ,CHEQUENOFROM,CHEQUENOTO,CHEQUEDT,PREPAREDBY,CHECKEDBY,PASSEDBY,RECEIVEDBY) values('"
					+ rbean.getVrNo()
					+ "','"
					+ rbean.getVrDt()
					+ "','"
					+ rbean.getPfAccretion()
					+ "','"
					+ rbean.getPfRemitterBankNM()
					+ "','"
					+ rbean.getPfRemitterBankACNo()
					+ "','"
					+ rbean.getPfnoofEmployees()
					+ "','"
					+ rbean.getEmoluments()
					+ "','"
					+ rbean.getInspectionCharges()
					+ "','"
					+ rbean.getPcNoofEmployees()
					+ "','"
					+ rbean.getPensionContribution()
					+ "','"
					+ rbean.getPcRemitterBankNM()
					+ "','"
					+ rbean.getPcRemitterBankAcNo()
					+ "','"
					+ rbean.getSalMonth()
					+ "','"
					+ rbean.getAirportcode()
					+ "','"
					+ rbean.getRegion()
					+ "','"
					+ username
					+ "','"
					+ computername
					+ "','"
					+ rbean.getPcEmoluments()
					+ "','"
					+ rbean.getPfAccretion()
					+ "','"
					+ rbean.getPensionContribution()
					+ "','"
					+ rbean.getEpf3Emoluments()
					+ "','"
					+ remitflag
					+ "','"
					+ rbean.getBillRefno()
					+ "','"
					+ rbean.getBilldate()
					+ "','"
					+ rbean.getChequenofrom()
					+ "','"
					+ rbean.getChequenoto()
					+ "','"
					+ rbean.getChequedt()
					+ "','"
					+ rbean.getPreparedby()
					+ "','"
					+ rbean.getCheckedby()
					+ "','"
					+ rbean.getPassedby()
					+ "','" + rbean.getReceivedby() + "')";
			String vrnoUpdationToValidateTable = "";
			if (rbean.getRemitanceType().trim().equals("aaiepf3Suppl")) {
				vrnoUpdationToValidateTable = "update employee_supplimentory_data set vrno='"
						+ rbean.getVrNo()
						+ "' where region='"
						+ rbean.getRegion()
						+ "' and lower(airportcode)='"
						+ rbean.getAirportcode().toLowerCase()
						+ "' and PAIDDATE between '"
						+ rbean.getSalMonth()
						+ "' and  last_day('"
						+ rbean.getSalMonth()
						+ "') and suppliflag='S'";
			} else {
				vrnoUpdationToValidateTable = "update employee_pension_validate set vrno='"
						+ rbean.getVrNo()
						+ "' where region='"
						+ rbean.getRegion()
						+ "' and lower(airportcode)='"
						+ rbean.getAirportcode().toLowerCase()
						+ "' and monthyear between '"
						+ rbean.getSalMonth()
						+ "' and  last_day('" + rbean.getSalMonth() + "')";
				movingrecordVRNOUpdate = "update employee_supplimentory_data set vrno='"
						+ rbean.getVrNo()
						+ "' where region='"
						+ rbean.getRegion()
						+ "' and lower(airportcode)='"
						+ rbean.getAirportcode().toLowerCase()
						+ "' and monthyear between '"
						+ rbean.getSalMonth()
						+ "' and  last_day('"
						+ rbean.getSalMonth()
						+ "') and suppliflag='N'";
			}
			log.info(queryRemitInsert);
			log.info(vrnoUpdationToValidateTable);
			log.info(movingrecordVRNOUpdate);
			st.executeUpdate(queryRemitInsert);
			st.executeUpdate(vrnoUpdationToValidateTable);
			st.executeUpdate(movingrecordVRNOUpdate);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
	}

	public ArrayList getNoOfEmployees(String salaryMonth, String region,
			String airportcode, String remitanceType) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList empList = new ArrayList();
		String table = "", condition = "";
		String query = "";
		if (remitanceType.trim().equals("aaiepf3Suppl")) {
			table = "employee_supplimentory_data";
			condition = "AND paiddate BETWEEN '" + salaryMonth
					+ "' and last_day('" + salaryMonth
					+ "') and empflag='Y' and SUPPLIFLAG='S'";
			query = "select * from (select count(*) as pfnoofemployees  from "
					+ table + " where region = '" + region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase() + "' " + condition
					+ " )tab1,(select count(*) as pcnoofemployees from "
					+ table + " where region='" + region
					+ "' and lower(airportcode)='" + airportcode.toLowerCase()
					+ "' " + condition + " and to_number(pensioncontri)>0)";
		} else if (remitanceType.trim().equals("aiepf3Arr")) {
			table = "employee_supplimentory_data";
			condition = "AND paiddate BETWEEN '" + salaryMonth
					+ "' and last_day('" + salaryMonth
					+ "') and empflag='Y' and SUPPLIFLAG='A'";
			query = "select * from (select count(*) as pfnoofemployees  from "
					+ table + " where region = '" + region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase() + "' " + condition
					+ " )tab1,(select count(*) as pcnoofemployees from "
					+ table + " where region='" + region
					+ "' and lower(airportcode)='" + airportcode.toLowerCase()
					+ "' " + condition + " and to_number(pensioncontri)>0)";
		} else {
			query = "select (validatetable.pfnoofemployees+supplitable.pfnoofemployees) as pfnoofemployees,(validatetable.pcnoofemployees+supplitable.pcnoofemployees) as pcnoofemployees from "
					+ "(select * from (select count(*) as pfnoofemployees  from employee_pension_validate   where region = '"
					+ region
					+ "'  and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND monthyear BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') and empflag = 'Y' and SUPPLIFLAG = 'N') tab1,"
					+ "(select count(*) as pcnoofemployees from employee_pension_validate where region = '"
					+ region
					+ "'	 and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND monthyear BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') and empflag = 'Y' and SUPPLIFLAG = 'N' and to_number(pensioncontri) > 0))validatetable,"
					+ "(select * from (select count(*) as pfnoofemployees   from employee_supplimentory_data    where region = '"
					+ region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND monthyear BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') and empflag = 'Y' and SUPPLIFLAG = 'N') tab1,"
					+ "(select count(*) as pcnoofemployees   from employee_supplimentory_data where region = '"
					+ region
					+ "' and lower(airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' AND monthyear BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') and empflag = 'Y' and SUPPLIFLAG = 'N'  and to_number(pensioncontri) > 0)) supplitable";

		}
		try {
			con = commonDB.getConnection();
			st = con.createStatement();

			log.info("getNoOfEmployees===Query " + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				AaiEpfform3Bean epf3bean = new AaiEpfform3Bean();
				if (rs.getString("pfnoofemployees") != null) {
					epf3bean.setEpf3PfNoofEmployees(Integer.parseInt(rs
							.getString("pfnoofemployees")));
				}
				if (rs.getString("pcnoofemployees") != null) {
					epf3bean.setEpf3PcNoofEmployees(Integer.parseInt(rs
							.getString("pcnoofemployees")));
				}
				empList.add(epf3bean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return empList;
	}

	public ArrayList getNoOfEmployeesForACCRETION(String salaryMonth,
			String region, String airportcode, String remitancetype) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList empList = new ArrayList();
		String condition = "";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "";
			if (remitancetype.equals("aaiepf3")) {
				condition = " and empflag='Y' AND MONTHYEAR BETWEEN '"
						+ salaryMonth + "' and last_day('" + salaryMonth + "')";
				query = "select sum(Pfnoofemployees) as pfnoofemployees,sum(Pcnoofemployees) as Pcnoofemployees from((select * from (select count(*) as pfnoofemployees  from employee_pension_validate where region = '"
						+ region
						+ "' and lower(airportcode) = '"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='N' "
						+ condition
						+ " )tab1,(select count(*) as pcnoofemployees from employee_pension_validate where region='"
						+ region
						+ "' and lower(airportcode)='"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='N' "
						+ condition
						+ " and to_number(pensioncontri)>0)) union all (select * from (select count(*) as pfnoofemployees  from employee_supplimentory_data where region = '"
						+ region
						+ "' and lower(airportcode) = '"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='N' "
						+ condition
						+ " )tab1,(select count(*) as pcnoofemployees from employee_supplimentory_data where region='"
						+ region
						+ "' and lower(airportcode)='"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='N' "
						+ condition + " and to_number(pensioncontri)>0)))";
			} else if (remitancetype.equals("aaiepf3Suppl")) {
				condition = " and empflag='Y' AND PAIDDATE BETWEEN '"
						+ salaryMonth + "' and last_day('" + salaryMonth + "')";
				query = "select * from (select count(*) as pfnoofemployees  from employee_supplimentory_data where region = '"
						+ region
						+ "' and lower(airportcode) = '"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='S' "
						+ condition
						+ " )tab1,(select count(*) as pcnoofemployees from employee_supplimentory_data where region='"
						+ region
						+ "' and lower(airportcode)='"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='S' "
						+ condition + " and to_number(pensioncontri)>0)";
			} else if (remitancetype.equals("aaiepf3Arr")) {
				condition = " and empflag='Y' AND MONTHYEAR BETWEEN '"
						+ salaryMonth + "' and last_day('" + salaryMonth + "')";
				query = "select * from (select count(*) as pfnoofemployees  from employee_supplimentory_data where region = '"
						+ region
						+ "' and lower(airportcode) = '"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='A' "
						+ condition
						+ " )tab1,(select count(*) as pcnoofemployees from employee_supplimentory_data where region='"
						+ region
						+ "' and lower(airportcode)='"
						+ airportcode.toLowerCase()
						+ "' and suppliflag='A' "
						+ condition + " and to_number(pensioncontri)>0)";
			}else if(remitancetype.equals("form4deputation")){
				condition = " and v.empflag='Y' AND s.MONTHYEAR BETWEEN '"
					+ salaryMonth + "' and last_day('" + salaryMonth + "')";
			query = "select * from (select count(*) as pfnoofemployees  FROM EMPLOYEE_PENSION_VALIDATE   v,EMPLOYEE_SUPPLIMENTORY_DATA s WHERE v.PENSIONNO = s.PENSIONNO AND v.EMPFLAG = s.EMPFLAG  AND v.EMPRECOVERYSTS = 'DEP' AND v.PENSIONNO IS NOT NULL and s.monthyear = v.monthyear AND s.SUPPLIFLAG IS NOT NULL AND s.SUPPLIFLAG in ('F') and s.region = '"
					+ region
					+ "' and lower(s.airportcode) = '"
					+ airportcode.toLowerCase()
					+ "' and v.suppliflag='Y' "
					+ condition
					+ " )tab1,(select count(*) as pcnoofemployees FROM EMPLOYEE_PENSION_VALIDATE   v,EMPLOYEE_SUPPLIMENTORY_DATA s WHERE v.PENSIONNO = s.PENSIONNO AND v.EMPFLAG = s.EMPFLAG  AND v.EMPRECOVERYSTS = 'DEP' AND v.PENSIONNO IS NOT NULL and s.monthyear = v.monthyear AND s.SUPPLIFLAG IS NOT NULL AND s.SUPPLIFLAG in ('F') and s.region = '"
					+ region
					+ "' and lower(s.airportcode)='"
					+ airportcode.toLowerCase()
					+ "' and v.suppliflag='Y' "
					+ condition + " and to_number(s.pensioncontri)>0)";
				
			}
			log.info("getNoOfEmployees===Query " + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				AaiEpfform3Bean epf3bean = new AaiEpfform3Bean();
				if (rs.getString("pfnoofemployees") != null) {
					epf3bean.setEpf3PfNoofEmployees(Integer.parseInt(rs
							.getString("pfnoofemployees")));
				}
				if (rs.getString("pcnoofemployees") != null) {
					epf3bean.setEpf3PcNoofEmployees(Integer.parseInt(rs
							.getString("pcnoofemployees")));
				}
				empList.add(epf3bean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return empList;
	}

	public ArrayList getGrandtotalsRegionwise(String salaryMonth,
			String region, String airportcode, String remitanceType) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList empList = new ArrayList();

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "select region,sum(PFACCRETION) as totalPF,sum(emoluments)as totalEmoluments,sum(Inspectioncharges) as totalInspectioncharges ,sum(PFNOOFEMPLOYEES) as totalPfnoofemployees, sum(Pcnoofemployees) as totalPcnoofemployees,sum(Pensioncontribution)as totalPensioncontribution from EMPLOYEE_REMITTANCEINFO where   SALARYDT BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "') group by region order by region";
			log.info("getNoOfEmployees===Query " + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				AaiEpfform3Bean epf3bean = new AaiEpfform3Bean();
				if (rs.getString("totalEmoluments") != null) {
					epf3bean.setTotalEmoluments(Double.parseDouble(rs
							.getString("totalEmoluments")));
				}
				if (rs.getString("totalInspectioncharges") != null) {
					epf3bean.setTotalInspectioncharges(Double.parseDouble(rs
							.getString("totalInspectioncharges")));
				}
				if (rs.getString("totalPcnoofemployees") != null) {
					epf3bean.setEpf3PcNoofEmployees(Integer.parseInt(rs
							.getString("totalPcnoofemployees")));
				}
				if (rs.getString("totalPensioncontribution") != null) {
					epf3bean.setTotalPensioncontribution(Double.parseDouble(rs
							.getString("totalPensioncontribution")));
				}
				if (rs.getString("region") != null) {
					epf3bean.setRegion(rs.getString("region"));
				}
				if (rs.getString("totalPfnoofemployees") != null) {
					epf3bean.setEpf3PfNoofEmployees(Integer.parseInt(rs
							.getString("totalPfnoofemployees")));
				}
				if (rs.getString("totalPF") != null) {
					epf3bean.setTotalPF(Double.parseDouble(rs
							.getString("totalPF")));
				}

				empList.add(epf3bean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return empList;
	}

	public double getAaiEpf8totals(String salaryMonth, String region,
			String airportcode) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		double epf8Totals = 0.00;
		double advanceAmt = 0.00, loanamt = 0.00, finamt = 0.00;

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String advanceQuery = "select sum(nvl(amount,0))as advanceamt from employee_pension_advances where ADVTRANSDATE  between '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "')  and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' and region='"
					+ region
					+ "' and keyno is null";
			String loanQuery = "select  sum(nvl(sub_amt,0))+sum(nvl(cont_amt,0)) as loanamt from employee_pension_loans  where loandate between '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "')  and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' and region='"
					+ region
					+ "' and keyno is null";
			String finalsettlementQuery = "select  sum(nvl(finemp,0)+nvl(finaai,0)+nvl(pencon,0))as finamt from employee_pension_finsettlement  where   settlementdate  BETWEEN '"
					+ salaryMonth
					+ "' and last_day('"
					+ salaryMonth
					+ "')  and lower(airportcode)='"
					+ airportcode.toLowerCase()
					+ "' and region='"
					+ region
					+ "' and keyno is null";
			log.info("getNoOfEmployees===Query " + advanceQuery);
			rs = st.executeQuery(advanceQuery);
			while (rs.next()) {
				if (rs.getString("advanceamt") != null) {
					advanceAmt = Double.parseDouble(rs.getString("advanceamt"));
				}

			}
			rs = st.executeQuery(loanQuery);
			while (rs.next()) {
				if (rs.getString("loanamt") != null) {
					loanamt = Double.parseDouble(rs.getString("loanamt"));
				}
			}
			rs = st.executeQuery(finalsettlementQuery);
			while (rs.next()) {
				if (rs.getString("finamt") != null) {
					finamt = Double.parseDouble(rs.getString("finamt"));
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		epf8Totals = advanceAmt + loanamt + finamt;
		System.out.println("epf8Totals **** " + epf8Totals);
		return epf8Totals;

	}

	public ArrayList loadAccrationReport(String range, String region,
			String airportcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno,
			String accountType, String remitancetype) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		String selQuery = "", fromDate = "", toDate = "", pcnoofemployeesquery = "";
		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList accrationDataList = new ArrayList();
		String regionDesc = "", stationDesc = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airportcode.equals("NO-SELECT")) {
			stationDesc = airportcode;
		} else {
			stationDesc = "";
		}
		selQuery = this.buildQueryAccrationTransInfo(regionDesc, stationDesc,
				fromDate, toDate, frmPensionno, accountType, remitancetype);
		/*
		 * pcnoofemployeesquery =
		 * this.buildQuerypcnoofemployees(regionDesc,stationDesc, fromDate,
		 * toDate, frmPensionno); String totalquery = "select * from (" +
		 * selQuery + ") trans1,(" + pcnoofemployeesquery + ") trans2 where
		 * trans1.region=trans2.region and trans1.airportcode=trans2.airportcode
		 * and trans1.monthyear=trans2.monthyear";
		 */
		String totalquery = selQuery;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();

			log.info("EPFFormsReportDAO::totalquery" + totalquery);
			rs = st.executeQuery(totalquery);
			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(rs.getString("MONTHYEAR"));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setEmoluments("0.00");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				if (rs.getString("additionalcontri") != null) {
					epfForm3Bean.setAdditionalContri(rs.getString("additionalcontri"));
				} else {
					epfForm3Bean.setAdditionalContri("0.00");
				}
				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim())
								+ Double.parseDouble(epfForm3Bean.getPf()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				if (rs.getString("CALCEMOLUMENTS") != null) {
					epfForm3Bean.setPensionContriEmoluments(rs
							.getString("CALCEMOLUMENTS"));
				} else {
					epfForm3Bean.setPensionContriEmoluments("0.00");
				}
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));

				if (rs.getString("unitname") != null) {
					epfForm3Bean.setStation(rs.getString("unitname"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("unitregion") != null) {
					epfForm3Bean.setRegion(rs.getString("unitregion"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs.getString("ACCOUNTTYPE") != null) {
					epfForm3Bean.setAccountType(rs.getString("ACCOUNTTYPE"));
				} else {
					epfForm3Bean.setAccountType("---");
				}
				if (rs.getString("VACCOUNTNO") != null) {
					epfForm3Bean.setVAccountNo(rs.getString("VACCOUNTNO"));
				} else {
					epfForm3Bean.setVAccountNo("---");
				}
				if (rs.getString("newunitcode") != null) {
					epfForm3Bean.setNewunitcode(rs.getString("newunitcode"));
				} else {
					epfForm3Bean.setNewunitcode("---");
				}

				// For getting the no of subscribers
				log.info("year " + epfForm3Bean.getMonthyear() + " region "
						+ epfForm3Bean.getRegion() + "airportcode"
						+ epfForm3Bean.getStation());
				ArrayList list = this.getNoOfEmployeesForACCRETION(fromDate,
						epfForm3Bean.getRegion(), epfForm3Bean.getStation(),
						remitancetype);
				for (int i = 0; i < list.size(); i++) {
					AaiEpfform3Bean epf3bean = (AaiEpfform3Bean) list.get(i);
					if (epf3bean.getEpf3PfNoofEmployees() != 0) {
						epfForm3Bean.setTotalSubscribers(String
								.valueOf(epf3bean.getEpf3PfNoofEmployees()));
					} else {
						epfForm3Bean.setTotalSubscribers("0");
					}
					if (epf3bean.getEpf3PcNoofEmployees() != 0) {

						epfForm3Bean.setEpf3PcNoofEmployees(epf3bean
								.getEpf3PcNoofEmployees());
					} else {
						epfForm3Bean.setEpf3PcNoofEmployees(0);
					}
				}

				accrationDataList.add(epfForm3Bean);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return accrationDataList;
	}

	public String buildQuerypcnoofemployees(String region, String airportcode,
			String fromDate, String toDate, String pensionno) {
		log
				.info("EPFFormsReportDAO::buildQueryForm5TransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		sqlQuery = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE, TRANS.REGION AS REGION,COUNT(TRANS.PENSIONNO) AS pcnoofemployees,to_char(monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR "
				+ "FROM EMPLOYEE_PENSION_VALIDATE TRANS "
				+ "WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL and to_number(pensioncontri) > 0 AND  MONTHYEAR BETWEEN '"
				+ fromDate + "' and LAST_DAY('" + toDate + "') ";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='" + airportcode + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String groupBy = "GROUP BY TRANS.AIRPORTCODE,TRANS.REGION,to_char(trans.monthyear,'Mon-yyyy')";
		String orderBy = "ORDER BY TRANS.REGION ,TRANS.AIRPORTCODE,to_date(ORDMONTHYEAR) ";
		query.append(groupBy + orderBy);
		dynamicQuery = query.toString();
		dynamicQuery = "select * from ("
				+ query
				+ ") tab1,(select unitname, region   from employee_unit_master where region = '"
				+ region
				+ "'  group by region, unitname) tab2 where tab2.unitname = tab1.airportcode(+) order by tab2.region, tab2.unitname";
		log.info("EPFFormsReportDAO::buildQueryForm5TransInfo Leaving Method");
		return dynamicQuery;
	}

	public String buildQueryAccrationTransInfo(String region,
			String airportcode, String fromDate, String toDate,
			String pensionno, String accountType, String remitancetype) {
		log
				.info("EPFFormsReportDAO::buildQueryAccrationTransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		String rgcondition = "", stcondition1 = "";
		if (!region.equals("")) {
			rgcondition = " and region='" + region + "'";
		}
		if (!airportcode.equals("")) {
			stcondition1 = " and upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'";
		}
		if (remitancetype.equals("aaiepf3")) {
			sqlQuery = "select MONTHYEAR,('01-' || MONTHYEAR) as ORDMONTHYEAR,sum(NOOFSUBSCRIBERS),sum(round(EMOLUMENTS)) AS EMOLUMENTS,sum(EMPPFSTATUARY) AS EMPPFSTATUARY,sum(EMPVPF) AS EMPVPF,sum(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,sum(EMPADVRECINTEREST) AS EMPADVRECINTEREST,sum(PENSIONCONTRI) AS PENSIONCONTRI,sum(PF) AS PF, sum(CALCEMOLUMENTS) AS CALCEMOLUMENTS,sum(additionalcontri) AS additionalcontri,airportcode,region from((SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,TRANS.REGION AS REGION,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
					+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
					+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF,SUM(round(TRANS.CALCEMOLUMENTS)) AS CALCEMOLUMENTS,SUM(round(TRANS.additionalcontri)) AS additionalcontri FROM EMPLOYEE_PENSION_VALIDATE TRANS "
					+ "WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL AND  MONTHYEAR BETWEEN '"
					+ fromDate
					+ "' and LAST_DAY('"
					+ toDate
					+ "') and suppliflag='N' and empflag='Y' "
					+ rgcondition
					+ " "
					+ stcondition1
					+ " GROUP BY TRANS.AIRPORTCODE,TRANS.REGION,to_char(trans.monthyear,'Mon-yyyy')) union all (SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,TRANS.REGION AS REGION,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(monthyear,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.monthyear,'Mon-yyyy')) as ORDMONTHYEAR,"
					+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
					+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF,SUM(round(TRANS.CALCEMOLUMENTS)) AS CALCEMOLUMENTS,SUM(round(TRANS.additionalcontri)) AS additionalcontri FROM EMPLOYEE_supplimentory_data TRANS "
					+ "WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL AND  MONTHYEAR BETWEEN '"
					+ fromDate
					+ "' and LAST_DAY('"
					+ toDate
					+ "') and suppliflag='N' and empflag='Y' "
					+ rgcondition
					+ " "
					+ stcondition1
					+ " GROUP BY TRANS.AIRPORTCODE,TRANS.REGION,to_char(trans.monthyear,'Mon-yyyy'))";
		} else if (remitancetype.equals("aaiepf3Suppl")) {
			sqlQuery = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,TRANS.REGION AS REGION,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(PAIDDATE,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.PAIDDATE,'Mon-yyyy')) as ORDMONTHYEAR,"
					+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
					+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF,SUM(round(TRANS.CALCEMOLUMENTS)) AS CALCEMOLUMENTS,SUM(round(TRANS.additionalcontri)) AS additionalcontri FROM employee_supplimentory_data TRANS "
					+ "WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL AND suppliflag='S' and empflag='Y' and  PAIDDATE BETWEEN '"
					+ fromDate + "' and LAST_DAY('" + toDate + "')";
		} else if (remitancetype.equals("aaiepf3Arr")) {
			sqlQuery = "SELECT TRANS.AIRPORTCODE AS AIRPORTCODE,TRANS.REGION AS REGION,COUNT(TRANS.PENSIONNO) AS NOOFSUBSCRIBERS,to_char(PAIDDATE,'Mon-yyyy') AS MONTHYEAR, ('01-'||to_char(trans.PAIDDATE,'Mon-yyyy')) as ORDMONTHYEAR,"
					+ "SUM(round(TRANS.EMOLUMENTS)) AS EMOLUMENTS,SUM(round(TRANS.EMPPFSTATUARY)) AS EMPPFSTATUARY,SUM(round(TRANS.EMPVPF)) AS EMPVPF,SUM(round(TRANS.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
					+ "SUM(round(TRANS.EMPADVRECINTEREST)) AS EMPADVRECINTEREST,SUM(round(TRANS.PENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(TRANS.PF)) AS PF,SUM(round(TRANS.CALCEMOLUMENTS)) AS CALCEMOLUMENTS,SUM(round(TRANS.additionalcontri)) AS additionalcontri FROM employee_supplimentory_data TRANS "
					+ "WHERE EMPFLAG = 'Y' AND PENSIONNO IS NOT NULL AND suppliflag='A' and empflag='Y' and  MONTHYEAR BETWEEN '"
					+ fromDate + "' and LAST_DAY('" + toDate + "')";
		}
		else if (remitancetype.equals("form4deputation")) {	
			sqlQuery="select max(airportcode) as airportcode, max(region) as region, count(pensionno) as NOOFSUBSCRIBERS, to_char(MONTHYEAR, 'Mon-yyyy') AS MONTHYEAR, ('01-' || to_char(MONTHYEAR, 'Mon-yyyy')) as ORDMONTHYEAR, SUM(round(CPFEMOLUMENTS) + round(AEMOLUMENTS) + round(SEMOLUMENTS)) AS EMOLUMENTS, SUM(round(CPFEMPPFSTATUARY) + round(AEMPPFSTATUARY) +"+
					 " round(SEMPPFSTATUARY)) AS EMPPFSTATUARY, SUM(round(CPFPENSIONCONTRI) + round(APENSIONCONTRI) + round(SPENSIONCONTRI)) AS PENSIONCONTRI, SUM(round(CPFPF) + round(APF) + round(SPF)) AS PF, SUM(round(cpfvpf) + round(AVPF) + round(SVPF)) AS EMPVPF, SUM(round(cpfadvprincipal) + round(Aadvrecprincipal) +  round(Sadvprincipal)) AS EMPADVRECPRINCIPAL,"+
					 " SUM(round(cpfadvrecint) + round(Aadvrecint) + round(Sadvrecint)) AS EMPADVRECINTEREST,  '0' as CALCEMOLUMENTS,SUM(round(cpfaddcontri) + round(Aaddcontri) +round(Saddcontri)) AS Additionalcontri from (SELECT DT.PAIDDATE AS MONTHYEAR, SUM((CASE WHEN (DT.ECRFORM4SUPPLIARRARFLAG = 'C') then round(NVL(DT.EMOLUMENTS, 0)) else 0  end)) as CPFEMOLUMENTS, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then round(NVL(DT.EMPPFSTATUARY, 0))"+
                     " else 0  end)) as CPFEMPPFSTATUARY,  sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then ROUND(NVL(DT.PENSIONCONTRI, 0))  else  0 end)) as CPFPENSIONCONTRI, sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then ROUND(NVL(DT.PF, 0))  elsE  0  end)) as CPFPF, sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then ROUND(NVL(DT.Empvpf, 0)) elsE  0"+
                     " end)) as cpfvpf, sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then  ROUND(NVL(DT.Empadvrecprincipal, 0)) elsE  0  end)) as cpfadvprincipal, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then ROUND(NVL(DT.Empadvrecinterest, 0))  elsE  0  end)) as cpfadvrecint,sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'C' then ROUND(NVL(DT.Additionalcontri, 0)) elsE  0 end)) as cpfaddcontri, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then  round(NVL(DT.EMOLUMENTS, 0))"+
                     " else  0 end)) as AEMOLUMENTS,sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then round(NVL(DT.EMPPFSTATUARY, 0)) else 0 end)) as AEMPPFSTATUARY, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then ROUND(NVL(DT.PENSIONCONTRI, 0))  else 0  end)) as APENSIONCONTRI,  sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then  ROUND(NVL(DT.PF, 0)) else"+
                     " 0  end)) as APF, sum((CASE  WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then ROUND(NVL(DT.Empvpf, 0)) else 0 end)) as AVPF, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then ROUND(NVL(DT.Empadvrecprincipal, 0)) else 0 end)) as Aadvrecprincipal, sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then  ROUND(NVL(DT.Empadvrecinterest, 0))  else 0  end)) as Aadvrecint,sum((CASE WHEN DT.ECRFORM4SUPPLIARRARFLAG = 'A' then ROUND(NVL(DT.Additionalcontri, 0)) else 0 end)) as Aaddcontri,"+
                     " 0 as SEMOLUMENTS, 0 as SEMPPFSTATUARY, 0 as SPENSIONCONTRI,0 as SPF, 0 as SVPF, 0 as Sadvprincipal, 0 as Sadvrecint,0 as Saddcontri ,max(DT.AIRPORTCODE) as AIRPORTCODE, max(DT.REGION) as REGION, max(EMPRECOVERYSTS) as recsts, DT.PENSIONNO, max(CASE  WHEN LEAVEFLAG = 'Y' THEN (select sum(noofleaves) from employee_monthly_leaves eml where eml.pensionno = VAL.Pensionno and  eml.monthyear = val.monthyear"+
                     " group by eml.monthyear, eml.pensionno)  else   0 end) as NCPDays, max(case when nvl(dt.pensioncontri, 0) != 0 and nvl(dt.EMPPFSTATUARY, 0) != 0 then  'BOTH' when nvl(dt.EMPPFSTATUARY, 0) != 0 then  'PF' Else  (case when nvl(dt.pensioncontri, 0) != 0 then 'PC'  end) end) as flag FROM EMPLOYEE_PENSION_VALIDATE VAL, EMPLOYEE_SUPPLIMENTORY_DATA DT WHERE VAL.PENSIONNO = DT.PENSIONNO AND "+
                     " VAL.EMPFLAG = DT.EMPFLAG AND VAL.EMPFLAG = 'Y' AND VAL.SUPPLIFLAG = 'Y' AND VAL.EMPRECOVERYSTS = 'DEP' AND VAL.PENSIONNO IS NOT NULL and dt.monthyear = val.monthyear AND DT.SUPPLIFLAG IS NOT NULL AND  DT.SUPPLIFLAG in ('F') and dt.monthyear = '"+fromDate+"'";
		}

		if (remitancetype.equals("aaiepf3Suppl")
				|| remitancetype.equals("aaiepf3Arr")
				|| remitancetype.equals("form4deputation")) {
			if (!region.equals("")) {
				if(remitancetype.equals("form4deputation")) {
					whereClause.append(" val.REGION ='" + region + "'");
				}else {
					whereClause.append(" REGION ='" + region + "'");
				}
				
				whereClause.append(" AND ");
			}
			if (!airportcode.equals("")) {
				if(remitancetype.equals("form4deputation")) {
					whereClause.append(" upper(val.AIRPORTCODE) ='"
							+ airportcode.toUpperCase() + "'");
				}else {
					whereClause.append(" upper(AIRPORTCODE) ='"
							+ airportcode.toUpperCase() + "'");
				}
				
				whereClause.append(" AND ");
			}
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			if (remitancetype.equals("aaiepf3Suppl")
					|| remitancetype.equals("aaiepf3Arr") 
					|| remitancetype.equals("form4deputation")) {
				query.append(" AND ");
			}
			query.append(commonDAO.sTokenFormat(whereClause));
		}
		String condition = "";
		if (!region.equals("")) {
			condition = condition + " where REGION ='" + region + "'";
			if (!accountType.trim().equals("")) {
				condition = condition + " and accounttype='" + accountType
						+ "'";
			}
		}
		if (!region.equals("") && !airportcode.equals("")) {
			condition = condition + " and unitname ='" + airportcode + "'";
		}
		String groupBy = "";
		String orderBy = "";
		if (remitancetype.equals("aaiepf3")) {
			groupBy = "";
		} else if (remitancetype.equals("aaiepf3Suppl")
				|| remitancetype.equals("aaiepf3Arr")) {
			groupBy = "GROUP BY TRANS.AIRPORTCODE,TRANS.REGION,to_char(trans.PAIDDATE,'Mon-yyyy')";
		}
				
		else if (remitancetype.equals("form4deputation")) {
			groupBy="GROUP BY DT.PAIDDATE, DT.PENSIONNO) GROUP BY MONTHYEAR, airportcode";
		}
		
		if (remitancetype.equals("aaiepf3")) {
			orderBy = "ORDER BY REGION,AIRPORTCODE,ORDMONTHYEAR)GROUP BY airportcode,REGION,MONTHYEAR ";
		} else if (remitancetype.equals("aaiepf3Suppl")
				|| remitancetype.equals("aaiepf3Arr")) {
			orderBy = "ORDER BY REGION,AIRPORTCODE,ORDMONTHYEAR ";
		}
		query.append(groupBy + orderBy);
		dynamicQuery = query.toString();

		dynamicQuery = "select * from ("
				+ query
				+ ") tab1,(select unitname, region as unitregion,ACCOUNTTYPE,VACCOUNTNO,newunitcode  from employee_unit_master   "
				+ condition
				+ " group by region,ACCOUNTTYPE,newunitcode,unitname,VACCOUNTNO order by region,ACCOUNTTYPE,unitname) tab2 where tab2.unitname = tab1.airportcode(+) and tab2.unitregion = tab1.region(+) order by tab2.unitregion,tab2.ACCOUNTTYPE,tab2.unitname";
		log
				.info("EPFFormsReportDAO::buildQueryAccrationTransInfo	 Leaving Method");
		return dynamicQuery;
	}
//	By Radha On 11-Jul-2012 For Seperation On Supplimentary Detalis
	//By Prasad on 31-Jan-2012
	public ArrayList getEPFForm3SippliBlockReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno,
			String suppliflag) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList SuppliList = new ArrayList();
		ArrayList SuppliList_Prev = new ArrayList();
		ArrayList SuppliList_Reg= new ArrayList();
		DecimalFormat df = new DecimalFormat("#########0.00");
		String[] selectedYears = frmSelctedYear.split(",");
		String suppliSqlQuery = "", fromDate = "", toDate = "", uploadDate = "";
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		AaiEpfform3Bean epfForm3Bean = null;

		String regionDesc = "", stationDesc = "", selPerQuery = "", selTransQuery = "", selQuery = "", pensionoNo = "";
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			stationDesc = "";
		}
		selPerQuery = this.buildQueryEmpPFInfo(range, regionDesc, stationDesc,
				empNameFlag, empName, sortingOrder, frmPensionno);
		selTransQuery = this.buildQueryEmpPFTransForm3SuppliInfo(regionDesc,
				stationDesc, sortingOrder, fromDate, toDate, suppliflag);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO, EPV.CPFACCNO as CPFACCNO, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI, EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.PAIDDATE AS PAIDATE,EPV.UPLOADDATE AS UPLOADDATE,EPV.REGSALFLAG AS REGSALFLAG,EPV.ADDITIONALCONTRI AS ADDITIONALCONTRI FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y' ORDER BY EPV.PENSIONNO";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log
					.info("EPFFormsReportDAO::getEPFForm3BlockSippliReport===selQuery"
							+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				epfForm3Bean = new AaiEpfform3Bean();

				epfForm3Bean = this.fillEPFForm3Properties(rs, epfForm3Bean);
				epfForm3Bean.setPaidDate(commonUtil.converDBToAppFormat(rs
						.getDate("PAIDATE")));
				if (rs.getString("UPLOADDATE") != null) {
					epfForm3Bean.setUploaddate(commonUtil.converDBToAppFormat(rs
							.getDate("UPLOADDATE")));
				} else {
					epfForm3Bean.setUploaddate("---");
				}
				if (rs.getString("REGSALFLAG") != null) {
					epfForm3Bean.setRegSalFlag(rs
							.getString("REGSALFLAG"));
				} else {
					epfForm3Bean.setRegSalFlag("N");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+Double.parseDouble(epfForm3Bean
										.getAdditionalContri().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));

				/*//uploadDate = this
						.getUploadDate(epfForm3Bean.getMonthyear(),epfForm3Bean.getEmoluments(),
								epfForm3Bean.getPensionno(), epfForm3Bean
										.getPaidDate());
				//epfForm3Bean.setUploaddate(uploadDate);
				 * 
*/				
				if(suppliflag.equals("S")){
					if(epfForm3Bean.getRegSalFlag().equals("Y")){
						SuppliList_Reg.add(epfForm3Bean);
					}else{				
						SuppliList_Prev.add(epfForm3Bean);
					}
					}else{
						SuppliList.add(epfForm3Bean);
					}
				}
				if(suppliflag.equals("S")){
				SuppliList.add(SuppliList_Reg) ;
				SuppliList.add(SuppliList_Prev) ;
				 
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return SuppliList;
	}
	//By Radha On 11-Jul-2012 For Seperation On Supplimentary Detalis
	//	By Prasad on 31-Jan-2012
	// By Radha on 01-Oct-2011 for getting suppli data based on suppli flag from
	// trans data
	// New Method
	public String buildQueryEmpPFTransForm3SuppliInfo(String region,
			String airportcode, String sortedColumn, String fromDate,
			String toDate, String suppliflag) {
		log
				.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";

		sqlQuery = "SELECT SUPPLIDATA.* FROM (SELECT  PENSIONNO,MONTHYEAR FROM EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG='Y' AND SUPPLIFLAG = 'Y') TRANS,(SELECT MONTHYEAR, round(EMOLUMENTS) as EMOLUMENTS, round(EMPPFSTATUARY) AS EMPPFSTATUARY, round(EMPVPF) AS EMPVPF, round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ " round(EMPADVRECINTEREST) AS EMPADVRECINTEREST, ROUND(PENSIONCONTRI) AS PENSIONCONTRI, ROUND(PF) AS PF, AIRPORTCODE, "
				+ " REGION, EMPFLAG, CPFACCNO,PENSIONNO,EMPLOYEENO,PAIDDATE,UPLOADDATE,(CASE WHEN (ADD_MONTHS(SALMONTH,1)=PAIDDATE) THEN  'Y'  ELSE 'N'  END) AS REGSALFLAG,ADDITIONALCONTRI  FROM employee_supplimentory_data WHERE EMPFLAG = 'Y'  AND PENSIONNO IS NOT NULL"
				+ " and PAIDDATE BETWEEN '"
				+ fromDate
				+ "' and LAST_DAY('"
				+ toDate
				+ "')  and SUPPLIFLAG='"
				+ suppliflag
				+ "') SUPPLIDATA WHERE TRANS.PENSIONNO=SUPPLIDATA.PENSIONNO AND TRANS.MONTHYEAR=SUPPLIDATA.MONTHYEAR";

		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(AIRPORTCODE) ='"
					+ airportcode.toUpperCase() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "   ORDER BY  SUPPLIDATA.PENSIONNO";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryEmpPFTransInfo Leaving Method");
		return dynamicQuery;
	}


	public AaiEpfform3Bean getEPFForm3EmpSuppliData(String pensionNo,
			String monthYear, AaiEpfform3Bean epfForm3Bean, Connection con) {

		Statement st = null;
		ResultSet rs = null;
		String suppliSqlQuery = "";
		suppliSqlQuery = "SELECT EPI.PENSIONNO AS PENSIONNO,ESD.CPFACCNO AS CPFACCNO,ESD.EMPLOYEENO AS EMPLOYEENO,INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,EPI.DESEGNATION AS DESEGNATION,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.FHNAME AS FHNAME,EPI.WETHEROPTION AS WETHEROPTION , ESD.MONTHYEAR, round(ESD.EMOLUMENTS) as EMOLUMENTS, round(ESD.EMPPFSTATUARY) AS EMPPFSTATUARY, round(ESD.EMPVPF) AS EMPVPF, round(ESD.EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,"
				+ "round(ESD.EMPADVRECINTEREST) AS EMPADVRECINTEREST, ROUND(ESD.PENSIONCONTRI) AS PENSIONCONTRI, ROUND(ESD.PF) AS PF, ESD.AIRPORTCODE AS AIRPORTCODE, "
				+ "ESD.REGION AS REGION, ESD.EMPFLAG AS EMPFLAG,ESD.SUPPLIFLAG  AS SUPPLIFLAG,ESD.ADDITIONALCONTRI as ADDITIONALCONTRI FROM employee_supplimentory_data  ESD,employee_personal_info EPI WHERE ESD.PENSIONNO=EPI.PENSIONNO AND EPI.EMPFLAG='Y' AND  ESD.EMPFLAG = 'Y'  AND 	EPI.PENSIONNO='"
				+ pensionNo
				+ "'  and ESD.MONTHYEAR =  '"
				+ monthYear
				+ "' and ESD.SUPPLIFLAG='N' ";
		try {
			log.info("EPFFormsReportDAO::getEPFForm3Report===suppliSqlQuery"
					+ suppliSqlQuery);
			st = con.createStatement();
			rs = st.executeQuery(suppliSqlQuery);

			if (rs.next()) {
				epfForm3Bean = this.fillEPFForm3Properties(rs, epfForm3Bean);
			} else {
				epfForm3Bean = null;
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return epfForm3Bean;
	}

	/*public ArrayList getForm3SummaryReport(String year, String flag,
			String empStatus) {

		ArrayList arrearslist = new ArrayList();

		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		String region = "", airportcode = "---", sqlQueryAllRegions = "", sqlQueryCHQIAD = "", selectedYear = "", toSelectYear = "", fromYear = "", toYear = "", condition = "";
		String years[] = null;
		double totalEmoluments = 0.0, totalEPF = 0.0, totalVPF = 0.0, totalPrincipal = 0.0, totalInterest = 0.0, totalPF = 0.0, totalPensoinContri = 0.0;
		double totalSubscription = 0.0, totalContribution = 0.0, totalGrandSubscription = 0.0, totalGrandContribution = 0.0;

		EmployeePensionCardInfo summaryInfo = null;
		DecimalFormat df = new DecimalFormat("#########0");
		ArrayList list = new ArrayList();
		years = year.split("-");
		fromYear = "01-Apr-" + years[0];
		toYear = "31-Mar-" + years[1];
		if (!empStatus.equals("")) {
			condition = " and vemp.status='" + empStatus + "' ";
		}
		sqlQueryAllRegions = " select val.region  as region ,sum(round(nvl(val.emoluments, 0.0))) as emoluments,sum(round(nvl(val.emppfstatuary, 0.0))) as epf,"
				+ " sum(round(nvl(val.empvpf, 0.0))) as vpf,sum(round(nvl(val.empadvrecprincipal, 0.0))) as principal, sum(round(nvl(val.empadvrecinterest, 0.0))) as interest,"
				+ " sum(round(nvl(val.pf, 0.0))) as pf, sum(round(nvl(val.pensioncontri, 0.0))) as penContri  from employee_pension_validate val ,v_emp_personal_info vemp   where val.empflag = 'Y' AND "
				+ " val.pensionno = vemp.pensionno   "
				+ condition
				+ "   and vemp.finyear = '"
				+ year
				+ "' and  val.monthyear between  '"
				+ fromYear
				+ "' AND   '"
				+ toYear
				+ "'  and val.region !='CHQIAD'  and val.region is not null group by   val.region ";

		sqlQueryCHQIAD = " select  val.region as region , val.airportcode as airportcode,sum(round(nvl(val.emoluments, 0.0))) as emoluments, sum(round(nvl(val.emppfstatuary, 0.0))) as epf,"
				+ " sum(round(nvl(val.empvpf, 0.0))) as vpf,sum(round(nvl(val.empadvrecprincipal, 0.0))) as principal, sum(round(nvl(val.empadvrecinterest, 0.0))) as interest,"
				+ " sum(round(nvl(val.pf, 0.0))) as pf, sum(round(nvl(val.pensioncontri, 0.0))) as penContri  from employee_pension_validate val ,v_emp_personal_info vemp  where val.empflag = 'Y' AND "
				+ " val.pensionno = vemp.pensionno     and vemp.finyear = '"
				+ year
				+ "'   "
				+ condition
				+ "    and   val.monthyear between '"
				+ fromYear
				+ "' AND   '"
				+ toYear
				+ "' and  val.region ='CHQIAD' and  val.region is not null and  val.airportcode is not null group by    val.region, val.airportcode  order by  val.airportcode";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			if (flag.equals("ALL")) {
				log
						.info("FinancialReportDAO::getForm3SummaryReport--sqlQueryAllRegions "
								+ sqlQueryAllRegions);
				rs = st.executeQuery(sqlQueryAllRegions);
			} else {
				log
						.info("FinancialReportDAO::getForm3SummaryReport--sqlQueryCHQIAD "
								+ sqlQueryCHQIAD);
				rs = st.executeQuery(sqlQueryCHQIAD);
			}

			while (rs.next()) {
				summaryInfo = new EmployeePensionCardInfo();

				region = rs.getString("region");

				if (flag.equals("CHQIAD")) {
					airportcode = rs.getString("airportcode");
					summaryInfo.setStation(airportcode);
				}

				if (rs.getString("emoluments") != null) {
					totalEmoluments = Double.parseDouble(rs
							.getString("emoluments"));

				} else {
					totalEmoluments = 0.0;
				}
				if (rs.getString("epf") != null) {
					totalEPF = Double.parseDouble(rs.getString("epf"));
				} else {
					totalEPF = 0.0;
				}
				if (rs.getString("vpf") != null) {
					totalVPF = Double.parseDouble(rs.getString("vpf"));
				} else {
					totalVPF = 0.0;
				}
				if (rs.getString("principal") != null) {
					totalPrincipal = Double.parseDouble(rs
							.getString("principal"));
				} else {
					totalPrincipal = 0.0;
				}
				if (rs.getString("interest") != null) {
					totalInterest = Double
							.parseDouble(rs.getString("interest"));
				} else {
					totalInterest = 0.0;
				}
				if (rs.getString("pf") != null) {
					totalPF = Double.parseDouble(rs.getString("pf"));
				} else {
					totalPF = 0.0;
				}
				if (rs.getString("penContri") != null) {
					totalPensoinContri = Double.parseDouble(rs
							.getString("penContri"));
				} else {
					totalPensoinContri = 0.0;
				}

				totalSubscription = totalEmoluments + totalEPF + totalVPF
						+ totalPrincipal + totalInterest;
				totalGrandSubscription = totalGrandSubscription
						+ totalSubscription;
				totalContribution = totalPF + totalPensoinContri;
				totalGrandContribution = totalGrandContribution
						+ totalContribution;

				summaryInfo.setRegion(region);
				summaryInfo.setStation(airportcode);

				summaryInfo.setEmoluments(df
						.format(Math.round(totalEmoluments)));
				summaryInfo.setEmppfstatury(df.format(Math.round(totalEPF)));
				summaryInfo.setEmpvpf(df.format(Math.round(totalVPF)));
				summaryInfo.setPrincipal(df.format(Math.round(totalPrincipal)));
				summaryInfo.setInterest(df.format(Math.round(totalInterest)));
				summaryInfo.setAaiPF(df.format(Math.round(totalPF)));
				summaryInfo.setPensionContribution(df.format(Math
						.round(totalPensoinContri)));
				// summaryInfo.setSubscriptionSummary(df.format(Math.round(totalGrandSubscription)));
				// summaryInfo.setContributionSummary(df.format(Math.round(totalGrandContribution)));
				list.add(summaryInfo);

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, null);
		}
		return list;
	}*/

	// By Radha on 05-Oct-2011 for Justifiaction Data purpose
	// By Radha on 07-Sep-2011 for Form2 A Report
	public ArrayList getControlAccSummaryReport(String finyear,
			String formName, String region, String serialNo,
			String crtlAccFlag, String empStatus, String reportpurpose) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList cbobList = new ArrayList();
		ArrayList controlACCList = new ArrayList();
		ArrayList controlACCList_Prev = new ArrayList();
		ArrayList form2SummaryList = new ArrayList();
		ArrayList form2ASummaryList = new ArrayList();
		ArrayList form8SummaryList = new ArrayList();
		ArrayList crtlAccJustificationList = new ArrayList();
		ArrayList summaryList = new ArrayList();
		AaiEpfForm11Bean summaryInfo = new AaiEpfForm11Bean();

		String finyear_prev = "";
		String years[] = null;
		int fromYear_prev = 0, toYear_prev = 0;
		years = finyear.split("-");
		fromYear_prev = Integer.parseInt(years[0]) - 1;
		toYear_prev = Integer.parseInt(years[1]) - 1;
		finyear_prev = fromYear_prev + "-" + toYear_prev;

		try {
			con = commonDB.getConnection();
			log.info("--formName-----" + formName);
			if (formName.equals("form2summary")) {
				form2SummaryList = this.getForm2RegionwiseSummaryReport(con,
						finyear, region, empStatus);
				summaryInfo.setForm2SummaryList(form2SummaryList);
				if ((form2SummaryList.size() > 0)
						&& (!reportpurpose.equals("view"))) {
					commonDAO.updateControlAccntStatus(formName, serialNo,
							crtlAccFlag);
				}
			} else if (formName.equals("form2Asummary")) {
				form2ASummaryList = this.getForm2ASummaryReport(con, finyear,
						region);
				summaryInfo.setForm2ASummaryList(form2ASummaryList);
				if ((!reportpurpose.equals("view"))) {
					commonDAO.updateControlAccntStatus(formName, serialNo,
							crtlAccFlag);
				}
			} else if (formName.equals("form8summary")) {
				form8SummaryList = this.getForm8SummaryReport(con, finyear,
						empStatus);
				summaryInfo.setForm8SummaryList(form8SummaryList);
				if ((form8SummaryList.size() > 0)
						&& (!reportpurpose.equals("view"))) {
					commonDAO.updateControlAccntStatus(formName, serialNo,
							crtlAccFlag);
				}
			} else {
				cbobList = this.getClosingAndOpeningBalancesSummary(con,
						finyear, empStatus);
				controlACCList = this.getControlAccountClosingBalancesSummary(
						con, finyear, empStatus);
				controlACCList_Prev = this
						.getControlAccountClosingBalancesSummary(con,
								finyear_prev, empStatus);
				form2SummaryList = this.getForm2RegionwiseSummaryReport(con,
						finyear, region, empStatus);
				form8SummaryList = this.getForm8SummaryReport(con, finyear,
						empStatus);
				crtlAccJustificationList = this.getJustificationData(con,
						finyear, empStatus);
				if ((controlACCList.size() > 0)
						&& (!reportpurpose.equals("view"))) {
					commonDAO.updateControlAccntStatus(formName, serialNo,
							crtlAccFlag);
				}

				summaryInfo.setCBOBList(cbobList);
				summaryInfo.setControlAccountSummaryList(controlACCList);
				summaryInfo
						.setControlAccountSummaryList_prev(controlACCList_Prev);
				summaryInfo.setForm8SummaryList(form8SummaryList);
				summaryInfo.setForm2SummaryList(form2SummaryList);
				summaryInfo
						.setCrtlAccJustificationList(crtlAccJustificationList);

			}

			summaryList.add(summaryInfo);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return summaryList;
	}

	// New Method

	public ArrayList getForm2RegionwiseSummaryReport(Connection con,
			String finyear, String region, String empStatus) {

		Statement st = null;
		ResultSet rs = null;
		String form2summaryQuery = "", fromYear = "", toYear = "", dateOfRetriment = "";
		double subAmt = 0.0, subIntAmt = 0.0, subTot = 0.0, contriAmt = 0.0, penContriAmt = 0.0, contriTot = 0.0, outStandingAmt = 0.0;
		double subAmtGrandTot = 0.0, subIntAmtGrandTot = 0.0, subTotGrandTot = 0.0, contriAmtGrandTot = 0.0, penContriAmtGrandTot = 0.0, contriTotGrandTot = 0.0, outStandingAmtGrandTot = 0.0;
		AaiEpfForm11Bean form2summaryBean = null;
		AaiEpfForm11Bean form2GrnTotBean = new AaiEpfForm11Bean();
		ArrayList form2SummaryList = new ArrayList();
		ArrayList form2List = new ArrayList();
		ArrayList form2GrandTotList = new ArrayList();
		DecimalFormat df = new DecimalFormat("#########0");
		int fromYearVal = 0;
		log.info("--finyear----" + finyear);
		String[] dateArr = null;
		if (!finyear.equals("")) {
			dateArr = finyear.split("-");
			fromYear = "01-Apr-" + dateArr[0];
			toYear = "31-Mar-" + dateArr[1];
		}
		log.info("--fromYear----" + fromYear + "--toYear----" + toYear);
		form2summaryQuery = this.buildQueryForm2RegionwiseSummaryReport(region,
				fromYear, toYear, empStatus);
		log
				.info("FinancialReportDAO::getForm2SummaryReport--form2summaryQuery--"
						+ form2summaryQuery);

		try {

			st = con.createStatement();
			rs = st.executeQuery(form2summaryQuery);

			while (rs.next()) {
				form2summaryBean = new AaiEpfForm11Bean();

				if (rs.getString("PENSIONNO") != null) {
					form2summaryBean.setPensionNo(rs.getString("PENSIONNO"));
				} else {
					form2summaryBean.setPensionNo("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					form2summaryBean.setEmployeeName(rs
							.getString("EMPLOYEENAME"));
				} else {
					form2summaryBean.setEmployeeName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					form2summaryBean.setDateOfBirth(CommonUtil.getDatetoString(
							rs.getDate("DATEOFBIRTH"), "dd-MMM-yyyy"));
				} else {
					form2summaryBean.setDateOfBirth("--");
				}

				/*
				 * if (!form2summaryBean.getDateOfBirth().equals("--")) {
				 * form2summaryBean.setPensionNo(commonDAO.getPFID(form2summaryBean
				 * .getEmployeeName(), form2summaryBean.getDateOfBirth(),
				 * commonUtil.leadingZeros(5, rs .getString("PENSIONNO")))); }
				 * else {
				 * form2summaryBean.setPensionNo(rs.getString("PENSIONNO")); }
				 */

				if (rs.getString("ADJOBYEAR") != null) {
					form2summaryBean.setObYear(CommonUtil.getDatetoString(rs
							.getDate("ADJOBYEAR"), "dd-MMM-yyyy"));
				} else {
					form2summaryBean.setObYear("");
				}

				if (rs.getString("SUBSCRIPTIONAMT") != null) {
					subAmt = rs.getDouble("SUBSCRIPTIONAMT");
				} else {
					subAmt = 0.0;
				}

				if (rs.getString("SUBSCRIPTIONINTEREST") != null) {
					subIntAmt = rs.getDouble("SUBSCRIPTIONINTEREST");
				} else {
					subIntAmt = 0.0;
				}

				if (rs.getString("CONTRIBUTIONAMT") != null) {
					contriAmt = rs.getDouble("CONTRIBUTIONAMT");
				} else {
					contriAmt = 0.0;
				}

				if (rs.getString("PENSIONINTEREST") != null) {
					penContriAmt = rs.getDouble("PENSIONINTEREST");
				} else {
					penContriAmt = 0.0;
				}

				if (rs.getString("OUTSTANDADV") != null) {
					outStandingAmt = rs.getDouble("OUTSTANDADV");
				} else {
					outStandingAmt = 0.0;
				}

				subTot = subAmt + subIntAmt;
				contriTot = contriAmt + penContriAmt;

				subAmtGrandTot = subAmtGrandTot + subAmt;
				subIntAmtGrandTot = subIntAmtGrandTot + subIntAmt;
				subTotGrandTot = subTotGrandTot + subTot;
				contriAmtGrandTot = contriAmtGrandTot + contriAmt;
				penContriAmtGrandTot = penContriAmtGrandTot + penContriAmt;
				contriTotGrandTot = contriTotGrandTot + contriTot;
				outStandingAmtGrandTot = outStandingAmtGrandTot
						+ outStandingAmt;

				form2summaryBean.setAdjEmpSub(df.format(Math.round(subAmt)));
				form2summaryBean.setAdjEmpSubInterest(df.format(Math
						.round(subIntAmt)));
				form2summaryBean.setAdjEmpSubTotal(df
						.format(Math.round(subTot)));
				form2summaryBean
						.setAdjPension(df.format(Math.round(contriAmt)));
				form2summaryBean.setAdjPensionInt(df.format(Math
						.round(penContriAmt)));
				form2summaryBean.setAdjAaiContr(df
						.format(Math.round(contriTot)));
				form2summaryBean.setOutStndAdv(df.format(Math
						.round(outStandingAmt)));

				form2List.add(form2summaryBean);
			}
			form2GrnTotBean.setAdjEmpSubAmtGrandTot(df.format(Math
					.round(subAmtGrandTot)));
			form2GrnTotBean.setAdjEmpsubIntrstAmtGrandTot(df.format(Math
					.round(subIntAmtGrandTot)));
			form2GrnTotBean.setAdjEmpSubAmtTotlaGrandTot(df.format(Math
					.round(subTotGrandTot)));
			form2GrnTotBean.setAdjPensionAmtGrandTot(df.format(Math
					.round(contriAmtGrandTot)));
			form2GrnTotBean.setAdjPensionIntrstGrandTot(df.format(Math
					.round(penContriAmtGrandTot)));
			form2GrnTotBean.setAdjAAIContriTotGrandTot(df.format(Math
					.round(contriTotGrandTot)));
			form2GrnTotBean.setOutStndAdvGrandTot(df.format(Math
					.round(outStandingAmtGrandTot)));

			form2GrandTotList.add(form2GrnTotBean);

			form2SummaryList.add(form2List);
			form2SummaryList.add(form2GrandTotList);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, null);
		}

		log.info("==== getForm2SummaryReport() Size in DAO====="
				+ form2SummaryList.size());
		return form2SummaryList;
	}

	// New Method
	public String buildQueryForm2RegionwiseSummaryReport(String region,
			String fromYear, String toYear, String empStatus) {
		log
				.info("FinancialReportDAO::buildQueryForm2RegionwiseSummaryReport-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "", finYear = "";
		String frmYear[] = null;
		int frmYearVal = 0;
		frmYear = fromYear.split("-");
		frmYearVal = Integer.parseInt(frmYear[2]);
		finYear = frmYearVal + "-" + (frmYearVal + 1);
		log.info("-------frmYearVAl-----" + frmYearVal + "---finYear----"
				+ finYear);
		if (frmYearVal <= 2009) {

			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EAO.ADJOBYEAR AS ADJOBYEAR, nvl(EAO.EMPSUB,0.0) AS SUBSCRIPTIONAMT, nvl(EAO.EMPSUBINTEREST,0.0)  AS  SUBSCRIPTIONINTEREST,nvl(EAO.PENSIONTOTAL,0.0) AS CONTRIBUTIONAMT,nvl(EAO.PENSIONINTEREST,0.0) AS PENSIONINTEREST,nvl(EAO.OUTSTANDADV,0.0) AS OUTSTANDADV"
					+ " FROM employee_personal_info EPI, employee_adj_ob EAO ,  v_emp_personal_info VEMP  WHERE EPI.PENSIONNO = EAO.PENSIONNO AND EAO.PENSIONNO = VEMP.PENSIONNO  AND VEMP.FINYEAR='"
					+ finYear
					+ "' and  EAO.ADJOBYEAR BETWEEN '"
					+ fromYear
					+ "' AND last_day('"
					+ toYear
					+ "') and  EAO.exceptionflag ='N'    and  EAO.REGION is not null  and EPI.PENSIONNO is not null ";
		} else {

			sqlQuery = "select EPI.PENSIONNO,EPI.CPFACNO,EPI.EMPLOYEENO,EPI.EMPLOYEENAME,EPI.DESEGNATION,EPI.FHNAME,EPI.DATEOFBIRTH,EPI.AIRPORTCODE,EPI.REGION,EAO.ADJOBYEAR AS ADJOBYEAR, nvl(EAO.EMPSUB,0.0) AS SUBSCRIPTIONAMT, nvl(EAO.EMPSUBINTEREST,0.0)  AS  SUBSCRIPTIONINTEREST,-1*(nvl(EAO.PENSIONTOTAL,0.0)) AS CONTRIBUTIONAMT,-1*(nvl(EAO.PENSIONINTEREST,0.0)) AS PENSIONINTEREST,nvl(EAO.OUTSTANDADV,0.0) AS OUTSTANDADV"
					+ " FROM employee_personal_info EPI, employee_adj_ob EAO , v_emp_personal_info VEMP WHERE EPI.PENSIONNO = EAO.PENSIONNO AND EAO.PENSIONNO = VEMP.PENSIONNO  AND VEMP.FINYEAR='"
					+ finYear
					+ "' and  EAO.ADJOBYEAR BETWEEN '"
					+ fromYear
					+ "' AND last_day('"
					+ toYear
					+ "') and EAO.exceptionflag ='N'  and EAO.REGION is not null  and EPI.PENSIONNO is not null ";

		}

		if (!region.equals("NO-SELECT")) {
			whereClause.append(" EPI.REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		if (!empStatus.equals("")) {
			whereClause.append(" VEMP.STATUS  ='" + empStatus + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if (region.equals("NO-SELECT") && empStatus.equals("")) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}
		orderBy = " ORDER BY   EPI.REGION,EPI.AIRPORTCODE,EPI.PENSIONNO  ASC";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log
				.info("FinancialReportDAO::buildQueryForm2RegionwiseSummaryReport Leaving Method");
		return dynamicQuery;
	}

	// New Method
	public ArrayList getForm2ASummaryReport(Connection con, String finyear,
			String region) {

		Statement st = null;
		ResultSet rs = null;
		String form2AsummaryQuery = "", fromYear = "", toYear = "", dateOfRetriment = "";
		double CurrentAdjTotal = 0.0, FreezdAdjTotal = 0.0, DiffAdjAmt = 0.0;
		AaiEpfForm11Bean form2AsummaryBean = null;
		AaiEpfForm11Bean form2GrnTotBean = new AaiEpfForm11Bean();
		ArrayList form2ASummaryList = new ArrayList();
		DecimalFormat df = new DecimalFormat("#########0");
		int fromYearVal = 0;
		log.info("--finyear----" + finyear);
		String[] dateArr = null;
		if (!finyear.equals("")) {
			dateArr = finyear.split("-");
			fromYear = "01-Apr-" + dateArr[0];
			toYear = "31-Mar-" + dateArr[1];
		}
		log.info("--fromYear----" + fromYear + "--toYear----" + toYear);
		form2AsummaryQuery = this.buildQueryForm2ASummaryReport(con, region,
				finyear);
		log
				.info("FinancialReportDAO::getForm2ASummaryReport--form2AsummaryQuery--"
						+ form2AsummaryQuery);

		try {

			st = con.createStatement();
			rs = st.executeQuery(form2AsummaryQuery);

			while (rs.next()) {
				form2AsummaryBean = new AaiEpfForm11Bean();

				if (rs.getString("PENSIONNO") != null) {
					form2AsummaryBean.setPensionNo(rs.getString("PENSIONNO"));
				} else {
					form2AsummaryBean.setPensionNo("--");
				}

				if (rs.getString("EMPLOYEENAME") != null) {
					form2AsummaryBean.setEmployeeName(rs
							.getString("EMPLOYEENAME"));
				} else {
					form2AsummaryBean.setEmployeeName("--");
				}

				if (rs.getString("DATEOFBIRTH") != null) {
					form2AsummaryBean.setDateOfBirth(CommonUtil
							.getDatetoString(rs.getDate("DATEOFBIRTH"),
									"dd-MMM-yyyy"));
				} else {
					form2AsummaryBean.setDateOfBirth("--");
				}

				if (rs.getString("DATEOFJOINING") != null) {
					form2AsummaryBean.setDateofJoining(CommonUtil
							.getDatetoString(rs.getDate("DATEOFJOINING"),
									"dd-MMM-yyyy"));
				} else {
					form2AsummaryBean.setDateofJoining("--");
				}
				if (rs.getString("DESEGNATION") != null) {
					form2AsummaryBean.setDesignation(rs
							.getString("DESEGNATION"));
				} else {
					form2AsummaryBean.setDesignation("--");
				}

				if (rs.getString("FreezTotal") != null) {
					FreezdAdjTotal = rs.getDouble("FreezTotal");
				} else {
					FreezdAdjTotal = 0.0;
				}

				if (rs.getString("CurrentTotal") != null) {
					CurrentAdjTotal = rs.getDouble("CurrentTotal");
				} else {
					CurrentAdjTotal = 0.0;
				}

				if (rs.getString("DiffAmt") != null) {
					DiffAdjAmt = rs.getDouble("DiffAmt");
				} else {
					DiffAdjAmt = 0.0;
				}

				form2AsummaryBean.setFreezdAdjTotal(df.format(Math
						.round(FreezdAdjTotal)));
				form2AsummaryBean.setCurrentAdjTotal(df.format(Math
						.round(CurrentAdjTotal)));
				form2AsummaryBean.setDiffAdjAmt(df.format(Math
						.round(DiffAdjAmt)));

				form2ASummaryList.add(form2AsummaryBean);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, null);
		}

		log.info("==== getForm2ASummaryReport() Size in DAO====="
				+ form2ASummaryList.size());
		return form2ASummaryList;
	}

	public String buildQueryForm2ASummaryReport(Connection con, String region,
			String FinYear) {
		log
				.info("FinancialReportDAO::buildQueryForm2ASummaryReport-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", orderBy = "", adjOBYear = "", finYearFreezOrNot = "", frmFinYear = "", toFinYear = "";
		String frmYear[] = null;
		int frmYearVal = 0, ToYearVal = 0;
		frmYear = FinYear.split("-");
		frmYearVal = Integer.parseInt(frmYear[0]);
		ToYearVal = Integer.parseInt(frmYear[1]);
		adjOBYear = "01-Apr-" + (Integer.parseInt(frmYear[0]) - 1);
		frmFinYear = "01-Apr-" + Integer.parseInt(frmYear[0]);
		toFinYear = "31-Mar-" + Integer.parseInt(frmYear[1]);
		log.info("---adjOBYear--" + adjOBYear + "---frmFinYear--" + frmFinYear
				+ "---toFinYear--" + toFinYear);
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();

			finYearFreezOrNot = " select 'X' as flag from epis_freeze_year_details f where f.formfinyear='"
					+ frmFinYear
					+ "' and f.tofinyear='"
					+ toFinYear
					+ "' and f.freezeflag='Y' ";
			rs = st.executeQuery(finYearFreezOrNot);
			if (rs.next()) {

				sqlQuery = " select  pers.*,    (nvl(res2.res2totsum, 0.00) - nvl(res1.res1totsum, 0.00)) as DiffinPcAmnts,    trunc(((nvl(res2.res2totsum,0.00) - nvl(res1.res1totsum,0.00))*8.5)/100) as Intrst,  (nvl(res2.res2totsum, 0.00) - nvl(res1.res1totsum, 0.00))+ trunc((nvl(res2.res2totsum,0.00) - nvl(res1.res1totsum,0.00))*8.5/100) as FreezTotal,"
						+ " frzed.ObTabTot as CurrentTotal,    (nvl(res2.res2totsum, 0.00) - nvl(res1.res1totsum, 0.00))+ trunc((nvl(res2.res2totsum,0.00) - nvl(res1.res1totsum,0.00))*8.5/100) -nvl(frzed.ObTabTot,0) as DiffAmt  from   (select info.pensionno,   info.employeename,   info.desegnation,  info.dateofbirth,    info.dateofjoining,  info.dateofseperation_reason,"
						+ " info.dateofseperation_date,    info.region,  info.airportcode  from employee_personal_info info    where empflag = 'Y') pers,  (select pensionno,  frz.pensiontotal as frzPenTot,    frz.pensioninterest as frzPenIntr,    (nvl(frz.pensiontotal, 0.00) + nvl(frz.pensioninterest, 0.00)) as ObTabTot    from employee_adj_ob frz"
						+ " where adjobyear = '"
						+ frmFinYear
						+ "' and frz.manuals = 'N') frzed, (select pensionno,   ob1 .pensiontotal as prsntPenTot,     ob1 .pensioninterest as prsntPenIntrst,               (nvl(ob1.pensiontotal, 0.00) + nvl(ob1.pensioninterest, 0.00)) as res1totsum      from employee_adj_ob_freeze ob1 "
						+ " where adjobyear = '"
						+ adjOBYear
						+ "'  and ob1.manuals = 'N') res1,  (select pensionno,     ob2.pensiontotal as prsntPenTot,   ob2.pensioninterest as prsntPenIntrst, (nvl(ob2.pensiontotal, 0.00) + nvl(ob2.pensioninterest, 0.00)) as res2totsum  from employee_adj_ob_freeze ob2  where adjobyear = '"
						+ frmFinYear
						+ "'   and ob2.manuals = 'N') res2  where res1.pensionno = res2.pensionno"
						+ " and res2.pensionno = frzed.pensionno   and  frzed.pensionno  = pers.pensionno  and (nvl(res2.res2totsum, 0.00) - nvl(res1.res1totsum, 0.00))+ trunc((nvl(res2.res2totsum,0.00) - nvl(res1.res1totsum,0.00))*8.5/100) !=  frzed.ObTabTot order by res1.pensionno   ";

			} else {
				sqlQuery = "select pers.*,   round((nvl(pctotal.pctotsum, 0.00)-(nvl(frzee.frzsum, 0.00)))*POWER((1+8.5/100),2),0) as FreezTotal, nvl(OBPCTOT,0.00) as CurrentTotal,  round((nvl(pctotal.pctotsum, 0.00 ) - nvl(frzee.frzsum, 0.00))*POWER((1+8.5/100),2),0) - nvl(OBPCTOT,0.00) as DiffAmt"
						+ " from (select info.pensionno,info.employeename, info.desegnation,  info.dateofbirth, info.dateofjoining,  info.dateofseperation_reason,  info.dateofseperation_date,   info.region,  info.airportcode  from employee_personal_info info    where empflag = 'Y') pers,"
						+ " (select pensionno, pc.pensiontotal as prsntPenTot,  pc.pensioninterest as prsntPenIntrst, (nvl(pc.pensiontotal, 0.00) + nvl(pc.pensioninterest, 0.00)) as pctotsum   from epis_pc_totals pc) pctotal,"
						+ " (select pensionno,  frz.pensiontotal as frzPenTot,   frz.pensioninterest as frzPenIntr, (nvl(frz.pensiontotal, 0.00) + nvl(frz.pensioninterest, 0.00)) as frzsum   from employee_adj_ob_freeze frz   where adjobyear = '"
						+ adjOBYear
						+ "'  and frz.manuals = 'N') frzee,"
						+ " (select pensionno,  ob.pensiontotal as ObPenTot, ob.pensioninterest as ObPenIntr, (nvl(ob.pensiontotal, 0.00) + nvl(ob.pensioninterest, 0.00)) as  OBPCTOT     from employee_adj_ob  ob where adjobyear = '"
						+ frmFinYear
						+ "'  and ob.manuals = 'N') adjob  where frzee.frzsum != pctotal.pctotsum  and frzee.pensionno = pctotal.pensionno and  pctotal.pensionno = adjob.pensionno"
						+ " and adjob.pensionno = pers.pensionno and  round((nvl(pctotal.pctotsum, 0.00 ) - nvl(frzee.frzsum, 0.00))*POWER((1+8.5/100),2),0) - nvl(OBPCTOT,0.00) !=0 order by pctotal.pensionno";

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, null);
		}
		log
				.info("FinancialReportDAO::buildQueryForm2ASummaryReport Leaving Method");
		return sqlQuery;
	}

	// By Radha on 01-Oct-2011 for All regions Qry buliding properly
	// New Method
	public ArrayList getForm8SummaryReport(Connection con, String year,
			String empStatus) {

		ArrayList arrearslist = new ArrayList();

		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		String region = "", airportcode = "---", sqlQueryAllRegions = "", sqlQueryCHQIAD = "", selectedYear = "", toSelectYear = "", fromYear = "", toYear = "", condition = "";
		String years[] = null;
		double totadvanceAmt = 0.0, totEmpLoanAmt = 0.0, totAAILoanAmt = 0.0, totLoanAmt = 0.0, totFinEmpAmt = 0.0, totFinAAIAmt = 0.0, totPensoinContri = 0.0, totFinalsettlmentAmt = 0.0;
		double totadvanceAmt_chq = 0.0, totEmpLoanAmt_chq = 0.0, totAAILoanAmt_chq = 0.0, totLoanAmt_chq = 0.0, totFinEmpAmt_chq = 0.0, totFinAAIAmt_chq = 0.0, totPensoinContri_chq = 0.0, totFinalsettlmentAmt_chq = 0.0;
		double totalSubscription = 0.0, totalContribution = 0.0, totalGrandSubscription = 0.0, totalGrandContribution = 0.0;
		double grandTotadvanceAmt_all = 0.0, grandTotEmpLoanAmt_all = 0.0, grandTotAAILoanAmt_all = 0.0, grandTotLoanAmt_all = 0.0, grandTotFinEmpAmt_all = 0.0, grandTotFinAAIAmt_all = 0.0, grandTotPensoinContri_all = 0.0, grandtotFinalsettlmentAmt_all = 0.0;
		double grandTotadvanceAmt_chq = 0.0, grandTotEmpLoanAmt_chq = 0.0, grandTotAAILoanAmt_chq = 0.0, grandTotLoanAmt_chq = 0.0, grandTotFinEmpAmt_chq = 0.0, grandTotFinAAIAmt_chq = 0.0, grandTotPensoinContri_chq = 0.0, grandtotFinalsettlmentAmt_chq = 0.0;
		double grandTotadvanceAmt = 0.0, grandTotEmpLoanAmt = 0.0, grandTotAAILoanAmt = 0.0, grandTotLoanAmt = 0.0, grandTotFinEmpAmt = 0.0, grandTotFinAAIAmt = 0.0, grandTotPensoinContri = 0.0, grandtotFinalsettlmentAmt = 0.0;
		AaiEpfForm11Bean form8summaryInfo_All = null;
		AaiEpfForm11Bean form8summaryInfo_Chqiad = null;
		AaiEpfForm11Bean form8summaryInfo_GrndTotals = new AaiEpfForm11Bean();
		DecimalFormat df = new DecimalFormat("#########0");
		ArrayList form8SummaryList = new ArrayList();
		ArrayList form8SummaryList_all = new ArrayList();
		ArrayList form8SummaryList_chq = new ArrayList();
		ArrayList form8SummaryList_grndtots = new ArrayList();
		years = year.split("-");
		fromYear = "01-Apr-" + years[0];
		toYear = "31-Mar-" + years[1];

		if (!empStatus.equals("")) {
			condition = " and vemp.status = '" + empStatus + "' ";
		}

		/*
		 * sqlQueryAllRegions= "select advances.region as region,
		 * advances.advamount as advamount, loans.emploanamount as
		 * emploanamount, loans.aailoanamount as aailoanamount,
		 * finalsettlment.finempamount as finempamount,
		 * finalsettlment.finaaiamount as finaaiamount,
		 * finalsettlment.penconamount as penconamount" +" from (select
		 * sum(nvl(adv.amount, 0.0)) as advamount, adv.region from
		 * employee_pension_advances adv, v_emp_personal_info vemp where
		 * adv.pensionno is not null and adv.pensionno = vemp.pensionno and
		 * vemp.finyear = '"+year+"' "+condition+" and adv.ADVTRANSDATE between
		 * '"+fromYear+"' and '"+toYear+"' and adv.region is not null and
		 * adv.region != 'CHQIAD' group by adv.region) advances," +" (select
		 * sum(nvl(loan.sub_amt, 0.0)) as emploanamount, sum(nvl(loan.cont_amt,
		 * 0.0)) as aailoanamount,loan.region from employee_pension_loans loan,
		 * v_emp_personal_info vemp where loan.pensionno is not null and
		 * loan.pensionno = vemp.pensionno and vemp.finyear = '"+year+"'
		 * "+condition+" and loan.LOANDATE between '"+fromYear+"' and
		 * '"+toYear+"' and loan.region is not null and loan.region != 'CHQIAD'
		 * group by loan.region) loans," +" (select sum(nvl(finalstlmnt.finemp,
		 * 0.0)) as finempamount, sum(nvl(finalstlmnt.finaai, 0.0)) as
		 * finaaiamount, sum(nvl(finalstlmnt.pencon, 0.0)) as penconamount,
		 * finalstlmnt.region from employee_pension_finsettlement finalstlmnt,
		 * v_emp_personal_info vemp where finalstlmnt.pensionno is not null and
		 * finalstlmnt.pensionno = vemp.pensionno and vemp.finyear = '"+year+"'
		 * "+condition+" and finalstlmnt.settlementdate between
		 * '"+fromYear+"'and '"+toYear+"' and finalstlmnt.region is not null and
		 * finalstlmnt.region != 'CHQIAD'" +" group by finalstlmnt.region)
		 * finalsettlment where finalsettlment.region = loans.region and
		 * loans.region = advances.region order by advances.region ";
		 * 
		 */
		sqlQueryAllRegions = " select advancesData.region as region,  nvl(advancesData.advamount, 0.0) as advamount, nvl(loansData.emploanamount, 0.0) as emploanamount,  nvl(loansData.aailoanamount, 0.0) as aailoanamount,  nvl(finalsettlment.finempamount, 0.0) as finempamount,  nvl(finalsettlment.finaaiamount, 0.0) as finaaiamount, nvl(finalsettlment.penconamount, 0.0) as penconamount "
				+ " from (select nvl(advan.advamount,0.00) as advamount,  units.region   from (select sum(nvl(adv.amount, 0.0)) as advamount, adv.region   from employee_pension_advances adv, v_emp_personal_info       vemp where adv.ADVTRANSDATE between '"
				+ fromYear
				+ "' and   '"
				+ toYear
				+ "'  and adv.pensionno is not null    and adv.pensionno = vemp.pensionno"
				+ " and vemp.finyear = '"
				+ year
				+ "'    "
				+ condition
				+ "    and adv.region is not null      and adv.region! = 'CHQIAD'   group by adv.region) advan,  (select distinct(region)       from employee_unit_master    where region! = 'CHQIAD') units     where units.region = advan.region(+)) advancesData,"
				+ " (select  nvl(loans.emploanamount,0.00) as emploanamount,   nvl(loans.aailoanamount,0.00) as aailoanamount,      units.region   from (select sum(nvl(loan.sub_amt, 0.0)) as emploanamount,  sum(nvl(loan.cont_amt, 0.0)) as aailoanamount,    loan.region    from employee_pension_loans loan, v_emp_personal_info vemp    where loan.LOANDATE between '"
				+ fromYear
				+ "' and '"
				+ toYear
				+ "' "
				+ " and loan.pensionno is not null    and loan.pensionno = vemp.pensionno         and vemp.finyear = '"
				+ year
				+ "'    "
				+ condition
				+ "   and loan.region is not null    and loan.region! = 'CHQIAD'   group by loan.region) loans,  (select distinct(region)   from employee_unit_master  where region! = 'CHQIAD') units  where units.region = loans.region(+)) loansData,"
				+ " (select nvl(finalsettlment.finempamount,0.00) as finempamount, nvl(finalsettlment.finaaiamount,0.00) as finaaiamount,  nvl(finalsettlment.penconamount,0.00) as penconamount,  units.region  from (select    sum(nvl(finalstlmnt.finemp, 0.0)) as finempamount,     sum(nvl(finalstlmnt.finaai, 0.0)) as finaaiamount,  sum(nvl(finalstlmnt.pencon, 0.0)) as penconamount,    finalstlmnt.region"
				+ " from employee_pension_finsettlement finalstlmnt,    v_emp_personal_info  vemp where finalstlmnt.settlementdate between '"
				+ fromYear
				+ "'  and   '"
				+ toYear
				+ "'  and finalstlmnt.pensionno is not null and finalstlmnt.pensionno = vemp.pensionno  and vemp.finyear = '"
				+ year
				+ "'    "
				+ condition
				+ "    and finalstlmnt.region is not null      and finalstlmnt.region != 'CHQIAD'   group by finalstlmnt.region) finalsettlment,"
				+ " (select distinct(region) from employee_unit_master  where region != 'CHQIAD') units  where units.region = finalsettlment.region(+)) finalsettlment where     loansData.region = advancesData.region    and advancesData.region  = finalsettlment.region  ";

		sqlQueryCHQIAD = "select advancesData.region as region,     advancesData.airportcode,       nvl(advancesData.advamount, 0.0) as advamount,  nvl(loansData.emploanamount, 0.0) as emploanamount,nvl(loansData.aailoanamount, 0.0) as aailoanamount,   nvl(finalsettlment.finempamount, 0.0) as finempamount, nvl(finalsettlment.finaaiamount, 0.0) as finaaiamount,   nvl(finalsettlment.penconamount, 0.0) as penconamount,  loansData.airportcode,   finalsettlment.airportcode  from (select advan.advamount, units.unitname as airportcode, advan.region"
				+ " from (select sum(nvl(adv.amount, 0.0)) as advamount, adv.region,  adv.airportcode     from employee_pension_advances adv , v_emp_personal_info vemp  where adv.ADVTRANSDATE between '"
				+ fromYear
				+ "' and   '"
				+ toYear
				+ "'  and adv.pensionno is not null and adv.pensionno= vemp.pensionno and vemp.finyear='"
				+ year
				+ "'      "
				+ condition
				+ "    and adv.region is not null   and adv.region = 'CHQIAD'  group by adv.region, adv.airportcode) advan,    (select unitname, region   from employee_unit_master      where region = 'CHQIAD') units   where units.unitname = advan.airportcode(+)) advancesData,"
				+ " (select loans.emploanamount,loans.aailoanamount, units.unitname as airportcode, loans.region from (select sum(nvl(loan.sub_amt, 0.0)) as emploanamount,            sum(nvl(loan.cont_amt, 0.0)) as aailoanamount,  loan.region,loan.airportcode from employee_pension_loans loan ,   v_emp_personal_info vemp       where loan.LOANDATE between '"
				+ fromYear
				+ "' and '"
				+ toYear
				+ "'   and loan.pensionno is not null and loan.pensionno= vemp.pensionno and vemp.finyear='"
				+ year
				+ "'      "
				+ condition
				+ "        and loan.region is not null  and loan.region = 'CHQIAD'   group by loan.region, loan.airportcode) loans, (select unitname, region"
				+ "        from employee_unit_master where region = 'CHQIAD') units  where units.unitname = loans.airportcode(+)) loansData,       (select finalsettlment.finempamount,  finalsettlment.finaaiamount,     finalsettlment.penconamount,finalsettlment.region,      units.unitname as airportcode from (select finalstlmnt.airportcode,sum(nvl(finalstlmnt.finemp, 0.0)) as finempamount,   sum(nvl(finalstlmnt.finaai, 0.0)) as finaaiamount,   sum(nvl(finalstlmnt.pencon, 0.0)) as penconamount,      finalstlmnt.region   from employee_pension_finsettlement finalstlmnt, v_emp_personal_info vemp   where finalstlmnt.settlementdate between '"
				+ fromYear
				+ "' and   '"
				+ toYear
				+ "'      and finalstlmnt.pensionno is not null and finalstlmnt.pensionno= vemp.pensionno and vemp.finyear='"
				+ year
				+ "'   "
				+ condition
				+ "  "
				+ "        and finalstlmnt.region is not null   and finalstlmnt.region = 'CHQIAD'    group by finalstlmnt.region, finalstlmnt.airportcode) finalsettlment,  (select unitname, region   from employee_unit_master   where region = 'CHQIAD') units where units.unitname = finalsettlment.airportcode(+)) finalsettlment,  employee_unit_master mstr where mstr.region = 'CHQIAD'   and mstr.unitname = loansData.airportcode(+)   and mstr.unitname = advancesData.airportcode(+) and mstr.unitname = finalsettlment.airportcode(+) ";

		try {

			st = con.createStatement();

			log
					.info("FinancialReportDAO::getForm8SummaryReport()--sqlQueryAllRegions "
							+ sqlQueryAllRegions);
			rs = st.executeQuery(sqlQueryAllRegions);

			while (rs.next()) {
				form8summaryInfo_All = new AaiEpfForm11Bean();
				region = rs.getString("region");

				if (rs.getString("advamount") != null) {
					totadvanceAmt = Double.parseDouble(rs
							.getString("advamount"));
				} else {
					totadvanceAmt = 0.0;
				}
				if (rs.getString("emploanamount") != null) {
					totEmpLoanAmt = Double.parseDouble(rs
							.getString("emploanamount"));
				} else {
					totEmpLoanAmt = 0.0;
				}
				if (rs.getString("aailoanamount") != null) {
					totAAILoanAmt = Double.parseDouble(rs
							.getString("aailoanamount"));
				} else {
					totAAILoanAmt = 0.0;
				}

				if (rs.getString("finempamount") != null) {
					totFinEmpAmt = Double.parseDouble(rs
							.getString("finempamount"));
				} else {
					totFinEmpAmt = 0.0;
				}
				if (rs.getString("finaaiamount") != null) {
					totFinAAIAmt = Double.parseDouble(rs
							.getString("finaaiamount"));
				} else {
					totFinAAIAmt = 0.0;
				}
				if (rs.getString("penconamount") != null) {
					totPensoinContri = Double.parseDouble(rs
							.getString("penconamount"));
				} else {
					totPensoinContri = 0.0;
				}

				totLoanAmt = totEmpLoanAmt + totAAILoanAmt;
				totFinalsettlmentAmt = totFinEmpAmt + totFinAAIAmt
						- totPensoinContri;

				grandTotadvanceAmt_all = grandTotadvanceAmt_all + totadvanceAmt;
				grandTotEmpLoanAmt_all = grandTotEmpLoanAmt_all + totEmpLoanAmt;
				grandTotAAILoanAmt_all = grandTotAAILoanAmt_all + totAAILoanAmt;

				grandTotFinEmpAmt_all = grandTotFinEmpAmt_all + totFinEmpAmt;
				grandTotFinAAIAmt_all = grandTotFinAAIAmt_all + totFinAAIAmt;
				grandTotPensoinContri_all = grandTotPensoinContri_all
						+ totPensoinContri;

				grandTotLoanAmt_all = grandTotLoanAmt_all + totLoanAmt;
				grandtotFinalsettlmentAmt_all = grandtotFinalsettlmentAmt_all
						+ totFinalsettlmentAmt;

				form8summaryInfo_All.setRegion(region);
				form8summaryInfo_All.setAirportCode(airportcode);

				form8summaryInfo_All.setAdvanceAmnt(df.format(Math
						.round(totadvanceAmt)));
				form8summaryInfo_All.setTotLoanAmt(df.format(Math
						.round(totLoanAmt)));
				form8summaryInfo_All.setTotFinalsettlmentAmt(df.format(Math
						.round(totFinalsettlmentAmt)));

				form8summaryInfo_All.setPfwSubscr(df.format(Math
						.round(totEmpLoanAmt)));
				form8summaryInfo_All.setPfwContr(df.format(Math
						.round(totAAILoanAmt)));
				form8summaryInfo_All.setFinalEmpSubscr(df.format(Math
						.round(totFinEmpAmt)));
				form8summaryInfo_All.setFinalAAIContr(df.format(Math
						.round(totFinAAIAmt)));
				form8summaryInfo_All.setPensinonContr(df.format(Math
						.round(totPensoinContri)));

				form8SummaryList_all.add(form8summaryInfo_All);

			}
			log
					.info("FinancialReportDAO::getForm8SummaryReport()--sqlQueryCHQIAD "
							+ sqlQueryCHQIAD);
			st1 = con.createStatement();
			rs1 = st1.executeQuery(sqlQueryCHQIAD);

			while (rs1.next()) {
				form8summaryInfo_Chqiad = new AaiEpfForm11Bean();

				region = rs1.getString("region");
				airportcode = rs1.getString("airportcode");

				if (rs1.getString("advamount") != null) {
					totadvanceAmt_chq = Double.parseDouble(rs1
							.getString("advamount"));
				} else {
					totadvanceAmt_chq = 0.0;
				}
				if (rs1.getString("emploanamount") != null) {
					totEmpLoanAmt_chq = Double.parseDouble(rs1
							.getString("emploanamount"));
				} else {
					totEmpLoanAmt_chq = 0.0;
				}
				if (rs1.getString("aailoanamount") != null) {
					totAAILoanAmt_chq = Double.parseDouble(rs1
							.getString("aailoanamount"));
				} else {
					totAAILoanAmt_chq = 0.0;
				}

				if (rs1.getString("finempamount") != null) {
					totFinEmpAmt_chq = Double.parseDouble(rs1
							.getString("finempamount"));
				} else {
					totFinEmpAmt_chq = 0.0;
				}
				if (rs1.getString("finaaiamount") != null) {
					totFinAAIAmt_chq = Double.parseDouble(rs1
							.getString("finaaiamount"));
				} else {
					totFinAAIAmt_chq = 0.0;
				}
				if (rs1.getString("penconamount") != null) {
					totPensoinContri_chq = Double.parseDouble(rs1
							.getString("penconamount"));
				} else {
					totPensoinContri_chq = 0.0;
				}

				totLoanAmt_chq = totEmpLoanAmt_chq + totAAILoanAmt_chq;
				totFinalsettlmentAmt_chq = totFinEmpAmt_chq + totFinAAIAmt_chq
						- totPensoinContri_chq;

				grandTotadvanceAmt_chq = grandTotadvanceAmt_chq
						+ totadvanceAmt_chq;

				grandTotEmpLoanAmt_chq = grandTotEmpLoanAmt_chq
						+ totEmpLoanAmt_chq;
				grandTotAAILoanAmt_chq = grandTotAAILoanAmt_chq
						+ totAAILoanAmt_chq;

				grandTotFinEmpAmt_chq = grandTotFinEmpAmt_chq
						+ totFinEmpAmt_chq;
				grandTotFinAAIAmt_chq = grandTotFinAAIAmt_chq
						+ totFinAAIAmt_chq;
				grandTotPensoinContri_chq = grandTotPensoinContri_chq
						+ totPensoinContri_chq;

				grandTotLoanAmt_chq = grandTotLoanAmt_chq + totLoanAmt_chq;
				grandtotFinalsettlmentAmt_chq = grandtotFinalsettlmentAmt_chq
						+ totFinalsettlmentAmt_chq;

				form8summaryInfo_Chqiad.setRegion(region);
				form8summaryInfo_Chqiad.setAirportCode(airportcode);

				form8summaryInfo_Chqiad.setAdvanceAmnt(df.format(Math
						.round(totadvanceAmt_chq)));
				form8summaryInfo_Chqiad.setTotLoanAmt(df.format(Math
						.round(totLoanAmt_chq)));

				form8summaryInfo_Chqiad.setPfwSubscr(df.format(Math
						.round(totEmpLoanAmt_chq)));
				form8summaryInfo_Chqiad.setPfwContr(df.format(Math
						.round(totAAILoanAmt_chq)));
				form8summaryInfo_Chqiad.setFinalEmpSubscr(df.format(Math
						.round(totFinEmpAmt_chq)));
				form8summaryInfo_Chqiad.setFinalAAIContr(df.format(Math
						.round(totFinAAIAmt_chq)));
				form8summaryInfo_Chqiad.setPensinonContr(df.format(Math
						.round(totPensoinContri_chq)));
				form8summaryInfo_Chqiad.setTotFinalsettlmentAmt(df.format(Math
						.round(totFinalsettlmentAmt_chq)));

				form8SummaryList_chq.add(form8summaryInfo_Chqiad);

			}
			grandTotadvanceAmt = grandTotadvanceAmt_all
					+ grandTotadvanceAmt_chq;
			grandTotEmpLoanAmt = grandTotEmpLoanAmt_all
					+ grandTotEmpLoanAmt_chq;
			grandTotAAILoanAmt = grandTotAAILoanAmt_all
					+ grandTotAAILoanAmt_chq;
			grandTotFinEmpAmt = grandTotFinEmpAmt_all + grandTotFinEmpAmt_chq;
			grandTotFinAAIAmt = grandTotFinAAIAmt_all + grandTotFinAAIAmt_chq;
			grandTotPensoinContri = grandTotPensoinContri_all
					+ grandTotPensoinContri_chq;
			grandTotLoanAmt = grandTotLoanAmt_all + grandTotLoanAmt_chq;
			grandtotFinalsettlmentAmt = grandtotFinalsettlmentAmt_all
					+ grandtotFinalsettlmentAmt_chq;

			log.info("----grandTotadvanceAmt_all--" + grandTotadvanceAmt_all
					+ "---grandTotadvanceAmt_chq---" + grandTotadvanceAmt_chq);

			form8summaryInfo_GrndTotals.setGrandTotadvanceAmt(df.format(Math
					.round(grandTotadvanceAmt)));
			form8summaryInfo_GrndTotals.setGrandTotEmpLoanAmt(df.format(Math
					.round(grandTotEmpLoanAmt)));
			form8summaryInfo_GrndTotals.setGrandTotAAILoanAmt(df.format(Math
					.round(grandTotAAILoanAmt)));
			form8summaryInfo_GrndTotals.setGrandTotFinEmpAmt(df.format(Math
					.round(grandTotFinEmpAmt)));
			form8summaryInfo_GrndTotals.setGrandTotFinAAIAmt(df.format(Math
					.round(grandTotFinAAIAmt)));
			form8summaryInfo_GrndTotals.setGrandTotPensoinContri(df.format(Math
					.round(grandTotPensoinContri)));
			form8summaryInfo_GrndTotals.setGrandTotLoanAmt(df.format(Math
					.round(grandTotLoanAmt)));
			form8summaryInfo_GrndTotals.setGrandtotFinalsettlmentAmt(df
					.format(Math.round(grandtotFinalsettlmentAmt)));

			form8SummaryList_grndtots.add(form8summaryInfo_GrndTotals);

			form8SummaryList.add(form8SummaryList_all);
			form8SummaryList.add(form8SummaryList_chq);
			form8SummaryList.add(form8SummaryList_grndtots);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, null);
		}
		return form8SummaryList;
	}

	// New Method
	public ArrayList getClosingAndOpeningBalancesSummary(Connection con,
			String finyear, String empStatus) {

		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		String region = "", sqlQueryform1 = "---", sqlQueryform11 = "", fromYear_form1 = "", toYear_form1 = "";
		String years[] = null;
		String subscriptionAmt = "", contributionAmt = "", condition = "", condition1 = "";
		double EMPClosingBal = 0.0, AAIClosingBal = 0.0, totEMPClosingBal = 0.0, totAAIClosingBal = 0.0;
		int fromYear = 0, toYear = 0;

		AaiEpfForm11Bean cbobInfo = new AaiEpfForm11Bean();

		DecimalFormat df = new DecimalFormat("#########0");
		ArrayList cboblist = new ArrayList();

		years = finyear.split("-");
		fromYear = Integer.parseInt(years[0]) + 1;
		toYear = Integer.parseInt(years[1]) + 1;

		fromYear_form1 = "01-Apr-" + fromYear;
		toYear_form1 = "31-Mar-" + toYear;

		if (!empStatus.equals("")) {
			condition = " AND   empactsts ='" + empStatus + "' ";
			condition1 = " AND   V.STATUS ='" + empStatus + "' ";
		}
		sqlQueryform1 = "select  sum(nvl(EPO.EMPNETOB,0.0)) AS SUBSCRIPTIONAMT,  sum(nvl(EPO.AAINETOB,0.0)) AS CONTRIBUTIONAMT  FROM"
				+ " employee_personal_info EPI, employee_pension_ob EPO,  v_emp_personal_info   v WHERE EPI.PENSIONNO = EPO.PENSIONNO AND EPO.PENSIONNO=v.pensionno AND V.FINYEAR='"
				+ finyear
				+ "' "
				+ condition1
				+ "   AND  EPO.OBSEPERATIONFLAG = 'Y' AND  EPO.NEWJOINEEREMARKS = 'N' AND EPO.EMPFLAG = 'Y' and  EPO.OBYEAR BETWEEN '"
				+ fromYear_form1
				+ "' AND last_day('"
				+ toYear_form1
				+ "')"
				+ " ORDER BY EPI.pensionno ASC ";
		sqlQueryform11 = "SELECT   (NVL(EMPNETOB, 0.00) +  NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) +  NVL(TOTAL, 0.00) - NVL(REFADVPFWNRW, 0.00) +   NVL(EMPINTERSTCR, 0.00) - "
				+ " NVL(FINEMP, 0.00) + NVL(NETCLOSINGEMPNET, 0.00)) AS CLBAL,"
				+ "(NVL(AAINETOB, '0.00') - NVL((ADJPENSIONTOTAL + ADJPENSIONINTEREST), 0.00) +   NVL(PF, 0.00) - NVL(CONT_AMT, 0.00) +"
				+ " NVL(AAIINTERSTCR, 0.00) - NVL(FINAAI, 0.00) +    NVL(NETCLOSINGAAINET, 0.00)) AS AAICLBL"
				+ " FROM V_EMP_PENSION_YEAR_TOTALS  WHERE FINYEAR = '"
				+ finyear + "'   " + condition + "     ORDER BY PENSIONNO";
		try {

			st = con.createStatement();
			log
					.info("FinancialReportDAO::getClosingAndOpeningBalancesSummary()--sqlQueryform1 "
							+ sqlQueryform1);
			rs = st.executeQuery(sqlQueryform1);

			while (rs.next()) {

				if (rs.getString("SUBSCRIPTIONAMT") != null) {
					subscriptionAmt = rs.getString("SUBSCRIPTIONAMT");
				} else {
					subscriptionAmt = "0.00";
				}
				if (rs.getString("CONTRIBUTIONAMT") != null) {
					contributionAmt = rs.getString("CONTRIBUTIONAMT");
				} else {
					contributionAmt = "0.00";
				}

			}
			log
					.info("FinancialReportDAO::getClosingAndOpeningBalancesSummary()--sqlQueryform11 "
							+ sqlQueryform11);
			st1 = con.createStatement();
			rs1 = st1.executeQuery(sqlQueryform11);

			while (rs1.next()) {

				if (rs1.getString("CLBAL") != null) {
					EMPClosingBal = Double.parseDouble(rs1.getString("CLBAL"));
				} else {
					EMPClosingBal = 0.0;
				}

				if (rs1.getString("AAICLBL") != null) {
					AAIClosingBal = Double
							.parseDouble(rs1.getString("AAICLBL"));
				} else {
					AAIClosingBal = 0.0;
				}
				totEMPClosingBal = totEMPClosingBal + EMPClosingBal;
				totAAIClosingBal = totAAIClosingBal + AAIClosingBal;

			}

			cbobInfo.setSubscriptionAmt(subscriptionAmt);
			cbobInfo.setContributionamt(contributionAmt);
			cbobInfo.setTotAAIClosingBalance(df.format(Math
					.round(totAAIClosingBal)));
			cbobInfo.setTotEmpClosingBalance(df.format(Math
					.round(totEMPClosingBal)));

			cboblist.add(cbobInfo);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return cboblist;
	}

	// new method
	public ArrayList getControlAccountClosingBalancesSummary(Connection con,
			String finyear, String empStatus) {

		ArrayList arrearslist = new ArrayList();
		Statement st = null;
		ResultSet rs = null;

		String region = "", sqlQueryform1 = "---", controlAccSqlQuery = "", fromYear_form1 = "", toYear_form1 = "";
		String years[] = null;
		String subscriptionAmt = "", contributionAmt = "", condition = "";
		double totEMPClosingBal = 0.0, totAAIClosingBal = 0.0;
		double empNetOB = 0.0, AAINetOB = 0.0, empAdjOB = 0.0, AAIAdjOB = 0.0, empSubTotal = 0.0, emplrPF = 0.0, empPFWTotal = 0.0, AAIPFWTotal = 0.0, empSubIntrst = 0.0, AAIContriIntrst = 0.0, FinalPaymentSub = 0.0, FinalPaymentContr = 0.0, EMPClosingBal = 0.0, AAIClosingBal = 0.0;
		double empNetOBTot = 0.0, AAINetOBTot = 0.0, empAdjOBTot = 0.0, AAIAdjOBTot = 0.0, empSubTotalTot = 0.0, emplrPFTot = 0.0, empPFWTotalTot = 0.0, AAIPFWTotalTot = 0.0, empSubIntrstTot = 0.0, AAIContriIntrstTot = 0.0, FinalPaymentSubTot = 0.0, FinalPaymentContrTot = 0.0, EMPClosingBalTot = 0.0, AAIClosingBaltot = 0.0;

		int fromYear = 0, toYear = 0;

		AaiEpfForm11Bean form1Bean = null;
		AaiEpfForm11Bean form11Bean = null;
		AaiEpfForm11Bean summaryInfo = new AaiEpfForm11Bean();
		DecimalFormat df = new DecimalFormat("#########0");
		ArrayList list = new ArrayList();
		years = finyear.split("-");
		fromYear = Integer.parseInt(years[0]) + 1;
		toYear = Integer.parseInt(years[1]) + 1;

		fromYear_form1 = "01-Apr-" + fromYear;
		toYear_form1 = "31-Mar-" + toYear;

		if (!empStatus.equals("")) {
			condition = " AND   empactsts ='" + empStatus + "'";
		}
		if (fromYear <= 2009) {
			controlAccSqlQuery = "SELECT  EMPNETOB as EMPNETOB, AAINETOB as AAINETOB,"
					+ " NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) AS ADJEMPSUBTOTAL,  NVL((ADJPENSIONTOTAL + ADJPENSIONINTEREST), 0.00) AS ADJAAICONTR,"
					+ " TOTAL as EmpSubTotal, AAITOTAL as EmplrPFTotal, REFADVPFWNRW as ADVANCESPFWNRFWSUB ,  CONT_AMT as PFWNRFWCONTRI,"
					+ " (EMPINTERSTCR + NVL(NETCLOSINGEMPNET, 0.00)) AS  EmpSubInt, (NVL(NETCLOSINGAAINET, 0.00) + AAIINTERSTCR) AS EmplrContriInt ,"
					+ " NVL(FINEMP, 0.00) AS FinalPaymentSub,  NVL(FINAAI, 0.00) AS FinalPaymentContri,"
					+ " (NVL(EMPNETOB, 0.00) +  NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) +  NVL(TOTAL, 0.00) - NVL(REFADVPFWNRW, 0.00) +   NVL(EMPINTERSTCR, 0.00) - "
					+ " NVL(FINEMP, 0.00) + NVL(NETCLOSINGEMPNET, 0.00)) AS EMPCLBAL,"
					+ "(NVL(AAINETOB, '0.00') - NVL((ADJPENSIONTOTAL + ADJPENSIONINTEREST), 0.00) +   NVL(PF, 0.00) - NVL(CONT_AMT, 0.00) +"
					+ " NVL(AAIINTERSTCR, 0.00) - NVL(FINAAI, 0.00) +    NVL(NETCLOSINGAAINET, 0.00)) AS AAICLBL"
					+ " FROM V_EMP_PENSION_YEAR_TOTALS  WHERE FINYEAR = '"
					+ finyear + "' " + condition + "   ORDER BY PENSIONNO";
		} else {

			controlAccSqlQuery = "SELECT  EMPNETOB as EMPNETOB, AAINETOB as AAINETOB,"
					+ " NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) AS ADJEMPSUBTOTAL,   NVL((-1*(nvl(ADJPENSIONTOTAL,0.00)) + -1*(nvl(ADJPENSIONINTEREST,0.00))), 0.00) AS ADJAAICONTR,"
					+ " TOTAL as EmpSubTotal, AAITOTAL as EmplrPFTotal, REFADVPFWNRW as ADVANCESPFWNRFWSUB ,  CONT_AMT as PFWNRFWCONTRI,"
					+ " (EMPINTERSTCR + NVL(NETCLOSINGEMPNET, 0.00)) AS  EmpSubInt, (NVL(NETCLOSINGAAINET, 0.00) + AAIINTERSTCR) AS EmplrContriInt ,"
					+ " NVL(FINEMP, 0.00) AS FinalPaymentSub,  NVL(FINAAI, 0.00) AS FinalPaymentContri,"
					+ " (NVL(EMPNETOB, 0.00) +  NVL((ADJEMPSUB + ADJEMPSUBINTEREST), 0.00) +  NVL(TOTAL, 0.00) - NVL(REFADVPFWNRW, 0.00) +   NVL(EMPINTERSTCR, 0.00) - "
					+ " NVL(FINEMP, 0.00) + NVL(NETCLOSINGEMPNET, 0.00)) AS EMPCLBAL,"
					+ "(NVL(AAINETOB, '0.00') - NVL((ADJPENSIONTOTAL + ADJPENSIONINTEREST), 0.00) +   NVL(PF, 0.00) - NVL(CONT_AMT, 0.00) +"
					+ " NVL(AAIINTERSTCR, 0.00) - NVL(FINAAI, 0.00) +    NVL(NETCLOSINGAAINET, 0.00)) AS AAICLBL"
					+ " FROM V_EMP_PENSION_YEAR_TOTALS  WHERE FINYEAR = '"
					+ finyear + "' " + condition + "    ORDER BY PENSIONNO";
		}

		try {

			st = con.createStatement();
			log
					.info("FinancialReportDAO::getControlAccountClosingBalancesSummary()--controlAccSqlQuery "
							+ controlAccSqlQuery);
			rs = st.executeQuery(controlAccSqlQuery);

			while (rs.next()) {

				if (rs.getString("EMPNETOB") != null) {
					empNetOB = rs.getDouble("EMPNETOB");
				} else {
					empNetOB = 0.0;
				}
				if (rs.getString("AAINETOB") != null) {
					AAINetOB = rs.getDouble("AAINETOB");
				} else {
					AAINetOB = 0.0;
				}
				if (rs.getString("ADJEMPSUBTOTAL") != null) {
					empAdjOB = rs.getDouble("ADJEMPSUBTOTAL");
				} else {
					empAdjOB = 0.0;
				}
				if (rs.getString("ADJAAICONTR") != null) {
					AAIAdjOB = rs.getDouble("ADJAAICONTR");
				} else {
					AAIAdjOB = 0.0;
				}
				if (rs.getString("EmpSubTotal") != null) {
					empSubTotal = rs.getDouble("EmpSubTotal");
				} else {
					empSubTotal = 0.0;
				}
				if (rs.getString("EmplrPFTotal") != null) {
					emplrPF = rs.getDouble("EmplrPFTotal");
				} else {
					emplrPF = 0.0;
				}
				if (rs.getString("ADVANCESPFWNRFWSUB") != null) {
					empPFWTotal = rs.getDouble("ADVANCESPFWNRFWSUB");
				} else {
					empPFWTotal = 0.0;
				}
				if (rs.getString("PFWNRFWCONTRI") != null) {
					AAIPFWTotal = rs.getDouble("PFWNRFWCONTRI");
				} else {
					AAIPFWTotal = 0.0;
				}
				if (rs.getString("EmpSubInt") != null) {
					empSubIntrst = rs.getDouble("EmpSubInt");
				} else {
					empSubIntrst = 0.0;
				}
				if (rs.getString("EmplrContriInt") != null) {
					AAIContriIntrst = rs.getDouble("EmplrContriInt");
				} else {
					AAIContriIntrst = 0.0;
				}
				if (rs.getString("FinalPaymentSub") != null) {
					FinalPaymentSub = rs.getDouble("FinalPaymentSub");
				} else {
					FinalPaymentSub = 0.0;
				}
				if (rs.getString("FinalPaymentContri") != null) {
					FinalPaymentContr = rs.getDouble("FinalPaymentContri");
				} else {
					FinalPaymentContr = 0.0;
				}
				if (rs.getString("EMPCLBAL") != null) {
					EMPClosingBal = rs.getDouble("EMPCLBAL");
				} else {
					EMPClosingBal = 0.0;
				}
				if (rs.getString("AAICLBL") != null) {
					AAIClosingBal = rs.getDouble("AAICLBL");
				} else {
					AAIClosingBal = 0.0;
				}

				empNetOBTot = empNetOBTot + empNetOB;
				AAINetOBTot = AAINetOBTot + AAINetOB;
				empAdjOBTot = empAdjOBTot + empAdjOB;
				AAIAdjOBTot = AAIAdjOBTot + AAIAdjOB;
				empSubTotalTot = empSubTotalTot + empSubTotal;
				emplrPFTot = emplrPFTot + emplrPF;
				empPFWTotalTot = empPFWTotalTot + empPFWTotal;
				AAIPFWTotalTot = AAIPFWTotalTot + AAIPFWTotal;
				empSubIntrstTot = empSubIntrstTot + empSubIntrst;
				AAIContriIntrstTot = AAIContriIntrstTot + AAIContriIntrst;
				FinalPaymentSubTot = FinalPaymentSubTot + FinalPaymentSub;
				FinalPaymentContrTot = FinalPaymentContrTot + FinalPaymentContr;
				EMPClosingBalTot = EMPClosingBalTot + EMPClosingBal;
				AAIClosingBaltot = AAIClosingBaltot + AAIClosingBal;

			}

			summaryInfo.setEmpNetOBTot(df.format(Math.round(empNetOBTot)));
			summaryInfo.setAAINetOBTot(df.format(Math.round(AAINetOBTot)));
			summaryInfo.setEmpAdjOBTot(df.format(Math.round(empAdjOBTot)));
			summaryInfo.setAAIAdjOBTot(df.format(Math.round(AAIAdjOBTot)));
			summaryInfo
					.setEmpSubTotalTot(df.format(Math.round(empSubTotalTot)));
			summaryInfo.setEmplrPFTot(df.format(Math.round(emplrPFTot)));
			summaryInfo
					.setEmpPFWTotalTot(df.format(Math.round(empPFWTotalTot)));
			summaryInfo
					.setAAIPFWTotalTot(df.format(Math.round(AAIPFWTotalTot)));
			summaryInfo.setEmpSubIntrstTot(df.format(Math
					.round(empSubIntrstTot)));
			summaryInfo.setAAIContriIntrstTot(df.format(Math
					.round(AAIContriIntrstTot)));
			summaryInfo.setFinalPaymentSubTot(df.format(Math
					.round(FinalPaymentSubTot)));
			summaryInfo.setFinalPaymentContrTot(df.format(Math
					.round(FinalPaymentContrTot)));
			summaryInfo.setEmpCLBalTot(df.format(Math.round(EMPClosingBalTot)));
			summaryInfo.setAAICLBalTot(df.format(Math.round(AAIClosingBaltot)));

			list.add(summaryInfo);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
	}

	// New Method
	public ArrayList getJustificationData(Connection con, String finyear,
			String empStatus) {
		String sqlQuery = "", dynamicQuery = "", orderBy = "";
		StringBuffer query = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();
		AaiEpfForm11Bean justificationBean = null;
		ArrayList jList = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		try {
			con = commonDB.getConnection();
			sqlQuery = "select JID as JID  ,JDESCRIPTION as JDESCRIPTION , JPATH  AS JPATH,  EMPSTATUS AS EMPSTATUS  from  epis_cntrl_justifications where FINYEAR='"
					+ finyear + "'   ";
			st = con.createStatement();
			log.info("------empStatus----------" + empStatus);
			if (!empStatus.equals("")) {
				whereClause.append(" AND ");
				whereClause.append("   (EMPSTATUS  is null or EMPSTATUS in('"
						+ empStatus + "'))");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if (empStatus.equals("")) {

			} else {

				query.append(commonDAO.sTokenFormat(whereClause));
			}
			orderBy = " ORDER BY  JID ";
			query.append(orderBy);
			dynamicQuery = query.toString();

			log.info("FinancialReportDAO::getJustificationData()--sqlQuery "
					+ dynamicQuery);
			rs = st.executeQuery(dynamicQuery);
			while (rs.next()) {
				justificationBean = new AaiEpfForm11Bean();
				if (rs.getString("JID") != null) {
					justificationBean.setJid(rs.getString("JID"));
				} else {
					justificationBean.setJid("---");
				}
				if (rs.getString("JDESCRIPTION") != null) {
					justificationBean.setJDescription(rs
							.getString("JDESCRIPTION"));
				} else {
					justificationBean.setJDescription("---");
				}
				if (rs.getString("JPATH") != null) {
					justificationBean.setJPath(rs.getString("JPATH"));
				} else {
					justificationBean.setJPath("---");
				}

				jList.add(justificationBean);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return jList;
	}

	// New method
	public ArrayList getControlAccSummaryRegionWiseReport(String finyear,
			String empStatus) {
		String selQuery = "";
		String selQuery1 = "";
		AaiEpfForm11Bean form11Bean = null;
		AaiEpfForm11Bean form11Bean1 = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		ArrayList form11DataList = new ArrayList();
		selQuery = this.buildQueryRegionWiseInfo(finyear, empStatus);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log
					.info("FinancialReportDAO::getControlAccSummaryRegionWiseReport===selQuery"
							+ selQuery);
			rs = st.executeQuery(selQuery);

			while (rs.next()) {

				form11Bean = new AaiEpfForm11Bean();
				if (rs.getString("EMPNETOB") != null) {
					form11Bean.setObEmpSub(rs.getString("EMPNETOB"));
				} else {
					form11Bean.setObEmpSub("0.00");
				}
				if (rs.getString("AAINETOB") != null) {
					form11Bean.setObAAISub(rs.getString("AAINETOB"));
				} else {
					form11Bean.setObAAISub("0.00");
				}
				if (rs.getString("REGION") != null) {
					form11Bean.setRegion(rs.getString("REGION"));
				} else {
					form11Bean.setRegion("---");
				}
				form11Bean.setRemarks("---");

				form11DataList.add(form11Bean);
			}
			log.info("form11DataList: size:" + form11DataList.size());

			selQuery1 = this.buildQueryRegionWiseInfo1(finyear, empStatus);

			st = con.createStatement();
			log
					.info("FinancialReportDAO::getControlAccSummaryRegionWiseReport===selQuery"
							+ selQuery1);
			rs = st.executeQuery(selQuery1);

			while (rs.next()) {
				form11Bean1 = new AaiEpfForm11Bean();
				if (rs.getString("EMPNETOB") != null) {
					form11Bean1.setObEmpSub(rs.getString("EMPNETOB"));
				} else {
					form11Bean1.setObEmpSub("0.00");
				}
				if (rs.getString("AAINETOB") != null) {
					form11Bean1.setObAAISub(rs.getString("AAINETOB"));
				} else {
					form11Bean1.setObAAISub("0.00");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					form11Bean1.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					form11Bean1.setAirportCode("---");
				}
				if (rs.getString("REGION") != null) {
					form11Bean1.setRegion(rs.getString("REGION"));
				} else {
					form11Bean1.setRegion("---");
				}
				form11Bean1.setRemarks("---");

				form11DataList.add(form11Bean1);

			}

			log.info("form11DataList: size:" + form11DataList.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return form11DataList;
	}

	// New method
	public String buildQueryRegionWiseInfo(String frmSelectedYear,
			String empStatus) {
		log
				.info("FinancialReportDAO::buildQueryRegionWiseInfo-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "", condition = "";

		if (!empStatus.equals("")) {
			condition = " AND   empactsts ='" + empStatus + "' ";
		}
		sqlQuery = "SELECT  VEPYT.REGION, sum(nvl(VEPYT.EMPNETOB,0.0)) AS EMPNETOB, sum(nvl(VEPYT.AAINETOB,0.0)) AS AAINETOB from V_EMP_PENSION_YEAR_TOTALS VEPYT where VEPYT.REGION!='CHQIAD' and FINYEAR = '"
				+ frmSelectedYear
				+ "' "
				+ condition
				+ "  group by VEPYT.REGION ";
		log.info("value of the query:" + sqlQuery);
		query.append(sqlQuery);
		dynamicQuery = query.toString();
		log.info("FinancialReportDAO::buildQueryRegionWiseInfo Leaving Method");
		return dynamicQuery;
	}

	// New method
	public String buildQueryRegionWiseInfo1(String frmSelectedYear,
			String empStatus) {
		log
				.info("FinancialReportDAO::buildQueryRegionWiseInfo1-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery1 = "", condition = "";

		if (!empStatus.equals("")) {
			condition = " AND   empactsts ='" + empStatus + "' ";
		}

		sqlQuery1 = "SELECT  VEPYT.REGION, VEPYT.AIRPORTCODE, sum(nvl(VEPYT.EMPNETOB,0.0)) AS EMPNETOB, sum(nvl(VEPYT.AAINETOB,0.0)) AS AAINETOB from V_EMP_PENSION_YEAR_TOTALS VEPYT where  VEPYT.airportcode is not null and  VEPYT.REGION='CHQIAD' and  FINYEAR = '"
				+ frmSelectedYear
				+ "' "
				+ condition
				+ " group by  VEPYT.REGION,VEPYT.AIRPORTCODE ";
		log.info("value of the query :" + sqlQuery1);
		query.append(sqlQuery1);
		dynamicQuery = query.toString();
		log.info("FinancialReportDAO::buildQueryRegionWiseInfo1 Leaving Method"
				+ sqlQuery1);
		return dynamicQuery;
	}

	// New Method
	public ControlAccountForm2Info getControlAccSummaryAdjReport(
			String finyear, String empstatus) {
		log.info("getControlAccSummaryAdjReport Entering Method");
		ArrayList summaryNonManualAdjList = new ArrayList();
		ArrayList summaryNonManualAdjList1 = new ArrayList();
		ArrayList summaryManualAdjList = new ArrayList();
		ArrayList summaryManualAdjList1 = new ArrayList();
		ControlAccountForm2Info formInfo = new ControlAccountForm2Info();
		String nonmanualcol = "", manualcol = "";
		nonmanualcol = ",nvl(a.pensionwopriint,0) as aaiprincipal,(nvl(a.pensiontotal,0) - nvl(a.pensionwopriint,0)) as aaiinterest,-(nvl(a.pensiontotal,0)) as aaitotal,";
		manualcol = ",nvl(a.pensiontotal,0) as aaiprincipal,nvl(a.pensioninterest,0) as aaiinterest,- (nvl(a.pensiontotal,0) + nvl(a.pensioninterest,0)) as aaitotal,";
		summaryNonManualAdjList = getAdjInformation(finyear, empstatus, "N",
				nonmanualcol);
		summaryManualAdjList = getAdjInformation(finyear, empstatus, "Y",
				manualcol);

		summaryManualAdjList1 = (ArrayList) summaryManualAdjList.get(0);

		formInfo.setManaulAaiContriGrandTotal(summaryManualAdjList.get(2)
				.toString());

		formInfo.setManaulempSubGrandTotal(summaryManualAdjList.get(1)
				.toString());
		formInfo.setManualPContriAdjTotal(summaryManualAdjList.get(3)
				.toString());
		formInfo.setManaulempSubGrandTotalNe(summaryManualAdjList.get(4)
				.toString());
		formInfo.setManaulAaiContriGrandTotalNe(summaryManualAdjList.get(5)
				.toString());
		formInfo.setManualPContriAdjTotalNe(summaryManualAdjList.get(6)
				.toString());

		summaryNonManualAdjList1 = (ArrayList) summaryNonManualAdjList.get(0);

		formInfo.setNonManaulAaiGrandTotal(summaryNonManualAdjList.get(2)
				.toString());
		formInfo.setNonManaulempSubTotal(summaryNonManualAdjList.get(1)
				.toString());
		formInfo.setNonManualPContriAdjTotal(summaryNonManualAdjList.get(3)
				.toString());
		formInfo.setNonManaulempSubTotalNe(summaryNonManualAdjList.get(4)
				.toString());
		formInfo.setNonManaulAaiGrandTotalNe(summaryNonManualAdjList.get(5)
				.toString());
		formInfo.setNonManualPContriAdjTotalNe(summaryNonManualAdjList.get(6)
				.toString());

		formInfo.setManualList(summaryManualAdjList1);
		formInfo.setNonManualList(summaryNonManualAdjList1);
		log.info("getControlAccSummaryAdjReport leaving ");
		return formInfo;

	}

	// new
	private ArrayList getAdjInformation(String finyear, String empstatus,
			String manuals, String adjaaicontricols) {
		log.info("getAdjInformation Entering " + finyear + empstatus + manuals);
		ArrayList adjlist = new ArrayList();
		String adjinfo = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfForm11Bean GrdTotinfo = null;
		String[] adjyear = finyear.split("-");
		String adjsubtot = "", aaitot = "", pconadj = "", pensionno = "", adjsubinterest = "", adjsubprincipal = "", aaiprincipal = "", aaiinterest = "";
		double adjGrdtot = 0, aaiGrdtot = 0, pconadjGrdtot = 0, adjGrdtotne = 0, aaiGrdtotne = 0, pconadjGrdtotne = 0;
		ArrayList list = new ArrayList();
		adjinfo = "select tab1.* from(select a.pensionno as pensionno,a.adjobyear as adjobyear, nvl(a.empsub,0) as adjsubprincipal,nvl(a.empsubinterest,0) as adjsubinterest,(nvl(empsub,0) + nvl(empsubinterest,0)) as ADJSUBTOT "
				+ adjaaicontricols
				+ " a.pensioncontriadj as pensioncontriadj from employee_adj_ob a where "
				+ "adjobyear = '01-APR-"
				+ adjyear[0]
				+ "' and manuals = '"
				+ manuals
				+ "') tab1,(select pensionno from v_emp_pension_year_totals b where b.empactsts='"
				+ empstatus
				+ "' and finyear='"
				+ finyear
				+ "') tab2  where tab1.pensionno=tab2.pensionno";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("qry" + adjinfo);
			rs = st.executeQuery(adjinfo);
			while (rs.next()) {

				GrdTotinfo = new AaiEpfForm11Bean();
				if (rs.getString("adjobyear") != null) {
					GrdTotinfo.setObYear(CommonUtil.getDatetoString(rs
							.getDate("adjobyear"), "dd-MMM-yyyy"));
				} else {
					GrdTotinfo.setObYear("---");
				}
				if (rs.getString("ADJSUBTOT") != null) {
					adjsubtot = rs.getString("ADJSUBTOT");
					GrdTotinfo.setAdjEmpSubTotal(adjsubtot);
				} else {
					adjsubtot = "0.0";
					GrdTotinfo.setAdjEmpSubTotal(adjsubtot);
				}
				if (rs.getString("adjsubprincipal") != null) {
					adjsubprincipal = rs.getString("adjsubprincipal");
					GrdTotinfo.setAdjEmpSub(adjsubprincipal);
				} else {
					adjsubprincipal = "0.0";
					GrdTotinfo.setAdjEmpSub(adjsubprincipal);
				}
				if (rs.getString("adjsubinterest") != null) {
					adjsubinterest = rs.getString("adjsubinterest");
					GrdTotinfo.setAdjEmpSubInterest(adjsubinterest);
				} else {
					adjsubinterest = "0.0";
					GrdTotinfo.setAdjEmpSubInterest(adjsubinterest);
				}
				if (rs.getString("pensionno") != null) {
					pensionno = rs.getString("pensionno");
					GrdTotinfo.setPensionNo(pensionno);
				} else {
					pensionno = "--";
					GrdTotinfo.setPensionNo(pensionno);
				}
				if (rs.getString("aaitotal") != null) {
					aaitot = rs.getString("aaitotal");
					GrdTotinfo.setAdjAaiContr(aaitot);
				} else {
					aaitot = "0.0";
					GrdTotinfo.setAdjAaiContr(aaitot);
				}
				if (rs.getString("aaiprincipal") != null) {
					aaiprincipal = rs.getString("aaiprincipal");
					GrdTotinfo.setAdjPension(aaiprincipal);
				} else {
					aaiprincipal = "0.0";
					GrdTotinfo.setAdjPension(aaiprincipal);
				}
				if (rs.getString("aaiinterest") != null) {
					aaiinterest = rs.getString("aaiinterest");
					GrdTotinfo.setAdjPensionInt(aaiinterest);
				} else {
					aaiinterest = "0.0";
					GrdTotinfo.setAdjPensionInt(aaiinterest);
				}
				if (rs.getString("pensioncontriadj") != null) {
					pconadj = rs.getString("pensioncontriadj");
					GrdTotinfo.setPensionContriAdj(pconadj);
				} else {
					pconadj = "0.0";
					GrdTotinfo.setPensionContriAdj(pconadj);
				}
				if (Double.parseDouble(adjsubtot) < 0) {
					adjGrdtotne = adjGrdtotne + Double.parseDouble(adjsubtot);
					GrdTotinfo.setAdjEmpSubAmtFlag("N");
				} else {
					adjGrdtot = adjGrdtot + Double.parseDouble(adjsubtot);
					GrdTotinfo.setAdjEmpSubAmtFlag("P");
				}
				if (Double.parseDouble(aaitot) < 0) {
					aaiGrdtotne = aaiGrdtotne + Double.parseDouble(aaitot);
					GrdTotinfo.setAdjAaiContrAmtFlag("N");
				} else {
					aaiGrdtot = aaiGrdtot + Double.parseDouble(aaitot);
					GrdTotinfo.setAdjAaiContrAmtFlag("P");
				}
				if (Double.parseDouble(pconadj) < 0) {
					pconadjGrdtotne = pconadjGrdtotne
							+ Double.parseDouble(pconadj);
					GrdTotinfo.setPensionContriAdjAmtFalg("N");
				} else {
					pconadjGrdtot = pconadjGrdtot + Double.parseDouble(pconadj);
					GrdTotinfo.setPensionContriAdjAmtFalg("P");
				}

				adjlist.add(GrdTotinfo);
			}

			list.add(adjlist);
			list.add(Double.toString(adjGrdtot));
			list.add(Double.toString(aaiGrdtot));
			list.add(Double.toString(pconadjGrdtot));
			list.add(Double.toString(adjGrdtotne));
			list.add(Double.toString(aaiGrdtotne));
			list.add(Double.toString(pconadjGrdtotne));

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("getAdjInformation leving ");
		return list;

	}

	public String getUploadDate(String monthyear, String pensionno,
			String paiddate) {
		log.info("EPFFormsReportDAO::getUploadDate:Entering method" + monthyear
				+ pensionno + paiddate);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String upLoadDate = "", query = "", dynamicQuery = "";
		StringBuffer selQuery = new StringBuffer();
		query = "select uploaddate from employee_suppli_log where pensionno='"
				+ pensionno + "'";
		selQuery.append(query);
		/*if(!monthyear.equals("")){
		 selQuery.append(" AND ");
		 selQuery.append(" monthyear='"+monthyear+"' ");
		 }*/
		if (!paiddate.equals("")) {
			selQuery.append(" AND ");
			selQuery.append(" paiddate='" + paiddate + "'");
		}
		dynamicQuery = selQuery.toString();
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getUploadDate===query" + dynamicQuery);
			rs = st.executeQuery(dynamicQuery);
			if (rs.next()) {
				upLoadDate = commonUtil.converDBToAppFormat(rs
						.getDate("uploaddate"));
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log
				.info("EPFFormsReportDAO::getUploadDate:Leaving method"
						+ upLoadDate);
		return upLoadDate;
	}
//	By Radha on 05-Apr-2012 for Displaying Form8Summary Details	 
	 public ArrayList epfForm8SummaryDetailsWitKeyNo(String finyear,String accounthead ){
			log.info("epfForm8SummaryDetailsWitKeyNo Entering " + finyear + accounthead);
			ArrayList adjlist = new ArrayList();
			ArrayList voucherInfo = new ArrayList();
			String adjinfo = "";
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
			ArrayList witKeyNoList = new ArrayList();			 
			AAIEPFReportBean summaryInfo = null;
			AAIEPFReportBean witKeyNodetails = new AAIEPFReportBean();
			String[] yearStrip = finyear.split("-");
			String sqlQry = "", fromYear = "", toYear = "";
			double adjGrdtot = 0, aaiGrdtot = 0, pconadjGrdtot = 0, adjGrdtotne = 0, aaiGrdtotne = 0, pconadjGrdtotne = 0;
			ArrayList list = new ArrayList();
			fromYear = "01-Apr-"+yearStrip[0];
			toYear = "31-Mar-"+(Integer.parseInt(yearStrip[0])+1);
			try {
			con = commonDB.getConnection();
			st = con.createStatement();
			 	
			if(accounthead.equals("672.03")){
				sqlQry ="select voucherno,keyno,nvl(debit,0.00) as debit,nvl(credit,0.00) as credit   from  v_form8_summary_cb where keyno in(select cb.keyno  from v_form8_summary_cb cb where fyear = '"+finyear+"'   and cb.accounthead = '"+accounthead+"'"
			                +" AND (DEBITTYPE not in ('NA') OR credittype NOT in ('NA')) minus (select keyno from employee_pension_advances  where  advtransdate between '"+fromYear+"' and '"+toYear+"') )"; 
			}
			if(accounthead.equals("672.04") || accounthead.equals("672.05")){
				sqlQry ="select voucherno,keyno,nvl(debit,0.00) as debit,nvl(credit,0.00) as credit   from  v_form8_summary_cb where keyno in(select cb.keyno  from v_form8_summary_cb cb where fyear = '"+finyear+"'   and cb.accounthead = '"+accounthead+"'"
			                +" AND (DEBITTYPE not in ('NA') OR credittype NOT in ('NA')) minus (select keyno from employee_pension_loans   where  loandate between '"+fromYear+"' and '"+toYear+"') )"; 
			}
			if(accounthead.equals("672.06") || accounthead.equals("672.07")){
				sqlQry ="select * from  v_form8_summary_cb where keyno in(select cb.keyno  from v_form8_summary_cb cb where fyear = '"+finyear+"'   and cb.accounthead = '"+accounthead+"'"
	                +" AND (DEBITTYPE not in ('NA') OR credittype NOT in ('NA')) minus (select keyno from employee_pension_finsettlement   where  settlementdate between '"+fromYear+"' and '"+toYear+"') )"; 
				}
			 
							
				log.info("sqlQry" + sqlQry);
				rs = st.executeQuery(sqlQry);
				while (rs.next()) { 
				summaryInfo = new AAIEPFReportBean();
				summaryInfo.setVoucherNo(rs.getString("voucherno"));
				summaryInfo.setKeyNo(rs.getString("keyno"));
				summaryInfo.setDebit(rs.getString("debit"));
				summaryInfo.setCredit(rs.getString("credit"));
			 log.info("==keyno="+summaryInfo.getKeyNo()+"==Vocher="+summaryInfo.getVoucherNo()+"==Debit=="+summaryInfo.getDebit()+"==Credit=="+summaryInfo.getCredit());
				} 
			log.info("summaryInfo="+summaryInfo);
			list.add(summaryInfo); 
			if(summaryInfo==null){
				list.clear();
			} 		         
			 voucherInfo = getJournalVoucherDetails(con,finyear,accounthead);
			 
			 witKeyNoList.add(list);
			 witKeyNoList.add(voucherInfo);
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(con, st, rs);
			}
			log.info("epfForm8SummaryDetailsWitKeyNo leaving ");
			return witKeyNoList;

		}
	 public ArrayList getJournalVoucherDetails(Connection con,String finyear,String accounthead ){
			log.info("getJournalVoucherDetails Entering " + finyear +"="+ accounthead);			 	 
			Statement st = null;
			ResultSet rs = null;
			AAIEPFReportBean vocherInfo =null;		 
			String sqlQry = "", fromYear = "", toYear = "", voucherno="", debit="0", credit="0"; 
			ArrayList list = new ArrayList();
			 
			try {			 
			st = con.createStatement();
			sqlQry ="select voucherno, nvl(debit,0) as debit,nvl(credit,0) as credit   from v_form8_summary_cb  where fyear = '"+finyear+"'   and  accounthead = '"+accounthead+"' and voucherno  like 'JV%'";
				log.info("sqlQry" + sqlQry);
				rs = st.executeQuery(sqlQry);
				while (rs.next()) {
					vocherInfo = new AAIEPFReportBean();				 
			 if(rs.getString("voucherno")!=null){
				 vocherInfo.setVoucherNo(rs.getString("voucherno"));
			 }
			 if(rs.getString("debit")!=null){
				 vocherInfo.setDebit(rs.getString("debit"));
			 }
			 if(rs.getString("credit")!=null){
				 vocherInfo.setCredit(rs.getString("credit"));
			 }
			 list.add(vocherInfo);
					 
		 log.info("==voucherno="+ vocherInfo.getVoucherNo()+"==debit="+ vocherInfo.getDebit()+"==credit=="+ vocherInfo.getCredit());
				} 
				         
				log.info("list size"+list.size());
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			log.info("getJournalVoucherDetails leaving ");
			return list;

		}
	 public ArrayList epfForm8SummaryDetailsWitOutKeyNo(String finyear,String accounthead){
			log.info("epfForm8SummaryDetailsWitOutKeyNo Entering " + finyear + accounthead );
			ArrayList adjlist = new ArrayList();
			String adjinfo = "";
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
			AAIEPFReportBean summaryInfo = null;
			ArrayList witOutKeyNoList = new ArrayList();
			String[] yearStrip = finyear.split("-");
			String sqlQry = "", fromYear = "", toYear = "";
			String advAmt="0",sub_amt="0",cont_amt="0",empshare="0",aaishare="0";
			double adjGrdtot = 0, aaiGrdtot = 0, pconadjGrdtot = 0, adjGrdtotne = 0, aaiGrdtotne = 0, pconadjGrdtotne = 0;
			ArrayList list = new ArrayList();
			fromYear = "01-Apr-"+yearStrip[0];
			toYear = "31-Mar-"+(Integer.parseInt(yearStrip[0])+1);
			try {
			con = commonDB.getConnection();
			st = con.createStatement();
		 
				
				if(accounthead.equals("672.03")){
					sqlQry =" select pensionno,amount from employee_pension_advances  where  advtransdate between '"+fromYear+"' and '"+toYear+"' and keyno is null"; 
				}
				if(accounthead.equals("672.04") || accounthead.equals("672.05")){
					sqlQry ="select pensionno,sub_amt, cont_amt   from employee_pension_loans   where  loandate between '"+fromYear+"' and '"+toYear+"' and keyno is null   "; 
				}
				if(accounthead.equals("672.06") || accounthead.equals("672.07")){
					sqlQry ="  select pensionno,FINEMP,FINAAI from employee_pension_finsettlement   where  settlementdate between '"+fromYear+"' and '"+toYear+"'and keyno is null "; 
					}
				log.info("sqlQry" + sqlQry);
				rs = st.executeQuery(sqlQry);
			
				while (rs.next()) { 
				summaryInfo = new AAIEPFReportBean();
				summaryInfo.setPensionNo(rs.getString("pensionno"));
				if(accounthead.equals("672.03")){
					if(rs.getString("amount")!=null){
						advAmt =rs.getString("amount");	
					} 
				}else if(accounthead.equals("672.04") || accounthead.equals("672.05")){
					if(rs.getString("sub_amt")!=null){
						sub_amt = rs.getString("sub_amt");	
						} 
					if(rs.getString("cont_amt")!=null){
						cont_amt = rs.getString("cont_amt");	
						}  
				}else if(accounthead.equals("672.06") || accounthead.equals("672.07")){
					if(rs.getString("FINEMP")!=null){
						empshare = rs.getString("FINEMP");	
						} 
					if(rs.getString("FINAAI")!=null){
						aaishare =rs.getString("FINAAI");	
						}  
					 
				}
				
				summaryInfo.setAdvAmt(advAmt);
				summaryInfo.setSubscriptionAmt(sub_amt);
				summaryInfo.setContributionamt(cont_amt);
				summaryInfo.setEmpShare(empshare);
				summaryInfo.setAAIShare(aaishare);	
				 
				
				log.info("==pensionno="+summaryInfo.getPensionNo()+"==amount="+ summaryInfo.getAdvAmt()+
						 "==sub_amt=="+summaryInfo.getSubscriptionAmt()+"==cont_amt=="+summaryInfo.getContributionamt()+
						 "==FINEMP=="+summaryInfo.getEmpShare()+"==FINAAI=="+summaryInfo.getAAIShare());
				witOutKeyNoList.add(summaryInfo);
				} 
			 
			 
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(con, st, rs);
			}
			log.info("epfForm8SummaryDetailsWitOutKeyNo leaving ");
			return witOutKeyNoList;

		}
	 // By Radha 12-Jul-2012 A Condition if Retired Cases Come as DEP Then Use ECRFORM4SUPPLIARRARFLAG 'J' in Suppli
//		Here  REGSALFLAG is for showing the Supplidata when sal month is May and Paid date is Jun  as Normal Wages as per Sehgal Request
	 public String buildQueryEmpPFTransArrearInfo(String region, String airportcode,
				String sortedColumn, String fromDate, String toDate) {
			log
					.info("EPFFormsReportDAO::buildQueryEmpPFTransArrearInfo-- Entering Method");
			StringBuffer whereClause = new StringBuffer();
			StringBuffer query = new StringBuffer();
			String dynamicQuery = "", sqlQuery = "";

			sqlQuery = "SELECT  SUP.PAIDDATE AS MONTHYEAR, SUM(round(SUP.EMOLUMENTS)) as  EMOLUMENTS,SUM(round(SUP.GROSS)) as  GROSS,SUM(round(SUP.EMPPFSTATUARY)) AS EMPPFSTATUARY, SUM(round(SUP.EMPVPF)) AS EMPVPF, SUM(VAL.CPF) AS CPF, SUM(round(SUP.EMPADVRECPRINCIPAL)) AS EMPADVRECPRINCIPAL,"
					+ "SUM(round(SUP.EMPADVRECINTEREST)) AS EMPADVRECINTEREST, SUM(round(VAL.AAICONPF)) AS AAICONPF, SUM(ROUND(VAL.CPFADVANCE)) AS CPFADVANCE, SUM(ROUND(VAL.DEDADV)) AS DEDADV, SUM(ROUND(VAL.REFADV)) AS REFADV, SUM(ROUND(SUP.PENSIONCONTRI)) AS PENSIONCONTRI,SUM(ROUND(SUP.additionalcontri)) AS additionalcontri,SUM(ROUND(SUP.PF)) AS PF, MAX(VAL.AIRPORTCODE) AS AIRPORTCODE, "
					+ "MAX(SUP.REGION) AS REGION, MAX(VAL.EMPFLAG) AS EMPFLAG, MAX(VAL.CPFACCNO) AS CPFACCNO,SUP.PENSIONNO AS PENSIONNO,MAX(VAL.EMPLOYEENO) AS EMPLOYEENO,MAX(VAL.ARREARFLAG) AS ARREARFLAG,MAX(CASE WHEN SUP.SUPPLIFLAG='S' THEN (CASE WHEN ADD_MONTHS(SUP.PAIDDATE,1)=SUP.SALMONTH THEN 'Y' ELSE 'N' END) ELSE 'N'  END) AS REGSALFLAG , MAX(VAL.EMPRECOVERYSTS) AS  EMPRECOVERYSTS FROM EMPLOYEE_PENSION_VALIDATE VAL,employee_supplimentory_data SUP WHERE VAL.PENSIONNO = SUP.PENSIONNO AND VAL.MONTHYEAR=SUP.MONTHYEAR AND SUP.PAIDDATE between '"
					+ fromDate + "' and LAST_DAY('" + toDate + "')  and  VAL.EMPFLAG = 'Y' AND VAL.EMPFLAG = SUP.EMPFLAG  AND ((SUP.SUPPLIFLAG IN ('A', 'S')) OR(SUP.SUPPLIFLAG='F' AND SUP.ECRFORM4SUPPLIARRARFLAG='J'))  AND VAL.SUPPLIFLAG = 'Y' AND VAL.PENSIONNO IS NOT NULL AND  SUP.PENSIONCONTRI != 0";

			if (!region.equals("")) {
				whereClause.append(" UPPER(VAL.REGION) ='" + region.toUpperCase()+ "'");
				whereClause.append(" AND ");
			}
			if (!airportcode.equals("")) {
				whereClause.append(" VAL.AIRPORTCODE  IN('" + airportcode + "')");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if ((region.equals("")) && (airportcode.equals(""))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}

			String orderBy = " Group by SUP.PENSIONNO,SUP.PAIDDATE ORDER BY " + sortedColumn;
			query.append(orderBy);
			dynamicQuery = query.toString();
			log.info("EPFFormsReportDAO::buildQueryEmpPFTransArrearInfo Leaving Method");
			return dynamicQuery;
		}
	 public Map getForm3SummaryReport(String year, String flag,String empStatus) { 

			Statement st = null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			Connection con = null;
			String region = "", airportcode = "---", sqlQueryAllRegions = "", sqlQueryCHQIAD = "", condition = "";
			double totalEmoluments = 0.0, totalEPF = 0.0, totalVPF = 0.0, totalPrincipal = 0.0, totalInterest = 0.0, totalPF = 0.0, totalPensoinContri = 0.0;
			Map map = new LinkedHashMap();
			ArrayList list=null;
					
			if (empStatus.equals("A")||empStatus.equals("D")) {
				condition = " and i.status='"+empStatus+"' ";
			}else{
				condition="";
			}
			if(flag.equals("CPF")){
			sqlQueryAllRegions = "select w.region,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf, SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc , sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where w.FINYEAR='"+year+"' and  w.region != 'CHQIAD' and w.form3flag in('O','M') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear   group by w.region order by w.region";

			sqlQueryCHQIAD = "select w.region,w.airportcode as airportcode,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf,SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc ,sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where  w.FINYEAR='"+year+"' and  w.region = 'CHQIAD' and w.form3flag in('O','M') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear  group by w.region,w.airportcode order by w.airportcode";
			}else if(flag.equals("SUP")){
				sqlQueryAllRegions = "select w.region,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf, SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc , sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where w.FINYEAR='"+year+"' and  w.region != 'CHQIAD' and w.form3flag in('S') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear   group by w.region order by w.region";

				sqlQueryCHQIAD = "select w.region,w.airportcode as airportcode,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf,SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc ,sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where  w.FINYEAR='"+year+"' and  w.region = 'CHQIAD' and w.form3flag in('S') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear  group by w.region,w.airportcode order by w.airportcode";
				
			}else if(flag.equals("ARR")){
				sqlQueryAllRegions = "select w.region,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf, SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc , sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where w.FINYEAR='"+year+"' and  w.region != 'CHQIAD' and w.form3flag in('A') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear   group by w.region order by w.region";

				sqlQueryCHQIAD = "select w.region,w.airportcode as airportcode,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf,SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc ,sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where  w.FINYEAR='"+year+"' and  w.region = 'CHQIAD' and w.form3flag in('A') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear  group by w.region,w.airportcode order by w.airportcode";
			}else if(flag.equals("FORM4")){
				sqlQueryAllRegions = "select w.region,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf, SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc , sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where w.FINYEAR='"+year+"' and  w.region != 'CHQIAD' and w.form3flag in('F') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear   group by w.region order by w.region";

				sqlQueryCHQIAD = "select w.region,w.airportcode as airportcode,sum(nvl(w.emoluments, 0)) as emoluments,sum(nvl(w.epf, 0)) as epf,SUM(nvl(w.vpf, 0)) as vpf,sum(nvl(w.emprefadvprincipal, 0)) as principal,sum(nvl(w.emprefadvinterest, 0)) as interest,sum(nvl(w.pc, 0)) as pc ,sum(nvl(w.pf, 0)) as pf  from epis_monthly_trans_pfid_wise w,v_emp_personal_info i where  w.FINYEAR='"+year+"' and  w.region = 'CHQIAD' and w.form3flag in('F') and w.pensionno=i.pensionno "+condition+" and w.finyear=i.finyear  group by w.region,w.airportcode order by w.airportcode";
	
			}
			try {
				con = commonDB.getConnection();
				st = con.createStatement();
				
					log.info("FinancialReportDAO::getForm3SummaryReport--sqlQueryAllRegions "
									+ sqlQueryAllRegions);
					rs = st.executeQuery(sqlQueryAllRegions);
				while (rs.next()) {
					list=new ArrayList();
					if (rs.getString("region")!=null) {
						region = rs.getString("region");
					}
					if (rs.getString("emoluments") != null) {
						totalEmoluments = Double.parseDouble(rs.getString("emoluments"));
					} else {
						totalEmoluments = 0.0;
					}
					if (rs.getString("epf") != null) {
						totalEPF = Double.parseDouble(rs.getString("epf"));
					} else {
						totalEPF = 0.0;
					}
					if (rs.getString("vpf") != null) {
						totalVPF = Double.parseDouble(rs.getString("vpf"));
					} else {
						totalVPF = 0.0;
					}
					if (rs.getString("principal") != null) {
						totalPrincipal = Double.parseDouble(rs
								.getString("principal"));
					} else {
						totalPrincipal = 0.0;
					}
					if (rs.getString("interest") != null) {
						totalInterest = Double
								.parseDouble(rs.getString("interest"));
					} else {
						totalInterest = 0.0;
					}
					if (rs.getString("pf") != null) {
						totalPF = Double.parseDouble(rs.getString("pf"));
					} else {
						totalPF = 0.0;
					}
					if (rs.getString("pc") != null) {
						totalPensoinContri = Double.parseDouble(rs
								.getString("pc"));
					} else {
						totalPensoinContri = 0.0;
					}
					list.add(region);
					list.add(Double.toString(totalEmoluments ));
					list.add(Double.toString(totalEPF));
					list.add(Double.toString(totalVPF));
					list.add(Double.toString(totalPrincipal));
					list.add(Double.toString(totalInterest));
					list.add(Double.toString(totalPF));
					list.add(Double.toString(totalPensoinContri));

					map.put("A-"+region,list );
				}
							
					log.info("FinancialReportDAO::getForm3SummaryReport--sqlQueryCHQIAD "
									+ sqlQueryCHQIAD);
					rs1 = st.executeQuery(sqlQueryCHQIAD);
					while (rs1.next()) {
						list=new ArrayList();
						if (rs1.getString("airportcode")!=null) {
							airportcode = rs.getString("airportcode");
						}
						if (rs1.getString("emoluments") != null) {
							totalEmoluments = Double.parseDouble(rs1.getString("emoluments"));						
						} else {
							totalEmoluments = 0.0;
						}
						if (rs1.getString("epf") != null) {
							totalEPF = Double.parseDouble(rs1.getString("epf"));
						} else {
							totalEPF = 0.0;
						}
						if (rs1.getString("vpf") != null) {
							totalVPF = Double.parseDouble(rs1.getString("vpf"));
						} else {
							totalVPF = 0.0;
						}
						if (rs1.getString("principal") != null) {
							totalPrincipal = Double.parseDouble(rs1
									.getString("principal"));
						} else {
							totalPrincipal = 0.0;
						}
						if (rs1.getString("interest") != null) {
							totalInterest = Double
									.parseDouble(rs1.getString("interest"));
						} else {
							totalInterest = 0.0;
						}
						if (rs1.getString("pf") != null) {
							totalPF = Double.parseDouble(rs1.getString("pf"));
						} else {
							totalPF = 0.0;
						}
						if (rs1.getString("pc") != null) {
							totalPensoinContri = Double.parseDouble(rs1
									.getString("pc"));
						} else {
							totalPensoinContri = 0.0;
						}
						list.add(airportcode);
						list.add(Double.toString(totalEmoluments ));
						list.add(Double.toString(totalEPF));
						list.add(Double.toString(totalVPF));
						list.add(Double.toString(totalPrincipal));
						list.add(Double.toString(totalInterest));
						list.add(Double.toString(totalPF));
						list.add(Double.toString(totalPensoinContri));

						map.put(airportcode,list );
					}
				
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, null);
			}
			return map;
	 }
	 public String buildQueryForm6ASeperationInfo(String range, String region,
				String airportcode, String empNameFlag, String empName,
				String sortedColumn, String pensionno, String fromDate) {
			log.info("EPFFormsReportDAO::buildQueryForm6ASeperationInfo-- Entering Method");
			StringBuffer whereClause = new StringBuffer();
			StringBuffer query = new StringBuffer();
			String dynamicQuery = "", sqlQuery = "";
			int startIndex = 0, endIndex = 0;
			sqlQuery = "SELECT CPFACNO,EMPLOYEENO,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,to_date(DATEOFBIRTH) as DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,to_date(to_char(DATEOFSEPERATION_DATE,'dd-Mon-yyyy')) as DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
				+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,EPI.REGION AS REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
				+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12,1)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
				+ "ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE,nvl(FHFLAG,'F') as FHFLAG, (ROUND(months_between('"+fromDate+"', last_day(dateofbirth)) / 12, 1)) AS PENSIONAGE,(ROUND(months_between('"+fromDate+"', last_day(dateofjoining)) / 12, 1)) AS DOJAGE,EUM.ACCOUNTTYPE AS ACCOUNTTYPE,(select f.freshpensionoption from employee_pension_freshoption f where deleteflag = 'N' and pensionno = epi.pensionno) as freshoption FROM EMPLOYEE_PERSONAL_INFO EPI ,EMPLOYEE_UNIT_MASTER EUM  WHERE UPPER(EUM.REGION)= UPPER(EPI.REGION) AND UPPER(EUM.UNITNAME)=UPPER(AIRPORTCODE) AND  "
				+ " (DATEOFSEPERATION_REASON IN ('Death','Resignation', 'Termination', 'VRS') AND to_date(to_char(ADD_MONTHS(NVL(DATEOFSEPERATION_DATE,last_day('"+fromDate+"')),1),'dd-Mon-yyyy'))  between '"+fromDate+"' and  TO_DATE(last_day('"
				+ fromDate + "'))) ";

			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  PENSIONNO >=" + startIndex
						+ " AND PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}
			if (!region.equals("")) {
				whereClause.append(" UPPER(EUM.REGION) ='" + region.toUpperCase() + "'");
				whereClause.append(" AND ");
			}
			if (!airportcode.equals("")) {
				whereClause.append(" UPPER(EUM.UNITNAME)  IN('" + airportcode + "')");
				whereClause.append(" AND ");
			}
			query.append(sqlQuery);
			if ((region.equals("")) && (airportcode.equals("")) && (range
					.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}

			String orderBy = "ORDER BY " + sortedColumn;
			query.append(orderBy);
			dynamicQuery = query.toString();
			log.info("EPFFormsReportDAO::buildQueryForm6ASeperationInfo Leaving Method");
			return dynamicQuery;
		}
	 public String buildQueryForm6AEmpPFNewJoineeInfo(String range, String region,
				String airportcode, String empNameFlag, String empName,
				String sortedColumn, String pensionno, String fromDate) {
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFNewJoineeInfo-- Entering Method");
			StringBuffer whereClause = new StringBuffer();
			StringBuffer query = new StringBuffer();
			String dynamicQuery = "", sqlQuery = "";
			int startIndex = 0, endIndex = 0;
			sqlQuery = "SELECT CPFACNO,EMPLOYEENO,INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,to_date(DATEOFBIRTH) as DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,to_date(to_char(DATEOFSEPERATION_DATE,'dd-Mon-yyyy')) as DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,"
				+ "TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,EPI.REGION AS REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,OTHERREASON,"
				+ "decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12,1)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
				+ "ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE,Nvl(FHFLAG,'F') as FHFLAG, (ROUND(months_between('"+fromDate+"', last_day(dateofbirth)) / 12, 1)) AS PENSIONAGE,(ROUND(months_between('"+fromDate+"', last_day(dateofjoining)) / 12, 1)) AS DOJAGE,EUM.ACCOUNTTYPE AS ACCOUNTTYPE,(CASE WHEN (EPI.BASIC IS NULL) THEN 'N' else 'Y' END) as DTRECEVIEDNEWJOINEE,(select f.freshpensionoption from employee_pension_freshoption f where deleteflag = 'N' and pensionno = epi.pensionno) as freshoption FROM EMPLOYEE_PERSONAL_INFO EPI ,EMPLOYEE_UNIT_MASTER EUM  WHERE UPPER(EUM.REGION)= UPPER(EPI.REGION) AND UPPER(EUM.UNITNAME)=UPPER(AIRPORTCODE) AND  "
				+ " (DATEOFSEPERATION_REASON is null and DATEOFSEPERATION_DATE is null) AND to_date(to_char(add_months(dateofjoining, 1), 'dd-Mon-yyyy'))  between '"+fromDate+"' and  TO_DATE(last_day('"
					+ fromDate + "')) ";

			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  PENSIONNO >=" + startIndex
						+ " AND PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}
			 if (!region.equals("")) {
				whereClause.append(" UPPER(EUM.REGION) ='" + region.toUpperCase()+ "'");
				whereClause.append(" AND ");
			}
			if (!airportcode.equals("")) {
				whereClause.append(" UPPER(EUM.UNITNAME)  IN ('" + airportcode + "')");
				whereClause.append(" AND ");
			} 
			query.append(sqlQuery);
			if ((region.equals("")) && (airportcode.equals("")) && (range
					.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(commonDAO.sTokenFormat(whereClause));
			}

			String orderBy = "ORDER BY " + sortedColumn;
			query.append(orderBy);
			dynamicQuery = query.toString();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFNewJoineeInfo Leaving Method");
			return dynamicQuery;
		}
	 public ArrayList getStationList(String region,String monthyear,String aiportcode){
		 log.info("monthyear===="+monthyear+"aiportcode"+aiportcode);
		 ArrayList stationsList =null;
		 StationWiseRemittancebean stationWiseRemittanceBean=null;
		 String sqlQry="",sqlCountQry="",sqlInsertQry="",station="",regionRegion="",accountType="",orders="",condition="",pf="",inspCharges="",uploadpc="",pc="",notes="",dateOfReceipt="";
		 int stationRecordCount=0;
		 double totalPC=0.0,totalPF=0.0,totalInspCharges=0.0,totalAddContri=0.0;
		 Connection con = null;
		 Statement st = null;
		 ResultSet rs = null;
		 ResultSet rs1 = null;
		 
		 try{ 
			 con=commonDB.getConnection();
			 st=con.createStatement();
			 if(!region.equals("All")){
				 if(region.equals("CHQIAD") && !aiportcode.equals("")){
					 condition=" and S.REGION='"+region+"' and s.airportcode='"+aiportcode+"'"; 
				 }else{
				 condition=" and S.REGION='"+region+"'";
				 }
			 }
			 sqlCountQry="select count(*) as count from epis_remittance_cmp_st d where d.remittedmonth='"+monthyear+"'";
			 log.info("sqlCountQry====="+sqlCountQry);
			 rs1=st.executeQuery(sqlCountQry);
			 if(rs1.next()){
				 stationRecordCount=rs1.getInt("count"); 
			 }
			 if(stationRecordCount==0){
				 sqlInsertQry="insert into epis_remittance_cmp_st(rid,region,airportcode,accnttype,remittedmonth,lastactive) (select Station_Wise_remittance_id.Nextval,f.REGION,f.AIRPORTCODE,f.accounttype,'"+monthyear+"',sysdate from  V_EMP_CPF_REM_FMT f)"; 
				 log.info("sqlInsertQry"+sqlInsertQry);
				 st.executeQuery(sqlInsertQry);
			 }
			// sqlQry="select c.ORDERS,c.REGION as REGION,d.monthyear,nvl(s.pc, 0) as pc,nvl(s.noofemppc, 0) as noofemppc,s.notes as notes,c.AIRPORTCODE as AIRPORTCODE,c.accounttype as accounttype,c.ORDERS as ORDERS,s.receiptpcdate as receiptpcdate,nvl(d.pc, 0) + nvl(d.suplipc, 0) as uploadpc  from V_EMP_CPF_REM_FMT c, mV_EMP_CPF_REM_DT d, epis_remittance_cmp_st s where c.REGION =s.region "+condition+" and c.AIRPORTCODE=s.airportcode and s.AIRPORTCODE = d.AIRPORTCODE(+) and d.monthyear(+)=s.remittedmonth and s.remittedmonth between '"+monthyear+"' and last_day('"+monthyear+"') order by c.ORDERS, c.accounttype, c.AIRPORTCODE";
			/* sqlQry="select c.ORDERS as ORDERS,c.REGION as REGION,d.monthyear,nvl(s.pc, 0) as pc,nvl(s.pf,0) as pf,nvl(s.inspcharges,0) as inspcharges,nvl(s.chqpc, 0) as chqpc,nvl(s.chqpf,0) as chqpf,nvl(s.chqinspcharges,0) as chqinspcharges,s.notes as notes,c.AIRPORTCODE as AIRPORTCODE,c.accounttype as accounttype,s.pcremitdate as pcremitdate,s.pfremitdate as pfremitdate,s.inspremitdate as inspremitdate,s.chqpcremitdate as chqpcremitdate,s.chqpfremitdate as chqpfremitdate,s.chqinspremitdate as chqinspremitdate," +
			 		"nvl(d.pc,0) as cpfpc,nvl(d.apc,0) as arrearpc,nvl(d.spc,0) as supplipc,nvl(d.pf,0) as cpfpf,nvl(d.apf,0) as arrearpf,nvl(d.spf,0) as supplipf,nvl(d.cpfinspcharges,0) as cpfinspcharges, nvl(d.ainspcharges,0) as ainspcharges,nvl(d.sinspcharges,0) as sinspcharges  from V_EMP_CPF_REM_FMT c, mV_EMP_CPF_swiseREM_DT d, epis_remittance_cmp_st s where c.REGION = s.region  "+condition+"  and c.AIRPORTCODE = s.airportcode   and s.AIRPORTCODE = d.AIRPORTCODE(+)   and d.monthyear(+) = s.remittedmonth   and s.remittedmonth between '"+monthyear+"' and last_day('"+monthyear+"') order by c.ORDERS, c.accounttype, c.AIRPORTCODE";
			 */
			 sqlQry="select c.ORDERS as ORDERS, c.REGION as REGION, d.monthyear,nvl(s.pc, 0) as pc,nvl(s.pf, 0) as pf,nvl(s.inspcharges, 0) as inspcharges,nvl(s.chqpc, 0) as chqpc,nvl(s.chqpf, 0) as chqpf,nvl(s.chqinspcharges, 0) as chqinspcharges,s.notes as notes,c.AIRPORTCODE as AIRPORTCODE,c.accounttype as accounttype,s.pcremitdate as pcremitdate,s.pfremitdate as pfremitdate,s.inspremitdate as inspremitdate,s.chqpcremitdate as chqpcremitdate,s.chqpfremitdate as chqpfremitdate,s.chqinspremitdate as chqinspremitdate,nvl(d.pc, 0) as cpfpc,nvl(d.apc, 0) as arrearpc,nvl(d.spc, 0) as supplipc,nvl(d.pf, 0) as cpfpf,nvl(d.apf, 0) as arrearpf," +
			 		"nvl(d.spf, 0) as supplipf,nvl(d.cpfinspcharges, 0) as cpfinspcharges,nvl(d.ainspcharges, 0) as ainspcharges,nvl(d.sinspcharges, 0) as sinspcharges,nvl(d.emppfstatuary, 0) as emppfstatuary,nvl(d.aemppfstatuary, 0) as aemppfstatuary,nvl(d.semppfstatuary, 0) as semppfstatuary,nvl(d.cpfpf, 0) as ccpf,nvl(d.aapf, 0) as aapf,nvl(d.sspf, 0) as sspf,nvl(d.cpfvpf,0) as cpfvpf,nvl(d.avpf,0)  as avpf,nvl(d.svpf,0) as svpf,nvl(d.cpfadvprincipal,0) as cpfadvprincipal,nvl(d.aadvprincipal,0) as aadvprincipal,nvl(d.sadvprincipal,0) as sadvprincipal,nvl(d.cpfadvrecint,0) as cpfadvrecint,nvl(d.aadvrecint,0) as aadvrecint,nvl(d.sadvrecint,0) as sadvrecint,nvl(d.cpfaddcontri, 0) as cpfaddcontri, nvl(d.aaddcontri, 0) as aaddcontri, nvl(d.saddcontri, 0) as saddcontri," +
			 		"nvl(d.emppfstatuary, 0)+nvl(d.cpfpf, 0)+nvl(d.cpfvpf,0)+nvl(d.cpfadvprincipal,0)+nvl(d.cpfadvrecint,0) as cpfacc,nvl(d.aemppfstatuary, 0)+nvl(d.aapf, 0)+ nvl(d.avpf,0)+nvl(d.aadvprincipal,0)+nvl(d.aadvrecint,0) as ArrearAcc,nvl(d.semppfstatuary, 0)+ nvl(d.sspf, 0)+  nvl(d.svpf,0)+  nvl(d.sadvprincipal,0)+ nvl(d.sadvrecint,0) as  suppliacc from V_EMP_CPF_REM_FMT      c, mV_EMP_CPF_swiseREM_DT d, epis_remittance_cmp_st s where c.REGION = s.region "+condition+" and c.AIRPORTCODE = s.airportcode  and s.AIRPORTCODE = d.AIRPORTCODE(+) and d.monthyear(+) = s.remittedmonth and s.remittedmonth between '"+monthyear+"' and last_day('"+monthyear+"') order by c.ORDERS, c.accounttype, c.AIRPORTCODE";
			 log.info("sqlQry====="+sqlQry);
			 rs=st.executeQuery(sqlQry);
			 stationsList = new ArrayList();
			 while(rs.next()){
				 stationWiseRemittanceBean= new  StationWiseRemittancebean(); 
				 if(rs.getString("region")!=null){
					 regionRegion=rs.getString("region");
					 stationWiseRemittanceBean.setRegion(regionRegion);
				 }
				 if(rs.getString("AIRPORTCODE")!=null){
					 station=rs.getString("AIRPORTCODE");
					 stationWiseRemittanceBean.setStation(station);
				 }
				 if(rs.getString("accounttype")!=null){
					 accountType=rs.getString("accounttype");
					 stationWiseRemittanceBean.setAccType(accountType);
				 }
				 if(rs.getString("ORDERS")!=null){
					 orders=rs.getString("ORDERS");
					 stationWiseRemittanceBean.setOrders(orders);
				 }
				 if(rs.getString("pc")!=null){
					 pc=rs.getString("pc");
					 stationWiseRemittanceBean.setPc(pc);
				 }
				 if(rs.getString("pf")!=null){
					 pf=rs.getString("pf");
					 stationWiseRemittanceBean.setPf(pf);
				 }
				 if(rs.getString("inspcharges")!=null){
					 inspCharges=rs.getString("inspcharges");
					 stationWiseRemittanceBean.setInspCharges(inspCharges);
				 }
				 if(rs.getDate("pcremitdate")!=null){
					  stationWiseRemittanceBean.setPcRemitDate(commonUtil.getDatetoString(rs.getDate("pcremitdate"),"dd-MMM-yyyy"));
				 }
				 if(rs.getDate("pfremitdate")!=null){
					  stationWiseRemittanceBean.setPfRemitDate(commonUtil.getDatetoString(rs.getDate("pfremitdate"),"dd-MMM-yyyy"));
				 }
				 if(rs.getDate("inspremitdate")!=null){
					 stationWiseRemittanceBean.setInspremitDate(commonUtil.getDatetoString(rs.getDate("inspremitdate"),"dd-MMM-yyyy"));
				 }
				 
				 if(rs.getString("chqpc")!=null){
					 stationWiseRemittanceBean.setChqPc(rs.getString("chqpc"));
				 }
				 if(rs.getString("chqpf")!=null){
					 stationWiseRemittanceBean.setChqPf(rs.getString("chqpf"));
				 }
				 if(rs.getString("chqinspcharges")!=null){
					 stationWiseRemittanceBean.setChqInspCharges(rs.getString("chqinspcharges"));
				 }
				 if(rs.getDate("chqpcremitdate")!=null){
					  stationWiseRemittanceBean.setChqPcRemitDate(commonUtil.getDatetoString(rs.getDate("chqpcremitdate"),"dd-MMM-yyyy"));
				 }
				 if(rs.getDate("chqpfremitdate")!=null){
					  stationWiseRemittanceBean.setChqPfRemitDate(commonUtil.getDatetoString(rs.getDate("chqpfremitdate"),"dd-MMM-yyyy"));
				 }
				 if(rs.getDate("chqinspremitdate")!=null){
					 stationWiseRemittanceBean.setChqInspremitDate(commonUtil.getDatetoString(rs.getDate("chqinspremitdate"),"dd-MMM-yyyy"));
				 }
				 
				 if(rs.getString("cpfpc")!=null){
					 stationWiseRemittanceBean.setCpfPc(rs.getString("cpfpc"));
				 }else{
					 stationWiseRemittanceBean.setCpfPc("0");
				 }
				 if(rs.getString("arrearpc")!=null){
					 stationWiseRemittanceBean.setArrearPc(rs.getString("arrearpc"));
				 }else{
					 stationWiseRemittanceBean.setArrearPc("0");
				 }
				 if(rs.getString("supplipc")!=null){
					 stationWiseRemittanceBean.setSuppliPc(rs.getString("supplipc"));
				 }else{
					 stationWiseRemittanceBean.setSuppliPc("0");
				 }
				 if(rs.getString("cpfpf")!=null){
					 stationWiseRemittanceBean.setCpfPf(rs.getString("cpfpf"));
				 }else{
					 stationWiseRemittanceBean.setCpfPf("0");
				 }
				 if(rs.getString("arrearpf")!=null){
					 stationWiseRemittanceBean.setArrearPf(rs.getString("arrearpf"));
				 }else{
					 stationWiseRemittanceBean.setArrearPf("0");
				 }
				 if(rs.getString("supplipf")!=null){
					 stationWiseRemittanceBean.setSuppliPf(rs.getString("supplipf"));
				 }else{
					 stationWiseRemittanceBean.setSuppliPf("0");
				 }
				 if(rs.getString("cpfinspcharges")!=null){
					 stationWiseRemittanceBean.setCpfInspCharges(rs.getString("cpfinspcharges"));
				 }else{
					 stationWiseRemittanceBean.setCpfInspCharges("0");
				 }
				 if(rs.getString("ainspcharges")!=null){
					 stationWiseRemittanceBean.setArrearInspCharges(rs.getString("ainspcharges"));
				 }else{
					 stationWiseRemittanceBean.setArrearInspCharges("0");
				 }
				 if(rs.getString("sinspcharges")!=null){
					 stationWiseRemittanceBean.setSuppliInspCharges(rs.getString("sinspcharges"));
				 }else{
					 stationWiseRemittanceBean.setSuppliInspCharges("0");
				 }
				 if(rs.getString("emppfstatuary")!=null){
					 stationWiseRemittanceBean.setCpfEpf(rs.getString("emppfstatuary"));
				 }else{
					 stationWiseRemittanceBean.setCpfEpf("0");
				 }
				 if(rs.getString("aemppfstatuary")!=null){
					 stationWiseRemittanceBean.setAEpf(rs.getString("aemppfstatuary"));
				 }else{
					 stationWiseRemittanceBean.setAEpf("0");
				 }
				 if(rs.getString("semppfstatuary")!=null){
					 stationWiseRemittanceBean.setSEpf(rs.getString("semppfstatuary"));
				 }else{
					 stationWiseRemittanceBean.setSEpf("0");
				 }
				 if(rs.getString("ccpf")!=null){
					 stationWiseRemittanceBean.setAaipc(rs.getString("ccpf"));
				 }else{
					 stationWiseRemittanceBean.setAaipc("0");
				 }
				 if(rs.getString("aapf")!=null){
					 stationWiseRemittanceBean.setApc(rs.getString("aapf"));
				 }else{
					 stationWiseRemittanceBean.setApc("0");
				 }
				 if(rs.getString("sspf")!=null){
					 stationWiseRemittanceBean.setSpc(rs.getString("sspf"));
				 }else{
					 stationWiseRemittanceBean.setSpc("0");
				 }
				 if(rs.getString("cpfvpf")!=null){
					 stationWiseRemittanceBean.setCpfVpf(rs.getString("cpfvpf"));
				 }else{
					 stationWiseRemittanceBean.setCpfVpf("0");
				 }
				 if(rs.getString("avpf")!=null){
					 stationWiseRemittanceBean.setAVpf(rs.getString("avpf"));
				 }else{
					 stationWiseRemittanceBean.setAVpf("0");
				 }
				 if(rs.getString("svpf")!=null){
					 stationWiseRemittanceBean.setSVpf(rs.getString("svpf"));
				 }else{
					 stationWiseRemittanceBean.setSVpf("0");
				 }
				 if(rs.getString("cpfadvprincipal")!=null){
					 stationWiseRemittanceBean.setCpfRefAdv(rs.getString("cpfadvprincipal"));
				 }else{
					 stationWiseRemittanceBean.setCpfRefAdv("0");
				 }
				 if(rs.getString("aadvprincipal")!=null){
					 stationWiseRemittanceBean.setARefAdv(rs.getString("aadvprincipal"));
				 }else{
					 stationWiseRemittanceBean.setARefAdv("0");
				 }
				 if(rs.getString("sadvprincipal")!=null){
					 stationWiseRemittanceBean.setSRefAdv(rs.getString("sadvprincipal"));
				 }else{
					 stationWiseRemittanceBean.setSRefAdv("0");
				 }
				 if(rs.getString("cpfadvrecint")!=null){
					 stationWiseRemittanceBean.setCpfAdvInt(rs.getString("cpfadvrecint"));
				 }else{
					 stationWiseRemittanceBean.setCpfAdvInt("0");
				 }
				 if(rs.getString("aadvrecint")!=null){
					 stationWiseRemittanceBean.setAAdvInt(rs.getString("aadvrecint"));
				 }else{
					 stationWiseRemittanceBean.setAAdvInt("0");
				 }
				 if(rs.getString("sadvrecint")!=null){
					 stationWiseRemittanceBean.setSAdvInt(rs.getString("sadvrecint"));
				 }else{
					 stationWiseRemittanceBean.setSAdvInt("0");
				 }
				 
				 
				 if(rs.getString("cpfaddcontri")!=null){
					 stationWiseRemittanceBean.setCpfaddcontri(rs.getString("cpfaddcontri"));
				 }else{
					 stationWiseRemittanceBean.setCpfaddcontri("0");
				 }
				 if(rs.getString("aaddcontri")!=null){
					 stationWiseRemittanceBean.setAaddcontri(rs.getString("aaddcontri"));
				 }else{
					 stationWiseRemittanceBean.setAaddcontri("0");
				 }
				 if(rs.getString("saddcontri")!=null){
					 stationWiseRemittanceBean.setSaddcontri(rs.getString("saddcontri"));
				 }else{
					 stationWiseRemittanceBean.setSaddcontri("0");
				 }
				 
				 
				 if(rs.getString("cpfacc")!=null){
					 stationWiseRemittanceBean.setCpfTotal(rs.getString("cpfacc"));
				 }else{
					 stationWiseRemittanceBean.setCpfTotal("0");
				 }
				 if(rs.getString("ArrearAcc")!=null){
					 stationWiseRemittanceBean.setATotal(rs.getString("ArrearAcc"));
				 }else{
					 stationWiseRemittanceBean.setATotal("0");
				 }
				 if(rs.getString("suppliacc")!=null){
					 stationWiseRemittanceBean.setSTotal(rs.getString("suppliacc"));
				 }else{
					 stationWiseRemittanceBean.setSTotal("0");
				 }
				 totalPC=Double.parseDouble(stationWiseRemittanceBean.getCpfPc())+Double.parseDouble(stationWiseRemittanceBean.getArrearPc())+Double.parseDouble(stationWiseRemittanceBean.getSuppliPc());
				 totalPF=Double.parseDouble(stationWiseRemittanceBean.getCpfPf())+Double.parseDouble(stationWiseRemittanceBean.getArrearPf())+Double.parseDouble(stationWiseRemittanceBean.getSuppliPf());
				 totalInspCharges=Double.parseDouble(stationWiseRemittanceBean.getCpfInspCharges())+Double.parseDouble(stationWiseRemittanceBean.getArrearInspCharges())+Double.parseDouble(stationWiseRemittanceBean.getSuppliInspCharges());
				 totalAddContri=Double.parseDouble(stationWiseRemittanceBean.getCpfaddcontri())+Double.parseDouble(stationWiseRemittanceBean.getAaddcontri())+Double.parseDouble(stationWiseRemittanceBean.getSaddcontri());
				 stationWiseRemittanceBean.setPcTotal(totalPC);
				 stationWiseRemittanceBean.setPfTotal(totalPF);
				 stationWiseRemittanceBean.setInspTotal(totalInspCharges);
				 stationWiseRemittanceBean.setAddContriTotal(totalAddContri);
				 
				 if(rs.getString("notes")!=null){
					 notes=rs.getString("notes");
					 stationWiseRemittanceBean.setNotes(notes);
				 }else{
					 notes="---"; 
					 stationWiseRemittanceBean.setNotes(notes);
				 }

				
				 if((stationWiseRemittanceBean.getStation().trim().equals("COCHIN") || stationWiseRemittanceBean.getStation().trim().equals("ALLAHABAD")) && (commonUtil.getDifferenceTwoDatesInDays(monthyear,"01-Jul-2013")>0)){
					 stationsList.add(stationWiseRemittanceBean); 
				 }else{
				 stationsList.add(stationWiseRemittanceBean);
				 }
				
				 //log.info("Stationlist Size"+stationsList.size());
			 }
			 
			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 commonDB.closeConnection(con,st,rs);
		 }
		 
		 return stationsList;
	 }
	 public int getupdateStationWiseRemittance(String pfRemitDate,String inspRemitDate,String pcRemitDate,String pf,String insp,String pc,String editid,String remittanceDate,String station,String region,String accType,String username,String flag,String remarks){
		 String sqlQry="",insertOrUpdateSqlQry="",insertLogSqlQry="";
		 int result=0;
		 Connection con = null;
		 Statement st = null;
		 ResultSet rs = null;
		 try{
			 con=commonDB.getConnection();
			 st=con.createStatement();
			 sqlQry="select 'X' as flag from epis_remittance_cmp_st where remittedmonth='"+remittanceDate+"' and  region = '"+region+"' and airportcode='"+station+"'";
			 log.info("sqlQry====="+sqlQry);
			 rs=st.executeQuery(sqlQry);
			 if(!rs.next()){
				 if(!flag.equals("C")){
				 insertOrUpdateSqlQry="insert into epis_remittance_cmp_st(rid,remittedmonth,region,airportcode,accnttype,PFREMITDATE,PF,INSPREMITDATE,INSPCHARGES,PCREMITDATE,PC,username,lastactive,notes) values  (Station_Wise_remittance_id.Nextval,'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"','"+pcRemitDate+"','"+pc+"','"+username+"',sysdate,'"+remarks+"')"; 
				 insertLogSqlQry="insert into epis_remittance_cmp_st_log(logrid,remittedmonth,region,airportcode,accnttype,PFREMITDATE,PF,INSPREMITDATE,INSPCHARGES,PCREMITDATE,PC,username,lastactive,notes) values  (Station_Wise_remittance_id.Currval,'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"',"+pcRemitDate+",'"+pc+"','"+username+"',sysdate,'"+remarks+"')";
				 }else{
					 insertOrUpdateSqlQry="insert into epis_remittance_cmp_st(rid,remittedmonth,region,airportcode,accnttype,CHQPFREMITDATE,CHQPF,CHQINSPREMITDATE,CHQINSPCHARGES,CHQPCREMITDATE,CHQPC,username,lastactive) values  (Station_Wise_remittance_id.Nextval,'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"','"+pcRemitDate+"','"+pc+"','"+username+"',sysdate)";  
					 insertLogSqlQry="insert into epis_remittance_cmp_st_log(logrid,remittedmonth,region,airportcode,accnttype,CHQPFREMITDATE,CHQPF,CHQINSPREMITDATE,CHQINSPCHARGES,CHQPCREMITDATE,CHQPC,username,lastactive) values  (Station_Wise_remittance_id.Currval,'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"',"+pcRemitDate+",'"+pc+"','"+username+"',sysdate)";
				 }
				 }else{
					 if(!flag.equals("C")){
						 insertOrUpdateSqlQry="update epis_remittance_cmp_st c   set c.accnttype = '"+accType+"',c.PFREMITDATE ='"+pfRemitDate+"',c.PF='"+pf+"',c.INSPREMITDATE='"+inspRemitDate+"',c.INSPCHARGES='"+insp+"',c.PCREMITDATE='"+pcRemitDate+"',c.PC='"+pc+"',c.username='"+username+"',c.notes='"+remarks+"',c.lastactive=sysdate where c.remittedmonth='"+remittanceDate+"' and c.region='"+region+"' and c.airportcode='"+station+"'";
						 insertLogSqlQry="insert into epis_remittance_cmp_st_log(logrid,remittedmonth,region,airportcode,accnttype,PFREMITDATE,PF,INSPREMITDATE,INSPCHARGES,PCREMITDATE,PC,username,lastactive,notes) values  ((select rid  from epis_remittance_cmp_st where region = '"+region+"' and airportcode='"+station+"' and remittedmonth = '"+remittanceDate+"'),'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"','"+pcRemitDate+"','"+pc+"','"+username+"',sysdate,'"+remarks+"')";
					 }else{
						 
						 insertOrUpdateSqlQry="update epis_remittance_cmp_st c   set c.accnttype = '"+accType+"',c.CHQPFREMITDATE ='"+pfRemitDate+"',c.CHQPF='"+pf+"',c.CHQINSPREMITDATE='"+inspRemitDate+"',c.CHQINSPCHARGES='"+insp+"',c.CHQPCREMITDATE='"+pcRemitDate+"',c.CHQPC='"+pc+"',c.username='"+username+"',c.lastactive=sysdate where c.remittedmonth='"+remittanceDate+"' and c.region='"+region+"' and c.airportcode='"+station+"'";
						 insertLogSqlQry="insert into epis_remittance_cmp_st_log(logrid,remittedmonth,region,airportcode,accnttype,CHQPFREMITDATE,CHQPF,CHQINSPREMITDATE,CHQINSPCHARGES,CHQPCREMITDATE,CHQPC,username,lastactive) values  ((select rid  from epis_remittance_cmp_st where region = '"+region+"' and airportcode='"+station+"' and remittedmonth = '"+remittanceDate+"'),'"+remittanceDate+"','"+region+"','"+station+"','"+accType+"','"+pfRemitDate+"','"+pf+"','"+inspRemitDate+"','"+insp+"','"+pcRemitDate+"','"+pc+"','"+username+"',sysdate)";	 
					 }
					 }
			log.info("insertOrUpdateSqlQry"+insertOrUpdateSqlQry);
			log.info("insertLogSqlQry"+insertLogSqlQry);
			 st.addBatch(insertOrUpdateSqlQry);
			 st.addBatch(insertLogSqlQry);
			 int count[]=st.executeBatch();
			 result=count.length;
		 }catch(Exception e){
			 e.printStackTrace();
		 }finally{
			 commonDB.closeConnection(con,st,rs);
		 }
		return result; 
	 }
	public AaiEpfform3Bean  getEmolumentsFrmChalan(String fromDate,String pensionno){

		log.info("EPFFormsReportDAO::getEmolumentsFrmChalan:Entering method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		AaiEpfform3Bean epfForm3Bean = null;
		String   selQuery = "";
		 
		selQuery = "select EMOLUMENTS,PENSIONCONTRI from v_emp_echallan where monthyear='"
				+ fromDate + "' and pensionno ="+pensionno;
	 
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getEmolumentsFrmChalan===query" + selQuery);
			rs = st.executeQuery(selQuery);
			if (rs.next()) {
				epfForm3Bean = new AaiEpfform3Bean();
				if(rs.getString("EMOLUMENTS")!=null){					 
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
				 }else{
						epfForm3Bean.setEmoluments("0");
				 }
				
				if(rs.getString("PENSIONCONTRI")!=null){					 
					epfForm3Bean.setPensionContribution(rs.getString("PENSIONCONTRI"));
				 }else{
						epfForm3Bean.setPensionContribution("0");
				 }
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log
				.info("EPFFormsReportDAO::getEmolumentsFrmChalan:Leaving method");
		return epfForm3Bean;
		
	}
	// By Radha On 09-Oct-2012 for adding -ve pensionContri values given in Supppli data in normal  wages  side
	public ArrayList getForm6AEChallanReport(String range, String region,
			String airprotcode, String empName, String empNameFlag,
			String frmSelctedYear, String sortingOrder, String frmPensionno) {
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		String selPerQuery = "", selQuery = "", fromDate = "", toDate = "", selTransQuery = "", dateOfRetriment = "", days = "",selTransArrearsQuery="",selPerQry1="";
		long transMntYear = 0, empRetriedDt = 0;
		boolean chkDOBFlag = false;
		double retriredEmoluments = 0.0;
		int getDaysBymonth = 0;

		String[] selectedYears = frmSelctedYear.split(",");
		fromDate = selectedYears[0];
		toDate = selectedYears[1];
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		AaiEpfform3Bean epfForm3Bean = null;
		String calEmoluments = "";
		ArrayList form3DataList = new ArrayList();
		ArrayList seperationDataList = new ArrayList();
		ArrayList stationList = new ArrayList();
		String regionDesc = "", staionsStr="", stationDesc = "",remarksAfter58=">58 Years",empRecovryRemarks="",accountType="",station="",pensionCmplteDate="";
		double dojAge=0, pensionAge =0;
		if (!region.equals("NO-SELECT")) {
			regionDesc = region;
		} else {
			regionDesc = "";
		}
		if (!airprotcode.equals("NO-SELECT")) {
			stationDesc = airprotcode;
		} else {
			if(!regionDesc.equals("")){
				staionsStr = commonUtil.getRAUAirports(regionDesc.toUpperCase());
					if((staionsStr.equals("")) && (regionDesc.toUpperCase().equals("CHQNAD"))){
						stationDesc="CHQNAD";
					}else{
						stationDesc=  staionsStr.substring(0, staionsStr.lastIndexOf("','"));
					}
				}else{
			stationDesc = "";
			}
		}
		
		log.info("==region=="+region+"==airprotcode=="+airprotcode+"=regionDesc="+regionDesc+"==stationDesc=="+stationDesc);
		selPerQuery = this.buildQueryForm6AEmpPFInfo(range, regionDesc,
				stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
				fromDate);
		selTransQuery = this.buildQueryEmpPFTransChallan(regionDesc, stationDesc,
				sortingOrder, fromDate, toDate);
		selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO,EMPFID.uanno as uanno,EMPFID.newempcode as newempcode, EPV.CPFACCNO as CPFACCNO, NVL(EMPFID.FHFLAG,'-')  as FHFLAG, EMPFID.GENDER as GENDER, EPV.EMPLOYEENO as EMPLOYEENO, EMPFID.EMPLOYEENAME as EMPLOYEENAME,EMPFID.DATEOFSEPERATION_REASON as DATEOFSEPERATION_REASON,EMPFID.DATEOFSEPERATION_DATE as DATEOFSEPERATION_DATE, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, EMPFID.ACCOUNTTYPE AS  ACCOUNTTYPE,EMPFID.freshoption as freshoption,"
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF,EMPFID.DATEOFENTITLE AS DATEOFENTITLE, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI,EPV.GROSS as GROSS,EPV.PF AS PF,EPV.AEMOLUMENTS as AEMOLUMENTS,EPV.AEMPPFSTATUARY as AEMPPFSTATUARY,EPV.APENSIONCONTRI as APENSIONCONTRI,EPV.APF as APF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.EMPRECOVERYSTS AS EMPRECOVERYSTS,EPV.ARREARFLAG AS ARREARFLAG,EPV.REGSALFLAG AS REGSALFLAG,EMPFID.PENSIONAGE AS PENSIONAGE,EMPFID.PENSIONCMPLTEDATE AS PENSIONCMPLTEDATE, EMPFID.DOJAGE AS DOJAGE, EPV.RECSTS AS RECSTS,EPV.NCPDAYS AS NCPDAYS,nvl(EPV.CPFADDTIONALCONTRI,0) as CPFADDTIONALCONTRI,nvl(EPV.Aaddcontri,0) as Aaddcontri FROM ( "
				+ selTransQuery
				+ ") EPV,("
				+ selPerQuery
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO(+)  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("EPFFormsReportDAO::getForm6AEChallanReport===selQuery"
					+ selQuery);
			FileWriter f=new FileWriter(new File("D:/Prasad.txt"));
			f.write(selQuery);
			f.flush();
			f.close();
			rs = st.executeQuery(selQuery);
			double diff = 0;
			while (rs.next()) {
				station="";
				String regularSalFlag="N";
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0,emoluments=0,pf=0,pensionContri=0,noncpfpc=0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs.getString("PENSIONNO"));

				}
				if (rs.getString("uanno") != null) {
					epfForm3Bean.setUanno(rs.getString("uanno"));

				}
				if (rs.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}
				if (rs.getString("newempcode") != null) {
					epfForm3Bean.setNewEMpCode(rs.getString("newempcode"));
				} else {
					epfForm3Bean.setNewEMpCode("---");
				}
				if (rs.getString("GROSS") != null) {
					epfForm3Bean.setGross(rs.getString("GROSS"));
				} else {
					epfForm3Bean.setGross("---");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				if (rs.getString("ARREARFLAG") != null) {
					epfForm3Bean.setArrearflag(rs.getString("ARREARFLAG"));
				} else {
					epfForm3Bean.setDesignation("N");
				}
				if (rs.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(personalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(commonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs.getString("FRESHOPTION") != null) {
					epfForm3Bean.setFreshOption(rs.getString("FRESHOPTION"));
				} else {
					epfForm3Bean.setFreshOption("---");
				}
				//By Radha On 16-Aug-2012 Req By sehgal
				if(epfForm3Bean.getWetherOption().trim().equals("N") || epfForm3Bean.getWetherOption().trim().equals("DB")){
					epfForm3Bean.setWetherOption("B");
				}
				
				if (rs.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments(rs.getString("EMOLUMENTS"));
					epfForm3Bean.setOriginalEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setOriginalEmoluments("0");
					epfForm3Bean.setEmoluments("0");
				}
				emoluments = Double.parseDouble(epfForm3Bean.getEmoluments());
				if (rs.getString("EMPPFSTATUARY") != null) {

					epfForm3Bean.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				if (rs.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}
				
				if (rs.getString("AEMOLUMENTS") != null) {
					epfForm3Bean.setNonCPFEmoluments(rs.getString("AEMOLUMENTS"));
				} else {
					epfForm3Bean.setNonCPFEmoluments("0.00");
				}
				if (rs.getString("AEMPPFSTATUARY") != null) {
					epfForm3Bean.setNonCPFemppfstatury(rs.getString("AEMPPFSTATUARY"));
				} else {
					epfForm3Bean.setNonCPFemppfstatury("0.00");
				}
				if (rs.getString("APENSIONCONTRI") != null) {
					epfForm3Bean.setNonCPFPC(rs.getString("APENSIONCONTRI"));
				} else {
					epfForm3Bean.setNonCPFPC("0.00");
				}
				noncpfpc = Double.parseDouble(epfForm3Bean.getNonCPFPC());
				if (rs.getString("APF") != null) {
					epfForm3Bean.setNonCPFPF(rs.getString("APF"));
				} else {
					epfForm3Bean.setNonCPFPF("0.00");
				}

				
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));

				if (rs.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution(rs
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
				}
				pensionContri = Double.parseDouble(epfForm3Bean.getPensionContribution());
				
				if (rs.getString("CPFADDTIONALCONTRI") != null) {
					epfForm3Bean.setAdditionalContri(rs
							.getString("CPFADDTIONALCONTRI"));
				} else {
					epfForm3Bean.setAdditionalContri("0.00");
				}
				if (rs.getString("Aaddcontri") != null) {
					epfForm3Bean.setNonCpfAddcontri(rs
							.getString("Aaddcontri"));
				} else {
					epfForm3Bean.setNonCpfAddcontri("0.00");
				}
				// log.info("Pension
				// No"+epfForm3Bean.getPensionno()+"Emoluments"
				// +epfForm3Bean.getEmoluments()+"Pension Contribution"+
				// epfForm3Bean.getPensionContribution());

				log.info("===pensionno=="+epfForm3Bean.getPensionno()+"==monthyear=="+epfForm3Bean.getMonthyear()+"==PC=="+epfForm3Bean.getNonCPFPC()+"==Emoluemnsy=="+epfForm3Bean.getNonCPFEmoluments());
				if (rs.getString("PF") != null) {
					epfForm3Bean.setPf(rs.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
				}
				if (rs.getString("REGSALFLAG") != null) {
					regularSalFlag = rs.getString("REGSALFLAG");
				}  
				if(regularSalFlag.equals("Y")){
					epfForm3Bean.setEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setOriginalEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setEmppfstatury(String.valueOf(pfstatutury+Double.parseDouble(epfForm3Bean.getNonCPFemppfstatury())));
					epfForm3Bean.setPensionContribution(String.valueOf(pensionContri+Double.parseDouble(epfForm3Bean.getNonCPFPC())));
					epfForm3Bean.setPf(String.valueOf(pf+Double.parseDouble(epfForm3Bean.getNonCPFPF())));
					//epfForm3Bean.setAdditionalContri(String.valueOf(Double.parseDouble(epfForm3Bean.getAdditionalContri())+Double.parseDouble(epfForm3Bean.getNonCpfAddcontri())));
					epfForm3Bean.setNonCPFEmoluments("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
					epfForm3Bean.setNonCpfAddcontri("0.00");
				}
            //By Radha On 09-Nov-2012 for adding -ve pensionContri values given in Supppli data in normal  wages  side  other than regularSalFlag
			//By Radha On 09-Oct-2012 for adding -ve pensionContri values given in Supppli data in normal  wages  side
				if(noncpfpc <0  && emoluments !=0 && regularSalFlag.equals("N")){
					
					epfForm3Bean.setEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setOriginalEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setEmppfstatury(String.valueOf(pfstatutury+Double.parseDouble(epfForm3Bean.getNonCPFemppfstatury())));
					epfForm3Bean.setPensionContribution(String.valueOf(pensionContri+Double.parseDouble(epfForm3Bean.getNonCPFPC())));
					epfForm3Bean.setPf(String.valueOf(pf+Double.parseDouble(epfForm3Bean.getNonCPFPF())));
					epfForm3Bean.setAdditionalContri(String.valueOf(Double.parseDouble(epfForm3Bean.getAdditionalContri())+Double.parseDouble(epfForm3Bean.getNonCpfAddcontri())));
					epfForm3Bean.setNonCPFEmoluments("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
					epfForm3Bean.setNonCpfAddcontri("0.00");
					
				}
			
				
				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				
				if (rs.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs.getString("ACCOUNTTYPE");
					
				}  
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						station=epfForm3Bean.getStation();
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				}
				 
				if (rs.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs.getString("PENSIONAGE"));
					pensionAge = Double.parseDouble(rs.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("");
				}
				if (rs.getString("PENSIONCMPLTEDATE") != null) {				 
					pensionCmplteDate = rs.getString("PENSIONCMPLTEDATE");
				} else {
					pensionCmplteDate="---";
				}
				
				
				
				if (rs.getString("DOJAGE") != null) {
					dojAge  = Double.parseDouble(rs.getString("DOJAGE"));
				} 
				
				if (rs.getString("EMPRECOVERYSTS") != null) {
					if(rs.getString("EMPRECOVERYSTS").equals("CPF")){
						empRecovryRemarks="Regular";
					}else if(rs.getString("EMPRECOVERYSTS").equals("DEP")){
						empRecovryRemarks="Deputation";
					}else if(rs.getString("EMPRECOVERYSTS").equals("RET")){
						empRecovryRemarks="Retirement";
					}
					epfForm3Bean.setRemarks(empRecovryRemarks);
				} else{
					epfForm3Bean.setRemarks("");
				} 
				
				if (rs.getString("RECSTS") != null) {					 
					epfForm3Bean.setRecoveryStatus(rs.getString("RECSTS"));
				} else{
					epfForm3Bean.setRecoveryStatus("");
				}
				
				calEmoluments = finReportDAO.calWages(epfForm3Bean
						.getEmoluments(), epfForm3Bean.getMonthyear(),
						epfForm3Bean.getWetherOption().trim(), false, false,
						"1");
				transMntYear = Date.parse(epfForm3Bean.getMonthyear());
				dateOfRetriment = commonDAO.getRetriedDate(epfForm3Bean
						.getDateOfBirth());
				log.info("3333333"+calEmoluments);

				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear == empRetriedDt) {

					chkDOBFlag = true;
				}
				if (chkDOBFlag == true) {
					String[] dobList = epfForm3Bean.getDateOfBirth().split("-");
					days = dobList[0];

					getDaysBymonth = commonDAO.getNoOfDays(epfForm3Bean
							.getDateOfBirth());

					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
				}
				log.info("4444444"+calEmoluments);
				epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
						.parseDouble(calEmoluments))));
				chkDOBFlag=false;
			
				// By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				
				// Changed on 15-May-2012 as per Sehgal 
				if(!(dojAge==0 || pensionAge==58 )){
					epfForm3Bean.setDateOfBirth("---");
					epfForm3Bean.setDateofJoining("---");
					//epfForm3Bean.setFhName("---");					 
					epfForm3Bean.setGender("---");
					//epfForm3Bean.setFhflag("---");
					epfForm3Bean.setDateofentitle("---");
				}
				
				if(dojAge==0){					 
					epfForm3Bean.setSeperationreason("---");
					epfForm3Bean.setSeperationDate("---");
				} 
				
				if(pensionAge==58){	
					if(!epfForm3Bean.getSeperationreason().equals("Death")){
					epfForm3Bean.setSeperationreason("SuperAnnuation");
					}
					//Added on 15-Jun-2012 as per Seghal
					epfForm3Bean.setSeperationDate(pensionCmplteDate);
				} 
				
				epfForm3Bean.setDaysBymonth(String.valueOf(getDaysBymonth));
				if (rs.getString("NCPDAYS") != null) {
					epfForm3Bean.setNCPDays(rs.getString("NCPDAYS"));

				}else{
					epfForm3Bean.setNCPDays("0");
				}
				
				if(!epfForm3Bean.getSeperationDate().equals("---")){
					seperationDataList.add(epfForm3Bean.getPensionno());
				}
				//By Radha On 16-Aug-2012 For not Showing Employee  reached above  58 Years  Req By Seghal
				
				if(pensionAge<=58){
				form3DataList.add(epfForm3Bean);
				}

			}
			
			//for >58 data
			String selPerQryAftr58 = this.buildQueryForm6AEmpPFAfter58Info(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
			selTransArrearsQuery = this.buildQueryEmpPFTransArrearInfo(regionDesc, stationDesc,
					sortingOrder, fromDate, toDate);
			
//			Here  DATAFLAG is for showing the Supplidata when sal month is May and Paid date is Jun  as Normal Wages as per Sehgal Request
			selQuery = "SELECT EMPFID.PENSIONNO as PENSIONNO,EMPFID.uanno as uanno,EMPFID.newempcode as newempcode,EPV.CPFACCNO as CPFACCNO, NVL(EMPFID.FHFLAG,'-') as FHFLAG, EMPFID.GENDER as GENDER, EPV.EMPLOYEENO as EMPLOYEENO,EMPFID.DATEOFENTITLE AS DATEOFENTITLE,EMPFID.DATEOFSEPERATION_REASON as DATEOFSEPERATION_REASON,EMPFID.DATEOFSEPERATION_DATE as DATEOFSEPERATION_DATE, EMPFID.EMPLOYEENAME as EMPLOYEENAME, EMPFID.DATEOFJOINING as DATEOFJOINING, EMPFID.DESEGNATION as DESEGNATION,EMPFID.FHNAME as FHNAME, EMPFID.DATEOFBIRTH as DATEOFBIRTH, EMPFID.WETHEROPTION as WETHEROPTION, EMPFID.ACCOUNTTYPE AS ACCOUNTTYPE, "
				+ "EPV.MONTHYEAR AS MONTHYEAR, ROUND(EPV.EMOLUMENTS) AS EMOLUMENTS, EPV.EMPPFSTATUARY AS EMPPFSTATUARY, EPV.EMPVPF AS EMPVPF, EPV.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL, EPV.EMPADVRECINTEREST AS EMPADVRECINTEREST, "
				+ "EPV.PENSIONCONTRI AS PENSIONCONTRI,EPV.GROSS as GROSS,EPV.PF AS PF, EPV.AIRPORTCODE AS AIRPORTCODE, EPV.REGION AS REGION, EPV.CPFACCNO AS CPFACCNO,EPV.ARREARFLAG AS ARREARFLAG,EPV.REGSALFLAG AS REGSALFLAG, EPV.EMPRECOVERYSTS AS EMPRECOVERYSTS,EMPFID.PENSIONAGE AS PENSIONAGE,EMPFID.freshoption as freshoption,nvl(EPV.additionalcontri,0) as additionalcontri  FROM ( "
				+ selTransArrearsQuery
				+ ") EPV,("
				+ selPerQryAftr58
				+ ") EMPFID WHERE EMPFID.PENSIONNO = EPV.PENSIONNO  AND EPV.EMPFLAG = 'Y' ORDER BY EMPFID."
				+ sortingOrder;
	 
			 
			st1 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFInfo==  For SuppliData=selQuery"
					+ selQuery);
			rs1 = st1.executeQuery(selQuery);
		 
			while (rs1.next()) {
				station="";
				String regularSalFlag="N";
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0,emoluments=0,pf=0,pensionContri=0;
				epfForm3Bean = new AaiEpfform3Bean();
				if (rs1.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs1.getString("PENSIONNO"));

				}
				if (rs1.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs1.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs1.getString("uanno") != null) {
					epfForm3Bean.setUanno(rs1.getString("uanno"));

				}
				if (rs1.getString("CPFACCNO") != null) {
					epfForm3Bean.setCpfaccno(rs1.getString("CPFACCNO"));
				} else {
					epfForm3Bean.setCpfaccno("---");
				}
				if (rs1.getString("newempcode") != null) {
					epfForm3Bean.setNewEMpCode(rs1.getString("newempcode"));
				} else {
					epfForm3Bean.setNewEMpCode("---");
				}
				if (rs1.getString("GROSS") != null) {
					epfForm3Bean.setGross(rs1.getString("GROSS"));
				} else {
					epfForm3Bean.setGross("---");
				}
				if (rs1.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs1.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs1.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs1.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				if (rs1.getString("ARREARFLAG") != null) {
					epfForm3Bean.setArrearflag(rs1.getString("ARREARFLAG"));
				} else {
					epfForm3Bean.setDesignation("N");
				}
				if (rs1.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs1.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs1.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs1.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs1.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs1.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs1.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs1.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs1.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs1.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs1.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs1.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs1.getString("FRESHOPTION") != null) {
					epfForm3Bean.setFreshOption(rs1.getString("FRESHOPTION"));
				} else {
					epfForm3Bean.setFreshOption("---");
				}
				if (rs1.getString("MONTHYEAR") != null) {
					epfForm3Bean.setMonthyear(CommonUtil.converDBToAppFormat(rs1
							.getDate("MONTHYEAR")));
				} else {
					epfForm3Bean.setMonthyear("---");
				}
				if (rs1.getString("EMOLUMENTS") != null) {
					epfForm3Bean.setEmoluments("0");
					epfForm3Bean.setOriginalEmoluments("0");
					epfForm3Bean.setNonCPFEmoluments(rs1.getString("EMOLUMENTS"));
				} else {
					epfForm3Bean.setOriginalEmoluments("0");
					epfForm3Bean.setEmoluments("0");
					epfForm3Bean.setNonCPFEmoluments("0");
				}
				emoluments = Double.parseDouble(epfForm3Bean.getEmoluments());
				if (rs1.getString("EMPPFSTATUARY") != null) {

					epfForm3Bean.setEmppfstatury("0.00");
					epfForm3Bean.setNonCPFemppfstatury(rs1.getString("EMPPFSTATUARY"));
				} else {
					epfForm3Bean.setEmppfstatury("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
				}
				pfstatutury = Double
						.parseDouble(epfForm3Bean.getEmppfstatury());
				if (rs1.getString("EMPVPF") != null) {
					epfForm3Bean.setEmpvpf(rs1.getString("EMPVPF"));
				} else {
					epfForm3Bean.setEmpvpf("0.00");
				}
				if (rs1.getString("EMPADVRECPRINCIPAL") != null) {
					epfForm3Bean.setPrincipal(rs1
							.getString("EMPADVRECPRINCIPAL"));
				} else {
					epfForm3Bean.setPrincipal("0.00");
				}
				if (rs1.getString("EMPADVRECINTEREST") != null) {
					epfForm3Bean.setInterest(rs1.getString("EMPADVRECINTEREST"));
				} else {
					epfForm3Bean.setInterest("0.00");
				}

				

				if (rs1.getString("PENSIONCONTRI") != null) {
					epfForm3Bean.setPensionContribution("0.00");
					epfForm3Bean.setNonCPFPC(rs1
							.getString("PENSIONCONTRI"));
				} else {
					epfForm3Bean.setPensionContribution("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
				}
				if (rs1.getString("additionalcontri") != null) {
					epfForm3Bean.setAdditionalContri(rs1
							.getString("additionalcontri"));
				} else {
					epfForm3Bean.setAdditionalContri("0.00");
				}
				pensionContri = Double.parseDouble(epfForm3Bean.getPensionContribution());
				// log.info("Pension
				// No"+epfForm3Bean.getPensionno()+"Emoluments"
				// +epfForm3Bean.getEmoluments()+"Pension Contribution"+
				// epfForm3Bean.getPensionContribution());

				if (rs1.getString("PF") != null) {
					epfForm3Bean.setPf("0.00");
					epfForm3Bean.setNonCPFPF(rs1
							.getString("PF"));
				} else {
					epfForm3Bean.setPf("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
				}
				
				if (rs1.getString("REGSALFLAG") != null) {
					regularSalFlag = rs1.getString("REGSALFLAG");
				}  
				if(regularSalFlag.equals("Y")){
					epfForm3Bean.setEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setOriginalEmoluments(String.valueOf(emoluments+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments())));
					epfForm3Bean.setEmppfstatury(String.valueOf(pfstatutury+Double.parseDouble(epfForm3Bean.getNonCPFemppfstatury())));
					epfForm3Bean.setPensionContribution(String.valueOf(pensionContri+Double.parseDouble(epfForm3Bean.getNonCPFPC())));
					epfForm3Bean.setAdditionalContri(String.valueOf(Double.parseDouble(epfForm3Bean.getAdditionalContri())));
					epfForm3Bean.setPf(String.valueOf(pf+Double.parseDouble(epfForm3Bean.getNonCPFPF())));
					epfForm3Bean.setNonCPFEmoluments("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
				}
				
				
				subTotal = new Double(df
						.format(Double.parseDouble(epfForm3Bean
								.getEmppfstatury().trim())
								+ Double.parseDouble(epfForm3Bean.getEmpvpf()
										.trim())
								+ Double.parseDouble(epfForm3Bean
										.getPrincipal().trim())
								+ Double.parseDouble(epfForm3Bean.getInterest()
										.trim()))).doubleValue();
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));
				
				
				conTotal = new Double(df.format(Double.parseDouble(epfForm3Bean
						.getPensionContribution().trim())
						+ Double.parseDouble(epfForm3Bean.getPf().trim())))
						.doubleValue();

				epfForm3Bean.setContributionTotal(Double.toString(conTotal));
				diff = pfstatutury - conTotal;
				epfForm3Bean.setFormDifference(Double
						.toString(Math.round(diff)));
				if (diff != 0) {
					epfForm3Bean.setHighlightedColor("");
				} else {
					epfForm3Bean.setHighlightedColor("");
				}
				
				if (rs1.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs1.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs1.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs1
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				
				if (rs1.getString("EMPRECOVERYSTS") != null) {
					  if(rs1.getString("EMPRECOVERYSTS").equals("DEP")){
						  epfForm3Bean.setRecoveryStatus(rs1.getString("EMPRECOVERYSTS"));
					} 					 
				} else{
					epfForm3Bean.setRecoveryStatus("");
				} 
				
				if (rs1.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs1.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs1.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs1.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs1.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs1.getString("ACCOUNTTYPE");
					
				}  
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						station=epfForm3Bean.getStation();
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				}
				if (rs1.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs1.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("");
				}
				calEmoluments = finReportDAO.calWages(epfForm3Bean
						.getEmoluments(), epfForm3Bean.getMonthyear(),
						epfForm3Bean.getWetherOption().trim(), false, false,
						"1");
				transMntYear = Date.parse(epfForm3Bean.getMonthyear());
				dateOfRetriment = commonDAO.getRetriedDate(epfForm3Bean
						.getDateOfBirth());

				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear == empRetriedDt) {

					chkDOBFlag = true;
				}
				if (chkDOBFlag == true) {
					String[] dobList = epfForm3Bean.getDateOfBirth().split("-");
					days = dobList[0];

					getDaysBymonth = commonDAO.getNoOfDays(epfForm3Bean
							.getDateOfBirth());

					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
				}
				epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
						.parseDouble(calEmoluments))));
				chkDOBFlag=false;
				
				epfForm3Bean.setRemarks(remarksAfter58);      
				
				//By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				
				//Changed on 15-May-2012 as per Sehgal 
				epfForm3Bean.setDateOfBirth("---");
				epfForm3Bean.setDateofJoining("---");
				//epfForm3Bean.setFhName("---");					 
				epfForm3Bean.setGender("---");
				//epfForm3Bean.setFhflag("---"); 	
				// As on 12-Jul-2012 For 2 cases 7992,7982 Remitted & Recocerved
				//epfForm3Bean.setRecoveryStatus("");
				 
				epfForm3Bean.setDaysBymonth(String.valueOf(getDaysBymonth));
				
				
				
				if(!epfForm3Bean.getSeperationDate().equals("---")){
					seperationDataList.add(epfForm3Bean.getPensionno());
				}
				epfForm3Bean.setNCPDays("0");
				form3DataList.add(epfForm3Bean);

			}
			//New Joinees
			commonDB.closeConnection(null,st1,rs1);
			String newJoineeFlag="";
			String selPerNewJoinee = this.buildQueryForm6AEmpPFNewJoineeInfo(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
				st1 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6AEmpPFNewJoineeInfo==  For New Joiness =selQuery"
					+ selPerNewJoinee);
			rs1 = st1.executeQuery(selPerNewJoinee);
		 
			while (rs1.next()) {
				station="";
				 	epfForm3Bean = new AaiEpfform3Bean();
				if (rs1.getString("PENSIONNO") != null) {
					epfForm3Bean.setPensionno(rs1.getString("PENSIONNO"));
					 
				}
				if (rs1.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs1.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				
				if (rs1.getString("GENDER") != null) {
					epfForm3Bean.setGender(rs1.getString("GENDER"));
				} else {
					epfForm3Bean.setGender("---");
				}
				if (rs1.getString("FHFLAG") != null) {
					epfForm3Bean.setFhflag(rs1.getString("FHFLAG"));
				} else {
					epfForm3Bean.setFhflag("---");
				}
				
				if (rs1.getString("EMPLOYEENO") != null) {
					epfForm3Bean.setEmployeeNo(rs1.getString("EMPLOYEENO"));
				} else {
					epfForm3Bean.setEmployeeNo("---");
				}
				if (rs1.getString("EMPLOYEENAME") != null) {
					epfForm3Bean.setEmployeeName(rs1.getString("EMPLOYEENAME"));
				} else {
					epfForm3Bean.setEmployeeName("---");
				}
				if (rs1.getString("DESEGNATION") != null) {
					epfForm3Bean.setDesignation(rs1.getString("DESEGNATION"));
				} else {
					epfForm3Bean.setDesignation("---");
				}
				if (rs1.getString("FHNAME") != null) {
					epfForm3Bean.setFhName(rs1.getString("FHNAME"));
				} else {
					epfForm3Bean.setFhName("---");
				}
				if (rs1.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				if (rs1.getString("DATEOFJOINING") != null) {
					epfForm3Bean.setDateofJoining(CommonUtil
							.converDBToAppFormat(rs1.getDate("DATEOFJOINING")));
				} else {
					epfForm3Bean.setDateofJoining("---");
				}
				if (rs1.getString("WETHEROPTION") != null) {
					epfForm3Bean.setWetherOption(rs1.getString("WETHEROPTION"));
				} else {
					epfForm3Bean.setWetherOption("---");
				}
				if (rs1.getString("FRESHOPTION") != null) {
					epfForm3Bean.setFreshOption(rs1.getString("FRESHOPTION"));
				} else {
					epfForm3Bean.setFreshOption("---");
				}
				
					epfForm3Bean.setMonthyear("---");
				
			
					epfForm3Bean.setEmoluments("0.00");
					epfForm3Bean.setOriginalEmoluments("0.00");
					epfForm3Bean.setNonCPFEmoluments("0.00");
				
					epfForm3Bean.setEmppfstatury("0.00");
					epfForm3Bean.setNonCPFemppfstatury("0.00");
				
			
					epfForm3Bean.setEmpvpf("0.00");
					epfForm3Bean.setPrincipal("0.00");
					epfForm3Bean.setInterest("0.00");
					epfForm3Bean.setSubscriptionTotal("0.00");

					epfForm3Bean.setPensionContribution("0.00");
					epfForm3Bean.setNonCPFPC("0.00");
					epfForm3Bean.setPf("0.00");
					epfForm3Bean.setNonCPFPF("0.00");
					
					epfForm3Bean.setAdditionalContri("0.00");
					epfForm3Bean.setNonCpfAddcontri("0.00");
			
				if (rs1.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs1.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs1.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(commonUtil.converDBToAppFormat(rs1
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				
				if (rs1.getString("AIRPORTCODE") != null) {
					epfForm3Bean.setStation(rs1.getString("AIRPORTCODE"));
				} else {
					epfForm3Bean.setStation("---");
				}
				if (rs1.getString("REGION") != null) {
					epfForm3Bean.setRegion(rs1.getString("REGION"));
				} else {
					epfForm3Bean.setRegion("---");
				}
				if (rs1.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs1.getString("ACCOUNTTYPE");
					
				}  
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						station=epfForm3Bean.getStation();
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				}
				if (rs1.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs1.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("");
				}
				if (rs1.getString("DTRECEVIEDNEWJOINEE") != null) {
					newJoineeFlag=rs1.getString("DTRECEVIEDNEWJOINEE");
				} else {
					newJoineeFlag="Y";
				}
				
				
				epfForm3Bean.setEmoluments("0.00");
				epfForm3Bean.setOriginalEmoluments("0.00");
				
				epfForm3Bean.setFormDifference("0.00");
				epfForm3Bean.setRemarks(remarksAfter58);      
				
				//By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				//Changed on 15-May-2012 as per Sehgal 
				epfForm3Bean.setDateOfBirth("---");
				epfForm3Bean.setDateofJoining("---");
				//epfForm3Bean.setFhName("---");					 
				epfForm3Bean.setGender("---");
				//epfForm3Bean.setFhflag("---"); 					 
				epfForm3Bean.setRecoveryStatus("");
				epfForm3Bean.setNCPDays("0");
				epfForm3Bean.setDaysBymonth(String.valueOf(getDaysBymonth));
				 
					if(!epfForm3Bean.getSeperationDate().equals("---")){
						seperationDataList.add(epfForm3Bean.getPensionno());
					}
					
					if (newJoineeFlag.equals("N")){
						form3DataList.add(epfForm3Bean);
					}
					
				

			}
			  // Form 5 Data
			selQuery = this.buildQueryForm6ASeperationInfo(range, regionDesc,
					stationDesc, empNameFlag, empName, sortingOrder, frmPensionno,
					fromDate);
			st2 = con.createStatement();
			log.info("EPFFormsReportDAO::buildQueryForm6ASeperationInfo==  For Seperation=selQuery"
					+ selQuery);
			rs2 = st2.executeQuery(selQuery);
		 
			while (rs2.next()) {
				double subTotal = 0.0, conTotal = 0.0, pfstatutury = 0;
				String pensionno="",cpfAccNo="",employeename="",seperationDate="",seperationReason="",wetheroption="" ;
				epfForm3Bean = new AaiEpfform3Bean();
				station="";
				if (rs2.getString("PENSIONNO") != null) {
					pensionno = rs2.getString("PENSIONNO") ;

				}
				if (rs2.getString("CPFACNO") != null) {
					cpfAccNo = rs2.getString("CPFACNO") ;
				} else {
					cpfAccNo = "---";
				}
				
				if (rs2.getString("EMPLOYEENAME") != null) {
					employeename =rs2.getString("EMPLOYEENAME");
				} else {
					employeename = "---";
				}
				
				if (rs2.getString("AIRPORTCODE") != null) {
					station = rs2.getString("AIRPORTCODE") ;
				} else {
					station = "---" ;
				}
				if (rs2.getString("WETHEROPTION") != null) {
					wetheroption=rs2.getString("WETHEROPTION");
				} else {
					wetheroption="---";
				}
				if (rs2.getString("FRESHOPTION") != null) {
					epfForm3Bean.setFreshOption(rs2.getString("FRESHOPTION"));
				} else {
					epfForm3Bean.setFreshOption("---");
				}
				if (rs2.getString("REGION") != null) {
					region = rs2.getString("REGION");
				} else {
					region =  "---" ;
				}
				if (rs2.getString("ACCOUNTTYPE") != null) {					 
					accountType = rs2.getString("ACCOUNTTYPE");
					
				}  
				
				//By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				//On 15-Jun-2012 as per Sehgal
				AaiEpfform3Bean  form5Data = new AaiEpfform3Bean();  
				form5Data = getEmolumentsFrmChalan(fromDate,pensionno);
				if(form5Data!=null){
					calEmoluments = finReportDAO.calWages(form5Data.getEmoluments(), fromDate,
							wetheroption.trim(), false, false,"1");
					epfForm3Bean.setEmoluments(Double.toString(Math.round(Double
							.parseDouble(calEmoluments))));					 
					epfForm3Bean.setPensionContribution(form5Data.getPensionContribution());
				}else{
					epfForm3Bean.setEmoluments("0");
					epfForm3Bean.setPensionContribution("0.00");
				}
				 
				epfForm3Bean.setPensionno(pensionno);
				epfForm3Bean.setCpfaccno(cpfAccNo);
				epfForm3Bean.setEmployeeName(employeename);
				//epfForm3Bean.setDateofentitle("---");
				epfForm3Bean.setGender("---");
				//epfForm3Bean.setFhflag("---");
				epfForm3Bean.setArrearflag("N");
				epfForm3Bean.setEmployeeNo("---");
				epfForm3Bean.setDesignation("---");
				//epfForm3Bean.setFhName("---");
				epfForm3Bean.setWetherOption(wetheroption);
				 
				
				
				
				epfForm3Bean.setOriginalEmoluments("0");
				epfForm3Bean.setEmppfstatury("0.00");
				epfForm3Bean.setNonCPFEmoluments("0.00");
				epfForm3Bean.setEmpvpf("0.00");
				epfForm3Bean.setPrincipal("0.00");
				epfForm3Bean.setInterest("0.00");
				epfForm3Bean.setSubscriptionTotal(Double.toString(subTotal));
				
				epfForm3Bean.setNonCPFPC("0.00");
				epfForm3Bean.setPf("0.00");
				epfForm3Bean.setNonCPFPF("0.00");
				epfForm3Bean.setContributionTotal(Double.toString(conTotal));					 
				epfForm3Bean.setFormDifference(Double.toString(Math.round(diff)));
				
				epfForm3Bean.setHighlightedColor("");
				
				epfForm3Bean.setRegion(region);
				epfForm3Bean.setStation(station);
				epfForm3Bean.setAdditionalContri("0.00");
				if(!epfForm3Bean.getRegion().equals("CHQIAD") && !epfForm3Bean.getRegion().equals("CHQNAD")){
					if(accountType.equals("SAU")){
						epfForm3Bean.setStation(accountType+"-"+station);
					}
				} 
				
				if (rs2.getString("DATEOFBIRTH") != null) {
					epfForm3Bean.setDateOfBirth(CommonUtil
							.converDBToAppFormat(rs2.getDate("DATEOFBIRTH")));
				} else {
					epfForm3Bean.setDateOfBirth("---");
				}
				if (!epfForm3Bean.getDateOfBirth().equals("---")) {
					String pers1onalPFID = commonDAO.getPFID(epfForm3Bean
							.getEmployeeName(), epfForm3Bean.getDateOfBirth(),
							commonUtil.leadingZeros(5, epfForm3Bean
									.getPensionno()));
					epfForm3Bean.setPfID(pers1onalPFID);
				} else {
					epfForm3Bean.setPfID(epfForm3Bean.getPensionno());
				}
				epfForm3Bean.setDateofJoining("---"); 
				 
					epfForm3Bean.setMonthyear("---");
				 
				
				if (rs2.getString("DATEOFENTITLE") != null) {
					epfForm3Bean.setDateofentitle(rs2.getString("DATEOFENTITLE"));
					//EDLI report purpose
					epfForm3Bean.setEdliDateOfJoining(rs2.getString("DATEOFENTITLE"));
				} else {
					epfForm3Bean.setDateofentitle("---");
				}
				if (rs2.getString("DATEOFSEPERATION_REASON") != null) {
					epfForm3Bean.setSeperationreason(rs2.getString("DATEOFSEPERATION_REASON"));
				} else {
					epfForm3Bean.setSeperationreason("---");
				}
				if (rs2.getString("DATEOFSEPERATION_DATE") != null) {
					epfForm3Bean.setSeperationDate(CommonUtil.converDBToAppFormat(rs2
							.getDate("DATEOFSEPERATION_DATE")));
				} else {
					epfForm3Bean.setSeperationDate("---");
				}
				//epfForm3Bean.setEmoluments("0.00");
				epfForm3Bean.setOriginalEmoluments("0.00");

				epfForm3Bean.setRemarks(""); 
				if (rs2.getString("PENSIONAGE") != null) {
					epfForm3Bean.setPensionAge(rs2.getString("PENSIONAGE"));
				} else {
					epfForm3Bean.setPensionAge("0");
				}
				epfForm3Bean.setDaysBymonth("");
				epfForm3Bean.setRecoveryStatus("");
				epfForm3Bean.setNCPDays("0");
				
//				By Prasad on 15-Jun-2012 for EDLI Report Purpose
				epfForm3Bean.setEdliDateOfBirth(epfForm3Bean.getDateOfBirth());
				//epfForm3Bean.setEdliDateOfJoining(epfForm3Bean.getDateofJoining());
				
				String ChkFlag="false";
				for(int i=0;i<seperationDataList.size();i++){
				
					if(seperationDataList.get(i).equals(pensionno)){
						ChkFlag="true";
					}
				}
				//log.info("=====In ChkFlag ======"+ChkFlag+"===pensionno====="+pensionno);
				if(ChkFlag.equals("false")){
					form3DataList.add(epfForm3Bean);
				}
				

			}
			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st1, rs1);
			commonDB.closeConnection(null, st2, rs2);
			commonDB.closeConnection(con, st, rs);
		}
		return form3DataList;
	}
	
	public String buildQueryFreezedChallan(String range, String region,
			String airportcode, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String fromDate ) {
		log.info("EPFFormsReportDAO::buildQueryFreezedChallan-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT ECR.PENSIONNO as PENSIONNO, INFO.CPFACNO as CPFACCNO,  NVL(ECR.FHFLAG, '-') as FHFLAG,   ECR.GENDER as GENDER, INFO.EMPLOYEENO as EMPLOYEENO,  ECR.EMPLOYEENAME as EMPLOYEENAME,   ECR.SEPERATIONREASON as DATEOFSEPERATION_REASON, ECR.SEPERATIONDT as DATEOFSEPERATION_DATE,"
					+" ECR.DATEOFJOINING as DATEOFJOINING, ECR.FHNAME as FHNAME, ECR.DATEOFBIRTH as DATEOFBIRTH,  ECR.WETHEROPTION as WETHEROPTION ,ECR.MONTHYEAR AS MONTHYEAR, ECR.DATEOFENTITLE AS DATEOFENTITLE,  ROUND(ECR.EMOLUMENTS) AS EMOLUMENTS,  ECR.PC AS PENSIONCONTRI,  ECR.NONCPFEMOLUMENTS as AEMOLUMENTS,"
					+"ECR.NONCPFPC as APENSIONCONTRI, ECR.AIRPORTCODE AS AIRPORTCODE, ECR.REGION AS REGION, INFO.CPFACNO AS CPFACCNO, ECR.NCPDAYS AS NCPDAYS,ECR.REASONFORLEAVING AS REASONFORLEAVING   FROM EPIS_ECR_FROZEN_MONTHS_DT ECR, EMPLOYEE_PERSONAL_INFO INFO  WHERE ECR.PENSIONNO = INFO.PENSIONNO AND MONTHYEAR = '"+fromDate+"' ";

		/*if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");

		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		 
		if (!region.equals("")) { 
			whereClause.append("UPPER(EUM.REGION) = '" + region.toUpperCase() + "'");
			whereClause.append(" AND "); 
	   }
		
		if (!airportcode.equals("")) { 
				whereClause.append("UPPER(EUM.UNITNAME) IN ('" + airportcode + "')");
				whereClause.append(" AND "); 
		}
		query.append(sqlQuery);
		if ( (region.equals("")) && (airportcode.equals("")) && (range
				.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY SRLNO "  ;
		query.append(orderBy);
		dynamicQuery = query.toString();*/
		
		if (!region.equals("")) {
			whereClause.append(" upper(ECR.REGION) ='" +   region.toUpperCase() + "'");
			whereClause.append(" AND ");
		}
		if (!airportcode.equals("")) {
			whereClause.append(" upper(ECR.AIRPORTCODE) in( '" + airportcode.toUpperCase() + "')");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(commonDAO.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY SRLNO " ;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("EPFFormsReportDAO::buildQueryFreezedChallan Leaving Method");
		return dynamicQuery;
		 
	}
	
}
