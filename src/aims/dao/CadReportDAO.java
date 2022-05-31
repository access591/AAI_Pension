package aims.dao;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;

import aims.bean.CadPensionInfo;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeePensionCardInfo;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinacialDataBean;
import aims.bean.FinancialYearBean;
import aims.bean.Form7MultipleYearBean;
import aims.bean.PensionBean;
import aims.bean.PensionContBean;
import aims.bean.TempPensionTransBean;
import aims.common.CommonUtil;
import aims.common.Constants;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;

public class CadReportDAO implements Constants{
	Log log = new Log(CadReportDAO.class);
	
	DBUtils commonDB = new DBUtils();
	CommonUtil commonUtil = new CommonUtil();
	CommonDAO commonDAO = new CommonDAO();
	
	public void formsDisable(String pfid, String formsstatus,
			String pcreportstatus, String pensionclaimstatus, String username,
			String ipaddress) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int totalRecords = 0;
		String form3_7disable = Constants.form3_7disable;
		String count = "Count";
		String selectSQL = "", selectSQL1 = "", pcreport_forms_stastus_log = "";
		if (formsstatus != "") {
			selectSQL = "update employee_info set formsdisable='" + formsstatus
					+ "' where empserialnumber='" + pfid + "' ";
			selectSQL1 = "update employee_personal_info set formsdisable='"
					+ formsstatus + "' where pensionno='" + pfid + "' ";

		}
		if (pensionclaimstatus != "") {
			selectSQL = "update employee_info set pensioncliamsProcess='"
					+ pensionclaimstatus + "' where empserialnumber='" + pfid
					+ "' ";
			selectSQL1 = "update employee_personal_info set pensioncliamsProcess='"
					+ pensionclaimstatus + "' where pensionno='" + pfid + "' ";
		}
		if (pcreportstatus != "") {
			selectSQL = "update employee_info set PCREPORTVERIFIED='"
					+ pcreportstatus + "' where empserialnumber='" + pfid
					+ "' ";
			selectSQL1 = "update employee_personal_info set PCREPORTVERIFIED='"
					+ pcreportstatus + "' where pensionno='" + pfid + "' ";
		}
		pcreport_forms_stastus_log = "insert into pcreport_forms_stastus_log(pensionno,PCREPORTVERIFIED,FORMSDISABLE,username,ipaddress)values('"
				+ pfid
				+ "','"
				+ pcreportstatus
				+ "','"
				+ formsstatus
				+ "','"
				+ username + "','" + ipaddress + "')";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			st.executeUpdate(selectSQL);
			st.executeUpdate(selectSQL1);
			st.executeUpdate(pcreport_forms_stastus_log);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

	}
	public double getPensionContributionTotal(String PFID){
		log.info(" cadDAO ::getPensionContributionTotal-- Entering Method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList arrearsinfo = new ArrayList();
		//String checkRecordsExist="select  nvl(PENSIONTOTAL,0)+nvl(PENSIONINTEREST,0) as pctotal from employee_adj_ob where pensionno="+PFID+" and adjobyear='01-Apr-2008'";
		String checkRecordsExist="select  nvl(PENSIONTOTAL,0)+nvl(PENSIONINTEREST,0) as pctotal from epis_pc_totals where pensionno="+PFID+" ";
		double count=0;	
		log.info("recoveriedatafrom95to2008" + checkRecordsExist);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(checkRecordsExist);
			
			while(rs.next()){
				count=Integer.parseInt(rs.getString("pctotal"));
			   }
			}catch (Exception e) {
         log.error("Error is "+e);
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (Exception e) {

			}
		}
		log.info(" cadDAO ::getPensionContributionTotal-- Leaving Method");
		return count;
	}
	
	public double getfinalrevoveryintrest(String PFID){
		log.info(" cadDAO ::getPensionContributionTotal-- Entering Method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList arrearsinfo = new ArrayList();
		String checkRecordsExist="select  difftotals from employee_final_recovintr where pensionno="+PFID+" ";
		double count=0;	
		log.info("recoveriedataIntrest" + checkRecordsExist);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(checkRecordsExist);
			
			while(rs.next()){
				count=Integer.parseInt(rs.getString("difftotals"));
			   }
			}catch (Exception e) {
         log.error("Error is "+e);
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (Exception e) {

			}
		}
		log.info(" cadDAO ::getPensionContributionTotal-- Leaving Method");
		return count;
	}
	public String getEditedPensionno(String PFID){
		log.info(" cadDAO ::getPensionContributionTotal-- Entering Method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList arrearsinfo = new ArrayList();
		String checkRecordsExist="select max(LASTACTIVE) as LASTACTIVE from employee_finalsettledate_info where pensionno="+PFID+" and INTERESTCALCDATE is not null ";
		String LASTACTIVE="";
		long transMntYear = 0,freezeddate=0,secfreezeddate=0;
		String flag = "";
		log.info("recoveriedataIntrest" + checkRecordsExist);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(checkRecordsExist);
			
			while(rs.next()){
				if(rs.getString("LASTACTIVE")!=null){				
					LASTACTIVE=commonUtil.getDatetoString(rs.getDate("LASTACTIVE"), "dd-MMM-yyyy");
			   }else{
				   LASTACTIVE="";
			   }
			}
			log.info("LASTACTIVE"+LASTACTIVE);
			if (!LASTACTIVE.equals("")) {
				transMntYear = Date.parse(LASTACTIVE);
				freezeddate = Date.parse("07-Apr-2012");
				secfreezeddate=Date.parse("09-Jul-2012");
				log.info("freezeddate " + freezeddate+ "transMntYear " + transMntYear);
				if (transMntYear >= secfreezeddate){
					 flag = "N";
				}else if (transMntYear >= freezeddate && transMntYear <= secfreezeddate) {
					 flag = "S";
				} else {
					flag = "Y";
				}
			}
			}catch (Exception e) {
         log.error("Error is "+e);
		} finally {
			try {
				rs.close();
				st.close();
				con.close();
			} catch (Exception e) {

			}
		}
		log.info(" cadDAO ::getPensionContributionTotal-- Leaving Method");
		return flag;
	}
	public int totalCountPersonal(EmpMasterBean empMasterBean, int gridLength,
			int startIndex, String empNameCheck, String total,
			String unmappedFlag, String allRecordsFlag, String pfidfrom) {
		log.info(" cadDAO  :totalCountPersonal() entering method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int totalRecords = 0;
		String count = "Count";
		String selectSQL = "";

		selectSQL = this.buildQueryforMapping(empMasterBean, gridLength,
				startIndex, empNameCheck, count, total, unmappedFlag,
				allRecordsFlag, pfidfrom);

		log.info("Query =====" + selectSQL);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectSQL);
			if (rs.next()) {
				totalRecords = Integer.parseInt(rs.getString("count"));
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("PersonalcadDAO :totalCountPersonal() leaving method");
		return totalRecords;
	}
	
	public ArrayList financeDataSearch(EmpMasterBean empSerach, int gridLength,
			int startIndex, String empNameCheck, String unmappedFlag,
			String reportType, String allRecordsFlag, String pfidfrom) {
		PensionBean pensionBean = null;
		ArrayList financedata = new ArrayList();
		String query = this.buildQueryforMapping(empSerach, gridLength,
				startIndex, empNameCheck, "", "", unmappedFlag, allRecordsFlag,
				pfidfrom);
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = commonDB.getConnection();
			log.info("query is " + query.toString());
			PreparedStatement pst = null;
			int countGridLength = -1;
			st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			// pst =
			// con.prepareStatement(query.toString(),ResultSet.CONCUR_UPDATABLE
			// ,ResultSet.CONCUR_UPDATABLE);

			// rs = pst.executeQuery();
			rs = st.executeQuery(query);
			// rs.setFetchSize(100);
			if (rs.next()) {

				if (rs.absolute(startIndex)) {
					countGridLength++;

					pensionBean = new PensionBean();

					if (rs.getString("cpfaccno") != null) {
						pensionBean.setCpfAcNo(rs.getString("cpfaccno")
								.toString());
					} else {
						pensionBean.setCpfAcNo("");
					}

					if (rs.getString("employeeno") != null) {
						pensionBean.setEmployeeCode(rs.getString("employeeno")
								.toString());
					} else {
						pensionBean.setEmployeeCode("");
					}
					if (rs.getString("REGION") != null) {
						pensionBean
								.setRegion(rs.getString("REGION").toString());
					} else {
						pensionBean.setRegion("");
					}
					if (rs.getString("AIRPORTCODE") != null) {
						pensionBean.setAirportCode(rs.getString("AIRPORTCODE")
								.toString());
					} else {
						pensionBean.setAirportCode("");
					}

					if (rs.getString("employeename") != null) {
						pensionBean.setEmployeeName(rs
								.getString("employeename").toString());
					} else {
						pensionBean.setEmployeeName("");
					}
					if (rs.getString("desegnation") != null) {
						pensionBean.setDesegnation(rs.getString("desegnation")
								.toString());
					} else {
						pensionBean.setDesegnation("---");
					}
					if (rs.getString("empserialnumber") != null) {
						pensionBean.setPensionnumber(rs.getString(
								"empserialnumber").toString());
					} else {
						pensionBean.setPensionnumber("");
					}
					if (rs.getString("dateofjoining") != null) {
						pensionBean.setDateofJoining(commonUtil
								.getDatetoString(rs.getDate("dateofjoining"),
										"dd-MMM-yyyy"));
					} else {
						pensionBean.setDateofJoining("---");
					}
					if (rs.getString("dateofbirth") != null) {
						pensionBean.setDateofBirth(commonUtil.getDatetoString(
								rs.getDate("dateofbirth"), "dd-MMM-yyyy"));
					} else {
						pensionBean.setDateofBirth("---");
					}
					if (unmappedFlag.equals("true")) {
						if (rs.getString("TOTALRECORDS") != null) {
							pensionBean.setTotalRecrods(rs.getString(
									"TOTALRECORDS").toString());
						} else {
							pensionBean.setTotalRecrods("0");
						}
					}
					if (rs.getString("REASON") != null) {
						pensionBean.setSeperationReason(rs.getString("REASON")
								.toString());
					} else {
						pensionBean.setSeperationReason("");
					}
					if (rs.getString("formsdisable") != null) {
						pensionBean.setFormsdisable(rs
								.getString("formsdisable").toString());
					} else {
						pensionBean.setFormsdisable("");
					}
					if (rs.getString("PCREPORTVERIFIED") != null) {
						pensionBean.setPcreportverified(rs.getString(
								"PCREPORTVERIFIED").toString());
					} else {
						pensionBean.setPcreportverified("");
					}
					if (rs.getString("PCREPORTVERIFIED") != null) {
						pensionBean.setPcreportverified(rs.getString(
								"PCREPORTVERIFIED").toString());
					} else {
						pensionBean.setPcreportverified("");
					}
					if (allRecordsFlag.equals("true")) {
						if (rs.getString("DATEOFSEPERATION_DATE") != null) {
							pensionBean.setSeperationDate(commonUtil
									.getDatetoString(rs
											.getDate("DATEOFSEPERATION_DATE"),
											"dd-MMM-yyyy"));
						} else {
							pensionBean.setSeperationDate("");
						}
					}
					if (rs.getString("pensioncliamsProcess") != null) {
						pensionBean.setClaimsprocess(rs.getString(
								"pensioncliamsProcess").toString());
					} else {
						pensionBean.setClaimsprocess("");
					}
					
					if (rs.getString("status") != null) {
						pensionBean.setStatus(rs.getString(
								"status"));
					} else {
						pensionBean.setStatus("");
					}

					financedata.add(pensionBean);
				}

				if (reportType == "") {

					while (rs.next() && countGridLength < gridLength) {
						countGridLength++;
						pensionBean = new PensionBean();

						if (rs.getString("cpfaccno") != null) {
							pensionBean.setCpfAcNo(rs.getString("cpfaccno")
									.toString());
						} else {
							pensionBean.setCpfAcNo("");
						}
						if (rs.getString("employeeno") != null) {
							pensionBean.setEmployeeCode(rs.getString(
									"employeeno").toString());
						} else {
							pensionBean.setEmployeeCode("");
						}
						if (rs.getString("REGION") != null) {
							pensionBean.setRegion(rs.getString("REGION")
									.toString());
						} else {
							pensionBean.setRegion("");
						}
						if (rs.getString("AIRPORTCODE") != null) {
							pensionBean.setAirportCode(rs.getString(
									"AIRPORTCODE").toString());
						} else {
							pensionBean.setAirportCode("");
						}
						if (rs.getString("dateofjoining") != null) {
							pensionBean.setDateofJoining(commonUtil
									.getDatetoString(rs
											.getDate("dateofjoining"),
											"dd-MMM-yyyy"));
						} else {
							pensionBean.setDateofJoining("---");
						}
						if (rs.getString("dateofbirth") != null) {
							pensionBean.setDateofBirth(commonUtil
									.getDatetoString(rs.getDate("dateofbirth"),
											"dd-MMM-yyyy"));
						} else {
							pensionBean.setDateofBirth("---");
						}

						if (rs.getString("employeename") != null) {
							pensionBean.setEmployeeName(rs.getString(
									"employeename").toString());
						} else {
							pensionBean.setEmployeeName("");
						}
						if (rs.getString("desegnation") != null) {
							pensionBean.setDesegnation(rs.getString(
									"desegnation").toString());
						} else {
							pensionBean.setDesegnation("---");
						}
						if (rs.getString("empserialnumber") != null) {
							pensionBean.setPensionnumber(rs.getString(
									"empserialnumber").toString());
						} else {
							pensionBean.setPensionnumber("");
						}
						log.info("pensionno " + pensionBean.getPensionnumber());
						if (unmappedFlag.equals("true")) {
							if (rs.getString("TOTALRECORDS") != null) {
								pensionBean.setTotalRecrods(rs.getString(
										"TOTALRECORDS").toString());
							} else {
								pensionBean.setTotalRecrods("0");
							}
						}
						if (rs.getString("REASON") != null) {
							pensionBean.setSeperationReason(rs.getString(
									"REASON").toString());
						} else {
							pensionBean.setSeperationReason("");
						}
						if (rs.getString("formsdisable") != null) {
							pensionBean.setFormsdisable(rs.getString(
									"formsdisable").toString());
						} else {
							pensionBean.setFormsdisable("");
						}
						if (rs.getString("PCREPORTVERIFIED") != null) {
							pensionBean.setPcreportverified(rs.getString(
									"PCREPORTVERIFIED").toString());
						} else {
							pensionBean.setPcreportverified("");
						}
						if (allRecordsFlag.equals("true")) {
							if (rs.getString("DATEOFSEPERATION_DATE") != null) {
								pensionBean
										.setSeperationDate(commonUtil
												.getDatetoString(
														rs
																.getDate("DATEOFSEPERATION_DATE"),
														"dd-MMM-yyyy"));
							} else {
								pensionBean.setSeperationDate("");
							}
						}
						if (rs.getString("pensioncliamsProcess") != null) {
							pensionBean.setClaimsprocess(rs.getString(
									"pensioncliamsProcess").toString());
						} else {
							pensionBean.setClaimsprocess("");
						}
						financedata.add(pensionBean);

					}
				} else {

					while (rs.next()) {
						pensionBean = new PensionBean();

						if (rs.getString("cpfaccno") != null) {
							pensionBean.setCpfAcNo(rs.getString("cpfaccno")
									.toString());
						} else {
							pensionBean.setCpfAcNo("");
						}
						if (rs.getString("employeeno") != null) {
							pensionBean.setEmployeeCode(rs.getString(
									"employeeno").toString());
						} else {
							pensionBean.setEmployeeCode("");
						}
						if (rs.getString("REGION") != null) {
							pensionBean.setRegion(rs.getString("REGION")
									.toString());
						} else {
							pensionBean.setRegion("");
						}
						if (rs.getString("AIRPORTCODE") != null) {
							pensionBean.setAirportCode(rs.getString(
									"AIRPORTCODE").toString());
						} else {
							pensionBean.setAirportCode("");
						}

						if (rs.getString("employeename") != null) {
							pensionBean.setEmployeeName(rs.getString(
									"employeename").toString());
						} else {
							pensionBean.setEmployeeName("");
						}
						if (rs.getString("desegnation") != null) {
							pensionBean.setDesegnation(rs.getString(
									"desegnation").toString());
						} else {
							pensionBean.setDesegnation("---");
						}
						if (rs.getString("dateofjoining") != null) {
							pensionBean.setDateofJoining(rs.getString(
									"dateofjoining").toString());
						} else {
							pensionBean.setDateofJoining("---");
						}
						if (rs.getString("dateofbirth") != null) {
							pensionBean.setDateofBirth(rs.getString(
									"dateofbirth").toString());
						} else {
							pensionBean.setDateofBirth("---");
						}
						if (rs.getString("empserialnumber") != null) {
							pensionBean.setPensionnumber(rs.getString(
									"empserialnumber").toString());
						} else {
							pensionBean.setPensionnumber("");
						}
						if (unmappedFlag.equals("true")) {
							if (rs.getString("TOTALRECORDS") != null) {
								pensionBean.setTotalRecrods(rs.getString(
										"TOTALRECORDS").toString());
							} else {
								pensionBean.setTotalRecrods("0");
							}
						}
						if (rs.getString("REASON") != null) {
							pensionBean.setSeperationReason(rs.getString(
									"REASON").toString());
						} else {
							pensionBean.setSeperationReason("");
						}
						if (rs.getString("pensioncliamsProcess") != null) {
							pensionBean.setClaimsprocess(rs.getString(
									"pensioncliamsProcess").toString());
						} else {
							pensionBean.setClaimsprocess("");
						}

						financedata.add(pensionBean);

					}

				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			log.info("inside finally block");
			commonDB.closeConnection(con, st, rs);
		}

		return financedata;
	}
	public String chkPfidinAdjCrtn(String pfid,String monthyear) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			tableName = "CAD_VALIDATE";
		
		sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid +" and monthyear='"+monthyear+"'";
		log.info("--CAD_VALIDATE  MONTH CHECK()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				chkpfid = "Exists";
			} else {
				chkpfid = "NotExists";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return chkpfid;
	}
	
	public String chkPfidinAdjCrtn(String pfid) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			tableName = "CAD_VALIDATE";
		
		sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid;
		log.info("--CAD_VALIDATE ()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				chkpfid = "Exists";
			} else {
				chkpfid = "NotExists";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return chkpfid;
	}
	
	public int insertEmployeeTransData(String pfId) {
		log.info(" CadReportDAO :insertEmployeeTransData() Entering Method ");
		Connection con = null;
		boolean dataavailbf2008 = false;
		EmpMasterBean bean = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		Statement st = null;
		Statement st1 = null;
		String episAdjCrtnLog = "", episAdjCrtnLogDtl = "", sqlForMapping1995to2008 = "", sqlFor1995to2008 = "";
		String chkpfid = "", commdata = "", revisedFlagQry = "", loansQuery = "", advancesQuery = "", sql = "",sqlAftr2008="", updateloans = "", updateadvances = "", loandate = "", subamt = "", contamt = "", advtransdate = "", advanceAmt = "", monthYear = "", tableName = "";
		String cpfregionstrip = "", condition = "", preparedString = "", dojcpfaccno = "", dojemployeeno = "", dojempname = "", dojstation = "", dojregion = "",	insertDummyRecord="", chkForRecord=""; 
		String[] cpfregiontrip = null;
		String[] cpfaccnos = null;
		String[] regions = null;
		String[] commondatalst = null;		
		int result = 0, loansresult = 0, advancesresult = 0, transID = 0, batchId = 0,insertDummyRecordResult=0;
		String table="",table2="";
		monthYear = commonUtil.getCurrentDate("dd-MMM-yyyy");
		EmpMasterBean empBean = new EmpMasterBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			st1 = con.createStatement();
			tableName = " CAD_VALIDATE ";
			table2 = " employee_pension_validate ";
			chkpfid = this.chkPfidinAdjCrtn(pfId); 
			String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
			if (chkpfid.equals("NotExists")) {
				
				sql = "insert into " + tableName + " (CPFACCNO,AIRPORTCODE,MONTHYEAR,BASIC,DAILYALLOWANCE,SPECIALBASIC,EMOLUMENTS,EMPPFSTATUARY,EMPVPF,ADVANCEDRWAN,PARTFINAL,PFADVANCE,CPFINTEREST,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,EMPTOTAL,AAICONPF,AAICONPENSION,AAITOTAL,EMPLOYEENAME,MISSINGFLAG,EMPLOYEENO,DESEGNATION,GPF,REMARKS,REGION,CPFARREAR,CPF,FPF,ADVREC,ADVGIV,SHORTREC,VDA,ADDCPF,ADVANCE,PFCOUNT,ADDA,DAARREAR,CPFADVANCE,DEDADV,DEDFEMP,DEDFNAA,REFADV,ARRCOUNT,OPTCPF,NONREFMEM,NONREFCOM,FDA,DNSPAY,SPAY,PFW,PAYFORCPF,PAYARREAR,ACTIVEFLAG,MASTER_EMPNAME,EMPFLAG,PENSIONNO,PF,PENSIONCONTRI,FINYEAR,ACC_KEYNO,USERNAME,IPADDRESS,EDITTRANS,DATAFREEZEFLAG,FORM7NARRATION,PCHELDAMT,EMOLUMENTMONTHS,PCCALCAPPLIED,ARREARFLAG,UPDATEDDATE,LATESTMNTHFLAG,ARREARAMOUNT,DEPUTATIONFLAG,DUEEMOLUMENTS,MERGEFLAG,ARREARSBREAKUP,EDITEDDATE,OPCHANGEPF,OPCHANGEPENSIONCONTRI,CALCEMOLUMENTS,SUPPLIFLAG,VRNO,PFCARDNARRATION,REVISEEPFEMOLUMENTS,REVISEEPFEMOLUMENTSFLAG,OTHERORG,OLDAIRPORTCODE,LEAVEFLAG,SWREMARKS,EMPRECOVERYSTS,UPLOADDATE,FORM3REMARKS,CRTLREQIURED,PFCARDNRFLAG,ECRFORM4SUPPLIARRARFLAG,ADDITIONALCONTRI,SPECIALEMOLUMENTSFLAG,CPFACNO_BAK,EMOLUMENTS_B,PENSIONCONTRI_B,FRESHOPFLAG,FORM4FLAG,GROSS) (select CPFACCNO,AIRPORTCODE,MONTHYEAR,BASIC,DAILYALLOWANCE,SPECIALBASIC,EMOLUMENTS,EMPPFSTATUARY,EMPVPF,ADVANCEDRWAN,PARTFINAL,PFADVANCE,CPFINTEREST,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,EMPTOTAL,AAICONPF,AAICONPENSION,AAITOTAL,EMPLOYEENAME,MISSINGFLAG,EMPLOYEENO,DESEGNATION,GPF,REMARKS,REGION,CPFARREAR,CPF,FPF,ADVREC,ADVGIV,SHORTREC,VDA,ADDCPF,ADVANCE,PFCOUNT,ADDA,DAARREAR,CPFADVANCE,DEDADV,DEDFEMP,DEDFNAA,REFADV,ARRCOUNT,OPTCPF,NONREFMEM,NONREFCOM,FDA,DNSPAY,SPAY,PFW,PAYFORCPF,PAYARREAR,ACTIVEFLAG,MASTER_EMPNAME,EMPFLAG,PENSIONNO,PF,PENSIONCONTRI,FINYEAR,ACC_KEYNO,USERNAME,IPADDRESS,EDITTRANS,DATAFREEZEFLAG,FORM7NARRATION,PCHELDAMT,EMOLUMENTMONTHS,PCCALCAPPLIED,ARREARFLAG,UPDATEDDATE,LATESTMNTHFLAG,ARREARAMOUNT,DEPUTATIONFLAG,DUEEMOLUMENTS,MERGEFLAG,ARREARSBREAKUP,EDITEDDATE,OPCHANGEPF,OPCHANGEPENSIONCONTRI,CALCEMOLUMENTS,SUPPLIFLAG,VRNO,PFCARDNARRATION,REVISEEPFEMOLUMENTS,REVISEEPFEMOLUMENTSFLAG,OTHERORG,OLDAIRPORTCODE,LEAVEFLAG,SWREMARKS,EMPRECOVERYSTS,UPLOADDATE,FORM3REMARKS,CRTLREQIURED,PFCARDNRFLAG,ECRFORM4SUPPLIARRARFLAG,ADDITIONALCONTRI,SPECIALEMOLUMENTSFLAG,CPFACNO_BAK,EMOLUMENTS_B,PENSIONCONTRI_B,FRESHOPFLAG,FORM4FLAG,GROSS  from  "+table2+"  where empflag='Y' and pensionno="
						+ pfId + " and monthyear <='"+updatedDate+"')";				
				log.info(" insert cad_validate " + sql);
				
				st.addBatch(sql);
				int insertCount[] = st.executeBatch();
				log.info(" insert length ...  " + insertCount.length);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
			log.info("error" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}	
		return result;
	}
	
	public ArrayList financeDataReport(EmpMasterBean empSerach, int gridLength,
			int startIndex, String empNameCheck, String unmappedFlag,
			String reportType, String allRecordsFlag, String pfidfrom) {
		PensionBean pensionBean = null;
		ArrayList financedata = new ArrayList();
		String query = this.buildQueryforMapping(empSerach, gridLength,
				startIndex, empNameCheck, "", "", unmappedFlag, allRecordsFlag,
				pfidfrom);
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;

		try {
			con = commonDB.getConnection();
			log.info("query is " + query.toString());
			PreparedStatement pst = null;
			int countGridLength = -1;
			st = con.createStatement();

			rs = st.executeQuery(query);

					while (rs.next()) {
						countGridLength++;
						pensionBean = new PensionBean();

						if (rs.getString("cpfaccno") != null) {
							pensionBean.setCpfAcNo(rs.getString("cpfaccno")
									.toString());
						} else {
							pensionBean.setCpfAcNo("");
						}
						if (rs.getString("employeeno") != null) {
							pensionBean.setEmployeeCode(rs.getString(
									"employeeno").toString());
						} else {
							pensionBean.setEmployeeCode("");
						}
						if (rs.getString("REGION") != null) {
							pensionBean.setRegion(rs.getString("REGION")
									.toString());
						} else {
							pensionBean.setRegion("");
						}
						if (rs.getString("AIRPORTCODE") != null) {
							pensionBean.setAirportCode(rs.getString(
									"AIRPORTCODE").toString());
						} else {
							pensionBean.setAirportCode("");
						}
						if (rs.getString("dateofjoining") != null) {
							pensionBean.setDateofJoining(commonUtil
									.getDatetoString(rs
											.getDate("dateofjoining"),
											"dd-MMM-yyyy"));
						} else {
							pensionBean.setDateofJoining("---");
						}
						if (rs.getString("dateofbirth") != null) {
							pensionBean.setDateofBirth(commonUtil
									.getDatetoString(rs.getDate("dateofbirth"),
											"dd-MMM-yyyy"));
						} else {
							pensionBean.setDateofBirth("---");
						}

						if (rs.getString("employeename") != null) {
							pensionBean.setEmployeeName(rs.getString(
									"employeename").toString());
						} else {
							pensionBean.setEmployeeName("");
						}
						if (rs.getString("desegnation") != null) {
							pensionBean.setDesegnation(rs.getString(
									"desegnation").toString());
						} else {
							pensionBean.setDesegnation("---");
						}
						if (rs.getString("empserialnumber") != null) {
							pensionBean.setPensionnumber(rs.getString(
									"empserialnumber").toString());
						} else {
							pensionBean.setPensionnumber("");
						}
					
						if (unmappedFlag.equals("true")) {
							if (rs.getString("TOTALRECORDS") != null) {
								pensionBean.setTotalRecrods(rs.getString(
										"TOTALRECORDS").toString());
							} else {
								pensionBean.setTotalRecrods("0");
							}
						}
						if (rs.getString("REASON") != null) {
							pensionBean.setSeperationReason(rs.getString(
									"REASON").toString());
						} else {
							pensionBean.setSeperationReason("");
						}
						if (rs.getString("formsdisable") != null) {
							pensionBean.setFormsdisable(rs.getString(
									"formsdisable").toString());
						} else {
							pensionBean.setFormsdisable("");
						}
						if (rs.getString("PCREPORTVERIFIED") != null) {
							pensionBean.setPcreportverified(rs.getString(
									"PCREPORTVERIFIED").toString());
						} else {
							pensionBean.setPcreportverified("");
						}
						if (allRecordsFlag.equals("true")) {
							if (rs.getString("DATEOFSEPERATION_DATE") != null) {
								pensionBean
										.setSeperationDate(commonUtil
												.getDatetoString(
														rs
																.getDate("DATEOFSEPERATION_DATE"),
														"dd-MMM-yyyy"));
							} else {
								pensionBean.setSeperationDate("");
							}
						}
						if (rs.getString("pensioncliamsProcess") != null) {
							pensionBean.setClaimsprocess(rs.getString(
									"pensioncliamsProcess").toString());
						} else {
							pensionBean.setClaimsprocess("");
						}
						financedata.add(pensionBean);
					}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			log.info("inside finally block");
			commonDB.closeConnection(con, st, rs);
		}
		log.info("cadDAO leaving method "+financedata.size());

		return financedata;
	}
	
	public double getInterestforNoofMonths(String interestCalcUpto) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		double noofMonths = 0;
		String sqlQuery = "select round(MONTHS_BETWEEN('" + interestCalcUpto
				+ "', '01-Apr-2008')) as noofmonths from dual";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				noofMonths = rs.getDouble("noofmonths");

			}
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return noofMonths;

	}
	
	public String reIntrestDate(String empserialNO) {
		Connection con = null;
		Statement st = null;
		ResultSet rs1 = null;
		String REIntrestDate = "";
		double noofMonths = 0;
		String currentDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String transQuery = "select REINTERESTDATE from employee_personal_info where pensionno='"
					+ empserialNO + "'";
			log.info("reIntrestDate" + transQuery);
			rs1 = st.executeQuery(transQuery);
			if (rs1.next()) {
				if (rs1.getString("REINTERESTDATE") != null) {
				REIntrestDate = commonUtil.converDBToAppFormat(rs1
						.getString("REINTERESTDATE"), "yyyy-MM-dd",
						"dd-MMM-yyyy");
				
			} 
			}
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs1);
		}
		return REIntrestDate;
	}
	
	public double interestforfinalsettleMonths(String reinterestcalcdate,
			String empserialNO) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String finaldate1 = "";
		double noofMonths = 0;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String transQuery = "select to_date(FINALSETTLMENTDT)+1 as FINALDATE from employee_personal_info where pensionno='"
					+ empserialNO + "'";
			log.info("transQuery====" + transQuery);
			rs1 = st.executeQuery(transQuery);
			String currentDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
			while (rs1.next()) {
				if(rs1.getString("FINALDATE")!=null){
				log.info("finaldate1====" + rs1.getString("FINALDATE"));
				finaldate1 = commonUtil.converDBToAppFormat(rs1
						.getString("FINALDATE"), "yyyy-MM-dd", "dd-MMM-yyyy");
				}
			}
			log.info("finaldate1" + finaldate1);
			
			String sqlQuery = "select round(MONTHS_BETWEEN('"
					+ reinterestcalcdate + "','" + finaldate1
					+ "')) as noofmonths from dual";
			if(!finaldate1.equals("")){
			log.info("interestforfinalsettleMonths" + sqlQuery);
			rs = st.executeQuery(sqlQuery); 
			while (rs.next()) {
				if(rs.getString("noofmonths")!=null){
				noofMonths = rs.getDouble("noofmonths");
				}
			}
			}
			log.info("noofMonths" + noofMonths);
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return noofMonths;

	}
	
	public String finalintrestdate(String interestCalcUpto, String empserialNO) {
		Connection con = null;
		Statement st = null;		
		ResultSet rs1 = null;
		String finaldate = "";
		double noofMonths = 0;
		String currentDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String transQuery = "select to_date(FINALSETTLMENTDT)+1 as FINALDATE from employee_personal_info where pensionno='"
					+ empserialNO + "'";
			rs1 = st.executeQuery(transQuery);
			while (rs1.next()) {
				if (rs1.getString("FINALDATE") != null) {	
				finaldate = commonUtil.converDBToAppFormat(rs1
						.getString("FINALDATE"), "yyyy-MM-dd", "dd-MMM-yyyy");
				}
			}
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs1);
		}
		return finaldate;
	}
	
	public String reSettlementdate(String empserialNO) {
		Connection con = null;
		Statement st = null;		
		ResultSet rs1 = null;
		String ResettlementDate = "";
		double noofMonths = 0;
		String currentDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String transQuery = "select RESETTLEMENTDATE from employee_personal_info where pensionno='"
					+ empserialNO + "'";
			log.info("reIntrestDate" + transQuery);
			rs1 = st.executeQuery(transQuery);
			if (rs1.next()) {
				if (rs1.getString("RESETTLEMENTDATE") != null) {
					log.info("RESETTLEMENTDATE"+rs1.getString("RESETTLEMENTDATE"));
				ResettlementDate = commonUtil.converDBToAppFormat(rs1
						.getString("RESETTLEMENTDATE"), "yyyy-MM-dd",
						"dd-MMM-yyyy");
				log.info("ResettlementDate"+rs1.getString("ResettlementDate"));
				}
			} 
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs1);
		}
		return ResettlementDate;
	}
	
	public ArrayList pensionContributionReport(String fromDate, String toDate,
			String region, String airportcode, String empserialNO,
			String cpfAccno, String transferFlag, String mappingFlag,
			boolean recoverieTable) {
		
		log.info(" pensionContributionReport ");
		ArrayList penContHeaderList = new ArrayList();

		// For Fetching the Employee PersonalInformation
		penContHeaderList = this.pensionContrHeaderInfo(region, airportcode,
				empserialNO, cpfAccno, transferFlag, mappingFlag);

		String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "", empCpfaccno = "";
		String[] cpfaccnos = new String[10];
		String[] regions = new String[10];
		String[] empPensionList = null;
		String[] pensionInfo = null;
		CommonDAO commonDAO = new CommonDAO();
		String pensionList = "", dateOfRetriment = "";
		ArrayList penConReportList = new ArrayList();
		log.info("Header Size" + penContHeaderList.size());	
		String countFlag = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = commonDB.getConnection();
			for (int i = 0; i < penContHeaderList.size(); i++) {
				PensionContBean penContHeaderBean = new PensionContBean();
				PensionContBean penContBean = new PensionContBean();

				penContHeaderBean = (PensionContBean) penContHeaderList.get(i);
				penContBean.setCpfacno(commonUtil
						.duplicateWords(penContHeaderBean.getCpfacno()));

				penContBean.setEmployeeNM(penContHeaderBean.getEmployeeNM());
				penContBean.setEmpDOB(penContHeaderBean.getEmpDOB());
				penContBean.setEmpSerialNo(penContHeaderBean.getEmpSerialNo());

				penContBean.setEmpDOJ(penContHeaderBean.getEmpDOJ());
				penContBean.setGender(penContHeaderBean.getGender());
				penContBean.setFhName(penContHeaderBean.getFhName());
				penContBean.setEmployeeNO(penContHeaderBean.getEmployeeNO());
				penContBean.setDesignation(penContHeaderBean.getDesignation());
				penContBean.setPfSettled(penContHeaderBean.getPfSettled());
				penContBean.setFinalSettlementDate(penContHeaderBean
						.getFinalSettlementDate());
				penContBean.setInterestCalUpto(penContHeaderBean
						.getInterestCalUpto());
				penContBean.setDateofSeperationDt(penContHeaderBean
						.getDateofSeperationDt());				
				if (!penContHeaderBean.getWhetherOption().equals("")) {
					penContBean.setWhetherOption(penContHeaderBean
							.getWhetherOption());
				}
				penContBean.setDateOfEntitle(penContHeaderBean
						.getDateOfEntitle());
				penContBean.setMaritalStatus(penContHeaderBean
						.getMaritalStatus());
				penContBean.setDepartment(penContHeaderBean.getDepartment());
				penContBean.setPensionNo(commonDAO.getPFID(penContBean
						.getEmployeeNM(), penContBean.getEmpDOB(), commonUtil
						.leadingZeros(5, penContHeaderBean.getEmpSerialNo())));
				log.info("penContBean " + penContBean.getPensionNo());
				empSerialNumber = penContHeaderBean.getEmpSerialNo();
				if (empSerialNumber.length() >= 5) {
					empSerialNumber = empSerialNumber.substring(empSerialNumber
							.length() - 5, empSerialNumber.length());
					empSerialNumber = commonUtil.trailingZeros(empSerialNumber
							.toCharArray());
				}
				cpfacno = penContHeaderBean.getCpfacno();
				empRegion = penContHeaderBean.getEmpRegion();

				cpfaccnos = cpfacno.split("=");
				regions = empRegion.split("=");
				double totalAAICont = 0.0, calCPF = 0.0, calPens = 0.0;
				ArrayList employeFinanceList = new ArrayList();
				String preparedString = commonDAO.preparedCPFString(cpfaccnos, regions);
				log.info("preparedString is " + preparedString);
				if (cpfaccnos.length >= 1) {
					for (int k = 0; k < cpfaccnos.length; k++) {
						region = regions[k];
						empCpfaccno = cpfaccnos[k];
					}
				}

				penContBean.setEmpRegion(region);
				penContBean.setEmpCpfaccno(empCpfaccno);
				try {
					if (!penContBean.getEmpDOB().trim().equals("---")
							&& !penContBean.getEmpDOB().trim().equals("")) {
						dateOfRetriment = commonDAO.getRetriedDate(penContBean
								.getEmpDOB());
					}

				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				pensionList = this.getEmployeePensionInfo(preparedString,
						penContHeaderBean.getDateOfEntitle(), toDate,
						penContHeaderBean.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), empserialNO, recoverieTable);
				String checkMnthDate = "", rateOfInterest = "";
				st = con.createStatement();
				if (!pensionList.equals("")) {
					empPensionList = pensionList.split("=");
					System.out.println(" length=== "+empPensionList.length);
					if (empPensionList != null) {
						for (int r = 0; r < empPensionList.length; r++) {
							
							TempPensionTransBean bean = new TempPensionTransBean();
							tempPensionInfo = empPensionList[r];
							pensionInfo = tempPensionInfo.split(",");
							bean.setMonthyear(pensionInfo[0]);
							try {
								checkMnthDate = commonUtil.converDBToAppFormat(
										pensionInfo[0], "dd-MMM-yyyy", "MMM")
										.toLowerCase();
								if (r == 0 && !checkMnthDate.equals("apr")) {
									checkMnthDate = "apr";

								}
								if (checkMnthDate.equals("apr")) {
									rateOfInterest = commonDAO
											.getEmployeeRateOfInterest(
													con,
													commonUtil
															.converDBToAppFormat(
																	pensionInfo[0],
																	"dd-MMM-yyyy",
																	"yyyy"));
									if (rateOfInterest.equals("")) {
										rateOfInterest = "12";
									}
								}
							} catch (InvalidDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bean.setInterestRate(rateOfInterest);							
							checkMnthDate = "";
							bean.setEmoluments(pensionInfo[1]);
							bean.setCpf(pensionInfo[2]);
							bean.setEmpVPF(pensionInfo[3]);
							bean.setEmpAdvRec(pensionInfo[4]);
							bean.setEmpInrstRec(pensionInfo[5]);
							bean.setStation(pensionInfo[6]);							
							bean.setPensionContr(pensionInfo[7]);						
							calCPF = Math.round(Double.parseDouble(bean.getCpf()));
							DateFormat df = new SimpleDateFormat("dd-MMM-yy");
							bean.setDeputationFlag(pensionInfo[19].trim());
							Date transdate = df.parse(pensionInfo[0]);
							if (transdate.before(new Date("31-Mar-08"))
									&& (bean.getDeputationFlag().equals("N") || bean
											.getDeputationFlag().equals(""))) {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[7]));
								totalAAICont = calCPF - calPens;
							} else {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[12]));
								bean.setPensionContr(pensionInfo[12]);
								totalAAICont = calCPF - calPens;
							}
							bean.setAaiPFCont(Double.toString(totalAAICont));							
							bean.setGenMonthYear(pensionInfo[8]);
							bean.setTransCpfaccno(pensionInfo[9]);
							bean.setRegion(pensionInfo[10]);							
							bean.setRecordCount(pensionInfo[11]);
							bean.setDbPensionCtr(pensionInfo[12]);
							bean.setDataFreezFlag(pensionInfo[13]);
							bean.setForm7Narration(pensionInfo[14]);
							bean.setPcHeldAmt(pensionInfo[15]);
							bean.setNoofMonths(pensionInfo[16]);
							bean.setPccalApplied(pensionInfo[17].trim());
							bean.setEditedDate(pensionInfo[21].trim());
							bean.setAddContri(pensionInfo[22]);
							bean.setBenfundContri(pensionInfo[23]);
							bean.setBenfundPension(pensionInfo[24]);
							bean.setGratuityInterest(pensionInfo[25]);
							bean.setEpfoPension(pensionInfo[26]);
						
							if (bean.getPccalApplied().equals("N")) {
								bean.setCpf("0.00");
								bean.setAaiPFCont("0.00");
								bean.setPensionContr("0.00");
								bean.setDbPensionCtr("0.00");
							}
							bean.setArrearFlag(pensionInfo[18].trim());
							bean.setDeputationFlag(pensionInfo[19].trim());						
							String monthYear1 = commonUtil.converDBToAppFormat(
									pensionInfo[0], "dd-MMM-yyyy", "MMM-yyyy");
							String advances = "select amount from CAD_EMPLOYEE_PENSION_ADVANCES where pensionno='"
									+ empSerialNumber
									+ "' and to_char(advtransdate,'Mon-yyyy') like'%"
									+ monthYear1 + "'";
							String loans = "select SUB_AMT,CONT_AMT  from CAD_PENSION_LOANS where pensionno='"
									+ empSerialNumber
									+ "' and to_char(loandate,'Mon-yyyy') like'%"
									+ monthYear1 + "'";
							String advAmount = "0.00";

							ResultSet rs1 = st.executeQuery(advances);
							while (rs1.next()) {
								if (rs1.getString("amount") != null) {
									advAmount = rs1.getString("amount");

								} else {
									advAmount = "0.00";
								}

							}
							bean.setAdvAmount(advAmount);
							String employeeLoan = "0.00", aaiLoan = "0.00";
							ResultSet rs2 = st.executeQuery(loans);
							while (rs2.next()) {
								if (rs2.getString("SUB_AMT") != null) {
									employeeLoan = rs2.getString("SUB_AMT");

								} else {
									employeeLoan = "0.00";
								}
								if (rs2.getString("CONT_AMT") != null) {
									aaiLoan = rs2.getString("CONT_AMT");

								} else {
									aaiLoan = "0.00";
								}
							}
							bean.setEmployeeLoan(employeeLoan);
							bean.setAaiLoan(aaiLoan);							
							if (bean.getRecordCount().equals("Duplicate")) {
								countFlag = "true";
							}
							bean.setRemarks("---");							
							employeFinanceList.add(bean);
						}

					}
				}
				if (!pensionList.equals("")) {
					penContBean.setBlockList(commonDAO.getMonthList(con,
							pensionInfo[20]));
				}
				employeFinanceList = commonDAO
						.chkDuplicateMntsForCpfs(employeFinanceList);
				penContBean.setEmpPensionList(employeFinanceList);
				penContBean.setCountFlag(countFlag);
				penConReportList.add(penContBean);

			}
		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			commonDB.closeConnection(con, st, null);
		}

		return penConReportList;
	}
	
	public ArrayList pensionContributionReportAll(String fromDate,
			String toDate, String region, String airportcode,
			String empserialNO, String cpfAccno, String transferFlag,
			String pfIDStrip, String bulkPrint) {
		// the below line for table check
		boolean recoverieTable = false;
		ArrayList penContHeaderList = new ArrayList();

		if (pfIDStrip.equals("NO-SELECT")) {
			penContHeaderList = this
					.pensionContrPFIDHeaderInfoWthNav(region, airportcode,
							empserialNO, cpfAccno, transferFlag, pfIDStrip);
		} else if (bulkPrint.equals("true")) {
			penContHeaderList = this
					.pensionContrBulkPrintPFIDS(region, airportcode,
							empserialNO, cpfAccno, transferFlag, pfIDStrip);
		} else {
			penContHeaderList = this
					.pensionContrPFIDHeaderInfo(region, airportcode,
							empserialNO, cpfAccno, transferFlag, pfIDStrip);
		}

		String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "";
		String[] cpfaccnos = new String[10];
		String[] dupCpfaccnos = new String[10];
		String[] regions = new String[10];
		String[] empPensionList = null;
		String[] pensionInfo = null;
		CommonDAO commonDAO = new CommonDAO();
		String pensionList = "", tempCPFAcno = "", tempRegion = "", dateOfRetriment = "";
		ArrayList penConReportList = new ArrayList();
		log.info("Header Size" + penContHeaderList.size());
		String dupCpf = "", dupRegion = "", countFlag = "";
		int yearCount = 0;
		Connection con = null;
		try {
			con = commonDB.getConnection();
			for (int i = 0; i < penContHeaderList.size(); i++) {
				PensionContBean penContHeaderBean = new PensionContBean();
				PensionContBean penContBean = new PensionContBean();

				penContHeaderBean = (PensionContBean) penContHeaderList.get(i);
				penContBean.setCpfacno(commonUtil
						.duplicateWords(penContHeaderBean.getCpfacno()));

				penContBean.setEmployeeNM(penContHeaderBean.getEmployeeNM());
				penContBean.setEmpDOB(penContHeaderBean.getEmpDOB());
				penContBean.setEmpSerialNo(penContHeaderBean.getEmpSerialNo());

				penContBean.setEmpDOJ(penContHeaderBean.getEmpDOJ());
				penContBean.setGender(penContHeaderBean.getGender());
				penContBean.setFhName(penContHeaderBean.getFhName());
				penContBean.setEmployeeNO(penContHeaderBean.getEmployeeNO());
				penContBean.setDesignation(penContHeaderBean.getDesignation());
				penContBean.setWhetherOption(penContHeaderBean
						.getWhetherOption());
				penContBean.setDateOfEntitle(penContHeaderBean
						.getDateOfEntitle());
				penContBean.setMaritalStatus(penContHeaderBean
						.getMaritalStatus());
				penContBean.setDepartment(penContHeaderBean.getDepartment());

				penContBean.setPensionNo(commonDAO.getPFID(penContBean
						.getEmployeeNM(), penContBean.getEmpDOB(), commonUtil
						.leadingZeros(5, penContHeaderBean.getEmpSerialNo())));
				log.info("penContBean " + penContBean.getPensionNo());
				empSerialNumber = penContHeaderBean.getEmpSerialNo();

				double totalAAICont = 0.0, calCPF = 0.0, calPens = 0.0;
				ArrayList employeFinanceList = new ArrayList();
				String preparedString = penContHeaderBean.getPrepareString();

				try {
					dateOfRetriment = commonDAO.getRetriedDate(penContBean
							.getEmpDOB());
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList rateList = new ArrayList();
				String findFromDate = "";
				findFromDate = this.compareTwoDates(penContHeaderBean
						.getDateOfEntitle(), fromDate);
				log.info("Find From Date" + findFromDate);
				pensionList = this.getEmployeePensionInfo(preparedString,
						findFromDate, toDate, penContHeaderBean
								.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), penContHeaderBean
								.getEmpSerialNo(), recoverieTable);
				String rateFromYear = "", rateToYear = "", checkMnthDate = "", rateOfInterest = "";
				boolean rateFlag = false;
				if (!pensionList.equals("")) {
					empPensionList = pensionList.split("=");
					log.info("empPensionList.length"+empPensionList.length);
					if (empPensionList != null) {
						for (int r = 0; r < empPensionList.length; r++) {
							TempPensionTransBean bean = new TempPensionTransBean();
							tempPensionInfo = empPensionList[r];
							pensionInfo = tempPensionInfo.split(",");
							log.info("11111111111:::"+pensionInfo[0]);
							log.info("22222222222:::"+pensionInfo[22]);
							bean.setMonthyear(pensionInfo[0]);
							try {
								checkMnthDate = commonUtil.converDBToAppFormat(
										pensionInfo[0], "dd-MMM-yyyy", "MMM")
										.toLowerCase();
								if (r == 0 && !checkMnthDate.equals("apr")) {
									checkMnthDate = "apr";

								}
								if (checkMnthDate.equals("apr")) {
									rateOfInterest = commonDAO
											.getEmployeeRateOfInterest(
													con,
													commonUtil
															.converDBToAppFormat(
																	pensionInfo[0],
																	"dd-MMM-yyyy",
																	"yyyy"));
									if (rateOfInterest.equals("")) {
										rateOfInterest = "12";
									}
								}
							} catch (InvalidDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							bean.setInterestRate(rateOfInterest);
							// log.info("Monthyear"+checkMnthDate+"Interest
							// Rate"
							// +rateOfInterest);
							checkMnthDate = "";
							bean.setEmoluments(pensionInfo[1]);
							bean.setCpf(pensionInfo[2]);
							bean.setEmpVPF(pensionInfo[3]);
							bean.setEmpAdvRec(pensionInfo[4]);
							bean.setEmpInrstRec(pensionInfo[5]);
							bean.setStation(pensionInfo[6]);
							bean.setPensionContr(pensionInfo[7]);
							// calCPF=Double.parseDouble(bean.getCpf());
							// calPens=Double.parseDouble(pensionInfo[7]);
							calCPF = Math.round(Double.parseDouble(bean
									.getCpf()));
							calPens = Math.round(Double
									.parseDouble(pensionInfo[7]));
							DateFormat df = new SimpleDateFormat("dd-MMM-yy");
							Date transdate = df.parse(pensionInfo[0]);
							bean.setDeputationFlag(pensionInfo[19].trim());
							if (transdate.before(new Date("31-Mar-08"))
									&& (bean.getDeputationFlag().equals("N") || bean
											.getDeputationFlag().equals(""))) {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[7]));
								totalAAICont = calCPF - calPens;
							} else {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[12]));
								bean.setPensionContr(pensionInfo[12]);
								totalAAICont = calCPF - calPens;
							}
							bean.setAaiPFCont(Double.toString(totalAAICont));
							bean.setGenMonthYear(pensionInfo[8]);
							bean.setTransCpfaccno(pensionInfo[9]);
							bean.setRegion(pensionInfo[10]);
							bean.setRecordCount(pensionInfo[11]);
							bean.setDbPensionCtr(pensionInfo[12]);
							bean.setForm7Narration(pensionInfo[14]);
							bean.setPcHeldAmt(pensionInfo[15]);
							bean.setPccalApplied(pensionInfo[17].trim());
							
							log.info("PcApplied " + bean.getPccalApplied());
							if (bean.getPccalApplied().equals("N")) {
								bean.setCpf("0.00");
								bean.setAaiPFCont("0.00");
								bean.setPensionContr("0.00");
								bean.setDbPensionCtr("0.00");
							}
							if (bean.getRecordCount().equals("Duplicate")) {
								countFlag = "true";
							}
							bean.setRemarks("---");

							employeFinanceList.add(bean);
						}

					}
				}
				if (!pensionList.equals("")) {
					penContBean.setBlockList(commonDAO.getMonthList(con,
							pensionInfo[20]));

					employeFinanceList = commonDAO
							.chkDuplicateMntsForCpfs(employeFinanceList);
					penContBean.setEmpPensionList(employeFinanceList);
					penContBean.setCountFlag(countFlag);

					penConReportList.add(penContBean);
				}
			}
		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			commonDB.closeConnection(con, null, null);
		}

		return penConReportList;
	}
	
	public String buildQueryforMapping(EmpMasterBean empSerach, int gridLength,
			int startIndex, String empNameCheck, String count, String total,
			String unmappedFlag, String allRecordsFlag, String pfidfrom) {
		PensionBean pensionBean = null;
		ArrayList financedata = new ArrayList();
		String sqlQuery = "";

		if (count == "" && unmappedFlag.equals("true")) {
			sqlQuery = "select * from v_unmapped_personal_info";
		} else if (count != "" && unmappedFlag.equals("true")) {
			sqlQuery = "select count(*)as count from v_unmapped_personal_info";
		} else if (count != "" && allRecordsFlag.equals("true")) {
			sqlQuery = "select count(*)as count  from employee_personal_info i";
		} else if (count == "" && allRecordsFlag.equals("true")) {
			sqlQuery = "select t.status,i.pensionno as empserialnumber,i.employeeno,i.desegnation,airportcode,cpfacno as cpfaccno,employeename,dateofbirth,dateofjoining,pfsettled,region,DATEOFSEPERATION_REASON as REASON,DATEOFSEPERATION_DATE,pcreportverified,formsdisable,pensioncliamsProcess from employee_personal_info i,cad_tracking t where i.pensionno = t.pensionno (+) ";
		} else if (count == "") {
			sqlQuery = "select * from (select * from mv_employee_Mapping";
		}

		else {
			sqlQuery = "select count(*) as count from (select * from mv_employee_Mapping";

		}

		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", prefixWhereClause = "";

		if (!empSerach.getCpfAcNo().equals("")) {
			whereClause.append(" LOWER(cpfaccno)='"
					+ empSerach.getCpfAcNo().toLowerCase().trim() + "'");
			whereClause.append(" AND ");
		}
		if (!empSerach.getEmpNumber().equals("")) {
			whereClause.append(" LOWER(employeeno)='"
					+ empSerach.getEmpNumber().toLowerCase().trim() + "'");
			whereClause.append(" AND ");
		}
		if (!empSerach.getEmpName().equals("") && empNameCheck.equals("true")) {
			whereClause.append(" LOWER(employeename) like'%"
					+ empSerach.getEmpName().toLowerCase().trim() + "%'");
			whereClause.append(" AND ");
		}
		if (!empSerach.getEmpName().equals("") && empNameCheck.equals("false")) {
			whereClause.append(" LOWER(employeename) like'"
					+ empSerach.getEmpName().toLowerCase().trim() + "%'");
			whereClause.append(" AND ");
		}

		if (!empSerach.getRegion().equals("")) {
			whereClause.append(" LOWER(region)='"
					+ empSerach.getRegion().trim().toLowerCase() + "'");
			whereClause.append(" AND ");

		}
		if (!empSerach.getStation().equals("")) {
			whereClause.append(" LOWER(airportcode)='"
					+ empSerach.getStation().trim().toLowerCase() + "'");
			whereClause.append(" AND ");

		}

		if (!empSerach.getPfid().equals("") && !allRecordsFlag.equals("true")) {
			whereClause.append(" LOWER(EMPSERIALNUMBER)='"
					+ empSerach.getPfid().toLowerCase().trim() + "'");
			whereClause.append(" AND ");
		} else if (!empSerach.getPfid().equals("")
				&& allRecordsFlag.equals("true")) {
			whereClause.append(" LOWER(i.pensionno)='"
					+ empSerach.getPfid().toLowerCase().trim() + "'");
			whereClause.append(" AND ");
		}
		if (!empSerach.getPcverified().equals("")) {
			whereClause.append(" PCREPORTVERIFIED='"
					+ empSerach.getPcverified().trim() + "'");
			whereClause.append(" AND ");
		}

		query.append(sqlQuery);
		if (empSerach.getEmpName().equals("")
				&& empSerach.getRegion().equals("")
				&& empSerach.getCpfAcNo().equals("")
				&& empSerach.getEmpNumber().equals("")
				&& unmappedFlag.equals("false")
				&& allRecordsFlag.equals("false")
				&& empSerach.getPfid().equals("")
				&& empSerach.getStation().equals("")) {
			;
		} else if ((unmappedFlag.equals("true") || allRecordsFlag
				.equals("true"))
				&& (!empSerach.getRegion().equals("") || (!empSerach
						.getStation().equals("")
						|| (!empSerach.getPfid().equals(""))
						|| (!empSerach.getCpfAcNo().equals(""))
						|| (!empSerach.getEmpName().equals("")) || (!empSerach
						.getPcverified().equals(""))))) {
			if(count != ""){
				query.append(" where ");
			}else{
				query.append(" and ");
			}
			query.append(this.sTokenFormat(whereClause));
		} else if (unmappedFlag.equals("true") || allRecordsFlag.equals("true")) {

		} else if ((!empSerach.getRegion().equals("") || (!empSerach
				.getStation().equals("")
				|| (!empSerach.getPfid().equals(""))
				|| (!empSerach.getCpfAcNo().equals("")) || (!empSerach
				.getEmpName().equals(""))))) {
			query.append(" where ");
			query.append(this.sTokenFormat(whereClause));
		}
		log.info(query.toString());
		String orderBy = "";
		if (count == "" && unmappedFlag.equals("true")) {
			// orderBy=" order by trim(employeename),cpfacno ";
		} else if (count != "" && unmappedFlag.equals("true")) {
			// orderBy=" order by trim(employeename),cpfacno";
		} else if (allRecordsFlag.equals("true")) {
			if (!pfidfrom.equals("")) {
				int pfidfrm = Integer.parseInt(pfidfrom);
				int pfidto = 23500;
				orderBy = " where pensionno between " + pfidfrm + " and "
						+ pfidto + " order by pensionno";
			} else {
				orderBy = " order by i.pensionno";
			}
		} else if (!empSerach.getEmpName().equals("")
				|| (!empSerach.getCpfAcNo().equals("")
						|| !empSerach.getPfid().equals("") || !empSerach
						.getEmpNumber().equals(""))) {
			orderBy = "and transferflag='Y'  and mappingflag='N' order by trim(employeename),cpfaccno)";
		} else if (count == "" && !allRecordsFlag.equals("true")) {
			orderBy = " where transferflag='Y' and mappingflag='N' order by trim(employeename),cpfaccno) ";
		} else if (total != "" && count != "") {
			orderBy = " where transferflag='Y'  and mappingflag='N' order by trim(employeename),cpfaccno) ";
		} else {
			orderBy = " where transferflag='Y' and mappingflag='N' order by trim(employeename),cpfaccno) ";
		}
		query.append(orderBy);
		return query.toString();
	}
	
	private ArrayList pensionContrHeaderInfo(String region, String airportCD,
			String empserialNO, String cpfAccno, String transferFlag,
			String mappingFlag) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		log.info(" pensionContrHeaderInfo  " + region);
		if (mappingFlag.equals("true")) {
			// The Below mentioned method for retrieving the mapping info from
			// PensionContribution screen hitting
			sqlQuery = this.buildPenContMappingQuery(region, airportCD,
					empserialNO, cpfAccno);
		} else {
			// The Below mentioned method for retrieving the mapping info from
			// PensionContribution Report hitting
			sqlQuery = this.buildPenContRptQuery(region, airportCD,
					empserialNO, cpfAccno, transferFlag);
		}

		log.info("pensionContrHeaderInfo === Query " + sqlQuery);
		String tempSrlNumber = "", srlNumber = "", doj = "", dob = "", cpfacnos = "", regions = "", fhName = "", employeeName = "", designation = "";
		String tempRegion = "", tempCPF = "", department = "";
		String finalSettlementdate = "";
		ArrayList finalList = new ArrayList();
		int totalRS = 0, tempTotalRs = 0, totalSrlNo = 0, totalRecCpf = 0;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			ArrayList list = new ArrayList();
			PensionContBean bean = new PensionContBean();
			totalRS = this.getEmpPensionCount(empserialNO);
			String wetherOption = "", pfSettled = "", interestCalcUpto = "", dateofSeperationDate = "";
			while (rs.next()) {
				tempTotalRs++;
				// code modified as on Feb 19th
				if (rs.getString("employeename") != null) {
					employeeName = rs.getString("employeename");
				}
				if (rs.getString("wetheroption") != null) {
					wetherOption = rs.getString("wetheroption");
				}
				if (rs.getString("dateofbirth") != null) {
					dob = CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH"));
				}
				if (rs.getString("dateofjoining") != null) {
					doj = CommonUtil.converDBToAppFormat(rs
							.getDate("dateofjoining"));
				}
				if (rs.getString("FHNAME") != null) {
					fhName = rs.getString("FHNAME");
				}
				if (rs.getString("DEPARTMENT") != null) {
					department = rs.getString("DEPARTMENT");
				}
				if (rs.getString("DESEGNATION") != null) {
					designation = rs.getString("DESEGNATION");
				}
				if (rs.getString("PFSETTLED") != null) {
					pfSettled = rs.getString("PFSETTLED");
				}
				if (rs.getString("interestCalUpto") != null) {
					interestCalcUpto = rs.getString("interestCalUpto");
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					dateofSeperationDate = rs
							.getString("DATEOFSEPERATION_DATE");
				}
				if (rs.getString("EMPSERIALNUMBER") != null) {
					if (tempSrlNumber.equals("")) {
						tempSrlNumber = rs.getString("EMPSERIALNUMBER");
					} else if (!tempSrlNumber.equals(rs
							.getString("EMPSERIALNUMBER"))) {
						tempRegion = "";
						tempCPF = "";
						cpfacnos = "";
						regions = "";
						if (totalSrlNo > 0) {
							finalList.add(bean);
							bean = null;
							bean = new PensionContBean();
							totalSrlNo = 0;
						}
						tempSrlNumber = rs.getString("EMPSERIALNUMBER");
					}
					if (tempSrlNumber.equals(rs.getString("EMPSERIALNUMBER"))) {
						totalSrlNo++;
						if (tempRegion.equals("") && tempCPF.equals("")) {
							tempRegion = rs.getString("REGION").trim();
							tempCPF = rs.getString("CPFACNO").trim();
							cpfacnos = cpfacnos + "=" + rs.getString("CPFACNO");
							regions = regions + "=" + rs.getString("REGION");
						} else if (!(tempRegion.equals("") && tempCPF
								.equals(""))
								&& tempRegion.trim().equals(
										rs.getString("REGION").trim())
								&& tempCPF.trim().equals(
										rs.getString("CPFACNO").trim())) {
							cpfacnos = cpfacnos;
							regions = regions;
						} else if (!(tempRegion.equals("") && tempCPF
								.equals(""))
								&& ((!tempRegion.equals(rs.getString("REGION")
										.trim()) && !tempCPF.equals(rs
										.getString("CPFACNO").trim()))
										|| (!tempRegion.equals(rs.getString(
												"REGION").trim()) && tempCPF
												.equals(rs.getString("CPFACNO")
														.trim())) || (tempRegion
										.equals(rs.getString("REGION").trim()) && !tempCPF
										.equals(rs.getString("CPFACNO").trim())))) {
							tempRegion = rs.getString("REGION").trim();
							tempCPF = rs.getString("CPFACNO").trim();
							cpfacnos = cpfacnos + "=" + rs.getString("CPFACNO");
							regions = regions + "=" + rs.getString("REGION");
						}

						bean = this.loadEmployeeInfo(rs, cpfacnos, regions);
						bean.setEmployeeNM(employeeName);
						bean.setWhetherOption(wetherOption);
						bean.setEmpDOB(dob);
						bean.setEmpDOJ(doj);
						bean.setFhName(fhName);
						bean.setDepartment(department);
						bean.setDesignation(designation);
						bean.setPfSettled(pfSettled);
						bean.setInterestCalUpto(interestCalcUpto);
						bean.setDateofSeperationDt(dateofSeperationDate);
					}
					if (tempTotalRs == totalRS) {
						finalList.add(bean);
					}
				} else {
					if (totalRecCpf == 0) {
						totalRecCpf++;
						bean = this.loadEmployeeInfo(rs, rs
								.getString("CPFACNO"), region);
						bean.setWhetherOption(wetherOption);
						bean.setEmpDOB(dob);
						bean.setEmpDOJ(doj);
						bean.setFhName(fhName);
						bean.setDepartment(department);
						bean.setInterestCalUpto(interestCalcUpto);
						bean.setDateofSeperationDt(dateofSeperationDate);
						finalList.add(bean);
					}
				}
			}
			// log.info("tempSrlNumber" + tempSrlNumber + "bean.cpfacnos"
					//+ bean.getCpfacno() + "regions" + regions
				//	+ finalList.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return finalList;
	}
	
	private String getEmployeePensionInfo(String cpfString, String fromDate,
			String toDate, String whetherOption, String dateOfRetriment,
			String dateOfBirth, String Pensionno, boolean recoverieTable) {
		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		
		
		String tablename = "CAD_VALIDATE";
		
		if (recoverieTable == true) {
			tablename = "CAD_VALIDATE";
		}
		log.info(" getEmployeePensionInfo ");
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (ADD_MONTHS(dateofjoining,1),'dd-Mon-yyyy'),'01-Apr-1989') from employee_info where ("
				+ tempCpfString + ") and rownum=1)";
		log.info(" dojQuery  "+dojQuery);
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI,emp.additionalcontri as AdditionalContribution,emp.ben_fund_contribution as BenfundContribution ,emp.ben_fund_pension as BenfundPension,emp.gratuity_interest_thereon as GratuityInterestThereon,emp.epfo_pension as EpfoPension  from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from "
				+ tablename
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,additionalcontri,ben_fund_contribution ,ben_fund_pension,gratuity_interest_thereon,epfo_pension FROM "
				+ tablename
				+ "  WHERE  empflag='Y' and ("
				+ cpfString
				+ " "
				+ condition
				+ ") AND MONTHYEAR>= TO_DATE('"
				+ fromDate
				+ "','DD-MON-YYYY') and empflag='Y' ORDER BY TO_DATE(MONTHYEAR, 'dd-Mon-yy')) emp  where empdt.monyear = emp.empmonyear(+)   and empdt.monyear in (select to_char(MONTHYEAR,'MONYYYY')monyear FROM "
				+ tablename
				+ " WHERE  empflag='Y' and  ("
				+ cpfString
				+ "  "
				+ condition
				+ ") and  MONTHYEAR >= TO_DATE('"
				+ fromDate
				+ "', 'DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)"
				+ " union	 (select TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||  SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR, emp.MONTHYEAR AS MONTHYEAR,"
				+ " emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,"
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI, emp.additionalcontri as AdditionalContribution,emp.ben_fund_contribution as BenfundContribution ,emp.ben_fund_pension as BenfundPension,emp.gratuity_interest_thereon as GratuityInterestThereon,emp.epfo_pension as EpfoPension  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,additionalcontri,ben_fund_contribution,ben_fund_pension,gratuity_interest_thereon,epfo_pension "
				+ " FROM "
				+ tablename
				+ "   WHERE empflag = 'Y' and ("
				+ cpfString
				+ " "
				+ condition
				+ " ) AND MONTHYEAR >= TO_DATE('"
				+ fromDate
				+ "', 'DD-MON-YYYY') and "
				+ " empflag = 'Y'  ORDER BY TO_DATE(MONTHYEAR, 'dd-Mon-yy')) emp where empdt.monyear = emp.empmonyear(+)   and empdt.monyear not in (select to_char(MONTHYEAR,'MONYYYY')monyear FROM "
				+ tablename
				+ " WHERE  empflag='Y' and  ("
				+ cpfString
				+ " "
				+ condition
				+ ") AND MONTHYEAR >= TO_DATE('"
				+ fromDate
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('01-Apr-1989')";

	
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		String monthsBuffer = "", formatter = "", tempMntBuffer = "";
		long transMntYear = 0, empRetriedDt = 0;
		double pensionCOntr = 0;
		double pensionCOntr1 = 0;
		String recordCount = "";
		int getDaysBymonth = 0, cnt = 0, checkMnts = 0, chkPrvmnth = 0, chkCrntMnt = 0;
		double PENSIONCONTRI = 0;
		boolean contrFlag = false, chkDOBFlag = false, formatterFlag = false;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			log.info(sqlQuery);
			
			FileWriter fw =new FileWriter(new File("D://Lal.txt"));
			fw.write(sqlQuery);
			fw.flush();
			rs = st.executeQuery(sqlQuery);
			log.info("Query" + sqlQuery);
			// log.info("Query" +sqlQuery1);
			String monthYear = "", days = "";
			int i = 0, count = 0;
			String marMnt = "", prvsMnth = "", crntMnth = "", frntYear = "";
			while (rs.next()) {

				if (rs.getString("MONTHYEAR") != null) {
					monthYear = rs.getString("MONTHYEAR");
					buffer.append(rs.getString("MONTHYEAR"));
				} else {
					i++;
					monthYear = commonUtil.converDBToAppFormat(rs
							.getDate("EMPMNTHYEAR"), "MM/dd/yyyy");
					buffer.append(monthYear);

				}
				buffer.append(",");
				count++;
				if (rs.getString("EMOLUMENTS") != null) {
					buffer.append(rs.getString("EMOLUMENTS"));
				} else {
					buffer.append("0");
				}
				buffer.append(",");
				if (rs.getString("EMPPFSTATUARY") != null) {
					buffer.append(rs.getString("EMPPFSTATUARY"));
				} else {
					buffer.append("0");
				}

				buffer.append(",");
				if (rs.getString("EMPVPF") != null) {
					buffer.append(rs.getString("EMPVPF"));
				} else {
					buffer.append("0");
				}

				buffer.append(",");
				if (rs.getString("EMPADVRECPRINCIPAL") != null) {
					buffer.append(rs.getString("EMPADVRECPRINCIPAL"));
				} else {
					buffer.append("0");
				}

				buffer.append(",");
				if (rs.getString("EMPADVRECINTEREST") != null) {
					buffer.append(rs.getString("EMPADVRECINTEREST"));
				} else {
					buffer.append("0");
				}

				buffer.append(",");

				if (rs.getString("AIRPORTCODE") != null) {
					buffer.append(rs.getString("AIRPORTCODE"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				String region = "";
				if (rs.getString("region") != null) {
					region = rs.getString("region");
				} else {
					region = "-NA-";
				}

				if (!monthYear.equals("-NA-") && !dateOfRetriment.equals("")) {
					transMntYear = Date.parse(monthYear);
					empRetriedDt = Date.parse(dateOfRetriment);
					/*
					 * log.info("monthYear" + monthYear + "dateOfRetriment" +
					 * dateOfRetriment);
					 */
					if (transMntYear > empRetriedDt) {
						contrFlag = true;
					} else if (transMntYear == empRetriedDt) {
						chkDOBFlag = true;
					}
				}

				if (rs.getString("EMOLUMENTS") != null) {
					// log.info("whetherOption==="+whetherOption+"Month
					// Year===="+rs.getString("MONTHYEAR"));
					if (contrFlag != true) {
						pensionCOntr = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), whetherOption,
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntr = Math.round(pensionCOntr
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}

					} else {
						pensionCOntr = 0;
					}
					
					log.info(" pension conf after query ## "+pensionCOntr);
					buffer.append(Double.toString(pensionCOntr));
				} else {
					buffer.append("0");
				}
				buffer.append(",");
				if (rs.getDate("EMPMNTHYEAR") != null) {
					buffer.append(commonUtil.converDBToAppFormat(rs
							.getDate("EMPMNTHYEAR")));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				if (rs.getString("CPFACCNO") != null) {
					buffer.append(rs.getString("CPFACCNO"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				if (rs.getString("region") != null) {
					buffer.append(rs.getString("region"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				// log.info(rs.getString("Dupflag"));
				if (rs.getString("Dupflag") != null) {
					recordCount = rs.getString("Dupflag");
					buffer.append(rs.getString("Dupflag"));
				}
				buffer.append(",");
				DateFormat df = new SimpleDateFormat("dd-MMM-yy");
				Date transdate = df.parse(monthYear);
				
		 	 if (transdate.after(new Date("31-Mar-08"))
						|| (rs.getString("Deputationflag").equals("Y"))) {
					if (rs.getString("PENSIONCONTRI") != null) {
						PENSIONCONTRI = Double.parseDouble(rs
								.getString("PENSIONCONTRI"));
						buffer.append(rs.getString("PENSIONCONTRI"));
					} else {
						buffer.append("0.00");
					}
				} else if (rs.getString("EMOLUMENTS") != null) {
					//log.info("inside emoluments=====================");
					if (contrFlag != true) {
						//log.info("inside contrFlag=====================");
						if (transdate.before(new Date("31-Mar-95")) && rs.getString("PENSIONCONTRI") != null ){
							pensionCOntr1 = Double.parseDouble(rs
									.getString("PENSIONCONTRI"));
						}else{
						pensionCOntr1 = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), whetherOption,
								region, rs.getString("emolumentmonths"));
						}
						if (chkDOBFlag == true) {
							//log.info("inside chkDOBFlag=====================");
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntr1 = Math.round(pensionCOntr1
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}
					}else if (transdate.before(new Date("31-Mar-95")) && rs.getString("PENSIONCONTRI") != null ){
						//log.info("inside if "+rs.getString("PENSIONCONTRI"));
						pensionCOntr1 = Double.parseDouble(rs
								.getString("PENSIONCONTRI"));
					} else {
						pensionCOntr1 = 0;
					}
					buffer.append(Double.toString(pensionCOntr1));
				} else if (transdate.before(new Date("31-Mar-95")) && rs.getString("EMOLUMENTS") == null && rs.getString("PENSIONCONTRI") != null) {
					pensionCOntr1 = Double.parseDouble(rs
							.getString("PENSIONCONTRI"));
					buffer.append(Double.toString(pensionCOntr1));
				}else{
					buffer.append("0");
				}
				buffer.append(",");
				
				log.info(" inside after "+rs.getString("PENSIONCONTRI"));
				if (rs.getString("Datafreezeflag") != null) {
					buffer.append(rs.getString("Datafreezeflag"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				if (rs.getString("FORM7NARRATION") != null) {
					buffer.append(rs.getString("FORM7NARRATION"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("pcHeldAmt") != null) {
					buffer.append(rs.getString("pcHeldAmt"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("emolumentmonths") != null) {
					buffer.append(rs.getString("emolumentmonths"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PCCALCAPPLIED") != null) {
					buffer.append(rs.getString("PCCALCAPPLIED"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("ARREARFLAG") != null) {
					buffer.append(rs.getString("ARREARFLAG"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("Deputationflag") != null) {
					buffer.append(rs.getString("Deputationflag"));
				} else {
					buffer.append("N");
				}
				buffer.append(",");
				
				

				monthYear = commonUtil.converDBToAppFormat(monthYear,
						"dd-MMM-yyyy", "MM-yy");

				crntMnth = monthYear.substring(0, 2);
				if (monthYear.substring(0, 2).equals("02")) {
					marMnt = "_03";
				} else {
					marMnt = "";
				}
				if (monthsBuffer.equals("")) {
					cnt++;

					if (!monthYear.equals("04-95")) {
						String[] checkOddEven = monthYear.split("-");
						int mntVal = Integer.parseInt(checkOddEven[0]);
						if (mntVal % 2 != 0) {
							monthsBuffer = "00-00" + "#" + monthYear + "_03";
							cnt = 0;
							formatterFlag = true;
						} else {
							monthsBuffer = monthYear + marMnt;
							formatterFlag = false;
						}

					} else {

						monthsBuffer = monthYear + marMnt;
					}

					// log.info("Month Buffer is blank"+monthsBuffer);
				} else {
					cnt++;
					if (cnt == 2) {
						formatter = "#";
						cnt = 0;
					} else {
						formatter = "@";
					}

					if (!prvsMnth.equals("") && !crntMnth.equals("")) {

						chkPrvmnth = Integer.parseInt(prvsMnth);
						chkCrntMnt = Integer.parseInt(crntMnth);
						checkMnts = chkPrvmnth - chkCrntMnt;
						if (checkMnts > 1 && checkMnts < 9) {
							frntYear = prvsMnth;
						}
						prvsMnth = "";

					}
				 if (frntYear.equals("")) {
						monthsBuffer = monthsBuffer + formatter + monthYear
								+ marMnt.trim();
					} else if (!frntYear.equals("")) {
						

						monthsBuffer = monthsBuffer + "*" + frntYear
								+ formatter + monthYear;

					}
					if (prvsMnth.equals("")) {
						prvsMnth = crntMnth;
					}

				}
				frntYear = "";

				buffer.append(monthsBuffer.toString());
				buffer.append(",");
				if (rs.getString("editeddate") != null) {
					buffer.append(rs.getString("editeddate"));
				} else {
					buffer.append(" ");
				}
				if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
					buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
				} else {
					buffer.append("N");
				}
				buffer.append(",");
				if (rs.getString("AdditionalContribution") != null) {
					buffer.append(rs.getString("AdditionalContribution"));
				
				} else {
					
					buffer.append("0");
				}	
				
				buffer.append(",");
				if (rs.getString("BenfundContribution") != null) {
					buffer.append(rs.getString("BenfundContribution"));
				
				} else {
					
					buffer.append("0");
				}	
				
				buffer.append(",");
				if (rs.getString("BenfundPension") != null) {
					buffer.append(rs.getString("BenfundPension"));
				
				} else {
					
					buffer.append("0");
				}	
				
				buffer.append(",");
				if (rs.getString("GratuityInterestThereon") != null) {
					buffer.append(rs.getString("GratuityInterestThereon"));
				
				} else {
					
					buffer.append("0");
				}	
				
				buffer.append(",");
				if (rs.getString("EpfoPension") != null) {
					buffer.append(rs.getString("EpfoPension"));
				
				} else {
					
					buffer.append("0");
				}	
				
				buffer.append("=");
							
			}
		
			if (count == i) {
				buffer = new StringBuffer();
			} else {

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return buffer.toString();
	}
	
	private ArrayList pensionContrPFIDHeaderInfoWthNav(String region,
			String airportCD, String empserialNO, String cpfAccno,
			String transferFlag, String pfidString) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sqlQuery = "", prCPFString = "";
		// log.info("region in reportDao******** "+region);
		if (!transferFlag.equals("")) {
			sqlQuery = this.buildPenContRptQuery(region, airportCD,
					empserialNO, cpfAccno, transferFlag);
		} else {
			sqlQuery = this.buildPenContRptWthOutTransferQuery(region,
					airportCD, empserialNO, cpfAccno);
		}

		PersonalDAO personalDAO = new PersonalDAO();
		PensionContBean data = null;
		CommonDAO commonDao = new CommonDAO();
		log.info("pensionContrHeaderInfo===Query" + sqlQuery);
		ArrayList empinfo = new ArrayList();
		try {
			con = commonDB.getConnection();
			pst = con
					.prepareStatement(sqlQuery,
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			rs = pst.executeQuery();
			while (rs.next()) {
				data = new PensionContBean();
				data = loadEmployeeInfo(rs);
				prCPFString = commonDao.getCPFACCNOByPension(data
						.getEmpSerialNo(), data.getCpfacno(), data
						.getEmpRegion());
				String[] cpfaccnoList = prCPFString.split("=");
				String cpfString = personalDAO.preparedCPFString(cpfaccnoList);
				log.info(data.getEmpSerialNo() + "-" + cpfString);
				data.setPrepareString(cpfString);
				empinfo.add(data);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, pst, rs);
		}
		return empinfo;
	}
	
	private ArrayList pensionContrBulkPrintPFIDS(String region,
			String airportCD, String empserialNO, String cpfAccno,
			String transferFlag, String pfidString) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sqlQuery = "", prCPFString = "", stationString = "";
		log.info("pensionContrBulkPrintPFIDS===region" + region + "airportCD"
				+ airportCD);
		if (!airportCD.equals("NO-SELECT")) {
			stationString = airportCD;
		} else {
			stationString = "";
		}
		sqlQuery = commonDAO.buildQueryEmpPFTransInfoPrinting(pfidString, region,
				"false", "", "PENSIONNO", empserialNO, "false", stationString,
				"Y", "2008-2009","","");
		PersonalDAO personalDAO = new PersonalDAO();
		PensionContBean data = null;
		CommonDAO commonDao = new CommonDAO();
		log.info("pensionContrBulkPrintPFIDS===Query" + sqlQuery);
		ArrayList empinfo = new ArrayList();
		try {
			con = DBUtils.getConnection();
			pst = con
					.prepareStatement(sqlQuery,
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			rs = pst.executeQuery();
			while (rs.next()) {
				data = new PensionContBean();
				data = loadEmployeeInfo(rs);
				prCPFString = commonDao.getCPFACCNOByPension(data
						.getEmpSerialNo(), data.getCpfacno(), data
						.getEmpRegion());

				String cpfString = "";
				if (!prCPFString.equals("---")) {
					String[] cpfaccnoList = prCPFString.split("=");

					cpfString = personalDAO.preparedCPFString(cpfaccnoList);
				} else {
					cpfString = "";
				}
				log.info(data.getEmpSerialNo() + "-" + cpfString);
				data.setPrepareString(cpfString);
				if (!cpfString.equals("")) {
					empinfo.add(data);
				}

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, pst, rs);
		}
		return empinfo;
	}
	
	private ArrayList pensionContrPFIDHeaderInfo(String region,
			String airportCD, String empserialNO, String cpfAccno,
			String transferFlag, String pfidString) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		log.info("empserialNO " + empserialNO);
		String sqlQuery = "", prCPFString = "";
		// log.info("pensionContrPFIDHeaderInfo::region"+region);
		int startIndex = 0, endIndex = 0, countGridLength = 0;
		int totalSize = 0;
		ResourceBundle bundle = ResourceBundle
				.getBundle("aims.resource.ApplicationResouces");
		if (bundle.getString("common.pension.pagesize") != null) {
			totalSize = Integer.parseInt(bundle
					.getString("common.pension.pagesize"));
		} else {
			totalSize = 100;
		}
		if (!pfidString.equals("")) {
			if (!pfidString.equals("1 - 1")) {
				String[] pfidList = pfidString.split(" - ");

				startIndex = Integer.parseInt(pfidList[0]);
				if (!pfidList[1].trim().equals("END")) {
					endIndex = Integer.parseInt(pfidList[1]);
				} else {
					endIndex = totalSize;
				}
			} else {
				pfidString = "";
			}

		}

		sqlQuery = this.buildPCReportQuery(region, airportCD, empserialNO,
				cpfAccno, transferFlag, startIndex, endIndex, pfidString);
		PersonalDAO personalDAO = new PersonalDAO();
		PensionContBean data = null;
		CommonDAO commonDao = new CommonDAO();
		log.info("pensionContrHeaderInfo===Query" + sqlQuery);
		ArrayList empinfo = new ArrayList();
		try {
			con = DBUtils.getConnection();
			pst = con
					.prepareStatement(sqlQuery,
							ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			rs = pst.executeQuery();
			// log.info("startIndex"+startIndex+"endIndex"+endIndex);
			while (rs.next()) {
				data = new PensionContBean();
				/*
				 * log.info("loadPersonalInfo=======WHILE=======" +
				 * rs.getString("cpfacno") + "countGridLength" +
				 * countGridLength);
				 */
				data = loadEmployeeInfo(rs);
				prCPFString = commonDao.getCPFACCNOByPension(data
						.getEmpSerialNo(), data.getCpfacno(), data
						.getEmpRegion());
				String cpfString = "";
				if (!("---".equals(prCPFString))) {
					String[] cpfaccnoList = prCPFString.split("=");

					log
							.info("======================================================"
									+ prCPFString);

					cpfString = personalDAO.preparedCPFString(cpfaccnoList);
					log.info(data.getEmpSerialNo() + "" + cpfString);
					data.setPrepareString(cpfString);
					empinfo.add(data);
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, pst, rs);
		}
		return empinfo;
	}
	private String compareTwoDates(String dateOfEntitle, String fromDate) {
		long lDoE = 0, lFrDt = 0;
		String fndDate = "";
		lDoE = Date.parse(dateOfEntitle);
		lFrDt = Date.parse(fromDate);
		log.info("Date Of Entile" + dateOfEntitle + "fromDate" + fromDate);
		log.info("Date Of Entile" + lDoE + "fromDate" + lFrDt);
		if (lDoE >= lFrDt) {
			fndDate = dateOfEntitle;

		} else {
			fndDate = fromDate;
		}
		return fndDate;
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
	
	public int getEmpPensionCount(String empserialNO) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		FinancialYearBean yearBean = new FinancialYearBean();
		if (empserialNO.equals("")) {
			sqlQuery = "SELECT COUNT(*) AS COUNT FROM EMPLOYEE_INFO WHERE empserialnumber is not null  ORDER BY empserialnumber";

		} else {
			sqlQuery = "SELECT COUNT(*) AS COUNT FROM EMPLOYEE_INFO WHERE empserialnumber is not null  and empserialnumber='"
					+ empserialNO + "' ORDER BY empserialnumber";
		}
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return count;
	}
	
	public String buildPenContMappingQuery(String region, String airportCD,
			String empserialNO, String cpfAccno) {
		log
				.info(" cadDAO  :buildPenContMappingQuery() entering method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String Query = "";
		String finalsettlementDateQuery = "";
		String dynamicQuery = "";
		String finalsettlementdate = "";
		String personalfinalsettlementdate = "";
		String interestCalUpto = "";
		String dateofSeperationDate = "";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = commonDB.getConnection();
			st = conn.createStatement();
			Query = "select SETTLEMENTDATE from employee_pension_finsettlement where pensionno='"
					+ empserialNO + "'";
			rs = st.executeQuery(Query);
			log.info("Query" + Query);
			if (rs.next()) {
				if (rs.getString("SETTLEMENTDATE") != null) {
					finalsettlementdate = commonUtil.converDBToAppFormat(rs
							.getDate("SETTLEMENTDATE"));
				}
			}
			finalsettlementDateQuery = "select FINALSETTLMENTDT,DATEOFSEPERATION_DATE,INTERESTCALCDATE from employee_personal_info where pensionno='"
					+ empserialNO + "'";
			rs = st.executeQuery(finalsettlementDateQuery);
			log.info("Query" + finalsettlementDateQuery);
			if (rs.next()) {
				// in screen we are showing seperation date in Final settlement
				// date place
				/*
				 * if (rs.getString("FINALSETTLMENTDT") != null) {
				 * personalfinalsettlementdate =
				 * commonUtil.converDBToAppFormat(rs
				 * .getDate("FINALSETTLMENTDT")); }
				 */
				if (rs.getString("FINALSETTLMENTDT") != null) {
					personalfinalsettlementdate = commonUtil
							.converDBToAppFormat(rs.getDate("FINALSETTLMENTDT"));
				}
				if (rs.getString("INTERESTCALCDATE") != null) {
					interestCalUpto = commonUtil.converDBToAppFormat(rs
							.getDate("INTERESTCALCDATE"));
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					dateofSeperationDate = commonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE"));
				}
			}
			String settlementDate = "";
			settlementDate = personalfinalsettlementdate;
			/*
			 * if (finalsettlementdate.equals("")) { settlementDate =
			 * personalfinalsettlementdate; } else{
			 * settlementDate=finalsettlementdate; }
			 */

			if (!cpfAccno.equals("") && empserialNO.equals("")) {
				sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,REGION,'"
						+ settlementDate
						+ "' AS FINALSETTLMENTDT,'"
						+ interestCalUpto
						+ "' as InterestCalupto,'"
						+ dateofSeperationDate
						+ "'as DATEOFSEPERATION_DATE,MARITALSTATUS,PENSIONNUMBER,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_INFO WHERE region='"
						+ region + "' and cpfacno='" + cpfAccno + "'  ";
			} else if (empserialNO.equals("") && region.equals("NO-SELECT")) {
				sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,'"
						+ settlementDate
						+ "' AS FINALSETTLMENTDT,'"
						+ interestCalUpto
						+ "' as InterestCalupto,'"
						+ dateofSeperationDate
						+ "'as DATEOFSEPERATION_DATE,PENSIONNUMBER,REGION,MARITALSTATUS,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER IS NOT NULL ";
			} else if (empserialNO.equals("") && !region.equals("")) {
				sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,'"
						+ settlementDate
						+ "' AS FINALSETTLMENTDT,'"
						+ interestCalUpto
						+ "' as InterestCalupto,'"
						+ dateofSeperationDate
						+ "'as DATEOFSEPERATION_DATE,PENSIONNUMBER,REGION,MARITALSTATUS,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER IS NOT NULL  and region='"
						+ region + "'";
			} else {
				sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,REGION,'"
						+ settlementDate
						+ "'AS FINALSETTLMENTDT,'"
						+ interestCalUpto
						+ "' as InterestCalupto,'"
						+ dateofSeperationDate
						+ "'as DATEOFSEPERATION_DATE,PENSIONNUMBER,MARITALSTATUS,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER IS NOT NULL  and EMPSERIALNUMBER='"
						+ empserialNO + "'  ";
			}

			/*
			 * if (cpfAccno.equals("")) { region = "NO-SELECT"; }
			 */
			if (!empserialNO.equals("")) {
				region = "NO-SELECT";
			}

			if (!airportCD.equals("NO-SELECT") && !airportCD.equals("")) {
				whereClause.append(" AIRPORTCODE like'%" + airportCD.trim()
						+ "%'");
				whereClause.append(" AND ");
			}
			if (!region.equals("NO-SELECT")) {
				whereClause.append(" LOWER(region)='"
						+ region.trim().toLowerCase() + "'");
				whereClause.append(" AND ");
			}

			query.append(sqlQuery);
			if ((region.equals("NO-SELECT")) && (region.equals("NO-SELECT"))) {
				;
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}

			String orderBy = "ORDER BY EMPSERIALNUMBER";
			query.append(orderBy);
			dynamicQuery = query.toString();
			log.info(" cadDAO  :buildQuery() leaving method "+dynamicQuery);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		return dynamicQuery;
	}
	
	private PensionContBean loadEmployeeInfo(ResultSet rs, String cpfacnos,
			String regions) throws SQLException {
		PensionContBean contr = new PensionContBean();
		if (rs.getString("MARITALSTATUS") != null) {
			contr.setMaritalStatus(rs.getString("MARITALSTATUS").trim());
		} else {
			contr.setMaritalStatus("---");
		}
		if (rs.getString("EMPSERIALNUMBER") != null) {
			contr.setEmpSerialNo(rs.getString("EMPSERIALNUMBER"));
		}
		if (rs.getString("EMPLOYEENO") != null) {
			contr.setEmployeeNO(rs.getString("EMPLOYEENO"));
		} else {
			contr.setEmployeeNO("---");
		}
		if (rs.getString("SEX") != null) {
			contr.setGender(rs.getString("SEX"));
		} else {
			contr.setGender("---");
		}
		if (rs.getString("DESEGNATION") != null) {
			contr.setDesignation(rs.getString("DESEGNATION"));
		} else {
			contr.setDesignation("---");
		}
		if (rs.getString("FHNAME") != null) {
			contr.setFhName(rs.getString("FHNAME"));
		} else {
			contr.setFhName("---");
		}
		if (rs.getString("CPFACNO") != null) {
			contr.setCpfacno(cpfacnos);
		}
		if (rs.getString("EMPLOYEENAME") != null) {
			contr.setEmployeeNM(rs.getString("EMPLOYEENAME"));
		}
		if (rs.getString("REGION") != null) {
			contr.setEmpRegion(regions);
		}
		if (rs.getString("DATEOFJOINING") != null) {
			contr.setEmpDOJ(CommonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			contr.setEmpDOJ("---");
		}
		if (rs.getString("DATEOFBIRTH") != null) {
			contr.setEmpDOB(CommonUtil.converDBToAppFormat(rs
					.getDate("DATEOFBIRTH")));
		} else {
			contr.setEmpDOB("---");
		}
		if (rs.getString("department") != null) {
			contr.setDepartment(rs.getString("department"));
		} else {
			contr.setDepartment("---");
		}
		if (rs.getString("FINALSETTLMENTDT") != null) {
			String finalSettlementdate = rs.getString("FINALSETTLMENTDT");
			contr.setFinalSettlementDate(finalSettlementdate);
		}
		if (rs.getString("interestCalUpto") != null) {
			contr.setInterestCalUpto(rs.getString("interestCalUpto"));
		}
		log.info("department " + contr.getDepartment());
		CommonDAO commonDAO = new CommonDAO();
		String pensionNumber = commonDAO.getPFID(contr.getEmployeeNM(), contr
				.getEmpDOB(), contr.getEmpSerialNo());
		contr.setPensionNo(pensionNumber);
		if (rs.getString("WETHEROPTION") != null) {
			contr.setWhetherOption(rs.getString("WETHEROPTION"));
		} else {
			contr.setWhetherOption("---");
		}
		long noOfYears = 0;
		noOfYears = rs.getLong("ENTITLEDIFF");
		if (noOfYears > 0) {
			contr.setDateOfEntitle(contr.getEmpDOJ());
		} else {
			contr.setDateOfEntitle("01-Apr-1989");
		}
		return contr;
	}

	public String buildPenContRptQuery(String region, String airportCD,
			String empserialNO, String cpfAccno, String transferFlag) {
		log.info(" cadDAO  :buildPenContRptQuery() entering method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO";
		if (!cpfAccno.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND  EPI.region='"
					+ region
					+ "' and EPI.cpfacno='"
					+ cpfAccno
					+ "' AND ETI.TRANSFERFLAG='" + transferFlag + "' ";
		} else if (empserialNO.equals("") && region.equals("NO-SELECT")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND ETI.TRANSFERFLAG='"
					+ transferFlag + "' ";
		} else if (empserialNO.equals("") && !region.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND  EPI.region='"
					+ region + "' AND ETI.TRANSFERFLAG='" + transferFlag + "' ";
		} else {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND EPI.PENSIONNO='"
					+ empserialNO + "'";
			// AND ETI.TRANSFERFLAG='"+transferFlag+"' ";
		}

		/*
		 * if (!cpfAccno.equals("")) { whereClause.append(" LOWER(cpfaccno)
		 * like'" + cpfAccno.trim()+ "'"); whereClause.append(" AND "); } if
		 * (!empserialNO.equals("")) { whereClause.append(" LOWER(epi.pensionno)
		 * like'" + empserialNO.trim()+ "'"); whereClause.append(" AND "); }
		 */
		if (!airportCD.equals("NO-SELECT")) {
			whereClause.append(" LOWER(EPI.AIRPORTCODE) like'%"
					+ airportCD.trim().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}
		if (!region.equals("NO-SELECT")) {

			whereClause.append(" LOWER(EPI.region)='"
					+ region.trim().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("NO-SELECT")) && (airportCD.equals("NO-SELECT"))) {
			;
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY EPI.PENSIONNO";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info(" cadDAO  :buildQuery() leaving method");
		return dynamicQuery;
	}
	private PensionContBean loadEmployeeInfo(ResultSet rs) throws SQLException {
		PensionContBean contr = new PensionContBean();

		/*
		 * if(rs.getString("PENSIONNUMBER")!=null){
		 * contr.setPensionNo(rs.getString("PENSIONNUMBER")); }
		 */
		if (rs.getString("MARITALSTATUS") != null) {
			contr.setMaritalStatus(rs.getString("MARITALSTATUS").trim());
		} else {
			contr.setMaritalStatus("---");

		}
		if (rs.getString("DEPARTMENT") != null) {
			contr.setDepartment(rs.getString("DEPARTMENT").trim());
		} else {
			contr.setDepartment("---");

		}
		if (rs.getString("EMPSERIALNUMBER") != null) {
			contr.setEmpSerialNo(rs.getString("EMPSERIALNUMBER"));
		}
		if (rs.getString("EMPLOYEENO") != null) {
			contr.setEmployeeNO(rs.getString("EMPLOYEENO"));
		} else {
			contr.setEmployeeNO("---");
		}
		if (rs.getString("SEX") != null) {
			contr.setGender(rs.getString("SEX"));
		} else {
			contr.setGender("---");
		}
		if (rs.getString("DESEGNATION") != null) {
			contr.setDesignation(rs.getString("DESEGNATION"));
		} else {
			contr.setDesignation("---");
		}
		if (rs.getString("FHNAME") != null) {
			contr.setFhName(rs.getString("FHNAME"));
		} else {
			contr.setFhName("---");
		}

		if (rs.getString("REGION") != null) {
			contr.setEmpRegion(rs.getString("REGION"));
		}
		if (rs.getString("EMPLOYEENAME") != null) {
			contr.setEmployeeNM(rs.getString("EMPLOYEENAME"));
		}
		if (rs.getString("CPFACNO") != null) {
			contr.setCpfacno(rs.getString("CPFACNO"));
		} else {
			contr.setCpfacno("---");
		}

		if (rs.getString("DATEOFJOINING") != null) {
			contr.setEmpDOJ(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFJOINING")));
		} else {
			contr.setEmpDOJ("---");
		}
		if (rs.getString("DATEOFBIRTH") != null) {
			contr.setEmpDOB(commonUtil.converDBToAppFormat(rs
					.getDate("DATEOFBIRTH")));
		} else {
			contr.setEmpDOB("---");
		}
		CommonDAO commonDAO = new CommonDAO();
		String pensionNumber = commonDAO.getPFID(contr.getEmployeeNM(), contr
				.getEmpDOB(), contr.getEmpSerialNo());
		contr.setPensionNo(pensionNumber);
		if (rs.getString("WETHEROPTION") != null) {
			contr.setWhetherOption(rs.getString("WETHEROPTION"));
		} else {
			contr.setWhetherOption("---");
		}
		long noOfYears = 0;
		noOfYears = rs.getLong("ENTITLEDIFF");

		if (noOfYears > 0) {
			contr.setDateOfEntitle(contr.getEmpDOJ());
		} else {
			contr.setDateOfEntitle("01-Apr-1989");
		}
		return contr;

	}
	
	public String buildPCReportQuery(String region, String airportCD,
			String empserialNO, String cpfAccno, String transferFlag,
			int startPensionNo, int endPensionno, String pfdString) {
		log.info(" cadDAO  :buildPCReportQuery() entering method");
		log.info("endPensionno" + empserialNO);
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		log.info(" transferFlag " + transferFlag);
		if (!transferFlag.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DEPARTMENT AS DEPARTMENT,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO ";
		} else {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DEPARTMENT AS DEPARTMENT,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL  ";
		}
		log.info(sqlQuery);
		if (!airportCD.equals("NO-SELECT")) {
			whereClause.append(" LOWER(EPI.AIRPORTCODE) ='"
					+ airportCD.trim().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!empserialNO.equals("")) {
			whereClause.append(" EPI.PENSIONNO ='" + empserialNO + "'");
			whereClause.append(" AND ");
		}
		if (!pfdString.equals("") && startPensionNo != 0) {
			whereClause.append(" EPI.PENSIONNO BETWEEN'" + startPensionNo
					+ "' AND '" + endPensionno + "'");
			whereClause.append(" AND ");
		}

		if (!cpfAccno.equals("")) {
			whereClause.append(" EPI.cpfAccno ='" + cpfAccno + "'");
			whereClause.append(" AND ");
		}
		if (!region.equals("NO-SELECT") && !region.equals("AllRegions")) {
			whereClause.append(" LOWER(EPI.region)='"
					+ region.trim().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!transferFlag.equals("")) {
			whereClause.append(" ETI.TRANSFERFLAG='" + transferFlag + "'");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if (region.equals("NO-SELECT") && (airportCD.equals("NO-SELECT"))
				&& (pfdString.equals("")) && (empserialNO.equals(""))
				&& (transferFlag.equals("")) && region.equals("AllRegions")) {
			;
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY EPI.PENSIONNO";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log.info(" cadDAO  :buildPCReportQuery() leaving method");
		return dynamicQuery;
	}
	
	public String buildPenContRptWthOutTransferQuery(String region,
			String airportCD, String empserialNO, String cpfAccno) {
		log
				.info(" cadDAO  :buildPenContRptWthOutTransferQuery() entering method");

		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		sqlQuery = 
			"SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1989'),'01-Apr-1989'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI WHERE EPI.PENSIONNO IS NOT NULL ";

		if (!airportCD.equals("NO-SELECT")) {
			whereClause.append(" LOWER(EPI.AIRPORTCODE) like'%"
					+ airportCD.trim().toLowerCase() + "%'");
			whereClause.append(" AND ");
		}

		if (!region.equals("NO-SELECT")) {
			whereClause.append(" LOWER(EPI.region)='"
					+ region.trim().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!empserialNO.equals("")) {

			whereClause.append(" EPI.PENSIONNO=" + empserialNO.trim() + "");
			whereClause.append(" AND ");
		}
		query.append(sqlQuery);
		if ((region.equals("NO-SELECT")) && (airportCD.equals("NO-SELECT"))
				&& (empserialNO.equals(""))) {
			;
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}

		String orderBy = "ORDER BY EPI.PENSIONNO";
		query.append(orderBy);
		dynamicQuery = query.toString();
		log
				.info(" cadDAO  :buildPenContRptWthOutTransferQuery() leaving method");
		return dynamicQuery;
	}
	
	public void editTransactionData(String cpfAccno, String monthyear,
			String emoluments, String empvpf, String principle,
			String interest, String contri, String advance, String loan,
			String aailoan, String pfid, String region, String airportcode,
			String username, String computername, String from7narration,
			String pcheldamt, String noofmonths, String arrearflag,
			String duputationflag, String pensionoption, String recoverieTable,
			String epf,String benfundcontr,String benfundPension,String gratuityInterest,String epfoPension,String addContri) {
		
		recoverieTable = "true";
		log.info(" CadReportDAO editTransactionData "+airportcode+" recoverieTable "+recoverieTable+" epf "+epf+" contri "+contri);
				
		String emppfstatuary = "0.00", oldemppfstatuary = "0.00", pf = "0.00";
		String tableName = "",logtable="",loghistorytable="";
		
		
		if (recoverieTable.trim().equals("true")) {
			tableName = "CAD_VALIDATE";
			logtable="CAD_EML_LOG_FINAL_RECOVERY";
			loghistorytable="CAD_EMO_LOG_HIS_FINALREC";
		}	
		
		double pensionCOntr = 0.0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;		
		
		String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "";
		
		try {			
			con = commonDB.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			transMonthYear = commonUtil.converDBToAppFormat(monthyear.trim(),
					"dd-MMM-yy", "-MMM-yy");
			
			log.info(" transdate &&&&&& "+transdate+" epf "+epf);

			if (cpfAccno.indexOf(",") != -1) {
				cpfAccno = cpfAccno.substring(0, cpfAccno.indexOf(","));
			}
			log.info(" After transdate @@@ "+transdate.after(new Date("31-Mar-89")));
			log.info(" Before transdate @@@ "+transdate.before(new Date("31-Mar-95")));
			if (transdate.after(new Date("31-Mar-89"))
					&& transdate.before(new Date("31-Mar-95"))) {
				log.info(" epf !!!!!! "+epf);
				emppfstatuary = String.valueOf(Float.parseFloat(epf));
			} else	if (transdate.after(new Date("31-Mar-98"))
					&& transdate.before(new Date("31-Mar-08"))
					&& duputationflag != "Y") {
				emppfstatuary = String
						.valueOf(Float.parseFloat(emoluments) * 12 / 100);
			} else if (transdate.before(new Date("31-Mar-98"))) {
				emppfstatuary = String
						.valueOf(Float.parseFloat(emoluments) * 10 / 100);
			}  else {
				if (epf.equals("0")) {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 12 / 100);
				} else {
					emppfstatuary = epf;
				}
			}
			
			if (transdate.after(new Date("31-Mar-89"))
					&& transdate.before(new Date("31-Mar-95"))) {
				log.info(" contri !!!!!! "+contri);
				contri = String.valueOf(Float.parseFloat(contri));
			}else if 		
			 (emppfstatuary != "" && emppfstatuary != "0.00") {
				pf = String.valueOf(Float.parseFloat(emppfstatuary)
						- Float.parseFloat(contri));
			}
			
				String	checkmonth = this.chkPfidinAdjCrtn(pfid,monthyear);
				String wherecondition = "";
				log.info(" checkmonth "+checkmonth);
				String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
			if (checkmonth.equals("NotExists")) {				
				
					sqlQuery = " insert into " + tableName + " (pensionno,cpfaccno,monthyear,emoluments,emppfstatuary,empvpf,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,pf,empflag,edittrans,"
								+ "arrearflag,FORM7NARRATION,pcheldamt,emolumentmonths,editeddate,ben_fund_contribution,ben_fund_pension,gratuity_interest_thereon,epfo_pension,additionalcontri) values('"
								+pfid+"','"+cpfAccno+"','"+monthyear+"','"+emoluments+"','"+emppfstatuary+"','"+empvpf
								+"','"+principle +"','"+interest+"','"+contri
								+"','"+pf+"','Y','Y','"+arrearflag.trim()
								+"','"+from7narration+"','"+pcheldamt.trim()+"','"+noofmonths+"','"+updatedDate+ "','"+benfundcontr+ "','"+benfundPension+ "','"+gratuityInterest+ "','"+epfoPension+ "','"+addContri+ "')";					
				 }else{
				sqlQuery = "update " + tableName + " set emoluments='"
						+ emoluments + "',emppfstatuary='" + emppfstatuary
						+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
						+ principle + "',EMPADVRECINTEREST='" + interest
						+ "',PENSIONCONTRI='" + contri + "',pf='" + pf
						+ "', empflag='Y',edittrans='Y',arrearflag='"
						+ arrearflag.trim() + "',FORM7NARRATION='"
						+ from7narration + "',pcheldamt='" + pcheldamt.trim()
						+ "',emolumentmonths='" + noofmonths + "',editeddate='"
						+ updatedDate + "',ben_fund_contribution='"
						+ benfundcontr + "',ben_fund_pension='"
						+ benfundPension + "',gratuity_interest_thereon='"
						+ gratuityInterest + "',epfo_pension='"
						+ epfoPension + "',additionalcontri='"
						+ addContri + "' where pensionno ="+pfid+" and  to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear + "'  AND empflag='Y' ";						
				}
			
			log.info(" sql @@@@@@@@@@@@@@@@@@@@@@@ "+sqlQuery);
			
			FinacialDataBean bean = new FinacialDataBean();
			String checkArrearTable = this.checkArrears(con, monthyear,
					cpfAccno, "", region, pfid);
			bean = this.getEmolumentsBean(con, monthyear, cpfAccno, "", region,
					pfid, recoverieTable);		
			if (duputationflag.equals("Y")) {
				emppfstatuary = bean.getEmpPfStatuary();
			} else if (transdate.after(new Date("31-Mar-2008"))
					&& bean.getEmpPfStatuary() != "") {
				pf = String.valueOf(Float.parseFloat(epf)
						- Float.parseFloat(contri));
			}

			oldemppfstatuary = bean.getEmpPfStatuary();
			if (bean.getEmoluments() != "" && bean.getEmoluments() != "0.00") {
				transMonthYear = commonUtil.converDBToAppFormat(monthyear
						.trim(), "dd-MMM-yy", "-MMM-yy");
				
				if ((pfid == "" || transdate.before(new Date("31-Mar-2008")))
						&& !recoverieTable.trim().equals("true")) {
					wherecondition += "cpfaccno='" + cpfAccno
							+ "' and region='" + region + "'";
				} else {
					wherecondition += "pensionno='" + pfid + "'";
				}
				
			
				if (checkArrearTable.trim().equals("")
						&& arrearflag.equals("Y")) {
					arrearQuery = "insert into CAD_EMPLOYEE_PENSION_ARREAR(PENSIONNO,ARREARAMT,ARREARDATE,REGION,CPFACCNO,airportcode) values('"
							+ pfid
							+ "','"
							+ emoluments
							+ "','"
							+ monthyear
							+ "','"
							+ region
							+ "','"
							+ cpfAccno
							+ "','"
							+ airportcode + "')";
					st.executeUpdate(arrearQuery);
				}

			} else {
				if (airportcode.trim().equals("-NA-")) {
					airportcode = "";
				}
				if (transdate.before(new Date("31-Mar-2008"))) {
					pensionCOntr = commonDAO.pensionCalculation(monthyear,
							emoluments, pensionoption, region, "1");
					pf = String.valueOf(Double.parseDouble(emppfstatuary)
							- pensionCOntr);
				} else {
					String wetheroption = "", retirementDate = "", dateofbirth = "";
					String days = "0";
					double calculatedPension = 0.00;
					String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('"
							+ monthyear
							+ "','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days from employee_personal_info where to_char(pensionno)='"
							+ pfid + "'";
					log.info(" checkPFID "+checkPFID);
					rs = st.executeQuery(checkPFID);
					while (rs.next()) {
						if (rs.getString("wetheroption") != null) {
							wetheroption = rs.getString("wetheroption");
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
					}

					calculatedPension = commonDAO.calclatedPF(monthyear,
							retirementDate, dateofbirth, emoluments,
							wetheroption, "", days, "1");

					pensionCOntr = Math.round(calculatedPension);
					pf = String.valueOf(Double.parseDouble(emppfstatuary)
							- pensionCOntr);

				}
				
				
				if (checkArrearTable.trim().equals("")
						&& arrearflag.equals("Y")) {
					arrearQuery = "insert into CAD_EMPLOYEE_PENSION_ARREAR(PENSIONNO,ARREARAMT,ARREARDATE,REGION,CPFACCNO,airportcode) values('"
							+ pfid
							+ "','"
							+ emoluments
							+ "','"
							+ monthyear
							+ "','"
							+ region
							+ "','"
							+ cpfAccno
							+ "','"
							+ airportcode + "')";
					st.executeUpdate(arrearQuery);
					log.info(" CAD_EMPLOYEE_PENSION_ARREAR  "+arrearQuery);
				}

			}
			if (advance != "" && advance != "0.00" && !advance.equals("0")) {
				String checkAdvtbl = "select count(*) as count from CAD_EMPLOYEE_PENSION_ADVANCES where pensionno='"
						+ pfid
						+ "' and  to_char(ADVTRANSDATE,'dd-Mon-yy') like '%"
						+ transMonthYear + "'";
				rs = st.executeQuery(checkAdvtbl);
				log.info(" CAD_EMPLOYEE_PENSION_ADVANCES  "+checkAdvtbl);
				String updateAdvtbl = "";
				while (rs.next()) {
					int count = rs.getInt(1);
					if (count != 0) {
						updateAdvtbl = "update CAD_EMPLOYEE_PENSION_ADVANCES set AMOUNT='"
								+ advance
								+ "' where pensionno='"
								+ pfid
								+ "' and  to_char(ADVTRANSDATE,'dd-Mon-yy') like '%"
								+ transMonthYear + "'";
						st.executeUpdate(updateAdvtbl);
						log.info(" CAD_EMPLOYEE_PENSION_ADVANCES  "+updateAdvtbl);
					} else {
						updateAdvtbl = "insert into CAD_EMPLOYEE_PENSION_ADVANCES(AMOUNT,pensionno,ADVTRANSDATE) values('"
								+ advance
								+ "','"
								+ pfid
								+ "','"
								+ monthyear
								+ "')";
						st.executeUpdate(updateAdvtbl);
					}
					log.info(" insert CAD_EMPLOYEE_PENSION_ADVANCES "+updateAdvtbl);
				}

			}
			
			
			
			if (loan != "" && loan != "0.00" && !loan.equals("0")) {
				String checkLoantbl = "select count(*) as count from CAD_PENSION_LOANS where pensionno='"
						+ pfid
						+ "' and  to_char(loandate,'dd-Mon-yy') like '%"
						+ transMonthYear + "'";
				rs = st.executeQuery(checkLoantbl);
				String updateloantbl = "";
				while (rs.next()) {
					int count = rs.getInt(1);
					if (count != 0) {
						updateloantbl = "update CAD_PENSION_LOANS set SUB_AMT='"
								+ loan
								+ "',CONT_AMT='"
								+ aailoan
								+ "',LOANTYPE='NRF' where pensionno='"
								+ pfid
								+ "' and  to_char(LOANDATE,'dd-Mon-yy') like '%"
								+ transMonthYear + "'";
						st.executeUpdate(updateloantbl);
						log.info(" CAD_PENSION_LOANS  " + updateloantbl);
					} else {
						updateloantbl = "insert into CAD_PENSION_LOANS(SUB_AMT,CONT_AMT,pensionno,LOANDATE,LOANTYPE) values('"
								+ loan
								+ "','"
								+ aailoan
								+ "','"
								+ pfid
								+ "','"
								+ monthyear + "','NRF')";
						st.executeUpdate(updateloantbl);
						log.info(" insert CAD_PENSION_LOANS  " + updateloantbl);
					}
					

				}
			}
			String selectEmolumentsLog = "select count(*) as count from "+logtable+" where cpfacno='"
					+ cpfAccno
					+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear + "' and region='" + region + "' ";
			log.info(" logtable " + selectEmolumentsLog);
			rs = st.executeQuery(selectEmolumentsLog);
			
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					emoluments_log = "insert into "+logtable+"(oldemoluments,newemoluments,oldemppfstatuary,newemppfstatuary,oldprinciple,newprinciple,oldinterest,newinterest,oldempvpf,newempvpf,OLDPENSIONCONTRI,NEWENSIONCONTRI,monthyear,UPDATEDDATE,pensionno,cpfacno,region,username,computername)values('"
							+ bean.getEmoluments()
							+ "','"
							+ emoluments
							+ "','"
							+ oldemppfstatuary
							+ "','"
							+ emppfstatuary
							+ "','"
							+ bean.getPrincipal()
							+ "','"
							+ principle
							+ "','"
							+ bean.getInterest()
							+ "','"
							+ interest
							+ "','"
							+ bean.getEmpVpf()
							+ "','"
							+ empvpf
							+ "','"
							+ bean.getPenContri()
							+ "','"
							+ contri
							+ "','"
							+ monthyear
							+ "','"
							+ updatedDate
							+ "','"
							+ pfid
							+ "','"
							+ cpfAccno
							+ "','"
							+ region
							+ "','"
							+ username + "','" + computername + "')";
				} else {
					emoluments_log = "update "+logtable+" set oldemoluments='"
							+ bean.getEmoluments()
							+ "',newemoluments='"
							+ emoluments
							+ "',oldemppfstatuary='"
							+ oldemppfstatuary
							+ "',newemppfstatuary='"
							+ emppfstatuary
							+ "',oldprinciple='"
							+ bean.getPrincipal()
							+ "',newprinciple='"
							+ principle
							+ "',oldinterest='"
							+ bean.getInterest()
							+ "',newinterest='"
							+ interest
							+ "',oldempvpf='"
							+ bean.getEmpVpf()
							+ "',newempvpf='"
							+ empvpf
							+ "',OLDPENSIONCONTRI='"
							+ bean.getPenContri()
							+ "',NEWENSIONCONTRI='"
							+ contri
							+ "',monthyear='"
							+ monthyear
							+ "',UPDATEDDATE='"
							+ updatedDate
							+ "',pensionno='"
							+ pfid
							+ "',region='"
							+ region
							+ "',username='"
							+ username
							+ "',computername='"
							+ computername
							+ "' where cpfacno='"
							+ cpfAccno
							+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
							+ transMonthYear + "' and region='" + region + "'";
					log.info(" logtable 22 "+emoluments_log);
				}
				emoluments_log_history = "insert into "+loghistorytable+"(oldemoluments,newemoluments,oldemppfstatuary,newemppfstatuary,monthyear,UPDATEDDATE,pensionno,cpfacno,region,username,computername)values('"
						+ bean.getEmoluments()
						+ "','"
						+ emoluments
						+ "','"
						+ oldemppfstatuary
						+ "','"
						+ emppfstatuary
						+ "','"
						+ monthyear
						+ "','"
						+ updatedDate
						+ "','"
						+ pfid
						+ "','"
						+ cpfAccno
						+ "','"
						+ region
						+ "','"
						+ username
						+ "','" + computername + "')";

				log.info(" loghistorytable  "+emoluments_log_history);
			}
			String queryforProcess = "insert into CAD_GENRTED_PEND_ADJ_0B (PENSIONNO,cpfaccno,EMPLOYEENAME,AIRPORTCODE,region,REQGENDATE,STATUS,dateofbirth,wetheroption,dateofjoining,remarks,ADJSTATUS,PFCRDSTATUS) (select pensionno,cpfacno,employeename,'"
					+ airportcode
					+ "','"
					+ region
					+ "',TO_CHAR(CURRENT_DATE, 'DD-MON-YYYY HH:MI:SS'),'N' ,dateofbirth,wetheroption,dateofjoining,'"
					+ username
					+ "','N','N' from employee_personal_info where pensionno='"
					+ pfid + "')";
			
			
			st.executeUpdate(emoluments_log);
			st.executeUpdate(emoluments_log_history);
			st.executeUpdate(sqlQuery);
			st.executeUpdate(queryforProcess);
			log.info(" insert "+sqlQuery);
			log.info(" queryforProcess "+queryforProcess);
		

			String weatherOption = "", retirementDate = "", dateofbirth = "", days = "";
			if (!pfid.equals("")) {
				String checkPFID = "select wetheroption,pensionno, to_char(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth from employee_personal_info where to_char(pensionno)='"
						+ pfid + "'";
				log.info(checkPFID);
				rs = st.executeQuery(checkPFID);

				if (rs.next()) {
					if (rs.getString("wetheroption") != null) {
						weatherOption = rs.getString("wetheroption").toString();
					} else {
						weatherOption = "";
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
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
	}
	
	
	public String checkArrears(Connection con, String fromDate,
			String cpfaccno, String employeeno, String region, String Pensionno) {	

		String foundEmpFlag = "", arrearamt = "";
		Statement st = null;
		ResultSet rs = null;
		try {
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			Date transdate = df.parse(fromDate);
			String transMonthYear = commonUtil.converDBToAppFormat(fromDate
					.trim(), "dd-MMM-yyyy", "-MMM-yy");
			String query = "";
			if (Pensionno == "" || transdate.before(new Date("31-Mar-2008"))) {
				query = "select ARREARAMT from CAD_EMPLOYEE_PENSION_ARREAR where to_char(ARREARDATE,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and cpfaccno='"
						+ cpfaccno
						+ "' and region='" + region + "' ";
			} else {
				query = "select ARREARAMT from CAD_EMPLOYEE_PENSION_ARREAR where to_char(ARREARDATE,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and pensionno='"
						+ Pensionno
						+ "'  ";
			}

			st = con.createStatement();
			rs = st.executeQuery(query);
				log.info(" checkArrears  "+query);
			if (rs.next()) {
				if (rs.getString("ARREARAMT") != null) {
					arrearamt = rs.getString("ARREARAMT");
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
		// log.info("PensionDAO :checkPensionDuplicate() leaving method");
		return arrearamt;
	}
	
	public int preProcessAdjOB(String pfid) {
		Connection con = null;
		Statement pst = null;
		ResultSet rs = null;
		String status = "";
		int updatedRecords = 0;
		String sqlInsertQuery = "", prCPFString = "", sqlUpdateQuery = "";
		sqlInsertQuery = "insert into CAD_GENRTED_PEND_ADJ_0B(PENSIONNO,cpfaccno,EMPLOYEENAME,AIRPORTCODE,region,REQGENDATE,STATUS,dateofbirth,wetheroption,dateofjoining) select pensionno,cpfacno,employeename,airportcode,region,TO_CHAR(CURRENT_DATE, 'DD-MON-YYYY HH:MI:SS'),'N' ,dateofbirth,wetheroption,dateofjoining from employee_personal_info where pensionno="
				+ pfid;
		sqlUpdateQuery = "UPDATE CAD_GENRTED_PEND_ADJ_0B SET STATUS='N' , REQGENDATE=TO_CHAR(CURRENT_DATE, 'DD-MON-YYYY HH:MI:SS') WHERE PENSIONNO="
				+ pfid;
		PersonalDAO personalDAO = new PersonalDAO();
		PensionContBean data = null;
		CommonDAO commonDao = new CommonDAO();

		ArrayList empinfo = new ArrayList();
		try {
			con = commonDB.getConnection();
			pst = con.createStatement();
			if (!pfid.equals("")) {
				status = this.checkPreProcessAdjOB(con, pfid);
				if (status.equals("insert")) {
					updatedRecords = pst.executeUpdate(sqlInsertQuery);
				} else if (status.equals("update")) {
					updatedRecords = pst.executeUpdate(sqlUpdateQuery);
				}
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, pst, rs);
		}
		return updatedRecords;
	}
	
	private String checkPreProcessAdjOB(Connection con, String pfid) {
		Statement pst = null;
		ResultSet rs = null;
		String pensionNo = "", requestGenDate = "", status = "", returnType = "";
		long noOfDays = 0;
		String sqlQuery = "SELECT PENSIONNO,REQGENDATE,STATUS FROM CAD_GENRTED_PEND_ADJ_0B WHERE PENSIONNO="
				+ pfid;
		// log.info("FinancialReportDAO::checkPreProcessAdjOB" + sqlQuery);
		try {
			con = commonDB.getConnection();
			pst = con.createStatement();
			rs = pst.executeQuery(sqlQuery);
			if (rs.next()) {
				if (rs.getString("PENSIONNO") != null) {
					pensionNo = rs.getString("PENSIONNO");
				}
				if (rs.getString("REQGENDATE") != null) {
					requestGenDate = commonUtil.converDBToAppFormat(rs
							.getDate("REQGENDATE"));
				}
				if (rs.getString("STATUS") != null) {
					status = rs.getString("STATUS");
				}
				noOfDays = commonUtil.getDateDifference(commonUtil
						.getCurrentDate("dd-MMM-yyyy"), requestGenDate);
				if (noOfDays > 1) {
					returnType = "update";
				} else {
					returnType = "none";
				}
			} else {
				returnType = "insert";
			}

			// log.info("FinancialReportDAO::checkPreProcessAdjOB noOfDays"+
			// noOfDays + "returnType=====" + returnType);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, pst, rs);
		}
		return returnType;
	}
	
	public void editFinalDate(String finalsettlementDate, String pfid,
			String userId) {
		log.info("pfid" + pfid + "finalsettlementDate" + finalsettlementDate);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String transMonthYear = "", sqlQuery = "", sqlQuery1 = "";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String fromFormat = "";
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			// Date transdate = df.parse(finalsettlementDate);
			if (finalsettlementDate.indexOf("/") != -1
					&& finalsettlementDate.length() == 10) {
				fromFormat = "dd/MM/yyyy";
			} else if (finalsettlementDate.indexOf("/") != -1
					&& finalsettlementDate.length() == 11) {
				fromFormat = "dd/MMM/yyyy";
			} else if (finalsettlementDate.indexOf("-") != -1
					&& finalsettlementDate.length() == 11) {
				fromFormat = "dd-MMM-yyyy";
			} else if (finalsettlementDate.indexOf("-") != -1
					&& finalsettlementDate.length() == 10) {
				fromFormat = "dd-MM-yyyy";
			} else if (finalsettlementDate.indexOf(".") != -1
					&& finalsettlementDate.length() == 10) {
				fromFormat = "dd.MM.yyyy";
			} else if (finalsettlementDate.indexOf(".") != -1
					&& finalsettlementDate.length() == 11) {
				fromFormat = "dd.MMM.yyyy";
			}
			transMonthYear = commonUtil.converDBToAppFormat(finalsettlementDate
					.trim(), fromFormat, "dd-MMM-yyyy");
			log.info("transMonthYear" + transMonthYear);
			PensionContBean bean = new PensionContBean();
			sqlQuery = "update employee_info set SETDATEOFANNUATION='"
					+ transMonthYear + "' where empserialnumber='" + pfid + "'";
			sqlQuery1 = "update employee_personal_info set DATEOFSEPERATION_DATE='"
					+ transMonthYear
					+ "',LASTACTIVE='"
					+ updatedDate
					+ "',USERNAME='"
					+ userId
					+ "' where pensionno='"
					+ pfid
					+ "'";
			log.info("sqlQuery" + sqlQuery);
			log.info("sqlQuery" + sqlQuery1);
			st.executeUpdate(sqlQuery);
			st.executeUpdate(sqlQuery1);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}

	}
	
	public void deleteTransactionData(String cpfAccno, String monthyear,
			String region, String airportcode, String Computername,
			String Username, String pfid) {
		Connection con = null;
		Statement st = null;
		String sqlQuery = "", condition = "";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			if (!airportcode.equals("") && !airportcode.equals("-NA-")) {
				if (transdate.before(new Date("31-Mar-08"))) {
					condition = " cpfaccno='" + cpfAccno + "'";
				} else {
					condition = " Pensionno='" + pfid + "'";
				}
				sqlQuery = "update CAD_VALIDATE set empflag='N',username='"
						+ Username
						+ "',ipaddress='"
						+ Computername
						+ "',UPDATEDDATE='"
						+ updatedDate
						+ "' where "
						+ condition
						+ " and to_char(monthyear,'dd-mon-yyyy')='"
						+ monthyear.toLowerCase()
						+ "' and region='"
						+ region
						+ "' and airportcode='" + airportcode + "'  ";
			} else {

				if (transdate.before(new Date("31-Mar-08"))) {
					condition = " cpfaccno='" + cpfAccno + "'";
				} else {
					condition = " Pensionno='" + pfid + "'";
				}
				sqlQuery = "update CAD_VALIDATE set empflag='N',username='"
						+ Username
						+ "',ipaddress='"
						+ Computername
						+ "',UPDATEDDATE='"
						+ updatedDate
						+ "' where "
						+ condition
						+ " and to_char(monthyear,'dd-mon-yyyy')='"
						+ monthyear.toLowerCase()
						+ "' and region='"
						+ region
						+ "' and (airportcode is null or airportcode='-NA-') ";

			}
			log.info(" " + sqlQuery);
			st.executeQuery(sqlQuery);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {

		}
	}
	
	public void editReInterestCalc(String interestcalcDate, String pfid,
			String userId, String recoverieTable) {
		log.info("pfid" + pfid + "editReInterestCalc" + interestcalcDate);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String transMonthYear = "", sqlQuery = "", sqlQuery1 = "", screenname = "";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String fromFormat = "";
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			// Date transdate = df.parse(finalsettlementDate);
			if (interestcalcDate.indexOf("/") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd/MM/yyyy";
			} else if (interestcalcDate.indexOf("/") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd/MMM/yyyy";
			} else if (interestcalcDate.indexOf("-") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd-MMM-yyyy";
			} else if (interestcalcDate.indexOf("-") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd-MM-yyyy";
			} else if (interestcalcDate.indexOf(".") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd.MM.yyyy";
			} else if (interestcalcDate.indexOf(".") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd.MMM.yyyy";
			}
			transMonthYear = commonUtil.converDBToAppFormat(interestcalcDate
					.trim(), fromFormat, "dd-MMM-yyyy");
			String condition = "";
			String sqlQueryfinalsettle = "";
			if (recoverieTable.equals("true")) {
				condition = "set REINTERESTDATE='" + transMonthYear + "',";
				screenname = "Final_settlement_verified";
			} else {
				condition = "set RESETTLEMENTDATE='" + transMonthYear + "',";
				screenname = "Edit SCREEN";
			}
			log.info("transMonthYear" + transMonthYear);
			PensionContBean bean = new PensionContBean();
			sqlQuery1 = "update employee_personal_info  " + condition
					+ " LASTACTIVE='" + updatedDate + "',USERNAME='" + userId
					+ "' where pensionno='" + pfid + "'";
			log.info("sqlQuery" + sqlQuery1);
			st.executeUpdate(sqlQuery1);
			if (recoverieTable.equals("true")) {
				sqlQueryfinalsettle = " insert into employee_finalsettledate_info(pensionno,REINTRCALCDATE,LASTACTIVE,USERNAME,screenname) values('"
						+ pfid
						+ "','"
						+ transMonthYear
						+ "','"
						+ updatedDate
						+ "','" + userId + "','" + screenname + "')";
			} else {
				sqlQueryfinalsettle = " insert into employee_finalsettledate_info(pensionno,RESETTLEMENTDATE,LASTACTIVE,USERNAME,screenname) values('"
						+ pfid
						+ "','"
						+ transMonthYear
						+ "','"
						+ updatedDate
						+ "','" + userId + "','" + screenname + "')";
			}

			st.executeUpdate(sqlQueryfinalsettle);
			log.info("sqlQueryfinalsettle" + sqlQueryfinalsettle);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}

	}
	
	public FinacialDataBean getEmolumentsBean(Connection con, String fromDate,
			String cpfaccno, String employeeno, String region, String Pensionno,String recoverieTable) {

		String foundEmpFlag = "";
		Statement st = null;
		ResultSet rs = null;
		FinacialDataBean bean = new FinacialDataBean();
		String tableName = "CAD_VALIDATE";
		if (recoverieTable.trim().equals("true")) {
			tableName = "CAD_VALIDATE";
		}
		try {
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

			Date transdate = df.parse(fromDate);

			String transMonthYear = commonUtil.converDBToAppFormat(fromDate
					.trim(), "dd-MMM-yyyy", "-MMM-yy");
			String query = "";
			if ((Pensionno == "" || transdate.before(new Date("31-Mar-2008"))) && !recoverieTable.trim().equals("true")) {				
				query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI from " + tableName + " where to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and cpfaccno='"
						+ cpfaccno
						+ "' and region='" + region + "' and empflag='Y' ";
				log.info(" query if "+query);
			} else {
				query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI from " + tableName + " where to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and pensionno='"
						+ Pensionno
						+ "'  and empflag='Y' ";
				log.info(" query else "+query);
			}
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
		// log.info("PensionDAO :checkPensionDuplicate() leaving method");
		return bean;
	}

	
	public void editInterestCalcDate(String interestcalcDate, String pfid,
			String userId, String recoverieTable) {
		log.info("pfid" + pfid + "editInterestCalcDate" + interestcalcDate);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String transMonthYear = "", sqlQuery = "", sqlQuery1 = "", screenname = "";
		String sqlQueryfinalsettle = "";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String fromFormat = "";
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			// Date transdate = df.parse(finalsettlementDate);
			if (interestcalcDate.indexOf("/") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd/MM/yyyy";
			} else if (interestcalcDate.indexOf("/") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd/MMM/yyyy";
			} else if (interestcalcDate.indexOf("-") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd-MMM-yyyy";
			} else if (interestcalcDate.indexOf("-") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd-MM-yyyy";
			} else if (interestcalcDate.indexOf(".") != -1
					&& interestcalcDate.length() == 10) {
				fromFormat = "dd.MM.yyyy";
			} else if (interestcalcDate.indexOf(".") != -1
					&& interestcalcDate.length() == 11) {
				fromFormat = "dd.MMM.yyyy";
			}
			transMonthYear = commonUtil.converDBToAppFormat(interestcalcDate
					.trim(), fromFormat, "dd-MMM-yyyy");
			String condition = "";
			if (recoverieTable.equals("true")) {
				condition = "set interestcalcDate='" + transMonthYear + "',";
				screenname = "Final_settlement_verified";
			} else {
				condition = "set FINALSETTLMENTDT='" + transMonthYear + "',";
				screenname = "Edit SCREEN";
			}
			log.info("transMonthYear" + transMonthYear);
			PensionContBean bean = new PensionContBean();
			sqlQuery1 = "update employee_personal_info  " + condition
					+ " LASTACTIVE='" + updatedDate + "',USERNAME='" + userId
					+ "' where pensionno='" + pfid + "'";
			log.info("sqlQuery" + sqlQuery1);
			st.executeUpdate(sqlQuery1);
			sqlQuery = "update employee_info  set INTERESTCALCDATE='"
					+ transMonthYear + "',LASTACTIVE='" + updatedDate
					+ "',USERNAME='" + userId + "' where empserialnumber='"
					+ pfid + "'";
			st.executeUpdate(sqlQuery);
			log.info("sqlQuery" + sqlQuery);
			if (recoverieTable.equals("true")) {
				sqlQueryfinalsettle = " insert into employee_finalsettledate_info(pensionno,interestcalcDate,LASTACTIVE,USERNAME,screenname) values('"
						+ pfid
						+ "','"
						+ transMonthYear
						+ "','"
						+ updatedDate
						+ "','" + userId + "','" + screenname + "')";
			} else {
				sqlQueryfinalsettle = " insert into employee_finalsettledate_info(pensionno,FINALSETTLMENTDT,LASTACTIVE,USERNAME,screenname) values('"
						+ pfid
						+ "','"
						+ transMonthYear
						+ "','"
						+ updatedDate
						+ "','" + userId + "','" + screenname + "')";
			}
			st.executeUpdate(sqlQueryfinalsettle);
			log.info("sqlQueryfinalsettle" + sqlQueryfinalsettle);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
	}
	public void insertCadAllYear(String pfid,String year,String emoluments,String pfStaturary,String AdditionalContri,String empVpf,String cpfInterest,String pensionInterest,String totalPension){
				
		log.info("inside insertCadAllYear DAO pfid "+pfid+" year "+year+" emoluments"+emoluments);
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;				
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		String sqlQuery = "insert into CAD_REPORT_ALLYEARS(pensionno,year,emolument,cpf_contribution,addi_contri,net_epf,cpf_intrest,epfo_intrest,epfo_pension) values('"+pfid+"','"+year+"','"+emoluments+"','"+pfStaturary+"','"+AdditionalContri+"','"+empVpf+"','"+cpfInterest+"','"+pensionInterest+"','"+totalPension+"')";			
		log.info("--CAD_REPORT_ALLYEARS () ---" + sqlQuery);		
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			log.info("--CAD_REPORT_ALLYEARS ()---" + sqlQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		
	}
	
	
	public void cadtracking(String pfid,String status,String approvedby){
		
		log.info("inside cad_tracking DAO pfid "+pfid+" status "+status+" approvedby"+approvedby);
ResultSet rs = null;
Statement st = null;
Connection con = null;				
String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
String sqlQuery = "insert into cad_tracking(pensionno,status,approvedby) values('"+pfid+"','"+status+"','"+approvedby+"')";			
log.info("--cad_tracking ()---" + sqlQuery);		
try {
	con = DBUtils.getConnection();
	st = con.createStatement();
	rs = st.executeQuery(sqlQuery);
	log.info("--cad_tracking ()---" + sqlQuery);
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} finally {
	commonDB.closeConnection(con, st, rs);
}


}
	
	
	public void updateCadTracking(String pfid,String status){		
		log.info(" updateCadTrackingDAO pfid "+pfid+" status "+status);
ResultSet rs = null;
Statement st = null;
Connection con = null;				
String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
String sqlQuery = "update cad_tracking set status ='"+status+"' where pensionno = "+pfid;			
log.info("--updateCadTracking ()---" + sqlQuery);		
try {
	con = DBUtils.getConnection();
	st = con.createStatement();
	st.executeUpdate(sqlQuery);
	log.info("--updateCadTracking ()---" + sqlQuery);
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} finally {
	commonDB.closeConnection(con, st, rs);
}


}
	
	public String getCadTrackingStatus(String pfid){		
		
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;	
	String status = "";
	String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
	String sqlQuery = "select status from  cad_tracking  where pensionno = "+pfid;			
		
	try {
	con = DBUtils.getConnection();
	st = con.createStatement();
	log.info(" @@@ if "+status);
	rs =  st.executeQuery(sqlQuery);
	
	if(rs.next()){
		log.info(" @@@ 1st "+status);
		status = rs.getString(1);
		log.info(" @@@ if "+status);
	}
	
	log.info("--cad_tracking ()---" + status);
	} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	} finally {
	commonDB.closeConnection(con, st, rs);
}
	return status;

}
	
	public ArrayList Cpfdata(String selectedYear,
			String month, String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName, String formType, String formRevisedFlag,
			String adjFlag, String frmName, String pcFlag,String selectyear) {
		ArrayList totalYearForm8List = new ArrayList();
		ArrayList form8List = new ArrayList();
		String minYear = "", maxYear = "";
		boolean chkYears = false;
		String message = "";
		int messageFromYear = 0, messageToYear = 0;
		Form7MultipleYearBean multipleYearBean = null;
		int currentYear = 0, fromOldYear = 1995, totalSpan = 0;
		log.info(" selectedYear======getRPFCForm8IndivReport======="
				+ selectedYear+" empflag "+empflag);
		if (selectedYear.equals("NO-SELECT")
				|| selectedYear.equals("Select One")) {
			currentYear = Integer.parseInt(commonUtil.getCurrentDate("yyyy")) + 1;
			fromOldYear = 1995;
			totalSpan = currentYear - fromOldYear;
		} else {
			if (selectedYear.indexOf("-") != -1) {
				String[] minMaxYear = selectedYear.split("-");
				minYear = minMaxYear[0];
				maxYear = minMaxYear[1];
				chkYears = true;
			} else {
				minYear = selectedYear;
			}
			fromOldYear = Integer.parseInt(minYear);
			if (chkYears == true) {
				currentYear = Integer.parseInt(maxYear);
			} else {
				currentYear = fromOldYear + 1;
			}

			totalSpan = currentYear - fromOldYear;
		}

		
			log.info(" rnfc form8list ::  ");
			multipleYearBean = new Form7MultipleYearBean();
			form8List = this.rnfcCpfdata(
					month, sortedColumn, region, formFlag, airportCode,
					pensionNo, range, empflag, empName, formType,
					formRevisedFlag, adjFlag, frmName,pcFlag,selectyear);
			log.info(" rnfc form8list ::  "+form8List.size());
			if (form8List.size() != 0) {		
				
				multipleYearBean.setEachYearList(form8List);
				multipleYearBean.setMessage(message);
				totalYearForm8List.add(multipleYearBean);
			}
		
		return totalYearForm8List;
	}
	
	
	public ArrayList rnfcCpfdata( String month,
			String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName, String formType, String formRevisedFlag,
			String adjFlag, String frmName, String pcFlag,String selectyear) {
//		log.info("pcFlag:::::::::::::::::::"+pcFlag);
		String fromYear = "", toYear = "", dateOfRetriment = "", frmMonth = "",actualDOR="";
		int toSelectYear = 0;
		int Count=0;
		ArrayList empList = new ArrayList();
		ArrayList arrearEmpList = new ArrayList();
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		ArrayList form8List = new ArrayList();
		ArrayList getPensionList = new ArrayList();
		boolean arrearsFlag = false;
		if (!month.equals("NO-SELECT")) {
			try {
				frmMonth = commonUtil.converDBToAppFormat(month, "MM", "MMM");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	

		empList = this.getForm7EmployeeInfo1(range, sortedColumn, region,
				fromYear, toYear, airportCode, pensionNo, empflag, empName,
				"N", formType, formRevisedFlag,adjFlag,selectyear);

	
		log.info("rnfcForm7Report:::fromYear" + fromYear + "toYear" + toYear
				+ "arrearsFlag" + arrearsFlag + "empList.size()"
				+ empList.size());

		String pensionInfo = "", regionInfo = "";

		for (int i = 0; i < empList.size(); i++) {
			Count++;
			personalInfo = (EmployeePersonalInfo) empList.get(i);
			getPensionList = this.getForm7Pensioncpfdata(toYear,
					personalInfo.getPfIDString(), personalInfo
							.getWetherOption(), personalInfo.getRegion(),
					false, dateOfRetriment, personalInfo.getDateOfBirth(),
					personalInfo.getPensionNo(), arrearsFlag,
					formRevisedFlag, adjFlag, frmName,pcFlag,personalInfo.getSeperationReason(),selectyear);
			personalInfo.setPensionList(getPensionList);
			dateOfRetriment = "";
			personalInfo.setCount(Count);
			System.out.println(" count :: "+personalInfo.getCount());
			form8List.add(personalInfo);
		}
		return form8List;
	}
	
	
	private ArrayList getForm7Pensioncpfdata(String toDate,
			String pfIDS, String wetherOption, String region, boolean formFlag,
			String dateOfRetriment, String dateOfBirth, String pensionNo,
			boolean arrearsFlag, String formRevisedFlag, String adjFlag,
			String frmName,String pcFlag,String sepReason,String selectyear) {
		
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		CadPensionInfo cardInfo = null;
		ArrayList pensionList = new ArrayList();
		String  sqlQuery = "";	
		
		try {
			con = commonDB.getConnection();
			
	sqlQuery = "select to_number(pensionno) as Pensionno,year,emolument as Emoluments,cpf_contribution as CONTRIBUTION, addi_contri as Additional,net_epf as NetEPf,cpf_intrest as CPFIntrest,epfo_intrest as EpfoIntrest,epfo_pension as EpfoPension from CAD_REPORT_ALLYEARS p where p.pensionno="+pensionNo;							    
			
			log.info(" sqlQuery  " + sqlQuery);
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				cardInfo = new CadPensionInfo();
				
				if (rs.getString("year") != null) {
					cardInfo.setYear(rs.getString("year"));
				} else {
					cardInfo.setYear("-");
				}
								
				if (rs.getString("Emoluments") != null) {
					cardInfo.setEmoluments(rs.getString("Emoluments"));
				} else {
					cardInfo.setEmoluments("-");
				}
				
				
				if (rs.getString("CONTRIBUTION") != null) {
					cardInfo.setCONTRIBUTION(rs.getString("CONTRIBUTION"));
				} else {
					cardInfo.setCONTRIBUTION("-");
				}
				
				if (rs.getString("Additional") != null) {
					cardInfo.setAdditional(rs.getString("Additional"));
				} else {
					cardInfo.setAdditional("-");
				}
				
				if (rs.getString("NetEPf") != null) {
					cardInfo.setNetEPf(rs.getString("NetEPf"));
				} else {
					cardInfo.setNetEPf("-");
				}
				
				if (rs.getString("CPFIntrest") != null) {
					cardInfo.setCPFIntrest(rs.getString("CPFIntrest"));
				} else {
					cardInfo.setCPFIntrest("-");
				}
				
				if (rs.getString("EpfoIntrest") != null) {
					cardInfo.setEpfoIntrest(rs.getString("EpfoIntrest"));
				} else {
					cardInfo.setEpfoIntrest("-");
				}
				
				if (rs.getString("EpfoPension") != null) {
					cardInfo.setEpfoPension(rs.getString("EpfoPension"));
				} else {
					cardInfo.setEpfoPension("-");
				}
				
				
				System.out.println("  cardInfo  :: ");
				pensionList.add(cardInfo);
				
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return pensionList;
	}
	private ArrayList getForm7EmployeeInfo1(String range, String sortedColumn,
			String region, String fromYear, String toYear, String airportCode,
			String pensionNo, String empNameFlag, String empName,
			String arrears, String formType, String formRevisedFlag,String adjFlag,String selectyear) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String remarks = "", airportcodString = "", chkDOE = "", findFromYear = "", payrevisionarrear = "", arrearInfo = "", findToYear = "", sqlQuery = "", seperationFlag = "", pensionAppednQry = "", pfidWithRegion = "", appendRegionTag = "", nextAppendRegionTag = "", appendAirportTag = "", appendPenTag = "",chkForArrearAfter58Flag="false";
		EmployeePersonalInfo data = null;
		ArrayList empinfo = new ArrayList();
		int startIndex = 0, endIndex = 0;
		if (region.equals("NO-SELECT")) {
			region = "";
		}
		if (airportCode.equals("NO-SELECT")) {
			airportcodString = "";
		} else {
			airportcodString = airportCode;
		}

		try {
			findFromYear = commonUtil.converDBToAppFormat(fromYear,
					"dd-MMM-yyyy", "yyyy");
			findToYear = commonUtil.converDBToAppFormat(toYear, "dd-MMM-yyyy",
					"yyyy");
		} catch (InvalidDataException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
					
		log.info("formRevisedFlag=================" + formRevisedFlag+"arrears==============="+arrears +" empNameFlag "+empNameFlag);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			
						sqlQuery = this.buildQuerygetEmployeePFInfoPrintingforCpfdata(range,
								region, empNameFlag, empName, sortedColumn,
								pensionNo, toYear, fromYear,selectyear); 
						log.info(" count sql ");

			log.info("FinanceReportDAO::getForm7EmployeeInfo" + sqlQuery);
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				data = new EmployeePersonalInfo();
				CommonDAO commonDAO = new CommonDAO();
				data = commonDAO.loadPersonalInfo(rs);
				log.info(" data "+data.getPensionNo());
				
				System.out.println(" data pf ..."+data.getPensionNo());
					empinfo.add(data);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return empinfo;

	}
	
	public String buildQuerygetEmployeePFInfoPrintingforCpfdata(String range,
			String region, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String toYear,
			String fromYear,String selectyear) {
		log
				.info("FinanceReportDAO::buildQuerygetEmployeePFInfoPrinting-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = " select * from employee_personal_info i where i.pensionno = "+pensionno;
		
		if (!selectyear.equals("NO-SELECT")) {		
			whereClause.append("  i.finayear ='"+selectyear+"' ");
			whereClause.append(" AND ");
		}
		
		
		if (!range.equals("NO-SELECT")) {

			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);
			whereClause.append("  i.pfid >="+startIndex+" AND i.pfid <="+endIndex+"");
			whereClause.append(" AND ");
		}
		if (empNameFlag.equals("true")) {
			if (!pensionno.equals("")) {
				whereClause.append("i.PENSIONNO='" + pensionno + "' ");
				whereClause.append(" AND ");
			}
		}
		query.append(sqlQuery);
		if (region.equals("")
				&& (range.equals("NO-SELECT") && toYear.equals("") && (empNameFlag
						.equals("false")))) {

		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}
		query.append(" order by i.pensionno ");
		dynamicQuery = query.toString();
		log
				.info(" Emp Personal Info  "+dynamicQuery);
		return dynamicQuery;
	}}
