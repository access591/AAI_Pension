package aims.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import aims.bean.DesignationBean;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinalSettlementBean;
import aims.bean.TempPensionTransBean;
import aims.bean.epfforms.AaiEpfForm11Bean;
import aims.common.CommonUtil;
import aims.common.Constants;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.common.StringUtility;
/* 
##########################################
#Date					Developed by			Issue description
#28-Nov-2012			Radha					For proportionate calc of -ve emoluments for Option B Case as per Sehgal inpensionCalculation()
#23-Sep-2012			Radha					SEPERATION+ 3Years is ON  Feb 29  so CHKFRZNSEPERATION cmming as 0.9 By Sehgal Ex PFID 5007 in getEmployeePFInfoPrinting
#17-Sep-2012			Radha 					SEPERATION+ 3Years is ON  Mar  so CHKFRZNSEPERATION cmming as 0.9 By Sehgal Ex PFID 10524 in getEmployeePFInfoPrinting()
#22-MAy-2012            Radha                   Making exact Dataofseperation+ 36 Years for calculation Intrst on OB in pf card changes done in  buildQueryEmpPFTransInfoPrinting(), buildQuerygetEmployeePFInfoPrinting()
#14-Apr-2012            Suresh                  Modiifed getMonths and getfINALASETTLMENT due incositiency of o.b and closing balance in pfcard
#02-Apr-2012            Suresh                  Resolved the issue for interest calculation for old months.getNoOfMonthsForPFID
#21-Mar-2012			Suresh	                Add new condition for Seperation Date+ 3 Years and added constants for PFCards Years.
#10-Mar-2012			Suresh					Changes in PC Calc(pensionCalculation)
#24-Feb-2012			Radha					Correcting the changes due to seperation of methods  from FinancialReportDAO to CommonDAO
#########################################
*/
public class CommonDAO {
	Log log = new Log(CommonDAO.class);
	DBUtils commonDB = new DBUtils();
	CommonUtil commonUtil = new CommonUtil();

