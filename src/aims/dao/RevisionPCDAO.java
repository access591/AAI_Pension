package aims.dao;

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

import aims.bean.EmolumentslogBean;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeePensionCardInfo;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinacialDataBean;
import aims.bean.Form7MultipleYearBean;
import aims.bean.PensionContBean;
import aims.bean.TempPensionTransBean;
import aims.common.CommonUtil;
import aims.common.Constants;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;

public class RevisionPCDAO {
	Log log = new Log(FinancialReportDAO.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();
	public ArrayList pensionContributionReportAll(String fromDate,
			String toDate, String region, String airportcode,
			String empserialNO, String cpfAccno, String transferFlag,
			String pfIDStrip, String bulkPrint,String edit) {
		

		// the below line for table check
		boolean recoverieTable = false;
		if(edit.equals("edit")){
			recoverieTable=true;
		}
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
				penContBean.setDateofSeperationDt(penContHeaderBean.getDateofSeperationDt());
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
							bean.setPensionContriB(pensionInfo[22].trim());
							bean.setEmolumentsB(pensionInfo[23].trim());
							log.info("PcApplied " + bean.getPccalApplied()+pensionInfo[22].trim()+"pcb"+pensionInfo[21].trim()+"nn"+bean.getPensionContriB());
							
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
	public ArrayList pensionContributionReportAllSecLvl(String fromDate,
			String toDate, String region, String airportcode,
			String empserialNO, String cpfAccno, String transferFlag,
			String pfIDStrip, String bulkPrint,String edit) {
		// the below line for table check
		boolean recoverieTable = false;
		if(edit.equals("edit")){
			recoverieTable=true;
		}
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
				pensionList = this.getEmployeePensionInfoSecLvl(preparedString,
						findFromDate, toDate, penContHeaderBean
								.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), penContHeaderBean
								.getEmpSerialNo(), recoverieTable);
				String rateFromYear = "", rateToYear = "", checkMnthDate = "", rateOfInterest = "";
				boolean rateFlag = false;
				if (!pensionList.equals("")) {
					empPensionList = pensionList.split("=");
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
							bean.setPensionContriB(pensionInfo[22].trim());
							bean.setEmolumentsB(pensionInfo[23].trim());
							log.info("PcApplied " + bean.getPccalApplied()+pensionInfo[22].trim()+"pcb"+pensionInfo[21].trim()+"nn"+bean.getPensionContriB());
							
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
					if(rs.getString("DATEOFSEPERATION")!=null){
					data.setDateofSeperationDt(rs.getString("DATEOFSEPERATION"));
					}else{
						data.setDateofSeperationDt("---");
					}
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
	private String getEmployeePensionInfo(String cpfString, String fromDate,
			String toDate, String whetherOption, String dateOfRetriment,
			String dateOfBirth, String Pensionno, boolean recoverieTable) {
		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		//String tablename = "Epis_adj_crtn";
		String tablename = "Employee_pension_validate";
		if (recoverieTable == true) {
			tablename = "employee_freshop_crtn";
		}
		log.info("formdate " + fromDate + "todate " + toDate);
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from employee_info where ("
				+ tempCpfString + ") and rownum=1)";
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,nvl(emp.freshopflag,'N') as freshopflag,emp.Emoluments_b as Emoluments_b,emp.pensioncontri_b as pensioncontri_b,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from epis_adj_crtn "
				//+ tablename
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,nvl(freshopflag,'N') as freshopflag,round(Emoluments_b,2) as Emoluments_b,pensioncontri_b as pensioncontri_b,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI FROM "
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
				+ " emp.EMOLUMENTS AS EMOLUMENTS,nvl(emp.freshopflag,'N') as freshopflag,emp.Emoluments_b as Emoluments_b,emp.pensioncontri_b as pensioncontri_b,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,"
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,nvl(freshopflag,'N') as freshopflag,round(Emoluments_b,2) as Emoluments_b,pensioncontri_b as pensioncontri_b,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI "
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
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('01-Apr-1995')";

		// String advances =
		// "select amount from employee_pension_advances where pensionno=1";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		String monthsBuffer = "", formatter = "", tempMntBuffer = "";
		long transMntYear = 0, empRetriedDt = 0;
		double pensionCOntr = 0;
		double pensionCOntr1 = 0,pensionCOntrB=0;
		String recordCount = "";
		int getDaysBymonth = 0, cnt = 0, checkMnts = 0, chkPrvmnth = 0, chkCrntMnt = 0;
		double PENSIONCONTRI = 0;
		boolean contrFlag = false, chkDOBFlag = false, formatterFlag = false;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			//log.info(sqlQuery);
			rs = st.executeQuery(sqlQuery);
			//log.info("Query" + sqlQuery);
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
					if (contrFlag != true) {
						pensionCOntr1 = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), "A",
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntr1 = Math.round(pensionCOntr1
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}
					} else {
						pensionCOntr1 = 0;
					}
					buffer.append(Double.toString(pensionCOntr1));
				} else {
					buffer.append("0");
				}
				
				buffer.append(",");
				if (rs.getString("Datafreezeflag") != null) {
					buffer.append(rs.getString("Datafreezeflag"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				if (rs.getString("FORM7NARRATION") != null) {
					buffer.append(rs.getString("FORM7NARRATION"));
				} else {
					buffer.append("--");
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
					// log.info("chkPrvmnth"+chkPrvmnth+"chkCrntMnt"+chkCrntMnt+
					// "Monthyear"+monthYear+"buffer ==== checkMnts"+checkMnts);
					if (frntYear.equals("")) {
						monthsBuffer = monthsBuffer + formatter + monthYear
								+ marMnt.trim();
					} else if (!frntYear.equals("")) {

						// log.info("buffer ==== frntYear"+monthsBuffer);
						// log.info("monthYear======"+monthYear+"cnt"+cnt+
						// "frntYear"+frntYear);

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
				buffer.append(",");
				if(recoverieTable == true){
				if (transdate.after(new Date("31-Mar-16"))
						|| (rs.getString("freshopflag").equals("Y"))) {
				/*	if (rs.getString("PENSIONCONTRI") != null) {
						PENSIONCONTRI = Double.parseDouble(rs
								.getString("PENSIONCONTRI"));
						buffer.append(rs.getString("PENSIONCONTRI"));
					} else {
						buffer.append("0.00");
					}*/
					pensionCOntrB = Math.round(Double.parseDouble(rs
					.getString("pensioncontri_b")));
					buffer.append(pensionCOntrB);
				}
				else if (rs.getString("EMOLUMENTS") != null) {
					if (contrFlag != true) {
						pensionCOntrB = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), "B",
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntrB = Math.round(pensionCOntr1
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}
					}
				 else {
						pensionCOntrB = 0;
					}
					
					 
					
					
					
					
					System.out.println("pensionCOntrB:"+pensionCOntrB);
					buffer.append(Double.toString(pensionCOntrB));
				} else {
					buffer.append("0");
				}
				buffer.append(",");
				}else{
					if (transdate.after(new Date("31-Mar-16"))
							|| (rs.getString("deputationflag").equals("Y"))) {
						if (rs.getString("PENSIONCONTRI") != null) {
							PENSIONCONTRI = Double.parseDouble(rs
									.getString("PENSIONCONTRI"));
							buffer.append(rs.getString("PENSIONCONTRI"));
						} else {
							buffer.append("0.00");
						
					}
					}else if (rs.getString("EMOLUMENTS") != null) {
						if (contrFlag != true) {
							pensionCOntrB = commonDAO.pensionCalculation(rs
									.getString("MONTHYEAR"), rs
									.getString("EMOLUMENTS"), "B",
									region, rs.getString("emolumentmonths"));
							if (chkDOBFlag == true) {
								String[] dobList = dateOfBirth.split("-");
								days = dobList[0];
								getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
								pensionCOntrB = Math.round(pensionCOntr1
										* (Double.parseDouble(days) - 1)
										/ getDaysBymonth);

							}
						}
					 else {
							pensionCOntrB = 0;
						}
						
						 
						
						
						
						
						System.out.println("pensionCOntrB:"+pensionCOntrB);
						buffer.append(Double.toString(pensionCOntrB));
					} else {
						buffer.append("0");
					}
					buffer.append(",");
				}
				
				//log.info("ooooooooooooooo======oooooooooo"+rs.getString("EMPMNTHYEAR"));
				//log.info("ooooooooooooooo======oooooooooo"+commonUtil.converDBToAppFormat(rs
						//.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").substring(3));
				if((commonUtil.converDBToAppFormat(rs
						.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Apr-1995")||(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("May-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Jun-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Jul-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Aug-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Sep-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy")).substring(3).equals("Oct-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Nov-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy")).substring(3).equals("Dec-1995")){
					if(rs.getString("freshopflag").equals("Y")){
						buffer.append(rs.getString("EMOLUMENTS_b"));
					}else{
						//log.info("rs78787878787878787878787878787878"+commonUtil.converDBToAppFormat(rs
								//.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").substring(3));
						if(rs.getString("EMOLUMENTS")!=null){
							double basic=Double.parseDouble(rs.getString("EMOLUMENTS"));
							if(basic>5000)
								basic=5000;
						buffer.append(basic);
						}else{
							buffer.append("0");
						}
					}
				}
				else if (transdate.after(new Date("31-Mar-16"))
						|| (rs.getString("freshopflag").equals("Y"))) {
					buffer.append(rs.getString("EMOLUMENTS_b"));
				}else{
					buffer.append("0");
				}
					buffer.append(",");
				if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
					buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
				} else {
					buffer.append("N");
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
	private String getEmployeePensionInfoSecLvl(String cpfString, String fromDate,
			String toDate, String whetherOption, String dateOfRetriment,
			String dateOfBirth, String Pensionno, boolean recoverieTable) {
		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		//String tablename = "Epis_adj_crtn";
		String tablename = "Employee_pension_validate";
		if (recoverieTable == true) {
			tablename = "employee_freshop_crtn_b";
		}
		log.info("formdate " + fromDate + "todate " + toDate);
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from employee_info where ("
				+ tempCpfString + ") and rownum=1)";
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,nvl(emp.freshopflag,'N') as freshopflag,emp.Emoluments_b as Emoluments_b,emp.pensioncontri_b as pensioncontri_b,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from epis_adj_crtn "
				//+ tablename
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,nvl(freshopflag,'N') as freshopflag,round(Emoluments_b,2) as Emoluments_b,pensioncontri_b as pensioncontri_b,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI FROM "
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
				+ " emp.EMOLUMENTS AS EMOLUMENTS,nvl(emp.freshopflag,'N') as freshopflag,emp.Emoluments_b as Emoluments_b,emp.pensioncontri_b as pensioncontri_b,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,"
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.DATAFREEZEFLAG as DATAFREEZEFLAG,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,nvl(freshopflag,'N') as freshopflag,round(Emoluments_b,2) as Emoluments_b,pensioncontri_b as pensioncontri_b,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,DATAFREEZEFLAG,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI "
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
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('01-Apr-1995')";

		// String advances =
		// "select amount from employee_pension_advances where pensionno=1";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		String monthsBuffer = "", formatter = "", tempMntBuffer = "";
		long transMntYear = 0, empRetriedDt = 0;
		double pensionCOntr = 0;
		double pensionCOntr1 = 0,pensionCOntrB=0;
		String recordCount = "";
		int getDaysBymonth = 0, cnt = 0, checkMnts = 0, chkPrvmnth = 0, chkCrntMnt = 0;
		double PENSIONCONTRI = 0;
		boolean contrFlag = false, chkDOBFlag = false, formatterFlag = false;
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			//log.info(sqlQuery);
			rs = st.executeQuery(sqlQuery);
			//log.info("Query" + sqlQuery);
			// log.info("Query" +sqlQuery1);
			//FileWriter fw=new FileWriter("E:\\prasad\\qry.txt");
	    	//fw.write(sqlQuery);
	    	//fw.flush();
	    	
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
					if (contrFlag != true) {
						pensionCOntr1 = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), "A",
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntr1 = Math.round(pensionCOntr1
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}
					} else {
						pensionCOntr1 = 0;
					}
					buffer.append(Double.toString(pensionCOntr1));
				} else {
					buffer.append("0");
				}
				
				buffer.append(",");
				if (rs.getString("Datafreezeflag") != null) {
					buffer.append(rs.getString("Datafreezeflag"));
				} else {
					buffer.append("-NA-");
				}
				buffer.append(",");
				if (rs.getString("FORM7NARRATION") != null) {
					buffer.append(rs.getString("FORM7NARRATION"));
				} else {
					buffer.append("--");
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
					// log.info("chkPrvmnth"+chkPrvmnth+"chkCrntMnt"+chkCrntMnt+
					// "Monthyear"+monthYear+"buffer ==== checkMnts"+checkMnts);
					if (frntYear.equals("")) {
						monthsBuffer = monthsBuffer + formatter + monthYear
								+ marMnt.trim();
					} else if (!frntYear.equals("")) {

						// log.info("buffer ==== frntYear"+monthsBuffer);
						// log.info("monthYear======"+monthYear+"cnt"+cnt+
						// "frntYear"+frntYear);

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
				buffer.append(",");
				if(recoverieTable == true){
				if (transdate.after(new Date("31-Mar-15"))
						|| (rs.getString("freshopflag").equals("Y"))) {
				/*	if (rs.getString("PENSIONCONTRI") != null) {
						PENSIONCONTRI = Double.parseDouble(rs
								.getString("PENSIONCONTRI"));
						buffer.append(rs.getString("PENSIONCONTRI"));
					} else {
						buffer.append("0.00");
					}*/
					pensionCOntrB = Math.round(Double.parseDouble(rs
					.getString("pensioncontri_b")));
					buffer.append(pensionCOntrB);
				}
				else if (rs.getString("EMOLUMENTS") != null) {
					if (contrFlag != true) {
						pensionCOntrB = commonDAO.pensionCalculation(rs
								.getString("MONTHYEAR"), rs
								.getString("EMOLUMENTS"), "B",
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionCOntrB = Math.round(pensionCOntr1
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}
					}
				 else {
						pensionCOntrB = 0;
					}
					
					 
					
					
					
					
					System.out.println("pensionCOntrB:"+pensionCOntrB);
					buffer.append(Double.toString(pensionCOntrB));
				} else {
					buffer.append("0");
				}
				buffer.append(",");
				}else{
					if (transdate.after(new Date("31-Mar-15"))
							|| (rs.getString("deputationflag").equals("Y"))) {
						if (rs.getString("PENSIONCONTRI") != null) {
							PENSIONCONTRI = Double.parseDouble(rs
									.getString("PENSIONCONTRI"));
							buffer.append(rs.getString("PENSIONCONTRI"));
						} else {
							buffer.append("0.00");
						
					}
					}else if (rs.getString("EMOLUMENTS") != null) {
						if (contrFlag != true) {
							pensionCOntrB = commonDAO.pensionCalculation(rs
									.getString("MONTHYEAR"), rs
									.getString("EMOLUMENTS"), "B",
									region, rs.getString("emolumentmonths"));
							if (chkDOBFlag == true) {
								String[] dobList = dateOfBirth.split("-");
								days = dobList[0];
								getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
								pensionCOntrB = Math.round(pensionCOntr1
										* (Double.parseDouble(days) - 1)
										/ getDaysBymonth);

							}
						}
					 else {
							pensionCOntrB = 0;
						}
						
						 
						
						
						
						
						System.out.println("pensionCOntrB:"+pensionCOntrB);
						buffer.append(Double.toString(pensionCOntrB));
					} else {
						buffer.append("0");
					}
					buffer.append(",");
				}
				
				//log.info("ooooooooooooooo======oooooooooo"+rs.getString("EMPMNTHYEAR"));
				//log.info("ooooooooooooooo======oooooooooo"+commonUtil.converDBToAppFormat(rs
						//.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").substring(3));
				if((commonUtil.converDBToAppFormat(rs
						.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Apr-1995")||(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("May-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Jun-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Jul-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Aug-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Sep-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy")).substring(3).equals("Oct-1995")||(commonUtil.converDBToAppFormat(rs
										.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").toString()).substring(3).equals("Nov-1995")||
						(commonUtil.converDBToAppFormat(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy")).substring(3).equals("Dec-1995")){
					if(rs.getString("freshopflag").equals("Y")){
						buffer.append(rs.getString("EMOLUMENTS_b"));
					}else{
						//log.info("rs78787878787878787878787878787878"+commonUtil.converDBToAppFormat(rs
								//.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy").substring(3));
						if(rs.getString("EMOLUMENTS")!=null){
							
								double basic=Double.parseDouble(rs.getString("EMOLUMENTS"));
								if(basic>5000)
									basic=5000;
							buffer.append(basic);
						}else{
							buffer.append("0");
						}
					}
				}
				else if (transdate.after(new Date("31-Mar-15"))
						|| (rs.getString("freshopflag").equals("Y"))) {
					buffer.append(rs.getString("EMOLUMENTS_b"));
				}else{
					buffer.append("0");
				}
					buffer.append(",");
				if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
					buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
				} else {
					buffer.append("N");
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
	public String buildPenContRptQuery(String region, String airportCD,
			String empserialNO, String cpfAccno, String transferFlag) {
		log.info("FinancialReportDAO :buildPenContRptQuery() entering method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO";
		if (!cpfAccno.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND  EPI.region='"
					+ region
					+ "' and EPI.cpfacno='"
					+ cpfAccno
					+ "' AND ETI.TRANSFERFLAG='" + transferFlag + "' ";
		} else if (empserialNO.equals("") && region.equals("NO-SELECT")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND ETI.TRANSFERFLAG='"
					+ transferFlag + "' ";
		} else if (empserialNO.equals("") && !region.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND  EPI.region='"
					+ region + "' AND ETI.TRANSFERFLAG='" + transferFlag + "' ";
		} else {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO AND EPI.PENSIONNO='"
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
		log.info("FinancialReportDAO :buildQuery() leaving method");
		return dynamicQuery;
	}
	public String buildPenContRptWthOutTransferQuery(String region,
			String airportCD, String empserialNO, String cpfAccno) {
		log
				.info("FinancialReportDAO :buildPenContRptWthOutTransferQuery() entering method");

		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.DEPARTMENT AS DEPARTMENT,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI WHERE EPI.PENSIONNO IS NOT NULL ";

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
				.info("FinancialReportDAO :buildPenContRptWthOutTransferQuery() leaving method");
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
			contr.setDateOfEntitle("01-Apr-1995");
		}
		return contr;

	}

	public ArrayList rnfcForm8Report(String selectedYear, String month,
			String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo) {
		String fromYear = "", toYear = "", dateOfRetriment = "", frmMonth = "";
		int toSelectYear = 0;
		ArrayList empList = new ArrayList();
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		ArrayList form8List = new ArrayList();
		ArrayList getPensionList = new ArrayList();
		if (!month.equals("NO-SELECT")) {
			try {
				frmMonth = commonUtil.converDBToAppFormat(month, "MM", "MMM");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!selectedYear.equals("Select One") && month.equals("NO-SELECT")) {
			fromYear = "01-Apr-" + selectedYear;
			toSelectYear = Integer.parseInt(selectedYear) + 1;
			toYear = "01-Mar-" + toSelectYear;
		} else if (!selectedYear.equals("Select One")
				&& !month.equals("NO-SELECT")) {
			fromYear = "01-" + frmMonth + "-" + selectedYear;
			toYear = fromYear;
		} else {
			fromYear = "01-Apr-1995";
			toYear = "01-Mar-2008";
		}
		log.info(fromYear + "====" + toYear);
		empList = this.getForm8EmployeeInfo(sortedColumn, region, formFlag,
				fromYear, airportCode, pensionNo);
		String pensionInfo = "", regionInfo = "";

		for (int i = 0; i < empList.size(); i++) {
			personalInfo = (EmployeePersonalInfo) empList.get(i);

			if (!personalInfo.getDateOfBirth().equals("---")) {
				try {
					dateOfRetriment = commonDAO.getRetriedDate(personalInfo
							.getDateOfBirth());
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (formFlag == true) {
				if (!personalInfo.getPfIDString().equals("")) {
					pensionInfo = this.getEmployeePensionInfo(fromYear, toYear,
							personalInfo.getPfIDString(), personalInfo
									.getWetherOption().trim(), personalInfo
									.getRegion(), dateOfRetriment, personalInfo
									.getDateOfBirth(), pensionNo);

					if (personalInfo.getRegion().equals("CHQIAD")) {
						regionInfo = "IAD-" + airportCode;
						personalInfo.setRegion(regionInfo);
						regionInfo = "";
					}
					if (!pensionInfo.equals("NO-RECORDS")) {
						personalInfo.setPensionInfo(pensionInfo);
					} else {
						personalInfo = null;
					}

				}

			} else {

				getPensionList = this.getEmployeePensionCard(fromYear, toYear,
						personalInfo.getPfIDString(), personalInfo
								.getWetherOption(), personalInfo.getRegion(),
						false, dateOfRetriment, personalInfo.getDateOfBirth(),
						personalInfo.getOldPensionNo());
				personalInfo.setPensionList(getPensionList);
			}
			dateOfRetriment = "";
			form8List.add(personalInfo);
		}
		return form8List;
	}
	public String buildPCReportQuery(String region, String airportCD,
			String empserialNO, String cpfAccno, String transferFlag,
			int startPensionNo, int endPensionno, String pfdString) {
		log.info("FinancialReportDAO :buildPCReportQuery() entering method");
		log.info("endPensionno" + empserialNO);
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String sqlQuery = "";
		String dynamicQuery = "";
		log.info(" transferFlag " + transferFlag);
		if (!transferFlag.equals("")) {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DEPARTMENT AS DEPARTMENT,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,to_char(EPI.DATEOFSEPERATION_DATE,'DD-Mon-YYYY') AS DATEOFSEPERATION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL AND ETI.PENSIONNO=EPI.PENSIONNO ";
		} else {
			sqlQuery = "SELECT DISTINCT NVL(EPI.CPFACNO,'NO-VAL') AS CPFACNO,EPI.REFPENSIONNUMBER AS PENSIONNUMBER,EPI.REGION AS REGION,EPI.MARITALSTATUS AS MARITALSTATUS,EPI.PENSIONNO AS EMPSERIALNUMBER,EPI.DATEOFJOINING AS DATEOFJOINING,EPI.EMPLOYEENO AS EMPLOYEENO,EPI.DEPARTMENT AS DEPARTMENT,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.GENDER AS SEX,EPI.FHNAME AS FHNAME,EPI.DESEGNATION AS DESEGNATION,EPI.WETHEROPTION AS WETHEROPTION,to_char(EPI.DATEOFSEPERATION_DATE,'DD-Mon-YYYY') AS DATEOFSEPERATION,round(months_between(NVL(EPI.DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF FROM EMPLOYEE_PERSONAL_INFO EPI,MV_EMPLOYEES_TRANSFER_INFO ETI WHERE EPI.PENSIONNO IS NOT NULL  ";
		}
		log.info(sqlQuery);
		if (!airportCD.equals("NO-SELECT")) {
			whereClause.append(" LOWER(EPI.AIRPORTCODE) ='"
					+ airportCD.trim().toLowerCase() + "'");
			whereClause.append(" AND ");
		}
		if (!empserialNO.equals("")) {
			whereClause.append(" EPI.PENSIONNO ='" + empserialNO + "'");
			//whereClause.append(" EPI.PENSIONNO in (select pensionno from employee_pension_freshoption f where f.deleteflag='N' and f.freshpensionoption='B' and f.weatheroption='A'  and pensionno in (1117,4902,10993)) ");
			
			//whereClause.append(" EPI.PENSIONNO in (17336,11942,10650,25636,18932,22146) ");
			

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
		log.info("FinancialReportDAO :buildPCReportQuery() leaving method");
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
	private ArrayList getForm8EmployeeInfo(String sortedColumn, String region) {
		log.info("FinanceReportDAO::getForm8EmployeeInfo");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfidWithRegion = "", appendRegionTag = "";
		EmployeePersonalInfo data = null;
		ArrayList empinfo = new ArrayList();
		if (sortedColumn.toLowerCase().equals("cpfaccno")) {
			sortedColumn = "cpfacno";
		}
		if (region.equals("NO-SELECT")) {
			appendRegionTag = "";
		} else {
			appendRegionTag = "WHERE REGION='" + region + "' ";
		}
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			sqlQuery = "SELECT REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO, EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,(LAST_DAY(dateofbirth) + INTERVAL '60' year ) as DOR,username,LASTACTIVE,RefMonthYear,IPAddress,OTHERREASON,decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB FROM EMPLOYEE_PERSONAL_INFO "
					+ appendRegionTag + " ORDER BY " + sortedColumn;
			log.info("FinanceReportDAO::getEmployeePFInfo Query" + sqlQuery);
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				data = new EmployeePersonalInfo();
				CommonDAO commonDAO = new CommonDAO();
				data = commonDAO.loadPersonalInfo(rs);
				pfidWithRegion = commonDAO.getEmployeeMappingPFInfo(con, data
						.getPensionNo(), data.getCpfAccno(), data.getRegion());
				String[] pfIDLists = pfidWithRegion.split("=");
				data.setPfIDString(commonDAO.preparedCPFString(pfIDLists));
				if (data.getWetherOption().trim().equals("---")) {
					data.setRemarks("Wether Option is not available");
				}
				log.info(data.getPfIDString());
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
	private ArrayList getForm8EmployeeInfo(String sortedColumn, String region,
			boolean formFlag, String fromYear, String airportCode,
			String pensionNo) {
		log.info("FinanceReportDAO::getForm8EmployeeInfo");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfidWithRegion = "", appendRegionTag = "", nextAppendRegionTag = "", appendAirportTag = "", appendPenTag = "";
		EmployeePersonalInfo data = null;
		ArrayList empinfo = new ArrayList();
		if (sortedColumn.toLowerCase().equals("cpfaccno")) {
			sortedColumn = "cpfacno";
		}

		if (pensionNo.equals("")) {
			appendPenTag = "";
		} else {
			appendPenTag = " AND PENSIONNO='" + pensionNo + "' ";
		}
		if (airportCode.equals("NO-SELECT")) {
			appendAirportTag = "";
		} else {
			appendAirportTag = "AND AIRPORTCODE='" + airportCode + "' ";
		}
		if (region.equals("NO-SELECT")) {
			appendRegionTag = "WHERE  NVL(DATEOFJOINING,'01-Mar-2009') <='01-Mar-2009' ";
			nextAppendRegionTag = " ";
		} else {
			appendRegionTag = "WHERE  NVL(DATEOFJOINING,'01-Mar-2009') <='01-Mar-2009' AND REGION='"
					+ region + "' " + appendAirportTag + appendPenTag;
			nextAppendRegionTag = " AND REGION='" + region + "'";
		}
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			/*
			 * sqlQuery = "SELECT
			 * REFPENSIONNUMBER,CPFACNO,AIRPORTSERIALNUMBER,EMPLOYEENO,
			 * EMPLOYEENAME,DESEGNATION,EMP_LEVEL,DATEOFBIRTH,DATEOFJOINING,DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,AIRPORTCODE,GENDER,FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,REGION,PENSIONNO,(LAST_DAY(dateofbirth) +
			 * INTERVAL '60' year ) as
			 * DOR,username,LASTACTIVE,RefMonthYear,IPAddress,OTHERREASON,decode(sign(round(months_between(dateofjoining,
			 * '01-Apr-1995') / 12)),-1,
			 * '01-Apr-1995',1,to_char(dateofjoining,'dd-Mon-yyyy'),to_char(dateofjoining,'dd-Mon-yyyy'))
			 * as DATEOFENTITLE,to_char(trunc((dateofbirth + INTERVAL '60' year ),
			 * 'MM') - 1,'dd-Mon-yyyy') as LASTDOB,ROUND(months_between('" +
			 * fromYear+ "',dateofbirth)/12) EMPAGE FROM EMPLOYEE_PERSONAL_INFO " +
			 * appendRegionTag + " ORDER BY " + sortedColumn;
			 */
			sqlQuery = "SELECT PENSIONNUMBER, EMPSERIALNUMBER, CPFACNO, AIRPORTSERIALNUMBER, EMPLOYEENO, EMPLOYEENAME, DESEGNATION, EMP_LEVEL, "
					+ "DATEOFBIRTH, DATEOFJOINING, DATEOFSEPERATION_REASON, DATEOFSEPERATION_DATE, WHETHER_FORM1_NOM_RECEIVED, REMARKS, AIRPORTCODE, "
					+ "SEX, FHNAME, MARITALSTATUS, PERMANENTADDRESS, TEMPORATYADDRESS, WETHEROPTION, SETDATEOFANNUATION, EMPFLAG, DIVISION, DEPARTMENT, "
					+ "EMAILID, EMPNOMINEESHARABLE, REGION, (LAST_DAY(dateofbirth) + INTERVAL '60' year) as DOR, username, LASTACTIVE, OTHERREASON, decode(sign(round(months_between(dateofjoining, '01-Apr-1995') / 12)), -1, '01-Apr-1995', 1, to_char(dateofjoining, 'dd-Mon-yyyy'), to_char(dateofjoining, 'dd-Mon-yyyy')) as DATEOFENTITLE, to_char(trunc((dateofbirth + INTERVAL '60' year), 'MM') - 1, 'dd-Mon-yyyy') as LASTDOB, ROUND(months_between('01-Apr-1995', dateofbirth) / 12) "
					+ "EMPAGE FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER IN (SELECT PENSIONNO FROM EMPLOYEE_PERSONAL_INFO "
					+ appendRegionTag
					+ ") "
					+ nextAppendRegionTag
					+ " ORDER BY " + sortedColumn;
			log.info("FinanceReportDAO::getEmployeePFInfo Query" + sqlQuery);
			rs = st.executeQuery(sqlQuery);
			String remarks = "";
			while (rs.next()) {
				data = new EmployeePersonalInfo();
				CommonDAO commonDAO = new CommonDAO();
				data = commonDAO.loadEmployeePersonalInfo(rs);
				/*
				 * pfidWithRegion = this.getEmployeeForm8MappingPFInfo(data
				 * .getOldPensionNo(), data.getCpfAccno(), data.getRegion());
				 */
				pfidWithRegion = data.getCpfAccno() + "," + data.getRegion()
						+ "=";
				if (!pfidWithRegion.equals("")) {
					String[] pfIDLists = pfidWithRegion.split("=");
					data.setPfIDString(commonDAO.preparedCPFString(pfIDLists));
				}

				if (data.getWetherOption().trim().equals("---")) {
					data.setRemarks("Wether Option is not available");
				}

				if (rs.getString("DATEOFENTITLE") != null) {
					data.setDateOfEntitle(rs.getString("DATEOFENTITLE"));
				} else {
					data.setDateOfEntitle("---");
				}
				if (formFlag == false) {

					if (rs.getInt("EMPAGE") == 58) {
						remarks = "Attained 58 years";
					}
					if (data.getSeperationReason().trim().equals("Resignation")
							|| data.getSeperationReason().trim().equals(
									"Termination")) {
						remarks = remarks + data.getSeperationReason() + " On"
								+ data.getSeperationDate();
					}
					data.setRemarks(remarks);
				}

				empinfo.add(data);
				remarks = "";
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
	private String getEmployeePensionInfo(String fromDate, String toDate,
			String pfIDS, String wetherOption, String region,
			String dateOfRetriment, String dateOfBirth, String pension) {
		// log.info("FinanceReportDAO::getEmployeePensionInfo");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String emoluments = "", pfStatury = "", vpf = "", cpf = "", monthYear = "", days = "";
		;
		String checkDate = "", chkMnthYear = "Apr-1995";
		boolean flag = false;
		boolean contrFlag = false, chkDOBFlag = false;
		int getDaysBymonth = 0;
		long transMntYear = 0, empRetriedDt = 0;
		double pensionVal = 0.0, retriredEmoluments = 0.0, totalEmoluments = 0.0, totalPFStatury = 0.0, totalVPF = 0.0, totalCPF = 0.0, totalContribution = 0.0;
		String sqlQuery = "SELECT CPFACCNO,MONTHYEAR,ROUND(EMOLUMENTS) AS EMOLUMENTS,round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,ROUND(REFADV) AS REFADV, AIRPORTCODE,EMOLUMENTMONTHS FROM EMPLOYEE_PENSION_VALIDATE WHERE EMPFLAG='Y' AND (to_date(to_char(monthyear,'dd-Mon-yyyy'))>='"
				+ fromDate
				+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=LAST_DAY('"
				+ toDate
				+ "'))"
				+ " AND ("
				+ pfIDS
				+ ") ORDER BY TO_DATE(monthyear)";
		log.info("FinanceReportDAO::getEmployeePensionInfo" + sqlQuery);
		StringBuffer buffer = new StringBuffer();
		String calEmoluments = "", frmCpfaccno = "", emolumentsMonths = "";
		double cellingRate = 0.0;
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				flag = false;
				contrFlag = false;
				chkDOBFlag = false;
				if (rs.getString("MONTHYEAR") != null) {
					monthYear = CommonUtil.getDatetoString(rs
							.getDate("MONTHYEAR"), "dd-MMM-yyyy");

				}
				if (rs.getString("CPFACCNO") != null) {
					frmCpfaccno = rs.getString("CPFACCNO");

				}
				if (rs.getString("EMOLUMENTMONTHS") != null) {
					emolumentsMonths = rs.getString("EMOLUMENTMONTHS".trim());
				} else {
					emolumentsMonths = "1";
				}
				if (rs.getString("EMOLUMENTS") != null) {
					emoluments = rs.getString("EMOLUMENTS");
				} else {
					emoluments = "0.00";
				}
				try {
					checkDate = commonUtil.converDBToAppFormat(monthYear,
							"dd-MMM-yyyy", "MMM-yyyy");
					flag = false;
				} catch (InvalidDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// log.info(checkDate + "chkMnthYear===" + chkMnthYear);
				if (checkDate.toLowerCase().equals(chkMnthYear.toLowerCase())) {
					flag = true;
				}
				calEmoluments = this.calWages(emoluments, monthYear,
						wetherOption, false, false, "1");

				if (rs.getString("EMPPFSTATUARY") != null) {
					pfStatury = rs.getString("EMPPFSTATUARY");
				} else {
					pfStatury = "0.00";
				}
				if (rs.getString("EMPVPF") != null) {
					vpf = rs.getString("EMPVPF");
				} else {
					vpf = "0";
				}
				if (rs.getString("CPF") != null) {
					cpf = rs.getString("CPF");
				} else {
					cpf = "0";
				}

				if (flag == false) {
					if (!monthYear.equals("-NA-")
							&& !dateOfRetriment.equals("")) {
						transMntYear = Date.parse(monthYear);
						empRetriedDt = Date.parse(dateOfRetriment);

						if (transMntYear > empRetriedDt) {
							contrFlag = true;
						} else if (transMntYear == 0 || empRetriedDt == 0) {
							contrFlag = false;
						} else if (transMntYear == empRetriedDt
								&& transMntYear != 0 && empRetriedDt != 0) {
							chkDOBFlag = true;
						}
					}
					// log.info("transMntYear"+transMntYear+"empRetriedDt"+
					// empRetriedDt);
					// log.info("contrFlag"+contrFlag+"chkDOBFlag"+chkDOBFlag);
					if (contrFlag != true) {
						pensionVal = commonDAO.pensionFormsCalculation(monthYear,
								calEmoluments, wetherOption.trim(), region,
								false, false, emolumentsMonths);

						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionVal = pensionVal
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth;
							retriredEmoluments = new Double(df1.format(Double
									.parseDouble(calEmoluments)
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth)).doubleValue();
							calEmoluments = Double.toString(retriredEmoluments);
						}

					} else {
						pensionVal = 0;
						calEmoluments = "0";
					}

				} else {
					pensionVal = 0;
				}

				emoluments = calEmoluments;
				totalEmoluments = totalEmoluments
						+ Double.parseDouble(emoluments);
				totalPFStatury = totalPFStatury + Double.parseDouble(pfStatury);
				totalVPF = totalVPF + Double.parseDouble(vpf);
				totalCPF = totalCPF + Double.parseDouble(cpf);
				totalContribution = totalContribution + pensionVal;
				log.info("monthYear===" + monthYear + "==frmCpfaccno==="
						+ commonUtil.leadingZeros(20, frmCpfaccno)
						+ "emoluments===="
						+ commonUtil.leadingZeros(20, emoluments)
						+ "pensionVal====" + pensionVal);
			}

			if (totalEmoluments != 0 && totalPFStatury != 0
					&& totalContribution != 0) {
				buffer.append(Math.round(totalEmoluments));
				buffer.append(",");
				buffer.append(Math.round(totalPFStatury));
				buffer.append(",");
				buffer.append(Math.round(totalVPF));
				buffer.append(",");
				buffer.append(Math.round(totalCPF));
				buffer.append(",");
				buffer.append(totalContribution);
			} else {
				buffer.append("NO-RECORDS");
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
	private ArrayList getEmployeePensionCard(String fromDate, String toDate,
			String pfIDS, String wetherOption, String region, boolean formFlag,
			String dateOfRetriment, String dateOfBirth, String pensionNo) {

		DecimalFormat df = new DecimalFormat("#########0.00");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		EmployeePensionCardInfo cardInfo = null;
		ArrayList pensionList = new ArrayList();
		boolean flag = false;
		boolean contrFlag = false, chkDOBFlag = false;
		String checkDate = "", chkMnthYear = "Apr-1995";
		String monthYear = "", days = "", getMonth = "", sqlQuery = "";
		int getDaysBymonth = 0;
		long transMntYear = 0, empRetriedDt = 0;
		log.info("checkDate==" + checkDate + "flag===" + flag);
		double totalAdvancePFWPaid = 0, loanPFWPaid = 0, advancePFWPaid = 0, empNet = 0, aaiNet = 0, advPFDrawn = 0, empCumlative = 0.0, aaiPF = 0.0, aaiNetCumlative = 0.0;
		double pensionAsPerOption = 0.0;

		boolean obFlag = false;
		/*
		 * sqlQuery = "SELECT MONTHYEAR,ROUND(EMOLUMENTS) AS
		 * EMOLUMENTS,round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS
		 * EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS
		 * EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS
		 * EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS
		 * CPFADVANCE,ROUND(DEDADV) AS DEDADV,ROUND(REFADV) AS REFADV,
		 * AIRPORTCODE FROM EMPLOYEE_PENSION_VALIDATE WHERE
		 * (to_date(to_char(monthyear,'dd-Mon-yyyy'))>='" + fromDate + "' and
		 * to_date(to_char(monthyear,'dd-Mon-yyyy'))<='" + toDate + "')" + "
		 * AND (" + pfIDS + ") ORDER BY TO_DATE(monthyear)";
		 */

		sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,emp.* from (select distinct to_char(to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
				+ "from employee_pension_validate where rownum <=to_date('"
				+ toDate
				+ "', 'dd-mon-yyyy') -to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
				+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
				+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG FROM EMPLOYEE_PENSION_VALIDATE  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
				+ fromDate
				+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
				+ toDate
				+ "'))"
				+ " AND ("
				+ pfIDS
				+ ")) emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";

		log.info("FinanceReportDAO::getEmployeePensionCard" + sqlQuery);
		ArrayList OBList = new ArrayList();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				cardInfo = new EmployeePensionCardInfo();
				double total = 0.0;
				if (rs.getString("MONTHYEAR") != null) {
					cardInfo.setMonthyear(CommonUtil.getDatetoString(rs
							.getDate("MONTHYEAR"), "dd-MMM-yyyy"));
					getMonth = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM");
					cardInfo.setShnMnthYear(commonUtil.converDBToAppFormat(
							cardInfo.getMonthyear(), "dd-MMM-yyyy", "MMM-yy"));
					if (getMonth.toUpperCase().equals("APR")) {
						obFlag = false;
						getMonth = "";
						empCumlative = 0.0;
						aaiNetCumlative = 0.0;
						advancePFWPaid = 0.0;
						advPFDrawn = 0.0;
						totalAdvancePFWPaid = 0.0;
					}
					if (getMonth.toUpperCase().equals("MAR")) {
						cardInfo.setCbFlag("Y");
					} else {
						cardInfo.setCbFlag("N");
					}
				} else {
					if (rs.getString("EMPMNTHYEAR") != null) {
						cardInfo.setMonthyear(commonUtil.getDatetoString(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy"));
					} else {
						cardInfo.setMonthyear("---");
					}
					getMonth = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM");
					cardInfo.setShnMnthYear(commonUtil.converDBToAppFormat(
							cardInfo.getMonthyear(), "dd-MMM-yyyy", "MMM-yy"));
					if (getMonth.toUpperCase().equals("APR")) {
						obFlag = false;
						getMonth = "";
						empCumlative = 0.0;
						aaiNetCumlative = 0.0;
						advancePFWPaid = 0.0;
						advPFDrawn = 0.0;
						totalAdvancePFWPaid = 0.0;
					}
					if (getMonth.toUpperCase().equals("MAR")) {
						cardInfo.setCbFlag("Y");
					} else {
						cardInfo.setCbFlag("N");
					}
					if (!cardInfo.getMonthyear().equals("---")) {
						cardInfo.setShnMnthYear(commonUtil.converDBToAppFormat(
								cardInfo.getMonthyear(), "dd-MMM-yyyy",
								"MMM-yy"));
					}

				}
				if (obFlag == false) {
					OBList = this.getOBForPFCardReport(con, cardInfo
							.getMonthyear(), pensionNo);
					cardInfo.setObList(OBList);
					cardInfo.setObFlag("Y");
					obFlag = true;
					getMonth = "";
				} else {
					cardInfo.setObFlag("N");
				}

				if (rs.getString("EMOLUMENTS") != null) {
					cardInfo.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					cardInfo.setEmoluments("0");
				}
				if (rs.getString("EMPPFSTATUARY") != null) {
					cardInfo.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					cardInfo.setEmppfstatury("0");
				}
				if (rs.getString("EMPVPF") != null) {
					cardInfo.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					cardInfo.setEmpvpf("0");
				}
				if (rs.getString("CPF") != null) {
					cardInfo.setEmpCPF(rs.getString("CPF"));
				} else {
					cardInfo.setEmpCPF("0");
				}

				/*
				 * if (region.equals("CHQNAD")) {
				 * 
				 * if (rs.getString("CPFADVANCE") != null) {
				 * cardInfo.setPrincipal(rs.getString("CPFADVANCE")); } else {
				 * cardInfo.setPrincipal("0"); } } else
				 */if (region.equals("North Region")) {
					if (rs.getString("REFADV") != null) {
						cardInfo.setPrincipal(rs.getString("REFADV"));
					} else {
						cardInfo.setPrincipal("0");
					}
				} else {
					if (rs.getString("EMPADVRECPRINCIPAL") != null) {
						cardInfo.setPrincipal(rs
								.getString("EMPADVRECPRINCIPAL"));
					} else {
						cardInfo.setPrincipal("0");
					}
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					cardInfo.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					cardInfo.setInterest("0");
				}
				try {
					checkDate = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM-yyyy");
					flag = false;
				} catch (InvalidDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// log.info(checkDate + "chkMnthYear===" + chkMnthYear);
				if (checkDate.toLowerCase().equals(chkMnthYear.toLowerCase())) {
					flag = true;
				}
				total = new Double(df.format(Double.parseDouble(cardInfo
						.getEmppfstatury().trim())
						+ Double.parseDouble(cardInfo.getEmpvpf().trim())
						+ Double.parseDouble(cardInfo.getPrincipal().trim())
						+ Double.parseDouble(cardInfo.getInterest().trim())))
						.doubleValue();
				if (formFlag == true) {
					/*
					 * if (region.equals("CHQIAD")) {
					 * 
					 * loanPFWPaid = this.getEmployeeLoans(con, cardInfo
					 * .getShnMnthYear(), pfIDS, "ADV.PAID"); advancePFWPaid =
					 * this.getEmployeeAdvances(con, cardInfo .getShnMnthYear(),
					 * pfIDS, "ADV.PAID");
					 * log.info("Region"+region+"loanPFWPaid"
					 * +loanPFWPaid+"advancePFWPaid"+advancePFWPaid);
					 * totalAdvancePFWPaid=loanPFWPaid+advancePFWPaid; } else if
					 * (region.equals("CHQNAD")) { loanPFWPaid =
					 * this.getEmployeeLoans(con, cardInfo .getShnMnthYear(),
					 * pfIDS, "ADV.PAID"); advancePFWPaid =
					 * this.getEmployeeAdvances(con, cardInfo .getShnMnthYear(),
					 * pfIDS, "ADV.PAID");
					 * totalAdvancePFWPaid=loanPFWPaid+advancePFWPaid; } else if
					 * (region.equals("North Region")) {
					 * if(rs.getString("DEDADV")!=null){ advancePFWPaid =
					 * Double.parseDouble(rs.getString("DEDADV"));
					 * totalAdvancePFWPaid=advancePFWPaid; }else{
					 * advancePFWPaid=0; totalAdvancePFWPaid=advancePFWPaid; }
					 *  }
					 */

					loanPFWPaid = commonDAO.getEmployeeLoans(con, cardInfo
							.getShnMnthYear(), pfIDS, "ADV.PAID", pensionNo);
					advancePFWPaid = this.getEmployeeAdvances(con, cardInfo
							.getShnMnthYear(), pfIDS, "ADV.PAID", pensionNo);
					log.info("Region" + region + "loanPFWPaid" + loanPFWPaid
							+ "advancePFWPaid" + advancePFWPaid);
					totalAdvancePFWPaid = loanPFWPaid + advancePFWPaid;
				}

				/*
				 * log.info("cardInfo.getShnMnthYear()" +
				 * cardInfo.getShnMnthYear() + "advancePFWPaid" +
				 * advancePFWPaid);
				 */
				empNet = total - totalAdvancePFWPaid;

				cardInfo.setEmptotal(Double.toString(Math.round(total)));
				cardInfo.setAdvancePFWPaid(Double.toString(Math
						.round(totalAdvancePFWPaid)));
				cardInfo.setEmpNet((Double.toString(Math.round(empNet))));
				empCumlative = empCumlative + empNet;
				cardInfo.setEmpNetCummulative(Double.toString(empCumlative));
				if (rs.getString("AAICONPF") != null) {
					cardInfo.setAaiPF(rs.getString("AAICONPF"));
				} else {
					cardInfo.setAaiPF("0");
				}

				log.info("monthYear" + cardInfo.getMonthyear()
						+ "dateOfRetriment" + dateOfRetriment);
				if (flag == false) {
					if (!cardInfo.getMonthyear().equals("-NA-")
							&& !dateOfRetriment.equals("")) {
						transMntYear = Date.parse(cardInfo.getMonthyear());
						empRetriedDt = Date.parse(dateOfRetriment);

						if (transMntYear > empRetriedDt) {
							contrFlag = true;
						} else if (transMntYear == empRetriedDt) {
							chkDOBFlag = true;
						}
					}
					log.info("transMntYear" + transMntYear + "empRetriedDt"
							+ empRetriedDt);
					log.info("contrFlag" + contrFlag + "chkDOBFlag"
							+ chkDOBFlag);
					if (contrFlag != true) {
						pensionAsPerOption = commonDAO.pensionCalculation(cardInfo
								.getMonthyear(), cardInfo.getEmoluments(),
								wetherOption, region, rs
										.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
							pensionAsPerOption = Math.round(pensionAsPerOption
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth);

						}

					} else {
						pensionAsPerOption = 0;
					}
					cardInfo.setPensionContribution(Double
							.toString(pensionAsPerOption));
				} else {
					cardInfo.setPensionContribution("0");
				}
				log.info("flag" + flag + checkDate + "Pension"
						+ cardInfo.getPensionContribution());
				if (formFlag == true) {
					/*
					 * if (region.equals("CHQIAD")) { advPFDrawn =
					 * this.getEmployeeLoans(con, cardInfo .getShnMnthYear(),
					 * pfIDS, "ADV.DRAWN"); } else if (region.equals("CHQNAD")) {
					 * advPFDrawn = this.getEmployeeLoans(con, cardInfo
					 * .getShnMnthYear(), pfIDS, "ADV.DRAWN"); } else if
					 * (region.equals("North Region")) { advPFDrawn = 0; }
					 */
					advPFDrawn = commonDAO.getEmployeeLoans(con, cardInfo
							.getShnMnthYear(), pfIDS, "ADV.DRAWN", pensionNo);
				}

				aaiPF = (Double.parseDouble(cardInfo.getEmppfstatury()) - pensionAsPerOption);
				cardInfo.setAaiPF(Double.toString(aaiPF));
				cardInfo.setPfDrawn(Double.toString(advPFDrawn));
				aaiNet = Double.parseDouble(cardInfo.getAaiPF()) - advPFDrawn;
				aaiNetCumlative = aaiNetCumlative + aaiNet;
				cardInfo.setAaiNet(Double.toString(aaiNet));
				cardInfo.setAaiCummulative(Double.toString(aaiNetCumlative));
				if (rs.getString("AIRPORTCODE") != null) {
					cardInfo.setStation(rs.getString("AIRPORTCODE"));
				} else {
					cardInfo.setStation("---");
				}
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
	public String calWages(String emoluments, String monthYear,
			String wethereOption, boolean formFlag, boolean secondFormFlag,
			String emolumentMonths) {
		String calEmoluments = "0", sndCalEmoluments = "", day = "", chkTransMnthYear = "", chkDecMnth = "01-dec-1995";
		long transMthYear = 0, transFrstDate = 0, transSndDate = 0, secEndDate = 0, transEndDate = 0, transSendDate = 0, secPenBeginDate = 0,thrPenBeginDate=0;
		boolean signFlag = false;
		DecimalFormat df = new DecimalFormat("#########0");
		day = monthYear.substring(0, 2);
		if (Integer.parseInt(day) >= 20 && Integer.parseInt(day) <= 31) {
			chkTransMnthYear = monthYear;
			monthYear = "";
			try {
				monthYear = "01-"
						+ commonUtil.converDBToAppFormat(chkTransMnthYear,
								"dd-MMM-yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!emoluments.equals("")) {
			if (emoluments.indexOf("-") != -1) {
				signFlag = true;
			} else {
				signFlag = false;
			}
		}
		// log.info(monthYear);
		transMthYear = Date.parse(monthYear);
		transFrstDate = Date.parse("01-Apr-1995");
		transEndDate = Date.parse("30-Nov-1995");
		transSendDate = Date.parse("01-Dec-1995");
		secPenBeginDate = Date.parse("01-Jul-2001");
		secEndDate = Date.parse("31-Mar-2016");
		thrPenBeginDate=Date.parse("01-Sep-2014");
		double cellingRate = 0.0;
		// log.info("calWages=======emoluments"+emoluments+"monthYear"+monthYear+
		// "wethereOption==="+wethereOption);

		// new code added (i.e emolumentMonths as per the issue raised by rahul
		// as on 03-Aug-2010
		if (!emolumentMonths.trim().equals("0.5")
				&& !emolumentMonths.trim().equals("1")
				&& !emolumentMonths.trim().equals("2")
				&& !emolumentMonths.trim().equals("90")
				&& !emolumentMonths.trim().equals("120")
				&& !emolumentMonths.trim().equals("150")
				&& !emolumentMonths.trim().equals("180")) {
			int getDaysBymonth = commonDAO.getNoOfDaysForsalRecovery(monthYear);
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

		if (transMthYear >= transFrstDate && transMthYear <= transEndDate) {

			if (Double.parseDouble(emoluments) > 5000) {
				calEmoluments = "5000";
				calEmoluments = String.valueOf(Integer.parseInt(calEmoluments)
						* Double.parseDouble(emolumentMonths));
			} else {
				calEmoluments = emoluments;
			}
		} else if (monthYear.toLowerCase().equals(chkDecMnth.toLowerCase())) {
			double salary = 0, perDaySal = 0, firstHalfMnthAmt = 0, secHalfMnthAmt = 0;
			String firstCalEmoluments = "";
			if (wethereOption.trim().equals("B")
					|| wethereOption.toLowerCase().trim().equals(
							"NO".toLowerCase())) {
				if (Double.parseDouble(emoluments) >= 5000) {
					sndCalEmoluments = "5000";
				} else {
					sndCalEmoluments = emoluments;
				}
			} else {
				sndCalEmoluments = emoluments;
			}

			if (Double.parseDouble(emoluments) >= 5000) {
				firstCalEmoluments = "5000";
			} else {
				firstCalEmoluments = emoluments;
			}

			if (formFlag == true) {
				calEmoluments = df.format(Double
						.parseDouble(firstCalEmoluments));
			} else if (secondFormFlag == true) {
				calEmoluments = df.format(Double.parseDouble(sndCalEmoluments));
			} else {
				log.info("First Emoluments" + firstCalEmoluments
						+ "Second Emoluments=====" + sndCalEmoluments);
				calEmoluments = df.format(Double
						.parseDouble(firstCalEmoluments)
						+ Double.parseDouble(sndCalEmoluments));
			}
		} else if (transMthYear >= transSendDate && transMthYear <= secEndDate) {

			if (wethereOption.toUpperCase().equals("B")
					|| wethereOption.toUpperCase().equals("NO")) {
				if (transMthYear >= secPenBeginDate) {
					if(transMthYear >thrPenBeginDate){

						if (Double.parseDouble(emoluments) >= 15000
								&& signFlag == false) {
							calEmoluments = "15000";
							calEmoluments = String.valueOf(Integer
									.parseInt(calEmoluments)
									* Double.parseDouble(emolumentMonths.trim()));
						} else if (Double.parseDouble(emoluments) <= -15000
								&& signFlag == true) {
							calEmoluments = "-15000";
						} else if (Double.parseDouble(emoluments) < 15000
								&& Double.parseDouble(emoluments) >= 0
								&& signFlag == false) {
							calEmoluments = emoluments;
						} else if (Double.parseDouble(emoluments) < -15000
								&& Double.parseDouble(emoluments) >= 0
								&& signFlag == true) {
							calEmoluments = emoluments;
						}
						
					}else{
					if (Double.parseDouble(emoluments) >= 6500
							&& signFlag == false) {
						calEmoluments = "6500";
						calEmoluments = String.valueOf(Integer
								.parseInt(calEmoluments)
								* Double.parseDouble(emolumentMonths.trim()));
					} else if (Double.parseDouble(emoluments) <= -6500
							&& signFlag == true) {
						calEmoluments = "-6500";
					} else if (Double.parseDouble(emoluments) < 6500
							&& Double.parseDouble(emoluments) >= 0
							&& signFlag == false) {
						calEmoluments = emoluments;
					} else if (Double.parseDouble(emoluments) < -6500
							&& Double.parseDouble(emoluments) >= 0
							&& signFlag == true) {
						calEmoluments = emoluments;
					}
					}
				} else {
					if (Double.parseDouble(emoluments) < 5000) {
						calEmoluments = emoluments;
					} else if (Double.parseDouble(emoluments) >= 5000) {
						calEmoluments = "5000";
						;
						calEmoluments = String.valueOf(Integer
								.parseInt(calEmoluments)
								* Double.parseDouble(emolumentMonths));

					}

				}
			} else if (wethereOption.toUpperCase().equals("A")) {
				calEmoluments = emoluments;
			}
		}

		// log.info("calWages=======emoluments"+emoluments+"monthYear"+monthYear+
		// "cellingRate"+cellingRate);
		return calEmoluments;
	}
	private ArrayList getOBForPFCardReport(Connection con, String monthYear,
			String pensionNo) {
		ArrayList list = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", obYear = "", obFlag = "", tempMonthYear = "",calsepflag="N",obSepDate="",message="";
		long empNetOB = 0, aaiNetOB = 0, pensionOB = 0, outstandOB = 0,calempnetob=0,calaainetob=0,calempnetintob=0,calaainetintob=0;
		tempMonthYear = "%" + monthYear.substring(2, monthYear.length());
		sqlQuery = "SELECT EMPNETOB,AAINETOB,PENSIONOB,CALEMPNETOB,CALAAINETOB,CALEMPNETOBINTAMT,CALAAINETOBINTAMT,CALINTMNTHS,CALSEPFLAG,to_char(last_day(add_months(OBYEAR,CALINTMNTHS-1)),'dd-Mon-yyyy') as ADJSEPDT,OBFLAG,TO_CHAR(OBYEAR,'yyyy') AS YEAR,OUTSTANDADV FROM EMPLOYEE_PENSION_OB WHERE PENSIONNO="
				+ pensionNo.trim()
				+ " and to_char(OBYEAR,'dd-Mon-yyyy') like '"
				+ tempMonthYear
				+ "' and empflag='Y'";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			log.info("getOBForPFCardReport===================sqlQuery==="
					+ sqlQuery);
			if (rs.next()) {
				empNetOB = rs.getLong("EMPNETOB");
				aaiNetOB = rs.getLong("AAINETOB");
				pensionOB = rs.getLong("PENSIONOB");
				calempnetob=rs.getLong("CALEMPNETOB");
				calaainetob=rs.getLong("CALAAINETOB");
				calempnetintob=rs.getLong("CALEMPNETOBINTAMT");
				calaainetintob=rs.getLong("CALAAINETOBINTAMT");
				if(rs.getString("ADJSEPDT")!=null){
					obSepDate=rs.getString("ADJSEPDT");
				}else{
					obSepDate="---";
				}
				
				if(rs.getString("CALSEPFLAG") != null){
					calsepflag = rs.getString("CALSEPFLAG");
				}else{
					calsepflag="N";
				}
				obFlag = rs.getString("OBFLAG");
				outstandOB = rs.getLong("OUTSTANDADV");
				if (rs.getString("YEAR") != null) {
					obYear = rs.getString("YEAR");
				} else {
					obYear = "---";
				}
			}
			if(!calsepflag.equals("N") && !obSepDate.equals("---")){
				message="EMP NET.OB="+calempnetob+" & AAI NET.OB="+calaainetob+" And Interest Upto "+obSepDate;
			}
			list.add(new Long(empNetOB));
			list.add(new Long(aaiNetOB));
			list.add(new Long(pensionOB));
			list.add(obFlag);
			list.add(obYear);
			list = this
					.getAdjOBForPFCardReport(con, monthYear, pensionNo, list);
			list.add(new Long(outstandOB));
			list.add(new Long(calempnetob));
			list.add(new Long(calaainetob));
			list.add(new Long(calempnetintob));
			list.add(new Long(calaainetintob));
			list.add(calsepflag);
			list.add(message);
			log.info("Size of Opening Balances" + list.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
	}
	private long getEmployeeAdvances(Connection con, String monthyear,
			String pfIDS, String flag, String pensionNo) {
		String sqlQuery = "";
		Statement st = null;
		ResultSet rs = null;
		String status = "";
		long advance = 0, amount = 0, partAmount = 0;
		if (!pfIDS.equals("")) {
			if (flag.equals("ADV.PAID")) {
				sqlQuery = "SELECT SUM(NVL(PARTAMT,'0')) AS PARTAMT,SUM(NVL(AMOUNT,'0')) AS ADVANCE,'WITHOUTPARTAAI' as status FROM EMPLOYEE_PENSION_ADVANCES WHERE ("
						+ pfIDS
						+ ") AND to_char(ADVTRANSDATE,'dd-Mon-yy') like'%-"
						+ monthyear + "'";
			} else {
				sqlQuery = "SELECT AAIPARTAMT AS ADVANCE FROM EMPLOYEE_PENSION_ADVANCES WHERE ("
						+ pfIDS
						+ ") AND to_char(ADVTRANSDATE,'dd-Mon-yy') like'%-"
						+ monthyear + "'";
			}
		} else {
			if (flag.equals("ADV.PAID")) {
				sqlQuery = "SELECT SUM(NVL(PARTAMT,'0')) AS PARTAMT,SUM(NVL(AMOUNT,'0')) AS ADVANCE,'WITHOUTPARTAAI' as status FROM EMPLOYEE_PENSION_ADVANCES WHERE PENSIONNO="
						+ pensionNo
						+ " AND to_char(ADVTRANSDATE,'dd-Mon-yy') like'%-"
						+ monthyear + "'";
			} else {
				sqlQuery = "SELECT AAIPARTAMT AS ADVANCE FROM EMPLOYEE_PENSION_ADVANCES WHERE PENSIONNO="
						+ pensionNo
						+ " AND to_char(ADVTRANSDATE,'dd-Mon-yy') like'%-"
						+ monthyear + "'";
			}
		}

		// log.info("FinanceReportDAO::getEmployeeAdvances" + sqlQuery);
		try {
			// con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				if (rs.getString("status") != null) {
					status = rs.getString("status");
				}
				if (status.equals("WITHOUTPARTAAI")) {
					partAmount = rs.getLong("PARTAMT");
					amount = rs.getLong("ADVANCE");
					advance = partAmount + amount;

				} else {
					advance = rs.getInt("ADVANCE");
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return advance;
	}
	public ArrayList getAdjOBForPFCardReport(Connection con, String monthYear,
			String pensionNo, ArrayList list) {

		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", obYear = "", obFlag = "", tempMonthYear = "", adjFlag = "N", adjNarration = "";
		long cpfTotal = 0, pensionTotal = 0, pfTotal = 0, empsubTotal = 0;
		long cpfInterest = 0, pensionInterest = 0, pfInterest = 0, empsubInterest = 0, priorAdjAmount = 0, adjOutstandAdv = 0;
		long adjcpfTotal = 0, adjPensionTotal = 0, adjPfTotal = 0, adjPensionTotalInt = 0, adjEmpSubTotal = 0, adjPensionContri = 0;
		tempMonthYear = "%" + monthYear.substring(2, monthYear.length());
		sqlQuery = "SELECT CPFTOTAL,CPFINTEREST,PENSIONTOTAL,PENSIONINTEREST,PFTOTAL,PFINTEREST,NVL(EMPSUB,'0') AS EMPSUB,NVL(EMPSUBINTEREST,'0') AS EMPSUBINTEREST,OUTSTANDADV,PRIORADJFLAG,NVL(PENSIONCONTRIADJ,'0.00') AS PENSIONCONTRIADJ,ADJNARRATION  FROM EMPLOYEE_ADJ_OB WHERE PENSIONNO="
				+ pensionNo.trim()
				+ " and to_char(ADJOBYEAR,'dd-Mon-yyyy') like '"
				+ tempMonthYear + "'";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			log.info("getAdjOBForPFCardReport===================sqlQuery==="
					+ sqlQuery);
			if (rs.next()) {
				cpfTotal = rs.getLong("CPFTOTAL");
				pensionTotal = rs.getLong("PENSIONTOTAL");
				pfTotal = rs.getLong("PFTOTAL");
				cpfInterest = rs.getLong("CPFINTEREST");
				pensionInterest = rs.getLong("PENSIONINTEREST");
				pfInterest = rs.getLong("PFINTEREST");
				empsubTotal = rs.getLong("EMPSUB");
				empsubInterest = rs.getLong("EMPSUBINTEREST");
				adjPensionContri = rs.getLong("PENSIONCONTRIADJ");
				adjOutstandAdv = rs.getLong("OUTSTANDADV");
				if (rs.getString("ADJNARRATION") != null) {
					adjNarration = rs.getString("ADJNARRATION");
				} else {
					adjNarration = "---";
				}

				if (rs.getString("PRIORADJFLAG") != null) {
					adjFlag = rs.getString("PRIORADJFLAG");
				}

			}
			adjcpfTotal = cpfTotal + cpfInterest;

			adjPensionTotal = pensionTotal + pensionInterest;
			/*
			 * if(Integer.parseInt(commonUtil.converDBToAppFormat(monthYear,"dd-MMM-yyyy"
			 * ,"yyyy"))==2009 && adjFlag.equals("N")){ adjPensionTotalInt=new
			 * Double(adjPensionTotal8.5/100).longValue(); }
			 * adjPensionTotal=adjPensionTotal+adjPensionTotalInt;
			 */
			adjPfTotal = pfTotal + pfInterest;
			adjEmpSubTotal = empsubTotal + empsubInterest;
			log
					.info("PFCard==========getAdjOBForPFCardReport===================pensionTotal==="
							+ pensionTotal
							+ "pensionInterest"
							+ pensionInterest);
			list.add(new Long(adjcpfTotal));
			list.add(new Long(-adjPensionTotal));
			list.add(new Long(adjPfTotal));
			list.add(new Long(adjEmpSubTotal));
			list.add(new Long(adjPensionContri));
			list.add(new Long(adjOutstandAdv));
			list.add(adjNarration);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
	}
	public ArrayList searchForRevisionOptionPC(String userRegion, String userStation,
			String profileType,  String accountType,
			String employeeNo) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "",approvedStage="";
		ArrayList al = new ArrayList();
		EmpMasterBean empBean = null;
		selectQuery = this.buildSearchQueryForRevisionOptionPC(userRegion, userStation,
				profileType,    accountType, employeeNo);
		log.info("searchFor12MnthStatemntCtrn()==========" + selectQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectQuery);
			while (rs.next()) { 
				empBean = new EmpMasterBean();
				if (rs.getString("PENSIONNO") != null) {
					empBean.setPfid(rs.getString("PENSIONNO"));
				} else {
					empBean.setPfid("0");
				}
				if (rs.getString("CPFACNO") != null) {
					empBean.setCpfAcNo(rs.getString("PENSIONNO"));
				} else {
					empBean.setCpfAcNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empBean.setEmpName(rs.getString("EMPLOYEENAME"));
				} else {
					empBean.setEmpName("0");
				} 
				if (rs.getString("AIRPORTCODE") != null) {
					empBean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					empBean.setStation("");
				} 
				if (rs.getString("REGION") != null) {
					empBean.setRegion(rs.getString("REGION"));
				} else {
					empBean.setRegion("");
				} 
				if (rs.getString("DESEGNATION") != null) {
					empBean.setDesegnation(rs.getString("DESEGNATION"));
				} else {
					empBean.setDesegnation("---");
				} 
				if (rs.getString("DATEOFBIRTH") != null) {
					empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					empBean.setDateofBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFJOINING")));
				} else {
					empBean.setDateofJoining("---");
				} 
				if (rs.getString("WETHEROPTION") != null) {
					empBean.setWetherOption(rs.getString("WETHEROPTION")) ;
				} else{
					empBean.setWetherOption("");
				}
				if (rs.getString("SEPERATIONREASON") != null) {
					empBean.setSeperationReason(rs.getString("SEPERATIONREASON")) ;
				} else{
					empBean.setSeperationReason("");
				} 
				if (rs.getString("SEPERATIONDATE") != null) {
					empBean.setDateofSeperationDate(rs.getString("SEPERATIONDATE")) ;
				} else{
					empBean.setDateofSeperationDate("");
				}
				if (rs.getString("status") != null) {
					empBean.setChkPfidIn78PS(rs.getString("status")) ;
				} else{
					empBean.setChkPfidIn78PS("");
				}
				if (rs.getString("secstatus") != null) {
					empBean.setApprovedStatus(rs.getString("secstatus")) ;
				} else{
					empBean.setApprovedStatus("");
				}
			 al.add(empBean);
			
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
	public ArrayList searchForRevisionOptionPCSecLvl(String userRegion, String userStation,
			String profileType,  String accountType,
			String employeeNo) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "",approvedStage="";
		ArrayList al = new ArrayList();
		EmpMasterBean empBean = null;
		selectQuery = this.buildSearchQueryForRevisionOptionPCSecLvl(userRegion, userStation,
				profileType,    accountType, employeeNo);
		log.info("searchFor12MnthStatemntCtrn()==========" + selectQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectQuery);
			while (rs.next()) { 
				empBean = new EmpMasterBean();
				if (rs.getString("PENSIONNO") != null) {
					empBean.setPfid(rs.getString("PENSIONNO"));
				} else {
					empBean.setPfid("0");
				}
				if (rs.getString("CPFACNO") != null) {
					empBean.setCpfAcNo(rs.getString("PENSIONNO"));
				} else {
					empBean.setCpfAcNo("---");
				}
				if (rs.getString("EMPLOYEENAME") != null) {
					empBean.setEmpName(rs.getString("EMPLOYEENAME"));
				} else {
					empBean.setEmpName("0");
				} 
				if (rs.getString("AIRPORTCODE") != null) {
					empBean.setStation(rs.getString("AIRPORTCODE"));
				} else {
					empBean.setStation("");
				} 
				if (rs.getString("REGION") != null) {
					empBean.setRegion(rs.getString("REGION"));
				} else {
					empBean.setRegion("");
				} 
				if (rs.getString("DESEGNATION") != null) {
					empBean.setDesegnation(rs.getString("DESEGNATION"));
				} else {
					empBean.setDesegnation("---");
				} 
				if (rs.getString("DATEOFBIRTH") != null) {
					empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFBIRTH")));
				} else {
					empBean.setDateofBirth("---");
				}
				if (rs.getString("DATEOFJOINING") != null) {
					empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFJOINING")));
				} else {
					empBean.setDateofJoining("---");
				} 
				if (rs.getString("WETHEROPTION") != null) {
					empBean.setWetherOption(rs.getString("WETHEROPTION")) ;
				} else{
					empBean.setWetherOption("");
				}
				if (rs.getString("SEPERATIONREASON") != null) {
					empBean.setSeperationReason(rs.getString("SEPERATIONREASON")) ;
				} else{
					empBean.setSeperationReason("");
				} 
				if (rs.getString("SEPERATIONDATE") != null) {
					empBean.setDateofSeperationDate(rs.getString("SEPERATIONDATE")) ;
				} else{
					empBean.setDateofSeperationDate("");
				}
				if (rs.getString("status") != null) {
					empBean.setChkPfidIn78PS(rs.getString("status")) ;
				} else{
					empBean.setChkPfidIn78PS("");
				}
				if (rs.getString("status_sec") != null) {
					empBean.setApprovedStatus(rs.getString("status_sec")) ;
				} else{
					empBean.setApprovedStatus("");
				}
				
			 al.add(empBean);
			
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
	public String buildSearchQueryForRevisionOptionPC(String userRegion,
			String userStation, String profileType, 
			String accountType, String employeeNo) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", orderBy = "", sqlQuery = "";

		 
	  
	 	sqlQuery =" SELECT PERSNL.* , TRACK.PENSIONNO AS   CHKPFIDIN78PS  from (SELECT EPI.PENSIONNO AS PENSIONNO, EPI.EMPLOYEENAME AS EMPLOYEENAME,   EPI.CPFACNO AS CPFACNO,"
	 		      +" EPI.REGION AS REGION,EPI.AIRPORTCODE AS AIRPORTCODE, EPI.DESEGNATION AS DESEGNATION,  EPI.DATEOFBIRTH AS DATEOFBIRTH,  EPI.DATEOFJOINING AS DATEOFJOINING,"
	 		      +" EPI.WETHEROPTION AS WETHEROPTION,tr.status as status,tr.status_sec as secstatus,  EPI.DATEOFSEPERATION_REASON AS SEPERATIONREASON,   TO_CHAR(EPI.DATEOFSEPERATION_DATE, 'dd-Mon-yyyy') AS SEPERATIONDATE"
	 		      +" FROM EMPLOYEE_PERSONAL_INFO EPI,employee_freshedit_track tr  WHERE EPI.EMPFLAG = 'Y' and epi.pensionno=tr.pensionno(+)) PERSNL,(SELECT * FROM employee_pension_freshoption where deleteflag='N' and freshpensionoption='B') TRACK   WHERE PERSNL.PENSIONNO =TRACK.PENSIONNO ";
		
	 	log.info("==profileType=="+profileType+"==userRegion=="+userRegion+"=userStation="+userStation+"==");
	 	
	 	if (!(profileType.equals("C") || profileType.equals("S") || profileType
				.equals("A"))) {
	 	
			if (profileType.equals("R")) {
				if (!userRegion.toUpperCase().equals("CHQIAD")) {
					whereClause
							.append(" LOWER(PERSNL.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
									+ userRegion.toLowerCase().trim()+"')");
					whereClause.append(" AND ");
				} else {
					 //For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 05-Oct-2012
					if (!userStation.equals("")) { 
						whereClause	.append(" LOWER(PERSNL.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
								+ userRegion.toLowerCase().trim()+ "' AND ACCOUNTTYPE='RAU')");
						whereClause.append(" AND ");
					}
				}
			} else {
				if (!userStation.equals("")) {
					whereClause.append(" LOWER(PERSNL.AIRPORTCODE) like'%"
							+ userStation.toLowerCase().trim() + "%'");
					whereClause.append(" AND ");
				}
			}
			
			
			if (!userRegion.equals("")) {
				whereClause.append(" LOWER(PERSNL.REGION) like'%"
						+ userRegion.toLowerCase().trim() + "%'");
				whereClause.append(" AND ");
			}
			if (!employeeNo.equals("")) {
				whereClause.append(" PERSNL.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			}
		 
			query.append(sqlQuery);

			if (userStation.equals("") && userRegion.equals("")
					&& employeeNo.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
		} else {
			 
			if (!employeeNo.equals("")) {
				whereClause.append(" PERSNL.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			} 
			query.append(sqlQuery);
			if (employeeNo.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			} 
		}
		 
			orderBy = " ORDER BY PERSNL.PENSIONNO";
		 
		query.append(orderBy);
		dynamicQuery = query.toString();

		return dynamicQuery;

	}
	public String buildSearchQueryForRevisionOptionPCSecLvl(String userRegion,
			String userStation, String profileType, 
			String accountType, String employeeNo) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", orderBy = "", sqlQuery = "";

		 
	  
	 	sqlQuery =" SELECT PERSNL.* , TRACK.PENSIONNO AS   CHKPFIDIN78PS  from (SELECT EPI.PENSIONNO AS PENSIONNO, EPI.EMPLOYEENAME AS EMPLOYEENAME,   EPI.CPFACNO AS CPFACNO,"
	 		      +" EPI.REGION AS REGION,EPI.AIRPORTCODE AS AIRPORTCODE, EPI.DESEGNATION AS DESEGNATION,  EPI.DATEOFBIRTH AS DATEOFBIRTH,  EPI.DATEOFJOINING AS DATEOFJOINING,"
	 		      +" EPI.WETHEROPTION AS WETHEROPTION,tr.status as status,tr.status_sec,  EPI.DATEOFSEPERATION_REASON AS SEPERATIONREASON,   TO_CHAR(EPI.DATEOFSEPERATION_DATE, 'dd-Mon-yyyy') AS SEPERATIONDATE"
	 		      +" FROM EMPLOYEE_PERSONAL_INFO EPI,employee_freshedit_track tr  WHERE EPI.EMPFLAG = 'Y' and epi.pensionno=tr.pensionno and tr.status='Approved') PERSNL,(SELECT * FROM employee_pension_freshoption where deleteflag='N' and freshpensionoption='B') TRACK   WHERE PERSNL.PENSIONNO =TRACK.PENSIONNO ";
		
	 	log.info("==profileType=="+profileType+"==userRegion=="+userRegion+"=userStation="+userStation+"==");
	 	
	 	if (!(profileType.equals("C") || profileType.equals("S") || profileType
				.equals("A"))) {
	 	
			if (profileType.equals("R")) {
				if (!userRegion.toUpperCase().equals("CHQIAD")) {
					whereClause
							.append(" LOWER(PERSNL.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
									+ userRegion.toLowerCase().trim()+"')");
					whereClause.append(" AND ");
				} else {
					 //For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 05-Oct-2012
					if (!userStation.equals("")) { 
						whereClause	.append(" LOWER(PERSNL.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
								+ userRegion.toLowerCase().trim()+ "' AND ACCOUNTTYPE='RAU')");
						whereClause.append(" AND ");
					}
				}
			} else {
				if (!userStation.equals("")) {
					whereClause.append(" LOWER(PERSNL.AIRPORTCODE) like'%"
							+ userStation.toLowerCase().trim() + "%'");
					whereClause.append(" AND ");
				}
			}
			
			
			if (!userRegion.equals("")) {
				whereClause.append(" LOWER(PERSNL.REGION) like'%"
						+ userRegion.toLowerCase().trim() + "%'");
				whereClause.append(" AND ");
			}
			if (!employeeNo.equals("")) {
				whereClause.append(" PERSNL.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			}
		 
			query.append(sqlQuery);

			if (userStation.equals("") && userRegion.equals("")
					&& employeeNo.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
		} else {
			 
			if (!employeeNo.equals("")) {
				whereClause.append(" PERSNL.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			} 
			query.append(sqlQuery);
			if (employeeNo.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			} 
		}
		 
			orderBy = " ORDER BY PERSNL.PENSIONNO";
		 
		query.append(orderBy);
		dynamicQuery = query.toString();

		return dynamicQuery;

	}
	public int insertEmployeeTransData(String pfId, String frmName,
			String username, String ipaddress, String flag, String mappingFlag,
			String cpfacno, String region, String upflag) {
		log.info("AdjCrtnDAO :insertEmployeeTransData() Entering Method ");
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
		String [] years={"1995-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013"};
		int result = 0, loansresult = 0, advancesresult = 0, transID = 0, batchId = 0,insertDummyRecordResult=0;
		String table2="";
		monthYear = commonUtil.getCurrentDate("dd-MMM-yyyy");
		EmpMasterBean empBean = new EmpMasterBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			st1 = con.createStatement();
			//this.chkDOJ(con, pfId);
			cpfregionstrip = this.getEmployeeCpfacno(con, pfId);
			String[] pfIDLists = cpfregionstrip.split("=");
			preparedString = commonDAO.preparedCPFString(pfIDLists);
			
			table2=this.chkPfidStatusInAdjCrtn(pfId);
			if(table2.equals("Approved")){
				table2="epis_adj_crtn";
			}else{
				table2="employee_pension_validate";
			}
			log.info("preparedString====================" + preparedString);

		
				tableName = "employee_freshop_crtn";
			
			// chkpfid = "select * from "+tableName+" where pensionno=" + pfId;
			// rs = st.executeQuery(chkpfid);
			chkpfid = this.chkPfidinAdjCrtn(pfId, frmName); 
			if (chkpfid.equals("NotExists")) {
				/* if(dataavailbf2008==false){ */
				sql = "insert into "
						+ tableName
						+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
						+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
						+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS)   (select  "
						+ pfId
						+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
						+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
						+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP,OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
						+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS   from  "+table2+"  where empflag='Y' and (pensionno="
						+ pfId + " or " + preparedString
						+ ") and monthyear <='31-Mar-2015')";
				
				sqlAftr2008 =  "insert into "
					+ tableName
					+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
					+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
					+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS)   (select  "
					+ pfId
					+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
					+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
					+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
					+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS   from employee_pension_validate where empflag='Y' and   pensionno="
					+ pfId +" and monthyear between '01-Apr-2008' and  '"+monthYear + "')";
				/*
				 * }else{ sql = "insert into "+tableName+" (PENSIONNO ,CPFACCNO
				 * ,EMPLOYEENAME, EMPLOYEENO,AIRPORTCODE ,REGION ,MONTHYEAR
				 * ,EMOLUMENTS ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,
				 * EMPADVRECINTEREST , " + " EMPFLAG ,LASTACTIVEDATE ) (select
				 * "+pfId+" ,'"+dojcpfaccno+"' ,'"+dojempname+"',
				 * '"+dojemployeeno+"' ,'"+dojstation+"' ,'"+dojregion+"' ,
				 * MONTHYEAR ,0.00, 0.00 , 0.00 , 0.00 ,0.00 , EMPFLAG , sysdate
				 * from employee_pension_validate where pensionno=51 and
				 * empflag='Y' and monthyear<='31-Mar-2008')"; log.info("sql " +
				 * sql); result = st.executeUpdate(sql); st=null;
				 * st=con.createStatement(); sql = "insert into "+tableName+"
				 * (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION
				 * ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS ,EMPPFSTATUARY ,
				 * EMPVPF , EMPADVRECPRINCIPAL , EMPADVRECINTEREST , " + "
				 * Advance, PFWSUB , PFWCONTRI , PF, PENSIONCONTRI , EMPFLAG ,
				 * EDITTRANS ,FORM7NARRATION , PCHELDAMT
				 * ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG
				 * ,ARREARAMOUNT , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG," + "
				 * ARREARSBREAKUP, EDITEDDATE , OPCHANGEPF ,
				 * OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG ,
				 * REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR
				 * ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS)
				 * (select PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ," + "
				 * DESEGNATION ,AIRPORTCODE ,REGION , MONTHYEAR ,EMOLUMENTS
				 * ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL
				 * ,EMPADVRECINTEREST , 0.00, 0.00 , 0.00 , PF, PENSIONCONTRI ,
				 * EMPFLAG , EDITTRANS , FORM7NARRATION , PCHELDAMT
				 * ,EMOLUMENTMONTHS, PCCALCAPPLIED ," + " ARREARFLAG ,
				 * LATESTMNTHFLAG , ARREARAMOUNT , DEPUTATIONFLAG, DUEEMOLUMENTS ,
				 * MERGEFLAG, ARREARSBREAKUP, EDITEDDATE , OPCHANGEPF ,
				 * OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG ,
				 * REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG ," + " FINYEAR
				 * ,ACC_KEYNO ,USERNAME , IPADDRESS , '' , REMARKS from
				 * employee_pension_validate where pensionno=" + pfId + "
				 * "+condition+" and monthyear >='01-Apr-2008')"; }
				 */

				// for Uploding Mapping Data
				// for Uploding Mapping Data
				/*if (mappingFlag.equals("U") && upflag.equals("N")) {

					sql = "insert into "
							+ tableName
							+ " (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
							+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,empvpf,advance,pfwsub,pfwcontri)   (select  "
							+ pfId
							+ " ,"
							+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
							+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , 'Through upload screen',empvpf,advance,pfwsub,pfwcontri   from EPIS_ADJ_CRTN_md_bk where empflag='Y' and datatype='N' and PENSIONNO='"
							+ pfId + "'and monthyear <='01-Mar-2008')";

				}
				// In where Condition == datatype='U'== is removed for loading
				// all the data
				if (mappingFlag.equals("U") && upflag.equals("U")) {

					sql = "insert into "
							+ tableName
							+ " (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
							+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,empvpf,advance,pfwsub,pfwcontri)   (select  "
							+ pfId
							+ " ,"
							+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
							+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , 'Through upload screen',empvpf,advance,pfwsub,pfwcontri   from EPIS_ADJ_CRTN_md_bk where empflag='Y'  and PENSIONNO='"
							+ pfId + "'and monthyear <='01-Mar-2008')";
				
				}*/
				log.info("----------sql-------------" + sql);
				log.info("----------sqlAftr2008-------------" + sqlAftr2008);
				st.addBatch(sql);
				//st.addBatch(sqlAftr2008);
				if (frmName.equals("adjcorrections")) {
				for(int i=0;i<years.length;i++){
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"
							+ pfId + ",'"+years[i]+"',sysdate)";
					log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
					//st.addBatch(episAdjCrtnLog);
					episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"
						+ username + "','" + ipaddress + "',sysdate)";
					log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
					//st.addBatch(episAdjCrtnLogDtl);	 
				}
				
				revisedFlagQry = " update "
					+ tableName
					+ "    set  reviseepfemolumentsflag='N' where  pensionno="
					+ pfId + " and empflag='Y'"; 
			
				log.info("----------revisedFlagQry-------------"
					+ revisedFlagQry);
				//st.addBatch(revisedFlagQry); 
				}
				
				int insertCount[] = st.executeBatch();
				log.info("insertCount  " + insertCount.length);
				st = null;
				st = con.createStatement();
				loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
						+ pfId;
				log.info("----------loansQuery-------------" + loansQuery);
				//rs1 = st.executeQuery(loansQuery);
			/*	while (rs1.next()) {
					if (rs1.getString("loandate") != null) {
						loandate = rs1.getString("loandate");
					} else {
						loandate = "";
					}
					if (rs1.getString("subamt") != null) {
						subamt = rs1.getString("subamt");
					} else {
						subamt = "0.00";
					}
					if (rs1.getString("contamt") != null) {
						contamt = rs1.getString("contamt");
					} else {
						contamt = "0.00";
					}
					st = con.createStatement();
					updateloans = "update  " + tableName + "   set  pfwsub="
							+ subamt + ", pfwcontri=" + contamt
							+ " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ loandate + "'";
					
				
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy')='"+ loandate + "'";
					log.info("chkForRecord-----"+ chkForRecord);
					rs5 = st.executeQuery(chkForRecord);
					if(rs5.next()){
						//loansresult = st.executeUpdate(updateloans);
					}else{
						insertDummyRecord= " insert into employee_freshop_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'))";       
						 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
						 loansresult = st.executeUpdate(updateloans);
					}
					log	.info("----------updateloans-------------"	+ updateloans);
					
				}*/
				advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
						+ pfId;
				log
						.info("----------advancesQuery-------------"
								+ advancesQuery);
				st = con.createStatement();
				//rs2 = st.executeQuery(advancesQuery);
			/*	while (rs2.next()) {
					if (rs2.getString("advtransdate") != null) {
						advtransdate = rs2.getString("advtransdate");
					} else {
						advtransdate = "";
					}
					if (rs2.getString("advanceAmt") != null) {
						advanceAmt = rs2.getString("advanceAmt");
					} else {
						advanceAmt = "0.00";
					}

					st = con.createStatement();
					updateadvances = "update  " + tableName
							+ "   set    advance =" + advanceAmt
							+ " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ advtransdate + "'";
					
					 
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and   to_char(monthyear,'MON-yyyy')='"+ advtransdate + "'";
					rs5 = st.executeQuery(chkForRecord);
					log.info("chkForRecord-----"+ chkForRecord);
					if(rs5.next()){
						//advancesresult = st.executeUpdate(updateadvances);
						 
					}else{
						insertDummyRecord= " insert into employee_freshop_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'))";       
						insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
						advancesresult = st.executeUpdate(updateadvances);
					}
					log.info("----------updateadvances-------------"+ updateadvances);
				}*/
				//No need to inserting into seperate table as we remove edit  facility to Opening Balances
				/*if (frmName.equals("adjcorrections")) {
					String obQuery = "insert into EMPLOYEE_PENSION_OB_ADJ_CRTN (select * from  EMPLOYEE_PENSION_OB where pensionno="
							+ pfId + " and  OBYEAR <='01-Apr-2010')";
					log.info("Opening Balance Fetching Query  " + obQuery);

					result = st.executeUpdate(obQuery);
				}*/
			} else {
				for(int i=0;i<years.length;i++){
				String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
						+ pfId+ " and adjobyear='"+years[i]+"'";
				int logid = 0;
				log.info("loggeridseq " + loggeridseq);
				//rs3 = st.executeQuery(loggeridseq);
				if (rs3.next()) {
					//logid = Integer.parseInt(rs3.getString("loggerid"));
					log.info("logid  test" + logid);
				} else {
					st = null;
					st = con.createStatement();
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
							+ pfId
							+ ",'"+years[i]+"',sysdate,'This pfid already ported before implmenation logic')";
					//st.executeUpdate(episAdjCrtnLog);
					st = null;
					st = con.createStatement();
					rs3 = st.executeQuery(loggeridseq);
					if (rs3.next())
						logid = Integer.parseInt(rs3.getString("loggerid"));
					st = null;
				}
				if (flag.equals("S")) {
					episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values ("
							+ logid
							+ ",'"
							+ username
							+ "','"
							+ ipaddress
							+ "',sysdate)";
					st = con.createStatement();
					log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
					//result = st.executeUpdate(episAdjCrtnLogDtl);
				}
				log.info("count :" + result);
				log.info("--------Data already exists----------");
			}
			}
			// Mapping Data Tracking
			if (mappingFlag.equals("M")) {
				String mappingPreparedString = "(CPFACCNO='" + cpfacno
						+ "' AND REGION='" + region + "')";
			//	batchId = this.getBatchId(con);
				String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TEMP  where pensionno='"
						+ pfId + "' and ADJOBYEAR ='1995-2008'";
				//rs4 = st.executeQuery(transIDQry);
				if (rs4.next()) {
					//transID = Integer.parseInt(rs4.getString("TRANSID"));
				}

				sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_log (PENSIONNO ,CPFACNO,MONTHYEAR ,NEWEMOLUMENTS  ,NEWEMPPFSTATUARY , NEWEMPVPF , "
						+ "  UPDATEDDATE  , REMARKS ,REGION , USERNAME ,COMPUTERNAME,BATCHID ,TRANSID)   (select  "
						+ pfId
						+ " ,CPFACCNO ,MONTHYEAR,"
						+ " EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF ,sysdate , 'Through Mapping Screen',REGION ,USERNAME,IPADDRESS,'"
						+ batchId
						+ "','"
						+ transID
						+ "'"
						+ "  from employee_pension_validate where empflag='Y' and ("
						+ mappingPreparedString
						+ ") and monthyear <='01-Mar-2008')";

				log.info("sqlForMapping1995to2008" + sqlForMapping1995to2008);
				//result = st.executeUpdate(sqlForMapping1995to2008);
				log.info("result" + result);
			}
			if (mappingFlag.equals("U")) {
				// String mappingPreparedString="(CPFACCNO='"+cpfacno+"' AND
				// REGION='"+region+"')";
				//batchId = this.getBatchId(con);
				String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TEMP  where pensionno='"
						+ pfId + "' and ADJOBYEAR ='1995-2008'";
				rs4 = st.executeQuery(transIDQry);
				if (rs4.next()) {
					transID = Integer.parseInt(rs4.getString("TRANSID"));
				}

				sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_log (PENSIONNO ,MONTHYEAR ,NEWEMOLUMENTS ,NEWEMPPFSTATUARY, NEWPRINCIPLE ,  NEWINTEREST, "
						+ "  UPDATEDDATE ,REMARKS, USERNAME ,COMPUTERNAME,BATCHID ,TRANSID)   (select  "
						+ pfId
						+ " ,"
						+ "  MONTHYEAR ,EMOLUMENTS,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,sysdate , 'Through Upload Screen',USERNAME,IPADDRESS,'"
						+ batchId
						+ "','"
						+ transID
						+ "'"
						+ "  from EPIS_ADJ_CRTN_MD_BK where empflag='Y' and datatype='U' and PENSIONNO="
						+ pfId + " and monthyear <='01-Mar-2008')";

				log.info("sqlForMapping1995to2008" + sqlForMapping1995to2008);
				//result = st.executeUpdate(sqlForMapping1995to2008);
				log.info("resultForModified" + result);
				// String emolumentLog="insert into
				// epis_adj_crtn_emoluments_log(pensionno,cpfacno,monthyear,oldemoluments,oldemppfstatuary,oldempvpf,oldprinciple,oldinterest,
				// originalrecord) select
				// PENSIONNO,CPFACCNO,MONTHYEAR,0,0,0,0,0,'Y' from
				// epis_adj_crtn_md_bk where pensionno='"+pfId+"' and
				// DATATYPE='U'";
				String emolumentLog = "update epis_adj_crtn set DATAMODIFIEDFLAG='Y' where pensionno='"
						+ pfId + "'";
				result = st1.executeUpdate(emolumentLog);
				log.info("resultOriginal" + result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
			log.info("error" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("AdjCrtnDAO :insertEmployeeTransData() Leaving Method ");
		return result;
	}
	public int insertEmployeeTransDataSecLvl(String pfId, String frmName,
			String username, String ipaddress, String flag, String mappingFlag,
			String cpfacno, String region, String upflag) {
		log.info("AdjCrtnDAO :insertEmployeeTransData() Entering Method ");
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
		String [] years={"1995-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013"};
		int result = 0, loansresult = 0, advancesresult = 0, transID = 0, batchId = 0,insertDummyRecordResult=0;
		monthYear = commonUtil.getCurrentDate("dd-MMM-yyyy");
		EmpMasterBean empBean = new EmpMasterBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			st1 = con.createStatement();
			//this.chkDOJ(con, pfId);
			cpfregionstrip = this.getEmployeeCpfacno(con, pfId);
			String[] pfIDLists = cpfregionstrip.split("=");
			preparedString = commonDAO.preparedCPFString(pfIDLists);
			log.info("preparedString====================" + preparedString);

		
				tableName = "employee_freshop_crtn_b";
			
			// chkpfid = "select * from "+tableName+" where pensionno=" + pfId;
			// rs = st.executeQuery(chkpfid);
			chkpfid = this.chkPfidinAdjCrtnSecLvl(pfId, frmName); 
			if (chkpfid.equals("NotExists")) {
				/* if(dataavailbf2008==false){ */
				sql = "insert into "
						+ tableName
						+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
						+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
						+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS,EMOLUMENTS_B,PENSIONCONTRI_B,FRESHOPFLAG)   (select  "
						+ pfId
						+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
						+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
						+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP,OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
						+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS,EMOLUMENTS_B,PENSIONCONTRI_B,FRESHOPFLAG   from employee_freshop_crtn where empflag='Y' and (pensionno="
						+ pfId + " or " + preparedString
						+ ") and monthyear <='31-Mar-2015')";
				
				sqlAftr2008 =  "insert into "
					+ tableName
					+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
					+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
					+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS)   (select  "
					+ pfId
					+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
					+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
					+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
					+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS   from employee_freshop_crtn where empflag='Y' and   pensionno="
					+ pfId +" and monthyear between '01-Apr-2008' and  '"+monthYear + "')";
				/*
				 * }else{ sql = "insert into "+tableName+" (PENSIONNO ,CPFACCNO
				 * ,EMPLOYEENAME, EMPLOYEENO,AIRPORTCODE ,REGION ,MONTHYEAR
				 * ,EMOLUMENTS ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,
				 * EMPADVRECINTEREST , " + " EMPFLAG ,LASTACTIVEDATE ) (select
				 * "+pfId+" ,'"+dojcpfaccno+"' ,'"+dojempname+"',
				 * '"+dojemployeeno+"' ,'"+dojstation+"' ,'"+dojregion+"' ,
				 * MONTHYEAR ,0.00, 0.00 , 0.00 , 0.00 ,0.00 , EMPFLAG , sysdate
				 * from employee_pension_validate where pensionno=51 and
				 * empflag='Y' and monthyear<='31-Mar-2008')"; log.info("sql " +
				 * sql); result = st.executeUpdate(sql); st=null;
				 * st=con.createStatement(); sql = "insert into "+tableName+"
				 * (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION
				 * ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS ,EMPPFSTATUARY ,
				 * EMPVPF , EMPADVRECPRINCIPAL , EMPADVRECINTEREST , " + "
				 * Advance, PFWSUB , PFWCONTRI , PF, PENSIONCONTRI , EMPFLAG ,
				 * EDITTRANS ,FORM7NARRATION , PCHELDAMT
				 * ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG
				 * ,ARREARAMOUNT , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG," + "
				 * ARREARSBREAKUP, EDITEDDATE , OPCHANGEPF ,
				 * OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG ,
				 * REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR
				 * ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS)
				 * (select PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ," + "
				 * DESEGNATION ,AIRPORTCODE ,REGION , MONTHYEAR ,EMOLUMENTS
				 * ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL
				 * ,EMPADVRECINTEREST , 0.00, 0.00 , 0.00 , PF, PENSIONCONTRI ,
				 * EMPFLAG , EDITTRANS , FORM7NARRATION , PCHELDAMT
				 * ,EMOLUMENTMONTHS, PCCALCAPPLIED ," + " ARREARFLAG ,
				 * LATESTMNTHFLAG , ARREARAMOUNT , DEPUTATIONFLAG, DUEEMOLUMENTS ,
				 * MERGEFLAG, ARREARSBREAKUP, EDITEDDATE , OPCHANGEPF ,
				 * OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG ,
				 * REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG ," + " FINYEAR
				 * ,ACC_KEYNO ,USERNAME , IPADDRESS , '' , REMARKS from
				 * employee_pension_validate where pensionno=" + pfId + "
				 * "+condition+" and monthyear >='01-Apr-2008')"; }
				 */

				// for Uploding Mapping Data
				// for Uploding Mapping Data
				/*if (mappingFlag.equals("U") && upflag.equals("N")) {

					sql = "insert into "
							+ tableName
							+ " (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
							+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,empvpf,advance,pfwsub,pfwcontri)   (select  "
							+ pfId
							+ " ,"
							+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
							+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , 'Through upload screen',empvpf,advance,pfwsub,pfwcontri   from EPIS_ADJ_CRTN_md_bk where empflag='Y' and datatype='N' and PENSIONNO='"
							+ pfId + "'and monthyear <='01-Mar-2008')";

				}
				// In where Condition == datatype='U'== is removed for loading
				// all the data
				if (mappingFlag.equals("U") && upflag.equals("U")) {

					sql = "insert into "
							+ tableName
							+ " (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
							+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,empvpf,advance,pfwsub,pfwcontri)   (select  "
							+ pfId
							+ " ,"
							+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
							+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , 'Through upload screen',empvpf,advance,pfwsub,pfwcontri   from EPIS_ADJ_CRTN_md_bk where empflag='Y'  and PENSIONNO='"
							+ pfId + "'and monthyear <='01-Mar-2008')";
				
				}*/
				log.info("----------sql-------------" + sql);
				log.info("----------sqlAftr2008-------------" + sqlAftr2008);
				st.addBatch(sql);
				//st.addBatch(sqlAftr2008);
				if (frmName.equals("adjcorrections")) {
				for(int i=0;i<years.length;i++){
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"
							+ pfId + ",'"+years[i]+"',sysdate)";
					log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
					//st.addBatch(episAdjCrtnLog);
					episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"
						+ username + "','" + ipaddress + "',sysdate)";
					log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
					//st.addBatch(episAdjCrtnLogDtl);	 
				}
				
				revisedFlagQry = " update "
					+ tableName
					+ "    set  reviseepfemolumentsflag='N' where  pensionno="
					+ pfId + " and empflag='Y'"; 
			
				log.info("----------revisedFlagQry-------------"
					+ revisedFlagQry);
				//st.addBatch(revisedFlagQry); 
				}
				
				int insertCount[] = st.executeBatch();
				log.info("insertCount  " + insertCount.length);
				st = null;
				st = con.createStatement();
				loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
						+ pfId;
				log.info("----------loansQuery-------------" + loansQuery);
				//rs1 = st.executeQuery(loansQuery);
			/*	while (rs1.next()) {
					if (rs1.getString("loandate") != null) {
						loandate = rs1.getString("loandate");
					} else {
						loandate = "";
					}
					if (rs1.getString("subamt") != null) {
						subamt = rs1.getString("subamt");
					} else {
						subamt = "0.00";
					}
					if (rs1.getString("contamt") != null) {
						contamt = rs1.getString("contamt");
					} else {
						contamt = "0.00";
					}
					st = con.createStatement();
					updateloans = "update  " + tableName + "   set  pfwsub="
							+ subamt + ", pfwcontri=" + contamt
							+ " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ loandate + "'";
					
				
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy')='"+ loandate + "'";
					log.info("chkForRecord-----"+ chkForRecord);
					rs5 = st.executeQuery(chkForRecord);
					if(rs5.next()){
						//loansresult = st.executeUpdate(updateloans);
					}else{
						insertDummyRecord= " insert into employee_freshop_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'))";       
						 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
						 loansresult = st.executeUpdate(updateloans);
					}
					log	.info("----------updateloans-------------"	+ updateloans);
					
				}*/
				advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
						+ pfId;
				log
						.info("----------advancesQuery-------------"
								+ advancesQuery);
				st = con.createStatement();
				//rs2 = st.executeQuery(advancesQuery);
			/*	while (rs2.next()) {
					if (rs2.getString("advtransdate") != null) {
						advtransdate = rs2.getString("advtransdate");
					} else {
						advtransdate = "";
					}
					if (rs2.getString("advanceAmt") != null) {
						advanceAmt = rs2.getString("advanceAmt");
					} else {
						advanceAmt = "0.00";
					}

					st = con.createStatement();
					updateadvances = "update  " + tableName
							+ "   set    advance =" + advanceAmt
							+ " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ advtransdate + "'";
					
					 
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and   to_char(monthyear,'MON-yyyy')='"+ advtransdate + "'";
					rs5 = st.executeQuery(chkForRecord);
					log.info("chkForRecord-----"+ chkForRecord);
					if(rs5.next()){
						//advancesresult = st.executeUpdate(updateadvances);
						 
					}else{
						insertDummyRecord= " insert into employee_freshop_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'))";       
						insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
						advancesresult = st.executeUpdate(updateadvances);
					}
					log.info("----------updateadvances-------------"+ updateadvances);
				}*/
				//No need to inserting into seperate table as we remove edit  facility to Opening Balances
				/*if (frmName.equals("adjcorrections")) {
					String obQuery = "insert into EMPLOYEE_PENSION_OB_ADJ_CRTN (select * from  EMPLOYEE_PENSION_OB where pensionno="
							+ pfId + " and  OBYEAR <='01-Apr-2010')";
					log.info("Opening Balance Fetching Query  " + obQuery);

					result = st.executeUpdate(obQuery);
				}*/
			} else {
				for(int i=0;i<years.length;i++){
				String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
						+ pfId+ " and adjobyear='"+years[i]+"'";
				int logid = 0;
				log.info("loggeridseq " + loggeridseq);
				//rs3 = st.executeQuery(loggeridseq);
				if (rs3.next()) {
					//logid = Integer.parseInt(rs3.getString("loggerid"));
					log.info("logid  test" + logid);
				} else {
					st = null;
					st = con.createStatement();
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
							+ pfId
							+ ",'"+years[i]+"',sysdate,'This pfid already ported before implmenation logic')";
					//st.executeUpdate(episAdjCrtnLog);
					st = null;
					st = con.createStatement();
					rs3 = st.executeQuery(loggeridseq);
					if (rs3.next())
						logid = Integer.parseInt(rs3.getString("loggerid"));
					st = null;
				}
				if (flag.equals("S")) {
					episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values ("
							+ logid
							+ ",'"
							+ username
							+ "','"
							+ ipaddress
							+ "',sysdate)";
					st = con.createStatement();
					log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
					//result = st.executeUpdate(episAdjCrtnLogDtl);
				}
				log.info("count :" + result);
				log.info("--------Data already exists----------");
			}
			}
			// Mapping Data Tracking
			if (mappingFlag.equals("M")) {
				String mappingPreparedString = "(CPFACCNO='" + cpfacno
						+ "' AND REGION='" + region + "')";
			//	batchId = this.getBatchId(con);
				String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TEMP  where pensionno='"
						+ pfId + "' and ADJOBYEAR ='1995-2008'";
				//rs4 = st.executeQuery(transIDQry);
				if (rs4.next()) {
					//transID = Integer.parseInt(rs4.getString("TRANSID"));
				}

				sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_log (PENSIONNO ,CPFACNO,MONTHYEAR ,NEWEMOLUMENTS  ,NEWEMPPFSTATUARY , NEWEMPVPF , "
						+ "  UPDATEDDATE  , REMARKS ,REGION , USERNAME ,COMPUTERNAME,BATCHID ,TRANSID)   (select  "
						+ pfId
						+ " ,CPFACCNO ,MONTHYEAR,"
						+ " EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF ,sysdate , 'Through Mapping Screen',REGION ,USERNAME,IPADDRESS,'"
						+ batchId
						+ "','"
						+ transID
						+ "'"
						+ "  from employee_pension_validate where empflag='Y' and ("
						+ mappingPreparedString
						+ ") and monthyear <='01-Mar-2008')";

				log.info("sqlForMapping1995to2008" + sqlForMapping1995to2008);
				//result = st.executeUpdate(sqlForMapping1995to2008);
				log.info("result" + result);
			}
			if (mappingFlag.equals("U")) {
				// String mappingPreparedString="(CPFACCNO='"+cpfacno+"' AND
				// REGION='"+region+"')";
				//batchId = this.getBatchId(con);
				String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TEMP  where pensionno='"
						+ pfId + "' and ADJOBYEAR ='1995-2008'";
				rs4 = st.executeQuery(transIDQry);
				if (rs4.next()) {
					transID = Integer.parseInt(rs4.getString("TRANSID"));
				}

				sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_log (PENSIONNO ,MONTHYEAR ,NEWEMOLUMENTS ,NEWEMPPFSTATUARY, NEWPRINCIPLE ,  NEWINTEREST, "
						+ "  UPDATEDDATE ,REMARKS, USERNAME ,COMPUTERNAME,BATCHID ,TRANSID)   (select  "
						+ pfId
						+ " ,"
						+ "  MONTHYEAR ,EMOLUMENTS,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,sysdate , 'Through Upload Screen',USERNAME,IPADDRESS,'"
						+ batchId
						+ "','"
						+ transID
						+ "'"
						+ "  from EPIS_ADJ_CRTN_MD_BK where empflag='Y' and datatype='U' and PENSIONNO="
						+ pfId + " and monthyear <='01-Mar-2008')";

				log.info("sqlForMapping1995to2008" + sqlForMapping1995to2008);
				//result = st.executeUpdate(sqlForMapping1995to2008);
				log.info("resultForModified" + result);
				// String emolumentLog="insert into
				// epis_adj_crtn_emoluments_log(pensionno,cpfacno,monthyear,oldemoluments,oldemppfstatuary,oldempvpf,oldprinciple,oldinterest,
				// originalrecord) select
				// PENSIONNO,CPFACCNO,MONTHYEAR,0,0,0,0,0,'Y' from
				// epis_adj_crtn_md_bk where pensionno='"+pfId+"' and
				// DATATYPE='U'";
				String emolumentLog = "update epis_adj_crtn set DATAMODIFIEDFLAG='Y' where pensionno='"
						+ pfId + "'";
				result = st1.executeUpdate(emolumentLog);
				log.info("resultOriginal" + result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
			log.info("error" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("AdjCrtnDAO :insertEmployeeTransData() Leaving Method ");
		return result;
	}
	public String chkPfidinAdjCrtn(String pfid, String frmName) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			tableName = "employee_freshop_crtn";
		
		sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid;
		log.info("--chkPfidinAdjCrth()---" + sqlQuery);
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
	public String chkPfidinAdjCrtnSecLvl(String pfid, String frmName) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			tableName = "employee_freshop_crtn_b";
		
		sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid;
		log.info("--chkPfidinAdjCrth()---" + sqlQuery);
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
	private String chkDOJ(Connection con, String pfid) {
		String sqlQuery = "", insquery = "", cpfacnos = "", regions = "", cpfregionstrip = "";
		boolean flag = false;
		ResultSet rs = null;
		Statement st = null;
		String cpfaccno, employeename = "", region, airportcode, employeeno, commonstring = "";
		sqlQuery = "select '1' from EPIS_INFO_ADJ_CRTN where dateofjoining<='31-Mar-2008' and empserialnumber='"
				+ pfid + "'";
		log.info("--chkDOJ()---" + sqlQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (!rs.next()) {
				rs = null;
				st = null;
				insquery = "insert into EPIS_INFO_ADJ_CRTN(cpfacno ,employeename,dateofbirth,dateofjoining,wetheroption,region,FHNAME,SEX,DEPARTMENT,empserialnumber)select (CASE    WHEN (cpfacno IS NULL or cpfacno like 'N%' or cpfacno = '00' or    cpfacno like '-%') THEN     'NCPF-' || pensionno   ELSE   cpfacno  END) AS cpfacno,employeename,dateofbirth,dateofjoining,wetheroption,region,FHNAME,GENDER,DEPARTMENT,pensionno from employee_personal_info where pensionno="
						+ pfid;
				log.info("-insquery----" + insquery);
				st = con.createStatement();
				st.executeUpdate(insquery);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return commonstring;
	}
	public String getEmployeeCpfacno(Connection con, String pfid) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfID = "", region = "", regionPFIDS = "";
		try {

			st = con.createStatement();
			sqlQuery = "SELECT CPFACNO,REGION FROM employee_info WHERE EMPSERIALNUMBER='"
					+ pfid + "'";
			log.info("FinanceReportDAO::getEmployeeMappingPFInfo" + sqlQuery);
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
	public ArrayList editTransactionDataForAdjCrtn(String cpfAccno,
			String monthyear, String emoluments, String epf, String empvpf,
			String principle, String interest, String advance, String loan,
			String aailoan, String contri, String noofmonths, String pfid,
			String region, String airportcode, String username,
			String computername, String form7narration, String duputationflag,
			String pensionoption, String empnetob, String aainetob,
			String empnetobFlag, String finYear, String editTransFlag,String dateOfBirth) {

		String emppfstatuary = "0", oldemppfstatuary = "0.00", pf = "0.00", adjObYear = "";
		String updateEpisAdjCrtnLog="",insertEpisAdjCrtnLogDtl="",episAdjCrtnLog="",episAdjCrtnLogDtl="";
		String tableName = "employee_freshop_crtn";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		String years[] = null;
		long  retiremntDate =0,monthYear=0,retiremntDate1 =0,monthYear1=0;
		double pensionCOntr = 0.0,oldEmoluments=0.0,oldPensionContri=0.0,oldDueEmoluments=0.0,oldDuePensionAmnt=0.0;
		Connection con = null;
		Statement st = null;	 
		ResultSet rs = null;
		ResultSet rs1= null;
		String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "", chkArrearBrkupFlag = "", arrearBreakupFlag = "N",notFianalizetransID="",chkMnthFlag="",newRecord="false",updateArrearBrkUpData="",updatePFCardNarration="";
		boolean newcpfaccnoflag = false;
		ArrayList adjEmolumentsList = new ArrayList();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			log.info("-------Pension Contri ------" + contri);
			retiremntDate = Date.parse(commonDAO.getRetriedDate(dateOfBirth));
			monthYear = Date.parse(monthyear);
			 
			String retirementMnthYear ="",monthYearOnly="",crtMonthYearFormat="";
			retirementMnthYear =  commonUtil.converDBToAppFormat(
								commonDAO.getRetriedDate(dateOfBirth).trim(), "dd-MMM-yyyy", "MMM-yyyy");
			monthYearOnly =  commonUtil.converDBToAppFormat(
					monthyear.trim(), "dd-MMM-yyyy", "MMM-yyyy") ;
			
			log.info("==retirementMnthYear"+retirementMnthYear.toUpperCase()+"==monthYearOnly"+monthYearOnly.toUpperCase());
			
			
			retiremntDate1 = Date.parse("01-"+retirementMnthYear);
			monthYear1 =  Date.parse("01-"+monthYearOnly);
			log.info("==retiremntDate"+retiremntDate+"==monthYear"+monthYear);
			log.info("==retiremntDate1"+retiremntDate1+"==monthYear1"+monthYear1);
			if((retiremntDate!=monthYear) && (retiremntDate1==monthYear1)){
				crtMonthYearFormat = "01-"+monthYearOnly;
				monthyear = crtMonthYearFormat;
				updateMnthYearFormat(con,pfid,monthyear);
				
			}
			 
			years = finYear.split("-");
			adjObYear = "01-Apr-" + Integer.parseInt(years[0]);
			//For removing  Editing Opening Balance
			empnetobFlag="false";
			if (empnetobFlag.equals("true")) {
				//	 Making this never executed 
				String updateObForAdjCrtn = "update  EMPLOYEE_PENSION_OB_ADJ_CRTN set EMPNETOB="
						+ empnetob
						+ " ,AAINETOB="
						+ aainetob
						+ " where pensionno ="
						+ pfid
						+ " and OBYEAR='"
						+ adjObYear + "'";
				log.info("---update OB Values For Adj Correction------"
						+ updateObForAdjCrtn);
				//st.executeUpdate(updateObForAdjCrtn);

			} else {

				transMonthYear = commonUtil.converDBToAppFormat(monthyear
						.trim(), "dd-MMM-yy", "-MMM-yy");

				if (cpfAccno.indexOf(",") != -1) {
					cpfAccno = cpfAccno.substring(0, cpfAccno.indexOf(","));
				}
				if (transdate.after(new Date("31-Mar-98"))
						&& transdate.before(new Date(commonUtil
								.getCurrentDate("dd-MMM-yy")))
						&& duputationflag != "Y") {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 12 / 100);
				} else if (transdate.before(new Date("31-Mar-98")) || transdate.equals(new Date("31-Mar-98")) ) {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 10 / 100);
				} else {
					if (epf.equals("0")) {
						emppfstatuary = String.valueOf(Float
								.parseFloat(emoluments) * 12 / 100);
					} else {
						emppfstatuary = epf;
					}
				}
				if (emppfstatuary != "" && emppfstatuary != "0.00") {
					pf = String.valueOf(Float.parseFloat(emppfstatuary)
							- Float.parseFloat(contri));
				}
				 
				FinacialDataBean bean = new FinacialDataBean();
				// String checkArrearTable = IDAO.checkArrears(con,
				// monthyear,cpfAccno, "", region, pfid);
				//bean = this.getEmolumentsBeanForAdjCrtn(con, monthyear,
						//cpfAccno, "", region, pfid);
				if (transdate.before(new Date("31-Mar-2008"))) {
					if (bean.getCpfAccNo().equals("")
							|| bean.getNoDataFlag().equals("true")) {
						newcpfaccnoflag = true;
					}
				} else {
					newcpfaccnoflag = false;
				}
				log.info("cpfno ==" + bean.getCpfAccNo() + "newcpfaccnoflag "
						+ newcpfaccnoflag + "--" + bean);
				log.info("emoluments " + bean.getEmoluments());
				if (duputationflag.equals("Y")) {
					emppfstatuary = bean.getEmpPfStatuary();
				} else if (transdate.after(new Date("31-Mar-2008"))
						&& bean.getEmpPfStatuary() != "") {
					pf = String.valueOf(Float.parseFloat(epf)
							- Float.parseFloat(contri));
				}
				
				if(bean!=null){						 
				if(bean.getEmoluments()!=null && !bean.getEmoluments().equals("")){
					oldEmoluments= Double.parseDouble(bean.getEmoluments());
				}
				if(bean.getDueemoluments()!=null && !bean.getDueemoluments().equals("")){
					oldDueEmoluments = Double.parseDouble(bean.getDueemoluments());
				}
				if(bean.getDuepensionamount()!=null && !bean.getDuepensionamount().equals("")){
					oldDuePensionAmnt = Double.parseDouble(bean.getDuepensionamount());
				} 
				if(bean.getArrearBrkUpFlag()!=null && !bean.getArrearBrkUpFlag().equals("")){
				arrearBreakupFlag = bean.getArrearBrkUpFlag();
				}
				}
				
				 
				if (bean.getEmoluments() != ""
						&& bean.getEmoluments() != "0.00") {
					transMonthYear = commonUtil.converDBToAppFormat(monthyear
							.trim(), "dd-MMM-yy", "-MMM-yy");
					String wherecondition = "", emolumntscondition = "";
					if (pfid == "" || transdate.before(new Date("31-Mar-2008"))) {
						wherecondition += "(( cpfaccno='" + cpfAccno
								+ "'   and region='" + region
								+ "' ) or pensionno='" + pfid + "')";

					} else {
						wherecondition += "pensionno='" + pfid + "'";
					}
					log.info("Entered emoluments " + emoluments);
					/*if (emoluments.equals("0")) {
						emolumntscondition = " ,reviseepfemolumentsflag='Y'";
					} else {
						emolumntscondition = "";
					}*/

					sqlQuery = "update " + tableName + " set cpfaccno = '"
							+ cpfAccno + "' , Emoluments_b='" + emoluments
							+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
							+ principle + "',EMPADVRECINTEREST='" + interest
							+ "',pensioncontri_b='"+ contri + "',pf='" + pf + "', emolumentmonths='"
							+ noofmonths + "', empflag='Y',edittrans='"
							+ editTransFlag + "',FORM7NARRATION='"
							+ form7narration + "',editeddate='" + updatedDate
							+ "',freshopflag='Y'  where "+ wherecondition+ " and  to_char(monthyear,'dd-Mon-yy') like '%"+ transMonthYear + "'  AND empflag='Y' ";

				} else {
					if (airportcode.trim().equals("-NA-")) {
						airportcode = "";
					}
					if (transdate.before(new Date("31-Mar-2008"))) {
						pensionCOntr = commonDAO.pensionCalculation(monthyear,
								emoluments, pensionoption, region, "1");
						/*pf = String.valueOf(Double.parseDouble(emppfstatuary)
								- pensionCOntr);*/
					} else {
						String wetheroption = "", retirementDate = "", dateofbirth = "";
						String days = "0";
						double calculatedPension = 0.00;
						String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('"
								+ monthyear
								+ "','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days from employee_personal_info where to_char(pensionno)='"
								+ pfid + "'";
						log.info(checkPFID);
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

						log.info("----in else --calculatedPension---"
								+ calculatedPension);
						pf = String.valueOf(Double.parseDouble(emppfstatuary)
								- pensionCOntr);

					}
					log.info("----contri---" + contri + "----pensionCOntr-----"
							+ pensionCOntr+"oooo"+bean+"uuuuuu"+bean.getNoDataFlag());
					// implemented for having no emoluments but having some
					// arrear related data
					String selqry2 = " select 'X' as flag  from employee_freshop_crtn where pensionno='"
						+ pfid + "' and monthyear='"+monthyear+"' and empflag='Y'";
				rs = st.executeQuery(selqry2);
				if (rs.next()) {
					 

						sqlQuery = "update "
								+ tableName
								+ " set  cpfaccno='"
								+ cpfAccno
								+ "' ,Emoluments_b='"
								+ emoluments
								+ "',empvpf='"
								+ empvpf
								+ "',EMPADVRECPRINCIPAL='"
								+ principle
								+ "',EMPADVRECINTEREST='"
								+ interest								 
								+ "',pensioncontri_b='"
								+ contri
								+ "',pf='"
								+ pf
								+ "',emolumentmonths='"
								+ noofmonths
								+ "' , empflag='Y',edittrans='"
								+ editTransFlag
								+ "',FORM7NARRATION='"
								+ form7narration
								+ "',editeddate='"
								+ updatedDate
								+ "',freshopflag='Y' where pensionno='"
								+ pfid
								+ "'"
								+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
								+ transMonthYear + "'  AND empflag='Y' ";

					} else {
						newRecord="true";
						if (transdate.after(new Date("31-Mar-2008"))) {
							FinacialDataBean dataBean = new FinacialDataBean();
							//dataBean = this.getPreMonthYearData(con, monthyear,
									//pfid);
							//Regarding NullpointerException Ex Pfid:24796 
							if(dataBean!=null){
							log.info("==================Region"+dataBean.getRegion()+"aIRPORTCODE"+dataBean.getAirportCode());
							region = dataBean.getRegion();
							airportcode = dataBean.getAirportCode();
							}
						}
						/* Added on 16-Dec-2011 */
						if ((transdate.after(new Date("01-Jan-2007")) || (transdate
								.equals(new Date("01-Jan-2007"))))) {
							//chkArrearBrkupFlag = this.checkArrearBreakupLimit(
									//con, pfid, monthyear);
							if (chkArrearBrkupFlag.equals("X")) {
								arrearBreakupFlag = "Y";
							} else {
								arrearBreakupFlag = "N";
							}
							log.info("chkArrearBrkupFlag=="
									+ chkArrearBrkupFlag);
						}
						sqlQuery = "insert into "
								+ tableName
								+ " (Emoluments_b,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,ADVANCE,PFWSUB ,PFWCONTRI ,pensioncontri_b,pf,monthyear,cpfaccno,region,pensionno,FORM7NARRATION,ARREARSBREAKUP, EMPFLAG, edittrans,remarks,AIRPORTCODE,editeddate,freshopflag) values('"
								+ emoluments 
								+ "','" + Math.round(Float.parseFloat(empvpf))
								+ "','" + principle + "','" + interest + "','"
								+ advance + "','" + loan + "','" + aailoan
								+ "','" + Math.round(pensionCOntr) + "','"
								+ Math.round(Float.parseFloat(pf)) + "','"
								+ monthyear + "','" + cpfAccno + "','" + region
								+ "','" + pfid + "','" + form7narration + "','"
								+ arrearBreakupFlag
								+ "','Y','N','New Record','" + airportcode
								+ "','" + updatedDate + "','Y')";

					}
				}
				int count = 0;
				String selectEmolumentsLog = "select count(*) as count from EPIS_ADJ_CRTN_EMOLUMENTS_LOG where cpfacno='"
						+ cpfAccno
						+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear + "' and region='" + region + "' ";
				log.info("---------selectEmolumentsLog ------"
						+ selectEmolumentsLog);
				//rs = st.executeQuery(selectEmolumentsLog);
				while (rs.next()) {
					//count = rs.getInt(1);
				}
				/*EmolumentslogBean info = new EmolumentslogBean();
				String mnthYearCompar = "";
				String[] monthYearArray = null;
				for (int i = 0; i < adjEmolumentsList.size(); i++) {
					info = (EmolumentslogBean) adjEmolumentsList.get(i);
					log.info("--monthyear-" + monthyear + "-in List----"
							+ info.getMonthYear());
					if (monthyear.equals(info.getMonthYear())) {
						count = 1;
						if (count == 1) {
							i = adjEmolumentsList.size();
						}
					}
				}*/
// code modified according to monthyear chking
				
				//chkMnthFlag = chkMnthInEmolTempLog(con,pfid,monthyear);
				if(!chkMnthFlag.equals("X")){
					count = 0;
				} 
				EmolumentslogBean emolBeanFirst = new EmolumentslogBean();
				EmolumentslogBean emolBean = new EmolumentslogBean();
				log.info("---------count ------" + count);
				if (count == 0) {
					emolBeanFirst.setPensionNo(pfid);
					emolBeanFirst.setCpfAcno(cpfAccno);
					emolBeanFirst.setMonthYear(monthyear);
					emolBeanFirst.setOldEmoluments(bean.getEmoluments());
					emolBeanFirst.setOldEmppfstatury(oldemppfstatuary);
					emolBeanFirst.setOldEmpvpf(bean.getEmpVpf());
					emolBeanFirst.setOldPrincipal(bean.getPrincipal());
					emolBeanFirst.setOldInterest(bean.getInterest());
					emolBeanFirst.setOldPensioncontri(contri);
					emolBeanFirst.setRegion(region);
					emolBeanFirst.setUserName(username);
					emolBeanFirst.setComputerName(computername);
					emolBeanFirst.setUpdatedDate(updatedDate);
					emolBeanFirst.setOriginalRecord("Y");
					adjEmolumentsList.add(emolBeanFirst);

					emolBean.setPensionNo(pfid);
					emolBean.setCpfAcno(cpfAccno);
					emolBean.setMonthYear(monthyear);
					emolBean.setOldEmoluments(bean.getEmoluments());
					emolBean.setNewEmoluments(emoluments);
					emolBean.setOldEmppfstatury(oldemppfstatuary);
					emolBean.setNewEmppfstatury((new Integer(Math.round(Float
							.parseFloat(emppfstatuary)))).toString());
					emolBean.setOldEmpvpf(bean.getEmpVpf());
					emolBean.setNewEmpvpf(empvpf);
					emolBean.setOldPrincipal(bean.getPrincipal());
					emolBean.setNewPrincipal(principle);
					emolBean.setOldInterest(bean.getInterest());
					emolBean.setNewInterest(interest);
					emolBean.setOldPensioncontri(contri);
					emolBean.setNewPensioncontri(Double.toString(pensionCOntr));
					emolBean.setRegion(region);
					emolBean.setUserName(username);
					emolBean.setComputerName(computername);
					emolBean.setUpdatedDate(updatedDate);
					emolBean.setOriginalRecord("N");
					adjEmolumentsList.add(emolBean);
					log.info("---------2 record ------" + pensionCOntr);
				} else {

					emolBean.setPensionNo(pfid);
					emolBean.setCpfAcno(cpfAccno);
					emolBean.setMonthYear(monthyear);
					emolBean.setOldEmoluments(bean.getEmoluments());
					emolBean.setNewEmoluments(emoluments);
					emolBean.setOldEmppfstatury(oldemppfstatuary);
					emolBean.setNewEmppfstatury((new Integer(Math.round(Float
							.parseFloat(emppfstatuary)))).toString());
					emolBean.setOldEmpvpf(bean.getEmpVpf());
					emolBean.setNewEmpvpf(empvpf);
					emolBean.setOldPrincipal(bean.getPrincipal());
					emolBean.setNewPrincipal(principle);
					emolBean.setOldInterest(bean.getInterest());
					emolBean.setNewInterest(interest);
					emolBean.setOldPensioncontri(bean.getPenContri());
					emolBean.setNewPensioncontri(Double.toString(pensionCOntr));
					emolBean.setRegion(region);
					emolBean.setUserName(username);
					emolBean.setComputerName(computername);
					emolBean.setUpdatedDate(updatedDate);
					emolBean.setOriginalRecord("N");
					adjEmolumentsList.add(emolBean);
					log.info("---------1 record ------" + pensionCOntr);
				}
				
				/*
				 * String selectEmolumentsLog = "select count(*) as count from
				 * EPIS_ADJ_CRTN_EMOLUMENTS_LOG where cpfacno='" + cpfAccno + "'
				 * and to_char(monthyear,'dd-Mon-yy') like '%" + transMonthYear + "'
				 * and region='" + region + "' "; rs =
				 * st.executeQuery(selectEmolumentsLog); while (rs.next()) { int
				 * count = rs.getInt(1); if (count == 0) { emoluments_log =
				 * "insert into
				 * EPIS_ADJ_CRTN_EMOLUMENTS_LOG(oldemoluments,newemoluments,oldemppfstatuary,newemppfstatuary,oldprinciple,newprinciple,oldinterest,newinterest,oldempvpf,newempvpf,OLDPENSIONCONTRI,NEWENSIONCONTRI,monthyear,UPDATEDDATE,pensionno,cpfacno,region,username,computername)values('" +
				 * bean.getEmoluments() + "','" + emoluments + "','" +
				 * oldemppfstatuary + "','" + emppfstatuary + "','" +
				 * bean.getPrincipal() + "','" + principle + "','" +
				 * bean.getInterest() + "','" + interest + "','" +
				 * bean.getEmpVpf() + "','" + empvpf + "','" +
				 * bean.getPenContri() + "','" + contri + "','" + monthyear +
				 * "','" + updatedDate + "','" + pfid + "','" + cpfAccno + "','" +
				 * region + "','" + username + "','" + computername + "')"; }
				 * else { emoluments_log = "update EPIS_ADJ_CRTN_EMOLUMENTS_LOG
				 * set oldemoluments='" + bean.getEmoluments() +
				 * "',newemoluments='" + emoluments + "',oldemppfstatuary='" +
				 * oldemppfstatuary + "',newemppfstatuary='" + emppfstatuary +
				 * "',oldprinciple='" + bean.getPrincipal() + "',newprinciple='" +
				 * principle + "',oldinterest='" + bean.getInterest() +
				 * "',newinterest='" + interest + "',oldempvpf='" +
				 * bean.getEmpVpf() + "',newempvpf='" + empvpf +
				 * "',OLDPENSIONCONTRI='" + bean.getPenContri() +
				 * "',NEWENSIONCONTRI='" + contri + "',monthyear='" + monthyear +
				 * "',UPDATEDDATE='" + updatedDate + "',pensionno='" + pfid +
				 * "',region='" + region + "',username='" + username +
				 * "',computername='" + computername + "' where cpfacno='" +
				 * cpfAccno + "' and to_char(monthyear,'dd-Mon-yy') like '%" +
				 * transMonthYear + "' and region='" + region + "'"; }
				 * 
				 *  }
				 */
				// log.info("emoluments_log .." + emoluments_log);
				log.info(" update transaction " + sqlQuery);
				// st.executeUpdate(emoluments_log);
				st.executeUpdate(sqlQuery);
				//For Inserting in Temp Log Table 
				/*notFianalizetransID = this.getAdjCrtnNotFinalizedTransId(con,
						pfid, finYear);
				log.info("notFianalizetransID  In   " + notFianalizetransID
						+ "adjEmolList Size " + adjEmolumentsList.size());
				 
				this.insertAdjEmolumenstLogInTemp(adjEmolumentsList, pfid,
							finYear, notFianalizetransID);*/
				 //--------------------
				
				/*
				 * If entry not having cpfaccno then new cpfno like AOB-pfid is
				 * updated to transaction table and a record will be inserted
				 * into epis_info_adj_crtn with that AOB-pfid as new cpfaccno
				 */
				ArrayList empPersionalInfoList = new ArrayList();
				EmpMasterBean empBean = new EmpMasterBean();
				String pensionNumber = "";
				if (newcpfaccnoflag == true) {

					String selectQry = " select 'X' as flag  from EPIS_INFO_ADJ_CRTN where CPFACNO='"
							+ cpfAccno + "'";
					//rs = st.executeQuery(selectQry);
					if (!rs.next()) {
						empPersionalInfoList = commonDAO
								.getEmployeePersonalInfo(pfid);
						empBean = (EmpMasterBean) empPersionalInfoList.get(0);
						pensionNumber = commonDAO.getPFID(empBean.getEmpName(),
								empBean.getDateofBirth(), pfid);
						String insertQry = "insert into EPIS_INFO_ADJ_CRTN (CPFACNO, PENSIONNUMBER,  EMPLOYEENO, EMPLOYEENAME, DESEGNATION,  DATEOFBIRTH, DATEOFJOINING,  REMARKS,   EMPFLAG,   REGION, EMPSERIALNUMBER,WETHEROPTION, LASTACTIVE, USERNAME,  IPADDRESS)"
								+ " values ('"
								+ cpfAccno
								+ "', '"
								+ pensionNumber
								+ "',  '"
								+ empBean.getEmpNumber()
								+ "', '"
								+ empBean.getEmpName()
								+ "', '"
								+ empBean.getDesegnation()
								+ "', '"
								+ empBean.getDateofBirth()
								+ "', '"
								+ empBean.getDateofJoining()
								+ "','New Entry due to Non Availability of Cpfaccno',"
								+ "'Y', '"
								+ region
								+ "', '"
								+ pfid
								+ "', '"
								+ empBean.getWetherOption().trim()
								+ "', sysdate, '"
								+ username
								+ "', '"
								+ computername + "')";
						log.info(" New Entry in EPIS_INFO_ADJ_CRTN   "
								+ insertQry);
						//st.executeUpdate(insertQry);
					}
				}

			}
			
			//For Log Tracking for Already entered Data
		 
		String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
			+ pfid+ " and adjobyear='"+finYear+"' and deletedflag='N'";
		int logid = 0;
		log.info("loggeridseq " + loggeridseq);
		rs1 = st.executeQuery(loggeridseq);
		if (rs1.next()) {
		logid = Integer.parseInt(rs1.getString("loggerid"));
		log.info("logid  test" + logid);
		} else {
		st = null;
		st = con.createStatement();
		episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
				+ pfid
				+ ",'"+finYear+"',sysdate,'This pfid already ported before implmenation logic')";
		st.executeUpdate(episAdjCrtnLog);
		st = null;
		st = con.createStatement();
		rs1 = st.executeQuery(loggeridseq);
		if (rs1.next())
			logid = Integer.parseInt(rs1.getString("loggerid"));
		st = null;
		}
		
			// For Restricting the automatic Calc of Pc for all the Months i.e.,
			// from Apr 2008 to till
			if (transdate.after(new Date("31-Mar-08"))) {
				//For updating the narration in the pfcard related
				log.info("===form7narration==="+form7narration);
				if(!form7narration.equals("")){
					updatePFCardNarration= "update epis_adj_crtn set  PFCARDNARRATION='"+form7narration+"' ,PFCARDNRFLAG='Y'  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' ";
					log.info("updatePFCardNarration   " + updatePFCardNarration);
					st.executeUpdate(updatePFCardNarration);
				}
				
				double  returnPC=0.0;
			/*	returnPC = this.pensionContributionProcess2008to11ForAdjCRTN(region, pfid,
						monthyear);*/
				//By Radha On 25-Oct-2012 ArrearBreak up data updation while modifying that data 
				if(newRecord.equals("false") && arrearBreakupFlag.equals("Y")){
					log.info("=======Enters=======");
					st = con.createStatement();
					double newDueEmoluments=0.0,newArrearAmount=0.0,emolDiff=0.0,pcDiff=0.0;
					emolDiff = Double.parseDouble(emoluments)-oldEmoluments;
					pcDiff =   returnPC  - Double.parseDouble(contri);
					newDueEmoluments = oldDueEmoluments -emolDiff;
					newArrearAmount = oldDuePensionAmnt -pcDiff;
					log.info("==Ori=oldEmoluments=="+oldEmoluments+"=emoluments="+emoluments);
					log.info("==OldPensionContri=="+contri+"=returnPC="+returnPC);
					
					log.info("==emolDiff=="+emolDiff+"=newDueEmoluments="+newDueEmoluments);
					log.info("==pcDiff=="+pcDiff+"=newDueEmoluments="+newDueEmoluments);
					updateArrearBrkUpData = "update epis_adj_crtn set dueemoluments="+newDueEmoluments+", arrearamount="+newArrearAmount+"  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' and ARREARSBREAKUP='Y' and dueemoluments is not null and arrearamount is not null and dueemoluments>0";
					log.info("updateArrearBrkUpData   " + updateArrearBrkUpData);
					st.executeUpdate(updateArrearBrkUpData);
				}
				 
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs1.close();
				 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			commonDB.closeConnection(con, st, rs);

		}
		return adjEmolumentsList;
	}
	public ArrayList editTransactionDataForAdjCrtnSecLvl(String cpfAccno,
			String monthyear, String emoluments, String epf, String empvpf,
			String principle, String interest, String advance, String loan,
			String aailoan, String contri, String noofmonths, String pfid,
			String region, String airportcode, String username,
			String computername, String form7narration, String duputationflag,
			String pensionoption, String empnetob, String aainetob,
			String empnetobFlag, String finYear, String editTransFlag,String dateOfBirth) {

		String emppfstatuary = "0", oldemppfstatuary = "0.00", pf = "0.00", adjObYear = "";
		String updateEpisAdjCrtnLog="",insertEpisAdjCrtnLogDtl="",episAdjCrtnLog="",episAdjCrtnLogDtl="";
		String tableName = "employee_freshop_crtn_b";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		String years[] = null;
		long  retiremntDate =0,monthYear=0,retiremntDate1 =0,monthYear1=0;
		double pensionCOntr = 0.0,oldEmoluments=0.0,oldPensionContri=0.0,oldDueEmoluments=0.0,oldDuePensionAmnt=0.0;
		Connection con = null;
		Statement st = null;	 
		ResultSet rs = null;
		ResultSet rs1= null;
		String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "", chkArrearBrkupFlag = "", arrearBreakupFlag = "N",notFianalizetransID="",chkMnthFlag="",newRecord="false",updateArrearBrkUpData="",updatePFCardNarration="";
		boolean newcpfaccnoflag = false;
		ArrayList adjEmolumentsList = new ArrayList();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			log.info("-------Pension Contri ------" + contri);
			retiremntDate = Date.parse(commonDAO.getRetriedDate(dateOfBirth));
			monthYear = Date.parse(monthyear);
			 
			String retirementMnthYear ="",monthYearOnly="",crtMonthYearFormat="";
			retirementMnthYear =  commonUtil.converDBToAppFormat(
								commonDAO.getRetriedDate(dateOfBirth).trim(), "dd-MMM-yyyy", "MMM-yyyy");
			monthYearOnly =  commonUtil.converDBToAppFormat(
					monthyear.trim(), "dd-MMM-yyyy", "MMM-yyyy") ;
			
			log.info("==retirementMnthYear"+retirementMnthYear.toUpperCase()+"==monthYearOnly"+monthYearOnly.toUpperCase());
			
			
			retiremntDate1 = Date.parse("01-"+retirementMnthYear);
			monthYear1 =  Date.parse("01-"+monthYearOnly);
			log.info("==retiremntDate"+retiremntDate+"==monthYear"+monthYear);
			log.info("==retiremntDate1"+retiremntDate1+"==monthYear1"+monthYear1);
			if((retiremntDate!=monthYear) && (retiremntDate1==monthYear1)){
				crtMonthYearFormat = "01-"+monthYearOnly;
				monthyear = crtMonthYearFormat;
				updateMnthYearFormat(con,pfid,monthyear);
				
			}
			 
			years = finYear.split("-");
			adjObYear = "01-Apr-" + Integer.parseInt(years[0]);
			//For removing  Editing Opening Balance
			empnetobFlag="false";
			if (empnetobFlag.equals("true")) {
				//	 Making this never executed 
				String updateObForAdjCrtn = "update  EMPLOYEE_PENSION_OB_ADJ_CRTN set EMPNETOB="
						+ empnetob
						+ " ,AAINETOB="
						+ aainetob
						+ " where pensionno ="
						+ pfid
						+ " and OBYEAR='"
						+ adjObYear + "'";
				log.info("---update OB Values For Adj Correction------"
						+ updateObForAdjCrtn);
				//st.executeUpdate(updateObForAdjCrtn);

			} else {

				transMonthYear = commonUtil.converDBToAppFormat(monthyear
						.trim(), "dd-MMM-yy", "-MMM-yy");

				if (cpfAccno.indexOf(",") != -1) {
					cpfAccno = cpfAccno.substring(0, cpfAccno.indexOf(","));
				}
				if (transdate.after(new Date("31-Mar-98"))
						&& transdate.before(new Date(commonUtil
								.getCurrentDate("dd-MMM-yy")))
						&& duputationflag != "Y") {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 12 / 100);
				} else if (transdate.before(new Date("31-Mar-98")) || transdate.equals(new Date("31-Mar-98")) ) {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 10 / 100);
				} else {
					if (epf.equals("0")) {
						emppfstatuary = String.valueOf(Float
								.parseFloat(emoluments) * 12 / 100);
					} else {
						emppfstatuary = epf;
					}
				}
				if (emppfstatuary != "" && emppfstatuary != "0.00") {
					pf = String.valueOf(Float.parseFloat(emppfstatuary)
							- Float.parseFloat(contri));
				}
				 
				FinacialDataBean bean = new FinacialDataBean();
				// String checkArrearTable = IDAO.checkArrears(con,
				// monthyear,cpfAccno, "", region, pfid);
				//bean = this.getEmolumentsBeanForAdjCrtn(con, monthyear,
						//cpfAccno, "", region, pfid);
				if (transdate.before(new Date("31-Mar-2008"))) {
					if (bean.getCpfAccNo().equals("")
							|| bean.getNoDataFlag().equals("true")) {
						newcpfaccnoflag = true;
					}
				} else {
					newcpfaccnoflag = false;
				}
				log.info("cpfno ==" + bean.getCpfAccNo() + "newcpfaccnoflag "
						+ newcpfaccnoflag + "--" + bean);
				log.info("emoluments " + bean.getEmoluments());
				if (duputationflag.equals("Y")) {
					emppfstatuary = bean.getEmpPfStatuary();
				} else if (transdate.after(new Date("31-Mar-2008"))
						&& bean.getEmpPfStatuary() != "") {
					pf = String.valueOf(Float.parseFloat(epf)
							- Float.parseFloat(contri));
				}
				
				if(bean!=null){						 
				if(bean.getEmoluments()!=null && !bean.getEmoluments().equals("")){
					oldEmoluments= Double.parseDouble(bean.getEmoluments());
				}
				if(bean.getDueemoluments()!=null && !bean.getDueemoluments().equals("")){
					oldDueEmoluments = Double.parseDouble(bean.getDueemoluments());
				}
				if(bean.getDuepensionamount()!=null && !bean.getDuepensionamount().equals("")){
					oldDuePensionAmnt = Double.parseDouble(bean.getDuepensionamount());
				} 
				if(bean.getArrearBrkUpFlag()!=null && !bean.getArrearBrkUpFlag().equals("")){
				arrearBreakupFlag = bean.getArrearBrkUpFlag();
				}
				}
				
				 
				if (bean.getEmoluments() != ""
						&& bean.getEmoluments() != "0.00") {
					transMonthYear = commonUtil.converDBToAppFormat(monthyear
							.trim(), "dd-MMM-yy", "-MMM-yy");
					String wherecondition = "", emolumntscondition = "";
					if (pfid == "" || transdate.before(new Date("31-Mar-2008"))) {
						wherecondition += "(( cpfaccno='" + cpfAccno
								+ "'   and region='" + region
								+ "' ) or pensionno='" + pfid + "')";

					} else {
						wherecondition += "pensionno='" + pfid + "'";
					}
					log.info("Entered emoluments " + emoluments);
					/*if (emoluments.equals("0")) {
						emolumntscondition = " ,reviseepfemolumentsflag='Y'";
					} else {
						emolumntscondition = "";
					}*/

					sqlQuery = "update " + tableName + " set cpfaccno = '"
							+ cpfAccno + "' , Emoluments_b='" + emoluments
							+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
							+ principle + "',EMPADVRECINTEREST='" + interest
							+ "',pensioncontri_b='"+ contri + "',pf='" + pf + "', emolumentmonths='"
							+ noofmonths + "', empflag='Y',edittrans='"
							+ editTransFlag + "',FORM7NARRATION='"
							+ form7narration + "',editeddate='" + updatedDate
							+ "',freshopflag='Y'  where "+ wherecondition+ " and  to_char(monthyear,'dd-Mon-yy') like '%"+ transMonthYear + "'  AND empflag='Y' ";

				} else {
					if (airportcode.trim().equals("-NA-")) {
						airportcode = "";
					}
					if (transdate.before(new Date("31-Mar-2008"))) {
						pensionCOntr = commonDAO.pensionCalculation(monthyear,
								emoluments, pensionoption, region, "1");
						/*pf = String.valueOf(Double.parseDouble(emppfstatuary)
								- pensionCOntr);*/
					} else {
						String wetheroption = "", retirementDate = "", dateofbirth = "";
						String days = "0";
						double calculatedPension = 0.00;
						String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('"
								+ monthyear
								+ "','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days from employee_personal_info where to_char(pensionno)='"
								+ pfid + "'";
						log.info(checkPFID);
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

						log.info("----in else --calculatedPension---"
								+ calculatedPension);
						pf = String.valueOf(Double.parseDouble(emppfstatuary)
								- pensionCOntr);

					}
					log.info("----contri---" + contri + "----pensionCOntr-----"
							+ pensionCOntr+"oooo"+bean+"uuuuuu"+bean.getNoDataFlag());
					// implemented for having no emoluments but having some
					// arrear related data
					String selqry2 = " select 'X' as flag  from employee_freshop_crtn where pensionno='"
						+ pfid + "' and monthyear='"+monthyear+"' and empflag='Y'";
				rs = st.executeQuery(selqry2);
				if (rs.next()) {
					 

						sqlQuery = "update "
								+ tableName
								+ " set  cpfaccno='"
								+ cpfAccno
								+ "' ,Emoluments_b='"
								+ emoluments
								+ "',empvpf='"
								+ empvpf
								+ "',EMPADVRECPRINCIPAL='"
								+ principle
								+ "',EMPADVRECINTEREST='"
								+ interest								 
								+ "',pensioncontri_b='"
								+ contri
								+ "',pf='"
								+ pf
								+ "',emolumentmonths='"
								+ noofmonths
								+ "' , empflag='Y',edittrans='"
								+ editTransFlag
								+ "',FORM7NARRATION='"
								+ form7narration
								+ "',editeddate='"
								+ updatedDate
								+ "',freshopflag='Y' where pensionno='"
								+ pfid
								+ "'"
								+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
								+ transMonthYear + "'  AND empflag='Y' ";

					} else {
						newRecord="true";
						if (transdate.after(new Date("31-Mar-2008"))) {
							FinacialDataBean dataBean = new FinacialDataBean();
							//dataBean = this.getPreMonthYearData(con, monthyear,
									//pfid);
							//Regarding NullpointerException Ex Pfid:24796 
							if(dataBean!=null){
							log.info("==================Region"+dataBean.getRegion()+"aIRPORTCODE"+dataBean.getAirportCode());
							region = dataBean.getRegion();
							airportcode = dataBean.getAirportCode();
							}
						}
						/* Added on 16-Dec-2011 */
						if ((transdate.after(new Date("01-Jan-2007")) || (transdate
								.equals(new Date("01-Jan-2007"))))) {
							//chkArrearBrkupFlag = this.checkArrearBreakupLimit(
									//con, pfid, monthyear);
							if (chkArrearBrkupFlag.equals("X")) {
								arrearBreakupFlag = "Y";
							} else {
								arrearBreakupFlag = "N";
							}
							log.info("chkArrearBrkupFlag=="
									+ chkArrearBrkupFlag);
						}
						sqlQuery = "insert into "
								+ tableName
								+ " (Emoluments_b,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,ADVANCE,PFWSUB ,PFWCONTRI ,pensioncontri_b,pf,monthyear,cpfaccno,region,pensionno,FORM7NARRATION,ARREARSBREAKUP, EMPFLAG, edittrans,remarks,AIRPORTCODE,editeddate,freshopflag) values('"
								+ emoluments 
								+ "','" + Math.round(Float.parseFloat(empvpf))
								+ "','" + principle + "','" + interest + "','"
								+ advance + "','" + loan + "','" + aailoan
								+ "','" + Math.round(pensionCOntr) + "','"
								+ Math.round(Float.parseFloat(pf)) + "','"
								+ monthyear + "','" + cpfAccno + "','" + region
								+ "','" + pfid + "','" + form7narration + "','"
								+ arrearBreakupFlag
								+ "','Y','N','New Record','" + airportcode
								+ "','" + updatedDate + "','Y')";

					}
				}
				int count = 0;
				String selectEmolumentsLog = "select count(*) as count from EPIS_ADJ_CRTN_EMOLUMENTS_LOG where cpfacno='"
						+ cpfAccno
						+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear + "' and region='" + region + "' ";
				log.info("---------selectEmolumentsLog ------"
						+ selectEmolumentsLog);
				//rs = st.executeQuery(selectEmolumentsLog);
				while (rs.next()) {
					//count = rs.getInt(1);
				}
				/*EmolumentslogBean info = new EmolumentslogBean();
				String mnthYearCompar = "";
				String[] monthYearArray = null;
				for (int i = 0; i < adjEmolumentsList.size(); i++) {
					info = (EmolumentslogBean) adjEmolumentsList.get(i);
					log.info("--monthyear-" + monthyear + "-in List----"
							+ info.getMonthYear());
					if (monthyear.equals(info.getMonthYear())) {
						count = 1;
						if (count == 1) {
							i = adjEmolumentsList.size();
						}
					}
				}*/
// code modified according to monthyear chking
				
				//chkMnthFlag = chkMnthInEmolTempLog(con,pfid,monthyear);
				if(!chkMnthFlag.equals("X")){
					count = 0;
				} 
				EmolumentslogBean emolBeanFirst = new EmolumentslogBean();
				EmolumentslogBean emolBean = new EmolumentslogBean();
				log.info("---------count ------" + count);
				if (count == 0) {
					emolBeanFirst.setPensionNo(pfid);
					emolBeanFirst.setCpfAcno(cpfAccno);
					emolBeanFirst.setMonthYear(monthyear);
					emolBeanFirst.setOldEmoluments(bean.getEmoluments());
					emolBeanFirst.setOldEmppfstatury(oldemppfstatuary);
					emolBeanFirst.setOldEmpvpf(bean.getEmpVpf());
					emolBeanFirst.setOldPrincipal(bean.getPrincipal());
					emolBeanFirst.setOldInterest(bean.getInterest());
					emolBeanFirst.setOldPensioncontri(contri);
					emolBeanFirst.setRegion(region);
					emolBeanFirst.setUserName(username);
					emolBeanFirst.setComputerName(computername);
					emolBeanFirst.setUpdatedDate(updatedDate);
					emolBeanFirst.setOriginalRecord("Y");
					adjEmolumentsList.add(emolBeanFirst);

					emolBean.setPensionNo(pfid);
					emolBean.setCpfAcno(cpfAccno);
					emolBean.setMonthYear(monthyear);
					emolBean.setOldEmoluments(bean.getEmoluments());
					emolBean.setNewEmoluments(emoluments);
					emolBean.setOldEmppfstatury(oldemppfstatuary);
					emolBean.setNewEmppfstatury((new Integer(Math.round(Float
							.parseFloat(emppfstatuary)))).toString());
					emolBean.setOldEmpvpf(bean.getEmpVpf());
					emolBean.setNewEmpvpf(empvpf);
					emolBean.setOldPrincipal(bean.getPrincipal());
					emolBean.setNewPrincipal(principle);
					emolBean.setOldInterest(bean.getInterest());
					emolBean.setNewInterest(interest);
					emolBean.setOldPensioncontri(contri);
					emolBean.setNewPensioncontri(Double.toString(pensionCOntr));
					emolBean.setRegion(region);
					emolBean.setUserName(username);
					emolBean.setComputerName(computername);
					emolBean.setUpdatedDate(updatedDate);
					emolBean.setOriginalRecord("N");
					adjEmolumentsList.add(emolBean);
					log.info("---------2 record ------" + pensionCOntr);
				} else {

					emolBean.setPensionNo(pfid);
					emolBean.setCpfAcno(cpfAccno);
					emolBean.setMonthYear(monthyear);
					emolBean.setOldEmoluments(bean.getEmoluments());
					emolBean.setNewEmoluments(emoluments);
					emolBean.setOldEmppfstatury(oldemppfstatuary);
					emolBean.setNewEmppfstatury((new Integer(Math.round(Float
							.parseFloat(emppfstatuary)))).toString());
					emolBean.setOldEmpvpf(bean.getEmpVpf());
					emolBean.setNewEmpvpf(empvpf);
					emolBean.setOldPrincipal(bean.getPrincipal());
					emolBean.setNewPrincipal(principle);
					emolBean.setOldInterest(bean.getInterest());
					emolBean.setNewInterest(interest);
					emolBean.setOldPensioncontri(bean.getPenContri());
					emolBean.setNewPensioncontri(Double.toString(pensionCOntr));
					emolBean.setRegion(region);
					emolBean.setUserName(username);
					emolBean.setComputerName(computername);
					emolBean.setUpdatedDate(updatedDate);
					emolBean.setOriginalRecord("N");
					adjEmolumentsList.add(emolBean);
					log.info("---------1 record ------" + pensionCOntr);
				}
				
				/*
				 * String selectEmolumentsLog = "select count(*) as count from
				 * EPIS_ADJ_CRTN_EMOLUMENTS_LOG where cpfacno='" + cpfAccno + "'
				 * and to_char(monthyear,'dd-Mon-yy') like '%" + transMonthYear + "'
				 * and region='" + region + "' "; rs =
				 * st.executeQuery(selectEmolumentsLog); while (rs.next()) { int
				 * count = rs.getInt(1); if (count == 0) { emoluments_log =
				 * "insert into
				 * EPIS_ADJ_CRTN_EMOLUMENTS_LOG(oldemoluments,newemoluments,oldemppfstatuary,newemppfstatuary,oldprinciple,newprinciple,oldinterest,newinterest,oldempvpf,newempvpf,OLDPENSIONCONTRI,NEWENSIONCONTRI,monthyear,UPDATEDDATE,pensionno,cpfacno,region,username,computername)values('" +
				 * bean.getEmoluments() + "','" + emoluments + "','" +
				 * oldemppfstatuary + "','" + emppfstatuary + "','" +
				 * bean.getPrincipal() + "','" + principle + "','" +
				 * bean.getInterest() + "','" + interest + "','" +
				 * bean.getEmpVpf() + "','" + empvpf + "','" +
				 * bean.getPenContri() + "','" + contri + "','" + monthyear +
				 * "','" + updatedDate + "','" + pfid + "','" + cpfAccno + "','" +
				 * region + "','" + username + "','" + computername + "')"; }
				 * else { emoluments_log = "update EPIS_ADJ_CRTN_EMOLUMENTS_LOG
				 * set oldemoluments='" + bean.getEmoluments() +
				 * "',newemoluments='" + emoluments + "',oldemppfstatuary='" +
				 * oldemppfstatuary + "',newemppfstatuary='" + emppfstatuary +
				 * "',oldprinciple='" + bean.getPrincipal() + "',newprinciple='" +
				 * principle + "',oldinterest='" + bean.getInterest() +
				 * "',newinterest='" + interest + "',oldempvpf='" +
				 * bean.getEmpVpf() + "',newempvpf='" + empvpf +
				 * "',OLDPENSIONCONTRI='" + bean.getPenContri() +
				 * "',NEWENSIONCONTRI='" + contri + "',monthyear='" + monthyear +
				 * "',UPDATEDDATE='" + updatedDate + "',pensionno='" + pfid +
				 * "',region='" + region + "',username='" + username +
				 * "',computername='" + computername + "' where cpfacno='" +
				 * cpfAccno + "' and to_char(monthyear,'dd-Mon-yy') like '%" +
				 * transMonthYear + "' and region='" + region + "'"; }
				 * 
				 *  }
				 */
				// log.info("emoluments_log .." + emoluments_log);
				log.info(" update transaction " + sqlQuery);
				// st.executeUpdate(emoluments_log);
				st.executeUpdate(sqlQuery);
				//For Inserting in Temp Log Table 
				/*notFianalizetransID = this.getAdjCrtnNotFinalizedTransId(con,
						pfid, finYear);
				log.info("notFianalizetransID  In   " + notFianalizetransID
						+ "adjEmolList Size " + adjEmolumentsList.size());
				 
				this.insertAdjEmolumenstLogInTemp(adjEmolumentsList, pfid,
							finYear, notFianalizetransID);*/
				 //--------------------
				
				/*
				 * If entry not having cpfaccno then new cpfno like AOB-pfid is
				 * updated to transaction table and a record will be inserted
				 * into epis_info_adj_crtn with that AOB-pfid as new cpfaccno
				 */
				ArrayList empPersionalInfoList = new ArrayList();
				EmpMasterBean empBean = new EmpMasterBean();
				String pensionNumber = "";
				if (newcpfaccnoflag == true) {

					String selectQry = " select 'X' as flag  from EPIS_INFO_ADJ_CRTN where CPFACNO='"
							+ cpfAccno + "'";
					//rs = st.executeQuery(selectQry);
					if (!rs.next()) {
						empPersionalInfoList = commonDAO
								.getEmployeePersonalInfo(pfid);
						empBean = (EmpMasterBean) empPersionalInfoList.get(0);
						pensionNumber = commonDAO.getPFID(empBean.getEmpName(),
								empBean.getDateofBirth(), pfid);
						String insertQry = "insert into EPIS_INFO_ADJ_CRTN (CPFACNO, PENSIONNUMBER,  EMPLOYEENO, EMPLOYEENAME, DESEGNATION,  DATEOFBIRTH, DATEOFJOINING,  REMARKS,   EMPFLAG,   REGION, EMPSERIALNUMBER,WETHEROPTION, LASTACTIVE, USERNAME,  IPADDRESS)"
								+ " values ('"
								+ cpfAccno
								+ "', '"
								+ pensionNumber
								+ "',  '"
								+ empBean.getEmpNumber()
								+ "', '"
								+ empBean.getEmpName()
								+ "', '"
								+ empBean.getDesegnation()
								+ "', '"
								+ empBean.getDateofBirth()
								+ "', '"
								+ empBean.getDateofJoining()
								+ "','New Entry due to Non Availability of Cpfaccno',"
								+ "'Y', '"
								+ region
								+ "', '"
								+ pfid
								+ "', '"
								+ empBean.getWetherOption().trim()
								+ "', sysdate, '"
								+ username
								+ "', '"
								+ computername + "')";
						log.info(" New Entry in EPIS_INFO_ADJ_CRTN   "
								+ insertQry);
						//st.executeUpdate(insertQry);
					}
				}

			}
			
			//For Log Tracking for Already entered Data
		 
		String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
			+ pfid+ " and adjobyear='"+finYear+"' and deletedflag='N'";
		int logid = 0;
		log.info("loggeridseq " + loggeridseq);
		rs1 = st.executeQuery(loggeridseq);
		if (rs1.next()) {
		logid = Integer.parseInt(rs1.getString("loggerid"));
		log.info("logid  test" + logid);
		} else {
		st = null;
		st = con.createStatement();
		episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
				+ pfid
				+ ",'"+finYear+"',sysdate,'This pfid already ported before implmenation logic')";
		st.executeUpdate(episAdjCrtnLog);
		st = null;
		st = con.createStatement();
		rs1 = st.executeQuery(loggeridseq);
		if (rs1.next())
			logid = Integer.parseInt(rs1.getString("loggerid"));
		st = null;
		}
		
			// For Restricting the automatic Calc of Pc for all the Months i.e.,
			// from Apr 2008 to till
			if (transdate.after(new Date("31-Mar-08"))) {
				//For updating the narration in the pfcard related
				log.info("===form7narration==="+form7narration);
				if(!form7narration.equals("")){
					updatePFCardNarration= "update epis_adj_crtn set  PFCARDNARRATION='"+form7narration+"' ,PFCARDNRFLAG='Y'  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' ";
					log.info("updatePFCardNarration   " + updatePFCardNarration);
					st.executeUpdate(updatePFCardNarration);
				}
				
				double  returnPC=0.0;
			/*	returnPC = this.pensionContributionProcess2008to11ForAdjCRTN(region, pfid,
						monthyear);*/
				//By Radha On 25-Oct-2012 ArrearBreak up data updation while modifying that data 
				if(newRecord.equals("false") && arrearBreakupFlag.equals("Y")){
					log.info("=======Enters=======");
					st = con.createStatement();
					double newDueEmoluments=0.0,newArrearAmount=0.0,emolDiff=0.0,pcDiff=0.0;
					emolDiff = Double.parseDouble(emoluments)-oldEmoluments;
					pcDiff =   returnPC  - Double.parseDouble(contri);
					newDueEmoluments = oldDueEmoluments -emolDiff;
					newArrearAmount = oldDuePensionAmnt -pcDiff;
					log.info("==Ori=oldEmoluments=="+oldEmoluments+"=emoluments="+emoluments);
					log.info("==OldPensionContri=="+contri+"=returnPC="+returnPC);
					
					log.info("==emolDiff=="+emolDiff+"=newDueEmoluments="+newDueEmoluments);
					log.info("==pcDiff=="+pcDiff+"=newDueEmoluments="+newDueEmoluments);
					updateArrearBrkUpData = "update epis_adj_crtn set dueemoluments="+newDueEmoluments+", arrearamount="+newArrearAmount+"  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' and ARREARSBREAKUP='Y' and dueemoluments is not null and arrearamount is not null and dueemoluments>0";
					log.info("updateArrearBrkUpData   " + updateArrearBrkUpData);
					st.executeUpdate(updateArrearBrkUpData);
				}
				 
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs1.close();
				 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			commonDB.closeConnection(con, st, rs);

		}
		return adjEmolumentsList;
	}
	public int updateMnthYearFormat(Connection con, String pfid,	String monthyear) {
		Statement st = null;
		ResultSet rs = null;
		String updateQuery = "";
		int result =0;
		updateQuery = "update  employee_freshop_crtn set monthyear ='"+monthyear+"' where pensionno ="+pfid+" and  to_char(monthyear,'Mon-yyyy') = to_char(to_date('"+monthyear+"'), 'Mon-yyyy')";
			 
		try {
			log.info("updateMnthYearFormat ==updateQuery===" + updateQuery);
			st = con.createStatement();
			result = st.executeUpdate(updateQuery);
			  
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return result;
	}
	public FinacialDataBean getEmolumentsBeanForAdjCrtn(Connection con, String fromDate,
			String cpfaccno, String employeeno, String region, String Pensionno) {

		String foundEmpFlag = "";
		Statement st = null;
		ResultSet rs = null;
		FinacialDataBean bean =null;
		try {
			DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

			Date transdate = df.parse(fromDate);

			String transMonthYear = commonUtil.converDBToAppFormat(fromDate
					.trim(), "dd-MMM-yyyy", "-MMM-yy");
			String query = "";
			log.info("cpfaccno---------"+cpfaccno+"--"+region);
			if (Pensionno == "" || transdate.before(new Date("31-Mar-2008"))) {
				query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(ARREARSBREAKUP,'N') as ARREARSBREAKUP,DUEEMOLUMENTS,ARREARAMOUNT  from epis_adj_crtn where to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and (( cpfaccno='" + cpfaccno + "' and region='" + region + "') or pensionno='" + Pensionno + "')   and empflag='Y' ";
			} else {
				query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(ARREARSBREAKUP,'N') as ARREARSBREAKUP,DUEEMOLUMENTS,ARREARAMOUNT   from epis_adj_crtn where to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear
						+ "' and pensionno='"
						+ Pensionno
						+ "'  and empflag='Y' ";
			}
			log.info("AdjCrtnDAO::getEmolumentsBeanForAdjCrtn()----------"+query);
			st = con.createStatement();
			rs = st.executeQuery(query);

			if (rs.next()) {			 
				bean = new FinacialDataBean();
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
				if (rs.getString("ARREARSBREAKUP") != null) {
					bean.setArrearBrkUpFlag(rs.getString("ARREARSBREAKUP"));
				}else{
					bean.setArrearBrkUpFlag("N");
				}
				if (rs.getString("DUEEMOLUMENTS") != null) {
					bean.setDueemoluments(rs.getString("DUEEMOLUMENTS"));
				}else{
					bean.setDueemoluments("0");
				}
				if (rs.getString("ARREARAMOUNT") != null) {
					bean.setDuepensionamount(rs.getString("ARREARAMOUNT"));
				}else{
					bean.setDuepensionamount("0");
				}			 
				bean.setNoDataFlag("false");
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
		// log.info("AdjCrtnDAO :checkPensionDuplicate() leaving method");
		//Added on 23-Nov-2011 by Radha If not data is available as not distrubing othee code 
		log.info("---bean-----"+bean);
		if(bean==null){
			bean = new FinacialDataBean();
			bean.setNoDataFlag("true");
		}
		return bean;
	}
	
	public int getApprove(String pfid) {
		String sqlQuery = "";
		int n=0;
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			
		sqlQuery = "insert into employee_freshedit_track(pensionno,status) values('"+pfid+"','Approved')";
		log.info("--chkPfidinAdjCrth()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			n = st.executeUpdate(sqlQuery);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return n;
	} 
	public int getApproveSec(String pfid) {
		String sqlQuery = "";
		int n=0;
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
			
		sqlQuery = "update employee_freshedit_track set Status_Sec='Approved' where PENSIONNO='"+pfid+"'";
		log.info("--update()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			n = st.executeUpdate(sqlQuery);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return n;
	} 
	public String chkPfidStatusInAdjCrtn(String empserialNO) {
		log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtn Entering method");		 
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		String selectSQL = "", verifiedby = "",pfidStatus="";
		int i = 0;
		selectSQL = "select distinct verifiedby as verifiedby  from epis_adj_crtn_pfid_tracking where pensionno= "+ empserialNO +" and blocked ='N'";

		try {
			log.info("selectSQL==" + selectSQL);		 
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(selectSQL);
			while (rs.next()) {
				if (rs.getString("verifiedby") != null) {
					verifiedby = rs.getString("verifiedby");
				}
				if(verifiedby.equals("Initial,Approved")){
					pfidStatus="Approved";
					break;
				}else{
					pfidStatus="NotApproved";
				}
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("pfidStatus=="+pfidStatus+"===verifiedby=="+verifiedby);
		log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtn leaving method");
		return pfidStatus;
	}
	public ArrayList getAllYearsForm7PrintOut(String selectedYear,
			String month, String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName, String formType, String formRevisedFlag,
			String adjFlag, String frmName, String pcFlag) {
		ArrayList totalYearForm8List = new ArrayList();
		ArrayList form8List = new ArrayList();
		String minYear = "", maxYear = "";
		boolean chkYears = false;
		String message = "";
		int messageFromYear = 0, messageToYear = 0;
		Form7MultipleYearBean multipleYearBean = null;
		int currentYear = 0, fromOldYear = 1995, totalSpan = 0;
		log.info("selectedYear======getRPFCForm8IndivReport======="
				+ selectedYear);
		if (selectedYear.equals("NO-SELECT")
				|| selectedYear.equals("Select One")) {
			//currentYear = Integer.parseInt(commonUtil.getCurrentDate("yyyy")) + 1;
			currentYear=2015+1;
			
			
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

		for (int i = 0; i < totalSpan; i++) {
			multipleYearBean = new Form7MultipleYearBean();
			form8List = this.rnfcForm7Report(Integer.toString(fromOldYear + i),
					month, sortedColumn, region, formFlag, airportCode,
					pensionNo, range, empflag, empName, formType,
					formRevisedFlag, adjFlag, frmName,pcFlag);
			if (form8List.size() != 0) {
				messageFromYear = fromOldYear + i;
				messageToYear = messageFromYear + 1;
				message = "01-Apr-" + messageFromYear + " To Mar-"
						+ messageToYear;
				multipleYearBean.setEachYearList(form8List);
				multipleYearBean.setMessage(message);
				totalYearForm8List.add(multipleYearBean);
			}
		}
		return totalYearForm8List;
	}
	public String getMinMaxYearsForArrearBreakup(String pensionno) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String minMaxYearOfArrear = "";
		int minYear = 0, maxYear = 0;
		boolean breakFlag = false;
		String sqlQuery = "select to_char(ARREARFORMDATE,'yyyy') as minyear,to_char(ARREARTODATE,'yyyy') as maxyear from employee_arrear_breakup where pensionno="
				+ pensionno;
		try {
			con = commonDB.getConnection();

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				minYear = rs.getInt("minyear");
				maxYear = rs.getInt("maxyear");
				breakFlag = true;
			}
			if (breakFlag == true) {
				minMaxYearOfArrear = Integer.toString(minYear) + "-"
						+ Integer.toString(maxYear);
			} else {
				minMaxYearOfArrear = "";
			}

		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return minMaxYearOfArrear;

	}
	public ArrayList rnfcForm7Report(String selectedYear, String month,
			String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName, String formType, String formRevisedFlag,
			String adjFlag, String frmName, String pcFlag) {
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

		if (!selectedYear.equals("Select One") && month.equals("NO-SELECT")) {
			fromYear = "01-Apr-" + selectedYear;
			toSelectYear = Integer.parseInt(selectedYear) + 1;
			toYear = "01-Mar-" + toSelectYear;
		} else if (!selectedYear.equals("Select One")
				&& !month.equals("NO-SELECT")) {
			fromYear = "01-" + frmMonth + "-" + selectedYear;
			toYear = fromYear;
		} else {
			fromYear = Constants.FORMS_FROM_DATE;
			toYear = Constants.FORMS_TO_DATE;
		}  

		empList = this.getForm7EmployeeInfo(range, sortedColumn, region,
				fromYear, toYear, airportCode, pensionNo, empflag, empName,
				"N", formType, formRevisedFlag,adjFlag);

		/*
		 * if ((empList.size() == 0 || (!range.equals("NO-SELECT") &&
		 * formRevisedFlag .equals("N"))) && Integer.parseInt(selectedYear) >=
		 * 2008) {
		 * 
		 * arrearEmpList = this.getForm7EmployeeInfo(range, sortedColumn,
		 * region, fromYear, toYear, airportCode, pensionNo, empflag, empName,
		 * "Y", formType, formRevisedFlag); empList.addAll(arrearEmpList);
		 * arrearsFlag = true;
		 *  }
		 */
		log.info("rnfcForm7Report:::fromYear" + fromYear + "toYear" + toYear
				+ "arrearsFlag" + arrearsFlag + "empList.size()"
				+ empList.size());

		String pensionInfo = "", regionInfo = "";

		for (int i = 0; i < empList.size(); i++) {
			Count++;
			personalInfo = (EmployeePersonalInfo) empList.get(i);
			log.info("rnfcForm7Report:::Pensionno"
					+ personalInfo.getOldPensionNo());
			
			if (personalInfo.getChkarrearAdj().equals("Y")) {
				arrearsFlag = true;
			} else {
				arrearsFlag = false;
			}
			if (!personalInfo.getDateOfBirth().equals("---")) {
				try {
					dateOfRetriment = commonDAO.getRetriedDate(personalInfo
							.getDateOfBirth());
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!personalInfo.getFinalSettlementDate().equals("---")) {
				int finalSettlmentYear = 0;
				try {
					finalSettlmentYear = Integer.parseInt(commonUtil
							.converDBToAppFormat(personalInfo
									.getFinalSettlementDate(), "dd-MMM-yyyy",
									"yyyy"));
				} catch (NumberFormatException e) {
					log.printStackTrace(e);
				} catch (InvalidDataException e) {
					log.printStackTrace(e);
				}
				/*
				 * Commented on 21-Jun-2012 By Radha fpr pfid :12888 having no arrears in 2011-2012
				 * if ((Integer.parseInt(selectedYear) >= finalSettlmentYear && finalSettlmentYear <= toSelectYear)
						&& arrearsFlag == false) {
					arrearsFlag = true;
					if (Integer.parseInt(selectedYear)>=2011){
						dateOfRetriment=personalInfo.getSeperationDate();
					}
				}*/
				
				if ((Integer.parseInt(selectedYear) >= finalSettlmentYear && finalSettlmentYear <= toSelectYear)
						&& arrearsFlag == false) {
					if(!personalInfo.getSeperationDate().equals("---")){
						arrearsFlag = true;
					}					
					if (Integer.parseInt(selectedYear)>=2011 && !personalInfo.getSeperationDate().equals("---") ){						
						dateOfRetriment=personalInfo.getSeperationDate();
					}
				}
				
			}
			//For Identifying the Month upto PC has to be Calculated for Other than Retired Cases
			if(personalInfo.getSeperationReason().equals("Resignation")  ||
					personalInfo.getSeperationReason().equals("Death") ||
					personalInfo.getSeperationReason().equals("VRS")){
				int daysInMonth=0;
				
				try {
					daysInMonth = Integer.parseInt(commonUtil
							.converDBToAppFormat(dateOfRetriment, "dd-MMM-yyyy",
									"dd"));
				if(daysInMonth>1){
					dateOfRetriment = personalInfo.getCalcPCUptoDate();
				}
				} catch (NumberFormatException e) {
					log.printStackTrace(e);
				} catch (InvalidDataException e) {
					log.printStackTrace(e);
				}
			}
			//By Radha On 3-Aug-2012 For Getting PC after Seperation Ex:1007 Req By Sehgal
		/*	if(pcFlag.equals("true")){
				actualDOR = dateOfRetriment;
				dateOfRetriment = getPCMonthsAfterSeperation(personalInfo.getPensionNo(),actualDOR);
				//For Calc Emoluments Correctly According to Days of Retirement Month
				if(personalInfo.getSeperationReason_PC().trim().equals("Retirement")){
					pcFlag="false";
		 			}
			}*/
			//log.info("===personalInfo.getSeperationReason()==="+personalInfo.getSeperationReason_PC()+"==pcFlag="+pcFlag);
			String tempEntitle = "", returnFromDate = "", fromDOJ = "";
			if (formRevisedFlag.equals("N")) {
				tempEntitle = "01"
						+ personalInfo.getDateOfEntitle().substring(2,
								personalInfo.getDateOfEntitle().length());
				returnFromDate = this.compareTwoDates(tempEntitle, fromYear);
				if (returnFromDate.equals(tempEntitle)) {
					String[] entitlelist = personalInfo.getDateOfEntitle()
							.split("-");
					if (entitlelist[1].toLowerCase().equals("mar")) {
						fromDOJ = "31-Mar-" + entitlelist[2];
					} else {
						fromDOJ = personalInfo.getDateOfEntitle();
					}
				} else {
					fromDOJ = fromYear;
				}
			} else {
				log.info("rnfcForm7Report:::fromYear"
						+ personalInfo.getFromarrearreviseddate() + "toYear"
						+ personalInfo.getToarrearreviseddate());
				tempEntitle = personalInfo.getFromarrearreviseddate();
				returnFromDate = this.compareTwoDates(tempEntitle, fromYear);
				if (returnFromDate.equals(tempEntitle)) {
					fromDOJ = personalInfo.getFromarrearreviseddate();
				} else {
					fromDOJ = fromYear;
				}

			}

			log.info("Pensionno" + personalInfo.getOldPensionNo() + "From Date"
					+ fromYear + "toYear" + toYear + "fromDOJ" + fromDOJ
					+ "returnFromDate" + returnFromDate + "PFID"
					+ personalInfo.getPfIDString());
			getPensionList = this.getForm7PensionInfo(fromDOJ, toYear,
					personalInfo.getPfIDString(), personalInfo
							.getWetherOption(), personalInfo.getRegion(),
					false, dateOfRetriment, personalInfo.getDateOfBirth(),
					personalInfo.getOldPensionNo(), arrearsFlag,
					formRevisedFlag, adjFlag, frmName,pcFlag,personalInfo.getSeperationReason());
			personalInfo.setPensionList(getPensionList);
			dateOfRetriment = "";
			personalInfo.setCount(Count);
			form8List.add(personalInfo);
		}
		return form8List;
	}
	private ArrayList getForm7PensionInfo(String fromDate, String toDate,
			String pfIDS, String wetherOption, String region, boolean formFlag,
			String dateOfRetriment, String dateOfBirth, String pensionNo,
			boolean arrearsFlag, String formRevisedFlag, String adjFlag,
			String frmName,String pcFlag,String sepReason) {
		//log.info("FinanceReportDAO::getForm7PensionInfo");
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		EmployeePensionCardInfo cardInfo = null;
		ArrayList pensionList = new ArrayList();
		boolean flag = false;
		boolean contrFlag = false, chkDOBFlag = false;
		String checkDate = "", chkMnthYear = "Apr-1995", formatMnth = "", chkDecMnthYear = "", findFromYear = "", findFromMnth = "", findToYear = "", tableName = "";
		String monthYear = "", days = "", getMonth = "", condition = "", calPenEmoluments = "", reviseepfemolumentsflag = "", sqlQuery = "", deptuationflag = "N", calEmoluments = "", arrearFromDate = "01-Sep-2009", arrearToDate = "", arrearDatesInfo = "";
		double getDaysBymonth = 0;
		long transMntYear = 0, empRetriedDt = 0;
		log.info("checkDate==" + checkDate + "flag===" + flag);
		double totalAdvancePFWPaid = 0, loanPFWPaid = 0, advancePFWPaid = 0, empNet = 0, aaiNet = 0, advPFDrawn = 0, empCumlative = 0.0, aaiPF = 0.0, aaiNetCumlative = 0.0;
		double pensionAsPerOption = 0.0,tempPensionAsPerOption = 0.0, retriredEmoluments = 0.0,	arrearAmnt = 0.00 , arrearContriAmnt = 0.00,emoluments = 0.0 , pensionContri =0.0,retiredArrearEmoluments=0.0,retiredArrearPensionContri = 0.0 ,aftr58ArrearEmol=0.0,  aftr58ArrearPC=0.0;
		String leaveflag="N";
		String[] arrearBreakupData = null;
		boolean yearBreakMonthFlag = false;
		boolean obFlag = false, fpfFund = false, aftr58ArrearFlag= false;
		boolean yearchkflag = false;
		double pensionVal = 0.0;
		double pensionCOntr1=0.0;
		double emolments=0.0;
		/*
		 * sqlQuery = "SELECT MONTHYEAR,ROUND(EMOLUMENTS) AS
		 * EMOLUMENTS,round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS
		 * EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS
		 * EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS
		 * EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS
		 * CPFADVANCE,ROUND(DEDADV) AS DEDADV,ROUND(REFADV) AS REFADV,
		 * AIRPORTCODE FROM EMPLOYEE_PENSION_VALIDATE WHERE
		 * (to_date(to_char(monthyear,'dd-Mon-yyyy'))>='" + fromDate + "' and
		 * to_date(to_char(monthyear,'dd-Mon-yyyy'))<='" + toDate + "')" + "
		 * AND (" + pfIDS + ") ORDER BY TO_DATE(monthyear)";
		 */
		try {

			findFromYear = commonUtil.converDBToAppFormat(fromDate,
					"dd-MMM-yyyy", "yyyy");
			findFromMnth = commonUtil.converDBToAppFormat(fromDate,
					"dd-MMM-yyyy", "MM");
			findToYear = commonUtil.converDBToAppFormat(toDate, "dd-MMM-yyyy",
					"yyyy");
			yearchkflag = commonUtil.compareTwoDates(commonUtil
					.converDBToAppFormat(fromDate, "dd-MMM-yyyy", "MMM-yyyy"),
					"Mar-2008");
			log.info("getForm7PensionInfo======================="
					+ commonUtil.compareTwoDates(commonUtil
							.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
									"MMM-yyyy"), "Mar-2008"));
		} catch (InvalidDataException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (pensionNo != "" && !pensionNo.equals("")) {
			condition = " pensionno=" + pensionNo;
		}
		boolean arrearData = false;

		ArrayList OBList = new ArrayList();

/*		if (adjFlag.equals("true")) {
		
			if (frmName.equals("adjcorrections")) {
				if(sepReason.trim().equals("Death")){
				
					String	actualDOR = dateOfRetriment;
						dateOfRetriment = getPCMonthsAfterSeperation(pensionNo,actualDOR);
					
				}
			
				tableName = "epis_adj_crtnback";
			} else {
				tableName = "epis_adj_crtn_form78ps";
			}
		} else {*/
			tableName = "employee_freshop_crtn";
		//}

		try {
			con = commonDB.getConnection();

			if (yearchkflag == false) {
				yearBreakMonthFlag = true;
				if (arrearsFlag == false) {
	
					sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,to_char(add_months(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
							+ "SUBSTR(empdt.MONYEAR, 4, 4)),-1),'Mon') as FORMATMONTHYEAR,nvl(emp.EMOLUMENTMONTHS, 1) AS EMOLUMENTMONTHS,nvl(emp.REVISEEPFEMOLUMENTSFLAG,'N') as REVISEEPFEMOLUMENTSFLAG,REVISEEPFEMOLUMENTS,emp.* from (select distinct to_char(to_date('"
							+ fromDate
							+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
							+ "from employee_pension_validate where rownum <=to_date('"
							+ toDate
							+ "', 'dd-mon-yyyy') -to_date('"
							+ fromDate
							+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,EMOLUMENTS AS EMOLUMENTS,"
							+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
							+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG,FORM7NARRATION,NVL(EMOLUMENTMONTHS,'1') AS EMOLUMENTMONTHS,PENSIONCONTRI,PF,NVL(ARREARFLAG,'N') AS ARREARSMONTHS,nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,NVL(ARREARAMOUNT,'0.00') AS ARREARAMOUNT,NVL(DUEEMOLUMENTS,'0.00') AS DUEEMOLUMENTS,OPCHANGEPF As OPCHANGEPF,OPCHANGEPENSIONCONTRI AS OPCHANGEPENSIONCONTRI,NVL(REVISEEPFEMOLUMENTS,0.0) as REVISEEPFEMOLUMENTS, REVISEEPFEMOLUMENTSFLAG,LEAVEFLAG,ecrform4suppliarrarflag,specialemolumentsflag,pensioncontri_b,freshopflag,emoluments_b  FROM "
							+ tableName
							+ "  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
							+ fromDate
							+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
							+ toDate
							+ "'))"
							+ " AND PENSIONNO="
							+ pensionNo
							+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
				} /*else {
					arrearDatesInfo = this.getArrearDate(con, fromDate, toDate,
							pensionNo);
					String[] arrearDts = arrearDatesInfo.split(",");
					arrearFromDate = arrearDts[0];
					arrearToDate = dateOfRetriment;
					if(sepReason.equals("Death")){
						arrearFromDate=fromDate;
					}
					if(sepReason.equals("VRS") || sepReason.equals("Retirement")){
						arrearFromDate=fromDate;
						arrearToDate=arrearDts[1];
						
					}
					sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,to_char(add_months(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
							+ "SUBSTR(empdt.MONYEAR, 4, 4)),-1),'Mon') as FORMATMONTHYEAR,nvl(emp.EMOLUMENTMONTHS, 1) AS EMOLUMENTMONTHS,nvl(emp.REVISEEPFEMOLUMENTSFLAG,'N') as REVISEEPFEMOLUMENTSFLAG,REVISEEPFEMOLUMENTS ,emp.* from (select distinct to_char(to_date('"
							+ fromDate
							+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
							+ "from employee_pension_validate where rownum <=to_date('"
							+ toDate
							+ "', 'dd-mon-yyyy') -to_date('"
							+ fromDate
							+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,EMOLUMENTS AS EMOLUMENTS,"
							+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
							+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG,FORM7NARRATION,NVL(EMOLUMENTMONTHS,'1') AS EMOLUMENTMONTHS,PENSIONCONTRI,PF,NVL(ARREARFLAG,'N') AS ARREARSMONTHS,nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,NVL(ARREARAMOUNT,'0.00') AS ARREARAMOUNT,NVL(DUEEMOLUMENTS,'0.00') AS DUEEMOLUMENTS,OPCHANGEPF As OPCHANGEPF,OPCHANGEPENSIONCONTRI AS OPCHANGEPENSIONCONTRI,NVL(REVISEEPFEMOLUMENTS,0.0) AS REVISEEPFEMOLUMENTS,REVISEEPFEMOLUMENTSFLAG,LEAVEFLAG,ecrform4suppliarrarflag,specialemolumentsflag  FROM "
							+ tableName
							+ "   WHERE ARREARSBREAKUP='"
							+ formRevisedFlag
							+ "' and empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
							+ arrearFromDate
							+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<='"
							+ arrearToDate
							+ "')"
							+ " AND PENSIONNO="
							+ pensionNo
							+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
				}*/

			} else {
				yearBreakMonthFlag = false;
				sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,to_char(add_months(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
						+ "SUBSTR(empdt.MONYEAR, 4, 4)),-1),'Mon') as FORMATMONTHYEAR,nvl(emp.EMOLUMENTMONTHS, 1) AS EMOLUMENTMONTHS,nvl(emp.REVISEEPFEMOLUMENTSFLAG,'N') as REVISEEPFEMOLUMENTSFLAG,REVISEEPFEMOLUMENTS,emp.* from (select distinct to_char(to_date('"
						+ fromDate
						+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
						+ "from employee_pension_validate where rownum <=to_date('"
						+ toDate
						+ "', 'dd-mon-yyyy') -to_date('"
						+ fromDate
						+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,EMOLUMENTS AS EMOLUMENTS,"
						+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
						+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG,FORM7NARRATION,NVL(EMOLUMENTMONTHS,'1') AS EMOLUMENTMONTHS,PENSIONCONTRI,PF,NVL(ARREARFLAG,'N') AS ARREARSMONTHS,DEPUTATIONFLAG,NVL(ARREARAMOUNT,'0.00') AS ARREARAMOUNT,NVL(DUEEMOLUMENTS,'0.00') AS DUEEMOLUMENTS,"
						+ "OPCHANGEPF As OPCHANGEPF,OPCHANGEPENSIONCONTRI AS OPCHANGEPENSIONCONTRI,NVL(REVISEEPFEMOLUMENTS,0.0) AS REVISEEPFEMOLUMENTS,REVISEEPFEMOLUMENTSFLAG,LEAVEFLAG,ecrform4suppliarrarflag,specialemolumentsflag,pensioncontri_b,freshopflag,emoluments_b FROM "
						+ tableName
						+ "   WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
						+ fromDate
						+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
						+ toDate
						+ "'))"
						+ " AND ("
						+ condition
						+ ")) emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
			}
			log.info("getForm7PensionInfo:======" + sqlQuery);
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			/* log.info("yearBreakMonthFlag"+yearBreakMonthFlag+"findFromYear"+findFromYear); */
			while (rs.next()) {
				cardInfo = new EmployeePensionCardInfo();
				double total = 0.0;
				formatMnth = rs.getString("FORMATMONTHYEAR");
				
				if (rs.getString("EMOLUMENTS") != null) {
					cardInfo.setOriginalEmv(rs.getString("EMOLUMENTS"));
				} else {
					cardInfo.setOriginalEmv("0");
				}
				
				if (rs.getString("pensioncontri_b") != null) {
					cardInfo.setPensioncontrib(rs.getString("pensioncontri_b"));
				} else {
					cardInfo.setPensioncontrib("0");
				}
				if (rs.getString("freshopflag") != null) {
					cardInfo.setFreshopflag(rs.getString("freshopflag"));
				} else {
					cardInfo.setFreshopflag("---");
				}
				if (rs.getString("emoluments_b") != null) {
					cardInfo.setEmoluments_b(rs.getString("emoluments_b"));
				} else {
					cardInfo.setEmoluments_b("0");
				}
				
				if (rs.getString("PENSIONCONTRI") != null) {
					cardInfo.setOriginalpensionContri(rs.getString("PENSIONCONTRI"));
				} else {
					cardInfo.setOriginalpensionContri("0");
				}
				
				if (rs.getString("MONTHYEAR") != null) {
					cardInfo.setMonthyear(commonUtil.getDatetoString(rs
							.getDate("MONTHYEAR"), "dd-MMM-yyyy"));
					if(new Date(commonUtil.getCurrentDate("dd-MMM-yyyy")).after(new Date("25-Dec-2011"))){
						cardInfo.setMonthyear("01-"+commonUtil.converDBToAppFormat(cardInfo.getMonthyear(), "dd-MMM-yyyy","MMM-yyyy"));
					}
					
					getMonth = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM");
					chkDecMnthYear = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM-yy");
					cardInfo.setShnMnthYear(formatMnth
							+ "/"
							+ commonUtil.converDBToAppFormat(cardInfo
									.getMonthyear(), "dd-MMM-yyyy", "MMM"));
					if (getMonth.toUpperCase().equals("APR")) {
						obFlag = false;
						getMonth = "";
						empCumlative = 0.0;
						aaiNetCumlative = 0.0;
						advancePFWPaid = 0.0;
						advPFDrawn = 0.0;
						totalAdvancePFWPaid = 0.0;
					}
					if (getMonth.toUpperCase().equals("MAR")) {
						cardInfo.setCbFlag("Y");
					} else {
						cardInfo.setCbFlag("N");
					}
				} else {
					if (rs.getString("EMPMNTHYEAR") != null) {
						cardInfo.setMonthyear(commonUtil.getDatetoString(rs
								.getDate("EMPMNTHYEAR"), "dd-MMM-yyyy"));

					} else {
						cardInfo.setMonthyear("---");
					}

					getMonth = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM");
					chkDecMnthYear = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM-yy");
					cardInfo.setShnMnthYear(commonUtil.converDBToAppFormat(
							cardInfo.getMonthyear(), "dd-MMM-yyyy", "MMM"));

					if (getMonth.toUpperCase().equals("APR")) {
						obFlag = false;
						getMonth = "";
						empCumlative = 0.0;
						aaiNetCumlative = 0.0;
						advancePFWPaid = 0.0;
						advPFDrawn = 0.0;
						totalAdvancePFWPaid = 0.0;
					}
					if (getMonth.toUpperCase().equals("MAR")) {
						cardInfo.setCbFlag("Y");
					} else {
						cardInfo.setCbFlag("N");
					}
					if (!cardInfo.getMonthyear().equals("---")) {
						cardInfo.setShnMnthYear(formatMnth
								+ "/"
								+ commonUtil.converDBToAppFormat(cardInfo
										.getMonthyear(), "dd-MMM-yyyy", "MMM"));
					}

				}

				if (obFlag == false) {

					/*
					 * if(adjFlag.equals("true")){ OBList =
					 * this.getOBForAdjCrtn(con, cardInfo .getMonthyear(),
					 * pensionNo); }else{ OBList =
					 * this.getOBForPFCardReport(con, cardInfo .getMonthyear(),
					 * pensionNo); } cardInfo.setObList(OBList);
					 * cardInfo.setObFlag("Y"); obFlag = true; getMonth = "";
					 */
					cardInfo.setObFlag("N");
					obFlag = true;
				} else {
					cardInfo.setObFlag("N");
				}

				
				if (rs.getString("ARREARAMOUNT") != null) {
					cardInfo.setDuepensionamount(rs.getString("ARREARAMOUNT"));
				} else {
					cardInfo.setDuepensionamount("0");
				}
				log.info("==monthyear======"+cardInfo.getMonthyear()+"==pc="+cardInfo.getDuepensionamount());
				
				if (rs.getString("LEAVEFLAG") != null) {
					leaveflag=rs.getString("LEAVEFLAG");
				} 
				if (rs.getString("DUEEMOLUMENTS") != null) {
					cardInfo.setDueemoluments(rs.getString("DUEEMOLUMENTS"));
				} else {
					cardInfo.setDueemoluments("0");
				}
				if (rs.getString("ARREARSMONTHS") != null) {
					if (rs.getString("ARREARSMONTHS").trim().equals("Y")
							|| rs.getString("ARREARSMONTHS").trim().equals("P")) {
						cardInfo.setTransArrearFlag("Y");

						arrearData = true;
					} else {
						cardInfo.setTransArrearFlag("N");
					}
				} else {
					cardInfo.setTransArrearFlag("N");
				}
				/*if (cardInfo.getTransArrearFlag().equals("Y")) {
					arrearBreakupData = chkArrearBreakup(pensionNo, pfIDS,
							cardInfo.getMonthyear()).split("@");
					arrearAmnt = Double.parseDouble(arrearBreakupData[0]);
					arrearContriAmnt = Double.parseDouble(arrearBreakupData[1]);
					cardInfo.setOringalArrearAmnt(arrearBreakupData[0]);
					cardInfo.setOringalArrearContri(arrearBreakupData[1]);
				}*/

				/*
				 * log.info("monthYear===========sss===========" +
				 * cardInfo.getShnMnthYear() + "ARREARSMONTHS" +
				 * rs.getString("ARREARSMONTHS") + "arrearData" + arrearData +
				 * "Emoluments" + cardInfo.getEmoluments());
				 */
				
				transMntYear = Date.parse(cardInfo.getMonthyear());
				empRetriedDt = Date.parse(dateOfRetriment);

				if (transMntYear > empRetriedDt) {
					contrFlag = true;
				} else if (transMntYear == empRetriedDt) {
					chkDOBFlag = true;
				}
				
				log.info("transMntYear"+transMntYear+"empRetriedDt"+empRetriedDt);
				log.info("cardInfo.getMonthyear()"+cardInfo.getMonthyear()+"dateOfRetriment"+dateOfRetriment);
				/*
				 * log.info("transMntYear" + transMntYear + "empRetriedDt" +
				 * empRetriedDt); log.info("contrFlag" + contrFlag +
				 * "chkDOBFlag" + chkDOBFlag);
				 */
				
				
				if (rs.getString("EMOLUMENTMONTHS") != null) {
					cardInfo.setEmolumentMonths(rs.getString("EMOLUMENTMONTHS"
							.trim()));
				} else {
					cardInfo.setEmolumentMonths("1");
				}
				/*reviseepfemolumentsflag = rs
						.getString("REVISEEPFEMOLUMENTSFLAG");*/
				reviseepfemolumentsflag = "N";
				
				if (rs.getString("EMOLUMENTS") != null) {
					 emoluments = rs.getDouble("EMOLUMENTS");
				} else {
					emoluments = 0.0;
				}
				//log.info("emoluments"+emoluments+cardInfo.getTransArrearFlag());
				
				/*if (rs.getString("EMOLUMENTS") != null) {
					if (reviseepfemolumentsflag.equals("Y")) {
						cardInfo.setEmoluments(rs
								.getString("REVISEEPFEMOLUMENTS"));
					} else {
						cardInfo.setEmoluments(rs.getString("EMOLUMENTS"));
					}

				} else {
					cardInfo.setEmoluments("0");
				}
				// reviseepfemolumentsflag=""; 
				if (reviseepfemolumentsflag.equals("N")) {
					calEmoluments = this.calWages(cardInfo.getEmoluments(),
							cardInfo.getMonthyear(), wetherOption.trim(),
							false, false, cardInfo.getEmolumentMonths());
				} else {

					calEmoluments = cardInfo.getEmoluments();

				}*/
				//log.info("chkDOBFlagchkDOBFlagchkDOBFlagchkDOBFlag=========="+chkDOBFlag+"cardInfo.getTransArrearFlag()"+cardInfo.getTransArrearFlag());
				if(chkDOBFlag==true && cardInfo.getTransArrearFlag().equals("Y")){
					retiredArrearEmoluments = emoluments - arrearAmnt;
					retiredArrearPensionContri = pensionContri - arrearContriAmnt;  
					cardInfo.setEmoluments(String.valueOf(retiredArrearEmoluments));
					calEmoluments = cardInfo.getEmoluments();
				}else{ 
						if (reviseepfemolumentsflag.equals("Y")) {
							
							cardInfo.setEmoluments(rs
									.getString("REVISEEPFEMOLUMENTS"));
						} else {
							cardInfo.setEmoluments(String.valueOf(emoluments));
						} 
						

						if (reviseepfemolumentsflag.equals("N")) {
							calEmoluments = this.calWages(cardInfo.getEmoluments(),
									cardInfo.getMonthyear(), wetherOption.trim(),
									false, false, cardInfo.getEmolumentMonths());
						} else {

							calEmoluments = cardInfo.getEmoluments();

						}
				}
				
				if (chkDecMnthYear.trim().equals("Dec-95")) {
					if (!calEmoluments.equals("")) {
						calEmoluments = Double.toString(Double
								.parseDouble(calEmoluments) / 2);
					}

				}
				//log.info("monthYear======================"+
				 //cardInfo.getShnMnthYear() + "calEmoluments"+ calEmoluments);
				if (rs.getString("EMPPFSTATUARY") != null) {
					cardInfo.setEmppfstatury(rs.getString("EMPPFSTATUARY"));
				} else {
					cardInfo.setEmppfstatury("0");
				}
				if (rs.getString("EMPVPF") != null) {
					cardInfo.setEmpvpf(rs.getString("EMPVPF"));
				} else {
					cardInfo.setEmpvpf("0");
				}
				if (rs.getString("CPF") != null) {
					cardInfo.setEmpCPF(rs.getString("CPF"));
				} else {
					cardInfo.setEmpCPF("0");
				}
				if (rs.getString("PENSIONCONTRI") != null) {
					pensionVal = rs.getDouble("PENSIONCONTRI");
				} else {
					pensionVal = 0.0;
				}
				if (rs.getString("DEPUTATIONFLAG") != null) {
					deptuationflag = rs.getString("DEPUTATIONFLAG");
				}

				if (region.equals("North Region")) {
					if (rs.getString("REFADV") != null) {
						cardInfo.setPrincipal(rs.getString("REFADV"));
					} else {
						cardInfo.setPrincipal("0");
					}
				} else {
					if (rs.getString("EMPADVRECPRINCIPAL") != null) {
						cardInfo.setPrincipal(rs
								.getString("EMPADVRECPRINCIPAL"));
					} else {
						cardInfo.setPrincipal("0");
					}
				}
				if (rs.getString("EMPADVRECINTEREST") != null) {
					cardInfo.setInterest(rs.getString("EMPADVRECINTEREST"));
				} else {
					cardInfo.setInterest("0");
				}
				try {
					checkDate = commonUtil.converDBToAppFormat(cardInfo
							.getMonthyear(), "dd-MMM-yyyy", "MMM-yyyy");
					flag = false;
				} catch (InvalidDataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// log.info(checkDate + "chkMnthYear===" + chkMnthYear);
				if (checkDate.toLowerCase().equals(chkMnthYear.toLowerCase())) {
					flag = true;
				}
				/*
				 * total = new Double(df.format(Double.parseDouble(cardInfo
				 * .getEmppfstatury().trim()) +
				 * Double.parseDouble(cardInfo.getEmpvpf().trim()) +
				 * Double.parseDouble(cardInfo.getPrincipal().trim()) +
				 * Double.parseDouble(cardInfo.getInterest().trim())))
				 * .doubleValue(); if (formFlag == true) { if
				 * (region.equals("CHQIAD")) {
				 * 
				 * loanPFWPaid = this .getEmployeeLoans(con, cardInfo
				 * .getShnMnthYear(), pfIDS, "ADV.PAID", pensionNo);
				 * advancePFWPaid = this .getEmployeeAdvances(con, cardInfo
				 * .getShnMnthYear(), pfIDS, "ADV.PAID", pensionNo);
				 * 
				 * totalAdvancePFWPaid = loanPFWPaid + advancePFWPaid; } else if
				 * (region.equals("CHQNAD")) { loanPFWPaid = this
				 * .getEmployeeLoans(con, cardInfo .getShnMnthYear(), pfIDS,
				 * "ADV.PAID", pensionNo); advancePFWPaid = this
				 * .getEmployeeAdvances(con, cardInfo .getShnMnthYear(), pfIDS,
				 * "ADV.PAID", pensionNo); totalAdvancePFWPaid = loanPFWPaid +
				 * advancePFWPaid; } else if (region.equals("North Region")) {
				 * if (rs.getString("DEDADV") != null) { advancePFWPaid =
				 * Double.parseDouble(rs .getString("DEDADV"));
				 * totalAdvancePFWPaid = advancePFWPaid; } else { advancePFWPaid =
				 * 0; totalAdvancePFWPaid = advancePFWPaid; }
				 *  } }
				 *  /* log.info("cardInfo.getShnMnthYear()" +
				 * cardInfo.getShnMnthYear() + "advancePFWPaid" +
				 * advancePFWPaid);
				 */
				/*
				 * empNet = total - totalAdvancePFWPaid;
				 * 
				 * cardInfo.setEmptotal(Double.toString(Math.round(total)));
				 * cardInfo.setAdvancePFWPaid(Double.toString(Math
				 * .round(totalAdvancePFWPaid)));
				 * cardInfo.setEmpNet((Double.toString(Math.round(empNet))));
				 * empCumlative = empCumlative + empNet;
				 * cardInfo.setEmpNetCummulative(Double.toString(empCumlative));
				 */
				
				if (rs.getString("AAICONPF") != null) {
					cardInfo.setAaiPF(rs.getString("AAICONPF"));
				} else {
					cardInfo.setAaiPF("0");
				}
				// By Radha On 05-Jun-2012 for making emolumentMonths as 1 as already wages are calculating on Days for Option B having calWages<6500 Ex Pfid 975
				if(wetherOption.trim().equals("B") || wetherOption.trim().equals("NO")){
					 if(Double.parseDouble(calEmoluments)<6500){
						 cardInfo.setEmolumentMonths("1");        
					 }  
				 }
				fpfFund = commonUtil.compareTwoDates(commonUtil
						.converDBToAppFormat(cardInfo.getMonthyear(),
								"dd-MMM-yyyy", "MMM-yyyy"), "Jan-1996");
                log.info("yearBreakMonthFlag"+yearBreakMonthFlag);
                yearBreakMonthFlag=false;
                
               // if (yearBreakMonthFlag=false) {
					
				//} 
				
                
				if (yearBreakMonthFlag == false) {

					if (flag == false) {
						if (!cardInfo.getMonthyear().equals("-NA-")
								&& !dateOfRetriment.equals("")) {

							if (contrFlag != true) {
								if (deptuationflag.equals("Y")) {
									pensionAsPerOption = rs
											.getDouble("PENSIONCONTRI");
								} else {
									if (chkDecMnthYear.trim().equals("Dec-95")) {
										calPenEmoluments = cardInfo
												.getEmoluments();
									} else {
										calPenEmoluments = calEmoluments;
									}
 									 //By Radha On 05-Jun-2012 Removing Hardcode for EmolumentMnths
									//By Radha On 18-May-2012 for making emolumentMonths as 1 as already wages are calculating on Days
									pensionAsPerOption = commonDAO
											.pensionCalculation(
													cardInfo.getMonthyear(),
													calPenEmoluments,
													wetherOption,
													region,cardInfo.getEmolumentMonths());
									tempPensionAsPerOption=pensionAsPerOption;
									if (chkDOBFlag == true) {
										String[] dobList = dateOfBirth
												.split("-");
										days = dobList[0];
										if(leaveflag.equals("Y")){
											getDaysBymonth = this
											.getNoOfDays(con,pensionNo,cardInfo.getMonthyear(),dateOfBirth);
										}else{
											getDaysBymonth = commonDAO
											.getNoOfDays(dateOfBirth);
										}
										
										pensionAsPerOption = pensionAsPerOption
												* (Double.parseDouble(days) - 1)
												/ getDaysBymonth;
										retriredEmoluments = new Double(
												df1
														.format(Double
																.parseDouble(calEmoluments)
																* (Double
																		.parseDouble(days) - 1)
																/ getDaysBymonth))
												.doubleValue();
										log.info("reached 58 years"+retriredEmoluments+"pensionAsPerOption"+pensionAsPerOption);
								/*		if(Double.parseDouble(days)==1 && adjFlag.equals("true")){
											pensionAsPerOption = tempPensionAsPerOption;
											retriredEmoluments = Double.parseDouble(calEmoluments);
											log.info("reached 58 years 1st month of day"+retriredEmoluments+"pensionAsPerOption"+pensionAsPerOption);
										}*/

										calEmoluments = Double
												.toString(retriredEmoluments);
									}
								}
							} else {
								pensionAsPerOption = 0;
								calEmoluments = "0";
							}
							cardInfo.setPensionContribution(Double
									.toString(pensionAsPerOption));
						} else {
							cardInfo.setPensionContribution("0");
						}
					} else {
						cardInfo.setPensionContribution("0");
					}
					if (deptuationflag.equals("Y")) {
						
						pensionAsPerOption = rs.getDouble("PENSIONCONTRI");
						cardInfo.setPensionContribution(Double
								.toString(pensionAsPerOption));
					}

					
					
				} else {
					pensionAsPerOption = rs.getDouble("PENSIONCONTRI");
					if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
						pensionAsPerOption = rs
								.getDouble("OPCHANGEPENSIONCONTRI");
					}
					log.info("==================verfiy condition================================"+pensionAsPerOption+contrFlag+"chkDOBFlag"+chkDOBFlag+pensionAsPerOption+"calEmoluments"+calEmoluments);
					if (contrFlag != true) {
						if(leaveflag.equals("Y")){
							getDaysBymonth = this
							.getNoOfDays(con,pensionNo,cardInfo.getMonthyear(),dateOfBirth);
						}else{
							getDaysBymonth = commonDAO
							.getNoOfDays(dateOfBirth);
						}
						log.info("==================getDaysBymonth================================"+getDaysBymonth);
						 if(!cardInfo.getTransArrearFlag().equals("Y")){
						if (chkDOBFlag == true
								&& (reviseepfemolumentsflag.equals("N"))) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];

						
							
							retriredEmoluments = new Double(df1.format(Double
									.parseDouble(calEmoluments)
									* (Double.parseDouble(days) - 1)
									/ getDaysBymonth)).doubleValue();
				/*			if(Double.parseDouble(days)==1 &&  (adjFlag.equals("true"))){
								//log.info("==================verfiy condition================================"+pensionAsPerOption);
								pensionAsPerOption = pensionAsPerOption;
								if(pensionAsPerOption==0.00){
									retriredEmoluments = 0.00;
								}else{
									retriredEmoluments = Double.parseDouble(calEmoluments);
								}
							}*/
							//log.info("==================After verfiy condition================================"+calEmoluments+"pensionAsPerOption=========="+pensionAsPerOption);
							 
							//	By Radha On 03-Aug-2012 For Getting Normal Month Wages  not treating the last month as DOR  
							 if(pcFlag.equals("false")){
								 calEmoluments = Double.toString(retriredEmoluments);
							 }							 
							 log.info("=====pcFlag==="+pcFlag+"===calEmoluments"+calEmoluments);
						}
					}else{
						/*If Arrear came at 58 Years Ex case 16161*/
						if (chkDOBFlag == true && adjFlag.equals("false")){
							 String[] dobList = dateOfBirth.split("-");
								days = dobList[0];

if(reviseepfemolumentsflag.equals("Y")){
	retriredEmoluments=Double.parseDouble(rs.getString("REVISEEPFEMOLUMENTS"));
}else{
								retriredEmoluments = new Double(df1.format(Double
										.parseDouble(calEmoluments)
										* (Double.parseDouble(days) - 1)
										/ getDaysBymonth)).doubleValue();
}
								calEmoluments = Double.toString(Math.round(retriredEmoluments));
 								 //By Radha On 05-Jun-2012 Removing Hardcode for EmolumentMnths
								//By Radha On 18-May-2012 for making emolumentMonths as 1 as already wages are calculating on Days
								pensionAsPerOption = commonDAO
								.pensionCalculation(
										cardInfo.getMonthyear(),
										calEmoluments,
										wetherOption,
										region,cardInfo.getEmolumentMonths());
								log.info("==calEmoluments"+calEmoluments+"-====arrearAmnt"+arrearAmnt+"pensionAsPerOption:"
										+pensionAsPerOption+"arrearContriAmnt:"+arrearContriAmnt);
								calEmoluments = Double.toString(retriredEmoluments + arrearAmnt);
								cardInfo.setEmoluments(String.valueOf(calEmoluments));
								pensionAsPerOption = Math.round(pensionAsPerOption) + arrearContriAmnt;
							 } 
					}
					} else {
						
						//By Radha On 06-Nov-2012 Getting ArrearData after 58 Years when changes are done thru Ompact Calc. Req By Sehgal
						
						if(adjFlag.equals("true")  && frmName.equals("adjcorrections") && arrearData == true){
							aftr58ArrearEmol = Double.parseDouble(calEmoluments);
							aftr58ArrearPC = pensionAsPerOption;
							aftr58ArrearFlag = true;
						}
						
						if (arrearData == true) {
							arrearData = false;
							//cardInfo.setOringalArrearAmnt("0.00");
							//cardInfo.setOringalArrearContri("0.00");
							cardInfo.setOringalArrearAmnt(arrearBreakupData[0]);
							cardInfo.setOringalArrearContri(arrearBreakupData[1]);
						}
						if (arrearData == false) {
							//code changed on 6-10-2016 for form-4(7ps)
							//pensionAsPerOption = 0;
							//calEmoluments = "0";
							pensionAsPerOption = pensionAsPerOption;
							
							if(reviseepfemolumentsflag.equals("Y")){
								calEmoluments=rs.getString("REVISEEPFEMOLUMENTS");
							}else {
								calEmoluments = Long.toString(Math.round(pensionAsPerOption*100/8.33));
							}
								
						} 
						if(adjFlag.equals("true")  && frmName.equals("adjcorrections") && aftr58ArrearFlag == true){
							arrearData = true;
							calEmoluments =Double.toString(aftr58ArrearEmol);
							pensionAsPerOption = aftr58ArrearPC;
							cardInfo.setOringalArrearAmnt(arrearBreakupData[0]);
							cardInfo.setOringalArrearContri(arrearBreakupData[1]);
						}
					}
					cardInfo.setPensionContribution(Double
							.toString(pensionAsPerOption));
				}
				//emolments=rs.getString("EMOLUMENTS");
				log.info("emolments:::"+emolments);
				log.info("emolments:::"+cardInfo.getEmoluments());
				if (contrFlag != true) {
					log.info("yearBreakMonthFlag"+yearBreakMonthFlag);
					pensionCOntr1 = commonDAO.pensionCalculation(
							cardInfo.getMonthyear(),
							cardInfo.getEmoluments(),
							"A",
							region,cardInfo.getEmolumentMonths());
					if (chkDOBFlag == true) {
						String[] dobList = dateOfBirth.split("-");
						days = dobList[0];
						getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
						pensionCOntr1 = Math.round(pensionCOntr1
								* (Double.parseDouble(days) - 1)
								/ getDaysBymonth);

					}
				} else {
					pensionCOntr1 = 0;
				}
				log.info("pensionCOntr1::::"+pensionCOntr1);
				cardInfo.setOriginalpensionContri(Double.toString(pensionCOntr1));
				
				
				if (fpfFund == true) {
					if (!chkDecMnthYear.trim().equals("Dec-95")) {
						cardInfo.setFpfFund(df.format(Math
								.round(pensionAsPerOption / 2)));
					} else {
 						 //By Radha On 05-Jun-2012 Removing Hardcode for EmolumentMnths
						//By Radha On 18-May-2012 for making emolumentMonths as 1 as already wages are calculating on Days
						pensionAsPerOption = commonDAO.pensionFormsCalculation(
								cardInfo.getMonthyear(), cardInfo
										.getEmoluments(), wetherOption, region,
								true, false,cardInfo.getEmolumentMonths());
						cardInfo.setFpfFund(df.format(Math
								.round(pensionAsPerOption / 2)));
					}
				} else {
					cardInfo.setFpfFund("0");
				}
				/*
				 * log.info("Month Year================" +
				 * cardInfo.getMonthyear() + "flag" + flag + checkDate +
				 * "Pension" + cardInfo.getPensionContribution() +
				 * "calEmoluments" + calEmoluments);
				 */
				
				if (rs.getString("ecrform4suppliarrarflag") != null) {					
					cardInfo.setJunkflag(rs.getString("ecrform4suppliarrarflag"));
				} else {
					cardInfo.setJunkflag("");
				}
				if (arrearData == false) {
					cardInfo.setEmoluments(calEmoluments);
				} else {
					if (pensionAsPerOption == 541) {
						cardInfo.setEmoluments("6500");
					} else {
						if(cardInfo.getJunkflag().equals("J")) {
							cardInfo.setEmoluments("0");
						}else{
						cardInfo.setEmoluments(df.format(Math
								.round(pensionAsPerOption * 100 / 8.33)));
						}
					}
				}

				/*
				 * if (formFlag == true) { if (region.equals("CHQIAD")) {
				 * advPFDrawn = commonDAO.getEmployeeLoans(con, cardInfo
				 * .getShnMnthYear(), pfIDS, "ADV.DRAWN", pensionNo); } else if
				 * (region.equals("CHQNAD")) { advPFDrawn =
				 * commonDAO.getEmployeeLoans(con, cardInfo .getShnMnthYear(), pfIDS,
				 * "ADV.DRAWN", pensionNo); } else if (region.equals("North
				 * Region")) { advPFDrawn = 0; } }
				 */
				if (yearBreakMonthFlag == false) {
					aaiPF = (Double.parseDouble(cardInfo.getEmppfstatury()) - pensionAsPerOption);
				} else {
					aaiPF = rs.getDouble("PF");
					if (rs.getString("OPCHANGEPF") != null) {
						aaiPF = rs.getDouble("OPCHANGEPF");
					}
				}

				cardInfo.setAaiPF(Double.toString(aaiPF));
				/*
				 * cardInfo.setPfDrawn(Double.toString(advPFDrawn)); aaiNet =
				 * Double.parseDouble(cardInfo.getAaiPF()) - advPFDrawn;
				 * aaiNetCumlative = aaiNetCumlative + aaiNet;
				 * cardInfo.setAaiNet(Double.toString(aaiNet));
				 * cardInfo.setAaiCummulative(Double.toString(aaiNetCumlative));
				 */
				if (rs.getString("FORM7NARRATION") != null) {
					cardInfo.setForm7Narration(rs.getString("FORM7NARRATION"));
				} else {
					cardInfo.setForm7Narration("---");
				}
				if (rs.getString("EMOLUMENTMONTHS") != null) {
					cardInfo
							.setEmolumentMonths(rs.getString("EMOLUMENTMONTHS"));
				} else {
					cardInfo.setEmolumentMonths("1");
				}
				if (rs.getString("AIRPORTCODE") != null) {
					cardInfo.setStation(rs.getString("AIRPORTCODE"));
				} else {
					cardInfo.setStation("---");
				}
				arrearData = false;
				aftr58ArrearFlag = false;
				// new code snippet added as on Sep-02
				if (rs.getString("EMOLUMENTS") != null) {
					cardInfo.setOriginalEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					cardInfo.setOriginalEmoluments("0");
				}				
				if (rs.getString("specialemolumentsflag") != null) {
					cardInfo.setSpecialEmolumentsFlag(rs.getString("specialemolumentsflag"));
				} else {
					cardInfo.setSpecialEmolumentsFlag("");
				}
				if(cardInfo.getSpecialEmolumentsFlag().equals("Y")){
					if(reviseepfemolumentsflag.equals("Y")){
						cardInfo.setEmoluments(rs.getString("REVISEEPFEMOLUMENTS"));
					}
					
				}
				
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
	private ArrayList getForm7EmployeeInfo(String range, String sortedColumn,
			String region, String fromYear, String toYear, String airportCode,
			String pensionNo, String empNameFlag, String empName,
			String arrears, String formType, String formRevisedFlag,String adjFlag) {

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
		
		//if(adjFlag.equals("true")){
			/*chkForArrearAfter58Flag = chkForArrearAfter58(pensionNo, fromYear,toYear);*/
			//}
					
		log.info("formRevisedFlag=================" + formRevisedFlag+"arrears==============="+arrears);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			if (formRevisedFlag.equals("N") || formRevisedFlag.equals("")) {
				if (arrears.equals("N")) {
						sqlQuery = this.buildQuerygetEmployeePFInfoPrinting(range,
								region, empNameFlag, empName, sortedColumn,
								pensionNo, toYear, fromYear); 
				}
			}

			log.info("FinanceReportDAO::getForm7EmployeeInfo" + sqlQuery);
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				data = new EmployeePersonalInfo();
				CommonDAO commonDAO = new CommonDAO();
				data = commonDAO.loadPersonalInfo(rs);
				if (Integer.parseInt(findFromYear) >= 2008) {
					pfidWithRegion = "";
				} else {
					pfidWithRegion = this.getEmployeeForm7MappingPFInfo(data
							.getOldPensionNo(), data.getCpfAccno(), data
							.getRegion());
				}

				/*if(adjFlag.equals("true")){
					pfidWithRegion = this.getEmployeeForm7MappingPFInfoForCrtn(data
							.getOldPensionNo(), data.getCpfAccno(), data
							.getRegion());
				}*/


				if (!pfidWithRegion.equals("")) {
					String[] pfIDLists = pfidWithRegion.split("=");
					data.setPfIDString(commonDAO.preparedCPFString(pfIDLists));
				}
				if (rs.getString("CHKWTHRARRNOT") != null) {
					data.setChkarrearAdj((rs.getString("CHKWTHRARRNOT")));
					arrears = data.getChkarrearAdj();
				}
				data.setCurrentage(rs.getDouble("currentage"));
				if (data.getWetherOption().trim().equals("---")) {
					data.setRemarks("Wether Option is not available");
				}
				//if ((data.getWetherOption().trim().equals("DB")||data.getWetherOption().trim().equals("N"))&& adjFlag.equals("true")) {
					data.setWetherOption("B");
				//}
				

				long noOfYears = 0;
				noOfYears = rs.getLong("ENTITLEDIFF");

				if (noOfYears > 0) {
					data.setDateOfEntitle(data.getDateOfJoining());
				} else {
					data.setDateOfEntitle("01-Apr-1995");
				}
				if (rs.getString("ARREARFORMDATE") != null) {
					data.setFromarrearreviseddate(commonUtil
							.converDBToAppFormat(rs.getDate("ARREARFORMDATE"),
									"dd-MMM-yyyy"));
				} else {
					data.setFromarrearreviseddate("---");
				}
				
				if (rs.getString("ARREARTODATE") != null) {
					data.setToarrearreviseddate(commonUtil.converDBToAppFormat(
							rs.getDate("ARREARTODATE"), "dd-MMM-yyyy"));
				} else {
					data.setToarrearreviseddate("---");
				}
				if (rs.getString("GETARREARREVISEDFLAG") != null) {
					data.setArrearRevisedFlag(rs
							.getString("GETARREARREVISEDFLAG"));
				} else {
					data.setArrearRevisedFlag("N");
				}
				/*
				 * if (rs.getString("DATEOFENTITLE") != null) {
				 * data.setDateOfEntitle(rs.getString("DATEOFENTITLE"));
				 * chkDOE=commonUtil
				 * .converDBToAppFormat(data.getDateOfEntitle(), "dd-MMM-yyyy",
				 * "dd-MM-yyyy");
				 * 
				 * String[] chckDOFArray=chkDOE.split("-");
				 * log.info("chkDOE"+chkDOE+""+chckDOFArray.length);
				 * if(Integer.parseInt(chckDOFArray[0])==30 ||
				 * Integer.parseInt(chckDOFArray[0])==31){
				 *  } if(Integer.parseInt(chckDOFArray[0])>1 &&
				 * Integer.parseInt(chckDOFArray[1])==03){
				 * data.setDateOfEntitle("01-Apr-"+chckDOFArray[2]); } } else {
				 * data.setDateOfEntitle("---"); }
				 */
				//CALCPCUPTODATE is for identifying the next  month after Serperation date for calc PC upto that month
				if (rs.getString("CALCPCUPTODATE") != null) {					 
					data.setCalcPCUptoDate(rs.getString("CALCPCUPTODATE"));
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {	
					data.setDateOfSaparation(commonUtil.converDBToAppFormat(
							rs.getDate("DATEOFSEPERATION_DATE"), "dd-MMM-yyyy"));
					//data.setDateOfSaparation(rs.getString(""));
				}else {
					data.setDateOfSaparation("---");
				}
				
				if (rs.getString("SEPER_FLAG") != null) {
					seperationFlag = rs.getString("SEPER_FLAG");
				}
				if (rs.getInt("EMPAGE1") >= 58 && rs.getInt("EMPAGE1") <= 59) {
					remarks = "Date of Leaving service:"
							+ rs.getString("LASTATTAINED")
							+ "<br/>Reason for Leaving Service:Attained 58 years";
				}
				if(rs.getString("LASTATTAINED")!= null){
					data.setLastatteained(rs.getString("LASTATTAINED"));
				}
				if (rs.getString("ARREARDATE") != null) {
					arrearInfo = rs.getString("ARREARDATE");
				}
				if (rs.getString("UANNO") != null) {					 
					data.setUanno(rs.getString("UANNO"));
				}else{
					data.setUanno("---");
				}
				//By Radha On 03-Aug-2012 for Getting Reason
				data.setSeperationReason_PC(data.getSeperationReason());
				data.setSeperationDate_PC(data.getSeperationDate());
				
				if (seperationFlag.equals("N")) {
					data.setSeperationReason("---");
					data.setSeperationDate("---");
				}

				if (rs.getString("REMARKS") != null && !arrears.equals("N")
						&& formType.equals("Summary")) {
					if (commonDAO.compareFinalSettlementDates(fromYear, toYear,
							arrearInfo) == true) {
						remarks = remarks + rs.getString("REMARKS");
					}
				}
				if (data.getSeperationReason().trim().equals("Resignation")
						|| data.getSeperationReason().trim().equals(
								"Termination")) {
					remarks = remarks + data.getSeperationReason() + " On"
							+ data.getSeperationDate();
				} else if (data.getSeperationReason().trim().equals("Death")) {
					remarks = remarks + "Death Case & PF settled";
				} else if (data.getSeperationReason().trim().equals(
						"Retirement")) {
					remarks = remarks + "Retirement Case & PF settled";
				}
				data.setRemarks(remarks);
				log.info("getForm7EmployeeInfo============Employee Name"
						+ data.getEmployeeName() + "Seperation Reason"
						+ data.getSeperationReason() + "Arrear Condition"
						+ "remarks" + remarks);
				if (!payrevisionarrear.equals("DONTADD")) {
					empinfo.add(data);
				}

				remarks = "";
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
	private double getNoOfDays(Connection con,String pensionno,String monthyear,String dateOfBirth) {
		String sqlQuery = "";
	
		Statement st = null;
		ResultSet rs = null;
		int days = 0;
		double leaves=0.00,totaldays=0.00;
		sqlQuery =  "SELECT SUM(NVL(NOOFLEAVES,0.00)) AS LEAVES,TO_CHAR(LAST_DAY(ADD_MONTHS('" + dateOfBirth+"',696)),'dd') as days  FROM employee_monthly_leaves WHERE PENSIONNO="+pensionno+" AND MONTHYEAR='"+monthyear+"' AND LEAVERESTRICTION='Y'";

		log.info("FinanceReportDAO::LEVAE TYPE getNoOfDays" + sqlQuery);
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			
			if (rs.next()) {
				leaves = rs.getDouble("LEAVES");
				days = rs.getInt("days");
			}
			if (days == 29) {
				days = 29;
			}
			totaldays=days-leaves;
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return totaldays;
	}
	/*public String getPCMonthsAfterSeperation(String pensionno, String dateOfRetriment) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String lastMonthForPC = "";
		int minYear = 0, maxYear = 0;
		boolean breakFlag = false;
		String sqlQuery = "select  to_char(max(monthyear),'dd-Mon-yyyy') as lastMonthForPC   from employee_pension_validate where pensionno="
				+ pensionno+" and  monthyear>='"+dateOfRetriment+"' and ( pensioncontri is not null and to_number(pensioncontri)!=0) and arrearflag='N'";
		
		log.info("==getPCMonthsAfterSeperation====="+sqlQuery);
		try {
			con = commonDB.getConnection();

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				 if(rs.getString("lastMonthForPC")!=null){
					 lastMonthForPC = rs.getString("lastMonthForPC");
				 }else{
					 lastMonthForPC="";
				 }
				 if(lastMonthForPC.equals("")){
					 lastMonthForPC = dateOfRetriment;
				 }
			}
			 log.info("========lastMonthForPC======="+lastMonthForPC);
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return lastMonthForPC;

	}*/
/*	private String chkArrearBreakup(String pensionno, String pfidstrip,
			String monthyear) {
		String arrearBreakUpData;
		arrearBreakUpData = getArrearsWithOutBreakup(pensionno, pfidstrip,
				monthyear);
		if (arrearBreakUpData.equals("")) {
			arrearBreakUpData = getArrearsBreakUp(pensionno, pfidstrip,
					monthyear, monthyear);
		}
		return arrearBreakUpData;

	}*/
	/*public String getArrearDate(Connection con, String fromYear, String toYear,
			String pensionno) {
		String arrearFromDate = "", arrearToDate = "", year = "";
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		try {
			year = commonUtil.converDBToAppFormat(fromYear, "dd-MMM-yyyy",
					"yyyy");
		} catch (InvalidDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			st = con.createStatement();
			if (Integer.parseInt(year) == 2009) {
				sqlQuery = "SELECT TO_CHAR(MONTHYEAR,'dd-Mon-yyyy') as ARREARFROMDATE,TO_CHAR(LAST_DAY(MONTHYEAR),'dd-Mon-yyyy') as ARREARTODATE FROM EMPLOYEE_PENSION_VALIDATE WHERE (NVL(PENSIONCONTRI,0.00)!=0.00 ) AND (ARREARFLAG='Y' OR EDITTRANS='Y') AND EMPFLAG='Y' AND MONTHYEAR BETWEEN '"
						+ fromYear
						+ "' AND '"
						+ toYear
						+ "' AND PENSIONNO="
						+ pensionno;
			} else {
				sqlQuery = "SELECT TO_CHAR(max(MONTHYEAR),'dd-Mon-yyyy') as ARREARFROMDATE,TO_CHAR(LAST_DAY(max(MONTHYEAR)),'dd-Mon-yyyy') as ARREARTODATE FROM EMPLOYEE_PENSION_VALIDATE WHERE (NVL(PENSIONCONTRI,0.00)!=0.00 ) AND (ARREARFLAG='Y') AND EMPFLAG='Y' AND MONTHYEAR BETWEEN '"
						+ fromYear
						+ "' AND '"
						+ toYear
						+ "' AND PENSIONNO="
						+ pensionno;
			}

			log.info("FinanceReportDAO::getEmployeePersonalInfo" + sqlQuery);
			rs = st.executeQuery(sqlQuery);

			
			 * while (rs.next() && i<2) { i++; if
			 * (rs.getString("ARREARFROMDATE") != null) {
			 * arrearFromDate=rs.getString("ARREARFROMDATE");
			 * arrearToDate=rs.getString("ARREARTODATE"); } else {
			 * arrearFromDate=fromYear; arrearToDate=toYear; }
			 *  } if(i!=1){ arrearFromDate=fromYear; arrearToDate=toYear; }
			 
			if (rs.next()) {
				if (rs.getString("ARREARFROMDATE") != null) {
					arrearFromDate = rs.getString("ARREARFROMDATE");
					arrearToDate = rs.getString("ARREARTODATE");
				} else {
					arrearFromDate = fromYear;
					arrearToDate = toYear;
				}

			} else {
				arrearFromDate = fromYear;
				arrearToDate = toYear;
			}
			buffer.append(arrearFromDate);
			buffer.append(",");
			buffer.append(arrearToDate);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return buffer.toString();

	}*/
	private String getArrearsWithOutBreakup(String pensionno, String pfidstrip,
			String monthyear) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		String arrearBreakUpData = "";
		double totalEmoluments = 0.00, totalArrearAmount = 0.00;
		boolean breakFlag = false;
		try {
			con = commonDB.getConnection();
			sqlQuery = "SELECT ORIGINALARREARAMOUNT AS ORIGINALARREARAMOUNT,ORIGINALARREARCONTRIBUTION AS ORIGINALARREARCONTRIBUTION   FROM EMPLOYEE_PENSION_ARREAR PV  WHERE PV.PENSIONNO = "
					+ pensionno
					+ " AND PV.ARREARDATE between '"
					+ monthyear
					+ "' and last_day('" + monthyear + "')";
			log.info("==============Get Arrears Without Breakup=============="
					+ sqlQuery);
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				totalEmoluments = rs.getDouble("ORIGINALARREARAMOUNT");
				totalArrearAmount = rs.getDouble("ORIGINALARREARCONTRIBUTION");
				breakFlag = true;
			}
			if (breakFlag == true) {
				if (totalArrearAmount == 541) {
					arrearBreakUpData = "6500@541";
				} else {
					arrearBreakUpData = Double.toString(Math
							.round(totalArrearAmount * 100 / 8.33))
							+ "@" + Double.toString(totalArrearAmount);
				}

			} else {
				arrearBreakUpData = "";
			}

		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return arrearBreakUpData;
	}

/*	private String getArrearsBreakUp(String pensionno, String pfidstrip,
			String fromDate, String toDate) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		String arrearBreakUpData = "";
		double totalDueEmoluments = 0.00, totalArrearAmount = 0.00;

		try {
			con = commonDB.getConnection();
			sqlQuery = "SELECT finyear,SUM(DUEEMOLUMENTS) AS DUEEMOLUMENTS,SUM(ARREARAMOUNT) AS ARREARAMOUNT   FROM EMPLOYEE_PENSION_VALIDATE PV, employee_arrear_breakup RF  WHERE PV.PENSIONNO = "
					+ pensionno
					+ " AND PV.ARREARSBREAKUP = 'Y' and PV.PENSIONNO = RF.PENSIONNO and pv.monthyear between rf.arrearformdate and RF.Arreartodate group by finyear";

			log.info("==============Get Arrears Breakup=============="
					+ sqlQuery);
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				totalDueEmoluments = totalDueEmoluments
						+ rs.getDouble("DUEEMOLUMENTS");
				totalArrearAmount = totalArrearAmount
						+ rs.getDouble("ARREARAMOUNT");
			}
			arrearBreakUpData = Double.toString(totalDueEmoluments) + "@"
					+ Double.toString(totalArrearAmount);
		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return arrearBreakUpData;
	}*/
	/*private String getEmployeeForm7MappingPFInfoForCrtn(String personalPFID,
			String personalCPFACCNO, String personalRegion) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfID = "", region = "", regionPFIDS = "";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			sqlQuery = "SELECT CPFACNO,REGION FROM epis_info_adj_crtn WHERE EMPSERIALNUMBER='"
					+ personalPFID + "'";
			// log.info("FinanceReportDAO::getEmployeeMappingPFInfo" +
			// sqlQuery);
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
			
			 * regionPFIDS = regionPFIDS + personalCPFACCNO + "," +
			 * personalRegion + "=";
			 
			log
					.info("=============getEmployeeForm7MappingPFInfo========================"
							+ regionPFIDS);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return regionPFIDS;
	}*/

	
	public String buildQuerygetEmployeePFInfoPrinting(String range,
			String region, String empNameFlag, String empName,
			String sortedColumn, String pensionno, String toYear,
			String fromYear) {
		log
				.info("FinanceReportDAO::buildQuerygetEmployeePFInfoPrinting-- Entering Method");
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", sqlQuery = "";
		int startIndex = 0, endIndex = 0;
		sqlQuery = "SELECT REFPENSIONNUMBER,CPFACNO,i.UANNO,AIRPORTSERIALNUMBER,i.EMPLOYEENO, INITCAP(i.EMPLOYEENAME) AS EMPLOYEENAME,DESEGNATION,EMP_LEVEL,i.DATEOFBIRTH,i.dateofjoining,"
				+ "DATEOFSEPERATION_REASON,DATEOFSEPERATION_DATE,to_char( add_months(DATEOFSEPERATION_DATE ,1), 'dd-Mon-yyyy') as CALCPCUPTODATE ,DATEOFSEPERATION_DATE,WHETHER_FORM1_NOM_RECEIVED,REMARKS,i.AIRPORTCODE,GENDER,i.FHNAME,MARITALSTATUS,PERMANENTADDRESS,TEMPORATYADDRESS,"
				+ "WETHEROPTION,SETDATEOFANNUATION,EMPFLAG,DIVISION,DEPARTMENT,EMAILID,EMPNOMINEESHARABLE,i.REGION,i.PENSIONNO,(LAST_DAY(i.dateofbirth) + INTERVAL '60' year ) as DOR,i.username,"
				+ "i.LASTACTIVE,RefMonthYear,IPAddress,OTHERREASON,decode(sign(round(months_between(i.dateofjoining, '01-Apr-1995') / 12)),-1, '01-Apr-1995',1,to_char(i.dateofjoining,'dd-Mon-yyyy'),to_char(i.dateofjoining,'dd-Mon-yyyy')) as DATEOFENTITLE,"
				+ "to_char(trunc((i.dateofbirth + INTERVAL '60' year ), 'MM') - 1,'dd-Mon-yyyy')  as LASTDOB,ROUND(months_between('01-Apr-1995', i.dateofbirth) / 12) EMPAGE,FINALSETTLMENTDT,(CASE  WHEN (i.dateofbirth<last_day('"
				+ toYear
				+ "')) THEN  floor(months_between(last_day('"
				+ toYear
				+ "'),i.dateofbirth)/12) ELSE  ceil(months_between(i.dateofbirth, last_day('"
				+ toYear
				+ "')) / 12) END)  as	EMPAGE1, "
				+ "(CASE WHEN (DATEOFSEPERATION_date < = last_day('"
				+ toYear
				+ "')  AND DATEOFSEPERATION_date >='"
				+ fromYear
				+ "') THEN 'Y' ELSE 'N' END) as SEPER_FLAG,to_char( add_months(i.dateofbirth ,696), 'dd-Mon-yyyy') as LASTATTAINED,'N' AS GETARREARREVISEDFLAG ,'01-Jan-1900' as ARREARDATE,round(months_between(NVL(i.dateofjoining,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,'' as ARREARTODATE,'' as ARREARFORMDATE,'N' AS CHKWTHRARRNOT,round(months_between('"
				+ fromYear
				+ "',last_day(i.dateofbirth))/12,2) as currentage FROM EMPLOYEE_PERSONAL_INFO i,employee_pension_freshoption b WHERE FORMSDISABLE='Y' and b.freshpensionoption = 'B' and b.deleteflag = 'N' and b.pensionno=i.pensionno " ;
				/*+ "AND ((round(months_between('"
				+ fromYear
				+ "',last_day(dateofbirth))/12,2)<=58 AND (DATEOFSEPERATION_REASON  IN ('Resignation') OR DATEOFSEPERATION_REASON IS not NULL)OR DATEOFSEPERATION_REASON IS  NULL) OR (DATEOFSEPERATION_REASON IN ('Death','Resignation','Retirement') AND ADD_MONTHS(DATEOFSEPERATION_DATE,1)>=TO_DATE('"
				+ fromYear + "'))) ";*/

		if (!range.equals("NO-SELECT")) {

			String[] findRnge = range.split(" - ");
			startIndex = Integer.parseInt(findRnge[0]);
			endIndex = Integer.parseInt(findRnge[1]);

			whereClause.append("  i.PENSIONNO >=1 AND i.PENSIONNO <=27000");
			whereClause.append(" AND ");

		}

		if (!region.equals("") && !region.equals("AllRegions")) {
			whereClause.append(" REGION ='" + region + "'");
			whereClause.append(" AND ");
		}
		/*if (!toYear.equals("")) {
			whereClause.append(" i.dateofjoining <=LAST_DAY('" + toYear + "')");
			whereClause.append(" AND ");
		}*/
		if (empNameFlag.equals("true")) {
			if (!empName.equals("") && !pensionno.equals("")) {
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
		query.append(" and i.dateofjoining<='"+toYear+"' ");
		query.append(" order by i.pensionno ");
		dynamicQuery = query.toString();
		log
				.info("FinanceReportDAO::buildQuerygetEmployeePFInfoPrinting Leaving Method");
		return dynamicQuery;
	}
/*	public String chkForArrearAfter58(String pfId ,String FromDate, String toDate) {
		log.info("FinancialReportDAO:chkForArrearAfter58-- Entering Method");

		String sqlQueryForArrear = "",sqlQueryForChking58Years = "",crossed58Years="NotCrossed", ArrearAftr58Flag="NotExists";
		EmployeePensionCardInfo data = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null; 
		sqlQueryForChking58Years = "select 'X' as Flag  from employee_personal_info i where pensionno = "+pfId+"   and (round(months_between('"+FromDate+"', last_day(dateofbirth)) / 12, 2) >58)";
		sqlQueryForArrear = " select distinct pensionno as pensionno  from employee_pension_arrear  where  pensionno ="+pfId+"   and arreardate between  '"+FromDate+"' and '"+toDate+"'";  
		
	
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("sqlQueryForChking58Years" + sqlQueryForChking58Years);
			rs = st.executeQuery(sqlQueryForChking58Years);
			 
			if (rs.next()) { 
				 if(rs.getString("Flag")!=null){
					 crossed58Years="Crossed";
				 }
			} 
			
			if(crossed58Years.equals("Crossed")){
			log.info("sqlQueryForArrear" + sqlQueryForArrear);
			rs = st.executeQuery(sqlQueryForArrear);
		
			if (rs.next()) { 
				 if(rs.getString("pensionno")!=null){
					 ArrearAftr58Flag="Exists";
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
	 
		log.info("FinancialReportDAO -- Leaving Method"+ArrearAftr58Flag);
		return ArrearAftr58Flag;
	}*/
	
	
	private String getEmployeeForm7MappingPFInfo(String personalPFID,
			String personalCPFACCNO, String personalRegion) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", pfID = "", region = "", regionPFIDS = "";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			sqlQuery = "SELECT CPFACNO,REGION FROM EMPLOYEE_INFO WHERE EMPSERIALNUMBER='"
					+ personalPFID + "'";
			// log.info("FinanceReportDAO::getEmployeeMappingPFInfo" +
			// sqlQuery);
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
			/*
			 * regionPFIDS = regionPFIDS + personalCPFACCNO + "," +
			 * personalRegion + "=";
			 */
			log
					.info("=============getEmployeeForm7MappingPFInfo========================"
							+ regionPFIDS);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return regionPFIDS;
	}
	
}

