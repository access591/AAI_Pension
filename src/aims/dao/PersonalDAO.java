package aims.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;

import aims.bean.AdjExtractData;
import aims.bean.DatabaseBean;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeeAdlPensionInfo;
import aims.bean.EmployeeFreshOptionBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.NomineeBean;
import aims.bean.RPFCForm9Bean;
import aims.bean.RegionBean;
import aims.bean.form3Bean;
import aims.common.CommonUtil;
import aims.common.Constants;
import aims.common.DBUtils;
import aims.common.DateValidation;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.common.StringUtility;

/* 
 ##########################################
 #Date					Developed by			Issue description
 #17-Aug-2012			Radha					For PFID Creation Request Processing 
 #24-Feb-2012			Radha					Correcting the changes due to seperation of methods  from FinancialReportDAO to CommonDAO
 #########################################
 */
public class PersonalDAO {
	Log log = new Log(PersonalDAO.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	FinancialDAO financeDAO = new FinancialDAO();

	PensionDAO pensionDAO = new PensionDAO();

	CommonDAO commonDAO = new CommonDAO();

	public String autoProcessingPersonalInfo(String selectedDate,
			String retiredDate, HashMap regionMap, String selectedairportCode,
			String userName, String ipName) throws IOException {
		ArrayList airportList = new ArrayList();
		String lastactive = "", region = "", airportCode = "", message = "";
		ArrayList form3List = new ArrayList();
		Set set = regionMap.entrySet();
		Iterator iter = set.iterator();
		ArrayList regionList = new ArrayList();
		RegionBean regionInfo = new RegionBean();
		int totalFailed = 0, totalInserted = 0, totalRecords = 0, form3Cnt = 0;
		Connection con = null;
		try {
			con = DBUtils.getConnection();
			BufferedWriter fw = new BufferedWriter(new FileWriter(
					"c://missingImportData.txt"));
			BufferedWriter fw1 = new BufferedWriter(new FileWriter(
					"c://chqiadData.txt"));
			lastactive = commonUtil.getCurrentDate("dd-MMM-yyyy");
			regionList = commonUtil.loadRegions();

			boolean chkMnthYearFlag = false;

			for (int rg = 0; rg < regionList.size(); rg++) {
				regionInfo = (RegionBean) regionList.get(rg);

				if (selectedairportCode.equals("NO-SELECT")) {
					if (regionInfo.getAaiCategory().equals("METRO AIRPORT")) {
						region = regionInfo.getRegion();
						airportList = null;
						airportList = new ArrayList();
						airportList.add(regionInfo.getAirportcode());
					} else {
						if (regionInfo.getAaiCategory().equals(
								"NON-METRO AIRPORT")) {
							region = regionInfo.getRegion();
							airportList = financeDAO.getPensionAirportList(
									region, "");
						}

					}

				} else {
					airportList.add(selectedairportCode);
				}

				if (chkMnthYearFlag = true) {
					for (int k = 0; k < airportList.size(); k++) {
						airportCode = (String) airportList.get(k);
						form3List = financeDAO.financeForm3Report(airportCode,
								selectedDate, region, retiredDate,
								"employeename", "", "false");
						form3Cnt = form3Cnt + form3List.size();
						for (int j = 0; j < form3List.size(); j++) {
							String ms = "";
							form3Bean form3 = new form3Bean();
							form3 = (form3Bean) form3List.get(j);

							try {
								totalRecords = this.processinPersonalInfo(con,
										form3, lastactive, region, fw,
										userName, ipName, selectedDate);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								log.printStackTrace(e);
							}
							if (totalRecords == 0) {
								totalFailed = totalFailed + 1;
							} else {
								totalInserted = totalInserted + 1;
							}
						}

					}
					form3List = null;
					form3List = new ArrayList();
					if (region.equals("CHAIAD")
							&& airportCode.equals("DPO IAD")) {
						chkMnthYearFlag = true;
					}

				}
				message = "Total Form 3 Records=====" + form3Cnt + "<br/>";
				message = "Total Inserted=====" + totalInserted + "<br/>";
				message = message + "Total Failed=====" + totalFailed + "<br/>";
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return message;
	}

	public ArrayList searchPensionFreshOption(EmployeePersonalInfo empBean,
			int start, int gridLength, String sortingColumn) {
		log.info("PersonalDAO : searchPensionFreshOption() entering method");
		ArrayList empInfo = new ArrayList();
		Connection conn = null;
		ResultSet rs = null;
		String sqlQury = "";
		Statement stmt = null;
		if (empBean.getPensionNo() != null) {
			empBean.setPensionNo(commonUtil.trailingZeros(empBean
					.getPensionNo().toCharArray()));
		}
		int countGridLength = 0;
		sqlQury = this.pensionFreshOptBuildQuery(empBean, "", sortingColumn);
		EmployeePersonalInfo personal = null;
		try {
			conn = commonDB.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlQury);

			while (rs.next()) {
				countGridLength++;
				/*
				 * empInfo = new ArrayList(); log.info("gridLength" + gridLength +
				 * "countGridLength" + countGridLength); if (rs.absolute(start)) {
				 * countGridLength++; data = new EmployeePersonalInfo();
				 * log.info("searchPensionFreshOption=======IF=======" +
				 * rs.getString("cpfacno") + "countGridLength" +
				 * countGridLength); data = loadPensionFreshOption(rs);
				 * empInfo.add(data); } while (rs.next() && countGridLength <
				 * gridLength) { countGridLength++; data = new
				 * EmployeePersonalInfo();
				 * log.info("searchPensionFreshOption=======WHILE=======" +
				 * rs.getString("cpfacno") + "countGridLength" +
				 * countGridLength); data = loadPensionFreshOption(rs);
				 * empInfo.add(data); }
				 */
				personal = new EmployeePersonalInfo();
				if (rs.getString("uanNo") != null) {
				    personal.setUanno(rs.getString("uanNo"));
				} else {
				    personal.setUanno("---");
				}
				if (rs.getString("airportcode") != null) {
					personal.setAirportCode(rs.getString("airportcode"));
				} else {
					personal.setAirportCode("---");
				}
				if (rs.getString("desegnation") != null) {
					personal.setDesignation(rs.getString("desegnation"));
				} else {
					personal.setDesignation("---");
				}
				if (rs.getString("PENSIONNO") != null) {
					personal.setOldPensionNo(rs.getString("PENSIONNO"));
				} else {
					personal.setOldPensionNo("---");
				}
				if (rs.getString("PENSIONNO") != null) {
					personal.setPensionNo(commonUtil.leadingZeros(5, rs
							.getString("PENSIONNO")));
				} else {
					personal.setPensionNo("---");
				}

				if (rs.getString("employeename") != null) {
					personal.setEmployeeName(rs.getString("employeename"));
				} else {
					personal.setEmployeeName("---");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					personal.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					personal.setEmployeeNumber("---");
				}
				if (rs.getString("dateofbirth") != null) {
					personal.setDateOfBirth(commonUtil.converDBToAppFormat(rs
							.getDate("dateofbirth")));
				} else {
					personal.setDateOfBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					personal.setDateOfJoining(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFJOINING")));
				} else {
					personal.setDateOfJoining("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					personal.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					personal.setWetherOption("---");
				}
				if (rs.getString("freshpensionoption") != null) {
					personal.setFreshPensionOption(rs
							.getString("freshpensionoption"));
				} else {
					personal.setFreshPensionOption("---");
				}
				if (rs.getString("region") != null) {
					personal.setRegion(rs.getString("region"));
				} else {
					personal.setRegion("---");
				}
				if (rs.getString("DATEOFSEPERATION_REASON") != null) {
					personal.setSeperationReason(rs
							.getString("DATEOFSEPERATION_REASON"));
				} else {
					personal.setSeperationReason("---");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					personal.setSeperationDate(rs
							.getString("DATEOFSEPERATION_DATE"));
				} else {
					personal.setSeperationDate("---");
				}
				if (rs.getString("app_date") != null) {
					personal.setAppDate(commonUtil.getDatetoString(rs
							.getDate("app_date"), "dd-MMM-yyyy"));
				} else {
					personal.setAppDate("---");
				}
				if (rs.getString("flag") != null) {
					if (rs.getString("flag").equals("T"))
						personal.setFreshOPFalg(true);
				}
				if (rs.getString("FHNAME") != null) {
					personal.setFhName(rs.getString("FHNAME"));
				}
				if (!personal.getDateOfBirth().equals("---")) {
					String personalPFID = commonDAO.getPFID(personal
							.getEmployeeName().trim(), personal
							.getDateOfBirth(), personal.getPensionNo());
					personal.setPfID(personalPFID);
				} else {
					personal.setPfID(personal.getPensionNo());
				}
				empInfo.add(personal);

			}

		} catch (Exception e) {
			log.info("Inside catch" + countGridLength);
			log.printStackTrace(e);
		}

		return empInfo;
	}

	public String pensionFreshOptBuildQuery(EmployeePersonalInfo bean,
			String empName, String sortingColumn) {
		log.info("PersonalDAO:pensionFreshOptBuildQuery-- Entering Method"
				+ sortingColumn);
		String sqlQury = "", dynamicQuery = "", whereClause1 = "", whereClause2 = "";
		StringBuffer whereClause = new StringBuffer();
		StringBuffer whereClauseFresh = new StringBuffer();
		StringBuffer query = new StringBuffer();
		if (!bean.getEmployeeName().equals("")) {
			whereClauseFresh.append("  LOWER(f.employeename) like '%"
					+ bean.getEmployeeName().toLowerCase() + "%' ");
			whereClauseFresh.append(" and ");

			whereClause.append("  LOWER(i.employeename) like '%"
					+ bean.getEmployeeName().toLowerCase() + "%' ");
			whereClause.append(" and ");
		}
		if (!bean.getPensionNo().equals("")) {
			whereClauseFresh.append(" f.pensionno=" + bean.getPensionNo());
			whereClauseFresh.append(" and ");

			whereClause.append(" i.pensionno=" + bean.getPensionNo());
			whereClause.append(" and ");
		}
		if(!bean.getUanno().equals("")) {
	 		whereClauseFresh.append(" LOWER(f.uanNo)='"+bean.getUanno().toLowerCase()+"'");
	 		whereClauseFresh.append(" and ");
	 		
	 		whereClause.append(" LOWER(i.uanNo)='"+bean.getUanno().toLowerCase()+"'");
	 		whereClause.append(" and ");
	 	}
		if (!bean.getEmployeeNumber().equals("")) {
			whereClause.append(" i.employeeno=" + bean.getEmployeeNumber());
			whereClause.append(" and ");
		}
		if (!bean.getDesignation().equals("")) {
			whereClauseFresh.append(" LOWER(f.desegnation)='"
					+ bean.getDesignation().toLowerCase() + "'");
			whereClauseFresh.append(" and ");

			whereClause.append(" LOWER(i.desegnation)='"
					+ bean.getDesignation().toLowerCase() + "'");
			whereClause.append(" and ");
		}
		if (!bean.getAirportCode().equals("")) {
			whereClauseFresh.append(" LOWER(f.airportcode)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClauseFresh.append(" and ");

			whereClause.append(" LOWER(i.airportcode)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClause.append(" and ");
		}
		if (!bean.getRegion().equals("")) {
			whereClauseFresh.append(" LOWER(f.region)='"
					+ bean.getRegion().toLowerCase() + "'");
			whereClauseFresh.append(" and ");

			whereClause.append(" LOWER(i.region)='"
					+ bean.getRegion().toLowerCase() + "'");
			whereClause.append(" and ");
		}
		if (!bean.getDateOfBirth().equals("")) {
			if (bean.getDateOfBirth().indexOf("-") != -1
					&& bean.getDateOfBirth().length() == 11) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFBIRTH,'dd-MON-yyyy') like '"
								+ bean.getDateOfBirth().toUpperCase() + "'");

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MON-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("/") != -1) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFBIRTH,'dd/MM/yyyy') like '"
								+ bean.getDateOfBirth().toUpperCase() + "'");

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MM/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("-") != -1) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFBIRTH,'dd-MM-yyyy') like '"
								+ bean.getDateOfBirth().toUpperCase() + "'");
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MM-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFBIRTH,'dd/MON/yyyy') like '"
								+ bean.getDateOfBirth().toUpperCase() + "'");
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MON/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			}
			whereClauseFresh.append(" AND ");
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfJoining().equals("")) {
			if (bean.getDateOfJoining().indexOf("-") != -1
					&& bean.getDateOfJoining().length() == 11) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("/") != -1) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFJOINING,'dd/MM/yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd/MM/yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("-") != -1) {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFJOINING,'dd-MM-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MM-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else {
				whereClauseFresh
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");

			}
			whereClauseFresh.append(" AND ");
			whereClause.append(" AND ");
		}

		whereClause1 = this.sTokenFormat(whereClause).toString();
		whereClause2 = this.sTokenFormat(whereClauseFresh).toString();
		if (!whereClause1.equals("") && whereClause1 != null) {
			log.info("whereClause1=====" + whereClause1 + "whereClause1");
			whereClause1 = " and " + whereClause1;
		}
		if (!whereClause2.equals("") && whereClause2 != null) {
			log.info("whereClause2=====" + whereClause2 + "whereClause2");
			whereClause2 = " and " + whereClause2;
		}
		// query.append(sqlQury);
		if (bean.getAirportCode().equals("")
				&& bean.getPensionNo().equals("")
				&& (bean.getDateOfBirth().equals("") || bean.getDateOfBirth()
						.equals(""))
				&& bean.getUanno().equals("")
				&& (bean.getEmployeeName().equals(""))
				&& (bean.getDesignation().equals("") && (bean.getUanno().equals("")
						&& (bean.getEmployeeNumber().equals("")) && (bean
						.getRegion().equals("") && (bean.getDateOfJoining()
						.equals("")))))) {
			log.info("inside if");
		} else {
			log.info("inside else");
			query.append(" AND ");
		}
		sqlQury = " select * from (select i.cpfacno,i.uanno, i.airportcode, i.desegnation, i.pensionno, i.employeeno, i.employeename, i.dateofbirth, i.dateofjoining,"
				+ " '' as  freshpensionoption,i.wetheroption, i.region, i.fhname, i.dateofseperation_reason, i.dateofseperation_date, to_date('') as app_date, 'F' as flag "
				+ "  from employee_personal_info i where i.dateofbirth > to_date('01-Sep-1956', 'dd-Mon-yyyy') and i.dateofseperation_date is null"
				+ "  and i.dateofseperation_reason is null  and i.wetheroption = 'A'  and i.pensionno not in  (select pensionno from employee_pension_freshoption where  deleteflag='N') "
				+ whereClause1
				+ " "
				+ " union select f.cpfno as cpfacno, f.uanno, f.airportcode, f.designation, f.pensionno, f.employeeno as employeeno, f.employeename, f.dateofbirth, f.dateofjoining, f.freshpensionoption as freshpensionoption , f.weatheroption as wetheroption , "
				+ "  f.region, f.fhname, '' as dateofseperation_reason, to_date('') as dateofseperation_date, f.app_date, 'T' as flag from employee_pension_freshoption f where  deleteflag='N'  "
				+ whereClause2 + " ) order by pensionno ";

		/*
		 * sqlQury = "select
		 * epi.cpfacno,epi.pensionno,epi.employeeno,epi.employeename,epi.desegnation,epi.dateofbirth,epi.dateofjoining,epi.region,epi.airportcode," +
		 * "epi.wetheroption,epi.dateofseperation_date,epi.dateofseperation_reason "+
		 * "from employee_personal_info epi where epi.dateofbirth >
		 * to_date('01-Sep-1956', 'dd-Mon-yyyy')"+ " and
		 * epi.dateofseperation_date is null"+ " and epi.dateofseperation_reason
		 * is null"+ " and epi.wetheroption = 'A'";
		 */

		// query.append(this.sTokenFormat(whereClause));
		// String orderBy = " ORDER BY epi."+sortingColumn;
		// query.append(orderBy);

		log.info("Qury ===========" + sqlQury);

		return sqlQury;
	}

	public void insertPensionFreshOption(EmployeeFreshOptionBean freshOptionBean) {
		Connection conn = null;
		Statement instQur1 = null;
		Statement instQur2 = null;
		// Statement transstmt = null;
		Statement insertUpdateQury = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		boolean flag = false;
		try {
			conn = commonDB.getConnection();
			String insertOrUpdateQury = "";
			instQur1 = conn.createStatement();
			// trackstmt = conn.createStatement();
			// transstmt = conn.createStatement();
			// String selecttrackSeqQury = "select FRESHPENOPTION_track.nextval
			// as trackingID from dual";
			// String selectTransSeqQury = "select FRESHPENOPTION_trns.nextval
			// as trackingID from dual";
			// ResultSet seqTrackres=
			// trackstmt.executeQuery(selecttrackSeqQury);
			// ResultSet seqTransres =
			// transstmt.executeQuery(selectTransSeqQury);
			flag = this.checkfreshOptionData(conn, freshOptionBean);
			String trackingId = this.getTrackingId(conn, freshOptionBean);
			String condition = "N";
			if (flag) {

				if (freshOptionBean.getNewPensionOption().equals("A")) {
					condition = "N";
				}

				// insertOrUpdateQury = "update employee_pension_freshoption set
				// freshpensionoption=
				// '"+freshOptionBean.getNewPensionOption()+"',deleteflag='"+condition+"',app_date='"+freshOptionBean.getApplicationDate()+"'
				// where pensionno="+freshOptionBean.getPensionNo()+ " and
				// trackingid = '"+trackingId+"'";
				insertOrUpdateQury = "update employee_pension_freshoption  set freshpensionoption='"
						+ freshOptionBean.getNewPensionOption()
						+ "' , deleteflag='"
						+ condition
						+ "', app_date=to_date('"
						+ freshOptionBean.getApplicationDate()
						+ "') , employeename='"
						+ freshOptionBean.getEmployeeName()
						+ "' , fhname='"
						+ freshOptionBean.getFhName()
						+ "' , "
						+ "dateofbirth=to_date('"
						+ freshOptionBean.getDateOfBirth()
						+ "') , dateofjoining=to_date('"
						+ freshOptionBean.getDateOfJoining()
						+ "') , region='"
						+ freshOptionBean.getRegion()
						+ "' , airportcode='"
						+ freshOptionBean.getAirportCode()
						+ "' , username='"
						+ freshOptionBean.getUserName()
						+ "' where pensionno="
						+ freshOptionBean.getPensionNo()
						+ "  and trackingid='"
						+ trackingId + "'";

			} else {
				insertOrUpdateQury = "insert into employee_pension_freshoption(trackingid,pensionno,CPFNO,uanno,employeeno,employeename,"
					+ "fhname,designation,dateofbirth,dateofjoining,weatheroption,freshpensionoption,app_date,lastactive,"
					+ "airportcode,region,username) values('"
					+ trackingId
					+ "' ,'"
					+ freshOptionBean.getPensionNo()
					+ "', '"
					+freshOptionBean.getCpfAcno()
					+ "', '"
					+ freshOptionBean.getUanno()
					+ "', '"
					+ freshOptionBean.getEmployeeNo()
					+ "' , '"
					+ freshOptionBean.getEmployeeName()
					+ "' ,  '"
					+ freshOptionBean.getFhName()
					+ "' , '"
					+ freshOptionBean.getDesignation()
					+ "' ,  to_char(to_date('"
					+ freshOptionBean.getDateOfBirth()
					+ "') ,'dd-Mon-yyyy') , to_char(to_date('"
					+ freshOptionBean.getDateOfJoining()
					+ "'),'dd-Mon-yyyy') , '"
					+ freshOptionBean.getOldPensionOption()
					+ "' , '"
					+ freshOptionBean.getNewPensionOption()
					+ "' , to_char(to_date('"
					+ freshOptionBean.getApplicationDate()
					+ "'),'dd-Mon-yyyy'), "
					+ "sysdate,'"
					+ freshOptionBean.getAirportCode()
					+ "', '"
					+ freshOptionBean.getRegion()
					+ "' ,'"
					+ freshOptionBean.getUserName() + "' )";

		}

		String insrtTransQury = " insert into employee_freshop_transactions(pensionno, transid, trackingid,"
				+ "uanno,CPFACNO, employeeno, employeename,fhname,desegnation,dateofbirth,dateofjoining,wetheroption,"
				+ "applicationdate,lastactive,username,ipaddress) values('"
				+ freshOptionBean.getPensionNo()
				+ "' , FRESHPENOPTION_trns.Nextval ,'"
				+ trackingId
				+ "', '"
				+ freshOptionBean.getUanno()
				+ "','"					
				+freshOptionBean.getCpfAcno()
				+ "', '"
				+ freshOptionBean.getEmployeeNo()
				+ "', '"
				+ freshOptionBean.getEmployeeName()
				+ "' , '"
				+ freshOptionBean.getFhName()
				+ "' , '"
				+ freshOptionBean.getDesignation()
				+ "' , to_char(to_date('"
				+ freshOptionBean.getDateOfBirth()
				+ "') ,'dd-Mon-yyyy')  ,to_char(to_date('"					
				+ freshOptionBean.getDateOfJoining()
				+ "'),'dd-Mon-yyyy') , '"
				+ freshOptionBean.getNewPensionOption()
				+ "' , to_char(to_date('"
				+ freshOptionBean.getApplicationDate()
				+ "'),'dd-Mon-yyyy') , sysdate, '"
				+ freshOptionBean.getUserName()
				+ "' , '"
				+ freshOptionBean.getIpAddress() + "')";
			// rs = instQur1.executeQuery(insertOrUpdateQury);
			// rs1 = instQur2.executeQuery(insrtTransQury);
			log.info("insertorUpdateQury" + insertOrUpdateQury);
			log.info("insrtTransQury------" + insrtTransQury);
			instQur1.addBatch(insertOrUpdateQury);
			instQur1.addBatch(insrtTransQury);
			int a[] = instQur1.executeBatch();

			log.info("result of execution=====" + a);
		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
	}

	public boolean checkfreshOptionData(Connection conn,
			EmployeeFreshOptionBean freshOptionBean) {
		boolean flag = false;
		Statement checkStmt = null;
		ResultSet rs = null;
		try {
			checkStmt = conn.createStatement();
			String checkQury = "select pensionno from employee_pension_freshoption where pensionno="
					+ freshOptionBean.getPensionNo() + " and deleteflag='N'";
			rs = checkStmt.executeQuery(checkQury);
			if (rs.next()) {
				flag = true;
			} else {
				flag = false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
		return flag;
	}

	public String getTrackingId(Connection conn,
			EmployeeFreshOptionBean freshOptionBean) {
		boolean flag = false;
		String trackId = "";
		Statement checkStmt = null;
		ResultSet rs = null;
		String checkQury = "";

		try {
			checkStmt = conn.createStatement();
			// String checkQury = "select trackingid from
			// employee_pension_freshoption where
			// pensionno="+freshOptionBean.getPensionNo() +" and
			// deleteflag='N'";
			if (this.checkfreshOptionData(conn, freshOptionBean)) {
				checkQury = "select trackingid from employee_pension_freshoption where pensionno="
						+ freshOptionBean.getPensionNo()
						+ " and deleteflag='N'";
			} else {
				checkQury = "select FRESHPENOPTION_track.nextval as trackingID from dual";
			}

			rs = checkStmt.executeQuery(checkQury);
			if (rs.next()) {
				trackId = rs.getString("trackingid");
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
		return trackId;
	}

	public String getMessageForFreshOption(EmployeePersonalInfo bean) {
		log.info("Entering PersonalDAO: getMessageForFreshOption---");
		String message = "", option = "", whereCondition = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer whereClause = new StringBuffer();
		try {
			conn = commonDB.getConnection();
			stmt = conn.createStatement();
			if (!bean.getEmployeeName().equals("")
					|| !bean.getPensionNo().equals("")) {
				whereClause.append(" where ");
			}
			if (!bean.getEmployeeName().equals("")) {
				whereClause.append("  LOWER(i.employeename) = '"
						+ bean.getEmployeeName().toLowerCase() + "' ");
				whereClause.append(" and ");

			}
			if (!bean.getPensionNo().equals("")) {
				whereClause.append(" i.pensionno=" + bean.getPensionNo());
				whereClause.append(" and ");
			}
			whereCondition = this.sTokenFormat(whereClause).toString();
			log.info("wherCondition***" + whereCondition);

			String sqlQury = "select i.pensionno as pensionno, i.wetheroption as wetheroption , i.dateofbirth, CASE WHEN to_date(i.dateofbirth) > to_date('01-Sep-1956') THEN"
					+ " 'T'  ELSE 'F' END AS flag "
					+ " from employee_personal_info i " + whereCondition;

			log.info("Qury---" + sqlQury);
			rs = stmt.executeQuery(sqlQury);
			if (rs.next()) {
				if (rs.getString("wetheroption") != null) {
					option = rs.getString("wetheroption");
					if (option.trim().equals("B".trim())) {
						message = "NOT ELIGIBLE FOR FRESH OPTION AS PENSION OPTION IS 'B' ";

					} else if (option.trim().equals("A".trim())) {
						/*
						 * if(rs.getInt("checkdob")>696) { message = "NOT
						 * ELIGIBLE FOR FRESH OPTION AS DATE OF BIRTH IS LESS
						 * THAN NOV-1956"; }
						 */
						if (rs.getString("FLAG").equals("F")) {
							message = "NOT ELIGIBLE FOR FRESH OPTION AS DATE OF BIRTH IS LESS THAN NOV-1956";
						}
					} else {
						message = "NO RECORDS FOUND";
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
		log.info("End of PersonalDAO: getMessageForFreshOption---" + message);
		return message;
	}

	private int processinPersonalInfo(Connection con, form3Bean form3Bean,
			String lastactive, String region, BufferedWriter fw,
			String userName, String IPAddress, String selectedDate)
			throws SQLException {
		log.info("processinPersonalInfo========");
		String sqlSelQuery = "", totalRecords = "", pensionNumber = "", insertQry = "", updateQry = "", updateNomineeQry = "", updateFamilDetails = "";

		Statement st = null;
		Statement perSt = null;
		Statement nomineeSt = null;
		Statement familySt = null;
		int i = 0, totalInserted = 0, totalFail = 0, j = 0, k = 0;
		selectedDate = selectedDate.replaceAll("%", "01");
		try {
			pensionNumber = this.getSequenceNo(con);
			if (!pensionNumber.equals("")) {
				sqlSelQuery = "select PENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,"
						+ "WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,SEX,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,"
						+ Long.parseLong(pensionNumber)
						+ ",'"
						+ userName
						+ "','"
						+ lastactive
						+ "','"
						+ selectedDate
						+ "','"
						+ IPAddress
						+ "' "
						+ " FROM employee_info WHERE  ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ form3Bean.getCpfaccno().trim()
						+ "' AND REGION='"
						+ region.trim() + "')";
				log.info(sqlSelQuery);
				st = con.createStatement();
				perSt = con.createStatement();
				nomineeSt = con.createStatement();
				familySt = con.createStatement();
				insertQry = "insert into employee_personal_info(REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,"
						+ "GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,username,LASTACTIVE,RefMonthYear,IPAddress)  "
						+ sqlSelQuery;

				log.info(insertQry);
				i = st.executeUpdate(insertQry);
				updateQry = "UPDATE EMPLOYEE_INFO SET EMPSERIALNUMBER='"
						+ pensionNumber
						+ "' WHERE ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "')";
				i = perSt.executeUpdate(updateQry);
				updateNomineeQry = "UPDATE EMPLOYEE_NOMINEE_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "'";
				j = nomineeSt.executeUpdate(updateNomineeQry);
				updateFamilDetails = "UPDATE EMPLOYEE_FAMILY_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "'";
				k = familySt.executeUpdate(updateFamilDetails);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);

			if (!(insertQry.equals("") || updateQry.equals(""))) {
				writeFailedQueryErrorLog(insertQry, fw);
				writeFailedQueryErrorLog(updateQry, fw);
				writeFailedQueryErrorLog(updateNomineeQry, fw);
				writeFailedQueryErrorLog(updateFamilDetails, fw);
			}

		} catch (Exception e) {
			log.printStackTrace(e);

			if (!(insertQry.equals("") || updateQry.equals(""))) {
				writeFailedQueryErrorLog(insertQry, fw);
				writeFailedQueryErrorLog(updateQry, fw);
				writeFailedQueryErrorLog(updateNomineeQry, fw);
				writeFailedQueryErrorLog(updateFamilDetails, fw);
			}

		} finally {
			if (perSt != null) {
				try {
					perSt.close();
					perSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (nomineeSt != null) {
				try {
					nomineeSt.close();
					nomineeSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (familySt != null) {
				try {
					familySt.close();
					familySt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (st != null) {
				try {
					st.close();
					st = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}

		}

		return i;
	}

	public int addPersonalInfo(String pensionNumber, String cpfaccno,
			String lastactive, String region, String userName,
			String IPAddress, String selectedDate) throws SQLException {
		log.info("processinPersonalInfo========");
		String sqlSelQuery = "", insertQry = "", updateQry = "", updateNomineeQry = "", updateFamilDetails = "";
		Connection con = null;
		Statement st = null;
		Statement perSt = null;
		Statement nomineeSt = null;
		Statement familySt = null;
		int i = 0, totalInserted = 0, totalFail = 0, j = 0, k = 0;
		selectedDate = selectedDate.replaceAll("%", "01");
		try {
			con = DBUtils.getConnection();
			if (!pensionNumber.equals("")) {
				sqlSelQuery = "select PENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,"
						+ "WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,SEX,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,"
						+ Long.parseLong(pensionNumber)
						+ ",'"
						+ userName
						+ "','"
						+ lastactive
						+ "','"
						+ selectedDate
						+ "','"
						+ IPAddress
						+ "' "
						+ " FROM employee_info WHERE  ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ cpfaccno.trim()
						+ "' AND REGION='"
						+ region.trim()
						+ "')";
				log.info(sqlSelQuery);
				st = con.createStatement();
				perSt = con.createStatement();
				nomineeSt = con.createStatement();
				familySt = con.createStatement();
				insertQry = "insert into employee_personal_info(REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,"
						+ "GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,username,LASTACTIVE,RefMonthYear,IPAddress)  "
						+ sqlSelQuery;

				log.info(insertQry);
				i = st.executeUpdate(insertQry);
				updateQry = "UPDATE EMPLOYEE_INFO SET EMPSERIALNUMBER='"
						+ pensionNumber
						+ "' WHERE ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ cpfaccno.trim() + "' AND REGION='" + region.trim()
						+ "')";
				i = perSt.executeUpdate(updateQry);
				updateNomineeQry = "UPDATE EMPLOYEE_NOMINEE_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ cpfaccno.trim() + "' AND REGION='" + region.trim()
						+ "'";
				j = nomineeSt.executeUpdate(updateNomineeQry);
				updateFamilDetails = "UPDATE EMPLOYEE_FAMILY_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ cpfaccno.trim() + "' AND REGION='" + region.trim()
						+ "'";
				k = familySt.executeUpdate(updateFamilDetails);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (perSt != null) {
				try {
					perSt.close();
					perSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (nomineeSt != null) {
				try {
					nomineeSt.close();
					nomineeSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (familySt != null) {
				try {
					familySt.close();
					familySt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (st != null) {
				try {
					st.close();
					st = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}

		}

		return i;
	}

	private void writeFailedQueryErrorLog(String query, BufferedWriter fw) {
		try {
			fw.write(query + ";");
			fw.newLine();
			fw.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.printStackTrace(e1);
		}
	}

	public String getSequenceNo(Connection con) throws Exception {
		String selQuery = "SELECT max(pensionno)+1 AS PENSIONNO FROM employee_personal_info";

		Statement st = null;
		ResultSet rs = null;
		String pensionNo = "";
		try {

			st = con.createStatement();
			rs = st.executeQuery(selQuery);
			if (rs.next()) {
				pensionNo = rs.getString("PENSIONNO");
			}

		} catch (SQLException e) {
			log.printStackTrace(e);

		} catch (Exception e) {

			log.printStackTrace(e);

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
		}
		return pensionNo;
	}

	public boolean checkRefMnthYear(String selectedDate, String region) {
		log.info("PersonalDAO :checkRefMnthYear() entering method");
		String qryRegion = "";
		if (!region.equals("")) {
			qryRegion = " AND REGION='" + region + "'";
		} else {
			qryRegion = "";
		}
		selectedDate = selectedDate.replaceAll("%", "01");
		String selQuery = "SELECT COUNT(*) AS COUNT FROM EMPLOYEE_PERSONAL_INFO WHERE TO_CHAR(REFMONTHYEAR,'dd-Mon-yyyy')='"
				+ selectedDate + "'" + qryRegion;
		log.info("PersonalDAO : Select Query" + selQuery);
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		int count = 0;
		boolean isAvailable = false;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selQuery);
			if (rs.next()) {
				count = rs.getInt("COUNT");
				if (count == 0) {
					isAvailable = true;
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return isAvailable;
	}

	public ArrayList searchPersonal(EmployeePersonalInfo empPersonalBean,
			int start, int gridLength, String sortingColumn, String flag,
			String pageFlag) throws Exception {

		log.info("PersonalDAO :searchPersonal() entering method");
		ArrayList empinfo = new ArrayList();

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sqlQuery = "";
		DatabaseBean searchData = new DatabaseBean();

		log.info("pensionno " + empPersonalBean.getPensionNo());
		if (empPersonalBean.getPensionNo() != null) {
			empPersonalBean
					.setPensionNo(commonUtil.trailingZeros(empPersonalBean
							.getPensionNo().toCharArray()));
		}

		if (!pageFlag.equals("form10D")) {
			sqlQuery = this.personalBuildQuery(empPersonalBean, flag, "",
					sortingColumn);
		} else {
			sqlQuery = this.form10DPersonalBuildQuery(empPersonalBean, flag,
					"", sortingColumn);
		}

		log.info("sql query " + sqlQuery);

		int countGridLength = 0;
		System.out.println("Query is(retriveByAll)" + sqlQuery);
		String employeeName = "";
		try {
			con = commonDB.getConnection();
			pst = con
					.prepareStatement(sqlQuery,
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			rs = pst.executeQuery();
			EmployeePersonalInfo data = null;
			if (rs.next()) {
				empinfo = new ArrayList();
				log.info("gridLength" + gridLength + "countGridLength"
						+ countGridLength);
				if (!pageFlag.equals("form10D")) {
					if (rs.absolute(start)) {
						countGridLength++;
						data = new EmployeePersonalInfo();
						log.info("loadPersonalInfoRevised=======IF======="
								+ rs.getString("cpfacno") + "countGridLength"
								+ countGridLength);
						data = loadPersonalInfoRevised(rs);
						empinfo.add(data);
					}
					while (rs.next() && countGridLength < gridLength) {
						countGridLength++;
						data = new EmployeePersonalInfo();
						log.info("loadPersonalInfoRevised=======WHILE======="
								+ rs.getString("cpfacno") + "countGridLength"
								+ countGridLength);
						data = loadPersonalInfoRevised(rs);
						empinfo.add(data);
					}
				} else {
					if (rs.absolute(start)) {
						countGridLength++;
						data = new EmployeePersonalInfo();
						log.info("loadPersonalInfo=======IF======="
								+ rs.getString("cpfacno") + "countGridLength"
								+ countGridLength);
						data = loadPersonalInfo(rs);
						empinfo.add(data);
					}
					while (rs.next() && countGridLength < gridLength) {
						countGridLength++;
						data = new EmployeePersonalInfo();
						log.info("loadPersonalInfo=======WHILE======="
								+ rs.getString("cpfacno") + "countGridLength"
								+ countGridLength);
						data = loadPersonalInfo(rs);
						empinfo.add(data);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, pst, rs);
		}
		log.info("PersonalDAO :searchPersonal leaving method");
		return empinfo;
	}

	private EmployeePersonalInfo loadPersonalInfo(ResultSet rs)
			throws SQLException {
		EmployeePersonalInfo personal = new EmployeePersonalInfo();
		log.info("loadPersonalInfo==============");

		if (rs.getString("cpfacno") != null) {
			personal.setCpfAccno(rs.getString("cpfacno"));
		} else {
			personal.setCpfAccno("---");
		}
		log
				.info("loadPersonalInfo=============="
						+ rs.getString("airportcode"));
		if (rs.getString("airportcode") != null) {
			personal.setAirportCode(rs.getString("airportcode"));
		} else {
			personal.setAirportCode("---");
		}
		if (rs.getString("desegnation") != null) {
			personal.setDesignation(rs.getString("desegnation"));
		} else {
			personal.setDesignation("---");
		}
		if (rs.getString("PENSIONNO") != null) {
			personal.setOldPensionNo(rs.getString("PENSIONNO"));
			log.info("setOldPensionNo " + personal.getOldPensionNo());
		} else {
			personal.setOldPensionNo("---");
		}
		if (rs.getString("PENSIONNO") != null) {
			personal.setPensionNo(commonUtil.leadingZeros(5, rs
					.getString("PENSIONNO")));
			log.info("pfno " + personal.getPensionNo());
		} else {
			personal.setPensionNo("---");
		}

		if (rs.getString("employeename") != null) {
			personal.setEmployeeName(rs.getString("employeename"));
		} else {
			personal.setEmployeeName("---");
		}
		if (rs.getString("EMPLOYEENO") != null) {
			personal.setEmployeeNumber(rs.getString("EMPLOYEENO"));
		} else {
			personal.setEmployeeNumber("---");
		}

		if (rs.getString("REFPENSIONNUMBER") != null) {
			personal.setRefPensionNumber(rs.getString("REFPENSIONNUMBER"));
		} else {
			personal.setRefPensionNumber("---");
		}
		if (rs.getString("dateofbirth") != null) {
			personal.setDateOfBirth(commonUtil.converDBToAppFormat(rs
					.getDate("dateofbirth")));
		} else {
			personal.setDateOfBirth("---");
		}
		if (rs.getString("DATEOFJOINING") != null) {
			personal.setDateOfJoining(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			personal.setDateOfJoining("---");
		}
		if (rs.getString("WETHEROPTION") != null) {
			personal.setWetherOption(rs.getString("WETHEROPTION"));
		} else {
			personal.setWetherOption("---");
		}
		if (rs.getString("region") != null) {
			personal.setRegion(rs.getString("region"));
		} else {
			personal.setRegion("---");
		}
		if (rs.getString("MARITALSTATUS") != null) {
			personal.setMaritalStatus(rs.getString("MARITALSTATUS"));
		} else {
			personal.setMaritalStatus("---");
		}

		if ((personal.getCpfAccno().equals("")
				|| (personal.getEmployeeNumber().equals(""))
				|| (personal.getDateOfBirth().equals(""))
				|| (personal.getWetherOption().equals("")) || (personal
				.getEmployeeName().equals("")))) {
			personal.setRemarks("incomplete Data");
			// log.info("inside if");
		} else {
			personal.setRemarks("---");
			// log.info("inside else");
		}

		if (rs.getString("lastactive") != null) {
			personal.setLastActive(commonUtil.getDatetoString(rs
					.getDate("lastactive"), "dd-MMM-yyyy"));
		} else {
			personal.setLastActive("---");
		}

		if (rs.getString("GENDER") != null) {
			personal.setGender(rs.getString("GENDER"));
			log.info("gender " + rs.getString("GENDER").toString());
		} else {
			personal.setGender("---");
		}
		if (rs.getString("FHNAME") != null) {
			personal.setFhName(rs.getString("FHNAME"));
		} else {
			personal.setFhName("---");
		}
		if (rs.getString("OTHERREASON") != null) {
			personal.setOtherReason(rs.getString("OTHERREASON"));
		} else {
			personal.setOtherReason("---");
		}
		if (rs.getString("DATEOFSEPERATION_REASON") != null) {
			personal.setSeperationReason(rs
					.getString("DATEOFSEPERATION_REASON"));
		} else {
			personal.setSeperationReason("---");
		}
		if (rs.getString("DATEOFSEPERATION_DATE") != null) {
			personal.setSeperationDate(rs.getString("DATEOFSEPERATION_DATE"));
		} else {
			personal.setSeperationDate("---");
		}
		if (rs.getString("division") != null) {
			personal.setDivision(rs.getString("division"));
		} else {
			personal.setDivision("---");
		}

		if (!personal.getDateOfBirth().equals("---")) {
			String personalPFID = commonDAO
					.getPFID(personal.getEmployeeName().trim(), personal
							.getDateOfBirth(), personal.getPensionNo());
			personal.setPfID(personalPFID);
		} else {
			personal.setPfID(personal.getPensionNo());
		}

		return personal;

	}

	public String personalBuildQuery(EmployeePersonalInfo bean, String flag,
			String empNamechek, String sortingColumn) {

		log.info("PersonalDao:personalBuildQuery-- Entering Method"
				+ bean.getUanno());
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "", sqlQuery = "";
		log.info("empname check " + bean.getChkEmpNameFlag()
				+ "empNamechek====" + empNamechek);
		if (!flag.equals("count")) {

			sqlQuery = "select a.*, nvl(b.freshpensionoption,'---') as freshoption from (select i.*, (case when (f.purpose is not null) then f.purpose when (i.dateofseperation_reason is null and i.dateofseperation_date is null) then 'Active' when (i.dateofseperation_reason is null and i.dateofseperation_date is not null) then '-NA-' else i.dateofseperation_reason end) as status from employee_personal_info i, epis_pfid_year_wise_frozen f where i.pensionno = f.pensionno(+) and empflag = 'Y') a, (select * from employee_pension_freshoption where deleteflag = 'N') b where a.pensionno = b.pensionno(+)"
					+ empNamechek;
			/*
			 * sqlQuery =
			 * "select i.*,(case when (f.purpose is not null) then f.purpose when(i.dateofseperation_reason is null and i.dateofseperation_date is null) then 'Active' when(i.dateofseperation_reason is null and i.dateofseperation_date is not null) then '-NA-' else i.dateofseperation_reason end) as status from employee_personal_info i,epis_pfid_year_wise_frozen f where i.pensionno = f.pensionno(+) and empflag='Y' "
			 * + empNamechek;
			 */
			/*
			 * sqlQuery = "SELECT
			 * CPFACNO,REFPENSIONNUMBER,EMPLOYEENO,EMPLOYEENAME
			 * ,DESEGNATION,EMP_LEVEL
			 * ,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON
			 * ,DATEOFSEPERATION_DATE,AIRPORTCODE,GENDER,FHNAME" +
			 * ",MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,OTHERREASON,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REMARKS,PENSIONNO,REGION,LASTACTIVE
			 * from employee_personal_info where empflag='Y'";
			 */
		} else {
			sqlQuery = "select COUNT(*) as COUNT from employee_personal_info a,epis_pfid_year_wise_frozen f where a.pensionno = f.pensionno(+) and empflag='Y' "
					+ empNamechek;
		}

		if (!bean.getCpfAccno().equals("")) {
			whereClause.append(" LOWER(cpfacno) ='"
					+ bean.getCpfAccno().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getPensionNo().equals("")) {
			whereClause.append(" a.PENSIONNO =" + bean.getPensionNo());
			whereClause.append(" AND ");
		}
		if (!bean.getUanno().equals("")) {
			whereClause.append(" a.uanno =" + bean.getUanno());
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfBirth().equals("")) {

			if (bean.getDateOfBirth().indexOf("-") != -1
					&& bean.getDateOfBirth().length() == 11) {
				whereClause.append(" TO_CHAR(a.DATEOFBIRTH,'dd-MON-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("/") != -1) {

				whereClause.append(" TO_CHAR(a.DATEOFBIRTH,'dd/MM/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("-") != -1) {

				whereClause.append(" TO_CHAR(a.DATEOFBIRTH,'dd-MM-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else {
				whereClause.append(" TO_CHAR(a.DATEOFBIRTH,'dd/MON/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			}
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfJoining().equals("")) {
			if (bean.getDateOfJoining().indexOf("-") != -1
					&& bean.getDateOfJoining().length() == 11) {
				whereClause
						.append(" TO_CHAR(a.DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("/") != -1) {

				whereClause
						.append(" TO_CHAR(a.DATEOFJOINING,'dd/MM/yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("-") != -1) {

				whereClause
						.append(" TO_CHAR(a.DATEOFJOINING,'dd-MM-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else {
				whereClause
						.append(" TO_CHAR(a.DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");

			}
			whereClause.append(" AND ");
		}
		if (!bean.getAirportCode().equals("")) {
			whereClause.append(" LOWER(a.airportcode)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeName().equals("")) {
			whereClause.append(" LOWER(a.employeename) like'%"
					+ bean.getEmployeeName().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		if (!bean.getDesignation().equals("")) {
			whereClause.append(" LOWER(a.desegnation)='"
					+ bean.getDesignation().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeNumber().equals("")) {
			whereClause
					.append(" a.EMPLOYEENO='" + bean.getEmployeeNumber() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getRegion().equals("")) {
			whereClause.append(" a.region='" + bean.getRegion() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (bean.getUanno().equals("") && bean.getAirportCode().equals("")
				&& bean.getPensionNo().equals("")
				&& (bean.getDateOfBirth().equals("") || bean.getDateOfBirth()
						.equals(""))
				&& bean.getCpfAccno().equals("")
				&& (bean.getEmployeeName().equals(""))
				&& (bean.getDesignation().equals("") && (bean.getCpfAccno()
						.equals("")
						&& (bean.getEmployeeNumber().equals("")) && (bean
						.getRegion().equals("") && (bean.getDateOfJoining()
						.equals("")))))) {
			log.info("inside if");
		} else {
			log.info("inside else");
			if (!prefixWhereClause.equals("")) {
				query.append(" AND ");
			} else {
				query.append(" AND ");
			}
		}

		query.append(this.sTokenFormat(whereClause));
		String orderBy = " ORDER BY a." + sortingColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("PersonalDAO:buildQuery Leaving Method");
		return dynamicQuery;
	}

	private String sTokenFormat(StringBuffer stringBuffer) {
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

	public int totalCountPersonal(EmployeePersonalInfo empMasterBean,
			String flag, String sortingColumn, String pageFlag) {
		log.info("PersonalDAO :totalCountPersonal() entering method");
		Connection con = null;
		int totalRecords = 0;

		log.info("Sorting Column" + sortingColumn);
		String selectSQL = "";
		if (!pageFlag.equals("form10D")) {
			selectSQL = this.personalBuildQuery(empMasterBean, flag, "",
					sortingColumn);
		} else {
			selectSQL = this.form10DPersonalBuildQuery(empMasterBean, flag, "",
					sortingColumn);
		}
		log.info("Query =====" + selectSQL);
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(selectSQL);
			if (rs.next()) {
				totalRecords = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("PersonalDAO :totalCountPersonal() leaving method");
		return totalRecords;
	}

	public String form10DPersonalBuildQuery(EmployeePersonalInfo bean,
			String flag, String empNamechek, String sortingColumn) {

		log.info("PersonalDao:personalBuildQuery-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "", sqlQuery = "";
		log.info("empname check " + bean.getChkEmpNameFlag()
				+ "empNamechek====" + empNamechek);
		if (!flag.equals("count")) {
			sqlQuery = "SELECT * from employee_personal_info where empflag='Y' AND FINALSETTLMENTDT IS NOT NULL"
					+ empNamechek;
			/*
			 * sqlQuery = "SELECT
			 * CPFACNO,REFPENSIONNUMBER,EMPLOYEENO,EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,AIRPORTCODE,GENDER,FHNAME" +
			 * ",MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,OTHERREASON,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REMARKS,PENSIONNO,REGION,LASTACTIVE
			 * from employee_personal_info where empflag='Y'";
			 */
		} else {
			sqlQuery = "SELECT COUNT(*) as COUNT from employee_personal_info where empflag='Y' AND FINALSETTLMENTDT IS NOT NULL"
					+ empNamechek;
		}

		if (!bean.getCpfAccno().equals("")) {
			whereClause.append(" LOWER(cpfacno) ='"
					+ bean.getCpfAccno().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getPensionNo().equals("")) {
			whereClause.append(" PENSIONNO =" + bean.getPensionNo());
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfBirth().equals("")) {

			if (bean.getDateOfBirth().indexOf("-") != -1
					&& bean.getDateOfBirth().length() == 11) {
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MON-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("/") != -1) {

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MM/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("-") != -1) {

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MM-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else {
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MON/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			}
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfJoining().equals("")) {
			if (bean.getDateOfJoining().indexOf("-") != -1
					&& bean.getDateOfJoining().length() == 11) {
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("/") != -1) {

				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd/MM/yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("-") != -1) {

				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MM-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else {
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");

			}
			whereClause.append(" AND ");
		}
		if (!bean.getAirportCode().equals("")) {
			whereClause.append(" LOWER(airportcode)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeName().equals("")) {
			whereClause.append(" LOWER(employeename) like'%"
					+ bean.getEmployeeName().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		if (!bean.getDesignation().equals("")) {
			whereClause.append(" LOWER(desegnation)='"
					+ bean.getDesignation().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeNumber().equals("")) {
			whereClause
					.append(" EMPLOYEENO='" + bean.getEmployeeNumber() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getRegion().equals("")) {
			whereClause.append(" region='" + bean.getRegion() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (bean.getAirportCode().equals("")
				&& bean.getPensionNo().equals("")
				&& (bean.getDateOfBirth().equals("") || bean.getDateOfBirth()
						.equals(""))
				&& bean.getCpfAccno().equals("")
				&& (bean.getEmployeeName().equals(""))
				&& (bean.getDesignation().equals("") && (bean.getCpfAccno()
						.equals("")
						&& (bean.getEmployeeNumber().equals("")) && (bean
						.getRegion().equals("") && (bean.getDateOfJoining()
						.equals("")))))) {
			log.info("inside if");
		} else {
			log.info("inside else");
			if (!prefixWhereClause.equals("")) {
				query.append(" AND ");
			} else {
				query.append(" AND ");
			}
		}

		query.append(this.sTokenFormat(whereClause));
		String orderBy = " ORDER BY " + sortingColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("PersonalDAO:buildQuery Leaving Method");
		return dynamicQuery;
	}

	public EmployeePersonalInfo getPersonalInfo(Connection con, String pensionNo) {
		Statement st = null;
		ResultSet rs = null;
		String sqlSelQuery = "";
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		try {

			st = con.createStatement();
			sqlSelQuery = "SELECT CPFACNO, REFPENSIONNUMBER   , AIRPORTSERIALNUMBER, EMPLOYEENO   , EMPLOYEENAME , DESEGNATION  , EMP_LEVEL    , "
					+ "DATEOFBIRTH   , DATEOFJOINING , DATEOFSEPERATION_REASON    , DATEOFSEPERATION_DATE      , WHETHER_FORM1_NOM_RECEIVED , AIRPORTCODE, GENDER , "
					+ "FHNAME , MARITALSTATUS   , PERMANENTADDRESS, TEMPORATYADDRESS, WETHEROPTION    , SETDATEOFANNUATION, EMPFLAG    , OTHERREASON, DIVISION   , DEPARTMENT , EMAILID    , "
					+ "EMPNOMINEESHARABLE, REMARKS    , PENSIONNO  , REGION     , USERNAME   , LASTACTIVE , REFMONTHYEAR , IPADDRESS , FHFLAG    , MAPINGFLAG, PFSETTLED , FINALSETTLMENTDT  , "
					+ "OPTIONFORMRECEIVED,NATIONALITY,HEIGHT, MAPPEDUSERNM,to_char(add_months(DATEOFSEPERATION_DATE,-12),'dd-Mon-yyyy') AS SEPERATION_FROMDATE,PHONENUMBER FROM EMPLOYEE_PERSONAL_INFO WHERE PENSIONNO="
					+ pensionNo;
			log.info("PersonalDAO:getPersonalInfo" + sqlSelQuery);
			st = con.createStatement();
			rs = st.executeQuery(sqlSelQuery);
			if (rs.next()) {
				personalInfo = commonDAO.loadPersonalInfo(rs);
				if (rs.getString("FINALSETTLMENTDT") != null) {
					personalInfo
							.setFinalSettlementDate(commonUtil
									.converDBToAppFormat(rs
											.getDate("FINALSETTLMENTDT")));
				} else {
					personalInfo.setFinalSettlementDate("---");
				}
				if (rs.getString("PERMANENTADDRESS") != null) {
					personalInfo
							.setPerAddress(rs.getString("PERMANENTADDRESS"));
				} else {
					personalInfo.setPerAddress("");
				}
				if (rs.getString("TEMPORATYADDRESS") != null) {
					personalInfo.setTempAddress(rs
							.getString("TEMPORATYADDRESS"));
				} else {
					personalInfo.setTempAddress("");
				}
				if (rs.getString("PHONENUMBER") != null) {
					personalInfo.setPhoneNumber(rs.getString("PHONENUMBER"));
				} else {
					personalInfo.setPhoneNumber("");
				}
				if (rs.getString("NATIONALITY") != null) {
					personalInfo.setNationality(rs.getString("NATIONALITY"));
				} else {
					personalInfo.setNationality("");
				}
				if (rs.getString("HEIGHT") != null) {
					personalInfo.setHeightWithInches(StringUtility.replace(
							rs.getString("HEIGHT").toCharArray(),
							".".toCharArray(), "'").toString());
				} else {
					personalInfo.setHeightWithInches("");
				}
				if (rs.getString("SEPERATION_FROMDATE") != null) {
					personalInfo.setSeperationFromDate(rs
							.getString("SEPERATION_FROMDATE"));
				} else {
					personalInfo.setSeperationFromDate("---");
				}
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return personalInfo;
	}

	public ArrayList freshOptionReports(String frmName,String region,String airportCode) {
		// String
		// pensionno="",employeename="",cpfAccno="",dateOfBirth="",dateofjoining="",wetheroption="",selectedMonth="",selectedYear="";

		ArrayList list = null;

		EmployeePersonalInfo info = null;
		String sqlQry = "";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		sqlQry = this.buildFreshOptionQury(frmName, region, airportCode);
		log.info("FreshOption Master Report Qury======" + sqlQry);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQry);

			list = new ArrayList();
			while (rs.next()) {

				info = new EmployeePersonalInfo();
				if (rs.getString("pensionno") != null) {

					info.setPensionNo(rs.getString("pensionno"));
				}
				if (rs.getString("cpfacno") != null) {
					info.setCpfAccno(rs.getString("cpfacno"));
				} else {
					info.setCpfAccno("---");
				}
				if (rs.getString("employeeno") != null) {
					info.setEmployeeNumber(rs.getString("employeeno"));
				} else {
					info.setEmployeeNumber("---");
				}
				if (rs.getString("employeename") != null) {
					info.setEmployeeName(rs.getString("employeename"));
				}

				if (rs.getString("desegnation") != null) {
					info.setDesignation(rs.getString("desegnation"));
				}
				if (rs.getString("dateofbirth") != null) {
					info.setDateOfBirth(rs.getString("dateofbirth"));
				}
				if (rs.getString("dateofjoining") != null) {
					info.setDateOfJoining(rs.getString("dateofjoining"));
				}
				if (rs.getString("wetheroption") != null) {
					info.setWetherOption(rs.getString("wetheroption"));
				}
				if (rs.getString("airportcode") != null) {
					info.setAirportCode(rs.getString("airportcode"));
				}
				if (rs.getString("region") != null) {
					info.setRegion(rs.getString("region"));
				}
				if(rs.getString("uanno")!=null) {
					info.setUanno(rs.getString("uanno"));
				}else {
					info.setUanno("---");
				}
				if(rs.getString("fhname")!=null) {
					info.setFhName(rs.getString("fhname"));
				}
				else {
					info.setFhName("---");
				}
				if(rs.getString("freshpensionoption")!=null) {
					info.setFreshPensionOption(rs.getString("freshpensionoption"));
				}
				else {
					info.setFreshPensionOption("---");
				}
				//log.info("" + info.getPensionNo());
				list.add(info);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			commonDB.closeConnection(con,st,rs);
		}

		return list;
	}

	public ArrayList allFreshOptionReports(String frmName,String region,String airportCode) {
		// String
		// pensionno="",employeename="",cpfAccno="",dateOfBirth="",dateofjoining="",wetheroption="",selectedMonth="",selectedYear="";

		ArrayList list = null;

		EmployeeFreshOptionBean fresh = null;
		String sqlQry = "";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		sqlQry=this.buildFreshOptionQury(frmName, region, airportCode);

		log.info("sqlQrysqlQrysqlQrysqlQrysqlQry======" + sqlQry);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQry);

			list = new ArrayList();
			while (rs.next()) {

				fresh = new EmployeeFreshOptionBean();
				if (rs.getString("pensionno") != null) {

					fresh.setPensionNo(rs.getString("pensionno"));
				}
				if (rs.getString("cpfno") != null) {
					fresh.setCpfAcno(rs.getString("cpfno"));
				} else {
					fresh.setCpfAcno("---");
				}
				if (rs.getString("employeeno") != null) {
					fresh.setEmployeeNo(rs.getString("employeeno"));
				} else {
					fresh.setEmployeeNo("---");
				}
				if (rs.getString("employeename") != null) {
					fresh.setEmployeeName(rs.getString("employeename"));
				}

				if (rs.getString("designation") != null) {
					fresh.setDesignation(rs.getString("designation"));
				}
				if (rs.getString("dateofbirth") != null) {
					fresh.setDateOfBirth(rs.getString("dateofbirth"));
				}
				if (rs.getString("dateofjoining") != null) {
					fresh.setDateOfJoining(rs.getString("dateofjoining"));
				}
				/*
				 * if(rs.getString("wetheroption")!=null){
				 * fresh.setw(rs.getString("wetheroption")); }
				 */
				if (rs.getString("airportcode") != null) {
					fresh.setAirportCode(rs.getString("airportcode"));
				}
				if (rs.getString("region") != null) {
					fresh.setRegion(rs.getString("region"));
				}
				if (rs.getString("username") != null) {
					fresh.setUserName(rs.getString("username"));
				}
				if (rs.getString("app_date") != null) {
					fresh.setApplicationDate(rs.getString("app_date"));
				} else {
					fresh.setApplicationDate("---");
				}
				if(rs.getString("uanno")!=null) {
					fresh.setUanno(rs.getString("uanno"));
				}else {
					fresh.setUanno("---");
				}
				if(rs.getString("fhname")!=null) {
					fresh.setFhName(rs.getString("fhname"));
				}else {
					fresh.setFhName("---");
				}
				//log.info("" + fresh.getPensionNo());
				list.add(fresh);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			commonDB.closeConnection(con,st,rs);
		}

		return list;
	}

	public ArrayList personalReport(EmployeePersonalInfo empMasterBean,
			String flag, String empNameCheak, String sortingColumn,
			String reporttype) {
		log.info("PersonalDAO :personalReport() entering method");
		Connection con = null;
		Statement st=null;
		ResultSet rs=null;
		String empNameCheck = "";
		EmployeePersonalInfo personal = null;
		ArrayList reportPersList = new ArrayList();

		int totalRecords = 0;
		if (empMasterBean.getPensionNo() != null) {
			empMasterBean.setPensionNo(commonUtil.trailingZeros(empMasterBean
					.getPensionNo().toCharArray()));
		}
		if (empMasterBean.getChkEmpNameFlag().equals("true")) {
			empNameCheck = " and info.employeename is null";
		}
		log.info("empNameCheck" + empNameCheck);
		String selectSQL = this.personalReportBuildQuery(empMasterBean, flag,
				empNameCheck, "PENSIONNO ASC");
		log.info("Query =====" + selectSQL);
		try {
			con = commonDB.getConnection();
		 st = con.createStatement();
		 rs = st.executeQuery(selectSQL);
			while (rs.next()) {
				personal = new EmployeePersonalInfo();
				personal = loadPersonalInfoRevised(rs);
				personal = this.getCurrentPersonalInfo(con, personal
						.getPensionNo(), personal);
				// personal.setTransferFlag(rs.getString("TRANSFERFLAG"));
				reportPersList.add(personal);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			commonDB.closeConnection(con,st,rs);
		}
		log.info("PersonalDAO :personalReport() leaving method");
		return reportPersList;
	}

	public EmpMasterBean empPersonalEdit(String cpfacno, String empName,
			boolean flag, String empCode, String region, String pfid)
			throws Exception {
		log.info("PensionDAO:empPersonalEdit entering method ");
		EmpMasterBean editBean = new EmpMasterBean();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String query = "", query1 = "";
		if (flag == true) {
			if (!cpfacno.equals("") && !empName.equals("")) {
				/*query = " select i.*,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else add_months(i.dateofbirth, 696) - 1 end) as dateofretirement,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else last_day(add_months(i.dateofbirth, 720))end ) as superannuation1 from EMPLOYEE_PERSONAL_INFO i where  empflag='Y' AND LOWER(PENSIONNO) ='"
						+ pfid.toLowerCase().trim()
						+ "' AND region='"
						+ region.trim() + "'  order by PENSIONNO";*/
				query = "select a.*, b.freshpensionoption as freshpensionoption from  (select i.*,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else add_months(i.dateofbirth, 696) - 1 end) as dateofretirement,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else last_day(add_months(i.dateofbirth, 720))end ) as superannuation1 from EMPLOYEE_PERSONAL_INFO i where  empflag='Y' AND LOWER(PENSIONNO) ='"
					+ pfid.toLowerCase().trim()
					+ "' AND region='" + region.trim() + "') a ,(select d.freshpensionoption as freshpensionoption,d.pensionno from employee_pension_freshoption d where d.deleteflag = 'N') b where a.pensionno=b.pensionno(+) order by a.PENSIONNO";

				log.info("query1" + query1);
			} else if (!cpfacno.equals("")) {
				query = " select i.*,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else add_months(i.dateofbirth, 696) - 1 end) as dateofretirement,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else last_day(add_months(i.dateofbirth, 720))end ) as superannuation1 from EMPLOYEE_PERSONAL_INFO i where ROWID IN(select MIN(ROWID) from EMPLOYEE_PERSONAL_INFO"
						+ " where empflag='Y' AND LOWER(PENSIONNO) ='"
						+ pfid.toLowerCase()
						+ "' AND region='"
						+ region.trim()
						+ "'  group by cpfacno) order by pfid";
			} else {
				query = " select i.*,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else add_months(i.dateofbirth, 696) - 1 end) as dateofretirement,(case when(i.dateofseperation_date<add_months(i.dateofbirth, 696)) then i.dateofseperation_date else last_day(add_months(i.dateofbirth, 720))end ) as superannuation1 from EMPLOYEE_PERSONAL_INFO i where ROWID IN(select MIN(ROWID) from employee_info"
						+ " where empflag='Y' AND LOWER(EMPLOYEENO) ='"
						+ empCode.toLowerCase()
						+ "' AND region='"
						+ region.trim() + "')";
			}
		} else {
			query = "select * from employee_info_invaliddata where cpfacno='"
					+ cpfacno + "' AND LOWER(employeename)='"
					+ empName.toLowerCase() + "'";
		}
		log.info("query is " + query);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);
			editBean.setCpfAcNo(cpfacno.trim());
			while (rs.next()) {

				if (rs.getString("employeename") != null)
					editBean.setEmpName(rs.getString("employeename").trim());
				else
					editBean.setEmpName("");
				if (rs.getString("EMPLOYEENO") != null)
					editBean.setEmpNumber(rs.getString("EMPLOYEENO").trim());
				else
					editBean.setEmpNumber("");
				if (rs.getString("NEWEMPCODE") != null)
					editBean.setNewEmployeeNumber(rs.getString("NEWEMPCODE")
							.trim());
				else
					editBean.setNewEmployeeNumber("");

				if (rs.getString("desegnation") != null)
					editBean.setDesegnation(rs.getString("desegnation").trim());
				else
					editBean.setDesegnation("");

				if (rs.getString("EMP_LEVEL") != null)
					editBean.setEmpLevel(rs.getString("EMP_LEVEL"));
				else
					editBean.setEmpLevel("");

				if (rs.getString("DATEOFBIRTH") != null) {
					editBean.setDateofBirth(commonUtil.converDBToAppFormat(rs
							.getString("DATEOFBIRTH").toString(), "yyyy-MM-dd",
							"dd-MMM-yyyy"));
					// editBean.setDateofBirth(CommonUtil.getStringtoDate(rs.getString("DATEOFBIRTH").toString()));
				} else
					editBean.setDateofBirth("");
				if (rs.getString("DATEOFJOINING") != null)
					// editBean.setDateofJoining(CommonUtil.getStringtoDate(rs.getString("DATEOFJOINING").toString()));
					editBean.setDateofJoining(commonUtil.converDBToAppFormat(rs
							.getString("DATEOFJOINING").toString(),
							"yyyy-MM-dd", "dd-MMM-yyyy"));
				else
					editBean.setDateofJoining("");

				if (rs.getString("DATEOFSEPERATION_REASON") != null)
					editBean.setSeperationReason(rs
							.getString("DATEOFSEPERATION_REASON"));
				else
					editBean.setSeperationReason("");
				if (rs.getString("OTHERREASON") != null)
					editBean.setOtherReason(rs.getString("OTHERREASON"));
				else
					editBean.setOtherReason("");
				if (rs.getString("DATEOFSEPERATION_DATE") != null)
					// editBean.setDateofSeperationDate(CommonUtil.getStringtoDate(rs.getString("DATEOFSEPERATION_DATE").toString()));
					editBean.setDateofSeperationDate(commonUtil
							.converDBToAppFormat(rs.getString(
									"DATEOFSEPERATION_DATE").toString(),
									"yyyy-MM-dd", "dd-MMM-yyyy"));
				else
					editBean.setDateofSeperationDate("");

				if (rs.getString("WHETHER_FORM1_NOM_RECEIVED") != null)
					editBean.setForm2Nomination(rs
							.getString("WHETHER_FORM1_NOM_RECEIVED"));
				else
					editBean.setForm2Nomination("");

				if (rs.getString("airportcode") != null)
					editBean.setStation(rs.getString("airportcode"));
				else
					editBean.setStation("");
				if (rs.getString("REFPENSIONNUMBER") != null)
					editBean.setPensionNumber(rs.getString("REFPENSIONNUMBER"));
				else
					editBean.setPensionNumber("");
				if (rs.getString("emailId") != null)
					editBean.setEmailId(rs.getString("emailId"));
				else
					editBean.setEmailId("");
				if (rs.getString("AIRPORTSERIALNUMBER") != null)
					editBean.setAirportSerialNumber(rs
							.getString("AIRPORTSERIALNUMBER"));
				else
					editBean.setAirportSerialNumber("");
				if (rs.getString("employeeno") != null)
					editBean.setEmpNumber(rs.getString("employeeno"));
				else
					editBean.setEmpNumber("");
				if (rs.getString("remarks") != null)
					editBean.setRemarks(rs.getString("remarks"));
				else
					editBean.setRemarks("");
				if (rs.getString("GENDER") != null)
					editBean.setSex(rs.getString("GENDER"));
				else
					editBean.setSex("");

				if (rs.getString("superannuation1") != null)
					// editBean.setDateOfAnnuation(CommonUtil.getStringtoDate(rs.getString("setDateOfAnnuation")));
					editBean.setDateOfAnnuation(commonUtil.converDBToAppFormat(
							rs.getString("superannuation1"), "yyyy-MM-dd",
							"dd-MMM-yyyy"));
				else
					editBean.setDateOfAnnuation("");
				if (rs.getString("dateofretirement") != null)
					// editBean.setDateOfAnnuation(CommonUtil.getStringtoDate(rs.getString("setDateOfAnnuation")));
					editBean.setDateOfRetirement(commonUtil
							.converDBToAppFormat(rs
									.getString("dateofretirement"),
									"yyyy-MM-dd", "dd-MMM-yyyy"));
				else
					editBean.setDateOfRetirement("");
				if (rs.getString("fhname") != null)
					editBean.setFhName(rs.getString("fhname"));
				else
					editBean.setFhName("");
				if (rs.getString("fhflag") != null)
					editBean.setFhFlag(rs.getString("fhflag"));
				else
					editBean.setFhFlag("");
				if (rs.getString("maritalstatus") != null)
					editBean.setMaritalStatus(rs.getString("maritalstatus"));
				else
					editBean.setMaritalStatus("");
				log.info("PERMANENTADDRESS" + rs.getString("PERMANENTADDRESS"));
				if (rs.getString("PERMANENTADDRESS") != null)
					editBean.setPermanentAddress(rs
							.getString("PERMANENTADDRESS"));
				else
					editBean.setPermanentAddress("");

				if (rs.getString("TEMPORATYADDRESS") != null)
					editBean.setTemporatyAddress(rs
							.getString("TEMPORATYADDRESS"));
				else
					editBean.setTemporatyAddress("");
				if (rs.getString("wetherOption") != null) {
					editBean.setWetherOption(rs.getString("wetherOption"));
					log.info("wetheroption is" + editBean.getWetherOption());
				} else
					editBean.setWetherOption("");

				if (rs.getString("region") != null) {
					editBean.setRegion(rs.getString("region"));
					log.info("region is" + editBean.getRegion());
				} else
					editBean.setRegion("");

				if (rs.getString("otherreason") != null) {
					editBean.setOtherReason(rs.getString("otherreason"));
					log.info("otherreason is" + editBean.getOtherReason());
				} else
					editBean.setOtherReason("");

				if (rs.getString("department") != null) {
					editBean.setDepartment(rs.getString("department"));
					log.info("department is" + editBean.getDepartment());
				} else
					editBean.setDepartment("");
				if (rs.getString("division") != null) {
					editBean.setDivision(rs.getString("division"));
					log.info("division is" + editBean.getDivision());
				} else
					editBean.setDivision("");

				if (rs.getString("empnomineesharable") != null) {
					editBean.setEmpNomineeSharable(rs
							.getString("empnomineesharable"));
					log.info("empnomineesharable is"
							+ editBean.getEmpNomineeSharable());
				} else
					editBean.setEmpNomineeSharable("");

				if (rs.getString("region") != null) {
					editBean.setRegion(rs.getString("region"));
					log.info("region  is" + editBean.getRegion());
				} else
					editBean.setRegion("");
				if (rs.getString("OPTIONFORMRECEIVED") != null) {
					editBean.setOptionForm(rs.getString("OPTIONFORMRECEIVED"));
				} else {
					editBean.setOptionForm("");
				}
				// for pfacno
				if (rs.getString("PENSIONNO") != null) {
					editBean.setEmpSerialNo(rs.getString("PENSIONNO"));
					log.info("setEmpSerialNo  is" + editBean.getEmpSerialNo());
				} else
					editBean.setEmpSerialNo("");
				
				
				if (rs.getString("freshpensionoption") != null) {
					editBean.setFreshPenOption(rs.getString("freshpensionoption"));
					//log.info("setEmpSerialNo  is" + editBean.getEmpSerialNo());
				} else
					editBean.setFreshPenOption("");

				// for pfacno
				if (rs.getString("PENSIONNO") != null) {
					editBean.setEmpSerialNo(rs.getString("PENSIONNO"));
					editBean.setPfid(commonDAO.getPFID(editBean.getEmpName(),
							editBean.getDateofBirth(), commonUtil.leadingZeros(
									5, editBean.getEmpSerialNo())));
					log.info("setEmpSerialNo  is" + editBean.getEmpSerialNo());
				} else
					editBean.setEmpSerialNo("");
				/*added by mohan for deferment Pc*/
				if (rs.getString("deferement") != null)
					editBean.setDeferement(rs.getString("deferement").trim());
				else
					editBean.setDeferement("N");
				if (rs.getString("Deferementpension") != null)
					editBean.setDeferementpension(rs.getString("Deferementpension").trim());
				else
					editBean.setDeferementpension("N");
				if (rs.getString("Deferementage") != null)
					editBean.setDeferementage(rs.getString("Deferementage").trim());
				else
					editBean.setDeferementage("");
				/* Editing Family records */
				String query2 = "select FAMILYMEMBERNAME,DATEOFBIRTH,FAMILYRELATION,rowId from employee_family_dtls where LOWER(PENSIONNO) ='"
						+ pfid.toLowerCase().trim() + "'";
				log.debug("familydtls " + query2);
				rs1 = st.executeQuery(query2);
				StringBuffer sbf = new StringBuffer();
				String FAMILYMEMBERNAME = "", DATEOFBIRTH = "", FAMILYRELATION = "", rowId = "";
				while (rs1.next()) {
					if (rs.getString("FAMILYMEMBERNAME") != null) {
						FAMILYMEMBERNAME = rs.getString("FAMILYMEMBERNAME");
					} else {
						FAMILYMEMBERNAME = "xxx";
					}
					if (rs.getString("DATEOFBIRTH") != null) {

						// DATEOFBIRTH =
						// CommonUtil.getStringtoDate(rs.getString("DATEOFBIRTH"));
						DATEOFBIRTH = commonUtil.converDBToAppFormat(rs
								.getString("DATEOFBIRTH"), "yyyy-MM-dd",
								"dd-MMM-yyyy");
					} else {
						DATEOFBIRTH = "xxx";
					}
					if (rs.getString("FAMILYRELATION") != null) {
						FAMILYRELATION = rs.getString("FAMILYRELATION");
					} else {
						FAMILYRELATION = "xxx";
					}
					log.info("FAMILYRELATION   " + FAMILYRELATION);
					if (rs.getString("rowId") != null) {
						rowId = rs.getString("rowId");
					} else {
						rowId = "xxx";
					}
					sbf.append(FAMILYMEMBERNAME + "@");
					sbf.append(DATEOFBIRTH + "@");
					sbf.append(FAMILYRELATION + "@");
					sbf.append(rowId + "***");

				}
				log.info("family records " + sbf.toString());
				editBean.setFamilyRow(sbf.toString());
				/* End of Editing Family of records */

				/* Editing nominee records */
				String query3 = "select NOMINEENAME,NOMINEEADDRESS,NOMINEEDOB,NOMINEERELATION,NAMEOFGUARDIAN,GAURDIANADDRESS,TOTALSHARE,rowid from employee_nominee_dtls where EMPFLAG='Y' AND LOWER(PENSIONNO) ='"
						+ pfid.toLowerCase().trim() + "'";
				rs2 = st.executeQuery(query3);
				StringBuffer sbf1 = new StringBuffer();
				while (rs2.next()) {
					String nomineeName = "", nomineeAddress = "", nomineeDob = "";
					String nomineeRelation = "", nameofGuardian = "", guardianAddress = "", totalShare = "";

					if (rs.getString("NOMINEENAME") != null) {
						nomineeName = rs.getString("NOMINEENAME");
					} else {
						nomineeName = "xxx";
					}
					// nomineeName =rs.getString("NOMINEENAME");
					if (rs.getString("NOMINEEADDRESS") != null) {
						nomineeAddress = rs.getString("NOMINEEADDRESS");
					} else {
						nomineeAddress = "xxx";
					}
					if (rs.getString("NOMINEEDOB") != null) {
						// nomineeDob =
						// CommonUtil.getStringtoDate(rs.getString("NOMINEEDOB"));
						nomineeDob = commonUtil.converDBToAppFormat(rs
								.getString("NOMINEEDOB"), "yyyy-MM-dd",
								"dd-MMM-yyyy");
					} else {
						nomineeDob = "xxx";
					}
					if (rs.getString("NOMINEERELATION") != null) {
						nomineeRelation = rs.getString("NOMINEERELATION");
					} else {
						nomineeRelation = "xxx";
					}
					if (rs.getString("NAMEOFGUARDIAN") != null) {
						nameofGuardian = rs.getString("NAMEOFGUARDIAN");

					} else {
						nameofGuardian = "xxx";
					}
					if (rs.getString("GAURDIANADDRESS") != null) {
						guardianAddress = rs.getString("GAURDIANADDRESS");
						log.info("guardianAddress  " + guardianAddress);
					} else {
						guardianAddress = "xxx";
					}
					if (rs.getString("TOTALSHARE") != null) {
						totalShare = rs.getString("TOTALSHARE");
					} else {
						totalShare = "xxx";
					}

					if (rs.getString("rowId") != null) {
						rowId = rs.getString("rowId");
					} else {
						rowId = "xxx";
					}

					sbf1.append(nomineeName + "@");
					sbf1.append(nomineeAddress + "@");
					sbf1.append(nomineeDob + "@");
					sbf1.append(nomineeRelation + "@");
					sbf1.append(nameofGuardian + "@");
					sbf1.append(guardianAddress + "@");
					sbf1.append(totalShare + "@");
					sbf1.append(rowId + "***");
					log.info(sbf1.toString());
					// sbf1.append(nomineeRelation + "@");

				}
				log.info("nominee records " + sbf1.toString());
				editBean.setNomineeRow(sbf1.toString());

			}

		} catch (Exception e) {
			log.printStackTrace(e);
			System.out.println("Exception is" + e.getMessage());
		}

		finally {
			rs.close();
			st.close();
			con.close();
		}
		log.info("PersionalDAO:empPersonalEdit leaving method ");
		return editBean;
	}
	public EmpMasterBean getDesegnation(String empLevel) {
		Connection con = null;
		EmpMasterBean bean = new EmpMasterBean();
		ResultSet rs = null;
		String designation = "";
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			String sql = "select * from employee_desegnation where empLevel='"
					+ empLevel.trim() + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				designation = (String) rs.getString("DESIGNATION").toString()
						.trim();
				if (!designation.equals("")) {
					bean.setDesegnation(designation);
				} else {
					bean.setDesegnation("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return bean;
	}

	public int personalUpdate(EmpMasterBean bean, String flag)
			throws InvalidDataException {
		log.info("PersonalDAO:updatePensionMaster entering method ");
		// PensionBean editBean =new PensionBean();
		Connection con = null;
		Statement st = null;
		int count = 0;
		String srno = "", airportSerialNumber = "", empNumber = "", cpfAcNo = "", newCpfAcno = "";
		String empName = "", desegnation = "", empLevel = "", seperationReason = "", whetherOptionA = "";
		String whetherOptionB = "", whetherOptionNO = "", form2Nomination = "";
		String remarks = "", station = "", dateofBirth = "", dateofJoining = "", dateofSeperationDate = "";
		String fMemberName = "", fDateofBirth = "", frelation = "", familyrows = "";
		String wetherOption = "", sex = "", maritalStatus = "", fhName = "", permanentAddress = "", temporatyAddress = "", dateOfAnnuation = "", otherReason = "";
		String pfId = "";
		int count1[] = new int[3];
		pfId = bean.getPensionNumber();
		airportSerialNumber = bean.getAirportSerialNumber();
		empNumber = bean.getEmpNumber();
		cpfAcNo = bean.getCpfAcNo().trim();
		newCpfAcno = bean.getNewCpfAcNo();
		station = bean.getStation();
		empName = bean.getEmpName();
		desegnation = bean.getDesegnation();
		empLevel = bean.getEmpLevel();
		seperationReason = bean.getSeperationReason();
		whetherOptionA = bean.getWhetherOptionA();
		whetherOptionB = bean.getWhetherOptionB();
		whetherOptionNO = bean.getWhetherOptionNO();
		remarks = bean.getRemarks();
		dateofBirth = bean.getDateofBirth();
		dateofJoining = bean.getDateofJoining();
		dateofSeperationDate = bean.getDateofSeperationDate();
		form2Nomination = bean.getForm2Nomination();
		String pensionNumber = bean.getPensionNumber();
		String empNomineeSharable = bean.getEmpNomineeSharable();
		wetherOption = bean.getWetherOption();
		sex = bean.getSex();
		maritalStatus = bean.getMaritalStatus();
		fhName = bean.getFhName();
		permanentAddress = bean.getPermanentAddress();
		temporatyAddress = bean.getTemporatyAddress();
		dateOfAnnuation = bean.getDateOfAnnuation();
		// String pensionNumber = this.getPensionNumber(empName.toUpperCase(),
		// dateofBirth, cpfAcNo);
		otherReason = bean.getOtherReason().trim();
		String division = bean.getDivision();
		String department = bean.getDepartment();
		String emailId = bean.getEmailId();
		String empOldName = bean.getEmpOldName();
		String empOldNumber = bean.getEmpOldNumber();
		String region = bean.getRegion();
		String setRecordVerified = bean.getRecordVerified();
		log.info("PersonalDAO:userName " + bean.getUserName());
		log.info("PersonalDAO:computer Name" + bean.getComputerName());
		java.util.Date now = new java.util.Date();
		String MY_DATE_FORMAT = "dd-MM-yyyy hh:mm a";
		String currDateTime = new SimpleDateFormat(MY_DATE_FORMAT).format(now);
		log.info("date is  " + currDateTime);
		String fhFlag = bean.getFhFlag();
		if (!bean.getChangedRegion().equals("")) {
			region = bean.getChangedRegion();
		}
		if (!bean.getChangedStation().equals("")) {
			station = bean.getChangedStation();
		}

		String changedStation = bean.getChangedStation();

		try {
			String query = "";
			con = commonDB.getConnection();
			st = con.createStatement();
			String sql1 = "";
			pensionDAO.insertEmployeeHistory(pfId, cpfAcNo, "", true, "",
					region, currDateTime, bean.getComputerName().trim(), bean
							.getUserName());
			if (newCpfAcno.equals(cpfAcNo.trim())
					&& bean.getRegion().equals(bean.getNewRegion().trim())) {
				log.info("pensionNumber  " + pensionNumber + "cpfAcNo  "
						+ cpfAcNo + "bean.getEmpOldNumber()"
						+ bean.getEmpOldNumber() + "region===========" + region
						+ "dateofBirth" + dateofBirth);

				// pensionNumber = checkPensionNumber(pensionNumber, cpfAcNo,
				// bean.getEmpOldNumber(), empName, region.trim(), dateofBirth);
				System.out.println("pensionNumber===New========"
						+ pensionNumber);
				if (this.checkPfAcnoInEmployeeInfo(bean.getEmpSerialNo(), bean
						.getRegion()) > 0) {
					String query2 = "";
					if (bean.getEmpOldNumber().trim().equals("")
							&& bean.getEmpOldName().equals("")) {
						query2 = "update  employee_info set airportcode='"
								+ station + "',employeename='" + empName.trim()
								+ "',desegnation='" + desegnation
								+ "',AIRPORTSERIALNUMBER='"
								+ airportSerialNumber + "',EMPLOYEENO='"
								+ empNumber + "',EMP_LEVEL='" + empLevel
								+ "',DATEOFBIRTH ='" + dateofBirth
								+ "',DATEOFJOINING='" + dateofJoining
								+ "',DATEOFSEPERATION_REASON='"
								+ seperationReason
								+ "',DATEOFSEPERATION_DATE='"
								+ dateofSeperationDate + "',"
								+ "WHETHER_OPTION_A ='" + whetherOptionA
								+ "',WHETHER_OPTION_B ='" + whetherOptionB
								+ "',WHETHER_OPTION_NO='" + whetherOptionNO
								+ "',REMARKS='" + remarks.trim() + "',sex='"
								+ sex + "',maritalStatus='" + maritalStatus
								+ "',permanentAddress='" + permanentAddress
								+ "',temporatyAddress='" + temporatyAddress
								+ "',wetherOption='" + wetherOption
								+ "', WHETHER_FORM1_NOM_RECEIVED ='"
								+ form2Nomination + "',fhname='" + fhName
								+ "',setDateOfAnnuation='" + dateOfAnnuation
								+ "',pensionnumber='" + pensionNumber
								+ "',otherreason='" + otherReason
								+ "',division='" + division + "',department='"
								+ department + "',emailId='" + emailId
								+ "',lastactive='"
								+ commonUtil.getCurrentDate("dd-MMM-yyyy")
								+ "',empnomineesharable='" + empNomineeSharable
								+ "',userName='" + bean.getUserName()
								+ "',recordVerified='" + setRecordVerified
								+ "',fhflag='" + bean.getFhFlag()
								+ "',region='" + bean.getRegion()
								+ "' where empflag='Y' and  cpfacno='"
								+ cpfAcNo + "'"
								+ " and employeename is null   and region='"
								+ region.trim() + "'  and EMPLOYEENO is null ";
					} else if (bean.getEmpOldNumber().trim().equals("")) {
						query2 = "update  employee_info set airportcode='"
								+ station + "',employeename='" + empName.trim()
								+ "',desegnation='" + desegnation
								+ "',AIRPORTSERIALNUMBER='"
								+ airportSerialNumber + "',EMPLOYEENO='"
								+ empNumber + "',EMP_LEVEL='" + empLevel
								+ "',DATEOFBIRTH ='" + dateofBirth
								+ "',DATEOFJOINING='" + dateofJoining
								+ "',DATEOFSEPERATION_REASON='"
								+ seperationReason
								+ "',DATEOFSEPERATION_DATE='"
								+ dateofSeperationDate + "',"
								+ "WHETHER_OPTION_A ='" + whetherOptionA
								+ "',WHETHER_OPTION_B ='" + whetherOptionB
								+ "',WHETHER_OPTION_NO='" + whetherOptionNO
								+ "',REMARKS='" + remarks.trim() + "',sex='"
								+ sex + "',maritalStatus='" + maritalStatus
								+ "',permanentAddress='" + permanentAddress
								+ "',temporatyAddress='" + temporatyAddress
								+ "',wetherOption='" + wetherOption
								+ "', WHETHER_FORM1_NOM_RECEIVED ='"
								+ form2Nomination + "',fhname='" + fhName
								+ "',setDateOfAnnuation='" + dateOfAnnuation
								+ "',pensionnumber='" + pensionNumber
								+ "',otherreason='" + otherReason
								+ "',division='" + division + "',department='"
								+ department + "',emailId='" + emailId
								+ "',lastactive='"
								+ commonUtil.getCurrentDate("dd-MMM-yyyy")
								+ "',empnomineesharable='" + empNomineeSharable
								+ "',userName='" + bean.getUserName()
								+ "',recordVerified='" + setRecordVerified
								+ "',fhflag='" + bean.getFhFlag()
								+ "',region='" + region.trim()
								+ "'   where empflag='Y' and  cpfacno='"
								+ cpfAcNo + "' and trim(employeename)='"
								+ empOldName.trim() + "'   and region='"
								+ bean.getRegion()
								+ "'  and EMPLOYEENO is null ";
					} else if (bean.getEmpOldName().equals("")) {
						query2 = "update  employee_info set airportcode='"
								+ station + "',employeename='" + empName.trim()
								+ "',desegnation='" + desegnation
								+ "',AIRPORTSERIALNUMBER='"
								+ airportSerialNumber + "',EMPLOYEENO='"
								+ empNumber + "',EMP_LEVEL='" + empLevel
								+ "',DATEOFBIRTH ='" + dateofBirth
								+ "',DATEOFJOINING='" + dateofJoining
								+ "',DATEOFSEPERATION_REASON='"
								+ seperationReason
								+ "',DATEOFSEPERATION_DATE='"
								+ dateofSeperationDate + "',"
								+ "WHETHER_OPTION_A ='" + whetherOptionA
								+ "',WHETHER_OPTION_B ='" + whetherOptionB
								+ "',WHETHER_OPTION_NO='" + whetherOptionNO
								+ "',REMARKS='" + remarks.trim() + "',sex='"
								+ sex + "',maritalStatus='" + maritalStatus
								+ "',permanentAddress='" + permanentAddress
								+ "',temporatyAddress='" + temporatyAddress
								+ "',wetherOption='" + wetherOption
								+ "', WHETHER_FORM1_NOM_RECEIVED ='"
								+ form2Nomination + "',fhname='" + fhName
								+ "',setDateOfAnnuation='" + dateOfAnnuation
								+ "',pensionnumber='" + pensionNumber
								+ "',otherreason='" + otherReason
								+ "',division='" + division + "',department='"
								+ department + "',emailId='" + emailId
								+ "',lastactive='"
								+ commonUtil.getCurrentDate("dd-MMM-yyyy")
								+ "',empnomineesharable='" + empNomineeSharable
								+ "',userName='" + bean.getUserName()
								+ "',recordVerified='" + setRecordVerified
								+ "',fhflag='" + bean.getFhFlag()
								+ "',region='" + region.trim()
								+ "'   where empflag='Y' and  cpfacno='"
								+ cpfAcNo + "'  and region='"
								+ bean.getRegion()
								+ "'  and employeename is null ";
					} else {
						query2 = "update  employee_info set employeename='"
								+ empName.trim() + "',desegnation='"
								+ desegnation + "',AIRPORTSERIALNUMBER='"
								+ airportSerialNumber + "',EMPLOYEENO='"
								+ empNumber + "',EMP_LEVEL='" + empLevel
								+ "',DATEOFBIRTH ='" + dateofBirth
								+ "',DATEOFJOINING='" + dateofJoining
								+ "',DATEOFSEPERATION_REASON='"
								+ seperationReason
								+ "',DATEOFSEPERATION_DATE='"
								+ dateofSeperationDate + "',REMARKS='"
								+ remarks.trim() + "',sex='" + sex
								+ "',maritalStatus='" + maritalStatus
								+ "',permanentAddress='" + permanentAddress
								+ "',temporatyAddress='" + temporatyAddress
								+ "',wetherOption='" + wetherOption
								+ "', WHETHER_FORM1_NOM_RECEIVED ='"
								+ form2Nomination + "',fhname='" + fhName
								+ "',setDateOfAnnuation='" + dateOfAnnuation
								+ "',pensionnumber='" + pensionNumber
								+ "',otherreason='" + otherReason
								+ "',division='" + division + "',department='"
								+ department + "',emailId='" + emailId
								+ "',lastactive='"
								+ commonUtil.getCurrentDate("dd-MMM-yyyy")
								+ "',empnomineesharable='" + empNomineeSharable
								+ "',userName='" + bean.getUserName()
								+ "',fhflag='" + bean.getFhFlag()
								+ "'   where empflag='Y' and EMPSERIALNUMBER='"
								+ bean.getEmpSerialNo() + "'";

					}
					log.info("query2 is " + query2);
					st.executeQuery(query2);

					// new code
					// for familydata updation

					familyrows = bean.getFamilyRow();
					ArrayList familyList = commonUtil.getTheList(familyrows,
							"***");
					log.info("listsize is d  " + familyList.size());
					String tempInfo[] = null;
					String tempData = "", sql2 = "";
					String fMemberOldName = "";
					for (int i = 0; i < familyList.size(); i++) {
						tempData = familyList.get(i).toString();
						tempInfo = tempData.split("@");
						fMemberName = tempInfo[0];

						if (!tempInfo[1].equals("XXX")) {
							fDateofBirth = tempInfo[1];
						} else {
							fDateofBirth = "";
						}
						if (!tempInfo[2].equals("XXX")) {
							frelation = tempInfo[2];
						} else {
							frelation = "";
						}
						if (!tempInfo[3].equals("XXX")) {
							fMemberOldName = tempInfo[3].trim();
						} else
							fMemberOldName = "";

						if (!pensionDAO.checkFamilyDetails(fMemberOldName,
								newCpfAcno, "employee_detail").equals("")) {
							sql2 = "update  employee_family_dtls set familyMemberName='"
									+ fMemberName
									+ "',dateofBirth='"
									+ fDateofBirth
									+ "',familyRelation='"
									+ frelation
									+ "',region='"
									+ region
									+ "',SRNO='"
									+ i
									+ "' where cpfaccno='"
									+ newCpfAcno
									+ "' and familyMemberName='"
									+ fMemberOldName.trim() + "'";
							log.info("sql2 " + sql2);
							st.executeUpdate(sql2);

						} else if (!fMemberName.equals("")) {

							if (!newCpfAcno.equals(cpfAcNo)) {
								sql1 = "update  employee_family_dtls  set empflag='N' where cpfaccno='"
										+ cpfAcNo
										+ "' and familyMemberName='"
										+ fMemberName
										+ "' and region='"
										+ region.trim()
										+ "' and PENSIONNO='"
										+ bean.getEmpSerialNo() + "'";
								count = st.executeUpdate(sql1);
								log.info("family update " + sql1);
							}
							String familyDtlsAdd = "insert into employee_family_dtls(cpfaccno,familyMemberName,dateofBirth,familyRelation,region,SRNO,PENSIONNO)values"
									+ "('"
									+ newCpfAcno
									+ "','"
									+ fMemberName
									+ "','"
									+ fDateofBirth
									+ "','"
									+ frelation
									+ "','"
									+ region
									+ "','"
									+ i
									+ "','"
									+ bean.getEmpSerialNo() + "')";
							log.info("familyDtlsAdd is" + familyDtlsAdd);
							st.executeUpdate(familyDtlsAdd);
						}

						/* end of Family rows update */
					}
					String sql3 = "";
					String nomineeName = "", nomineeAddress = "", nrowid = "", nomineeDob = "", nomineeRelation = "", nameofGuardian = "", totalShare = "", gaurdianAddress = "", nomineeRows = "";
					String nomineeOldName = "";
					nomineeRows = bean.getNomineeRow();
					log.info("nomineeRows " + nomineeRows);
					ArrayList nomineeList = commonUtil.getTheList(nomineeRows,
							"***");
					DateValidation dateValidation = new DateValidation();
					int serialno = 0;
					for (int j = 0; j < nomineeList.size(); j++) {
						tempData = nomineeList.get(j).toString();
						tempInfo = tempData.split("@");
						nomineeName = tempInfo[0];
						System.out.println("tempInfo(updatePensionMaster)"
								+ tempInfo);
						if (!tempInfo[1].equals("XXX")) {
							nomineeAddress = tempInfo[1];
						} else {
							nomineeAddress = "";
						}
						if (!tempInfo[2].equals("XXX")) {
							nomineeDob = tempInfo[2].toString().trim();
						} else {
							nomineeDob = "";
						}

						if (!tempInfo[3].equals("XXX")) {
							nomineeRelation = tempInfo[3];
						} else {
							nomineeRelation = "";
						}
						if (!tempInfo[4].equals("XXX")) {
							nameofGuardian = tempInfo[4];
						} else {
							nameofGuardian = "";
						}
						if (!tempInfo[5].equals("XXX")) {
							gaurdianAddress = tempInfo[5];
						} else {
							gaurdianAddress = "";
						}
						if (!tempInfo[6].equals("XXX")) {
							nomineeOldName = tempInfo[6];
						} else {
							nomineeOldName = "";
						}

						if (!tempInfo[7].equals("XXX")) {
							totalShare = tempInfo[7];
						} else {
							totalShare = "";
						}
						if (!tempInfo[8].equals("XXX")) {
							nrowid = tempInfo[8];
						} else {
							nrowid = "";
						}
						log.info("rowid " + tempInfo[8]);
						log.info("nomineeDob" + nomineeDob);
						serialno++;
						// nomineeRows=tempInfo[5];
						if (!pensionDAO.checkFamilyDetails(nomineeOldName,
								newCpfAcno, "").equals("")) {
							sql3 = "update employee_nominee_dtls set nomineeName='"
									+ nomineeName
									+ "',nomineeAddress='"
									+ nomineeAddress
									+ "',nomineeDob='"
									+ nomineeDob
									+ "',nomineeRelation='"
									+ nomineeRelation
									+ "',nameofGuardian='"
									+ nameofGuardian
									+ "',totalshare='"
									+ totalShare
									+ "',GAURDIANADDRESS='"
									+ gaurdianAddress
									+ "' where cpfaccno='"
									+ newCpfAcno
									+ "' and nomineeName='"
									+ nomineeOldName.trim()
									+ "' and PENSIONNO='"
									+ bean.getEmpSerialNo()
									+ "' and rowid='"
									+ nrowid + "'";
							// and srno='"+j+"'";
						} else {
							if (!newCpfAcno.equals(cpfAcNo)) {
								sql1 = "update  employee_nominee_dtls  set empflag='N' where cpfaccno='"
										+ cpfAcNo
										+ "' and nomineeName='"
										+ nomineeName
										+ "' and region='"
										+ region.trim()
										+ "' and PENSIONNO='"
										+ bean.getEmpSerialNo() + "'";
								count = st.executeUpdate(sql1);
							}

							sql3 = "insert into employee_nominee_dtls(cpfaccno,nomineeName,nomineeAddress,nomineeDob,nomineeRelation,nameofGuardian,GAURDIANADDRESS,totalshare,region,SRNO,PENSIONNO)values('"
									+ newCpfAcno
									+ "','"
									+ nomineeName
									+ "','"
									+ nomineeAddress
									+ "','"
									+ nomineeDob
									+ "','"
									+ nomineeRelation
									+ "','"
									+ nameofGuardian
									+ "','"
									+ gaurdianAddress
									+ "','"
									+ totalShare
									+ "','"
									+ region
									+ "','"
									+ serialno
									+ "','"
									+ bean.getEmpSerialNo() + "')";
						}
						log.info("sql3 is" + sql3);
						st.executeUpdate(sql3);
					}
				}

				if (bean.getEmpOldNumber().trim().equals("")
						&& bean.getEmpOldName().equals("")) {
					query = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
							+ station + "',employeename='" + empName.trim()
							+ "',desegnation='" + desegnation
							+ "',AIRPORTSERIALNUMBER='" + airportSerialNumber
							+ "',EMPLOYEENO='" + empNumber + "',EMP_LEVEL='"
							+ empLevel + "',DATEOFBIRTH ='" + dateofBirth
							+ "',DATEOFJOINING='" + dateofJoining
							+ "',DATEOFSEPERATION_REASON='" + seperationReason
							+ "',DATEOFSEPERATION_DATE='"
							+ dateofSeperationDate + "',REMARKS='"
							+ remarks.trim() + "',gender='" + sex
							+ "',maritalStatus='" + maritalStatus
							+ "',permanentAddress='" + permanentAddress
							+ "',temporatyAddress='" + temporatyAddress
							+ "',wetherOption='" + wetherOption
							+ "', WHETHER_FORM1_NOM_RECEIVED ='"
							+ form2Nomination + "',fhname='" + fhName
							+ "',setDateOfAnnuation='" + dateOfAnnuation
							+ "',REFPENSIONNUMBER='" + pensionNumber
							+ "',otherreason='" + otherReason + "',division='"
							+ division + "',department='" + department
							+ "',emailId='" + emailId + "',lastactive='"
							+ commonUtil.getCurrentDate("dd-MMM-yyyy")
							+ "',empnomineesharable='" + empNomineeSharable
							+ "',userName='" + bean.getUserName()
							+ "',fhflag='" + bean.getFhFlag() + "',region='"
							+ region.trim() + "',optionformreceived='"
							+ bean.getOptionForm()
							+ "'  where empflag='Y' and  PENSIONNO='"
							+ bean.getEmpSerialNo() + "'"
							+ " and employeename is null   and region='"
							+ bean.getRegion() + "'  and EMPLOYEENO is null ";
				} else if (bean.getEmpOldNumber().trim().equals("")) {
					query = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
							+ station + "',employeename='" + empName.trim()
							+ "',desegnation='" + desegnation
							+ "',AIRPORTSERIALNUMBER='" + airportSerialNumber
							+ "',EMPLOYEENO='" + empNumber + "',EMP_LEVEL='"
							+ empLevel + "',DATEOFBIRTH ='" + dateofBirth
							+ "',DATEOFJOINING='" + dateofJoining
							+ "',DATEOFSEPERATION_REASON='" + seperationReason
							+ "',DATEOFSEPERATION_DATE='"
							+ dateofSeperationDate + "',REMARKS='"
							+ remarks.trim() + "',gender='" + sex
							+ "',maritalStatus='" + maritalStatus
							+ "',permanentAddress='" + permanentAddress
							+ "',temporatyAddress='" + temporatyAddress
							+ "',wetherOption='" + wetherOption
							+ "', WHETHER_FORM1_NOM_RECEIVED ='"
							+ form2Nomination + "',fhname='" + fhName
							+ "',setDateOfAnnuation='" + dateOfAnnuation
							+ "',REFPENSIONNUMBER='" + pensionNumber
							+ "',otherreason='" + otherReason + "',division='"
							+ division + "',department='" + department
							+ "',emailId='" + emailId + "',lastactive='"
							+ commonUtil.getCurrentDate("dd-MMM-yyyy")
							+ "',empnomineesharable='" + empNomineeSharable
							+ "',userName='" + bean.getUserName()
							+ "',fhflag='" + bean.getFhFlag() + "',region='"
							+ region.trim() + "',optionformreceived='"
							+ bean.getOptionForm()
							+ "' where empflag='Y' and  PENSIONNO='"
							+ bean.getEmpSerialNo()
							+ "' and trim(employeename)='" + empOldName.trim()
							+ "'   and region='" + bean.getRegion()
							+ "'  and EMPLOYEENO is null ";
				} else if (bean.getEmpOldName().equals("")) {
					query = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
							+ station + "',employeename='" + empName.trim()
							+ "',desegnation='" + desegnation
							+ "',AIRPORTSERIALNUMBER='" + airportSerialNumber
							+ "',EMPLOYEENO='" + empNumber + "',EMP_LEVEL='"
							+ empLevel + "',DATEOFBIRTH ='" + dateofBirth
							+ "',DATEOFJOINING='" + dateofJoining
							+ "',DATEOFSEPERATION_REASON='" + seperationReason
							+ "',DATEOFSEPERATION_DATE='"
							+ dateofSeperationDate + "',REMARKS='"
							+ remarks.trim() + "',gender='" + sex
							+ "',maritalStatus='" + maritalStatus
							+ "',permanentAddress='" + permanentAddress
							+ "',temporatyAddress='" + temporatyAddress
							+ "',wetherOption='" + wetherOption
							+ "', WHETHER_FORM1_NOM_RECEIVED ='"
							+ form2Nomination + "',fhname='" + fhName
							+ "',setDateOfAnnuation='" + dateOfAnnuation
							+ "',REFPENSIONNUMBER='" + pensionNumber
							+ "',otherreason='" + otherReason + "',division='"
							+ division + "',department='" + department
							+ "',emailId='" + emailId + "',lastactive='"
							+ commonUtil.getCurrentDate("dd-MMM-yyyy")
							+ "',empnomineesharable='" + empNomineeSharable
							+ "',userName='" + bean.getUserName()
							+ "',fhflag='" + bean.getFhFlag() + "',region='"
							+ region.trim() + "',optionformreceived='"
							+ bean.getOptionForm()
							+ "' where empflag='Y' and  PENSIONNO='"
							+ bean.getEmpSerialNo() + "'  and region='"
							+ bean.getRegion() + "'  and employeename is null ";
				} else {
					query = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
							+ station + "',employeename='" + empName.trim()
							+ "',desegnation='" + desegnation
							+ "',AIRPORTSERIALNUMBER='" + airportSerialNumber
							+ "',EMPLOYEENO='" + empNumber + "',EMP_LEVEL='"
							+ empLevel + "',DATEOFBIRTH ='" + dateofBirth
							+ "',DATEOFJOINING='" + dateofJoining
							+ "',DATEOFSEPERATION_REASON='" + seperationReason
							+ "',DATEOFSEPERATION_DATE='"
							+ dateofSeperationDate + "',REMARKS='"
							+ remarks.trim() + "',gender='" + sex
							+ "',maritalStatus='" + maritalStatus
							+ "',permanentAddress='" + permanentAddress
							+ "',temporatyAddress='" + temporatyAddress
							+ "',wetherOption='" + wetherOption
							+ "', WHETHER_FORM1_NOM_RECEIVED ='"
							+ form2Nomination + "',fhname='" + fhName
							+ "',setDateOfAnnuation='" + dateOfAnnuation
							+ "',REFPENSIONNUMBER='" + pensionNumber
							+ "',otherreason='" + otherReason + "',division='"
							+ division + "',department='" + department
							+ "',emailId='" + emailId + "',lastactive='"
							+ commonUtil.getCurrentDate("dd-MMM-yyyy")
							+ "',empnomineesharable='" + empNomineeSharable
							+ "',userName='" + bean.getUserName()
							+ "',fhflag='" + bean.getFhFlag() + "',region='"
							+ region.trim() + "',optionformreceived='"
							+ bean.getOptionForm()
							+ "'  where empflag='Y' and PENSIONNO='"
							+ bean.getEmpSerialNo()
							+ "' and trim(employeename)='" + empOldName.trim()
							+ "' and EMPLOYEENO='" + bean.getEmpOldNumber()
							+ "'  and region='" + bean.getRegion() + "'";

				}
				log.info("query is " + query);
				count = st.executeUpdate(query);
				// EmpMaster Name updated to Transaction
				String query2 = "update employee_pension_validate set MASTER_EMPNAME='"
						+ empName.trim()
						+ "' where  cpfaccno='"
						+ cpfAcNo.trim()
						+ "' and region='"
						+ region.trim()
						+ "'";
				st.executeUpdate(query2);
				log.info("query2 is " + query2);

			} else {
				int foundCPFNO = pensionDAO.empEditCpfAcno(empName, station,
						desegnation, newCpfAcno, bean.getNewRegion());
				// pensionNumber = pensionDAO.checkPensionNumber("", newCpfAcno,
				// empNumber,empName, region.trim(), dateofBirth);

				System.out.println("foundCPFNO====empEditCpfAcno===="
						+ foundCPFNO + "new pensionNumber==============="
						+ pensionNumber);
				// modified on 14 th
				// if (foundCPFNO == 0) {
				if (!flag.equals("false")) {
					sql1 = "update  employee_info  set empflag='N' where cpfacno='"
							+ cpfAcNo.trim()
							+ "' and employeename='"
							+ empName.trim()
							+ "' and region='"
							+ region.trim()
							+ "'";
					// count = st.executeUpdate(sql1);

				}

				String sql3 = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
						+ station
						+ "',cpfacno='"
						+ newCpfAcno
						+ "',employeename='"
						+ empName.trim()
						+ "',desegnation='"
						+ desegnation
						+ "',AIRPORTSERIALNUMBER='"
						+ airportSerialNumber
						+ "',EMPLOYEENO='"
						+ empNumber
						+ "',EMP_LEVEL='"
						+ empLevel
						+ "',DATEOFBIRTH ='"
						+ dateofBirth
						+ "',DATEOFJOINING='"
						+ dateofJoining
						+ "',DATEOFSEPERATION_REASON='"
						+ seperationReason
						+ "',DATEOFSEPERATION_DATE='"
						+ dateofSeperationDate
						+ "',REMARKS='"
						+ remarks.trim()
						+ "',gender='"
						+ sex
						+ "',maritalStatus='"
						+ maritalStatus
						+ "',permanentAddress='"
						+ permanentAddress
						+ "',temporatyAddress='"
						+ temporatyAddress
						+ "',wetherOption='"
						+ wetherOption
						+ "', WHETHER_FORM1_NOM_RECEIVED ='"
						+ form2Nomination
						+ "',fhname='"
						+ fhName
						+ "',setDateOfAnnuation='"
						+ dateOfAnnuation
						+ "',REFPENSIONNUMBER='"
						+ pensionNumber
						+ "',otherreason='"
						+ otherReason
						+ "',division='"
						+ division
						+ "',department='"
						+ department
						+ "',emailId='"
						+ emailId
						+ "',lastactive='"
						+ commonUtil.getCurrentDate("dd-MMM-yyyy")
						+ "',empnomineesharable='"
						+ empNomineeSharable
						+ "',userName='"
						+ bean.getUserName()
						+ "',fhflag='"
						+ bean.getFhFlag()
						+ "',region='"
						+ region.trim()
						+ "'  where empflag='Y' and PENSIONNO='"
						+ bean.getEmpSerialNo()
						+ "' and trim(employeename)='"
						+ empOldName.trim()
						+ "'  and region='"
						+ bean.getRegion() + "'";
				String sql2 = "update employee_info set DATEOFBIRTH ='"
						+ dateofBirth + "',DATEOFJOINING='" + dateofJoining
						+ "',DATEOFSEPERATION_REASON='" + seperationReason
						+ "',DATEOFSEPERATION_DATE='" + dateofSeperationDate
						+ "',REMARKS='" + remarks.trim() + "',sex='" + sex
						+ "',maritalStatus='" + maritalStatus
						+ "',permanentAddress='" + permanentAddress
						+ "',temporatyAddress='" + temporatyAddress
						+ "',wetherOption='" + wetherOption
						+ "'  where empflag='Y' and empserialnumber='"
						+ bean.getEmpSerialNo() + "' and trim(employeename)='"
						+ empOldName.trim() + "'  and region='"
						+ bean.getRegion() + "'";

				log.info("sql one " + sql2);
				log.info("sql two " + sql3);

				st.addBatch(sql2);
				st.addBatch(sql3);
				count1 = st.executeBatch();
				for (int i = 0; i < count1.length; i++) {
					count = count1[i];
				}

				/*
				 * } else { throw new InvalidDataException( "CPF ACC.NO already
				 * Exist in the Selected Region"); }
				 */
			}

		} catch (SQLException sqle) {
			log.printStackTrace(sqle);
			if (sqle.getErrorCode() == 00001) {
				throw new InvalidDataException("PensionNumber Already Exist");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, null);
		}
		log.info("PersonalDAO:updatePensionMaster leaving method ");
		return count;
	}

	public int checkPfAcnoInEmployeeInfo(String pfId, String region) {
		Connection con = null;
		Statement st = null;
		int count = 0;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "select EMPSERIALNUMBER from employee_info where region='"
					+ region + "' and EMPSERIALNUMBER='" + pfId + "'";
			count = st.executeUpdate(query);
		} catch (Exception e) {

		}

		return count;
	}

	public String addPersonalInfo(EmployeePersonalInfo bean,
			NomineeBean nomineeInfo, String userName, String ipAddress)
			throws Exception {
		log.info("PersonalDao :addPersonalInfo() entering method");
		boolean isaddPensionRecord = false;
		Connection conn = null;
		String pensionNo = "", refPensionNo = "", familyrows = "", nomineeRows = "", fMemberName = "", fDateofBirth = "", frelation = "";
		String nomineeName = "", nomineeAddress = "", nomineeDob = "", nomineeRelation = "", nameofGuardian = "", gaddress = "", totalShare = "";
		int success = 0;
		String uniqueID = "";

		Statement st = null;
		try {
			conn = commonDB.getConnection();
			pensionNo = this.getSequenceNo(conn);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			log.printStackTrace(e1);
			throw e1;
		}
		log.info(" maritalStatus " + bean.getMaritalStatus());

		refPensionNo = commonDAO.getPFID(bean.getEmployeeName(), bean
				.getDateOfBirth(), pensionNo);
		log.info("Pension No" + pensionNo + "refPensionNo" + refPensionNo);
		String empFlag = "Y";
		try {

			st = conn.createStatement();
			String sql = "insert into employee_personal_info(REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,"
					+ "GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,username,LASTACTIVE,RefMonthYear,FHFLAG,IPAddress)"
					+ " VALUES "
					+ "('"
					+ refPensionNo
					+ "','"
					+ bean.getCpfAccno()
					+ "','"
					+ bean.getAirportSerialNumber()
					+ "','"
					+ bean.getEmployeeNumber()
					+ "','"
					+ bean.getEmployeeName().toUpperCase()
					+ "','"
					+ bean.getDesignation()
					+ "','"
					+ bean.getEmpDesLevel()
					+ "','"
					+ bean.getDateOfBirth()
					+ "','"
					+ bean.getDateOfJoining()
					+ "','"
					+ bean.getSeperationReason()
					+ "','"
					+ bean.getSeperationDate()
					+ "','"
					+ bean.getForm2Nominee()
					+ "','"
					+ bean.getRemarks()
					+ "','"
					+ bean.getAirportCode()
					+ "','"
					+ bean.getGender()
					+ "','"
					+ bean.getFhName()
					+ "','"
					+ bean.getMaritalStatus()
					+ "','"
					+ bean.getPerAddress()
					+ "','"
					+ bean.getTempAddress()
					+ "','"
					+ bean.getWetherOption()
					+ "','"
					+ bean.getDateOfAnnuation()
					+ "','"
					+ bean.getDivision()
					+ "','"
					+ bean.getDepartment()
					+ "','"
					+ bean.getEmailID()
					+ "','"
					+ bean.getEmpNomineeSharable()
					+ "','"
					+ bean.getRegion()
					+ "','"
					+ pensionNo
					+ "','"
					+ userName
					+ "','"
					+ commonUtil.getCurrentDate("dd-MMM-yyyy")
					+ "','"
					+ commonUtil.getCurrentDate("dd-MMM-yyyy")
					+ "','"
					+ bean.getFhFlag() + "','" + ipAddress + "')";
			log.info(sql);
			success = st.executeUpdate(sql);
			if (success != 0) {
				uniqueID = pensionNo;
			}
			familyrows = nomineeInfo.getFamilyDetail();
			nomineeRows = nomineeInfo.getNomineeDetail();
			ArrayList nomineeList = commonUtil.getTheList(nomineeRows, "***");
			ArrayList familyList = commonUtil.getTheList(familyrows, "***");

			String tempInfo[] = null;
			String tempData = "";

			for (int i = 0; i < familyList.size(); i++) {
				tempData = familyList.get(i).toString();
				tempInfo = tempData.split("@");
				fMemberName = tempInfo[0];

				if (!tempInfo[1].equals("XXX")) {
					fDateofBirth = tempInfo[1];
				} else {
					fDateofBirth = "";
				}
				if (!tempInfo[2].equals("XXX")) {
					frelation = tempInfo[2];
				} else {
					frelation = "";
				}
				if (frelation.equals("") && fDateofBirth.equals("")
						&& fMemberName.equals("")) {
					log
							.info("Data is not entered frelation,fDateofBirth,fMemberName");
				} else if (!fMemberName.equals("")) {
					String sql2 = "insert into employee_family_dtls(cpfaccno,srno,familyMemberName,dateofBirth,familyRelation,region,PENSIONNO)values"
							+ "('"
							+ bean.getCpfAccno()
							+ "','"
							+ i
							+ "','"
							+ fMemberName
							+ "','"
							+ fDateofBirth
							+ "','"
							+ frelation
							+ "','"
							+ bean.getRegion()
							+ "','"
							+ pensionNo + "')";
					log.info("sql2 is" + sql2);
					st.executeUpdate(sql2);
				}
			}
			int serialno = 0;
			for (int i = 0; i < nomineeList.size(); i++) {
				serialno++;
				tempData = nomineeList.get(i).toString();
				tempInfo = tempData.split("@");
				nomineeName = tempInfo[0];

				if (!tempInfo[1].equals("XXX")) {
					nomineeAddress = tempInfo[1];
				} else {
					nomineeAddress = "";
				}
				if (!tempInfo[2].equals("XXX")) {
					nomineeDob = tempInfo[2];
				} else {
					nomineeDob = "";
				}

				if (!tempInfo[3].equals("XXX")) {
					nomineeRelation = tempInfo[3];
				} else {
					nomineeRelation = "";
				}
				if (!tempInfo[4].equals("XXX")) {
					nameofGuardian = tempInfo[4];
				} else {
					nameofGuardian = "";
				}
				if (!tempInfo[5].equals("XXX")) {
					gaddress = tempInfo[5];
					log.info("gaddress ******* " + gaddress);
				} else {
					gaddress = "";
				}
				if (!tempInfo[6].equals("XXX")) {
					totalShare = tempInfo[6];
				} else {
					totalShare = "";
				}

				if (totalShare.equals("") && nameofGuardian.equals("")
						&& nomineeRelation.equals("") && nomineeDob.equals("")
						&& nomineeAddress.equals("") && nomineeName.equals("")) {

				} else {
					String sql3 = "insert into employee_nominee_dtls(cpfaccno,srno,nomineeName,nomineeAddress,nomineeDob,nomineeRelation,nameofGuardian,gaurdianAddress,totalshare,region,PENSIONNO)values('"
							+ bean.getCpfAccno()
							+ "','"
							+ serialno
							+ "','"
							+ nomineeName
							+ "','"
							+ nomineeAddress
							+ "','"
							+ nomineeDob
							+ "','"
							+ nomineeRelation
							+ "','"
							+ nameofGuardian
							+ "','"
							+ gaddress
							+ "','"
							+ totalShare
							+ "','"
							+ bean.getRegion()
							+ "','"
							+ pensionNo + "')";
					log.info("sql3 is" + sql3);
					st.executeUpdate(sql3);
				}
			}
			isaddPensionRecord = true;
			// }

		} catch (SQLException e) {
			log.printStackTrace(e);
			throw new InvalidDataException();
			// e.printStackTrace();
		} catch (Exception e) {
			log.printStackTrace(e);
			// e.printStackTrace();
		} finally {
			commonDB.closeConnection(conn, st, null);
		}
		log.info("PersonalDao :addPersonalInfo()  leaving method");
		return uniqueID;
	}

	/*
	 * public String getPFID(String empName, String dateofBirth, String
	 * sequenceNo) { log.info("PersonalDao:getPFID() entering method");
	 * log.info("PersonalDao:getPFID() dateofBirth" + dateofBirth + "empName" +
	 * empName); // TODO Auto-generated method stub String finalName = "", fname =
	 * ""; SimpleDateFormat fromDate = null; int endIndxName = 0; String quats[] = {
	 * "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." }; String tempName = "",
	 * convertedDt = "";
	 * 
	 * try { if (dateofBirth.indexOf("-") != -1) { fromDate = new
	 * SimpleDateFormat("dd-MMM-yyyy"); } else { fromDate = new
	 * SimpleDateFormat("dd/MMM/yyyy"); } SimpleDateFormat toDate = new
	 * SimpleDateFormat("ddMMyy"); convertedDt =
	 * toDate.format(fromDate.parse(dateofBirth)); log.info("convertedDt" +
	 * convertedDt); int startIndxName = 0, index = 0; endIndxName = 1; for (int
	 * i = 0; i < quats.length; i++) { if (empName.indexOf(quats[i]) != -1) {
	 * tempName = empName.replaceAll(quats[i], "").trim(); //
	 * tempName=empName.substring(index+1,empName.length()); empName = tempName; } }
	 * finalName = empName.substring(startIndxName, endIndxName); finalName =
	 * empName.substring(startIndxName, endIndxName); if (empName.indexOf(" ") !=
	 * -1) { endIndxName = empName.lastIndexOf(" "); finalName = finalName +
	 * empName.substring(endIndxName).trim(); } else if (empName.indexOf(".") !=
	 * -1) { endIndxName = empName.lastIndexOf("."); finalName = finalName +
	 * empName.substring(endIndxName + 1, empName.length()) .trim(); } else {
	 * finalName = empName.substring(0, 2); } log.info("final name is" +
	 * finalName); char[] cArray = finalName.toCharArray(); fname =
	 * String.valueOf(cArray[0]); fname = fname + String.valueOf(cArray[1]);
	 * log.info(empName + " Short Name of " + fname);
	 *  } catch (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (Exception e) { log.info("Exception is " +
	 * e);
	 *  } log.info("PersonalDao:getPFID() leaving method"); return convertedDt +
	 * fname + sequenceNo;
	 *  }
	 */

	private String validateAlphaCPFAccno(char[] frmtDt) {
		StringBuffer buff = new StringBuffer();
		StringBuffer digitBuff = new StringBuffer();
		for (int i = 0; i < frmtDt.length; i++) {
			char c = frmtDt[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				buff.append(c);
			} else if (c >= '0' && c <= '9') {
				buff.append(c);
			} else if (c >= '-') {
				digitBuff.append(c);
			}

		}
		String validDt = buff.toString();
		log.info("cpfacno " + validDt);
		return validDt;

	}

	public ArrayList checkPersonalDtOfBirthInfo(
			EmployeePersonalInfo personalInfo, boolean empflag, boolean dobFlag) {
		log.info("PersonalDAO :checkPersonalDtOfBirthInfo() entering method");
		Connection con = null;
		String empNameCheck = "";
		EmployeePersonalInfo personal = null;
		ArrayList reportPersList = new ArrayList();

		int totalRecords = 0;

		String selectSQL = this.personalConditionsBuildQuery(personalInfo,
				empflag, dobFlag);
		log.info("Query =====" + selectSQL);
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(selectSQL);
			while (rs.next()) {
				personal = new EmployeePersonalInfo();
				personal = loadPersonalInfo(rs);
				reportPersList.add(personal);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("PersonalDAO :checkPersonalDtOfBirthInfo() leaving method");
		return reportPersList;
	}

	public String personalConditionsBuildQuery(EmployeePersonalInfo bean,
			boolean empflag, boolean dobFlag) {

		log.info("PersonalDao:personalConditionsBuildQuery-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "", sqlQuery = "";
		sqlQuery = "SELECT  * from employee_personal_info where empflag='Y' ";
		log.info("EMployeename" + bean.getEmployeeName() + "empflag" + empflag);
		if (!bean.getEmployeeName().equals("") && empflag == true) {
			whereClause.append(" LOWER(employeename) like'%"
					+ bean.getEmployeeName().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfBirth().equals("") && dobFlag == true) {
			whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-Mon-yyyy')='"
					+ bean.getDateOfBirth() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (bean.getEmployeeName().equals("")
				&& (bean.getDateOfBirth().equals(""))) {
			log.info("inside if");
		} else {
			log.info("inside else");
			if (!prefixWhereClause.equals("")) {
				query.append(" AND ");
			} else {
				query.append(" AND ");
			}
		}

		query.append(this.sTokenFormat(whereClause));
		String orderBy = " ORDER BY cpfacno";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("PersonalDao:personalConditionsBuildQuery-- Leaving Method");
		return dynamicQuery;
	}

	public int processinPersonalInfoMissingRecords(String cpfAccno,
			String region) throws SQLException {
		log.info("processinPersonalInfo========");
		String sqlSelQuery = "", totalRecords = "", pensionNumber = "", insertQry = "", updateQry = "", updateNomineeQry = "", updateFamilDetails = "";
		Connection con = null;
		Statement st = null;
		Statement perSt = null;
		Statement nomineeSt = null;
		Statement familySt = null;
		String userName = "SEHGAL";
		String selectedDate = "01-Sep-2007";
		String IPAddress = "192.168.2.13";

		int i = 0, totalInserted = 0, totalFail = 0, j = 0, k = 0;
		try {
			con = commonDB.getConnection();
			pensionNumber = this.getSequenceNo(con);
			if (!pensionNumber.equals("")) {
				sqlSelQuery = "select PENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,"
						+ "WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,SEX,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,"
						+ Long.parseLong(pensionNumber)
						+ ",'"
						+ userName
						+ "','"
						+ commonUtil.getCurrentDate("dd-MMM-yyyy")
						+ "','"
						+ selectedDate
						+ "','"
						+ IPAddress
						+ "' "
						+ " FROM employee_info WHERE  ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ cpfAccno.trim()
						+ "' AND REGION='"
						+ region.trim()
						+ "')";
				log.info(sqlSelQuery);
				insertQry = "insert into employee_personal_info(REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,"
						+ "GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,username,LASTACTIVE,RefMonthYear,IPAddress)  "
						+ sqlSelQuery;
				updateQry = "UPDATE EMPLOYEE_INFO SET EMPSERIALNUMBER='"
						+ pensionNumber
						+ "' WHERE ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ cpfAccno.trim() + "' AND REGION='" + region.trim()
						+ "')";
				updateNomineeQry = "UPDATE EMPLOYEE_NOMINEE_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ cpfAccno.trim() + "' AND REGION='" + region.trim()
						+ "'";
				updateFamilDetails = "UPDATE EMPLOYEE_FAMILY_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ cpfAccno.trim() + "' AND REGION='" + region.trim()
						+ "'";

				log.info("insertQry  " + insertQry);
				log.info("updateQry " + updateQry);
				log.info("updateNomineeQry  " + updateNomineeQry);
				log.info("updateFamilDetails   " + updateFamilDetails);
				// con = DBUtils.getConnection();
				st = con.createStatement();
				perSt = con.createStatement();
				nomineeSt = con.createStatement();
				familySt = con.createStatement();
				con.setAutoCommit(false);
				log.info(insertQry);
				i = st.executeUpdate(insertQry);

				i = perSt.executeUpdate(updateQry);
				j = nomineeSt.executeUpdate(updateNomineeQry);
				k = familySt.executeUpdate(updateFamilDetails);

				con.commit();
				con.setAutoCommit(true);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
			if (con != null) {
				con.rollback();
			}

		} catch (Exception e) {
			log.printStackTrace(e);
			if (con != null) {
				con.rollback();
			}

		} finally {
			if (perSt != null) {
				try {
					perSt.close();
					perSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (nomineeSt != null) {
				try {
					nomineeSt.close();
					nomineeSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (familySt != null) {
				try {
					familySt.close();
					familySt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			commonDB.closeConnection(con, st, null);
		}

		return i;
	}

	public ArrayList form2NomineeReport(EmployeePersonalInfo personalInfo) {
		log.info("PersonalDAO :form2NomineeReport() entering method");
		Connection con = null;
		String empNameCheck = "";
		NomineeBean nomineeInfo = null;
		ArrayList reportNomineeList = new ArrayList();

		if (personalInfo.getPensionNo() != null) {
			personalInfo.setPensionNo(commonUtil.trailingZeros(personalInfo
					.getPensionNo().toCharArray()));
		}
		String selectNomineeSQL = "SELECT * FROM EMPLOYEE_NOMINEE_DTLS WHERE PENSIONNO='"
				+ personalInfo.getPensionNo() + "' ORDER BY SRNO";
		log.info("Query =====" + selectNomineeSQL);
		Statement st = null;
		ResultSet rs = null;

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectNomineeSQL);
			while (rs.next()) {
				nomineeInfo = new NomineeBean();
				nomineeInfo = loadNominneeInfo(rs);
				reportNomineeList.add(nomineeInfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("PersonalDAO :form2NomineeReport() leaving method");
		return reportNomineeList;
	}

	public ArrayList form2FamilyReport(EmployeePersonalInfo personalInfo) {
		log.info("PersonalDAO :form2FamilyReport() entering method");
		Connection con = null;
		NomineeBean familyInfo = null;
		ArrayList reportFamilyList = new ArrayList();
		int totalRecords = 0;
		if (personalInfo.getPensionNo() != null) {
			personalInfo.setPensionNo(commonUtil.trailingZeros(personalInfo
					.getPensionNo().toCharArray()));
		}
		/*
		 * String selectFamilySQL = "SELECT * FROM EMPLOYEE_FAMILY_DTLS WHERE
		 * SRNO='" + personalInfo.getPensionNo() + "' AND CPFACCNO='" +
		 * personalInfo.getCpfAccno() + "'";
		 */
		String selectFamilySQL = "SELECT * FROM EMPLOYEE_FAMILY_DTLS WHERE PENSIONNO='"
				+ personalInfo.getPensionNo() + "' ORDER BY SRNO";
		log.info("Query =====" + selectFamilySQL);
		Statement st = null;
		ResultSet rs = null;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectFamilySQL);
			while (rs.next()) {
				familyInfo = new NomineeBean();
				familyInfo = loadFamilyInfo(rs);
				reportFamilyList.add(familyInfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("PersonalDAO :form2FamilyReport() leaving method");
		return reportFamilyList;
	}

	private NomineeBean loadNominneeInfo(ResultSet rs) throws SQLException {
		NomineeBean nominee = new NomineeBean();
		log.info("loadNominneeInfo==============");
		log.info("loadNominneeInfo==============" + rs.getString("SRNO"));
		if (rs.getString("CPFACCNO") != null) {
			nominee.setCpfaccno(rs.getString("CPFACCNO"));
		} else {
			nominee.setCpfaccno("");
		}
		if (rs.getString("SRNO") != null) {
			nominee.setSrno(rs.getString("SRNO"));
		} else {
			nominee.setSrno("---");
		}
		if (rs.getString("NOMINEENAME") != null) {
			nominee.setNomineeName(rs.getString("NOMINEENAME"));
		} else {
			nominee.setNomineeName("---");
		}
		if (rs.getString("NOMINEEDOB") != null) {
			nominee.setNomineeDob(commonUtil.converDBToAppFormat(rs
					.getDate("NOMINEEDOB")));
		} else {
			nominee.setNomineeDob("---");
		}
		if (rs.getString("NOMINEEADDRESS") != null) {
			nominee.setNomineeAddress(rs.getString("NOMINEEADDRESS"));
		} else {
			nominee.setNomineeAddress("---");
		}
		if (rs.getString("NOMINEERELATION") != null) {
			nominee.setNomineeRelation(rs.getString("NOMINEERELATION"));
		} else {
			nominee.setNomineeRelation("---");
		}
		if (rs.getString("NAMEOFGUARDIAN") != null) {
			nominee.setNameOfGuardian(rs.getString("NAMEOFGUARDIAN"));
		} else {
			nominee.setNameOfGuardian("---");
		}

		if (rs.getString("TOTALSHARE") != null) {
			nominee.setTotalShare(rs.getString("TOTALSHARE"));
		} else {
			nominee.setTotalShare("---");
		}
		if (rs.getString("GAURDIANADDRESS") != null) {
			nominee.setGaurdianAddress(rs.getString("GAURDIANADDRESS"));
		} else {
			nominee.setGaurdianAddress("---");
		}
		if (rs.getString("GAURDIANADDRESS") != null) {
			nominee.setGaurdianAddress(rs.getString("GAURDIANADDRESS"));
		} else {
			nominee.setGaurdianAddress("---");
		}
		if (rs.getString("GAURDIANADDRESS") != null) {
			nominee.setGaurdianAddress(rs.getString("GAURDIANADDRESS"));
		} else {
			nominee.setGaurdianAddress("---");
		}
		if (rs.getString("SAVINGACC") != null) {
			nominee.setNomineeAccNo(rs.getString("SAVINGACC"));
		} else {
			nominee.setNomineeAccNo("---");
		}
		if (rs.getString("NOMINEERETCAP") != null) {
			nominee.setNomineeReturnFlag(rs.getString("NOMINEERETCAP"));
		} else {
			nominee.setNomineeReturnFlag("");
		}
		return nominee;

	}

	private NomineeBean loadFamilyInfo(ResultSet rs) throws SQLException {
		NomineeBean nominee = new NomineeBean();
		log.info("loadFamilyInfo==============");
		log.info("loadFamilyInfo==============" + rs.getString("SRNO"));
		if (rs.getString("CPFACCNO") != null) {
			nominee.setCpfaccno(rs.getString("CPFACCNO"));
		} else {
			nominee.setCpfaccno("---");
		}
		if (rs.getString("SRNO") != null) {
			nominee.setSrno(rs.getString("SRNO"));
		} else {
			nominee.setSrno("---");
		}
		if (rs.getString("FAMILYMEMBERNAME") != null) {
			nominee.setFamilyMemberName(rs.getString("FAMILYMEMBERNAME"));
		} else {
			nominee.setFamilyMemberName("---");
		}
		if (rs.getString("DATEOFBIRTH") != null) {
			nominee.setFamilyDateOfBirth(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFBIRTH")));
		} else {
			nominee.setFamilyDateOfBirth("---");
		}
		if (rs.getString("FAMILYRELATION") != null) {
			nominee.setFamilyRelation(rs.getString("FAMILYRELATION"));
		} else {
			nominee.setFamilyRelation("---");
		}
		return nominee;

	}

	public String personalReportBuildQuery(EmployeePersonalInfo bean,
			String flag, String empNamechek, String sortingColumn) {

		log.info("PersonalDao:personalBuildQuery-- Entering Method"
				+ empNamechek + sortingColumn);
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "", sqlQuery = "";
		log.info("empname check " + bean.getChkEmpNameFlag()
				+ "empNamechek====" + empNamechek);
		if (!flag.equals("count")) {
			sqlQuery = "SELECT INFO.CPFACNO  AS  CPFACNO,INFO.REFPENSIONNUMBER  AS  REFPENSIONNUMBER,INFO.NEWEMPCODE as NEWEMPCODE,INFO.AIRPORTSERIALNUMBER AS  AIRPORTSERIALNUMBER,INFO.EMPLOYEENO AS  EMPLOYEENO,INFO.EMPLOYEENAME AS  EMPLOYEENAME,INFO.DESEGNATION  AS  DESEGNATION,INFO.EMP_LEVEL  AS  EMP_LEVEL,INFO.DATEOFBIRTH  AS  DATEOFBIRTH,INFO.uanno as uanno, "
					+ "INFO.DATEOFJOINING AS  DATEOFJOINING,INFO.DATEOFSEPERATION_REASON AS  DATEOFSEPERATION_REASON,INFO.DATEOFSEPERATION_DATE  AS  DATEOFSEPERATION_DATE,INFO.WHETHER_FORM1_NOM_RECEIVED AS  WHETHER_FORM1_NOM_RECEIVED,"
					+ "INFO.AIRPORTCODE AS  AIRPORTCODE,INFO.GENDER AS  GENDER,INFO.FHNAME AS  FHNAME,INFO.MARITALSTATUS AS  MARITALSTATUS,INFO.PERMANENTADDRESS AS  PERMANENTADDRESS,INFO.TEMPORATYADDRESS AS  TEMPORATYADDRESS,INFO.WETHEROPTION  AS  WETHEROPTION,INFO.SETDATEOFANNUATION AS  SETDATEOFANNUATION,INFO.EMPFLAG AS  EMPFLAG,"
					+ "INFO.OTHERREASON AS  OTHERREASON,INFO.DIVISION  AS  DIVISION, INFO.DEPARTMENT  AS  DEPARTMENT, INFO.EMAILID AS  EMAILID,INFO.EMPNOMINEESHARABLE AS  EMPNOMINEESHARABLE,INFO.REMARKS AS  REMARKS,INFO.PENSIONNO AS  PENSIONNO,INFO.REGION  AS  REGION,INFO.USERNAME  AS  USERNAME,"
					+ "INFO.LASTACTIVE AS  LASTACTIVE,INFO.REFMONTHYEAR AS  REFMONTHYEAR,INFO.IPADDRESS AS  IPADDRESS,INFO.FHFLAG AS  FHFLAG,(case when (f.purpose is not null) then f.purpose when(info.dateofseperation_reason is null and info.dateofseperation_date is null) then 'Active' when(info.dateofseperation_reason is null and info.dateofseperation_date is not null) then '-NA-' else info.dateofseperation_reason end) as status,(select f.freshpensionoption from employee_pension_freshoption f where f.pensionno=info.pensionno and f.deleteflag='N') as freshoption  from employee_personal_info info,epis_pfid_year_wise_frozen f where info.pensionno = f.pensionno(+)  and  info.empflag='Y' "
					+ empNamechek;
			/*
			 * sqlQuery = "SELECT INFO.CPFACNO AS CPFACNO,INFO.REFPENSIONNUMBER
			 * AS REFPENSIONNUMBER,INFO.AIRPORTSERIALNUMBER AS
			 * AIRPORTSERIALNUMBER,INFO.EMPLOYEENO AS
			 * EMPLOYEENO,INFO.EMPLOYEENAME AS EMPLOYEENAME,INFO.DESEGNATION AS
			 * DESEGNATION,INFO.EMP_LEVEL AS EMP_LEVEL,INFO.DATEOFBIRTH AS
			 * DATEOFBIRTH, " + "INFO.DATEOFJOINING AS
			 * DATEOFJOINING,INFO.DATEOFSEPERATION_REASON AS
			 * DATEOFSEPERATION_REASON,INFO.DATEOFSEPERATION_DATE AS
			 * DATEOFSEPERATION_DATE,INFO.WHETHER_FORM1_NOM_RECEIVED AS
			 * WHETHER_FORM1_NOM_RECEIVED,"+ "INFO.AIRPORTCODE AS
			 * AIRPORTCODE,INFO.GENDER AS GENDER,INFO.FHNAME AS
			 * FHNAME,INFO.MARITALSTATUS AS MARITALSTATUS,INFO.PERMANENTADDRESS
			 * AS PERMANENTADDRESS,INFO.TEMPORATYADDRESS AS
			 * TEMPORATYADDRESS,INFO.WETHEROPTION AS
			 * WETHEROPTION,INFO.SETDATEOFANNUATION AS
			 * SETDATEOFANNUATION,INFO.EMPFLAG AS EMPFLAG,"+ "INFO.OTHERREASON
			 * AS OTHERREASON,INFO.DIVISION AS DIVISION, INFO.DEPARTMENT AS
			 * DEPARTMENT, INFO.EMAILID AS EMAILID,INFO.EMPNOMINEESHARABLE AS
			 * EMPNOMINEESHARABLE,INFO.REMARKS AS REMARKS,INFO.PENSIONNO AS
			 * PENSIONNO,INFO.REGION AS REGION,INFO.USERNAME AS USERNAME,"+
			 * "INFO.LASTACTIVE AS LASTACTIVE,INFO.REFMONTHYEAR AS
			 * REFMONTHYEAR,INFO.IPADDRESS AS IPADDRESS,INFO.FHFLAG AS FHFLAG
			 * from employee_personal_info info where info.empflag='Y' " +
			 * empNamechek;
			 */
			/*
			 * sqlQuery = "SELECT
			 * CPFACNO,REFPENSIONNUMBER,EMPLOYEENO,EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,AIRPORTCODE,GENDER,FHNAME" +
			 * ",MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,OTHERREASON,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REMARKS,PENSIONNO,REGION,LASTACTIVE
			 * from employee_personal_info where empflag='Y'";
			 */
		} else {
			sqlQuery = "SELECT COUNT(*) as COUNT from employee_personal_info i,epis_pfid_year_wise_frozen f where i.pensionno = f.pensionno(+) and empflag='Y' "
					+ empNamechek;
		}

		if (!bean.getCpfAccno().equals("")) {
			whereClause.append(" LOWER(info.cpfacno) ='"
					+ bean.getCpfAccno().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getPensionNo().equals("")) {
			whereClause.append(" info.PENSIONNO =" + bean.getPensionNo());
			whereClause.append(" AND ");
		}
		if (!bean.getUanno().equals("")) {
			whereClause.append(" info.uanno =" + bean.getUanno());
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfBirth().equals("")) {
			whereClause
					.append(" TO_CHAR(info.DATEOFBIRTH,'dd-Mon-yyyy') like '"
							+ bean.getDateOfBirth() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getAirportCode().equals("")) {
			whereClause.append(" LOWER(info.airportcode)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeName().equals("")) {
			whereClause.append(" LOWER(info.employeename) like'%"
					+ bean.getEmployeeName().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		if (!bean.getDesignation().equals("")) {
			whereClause.append(" LOWER(info.desegnation)='"
					+ bean.getDesignation().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeNumber().equals("")) {
			whereClause.append(" info.EMPLOYEENO='" + bean.getEmployeeNumber()
					+ "'");
			whereClause.append(" AND ");
		}
		if (!bean.getRegion().equals("")) {
			whereClause.append(" info.region='" + bean.getRegion() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (bean.getUanno().equals("") && bean.getAirportCode().equals("")
				&& bean.getPensionNo().equals("")
				&& (bean.getDateOfBirth().equals(""))
				&& bean.getCpfAccno().equals("")
				&& (bean.getEmployeeName().equals(""))
				&& (bean.getDesignation().equals("") && (bean.getCpfAccno()
						.equals("")
						&& (bean.getEmployeeNumber().equals("")) && (bean
						.getRegion().equals(""))))) {
			log.info("inside if");
		} else {
			log.info("inside else");
			if (!prefixWhereClause.equals("")) {
				query.append(" AND ");
			} else {
				query.append(" AND ");
			}
		}

		query.append(this.sTokenFormat(whereClause));
		String orderBy = " ORDER BY info." + sortingColumn;
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info("dynamicQuery " + dynamicQuery);
		log.info("PersonalDAO:buildQuery Leaving Method");
		return dynamicQuery;
	}

	public RPFCForm9Bean rpfcForm9Report(EmployeePersonalInfo personalInfo,
			String flag, String empNameCheak, String sortingColumn) {
		Connection con = null;
		String empNameCheck = "";
		RPFCForm9Bean personal = null;
		ArrayList reportPersList = new ArrayList();

		int totalRecords = 0;
		if (personalInfo.getPensionNo() != null) {
			personalInfo.setPensionNo(commonUtil.trailingZeros(personalInfo
					.getPensionNo().toCharArray()));
		}
		if (personalInfo.getChkEmpNameFlag().equals("true")) {
			empNameCheck = " and info.employeename is null";
		}
		log.info("empNameCheck" + empNameCheck);
		String selectSQL = this.personalBuildQuery(personalInfo, flag,
				empNameCheck, sortingColumn);
		log.info("Query =====" + selectSQL);
		String pensionID = "";

		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(selectSQL);
			if (rs.next()) {
				personal = new RPFCForm9Bean();
				pensionID = rs.getString("PENSIONNO");
				personal = this.loadRPFCForm9Info(rs);
				String findDates = getPensionDatesByEmployee(pensionID,
						personal.getCpfaccno(), personal.getRegion());
				String[] pensionDts = findDates.split(",");
				personal.setFromEmpYear(pensionDts[0]);
				personal.setToEmpYear(pensionDts[1]);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("PersonalDAO :rpfcForm9Report() leaving method");
		return personal;
	}

	public String getPensionDatesByEmployee(String pensionNo, String cpfaccno,
			String region) {
		String cpfacno = this.getCPFACCNOByPension(pensionNo, cpfaccno, region);
		String[] cpfaccnoList = cpfacno.split("=");
		String cpfString = preparedCPFString(cpfaccnoList);
		String sql = "SELECT MIN(MONTHYEAR) AS MINMONTHYEAR,MAX(MONTHYEAR) AS MAXMONTHYEAR FROM EMPLOYEE_PENSION_VALIDATE WHERE  empflag='Y' and "
				+ cpfString;
		Connection con = null;
		StringBuffer buffer = new StringBuffer();
		log.info("Query =====" + sql);
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				buffer.append(commonUtil.getDatetoString(rs
						.getDate("MINMONTHYEAR"), "dd-MMM-yyyy"));
				buffer.append(",");
				buffer.append(commonUtil.getDatetoString(rs
						.getDate("MAXMONTHYEAR"), "dd-MMM-yyyy"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public String preparedCPFString(String[] cpfaccnoList) {
		String cpfstring = "", cpfacno = "", regions = "";
		String getCpfalist = "";
		for (int i = 0; i < cpfaccnoList.length; i++) {
			getCpfalist = cpfaccnoList[i];
			String[] loadCPFRegionlist = getCpfalist.split(",");
			cpfacno = loadCPFRegionlist[0];
			regions = loadCPFRegionlist[1];
			if (cpfstring.equals("")) {
				cpfstring = " (CPFACCNO='" + cpfacno + "' AND REGION='"
						+ regions + "') ";
			} else {
				cpfstring = cpfstring + " OR " + " (CPFACCNO='" + cpfacno
						+ "' AND REGION='" + regions + "') ";
			}
		}
		return cpfstring;
	}

	private String getCPFACCNOByPension(String pensionNo, String cpfacno,
			String region) {
		Connection con = null;
		StringBuffer buffer = new StringBuffer();
		String cpfaccno = "";
		String selectFamilySQL = "SELECT CPFACNO,REGION FROM EMPLOYEE_INFO WHERE SRNO='"
				+ pensionNo.trim() + "' ";
		log.info("Query =====" + selectFamilySQL);
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(selectFamilySQL);
			while (rs.next()) {
				buffer.append(rs.getString("CPFACNO"));
				buffer.append(",");
				buffer.append(rs.getString("REGION"));
				buffer.append("=");
			}
			buffer.append(cpfacno);
			buffer.append(",");
			buffer.append(region);
			buffer.append("=");
			cpfaccno = buffer.toString();
			if (cpfaccno.equals("")) {
				cpfaccno = "---";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cpfaccno;
	}

	private RPFCForm9Bean loadRPFCForm9Info(ResultSet rs) throws SQLException {
		RPFCForm9Bean personal = new RPFCForm9Bean();
		log.info("loadPersonalInfo==============");

		if (rs.getString("cpfacno") != null) {
			personal.setCpfaccno(rs.getString("cpfacno"));
		} else {
			personal.setCpfaccno("---");
		}
		log
				.info("loadPersonalInfo=============="
						+ rs.getString("airportcode"));
		if (rs.getString("airportcode") != null) {
			personal.setEmpAirportCode(rs.getString("airportcode"));
		} else {
			personal.setEmpAirportCode("---");
		}
		if (rs.getString("desegnation") != null) {
			personal.setEmpDesig(rs.getString("desegnation"));
		} else {
			personal.setEmpDesig("---");
		}

		if (rs.getString("PENSIONNO") != null) {
			personal.setEmpPensionNo(commonUtil.leadingZeros(5, rs
					.getString("PENSIONNO")));

		} else {
			personal.setEmpPensionNo("---");
		}

		if (rs.getString("employeename") != null) {
			personal.setEmployeeName(rs.getString("employeename"));
		} else {
			personal.setEmployeeName("---");
		}
		if (rs.getString("EMPLOYEENO") != null) {
			personal.setEmployeeNo(rs.getString("EMPLOYEENO"));
		} else {
			personal.setEmployeeNo("---");
		}
		if (rs.getString("DATEOFJOINING") != null) {
			personal.setEmpDOJ(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			personal.setEmpDOJ("---");
		}

		if (rs.getString("region") != null) {
			personal.setRegion(rs.getString("region"));
		} else {
			personal.setRegion("---");
		}
		if (rs.getString("dateofbirth") != null) {
			personal.setDateOfBirth(commonUtil.converDBToAppFormat(rs
					.getDate("dateofbirth")));
		} else {
			personal.setDateOfBirth("---");
		}

		if (rs.getString("FHNAME") != null) {
			personal.setFhName(rs.getString("FHNAME"));
		} else {
			personal.setFhName("---");
		}

		if (rs.getString("DATEOFSEPERATION_DATE") != null) {
			personal.setEmpDOS(rs.getString("DATEOFSEPERATION_DATE"));
		} else {
			personal.setEmpDOS("---");
		}

		if (!personal.getDateOfBirth().equals("---")) {
			String personalPFID = commonDAO.getPFID(personal.getEmployeeName(),
					personal.getDateOfBirth(), personal.getEmpPensionNo());
			personal.setEmpPFID(personalPFID);
		} else {
			personal.setEmpPFID(personal.getEmpPensionNo());
		}

		return personal;

	}

	public String autoPendingProcessingPersonalInfo(String selectedDate,
			String retiredDate, String userName, String ipName)
			throws IOException {
		ArrayList airportList = new ArrayList();
		String lastactive = "", region = "";
		ArrayList form3List = new ArrayList();

		int totalFailed = 0, totalInserted = 0, totalRecords = 0, form3Cnt = 0;

		BufferedWriter fw = new BufferedWriter(new FileWriter(
				"c://missingImportData.txt"));
		BufferedWriter fw1 = new BufferedWriter(new FileWriter(
				"c://pending_pfids_report.txt"));
		lastactive = commonUtil.getCurrentDate("dd-MMM-yyyy");

		form3List = financeDAO.financeForm3ReportPFIDSAll(selectedDate, region,
				retiredDate, "cpfaccno", "", "false");
		form3Cnt = form3Cnt + form3List.size();
		String format = "";
		for (int j = 0; j < form3List.size(); j++) {
			form3Bean form3 = new form3Bean();
			form3 = (form3Bean) form3List.get(j);
			try {
				format = form3.getCpfaccno() + "," + form3.getEmployeeName()
						+ "," + form3.getAirportCode() + ","
						+ form3.getRegion();
				try {
					fw1.write(format);
					fw1.newLine();
					fw1.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					log.printStackTrace(e1);
				}

				totalRecords = this.processingPendingPersonalInfo(form3,
						lastactive, form3.getRegion(), fw, userName, ipName,
						selectedDate);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			if (totalRecords == 0) {
				totalFailed = totalFailed + 1;
			} else {
				totalInserted = totalInserted + 1;
			}
		}

		String message = "Total Form 3 Records=====" + form3Cnt + "<br/>";
		message = "Total Inserted=====" + totalInserted + "<br/>";
		message = message + "Total Failed=====" + totalFailed + "<br/>";
		return message;
	}

	private int processingPendingPersonalInfo(form3Bean form3Bean,
			String lastactive, String region, BufferedWriter fw,
			String userName, String IPAddress, String selectedDate)
			throws SQLException {
		log.info("processinPersonalInfo========");
		String sqlSelQuery = "", totalRecords = "", pensionNumber = "", insertQry = "", updateQry = "", updateNomineeQry = "", updateFamilDetails = "";
		Connection con = null;
		Statement st = null;
		Statement perSt = null;
		Statement nomineeSt = null;
		Statement familySt = null;
		int i = 0, j = 0, k = 0;
		selectedDate = selectedDate.replaceAll("%", "01");
		try {
			con = DBUtils.getConnection();
			pensionNumber = this.getSequenceNo(con);
			if (!pensionNumber.equals("")) {
				sqlSelQuery = "select PENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,"
						+ "WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,SEX,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,"
						+ Long.parseLong(pensionNumber)
						+ ",'"
						+ userName
						+ "','"
						+ lastactive
						+ "','"
						+ selectedDate
						+ "','"
						+ IPAddress
						+ "' "
						+ " FROM employee_info WHERE  ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ form3Bean.getCpfaccno().trim()
						+ "' AND REGION='"
						+ region.trim() + "')";
				log.info(sqlSelQuery);
				String chkPFID = checkPFID(form3Bean.getCpfaccno().trim(),
						region.trim());
				if (chkPFID.equals("")) {
					insertQry = "insert into employee_personal_info(REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,"
							+ "GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,username,LASTACTIVE,RefMonthYear,IPAddress)  "
							+ sqlSelQuery;
				} else {
					pensionNumber = chkPFID;
				}

				updateQry = "UPDATE EMPLOYEE_INFO SET EMPSERIALNUMBER='"
						+ pensionNumber
						+ "' WHERE ROWID=(SELECT MIN(ROWID) FROM EMPLOYEE_INFO WHERE EMPFLAG='Y' AND CPFACNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "')";
				updateNomineeQry = "UPDATE EMPLOYEE_NOMINEE_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "'";
				updateFamilDetails = "UPDATE EMPLOYEE_FAMILY_DTLS SET SRNO='"
						+ pensionNumber + "' WHERE EMPFLAG='Y' AND CPFACCNO='"
						+ form3Bean.getCpfaccno().trim() + "' AND REGION='"
						+ region.trim() + "'";

				// con = DBUtils.getConnection();
				st = con.createStatement();
				perSt = con.createStatement();
				nomineeSt = con.createStatement();
				familySt = con.createStatement();
				con.setAutoCommit(false);
				log.info(insertQry);
				if (chkPFID.equals("")) {
					i = st.executeUpdate(insertQry);
				} else {
					i = 1;
				}

				if (i == 0) {
					writeFailedQueryErrorLog(insertQry, fw);
					writeFailedQueryErrorLog(updateQry, fw);
				} else {
					i = perSt.executeUpdate(updateQry);
					j = nomineeSt.executeUpdate(updateNomineeQry);
					k = familySt.executeUpdate(updateFamilDetails);
				}
				con.commit();
				con.setAutoCommit(true);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
			if (con != null) {
				con.rollback();
			}
			if (!(insertQry.equals("") || updateQry.equals(""))) {
				writeFailedQueryErrorLog(insertQry, fw);
				writeFailedQueryErrorLog(updateQry, fw);
				writeFailedQueryErrorLog(updateNomineeQry, fw);
				writeFailedQueryErrorLog(updateFamilDetails, fw);
			}

		} catch (Exception e) {
			log.printStackTrace(e);
			if (con != null) {
				con.rollback();
			}
			if (!(insertQry.equals("") || updateQry.equals(""))) {
				writeFailedQueryErrorLog(insertQry, fw);
				writeFailedQueryErrorLog(updateQry, fw);
				writeFailedQueryErrorLog(updateNomineeQry, fw);
				writeFailedQueryErrorLog(updateFamilDetails, fw);
			}

		} finally {
			if (perSt != null) {
				try {
					perSt.close();
					perSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (nomineeSt != null) {
				try {
					nomineeSt.close();
					nomineeSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			if (familySt != null) {
				try {
					familySt.close();
					familySt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}
			commonDB.closeConnection(con, st, null);
		}

		return i;
	}

	private String checkPFID(String cpfaccno, String region) {
		boolean chkPFIDFlag = false;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int count = 0;
		String pensionNo = "", selQuery = "";
		selQuery = "SELECT PENSIONNO AS PENSIONNO FROM EMPLOYEE_PERSONAL_INFO WHERE REGION='"
				+ region + "' AND CPFACNO='" + cpfaccno + "'";
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selQuery);
			if (rs.next()) {
				if (rs.getString("PENSIONNO") != null) {
					pensionNo = rs.getString("PENSIONNO");
				}

			}

		} catch (SQLException e) {
			log.printStackTrace(e);

		} catch (Exception e) {

			log.printStackTrace(e);

		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return pensionNo;
	}

	public String autoUpdateProcessingPersonalInfo(String selectedDate,
			String retiredDate, HashMap regionMap, String selectedairportCode,
			String userName, String ipName) throws IOException {
		ArrayList airportList = new ArrayList();
		String lastactive = "", region = "", airportCode = "", message = "";
		ArrayList form3List = new ArrayList();
		Set set = regionMap.entrySet();
		Iterator iter = set.iterator();
		ArrayList regionList = new ArrayList();
		RegionBean regionInfo = new RegionBean();
		int totalFailed = 0, totalInserted = 0, totalRecords = 0, form3Cnt = 0;
		Connection con = null;
		try {
			con = DBUtils.getConnection();
			BufferedWriter fw = new BufferedWriter(new FileWriter(
					"c://missingImportData.txt"));
			BufferedWriter fw1 = new BufferedWriter(new FileWriter(
					"c://chqiadData.txt"));
			lastactive = commonUtil.getCurrentDate("dd-MMM-yyyy");
			regionList = commonUtil.loadRegions();

			boolean chkMnthYearFlag = false;

			for (int rg = 0; rg < regionList.size(); rg++) {
				regionInfo = (RegionBean) regionList.get(rg);

				if (selectedairportCode.equals("NO-SELECT")) {
					if (regionInfo.getAaiCategory().equals("METRO AIRPORT")) {
						region = regionInfo.getRegion();
						airportList = null;
						airportList = new ArrayList();
						airportList.add(regionInfo.getAirportcode());
					} else {
						if (regionInfo.getAaiCategory().equals(
								"NON-METRO AIRPORT")) {
							region = regionInfo.getRegion();
							airportList = financeDAO.getPensionAirportList(
									region, "");
						}

					}

				} else {
					airportList.add(selectedairportCode);
				}

				if (chkMnthYearFlag = true) {
					for (int k = 0; k < airportList.size(); k++) {
						airportCode = (String) airportList.get(k);
						form3List = financeDAO.financeForm3Report(airportCode,
								selectedDate, region, retiredDate,
								"employeename", "", "false");
						form3Cnt = form3Cnt + form3List.size();
						for (int j = 0; j < form3List.size(); j++) {
							String ms = "";
							form3Bean form3 = new form3Bean();
							form3 = (form3Bean) form3List.get(j);

							try {
								totalRecords = this.updatePersonalInfo(con,
										form3, lastactive, region, fw,
										userName, ipName, selectedDate);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								log.printStackTrace(e);
							}
							if (totalRecords == 0) {
								totalFailed = totalFailed + 1;
							} else {
								totalInserted = totalInserted + 1;
							}
						}

					}
					form3List = null;
					form3List = new ArrayList();
					if (region.equals("CHAIAD")
							&& airportCode.equals("DPO IAD")) {
						chkMnthYearFlag = true;
					}

				}
				message = "Total Form 3 Records=====" + form3Cnt + "<br/>";
				message = "Total Inserted=====" + totalInserted + "<br/>";
				message = message + "Total Failed=====" + totalFailed + "<br/>";
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return message;
	}

	private int updatePersonalInfo(Connection con, form3Bean form3Bean,
			String lastactive, String region, BufferedWriter fw,
			String userName, String IPAddress, String selectedDate)
			throws SQLException {
		log.info("processinPersonalInfo========");
		String sqlSelQuery = "", totalRecords = "", pensionNumber = "", insertQry = "", updateQry = "", updateNomineeQry = "", updateFamilDetails = "";

		Statement st = null;
		Statement perSt = null;
		Statement nomineeSt = null;
		Statement familySt = null;
		int i = 0, totalInserted = 0, totalFail = 0, j = 0, k = 0;
		selectedDate = selectedDate.replaceAll("%", "01");
		try {
			perSt = con.createStatement();
			updateQry = "UPDATE EMPLOYEE_PERSONAL_INFO SET AIRPORTCODE='"
					+ form3Bean.getAirportCode() + "' WHERE CPFACNO='"
					+ form3Bean.getCpfaccno() + "' AND REGION='"
					+ region.trim() + "'";
			i = perSt.executeUpdate(updateQry);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			if (perSt != null) {
				try {
					perSt.close();
					perSt = null;
				} catch (SQLException se) {
					log.printStackTrace(se);
				}
			}

		}

		return i;
	}

	public void updatePFIDTransTbl(String range, String region,
			String empNameFlag, String empName, String pensionno, FileWriter fw) {
		log.info("PersonalDAO::updatePFIDTransTbl");
		Connection con = null;

		ArrayList empDataList = new ArrayList();
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		int totalRecords = 0, totalAdvRecords = 0, totalLoansRecords = 0;
		try {
			con = commonDB.getConnection();
			empDataList = this.getEmployeePFInfo(con, range, region,
					empNameFlag, empName, pensionno);

			for (int i = 0; i < empDataList.size(); i++) {

				personalInfo = (EmployeePersonalInfo) empDataList.get(i);
				if (!personalInfo.getPfIDString().equals("")) {
					totalRecords = this.updateTranTbl(con, personalInfo
							.getPfIDString(), personalInfo.getOldPensionNo());
					totalAdvRecords = this.updateTranAdvanceTbl(con,
							personalInfo.getPfIDString(), personalInfo
									.getOldPensionNo());
					totalLoansRecords = this.updateTranLoansTbl(con,
							personalInfo.getPfIDString(), personalInfo
									.getOldPensionNo());
					fw.write(commonUtil.leadingZeros(5, personalInfo
							.getPensionNo())
							+ "================"
							+ totalRecords
							+ "========================"
							+ personalInfo.getPfIDString() + "\n");
					fw.write(commonUtil.leadingZeros(5, personalInfo
							.getPensionNo())
							+ "================"
							+ totalAdvRecords
							+ "==========A=============="
							+ personalInfo.getPfIDString() + "\n");
					fw.write(commonUtil.leadingZeros(5, personalInfo
							.getPensionNo())
							+ "================"
							+ totalLoansRecords
							+ "========L================"
							+ personalInfo.getPfIDString() + "\n");
				} else {
					fw.write(commonUtil.leadingZeros(5, personalInfo
							.getPensionNo())
							+ "================"
							+ totalRecords
							+ "========================" + "Un mapped PFID\n");
				}

				fw.flush();
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, null, null);
		}

		return;
	}

	private ArrayList getEmployeePFInfo(Connection con, String range,
			String region, String empNameFlag, String empName, String pensionno) {
		CommonDAO commonDAO = new CommonDAO();

		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfidWithRegion = "", pensionAppednQry = "";
		EmployeePersonalInfo data = null;
		ArrayList empinfo = new ArrayList();
		try {
			st = con.createStatement();
			if (region.equals("NO-SELECT")) {
				region = "";
			}
			sqlQuery = this.buildQueryEmpPFIDInfo(range, region, empNameFlag,
					empName, pensionno);
			log.info("PersonalDAO::getEmployeePFInfo" + sqlQuery);
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				data = new EmployeePersonalInfo();
				data = commonDAO.loadPersonalInfo(rs);
				pfidWithRegion = this.getEmpMapPFInfo(con, data.getPensionNo(),
						data.getCpfAccno(), data.getRegion());
				if (!pfidWithRegion.equals("")) {
					String[] pfIDLists = pfidWithRegion.split("=");
					log.info(data.getOldPensionNo() + "============"
							+ pfidWithRegion);
					if (pfIDLists.length != 0) {
						data.setPfIDString(this.preparedCPFString(pfIDLists));
					} else {
						data.setPfIDString("");
					}
				} else {
					data.setPfIDString("");
				}

				empinfo.add(data);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return empinfo;
	}

	private String getEmpMapPFInfo(Connection con, String personalPFID,
			String personalCPFACCNO, String personalRegion) {

		Statement st = null;
		ResultSet rs = null;
		boolean flag = false;
		String sqlQuery = "", pfID = "", region = "", regionPFIDS = "";
		if (("---".equals(personalCPFACCNO))
				|| ("---".equals(personalRegion))
				|| ("---".equals(personalRegion) && "---"
						.equals(personalCPFACCNO))) {
			flag = true;
		}
		try {

			st = con.createStatement();
			sqlQuery = "SELECT CPFACNO,REGION FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER='"
					+ personalPFID + "'";
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				if (rs.getString("CPFACNO") != null) {
					pfID = rs.getString("CPFACNO");

				}
				if (rs.getString("REGION") != null) {
					region = rs.getString("REGION");

				}
				if (regionPFIDS.equals("")) {
					regionPFIDS = pfID + "," + region + "=";
				} else {
					regionPFIDS = regionPFIDS + pfID + "," + region + "=";
				}

			}
			if (!regionPFIDS.equals("")) {
				if (flag = true) {
					regionPFIDS = regionPFIDS + "=";
				} else {
					regionPFIDS = regionPFIDS + personalCPFACCNO + ","
							+ personalRegion + "=";
				}
			} else {
				if (flag = true) {
					regionPFIDS = "";
				} else {
					regionPFIDS = personalCPFACCNO + "," + personalRegion + "=";
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return regionPFIDS;
	}

	private String buildQueryEmpPFIDInfo(String range, String region,
			String empNameFlag, String empName, String pensionno) {

		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, INITCAP(EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,username,LASTACTIVE,RefMonthYear,IPAddress,OTHERREASON,decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,ROUND(months_between('01-Apr-1995', dateofbirth) / 12) EMPAGE FROM EMPLOYEE_PERSONAL_INFO";
		if (!range.equals("NO-SELECT")) {
			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);
			whereClause.append("  PENSIONNO >=" + startIndex
					+ " AND PENSIONNO <=" + endIndex);
			whereClause.append(" AND ");
		}
		if (!region.equals("")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}

		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
				whereClause.append("PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}

		query.append(sqlQuery);
		if ((region.equals(""))
				&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

		} else {
			query.append(" WHERE ");
			query.append(this.sTokenFormat(whereClause));
		}
		String orderBy = "  ORDER BY PENSIONNO";
		query.append(orderBy);
		dynamicQuery = query.toString();

		return dynamicQuery;
	}

	private int updateTranTbl(Connection con, String pfidString,
			String pensionno) {
		String seltransTbl = "UPDATE EMPLOYEE_PENSION_VALIDATE SET PENSIONNO="
				+ pensionno + " WHERE (" + pfidString + ")";

		Statement st = null;
		ResultSet rs = null;
		int updatedStatus = 0;
		try {

			st = con.createStatement();
			log.info("PersonalDA::updateTranTbl" + seltransTbl);
			updatedStatus = st.executeUpdate(seltransTbl);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return updatedStatus;
	}

	private int updateTranLoansTbl(Connection con, String pfidString,
			String pensionno) {
		String seltransTbl = "UPDATE EMPLOYEE_PENSION_LOANS SET PENSIONNO="
				+ pensionno + " WHERE (" + pfidString + ")";

		Statement st = null;
		ResultSet rs = null;
		int updatedStatus = 0;
		try {

			st = con.createStatement();
			log.info("PersonalDA::updateTranTbl" + seltransTbl);
			updatedStatus = st.executeUpdate(seltransTbl);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return updatedStatus;
	}

	private int updateTranAdvanceTbl(Connection con, String pfidString,
			String pensionno) {
		String seltransTbl = "UPDATE EMPLOYEE_PENSION_ADVANCES SET PENSIONNO="
				+ pensionno + " WHERE (" + pfidString + ")";

		Statement st = null;
		ResultSet rs = null;
		int updatedStatus = 0;
		try {

			st = con.createStatement();
			log.info("PersonalDAO::updateTranAdvanceTbl" + seltransTbl);
			updatedStatus = st.executeUpdate(seltransTbl);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return updatedStatus;
	}

	public int updateForm10D(EmpMasterBean bean,
			EmployeeAdlPensionInfo empAdlPensionInfo)
			throws InvalidDataException {
		log.info("PersonalDAO:updateForm10D entering method ");
		// PensionBean editBean =new PensionBean();
		Connection con = null;
		Statement st = null;

		int count = 0;
		String airportSerialNumber = "", empNumber = "", cpfAcNo = "", newCpfAcno = "";
		String empName = "", desegnation = "", empLevel = "", seperationReason = "", whetherOptionA = "";
		String whetherOptionB = "", whetherOptionNO = "", form2Nomination = "";
		String remarks = "", station = "", dateofBirth = "", dateofJoining = "", dateofSeperationDate = "";
		String fMemberName = "", fDateofBirth = "", frelation = "", familyrows = "";
		String wetherOption = "", sex = "", maritalStatus = "", fhName = "", permanentAddress = "", temporatyAddress = "", dateOfAnnuation = "", otherReason = "";
		String pfId = "";
		int count1[] = new int[3];
		pfId = bean.getPfid();
		airportSerialNumber = bean.getAirportSerialNumber();
		empNumber = bean.getEmpNumber();
		cpfAcNo = bean.getCpfAcNo().trim();
		newCpfAcno = bean.getNewCpfAcNo();
		station = bean.getStation();
		empName = bean.getEmpName();
		desegnation = bean.getDesegnation();
		empLevel = bean.getEmpLevel();
		seperationReason = bean.getSeperationReason();
		whetherOptionA = bean.getWhetherOptionA();
		whetherOptionB = bean.getWhetherOptionB();
		whetherOptionNO = bean.getWhetherOptionNO();
		remarks = bean.getRemarks();
		dateofBirth = bean.getDateofBirth();
		dateofJoining = bean.getDateofJoining();
		dateofSeperationDate = bean.getDateofSeperationDate();
		form2Nomination = bean.getForm2Nomination();
		String pensionNumber = bean.getPensionNumber();
		String empNomineeSharable = bean.getEmpNomineeSharable();
		wetherOption = bean.getWetherOption();
		sex = bean.getSex();
		maritalStatus = bean.getMaritalStatus();
		fhName = bean.getFhName();
		permanentAddress = bean.getPermanentAddress();
		temporatyAddress = bean.getTemporatyAddress();
		dateOfAnnuation = bean.getDateOfAnnuation();
		// String pensionNumber = this.getPensionNumber(empName.toUpperCase(),
		// dateofBirth, cpfAcNo);
		otherReason = bean.getOtherReason().trim();
		String division = bean.getDivision();
		String department = bean.getDepartment();
		String emailId = bean.getEmailId();
		String empOldName = bean.getEmpOldName();
		String empOldNumber = bean.getEmpOldNumber();
		String region = bean.getRegion();
		String setRecordVerified = bean.getRecordVerified();

		java.util.Date now = new java.util.Date();
		String MY_DATE_FORMAT = "dd-MM-yyyy hh:mm a";
		String currDateTime = new SimpleDateFormat(MY_DATE_FORMAT).format(now);
		log.info("date is  " + currDateTime);
		String fhFlag = bean.getFhFlag();
		if (!bean.getChangedRegion().equals("")) {
			region = bean.getChangedRegion();
		}
		if (!bean.getChangedStation().equals("")) {
			station = bean.getChangedStation();
		}

		String changedStation = bean.getChangedStation();

		try {
			String query = "";

			con = commonDB.getConnection();
			st = con.createStatement();

			pensionDAO.insertEmployeeHistory(pfId, cpfAcNo, "", true, "",
					region, currDateTime, bean.getComputerName().trim(), bean
							.getUserName());

			log.info("updateForm10D::pensionNumber  " + pensionNumber
					+ "cpfAcNo  " + cpfAcNo + "bean.getEmpOldNumber()"
					+ bean.getEmpOldNumber() + "region===========" + region
					+ "dateofBirth" + dateofBirth);

			String nomineeDmlQuery = "";
			String nomineeName = "", tempData = "", nomineeAddress = "", nrowid = "", nomineeDob = "", nomineeRelation = "", nameofGuardian = "", totalShare = "", gaurdianAddress = "", nomineeRows = "";
			String nomineeAccounts = "", nomineeRetCaptOtpion = "";
			String nomineeOldName = "";
			String tempInfo[] = null;
			nomineeRows = bean.getNomineeRow();
			log.info("updateForm10D::nomineeRows " + nomineeRows);
			ArrayList nomineeList = commonUtil.getTheList(nomineeRows, "***");
			DateValidation dateValidation = new DateValidation();
			int serialno = 0;
			for (int j = 0; j < nomineeList.size(); j++) {
				tempData = nomineeList.get(j).toString();
				tempInfo = tempData.split("@");
				nomineeName = tempInfo[0];
				log.info("updateForm10D::tempInfo(updateForm10D)" + tempInfo);
				if (!tempInfo[1].equals("XXX")) {
					nomineeAddress = tempInfo[1];
				} else {
					nomineeAddress = "";
				}
				if (!tempInfo[2].equals("XXX")) {
					nomineeDob = tempInfo[2].toString().trim();
				} else {
					nomineeDob = "";
				}

				if (!tempInfo[3].equals("XXX")) {
					nomineeRelation = tempInfo[3];
				} else {
					nomineeRelation = "";
				}
				if (!tempInfo[4].equals("XXX")) {
					nameofGuardian = tempInfo[4];
				} else {
					nameofGuardian = "";
				}
				if (!tempInfo[5].equals("XXX")) {
					gaurdianAddress = tempInfo[5];
				} else {
					gaurdianAddress = "";
				}
				if (!tempInfo[6].equals("XXX")) {
					nomineeOldName = tempInfo[6];
				} else {
					nomineeOldName = "";
				}

				if (!tempInfo[7].equals("XXX")) {
					nomineeRetCaptOtpion = tempInfo[7];
				} else {
					nomineeRetCaptOtpion = "N";
				}

				if (!tempInfo[8].equals("XXX")) {
					nomineeAccounts = tempInfo[8];
				} else {
					nomineeAccounts = "";
				}
				if (!tempInfo[9].equals("XXX")) {
					nrowid = tempInfo[9];
				} else {
					nrowid = "";
				}
				log.info("updateForm10D::rowid " + nrowid + "nomineeDob"
						+ nomineeDob);

				// nomineeRows=tempInfo[5];
				if (!this.checkFamilyNomineeDtls(nomineeOldName,
						bean.getEmpSerialNo(), "", nrowid).equals("NOT FOUND")) {
					nomineeDmlQuery = "update employee_nominee_dtls set nomineeName='"
							+ nomineeName
							+ "',nomineeAddress='"
							+ nomineeAddress
							+ "',nomineeDob='"
							+ nomineeDob
							+ "',nomineeRelation='"
							+ nomineeRelation
							+ "',nameofGuardian='"
							+ nameofGuardian
							+ "',nomineeretcap='"
							+ nomineeRetCaptOtpion
							+ "',GAURDIANADDRESS='"
							+ gaurdianAddress
							+ "',savingacc='"
							+ nomineeAccounts
							+ "' where nomineeName='"
							+ nomineeOldName.trim()
							+ "' and PENSIONNO='"
							+ bean.getEmpSerialNo()
							+ "' and SRNO='" + nrowid + "'";

				} else {
					serialno++;
					nomineeDmlQuery = "insert into employee_nominee_dtls(cpfaccno,nomineeName,nomineeAddress,nomineeDob,nomineeRelation,nameofGuardian,GAURDIANADDRESS,region,SRNO,nomineeretcap,savingacc,PENSIONNO)values('"
							+ newCpfAcno
							+ "','"
							+ nomineeName
							+ "','"
							+ nomineeAddress
							+ "','"
							+ nomineeDob
							+ "','"
							+ nomineeRelation
							+ "','"
							+ nameofGuardian
							+ "','"
							+ gaurdianAddress
							+ "','"
							+ region
							+ "','"
							+ serialno
							+ "','"
							+ nomineeRetCaptOtpion
							+ "','"
							+ nomineeAccounts
							+ "','"
							+ bean.getEmpSerialNo()
							+ "')";
				}
				log.info("Form10DUpdate::nomineeDmlQuery is" + nomineeDmlQuery);
				st.executeUpdate(nomineeDmlQuery);
			}
			int otherPencnt = this.addOtherPensionDtls(bean.getEmpSerialNo(),
					con, empAdlPensionInfo);
			int schemeCnt = this.addSchmemeDtls(bean.getEmpSerialNo(), con,
					bean);

			String personalUpdQry = "update  EMPLOYEE_PERSONAL_INFO set permanentAddress='"
					+ permanentAddress
					+ "',temporatyAddress='"
					+ temporatyAddress
					+ "',NATIONALITY='"
					+ bean.getNationality()
					+ "',PHONENUMBER='"
					+ bean.getPhoneNumber()
					+ "',HEIGHT='"
					+ bean.getHeightWithInches()
					+ "'  where empflag='Y' and PENSIONNO="
					+ bean.getEmpSerialNo();
			st.executeUpdate(personalUpdQry);
			log.info("updateForm10D::empPersonalInfoQuery" + personalUpdQry);

		} catch (SQLException sqle) {
			log.printStackTrace(sqle);
			if (sqle.getErrorCode() == 00001) {
				throw new InvalidDataException("PensionNumber Already Exist");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, null);
		}
		log.info("PersonalDAO:updateForm10D leaving method ");
		return count;
	}

	public String checkFamilyNomineeDtls(String memberName, String pensionNo,
			String flag, String serialNo) {

		log.info("PersonalDAO :checkFamilyNomineeDtls() entering method ");
		String foundEmpFlag = "", query = "";
		Statement st = null;
		Connection con = null;
		ResultSet rs = null;
		if (flag.trim().equals("family")) {
			query = "SELECT  'X' as COLUMNNM FROM EMPLOYEE_FAMILY_DTLS where  PENSIONNO="
					+ pensionNo
					+ "  AND lower(NOMINEENAME)='"
					+ memberName.toLowerCase() + "'";
		} else {
			query = "select 'X' as COLUMNNM from EMPLOYEE_NOMINEE_DTLS where  PENSIONNO="
					+ pensionNo
					+ "  AND lower(NOMINEENAME)='"
					+ memberName.toLowerCase() + "'";
		}

		log.info("query is " + query);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				if (rs.getString("COLUMNNM") != null) {
					foundEmpFlag = rs.getString("COLUMNNM");
				}
			} else {
				foundEmpFlag = "NOT FOUND";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("PersonalDAO :checkFamilyNomineeDtls() leaving method");
		return foundEmpFlag;
	}

	public EmpMasterBean empForm10DPersonalEdit(String cpfacno, String empName,
			String region, String pfid) throws Exception {
		log.info("PensionDAO:empForm10DPersonalEdit entering method ");
		EmpMasterBean editBean = new EmpMasterBean();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		String personalSelQuery = "", query1 = "";
		personalSelQuery = " SELECT * FROM EMPLOYEE_PERSONAL_INFO where  empflag='Y' AND PENSIONNO ="
				+ pfid.toLowerCase().trim();
		log.info("PersonalDAO::empForm10DPersonalEdit is " + personalSelQuery);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(personalSelQuery);
			if (rs.next()) {

				if (rs.getString("employeename") != null)
					editBean.setEmpName(rs.getString("employeename").trim());
				else
					editBean.setEmpName("");
				if (rs.getString("EMPLOYEENO") != null)
					editBean.setEmpNumber(rs.getString("EMPLOYEENO").trim());
				else
					editBean.setEmpNumber("");

				if (rs.getString("desegnation") != null)
					editBean.setDesegnation(rs.getString("desegnation").trim());
				else
					editBean.setDesegnation("");

				if (rs.getString("EMP_LEVEL") != null)
					editBean.setEmpLevel(rs.getString("EMP_LEVEL"));
				else
					editBean.setEmpLevel("");

				if (rs.getString("DATEOFBIRTH") != null) {
					editBean.setDateofBirth(commonUtil.converDBToAppFormat(rs
							.getString("DATEOFBIRTH").toString(), "yyyy-MM-dd",
							"dd-MMM-yyyy"));
				} else {
					editBean.setDateofBirth("");
				}
				if (rs.getString("PHONENUMBER") != null)
					editBean.setPhoneNumber(rs.getString("PHONENUMBER"));
				else
					editBean.setPhoneNumber("");

				if (rs.getString("DATEOFJOINING") != null)
					// editBean.setDateofJoining(CommonUtil.getStringtoDate(rs.getString("DATEOFJOINING").toString()));
					editBean.setDateofJoining(commonUtil.converDBToAppFormat(rs
							.getString("DATEOFJOINING").toString(),
							"yyyy-MM-dd", "dd-MMM-yyyy"));
				else
					editBean.setDateofJoining("");

				if (rs.getString("DATEOFSEPERATION_REASON") != null)
					editBean.setSeperationReason(rs
							.getString("DATEOFSEPERATION_REASON"));
				else
					editBean.setSeperationReason("");

				if (rs.getString("DATEOFSEPERATION_DATE") != null)
					// editBean.setDateofSeperationDate(CommonUtil.getStringtoDate(rs.getString("DATEOFSEPERATION_DATE").toString()));
					editBean.setDateofSeperationDate(commonUtil
							.converDBToAppFormat(rs.getString(
									"DATEOFSEPERATION_DATE").toString(),
									"yyyy-MM-dd", "dd-MMM-yyyy"));
				else
					editBean.setDateofSeperationDate("");

				if (rs.getString("WHETHER_FORM1_NOM_RECEIVED") != null)
					editBean.setForm2Nomination(rs
							.getString("WHETHER_FORM1_NOM_RECEIVED"));
				else
					editBean.setForm2Nomination("");

				if (rs.getString("airportcode") != null)
					editBean.setStation(rs.getString("airportcode"));
				else
					editBean.setStation("");
				if (rs.getString("REFPENSIONNUMBER") != null)
					editBean.setPensionNumber(rs.getString("REFPENSIONNUMBER"));
				else
					editBean.setPensionNumber("");
				if (rs.getString("emailId") != null)
					editBean.setEmailId(rs.getString("emailId"));
				else
					editBean.setEmailId("");
				if (rs.getString("AIRPORTSERIALNUMBER") != null)
					editBean.setAirportSerialNumber(rs
							.getString("AIRPORTSERIALNUMBER"));
				else
					editBean.setAirportSerialNumber("");
				if (rs.getString("employeeno") != null)
					editBean.setEmpNumber(rs.getString("employeeno"));
				else
					editBean.setEmpNumber("");
				if (rs.getString("remarks") != null)
					editBean.setRemarks(rs.getString("remarks"));
				else
					editBean.setRemarks("");
				if (rs.getString("GENDER") != null)
					editBean.setSex(rs.getString("GENDER"));
				else
					editBean.setSex("");

				if (rs.getString("setDateOfAnnuation") != null)
					// editBean.setDateOfAnnuation(CommonUtil.getStringtoDate(rs.getString("setDateOfAnnuation")));
					editBean.setDateOfAnnuation(commonUtil.converDBToAppFormat(
							rs.getString("setDateOfAnnuation"), "yyyy-MM-dd",
							"dd-MMM-yyyy"));
				else
					editBean.setDateOfAnnuation("");

				if (rs.getString("fhname") != null)
					editBean.setFhName(rs.getString("fhname"));
				else
					editBean.setFhName("");
				if (rs.getString("fhflag") != null)
					editBean.setFhFlag(rs.getString("fhflag"));
				else
					editBean.setFhFlag("");

				if (rs.getString("maritalstatus") != null)
					editBean.setMaritalStatus(rs.getString("maritalstatus"));
				else
					editBean.setMaritalStatus("");

				if (rs.getString("PERMANENTADDRESS") != null)
					editBean.setPermanentAddress(rs
							.getString("PERMANENTADDRESS"));
				else
					editBean.setPermanentAddress("");

				if (rs.getString("TEMPORATYADDRESS") != null)
					editBean.setTemporatyAddress(rs
							.getString("TEMPORATYADDRESS"));
				else
					editBean.setTemporatyAddress("");

				if (rs.getString("wetherOption") != null) {
					editBean.setWetherOption(rs.getString("wetherOption"));
					log.info("wetheroption is" + editBean.getWetherOption());
				} else
					editBean.setWetherOption("");

				if (rs.getString("region") != null) {
					editBean.setRegion(rs.getString("region"));
					log.info("region is" + editBean.getRegion());
				} else
					editBean.setRegion("");

				if (rs.getString("otherreason") != null) {
					editBean.setOtherReason(rs.getString("otherreason"));
					log.info("otherreason is" + editBean.getOtherReason());
				} else
					editBean.setOtherReason("");

				if (rs.getString("department") != null) {
					editBean.setDepartment(rs.getString("department"));
					log.info("department is" + editBean.getDepartment());
				} else
					editBean.setDepartment("");
				if (rs.getString("division") != null) {
					editBean.setDivision(rs.getString("division"));
					log.info("division is" + editBean.getDivision());
				} else
					editBean.setDivision("");

				if (rs.getString("empnomineesharable") != null) {
					editBean.setEmpNomineeSharable(rs
							.getString("empnomineesharable"));
					log.info("empnomineesharable is"
							+ editBean.getEmpNomineeSharable());
				} else
					editBean.setEmpNomineeSharable("");

				if (rs.getString("region") != null) {
					editBean.setRegion(rs.getString("region"));
					log.info("region  is" + editBean.getRegion());
				} else
					editBean.setRegion("");
				if (rs.getString("OPTIONFORMRECEIVED") != null) {
					editBean.setOptionForm(rs.getString("OPTIONFORMRECEIVED"));
				} else {
					editBean.setOptionForm("");
				}
				// for pfacno
				if (rs.getString("PENSIONNO") != null) {
					editBean.setEmpSerialNo(rs.getString("PENSIONNO"));
					log.info("setEmpSerialNo  is" + editBean.getEmpSerialNo());
				} else
					editBean.setEmpSerialNo("");
				if (rs.getString("NATIONALITY") != null) {
					editBean.setNationality(rs.getString("NATIONALITY"));
				} else {
					editBean.setNationality("");
				}

				if (rs.getString("HEIGHT") != null) {
					editBean.setHeightWithInches(rs.getString("HEIGHT"));
				} else {
					editBean.setHeightWithInches("");
				}

				// for pfacno
				if (rs.getString("PENSIONNO") != null) {
					editBean.setEmpSerialNo(rs.getString("PENSIONNO"));
					editBean.setPfid(commonDAO.getPFID(editBean.getEmpName(),
							editBean.getDateofBirth(), commonUtil.leadingZeros(
									5, editBean.getEmpSerialNo())));
					log.info("empForm10DPersonalEdit::EmpSerialNo  is"
							+ editBean.getEmpSerialNo());
				} else
					editBean.setEmpSerialNo("");
				editBean.setFamilyRow(this.getFamilyDtlsByPFID(editBean
						.getEmpSerialNo(), con));
				editBean.setNomineeRow(this.getNomineeDtlsByPFID(editBean
						.getEmpSerialNo(), con));
				editBean.setSchemeList(this.getSchemeDtls(editBean
						.getEmpSerialNo(), con));
			}

		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("PersionalDAO:empForm10DPersonalEdit leaving method ");
		return editBean;
	}

	public String getNomineeDtlsByPFID(String pensionno, Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String nomineeDtlSelQuery = "", nomineeName = "", nomineeAddress = "", nomineeDob = "", nomineeRetCap = "", nomineeSvingAccno = "";
		String nomineeRelation = "", nameofGuardian = "", guardianAddress = "", totalShare = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		nomineeDtlSelQuery = "SELECT NOMINEENAME,NOMINEEADDRESS,TO_CHAR(NOMINEEDOB,'dd-Mon-yyyy') AS NOMINEEDOB,NOMINEERELATION,NAMEOFGUARDIAN,GAURDIANADDRESS,TOTALSHARE,SRNO,NOMINEERETCAP,SAVINGACC FROM EMPLOYEE_NOMINEE_DTLS WHERE EMPFLAG='Y' AND  PENSIONNO="
				+ pensionno + " ORDER BY SRNO";
		try {
			st = con.createStatement();
			rs = st.executeQuery(nomineeDtlSelQuery);
			while (rs.next()) {
				if (rs.getString("NOMINEENAME") != null) {
					nomineeName = rs.getString("NOMINEENAME");
				} else {
					nomineeName = "xxx";
				}
				if (rs.getString("NOMINEEADDRESS") != null) {
					nomineeAddress = rs.getString("NOMINEEADDRESS");
				} else {
					nomineeAddress = "xxx";
				}
				if (rs.getString("NOMINEEDOB") != null) {

					nomineeDob = rs.getString("NOMINEEDOB");
				} else {
					nomineeDob = "xxx";
				}
				if (rs.getString("NOMINEERELATION") != null) {
					nomineeRelation = rs.getString("NOMINEERELATION");
				} else {
					nomineeRelation = "xxx";
				}
				if (rs.getString("NAMEOFGUARDIAN") != null) {
					nameofGuardian = rs.getString("NAMEOFGUARDIAN");

				} else {
					nameofGuardian = "xxx";
				}
				if (rs.getString("GAURDIANADDRESS") != null) {
					guardianAddress = rs.getString("GAURDIANADDRESS");
					log.info("guardianAddress  " + guardianAddress);
				} else {
					guardianAddress = "xxx";
				}
				if (rs.getString("TOTALSHARE") != null) {
					totalShare = rs.getString("TOTALSHARE");
				} else {
					totalShare = "xxx";
				}

				if (rs.getString("SRNO") != null) {
					rowId = rs.getString("SRNO");
				} else {
					rowId = "xxx";
				}
				if (rs.getString("NOMINEERETCAP") != null) {
					nomineeRetCap = rs.getString("NOMINEERETCAP");
				} else {
					nomineeRetCap = "xxx";
				}
				if (rs.getString("SAVINGACC") != null) {
					nomineeSvingAccno = rs.getString("SAVINGACC");
				} else {
					nomineeSvingAccno = "xxx";
				}
				buffer.append(nomineeName + "@");
				buffer.append(nomineeAddress + "@");
				buffer.append(nomineeDob + "@");
				buffer.append(nomineeRelation + "@");
				buffer.append(nameofGuardian + "@");
				buffer.append(guardianAddress + "@");
				buffer.append(totalShare + "@");
				buffer.append(nomineeRetCap + "@");
				buffer.append(nomineeSvingAccno + "@");
				buffer.append(rowId + "***");

			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return buffer.toString();
	}

	public String getFamilyDtlsByPFID(String pensionno, Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String familyDtlSelQuery = "", familyMemberName = "", fmlyMbrDOB = "", fmlyMbrRelation = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		familyDtlSelQuery = "SELECT FAMILYMEMBERNAME,TO_CHAR(DATEOFBIRTH,'dd-Mon-yyyy') AS DATEOFBIRTH,FAMILYRELATION,SRNO from employee_family_dtls where EMPFLAG='Y' AND  PENSIONNO="
				+ pensionno + "ORDER BY SRNO";
		try {
			st = con.createStatement();
			rs = st.executeQuery(familyDtlSelQuery);
			while (rs.next()) {
				if (rs.getString("FAMILYMEMBERNAME") != null) {
					familyMemberName = rs.getString("FAMILYMEMBERNAME");
				} else {
					familyMemberName = "xxx";
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					fmlyMbrDOB = rs.getString("DATEOFBIRTH");
				} else {
					fmlyMbrDOB = "xxx";
				}
				if (rs.getString("FAMILYRELATION") != null) {
					fmlyMbrRelation = rs.getString("FAMILYRELATION");
				} else {
					fmlyMbrRelation = "xxx";
				}
				if (rs.getString("SRNO") != null) {
					rowId = rs.getString("SRNO");
				} else {
					rowId = "xxx";
				}
				buffer.append(familyMemberName + "@");
				buffer.append(fmlyMbrDOB + "@");
				buffer.append(fmlyMbrRelation + "@");
				buffer.append(rowId + "***");
			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return buffer.toString();
	}

	public ArrayList getSchemeDtls(String pensionno, Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String familyDtlSelQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		ArrayList schmeList = new ArrayList();
		familyDtlSelQuery = "SELECT PENSIONNO,SRNO,CERTIFICONTROLNO,CERTIFIAUTHORITY from EPIS_PENSION_CERTIFICATE_DTLS where EMPFLAG='Y' AND  PENSIONNO="
				+ pensionno + "ORDER BY SRNO";
		try {
			st = con.createStatement();
			rs = st.executeQuery(familyDtlSelQuery);
			while (rs.next()) {
				buffer = new StringBuffer();

				if (rs.getString("CERTIFICONTROLNO") != null) {
					certiControlNO = rs.getString("CERTIFICONTROLNO");
				} else {
					certiControlNO = "xxx";
				}
				if (rs.getString("CERTIFIAUTHORITY") != null) {
					certifAuthority = rs.getString("CERTIFIAUTHORITY");
				} else {
					certifAuthority = "xxx";
				}

				if (rs.getString("SRNO") != null) {
					rowId = rs.getString("SRNO");
				} else {
					rowId = "xxx";
				}
				buffer.append(certiControlNO + "@");
				buffer.append(certifAuthority + "@");
				buffer.append(rowId);
				schmeList.add(buffer.toString());

			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return schmeList;
	}

	public boolean delSchemeDtls(String pensionno, Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String familyDtlDelQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		ArrayList schmeList = new ArrayList();
		int dbCnt = 0;
		boolean schemeDtlCnt = true;
		familyDtlDelQuery = "DELETE from EPIS_PENSION_CERTIFICATE_DTLS where PENSIONNO="
				+ pensionno;
		try {
			st = con.createStatement();
			dbCnt = st.executeUpdate(familyDtlDelQuery);
			if (dbCnt != 0) {
				schemeDtlCnt = true;
			} else {
				schemeDtlCnt = false;
			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return schemeDtlCnt;
	}

	public int addOtherPensionDtls(String pensionno, Connection con,
			EmployeeAdlPensionInfo additonalInfo) {
		Statement st = null;
		String otherPensQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		ArrayList schmeList = new ArrayList();
		int dbCnt = 0;
		boolean schemeDtlCnt = true;
		if (this.checkOtherPensionDtls(pensionno, con).equals("NOTFOUND")) {
			otherPensQuery = "INSERT INTO EPIS_EMPLOYEE_PENSION_DTLS(PENSIONNO,REDPENSION,REDPENSION_DATE,REDDATEOFSUBMSION,QUANTUMOPT,QUANTUMOPTAMOUNT,NOMINEERETCAPTIAL,MEMBERDEATHDATE,PAYMENTFLAG,NAMEOFPAYMENTBRANCH,PAYMENTBRANCHADDR,PINCODE,CLAIMPREBYNOMINEENAME,CLAIMPREBYNOMINEERELATION,MEMBERSCHEMECERT,POSSESSIONMEMBER,SCHEMEADDRESS,SCHEMEPINCODE,DOCUMENTINFO,CURRENTPOSTING,SUBMISSIONDATE,RPFCSTATION,SANCTIONNO,SANCTIONORDERDT,PENSIONDRWNUNDEPS,PPOISSUEDBY) VALUES "
					+ "("
					+ pensionno
					+ ",'"
					+ additonalInfo.getEarlyPensionTaken()
					+ "','"
					+ additonalInfo.getEarlyPensionDate()
					+ "','"
					+ additonalInfo.getEpForm10DSubDate()
					+ "','"
					+ additonalInfo.getQuantum1By3Option()
					+ "','"
					+ additonalInfo.getQuantum1By3Amount()
					+ "','"
					+ additonalInfo.getOptionReturnCaptial()
					+ "','"
					+ additonalInfo.getMemberDeathDate()
					+ "','"
					+ additonalInfo.getPaymentInfoType()
					+ "','"
					+ additonalInfo.getNameofPaymentBranch()
					+ "','"
					+ additonalInfo.getAddressofPayementBranch()
					+ "','"
					+ additonalInfo.getPaymentBranchPincode()
					+ "','"
					+ additonalInfo.getClaimNomineeRefName()
					+ "','"
					+ additonalInfo.getClaimNomineeRefRelation()
					+ "','"
					+ additonalInfo.getSchemeCertificateRecEncl()
					+ "','"
					+ additonalInfo.getPossesionMember()
					+ "','"
					+ additonalInfo.getNomineePostalAddrss()
					+ "','"
					+ additonalInfo.getNomineePincode()
					+ "','"
					+ additonalInfo.getDocumentInfo()
					+ "','"
					+ additonalInfo.getCurrentPostingStation()
					+ "','"
					+ additonalInfo.getForm10DSubmsionDate()
					+ "','"
					+ additonalInfo.getForm10DRpfcStation()
					+ "','"
					+ additonalInfo.getSanctionOrderNo()
					+ "','"
					+ additonalInfo.getSanctionOrderDate()
					+ "','"
					+ additonalInfo.getPensionDrwnFrom1995()
					+ "','"
					+ additonalInfo.getPponoIssuedBy() + "')";
		} else {
			otherPensQuery = "UPDATE	EPIS_EMPLOYEE_PENSION_DTLS SET REDPENSION='"
					+ additonalInfo.getEarlyPensionTaken()
					+ "',REDPENSION_DATE='"
					+ additonalInfo.getEarlyPensionDate()
					+ "',REDDATEOFSUBMSION='"
					+ additonalInfo.getEpForm10DSubDate()
					+ "',QUANTUMOPT='"
					+ additonalInfo.getQuantum1By3Option()
					+ "',QUANTUMOPTAMOUNT='"
					+ additonalInfo.getQuantum1By3Amount()
					+ "',NOMINEERETCAPTIAL='"
					+ additonalInfo.getOptionReturnCaptial()
					+ "',MEMBERDEATHDATE='"
					+ additonalInfo.getMemberDeathDate()
					+ "',PAYMENTFLAG='"
					+ additonalInfo.getPaymentInfoType()
					+ "',NAMEOFPAYMENTBRANCH='"
					+ additonalInfo.getNameofPaymentBranch()
					+ "',PAYMENTBRANCHADDR='"
					+ additonalInfo.getAddressofPayementBranch()
					+ "',PINCODE='"
					+ additonalInfo.getPaymentBranchPincode()
					+ "',CLAIMPREBYNOMINEENAME='"
					+ additonalInfo.getClaimNomineeRefName()
					+ "',DOCUMENTINFO='"
					+ additonalInfo.getDocumentInfo()
					+ "',CLAIMPREBYNOMINEERELATION='"
					+ additonalInfo.getClaimNomineeRefRelation()
					+ "',MEMBERSCHEMECERT='"
					+ additonalInfo.getSchemeCertificateRecEncl()
					+ "',POSSESSIONMEMBER='"
					+ additonalInfo.getPossesionMember()
					+ "',SCHEMEADDRESS='"
					+ additonalInfo.getNomineePostalAddrss()
					+ "',CURRENTPOSTING='"
					+ additonalInfo.getCurrentPostingStation()
					+ "',SUBMISSIONDATE='"
					+ additonalInfo.getForm10DSubmsionDate()
					+ "',RPFCSTATION='"
					+ additonalInfo.getForm10DRpfcStation()
					+ "',SANCTIONNO='"
					+ additonalInfo.getSanctionOrderNo()
					+ "',SANCTIONORDERDT='"
					+ additonalInfo.getSanctionOrderDate()
					+ "',SCHEMEPINCODE='"
					+ additonalInfo.getNomineePincode()
					+ "',PENSIONDRWNUNDEPS='"
					+ additonalInfo.getPensionDrwnFrom1995()
					+ "',PPOISSUEDBY='"
					+ additonalInfo.getPponoIssuedBy()
					+ "',LASTACTIVE=CURRENT_DATE WHERE DELETEFLAG='N' AND PENSIONNO="
					+ pensionno;
		}
		log.info("PersonalDAO::addOtherPensionDtls" + otherPensQuery);
		try {
			st = con.createStatement();
			dbCnt = st.executeUpdate(otherPensQuery);
		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, null);
		}
		return dbCnt;
	}

	public String checkOtherPensionDtls(String pensionno, Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String otherPensionDtlSelQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		ArrayList schmeList = new ArrayList();
		int dbCnt = 0;
		String returnFlag = "";

		otherPensionDtlSelQuery = "SELECT 'X' AS FLAG FROM EPIS_EMPLOYEE_PENSION_DTLS  WHERE PENSIONNO="
				+ pensionno + " AND DELETEFLAG='N'";
		log.info("Pension Details Query" + otherPensionDtlSelQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(otherPensionDtlSelQuery);
			if (rs.next()) {
				returnFlag = rs.getString("FLAG");
			} else {
				returnFlag = "NOTFOUND";
			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return returnFlag;
	}

	public int addSchmemeDtls(String pensionno, Connection con,
			EmpMasterBean bean) {
		Statement schemeSt = null;
		String otherPensQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		StringBuffer buffer = new StringBuffer();
		ArrayList schmeList = new ArrayList();
		int dbCnt = 0;
		ArrayList schemeList = new ArrayList();
		schemeList = bean.getSchemeList();
		String schmeInfo = "", schmeNo = "", schmeControlNo = "", schmeAuthority = "", insertSchQry = "";
		int schmeCnt = 0;
		log.info("Scheme Certificate List size" + schemeList.size());

		try {

			boolean deleSchmeDtls = delSchemeDtls(bean.getEmpSerialNo(), con);
			log.info("Scheme Informaiton" + deleSchmeDtls);
			for (int i = 0; i < schemeList.size(); i++) {
				schemeSt = con.createStatement();
				schmeInfo = (String) schemeList.get(i);
				schmeCnt++;
				String[] schmemData = schmeInfo.split("#");

				if (schmemData.length == 2) {
					schmeControlNo = schmemData[0];
					schmeAuthority = schmemData[1];
					insertSchQry = "INSERT INTO epis_pension_certificate_dtls(pensionno,srno,CERTIFICONTROLNO,CERTIFIAUTHORITY) VALUES("
							+ bean.getEmpSerialNo()
							+ ","
							+ schmeCnt
							+ ",'"
							+ schmeControlNo + "','" + schmeAuthority + "')";
					log.info("INSERT QUERY" + insertSchQry);
					schemeSt.executeUpdate(insertSchQry);
					if (schemeSt != null) {
						schemeSt.close();
					}
				}

			}
		} catch (SQLException se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(null, schemeSt, null);
		}
		return dbCnt;
	}

	public EmployeeAdlPensionInfo getPensionForm10DDtls(String pensionno) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String familyDtlSelQuery = "";
		String certiControlNO = "", certifAuthority = "", rowId = "";
		EmployeeAdlPensionInfo adlPensionInfo = new EmployeeAdlPensionInfo();
		ArrayList schmeList = new ArrayList();
		familyDtlSelQuery = "SELECT PENSIONNO, REDPENSION, REDPENSION_DATE  , REDDATEOFSUBMSION, QUANTUMOPT , QUANTUMOPTAMOUNT   ,"
				+ " MEMBERDEATHDATE, PAYMENTFLAG , NAMEOFPAYMENTBRANCH , PAYMENTBRANCHADDR   , PINCODE , CLAIMPREBYNOMINEENAME , "
				+ "CLAIMPREBYNOMINEERELATION , MEMBERSCHEMECERT, POSSESSIONMEMBER, SCHEMEADDRESS, SCHEMEPINCODE, PENSIONDRWNUNDEPS , PPOISSUEDBY , LASTACTIVE  , "
				+ "DELETEFLAG,NOMINEERETCAPTIAL,DOCUMENTINFO,CURRENTPOSTING,to_char(SUBMISSIONDATE,'dd/MM/yyyy') as SUBMISSIONDATE,RPFCSTATION,SANCTIONNO,to_char(SANCTIONORDERDT,'dd/MM/yyyy') as SANCTIONORDERDT FROM EPIS_EMPLOYEE_PENSION_DTLS where DELETEFLAG='N' AND  PENSIONNO="
				+ pensionno;
		log
				.info("getPensionForm10DDtls==================familyDtlSelQuery========"
						+ familyDtlSelQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(familyDtlSelQuery);
			if (rs.next()) {

				if (rs.getString("REDPENSION") != null) {
					adlPensionInfo.setEarlyPensionTaken(rs
							.getString("REDPENSION"));
				} else {
					adlPensionInfo.setEarlyPensionTaken("");
				}
				if (rs.getString("REDPENSION_DATE") != null) {
					adlPensionInfo.setEarlyPensionDate(rs
							.getString("REDPENSION_DATE"));
				} else {
					adlPensionInfo.setEarlyPensionDate("");
				}
				if (rs.getString("REDDATEOFSUBMSION") != null) {
					adlPensionInfo.setEpForm10DSubDate(rs
							.getString("REDDATEOFSUBMSION"));
				} else {
					adlPensionInfo.setEpForm10DSubDate("");
				}
				if (rs.getString("QUANTUMOPT") != null) {
					adlPensionInfo.setQuantum1By3Option(rs
							.getString("QUANTUMOPT"));
				} else {
					adlPensionInfo.setQuantum1By3Option("");
				}
				if (rs.getString("QUANTUMOPTAMOUNT") != null) {
					adlPensionInfo.setQuantum1By3Amount(rs
							.getString("QUANTUMOPTAMOUNT"));
				} else {
					adlPensionInfo.setQuantum1By3Amount("");
				}
				if (rs.getString("MEMBERDEATHDATE") != null) {
					adlPensionInfo.setMemberDeathDate(rs
							.getString("MEMBERDEATHDATE"));
				} else {
					adlPensionInfo.setMemberDeathDate("");
				}
				if (rs.getString("DOCUMENTINFO") != null) {
					adlPensionInfo
							.setDocumentInfo(rs.getString("DOCUMENTINFO"));
				} else {
					adlPensionInfo.setDocumentInfo("");
				}
				if (rs.getString("PAYMENTFLAG") != null) {
					adlPensionInfo.setPaymentInfoType(rs
							.getString("PAYMENTFLAG"));
					if (rs.getString("PAYMENTFLAG").trim().toUpperCase()
							.equals("B")) {
						adlPensionInfo.setPaymentInfoTypeDesc("Bank");
					} else {
						adlPensionInfo.setPaymentInfoTypeDesc("Post Office");
					}

				} else {
					adlPensionInfo.setPaymentInfoType("");
				}
				if (rs.getString("NAMEOFPAYMENTBRANCH") != null) {
					adlPensionInfo.setNameofPaymentBranch(rs
							.getString("NAMEOFPAYMENTBRANCH"));
				} else {
					adlPensionInfo.setNameofPaymentBranch("");
				}
				if (rs.getString("PAYMENTBRANCHADDR") != null) {
					adlPensionInfo.setAddressofPayementBranch(rs
							.getString("PAYMENTBRANCHADDR"));
				} else {
					adlPensionInfo.setAddressofPayementBranch("");
				}
				if (rs.getString("PINCODE") != null) {
					adlPensionInfo.setPaymentBranchPincode(rs
							.getString("PINCODE"));
				} else {
					adlPensionInfo.setPaymentBranchPincode("");
				}
				if (rs.getString("CLAIMPREBYNOMINEENAME") != null) {
					adlPensionInfo.setClaimNomineeRefName(rs
							.getString("CLAIMPREBYNOMINEENAME"));
				} else {
					adlPensionInfo.setClaimNomineeRefName("");
				}
				if (rs.getString("CLAIMPREBYNOMINEERELATION") != null) {
					adlPensionInfo.setClaimNomineeRefRelation(rs
							.getString("CLAIMPREBYNOMINEERELATION"));
				} else {
					adlPensionInfo.setClaimNomineeRefRelation("");
				}
				if (rs.getString("MEMBERSCHEMECERT") != null) {
					adlPensionInfo.setSchemeCertificateRecEncl(rs
							.getString("MEMBERSCHEMECERT"));
				} else {
					adlPensionInfo.setSchemeCertificateRecEncl("");
				}
				if (rs.getString("POSSESSIONMEMBER") != null) {
					adlPensionInfo.setPossesionMember(rs
							.getString("POSSESSIONMEMBER"));
				} else {
					adlPensionInfo.setPossesionMember("");
				}
				if (rs.getString("SCHEMEADDRESS") != null) {
					adlPensionInfo.setNomineePostalAddrss(rs
							.getString("SCHEMEADDRESS"));
				} else {
					adlPensionInfo.setNomineePostalAddrss("");
				}
				if (rs.getString("SCHEMEPINCODE") != null) {
					adlPensionInfo.setNomineePincode(rs
							.getString("SCHEMEPINCODE"));
				} else {
					adlPensionInfo.setNomineePincode("");
				}
				if (rs.getString("PENSIONDRWNUNDEPS") != null) {
					adlPensionInfo.setPensionDrwnFrom1995(rs
							.getString("PENSIONDRWNUNDEPS"));
				} else {
					adlPensionInfo.setPensionDrwnFrom1995("");
				}
				if (rs.getString("PPOISSUEDBY") != null) {
					adlPensionInfo
							.setPponoIssuedBy(rs.getString("PPOISSUEDBY"));
				} else {
					adlPensionInfo.setPponoIssuedBy("");
				}
				if (rs.getString("NOMINEERETCAPTIAL") != null) {
					adlPensionInfo.setOptionReturnCaptial(rs
							.getString("NOMINEERETCAPTIAL"));
				} else {
					adlPensionInfo.setOptionReturnCaptial("");
				}

				if (rs.getString("CURRENTPOSTING") != null) {
					adlPensionInfo.setCurrentPostingStation(rs
							.getString("CURRENTPOSTING"));
				} else {
					adlPensionInfo.setCurrentPostingStation("");
				}
				if (rs.getString("SUBMISSIONDATE") != null) {
					adlPensionInfo.setForm10DSubmsionDate(rs
							.getString("SUBMISSIONDATE"));
				} else {
					adlPensionInfo.setForm10DSubmsionDate(commonUtil
							.getCurrentDate("dd/MM/yyyy"));
				}
				if (rs.getString("RPFCSTATION") != null) {
					adlPensionInfo.setForm10DRpfcStation(rs
							.getString("RPFCSTATION"));
				} else {
					adlPensionInfo.setForm10DRpfcStation("");
				}
				if (rs.getString("SANCTIONNO") != null) {
					adlPensionInfo.setSanctionOrderNo(rs
							.getString("SANCTIONNO"));
				} else {
					adlPensionInfo.setSanctionOrderNo("");
				}
				if (rs.getString("SANCTIONORDERDT") != null) {
					adlPensionInfo.setSanctionOrderDate(rs
							.getString("SANCTIONORDERDT"));
				} else {
					adlPensionInfo.setSanctionOrderDate("");
				}
			} else {
				adlPensionInfo.setForm10DSubmsionDate(commonUtil
						.getCurrentDate("dd/MM/yyyy"));
				adlPensionInfo = this.getForm10DCurrentPersonalInfo(con,
						pensionno, adlPensionInfo);
			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return adlPensionInfo;
	}

	public EmployeeAdlPensionInfo getForm10DCurrentPersonalInfo(Connection con,
			String pensionNo, EmployeeAdlPensionInfo editMasterBean) {
		Statement st = null;
		ResultSet rs = null;
		String sqlSelectQuery = "";
		sqlSelectQuery = "SELECT MONTHYEAR,DESEGNATION,AIRPORTCODE,REGION FROM EMPLOYEE_PENSION_VALIDATE WHERE MONTHYEAR IN (SELECT MAX(MONTHYEAR) FROM EMPLOYEE_PENSION_VALIDATE WHERE PENSIONNO="
				+ pensionNo + " AND EMPFLAG='Y') AND PENSIONNO=" + pensionNo;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlSelectQuery);
			if (rs.next()) {
				if (rs.getString("DESEGNATION") != null) {
					editMasterBean.setCurrentPostingDesg(rs
							.getString("DESEGNATION"));
				} else {
					editMasterBean.setCurrentPostingDesg("");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					editMasterBean.setCurrentPostingStation(rs
							.getString("AIRPORTCODE"));
				} else {
					editMasterBean.setCurrentPostingStation("");
				}

				if (rs.getString("REGION") != null) {
					editMasterBean.setCurrentPostingRegion(rs
							.getString("REGION"));
				} else {
					editMasterBean.setCurrentPostingRegion("");
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return editMasterBean;
	}

	public EmployeePersonalInfo getCurrentPersonalInfo(Connection con,
			String pensionNo, EmployeePersonalInfo personalinfo) {
		Statement st = null;
		ResultSet rs = null;
		String sqlSelectQuery = "";
		sqlSelectQuery = "SELECT MONTHYEAR,DESEGNATION,AIRPORTCODE,REGION FROM EMPLOYEE_PENSION_VALIDATE WHERE MONTHYEAR IN (SELECT MAX(MONTHYEAR) FROM EMPLOYEE_PENSION_VALIDATE WHERE PENSIONNO="
				+ pensionNo + " AND EMPFLAG='Y') AND PENSIONNO=" + pensionNo;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("getCurrentPersonalInfo:sqlSelectQuery================="
					+ sqlSelectQuery);
			rs = st.executeQuery(sqlSelectQuery);
			if (rs.next()) {
				if (rs.getString("DESEGNATION") != null) {
					personalinfo.setDesignation(rs.getString("DESEGNATION"));
				}

				if (rs.getString("AIRPORTCODE") != null) {
					personalinfo.setAirportCode(rs.getString("AIRPORTCODE"));
				}

				if (rs.getString("REGION") != null) {
					personalinfo.setRegion(rs.getString("REGION"));
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return personalinfo;
	}

	public ArrayList getForm10DInfo(String pensionNo) {
		Connection con = null;
		ArrayList familyDetailsList = new ArrayList();
		ArrayList pensionDetailsList = new ArrayList();
		ArrayList nomieeDetailList = new ArrayList();
		ArrayList form10DList = new ArrayList();
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		EmployeeAdlPensionInfo adlPensionInfo = new EmployeeAdlPensionInfo();
		ArrayList schemeList = new ArrayList();
		FinancialReportDAO reportDAO = new FinancialReportDAO();
		String fromDate = "", toDate = "", cpfaccnosString = "", dateOfRetriment = "";
		boolean loadTransList = false;
		try {
			con = commonDB.getConnection();
			personalInfo = this.getPersonalInfo(con, pensionNo);
			familyDetailsList = this.form2FamilyReport(personalInfo);
			nomieeDetailList = this.form2NomineeReport(personalInfo);
			schemeList = this.getSchemeDtls(pensionNo, con);
			adlPensionInfo = this.getPensionForm10DDtls(pensionNo);
			try {
				dateOfRetriment = commonDAO.getRetriedDate(personalInfo
						.getDateOfBirth());
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			fromDate = personalInfo.getSeperationFromDate();
			fromDate = "01" + fromDate.substring(2, fromDate.length());
			toDate = personalInfo.getSeperationDate();
			if (fromDate.equals("---")) {
				loadTransList = true;
			} else if (toDate.equals("---")) {
				loadTransList = true;
			}
			log.info("fromDate" + fromDate + "toDate" + toDate
					+ "Wether Option" + personalInfo.getWetherOption().trim());
			if (loadTransList == false) {
				pensionDetailsList = reportDAO.getForm10DPensionInfo(con,
						fromDate, toDate, cpfaccnosString, personalInfo
								.getWetherOption().trim(), dateOfRetriment,
						personalInfo.getDateOfBirth(), pensionNo, true);
			}

			form10DList.add(personalInfo);
			form10DList.add(familyDetailsList);
			form10DList.add(nomieeDetailList);
			form10DList.add(pensionDetailsList);
			form10DList.add(schemeList);
			form10DList.add(adlPensionInfo);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		return form10DList;
	}

	public String savePfidProcessInfo(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		log.info("PensionDAO:savePfidProcessInfo entering method ");
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String insertQry = "", updateQry = "", processId = "";
		int result = 0, result1 = 0;

		FileInputStream inputFileInputStream = null;
		String sheetName = "", updateQuery = "", sqlText = "", extension = "", fileName = "", slashsuffix = "", fileFolder = "";
		File pfidFiles = null;
		BLOB xlsDocument = null;
		log.info("frmName=====  " + empInfo.getFrmName());
		log.info("savePfidProcessInfo:: filename" + empInfo.getFileName());

		ResourceBundle bundle = ResourceBundle
				.getBundle("aims.resource.ApplicationResouces");
		fileFolder = bundle.getString("upload.folder.path.pfidFiles");
		slashsuffix = bundle.getString("upload.folder.path.slashsuffix");

		pfidFiles = new File(empInfo.getFileName());

		try {

			con = DBUtils.getConnection();
			con.setAutoCommit(false);
			processId = getPfidProcessSequence(con);

			sheetName = empInfo.getFileName().substring(
					(empInfo.getFileName().lastIndexOf("\\") + 1),
					empInfo.getFileName().length());
			extension = empInfo.getFileName().substring(
					(empInfo.getFileName().lastIndexOf(".") + 1),
					empInfo.getFileName().length());
			log.info("savePfidProcessInfo::extension============" + extension
					+ "filename" + sheetName);

			String query = "insert into EMPLOYEE_PFID_CREATION_FORM(PROCESSID,Cpfacno,Employeeno,Employeename,DESIGNATION,DATEOFBIRTH,DATEOFJOINING,REGION,AIRPORTCODE,GENDER,FHNAME,WETHEROPTION,VERIFIEDBY,REMARKS,ENTEREDBY,LASTACTIVE,IPADDRESS,TRANSDT,PFIDFILES,FILENAME)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			log.info("==query==" + query);
			pst = con.prepareStatement(query);
			pst.setString(1, processId);
			pst.setString(2, empInfo.getCpfAccno());
			pst.setString(3, empInfo.getEmployeeNumber());
			pst.setString(4, empInfo.getEmployeeName());
			pst.setString(5, empInfo.getDesignation());
			pst.setString(6, empInfo.getDateOfBirth());
			pst.setString(7, empInfo.getDateOfJoining());
			pst.setString(8, empInfo.getRegion());
			pst.setString(9, empInfo.getAirportCode());
			pst.setString(10, empInfo.getGender());
			pst.setString(11, empInfo.getFhName());
			pst.setString(12, empInfo.getWetherOption());
			pst.setString(13, "PERSONAL");
			pst.setString(14, empInfo.getRemarks());
			pst.setString(15, userName);
			pst.setString(16, commonUtil.getCurrentDate("dd-MMM-yyyy"));
			pst.setString(17, ipAddress);
			pst.setString(18, commonUtil.getCurrentDate("dd-MMM-yyyy"));
			pst.setBlob(19, xlsDocument.empty_lob());
			pst.setString(20, sheetName);

			pst.executeUpdate();
			pst.close();

			log.info("==========getProcessID===" + empInfo.getProcessID()
					+ "===processId===" + processId);

			fileName = fileFolder + slashsuffix + sheetName;

			if (!empInfo.getFileName().equals("")) {
				pst = con
						.prepareStatement("select PFIDFILES from EMPLOYEE_PFID_CREATION_FORM where PROCESSID ='"
								+ processId + "' FOR UPDATE");
				rs = pst.executeQuery();

				int bytesRead = 0;
				int bytesWritten = 0;
				int totbytesRead = 0;
				int totbytesWritten = 0;
				int position = 1;

				if (rs.next()) {
					xlsDocument = ((OracleResultSet) rs).getBLOB("PFIDFILES");
					int chunkSize = xlsDocument.getChunkSize();
					log.info("==========chunkSize===" + chunkSize
							+ "FileName ========" + fileName);
					byte[] binaryBuffer = new byte[chunkSize];

					inputFileInputStream = new FileInputStream(new File(
							fileName));
					while ((bytesRead = inputFileInputStream.read(binaryBuffer)) != -1) {
						bytesWritten = xlsDocument.putBytes(position,
								binaryBuffer, bytesRead);
						position += bytesRead;
						totbytesRead += bytesRead;
						totbytesWritten += bytesWritten;
					}
					inputFileInputStream.close();
					log.info("==========totbytesWritten===" + totbytesWritten);

				}
			}
			con.setAutoCommit(true);
			con.commit();
			pst = null;

			createPFIDTrans(con, processId, Constants.PFID_PROCESSING_PERSONAL,
					empInfo, userName, ipAddress, loginUserId, loginUsrStation,
					loginUsrRegion, emailid, loginUsrDesgn);

		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			pst = null;
			commonDB.closeConnection(con, null, rs);

		}
		return processId;
	}

	public String getPfidProcessSequence(Connection con) {
		String processSeqVal = "";
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT PFIDPROCESS_EMPTRACKID.NEXTVAL AS PFIDProcessId FROM DUAL";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				processSeqVal = rs.getString("PFIDProcessId");
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return processSeqVal;
	}

	public int createPFIDTrans(Connection con, String processId,
			String cpfpfwTransCD, EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		int transCode = 0, inserted = 0;
		String sqlQuery = "", transDescr = "";
		String getTransInfo = this.getTranscd(cpfpfwTransCD);
		String[] transInfo = getTransInfo.split(",");
		transCode = Integer.parseInt(transInfo[0]);
		transDescr = transInfo[1];
		Statement st = null;
		ResultSet rs = null;

		try {

			if (this.checkTransInfo(con, processId, transCode).equals("X")) {
				// Update
				sqlQuery = "UPDATE EPIS_PFID_CREATION_TRANS SET APPROVEDBY='"
						+ loginUserId + "',APRVDSIGNNAME='" + userName
						+ "'  WHERE PROCESSID=" + processId
						+ " AND     TRANSCD=" + transCode;
			} else {
				// Insert
				sqlQuery = "INSERT INTO EPIS_PFID_CREATION_TRANS(TRANSNO,TRANSDATE,TRANSCD,TRANSDESCRIPTION,APPROVEDBY,APPROVEDDATE,APRVDSIGNNAME,AIRPORTCODE,REGION,PROCESSID,DESIGNATION,EMAILID) VALUES ("
						+ " PFIDPROCESS_TRANSNO.NEXTVAL,SYSDATE,'"
						+ transCode
						+ "','"
						+ transDescr
						+ "','"
						+ loginUserId
						+ "',SYSDATE,'"
						+ userName
						+ "','"
						+ loginUsrStation
						+ "','"
						+ loginUsrRegion
						+ "','"
						+ processId
						+ "','"
						+ loginUsrDesgn + "','" + emailid + "')";
			}

			log.info("createPFIDTrans() sqlQuery ---- " + sqlQuery);
			st = con.createStatement();
			inserted = st.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return inserted;
	}

	private String checkTransInfo(Connection con, String processId, int transCD) {
		String checkInfo = "";
		Statement st = null;
		ResultSet rs = null;
		String checkTransSelQuery = "SELECT 'X' AS FLAG FROM EPIS_PFID_CREATION_TRANS WHERE PROCESSID="
				+ processId + "   AND TRANSCD=" + transCD;
		log.info("checkTransInfo============" + checkTransSelQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(checkTransSelQuery);
			if (rs.next()) {
				checkInfo = rs.getString("FLAG");
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return checkInfo;
	}

	private String getTranscd(String cpfpfwTransCD) {
		int transcd = 0;
		String description = "";
		StringBuffer buffer = new StringBuffer();
		if (cpfpfwTransCD.equals(Constants.PFID_PROCESSING_PERSONAL)) {
			transcd = 1;
			description = "PFID PROCESS VERIFIED AT STATION";
		} else if (cpfpfwTransCD
				.equals(Constants.PFID_PROCESSING_RECOMMENDATION)) {
			transcd = 2;
			description = "PFID PROCESS RECOMMENDATION AT REGIONAL HEAD QUARTERS";
		} else if (cpfpfwTransCD.equals(Constants.PFID_PROCESSING_APPROVAL)) {
			transcd = 3;
			description = "PFID PROCESS APPROVED AT HEAD QUARTERS";
		} else if (cpfpfwTransCD.equals(Constants.PFID_PROCESSING_CREATION)) {
			transcd = 4;
			description = "PFID CREATED";
		} else if (cpfpfwTransCD.equals(Constants.PFID_PROCESSING_DELETE)) {
			transcd = 5;
			description = "PFID PROCESS DELETED";
		}
		buffer.append(transcd);
		buffer.append(",");
		buffer.append(description);
		return buffer.toString();
	}

	public String updatePFIDProcess(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		log.info("PensionDAO:savePfidProcessInfo entering method ");
		ArrayList list = new ArrayList();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String insertQry = "", updateQry = "", processId = "";
		int result = 0, result1 = 0;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("frmName=====  " + empInfo.getFrmName());
			if (empInfo.getFrmName().equals("PFIDProcessRecommendation")) {

				updateQry = "UPDATE    EMPLOYEE_PFID_CREATION_FORM  SET  "
						+ " Cpfacno='" + empInfo.getCpfAccno() + "' ,"
						+ " Employeeno='" + empInfo.getEmployeeNumber() + "',"
						+ " Employeename ='" + empInfo.getEmployeeName() + "',"
						+ " DESIGNATION ='" + empInfo.getDesignation() + "',"
						+ " DATEOFBIRTH='" + empInfo.getDateOfBirth() + "',"
						+ " DATEOFJOINING ='" + empInfo.getDateOfJoining()
						+ "'," + " REGION ='" + empInfo.getRegion() + "',"
						+ " AIRPORTCODE ='" + empInfo.getAirportCode() + "',"
						+ " GENDER='" + empInfo.getGender() + "',"
						+ " FHNAME='" + empInfo.getFhName() + "',"
						+ " WETHEROPTION ='" + empInfo.getWetherOption() + "',"
						+ " REMARKS ='" + empInfo.getRemarks()
						+ "', VERIFIEDBY='PERSONAL,RHQ', " + " RECOMMENDBY='"
						+ userName + "'," + " LASTACTIVE =sysdate,"
						+ " IPADDRESS ='" + ipAddress + "'   WHERE PROCESSID='"
						+ empInfo.getProcessID() + "'";

			} else if (empInfo.getFrmName().equals("PFIDProcessApproval")) {

				updateQry = "UPDATE    EMPLOYEE_PFID_CREATION_FORM  SET  "
						+ " Cpfacno='" + empInfo.getCpfAccno() + "' ,"
						+ " Employeeno='" + empInfo.getEmployeeNumber() + "',"
						+ " Employeename ='" + empInfo.getEmployeeName() + "',"
						+ " DESIGNATION ='" + empInfo.getDesignation() + "',"
						+ " DATEOFBIRTH='" + empInfo.getDateOfBirth() + "',"
						+ " DATEOFJOINING ='" + empInfo.getDateOfJoining()
						+ "'," + " REGION ='" + empInfo.getRegion() + "',"
						+ " AIRPORTCODE ='" + empInfo.getAirportCode() + "',"
						+ " GENDER='" + empInfo.getGender() + "',"
						+ " FHNAME='" + empInfo.getFhName() + "',"
						+ " WETHEROPTION ='" + empInfo.getWetherOption() + "',"
						+ " REMARKS ='" + empInfo.getRemarks()
						+ "', VERIFIEDBY='PERSONAL,RHQ,CHQ',"
						+ " APPROVEDDT =sysdate," + " APPROVEDBY ='" + userName
						+ "'," + " LASTACTIVE =sysdate," + " IPADDRESS ='"
						+ ipAddress + "'   WHERE PROCESSID='"
						+ empInfo.getProcessID() + "'";

			}
			log.info("updatePFIDProcess=====updateQry " + updateQry);
			result1 = st.executeUpdate(updateQry);

			if (empInfo.getFrmName().equals("PFIDProcessRecommendation")) {

				createPFIDTrans(con, empInfo.getProcessID(),
						Constants.PFID_PROCESSING_RECOMMENDATION, empInfo,
						userName, ipAddress, loginUserId, loginUsrStation,
						loginUsrRegion, emailid, loginUsrDesgn);

			} else if (empInfo.getFrmName().equals("PFIDProcessApproval")) {

				createPFIDTrans(con, empInfo.getProcessID(),
						Constants.PFID_PROCESSING_APPROVAL, empInfo, userName,
						ipAddress, loginUserId, loginUsrStation,
						loginUsrRegion, emailid, loginUsrDesgn);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return processId;

	}

	public ArrayList searchForPfidProcess(EmployeePersonalInfo empBean,
			String userRegion, String userStation, String profileType,
			String accessCode, String accountType) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "", status = "", pfidCreationFlag = "";
		ArrayList al = new ArrayList();
		EmployeePersonalInfo empPerInfo = null;
		DecimalFormat df = new DecimalFormat("#########0");
		selectQuery = this.buildSearchQueryForPFIDProcess(empBean, userRegion,
				userStation, profileType, accessCode, accountType);
		log.info("searchForPfidProcess()==========" + selectQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectQuery);
			while (rs.next()) {
				empPerInfo = new EmployeePersonalInfo();
				if (rs.getString("PROCESSID") != null) {
					empPerInfo.setProcessID(rs.getString("PROCESSID"));
				} else {
					empPerInfo.setProcessID("---");
				}
				if (rs.getString("CPFACNO") != null) {
					empPerInfo.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					empPerInfo.setCpfAccno("---");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					empPerInfo.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					empPerInfo.setEmployeeNumber("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empPerInfo.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					empPerInfo.setEmployeeName("---");
				}
				if (rs.getString("GENDER") != null) {
					empPerInfo.setGender(rs.getString("GENDER"));
				} else {
					empPerInfo.setGender("---");
				}
				if (rs.getString("FHNAME") != null) {
					empPerInfo.setFhName(rs.getString("FHNAME"));
				} else {
					empPerInfo.setFhName("---");
				}

				if (rs.getString("AIRPORTCODE") != null) {
					empPerInfo.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					empPerInfo.setAirportCode("");
				}
				if (rs.getString("REGION") != null) {
					empPerInfo.setRegion(rs.getString("REGION"));
				} else {
					empPerInfo.setRegion("");
				}
				if (rs.getString("DESIGNATION") != null) {
					empPerInfo.setDesignation(rs.getString("DESIGNATION"));
				} else {
					empPerInfo.setDesignation("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					empPerInfo.setDateOfBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					empPerInfo.setDateOfBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empPerInfo.setDateOfJoining(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					empPerInfo.setDateOfJoining("---");
				}
				if (rs.getString("FILENAME") != null) {
					empPerInfo.setFileName(rs.getString("FILENAME"));
				} else {
					empPerInfo.setFileName("---");
				}

				if (rs.getString("PFIDCREATION") != null) {
					pfidCreationFlag = rs.getString("PFIDCREATION");
				}
				if (rs.getString("PENSIONNO") != null) {
					empPerInfo.setPensionNo(rs.getString("PENSIONNO"));
				}
				if (rs.getString("TRANSDT") != null) {
					empPerInfo.setTransDt(CommonUtil.converDBToAppFormat(rs
							.getDate("TRANSDT")));
				} else {
					empPerInfo.setTransDt("---");
				}
				if (rs.getString("ENTEREDBY") != null) {
					empPerInfo.setEnteredBy(rs.getString("ENTEREDBY"));
				} else {
					empPerInfo.setEnteredBy("---");
				}
				if (rs.getString("RECOMMENDBY") != null) {
					empPerInfo.setRecomendBy(rs.getString("RECOMMENDBY"));
				} else {
					empPerInfo.setRecomendBy("---");
				}
				if (rs.getString("APPROVEDBY") != null) {
					empPerInfo.setApprovedBy(rs.getString("APPROVEDBY"));
				} else {
					empPerInfo.setApprovedBy("---");
				}

				if (rs.getString("VERIFIEDBY") != null) {
					verifiedBy = rs.getString("VERIFIEDBY");
					empPerInfo.setVerifiedBy(verifiedBy);
				}

				if (!verifiedBy.equals("")) {
					if (empBean.getFrmName().equals("PFIDProcessSearch")) {
						if (verifiedBy.equals("PERSONAL")) {
							status = "Verified by Personal";
						} else if (verifiedBy.equals("PERSONAL,RHQ")) {
							status = "Verified by Personal,RHQ";
						} else if (verifiedBy.equals("PERSONAL,RHQ,CHQ")) {
							status = "Approved";
						} else {
							status = "New";
						}
					} else if (empBean.getFrmName().equals(
							"PFIDRecommendationSearch")) {
						if (verifiedBy.equals("PERSONAL,RHQ")
								|| verifiedBy.equals("PERSONAL,RHQ,CHQ")) {
							status = "A";
						} else {
							status = "N";
						}
					} else if (empBean.getFrmName()
							.equals("PFIDApprovalSearch")) {
						if (verifiedBy.equals("PERSONAL,RHQ,CHQ")) {
							status = "A";
						} else {
							status = "N";
						}
					} else if (empBean.getFrmName()
							.equals("PFIDApprovedSearch")) {
						if (pfidCreationFlag.equals("A")) {
							status = "PFID Created";
						} else {
							status = "N";
						}
					}
				}

				empPerInfo.setPfidProcessStatus(status);

				al.add(empPerInfo);

			}
			log.info("searchLIst" + al.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs.close();
				commonDB.closeConnection(con, st, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return al;
	}

	public String buildSearchQueryForPFIDProcess(EmployeePersonalInfo empBean,
			String userRegion, String userStation, String profileType,
			String accessCode, String accountType) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", orderBy = "", sqlQuery = "", verifiedby = "", profilecondition = "";

		log.info("==userRegion==" + userRegion + "==userStation===="
				+ userStation + "==profileType==" + profileType
				+ "==accessCode==" + accessCode + "==empBean.getFrmName()=="
				+ empBean.getFrmName());

		sqlQuery = " SELECT EPCF.PROCESSID AS  PROCESSID, EPCF.EMPLOYEENO AS EMPLOYEENO, EPCF.EMPLOYEENAME AS EMPLOYEENAME, EPCF.CPFACNO AS CPFACNO ,EPCF.DESIGNATION AS DESIGNATION, EPCF.DATEOFBIRTH AS DATEOFBIRTH,EPCF.DATEOFJOINING AS   DATEOFJOINING,EPCF.REGION AS REGION,EPCF.AIRPORTCODE AS AIRPORTCODE,EPCF.VERIFIEDBY AS VERIFIEDBY,EPCF.PFIDCREATION AS PFIDCREATION ,EPCF.PENSIONNO AS PENSIONNO,EPCF.GENDER AS GENDER, EPCF.FHNAME AS FHNAME,EPCF.FILENAME AS FILENAME,EPCF.TRANSDT AS TRANSDT, EPCF.ENTEREDBY AS ENTEREDBY,EPCF.RECOMMENDBY AS RECOMMENDBY, EPCF.APPROVEDBY AS APPROVEDBY FROM EMPLOYEE_PFID_CREATION_FORM  EPCF WHERE DELETEFLAG='N'";

		if (empBean.getFrmName().equals("PFIDRecommendationSearch")) {
			sqlQuery += "AND  EPCF.VERIFIEDBY  IS NOT NULL AND  EPCF.VERIFIEDBY   LIKE 'PERSONAL%'";
		} else if (empBean.getFrmName().equals("PFIDApprovalSearch")) {
			sqlQuery += "AND  EPCF.VERIFIEDBY  IS NOT NULL AND  EPCF.VERIFIEDBY   LIKE 'PERSONAL,RHQ%' AND   PFIDCREATION='N'";
		} else if (empBean.getFrmName().equals("PFIDApprovedSearch")) {
			sqlQuery += "AND  EPCF.VERIFIEDBY  IS NOT NULL AND  EPCF.VERIFIEDBY   LIKE 'PERSONAL,RHQ,CHQ'  AND PFIDCREATION='Y'";
		}

		if (!(profileType.equals("C") || profileType.equals("S") || profileType
				.equals("A"))) {

			if (profileType.equals("R")) {
				if (!userRegion.equals("CHQIAD")) {
					whereClause
							.append(" LOWER(EPCF.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
									+ userRegion.toLowerCase().trim() + "')");
					whereClause.append(" AND ");
				} else {
					// For Restricting Rigths to RAU of CHQIAD to SAU Accounts
					// on 07-Jun-2012
					if (!userStation.equals("")) {
						whereClause
								.append(" LOWER(EPCF.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
										+ userRegion.toLowerCase().trim()
										+ "' AND ACCOUNTTYPE='RAU')");

						whereClause.append(" AND ");
					}
				}
			} else {
				if (!userStation.equals("")) {
					whereClause.append(" LOWER(EPCF.AIRPORTCODE) like'%"
							+ userStation.toLowerCase().trim() + "%'");
					whereClause.append(" AND ");
				}
			}

			if (!userRegion.equals("")) {
				whereClause.append(" LOWER(EPCF.REGION) like'%"
						+ userRegion.toLowerCase().trim() + "%'");
				whereClause.append(" AND ");
			}
		}

		if (!empBean.getRegion().equals("")) {
			whereClause.append(" LOWER(EPCF.REGION) like'%"
					+ empBean.getRegion().toLowerCase().trim() + "%'");
			whereClause.append(" AND ");
		}
		if (!empBean.getAirportCode().equals("")) {
			whereClause.append(" LOWER(EPCF.AIRPORTCODE) like'%"
					+ empBean.getAirportCode().toLowerCase().trim() + "%'");
			whereClause.append(" AND ");
		}
		if (!empBean.getEmployeeName().equals("")) {
			whereClause.append(" EPCF.EMPLOYEENAME ='"
					+ empBean.getEmployeeName().trim() + "'");
			whereClause.append(" AND ");
		}
		if (!empBean.getEmployeeNumber().equals("")) {
			whereClause.append(" EPCF.EMPLOYEENO ='"
					+ empBean.getEmployeeNumber().trim() + "'");
			whereClause.append(" AND ");
		}
		if (!empBean.getDateOfBirth().equals("")) {
			whereClause.append(" EPCF.DATEOFBIRTH ='"
					+ empBean.getDateOfBirth() + "'");
			whereClause.append(" AND ");
		}
		if (!empBean.getDateOfJoining().equals("")) {
			whereClause.append(" EPCF.DATEOFJOINING ='"
					+ empBean.getDateOfJoining() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);

		if (empBean.getRegion().equals("")
				&& empBean.getAirportCode().equals("")
				&& empBean.getEmployeeName().equals("")
				&& empBean.getEmployeeNumber().equals("")
				&& empBean.getDateOfBirth().equals("")
				&& empBean.getDateOfJoining().equals("")) {
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}

		orderBy = "ORDER BY EPCF.PROCESSID";

		query.append(orderBy);
		dynamicQuery = query.toString();

		return dynamicQuery;

	}

	public ArrayList editPfidProcessInfo(String processId, String frmName) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "", status = "";
		ArrayList al = new ArrayList();
		EmployeePersonalInfo empPerInfo = null;
		DecimalFormat df = new DecimalFormat("#########0");
		selectQuery = "SELECT EPCF.PROCESSID AS  PROCESSID, EPCF.EMPLOYEENO AS EMPLOYEENO, EPCF.EMPLOYEENAME AS EMPLOYEENAME, EPCF.CPFACNO AS CPFACNO ,EPCF.DESIGNATION AS DESIGNATION,EPCF.GENDER AS GENDER, "
				+ " EPCF.DATEOFBIRTH AS DATEOFBIRTH,EPCF.DATEOFJOINING AS   DATEOFJOINING,EPCF.REGION AS REGION,EPCF.AIRPORTCODE AS AIRPORTCODE,EPCF.VERIFIEDBY AS VERIFIEDBY,EPCF.FHNAME AS FHNAME, EPCF.REMARKS AS REMARKS,EPCF.WETHEROPTION AS WETHEROPTION,EPCF.FILENAME AS  FILENAME  FROM EMPLOYEE_PFID_CREATION_FORM  EPCF WHERE DELETEFLAG='N' AND PROCESSID='"
				+ processId + "'";
		log.info("editPfidProcessInfo()==========" + selectQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectQuery);
			while (rs.next()) {
				empPerInfo = new EmployeePersonalInfo();
				if (rs.getString("PROCESSID") != null) {
					empPerInfo.setProcessID(rs.getString("PROCESSID"));
				} else {
					empPerInfo.setProcessID("---");
				}
				if (rs.getString("CPFACNO") != null) {
					empPerInfo.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					empPerInfo.setCpfAccno("---");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					empPerInfo.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					empPerInfo.setEmployeeNumber("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empPerInfo.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					empPerInfo.setEmployeeName("---");
				}
				if (rs.getString("FHNAME") != null) {
					empPerInfo.setFhName(rs.getString("FHNAME"));
				} else {
					empPerInfo.setFhName("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					empPerInfo.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					empPerInfo.setWetherOption("---");
				}
				if (rs.getString("GENDER") != null) {
					empPerInfo.setGender(rs.getString("GENDER"));
				} else {
					empPerInfo.setGender("---");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					empPerInfo.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					empPerInfo.setAirportCode("---");
				}
				if (rs.getString("REGION") != null) {
					empPerInfo.setRegion(rs.getString("REGION"));
				} else {
					empPerInfo.setRegion("---");
				}
				if (rs.getString("DESIGNATION") != null) {
					empPerInfo.setDesignation(rs.getString("DESIGNATION"));
				} else {
					empPerInfo.setDesignation("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					empPerInfo.setDateOfBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					empPerInfo.setDateOfBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empPerInfo.setDateOfJoining(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					empPerInfo.setDateOfJoining("---");
				}

				if (rs.getString("VERIFIEDBY") != null) {
					empPerInfo.setVerifiedBy(rs.getString("VERIFIEDBY"));
				}
				if (rs.getString("REMARKS") != null) {
					empPerInfo.setRemarks(rs.getString("REMARKS"));
				}
				if (rs.getString("FILENAME") != null) {
					empPerInfo.setFileName(rs.getString("FILENAME"));
				} else {
					empPerInfo.setFileName("---");
				}

				al.add(empPerInfo);

			}
			log.info("searchLIst" + al.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs.close();
				commonDB.closeConnection(con, st, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return al;
	}

	public String createPFIDProcess(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		log.info("PersonalDAO :createNewPFIDS() entering method");
		Connection conn = null;
		Statement st = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		String uniqueID = "", pensionNo = "", insertNewRecordInPersonalInfo = "", pensionObQuery = "", FinYear = "", obYear = "", dojYear = "", doj = "", message = "", updateQry = "";
		String year[] = null;
		int dojMnth = 0, length = 0;
		int addBatchcount[] = { 0 };
		int addOBBatchCount[] = { 0 };
		int addProcBatchCount[] = { 0 };
		try {
			conn = commonDB.getConnection();
			st = conn.createStatement();

			uniqueID = this.getSequenceNo(conn);
			pensionNo = commonDAO.getPFID(empInfo.getEmployeeName(), empInfo
					.getDateOfBirth(), uniqueID);

			log.info("uniqueID == " + uniqueID + "Pension No==" + pensionNo
					+ "Employee Name" + empInfo.getEmployeeName());
			log.info(" dob " + empInfo.getDateOfBirth() + "doj "
					+ empInfo.getDateOfJoining());
			log.info(" CPFACCNO " + empInfo.getCpfAccno() + "Employeeno"
					+ empInfo.getEmployeeNumber() + "Desigantion"
					+ empInfo.getDesignation());
			log.info(" GENDER " + empInfo.getGender() + "fhName"
					+ empInfo.getFhName());

			/*
			 * if (!empInfo.getCpfAccno().equals("")) { updateEmpSerialNumber =
			 * "update employee_info set EMPSERIALNUMBER='" +
			 * empInfo.getPensionNo() + "' ,DATEOFBIRTH='" +
			 * empInfo.getDateOfBirth() + "',dateofjoining='" +
			 * empInfo.getDateOfJoining() + "',FHNAME='" + empInfo.getFhName() +
			 * "',DESEGNATION ='" + empInfo.getDesignation() +
			 * "',wetheroption='" + empInfo.getWetherOption() + "',sex='" +
			 * empInfo.getGender() + "' where cpfacno='" + empInfo.getCpfAccno() + "'
			 * and region='" + empInfo.getRegion() + "'"; updateTransaction =
			 * "update employee_pension_validate set pensionno='" +
			 * empInfo.getPensionNo() + "' where cpfaccno='" +
			 * empInfo.getCpfAccno() + "' and region='" + empInfo.getRegion() +
			 * "'"; }
			 */
			insertNewRecordInPersonalInfo = "insert into employee_personal_info (cpfacno,employeename,dateofbirth,dateofjoining,GENDER,airportcode,region,USERNAME,ipaddress,lastactive,pensionno,WETHEROPTION,EMPLOYEENO,DESEGNATION,FHNAME,REMARKS) values('"
					+ empInfo.getCpfAccno()
					+ "','"
					+ empInfo.getEmployeeName()
					+ "','"
					+ empInfo.getDateOfBirth()
					+ "','"
					+ empInfo.getDateOfJoining()
					+ "','"
					+ empInfo.getGender()
					+ "','"
					+ empInfo.getAirportCode()
					+ "','"
					+ empInfo.getRegion()
					+ "','"
					+ userName
					+ "','"
					+ ipAddress.trim()
					+ "',sysdate,'"
					+ uniqueID.trim()
					+ "','"
					+ empInfo.getWetherOption()
					+ "','"
					+ empInfo.getEmployeeNumber()
					+ "','"
					+ empInfo.getDesignation()
					+ "','"
					+ empInfo.getFhName()
					+ "', 'Thru PFID Process')";

			st.addBatch(insertNewRecordInPersonalInfo);
			log.info("insertNewRecordInPersonalInfo  "
					+ insertNewRecordInPersonalInfo);

			log.info("-----doj-----" + empInfo.getDateOfJoining());
			doj = commonUtil.converDBToAppFormat(empInfo.getDateOfJoining(),
					"dd-MMM-yyyy", "dd-MM-yyyy");
			year = doj.split("-");
			dojMnth = Integer.parseInt(year[1]);
			dojYear = year[2];
			log.info("-----dojMnth-----" + dojMnth);
			if (dojMnth >= 4 && dojMnth <= 12) {
				FinYear = dojYear + "-" + ((Integer.parseInt(dojYear)) + 1);
				obYear = "01-Apr-" + dojYear;
			} else if (dojMnth >= 1 && dojMnth <= 3) {
				FinYear = ((Integer.parseInt(dojYear)) - 1) + "-" + dojYear;
				obYear = "01-Apr-" + ((Integer.parseInt(dojYear)) - 1);
			}

			log.info("-----obYear-----" + obYear);
			pensionObQuery = "insert into employee_pension_ob (PENSIONNO, OBYEAR ,EMPNETOB , AAINETOB ,PENSIONOB , OBFLAG  ,REMARKS , CPFACCNO, REGION , AIRPORTCODE  ,  PRINCIPALOB, OUTSTANDADV,  FINYEAR ,  DESEGNATION  , EMPLOYEENO, EMPLOYEENAME ) values('"
					+ uniqueID.trim()
					+ "','"
					+ obYear.trim()
					+ "','0.00','0.00','0.00','Y','AAI New Entry','"
					+ empInfo.getCpfAccno().trim()
					+ "','"
					+ empInfo.getRegion()
					+ "','"
					+ empInfo.getAirportCode()
					+ "','0.00','0.00','"
					+ FinYear
					+ "','"
					+ empInfo.getDesignation().trim()
					+ "','"
					+ empInfo.getEmployeeNumber().trim()
					+ "','"
					+ empInfo.getEmployeeName().trim() + "')";
			log.info("pensionObQuery  " + pensionObQuery);
			st.addBatch(pensionObQuery);

			updateQry = "UPDATE  EMPLOYEE_PFID_CREATION_FORM SET PFIDCREATION='Y',PENSIONNO='"
					+ uniqueID
					+ "' WHERE PROCESSID='"
					+ empInfo.getProcessID()
					+ "'";
			log.info("updateQry  " + updateQry);
			st.addBatch(updateQry);

			int insertCount[] = st.executeBatch();
			log.info("insertCount  " + insertCount.length);
			createPFIDTrans(conn, empInfo.getProcessID(),
					Constants.PFID_PROCESSING_CREATION, empInfo, userName,
					ipAddress, loginUserId, loginUsrStation, loginUsrRegion,
					emailid, loginUsrDesgn);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(conn, pst, null);
			commonDB.closeConnection(conn, pst1, null);
			commonDB.closeConnection(conn, st, null);
		}
		log.info("length" + length);
		log.info("ImportDao :createNewPFIDS leaving method");
		message = String.valueOf(length);
		return message;
	}

	public String deletePFIDProcess(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		log.info("PensionDAO:deletePFIDProcess entering method ");
		ArrayList list = new ArrayList();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String insertQry = "", deleteQry = "", processId = "";
		int result = 0, result1 = 0;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("frmName=====  " + empInfo.getFrmName());

			deleteQry = "UPDATE    EMPLOYEE_PFID_CREATION_FORM  SET DELETEFLAG='N'   WHERE PROCESSID='"
					+ empInfo.getProcessID() + "'";
			log.info("deletePFIDProcess=====deleteQry " + deleteQry);
			result1 = st.executeUpdate(deleteQry);

			createPFIDTrans(con, empInfo.getProcessID(),
					Constants.PFID_PROCESSING_DELETE, empInfo, userName,
					ipAddress, loginUserId, loginUsrStation, loginUsrRegion,
					emailid, loginUsrDesgn);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return processId;

	}

	public String chkForDuplicateEntry(String employeeName, String dateOfBirth) {
		log.info("PensionDAO:chkForDuplicateEntry entering method ");
		ArrayList list = new ArrayList();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selQry = "", valEmpNameAndDOB = "---";

		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			selQry = " SELECT PENSIONNO FROM EMPLOYEE_PERSONAL_INFO WHERE UPPER(EMPLOYEENAME) ='"
					+ employeeName.toUpperCase()
					+ "' AND DATEOFBIRTH ='"
					+ dateOfBirth + "'";
			log.info("chkForDuplicateEntry=====selQry " + selQry);
			rs = st.executeQuery(selQry);
			if (rs.next()) {
				if (rs.getString("PENSIONNO") != null) {
					valEmpNameAndDOB = rs.getString("PENSIONNO");
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return valEmpNameAndDOB;

	}

	public String readPFIDFiles(String processid, String fileName)
			throws IOException, SQLException, Exception {
		log.info("PensionDAO:readPFIDFiles entering method ");

		FileOutputStream outputFileOutputStream = null;
		String sqlText = null;
		Statement stmt = null;
		ResultSet rset = null;
		long blobLength;
		long position;
		BLOB pfidFile = null;
		int chunkSize;
		byte[] binaryBuffer;
		int bytesRead = 0;
		int totbytesRead = 0;
		int totbytesWritten = 0;
		Connection con = null;
		String fileFolder = "", outputFilePath = "";
		try {
			con = DBUtils.getConnection();
			stmt = con.createStatement();
			con.setAutoCommit(false);

			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			fileFolder = bundle.getString("upload.folder.path.pfidFiles");
			outputFilePath = fileFolder
					+ bundle.getString("upload.folder.path.slashsuffix")
					+ fileName;
			File filePath = new File(fileFolder);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}

			log.info("==outputFilePath=" + outputFilePath);
			File outputBinaryFile1 = new File(outputFilePath);

			outputFileOutputStream = new FileOutputStream(outputBinaryFile1);
			sqlText = "SELECT PFIDFILES "
					+ "FROM   EMPLOYEE_PFID_CREATION_FORM "
					+ "WHERE  PROCESSID = '" + processid + "' FOR UPDATE";
			rset = stmt.executeQuery(sqlText);
			rset.next();
			pfidFile = ((OracleResultSet) rset).getBLOB("PFIDFILES");

			blobLength = pfidFile.length();
			log.info("====blobLength===" + blobLength);
			chunkSize = pfidFile.getChunkSize();
			binaryBuffer = new byte[chunkSize];
			for (position = 1; position <= blobLength; position += chunkSize) {
				bytesRead = pfidFile
						.getBytes(position, chunkSize, binaryBuffer);
				outputFileOutputStream.write(binaryBuffer, 0, bytesRead);
				totbytesRead += bytesRead;
				totbytesWritten += bytesRead;
			}
			outputFileOutputStream.close();
			con.commit();
			rset.close();
			stmt.close();

		} catch (IOException e) {
			con.rollback();
			log
					.error("Caught I/O Exception: (Write BLOB value to file - Get Method).");
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			con.rollback();
			log
					.error("Caught SQL Exception: (Write BLOB value to file - Get Method).");
			log.error("SQL:\n" + sqlText);
			e.printStackTrace();
			throw e;
		}

		return outputFilePath;

	}

	public String savePfidProcessEditInfo(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		log.info("PensionDAO:savePfidProcessEditInfo entering method ");
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		String insertQry = "", updateQry = "", processId = "";
		int result = 0, result1 = 0;
		FileInputStream inputFileInputStream = null;
		String sheetName = "", updateQuery = "", sqlText = "", extension = "", fileCondition = "", fileName = "", fileFolder = "", slashsuffix = "";
		BLOB xlsDocument = null;
		log.info("frmName=====  " + empInfo.getFrmName());
		log.info("savePfidProcessEditInfo:: filename" + empInfo.getFileName());

		try {

			con = DBUtils.getConnection();

			st = con.createStatement();

			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			fileFolder = bundle.getString("upload.folder.path.pfidFiles");
			slashsuffix = bundle.getString("upload.folder.path.slashsuffix");

			if (!empInfo.getFileName().equals("")) {
				sheetName = empInfo.getFileName().substring(
						(empInfo.getFileName().lastIndexOf("\\") + 1),
						empInfo.getFileName().length());
				extension = empInfo.getFileName().substring(
						(empInfo.getFileName().lastIndexOf(".") + 1),
						empInfo.getFileName().length());
				log.info("savePfidProcessInfo::extension============"
						+ extension + "filename" + sheetName);
				fileCondition = "FILENAME ='" + sheetName + "',";
			}

			updateQry = "UPDATE    EMPLOYEE_PFID_CREATION_FORM  SET  "
					+ " Cpfacno='" + empInfo.getCpfAccno() + "' ,"
					+ " Employeeno='" + empInfo.getEmployeeNumber() + "',"
					+ " Employeename ='" + empInfo.getEmployeeName() + "',"
					+ " DESIGNATION ='" + empInfo.getDesignation() + "',"
					+ " DATEOFBIRTH='" + empInfo.getDateOfBirth() + "',"
					+ " DATEOFJOINING ='" + empInfo.getDateOfJoining() + "',"
					+ " REGION ='" + empInfo.getRegion() + "',"
					+ " AIRPORTCODE ='" + empInfo.getAirportCode() + "',"
					+ " GENDER='" + empInfo.getGender() + "'," + " FHNAME='"
					+ empInfo.getFhName() + "'," + " WETHEROPTION ='"
					+ empInfo.getWetherOption() + "'," + fileCondition
					+ " REMARKS ='" + empInfo.getRemarks() + "',"
					+ " ENTEREDBY ='" + userName + "',"
					+ " LASTACTIVE =sysdate," + " IPADDRESS ='" + ipAddress
					+ "'   WHERE PROCESSID='" + empInfo.getProcessID() + "'";

			log.info("savePfidProcessEditInfo::updateQry============"
					+ updateQry);
			st.executeUpdate(updateQry);

			if (!empInfo.getFileName().equals("")) {

				con.setAutoCommit(false);
				pst = con
						.prepareStatement("select PFIDFILES from EMPLOYEE_PFID_CREATION_FORM where PROCESSID ='"
								+ empInfo.getProcessID() + "' FOR UPDATE");
				rs = pst.executeQuery();

				int bytesRead = 0;
				int bytesWritten = 0;
				int totbytesRead = 0;
				int totbytesWritten = 0;
				int position = 1;
				fileName = fileFolder + slashsuffix + sheetName;
				if (rs.next()) {
					xlsDocument = ((OracleResultSet) rs).getBLOB("PFIDFILES");
					int chunkSize = xlsDocument.getChunkSize();
					log.info("==========chunkSize===" + chunkSize
							+ "FileName ========" + fileName);
					byte[] binaryBuffer = new byte[chunkSize];

					inputFileInputStream = new FileInputStream(new File(
							fileName));
					while ((bytesRead = inputFileInputStream.read(binaryBuffer)) != -1) {
						bytesWritten = xlsDocument.putBytes(position,
								binaryBuffer, bytesRead);
						position += bytesRead;
						totbytesRead += bytesRead;
						totbytesWritten += bytesWritten;
					}
					inputFileInputStream.close();
					log.info("==========totbytesWritten===" + totbytesWritten);

				}
				con.setAutoCommit(true);
				con.commit();
				pst = null;
			}
			createPFIDTrans(con, empInfo.getProcessID(),
					Constants.PFID_PROCESSING_PERSONAL, empInfo, userName,
					ipAddress, loginUserId, loginUsrStation, loginUsrRegion,
					emailid, loginUsrDesgn);

		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			pst = null;
			commonDB.closeConnection(con, null, rs);

		}
		return processId;
	}

	public int personalUpdateRevised(EmpMasterBean bean, String flag)
			throws InvalidDataException {
		log.info("PersonalDAO:updatePensionMasterRevised entering method ");
		// PensionBean editBean =new PensionBean();
		Connection con = null;
		Statement st = null;
		int count = 0;
		String srno = "", airportSerialNumber = "", empNumber = "", cpfAcNo = "", newCpfAcno = "";
		String empName = "", desegnation = "", empLevel = "", seperationReason = "", whetherOptionA = "";
		String whetherOptionB = "", whetherOptionNO = "", form2Nomination = "";
		String remarks = "", station = "", dateofBirth = "", dateofJoining = "", dateofSeperationDate = "";
		String fMemberName = "", fDateofBirth = "", frelation = "", familyrows = "";
		String wetherOption = "", sex = "", maritalStatus = "", fhName = "", permanentAddress = "", temporatyAddress = "", dateOfAnnuation = "", otherReason = "";
		String pfId = "";
		int count1[] = new int[3];
		pfId = bean.getPensionNumber();
		airportSerialNumber = bean.getAirportSerialNumber();
		empNumber = bean.getEmpNumber();
		cpfAcNo = bean.getCpfAcNo().trim();
		newCpfAcno = bean.getNewCpfAcNo();
		station = bean.getStation();
		empName = bean.getEmpName();
		desegnation = bean.getDesegnation();
		empLevel = bean.getEmpLevel();
		seperationReason = bean.getSeperationReason();
		whetherOptionA = bean.getWhetherOptionA();
		whetherOptionB = bean.getWhetherOptionB();
		whetherOptionNO = bean.getWhetherOptionNO();
		remarks = bean.getRemarks();
		dateofBirth = bean.getDateofBirth();
		dateofJoining = bean.getDateofJoining();
		dateofSeperationDate = bean.getDateofSeperationDate();
		form2Nomination = bean.getForm2Nomination();
		String pensionNumber = bean.getPensionNumber();
		String empNomineeSharable = bean.getEmpNomineeSharable();
		wetherOption = bean.getWetherOption();
		sex = bean.getSex();
		maritalStatus = bean.getMaritalStatus();
		fhName = bean.getFhName();
		permanentAddress = bean.getPermanentAddress();
		temporatyAddress = bean.getTemporatyAddress();
		dateOfAnnuation = bean.getDateOfAnnuation();
		// String pensionNumber = this.getPensionNumber(empName.toUpperCase(),
		// dateofBirth, cpfAcNo);
		otherReason = bean.getOtherReason().trim();
		String division = bean.getDivision();
		String department = bean.getDepartment();
		String emailId = bean.getEmailId();
		String empOldName = bean.getEmpOldName();
		String empOldNumber = bean.getEmpOldNumber();
		String region = bean.getRegion();
		String setRecordVerified = bean.getRecordVerified();
		log.info("PersonalDAO:userName " + bean.getUserName());
		log.info("PersonalDAO:computer Name" + bean.getComputerName());
		log.info("PersonalDAO:pfid" + bean.getEmpSerialNo());
		log.info("PersonalDAO:pensionno" + bean.getPensionNumber());
		java.util.Date now = new java.util.Date();
		String MY_DATE_FORMAT = "dd-MM-yyyy hh:mm a";
		String currDateTime = new SimpleDateFormat(MY_DATE_FORMAT).format(now);
		log.info("date is  " + currDateTime);
		String fhFlag = bean.getFhFlag();
		if (!bean.getChangedRegion().equals("")) {
			region = bean.getChangedRegion();
		}
		if (!bean.getChangedStation().equals("")) {
			station = bean.getChangedStation();
		}

		String changedStation = bean.getChangedStation();

		try {
			String query = "";
			con = commonDB.getConnection();
			st = con.createStatement();
			String sql1 = "";
			this.insertEmployeeHistoryinfo(pfId, cpfAcNo, "", true, "", region,
					currDateTime, bean.getComputerName().trim(), bean
							.getUserName());
			if (newCpfAcno.equals(cpfAcNo.trim())
					&& bean.getRegion().equals(bean.getNewRegion().trim())) {
			} else {
				int foundCPFNO = pensionDAO.empEditCpfAcno(empName, station,
						desegnation, newCpfAcno, bean.getNewRegion());

				System.out.println("foundCPFNO====empEditCpfAcno===="
						+ foundCPFNO + "new pensionNumber==============="
						+ pensionNumber);

				String sql3 = "update  EMPLOYEE_PERSONAL_INFO set airportcode='"
						+ station
						+ "',cpfacno='"
						+ newCpfAcno
						+ "',employeename='"
						+ empName.trim()
						+ "',desegnation='"
						+ desegnation
						+ "',AIRPORTSERIALNUMBER='"
						+ airportSerialNumber
						+ "',EMPLOYEENO='"
						+ empNumber
						+ "',EMP_LEVEL='"
						+ empLevel
						+ "',DATEOFBIRTH ='"
						+ dateofBirth
						+ "',DATEOFJOINING='"
						+ dateofJoining
						+ "',DATEOFSEPERATION_REASON='"
						+ seperationReason
						+ "',DATEOFSEPERATION_DATE='"
						+ dateofSeperationDate
						+ "',REMARKS='"
						+ remarks.trim()
						+ "',gender='"
						+ sex
						+ "',maritalStatus='"
						+ maritalStatus
						+ "',permanentAddress='"
						+ permanentAddress
						+ "',temporatyAddress='"
						+ temporatyAddress
						+ "',wetherOption='"
						+ wetherOption
						+ "', WHETHER_FORM1_NOM_RECEIVED ='"
						+ form2Nomination
						+ "',fhname='"
						+ fhName
						+ "',setDateOfAnnuation='"
						+ dateOfAnnuation
						+ "',REFPENSIONNUMBER='"
						+ pensionNumber
						+ "',otherreason='"
						+ otherReason
						+ "',division='"
						+ division
						+ "',department='"
						+ department
						+ "',emailId='"
						+ emailId
						+ "',lastactive='"
						+ commonUtil.getCurrentDate("dd-MMM-yyyy")
						+ "',empnomineesharable='"
						+ empNomineeSharable
						+ "',userName='"
						+ bean.getUserName()
						+ "',fhflag='"
						+ bean.getFhFlag()
						+ "',region='"
						+ region.trim()
						+ "'  where empflag='Y' and PENSIONNO='"
						+ bean.getEmpSerialNo()
						+ "' and trim(employeename)='"
						+ empOldName.trim()
						+ "'  and region='"
						+ bean.getRegion() + "'";

				log.info("sql two " + sql3);

				st.addBatch(sql3);
				count1 = st.executeBatch();
				for (int i = 0; i < count1.length; i++) {
					count = count1[i];
				}

			}

		} catch (SQLException sqle) {
			log.printStackTrace(sqle);
			if (sqle.getErrorCode() == 00001) {
				throw new InvalidDataException("Pensionno Already Exist");
			}
		} catch (Exception e) {
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, null);
		}
		log.info("PersonalDAO:updatePensionMasterRevised leaving method ");
		return count;
	}

	public void insertEmployeeHistoryinfo(String Pensionno, String cpfacno,
			String empName, boolean flag, String empCode, String region,
			String currentDate, String computerName, String userName)
			throws Exception {

		EmpMasterBean historyBean = pensionDAO.editPensionMaster(Pensionno, "",
				true, "", region);
		Connection conn = null;
		Statement st = null;
		Pensionno = commonUtil.getSearchPFID(Pensionno);
		conn = commonDB.getConnection();

		String dbFormat = "";
		st = conn.createStatement();
		String sql = "insert into EMPLOYEE_EDITINFO_LOG(cpfacno,pensionnumber,employeeNo,employeeName,"
				+ "desegnation,emp_level,dateofbirth,dateofjoining,dateofseperation_reason,dateofseperation_date,"
				+ "whether_option_a,whether_option_b,whether_option_no,WHETHER_FORM1_NOM_RECEIVED,remarks,airportcode,"
				+ "sex,fhname,maritalStatus,permanentAddress,temporatyAddress,wetherOption,setDateOfAnnuation,otherreason,"
				+ "division,department,emailId,empnomineesharable,region,lastactive,computerName,userName)"
				+ " VALUES "
				+ "('"
				+ cpfacno.trim()
				+ "','"
				+ Pensionno.trim()
				+ "','"
				+ historyBean.getEmpNumber().trim()
				+ "','"
				+ historyBean.getEmpName().trim()
				+ "','"
				+ historyBean.getDesegnation().trim()
				+ "','"
				+ historyBean.getEmpLevel().trim()
				+ "','"
				+ historyBean.getDateofBirth().trim()
				+ "','"
				+ historyBean.getDateofJoining().trim()
				+ "','"
				+ historyBean.getSeperationReason().trim()
				+ "','"
				+ historyBean.getDateofSeperationDate().trim()
				+ "','"
				+ historyBean.getWhetherOptionA().trim()
				+ "','"
				+ historyBean.getWhetherOptionB().trim()
				+ "','"
				+ historyBean.getWhetherOptionNO().trim()
				+ "','"
				+ historyBean.getForm2Nomination().trim()
				+ "','"
				+ historyBean.getRemarks().trim()
				+ "','"
				+ historyBean.getStation().trim()
				+ "','"
				+ historyBean.getSex().trim()
				+ "','"
				+ historyBean.getFhName().trim()
				+ "','"
				+ historyBean.getMaritalStatus().trim()
				+ "','"
				+ historyBean.getPermanentAddress().trim()
				+ "','"
				+ historyBean.getTemporatyAddress().trim()
				+ "','"
				+ historyBean.getWetherOption().trim()
				+ "','"
				+ historyBean.getDateOfAnnuation().trim()
				+ "','"
				+ historyBean.getOtherReason().trim()
				+ "','"
				+ historyBean.getDivision().trim()
				+ "','"
				+ historyBean.getDepartment().trim()
				+ "','"
				+ historyBean.getEmailId().trim()
				+ "','"
				+ historyBean.getEmpNomineeSharable().trim()
				+ "','"
				+ historyBean.getRegion().trim()
				+ "','"
				+ currentDate.trim()
				+ "','" + computerName.trim() + "','" + userName.trim() + "')";

		log.info(sql);
		st.execute(sql);

	}

	private EmployeePersonalInfo loadPersonalInfoRevised(ResultSet rs)
			throws SQLException {

		EmployeePersonalInfo personal = new EmployeePersonalInfo();
		log.info("loadPersonalInfoRevised==============");

		if (rs.getString("cpfacno") != null) {
			personal.setCpfAccno(rs.getString("cpfacno"));
		} else {
			personal.setCpfAccno("---");
		}
		log
				.info("loadPersonalInfo=============="
						+ rs.getString("airportcode"));
		if (rs.getString("airportcode") != null) {
			personal.setAirportCode(rs.getString("airportcode"));
		} else {
			personal.setAirportCode("---");
		}
		if (rs.getString("desegnation") != null) {
			personal.setDesignation(rs.getString("desegnation"));
		} else {
			personal.setDesignation("---");
		}
		if (rs.getString("PENSIONNO") != null) {
			personal.setOldPensionNo(rs.getString("PENSIONNO"));
			log.info("setOldPensionNo " + personal.getOldPensionNo());
		} else {
			personal.setOldPensionNo("---");
		}
		if (rs.getString("PENSIONNO") != null) {
			personal.setPensionNo(commonUtil.leadingZeros(5, rs
					.getString("PENSIONNO")));
			log.info("pfno " + personal.getPensionNo());
		} else {
			personal.setPensionNo("---");
		}

		if (rs.getString("employeename") != null) {
			personal.setEmployeeName(rs.getString("employeename"));
		} else {
			personal.setEmployeeName("---");
		}
		if (rs.getString("EMPLOYEENO") != null) {
			personal.setEmployeeNumber(rs.getString("EMPLOYEENO"));
		} else {
			personal.setEmployeeNumber("---");
		}

		if (rs.getString("REFPENSIONNUMBER") != null) {
			personal.setRefPensionNumber(rs.getString("REFPENSIONNUMBER"));
		} else {
			personal.setRefPensionNumber("---");
		}
		if (rs.getString("dateofbirth") != null) {
			personal.setDateOfBirth(commonUtil.converDBToAppFormat(rs
					.getDate("dateofbirth")));
		} else {
			personal.setDateOfBirth("---");
		}
		if (rs.getString("DATEOFJOINING") != null) {
			personal.setDateOfJoining(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			personal.setDateOfJoining("---");
		}
		if (rs.getString("WETHEROPTION") != null) {
			personal.setWetherOption(rs.getString("WETHEROPTION"));
		} else {
			personal.setWetherOption("---");
		}
		if (rs.getString("freshoption") != null) {
			personal.setFreshPensionOption(rs.getString("freshoption"));
		} else {
			personal.setFreshPensionOption("---");
		}
		if (rs.getString("region") != null) {
			personal.setRegion(rs.getString("region"));
		} else {
			personal.setRegion("---");
		}
		if (rs.getString("MARITALSTATUS") != null) {
			personal.setMaritalStatus(rs.getString("MARITALSTATUS"));
		} else {
			personal.setMaritalStatus("---");
		}

		if ((personal.getCpfAccno().equals("")
				|| (personal.getEmployeeNumber().equals(""))
				|| (personal.getDateOfBirth().equals(""))
				|| (personal.getWetherOption().equals("")) || (personal
				.getEmployeeName().equals("")))) {
			personal.setRemarks("incomplete Data");
			// log.info("inside if");
		} else {
			personal.setRemarks("---");
			// log.info("inside else");
		}

		if (rs.getString("lastactive") != null) {
			personal.setLastActive(commonUtil.getDatetoString(rs
					.getDate("lastactive"), "dd-MMM-yyyy"));
		} else {
			personal.setLastActive("---");
		}

		if (rs.getString("GENDER") != null) {
			personal.setGender(rs.getString("GENDER"));
			log.info("gender " + rs.getString("GENDER").toString());
		} else {
			personal.setGender("---");
		}
		if (rs.getString("FHNAME") != null) {
			personal.setFhName(rs.getString("FHNAME"));
		} else {
			personal.setFhName("---");
		}
		if (rs.getString("OTHERREASON") != null) {
			personal.setOtherReason(rs.getString("OTHERREASON"));
		} else {
			personal.setOtherReason("---");
		}
		if (rs.getString("DATEOFSEPERATION_REASON") != null) {
			personal.setSeperationReason(rs
					.getString("DATEOFSEPERATION_REASON"));
		} else {
			personal.setSeperationReason("---");
		}
		if (rs.getString("DATEOFSEPERATION_DATE") != null) {
			personal.setSeperationDate(rs.getString("DATEOFSEPERATION_DATE"));
		} else {
			personal.setSeperationDate("---");
		}
		if (rs.getString("division") != null) {
			personal.setDivision(rs.getString("division"));
		} else {
			personal.setDivision("---");
		}
		if (rs.getString("status") != null) {
			personal.setStatus(rs.getString("status"));
		} else {
			personal.setStatus("---");
		}
		if (rs.getString("NEWEMPCODE") != null) {
			personal.setNewEmployeeNumber(rs.getString("NEWEMPCODE"));
		} else {
			personal.setNewEmployeeNumber("---");
		}
		if (rs.getString("uanno") != null) {
			personal.setUanno(rs.getString("uanno"));
		} else {
			personal.setUanno("---");
		}
		if (!personal.getDateOfBirth().equals("---")) {
			String personalPFID = commonDAO
					.getPFID(personal.getEmployeeName().trim(), personal
							.getDateOfBirth(), personal.getPensionNo());
			personal.setPfID(personalPFID);
		} else {
			personal.setPfID(personal.getPensionNo());
		}

		return personal;

	}

	public ArrayList searchPension1(EmployeePersonalInfo empPersonalBean,
			String formName) throws Exception {

		log.info("PersonalDAO :searchPension1() entering method");
		ArrayList empinfo = new ArrayList();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		EmployeePersonalInfo emp = null;
		sqlQuery = this.pensionlBuildQuery(empPersonalBean, formName);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("search query----" + sqlQuery);
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				emp = new EmployeePersonalInfo();
				if (rs.getString("PENSIONNO") != null) {
					emp.setPensionNo(rs.getString("PENSIONNO"));
				} else {
					emp.setPensionNo("---");
				}
				if (rs.getString("PENSION_PROCESSID") != null) {
					emp.setProcessID(rs.getString("PENSION_PROCESSID"));
				} else {
					emp.setProcessID("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					emp.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					emp.setEmployeeName("---");
				}
				if (rs.getString("DESEGNATION") != null) {
					emp.setDesignation(rs.getString("DESEGNATION"));
				} else {
					emp.setDesignation("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					emp.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					emp.setWetherOption("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					emp.setDateOfBirth(commonUtil.converDBToAppFormat(rs
							.getDate("dateofbirth")));
				} else {
					emp.setDateOfBirth("---");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					emp.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					emp.setAirportCode("---");
				}
				if (rs.getString("REGION") != null) {
					emp.setRegion(rs.getString("REGION"));
				} else {
					emp.setRegion("---");
				}
				if (rs.getString("verifiedby") != null) {
					emp.setVerifiedBy(rs.getString("verifiedby"));
				} else {
					emp.setVerifiedBy("");
				}
				empinfo.add(emp);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, null);
		}

		return empinfo;
	}

	/*
	 * public ArrayList searchPensionHrInit(EmployeePersonalInfo
	 * empPersonalBean) throws Exception {
	 * 
	 * log.info("PersonalDAO :searchPension1() entering method"); ArrayList
	 * empinfo = new ArrayList();
	 * 
	 * Connection con = null; Statement st=null;
	 * 
	 * ResultSet rs = null; String sqlQuery=""; EmployeePersonalInfo emp=null;
	 * 
	 * 
	 * //String pensionno = null; sqlQuery =
	 * this.pensionlBuildQuery(empPersonalBean); try {
	 * con=commonDB.getConnection(); st = con.createStatement();
	 * log.info("bbbbbbbbbbbbbbbbbbbbbbbb" + sqlQuery);
	 * rs=st.executeQuery(sqlQuery); while(rs.next()){ emp=new
	 * EmployeePersonalInfo(); if (rs.getString("PENSIONNO") != null) {
	 * emp.setPensionNo(rs.getString("PENSIONNO")); } else {
	 * emp.setPensionNo("---"); } if (rs.getString("PENSION_PROCESSID") != null) {
	 * emp.setProcessID(rs.getString("PENSION_PROCESSID")); } else {
	 * emp.setProcessID("---"); } if (rs.getString("EMPLOYEENAME") != null) {
	 * emp.setEmployeeName(rs.getString("EMPLOYEENAME")); } else {
	 * emp.setEmployeeName("---"); } if (rs.getString("DESEGNATION") != null) {
	 * emp.setDesignation(rs.getString("DESEGNATION")); } else {
	 * emp.setDesignation("---"); } if (rs.getString("WETHEROPTION") != null) {
	 * emp.setWetherOption(rs.getString("WETHEROPTION")); } else {
	 * emp.setWetherOption("---"); } if (rs.getString("DATEOFBIRTH") != null) {
	 * emp.setDateOfBirth(commonUtil.converDBToAppFormat(rs
	 * .getDate("dateofbirth"))); } else { emp.setDateOfBirth("---"); } if
	 * (rs.getString("AIRPORTCODE") != null) {
	 * emp.setAirportCode(rs.getString("AIRPORTCODE")); } else {
	 * emp.setAirportCode("---"); } if (rs.getString("REGION") != null) {
	 * emp.setRegion(rs.getString("REGION")); } else { emp.setRegion("---"); }
	 * 
	 * empinfo.add(emp); }
	 *  } catch (SQLException e) { log.printStackTrace(e); } catch (Exception e) {
	 * log.printStackTrace(e); } finally { commonDB.closeConnection(null, st,
	 * null); }
	 * 
	 * return empinfo; }
	 */
	public String pensionlBuildQuery(EmployeePersonalInfo bean, String formName) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "", sqlQuery = "", verifiedBy = "";
		int startIndex = 0, endIndex = 0;
		if (formName.equals("")) {
			verifiedBy = "i.verifiedby is null or i.verifiedby is not null ";
		} else if (formName.equals("chqHRInitial")) {
			verifiedBy = "i.verifiedby is null or i.verifiedby='HRINIT'";
		} else if (formName.equals("chqFinance")) {
			verifiedBy = "i.verifiedby='HRINIT' or i.verifiedby='HRINIT/HRFIN'";
		} else if (formName.equals("RPFCSubmission")) {
			verifiedBy = "i.verifiedby='HRINIT/HRFIN' or i.verifiedby='HRINIT/HRFIN/HRAPPROVE'";
		} else if (formName.equals("RPFCReturn")) {
			verifiedBy = "i.verifiedby='HRINIT/HRFIN/HRAPPROVE' or i.verifiedby='HRINIT/HRFIN/HRAPPROVE/RPFC'";
		}
		log.info("verfiedby ------" + verifiedBy);
		sqlQuery = "select i.pensionno,i.PENSION_PROCESSID,i.employeename,i.verifiedby,i.desegnation,i.wetheroption,i.dateofbirth,i.airportcode,i.region from employee_pension_process i where i.deleteflag='N' and ("
				+ verifiedBy + ") ";
		if (!bean.getWetherOption().equals("")) {
			 whereClause.append(" i.WETHEROPTION ='"
					  + bean.getWetherOption().toUpperCase()+"'");
			  whereClause.append(" AND ");
		}
		log.info("wetherOption : " + bean.getWetherOption());
		if (!bean.getPensionNo().equals("")) {
			whereClause.append(" i.PENSIONNO =" + bean.getPensionNo());
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfBirth().equals("")) {

			if (bean.getDateOfBirth().indexOf("-") != -1
					&& bean.getDateOfBirth().length() == 11) {
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MON-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("/") != -1) {

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MM/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else if (bean.getDateOfBirth().length() == 10
					&& bean.getDateOfBirth().indexOf("-") != -1) {

				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd-MM-yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			} else {
				whereClause.append(" TO_CHAR(DATEOFBIRTH,'dd/MON/yyyy') like '"
						+ bean.getDateOfBirth().toUpperCase() + "'");
			}
			whereClause.append(" AND ");
		}
		if (!bean.getDateOfJoining().equals("")) {
			if (bean.getDateOfJoining().indexOf("-") != -1
					&& bean.getDateOfJoining().length() == 11) {
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("/") != -1) {

				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd/MM/yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else if (bean.getDateOfJoining().length() == 10
					&& bean.getDateOfJoining().indexOf("-") != -1) {

				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MM-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");
			} else {
				whereClause
						.append(" TO_CHAR(DATEOFJOINING,'dd-MON-yyyy') like '"
								+ bean.getDateOfJoining().toUpperCase() + "'");

			}
			whereClause.append(" AND ");
		}
		if (!bean.getAirportCode().equals("")) {
			whereClause.append(" LOWER(AIRPORTCODE)='"
					+ bean.getAirportCode().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getEmployeeName().equals("")) {
			whereClause.append(" LOWER(EMPLOYEENAME) like'%"
					+ bean.getEmployeeName().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		/*
		 * if (!bean.getDesignation().equals("")) { whereClause.append("
		 * LOWER(DESEGNATION)='" + bean.getDesignation().toLowerCase() + "'");
		 * whereClause.append(" AND "); }
		 */
		if (!bean.getEmployeeNumber().equals("")) {
			whereClause
					.append(" EMPLOYEENO='" + bean.getEmployeeNumber() + "'");
			whereClause.append(" AND ");
		}
		if (!bean.getRegion().equals("")) {
			whereClause.append(" REGION ='" + bean.getRegion() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (bean.getAirportCode().equals("") && bean.getPensionNo().equals("")
				&& bean.getDateOfBirth().equals("")
				&& bean.getWetherOption().equals("")
				&& bean.getEmployeeName().equals("")
				&& bean.getEmployeeNumber().equals("")
				&& bean.getRegion().equals("")
				&& bean.getDateOfJoining().equals("")) {
			log.info("inside if");
		} else {
			log.info("inside else");
			query.append(" and ");
			query.append(this.sTokenFormat(whereClause));
		}
		log.info("where clause : " + whereClause);

		// String orderBy = " ORDER BY i."+sortingColumn;
		// query.append(orderBy);
		dynamicQuery = query.toString();
		return dynamicQuery;
	}

	public void updatePensionProcessCHQ(EmployeePersonalInfo personal,
			String formName) {
		Connection con = null;
		Statement stmt = null;
		String updateQury = "", insertTransQury = "", verifiedby = "", verifiedbyforprocestable = "", datecolumnName = "", remarkscolumnName = "";
		int trans_code = 0;
		try {
			if (formName.equals("chqHRInitial")) {
				verifiedby = "HRINIT";
				verifiedbyforprocestable = verifiedby;
				trans_code = 1;
				datecolumnName = "p.dateofcasesendtochq_fin";
				remarkscolumnName = "p.chqremarks_hr";

			} else if (formName.equals("chqFinance")) {
				verifiedby = "HRFIN";
				verifiedbyforprocestable = "HRINIT/HRFIN";
				trans_code = 2;
				datecolumnName = "p.dateofcasesendtochq_hr";
				remarkscolumnName = "p.chqremarks_fin";
			} else if (formName.equals("RPFCSubmission")) {
				verifiedby = "HRAPPROVE";
				verifiedbyforprocestable = "HRINIT/HRFIN/HRAPPROVE";
				trans_code = 3;
				datecolumnName = "p.DateofcasesendtoRPFc";
				remarkscolumnName = "p.RPFCREMARKS";
			} else if (formName.equals("RPFCReturn")) {
				verifiedby = "RPFC";
				verifiedbyforprocestable = "HRINIT/HRFIN/HRAPPROVE/RPFC";
				trans_code = 4;
				datecolumnName = "p.DATEOFCASE_RETURNEDBYRPFC";
				remarkscolumnName = "p.RETURNBYRPFCREMARKS";
			}
			con = commonDB.getConnection();
			stmt = con.createStatement();
			updateQury = "update employee_pension_process p set "
					+ datecolumnName + "=to_date('" + personal.getDatetoCHQHR()
					+ "','dd-Mon-yyyy') , " + remarkscolumnName + "='"
					+ personal.getRemarks() + "',  p.verifiedby='"
					+ verifiedbyforprocestable
					+ "',p.lastactive=sysdate where p.pensionno='"
					+ personal.getPensionNo() + "' and  p.pension_processid='"
					+ personal.getProcessID() + "' and p.deleteflag='N' ";
			insertTransQury = "insert into EMPLOYEE_PENSION_PROCESS_trans ( PENSIONNO, PENSION_PROCESSID, PENSION_PROCESS_tr, TRANSREMARKS, DATEOFSENT, verifiedby, lastactive, username, ipaddress , region, airportcode, trans_code) values  ('"
					+ personal.getPensionNo()
					+ "', '"
					+ personal.getProcessID()
					+ "', PENSION_PROCESS_tr.Nextval, '"
					+ personal.getRemarks()
					+ "', to_date('"
					+ personal.getDatetoCHQHR()
					+ "','dd-Mon-yyyy'), '"
					+ verifiedby
					+ "', sysdate, '"
					+ personal.getUserName()
					+ "', '"
					+ personal.getIpAddress()
					+ "', '"
					+ personal.getRegion()
					+ "', '"
					+ personal.getAirportCode()
					+ "', '" + trans_code + "')";
			log.info("update Qury====" + updateQury);
			log.info("insertTransQury Qury====" + insertTransQury);
			stmt.addBatch(updateQury);
			stmt.addBatch(insertTransQury);
			int[] result = stmt.executeBatch();
			log.info("result===" + result);

		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
	}

	public int updatePensionProcessInfo(EmployeePersonalInfo personal) {
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		Connection con = null;
		String updateQury = "";
		Statement updateStmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			con = commonDB.getConnection();
			updateStmt = con.createStatement();
			updateQury = "update EMPLOYEE_PENSION_PROCESS f set f.remarks='"
					+ personal.getRemarks() + "',f.pensiontype='"
					+ personal.getPensiontype() + "'," + "f.phonenumber='"
					+ personal.getPhoneNumber() + "',f.emailid='"
					+ personal.getEmailID() + "',f.dateofsenttochq='"
					+ personal.getDostochq() + "'" + " where f.pensionno='"
					+ personal.getPensionNo() + "' and f.pension_processid='"
					+ personal.getProcessID() + "' and f.deleteflag='N' ";
			log.info("update Qury : " + updateQury);
			result = updateStmt.executeUpdate(updateQury);
			log.info("reuslt:" + result);

		} catch (Exception e) {
			// TODO: handle exception
			log.printStackTrace(e);
		}
		return result;
	}

	public EmployeePersonalInfo editPensionProcessInfo(String p_id,
			String pensionno, String verifiedby) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "", status = "", trans_code = "";
		EmployeePersonalInfo empPerInfo = new EmployeePersonalInfo();
		if (verifiedby.equals("")) {
			selectQuery = "select p.pensionno, p.cpfacno, p.employeeno, p.PENSION_PROCESSID, p.employeename, p.desegnation, p.dateofbirth, p.dateofjoining, p.airportcode, p.gender,"
					+ " p.fhname, p.wetheroption, p.remarks, p.region, p.phonenumber, p.dateofsenttochq, p.dateofcasesendtochq_hr, p.dateofcasesendtochq_fin, p.emailid, p.pensiontype"
					+ " from employee_pension_process p where pension_processid = '"
					+ p_id
					+ "' and pensionno = '"
					+ pensionno
					+ "'  and deleteflag = 'N'";
		} else {
			if (verifiedby.equals("HRINIT")) {
				trans_code = "1";
			} else if (verifiedby.equals("HRINIT/HRFIN")) {
				trans_code = "2";
			} else if (verifiedby.equals("HRINIT/HRFIN/HRAPPROVE")) {
				trans_code = "3";
			} else if (verifiedby.equals("HRINIT/HRFIN/HRAPPROVE/RPFC")) {
				trans_code = "4";
			}
			selectQuery = "select p.pensionno,p.cpfacno,p.employeeno,p.PENSION_PROCESSID,p.employeename,p.desegnation,p.dateofbirth,p.dateofjoining,p.airportcode,p.gender,p.fhname,p.wetheroption,p.remarks,p.region,p.phonenumber,p.dateofsenttochq,"
					+ "p.dateofcasesendtochq_hr,p.dateofcasesendtochq_fin,p.emailid,p.pensiontype, p.chqremarks_hr, p.chqremarks_fin, p.rpfcremarks,p.returnbyrpfcremarks,p.Dateofcasesendtorpfc, p.dateofcase_returnedbyrpfc,p.verifiedby from employee_pension_process p, EMPLOYEE_PENSION_PROCESS_trans t  "
					+ "where p.pensionno = t.pensionno and  p.pension_processid = t.pension_processid and  p.verifiedby='"
					+ verifiedby
					+ "' and t.trans_code='"
					+ trans_code
					+ "' and p.pension_processid='"
					+ p_id
					+ "' and p.pensionno='"
					+ pensionno
					+ "' and p.deleteflag='N' ";

		}

		log.info("editPfidProcessInfo()==========" + selectQuery);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectQuery);
			while (rs.next()) {
				if (rs.getString("pensionno") != null) {
					empPerInfo.setPensionNo(rs.getString("pensionno"));
				} else {
					empPerInfo.setPensionNo("---");
				}
				if (rs.getString("pension_processid") != null) {
					empPerInfo.setProcessID(rs.getString("pension_processid"));
				} else {
					empPerInfo.setProcessID("---");
				}
				if (rs.getString("CPFACNO") != null) {
					empPerInfo.setCpfAccno(rs.getString("CPFACNO"));
				} else {
					empPerInfo.setCpfAccno("---");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					empPerInfo.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				} else {
					empPerInfo.setEmployeeNumber("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empPerInfo.setEmployeeName(rs.getString("EMPLOYEENAME"));
				} else {
					empPerInfo.setEmployeeName("---");
				}
				log.info("empname" + empPerInfo.getEmployeeName() + " "
						+ rs.getString("EMPLOYEENAME"));
				if (rs.getString("FHNAME") != null) {
					empPerInfo.setFhName(rs.getString("FHNAME"));
				} else {
					empPerInfo.setFhName("---");
				}
				if (rs.getString("WETHEROPTION") != null) {
					empPerInfo.setWetherOption(rs.getString("WETHEROPTION"));
				} else {
					empPerInfo.setWetherOption("---");
				}
				if (rs.getString("GENDER") != null) {
					empPerInfo.setGender(rs.getString("GENDER"));
				} else {
					empPerInfo.setGender("---");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					empPerInfo.setAirportCode(rs.getString("AIRPORTCODE"));
				} else {
					empPerInfo.setAirportCode("---");
				}
				if (rs.getString("REGION") != null) {
					empPerInfo.setRegion(rs.getString("REGION"));
				} else {
					empPerInfo.setRegion("---");
				}
				if (rs.getString("desegnation") != null) {
					empPerInfo.setDesignation(rs.getString("desegnation"));
				} else {
					empPerInfo.setDesignation("---");
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					empPerInfo.setDateOfBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					empPerInfo.setDateOfBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empPerInfo.setDateOfJoining(CommonUtil
							.converDBToAppFormat(rs.getDate("DATEOFJOINING")));
				} else {
					empPerInfo.setDateOfJoining("---");
				}

				if (rs.getString("REMARKS") != null) {
					empPerInfo.setRemarks(rs.getString("REMARKS"));
				}
				if (rs.getString("PHONENUMBER") != null) {
					empPerInfo.setPhoneNumber(rs.getString("PHONENUMBER"));
				} else {
					empPerInfo.setPhoneNumber("---");
				}
				if (rs.getString("DATEOFSENTTOCHQ") != null) {
					empPerInfo.setDostochq(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSENTTOCHQ")));
				} else {
					empPerInfo.setDostochq("---");
				}
				if (rs.getString("DATEOFCASESENDTOCHQ_HR") != null) {
					empPerInfo.setDatetoCHQHR(commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFCASESENDTOCHQ_HR")));
				} else {
					empPerInfo.setDatetoCHQHR("---");
				}
				if (rs.getString("DATEOFCASESENDTOCHQ_FIN") != null) {
					empPerInfo.setDatetoCHQFIN(commonUtil
							.converDBToAppFormat(rs
									.getDate("DATEOFCASESENDTOCHQ_FIN")));
				} else {
					empPerInfo.setDatetoCHQFIN("---");
				}

				if (rs.getString("EMAILID") != null) {
					empPerInfo.setEmailID(rs.getString("EMAILID"));
				} else {
					empPerInfo.setEmailID("---");
				}
				if (rs.getString("PENSIONTYPE") != null) {
					empPerInfo.setPensiontype(rs.getString("PENSIONTYPE"));
				} else {
					empPerInfo.setPensiontype("---");
				}
				if (!verifiedby.equals("")) {
					if (rs.getString("VERIFIEDBY") != null) {
						empPerInfo.setVerifiedBy(rs.getString("VERIFIEDBY"));
					}
					if (rs.getString("chqremarks_hr") != null) {
						empPerInfo.setChqHrRemarks(rs
								.getString("chqremarks_hr"));
					}
					if (rs.getString("chqremarks_fin") != null) {
						empPerInfo.setChqFinRemarks(rs
								.getString("chqremarks_fin"));
					}
					if (rs.getString("RPFCREMARKS") != null) {
						empPerInfo.setRpfcSubmisionRemarks(rs
								.getString("RPFCREMARKS"));
					}
					if (rs.getString("RETURNBYRPFCREMARKS") != null) {
						empPerInfo.setRpfcReturnRemarks(rs
								.getString("RETURNBYRPFCREMARKS"));
					}
					if (rs.getString("DATEOFCASE_RETURNEDBYRPFC") != null) {
						empPerInfo.setRpfcReturnDate(commonUtil
								.converDBToAppFormat(rs
										.getDate("DATEOFCASE_RETURNEDBYRPFC")));
					} else {
						empPerInfo.setRpfcReturnDate("---");
					}
					if (rs.getString("DATEOFCASESENDTORPFC") != null) {
						empPerInfo.setRpfcSubmissionDate(commonUtil
								.converDBToAppFormat(rs
										.getDate("DATEOFCASESENDTORPFC")));
					} else {
						empPerInfo.setRpfcSubmissionDate("---");
					}

				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs.close();
				commonDB.closeConnection(con, st, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return empPerInfo;
	}
	public String buildFreshOptionQury(String frmName,String region,String airportCode){
		String sqlQury="" , dynamicQury="",freshOption="";
		log.info("frmName::"+frmName);
		StringBuffer whereClause = new StringBuffer();		
		StringBuffer qury=new StringBuffer();
		if(frmName.equals("Report A")) {sqlQury = " select * from (select epi.pensionno,epi.cpfacno,epi.uanno,epi.employeeno,epi.employeename,epi.fhname, epi.desegnation,to_char(epi.dateofbirth, 'dd-Mon-yyyy') as dateofbirth,to_char(epi.dateofjoining, 'dd-Mon-yyyy') as dateofjoining, epi.wetheroption,epi.airportcode,epi.region from employee_personal_info epi where epi.dateofbirth > to_date('01-Sep-1956', 'dd-Mon-yyyy') and epi.dateofseperation_date is null and epi.dateofseperation_reason is null and epi.wetheroption = 'A')i,(select f.pensionno as penno,f.freshpensionoption from employee_pension_freshoption f where f.deleteflag='N') j  where  j.penno(+)=i.pensionno";
}else {
	if (frmName.equals("Report A-B")) {
		freshOption="B";			
	}else if(frmName.equals("Report A-A")) {
		freshOption="A";			
	}
	log.info("freshOption =="+freshOption);
	sqlQury = " select epi.pensionno,epi.cpfno,epi.uanno,epi.employeeno,epi.employeename,epi.designation,epi.fhname, to_char(epi.dateofbirth,'dd-Mon-yyyy') as dateofbirth ," +
			"to_char(epi.dateofjoining,'dd-Mon-yyyy') as dateofjoining ,epi.airportcode,epi.region,epi.username,to_char(epi.app_date,'dd-Mon-yyyy') as app_date from " +
			"employee_pension_freshoption epi,employee_personal_info i where epi.pensionno=i.pensionno and deleteflag='N' and i.dateofseperation_date is null and i.dateofseperation_reason is null and   epi.freshpensionoption='"+freshOption+"'";
	
	}
		qury.append(sqlQury);		
		if(!region.equals("NO-SELECT")) {
			whereClause.append(" i.region='"+region+"'");
			whereClause.append(" and ");
		}
		if(!airportCode.equals("NO-SELECT")){
			whereClause.append(" i.airportcode='"+airportCode+"'");
			whereClause.append(" and ");
		}
		if(region.equals("NO-SELECT") && airportCode.equals("NO-SELECT")){
			
		}else {
			qury.append(" and ");
			qury.append(this.sTokenFormat(whereClause));			
		}	
		qury.append(" order by i.pensionno");
		dynamicQury = qury.toString();
		log.info("where clause====="+whereClause+"dynamicQury :"+dynamicQury);
		
		return dynamicQury;
	}
	 public ArrayList freshSummaryReport() {
			// String
			// pensionno="",employeename="",cpfAccno="",dateOfBirth="",dateofjoining="",wetheroption="",selectedMonth="",selectedYear="";

			ArrayList list = null;
			
			EmployeePersonalInfo info = null;
			String sqlQry = "";

			Connection con = null;
			Statement st = null;
			ResultSet rs = null;

			sqlQry = " select region, sum(cnt1) as optiona, sum(cnt2) as a2a, sum(cnt3) as a2b from (select epi.region, count(pensionno) as cnt1, 0 as cnt2, 0 as cnt3" +
	          " from employee_personal_info epi "+
	         "where epi.dateofbirth > to_date('01-Sep-1956', 'dd-Mon-yyyy')"+
	           "and epi.dateofseperation_date is null " +
	           "and epi.dateofseperation_reason is null " +
	           "and epi.wetheroption = 'A'"+
	         "group by region " +
	        "union all " +
	        "select i.region as region, 0 as cnt1, count(n.pensionno) as cnt2, 0 as cnt3 " +
	        
	          "from employee_pension_freshoption n,employee_personal_info i "+ 
	         "where n.freshpensionoption = 'A' and n.pensionno=i.pensionno and n.deleteflag='N' and  i.dateofseperation_date is null and i.dateofseperation_reason is null " +
	         "group by i.region " +
	        "union all " +
	        "select i.region as region, 0 as cnt1, 0 as cnt2, count(n.pensionno) as cnt3 " +
	        
	          "from employee_pension_freshoption n,employee_personal_info i " +
	         "where n.freshpensionoption = 'B' and n.pensionno=i.pensionno and n.deleteflag='N' and  i.dateofseperation_date is null and i.dateofseperation_reason is null " +
	         "group by i.region) " +
	 "group by region ";


			log.info("Summary Qry======" + sqlQry);
			try {
				con = commonDB.getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlQry);

				list = new ArrayList();
				while (rs.next()) {
					info=new EmployeePersonalInfo();

					if (rs.getString("region") != null) {
						info.setRegion(rs.getString("region"));
					}
					if (rs.getString("optiona") != null) {
						info.setOptiona(rs.getString("optiona"));
					}
					if (rs.getString("a2a") != null) {
						info.setA2a(rs.getString("a2a"));
					}
					if (rs.getString("a2b") != null) {
						info.setA2b(rs.getString("a2b"));
					}
			list.add(info);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return list;
		}


}
