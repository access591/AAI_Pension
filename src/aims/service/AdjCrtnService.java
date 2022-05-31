package aims.service;

import java.sql.SQLException;
import java.util.ArrayList;

import aims.bean.EmpMasterBean;
import aims.bean.adjcrnt.AdjCrntSaveDtlBean;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.AdjCrtnDAO;

public class AdjCrtnService {

	/**
	 * @param args
	 */
	
 AdjCrtnDAO adjCrtnDAO= new AdjCrtnDAO();
 private  static final Log log=new Log(AdjCrtnService.class);
	public int insertEmployeeTransData(String pfId, String frmName,
			String username, String ipaddress, String flag, String mFlag,
			String cpfacno, String region, String upflag) throws Exception {
		int result = 0;
		result =  adjCrtnDAO.insertEmployeeTransData(pfId, frmName, username,
				ipaddress, flag, mFlag, cpfacno, region, upflag);
		return result;

	}
	//	Modified on 13-Feb-2012 for providing Mapping Adj Calc
	//	 New methd
	public int insertRecordForAdjCtrnTracking(String empserialNO,String cpfAccno,String adjOBYear,String reasonForInsert,String username, String ipaddress) throws InvalidDataException{
		 int result=0;
		 try {
			result=adjCrtnDAO.insertRecordForAdjCtrnTracking(empserialNO,cpfAccno,adjOBYear,reasonForInsert,username,ipaddress);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw e;
		}
		 return result;
		 
	 }
//	 new method 
	public ArrayList getPCAdjDiff(String pfId, String adjOBYear)
			throws Exception {
		ArrayList pcAdjDiffList = new ArrayList();
		pcAdjDiffList = adjCrtnDAO.getPCAdjDiff(pfId, adjOBYear);
		return pcAdjDiffList;

	}

	public String chkPfidinAdjCrtn(String pfId,String frmName) {
		String chkPfid = "";
		frmName = "adjcorrections";
		chkPfid = adjCrtnDAO.chkPfidinAdjCrtn(pfId, frmName);
		return chkPfid;
	}
	public String chkPfidBRF(String pfId) {
		String chkPfid = "";
		chkPfid = adjCrtnDAO.chkPfidinAdjCrtnBRF(pfId);
		return chkPfid;
	}

