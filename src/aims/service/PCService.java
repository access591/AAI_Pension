package aims.service;

import java.util.ArrayList;

import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.FinancialReportDAO;
import aims.dao.RevisionPCDAO;

public class PCService {
	Log log = new Log(FinancialReportService.class);
	FinancialReportDAO finReportDAO=new FinancialReportDAO();
	RevisionPCDAO rpcADO=new RevisionPCDAO();
	CommonUtil commonUtil=new CommonUtil();
	public ArrayList getRevisionPCReport(String finToYear,String finYear,String region,String airportcode,String selectedMonth,String empserialNO,String cpfAccno,String transferFlag,String mappingFlag,String pfIDStrip,String bulkPrint,String edit){
        String fromYear="",toYear="",tempToYear="",finMonth="",selectedToYear="";
        boolean recoverieTableCheck=false;
        String[] finMnthYearList=new String[5];
        boolean monthFlag=false;
        ArrayList PensionContributionList=new ArrayList();
        finMnthYearList=finYear.split(",");
        System.out.println(finMnthYearList);
        System.out.println(toYear);
        if(!finToYear.equals("NO-SELECT")){
        	selectedToYear=finToYear;
        }else{
        	selectedToYear="2008";
        }
        
        selectedToYear="2016";
        finYear="1995";
        if(!selectedMonth.equals("NO-SELECT")){
            finMonth=selectedMonth;
            monthFlag=true;
        }
        finMonth="03";
          if(!finYear.equals("NO-SELECT") && monthFlag==false){            
            log.info("From Year"+finYear+"To Year"+selectedToYear+"finMonth"+finMonth);
            fromYear="01-Apr-"+finYear;
            try {
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("fromYear"+fromYear);
                if(Integer.parseInt(selectedToYear.trim())>=95 && Integer.parseInt(selectedToYear.trim())<=99 ){
                    toYear="01-"+finMonth+"-"+selectedToYear;
                }else{
                    toYear="01-"+finMonth+"-"+selectedToYear;
                }
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
        }else if(!finYear.equals("NO-SELECT") && monthFlag==true){
            try {
            if(Integer.parseInt(finMonth)>=4 && Integer.parseInt(finMonth)<=12){
                fromYear="01-Apr-"+finYear;
                toYear="01-"+finMonth+"-"+selectedToYear;
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MMM-yyyy","dd-MMM-yyyy");
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("check condition1"+toYear);
            }else if(Integer.parseInt(finMonth)>=1 && Integer.parseInt(finMonth)<=3){
                if(Integer.parseInt(selectedToYear.trim())>=95 && Integer.parseInt(selectedToYear.trim())<=99 ){
                	  fromYear="01-Apr-"+finYear;
                	  toYear="01-"+finMonth+"-"+selectedToYear;
                }else{
                	  fromYear="01-Apr-"+finYear;
                	  toYear="01-"+finMonth+"-"+selectedToYear;
                }
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MMM-yyyy","dd-MMM-yyyy");
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("check condition2"+toYear);
            }
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
         
        }else{
            fromYear="01-Apr-1995";
            tempToYear=commonUtil.getCurrentDate("yyyy");
            toYear="01-Mar-2008";
          //  toYear="01-May-2008";
        }
        
        log.info("fromYear====="+fromYear+"toYear======"+toYear+"mappingFlag"+mappingFlag);
        // newline  code for check the recovery table
         
         /* if(edit.equals("true")){
        	  recoverieTableCheck=true;  
          }*/
       /* if(mappingFlag.trim().equals("true")){
        	 log.info("pensionContributionReport "+toYear+"mappingFlag"+mappingFlag);
        	PensionContributionList=finReportDAO.pensionContributionReport(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag, mappingFlag,recoverieTableCheck);
        }else{*/
       	 log.info("pensionContributionReportAll mappingFlag"+mappingFlag);
     	PensionContributionList=rpcADO.pensionContributionReportAll(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag,pfIDStrip,bulkPrint,edit);
    // }
		
		return PensionContributionList;
		
		
	}
	public ArrayList getRevisionPCReportSecLvl(String finToYear,String finYear,String region,String airportcode,String selectedMonth,String empserialNO,String cpfAccno,String transferFlag,String mappingFlag,String pfIDStrip,String bulkPrint,String edit){
        String fromYear="",toYear="",tempToYear="",finMonth="",selectedToYear="";
        boolean recoverieTableCheck=false;
        String[] finMnthYearList=new String[5];
        boolean monthFlag=false;
        ArrayList PensionContributionList=new ArrayList();
        finMnthYearList=finYear.split(",");
        System.out.println(finMnthYearList);
        System.out.println(toYear);
        if(!finToYear.equals("NO-SELECT")){
        	selectedToYear=finToYear;
        }else{
        	selectedToYear="2008";
        }
        
        selectedToYear="2015";
        finYear="1995";
        if(!selectedMonth.equals("NO-SELECT")){
            finMonth=selectedMonth;
            monthFlag=true;
        }
        finMonth="03";
          if(!finYear.equals("NO-SELECT") && monthFlag==false){            
            log.info("From Year"+finYear+"To Year"+selectedToYear+"finMonth"+finMonth);
            fromYear="01-Apr-"+finYear;
            try {
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("fromYear"+fromYear);
                if(Integer.parseInt(selectedToYear.trim())>=95 && Integer.parseInt(selectedToYear.trim())<=99 ){
                    toYear="01-"+finMonth+"-"+selectedToYear;
                }else{
                    toYear="01-"+finMonth+"-"+selectedToYear;
                }
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
        }else if(!finYear.equals("NO-SELECT") && monthFlag==true){
            try {
            if(Integer.parseInt(finMonth)>=4 && Integer.parseInt(finMonth)<=12){
                fromYear="01-Apr-"+finYear;
                toYear="01-"+finMonth+"-"+selectedToYear;
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MMM-yyyy","dd-MMM-yyyy");
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("check condition1"+toYear);
            }else if(Integer.parseInt(finMonth)>=1 && Integer.parseInt(finMonth)<=3){
                if(Integer.parseInt(selectedToYear.trim())>=95 && Integer.parseInt(selectedToYear.trim())<=99 ){
                	  fromYear="01-Apr-"+finYear;
                	  toYear="01-"+finMonth+"-"+selectedToYear;
                }else{
                	  fromYear="01-Apr-"+finYear;
                	  toYear="01-"+finMonth+"-"+selectedToYear;
                }
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MMM-yyyy","dd-MMM-yyyy");
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("check condition2"+toYear);
            }
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
         
        }else{
            fromYear="01-Apr-1995";
            tempToYear=commonUtil.getCurrentDate("yyyy");
            toYear="01-Mar-2008";
          //  toYear="01-May-2008";
        }
        
        log.info("fromYear====="+fromYear+"toYear======"+toYear+"mappingFlag"+mappingFlag);
        // newline  code for check the recovery table
         
         /* if(edit.equals("true")){
        	  recoverieTableCheck=true;  
          }*/
       /* if(mappingFlag.trim().equals("true")){
        	 log.info("pensionContributionReport "+toYear+"mappingFlag"+mappingFlag);
        	PensionContributionList=finReportDAO.pensionContributionReport(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag, mappingFlag,recoverieTableCheck);
        }else{*/
       	 log.info("pensionContributionReportAll mappingFlag"+mappingFlag);
     	PensionContributionList=rpcADO.pensionContributionReportAllSecLvl(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag,pfIDStrip,bulkPrint,edit);
    // }
		
		return PensionContributionList;
		
		
	}
	public double getInterestforNoofMonths(String interestCalcUpto){
		return finReportDAO.getInterestforNoofMonths(interestCalcUpto);
	}
	public String reIntrestDate(String empserialNO){
		return finReportDAO.reIntrestDate(empserialNO);
	}
	public double interestforfinalsettleMonths(String reinterestcalcdate,String empserialNO){
		return finReportDAO.interestforfinalsettleMonths(reinterestcalcdate,empserialNO);
	}
	public String finalintrestdate(String interestCalcUpto,String empserialNO){
		return finReportDAO.finalintrestdate(interestCalcUpto,empserialNO);
	}
	public String reSettlementdate(String empserialNO){
		return finReportDAO.reSettlementdate(empserialNO);
	}
	public ArrayList  searchForRevisionOptionPC(String  userRegion,String  userStation,String  profileType ,String  accountType,String  employeeNo){
		 ArrayList employeeList = new ArrayList();
		 employeeList =  rpcADO.searchForRevisionOptionPC(userRegion,userStation,profileType,  accountType,employeeNo); 
		
		 return employeeList;
	}
	public ArrayList  searchForRevisionOptionPCSecLvl(String  userRegion,String  userStation,String  profileType ,String  accountType,String  employeeNo){
		 ArrayList employeeList = new ArrayList();
		 employeeList =  rpcADO.searchForRevisionOptionPCSecLvl(userRegion,userStation,profileType,  accountType,employeeNo); 
		
		 return employeeList;
	}
	
	public int getApprove(String pfId)  {
		int result = 0;
		result =  rpcADO.getApprove(pfId);
		return result;

	}
	public int getApproveSec(String pfId)  {
		int result = 0;
		result =  rpcADO.getApproveSec(pfId);
		return result;

	}
	public int insertEmployeeTransData(String pfId, String frmName,
			String username, String ipaddress, String flag, String mFlag,
			String cpfacno, String region, String upflag) throws Exception {
		int result = 0;
		result =  rpcADO.insertEmployeeTransData(pfId, frmName, username,
				ipaddress, flag, mFlag, cpfacno, region, upflag);
		return result;

	}
	public int insertEmployeeTransDataSecLvl(String pfId, String frmName,
			String username, String ipaddress, String flag, String mFlag,
			String cpfacno, String region, String upflag) throws Exception {
		int result = 0;
		result =  rpcADO.insertEmployeeTransDataSecLvl(pfId, frmName, username,
				ipaddress, flag, mFlag, cpfacno, region, upflag);
		return result;

	}
	  public ArrayList editTransactionDataForAdjCrtn(String cpfAccno,String monthyear,String emoluments,String epf,String vpf,String principle,String interest,String advance,String loan,String aailoan,String contri,String noofmonths,String pfid,String region,String airportcode,String username,String computername,String from7narration,String duputationflag,String pensionoption,
	    		String empnetob,String aainetob,String empnetobFlag,String finYear,String editTransFlag,String dateOfBirth){
	        ArrayList adjEmolList = new ArrayList();
	        adjEmolList = rpcADO.editTransactionDataForAdjCrtn(cpfAccno,monthyear,emoluments,epf,vpf,principle,interest,advance,loan,aailoan,contri,noofmonths,pfid,region,airportcode,username,computername,from7narration,duputationflag,pensionoption,empnetob,aainetob,empnetobFlag,finYear,editTransFlag,dateOfBirth);
	        return adjEmolList;
}
	  public ArrayList editTransactionDataForAdjCrtnSecLvl(String cpfAccno,String monthyear,String emoluments,String epf,String vpf,String principle,String interest,String advance,String loan,String aailoan,String contri,String noofmonths,String pfid,String region,String airportcode,String username,String computername,String from7narration,String duputationflag,String pensionoption,
	    		String empnetob,String aainetob,String empnetobFlag,String finYear,String editTransFlag,String dateOfBirth){
	        ArrayList adjEmolList = new ArrayList();
	        adjEmolList = rpcADO.editTransactionDataForAdjCrtnSecLvl(cpfAccno,monthyear,emoluments,epf,vpf,principle,interest,advance,loan,aailoan,contri,noofmonths,pfid,region,airportcode,username,computername,from7narration,duputationflag,pensionoption,empnetob,aainetob,empnetobFlag,finYear,editTransFlag,dateOfBirth);
	        return adjEmolList;
}
	 	public ArrayList allYearsForm7PrintOutForPFID(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName,String formType,String formRevisedFlag,String adjFlag,String frmName,String pcFlag){
	        ArrayList form8List=new ArrayList();
	        form8List=rpcADO.getAllYearsForm7PrintOut(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName,formType,formRevisedFlag,adjFlag,frmName,pcFlag);
	        return form8List;
	    }
	 	public String getMinMaxYearsForArrearBreakup(String pensionno){
			return rpcADO.getMinMaxYearsForArrearBreakup(pensionno);
		}
}