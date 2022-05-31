package aims.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.ResourceBundle;

import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

import aims.bean.EmpMasterBean;

import aims.bean.AAIEPF2Bean;
import aims.common.CommonUtil;

import aims.common.DBUtils;

import aims.common.Constants;
import aims.common.InvalidDataException;
import aims.common.Log;

public class AdjUploadDAO implements Constants {
	Log log = new Log(ImportDao.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	PensionDAO PDAO = new PensionDAO();

	PersonalDAO PersonalDAO = new PersonalDAO();

	FinancialReportDAO fDao = new FinancialReportDAO();

	CommonDAO commonDAO = new CommonDAO();
	AdjCrtnDAO adjCrtnDAO=new AdjCrtnDAO();

	// Modified By Prasad on 05-Mar-2012
	// Modified By Prasad on 06-Dec-2011
	public List importAdjOpeningBalanceNN(String xlsData, String region,
			String userName, String ipAddress, String fileName, String year,
			String month, String airportcode) throws InvalidDataException {
		int total = 0;
		log.info("AdjUploadDAO :importAdjOpeningBalanceNN()-- entering method");
		ArrayList xlsDataList = new ArrayList();
		List outputList = new LinkedList();
		Map map = new LinkedHashMap();
		ArrayList revpfIdslist = new ArrayList();
		xlsDataList = commonUtil.getTheList(xlsData, "***");
		Connection conn = null;
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		EmpMasterBean bean = null;
		AAIEPF2Bean aaiepf2 = null;
		String tempInfo[] = null;
		StringBuffer sb = null;
		StringBuffer sb1 = null;
		String tempData = "" ,revFlag = "";
		String multipleFlag[]=null;
		FileWriter fw = null;
		String deleteAdjOB = "", deleteempAdjoblog = "", deleteempyearwiseadjob = "";
		char[] delimiters = { '"', ',', '\'', '*', ',' };
		String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
		String uploadFilePath = "";
		String xlsEmpName = "", empName = "", designation = "", station = "", insertAdjOB = "", AdJ_UPLOAD_LOG = "", empAdjoblog = "", empyearwiseadjob = "";
		String adjobyear1 = "", previousPfid = "";
		FinancialReportDAO frDao = new FinancialReportDAO();
		String pfids = "",checkPfidForFrozen="";
		log.debug("xlsData " + xlsData + "Size" + xlsDataList.size());
		String upld_seq = "select upld_adj_grpid_seq.NEXTVAL as id from dual";
		String upld_seq_id = "";
		String consadjobyear = FORM2_CURRENT_ADJOBYEAR;
		try {
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			uploadFilePath = bundle.getString("upload.folder.path.epf.form2");

			conn = commonDB.getConnection();
			st = conn.createStatement();
			st1 = conn.createStatement();
			st2 = conn.createStatement();

			rs1 = st1.executeQuery(upld_seq);
			if (rs1.next()) {
				upld_seq_id = rs1.getString("id");
			}
			commonDB.closeConnection(null, st1, rs1);
			st1 = conn.createStatement();
			String[] temppfid;

			AdJ_UPLOAD_LOG = "insert into AdJ_UPLOAD_LOG(FILENAME,USERNAME,IPADDRESS,AIRPORT,REGION,UPLDPFIDS,"
					+ "ADJID) values(?,?,?,?,?,?,ADJUPDLOG_SEQ.NEXTVAL)";

			pst = conn.prepareStatement(AdJ_UPLOAD_LOG);
			pst.setString(1, fileName.trim());
			pst.setString(2, userName.trim());
			pst.setString(3, ipAddress.trim());
			pst.setString(4, airportcode);
			pst.setString(5, region);
			pst.setString(6, pfids);

			pst.executeUpdate();

			System.out.println("Before for loop" + xlsDataList.size());
			if (xlsDataList.size() < 1) {
				throw new Exception("Empty Sheet");
			}

			for (int i = 0; i < xlsDataList.size(); i++) {
				bean = new EmpMasterBean();
				aaiepf2 = new AAIEPF2Bean();
				System.out.println("After for loop");

				tempData = xlsDataList.get(i).toString();
				tempInfo = tempData.split("@");
				String cpfaccno = "", recivedRemarks = "", employeeName = "", employeeNo = "", pensionNo = "", status = "", user = "", adjyear = "", remarks = "", flag = "", table = "", form2id = "", checkDiff = "", checkExcelDiff = "",jvNo="";
				if (!tempInfo[1].equals("XXX")) {
					bean.setPfid(commonUtil.getSearchPFID1(tempInfo[1].trim()));
					pfids = pfids + bean.getPfid() + ",";
					aaiepf2.setPfid(bean.getPfid());

				} else {
					throw new Exception("PFID Mandatory");
				}
				if (!previousPfid.equals("")) {
					if (previousPfid.equals(aaiepf2.getPfid())) {
						throw new Exception("Pfid : " + aaiepf2.getPfid()
								+ " Duplicate Exist in Upload ExcelSheet");
					}
				}
				if (!tempInfo[8].equals("XXX")) {
					adjyear = commonUtil
					.convertDateFormat(tempInfo[8].trim());
					//adjyear="01-Apr-2011";
			           log.info("converted date is " + adjyear);
			           	int index = tempInfo[8].trim().indexOf("-");
			           	String count[] = tempInfo[8].split("-");
			           	log.info(" countlength " + count.length);
			           	if (index == -1 || count.length != 3) {
			           		throw new Exception(
			           				"PFID "
								+ bean.getPfid().trim()
								+ " Doesn't Have Valid Date Format(i.e dd-Mon-yyyy) In Column I of The Uploaded Sheet ");
			}
					
					log.info("adjobyear"+adjyear);
					int diff = commonUtil.getYeareDifference(adjyear,
							consadjobyear);
					/*if (diff > 0) {
						throw new Exception(
								"You can't adjustment in future financial year");
					} else */
						
						
						if (diff < 0) {
						throw new Exception(
								"2008-09,2009-10,2010-11,2011-12,2012-13,2013-14,2014-15,2015-16,2016-17,2017-18,2018-19,2019-20 are frozen.So,you can adjust as current FY");
					}
					aaiepf2.setAdjobyear(adjyear);
					
					checkPfidForFrozen=commonDAO.getUserReqForFrozenPfid(conn,aaiepf2.getPfid(),adjyear);
					if(!checkPfidForFrozen.equals("NOTEXIST")){
						throw new Exception("The  PFID:"+aaiepf2.getPfid()+ " Frozen by "+checkPfidForFrozen+". In the AdjOBYear : "+adjyear);
					}

				} else {
					throw new Exception("AdjObYear mandatory");
				}
				if (!tempInfo[2].equals("XXX")) {
					cpfaccno = tempInfo[2].trim();
				} else {
					cpfaccno = "";
				}
				if (!tempInfo[3].equals("XXX")) {
					employeeNo = tempInfo[3].trim();
				}
				if (!tempInfo[4].equals("XXX")) {
					empName = tempInfo[4].trim();
				} else {
					empName = "";
				}
				if (!tempInfo[5].equals("XXX")) {
					designation = tempInfo[5].trim();
				} else {
					designation = "";
				}

				/*
				 * if (!tempInfo[19].equals("XXX")) { status =
				 * tempInfo[19].trim(); } else { throw new Exception("Status
				 * mandatory"); }
				 */
				if (!tempInfo[19].equals("XXX")) {
					user = tempInfo[19].trim();
					log.info("user:=======:"+user);
					aaiepf2.setRequestedby(user);
				} else {
					throw new Exception("User Name Mandatory");
				}
				if (!tempInfo[20].equals("XXX")) {
					form2id = tempInfo[20].trim();
					aaiepf2.setForm2Id(form2id);
				} else {

					aaiepf2.setForm2Id("");

				}
				if (!tempInfo[21].equals("XXX")) {
					jvNo = tempInfo[21].trim();
					aaiepf2.setJvNo(jvNo);
				} else {

					aaiepf2.setJvNo("");

				}

				if (!tempInfo[22].equals("XXX")) {
					remarks = tempInfo[22].trim();
					aaiepf2.setRemarks(remarks);
				} else {

					aaiepf2.setRemarks("");

				}
				if (!tempInfo[23].equals("XXX")) {
					flag = tempInfo[23].trim();
					if(flag.equals("")||flag.equals("U")||flag.equals("R")||flag.equals("U-RE")||flag.equals("R-RE")||flag.equals("D")||flag.equals("DEL-RE") ){
						
					}else{
						throw new Exception(" Please Enter the appropriate Data in Flag Column i.e U for Update D for Delete  R for Replace U-RE for UpdateRevised R-RE for ReplaceRevised DEL-RE for DeleteRevised(Including ImpactCalc)");
					}
					int flagSize=flag.indexOf("-");
					if(flagSize!=-1){
						multipleFlag=flag.split("-");
						flag=multipleFlag[0];
						revFlag=multipleFlag[1];
						log.info("Flag====="+flag+"  RevFlag:"+revFlag);
					}
				} else {
					flag = "";
					// throw new Exception("Flag mandatory");
				}
				if (!tempInfo[24].equals("XXX")) {
					recivedRemarks = tempInfo[24].trim();
					if (recivedRemarks.length() < 75) {

						aaiepf2.setReceivedRemarks(recivedRemarks);
					} else {
						throw new Exception("Received Remarks length Should be less than 75 Characters");
					}
				} else {
					throw new Exception("Received Remarks Mandatory");
				}
				if (!flag.equals("D")) {
					/*
					 * if (!tempInfo[23].equals("XXX")) { priorFlag=
					 * tempInfo[23].trim(); aaiepf2.setPrioradjflag(priorFlag); }
					 * else { priorFlag=""; aaiepf2.setPrioradjflag(priorFlag); }
					 */

					// log.info("1819"+status+user);
					String monthYear = "01-Apr-2008";
					double empsub = 0.00, empsubInterest = 0.00, aaiAmount = 0.00, aaiInterest = 0.00, outstandadv = 0.00, pencontri = 0.00, pensiontotal = 0.00;
					if (!tempInfo[18].equals("XXX")) {
						station = tempInfo[18].trim();
					} else {
						//throw new InvalidDataException("Airport Name Mandatory");
					}
					if (airportcode != null
							&& !bean.getPfid().trim().equals("")) {
						log.info("pfid" + bean.getPfid() + "sheet airportcode "
								+ station.replaceAll(" ", "")
								+ " selected airportcode "
								+ airportcode.replaceAll(" ", ""));

						if (region.equals("CHQIAD")) {
							if (!station.replaceAll(" ", "").equals(
									airportcode.replaceAll(" ", ""))) {
								throw new InvalidDataException(
										"Please make sure that the Seleted Airportcode above  (i.e "
												+ airportcode
												+ ") and is matching with AirportCode given in the excel sheet at Column 17. i.e"
												+ station);
							}
						}
					}
					if (!tempInfo[10].equals("XXX")
							&& !tempInfo[10].trim().equals("")) {
						log.info("ob " + tempInfo[10]);
						empsub = Double.parseDouble(tempInfo[10].trim()
								.replaceAll(",", "").trim());

						aaiepf2.setEmpsub(Double.toString(Math.round(empsub)));
					} else {
						empsub = 0.00;
						aaiepf2.setEmpsub("0.00");
					}

					if (!tempInfo[11].equals("XXX")
							&& !tempInfo[11].trim().equals("")) {
						empsubInterest = Double.parseDouble(tempInfo[11]
								.replaceAll(",", "").trim());
						aaiepf2.setEmpsubInterest(Double.toString(Math
								.round(empsubInterest)));
					} else {
						empsubInterest = 0.00;
						aaiepf2.setEmpsubInterest("0.00");
					}

					if (!tempInfo[13].equals("XXX")
							&& !tempInfo[13].trim().equals("")) {
						aaiAmount = Double.parseDouble(tempInfo[13].replaceAll(
								",", "").trim());
						aaiepf2.setAaiAmount(Double.toString(Math
								.round(aaiAmount)));
					} else {
						aaiAmount = 0.00;
						aaiepf2.setAaiAmount("0.00");
					}
					if (!tempInfo[14].equals("XXX")
							&& !tempInfo[14].trim().equals("")) {
						aaiInterest = Double.parseDouble(tempInfo[14]
								.replaceAll(",", "").trim());
						aaiepf2.setAaiInterest(Double.toString(Math
								.round(aaiInterest)));
					} else {
						aaiInterest = 0.00;
						aaiepf2.setAaiInterest("0.00");
					}
					if (!tempInfo[15].equals("XXX")
							&& !tempInfo[15].trim().equals("")) {
						pensiontotal = Double.parseDouble(tempInfo[15]
								.replaceAll(",", "").trim());
						aaiepf2.setPensiontotal(Double.toString(Math
								.round(pensiontotal)));
					} else {
						pensiontotal = 0.00;
						aaiepf2.setPensiontotal("0.00");
					}
					if (!tempInfo[17].equals("XXX")
							&& !tempInfo[17].trim().equals("")) {
						outstandadv = Double.parseDouble(tempInfo[17]
								.replaceAll(",", "").trim());
						aaiepf2.setOutstandadv(Double.toString(Math
								.round(outstandadv)));
					} else {
						outstandadv = 0.00;
						aaiepf2.setOutstandadv("0.00");
					}
					if (!tempInfo[16].equals("XXX")
							&& !tempInfo[16].trim().equals("")) {
						pencontri = Double.parseDouble(tempInfo[16].replaceAll(
								",", "").trim());
						aaiepf2.setPencontri(Double.toString(Math
								.round(pencontri)));
					} else {
						pencontri = 0.00;
						aaiepf2.setPencontri("0.00");
					}
					if (!aaiepf2.getForm2Id().equals("")) {
						if(aaiepf2.getRemarks().equals("")){
							throw new Exception("PFID: "+aaiepf2.getPfid()+" Remarks Mandatory");	
						}
						if(aaiepf2.getJvNo().equals("")&&( aaiepf2.getRemarks().equals("2010-2011")||aaiepf2.getRemarks().equals("2011-2012")||aaiepf2.getRemarks().equals("2012-2013")||aaiepf2.getRemarks().equals("2013-2014")||aaiepf2.getRemarks().equals("2014-2015"))){
							throw new Exception("PFID: "+aaiepf2.getPfid()+" JV Number Not Available.");
						}
						if(revFlag.equals("RE")){
							String checkForm2idStatus=this.checkForm2idStatus(aaiepf2.getPfid(),aaiepf2.getForm2Id(),st1,rs,"");
							if(!checkForm2idStatus.equals("NOTEXIST")){
							revpfIdslist.add(checkForm2idStatus);	
							revpfIdslist.add(bean.getPfid());
							revpfIdslist.add(aaiepf2.getRequestedby());
							map.put(bean.getPfid(),revpfIdslist);
							}else{
								throw new Exception("Not given adjustment / Form-2ID Not matched with Imp.Calc Screen Generation ID");
							}
						}else{
						checkExcelDiff = this.chkDiff(aaiepf2.getPfid(), "E",
								aaiepf2.getEmpsub(), aaiepf2
										.getEmpsubInterest(), aaiepf2
										.getAaiAmount(), aaiepf2
										.getAaiInterest(), aaiepf2
										.getPencontri(), aaiepf2.getForm2Id(),aaiepf2.getJvNo(),aaiepf2.getRemarks());
						if (checkExcelDiff.equals("DIFF")) {
							throw new Exception(
									"PFID "
											+ aaiepf2.getPfid()
											+ " : Uploading Form2 values, Imp.Calc Form2 values are not match");
						} else if (checkExcelDiff.equals("NOTEXIST")) {
							throw new Exception(
									"PFID "
											+ aaiepf2.getPfid()
											+ " : Imp.Calc screen generation ID / JV Number is not matched with uploaded form-2 id / JV Number ");
						}
						checkDiff = this.chkDiff(aaiepf2.getPfid(), "", aaiepf2
								.getEmpsub(), aaiepf2.getEmpsubInterest(),
								aaiepf2.getAaiAmount(), aaiepf2
										.getAaiInterest(), aaiepf2
										.getPencontri(), aaiepf2.getForm2Id(),aaiepf2.getJvNo(),aaiepf2.getRemarks());
						if (checkDiff.equals("DIFF")) {
							throw new Exception(
									"PFID "
											+ aaiepf2.getPfid()
											+ " : Disperency of adjustments in Imp.Calc (i..e, transaction ,difference tables)");
						}
						if (!aaiepf2.getOutstandadv().equals("0.00")) {
							throw new Exception(
									"PFID "
											+ aaiepf2.getPfid()
											+ " : Imp.Cal screen is not processed of OutStandAdv amount,So you can't upload the OutStandAdv with generation ID of Imp.Cal");
						}
						
						if (flag.equals("")) {
							String checkForm2idStatus=this.checkForm2idStatus(aaiepf2.getPfid(),aaiepf2.getForm2Id(),st1,rs,"F");
							if(!checkForm2idStatus.equals("NOTEXIST")){
								throw new InvalidDataException("PFID: "
									+ bean.getPfid().trim() + " already AdjAmt Exist");
							}
						}
						}
						

					}

					aaiepf2.setMauals("Y");
					if (!bean.getCpfAcNo().trim().equals("")
							|| !bean.getPfid().trim().equals("")) {
						// if(!tempInfo[1].equals("XXX")){
						String checkPFID = "select wetheroption,pensionno,dateofbirth,dateofjoining,employeename from employee_personal_info where to_char(pensionno)='"
								+ bean.getPfid() + "'";
						log.info(checkPFID);
						st1 = conn.createStatement();
						rs = null;
						rs = st1.executeQuery(checkPFID);

						if (!rs.next()) {
							throw new InvalidDataException("PFID "
									+ bean.getPfid().trim() + " doesn't Exist");
						}

					}
					log.info("Data" + aaiAmount + " " + aaiInterest + " "
							+ empsub + " " + empsubInterest + " " + outstandadv
							+ " " + adjyear + " " + remarks + " "
							+ pensiontotal + " " + flag);
					//con
					if(flag.equals("DEL")){
						
						empAdjoblog = this.insertQuery(aaiepf2, "Employee_adj_ob_log");
						log.info("empAdjoblog" + empAdjoblog);
						st.addBatch(empAdjoblog);
						
						insertAdjOB = this.updateQuery(aaiepf2);
						st.addBatch(insertAdjOB);
						deleteempAdjoblog = "insert into employee_adj_ob_log(pensionno,adjobyear,requestedby,remarks) values('"
							+ bean.getPfid()
							+ "','"
							+ aaiepf2.getAdjobyear()
							+ "','"
							+ aaiepf2.getRequestedby()
							+ "','Deleted')";
					deleteempyearwiseadjob = "delete from employee_yearwise_adjob b where pensionno='"+bean.getPfid()+"' and remarks=(select adjobyear from epis_adj_crtn_pfid_tracking t where t.pensionno='"+bean.getPfid()+"' and t.form2status='Y' and t.form2id='"+aaiepf2.getForm2Id()+"')";
					
					log.info("deleteempAdjoblog" + deleteempAdjoblog);
					st.addBatch(deleteempAdjoblog);
					log.info("deleteempyearwiseadjob" + deleteempyearwiseadjob);
					st.addBatch(deleteempyearwiseadjob);
					}else{

					table = "Employee_adj_ob_log";
					empAdjoblog = this.insertQuery(aaiepf2, table);
					log.info("empAdjoblog" + empAdjoblog);
					// st.executeQuery(empAdjoblog);

					st.addBatch(empAdjoblog);
					empyearwiseadjob = this.insertEmpyearwiseadjobQuery(
							aaiepf2, upld_seq_id);
					log.info("empyearwiseadjob" + empAdjoblog);
					st.addBatch(empyearwiseadjob);

					String pfid = "select pensionno,prioradjflag from employee_adj_ob where pensionno='"
							+ bean.getPfid()
							+ "' and ADJOBYEAR='"
							+ adjyear
							+ "'";
					log.info(pfid);
					rs1 = st1.executeQuery(pfid);

					if (!rs1.next()) {
						log.info("insert");
						if (!flag.equals("")) {
							throw new Exception("Pfid " + bean.getPfid()
									+ " is not Exist in AdjobYear : " + adjyear);

						}

						insertAdjOB = this.insertPriorQuery(aaiepf2);

						/*
						 * else{ table="Employee_adj_ob";
						 * insertAdjOB=this.insertQuery(aaiepf2,table); }
						 */

					} else {

						if (flag.equals("")) {
							throw new Exception("Pfid " + bean.getPfid()
									+ " is already exist.");

						}
						//
						/*
						 * String Pflag=rs1.getString("prioradjflag");
						 * aaiepf2.setPrioradjflag(Pflag);
						 */
						//
						log.info("flag" + flag);
						if (flag.equals("U")) {
							String checkForm2ID="";
							log.info("Update");
							if(!aaiepf2.getForm2Id().equals("")){
								checkForm2ID=this.checkForm2ID(aaiepf2);
								if(checkForm2ID.equals("EXIST")){
									throw new Exception("PFID: "+bean.getPfid() +" already processed for the slot:"+aaiepf2.getRemarks()+" and Form2Id: "+aaiepf2.getForm2Id());
								}
							}
							insertAdjOB = this.updateQuery(aaiepf2);

						} else if (flag.equals("R")) {
							log.info("Replace");
							insertAdjOB = this.replaceQuery(aaiepf2);

						}/*
							 * else if(flag.equals("P")){ log.info("Prior");
							 * insertAdjOB=this.updatePriorQuery(aaiepf2); }
							 */
					}

					log.info("insertAdjOB" + insertAdjOB);
					// st.executeQuery(insertAdjOB);
					st.addBatch(insertAdjOB);
					}

				} else {
					log.info("Delete");
					String deletedremarks = "";
					if (!aaiepf2.getRemarks().equals("")) {
						deletedremarks = aaiepf2.getRemarks();
					} else {
						deletedremarks = "Deleted";
					}
					deleteAdjOB = "delete from employee_adj_ob where adjobyear='"
							+ aaiepf2.getAdjobyear()
							+ "' and pensionno='"
							+ bean.getPfid() + "'";

					deleteempAdjoblog = "insert into employee_adj_ob_log(pensionno,adjobyear,requestedby,remarks) values('"
							+ bean.getPfid()
							+ "','"
							+ aaiepf2.getAdjobyear()
							+ "','"
							+ aaiepf2.getRequestedby()
							+ "','"
							+ deletedremarks + "')";
					deleteempyearwiseadjob = "delete from employee_yearwise_adjob where adjobdt='"
							+ aaiepf2.getAdjobyear()
							+ "' and pensionno='"
							+ bean.getPfid() + "'";

					log.info("deleteAdjOB" + deleteAdjOB);
					st.addBatch(deleteAdjOB);
					log.info("deleteempAdjoblog" + deleteempAdjoblog);
					st.addBatch(deleteempAdjoblog);
					log.info("deleteempyearwiseadjob" + deleteempyearwiseadjob);
					st.addBatch(deleteempyearwiseadjob);
				}
				if (!aaiepf2.getForm2Id().equals("")) {
					String updateForm2Status = "update epis_adj_crtn_pfid_tracking set form2status='Y'  where pensionno='"
							+ bean.getPfid()
							+ "' and form2id='"
							+ aaiepf2.getForm2Id() + "'";
					st.addBatch(updateForm2Status);
				}
				previousPfid = aaiepf2.getPfid();

			}

			String pfidlist = pfids.substring(0, pfids.lastIndexOf(","));
			log.info("pfidlist" + pfidlist);
			sb = new StringBuffer();
			sb.append(uploadFilePath);
			sb.append("\\");
			sb.append(fileName.trim());
			String originalFileName = sb.toString();
			String file2 = fileName.substring(0, fileName.indexOf("."));
			sb1 = new StringBuffer();
			sb1.append(uploadFilePath);
			sb1.append("\\");
			sb1.append(file2);
			sb1.append(pfidlist);
			sb1.append(".xls");
			String renamedFileName = sb1.toString();

			// This method is used for rename the file
			commonUtil.renameFile(originalFileName, renamedFileName);

			String importStausupdate = "update AdJ_UPLOAD_LOG  set IMPORTSTATUS='Y',UPLDPFIDS='"
					+ pfidlist
					+ "',filename='"
					+ renamedFileName
					+ "' where filename='" + fileName + "'";
			log.info("importStausupdate" + importStausupdate);
			// st.executeQuery(importStausupdate);
			/*log.info("revpfIdslist"+revpfIdslist.size());
			for(int i=0;i<revpfIdslist.size();i++){
				log.info("forloop::::::::::"+revpfIdslist.get(i));
			}*/
			st.addBatch(importStausupdate);
			int count[] = st.executeBatch();
			if(revFlag.equals("RE")){
				Set regionset = map.entrySet();
				Iterator itr = regionset.iterator();
       	while (itr.hasNext()) {
				Map.Entry entry = (Map.Entry) itr.next(); 
				revpfIdslist=(ArrayList)entry.getValue();
				log.info("pfid:::::::::::::::::::"+revpfIdslist.get(1).toString()+"adjobyear::::::::::::"+revpfIdslist.get(0).toString()+"user::::::::::::"+revpfIdslist.get(2));
				adjCrtnDAO.getDeleteAllRecords(revpfIdslist.get(1).toString(),revpfIdslist.get(0).toString(),"adjcorrections",revpfIdslist.get(2).toString(),"","","","","","");
					}

				}
			outputList.add(renamedFileName);
			outputList.add(upld_seq_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			try {
				st2.close();
				st1.close();
				st.close();
				rs2.close();
				pst.close();
				rs1.close();
				rs.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		log.info("AdjUploadDAO :importAdjOpeningBalanceNN leaving method");
		return outputList;
	}

	public String checkForm2idStatus(String pfid,String form2id,Statement st1,ResultSet rs,String flag){
		String status="",checkAdjobyearStatus="";
		/*if(flag.equals("F")){*/
			checkAdjobyearStatus="select  adjobyear from epis_adj_crtn_pfid_tracking    where pensionno='"
				+ pfid
				+ "' and form2id='"
				+ form2id
				+ "' and form2status='Y'";
		/*}else{
		 checkAdjobyearStatus = "select  adjobyear from epis_adj_crtn_pfid_tracking    where pensionno='"
			+ pfid
			+ "' and form2id='"
			+ form2id
			+ "' and form2status='Y' and frozen='Y'";
		}*/
	log.info(checkAdjobyearStatus);
	try{
	rs = st1.executeQuery(checkAdjobyearStatus);

	if (rs.next()) {
		if(rs.getString("adjobyear")!=null){
		status=rs.getString("adjobyear");
		}
	}else{
		status="NOTEXIST";
	}
	}catch(Exception e){
		log.info("=======<<<<<<<<<<>>>>>>Exception<<<<<<<>>>>>>>>>>>>>=========="+e.getMessage());
	}finally{
	commonDB.closeConnection(null, st1, rs);
	try {
		rs.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	return status;
	}
	public String insertEmpyearwiseadjobQuery(AAIEPF2Bean aaiepf2,
			String upld_seq) {

		log.info("AdjUploadDAO:insertEmpyearwiseadjobQuery Entering Method");

		StringBuffer is = new StringBuffer();
		String qry = "";
		double empint = 0.00, empsub = 0.00;
		double aaiamt = 0.00, aaiint = 0.00;
		double emptotal = 0.00, aaitotal = 0.00;

		is.append("insert into employee_yearwise_adjob (");

		if (!aaiepf2.getPfid().equals("")) {
			is.append("PENSIONNO");
			is.append(",shtgrupid");

		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			is.append(",adjobdt");

		}

		if (!aaiepf2.getAaiAmount().equals("")) {
			is.append(",AAICONTRI");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",AAIIINTEREST");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",EMPCONTRI");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",EMPINTEREST");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",OUTSTANDADV");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",REMARKS");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",RECEIVEDREMARKS");

		}
		if (!aaiepf2.getEmpsub().equals("")
				&& !aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",EMPTOTAL");

		}
		if (!aaiepf2.getAaiAmount().equals("")
				&& !aaiepf2.getAaiInterest().equals("")) {
			is.append(",AAITOTAL");

		}
		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",EMPCONTRIDEVIATION");

		}
		is.append(")values(");

		if (!aaiepf2.getPfid().equals("")) {
			is.append("'" + aaiepf2.getPfid() + "'");
			is.append("," + upld_seq);

		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			is.append(",'" + aaiepf2.getAdjobyear() + "'");

		}
		if (!aaiepf2.getAaiAmount().equals("")) {
			is.append(",'" + aaiepf2.getAaiAmount() + "'");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",'" + aaiepf2.getAaiInterest() + "'");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",'" + aaiepf2.getEmpsub() + "'");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",'" + aaiepf2.getEmpsubInterest() + "'");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",'" + aaiepf2.getOutstandadv() + "'");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",'" + aaiepf2.getRemarks() + "'");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",'" + aaiepf2.getReceivedRemarks() + "'");

		}
		if (!aaiepf2.getEmpsub().equals("")) {

			empsub = Double.parseDouble(aaiepf2.getEmpsub());

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {

			empint = Double.parseDouble(aaiepf2.getEmpsubInterest());
		}
		emptotal = empsub + empint;

		is.append(",'" + emptotal + "'");

		if (!aaiepf2.getAaiAmount().equals("")) {
			aaiamt = Double.parseDouble(aaiepf2.getAaiAmount());

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			aaiint = Double.parseDouble(aaiepf2.getAaiInterest());
		}
		aaitotal = aaiamt + aaiint;
		is.append(",'" + aaitotal + "'");

		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",'" + aaiepf2.getPencontri() + "'");

		}
		is.append(")");

		qry = is.toString();

		return qry;
	}

	public String insertQuery(AAIEPF2Bean aaiepf2, String table) {
		log.info("AdjUploadDAO:insertQuery Entering Method");

		StringBuffer is = new StringBuffer();
		String insertqry = "";
		if (table.equals("Employee_adj_ob_log")) {
			is.append("insert into employee_adj_ob_log (");
		} else {
			is.append("insert into employee_adj_ob (");
		}

		if (!aaiepf2.getPfid().equals("")) {
			is.append("PENSIONNO");

		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			is.append(",ADJOBYEAR");

		}
		if (!aaiepf2.getPensiontotal().equals("")) {
			is.append(",PENSIONTOTAL");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",PENSIONINTEREST");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",EMPSUB");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",EMPSUBINTEREST");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",OUTSTANDADV");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",REMARKS");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",RECEIVEDREMARKS");

		}
		if (!aaiepf2.getMauals().equals("")) {
			is.append(",MANUALS");

		}
		if (!aaiepf2.getRequestedby().equals("")) {
			is.append(",REQUESTEDBY");

		}
		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",PENSIONCONTRIADJ");

		}
		is.append(")values(");

		if (!aaiepf2.getPfid().equals("")) {
			is.append("'" + aaiepf2.getPfid() + "'");

		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			is.append(",'" + aaiepf2.getAdjobyear() + "'");

		}

		if (!aaiepf2.getAaiAmount().equals("")) {
			is.append(",'" + aaiepf2.getAaiAmount() + "'");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",'" + aaiepf2.getAaiInterest() + "'");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",'" + aaiepf2.getEmpsub() + "'");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",'" + aaiepf2.getEmpsubInterest() + "'");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",'" + aaiepf2.getOutstandadv() + "'");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",'" + aaiepf2.getRemarks() + "'");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",'" + aaiepf2.getReceivedRemarks() + "'");

		}
		if (!aaiepf2.getMauals().equals("")) {
			is.append(",'Y'");

		}
		if (!aaiepf2.getRequestedby().equals("")) {
			is.append(",'" + aaiepf2.getRequestedby() + "'");

		}
		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",'" + aaiepf2.getPencontri() + "'");

		}
		is.append(")");

		insertqry = is.toString();
		log.info("ImportDao:insertQuery leaving Method");
		return insertqry;

	}

	public String insertPriorQuery(AAIEPF2Bean aaiepf2) {
		log.info("AdjUploadDAO:insertQuery Entering Method");

		StringBuffer is = new StringBuffer();
		String insertqry = "";

		is.append("insert into employee_adj_ob (");

		if (!aaiepf2.getPfid().equals("")) {
			is.append("PENSIONNO");

		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			is.append(",ADJOBYEAR");

		}
		if (!aaiepf2.getPensiontotal().equals("")) {
			is.append(",PENSIONTOTAL");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",PENSIONINTEREST");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",EMPSUB");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",EMPSUBINTEREST");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",OUTSTANDADV");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",REMARKS");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",RECEIVEDREMARKS");

		}
		if (!aaiepf2.getMauals().equals("")) {
			is.append(",MANUALS");
			is.append(",LASTACTIVE");

		}
		if (!aaiepf2.getRequestedby().equals("")) {
			is.append(",REQUESTEDBY");

		}
		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",PENSIONCONTRIADJ");

		}
		is.append(")values(");

		if (!aaiepf2.getPfid().equals("")) {
			is.append("'" + aaiepf2.getPfid() + "'");

		}
		if (!aaiepf2.getAdjobyear().equals("")) {

			is.append(",'" + aaiepf2.getAdjobyear() + "'");

		}
		if (!aaiepf2.getAaiAmount().equals("")) {
			is.append(",-('" + aaiepf2.getAaiAmount() + "')");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			is.append(",-('" + aaiepf2.getAaiInterest() + "')");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			is.append(",'" + aaiepf2.getEmpsub() + "'");

		}
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			is.append(",'" + aaiepf2.getEmpsubInterest() + "'");

		}
		if (!aaiepf2.getOutstandadv().equals("")) {
			is.append(",'" + aaiepf2.getOutstandadv() + "'");

		}
		if (!aaiepf2.getRemarks().equals("")) {
			is.append(",'" + aaiepf2.getRemarks() + "'");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			is.append(",'" + aaiepf2.getReceivedRemarks() + "'");

		}
		if (!aaiepf2.getMauals().equals("")) {
			is.append(",'Y'");
			is.append(",sysdate");

		}
		if (!aaiepf2.getRequestedby().equals("")) {
			is.append(",'" + aaiepf2.getRequestedby() + "'");

		}
		if (!aaiepf2.getPencontri().equals("")) {
			is.append(",'" + aaiepf2.getPencontri() + "'");

		}
		is.append(")");

		insertqry = is.toString();
		log.info("ImportDao:insertQuery leaving Method");
		return insertqry;

	}

	public String updateQuery(AAIEPF2Bean aaiepf2) {

		log.info("AdjUploadDAO : updateQuery Entering Method");

		StringBuffer updateAdjOB = new StringBuffer();
		String insertAdjOB = "";

		updateAdjOB.append("update employee_adj_ob  set ");
		updateAdjOB.append("PENSIONTOTAL=nvl(PENSIONTOTAL,0)");
		if (!aaiepf2.getAaiAmount().equals("")) {
			updateAdjOB.append("-(");
			updateAdjOB.append(aaiepf2.getAaiAmount());
			updateAdjOB.append(")");
		}
		updateAdjOB.append(",PENSIONINTEREST=nvl(PENSIONINTEREST,0)");
		if (!aaiepf2.getAaiInterest().equals("")) {
			updateAdjOB.append("-(");
			updateAdjOB.append(aaiepf2.getAaiInterest());
			updateAdjOB.append(")");
		}

		updateAdjOB.append(",EMPSUB=nvl(EMPSUB,0)");
		if (!aaiepf2.getEmpsub().equals("")) {
			updateAdjOB.append("+");
			updateAdjOB.append(aaiepf2.getEmpsub());
		}

		updateAdjOB.append(",EMPSUBINTEREST=nvl(EMPSUBINTEREST,0)");
		if (!aaiepf2.getEmpsubInterest().equals("")) {
			updateAdjOB.append("+");
			updateAdjOB.append(aaiepf2.getEmpsubInterest());
		}

		updateAdjOB.append(",OUTSTANDADV=nvl(OUTSTANDADV,0)");
		if (!aaiepf2.getOutstandadv().equals("")) {
			updateAdjOB.append("+");
			updateAdjOB.append(aaiepf2.getOutstandadv());
		}
		updateAdjOB.append(",REMARKS=REMARKS ");
		if (!aaiepf2.getRemarks().equals("")) {
			updateAdjOB.append(" || ");
			updateAdjOB.append("'" + aaiepf2.getRemarks() + "'");
		}
		updateAdjOB.append(",RECEIVEDREMARKS=RECEIVEDREMARKS ");
		if (!aaiepf2.getReceivedRemarks().equals("")) {
			updateAdjOB.append(" || ");
			updateAdjOB.append("'" + aaiepf2.getReceivedRemarks() + "'");
		}
		updateAdjOB.append(",MANUALS='Y'");
		updateAdjOB.append(",LASTACTIVE=sysdate");
		if (!aaiepf2.getRequestedby().equals("")) {
			updateAdjOB.append(",REQUESTEDBY='"
					+ aaiepf2.getRequestedby().trim() + "'");

		}

		updateAdjOB.append(",PENSIONCONTRIADJ=nvl(PENSIONCONTRIADJ,0)");
		if (!aaiepf2.getPencontri().equals("")) {
			updateAdjOB.append("+");
			updateAdjOB.append(aaiepf2.getPencontri());
		}
		updateAdjOB.append(" ");
		updateAdjOB.append("where");
		updateAdjOB.append(" ");
		if (!aaiepf2.getPfid().equals("")) {

			updateAdjOB.append("pensionno='" + aaiepf2.getPfid().trim() + "'");
			updateAdjOB.append(" ");
			updateAdjOB.append("And");
			updateAdjOB.append(" ");
		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			updateAdjOB.append("ADJOBYEAR='" + aaiepf2.getAdjobyear().trim()
					+ "'");

		}
		insertAdjOB = updateAdjOB.toString();
		log.info("ImportDao : updateQuery leaving Method");
		return insertAdjOB;
	}

	public String replaceQuery(AAIEPF2Bean aaiepf2) {

		log.info("AdjUploadDAO : replaceQuery Entering Method");

		StringBuffer updateAdjOB = new StringBuffer();
		String insertAdjOB = "";

		updateAdjOB.append("update employee_adj_ob  set ");

		if (!aaiepf2.getAaiAmount().equals("")) {

			updateAdjOB
					.append("PENSIONTOTAL=-'" + aaiepf2.getAaiAmount() + "'");

		}
		if (!aaiepf2.getAaiInterest().equals("")) {
			updateAdjOB.append(",PENSIONINTEREST=-'" + aaiepf2.getAaiInterest()
					+ "'");

		}

		if (!aaiepf2.getEmpsub().equals("")) {
			updateAdjOB.append(",EMPSUB='" + aaiepf2.getEmpsub() + "'");
		}

		if (!aaiepf2.getEmpsubInterest().equals("")) {
			updateAdjOB.append(",EMPSUBINTEREST='"
					+ aaiepf2.getEmpsubInterest() + "'");
		}

		if (!aaiepf2.getOutstandadv().equals("")) {
			updateAdjOB.append(",OUTSTANDADV='" + aaiepf2.getOutstandadv()
					+ "'");
		}

		if (!aaiepf2.getRemarks().equals("")) {

			updateAdjOB.append(",REMARKS=REMARKS || '" + aaiepf2.getRemarks()
					+ "'");

		}
		if (!aaiepf2.getReceivedRemarks().equals("")) {

			updateAdjOB.append(",RECEIVEDREMARKS=RECEIVEDREMARKS || '"
					+ aaiepf2.getReceivedRemarks() + "'");

		}
		updateAdjOB.append(",MANUALS='Y'");
		updateAdjOB.append(",LASTACTIVE=sysdate");
		if (!aaiepf2.getRequestedby().equals("")) {
			updateAdjOB.append(",REQUESTEDBY='"
					+ aaiepf2.getRequestedby().trim() + "'");

		}

		if (!aaiepf2.getPencontri().equals("")) {
			updateAdjOB.append(",PENSIONCONTRIADJ='" + aaiepf2.getPencontri()
					+ "'");
		}

		updateAdjOB.append(" ");
		updateAdjOB.append("where");
		updateAdjOB.append(" ");
		if (!aaiepf2.getPfid().equals("")) {

			updateAdjOB.append("pensionno='" + aaiepf2.getPfid().trim() + "'");
			updateAdjOB.append(" ");
			updateAdjOB.append("And");
			updateAdjOB.append(" ");
		}
		if (!aaiepf2.getAdjobyear().equals("")) {
			updateAdjOB.append("ADJOBYEAR='" + aaiepf2.getAdjobyear().trim()
					+ "'");

		}
		insertAdjOB = updateAdjOB.toString();
		log.info("ImportDao : replaceQuery leaving Method");
		return insertAdjOB;
	}

	public void uploadsheetsintoDB(String filename, String pensionnolst,
			String type, String suffix) {
		log.info("AdjUploadDAO : uploadsheetsintoDB entering Method");
		Connection con = null;
		BLOB xlsDocument = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		FileInputStream inputFileInputStream = null;
		String sheetName = "", updateQuery = "", sqlText = "", extension = "";
		File f = null;

		sqlText = "SELECT jsinformation FROM   upld_adj_justfications_info WHERE SHTGRUPID=? AND SHEETTYPE=? FOR UPDATE";

		int bytesRead = 0, bytesWritten = 0, totbytesRead = 0, totbytesWritten = 0, position = 1;
		try {
			log.info("uploadsheetsintoDB::pensionnolst============"
					+ pensionnolst + "filename" + filename);
			con = DBUtils.getConnection();
			f = new File(filename);

			sheetName = filename.substring((filename.lastIndexOf(suffix) + 1),
					filename.length());
			extension = filename.substring((filename.lastIndexOf(".") + 1),
					filename.length());
			log.info("uploadsheetsintoDB::extension============" + extension
					+ "filename" + sheetName);
			updateQuery = "UPDATE EMPLOYEE_YEARWISE_ADJOB SET chklob=? WHERE SHTGRUPID=?";

			pst = con
					.prepareStatement("INSERT INTO upld_adj_justfications_info(SHTGRUPID,sheetid,sheetname,SHEETTYPE,sheetlastactive,jsinformation,extension) VALUES(?,upld_adj_jstf_seq.nextval,?,?,sysdate,?,?)");
			pst.setString(1, pensionnolst);
			pst.setString(2, sheetName);
			pst.setString(3, type);
			pst.setBlob(4, xlsDocument.empty_lob());
			pst.setString(5, extension);
			pst.executeUpdate();
			pst = null;
			pst = con.prepareStatement(sqlText);
			pst.setString(1, pensionnolst);
			pst.setString(2, type);
			con.setAutoCommit(false);
			rs = pst.executeQuery();

			if (rs.next()) {
				xlsDocument = ((OracleResultSet) rs).getBLOB("jsinformation");
				int chunkSize = xlsDocument.getChunkSize();
				byte[] binaryBuffer = new byte[chunkSize];

				inputFileInputStream = new FileInputStream(new File(filename));
				while ((bytesRead = inputFileInputStream.read(binaryBuffer)) != -1) {
					bytesWritten = xlsDocument.putBytes(position, binaryBuffer,
							bytesRead);
					position += bytesRead;
					totbytesRead += bytesRead;
					totbytesWritten += bytesWritten;
				}
				inputFileInputStream.close();
			}
			con.setAutoCommit(true);
			con.commit();
			pst = null;
			pst = con.prepareStatement(updateQuery);

			pst.setString(1, "Y");
			pst.setString(2, pensionnolst);
			pst.executeUpdate();

			boolean success = f.delete();
			if (success) {
				log.info("uploadsheetsintoDB::Deletion failed.");
			}
		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} finally {
			pst = null;
			commonDB.closeConnection(con, null, rs);

		}

	}

	public List importAdjOpeningBalanceLoad(String xlsData, String region,
			String userName, String ipAddress, String fileName, String year,
			String month, String airportcode) throws InvalidDataException {
		int total = 0;
		log
				.info("AdjUploadDAO :importAdjOpeningBalanceLoad()-- entering method");
		ArrayList xlsDataList = new ArrayList();
		List outputList = new LinkedList();
		ArrayList pfIds1 = new ArrayList();
		xlsDataList = commonUtil.getTheList(xlsData, "***");
		Connection conn = null;
		Statement st = null;
		Statement st1 = null;
		Statement st2 = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		EmpMasterBean bean = null;
		AAIEPF2Bean aaiepf2 = null;
		String tempInfo[] = null;
		StringBuffer sb = null;
		StringBuffer sb1 = null;
		String tempData = "";
		FileWriter fw = null;
		String deleteAdjOB = "", deleteempAdjoblog = "", deleteempyearwiseadjob = "";
		char[] delimiters = { '"', ',', '\'', '*', ',' };
		String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
		String uploadFilePath = "";
		String xlsEmpName = "", empName = "", designation = "", station = "", insertAdjOB = "", AdJ_UPLOAD_LOG = "", empAdjoblog = "", empyearwiseadjob = "";
		String adjobyear1 = "";
		FinancialReportDAO frDao = new FinancialReportDAO();
		String pfids = "";
		log.debug("xlsData " + xlsData + "Size" + xlsDataList.size());
		String upld_seq = "select upld_adj_grpid_seq.NEXTVAL as id from dual";
		String upld_seq_id = "";
		String consadjobyear = FORM2_CURRENT_ADJOBYEAR;
		try {
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			uploadFilePath = bundle.getString("upload.folder.path.epf.form2");

			conn = commonDB.getConnection();
			st = conn.createStatement();
			st1 = conn.createStatement();
			st2 = conn.createStatement();

			rs1 = st1.executeQuery(upld_seq);
			if (rs1.next()) {
				upld_seq_id = rs1.getString("id");
			}

			AdJ_UPLOAD_LOG = "insert into AdJ_UPLOAD_LOG(FILENAME,USERNAME,IPADDRESS,AIRPORT,REGION,UPLDPFIDS,"
					+ "ADJID) values(?,?,?,?,?,?,ADJUPDLOG_SEQ.NEXTVAL)";

			pst = conn.prepareStatement(AdJ_UPLOAD_LOG);
			pst.setString(1, fileName.trim());
			pst.setString(2, userName.trim());
			pst.setString(3, ipAddress.trim());
			pst.setString(4, airportcode);
			pst.setString(5, region);
			pst.setString(6, pfids);

			pst.executeUpdate();

			System.out.println("Before for loop" + xlsDataList.size());
			if (xlsDataList.size() < 1) {
				throw new Exception("Empty Sheet");
			}

			for (int i = 0; i < xlsDataList.size(); i++) {
				bean = new EmpMasterBean();
				aaiepf2 = new AAIEPF2Bean();
				System.out.println("After for loop");

				tempData = xlsDataList.get(i).toString();
				tempInfo = tempData.split("@");
				String cpfaccno = "", employeeName = "", employeeNo = "", pensionNo = "", status = "", user = "", adjyear = "", remarks = "", flag = "", priorFlag = "", table = "";
				if (!tempInfo[1].equals("XXX")) {
					bean.setPfid(commonUtil.getSearchPFID1(tempInfo[1].trim()));
					pfids = pfids + bean.getPfid() + ",";
					aaiepf2.setPfid(bean.getPfid());
				} else {
					throw new Exception("PFID Mandatory");
				}
				if (!tempInfo[8].equals("XXX")) {
					adjyear = tempInfo[8].trim();
					int diff = commonUtil.getYeareDifference(adjyear,
							consadjobyear);
					if (diff > 0) {
						throw new Exception(
								"You can't adjustment in future financial year");
					} else if (diff < 0) {
						throw new Exception(
								"2008-09,2009-10,2010-11 are frozen.So,you can adjust as current FY");
					}
					aaiepf2.setAdjobyear(adjyear);

				} else {
					throw new Exception("AdjObYear mandatory");
				}
				if (!tempInfo[2].equals("XXX")) {
					cpfaccno = tempInfo[2].trim();
				} else {
					cpfaccno = "";
				}
				if (!tempInfo[3].equals("XXX")) {
					employeeNo = tempInfo[3].trim();
				}
				if (!tempInfo[4].equals("XXX")) {
					empName = tempInfo[4].trim();
				} else {
					empName = "";
				}
				if (!tempInfo[5].equals("XXX")) {
					designation = tempInfo[5].trim();
				} else {
					designation = "";
				}

				if (!tempInfo[19].equals("XXX")) {
					user = tempInfo[19].trim();
					aaiepf2.setRequestedby(user);
				} else {
					throw new Exception("User Name Mandatory");
				}
				if (!tempInfo[20].equals("XXX")) {
					remarks = tempInfo[20].trim();
					if (remarks.length() < 50) {

						aaiepf2.setRemarks(remarks);
					} else {
						throw new Exception(
								"Remarks length Should be less than 50 Characters");
					}
				} else {
					throw new Exception("Remarks Mandatory");
				}
				if (!tempInfo[21].equals("XXX")) {
					flag = tempInfo[21].trim();

				} else {
					flag = "";

				}

				String monthYear = "01-Apr-2008";
				double empsub = 0.00, empsubInterest = 0.00, aaiAmount = 0.00, aaiInterest = 0.00, outstandadv = 0.00, pencontri = 0.00, pensiontotal = 0.00;
				if (!tempInfo[18].equals("XXX")) {
					station = tempInfo[17].trim();
				} else {
					station = "";
				}
				if (airportcode != null && !bean.getPfid().trim().equals("")) {
					log.info("pfid" + bean.getPfid() + "sheet airportcode "
							+ station.replaceAll(" ", "")
							+ " selected airportcode "
							+ airportcode.replaceAll(" ", ""));

					if (region.equals("CHQIAD")) {
						if (!station.replaceAll(" ", "").equals(
								airportcode.replaceAll(" ", ""))) {
							throw new InvalidDataException(
									"Please make sure that the Seleted Airportcode above  (i.e "
											+ airportcode
											+ ") and is matching with AirportCode given in the excel sheet at Column 17. i.e"
											+ station);
						}
					}
				}
				if (!tempInfo[10].equals("XXX")
						&& !tempInfo[10].trim().equals("")) {
					log.info("ob " + tempInfo[10]);
					empsub = Double.parseDouble(tempInfo[10].trim().replaceAll(
							",", "").trim());
					aaiepf2.setEmpsub(tempInfo[10].trim().replaceAll(",", "")
							.trim());
				} else {
					empsub = 0.00;
					aaiepf2.setEmpsub("0.00");
				}

				if (!tempInfo[11].equals("XXX")
						&& !tempInfo[11].trim().equals("")) {
					empsubInterest = Double.parseDouble(tempInfo[11]
							.replaceAll(",", "").trim());
					aaiepf2.setEmpsubInterest(tempInfo[11].replaceAll(",", "")
							.trim());
				} else {
					empsubInterest = 0.00;
					aaiepf2.setEmpsubInterest("0.00");
				}

				if (!tempInfo[13].equals("XXX")
						&& !tempInfo[13].trim().equals("")) {
					aaiAmount = Double.parseDouble(tempInfo[13].replaceAll(",",
							"").trim());
					aaiepf2.setAaiAmount(tempInfo[13].replaceAll(",", "")
							.trim());
				} else {
					aaiAmount = 0.00;
					aaiepf2.setAaiAmount("0.00");
				}
				if (!tempInfo[14].equals("XXX")
						&& !tempInfo[14].trim().equals("")) {
					aaiInterest = Double.parseDouble(tempInfo[14].replaceAll(
							",", "").trim());
					aaiepf2.setAaiInterest(tempInfo[14].replaceAll(",", "")
							.trim());
				} else {
					aaiInterest = 0.00;
					aaiepf2.setAaiInterest("0.00");
				}
				if (!tempInfo[15].equals("XXX")
						&& !tempInfo[15].trim().equals("")) {
					pensiontotal = Double.parseDouble(tempInfo[15].replaceAll(
							",", "").trim());
					aaiepf2.setPensiontotal(tempInfo[15].replaceAll(",", "")
							.trim());
				} else {
					pensiontotal = 0.00;
					aaiepf2.setPensiontotal("0.00");
				}
				if (!tempInfo[17].equals("XXX")
						&& !tempInfo[17].trim().equals("")) {
					outstandadv = Double.parseDouble(tempInfo[17].replaceAll(
							",", "").trim());
					aaiepf2.setOutstandadv(tempInfo[17].replaceAll(",", "")
							.trim());
				} else {
					outstandadv = 0.00;
					aaiepf2.setOutstandadv("0.00");
				}
				if (!tempInfo[16].equals("XXX")
						&& !tempInfo[16].trim().equals("")) {
					pencontri = Double.parseDouble(tempInfo[16].replaceAll(",",
							"").trim());
					aaiepf2.setPencontri(tempInfo[16].replaceAll(",", "")
							.trim());
				} else {
					pencontri = 0.00;
					aaiepf2.setPencontri("0.00");
				}
				aaiepf2.setMauals("Y");
				if (!bean.getCpfAcNo().trim().equals("")
						|| !bean.getPfid().trim().equals("")) {
					// if(!tempInfo[1].equals("XXX")){
					String checkPFID = "select wetheroption,pensionno,dateofbirth,dateofjoining,employeename from employee_personal_info where to_char(pensionno)='"
							+ bean.getPfid() + "'";
					log.info(checkPFID);
					rs = st1.executeQuery(checkPFID);

					if (!rs.next()) {
						throw new InvalidDataException("PFID "
								+ bean.getPfid().trim() + " doesn't Exist");
					}

				}
				log.info("Data" + aaiAmount + " " + aaiInterest + " " + empsub
						+ " " + empsubInterest + " " + outstandadv + " "
						+ adjyear + " " + remarks + " " + pensiontotal + " "
						+ flag);

				table = "Employee_adj_ob_log";
				empAdjoblog = this.insertQuery(aaiepf2, table);
				log.info("empAdjoblog" + empAdjoblog);
				// st.executeQuery(empAdjoblog);

				st.addBatch(empAdjoblog);
				// adjobyear= '2011-2012' before AAIEPF-2 implimentation
				empyearwiseadjob = "update employee_yearwise_adjob set shtgrupid='"
						+ upld_seq_id
						+ "' where pensionno='"
						+ bean.getPfid()
						+ "' and ((adjobyear='2011-2012' or adjobyear='01-Apr-2011')  or adjobdt='"
						+ aaiepf2.getAdjobyear() + "')";

				log.info("empyearwiseadjob11" + empyearwiseadjob);

				int count = st2.executeUpdate(empyearwiseadjob);
				log.info("count" + count);
				if (count <= 0) {
					empyearwiseadjob = this.insertEmpyearwiseadjobQuery(
							aaiepf2, upld_seq_id);
					st.addBatch(empyearwiseadjob);
					log.info("empyearwiseadjob" + empyearwiseadjob);
				}

			}

			String pfidlist = pfids.substring(0, pfids.lastIndexOf(","));
			log.info("pfidlist" + pfidlist);
			sb = new StringBuffer();
			sb.append(uploadFilePath);
			sb.append("\\");
			sb.append(fileName.trim());
			String originalFileName = sb.toString();
			String file2 = fileName.substring(0, fileName.indexOf("."));
			sb1 = new StringBuffer();
			sb1.append(uploadFilePath);
			sb1.append("\\");
			sb1.append(file2);
			sb1.append(pfidlist);
			sb1.append(".xls");
			String renamedFileName = sb1.toString();

			// This method is used for rename the file
			commonUtil.renameFile(originalFileName, renamedFileName);

			String importStausupdate = "update AdJ_UPLOAD_LOG  set IMPORTSTATUS='Y',UPLDPFIDS='"
					+ pfidlist
					+ "',filename='"
					+ renamedFileName
					+ "' where filename='" + fileName + "'";
			log.info("importStausupdate" + importStausupdate);
			// st.executeQuery(importStausupdate);
			st.addBatch(importStausupdate);
			int count[] = st.executeBatch();
			outputList.add(renamedFileName);
			outputList.add(upld_seq_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			try {
				st2.close();
				st1.close();
				st.close();
				rs2.close();
				pst.close();
				rs1.close();
				rs.close();
				conn.close();
			} catch (Exception e) {

			}
		}
		log.info("ImportDao :importAdjOpeningBalanceLoad leaving method");
		return outputList;
	}

	public String chkDiff(String pensionno, String flag, String empSub,
			String empSubInt, String aaiContri, String aaiContriInt,
			String penContri, String form2id,String jvno,String adjobyear) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String result = "", query = "";
		String condition="";
		if(adjobyear.equals("2010-2011")||adjobyear.equals("2011-2012")||adjobyear.equals("2012-2013")){
			condition=" and t.jvno ='"+jvno+"'";
		}
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			if (flag.equals("E")) {
				query = "select ((round(nvl(diff.pensiontotal, 0),0) + round(nvl(diff.pensionintrst,0),0)) - '"
						+ penContri
						+ "') as pendiff,(round(nvl(diff.empsub,0),0) - '"
						+ empSub
						+ "') as empsubdiff,(round(nvl(diff.empsubintrst,0),0) - '"
						+ empSubInt
						+ "') as empsubintdiff, (round(nvl(diff.aaicontri,0),0) - '"
						+ aaiContri
						+ "') as aaicontridiff , (round(nvl(diff.aaicontriintrst,0),0) - '"
						+ aaiContriInt
						+ "') as aaicontriintdiff  from epis_adj_crtn_form2 diff,epis_adj_crtn_pfid_tracking t where t.pensionno=diff.pensionno and t.adjobyear=diff.adjobyears and t.form2id=diff.form2id and  diff.pensionno = '"
						+ pensionno + "' and t.form2id = '" + form2id + "'"+condition;
				// query="select 'X' as flag from epis_adj_crtn_form2 diff where
				// ((((diff.pensiontotal +
				// diff.pensionintrst)-'"+penContri+"')!=0) or ((diff.empsub-
				// '"+empSub+"')!= 0 or (diff.empsubintrst - '"+empSubInt+"') !=
				// 0) or ((diff.aaicontri - '"+aaiContri+"')!= 0 or
				// (diff.aaicontriintrst - '"+aaiContriInt+"') != 0)) and
				// diff.form2id='"+form2id+"'and diff.pensionno="+pensionno;
			} else {
				query = "select 'X' as flag from v_epis_adj_crnt_diff diff, epis_adj_crtn_transactions trans where diff.pensionno = trans.pensionno and diff.adjobyear=trans.adjobyear and (((diff.pcprincipal - trans.pcprincipal)!= 0 or (diff.pcinterest - trans.pcinterest) != 0) or ((diff.empsubprincipal - trans.empsubscription)!= 0 or (diff.empsubinterest - trans.empsubint) != 0) or ((diff.aainetprincipal - trans.aaicontri)!= 0 or (diff.aainetinterest - trans.aaicontriint) != 0)) and  trans.approvedtype='Approved' and diff.adjobyear='"+adjobyear+"' and diff.pensionno="
						+ pensionno;
			}
			log.info("AdjUploadDAo::chkDiff()==query====" + query);
			rs = st.executeQuery(query);
			log.info("flag errrrrrrrrrrrrrrttttttttttt"+flag);
			if (rs.next()) {
				log.info("flag errrrrrrrrrrr"+flag);
				if (flag.equals("E")) {
					log.info("pendiff" + rs.getString("pendiff") + "ee"
							+ rs.getString("empsubdiff"));
					if (rs.getString("pendiff").equals("0")
							&& rs.getString("empsubdiff").equals("0")
							&& rs.getString("empsubintdiff").equals("0")
							&& rs.getString("aaicontridiff").equals("0")
							&& rs.getString("aaicontriintdiff").equals("0")) {
						result = "NODIFF";
					} else {
						result = "DIFF";
					}
				} else {
					result = "DIFF";
				}

			} else {
				result = "NOTEXIST";
			}

		} catch (Exception e) {
			log.info("===========>>>>>>>>>> Exception <<<<<<<<<============="
					+ e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("====result====" + result+ "flag"+flag);

		return result;
	}
	public String checkForm2ID(AAIEPF2Bean aaiepf2) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String result = "", query = "";
		
	
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			
				query = "select form2id from epis_adj_crtn_pfid_tracking t where t.form2id='"+aaiepf2.getForm2Id()+"' and t.adjobyear='"+aaiepf2.getRemarks()+"' and form2status='Y'";
				
		
			log.info("AdjUploadDAo::checkForm2ID==query====" + query);
			rs = st.executeQuery(query);
			log.info("Remarks======"+aaiepf2.getRemarks()+"id===="+aaiepf2.getForm2Id());
			if (rs.next()) {
				log.info("update second time with same id"+rs.getString("form2id"));
				
				result="EXIST";

			} else {
				result = "NOTEXIST";
			}

		} catch (Exception e) {
			log.info("===========>>>>>>>>>> Exception <<<<<<<<<============="
					+ e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("====result====" + result);

		return result;
	}
}