	public int checkEmployeeInfo(Connection con, String empCode,
			String cpfaccno, String employeeName, String region) {
		log.info("FinanceDAO :checkEmployeeInfo() entering method ");
		log.info("FinanceDAO :checkEmployeeInfo() cpfaccno " + cpfaccno
				+ "empCode " + empCode);
		int foundEmpFlag = 0;
		String query = "";
		Statement st = null;
		ResultSet rs = null;
		// for Eastern Region Data.
		/*
		 * if (!cpfaccno.equals("") && !employeeName.equals("")) { query =
		 * "select count(EMPLOYEENAME) as EMPLOYEENAME from employee_info where
		 * CPFACNO='" + cpfaccno + "' and employeeName='" + employeeName + "'"; }
		 * else if (!cpfaccno.equals("") && employeeName.equals("")) { query =
		 * "select count(cpfacno) as cpfacno from employee_info where CPFACNO='" +
		 * cpfaccno + "'"; } else
		 */
		/*if (!cpfaccno.equals("") && !empCode.equals("")) {
			query = "select count(*) as count from employee_info where  cpfacno='"
					+ cpfaccno
					+ "' and employeeno='"
					+ empCode
					+ "' and region='" + region + "' ";
		} else*/ if (!cpfaccno.equals("")) {
			query = "select count(*) as count from employee_info where   cpfacno='"
					+ cpfaccno.trim() + "' and region='" + region.trim() + "'";
                         //   " AND airportcode='IGI IAD'";
		} else {
			query = "select count(*) as count from employee_info where employeeno='"
					+ empCode + "' and region='" + region + "'" ;
                          //  " AND airportcode='IGI IAD' ";
		}

		// For Southern Region
		/*
		 * if (!cpfaccno.equals("") && (!empCode.equals(""))) { query = "select
		 * EMPLOYEENAME as EMPLOYEENAME from employee_info where CPFACNO='" +
		 * cpfaccno + "' and EMPLOYEENO='" + empCode + "' and employeeName='" +
		 * employeeName + "'"; } else
		 * 
		 * if (!cpfaccno.equals("") ) { query = "select EMPLOYEENAME as
		 * EMPLOYEENAME from employee_info where CPFACNO='" + cpfaccno + "' and
		 * employeeName='" + employeeName + "'"; } else { query = "select
		 * EMPLOYEENAME as EMPLOYEENAME from employee_info where EMPLOYEENO='" +
		 * empCode + "' and employeeName='" + employeeName + "'"; }
		 */

		log.info("query is " + query);
		try {

			st = con.createStatement();
			rs = st.executeQuery(query);

			if (rs.next()) { 
				if (rs.getString("count") != null) {
					foundEmpFlag = rs.getInt("count");
				}
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
		log.info("FinanceDAO :checkEmployeeInfo() leaving method"
				+ foundEmpFlag);
		return foundEmpFlag;
	}
	public String checkEmployeeInfo1(Connection con, String empCode,
			String cpfaccno, String employeeName, String region) {
		log.info("FinanceDAO :checkEmployeeInfo() entering method ");
		log.info("FinanceDAO :checkEmployeeInfo() cpfaccno " + cpfaccno
				+ "empCode " + empCode);
		String foundEmpFlag ="";
		String query = "";
		Statement st = null;
		ResultSet rs = null;
		 if (!cpfaccno.equals("")) {
			query = "select cpfacno  from employee_info where   cpfacno='"
					+ cpfaccno.trim() + "' and region='" + region.trim() + "' and cpfacno is not null";
                       
		} else {
			query = "select cpfacno  from employee_info where employeeno='"
					+ empCode + "' and region='" + region + "' and cpfacno is not null" ;
                         
		}
	log.info("query is " + query);
		try {

			st = con.createStatement();
			log.info("st "+st);
			rs = st.executeQuery(query);

			if (rs.next()) {
			//	log.info("cpfaccno "+rs.getString(1));
					foundEmpFlag = rs.getString(1);
				
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
		log.info("FinanceDAO :checkEmployeeInfo() leaving method"
				+ foundEmpFlag);
		return foundEmpFlag;
	}
	public String checkFinanceDuplicate(Connection con, String fromDate,
			String cpfaccno, String employeeNo, String region) {
		log.info("CommonDAO :checkPensionDuplicate() entering method ");
		String foundEmpFlag = "";
		Statement st = null;

		ResultSet rs = null;
		System.out.println("fromDate" + fromDate + "employeeNo" + employeeNo);
		
		CommonUtil commonUtil = new CommonUtil();
		try {
			String transMonthYear = commonUtil.converDBToAppFormat(fromDate
					.trim(), "dd-MMM-yy", "-MMM-yy");
			 String query = "select employeeNo as COLUMNNM from employee_Pension_validate where to_char(monthyear,'dd-Mon-yy') like '%"+transMonthYear+"' and employeeno='"+ employeeNo+ "' and region='"+ region + "'";
		
			log.info("query is " + query);

			// con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			if (rs.next()) {
				if (rs.getString("COLUMNNM") != null) {
					foundEmpFlag = rs.getString("COLUMNNM");
				}
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
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
		log.info("CommonDAO :checkPensionDuplicate() leaving method");
		return foundEmpFlag;
	}
	
	public int checkRecordCount(Connection con, String fromDate,
			String cpfaccno, String employeeNo, String region) {
		log.info("CommonDAO :checkRecordCount() entering method ");
		int foundEmpFlag = 0;
		Statement st = null;

		ResultSet rs = null;
		System.out.println("fromDate" + fromDate + "employeeNo" + employeeNo);
		
		CommonUtil commonUtil = new CommonUtil();
		try {
			String transMonthYear = commonUtil.converDBToAppFormat(fromDate
					.trim(), "dd-MMM-yy", "-MMM-yy");
			 String query = "select *  from employee_Pension_validate where to_char(monthyear,'dd-Mon-yy') like '%"+transMonthYear+"' and cpfaccno='"+ cpfaccno+ "' and region='"+ region + "'";
		
			log.info("query is " + query);

			// con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				bean.setBasic(rs.getString("basic"));
				bean.setSpecialBasic(rs.getString("SPECIALBASIC"));
				bean.setEmoluments(rs.getString("EMOLUMENTS"));
				bean.setDailyAllowance(rs.getString("DAILYALLOWANCE"));
				String emoluments = String.valueOf(Float.parseFloat(bean
						.getBasic().trim())
						+ Float.parseFloat(bean.getDailyAllowance())
						+ Float.parseFloat(bean.getSpecialBasic()));
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
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
		log.info("CommonDAO :checkRecordCount() leaving method");
		return foundEmpFlag;
	}
	public String getPensionNumber(String empName, String dateofBirth,
			String cpf,boolean flag) {
		log.info("CommonDAO:getPensionNumber() entering method");
		log.info("CommonDAO:getPensionNumber() dateofBirth" + dateofBirth
				+ "empName" + empName);
		// TODO Auto-generated method stub
		String finalName = "", fname = "";
		SimpleDateFormat fromDate = null;
		int endIndxName = 0;
		String quats[] = { "Mrs.", "DR." };
		String tempName = "", convertedDt = "";
	//	String uniquenumber = validateCPFAccno(cpf.toCharArray());
		try {
			if (dateofBirth.indexOf("-") != -1) {
				fromDate = new SimpleDateFormat("dd-MMM-yyyy");
			} else {
				fromDate = new SimpleDateFormat("dd/MMM/yyyy");
			}
			SimpleDateFormat toDate = new SimpleDateFormat("ddMMyy");
			convertedDt = toDate.format(fromDate.parse(dateofBirth));
			System.out.println(" convertedDt " + convertedDt);
			int startIndxName = 0;
			endIndxName = 1;
			for (int i = 0; i < quats.length; i++) {
				if (empName.indexOf(quats[i]) != -1) {
					tempName = empName.replaceAll(quats[i], "").trim();
					// tempName=empName.substring(index+1,empName.length());
					empName = tempName;
				}
			}

			finalName = empName.substring(startIndxName, endIndxName);
			finalName = empName.substring(startIndxName, endIndxName);
			if (empName.indexOf(" ") != -1) {
				endIndxName = empName.lastIndexOf(" ");
				finalName = finalName + empName.substring(endIndxName).trim();
			} else if (empName.indexOf(".") != -1) {
				endIndxName = empName.lastIndexOf(".");
				finalName = finalName
						+ empName.substring(endIndxName + 1, empName.length())
								.trim();
			} else {
				finalName = empName.substring(0, 2);
			}
			log.info("final name is" + finalName);
			char[] cArray = finalName.toCharArray();
			fname = String.valueOf(cArray[0]);
			fname = fname + String.valueOf(cArray[1]);
			log.info(empName + " Short Name of " + fname);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			log.info("Exception is " + e);
		}
		log.info("CommonDAO:getPensionNumber() leaving method");
		if(flag==true)
		return convertedDt + fname + cpf;
		else return convertedDt + fname;
	}
	public String getPensionNumber(String empName, String dateofBirth,
			String cpf, String region) {
		log.info("CommonDAO:getPensionNumber() entering method");
		log.info("CommonDAO:getPensionNumber() dateofBirth" + dateofBirth
				+ "empName" + empName);
		// TODO Auto-generated method stub
		String finalName = "", fname = "";
		SimpleDateFormat fromDate = null;
		int endIndxName = 0;
		String quats[] = { "Mrs.", "DR." };
		String tempName = "", convertedDt = "";
		String uniquenumber = validateAlphaCPFAccno(cpf.toCharArray());

		try {
			if (dateofBirth.indexOf("-") != -1) {
				fromDate = new SimpleDateFormat("dd-MMM-yyyy");
			} else {
				fromDate = new SimpleDateFormat("dd/MMM/yyyy");
			}
			SimpleDateFormat toDate = new SimpleDateFormat("ddMMyy");
			convertedDt = toDate.format(fromDate.parse(dateofBirth));
			System.out.println(" convertedDt " + convertedDt);

			int startIndxName = 0, index = 0;
			endIndxName = 1;
			for (int i = 0; i < quats.length; i++) {
				if (empName.indexOf(quats[i]) != -1) {
					tempName = empName.replaceAll(quats[i], "").trim();
					// tempName=empName.substring(index+1,empName.length());
					empName = tempName;
				}
			}
			finalName = empName.substring(startIndxName, endIndxName);
			finalName = empName.substring(startIndxName, endIndxName);
			if (empName.indexOf(" ") != -1) {
				endIndxName = empName.lastIndexOf(" ");
				finalName = finalName + empName.substring(endIndxName).trim();
			} else if (empName.indexOf(".") != -1) {
				endIndxName = empName.lastIndexOf(".");
				finalName = finalName
						+ empName.substring(endIndxName + 1, empName.length())
								.trim();
			} else {
				finalName = empName.substring(0, 2);
			}
			log.info("final name is" + finalName);
			char[] cArray = finalName.toCharArray();
			fname = String.valueOf(cArray[0]);
			fname = fname + String.valueOf(cArray[1]);
			log.info(empName + " Short Name of " + fname);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			log.info("Exception is " + e);

		}
		log.info("CommonDAO:getPensionNumber() leaving method");
		return convertedDt + fname + uniquenumber;

	}
	public String getPensionNumberError(String empName, String dateofBirth,
			String cpf, String tempPensionNumber) {
		log.info("CommonDAO:getPensionNumber() entering method");
		log.info("CommonDAO:getPensionNumber() dateofBirth" + dateofBirth
				+ "empName" + empName);
		// TODO Auto-generated method stub
		String finalName = "", fname = "", finalString = "", tempPensionSubString = "";
		String finalPensionNumber = "";
		SimpleDateFormat fromDate = null;
		int endIndxName = 0, tempPensionNoSize = 0;
		String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt.", "SHRI",
				"MISS." };
		String tempName = "", PensionNumber = "", convertedDt = "";
		String uniquenumber = validateCPFAccno(cpf.toCharArray());
		try {
			if (dateofBirth.indexOf("-") != -1) {
				fromDate = new SimpleDateFormat("dd-MMM-yyyy");
			} else {
				fromDate = new SimpleDateFormat("dd/MMM/yyyy");
			}
			SimpleDateFormat toDate = new SimpleDateFormat("ddMMyy");
			convertedDt = toDate.format(fromDate.parse(dateofBirth));
			System.out.println(" convertedDt " + convertedDt);

			int startIndxName = 0, index = 0;
			endIndxName = 1;
			for (int i = 0; i < quats.length; i++) {
				if (empName.indexOf(quats[i]) != -1) {
					tempName = empName.replaceAll(quats[i], "").trim();
					// tempName=empName.substring(index+1,empName.length());
					empName = tempName;
				}
			}
			String delimiters = " ";
			System.out
					.println("Oringial String indexof" + empName.indexOf(" "));
			if (empName.indexOf(" ") != -1) {
				finalString = StringUtility.replace(
						empName.trim().toCharArray(), delimiters.toCharArray(),
						".").toString();
			} else {
				finalString = empName;
			}
			System.out.println("Oringial String" + empName
					+ " Modified String " + finalString);
			StringTokenizer st = new StringTokenizer(finalString, ".");
			int count = 0, i = 0;

			int stCount = st.countTokens();
			String[] finalStringArray = new String[stCount];
			// && && count<=st.countTokens()-1st.countTokens()-1
			while (st.hasMoreElements()) {
				finalStringArray[i] = st.nextToken();
				i++;
			}
			System.out.println("Length==tempPensionNumber==="
					+ finalStringArray.length + "Temp Pension Number is "
					+ tempPensionNumber);
			tempPensionNoSize = tempPensionNumber.length();
			System.out.println("tempPensionNoSize=====" + tempPensionNoSize);
			for (int j = 0; j < finalStringArray.length; j++) {
				if (!tempPensionNumber.equals("") && tempPensionNoSize != 0) {
					if (tempPensionNumber.substring(0, 1).equals(
							finalStringArray[j].substring(0, 1))) {
						System.out.println("finalStringArray[j].length()====="
								+ finalStringArray[j].length());
						if (finalStringArray[j].length() <= 1) {
							tempPensionSubString = tempPensionNumber.substring(
									1, 2);
						} else {
							tempPensionSubString = tempPensionNumber.substring(
									0, 1);

						}
					}
				}

			}

			System.out.println("tempPensionSubString====="
					+ tempPensionSubString);
			int startIndex = 0;
			for (int j = 0; j < finalStringArray.length; j++) {
				System.out.println("Length=====" + finalStringArray[j].length()
						+ "Value is" + finalStringArray[j]);
				if (finalStringArray[j].length() > 1) {
					if (!tempPensionNumber.equals("") && tempPensionNoSize != 0) {
						System.out
								.println("Check Pension number===tempPensionSubString=="
										+ tempPensionSubString
										+ "Value is"
										+ finalStringArray[j].substring(0, 1)
										+ "Temp Pension Size========="
										+ tempPensionNoSize);
						if (tempPensionSubString.equals(finalStringArray[j]
								.substring(0, 1))) {
							if ((tempPensionNoSize - 1) == 1) {
								fname = fname
										+ finalStringArray[j].substring(0,
												tempPensionNoSize + 1);
							} else {
								if (tempPensionNoSize + 1 != finalStringArray[j]
										.length() - 1) {
									fname = fname
											+ finalStringArray[j].substring(0,
													tempPensionNoSize + 1);
								} else {
									fname = fname
											+ finalStringArray[j].substring(0,
													tempPensionNoSize);
								}
							}
							System.out
									.println("Check Pension number===tempPensionSubString==fname===="
											+ fname);
						} else {
							fname = fname + finalStringArray[j].substring(0, 1);
						}

					} else {
						fname = fname + finalStringArray[j].substring(0, 2);
					}

				} else if (finalStringArray[j].length() <= 1) {
					fname = fname + finalStringArray[j];
				}
			}
			finalPensionNumber = convertedDt + fname + uniquenumber;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		}
		log.info("CommonDAO:getPensionNumber() leaving method");
		return finalPensionNumber;

	}
	
	public String checkPensionNumber(String pensionnumber, String cpfaccno,
			String employeeno, String employeeName, String region,
			String dateOfBirth) {
		log.info("CommonDAO :checkPensionNumber() Enter method==============");
		String pensionNumber = "", tempPensionNumber = "";
		boolean tempPensionflag = false;
		String[] employeeList = new String[3];
		employeeList = getPensionNumberFromDB(pensionnumber);
		String dbCPFACCno = "", dbEmployeeNo = "", dbEmployeeName = "", dbRegion = "", chkFinalPensionNumber = "";
		if (employeeList[0] != null) {
			dbCPFACCno = employeeList[0].toString();
		}
		if (employeeList[1] != null) {
			dbEmployeeName = employeeList[1].toString();
		}
		if (employeeList[2] != null) {
			dbEmployeeNo = employeeList[2].toString();
		}
		if (employeeList[3] != null) {
			dbRegion = employeeList[3].toString();
		}
		System.out.println("Size is (checkPensionNumber) "
				+ employeeList.length);
		System.out.println("CPFACCNO From DB " + employeeList[0] + "From Form"
				+ cpfaccno);
		System.out.println("Employee Name From DB" + employeeList[1]
				+ "From Form" + employeeName);
		System.out.println("Employee No From DB" + employeeList[2]
				+ "From Form" + employeeno);
		System.out.println("Region From DB" + employeeList[3] + "From Form"
				+ region);
		String delimeters = " ";
		dbEmployeeName = StringUtility.replace(dbEmployeeName.toCharArray(),
				delimeters.toCharArray(), ".").toString();
		employeeName = StringUtility.replace(employeeName.toCharArray(),
				delimeters.toCharArray(), ".").toString();
		if (dbCPFACCno.equals(cpfaccno) && dbEmployeeName.equals(employeeName)
				&& dbEmployeeNo.equals(employeeno) && dbRegion.equals(region)) {
			log.info("Pnesion number both are equal");
			pensionNumber = pensionnumber;
		} else if (dbCPFACCno != null && dbEmployeeName != null
				&& dbEmployeeNo != null && !dbRegion.equals("")
				&& dbRegion.equals(region)) {
			log
					.info("Pnesion number db values are avaliable the record is created new both are equal");
			pensionNumber = pensionnumber;
		} else {
			if (!pensionnumber.equals("") && !dbRegion.equals("")) {
				tempPensionNumber = pensionnumber.substring(6, pensionnumber
						.length());
				String tempCpfacno = employeeList[0].toString();
				tempPensionNumber = StringUtility.replace(
						tempPensionNumber.toCharArray(),
						tempCpfacno.toCharArray(), "").toString();
				tempPensionflag = true;
			}
			System.out.println("tempPensionNumber=============="
					+ tempPensionNumber);
			pensionNumber = this.getPensionNumberError(employeeName, dateOfBirth,
					cpfaccno, tempPensionNumber);
			if (checkPensionNumberFromDB(pensionNumber) == true) {

				pensionNumber = checkPensionNumber(pensionNumber, cpfaccno,
						employeeno, employeeName, region.trim(), dateOfBirth);
			}
		}
		System.out.println("checkPensionNumber==============" + pensionNumber);
		log.info("CommonDAO :checkPensionNumber() Exit method==============");
		return pensionNumber;
	}

	private String[] getPensionNumberFromDB(String pensionnumber) {
		int count = 0;
		String[] employeeList = new String[4];
		Statement st = null;
		Connection con = null;
		ResultSet rs = null;
		System.out.println("pensionnumber==============" + pensionnumber);
		String query = "select CPFACNO,EMPLOYEENAME,EMPLOYEENO,REGION from employee_info where  pensionnumber='"
				+ pensionnumber + "' and pensionnumber is not null";
		log.info("query is " + query);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				if (rs.getString("CPFACNO") != null) {
					employeeList[0] = rs.getString("CPFACNO");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					employeeList[1] = rs.getString("EMPLOYEENAME");
				}
				if (rs.getString("EMPLOYEENO") != null) {
					employeeList[2] = rs.getString("EMPLOYEENO");
				}
				if (rs.getString("REGION") != null) {
					employeeList[3] = rs.getString("REGION");
				} else {
					employeeList[3] = "";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return employeeList;
	}
	private boolean checkPensionNumberFromDB(String pensionnumber) {
		int count = 0;
		String[] employeeList = new String[4];
		boolean flag = false;
		Statement st = null;
		Connection con = null;
		ResultSet rs = null;
		System.out.println("pensionnumber==============" + pensionnumber);
		String query = "select pensionnumber from employee_info where  pensionnumber='"
				+ pensionnumber + "' and pensionnumber is not null";
		log.info("query is " + query);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(query);

			while (rs.next()) {
				if (rs.getString("pensionnumber") != null) {
					flag = true;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return flag;
	}

	private String validateCPFAccno(char[] frmtDt) {
		StringBuffer buff = new StringBuffer();
		StringBuffer digitBuff = new StringBuffer();
		for (int i = 0; i < frmtDt.length; i++) {
			char c = frmtDt[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				buff.append(c);
			} else if (c >= '0' && c <= '9') {
				digitBuff.append(c);
			}
		}
		String validDt = digitBuff.toString();
		log.info("cpfacno " + validDt);
		return validDt;
	}
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
    public ArrayList getDepartmentList() {
        log.info("CommonDAO :getDepartmentList() Entering Method ");
        Connection con = null;
        Statement st =null;
        ArrayList departmentList = new ArrayList();
        ResultSet rs = null;
        String departmentName = "";
        try {
            con = commonDB.getConnection();
            st = con.createStatement();
            String sql = "select * from employee_department";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                if(rs.getString("departmentname")!=null){
                    departmentName=rs.getString("departmentname");
                }
                departmentList.add(departmentName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getDepartmentList() Leaving Method ");
        return departmentList;
    }
    public ArrayList getDesignationList() {
        log.info("CommonDAO :getDesignationList() Entering Method ");
        Connection con = null;
        Statement st=null;
        ArrayList designationList = new ArrayList();
        ResultSet rs = null;
        DesignationBean bean=null;
        String sql = "select DESIGNATION,EMPLEVEL from EMPLOYEE_DESEGNATION";
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    bean = new DesignationBean();
                    if (rs.getString("EMPLEVEL")!=null) {
                        bean.setEmplevel(rs.getString("EMPLEVEL"));
                    } else {
                        bean.setEmplevel("");
                    }
                    if (rs.getString("DESIGNATION")!=null) {
                        bean.setDesingation(rs.getString("DESIGNATION"));
                    } else {
                        bean.setDesingation("");
                    }
                    designationList.add(bean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getDesignationList() Leaving Method ");
        return designationList;
    }
    public EmployeePersonalInfo loadEmployeePersonalInfo(ResultSet rs)  throws SQLException {
        EmployeePersonalInfo personal = new EmployeePersonalInfo();
        log.info("loadEmployeePersonalInfo==============");
        CommonUtil  commonUtil=new CommonUtil();
        if (rs.getString("cpfacno") != null) {
            personal.setCpfAccno(rs.getString("cpfacno"));
        } else {
            personal.setCpfAccno("---");
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
        if (rs.getString("EMPSERIALNUMBER") != null) {
            personal.setOldPensionNo(rs.getString("EMPSERIALNUMBER"));
       
        } else {
            personal.setOldPensionNo("---");
        }
        if (rs.getString("EMPSERIALNUMBER") != null) {
            personal.setPensionNo(commonUtil.leadingZeros(5,rs.getString("EMPSERIALNUMBER")));
      
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
        
        if (rs.getString("PENSIONNUMBER") != null) {
            personal.setRefPensionNumber(rs.getString("PENSIONNUMBER"));
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
    
            personal.setRemarks("---");
        
        if (rs.getString("lastactive") != null) {
            personal.setLastActive(commonUtil.getDatetoString(rs
                    .getDate("lastactive"), "dd-MMM-yyyy"));
        } else {
            personal.setLastActive("---");
        }
        
        if (rs.getString("SEX") != null) {
            personal.setGender(rs.getString("SEX"));
         
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
        if(!personal.getDateOfBirth().equals("---")){
            String personalPFID = this.getPFID(personal.getEmployeeName(), personal
                    .getDateOfBirth(), personal.getPensionNo());
            personal.setPfID(personalPFID);
        }else{
            personal.setPfID(personal.getPensionNo());
        }
        
        return personal;
        
        }
    public EmployeePersonalInfo loadPersonalInfo(ResultSet rs)  throws SQLException {
        EmployeePersonalInfo personal = new EmployeePersonalInfo();
        log.info("loadPersonalInfo==============");
        CommonUtil  commonUtil=new CommonUtil();
        if (rs.getString("cpfacno") != null) {
            personal.setCpfAccno(rs.getString("cpfacno"));
        } else {
            personal.setCpfAccno("---");
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
            personal.setPensionNo(commonUtil.leadingZeros(5,rs.getString("PENSIONNO")));
        log.info("pfno "+personal.getPensionNo());
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
        log.info("WETHEROPTION"+personal.getWetherOption());
      
     
        
       
        
        
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
    
            personal.setRemarks("---");
        
        if (rs.getString("lastactive") != null) {
            personal.setLastActive(commonUtil.getDatetoString(rs
                    .getDate("lastactive"), "dd-MMM-yyyy"));
        } else {
            personal.setLastActive("---");
        }
        
        if (rs.getString("GENDER") != null) {
            personal.setGender(rs.getString("GENDER"));
            log.info("gender "+rs.getString("GENDER").toString());
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
            personal.setSeperationDate(commonUtil.converDBToAppFormat(rs
                    .getDate("DATEOFSEPERATION_DATE")));
        } else {
            personal.setSeperationDate("---");
        }
        if (rs.getString("division") != null) {
            personal.setDivision(rs.getString("division"));
        } else {
            personal.setDivision("---");
        }
        if(!personal.getDateOfBirth().equals("---")){
            String personalPFID = this.getPFID(personal.getEmployeeName().trim(), personal
                    .getDateOfBirth(), personal.getPensionNo());
            personal.setPfID(personalPFID);
        }else{
            personal.setPfID(personal.getPensionNo());
        }
        //below lines added as on 21-sep-2010
        if (rs.getString("FINALSETTLMENTDT") != null) {
            personal.setFinalSettlementDate(commonUtil.converDBToAppFormat(rs
                    .getDate("FINALSETTLMENTDT")));
        } else {
            personal.setFinalSettlementDate("---");
        }    
        
        
        
        
        
        return personal;
        
        }
    
   /* public String getPFID(String empName, String dateofBirth, String sequenceNo) {

        // TODO Auto-generated method stub
        String finalName = "", fname = "";
        SimpleDateFormat fromDate = null;
        int endIndxName = 0;
        String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
        String tempName = "", convertedDt = "";
       
        try {
            if(!(dateofBirth.equals("")||dateofBirth.equals("---"))){
                 if (dateofBirth.indexOf("-") != -1) {
                     fromDate = new SimpleDateFormat("dd-MMM-yyyy");
                 } else {
                     fromDate = new SimpleDateFormat("dd/MMM/yyyy");
                 }
                 SimpleDateFormat toDate = new SimpleDateFormat("ddMMyy");
                 convertedDt = toDate.format(fromDate.parse(dateofBirth));
            }
           
        
            int startIndxName = 0, index = 0;
            endIndxName = 1;
            if(!empName.equals("")){
                 if(empName.lastIndexOf(".")==empName.length()-1){
                    empName=empName.substring(0,empName.length()-1);
                 }
            }
           
            for (int i = 0; i < quats.length; i++) {
                if (empName.indexOf(quats[i]) != -1) {
                    tempName = empName.replaceAll(quats[i], "").trim();
                    // tempName=empName.substring(index+1,empName.length());
                    
                    empName = tempName;
                }
            }
            finalName = empName.substring(startIndxName, endIndxName);
       
            if (empName.indexOf(" ") != -1) {
                endIndxName = empName.trim().indexOf(" ");
                finalName = finalName + empName.substring(endIndxName).trim();
                log.info("First if finalName"+finalName+"empName"+empName);
            } else if (empName.indexOf(".") != -1) {
                endIndxName = empName.lastIndexOf(".");
                finalName = finalName
                        + empName.substring(endIndxName + 1, empName.length())
                                .trim();
                log.info("Else if finalName"+finalName+"empName"+empName);
            } else {
                finalName = empName.substring(0, 2);
            }
            log.info("finalName"+finalName+"empName===="+empName);
            char[] cArray = finalName.toCharArray();
            log.info("Array0"+cArray[0]+"Array1"+cArray[1]);
            fname = String.valueOf(cArray[0]);
            if(cArray[1]!=' '){
            	 fname = fname + String.valueOf(cArray[1]);
            }
           

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.printStackTrace(e);
        } catch (Exception e) {
             log.printStackTrace(e);

        }
     
        return convertedDt + fname + sequenceNo;

    }*/
    public String getPFID(String empName, String dateofBirth, String sequenceNo) {
        log.info("======================"+empName+dateofBirth+sequenceNo);
        empName=empName.toUpperCase();
 	   String finalName = "", fname = "";
        SimpleDateFormat fromDate = null;
        int endIndxName = 0;
        String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
        String tempName = "", convertedDt = "";
       if(dateofBirth.equals("") || dateofBirth.equals("---")){
     	  dateofBirth="01-Apr-1995";
       }
        try {
     	   
            if (dateofBirth.indexOf("-") != -1) {
                fromDate = new SimpleDateFormat("dd-MMM-yyyy");
            } else {
                fromDate = new SimpleDateFormat("dd/MMM/yyyy");
            }
            SimpleDateFormat toDate = new SimpleDateFormat("ddMMyy");
            convertedDt = toDate.format(fromDate.parse(dateofBirth));
            int startIndxName = 0, index = 0;
            endIndxName = 1;
            for (int i = 0; i < quats.length; i++) {
                if (empName.indexOf(quats[i]) != -1) {
                    tempName = empName.trim().replaceAll(quats[i], "").trim();
                    empName = tempName;
                }
            }
            if(empName.lastIndexOf("(")!=-1){
           	 empName=empName.substring(0,empName.lastIndexOf("(")).trim();
            }else if(empName.lastIndexOf(" -")!=-1){
           	 empName=empName.substring(0,empName.lastIndexOf(" -")).trim();
            }else if(empName.lastIndexOf(" I")!=-1){
           	 empName=empName.substring(0,empName.lastIndexOf(" I")).trim();
            }
            
            finalName = empName.substring(startIndxName, endIndxName);
            if (empName.indexOf(" ") != -1) {
                endIndxName = empName.lastIndexOf(" ");
                finalName = finalName + empName.substring(endIndxName).trim();
            } else if (empName.indexOf(".") != -1) {
                endIndxName = empName.indexOf(".");
                finalName = finalName
                        + empName.substring(endIndxName + 1, empName.length())
                                .trim();
            } else {
                finalName = empName.substring(0, 2);
            }
            char[] cArray = finalName.toCharArray();
            fname = String.valueOf(cArray[0]);
            fname = fname + String.valueOf(cArray[1]);

      } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      } catch (Exception e) {
          log.info("Exception is " + e);

      }
     // log.info("PersonalDao:getPFID() leaving method");
      return convertedDt + fname + sequenceNo;

  }
    public String getCPFACCNOByPension(String pensionNo,String cpfacno,String region){
        Connection con = null;
        Statement st=null;
        ResultSet rs=null;
        StringBuffer buffer=new StringBuffer();
        String cpfaccno="";
        String selectFamilySQL = "SELECT CPFACNO,REGION FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER='"+pensionNo.trim()+"' ";
        //log.info("Query =====" + selectFamilySQL);
        try {
            con = commonDB.getConnection();
             st = con.createStatement();
             rs = st.executeQuery(selectFamilySQL);
            while (rs.next()) {
                buffer.append(rs.getString("CPFACNO"));
                buffer.append(",");
                buffer.append(rs.getString("REGION"));
                buffer.append("=");
            }
      /*      buffer.append(cpfacno);
            buffer.append(",");
            buffer.append(region);
            buffer.append("=");*/
            cpfaccno=buffer.toString();
            if(cpfaccno.equals("")){
                cpfaccno="---";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
        	commonDB.closeConnection(con,st,rs);
        }
       return cpfaccno;
   }
    public ArrayList getAirportsByPersonalTbl(String region,String accountType,String airportcode) {
		Connection con = null;
		Statement st=null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "";
		String condition="";
		if(accountType!=""){
			  condition=condition+" and accounttype='"+accountType+"'";
		}
		if(airportcode!=""){
			condition=condition+" and unitname='"+airportcode+"'";
		}
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT distinct UNITNAME as AIRPORTCODE,VACCOUNTNO FROM employee_unit_master where region='"
				+ region+"' and UNITNAME is not null "+condition+"";
			log.info("getAirportsByPersonalTbl==query==========="+query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("AIRPORTCODE") != null) {
					unitName = (String) rs.getString("airportcode").toString().trim();
					bean.setStation(unitName);
				}else {
					bean.setStation("");
				}
				if (rs.getString("VACCOUNTNO") != null) {
					
					bean.setVAccountNo(rs.getString("VACCOUNTNO"));
				}else {
					bean.setVAccountNo("");
				}
			airportList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}
    public ArrayList getAirportsByAccntType(String region,String accountType,String airportcode) {
		Connection con = null;
		Statement st=null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "";
		String condition="";
		if(accountType!=""){
			  condition=condition+" and accounttype='"+accountType+"'";
		}
		if(airportcode!=""){
			condition=condition+" and unitname='"+airportcode+"'";
		}
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT distinct UNITNAME as AIRPORTCODE,VACCOUNTNO FROM employee_unit_master where region='"
				+ region+"' and UNITNAME is not null  and VACCOUNTNO IS NOT NULL "+condition+" ORDER BY VACCOUNTNO";
			log.info("getAirportsByAccntType==query==========="+query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("AIRPORTCODE") != null) {
					unitName = (String) rs.getString("airportcode").toString().trim();
					bean.setStation(unitName);
				}else {
					bean.setStation("");
				}
				if (rs.getString("VACCOUNTNO") != null) {
					
					bean.setVAccountNo(rs.getString("VACCOUNTNO"));
				}else {
					bean.setVAccountNo("");
				}
			airportList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}
    public int getPFIDsNaviagtionList(String region,String airportcode,String transferFlag,int totalSize) {
        log.info("CommonDAO :getPFIDsNaviagtionList() Entering Method ");
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        String airportCodeString="";
        if(!airportcode.equals("NO-SELECT")){
        	airportCodeString="AND EPI.AIRPORTCODE='"+airportcode+"'";
        }else{
        	airportCodeString="";
        }
        
        String sql = "SELECT COUNT(*) AS COUNT FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND  EPI.REGION='"+region+ "' " +
        		" AND ETI.TRANSFERFLAG='"+transferFlag+"' "+airportCodeString;
        log.info(sql);
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if (rs.next()) {
                	pfidTotal=rs.getInt("COUNT");
                }	
               
                eachPage=Math.round(pfidTotal/totalSize+1);
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getPFIDsNaviagtionList() Leaving Method ");
        return eachPage;
    }
    public int getPFIDsNaviagtionListSize(int totalSize) {
        log.info("CommonDAO :getPFIDsNaviagtionListSize() Entering Method ");
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS COUNT FROM EMPLOYEE_PERSONAL_INFO ";
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sql);
                if (rs.next()) {
                	pfidTotal=rs.getInt("COUNT");
                }	
               
                eachPage=Math.round(pfidTotal/totalSize+1);
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getPFIDsNaviagtionListSize() Leaving Method ");
        return eachPage;
    }
    public void updateTransPensionData() {
        String fromYear = "", toYear = "",dateOfRetriment="";
       
        ArrayList empList = new ArrayList();
        EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
        String updateQuery="";
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Statement upST = null;
        int updateSt=0,finalUpdate=0;
        String sqlQuery = "SELECT CPFACNO,REGION,EMPLOYEENAME,EMPSERIALNUMBER FROM EMPLOYEE_INFO  WHERE EMPSERIALNUMBER IN (SELECT PENSIONNO FROM EMPLOYEE_PERSONAL_INFO) ORDER BY EMPSERIALNUMBER";
        try {
            con = commonDB.getConnection();
            st = con.createStatement();
            upST=con.createStatement();
            rs = st.executeQuery(sqlQuery);
            String per_region="",cpfaccno="",employeename="",perPensionNo="";
            BufferedWriter fw = new BufferedWriter(new FileWriter(
            "c://GenertedPensionNoTrnsData.txt"));
            String totalInfo="";
            while (rs.next()) {
           	 per_region=rs.getString("REGION");
           	 cpfaccno=rs.getString("CPFACNO");
           	 perPensionNo=rs.getString("EMPSERIALNUMBER");
           	 totalInfo=per_region+","+cpfaccno+","+perPensionNo; 
           	 updateQuery="UPDATE employee_pension_validate SET PENSIONNO='"+perPensionNo.trim()+"' WHERE CPFACCNO='"+cpfaccno.trim()+"' AND REGION='"+per_region.trim()+"'";
           	 fw.write(totalInfo);
           	 fw.newLine();
           	 fw.write(updateQuery + ";");
                fw.newLine();
                fw.write("============================");
                fw.flush();
           	 updateSt=upST.executeUpdate(updateQuery);
           	 finalUpdate=finalUpdate+updateSt;
           	 
            }
            log.info("Total Updations"+finalUpdate);
        }catch (SQLException e) {
            log.printStackTrace(e);
        } catch (Exception e) {
            log.printStackTrace(e);
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
      //  return form8List;
    }
    public String getLastDayOfMonth(String date) {
		Connection con = null;
		Statement st=null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String lastDayOfMnth="";
		
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT TO_CHAR(LAST_DAY(add_months('"+date+"',1)),'dd-Mon-yyyy') AS LASTDAYMNTH FROM DUAL";
				
			log.info("getAirportsByFinanceTbl==query==========="+query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				lastDayOfMnth=rs.getString("LASTDAYMNTH");
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return lastDayOfMnth;

	}
    public double getRateOfInterest(String year){
    	double rateOfInterest=0.0;
    	log.info("getRateOfInterest"+year);
    	if(Integer.parseInt(year)>=1995 && Integer.parseInt(year)<2000){
			rateOfInterest=12;
		}else if(Integer.parseInt(year)>2000 && Integer.parseInt(year)<2001){
			rateOfInterest=11;
		}else if(Integer.parseInt(year)>2001 && Integer.parseInt(year)<2005){
			rateOfInterest=9.50;
		}else if(Integer.parseInt(year)>2005 && Integer.parseInt(year)<2009){
			rateOfInterest=8.50;
		}else if(Integer.parseInt(year)>2009 && Integer.parseInt(year)<2010){
			rateOfInterest=8.00;
		}
    	

    	return rateOfInterest;
    }
    public ArrayList getPFIDsNaviagtionList(String region,String airportcode,String transferFlag,String range) {
        log.info("CommonDAO :getPFIDsNaviagtionList() Entering Method ");
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        ArrayList rangeList=new ArrayList(); 
        String airportCodeString="",regionString="",personalQry="",sqlQuery="",fromID="",toID="",pfidString="";
        if(!airportcode.equals("NO-SELECT")){
        	airportCodeString=airportcode;
        }else{
        	airportCodeString="";
        }
        if(!region.equals("NO-SELECT")){
        	regionString=region;
        }else{
        	regionString="";
        }
        personalQry=this.buildQueryNavgationForTrnsferPFList(regionString,airportCodeString,transferFlag);
        sqlQuery="SELECT SFROM, STO FROM (SELECT ((MYSEQ - 1) * "+range+" + 1) SFROM, (MYSEQ * "+range+") STO FROM (SELECT LEVEL MYSEQ FROM DUAL  CONNECT BY LEVEL <= 1000 "+
        ")) SEQ, ("+personalQry+") PNO WHERE PNO.PENSIONNO BETWEEN SFROM AND STO GROUP BY SFROM, STO ORDER BY SFROM, STO";
        log.info(sqlQuery);
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sqlQuery);
                while (rs.next()) {
                	if(rs.getString("SFROM")!=null){
                		fromID=rs.getString("SFROM");
                	}
                	if(rs.getString("STO")!=null){
                		toID=rs.getString("STO");
                	}
                	pfidString=fromID+" - "+toID;
                	rangeList.add(pfidString);
                }	
               
              
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getPFIDsNaviagtionList() Leaving Method ");
        return rangeList;
    }
    public String buildQuerygetPFIDsNaviagtionList(String region,String airportcode) {
    	log.info("CommonDAO::buildQuerygetPFIDsNaviagtionList-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
  
    	sqlQuery="SELECT COUNT(*) AS COUNT FROM EMPLOYEE_PERSONAL_INFO ";
    	if (!region.equals("")) {
			whereClause.append(" REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportcode.equals("")) {
			whereClause.append(" AIRPORTCODE ='"
					+ airportcode + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportcode.equals("")) ) {

		} else {
			query.append(" WHERE ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQuerygetPFIDsNaviagtionList Leaving Method");
    	return dynamicQuery;
    }
    public String sTokenFormat(StringBuffer stringBuffer) {

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
    public ArrayList navgtionPFIDsList(String region,String airportcode,String range,String monthYear,String mnthYearFlag) {
        log.info("CommonDAO :navgtionPFIDsList() Entering Method ");
        ArrayList rangeList=new ArrayList();
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        String airportCodeString="",regionString="",personalQry="",sqlQuery="",fromID="",toID="",pfidString="";
        if(!airportcode.equals("NO-SELECT")){
        	airportCodeString=airportcode;
        }else{
        	airportCodeString="";
        }
        if(!region.equals("NO-SELECT")){
        	regionString=region;
        }else{
        	regionString="";
        }
        if(mnthYearFlag.equals("true")){
        	personalQry=this.buildQueryTransNavgationPFList(regionString,airportCodeString,monthYear);	
        }else{
        	personalQry=this.buildQueryNavgationPFList(regionString,airportCodeString);	
        	
        }
        
        sqlQuery="SELECT SFROM, STO FROM (SELECT ((MYSEQ - 1) * "+range+" + 1) SFROM, (MYSEQ * "+range+") STO FROM (SELECT LEVEL MYSEQ FROM DUAL  CONNECT BY LEVEL <= 1000 "+
        ")) SEQ, ("+personalQry+") PNO WHERE PNO.PENSIONNO BETWEEN SFROM AND STO GROUP BY SFROM, STO ORDER BY SFROM, STO";
        log.info(sqlQuery);
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sqlQuery);
                while (rs.next()) {
                	if(rs.getString("SFROM")!=null){
                		fromID=rs.getString("SFROM");
                	}
                	if(rs.getString("STO")!=null){
                		toID=rs.getString("STO");
                	}
                	pfidString=fromID+" - "+toID;
                	rangeList.add(pfidString);
                }	
               
               
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :navgtionPFIDsList() Leaving Method ");
        return rangeList;
    }

    public String buildQueryNavgationPFList(String region,String airportCode) {
    	log.info("CommonDAO::buildQuerygetEmployeePFInfoPrinting-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
    	
    	sqlQuery="SELECT PENSIONNO FROM EMPLOYEE_PERSONAL_INFO";

    	if (!region.equals("")) {
			whereClause.append(" REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportCode.equals("")) {
			whereClause.append(" AIRPORTCODE ='"
					+ airportCode + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportCode.equals(""))) {

		} else {
			query.append(" WHERE ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQueryNavgationPFList Leaving Method");
    	return dynamicQuery;
    }
    public String buildQueryNavgationForTrnsferPFList(String region,String airportCode,String transferFlag) {
    	log.info("CommonDAO::buildQueryNavgationForTrnsferPFList-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
    	if(!transferFlag.equals("")){
    		sqlQuery = "SELECT EPI.PENSIONNO FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO ";
    	}else{
    		sqlQuery = "SELECT EPI.PENSIONNO FROM EMPLOYEE_PERSONAL_INFO EPI WHERE EPI.PENSIONNO IS NOT NULL  ";
    	}
    	
    

    	if (!region.equals("")) {
			whereClause.append(" EPI.REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportCode.equals("")) {
			whereClause.append(" EPI.AIRPORTCODE ='"
					+ airportCode + "'");
			whereClause.append(" AND ");
		}
    	if (!transferFlag.equals("")) {
			whereClause.append("  ETI.TRANSFERFLAG ='"
					+ transferFlag + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportCode.equals("")) && (transferFlag.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQueryNavgationForTrnsferPFList Leaving Method");
    	return dynamicQuery;
    }
    public String buildQueryTransNavgationPFList(String region,String airportCode,String monthYear) {
    	log.info("CommonDAO::buildQueryTransNavgationPFList-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
    	
    	sqlQuery="SELECT DISTINCT PENSIONNO FROM EMPLOYEE_PENSION_VALIDATE";

    	if (!region.equals("")) {
			whereClause.append(" REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportCode.equals("")) {
			whereClause.append(" AIRPORTCODE ='"
					+ airportCode + "'");
			whereClause.append(" AND ");
		}
    	if (!monthYear.equals("")) {
			whereClause.append(" TO_CHAR(MONTHYEAR,'dd-MM-yyyy') like'%"
					+ monthYear + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportCode.equals(""))) {

		} else {
			query.append(" WHERE ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQueryTransNavgationPFList Leaving Method");
    	return dynamicQuery;
    }

    public String getLatestTrnsDate(String region) {
		Connection con = null;
		Statement st=null;
		ResultSet rs = null;
		String monthYear="";
		
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT ('-' || TO_CHAR(MAX(MONTHYEAR),'MM') || '-' || TO_CHAR(MAX(MONTHYEAR),'YYYY')) AS INFORMAT FROM EMPLOYEE_PENSION_VALIDATE WHERE REGION='"+region+"'";
			log.info("getLatestTrnsDate==query==========="+query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				if (rs.getString("INFORMAT") != null) {
					monthYear = rs.getString("INFORMAT");
				}
				
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		}catch (Exception e) {
			log.printStackTrace(e);
		}finally{
			
		}
		return monthYear;

	}
    public ArrayList getAirportsByBulkPFIDSTbl(String region) {
		Connection con = null;
		Statement st=null;
		ArrayList airportList = new ArrayList();
		EmpMasterBean bean=null;
		ResultSet rs = null;
		String unitName = "", unitCode = "";
		
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT distinct AIRPORTCODE  FROM EPIS_BULK_PRINT_PFIDS where region='"
				+ region+"' and airportcode is not null";
			log.info("getAirportsByFinanceTbl==query==========="+query);
			rs = st.executeQuery(query);
			bean = new EmpMasterBean();
			
			while (rs.next()) {
				bean = new EmpMasterBean();
				if (rs.getString("AIRPORTCODE") != null) {
					unitName = (String) rs.getString("airportcode").toString().trim();
					bean.setStation(unitName);
				}else {
					bean.setStation("");
				}
				
				airportList.add(bean);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}
    public ArrayList bulkNavgtionPFIDsList(String region,String airportcode,String range,String monthYear,String mnthYearFlag,String finYear) {
        log.info("CommonDAO :navgtionPFIDsList() Entering Method ");
        ArrayList rangeList=new ArrayList();
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        String airportCodeString="",regionString="",personalQry="",sqlQuery="",fromID="",toID="",pfidString="";
        if(!airportcode.equals("NO-SELECT")){
        	airportCodeString=airportcode;
        }else{
        	airportCodeString="";
        }
        if(!region.equals("NO-SELECT")){
        	regionString=region;
        }else{
        	
        	
        	regionString="";
        }
  
        	
        personalQry=this.buildQueryBulkNavgationPFList(regionString,airportCodeString,finYear);	
       
        
        sqlQuery="SELECT SFROM, STO FROM (SELECT ((MYSEQ - 1) * "+range+" + 1) SFROM, (MYSEQ * "+range+") STO FROM (SELECT LEVEL MYSEQ FROM DUAL  CONNECT BY LEVEL <= 1000 "+
        ")) SEQ, ("+personalQry+") PNO WHERE PNO.PENSIONNO BETWEEN SFROM AND STO GROUP BY SFROM, STO ORDER BY SFROM, STO";
        log.info(sqlQuery);
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sqlQuery);
                while (rs.next()) {
                	if(rs.getString("SFROM")!=null){
                		fromID=rs.getString("SFROM");
                	}
                	if(rs.getString("STO")!=null){
                		toID=rs.getString("STO");
                	}
                	pfidString=fromID+" - "+toID;
                	rangeList.add(pfidString);
                }	
               
               
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :navgtionPFIDsList() Leaving Method ");
        return rangeList;
    }

    public String buildQueryBulkNavgationPFList(String region,String airportCode,String finYear) {
    	log.info("CommonDAO::buildQuerygetEmployeePFInfoPrinting-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
    	
    	sqlQuery="SELECT PFID AS PENSIONNO FROM EPIS_BULK_PRINT_PFIDS WHERE FINAYEAR='"+finYear+"'";

    	if (!region.equals("")) {
			whereClause.append(" REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportCode.equals("")) {
			whereClause.append(" AIRPORTCODE ='"
					+ airportCode + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportCode.equals(""))) {

		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQueryNavgationPFList Leaving Method");
    	return dynamicQuery;
    }

    public ArrayList getAirportsByLatestPersonalTbl(String region,String finayear) {
		Connection con = null;
		Statement st=null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "", unitCode = "";
		
		try {
			con = commonDB.getConnection();
			 st = con.createStatement();
			String query = "SELECT REGION,AIRPORTCODE FROM V_EMP_PERSONAL_INFO WHERE FINYEAR='"+finayear+"' AND UPPER(REGION)='"+region.toUpperCase()+"' GROUP BY REGION,AIRPORTCODE ORDER BY REGION,AIRPORTCODE";
			
			log.info("getAirportsByLatestPersonalTbl==query==========="+query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("AIRPORTCODE") != null) {
					unitName = (String) rs.getString("AIRPORTCODE").toString().trim();
					bean.setStation(unitName);
				}else {
					bean.setStation("");
				}
				
				airportList.add(bean);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}
    public ArrayList getPFIDsForm78RevisedNaviagtionList(String region,String airportcode,String transferFlag,String range) {
        log.info("CommonDAO :getPFIDsNaviagtionList() Entering Method ");
        Connection con = null;
        Statement st=null;
        int pfidTotal=0,eachPage=0;
        ResultSet rs = null;
        ArrayList rangeList=new ArrayList(); 
        String airportCodeString="",regionString="",personalQry="",sqlQuery="",fromID="",toID="",pfidString="";
        if(!airportcode.equals("NO-SELECT")){
        	airportCodeString=airportcode;
        }else{
        	airportCodeString="";
        }
        if(!region.equals("NO-SELECT")){
        	regionString=region;
        }else{
        	regionString="";
        }
        personalQry=this.buildQueryBulkNavgationRevisedList(regionString,airportCodeString,"");
        sqlQuery="SELECT SFROM, STO FROM (SELECT ((MYSEQ - 1) * "+range+" + 1) SFROM, (MYSEQ * "+range+") STO FROM (SELECT LEVEL MYSEQ FROM DUAL  CONNECT BY LEVEL <= 1000 "+
        ")) SEQ, ("+personalQry+") PNO WHERE PNO.PENSIONNO BETWEEN SFROM AND STO GROUP BY SFROM, STO ORDER BY SFROM, STO";
        log.info(sqlQuery);
        try {
                con = commonDB.getConnection();
                st = con.createStatement();
                rs = st.executeQuery(sqlQuery);
                while (rs.next()) {
                	if(rs.getString("SFROM")!=null){
                		fromID=rs.getString("SFROM");
                	}
                	if(rs.getString("STO")!=null){
                		toID=rs.getString("STO");
                	}
                	pfidString=fromID+" - "+toID;
                	rangeList.add(pfidString);
                }	
               
              
                
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commonDB.closeConnection(con, st, rs);
        }
        log.info("CommonDAO :getPFIDsNaviagtionList() Leaving Method ");
        return rangeList;
    }
    public String buildQueryBulkNavgationRevisedList(String region,String airportCode,String finYear) {
    	log.info("CommonDAO::buildQueryBulkNavgationRevisedList-- Entering Method");
    	StringBuffer whereClause = new StringBuffer();
    	StringBuffer query = new StringBuffer();
    	String dynamicQuery = "",sqlQuery = "";
    	
    	sqlQuery="SELECT PENSIONNO FROM employee_arrear_breakup ";

    	if (!region.equals("")) {
			whereClause.append(" REGION ='"
					+ region + "'");
			whereClause.append(" AND ");
		}
  
    	if (!airportCode.equals("")) {
			whereClause.append(" AIRPORTCODE ='"
					+ airportCode + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("")) && (airportCode.equals(""))) {

		} else {
			query.append(" WHERE ");
			query.append(this.sTokenFormat(whereClause));
		}
    	dynamicQuery = query.toString();
    	log.info("CommonDAO::buildQueryBulkNavgationRevisedList Leaving Method");
    	return dynamicQuery;
    }
    //New Method
    //By Radha On 3-Aug-2011 for CntrlAccount 11 & 12 Tracking purpose 
    public String  getCtrlTrackingId(AaiEpfForm11Bean epfForm11Bean,String dbFlag){
    	log.info("CommonDAO::getCtrlTrackingId-- Entering Method");
    	Connection con = null;
		Statement st=null; 
    	String sqlQuery = "",trakingId = "",reportChkType = "",formType = "",userRegion = "",UserStation = "",userName = "", selectedYear = "";
    	reportChkType = epfForm11Bean.getReportChkType();
    	formType = epfForm11Bean.getFormType();
    	userRegion = epfForm11Bean.getRegion();
    	UserStation =  epfForm11Bean.getAirportCode();
    	userName =  epfForm11Bean.getUserName();
    	selectedYear = epfForm11Bean.getSelectedYear();
    	try {
            con = commonDB.getConnection();
            reportChkType =  epfForm11Bean.getReportChkType();
            
            if(dbFlag.equals("insert")){
            trakingId = this.getCntrlTrackingSequenceId(con);            
            sqlQuery = "insert into epis_cntrl_tracking_log(TRACKID,FORMTYPE,REGION,AIRPORTCODE,TRKLOGDATE,USERNAME,TYPE,YEAR) "            			 
            		+ " values('"+trakingId+"','"+formType+"','"+userRegion+"','"+UserStation+"',sysdate,'"+userName+"','"+reportChkType+"','"+selectedYear+"')";
            }else{           	
             if(formType.equals("Form-11")){
              sqlQuery = "update epis_cntrl_tracking_log  set EMPOB = "
            			+ epfForm11Bean.getEmpNetOBTot()
            			+" ,EMPADJOB ="
            			+ epfForm11Bean.getEmpAdjOBTot()
            			+", EMOLUMENTS = "
            			+ epfForm11Bean.getEmptotal()
            			+" ,EPF ="
            			+ epfForm11Bean.getEmppfstatury()
            			+" ,VPF ="
            			+ epfForm11Bean.getEmpvpf()
            			+" ,PFPRINCIPAL =" 
            			+ epfForm11Bean.getPrincipal()
            			+" ,PFINTEREST ="
            			+ epfForm11Bean.getInterest()
            			+" ,EMPTOTAL ="
            			+ epfForm11Bean.getEmpSubTotalTot()
            			+" ,PFWEMPADVANCE ="
            			+ epfForm11Bean.getRefAdvPFW()
            			+" ,EMPINTEREST ="
            			+ epfForm11Bean.getInterestCredited()
            			+" ,EMPFINALSET ="
            			+ epfForm11Bean.getFinalPaymentSubTot()
            			+" ,EMPCLOSEOB ="
            			+ epfForm11Bean.getEmpCLBalTot()
            			+ " where TRACKID ="+epfForm11Bean.getTrackingId() ;	 
            }else{                                
            	 sqlQuery = "update epis_cntrl_tracking_log  set AAIOB = "
          			+ epfForm11Bean.getAAINetOBTot()
          			+" ,AAIADJOB ="
          			+ epfForm11Bean.getAAIAdjOBTot()
          			+", AAIPF = "
          			+ epfForm11Bean.getPf()
          			+" ,PFWCONTRI ="
          			+ epfForm11Bean.getPfwContr()
          			+" ,PENSIONCONTRI ="
          			+ epfForm11Bean.getPensinonContr()
          			+" ,AAIPFFINALSET =" 
          			+ epfForm11Bean.getFinalPaymentContrTot()
          			+" ,AAIDEDPENSIONFINALSET ="
          			+ epfForm11Bean.getAaiIntersetCredited()
          			+" ,AAICLOSEOB ="
          			+ epfForm11Bean.getAAICLBalTot()         			 
          			+ " where TRACKID ="+epfForm11Bean.getTrackingId() ;                            
                    
            } 
            	
            }
          log.info("--sqlQuery-------"+sqlQuery);     
            st = con.createStatement();
            st.executeUpdate(sqlQuery);
            
             
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        commonDB.closeConnection(con, st, null);
    }
    log.info("CommonDAO::getCtrlTrackingId-- Leaving Method");
    return trakingId;
    }
   
    public String getCntrlTrackingSequenceId(Connection con) {
		String trackSeqId = "";
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT TRACK_SEQ.NEXTVAL AS  TRACKINGID FROM DUAL";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				trackSeqId = rs.getString("TRACKINGID");
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null,st,rs);
		}
		return trackSeqId;
	}
    //New Method
    
   /* public String checkControlAccntStatus(String formType,String serialNo) {
		log.info("CommonDAO :checkControlAccntStatus() entering method ");
	   
		String sqlQuery = "" ,result="",verifyform2Query="",verifyform2AQuery="",verifyform8Query="",commonQry="";
		double daysDiff  =0, maxDays =4.00;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		  if(formType.equals("summaryReports")){
			  
			  sqlQuery = "select  serialNo  from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N'" ;
		  
		  } else if(formType.equals("summaryinformation")){
			  
			  sqlQuery = "   select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'Form 2A') res1,"
                  +" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 8') res2  where res1.serialno = res2.serialno and res1.serialno="+serialNo ;
		  
		  }else if(formType.equals("form2summary")){
			  
			  sqlQuery = "select   round((sysdate-lastactive),2) as diff   from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N' and serialNo="+serialNo;
		  
		  }else if(formType.equals("form2Asummary")){
			  
			  sqlQuery = "   select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'ControlAccountProccessing') res1,"
                         +" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2') res2  where res1.serialno = res2.serialno  and res1.serialno="+serialNo ;
			  
		  }else if(formType.equals("form8summary")){
			  
			  sqlQuery = "   select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'Form 2') res1,"
                  +" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2A') res2  where res1.serialno = res2.serialno and res1.serialno="+serialNo ;
		 
		  }
		  commonQry =" select   round((sysdate-lastactive),2) as diff   from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N'";
		  verifyform2Query =  "select   round((sysdate-lastactive),2) as diff   from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N' and serialNo="+serialNo;
		verifyform2AQuery = "  select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'ControlAccountProccessing') res1,"
							+" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2') res2  where res1.serialno = res2.serialno  and res1.serialno="+serialNo ;
		verifyform8Query ="   select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'Form 2') res1,"
                         +" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2A') res2  where res1.serialno = res2.serialno and res1.serialno="+serialNo ;
		log.info("formType is--------- " + formType);     
		log.info("sqlQuery is " + sqlQuery);
		try {
		   con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			if (rs.next()) {
				
				if(formType.equals("summaryReports")){
					result= rs.getString("serialNo"); 
				}else{
					daysDiff=  Double.parseDouble(rs.getString("diff")); 
					
					if(daysDiff>0){
						result="Days Expired";
					}else{
						result="exists";
					}
					
				}
				 
			}else{
				log.info("in else----------------");
				
				if(formType.equals("summaryinformation")){ 
					
					
					
				}else if(formType.equals("form8summary")){
					log.info("verifyform2AQuery is " + verifyform2AQuery);
					rs = st.executeQuery(verifyform2AQuery);
					if(rs.next()){
						  daysDiff=  Double.parseDouble(rs.getString("diff"));						
							if(daysDiff>4){
								result="Days Expired"; 
							}
					 
					}else{
						log.info("verifyform2Query is " + verifyform2Query);
						rs = st.executeQuery(verifyform2Query);
						if(rs.next()){
							daysDiff=  Double.parseDouble(rs.getString("diff"));						
							if(daysDiff>4){
								result="Days Expired"; 
							}else{
								result="Please Generate Form 2  Report First";
							}
					 
					}else{
						result="Please Generate Form 2 Report First";
					}
				} 
				}else if(formType.equals("form2Asummary")){
 
							log.info("verifyform2Query is " + verifyform2Query);
							rs = st.executeQuery(verifyform2Query);
							if(rs.next()){
								daysDiff=  Double.parseDouble(rs.getString("diff"));						
								if(daysDiff>4){
									result="Days Expired";
								} 								
							}else{
								result="Please Generate Form 2 Report First";
							} 
					 
				} 
				 
				
			}
			
			
			
			
	 	log.info("----daysDiff----- "+daysDiff+"--result--"+result);
		 if(formType.equals("form2Asummary")){
			if(result.equals("")){
				log.info("verifyform2Query is " + verifyform2Query);
				rs = st.executeQuery(verifyform2Query);
				if (rs.next()) {
					daysDiff=  Double.parseDouble(rs.getString("diff")); 						
					if(daysDiff>0){
						result="Days Expired";
					}else{
						result="Please Generate Form 2 Report First";
					}
				}else{ 
					result="Please Generate Form 2 Report First";						 
				}
			}
			
		} 
		if(formType.equals("form8summary")){
			if(result.equals("")){
				log.info("verifyform2AQuery is " + verifyform2AQuery);
				rs = st.executeQuery(verifyform2AQuery); 
					if (rs.next()) {
						daysDiff=  Double.parseDouble(rs.getString("diff")); 						
						if(daysDiff>0){
							result="Days Expired";
						}else{
						 result="Please Generate Form 2  Report First"; 
						}
							 
					
				}else{
					log.info("verifyform2Query is " + verifyform2Query);
					rs = st.executeQuery(verifyform2Query);
					if (rs.next()) {
						daysDiff=  Double.parseDouble(rs.getString("diff")); 						
						if(daysDiff>0){
							result="Days Expired";
						}else{
						 result="Please Generate Form 2  Report First"; 
						}
						
					}else{ 
						
					}
				}
			}
			
		} 
		if(formType.equals("summaryinformation")){
			if(result.equals("")){
				log.info("verifyform8Query is " + verifyform8Query);
				rs = st.executeQuery(verifyform8Query);
				if (rs.next()) {
					 
				}else{
					log.info("verifyform2AQuery is " + verifyform2AQuery);
					rs = st.executeQuery(verifyform2AQuery);
					if (rs.next()) { 
						daysDiff=  Double.parseDouble(rs.getString("diff")); 						
						if(daysDiff>0){
							result="Days Expired";
						}else{
							result="Please Generate Form 8 Report First";
						}
						
					}else{ 
						log.info("verifyform2Query is " + verifyform2Query);
						rs = st.executeQuery(verifyform2Query);
						if (rs.next()) {
							daysDiff=  Double.parseDouble(rs.getString("diff")); 						
							if(daysDiff>0){
								result="Days Expired";
							}else{
								result="Please Generate Form 2A Report First"; 
							}
							
						}else{
							log.info("commonQry is " + commonQry);
							rs = st.executeQuery(commonQry);
							if (rs.next()) { 
								daysDiff=  Double.parseDouble(rs.getString("diff")); 						
								if(daysDiff>0){
									result="Days Expired";
								}else{
									result="Please Generate Form 2 Report First";
								}
							}
						}
						
					}
				}
			}
			
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
		log.info("CommonDAO :checkControlAccntStatus() leaving method"
				+ result);
		return result;
	}*/
    
    
    
   public ArrayList checkControlAccntStatus(String formType,String serialNo) {
	log.info("CommonDAO :checkControlAccntStatus() entering method ");
   
	String sqlQuery = "" ,result="",verifyform2Query="",verifyform2AQuery="",verifyform8Query="",commonQry="",formstatus="",verifyCrtlAccStatus="";
	double daysDiff  =0, maxDays =4.00;
	Connection con = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;	
	ResultSet rs1 = null;
	ArrayList list = new ArrayList();
	  if(formType.equals("summaryReports")){
		  
		  sqlQuery = "select  serialNo, formstatus  from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus in ('N','V','F')" ;
	  
	  }else if(formType.equals("form2summary")){
		  
		  sqlQuery = "   select  round((sysdate-lastactive),2) as diff  from   epis_control_account_tracking     where  formtype = 'ControlAccountProccessing' and  formstatus in ('N','V','F') and serialno="+serialNo;
          
	  }else if(formType.equals("form2Asummary")){
		  
		  sqlQuery = "   select  round((sysdate-lastactive),2) as diff  from   epis_control_account_tracking     where  formtype = 'Form 2' and  formstatus='Y' and serialno="+serialNo;
          
	  }else if(formType.equals("form8summary")){
		  
		  sqlQuery = "   select  round((sysdate-lastactive),2) as diff    from   epis_control_account_tracking     where  formtype = 'Form 2A' and  formstatus='Y' and serialno="+serialNo;
          
	  }else if(formType.equals("summaryinformation")){
		  
		  sqlQuery = "   select  round((sysdate-lastactive),2) as diff   from   epis_control_account_tracking     where  formtype = 'Form 8' and  formstatus='Y' and serialno="+serialNo;
               
	  }
	  commonQry =" select   round((sysdate-lastactive),2) as diff   from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N'";
	  verifyform2Query =  "select   round((sysdate-lastactive),2) as diff   from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and f.formstatus='N' and serialNo="+serialNo;
	  verifyform2AQuery = "  select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'ControlAccountProccessing') res1,"
						+" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2') res2  where res1.serialno = res2.serialno  and res1.serialno="+serialNo ;
	  verifyform8Query ="   select (res2.lastactive - res1.lastactive) as diff from (select c.lastactive, serialno  from epis_control_account_tracking c   where c.formtype = 'Form 2') res1,"
                     +" (select c.lastactive, serialno   from epis_control_account_tracking c        where c.formtype = 'Form 2A') res2  where res1.serialno = res2.serialno and res1.serialno="+serialNo ;
	
	 verifyCrtlAccStatus = "select  serialNo, formstatus  from  epis_Control_Account_Tracking f where f.formtype='ControlAccountProccessing' and serialno="+serialNo;
	  
	  
	  String form2="";
	  
	  form2 ="";
	  log.info("formType is--------- " + formType);     
	log.info("sqlQuery is " + sqlQuery);
	try {
	   con = commonDB.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		st1 = con.createStatement();
		if (rs.next()) {
			
			if(formType.equals("summaryReports")){
				result= rs.getString("serialNo"); 
				formstatus= rs.getString("formstatus"); 
				
			}else{
				
				
				log.info("verifyCrtlAccStatus-----"+verifyCrtlAccStatus);
				rs1 = st1.executeQuery(verifyCrtlAccStatus);
				if(rs1.next()){
					formstatus = rs1.getString("formstatus");
				}
				
				daysDiff=  Double.parseDouble(rs.getString("diff")); 
				
				if(daysDiff<4){
					result="exists";
				}else{
					 
					if(!formstatus.equals("F")){
					result="Days Expired";
					}
				}
				
			}
			 
		}else{
			
			if(formType.equals("form2summary")){ 
			 
			}
			 
			if(formType.equals("form2Asummary")){
				log.info("verifyform2Query-----"+verifyform2Query);
				rs = st.executeQuery(verifyform2Query);
				if(rs.next()){
					daysDiff=  Double.parseDouble(rs.getString("diff")); 
					if(daysDiff<4){
						 result ="Please Generate Form 2 Report First";
						 
					}else{
						 result ="Days Expired";
					}
				}else{
					 result = "Please  execute Control Account Procedure ...........";
					
				}
				
			}
			
			
			if(formType.equals("form8summary")){
				log.info("verifyform2AQuery-----"+verifyform2AQuery);
				rs = st.executeQuery(verifyform2AQuery);
				if(rs.next()){
					daysDiff=  Double.parseDouble(rs.getString("diff")); 
					if(daysDiff<4){
						 result ="Please Generate Form 2A Report First";
						 
					}else{
						 result ="Days Expired";
					}
				}else{ 
					log.info("verifyform2Query-----"+verifyform2Query);
					rs = st.executeQuery(verifyform2Query);
					if(rs.next()){
						daysDiff=  Double.parseDouble(rs.getString("diff")); 
						if(daysDiff<4){
							 result ="Please Generate Form 2 Report First";
							 
						}else{
							 result ="Days Expired";
						}
					}else{
						 result = "Please  execute Control Account Procedure ...........";
						
					}
					
				}
			
			}
			
			if(formType.equals("summaryinformation")){
				log.info("verifyCrtlAccStatus-----"+verifyCrtlAccStatus);
				rs1 = st1.executeQuery(verifyCrtlAccStatus);
				if(rs1.next()){
					formstatus = rs1.getString("formstatus");
				}
				
				log.info("verifyform8Query-----"+verifyform8Query);
				rs = st.executeQuery(verifyform8Query);
				if(rs.next()){
					daysDiff=  Double.parseDouble(rs.getString("diff")); 
					if(daysDiff<4){
						 result ="Please Generate Form 8 Report First";
						 
					}else{
						 
						if(!formstatus.equals("F")){
						 result ="Days Expired";
						}
					}
				}else{

					log.info("verifyform2AQuery-----"+verifyform2AQuery);
					rs = st.executeQuery(verifyform2AQuery);
					if(rs.next()){
						daysDiff=  Double.parseDouble(rs.getString("diff")); 
						if(daysDiff<4){
							 result ="Please Generate Form 2A Report First";
							 
						}else{
							 result ="Days Expired";
						}
					}else{ 
						log.info("verifyform2Query-----"+verifyform2Query);
						rs = st.executeQuery(verifyform2Query);
						if(rs.next()){
							daysDiff=  Double.parseDouble(rs.getString("diff")); 
							if(daysDiff<4){
								 result ="Please Generate Form 2 Report First";
								 
							}else{
								 result ="Days Expired";
							}
						}else{
							 result = "Please  execute Control Account Procedure ...........";
							
						}
						
					}
				
				
				}
			 
			}
			
		}
		
		list.add(result);
		list.add(formstatus);
		
		
 	log.info("----daysDiff----- "+daysDiff+"--result--"+result);
	  
		
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
	log.info("CommonDAO :checkControlAccntStatus() leaving method"
			+ result);
	return list;
}   
    public int updateControlAccntStatus(String formType,String serialNo,String crtlAccFlag) {
		log.info("CommonDAO :updateControlAccntStatus() entering method ");
	   
		String selectQuery = "",updateQuery = "" , insertQry="";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int result =0, updateResult =0;
		   if(formType.equals("form2summary")){
			   insertQry = "insert into epis_control_account_tracking values("+serialNo+",'Form 2','Y',sysdate)";
			   selectQuery = "select 'X' as flag from epis_control_account_tracking where serialno="+serialNo+" and formtype='Form 2' and formstatus='Y'";
		   }else if(formType.equals("form2Asummary")){
			   insertQry = "insert into epis_control_account_tracking values("+serialNo+",'Form 2A','Y',sysdate)";
			   selectQuery = "select 'X' as flag from epis_control_account_tracking where serialno="+serialNo+" and formtype='Form 2A' and formstatus='Y'";
		   }else if(formType.equals("form8summary")){
			   selectQuery = "select 'X' as flag from epis_control_account_tracking where serialno="+serialNo+" and formtype='Form 8' and formstatus='Y'";
			   insertQry = "insert into epis_control_account_tracking values("+serialNo+",'Form 8','Y',sysdate)";
			}else{
				
				 selectQuery = "select 'X' as flag from epis_control_account_tracking where serialno="+serialNo+" and formtype='ControlAccountProccessing' and formstatus ='N'";
				 updateQuery = "update  epis_Control_Account_Tracking set formstatus='"+crtlAccFlag+"' where formtype='ControlAccountProccessing' and serialno="+serialNo;
					
			}
		  
		log.info("formType is--------- " + formType);     
		
		try {
		   con = commonDB.getConnection();
			st = con.createStatement();
			if(!formType.equals("")){
			rs = st.executeQuery(selectQuery); 
			if(rs.next()){ 
				log.info("Record is already there.........");	
			}else{ 
				log.info("insertQry is " + insertQry);
			result =  st.executeUpdate(insertQry); 
			}
			}else{
				rs = st.executeQuery(selectQuery); 
				if(rs.next()){ 
					log.info("Record is already there.........");	
				}else{ 
					log.info("updateQuery is " + updateQuery);
					 updateResult = st.executeUpdate(updateQuery); 
				}
				
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
		log.info("CommonDAO :updateControlAccntStatus() leaving method"
				+ result);
		return result;
	}
    
    public ArrayList getEmployeePersonalInfo(String pensionNo) {
		String sqlQuery = "";
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		String employeeName = "";
		long advance = 0, amount = 0, partAmount = 0;
		EmpMasterBean empInfo = null;
		ArrayList al = new ArrayList();
		sqlQuery = "SELECT PENSIONNO, CPFACNO, EMPLOYEENO ,FHNAME,EMPLOYEENAME,DESEGNATION,DATEOFBIRTH, DATEOFJOINING , WETHEROPTION,EMPLOYEENO, REGION ,AIRPORTCODE FROM EMPLOYEE_PERSONAL_INFO WHERE PENSIONNO="
				+ pensionNo;
		 log.info("CommonDAO::getEmployeePersonalInfo" + sqlQuery);
		try {
			 con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				empInfo = new EmpMasterBean();
				if (rs.getString("PENSIONNO") != null) {
					empInfo.setEmpSerialNo(rs.getString("PENSIONNO"));
				}
				if (rs.getString("CPFACNO") != null) {
					empInfo.setCpfAcNo(rs.getString("CPFACNO"));
				}				
				if (rs.getString("EMPLOYEENO") != null) {
					empInfo.setEmpNumber(rs.getString("EMPLOYEENO"));
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empInfo.setEmpName(rs.getString("EMPLOYEENAME"));
				}
				if (rs.getString("FHNAME") != null) {
					empInfo.setFhName(rs.getString("FHNAME"));
				}
				if (rs.getString("DESEGNATION") != null) {
					empInfo.setDesegnation(rs.getString("DESEGNATION"));
				}
				if (rs.getString("DATEOFBIRTH") != null) {
					empInfo.setDateofBirth(CommonUtil.getDatetoString(rs.getDate("DATEOFBIRTH"),"dd-MMM-yyyy"));
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empInfo.setDateofJoining(CommonUtil.getDatetoString(rs.getDate("DATEOFJOINING"),"dd-MMM-yyyy"));
				}
				if (rs.getString("WETHEROPTION") != null) {
					empInfo.setWetherOption(rs.getString("WETHEROPTION"));
				}
				if (rs.getString("EMPLOYEENO") != null) {
					empInfo.setEmpNumber(rs.getString("EMPLOYEENO"));
				}
				if (rs.getString("REGION") != null) {
					empInfo.setRegion(rs.getString("REGION"));
				}
				if (rs.getString("AIRPORTCODE") != null) {
					empInfo.setStation(rs.getString("AIRPORTCODE"));
				}
				al.add(empInfo);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return al;
	}
//	 By Radha On 04-Feb-2012 for Checking User Screen Access Rights
	public String chkScreenAccessRights(String userId,String accessCode) {	 	 
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  result="";
		try {
			con = DBUtils.getConnection();;
			st = con.createStatement();
			  
			String query = "select 'X' as flag from pension_user_access_mt where access_cd='"
					+ accessCode + "'   and   userid ='"+userId.trim()+"'"; 
			log.info("CommonUtil::chkScreenAccessRights()==query====" + query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				result="Having";
			}else{
				result="NotHaving";
			}

		} catch (Exception e) {
			log.info("<<<<<<<<<< Exception  >>>>>>>>>>>>" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("====result===="+result);
		
		return result;
	}
    
//	 By Radha On 04-Feb-2012  for Checking User Screen Access Rights
	public String chkStageWiseprocessinAdjCalc(String pensionno) {
		ArrayList stationList = new ArrayList();		 
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  verifiedby="";
		int i=0;
		try {
			con = DBUtils.getConnection();;
			st = con.createStatement();  
			  
			String query = "select DISTINCT  APPROVEDTYPE from EPIS_ADJ_CRTN_TRANSACTIONS where pensionno='"+pensionno+"'"; 
			log.info("CommonDAO::chkStageWiseprocessinAdjCalc()==query====" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				
				if(rs.getString("APPROVEDTYPE")!=null){
					log.info("====rs===="+rs.getString("APPROVEDTYPE"));
					if(i==0){
					verifiedby = rs.getString("APPROVEDTYPE"); 
					}else{
						verifiedby =verifiedby+","+rs.getString("APPROVEDTYPE"); 
					}
				}
				i++;
			} 
			if(verifiedby.equals("Initial,Approved")){
				verifiedby="Approved";
				}
		} catch (Exception e) {
			log.info("<<<<<<<<<< Exception  >>>>>>>>>>>>" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("====verifiedby===="+verifiedby);
		
		return verifiedby;
	}
	
//	 By Radha On 04-Feb-2012  for Loading staions based on Profile(SAU/RAU) basis 
	public String getAccountType(String region,String station) {		 		 
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String  accountype="";
		try {
			con = DBUtils.getConnection();;
			st = con.createStatement();
			  
			String query = "select accounttype from employee_unit_master where UPPER(region)='"
					+ region.toUpperCase().trim() + "'   and  UPPER(unitname) ='"+station.toUpperCase().trim()+"'";
			
			log.info("CommonUtil::getAccountType()==query====" + query);
			rs = st.executeQuery(query);
			if (rs.next()) {
				accountype = rs.getString("accounttype");
				 
			}

		} catch (Exception e) {
			log.info("<<<<<<<<<< Exception  >>>>>>>>>>>>" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return accountype;
	}
	public int numOfMnthFinalSettlement(String finalSettlementDate) {
		int noOfMonths = 0;
		if (finalSettlementDate.toUpperCase().equals("APR")) {
			noOfMonths = 1;
		} else if (finalSettlementDate.toUpperCase().equals("MAY")) {
			noOfMonths = 2;
		} else if (finalSettlementDate.toUpperCase().equals("JUN")) {
			noOfMonths = 3;
		} else if (finalSettlementDate.toUpperCase().equals("JUL")) {
			noOfMonths = 4;
		} else if (finalSettlementDate.toUpperCase().equals("AUG")) {
			noOfMonths = 5;
		} else if (finalSettlementDate.toUpperCase().equals("SEP")) {
			noOfMonths = 6;
		} else if (finalSettlementDate.toUpperCase().equals("OCT")) {
			noOfMonths = 7;
		} else if (finalSettlementDate.toUpperCase().equals("NOV")) {
			noOfMonths = 8;
		} else if (finalSettlementDate.toUpperCase().equals("DEC")) {
			noOfMonths = 9;
		} else if (finalSettlementDate.toUpperCase().equals("JAN")) {
			noOfMonths = 10;
		} else if (finalSettlementDate.toUpperCase().equals("FEB")) {
			noOfMonths = 11;
		} else if (finalSettlementDate.toUpperCase().equals("MAR")) {
			noOfMonths = 12;
		}

		return noOfMonths;
	}
	public int getNoOfMonthsForPFID(String formFromSelectDate,String toDate) {
		log
				.info("=====================getNoOfMonthsForEachPFID===============================");
		String currentDt = "", findCurrentYear = "", findToMonth = "",formToDate="", findCurretntMonth = "", findFormYear = "";
		currentDt = commonUtil.getCurrentDate("dd-MMM-yyyy");
		log
		.info("=====================getNoOfMonthsForEachPFID==============================="+compareFinalSettlementDates(formFromSelectDate,toDate,currentDt));
		if(compareFinalSettlementDates(formFromSelectDate,toDate,currentDt)==true){
			currentDt=currentDt;
		}else{
			currentDt=toDate;
			
		}
		
		int noOfMonths = 0, findFormToYear = 0,difference=0;
		try {
			findFormYear = commonUtil.converDBToAppFormat(formFromSelectDate,
					"dd-MMM-yyyy", "yyyy");
		
			findCurrentYear = commonUtil.converDBToAppFormat(currentDt,
					"dd-MMM-yyyy", "yyyy");
			findCurretntMonth = commonUtil.converDBToAppFormat(currentDt,
					"dd-MMM-yyyy", "MM");
			findCurretntMonth = commonUtil.converDBToAppFormat(currentDt,
					"dd-MMM-yyyy", "MM");
			findFormToYear=Integer.parseInt(findFormYear)+1;
			log.info("======findFormYear========" + findFormYear);
			log.info("======findCurrentYear========" + findCurrentYear);
			log.info("======findCurretntMonth========" + findCurretntMonth);
			log.info("======findCurretntMonth========" + findFormToYear);
			
			/*if ((Integer.parseInt(findCurrentYear) <= Integer
					.parseInt(findFormYear) && Integer.parseInt(findCurretntMonth)>=04 && Integer.parseInt(findCurretntMonth)<=12)||(Integer.parseInt(findCurrentYear) >= findFormToYear && Integer.parseInt(findCurretntMonth)<=2)) {
				noOfMonths = this.numOfMnthFinalSettlement(commonUtil
						.converDBToAppFormat(commonUtil
								.getCurrentDate("dd-MMM-yyyy"), "dd-MMM-yyyy",
								"MMM"));
			} else {
				noOfMonths = 12;
			}*/
			if(Integer.parseInt(findCurretntMonth)>=04 && Integer.parseInt(findCurretntMonth)<=12){
				difference=(Integer.parseInt(findCurrentYear))-Integer.parseInt(findFormYear);
				
			}else if(Integer.parseInt(findCurretntMonth)>=01 && Integer.parseInt(findCurretntMonth)<=03){
				difference=(Integer.parseInt(findCurrentYear))-findFormToYear;
			}
			if(difference==0){
				noOfMonths = this.numOfMnthFinalSettlement(commonUtil
						.converDBToAppFormat(currentDt,"dd-MMM-yyyy",
								"MMM"));
			}else{
				noOfMonths = 12;
			}
			
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		}
		log.info("===getNoOfMonthsForPFID===noOfMonths========" + noOfMonths+"difference"+difference);
		return noOfMonths;
	}
	public String getEmployeeRateOfInterest(Connection con, String getTranDate) {
		String sqlQuery = "", interestRate = "", toDate = "", fromDate = "";

		int transAddYears = 0;
		// log.info("getEmployeeRateOfInterest" + getTranDate);
		if (Integer.parseInt(getTranDate) >= 1995
				&& Integer.parseInt(getTranDate) < 2000) {

			fromDate = "Apr-1995";
			toDate = "Mar-2000";
		} else if (Integer.parseInt(getTranDate) >= 2000
				&& Integer.parseInt(getTranDate) < 2001) {

			fromDate = "Apr-2000";
			toDate = "Mar-2001";
		} else if (Integer.parseInt(getTranDate) >= 2001
				&& Integer.parseInt(getTranDate) < 2005) {
			fromDate = "Apr-2001";
			toDate = "Mar-2005";
		} else if (Integer.parseInt(getTranDate) >= 2005
				&& Integer.parseInt(getTranDate) < 2009) {
			fromDate = "Apr-2005";
			toDate = "Mar-2009";
		} else if (Integer.parseInt(getTranDate) >= 2009
				&& Integer.parseInt(getTranDate) < 2010) {
			fromDate = "Apr-2009";
			toDate = "Mar-2010";
		} else if (Integer.parseInt(getTranDate) >= 2010
				&& Integer.parseInt(getTranDate) < 2011) {
			fromDate = "Apr-2010";
			toDate = "Mar-2011";
		} else if (Integer.parseInt(getTranDate) >= 2011
				&& Integer.parseInt(getTranDate) < 2012) {
			fromDate = "Apr-2011";
			toDate = "Mar-2012";
		} else if (Integer.parseInt(getTranDate) >= 2010
				&& Integer.parseInt(getTranDate) < 2011) {
			fromDate = "Apr-2010";
			toDate = "Mar-2011";
		} else if (Integer.parseInt(getTranDate) >= 2011
				&& Integer.parseInt(getTranDate) < 2012) {
			fromDate = "Apr-2011";
			toDate = "Mar-2012";
		}else if (Integer.parseInt(getTranDate) >= 2012
				&& Integer.parseInt(getTranDate) < 2013) {
			fromDate = "Apr-2012";
			toDate = "Mar-2013";
		} else if (Integer.parseInt(getTranDate) >= 2013
				&& Integer.parseInt(getTranDate) < 2014) {
			fromDate = "Apr-2013";
			toDate = "Mar-2014";
		} else if (Integer.parseInt(getTranDate) >= 2014
				&& Integer.parseInt(getTranDate) < 2015) {
			fromDate = "Apr-2014";
			toDate = "Mar-2015";
		} else {
			fromDate = "Apr-1995";
			toDate = "Mar-2009";
		}

		Statement st = null;
		ResultSet rs = null;

		ArrayList list = new ArrayList();

		sqlQuery = "SELECT CEILINGRATE,TO_CHAR(WITHEFFCTDATE,'Mon-yyyy') AS FROMYEAR,TO_CHAR(WITHEFFCTTODATE,'Mon-yyyy') AS TOYEAR FROM EMPLOYEE_CEILING_MASTER WHERE TO_CHAR(WITHEFFCTDATE,'Mon-yyyy')>='"
				+ fromDate
				+ "' AND TO_CHAR(WITHEFFCTTODATE,'Mon-yyyy')<='"
				+ toDate + "'";
		log.info("CommonDAO::getEmployeeRateOfInterest" + sqlQuery);
		try {

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {

				if (rs.getString("CEILINGRATE") != null) {
					interestRate = rs.getString("CEILINGRATE");
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return interestRate;
	}
	public String getRetriedDate(String dateOfBirth)	throws InvalidDataException {
			dateOfBirth = commonUtil.converDBToAppFormat(dateOfBirth,"dd-MMM-yyyy", "dd-MM-yyyy");
	String[] dobElements = dateOfBirth.split("-");
	int day = 0, month = 0, year = 0;
	String retriedDt = "", formatedRtDt = "";
	day = Integer.parseInt(dobElements[0]);
	month = Integer.parseInt(dobElements[1]);
	year = Integer.parseInt(dobElements[2]);
	if (day >= 1) {
		month = month + 1;
		day = 1;
	}
	year = year + 58;
	formatedRtDt = day + "-" + month + "-" + year;
	retriedDt = commonUtil.converDBToAppFormat(formatedRtDt, "dd-MM-yyyy",
			"dd-MMM-yyyy");
	return retriedDt;
	}
		public int getArrearInfo(Connection con, String fromYear, String toYear,
				String pensionNo) {
			int noOfMonths = 0;
			PreparedStatement pst = null;
			ResultSet rs = null;
			String sqlQuery = "", finalFromDate = "";
			log.info("::getArrearInfo=" + fromYear + "toYear" + toYear
					+ "pensionNo" + pensionNo);
			sqlQuery = "SELECT round(months_between(LAST_DAY(?),ARREARDATE)) AS MONTHS FROM EMPLOYEE_PENSION_ARREAR WHERE  (ARREARDATE BETWEEN ? AND ?) AND  PENSIONNO=?";
			log
					.info("CommonDAO:::getArrearInfo=================================="
							+ sqlQuery);
			try {

				pst = con.prepareStatement(sqlQuery);
				pst.setDate(1, java.sql.Date.valueOf(commonUtil
						.converDBToAppFormat(toYear, "dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setDate(2, java.sql.Date
						.valueOf(commonUtil.converDBToAppFormat(fromYear,
								"dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setDate(3, java.sql.Date.valueOf(commonUtil
						.converDBToAppFormat(toYear, "dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setString(4, pensionNo);
				rs = pst.executeQuery();
				if (rs.next()) {
					noOfMonths = rs.getInt("MONTHS");
				} else {
					noOfMonths = 0;
				}
				/*
				 * log
				 * .info("CommonDAO:::getArrearInfo=================================noOfMonths" +
				 * noOfMonths);
				 */
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (pst != null) {
					try {
						pst.close();
						pst = null;
					} catch (SQLException se) {
						log.printStackTrace(se);
					}
				}
				commonDB.closeConnection(null, null, rs);
			}
			return noOfMonths;
		}
		//		 By Radha On 03-Sep-2011 for displaying single line amounts in arrear
		// block in pf card
		public String getArrearData(Connection con, String fromYear, String toYear,
				String pensionNo) {
			int noOfMonths = 0;
			PreparedStatement pst = null;
			ResultSet rs = null;
			String sqlQuery = "", finalFromDate = "", arrearDate = "", arrearInfo = "";
			double arrearAmount = 0.00, arrearContr = 0.00;

			log.info("::getArrearInfo=" + fromYear + "toYear" + toYear
					+ "pensionNo" + pensionNo);
			sqlQuery = "SELECT NVL(ORIGINALARREARAMOUNT,'0.00') AS ORIGINALARREARAMOUNT,NVL(ORIGINALARREARCONTRIBUTION,'0.00') AS ORIGINALARREARCONTRIBUTION,TO_CHAR(ARREARDATE,'dd-Mon-yyyy') as ARREARDATE,round(months_between(LAST_DAY(?),ARREARDATE)) AS MONTHS FROM EMPLOYEE_PENSION_ARREAR WHERE  (ARREARDATE BETWEEN ? AND ?) AND  PENSIONNO=?";
			log
					.info("CommonDAO:::getArrearInfo=================================="
							+ sqlQuery);
			try {

				pst = con.prepareStatement(sqlQuery);
				pst.setDate(1, java.sql.Date.valueOf(commonUtil
						.converDBToAppFormat(toYear, "dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setDate(2, java.sql.Date
						.valueOf(commonUtil.converDBToAppFormat(fromYear,
								"dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setDate(3, java.sql.Date.valueOf(commonUtil
						.converDBToAppFormat(toYear, "dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setString(4, pensionNo);
				rs = pst.executeQuery();
				if (rs.next()) {
					noOfMonths = rs.getInt("MONTHS");
					arrearAmount = rs.getDouble("ORIGINALARREARAMOUNT");
					arrearContr = rs.getDouble("ORIGINALARREARCONTRIBUTION");
					arrearDate = rs.getString("ARREARDATE");
				} else {
					noOfMonths = 0;
					arrearAmount = 0.00;
					arrearContr = 0.00;
					arrearDate = "NA";
				}
				arrearInfo = arrearDate + "," + noOfMonths + "," + arrearAmount
						+ "," + arrearContr;
				log
						.info("CommonDAO:::getArrearInfo=================================arrearInfo"
								+ arrearInfo);
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (pst != null) {
					try {
						pst.close();
						pst = null;
					} catch (SQLException se) {
						log.printStackTrace(se);
					}
				}
				commonDB.closeConnection(null, null, rs);
			}
			return arrearInfo;
		}
//		By Radha on 28-Nov-2012 for proportionate calc of -ve emoluments for Option B Case as per Sehgal
//		By Radha on 12-Dec-2011 for rounding of Pension Contri if  emolument months is there 
		public double pensionCalculation(String monthYear, String emoluments,
				String penionOption, String region, String emolumentMonths) { 

			double penContrVal = 0.0;
			log.info("emolumentMonths " + emolumentMonths);
			if (!emolumentMonths.trim().equals("0.5")
					&& !emolumentMonths.trim().equals("1")
					&& !emolumentMonths.trim().equals("2")
					&& !emolumentMonths.trim().equals("90")
					&& !emolumentMonths.trim().equals("120")
					&& !emolumentMonths.trim().equals("150")
					&& !emolumentMonths.trim().equals("180")) {
				int getDaysBymonth = this.getNoOfDaysForsalRecovery(monthYear);
				emolumentMonths = String.valueOf(Double.parseDouble(emolumentMonths
						.trim())
						/ getDaysBymonth);
				DecimalFormat twoDForm = new DecimalFormat("#.####");
				// log.info("emolumentMonths "+emolumentMonths
				// +"getDaysBymonth "+getDaysBymonth);
				emolumentMonths = twoDForm.format(Double
						.parseDouble(emolumentMonths));
			} else if (emolumentMonths.trim().equals("90")
					|| emolumentMonths.trim().equals("120")
					|| emolumentMonths.trim().equals("150")
					|| emolumentMonths.trim().equals("180")) {
				if (emolumentMonths.trim().equals("90")) {
					emolumentMonths = "3";
				} else if (emolumentMonths.trim().equals("120")) {
					emolumentMonths = "4";
				} else if (emolumentMonths.trim().equals("150")) {
					emolumentMonths = "5";
				} else if (emolumentMonths.trim().equals("180")) {
					emolumentMonths = "6";
				} else if (emolumentMonths.trim().equals("210")) {
					emolumentMonths = "7";
				} else if (emolumentMonths.trim().equals("240")) {
					emolumentMonths = "8";
				} else if (emolumentMonths.trim().equals("270")) {
					emolumentMonths = "9";
				} else if (emolumentMonths.trim().equals("300")) {
					emolumentMonths = "10";
				} else if (emolumentMonths.trim().equals("330")) {
					emolumentMonths = "11";
				} else if (emolumentMonths.trim().equals("360")) {
					emolumentMonths = "12";
				} else if (emolumentMonths.trim().equals("390")) {
					emolumentMonths = "13";
				} else if (emolumentMonths.trim().equals("420")) {
					emolumentMonths = "14";
				} else if (emolumentMonths.trim().equals("450")) {
					emolumentMonths = "15";
				} else {
					emolumentMonths = "1";
				}
			}
			// log.info("emolumentMonths "+emolumentMonths);
			DecimalFormat df = new DecimalFormat("#########0.00");
			String sqlQuery = "", chkDecMnth = "";
			boolean dtFlag = false;
			long beginDate = 0, endDate = 0, empPenDt = 0, endNovMnthDt = 0, secBeginDate = 0, secEndDate = 0, secPenBeginDate = 0;
			if (monthYear.substring(0, 2).equals("01")) {
				dtFlag = true;
			}
			if (dtFlag == true) {
				beginDate = Date.parse("01-Apr-1995");
				endDate = Date.parse("01-Nov-1995");
				secBeginDate = Date.parse("01-Jan-1996");
				chkDecMnth = "01-dec-1995";
			} else {
				beginDate = Date.parse("30-Apr-1995");
				endDate = Date.parse("30-Nov-1995");
				secBeginDate = Date.parse("20-Jan-1996");
				chkDecMnth = "31-dec-1995";
			}
			String transMonthYear = "";
			try {
				transMonthYear = commonUtil.converDBToAppFormat(monthYear,
						"dd-MMM-yyyy", "-MMM-yyyy");
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (transMonthYear.equals("-Apr-1995")) {
				// log.info("transMonthYear in if " + transMonthYear + "region" +
				// region);
				penContrVal = 0.0;
				return penContrVal;
			}
			secPenBeginDate = Date.parse("01-Jul-2001");
			// secEndDate = Date.parse("31-Mar-2008");
			//secEndDate = Date.parse("31-Dec-2011");
			//secEndDate changed by prasanthi on 20-Jan-2012
			secEndDate = Date.parse(Constants.CALCULATED_PC_UPDATE);
			empPenDt = Date.parse(monthYear);

			if (empPenDt >= beginDate && empPenDt <= endDate) {
				
				log.info("if block : 1");
				// log.info("beginDate"+beginDate+"endDate"+endDate+"empPenDt"+
				// empPenDt);
				if (Double.parseDouble(emoluments) >= 5000) {
					emoluments = "5000";
				}
				penContrVal = new Double(df
						.format((Double.parseDouble(emoluments) * 1.16 * Double
								.parseDouble(emolumentMonths)) / 100f))
						.doubleValue() * 2;

			} else if (empPenDt >= secBeginDate && empPenDt <= secEndDate) {
				log.info("if block : 2");
				if (!penionOption.equals("---")) {
					if (penionOption.trim().equals("A")) {
						penContrVal = new Double(df.format((Double
								.parseDouble(emoluments) * 8.33) / 100f))
								.doubleValue();

					} else if (penionOption.trim().equals("B")
							|| penionOption.toLowerCase().trim().equals(
									"NO".toLowerCase())) {
						if (empPenDt >= Date.parse("30-Sep-2014")) {
							if (Double.parseDouble(emoluments) >= 15000) {
								penContrVal = Math.round((15000 * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f);
							//By Radha on 28-Nov-2012 for proportionate calc of -ve emoluments for Option B Case as per Sehgal 
							}else if(Double.parseDouble(emoluments)<0 && Double.parseDouble(emoluments)>-15000){								
							penContrVal = Math.round((Double.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f);
							} else if(Double.parseDouble(emoluments)<=-15000){
								penContrVal = Math.round((-15000 * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f);
								//------------------------------------
							}else if (Double.parseDouble(emoluments) < 15000 && Double.parseDouble(emoluments) >=0) {								
							penContrVal = Math.round(new Double(df.format((Double
									.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f))
									.doubleValue());							
							}
						}else

						if (empPenDt >= secPenBeginDate) {
							if (Double.parseDouble(emoluments) >= 6500) {
								penContrVal = Math.round((6500 * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f);
							//By Radha on 28-Nov-2012 for proportionate calc of -ve emoluments for Option B Case as per Sehgal 
							}else if(Double.parseDouble(emoluments)<0 && Double.parseDouble(emoluments)>-6500){								
							penContrVal = Math.round((Double.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f);
							} else if(Double.parseDouble(emoluments)<=-6500){
								penContrVal = Math.round((-6500 * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f);
								//------------------------------------
							}else if (Double.parseDouble(emoluments) < 6500 && Double.parseDouble(emoluments) >=0) {								
							penContrVal = Math.round(new Double(df.format((Double
									.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f))
									.doubleValue());							
							}
						} else {
							if (Double.parseDouble(emoluments) < 5000) {
								penContrVal = new Double(df.format((Double
										.parseDouble(emoluments) * 8.33* Double
										.parseDouble(emolumentMonths)) / 100f))
										.doubleValue();
							} else if (Double.parseDouble(emoluments) >= 5000) {
								penContrVal = (5000 * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f;

							}
						}

						// log.info("penionOption==="+penionOption+"Pension Total
						// Value"+penContrVal);
					}
				}
			} else if (monthYear.toLowerCase().equals(chkDecMnth)) {
				log.info("if block : 3");
				double salary = 0, perDaySal = 0, firstHalfMnthAmt = 0, secHalfMnthAmt = 0;

				if (penionOption.trim().equals("B")
						|| penionOption.toLowerCase().trim().equals(
								"NO".toLowerCase())) {
					if (Double.parseDouble(emoluments) >= 5000) {
						emoluments = "5000";
					}
				}
				/*
				 * Issue fixed by suresh kumar on 18-Aug-2009 Issue_no:86
				 */
				salary = new Double(df.format(Double.parseDouble(emoluments)))
						.doubleValue();
				perDaySal = salary / 30;
				secHalfMnthAmt = Math.round(salary * 8.33 / 2 / 100);
				log.info("salary================" + salary + "==========" + salary
						* 8.33 / 2 / 100 + "secHalfMnthAmt" + secHalfMnthAmt);

				if (salary >= 5000) {
					salary = 5000;

				}
				firstHalfMnthAmt = (Math.round((salary * 1.16) / 2 / 100)) * 2;

				log.info("firstHalfMnthAmt" + firstHalfMnthAmt + "secHalfMnthAmt"
						+ secHalfMnthAmt);
				penContrVal = new Double(df.format(firstHalfMnthAmt
						+ secHalfMnthAmt)).doubleValue();

			}
			log.info("*******************" + monthYear + "====" + emoluments + " : penContrVal : " + penContrVal
					+ "penionOption" + penionOption);
			return penContrVal;
		}
		public int getNoOfDaysForsalRecovery(String salMonth) {
			String sqlQuery = "";
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
			int days = 0;

			sqlQuery = "SELECT to_char(add_months(LAST_DAY(TO_DATE('" + salMonth
					+ "')),-1),'dd') as days FROM DUAL";

			log.info("CommonDAO::getNoOfDays" + sqlQuery);
			try {
				con = commonDB.getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlQuery);
				if (rs.next()) {
					days = rs.getInt("days");
				}
				if (days == 29) {
					days = 29;
				}
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(con, st, rs);
			}
			return days;
		}
		public double calclatedPF(String monthYear, String dateOfRetriment,
				String dateOfBirth, String calEmoluments, String wetherOption,
				String region, String days1, String emolumentMonths) throws NumberFormatException, ParseException {
			
			log.info("monthyear====="+monthYear);
			log.info("dateOfRetriment====="+dateOfRetriment);
			log.info("dateOfBirth====="+dateOfBirth);
			log.info("calEmoluments====="+calEmoluments);
			log.info("wetherOption====="+wetherOption);
			log.info("region====="+region);
			log.info("days1====="+days1);
			log.info("emolumentMonths====="+emolumentMonths);
			
			
			long transMntYear = 0, empRetriedDt = 0;
			boolean flag = true, contrFlag = false, chkDOBFlag = false;
			double pensionVal = 0.00;
			String days = "", getMonth = "";
			int getDaysBymonth = 0;
			DecimalFormat df = new DecimalFormat("#########0.00");
			DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
			double pensionAsPerOption = 0.0, retriredEmoluments = 0.0;
			if (flag == true) {
				if (!monthYear.equals("-NA-") && !dateOfRetriment.equals("")) {
					transMntYear = Date.parse(monthYear);
					
					//SimpleDateFormat s = new SimpleDateFormat("dd-MMM-yyyy");
					SimpleDateFormat s = new SimpleDateFormat("ddMMyy");
					Date d = s.parse(dateOfRetriment);
					empRetriedDt = d.getTime();
					
					log.info("*********************************************");

					log.info("days " + days1);

					if (transMntYear > empRetriedDt) {
						contrFlag = true;
					} else if (transMntYear == 0 || empRetriedDt == 0) {
						contrFlag = false;
					}

					if (transMntYear != 0
							&& empRetriedDt != 0
							&& (Integer.parseInt(days1) >= 0 && Integer
									.parseInt(days1) <= 1)
							|| (Integer.parseInt(days1) < 0 && Integer
									.parseInt(days1) > -29)) {
						chkDOBFlag = true;
					}

				}
				// log.info("transMntYear"+transMntYear+"empRetriedDt"+
				// empRetriedDt);
				// log.info("contrFlag"+contrFlag+"chkDOBFlag"+chkDOBFlag);
				if (contrFlag != true) {
					pensionVal = this.pensionFormsCalculation(monthYear,
							calEmoluments, wetherOption.trim(), region, false,
							false, emolumentMonths);

				}
				if (chkDOBFlag == true) {
					String[] dobList = dateOfBirth.split("-");
					days = dobList[0];
					getDaysBymonth = this.getNoOfDays(dateOfBirth);
					pensionVal = pensionVal * (Double.parseDouble(days) - 1)
							/ getDaysBymonth;
					retriredEmoluments = new Double(df1.format(Double
							.parseDouble(calEmoluments)
							* (Double.parseDouble(days) - 1) / getDaysBymonth))
							.doubleValue();
					calEmoluments = Double.toString(retriredEmoluments);
					pensionVal = this.pensionFormsCalculation(monthYear,
							calEmoluments, wetherOption.trim(), region, false,
							false, emolumentMonths);

				}

			} else {
				pensionVal = 0;
			}
			return pensionVal;
		}
		public double pensionFormsCalculation(String monthYear, String emoluments,
				String penionOption, String region, boolean formFlag,
				boolean secondFormFlag, String emolumentMonths) {
			double penContrVal = 0.0;
			DecimalFormat df = new DecimalFormat("#########0.00");
			String chkDecMnth = "";
			boolean dtFlag = false;
			long beginDate = 0, endDate = 0, empPenDt = 0, secBeginDate = 0, secEndDate = 0;

			if (!emolumentMonths.trim().equals("0.5")
					&& !emolumentMonths.trim().equals("1")
					&& !emolumentMonths.trim().equals("2")
					&& !emolumentMonths.trim().equals("90")
					&& !emolumentMonths.trim().equals("120")
					&& !emolumentMonths.trim().equals("150")
					&& !emolumentMonths.trim().equals("180")) {
				int getDaysBymonth = this.getNoOfDaysForsalRecovery(monthYear);
				emolumentMonths = String.valueOf(Double.parseDouble(emolumentMonths
						.trim())
						/ getDaysBymonth);
				DecimalFormat twoDForm = new DecimalFormat("#.#####");
				log.info("emolumentMonths " + emolumentMonths + "getDaysBymonth "
						+ getDaysBymonth);
				emolumentMonths = twoDForm.format(Double
						.parseDouble(emolumentMonths));
			} else if (emolumentMonths.trim().equals("90")
					|| emolumentMonths.trim().equals("120")
					|| emolumentMonths.trim().equals("150")
					|| emolumentMonths.trim().equals("180")) {
				if (emolumentMonths.trim().equals("90")) {
					emolumentMonths = "3";
				} else if (emolumentMonths.trim().equals("120")) {
					emolumentMonths = "4";
				} else if (emolumentMonths.trim().equals("150")) {
					emolumentMonths = "5";
				} else if (emolumentMonths.trim().equals("180")) {
					emolumentMonths = "6";
				} else if (emolumentMonths.trim().equals("210")) {
					emolumentMonths = "7";
				} else if (emolumentMonths.trim().equals("240")) {
					emolumentMonths = "8";
				} else if (emolumentMonths.trim().equals("270")) {
					emolumentMonths = "9";
				} else if (emolumentMonths.trim().equals("300")) {
					emolumentMonths = "10";
				} else if (emolumentMonths.trim().equals("330")) {
					emolumentMonths = "11";
				} else if (emolumentMonths.trim().equals("360")) {
					emolumentMonths = "12";
				} else if (emolumentMonths.trim().equals("390")) {
					emolumentMonths = "13";
				} else if (emolumentMonths.trim().equals("420")) {
					emolumentMonths = "14";
				} else if (emolumentMonths.trim().equals("450")) {
					emolumentMonths = "15";
				} else {
					emolumentMonths = "1";
				}
			}
			if (monthYear.substring(0, 2).equals("01")) {
				dtFlag = true;
			}
			if (dtFlag == true) {
				beginDate = Date.parse("01-Apr-1995");
				endDate = Date.parse("01-Nov-1995");
				secBeginDate = Date.parse("01-Jan-1996");
				chkDecMnth = "01-dec-1995";
			} else {
				beginDate = Date.parse("30-Apr-1995");
				endDate = Date.parse("30-Nov-1995");
				secBeginDate = Date.parse("20-Jan-1996");
				chkDecMnth = "31-dec-1995";
			}
			String transMonthYear = "";
			try {
				transMonthYear = commonUtil.converDBToAppFormat(monthYear,
						"dd-MMM-yyyy", "-MMM-yyyy");
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (transMonthYear.equals("-Apr-1995")) {
				log.info("transMonthYear in if " + transMonthYear + "region"
						+ region);
				penContrVal = 0.0;
				return penContrVal;
			}
			// modifed from 2009 to 2011 on apr 15 th 2010
			//secEndDate = Date.parse("31-Dec-2011");
	       //secEndDate changed by prasanthi on 20-Jan-2012
			 secEndDate = Date.parse(Constants.CALCULATED_PC_UPDATE);
			empPenDt = Date.parse(monthYear);

			if (empPenDt >= beginDate && empPenDt <= endDate) {
				// log.info("beginDate"+beginDate+"endDate"+endDate+"empPenDt"+
				// empPenDt);
				if (Double.parseDouble(emoluments) >= 5000) {
					emoluments = "5000";
				}
			 
				penContrVal = new Double(df
						.format((Double.parseDouble(emoluments) * 1.16 * Double
								.parseDouble(emolumentMonths)) / 100f))
						.doubleValue() * 2;

			} else if (empPenDt >= secBeginDate && empPenDt <= secEndDate) {
				if (!penionOption.equals("---")) {
					if (penionOption.trim().equals("A")) {
						penContrVal = new Double(df.format((Double
								.parseDouble(emoluments) * 8.33) / 100f))
								.doubleValue();
						// log.info("penionOption==="+penionOption+"Pension Total
						// Value"+penContrVal);
					} else if (penionOption.trim().equals("B")
							|| penionOption.toLowerCase().trim().equals(
									"NO".toLowerCase())) {

						if (Double.parseDouble(emoluments) >= 6500) {
							//code changed for  pc calculated 2 months as 1083 insted of 1082,rectified using round 
							penContrVal = (Math.round((6500 * 8.33 ) / 100f)* Double
									.parseDouble(emolumentMonths));
						}else if(Double.parseDouble(emoluments)<0){
							penContrVal = Math.round((-6500 * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f);
						} else if (Double.parseDouble(emoluments) < 6500 && Double.parseDouble(emoluments) >=0) {
							penContrVal = Math.round(new Double(df.format((Double
									.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f))
									.doubleValue());
						}
					}
				}

			} else if (monthYear.toLowerCase().equals(chkDecMnth)) {
				double salary = 0, perDaySal = 0, firstHalfMnthAmt = 0, secHalfMnthAmt = 0;

				if (penionOption.trim().equals("B")
						|| penionOption.toLowerCase().trim().equals(
								"NO".toLowerCase())) {
					if (Double.parseDouble(emoluments) >= 5000) {
						emoluments = "5000";
					}
				}
				/*
				 * Issue fixed by suresh kumar on 18-Aug-2009 Issue_no:86
				 */
				salary = new Double(df.format(Double.parseDouble(emoluments)))
						.doubleValue();
				perDaySal = salary / 30;
				secHalfMnthAmt = Math.round(salary * 8.33 / 2 / 100);
				if (salary >= 5000) {
					salary = 5000;

				}
				firstHalfMnthAmt = (Math.round((salary * 1.16) / 2 / 100)) * 2;

				if (formFlag == true) {
					penContrVal = new Double(df.format(firstHalfMnthAmt))
							.doubleValue();
				} else if (secondFormFlag == true) {

					penContrVal = new Double(df.format(secHalfMnthAmt))
							.doubleValue();
				} else {
					penContrVal = new Double(df.format(firstHalfMnthAmt
							+ secHalfMnthAmt)).doubleValue();
				}

			}
			// log.info(monthYear+"===="+emoluments+"penContrVal"+penContrVal+
			// "penionOption"+penionOption);
			return penContrVal;
		}
//		 By Radha on 15-Dec-2011 for rectifying the case of leapYear 29-Feb
		public int getNoOfDays(String dateOfBirth) {
			String sqlQuery = "";
			Connection con = null;
			Statement st = null;
			ResultSet rs = null;
			int days = 0;
			sqlQuery =  "SELECT TO_CHAR(LAST_DAY(ADD_MONTHS('" + dateOfBirth+"',696)),'dd') as days FROM DUAL";

			log.info("CommonDAO::getNoOfDays" + sqlQuery);
			try {
				con = commonDB.getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlQuery);
				if (rs.next()) {
					days = rs.getInt("days");
				}
				if (days == 29) {
					days = 29;
				}
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(con, st, rs);
			}
			return days;
		}
		public int getEmployeeLoans(Connection con, String monthyear,
				String pfIDS, String flag, String pensionNo) {
			String sqlQuery = "";
			Statement st = null;
			ResultSet rs = null;
			String loanType = "";
			int rfSubamount = 0, nrfSubAmount = 0;
			int loanAmount = 0;
			if (!pfIDS.equals("")) {
				if (flag.equals("ADV.PAID")) {
					sqlQuery = "SELECT SUB_AMT AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE ("
							+ pfIDS
							+ ") AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE IN('RF','NRF')";
				} else if (flag.equals("ADV.DRAWN")) {
					sqlQuery = "SELECT CONT_AMT AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE ("
							+ pfIDS
							+ ") AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE='NRF'";
				} else {
					sqlQuery = "SELECT (CONT_AMT+SUB_AMT) AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE ("
							+ pfIDS
							+ ") AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE='NRF'";
				}
			} else {
				if (flag.equals("ADV.PAID")) {
					sqlQuery = "SELECT SUB_AMT AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE PENSIONNO="
							+ pensionNo
							+ " AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE IN('RF','NRF')";
				} else if (flag.equals("ADV.DRAWN")) {
					sqlQuery = "SELECT CONT_AMT AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE PENSIONNO="
							+ pensionNo
							+ " AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE='NRF'";
				} else {
					sqlQuery = "SELECT (CONT_AMT+SUB_AMT) AS SUAMNT,LOANTYPE AS LOANTYPE FROM EMPLOYEE_PENSION_LOANS WHERE WHERE PENSIONNO="
							+ pensionNo
							+ " AND to_char(LOANDATE,'dd-Mon-yy') like'%-"
							+ monthyear + "' AND LOANTYPE='NRF'";
				}
			}

			// log.info("CommonDAO::getEmployeeLoans" + sqlQuery);
			try {
				// con = commonDB.getConnection();
				st = con.createStatement();
				rs = st.executeQuery(sqlQuery);
				while (rs.next()) {
					if (rs.getString("SUAMNT") != null) {
						nrfSubAmount = nrfSubAmount + rs.getInt("SUAMNT");
					}
					/*
					 * log.info("In While rfSubamount" + rfSubamount +
					 * "nrfSubAmount" + nrfSubAmount);
					 */
				}
				/*
				 * log.info("rfSubamount" + rfSubamount + "nrfSubAmount" +
				 * nrfSubAmount); if (flag.equals("ADV.PAID")) { loanAmount =
				 * rfSubamount + nrfSubAmount; } else { loanAmount = nrfSubAmount; }
				 */

			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return nrfSubAmount;
		}
		public boolean compareFinalSettlementDates(String fromDate, String todate,
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
//		 By Radha On 15-Oct-2011 for using in CommonDAO
		public String preparedCPFString(String[] cpfacno, String[] regions) {
			String cpfstring = "";
			for (int i = 0; i < cpfacno.length; i++) {
				if (cpfstring.equals("")) {
					cpfstring = " (CPFACCNO='" + cpfacno[i] + "' AND REGION='"
							+ regions[i] + "') ";
				} else {
					cpfstring = cpfstring + " OR " + " (CPFACCNO='" + cpfacno[i]
							+ "' AND REGION='" + regions[i] + "') ";
				}

			}
			return cpfstring;
		}
		public ArrayList getMonthList(Connection con, String monthsInfo) {
			String[] twoMnths = monthsInfo.split("@");
			String firstDt = "", secondDt = "";
			String findMnth = "", foundDate = "";
			int diff = 0;
			ArrayList list = new ArrayList();
			ArrayList mnthBlckList = new ArrayList();
			int firstMnt = 0, secMnts = 0, prvMnth = 0, chkPrvsDt = 0;
			String tempMar = "", tempYear = "", tempFoundDate = "", tempDate = "", firstDtYear = "", secndDtYear = "", chkPrvDtYear = "";
			boolean tempMarFlag = false, breakNotMarFlag = false, hyPenFlag = false, needCheckFlag = false;
			String prvMnt = "", chkPrvMnt = "";
			for (int i = 0; i < twoMnths.length; i++) {
				findMnth = twoMnths[i].toString().trim();
				String[] splits = findMnth.split("#");

				if (splits.length == 2) {
					if (!splits[0].equals("")) {
						firstDt = splits[0];
						if (firstDt.lastIndexOf("*") != -1) {
							tempFoundDate = firstDt.substring(0, firstDt
									.lastIndexOf("*"));
						}
					} else {
						firstDt = "";
					}
					if (!splits[1].equals("")) {
						secondDt = splits[1];
						if (secondDt.lastIndexOf("*") != -1) {
							tempFoundDate = secondDt.substring(0, secondDt
									.lastIndexOf("*"));
						}
					} else {
						secondDt = "";
					}
				} else {
					if (!splits[0].equals("")) {
						firstDt = splits[0];
						secondDt = "";
					} else {
						firstDt = "";
					}
				}
				// log.info("firstDt"+firstDt+"secondDt"+secondDt+"tempFoundDate"+
				// tempFoundDate);

				if (!chkPrvMnt.equals("")) {
					if (chkPrvMnt.lastIndexOf("*") != -1) {
						chkPrvMnt = chkPrvMnt.substring(0, chkPrvMnt
								.lastIndexOf("*"));
						chkPrvsDt = Integer.parseInt(chkPrvMnt.substring(0, 2));
					} else if (chkPrvMnt.lastIndexOf("_") != -1) {
						chkPrvMnt = chkPrvMnt.substring(0, chkPrvMnt
								.lastIndexOf("_"));
						chkPrvsDt = Integer.parseInt(chkPrvMnt.substring(0, 2));
					} else {
						chkPrvsDt = Integer.parseInt(chkPrvMnt.substring(0, 2));
					}
					// log.info("firstDt"+firstDt);
					firstMnt = Integer.parseInt(firstDt.substring(0, 2));
					if (firstMnt < chkPrvsDt) {
						diff = chkPrvsDt - firstMnt;
					} else {
						diff = firstMnt - chkPrvsDt;
					}
					// log.info("firstMnt==================================="+firstMnt
					// );
					if (firstMnt == 4) {
						needCheckFlag = true;
						foundDate = chkPrvMnt;
					}
				}
				// log.info("chkPrvsDt"+chkPrvsDt+"firstMnt"+firstMnt+"needCheckFlag"
				// +needCheckFlag+"diff"+diff+"foundDate==="+foundDate);
				if (needCheckFlag != true) {
					if (!firstDt.equals("")) {
						if (firstDt.lastIndexOf("_") != -1) {
							String[] seprDt = firstDt.split("_");
							tempMar = seprDt[1];
							if (!secondDt.equals("")) {
								if (!tempMar.equals("")
										&& secondDt.substring(0, 2).equals(tempMar)) {
									tempFoundDate = secondDt;
								}

							}

						} else if (!secondDt.equals("")) {
							if (tempMar.equals("")) {
								if (secondDt.lastIndexOf("_") != -1) {
									String[] seprDt1 = secondDt.split("_");
									tempMar = seprDt1[1];
									prvMnt = seprDt1[0];
									breakNotMarFlag = false;
									hyPenFlag = true;
								}

							}
						}
					}

					// log.info("**************firstDt"+firstDt+"secondDt"+secondDt+
					// "hyPenFlag"+hyPenFlag+"breakNotMarFlag"+breakNotMarFlag);

					if (!tempMar.equals("") && tempFoundDate.equals("")
							&& hyPenFlag != true) {
						firstMnt = Integer.parseInt(firstDt.substring(0, 2));
						secMnts = Integer.parseInt(firstDt.substring(0, 2));
						tempYear = firstDt.substring(3, 5);
						// log.info("firstMnt" + firstMnt + "secMnts" + secMnts);
						if (firstMnt != 03) {
							foundDate = "02-" + tempYear;
							tempMarFlag = true;
						} else if (secMnts != 03) {
							foundDate = "02-" + tempYear;
							tempMarFlag = true;
						}
						tempDate = prvMnt;

					} else if (!tempMar.equals("") && breakNotMarFlag == true
							&& !prvMnt.equals("")) {
						// log.info("tempMar-----------"+tempMar+"prvMnt"+prvMnt);
						firstMnt = Integer.parseInt(firstDt.substring(0, 2));
						if (!secondDt.equals("")) {
							secMnts = Integer.parseInt(secondDt.substring(0, 2));
						}

						prvMnth = Integer.parseInt(prvMnt.substring(0, 2));

						if (prvMnth > firstMnt) {
							diff = prvMnth - firstMnt;
						} else {
							diff = firstMnt - prvMnth;
						}

						// log.info("tempMar-----------"+tempMar+"firstMnt"+firstDt+
						// "prvMnt"+prvMnth+"diff=="+diff);
						if (diff >= 2) {
							if (firstMnt == 4) {
								foundDate = prvMnt;
								tempMarFlag = true;
							}

						} else if (diff == 1 && firstMnt == 3) {
							foundDate = firstDt;
							tempMarFlag = true;
						}
						prvMnt = "";
						tempDate = "";
						breakNotMarFlag = false;
						hyPenFlag = false;
					} else if (!tempFoundDate.equals("") && breakNotMarFlag != true) {
						if (firstDt.lastIndexOf("*") != -1) {
							foundDate = tempFoundDate;
							tempMarFlag = true;
						} else if (secondDt.lastIndexOf("*") != -1) {
							chkPrvMnt = tempFoundDate;
							tempMarFlag = true;
						}

					}
					// log.info("tempMar********"+tempMar+"tempFoundDate"+
					// tempFoundDate+"foundDate"+foundDate+"prvMnt"+prvMnt);
					if (!firstDt.equals("") && !secondDt.equals("")) {
						firstMnt = Integer.parseInt(firstDt.substring(0, 2));
						secMnts = Integer.parseInt(secondDt.substring(0, 2));
						if (firstMnt < secMnts) {

							diff = secMnts - firstMnt;
						} else {
							diff = firstMnt - secMnts;
						}

					} else if (!firstDt.equals("") && secondDt.equals("")) {
						log.info("===firstMnt===   " + firstDt);
						firstMnt = Integer.parseInt(firstDt.substring(0, 2));
						secMnts = 0;
						diff = firstMnt - secMnts;
					}

					if (hyPenFlag == true) {
						breakNotMarFlag = true;
					}

					// log.info("firstMnt"+firstMnt+","+firstDt+"secMnts"+secondDt+","
					// +secMnts+"diff"+diff);

					if (tempMarFlag != true && hyPenFlag != true) {
						// log.info("tempMarFlag!=true");
						if (!chkPrvMnt.equals("")) {
							firstDtYear = firstDt.substring(firstDt
									.lastIndexOf("-") + 1, firstDt.length());
							secndDtYear = secondDt.substring(secondDt
									.lastIndexOf("-") + 1, secondDt.length());
							chkPrvDtYear = chkPrvMnt.substring(chkPrvMnt
									.lastIndexOf("-") + 1, chkPrvMnt.length());

						}
						// log.info("firstDtYear"+firstDtYear+"secndDtYear"+
						// secndDtYear+"chkPrvDtYear"+chkPrvDtYear);
						if (firstMnt == 3) {
							log.info("===firstMnt===");
							if (!chkPrvDtYear.equals(firstDtYear) && firstMnt > 3) {
								foundDate = chkPrvMnt;
							} else {
								foundDate = firstDt;
							}

						} else if (secMnts == 3) {
							if (!firstDtYear.equals(secndDtYear)) {
								if (!chkPrvDtYear.equals(secndDtYear)) {
									foundDate = firstDt;
								} else {
									foundDate = secondDt;
								}
							} else {
								foundDate = secondDt;
							}

							// log.info(secndDtYear+"===secMnts==="+chkPrvDtYear);

						} else if (secMnts == 0 && (firstMnt >= 1 && firstMnt <= 3)) {
							// log.info("secMnts==0 && (firstMnt>=1 &&
							// firstMnt<=3)")
							// ;
							foundDate = firstDt;

						} else if ((diff > 3 && diff <= 8) && (secMnts == 4)) {
							// log.info("(diff> 3 && diff<=8)&& (secMnts==4)");
							foundDate = firstDt;
						} else {
							// log.info("===Both Failed ===");
							if (diff != 1 && diff != 0 && secMnts > 3
									&& firstMnt < 3) {
								// log.info(
								// "diff!=1 && diff!=0 && secMnts>3 && firstMnt<3");
								foundDate = firstDt;
							} else if (diff != 1 && diff != 0 && firstMnt < 3) {
								// log.info("diff!=1 && diff!=0 && firstMnt<3");
								foundDate = secondDt;
							} else if (diff == 1 && firstMnt == 3) {
								// log.info("diff==1 && firstMnt==3");
								foundDate = firstDt;
							} else if (diff == 1 && secMnts == 3) {
								// log.info("diff==1 && secMnts==3");
								foundDate = secondDt;
							}
						}
					}
					// log.info("diff"+diff+"foundDate"+foundDate);
				} else {
					needCheckFlag = false;
					chkPrvMnt = "";
				}

				if (breakNotMarFlag != true) {
					tempMarFlag = false;
					tempMar = "";
					tempYear = "";
					tempFoundDate = "";
					prvMnt = "";

				}

				chkPrvMnt = secondDt;
				// log.info("Last secondDt"+secondDt);
				// log.info("i=="+i+twoMnths.length);
				if (i == (twoMnths.length - 1)) {
					if (!secondDt.equals("")) {
						foundDate = secondDt;
					} else {
						foundDate = firstDt;
					}
				}
				if (!foundDate.equals("")) {
					if (foundDate.lastIndexOf("*") != -1) {
						foundDate = foundDate.substring(0, foundDate
								.lastIndexOf("*"));
					}
					if (foundDate.lastIndexOf("_") != -1) {
						foundDate = foundDate.substring(0, foundDate
								.lastIndexOf("_"));
					}
					if (!list.contains(foundDate)) {
						list.add(foundDate);
					}

				}
				foundDate = "";

			}

			mnthBlckList = this.getRatesForBlockMnth(con, list);
			return mnthBlckList;
		}
		private ArrayList getRatesForBlockMnth(Connection con, ArrayList blockList) {
			ArrayList list = new ArrayList();
			int counter = 0;
			String interestRtMnt = "", getMnth = "", finalRtMnth = "", years = "", decremtnYear = "", finalRtrvdMthYear = "";
			try {

				for (int i = 0; i < blockList.size(); i++) {
					counter++;
					getMnth = (String) blockList.get(i);
					String getyear[] = getMnth.split("-");
					years = getyear[1];
					// log.info("=====================================" + years);
					if (years.equals("95")) {
						decremtnYear = years;

					} else {
						/*
						 * if(counter==blockList.size()){
						 * decremtnYear=Integer.toString(Integer.parseInt(years));
						 * }else{
						 */
						if (years.equals("00")) {
							decremtnYear = "99";
						} else {
							decremtnYear = Integer
									.toString(Integer.parseInt(years) - 1);
						}

						/* } */

						if (decremtnYear.length() < 2) {
							decremtnYear = "0" + decremtnYear;
						}

					}
					finalRtrvdMthYear = getyear[0] + "-" + decremtnYear;
					// log.info("getRatesForBlockMnth================="+
					// finalRtrvdMthYear);
					try {
						interestRtMnt = this.getEmployeeRateOfInterest(con,
								commonUtil.converDBToAppFormat(finalRtrvdMthYear,
										"MM-yy", "yyyy"));
						finalRtMnth = interestRtMnt + "," + getMnth;
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					log.info(finalRtMnth);
					list.add(finalRtMnth);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return list;
		}
		public String getBlockYear(String year, ArrayList blockList) {
			String bYear = "", breakYear = "";
			for (int by = 0; by < blockList.size(); by++) {
				bYear = (String) blockList.get(by);

				String[] bDate = bYear.split(",");

				if (year.equals(bDate[1])) {
					breakYear = bDate[1];
					breakYear = bYear;
					break;
				} else {
					breakYear = "03-96";
				}
			}
			return breakYear;
		}
		public ArrayList chkDuplicateMntsForCpfs(ArrayList list) {
			ArrayList finalList = new ArrayList();
			finalList = list;
			TempPensionTransBean bean = new TempPensionTransBean();
			log.info("List Size" + list.size() + "Final List Size"
					+ finalList.size());
			for (int i = 0; i < list.size(); i++) {
				bean = (TempPensionTransBean) list.get(i);
				if (i > 168) {
					System.out.println("Greter than Monthyear"
							+ bean.getMonthyear() + "i=" + i);
					if (bean.getMonthyear().trim().equals("-NA-")) {
						bean = null;
						finalList.set(i, bean);
					}
				} else {
					if (bean.getMonthyear().trim().equals("-NA-")) {
						bean.setMonthyear(bean.getGenMonthYear().toUpperCase());
						finalList.set(i, bean);
					}
				}

			}

			return finalList;
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
		public String getSanctionOrderInfo(Connection con, String fromYear,
				String toYear, String pensionNo) {
			PreparedStatement pst = null;
			ResultSet rs = null;
			FinalSettlementBean fsb =null;
			int count=0;
			String sanctionOrderInfo1="";
			String sqlQuery = "", finalFromDate = "", arrearDate = "", sanctionOrderInfo = "";
			String pensionno = "", sancationNo = "", sanctionDate = "", seperationreason = "", verfiedby = "", arrearType = "";

			log.info("::getArrearInfo=" + fromYear + "toYear" + toYear
					+ "pensionNo" + pensionNo);
			sqlQuery = "SELECT PENSIONNO,NSSANCTIONNO,SEPERATIONRESAON,TO_CHAR(NSSANCTIONEDDT,'dd-Mon-yyyy') AS NSSANCTIONEDDT,VERIFIEDBY,NSTYPE FROM EMPLOYEE_ADVANCE_NOTEPARAM WHERE  ((VERIFIEDBY='PERSONNEL,FINANCE,SRMGRREC,DGMREC,APPROVED') or (VERIFIEDBY='FINANCE,SRMGRREC,DGMREC,APPROVED')) AND DELETEFLAG='N' AND (NSSANCTIONEDDT BETWEEN add_months(?,-1) AND last_day(?)) AND  PENSIONNO=?  order by NSSANCTIONNO";
			log
					.info("CommonDAO:::getArrearInfo=================================="
							+ sqlQuery);
			try {

				pst = con.prepareStatement(sqlQuery);
				pst.setDate(1, java.sql.Date
						.valueOf(commonUtil.converDBToAppFormat(fromYear,
								"dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setDate(2, java.sql.Date.valueOf(commonUtil
						.converDBToAppFormat(toYear, "dd-MMM-yyyy", "yyyy-MM-dd")));
				pst.setString(3, pensionNo);
				rs = pst.executeQuery();
				while(rs.next()) {
					count=count+1;
					fsb = new FinalSettlementBean();
					pensionno = rs.getString("PENSIONNO");
					sancationNo = rs.getString("NSSANCTIONNO");
					sanctionDate = rs.getString("NSSANCTIONEDDT");
					seperationreason = rs.getString("SEPERATIONRESAON");
					verfiedby = rs.getString("VERIFIEDBY");
					arrearType = rs.getString("NSTYPE");
					sanctionOrderInfo = sancationNo + "," + sanctionDate;
					if(count==1)
					sanctionOrderInfo1=sanctionOrderInfo;
					if(count>1)
					sanctionOrderInfo=sanctionOrderInfo1+"@"+sanctionOrderInfo;
				}
				/*if (rs.next()) {
					pensionno = rs.getString("PENSIONNO");
					sancationNo = rs.getString("NSSANCTIONNO");
					sanctionDate = rs.getString("NSSANCTIONEDDT");
					seperationreason = rs.getString("SEPERATIONRESAON");
					verfiedby = rs.getString("VERIFIEDBY");
					arrearType = rs.getString("NSTYPE");
					sanctionOrderInfo = sancationNo + "," + sanctionDate;
				} else {
					pensionno = "";
					sancationNo = "";
					sanctionDate = "";
					seperationreason = "";
					verfiedby = "";
					arrearType = "";
				}*/

				log
						.info("CommonDAO:::getArrearInfo=================================arrearInfo"
								+ sanctionOrderInfo);
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				if (pst != null) {
					try {
						pst.close();
						pst = null;
					} catch (SQLException se) {
						log.printStackTrace(se);
					}
				}
				commonDB.closeConnection(null, null, rs);
			}
			return sanctionOrderInfo;
		}
		public ArrayList getFinalSettlement(Connection con, String fromYear,
				String toYear, String pfIDS, String pensionno,
				String finalsettlemntdt, String resettlmentdt,boolean crossfinyear,boolean isCrossingSepertionDt,String seperationDate,String chkPymntDtSprtinDT) {

			Statement st = null;
			ResultSet rs = null;
			String sqlQuery = "";
			String finalEmpSub = "", findFromYear = "", findToYear = "", finalAAICont = "", finalPensionCon = "", finalNetAmount = "", finalPurpose = "", finalPensionNo = "", finalCpfaccno = "", finalAirportcode = "", finalRegion = "";
			String finalStlmntDt = "", finalEmployeeNo = "", finalEmpName = "", finalSetlmntMsg = "", finalSetlmntNxtMonth = "", finalSetlmntEndMonth = "";
			String finalsttlmnnls = "",resettlmentdtls="", restlmnth = "", finalstlmntmnt = "",compareFinResltDates="";
			int noOfMonths = 0;
			boolean alreadyfinal=false;
			ArrayList finalSettlementList = new ArrayList();
			log.info("getFinalSettlement============"+resettlmentdt+"finalsettlemntdt"+finalsettlemntdt+"chkPymntDtSprtinDT================="+chkPymntDtSprtinDT);
			if (finalsettlemntdt.equals("---")) {
				finalsttlmnnls = "";
			} else {
				finalsttlmnnls = finalsettlemntdt;
			}
			if (resettlmentdt.equals("---")) {
				resettlmentdtls = "";
			} else {
				resettlmentdtls = resettlmentdt;
			}
			try {
				findFromYear = commonUtil.converDBToAppFormat(fromYear,
						"dd-MMM-yyyy", "yyyy");
				findToYear = commonUtil.converDBToAppFormat(toYear, "dd-MMM-yyyy",
						"yyyy");
				
				//newly added for finalsettlement interest calc cases up to current month req for harinder
				
				  /*	finalSetlmntEndMonth = commonUtil.converDBToAppFormat(toYear,




						"dd-MMM-yyyy", "MMM-yy");
*/
				 
			//increase fin year After mar month
				if(Integer.parseInt(findFromYear)>=2022){
					finalSetlmntEndMonth = commonUtil.converDBToAppFormat(commonUtil.getCurrentDate("dd-MMM-yyyy"),
				
						"dd-MMM-yyyy", "MMM-yy");
				}else{
					finalSetlmntEndMonth = commonUtil.converDBToAppFormat(toYear,
							"dd-MMM-yyyy", "MMM-yy");
				}
		
				if (!resettlmentdt.equals("---")) {
					restlmnth = commonUtil.converDBToAppFormat(resettlmentdt,
							"dd-MMM-yyyy", "MM");
				} else {
					restlmnth = "0";
				}
				if (!finalsettlemntdt.equals("---")) {
					
					
					finalstlmntmnt = commonUtil.converDBToAppFormat(
							finalsettlemntdt, "dd-MMM-yyyy", "MM");
					
				}
				// log.info("===================="+compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)+"crossfinyear================="+crossfinyear+"findFromYear"+findFromYear);
					
				if (!finalsettlemntdt.equals("---")) {
				if (!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt) && crossfinyear==false && Integer.parseInt(findFromYear)>=2011){
					
					alreadyfinal=true;
				}
				}
				log.info("======CommonDAO.getFinalSettlement()=============="+restlmnth+"alreadyfinal================="+alreadyfinal+"resettlmentdt"+resettlmentdt+"isCrossingSepertionDt"+isCrossingSepertionDt);
				if (!restlmnth.equals("0") && alreadyfinal==false) {
					/*noOfMonths = Integer.parseInt(restlmnth)
							- Integer.parseInt(finalstlmntmnt);*/
					if(Integer.parseInt(findFromYear)>=2013){
						System.out.println("getMonths2===");
					 noOfMonths = getMonths2(con,resettlmentdt,finalsettlemntdt); 
					}else{
						System.out.println("getMonths===");
						 noOfMonths = getMonths(con,resettlmentdt,finalsettlemntdt);
					}
					
				} else {
					noOfMonths = 0;
				}
				if(isCrossingSepertionDt==true){
					 if(alreadyfinal==false){
						 compareFinResltDates=finalsettlemntdt;
					 }else{
						 compareFinResltDates=resettlmentdt;
					 }
					 if(!compareFinResltDates.equals("---")){
						 if(Integer.parseInt(findFromYear)>=2013){
						 noOfMonths = getMonths2(con,seperationDate,compareFinResltDates); 
						 }else{
							 noOfMonths = getMonths(con,seperationDate,compareFinResltDates);  
						 }
					 }
					 
					 if(noOfMonths<0){
					
						 noOfMonths=0;
					 }
				}

			} catch (InvalidDataException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (Integer.parseInt(findFromYear) >= 2008) {
				/*
				 * sqlQuery = "SELECT
				 * PENSIONNO,CPFACCNO,EMPLOYEENO,PURPOSE,EMPLOYEENAME,FINEMP,(FINAAI-NVL(PENCON,'0.00'))
				 * AS FINAAI,PENCON,NETAMOUNT,SETTLEMENTDATE,AIRPORTCODE,REGION FROM
				 * EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO=" + pensionno + "
				 * AND SETTLEMENTDATE BETWEEN '"+fromYear+"' AND '"+toYear +"' AND
				 * SETTLEMENTDATE IS NOT NULL ORDER BY PENSIONNO";
				 */

				sqlQuery = "SELECT WITHOUTSUM.PENSIONNO AS PENSIONNO, WITHOUTSUM.CPFACCNO AS CPFACCNO, WITHOUTSUM.EMPLOYEENO AS EMPLOYEENO, WITHOUTSUM.PURPOSE AS PURPOSE, WITHOUTSUM.EMPLOYEENAME AS EMPLOYEENAME, SUMDATA.FINEMP AS FINEMP, SUMDATA.FINAAI AS FINAAI, SUMDATA.PENCON AS PENCON, SUMDATA.NETAMOUNT AS NETAMOUNT , "
						+ "SUMDATA.SETTLEMENTDATE AS SETTLEMENTDATE,(case when ((to_char(to_date('"+ finalsttlmnnls+"'),'MM'))=03) then   to_char(to_date(add_months('"+ finalsttlmnnls+"', 1)), 'Mon-yy')       else        to_char(add_months('"+ finalsttlmnnls+"', 1), 'Mon-yy') end) as NEXTFINALSTLMNTDT,(case when ((to_char(to_date('"+resettlmentdtls+"'),'MM'))=03) then   to_char(to_date(add_months('"+resettlmentdtls+"', 1)), 'Mon-yy')       else        to_char(add_months('"+resettlmentdtls+"', 1), 'Mon-yy') end) as NEXTRESTSTLMNTDT,to_char(add_months(SUMDATA.SETTLEMENTDATE,1),'Mon-yy') as NEXTMNTHINT, WITHOUTSUM.AIRPORTCODE AS AIRPORTCODE, WITHOUTSUM.REGION AS REGION FROM(SELECT PENSIONNO, CPFACCNO, EMPLOYEENO, PURPOSE, EMPLOYEENAME, AIRPORTCODE, REGION FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO = "
						+ pensionno
						+ " AND SETTLEMENTDATE "
						+ "BETWEEN '"
						+ fromYear
						+ "' AND LAST_DAY('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL AND ROWNUM=1 ORDER BY PENSIONNO) WITHOUTSUM, (SELECT PENSIONNO,MIN(SETTLEMENTDATE) AS SETTLEMENTDATE, ROUND(SUM(FINEMP)) AS FINEMP, ROUND(SUM((FINAAI - NVL(PENCON, '0.00')))) AS FINAAI, ROUND(SUM(PENCON)) AS PENCON, "
						+ "ROUND(SUM(NETAMOUNT)) AS NETAMOUNT FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO = "
						+ pensionno
						+ " AND SETTLEMENTDATE BETWEEN '"
						+ fromYear
						+ "' AND last_day('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL GROUP BY PENSIONNO) SUMDATA WHERE WITHOUTSUM.PENSIONNO=SUMDATA.PENSIONNO";
			} else {
				/*
				 * sqlQuery = "SELECT
				 * PENSIONNO,CPFACCNO,EMPLOYEENO,PURPOSE,EMPLOYEENAME,FINEMP,(FINAAI-NVL(PENCON,'0.00'))AS
				 * FINAAI,PENCON,NETAMOUNT,SETTLEMENTDATE,AIRPORTCODE,REGION FROM
				 * EMPLOYEE_PENSION_FINSETTLEMENT WHERE " + "(" + pfIDS + ") AND
				 * SETTLEMENTDATE BETWEEN '"+fromYear+"' AND '" +toYear+"' AND
				 * SETTLEMENTDATE IS NOT NULL ORDER BY PENSIONNO";
				 */
				sqlQuery = "SELECT WITHOUTSUM.PENSIONNO AS PENSIONNO, WITHOUTSUM.CPFACCNO AS CPFACCNO, WITHOUTSUM.EMPLOYEENO AS EMPLO, WITHOUTSUM.PURPOSE AS PURPOSE, WITHOUTSUM.EMPLOYEENAME AS EMPLOYEENAME, SUMDATA.FINEMP AS FINEMP, SUMDATA.FINAAI AS FINAAI, SUMDATA.PENCON AS PENCON, SUMDATA.NETAMOUNT AS NETAMOUNT , "
						+ "SUMDATA.SETTLEMENTDATE AS SETTLEMENTDATE,(case when ((to_char(to_date('"+ finalsttlmnnls+"'),'MM'))=03) then   to_char(to_date('"+ finalsttlmnnls+"'), 'Mon-yy')       else        to_char(add_months('"+ finalsttlmnnls+"', 1), 'Mon-yy') end) as NEXTFINALSTLMNTDT,(case when ((to_char(to_date('"+resettlmentdtls+"'),'MM'))=03) then   to_char(to_date('"+resettlmentdtls+"'), 'Mon-yy')       else        to_char(add_months('"+resettlmentdtls+"', 1), 'Mon-yy') end) as NEXTRESTSTLMNTDT,to_char(add_months(SUMDATA.SETTLEMENTDATE,1),'Mon-yy') as NEXTMNTHINT, WITHOUTSUM.AIRPORTCODE AS AIRPORTCODE, WITHOUTSUM.REGION AS REGION FROM(SELECT PENSIONNO, CPFACCNO, EMPLOYEENO, PURPOSE, EMPLOYEENAME, AIRPORTCODE, REGION FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE "
						+ "("
						+ pfIDS
						+ ")  AND SETTLEMENTDATE "
						+ "BETWEEN '"
						+ fromYear
						+ "' AND LAST_DAY('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL AND ROWNUM=1 ORDER BY PENSIONNO) WITHOUTSUM, (SELECT PENSIONNO,MIN(SETTLEMENTDATE) AS SETTLEMENTDATE,ROUND(SUM(FINEMP)) AS FINEMP, ROUND(SUM((FINAAI - NVL(PENCON, '0.00')))) AS FINAAI, ROUND(SUM(PENCON)) AS PENCON, "
						+ "ROUND(SUM(NETAMOUNT)) AS NETAMOUNT FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE "
						+ "("
						+ pfIDS
						+ ")  AND SETTLEMENTDATE BETWEEN '"
						+ fromYear
						+ "' AND last_day('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL GROUP BY PENSIONNO) SUMDATA WHERE WITHOUTSUM.PENSIONNO=SUMDATA.PENSIONNO";
			}

			log.info("getFinalSettlement" + sqlQuery);
			try {

				st = con.createStatement();
				rs = st.executeQuery(sqlQuery);
				while (rs.next()) {
					if (rs.getString("PENSIONNO") != null) {
						finalPensionNo = rs.getString("PENSIONNO");
					}
					if (rs.getString("CPFACCNO") != null) {
						finalCpfaccno = rs.getString("CPFACCNO");
					} else {
						finalCpfaccno = "---";
					}
					if (rs.getString("EMPLOYEENO") != null) {
						finalEmployeeNo = rs.getString("EMPLOYEENO");
					} else {
						finalEmployeeNo = "---";
					}
					if (rs.getString("PURPOSE") != null) {
						finalPurpose = rs.getString("PURPOSE");
					}
					if (rs.getString("EMPLOYEENAME") != null) {
						finalEmpName = rs.getString("EMPLOYEENAME");
					} else {
						finalEmpName = "---";
					}
					if (rs.getString("FINEMP") != null) {
						finalEmpSub = rs.getString("FINEMP");
					} else {
						finalEmpSub = "0";
					}
					if (rs.getString("FINAAI") != null) {
						finalAAICont = rs.getString("FINAAI");
					} else {
						finalAAICont = "0";
					}
					if (rs.getString("PENCON") != null) {
						finalPensionCon = rs.getString("PENCON");
					} else {
						finalPensionCon = "0";
					}
					if (rs.getString("NETAMOUNT") != null) {
						finalNetAmount = rs.getString("NETAMOUNT");
					} else {
						finalNetAmount = "0";
					}
					if (rs.getString("AIRPORTCODE") != null) {
						finalAirportcode = rs.getString("AIRPORTCODE");
					}
					if (rs.getString("REGION") != null) {
						finalRegion = rs.getString("REGION");
					}
					if (rs.getString("SETTLEMENTDATE") != null) {
						finalStlmntDt = commonUtil.getDatetoString(rs
								.getDate("SETTLEMENTDATE"), "dd-MMM-yyyy");
					}

					if (commonUtil.compareTwoDates("Mar-2011", commonUtil
							.converDBToAppFormat(fromYear, "dd-MMM-yyyy",
									"MMM-yyyy")) == true) {
						if (rs.getString("NEXTFINALSTLMNTDT") != null) {
							finalSetlmntNxtMonth = rs
									.getString("NEXTFINALSTLMNTDT");
						}
						if(alreadyfinal==false){
							if (!resettlmentdt.equals("---")) {
								finalSetlmntEndMonth = commonUtil.converDBToAppFormat(
										resettlmentdt, "dd-MMM-yyyy", "MMM-yy");
							}
						}else{
							finalSetlmntNxtMonth = rs.getString("NEXTRESTSTLMNTDT");
						}
					

					} else {
						if (rs.getString("NEXTMNTHINT") != null) {
							finalSetlmntNxtMonth = rs.getString("NEXTMNTHINT");
						}
					}
					//newly added for finalsettlement interest calc cases up to current month req for harinder
					/*	if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
						noOfMonths = getMonths(con,"31"+toYear.substring(2,11),resettlmentdt); 
						log.info("============No of Months:"+noOfMonths);
					}/* else {
						noOfMonths = 0;
					}*
					 * 
					 */
					//increase fin year After mar month
					
					if(Integer.parseInt(findFromYear)>=2022){
						
						if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
						//	if(finalsettlemntdt.equals("31-Mar-2014") && this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt) ){
							if((finalsettlemntdt.equals("31-Mar-2014")||finalsettlemntdt.equals("31-Mar-2015")||finalsettlemntdt.equals("31-Mar-2016")||finalsettlemntdt.equals("31-Mar-2017")||finalsettlemntdt.equals("31-Mar-2018") || finalsettlemntdt.equals("31-Mar-2019")|| finalsettlemntdt.equals("31-Mar-2020")|| finalsettlemntdt.equals("31-Mar-2021")|| finalsettlemntdt.equals("31-Mar-2022")) && this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt) ){	
								if(!resettlmentdt.equals("---")){
									//System.out.println("resettlement date not null");
								noOfMonths = getMonths1(con,resettlmentdt,finalsettlemntdt);
								}else{
									//System.out.println("resettlement date  null");
									noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt);
								}
							}else{
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),resettlmentdt);
							}
						//log.info("============No of Months:"+noOfMonths);
						} else if(this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&& resettlmentdt.equals("---")) {
						//log.info("============No of Months:Else::::"+noOfMonths);
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt); 
						}else if(finalsettlemntdt.equals("31-Mar-2013") ||finalsettlemntdt.equals("31-Mar-2014")||finalsettlemntdt.equals("31-Mar-2015")||finalsettlemntdt.equals("31-Mar-2016")||finalsettlemntdt.equals("31-Mar-2017")||finalsettlemntdt.equals("31-Mar-2018")||finalsettlemntdt.equals("31-Mar-2019")||finalsettlemntdt.equals("31-Mar-2020")||finalsettlemntdt.equals("31-Mar-2021")||finalsettlemntdt.equals("31-Mar-2022")){
						//log.info("============No of Months:Else::::31-Mar-2013"+noOfMonths);
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt);
						} else if(!restlmnth.equals("0") && alreadyfinal==false){
							
						}else {
						
							noOfMonths=0;
						}
						
					}else{
						if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
							if(chkPymntDtSprtinDT.equals("Y") && Integer.parseInt(findFromYear)>=2013){
								noOfMonths = getMonths1(con,"01"+seperationDate.substring(2,11),resettlmentdt); 
							}else{
							noOfMonths = getMonths(con,"31"+toYear.substring(2,11),resettlmentdt); 
							log.info("============No of Months:"+noOfMonths);
						} /*else {
							noOfMonths = 0;
						}*/
						
						}else if(Integer.parseInt(findFromYear)>=2014){
							noOfMonths = getMonths1(con,"01"+toYear.substring(2,11),finalsettlemntdt);
						}
					}

					if(Integer.parseInt(findFromYear)>=2022 && resettlmentdt.equals("---") && isCrossingSepertionDt){
						log.info(isCrossingSepertionDt+seperationDate+finalsettlemntdt+resettlmentdt);
						noOfMonths = getMonths2(con,seperationDate,finalsettlemntdt);
					}
					finalSetlmntMsg = "From " + finalSetlmntNxtMonth + " To "
							+ finalSetlmntEndMonth;
					if((isCrossingSepertionDt==true && chkPymntDtSprtinDT.equals("Y")) && noOfMonths>0){
						finalSetlmntEndMonth = commonUtil.converDBToAppFormat(
								seperationDate, "dd-MMM-yyyy", "MMM-yy");
						
						finalSetlmntMsg = "From " + finalSetlmntNxtMonth + " To "
						+ finalSetlmntEndMonth;
					}
					log.info("finalSetlmntMsg:commonDAO"+finalSetlmntMsg);

					finalSettlementList.add(finalPensionNo);
					finalSettlementList.add(finalCpfaccno);
					finalSettlementList.add(finalEmployeeNo);
					finalSettlementList.add(finalPurpose);
					finalSettlementList.add(finalEmpName);
					finalSettlementList.add(finalEmpSub);
					finalSettlementList.add(finalAAICont);
					finalSettlementList.add(finalPensionCon);
					finalSettlementList.add(finalNetAmount);
					finalSettlementList.add(finalAirportcode);
					finalSettlementList.add(finalRegion);
					finalSettlementList.add(finalStlmntDt);
					finalSettlementList.add(finalSetlmntMsg);
					finalSettlementList.add(Integer.toString(noOfMonths));

				}
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return finalSettlementList;
		}
//		By Prasad on 07-Apr-2012  
//		By Radha on 08-Feb-2012 for Calc Diff in Months
		public ArrayList getMltipleFinalSettlement(Connection con, String fromYear,
				String toYear, String pfIDS, String pensionno,
				String finalsettlemntdt, String resettlmentdt,boolean crossfinyear,boolean isCrossingSepertionDt,String seperationDate,String chkPymntDtSprtinDT) {

			Statement st = null;
			ResultSet rs = null;
			String sqlQuery = "";
			String finalEmpSub = "", findFromYear = "", findToYear = "", finalAAICont = "", finalPensionCon = "", finalNetAmount = "", finalPurpose = "", finalPensionNo = "", finalCpfaccno = "", finalAirportcode = "", finalRegion = "";
			String finalStlmntDt = "", finalEmployeeNo = "", finalEmpName = "", finalSetlmntMsg = "", finalSetlmntNxtMonth = "", finalSetlmntEndMonth = "";
			String finalsttlmnnls = "",resettlmentdtls="", restlmnth = "", finalstlmntmnt = "",compareFinResltDates="";
			int noOfMonths = 0;
			int count=0;
			boolean alreadyfinal=false;
			ArrayList finalSettlementList = new ArrayList();
			ArrayList list = new ArrayList();
			FinalSettlementBean fsb = null;
			log.info("getFinalSettlement============"+resettlmentdt+"finalsettlemntdt"+finalsettlemntdt+"chkPymntDtSprtinDT================="+chkPymntDtSprtinDT);
			if (finalsettlemntdt.equals("---")) {
				finalsttlmnnls = "";
			} else {
				finalsttlmnnls = finalsettlemntdt;
			}
			if (resettlmentdt.equals("---")) {
				resettlmentdtls = "";
			} else {
				resettlmentdtls = resettlmentdt;
			}
			try {
				findFromYear = commonUtil.converDBToAppFormat(fromYear,
						"dd-MMM-yyyy", "yyyy");
				findToYear = commonUtil.converDBToAppFormat(toYear, "dd-MMM-yyyy",
						"yyyy");
				
				//newly added for finalsettlement interest calc cases up to current month req for harinder
				
				  /*	finalSetlmntEndMonth = commonUtil.converDBToAppFormat(toYear,




						"dd-MMM-yyyy", "MMM-yy");
*/
				 
			//increase fin year After mar month
				if(Integer.parseInt(findFromYear)>=2019){
					finalSetlmntEndMonth = commonUtil.converDBToAppFormat(commonUtil.getCurrentDate("dd-MMM-yyyy"),
				
						"dd-MMM-yyyy", "MMM-yy");
				}else{
					finalSetlmntEndMonth = commonUtil.converDBToAppFormat(toYear,
							"dd-MMM-yyyy", "MMM-yy");
				}
		
				if (!resettlmentdt.equals("---")) {
					restlmnth = commonUtil.converDBToAppFormat(resettlmentdt,
							"dd-MMM-yyyy", "MM");
				} else {
					restlmnth = "0";
				}
				if (!finalsettlemntdt.equals("---")) {
					
					
					finalstlmntmnt = commonUtil.converDBToAppFormat(
							finalsettlemntdt, "dd-MMM-yyyy", "MM");
					
				}
				// log.info("===================="+compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)+"crossfinyear================="+crossfinyear+"findFromYear"+findFromYear);
					
				if (!finalsettlemntdt.equals("---")) {
				if (!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt) && crossfinyear==false && Integer.parseInt(findFromYear)>=2011){
					
					alreadyfinal=true;
				}
				}
				log.info("======CommonDAO.getFinalSettlement()=============="+restlmnth+"alreadyfinal================="+alreadyfinal+"resettlmentdt"+resettlmentdt+"isCrossingSepertionDt"+isCrossingSepertionDt);
				if (!restlmnth.equals("0") && alreadyfinal==false) {
					/*noOfMonths = Integer.parseInt(restlmnth)
							- Integer.parseInt(finalstlmntmnt);*/
					
					 noOfMonths = getMonths(con,resettlmentdt,finalsettlemntdt);
					 
					
				} else {
					noOfMonths = 0;
				}
				if(isCrossingSepertionDt==true){
					log.info("==werwer====CommonDAO.getFinalSettlement()=============="+noOfMonths);
					 if(alreadyfinal==false){
						 compareFinResltDates=finalsettlemntdt;
					 }else{
						 compareFinResltDates=resettlmentdt;
					 }
					 if(!compareFinResltDates.equals("---")){
						 if(Integer.parseInt(findFromYear)>=2013){
						 noOfMonths = getMonths2(con,seperationDate,compareFinResltDates); 
					 }else{
						 noOfMonths = getMonths(con,seperationDate,compareFinResltDates); 
					 }
					 }
					 if(noOfMonths<0){
					
						 noOfMonths=0;
					 }
				}

			} catch (InvalidDataException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (Integer.parseInt(findFromYear) >= 2008) {
				/*
				 * sqlQuery = "SELECT
				 * PENSIONNO,CPFACCNO,EMPLOYEENO,PURPOSE,EMPLOYEENAME,FINEMP,(FINAAI-NVL(PENCON,'0.00'))
				 * AS FINAAI,PENCON,NETAMOUNT,SETTLEMENTDATE,AIRPORTCODE,REGION FROM
				 * EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO=" + pensionno + "
				 * AND SETTLEMENTDATE BETWEEN '"+fromYear+"' AND '"+toYear +"' AND
				 * SETTLEMENTDATE IS NOT NULL ORDER BY PENSIONNO";
				 */

				sqlQuery = "SELECT WITHOUTSUM.PENSIONNO AS PENSIONNO, WITHOUTSUM.CPFACCNO AS CPFACCNO, WITHOUTSUM.EMPLOYEENO AS EMPLOYEENO, WITHOUTSUM.PURPOSE AS PURPOSE, WITHOUTSUM.EMPLOYEENAME AS EMPLOYEENAME, SUMDATA.FINEMP AS FINEMP, SUMDATA.FINAAI AS FINAAI, SUMDATA.PENCON AS PENCON, SUMDATA.NETAMOUNT AS NETAMOUNT , "
						+ "SUMDATA.SETTLEMENTDATE AS SETTLEMENTDATE,(case when ((to_char(to_date('"+ finalsttlmnnls+"'),'MM'))=03) then   to_char(to_date('"+ finalsttlmnnls+"'), 'Mon-yy')       else        to_char(add_months('"+ finalsttlmnnls+"', 1), 'Mon-yy') end) as NEXTFINALSTLMNTDT,(case when ((to_char(to_date('"+resettlmentdtls+"'),'MM'))=03) then   to_char(to_date('"+resettlmentdtls+"'), 'Mon-yy')       else        to_char(add_months('"+resettlmentdtls+"', 1), 'Mon-yy') end) as NEXTRESTSTLMNTDT,to_char(add_months(SUMDATA.SETTLEMENTDATE,1),'Mon-yy') as NEXTMNTHINT, WITHOUTSUM.AIRPORTCODE AS AIRPORTCODE, WITHOUTSUM.REGION AS REGION FROM(SELECT PENSIONNO, CPFACCNO, EMPLOYEENO, PURPOSE, EMPLOYEENAME, AIRPORTCODE, REGION FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO = "
						+ pensionno
						+ " AND SETTLEMENTDATE "
						+ "BETWEEN '"
						+ fromYear
						+ "' AND LAST_DAY('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL AND ROWNUM=1 ORDER BY PENSIONNO) WITHOUTSUM, (SELECT PENSIONNO,keyno,MIN(SETTLEMENTDATE) AS SETTLEMENTDATE, ROUND(SUM(FINEMP)) AS FINEMP, ROUND(SUM((FINAAI - NVL(PENCON, '0.00')))) AS FINAAI, ROUND(SUM(PENCON)) AS PENCON, "
						+ "ROUND(SUM(NETAMOUNT)) AS NETAMOUNT FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE PENSIONNO = "
						+ pensionno
						+ " AND SETTLEMENTDATE BETWEEN '"
						+ fromYear
						+ "' AND last_day('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL GROUP BY PENSIONNO,SETTLEMENTDATE,keyno) SUMDATA WHERE WITHOUTSUM.PENSIONNO=SUMDATA.PENSIONNO order by SUMDATA.SETTLEMENTDATE,SUMDATA.keyno";
			} else {
				/*
				 * sqlQuery = "SELECT
				 * PENSIONNO,CPFACCNO,EMPLOYEENO,PURPOSE,EMPLOYEENAME,FINEMP,(FINAAI-NVL(PENCON,'0.00'))AS
				 * FINAAI,PENCON,NETAMOUNT,SETTLEMENTDATE,AIRPORTCODE,REGION FROM
				 * EMPLOYEE_PENSION_FINSETTLEMENT WHERE " + "(" + pfIDS + ") AND
				 * SETTLEMENTDATE BETWEEN '"+fromYear+"' AND '" +toYear+"' AND
				 * SETTLEMENTDATE IS NOT NULL ORDER BY PENSIONNO";
				 */
				sqlQuery = "SELECT WITHOUTSUM.PENSIONNO AS PENSIONNO, WITHOUTSUM.CPFACCNO AS CPFACCNO, WITHOUTSUM.EMPLOYEENO AS EMPLO, WITHOUTSUM.PURPOSE AS PURPOSE, WITHOUTSUM.EMPLOYEENAME AS EMPLOYEENAME, SUMDATA.FINEMP AS FINEMP, SUMDATA.FINAAI AS FINAAI, SUMDATA.PENCON AS PENCON, SUMDATA.NETAMOUNT AS NETAMOUNT , "
						+ "SUMDATA.SETTLEMENTDATE AS SETTLEMENTDATE,(case when ((to_char(to_date('"+ finalsttlmnnls+"'),'MM'))=03) then   to_char(to_date('"+ finalsttlmnnls+"'), 'Mon-yy')       else        to_char(add_months('"+ finalsttlmnnls+"', 1), 'Mon-yy') end) as NEXTFINALSTLMNTDT,(case when ((to_char(to_date('"+resettlmentdtls+"'),'MM'))=03) then   to_char(to_date('"+resettlmentdtls+"'), 'Mon-yy')       else        to_char(add_months('"+resettlmentdtls+"', 1), 'Mon-yy') end) as NEXTRESTSTLMNTDT,to_char(add_months(SUMDATA.SETTLEMENTDATE,1),'Mon-yy') as NEXTMNTHINT, WITHOUTSUM.AIRPORTCODE AS AIRPORTCODE, WITHOUTSUM.REGION AS REGION FROM(SELECT PENSIONNO, CPFACCNO, EMPLOYEENO, PURPOSE, EMPLOYEENAME, AIRPORTCODE, REGION FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE "
						+ "("
						+ pfIDS
						+ ")  AND SETTLEMENTDATE "
						+ "BETWEEN '"
						+ fromYear
						+ "' AND LAST_DAY('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL AND ROWNUM=1 ORDER BY PENSIONNO) WITHOUTSUM, (SELECT PENSIONNO,MIN(SETTLEMENTDATE) AS SETTLEMENTDATE,ROUND(SUM(FINEMP)) AS FINEMP, ROUND(SUM((FINAAI - NVL(PENCON, '0.00')))) AS FINAAI, ROUND(SUM(PENCON)) AS PENCON, "
						+ "ROUND(SUM(NETAMOUNT)) AS NETAMOUNT FROM EMPLOYEE_PENSION_FINSETTLEMENT WHERE "
						+ "("
						+ pfIDS
						+ ")  AND SETTLEMENTDATE BETWEEN '"
						+ fromYear
						+ "' AND last_day('"
						+ toYear
						+ "') AND SETTLEMENTDATE IS NOT NULL GROUP BY PENSIONNO) SUMDATA WHERE WITHOUTSUM.PENSIONNO=SUMDATA.PENSIONNO ";
			}

			log.info("getFinalSettlement" + sqlQuery);
			try {

				st = con.createStatement();
				rs = st.executeQuery(sqlQuery);
				while (rs.next()) {
					count=count+1;
					if (rs.getString("PENSIONNO") != null) {
						finalPensionNo = rs.getString("PENSIONNO");
					}
					if (rs.getString("CPFACCNO") != null) {
						finalCpfaccno = rs.getString("CPFACCNO");
					} else {
						finalCpfaccno = "---";
					}
					if (rs.getString("EMPLOYEENO") != null) {
						finalEmployeeNo = rs.getString("EMPLOYEENO");
					} else {
						finalEmployeeNo = "---";
					}
					if (rs.getString("PURPOSE") != null) {
						finalPurpose = rs.getString("PURPOSE");
					}
					if (rs.getString("EMPLOYEENAME") != null) {
						finalEmpName = rs.getString("EMPLOYEENAME");
					} else {
						finalEmpName = "---";
					}
					if (rs.getString("FINEMP") != null) {
						finalEmpSub = rs.getString("FINEMP");
					} else {
						finalEmpSub = "0";
					}
					if (rs.getString("FINAAI") != null) {
						finalAAICont = rs.getString("FINAAI");
					} else {
						finalAAICont = "0";
					}
					if (rs.getString("PENCON") != null) {
						finalPensionCon = rs.getString("PENCON");
					} else {
						finalPensionCon = "0";
					}
					if (rs.getString("NETAMOUNT") != null) {
						finalNetAmount = rs.getString("NETAMOUNT");
					} else {
						finalNetAmount = "0";
					}
					if (rs.getString("AIRPORTCODE") != null) {
						finalAirportcode = rs.getString("AIRPORTCODE");
					}
					if (rs.getString("REGION") != null) {
						finalRegion = rs.getString("REGION");
					}
					if (rs.getString("SETTLEMENTDATE") != null) {
						finalStlmntDt = commonUtil.getDatetoString(rs
								.getDate("SETTLEMENTDATE"), "dd-MMM-yyyy");
					}

					if (commonUtil.compareTwoDates("Mar-2011", commonUtil
							.converDBToAppFormat(fromYear, "dd-MMM-yyyy",
									"MMM-yyyy")) == true) {
						if (rs.getString("NEXTFINALSTLMNTDT") != null) {
							finalSetlmntNxtMonth = rs
									.getString("NEXTFINALSTLMNTDT");
						}
						if(alreadyfinal==false){
							if (!resettlmentdt.equals("---")) {
								finalSetlmntEndMonth = commonUtil.converDBToAppFormat(
										resettlmentdt, "dd-MMM-yyyy", "MMM-yy");
							}
							
						}else{
							finalSetlmntNxtMonth = rs.getString("NEXTRESTSTLMNTDT");
						}
					System.out.println("Final Settlement -----------1");

					} else {
						System.out.println("Final Settlement -----------1-else");
						if (rs.getString("NEXTMNTHINT") != null) {
							finalSetlmntNxtMonth = rs.getString("NEXTMNTHINT");
						}
					}
					
					
					if(count>1){
						finalSetlmntNxtMonth = rs.getString("NEXTRESTSTLMNTDT");
						finalSetlmntEndMonth = commonUtil.converDBToAppFormat(
								toYear, "dd-MMM-yyyy", "MMM-yy");
						System.out.println(finalSetlmntMsg+"finemp::::::"+finalEmpSub+"findate:::::"+finalStlmntDt);
					}
					//newly added for finalsettlement interest calc cases up to current month req for harinder
					/*	if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
						noOfMonths = getMonths(con,"31"+toYear.substring(2,11),resettlmentdt); 
						log.info("============No of Months:"+noOfMonths);
					}/* else {
						noOfMonths = 0;
					}*
					 * 
					 */
					//increase fin year After mar month
					
					if(Integer.parseInt(findFromYear)>=2019){
						
						if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
							if((finalsettlemntdt.equals("31-Mar-2014")||finalsettlemntdt.equals("31-Mar-2015")||finalsettlemntdt.equals("31-Mar-2016")||finalsettlemntdt.equals("31-Mar-2017")||finalsettlemntdt.equals("31-Mar-2018")) && this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt) ){	
								if(!resettlmentdt.equals("---")){
									System.out.println("resettlement date not null");
								noOfMonths = getMonths1(con,resettlmentdt,finalsettlemntdt);
								}else{
									System.out.println("resettlement date  null");
									noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt);
								}
							}else{
								if(this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt)){
									noOfMonths = getMonths1(con,finalsettlemntdt,resettlmentdt);
								}else
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),resettlmentdt);
							}
						} else if(this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&& resettlmentdt.equals("---")) {
						log.info("============No of Months:Else::::"+noOfMonths);
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt); 
						}else if(finalsettlemntdt.equals("31-Mar-2013")){
						log.info("============No of Months:Else::::31-Mar-2013"+noOfMonths);
						noOfMonths = getMonths1(con,"01"+commonUtil.getCurrentDate("dd-MMM-yyyy").substring(2,11),finalsettlemntdt);
						} else if(!restlmnth.equals("0") && alreadyfinal==false){
							
						}else {
						
							noOfMonths=0;
						}
					}else{
						if((finalsettlemntdt.equals("31-Mar-2014")||finalsettlemntdt.equals("31-Mar-2015")||finalsettlemntdt.equals("31-Mar-2016")||finalsettlemntdt.equals("31-Mar-2017")||finalsettlemntdt.equals("31-Mar-2018")) && this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt) ){	
							if(!resettlmentdt.equals("---")){
								//System.out.println("resettlement date not null");
							noOfMonths = getMonths1(con,resettlmentdt,finalsettlemntdt);
							}
						}else if(!this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&&!resettlmentdt.equals("---")){
							noOfMonths = getMonths(con,"31"+toYear.substring(2,11),resettlmentdt); 
							log.info("============No of Months:"+noOfMonths);
						} else if(this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&& this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt)&& count==1){
							noOfMonths = getMonths(con,resettlmentdt,finalsettlemntdt); 	
						}else if(this.compareFinalSettlementDates(fromYear,toYear,finalsettlemntdt)&& this.compareFinalSettlementDates(fromYear,toYear,resettlmentdt) && count==2){
							if(isCrossingSepertionDt==true && alreadyfinal==true){
								log.info("============seprdate:"+seperationDate+"resettlmentdt:"+resettlmentdt);
							}else
							noOfMonths = getMonths(con,"31"+toYear.substring(2,11),resettlmentdt);	
						}
						/*else {
						}
							noOfMonths = 0;
						}*/
						
						
					}

					
					finalSetlmntMsg = "From " + finalSetlmntNxtMonth + " To "
							+ finalSetlmntEndMonth;
					if(isCrossingSepertionDt==true && chkPymntDtSprtinDT.equals("Y")){
						finalSetlmntEndMonth = commonUtil.converDBToAppFormat(
								seperationDate, "dd-MMM-yyyy", "MMM-yy");
						
						finalSetlmntMsg = "From " + finalSetlmntNxtMonth + " To "
						+ finalSetlmntEndMonth;
						
						System.out.println("Final Settlement -----------2");
					}
					log.info("finalSetlmntMsg:commonDAO"+finalSetlmntMsg);
     
			/*		finalSettlementList.add(finalPensionNo);
					finalSettlementList.add(finalCpfaccno);
					finalSettlementList.add(finalEmployeeNo);
					finalSettlementList.add(finalPurpose);
					finalSettlementList.add(finalEmpName);
					finalSettlementList.add(finalEmpSub);
					finalSettlementList.add(finalAAICont);
					finalSettlementList.add(finalPensionCon);
					finalSettlementList.add(finalNetAmount);
					finalSettlementList.add(finalAirportcode);
					finalSettlementList.add(finalRegion);
					finalSettlementList.add(finalStlmntDt);
					finalSettlementList.add(finalSetlmntMsg);
					finalSettlementList.add(Integer.toString(noOfMonths));*/
					
					fsb = new FinalSettlementBean();
					fsb.setPensionNo(finalPensionNo);
					fsb.setCpfNo(finalCpfaccno);
					fsb.setEmployeeNo(finalEmployeeNo);
					fsb.setPurpose(finalPurpose);
					fsb.setEmployeeName(finalEmpName);
					fsb.setFinEmp(finalEmpSub);
					fsb.setFinAai(finalAAICont);
					fsb.setPenCon(finalPensionCon);
					fsb.setNetAmount(finalNetAmount);
					fsb.setAirportCode(finalAirportcode);
					fsb.setRegion(finalRegion);
					fsb.setSettlementDate(finalStlmntDt);
					fsb.setFinalSettlementMsg(finalSetlmntMsg);
					fsb.setNoofMonths(Integer.toString(noOfMonths));
					log.info("Prasadddddddd"+finalSetlmntMsg+"finemp::::::"+finalEmpSub+"findate:::::"+finalStlmntDt);
					list.add(fsb);
				}
				log.info("Prasadddddddd"+list.size());
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return list;
			//return finalSettlementList;
		}
		public ArrayList getEmployeePFInfoPrinting(Connection con, String range,
				String region, String empNameFlag, String empName,
				String sortedColumn, String pensionno, String lastmonthFlag,
				String lastmonthYear, String airportcode, String fromYear,
				String toYear, String bulkPrintFlag) {
			CommonDAO commonDAO = new CommonDAO();
			String finalsettlemntyear="";
			Statement st = null;
			ResultSet rs = null;
			String sqlQuery = "",prefixSeperationDate="", pfidWithRegion = "",resettlementflag="N",processdate="",resettlementdate="", restrType = "", restrictionflag = "", findFromYear = "", findToYear = "", pensionAppednQry = "", finyear = "";
			EmployeePersonalInfo data = null;
			int restltmentYear = 0;
			ArrayList empinfo = new ArrayList();
			String freshOptQuery="",freshPensionOption="";
			Statement freshOptStmt=null;
			ResultSet freshOptrs=null;
			
			
			if (sortedColumn.toLowerCase().equals("cpfaccno")) {
				sortedColumn = "cpfacno";
			}
			try {
				findFromYear = commonUtil.converDBToAppFormat(fromYear,
						"dd-MMM-yyyy", "yyyy");
				findToYear = commonUtil.converDBToAppFormat(toYear, "dd-MMM-yyyy",
						"yyyy");
			} catch (InvalidDataException e2) {
				// TODO Auto-generated catch block
				log.printStackTrace(e2);
			}
			finyear = findFromYear + "-" + findToYear;
			try {

				st = con.createStatement();
				if (region.equals("NO-SELECT")) {
					region = "";
				}
				if (lastmonthFlag.equals("true")) {
					sqlQuery = this.buildQueryEmpPFTransInfoPrinting(range, region,
							empNameFlag, empName, sortedColumn, pensionno,
							lastmonthYear, airportcode, bulkPrintFlag, finyear,fromYear,toYear);
				} else {
					sqlQuery = this.buildQuerygetEmployeePFInfoPrinting(range,
							region, empNameFlag, empName, sortedColumn, pensionno,fromYear,toYear);
				}

				log.info("CommonDAO::getEmployeePFInfoPrinting" + sqlQuery);
				// obtaining fresh option 
				
				
				//log.info("CommonDAO::getEmployeePFInfoPrinting fresh option qury "+freshOptQuery+" freshoption: "+freshPensionOption);
				rs = st.executeQuery(sqlQuery);

				while (rs.next()) {
					data = new EmployeePersonalInfo();

					data = commonDAO.loadPersonalInfo(rs);
	
			
					
					if (rs.getString("deferement") != null) {
						data.setDeferment(rs.getString("deferement"));
					} else {
						data.setDeferment("---");
					}
      
			
				
			        
			        if (rs.getString("deferementpension") != null) {
			        	data.setDefermentOption(rs.getString("deferementpension"));
			        } else {

			        	data.setDefermentOption("---");
			        }
			      
			        
			      
			        
			        if(rs.getString("deferementage")!=null){
			        	data.setDeferementAge(rs.getString("deferementage"));
					}
			                
			        else {
			        	data.setDeferementAge("NO");
			        } 
			        
			        if(rs.getString("cadoption")!=null){
			        	data.setCadOption(rs.getString("cadoption"));
					}
			                
			        else {
			        	data.setCadOption("No");
			        }  
			        
						
				
					if(!data.getSeperationDate().equals("---") && !data.getSeperationReason().equals("---") && Integer.parseInt(findFromYear)>=2011){
						if(rs.getString("PREFIXSEPDATE")!=null){
							data.setFrozedSeperationDate(rs.getString("PREFIXSEPDATE"));
						}else{
							data.setFrozedSeperationDate("---");
						}
						// By Radha On 23-Nov-2012 as SEPERATION+ 3Years is ON  Mar  so CHKFRZNSEPERATION cmming as 0.9 By Sehgal Ex PFID 5007
					 	//By Radha On 17-Sep-2012 as SEPERATION+ 3Years is ON  Mar  so CHKFRZNSEPERATION cmming as 0.9 By Sehgal Ex PFID 10524
						if (rs.getDouble("CHKFRZNSEPERATION")<=0.9){
							data =getFrozedPFCardInfo(con,fromYear,toYear,data);
							data.setChksepmnths(rs.getDouble("CHKFRZNSEPERATION"));
						}
						
						
					}
					if(rs.getString("cmprpymntseprtndt")!=null){
						data.setChkPaymntSprtinDtFlag(rs.getString("cmprpymntseprtndt"));
					}
					if(rs.getString("pfwdisableFlag")!=null){
						data.setPfwdisableFlag(rs.getString("pfwdisableFlag"));
					}
					log.info("CommonDAO::compareTwoDates"+"fromYear"+fromYear+"data.getFrozedSeperationDate()"+data.getFrozedSeperationDate());
				
					if (this.checkDOB(data.getDateOfBirth().trim()) == true) {
						if (rs.getString("LASTDOB") != null) {
							data.setDateOfAnnuation(rs.getString("LASTDOB"));
						} else {
							data.setDateOfAnnuation("");
						}
					} else {
						if (rs.getString("DOR") != null) {
							data.setDateOfAnnuation(commonUtil.getDatetoString(rs
									.getDate("DOR"), "dd-MMM-yyyy"));
						} else {
							data.setDateOfAnnuation("");
						}
					}
					if (rs.getString("ADJDATE") != null) {
						data.setAdjDate(commonUtil.getDatetoString(rs
								.getDate("ADJDATE"), "dd-MMM-yyyy"));
						/*log.info("GEEEEEEEEEEEE"
								+ this.compareFinalSettlementDates(fromYear,
										toYear, data.getAdjDate()));*/
						if (commonDAO.compareFinalSettlementDates(fromYear, toYear, data
								.getAdjDate()) == true) {
							if (rs.getString("ADJAMOUNT") != null) {
								data.setAdjAmount(rs.getString("ADJAMOUNT"));
							} else {
								data.setAdjAmount("0");
							}
							if (rs.getString("ADJINT") != null) {
								data.setAdjInt(rs.getString("ADJINT"));
							} else {
								data.setAdjInt("0");
							}
							data.setAdjRemarks("Int. on Rs."
									+ data.getAdjAmount()
									+ " received in "
									+ commonUtil.getDatetoString(rs
											.getDate("ADJDATE"), "MMM-yyyy"));
						} else {
							data.setAdjDate("---");
							data.setAdjAmount("0");
							data.setAdjInt("0");
						}
					} else {
						data.setAdjDate("---");
						data.setAdjAmount("0");
						data.setAdjInt("0");
					}
				
					if (rs.getString("FINALSETTLMENTDT") != null) {
						data.setFinalSettlementDate(commonUtil.getDatetoString(rs
								.getDate("FINALSETTLMENTDT"), "dd-MMM-yyyy"));
					} else {
						data.setFinalSettlementDate("---");
					}
					log.info("Resettlement Date" + rs.getString("RESETTLEMENTDATE")
							+ "fromYear" + fromYear);
					if(!data.getFinalSettlementDate().equals("---")){
						finalsettlemntyear=commonUtil.converDBToAppFormat(data.getFinalSettlementDate(),"dd-MMM-yyyy", "yyyy");
					    int difference=Integer.parseInt(finalsettlemntyear)-Integer.parseInt(findFromYear);
						if(commonUtil.converDBToAppFormat(data.getFinalSettlementDate(),"dd-MMM-yyyy", "dd-MMM").equals("31-Mar") && difference==0){
							data.setCrossfinyear(true);
						}
					}
					
					if (rs.getString("resettlementflag") != null) {
						resettlementflag=rs.getString("resettlementflag");
						data.setResettlementFlag(resettlementflag);
					}
					
					if(!resettlementflag.equals("N")){
						if(resettlementflag.equals("S")){
							if (rs.getString("RESETTLEMENTDATE") != null) {
								
								resettlementdate=commonUtil
								.getDatetoString(rs
										.getDate("RESETTLEMENTDATE"),
										"dd-MMM-yyyy");
							}else{
								resettlementdate="---";
							}
						}else{
							resettlementdate=getResettlementDtByDate(con,data.getOldPensionNo(),fromYear,toYear);
						}
					}else{
						resettlementdate="---";
					}
					if(!resettlementdate.equals("---")){
					
						
						if (commonDAO.compareFinalSettlementDates(fromYear, toYear,resettlementdate) == true) {
							data.setResttlmentDate(resettlementdate);
						} else {
							if(resettlementdate.substring(0,6).equals("31-Mar") && fromYear.substring(7,11).equals(resettlementdate.substring(7,11))){
								data.setResttlmentDate(resettlementdate);
							}else{
							data.setResttlmentDate("---");
							}
						}

					}else{
						data.setResttlmentDate("---");
					}
					
					if (rs.getString("DATEOFENTITLE") != null) {
						data.setDateOfEntitle(rs.getString("DATEOFENTITLE"));
					} else {
						data.setDateOfEntitle("");
					}
					if (rs.getString("restrictionflag") != null) {
						restrictionflag = rs.getString("restrictionflag");
					}
					if (restrictionflag.equals("Y")) {
						restrType = this.getRestriction(con, data.getPensionNo(),
								fromYear, toYear);
						if (!restrType.equals("")) {
							if (restrType.equals("ARREARS.INT")) {
								data.setArreerintflag(true);
							} else if (restrType.equals("NO.INTEREST-OB")) {
								data.setObInterst(true);
							}else if(restrType.equals("NO.INTEREST-ALL")){
								data.setNoInterest(true);
								data.setAdjObInterst(true);
								data.setObInterst(true);
								data.setAfterFSInt(true);
							}else if(restrType.equals("NO.INTEREST-ADJ.OB")){
								data.setAdjObInterst(true);
							}else if(restrType.equals("NO.INTEREST-BOTH")){
								data.setAdjObInterst(true);
								data.setObInterst(true);
							}else if(restrType.equals("NO.INT-AFTERFS")){
								data.setAfterFSInt(true);
							}
						}

					}
					if (data.getWetherOption().trim().equals("A")) {
						data.setWhetherOptionDescr("Full Pay");
					} else if (data.getWetherOption().trim().equals("B")
							|| data.getWetherOption().trim().equals("NO")) {
						data.setWhetherOptionDescr("Celing");
					}
					if (Integer.parseInt(findFromYear) > 2008) {
						data.setPfIDString("");
					} else {
						pfidWithRegion = this.getEmployeeMappingPFInfo(con, data
								.getPensionNo(), data.getCpfAccno(), data
								.getRegion());
						if (!pfidWithRegion.equals("---")) {
							String[] pfIDLists = pfidWithRegion.split("=");
							data.setPfIDString(commonDAO.preparedCPFString(pfIDLists));
							log.info("CPACCNO Strip==========="
									+ data.getPfIDString());
						}
						log.info(data.getPfIDString());
					}	
					
					if (rs.getString("UANNO") != null) {
						data.setUanno(rs.getString("UANNO"));
			        } else {
			        	data.setUanno("---");
			        }
					
					
					
					//fresh option
					freshPensionOption="";
					freshOptQuery="select pensionno,freshpensionoption,weatheroption from employee_pension_freshoption " +
					" where pensionno="+data.getPensionNo()+" and deleteflag='N'";
			
			freshOptStmt = con.createStatement();
			freshOptrs = freshOptStmt.executeQuery(freshOptQuery);
			
			while(freshOptrs.next()) {
				freshPensionOption=freshOptrs.getString("freshpensionoption");
			}
			
			freshOptStmt.close();
					
					
					
					
					
					
					
					if(!freshPensionOption.equals("")) {
						data.setFreshPensionOption(freshPensionOption);
					}
					else {
						data.setFreshPensionOption("---");
					}
					log.info("freshpensionoption=="+data.getFreshPensionOption());
					if (data.getFreshPensionOption().trim().equals("A")) {
						data.setFreshPensionOptionDescr("Full Pay");
					} else if (data.getFreshPensionOption().trim().equals("B")) {
						data.setFreshPensionOptionDescr("Celing");
					} else {
						data.setFreshPensionOptionDescr("---");
					}
					
					if(rs.getString("NEWEMPCODE")!=null) {
						data.setNewEmployeeNumber(rs.getString("NEWEMPCODE"));						
					}else {
						data.setNewEmployeeNumber("---");
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
		public String getResettlementDtByDate(Connection con,String pfid,String fromdate,String todate){
			String resettlementdt="---",frmSelQry="";
			frmSelQry="SELECT to_char(RESETTLEMENTDATE,'dd-Mon-yyyy')  AS RESETTLEMENTDATE FROM employee_resettlement_dtl WHERE PENSIONNO="+pfid+" AND resettlementdate BETWEEN '"+fromdate+"' AND LAST_DAY('"+todate+"') AND flag='N'";
			Statement st = null;
			ResultSet rs = null;
			log.info("getResettlementDtByDate"+frmSelQry);
			try{
				st = con.createStatement(); 
				rs=st.executeQuery(frmSelQry);
				if(rs.next()){
					if(rs.getString("RESETTLEMENTDATE")!=null){
						resettlementdt=rs.getString("RESETTLEMENTDATE");
					}
				}
				log.info("getResettlementDtByDate:===resettlementdt"+resettlementdt);
			}catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return resettlementdt;
		}
		public String getResettlementDtByDateMultiple(Connection con,String pfid,String fromdate,String todate){
			String resettlementdt="---",frmSelQry="";
			frmSelQry="SELECT to_char(RESETTLEMENTDATE,'dd-Mon-yyyy')  AS RESETTLEMENTDATE FROM employee_resettlement_dtl WHERE PENSIONNO="+pfid+" AND resettlementdate BETWEEN '"+fromdate+"' AND LAST_DAY('"+todate+"') AND flag='N' order by RESETTLEMENTDATE desc";
			Statement st = null;
			ResultSet rs = null;
			log.info("getResettlementDtByDate"+frmSelQry);
			try{
				st = con.createStatement(); 
				rs=st.executeQuery(frmSelQry);
				if(rs.next()){
					if(rs.getString("RESETTLEMENTDATE")!=null){
						resettlementdt=rs.getString("RESETTLEMENTDATE");
					}
				}
				log.info("getResettlementDtByDate:===resettlementdt"+resettlementdt);
			}catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return resettlementdt;
		}
		public String getRestriction(Connection con, String pensionno,
				String fromdate, String todate) {
			String restrictionType = "";
			Statement st = null;
			ResultSet rs = null;

			String sqlSelQuery = "SELECT restrictionType from employee_retrictions_info WHERE PENSIONNO="
					+ pensionno
					+ " AND restrictiondate between '"
					+ fromdate
					+ "' and '" + todate + "'";
			log.info("getRestriction" + sqlSelQuery);
			try {
				st = con.createStatement();
				rs = st.executeQuery(sqlSelQuery);

				if (rs.next()) {

					restrictionType = rs.getString("restrictionType");
				}

			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, null);
			}
			return restrictionType;
		}
		public boolean checkDOB(String dateOfBirth) {
			boolean dtFlag = false;
			String getDay = "";
			log.info("checkDOB===dateOfBirth=" + dateOfBirth);
			getDay = dateOfBirth.substring(0, 2);
			log.info("checkDOB====" + getDay);
			String[] dates = { "01" };
			for (int i = 0; i < dates.length; i++) {
				if (getDay.equals(dates[i])) {
					dtFlag = true;
				}
			}
			return dtFlag;
		}
		public int getMonths(Connection con,String resettlmentdt,String finalsettlemntdt){
			int months = 0;	 
			Statement st = null;
			ResultSet rs = null;			 
			String selectQuery = "";
		 
			selectQuery =  "select floor(months_between('"+resettlmentdt+"','"+finalsettlemntdt+"')) as MONTHS from dual";
			log.info("getMonths()=========="+selectQuery);	 
			try{
				con = commonDB.getConnection();
				st = con.createStatement(); 
				rs = st.executeQuery(selectQuery);
				if (rs.next()) {
					if(rs.getString("MONTHS")!=null){
						months=Integer.parseInt(rs.getString("MONTHS"));
					}else{
						months=0;
					}
				}
				 
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				try {
					rs.close();
					commonDB.closeConnection(null, st, rs);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return months;
			
		}
		public int getMonths2(Connection con,String resettlmentdt,String finalsettlemntdt){
			int months = 0;	 
			Statement st = null;
			ResultSet rs = null;			 
			String selectQuery = "";
		 
			selectQuery =  "select ceil(months_between('"+resettlmentdt+"','"+finalsettlemntdt+"')) as MONTHS from dual";
			log.info("getMonths()=========="+selectQuery);	 
			try{
				con = commonDB.getConnection();
				st = con.createStatement(); 
				rs = st.executeQuery(selectQuery);
				if (rs.next()) {
					if(rs.getString("MONTHS")!=null){
						months=Integer.parseInt(rs.getString("MONTHS"));
					}else{
						months=0;
					}
				}
				 
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				try {
					rs.close();
					commonDB.closeConnection(null, st, rs);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return months;
			
		}
		public int getMonths1(Connection con,String resettlmentdt,String finalsettlemntdt){
			int months = 0;	 
			Statement st = null;
			ResultSet rs = null;			 
			String selectQuery = "";
		 
			selectQuery =  "select floor(months_between(last_day('"+resettlmentdt+"'),'"+finalsettlemntdt+"')) as MONTHS from dual";
			log.info("getMonths()=========="+selectQuery);	 
			try{
				con = commonDB.getConnection();
				st = con.createStatement(); 
				rs = st.executeQuery(selectQuery);
				if (rs.next()) {
					if(rs.getString("MONTHS")!=null){
						months=Integer.parseInt(rs.getString("MONTHS"));
					}else{
						months=0;
					}
				}
				 
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				try {
					rs.close();
					commonDB.closeConnection(null, st, rs);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return months;
			
		}
		//By Radha on 22-May-2012 for making exact Dataofseperation+ 36 Years for calculation Intrst on OB
		public String buildQueryEmpPFTransInfoPrinting(String range, String region,
				String empNameFlag, String empName, String sortedColumn,
				String pensionno, String lastmonthYear, String airportcode,
				String bulkPritflag, String finyear,String fromYear,String toYear) {
			log
					.info("CommonDAO::buildQueryEmpPFTransInfoPrinting-- Entering Method");
			StringBuffer whereClause = new StringBuffer();
			StringBuffer query = new StringBuffer();
			String dynamicQuery = "", sqlQuery = "";
			int startIndex = 0, endIndex = 0;
			// hard coded as per pfcard request purpose
			// finyear="2010-2011";
			
			sqlQuery="SELECT EPI.UANNO,       decode(EPI.cad_flag, 'N', 'No', 'Y', 'Yes') as cadoption,       EPI.Deferementpension,       EPI.Deferement,       epi.deferementage,       EPI.NEWEMPCODE,       EPI.REFPENSIONNUMBER,       EPI.RESETTLEMENTDATE,       EPI.CPFACNO,       EPI.AIRPORTSERIALNUMBER,       EPI.EMPLOYEENO,       INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,       EPI.DESEGNATION,       EPI.EMP_LEVEL,       EPI.DATEOFBIRTH,       EPI.DATEOFJOINING,       EPI.DATEOFSEPERATION_REASON,       EPI.DATEOFSEPERATION_DATE,       EPI.WHETHER_FORM1_NOM_RECEIVED,       REMARKS,       EPI.AIRPORTCODE,       EPI.GENDER,       EPI.FHNAME, " +
			"      EPI.MARITALSTATUS,       EPI.PERMANENTADDRESS,       EPI.TEMPORATYADDRESS,       EPI.WETHEROPTION,       EPI.SETDATEOFANNUATION,       EPI.EMPFLAG,       EPI.DIVISION,       EPI.DEPARTMENT,       EPI.EMAILID,       EPI.EMPNOMINEESHARABLE,       EPI.REGION,       EPI.PENSIONNO,       (LAST_DAY(EPI.dateofbirth) + INTERVAL '60' year) as DOR,       EPI.username,       EPI.LASTACTIVE,       EPI.RefMonthYear,       EPI.IPAddress,       EPI.OTHERREASON,       decode(sign(round(months_between(EPI.dateofjoining, '01-Apr-1995') / 12)),              -1,              '01-Apr-1995',              1,              to_char(EPI.dateofjoining, 'dd-Mon-yyyy'),              to_char(EPI.dateofjoining, 'dd-Mon-yyyy')) as DATEOFENTITLE,       to_char(trunc((EPI.dateofbirth + INTERVAL '60' year), 'MM') - 1, " +
			"              'dd-Mon-yyyy') as LASTDOB,       ROUND(months_between('01-Apr-1995', EPI.dateofbirth) / 12) EMPAGE,       EPI.FINALSETTLMENTDT,       PADJ.ADJDATE,       PADJ.ADJAMOUNT,       PADJ.ADJINT,       EPI.restrictionflag,       EPI.resettlementflag,       (case when epi.dateofseperation_reason='Resignation' then       to_char(add_months(EPI.Dateofbirth, 696), 'dd-Mon-yyyy')       else       to_char(add_months(EPI.dateofseperation_date, 36), 'dd-Mon-yyyy')         end)  as PREFIXSEPDATE,(case when epi.dateofseperation_reason='Resignation' then        round(months_between(to_char(add_months(EPI.Dateofbirth,                                               696),                                    'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)       else      round(months_between(to_char(add_months(EPI.dateofseperation_date,                                               36), " +
			"                                   'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)         end)    AS CHKFRZNSEPERATION,       (CASE         WHEN (TO_DATE(add_months(EPI.dateofseperation_date, 36)) > ((case                when ((case                       when (EPI.Finalsettlmentdt between '"+fromYear+"' and '"+toYear+"') then                        to_date(EPI.Finalsettlmentdt)                     end) is null) then                 ((case                   when (EPI.Resettlementdate between '"+fromYear+"' and '"+toYear+"') then                    to_date(EPI.Resettlementdate)                 end))              end))) then          'Y'         else          'N'       end) as cmprpymntseprtndt,       EPI.pfwdisableFlag as pfwdisableFlag  FROM EMPLOYEE_PERSONAL_INFO EPI, EMPLOYEE_PENSION_ADJUSTMENTS PADJ,EPIS_BULK_PRINT_PFIDS EPV WHERE EPI.PENSIONNO = PADJ.PENSIONNO(+) AND EPV.PENSIONNO=EPI.PENSIONNO";
	

			
			
			
			/*sqlQuery = "SELECT DISTINCT EPI.REFPENSIONNUMBER,EPI.CPFACNO,EPI.AIRPORTSERIALNUMBER,EPI.EMPLOYEENO, INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,EPI.DESEGNATION,EPI.EMP_LEVEL,EPI.DATEOFBIRTH,EPI.DATEOFJOINING,"
					+ "EPI.DATEOFSEPERATION_REASON,EPI.DATEOFSEPERATION_DATE,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.WHETHER_FORM1_NOM_RECEIVED,EPI.REMARKS,EPI.AIRPORTCODE,EPI.GENDER,EPI.GENDER AS SEX,EPI.FHNAME,EPI.MARITALSTATUS,EPI.PERMANENTADDRESS,"
					+ "EPI.TEMPORATYADDRESS,EPI.WETHEROPTION,EPI.SETDATEOFANNUATION,EPI.EMPFLAG,EPI.DIVISION,EPI.DEPARTMENT,EPI.EMAILID,EPI.EMPNOMINEESHARABLE,EPI.REGION,EPI.PENSIONNO,(LAST_DAY(EPI.dateofbirth) + INTERVAL '60' year ) as DOR,EPI.username,EPI.LASTACTIVE,EPI.RefMonthYear,EPI.IPAddress,"
					+ "EPI.OTHERREASON,decode(sign(round(months_between(EPI.dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(EPI.dateofjoining,'dd-Mon-yyyy'),to_char(EPI.dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((EPI.dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
					+ "ROUND(months_between('01-Apr-1995', EPI.dateofbirth) / 12) EMPAGE,EPI.FINALSETTLMENTDT,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED,EPI.RESETTLEMENTDATE,PADJ.ADJDATE,PADJ.ADJAMOUNT,PADJ.ADJINT,EPI.restrictionflag" +
					",EPI.resettlementflag,to_char(add_months(EPI.dateofseperation_date, 36),'dd-Mon-yyyy') as PREFIXSEPDATE, round(months_between(to_char(add_months(EPI.dateofseperation_date, 36), 'dd-Mon-yyyy'),'"+fromYear+"')/12,1) AS CHKFRZNSEPERATION, (CASE WHEN(TO_DATE(add_months(EPI.dateofseperation_date, 36))> " +
					"((case when ((case when (EPI.Finalsettlmentdt between '"+fromYear+"' and '"+toYear+"') then to_date(EPI.Finalsettlmentdt) end) is null) then  ((case  when (EPI.Resettlementdate between '"+fromYear+"' and '"+toYear+"') then to_date(EPI.Resettlementdate) end)) end))) then 'Y' else 'N' end) as cmprpymntseprtndt" +
					"  FROM EMPLOYEE_PERSONAL_INFO EPI,EMPLOYEE_PENSION_ADJUSTMENTS PADJ,EPIS_BULK_PRINT_PFIDS EPV WHERE EPI.PENSIONNO = PADJ.PENSIONNO(+) AND EPV.PENSIONNO=EPI.PENSIONNO";

			*/
			
			if (!range.equals("NO-SELECT")) {
				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  (EPV.PFID BETWEEN " + startIndex + " AND "
						+ endIndex + ")");
				whereClause.append(" AND ");

			}
			if (bulkPritflag.equals("true")) {
				whereClause.append(" EPV.FINAYEAR ='" + finyear + "'");
				whereClause.append(" AND ");
			}
			if (!region.equals("") && (empNameFlag.equals("false"))) {
				whereClause.append(" EPV.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}
			if (!airportcode.equals("") && (empNameFlag.equals("false"))) {
				whereClause.append(" EPV.AIRPORTCODE ='" + airportcode + "'");
				whereClause.append(" AND ");
			}
			/*
			 * if (!lastmonthYear.equals("")) { whereClause.append("
			 * TO_CHAR(MONTHYEAR,'dd-MM-yyyy') like'%" + lastmonthYear + "'");
			 * whereClause.append(" AND "); }
			 */
			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("EPV.PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}
			query.append(sqlQuery);
			if ((region.equals(""))
					&& (airportcode.equals(""))
					&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")) && (lastmonthYear
							.equals("")))) {

			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
			String orderBy = " ORDER BY EPI.PENSIONNO";
			query.append(orderBy);
			dynamicQuery = query.toString();
			log
					.info("CommonDAO::buildQueryEmpPFTransInfoPrinting Leaving Method");
			return dynamicQuery;
		}
		// By Radha on 22-May-2012 for making exact Dataofseperation+ 36 Years for calculation Intrst on OB
		public String buildQuerygetEmployeePFInfoPrinting(String range,
				String region, String empNameFlag, String empName,
				String sortedColumn, String pensionno,String fromYear,String toYear) {
			log
					.info("CommonDAO::buildQuerygetEmployeePFInfoPrinting-- Entering Method");
			StringBuffer whereClause = new StringBuffer();
			StringBuffer query = new StringBuffer();
			String dynamicQuery = "", sqlQuery = "";
			int startIndex = 0, endIndex = 0;
			log.info("pensionno  " + pensionno);
			String findFromYear="";
			try {
				findFromYear = commonUtil.converDBToAppFormat(fromYear,
						"dd-MMM-yyyy", "yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fromYear.equals("01-Apr-2018") ){
				
				sqlQuery="SELECT EPI.UANNO,       decode(EPI.cad_flag, 'N', 'No', 'Y', 'Yes') as cadoption,       EPI.Deferementpension,       EPI.Deferement,       epi.deferementage,       EPI.NEWEMPCODE,       EPI.REFPENSIONNUMBER,       EPI.RESETTLEMENTDATE,       EPI.CPFACNO,       EPI.AIRPORTSERIALNUMBER,       EPI.EMPLOYEENO,       INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,       EPI.DESEGNATION,       EPI.EMP_LEVEL,       EPI.DATEOFBIRTH,       EPI.DATEOFJOINING,       EPI.DATEOFSEPERATION_REASON,       EPI.DATEOFSEPERATION_DATE,       EPI.WHETHER_FORM1_NOM_RECEIVED,       REMARKS,       EPI.AIRPORTCODE,       EPI.GENDER,       EPI.FHNAME, " +
						"      EPI.MARITALSTATUS,       EPI.PERMANENTADDRESS,       EPI.TEMPORATYADDRESS,       EPI.WETHEROPTION,       EPI.SETDATEOFANNUATION,       EPI.EMPFLAG,       EPI.DIVISION,       EPI.DEPARTMENT,       EPI.EMAILID,       EPI.EMPNOMINEESHARABLE,       EPI.REGION,       EPI.PENSIONNO,       (LAST_DAY(EPI.dateofbirth) + INTERVAL '60' year) as DOR,       EPI.username,       EPI.LASTACTIVE,       EPI.RefMonthYear,       EPI.IPAddress,       EPI.OTHERREASON,       decode(sign(round(months_between(EPI.dateofjoining, '01-Apr-1995') / 12)),              -1,              '01-Apr-1995',              1,              to_char(EPI.dateofjoining, 'dd-Mon-yyyy'),              to_char(EPI.dateofjoining, 'dd-Mon-yyyy')) as DATEOFENTITLE,       to_char(trunc((EPI.dateofbirth + INTERVAL '60' year), 'MM') - 1, " +
						"              'dd-Mon-yyyy') as LASTDOB,       ROUND(months_between('01-Apr-1995', EPI.dateofbirth) / 12) EMPAGE,       EPI.FINALSETTLMENTDT,       PADJ.ADJDATE,       PADJ.ADJAMOUNT,       PADJ.ADJINT,       EPI.restrictionflag,       EPI.resettlementflag,       (case when epi.dateofseperation_reason='Resignation'  then       to_char(add_months(EPI.Dateofbirth, 696), 'dd-Mon-yyyy')       else       to_char(add_months(EPI.dateofseperation_date, 36), 'dd-Mon-yyyy')         end)  as PREFIXSEPDATE,(case when epi.dateofseperation_reason='Resignation'  then       round(months_between(to_char(add_months(EPI.Dateofbirth,                                               696),                                    'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)       else      round(months_between(to_char(add_months(EPI.dateofseperation_date,                                               36), " +
						"                                   'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)         end)    AS CHKFRZNSEPERATION,       (CASE         WHEN (TO_DATE(add_months(EPI.dateofseperation_date, 36)) > ((case                when ((case                       when (EPI.Finalsettlmentdt between '"+fromYear+"' and '"+toYear+"') then                        to_date(EPI.Finalsettlmentdt)                     end) is null) then                 ((case                   when (EPI.Resettlementdate between '"+fromYear+"' and '"+toYear+"') then                    to_date(EPI.Resettlementdate)                 end))              end))) then          'Y'         else          'N'       end) as cmprpymntseprtndt,       EPI.pfwdisableFlag as pfwdisableFlag  FROM EMPLOYEE_PERSONAL_INFO EPI, EMPLOYEE_PENSION_ADJUSTMENTS PADJ WHERE EPI.PENSIONNO = PADJ.PENSIONNO(+)";
				
			}else if(Integer.parseInt(findFromYear)>=2019){
				sqlQuery="SELECT EPI.UANNO,       decode(EPI.cad_flag, 'N', 'No', 'Y', 'Yes') as cadoption,       EPI.Deferementpension,       EPI.Deferement,       epi.deferementage,       EPI.NEWEMPCODE,       EPI.REFPENSIONNUMBER,       EPI.RESETTLEMENTDATE,       EPI.CPFACNO,       EPI.AIRPORTSERIALNUMBER,       EPI.EMPLOYEENO,       INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,       EPI.DESEGNATION,       EPI.EMP_LEVEL,       EPI.DATEOFBIRTH,       EPI.DATEOFJOINING,       EPI.DATEOFSEPERATION_REASON,       EPI.DATEOFSEPERATION_DATE,       EPI.WHETHER_FORM1_NOM_RECEIVED,       REMARKS,       EPI.AIRPORTCODE,       EPI.GENDER,       EPI.FHNAME, " +
				"      EPI.MARITALSTATUS,       EPI.PERMANENTADDRESS,       EPI.TEMPORATYADDRESS,       EPI.WETHEROPTION,       EPI.SETDATEOFANNUATION,       EPI.EMPFLAG,       EPI.DIVISION,       EPI.DEPARTMENT,       EPI.EMAILID,       EPI.EMPNOMINEESHARABLE,       EPI.REGION,       EPI.PENSIONNO,       (LAST_DAY(EPI.dateofbirth) + INTERVAL '60' year) as DOR,       EPI.username,       EPI.LASTACTIVE,       EPI.RefMonthYear,       EPI.IPAddress,       EPI.OTHERREASON,       decode(sign(round(months_between(EPI.dateofjoining, '01-Apr-1995') / 12)),              -1,              '01-Apr-1995',              1,              to_char(EPI.dateofjoining, 'dd-Mon-yyyy'),              to_char(EPI.dateofjoining, 'dd-Mon-yyyy')) as DATEOFENTITLE,       to_char(trunc((EPI.dateofbirth + INTERVAL '60' year), 'MM') - 1, " +
				"              'dd-Mon-yyyy') as LASTDOB,       ROUND(months_between('01-Apr-1995', EPI.dateofbirth) / 12) EMPAGE,       EPI.FINALSETTLMENTDT,       PADJ.ADJDATE,       PADJ.ADJAMOUNT,       PADJ.ADJINT,       EPI.restrictionflag,       EPI.resettlementflag,       (case when epi.dateofseperation_reason='Resignation' and round(months_between(epi.dateofseperation_date,epi.dateofbirth)/12)<55 then       to_char(add_months(EPI.Dateofbirth, 696), 'dd-Mon-yyyy')       else       to_char(add_months(EPI.dateofseperation_date, 36), 'dd-Mon-yyyy')         end)  as PREFIXSEPDATE,(case when epi.dateofseperation_reason='Resignation' and round(months_between(epi.dateofseperation_date,epi.dateofbirth)/12)<55 then       round(months_between(to_char(add_months(EPI.Dateofbirth,                                               696),                                    'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)       else      round(months_between(to_char(add_months(EPI.dateofseperation_date,                                               36), " +
				"                                   'dd-Mon-yyyy'),                            '"+fromYear+"') / 12,             1)         end)    AS CHKFRZNSEPERATION,       (CASE         WHEN (TO_DATE(add_months(EPI.dateofseperation_date, 36)) > ((case                when ((case                       when (EPI.Finalsettlmentdt between '"+fromYear+"' and '"+toYear+"') then                        to_date(EPI.Finalsettlmentdt)                     end) is null) then                 ((case                   when (EPI.Resettlementdate between '"+fromYear+"' and '"+toYear+"') then                    to_date(EPI.Resettlementdate)                 end))              end))) then          'Y'         else          'N'       end) as cmprpymntseprtndt,       EPI.pfwdisableFlag as pfwdisableFlag  FROM EMPLOYEE_PERSONAL_INFO EPI, EMPLOYEE_PENSION_ADJUSTMENTS PADJ WHERE EPI.PENSIONNO = PADJ.PENSIONNO(+)";
		
	
			}
				
				else{
			sqlQuery = "SELECT EPI.UANNO,decode(EPI.cad_flag,'N','No','Y','Yes') as cadoption,EPI.Deferementpension,EPI.Deferement, epi.deferementage,EPI.NEWEMPCODE,EPI.REFPENSIONNUMBER,EPI.RESETTLEMENTDATE,EPI.CPFACNO,EPI.AIRPORTSERIALNUMBER,EPI.EMPLOYEENO, INITCAP(EPI.EMPLOYEENAME) AS EMPLOYEENAME,EPI.DESEGNATION,EPI.EMP_LEVEL,EPI.DATEOFBIRTH,EPI.DATEOFJOINING,EPI.DATEOFSEPERATION_REASON,EPI.DATEOFSEPERATION_DATE,EPI.WHETHER_FORM1_NOM_RECEIVED,REMARKS,"
					+ "EPI.AIRPORTCODE,EPI.GENDER,EPI.FHNAME,EPI.MARITALSTATUS,EPI.PERMANENTADDRESS,EPI.TEMPORATYADDRESS,EPI.WETHEROPTION,EPI.SETDATEOFANNUATION,EPI.EMPFLAG,EPI.DIVISION,EPI.DEPARTMENT,EPI.EMAILID,EPI.EMPNOMINEESHARABLE,EPI.REGION,EPI.PENSIONNO,(LAST_DAY(EPI.dateofbirth) + INTERVAL '60' year ) as DOR,EPI.username,EPI.LASTACTIVE,EPI.RefMonthYear,"
					+ "EPI.IPAddress,EPI.OTHERREASON,decode(sign(round(months_between(EPI.dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(EPI.dateofjoining,'dd-Mon-yyyy'),to_char(EPI.dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((EPI.dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,"
					+ "ROUND(months_between('01-Apr-1995', EPI.dateofbirth) / 12) EMPAGE,EPI.FINALSETTLMENTDT,PADJ.ADJDATE,PADJ.ADJAMOUNT,PADJ.ADJINT,EPI.restrictionflag,EPI.resettlementflag,to_char(add_months(EPI.dateofseperation_date, 36),'dd-Mon-yyyy') as PREFIXSEPDATE, round(months_between(to_char(add_months(EPI.dateofseperation_date, 36), 'dd-Mon-yyyy'),'"+fromYear+"')/12,1) AS CHKFRZNSEPERATION, " +
					"(CASE WHEN(TO_DATE(add_months(EPI.dateofseperation_date, 36))> ((case when ((case when (EPI.Finalsettlmentdt between '"+fromYear+"' and '"+toYear+"') then to_date(EPI.Finalsettlmentdt) end) is null) then  ((case  when (EPI.Resettlementdate between '"+fromYear+"' and '"+toYear+"') then to_date(EPI.Resettlementdate) end)) end))) then 'Y' else 'N' end) as cmprpymntseprtndt,EPI.pfwdisableFlag as pfwdisableFlag  FROM EMPLOYEE_PERSONAL_INFO EPI,EMPLOYEE_PENSION_ADJUSTMENTS PADJ WHERE  EPI.PENSIONNO = PADJ.PENSIONNO(+) ";
			}
		
			
			if (!range.equals("NO-SELECT")) {

				String[] findRnge = range.split(" - ");
				startIndex = Integer.parseInt(findRnge[0]);
				endIndex = Integer.parseInt(findRnge[1]);

				whereClause.append("  EPI.PENSIONNO >=" + startIndex
						+ " AND EPI.PENSIONNO <=" + endIndex);
				whereClause.append(" AND ");

			}

			if (!region.equals("") && !region.equals("AllRegions")) {
				whereClause.append(" EPI.REGION ='" + region + "'");
				whereClause.append(" AND ");
			}

			if (empNameFlag.equals("true")) {
				if (!empName.equals("") && !pensionno.equals("")) {
					whereClause.append("EPI.PENSIONNO='" + pensionno + "' ");
					whereClause.append(" AND ");
				}
			}

			query.append(sqlQuery);
			if (region.equals("")
					&& (range.equals("NO-SELECT") && (empNameFlag.equals("false")))) {

			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
			dynamicQuery = query.toString();
			log
					.info("CommonDAO::buildQuerygetEmployeePFInfoPrinting Leaving Method");
			return dynamicQuery;
		}
		public String getEmployeeMappingPFInfo(Connection con, String personalPFID,
				String personalCPFACCNO, String personalRegion) {

			Statement st = null;
			ResultSet rs = null;
			String sqlQuery = "", pfID = "", region = "", regionPFIDS = "";
			try {

				st = con.createStatement();
				sqlQuery = "SELECT CPFACNO,REGION FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER='"
						+ personalPFID + "'";
				log.info("CommonDAO::getEmployeeMappingPFInfo" + sqlQuery);
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
				if (regionPFIDS.equals("")) {
					regionPFIDS = "---";
				}
				/*
				 * regionPFIDS = regionPFIDS + personalCPFACCNO + "," +
				 * personalRegion + "=";
				 */
			} catch (SQLException e) {
				log.printStackTrace(e);
			} catch (Exception e) {
				log.printStackTrace(e);
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			return regionPFIDS;
		}
		public EmployeePersonalInfo getFrozedPFCardInfo(Connection con,String fromYear,String toDate,EmployeePersonalInfo personalInfo) throws InvalidDataException{
			int arrearIntMonths=0,vInterestMnths=0;
			String vIntflag="";
			
			arrearIntMonths=12;
			personalInfo.setFrozedSeperationAvail(true);
			
			long days=commonUtil.getDateDifferenceDays(commonUtil.getCurrentDate("dd-MMM-yyyy"),personalInfo.getFrozedSeperationDate());
			
			log.info("commonUtil.getCurrentDate"+commonUtil.getCurrentDate("dd-MMM-yyyy")+"personalInfo.getFrozedSeperationDate"+personalInfo.getFrozedSeperationDate()+"days==========="+days);
			 if(days>0){
				 personalInfo.setFrozedSeperationDate(commonUtil.getCurrentDate("dd-MMM-yyyy"));
			 }
			 
			if (this.compareFinalSettlementDates(fromYear,toDate,personalInfo.getFrozedSeperationDate())==true){
				vIntflag ="INT";
				vInterestMnths = this.numOfMnthFinalSettlement(commonUtil
						.converDBToAppFormat(personalInfo.getFrozedSeperationDate(),
								"dd-MMM-yyyy", "MMM"));
				
			} else{
				vIntflag ="NOINT";
				vInterestMnths=0;
			}
			log.info("getFrozedPFCardInfo::compareFinalSettlementDates"+this.compareFinalSettlementDates(fromYear,toDate,personalInfo.getFrozedSeperationDate())+"vInterestMnths"+vInterestMnths);
			personalInfo.setFrozedSeperationInterest(vInterestMnths);
			personalInfo.setFrozedSeperationArrearInt(arrearIntMonths);
			return personalInfo;
		}
		
		
		public String getUserReqForFrozenPfid(Connection con,String pensionno,String adjobyear){	 	 
			
			Statement st = null;
			ResultSet rs = null;
			String  result="",query="",requestedby="",remarks="";
			try {
				
				st = con.createStatement();
				 
				 query = "select userreq,remarks from epis_pfid_year_wise_frozen  where pensionno='"+pensionno+"' and '"+adjobyear+"' between fromdate and todate";
				 
				 log.info("AdjUploadDAo::getUserReqForFrozenPfid====query====" + query);
				rs = st.executeQuery(query);
				if (rs.next()) {
					if(rs.getString("userreq")!=null){
						requestedby=rs.getString("userreq");
					}
					/*if(rs.getString("remarks")!=null){
						remarks=rs.getString("remarks");
					}*/
				result=requestedby;	
					
				}else{
					result="NOTEXIST";
				}

			} catch (Exception e) {
				log.info("===========<<<<<<<<<<< Exception >>>>>>>>>>==========" + e.getMessage());
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
			log.info("====result===="+result);
			
			return result;
		}	
public boolean getRateOfIntFlag(Connection con,String fromyear,String toyear,String pensionno){	 	 
			
			Statement st = null;
			ResultSet rs = null;
			String  query="";
			boolean flag=false;
			try {
				
				st = con.createStatement();
				 
				 query = "select 'X' as flag from Epis_changeinterest_cases  where pensionno='"+pensionno+"' and finyear='"+fromyear+"-"+toyear+"'";
				 
				 log.info("Rateofint Flag::getReateOfIntFlag====query====" + query);
				rs = st.executeQuery(query);
				if (rs.next()) {
					flag=true;
				}

			} catch (Exception e) {
				log.info("===========<<<<<<<<<<< Exception >>>>>>>>>>==========" + e.getMessage());
			} finally {
				commonDB.closeConnection(null, st, rs);
			}
		
			
			return flag;
		}
		public String  ChkFreezedMnthinECR(String monthyear) {
			Connection con = null;
			 
			ResultSet rs = null;
			String ECRStatus = "" ;
			StringBuffer stations = new StringBuffer();
			try {
				con = commonDB.getConnection();
				Statement st = con.createStatement();
				String sql = "select 'X' as flag from epis_ecr_frozen_months where monthyear  ='"+ monthyear + "' ";
				rs = st.executeQuery(sql);
				while (rs.next()) {				 
					if(rs.getString("flag")!=null){
						ECRStatus = "Freezed";
					}else{
						ECRStatus = "NotFreezed";
					}
				} 
				 
			} catch (Exception e) {
				e.printStackTrace();
				log.info("error" + e.getMessage());

			}
			return ECRStatus;

		}
		public double pensionFormsCalculation12Months(String monthYear, String emoluments,
				String penionOption, String region, boolean formFlag,
				boolean secondFormFlag, String emolumentMonths) {
			double penContrVal = 0.0;
			DecimalFormat df = new DecimalFormat("#########0.00");
			String chkDecMnth = "";
			boolean dtFlag = false;
			long beginDate = 0, endDate = 0, empPenDt = 0, secBeginDate = 0, secEndDate = 0;

			if (!emolumentMonths.trim().equals("0.5")
					&& !emolumentMonths.trim().equals("1")
					&& !emolumentMonths.trim().equals("2")
					&& !emolumentMonths.trim().equals("90")
					&& !emolumentMonths.trim().equals("120")
					&& !emolumentMonths.trim().equals("150")
					&& !emolumentMonths.trim().equals("180")) {
				int getDaysBymonth = this.getNoOfDaysForsalRecovery(monthYear);
				emolumentMonths = String.valueOf(Double.parseDouble(emolumentMonths
						.trim())
						/ getDaysBymonth);
				DecimalFormat twoDForm = new DecimalFormat("#.#####");
				log.info("emolumentMonths " + emolumentMonths + "getDaysBymonth "
						+ getDaysBymonth);
				emolumentMonths = twoDForm.format(Double
						.parseDouble(emolumentMonths));
			} else if (emolumentMonths.trim().equals("90")
					|| emolumentMonths.trim().equals("120")
					|| emolumentMonths.trim().equals("150")
					|| emolumentMonths.trim().equals("180")) {
				if (emolumentMonths.trim().equals("90")) {
					emolumentMonths = "3";
				} else if (emolumentMonths.trim().equals("120")) {
					emolumentMonths = "4";
				} else if (emolumentMonths.trim().equals("150")) {
					emolumentMonths = "5";
				} else if (emolumentMonths.trim().equals("180")) {
					emolumentMonths = "6";
				} else if (emolumentMonths.trim().equals("210")) {
					emolumentMonths = "7";
				} else if (emolumentMonths.trim().equals("240")) {
					emolumentMonths = "8";
				} else if (emolumentMonths.trim().equals("270")) {
					emolumentMonths = "9";
				} else if (emolumentMonths.trim().equals("300")) {
					emolumentMonths = "10";
				} else if (emolumentMonths.trim().equals("330")) {
					emolumentMonths = "11";
				} else if (emolumentMonths.trim().equals("360")) {
					emolumentMonths = "12";
				} else if (emolumentMonths.trim().equals("390")) {
					emolumentMonths = "13";
				} else if (emolumentMonths.trim().equals("420")) {
					emolumentMonths = "14";
				} else if (emolumentMonths.trim().equals("450")) {
					emolumentMonths = "15";
				} else {
					emolumentMonths = "1";
				}
			}
			if (monthYear.substring(0, 2).equals("01")) {
				dtFlag = true;
			}
			if (dtFlag == true) {
				beginDate = Date.parse("01-Apr-1995");
				endDate = Date.parse("01-Nov-1995");
				secBeginDate = Date.parse("01-Jan-1996");
				chkDecMnth = "01-dec-1995";
			} else {
				beginDate = Date.parse("30-Apr-1995");
				endDate = Date.parse("30-Nov-1995");
				secBeginDate = Date.parse("20-Jan-1996");
				chkDecMnth = "31-dec-1995";
			}
			String transMonthYear = "";
			String transMonthYear_new="",conditionDate="Sep-2014";
			try {
				transMonthYear = commonUtil.converDBToAppFormat(monthYear,
						"dd-MMM-yyyy", "-MMM-yyyy");
				transMonthYear_new = commonUtil.converDBToAppFormat(monthYear,
						"dd-MMM-yyyy", "MMM-yyyy");
				
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (transMonthYear.equals("-Apr-1995")) {
				log.info("transMonthYear in if " + transMonthYear + "region"
						+ region);
				penContrVal = 0.0;
				return penContrVal;
			}
			// modifed from 2009 to 2011 on apr 15 th 2010
			//secEndDate = Date.parse("31-Dec-2011");
	       //secEndDate changed by prasanthi on 20-Jan-2012
			 secEndDate = Date.parse(Constants.CALCULATED_PC_UPDATE_12MONTHS);
			empPenDt = Date.parse(monthYear);

			if (empPenDt >= beginDate && empPenDt <= endDate) {
				// log.info("beginDate"+beginDate+"endDate"+endDate+"empPenDt"+
				// empPenDt);
				if (Double.parseDouble(emoluments) >= 5000) {
					emoluments = "5000";
				}
			 
				penContrVal = new Double(df
						.format((Double.parseDouble(emoluments) * 1.16 * Double
								.parseDouble(emolumentMonths)) / 100f))
						.doubleValue() * 2;

			} else if (empPenDt >= secBeginDate && empPenDt <= secEndDate) {
				if (!penionOption.equals("---")) {
					if (penionOption.trim().equals("A")) {
						penContrVal = new Double(df.format((Double
								.parseDouble(emoluments) * 8.33) / 100f))
								.doubleValue();
						// log.info("penionOption==="+penionOption+"Pension Total
						// Value"+penContrVal);
					} else if (penionOption.trim().equals("B")
							|| penionOption.toLowerCase().trim().equals(
									"NO".toLowerCase())) {
						
						
						if( commonUtil.compareTwoDates(conditionDate, transMonthYear_new) ) {
							log.info("Inside new Condition for PC Calculation-------------");
							if (Double.parseDouble(emoluments) >= 15000) {
								penContrVal = Math.round((15000 * 8.33 * Double.parseDouble(emolumentMonths)) / 100f);
							}else if(Double.parseDouble(emoluments)<=-15000){
								penContrVal = Math.round((-15000 * 8.33 * Double.parseDouble(emolumentMonths)) / 100f);
							} else if (Double.parseDouble(emoluments) < 15000 && Double.parseDouble(emoluments) >=-15000) {
								penContrVal = Math.round(new Double(df.format((Double
										.parseDouble(emoluments) * 8.33 * Double
										.parseDouble(emolumentMonths)) / 100f))
										.doubleValue());
							}
							
						}
						else {

						if (Double.parseDouble(emoluments) >= 6500) {
							//code changed for  pc calculated 2 months as 1083 insted of 1082,rectified using round 
							penContrVal = (Math.round((6500 * 8.33 ) / 100f)* Double
									.parseDouble(emolumentMonths));
						}else if(Double.parseDouble(emoluments)<0){
							penContrVal = Math.round((-6500 * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f);
						} else if (Double.parseDouble(emoluments) < 6500 && Double.parseDouble(emoluments) >=0) {
							penContrVal = Math.round(new Double(df.format((Double
									.parseDouble(emoluments) * 8.33 * Double
									.parseDouble(emolumentMonths)) / 100f))
									.doubleValue());
						}
						}
					}
						
				}

			} else if (monthYear.toLowerCase().equals(chkDecMnth)) {
				double salary = 0, perDaySal = 0, firstHalfMnthAmt = 0, secHalfMnthAmt = 0;

				if (penionOption.trim().equals("B")
						|| penionOption.toLowerCase().trim().equals(
								"NO".toLowerCase())) {
					if (Double.parseDouble(emoluments) >= 5000) {
						emoluments = "5000";
					}
				}
				/*
				 * Issue fixed by suresh kumar on 18-Aug-2009 Issue_no:86
				 */
				salary = new Double(df.format(Double.parseDouble(emoluments)))
						.doubleValue();
				perDaySal = salary / 30;
				secHalfMnthAmt = Math.round(salary * 8.33 / 2 / 100);
				if (salary >= 5000) {
					salary = 5000;

				}
				firstHalfMnthAmt = (Math.round((salary * 1.16) / 2 / 100)) * 2;

				if (formFlag == true) {
					penContrVal = new Double(df.format(firstHalfMnthAmt))
							.doubleValue();
				} else if (secondFormFlag == true) {

					penContrVal = new Double(df.format(secHalfMnthAmt))
							.doubleValue();
				} else {
					penContrVal = new Double(df.format(firstHalfMnthAmt
							+ secHalfMnthAmt)).doubleValue();
				}

			}
			// log.info(monthYear+"===="+emoluments+"penContrVal"+penContrVal+
			// "penionOption"+penionOption);
			return penContrVal;
		}
		public ArrayList pfCardAdditionalContriCalculation(String pensionNo){
					
			Connection con=null;
			Statement stmt=null;
			ResultSet rs=null;
			String qury="",additionalContri="";
			double total=0.0;
			ArrayList al=new ArrayList();
			try {
				con = commonDB.getConnection();
				stmt = con.createStatement();
				qury = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' || SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR, decode((sign(sysdate - add_months(to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' || SUBSTR(empdt.MONYEAR, 4, 4))), 1))),-1, 0, 1, 1) as signs, emp.* " +
						" from (select distinct to_char(to_date('01-Sep-2014', 'dd-mon-yyyy') + rownum - 1, 'MONYYYY') monyear from employee_pension_validate where rownum <= to_date('01-Apr-2015', 'dd-mon-yyyy') - to_date('01-Sep-2014', 'dd-mon-yyyy') + 1) empdt, (SELECT add_months(MONTHYEAR, -1) as MONTHYEAR," +
						" to_char(add_months(MONTHYEAR, -1), 'MONYYYY') empmonyear, additionalcontri FROM EMPLOYEE_PENSION_VALIDATE  WHERE empflag = 'Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= add_months('01-Sep-2014', 1) and to_date(to_char(monthyear, 'dd-Mon-yyyy')) <= add_months(last_day('01-Apr-2015'), 1))" +
						" AND PENSIONNO = "+pensionNo+" ) emp where empdt.monyear = emp.empmonyear(+) ORDER BY TO_DATE(EMPMNTHYEAR)";
				log.info("pfCardAdditionalContriCalculation====qury====" + qury);
				rs = stmt.executeQuery(qury);
				while(rs.next()) {
					if(rs.getString("additionalcontri")!=null) {
						additionalContri=rs.getString("additionalcontri");
					}else {
						additionalContri="0.0";
					}
					al.add(additionalContri);
					total=total+Double.parseDouble(additionalContri);					
				}								
				al.add(Double.toString(total));
				log.info("total : "+total+" arraylist : "+al);
			} catch (Exception e) {
				log.info("===========<<<<<<<<<<< Exception >>>>>>>>>>==========" + e.getMessage());
			} finally {
				commonDB.closeConnection(null, stmt, rs);
			}
			
			return al;
		}
		public String caddatecheck(String monthyear,String name) {
			Connection con = null;
			Statement stmt = null;
			ResultSet rs = null;		
			String CadCheckdate = "", cadflag = "";
		   if(name.equals("Form4")){
				monthyear="01"+monthyear;			
			}else{
				monthyear = monthyear;
			}
			//System.out.println("month @@ "+monthyear+" name $$ "+name);
			try {
				con = commonDB.getConnection();
				stmt = con.createStatement();
				CadCheckdate = "select 'N' as flag from dual where to_Date('01/Apr/2018')<=to_Date('" + monthyear + "','dd/mm/yyyy')";
				rs = stmt.executeQuery(CadCheckdate);
				while (rs.next()) {
					if (rs.getString("flag") != null) {
						cadflag = rs.getString("flag");
					} else {
						cadflag = "";
					}
				}
			} catch (Exception e) {
				//log.info("===========<<<<<<<<<<< Exception >>>>>>>>>>=========="
					//	+ e.getMessage());
			} finally {
				commonDB.closeConnection(null, stmt, rs);
			}
			//System.out.println(CadCheckdate+" @@@ "+cadflag);
			return cadflag;
		}
		
}
