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
import java.util.List;
import java.util.StringTokenizer;

import aims.bean.CrtnsMadeInPcBean;
import aims.bean.EmolumentslogBean;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeeCardReportInfo;
import aims.bean.EmployeePensionCardInfo;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinacialDataBean;
import aims.bean.FinancialYearBean;
import aims.bean.PensionContBean;
import aims.bean.StationWiseRemittancebean;
import aims.bean.TempPensionTransBean;
import aims.bean.adjcrnt.AdjCrntSaveDtlBean;
import aims.bean.adjcrnt.SuppPCBean;
import aims.bean.adjcrnt.SuppPCBeanData;
import aims.common.CommonUtil;
import aims.common.Constants;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;

/**
 * @param args
 */
/*
 * ########################################## 

 #Date					Developed by			Issue description
 #18-Dec-2012			  Radha			  	  Getting Display pfcard remarks in impact Calc from 2008-2009 onwards
 #16-Oct-2012			  Prasad			  Remove Editdate field in insertEmployeeTransData method
 #08-May-2012             Prasad              Given feasility what ever pfids are unblocked
 #29-Feb-2012             Prasad              Adding difference values into form-2 report.
 #29-Feb-2012              Prasad             Added new fields for uploading excelsheet
 #29-Feb-2012              Prasad             Adding new methods Imp Calcultor log report 
 #29-Feb-2012               Radha             For Making Prev Grnd Totals not inserted upto Apporved ,Aftr Approved * new record shuld be inserted 
 #23-Feb-2012                Radha            For Records inserting into  transactions table #
 ########################################
 */
public class AdjCrtnDAO implements Constants {

	Log log = new Log(AdjCrtnDAO.class);

	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();
	//	By Radha on 23-May-2012 for getting data based on pensionno from 2008 onwards
	//	 By Radha On 21-Mar-2012 for getting ob values
	// By Radha On 01-Dec-2011 inserting data with selected pensionno default if
	// no pensionno is available r not
	// By Radha On 14-Oct-2011 for Cpf Corrections from1995 to tillDate
	// By Radha On 14-Sep-2011 for Cpf Corrections from1995 to tillDate
	
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
		String chkpfid = "", commdata = "", revisedFlagQry = "", loansQuery = "", addcontrisql="",advancesQuery = "", sql = "",sqlAftr2008="", updateloans = "", updateadvances = "", loandate = "", subamt = "", contamt = "", advtransdate = "", advanceAmt = "", monthYear = "", tableName = "";
		String cpfregionstrip = "", condition = "", preparedString = "", dojcpfaccno = "", dojemployeeno = "", dojempname = "", dojstation = "", dojregion = "",	insertDummyRecord="", chkForRecord=""; 
		String[] cpfregiontrip = null;
		String[] cpfaccnos = null;
		String[] regions = null;
		String[] commondatalst = null;
		String [] years={"1995-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013","2013-2014","2014-2015","2015-2016","2016-2017","2017-2018"};
		int result = 0, loansresult = 0, advancesresult = 0, transID = 0, batchId = 0,insertDummyRecordResult=0;
		monthYear = commonUtil.getCurrentDate("dd-MMM-yyyy");
		EmpMasterBean empBean = new EmpMasterBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			st1 = con.createStatement();
			this.chkDOJ(con, pfId);
			cpfregionstrip = this.getEmployeeCpfacno(con, pfId);
			String[] pfIDLists = cpfregionstrip.split("=");
			preparedString = commonDAO.preparedCPFString(pfIDLists);
			System.out.println("preparedString====================" + preparedString);
			System.out.println("frmName====================" + frmName);
			if (frmName.equals("adjcorrections")) {
				tableName = "EPIS_ADJ_CRTN";
			} else if (frmName.equals("form7/8psadjcrtn")) {
				tableName = "EPIS_ADJ_CRTN_FORM78PS";
			}
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
						+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS   from employee_pension_validate where empflag='Y' and (pensionno="
						+ pfId + " or " + preparedString
						+ ") and monthyear <='31-Mar-2008')";
				
				sqlAftr2008 =  "insert into "
					+ tableName
					+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
					+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
					+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS,ADDITIONALCONTRI)   (select  "
					+ pfId
					+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
					+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
					+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
					+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS, ADDITIONALCONTRI   from employee_pension_validate where empflag='Y' and   pensionno="
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
				if (mappingFlag.equals("U") && upflag.equals("N")) {

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
				
				}
				log.info("----------sql-------------" + sql);
				log.info("----------sqlAftr2008-------------" + sqlAftr2008);
				st.addBatch(sql);
				st.addBatch(sqlAftr2008);
				if (frmName.equals("adjcorrections")) {
				for(int i=0;i<years.length;i++){
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"
							+ pfId + ",'"+years[i]+"',sysdate)";
					log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
					st.addBatch(episAdjCrtnLog);
					episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"
						+ username + "','" + ipaddress + "',sysdate)";
					log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
					st.addBatch(episAdjCrtnLogDtl);	 
				}
				
				revisedFlagQry = " update "
					+ tableName
					+ "    set  reviseepfemolumentsflag='N' where  pensionno="
					+ pfId + " and empflag='Y'"; 
			
				log.info("----------revisedFlagQry-------------"
					+ revisedFlagQry);
				st.addBatch(revisedFlagQry); 
				}
				
				int insertCount[] = st.executeBatch();
				log.info("insertCount  " + insertCount.length);
				if (Integer.parseInt(years[0])>=2015) {
				st = null;
				st = con.createStatement();
				addcontrisql = " update epis_adj_crtn c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
						+ pfId+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+ pfId+" and monthyear ='01-May-2015'";
				log.info("----------addcontrisql-------------" + addcontrisql);
				rs1 = st.executeQuery(addcontrisql);
				}
				st = null;
				st = con.createStatement();
				loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
						+ pfId;
				log.info("----------loansQuery-------------" + loansQuery);
				rs1 = st.executeQuery(loansQuery);
				while (rs1.next()) {
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
						loansresult = st.executeUpdate(updateloans);
					}else{
						insertDummyRecord= " insert into epis_adj_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'))";       
						 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
						 loansresult = st.executeUpdate(updateloans);
					}
					log	.info("----------updateloans-------------"	+ updateloans);
					
				}
				advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
						+ pfId;
				log
						.info("----------advancesQuery-------------"
								+ advancesQuery);
				st = con.createStatement();
				rs2 = st.executeQuery(advancesQuery);
				while (rs2.next()) {
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
						advancesresult = st.executeUpdate(updateadvances);
						 
					}else{
						insertDummyRecord= " insert into epis_adj_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'))";       
						insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
						advancesresult = st.executeUpdate(updateadvances);
					}
					log.info("----------updateadvances-------------"+ updateadvances);
				}
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
				rs3 = st.executeQuery(loggeridseq);
				if (rs3.next()) {
					logid = Integer.parseInt(rs3.getString("loggerid"));
					log.info("logid  test" + logid);
				} else {
					st = null;
					st = con.createStatement();
					episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
							+ pfId
							+ ",'"+years[i]+"',sysdate,'This pfid already ported before implmenation logic')";
					st.executeUpdate(episAdjCrtnLog);
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
					result = st.executeUpdate(episAdjCrtnLogDtl);
				}
				log.info("count :" + result);
				log.info("--------Data already exists----------");
			}
			}
			// Mapping Data Tracking
			if (mappingFlag.equals("M")) {
				String mappingPreparedString = "(CPFACCNO='" + cpfacno
						+ "' AND REGION='" + region + "')";
				batchId = this.getBatchId(con);
				String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TEMP  where pensionno='"
						+ pfId + "' and ADJOBYEAR ='1995-2008'";
				rs4 = st.executeQuery(transIDQry);
				if (rs4.next()) {
					transID = Integer.parseInt(rs4.getString("TRANSID"));
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
				result = st.executeUpdate(sqlForMapping1995to2008);
				log.info("result" + result);
			}
			if (mappingFlag.equals("U")) {
				// String mappingPreparedString="(CPFACCNO='"+cpfacno+"' AND
				// REGION='"+region+"')";
				batchId = this.getBatchId(con);
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
				result = st.executeUpdate(sqlForMapping1995to2008);
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
			sqlQuery = "SELECT CPFACNO,REGION FROM epis_info_adj_crtn WHERE EMPSERIALNUMBER='"
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

	//	By Radha on 04-Jul-2012 for getting for 2011-2012 data
	//	Replace the method
	//By Radha on 28-Mar-2012 for getting Form2Status & displaying pensionIntrst for 1995-2008
	public ArrayList getPCAdjDiff(String pfId, String adjOBYear) {
		log.info("AdjCrtnDAO :getPCAdjDiff() Entering Method ");
		Connection con = null;

		String[] reportYears = { "1995-2008", "2008-2009", "2009-2010", "2010-2011","2011-2012","2012-2013","2013-2014","2014-2015","2015-2016","2016-2017","2017-2018"};
		String approvedStage="",sql="";
		EmployeePensionCardInfo bean = null;
		ResultSet rs = null;
		ArrayList pcTotalDiffAmnt = new ArrayList();

		try {
			con = DBUtils.getConnection();
			Statement st = con.createStatement();

			for (int i = 0; i < reportYears.length; i++) {
				if(reportYears[i].equals("1995-2008")){
					  sql = " select diff.ADJOBYEAR AS ADJOBYEAR, nvl(EMOLUMENTS, 0.0) as EMOLUMENTS, nvl(CPFTOTAL, 0.0) as CPFTOTAL,(nvl(PENSIONTOTAL, 0.0) + nvl(PENSIONINTRST, 0.0)) as PENSIONTOTAL,"
			       		+" nvl(PFTOTAL, 0.0) as PFTOTAL, (nvl(EMPSUB, 0.0) + nvl(EMPSUBINTRST, 0.0)) as EMPSUBTOT,  (nvl(AAICONTRI, 0.0) + NVL(AAICONTRIINTRST, 0.0)) as AAICONTRITOT,tracking.verifiedby as APPROVEDTYPE,tracking.FORM2STATUS as  FORM2STATUS,tracking.FROZEN as FROZEN,tracking.form2id as form2id"
			       		+" from epis_adj_crtn_pc_totals_diff diff,epis_adj_crtn_pfid_tracking tracking where diff.pensionno = tracking.pensionno and  diff.adjobyear = tracking.adjobyear  and diff.pensionno= "+pfId+"  and diff.adjobyear ='"+reportYears[i]+"'";
			 
				}else{
					  sql = " select diff.ADJOBYEAR AS ADJOBYEAR, nvl(EMOLUMENTS, 0.0) as EMOLUMENTS, nvl(CPFTOTAL, 0.0) as CPFTOTAL,(nvl(PENSIONTOTAL, 0.0) - nvl(ADJPENSIONCONTRIINTRST, 0.0)) as PENSIONTOTAL,"
			       		+" nvl(PFTOTAL, 0.0) as PFTOTAL, (nvl(EMPSUB, 0.0) + nvl(EMPSUBINTRST, 0.0)) as EMPSUBTOT,  (nvl(AAICONTRI, 0.0) + NVL(AAICONTRIINTRST, 0.0)) as AAICONTRITOT,tracking.verifiedby as APPROVEDTYPE,tracking.FORM2STATUS as  FORM2STATUS,tracking.FROZEN as FROZEN,tracking.form2id as form2id"
			       		+" from epis_adj_crtn_pc_totals_diff diff,epis_adj_crtn_pfid_tracking tracking where diff.pensionno = tracking.pensionno and  diff.adjobyear = tracking.adjobyear  and diff.pensionno= "+pfId+"  and diff.adjobyear ='"+reportYears[i]+"'";
			 
				}
				
				log.info("sql " + sql);

				rs = st.executeQuery(sql);
				if (rs.next()) {

					bean = new EmployeePensionCardInfo();
					if (rs.getString("ADJOBYEAR") != null) {
						bean.setReportYear(rs.getString("ADJOBYEAR"));
					} else {
						bean.setReportYear("---");
					}
					if (rs.getString("form2id") != null) {
						bean.setForm2id(rs.getString("form2id")) ;
					} else{
						bean.setForm2id("");
					}
					if (rs.getString("EMOLUMENTS") != null) {
						bean.setEmolumentsDiff(rs.getString("EMOLUMENTS"));
					} else {
						bean.setEmolumentsDiff("0.00");
					}
					if (rs.getString("CPFTOTAL") != null) {
						bean.setCpfTotDiff(rs.getString("CPFTOTAL"));
					} else {
						bean.setCpfTotDiff("0.00");
					}
					if (rs.getString("PENSIONTOTAL") != null) {
						bean.setPensionContriTotDiff(rs
								.getString("PENSIONTOTAL"));
					} else {
						bean.setPensionContriTotDiff("0.00");
					}
					if (rs.getString("PFTOTAL") != null) {
						bean.setPFTotDiff(rs.getString("PFTOTAL"));
					} else {
						bean.setPFTotDiff("0.00");
					}
					if (rs.getString("EMPSUBTOT") != null) {
						bean.setEmpSubTotDiff(rs.getString("EMPSUBTOT"));
					} else {
						bean.setEmpSubTotDiff("0.00");
					}
					if (rs.getString("AAICONTRITOT") != null) {
						bean.setAaiContriDiff(rs.getString("AAICONTRITOT"));
					} else {
						bean.setAaiContriDiff("0.00");
					}
					if (rs.getString("APPROVEDTYPE") != null) {
						approvedStage = rs.getString("APPROVEDTYPE");
						if(approvedStage.equals("Initial,Approved")){
							approvedStage="Approved";
							bean.setApprovedStage(approvedStage);  
						}else{
							bean.setApprovedStage(approvedStage); 
						}
					} else {
						bean.setApprovedStage("");
					}
					
					if (rs.getString("FORM2STATUS") != null) {
						bean.setForm2Status(rs.getString("FORM2STATUS")) ;
					} else{
						bean.setForm2Status("N");
					}
					if (rs.getString("FROZEN") != null) {
						bean.setFrozen(rs.getString("FROZEN")) ;
					} else{
						bean.setFrozen("N");
					}
					pcTotalDiffAmnt.add(bean);

				} else {
					bean = new EmployeePensionCardInfo();

					bean.setReportYear((String) reportYears[i]);
					bean.setEmolumentsDiff("0.00");
					bean.setCpfTotDiff("0.00");
					bean.setPensionContriTotDiff("0.00");
					bean.setPFTotDiff("0.00");
					bean.setEmpSubTotDiff("0.00");
					bean.setAaiContriDiff("0.00");
					bean.setApprovedStage("");
					bean.setForm2Status("N");
					bean.setFrozen("N");
					pcTotalDiffAmnt.add(bean);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
			log.info("error" + e.getMessage());
		}
		log.info("AdjCrtnDAO :getPCAdjDiff() Leaving Method ");
		return pcTotalDiffAmnt;
	}

	// By Prasad on 13-Feb-2012 for Mapping Data in Adj Calc & uploading Mapping
	// Data
	// By Radha on 04-Feb-2012 for adding verifiedby field
	// By Radha on 09-Jan-2012 for Record Tracking in Adj Screen
	public int insertRecordForAdjCtrnTracking(String empserialNO,
			String cpfAccno, String adjOBYear, String reasonForInsert,
			String username, String ipaddress) throws InvalidDataException {
		Connection con = null;
		int result = 0;
		try {
			con = commonDB.getConnection();
			result = this.insertRecordForAdjCtrnTracking(con, empserialNO,
					cpfAccno, adjOBYear, reasonForInsert, username, ipaddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		return result;
	}

	public int insertRecordForAdjCtrnTracking(Connection con,
			String empserialNO, String cpfAccno, String adjOBYear,
			String reasonForInsert, String username, String ipaddress)
			throws InvalidDataException {

		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", insertQry = "", updateQry = "", reason = "", insertcondition = "", updateondition = "",adjProcessedYear="01-Apr-2011";
		int result = 0, trackingId = 0;
		try {

			st = con.createStatement();
			if (reasonForInsert.equals("Edited")) {
				reason = "E";
				insertcondition = "DATAMODIFIED";
				updateondition = "DATAMODIFIED='" + reason;
				// sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking
				// where DATAMODIFIED='E' and pensionno="+empserialNO;
			} else if (reasonForInsert.equals("Mapped")) {
				reason = "M";
				insertcondition = "DATAMAPPED";
				updateondition = "DATAMAPPED='" + reason;
				// sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking
				// where DATAMAPPED='M' and pensionno="+empserialNO;

			} else if (reasonForInsert.equals("Upload")) {
				reason = "U";
				insertcondition = "DATAMAPPED";
				updateondition = "DATAMAPPED='" + reason;

			}
			if (reasonForInsert.equals("Edited")) {
				// common Cond
				sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking where   pensionno="
						+ empserialNO + " and adjobyear='" + adjOBYear + "'";
			} else if (reasonForInsert.equals("Mapped")
					|| reasonForInsert.equals("Upload")) {
				sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking where   pensionno="
						+ empserialNO;
			}
			log.info("===adjOBYear===" + adjOBYear + "=cpfAccno==" + cpfAccno
					+ "-");
			updateQry = " update epis_adj_crtn_pfid_tracking set  "
					+ updateondition + "',USERNAME='" + username
					+ "',COMPUTERNAME='" + ipaddress
					+ "', UPDATEDDATE= sysdate where pensionno =" + empserialNO
					+ " and ADJOBYEAR='" + adjOBYear + "'";

			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				log
						.info("AdjCrtnDAO::insertRecordForAdjCtrnTracking()==updateQry=="
								+ updateQry);
				result = st.executeUpdate(updateQry);
			} else {

				trackingId = this.getPfidTrackingId(con);
				insertQry = "insert into epis_adj_crtn_pfid_tracking(PENSIONNO,CPFACNO,ADJOBYEAR,"
						+ insertcondition
						+ ",USERNAME,COMPUTERNAME,UPDATEDDATE,TRACKINGID,ADJPROCESSEDYEAR)values ("
						+ empserialNO
						+ ",'"
						+ cpfAccno
						+ "','"
						+ adjOBYear
						+ "','"
						+ reason
						+ "','"
						+ username
						+ "','"
						+ ipaddress
						+ "',sysdate," + trackingId + ",'"+Constants.FORM2_CURRENT_ADJOBYEAR+"')";

				log
						.info("AdjCrtnDAO::insertRecordForAdjCtrnTracking()==insertQry=="
								+ insertQry);
				result = st.executeUpdate(insertQry);
			}

		} catch (SQLException e) {
			throw new InvalidDataException(e.getMessage());
		} catch (Exception e) {
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return result;
	}

	// By Radha On 23-Feb-2012 for Unique Id for traking pfid transactions
	public int getPfidTrackingId(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		int trackingId = 0;
		try {
			st = con.createStatement();
			sqlQuery = "select crtn_tracking_id.nextval as trackingid from dual";
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				trackingId = rs.getInt("trackingid");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return trackingId;
	}
//	 By Radha On 31-Mar-2012 for Unique Id for traking pfid transactions
	public int getPfidTrackingIdFor7PS(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		int trackingId = 0;
		try {
			st = con.createStatement();
			sqlQuery = "select crtn78ps_tracking_id.nextval as trackingid from dual";
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				trackingId = rs.getInt("trackingid");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return trackingId;
	}
	public String getNotFinalizedAdjObYear(String pfid, String frmName) {
		String sqlQuery="",adjobyear="",adjObYears="";	
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		int i = 0;		 
		sqlQuery = "select distinct adjobyear from epis_adj_crtn_prv_pc_totals eacprv, epis_adj_crtn_emol_log_temp temp"
				+ "  where eacprv.transid =temp.transid and eacprv.pensionno =temp.pensionno  and temp.pensionno ="
				+ pfid;
		log.info("AdjCrtnDAO::getNotFinalizedAdjObYear()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while(rs.next()){ 
				 
				if(rs.getString("adjobyear")!=null){
					log.info(" ---"+rs.getString("adjobyear"));
					adjobyear= rs.getString("adjobyear");
					i++;
					if(i==1){
						adjObYears = adjobyear;
					}else{
						adjObYears = adjObYears+":"+adjobyear;
					}
				}
				  
			}
			/*
			 * String adjObYears = new String[i]; for(int k=0;k<adjObYearList.length;k++){
			 * adjObYears =adjObYearList[k]; }
			 */
			log.info("==adjObYears==" + adjObYears	+ "-===i value==" + i);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return adjObYears;
	}
	//On 02-Mar-2012 By Radha for getting  blockedYears 
	public String getblockedAdjObYears(String pfid, String frmName) {
		String sqlQuery = "", blockedyear = "",blockedYears="";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		 
		int i = 0; 
		sqlQuery = " select adjobyear from epis_adj_crtn_pfid_tracking where    BLOCKED ='Y' and pensionno ="+pfid;
				 
		log.info("AdjCrtnDAO::getblockedAdjObYear()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {

				if (rs.getString("adjobyear") != null) {
					log.info(" ---" + rs.getString("adjobyear"));
					i++;
					blockedyear = rs.getString("adjobyear");
					if(i==1){
						blockedYears = blockedyear;
					}else{
						blockedYears = blockedYears + ":" + blockedyear;
					}
				}
				
			}
			/*
			 * String adjObYears = new String[i]; for(int k=0;k<adjObYearList.length;k++){
			 * adjObYears =adjObYearList[k]; }
			 */
			log.info("==blockedYears==" +blockedYears);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return blockedYears;
	}
	// By Radha on 23-Feb-2012 for adding upload condition
	// By Radha on 09-Jan-2012 for chking whether any modification done r not in
	// Adj Calc Screen in new Table
	// By Radha on 23-Dec-2011 for chking whether any modification done r not in
	// Adj Calc Screen
	public String chkPfidinAdjCrtnTracking(String pfid, String frmName) {
		String sqlQuery="",chkpfidyears="",tableName="",adjobyear="";	
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		int i=0;
		if (frmName.equals("adjcorrections")) {
			tableName = "epis_adj_crtn_pfid_tracking";
		}
		sqlQuery = "select adjobyear  from " + tableName + " where   pensionno= " + pfid
				+ " and (DATAMODIFIED='E' or DATAMAPPED='M' or DATAMAPPED='U')";
		log.info("--chkPfidinAdjCrtnTracking()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while(rs.next()){ 
				if(rs.getString("adjobyear")!=null){
					adjobyear = rs.getString("adjobyear");
					 i++;
					 if(i==1){
						 chkpfidyears = adjobyear;
					 }else{
						 chkpfidyears = chkpfidyears + ":" + adjobyear;
					 }
				}
				log.info("--chkpfidyears --"+chkpfidyears);
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

		return chkpfidyears;
	}

	public String chkPfidinAdjCrtn(String pfid, String frmName) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		if (frmName.equals("adjcorrections")) {
			tableName = "EPIS_ADJ_CRTN";
		} else if (frmName.equals("form7/8psadjcrtn")) {
			tableName = "EPIS_ADJ_CRTN_FORM78PS";
		}
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
	public String chkPfidinAdjCrtnBRF(String pfid) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		
		sqlQuery = "select * from epis_pfid_year_wise_frozen where   pensionno= " + pfid+" union" +
		" select * from epis_pfid_year_wise_frozenpc where   pensionno= " + pfid;
		log.info("--chkPfidinAdjCrthBRF---" + sqlQuery);
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
	//	 By Radha on 07-Jun-2012 for change Integer to Double value
	// By Radha on 28-Feb-2012 for inserting records on tracking id base & haing
	// diff records for each sloy
	// By Radha on 23-Feb-2012 for records inserting into transactions table
	// By Radha on 04-Feb-2012 for Record Tracking in Adj Screen
	public int updateStageWiseStatusInAdjCtrn(String empserialNO,
			String processedStage,String reportYear,String form2Status,String jvno, String userName, String loginUserId,
			String userRegion, String loginUsrStation, String loginUsrDesgn) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", insertQry = "", updateQry = "", trackingId = "", signPath = "", adjObYear = "",remarks="",updatetrackingid="";
		String chkStatus="", chkChqApproverFlag="false";
		ArrayList adjObYearList = new ArrayList();
		EmployeePensionCardInfo empPensionInfo = new EmployeePensionCardInfo();
		int result = 0, transId = 0;
		double   empSub=0, empSubIntrst=0,aaiContri=0,aaiContriIntrst=0,pcPrinciple=0,pcIntrst=0	;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			if (processedStage.equals("Approved")) {
				updateQry = " update epis_adj_crtn_pfid_tracking set  verifiedby=verifiedby||','||'"
						+ processedStage
						+ "' ,apporvedby='"
						+ userName
						+ "' ,jvno='"+jvno+"' where pensionno =" + empserialNO+" and  adjobyear ='" + reportYear+"' ";

			} else {
				updateQry = " update epis_adj_crtn_pfid_tracking set  verifiedby='"
						+ processedStage + "' where pensionno =" + empserialNO+" and  adjobyear ='" + reportYear+"'";
			}
 
			//chkStatus = this.getAdjCrtnApprovedStatus(con,empserialNO, reportYear); 
			//log.info("updateStageWiseStatusInAdjCtrn()--chkStatus---" + chkStatus);
			 
			if (processedStage.equals("CHQApproved")){
				remarks="After Approved"; 
			}else{
				log
				.info("AdjCrtnDAO::updateStageWiseStatusInAdjCtrn()==updateQry=="
						+ updateQry);
			result = st.executeUpdate(updateQry);
			} 
		 
			// Adding Data in transaction table
			if(form2Status.equals("Y")){
				chkChqApproverFlag="true";
			}
			 
			adjObYearList = (ArrayList) this.getFinalizedAdjObYear(con,
					empserialNO,reportYear,chkChqApproverFlag);

			if (!processedStage.equals("Reject")) {
				if (loginUserId.equals("30")) {
					signPath = Constants.ADJCRTN_SIGNATURES_SEHGAL;

				} else if (loginUserId.equals("79")) {
					signPath = Constants.ADJCRTN_SIGNATURES_NIMESH;

				} else if (loginUserId.equals("43")) {
					signPath = Constants.ADJCRTN_SIGNATURES_SHIKHA;

				} else if (loginUserId.equals("240")) {
					signPath = Constants.ADJCRTN_SIGNATURES_MONIKA;
				} else {
					signPath = "";
				}
				log.info(" ==loginUserId==" + insertQry);
				for (int i = 0; i < adjObYearList.size(); i++) {
					empPensionInfo = (EmployeePensionCardInfo) adjObYearList
							.get(i);
					sqlQuery = "select trackingid as trackingid from epis_adj_crtn_pfid_tracking where pensionno="
							+ empserialNO
							+ " and adjobyear='"
							+ empPensionInfo.getReportYear() + "'";
					log.info(" ==sqlQuery==" + sqlQuery);
					rs = st.executeQuery(sqlQuery);
					if (rs.next()) {
						if (rs.getString("trackingid") != null) {
							trackingId = rs.getString("trackingid");
						}
					}
					log.info(" ==trackingId==" + trackingId);
					transId = this.getTransIdForAdjTransactions(con);

					log.info(" ==adjObYear==" + empPensionInfo.getReportYear());
					
					 empSub= Double.parseDouble(empPensionInfo.getEmpSub())- Double.parseDouble(empPensionInfo.getAdjEmpSubInterest());
					 empSubIntrst = Double.parseDouble(empPensionInfo.getEmpSubInterest()) + Double.parseDouble(empPensionInfo.getAdjEmpSubInterest());
					 aaiContri= Double.parseDouble(empPensionInfo.getAaiContri()) - Double.parseDouble(empPensionInfo.getAdjAaiContriInterest());
					 aaiContriIntrst= Double.parseDouble(empPensionInfo.getAaiContriInterest()) + Double.parseDouble(empPensionInfo.getAdjAaiContriInterest());
					 pcPrinciple = Double.parseDouble(empPensionInfo.getPensionTotal()) - Double.parseDouble(empPensionInfo.getAdjPensionInterest());
					 pcIntrst = Double.parseDouble(empPensionInfo.getPensionInterest()) + Double.parseDouble(empPensionInfo.getAdjPensionInterest());
					
					 updatetrackingid =  this.chkPfidinAdjCrtnTrans(con,empserialNO,reportYear,processedStage); 
						 
					 
					insertQry = "insert into epis_adj_crtn_transactions (PENSIONNO ,ADJOBYEAR, EMPSUBSCRIPTION,EMPSUBINT , AAICONTRI ,  AAICONTRIINT , PCPRINCIPAL ,PCINTEREST ,TRACKINGID,TRANSID,APPROVEDTYPE, DESIGNATION ,  APRVDSIGNNAME , APPROVEDBY ,   APPROVEDDATE ,APPSTATION,APPREGION,SIGNPATH,REMARKS )values( "
							+ empserialNO
							+ ",'"
							+ empPensionInfo.getReportYear()
							+ "','"
							+ empSub
							+ "','"
							+ empSubIntrst
							+ "','"
							+ aaiContri
							+ "','"
							+ aaiContriIntrst
							+ "','"
							+ pcPrinciple
							+ "','"
							+ pcIntrst
							+ "','"
							+ trackingId
							+ "','"
							+ transId
							+ "','"
							+ processedStage
							+ "','"
							+ loginUsrDesgn
							+ "','"
							+ userName
							+ "','"
							+ loginUserId
							+ "',sysdate,'"
							+ loginUsrStation
							+ "','"
							+ userRegion
							+ "','"
							+ signPath + "','"+remarks+"')";
					
						 
				
				if(!updatetrackingid.equals("")){
					if(processedStage.equals("Initial")){
					remarks="Record Rejected Before";
					
					updateQry=" update   epis_adj_crtn_transactions  set   EMPSUBSCRIPTION ='"+empSub+"',EMPSUBINT ='"+empSubIntrst+"' , AAICONTRI='"+aaiContri+"'  ,  AAICONTRIINT ='"+aaiContriIntrst+"',"
					+ " PCPRINCIPAL ='"+pcPrinciple+"',PCINTEREST = '"+pcIntrst+"',APPROVEDDATE=sysdate,APRVDSIGNNAME='"+userName+"'  , APPROVEDBY='"+loginUserId+"'" 
				    +" , APPSTATION='"+loginUsrStation+"',APPREGION='"+userRegion+"',SIGNPATH='"+signPath+"',DESIGNATION='"+loginUsrDesgn+"',REMARKS='"+remarks+"' where pensionno ="+empserialNO+" and  TRACKINGID='"+updatetrackingid+"'"; 
			
					log.info(" ==updateQry==" + updateQry);
					st.executeUpdate(updateQry);
					}
				}else{
					log.info(" ==insertQry==" + insertQry);
					st.executeUpdate(insertQry);
				}
			 }
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return result;
	}
	 
	public String chkPfidinAdjCrtnTrans(Connection con, String empserialNO,String reportYear,String processedStage){	
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "",trackingid=""; 
		 
		try {
			st = con.createStatement();
			sqlQuery = "select trackingid  from epis_adj_crtn_transactions where pensionno='"+empserialNO+"' and adjobyear='"+reportYear+"' and  approvedtype='"+processedStage+"'";
			log.info("chkPfidinAdjCrtnTrans()=="+sqlQuery);
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				if( rs.getString("trackingid")!=null){
				trackingid = rs.getString("trackingid");
				}
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return trackingid;
	}
	// By Radha On 23-Feb-2012 for Unique Id for transid in transaction table
	public int getTransIdForAdjTransactions(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		int transId = 0;
		try {
			st = con.createStatement();
			sqlQuery = "select crtn_trans_id.nextval as crtn_trans_id from dual";
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				transId = rs.getInt("crtn_trans_id");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return transId;
	}

	// new method on 13-Feb-2012
	public int saveCurrPctoals(String empserialNO, String adjObYear,
			ArrayList currPcTotals) {
		String insertOrUpdateQuery = "", deleteQuery = "", transID = "", trackingQuery = "";
		String insertPreviousTotal = "";
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs3 = null;
		ResultSet rs2 = null;
		int result = 0;
		double EmolumentsTot = 0.0, cpfTotal = 0.0, cpfIntrst = 0.0, pensionTotal = 0.0, pensionIntrst = 0.0, pfContri = 0.0, pfContriIntrst = 0.0;
		EmolumentsTot = ((Double) currPcTotals.get(0)).doubleValue();
		cpfTotal = ((Double) currPcTotals.get(1)).doubleValue();
		cpfIntrst = ((Double) currPcTotals.get(2)).doubleValue();
		pensionTotal = ((Double) currPcTotals.get(3)).doubleValue();
		pensionIntrst = ((Double) currPcTotals.get(4)).doubleValue();
		pfContri = ((Double) currPcTotals.get(5)).doubleValue();
		pfContriIntrst = ((Double) currPcTotals.get(6)).doubleValue();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();

			// transID = this.getTransidSequence(con);
			String selectinsertPreviousTotal = "select *  from epis_adj_crtn_prv_pc_totals where pensionno='"
					+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "'";
			rs3 = st.executeQuery(selectinsertPreviousTotal);

			// int count1 = rs3.getInt(1);
			if (rs3.next()) {
				insertPreviousTotal = "update  epis_adj_crtn_prv_pc_totals latesttbl set(EMOLUMENTS,CPFTOTAL,CPFINTEREST,PENSIONTOTAL,PENSIONINTEREST,PFTOTAL,PFINTEREST,EMPSUB,EMPSUBINTEREST,AAICONTRI,AAICONTRIINTEREST,LASTACTIVE,ADJEMPSUBINTRST,ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,remarks)=(select EMOLUMENTS,CPFTOTAL,CPFINTEREST,PENSIONTOTAL,PENSIONINTEREST,PFTOTAL,PFINTEREST,EMPSUB,EMPSUBINTEREST,AAICONTRI,AAICONTRIINTEREST,sysdate,ADJEMPSUBINTRST,ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,remarks from epis_adjcrtn_prvpctotals_temp temp where  latesttbl.pensionno=temp.pensionno ) where latesttbl.pensionno='"
						+ empserialNO
						+ "'and latesttbl.ADJOBYEAR ='"
						+ adjObYear + "'";
			} else {

				insertPreviousTotal = "insert into epis_adj_crtn_prv_pc_totals (select * from epis_adjcrtn_prvpctotals_temp where pensionno='"
						+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "')";
			}
			log.info("insertPreviousTotal" + insertPreviousTotal);
			result = st.executeUpdate(insertPreviousTotal);

			String transIDQry = "select TRANSID from epis_adj_crtn_prv_pc_totals where pensionno='"
					+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "'";
			rs2 = st.executeQuery(transIDQry);
			if (rs2.next()) {
				transID = rs2.getString("TRANSID");
			}

			String selectADJCurrentPCTotals = "select count(*) as count from epis_adj_crtn_curnt_pc_totals where pensionno='"
					+ empserialNO + "' and  ADJOBYEAR='" + adjObYear + "'";
			log.info("--saveCurrPctoals----selectADJCurrentPCTotals--"
					+ selectADJCurrentPCTotals);
			rs = st.executeQuery(selectADJCurrentPCTotals);
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					// trackingQuery="select * from epis_adj_crtn_pfid_tracking
					// where pensionno="+ empserialNO;
					insertOrUpdateQuery = "insert into epis_adj_crtn_curnt_pc_totals(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST , PFTOTAL,PFINTEREST ,CREATIONDATE,REMARKS) values('"
							+ empserialNO
							+ "','"
							+ transID
							+ "','"
							+ adjObYear
							+ "','"
							+ EmolumentsTot
							+ "','"
							+ cpfTotal
							+ "','"
							+ cpfIntrst
							+ "','"
							+ pensionTotal
							+ "','"
							+ pensionIntrst
							+ "','"
							+ pfContri
							+ "','"
							+ pfContriIntrst + "', sysdate,'Through Mapping')";
				} else {
					insertOrUpdateQuery = "update epis_adj_crtn_curnt_pc_totals  set EMOLUMENTS='"
							+ EmolumentsTot
							+ "',CPFTOTAL='"
							+ cpfTotal
							+ "',CPFINTEREST='"
							+ cpfIntrst
							+ "',PENSIONTOTAL='"
							+ pensionTotal
							+ "',PENSIONINTEREST='"
							+ pensionIntrst
							+ "',PFTOTAL='"
							+ pfContri
							+ "',PFINTEREST='"
							+ pfContriIntrst
							+ "',CREATIONDATE= sysdate  where  pensionno='"
							+ empserialNO
							+ "' and   ADJOBYEAR='"
							+ adjObYear
							+ "'";

				}
			}

			log.info("-----saveCurrPctoals--insertOrUpdateQuery "
					+ insertOrUpdateQuery);
			result = st.executeUpdate(insertOrUpdateQuery);

			result = this.saveCurrDifftoals(empserialNO, adjObYear);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		return result;
	}

	// new method on 13-Feb-2012
	public int saveCurrDifftoals(String empserialNO, String adjObYear) {
		String insertOrUpdateQuery = "", deleteQuery = "", transID = "", sqlQuery = "";
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int result = 0;
		double EmolumentsTot = 0.0, cpfTotal = 0.0, cpfIntrst = 0.0, pensionTotal = 0.0, pensionIntrst = 0.0, pfContri = 0.0, pfContriIntrst = 0.0;
		sqlQuery = "select (a.emoluments - b.emoluments) as emoluments_diff,(a.cpftotal - b.cpftotal) as cpftotal_diff,(a.cpfinterest - b.cpfinterest) as cpfinterest_diff,(a.pensiontotal-b.pensiontotal) as pensiontotal_diff,(a.pensioninterest- b.pensioninterest) as pensioninterest_diff,(a.pftotal-b.pftotal) as pftotal_diff,(a.pfinterest-b.pfinterest) as pfinterest_diff from (select * from epis_adj_crtn_curnt_pc_totals where adjobyear='"
				+ adjObYear
				+ "') a,(select * from epis_adj_crtn_prv_pc_totals) b where a.pensionno = b.pensionno and a.adjobyear = b.adjobyear and a.pensionno='"
				+ empserialNO + "' ";
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs1 = st.executeQuery(sqlQuery);
			if (rs1.next()) {
				EmolumentsTot = Double.parseDouble(rs1
						.getString("emoluments_diff"));
				cpfTotal = Double.parseDouble(rs1.getString("cpftotal_diff"));
				cpfIntrst = Double.parseDouble(rs1
						.getString("cpfinterest_diff"));
				pensionTotal = Double.parseDouble(rs1
						.getString("pensiontotal_diff"));
				pensionIntrst = Double.parseDouble(rs1
						.getString("pensioninterest_diff"));
				pfContri = Double.parseDouble(rs1.getString("pftotal_diff"));
				pfContriIntrst = Double.parseDouble(rs1
						.getString("pfinterest_diff"));
			}
			transID = this.getTransidSequence(con);

			String selectADJCurrentPCTotals = "select count(*) as count from epis_adj_crtn_pc_totals_diff where pensionno='"
					+ empserialNO + "' and  ADJOBYEAR='" + adjObYear + "'";
			log.info("--saveCurrPctoals----selectADJTotalsDiff--"
					+ selectADJCurrentPCTotals);
			rs = st.executeQuery(selectADJCurrentPCTotals);
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					// trackingQuery="select * from epis_adj_crtn_pfid_tracking
					// where pensionno="+ empserialNO;
					insertOrUpdateQuery = "insert into epis_adj_crtn_pc_totals_diff(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL, PENSIONTOTAL,PFTOTAL ,CREATIONDATE) values('"
							+ empserialNO
							+ "','"
							+ transID
							+ "','"
							+ adjObYear
							+ "','" + EmolumentsTot + "','" + cpfTotal

							+ "','" + pensionTotal

							+ "','" + pfContri

							+ "', sysdate)";
				} else {
					insertOrUpdateQuery = "update epis_adj_crtn_pc_totals_diff  set EMOLUMENTS='"
							+ EmolumentsTot
							+ "',CPFTOTAL='"
							+ cpfTotal

							+ "',PENSIONTOTAL='"
							+ pensionTotal

							+ "',PFTOTAL='"
							+ pfContri

							+ "',CREATIONDATE=sysdate  where  pensionno='"
							+ empserialNO
							+ "' and   ADJOBYEAR='"
							+ adjObYear
							+ "'";

				}
			}
			log.info("-----saveCurrPctoals--insertOrUpdateQuery "
					+ insertOrUpdateQuery);
			result = st.executeUpdate(insertOrUpdateQuery);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs1);
			commonDB.closeConnection(con, st, rs);

		}
		return result;
	}

	public String getTransidSequence(Connection con) {
		String transIDSeqVal = "";
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "SELECT TRANSIDSEQ.NEXTVAL AS TRANSID FROM DUAL";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				transIDSeqVal = rs.getString("TRANSID");
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return transIDSeqVal;
	}

	// By Radha On 29-Feb-2012 For Making Prev Grnd Totals not inserted upto
	// Apporved ,Aftr Approved new record shuld be inserted
	// By Radha On 16-Feb-2012 Rename the Method & implementing Finalize r Not
	// Finalize Updation Cond
	// New Method
	public int saveprvadjcrtntotals(String empserialNO, String adjOBYear,String form2Status,
			double EmolumentsTot, double cpfTotal, double cpfIntrst,
			double PenContriTotal, double PensionIntrst, double PFTotal,
			double PFIntrst, double EmpSub, double EmpSubInterest,
			double adjEmpSubIntrst, double AAIContri, double AAIContriInterest,
			double adjAAiContriIntrst, double adjPensionContriInterest,
			double FSEmpSubInterest,double FSAAIContriInterest) throws Exception {
		int result = 0;
		Connection con = null;
		try {
			con = DBUtils.getConnection();
			result = this.saveprvadjcrtntotals(con, empserialNO, adjOBYear,form2Status,
					EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
					PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest,
					adjEmpSubIntrst, AAIContri, AAIContriInterest,
					adjAAiContriIntrst, adjPensionContriInterest,
					FSEmpSubInterest,FSAAIContriInterest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		return result;

	}
//By Radha on 27-Jun-2012 for BLOCKED,Released cases
	public int saveprvadjcrtntotals(Connection con, String empserialNO,
			String adjOBYear,String form2Status, double EmolumentsTot, double cpfTotal,
			double cpfIntrst, double PenContriTotal, double PensionIntrst,
			double PFTotal, double PFIntrst, double EmpSub,
			double EmpSubInterest, double adjEmpSubIntrst, double AAIContri,
			double AAIContriInterest, double adjAAiContriIntrst,
			double adjPensionContriInterest,double FSEmpSubInterest,double FSAAIContriInterest)
			throws InvalidDataException {
		String insertQuery = "", transID = "", chkStatus = "", chkPrevPcTots = "" ,chkChqApproverFlag = "false",remarks="",chkChqApproverStatus="";

		Statement st = null;
		ResultSet rs = null;
		int result = 0;
		try {

			st = con.createStatement();
			/*
			 * deleteQuery = "delete from epis_adj_crtn_prv_pc_totals where
			 * pensionno="+empSerialNo;
			 * log.info("----savepcadjcrtntotals---deleteQuery "+deleteQuery);
			 * st.executeUpdate(deleteQuery);
			 */
			chkPrevPcTots = chkAdjCrtnPrvPCTotals(con,empserialNO, adjOBYear,chkChqApproverFlag);
			chkStatus = this.getAdjCrtnApprovedStatus(con,empserialNO, adjOBYear);

			log.info("saveprvadjcrtntotals()--chkFlag---" + chkStatus);
			if ((chkStatus.equals(""))
					|| (chkStatus.equals("Initial,Approved")) || (chkStatus.equals("BLOCKED,Released"))) {
				transID = this.getTransidSequence(con);
			}
			if (chkStatus.equals("Initial,Approved")) {
				 remarks="After Approved";
				 chkChqApproverFlag="true";
			}
			insertQuery = "insert into epis_adj_crtn_prv_pc_totals(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB, EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST , ADJAAICONTRIINTRST ,ADJFINEMPSUBINTRST,ADJFINAAICONTRIINTRST,CREATIONDATE, REMARKS) values('"
					+ empserialNO
					+ "','"
					+ transID
					+ "','"
					+ adjOBYear
					+ "','"
					+ EmolumentsTot
					+ "','"
					+ cpfTotal
					+ "','"
					+ cpfIntrst
					+ "','"
					+ PenContriTotal
					+ "','"
					+ PensionIntrst
					+ "','"
					+ adjPensionContriInterest
					+ "','"
					+ PFTotal
					+ "','"
					+ PFIntrst
					+ "','"
					+ EmpSub
					+ "','"
					+ EmpSubInterest
					+ "','"
					+ adjEmpSubIntrst
					+ "','"
					+ AAIContri
					+ "','"
					+ AAIContriInterest
					+ "','"
					+ adjAAiContriIntrst
					+ "','"
					+ FSEmpSubInterest
					+ "','"
					+ FSAAIContriInterest
					+ "', sysdate,'"+remarks+"')";

			if (chkPrevPcTots.equals("NotExists")) {
				log
						.info("----savepcadjcrtntotals---insertQuery "
								+ insertQuery);
				result = st.executeUpdate(insertQuery);
			} else {
				if(form2Status.equals("Y")){
					chkChqApproverStatus = chkAdjCrtnPrvPCTotals(con,empserialNO, adjOBYear,chkChqApproverFlag);
					if (chkChqApproverStatus.equals("NotExists") &&  chkStatus.equals("Initial,Approved")) { 
						log
								.info("----savepcadjcrtntotals---insertQuery After Approved"
										+ insertQuery);
						result = st.executeUpdate(insertQuery);
					}
					} 
			}
			/*
			 * // saving in transactions table insertIntoTrans = "insert into
			 * EPIS_ADJ_CRTN_TRANSACTIONS(PENSIONNO,ADJOBYEAR
			 * ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST
			 * ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB,
			 * EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST ,
			 * ADJAAICONTRIINTRST
			 * ,CREATIONDATE,APPROVEDBY,DESIGNATION,APRVDSIGNNAME) values('" +
			 * empserialNO+ "','"+ transID + "','" + adjOBYear + "','" +
			 * EmolumentsTot + "','" + cpfTotal + "','" + cpfIntrst + "','" +
			 * PenContriTotal + "','"+ PensionIntrst + "','" +
			 * adjPensionContriInterest + "','" + PFTotal + "','" + PFIntrst +
			 * "','" + EmpSub + "','" + EmpSubInterest + "','" + adjEmpSubIntrst +
			 * "','" + AAIContri + "','" + AAIContriInterest + "','" +
			 * adjAAiContriIntrst+ "', sysdate)";
			 * 
			 */

		} catch (SQLException e) {
			throw new InvalidDataException(e.getMessage());
		} catch (Exception e) {
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(null, st, rs);

		}
		return result;
	}

	public ArrayList SumofSuppPCReportPensonalInfo1(Connection con,
			String pensionNo,String adjyear) {
		String sqlQuery = "";

		SuppPCBean cardInfo = null;
		ArrayList arrearslist = new ArrayList();

		Statement st = null;
		ResultSet rs = null;
			sqlQuery = "select  t.pensionno,t.verifiedby,t.adjobyear,t.adjprocessedyear,t.rem_month, p.employeename,to_char(p.dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_char(p.dateofjoining,'dd-Mon-yyyy') as dateofjoining,to_char(p.dateofseperation_date,'dd-Mon-yyyy') as dateofseperation_date,p.cpfacno,p.desegnation,p.fhname,p.wetheroption,p.employeeno,t.form4id,p.airportcode from epis_adj_crtn_pfid_trackingpc t,"
				+"employee_personal_info p where t.pensionno = p.pensionno and t.pensionno="+pensionNo+" and ADJOBYEAR='"+adjyear+"' and t.verifiedby='Initial,Approved'  order by adjobyear ";

		log.info(sqlQuery);
		try {

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				cardInfo = new SuppPCBean();
				double total = 0.0;

				if (rs.getString("pensionno") != null) {
					cardInfo.setPensionno(rs.getString("pensionno"));
				} 
				if (rs.getString("verifiedby") != null) {
					cardInfo.setVerifiedby(rs.getString("verifiedby"));
				}

				if (rs.getString("adjobyear") != null) {
					cardInfo.setAdjobyear(rs.getString("adjobyear"));
				}
				
				if (rs.getString("adjprocessedyear") != null) {
					cardInfo.setAdjprocessedyear(rs.getString("adjprocessedyear"));
				} 
				if (rs.getString("rem_month") != null) {
					cardInfo.setRem_month(rs.getString("rem_month"));
				}
				if (rs.getString("employeename") != null) {
					cardInfo.setEmployeename(rs.getString("employeename"));
				}
				if (rs.getString("dateofbirth") != null) {
					cardInfo.setDateofbirth(rs.getString("dateofbirth"));
				} else {
					cardInfo.setDateofbirth("---");
				} 
				if (rs.getString("airportcode") != null) {
					cardInfo.setAirportcode(rs.getString("airportcode"));
				} else {
					cardInfo.setAirportcode("---");
				} 
				if (rs.getString("dateofjoining") != null) {
					cardInfo.setDateofjoining(rs.getString("dateofjoining"));
				} else {
					cardInfo.setDateofjoining("---");
				}
				if (rs.getString("dateofseperation_date") != null) {
					cardInfo.setDateofseperation_date(rs.getString("dateofseperation_date"));
				} else {
					cardInfo.setDateofseperation_date("---");
				} 
				if (rs.getString("cpfacno") != null) {
					cardInfo.setCpfacno(rs.getString("cpfacno"));
				}
				if (rs.getString("desegnation") != null) {
					cardInfo.setDesegnation(rs.getString("desegnation"));
				}
				if (rs.getString("fhname") != null) {
					cardInfo.setFhname(rs.getString("fhname"));
				}
				if (rs.getString("wetheroption") != null) {
					cardInfo.setWetheroption(rs.getString("wetheroption"));
				}
				if (rs.getString("employeeno") != null) {
					cardInfo.setEmployeeno(rs.getString("employeeno"));
				}
				else{
					cardInfo.setEmployeeno("---");
				}
				if (rs.getString("form4id") != null) {
					cardInfo.setForm4id(rs.getString("form4id"));
					System.out.println("form4id========"+rs.getString("form4id"));
				}
				else{
					System.out.println("form4id========else==="+rs.getString("form4id"));
					cardInfo.setForm4id("---");
				}
				
				arrearslist.add(cardInfo);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return arrearslist;

	}
	public ArrayList SumofSuppPCReportPensonalInfo(Connection con,
			String pensionNo) {
		String sqlQuery = "";

		SuppPCBean cardInfo = null;
		ArrayList arrearslist = new ArrayList();

		Statement st = null;
		ResultSet rs = null;
			sqlQuery = "select  t.pensionno,t.verifiedby,t.adjobyear,t.adjprocessedyear,t.rem_month, p.employeename,to_char(p.dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_char(p.dateofjoining,'dd-Mon-yyyy') as dateofjoining,to_char(p.dateofseperation_date,'dd-Mon-yyyy') as dateofseperation_date,p.cpfacno,p.desegnation,p.fhname,p.wetheroption,p.employeeno,t.form4id,p.airportcode,p.uanno from epis_adj_crtn_pfid_trackingpc t,"
				+"employee_personal_info p where t.pensionno = p.pensionno and t.pensionno="+pensionNo+" and t.verifiedby='Initial,Approved'  order by adjobyear ";

		log.info(sqlQuery);
		try {

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				cardInfo = new SuppPCBean();
				double total = 0.0;

				if (rs.getString("pensionno") != null) {
					cardInfo.setPensionno(rs.getString("pensionno"));
				} 
				if (rs.getString("verifiedby") != null) {
					cardInfo.setVerifiedby(rs.getString("verifiedby"));
				}

				if (rs.getString("adjobyear") != null) {
					cardInfo.setAdjobyear(rs.getString("adjobyear"));
				}
				
				if (rs.getString("adjprocessedyear") != null) {
					cardInfo.setAdjprocessedyear(rs.getString("adjprocessedyear"));
				} 
				if (rs.getString("rem_month") != null) {
					cardInfo.setRem_month(rs.getString("rem_month"));
				}
				if (rs.getString("employeename") != null) {
					cardInfo.setEmployeename(rs.getString("employeename"));
				}
				if (rs.getString("dateofbirth") != null) {
					cardInfo.setDateofbirth(rs.getString("dateofbirth"));
				} else {
					cardInfo.setDateofbirth("---");
				} 
				if (rs.getString("airportcode") != null) {
					cardInfo.setAirportcode(rs.getString("airportcode"));
				} else {
					cardInfo.setAirportcode("---");
				} 
				if (rs.getString("dateofjoining") != null) {
					cardInfo.setDateofjoining(rs.getString("dateofjoining"));
				} else {
					cardInfo.setDateofjoining("---");
				}
				if (rs.getString("dateofseperation_date") != null) {
					cardInfo.setDateofseperation_date(rs.getString("dateofseperation_date"));
				} else {
					cardInfo.setDateofseperation_date("---");
				} 
				if (rs.getString("cpfacno") != null) {
					cardInfo.setCpfacno(rs.getString("cpfacno"));
				}
				if (rs.getString("desegnation") != null) {
					cardInfo.setDesegnation(rs.getString("desegnation"));
				}
				if (rs.getString("fhname") != null) {
					cardInfo.setFhname(rs.getString("fhname"));
				}
				if (rs.getString("wetheroption") != null) {
					cardInfo.setWetheroption(rs.getString("wetheroption"));
				}
				if (rs.getString("employeeno") != null) {
					cardInfo.setEmployeeno(rs.getString("employeeno"));
				}
				else{
					cardInfo.setEmployeeno("---");
				}
				if (rs.getString("form4id") != null) {
					cardInfo.setForm4id(rs.getString("form4id"));
					System.out.println("form4id========"+rs.getString("form4id"));
				}
				else{
					System.out.println("form4id========else==="+rs.getString("form4id"));
					cardInfo.setForm4id("---");
				}
				if (rs.getString("uanno") != null) {
					cardInfo.setUanno(rs.getString("uanno"));
				} else {
					cardInfo.setUanno("---");
				} 
				arrearslist.add(cardInfo);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return arrearslist;

	}
	public ArrayList SumofSuppPCReport(String pensionno) {
		log.info("AdjCrtnDAO::statmentpcwagesreport");
		Connection con = null;

		ArrayList empDataList = new ArrayList();
		SuppPCBean personalInfo = new SuppPCBean();
		//EmployeeCardReportInfo cardInfo = null;
		SuppPCBeanData cardInfo = null;
		ArrayList list = new ArrayList();

		ArrayList cardList = new ArrayList();
		try {
			con = commonDB.getConnection();
			empDataList = this.SumofSuppPCReportPensonalInfo(con,pensionno);

			for (int i = 0; i < empDataList.size(); i++) {
				cardInfo = new SuppPCBeanData();
				personalInfo = (SuppPCBean) empDataList.get(i);
				log
						.info("FincialReportDAO:::getStatementOfWagePension:Final Settlement Date"
								+ personalInfo.getPensionno()
								+ "Resttlement Date"
								+ personalInfo.getRem_month());
				if(personalInfo.getAdjobyear().equals("1995-2008")){
				list = this.SumofSuppPCReportData(con, personalInfo.getPensionno()
						,personalInfo.getRem_month(),personalInfo.getAdjobyear());
				}
				else{
				list = this.SumofSuppPCReportDataafter2008(con, personalInfo.getPensionno()
							,personalInfo.getRem_month(),personalInfo.getAdjobyear());
				}
				cardInfo.setPersonalInfo(personalInfo);
				cardInfo.setPensionCardList(list);
				cardList.add(cardInfo);

			}
			

		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, null, null);
		}

		return cardList;
	}
	public ArrayList SumofSuppPCReportData(Connection con,
			String pensionNo,String remMonth,String adjyear) {
		log.info("Pfids:::::::::"+pensionNo+"toDate::::::::::"+remMonth+"::adjyear=============="+adjyear);
		String sqlQuery = "";	
		String[] adjyears = adjyear.split("-");
		String adjyear1 = adjyears[0]; // 004
		String adjyear2 = adjyears[1];
		
		SuppPCBeanData cardInfo = null;
		ArrayList pensionList = new ArrayList();
		ArrayList arrearslist = new ArrayList();
	
		Statement st = null;
		ResultSet rs = null;

		sqlQuery = "select nvl((c.pensiontotal+c.pensioninterest),0) as newpc ,nvl((p.pensiontotal+p.pensioninterest),0) as oldpc,nvl((c.pensiontotal + c.pensioninterest) -(p.pensiontotal + p.pensioninterest),0) as diffpc,(select months_between('"+remMonth+"', '15-Apr-2008') from dual) as intmonths,"
		+"nvl(round((c.pensiontotal + c.pensioninterest -(p.pensiontotal + p.pensioninterest)) * 12 / 100 *(select months_between('"+remMonth+"', '15-Apr-2008') from dual) / 12,0),0)as interstpc,round((nvl(c.pensiontotal,0)-nvl(p.pensiontotal,0))*100/8.33,0) as emoluments,nvl(c.pensiontotal-p.pensiontotal,0) as diffrem from epis_adj_crtn_curnt_pc_topc c, epis_adj_crtn_prv_pc_totalspc p where c.pensionno = p.pensionno and c.adjobyear=p.adjobyear and c.adjobyear='1995-2008' and p.pensionno = "+pensionNo+" ";

		log.info(sqlQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				cardInfo = new SuppPCBeanData();
				if (rs.getString("oldpc") != null) {
					cardInfo.setOldpc(rs.getString("oldpc"));
				} 
				else{
					cardInfo.setOldpc("0");
				}
				if (rs.getString("diffrem") != null) {
					cardInfo.setDiffrem(rs.getString("diffrem"));
				} 
				else{
					cardInfo.setDiffrem("0");
				}
				if (rs.getString("newpc") != null) {
					cardInfo.setNewpc(rs.getString("newpc"));
				}
				else{
					cardInfo.setNewpc("0");
				}
				if (rs.getString("diffpc") != null) {
					cardInfo.setDiffpc(rs.getString("diffpc"));
				}
				else{
					cardInfo.setDiffpc("0");
				}
				if (rs.getString("intmonths") != null) {
					cardInfo.setIntmonths(rs.getString("intmonths"));
				} 
				if (rs.getString("interstpc") != null) {
					cardInfo.setInterstpc(rs.getString("interstpc"));
				}
				else{
					cardInfo.setInterstpc("0");
				}
				if (rs.getString("emoluments") != null) {
					cardInfo.setEmoluments(rs.getString("emoluments"));
				}
				else{
					cardInfo.setEmoluments("0");
				}
				arrearslist.add(cardInfo);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return arrearslist;

	}

	public ArrayList SumofSuppPCReportDataafter2008(Connection con,
			String pensionNo,String remMonth,String adjyear) {
		log.info("Pfids:::::::::"+pensionNo+"toDate::::::::::"+remMonth+"::adjyear=============="+adjyear);
		String  sqlQuery = "",fromYear="",toYear="",diffpc="",intrestpc="";
		double totdiffpc=0.0,totinterserpc=0.0;
		String[] adjyears = adjyear.split("-");
		if(adjyear.equals("2012-2013")){
			fromYear = "01-Apr-"+adjyears[0];
			toYear = "30-Apr-"+adjyears[1];
		}
		else if(Integer.parseInt(adjyear.substring(0, 4))>=2013){
		fromYear = "01-May-"+adjyears[0];
		toYear = "30-Apr-"+adjyears[1];
		}
		else{
		fromYear = "01-Apr-"+adjyears[0];
		toYear = "31-Mar-"+adjyears[1]; 
		}
		String adjyear1 = adjyears[0]; // 004
		String adjyear2 = adjyears[1];
		SuppPCBeanData cardInfo = null;
		ArrayList pensionList = new ArrayList();
		ArrayList arrearslist = new ArrayList();
	
		Statement st = null;
		ResultSet rs = null;
		if(Integer.parseInt(adjyear.substring(0, 4))>=2013){
		sqlQuery = "select c.monthyear,v.finyear,to_char(add_months(c.monthyear,0),'Mon-yyyy') as ecrmonth,(case when v.empflag='N' then 0 else nvl(to_number(v.emoluments),0) end) as oldemol,nvl(c.emoluments,0) as newemol,(c.emoluments - (case when v.empflag='N' then 0 else nvl(to_number(v.emoluments),0) end)) as diffemol,(case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end) as oldpc,nvl(c.pensioncontri,0) as newpc,nvl((c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end)),0) as diffpc,"
			       +"(CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno )ELSE v.ADDITIONALCONTRI END) AS oldaddpc, NVL(c.ADDITIONALCONTRI,0) AS newaddpc,NVL((c.ADDITIONALCONTRI - NVL((CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno)ELSE v.ADDITIONALCONTRI END), 0)),0) AS diffaddpc,(select floor(months_between('"+remMonth+"',(case when c.monthyear>='01-May-2013' then add_months(c.monthyear, 1) else add_months(c.monthyear, 0) end))) from dual)as intmonths,"
		           +"nvl(round(( c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end))*12/100*((select floor(months_between('"+remMonth+"',(case when c.monthyear>='01-May-2013' then add_months(c.monthyear, 1) else add_months(c.monthyear, 0) end))) from dual))/12,0),0) as interstpc,NVL(ROUND(( c.ADDITIONALCONTRI - NVL(  (CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno )ELSE v.ADDITIONALCONTRI END), 0))*12/100*((SELECT floor(months_between('15-Dec-2016',add_months(c.monthyear, 0))) FROM dual))/12,0),0) AS interstaddpc,round((nvl(c.pensioncontri,0)-(case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end))*100/8.33,0) as emoluments1,(select count(c.pensionno) from epis_adj_crtnpc c, employee_pension_validate v where c.pensionno = v.pensionno(+) and c.monthyear = v.monthyear(+) and c.pensionno = "+pensionNo+" and c.monthyear between '"+fromYear+"' and '"+toYear+"' and c.datamodifiedflag = 'Y' and c.empflag = v.empflag(+) group by c.pensionno) as count from epis_adj_crtnpc c, employee_pension_validate v where c.pensionno = v.pensionno(+) and c.monthyear = v.monthyear(+) and c.pensionno = "+pensionNo+" and c.monthyear between '"+fromYear+"' and '"+toYear+"' and c.datamodifiedflag = 'Y' and c.empflag = v.empflag(+) order by monthyear";
		}
		else{
		sqlQuery = "select c.monthyear,v.finyear,to_char(add_months(c.monthyear,1),'Mon-yyyy') as ecrmonth,(case when v.empflag='N' then 0 else nvl(to_number(v.emoluments),0) end) as oldemol,nvl(c.emoluments,0) as newemol,(c.emoluments - (case when v.empflag='N' then 0 else nvl(to_number(v.emoluments),0) end)) as diffemol,(case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end) as oldpc,nvl(c.pensioncontri,0) as newpc,nvl((c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end)),0) as diffpc,"
		       +"(CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno )ELSE v.ADDITIONALCONTRI END) AS oldaddpc, NVL(c.ADDITIONALCONTRI,0) AS newaddpc,NVL((c.ADDITIONALCONTRI - NVL((CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno)ELSE v.ADDITIONALCONTRI END), 0)),0) AS diffaddpc,(select floor(months_between('"+remMonth+"',add_months(c.monthyear, 1))) from dual)as intmonths,"
	           +"nvl(round(( c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end))*12/100*((select floor(months_between('"+remMonth+"',add_months(c.monthyear, 1))) from dual))/12,0),0) as interstpc,NVL(ROUND(( c.ADDITIONALCONTRI - NVL(  (CASE WHEN v.monthyear='01-May-2015' THEN (SELECT SUM(ADDITIONALCONTRI) FROM EMPLOYEE_PENSION_VALIDATE t WHERE t.monthyear BETWEEN '01-Oct-2014' AND '30-May-2015' AND t.pensionno=v.pensionno )ELSE v.ADDITIONALCONTRI END), 0))*12/100*((SELECT floor(months_between('15-Dec-2016',add_months(c.monthyear, 1))) FROM dual))/12,0),0) AS interstaddpc,round((nvl(c.pensioncontri,0)-(case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end))*100/8.33,0) as emoluments1,(select count(c.pensionno) from epis_adj_crtnpc c, employee_pension_validate v where c.pensionno = v.pensionno(+) and c.monthyear = v.monthyear(+) and c.pensionno = "+pensionNo+" and c.monthyear between '"+fromYear+"' and '"+toYear+"' and c.datamodifiedflag = 'Y' and c.empflag = v.empflag(+) group by c.pensionno) as count from epis_adj_crtnpc c, employee_pension_validate v where c.pensionno = v.pensionno(+) and c.monthyear = v.monthyear(+) and c.pensionno = "+pensionNo+" and c.monthyear between '"+fromYear+"' and '"+toYear+"' and c.datamodifiedflag = 'Y' and c.empflag = v.empflag(+) order by monthyear";
		}
		
		log.info(sqlQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				cardInfo = new SuppPCBeanData();
				
				if (rs.getString("monthyear") != null) {
					cardInfo.setMonthyear(CommonUtil.converDBToAppFormat(rs.getDate("monthyear")));
				}
				else{
					cardInfo.setMonthyear("---");
				}
				if (rs.getString("finyear") != null) {
					cardInfo.setFinyear(rs.getString("finyear"));
				}
				else{
					cardInfo.setFinyear("---");
				}
				if (rs.getString("ecrmonth") != null) {
					cardInfo.setEcrmonth(rs.getString("ecrmonth"));
				}
				else{
					cardInfo.setEcrmonth("---");
				}
				if (rs.getString("oldemol") != null) {
					cardInfo.setOldemol(rs.getString("oldemol"));
				}
				else{
					cardInfo.setOldemol("0");
				}
				if (rs.getString("newemol") != null) {
					cardInfo.setNewemol(rs.getString("newemol"));
				}
				else{
					cardInfo.setNewemol("0");
				}
				if (rs.getString("diffemol") != null) {
					cardInfo.setDiffemol(rs.getString("diffemol"));
				}
				else{
					cardInfo.setDiffemol("0");
				}
				if (rs.getString("oldpc") != null) {
					cardInfo.setOldpc1(rs.getString("oldpc"));
				} 
				else{
					cardInfo.setOldpc1("0");
				}
				if (rs.getString("newpc") != null) {
					cardInfo.setNewpc1(rs.getString("newpc"));
				}
				else{
					cardInfo.setNewpc1("0");
				}
				if (rs.getString("diffpc") != null) {
					cardInfo.setDiffpc1(rs.getString("diffpc"));
				}
				else{
					cardInfo.setDiffpc1("0");
				}
				diffpc=rs.getString("diffpc");
				
				if (rs.getString("oldaddpc") != null) {
					cardInfo.setOldaddpc(rs.getString("oldaddpc"));
				}
				else{
					cardInfo.setOldaddpc("0");
				}
				if (rs.getString("newaddpc") != null) {
					cardInfo.setNewaddpc(rs.getString("newaddpc"));
				}
				else{
					cardInfo.setNewaddpc("0");
				}
				if (rs.getString("diffaddpc") != null) {
					cardInfo.setDiffaddpc(rs.getString("diffaddpc"));
				}
				else{
					cardInfo.setDiffaddpc("0");
				}
				if (rs.getString("interstaddpc") != null) {
					cardInfo.setInterstaddpc(rs.getString("interstaddpc"));
				}
				else{
					cardInfo.setInterstaddpc("0");
				}
				
				if (rs.getString("intmonths") != null) {
					cardInfo.setIntmonths1(rs.getString("intmonths"));
				} 
				else{
					cardInfo.setIntmonths1("0");
				}
				if (rs.getString("interstpc") != null) {
					cardInfo.setInterstpc1(rs.getString("interstpc"));
				}
				else{
					cardInfo.setInterstpc1("0");
				}
				if (rs.getString("count") != null) {
					cardInfo.setCount(rs.getString("count"));
				}
				intrestpc=rs.getString("interstpc");
				if (rs.getString("emoluments1") != null) {
					if(rs.getString("diffpc").equals("541")){
					cardInfo.setEmoluments1("6500");
					}
					else{
						cardInfo.setEmoluments1(rs.getString("emoluments1"));	
					}
				}
				else{
					cardInfo.setEmoluments1("0");
				}
				//totdiffpc=totdiffpc+Double.parseDouble(diffpc);
				////totinterserpc=totinterserpc+Double.parseDouble(intrestpc);
				//cardInfo.setTotdiffpc(totdiffpc);
				//cardInfo.setTotinterserpc(totinterserpc);
				
				arrearslist.add(cardInfo);
			}
			
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return arrearslist;

	}

	// new method on 18-Feb-2012 By Radha for tracking of notfinalized transid
	public String getAdjCrtnFinalizedFlag(String empserialNO, String reportYear) {
		log.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag Entering method");
		Connection con = null;

		String finalizedFlag = "";

		try {

			con = DBUtils.getConnection();
			finalizedFlag = this.getAdjCrtnFinalizedFlag(con, empserialNO,
					reportYear);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		log.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag leaving method");
		return finalizedFlag;
	}

	public String getAdjCrtnFinalizedFlag(Connection con, String empserialNO,
			String reportYear) {
		log
				.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag Entering method With Connection");

		Statement st = null;
		ResultSet rs = null;

		String selectSQL = "", flag = "", fromYear = "", toYear = "", finalizedFlag = "";
		String years[] = null;
		years = reportYear.split("-");
		if(reportYear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else{
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		}
		selectSQL = "select 'X' as flag   from epis_adj_crtn_emol_log_temp where pensionno= "
				+ empserialNO
				+ " and monthyear between '"
				+ fromYear
				+ "' and '" + toYear + "'";

		try {
			log.info("selectSQL==" + selectSQL);

			st = con.createStatement();
			rs = st.executeQuery(selectSQL);
			if (rs.next()) {
				flag = rs.getString("flag");
			}
			if (flag.equals("X")) {
				finalizedFlag = "NotFinalize";
			} else {
				finalizedFlag = "Finalized";
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		log
				.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag leaving method With Connection");
		return finalizedFlag;
	}

	// new method on 29-Feb-2012 By Radha for chking record is approved r not
	// finally
	public String getAdjCrtnApprovedStatus(Connection con ,String empserialNO, String reportYear) {
		log.info("AdjCrtnDAO: getAdjCrtnApprovedStatus Entering method");		 
		Statement st = null;
		ResultSet rs = null;
		String selectSQL = "", APPROVEDTYPE = "";
		int i = 0;
		selectSQL = "select APPROVEDTYPE as APPROVEDTYPE  from epis_adj_crtn_transactions where pensionno= "
				+ empserialNO
				+ " and adjobyear= '"
				+ reportYear
				+ "'  order by transid";

		try {
			log.info("selectSQL==" + selectSQL);		 
			st = con.createStatement();
			rs = st.executeQuery(selectSQL);
			while (rs.next()) {
				if (rs.getString("APPROVEDTYPE") != null) {
					i++;
					if (i == 1) {
						APPROVEDTYPE = rs.getString("APPROVEDTYPE");
					} else {
						APPROVEDTYPE = APPROVEDTYPE + ","
								+ rs.getString("APPROVEDTYPE");
					}
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		log.info("AdjCrtnDAO: getAdjCrtnApprovedStatus leaving method");
		return APPROVEDTYPE;
	}

	// new method on 13-Feb-2012
	public int savePrePctoalsTemp(String empserialNO, String adjObYear,
			ArrayList prePcTotals) {
		String insertQuery = "", deleteQuery = "", transID = "", trackingQuery = "";
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int result = 0;
		double EmolumentsTot = 0.0, cpfTotal = 0.0, cpfIntrst = 0.0, pensionTotal = 0.0, pensionIntrst = 0.0, pfContri = 0.0, pfContriIntrst = 0.0;
		EmolumentsTot = ((Double) prePcTotals.get(0)).doubleValue();
		cpfTotal = ((Double) prePcTotals.get(1)).doubleValue();
		cpfIntrst = ((Double) prePcTotals.get(2)).doubleValue();
		pensionTotal = ((Double) prePcTotals.get(3)).doubleValue();
		pensionIntrst = ((Double) prePcTotals.get(4)).doubleValue();
		pfContri = ((Double) prePcTotals.get(5)).doubleValue();
		pfContriIntrst = ((Double) prePcTotals.get(6)).doubleValue();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();

			transID = this.getTransidSequence(con);
			String selectADJPrvPCTotals = "select count(*) as count from epis_adjcrtn_prvpctotals_temp where pensionno='"
					+ empserialNO + "' and  ADJOBYEAR='" + adjObYear + "'";
			log.info("--savePrvPctoals----selectADJTotalsDiff--"
					+ selectADJPrvPCTotals);
			rs1 = st.executeQuery(selectADJPrvPCTotals);
			while (rs1.next()) {
				int count = rs1.getInt(1);
				if (count == 0) {
					trackingQuery = "select * from epis_adj_crtn_pfid_tracking where pensionno="
							+ empserialNO;
					insertQuery = "insert into epis_adjcrtn_prvpctotals_temp(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST , PFTOTAL,PFINTEREST ,CREATIONDATE,remarks) values('"
							+ empserialNO
							+ "','"
							+ transID
							+ "','"
							+ adjObYear
							+ "','"
							+ EmolumentsTot
							+ "','"
							+ cpfTotal
							+ "','"
							+ cpfIntrst
							+ "','"
							+ pensionTotal
							+ "','"
							+ pensionIntrst
							+ "','"
							+ pfContri
							+ "','"
							+ pfContriIntrst
							+ "', sysdate,'Through Mapping Screen')";
				} else {
					insertQuery = "update epis_adjcrtn_prvpctotals_temp  set EMOLUMENTS='"
							+ EmolumentsTot
							+ "',CPFTOTAL='"
							+ cpfTotal
							+ "',CPFINTEREST='"
							+ cpfIntrst
							+ "',PENSIONTOTAL='"
							+ pensionTotal
							+ "',PENSIONINTEREST='"
							+ pensionIntrst
							+ "',PFTOTAL='"
							+ pfContri
							+ "',PFINTEREST='"
							+ pfContriIntrst
							+ "',CREATIONDATE= sysdate  where  pensionno='"
							+ empserialNO
							+ "' and   ADJOBYEAR='"
							+ adjObYear
							+ "'";

				}
			}
			log.info("----savePrePctoalsTemp---insertQuery " + insertQuery);
			result = st.executeUpdate(insertQuery);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		return result;
	}

	// By Radha on 08-Feb-2012 for making cond on accountType
	public ArrayList searchAdjctrn(String userRegion, String userStation,
			String profileType, String accessCode, String accountType,
			String employeeNo,String  adjOBYear,String approvedStatus) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String selectQuery = "", verifiedBy = "",approvedStage="",form2StatusDesc="";
		double pcprincipal=0.00,pcinterest=0.00,pensionTot=0.00;
		ArrayList al = new ArrayList();
		EmpMasterBean empBean = null;
		DecimalFormat df = new DecimalFormat("#########0");
		selectQuery = this.buildSearchQueryForAdjCrtn(userRegion, userStation,
				profileType, accessCode, accountType, employeeNo,adjOBYear,approvedStatus);
		log.info("searchAdjctrn()==========" + selectQuery);
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
				if (rs.getString("ADJOBYEAR") != null) {
					empBean.setReportYear(rs.getString("ADJOBYEAR")) ;
				} else{
					empBean.setReportYear("");
				}
				if (rs.getString("PCPRINCIPAL") != null) {
					pcprincipal = rs.getDouble("PCPRINCIPAL") ;
				}  
				if (rs.getString("PCINTEREST") != null) {
					pcinterest =  rs.getDouble("PCINTEREST")  ;
				} 
				
				if(empBean.getReportYear().equals("1995-2008")){
					pensionTot = pcprincipal + pcinterest;
				}else{
					pensionTot = pcprincipal ;
				}
				empBean.setPensionTot(String.valueOf(df.format(pensionTot)));
				
				if (rs.getString("EMPSUBTOT") != null) {
					empBean.setEmpSubTot(rs.getString("EMPSUBTOT")) ;
				} else{
					empBean.setEmpSubTot("");
				}
				if (rs.getString("AAICONTRITOT") != null) {
					empBean.setAaiContriTot(rs.getString("AAICONTRITOT")) ;
				} else{
					empBean.setAaiContriTot("");
				}
				if (rs.getString("APPROVEDTYPE") != null) {
					approvedStage = rs.getString("APPROVEDTYPE");
					if(approvedStage.equals("Initial,Approved")){
						approvedStage="Approved";
						empBean.setApprovedStatus(approvedStage);  
					}else{
						empBean.setApprovedStatus(approvedStage); 
					} 
				} else{
					empBean.setApprovedStatus("");
				}
				if (rs.getString("APPROVERNAME") != null) {
					empBean.setApproverName(rs.getString("APPROVERNAME")) ;
				} else{
					empBean.setApproverName("");
				}
				if (rs.getString("FORM2STATUS") != null) {
					empBean.setForm2Status(rs.getString("FORM2STATUS")) ;
				} else{
					empBean.setForm2Status("N");
				}
				
				if(empBean.getForm2Status().equals("Y")){
					form2StatusDesc ="Submitted";
				}else if(empBean.getForm2Status().equals("B")){
					form2StatusDesc ="Proccessing";
				}else if(empBean.getForm2Status().equals("M")){
					form2StatusDesc ="N/A";
				}else{
					form2StatusDesc ="Approved,But Form 2 not generated";
				}
				empBean.setForm2StatusDesc(form2StatusDesc);
				
				if (rs.getString("FROZEN") != null) {
					empBean.setFrozen(rs.getString("FROZEN")) ;
				} else{
					empBean.setFrozen("N");
				}
				if (rs.getString("JVNO") != null) {
					empBean.setJvNo(rs.getString("JVNO")) ;
				} else{
					empBean.setJvNo("");
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
     //By Radha on 20-MAr-2012 to implementing the CHQApprover
	//By Radha on 03-MAr-2012 to elimainate blocked Records
	// By Radha on 16-Feb-2012 for getting employee date
	// By Radha on 08-Feb-2012 for making cond on accountType
	public String buildSearchQueryForAdjCrtn(String userRegion,
			String userStation, String profileType, String accessCode,
			String accountType, String employeeNo,String adjOBYear,String approvedStatus) {
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		String dynamicQuery = "", orderBy = "", sqlQuery = "", verifiedby = "", profilecondition = "";

		log.info("==userRegion==" + userRegion + "==userStation===="
				+ userStation + "==profileType==" + profileType
				+ "==accessCode==" + accessCode + "=employeeNo==" + employeeNo);

		 
		if(accessCode.equals("PE040202")){
			verifiedby="'Initial','Reject'";
			profilecondition="AND TRACKING.VERIFIEDBY in   ("+verifiedby+")";
		} 
	 	if(accessCode.equals("PE040204")){
			verifiedby="'Initial,Approved'";
			profilecondition="AND TRACKING.VERIFIEDBY  in ("+verifiedby+")  AND TRANS.Approvedtype ='Approved'";
		} else if(accessCode.equals("PE04020602")){
			verifiedby="'Initial,Approved'";
			profilecondition="AND TRACKING.VERIFIEDBY  in ("+verifiedby+")  AND TRANS.Approvedtype ='CHQApproved'";
		} 
	 	 
	 	if(accessCode.equals("PE040202") || accessCode.equals("PE040204") || accessCode.equals("PE04020602")){
		 	sqlQuery ="  SELECT EPI.PENSIONNO AS PENSIONNO,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.CPFACNO AS CPFACNO ,EPI.REGION AS REGION,EPI.AIRPORTCODE AS AIRPORTCODE,EPI.DESEGNATION AS DESEGNATION,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.DATEOFJOINING AS DATEOFJOINING, TRANS.ADJOBYEAR AS ADJOBYEAR, NVL(TRANS.PCPRINCIPAL, 0.0) AS PCPRINCIPAL,  NVL(TRANS.PCINTEREST, 0.0)AS PCINTEREST,  (NVL(TRANS.EMPSUBSCRIPTION, 0.0) + NVL(TRANS.EMPSUBINT, 0.0)) AS EMPSUBTOT,"
				+ " (NVL(AAICONTRI, 0.0) + NVL(TRANS.AAICONTRIINT, 0.0)) AS AAICONTRITOT,TRACKING.VERIFIEDBY AS APPROVEDTYPE, TRACKING.FORM2ID AS FORM2ID,TRACKING.FORM2STATUS AS FORM2STATUS ,TRANS.APRVDSIGNNAME	AS APPROVERNAME ,TRACKING.FROZEN AS FROZEN,TRACKING.JVNO AS JVNO  FROM EPIS_ADJ_CRTN_TRANSACTIONS TRANS, EPIS_ADJ_CRTN_PFID_TRACKING  TRACKING,EMPLOYEE_PERSONAL_INFO EPI"
				+" WHERE EPI.EMPFLAG ='Y' AND TRACKING.BLOCKED='N' AND TRANS.PENSIONNO = TRACKING.PENSIONNO AND TRACKING.PENSIONNO = EPI.PENSIONNO    AND TRANS.ADJOBYEAR = TRACKING.ADJOBYEAR "+profilecondition;
		 	}else if(accessCode.equals("PE04020601")){
		 		sqlQuery ="  SELECT   PENSIONNO,  EMPLOYEENAME, CPFACNO, REGION, AIRPORTCODE, DESEGNATION,  DATEOFBIRTH, DATEOFJOINING,  ADJOBYEAR, NVL(PCPRINCIPAL, 0.0) AS PCPRINCIPAL ,NVL(PCINTEREST, 0.0) AS PCINTEREST ,NVL(EMPSUB, 0.0) AS EMPSUBTOT,"
					+ " NVL(AAICONTRI, 0.0) AS AAICONTRITOT,   APPROVEDTYPE,   FORM2STATUS , APPROVERNAME , FROZEN,JVNO FROM v_epis_adj_chq_search  ";
			 	
		 	}
	 	
		if (!(profileType.equals("C") || profileType.equals("S") || profileType
				.equals("A"))) {

			if (profileType.equals("R")) {
				if (!userRegion.equals("CHQIAD")) {
					whereClause
							.append(" LOWER(EPI.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
									+ userRegion.toLowerCase().trim()+ "')");
					whereClause.append(" AND ");
				} else {
					 //For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 07-Jun-2012
					if (!userStation.equals("")) {
						whereClause
						.append(" LOWER(EPI.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
								+ userRegion.toLowerCase().trim()+ "' AND ACCOUNTTYPE='RAU')");
					 
						whereClause.append(" AND ");
					}
				}
			} else {
				if (!userStation.equals("")) {
					whereClause.append(" LOWER(EPI.AIRPORTCODE) like'%"
							+ userStation.toLowerCase().trim() + "%'");
					whereClause.append(" AND ");
				}
			}

			if (!userRegion.equals("")) {
				whereClause.append(" LOWER(EPI.REGION) like'%"
						+ userRegion.toLowerCase().trim() + "%'");
				whereClause.append(" AND ");
			}
			if (!employeeNo.equals("")) {
				whereClause.append(" EPI.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			}
			if (!adjOBYear.equals("")) {
				whereClause.append(" TRANS.ADJOBYEAR ='"+ adjOBYear+"'");        
				whereClause.append(" AND ");      
			}
			if (!approvedStatus.equals("")) {
				whereClause.append(" TRANS.APPROVEDTYPE ='"+ approvedStatus+"'");         
				whereClause.append(" AND ");      
			} 
			query.append(sqlQuery);

			if (userStation.equals("") && userRegion.equals("")
					&& employeeNo.equals("") && adjOBYear.equals("") && approvedStatus.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
		} else {
			if(accessCode.equals("PE04020601")){
				if (!employeeNo.equals("")) {
					whereClause.append(" PENSIONNO =" + employeeNo);
					whereClause.append(" AND ");
				}
				if (!adjOBYear.equals("")) {
					whereClause.append(" ADJOBYEAR ='"+ adjOBYear+"'");         
					whereClause.append(" AND ");      
				}
			 
				query.append(sqlQuery);
				if (employeeNo.equals("") && adjOBYear.equals("")) {
				} else {
					query.append(" WHERE ");
					query.append(this.sTokenFormat(whereClause));
				}
			}else{
			if (!employeeNo.equals("")) {
				whereClause.append(" EPI.PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			}
			if (!adjOBYear.equals("")) {
				whereClause.append(" TRANS.ADJOBYEAR ='"+ adjOBYear+"'");         
				whereClause.append(" AND ");      
			}
			if (!approvedStatus.equals("")) {
				whereClause.append(" TRANS.APPROVEDTYPE ='"+ approvedStatus+"'");           
				whereClause.append(" AND ");      
			} 
			query.append(sqlQuery);
			if (employeeNo.equals("") && adjOBYear.equals("") && approvedStatus.equals("")) {
			} else {
				query.append(" AND ");
				query.append(this.sTokenFormat(whereClause));
			}
		}
			
		}
		if(accessCode.equals("PE04020601")){
		orderBy = "ORDER BY PENSIONNO";
		}else if(accessCode.equals("PE040204")){
			orderBy = "ORDER BY TRANS.APPROVEDDATE DESC ";
		}else{
			orderBy = "ORDER BY TRACKING.PENSIONNO";
		}
		query.append(orderBy);
		dynamicQuery = query.toString();

		return dynamicQuery;

	}

	// By Radha On 16-Feb-2012 for Unique Id for Storing Adj Emoluments in Temp
	// Table
	public void insertAdjEmolumenstLogInTemp(ArrayList adjEmolList,
			String pensionno, String reportYear, String notFianalizetransID) {
		int count = 0, batchId = 0, result = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String chkQuery = "", insertQuery = "", updateQry = "", transSqlQuery = "", ChkFlag = "", transId = "";
		EmolumentslogBean bean = new EmolumentslogBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("AdjCrtnDAO::insertAdjEmolumenstLogInTemp()====Entry");
			log.info("====notFianalizetransID== " + notFianalizetransID);
			// batchId = this.getBatchIdForAdjTemp(con);
			if (notFianalizetransID.equals("")) {
				transId = this.getPCTotalsTransId(con, pensionno, reportYear);
			} else {
				transId = notFianalizetransID;
			}
			for (int i = 0; i < adjEmolList.size(); i++) {
				bean = (EmolumentslogBean) adjEmolList.get(i);

				chkQuery = "select 'X' as flag from epis_adj_crtn_emol_log_temp where pensionno="
						+ pensionno
						+ " and monthyear='"
						+ bean.getMonthYear()
						+ "' and transid='"
						+ transId
						+ "' and originalrecord='N'";
				log.info("====chkQuery== " + chkQuery);
				rs = st.executeQuery(chkQuery);
				if (rs.next()) {
					ChkFlag = rs.getString("flag");
				}
				insertQuery = "insert into  epis_adj_crtn_emol_log_temp "
						+ " (pensionno,  cpfacno ,   monthyear,  oldemoluments , newemoluments ,  oldemppfstatuary,newemppfstatuary,  oldempvpf ,  newempvpf , oldprinciple ,  newprinciple , oldinterest ,  newinterest ,  oldpensioncontri  , newensioncontri , updateddate  , remarks  , region	 , username,  computername,originalrecord,transid) values('"
						+ bean.getPensionNo() + "','" + bean.getCpfAcno()
						+ "','" + bean.getMonthYear() + "','"
						+ bean.getOldEmoluments() + "','"
						+ bean.getNewEmoluments() + "','"
						+ bean.getOldEmppfstatury() + "','"
						+ bean.getNewEmppfstatury() + "','"
						+ bean.getOldEmpvpf() + "','" + bean.getNewEmpvpf()
						+ "','" + bean.getOldPrincipal() + "','"
						+ bean.getNewPrincipal() + "','"
						+ bean.getOldInterest() + "','" + bean.getNewInterest()
						+ "','" + bean.getOldPensioncontri() + "','"
						+ bean.getNewPensioncontri() + "', sysdate,'"
						+ bean.getRemarks() + "','" + bean.getRegion() + "','"
						+ bean.getUserName() + "','" + bean.getComputerName()
						+ "', '" + bean.getOriginalRecord() + "'," + transId
						+ ")";

				updateQry = "update epis_adj_crtn_emol_log_temp set   oldemoluments='"
						+ bean.getOldEmoluments()
						+ "',newemoluments='"
						+ bean.getNewEmoluments()
						+ "',oldemppfstatuary='"
						+ bean.getOldEmppfstatury()
						+ "',newemppfstatuary='"
						+ bean.getNewEmppfstatury()
						+ "',oldempvpf='"
						+ bean.getOldEmpvpf()
						+ "',newempvpf='"
						+ bean.getNewEmpvpf()
						+ "',oldprinciple='"
						+ bean.getOldPrincipal()
						+ "',newprinciple='"
						+ bean.getNewPrincipal()
						+ "',oldinterest='"
						+ bean.getOldInterest()
						+ "',newinterest='"
						+ bean.getNewInterest()
						+ "',OLDPENSIONCONTRI='"
						+ bean.getOldPensioncontri()
						+ "',NEWENSIONCONTRI='"
						+ bean.getNewPensioncontri()
						+ "',UPDATEDDATE= sysdate"
						+ " ,username='"
						+ bean.getUserName()
						+ "',computername='"
						+ bean.getComputerName()
						+ "' where pensionno="
						+ bean.getPensionNo()
						+ " and   monthyear ='"
						+ bean.getMonthYear()
						+ "' and transid='"
						+ transId
						+ "' and originalrecord='N'";

				log.info("====ChkFlag== " + ChkFlag);
				if (ChkFlag.equals("X")) {
					log.info("====updateQry== " + updateQry);
					result = st.executeUpdate(updateQry);
				} else {
					log.info("====insertQuery== " + insertQuery);
					result = st.executeUpdate(insertQuery);
				}

				log.info("====result==============" + result);
				transSqlQuery = "update epis_adj_crtn  set DataModifiedFlag='Y' where  pensionno ="
						+ bean.getPensionNo()
						+ " and empflag='Y' and monthyear='"
						+ bean.getMonthYear() + "'";
				log
						.info("====insertAdjEmolumenstLog========transSqlQuery======"
								+ transSqlQuery);
				st.executeUpdate(transSqlQuery);

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

	}

	// on 16-Feb-2012 By Radha Inserting Transid For all Years
	public String getPCTotalsTransId(Connection con, String pensionno,
			String reportYear) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", transId = "";

		try {
			st = con.createStatement();
			sqlQuery = "select max(transid) as transid from  epis_adj_crtn_prv_pc_totals where pensionno="
					+ pensionno + " and adjobyear='" + reportYear + "'";
			log.info("----sqlQuery---------" + sqlQuery);
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				if(rs.getString("transid")!=null){
				transId = rs.getString("transid");
				} 
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return transId;
	}

	// on 16-Feb-2012 By Radha Inserting Transid For all Years
	public String insertAdjEmolumenstLog(String pensionno, String reportYear,
			String notFianalizetransID) throws InvalidDataException {

		Connection con = null;

		String transId = "";

		try {
			con = DBUtils.getConnection();
			transId = this.insertAdjEmolumenstLog(con, pensionno, reportYear,
					notFianalizetransID);

		} catch (SQLException e) {
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} catch (Exception e) {
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		return transId;
	}

	public String insertAdjEmolumenstLog(Connection con, String pensionno,
			String reportYear, String notFianalizetransID) {
		int count = 0, batchId = 0, result = 0;

		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", transSqlQuery = "", transId = "";
		EmolumentslogBean bean = new EmolumentslogBean();
		ArrayList adjEmolList = new ArrayList();
		try {

			st = con.createStatement();
			batchId = this.getBatchId(con);
			if (notFianalizetransID.equals("")) {
				transId = this.getPCTotalsTransId(con, pensionno, reportYear);
			} else {
				transId = notFianalizetransID;
			}
			adjEmolList = this.getDataFromAdjEmolumentsLogTemp(con, pensionno,
					transId, reportYear);
			for (int i = 0; i < adjEmolList.size(); i++) {
				bean = (EmolumentslogBean) adjEmolList.get(i);
				sqlQuery = "insert into  epis_adj_crtn_emoluments_log "
						+ " (pensionno,  cpfacno ,   monthyear,  oldemoluments , newemoluments ,  oldemppfstatuary,newemppfstatuary,  oldempvpf ,  newempvpf , oldprinciple ,  newprinciple , oldinterest ,  newinterest ,  oldpensioncontri  , newensioncontri , updateddate  , remarks  , region	 , username,  computername,batchid,originalrecord,transid) values('"
						+ bean.getPensionNo() + "','" + bean.getCpfAcno()
						+ "','" + bean.getMonthYear() + "','"
						+ bean.getOldEmoluments() + "','"
						+ bean.getNewEmoluments() + "','"
						+ bean.getOldEmppfstatury() + "','"
						+ bean.getNewEmppfstatury() + "','"
						+ bean.getOldEmpvpf() + "','" + bean.getNewEmpvpf()
						+ "','" + bean.getOldPrincipal() + "','"
						+ bean.getNewPrincipal() + "','"
						+ bean.getOldInterest() + "','" + bean.getNewInterest()
						+ "','" + bean.getOldPensioncontri() + "','"
						+ bean.getNewPensioncontri() + "', sysdate,'"
						+ bean.getRemarks() + "','" + bean.getRegion() + "','"
						+ bean.getUserName() + "','" + bean.getComputerName()
						+ "'," + batchId + ",'" + bean.getOriginalRecord()
						+ "'," + transId + ")";

				log.info("====insertAdjEmolumenstLog==============" + sqlQuery);
				log.info("====result==============" + result);
				result = st.executeUpdate(sqlQuery);

				transSqlQuery = "update epis_adj_crtn  set DataModifiedFlag='Y' where  pensionno ="
						+ bean.getPensionNo()
						+ " and empflag='Y' and monthyear='"
						+ bean.getMonthYear() + "'";
				log
						.info("====insertAdjEmolumenstLog========transSqlQuery======"
								+ transSqlQuery);
				st.executeUpdate(transSqlQuery);

			}

			this.deleteDataInAdjCrtnEmolLogTemp(con, pensionno, reportYear);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return transId;
	}

	// By Radha On 16-Feb-2012 for getting emoluments log data Stored in Temp
	// Table
	public ArrayList getDataFromAdjEmolumentsLogTemp(Connection con,
			String pfid, String transid, String reportYear) {
		log
				.info("AdjCrtnDAO:getDataFromAdjEmolumentsLogTemp()-- Entering Method");

		String sqlQuery = "", prefixWhereClause = "", fromYear = "", toYear = "";
		EmolumentslogBean data = new EmolumentslogBean();
		Statement st = null;
		ResultSet rs = null;
		ArrayList emolumentsloginfo = null;
		String years[] = null;
		years = reportYear.split("-");
		if(reportYear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else{
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		}
		sqlQuery = "select * from epis_adj_crtn_emol_log_temp where pensionno="
				+ pfid + " and monthyear between '" + fromYear + "' and '"
				+ toYear + "'";

		log.info("sql query " + sqlQuery);
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery.toString());
			emolumentsloginfo = new ArrayList();
			while (rs.next()) {

				data = new EmolumentslogBean();
				if (rs.getString("PENSIONNO") != null) {
					data.setPensionNo(rs.getString("PENSIONNO"));
				} else {
					data.setPensionNo("");
				}
				if (rs.getString("CPFACNO") != null) {
					data.setCpfAcno(rs.getString("CPFACNO"));
				} else {
					data.setCpfAcno("");
				}
				if (rs.getString("MONTHYEAR") != null) {
					data.setMonthYear(commonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					data.setMonthYear("");
				}
				if (rs.getString("OLDEMOLUMENTS") != null) {
					data.setOldEmoluments(rs.getString("OLDEMOLUMENTS"));
				} else {
					data.setOldEmoluments("");
				}
				if (rs.getString("NEWEMOLUMENTS") != null) {
					data.setNewEmoluments(rs.getString("NEWEMOLUMENTS"));
				} else {
					data.setNewEmoluments("");
				}

				if (rs.getString("OLDEMPPFSTATUARY") != null) {
					data.setOldEmppfstatury(rs.getString("OLDEMPPFSTATUARY"));
				} else {
					data.setOldEmppfstatury("");
				}

				if (rs.getString("NEWEMPPFSTATUARY") != null) {
					data.setNewEmppfstatury(rs.getString("NEWEMPPFSTATUARY"));
				} else {
					data.setNewEmppfstatury("");
				}
				if (rs.getString("OLDEMPVPF") != null) {
					data.setOldEmpvpf(rs.getString("OLDEMPVPF"));
				} else {
					data.setOldEmpvpf("");
				}

				if (rs.getString("NEWEMPVPF") != null) {
					data.setNewEmpvpf(rs.getString("NEWEMPVPF"));
				} else {
					data.setNewEmpvpf("");
				}
				if (rs.getString("OLDPRINCIPLE") != null) {
					data.setOldPrincipal(rs.getString("OLDPRINCIPLE"));
				} else {
					data.setOldPrincipal("");
				}

				if (rs.getString("NEWPRINCIPLE") != null) {
					data.setNewPrincipal(rs.getString("NEWPRINCIPLE"));
				} else {
					data.setNewPrincipal("");
				}

				if (rs.getString("OLDINTEREST") != null) {
					data.setOldInterest(rs.getString("OLDINTEREST"));
				} else {
					data.setOldInterest("");
				}

				if (rs.getString("NEWINTEREST") != null) {
					data.setNewInterest(rs.getString("NEWINTEREST"));
				} else {
					data.setNewInterest("");
				}
				if (rs.getString("OLDPENSIONCONTRI") != null) {
					data.setOldPensioncontri(rs.getString("OLDPENSIONCONTRI"));
				} else {
					data.setOldPensioncontri("");
				}
				if (rs.getString("NEWENSIONCONTRI") != null) {
					data.setNewPensioncontri(rs.getString("NEWENSIONCONTRI"));
				} else {
					data.setNewPensioncontri("");
				}

				if (rs.getString("UPDATEDDATE") != null) {
					data.setUpdatedDate(CommonUtil.converDBToAppFormat(rs
							.getDate("UPDATEDDATE")));
				} else {
					data.setUpdatedDate("");
				}
				if (rs.getString("REMARKS") != null) {
					data.setRemarks(rs.getString("REMARKS"));
				} else {
					data.setRemarks("");
				}
				if (rs.getString("REGION") != null) {
					data.setRegion(rs.getString("REGION"));
				} else {
					data.setRegion("");
				}

				if (rs.getString("region") != null) {
					data.setRegion(rs.getString("region"));
				} else {
					data.setRegion("");
				}

				if (rs.getString("USERNAME") != null) {
					data.setUserName(rs.getString("USERNAME"));
				} else {
					data.setUserName("");
				}

				if (rs.getString("COMPUTERNAME") != null) {
					data.setComputerName(rs.getString("COMPUTERNAME"));
				} else {
					data.setComputerName("");
				}
				if (rs.getString("ORIGINALRECORD") != null) {
					data.setOriginalRecord(rs.getString("ORIGINALRECORD"));
				} else {
					data.setComputerName("");
				}
				emolumentsloginfo.add(data);
			}

			log.info("emolumentsloginfo list size " + emolumentsloginfo.size());

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return emolumentsloginfo;
	}

	// By Radha On 16-Feb-2012 for for Deleting Records from Adj Emoluments
	// LogTemp Table
	public void deleteDataInAdjCrtnEmolLogTemp(Connection con,
			String pensionno, String reportYear) {
		Statement st = null;
		ResultSet rs = null;
		String deleteQuery = "", fromYear = "", toYear = "";
		int result = 0;
		EmolumentslogBean bean = new EmolumentslogBean();
		String years[] = null;
		years = reportYear.split("-");
		if(reportYear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else{
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		}
		try {

			st = con.createStatement();

			deleteQuery = "delete from epis_adj_crtn_emol_log_temp where pensionno="
					+ pensionno
					+ " and monthyear between '"
					+ fromYear
					+ "' and '" + toYear + "'";
			log.info("deleteDataInAdjCrtnEmolLogTemp()===deleteQuery== "
					+ deleteQuery);
			result = st.executeUpdate(deleteQuery);
			log.info("deleteDataInAdjCrtnEmolLogTemp()===result== " + result);
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

	}

	public int getBatchId(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		int batchId = 0;
		try {
			st = con.createStatement();
			sqlQuery = "select batchid.nextval as batchid from dual";
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				batchId = rs.getInt("batchid");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return batchId;
	}

	// By Radha On 16-Feb-2012 for Unique Id for Storing Adj Emoluments in Temp
	// Table
	public int getBatchIdForAdjTemp(Connection con) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		int batchId = 0;
		try {
			st = con.createStatement();
			sqlQuery = "select adj_temp_batchid.nextval as adj_temp_batchid from dual";
			rs = st.executeQuery(sqlQuery);
			if (rs.next()) {
				batchId = rs.getInt("adj_temp_batchid");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return batchId;
	}

	/*
	 * By Prasad on 28-Feb-2012 deletion in epis_adj_crtn_transactions By Prasad
	 * on 22-Feb-2012 added new flag for the purpose of deleting the uploaded
	 * temp table By Radha on 16-Feb-2012 for deletion in
	 * epis_adj_crtn_emol_log_temp By Radha on 18-Jan-2012 for deletion in
	 * epis_adj_crtn_pfid_tracking
	 */

	

	public String chkPfidinAdjCrtnTrackingForUpload(String pfid, String frmName) {
		String sqlQuery = "", chkpfid = "", tableName = "";
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		if (frmName.equals("adjcorrections")) {
			tableName = "epis_adj_crtn_pfid_tracking";
		}
		sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid
				+ " and  DATAMAPPED='U'";
		log.info("--chkPfidinAdjCrtnTrackingForUpload()---" + sqlQuery);
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
// Implementing  Year Wise Segrigation 
	public ArrayList getAdjEmolumentsLog(String pfId, String adjOBYear,
			String frmName) {
		log.info("AdjCrtnDAO:getEmolumentslog-- Entering Method");

		String sqlQuery = "", prefixWhereClause = "";
		EmolumentslogBean data = new EmolumentslogBean();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList emolumentsloginfo = null;

		sqlQuery = " select data.*,to_char(data.updateddate,'HH:MI:SS AM') as batchtime ,prv.ADJOBYEAR as ADJOBYEAR from (select * from epis_adj_crtn_emoluments_log where pensionno = "
				+ pfId
				+ "  and originalrecord='N') data,"
				+ "   (SELECT  batchid,updateddate ,monthyear   from epis_adj_crtn_emoluments_log   where pensionno =  "
				+ pfId
				+ " and  originalrecord = 'N'  group by batchid,updateddate,monthyear) d , (select  adjobyear ,transid  from epis_adj_crtn_prv_pc_totals"
				+" where pensionno = "+pfId+") prv  where  data.transid = prv.transid and data.batchid = d.batchid   and data.monthyear=d.monthyear order by d.batchid,d.monthyear ";

		log.info("sql query " + sqlQuery);
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery.toString());
			emolumentsloginfo = new ArrayList();
			while (rs.next()) {
				/*
				 * log.info("OLDEMOLUMENTS" + rs.getString("OLDEMOLUMENTS"));
				 * log.info("NEWEMOLUMENTS" + rs.getString("NEWEMOLUMENTS"));
				 * log.info("PENSIONNO" + rs.getString("PENSIONNO"));
				 * log.info("MONTHYEAR" + commonUtil.converDBToAppFormat(rs
				 * .getDate("UPDATEDDATE")));
				 */
				data = new EmolumentslogBean();

				if (rs.getString("PENSIONNO") != null) {
					data.setPensionNo(rs.getString("PENSIONNO"));
				} else {
					data.setPensionNo("");
				}
				if (rs.getString("MONTHYEAR") != null) {
					data.setMonthYear(CommonUtil.converDBToAppFormat(rs
							.getDate("MONTHYEAR")));
				} else {
					data.setMonthYear("");
				}
				if (rs.getString("OLDEMOLUMENTS") != null) {
					data.setOldEmoluments(rs.getString("OLDEMOLUMENTS"));
				} else {
					data.setOldEmoluments("");
				}
				if (rs.getString("NEWEMOLUMENTS") != null) {
					data.setNewEmoluments(rs.getString("NEWEMOLUMENTS"));
				} else {
					data.setNewEmoluments("");
				}

				if (rs.getString("OLDEMPPFSTATUARY") != null) {
					data.setOldEmppfstatury(rs.getString("OLDEMPPFSTATUARY"));
				} else {
					data.setOldEmppfstatury("");
				}

				if (rs.getString("NEWEMPPFSTATUARY") != null) {
					data.setNewEmppfstatury(rs.getString("NEWEMPPFSTATUARY"));
				} else {
					data.setNewEmppfstatury("");
				}
				if (rs.getString("OLDEMPVPF") != null) {
					data.setOldEmpvpf(rs.getString("OLDEMPVPF"));
				} else {
					data.setOldEmpvpf("");
				}

				if (rs.getString("NEWEMPVPF") != null) {
					data.setNewEmpvpf(rs.getString("NEWEMPVPF"));
				} else {
					data.setNewEmpvpf("");
				}

				if (rs.getString("UPDATEDDATE") != null) {
					data.setUpdatedDate(CommonUtil.converDBToAppFormat(rs
							.getDate("UPDATEDDATE")));
				} else {
					data.setUpdatedDate("");

				}
				if (rs.getString("batchtime") != null) {
					data.setBatchTime(rs.getString("batchtime"));
				} else {
					data.setBatchTime("");
				}
				if (rs.getString("region") != null) {
					data.setRegion(rs.getString("region"));
				} else {
					data.setRegion("");
				}
				if (rs.getString("BATCHID") != null) {
					data.setBatchId(rs.getString("BATCHID"));
				} else {
					data.setBatchId("");
				}
				if (rs.getString("USERNAME") != null) {
					data.setUserName(rs.getString("USERNAME"));
				} else {
					data.setUserName("");
				}
				if (rs.getString("ADJOBYEAR") != null) {
					data.setAdjObYear(rs.getString("ADJOBYEAR"));
				} else {
					data.setAdjObYear("");
				}
				emolumentsloginfo.add(data);
			}

			log.info("emolumentsloginfo list size " + emolumentsloginfo.size());

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return emolumentsloginfo;
	}

	// new method on 18-Feb-2012 By Radha for tracking of notfinalized transid
	public String getAdjCrtnNotFinalizedTransId(String empserialNO,
			String reportYear) {
		log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId Entering method");
		Connection con = null;
		String notFianalizetransID = "";
		try {

			con = DBUtils.getConnection();

			notFianalizetransID = getAdjCrtnNotFinalizedTransId(con,
					empserialNO, reportYear);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId leaving method");
		return notFianalizetransID;
	}

	public String getAdjCrtnNotFinalizedTransId(Connection con,
			String empserialNO, String reportYear) {
		log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId Entering method");

		Statement st = null;
		ResultSet rs = null;

		String selectSQL = "", notFianalizetransID = "", fromYear = "", toYear = "";
		String years[] = null;
		years = reportYear.split("-");
		if(reportYear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else{
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		}
		selectSQL = "select distinct transid  as transid from epis_adj_crtn_emol_log_temp where pensionno= "
				+ empserialNO
				+ " and monthyear between '"
				+ fromYear
				+ "' and '" + toYear + "'";

		try {
			log.info("selectSQL==" + selectSQL);

			st = con.createStatement();
			rs = st.executeQuery(selectSQL);
			if (rs.next()) {
				if (rs.getString("transid") != null) {
					notFianalizetransID = rs.getString("transid");
				} else {
					notFianalizetransID = "";
				}

			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId leaving method");
		return notFianalizetransID;
	}

	// new method on 13-Feb-2012
	public ArrayList updatePCAdjCorrections(String fromDate, String toDate,
			String region, String airportcode, String empserialNO,
			String cpfaccnostrip, String regionstrip) {

		ArrayList penContHeaderList = new ArrayList();
		ArrayList penContDataList = new ArrayList();
		String mappingflag = "true";
		penContHeaderList = this.PCHeaderInfoForAdjCrtn(region, airportcode,
				empserialNO);
		String adjOBYear = "", adjOBRemarks = "";
		try {
			String getToYear = commonUtil.converDBToAppFormat(toDate,
					"dd-MMM-yyyy", "yyyy");
			log.info("getToYear " + getToYear);

		} catch (InvalidDataException e1) {
			// TODO Auto-generated catch block
			log.printStackTrace(e1);
		}

		String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "";
		String[] cpfaccnos = new String[10];
		String[] dupCpfaccnos = new String[10];
		String[] regions = new String[10];
		String[] empPensionList = null;
		String[] pensionInfo = null;
		CommonDAO commonDAO = new CommonDAO();
		String pensionList = "", tempCPFAcno = "", tempRegion = "", dateOfRetriment = "", getMnthYear = "";
		double totalEmoluments = 0.0, pfStaturary = 0.0, totalPension = 0.0, empVpf = 0.0, principle = 0.0, interest = 0.0, pfContribution = 0.0;
		double grandEmoluments = 0.0, grandCPF = 0.0, grandPension = 0.0, grandPFContribution = 0.0;
		double cpfInterest = 0.0, pensionInterest = 0.0, pfContributionInterest = 0.0;
		double grandCPFInterest = 0.0, calOptionRevised = 0.0, grandPensionInterest = 0.0, grandPFContributionInterest = 0.0;
		double cumPFStatury = 0.0, cumPension = 0.0, cumPfContribution = 0.0;
		double cpfOpeningBalance = 0.0, penOpeningBalance = 0.0, pfOpeningBalance = 0.0, rateOfInterest = 0.0;

		DecimalFormat df = new DecimalFormat("#########0");
		ArrayList penConReportList = new ArrayList();
		log.info("=================Update PC Report Starts===========");
		log.info("Header Size" + penContHeaderList.size());
		String dupCpf = "", dupRegion = "", countFlag = "";
		int yearCount = 0;
		int totalRecordIns = 0, inserted = 0;
		Connection con = null;
		try {
			con = DBUtils.getConnection();
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

				penContBean.setPensionNo(commonDAO.getPFID(penContBean
						.getEmployeeNM(), penContBean.getEmpDOB(), commonUtil
						.leadingZeros(5, penContHeaderBean.getEmpSerialNo())));

				empSerialNumber = penContHeaderBean.getEmpSerialNo();

				double totalAAICont = 0.0, calCPF = 0.0, calPens = 0.0;
				ArrayList employeFinanceList = new ArrayList();
				// String preparedString = penContHeaderBean.getPrepareString();
				try {
					dateOfRetriment = commonDAO.getRetriedDate(penContBean
							.getEmpDOB());
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ArrayList rateList = new ArrayList();
				String findFromDate = "", monthInfo = "", findMnt = "";
				findFromDate = this.compareTwoDates(penContHeaderBean
						.getDateOfEntitle(), fromDate);
				log.info("Find From Date" + findFromDate);
				// the below line for table check which table to hit the
				// recoverie data

				cpfacno = penContHeaderBean.getCpfacno();
				empRegion = penContHeaderBean.getEmpRegion();

				cpfaccnos = cpfacno.split("=");
				regions = empRegion.split("=");
				String preparedString = commonDAO.preparedCPFString(cpfaccnos,
						regions);
				String empCpfaccno = "";
				log.info("preparedString is " + preparedString);
				if (cpfaccnos.length >= 1) {
					for (int k = 0; k < cpfaccnos.length; k++) {
						region = regions[k];
						empCpfaccno = cpfaccnos[k];
					}
				}

				penContBean.setEmpRegion(region);
				penContBean.setEmpCpfaccno(empCpfaccno);

				boolean recoverieTable = false;
				pensionList = this.getEmployeePensionInfoForAdjCrtn(
						preparedString, fromDate, toDate, penContHeaderBean
								.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), empserialNO);

				String rateFromYear = "", rateToYear = "", checkMnthDate = "", dispFromYear = "", monthInterestInfo = "", dispFromMonth = "";
				ArrayList blockList = new ArrayList();
				boolean dispYearFlag = false, yearBreak = false;
				boolean rateFlag = false;
				if (!pensionList.equals("")) {
					grandEmoluments = 0.0;
					grandCPF = 0.0;
					grandPension = 0.0;
					grandCPFInterest = 0.0;
					grandPensionInterest = 0.0;
					grandPFContribution = 0.0;
					grandPFContributionInterest = 0.0;
					cumPFStatury = 0.0;
					cumPension = 0.0;
					cumPfContribution = 0.0;
					totalEmoluments = 0;
					pfStaturary = 0;
					totalPension = 0;
					pfContribution = 0;
					cpfInterest = 0;
					pensionInterest = 0;
					pfContributionInterest = 0;
					cpfOpeningBalance = 0.0;
					penOpeningBalance = 0.0;
					pfOpeningBalance = 0.0;
					empPensionList = pensionList.split("=");
					String penTempMnthInfo = empPensionList[empPensionList.length - 1];
					String[] penMnthInfo = penTempMnthInfo.split(",");
					log.info(penMnthInfo[20]);
					blockList = commonDAO.getMonthList(con, penMnthInfo[20]);

					if (empPensionList != null) {
						for (int r = 0; r < empPensionList.length; r++) {
							TempPensionTransBean bean = new TempPensionTransBean();
							tempPensionInfo = empPensionList[r];
							pensionInfo = tempPensionInfo.split(",");
							bean.setMonthyear(pensionInfo[0]);
							dispFromMonth = commonUtil.converDBToAppFormat(bean
									.getMonthyear(), "dd-MMM-yyyy", "MMM");
							if (dispYearFlag == false
									&& dispFromMonth.equals("Mar")) {
								if (dispFromYear.equals("")) {
									dispFromYear = commonUtil
											.converDBToAppFormat(bean
													.getMonthyear(),
													"dd-MMM-yyyy", "yy");
								}

								getMnthYear = commonUtil.converDBToAppFormat(
										bean.getMonthyear(), "dd-MMM-yyyy",
										"MM-yy");

								monthInterestInfo = commonDAO.getBlockYear(
										getMnthYear, blockList);
								// log.info("Month Info"+monthInterestInfo);
								String[] monthInterestList = monthInterestInfo
										.split(",");

								monthInfo = monthInterestList[1];

								rateOfInterest = new Double(
										monthInterestList[0]).doubleValue();

								dispYearFlag = true;
							}
							bean.setEmoluments(pensionInfo[1]);
							bean.setCpf(pensionInfo[2]);
							bean.setEmpVPF(pensionInfo[3]);
							bean.setEmpAdvRec(pensionInfo[4]);
							bean.setEmpInrstRec(pensionInfo[5]);
							bean.setStation(pensionInfo[6]);
							bean.setPensionContr(pensionInfo[7]);
							calCPF = Math.round(Double.parseDouble(bean
									.getCpf()));
							bean.setDeputationFlag(pensionInfo[19].trim());
							DateFormat dateFormat1 = new SimpleDateFormat(
									"dd-MMM-yy");

							Date transdate1 = dateFormat1.parse(pensionInfo[0]);

							if (transdate1.before(new Date("31-Mar-08"))
									&& (bean.getDeputationFlag().equals("N") || bean
											.getDeputationFlag().equals(""))) {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[7]));

								totalAAICont = calCPF - calPens;
							} else {
								calPens = Math.round(Double
										.parseDouble(pensionInfo[12]));
								bean.setPensionContr(pensionInfo[12]);

								if (pensionInfo[21].trim().equals("N")) {
									totalAAICont = calCPF - calPens;
								} else {
									calOptionRevised = Double
											.parseDouble(pensionInfo[21]);
									totalAAICont = calPens - calOptionRevised;
								}

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
							findMnt = commonUtil.converDBToAppFormat(bean
									.getMonthyear(), "dd-MMM-yyyy", "MM-yy");

							totalEmoluments = new Double(df
									.format(totalEmoluments
											+ Math.round(Double
													.parseDouble(bean
															.getEmoluments()))))
									.doubleValue();
							pfStaturary = new Double(df.format(pfStaturary
									+ Math.round(Double.parseDouble(bean
											.getCpf())))).doubleValue();
							cumPFStatury = cumPFStatury + pfStaturary;
							empVpf = new Double(df.format(empVpf
									+ Math.round(Double.parseDouble(bean
											.getEmpVPF())))).doubleValue();
							principle = new Double(df.format(principle
									+ Math.round(Double.parseDouble(bean
											.getEmpAdvRec())))).doubleValue();
							interest = new Double(df.format(interest
									+ Math.round(Double.parseDouble(bean
											.getEmpInrstRec())))).doubleValue();
							totalPension = new Double(df.format(totalPension
									+ Math.round(Double.parseDouble(bean
											.getPensionContr()))))
									.doubleValue();
							cumPension = cumPension + totalPension;

							pfContribution = new Double(df
									.format(pfContribution
											+ Math.round(Double
													.parseDouble(bean
															.getAaiPFCont()))))
									.doubleValue();
							cumPfContribution = cumPfContribution
									+ pfContribution;

							if (findMnt.equals(monthInfo)) {
								yearBreak = true;
							}
							if (yearBreak == true) {
								cpfInterest = Math.round((cumPFStatury
										* rateOfInterest / 100) / 12)
										+ Math.round(cpfOpeningBalance
												* rateOfInterest / 100);
								pensionInterest = Math.round((cumPension
										* rateOfInterest / 100) / 12)
										+ Math.round(penOpeningBalance
												* rateOfInterest / 100);
								pfContributionInterest = Math
										.round((cumPfContribution
												* rateOfInterest / 100) / 12)
										+ Math.round(pfOpeningBalance
												* rateOfInterest / 100);
								yearBreak = false;
								// log.info(bean.getMonthyear()+"cpfInterest"+
								// cpfInterest+"cumPFStatury"+cumPFStatury);
								cpfOpeningBalance = Math.round(pfStaturary
										+ cpfInterest
										+ Math.round(cpfOpeningBalance));
								penOpeningBalance = Math.round(totalPension
										+ pensionInterest
										+ Math.round(penOpeningBalance));
								pfOpeningBalance = Math.round(pfContribution
										+ pfContributionInterest
										+ Math.round(pfOpeningBalance));
								grandEmoluments = grandEmoluments
										+ totalEmoluments;
								grandCPF = grandCPF + pfStaturary;
								grandPension = grandPension + totalPension;
								grandPFContribution = grandPFContribution
										+ pfContribution;

								grandCPFInterest = grandCPFInterest
										+ cpfInterest;
								grandPensionInterest = grandPensionInterest
										+ pensionInterest;
								grandPFContributionInterest = grandPFContributionInterest
										+ pfContributionInterest;
								cumPFStatury = 0.0;
								cumPension = 0.0;
								cumPfContribution = 0.0;
								totalEmoluments = 0;
								pfStaturary = 0;
								totalPension = 0;
								pfContribution = 0;
								cpfInterest = 0;
								pensionInterest = 0;
								pfContributionInterest = 0;

								dispYearFlag = false;

							}

						}

					}

					// Need to implement the totals
					penContDataList.add(new Double(grandEmoluments));
					penContDataList.add(new Double(grandCPF));
					penContDataList.add(new Double(grandCPFInterest));
					penContDataList.add(new Double(grandPension));
					penContDataList.add(new Double(grandPensionInterest));
					penContDataList.add(new Double(grandPFContribution));
					penContDataList
							.add(new Double(grandPFContributionInterest));

				}
			}

		} catch (SQLException se) {
			log.printStackTrace(se);
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			commonDB.closeConnection(con, null, null);
		}
		log.info("penContDataList============================================"
				+ penContDataList.size());
		return penContDataList;
	}

	private ArrayList PCHeaderInfoForAdjCrtn(String region, String airportCD,
			String empserialNO) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		log.info("region in reportDao********  " + region);

		// The Below mentioned method for retrieving the mapping info from
		// PensionContribution screen hitting
		sqlQuery = this.buildPenConEmpInfoQuery(region, airportCD, empserialNO);

		log.info("PCHeaderInfoForAdhCrtn===Query" + sqlQuery);
		String tempSrlNumber = "", srlNumber = "", doj = "", dob = "", cpfacnos = "", regions = "", fhName = "", employeeName = "", designation = "";
		String tempRegion = "", tempCPF = "", department = "";
		String finalSettlementdate = "";
		ArrayList finalList = new ArrayList();
		int totalRS = 0, tempTotalRs = 0, totalSrlNo = 0, totalRecCpf = 0;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			ArrayList list = new ArrayList();
			PensionContBean bean = new PensionContBean();
			totalRS = this.getEmpPensionCountForAdjCrtn(empserialNO);
			String wetherOption = "", pfSettled = "", interestCalcUpto = "", dateofSeperationDate = "";
			while (rs.next()) {
				tempTotalRs++;
				// code modified as on Feb 19th
				if (rs.getString("employeename") != null) {
					employeeName = rs.getString("employeename");
				}
				if (rs.getString("wetheroption") != null) {
					wetherOption = rs.getString("wetheroption").trim();
					if(!wetherOption.equals("A")){
						wetherOption="B";
					}
				}
				// Enable after updating whetheroption in epis_info_adj_crtn for having option is null
				/*else{
					wetherOption="B";
				}*/
				
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
			log.info("tempSrlNumber" + tempSrlNumber + "bean.cpfacnos"
					+ bean.getCpfacno() + "regions" + regions
					+ finalList.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		return finalList;
	}

	public String buildPenConEmpInfoQuery(String region, String airportCD,
			String empserialNO) {
		log.info("AdjCrtnDAO :buildPenConEmpInfoQuery() entering method");
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
			conn = DBUtils.getConnection();
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

				if (rs.getString("FINALSETTLMENTDT") != null) {
					personalfinalsettlementdate = CommonUtil
							.converDBToAppFormat(rs.getDate("FINALSETTLMENTDT"));
				}
				if (rs.getString("INTERESTCALCDATE") != null) {
					interestCalUpto = CommonUtil.converDBToAppFormat(rs
							.getDate("INTERESTCALCDATE"));
				}
				if (rs.getString("DATEOFSEPERATION_DATE") != null) {
					dateofSeperationDate = CommonUtil.converDBToAppFormat(rs
							.getDate("DATEOFSEPERATION_DATE"));
				}
			}
			String settlementDate = "";
			settlementDate = personalfinalsettlementdate;

			sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,REGION,'"
					+ settlementDate
					+ "'AS FINALSETTLMENTDT,'"
					+ interestCalUpto
					+ "' as InterestCalupto,'"
					+ dateofSeperationDate
					+ "'as DATEOFSEPERATION_DATE,PENSIONNUMBER,MARITALSTATUS,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EPIS_Info_ADJ_CRTN WHERE EMPSERIALNUMBER IS NOT NULL  and EMPSERIALNUMBER='"
					+ empserialNO + "'  ";

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
			log.info("AdjCrtnDAO :buildQuery() leaving method");
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

	public int getEmpPensionCountForAdjCrtn(String empserialNO) {
		int count = 0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		FinancialYearBean yearBean = new FinancialYearBean();
		if (empserialNO.equals("")) {
			sqlQuery = "SELECT COUNT(*) AS COUNT FROM  EPIS_Info_ADJ_CRTN WHERE empserialnumber is not null  ORDER BY empserialnumber";

		} else {
			sqlQuery = "SELECT COUNT(*) AS COUNT FROM  EPIS_Info_ADJ_CRTN WHERE empserialnumber is not null  and empserialnumber='"
					+ empserialNO + "' ORDER BY empserialnumber";
		}
		try {
			con = DBUtils.getConnection();
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

	// New Method

	private String getEmployeePensionInfoForAdjCrtn(String cpfString,
			String fromDate, String toDate, String whetherOption,
			String dateOfRetriment, String dateOfBirth, String Pensionno) {

		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		String tablename = "EPIS_ADJ_CRTN";

		log.info("formdate " + fromDate + "todate " + toDate);
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from epis_info_adj_crtn where ("
				+ tempCpfString + ") and rownum=1)";
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.ADVANCE AS ADVANCE,emp.PFWSUB AS PFWSUB,emp.PFWCONTRI AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI,emp.EDITTRANS as EDITTRANS,emp.ARREARSBREAKUP as ARREARSBREAKUP from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from  employee_pension_validate  "
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,NVL(ADVANCE,0.00) AS ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,EDITTRANS,ARREARSBREAKUP FROM "
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
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,nvl(emp.ADVANCE,0.00) AS ADVANCE,nvl(emp.PFWSUB,0.00) AS PFWSUB,nvl(emp.PFWCONTRI,0.00) AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI,NVL(EDITTRANS,'N') as EDITTRANS, NVL(ARREARSBREAKUP,'N') AS ARREARSBREAKUP  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,nvl(ADVANCE,0.00) as ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,NVL(EDITTRANS,'N') as EDITTRANS, NVL(ARREARSBREAKUP,'N') AS ARREARSBREAKUP  "
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
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('"
				+ fromDate + "')";

		// String advances =
		// "select amount from employee_pension_advances where pensionno=1";

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
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info(sqlQuery);
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
					buffer.append(Double.toString(pensionCOntr));
				} else {
					buffer.append("0");
				}
				buffer.append(",");
				if (rs.getDate("EMPMNTHYEAR") != null) {
					buffer.append(CommonUtil.converDBToAppFormat(rs
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
				// Regarding pensioncontri calcuation upto current date modified
				// by radha p
				if (transdate.after(new Date(commonUtil
						.getCurrentDate("dd-MMM-yy")))
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
								.getString("EMOLUMENTS"), whetherOption,
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
				// Datafreezeflag
				buffer.append("-NA-");

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
				if (rs.getString("ADVANCE") != null) {
					buffer.append(rs.getString("ADVANCE"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWSUB") != null) {
					buffer.append(rs.getString("PFWSUB"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWCONTRI") != null) {
					buffer.append(rs.getString("PFWCONTRI"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("editeddate") != null) {
					buffer.append(rs.getString("editeddate"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
					buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
				} else {
					buffer.append("N");
				}
				buffer.append(",");
				
				if (rs.getString("EDITTRANS") != null) {
					buffer.append(rs.getString("EDITTRANS"));
				} else {
					buffer.append("N");
				}
				buffer.append(",");
				
				if (rs.getString("ARREARSBREAKUP") != null) {
					buffer.append(rs.getString("ARREARSBREAKUP"));
				} else {
					buffer.append("N");
				}
				buffer.append("=");
				

			}
			/*
			 * if (count == i) { buffer = new StringBuffer(); } else { }
			 */

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("----- " + buffer.toString());
		return buffer.toString();

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
			contr.setDateOfEntitle("01-Apr-1995");
		}
		return contr;
	}

	// New Method
	public ArrayList getPensionContributionReportForAdjCRTN(String fromDate,
			String toDate, String region, String airportcode,
			String empserialNO, String cpfAccno, String batchid,
			String ReportStatus) {
		ArrayList penContHeaderList = new ArrayList();

		// For Fetching the Employee PersonalInformation
		penContHeaderList = this.PCHeaderInfoForAdjCrtn(region, airportcode,
				empserialNO);

		String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "", empCpfaccno = "";
		String[] cpfaccnos = new String[10];
		String[] regions = new String[10];
		String[] empPensionList = null;
		String[] pensionInfo = null;
		CommonDAO commonDAO = new CommonDAO();
		String pensionList = "", dateOfRetriment = "";
		ArrayList penConReportList = new ArrayList();
		log.info("Header Size" + penContHeaderList.size());
		log.info("" + penContHeaderList);
		String countFlag = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getConnection();
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
				// log.info(penContHeaderBean.getWhetherOption() + "option");
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

				// The Below mentioned method for Preparing the CPFSTRING based
				// on CPFACCNOs and corresponding regions.
				String preparedString = commonDAO.preparedCPFString(cpfaccnos,
						regions);
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
				if (ReportStatus.equals("userRequired")) {
					// For User Specified Report Generation Condition
					pensionList = this
							.getEmployeePensionInfoForAdjCrtnForUserRequired(
									preparedString, fromDate, toDate,
									penContHeaderBean.getWhetherOption(),
									dateOfRetriment, penContBean.getEmpDOB(),
									empserialNO, batchid);
				} else {
					// Normal Condition
					pensionList = this.getEmployeePensionInfoForAdjCrtn(
							preparedString, fromDate, toDate, penContHeaderBean
									.getWhetherOption(), dateOfRetriment,
							penContBean.getEmpDOB(), empserialNO);
				}
				String checkMnthDate = "", rateOfInterest = "";
				st = con.createStatement();
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

							// log.info("contribution "+pensionInfo[7]);
							bean.setPensionContr(pensionInfo[7]);
							// calCPF=Double.parseDouble(bean.getCpf());
							// calPens=Double.parseDouble(pensionInfo[7]);
							calCPF = Math.round(Double.parseDouble(bean
									.getCpf()));
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
							// log.info("calCPF " +calCPF +"calPens "+calPens );
							bean.setGenMonthYear(pensionInfo[8]);
							bean.setTransCpfaccno(pensionInfo[9]);
							bean.setRegion(pensionInfo[10]);
							// log.info("pensionInfo[12] " + pensionInfo[12]);
							bean.setRecordCount(pensionInfo[11]);
							bean.setDbPensionCtr(pensionInfo[12]);
							bean.setDataFreezFlag(pensionInfo[13]);
							bean.setForm7Narration(pensionInfo[14]);
							bean.setPcHeldAmt(pensionInfo[15]);
							bean.setNoofMonths(pensionInfo[16]);
							bean.setPccalApplied(pensionInfo[17].trim());
							bean.setAdvAmount(pensionInfo[21].trim());
							bean.setEmployeeLoan(pensionInfo[22].trim());
							bean.setAaiLoan(pensionInfo[23].trim());
							bean.setEditedDate(pensionInfo[24].trim());
							bean.setEditedTransFlag(pensionInfo[26].trim());
							bean.setArrearBrkUpFlag(pensionInfo[27].trim());
							// log.info("PcApplied " +bean.getPccalApplied());
							if (bean.getPccalApplied().equals("N")) {
								bean.setCpf("0.00");
								bean.setAaiPFCont("0.00");
								bean.setPensionContr("0.00");
								bean.setDbPensionCtr("0.00");
							}
							bean.setArrearFlag(pensionInfo[18].trim());
							bean.setDeputationFlag(pensionInfo[19].trim());
							// For Fetching the Advances and Loans by Month Wise
							// for editing the Amount in Recoveries Screen
							String monthYear1 = commonUtil.converDBToAppFormat(
									pensionInfo[0], "dd-MMM-yyyy", "MMM-yyyy");

							// log.info("loan " + bean.getEmployeeLoan());
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

	// New Method

	public ArrayList getPrevPCGrandTotalsForAdjCrtn(String pfid,
			String adjOBYear, String batchid, String transIdToGetPrevData) {
		String query = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList grandTotList = new ArrayList();
		DecimalFormat df = new DecimalFormat("#########0");
		double emolumentsTotal = 0.00, CPFTotal = 0.0, PensionTotal = 0.00, PFTotal = 0.00, EmpSubscription = 0.00, AAIContribution = 0.00, adjEmpSubInterest = 0.00, adjAAiContriInterest = 0.00, adjPesnionContri = 0.00,adjFSEmpSubIntrst =0.00, adjFSContriIntrst =0.00;

		String transid = "";
		try {
			if (transIdToGetPrevData.equals("")) {

				if (batchid.equals("")) {
					query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
							+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
							+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST,"
							+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST from epis_adj_crtn_prv_pc_totals e"
							+ " where e.pensionno = "
							+ pfid
							+ "  and e.adjobyear ='"
							+ adjOBYear
							+ "' and e.transid = (select max(e.transid) from  epis_adj_crtn_prv_pc_totals e"
							+ " where e.pensionno = "
							+ pfid
							+ "   and e.adjobyear = '" + adjOBYear + "')";
				} else {
					query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
							+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
							+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST, "
							+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST from epis_adj_crtn_prv_pc_totals e"
							+ " where e.pensionno = "
							+ pfid
							+ "  and e.adjobyear ='"
							+ adjOBYear
							+ "' and e.transid =(select distinct(transid) from  epis_adj_crtn_emoluments_log where pensionno="
							+ pfid + " and batchid=" + batchid + ")";
				}
			} else {
				// For Making 1 r more batches transaction with out Finalizing
				// and after Finalizing getting Ist entry of grand Totals
				/*
				 * query = "select max(transid) as transid,
				 * sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal,
				 * 0.00) + nvl(e.cpfinterest, 0.00)) as
				 * CPFTotal,sum(nvl(e.pensiontotal, 0.00) +
				 * nvl(e.pensioninterest, 0.00)) as PensionTotal," + "
				 * sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as
				 * PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest,
				 * 0.00)) as EmpSubscription, sum(nvl(e.ADJEMPSUBINTRST, 0.00))
				 * as ADJEMPSUBINTRST," + " sum(nvl(e.aaicontri, 0.00) +
				 * nvl(e.aaicontriinterest, 0.00)) as AAIContribution ,
				 * sum(nvl(e.ADJAAICONTRIINTRST, 0.00)) as ADJAAICONTRIINTRST,
				 * sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00)) as
				 * ADJPENSIONCONTRIINTRST from epis_adj_crtn_prv_pc_totals e" + "
				 * where e.pensionno = " + pfid + " and e.adjobyear ='" +
				 * adjOBYear + "' and e.transid = (select e.transid from
				 * epis_adj_crtn_prv_pc_totals e where e.pensionno = "+pfid+"
				 * and e.adjobyear = '" + adjOBYear + "' and " + " transid <
				 * (select max(e.transid) from epis_adj_crtn_prv_pc_totals e
				 * where e.pensionno = "+pfid+" and e.adjobyear = '" + adjOBYear +
				 * "'))";
				 */

				query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
						+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
						+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST,"
						+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST from epis_adj_crtn_prv_pc_totals e"
						+ " where e.pensionno = "
						+ pfid
						+ "  and e.adjobyear ='"
						+ adjOBYear
						+ "' and e.transid = " + transIdToGetPrevData;

			}
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("----getPrevPCGrandTotalsForAdjCrtn--query--" + query);
			rs = st.executeQuery(query);

			if (rs.next()) {

				if (rs.getString("transid") != null) {
					transid = rs.getString("transid");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					emolumentsTotal = rs.getDouble("EMOLUMENTS");
				}
				if (rs.getString("CPFTotal") != null) {
					CPFTotal = rs.getDouble("CPFTotal");
				}
				if (rs.getString("PensionTotal") != null) {
					PensionTotal = rs.getDouble("PensionTotal");
				}
				if (rs.getString("PFTotal") != null) {
					PFTotal = rs.getDouble("PFTotal");
				}
				if (rs.getString("EmpSubscription") != null) {
					EmpSubscription = rs.getDouble("EmpSubscription");
				}
				if (rs.getString("AAIContribution") != null) {
					AAIContribution = rs.getDouble("AAIContribution");
				}
				if (rs.getString("ADJEMPSUBINTRST") != null) {
					adjEmpSubInterest = rs.getDouble("ADJEMPSUBINTRST");
				}
				if (rs.getString("ADJAAICONTRIINTRST") != null) {
					adjAAiContriInterest = rs.getDouble("ADJAAICONTRIINTRST");
				}
				if (rs.getString("ADJPENSIONCONTRIINTRST") != null) {
					adjPesnionContri = rs.getDouble("ADJPENSIONCONTRIINTRST"); 
				}
				if (rs.getString("ADJFINEMPSUBINTRST") != null) {
					adjFSEmpSubIntrst = rs.getDouble("ADJFINEMPSUBINTRST");
				}
				if (rs.getString("ADJFINAAICONTRIINTRST") != null) {
					adjFSContriIntrst = rs.getDouble("ADJFINAAICONTRIINTRST");
				}
				log.info("--transid--" + transid + "---emolumentsTotal----"
						+ emolumentsTotal + "----CPFTotal----" + CPFTotal);
				if (!transid.equals("")) {
					grandTotList.add(transid);
					grandTotList.add(df.format(Math.round(emolumentsTotal)));
					grandTotList.add(df.format(Math.round(CPFTotal)));
					grandTotList.add(df.format(Math.round(PensionTotal)));
					grandTotList.add(df.format(Math.round(PFTotal)));
					grandTotList.add(df.format(Math.round(EmpSubscription)));
					grandTotList.add(df.format(Math.round(AAIContribution)));
					grandTotList.add(df.format(Math.round(adjEmpSubInterest)));
					grandTotList.add(df
							.format(Math.round(adjAAiContriInterest)));
					grandTotList.add(df.format(Math.round(adjPesnionContri)));
					grandTotList.add(df.format(Math.round(adjFSEmpSubIntrst)));
					grandTotList.add(df.format(Math.round(adjFSContriIntrst)));
					 
				}

			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		return grandTotList;
	}

	// By Radha on 27-Feb-2012 for inserting value for form 2 intrst values in
	// epis_adj_crtn_pc_totals_diff
	// By Radha on 06-Jan-2012 for inserting value for AdjPensionContri in
	// current epis_adj_crtn_curnt_pc_totals & in epis_adj_crtn_pc_totals_diff
	// New Method
	public int savepcadjcrtnCurrenttotals(String empserialNO, String adjOBYear,
			String transid, String form2Status, double EmolumentsTot, double cpfTotal,
			double cpfIntrst, double PenContriTotal, double PensionIntrst,
			double PFTotal, double PFIntrst, double EmpSub,
			double EmpSubInterest, double adjEmpIntrst, double AAIContri,
			double AAIContriInterest, double adjAAiContrIntrst,
			double adjPensionContrIntrst, double emolumentsTot_diff,
			double cpfTot_diff, double PenscontriTot_diff, double PfTot_diff,
			double empSubTot_diff, double adjEmpIntrst_diff,
			double AAiContriTot_diff, double adjAAiContrIntrst_diff,
			double adjPensionContrIntrst_diff, double pensionIntrstfrm2,
			double empSubIntrstfrm2, double aaiContriIntrstfrm2) {
		String epis_adj_crtn_curnt_pc_totals_log = "", epis_adj_crtn_pc_totals_diff_log = "",remarksCond="",remarks="";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int result = 0;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			/*
			 * deleteQuery = "delete from epis_adj_crtn_pc_totals where
			 * pensionno="+empSerialNo;
			 * log.info("----savepcadjcrtntotals---deleteQuery "+deleteQuery);
			 * st.executeUpdate(deleteQuery);
			 */
			System.out.println("PenContriTotal=="+PenContriTotal+"==EmpSub=="+EmpSub+"==AAIContri=="+AAIContri);
			log.info("==adjAAiContrIntrst,=====" + adjAAiContrIntrst+"==form2Status==="+form2Status);
			if(form2Status.equals("Y")){
				remarksCond=" and remarks='After Approved'";
				remarks="After Approved";
			}
			String selectADJCurrentPCTotals = "select count(*) as count from epis_adj_crtn_curnt_pc_totals where pensionno='"
					+ empserialNO + "' and  ADJOBYEAR='" + adjOBYear + "'" +remarksCond;
			log
					.info("--savepcadjcrtnCurrenttotals----selectADJCurrentPCTotals--"
							+ selectADJCurrentPCTotals);
			rs = st.executeQuery(selectADJCurrentPCTotals);
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					epis_adj_crtn_curnt_pc_totals_log = "insert into epis_adj_crtn_curnt_pc_totals(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB, EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST ,ADJAAICONTRIINTRST,CREATIONDATE,REMARKS) values('"
							+ empserialNO
							+ "','"
							+ transid
							+ "','"
							+ adjOBYear
							+ "','"
							+ EmolumentsTot
							+ "','"
							+ cpfTotal
							+ "','"
							+ cpfIntrst
							+ "','"
							+ PenContriTotal
							+ "','"
							+ PensionIntrst
							+ "','"
							+ adjPensionContrIntrst
							+ "','"
							+ PFTotal
							+ "','"
							+ PFIntrst
							+ "','"
							+ EmpSub
							+ "','"
							+ EmpSubInterest
							+ "','"
							+ adjEmpIntrst
							+ "','"
							+ AAIContri
							+ "','"
							+ AAIContriInterest
							+ "','"
							+ adjAAiContrIntrst
							+ "', sysdate,'"+remarks+"')";
				} else {

					epis_adj_crtn_curnt_pc_totals_log = "update epis_adj_crtn_curnt_pc_totals  set transid='"
							+ transid
							+ "',EMOLUMENTS='"
							+ EmolumentsTot
							+ "',CPFTOTAL='"
							+ cpfTotal
							+ "',CPFINTEREST='"
							+ cpfIntrst
							+ "',PENSIONTOTAL='"
							+ PenContriTotal
							+ "',PENSIONINTEREST='"
							+ PensionIntrst
							+ "',ADJPENSIONCONTRIINTRST='"
							+ adjPensionContrIntrst
							+ "',PFTOTAL='"
							+ PFTotal
							+ "',PFINTEREST='"
							+ PFIntrst
							+ "',EMPSUB='"
							+ EmpSub
							+ "',EMPSUBINTEREST='"
							+ EmpSubInterest
							+ "',ADJEMPSUBINTRST='"
							+ adjEmpIntrst
							+ "',AAICONTRI='"
							+ AAIContri
							+ "',AAICONTRIINTEREST='"
							+ AAIContriInterest
							+ "',ADJAAICONTRIINTRST='"
							+ adjAAiContrIntrst
							+ "', CREATIONDATE= sysdate	"
							+ "  where  pensionno='"
							+ empserialNO
							+ "' and   ADJOBYEAR='" + adjOBYear + "'";

				}

			}
			String selectADJPCTotalsDiff = "select count(*) as count from epis_adj_crtn_pc_totals_Diff where pensionno='"
					+ empserialNO + "' and  ADJOBYEAR='" + adjOBYear + "'" +remarksCond;
			log.info("--savepcadjcrtnCurrenttotals----selectADJPCTotalsDiff--"
					+ selectADJPCTotalsDiff);
			rs = st.executeQuery(selectADJPCTotalsDiff);
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					epis_adj_crtn_pc_totals_diff_log = "insert into epis_adj_crtn_pc_totals_Diff(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL, PENSIONTOTAL,PENSIONINTRST, PFTOTAL,EMPSUB,EMPSUBINTRST,AAICONTRI ,AAICONTRIINTRST,ADJEMPSUBINTRST  , ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,CREATIONDATE,REMARKS) values('"
							+ empserialNO
							+ "','"
							+ transid
							+ "','"
							+ adjOBYear
							+ "','"
							+ emolumentsTot_diff
							+ "','"
							+ cpfTot_diff
							+ "','"
							+ PenscontriTot_diff
							+ "','"
							+ pensionIntrstfrm2
							+ "','"
							+ PfTot_diff
							+ "','"
							+ empSubTot_diff
							+ "','"
							+ empSubIntrstfrm2
							+ "','"
							+ AAiContriTot_diff
							+ "','"
							+ aaiContriIntrstfrm2
							+ "','"
							+ adjEmpIntrst_diff
							+ "','"
							+ adjAAiContrIntrst_diff
							+ "','"
							+ adjPensionContrIntrst_diff + "',  sysdate,'"+remarks+"')";
				} else {

					epis_adj_crtn_pc_totals_diff_log = "update epis_adj_crtn_pc_totals_Diff  set transid='"
							+ transid
							+ "',EMOLUMENTS='"
							+ emolumentsTot_diff
							+ "',CPFTOTAL='"
							+ cpfTot_diff
							+ "',PENSIONTOTAL='"
							+ PenscontriTot_diff
							+ "',PENSIONINTRST='"
							+ pensionIntrstfrm2
							+ "',PFTOTAL='"
							+ PfTot_diff
							+ "',EMPSUB='"
							+ empSubTot_diff
							+ "',EMPSUBINTRST='"
							+ empSubIntrstfrm2
							+ "',AAICONTRI='"
							+ AAiContriTot_diff
							+ "',AAICONTRIINTRST='"
							+ aaiContriIntrstfrm2
							+ "',ADJEMPSUBINTRST='"
							+ adjEmpIntrst_diff
							+ "',ADJAAICONTRIINTRST='"
							+ adjAAiContrIntrst_diff
							+ "',ADJPENSIONCONTRIINTRST ='"
							+ adjPensionContrIntrst_diff
							+ "', CREATIONDATE= sysdate	"
							+ "  where  pensionno='"
							+ empserialNO
							+ "' and   ADJOBYEAR='" + adjOBYear + "'";

				}

			}
			log.info("----savepcadjcrtnCurrenttotals---Current PC Totals  "
					+ epis_adj_crtn_curnt_pc_totals_log);
			st.executeUpdate(epis_adj_crtn_curnt_pc_totals_log);
			log.info("----savepcadjcrtnCurrenttotals--- PC Totals Diff"
					+ epis_adj_crtn_pc_totals_diff_log);
			st.executeUpdate(epis_adj_crtn_pc_totals_diff_log);

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		return result;
	}
//By Radha On 28-Dec-2012 updated the method with the latest version of the PF Card
	// New Method
	public ArrayList empPFCardReportPrintForAdjCrtn(String range,
			String region, String selectedYear, String empNameFlag,
			String empName, String sortedColumn, String pensionno,
			String lastmonthFlag, String lastmonthYear, String airportcode,
			String bulkFlag) {
		log.info("AdjCrtnDAO::empPFCardReportPrint");
		String fromYear = "", toYear = "", dateOfRetriment = "", transferFlag = "", mappingFlag = "true",crossFinalsettlementFlag="false";
		double additionalContri=0.0,netepf=0.0;	
		Connection con = null;
		if (!selectedYear.equals("Select One")) {
			fromYear = "01-Apr-" + selectedYear;
			int toSelectYear = 0;
			toSelectYear = Integer.parseInt(selectedYear) + 1;
			toYear = "01-Mar-" + toSelectYear;
		} else {
			fromYear = "01-Apr-1995";
			toYear = "31-May-2011";
		}
		int formFrmYear = 0, formToYear = 0, finalSttlementDtYear = 0, formMonthYear = 0;
		try {
			formFrmYear = Integer.parseInt(commonUtil.converDBToAppFormat(
					fromYear, "dd-MMM-yyyy", "yyyy"));
			formToYear = Integer.parseInt(commonUtil.converDBToAppFormat(
					toYear, "dd-MMM-yyyy", "yyyy"));

		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList empDataList = new ArrayList();
		EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
		EmployeeCardReportInfo cardInfo = null;
		ArrayList list = new ArrayList();
		ArrayList ptwList = new ArrayList();
		ArrayList finalSttmentList = new ArrayList();
		ArrayList list1 = new ArrayList();
		ArrayList ptwList1 = new ArrayList();
		ArrayList finalSttmentList1 = new ArrayList();
		String appEmpNameQry = "", finalSettlementDate = "";
		ArrayList cardList = new ArrayList();
		ArrayList addContriList = new ArrayList();
		ArrayList addConList=new ArrayList();
		int arrerMonths = 0;
		boolean finalStFlag = false,isFrozenAvail=false;
		try {
			con = DBUtils.getConnection();
			empDataList = commonDAO.getEmployeePFInfoPrinting(con, range,
					region, empNameFlag, empName, sortedColumn, pensionno,
					lastmonthFlag, lastmonthYear, airportcode, fromYear,
					toYear, bulkFlag);

			for (int i = 0; i < empDataList.size(); i++) {
				cardInfo = new EmployeeCardReportInfo();
				personalInfo = (EmployeePersonalInfo) empDataList.get(i);
				try {
					dateOfRetriment = commonDAO.getRetriedDate(personalInfo
							.getDateOfBirth());
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}
				log
						.info("FincialReportDAO:::empPFCardReportPrint:Final Settlement Date"
								+ personalInfo.getFinalSettlementDate()
								+ "Resttlement Date"
								+ personalInfo.getResttlmentDate());
				if (!personalInfo.getFinalSettlementDate().equals("---")) {
					finalSttlementDtYear = Integer.parseInt(commonUtil
							.converDBToAppFormat(personalInfo
									.getFinalSettlementDate(), "dd-MMM-yyyy",
									"yyyy"));
					formMonthYear = Integer.parseInt(commonUtil
							.converDBToAppFormat(personalInfo
									.getFinalSettlementDate(), "dd-MMM-yyyy",
									"MM"));

					if (finalSttlementDtYear <= formFrmYear) {
						finalStFlag = true;
						log.info("finalSttlementDtYear<= formFrmYear");
					} else if (formToYear == finalSttlementDtYear
							&& formMonthYear < 4) {
						finalStFlag = true;
						log
								.info("formToYear == finalSttlementDtYear&& formMonthYear < 4");
					} else {
						finalStFlag = false;
						log.info("con2 else");
					}

					if (finalStFlag == true) {

						finalSettlementDate = commonUtil.converDBToAppFormat(
								personalInfo.getFinalSettlementDate(),
								"dd-MMM-yyyy", "MMM-yyyy");
					} else {
						personalInfo.setFinalSettlementDate("---");
						finalSettlementDate = "---";
					}

				} else {
					finalSettlementDate = "---";
				}
				
				isFrozenAvail=personalInfo.isFrozedSeperationAvail();
				if (personalInfo.isFrozedSeperationAvail()==true && personalInfo.getChkPaymntSprtinDtFlag().equals("N")){
					isFrozenAvail=false;
					if (personalInfo.getFrozedSeperationInterest()!=0){
						finalSettlementDate=commonUtil.converDBToAppFormat(
								personalInfo.getFrozedSeperationDate(),
								"dd-MMM-yyyy", "MMM-yyyy");
					}
				}

				
				log.info("Before ProcessingAdjOB formFrmYear" + formFrmYear
						+ "formToYear" + formToYear + "finalSttlementDtYear"
						+ finalSttlementDtYear + "finalStFlag" + finalStFlag
						+ "formMonthYear" + formMonthYear
						+ "Final Settlement Date" + finalSettlementDate);
				log.info("PFCARD_ADDITIONALCONTRIYEAR111111111111111111111=========="+selectedYear);
				if(fromYear.equals(Constants.PFCARD_ADDITIONALCONTRIYEAR) ) {
					log.info("PFCARD_ADDITIONALCONTRIYEAR=========="+selectedYear);
					//cardInfo.getAdditionalContri();
					addContriList=this.pfCardAdditionalContriCalculationForAdj(personalInfo.getOldPensionNo());					
				}
				// ProcessforAdjOb(personalInfo.getPensionNo(), true);
				list = this.getEmployeePensionCardForAdjCrtn(con, fromYear,
						toYear, personalInfo.getPfIDString(), personalInfo
								.getWetherOption(), personalInfo.getRegion(),
						true, dateOfRetriment, personalInfo.getDateOfBirth(),
						personalInfo.getOldPensionNo(), finalSettlementDate,
						personalInfo.getResttlmentDate(),isFrozenAvail);
				if(fromYear.equals("01-Apr-2012")){
					list1 = this.getEmployeePensionCardForAdjCrtn(con, "01-Apr-2013", "30-Apr-2013",
							personalInfo.getPfIDString(), personalInfo
									.getWetherOption(), personalInfo.getRegion(),
							true, dateOfRetriment, personalInfo.getDateOfBirth(),
							personalInfo.getOldPensionNo(), finalSettlementDate,
							personalInfo.getResttlmentDate(),isFrozenAvail);
				//	ptwList1 = this.getPTWDetails(con, "01-Apr-2013", "30-Apr-2013",
						//	personalInfo.getCpfAccno(), personalInfo
								//	.getOldPensionNo());
					finalSttmentList1 = commonDAO.getFinalSettlement(con, "01-Apr-2013", "30-Apr-2013", personalInfo.getPfIDString(), 
							personalInfo.getOldPensionNo(), personalInfo
									.getFinalSettlementDate(), personalInfo
									.getResttlmentDate(),personalInfo.isCrossfinyear(),personalInfo.isFrozedSeperationAvail(),personalInfo.getFrozedSeperationDate(),personalInfo.getChkPaymntSprtinDtFlag());
					
				}
				// Flag is not used in the last paramter of getPTWDetails method

				/*
				 * ptwList = this.getPTWDetails(con, fromYear, toYear,
				 * personalInfo.getCpfAccno(), personalInfo .getOldPensionNo());
				 */

				finalSttmentList = commonDAO.getFinalSettlement(con, fromYear,
						toYear, personalInfo.getPfIDString(), personalInfo
								.getOldPensionNo(), personalInfo
								.getFinalSettlementDate(), personalInfo
								.getResttlmentDate(), personalInfo
								.isCrossfinyear(),false,personalInfo.getSeperationDate(),personalInfo.getChkPaymntSprtinDtFlag());
				log.info("PF Card====================Final Settlement Date"
						+ personalInfo.getFinalSettlementDate());


				if(finalSttmentList.size()!=0){
					if(personalInfo.getFinalSettlementDate().equals("31-Mar-2011") && finalSttmentList.get(11).equals("01-Apr-2011")){
						crossFinalsettlementFlag ="true";
					}
				}

				if (!personalInfo.getFinalSettlementDate().equals("---")) {
					arrerMonths = commonDAO.getArrearInfo(con, fromYear, toYear,
							personalInfo.getOldPensionNo());
					log.info("Arrear+FinalSettlement=========="+finalSettlementDate+"Comparing Condition"
							+ commonUtil.compareTwoDates(finalSettlementDate,
									commonUtil.converDBToAppFormat(fromYear,
											"dd-MMM-yyyy", "MMM-yyyy"))+"arrerMonths"+arrerMonths);
					if (commonUtil.compareTwoDates(finalSettlementDate,
							commonUtil.converDBToAppFormat(fromYear,
									"dd-MMM-yyyy", "MMM-yyyy")) == true) {
						// Final settlement date is lower than fromYear
						personalInfo.setChkArrearFlag("N");
						if (!personalInfo.getResttlmentDate().equals("---")) {
							log.info("Non-Blank Restlement Case");
							
							cardInfo.setInterNoOfMonths(12);
							cardInfo.setNoOfMonths(commonDAO
									.numOfMnthFinalSettlement(commonUtil
											.converDBToAppFormat(personalInfo
													.getResttlmentDate(),
													"dd-MMM-yyyy", "MMM")));
						} else if (arrerMonths != 0) {
							log.info("Blank Restlement Case and having arrear datata");
							cardInfo.setInterNoOfMonths(12);
							
							if (commonUtil.getDateDifference("01-Apr-2009",
									fromYear) >= 0) {
								personalInfo.setChkArrearFlag("Y");
							   

								cardInfo.setNoOfMonths(commonDAO
										.getNoOfMonthsForPFID(fromYear, toYear));
							} else {
								cardInfo.setNoOfMonths(12);
							}

						} else {
							log.info("ELSE CONDITON Blank Restlement Case --III");
							cardInfo.setInterNoOfMonths(12);
							cardInfo.setNoOfMonths(commonDAO
									.getNoOfMonthsForPFID(fromYear, toYear));

						}

					} else {
						if (formFrmYear <2011) {
							personalInfo.setChkArrearFlag("Y");
						}else{
							personalInfo.setChkArrearFlag("N");
							
						}
						log.info("Else Condtion compareTwoDates ChkArrearFlag"
								+ personalInfo.getChkArrearFlag());
						cardInfo.setNoOfMonths(commonDAO
								.numOfMnthFinalSettlement(commonUtil
										.converDBToAppFormat(personalInfo
												.getFinalSettlementDate(),
												"dd-MMM-yyyy", "MMM")));
						cardInfo.setArrearNoOfMonths(arrerMonths);
						cardInfo.setInterNoOfMonths(12);
					}

				} else {
					personalInfo.setChkArrearFlag("N");
					log.info("ChkArrearFlag" + personalInfo.getChkArrearFlag());
					cardInfo.setNoOfMonths(commonDAO.getNoOfMonthsForPFID(fromYear, toYear));
					cardInfo.setInterNoOfMonths(12);
				}
				log.info("personalInfo.isFrozedSeperationAvail()"+personalInfo.isFrozedSeperationAvail()+"ChkPaymntSprtinDt"+personalInfo.getChkPaymntSprtinDtFlag());
				if (personalInfo.isFrozedSeperationAvail()==true && personalInfo.getChkPaymntSprtinDtFlag().equals("N")){
					//personalInfo.setChkArrearFlag("Y");
					log.info("personalInfo.isFrozedSeperationAvail()=getFrozedSeperationInterest"+personalInfo.getFrozedSeperationInterest());
					cardInfo.setArrearNoOfMonths(personalInfo.getFrozedSeperationArrearInt());
					cardInfo.setInterNoOfMonths(personalInfo.getFrozedSeperationInterest());
					cardInfo.setNoOfMonths(personalInfo.getFrozedSeperationInterest());
				}
				log.info("PF Card====Final Settlement Date"
						+ personalInfo.getFinalSettlementDate()
						+ "Resettlement Date"
						+ personalInfo.getResttlmentDate() + "fromYear"
						+ fromYear + "NO.Of Months" + cardInfo.getNoOfMonths()
						+ "arrerMonths======" + arrerMonths);
				cardInfo.setFinalSettmentList(finalSttmentList);
				cardInfo.setArrearInfo(commonDAO.getArrearData(con, fromYear,
						toYear, personalInfo.getOldPensionNo()));
				if (finalSttmentList.size() != 0) {
					cardInfo.setOrderInfo(commonDAO.getSanctionOrderInfo(con,
							fromYear, toYear, personalInfo.getOldPensionNo()));
				} else {
					cardInfo.setOrderInfo("");
				}
				if(fromYear.equals("01-Apr-2012")){
					cardInfo.setPensionCardList1(list1);				 
					cardInfo.setPtwList1(ptwList1);
				}
				cardInfo.setPersonalInfo(personalInfo);
				cardInfo.setPensionCardList(list);		
				cardInfo.setAddContriList(addContriList);
				//cardInfo.setPtwList(ptwList);
				cardList.add(cardInfo);
			}

		} catch (Exception se) {
			log.printStackTrace(se);
		} finally {
			commonDB.closeConnection(con, null, null);
		}

		return cardList;
	}
//For Getting pfcardnarration details to show in impact calc as per sehgal request on 17-Dec-2012
	private ArrayList getEmployeePensionCardForAdjCrtn(Connection con,
			String fromDate, String toDate, String pfIDS, String wetherOption,
			String region, boolean formFlag, String dateOfRetriment,
			String dateOfBirth, String pensionNo, String finalSettlmentMonth, String resttlementdate,boolean isFrozenAvail) {

		DecimalFormat df = new DecimalFormat("#########0.00");

		Statement st = null;
		ResultSet rs = null;
		EmployeePensionCardInfo cardInfo = null;
		ArrayList pensionList = new ArrayList();
		boolean flag = false;
		boolean contrFlag = false, chkDOBFlag = false,  rareCaseflag = false, finalSettlementFlag = false, yearBreakMonthFlag = false, dataAfterFinalsettlemnt = false,resettlementFlag= false;
		boolean arrearsFlag = false;
		String checkDate = "", chkMnthYear = "Apr-1995", emolumentsMonths = "1", arrearFlag = "N";
		String monthYear = "", days = "", getMonth = "", sqlQuery = "", findFromYear = "", findToYear = "", finalSettlmentYear = "";
		int getDaysBymonth = 0, monthsCntAfterFinstlmnt = 0;
		long transMntYear = 0, empRetriedDt = 0;
		log.info("checkDate==" + checkDate + "flag===" + flag);
		double totalAdvancePFWPaid = 0, loanPFWPaid = 0, advancePFWPaid = 0, empNet = 0, aaiNet = 0, advPFDrawn = 0, empCumlative = 0.0, arrearEmpCumlative = 0.0, arrearAaiNetCumlative = 0, aaiPF = 0.0, aaiNetCumlative = 0.0, grandEmpCumlative = 0.0, grandAaiCumlative = 0.0, grandArrearEmpCumlative = 0.0, grandArrearAaiCumlative = 0.0;
		double pensionAsPerOption = 0.0, pensionContriTot = 0.0, pensionContriArrearTot = 0.0;
		double totalAdvances = 0;
		double additionalContri=0.0,netepf=0.0;
		boolean obFlag = false;
		boolean loanAdvFlag = false;
		ArrayList addConList=new ArrayList();
		try {
			findFromYear = commonUtil.converDBToAppFormat(fromDate,
					"dd-MMM-yyyy", "yyyy");
			findToYear = commonUtil.converDBToAppFormat(toDate, "dd-MMM-yyyy",
					"yyyy");
			if (!finalSettlmentMonth.equals("---")) {
				finalSettlmentYear = commonUtil.converDBToAppFormat(
						finalSettlmentMonth, "MMM-yyyy", "yyyy");
			}

		} catch (InvalidDataException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (Integer.parseInt(findFromYear) >= 2008) {
			yearBreakMonthFlag = true;
			pfIDS = "";
			if (Integer.parseInt(findFromYear) >= 2013 && Integer.parseInt(findToYear) > 2013 ) {
				loanAdvFlag=true;
			sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-add_months(to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
					+ "SUBSTR(empdt.MONYEAR, 4, 4))),1))),-1,0,1,1) as signs,emp.* from (select distinct to_char(to_date('"
					+ fromDate
					+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
					+ "from epis_adj_crtn where rownum <=to_date('"
					+ toDate
					+ "', 'dd-mon-yyyy') -to_date('"
					+ fromDate
					+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT add_months(MONTHYEAR,-1) as MONTHYEAR,to_char(add_months(MONTHYEAR,-1), 'MONYYYY') empmonyear,cpfaccno as CPFACCNO,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
					+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST," 
					+"ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION,additionalcontri FROM epis_adj_crtn  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= add_months('"
					+ fromDate
					+ "',1) and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=add_months(last_day('"
					+ toDate
					+ "'),1))"
					+ " AND PENSIONNO="
					+ pensionNo
					+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
			}else{
			sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
				+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs ,emp.* from (select distinct to_char(to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
				+ "from epis_adj_crtn where rownum <=to_date('"
				+ toDate
				+ "', 'dd-mon-yyyy') -to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO ,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
				+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,"
				+ "ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION,additionalcontri FROM epis_adj_crtn  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
				+ fromDate
				+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
				+ toDate
				+ "'))"
				+ " AND PENSIONNO="
				+ pensionNo
				+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
			}
		}/* else {
			yearBreakMonthFlag = false;
			sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
					+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs,emp.* from (select distinct to_char(to_date('"
					+ fromDate
					+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
					+ "from epis_adj_crtn where rownum <=to_date('"
					+ toDate
					+ "', 'dd-mon-yyyy') -to_date('"
					+ fromDate
					+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
					+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
					+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS,PFCARDNARRATION,SUPPLIFLAG,PFCARDNRFLAG FROM EMPLOYEE_PENSION_VALIDATE  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
					+ fromDate
					+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
					+ toDate
					+ "'))"
					+ " AND ("
					+ pfIDS
					+ ")) emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
		}
		sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
				+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs ,emp.* from (select distinct to_char(to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
				+ "from epis_adj_crtn where rownum <=to_date('"
				+ toDate
				+ "', 'dd-mon-yyyy') -to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO ,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
				+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,"
				+ "ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION FROM epis_adj_crtn  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
				+ fromDate
				+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
				+ toDate
				+ "'))"
				+ " AND PENSIONNO="
				+ pensionNo
				+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";*/

		log.info("FinanceReportDAO::getEmployeePensionCardForAdjCrtn"
				+ sqlQuery);
		ArrayList OBList = new ArrayList();
		try {

			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);

			while (rs.next()) {
				cardInfo = new EmployeePensionCardInfo();
				double total = 0.0;
				if (rs.getString("MONTHYEAR") != null) {
					cardInfo.setMonthyear(commonUtil.getDatetoString(rs
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
					OBList = this.getOBForAdjCrtn(con, cardInfo.getMonthyear(),
							pensionNo);
					cardInfo.setObList(OBList);
					cardInfo.setObFlag("Y");
					obFlag = true;
					getMonth = "";
				} else {
					cardInfo.setObFlag("N");
				}
				if (rs.getString("CPFACCNO") != null) {
					cardInfo.setCpfAccno(rs.getString("CPFACCNO"));
				} else {
					cardInfo.setCpfAccno("");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					cardInfo.setEmoluments(rs.getString("EMOLUMENTS"));
				} else {
					cardInfo.setEmoluments("0");
				}
				if (rs.getString("arrearflag") != null) {
					arrearFlag = rs.getString("arrearflag");
				} else {
					arrearFlag = "N";
				}
				cardInfo.setTransArrearFlag(arrearFlag);
				if (arrearFlag.equals("Y") && arrearsFlag == false) {
					arrearsFlag = true;
				}

				log.info("============================MonthYear"
						+ cardInfo.getMonthyear() + "Emoluments"
						+ cardInfo.getEmoluments() + "finalSettlmentMonth"
						+ finalSettlmentMonth + "arrearFlag" + arrearFlag
						+ "arrearsFlag" + arrearsFlag);
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
				/*
				 * if (rs.getString("CPF") != null) {
				 * cardInfo.setEmpCPF(rs.getString("CPF")); } else {
				 * cardInfo.setEmpCPF("0"); }
				 */
				if (rs.getString("SUPPLIFLAG") != null) {
					cardInfo.setSupflag(rs.getString("SUPPLIFLAG"));
				} else {
					cardInfo.setSupflag("N");
				}
				
//				To Disply PFCARDNARRATION irrespective of arrears,Suppli or Merge Data
				if (rs.getString("PFCARDNRFLAG") != null) {
					cardInfo.setPfcardNarrationFlag(rs.getString("PFCARDNRFLAG"));
				} else {
					cardInfo.setPfcardNarrationFlag("N");
				}
				
				/*
				 * if (region.equals("CHQNAD")) {
				 * 
				 * if (rs.getString("CPFADVANCE") != null) {
				 * cardInfo.setPrincipal(rs.getString("CPFADVANCE")); } else {
				 * cardInfo.setPrincipal("0"); } } else if (region.equals("North
				 * Region")) { if (rs.getString("REFADV") != null) {
				 * cardInfo.setPrincipal(rs.getString("REFADV")); } else {
				 * cardInfo.setPrincipal("0"); } }
				 */{
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
				if (rs.getString("ADVANCE") != null) {
					cardInfo.setAdvancesAmount(rs.getString("ADVANCE"));
				} else {
					cardInfo.setAdvancesAmount("0");
				}
				if (rs.getString("PFWSUB") != null) {
					cardInfo.setPFWSubscri(rs.getString("PFWSUB"));
				} else {
					cardInfo.setPFWSubscri("0");
				}
				if (rs.getString("PFWCONTRI") != null) {
					cardInfo.setPFWContri(rs.getString("PFWCONTRI"));
				} else {
					cardInfo.setPFWContri("0");
				}
				loanPFWPaid = Double.parseDouble(cardInfo.getAdvancesAmount());
				advancePFWPaid = Double.parseDouble(cardInfo.getPFWSubscri());

				totalAdvancePFWPaid = loanPFWPaid + advancePFWPaid;

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
				System.out.println("findFromYear000=="+Integer.parseInt(findFromYear));
				/*if(Integer.parseInt(findFromYear)>=2015) {								
					System.out.println("findFromYear=="+Integer.parseInt(findFromYear));
					//additionalcalculation.additionalContributionCalculation(con, cardInfo.getEmoluments(),pensionNo,cardInfo.getMonthyear(),
					//		dateOfRetriment,dateOfBirth);
					
					if(rs.getString("additionalcontri")!=null) {
						//additionalContri=commonDAO.additionalContriCalculation(cardInfo.getMonthyear(),cardInfo.getEmoluments());
						cardInfo.setAdditionalContri(rs.getString("additionalcontri"));
					}
					else {
						cardInfo.setAdditionalContri("0.0");
					}
					if( Date.parse(cardInfo.getMonthyear())<Date.parse("01-Sep-2014") &&
							Date.parse(cardInfo.getMonthyear())>=Date.parse("01-Apr-2014")) {
						cardInfo.setAdditionalContri("0.0");						
					}
					additionalContri=Double.parseDouble(cardInfo.getAdditionalContri());
			//if(cardInfo.getMonthyear().equals("01-Apr-2015")) {
					//	addConList=commonDAO.pfCardAdditionalContriCalculation(pensionNo);
					//	additionalContri=Double.parseDouble(addConList.get(8).toString());
				//	}
					
					netepf=Double.parseDouble(cardInfo.getEmppfstatury())-additionalContri;					
					cardInfo.setNetEPF(Double.toString(netepf));

					total = new Double(df.format(Double.parseDouble(cardInfo
							.getNetEPF().trim())
							+ Double.parseDouble(cardInfo.getEmpvpf().trim())
							+ Double.parseDouble(cardInfo.getPrincipal().trim())
							+ Double.parseDouble(cardInfo.getInterest().trim())))
							.doubleValue();
					System.out.println("netepf=="+netepf);
					System.out.println("additionalContri=="+additionalContri);
					
					System.out.println("total=="+total);
				}*/
				System.out.println("additionalcontri=="+rs.getString("additionalcontri"));
				if(rs.getString("additionalcontri")!=null) {
					//additionalContri=commonDAO.additionalContriCalculation(cardInfo.getMonthyear(),cardInfo.getEmoluments());
					cardInfo.setAdditionalContri(rs.getString("additionalcontri"));
				}
				else {
					cardInfo.setAdditionalContri("0.0");
				}
				netepf=Double.parseDouble(cardInfo.getEmppfstatury())-additionalContri;					
				cardInfo.setNetEPF(Double.toString(netepf));
				System.out.println("netepf=="+netepf);
				
				/*
				 * if (formFlag == true) { loanPFWPaid =
				 * this.getEmployeeLoans(con, cardInfo .getShnMnthYear(), pfIDS,
				 * "ADV.PAID", pensionNo); advancePFWPaid =
				 * this.getEmployeeAdvances(con, cardInfo .getShnMnthYear(),
				 * pfIDS, "ADV.PAID", pensionNo); log.info("Region" + region +
				 * "loanPFWPaid" + loanPFWPaid + "advancePFWPaid" +
				 * advancePFWPaid); totalAdvancePFWPaid = loanPFWPaid +
				 * advancePFWPaid; }
				 * 
				 * cardInfo.setAdvancesAmount(Double.toString(Math
				 * .round(advancePFWPaid)));
				 */

				log.info("findFromYear======================" + findFromYear
						+ "finalSettlmentYear" + finalSettlmentYear
						+ "findToYear" + findToYear);

				if (!finalSettlmentMonth.equals("---")) {
					if (commonDAO.compareFinalSettlementDates(fromDate, "31-Mar-"
							+ findToYear, "02-" + finalSettlmentMonth) == true) {

						finalSettlementFlag = commonUtil.compareTwoDates(
								finalSettlmentMonth, checkDate);
					} else {
						/*
						 * if(cardInfo.getTransArrearFlag().equals("Y")&&
						 * commonUtil.getDateDifference("01-Apr-2010",fromDate)>=0){
						 * finalSettlementFlag = true; }else{
						 * finalSettlementFlag = false; }
						 */

						if (commonUtil.compareTwoDates(commonUtil
								.converDBToAppFormat("01-Mar-2011",
										"dd-MMM-yyyy", "MMM-yyyy"), commonUtil
								.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
										"MMM-yyyy")) == true) {
							if (finalSettlmentMonth.equals("Mar-"
									+ findFromYear)) {
								if (commonUtil.getDifferenceTwoDatesInDays(
										"31-" + finalSettlmentMonth, cardInfo
												.getMonthyear()) > -30) {
									rareCaseflag = true;
								}
							}
						}
						if (rareCaseflag == true) {
							finalSettlementFlag = true;
						} else {
							finalSettlementFlag = false;
						}

					}

				} else {

					finalSettlementFlag = false;
				}
				
				
				
				// code for retriving data after finalsettlement

				if (finalSettlementFlag == true) {
					dataAfterFinalsettlemnt = true;
					monthsCntAfterFinstlmnt++;
				}
				
				log.info("Two dates informaiton"+commonUtil.compareTwoDates(commonUtil.converDBToAppFormat(
						"01-Mar-2010", "dd-MMM-yyyy", "MMM-yyyy"), commonUtil
						.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
								"MMM-yyyy")));
				if (commonUtil.compareTwoDates(commonUtil.converDBToAppFormat(
						"01-Mar-2010", "dd-MMM-yyyy", "MMM-yyyy"), commonUtil
						.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
								"MMM-yyyy")) == true
						&& !finalSettlmentMonth.equals("---")) {

					if (!(resttlementdate.equals("---"))) {
						if (commonDAO.compareFinalSettlementDates(fromDate,
								"31-Mar-" + findToYear, "02-"
										+ commonUtil.converDBToAppFormat(
												resttlementdate, "dd-MMM-yyyy",
												"MMM-yyyy")) == true) {
							resettlementFlag = commonUtil.compareTwoDates(
									commonUtil.converDBToAppFormat(
											resttlementdate, "dd-MMM-yyyy",
											"MMM-yyyy"), checkDate);
						}
					}
					if (resettlementFlag == true) {
						finalSettlementFlag = true;
						arrearsFlag = false;

					}

				}
				if (isFrozenAvail==true && arrearFlag.equals("Y")){
					//arrearsFlag=true;
					cardInfo.setTransArrearFlag("Y");
				}
				log.info("finalSettlementFlag======================"
						+ finalSettlementFlag + "checkDate" + checkDate
						+ "resettlementFlag" + resettlementFlag
						+ "finalSettlmentMonth" + finalSettlmentMonth
						+ "rareCaseflag" + rareCaseflag + "finalSettlementFlag"
						+ finalSettlementFlag+"isFrozenAvail"+isFrozenAvail);
				
				empNet = total - totalAdvancePFWPaid;
				rareCaseflag = false;
				resettlementFlag = false;
				
				if (arrearsFlag == true) {
					arrearEmpCumlative = arrearEmpCumlative + empNet;

					cardInfo.setArrearEmpNetCummulative(Double
							.toString(arrearEmpCumlative));

					grandArrearEmpCumlative = grandArrearEmpCumlative
							+ arrearEmpCumlative;

					cardInfo.setGrandArrearEmpNetCummulative(Double
							.toString(Math.round(grandArrearEmpCumlative)));
				}
				cardInfo.setEmptotal(Double.toString(Math.round(total)));
				cardInfo.setAdvancePFWPaid(Double.toString(Math
						.round(totalAdvancePFWPaid)));
				cardInfo.setEmpNet((Double.toString(Math.round(empNet))));

				// code for retriving empNet data after finalsettlement
				if (dataAfterFinalsettlemnt == true) {
					cardInfo.setDataAfterFinalsettlemnt(String
							.valueOf(dataAfterFinalsettlemnt));
					cardInfo.setAftrFinstlmntEmpNetTot(Double.toString(Math
							.round(empNet)));
				}

				if (finalSettlementFlag == true) {
					empNet = 0;
				}
				empCumlative = empCumlative + empNet;
				
				if(cardInfo.getMonthyear().equals("01-Mar-2014") || cardInfo.getMonthyear().equals("01-Mar-2015") || cardInfo.getMonthyear().equals("01-Mar-2016")){
					finalSettlementFlag=true;
				}
				if (finalSettlementFlag == false) {
					if (Integer.parseInt(findFromYear) >= 2011) {
						cardInfo.setYearFlag(true);
						if (rs.getInt("signs") == 1) {

							grandEmpCumlative = grandEmpCumlative + empNet;
							cardInfo.setGrandCummulative(Double.toString(Math
									.round(grandEmpCumlative)));
						} else {
							cardInfo.setGrandCummulative(Double.toString(Math
									.round(0)));
						}

					} else {
						grandEmpCumlative = grandEmpCumlative + empCumlative;
						cardInfo.setGrandCummulative(Double.toString(Math
								.round(grandEmpCumlative)));
					}

				}

				cardInfo.setEmpNetCummulative(Double.toString(empCumlative));
				/*
				 * (if (rs.getString("AAICONPF") != null) {
				 * cardInfo.setAaiPF(rs.getString("AAICONPF")); } else {
				 * cardInfo.setAaiPF("0"); }
				 */
				if (rs.getString("emolumentmonths") != null) {
					emolumentsMonths = rs.getString("emolumentmonths");
				} else {
					emolumentsMonths = "1";
				}
				cardInfo.setEmolumentMonths(emolumentsMonths);

				pensionAsPerOption = rs.getDouble("PENSIONCONTRI");
				cardInfo.setPensionContribution(Double
						.toString(pensionAsPerOption));

				log.info("flag" + flag + checkDate + "Pension"
						+ cardInfo.getPensionContribution());
				if (formFlag == true) {
					advPFDrawn = commonDAO.getEmployeeLoans(con, cardInfo
							.getShnMnthYear(), pfIDS, "ADV.DRAWN", pensionNo);
				}

				aaiPF = rs.getDouble("PF");

				cardInfo.setAaiPF(Double.toString(aaiPF));
				cardInfo.setPfDrawn(Double.toString(advPFDrawn));
				aaiNet = Double.parseDouble(cardInfo.getAaiPF()) - advPFDrawn;
				log.info("aaiPF=======================================" + aaiPF
						+ "advPFDrawn" + advPFDrawn + "aaiNet" + aaiNet);
				cardInfo.setAaiNet(Double.toString(aaiNet));
				if (arrearsFlag == true) {
					arrearAaiNetCumlative = arrearAaiNetCumlative + aaiNet;
					cardInfo.setArrearAaiCummulative(Double
							.toString(arrearAaiNetCumlative));
					grandArrearAaiCumlative = grandArrearAaiCumlative
							+ arrearAaiNetCumlative;
					cardInfo.setGrandArrearAAICummulative(Double
							.toString(grandArrearAaiCumlative));
				}

				// code for retriving AAINet data after finalsettlement
				if (dataAfterFinalsettlemnt == true) {
					cardInfo.setDataAfterFinalsettlemnt(String
							.valueOf(dataAfterFinalsettlemnt));
					cardInfo.setAftrFinstlmntAAINetTot(Double.toString(Math
							.round(aaiNet)));
				}

				if (finalSettlementFlag == true) {
					aaiNet = 0;
				}

				aaiNetCumlative = aaiNetCumlative + aaiNet;

				cardInfo.setAaiCummulative(Double.toString(aaiNetCumlative));				 
				if (finalSettlementFlag == false) {
					if (Integer.parseInt(findFromYear) >= 2011) {
						cardInfo.setYearFlag(true);
						if (rs.getInt("signs") == 1) {
							grandAaiCumlative = grandAaiCumlative + aaiNet;
							cardInfo.setGrandAAICummulative(Double
									.toString(Math.round(grandAaiCumlative)));
						} else {
							cardInfo.setGrandAAICummulative(Double
									.toString(Math.round(0)));
						}
					} else {
						grandAaiCumlative = grandAaiCumlative + aaiNetCumlative;
						cardInfo.setGrandAAICummulative(Double.toString(Math
								.round(grandAaiCumlative)));
					}

				}
				
				// code for retriving PensionContri data after finalsettlement
				if (dataAfterFinalsettlemnt == true) {
					cardInfo.setDataAfterFinalsettlemnt(String
							.valueOf(dataAfterFinalsettlemnt));
					cardInfo.setAftrFinstlmntPCNetTot(Double.toString(Math
							.round(pensionAsPerOption)));
				}

				// pensionContriTot is the total of pc values except arrear
				if (finalSettlementFlag == false) {
					cardInfo.setPensionContriAmnt(Double.toString(Math
							.round(pensionAsPerOption)));
				} else {
					cardInfo.setPensionContriAmnt("0");
				}
				log.info("======pensionContriTot========" + pensionAsPerOption
						+ "=======" + cardInfo.getPensionContriAmnt());
				if (finalSettlementFlag == true) {
					pensionContriTot = 0;
				}
				if (arrearsFlag == true) {

					cardInfo.setPensionContriArrearAmnt(Double.toString(Math
							.round(pensionAsPerOption)));
				}

				if (rs.getString("AIRPORTCODE") != null) {
					cardInfo.setStation(rs.getString("AIRPORTCODE"));
				} else {
					cardInfo.setStation("---");
				}

				if (rs.getString("MERGEFLAG") != null) {
					cardInfo.setMergerflag(rs.getString("MERGEFLAG"));
				} else {
					cardInfo.setMergerflag("N");
				}
				if (rs.getString("region") != null) {
					cardInfo.setRegion(rs.getString("region"));
				} else {
					cardInfo.setRegion("");
				}
				if (cardInfo.getMergerflag().equals("Y")) {
					if (rs.getString("REMARKS") != null) {
						cardInfo.setMergerremarks(rs.getString("REMARKS"));
					} else {
						cardInfo.setMergerremarks("---");
					}
				} else {
					cardInfo.setMergerremarks("---");
				}
				 
				if (rs.getString("PFCARDNARRATION") != null) {
					cardInfo
							.setPfcardNarration(rs.getString("PFCARDNARRATION"));
				} else {
					cardInfo.setPfcardNarration("---");
				}
				
				log.info("=====PFCARDNARRATION===="+cardInfo.getPfcardNarration());
				finalSettlementFlag = false;
				pensionList.add(cardInfo);
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return pensionList;
	}
	//By Radha on 22-Mar-2012 for implementing the changes regarding orignal PFCArd 
	public ArrayList getOBForAdjCrtn(Connection con, String monthYear,
			String pensionNo) {
		ArrayList list = new ArrayList();
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", obYear = "", obFlag = "", tempMonthYear = "",calsepflag="N",obSepDate="",message="";
		long empNetOB = 0, aaiNetOB = 0, pensionOB = 0, outstandOB = 0,calempnetob=0,calaainetob=0,calempnetintob=0,calaainetintob=0,CALCNETEMPREMINT=0,CALCNETAAICONTREMINT=0,CALCNETEMPREMINT1=0,CALCNETAAICONTREMINT1=0;
		tempMonthYear = "%" + monthYear.substring(2, monthYear.length());
					
		sqlQuery = "SELECT EMPNETOB,AAINETOB,PENSIONOB,CALEMPNETOB,CALAAINETOB,CALEMPNETOBINTAMT,CALAAINETOBINTAMT,CALINTMNTHS,CALSEPFLAG,CALCNETEMPREMINT,CALCNETAAICONTREMINT,CALCNETEMPREMINT1,CALCNETAAICONTREMINT1,to_char(last_day(add_months(OBYEAR,CALINTMNTHS-1)),'dd-Mon-yyyy') as ADJSEPDT,OBFLAG,TO_CHAR(OBYEAR,'yyyy') AS YEAR,OUTSTANDADV,re_req FROM EMPLOYEE_PENSION_OB WHERE PENSIONNO="
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
				
			    CALCNETEMPREMINT=rs.getLong("CALCNETEMPREMINT");
				
				CALCNETAAICONTREMINT=rs.getLong("CALCNETAAICONTREMINT");
				
				CALCNETEMPREMINT1=rs.getLong("CALCNETEMPREMINT1");
				
				CALCNETAAICONTREMINT1=rs.getLong("CALCNETAAICONTREMINT1");
				
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
			//rem int fetch from db exp:2940
			list.add(new Long(CALCNETEMPREMINT));
			list.add(new Long(CALCNETAAICONTREMINT));
			list.add(new Long(CALCNETEMPREMINT1));
			list.add(new Long(CALCNETAAICONTREMINT1));
			//list.add(remarkflag);
			log.info("Size of Opening adj1111Balances" + list.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
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
			System.out.println("sqlQuery====="+sqlQuery);
			rs = st.executeQuery(sqlQuery);
			log.info("getAdjOBForPFCardReport===========AdjcrtnDao========sqlQuery==="
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
			System.out.println("sizee====="+list.size());
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		return list;
	}

	private String getEmployeePensionInfoForAdjCrtnForUserRequired(
			String cpfString, String fromDate, String toDate,
			String whetherOption, String dateOfRetriment, String dateOfBirth,
			String Pensionno, String batchid) {

		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		String tablename = "EPIS_ADJ_CRTN";

		log.info("formdate " + fromDate + "todate " + toDate);
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from epis_info_adj_crtn where ("
				+ tempCpfString + ") and rownum=1)";
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.ADVANCE AS ADVANCE,emp.PFWSUB AS PFWSUB,emp.PFWCONTRI AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI, DataModifiedFlag as DataModifiedFlag from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from  employee_pension_validate  "
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,NVL(ADVANCE,0.00) AS ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,DataModifiedFlag FROM "
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
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,nvl(emp.ADVANCE,0.00) AS ADVANCE,nvl(emp.PFWSUB,0.00) AS PFWSUB,nvl(emp.PFWCONTRI,0.00) AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI, emp.DataModifiedFlag as DataModifiedFlag  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,nvl(ADVANCE,0.00) as ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,DataModifiedFlag "
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
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('"
				+ fromDate + "')";

		// String advances =
		// "select amount from employee_pension_advances where pensionno=1";

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		String monthsBuffer = "", formatter = "", tempMntBuffer = "";
		long transMntYear = 0, empRetriedDt = 0;
		double pensionCOntr = 0;
		double pensionCOntr1 = 0;
		String recordCount = "", DataModifiedFlag = "";
		int getDaysBymonth = 0, cnt = 0, checkMnts = 0, chkPrvmnth = 0, chkCrntMnt = 0;
		double PENSIONCONTRI = 0;
		boolean contrFlag = false, chkDOBFlag = false, formatterFlag = false;
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info(sqlQuery);
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

				if (rs.getString("DataModifiedFlag") != null) {
					DataModifiedFlag = rs.getString("DataModifiedFlag");
				}
				log
						.info("getEmployeePensionInfoForAdjCrtnForUserRequired==DataModifiedFlag  "
								+ DataModifiedFlag);
				ArrayList empPensionList = new ArrayList();
				if (DataModifiedFlag.equals("Y")) {

					empPensionList = this.getEmpPensionInfoForAdjCrtnLogData(
							con, Pensionno, monthYear, batchid);
				}
				if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
					buffer.append(empPensionList.get(0));
					buffer.append(",");

					buffer.append(empPensionList.get(1));
					buffer.append(",");

					buffer.append(empPensionList.get(2));
					buffer.append(",");

					buffer.append(empPensionList.get(3));
					buffer.append(",");

					buffer.append(empPensionList.get(4));
					buffer.append(",");

				} else {
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

				}

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

				if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
					buffer.append(empPensionList.get(5));
				} else {
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
								getDaysBymonth = commonDAO
										.getNoOfDays(dateOfBirth);
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
				// Regarding pensioncontri calcuation upto current date modified
				// by radha p
				if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
					buffer.append(empPensionList.get(5));
				} else {
					if (transdate.after(new Date(commonUtil
							.getCurrentDate("dd-MMM-yy")))
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
									.getString("EMOLUMENTS"), whetherOption,
									region, rs.getString("emolumentmonths"));
							if (chkDOBFlag == true) {
								String[] dobList = dateOfBirth.split("-");
								days = dobList[0];
								getDaysBymonth = commonDAO
										.getNoOfDays(dateOfBirth);
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
				}
				buffer.append(",");
				// Datafreezeflag
				buffer.append("-NA-");

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
				if (rs.getString("ADVANCE") != null) {
					buffer.append(rs.getString("ADVANCE"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWSUB") != null) {
					buffer.append(rs.getString("PFWSUB"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWCONTRI") != null) {
					buffer.append(rs.getString("PFWCONTRI"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");

				if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
					buffer.append(empPensionList.get(6));
					buffer.append(",");
				} else {
					buffer.append("N");
					buffer.append(",");
				}
				/*
				 * if (rs.getString("editeddate") != null) {
				 * buffer.append(rs.getString("editeddate")); } else {
				 * buffer.append(" "); }
				 */
				if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
					buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
				} else {
					buffer.append("N");
				}
				buffer.append("=");

			}
			/*
			 * if (count == i) { buffer = new StringBuffer(); } else { }
			 */

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("----- " + buffer.toString());
		return buffer.toString();

	}

	public ArrayList getEmpPensionInfoForAdjCrtnLogData(Connection con,
			String Pensionno, String monthYear, String batchId) {
		int count = 0;

		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String sqlQuery = "", emoluments = "", epf = "", empvpf = "", principle = "", interest = "", pensionContri = "", editTransFlag = "", monthYearQry = "", minBatchId = "";
		ArrayList al = new ArrayList();

		monthYearQry = "select min(batchid) as batchid from  epis_adj_crtn_emoluments_log where pensionno = "
				+ Pensionno
				+ "    and originalrecord = 'N'    and monthyear = '"
				+ monthYear
				+ "' and batchid between "
				+ batchId
				+ " and   (select max(batchid) from epis_adj_crtn_emoluments_log  where pensionno =  "
				+ Pensionno + ")";
		try {
			log.info("--monthYearQry------" + monthYearQry);

			st = con.createStatement();
			rs = st.executeQuery(monthYearQry);
			if (rs.next()) {
				minBatchId = rs.getString("batchid");
			}
			sqlQuery = " select OLDEMOLUMENTS AS OLDEMOLUMENTS, OLDEMPPFSTATUARY  AS OLDEMPPFSTATUARY,OLDEMPVPF  AS OLDEMPVPF, OLDPRINCIPLE as OLDPRINCIPLE,OLDINTEREST AS OLDINTEREST, OLDPENSIONCONTRI AS OLDPENSIONCONTRI , NEWEMOLUMENTS  AS NEWEMOLUMENTS , NEWEMPPFSTATUARY AS NEWEMPPFSTATUARY, NEWEMPVPF AS NEWEMPVPF, NEWPRINCIPLE AS NEWPRINCIPLE, NEWINTEREST AS NEWINTEREST  from epis_adj_crtn_emoluments_log    where pensionno = "
					+ Pensionno
					+ "    and batchid   ="
					+ minBatchId
					+ "   and originalrecord = 'N'    and monthyear = '"
					+ monthYear
					+ "' "
					+ " and rowid = (select max(rowid)   from epis_adj_crtn_emoluments_log  where pensionno = "
					+ Pensionno
					+ "     and batchid ="
					+ minBatchId
					+ "       and originalrecord = 'N'    and monthyear = '"
					+ monthYear + "')";
			log.info("--sqlQuery------" + sqlQuery);

			rs1 = st.executeQuery(sqlQuery);
			if (rs1.next()) {

				if (rs1.getString("OLDEMOLUMENTS") != null) {
					emoluments = rs1.getString("OLDEMOLUMENTS");
				} else {
					emoluments = "0";
				}
				if (rs1.getString("OLDEMPPFSTATUARY") != null) {
					epf = rs1.getString("OLDEMPPFSTATUARY");
				} else {
					epf = "0";
				}
				if (rs1.getString("OLDEMPVPF") != null) {
					empvpf = rs1.getString("OLDEMPVPF");
				} else {
					empvpf = "0";
				}
				if (rs1.getString("OLDPRINCIPLE") != null) {
					principle = rs1.getString("OLDPRINCIPLE");
				} else {
					principle = "0";
				}
				if (rs1.getString("OLDINTEREST") != null) {
					interest = rs1.getString("OLDINTEREST");
				} else {
					interest = "0";
				}
				if (rs1.getString("OLDPENSIONCONTRI") != null) {
					pensionContri = rs1.getString("OLDPENSIONCONTRI");
				} else {
					pensionContri = "0";
				}
				log.info("--minBatchId---=" + minBatchId);
				if (batchId.equals(minBatchId)) {
					editTransFlag = "Y";
				} else {
					editTransFlag = "N";
				}
				al.add(emoluments);
				al.add(epf);
				al.add(empvpf);
				al.add(principle);
				al.add(interest);
				al.add(pensionContri);
				al.add(editTransFlag);
				log.info("--emoluments---" + emoluments + "---epf----" + epf
						+ "---empvpf------" + empvpf + "----principle----"
						+ principle + "----interest--" + interest
						+ "---pensionContri--" + pensionContri
						+ "--editTransFlag---" + editTransFlag);
			}

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			try {
				rs1.close();
				commonDB.closeConnection(null, st, rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return al;
	}

	// By Radha On 16-Feb-2012 For Chking Prv Grand Totals r there r not for
	// Finalize Condition
	public String chkAdjCrtnPrvPCTotals(Connection con, String empserialNO, String adjOBYear,String chkChqApproverFlag) {
		String selQuery = "", result = "", remarksCond="";		 
		Statement st = null;
		ResultSet rs = null;
		try {
			if(chkChqApproverFlag.equals("true")){
				remarksCond="and remarks='After Approved'";
			}else{
				remarksCond="";
			}
			st = con.createStatement();
			selQuery = "select 'X' as flag from epis_adj_crtn_prv_pc_totals where pensionno="
					+ empserialNO + " and ADJOBYEAR='" + adjOBYear + "'" +remarksCond;

			log.info("AdjCrtnDAO::chkAdjCrtnPrvPCTotals()---selQuery "
					+ selQuery);
			rs = st.executeQuery(selQuery);
			if (rs.next()) {
				result = rs.getString("flag");
			}

			if (result.equals("X")) {
				result = "Exists";
			} else {
				result = "NotExists";
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);

		}
		return result;
	}
//	By Radha On 25-Oct-2012 ArrearBreak up data updation while modifying that data 
//	 By Radha On 21-Jun -2012 for  Disabling revisedemolumentsflag when emolumets are nullified
	//	 By Radha On 26-Mar-2012 for  Removing editing values in EMPLOYEE_PENSION_OB_ADJ_CRTN
//	By radha on 02-Mar-2012 for inserting records in  Emol temp Table  while Edit the Each Record
	// By Radha on 18-Jan-2012 Enabling revisedemolumentsflag when emolumets are
	// nullified
	// By Radha on 05-Jan-2012 For Restricting the automatic Calc of Pc for all
	// the Months i.e., from Apr 2008 to till
	// By Radha on 16-Dec-2011 While Adding new records after Death r retirement
	// enabling arrearbreakupflag to restrict in form 7 PS
	// By Radha on 12-Dec-2011 for adding Noof Months in AdjCal Screen
	public ArrayList editTransactionDataForAdjCrtn(String cpfAccno,
			String monthyear, String emoluments,String addcon, String epf, String empvpf,
			String principle, String interest, String advance, String loan,
			String aailoan, String contri, String noofmonths, String pfid,
			String region, String airportcode, String username,
			String computername, String form7narration, String duputationflag,
			String pensionoption, String empnetob, String aainetob,
			String empnetobFlag, String finYear, String editTransFlag,String dateOfBirth) {

		String emppfstatuary = "0.00", oldemppfstatuary = "0.00", pf = "0.00", adjObYear = "";
		String updateEpisAdjCrtnLog="",insertEpisAdjCrtnLogDtl="",episAdjCrtnLog="",episAdjCrtnLogDtl="";
		String tableName = "EPIS_ADJ_CRTN";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		String years[] = null;
		long  retiremntDate =0,monthYear=0,retiremntDate1 =0,monthYear1=0;
		double pensionCOntr = 0.0,oldEmoluments=0.0,oldPensionContri=0.0,oldDueEmoluments=0.0,oldDuePensionAmnt=0.0,addcon1=0.0,oldemolments=0.0;
		Connection con = null;
		Statement st = null;	 
		ResultSet rs = null;
		ResultSet rs1= null;
		String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "", chkArrearBrkupFlag = "", arrearBreakupFlag = "N",notFianalizetransID="",chkMnthFlag="",newRecord="false",updateArrearBrkUpData="",updatePFCardNarration="";
		boolean newcpfaccnoflag = false;
		System.out.println("finYear==="+finYear+"==transMonthYear=="+monthyear);
		System.out.println("1111====="+finYear.substring(0, 4));
		
		
		ArrayList adjEmolumentsList = new ArrayList();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			log.info("-------Pension Contri ------" + contri);
			retiremntDate = Date.parse(commonDAO.getRetriedDate(dateOfBirth));
			monthYear = Date.parse(monthyear);
			if(Integer.parseInt(finYear.substring(0, 4))>=2013){
				String checkPFID = " SELECT TO_CHAR(ADD_MONTHS('"+monthyear+"',1),'DD-MON-YYYY') as transMonthYear FROM dual ";
			    log.info(checkPFID);
			    rs = st.executeQuery(checkPFID);
			    log.info("==="+checkPFID);
			    while(rs.next()){
			    	log.info("::=====");
			    	monthyear=rs.getString("transMonthYear");
			    }
				}
			    System.out.println("1111=====transMonthYear=="+transMonthYear);
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
				st.executeUpdate(updateObForAdjCrtn);

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
				bean = this.getEmolumentsBeanForAdjCrtn(con, monthyear,
						cpfAccno, "", region, pfid);
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
							+ cpfAccno + "' , emoluments='" + emoluments
							+ "',additionalcontri='" + addcon
							+ "',emppfstatuary='"
							+ Math.round(Float.parseFloat(emppfstatuary))
							+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
							+ principle + "',EMPADVRECINTEREST='" + interest
							+ "',PENSIONCONTRI='"+ contri + "',pf='" + pf + "', emolumentmonths='"
							+ noofmonths + "', empflag='Y',edittrans='"
							+ editTransFlag + "',FORM7NARRATION='"
							+ form7narration + "',editeddate='" + updatedDate
							+ "'  where "+ wherecondition+ " and  to_char(monthyear,'dd-Mon-yy') like '%"+ transMonthYear + "'  AND empflag='Y' ";

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
							+ pensionCOntr);
					// implemented for having no emoluments but having some
					// arrear related data
					if (bean != null && bean.getNoDataFlag().equals("false")) {

						sqlQuery = "update "
								+ tableName
								+ " set  cpfaccno='"
								+ cpfAccno
								+ "' ,emoluments='"
								+ emoluments
								+ "',additionalcontri='"
								+ addcon
								+ "',emppfstatuary='"
								+ Math.round(Float.parseFloat(emppfstatuary))
								+ "',empvpf='"
								+ empvpf
								+ "',EMPADVRECPRINCIPAL='"
								+ principle
								+ "',EMPADVRECINTEREST='"
								+ interest								 
								+ "',PENSIONCONTRI='"
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
								+ "' where pensionno='"
								+ pfid
								+ "'"
								+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
								+ transMonthYear + "'  AND empflag='Y' ";

					} else {
						newRecord="true";
						if (transdate.after(new Date("31-Mar-2008"))) {
							FinacialDataBean dataBean = new FinacialDataBean();
							dataBean = this.getPreMonthYearData(con, monthyear,
									pfid);
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
							chkArrearBrkupFlag = this.checkArrearBreakupLimit(
									con, pfid, monthyear);
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
								+ " (emoluments,additionalcontri,emppfstatuary,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,ADVANCE,PFWSUB ,PFWCONTRI ,PENSIONCONTRI,pf,monthyear,cpfaccno,region,pensionno,FORM7NARRATION,ARREARSBREAKUP, EMPFLAG, edittrans,remarks,AIRPORTCODE,editeddate) values('"
								+ emoluments + "','"
								+ addcon + "','"
								+ Math.round(Float.parseFloat(emppfstatuary))
								+ "','" + Math.round(Float.parseFloat(empvpf))
								+ "','" + principle + "','" + interest + "','"
								+ advance + "','" + loan + "','" + aailoan
								+ "','" + Math.round(pensionCOntr) + "','"
								+ Math.round(Float.parseFloat(pf)) + "','"
								+ monthyear + "','" + cpfAccno + "','" + region
								+ "','" + pfid + "','" + form7narration + "','"
								+ arrearBreakupFlag
								+ "','Y','N','New Record','" + airportcode
								+ "','" + updatedDate + "')";

					}
				}
				int count = 0;
				String selectEmolumentsLog = "select count(*) as count from EPIS_ADJ_CRTN_EMOLUMENTS_LOG where cpfacno='"
						+ cpfAccno
						+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear + "' and region='" + region + "' ";
				log.info("---------selectEmolumentsLog ------"
						+ selectEmolumentsLog);
				rs = st.executeQuery(selectEmolumentsLog);
				while (rs.next()) {
					count = rs.getInt(1);
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
				
				chkMnthFlag = chkMnthInEmolTempLog(con,pfid,monthyear);
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
					emolBean.setAddcon(addcon);
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
					emolBean.setAddcon(addcon);
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
				notFianalizetransID = this.getAdjCrtnNotFinalizedTransId(con,
						pfid, finYear);
				log.info("notFianalizetransID  In   " + notFianalizetransID
						+ "adjEmolList Size " + adjEmolumentsList.size());
				 
				this.insertAdjEmolumenstLogInTemp(adjEmolumentsList, pfid,
							finYear, notFianalizetransID);
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
					rs = st.executeQuery(selectQry);
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
						st.executeUpdate(insertQry);
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
		log.info("logid  loggeridseq" + loggeridseq);
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
					log.info("updatePFCardNarration111111111111111111   " + updatePFCardNarration);
				}
				
				double  returnPC=0.0;
				log.info("monthyear   " + monthyear);
				returnPC = this.pensionContributionProcess2008to11ForAdjCRTN(region, pfid,
						monthyear);
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
//	By Radha on 02-Feb-2012 for modifying(removing rowid based cond) the code according to the condition getting prev Mnth Details
	//By Radha on 17-Dec-2011 for modifying the code according to the condition getting prev Mnth Details
	/*
	 * This dataBean for getting for previous monthyear region & airportcode
	 * while inserting new record
	 */
	public FinacialDataBean getPreMonthYearData(Connection con, String fromDate,String Pensionno) {

		String foundEmpFlag = "";
		Statement st = null;
		ResultSet rs = null;
		FinacialDataBean bean =null;
		try {
			 
			String query = "",query1="";
				query =" select emoluments, EMPPFSTATUARY,EMPVPF, EMPADVRECPRINCIPAL,   EMPADVRECINTEREST,  PENSIONCONTRI,REGION,AIRPORTCODE,MONTHYEAR from epis_adj_crtn"
						+ " where to_date(to_char(monthyear, 'dd-Mon-yyyy')) < '"+fromDate+"'   and pensionno = "+Pensionno+"  and empflag = 'Y' and  region is not null and monthyear=(select max(monthyear) from" 
			            +" epis_adj_crtn where to_date(to_char(monthyear, 'dd-Mon-yyyy')) < '"+fromDate+"'  and pensionno = "+Pensionno+"    and empflag = 'Y' and  region is not null)";
			
				
				query1 =" select emoluments, EMPPFSTATUARY,EMPVPF, EMPADVRECPRINCIPAL,   EMPADVRECINTEREST,  PENSIONCONTRI,REGION,AIRPORTCODE,MONTHYEAR from epis_adj_crtn"
					+ " where to_date(to_char(monthyear, 'dd-Mon-yyyy')) > '"+fromDate+"'   and pensionno = "+Pensionno+"  and empflag = 'Y' and  region is not null and monthyear=(select min(monthyear) from" 
		            +" epis_adj_crtn where to_date(to_char(monthyear, 'dd-Mon-yyyy')) > '"+fromDate+"'  and pensionno = "+Pensionno+"    and empflag = 'Y' and  region is not null)";
		
			log.info("AdjCrtnDAO::getPreMonthYearData()----------"+query);
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
				if (rs.getString("REGION") != null) {
					bean.setRegion(rs.getString("REGION"));
				}
				if (rs.getString("AIRPORTCODE") != null) {
					bean.setAirportCode(rs.getString("AIRPORTCODE"));
				}
				 
			}else{ 
				rs.close(); 
				log.info("AdjCrtnDAO::getPreMonthYearData()----------"+query1);
				rs = st.executeQuery(query1);
				 
				if (rs.next()) {				
					bean = new FinacialDataBean();
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
				if (rs.getString("REGION") != null) {
					bean.setRegion(rs.getString("REGION"));
				}
				if (rs.getString("AIRPORTCODE") != null) {
					bean.setAirportCode(rs.getString("AIRPORTCODE"));
				}
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
		 
		return bean;
	}
	public int updateMnthYearFormat(Connection con, String pfid,	String monthyear) {
		Statement st = null;
		ResultSet rs = null;
		String updateQuery = "";
		int result =0;
		updateQuery = "update  epis_adj_crtn set monthyear ='"+monthyear+"' where pensionno ="+pfid+" and  to_char(monthyear,'Mon-yyyy') = to_char(to_date('"+monthyear+"'), 'Mon-yyyy')";
			 
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
	public String chkMnthInEmolTempLog(Connection con, String pfid,	String monthyear) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", chkMnthFlag = "";
		sqlQuery = "select  'X' as flag  from epis_adj_crtn_emol_log_temp  where pensionno = "
				+ pfid
				+ " and  monthyear ='"+monthyear+"'";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			log.info("checkArrearBreakupLimit ==sqlQuery===" + sqlQuery);
			if (rs.next()) {
				if(rs.getString("flag")!=null){
				chkMnthFlag = rs.getString("flag");
				}
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return chkMnthFlag;
	}
	// By Radha on 16-Dec-2011
	public String checkArrearBreakupLimit(Connection con, String pfid,
			String monthyear) {
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", arrearBrkupFlag = "";
		sqlQuery = "select  'X' as flag  from employee_arrear_breakup  where pensionno = "
				+ pfid
				+ " and  arreartodate  between   '"
				+ monthyear
				+ "' and arreartodate";
		try {
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			log.info("checkArrearBreakupLimit ==sqlQuery===" + sqlQuery);
			if (rs.next()) {
				arrearBrkupFlag = rs.getString("flag");
			}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}

		return arrearBrkupFlag;
	}

	// new method on 13-Feb-2012
	public String adjCrtnMappingUpdate(String pfid, String cpfaccno,
			String region, String username, String ipaddress, String airportCode) {
		log.info("AdjCrtnDAO: adjCrtnMappingUpdate Entering method");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int totalRecords = 0;
		String count = "Count";
		String selectSQL = "", updatePfid = "";

		selectSQL = "update  epis_info_adj_crtn set   empserialnumber='" + pfid
				+ "',mappedusernm='" + username + "',ipaddress='" + ipaddress
				+ "' where cpfacno='" + cpfaccno + "' and region='" + region
				+ "'and AIRPORTCODE='" + airportCode + "'";

		try {
			log.info(selectSQL);
			con = DBUtils.getConnection();
			st = con.createStatement();
			int result = st.executeUpdate(selectSQL);
			log.info("result" + result);

			updatePfid = pfid;

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("AdjCrtnDAO: adjCrtnMappingUpdate leaving method");
		return updatePfid;
	}

	public int moveAdjCrtnToAdjCrtnMdBk(String pfid, String adjObYear) {
		log.info("AdjCrtnDAO:moveAdjCrtnToAdjCrtnMdBk() Entering Method");
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int result = 0;
		String sql = "insert into EPIS_ADJ_CRTN_md_bk (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
				+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,empvpf,advance,pfwsub,pfwcontri)   (select  "
				+ pfid
				+ " ,"
				+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
				+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , REMARKS,empvpf,advance,pfwsub,pfwcontri   from EPIS_ADJ_CRTN where empflag='Y' and PENSIONNO='"
				+ pfid + "'and monthyear <='01-Mar-2008')";

		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("sql" + sql);
			result = st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		log.info("AdjCrtnDAO:moveAdjCrtnTempToAdjCrtnMdBk() leaving Method");
		return result;

	}

	// new method
	public int moveAdjCrtnTempToAdjCrtnMdBk(String pfid, String adjObYear) {
		Connection con = null;
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int result = 0;
		String sql = "insert into EPIS_ADJ_CRTN_md_bk (PENSIONNO ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY, EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
				+ " USERNAME ,IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,LASTACTIVEDATE , REMARKS,datatype)   (select  "
				+ pfid
				+ " ,"
				+ "  MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,"
				+ " USERNAME  , IPADDRESS , DEPUTATIONFLAG,PF,PENSIONCONTRI,sysdate , 'Through Upload Screen','U'   from EPIS_ADJ_CRTN_TEMP where empflag='Y' and PENSIONNO='"
				+ pfid + "'and monthyear <='01-Mar-2008')";

		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info("sql" + sql);
			result = st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
		log.info("AdjCrtnDAO:moveAdjCrtnTempToAdjCrtnMdBk() leaving Method");
		return result;

	}

	// New Method
	// By Radha On 14-Oct-2011 for Adj Corrections in Form 7/8Ps
	public ArrayList getPCReportFor78PsAdjCRTN(String fromDate, String toDate,
			String region, String airportcode, String empserialNO,
			String cpfAccno, String transferFlag, String mappingFlag) {
		ArrayList penContHeaderList = new ArrayList();

		// For Fetching the Employee PersonalInformation
		penContHeaderList = this.PCHeaderInfoForAdjCrtn(region, airportcode,
				empserialNO);

		String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "", empCpfaccno = "";
		String[] cpfaccnos = new String[10];
		String[] regions = new String[10];
		String[] empPensionList = null;
		String[] pensionInfo = null;
		CommonDAO commonDAO = new CommonDAO();
		String pensionList = "", dateOfRetriment = "";
		ArrayList penConReportList = new ArrayList();
		log.info("Header Size" + penContHeaderList.size());
		log.info("" + penContHeaderList);
		String countFlag = "";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getConnection();
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
				// log.info(penContHeaderBean.getWhetherOption() + "option");
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

				// The Below mentioned method for Preparing the CPFSTRING based
				// on CPFACCNOs and corresponding regions.
				String preparedString = commonDAO.preparedCPFString(cpfaccnos,
						regions);
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

				pensionList = this.getEmployeePensionInfoFor78PsAdjCrtn(
						preparedString, fromDate, toDate, penContHeaderBean
								.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), empserialNO);
				String checkMnthDate = "", rateOfInterest = "";
				st = con.createStatement();
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

							// log.info("contribution "+pensionInfo[7]);
							bean.setPensionContr(pensionInfo[7]);
							// calCPF=Double.parseDouble(bean.getCpf());
							// calPens=Double.parseDouble(pensionInfo[7]);
							calCPF = Math.round(Double.parseDouble(bean
									.getCpf()));
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
							// log.info("calCPF " +calCPF +"calPens "+calPens );
							bean.setGenMonthYear(pensionInfo[8]);
							bean.setTransCpfaccno(pensionInfo[9]);
							bean.setRegion(pensionInfo[10]);
							// log.info("pensionInfo[12] " + pensionInfo[12]);
							bean.setRecordCount(pensionInfo[11]);
							bean.setDbPensionCtr(pensionInfo[12]);
							bean.setDataFreezFlag(pensionInfo[13]);
							bean.setForm7Narration(pensionInfo[14]);
							bean.setPcHeldAmt(pensionInfo[15]);
							bean.setNoofMonths(pensionInfo[16]);
							bean.setPccalApplied(pensionInfo[17].trim());
							bean.setAdvAmount(pensionInfo[21].trim());
							bean.setEmployeeLoan(pensionInfo[22].trim());
							bean.setAaiLoan(pensionInfo[23].trim());
							bean.setEditedDate(pensionInfo[24].trim());
							// log.info("PcApplied " +bean.getPccalApplied());
							if (bean.getPccalApplied().equals("N")) {
								bean.setCpf("0.00");
								bean.setAaiPFCont("0.00");
								bean.setPensionContr("0.00");
								bean.setDbPensionCtr("0.00");
							}
							bean.setArrearFlag(pensionInfo[18].trim());
							bean.setDeputationFlag(pensionInfo[19].trim());
							// For Fetching the Advances and Loans by Month Wise
							// for editing the Amount in Recoveries Screen
							String monthYear1 = commonUtil.converDBToAppFormat(
									pensionInfo[0], "dd-MMM-yyyy", "MMM-yyyy");

							// log.info("loan " + bean.getEmployeeLoan());
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

	// New Method

	private String getEmployeePensionInfoFor78PsAdjCrtn(String cpfString,
			String fromDate, String toDate, String whetherOption,
			String dateOfRetriment, String dateOfBirth, String Pensionno) {

		// Here based on recoveries table flag we deside which table to hit and
		// retrive the data. if recoverie table value is false we will hit
		// Employee_pension_validate else employee_pension_final_recover table.
		String tablename = "EPIS_ADJ_CRTN_FORM78PS";

		log.info("formdate " + fromDate + "todate " + toDate);
		String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
		String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from epis_info_adj_crtn where ("
				+ tempCpfString + ") and rownum=1)";
		String condition = "";
		if (Pensionno != "" && !Pensionno.equals("")) {
			condition = " or pensionno=" + Pensionno;
		}

		String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.ADVANCE AS ADVANCE,emp.PFWSUB AS PFWSUB,emp.PFWCONTRI AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI from "
				+ "(select distinct to_char(to_date('"
				+ fromDate
				+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from   employee_pension_validate    "
				+ " where empflag='Y' and    rownum "
				+ "<= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date('"
				+ fromDate
				+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,NVL(ADVANCE,0.00) AS ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI FROM "
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
				+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,nvl(emp.ADVANCE,0.00) AS ADVANCE,nvl(emp.PFWSUB,0.00) AS PFWSUB,nvl(emp.PFWCONTRI,0.00) AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI  from (select distinct to_char(to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
				+ toDate
				+ "','dd-mon-yyyy')-to_date("
				+ dojQuery
				+ ",'dd-mon-yyyy')+1 ) empdt,"
				+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
				+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,nvl(ADVANCE,0.00) as ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI "
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
				+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('"
				+ fromDate + "')";

		// String advances =
		// "select amount from employee_pension_advances where pensionno=1";

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
			con = DBUtils.getConnection();
			st = con.createStatement();
			log.info(sqlQuery);
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
				// Regarding pensioncontri calcuation upto current date modified
				// by radha p
				if (transdate.after(new Date(commonUtil
						.getCurrentDate("dd-MMM-yy")))
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
								.getString("EMOLUMENTS"), whetherOption,
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
				// Datafreezeflag
				buffer.append("-NA-");

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
				if (rs.getString("ADVANCE") != null) {
					buffer.append(rs.getString("ADVANCE"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWSUB") != null) {
					buffer.append(rs.getString("PFWSUB"));
				} else {
					buffer.append(" ");
				}
				buffer.append(",");
				if (rs.getString("PFWCONTRI") != null) {
					buffer.append(rs.getString("PFWCONTRI"));
				} else {
					buffer.append(" ");
				}
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
				buffer.append("=");

			}
			/*
			 * if (count == i) { buffer = new StringBuffer(); } else { }
			 */

		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("----- " + buffer.toString());
		return buffer.toString();

	}

	public void editTransactionDataFor78PsAdjCrtn(String cpfAccno,
			String monthyear, String emoluments, String epf, String empvpf,
			String principle, String interest, String advance, String loan,
			String aailoan, String contri, String pfid, String region,
			String airportcode, String username, String computername,
			String from7narration, String statemntWagesRemarks ,String duputationflag, String pensionoption,
			String finYear, String editTransFlag,String dueemoluments, String duepension) {
		String emppfstatuary = "0.00", oldemppfstatuary = "0.00", pf = "0.00", adjObYear = "",remarks="";
		String tableName = "EPIS_ADJ_CRTN_FORM78PS";
		String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
		String years[] = null;
		double pensionCOntr = 0.0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "";
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			Date transdate = df.parse(monthyear);
			log.info("-------Pension Contri ------" + contri);
			 
			if (transdate.after(new Date("01-Apr-95")) && transdate.before(new Date("31-Mar-08"))){
				adjObYear="1995-2008";
			}else if (transdate.after(new Date("01-Apr-08")) && transdate.before(new Date("31-Mar-09"))){
				adjObYear="2008-2009";
			}else if (transdate.after(new Date("01-Apr-09")) && transdate.before(new Date("31-Mar-10"))){
				adjObYear="2009-2010";
			}else if (transdate.after(new Date("01-Apr-10")) && transdate.before(new Date("31-Mar-11"))){
				adjObYear="2009-2010";
			}else{
				adjObYear="";
			}
							
			//For Tracking 			
			 insertRecordFor78PSAdjCtrnTracking(con,
					 pfid,   cpfAccno,   adjObYear,
					  username,   computername);
			
			if(!finYear.equals("")){
			years = finYear.split("-");
			adjObYear = "01-Apr-" + Integer.parseInt(years[0]);
			}
			transMonthYear = commonUtil.converDBToAppFormat(monthyear.trim(),
					"dd-MMM-yy", "-MMM-yy");

			if (cpfAccno.indexOf(",") != -1) {
				cpfAccno = cpfAccno.substring(0, cpfAccno.indexOf(","));
			}
			if (transdate.after(new Date("31-Mar-98"))
					&& transdate.before(new Date(commonUtil
							.getCurrentDate("dd-MMM-yy")))
					&& duputationflag != "Y") {
				emppfstatuary = String
						.valueOf(Float.parseFloat(emoluments) * 12 / 100);
			} else if (transdate.before(new Date("31-Mar-98"))) {
				emppfstatuary = String
						.valueOf(Float.parseFloat(emoluments) * 10 / 100);
			} else {
				if (epf.equals("0")) {
					emppfstatuary = String
							.valueOf(Float.parseFloat(emoluments) * 12 / 100);
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
			bean = this.getEmolumentsBeanFor78PsAdjCrtn(con, monthyear,
					cpfAccno, "", region, pfid);
			log.info("emoluments " + bean.getEmoluments());
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
				String wherecondition = "";
				if (pfid == "" || transdate.before(new Date("31-Mar-2008"))) {
					wherecondition += "(( cpfaccno='" + cpfAccno
					+ "'   and region='" + region
					+ "' ) or pensionno='" + pfid + "')";

				} else {
					wherecondition += "pensionno='" + pfid + "'";
				}

				/*sqlQuery = "update " + tableName + " set emoluments='"
						+ emoluments + "',emppfstatuary='" + emppfstatuary
						+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
						+ principle + "',EMPADVRECINTEREST='" + interest
						+ "',ADVANCE='" + advance + "',PFWSUB='" + loan
						+ "',PFWCONTRI='" + aailoan + "',PENSIONCONTRI='"
						+ contri + "',pf='" + pf + "', empflag='Y',edittrans='"
						+ editTransFlag + "',FORM7NARRATION='"
						+ from7narration +"' ,SWREMARKS='"+ statemntWagesRemarks
						+ "',editeddate='" + updatedDate + "' where "
						+ wherecondition
						+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
						+ transMonthYear + "'  AND empflag='Y' ";*/
				
				if((!bean.getDueemoluments().equals(dueemoluments)) || (!bean.getDuepensionamount().equals(duepension))){
					remarks=bean.getDueemoluments()+","+bean.getDuepensionamount();
				}
				
				//For 12Mnth Statement purpose
				sqlQuery = "update " + tableName + " set emoluments='"
				+ emoluments + "',emppfstatuary='" + emppfstatuary
				+ "'  ,PENSIONCONTRI='"
				+ contri + "',edittrans='"
				+ editTransFlag + "',FORM7NARRATION='"
				+ from7narration +"' ,SWREMARKS='"+ statemntWagesRemarks
				+ "',editeddate='" + updatedDate
				+ "',reviseepfemolumentsflag='N',DUEEMOLUMENTS='"+dueemoluments+"',ARREARAMOUNT='"+duepension
				+ "', REMARKS = REMARKS||+'"+remarks+"' where "
				+ wherecondition
				+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
				+ transMonthYear + "'  AND empflag='Y' ";

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
				sqlQuery = "insert into "
						+ tableName
						+ " (emoluments,emppfstatuary,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,ADVANCE,PFWSUB ,PFWCONTRI ,PENSIONCONTRI,pf,monthyear,cpfaccno,region,pensionno,FORM7NARRATION,SWREMARKS, EMPFLAG, edittrans,remarks,AIRPORTCODE,editeddate,DUEEMOLUMENTS,ARREARAMOUNT) values('"
						+ emoluments + "','" + emppfstatuary + "','" + empvpf
						+ "','" + principle + "','" + interest + "','"
						+ advance + "','" + loan + "','" + aailoan + "','"
						+ pensionCOntr + "','" + pf + "','" + monthyear + "','"
						+ cpfAccno + "','" + region + "','" + pfid + "','"
						+ from7narration + "','"+statemntWagesRemarks+"','Y','N','New Record','"
						+ airportcode + "','" + updatedDate + "','" + dueemoluments + "','" + duepension + "')";

			}

			String selectEmolumentsLog = "select count(*) as count from EPIS_ADJ_CRTN_78PS_LOG where cpfacno='"
					+ cpfAccno
					+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear + "' and region='" + region + "' ";
			rs = st.executeQuery(selectEmolumentsLog);
			while (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					emoluments_log = "insert into EPIS_ADJ_CRTN_78PS_LOG(oldemoluments,newemoluments,oldemppfstatuary,newemppfstatuary,oldprinciple,newprinciple,oldinterest,newinterest,oldempvpf,newempvpf,OLDPENSIONCONTRI,NEWENSIONCONTRI,monthyear,UPDATEDDATE,pensionno,cpfacno,region,username,computername)values('"
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
					emoluments_log = "update EPIS_ADJ_CRTN_78PS_LOG set oldemoluments='"
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
							+ "' where(( cpfaccno='" + cpfAccno	+ "'   and region='" + region + "' ) or pensionno='" + pfid + "') "
							+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
							+ transMonthYear + "' ";

				}

			}
			log.info("emoluments_log .." + emoluments_log);
			log.info(" update transaction " + sqlQuery);
			st.executeUpdate(emoluments_log);
			st.executeUpdate(sqlQuery);

			if (transdate.after(new Date("31-Mar-08"))) {				 
				this.pensionContributionProcess2008to11For78PsAdjCRTN(region,
						pfid,monthyear);
			}
			
			
			
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);

		}
	}

	// On 13-Dec-2011 By Radha for rounding of Pension Contri if emolument
	// Months is there
	// On 15-Nov-2011 By Radha to resolve 01-Mar & Ist day of month issue	 
	public double calclatedPFForAdjCrtn(String monthYear,
			String dateOfRetriment, String dateOfBirth, String calEmoluments,
			String wetherOption, String region, 
			String emolumentMonths, String emppfstatuary,String pcCalFlag) {
		long transMntYear = 0, empRetriedDt = 0;
		boolean flag = true, contrFlag = false, chkDOBFlag = false;
		double pensionVal = 0.00;
		String days = "", getMonth = "";
		int getDaysBymonth = 0;
		DecimalFormat df = new DecimalFormat("#########0.00");
		DecimalFormat df1 = new DecimalFormat("#########0.0000000000000");
		double pensionAsPerOption = 0.0, retriredEmoluments = 0.0;
		/*long noofdays = CommonUtil.getDifferenceTwoDatesInDays(dateOfRetriment,
				monthYear);*/
		log.info("calclatedPFForAdjCrtn() Entering== "); 
		if (flag == true) {
			if (!monthYear.equals("-NA-") && !dateOfRetriment.equals("")) {
				transMntYear = Date.parse(monthYear);
				empRetriedDt = Date.parse(dateOfRetriment);

				log.info("pcCalFlag= " + pcCalFlag + "  =dateOfRetriment= " + dateOfRetriment
						+ " =transMntYear= " + monthYear);

				/*if (transMntYear >= empRetriedDt) {
					contrFlag = true;
				} else if (transMntYear == 0 || empRetriedDt == 0) {
					contrFlag = false;
				}*/
				
				/*if (transMntYear != 0
						&& empRetriedDt != 0
						&& (Integer.parseInt(days1) >= 0 && Integer
								.parseInt(days1) <= 1)
						|| (Integer.parseInt(days1) < 0 && Integer
								.parseInt(days1) > -29)) {
					chkDOBFlag = true;
				}*/
			
				
			if(pcCalFlag.equals("NIL") || pcCalFlag.equals("H")){
				chkDOBFlag = true;
			}else if (pcCalFlag.equals("F") ){
				contrFlag = true;
			}
			log.info("chkDOBFlag " + chkDOBFlag);
			log.info("contrFlag " + contrFlag);
			}
			if (contrFlag == true) {
				pensionVal = commonDAO.pensionFormsCalculation12Months(monthYear,
						calEmoluments, wetherOption.trim(), region, false,
						false, emolumentMonths);

			}

			/*if (noofdays == 0) {
				log.info("noofdays " + noofdays);
				pensionVal = commonDAO.pensionFormsCalculation(monthYear,
						calEmoluments, wetherOption.trim(), region, false,
						false, emolumentMonths);
				log.info("--pensionVal-- " + pensionVal);
			}*/
			 
				if (chkDOBFlag == true) {
					if(pcCalFlag.equals("H")){
					pensionVal = commonDAO.pensionFormsCalculation12Months(monthYear,
							calEmoluments, wetherOption.trim(), region, false,
							false, emolumentMonths);
					}
					
					String[] dobList = dateOfBirth.split("-");
					days = dobList[0];
					getDaysBymonth = commonDAO.getNoOfDays(dateOfBirth);
				/*	long noofdays1 = CommonUtil.getDifferenceTwoDatesInDays(
							dateOfRetriment, dateOfBirth);
					log.info("--noofdays1-- " + noofdays1);
					if (noofdays1 > 0) {*/
						pensionVal = pensionVal
								* (Double.parseDouble(days) - 1)
								/ getDaysBymonth;
					} /*else {
						pensionVal = pensionVal * (Double.parseDouble(days));

					}*/
				/*}*/
			 
			log.info("--pensionVal-result- " + pensionVal);
		} else {
			pensionVal = 0;
		}
		log.info("calclatedPFForAdjCrtn() Leaving== "); 
		return pensionVal;
	}
	//Bt Radha On 02-Mar-2012 for getting more data
	public ArrayList getFinalizedAdjObYear(Connection con, String pfid, String reportYear,String chkChqApproverFlag) {
		String sqlQuery = "", adjobyear = "", remarksCond="";
		ResultSet rs = null;
		Statement st = null;
		ArrayList empPensionInfo = new ArrayList();
		EmployeePensionCardInfo empBean = null;
		if(chkChqApproverFlag.equals("true")){
			remarksCond=" and remarks='After Approved'";
		}
		sqlQuery = "select ADJOBYEAR,nvl(EMPSUB,0) AS EMPSUB,nvl(EMPSUBINTRST,0) AS EMPSUBINTRST , nvl(ADJEMPSUBINTRST,0) AS ADJEMPSUBINTRST  , nvl(AAICONTRI,0) AS AAICONTRI ,  nvl(AAICONTRIINTRST,0) AS AAICONTRIINTRST , nvl(ADJAAICONTRIINTRST,0) AS ADJAAICONTRIINTRST, nvl(PENSIONTOTAL,0) AS PENSIONTOTAL ,nvl(PENSIONINTRST,0) as PENSIONINTRST,nvl(ADJPENSIONCONTRIINTRST,0) as ADJPENSIONCONTRIINTRST   from epis_adj_crtn_pc_totals_diff where  pensionno ="
				+ pfid+" and adjobyear='"+reportYear+"'" +remarksCond;;
		log.info("AdjCrtnDAO::getFinalizedAdjObYear()---" + sqlQuery);
		try {

			con = DBUtils.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sqlQuery);
			while (rs.next()) {
				empBean = new EmployeePensionCardInfo();
				if (rs.getString("ADJOBYEAR") != null) {
					empBean.setReportYear(rs.getString("ADJOBYEAR"));
				}
				if (rs.getString("EMPSUB") != null) {
					empBean.setEmpSub(rs.getString("EMPSUB"));
				} else {
					empBean.setEmpSub("0");
				}
				if (rs.getString("EMPSUBINTRST") != null) {
					empBean.setEmpSubInterest(rs.getString("EMPSUBINTRST"));
				} else {
					empBean.setEmpSubInterest("0");
				}
				if(rs.getString("ADJEMPSUBINTRST")!=null){
					empBean.setAdjEmpSubInterest(rs.getString("ADJEMPSUBINTRST"));			 
				}else{
					empBean.setAdjEmpSubInterest("0");
				}
				if (rs.getString("AAICONTRI") != null) {
					empBean.setAaiContri(rs.getString("AAICONTRI"));
				} else {
					empBean.setAaiContri("0");
				}
				if (rs.getString("AAICONTRIINTRST") != null) {
					empBean.setAaiContriInterest(rs
							.getString("AAICONTRIINTRST"));
				} else {
					empBean.setAaiContriInterest("0");
				}
				if(rs.getString("ADJAAICONTRIINTRST")!=null){
					empBean.setAdjAaiContriInterest(rs.getString("ADJAAICONTRIINTRST"));			 
				}else{
					empBean.setAdjAaiContriInterest("0");
				}
				if (rs.getString("PENSIONTOTAL") != null) {
					empBean.setPensionTotal(rs.getString("PENSIONTOTAL"));
				} else {
					empBean.setPensionTotal("0");
				}
				if (rs.getString("PENSIONINTRST") != null) {
					empBean.setPensionInterest(rs.getString("PENSIONINTRST"));
				} else {
					empBean.setPensionInterest("0");
				}
				if(rs.getString("ADJPENSIONCONTRIINTRST")!=null){
					empBean.setAdjPensionInterest(rs.getString("ADJPENSIONCONTRIINTRST"));			 
				}else{
					empBean.setAdjPensionInterest("0");
				}
				empPensionInfo.add(empBean);
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
		return empPensionInfo;
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

	public ArrayList crtnsMadeInPCAndPFCard(String fromDate,String toDate) {
		log.info("ADJCRTNDAO:crtnsMadeInPCAndPFCard() Entering Method"+fromDate+toDate);
		String sqlQuery = "",orderby="",dynamicSelectQry="";
		ArrayList list = new ArrayList();
		CrtnsMadeInPcBean crtnsMadeBean = null;
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();

		sqlQuery = "select ts.pensionno as pensionno,ts.apporvedby as approvedby,to_date(ts.approveddate,'dd-Mon-yyyy')  as approveddate,ts.approvedtype as approvedtype,ts.appstation as appstation,ts.appregion as appregion,ts.adjobyear as adjobyear,(ts.pcinterest + ts.pcprincipal) as ob,info.employeename as employeename,info.dateofbirth as dateofbirth,info.dateofjoining as dateofjoining,info.dateofseperation_reason as dateofseperation_reason from employee_personal_info info,(select s.pensionno, t.apporvedby,s.approvedtype,to_char(s.approveddate,'dd/MON/yyyy') as approveddate,s.appstation,s.appregion,s.pcprincipal,s.pcinterest,s.adjobyear from epis_adj_crtn_transactions s, epis_adj_crtn_pfid_tracking t  where s.trackingid = t.trackingid) ts where info.pensionno = ts.pensionno  and ts.approvedtype not in('BLOCKED','Released') ";
		
		query.append(sqlQuery);
		if(!fromDate.equals("") && !toDate.equals("")){
			log.info("whereClause");
			whereClause.append(" and ");
			whereClause.append("  to_date(ts.approveddate,'dd/MON/yyyy') between '"+fromDate+"' and '"+toDate+"' ");
			
			query.append(whereClause);
		}
		if(!fromDate.equals("") && toDate.equals("")){
			log.info("whereClause");
			whereClause.append(" and ");
			whereClause.append("  ts.approveddate  >='"+fromDate+"' ");
			
			query.append(whereClause);
		}
		if(fromDate.equals("") && !toDate.equals("")){
			log.info("whereClause");
			whereClause.append(" and ");
			whereClause.append("  ts.approveddate <= '"+toDate+"' ");
			
			query.append(whereClause);
		}
		

		orderby=" order by ts.pensionno,ts.adjobyear,ts.approvedtype Desc ";
		query.append(orderby);		 
		dynamicSelectQry = query.toString();
		
		try {

			con = commonDB.getConnection();
			st = con.createStatement();
			log.info("sqlQuery" + dynamicSelectQry);
			rs = st.executeQuery(dynamicSelectQry);
			while (rs.next()) {
				crtnsMadeBean = new CrtnsMadeInPcBean();
				if (rs.getString("pensionno") != null) {
					crtnsMadeBean.setPensionno(rs.getString("pensionno"));
				} else {
					crtnsMadeBean.setPensionno("0");
				}
				if (rs.getString("approvedby") != null) {
					crtnsMadeBean.setUsername(rs.getString("approvedby"));

				} else {
					crtnsMadeBean.setUsername("---");
				}
				if (rs.getString("approveddate") != null) {
					crtnsMadeBean.setDateOfApproval(commonUtil
							.converDBToAppFormat(rs.getDate("approveddate")));

				} else {
					crtnsMadeBean.setDateOfApproval("---");
				}
				if (rs.getString("approvedtype") != null) {
					crtnsMadeBean.setApproverOrEditor(rs
							.getString("approvedtype"));

				} else {
					crtnsMadeBean.setApproverOrEditor("---");
				}
				if (rs.getString("appstation") != null) {
					crtnsMadeBean
							.setStationOrRegion(rs.getString("appstation"));

				} else {
					crtnsMadeBean.setStationOrRegion("---");
				}
				if (rs.getString("appregion") != null) {
					crtnsMadeBean.setRegion(rs.getString("appregion"));

				} else {
					crtnsMadeBean.setRegion("---");
				}
				if (rs.getString("adjobyear") != null) {
					crtnsMadeBean.setPeriodOfCorrction(rs
							.getString("adjobyear"));

				} else {
					crtnsMadeBean.setPeriodOfCorrction("---");
				}
				if (rs.getString("ob") != null) {
					crtnsMadeBean.setAdjObInPFCard(rs.getString("ob"));

				} else {
					crtnsMadeBean.setAdjObInPFCard("---");
				}
				if (rs.getString("employeename") != null) {
					crtnsMadeBean.setEmployeeName(rs.getString("employeename"));

				} else {
					crtnsMadeBean.setEmployeeName("---");
				}
				if (rs.getString("dateofbirth") != null) {
					crtnsMadeBean.setDateOfBirth(commonUtil
							.converDBToAppFormat(rs.getDate("dateofbirth")));

				} else {
					crtnsMadeBean.setDateOfBirth("---");
				}
				if (rs.getString("dateofjoining") != null) {
					crtnsMadeBean.setDateOfJoining(commonUtil
							.converDBToAppFormat(rs.getDate("dateofjoining")));

				} else {
					crtnsMadeBean.setDateOfJoining("---");
				}
				if (rs.getString("dateofseperation_reason") != null) {
					crtnsMadeBean.setDateOfSeperationReason(rs
							.getString("dateofseperation_reason"));

				} else {
					crtnsMadeBean.setDateOfSeperationReason("---");
				}
				list.add(crtnsMadeBean);

			}
			log.info("List " + list.size());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("ADJCRTNDAO:crtnsMadeInPCAndPFCard() Leaving Method");
		return list;
	}
	public AdjCrntSaveDtlBean getFinzdPreviouseGrndTotals(String empserialNO,
			String adjOBYear, String batchid) {
		String finlaizedFlag = "";
		List prevGrandTotalsList = new ArrayList();
		AdjCrntSaveDtlBean grandTotalBean = new AdjCrntSaveDtlBean();
		finlaizedFlag = this.getAdjCrtnFinalizedFlag(empserialNO, adjOBYear);
		if (finlaizedFlag.equals("Finalized")) {
			prevGrandTotalsList = this.getPrevPCGrandTotalsForAdjCrtn(
					empserialNO, adjOBYear, batchid, "");

		}
		log.info("finlaizedFlag  In   " + finlaizedFlag
				+ "prevGrandTotalsList Size " + prevGrandTotalsList.size());
		grandTotalBean.setPreviouseGrndList(prevGrandTotalsList);
		grandTotalBean.setFinalizedFlag(finlaizedFlag);
		return grandTotalBean;

	}
//By radha on 02-Mar-2012 for inserting records in  Emol temp Table  while Edit the Each Record
	public AdjCrntSaveDtlBean saveAdjCrntDetails(String empserialNO,
			String cpfAccno, String adjOBYear, double EmolumentsTot,
			double cpfTotal, double cpfIntrst, double PenContriTotal,
			double PensionIntrst, double PFTotal, double PFIntrst,
			double EmpSub, double EmpSubInterest, double adjEmpSubIntrst,
			double AAIContri, double AAIContriInterest,
			double adjAAiContriIntrst, double adjPensionContriInterest,
			String grandTotDiffShowFlag, String reasonForInsert,
			String username, String ipaddress, ArrayList adjEmolList,
			String batchid) throws InvalidDataException, SQLException {
		Connection con = null;
		int result = 0;
		String notFianalizetransID = "", transIdToGetPrevData = "", finlaizedFlag = "";
		List prevGrandTotalsList = new ArrayList();
		AdjCrntSaveDtlBean saveDtlsBean = new AdjCrntSaveDtlBean();
		try {
			con = DBUtils.getConnection();
			con.setAutoCommit(false);
			/*result = saveprvadjcrtntotals(con, empserialNO, adjOBYear,
					EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
					PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest,
					adjEmpSubIntrst, AAIContri, AAIContriInterest,
					adjAAiContriIntrst, adjPensionContriInterest);*/
			this.insertRecordForAdjCtrnTracking(con, empserialNO, cpfAccno,
					adjOBYear, reasonForInsert, username, ipaddress);
			notFianalizetransID = this.getAdjCrtnNotFinalizedTransId(con,
					empserialNO, adjOBYear);
			log.info("notFianalizetransID  In   " + notFianalizetransID
					+ "adjEmolList Size " + adjEmolList.size());
			if (notFianalizetransID.equals("")) {
				notFianalizetransID = this.getPCTotalsTransId(con, empserialNO,
						adjOBYear);
			}

			/*if (adjEmolList.size() > 0) {
				this.insertAdjEmolumenstLogInTemp(adjEmolList, empserialNO,
						adjOBYear, notFianalizetransID);
			}*/
			if (grandTotDiffShowFlag.equals("true")) {
				transIdToGetPrevData = this.insertAdjEmolumenstLog(con,
						empserialNO, adjOBYear, notFianalizetransID);
			}
			con.setAutoCommit(true);
			finlaizedFlag = this.getAdjCrtnFinalizedFlag(con, empserialNO,
					adjOBYear);
			if (finlaizedFlag.equals("Finalized")) {
				prevGrandTotalsList = this.getPrevPCGrandTotalsForAdjCrtn(
						empserialNO, adjOBYear, batchid, transIdToGetPrevData);

			}
			log.info("finlaizedFlag  In   " + finlaizedFlag
					+ "prevGrandTotalsList Size " + prevGrandTotalsList.size());
			saveDtlsBean.setPreviouseGrndList(prevGrandTotalsList);
			saveDtlsBean.setFinalizedFlag(finlaizedFlag);

		} catch (SQLException se) {
			con.rollback();
			throw new InvalidDataException(se.getMessage());
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			con.rollback();
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			con.rollback();
			throw new InvalidDataException(e.getMessage());
		} finally {
			commonDB.closeConnection(con, null, null);
		}

		return saveDtlsBean;

	}
	//By Radha on 23-May-2012 for getting data based on pensionno from 2008 onwards
	public int insertEmployeeTransDataYearWise(Connection con,String pfId,String reportYear, String frmName,String username,String ipaddress) {
		log.info("AdjCrtnDAO :insertEmployeeTransDataYearWise() Entering Method ");
		boolean dataavailbf2008=false;
		EmpMasterBean bean = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		Statement st = null;
		Statement st1 = null;
		String episAdjCrtnLog="",episAdjCrtnLogDtl="",sqlForMapping1995to2008="",sqlFor1995to2008="",fromYear="",toYear="";
		String chkpfid = "",commdata="",revisedFlagQry ="",addcontrisql="", loansQuery = "", advancesQuery = "",sql="", updateloans = "", updateadvances = "", loandate = "", subamt = "", contamt = "", advtransdate = "", advanceAmt = "", monthYear = "",tableName ="";
		String cpfregionstrip="",condition="",preparedString="",preparedStringCond="",dojcpfaccno="",dojemployeeno="",dojempname="",dojstation="",dojregion="",chkForRecord="",insertDummyRecord="";
		String[] cpfregiontrip=null;
		String[] cpfaccnos=null;
		String[] regions=null;
		String[] commondatalst=null;
		String years[] =null;
		int result = 0, loansresult = 0, advancesresult = 0 ,transID=0,batchId=0,insertDummyRecordResult=0; 
		years =reportYear.split("-");
		if(reportYear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else{
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		}
		 
		EmpMasterBean empBean = new EmpMasterBean();
		try {
			con = DBUtils.getConnection();
			st = con.createStatement();
			st1 = con.createStatement();
			this.chkDOJ(con,pfId);
			cpfregionstrip =this.getEmployeeCpfacno(con,pfId);
			String[] pfIDLists = cpfregionstrip.split("=");
			preparedString = commonDAO.preparedCPFString(pfIDLists);
			log.info("preparedString===================="+preparedString+"toYear"+toYear+"fromYear"+fromYear);
			if (Integer.parseInt(years[0]) == 1995) {
				preparedStringCond = "or "+preparedString;
			} 
			if(frmName.equals("adjcorrections")){
				tableName ="EPIS_ADJ_CRTN";
			}else if(frmName.equals("form7/8psadjcrtn")){
				tableName ="EPIS_ADJ_CRTN_FORM78PS";
			}
			//chkpfid = "select * from "+tableName+" where pensionno=" + pfId;
			//rs = st.executeQuery(chkpfid);
			chkpfid = this.chkPfidinAdjCrtnYearWise(pfId,fromYear,toYear,frmName);
			episAdjCrtnLog="insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"+ pfId +",'"+reportYear+"',sysdate)";	
			if (chkpfid.equals("NotExists")) {
				/*if(dataavailbf2008==false){*/
					sql = "insert into "+tableName+" (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
					+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
					+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS,additionalcontri)   (select  "+pfId+" ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
					+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
					+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
					+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS,additionalcontri   from employee_pension_validate where empflag='Y' and (pensionno="
					+ pfId +" "+preparedStringCond+") and monthyear between '" + fromYear + "' and '" + toYear + "')";
			 
					 
					
					episAdjCrtnLogDtl="insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"+username+"','"+ipaddress+"',sysdate)";
					revisedFlagQry =" update "+tableName+"    set  reviseepfemolumentsflag='N' where  pensionno="+pfId+" and empflag='Y' and  monthyear between '" + fromYear + "' and '" + toYear + "'";
					log.info("episAdjCrtnLog"+ episAdjCrtnLog);
					st.addBatch(episAdjCrtnLog);
					log.info("episAdjCrtnLogDtl "+episAdjCrtnLogDtl);
					st.addBatch(episAdjCrtnLogDtl);
					log.info("insertEmployeeTransDataYearWise()----sql---" + sql);		
					st.addBatch(sql);
					log.info("insertEmployeeTransDataYearWise()---revisedFlagQry--" + revisedFlagQry);		
					st.addBatch(revisedFlagQry);
					int insertCount[]=st.executeBatch();
					log.info("insertCount  "+insertCount.length);
					st=null;
					st = con.createStatement();
					loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
						+ pfId+" and loandate between '" + fromYear + "' and '" + toYear + "'";
					log.info("insertEmployeeTransDataYearWise()---loansQuery--" + loansQuery);				 
					rs1 = st.executeQuery(loansQuery);
					if (Integer.parseInt(years[0])>=2015) {

					st = null;
					st = con.createStatement();
					addcontrisql = " update epis_adj_crtn c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
							+ pfId+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+ pfId+" and monthyear ='01-May-2015'";
					log.info("----------addcontrisql-------------" + addcontrisql);
					rs5 = st.executeQuery(addcontrisql);
					}
				while (rs1.next()) {
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
					updateloans = "update  "+tableName+"   set  pfwsub="
							+ subamt + ", pfwcontri=" + contamt
							+ " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ loandate + "'";
				 
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy')='"+ loandate + "'";
					rs4 = st.executeQuery(chkForRecord);
					if(rs4.next()){
						loansresult = st.executeUpdate(updateloans);
					}else{
						insertDummyRecord= " insert into epis_adj_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and monthyear between  '"+fromYear+"' and '" + toYear + "'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and monthyear between '"+fromYear+"' and '" + toYear + "'))";       
						 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
						 loansresult = st.executeUpdate(updateloans);
					}
					log.info("insertEmployeeTransDataYearWise()---updateloans---"+ updateloans);
				}
				advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
						+ pfId+"  and advtransdate between '" + fromYear + "' and '" + toYear + "'";
				log
						.info("-insertEmployeeTransDataYearWise()-----advancesQuery-------"
								+ advancesQuery);
				st = con.createStatement();
				rs2 = st.executeQuery(advancesQuery);
				while (rs2.next()) {
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
					updateadvances = "update  "+tableName+"   set    advance ="
							+ advanceAmt + " where pensionno=" + pfId
							+ " and  to_char(monthyear,'MON-yyyy')='"
							+ advtransdate + "'";
					 
					
					chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and   to_char(monthyear,'MON-yyyy')='"+ advtransdate + "'";
					rs4 = st.executeQuery(chkForRecord);
					log.info("chkForRecord-----"+ chkForRecord);
					if(rs4.next()){
						advancesresult = st.executeUpdate(updateadvances);
						 
					}else{
						insertDummyRecord= " insert into epis_adj_crtn (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
										+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtn where pensionno ="+pfId+" and monthyear between  '"+fromYear+"' and '" + toYear + "'"
										+" and rowid =(select max(rowid) from epis_adj_crtn where pensionno ="+pfId+" and monthyear between '"+fromYear+"' and '" + toYear + "'))";       
						insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
						log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
						advancesresult = st.executeUpdate(updateadvances);
					}
					log.info("insertEmployeeTransDataYearWise()------updateadvances----"+ updateadvances);
				}
				 
			} else {
				String loggeridseq="select loggerid from epis_adj_crtn_log where pensionno="+pfId+" and adjobyear='"+reportYear+"'";
				int	logid=0;
				log.info("loggeridseq "+loggeridseq);
				rs3=st.executeQuery(loggeridseq);
				if(rs3.next()){
					logid=Integer.parseInt(rs3.getString("loggerid"));
					log.info("logid  test"+logid);
				}else{
					st=null;
					st=con.createStatement();
					episAdjCrtnLog="insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"+ pfId +",'"+reportYear+"',sysdate,'This pfid already ported before implmenation logic')";
					st.executeUpdate(episAdjCrtnLog);
					st=null;
					st=con.createStatement();
					rs3=st.executeQuery(loggeridseq);
					if(rs3.next())
					logid=Integer.parseInt(rs3.getString("loggerid"));
					st=null;
				}
				log.info("--------Data already exists----------");
			
		}
		 
			
		} catch (Exception e) {
			e.printStackTrace();
			log.printStackTrace(e);
			log.info("error" + e.getMessage());
		}finally{ 
			try {
				if(rs1!=null){
					rs1.close();
				}
				if(rs2!=null){
					rs2.close();
				}
				if(rs3!=null){
					rs3.close();
				}
				if(rs4!=null){
					rs4.close();
				} 
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			commonDB.closeConnection(null,st,rs);
		}
		log.info("AdjCrtnDAO :insertEmployeeTransDataYearWise() Leaving Method ");
		return result;
	}
	public String  chkPfidinAdjCrtnYearWise(String pfid,String fromYear, String toYear,String frmName){
		String sqlQuery="",chkpfid="",tableName="";		 
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		if(frmName.equals("adjcorrections")){
			tableName ="EPIS_ADJ_CRTN";
		}else if(frmName.equals("form7/8psadjcrtn")){
			tableName ="EPIS_ADJ_CRTN_FORM78PS";
		}
		sqlQuery = "select * from "+tableName+" where   pensionno= "+ pfid+" and monthyear between '"+fromYear+"' and  '"+toYear+"'";
		log.info("--chkPfidinAdjCrtnYearWise()---"+sqlQuery);
		try {
			 
			con = DBUtils.getConnection(); 
			st = con.createStatement();
			rs  = st.executeQuery(sqlQuery);
			if(rs.next()){ 
				chkpfid="Exists"; 
			}else{
				chkpfid="NotExists";
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
		 
		return chkpfid;
	}
	public ArrayList getApprovedRecords(String employeeNo){
		int count = 0;
		Connection con= null;
		Statement st = null;
		ResultSet rs = null;			 
		String selectQuery = "",verifiedBy="",orderby="",dynamicSelectQry="";
		ArrayList al = new ArrayList();
		StringBuffer whereClause = new StringBuffer();
		StringBuffer query = new StringBuffer();
		EmpMasterBean  empBean = null;
		selectQuery="select * from v_emp_crnt_dtl";
		if(employeeNo != null){
			log.info("whereClause");
			whereClause.append(" where");
			whereClause.append("  pensionno="+employeeNo);
			
			
		}
		query.append(selectQuery);
		if(employeeNo.equals("")){
			
		}else{
			query.append(whereClause);
		}
		
		
		orderby=" order by pensionno, adjobyear  Desc";
		query.append(orderby);		 
		dynamicSelectQry = query.toString();
		log.info("ApprovedSearch=========="+dynamicSelectQry);	 
		try{
			con = DBUtils.getConnection();
			st = con.createStatement(); 
			rs = st.executeQuery(dynamicSelectQry);
			while (rs.next()) {
				empBean = new EmpMasterBean();
				if (rs.getString("PENSIONNO") != null) {
					empBean.setPfid(rs.getString("PENSIONNO"));
				} else {
					empBean.setPfid("0");
				}
				
				if (rs.getString("EMPLOYEENAME") != null) {
					empBean.setEmpName(rs.getString("EMPLOYEENAME"));
				} else {
					empBean.setEmpName("0");
				} 
				if (rs.getString("appstation") != null) {
					empBean.setStation(rs.getString("appstation"));
				} else {
					empBean.setStation("");
				} 
				if (rs.getString("appregion") != null) {
					empBean.setRegion(rs.getString("appregion"));
				} else {
					empBean.setRegion("");
				} 
			
				if (rs.getString("dateofbirth") != null) {
					empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
							.getDate("dateofbirth")));
				} else {
					empBean.setDateofBirth("---");
				}
				if (rs.getString("dateofjoining") != null) {
					empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
							.getDate("dateofjoining")));
				} else {
					empBean.setDateofJoining("---");
				}
				if (rs.getString("APPROVEDSTATUS") != null) {
					verifiedBy = rs.getString("APPROVEDSTATUS") ;
					empBean.setVerifiedBy(verifiedBy);
				} else{
					verifiedBy ="---";
				}
				if (rs.getString("APPROVEDUSERS") != null) {
					empBean.setUserName(rs.getString("APPROVEDUSERS")) ;
				} else{
					empBean.setUserName("");
				}
			
				if (rs.getString("ADJPENSIONCONTRI")!= null) {
					empBean.setPensionTot(rs.getString("ADJPENSIONCONTRI")) ;
				} else{
					empBean.setPensionTot("");
				}
				if (rs.getString("ADJEMPSUB")!= null) {
					empBean.setEmpSubTot(rs.getString("ADJEMPSUB")) ;
				} else{
					empBean.setEmpSubTot("");
				}
				if (rs.getString("ADJAAICONTRI")!= null) {
					empBean.setAaiContriTot(rs.getString("ADJAAICONTRI")) ;
				} else{
					empBean.setAaiContriTot("");
				}
				if (rs.getString("adjobyear") != null) {
					empBean.setReportYear(rs.getString("adjobyear"));
				} else{
					empBean.setReportYear("");
				}
				if (rs.getString("trackingid") != null) {
					empBean.setTrackingId(rs.getString("trackingid"));
				} else{
					empBean.setTrackingId("");
				}
				if (rs.getString("frozen") != null) {
					empBean.setFrozen(rs.getString("frozen"));
				} else{
					empBean.setFrozen("");
				}
				if (rs.getString("blocked") != null) {
					empBean.setBlock(rs.getString("blocked"));
				} else{
					empBean.setBlock("");
				}
				if (rs.getString("remarks") != null) {
					empBean.setNotes(rs.getString("remarks"));
				} else{
					empBean.setNotes("");
				}
				if(!(empBean.getVerifiedBy().equals("BLOCKED")&&empBean.getBlock().equals("N"))){
				
					al.add(empBean);
				}
				 
			
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

//new
public int updateApprovedRecord(String empserialNo,String trackId,String notes,String statusType,String status,String loginUserId,String adjobYear) {

	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String updateQry="",insertQry="",fronzenStatus="",blockedStatus="",update="";
	int result = 0 ,transId;
	try {
		con = DBUtils.getConnection();
		st = con.createStatement(); 
		
		transId = this.getTransIdForAdjTransactions(con); 			
//		if( statusType.equals("FROZEN")){
//			fronzenStatus=status;
//		 update="	FROZEN='"+status+"'";
//		}
		if( statusType.equals("BLOCKED")){
			 update="	BLOCKED='"+status+"',verifiedby=''";
		}
		if(status.equals("N")){
			statusType="Released";
		}
		 log.info(" ==transId==" + transId);	
		 insertQry ="insert into epis_adj_crtn_transactions (pensionno, trackingid,transid,approvedtype,approveddate,appstation,appregion,empsubscription,empsubint,aaicontri,aaicontriint,pcprincipal,pcinterest,signpath,designation,aprvdsignname,adjobyear,approvedby,remarks,SCREEN_BLOCKEDFLAG) (select pensionno,trackingid,crtn_trans_id.nextval,'"+statusType+"',sysdate, appstation,appregion,empsubscription,empsubint,aaicontri,aaicontriint,pcprincipal,pcinterest,'','','',adjobyear,'"+loginUserId+"','"+notes+"','S' from epis_adj_crtn_transactions where trackingid='"+trackId+"' and pensionno='"+empserialNo+"' and adjobyear='"+adjobYear+"' and rowid=(select max(rowid) from epis_adj_crtn_transactions where trackingid='"+trackId+"' and pensionno='"+empserialNo+"' and adjobyear='"+adjobYear+"'))";
		 log.info(" ==insertQry==" + insertQry);	 
		 result=st.executeUpdate(insertQry);
		
		updateQry = " update epis_adj_crtn_pfid_tracking set  REMARKS='"+notes+"',"+update+", UPDATEDDATE= sysdate where pensionno ="+empserialNo+" and TRACKINGID='"+trackId+"'";
			log.info("AdjCrtnDAO::updateApprovedRecord()==updateQry==" + updateQry);
			
			st.executeUpdate(updateQry);
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return result;
}

public ArrayList getBlockedPfidsReport(){
	int count = 0;
	Connection con= null;
	Statement st = null;
	ResultSet rs = null;			 
	String selectQuery = "",verifiedBy="",orderby="",dynamicSelectQry="";
	ArrayList al = new ArrayList();
	StringBuffer whereClause = new StringBuffer();
	StringBuffer query = new StringBuffer();
	EmpMasterBean  empBean = null;
	selectQuery="select ts.pensionno as pensionno,ts.remarks as remarks,ts.apporvedby as approvedby,ts.username as blockedby,to_date(ts.approveddate, 'dd/MON/yyyy') as approveddate,ts.approvedtype as approvedtype,ts.appstation as appstation,ts.appregion as appregion,ts.adjobyear as adjobyear,(ts.pcinterest + ts.pcprincipal) as ADJPENSIONCONTRI,(ts.aaicontri + ts.aaicontriint) as ADJAAICONTRI,(ts.empsubscription + ts.empsubint) as ADJEMPSUB,info.employeename as employeename,info.dateofbirth as dateofbirth,info.dateofjoining as dateofjoining,info.dateofseperation_reason as dateofseperation_reason from employee_personal_info info,(select  distinct(s.pensionno),s.remarks,u.username,t.apporvedby,s.aprvdsignname,s.approvedtype,to_char(s.approveddate, 'dd/MON/yyyy') as approveddate,s.appstation,s.appregion,s.pcprincipal,s.pcinterest,s.adjobyear,s.aaicontri,s.aaicontriint,s.empsubscription,s.empsubint from epis_adj_crtn_transactions  s,epis_adj_crtn_pfid_tracking t,employee_user u where s.trackingid = t.trackingid and  s.approvedby=u.userid and t.blocked = 'Y' and s.approvedtype='BLOCKED') ts where info.pensionno = ts.pensionno  order by ts.pensionno, ts.adjobyear, ts.approvedtype Desc";
	
	log.info("selectQuery=========="+selectQuery);	 
	try{
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
			if (rs.getDate("approveddate") != null) {
				empBean.setApprovedDate(CommonUtil.converDBToAppFormat(rs.getDate("approveddate")));
			} else {
				empBean.setApprovedDate("---");
			}
			
			if (rs.getString("EMPLOYEENAME") != null) {
				empBean.setEmpName(rs.getString("EMPLOYEENAME"));
			} else {
				empBean.setEmpName("0");
			} 
			if (rs.getString("appstation") != null) {
				empBean.setStation(rs.getString("appstation"));
			} else {
				empBean.setStation("---");
			} 
			if (rs.getString("appregion") != null) {
				empBean.setRegion(rs.getString("appregion"));
			} else {
				empBean.setRegion("---");
			} 
		
			if (rs.getString("dateofbirth") != null) {
				empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
						.getDate("dateofbirth")));
			} else {
				empBean.setDateofBirth("---");
			}
			if (rs.getString("dateofjoining") != null) {
				empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
						.getDate("dateofjoining")));
			} else {
				empBean.setDateofJoining("---");
			}
			
			if (rs.getString("blockedby") != null) {
				empBean.setUserName(rs.getString("blockedby")) ;
			} else{
				empBean.setUserName("---");
			}
		
			if (rs.getString("ADJPENSIONCONTRI")!= null) {
				empBean.setPensionTot(rs.getString("ADJPENSIONCONTRI")) ;
			} else{
				empBean.setPensionTot("");
			}
			if (rs.getString("ADJEMPSUB")!= null) {
				empBean.setEmpSubTot(rs.getString("ADJEMPSUB")) ;
			} else{
				empBean.setEmpSubTot("");
			}
			if (rs.getString("ADJAAICONTRI")!= null) {
				empBean.setAaiContriTot(rs.getString("ADJAAICONTRI")) ;
			} else{
				empBean.setAaiContriTot("");
			}
			if (rs.getString("adjobyear") != null) {
				empBean.setReportYear(rs.getString("adjobyear"));
			} else{
				empBean.setReportYear("");
			}
			
			if (rs.getString("remarks") != null) {
				empBean.setNotes(rs.getString("remarks"));
			} else{
				empBean.setNotes("--");
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
public EmpMasterBean getImpCalCForm2Data(String pensionno,String frmName,String adjobyear,String form2id){
	log.info("frmName"+frmName+form2id+adjobyear);
	int count = 0;
	Connection con= null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	String selectQuery = "",sql="",selsql="",adjyears="",insertQry="",checkUniqueId="",updateQry="",form2id1="",adjobYears="",adjobYears1="",checkCHQApprovedPfid="";
	String []reportYears={};
	ArrayList al = new ArrayList();
	StringBuffer whereClause = new StringBuffer();
	StringBuffer query = new StringBuffer();
	EmpMasterBean  empBean = null;
	//String const_adjobyear = FORM2_CURRENT_ADJOBYEAR;
	try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		st1 = con.createStatement(); 
	if(!form2id.equals("")){
		checkUniqueId=this.checkUniqueId(form2id);
		if(checkUniqueId.equals("EXIST")){
			selectQuery="select w.pensionno as pensionno,w.cpfacno as cpfacno,w.form2id as form2id,w.employeeno as employeeno,w.employeename as employeename,w.desegnation as desegnation,w.dateofbirth as dateofbirth,w.dateofjoining as dateofjoining,w.airportcode as airportcode,w.fhname as fhname,w.remarks as remarks,w.region as region,w.pensiontotal as pensiontotal,w.empsub as empsub,w.aaicontri as aaicontri,w.pensionintrst as pensionintrst,w.empsubintrst as empsubintrst,w.aaicontriintrst as aaicontriintrst,p.adjprocessedyear as adjprocessedyear,p.jvno as jvno from epis_adj_crtn_form2 w,epis_adj_crtn_pfid_tracking p where w.form2id = '"+form2id+"' and w.adjobyears=p.adjobyear and w.pensionno=p.pensionno";
		}
	}else{
		if(adjobyear.equals("1995-2008")){
			  sql = "insert into epis_adj_crtn_form2(pensionno,cpfacno, form2id,employeeno,employeename,desegnation, dateofbirth,dateofjoining, airportcode, fhname, aaicontri,aaicontriintrst,region, adjobyears,remarks) (select info.pensionno as pensionno,info.cpfacno as cpfacno,'IMP-' || tab1.pensionno || to_char(tab1.appdate, 'ddMMyyHHMISS') as id,info.employeeno as employeeno,info.employeename as employeename,info.desegnation as desegnation,info.dateofbirth as dateofbirth,info.dateofjoining as dateofjoining,info.airportcode as airportcode,info.fhname as fhname, tab1.pcprincipal as pentot,tab1.pcinterest as pensionintrst,info.region as region,'"+adjobyear+"','"+adjobyear+"'  from (select  -(d.pcprincipal) as pcprincipal,-(d.pcinterest) as pcinterest , d.approveddate as appdate, pensionno    from epis_adj_crtn_transactions d   where adjobyear='"+adjobyear+"'   and pensionno = '"+pensionno+"'   and approvedtype = 'Approved') tab1, employee_personal_info info   where info.pensionno = tab1.pensionno)";
		}else{
			  sql = "insert into epis_adj_crtn_form2(pensionno,cpfacno,form2id,employeeno,employeename,desegnation,dateofbirth,dateofjoining,  airportcode,fhname, pensiontotal,empsub, aaicontri, region, pensionintrst, empsubintrst, aaicontriintrst, adjobyears,remarks) (select info.pensionno as pensionno,info.cpfacno as cpfacno,'IMP-' || tab1.pensionno || to_char(tab1.appdate, 'ddMMyyHHMISS') as id,info.employeeno as employeeno,info.employeename as employeename,info.desegnation as desegnation,info.dateofbirth as dateofbirth,info.dateofjoining as dateofjoining,info.airportcode as airportcode,info.fhname as fhname,tab1.pcprincipal as pentot,tab1.empsub as empsub,tab1.aaicontri as aaicontri,info.region as region, tab1.pcinterest as pensionintrst, tab1.empsubint as empsubintrst,tab1.aaicontriint as aaicontriintrst,'"+adjobyear+"','"+adjobyear+"'  from (select d.empsubscription as empsub, d.pcprincipal as pcprincipal,d.aaicontri as aaicontri,d.empsubint as empsubint,0 AS pcinterest,d.aaicontriint as aaicontriint, pensionno,d.approveddate as appdate from epis_adj_crtn_transactions d where adjobyear='"+adjobyear+"'and pensionno = '"+pensionno+"' and approvedtype = 'Approved') tab1, employee_personal_info info  where info.pensionno = tab1.pensionno)";
		}
		log.info("ifffffffffql"+sql);
		st1.execute(sql);
		selectQuery="select w.pensionno as pensionno,w.cpfacno as cpfacno,w.form2id as form2id,w.employeeno as employeeno,w.employeename as employeename,w.desegnation as desegnation,w.dateofbirth as dateofbirth,w.dateofjoining as dateofjoining,w.airportcode as airportcode,w.fhname as fhname,w.remarks as remarks,w.region as region,w.pensiontotal as pensiontotal,w.empsub as empsub,w.aaicontri as aaicontri,w.pensionintrst as pensionintrst,w.empsubintrst as empsubintrst,w.aaicontriintrst as aaicontriintrst, p.adjprocessedyear as adjprocessedyear,p.jvno as jvno from epis_adj_crtn_form2 w,epis_adj_crtn_pfid_tracking p where  w.adjobyears=p.adjobyear and w.pensionno=p.pensionno and w.adjobyears ='"+adjobyear+"'  and w.pensionno = '"+pensionno+"' order by w.lastactive desc";
		
				
	}
	
		
		log.info("selectQuery"+selectQuery);
		rs = st.executeQuery(selectQuery);
		if(rs.next()) {
			empBean = new EmpMasterBean();
			if (rs.getString("FORM2ID") != null) {
				form2id=rs.getString("FORM2ID");
				log.info("Form2=====id"+form2id);
				empBean.setForm2id(rs.getString("FORM2ID"));
			} else {
				empBean.setForm2id("---");
			}
			if (rs.getString("PENSIONNO") != null) {
				empBean.setPfid(rs.getString("PENSIONNO"));
			} else {
				empBean.setPfid("---");
			}
			if (rs.getString("CPFACNO") != null) {
				empBean.setCpfAcNo(rs.getString("CPFACNO"));
			} else {
				empBean.setCpfAcNo("---");
			}
			if (rs.getString("EMPLOYEENO") != null) {
				empBean.setEmpNumber(rs.getString("EMPLOYEENO"));
			} else {
				empBean.setEmpNumber("---");
			} 
			
			if (rs.getString("DESEGNATION") != null) {
				empBean.setDesegnation(rs.getString("DESEGNATION"));
			} else {
				empBean.setDesegnation("---");
			}
			if (rs.getString("EMPLOYEENAME") != null) {
				empBean.setEmpName(rs.getString("EMPLOYEENAME"));
			} else {
				empBean.setEmpName("---");
			} 
			if (rs.getString("AIRPORTCODE") != null) {
				empBean.setStation(rs.getString("AIRPORTCODE"));
			} else {
				empBean.setStation("---");
			} 
			if (rs.getString("REGION") != null) {
				empBean.setRegion(rs.getString("REGION"));
			} else {
				empBean.setRegion("---");
			} 
		
			if (rs.getString("DATEOFBIRTH") != null) {
				empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
						.getDate("dateofbirth")));
			} else {
				empBean.setDateofBirth("---");
			}
			if (rs.getString("DATEOFJOINING") != null) {
				empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
						.getDate("DATEOFJOINING")));
			} else {
				empBean.setDateofJoining("---");
			}
			if (rs.getString("FHNAME") != null) {
				empBean.setFhName(rs
						.getString("FHNAME"));
			} else {
				empBean.setFhName("---");
			}

			if (rs.getString("PENSIONTOTAL")!= null) {
				empBean.setPensionTot(rs.getString("PENSIONTOTAL")) ;
			} else{
				empBean.setPensionTot("0");
			}
			if (rs.getString("EMPSUB")!= null) {
				empBean.setEmpSubTot(rs.getString("EMPSUB")) ;
			} else{
				empBean.setEmpSubTot("0");
			}
			if (rs.getString("AAICONTRI")!= null) {
				empBean.setAaiContriTot(rs.getString("AAICONTRI")) ;
			} else{
				empBean.setAaiContriTot("0");
			}
			if (rs.getString("PENSIONINTRST")!= null) {
				empBean.setPensionInt(rs.getString("PENSIONINTRST")) ;
			} else{
				empBean.setPensionInt("0");
			}
			if (rs.getString("EMPSUBINTRST")!= null) {
				empBean.setEmpSubInt(rs.getString("EMPSUBINTRST")) ;
			} else{
				empBean.setEmpSubInt("0");
			}
			if (rs.getString("AAICONTRIINTRST")!= null) {
				empBean.setAaiContriInt(rs.getString("AAICONTRIINTRST")) ;
			} else{
				empBean.setAaiContriInt("0");
			}
			
			/*if (rs.getString("adjobyears")!= null){
				empBean.setRemarks(rs.getString("adjobyears"));
			}else{
				empBean.setRemarks("---");
			}
			*/
			if (rs.getString("REMARKS") != null) {
				empBean.setRemarks(rs.getString("REMARKS"));
			} else{
				empBean.setRemarks("---");
			}
			if(rs.getDate("adjprocessedyear")!=null){
				empBean.setReportYear(CommonUtil.converDBToAppFormat(rs.getDate("adjprocessedyear")));
			}else{
				empBean.setReportYear("---");
			}
			if (rs.getString("jvno") != null) {
				empBean.setJvNo(rs.getString("jvno"));
			} else{
				empBean.setJvNo("---");
			}
			
			
			st1 = con.createStatement(); 
			
			updateQry="update epis_adj_crtn_pfid_tracking set  form2id='"+form2id+"',form2status='B' where pensionno='"+pensionno+"' and form2status not in ('Y','B','M') and ADJOBYEAR ='"+adjobyear+"'";
		 log.info("updateQry============"+updateQry);
		
		 count=st1.executeUpdate(updateQry);
		
		 st1.close();
		}
		log.info("count: " +count);
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

	return empBean;
}
//new



public ArrayList searchFor12MnthStatemntCtrn(String userRegion, String userStation,
		String profileType,  String accountType,
		String employeeNo) {
	int count = 0;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String selectQuery = "", verifiedBy = "",approvedStage="";
	ArrayList al = new ArrayList();
	EmpMasterBean empBean = null;
	selectQuery = this.buildSearchQueryFor12MnthStatementCrtn(userRegion, userStation,
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
			if (rs.getString("CHKPFIDIN78PS") != null) {
				empBean.setChkPfidIn78PS(rs.getString("CHKPFIDIN78PS")) ;
			} else{
				empBean.setChkPfidIn78PS("");
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
// For 58 Years Reached employees also included
public String buildSearchQueryFor12MnthStatementCrtn(String userRegion,
		String userStation, String profileType, 
		String accountType, String employeeNo) {
	StringBuffer whereClause = new StringBuffer();
	StringBuffer query = new StringBuffer();
	String dynamicQuery = "", orderBy = "", sqlQuery = "";

	 
  
 	sqlQuery =" SELECT PERSNL.* , TRACK.PENSIONNO AS   CHKPFIDIN78PS  from (SELECT EPI.PENSIONNO AS PENSIONNO, EPI.EMPLOYEENAME AS EMPLOYEENAME,   EPI.CPFACNO AS CPFACNO,"
 		      +" EPI.REGION AS REGION,EPI.AIRPORTCODE AS AIRPORTCODE, EPI.DESEGNATION AS DESEGNATION,  EPI.DATEOFBIRTH AS DATEOFBIRTH,  EPI.DATEOFJOINING AS DATEOFJOINING,"
 		      +" EPI.WETHEROPTION AS WETHEROPTION,  EPI.DATEOFSEPERATION_REASON AS SEPERATIONREASON,   TO_CHAR(EPI.DATEOFSEPERATION_DATE, 'dd-Mon-yyyy') AS SEPERATIONDATE"
 		      +" FROM EMPLOYEE_PERSONAL_INFO EPI WHERE EPI.EMPFLAG = 'Y' AND( (EPI.DATEOFSEPERATION_DATE IS NOT NULL AND EPI.DATEOFSEPERATION_REASON IS NOT NULL)"
              +" OR (add_months(EPI.DATEOFBIRTH, 696) <= sysdate))) PERSNL,(SELECT * FROM EPIS_78PS_CRTN_PFID_TRACKING) TRACK   WHERE PERSNL.PENSIONNO =TRACK.PENSIONNO(+) ";
	
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
public int insertRecordFor78PSAdjCtrnTracking(Connection con,
		String empserialNO, String cpfAccno, String adjOBYear,
		  String username, String ipaddress)
		throws InvalidDataException {

	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "", insertQry = "", adjProcessedYear="01-Apr-2011";
	int result = 0, trackingId = 0;
	try {

		st = con.createStatement();
		 
			sqlQuery = "select pensionno from EPIS_78PS_CRTN_PFID_TRACKING where   pensionno="
					+ empserialNO ;	 
		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			 
		} else {

			trackingId = this.getPfidTrackingIdFor7PS(con);
			insertQry = "insert into EPIS_78PS_CRTN_PFID_TRACKING(PENSIONNO,CPFACNO,ADJOBYEAR,USERNAME,COMPUTERNAME,UPDATEDDATE,TRACKINGID)values ("
					+ empserialNO
					+ ",'"
					+ cpfAccno
					+ "','"
					+ adjOBYear
					+ "','"					 
					+ username
					+ "','"
					+ ipaddress
					+ "',sysdate," + trackingId + ")";

			log
					.info("AdjCrtnDAO::insertRecordFor78PSAdjCtrnTracking()==insertQry=="
							+ insertQry);
			result = st.executeUpdate(insertQry);
		}

	} catch (SQLException e) {
		throw new InvalidDataException(e.getMessage());
	} catch (Exception e) {
		throw new InvalidDataException(e.getMessage());
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return result;
}
public String chkPfidinAdjCrtn78PSTracking(String pfid, String frmName) {
	String sqlQuery="",chkpfid="",tableName="" ;	
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	int i=0; 
		tableName = "EPIS_78PS_CRTN_PFID_TRACKING";
	 
	sqlQuery = "select *  from " + tableName + " where   pensionno= " + pfid ;
	log.info("--chkPfidinAdjCrtn78PSTracking()---" + sqlQuery);
	try {

		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		if(rs.next()){ 
			chkpfid="Exixts";
		}else{
			chkpfid="NotExists";
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
//By Radha On 30-Apr-2012 Deletion is only for Form2Status 'Y' cases
//By Radha on 2-Apr-2012  For deleting Record at CHQ Approver 
public String getCHQDeletion(String pfid,String reportYear,String frmName, String username,
		String ipaddress, String form2Stats, String chqFlag,String empSubTot, String aaiContriTot,String pensionTot) {
	String message = "";
	String updateEpisAdjCrtnLog = "", insertEpisAdjCrtnLogDtl = "", deleteEpisAdjCrtnTemp = ""  ,fromYear="",toYear="";
	String episAdjCrtnLog ="", episAdjCrtnLogDtl="",updateQry="", chqRemarks="",episAdjCrtnLogNew="";
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	String years[] =null;
	years =reportYear.split("-");
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	 
	try {
		conn = DBUtils.getConnection();
		st = conn.createStatement(); 
		
		chqRemarks="CHQ-"+empSubTot+"-"+aaiContriTot+"-"+pensionTot;
		// Checking for record with reportyear is there r not 
		String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'  and deletedflag='N'"; 
		st1 = conn.createStatement();
		log.info("loggeridseq " + loggeridseq);
		rs = st1.executeQuery(loggeridseq);
		 
		if (rs.next()) {				
			updateEpisAdjCrtnLog = "update epis_adj_crtn_log set deletedflag='Y',remarks=remarks||'"+chqRemarks+"' where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'"; 
			log.info("updateEpisAdjCrtnLog " + updateEpisAdjCrtnLog);
			st.addBatch(updateEpisAdjCrtnLog);
			
			int logid = Integer.parseInt(rs.getString("loggerid"));

			insertEpisAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt,remarks) values ("
					+ logid
					+ ",'"
					+ username
					+ "','"
					+ ipaddress
					+ "',sysdate,'Record Deleted')";
			log.info("insertEpisAdjCrtnLogDtl " + insertEpisAdjCrtnLogDtl);
			st.addBatch(insertEpisAdjCrtnLogDtl);
		}else{
		episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,deletedflag,remarks) values (loggerid_seq.nextval,"
				+ pfid + ",'"+reportYear+"',sysdate,'Y','"+chqRemarks+"')";
		log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
		st.addBatch(episAdjCrtnLog);
		episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt,remarks) values (loggerid_seq.currval,'"
			+ username + "','" + ipaddress + "',sysdate,'Record Deleted')";
		log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
		st.addBatch(episAdjCrtnLogDtl);	
			
		}
		if(form2Stats.equals("Y")){
		updateQry ="update epis_adj_crtn_pfid_tracking  set  frozen='Y' where pensionno="+pfid+" and adjobyear='"+reportYear+"'";
		log.info("updateQry in epis_adj_crtn_pfid_tracking " + updateQry);
		st.addBatch(updateQry);	
		}
		
		
		episAdjCrtnLogNew="insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"+ pfid +",'"+reportYear+"',sysdate)";
		log.info("episAdjCrtnLogNew Entry " + episAdjCrtnLogNew);
		st.addBatch(episAdjCrtnLogNew);	
		
		int count[] = st.executeBatch();
		log.info("count" + count.length);

		message = "Succesfully Deleted"; 
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		commonDB.closeConnection(conn, st1, rs);
		DBUtils.close(conn);
	}
	return message;
}
//new method
public EmpMasterBean getImpCalCForm2CHQData(String pensionno,String adjobyear){
	int count = 0;
	Connection con= null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;			 
	String selectQuery = "",insertQry="",checkUniqueId="",updateQry="",form2id="",adjobYears="",checkCHQApprovedPfid="";
	ArrayList al = new ArrayList();
	StringBuffer whereClause = new StringBuffer();
	StringBuffer query = new StringBuffer();
	EmpMasterBean  empBean = null;
	//String const_adjobyear = FORM2_CURRENT_ADJOBYEAR;
	
	try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		//st1 = con.createStatement();
	
	
		selectQuery="select * from employee_personal_info where pensionno='"+pensionno+"'"; 
	
		
		//st1.close();
		log.info("selectQuery"+selectQuery);
		rs = st.executeQuery(selectQuery);
		if(rs.next()) {
			empBean = new EmpMasterBean();
			
			if (rs.getString("PENSIONNO") != null) {
				empBean.setPfid(rs.getString("PENSIONNO"));
			} else {
				empBean.setPfid("---");
			}
			if (rs.getString("CPFACNO") != null) {
				empBean.setCpfAcNo(rs.getString("CPFACNO"));
			} else {
				empBean.setCpfAcNo("---");
			}
			if (rs.getString("EMPLOYEENO") != null) {
				empBean.setEmpNumber(rs.getString("EMPLOYEENO"));
			} else {
				empBean.setEmpNumber("---");
			} 
			
			if (rs.getString("DESEGNATION") != null) {
				empBean.setDesegnation(rs.getString("DESEGNATION"));
			} else {
				empBean.setDesegnation("---");
			}
			if (rs.getString("EMPLOYEENAME") != null) {
				empBean.setEmpName(rs.getString("EMPLOYEENAME"));
			} else {
				empBean.setEmpName("---");
			} 
			if (rs.getString("AIRPORTCODE") != null) {
				empBean.setStation(rs.getString("AIRPORTCODE"));
			} else {
				empBean.setStation("---");
			} 
			if (rs.getString("REGION") != null) {
				empBean.setRegion(rs.getString("REGION"));
			} else {
				empBean.setRegion("---");
			} 
		
			if (rs.getString("DATEOFBIRTH") != null) {
				empBean.setDateofBirth(CommonUtil.converDBToAppFormat(rs
						.getDate("dateofbirth")));
			} else {
				empBean.setDateofBirth("---");
			}
			if (rs.getString("DATEOFJOINING") != null) {
				empBean.setDateofJoining(CommonUtil.converDBToAppFormat(rs
						.getDate("DATEOFJOINING")));
			} else {
				empBean.setDateofJoining("---");
			}
			if (rs.getString("FHNAME") != null) {
				empBean.setFhName(rs
						.getString("FHNAME"));
			} else {
				empBean.setFhName("---");
			}
			

			
			
		}
		log.info("count: " +count);
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

	return empBean;
}
//By Radha on 05-Jan-2012 For Restricting the automatic Calc of Pc for all the Months i.e., from Apr 2008 to till(pensionContributionProcess2008to11ForAdjCRTN)
// By Radha On 13-Dec-2010 for rounding the calculated PC for emolument Months
public double  pensionContributionProcess2008to11ForAdjCRTN(String region, String Pfid,String selectedMonth) {
	log.info("AdjCrtnDao :pentionContributionProcess2008to11ForAdjCRTN() entering method");
	String message = "";
	ArrayList xlsDataList = new ArrayList();	 
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	Statement st2 = null;
	EmpMasterBean bean = null;
	String tempInfo[] = null;
	String tempData = "";
	FileWriter fw = null;
	char[] delimiters = { '"', ',', '\'', '*', ',' };
	String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
	String uploadFilePath = "";
	String xlsEmpName = "", monthYear = "", dateofbirth = "", pensionno = "";
	PreparedStatement pst = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	String foundSalaryDate = "", retirementDate = "";
	boolean addflag = false;
	boolean updateflag = false;
	int addBatchcount[] = { 0 };
	double returnPC=0.0;
	try {
		conn = commonDB.getConnection();
		st = conn.createStatement();
		st1 = conn.createStatement();
		st2 = conn.createStatement();
		String airportQuery = "select nvl(airportcode,'X')as airportcode from epis_bulk_print_pfids where pensionno='"
				+ Pfid + "'";
		rs1 = st.executeQuery(airportQuery);
		String airportcode = "";
		if (rs1.next()) {
			airportcode = rs1.getString("airportcode");
		}
		CommonUtil cu = new CommonUtil();
		String todate = cu.getDateTime("dd-MMM-yyyy");
		String checkcontributionUpto = "select pensionno,to_char(monthyear,'dd-Mon-yyyy') as monthyear from epis_adj_crtn where pensionno='"
				+ Pfid
				+ "' and monthyear =to_date('"+selectedMonth+"','dd-Mon-yyyy')   and edittrans='N' AND empflag='Y'";
		log.info("---checkcontributionUpto------"+checkcontributionUpto);
		rs = st1.executeQuery(checkcontributionUpto);

		while (rs.next()) {
			double calculatedPension = 0.00, pf = 0.00;
			bean = new EmpMasterBean();

			if (rs.getString("monthyear") != null) {
				monthYear = rs.getString("monthyear").toString();
			} else {
				monthYear = "";
			}
			if (rs.getString("pensionno") != null) {
				pensionno = rs.getString("pensionno").trim();
			} else {
				pensionno = "";
			}

			String transMonthYear = "";

			if (!pensionno.trim().equals("")) {
/*
				String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date(to_char(add_months(TO_DATE('"
					+ monthYear
					+ "'), -1),'dd-Mon-yyyy'),'DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days,"
					+ "(case when to_char(dateofbirth,'dd-Mon-yyyy') like '01-%' then    to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy') when      to_char(dateofbirth,'dd-Mon-yyyy') not like '01-%' then   to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy')  END ) AS calPensionupto from employee_personal_info where to_char(pensionno)='"
					+ pensionno + "'";*/
				
				String checkPFID 	=" SELECT PENSIONNO,dateofbirth,REIREMENTDATE,calPensionupto, wetheroption,   (CASE   WHEN ((to_date(REIREMENTDATE) > to_date(tomonthyear)) or  (to_date(LASTDAY) = to_date(ACTUALREIREMENTDATE) and"
									 +" (TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) >= 0)) THEN  'F'   else     (case   when ((TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) <= 0) then  'NIL'  ELSE 'H' end) END)  AS  CALFLAG "
									 +" FROM (select wetheroption, pensionno,to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy') AS ACTUALREIREMENTDATE,"
									 +" to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy') AS REIREMENTDATE, '"+monthYear+"' as monthyear, to_char(to_date(last_day('"+monthYear+"')), 'dd-Mon-yyyy') as tomonthyear,"
									 +" to_char(dateofbirth, 'dd-Mon-yyyy') as dateofbirth,  trunc(trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1,'MM') AS FIRSTDAY,"
									 +" trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1 AS LASTDAY,"
									 +"(case   when to_char(dateofbirth, 'dd-Mon-yyyy') like '01-%' then  to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy')"
									 +" when to_char(dateofbirth, 'dd-Mon-yyyy') not like '01-%' then  to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy')  END) AS calPensionupto"
									 +" from employee_personal_info  where to_char(pensionno) ='"+pensionno+"')";
				 log.info("-----checkPFID---"+checkPFID);
				rs1 = st.executeQuery(checkPFID);

				if (!rs1.next()) {
					throw new InvalidDataException("PFID "
							+ bean.getPfid().trim() + " doesn't Exist"
							+ " for  Employee " + bean.getEmpName()
							+ ". PFID is Mandatory for All The Employees");
				}
				if (rs1.getString("wetheroption") != null) {
					if(!rs1.getString("wetheroption").trim().equals("A")){
						bean.setWetherOption("B");
					}else{
					bean.setWetherOption(rs1.getString("wetheroption"));
					}
				} else {
					bean.setWetherOption("B");
				}
				if (rs1.getString("REIREMENTDATE") != null) {
					retirementDate = rs1.getString("REIREMENTDATE");
				} else {
					retirementDate = "";
				}
				if (rs1.getString("dateofbirth") != null) {
					dateofbirth = rs1.getString("dateofbirth");
				} else {
					dateofbirth = "";
				}
				 
				String calPensionupto = "",pcCalFlag="";
				if (rs1.getString("calPensionupto") != null) {
					calPensionupto = rs1.getString("calPensionupto");
				} else {
					calPensionupto = "0";
				}
				if (rs1.getString("CALFLAG") != null) {
					pcCalFlag = rs1.getString("CALFLAG");
				} else {
					pcCalFlag = "NIL";
				}
				transMonthYear = commonUtil.converDBToAppFormat(monthYear
						.trim(), "dd-MMM-yyyy", "-MMM-yyyy");
				String emolumentsQuery = "select emoluments,EMPPFSTATUARY,airportcode , emolumentmonths from epis_adj_crtn where pensionno='"
						+ pensionno
						+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
						+ transMonthYear
						+ "' and edittrans='N' and empflag='Y' order by monthyear";
				// log.info(emolumentsQuery);
				rs2 = st.executeQuery(emolumentsQuery);

				// code changed as on 25-Oct-2010
				/*
				 * if (rs2.next()) {
				 * 
				 * if (rs2.getString("emoluments") != null) {
				 * bean.setEmoluments(rs2.getString("emoluments")); } } else
				 * { bean.setEmoluments("0.00"); }
				 */
				while (rs2.next()) {
					if (rs2.getString("EMPPFSTATUARY") != null) {
						bean.setEmployeePF(rs2.getString("EMPPFSTATUARY"));
					} else {
						bean.setEmployeePF("0.00");
					}

					if (rs2.getString("emoluments") != null) {
						bean.setEmoluments(rs2.getString("emoluments"));
					} else {
						bean.setEmoluments("0.00");
					}
					if (rs2.getString("airportcode") != null) {
						bean.setStation(rs2.getString("airportcode"));
					}
					if (rs2.getString("emolumentmonths") != null) {
						bean.setEmolumentMonths(rs2.getString("emolumentmonths"));
					}else{
						bean.setEmolumentMonths("1");
					}
					// log.info("emoluments" + bean.getEmoluments() +
					// "monthYear "+ monthYear);
					Date transdate = null;
					DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					transdate = df.parse(monthYear);
					float cpfarrear = 0;
					String emolumetns = "0";
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String
									.valueOf(Float.parseFloat(bean
											.getEmoluments()) * 12 / 100));
						} else {
							// bean.setEmployeePF(String.valueOf(Float.
							// parseFloat(bean.getEmoluments()) * 10 /
							// 100));
						}
						emolumetns = bean.getEmoluments();
					} else {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String.valueOf(Math
									.round(Double.parseDouble(bean
											.getEmoluments()) * 12 / 100)));
							log.info(" pf is : " + bean.getEmployeePF());
						} else {
							// bean.setEmployeePF(String.valueOf(Math.round(
							// Double.parseDouble(bean.getEmoluments())* 10
							// / 100)));
						}
						emolumetns = String.valueOf(Math.round(Double
								.parseDouble(bean.getEmoluments())));
					}
					// log.info(" pf is : " + bean.getEmployeePF());				 
					calculatedPension = this.calclatedPFForAdjCrtn(monthYear,
							calPensionupto, dateofbirth, String
									.valueOf(Double.parseDouble(bean
											.getEmployeePF()) * 100 / 12),
							bean.getWetherOption(), "",  bean.getEmolumentMonths(), bean
									.getEmployeePF(),pcCalFlag);
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF()
								.toString())
								- calculatedPension;
						// pf = Math.round(pf);
					} else {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF())
								- calculatedPension;

					}

					/*String condition = "";
					if ((bean.getStation() != null && bean.getStation() != "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}*/
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update epis_adj_crtn set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "								 
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ Math.round(Double.parseDouble(bean.getEmployeePF())) + "'";

					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st2.executeUpdate(insertPensionCon);

				}
				log.info("-----pensionno----------"+pensionno);
				if (!pensionno.equals("")) {
					/*String condition = "";
					if ((bean.getStation() != null || bean.getStation() == "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}*/
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update epis_adj_crtn set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "								 
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ Math.round(Double.parseDouble(bean.getEmployeePF())) + "'";
					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st.executeUpdate(insertPensionCon);

				}

			}
			returnPC =  calculatedPension;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		log.printStackTrace(e);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		log.printStackTrace(e);

	} finally {
		try {
			pst.close();
			rs.close();
			rs2.close();
			st1.close();
			st.close();
			conn.close();
		} catch (Exception e) {

		}
	}
return returnPC;
}
public void pensionContributionProcess2008to11For78PsAdjCRTN(String region, String Pfid, String selectedMonth) {
	log.info("AdjCrtnDao :pentionContributionProcess2008to11For78PsAdjCRTN() entering method");
	String message = "";
	ArrayList xlsDataList = new ArrayList();	 
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	Statement st2 = null;
	EmpMasterBean bean = null;
	String tempInfo[] = null;
	String tempData = "";
	FileWriter fw = null;
	char[] delimiters = { '"', ',', '\'', '*', ',' };
	String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
	String uploadFilePath = "";
	String xlsEmpName = "", monthYear = "", dateofbirth = "", pensionno = "";
	PreparedStatement pst = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	String foundSalaryDate = "", retirementDate = "";
	boolean addflag = false;
	boolean updateflag = false;
	int addBatchcount[] = { 0 };
	try {
		conn = commonDB.getConnection();
		st = conn.createStatement();
		st1 = conn.createStatement();
		st2 = conn.createStatement();
		String airportQuery = "select nvl(airportcode,'X')as airportcode from epis_bulk_print_pfids where pensionno='"
				+ Pfid + "'";
		rs1 = st.executeQuery(airportQuery);
		String airportcode = "";
		if (rs1.next()) {
			airportcode = rs1.getString("airportcode");
		}
		CommonUtil cu = new CommonUtil();
		String todate = cu.getDateTime("dd-MMM-yyyy");
		String checkcontributionUpto = "select pensionno,to_char(monthyear,'dd-Mon-yyyy') as monthyear from EPIS_ADJ_CRTN_FORM78PS where pensionno='"
				+ Pfid
				+ "' and monthyear =to_date('"+selectedMonth+"','dd-Mon-yyyy') and edittrans='N' AND empflag='Y'";
		log.info("---checkcontributionUpto------"+checkcontributionUpto);
		rs = st1.executeQuery(checkcontributionUpto);

		while (rs.next()) {
			double calculatedPension = 0.00, pf = 0.00;
			bean = new EmpMasterBean();

			if (rs.getString("monthyear") != null) {
				monthYear = rs.getString("monthyear").toString();
			} else {
				monthYear = "";
			}
			if (rs.getString("pensionno") != null) {
				pensionno = rs.getString("pensionno").trim();
			} else {
				pensionno = "";
			}

			String transMonthYear = "";

			if (!pensionno.trim().equals("")) {

				String checkPFID 	=" SELECT PENSIONNO,dateofbirth,REIREMENTDATE,calPensionupto, wetheroption,   (CASE   WHEN ((to_date(REIREMENTDATE) > to_date(tomonthyear)) or  (to_date(LASTDAY) = to_date(ACTUALREIREMENTDATE) and"
					 +" (TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) >= 0)) THEN  'F'   else     (case   when ((TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) <= 0) then  'NIL'  ELSE 'H' end) END)  AS  CALFLAG "
					 +" FROM (select wetheroption, pensionno,to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy') AS ACTUALREIREMENTDATE,"
					 +" to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy') AS REIREMENTDATE, '"+monthYear+"' as monthyear, to_char(to_date(last_day('"+monthYear+"')), 'dd-Mon-yyyy') as tomonthyear,"
					 +" to_char(dateofbirth, 'dd-Mon-yyyy') as dateofbirth,  trunc(trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1,'MM') AS FIRSTDAY,"
					 +" trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1 AS LASTDAY,"
					 +"(case   when to_char(dateofbirth, 'dd-Mon-yyyy') like '01-%' then  to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy')"
					 +" when to_char(dateofbirth, 'dd-Mon-yyyy') not like '01-%' then  to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy')  END) AS calPensionupto"
					 +" from employee_personal_info  where to_char(pensionno) ='"+pensionno+"')";

				 log.info("-----checkPFID---"+checkPFID);
				rs1 = st.executeQuery(checkPFID);

				if (!rs1.next()) {
					throw new InvalidDataException("PFID "
							+ bean.getPfid().trim() + " doesn't Exist"
							+ " for  Employee " + bean.getEmpName()
							+ ". PFID is Mandatory for All The Employees");
				}
				if (rs1.getString("wetheroption") != null) {
					if(!rs1.getString("wetheroption").trim().equals("A")){
						bean.setWetherOption("B");
					}else{
					bean.setWetherOption(rs1.getString("wetheroption"));
					}
				} else {
					bean.setWetherOption("B");
				}
				if (rs1.getString("REIREMENTDATE") != null) {
					retirementDate = rs1.getString("REIREMENTDATE");
				} else {
					retirementDate = "";
				}
				if (rs1.getString("dateofbirth") != null) {
					dateofbirth = rs1.getString("dateofbirth");
				} else {
					dateofbirth = "";
				}					 
				String calPensionupto = "",pcCalFlag="";
				if (rs1.getString("calPensionupto") != null) {
					calPensionupto = rs1.getString("calPensionupto");
				} else {
					calPensionupto = "0";
				}
				if (rs1.getString("CALFLAG") != null) {
					pcCalFlag = rs1.getString("CALFLAG");
				} else {
					pcCalFlag = "NIL";
				}
				transMonthYear = commonUtil.converDBToAppFormat(monthYear
						.trim(), "dd-MMM-yyyy", "-MMM-yyyy");
				String emolumentsQuery = "select emoluments,EMPPFSTATUARY,airportcode from EPIS_ADJ_CRTN_FORM78PS where pensionno='"
						+ pensionno
						+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
						+ transMonthYear
						+ "' and edittrans='N' and empflag='Y' order by monthyear";
				// log.info(emolumentsQuery);
				rs2 = st.executeQuery(emolumentsQuery);

				// code changed as on 25-Oct-2010
				/*
				 * if (rs2.next()) {
				 * 
				 * if (rs2.getString("emoluments") != null) {
				 * bean.setEmoluments(rs2.getString("emoluments")); } } else
				 * { bean.setEmoluments("0.00"); }
				 */
				while (rs2.next()) {
					if (rs2.getString("EMPPFSTATUARY") != null) {
						bean.setEmployeePF(rs2.getString("EMPPFSTATUARY"));
					} else {
						bean.setEmployeePF("0.00");
					}

					if (rs2.getString("emoluments") != null) {
						bean.setEmoluments(rs2.getString("emoluments"));
					} else {
						bean.setEmoluments("0.00");
					}
					if (rs2.getString("airportcode") != null) {
						bean.setStation(rs2.getString("airportcode"));
					}

					// log.info("emoluments" + bean.getEmoluments() +
					// "monthYear "+ monthYear);
					Date transdate = null;
					DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					transdate = df.parse(monthYear);
					float cpfarrear = 0;
					String emolumetns = "0";
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String
									.valueOf(Float.parseFloat(bean
											.getEmoluments()) * 12 / 100));
						} else {
							// bean.setEmployeePF(String.valueOf(Float.
							// parseFloat(bean.getEmoluments()) * 10 /
							// 100));
						}
						emolumetns = bean.getEmoluments();
					} else {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String.valueOf(Math
									.round(Double.parseDouble(bean
											.getEmoluments()) * 12 / 100)));
							log.info(" pf is : " + bean.getEmployeePF());
						} else {
							// bean.setEmployeePF(String.valueOf(Math.round(
							// Double.parseDouble(bean.getEmoluments())* 10
							// / 100)));
						}
						emolumetns = String.valueOf(Math.round(Double
								.parseDouble(bean.getEmoluments())));
					}
					// log.info(" pf is : " + bean.getEmployeePF());				 
					calculatedPension = this.calclatedPFForAdjCrtn(monthYear,
							calPensionupto, dateofbirth, String
									.valueOf(Double.parseDouble(bean
											.getEmployeePF()) * 100 / 12),
							bean.getWetherOption(), "", "1", bean
									.getEmployeePF(),pcCalFlag);
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF()
								.toString())
								- calculatedPension;
						// pf = Math.round(pf);
					} else {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF())
								- calculatedPension;

					}

					String condition = "";
					if ((bean.getStation() != null && bean.getStation() != "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update EPIS_ADJ_CRTN_FORM78PS set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "
							+ condition
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ bean.getEmployeePF() + "'";

					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st2.executeUpdate(insertPensionCon);

				}
				log.info("-----pensionno----------"+pensionno);
				if (!pensionno.equals("")) {
					String condition = "";
					if ((bean.getStation() != null || bean.getStation() == "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update EPIS_ADJ_CRTN_FORM78PS set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "
							+ condition
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ bean.getEmployeePF() + "'";
					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st.executeUpdate(insertPensionCon);

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
		try {
			pst.close();
			rs.close();
			rs2.close();
			st1.close();
			st.close();
			conn.close();
		} catch (Exception e) {

		}
	}

}
public FinacialDataBean getEmolumentsBeanFor78PsAdjCrtn(Connection con, String fromDate,
		String cpfaccno, String employeeno, String region, String Pensionno) {

	String foundEmpFlag = "";
	Statement st = null;
	ResultSet rs = null;
	FinacialDataBean bean = new FinacialDataBean();
	try {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

		Date transdate = df.parse(fromDate);

		String transMonthYear = commonUtil.converDBToAppFormat(fromDate
				.trim(), "dd-MMM-yyyy", "-MMM-yy");
		String query = "";
		if (Pensionno == "" || transdate.before(new Date("31-Mar-2008"))) {
			query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(DUEEMOLUMENTS,0) AS  DUEEMOLUMENTS,nvl(ARREARAMOUNT,0)AS ARREARAMOUNT from EPIS_ADJ_CRTN_FORM78PS where to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear
					+ "' and (( cpfaccno='" + cpfaccno + "' and region='" + region + "') or pensionno='" + Pensionno + "')   and empflag='Y' ";
			
		} else {
			query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(DUEEMOLUMENTS,0) AS DUEEMOLUMENTS ,nvl(ARREARAMOUNT,0) AS ARREARAMOUNT from EPIS_ADJ_CRTN_FORM78PS where to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear
					+ "' and pensionno='"
					+ Pensionno
					+ "'  and empflag='Y' ";
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
	return bean;
}
//By Radha for getting due emoluments data 
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
public String getForm2id(String pensionno,String adjobyear){
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "",form2id="";
	try {
		con = commonDB.getConnection();
		sqlQuery = "select form2id from epis_adj_crtn_form2   where pensionno = '"+pensionno+"' and adjobyears='"+adjobyear+"'";
				
		log.info("==============getForm2id=============="+ sqlQuery);
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);

		if (rs.next()) {
			if(rs.getString("form2id")!=null){
			form2id=rs.getString("form2id");	
			}else{
				form2id="";
			}
		}
		
		log.info("form2id : "+form2id);
		
	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return form2id;
}
private String checkUniqueId(String form2id){
	String result="";
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "";
	try {
		con = commonDB.getConnection();
		sqlQuery = "select 'X' as flag from epis_adj_crtn_form2   where form2id = '"+form2id+"'";
				
		log.info("==============checkuUniqueid=============="+ sqlQuery);
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);

		if (rs.next()) {
			result="EXIST";
			
		}else{
			result="NOTEXIST";
		}
		
		log.info("result : "+result);
		
	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return result;
}

 
public String getCHQApproverEditStatus(String pensionno,String reportYear,String frmName){
	String result="";
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "",editStatus="";
	try {
		con = commonDB.getConnection();
		sqlQuery = "select remarks as remarks  from epis_adj_crtn_pc_totals_diff   where pensionno = "+pensionno+" and adjobyear='"+reportYear+"' and remarks = 'After Approved'";
				
		log.info("==============getCHQApproverEditStatus=============="+ sqlQuery);
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);

		if (rs.next()) {			
			editStatus = "CHQEdited";  
		} else{
			editStatus = "CHQNotEdited";  
		}
		
		log.info("editStatus : "+editStatus);
		
	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return editStatus;
}

public String chkPfidStatusInAdjCrtn(String empserialNO) {
	log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtn Entering method");		 
	Statement st = null;
	ResultSet rs = null;
	Connection con = null;
	String selectSQL = "", verifiedby = "",pfidStatus="";
	int i = 0;
	selectSQL = "select distinct verifiedby as verifiedby  from epis_adj_crtn_pfid_tracking where pensionno= "+ empserialNO +" and blocked ='N' and updateddate<='27-Aug-2016'";

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


public String chkPfidStatusInAdjCrtnForPCReport(String empserialNO) {
	log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtnForPCReport Entering method");		 
	Statement st = null;
	ResultSet rs = null;
	Connection con = null;
	String selectSQL = "", verifiedby = "",pfidStatus="";
	int i = 0;
	selectSQL = "select distinct verifiedby as verifiedby  from epis_adj_crtn_pfid_tracking where adjobyear='1995-2008' and pensionno= "+ empserialNO +" and blocked ='N' and updateddate<='27-Aug-2016'";

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
	log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtnForPCReport leaving method");
	return pfidStatus;
}

public String chkPfidStatusInAdjCrtnForPCReport1(String empserialNO) {
	log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtnForPCReport Entering method");		 
	Statement st = null;
	ResultSet rs = null;
	Connection con = null;
	String selectSQL = "", verifiedby = "",pfidStatus="";
	int i = 0;
	selectSQL = "select distinct verifiedby as verifiedby  from epis_adj_crtn_pfid_tracking where adjobyear='1995-2008' and pensionno= "+ empserialNO +" and blocked ='N' ";

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
	log.info("AdjCrtnDAO: chkPfidStatusInAdjCrtnForPCReport leaving method");
	return pfidStatus;
}


public ArrayList getAdjCrtnFinalizedTotals(String pfId, String adjOBYear) {
	log.info("AdjCrtnDAO:getAdjCrtnFinalizedTotals-- Entering Method");

	String sqlQuery = "", prefixWhereClause = "";
	EmployeePensionCardInfo data = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	ArrayList finalizedinfo = null;

	sqlQuery = " select pensionno, nvl(empsubscription, 0) as empsub, nvl(empsubint, 0) as empsubint, nvl(aaicontri, 0) as aaicontri, nvl(aaicontriint, 0) as aaicontriint,"
				+" nvl(pcprincipal, 0) as pcprincipal, nvl(pcinterest,0) as pcinterest  from epis_adj_crtn_transactions where approvedtype = 'Approved'  and pensionno ="+pfId+"   and adjobyear  = '"+adjOBYear+"'";  
	log.info("sql query " + sqlQuery);
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery.toString());
		finalizedinfo = new ArrayList();
		while (rs.next()) {
			 
			data = new EmployeePensionCardInfo();

			if (rs.getString("pensionno") != null) {
				data.setPensionNo(rs.getString("pensionno"));
			} else {
				data.setPensionNo("");
			}
			 
			if (rs.getString("empsub") != null) {
				data.setEmpSub(rs.getString("empsub"));
			} else {
				data.setEmpSub("0");
			}
			if (rs.getString("empsubint") != null) {
				data.setEmpSubInterest(rs.getString("empsubint"));
			} else {
				data.setEmpSubInterest("0");
			}

			if (rs.getString("aaicontri") != null) {
				data.setAaiContri(rs.getString("aaicontri"));
			} else {
				data.setAaiContri("0");
			}

			if (rs.getString("aaicontriint") != null) {
				data.setAaiContriInterest(rs.getString("aaicontriint"));
			} else {
				data.setAaiContriInterest("");
			}
			if (rs.getString("pcprincipal") != null) {
				data.setPensionTotal(rs.getString("pcprincipal"));
			} else {
				data.setPensionTotal("0");
			}

			if (rs.getString("pcinterest") != null) {
				data.setPensionInterest(rs.getString("pcinterest"));
			} else {
				data.setPensionInterest("0");
			}
			  
			finalizedinfo.add(data);
		}

		log.info("getAdjCrtnFinalizedTotals list size " + finalizedinfo.size());

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return finalizedinfo;
}

// added by mohan for impact pc report.
public String chkPfidinAdjCrtnforPc(String pfid, String frmName) {
	String sqlQuery = "", chkpfid = "", tableName = "";
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	if (frmName.equals("adjcorrections")) {
		tableName = "EPIS_ADJ_CRTNPC";
	} else if (frmName.equals("form7/8psadjcrtn")) {
		tableName = "EPIS_ADJ_CRTN_FORM78PS";
	}
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

public String chkPfidinAdjCrtnTrackingForUploadforPc(String pfid, String frmName) {
	String sqlQuery = "", chkpfid = "", tableName = "";
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	if (frmName.equals("adjcorrections")) {
		tableName = "epis_adj_crtn_pfid_trackingpc";
	}
	sqlQuery = "select * from " + tableName + " where   pensionno= " + pfid
			+ " and  DATAMAPPED='U'";
	log.info("--chkPfidinAdjCrtnTrackingForUpload()---" + sqlQuery);
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
public ArrayList updatePCAdjCorrectionsforPc(String fromDate, String toDate,
		String region, String airportcode, String empserialNO,
		String cpfaccnostrip, String regionstrip) {

	ArrayList penContHeaderList = new ArrayList();
	ArrayList penContDataList = new ArrayList();
	String mappingflag = "true";
	penContHeaderList = this.PCHeaderInfoForAdjCrtnforPc(region, airportcode,
			empserialNO);
	String adjOBYear = "", adjOBRemarks = "";
	try {
		String getToYear = commonUtil.converDBToAppFormat(toDate,
				"dd-MMM-yyyy", "yyyy");
		log.info("getToYear " + getToYear);

	} catch (InvalidDataException e1) {
		// TODO Auto-generated catch block
		log.printStackTrace(e1);
	}

	String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "";
	String[] cpfaccnos = new String[10];
	String[] dupCpfaccnos = new String[10];
	String[] regions = new String[10];
	String[] empPensionList = null;
	String[] pensionInfo = null;
	CommonDAO commonDAO = new CommonDAO();
	String pensionList = "", tempCPFAcno = "", tempRegion = "", dateOfRetriment = "", getMnthYear = "";
	double totalEmoluments = 0.0, pfStaturary = 0.0, totalPension = 0.0, empVpf = 0.0, principle = 0.0, interest = 0.0, pfContribution = 0.0;
	double grandEmoluments = 0.0, grandCPF = 0.0, grandPension = 0.0, grandPFContribution = 0.0;
	double cpfInterest = 0.0, pensionInterest = 0.0, pfContributionInterest = 0.0;
	double grandCPFInterest = 0.0, calOptionRevised = 0.0, grandPensionInterest = 0.0, grandPFContributionInterest = 0.0;
	double cumPFStatury = 0.0, cumPension = 0.0, cumPfContribution = 0.0;
	double cpfOpeningBalance = 0.0, penOpeningBalance = 0.0, pfOpeningBalance = 0.0, rateOfInterest = 0.0;

	DecimalFormat df = new DecimalFormat("#########0");
	ArrayList penConReportList = new ArrayList();
	log.info("=================Update PC Report Starts===========");
	log.info("Header Size" + penContHeaderList.size());
	String dupCpf = "", dupRegion = "", countFlag = "";
	int yearCount = 0;
	int totalRecordIns = 0, inserted = 0;
	Connection con = null;
	try {
		con = DBUtils.getConnection();
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

			penContBean.setPensionNo(commonDAO.getPFID(penContBean
					.getEmployeeNM(), penContBean.getEmpDOB(), commonUtil
					.leadingZeros(5, penContHeaderBean.getEmpSerialNo())));

			empSerialNumber = penContHeaderBean.getEmpSerialNo();

			double totalAAICont = 0.0, calCPF = 0.0, calPens = 0.0;
			ArrayList employeFinanceList = new ArrayList();
			// String preparedString = penContHeaderBean.getPrepareString();
			try {
				dateOfRetriment = commonDAO.getRetriedDate(penContBean
						.getEmpDOB());
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList rateList = new ArrayList();
			String findFromDate = "", monthInfo = "", findMnt = "";
			findFromDate = this.compareTwoDates(penContHeaderBean
					.getDateOfEntitle(), fromDate);
			log.info("Find From Date" + findFromDate);
			// the below line for table check which table to hit the
			// recoverie data

			cpfacno = penContHeaderBean.getCpfacno();
			empRegion = penContHeaderBean.getEmpRegion();

			cpfaccnos = cpfacno.split("=");
			regions = empRegion.split("=");
			String preparedString = commonDAO.preparedCPFString(cpfaccnos,
					regions);
			String empCpfaccno = "";
			log.info("preparedString is " + preparedString);
			if (cpfaccnos.length >= 1) {
				for (int k = 0; k < cpfaccnos.length; k++) {
					region = regions[k];
					empCpfaccno = cpfaccnos[k];
				}
			}

			penContBean.setEmpRegion(region);
			penContBean.setEmpCpfaccno(empCpfaccno);

			boolean recoverieTable = false;
			pensionList = this.getEmployeePensionInfoForAdjCrtnforPc(
					preparedString, fromDate, toDate, penContHeaderBean
							.getWhetherOption(), dateOfRetriment,
					penContBean.getEmpDOB(), empserialNO);

			String rateFromYear = "", rateToYear = "", checkMnthDate = "", dispFromYear = "", monthInterestInfo = "", dispFromMonth = "";
			ArrayList blockList = new ArrayList();
			boolean dispYearFlag = false, yearBreak = false;
			boolean rateFlag = false;
			if (!pensionList.equals("")) {
				grandEmoluments = 0.0;
				grandCPF = 0.0;
				grandPension = 0.0;
				grandCPFInterest = 0.0;
				grandPensionInterest = 0.0;
				grandPFContribution = 0.0;
				grandPFContributionInterest = 0.0;
				cumPFStatury = 0.0;
				cumPension = 0.0;
				cumPfContribution = 0.0;
				totalEmoluments = 0;
				pfStaturary = 0;
				totalPension = 0;
				pfContribution = 0;
				cpfInterest = 0;
				pensionInterest = 0;
				pfContributionInterest = 0;
				cpfOpeningBalance = 0.0;
				penOpeningBalance = 0.0;
				pfOpeningBalance = 0.0;
				empPensionList = pensionList.split("=");
				String penTempMnthInfo = empPensionList[empPensionList.length - 1];
				String[] penMnthInfo = penTempMnthInfo.split(",");
				log.info(penMnthInfo[20]);
				blockList = commonDAO.getMonthList(con, penMnthInfo[20]);

				if (empPensionList != null) {
					for (int r = 0; r < empPensionList.length; r++) {
						TempPensionTransBean bean = new TempPensionTransBean();
						tempPensionInfo = empPensionList[r];
						pensionInfo = tempPensionInfo.split(",");
						bean.setMonthyear(pensionInfo[0]);
						dispFromMonth = commonUtil.converDBToAppFormat(bean
								.getMonthyear(), "dd-MMM-yyyy", "MMM");
						if (dispYearFlag == false
								&& dispFromMonth.equals("Mar")) {
							if (dispFromYear.equals("")) {
								dispFromYear = commonUtil
										.converDBToAppFormat(bean
												.getMonthyear(),
												"dd-MMM-yyyy", "yy");
							}

							getMnthYear = commonUtil.converDBToAppFormat(
									bean.getMonthyear(), "dd-MMM-yyyy",
									"MM-yy");

							monthInterestInfo = commonDAO.getBlockYear(
									getMnthYear, blockList);
							// log.info("Month Info"+monthInterestInfo);
							String[] monthInterestList = monthInterestInfo
									.split(",");

							monthInfo = monthInterestList[1];

							rateOfInterest = new Double(
									monthInterestList[0]).doubleValue();

							dispYearFlag = true;
						}
						bean.setEmoluments(pensionInfo[1]);
						bean.setCpf(pensionInfo[2]);
						bean.setEmpVPF(pensionInfo[3]);
						bean.setEmpAdvRec(pensionInfo[4]);
						bean.setEmpInrstRec(pensionInfo[5]);
						bean.setStation(pensionInfo[6]);
						bean.setPensionContr(pensionInfo[7]);
						calCPF = Math.round(Double.parseDouble(bean
								.getCpf()));
						bean.setDeputationFlag(pensionInfo[19].trim());
						DateFormat dateFormat1 = new SimpleDateFormat(
								"dd-MMM-yy");

						Date transdate1 = dateFormat1.parse(pensionInfo[0]);

						if (transdate1.before(new Date("31-Mar-08"))
								&& (bean.getDeputationFlag().equals("N") || bean
										.getDeputationFlag().equals(""))) {
							calPens = Math.round(Double
									.parseDouble(pensionInfo[7]));

							totalAAICont = calCPF - calPens;
						} else {
							calPens = Math.round(Double
									.parseDouble(pensionInfo[12]));
							bean.setPensionContr(pensionInfo[12]);

							if (pensionInfo[21].trim().equals("N")) {
								totalAAICont = calCPF - calPens;
							} else {
								calOptionRevised = Double
										.parseDouble(pensionInfo[21]);
								totalAAICont = calPens - calOptionRevised;
							}

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
						findMnt = commonUtil.converDBToAppFormat(bean
								.getMonthyear(), "dd-MMM-yyyy", "MM-yy");

						totalEmoluments = new Double(df
								.format(totalEmoluments
										+ Math.round(Double
												.parseDouble(bean
														.getEmoluments()))))
								.doubleValue();
						pfStaturary = new Double(df.format(pfStaturary
								+ Math.round(Double.parseDouble(bean
										.getCpf())))).doubleValue();
						cumPFStatury = cumPFStatury + pfStaturary;
						empVpf = new Double(df.format(empVpf
								+ Math.round(Double.parseDouble(bean
										.getEmpVPF())))).doubleValue();
						principle = new Double(df.format(principle
								+ Math.round(Double.parseDouble(bean
										.getEmpAdvRec())))).doubleValue();
						interest = new Double(df.format(interest
								+ Math.round(Double.parseDouble(bean
										.getEmpInrstRec())))).doubleValue();
						totalPension = new Double(df.format(totalPension
								+ Math.round(Double.parseDouble(bean
										.getPensionContr()))))
								.doubleValue();
						cumPension = cumPension + totalPension;

						pfContribution = new Double(df
								.format(pfContribution
										+ Math.round(Double
												.parseDouble(bean
														.getAaiPFCont()))))
								.doubleValue();
						cumPfContribution = cumPfContribution
								+ pfContribution;

						if (findMnt.equals(monthInfo)) {
							yearBreak = true;
						}
						if (yearBreak == true) {
							cpfInterest = Math.round((cumPFStatury
									* rateOfInterest / 100) / 12)
									+ Math.round(cpfOpeningBalance
											* rateOfInterest / 100);
							pensionInterest = Math.round((cumPension
									* rateOfInterest / 100) / 12)
									+ Math.round(penOpeningBalance
											* rateOfInterest / 100);
							pfContributionInterest = Math
									.round((cumPfContribution
											* rateOfInterest / 100) / 12)
									+ Math.round(pfOpeningBalance
											* rateOfInterest / 100);
							yearBreak = false;
							// log.info(bean.getMonthyear()+"cpfInterest"+
							// cpfInterest+"cumPFStatury"+cumPFStatury);
							cpfOpeningBalance = Math.round(pfStaturary
									+ cpfInterest
									+ Math.round(cpfOpeningBalance));
							penOpeningBalance = Math.round(totalPension
									+ pensionInterest
									+ Math.round(penOpeningBalance));
							pfOpeningBalance = Math.round(pfContribution
									+ pfContributionInterest
									+ Math.round(pfOpeningBalance));
							grandEmoluments = grandEmoluments
									+ totalEmoluments;
							grandCPF = grandCPF + pfStaturary;
							grandPension = grandPension + totalPension;
							grandPFContribution = grandPFContribution
									+ pfContribution;

							grandCPFInterest = grandCPFInterest
									+ cpfInterest;
							grandPensionInterest = grandPensionInterest
									+ pensionInterest;
							grandPFContributionInterest = grandPFContributionInterest
									+ pfContributionInterest;
							cumPFStatury = 0.0;
							cumPension = 0.0;
							cumPfContribution = 0.0;
							totalEmoluments = 0;
							pfStaturary = 0;
							totalPension = 0;
							pfContribution = 0;
							cpfInterest = 0;
							pensionInterest = 0;
							pfContributionInterest = 0;

							dispYearFlag = false;

						}

					}

				}

				// Need to implement the totals
				penContDataList.add(new Double(grandEmoluments));
				penContDataList.add(new Double(grandCPF));
				penContDataList.add(new Double(grandCPFInterest));
				penContDataList.add(new Double(grandPension));
				penContDataList.add(new Double(grandPensionInterest));
				penContDataList.add(new Double(grandPFContribution));
				penContDataList
						.add(new Double(grandPFContributionInterest));

			}
		}

	} catch (SQLException se) {
		log.printStackTrace(se);
	} catch (Exception ex) {
		log.printStackTrace(ex);
	} finally {
		commonDB.closeConnection(con, null, null);
	}
	log.info("penContDataList============================================"
			+ penContDataList.size());
	return penContDataList;
}
public int savePrePctoalsTempforPc(String empserialNO, String adjObYear,
		ArrayList prePcTotals) {
	String insertQuery = "", deleteQuery = "", transID = "", trackingQuery = "";
	Connection con = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	int result = 0;
	double EmolumentsTot = 0.0, cpfTotal = 0.0, cpfIntrst = 0.0, pensionTotal = 0.0, pensionIntrst = 0.0, pfContri = 0.0, pfContriIntrst = 0.0;
	EmolumentsTot = ((Double) prePcTotals.get(0)).doubleValue();
	cpfTotal = ((Double) prePcTotals.get(1)).doubleValue();
	cpfIntrst = ((Double) prePcTotals.get(2)).doubleValue();
	pensionTotal = ((Double) prePcTotals.get(3)).doubleValue();
	pensionIntrst = ((Double) prePcTotals.get(4)).doubleValue();
	pfContri = ((Double) prePcTotals.get(5)).doubleValue();
	pfContriIntrst = ((Double) prePcTotals.get(6)).doubleValue();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();

		transID = this.getTransidSequence(con);
		String selectADJPrvPCTotals = "select count(*) as count from epis_adjcrtn_prvpctotals_temp where pensionno='"
				+ empserialNO + "' and  ADJOBYEAR='" + adjObYear + "'";
		log.info("--savePrvPctoals----selectADJTotalsDiff--"
				+ selectADJPrvPCTotals);
		rs1 = st.executeQuery(selectADJPrvPCTotals);
		while (rs1.next()) {
			int count = rs1.getInt(1);
			if (count == 0) {
				trackingQuery = "select * from epis_adj_crtn_pfid_trackingpc where pensionno="
						+ empserialNO;
				insertQuery = "insert into epis_adjcrtn_prvpctotals_tpc(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST , PFTOTAL,PFINTEREST ,CREATIONDATE,remarks) values('"
						+ empserialNO
						+ "','"
						+ transID
						+ "','"
						+ adjObYear
						+ "','"
						+ EmolumentsTot
						+ "','"
						+ cpfTotal
						+ "','"
						+ cpfIntrst
						+ "','"
						+ pensionTotal
						+ "','"
						+ pensionIntrst
						+ "','"
						+ pfContri
						+ "','"
						+ pfContriIntrst
						+ "', sysdate,'Through Mapping Screen')";
			} else {
				insertQuery = "update epis_adjcrtn_prvpctotals_tpc  set EMOLUMENTS='"
						+ EmolumentsTot
						+ "',CPFTOTAL='"
						+ cpfTotal
						+ "',CPFINTEREST='"
						+ cpfIntrst
						+ "',PENSIONTOTAL='"
						+ pensionTotal
						+ "',PENSIONINTEREST='"
						+ pensionIntrst
						+ "',PFTOTAL='"
						+ pfContri
						+ "',PFINTEREST='"
						+ pfContriIntrst
						+ "',CREATIONDATE= sysdate  where  pensionno='"
						+ empserialNO
						+ "' and   ADJOBYEAR='"
						+ adjObYear
						+ "'";

			}
		}
		log.info("----savePrePctoalsTemp---insertQuery " + insertQuery);
		result = st.executeUpdate(insertQuery);
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);

	}
	return result;
}

public int saveCurrPctoalsforPc(String empserialNO, String adjObYear,
		ArrayList currPcTotals) {
	String insertOrUpdateQuery = "", deleteQuery = "", transID = "", trackingQuery = "";
	String insertPreviousTotal = "";
	Connection con = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs3 = null;
	ResultSet rs2 = null;
	int result = 0;
	double EmolumentsTot = 0.0, cpfTotal = 0.0, cpfIntrst = 0.0, pensionTotal = 0.0, pensionIntrst = 0.0, pfContri = 0.0, pfContriIntrst = 0.0;
	EmolumentsTot = ((Double) currPcTotals.get(0)).doubleValue();
	cpfTotal = ((Double) currPcTotals.get(1)).doubleValue();
	cpfIntrst = ((Double) currPcTotals.get(2)).doubleValue();
	pensionTotal = ((Double) currPcTotals.get(3)).doubleValue();
	pensionIntrst = ((Double) currPcTotals.get(4)).doubleValue();
	pfContri = ((Double) currPcTotals.get(5)).doubleValue();
	pfContriIntrst = ((Double) currPcTotals.get(6)).doubleValue();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();

		// transID = this.getTransidSequence(con);
		String selectinsertPreviousTotal = "select *  from epis_adj_crtn_prv_pc_totalspc where pensionno='"
				+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "'";
		rs3 = st.executeQuery(selectinsertPreviousTotal);

		// int count1 = rs3.getInt(1);
		if (rs3.next()) {
			insertPreviousTotal = "update  epis_adj_crtn_prv_pc_totalspc latesttbl set(EMOLUMENTS,CPFTOTAL,CPFINTEREST,PENSIONTOTAL,PENSIONINTEREST,PFTOTAL,PFINTEREST,EMPSUB,EMPSUBINTEREST,AAICONTRI,AAICONTRIINTEREST,LASTACTIVE,ADJEMPSUBINTRST,ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,remarks)=(select EMOLUMENTS,CPFTOTAL,CPFINTEREST,PENSIONTOTAL,PENSIONINTEREST,PFTOTAL,PFINTEREST,EMPSUB,EMPSUBINTEREST,AAICONTRI,AAICONTRIINTEREST,sysdate,ADJEMPSUBINTRST,ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,remarks from epis_adjcrtn_prvpctotals_temp temp where  latesttbl.pensionno=temp.pensionno ) where latesttbl.pensionno='"
					+ empserialNO
					+ "'and latesttbl.ADJOBYEAR ='"
					+ adjObYear + "'";
		} else {

			insertPreviousTotal = "insert into epis_adj_crtn_prv_pc_totalspc (select * from EPIS_ADJCRTN_PRVPCTOTALS_TPC where pensionno='"
					+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "')";
		}
		log.info("insertPreviousTotal" + insertPreviousTotal);
		result = st.executeUpdate(insertPreviousTotal);

		String transIDQry = "select TRANSID from epis_adj_crtn_prv_pc_totalspc where pensionno='"
				+ empserialNO + "' and ADJOBYEAR ='" + adjObYear + "'";
		rs2 = st.executeQuery(transIDQry);
		if (rs2.next()) {
			transID = rs2.getString("TRANSID");
		}

		String selectADJCurrentPCTotals = "select count(*) as count from epis_adj_crtn_curnt_pc_topc where pensionno='"
				+ empserialNO + "' and  ADJOBYEAR='" + adjObYear + "'";
		log.info("--saveCurrPctoals----selectADJCurrentPCTotals--"
				+ selectADJCurrentPCTotals);
		rs = st.executeQuery(selectADJCurrentPCTotals);
		while (rs.next()) {
			int count = rs.getInt(1);
			if (count == 0) {
				// trackingQuery="select * from epis_adj_crtn_pfid_tracking
				// where pensionno="+ empserialNO;
				insertOrUpdateQuery = "insert into epis_adj_crtn_curnt_pc_topc(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST , PFTOTAL,PFINTEREST ,CREATIONDATE,REMARKS) values('"
						+ empserialNO
						+ "','"
						+ transID
						+ "','"
						+ adjObYear
						+ "','"
						+ EmolumentsTot
						+ "','"
						+ cpfTotal
						+ "','"
						+ cpfIntrst
						+ "','"
						+ pensionTotal
						+ "','"
						+ pensionIntrst
						+ "','"
						+ pfContri
						+ "','"
						+ pfContriIntrst + "', sysdate,'Through Mapping')";
			} else {
				insertOrUpdateQuery = "update epis_adj_crtn_curnt_pc_topc  set EMOLUMENTS='"
						+ EmolumentsTot
						+ "',CPFTOTAL='"
						+ cpfTotal
						+ "',CPFINTEREST='"
						+ cpfIntrst
						+ "',PENSIONTOTAL='"
						+ pensionTotal
						+ "',PENSIONINTEREST='"
						+ pensionIntrst
						+ "',PFTOTAL='"
						+ pfContri
						+ "',PFINTEREST='"
						+ pfContriIntrst
						+ "',CREATIONDATE= sysdate  where  pensionno='"
						+ empserialNO
						+ "' and   ADJOBYEAR='"
						+ adjObYear
						+ "'";

			}
		}

		log.info("-----saveCurrPctoals--insertOrUpdateQuery "
				+ insertOrUpdateQuery);
		result = st.executeUpdate(insertOrUpdateQuery);

		result = this.saveCurrDifftoals(empserialNO, adjObYear);

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);

	}
	return result;
}
public ArrayList getPCAdjDiffforPc(String pfId, String adjOBYear) {
	log.info("AdjCrtnDAO :getPCAdjDiff() Entering Method ");
	Connection con = null;

	String[] reportYears = { "1995-2008", "2008-2009", "2009-2010", "2010-2011","2011-2012","2012-2013","2013-2014","2014-2015","2015-2016","2016-2017","2017-2018","2018-2019","2019-2020","2020-2021","2021-2022"};
	String approvedStage="",sql="";
	EmployeePensionCardInfo bean = null;
	ResultSet rs = null;
	ArrayList pcTotalDiffAmnt = new ArrayList();

	try {
		con = DBUtils.getConnection();
		Statement st = con.createStatement();

		for (int i = 0; i < reportYears.length; i++) {
			if(reportYears[i].equals("1995-2008")){
				  sql = " select diff.ADJOBYEAR AS ADJOBYEAR, nvl(EMOLUMENTS, 0.0) as EMOLUMENTS, nvl(CPFTOTAL, 0.0) as CPFTOTAL,(nvl(PENSIONTOTAL, 0.0) + nvl(PENSIONINTRST, 0.0)) as PENSIONTOTAL,"
		       		+" nvl(PFTOTAL, 0.0) as PFTOTAL, (nvl(EMPSUB, 0.0) + nvl(EMPSUBINTRST, 0.0)) as EMPSUBTOT,  (nvl(AAICONTRI, 0.0) + NVL(AAICONTRIINTRST, 0.0)) as AAICONTRITOT,tracking.verifiedby as APPROVEDTYPE,tracking.FORM2STATUS as  FORM2STATUS,tracking.FROZEN as FROZEN,tracking.form2id as form2id"
		       		+" from epis_adj_crtn_pc_totals_diffpc diff,epis_adj_crtn_pfid_trackingpc tracking where diff.pensionno = tracking.pensionno and  diff.adjobyear = tracking.adjobyear  and diff.pensionno= "+pfId+"  and diff.adjobyear ='"+reportYears[i]+"'";
		 
			}else{
				  sql = " select diff.ADJOBYEAR AS ADJOBYEAR, nvl(EMOLUMENTS, 0.0) as EMOLUMENTS, nvl(CPFTOTAL, 0.0) as CPFTOTAL,(nvl(PENSIONTOTAL, 0.0) - nvl(ADJPENSIONCONTRIINTRST, 0.0)) as PENSIONTOTAL,"
		       		+" nvl(PFTOTAL, 0.0) as PFTOTAL, (nvl(EMPSUB, 0.0) + nvl(EMPSUBINTRST, 0.0)) as EMPSUBTOT,  (nvl(AAICONTRI, 0.0) + NVL(AAICONTRIINTRST, 0.0)) as AAICONTRITOT,tracking.verifiedby as APPROVEDTYPE,tracking.FORM2STATUS as  FORM2STATUS,tracking.FROZEN as FROZEN,tracking.form2id as form2id"
		       		+" from epis_adj_crtn_pc_totals_diffpc diff,epis_adj_crtn_pfid_trackingpc tracking where diff.pensionno = tracking.pensionno and  diff.adjobyear = tracking.adjobyear  and diff.pensionno= "+pfId+"  and diff.adjobyear ='"+reportYears[i]+"'";
		 
			}
			
			log.info("sql " + sql);

			rs = st.executeQuery(sql);
			if (rs.next()) {

				bean = new EmployeePensionCardInfo();
				if (rs.getString("ADJOBYEAR") != null) {
					bean.setReportYear(rs.getString("ADJOBYEAR"));
				} else {
					bean.setReportYear("---");
				}
				if (rs.getString("form2id") != null) {
					bean.setForm2id(rs.getString("form2id")) ;
				} else{
					bean.setForm2id("");
				}
				if (rs.getString("EMOLUMENTS") != null) {
					bean.setEmolumentsDiff(rs.getString("EMOLUMENTS"));
				} else {
					bean.setEmolumentsDiff("0.00");
				}
				if (rs.getString("CPFTOTAL") != null) {
					bean.setCpfTotDiff(rs.getString("CPFTOTAL"));
				} else {
					bean.setCpfTotDiff("0.00");
				}
				if (rs.getString("PENSIONTOTAL") != null) {
					bean.setPensionContriTotDiff(rs
							.getString("PENSIONTOTAL"));
				} else {
					bean.setPensionContriTotDiff("0.00");
				}
				if (rs.getString("PFTOTAL") != null) {
					bean.setPFTotDiff(rs.getString("PFTOTAL"));
				} else {
					bean.setPFTotDiff("0.00");
				}
				if (rs.getString("EMPSUBTOT") != null) {
					bean.setEmpSubTotDiff(rs.getString("EMPSUBTOT"));
				} else {
					bean.setEmpSubTotDiff("0.00");
				}
				if (rs.getString("AAICONTRITOT") != null) {
					bean.setAaiContriDiff(rs.getString("AAICONTRITOT"));
				} else {
					bean.setAaiContriDiff("0.00");
				}
				if (rs.getString("APPROVEDTYPE") != null) {
					approvedStage = rs.getString("APPROVEDTYPE");
					if(approvedStage.equals("Initial,Approved")){
						approvedStage="Approved";
						bean.setApprovedStage(approvedStage);  
					}else{
						bean.setApprovedStage(approvedStage); 
					}
				} else {
					bean.setApprovedStage("");
				}
				
				if (rs.getString("FORM2STATUS") != null) {
					bean.setForm2Status(rs.getString("FORM2STATUS")) ;
				} else{
					bean.setForm2Status("N");
				}
				if (rs.getString("FROZEN") != null) {
					bean.setFrozen(rs.getString("FROZEN")) ;
				} else{
					bean.setFrozen("N");
				}
				pcTotalDiffAmnt.add(bean);

			} else {
				bean = new EmployeePensionCardInfo();

				bean.setReportYear((String) reportYears[i]);
				bean.setEmolumentsDiff("0.00");
				bean.setCpfTotDiff("0.00");
				bean.setPensionContriTotDiff("0.00");
				bean.setPFTotDiff("0.00");
				bean.setEmpSubTotDiff("0.00");
				bean.setAaiContriDiff("0.00");
				bean.setApprovedStage("");
				bean.setForm2Status("N");
				bean.setFrozen("N");
				pcTotalDiffAmnt.add(bean);

			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		log.printStackTrace(e);
		log.info("error" + e.getMessage());
	}
	log.info("AdjCrtnDAO :getPCAdjDiff() Leaving Method ");
	return pcTotalDiffAmnt;
}
public String chkPfidinAdjCrtnTrackingforPc(String pfid, String frmName) {
	String sqlQuery="",chkpfidyears="",tableName="",adjobyear="";	
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	int i=0;
	if (frmName.equals("adjcorrections")) {
		tableName = "epis_adj_crtn_pfid_trackingpc";
	}
	sqlQuery = "select adjobyear  from " + tableName + " where   pensionno= " + pfid
			+ " and (DATAMODIFIED='E' or DATAMAPPED='M' or DATAMAPPED='U')";
	log.info("--chkPfidinAdjCrtnTracking()---" + sqlQuery);
	try {

		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while(rs.next()){ 
			if(rs.getString("adjobyear")!=null){
				adjobyear = rs.getString("adjobyear");
				 i++;
				 if(i==1){
					 chkpfidyears = adjobyear;
				 }else{
					 chkpfidyears = chkpfidyears + ":" + adjobyear;
				 }
			}
			log.info("--chkpfidyears --"+chkpfidyears);
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

	return chkpfidyears;
}
public String getNotFinalizedAdjObYearforPc(String pfid, String frmName) {
	String sqlQuery="",adjobyear="",adjObYears="";	
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	int i = 0;		 
	sqlQuery = "select distinct adjobyear from epis_adj_crtn_prv_pc_totalspc eacprv, epis_adj_crtn_emol_log_temppc temp"
			+ "  where eacprv.transid =temp.transid and eacprv.pensionno =temp.pensionno  and temp.pensionno ="
			+ pfid;
	log.info("AdjCrtnDAO::getNotFinalizedAdjObYear()---" + sqlQuery);
	try {

		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while(rs.next()){ 
			 
			if(rs.getString("adjobyear")!=null){
				log.info(" ---"+rs.getString("adjobyear"));
				adjobyear= rs.getString("adjobyear");
				i++;
				if(i==1){
					adjObYears = adjobyear;
				}else{
					adjObYears = adjObYears+":"+adjobyear;
				}
			}
			  
		}
		/*
		 * String adjObYears = new String[i]; for(int k=0;k<adjObYearList.length;k++){
		 * adjObYears =adjObYearList[k]; }
		 */
		log.info("==adjObYears==" + adjObYears	+ "-===i value==" + i);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return adjObYears;
}
public String getblockedAdjObYearsforPc(String pfid, String frmName) {
	String sqlQuery = "", blockedyear = "",blockedYears="";
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	 
	int i = 0; 
	sqlQuery = " select adjobyear from epis_adj_crtn_pfid_trackingpc where    BLOCKED ='Y' and pensionno ="+pfid;
			 
	log.info("AdjCrtnDAO::getblockedAdjObYear()---" + sqlQuery);
	try {

		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while (rs.next()) {

			if (rs.getString("adjobyear") != null) {
				log.info(" ---" + rs.getString("adjobyear"));
				i++;
				blockedyear = rs.getString("adjobyear");
				if(i==1){
					blockedYears = blockedyear;
				}else{
					blockedYears = blockedYears + ":" + blockedyear;
				}
			}
			
		}
		/*
		 * String adjObYears = new String[i]; for(int k=0;k<adjObYearList.length;k++){
		 * adjObYears =adjObYearList[k]; }
		 */
		log.info("==blockedYears==" +blockedYears);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return blockedYears;
}
public ArrayList getPensionContributionReportForAdjCRTNforPc(String fromDate,
		String toDate, String region, String airportcode,
		String empserialNO, String cpfAccno, String batchid,
		String ReportStatus) {
	ArrayList penContHeaderList = new ArrayList();

	// For Fetching the Employee PersonalInformation
	penContHeaderList = this.PCHeaderInfoForAdjCrtn(region, airportcode,
			empserialNO);

	String cpfacno = "", empRegion = "", empSerialNumber = "", tempPensionInfo = "", empCpfaccno = "";
	String[] cpfaccnos = new String[10];
	String[] regions = new String[10];
	String[] empPensionList = null;
	String[] pensionInfo = null;
	CommonDAO commonDAO = new CommonDAO();
	String pensionList = "", dateOfRetriment = "";
	ArrayList penConReportList = new ArrayList();
	log.info("Header Size" + penContHeaderList.size());
	log.info("" + penContHeaderList);
	String countFlag = "";
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	try {
		con = DBUtils.getConnection();
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
			// log.info(penContHeaderBean.getWhetherOption() + "option");
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

			// The Below mentioned method for Preparing the CPFSTRING based
			// on CPFACCNOs and corresponding regions.
			String preparedString = commonDAO.preparedCPFString(cpfaccnos,
					regions);
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
			if (ReportStatus.equals("userRequired")) {
				// For User Specified Report Generation Condition
				pensionList = this
						.getEmployeePensionInfoForAdjCrtnForUserRequiredforPc(
								preparedString, fromDate, toDate,
								penContHeaderBean.getWhetherOption(),
								dateOfRetriment, penContBean.getEmpDOB(),
								empserialNO, batchid);
			} else {
				// Normal Condition
				pensionList = this.getEmployeePensionInfoForAdjCrtnforPc(
						preparedString, fromDate, toDate, penContHeaderBean
								.getWhetherOption(), dateOfRetriment,
						penContBean.getEmpDOB(), empserialNO);
			}
			String checkMnthDate = "", rateOfInterest = "";
			st = con.createStatement();
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

						// log.info("contribution "+pensionInfo[7]);
						bean.setPensionContr(pensionInfo[7]);
						// calCPF=Double.parseDouble(bean.getCpf());
						// calPens=Double.parseDouble(pensionInfo[7]);
						calCPF = Math.round(Double.parseDouble(bean
								.getCpf()));
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
						// log.info("calCPF " +calCPF +"calPens "+calPens );
						bean.setGenMonthYear(pensionInfo[8]);
						bean.setTransCpfaccno(pensionInfo[9]);
						bean.setRegion(pensionInfo[10]);
						// log.info("pensionInfo[12] " + pensionInfo[12]);
						bean.setRecordCount(pensionInfo[11]);
						bean.setDbPensionCtr(pensionInfo[12]);
						bean.setDataFreezFlag(pensionInfo[13]);
						bean.setForm7Narration(pensionInfo[14]);
						bean.setPcHeldAmt(pensionInfo[15]);
						bean.setNoofMonths(pensionInfo[16]);
						bean.setPccalApplied(pensionInfo[17].trim());
						bean.setAdvAmount(pensionInfo[21].trim());
						bean.setEmployeeLoan(pensionInfo[22].trim());
						bean.setAaiLoan(pensionInfo[23].trim());
						bean.setEditedDate(pensionInfo[24].trim());
						bean.setEditedTransFlag(pensionInfo[26].trim());
						bean.setArrearBrkUpFlag(pensionInfo[27].trim());
						// log.info("PcApplied " +bean.getPccalApplied());
						if (bean.getPccalApplied().equals("N")) {
							bean.setCpf("0.00");
							bean.setAaiPFCont("0.00");
							bean.setPensionContr("0.00");
							bean.setDbPensionCtr("0.00");
						}
						bean.setArrearFlag(pensionInfo[18].trim());
						bean.setDeputationFlag(pensionInfo[19].trim());
						// For Fetching the Advances and Loans by Month Wise
						// for editing the Amount in Recoveries Screen
						String monthYear1 = commonUtil.converDBToAppFormat(
								pensionInfo[0], "dd-MMM-yyyy", "MMM-yyyy");

						// log.info("loan " + bean.getEmployeeLoan());
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
public ArrayList empPFCardReportPrintForAdjCrtnforPc(String range,
		String region, String selectedYear, String empNameFlag,
		String empName, String sortedColumn, String pensionno,
		String lastmonthFlag, String lastmonthYear, String airportcode,
		String bulkFlag) {
	log.info("AdjCrtnDAO::empPFCardReportPrint");
	String fromYear = "", toYear = "", dateOfRetriment = "", transferFlag = "", mappingFlag = "true",crossFinalsettlementFlag="false",addcontrisql="";
	double additionalContri=0.0,netepf=0.0;	
	ResultSet rs5=null;
	Statement st = null;
	Statement st1= null;
	Connection con = null;

	if (!selectedYear.equals("Select One")) {
		fromYear = "01-Apr-" + selectedYear;
		int toSelectYear = 0;
		toSelectYear = Integer.parseInt(selectedYear) + 1;
		toYear = "01-Mar-" + toSelectYear;
	} else {
		fromYear = "01-Apr-1995";
		toYear = "31-May-2011";
	}
	int formFrmYear = 0, formToYear = 0, finalSttlementDtYear = 0, formMonthYear = 0;
	try {
		formFrmYear = Integer.parseInt(commonUtil.converDBToAppFormat(
				fromYear, "dd-MMM-yyyy", "yyyy"));
		formToYear = Integer.parseInt(commonUtil.converDBToAppFormat(
				toYear, "dd-MMM-yyyy", "yyyy"));

	} catch (NumberFormatException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (InvalidDataException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	ArrayList empDataList = new ArrayList();
	EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
	EmployeeCardReportInfo cardInfo = null;
	ArrayList list = new ArrayList();
	ArrayList ptwList = new ArrayList();
	ArrayList finalSttmentList = new ArrayList();
	ArrayList list1 = new ArrayList();
	ArrayList ptwList1 = new ArrayList();
	ArrayList finalSttmentList1 = new ArrayList();
	String appEmpNameQry = "", finalSettlementDate = "";
	ArrayList cardList = new ArrayList();
	ArrayList addContriList = new ArrayList();
	ArrayList addConList=new ArrayList();
	int arrerMonths = 0;
	boolean finalStFlag = false,isFrozenAvail=false;
	try {
		con = DBUtils.getConnection();
		empDataList = commonDAO.getEmployeePFInfoPrinting(con, range,
				region, empNameFlag, empName, sortedColumn, pensionno,
				lastmonthFlag, lastmonthYear, airportcode, fromYear,
				toYear, bulkFlag);

		for (int i = 0; i < empDataList.size(); i++) {
			cardInfo = new EmployeeCardReportInfo();
			personalInfo = (EmployeePersonalInfo) empDataList.get(i);
			try {
				dateOfRetriment = commonDAO.getRetriedDate(personalInfo
						.getDateOfBirth());
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			log
					.info("FincialReportDAO:::empPFCardReportPrint:Final Settlement Date"
							+ personalInfo.getFinalSettlementDate()
							+ "Resttlement Date"
							+ personalInfo.getResttlmentDate());
			if (!personalInfo.getFinalSettlementDate().equals("---")) {
				finalSttlementDtYear = Integer.parseInt(commonUtil
						.converDBToAppFormat(personalInfo
								.getFinalSettlementDate(), "dd-MMM-yyyy",
								"yyyy"));
				formMonthYear = Integer.parseInt(commonUtil
						.converDBToAppFormat(personalInfo
								.getFinalSettlementDate(), "dd-MMM-yyyy",
								"MM"));

				if (finalSttlementDtYear <= formFrmYear) {
					finalStFlag = true;
					log.info("finalSttlementDtYear<= formFrmYear");
				} else if (formToYear == finalSttlementDtYear
						&& formMonthYear < 4) {
					finalStFlag = true;
					log
							.info("formToYear == finalSttlementDtYear&& formMonthYear < 4");
				} else {
					finalStFlag = false;
					log.info("con2 else");
				}

				if (finalStFlag == true) {

					finalSettlementDate = commonUtil.converDBToAppFormat(
							personalInfo.getFinalSettlementDate(),
							"dd-MMM-yyyy", "MMM-yyyy");
				} else {
					personalInfo.setFinalSettlementDate("---");
					finalSettlementDate = "---";
				}

			} else {
				finalSettlementDate = "---";
			}
			
			isFrozenAvail=personalInfo.isFrozedSeperationAvail();
			if (personalInfo.isFrozedSeperationAvail()==true && personalInfo.getChkPaymntSprtinDtFlag().equals("N")){
				isFrozenAvail=false;
				if (personalInfo.getFrozedSeperationInterest()!=0){
					finalSettlementDate=commonUtil.converDBToAppFormat(
							personalInfo.getFrozedSeperationDate(),
							"dd-MMM-yyyy", "MMM-yyyy");
				}
			}

			
			log.info("Before ProcessingAdjOB formFrmYear" + formFrmYear
					+ "formToYear" + formToYear + "finalSttlementDtYear"
					+ finalSttlementDtYear + "finalStFlag" + finalStFlag
					+ "formMonthYear" + formMonthYear
					+ "Final Settlement Date" + finalSettlementDate);
			//log.info("PFCARD_ADDITIONALCONTRIYEAR111111111111111111111=========="+selectedYear);

			// ProcessforAdjOb(personalInfo.getPensionNo(), true);
			st = null;
			st = con.createStatement();
			String sql="select nvl(DATAMODIFIEDFLAG,'N') as DATAMODIFIEDFLAG from epis_adj_crtnpc where pensionno="+pensionno+" and monthyear='01-May-2015' ";
			ResultSet rs=st.executeQuery(sql);
			if(rs.next()){
			if(rs.getString("DATAMODIFIEDFLAG").equals("N")){
			st1 = null;
			st1 = con.createStatement();
			addcontrisql = " update epis_adj_crtnpc c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
					+ pensionno+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+pensionno+" and monthyear ='01-May-2015'";
			log.info("----------addcontrisql-------------" + addcontrisql);
			rs5 = st1.executeQuery(addcontrisql);					
	              }
			}
			list = this.getEmployeePensionCardForAdjCrtnforPc(con, fromYear,
					toYear, personalInfo.getPfIDString(), personalInfo
							.getWetherOption(), personalInfo.getRegion(),
					true, dateOfRetriment, personalInfo.getDateOfBirth(),
					personalInfo.getOldPensionNo(), finalSettlementDate,
					personalInfo.getResttlmentDate(),isFrozenAvail);
			if(fromYear.equals("01-Apr-2012")){
				list1 = this.getEmployeePensionCardForAdjCrtnforPc(con, "01-Apr-2013", "30-Apr-2013",
						personalInfo.getPfIDString(), personalInfo
								.getWetherOption(), personalInfo.getRegion(),
						true, dateOfRetriment, personalInfo.getDateOfBirth(),
						personalInfo.getOldPensionNo(), finalSettlementDate,
						personalInfo.getResttlmentDate(),isFrozenAvail);
			//	ptwList1 = this.getPTWDetails(con, "01-Apr-2013", "30-Apr-2013",
					//	personalInfo.getCpfAccno(), personalInfo
							//	.getOldPensionNo());
				finalSttmentList1 = commonDAO.getFinalSettlement(con, "01-Apr-2013", "30-Apr-2013", personalInfo.getPfIDString(), 
						personalInfo.getOldPensionNo(), personalInfo
								.getFinalSettlementDate(), personalInfo
								.getResttlmentDate(),personalInfo.isCrossfinyear(),personalInfo.isFrozedSeperationAvail(),personalInfo.getFrozedSeperationDate(),personalInfo.getChkPaymntSprtinDtFlag());
				
			}
			// Flag is not used in the last paramter of getPTWDetails method

			/*
			 * ptwList = this.getPTWDetails(con, fromYear, toYear,
			 * personalInfo.getCpfAccno(), personalInfo .getOldPensionNo());
			 */

			finalSttmentList = commonDAO.getFinalSettlement(con, fromYear,
					toYear, personalInfo.getPfIDString(), personalInfo
							.getOldPensionNo(), personalInfo
							.getFinalSettlementDate(), personalInfo
							.getResttlmentDate(), personalInfo
							.isCrossfinyear(),false,personalInfo.getSeperationDate(),personalInfo.getChkPaymntSprtinDtFlag());
			log.info("PF Card====================Final Settlement Date"
					+ personalInfo.getFinalSettlementDate());


			if(finalSttmentList.size()!=0){
				if(personalInfo.getFinalSettlementDate().equals("31-Mar-2011") && finalSttmentList.get(11).equals("01-Apr-2011")){
					crossFinalsettlementFlag ="true";
				}
			}

			if (!personalInfo.getFinalSettlementDate().equals("---")) {
				arrerMonths = commonDAO.getArrearInfo(con, fromYear, toYear,
						personalInfo.getOldPensionNo());
				log.info("Arrear+FinalSettlement=========="+finalSettlementDate+"Comparing Condition"
						+ commonUtil.compareTwoDates(finalSettlementDate,
								commonUtil.converDBToAppFormat(fromYear,
										"dd-MMM-yyyy", "MMM-yyyy"))+"arrerMonths"+arrerMonths);
				if (commonUtil.compareTwoDates(finalSettlementDate,
						commonUtil.converDBToAppFormat(fromYear,
								"dd-MMM-yyyy", "MMM-yyyy")) == true) {
					// Final settlement date is lower than fromYear
					personalInfo.setChkArrearFlag("N");
					if (!personalInfo.getResttlmentDate().equals("---")) {
						log.info("Non-Blank Restlement Case");
						
						cardInfo.setInterNoOfMonths(12);
						cardInfo.setNoOfMonths(commonDAO
								.numOfMnthFinalSettlement(commonUtil
										.converDBToAppFormat(personalInfo
												.getResttlmentDate(),
												"dd-MMM-yyyy", "MMM")));
					} else if (arrerMonths != 0) {
						log.info("Blank Restlement Case and having arrear datata");
						cardInfo.setInterNoOfMonths(12);
						
						if (commonUtil.getDateDifference("01-Apr-2009",
								fromYear) >= 0) {
							personalInfo.setChkArrearFlag("Y");
						   

							cardInfo.setNoOfMonths(commonDAO
									.getNoOfMonthsForPFID(fromYear, toYear));
						} else {
							cardInfo.setNoOfMonths(12);
						}

					} else {
						log.info("ELSE CONDITON Blank Restlement Case --III");
						cardInfo.setInterNoOfMonths(12);
						cardInfo.setNoOfMonths(commonDAO
								.getNoOfMonthsForPFID(fromYear, toYear));

					}

				} else {
					if (formFrmYear <2011) {
						personalInfo.setChkArrearFlag("Y");
					}else{
						personalInfo.setChkArrearFlag("N");
						
					}
					log.info("Else Condtion compareTwoDates ChkArrearFlag"
							+ personalInfo.getChkArrearFlag());
					cardInfo.setNoOfMonths(commonDAO
							.numOfMnthFinalSettlement(commonUtil
									.converDBToAppFormat(personalInfo
											.getFinalSettlementDate(),
											"dd-MMM-yyyy", "MMM")));
					cardInfo.setArrearNoOfMonths(arrerMonths);
					cardInfo.setInterNoOfMonths(12);
				}

			} else {
				personalInfo.setChkArrearFlag("N");
				log.info("ChkArrearFlag" + personalInfo.getChkArrearFlag());
				cardInfo.setNoOfMonths(commonDAO.getNoOfMonthsForPFID(fromYear, toYear));
				cardInfo.setInterNoOfMonths(12);
			}
			log.info("personalInfo.isFrozedSeperationAvail()"+personalInfo.isFrozedSeperationAvail()+"ChkPaymntSprtinDt"+personalInfo.getChkPaymntSprtinDtFlag());
			if (personalInfo.isFrozedSeperationAvail()==true && personalInfo.getChkPaymntSprtinDtFlag().equals("N")){
				//personalInfo.setChkArrearFlag("Y");
				log.info("personalInfo.isFrozedSeperationAvail()=getFrozedSeperationInterest"+personalInfo.getFrozedSeperationInterest());
				cardInfo.setArrearNoOfMonths(personalInfo.getFrozedSeperationArrearInt());
				cardInfo.setInterNoOfMonths(personalInfo.getFrozedSeperationInterest());
				cardInfo.setNoOfMonths(personalInfo.getFrozedSeperationInterest());
			}
			log.info("PF Card====Final Settlement Date"
					+ personalInfo.getFinalSettlementDate()
					+ "Resettlement Date"
					+ personalInfo.getResttlmentDate() + "fromYear"
					+ fromYear + "NO.Of Months" + cardInfo.getNoOfMonths()
					+ "arrerMonths======" + arrerMonths);
			cardInfo.setFinalSettmentList(finalSttmentList);
			cardInfo.setArrearInfo(commonDAO.getArrearData(con, fromYear,
					toYear, personalInfo.getOldPensionNo()));
			if (finalSttmentList.size() != 0) {
				cardInfo.setOrderInfo(commonDAO.getSanctionOrderInfo(con,
						fromYear, toYear, personalInfo.getOldPensionNo()));
			} else {
				cardInfo.setOrderInfo("");
			}
			if(fromYear.equals("01-Apr-2012")){
				cardInfo.setPensionCardList1(list1);				 
				cardInfo.setPtwList1(ptwList1);
			}
			cardInfo.setPersonalInfo(personalInfo);
			cardInfo.setPensionCardList(list);		
			//cardInfo.setAddContriList(addContriList);
			//cardInfo.setPtwList(ptwList);
			cardList.add(cardInfo);
		}

	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, null, null);
	}

	return cardList;
}
public AdjCrntSaveDtlBean saveAdjCrntDetailsforPc(String empserialNO,
		String cpfAccno, String adjOBYear, double EmolumentsTot,
		double cpfTotal, double cpfIntrst, double PenContriTotal,
		double PensionIntrst, double PFTotal, double PFIntrst,
		double EmpSub, double EmpSubInterest, double adjEmpSubIntrst,
		double AAIContri, double AAIContriInterest,
		double adjAAiContriIntrst, double adjPensionContriInterest,
		String grandTotDiffShowFlag, String reasonForInsert,
		String username, String ipaddress, ArrayList adjEmolList,
		String batchid) throws InvalidDataException, SQLException {
	Connection con = null;
	int result = 0;
	String notFianalizetransID = "", transIdToGetPrevData = "", finlaizedFlag = "";
	List prevGrandTotalsList = new ArrayList();
	AdjCrntSaveDtlBean saveDtlsBean = new AdjCrntSaveDtlBean();
	try {
		con = DBUtils.getConnection();
		con.setAutoCommit(false);
		/*result = saveprvadjcrtntotals(con, empserialNO, adjOBYear,
				EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
				PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest,
				adjEmpSubIntrst, AAIContri, AAIContriInterest,
				adjAAiContriIntrst, adjPensionContriInterest);*/
		this.insertRecordForAdjCtrnTrackingforPc(con, empserialNO, cpfAccno,
				adjOBYear, reasonForInsert, username, ipaddress);
		notFianalizetransID = this.getAdjCrtnNotFinalizedTransIdforPc(con,
				empserialNO, adjOBYear);
		log.info("notFianalizetransID  In   " + notFianalizetransID
				+ "adjEmolList Size " + adjEmolList.size());
		if (notFianalizetransID.equals("")) {
			notFianalizetransID = this.getPCTotalsTransIdforPc(con, empserialNO,
					adjOBYear);
		}

		/*if (adjEmolList.size() > 0) {
			this.insertAdjEmolumenstLogInTemp(adjEmolList, empserialNO,
					adjOBYear, notFianalizetransID);
		}*/
		if (grandTotDiffShowFlag.equals("true")) {
			transIdToGetPrevData = this.insertAdjEmolumenstLogforPc(con,
					empserialNO, adjOBYear, notFianalizetransID);
		}
		con.setAutoCommit(true);
		finlaizedFlag = this.getAdjCrtnFinalizedFlagforPc(con, empserialNO,
				adjOBYear);
		if (finlaizedFlag.equals("Finalized")) {
			prevGrandTotalsList = this.getPrevPCGrandTotalsForAdjCrtnforPc(
					empserialNO, adjOBYear, batchid, transIdToGetPrevData);

		}
		log.info("finlaizedFlag  In   " + finlaizedFlag
				+ "prevGrandTotalsList Size " + prevGrandTotalsList.size());
		saveDtlsBean.setPreviouseGrndList(prevGrandTotalsList);
		saveDtlsBean.setFinalizedFlag(finlaizedFlag);

	} catch (SQLException se) {
		con.rollback();
		throw new InvalidDataException(se.getMessage());
	} catch (InvalidDataException e) {
		// TODO Auto-generated catch block
		con.rollback();
		throw e;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		con.rollback();
		throw new InvalidDataException(e.getMessage());
	} finally {
		commonDB.closeConnection(con, null, null);
	}

	return saveDtlsBean;
}
public AdjCrntSaveDtlBean getFinzdPreviouseGrndTotalsforPc(String empserialNO,
		String adjOBYear, String batchid) {
	String finlaizedFlag = "";
	List prevGrandTotalsList = new ArrayList();
	AdjCrntSaveDtlBean grandTotalBean = new AdjCrntSaveDtlBean();
	finlaizedFlag = this.getAdjCrtnFinalizedFlagforPc(empserialNO, adjOBYear);
	if (finlaizedFlag.equals("Finalized")) {
		prevGrandTotalsList = this.getPrevPCGrandTotalsForAdjCrtnforPc(
				empserialNO, adjOBYear, batchid, "");

	}
	log.info("finlaizedFlag  In   " + finlaizedFlag
			+ "prevGrandTotalsList Size " + prevGrandTotalsList.size());
	grandTotalBean.setPreviouseGrndList(prevGrandTotalsList);
	grandTotalBean.setFinalizedFlag(finlaizedFlag);
	return grandTotalBean;

}
public ArrayList editTransactionDataForAdjCrtnforPc(String cpfAccno,
		String monthyear, String emoluments,String addcon, String epf, String empvpf,
		String principle, String interest, String advance, String loan,
		String aailoan, String contri, String noofmonths, String pfid,
		String region, String airportcode, String username,
		String computername, String form7narration, String duputationflag,
		String pensionoption, String empnetob, String aainetob,
		String empnetobFlag, String finYear, String editTransFlag,String dateOfBirth) {

	String emppfstatuary = "0.00", oldemppfstatuary = "0.00", pf = "0.00", adjObYear = "";
	String updateEpisAdjCrtnLog="",insertEpisAdjCrtnLogDtl="",episAdjCrtnLog="",episAdjCrtnLogDtl="";
	String tableName = "EPIS_ADJ_CRTNPC";
	String updatedDate = commonUtil.getCurrentDate("dd-MMM-yyyy");
	String years[] = null;
	long  retiremntDate =0,monthYear=0,retiremntDate1 =0,monthYear1=0;
	double pensionCOntr = 0.0,oldEmoluments=0.0,oldPensionContri=0.0,oldDueEmoluments=0.0,oldDuePensionAmnt=0.0,addcon1=0.0,oldemolments=0.0;
	Connection con = null;
	Statement st = null;	 
	ResultSet rs = null;
	ResultSet rs1= null;
	String sqlQuery = "", transMonthYear = "", emoluments_log = "", emoluments_log_history = "", arrearQuery = "", chkArrearBrkupFlag = "", arrearBreakupFlag = "N",notFianalizetransID="",chkMnthFlag="",newRecord="false",updateArrearBrkUpData="",updatePFCardNarration="";
	boolean newcpfaccnoflag = false;
	System.out.println("finYear==="+finYear+"==transMonthYear=="+monthyear);
	System.out.println("1111====="+finYear.substring(0, 4));
	
	
	ArrayList adjEmolumentsList = new ArrayList();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		DateFormat df = new SimpleDateFormat("dd-MMM-yy");
		Date transdate = df.parse(monthyear);
		log.info("-------Pension Contri ------" + contri);
		retiremntDate = Date.parse(commonDAO.getRetriedDate(dateOfBirth));
		monthYear = Date.parse(monthyear);
		if(Integer.parseInt(finYear.substring(0, 4))>=2013){
			String checkPFID = " SELECT TO_CHAR(ADD_MONTHS('"+monthyear+"',1),'DD-MON-YYYY') as transMonthYear FROM dual ";
		    log.info(checkPFID);
		    rs = st.executeQuery(checkPFID);
		    while(rs.next()){
		    	monthyear=rs.getString("transMonthYear");
		    }
			}
		    System.out.println("1111=====transMonthYear=="+transMonthYear);
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
			String updateObForAdjCrtn = "update  EMPLOYEE_PENSION_OB_ADJ_CRTNPC set EMPNETOB="
					+ empnetob
					+ " ,AAINETOB="
					+ aainetob
					+ " where pensionno ="
					+ pfid
					+ " and OBYEAR='"
					+ adjObYear + "'";
			log.info("---update OB Values For Adj Correction------"
					+ updateObForAdjCrtn);
			st.executeUpdate(updateObForAdjCrtn);

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
			bean = this.getEmolumentsBeanForAdjCrtnforPc(con, monthyear,
					cpfAccno, "", region, pfid);
			if (transdate.before(new Date("31-Mar-2008"))) {
				log.info("---222222222222222222222222222------");
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
				log.info("addcon==="+addcon);
				log.info("Entered emoluments " + emoluments);
				if(Integer.parseInt(years[0])>=2015){
					oldemolments=Double.parseDouble(bean.getEmoluments());
					if(oldemolments>=15000){
					addcon1=(Double.parseDouble(emoluments)-oldemolments)*1.16/100;
					System.out.println("addcon1=="+addcon1);
					}else{
						addcon1=(Double.parseDouble(emoluments)-15000)*1.16/100;
					}
					addcon1=addcon1+Double.parseDouble(addcon);
					addcon=String.valueOf(addcon1);
				}
				/*if (emoluments.equals("0")) {
					emolumntscondition = " ,reviseepfemolumentsflag='Y'";
				} else {
					emolumntscondition = "";
				}*/
				log.info("addcon after adding==="+addcon);
				sqlQuery = "update " + tableName + " set cpfaccno = '"
						+ cpfAccno + "' , emoluments='" + emoluments
						+ "',additionalcontri='" + addcon
						+ "',emppfstatuary='"
						+ Math.round(Float.parseFloat(emppfstatuary))
						+ "',empvpf='" + empvpf + "',EMPADVRECPRINCIPAL='"
						+ principle + "',EMPADVRECINTEREST='" + interest
						+ "',PENSIONCONTRI='"+ contri + "',pf='" + pf + "', emolumentmonths='"
						+ noofmonths + "', empflag='Y',edittrans='"
						+ editTransFlag + "',FORM7NARRATION='"
						+ form7narration + "',editeddate='" + updatedDate
						+ "'  where "+ wherecondition+ " and  to_char(monthyear,'dd-Mon-yy') like '%"+ transMonthYear + "'  AND empflag='Y' ";

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
					String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, (case when i.deferement = 'Y' and i.deferementpension = 'S' then  i.deferementage * 12  else  696  end)),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date('"
							+ monthyear
							+ "','DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), (case when i.deferement = 'Y' and i.deferementpension = 'S' then  i.deferementage * 12  else  696  end)),'dd-Mon-RRRR')+1 as days from employee_personal_info i where to_char(pensionno)='"
							+ pfid + "'";
					log.info("checkPFID===============SATYAVENKATESH====================>"+checkPFID);
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
						+ pensionCOntr);
				// implemented for having no emoluments but having some
				// arrear related data
				if (bean != null && bean.getNoDataFlag().equals("false")) {

					sqlQuery = "update "
							+ tableName
							+ " set  cpfaccno='"
							+ cpfAccno
							+ "' ,emoluments='"
							+ emoluments
							+ "',additionalcontri='"
							+ addcon
							+ "',emppfstatuary='"
							+ Math.round(Float.parseFloat(emppfstatuary))
							+ "',empvpf='"
							+ empvpf
							+ "',EMPADVRECPRINCIPAL='"
							+ principle
							+ "',EMPADVRECINTEREST='"
							+ interest								 
							+ "',PENSIONCONTRI='"
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
							+ "' where pensionno='"
							+ pfid
							+ "'"
							+ " and  to_char(monthyear,'dd-Mon-yy') like '%"
							+ transMonthYear + "'  AND empflag='Y' ";

				} else {
					newRecord="true";
					if (transdate.after(new Date("31-Mar-2008"))) {
						FinacialDataBean dataBean = new FinacialDataBean();
						dataBean = this.getPreMonthYearDataforPc(con, monthyear,
								pfid);
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
						chkArrearBrkupFlag = this.checkArrearBreakupLimit(
								con, pfid, monthyear);
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
							+ " (emoluments,additionalcontri,emppfstatuary,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,ADVANCE,PFWSUB ,PFWCONTRI ,PENSIONCONTRI,pf,monthyear,cpfaccno,region,pensionno,FORM7NARRATION,ARREARSBREAKUP, EMPFLAG, edittrans,remarks,AIRPORTCODE,editeddate) values('"
							+ emoluments + "','"
							+ addcon + "','"
							+ Math.round(Float.parseFloat(emppfstatuary))
							+ "','" + Math.round(Float.parseFloat(empvpf))
							+ "','" + principle + "','" + interest + "','"
							+ advance + "','" + loan + "','" + aailoan
							+ "','" + Math.round(pensionCOntr) + "','"
							+ Math.round(Float.parseFloat(pf)) + "','"
							+ monthyear + "','" + cpfAccno + "','" + region
							+ "','" + pfid + "','" + form7narration + "','"
							+ arrearBreakupFlag
							+ "','Y','N','New Record','" + airportcode
							+ "','" + updatedDate + "')";

				}
			}
			int count = 0;
			String selectEmolumentsLog = "select count(*) as count from EPIS_ADJ_CRTN_EMOLUMENTS_LOGPC where cpfacno='"
					+ cpfAccno
					+ "' and  to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear + "' and region='" + region + "' ";
			log.info("---------selectEmolumentsLog ------"
					+ selectEmolumentsLog);
			rs = st.executeQuery(selectEmolumentsLog);
			while (rs.next()) {
				count = rs.getInt(1);
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
//code modified according to monthyear chking
			
			chkMnthFlag = chkMnthInEmolTempLog(con,pfid,monthyear);
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
				emolBean.setAddcon(addcon);
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
				emolBean.setAddcon(addcon);
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
			notFianalizetransID = this.getAdjCrtnNotFinalizedTransIdforPc(con,
					pfid, finYear);
			log.info("notFianalizetransID  In   " + notFianalizetransID
					+ "adjEmolList Size " + adjEmolumentsList.size());
			 
			this.insertAdjEmolumenstLogInTempforPc(adjEmolumentsList, pfid,
						finYear, notFianalizetransID);
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
				rs = st.executeQuery(selectQry);
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
					log.info(" New Entry in EPIS_INFO_ADJ_CRTNPC   "
							+ insertQry);
					st.executeUpdate(insertQry);
				}
			}

		}
		
		//For Log Tracking for Already entered Data
	 
	String loggeridseq = "select loggerid from epis_adj_crtn_logpc where pensionno="
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
	episAdjCrtnLog = "insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
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
				updatePFCardNarration= "update epis_adj_crtnpc set  PFCARDNARRATION='"+form7narration+"' ,PFCARDNRFLAG='Y'  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' ";
				log.info("updatePFCardNarration   " + updatePFCardNarration);
				st.executeUpdate(updatePFCardNarration);
			}
			
			double  returnPC=0.0;
			returnPC = this.pensionContributionProcess2008to11ForAdjCRTNforPc(region, pfid,
					monthyear);
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
				updateArrearBrkUpData = "update epis_adj_crtnpc set dueemoluments="+newDueEmoluments+", arrearamount="+newArrearAmount+"  where pensionno = "+pfid+" and monthyear = '"+monthyear+"' and ARREARSBREAKUP='Y' and dueemoluments is not null and arrearamount is not null and dueemoluments>0";
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
private ArrayList PCHeaderInfoForAdjCrtnforPc(String region, String airportCD,
		String empserialNO) {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "";
	log.info("region in reportDao********  " + region);

	// The Below mentioned method for retrieving the mapping info from
	// PensionContribution screen hitting
	sqlQuery = this.buildPenConEmpInfoQueryforPc(region, airportCD, empserialNO);

	log.info("PCHeaderInfoForAdhCrtn===Query" + sqlQuery);
	String tempSrlNumber = "", srlNumber = "", doj = "", dob = "", cpfacnos = "", regions = "", fhName = "", employeeName = "", designation = "";
	String tempRegion = "", tempCPF = "", department = "";
	String finalSettlementdate = "";
	ArrayList finalList = new ArrayList();
	int totalRS = 0, tempTotalRs = 0, totalSrlNo = 0, totalRecCpf = 0;
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		ArrayList list = new ArrayList();
		PensionContBean bean = new PensionContBean();
		totalRS = this.getEmpPensionCountForAdjCrtnforPc(empserialNO);
		String wetherOption = "", pfSettled = "", interestCalcUpto = "", dateofSeperationDate = "";
		while (rs.next()) {
			tempTotalRs++;
			// code modified as on Feb 19th
			if (rs.getString("employeename") != null) {
				employeeName = rs.getString("employeename");
			}
			if (rs.getString("wetheroption") != null) {
				wetherOption = rs.getString("wetheroption").trim();
				if(!wetherOption.equals("A")){
					wetherOption="B";
				}
			}
			// Enable after updating whetheroption in epis_info_adj_crtn for having option is null
			/*else{
				wetherOption="B";
			}*/
			
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
		log.info("tempSrlNumber" + tempSrlNumber + "bean.cpfacnos"
				+ bean.getCpfacno() + "regions" + regions
				+ finalList.size());
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return finalList;
}
public int getEmpPensionCountForAdjCrtnforPc(String empserialNO) {
	int count = 0;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "";
	FinancialYearBean yearBean = new FinancialYearBean();
	if (empserialNO.equals("")) {
		sqlQuery = "SELECT COUNT(*) AS COUNT FROM  EPIS_Info_ADJ_CRTN WHERE empserialnumber is not null  ORDER BY empserialnumber";

	} else {
		sqlQuery = "SELECT COUNT(*) AS COUNT FROM  EPIS_Info_ADJ_CRTN WHERE empserialnumber is not null  and empserialnumber='"
				+ empserialNO + "' ORDER BY empserialnumber";
	}
	try {
		con = DBUtils.getConnection();
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
public String buildPenConEmpInfoQueryforPc(String region, String airportCD,
		String empserialNO) {
	log.info("AdjCrtnDAO :buildPenConEmpInfoQuery() entering method");
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
		conn = DBUtils.getConnection();
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

			if (rs.getString("FINALSETTLMENTDT") != null) {
				personalfinalsettlementdate = CommonUtil
						.converDBToAppFormat(rs.getDate("FINALSETTLMENTDT"));
			}
			if (rs.getString("INTERESTCALCDATE") != null) {
				interestCalUpto = CommonUtil.converDBToAppFormat(rs
						.getDate("INTERESTCALCDATE"));
			}
			if (rs.getString("DATEOFSEPERATION_DATE") != null) {
				dateofSeperationDate = CommonUtil.converDBToAppFormat(rs
						.getDate("DATEOFSEPERATION_DATE"));
			}
		}
		String settlementDate = "";
		settlementDate = personalfinalsettlementdate;

		sqlQuery = "SELECT DISTINCT NVL(CPFACNO,'NO-VAL') AS CPFACNO,DEPARTMENT,REGION,'"
				+ settlementDate
				+ "'AS FINALSETTLMENTDT,'"
				+ interestCalUpto
				+ "' as InterestCalupto,'"
				+ dateofSeperationDate
				+ "'as DATEOFSEPERATION_DATE,PENSIONNUMBER,MARITALSTATUS,EMPSERIALNUMBER,DATEOFJOINING,EMPLOYEENO,DATEOFBIRTH,EMPLOYEENAME,SEX,FHNAME,DESEGNATION,WETHEROPTION,round(months_between(NVL(DATEOFJOINING,'01-Apr-1995'),'01-Apr-1995'),3) ENTITLEDIFF,PFSETTLED FROM EPIS_Info_ADJ_CRTN WHERE EMPSERIALNUMBER IS NOT NULL  and EMPSERIALNUMBER='"
				+ empserialNO + "'  ";

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
		log.info("AdjCrtnDAO :buildQuery() leaving method");
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
private String getEmployeePensionInfoForAdjCrtnforPc(String cpfString,
		String fromDate, String toDate, String whetherOption,
		String dateOfRetriment, String dateOfBirth, String Pensionno) {

	// Here based on recoveries table flag we deside which table to hit and
	// retrive the data. if recoverie table value is false we will hit
	// Employee_pension_validate else employee_pension_final_recover table.
	String tablename = "EPIS_ADJ_CRTNPC";

	log.info("formdate " + fromDate + "todate " + toDate);
	String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
	String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from epis_info_adj_crtn where ("
			+ tempCpfString + ") and rownum=1)";
	String condition = "";
	if (Pensionno != "" && !Pensionno.equals("")) {
		condition = " or pensionno=" + Pensionno;
	}

	String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.ADVANCE AS ADVANCE,emp.PFWSUB AS PFWSUB,emp.PFWCONTRI AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI,emp.EDITTRANS as EDITTRANS,emp.ARREARSBREAKUP as ARREARSBREAKUP from "
			+ "(select distinct to_char(to_date('"
			+ fromDate
			+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from  employee_pension_validate  "
			+ " where empflag='Y' and    rownum "
			+ "<= to_date('"
			+ toDate
			+ "','dd-mon-yyyy')-to_date('"
			+ fromDate
			+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,NVL(ADVANCE,0.00) AS ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,EDITTRANS,ARREARSBREAKUP FROM "
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
			+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,nvl(emp.ADVANCE,0.00) AS ADVANCE,nvl(emp.PFWSUB,0.00) AS PFWSUB,nvl(emp.PFWCONTRI,0.00) AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI,NVL(EDITTRANS,'N') as EDITTRANS, NVL(ARREARSBREAKUP,'N') AS ARREARSBREAKUP  from (select distinct to_char(to_date("
			+ dojQuery
			+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
			+ toDate
			+ "','dd-mon-yyyy')-to_date("
			+ dojQuery
			+ ",'dd-mon-yyyy')+1 ) empdt,"
			+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
			+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,nvl(ADVANCE,0.00) as ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,NVL(EDITTRANS,'N') as EDITTRANS, NVL(ARREARSBREAKUP,'N') AS ARREARSBREAKUP  "
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
			+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('"
			+ fromDate + "')";

	// String advances =
	// "select amount from employee_pension_advances where pensionno=1";

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
		con = DBUtils.getConnection();
		st = con.createStatement();
		log.info(sqlQuery);
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
				buffer.append(Double.toString(pensionCOntr));
			} else {
				buffer.append("0");
			}
			buffer.append(",");
			if (rs.getDate("EMPMNTHYEAR") != null) {
				buffer.append(CommonUtil.converDBToAppFormat(rs
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
			// Regarding pensioncontri calcuation upto current date modified
			// by radha p
			if (transdate.after(new Date(commonUtil
					.getCurrentDate("dd-MMM-yy")))
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
							.getString("EMOLUMENTS"), whetherOption,
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
			// Datafreezeflag
			buffer.append("-NA-");

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
			if (rs.getString("ADVANCE") != null) {
				buffer.append(rs.getString("ADVANCE"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("PFWSUB") != null) {
				buffer.append(rs.getString("PFWSUB"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("PFWCONTRI") != null) {
				buffer.append(rs.getString("PFWCONTRI"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("editeddate") != null) {
				buffer.append(rs.getString("editeddate"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
				buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
			} else {
				buffer.append("N");
			}
			buffer.append(",");
			
			if (rs.getString("EDITTRANS") != null) {
				buffer.append(rs.getString("EDITTRANS"));
			} else {
				buffer.append("N");
			}
			buffer.append(",");
			
			if (rs.getString("ARREARSBREAKUP") != null) {
				buffer.append(rs.getString("ARREARSBREAKUP"));
			} else {
				buffer.append("N");
			}
			buffer.append("=");
			

		}
		/*
		 * if (count == i) { buffer = new StringBuffer(); } else { }
		 */

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	log.info("----- " + buffer.toString());
	return buffer.toString();

}
public int insertEmployeeTransDataforPc(String pfId, String frmName,
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
	ResultSet rs6 = null;
	Statement st = null;
	Statement st1 = null;
	Statement st2 = null;
	String episAdjCrtnLog = "", episAdjCrtnLogDtl = "", sqlForMapping1995to2008 = "", sqlFor1995to2008 = "";
	String chkpfid = "", commdata = "", revisedFlagQry = "", loansQuery = "", advancesQuery = "", sql = "",sqlAftr2008="", updateloans = "", updateadvances = "", loandate = "", subamt = "", contamt = "", advtransdate = "", advanceAmt = "", monthYear = "", tableName = "";
	String cpfregionstrip = "", condition = "", preparedString = "", dojcpfaccno = "", dojemployeeno = "", dojempname = "", dojstation = "", dojregion = "",	insertDummyRecord="", chkForRecord=""; 
	String[] cpfregiontrip = null;
	String[] cpfaccnos = null;
	String[] regions = null;
	String[] commondatalst = null;
	String [] years={"1995-2008","2008-2009","2009-2010","2010-2011","2011-2012","2012-2013","2013-2014","2014-2015","2015-2016","2016-2017","2017-2018"};
	int result = 0, loansresult = 0, advancesresult = 0, transID = 0, batchId = 0,insertDummyRecordResult=0;
	monthYear = commonUtil.getCurrentDate("dd-MMM-yyyy");
	EmpMasterBean empBean = new EmpMasterBean();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		st1 = con.createStatement();
		this.chkDOJ(con, pfId);
		cpfregionstrip = this.getEmployeeCpfacno(con, pfId);
		String[] pfIDLists = cpfregionstrip.split("=");
		preparedString = commonDAO.preparedCPFString(pfIDLists);
		log.info("preparedString====================" + preparedString);

		if (frmName.equals("adjcorrections")) {
			tableName = "EPIS_ADJ_CRTNPC";
		} else if (frmName.equals("form7/8psadjcrtn")) {
			tableName = "EPIS_ADJ_CRTN_FORM78PS";
		}
		// chkpfid = "select * from "+tableName+" where pensionno=" + pfId;
		// rs = st.executeQuery(chkpfid);
		chkpfid = this.chkPfidinAdjCrtnforPc(pfId, frmName); 
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
					+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS   from employee_pension_validate where empflag='Y' and (pensionno="
					+ pfId + " or " + preparedString
					+ ") and monthyear <='31-Mar-2008')";
			
			sqlAftr2008 =  "insert into "
				+ tableName
				+ " (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
				+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
				+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS,ADDITIONALCONTRI)   (select  "
				+ pfId
				+ " ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
				+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
				+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
				+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS, ADDITIONALCONTRI   from employee_pension_validate where empflag='Y' and   pensionno="
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
			if (mappingFlag.equals("U") && upflag.equals("N")) {

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
			
			}
			log.info("----------sql-------------" + sql);
			log.info("----------sqlAftr2008-------------" + sqlAftr2008);
			st.addBatch(sql);
			st.addBatch(sqlAftr2008);
			if (frmName.equals("adjcorrections")) {
			for(int i=0;i<years.length;i++){
				episAdjCrtnLog = "insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"
						+ pfId + ",'"+years[i]+"',sysdate)";
				log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
				st.addBatch(episAdjCrtnLog);
				episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtlpc(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"
					+ username + "','" + ipaddress + "',sysdate)";
				log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
				st.addBatch(episAdjCrtnLogDtl);	 
			}


			
			revisedFlagQry = " update "
				+ tableName
				+ "    set  reviseepfemolumentsflag='N' where  pensionno="
				+ pfId + " and empflag='Y'"; 
		
			log.info("----------revisedFlagQry-------------"
				+ revisedFlagQry);
			st.addBatch(revisedFlagQry); 
			}
			
			int insertCount[] = st.executeBatch();
			log.info("insertCount  " + insertCount.length);
			st2 = null;
			st2 = con.createStatement();
			String addcontrisql = " update epis_adj_crtnpc c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
					+ pfId+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+ pfId+" and monthyear ='01-May-2015'";
			log.info("----------addcontrisql-------------" + addcontrisql);
			rs6 = st2.executeQuery(addcontrisql);
			
			st = null;
			st = con.createStatement();
			loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
					+ pfId;
			log.info("----------loansQuery-------------" + loansQuery);
			rs1 = st.executeQuery(loansQuery);
			while (rs1.next()) {

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
					loansresult = st.executeUpdate(updateloans);
				}else{
					insertDummyRecord= " insert into epis_adj_crtnpc (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
									+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtnpc where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'"
									+" and rowid =(select max(rowid) from epis_adj_crtnpc where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy') <'"+ loandate + "'))";       
					 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
					 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
					 loansresult = st.executeUpdate(updateloans);
				}
				log	.info("----------updateloans-------------"	+ updateloans);
				
			}
			advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
					+ pfId;
			log
					.info("----------advancesQuery-------------"
							+ advancesQuery);
			st = con.createStatement();
			rs2 = st.executeQuery(advancesQuery);
			while (rs2.next()) {
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
					advancesresult = st.executeUpdate(updateadvances);
					 
				}else{
					insertDummyRecord= " insert into epis_adj_crtnpc (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
									+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtnpc where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'"
									+" and rowid =(select max(rowid) from epis_adj_crtnpc where pensionno ="+pfId+" and to_char(monthyear,'MON-yyyy')<'"+advtransdate+"'))";       
					insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
					log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
					advancesresult = st.executeUpdate(updateadvances);
				}
				log.info("----------updateadvances-------------"+ updateadvances);
			}
			//No need to inserting into seperate table as we remove edit  facility to Opening Balances
			/*if (frmName.equals("adjcorrections")) {
				String obQuery = "insert into EMPLOYEE_PENSION_OB_ADJ_CRTN (select * from  EMPLOYEE_PENSION_OB where pensionno="
						+ pfId + " and  OBYEAR <='01-Apr-2010')";
				log.info("Opening Balance Fetching Query  " + obQuery);

				result = st.executeUpdate(obQuery);
			}*/
		} else {
			for(int i=0;i<years.length;i++){
			String loggeridseq = "select loggerid from epis_adj_crtn_logpc where pensionno="
					+ pfId+ " and adjobyear='"+years[i]+"'";
			int logid = 0;
			log.info("loggeridseq " + loggeridseq);
			rs3 = st.executeQuery(loggeridseq);
			if (rs3.next()) {
				logid = Integer.parseInt(rs3.getString("loggerid"));
				log.info("logid  test" + logid);
			} else {
				st = null;
				st = con.createStatement();
				episAdjCrtnLog = "insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"
						+ pfId
						+ ",'"+years[i]+"',sysdate,'This pfid already ported before implmenation logic')";
				st.executeUpdate(episAdjCrtnLog);
				st = null;
				st = con.createStatement();
				rs3 = st.executeQuery(loggeridseq);
				if (rs3.next())
					logid = Integer.parseInt(rs3.getString("loggerid"));
				st = null;
			}
			if (flag.equals("S")) {
				episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtlpc(loggerid,username,ipaddress,workingdt) values ("
						+ logid
						+ ",'"
						+ username
						+ "','"
						+ ipaddress
						+ "',sysdate)";
				st = con.createStatement();
				log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
				result = st.executeUpdate(episAdjCrtnLogDtl);
			}
			log.info("count :" + result);
			log.info("--------Data already exists----------");
		}
		}
		// Mapping Data Tracking
		if (mappingFlag.equals("M")) {
			String mappingPreparedString = "(CPFACCNO='" + cpfacno
					+ "' AND REGION='" + region + "')";
			batchId = this.getBatchId(con);
			String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TPC  where pensionno='"
					+ pfId + "' and ADJOBYEAR ='1995-2008'";
			rs4 = st.executeQuery(transIDQry);
			if (rs4.next()) {
				transID = Integer.parseInt(rs4.getString("TRANSID"));
			}

			sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_logpc (PENSIONNO ,CPFACNO,MONTHYEAR ,NEWEMOLUMENTS  ,NEWEMPPFSTATUARY , NEWEMPVPF , "
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
			result = st.executeUpdate(sqlForMapping1995to2008);
			log.info("result" + result);
		}
		if (mappingFlag.equals("U")) {
			// String mappingPreparedString="(CPFACCNO='"+cpfacno+"' AND
			// REGION='"+region+"')";
			batchId = this.getBatchId(con);
			String transIDQry = "select TRANSID from EPIS_ADJCRTN_PRVPCTOTALS_TPC  where pensionno='"
					+ pfId + "' and ADJOBYEAR ='1995-2008'";
			rs4 = st.executeQuery(transIDQry);
			if (rs4.next()) {
				transID = Integer.parseInt(rs4.getString("TRANSID"));
			}

			sqlForMapping1995to2008 = "insert into epis_adj_crtn_emoluments_logpc (PENSIONNO ,MONTHYEAR ,NEWEMOLUMENTS ,NEWEMPPFSTATUARY, NEWPRINCIPLE ,  NEWINTEREST, "
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
			result = st.executeUpdate(sqlForMapping1995to2008);
			log.info("resultForModified" + result);
			// String emolumentLog="insert into
			// epis_adj_crtn_emoluments_log(pensionno,cpfacno,monthyear,oldemoluments,oldemppfstatuary,oldempvpf,oldprinciple,oldinterest,
			// originalrecord) select
			// PENSIONNO,CPFACCNO,MONTHYEAR,0,0,0,0,0,'Y' from
			// epis_adj_crtn_md_bk where pensionno='"+pfId+"' and
			// DATATYPE='U'";
			String emolumentLog = "update epis_adj_crtnpc set DATAMODIFIEDFLAG='Y' where pensionno='"
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
private String getEmployeePensionInfoForAdjCrtnForUserRequiredforPc(
		String cpfString, String fromDate, String toDate,
		String whetherOption, String dateOfRetriment, String dateOfBirth,
		String Pensionno, String batchid) {

	// Here based on recoveries table flag we deside which table to hit and
	// retrive the data. if recoverie table value is false we will hit
	// Employee_pension_validate else employee_pension_final_recover table.
	String tablename = "EPIS_ADJ_CRTNPC";

	log.info("formdate " + fromDate + "todate " + toDate);
	String tempCpfString = cpfString.replaceAll("CPFACCNO", "cpfacno");
	String dojQuery = "(select nvl(to_char (dateofjoining,'dd-Mon-yyyy'),'1-Apr-1995') from epis_info_adj_crtn where ("
			+ tempCpfString + ") and rownum=1)";
	String condition = "";
	if (Pensionno != "" && !Pensionno.equals("")) {
		condition = " or pensionno=" + Pensionno;
	}

	String sqlQuery = " select mo.* from (select TO_DATE('01-'||SUBSTR(empdt.MONYEAR,0,3)||'-'||SUBSTR(empdt.MONYEAR,4,4)) AS EMPMNTHYEAR,emp.MONTHYEAR AS MONTHYEAR,emp.EMOLUMENTS AS EMOLUMENTS,emp.EMPPFSTATUARY AS EMPPFSTATUARY,emp.EMPVPF AS EMPVPF,emp.EMPADVRECPRINCIPAL AS EMPADVRECPRINCIPAL,emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,emp.ADVANCE AS ADVANCE,emp.PFWSUB AS PFWSUB,emp.PFWCONTRI AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region ,'Duplicate' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths, PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as  OPCHANGEPENSIONCONTRI, DataModifiedFlag as DataModifiedFlag from "
			+ "(select distinct to_char(to_date('"
			+ fromDate
			+ "','dd-mon-yyyy') + rownum -1,'MONYYYY') monyear from  employee_pension_validate  "
			+ " where empflag='Y' and    rownum "
			+ "<= to_date('"
			+ toDate
			+ "','dd-mon-yyyy')-to_date('"
			+ fromDate
			+ "','dd-mon-yyyy')+1) empdt ,(SELECT cpfaccno,to_char(MONTHYEAR,'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR,'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS,2) AS EMOLUMENTS,ROUND(EMPPFSTATUARY,2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,NVL(ADVANCE,0.00) AS ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,DataModifiedFlag FROM "
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
			+ "emp.EMPADVRECINTEREST AS EMPADVRECINTEREST,nvl(emp.ADVANCE,0.00) AS ADVANCE,nvl(emp.PFWSUB,0.00) AS PFWSUB,nvl(emp.PFWCONTRI,0.00) AS PFWCONTRI,emp.AIRPORTCODE AS AIRPORTCODE,emp.cpfaccno AS CPFACCNO,emp.region as region,'Single' DUPFlag,emp.PENSIONCONTRI as PENSIONCONTRI,emp.form7narration as form7narration,emp.pcHeldAmt as pcHeldAmt,nvl(emp.emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,emp.OPCHANGEPENSIONCONTRI as OPCHANGEPENSIONCONTRI, emp.DataModifiedFlag as DataModifiedFlag  from (select distinct to_char(to_date("
			+ dojQuery
			+ ",'dd-mon-yyyy') + rownum -1,'MONYYYY') MONYEAR from employee_pension_validate where empflag='Y' AND rownum <= to_date('"
			+ toDate
			+ "','dd-mon-yyyy')-to_date("
			+ dojQuery
			+ ",'dd-mon-yyyy')+1 ) empdt,"
			+ "(SELECT cpfaccno,to_char(MONTHYEAR, 'MONYYYY') empmonyear,TO_CHAR(MONTHYEAR, 'DD-MON-YYYY') AS MONTHYEAR,ROUND(EMOLUMENTS, 2) AS EMOLUMENTS,"
			+ " ROUND(EMPPFSTATUARY, 2) AS EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,nvl(ADVANCE,0.00) as ADVANCE,NVL(PFWSUB,0.00) AS PFWSUB,NVL(PFWCONTRI,0.00) AS PFWCONTRI,AIRPORTCODE,REGION,EMPFLAG,PENSIONCONTRI,form7narration,pcHeldAmt,nvl(emolumentmonths,'1') as emolumentmonths,PCCALCAPPLIED,ARREARFLAG, nvl(DEPUTATIONFLAG,'N') AS DEPUTATIONFLAG,editeddate,OPCHANGEPENSIONCONTRI,DataModifiedFlag "
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
			+ "','DD-MON-YYYY')  group by  to_char(MONTHYEAR,'MONYYYY')  having count(*)>1)))mo where to_date(to_char(mo.Empmnthyear,'dd-Mon-yyyy')) >= to_date('"
			+ fromDate + "')";

	// String advances =
	// "select amount from employee_pension_advances where pensionno=1";

	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	StringBuffer buffer = new StringBuffer();
	String monthsBuffer = "", formatter = "", tempMntBuffer = "";
	long transMntYear = 0, empRetriedDt = 0;
	double pensionCOntr = 0;
	double pensionCOntr1 = 0;
	String recordCount = "", DataModifiedFlag = "";
	int getDaysBymonth = 0, cnt = 0, checkMnts = 0, chkPrvmnth = 0, chkCrntMnt = 0;
	double PENSIONCONTRI = 0;
	boolean contrFlag = false, chkDOBFlag = false, formatterFlag = false;
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		log.info(sqlQuery);
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

			if (rs.getString("DataModifiedFlag") != null) {
				DataModifiedFlag = rs.getString("DataModifiedFlag");
			}
			log
					.info("getEmployeePensionInfoForAdjCrtnForUserRequired==DataModifiedFlag  "
							+ DataModifiedFlag);
			ArrayList empPensionList = new ArrayList();
			if (DataModifiedFlag.equals("Y")) {

				empPensionList = this.getEmpPensionInfoForAdjCrtnLogDataforPc(
						con, Pensionno, monthYear, batchid);
			}
			if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
				buffer.append(empPensionList.get(0));
				buffer.append(",");

				buffer.append(empPensionList.get(1));
				buffer.append(",");

				buffer.append(empPensionList.get(2));
				buffer.append(",");

				buffer.append(empPensionList.get(3));
				buffer.append(",");

				buffer.append(empPensionList.get(4));
				buffer.append(",");

			} else {
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

			}

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

			if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
				buffer.append(empPensionList.get(5));
			} else {
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
							getDaysBymonth = commonDAO
									.getNoOfDays(dateOfBirth);
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
			// Regarding pensioncontri calcuation upto current date modified
			// by radha p
			if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
				buffer.append(empPensionList.get(5));
			} else {
				if (transdate.after(new Date(commonUtil
						.getCurrentDate("dd-MMM-yy")))
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
								.getString("EMOLUMENTS"), whetherOption,
								region, rs.getString("emolumentmonths"));
						if (chkDOBFlag == true) {
							String[] dobList = dateOfBirth.split("-");
							days = dobList[0];
							getDaysBymonth = commonDAO
									.getNoOfDays(dateOfBirth);
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
			}
			buffer.append(",");
			// Datafreezeflag
			buffer.append("-NA-");

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
			if (rs.getString("ADVANCE") != null) {
				buffer.append(rs.getString("ADVANCE"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("PFWSUB") != null) {
				buffer.append(rs.getString("PFWSUB"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");
			if (rs.getString("PFWCONTRI") != null) {
				buffer.append(rs.getString("PFWCONTRI"));
			} else {
				buffer.append(" ");
			}
			buffer.append(",");

			if (DataModifiedFlag.equals("Y") && empPensionList.size() > 0) {
				buffer.append(empPensionList.get(6));
				buffer.append(",");
			} else {
				buffer.append("N");
				buffer.append(",");
			}
			/*
			 * if (rs.getString("editeddate") != null) {
			 * buffer.append(rs.getString("editeddate")); } else {
			 * buffer.append(" "); }
			 */
			if (rs.getString("OPCHANGEPENSIONCONTRI") != null) {
				buffer.append(rs.getString("OPCHANGEPENSIONCONTRI"));
			} else {
				buffer.append("N");
			}
			buffer.append("=");

		}
		/*
		 * if (count == i) { buffer = new StringBuffer(); } else { }
		 */

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	log.info("----- " + buffer.toString());
	return buffer.toString();

}
public ArrayList getEmpPensionInfoForAdjCrtnLogDataforPc(Connection con,
		String Pensionno, String monthYear, String batchId) {
	int count = 0;

	Statement st = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	String sqlQuery = "", emoluments = "", epf = "", empvpf = "", principle = "", interest = "", pensionContri = "", editTransFlag = "", monthYearQry = "", minBatchId = "";
	ArrayList al = new ArrayList();

	monthYearQry = "select min(batchid) as batchid from  epis_adj_crtn_emoluments_logpc where pensionno = "
			+ Pensionno
			+ "    and originalrecord = 'N'    and monthyear = '"
			+ monthYear
			+ "' and batchid between "
			+ batchId
			+ " and   (select max(batchid) from epis_adj_crtn_emoluments_logpc  where pensionno =  "
			+ Pensionno + ")";
	try {
		log.info("--monthYearQry------" + monthYearQry);

		st = con.createStatement();
		rs = st.executeQuery(monthYearQry);
		if (rs.next()) {
			minBatchId = rs.getString("batchid");
		}
		sqlQuery = " select OLDEMOLUMENTS AS OLDEMOLUMENTS, OLDEMPPFSTATUARY  AS OLDEMPPFSTATUARY,OLDEMPVPF  AS OLDEMPVPF, OLDPRINCIPLE as OLDPRINCIPLE,OLDINTEREST AS OLDINTEREST, OLDPENSIONCONTRI AS OLDPENSIONCONTRI , NEWEMOLUMENTS  AS NEWEMOLUMENTS , NEWEMPPFSTATUARY AS NEWEMPPFSTATUARY, NEWEMPVPF AS NEWEMPVPF, NEWPRINCIPLE AS NEWPRINCIPLE, NEWINTEREST AS NEWINTEREST  from epis_adj_crtn_emoluments_logpc    where pensionno = "
				+ Pensionno
				+ "    and batchid   ="
				+ minBatchId
				+ "   and originalrecord = 'N'    and monthyear = '"
				+ monthYear
				+ "' "
				+ " and rowid = (select max(rowid)   from epis_adj_crtn_emoluments_logpc  where pensionno = "
				+ Pensionno
				+ "     and batchid ="
				+ minBatchId
				+ "       and originalrecord = 'N'    and monthyear = '"
				+ monthYear + "')";
		log.info("--sqlQuery------" + sqlQuery);

		rs1 = st.executeQuery(sqlQuery);
		if (rs1.next()) {

			if (rs1.getString("OLDEMOLUMENTS") != null) {
				emoluments = rs1.getString("OLDEMOLUMENTS");
			} else {
				emoluments = "0";
			}
			if (rs1.getString("OLDEMPPFSTATUARY") != null) {
				epf = rs1.getString("OLDEMPPFSTATUARY");
			} else {
				epf = "0";
			}
			if (rs1.getString("OLDEMPVPF") != null) {
				empvpf = rs1.getString("OLDEMPVPF");
			} else {
				empvpf = "0";
			}
			if (rs1.getString("OLDPRINCIPLE") != null) {
				principle = rs1.getString("OLDPRINCIPLE");
			} else {
				principle = "0";
			}
			if (rs1.getString("OLDINTEREST") != null) {
				interest = rs1.getString("OLDINTEREST");
			} else {
				interest = "0";
			}
			if (rs1.getString("OLDPENSIONCONTRI") != null) {
				pensionContri = rs1.getString("OLDPENSIONCONTRI");
			} else {
				pensionContri = "0";
			}
			log.info("--minBatchId---=" + minBatchId);
			if (batchId.equals(minBatchId)) {
				editTransFlag = "Y";
			} else {
				editTransFlag = "N";
			}
			al.add(emoluments);
			al.add(epf);
			al.add(empvpf);
			al.add(principle);
			al.add(interest);
			al.add(pensionContri);
			al.add(editTransFlag);
			log.info("--emoluments---" + emoluments + "---epf----" + epf
					+ "---empvpf------" + empvpf + "----principle----"
					+ principle + "----interest--" + interest
					+ "---pensionContri--" + pensionContri
					+ "--editTransFlag---" + editTransFlag);
		}

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		try {
			rs1.close();
			commonDB.closeConnection(null, st, rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	return al;
}
private ArrayList getEmployeePensionCardForAdjCrtnforPc(Connection con,
		String fromDate, String toDate, String pfIDS, String wetherOption,
		String region, boolean formFlag, String dateOfRetriment,
		String dateOfBirth, String pensionNo, String finalSettlmentMonth, String resttlementdate,boolean isFrozenAvail) {

	DecimalFormat df = new DecimalFormat("#########0.00");

	Statement st = null;
	ResultSet rs = null;
	ResultSet rs5 = null;
	EmployeePensionCardInfo cardInfo = null;
	ArrayList pensionList = new ArrayList();
	boolean flag = false;
	boolean contrFlag = false, chkDOBFlag = false,  rareCaseflag = false, finalSettlementFlag = false, yearBreakMonthFlag = false, dataAfterFinalsettlemnt = false,resettlementFlag= false;
	boolean arrearsFlag = false;
	String checkDate = "", chkMnthYear = "Apr-1995", emolumentsMonths = "1", arrearFlag = "N";
	String monthYear = "", days = "", getMonth = "", sqlQuery = "", findFromYear = "",addcontrisql="", findToYear = "", finalSettlmentYear = "";
	int getDaysBymonth = 0, monthsCntAfterFinstlmnt = 0;
	long transMntYear = 0, empRetriedDt = 0;
	log.info("checkDate==" + checkDate + "flag===" + flag);
	double totalAdvancePFWPaid = 0, loanPFWPaid = 0, advancePFWPaid = 0, empNet = 0, aaiNet = 0, advPFDrawn = 0, empCumlative = 0.0, arrearEmpCumlative = 0.0, arrearAaiNetCumlative = 0, aaiPF = 0.0, aaiNetCumlative = 0.0, grandEmpCumlative = 0.0, grandAaiCumlative = 0.0, grandArrearEmpCumlative = 0.0, grandArrearAaiCumlative = 0.0;
	double pensionAsPerOption = 0.0, pensionContriTot = 0.0, pensionContriArrearTot = 0.0;
	double totalAdvances = 0;
	double additionalContri=0.0,netepf=0.0;
	boolean obFlag = false;
	boolean loanAdvFlag = false;
	ArrayList addConList=new ArrayList();
	try {
		findFromYear = commonUtil.converDBToAppFormat(fromDate,
				"dd-MMM-yyyy", "yyyy");
		findToYear = commonUtil.converDBToAppFormat(toDate, "dd-MMM-yyyy",
				"yyyy");
		if (!finalSettlmentMonth.equals("---")) {
			finalSettlmentYear = commonUtil.converDBToAppFormat(
					finalSettlmentMonth, "MMM-yyyy", "yyyy");
		}

	} catch (InvalidDataException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}
	if (Integer.parseInt(findFromYear) >= 2008) {
		yearBreakMonthFlag = true;
		pfIDS = "";
		if (Integer.parseInt(findFromYear) >= 2013 && Integer.parseInt(findToYear) > 2013 ) {
			loanAdvFlag=true;
		sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-add_months(to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
				+ "SUBSTR(empdt.MONYEAR, 4, 4))),1))),-1,0,1,1) as signs,emp.* from (select distinct to_char(to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
				+ "from employee_pension_validate where rownum <=to_date('"
				+ toDate
				+ "', 'dd-mon-yyyy') -to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT add_months(MONTHYEAR,-1) as MONTHYEAR,to_char(add_months(MONTHYEAR,-1), 'MONYYYY') empmonyear,cpfaccno as CPFACCNO,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
				+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST," 
				+"ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION,nvl(additionalcontri,'0') as additionalcontri,nvl(DATAMODIFIEDFLAG,'N') as DATAMODIFIEDFLAG FROM epis_adj_crtnpc  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= add_months('"
				+ fromDate
				+ "',1) and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=add_months(last_day('"
				+ toDate
				+ "'),1))"
				+ " AND PENSIONNO="
				+ pensionNo
				+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
		}else{
		sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
			+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs ,emp.* from (select distinct to_char(to_date('"
			+ fromDate
			+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
			+ "from employee_pension_validate where rownum <=to_date('"
			+ toDate
			+ "', 'dd-mon-yyyy') -to_date('"
			+ fromDate
			+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO ,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
			+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,"
			+ "ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION FROM epis_adj_crtnpc  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
			+ fromDate
			+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
			+ toDate
			+ "'))"
			+ " AND PENSIONNO="
			+ pensionNo
			+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
		}
	}/* else {
		yearBreakMonthFlag = false;
		sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
				+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs,emp.* from (select distinct to_char(to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
				+ "from epis_adj_crtn where rownum <=to_date('"
				+ toDate
				+ "', 'dd-mon-yyyy') -to_date('"
				+ fromDate
				+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
				+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,CPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,round(AAICONPF) AS AAICONPF,ROUND(CPFADVANCE) AS CPFADVANCE,ROUND(DEDADV) AS DEDADV,"
				+ "ROUND(REFADV) AS REFADV,AIRPORTCODE,EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS,PFCARDNARRATION,SUPPLIFLAG,PFCARDNRFLAG FROM EMPLOYEE_PENSION_VALIDATE  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
				+ fromDate
				+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
				+ toDate
				+ "'))"
				+ " AND ("
				+ pfIDS
				+ ")) emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";
	}
	sqlQuery = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR,decode((sign(sysdate-to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' ||"
			+ "SUBSTR(empdt.MONYEAR, 4, 4))))),-1,0,1,1) as signs ,emp.* from (select distinct to_char(to_date('"
			+ fromDate
			+ "', 'dd-mon-yyyy') + rownum - 1,'MONYYYY') monyear "
			+ "from epis_adj_crtn where rownum <=to_date('"
			+ toDate
			+ "', 'dd-mon-yyyy') -to_date('"
			+ fromDate
			+ "', 'dd-mon-yyyy') + 1) empdt,(SELECT MONTHYEAR,to_char(MONTHYEAR, 'MONYYYY') empmonyear,cpfaccno as CPFACCNO ,ROUND(EMOLUMENTS) AS EMOLUMENTS,"
			+ "round(EMPPFSTATUARY) AS EMPPFSTATUARY,arrearflag as arrearflag,round(EMPVPF) AS EMPVPF,round(EMPADVRECPRINCIPAL) AS EMPADVRECPRINCIPAL,round(EMPADVRECINTEREST) AS EMPADVRECINTEREST,"
			+ "ADVANCE as ADVANCE, PFWSUB as PFWSUB,PFWCONTRI as PFWCONTRI, AIRPORTCODE,region, EMPFLAG,PENSIONCONTRI,PF,emolumentmonths,MERGEFLAG,REMARKS, SUPPLIFLAG,PFCARDNRFLAG,PFCARDNARRATION FROM epis_adj_crtn  WHERE empflag='Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= '"
			+ fromDate
			+ "' and to_date(to_char(monthyear,'dd-Mon-yyyy'))<=last_day('"
			+ toDate
			+ "'))"
			+ " AND PENSIONNO="
			+ pensionNo
			+ ") emp where empdt.monyear =  emp.empmonyear  (+) ORDER BY TO_DATE(EMPMNTHYEAR)";*/

	log.info("FinanceReportDAO::getEmployeePensionCardForAdjCrtn"
			+ sqlQuery);
	ArrayList OBList = new ArrayList();
	try {

		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);

		while (rs.next()) {
			cardInfo = new EmployeePensionCardInfo();
			double total = 0.0;

			if (rs.getString("MONTHYEAR") != null) {
				cardInfo.setMonthyear(commonUtil.getDatetoString(rs
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
				OBList = this.getOBForAdjCrtn(con, cardInfo.getMonthyear(),
						pensionNo);
				cardInfo.setObList(OBList);
				cardInfo.setObFlag("Y");
				obFlag = true;
				getMonth = "";
			} else {
				cardInfo.setObFlag("N");
			}
			if (rs.getString("CPFACCNO") != null) {
				cardInfo.setCpfAccno(rs.getString("CPFACCNO"));
			} else {
				cardInfo.setCpfAccno("");
			}
			if (rs.getString("EMOLUMENTS") != null) {
				cardInfo.setEmoluments(rs.getString("EMOLUMENTS"));
			} else {
				cardInfo.setEmoluments("0");
			}
			if (rs.getString("arrearflag") != null) {
				arrearFlag = rs.getString("arrearflag");
			} else {
				arrearFlag = "N";
			}
			cardInfo.setTransArrearFlag(arrearFlag);
			if (arrearFlag.equals("Y") && arrearsFlag == false) {
				arrearsFlag = true;
			}

			log.info("============================MonthYear"
					+ cardInfo.getMonthyear() + "Emoluments"
					+ cardInfo.getEmoluments() + "finalSettlmentMonth"
					+ finalSettlmentMonth + "arrearFlag" + arrearFlag
					+ "arrearsFlag" + arrearsFlag);
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
			/*
			 * if (rs.getString("CPF") != null) {
			 * cardInfo.setEmpCPF(rs.getString("CPF")); } else {
			 * cardInfo.setEmpCPF("0"); }
			 */
			if (rs.getString("SUPPLIFLAG") != null) {
				cardInfo.setSupflag(rs.getString("SUPPLIFLAG"));
			} else {
				cardInfo.setSupflag("N");
			}
			
//			To Disply PFCARDNARRATION irrespective of arrears,Suppli or Merge Data
			if (rs.getString("PFCARDNRFLAG") != null) {
				cardInfo.setPfcardNarrationFlag(rs.getString("PFCARDNRFLAG"));
			} else {
				cardInfo.setPfcardNarrationFlag("N");
			}
			
			/*
			 * if (region.equals("CHQNAD")) {
			 * 
			 * if (rs.getString("CPFADVANCE") != null) {
			 * cardInfo.setPrincipal(rs.getString("CPFADVANCE")); } else {
			 * cardInfo.setPrincipal("0"); } } else if (region.equals("North
			 * Region")) { if (rs.getString("REFADV") != null) {
			 * cardInfo.setPrincipal(rs.getString("REFADV")); } else {
			 * cardInfo.setPrincipal("0"); } }
			 */{
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
			if (rs.getString("ADVANCE") != null) {
				cardInfo.setAdvancesAmount(rs.getString("ADVANCE"));
			} else {
				cardInfo.setAdvancesAmount("0");
			}
			if (rs.getString("PFWSUB") != null) {
				cardInfo.setPFWSubscri(rs.getString("PFWSUB"));
			} else {
				cardInfo.setPFWSubscri("0");
			}
			if (rs.getString("PFWCONTRI") != null) {
				cardInfo.setPFWContri(rs.getString("PFWCONTRI"));
			} else {
				cardInfo.setPFWContri("0");
			}
			loanPFWPaid = Double.parseDouble(cardInfo.getAdvancesAmount());
			advancePFWPaid = Double.parseDouble(cardInfo.getPFWSubscri());

			totalAdvancePFWPaid = loanPFWPaid + advancePFWPaid;

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
			//System.out.println("findFromYear000=="+Integer.parseInt(findFromYear));
			if(Integer.parseInt(findFromYear)>=2015) {
				
				/*if(rs.getString("DATAMODIFIEDFLAG").equals("N")){
						st = null;
						st = con.createStatement();
						addcontrisql = " update epis_adj_crtnpc c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
								+ pensionNo+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+pensionNo+" and monthyear ='01-May-2015'";
						log.info("----------addcontrisql-------------" + addcontrisql);
						rs5 = st.executeQuery(addcontrisql);					
				}*/
				//System.out.println("findFromYear=="+Integer.parseInt(findFromYear));
				//additionalcalculation.additionalContributionCalculation(con, cardInfo.getEmoluments(),pensionNo,cardInfo.getMonthyear(),
				//		dateOfRetriment,dateOfBirth);
				//System.out.println("additionalcontri==="+rs.getString("additionalcontri"));
				if(rs.getString("additionalcontri")!=null) {
					//additionalContri=commonDAO.additionalContriCalculation(cardInfo.getMonthyear(),cardInfo.getEmoluments());
					cardInfo.setAdditionalContri(rs.getString("additionalcontri"));
				}
				else {
					cardInfo.setAdditionalContri("0.0");
				}
				
				if( Date.parse(cardInfo.getMonthyear())<Date.parse("01-Sep-2014") &&
						Date.parse(cardInfo.getMonthyear())>=Date.parse("01-Apr-2014")) {
					cardInfo.setAdditionalContri("0.0");						
				}
				additionalContri=Double.parseDouble(cardInfo.getAdditionalContri());
				//if(cardInfo.getMonthyear().equals("01-Apr-2015")) {
				//	addConList=this.pfCardAdditionalContriCalculation(pensionNo);
				//	additionalContri=Double.parseDouble(addConList.get(8).toString());
				//}
				
				netepf=Double.parseDouble(cardInfo.getEmppfstatury())-additionalContri;					
				cardInfo.setNetEPF(Double.toString(netepf));

				total = new Double(df.format(Double.parseDouble(cardInfo
						.getNetEPF().trim())
						+ Double.parseDouble(cardInfo.getEmpvpf().trim())
						+ Double.parseDouble(cardInfo.getPrincipal().trim())
						+ Double.parseDouble(cardInfo.getInterest().trim())))
						.doubleValue();
				//System.out.println("netepf=="+netepf);
				//System.out.println("additionalContri=="+additionalContri);
				
				//System.out.println("total=="+total);
			}
			
			
			
			/*
			 * if (formFlag == true) { loanPFWPaid =
			 * this.getEmployeeLoans(con, cardInfo .getShnMnthYear(), pfIDS,
			 * "ADV.PAID", pensionNo); advancePFWPaid =
			 * this.getEmployeeAdvances(con, cardInfo .getShnMnthYear(),
			 * pfIDS, "ADV.PAID", pensionNo); log.info("Region" + region +
			 * "loanPFWPaid" + loanPFWPaid + "advancePFWPaid" +
			 * advancePFWPaid); totalAdvancePFWPaid = loanPFWPaid +
			 * advancePFWPaid; }
			 * 
			 * cardInfo.setAdvancesAmount(Double.toString(Math
			 * .round(advancePFWPaid)));
			 */

			log.info("findFromYear======================" + findFromYear
					+ "finalSettlmentYear" + finalSettlmentYear
					+ "findToYear" + findToYear);

			if (!finalSettlmentMonth.equals("---")) {
				if (commonDAO.compareFinalSettlementDates(fromDate, "31-Mar-"
						+ findToYear, "02-" + finalSettlmentMonth) == true) {

					finalSettlementFlag = commonUtil.compareTwoDates(
							finalSettlmentMonth, checkDate);
				} else {
					/*
					 * if(cardInfo.getTransArrearFlag().equals("Y")&&
					 * commonUtil.getDateDifference("01-Apr-2010",fromDate)>=0){
					 * finalSettlementFlag = true; }else{
					 * finalSettlementFlag = false; }
					 */

					if (commonUtil.compareTwoDates(commonUtil
							.converDBToAppFormat("01-Mar-2011",
									"dd-MMM-yyyy", "MMM-yyyy"), commonUtil
							.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
									"MMM-yyyy")) == true) {
						if (finalSettlmentMonth.equals("Mar-"
								+ findFromYear)) {
							if (commonUtil.getDifferenceTwoDatesInDays(
									"31-" + finalSettlmentMonth, cardInfo
											.getMonthyear()) > -30) {
								rareCaseflag = true;
							}
						}
					}
					if (rareCaseflag == true) {
						finalSettlementFlag = true;
					} else {
						finalSettlementFlag = false;
					}

				}

			} else {

				finalSettlementFlag = false;
			}
			
			
			
			// code for retriving data after finalsettlement

			if (finalSettlementFlag == true) {
				dataAfterFinalsettlemnt = true;
				monthsCntAfterFinstlmnt++;
			}
			
			log.info("Two dates informaiton"+commonUtil.compareTwoDates(commonUtil.converDBToAppFormat(
					"01-Mar-2010", "dd-MMM-yyyy", "MMM-yyyy"), commonUtil
					.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
							"MMM-yyyy")));
			if (commonUtil.compareTwoDates(commonUtil.converDBToAppFormat(
					"01-Mar-2010", "dd-MMM-yyyy", "MMM-yyyy"), commonUtil
					.converDBToAppFormat(fromDate, "dd-MMM-yyyy",
							"MMM-yyyy")) == true
					&& !finalSettlmentMonth.equals("---")) {

				if (!(resttlementdate.equals("---"))) {
					if (commonDAO.compareFinalSettlementDates(fromDate,
							"31-Mar-" + findToYear, "02-"
									+ commonUtil.converDBToAppFormat(
											resttlementdate, "dd-MMM-yyyy",
											"MMM-yyyy")) == true) {
						resettlementFlag = commonUtil.compareTwoDates(
								commonUtil.converDBToAppFormat(
										resttlementdate, "dd-MMM-yyyy",
										"MMM-yyyy"), checkDate);
					}
				}
				if (resettlementFlag == true) {
					finalSettlementFlag = true;
					arrearsFlag = false;

				}

			}
			if (isFrozenAvail==true && arrearFlag.equals("Y")){
				//arrearsFlag=true;
				cardInfo.setTransArrearFlag("Y");
			}
			log.info("finalSettlementFlag======================"
					+ finalSettlementFlag + "checkDate" + checkDate
					+ "resettlementFlag" + resettlementFlag
					+ "finalSettlmentMonth" + finalSettlmentMonth
					+ "rareCaseflag" + rareCaseflag + "finalSettlementFlag"
					+ finalSettlementFlag+"isFrozenAvail"+isFrozenAvail);
			
			empNet = total - totalAdvancePFWPaid;
			rareCaseflag = false;
			resettlementFlag = false;
			
			if (arrearsFlag == true) {
				arrearEmpCumlative = arrearEmpCumlative + empNet;

				cardInfo.setArrearEmpNetCummulative(Double
						.toString(arrearEmpCumlative));

				grandArrearEmpCumlative = grandArrearEmpCumlative
						+ arrearEmpCumlative;

				cardInfo.setGrandArrearEmpNetCummulative(Double
						.toString(Math.round(grandArrearEmpCumlative)));
			}
			cardInfo.setEmptotal(Double.toString(Math.round(total)));
			cardInfo.setAdvancePFWPaid(Double.toString(Math
					.round(totalAdvancePFWPaid)));
			cardInfo.setEmpNet((Double.toString(Math.round(empNet))));

			// code for retriving empNet data after finalsettlement
			if (dataAfterFinalsettlemnt == true) {
				cardInfo.setDataAfterFinalsettlemnt(String
						.valueOf(dataAfterFinalsettlemnt));
				cardInfo.setAftrFinstlmntEmpNetTot(Double.toString(Math
						.round(empNet)));
			}

			if (finalSettlementFlag == true) {
				empNet = 0;
			}
			empCumlative = empCumlative + empNet;
			
			if(cardInfo.getMonthyear().equals("01-Mar-2014") || cardInfo.getMonthyear().equals("01-Mar-2015") || cardInfo.getMonthyear().equals("01-Mar-2016")){
				finalSettlementFlag=true;
			}
			if (finalSettlementFlag == false) {
				if (Integer.parseInt(findFromYear) >= 2011) {
					cardInfo.setYearFlag(true);
					if (rs.getInt("signs") == 1) {

						grandEmpCumlative = grandEmpCumlative + empNet;
						cardInfo.setGrandCummulative(Double.toString(Math
								.round(grandEmpCumlative)));
					} else {
						cardInfo.setGrandCummulative(Double.toString(Math
								.round(0)));
					}

				} else {
					grandEmpCumlative = grandEmpCumlative + empCumlative;
					cardInfo.setGrandCummulative(Double.toString(Math
							.round(grandEmpCumlative)));
				}

			}

			cardInfo.setEmpNetCummulative(Double.toString(empCumlative));
			/*
			 * (if (rs.getString("AAICONPF") != null) {
			 * cardInfo.setAaiPF(rs.getString("AAICONPF")); } else {
			 * cardInfo.setAaiPF("0"); }
			 */
			if (rs.getString("emolumentmonths") != null) {
				emolumentsMonths = rs.getString("emolumentmonths");
			} else {
				emolumentsMonths = "1";
			}
			cardInfo.setEmolumentMonths(emolumentsMonths);

			pensionAsPerOption = rs.getDouble("PENSIONCONTRI");
			cardInfo.setPensionContribution(Double
					.toString(pensionAsPerOption));

			log.info("flag" + flag + checkDate + "Pension"
					+ cardInfo.getPensionContribution());
			if (formFlag == true) {
				advPFDrawn = commonDAO.getEmployeeLoans(con, cardInfo
						.getShnMnthYear(), pfIDS, "ADV.DRAWN", pensionNo);
			}

			aaiPF = rs.getDouble("PF");

			cardInfo.setAaiPF(Double.toString(aaiPF));
			cardInfo.setPfDrawn(Double.toString(advPFDrawn));
			aaiNet = Double.parseDouble(cardInfo.getAaiPF()) - advPFDrawn;
			log.info("aaiPF=======================================" + aaiPF
					+ "advPFDrawn" + advPFDrawn + "aaiNet" + aaiNet);
			cardInfo.setAaiNet(Double.toString(aaiNet));
			if (arrearsFlag == true) {
				arrearAaiNetCumlative = arrearAaiNetCumlative + aaiNet;
				cardInfo.setArrearAaiCummulative(Double
						.toString(arrearAaiNetCumlative));
				grandArrearAaiCumlative = grandArrearAaiCumlative
						+ arrearAaiNetCumlative;
				cardInfo.setGrandArrearAAICummulative(Double
						.toString(grandArrearAaiCumlative));
			}

			// code for retriving AAINet data after finalsettlement
			if (dataAfterFinalsettlemnt == true) {
				cardInfo.setDataAfterFinalsettlemnt(String
						.valueOf(dataAfterFinalsettlemnt));
				cardInfo.setAftrFinstlmntAAINetTot(Double.toString(Math
						.round(aaiNet)));
			}

			if (finalSettlementFlag == true) {
				aaiNet = 0;
			}

			aaiNetCumlative = aaiNetCumlative + aaiNet;

			cardInfo.setAaiCummulative(Double.toString(aaiNetCumlative));				 
			if (finalSettlementFlag == false) {
				if (Integer.parseInt(findFromYear) >= 2011) {
					cardInfo.setYearFlag(true);
					if (rs.getInt("signs") == 1) {
						grandAaiCumlative = grandAaiCumlative + aaiNet;
						cardInfo.setGrandAAICummulative(Double
								.toString(Math.round(grandAaiCumlative)));
					} else {
						cardInfo.setGrandAAICummulative(Double
								.toString(Math.round(0)));
					}
				} else {
					grandAaiCumlative = grandAaiCumlative + aaiNetCumlative;
					cardInfo.setGrandAAICummulative(Double.toString(Math
							.round(grandAaiCumlative)));
				}

			}
			
			// code for retriving PensionContri data after finalsettlement
			if (dataAfterFinalsettlemnt == true) {
				cardInfo.setDataAfterFinalsettlemnt(String
						.valueOf(dataAfterFinalsettlemnt));
				cardInfo.setAftrFinstlmntPCNetTot(Double.toString(Math
						.round(pensionAsPerOption)));
			}

			// pensionContriTot is the total of pc values except arrear
			if (finalSettlementFlag == false) {
				cardInfo.setPensionContriAmnt(Double.toString(Math
						.round(pensionAsPerOption)));
			} else {
				cardInfo.setPensionContriAmnt("0");
			}
			log.info("======pensionContriTot========" + pensionAsPerOption
					+ "=======" + cardInfo.getPensionContriAmnt());
			if (finalSettlementFlag == true) {
				pensionContriTot = 0;
			}
			if (arrearsFlag == true) {

				cardInfo.setPensionContriArrearAmnt(Double.toString(Math
						.round(pensionAsPerOption)));
			}

			if (rs.getString("AIRPORTCODE") != null) {
				cardInfo.setStation(rs.getString("AIRPORTCODE"));
			} else {
				cardInfo.setStation("---");
			}

			if (rs.getString("MERGEFLAG") != null) {
				cardInfo.setMergerflag(rs.getString("MERGEFLAG"));
			} else {
				cardInfo.setMergerflag("N");
			}
			if (rs.getString("region") != null) {
				cardInfo.setRegion(rs.getString("region"));
			} else {
				cardInfo.setRegion("");
			}
			if (cardInfo.getMergerflag().equals("Y")) {
				if (rs.getString("REMARKS") != null) {
					cardInfo.setMergerremarks(rs.getString("REMARKS"));
				} else {
					cardInfo.setMergerremarks("---");
				}
			} else {
				cardInfo.setMergerremarks("---");
			}
			 
			if (rs.getString("PFCARDNARRATION") != null) {
				cardInfo
						.setPfcardNarration(rs.getString("PFCARDNARRATION"));
			} else {
				cardInfo.setPfcardNarration("---");
			}
			
			log.info("=====PFCARDNARRATION===="+cardInfo.getPfcardNarration());
			finalSettlementFlag = false;
			pensionList.add(cardInfo);
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return pensionList;
}
public int insertRecordForAdjCtrnTrackingforPc(Connection con,
		String empserialNO, String cpfAccno, String adjOBYear,
		String reasonForInsert, String username, String ipaddress)
		throws InvalidDataException {

	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "", insertQry = "", updateQry = "", reason = "", insertcondition = "", updateondition = "",adjProcessedYear="01-Apr-2011";
	int result = 0, trackingId = 0;
	try {

		st = con.createStatement();
		if (reasonForInsert.equals("Edited")) {
			reason = "E";
			insertcondition = "DATAMODIFIED";
			updateondition = "DATAMODIFIED='" + reason;
			// sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking
			// where DATAMODIFIED='E' and pensionno="+empserialNO;
		} else if (reasonForInsert.equals("Mapped")) {
			reason = "M";
			insertcondition = "DATAMAPPED";
			updateondition = "DATAMAPPED='" + reason;
			// sqlQuery = "select pensionno from epis_adj_crtn_pfid_tracking
			// where DATAMAPPED='M' and pensionno="+empserialNO;

		} else if (reasonForInsert.equals("Upload")) {
			reason = "U";
			insertcondition = "DATAMAPPED";
			updateondition = "DATAMAPPED='" + reason;

		}
		if (reasonForInsert.equals("Edited")) {
			// common Cond
			sqlQuery = "select pensionno from epis_adj_crtn_pfid_trackingpc where   pensionno="
					+ empserialNO + " and adjobyear='" + adjOBYear + "'";
		} else if (reasonForInsert.equals("Mapped")
				|| reasonForInsert.equals("Upload")) {
			sqlQuery = "select pensionno from epis_adj_crtn_pfid_trackingpc where   pensionno="
					+ empserialNO;
		}
		log.info("===adjOBYear===" + adjOBYear + "=cpfAccno==" + cpfAccno
				+ "-");
		updateQry = " update epis_adj_crtn_pfid_trackingpc set  "
				+ updateondition + "',USERNAME='" + username
				+ "',COMPUTERNAME='" + ipaddress
				+ "', UPDATEDDATE= sysdate where pensionno =" + empserialNO
				+ " and ADJOBYEAR='" + adjOBYear + "'";

		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			log
					.info("AdjCrtnDAO::insertRecordForAdjCtrnTracking()==updateQry=="
							+ updateQry);
			result = st.executeUpdate(updateQry);
		} else {

			trackingId = this.getPfidTrackingId(con);
			insertQry = "insert into epis_adj_crtn_pfid_trackingpc(PENSIONNO,CPFACNO,ADJOBYEAR,"
					+ insertcondition
					+ ",USERNAME,COMPUTERNAME,UPDATEDDATE,TRACKINGID,ADJPROCESSEDYEAR)values ("
					+ empserialNO
					+ ",'"
					+ cpfAccno
					+ "','"
					+ adjOBYear
					+ "','"
					+ reason
					+ "','"
					+ username
					+ "','"
					+ ipaddress
					+ "',sysdate," + trackingId + ",'"+Constants.FORM2_CURRENT_ADJOBYEAR+"')";

			log
					.info("AdjCrtnDAO::insertRecordForAdjCtrnTracking()==insertQry=="
							+ insertQry);
			result = st.executeUpdate(insertQry);
		}

	} catch (SQLException e) {
		throw new InvalidDataException(e.getMessage());
	} catch (Exception e) {
		throw new InvalidDataException(e.getMessage());
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return result;
}
public String getPCTotalsTransIdforPc(Connection con, String pensionno,
		String reportYear) {
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "", transId = "";

	try {
		st = con.createStatement();
		sqlQuery = "select max(transid) as transid from  epis_adj_crtn_prv_pc_totalspc where pensionno="
				+ pensionno + " and adjobyear='" + reportYear + "'";
		log.info("----sqlQuery---------" + sqlQuery);
		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			if(rs.getString("transid")!=null){
			transId = rs.getString("transid");
			} 
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return transId;
}
public String getAdjCrtnNotFinalizedTransIdforPc(Connection con,
		String empserialNO, String reportYear) {
	log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId Entering method");

	Statement st = null;
	ResultSet rs = null;

	String selectSQL = "", notFianalizetransID = "", fromYear = "", toYear = "";
	String years[] = null;
	years = reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	selectSQL = "select distinct transid  as transid from epis_adj_crtn_emol_log_temppc where pensionno= "
			+ empserialNO
			+ " and monthyear between '"
			+ fromYear
			+ "' and '" + toYear + "'";

	try {
		log.info("selectSQL==" + selectSQL);

		st = con.createStatement();
		rs = st.executeQuery(selectSQL);
		if (rs.next()) {
			if (rs.getString("transid") != null) {
				notFianalizetransID = rs.getString("transid");
			} else {
				notFianalizetransID = "";
			}

		}

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	log.info("AdjCrtnDAO: getAdjCrtnNoFinalizedTransId leaving method");
	return notFianalizetransID;
}
public String insertAdjEmolumenstLogforPc(Connection con, String pensionno,
		String reportYear, String notFianalizetransID) {
	int count = 0, batchId = 0, result = 0;

	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "", transSqlQuery = "", transId = "";
	EmolumentslogBean bean = new EmolumentslogBean();
	ArrayList adjEmolList = new ArrayList();
	try {

		st = con.createStatement();
		batchId = this.getBatchId(con);
		if (notFianalizetransID.equals("")) {
			transId = this.getPCTotalsTransIdforPc(con, pensionno, reportYear);
		} else {
			transId = notFianalizetransID;
		}
		adjEmolList = this.getDataFromAdjEmolumentsLogTempforPc(con, pensionno,
				transId, reportYear);
		for (int i = 0; i < adjEmolList.size(); i++) {
			bean = (EmolumentslogBean) adjEmolList.get(i);
			sqlQuery = "insert into  epis_adj_crtn_emoluments_logpc "
					+ " (pensionno,  cpfacno ,   monthyear,  oldemoluments , newemoluments ,  oldemppfstatuary,newemppfstatuary,  oldempvpf ,  newempvpf , oldprinciple ,  newprinciple , oldinterest ,  newinterest ,  oldpensioncontri  , newensioncontri , updateddate  , remarks  , region	 , username,  computername,batchid,originalrecord,transid) values('"
					+ bean.getPensionNo() + "','" + bean.getCpfAcno()
					+ "','" + bean.getMonthYear() + "','"
					+ bean.getOldEmoluments() + "','"
					+ bean.getNewEmoluments() + "','"
					+ bean.getOldEmppfstatury() + "','"
					+ bean.getNewEmppfstatury() + "','"
					+ bean.getOldEmpvpf() + "','" + bean.getNewEmpvpf()
					+ "','" + bean.getOldPrincipal() + "','"
					+ bean.getNewPrincipal() + "','"
					+ bean.getOldInterest() + "','" + bean.getNewInterest()
					+ "','" + bean.getOldPensioncontri() + "','"
					+ bean.getNewPensioncontri() + "', sysdate,'"
					+ bean.getRemarks() + "','" + bean.getRegion() + "','"
					+ bean.getUserName() + "','" + bean.getComputerName()
					+ "'," + batchId + ",'" + bean.getOriginalRecord()
					+ "'," + transId + ")";

			log.info("====insertAdjEmolumenstLog==============" + sqlQuery);
			log.info("====result==============" + result);
			result = st.executeUpdate(sqlQuery);

			transSqlQuery = "update epis_adj_crtnpc  set DataModifiedFlag='Y' where  pensionno ="
					+ bean.getPensionNo()
					+ " and empflag='Y' and monthyear='"
					+ bean.getMonthYear() + "'";
			log
					.info("====insertAdjEmolumenstLog========transSqlQuery======"
							+ transSqlQuery);
			st.executeUpdate(transSqlQuery);

		}

		this.deleteDataInAdjCrtnEmolLogTempforPc(con, pensionno, reportYear);

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return transId;
}
public ArrayList getDataFromAdjEmolumentsLogTempforPc(Connection con,
		String pfid, String transid, String reportYear) {
	log
			.info("AdjCrtnDAO:getDataFromAdjEmolumentsLogTemp()-- Entering Method");

	String sqlQuery = "", prefixWhereClause = "", fromYear = "", toYear = "";
	EmolumentslogBean data = new EmolumentslogBean();
	Statement st = null;
	ResultSet rs = null;
	ArrayList emolumentsloginfo = null;
	String years[] = null;
	years = reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	sqlQuery = "select * from epis_adj_crtn_emol_log_temppc where pensionno="
			+ pfid + " and monthyear between '" + fromYear + "' and '"
			+ toYear + "'";

	log.info("sql query " + sqlQuery);
	try {
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery.toString());
		emolumentsloginfo = new ArrayList();
		while (rs.next()) {

			data = new EmolumentslogBean();
			if (rs.getString("PENSIONNO") != null) {
				data.setPensionNo(rs.getString("PENSIONNO"));
			} else {
				data.setPensionNo("");
			}
			if (rs.getString("CPFACNO") != null) {
				data.setCpfAcno(rs.getString("CPFACNO"));
			} else {
				data.setCpfAcno("");
			}
			if (rs.getString("MONTHYEAR") != null) {
				data.setMonthYear(commonUtil.converDBToAppFormat(rs
						.getDate("MONTHYEAR")));
			} else {
				data.setMonthYear("");
			}
			if (rs.getString("OLDEMOLUMENTS") != null) {
				data.setOldEmoluments(rs.getString("OLDEMOLUMENTS"));
			} else {
				data.setOldEmoluments("");
			}
			if (rs.getString("NEWEMOLUMENTS") != null) {
				data.setNewEmoluments(rs.getString("NEWEMOLUMENTS"));
			} else {
				data.setNewEmoluments("");
			}

			if (rs.getString("OLDEMPPFSTATUARY") != null) {
				data.setOldEmppfstatury(rs.getString("OLDEMPPFSTATUARY"));
			} else {
				data.setOldEmppfstatury("");
			}

			if (rs.getString("NEWEMPPFSTATUARY") != null) {
				data.setNewEmppfstatury(rs.getString("NEWEMPPFSTATUARY"));
			} else {
				data.setNewEmppfstatury("");
			}
			if (rs.getString("OLDEMPVPF") != null) {
				data.setOldEmpvpf(rs.getString("OLDEMPVPF"));
			} else {
				data.setOldEmpvpf("");
			}

			if (rs.getString("NEWEMPVPF") != null) {
				data.setNewEmpvpf(rs.getString("NEWEMPVPF"));
			} else {
				data.setNewEmpvpf("");
			}
			if (rs.getString("OLDPRINCIPLE") != null) {
				data.setOldPrincipal(rs.getString("OLDPRINCIPLE"));
			} else {
				data.setOldPrincipal("");
			}

			if (rs.getString("NEWPRINCIPLE") != null) {
				data.setNewPrincipal(rs.getString("NEWPRINCIPLE"));
			} else {
				data.setNewPrincipal("");
			}

			if (rs.getString("OLDINTEREST") != null) {
				data.setOldInterest(rs.getString("OLDINTEREST"));
			} else {
				data.setOldInterest("");
			}

			if (rs.getString("NEWINTEREST") != null) {
				data.setNewInterest(rs.getString("NEWINTEREST"));
			} else {
				data.setNewInterest("");
			}
			if (rs.getString("OLDPENSIONCONTRI") != null) {
				data.setOldPensioncontri(rs.getString("OLDPENSIONCONTRI"));
			} else {
				data.setOldPensioncontri("");
			}
			if (rs.getString("NEWENSIONCONTRI") != null) {
				data.setNewPensioncontri(rs.getString("NEWENSIONCONTRI"));
			} else {
				data.setNewPensioncontri("");
			}

			if (rs.getString("UPDATEDDATE") != null) {
				data.setUpdatedDate(CommonUtil.converDBToAppFormat(rs
						.getDate("UPDATEDDATE")));
			} else {
				data.setUpdatedDate("");
			}
			if (rs.getString("REMARKS") != null) {
				data.setRemarks(rs.getString("REMARKS"));
			} else {
				data.setRemarks("");
			}
			if (rs.getString("REGION") != null) {
				data.setRegion(rs.getString("REGION"));
			} else {
				data.setRegion("");
			}

			if (rs.getString("region") != null) {
				data.setRegion(rs.getString("region"));
			} else {
				data.setRegion("");
			}

			if (rs.getString("USERNAME") != null) {
				data.setUserName(rs.getString("USERNAME"));
			} else {
				data.setUserName("");
			}

			if (rs.getString("COMPUTERNAME") != null) {
				data.setComputerName(rs.getString("COMPUTERNAME"));
			} else {
				data.setComputerName("");
			}
			if (rs.getString("ORIGINALRECORD") != null) {
				data.setOriginalRecord(rs.getString("ORIGINALRECORD"));
			} else {
				data.setComputerName("");
			}
			emolumentsloginfo.add(data);
		}

		log.info("emolumentsloginfo list size " + emolumentsloginfo.size());

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return emolumentsloginfo;
}
public void deleteDataInAdjCrtnEmolLogTempforPc(Connection con,
		String pensionno, String reportYear) {
	Statement st = null;
	ResultSet rs = null;
	String deleteQuery = "", fromYear = "", toYear = "";
	int result = 0;
	EmolumentslogBean bean = new EmolumentslogBean();
	String years[] = null;
	years = reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	try {

		st = con.createStatement();

		deleteQuery = "delete from epis_adj_crtn_emol_log_temppc where pensionno="
				+ pensionno
				+ " and monthyear between '"
				+ fromYear
				+ "' and '" + toYear + "'";
		log.info("deleteDataInAdjCrtnEmolLogTemp()===deleteQuery== "
				+ deleteQuery);
		result = st.executeUpdate(deleteQuery);
		log.info("deleteDataInAdjCrtnEmolLogTemp()===result== " + result);
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}

}
public String getAdjCrtnFinalizedFlagforPc(Connection con, String empserialNO,
		String reportYear) {
	log
			.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag Entering method With Connection");

	Statement st = null;
	ResultSet rs = null;

	String selectSQL = "", flag = "", fromYear = "", toYear = "", finalizedFlag = "";
	String years[] = null;
	years = reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	selectSQL = "select 'X' as flag   from epis_adj_crtn_emol_log_temppc where pensionno= "
			+ empserialNO
			+ " and monthyear between '"
			+ fromYear
			+ "' and '" + toYear + "'";

	try {
		log.info("selectSQL==" + selectSQL);

		st = con.createStatement();
		rs = st.executeQuery(selectSQL);
		if (rs.next()) {
			flag = rs.getString("flag");
		}
		if (flag.equals("X")) {
			finalizedFlag = "NotFinalize";
		} else {
			finalizedFlag = "Finalized";
		}

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	log
			.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag leaving method With Connection");
	return finalizedFlag;
}
public ArrayList getPrevPCGrandTotalsForAdjCrtnforPc(String pfid,
		String adjOBYear, String batchid, String transIdToGetPrevData) {
	String query = "";
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	ArrayList grandTotList = new ArrayList();
	DecimalFormat df = new DecimalFormat("#########0");
	double emolumentsTotal = 0.00, CPFTotal = 0.0, PensionTotal = 0.00, PFTotal = 0.00, EmpSubscription = 0.00, AAIContribution = 0.00, adjEmpSubInterest = 0.00, adjAAiContriInterest = 0.00, adjPesnionContri = 0.00,adjFSEmpSubIntrst =0.00, adjFSContriIntrst =0.00,newadditionalcontri=0.0;

	String transid = "";
	try {
		if (transIdToGetPrevData.equals("")) {

			if (batchid.equals("")) {
				query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
						+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
						+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST,"
						+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST,sum(nvl(e.newadditionalcontri,0.0)) as newadditionalcontri from epis_adj_crtn_prv_pc_totalspc e"
						+ " where e.pensionno = "
						+ pfid
						+ "  and e.adjobyear ='"
						+ adjOBYear
						+ "' and e.transid = (select max(e.transid) from  epis_adj_crtn_prv_pc_totalspc e"
						+ " where e.pensionno = "
						+ pfid
						+ "   and e.adjobyear = '" + adjOBYear + "')";
			} else {
				query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
						+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
						+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST, "
						+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST,sum(nvl(e.newadditionalcontri,0.0)) as newadditionalcontri from epis_adj_crtn_prv_pc_totalspc e"
						+ " where e.pensionno = "
						+ pfid
						+ "  and e.adjobyear ='"
						+ adjOBYear
						+ "' and e.transid =(select distinct(transid) from  epis_adj_crtn_emoluments_logpc where pensionno="
						+ pfid + " and batchid=" + batchid + ")";
			}
		} else {
			// For Making 1 r more batches transaction with out Finalizing
			// and after Finalizing getting Ist entry of grand Totals
			/*
			 * query = "select max(transid) as transid,
			 * sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal,
			 * 0.00) + nvl(e.cpfinterest, 0.00)) as
			 * CPFTotal,sum(nvl(e.pensiontotal, 0.00) +
			 * nvl(e.pensioninterest, 0.00)) as PensionTotal," + "
			 * sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as
			 * PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest,
			 * 0.00)) as EmpSubscription, sum(nvl(e.ADJEMPSUBINTRST, 0.00))
			 * as ADJEMPSUBINTRST," + " sum(nvl(e.aaicontri, 0.00) +
			 * nvl(e.aaicontriinterest, 0.00)) as AAIContribution ,
			 * sum(nvl(e.ADJAAICONTRIINTRST, 0.00)) as ADJAAICONTRIINTRST,
			 * sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00)) as
			 * ADJPENSIONCONTRIINTRST from epis_adj_crtn_prv_pc_totals e" + "
			 * where e.pensionno = " + pfid + " and e.adjobyear ='" +
			 * adjOBYear + "' and e.transid = (select e.transid from
			 * epis_adj_crtn_prv_pc_totals e where e.pensionno = "+pfid+"
			 * and e.adjobyear = '" + adjOBYear + "' and " + " transid <
			 * (select max(e.transid) from epis_adj_crtn_prv_pc_totals e
			 * where e.pensionno = "+pfid+" and e.adjobyear = '" + adjOBYear +
			 * "'))";
			 */

			query = "select max(transid) as transid, sum(nvl(EMOLUMENTS,0.00)) as EMOLUMENTS,sum(nvl(e.cpftotal, 0.00) + nvl(e.cpfinterest, 0.00)) as CPFTotal,sum(nvl(e.pensiontotal, 0.00) + nvl(e.pensioninterest, 0.00)) as PensionTotal,"
					+ " sum(nvl(e.pftotal, 0.00) + nvl(e.pfinterest, 0.00)) as PFTotal,sum(nvl(e.empsub, 0.00) + nvl(e.empsubinterest, 0.00)) as EmpSubscription,  sum(nvl(e.ADJEMPSUBINTRST, 0.00))  as ADJEMPSUBINTRST,"
					+ " sum(nvl(e.aaicontri, 0.00) + nvl(e.aaicontriinterest, 0.00)) as AAIContribution , sum(nvl(e.ADJAAICONTRIINTRST, 0.00))  as ADJAAICONTRIINTRST, sum(nvl(e.ADJPENSIONCONTRIINTRST, 0.00))  as ADJPENSIONCONTRIINTRST,"
					+ " sum(nvl(e.ADJFINEMPSUBINTRST, 0.00)) as  ADJFINEMPSUBINTRST, sum(nvl(e.ADJFINAAICONTRIINTRST, 0.00)) as  ADJFINAAICONTRIINTRST,sum(nvl(e.newadditionalcontri,0.0)) as newadditionalcontri from epis_adj_crtn_prv_pc_totalspc e"
					+ " where e.pensionno = "
					+ pfid
					+ "  and e.adjobyear ='"
					+ adjOBYear
					+ "' and e.transid = " + transIdToGetPrevData;

		}
		con = DBUtils.getConnection();
		st = con.createStatement();
		log.info("----getPrevPCGrandTotalsForAdjCrtn--query--" + query);
		rs = st.executeQuery(query);

		if (rs.next()) {

			if (rs.getString("transid") != null) {
				transid = rs.getString("transid");
			}
			if (rs.getString("EMOLUMENTS") != null) {
				emolumentsTotal = rs.getDouble("EMOLUMENTS");
			}
			if (rs.getString("CPFTotal") != null) {
				CPFTotal = rs.getDouble("CPFTotal");
			}
			if (rs.getString("PensionTotal") != null) {
				PensionTotal = rs.getDouble("PensionTotal");
			}
			if (rs.getString("PFTotal") != null) {
				PFTotal = rs.getDouble("PFTotal");
			}
			if (rs.getString("EmpSubscription") != null) {
				EmpSubscription = rs.getDouble("EmpSubscription");
			}
			if (rs.getString("AAIContribution") != null) {
				AAIContribution = rs.getDouble("AAIContribution");
			}
			if (rs.getString("ADJEMPSUBINTRST") != null) {
				adjEmpSubInterest = rs.getDouble("ADJEMPSUBINTRST");
			}
			if (rs.getString("ADJAAICONTRIINTRST") != null) {
				adjAAiContriInterest = rs.getDouble("ADJAAICONTRIINTRST");
			}
			if (rs.getString("ADJPENSIONCONTRIINTRST") != null) {
				adjPesnionContri = rs.getDouble("ADJPENSIONCONTRIINTRST"); 
			}
			if (rs.getString("ADJFINEMPSUBINTRST") != null) {
				adjFSEmpSubIntrst = rs.getDouble("ADJFINEMPSUBINTRST");
			}
			if (rs.getString("ADJFINAAICONTRIINTRST") != null) {
				newadditionalcontri = rs.getDouble("ADJFINAAICONTRIINTRST");
			}
			if (rs.getString("newadditionalcontri") != null) {
				newadditionalcontri = rs.getDouble("newadditionalcontri");
			}
			
			log.info("--transid--" + transid + "---emolumentsTotal----"
					+ emolumentsTotal + "----CPFTotal----" + CPFTotal);
			if (!transid.equals("")) {
				grandTotList.add(transid);
				grandTotList.add(df.format(Math.round(emolumentsTotal)));
				grandTotList.add(df.format(Math.round(CPFTotal)));
				grandTotList.add(df.format(Math.round(PensionTotal)));
				grandTotList.add(df.format(Math.round(PFTotal)));
				grandTotList.add(df.format(Math.round(EmpSubscription)));
				grandTotList.add(df.format(Math.round(AAIContribution)));
				grandTotList.add(df.format(Math.round(adjEmpSubInterest)));
				grandTotList.add(df
						.format(Math.round(adjAAiContriInterest)));
				grandTotList.add(df.format(Math.round(adjPesnionContri)));
				grandTotList.add(df.format(Math.round(adjFSEmpSubIntrst)));
				grandTotList.add(df.format(Math.round(adjFSContriIntrst)));
				grandTotList.add(df.format(Math.round(newadditionalcontri)));
				
			}

		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);

	}
	return grandTotList;
}
public String getAdjCrtnFinalizedFlagforPc(String empserialNO, String reportYear) {
	log.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag Entering method");
	Connection con = null;

	String finalizedFlag = "";

	try {

		con = DBUtils.getConnection();
		finalizedFlag = this.getAdjCrtnFinalizedFlagforPc(con, empserialNO,
				reportYear);

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, null, null);
	}
	log.info("AdjCrtnDAO: getAdjCrtnFinalizedFlag leaving method");
	return finalizedFlag;
}
public FinacialDataBean getEmolumentsBeanForAdjCrtnforPc(Connection con, String fromDate,
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
			query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(ARREARSBREAKUP,'N') as ARREARSBREAKUP,DUEEMOLUMENTS,ARREARAMOUNT  from epis_adj_crtnpc where to_char(monthyear,'dd-Mon-yy') like '%"
					+ transMonthYear
					+ "' and (( cpfaccno='" + cpfaccno + "' and region='" + region + "') or pensionno='" + Pensionno + "')   and empflag='Y' ";
		} else {
			query = "select emoluments,EMPPFSTATUARY,EMPVPF,EMPADVRECPRINCIPAL,EMPADVRECINTEREST,PENSIONCONTRI,nvl(ARREARSBREAKUP,'N') as ARREARSBREAKUP,DUEEMOLUMENTS,ARREARAMOUNT   from epis_adj_crtnpc where to_char(monthyear,'dd-Mon-yy') like '%"
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
public void insertAdjEmolumenstLogInTempforPc(ArrayList adjEmolList,
		String pensionno, String reportYear, String notFianalizetransID) {
	int count = 0, batchId = 0, result = 0;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String chkQuery = "", insertQuery = "", updateQry = "", transSqlQuery = "", ChkFlag = "", transId = "";
	EmolumentslogBean bean = new EmolumentslogBean();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		log.info("AdjCrtnDAO::insertAdjEmolumenstLogInTemp()====Entry");
		log.info("====notFianalizetransID== " + notFianalizetransID);
		// batchId = this.getBatchIdForAdjTemp(con);
		if (notFianalizetransID.equals("")) {
			transId = this.getPCTotalsTransIdforPc(con, pensionno, reportYear);
		} else {
			transId = notFianalizetransID;
		}
		for (int i = 0; i < adjEmolList.size(); i++) {
			bean = (EmolumentslogBean) adjEmolList.get(i);

			chkQuery = "select 'X' as flag from epis_adj_crtn_emol_log_temppc where pensionno="
					+ pensionno
					+ " and monthyear='"
					+ bean.getMonthYear()
					+ "' and transid='"
					+ transId
					+ "' and originalrecord='N'";
			log.info("====chkQuery== " + chkQuery);
			rs = st.executeQuery(chkQuery);
			if (rs.next()) {
				ChkFlag = rs.getString("flag");
			}
			insertQuery = "insert into  epis_adj_crtn_emol_log_temppc "
					+ " (pensionno,  cpfacno ,   monthyear,  oldemoluments , newemoluments ,  oldemppfstatuary,newemppfstatuary,  oldempvpf ,  newempvpf , oldprinciple ,  newprinciple , oldinterest ,  newinterest ,  oldpensioncontri  , newensioncontri , updateddate  , remarks  , region	 , username,  computername,originalrecord,transid) values('"
					+ bean.getPensionNo() + "','" + bean.getCpfAcno()
					+ "','" + bean.getMonthYear() + "','"
					+ bean.getOldEmoluments() + "','"
					+ bean.getNewEmoluments() + "','"
					+ bean.getOldEmppfstatury() + "','"
					+ bean.getNewEmppfstatury() + "','"
					+ bean.getOldEmpvpf() + "','" + bean.getNewEmpvpf()
					+ "','" + bean.getOldPrincipal() + "','"
					+ bean.getNewPrincipal() + "','"
					+ bean.getOldInterest() + "','" + bean.getNewInterest()
					+ "','" + bean.getOldPensioncontri() + "','"
					+ bean.getNewPensioncontri() + "', sysdate,'"
					+ bean.getRemarks() + "','" + bean.getRegion() + "','"
					+ bean.getUserName() + "','" + bean.getComputerName()
					+ "', '" + bean.getOriginalRecord() + "'," + transId
					+ ")";

			updateQry = "update epis_adj_crtn_emol_log_temppc set   oldemoluments='"
					+ bean.getOldEmoluments()
					+ "',newemoluments='"
					+ bean.getNewEmoluments()
					+ "',oldemppfstatuary='"
					+ bean.getOldEmppfstatury()
					+ "',newemppfstatuary='"
					+ bean.getNewEmppfstatury()
					+ "',oldempvpf='"
					+ bean.getOldEmpvpf()
					+ "',newempvpf='"
					+ bean.getNewEmpvpf()
					+ "',oldprinciple='"
					+ bean.getOldPrincipal()
					+ "',newprinciple='"
					+ bean.getNewPrincipal()
					+ "',oldinterest='"
					+ bean.getOldInterest()
					+ "',newinterest='"
					+ bean.getNewInterest()
					+ "',OLDPENSIONCONTRI='"
					+ bean.getOldPensioncontri()
					+ "',NEWENSIONCONTRI='"
					+ bean.getNewPensioncontri()
					+ "',UPDATEDDATE= sysdate"
					+ " ,username='"
					+ bean.getUserName()
					+ "',computername='"
					+ bean.getComputerName()
					+ "' where pensionno="
					+ bean.getPensionNo()
					+ " and   monthyear ='"
					+ bean.getMonthYear()
					+ "' and transid='"
					+ transId
					+ "' and originalrecord='N'";

			log.info("====ChkFlag== " + ChkFlag);
			if (ChkFlag.equals("X")) {
				log.info("====updateQry== " + updateQry);
				result = st.executeUpdate(updateQry);
			} else {
				log.info("====insertQuery== " + insertQuery);
				result = st.executeUpdate(insertQuery);
			}

			log.info("====result==============" + result);
			transSqlQuery = "update epis_adj_crtnpc  set DataModifiedFlag='Y' where  pensionno ="
					+ bean.getPensionNo()
					+ " and empflag='Y' and monthyear='"
					+ bean.getMonthYear() + "'";
			log
					.info("====insertAdjEmolumenstLog========transSqlQuery======"
							+ transSqlQuery);
			st.executeUpdate(transSqlQuery);

		}

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}

}
public FinacialDataBean getPreMonthYearDataforPc(Connection con, String fromDate,String Pensionno) {

	String foundEmpFlag = "";
	Statement st = null;
	ResultSet rs = null;
	FinacialDataBean bean =null;
	try {
		 
		String query = "",query1="";
			query =" select emoluments, EMPPFSTATUARY,EMPVPF, EMPADVRECPRINCIPAL,   EMPADVRECINTEREST,  PENSIONCONTRI,REGION,AIRPORTCODE,MONTHYEAR from epis_adj_crtnpc"
					+ " where to_date(to_char(monthyear, 'dd-Mon-yyyy')) < '"+fromDate+"'   and pensionno = "+Pensionno+"  and empflag = 'Y' and  region is not null and monthyear=(select max(monthyear) from" 
		            +" epis_adj_crtnpc where to_date(to_char(monthyear, 'dd-Mon-yyyy')) < '"+fromDate+"'  and pensionno = "+Pensionno+"    and empflag = 'Y' and  region is not null)";
		
			
			query1 =" select emoluments, EMPPFSTATUARY,EMPVPF, EMPADVRECPRINCIPAL,   EMPADVRECINTEREST,  PENSIONCONTRI,REGION,AIRPORTCODE,MONTHYEAR from epis_adj_crtnpc"
				+ " where to_date(to_char(monthyear, 'dd-Mon-yyyy')) > '"+fromDate+"'   and pensionno = "+Pensionno+"  and empflag = 'Y' and  region is not null and monthyear=(select min(monthyear) from" 
	            +" epis_adj_crtnpc where to_date(to_char(monthyear, 'dd-Mon-yyyy')) > '"+fromDate+"'  and pensionno = "+Pensionno+"    and empflag = 'Y' and  region is not null)";
	
		log.info("AdjCrtnDAO::getPreMonthYearData()----------"+query);
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
			if (rs.getString("REGION") != null) {
				bean.setRegion(rs.getString("REGION"));
			}
			if (rs.getString("AIRPORTCODE") != null) {
				bean.setAirportCode(rs.getString("AIRPORTCODE"));
			}
			 
		}else{ 
			rs.close(); 
			log.info("AdjCrtnDAO::getPreMonthYearData()----------"+query1);
			rs = st.executeQuery(query1);
			 
			if (rs.next()) {				
				bean = new FinacialDataBean();
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
			if (rs.getString("REGION") != null) {
				bean.setRegion(rs.getString("REGION"));
			}
			if (rs.getString("AIRPORTCODE") != null) {
				bean.setAirportCode(rs.getString("AIRPORTCODE"));
			}
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
	 
	return bean;
}
public double  pensionContributionProcess2008to11ForAdjCRTNforPc(String region, String Pfid,String selectedMonth) {
	log.info("AdjCrtnDao :pentionContributionProcess2008to11ForAdjCRTN() entering method");
	String message = "";
	ArrayList xlsDataList = new ArrayList();	 
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	Statement st2 = null;
	EmpMasterBean bean = null;
	String tempInfo[] = null;
	String tempData = "";
	FileWriter fw = null;
	char[] delimiters = { '"', ',', '\'', '*', ',' };
	String quats[] = { "Mrs.", "DR.", "Mr.", "Ms.", "SH.", "smt." };
	String uploadFilePath = "";
	String xlsEmpName = "", monthYear = "", dateofbirth = "", pensionno = "";
	PreparedStatement pst = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	String foundSalaryDate = "", retirementDate = "";
	boolean addflag = false;
	boolean updateflag = false;
	int addBatchcount[] = { 0 };
	double returnPC=0.0;
	try {
		conn = commonDB.getConnection();
		st = conn.createStatement();
		st1 = conn.createStatement();
		st2 = conn.createStatement();
		String airportQuery = "select nvl(airportcode,'X')as airportcode from epis_bulk_print_pfids where pensionno='"
				+ Pfid + "'";
		rs1 = st.executeQuery(airportQuery);
		String airportcode = "";
		if (rs1.next()) {
			airportcode = rs1.getString("airportcode");
		}
		CommonUtil cu = new CommonUtil();
		String todate = cu.getDateTime("dd-MMM-yyyy");
		String checkcontributionUpto = "select pensionno,to_char(monthyear,'dd-Mon-yyyy') as monthyear from epis_adj_crtnpc where pensionno='"
				+ Pfid
				+ "' and monthyear =to_date('"+selectedMonth+"','dd-Mon-yyyy')   and edittrans='N' AND empflag='Y'";
		log.info("---checkcontributionUpto------"+checkcontributionUpto);
		rs = st1.executeQuery(checkcontributionUpto);

		while (rs.next()) {
			double calculatedPension = 0.00, pf = 0.00;
			bean = new EmpMasterBean();

			if (rs.getString("monthyear") != null) {
				monthYear = rs.getString("monthyear").toString();
			} else {
				monthYear = "";
			}
			if (rs.getString("pensionno") != null) {
				pensionno = rs.getString("pensionno").trim();
			} else {
				pensionno = "";
			}

			String transMonthYear = "";

			if (!pensionno.trim().equals("")) {
/*
				String checkPFID = "select wetheroption,pensionno, to_char(add_months(dateofbirth, 696),'dd-Mon-yyyy')AS REIREMENTDATE,to_char(dateofbirth,'dd-Mon-yyyy') as dateofbirth,to_date(to_char(add_months(TO_DATE('"
					+ monthYear
					+ "'), -1),'dd-Mon-yyyy'),'DD-Mon-RRRR')-to_date(add_months(TO_DATE(dateofbirth), 696),'dd-Mon-RRRR')+1 as days,"
					+ "(case when to_char(dateofbirth,'dd-Mon-yyyy') like '01-%' then    to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy') when      to_char(dateofbirth,'dd-Mon-yyyy') not like '01-%' then   to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy')  END ) AS calPensionupto from employee_personal_info where to_char(pensionno)='"
					+ pensionno + "'";*/
				
				String checkPFID 	=" SELECT PENSIONNO,dateofbirth,REIREMENTDATE,calPensionupto, wetheroption,   (CASE   WHEN ((to_date(REIREMENTDATE) > to_date(tomonthyear)) or  (to_date(LASTDAY) = to_date(ACTUALREIREMENTDATE) and"
									 +" (TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) >= 0)) THEN  'F'   else     (case   when ((TO_DATE(REIREMENTDATE) - TO_DATE(monthyear)) <= 0) then  'NIL'  ELSE 'H' end) END)  AS  CALFLAG "
									 +" FROM (select wetheroption, pensionno,to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy') AS ACTUALREIREMENTDATE,"
									 +" to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy') AS REIREMENTDATE, '"+monthYear+"' as monthyear, to_char(to_date(last_day('"+monthYear+"')), 'dd-Mon-yyyy') as tomonthyear,"
									 +" to_char(dateofbirth, 'dd-Mon-yyyy') as dateofbirth,  trunc(trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1,'MM') AS FIRSTDAY,"
									 +" trunc(add_months(TO_DATE(dateofbirth), 697), 'MM') - 1 AS LASTDAY,"
									 +"(case   when to_char(dateofbirth, 'dd-Mon-yyyy') like '01-%' then  to_char(add_months(dateofbirth, 696), 'dd-Mon-yyyy')"
									 +" when to_char(dateofbirth, 'dd-Mon-yyyy') not like '01-%' then  to_char(add_months(dateofbirth, 697), 'dd-Mon-yyyy')  END) AS calPensionupto"
									 +" from employee_personal_info  where to_char(pensionno) ='"+pensionno+"')";
				 log.info("-----checkPFID---"+checkPFID);
				rs1 = st.executeQuery(checkPFID);

				if (!rs1.next()) {
					throw new InvalidDataException("PFID "
							+ bean.getPfid().trim() + " doesn't Exist"
							+ " for  Employee " + bean.getEmpName()
							+ ". PFID is Mandatory for All The Employees");
				}
				if (rs1.getString("wetheroption") != null) {
					if(!rs1.getString("wetheroption").trim().equals("A")){
						bean.setWetherOption("B");
					}else{
					bean.setWetherOption(rs1.getString("wetheroption"));
					}
				} else {
					bean.setWetherOption("B");
				}
				if (rs1.getString("REIREMENTDATE") != null) {
					retirementDate = rs1.getString("REIREMENTDATE");
				} else {
					retirementDate = "";
				}
				if (rs1.getString("dateofbirth") != null) {
					dateofbirth = rs1.getString("dateofbirth");
				} else {
					dateofbirth = "";
				}
				 
				String calPensionupto = "",pcCalFlag="";
				if (rs1.getString("calPensionupto") != null) {
					calPensionupto = rs1.getString("calPensionupto");
				} else {
					calPensionupto = "0";
				}
				if (rs1.getString("CALFLAG") != null) {
					pcCalFlag = rs1.getString("CALFLAG");
				} else {
					pcCalFlag = "NIL";
				}
				transMonthYear = commonUtil.converDBToAppFormat(monthYear
						.trim(), "dd-MMM-yyyy", "-MMM-yyyy");
				String emolumentsQuery = "select emoluments,EMPPFSTATUARY,airportcode , emolumentmonths from epis_adj_crtnpc where pensionno='"
						+ pensionno
						+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
						+ transMonthYear
						+ "' and edittrans='N' and empflag='Y' order by monthyear";
				// log.info(emolumentsQuery);
				rs2 = st.executeQuery(emolumentsQuery);

				// code changed as on 25-Oct-2010
				/*
				 * if (rs2.next()) {
				 * 
				 * if (rs2.getString("emoluments") != null) {
				 * bean.setEmoluments(rs2.getString("emoluments")); } } else
				 * { bean.setEmoluments("0.00"); }
				 */
				while (rs2.next()) {
					if (rs2.getString("EMPPFSTATUARY") != null) {
						bean.setEmployeePF(rs2.getString("EMPPFSTATUARY"));
					} else {
						bean.setEmployeePF("0.00");
					}

					if (rs2.getString("emoluments") != null) {
						bean.setEmoluments(rs2.getString("emoluments"));
					} else {
						bean.setEmoluments("0.00");
					}
					if (rs2.getString("airportcode") != null) {
						bean.setStation(rs2.getString("airportcode"));
					}
					if (rs2.getString("emolumentmonths") != null) {
						bean.setEmolumentMonths(rs2.getString("emolumentmonths"));
					}else{
						bean.setEmolumentMonths("1");
					}
					// log.info("emoluments" + bean.getEmoluments() +
					// "monthYear "+ monthYear);
					Date transdate = null;
					DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
					transdate = df.parse(monthYear);
					float cpfarrear = 0;
					String emolumetns = "0";
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String
									.valueOf(Float.parseFloat(bean
											.getEmoluments()) * 12 / 100));
						} else {
							// bean.setEmployeePF(String.valueOf(Float.
							// parseFloat(bean.getEmoluments()) * 10 /
							// 100));
						}
						emolumetns = bean.getEmoluments();
					} else {
						if (transdate.after(new Date("31-Mar-1998"))
								&& transdate
										.before(new Date("31-Mar-2010"))) {
							bean.setEmployeePF(String.valueOf(Math
									.round(Double.parseDouble(bean
											.getEmoluments()) * 12 / 100)));
							log.info(" pf is : " + bean.getEmployeePF());
						} else {
							// bean.setEmployeePF(String.valueOf(Math.round(
							// Double.parseDouble(bean.getEmoluments())* 10
							// / 100)));
						}
						emolumetns = String.valueOf(Math.round(Double
								.parseDouble(bean.getEmoluments())));
					}
					// log.info(" pf is : " + bean.getEmployeePF());				 
					calculatedPension = this.calclatedPFForAdjCrtn(monthYear,
							calPensionupto, dateofbirth, String
									.valueOf(Double.parseDouble(bean
											.getEmployeePF()) * 100 / 12),
							bean.getWetherOption(), "",  bean.getEmolumentMonths(), bean
									.getEmployeePF(),pcCalFlag);
					if (airportcode.equals("CHQNAD")
							|| airportcode.equals("OFFICE COMPLEX")) {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF()
								.toString())
								- calculatedPension;
						// pf = Math.round(pf);
					} else {
						calculatedPension = Math.round(calculatedPension);
						pf = Double.parseDouble(bean.getEmployeePF())
								- calculatedPension;

					}

					/*String condition = "";
					if ((bean.getStation() != null && bean.getStation() != "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}*/
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update epis_adj_crtnpc set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "								 
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ Math.round(Double.parseDouble(bean.getEmployeePF())) + "'";

					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st2.executeUpdate(insertPensionCon);

				}
				log.info("-----pensionno----------"+pensionno);
				if (!pensionno.equals("")) {
					/*String condition = "";
					if ((bean.getStation() != null || bean.getStation() == "")) {
						condition = "and airportcode='" + bean.getStation()
								+ "'";
					}*/
					String insertPensionCon = "", updatePensionCon = "";
					float basicDaSum = 0;
					transMonthYear = commonUtil.converDBToAppFormat(
							monthYear.trim(), "dd-MMM-yyyy", "-MMM-yyyy");

					insertPensionCon = "update epis_adj_crtnpc set pf='"
							+ pf
							+ "',PENSIONCONTRI='"
							+ calculatedPension
							+ "'  where pensionno='"
							+ pensionno
							+ "' and to_char(monthyear,'dd-Mon-yyyy') like '%"
							+ transMonthYear
							+ "' "								 
							+ " and empflag='Y' and edittrans='N' and EMPPFSTATUARY='"
							+ Math.round(Double.parseDouble(bean.getEmployeePF())) + "'";
					  log.info(insertPensionCon);
					  log.info(updatePensionCon);
					st.executeUpdate(insertPensionCon);

				}

			}
			returnPC =  calculatedPension;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		log.printStackTrace(e);

	} catch (Exception e) {
		// TODO Auto-generated catch block
		log.printStackTrace(e);

	} finally {
		try {
			pst.close();
			rs.close();
			rs2.close();
			st1.close();
			st.close();
			conn.close();
		} catch (Exception e) {

		}
	}
return returnPC;
}
/*
 * 
 * two times
 */
public int saveprvadjcrtntotalsforPc(String empserialNO, String adjOBYear,String form2Status,
		double EmolumentsTot, double cpfTotal, double cpfIntrst,
		double PenContriTotal, double PensionIntrst, double PFTotal,
		double PFIntrst, double EmpSub, double EmpSubInterest,
		double adjEmpSubIntrst, double AAIContri, double AAIContriInterest,
		double adjAAiContriIntrst, double adjPensionContriInterest,
		double FSEmpSubInterest,double FSAAIContriInterest,double totalAdditionalContri) throws Exception {
	int result = 0;
	Connection con = null;
	try {
		con = DBUtils.getConnection();
		result = this.saveprvadjcrtntotalsforPc(con, empserialNO, adjOBYear,form2Status,
				EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
				PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest,
				adjEmpSubIntrst, AAIContri, AAIContriInterest,
				adjAAiContriIntrst, adjPensionContriInterest,
				FSEmpSubInterest,FSAAIContriInterest,totalAdditionalContri);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		throw e;
	} finally {
		commonDB.closeConnection(con, null, null);
	}
	return result;

}
public int saveprvadjcrtntotalsforPc(Connection con, String empserialNO,
		String adjOBYear,String form2Status, double EmolumentsTot, double cpfTotal,
		double cpfIntrst, double PenContriTotal, double PensionIntrst,
		double PFTotal, double PFIntrst, double EmpSub,
		double EmpSubInterest, double adjEmpSubIntrst, double AAIContri,
		double AAIContriInterest, double adjAAiContriIntrst,
		double adjPensionContriInterest,double FSEmpSubInterest,double FSAAIContriInterest,double totalAdditionalContri)
		throws InvalidDataException {
	String insertQuery = "", transID = "", chkStatus = "", chkPrevPcTots = "" ,chkChqApproverFlag = "false",remarks="",chkChqApproverStatus="";

	Statement st = null;
	ResultSet rs = null;
	int result = 0;
	try {

		st = con.createStatement();
		/*
		 * deleteQuery = "delete from epis_adj_crtn_prv_pc_totals where
		 * pensionno="+empSerialNo;
		 * log.info("----savepcadjcrtntotals---deleteQuery "+deleteQuery);
		 * st.executeUpdate(deleteQuery);
		 */
		chkPrevPcTots = chkAdjCrtnPrvPCTotalsforPc(con,empserialNO, adjOBYear,chkChqApproverFlag);
		chkStatus = this.getAdjCrtnApprovedStatusforPc(con,empserialNO, adjOBYear);

		log.info("saveprvadjcrtntotals()--chkFlag---" + chkStatus);
		if ((chkStatus.equals(""))
				|| (chkStatus.equals("Initial,Approved")) || (chkStatus.equals("BLOCKED,Released"))) {
			transID = this.getTransidSequence(con);
		}
		if (chkStatus.equals("Initial,Approved")) {
			 remarks="After Approved";
			 chkChqApproverFlag="true";
		}
		insertQuery = "insert into epis_adj_crtn_prv_pc_totalspc(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB, EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST , ADJAAICONTRIINTRST ,ADJFINEMPSUBINTRST,ADJFINAAICONTRIINTRST,CREATIONDATE, REMARKS,newadditionalcontri) values('"
				+ empserialNO
				+ "','"
				+ transID
				+ "','"
				+ adjOBYear
				+ "','"
				+ EmolumentsTot
				+ "','"
				+ cpfTotal
				+ "','"
				+ cpfIntrst
				+ "','"
				+ PenContriTotal
				+ "','"
				+ PensionIntrst
				+ "','"
				+ adjPensionContriInterest
				+ "','"
				+ PFTotal
				+ "','"
				+ PFIntrst
				+ "','"
				+ EmpSub
				+ "','"
				+ EmpSubInterest
				+ "','"
				+ adjEmpSubIntrst
				+ "','"
				+ AAIContri
				+ "','"
				+ AAIContriInterest
				+ "','"
				+ adjAAiContriIntrst
				+ "','"
				+ FSEmpSubInterest
				+ "','"
				+ FSAAIContriInterest
				+ "', sysdate,'"+remarks+"','"+totalAdditionalContri+"')";

		if (chkPrevPcTots.equals("NotExists")) {
			log
					.info("----savepcadjcrtntotals---insertQuery "
							+ insertQuery);
			result = st.executeUpdate(insertQuery);
		} else {
			if(form2Status.equals("Y")){
				chkChqApproverStatus = chkAdjCrtnPrvPCTotalsforPc(con,empserialNO, adjOBYear,chkChqApproverFlag);
				if (chkChqApproverStatus.equals("NotExists") &&  chkStatus.equals("Initial,Approved")) { 
					log
							.info("----savepcadjcrtntotals---insertQuery After Approved"
									+ insertQuery);
					result = st.executeUpdate(insertQuery);
				}
				} 
		}
		/*
		 * // saving in transactions table insertIntoTrans = "insert into
		 * EPIS_ADJ_CRTN_TRANSACTIONS(PENSIONNO,ADJOBYEAR
		 * ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST
		 * ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB,
		 * EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST ,
		 * ADJAAICONTRIINTRST
		 * ,CREATIONDATE,APPROVEDBY,DESIGNATION,APRVDSIGNNAME) values('" +
		 * empserialNO+ "','"+ transID + "','" + adjOBYear + "','" +
		 * EmolumentsTot + "','" + cpfTotal + "','" + cpfIntrst + "','" +
		 * PenContriTotal + "','"+ PensionIntrst + "','" +
		 * adjPensionContriInterest + "','" + PFTotal + "','" + PFIntrst +
		 * "','" + EmpSub + "','" + EmpSubInterest + "','" + adjEmpSubIntrst +
		 * "','" + AAIContri + "','" + AAIContriInterest + "','" +
		 * adjAAiContriIntrst+ "', sysdate)";
		 * 
		 */

	} catch (SQLException e) {
		throw new InvalidDataException(e.getMessage());
	} catch (Exception e) {
		throw new InvalidDataException(e.getMessage());
	} finally {
		commonDB.closeConnection(null, st, rs);

	}
	return result;
}
public String getAdjCrtnApprovedStatusforPc(Connection con ,String empserialNO, String reportYear) {
	log.info("AdjCrtnDAO: getAdjCrtnApprovedStatus Entering method");		 
	Statement st = null;
	ResultSet rs = null;
	String selectSQL = "", APPROVEDTYPE = "";
	int i = 0;
	selectSQL = "select APPROVEDTYPE as APPROVEDTYPE  from epis_adj_crtn_transactionspc where pensionno= "
			+ empserialNO
			+ " and adjobyear= '"
			+ reportYear
			+ "'  order by transid";

	try {
		log.info("selectSQL==" + selectSQL);		 
		st = con.createStatement();
		rs = st.executeQuery(selectSQL);
		while (rs.next()) {
			if (rs.getString("APPROVEDTYPE") != null) {
				i++;
				if (i == 1) {
					APPROVEDTYPE = rs.getString("APPROVEDTYPE");
				} else {
					APPROVEDTYPE = APPROVEDTYPE + ","
							+ rs.getString("APPROVEDTYPE");
				}
			}

		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	log.info("AdjCrtnDAO: getAdjCrtnApprovedStatus leaving method");
	return APPROVEDTYPE;
}
public String getDeleteAllRecords(String pfid,String reportYear,String frmName, String username,
		String ipaddress, String flag,String chqFlag,String empSubTot,String aaiContriTot,String pensionTot) {
	String message = "";

	String deleteEpisAdjCrtn = "", deleteEpisAdjCrtnPcTotalsDiff = "", deleteEpisAdjCrtnCurntPcTotals = "", deleteEpisAdjCrtnEmolumentsLog = "", deleteEpisAdjCrtnPrvPcTotals = "", deleteEpisAdjCrtnTracking = "", deleteEpisAdjCrtnEmolLogTemp = "";
	String updateEpisAdjCrtnLog = "", insertEpisAdjCrtnLogDtl = "", deleteEpisAdjCrtnTemp = "", deleteEpisAdjCrtnTransactions = "",fromYear="",toYear="";
	String episAdjCrtnLog ="", episAdjCrtnLogDtl="",chqRemarks="";
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	String years[] =null;
	years =reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	String chkUploadPfid = this.chkPfidinAdjCrtnTrackingForUpload(pfid,
			"adjcorrections");
	
	try {
		conn = DBUtils.getConnection();
		st = conn.createStatement();

		deleteEpisAdjCrtn = "delete from epis_adj_crtn where pensionno='"
				+ pfid + "'  and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnPcTotalsDiff = "delete from epis_adj_crtn_pc_totals_diff where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnCurntPcTotals = "delete from epis_adj_crtn_curnt_pc_totals where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnEmolumentsLog = "delete from  epis_adj_crtn_emoluments_log where pensionno='"
				+ pfid + "' and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnPrvPcTotals = "delete from epis_adj_crtn_prv_pc_totals where pensionno='"
				+ pfid + "'  and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnTracking = "delete from epis_adj_crtn_pfid_tracking where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnEmolLogTemp = "delete from epis_adj_crtn_emol_log_temp where pensionno='"
				+ pfid + "' and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnTemp = "delete  from  EPIS_ADJ_CRTN_TEMP where PENSIONNO='"
				+ pfid + "'"; 
		deleteEpisAdjCrtnTransactions = "delete  from  epis_adj_crtn_transactions where PENSIONNO='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		log.info("deleteEpisAdjCrtn" + deleteEpisAdjCrtn);
		st.addBatch(deleteEpisAdjCrtn);

		log.info("deleteEpisAdjCrtnPcTotalsDiff"
				+ deleteEpisAdjCrtnPcTotalsDiff);
		st.addBatch(deleteEpisAdjCrtnPcTotalsDiff);

		log.info("deleteEpisAdjCrtnCurntPcTotals"
				+ deleteEpisAdjCrtnCurntPcTotals);
		st.addBatch(deleteEpisAdjCrtnCurntPcTotals);

		log.info("deleteEpisAdjCrtnEmolumentsLog"
				+ deleteEpisAdjCrtnEmolumentsLog);
		st.addBatch(deleteEpisAdjCrtnEmolumentsLog);

		log.info("deleteEpisAdjCrtnPrvPcTotals"
				+ deleteEpisAdjCrtnPrvPcTotals);
		st.addBatch(deleteEpisAdjCrtnPrvPcTotals);

		log.info("deleteEpisAdjCrtnTransactions"
				+ deleteEpisAdjCrtnTransactions);
		st.addBatch(deleteEpisAdjCrtnTransactions);
		 
		 
		if (!chkUploadPfid.equals("Exists")) {

			log.info("deleteEpisAdjCrtnTracking"
					+ deleteEpisAdjCrtnTracking);
			st.addBatch(deleteEpisAdjCrtnTracking);
		}

		log.info("deleteEpisAdjCrtnEmolLogTemp"
				+ deleteEpisAdjCrtnEmolLogTemp);
		st.addBatch(deleteEpisAdjCrtnEmolLogTemp);
		if (!flag.equals("U") || !chkUploadPfid.equals("Exists") || reportYear.equals("")) {
			log.info("deleteEpisAdjCrtnTemp" + deleteEpisAdjCrtnTemp);
			st.addBatch(deleteEpisAdjCrtnTemp);
		}
		 
		// Checking for record with report is there r not 
		String loggeridseq = "select loggerid from epis_adj_crtn_log where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'  and deletedflag='N'"; 
		st1 = conn.createStatement();
		log.info("loggeridseq " + loggeridseq);
		rs = st1.executeQuery(loggeridseq);
		if(chqFlag.equals("true")){
			chqRemarks="CHQ-"+empSubTot+"-"+aaiContriTot+"-"+pensionTot; 
		}
		if (rs.next()) {				
			updateEpisAdjCrtnLog = "update epis_adj_crtn_log set deletedflag='Y',remarks=remarks ||'"+chqRemarks+"'  where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'"; 
			log.info("updateEpisAdjCrtnLog " + updateEpisAdjCrtnLog);
			st.addBatch(updateEpisAdjCrtnLog);
			
			int logid = Integer.parseInt(rs.getString("loggerid"));

			insertEpisAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt,remarks) values ("
					+ logid
					+ ",'"
					+ username
					+ "','"
					+ ipaddress
					+ "',sysdate,'Record Deleted')";
			log.info("insertEpisAdjCrtnLogDtl " + insertEpisAdjCrtnLogDtl);
			st.addBatch(insertEpisAdjCrtnLogDtl);
		}else{
		episAdjCrtnLog = "insert into epis_adj_crtn_log(loggerid,pensionno,adjobyear,creationdt,deletedflag,remarks) values (loggerid_seq.nextval,"
				+ pfid + ",'"+reportYear+"',sysdate,'Y','"+chqRemarks+"')";
		log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
		st.addBatch(episAdjCrtnLog);
		episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtl(loggerid,username,ipaddress,workingdt,remarks) values (loggerid_seq.currval,'"
			+ username + "','" + ipaddress + "',sysdate,'Record Deleted')";
		log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
		st.addBatch(episAdjCrtnLogDtl);				
		}
		 
		int count[] = st.executeBatch();
		log.info("count" + count.length);

		message = "Succesfully Deleted";
		//			 For ReLoading the Deleted Data freshly
		if(message.equals("Succesfully Deleted")){
		insertEmployeeTransDataYearWise(conn,pfid,reportYear, frmName, username,ipaddress);
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		commonDB.closeConnection(conn, st1, rs);
		DBUtils.close(conn);
	}
	return message;
}
public String getDeleteAllRecordsforPc(String pfid,String reportYear,String frmName, String username,
		String ipaddress, String flag,String chqFlag,String empSubTot,String aaiContriTot,String pensionTot) {
	String message = "";

	String deleteEpisAdjCrtn = "", deleteEpisAdjCrtnPcTotalsDiff = "", deleteEpisAdjCrtnCurntPcTotals = "", deleteEpisAdjCrtnEmolumentsLog = "", deleteEpisAdjCrtnPrvPcTotals = "", deleteEpisAdjCrtnTracking = "", deleteEpisAdjCrtnEmolLogTemp = "";
	String updateEpisAdjCrtnLog = "", insertEpisAdjCrtnLogDtl = "",deleteform4="", deleteEpisAdjCrtnTemp = "", deleteEpisAdjCrtnTransactions = "",fromYear="",toYear="";
	String episAdjCrtnLog ="", episAdjCrtnLogDtl="",chqRemarks="";
	Connection conn = null;
	Statement st = null;
	Statement st1 = null;
	ResultSet rs = null;
	String years[] =null;
	years =reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else{
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	}
	String chkUploadPfid = this.chkPfidinAdjCrtnTrackingForUpload(pfid,
			"adjcorrections");
	
	try {
		conn = DBUtils.getConnection();
		st = conn.createStatement();

		deleteEpisAdjCrtn = "delete from epis_adj_crtnpc where pensionno='"
				+ pfid + "'  and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnPcTotalsDiff = "delete from epis_adj_crtn_pc_totals_diffpc where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnCurntPcTotals = "delete from epis_adj_crtn_curnt_pc_topc where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnEmolumentsLog = "delete from  epis_adj_crtn_emoluments_logpc where pensionno='"
				+ pfid + "' and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnPrvPcTotals = "delete from epis_adj_crtn_prv_pc_totalspc where pensionno='"
				+ pfid + "'  and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnTracking = "delete from epis_adj_crtn_pfid_trackingpc where pensionno='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteEpisAdjCrtnEmolLogTemp = "delete from epis_adj_crtn_emol_log_temppc where pensionno='"
				+ pfid + "' and monthyear between '"+fromYear+"' and '"+toYear+"'";
		deleteEpisAdjCrtnTemp = "delete  from  EPIS_ADJ_CRTN_TEMPPC where PENSIONNO='"
				+ pfid + "'"; 
		deleteEpisAdjCrtnTransactions = "delete  from  epis_adj_crtn_transactionspc where PENSIONNO='"
				+ pfid + "' and adjobyear='"+reportYear+"'";
		deleteform4 = "delete  from  epis_form4pc where PENSIONNO='"
			+ pfid + "' and ADJYEAR='"+reportYear+"'";
		log.info("deleteEpisAdjCrtn" + deleteEpisAdjCrtn);
		st.addBatch(deleteEpisAdjCrtn);
		log.info("deleteform4" + deleteform4);
		st.addBatch(deleteform4);
		log.info("deleteEpisAdjCrtnPcTotalsDiff"
				+ deleteEpisAdjCrtnPcTotalsDiff);
		st.addBatch(deleteEpisAdjCrtnPcTotalsDiff);

		log.info("deleteEpisAdjCrtnCurntPcTotals"
				+ deleteEpisAdjCrtnCurntPcTotals);
		st.addBatch(deleteEpisAdjCrtnCurntPcTotals);

		log.info("deleteEpisAdjCrtnEmolumentsLog"
				+ deleteEpisAdjCrtnEmolumentsLog);
		st.addBatch(deleteEpisAdjCrtnEmolumentsLog);

		log.info("deleteEpisAdjCrtnPrvPcTotals"
				+ deleteEpisAdjCrtnPrvPcTotals);
		st.addBatch(deleteEpisAdjCrtnPrvPcTotals);

		log.info("deleteEpisAdjCrtnTransactions"
				+ deleteEpisAdjCrtnTransactions);
		st.addBatch(deleteEpisAdjCrtnTransactions);
		 
		 
		if (!chkUploadPfid.equals("Exists")) {

			log.info("deleteEpisAdjCrtnTracking"
					+ deleteEpisAdjCrtnTracking);
			st.addBatch(deleteEpisAdjCrtnTracking);
		}

		log.info("deleteEpisAdjCrtnEmolLogTemp"
				+ deleteEpisAdjCrtnEmolLogTemp);
		st.addBatch(deleteEpisAdjCrtnEmolLogTemp);
		if (!flag.equals("U") || !chkUploadPfid.equals("Exists") || reportYear.equals("")) {
			log.info("deleteEpisAdjCrtnTemp" + deleteEpisAdjCrtnTemp);
			st.addBatch(deleteEpisAdjCrtnTemp);
		}
		 
		// Checking for record with report is there r not 
		String loggeridseq = "select loggerid from epis_adj_crtn_logpc where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'  and deletedflag='N'"; 
		st1 = conn.createStatement();
		log.info("loggeridseq " + loggeridseq);
		rs = st1.executeQuery(loggeridseq);
		if(chqFlag.equals("true")){
			chqRemarks="CHQ-"+empSubTot+"-"+aaiContriTot+"-"+pensionTot; 
		}
		if (rs.next()) {				
			updateEpisAdjCrtnLog = "update epis_adj_crtn_logpc set deletedflag='Y',remarks=remarks ||'"+chqRemarks+"'  where pensionno="
				+ pfid+" and adjobyear='"+reportYear+"'"; 
			log.info("updateEpisAdjCrtnLog " + updateEpisAdjCrtnLog);
			st.addBatch(updateEpisAdjCrtnLog);
			
			int logid = Integer.parseInt(rs.getString("loggerid"));

			insertEpisAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtlpc(loggerid,username,ipaddress,workingdt,remarks) values ("
					+ logid
					+ ",'"
					+ username
					+ "','"
					+ ipaddress
					+ "',sysdate,'Record Deleted')";
			log.info("insertEpisAdjCrtnLogDtl " + insertEpisAdjCrtnLogDtl);
			st.addBatch(insertEpisAdjCrtnLogDtl);
		}else{
		episAdjCrtnLog = "insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt,deletedflag,remarks) values (loggerid_seq.nextval,"
				+ pfid + ",'"+reportYear+"',sysdate,'Y','"+chqRemarks+"')";
		log.info("episAdjCrtnLog" + episAdjCrtnLog);					 
		st.addBatch(episAdjCrtnLog);
		episAdjCrtnLogDtl = "insert into epis_adj_crtn_log_dtlpc(loggerid,username,ipaddress,workingdt,remarks) values (loggerid_seq.currval,'"
			+ username + "','" + ipaddress + "',sysdate,'Record Deleted')";
		log.info("episAdjCrtnLogDtl " + episAdjCrtnLogDtl);
		st.addBatch(episAdjCrtnLogDtl);				
		}
		 
		int count[] = st.executeBatch();
		log.info("count" + count.length);

		message = "Succesfully Deleted";
		//			 For ReLoading the Deleted Data freshly
		if(message.equals("Succesfully Deleted")){
		insertEmployeeTransDataYearWiseforPc(conn,pfid,reportYear, frmName, username,ipaddress);
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		commonDB.closeConnection(conn, st1, rs);
		DBUtils.close(conn);
	}
	return message;
}
public int savepcadjcrtnCurrenttotalsforPc(String empserialNO, String adjOBYear,
		String transid, String form2Status, double EmolumentsTot, double cpfTotal,
		double cpfIntrst, double PenContriTotal, double PensionIntrst,
		double PFTotal, double PFIntrst, double EmpSub,
		double EmpSubInterest, double adjEmpIntrst, double AAIContri,
		double AAIContriInterest, double adjAAiContrIntrst,
		double adjPensionContrIntrst, double emolumentsTot_diff,
		double cpfTot_diff, double PenscontriTot_diff, double PfTot_diff,
		double empSubTot_diff, double adjEmpIntrst_diff,
		double AAiContriTot_diff, double adjAAiContrIntrst_diff,
		double adjPensionContrIntrst_diff, double pensionIntrstfrm2,
		double empSubIntrstfrm2, double aaiContriIntrstfrm2,double deffAdditionalContri,double totalAdditionalContri) {
	String epis_adj_crtn_curnt_pc_totals_log = "", epis_adj_crtn_pc_totals_diff_log = "",remarksCond="",remarks="";
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	int result = 0;
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		/*
		 * deleteQuery = "delete from epis_adj_crtn_pc_totals where
		 * pensionno="+empSerialNo;
		 * log.info("----savepcadjcrtntotals---deleteQuery "+deleteQuery);
		 * st.executeUpdate(deleteQuery);
		 */
		log.info("==adjAAiContrIntrst,=====" + adjAAiContrIntrst+"==form2Status==="+form2Status);
		if(form2Status.equals("Y")){
			remarksCond=" and remarks='After Approved'";
			remarks="After Approved";
		}
		String selectADJCurrentPCTotals = "select count(*) as count from epis_adj_crtn_curnt_pc_topc where pensionno='"
				+ empserialNO + "' and  ADJOBYEAR='" + adjOBYear + "'" +remarksCond;
		log
				.info("--savepcadjcrtnCurrenttotals----selectADJCurrentPCTotals--"
						+ selectADJCurrentPCTotals);
		rs = st.executeQuery(selectADJCurrentPCTotals);
		while (rs.next()) {
			int count = rs.getInt(1);
			if (count == 0) {
				epis_adj_crtn_curnt_pc_totals_log = "insert into epis_adj_crtn_curnt_pc_topc(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL,CPFINTEREST , PENSIONTOTAL, PENSIONINTEREST ,ADJPENSIONCONTRIINTRST, PFTOTAL,PFINTEREST ,EMPSUB, EMPSUBINTEREST,ADJEMPSUBINTRST,AAICONTRI , AAICONTRIINTEREST ,ADJAAICONTRIINTRST,CREATIONDATE,REMARKS,newadditionalcontri) values('"
						+ empserialNO
						+ "','"
						+ transid
						+ "','"
						+ adjOBYear
						+ "','"
						+ EmolumentsTot
						+ "','"
						+ cpfTotal
						+ "','"
						+ cpfIntrst
						+ "','"
						+ PenContriTotal
						+ "','"
						+ PensionIntrst
						+ "','"
						+ adjPensionContrIntrst
						+ "','"
						+ PFTotal
						+ "','"
						+ PFIntrst
						+ "','"
						+ EmpSub
						+ "','"
						+ EmpSubInterest
						+ "','"
						+ adjEmpIntrst
						+ "','"
						+ AAIContri
						+ "','"
						+ AAIContriInterest
						+ "','"
						+ adjAAiContrIntrst
						+ "', sysdate,'"+remarks+"','"+totalAdditionalContri+"')";
			} else {

				epis_adj_crtn_curnt_pc_totals_log = "update epis_adj_crtn_curnt_pc_topc  set transid='"
						+ transid
						+ "',EMOLUMENTS='"
						+ EmolumentsTot
						+ "',CPFTOTAL='"
						+ cpfTotal
						+ "',CPFINTEREST='"
						+ cpfIntrst
						+ "',PENSIONTOTAL='"
						+ PenContriTotal
						+ "',PENSIONINTEREST='"
						+ PensionIntrst
						+ "',ADJPENSIONCONTRIINTRST='"
						+ adjPensionContrIntrst
						+ "',PFTOTAL='"
						+ PFTotal
						+ "',PFINTEREST='"
						+ PFIntrst
						+ "',EMPSUB='"
						+ EmpSub
						+ "',EMPSUBINTEREST='"
						+ EmpSubInterest
						+ "',ADJEMPSUBINTRST='"
						+ adjEmpIntrst
						+ "',AAICONTRI='"
						+ AAIContri
						+ "',AAICONTRIINTEREST='"
						+ AAIContriInterest
						+ "',newadditionalcontri='"
						+ totalAdditionalContri
						+ "',ADJAAICONTRIINTRST='"
						+ adjAAiContrIntrst
						+ "', CREATIONDATE= sysdate	"
						+ "  where  pensionno='"
						+ empserialNO
						+ "' and   ADJOBYEAR='" + adjOBYear + "'";
				
				
			}

		}
		String selectADJPCTotalsDiff = "select count(*) as count from epis_adj_crtn_pc_totals_Diffpc where pensionno='"
				+ empserialNO + "' and  ADJOBYEAR='" + adjOBYear + "'" +remarksCond;
		log.info("--savepcadjcrtnCurrenttotals----selectADJPCTotalsDiff--"
				+ selectADJPCTotalsDiff);
		rs = st.executeQuery(selectADJPCTotalsDiff);
		while (rs.next()) {
			int count = rs.getInt(1);
			if (count == 0) {
				epis_adj_crtn_pc_totals_diff_log = "insert into epis_adj_crtn_pc_totals_Diffpc(PENSIONNO,TRANSID, ADJOBYEAR ,EMOLUMENTS,CPFTOTAL, PENSIONTOTAL,PENSIONINTRST, PFTOTAL,EMPSUB,EMPSUBINTRST,AAICONTRI ,AAICONTRIINTRST,ADJEMPSUBINTRST  , ADJAAICONTRIINTRST,ADJPENSIONCONTRIINTRST,CREATIONDATE,REMARKS,deffadditionalcontri) values('"
						+ empserialNO
						+ "','"
						+ transid
						+ "','"
						+ adjOBYear
						+ "','"
						+ emolumentsTot_diff
						+ "','"
						+ cpfTot_diff
						+ "','"
						+ PenscontriTot_diff
						+ "','"
						+ pensionIntrstfrm2
						+ "','"
						+ PfTot_diff
						+ "','"
						+ empSubTot_diff
						+ "','"
						+ empSubIntrstfrm2
						+ "','"
						+ AAiContriTot_diff
						+ "','"
						+ aaiContriIntrstfrm2
						+ "','"
						+ adjEmpIntrst_diff
						+ "','"
						+ adjAAiContrIntrst_diff
						+ "','"
						+ adjPensionContrIntrst_diff + "',  sysdate,'"+remarks+"','"+deffAdditionalContri+"')";
			} else {

				epis_adj_crtn_pc_totals_diff_log = "update epis_adj_crtn_pc_totals_Diffpc  set transid='"
						+ transid
						+ "',EMOLUMENTS='"
						+ emolumentsTot_diff
						+ "',CPFTOTAL='"
						+ cpfTot_diff
						+ "',PENSIONTOTAL='"
						+ PenscontriTot_diff
						+ "',PENSIONINTRST='"
						+ pensionIntrstfrm2
						+ "',PFTOTAL='"
						+ PfTot_diff
						+ "',EMPSUB='"
						+ empSubTot_diff
						+ "',EMPSUBINTRST='"
						+ empSubIntrstfrm2
						+ "',AAICONTRI='"
						+ AAiContriTot_diff
						+ "',AAICONTRIINTRST='"
						+ aaiContriIntrstfrm2
						+ "',ADJEMPSUBINTRST='"
						+ adjEmpIntrst_diff
						+ "',deffadditionalcontri='"
						+ deffAdditionalContri
						+ "',ADJAAICONTRIINTRST='"
						+ adjAAiContrIntrst_diff
						+ "',ADJPENSIONCONTRIINTRST ='"
						+ adjPensionContrIntrst_diff
						+ "', CREATIONDATE= sysdate	"
						+ "  where  pensionno='"
						+ empserialNO
						+ "' and   ADJOBYEAR='" + adjOBYear + "'";

			}

		}
		log.info("----savepcadjcrtnCurrenttotals---Current PC Totals  "
				+ epis_adj_crtn_curnt_pc_totals_log);
		st.executeUpdate(epis_adj_crtn_curnt_pc_totals_log);
		log.info("----savepcadjcrtnCurrenttotals--- PC Totals Diff"
				+ epis_adj_crtn_pc_totals_diff_log);
		st.executeUpdate(epis_adj_crtn_pc_totals_diff_log);

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);

	}
	return result;
}

public int updateStageWiseStatusInAdjCtrnforPc(String empserialNO,
		String processedStage,String reportYear,String form2Status,String jvno, String userName, String loginUserId,
		String userRegion, String loginUsrStation, String loginUsrDesgn) {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "", insertQry = "", updateQry = "", trackingId = "", signPath = "", adjObYear = "",remarks="",updatetrackingid="";
	String chkStatus="", chkChqApproverFlag="false";
	ArrayList adjObYearList = new ArrayList();
	EmployeePensionCardInfo empPensionInfo = new EmployeePensionCardInfo();
	int result = 0, transId = 0;
	double   empSub=0, empSubIntrst=0,aaiContri=0,aaiContriIntrst=0,pcPrinciple=0,pcIntrst=0	;
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		if (processedStage.equals("Approved")) {
			//System.out.println("processedStage=="+processedStage);
			updateQry = " update epis_adj_crtn_pfid_trackingpc set  verifiedby=verifiedby||','||'"
					+ processedStage
					+ "' ,apporvedby='"
					+ userName
					+ "' ,jvno='"+jvno+"',rem_month=(select to_char(rem_month,'dd-Mon-yyyy') rem_month from rem_month where remflag='N') where pensionno =" + empserialNO+" and  adjobyear ='" + reportYear+"' ";

		} else {
			//System.out.println("processedStage==1111111111=="+processedStage);
			updateQry = " update epis_adj_crtn_pfid_trackingpc set  verifiedby='"
					+ processedStage + "' where pensionno =" + empserialNO+" and  adjobyear ='" + reportYear+"'";
		}

		//chkStatus = this.getAdjCrtnApprovedStatus(con,empserialNO, reportYear); 
		//log.info("updateStageWiseStatusInAdjCtrn()--chkStatus---" + chkStatus);
		 
		if (processedStage.equals("CHQApproved")){
			remarks="After Approved"; 
		}else{
			log
			.info("AdjCrtnDAO::updateStageWiseStatusInAdjCtrn()==updateQry=="
					+ updateQry);
		result = st.executeUpdate(updateQry);
		} 
	 
		// Adding Data in transaction table
		if(form2Status.equals("Y")){
			chkChqApproverFlag="true";
		}
		 
		adjObYearList = (ArrayList) this.getFinalizedAdjObYearforPc(con,
				empserialNO,reportYear,chkChqApproverFlag);

		if (!processedStage.equals("Reject")) {
			if (loginUserId.equals("30")) {
				signPath = Constants.ADJCRTN_SIGNATURES_SEHGAL;

			} else if (loginUserId.equals("79")) {
				signPath = Constants.ADJCRTN_SIGNATURES_NIMESH;

			} else if (loginUserId.equals("43")) {
				signPath = Constants.ADJCRTN_SIGNATURES_SHIKHA;

			} else if (loginUserId.equals("240")) {
				signPath = Constants.ADJCRTN_SIGNATURES_MONIKA;
			} else {
				signPath = "";
			}
			log.info(" ==loginUserId==" + insertQry);
			for (int i = 0; i < adjObYearList.size(); i++) {
				empPensionInfo = (EmployeePensionCardInfo) adjObYearList
						.get(i);
				sqlQuery = "select trackingid as trackingid from epis_adj_crtn_pfid_trackingpc where pensionno="
						+ empserialNO
						+ " and adjobyear='"
						+ empPensionInfo.getReportYear() + "'";
				log.info(" ==sqlQuery==" + sqlQuery);
				rs = st.executeQuery(sqlQuery);
				if (rs.next()) {
					if (rs.getString("trackingid") != null) {
						trackingId = rs.getString("trackingid");
					}
				}
				log.info(" ==trackingId==" + trackingId);
				transId = this.getTransIdForAdjTransactionsforPc(con);

				log.info(" ==adjObYear==" + empPensionInfo.getReportYear());
				
				 empSub= Double.parseDouble(empPensionInfo.getEmpSub())- Double.parseDouble(empPensionInfo.getAdjEmpSubInterest());
				 empSubIntrst = Double.parseDouble(empPensionInfo.getEmpSubInterest()) + Double.parseDouble(empPensionInfo.getAdjEmpSubInterest());
				 aaiContri= Double.parseDouble(empPensionInfo.getAaiContri()) - Double.parseDouble(empPensionInfo.getAdjAaiContriInterest());
				 aaiContriIntrst= Double.parseDouble(empPensionInfo.getAaiContriInterest()) + Double.parseDouble(empPensionInfo.getAdjAaiContriInterest());
				 pcPrinciple = Double.parseDouble(empPensionInfo.getPensionTotal()) - Double.parseDouble(empPensionInfo.getAdjPensionInterest());
				 pcIntrst = Double.parseDouble(empPensionInfo.getPensionInterest()) + Double.parseDouble(empPensionInfo.getAdjPensionInterest());
				
				 updatetrackingid =  this.chkPfidinAdjCrtnTransforPc(con,empserialNO,reportYear,processedStage); 
					 
				 
				insertQry = "insert into epis_adj_crtn_transactionspc (PENSIONNO ,ADJOBYEAR, EMPSUBSCRIPTION,EMPSUBINT , AAICONTRI ,  AAICONTRIINT , PCPRINCIPAL ,PCINTEREST ,TRACKINGID,TRANSID,APPROVEDTYPE, DESIGNATION ,  APRVDSIGNNAME , APPROVEDBY ,   APPROVEDDATE ,APPSTATION,APPREGION,SIGNPATH,REMARKS )values( "
						+ empserialNO
						+ ",'"
						+ empPensionInfo.getReportYear()
						+ "','"
						+ empSub
						+ "','"
						+ empSubIntrst
						+ "','"
						+ aaiContri
						+ "','"
						+ aaiContriIntrst
						+ "','"
						+ pcPrinciple
						+ "','"
						+ pcIntrst
						+ "','"
						+ trackingId
						+ "','"
						+ transId
						+ "','"
						+ processedStage
						+ "','"
						+ loginUsrDesgn
						+ "','"
						+ userName
						+ "','"
						+ loginUserId
						+ "',sysdate,'"
						+ loginUsrStation
						+ "','"
						+ userRegion
						+ "','"
						+ signPath + "','"+remarks+"')";
				
					 
			
			if(!updatetrackingid.equals("")){
				if(processedStage.equals("Initial")){
				remarks="Record Rejected Before";
				
				updateQry=" update   epis_adj_crtn_transactionspc  set   EMPSUBSCRIPTION ='"+empSub+"',EMPSUBINT ='"+empSubIntrst+"' , AAICONTRI='"+aaiContri+"'  ,  AAICONTRIINT ='"+aaiContriIntrst+"',"
				+ " PCPRINCIPAL ='"+pcPrinciple+"',PCINTEREST = '"+pcIntrst+"',APPROVEDDATE=sysdate,APRVDSIGNNAME='"+userName+"'  , APPROVEDBY='"+loginUserId+"'" 
			    +" , APPSTATION='"+loginUsrStation+"',APPREGION='"+userRegion+"',SIGNPATH='"+signPath+"',DESIGNATION='"+loginUsrDesgn+"',REMARKS='"+remarks+"' where pensionno ="+empserialNO+" and  TRACKINGID='"+updatetrackingid+"'"; 
		
				log.info(" ==updateQry==" + updateQry);
				st.executeUpdate(updateQry);
				}
			}else{
				log.info(" ==insertQry==" + insertQry);
				st.executeUpdate(insertQry);
			}
		 }
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return result;
}
public String chkPfidinAdjCrtnTransforPc(Connection con, String empserialNO,String reportYear,String processedStage){	
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "",trackingid=""; 
	 
	try {
		st = con.createStatement();
		sqlQuery = "select trackingid  from epis_adj_crtn_transactionspc where pensionno='"+empserialNO+"' and adjobyear='"+reportYear+"' and  approvedtype='"+processedStage+"'";
		log.info("chkPfidinAdjCrtnTrans()=="+sqlQuery);
		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			if( rs.getString("trackingid")!=null){
			trackingid = rs.getString("trackingid");
			}
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return trackingid;
}
public ArrayList getFinalizedAdjObYearforPc(Connection con, String pfid, String reportYear,String chkChqApproverFlag) {
	String sqlQuery = "", adjobyear = "", remarksCond="";
	ResultSet rs = null;
	Statement st = null;
	ArrayList empPensionInfo = new ArrayList();
	EmployeePensionCardInfo empBean = null;
	if(chkChqApproverFlag.equals("true")){
		remarksCond=" and remarks='After Approved'";
	}
	sqlQuery = "select ADJOBYEAR,nvl(EMPSUB,0) AS EMPSUB,nvl(EMPSUBINTRST,0) AS EMPSUBINTRST , nvl(ADJEMPSUBINTRST,0) AS ADJEMPSUBINTRST  , nvl(AAICONTRI,0) AS AAICONTRI ,  nvl(AAICONTRIINTRST,0) AS AAICONTRIINTRST , nvl(ADJAAICONTRIINTRST,0) AS ADJAAICONTRIINTRST, nvl(PENSIONTOTAL,0) AS PENSIONTOTAL ,nvl(PENSIONINTRST,0) as PENSIONINTRST,nvl(ADJPENSIONCONTRIINTRST,0) as ADJPENSIONCONTRIINTRST   from epis_adj_crtn_pc_totals_diffpc where  pensionno ="
			+ pfid+" and adjobyear='"+reportYear+"'" +remarksCond;;
	log.info("AdjCrtnDAO::getFinalizedAdjObYear()---" + sqlQuery);
	try {

		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			empBean = new EmployeePensionCardInfo();
			if (rs.getString("ADJOBYEAR") != null) {
				empBean.setReportYear(rs.getString("ADJOBYEAR"));
			}
			if (rs.getString("EMPSUB") != null) {
				empBean.setEmpSub(rs.getString("EMPSUB"));
			} else {
				empBean.setEmpSub("0");
			}
			if (rs.getString("EMPSUBINTRST") != null) {
				empBean.setEmpSubInterest(rs.getString("EMPSUBINTRST"));
			} else {
				empBean.setEmpSubInterest("0");
			}
			if(rs.getString("ADJEMPSUBINTRST")!=null){
				empBean.setAdjEmpSubInterest(rs.getString("ADJEMPSUBINTRST"));			 
			}else{
				empBean.setAdjEmpSubInterest("0");
			}
			if (rs.getString("AAICONTRI") != null) {
				empBean.setAaiContri(rs.getString("AAICONTRI"));
			} else {
				empBean.setAaiContri("0");
			}
			if (rs.getString("AAICONTRIINTRST") != null) {
				empBean.setAaiContriInterest(rs
						.getString("AAICONTRIINTRST"));
			} else {
				empBean.setAaiContriInterest("0");
			}
			if(rs.getString("ADJAAICONTRIINTRST")!=null){
				empBean.setAdjAaiContriInterest(rs.getString("ADJAAICONTRIINTRST"));			 
			}else{
				empBean.setAdjAaiContriInterest("0");
			}
			if (rs.getString("PENSIONTOTAL") != null) {
				empBean.setPensionTotal(rs.getString("PENSIONTOTAL"));
			} else {
				empBean.setPensionTotal("0");
			}
			if (rs.getString("PENSIONINTRST") != null) {
				empBean.setPensionInterest(rs.getString("PENSIONINTRST"));
			} else {
				empBean.setPensionInterest("0");
			}
			if(rs.getString("ADJPENSIONCONTRIINTRST")!=null){
				empBean.setAdjPensionInterest(rs.getString("ADJPENSIONCONTRIINTRST"));			 
			}else{
				empBean.setAdjPensionInterest("0");
			}
			empPensionInfo.add(empBean);
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
	return empPensionInfo;
}
public int getTransIdForAdjTransactionsforPc(Connection con) {
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "";
	int transId = 0;
	try {
		st = con.createStatement();
		sqlQuery = "select crtn_trans_idpc.nextval as crtn_trans_idpc from dual";
		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			transId = rs.getInt("crtn_trans_idpc");
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return transId;
}
public String chkAdjCrtnPrvPCTotalsforPc(Connection con, String empserialNO, String adjOBYear,String chkChqApproverFlag) {
	String selQuery = "", result = "", remarksCond="";		 
	Statement st = null;
	ResultSet rs = null;
	try {
		if(chkChqApproverFlag.equals("true")){
			remarksCond="and remarks='After Approved'";
		}else{
			remarksCond="";
		}
		st = con.createStatement();
		selQuery = "select 'X' as flag from epis_adj_crtn_prv_pc_totalspc where pensionno="
				+ empserialNO + " and ADJOBYEAR='" + adjOBYear + "'" +remarksCond;

		log.info("AdjCrtnDAO::chkAdjCrtnPrvPCTotals()---selQuery "
				+ selQuery);
		rs = st.executeQuery(selQuery);
		if (rs.next()) {
			result = rs.getString("flag");
		}

		if (result.equals("X")) {
			result = "Exists";
		} else {
			result = "NotExists";
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);

	}
	return result;
}
public int insertEmployeeTransDataYearWiseforPc(Connection con,String pfId,String reportYear, String frmName,String username,String ipaddress) {
	log.info("AdjCrtnDAO :insertEmployeeTransDataYearWise() Entering Method ");
	boolean dataavailbf2008=false;
	EmpMasterBean bean = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	ResultSet rs4 = null;
	ResultSet rs5 = null;
	Statement st = null;
	Statement st1 = null;
	String episAdjCrtnLog="",episAdjCrtnLogDtl="",sqlForMapping1995to2008="",sqlFor1995to2008="",fromYear="",toYear="";
	String chkpfid = "",commdata="",revisedFlagQry ="", loansQuery = "", advancesQuery = "",sql="",addcontrisql="", updateloans = "", updateadvances = "", loandate = "", subamt = "", contamt = "", advtransdate = "", advanceAmt = "", monthYear = "",tableName ="";
	String cpfregionstrip="",condition="",preparedString="",preparedStringCond="",dojcpfaccno="",dojemployeeno="",dojempname="",dojstation="",dojregion="",chkForRecord="",insertDummyRecord="";
	String[] cpfregiontrip=null;
	String[] cpfaccnos=null;
	String[] regions=null;
	String[] commondatalst=null;
	String years[] =null;
	int result = 0, loansresult = 0, advancesresult = 0 ,transID=0,batchId=0,insertDummyRecordResult=0; 
	years =reportYear.split("-");
	if(reportYear.equals("2012-2013")){
		fromYear = "01-Apr-"+years[0];
		toYear = "30-Apr-"+years[1];
	}
	else if(Integer.parseInt(reportYear.substring(0, 4))>=2013){
	fromYear = "01-May-"+years[0];
	toYear = "30-Apr-"+years[1];
	}
	else {
	fromYear = "01-Apr-"+years[0];
	toYear = "31-Mar-"+years[1]; 
	} 
	EmpMasterBean empBean = new EmpMasterBean();
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		st1 = con.createStatement();
		this.chkDOJ(con,pfId);
		cpfregionstrip =this.getEmployeeCpfacno(con,pfId);
		String[] pfIDLists = cpfregionstrip.split("=");
		preparedString = commonDAO.preparedCPFString(pfIDLists);
		log.info("preparedString===================="+preparedString);
		if (Integer.parseInt(years[0]) == 1995) {
			preparedStringCond = "or "+preparedString;
		} 
		if(frmName.equals("adjcorrections")){
			tableName ="EPIS_ADJ_CRTNPC";
		}else if(frmName.equals("form7/8psadjcrtn")){
			tableName ="EPIS_ADJ_CRTN_FORM78PS";
		}
		//chkpfid = "select * from "+tableName+" where pensionno=" + pfId;
		//rs = st.executeQuery(chkpfid);
		chkpfid = this.chkPfidinAdjCrtnYearWiseforPc(pfId,fromYear,toYear,frmName);
		episAdjCrtnLog="insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt) values (loggerid_seq.nextval,"+ pfId +",'"+reportYear+"',sysdate)";	
		if (chkpfid.equals("NotExists")) {
			/*if(dataavailbf2008==false){*/
				sql = "insert into "+tableName+" (PENSIONNO ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO,DESEGNATION ,AIRPORTCODE ,REGION ,MONTHYEAR ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,  EMPADVRECINTEREST , "
				+ " Advance,   PFWSUB ,   PFWCONTRI , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,FORM7NARRATION , PCHELDAMT ,EMOLUMENTMONTHS,PCCALCAPPLIED ,ARREARFLAG ,LATESTMNTHFLAG ,ARREARAMOUNT  , DEPUTATIONFLAG,DUEEMOLUMENTS ,MERGEFLAG,"
				+ " ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS ,REVISEEPFEMOLUMENTSFLAG ,FINYEAR ,ACC_KEYNO , USERNAME ,IPADDRESS , LASTACTIVEDATE , REMARKS,additionalcontri)   (select  "+pfId+" ,CPFACCNO ,EMPLOYEENAME, EMPLOYEENO ,"
				+ "  DESEGNATION ,AIRPORTCODE  ,REGION , MONTHYEAR  ,EMOLUMENTS  ,EMPPFSTATUARY , EMPVPF , EMPADVRECPRINCIPAL ,EMPADVRECINTEREST ,  0.00,   0.00 ,   0.00 , PF,  PENSIONCONTRI , EMPFLAG  ,  EDITTRANS ,  FORM7NARRATION ,  PCHELDAMT ,EMOLUMENTMONTHS, PCCALCAPPLIED ,"
				+ " ARREARFLAG , LATESTMNTHFLAG ,  ARREARAMOUNT  ,   DEPUTATIONFLAG,  DUEEMOLUMENTS ,   MERGEFLAG,  ARREARSBREAKUP, OPCHANGEPF , OPCHANGEPENSIONCONTRI ,CALCEMOLUMENTS ,SUPPLIFLAG , REVISEEPFEMOLUMENTS , REVISEEPFEMOLUMENTSFLAG  ,"
				+ " FINYEAR ,ACC_KEYNO ,USERNAME  , IPADDRESS , '' , REMARKS,additionalcontri   from employee_pension_validate where empflag='Y' and (pensionno="
				+ pfId +" "+preparedStringCond+") and monthyear between '" + fromYear + "' and '" + toYear + "')";
		 
				 
				
				episAdjCrtnLogDtl="insert into epis_adj_crtn_log_dtlpc(loggerid,username,ipaddress,workingdt) values (loggerid_seq.currval,'"+username+"','"+ipaddress+"',sysdate)";
				revisedFlagQry =" update "+tableName+"    set  reviseepfemolumentsflag='N' where  pensionno="+pfId+" and empflag='Y' and  monthyear between '" + fromYear + "' and '" + toYear + "'";
				log.info("episAdjCrtnLog"+ episAdjCrtnLog);
				st.addBatch(episAdjCrtnLog);
				log.info("episAdjCrtnLogDtl "+episAdjCrtnLogDtl);
				st.addBatch(episAdjCrtnLogDtl);
				log.info("insertEmployeeTransDataYearWise()----sql---" + sql);		
				st.addBatch(sql);
				log.info("insertEmployeeTransDataYearWise()---revisedFlagQry--" + revisedFlagQry);		
				st.addBatch(revisedFlagQry);
				int insertCount[]=st.executeBatch();
				log.info("insertCount  "+insertCount.length);
				st=null;
				st = con.createStatement();
				loansQuery = " select to_char(ln.loandate,'MON-yyyy') as loandate,ln.sub_amt as subamt,ln.cont_amt as contamt from employee_pension_loans ln where pensionno = "
					+ pfId+" and loandate between '" + fromYear + "' and '" + toYear + "'";
				log.info("insertEmployeeTransDataYearWise()---loansQuery--" + loansQuery);				 
				rs1 = st.executeQuery(loansQuery);
				if (Integer.parseInt(years[0])>=2015) {

					st = null;
					st = con.createStatement();
					addcontrisql = " update epis_adj_crtnpc c set c.additionalcontri= (select sum(additionalcontri) from EMPLOYEE_PENSION_VALIDATE v where pensionno= "
							+ pfId+" and  monthyear between '01-Oct-2014' and '01-May-2015') where pensionno="+ pfId+" and monthyear ='01-May-2015'";
					log.info("----------addcontrisql-------------" + addcontrisql);
					rs5 = st.executeQuery(addcontrisql);
					}
			while (rs1.next()) {
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
				updateloans = "update  "+tableName+"   set  pfwsub="
						+ subamt + ", pfwcontri=" + contamt
						+ " where pensionno=" + pfId
						+ " and  to_char(monthyear,'MON-yyyy')='"
						+ loandate + "'";
			 
				chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and  to_char(monthyear,'MON-yyyy')='"+ loandate + "'";
				rs4 = st.executeQuery(chkForRecord);
				if(rs4.next()){
					loansresult = st.executeUpdate(updateloans);
				}else{
					insertDummyRecord= " insert into epis_adj_crtnpc (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
									+" (select pensionno, TRUNC(to_date('"+loandate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtnpc where pensionno ="+pfId+" and monthyear between  '"+fromYear+"' and '" + toYear + "'"
									+" and rowid =(select max(rowid) from epis_adj_crtnpc where pensionno ="+pfId+" and monthyear between '"+fromYear+"' and '" + toYear + "'))";       
					 insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
					 log.info("Dummy Recprd Inserted--For Loans----insertDummyRecord----"+ insertDummyRecord);
					 loansresult = st.executeUpdate(updateloans);
				}
				log.info("insertEmployeeTransDataYearWise()---updateloans---"+ updateloans);
			}
			advancesQuery = " select to_char(adv.advtransdate,'MON-yyyy') as advtransdate ,adv.amount as advanceAmt from employee_pension_advances  adv  where pensionno = "
					+ pfId+"  and advtransdate between '" + fromYear + "' and '" + toYear + "'";
			log
					.info("-insertEmployeeTransDataYearWise()-----advancesQuery-------"
							+ advancesQuery);
			st = con.createStatement();
			rs2 = st.executeQuery(advancesQuery);
			while (rs2.next()) {
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
				updateadvances = "update  "+tableName+"   set    advance ="
						+ advanceAmt + " where pensionno=" + pfId
						+ " and  to_char(monthyear,'MON-yyyy')='"
						+ advtransdate + "'";
				 
				
				chkForRecord = "select 'X' as flag from "+tableName+" where pensionno ="+pfId+" and   to_char(monthyear,'MON-yyyy')='"+ advtransdate + "'";
				rs4 = st.executeQuery(chkForRecord);
				log.info("chkForRecord-----"+ chkForRecord);
				if(rs4.next()){
					advancesresult = st.executeUpdate(updateadvances);
					 
				}else{
					insertDummyRecord= " insert into epis_adj_crtnpc (pensionno,monthyear,employeename,employeeno,desegnation,airportcode,region,emoluments,emppfstatuary,empvpf,empadvrecprincipal,empadvrecinterest,advance,pfwsub,pfwcontri,pf,pensioncontri,finyear,remarks)"
									+" (select pensionno, TRUNC(to_date('"+advtransdate+"','mm-yyyy'),'MM') ,employeename,employeeno,desegnation,airportcode,region,0,0,0,0,0,0,0,0,0,0,finyear,'Dummy Record' from epis_adj_crtnpc where pensionno ="+pfId+" and monthyear between  '"+fromYear+"' and '" + toYear + "'"
									+" and rowid =(select max(rowid) from epis_adj_crtnpc where pensionno ="+pfId+" and monthyear between '"+fromYear+"' and '" + toYear + "'))";       
					insertDummyRecordResult = st.executeUpdate(insertDummyRecord);
					log.info("Dummy Recprd Inserted--For Advances----insertDummyRecord----"+ insertDummyRecord);
					advancesresult = st.executeUpdate(updateadvances);
				}
				log.info("insertEmployeeTransDataYearWise()------updateadvances----"+ updateadvances);
			}
			 
		} else {
			String loggeridseq="select loggerid from epis_adj_crtn_logpc where pensionno="+pfId+" and adjobyear='"+reportYear+"'";
			int	logid=0;
			log.info("loggeridseq "+loggeridseq);
			rs3=st.executeQuery(loggeridseq);
			if(rs3.next()){
				logid=Integer.parseInt(rs3.getString("loggerid"));
				log.info("logid  test"+logid);
			}else{
				st=null;
				st=con.createStatement();
				episAdjCrtnLog="insert into epis_adj_crtn_logpc(loggerid,pensionno,adjobyear,creationdt,remarks) values (loggerid_seq.nextval,"+ pfId +",'"+reportYear+"',sysdate,'This pfid already ported before implmenation logic')";
				st.executeUpdate(episAdjCrtnLog);
				st=null;
				st=con.createStatement();
				rs3=st.executeQuery(loggeridseq);
				if(rs3.next())
				logid=Integer.parseInt(rs3.getString("loggerid"));
				st=null;
			}
			log.info("--------Data already exists----------");
		
	}
	 
		
	} catch (Exception e) {
		e.printStackTrace();
		log.printStackTrace(e);
		log.info("error" + e.getMessage());
	}finally{ 
		try {
			if(rs1!=null){
				rs1.close();
			}
			if(rs2!=null){
				rs2.close();
			}
			if(rs3!=null){
				rs3.close();
			}
			if(rs4!=null){
				rs4.close();
			} 
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commonDB.closeConnection(null,st,rs);
	}
	log.info("AdjCrtnDAO :insertEmployeeTransDataYearWise() Leaving Method ");
	return result;
}
public ArrayList searchAdjctrnforPc(String userRegion, String userStation,
		String profileType, String accessCode, String accountType,
		String employeeNo,String  adjOBYear,String approvedStatus) {
	int count = 0;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String selectQuery = "", verifiedBy = "",approvedStage="",form2StatusDesc="";
	double pcprincipal=0.00,pcinterest=0.00,pensionTot=0.00;
	ArrayList al = new ArrayList();
	EmpMasterBean empBean = null;
	DecimalFormat df = new DecimalFormat("#########0");
	selectQuery = this.buildSearchQueryForAdjCrtnforPc(userRegion, userStation,
			profileType, accessCode, accountType, employeeNo,adjOBYear,approvedStatus);
	log.info("searchAdjctrn()==========" + selectQuery);
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
			if (rs.getString("ADJOBYEAR") != null) {
				empBean.setReportYear(rs.getString("ADJOBYEAR")) ;
			} else{
				empBean.setReportYear("");
			}
			if (rs.getString("PCPRINCIPAL") != null) {
				pcprincipal = rs.getDouble("PCPRINCIPAL") ;
			}  
			if (rs.getString("PCINTEREST") != null) {
				pcinterest =  rs.getDouble("PCINTEREST")  ;
			} 
			
			if(empBean.getReportYear().equals("1995-2008")){
				pensionTot = pcprincipal + pcinterest;
			}else{
				pensionTot = pcprincipal ;
			}
			empBean.setPensionTot(String.valueOf(df.format(pensionTot)));
			
			if (rs.getString("EMPSUBTOT") != null) {
				empBean.setEmpSubTot(rs.getString("EMPSUBTOT")) ;
			} else{
				empBean.setEmpSubTot("");
			}
			if (rs.getString("AAICONTRITOT") != null) {
				empBean.setAaiContriTot(rs.getString("AAICONTRITOT")) ;
			} else{
				empBean.setAaiContriTot("");
			}
			if (rs.getString("APPROVEDTYPE") != null) {
				approvedStage = rs.getString("APPROVEDTYPE");
				if(approvedStage.equals("Initial,Approved")){
					approvedStage="Approved";
					empBean.setApprovedStatus(approvedStage);  
				}else{
					empBean.setApprovedStatus(approvedStage); 
				} 
			} else{
				empBean.setApprovedStatus("");
			}
			if (rs.getString("APPROVERNAME") != null) {
				empBean.setApproverName(rs.getString("APPROVERNAME")) ;
			} else{
				empBean.setApproverName("");
			}
			if (rs.getString("FORM2STATUS") != null) {
				empBean.setForm2Status(rs.getString("FORM2STATUS")) ;
			} else{
				empBean.setForm2Status("N");
			}
			
			if(empBean.getForm2Status().equals("Y")){
				form2StatusDesc ="Submitted";
			}else if(empBean.getForm2Status().equals("B")){
				form2StatusDesc ="Proccessing";
			}else if(empBean.getForm2Status().equals("M")){
				form2StatusDesc ="N/A";
			}else{
				form2StatusDesc ="Approved,But Form 4 not generated";
			}
			empBean.setForm2StatusDesc(form2StatusDesc);
			
			if (rs.getString("FROZEN") != null) {
				empBean.setFrozen(rs.getString("FROZEN")) ;
			} else{
				empBean.setFrozen("N");
			}
			if (rs.getString("JVNO") != null) {
				empBean.setJvNo(rs.getString("JVNO")) ;
			} else{
				empBean.setJvNo("");
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
public String buildSearchQueryForAdjCrtnforPc(String userRegion,
		String userStation, String profileType, String accessCode,
		String accountType, String employeeNo,String adjOBYear,String approvedStatus) {
	StringBuffer whereClause = new StringBuffer();
	StringBuffer query = new StringBuffer();
	String dynamicQuery = "", orderBy = "", sqlQuery = "", verifiedby = "", profilecondition = "";

	log.info("==userRegion==" + userRegion + "==userStation===="
			+ userStation + "==profileType==" + profileType
			+ "==accessCode==" + accessCode + "=employeeNo==" + employeeNo);

	 
	if(accessCode.equals("PE040202")){
		verifiedby="'Initial','Reject'";
		profilecondition="AND TRACKING.VERIFIEDBY in   ("+verifiedby+")";
	} 
 	if(accessCode.equals("PE040204")){
		verifiedby="'Initial,Approved'";
		profilecondition="AND TRACKING.VERIFIEDBY  in ("+verifiedby+")  AND TRANS.Approvedtype ='Approved'";
	} else if(accessCode.equals("PE04020602")){
		verifiedby="'Initial,Approved'";
		profilecondition="AND TRACKING.VERIFIEDBY  in ("+verifiedby+")  AND TRANS.Approvedtype ='CHQApproved'";
	} 
 	 
 	if(accessCode.equals("PE040202") || accessCode.equals("PE040204") || accessCode.equals("PE04020602")){
	 	sqlQuery ="  SELECT EPI.PENSIONNO AS PENSIONNO,EPI.EMPLOYEENAME AS EMPLOYEENAME,EPI.CPFACNO AS CPFACNO ,EPI.REGION AS REGION,EPI.AIRPORTCODE AS AIRPORTCODE,EPI.DESEGNATION AS DESEGNATION,EPI.DATEOFBIRTH AS DATEOFBIRTH,EPI.DATEOFJOINING AS DATEOFJOINING, TRANS.ADJOBYEAR AS ADJOBYEAR, NVL(TRANS.PCPRINCIPAL, 0.0) AS PCPRINCIPAL,  NVL(TRANS.PCINTEREST, 0.0)AS PCINTEREST,  (NVL(TRANS.EMPSUBSCRIPTION, 0.0) + NVL(TRANS.EMPSUBINT, 0.0)) AS EMPSUBTOT,"
			+ " (NVL(AAICONTRI, 0.0) + NVL(TRANS.AAICONTRIINT, 0.0)) AS AAICONTRITOT,TRACKING.VERIFIEDBY AS APPROVEDTYPE, TRACKING.FORM2ID AS FORM2ID,TRACKING.FORM2STATUS AS FORM2STATUS ,TRANS.APRVDSIGNNAME	AS APPROVERNAME ,TRACKING.FROZEN AS FROZEN,TRACKING.JVNO AS JVNO  FROM EPIS_ADJ_CRTN_TRANSACTIONSPC TRANS, EPIS_ADJ_CRTN_PFID_TRACKINGPC  TRACKING,EMPLOYEE_PERSONAL_INFO EPI"
			+" WHERE EPI.EMPFLAG ='Y'  AND TRANS.PENSIONNO = TRACKING.PENSIONNO AND TRACKING.PENSIONNO = EPI.PENSIONNO    AND TRANS.ADJOBYEAR = TRACKING.ADJOBYEAR "+profilecondition;
	 	}else if(accessCode.equals("PE04020601")){
	 		sqlQuery ="  SELECT   PENSIONNO,  EMPLOYEENAME, CPFACNO, REGION, AIRPORTCODE, DESEGNATION,  DATEOFBIRTH, DATEOFJOINING,  ADJOBYEAR, NVL(PCPRINCIPAL, 0.0) AS PCPRINCIPAL ,NVL(PCINTEREST, 0.0) AS PCINTEREST ,NVL(EMPSUB, 0.0) AS EMPSUBTOT,"
				+ " NVL(AAICONTRI, 0.0) AS AAICONTRITOT,   APPROVEDTYPE,   FORM2STATUS , APPROVERNAME , FROZEN,JVNO FROM v_epis_adj_chq_search  ";
		 	
	 	}
 	
	if (!(profileType.equals("C") || profileType.equals("S") || profileType
			.equals("A"))) {

		if (profileType.equals("R")) {
			if (!userRegion.equals("CHQIAD")) {
				whereClause
						.append(" LOWER(EPI.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
								+ userRegion.toLowerCase().trim()+ "')");
				whereClause.append(" AND ");
			} else {
				 //For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 07-Jun-2012
				if (!userStation.equals("")) {
					whereClause
					.append(" LOWER(EPI.AIRPORTCODE)  IN (SELECT LOWER(UNITNAME)   FROM EMPLOYEE_UNIT_MASTER EUM     WHERE LOWER(EUM.REGION) ='"
							+ userRegion.toLowerCase().trim()+ "' AND ACCOUNTTYPE='RAU')");
				 
					whereClause.append(" AND ");
				}
			}
		} else {
			if (!userStation.equals("")) {
				whereClause.append(" LOWER(EPI.AIRPORTCODE) like'%"
						+ userStation.toLowerCase().trim() + "%'");
				whereClause.append(" AND ");
			}
		}

		if (!userRegion.equals("")) {
			whereClause.append(" LOWER(EPI.REGION) like'%"
					+ userRegion.toLowerCase().trim() + "%'");
			whereClause.append(" AND ");
		}
		if (!employeeNo.equals("")) {
			whereClause.append(" EPI.PENSIONNO =" + employeeNo);
			whereClause.append(" AND ");
		}
		if (!adjOBYear.equals("")) {
			whereClause.append(" TRANS.ADJOBYEAR ='"+ adjOBYear+"'");        
			whereClause.append(" AND ");      
		}
		if (!approvedStatus.equals("")) {
			whereClause.append(" TRANS.APPROVEDTYPE ='"+ approvedStatus+"'");         
			whereClause.append(" AND ");      
		} 
		query.append(sqlQuery);

		if (userStation.equals("") && userRegion.equals("")
				&& employeeNo.equals("") && adjOBYear.equals("") && approvedStatus.equals("")) {
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}
	} else {
		if(accessCode.equals("PE04020601")){
			if (!employeeNo.equals("")) {
				whereClause.append(" PENSIONNO =" + employeeNo);
				whereClause.append(" AND ");
			}
			if (!adjOBYear.equals("")) {
				whereClause.append(" ADJOBYEAR ='"+ adjOBYear+"'");         
				whereClause.append(" AND ");      
			}
		 
			query.append(sqlQuery);
			if (employeeNo.equals("") && adjOBYear.equals("")) {
			} else {
				query.append(" WHERE ");
				query.append(this.sTokenFormat(whereClause));
			}
		}else{
		if (!employeeNo.equals("")) {
			whereClause.append(" EPI.PENSIONNO =" + employeeNo);
			whereClause.append(" AND ");
		}
		if (!adjOBYear.equals("")) {
			whereClause.append(" TRANS.ADJOBYEAR ='"+ adjOBYear+"'");         
			whereClause.append(" AND ");      
		}
		if (!approvedStatus.equals("")) {
			whereClause.append(" TRANS.APPROVEDTYPE ='"+ approvedStatus+"'");           
			whereClause.append(" AND ");      
		} 
		query.append(sqlQuery);
		if (employeeNo.equals("") && adjOBYear.equals("") && approvedStatus.equals("")) {
		} else {
			query.append(" AND ");
			query.append(this.sTokenFormat(whereClause));
		}
	}
		
	}
	if(accessCode.equals("PE04020601")){
	orderBy = "ORDER BY PENSIONNO";
	}else if(accessCode.equals("PE040204")){
		orderBy = "ORDER BY TRANS.APPROVEDDATE DESC ";
	}else{
		orderBy = "ORDER BY TRACKING.PENSIONNO";
	}
	query.append(orderBy);
	dynamicQuery = query.toString();

	return dynamicQuery;

}
public ArrayList pfCardAdditionalContriCalculationForAdj(String pensionNo){
	
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	String qury="",additionalContri="" ,monthyear="";
	double total=0.0;
	ArrayList al=new ArrayList();
	try {
		con = commonDB.getConnection();
		stmt = con.createStatement();
		qury = "SELECT TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' || SUBSTR(empdt.MONYEAR, 4, 4)) AS EMPMNTHYEAR, decode((sign(sysdate - add_months(to_date(TO_DATE('01-' || SUBSTR(empdt.MONYEAR, 0, 3) || '-' || SUBSTR(empdt.MONYEAR, 4, 4))), 1))),-1, 0, 1, 1) as signs, emp.* " +
				" from (select distinct to_char(to_date('01-Sep-2014', 'dd-mon-yyyy') + rownum - 1, 'MONYYYY') monyear from employee_pension_validate where rownum <= to_date('01-Apr-2015', 'dd-mon-yyyy') - to_date('01-Sep-2014', 'dd-mon-yyyy') + 1) empdt, (SELECT add_months(MONTHYEAR, -1) as MONTHYEAR," +
				" to_char(add_months(MONTHYEAR, -1), 'MONYYYY') empmonyear, additionalcontri FROM epis_adj_crtn  WHERE empflag = 'Y' and (to_date(to_char(monthyear, 'dd-Mon-yyyy')) >= add_months('01-Sep-2014', 1) and to_date(to_char(monthyear, 'dd-Mon-yyyy')) <= add_months(last_day('01-Apr-2015'), 1))" +
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
//added by mohan for pc

public String chkStageWiseprocessinAdjCalcforPc(String pensionno) {
	ArrayList stationList = new ArrayList();		 
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	String  verifiedby="";
	int i=0;
	try {
		con = DBUtils.getConnection();;
		st = con.createStatement();  
		  
		String query = "select DISTINCT  APPROVEDTYPE from EPIS_ADJ_CRTN_TRANSACTIONSPC where pensionno='"+pensionno+"'"; 
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
public ArrayList getAdjEmolumentsLogforPc(String pfId, String adjOBYear,
		String frmName) {
	log.info("AdjCrtnDAO:getEmolumentslog-- Entering Method");

	String sqlQuery = "", prefixWhereClause = "";
	EmolumentslogBean data = new EmolumentslogBean();
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	ArrayList emolumentsloginfo = null;

	sqlQuery = " select data.*,to_char(data.updateddate,'HH:MI:SS AM') as batchtime ,prv.ADJOBYEAR as ADJOBYEAR from (select * from epis_adj_crtn_emoluments_logpc where pensionno = "
			+ pfId
			+ "  and originalrecord='N') data,"
			+ "   (SELECT  batchid,updateddate ,monthyear   from epis_adj_crtn_emoluments_logpc   where pensionno =  "
			+ pfId
			+ " and  originalrecord = 'N'  group by batchid,updateddate,monthyear) d , (select  adjobyear ,transid  from epis_adj_crtn_prv_pc_totalspc"
			+" where pensionno = "+pfId+") prv  where  data.transid = prv.transid and data.batchid = d.batchid   and data.monthyear=d.monthyear order by d.batchid,d.monthyear ";

	log.info("sql query " + sqlQuery);
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery.toString());
		emolumentsloginfo = new ArrayList();
		while (rs.next()) {
			/*
			 * log.info("OLDEMOLUMENTS" + rs.getString("OLDEMOLUMENTS"));
			 * log.info("NEWEMOLUMENTS" + rs.getString("NEWEMOLUMENTS"));
			 * log.info("PENSIONNO" + rs.getString("PENSIONNO"));
			 * log.info("MONTHYEAR" + commonUtil.converDBToAppFormat(rs
			 * .getDate("UPDATEDDATE")));
			 */
			data = new EmolumentslogBean();

			if (rs.getString("PENSIONNO") != null) {
				data.setPensionNo(rs.getString("PENSIONNO"));
			} else {
				data.setPensionNo("");
			}
			if (rs.getString("MONTHYEAR") != null) {
				data.setMonthYear(CommonUtil.converDBToAppFormat(rs
						.getDate("MONTHYEAR")));
			} else {
				data.setMonthYear("");
			}
			if (rs.getString("OLDEMOLUMENTS") != null) {
				data.setOldEmoluments(rs.getString("OLDEMOLUMENTS"));
			} else {
				data.setOldEmoluments("");
			}
			if (rs.getString("NEWEMOLUMENTS") != null) {
				data.setNewEmoluments(rs.getString("NEWEMOLUMENTS"));
			} else {
				data.setNewEmoluments("");
			}

			if (rs.getString("OLDEMPPFSTATUARY") != null) {
				data.setOldEmppfstatury(rs.getString("OLDEMPPFSTATUARY"));
			} else {
				data.setOldEmppfstatury("");
			}

			if (rs.getString("NEWEMPPFSTATUARY") != null) {
				data.setNewEmppfstatury(rs.getString("NEWEMPPFSTATUARY"));
			} else {
				data.setNewEmppfstatury("");
			}
			if (rs.getString("OLDEMPVPF") != null) {
				data.setOldEmpvpf(rs.getString("OLDEMPVPF"));
			} else {
				data.setOldEmpvpf("");
			}

			if (rs.getString("NEWEMPVPF") != null) {
				data.setNewEmpvpf(rs.getString("NEWEMPVPF"));
			} else {
				data.setNewEmpvpf("");
			}

			if (rs.getString("UPDATEDDATE") != null) {
				data.setUpdatedDate(CommonUtil.converDBToAppFormat(rs
						.getDate("UPDATEDDATE")));
			} else {
				data.setUpdatedDate("");

			}
			if (rs.getString("batchtime") != null) {
				data.setBatchTime(rs.getString("batchtime"));
			} else {
				data.setBatchTime("");
			}
			if (rs.getString("region") != null) {
				data.setRegion(rs.getString("region"));
			} else {
				data.setRegion("");
			}
			if (rs.getString("BATCHID") != null) {
				data.setBatchId(rs.getString("BATCHID"));
			} else {
				data.setBatchId("");
			}
			if (rs.getString("USERNAME") != null) {
				data.setUserName(rs.getString("USERNAME"));
			} else {
				data.setUserName("");
			}
			if (rs.getString("ADJOBYEAR") != null) {
				data.setAdjObYear(rs.getString("ADJOBYEAR"));
			} else {
				data.setAdjObYear("");
			}
			emolumentsloginfo.add(data);
		}

		log.info("emolumentsloginfo list size " + emolumentsloginfo.size());

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return emolumentsloginfo;
}
public String  chkPfidinAdjCrtnYearWiseforPc(String pfid,String fromYear, String toYear,String frmName){
	String sqlQuery="",chkpfid="",tableName="";		 
	ResultSet rs = null;
	Statement st = null;
	Connection con = null;
	if(frmName.equals("adjcorrections")){
		tableName ="EPIS_ADJ_CRTNPC";
	}else if(frmName.equals("form7/8psadjcrtn")){
		tableName ="EPIS_ADJ_CRTN_FORM78PSPC";
	}
	sqlQuery = "select * from "+tableName+" where   pensionno= "+ pfid+" and monthyear between '"+fromYear+"' and  '"+toYear+"'";
	log.info("--chkPfidinAdjCrtnYearWise()---"+sqlQuery);
	try {
		 
		con = DBUtils.getConnection(); 
		st = con.createStatement();
		rs  = st.executeQuery(sqlQuery);
		if(rs.next()){ 
			chkpfid="Exists"; 
		}else{
			chkpfid="NotExists";
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
	 
	return chkpfid;
}
public ArrayList getAdjCrtnFinalizedTotalsforPc(String pfId, String adjOBYear) {
	log.info("AdjCrtnDAO:getAdjCrtnFinalizedTotalsforPc-- Entering Method");

	String sqlQuery = "", prefixWhereClause = "";
	EmployeePensionCardInfo data = null;
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	ArrayList finalizedinfo = null;

	sqlQuery = " select pensionno, nvl(empsubscription, 0) as empsub, nvl(empsubint, 0) as empsubint, nvl(aaicontri, 0) as aaicontri, nvl(aaicontriint, 0) as aaicontriint,"
				+" nvl(pcprincipal, 0) as pcprincipal, nvl(pcinterest,0) as pcinterest  from epis_adj_crtn_transactionspc where approvedtype = 'Approved'  and pensionno ="+pfId+"   and adjobyear  = '"+adjOBYear+"'";  
	log.info("sql query " + sqlQuery);
	try {
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery.toString());
		finalizedinfo = new ArrayList();
		while (rs.next()) {
			 
			data = new EmployeePensionCardInfo();

			if (rs.getString("pensionno") != null) {
				data.setPensionNo(rs.getString("pensionno"));
			} else {
				data.setPensionNo("");
			}
			 
			if (rs.getString("empsub") != null) {
				data.setEmpSub(rs.getString("empsub"));
			} else {
				data.setEmpSub("0");
			}
			if (rs.getString("empsubint") != null) {
				data.setEmpSubInterest(rs.getString("empsubint"));
			} else {
				data.setEmpSubInterest("0");
			}

			if (rs.getString("aaicontri") != null) {
				data.setAaiContri(rs.getString("aaicontri"));
			} else {
				data.setAaiContri("0");
			}

			if (rs.getString("aaicontriint") != null) {
				data.setAaiContriInterest(rs.getString("aaicontriint"));
			} else {
				data.setAaiContriInterest("");
			}
			if (rs.getString("pcprincipal") != null) {
				data.setPensionTotal(rs.getString("pcprincipal"));
			} else {
				data.setPensionTotal("0");
			}

			if (rs.getString("pcinterest") != null) {
				data.setPensionInterest(rs.getString("pcinterest"));
			} else {
				data.setPensionInterest("0");
			}
			  
			finalizedinfo.add(data);
		}

		log.info("getAdjCrtnFinalizedTotals list size " + finalizedinfo.size());

	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(con, st, rs);
	}
	return finalizedinfo;
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
public ArrayList generateform4report(String pensionno,String empName,String remmonth,String emoluemnts,String diffpc,String diffaddcontri,String remarks,String id,String adjyear,String totalpc,String totalinterstpc) {
	log.info("AdjCrtnDAO::statmentpcwagesreport");
	log.info("diffpc =="+diffpc);
	String transId = id;
	Statement st = null;
	ResultSet rs = null;
	String form4insert="",form4idupdate="",fromYear="",toYear="",sqlQuery="",totpc="";
	double pcemoluments=0.0;
	Connection con = null;
	String years[] =null;
	years =adjyear.split("-");
		if(adjyear.equals("2012-2013")){
			fromYear = "01-Apr-"+years[0];
			toYear = "30-Apr-"+years[1];
		}
		else if(Integer.parseInt(adjyear.substring(0, 4))>=2013){
		fromYear = "01-May-"+years[0];
		toYear = "30-Apr-"+years[1];
		}
		else {
		fromYear = "01-Apr-"+years[0];
		toYear = "31-Mar-"+years[1]; 
		} 
	ArrayList empDataList = new ArrayList();
	SuppPCBean personalInfo = new SuppPCBean();
	//EmployeeCardReportInfo cardInfo = null;
	SuppPCBeanData cardInfo = null;
	ArrayList list = new ArrayList();

	ArrayList cardList = new ArrayList();
	try {
		con = commonDB.getConnection();
		st = con.createStatement();
		if(id.equals("---")){
		transId=this.getTransIdForForm4(con);
		if(!adjyear.equals("1995-2008")){
		sqlQuery = "select (sum(nvl((c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end)), 0)))+sum(nvl(round(( c.pensioncontri - (case when v.empflag='N' then 0 else nvl(v.pensioncontri, 0) end))*12/100*((select floor(months_between('"+remmonth+"',add_months(c.monthyear, 1))) from dual))/12,0),0)) as totpc from epis_adj_crtnpc c, employee_pension_validate v where c.pensionno = v.pensionno(+) and c.monthyear = v.monthyear(+) and c.pensionno = "+pensionno+" and c.monthyear between '"+fromYear+"' and '"+toYear+"' and c.datamodifiedflag = 'Y' and c.empflag = v.empflag(+) ";
		log.info("sqlQuery=="+sqlQuery);
		rs = st.executeQuery(sqlQuery);
		if(rs.next()){
			totpc=rs.getString("totpc");
		}
		diffpc=totpc;
		log.info("for pc =="+sqlQuery);
		}
		pcemoluments=Math.round((Double.parseDouble(totalpc)/8.33)*100);
		log.info("diffpc =="+diffpc);
		form4insert="insert into epis_form4pc(PENSIONNO,EMPLOYEENAME,SAL_MONTH,EMOLUMENTS,ECONTRI,PC,ADDCONTRI,ADDCONTRIPC,REMARKS,FORM4ID,ADJYEAR,TOTALDIFFPC,TOTALINTRESTPC,PCEMOLUMENTS) values ('"+pensionno+"','"+empName+"','"+remmonth+"','"+emoluemnts+"','"+diffpc+"','"+diffpc+"','"+diffaddcontri+"','"+diffaddcontri+"','"+remarks+"','"+transId+"','"+adjyear+"','"+totalpc+"','"+totalinterstpc+"','"+pcemoluments+"')";
		form4idupdate =" update epis_adj_crtn_pfid_trackingpc p set p.form4id="+transId+" where  pensionno="+pensionno+" and REM_MONTH='"+remmonth+"' and ADJOBYEAR='"+adjyear+"' ";
		log.info("form4insert"+ form4insert);
		st.addBatch(form4insert);
		log.info("form4idupdate "+form4idupdate);
		st.addBatch(form4idupdate);
		int insertCount[]=st.executeBatch();
		log.info("insertCount  "+insertCount.length);
		}
		empDataList = this.SumofSuppPCReportPensonalInfo1(con,pensionno,adjyear);
		for (int i = 0; i < empDataList.size(); i++) {
			cardInfo = new SuppPCBeanData();
			personalInfo = (SuppPCBean) empDataList.get(i);
			log
					.info("FincialReportDAO:::getStatementOfWagePension:Final Settlement Date"
							+ personalInfo.getPensionno()
							+ "Resttlement Date"
							+ personalInfo.getRem_month());
			
			list = this.SumofSuppPCReportDataforform4(con, transId);
			cardInfo.setPersonalInfo(personalInfo);
			cardInfo.setPensionCardList(list);
			cardList.add(cardInfo);

		}
		

	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, null, null);
	}

	return cardList;
}
public ArrayList generateform4report2(String id,String pensionNo) {
	log.info("AdjCrtnDAO::statmentpcwagesreport");
	String transId = id;
	Statement st = null;
	ResultSet rs = null;
	String form4insert="",form4idupdate="",fromYear="",toYear="",sqlQuery="",totpc="";
	Connection con = null;
	ArrayList empDataList = new ArrayList();
	SuppPCBean personalInfo = new SuppPCBean();
	//EmployeeCardReportInfo cardInfo = null;
	SuppPCBeanData cardInfo = null;
	ArrayList list = new ArrayList();
	ArrayList cardList = new ArrayList();
	try {
		con = commonDB.getConnection();
		st = con.createStatement();
		empDataList = this.SumofSuppPCReportPensonalInfo(con,pensionNo);
		for (int i = 0; i < empDataList.size(); i++) {
			cardInfo = new SuppPCBeanData();
			personalInfo = (SuppPCBean) empDataList.get(i);
			log
					.info("FincialReportDAO:::getStatementOfWagePension:Final Settlement Date"
							+ personalInfo.getPensionno()
							+ "Resttlement Date"
							+ personalInfo.getRem_month());
			
			list = this.SumofSuppPCReportDataforform4report(con, id);
			cardInfo.setPersonalInfo(personalInfo);
			cardInfo.setPensionCardList(list);
			cardList.add(cardInfo);

		}
		

	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, null, null);
	}

	return cardList;
}
public ArrayList SumofSuppPCReportDataforform4report(Connection con,
		String transId) {
	log.info("Pfids:::::::::"+transId);
	String sqlQuery = "";	
	//String[] adjyears = adjyear.split("-");
	//String adjyear1 = adjyears[0]; // 004
	//String adjyear2 = adjyears[1];
	
	SuppPCBeanData cardInfo = null;
	ArrayList pensionList = new ArrayList();
	ArrayList arrearslist = new ArrayList();

	Statement st = null;
	ResultSet rs = null;

	sqlQuery = "select to_char(p.sal_month,'dd-Mon-yyyy') as sal_month,p.emoluments as emoluments,p.econtri,p.pc,p.REMARKS,p.form4id from epis_form4pc p where p.form4id= "+transId+" ";

	log.info(sqlQuery);
	try {
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			cardInfo = new SuppPCBeanData();
			if (rs.getString("emoluments") != null) {
				cardInfo.setEmolumentsform4(rs.getString("emoluments"));
			} 
			else{
				cardInfo.setEmolumentsform4("0");
			}
			if (rs.getString("econtri") != null) {
				cardInfo.setEcontri(rs.getString("econtri"));
			} 
			else{
				cardInfo.setEcontri("0");
			}
			if (rs.getString("form4id") != null) {
				cardInfo.setForm4id(rs.getString("form4id"));
			} 
			else{
				cardInfo.setForm4id("---");
			}
			if (rs.getString("pc") != null) {
				cardInfo.setPc(rs.getString("pc"));
			}
			else{
				cardInfo.setPc("0");
			}
			if (rs.getString("sal_month") != null) {
				cardInfo.setSal_month(rs.getString("sal_month"));
			}
			else{
				cardInfo.setSal_month("---");
			}
			if (rs.getString("REMARKS") != null) {
				cardInfo.setRemarks(rs.getString("REMARKS"));
			}
			else{
				cardInfo.setRemarks("---");
			}
			arrearslist.add(cardInfo);
		}
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}

	return arrearslist;

}
public String getTransIdForForm4(Connection con) {
	Statement st = null;
	ResultSet rs = null;
	String sqlQuery = "";
	String transId = "";
	try {
		st = con.createStatement();
		sqlQuery = "select FORM4ID.nextval as FORM4ID from dual";
		rs = st.executeQuery(sqlQuery);
		if (rs.next()) {
			transId = rs.getString("FORM4ID");
		}
	} catch (SQLException e) {
		log.printStackTrace(e);
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}
	return transId;
}
public ArrayList SumofSuppPCReportDataforform4(Connection con,
		String transId) {
	log.info("Pfids:::::::::"+transId);
	String sqlQuery = "";	
	//String[] adjyears = adjyear.split("-");
	//String adjyear1 = adjyears[0]; // 004
	//String adjyear2 = adjyears[1];
	
	SuppPCBeanData cardInfo = null;
	ArrayList pensionList = new ArrayList();
	ArrayList arrearslist = new ArrayList();

	Statement st = null;
	ResultSet rs = null;

	sqlQuery = "select to_char(p.sal_month,'dd-Mon-yyyy') as sal_month,p.emoluments as emoluments,p.econtri,p.pc,P.ADDCONTRI,P.ADDCONTRIPC,p.REMARKS,p.form4id,p.TOTALDIFFPC,p.TOTALINTRESTPC,p.PCEMOLUMENTS from epis_form4pc p where p.form4id= "+transId+" ";

	log.info(sqlQuery);
	try {
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			cardInfo = new SuppPCBeanData();
			if (rs.getString("emoluments") != null) {
				cardInfo.setEmolumentsform4(rs.getString("emoluments"));
			} 
			else{
				cardInfo.setEmolumentsform4("0");
			}
			if (rs.getString("econtri") != null) {
				cardInfo.setEcontri(rs.getString("econtri"));
			} 
			else{
				cardInfo.setEcontri("0");
			}
			if (rs.getString("ADDCONTRI") != null) {
				cardInfo.setAddcontri(rs.getString("ADDCONTRI"));
			} 
			else{
				cardInfo.setAddcontri("0");
			}
			if (rs.getString("ADDCONTRIPC") != null) {
				cardInfo.setAddcontripc(rs.getString("ADDCONTRIPC"));
			} 
			else{
				cardInfo.setAddcontripc("0");
			}
			if (rs.getString("form4id") != null) {
				cardInfo.setForm4id(rs.getString("form4id"));
			} 
			else{
				cardInfo.setForm4id("---");
			}
			if (rs.getString("pc") != null) {
				cardInfo.setPc(rs.getString("pc"));
			}
			else{
				cardInfo.setPc("0");
			}
			if (rs.getString("sal_month") != null) {
				cardInfo.setSal_month(rs.getString("sal_month"));
			}
			else{
				cardInfo.setSal_month("---");
			}
			if (rs.getString("REMARKS") != null) {
				cardInfo.setRemarks(rs.getString("REMARKS"));
			}
			else{
				cardInfo.setRemarks("---");
			}
			if (rs.getString("TOTALDIFFPC") != null) {
				cardInfo.setTotaldiffpc(rs.getString("TOTALDIFFPC"));
			} 
			else{
				cardInfo.setTotaldiffpc("0");
			}
			if (rs.getString("TOTALINTRESTPC") != null) {
				cardInfo.setTotalintrestpc(rs.getString("TOTALINTRESTPC"));
			} 
			else{
				cardInfo.setTotalintrestpc("0");
			}
			if (rs.getString("PCEMOLUMENTS") != null) {
				cardInfo.setPcemoluments(rs.getString("PCEMOLUMENTS"));
			} 
			else{
				cardInfo.setPcemoluments("0");
			}
			arrearslist.add(cardInfo);
		}
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}

	return arrearslist;

}
public ArrayList getStationList(String region,String monthyear,String aiportcode){
	 log.info("monthyear===="+monthyear+"aiportcode"+aiportcode);
	 ArrayList stationsList =null;
	 SuppPCBean cardInfo=null;
	 String sqlQry="",sqlCountQry="",sqlInsertQry="",form4flag="",station="",regionRegion="",accountType="",orders="",condition="",pf="",inspCharges="",uploadpc="",pc="",notes="",dateOfReceipt="";
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
		 sqlQry="select a.*,i.region from (select p.pensionno,p.adjobyear,p.verifiedby,p.apporvedby,p.rem_month,p.form4id,nvl(v.form4flag,'N') as form4flag from epis_adj_crtn_pfid_trackingpc p, employee_pension_validate v where v.pensionno(+)=p.pensionno and last_day(v.monthyear(+))=last_day(p.rem_month) )a,employee_personal_info i  where a.pensionno=i.pensionno and a.rem_month='"+monthyear+"'";
		
		 if(!region.equals("All")){	
			 sqlQry +=" and i.region = '"+region+"' order by a.pensionno ";
			 }else{
				 sqlQry +=" order by a.pensionno ";
			 }
		 
		 log.info("sqlQry====="+sqlQry);
		 rs=st.executeQuery(sqlQry);
		 stationsList = new ArrayList();
		 while(rs.next()){
			 cardInfo= new  SuppPCBean();
			 form4flag=rs.getString("form4flag");
			 if(form4flag.equals("S")){
				 cardInfo.setForm4flag("submitted");
			 }else{
				 cardInfo.setForm4flag("Not submitted");
			 }
			 if (rs.getString("pensionno") != null) {
					cardInfo.setPensionno(rs.getString("pensionno"));
				} 
				if (rs.getString("verifiedby") != null) {
					cardInfo.setVerifiedby(rs.getString("verifiedby"));
				}else{
					cardInfo.setVerifiedby("---");
				}
				if (rs.getString("adjobyear") != null) {
					cardInfo.setAdjobyear(rs.getString("adjobyear"));
				}else{
					cardInfo.setAdjobyear("---");
				}
				if (rs.getString("apporvedby") != null) {
					cardInfo.setApporvedby(rs.getString("apporvedby"));
				}else{
					cardInfo.setApporvedby("---");
				}
				if (rs.getString("rem_month") != null) {
					cardInfo.setRem_month(rs.getString("rem_month"));
				}
				else{
					cardInfo.setRem_month("----");
				}
				if (rs.getString("form4id") != null) {
					cardInfo.setForm4id(rs.getString("form4id"));
				}else{
					cardInfo.setForm4id("---");
				}
				if (rs.getString("region") != null) {
					cardInfo.setRegion(rs.getString("region"));
				} else {
					cardInfo.setRegion("---");
				} 
			 stationsList.add(cardInfo);
			 //log.info("Stationlist Size"+stationsList.size());
		 }
		 
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally{
		 commonDB.closeConnection(con,st,rs);
	 }
	 
	 return stationsList;
}
public ArrayList BifurcationReport(String pensionno) {
	log.info("AdjCrtnDAO::BifurcationReport");
	Connection con = null;

	ArrayList empDataList = new ArrayList();
	SuppPCBean personalInfo = new SuppPCBean();
	//EmployeeCardReportInfo cardInfo = null;
	SuppPCBeanData cardInfo = null;
	ArrayList list = new ArrayList();

	ArrayList cardList = new ArrayList();
	try {
		con = commonDB.getConnection();
		empDataList = this.SumofSuppPCReportPensonalInfo(con,pensionno);

		for (int i = 0; i < empDataList.size(); i++) {
			cardInfo = new SuppPCBeanData();
			personalInfo = (SuppPCBean) empDataList.get(i);
		}
		list = this.BifurcationReport(con, pensionno);
		cardInfo.setPersonalInfo(personalInfo);
		cardInfo.setPensionCardList(list);
		cardList.add(cardInfo);

	} catch (Exception se) {
		log.printStackTrace(se);
	} finally {
		commonDB.closeConnection(con, null, null);
	}

	return cardList;
}
public ArrayList BifurcationReport(Connection con,
		String pensionNo) {
	//log.info("Pfids:::::::::"+pensionNo+"toDate::::::::::"+remMonth+"::adjyear=============="+adjyear);
	String  sqlQuery = "",fromYear="",toYear="",diffpc="",intrestpc="";
	double totdiffpc=0.0,totinterserpc=0.0;
	SuppPCBeanData cardInfo = null;
	ArrayList pensionList = new ArrayList();
	ArrayList arrearslist = new ArrayList();

	Statement st = null;
	ResultSet rs = null;

	sqlQuery = " select to_char((c.monthyear)-1,'Mon')|| '/' ||to_char(c.monthyear,'Mon') as monthyear,to_char(v.monthyear,'dd-Mon-yyyy') as monthyear1,to_char(c.monthyear,'yyyy') as finyear,nvl(c.emoluments,0) as emoluments,nvl(v.emoluments,0) as emoluments1,(nvl(c.emoluments,0)-nvl(v.emoluments,0)) as deffemoluments,"
		     +"(case when (select wetheroption from employee_personal_info p where p.pensionno = c.pensionno)='B' then round(c.pensioncontri) else (case when c.monthyear < '01-Apr-2008' then round(c.emoluments * 8.33 / 100) else nvl (c.pensioncontri, 0) end) end) as pensioncontri,(case when v.monthyear<'01-Apr-2008' then round(v.emoluments*8.33/100) else nvl(v.pensioncontri,0) end) as pensioncontri1,(select sum(pc) from epis_form4pc p where p.pensionno=c.pensionno) as pc,(select wetheroption from employee_personal_info p where p.pensionno = c.pensionno) as pensionoption,c.emolumentmonths from employee_pension_validate v, epis_adj_crtnpc c where v.pensionno(+) = c.pensionno and c.monthyear = v.monthyear(+) and c.empflag=v.empflag(+) and c.empflag='Y' and c.datamodifiedflag='Y' and c.pensionno="+pensionNo+" order by c.monthyear";

	log.info("sqlqurey==="+sqlQuery);
	try {
		st = con.createStatement();
		rs = st.executeQuery(sqlQuery);
		while (rs.next()) {
			cardInfo = new SuppPCBeanData();
			
			if (rs.getString("pensionoption") != null) {
				cardInfo.setPensionOption(rs.getString("pensionoption"));
			}
			else{
				cardInfo.setPensionOption("---");
			}
			if (rs.getString("monthyear") != null) {
				cardInfo.setBifercationmonthyear(rs.getString("monthyear"));
			}
			else{
				cardInfo.setBifercationmonthyear("---");
			}
			if (rs.getString("emoluments") != null) {
				cardInfo.setNewbifercationemoluments(rs.getString("emoluments"));
			}
			else{
				cardInfo.setNewbifercationemoluments("0");
			}
			if (rs.getString("emoluments1") != null) {
				cardInfo.setOldbifercationemoluments(rs.getString("emoluments1"));
			}
			else{
				cardInfo.setOldbifercationemoluments("0");
			}
			if (rs.getString("deffemoluments") != null) {
				cardInfo.setDiffbifercationemoluments(rs.getString("deffemoluments"));
			}
			else{
				cardInfo.setDiffbifercationemoluments("0");
			}
			if (rs.getString("pensioncontri") != null) {
				cardInfo.setNewbifercationpc(rs.getString("pensioncontri"));
			}
			else{
				cardInfo.setNewbifercationpc("0");
			}
			if (rs.getString("pensioncontri1") != null) {
				if(rs.getString("monthyear1")!=null){
				cardInfo.setOldbifercationpc(String.valueOf(commonDAO.pensionCalculation(rs.getString("monthyear1"),cardInfo.getOldbifercationemoluments(),cardInfo.getPensionOption(),"","1")) );
			
				}else{
					
					cardInfo.setOldbifercationpc(rs.getString("pensioncontri1"));
					}
				}
			else{
				cardInfo.setOldbifercationpc("0");
			}
			if (rs.getString("finyear") != null) {
				cardInfo.setFinyear(rs.getString("finyear"));
			} 
			else{
				cardInfo.setFinyear("0");
			}
			if (rs.getString("pc") != null) {
				cardInfo.setBifercationpc(rs.getString("pc"));
			}
			else{
				cardInfo.setBifercationpc("0");
			}
			
			
			if (rs.getString("emolumentmonths") != null) {
				cardInfo.setEmolumentmonths(rs.getString("emolumentmonths"));
			}
			else{
				cardInfo.setEmolumentmonths("1");
			}
			
			//totdiffpc=totdiffpc+Double.parseDouble(diffpc);
			////totinterserpc=totinterserpc+Double.parseDouble(intrestpc);
			//cardInfo.setTotdiffpc(totdiffpc);
			//cardInfo.setTotinterserpc(totinterserpc);
			
			arrearslist.add(cardInfo);
		}
		
	} catch (Exception e) {
		log.printStackTrace(e);
	} finally {
		commonDB.closeConnection(null, st, rs);
	}

	return arrearslist;

}


}