	public String chkPfidinAdjCrtnTracking(String pfId ,String frmName) {
		String chkPfid = "";
		frmName = "adjcorrections";
		chkPfid = adjCrtnDAO.chkPfidinAdjCrtnTracking(pfId, frmName);
		return chkPfid;
	}
	public String chkPfidinAdjCrtn78PSTracking(String pfId ,String frmName) {
		String chkPfid = "";		 
		chkPfid = adjCrtnDAO.chkPfidinAdjCrtn78PSTracking(pfId, frmName);
		return chkPfid;
	}
	public int saveCurrPctoals(String pfid,String adjObYear,ArrayList currPcTotals){
		 int result=0;
		 result=adjCrtnDAO.saveCurrPctoals( pfid, adjObYear, currPcTotals);
		 return result;
		 
		}
	 public int   updateStageWiseStatusInAdjCtrn(String empserialNO,String processedStage,String reportYear,String form2Status,String jvno ,String username,String loginUserId,String userRegion,String loginUsrStation,String loginUsrDesgn){
			int result =0;
			result = adjCrtnDAO.updateStageWiseStatusInAdjCtrn( empserialNO, processedStage,reportYear,form2Status,jvno, username,loginUserId,userRegion,loginUsrStation,loginUsrDesgn);		
			return  result;
	}
	 public ArrayList  searchAdjctrn(String userRegion,String userStation,String profileType,String accessCode,String accountType,String employeeNo,String reportYear, String status){
		 ArrayList searchList = new ArrayList();
		 searchList =  adjCrtnDAO.searchAdjctrn(userRegion,userStation,profileType,accessCode,accountType,employeeNo,reportYear,status);
		 return searchList;
 }
	 public String insertAdjEmolumenstLog(String pensionno, String reportYear,String notFianalizetransID) throws InvalidDataException{
		 String transId="";
		 try {
			transId = adjCrtnDAO.insertAdjEmolumenstLog(pensionno,reportYear, notFianalizetransID);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw e;
		}
		 return transId;
	    }
	 public ArrayList  getAdjEmolumentsLog(String pfid,String adjOBYear,String frmName){
		 ArrayList adjemolList = new ArrayList();
		 adjemolList =  adjCrtnDAO.getAdjEmolumentsLog(pfid ,adjOBYear,frmName);
		 return adjemolList;
	    }
	 public String  getDeleteAllRecords(String pfid,String reportYear,String frmName, String username,String ipaddress,String flag, String chqFlag,String empSubTot,String aaiContriTot,String pensionTot){
		 String message="";
		 message =  adjCrtnDAO.getDeleteAllRecords(pfid, reportYear,frmName,username,ipaddress ,flag ,chqFlag,empSubTot,aaiContriTot,pensionTot);
		 return message;
	    }
//	 New methd
	public int   saveprvadjcrtntotals(String empserialNO,String adjOBYear,String form2Status,double EmolumentsTot,double cpfTotal,double cpfIntrst,double PenContriTotal,double PensionIntrst,double PFTotal,double PFIntrst,double EmpSub ,double EmpSubInterest ,double adjEmpSubInterest ,double AAIContri,double AAIContriInterest,double adjAAiContriInterest,double aaiPensionContriInterest,double FSEmpSubInterest,double FSAAIContriInterest)throws SQLException, InvalidDataException{
			int result =0;
			try {
				result = adjCrtnDAO.saveprvadjcrtntotals( empserialNO,adjOBYear,form2Status, EmolumentsTot, cpfTotal,cpfIntrst,PenContriTotal,PensionIntrst,PFTotal,PFIntrst,EmpSub ,EmpSubInterest ,adjEmpSubInterest,AAIContri,AAIContriInterest,adjAAiContriInterest,aaiPensionContriInterest,FSEmpSubInterest,FSAAIContriInterest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
				throw new InvalidDataException(e.getMessage());
			}			
			return  result;
		} 
	public int savePrePctoalsTemp(String empserialNO,String adjObYear,ArrayList prePcTotals){
			 int result=0;
			 result=adjCrtnDAO.savePrePctoalsTemp( empserialNO, adjObYear, prePcTotals);
			 return result;
			 
		}
	 public void insertAdjEmolumenstLogInTemp(ArrayList adjEmolList,String pensionno, String reportYear,String notFianalizetransID){
		 adjCrtnDAO.insertAdjEmolumenstLogInTemp(adjEmolList,pensionno,reportYear, notFianalizetransID);
	    }
	 public String chkPfidinAdjCrtnTrackingForUpload(String pfid,String formname){
			String chkPfid = "";
			chkPfid = adjCrtnDAO.chkPfidinAdjCrtnTrackingForUpload(pfid, formname);
			return chkPfid;
		}
	 public String getAdjCrtnNotFinalizedTransId(String empserialNO, String reportYear ) {
			String notFianalizetransID="";
			notFianalizetransID=adjCrtnDAO.getAdjCrtnNotFinalizedTransId(empserialNO, reportYear );
			return notFianalizetransID;
		}
	 public String getAdjCrtnFinalizedFlag(String empserialNO, String reportYear ) {
			String finalizedFlag="";
			finalizedFlag=adjCrtnDAO.getAdjCrtnFinalizedFlag(empserialNO, reportYear );
			return finalizedFlag;
		}
	 
	 public ArrayList updatePCAdjCorrections(String fromDate, String toDate, String region,
				String airportcode, String empserialNO, String cpfaccnostrip,String regionstrip ){
		 ArrayList prePcTotals=new ArrayList();
		 prePcTotals=adjCrtnDAO.updatePCAdjCorrections(fromDate,toDate,region,
					airportcode,  empserialNO, cpfaccnostrip, regionstrip );
		 return prePcTotals;
		 
		} 
	 
	public String adjCrtnMappingUpdate(String pfid, String cpfaccno, String region,
				String username, String ipaddress,String airportCode) {
			String updatePfid="";
			updatePfid=adjCrtnDAO.adjCrtnMappingUpdate(pfid, cpfaccno, region, username, ipaddress,airportCode);
			return updatePfid;
		}
	public ArrayList getPensionContributionReportForAdjCRTN(String frmYear,String toYear,String region,String airportcode  ,String empserialNO,String cpfAccno,String batchid ,String ReportStatus){
        ArrayList PensionContributionList=new ArrayList();
       PensionContributionList=adjCrtnDAO.getPensionContributionReportForAdjCRTN(frmYear,toYear,region,airportcode,empserialNO,cpfAccno,batchid,ReportStatus);
   return PensionContributionList; 
		
	} 
	
	public ArrayList  getPrevPCGrandTotalsForAdjCrtn(String pfid,String adjOBYear,String batchid,String transIdToGetPrevData) throws SQLException{
		ArrayList grandTotList = new ArrayList();
		grandTotList = adjCrtnDAO.getPrevPCGrandTotalsForAdjCrtn(pfid,adjOBYear,batchid,  transIdToGetPrevData);
		return  grandTotList;
	}
	
	public ArrayList SumofSuppPCReport(String pensionno) {
		ArrayList list=new ArrayList();
		list=adjCrtnDAO.SumofSuppPCReport(pensionno);
		return list;
	}

	//New Method
	 public ArrayList pfCardReportForAdjCrtn(String range,String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo,String lastmonthFlag,String lastmonthYear,String stationName,String bulkFlag){
	        ArrayList pfCardLst=new ArrayList();
	        
	       pfCardLst=adjCrtnDAO.empPFCardReportPrintForAdjCrtn(range,region,selectedYear,flag,employeeName,sortedColumn,pensionNo,lastmonthFlag,lastmonthYear,stationName,bulkFlag);
	       //finReportDAO. updatePFCardReport(region,selectedYear,flag,employeeName,sortedColumn,pensionNo);
	        return pfCardLst;
	    }
	  //New method
	    public ArrayList editTransactionDataForAdjCrtn(String cpfAccno,String monthyear,String emoluments,String addcon,String epf,String vpf,String principle,String interest,String advance,String loan,String aailoan,String contri,String noofmonths,String pfid,String region,String airportcode,String username,String computername,String from7narration,String duputationflag,String pensionoption,
	    		String empnetob,String aainetob,String empnetobFlag,String finYear,String editTransFlag,String dateOfBirth){
	        ArrayList adjEmolList = new ArrayList();
	        adjEmolList = adjCrtnDAO.editTransactionDataForAdjCrtn(cpfAccno,monthyear,emoluments,addcon,epf,vpf,principle,interest,advance,loan,aailoan,contri,noofmonths,pfid,region,airportcode,username,computername,from7narration,duputationflag,pensionoption,empnetob,aainetob,empnetobFlag,finYear,editTransFlag,dateOfBirth);
	        return adjEmolList;
	    }
	public ArrayList getPCReportFor78PsAdjCRTN(String frmYear,String toYear,String region,String airportcode,String selectedMonth,String empserialNO,String cpfAccno,String transferFlag,String mappingFlag,String pfIDStrip,String bulkPrint,String recoverieTable){
	        String  tempToYear="",finMonth="",selectedToYear="";
	        boolean recoverieTableCheck=false;
	        String[] finMnthYearList=new String[5];
	        boolean monthFlag=false;
	        ArrayList PensionContributionList=new ArrayList();
	       
	      
	        // newline  code for check the recovery table
	         
	          if(recoverieTable.equals("true")){
	        	  recoverieTableCheck=true;  
	          }
	          
	     PensionContributionList=adjCrtnDAO.getPCReportFor78PsAdjCRTN(frmYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag, mappingFlag);
	         
			return PensionContributionList;
			 
		}
	 public void editTransactionDataFor78PsAdjCrtn(String cpfAccno,String monthyear,String emoluments,String epf,String vpf,String principle,String interest,String advance,String loan,String aailoan,String contri,String pfid,String region,String airportcode,String username,String computername,String from7narration,
			 String statemntWagesRemarks ,String duputationflag,String pensionoption ,String finYear,String editTransFlag,String dueemoluments, String duepension){
		 adjCrtnDAO.editTransactionDataFor78PsAdjCrtn(cpfAccno,monthyear,emoluments,epf,vpf,principle,interest,advance,loan,aailoan,contri,pfid,region,airportcode,username,computername,from7narration,statemntWagesRemarks,duputationflag,pensionoption, finYear,editTransFlag,dueemoluments,duepension);
	    }
	 public ArrayList  crtnsMadeInPCAndPFCard(String fromDate,String toDate){
		 ArrayList crtnsMadeInList = new ArrayList();
		 crtnsMadeInList =  adjCrtnDAO.crtnsMadeInPCAndPFCard(fromDate,toDate);
		 return crtnsMadeInList;
	 	}
	 public AdjCrntSaveDtlBean saveAdjCrntDetails(String empserialNO,String cpfAccno, String adjOBYear,
				double EmolumentsTot, double cpfTotal, double cpfIntrst,
				double PenContriTotal, double PensionIntrst, double PFTotal,
				double PFIntrst, double EmpSub, double EmpSubInterest,double adjEmpSubIntrst,
				double AAIContri, double AAIContriInterest,double adjAAiContriIntrst,double adjPensionContriInterest,String grandTotDiffShowFlag,String reasonForInsert,String username,String ipaddress,ArrayList adjEmolList,String batchid) throws  InvalidDataException{
		 AdjCrntSaveDtlBean saveDtlsBean;
		 try {
			 saveDtlsBean=adjCrtnDAO.saveAdjCrntDetails(empserialNO,cpfAccno,adjOBYear,
					 EmolumentsTot,cpfTotal,cpfIntrst,PenContriTotal,PensionIntrst,
					 PFTotal,PFIntrst,EmpSub,EmpSubInterest,adjEmpSubIntrst,AAIContri,AAIContriInterest,adjAAiContriIntrst,adjPensionContriInterest, grandTotDiffShowFlag,reasonForInsert,username,ipaddress,adjEmolList,batchid);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw e;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw new InvalidDataException(e.getMessage());
		}
		return saveDtlsBean;
	 }
	 public AdjCrntSaveDtlBean getFinzdPreviouseGrndTotals(String empserialNO,String adjOBYear,String batchid ){
		 AdjCrntSaveDtlBean grandTotalBean=new AdjCrntSaveDtlBean();
		 grandTotalBean=adjCrtnDAO.getFinzdPreviouseGrndTotals(empserialNO,adjOBYear,batchid);
		 return grandTotalBean;
	 }	
	 public ArrayList  getApprovedRecords(String employeeNo){
		 ArrayList searchList = new ArrayList();
		 searchList =  adjCrtnDAO.getApprovedRecords(employeeNo);
		 return searchList;
	}
	public int  updateApprovedRecord(String empserialNo,String trackId,String notes,String statusType,String status,String loginUserId,String adjobYear){
		 int result=0;
		
		 result =  adjCrtnDAO.updateApprovedRecord(empserialNo,trackId,notes,statusType,status,loginUserId,adjobYear);
		 return result;
	}
	public ArrayList  getBlockedPfidsReport( ){
		 ArrayList searchList = new ArrayList();
		 searchList =  adjCrtnDAO.getBlockedPfidsReport();
		 return searchList;
	}
	public EmpMasterBean  getImpCalCForm2Data(String pensionno,String frmName,String adjobyear,String form2id){
		EmpMasterBean empBean = new EmpMasterBean();
		empBean =  adjCrtnDAO.getImpCalCForm2Data(pensionno,frmName,adjobyear,form2id);
		 return empBean;
	}
	//new
	public String getForm2id(String pensionno,String adjobyear){
		String form2id="";
		form2id=adjCrtnDAO.getForm2id( pensionno,adjobyear);
		return form2id;
	}


	public ArrayList  searchFor12MnthStatemntCtrn(String  userRegion,String  userStation,String  profileType ,String  accountType,String  employeeNo){
		 ArrayList employeeList = new ArrayList();
		 employeeList =  adjCrtnDAO.searchFor12MnthStatemntCtrn(userRegion,userStation,profileType,  accountType,employeeNo);  
		 return employeeList;
	}
	 public String  getCHQDeletion(String pfid,String reportYear,String frmName, String username,String ipaddress,  String form2Stats ,String chqFlag,String empSubTot, String aaiContriTot,String pensionTot){
		 String message="";
		 message =  adjCrtnDAO.getCHQDeletion(pfid, reportYear,frmName,username,ipaddress ,form2Stats,chqFlag,empSubTot,aaiContriTot,pensionTot);
		 return message;
	    }
	 public EmpMasterBean  getImpCalCForm2CHQData(String pensionno,String adjobyear){
			EmpMasterBean empBean = new EmpMasterBean();
			empBean =  adjCrtnDAO.getImpCalCForm2CHQData(pensionno,adjobyear);
			 return empBean;
		}
	 public String getCHQApproverEditStatus(String pensionno,String reportYear,String frmName){
			String editStatus="";
			editStatus=adjCrtnDAO.getCHQApproverEditStatus( pensionno,reportYear,frmName);
			return editStatus;
		}
	 public String chkPfidStatusInAdjCrtn(String pfId) {
			String chkPfid = "";			 
			chkPfid = adjCrtnDAO.chkPfidStatusInAdjCrtn(pfId);
			return chkPfid;
		}
	 public String chkPfidStatusInAdjCrtnForPCReport(String pfId) {
			String chkPfid = "";			 
			chkPfid = adjCrtnDAO.chkPfidStatusInAdjCrtnForPCReport(pfId);
			return chkPfid;
		}
	 public String chkPfidStatusInAdjCrtnForPCReport1(String pfId) {
			String chkPfid = "";			 
			chkPfid = adjCrtnDAO.chkPfidStatusInAdjCrtnForPCReport1(pfId);
			return chkPfid;
		}
	 public ArrayList  getAdjCrtnFinalizedTotals( String  pensionno,String  adjobyear){
		 ArrayList finalizedTotals = new ArrayList();
		 finalizedTotals =  adjCrtnDAO.getAdjCrtnFinalizedTotals(pensionno,adjobyear);  
		 return finalizedTotals;
	}
	 //added by mohan for impact pc report.
		public String chkPfidinAdjCrtnforPc(String pfId,String frmName) {
			String chkPfid = "";
			frmName = "adjcorrections";
			chkPfid = adjCrtnDAO.chkPfidinAdjCrtnforPc(pfId, frmName);
			return chkPfid;
		}
		 public String chkPfidinAdjCrtnTrackingForUploadforPc(String pfid,String formname){
				String chkPfid = "";
				chkPfid = adjCrtnDAO.chkPfidinAdjCrtnTrackingForUploadforPc(pfid, formname);
				return chkPfid;
			}
		 public ArrayList updatePCAdjCorrectionsforPc(String fromDate, String toDate, String region,
					String airportcode, String empserialNO, String cpfaccnostrip,String regionstrip ){
			 ArrayList prePcTotals=new ArrayList();
			 prePcTotals=adjCrtnDAO.updatePCAdjCorrectionsforPc(fromDate,toDate,region,
						airportcode,  empserialNO, cpfaccnostrip, regionstrip );
			 return prePcTotals;
			 
			}
			public int savePrePctoalsTempforPc(String empserialNO,String adjObYear,ArrayList prePcTotals){
				 int result=0;
				 result=adjCrtnDAO.savePrePctoalsTempforPc( empserialNO, adjObYear, prePcTotals);
				 return result;
				 
			}
			public int insertEmployeeTransDataforPc(String pfId, String frmName,
					String username, String ipaddress, String flag, String mFlag,
					String cpfacno, String region, String upflag) throws Exception {
				int result = 0;
				result =  adjCrtnDAO.insertEmployeeTransDataforPc(pfId, frmName, username,
						ipaddress, flag, mFlag, cpfacno, region, upflag);
				return result;

			}	
			public int saveCurrPctoalsforPc(String pfid,String adjObYear,ArrayList currPcTotals){
				 int result=0;
				 result=adjCrtnDAO.saveCurrPctoalsforPc( pfid, adjObYear, currPcTotals);
				 return result;
				 
				}	
		   public ArrayList getPCAdjDiffforPc(String pfId, String adjOBYear)
			throws Exception {
					ArrayList pcAdjDiffList = new ArrayList();
					pcAdjDiffList = adjCrtnDAO.getPCAdjDiffforPc(pfId, adjOBYear);
					return pcAdjDiffList;

	}
		   
			public String chkPfidinAdjCrtnTrackingforPc(String pfId ,String frmName) {
				String chkPfid = "";
				frmName = "adjcorrections";
				chkPfid = adjCrtnDAO.chkPfidinAdjCrtnTrackingforPc(pfId, frmName);
				return chkPfid;
			}	   
			public ArrayList getPensionContributionReportForAdjCRTNforPc(String frmYear,String toYear,String region,String airportcode  ,String empserialNO,String cpfAccno,String batchid ,String ReportStatus){
		        ArrayList PensionContributionList=new ArrayList();
		       PensionContributionList=adjCrtnDAO.getPensionContributionReportForAdjCRTNforPc(frmYear,toYear,region,airportcode,empserialNO,cpfAccno,batchid,ReportStatus);
		   return PensionContributionList; 
				
			} 
			 public ArrayList pfCardReportForAdjCrtnforPc(String range,String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo,String lastmonthFlag,String lastmonthYear,String stationName,String bulkFlag){
			        ArrayList pfCardLst=new ArrayList();
			        
			       pfCardLst=adjCrtnDAO.empPFCardReportPrintForAdjCrtnforPc(range,region,selectedYear,flag,employeeName,sortedColumn,pensionNo,lastmonthFlag,lastmonthYear,stationName,bulkFlag);
			       //finReportDAO. updatePFCardReport(region,selectedYear,flag,employeeName,sortedColumn,pensionNo);
			        return pfCardLst;
			    }	
			 public AdjCrntSaveDtlBean saveAdjCrntDetailsforPc(String empserialNO,String cpfAccno, String adjOBYear,
						double EmolumentsTot, double cpfTotal, double cpfIntrst,
						double PenContriTotal, double PensionIntrst, double PFTotal,
						double PFIntrst, double EmpSub, double EmpSubInterest,double adjEmpSubIntrst,
						double AAIContri, double AAIContriInterest,double adjAAiContriIntrst,double adjPensionContriInterest,String grandTotDiffShowFlag,String reasonForInsert,String username,String ipaddress,ArrayList adjEmolList,String batchid) throws  InvalidDataException{
				 AdjCrntSaveDtlBean saveDtlsBean;
				 try {
					 saveDtlsBean=adjCrtnDAO.saveAdjCrntDetailsforPc(empserialNO,cpfAccno,adjOBYear,
							 EmolumentsTot,cpfTotal,cpfIntrst,PenContriTotal,PensionIntrst,
							 PFTotal,PFIntrst,EmpSub,EmpSubInterest,adjEmpSubIntrst,AAIContri,AAIContriInterest,adjAAiContriIntrst,adjPensionContriInterest, grandTotDiffShowFlag,reasonForInsert,username,ipaddress,adjEmolList,batchid);
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
					throw e;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
					throw new InvalidDataException(e.getMessage());
				}
				return saveDtlsBean;
			 }
			 public AdjCrntSaveDtlBean getFinzdPreviouseGrndTotalsforPc(String empserialNO,String adjOBYear,String batchid ){
				 AdjCrntSaveDtlBean grandTotalBean=new AdjCrntSaveDtlBean();
				 grandTotalBean=adjCrtnDAO.getFinzdPreviouseGrndTotalsforPc(empserialNO,adjOBYear,batchid);
				 return grandTotalBean;
			 }		 
			 public ArrayList editTransactionDataForAdjCrtnforPc(String cpfAccno,String monthyear,String emoluments,String addcon,String epf,String vpf,String principle,String interest,String advance,String loan,String aailoan,String contri,String noofmonths,String pfid,String region,String airportcode,String username,String computername,String from7narration,String duputationflag,String pensionoption,
			    	String empnetob,String aainetob,String empnetobFlag,String finYear,String editTransFlag,String dateOfBirth){
			        ArrayList adjEmolList = new ArrayList();
			        adjEmolList = adjCrtnDAO.editTransactionDataForAdjCrtnforPc(cpfAccno,monthyear,emoluments,addcon,epf,vpf,principle,interest,advance,loan,aailoan,contri,noofmonths,pfid,region,airportcode,username,computername,from7narration,duputationflag,pensionoption,empnetob,aainetob,empnetobFlag,finYear,editTransFlag,dateOfBirth);
			        return adjEmolList;
			    }	
				public int saveprvadjcrtntotalsforPc(String empserialNO,String adjOBYear,String form2Status,double EmolumentsTot,double cpfTotal,double cpfIntrst,double PenContriTotal,double PensionIntrst,double PFTotal,double PFIntrst,double EmpSub ,double EmpSubInterest ,double adjEmpSubInterest ,double AAIContri,double AAIContriInterest,double adjAAiContriInterest,double aaiPensionContriInterest,double FSEmpSubInterest,double FSAAIContriInterest,double totalAdditionalContri)throws SQLException, InvalidDataException{
					int result =0;
					try {
						result = adjCrtnDAO.saveprvadjcrtntotalsforPc( empserialNO,adjOBYear,form2Status, EmolumentsTot, cpfTotal,cpfIntrst,PenContriTotal,PensionIntrst,PFTotal,PFIntrst,EmpSub ,EmpSubInterest ,adjEmpSubInterest,AAIContri,AAIContriInterest,adjAAiContriInterest,aaiPensionContriInterest,FSEmpSubInterest,FSAAIContriInterest,totalAdditionalContri);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.printStackTrace(e);
						throw new InvalidDataException(e.getMessage());
					}			
					return  result;
				}	 
				 public String  getDeleteAllRecordsforPc(String pfid,String reportYear,String frmName, String username,String ipaddress,String flag, String chqFlag,String empSubTot,String aaiContriTot,String pensionTot){
					 String message="";
					 message =  adjCrtnDAO.getDeleteAllRecordsforPc(pfid, reportYear,frmName,username,ipaddress ,flag ,chqFlag,empSubTot,aaiContriTot,pensionTot);
					 return message;
				    } 
				 public int   updateStageWiseStatusInAdjCtrnforPc(String empserialNO,String processedStage,String reportYear,String form2Status,String jvno ,String username,String loginUserId,String userRegion,String loginUsrStation,String loginUsrDesgn){
						int result =0;
						result = adjCrtnDAO.updateStageWiseStatusInAdjCtrnforPc( empserialNO, processedStage,reportYear,form2Status,jvno, username,loginUserId,userRegion,loginUsrStation,loginUsrDesgn);		
						return  result;
				}	 
				 public ArrayList  searchAdjctrnforPc(String userRegion,String userStation,String profileType,String accessCode,String accountType,String employeeNo,String reportYear, String status){
					 ArrayList searchList = new ArrayList();
					 searchList =  adjCrtnDAO.searchAdjctrnforPc(userRegion,userStation,profileType,accessCode,accountType,employeeNo,reportYear,status);
					 return searchList;
			 }
				 public ArrayList  getAdjEmolumentsLogforPc(String pfid,String adjOBYear,String frmName){
					 ArrayList adjemolList = new ArrayList();
					 adjemolList =  adjCrtnDAO.getAdjEmolumentsLogforPc(pfid ,adjOBYear,frmName);
					 return adjemolList;
				    }		 
				 public ArrayList  getAdjCrtnFinalizedTotalsforPc( String  pensionno,String  adjobyear){
					 ArrayList finalizedTotals = new ArrayList();
					 finalizedTotals =  adjCrtnDAO.getAdjCrtnFinalizedTotalsforPc(pensionno,adjobyear);  
					 return finalizedTotals;
				}	
					public ArrayList generateform4report(String pensionno,String empName,String remmonth,String emoluemnts,String diffpc,String diffaddcontri,String remarks,String id,String adjyear,String totalpc,String totalinterstpc) {
						ArrayList list=new ArrayList();
						list=adjCrtnDAO.generateform4report(pensionno,empName,remmonth,emoluemnts,diffpc,diffaddcontri,remarks,id,adjyear,totalpc,totalinterstpc);
						return list;
					}	
					public ArrayList getStationList(String region,String monthyear,String station){
						ArrayList stationList = null;
						stationList = adjCrtnDAO.getStationList(region,monthyear,station);
						return stationList;
					}
					public ArrayList generateform4report2(String id,String pensionNo) {
						ArrayList list=new ArrayList();
						list=adjCrtnDAO.generateform4report2(id,pensionNo);
						return list;
					}
					public ArrayList BifurcationReport(String pensionno) {
						ArrayList list=new ArrayList();
						list=adjCrtnDAO.BifurcationReport(pensionno);
						return list;
					}
}
