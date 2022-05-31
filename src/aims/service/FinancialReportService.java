 /**
  * File       : FinancialReportService.java
  * Date       : 08/07/2007
  * Author     : AIMS 
  * Description: 
  * Copyright (2007) by the Navayuga Infotech, all rights reserved.
  */
package aims.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Set;

import aims.bean.ActiveInactivBean;
import aims.bean.Form7MultipleYearBean;
import aims.bean.Form8Bean;
import aims.bean.Form8RemittanceBean;
import aims.bean.PensionBean;
import aims.bean.TrustPCRegionBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.dao.FinancialReportDAO;

/* 
##########################################
#Date					Developed by			Issue description
#23-Feb-2012			Radha					For Seperating methods related to AdjCalc Screen
#14-Feb-2012			Prasad					Implementaing Mapping in Adj Cal Screen
#04-Feb-2012            Prasanthi               Stage wise segrigartion of Adj Cal Screen
#27-Jan-2012            Prasanthi               Changes in editsettlementinfo reg to insert old settlement date information
#13-Jan-2012            Prasanthi               Changes in editsettlementinfo regarding the dateofseperation
#13-Dec-2011        	Suresh Kumar		    Regarding final settlment verification add compareFinalsettlment method
#07-Dec-2011        	Prasanthi			    Method Added for Edit SettlementDate (editSettlementInfo)
#29-Dec-2011        	Prasanthi			    adding the remarks for Edit SettlementDate (editSettlementInfo)
#########################################
*/
public class FinancialReportService {
	Log log = new Log(FinancialReportService.class);
	FinancialReportDAO finReportDAO=new FinancialReportDAO();
	CommonUtil commonUtil=new CommonUtil();
	
	public ArrayList getPensionContributionReport(String finToYear,String finYear,String region,String airportcode,String selectedMonth,String empserialNO,String cpfAccno,String transferFlag,String mappingFlag,String pfIDStrip,String bulkPrint,String recoverieTable){
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
        if(!selectedMonth.equals("NO-SELECT")){
            finMonth=selectedMonth;
            monthFlag=true;
        }
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
         
          if(recoverieTable.equals("true")){
        	  recoverieTableCheck=true;  
          }
        if(mappingFlag.trim().equals("true")){
        	 log.info("pensionContributionReport "+toYear+"mappingFlag"+mappingFlag);
        	PensionContributionList=finReportDAO.pensionContributionReport(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag, mappingFlag,recoverieTableCheck);
        }else{
       	 log.info("pensionContributionReportAll mappingFlag"+mappingFlag);
     	PensionContributionList=finReportDAO.pensionContributionReportAll(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag,pfIDStrip,bulkPrint);
     }
		
		return PensionContributionList;
		
		
	}
	public ArrayList getFinanceYearList(){
		ArrayList financeYearLst=new ArrayList();
		financeYearLst=finReportDAO.financeYearList();
		return financeYearLst;
	}
	 public ArrayList  duplicateBlockedorFrozen(String fromDate,String toDate){
		 ArrayList duplicateBlockedorFrozenList = new ArrayList();
		 duplicateBlockedorFrozenList =  finReportDAO.duplicateBlockedorFrozen(fromDate,toDate);
		 return duplicateBlockedorFrozenList;
	 	}
    public void deleteTransactionData(String cpfAccno,String monthyear,String region,String airportcode,String ComputerName,String Username,String pfid){
        finReportDAO.deleteTransactionData(cpfAccno,monthyear,region,airportcode,ComputerName,Username,pfid);
    }
    public void editTransactionData(String cpfAccno,String monthyear,String emoluments,String vpf,String principle,String interest,String contri,String advance,String loan,String aailoan,String pfid,String region,String airportcode,String username,String computername,String from7narration,String pcheldamt,String noofmonths,String arrearflag,String duputationflag,String pensionoption,String recoverieTable,String epf){
        finReportDAO.editTransactionData(cpfAccno,monthyear,emoluments,vpf,principle,interest,contri,advance,loan,aailoan,pfid,region,airportcode,username,computername,from7narration,pcheldamt,noofmonths,arrearflag,duputationflag,pensionoption,recoverieTable,epf);
    }
     
    public ArrayList pfCardReport(String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo){
        ArrayList pfCardLst=new ArrayList();
       pfCardLst=finReportDAO.employeePFCardReport(region,selectedYear,flag,employeeName,sortedColumn,pensionNo);
       return pfCardLst;
    }
    public ArrayList pfCardEdit(String region,String selectedYear,String pensionNo){
        ArrayList pfCardLst=new ArrayList();
       pfCardLst=finReportDAO.employeePFCardEdit(region,selectedYear,pensionNo);
       return pfCardLst;
    }
    public ArrayList updateMonthlyCpfRecoverieData(String pfid,String monthYear,
			String emoluments,String  cpf,String vpf,String principle,String interest,String wetherOption){
        ArrayList pfCardLst=new ArrayList();
        
      // pfCardLst=finReportDAO.employeePFCardEdit(region,selectedYear,pensionNo);
       finReportDAO.updateMonthlyCpfRecoverieData(pfid,monthYear,emoluments,cpf,vpf,principle,interest,wetherOption);
       return pfCardLst;
    }
    

    public ArrayList calClosingOB(double interest,ArrayList obList,double totalAAINet,double totalAaiIntNet,double totalEmpNet,double totalEmpIntNet,double totalPensionContr,double penInterest,double advancePFWPaid, double principle,int noOfMonths,boolean chkRestrionflag,boolean chkadjobintflag){
        System.out.println("calClosingOB-service");
    	ArrayList calClosingOBList=new ArrayList();
        calClosingOBList=finReportDAO.calOBPFCard(interest, obList, totalAAINet, totalAaiIntNet, totalEmpNet, totalEmpIntNet, totalPensionContr, penInterest,advancePFWPaid,principle,noOfMonths,chkRestrionflag,chkadjobintflag);
        return calClosingOBList;
    }
   
   /* public ArrayList form8Report(String selectedYear,String sortedColumn,String region,String airportCode,String pensionno){
        ArrayList form8List=new ArrayList();
        form8List=finReportDAO.rnfcForm8Report(selectedYear,sortedColumn,region,true,airportCode,pensionno);
        return form8List;
    }*/
    public ArrayList form7Report(String selectedYear,String sortedColumn,String region,String airportCode,String pensionno){
        ArrayList form8List=new ArrayList();
        form8List=finReportDAO.rnfcForm8Report(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno);
        return form8List;
    }
  /*  public ArrayList form7PrintingReport(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno){
        ArrayList form8List=new ArrayList();
        form8List=finReportDAO.rnfcForm7Report(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range);
        return form8List;
    }*/
    public ArrayList form7PrintingReport(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName,String formType,String formRevisedFlag,String adjFlag,String frmName,String pcFlag){
        ArrayList form8List=new ArrayList();
     
        	form8List=finReportDAO.rnfcForm7Report(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName,formType,formRevisedFlag,adjFlag, frmName, pcFlag);
     
        
        return form8List;
    }
    public ArrayList form8Report(String selectedYear,String month,String sortedColumn,String region,String airportCode,String pensionno,String formType,String adjFlag,String frmName ){
    	String fromYear = "", toYear = "",dateOfRetriment="",mappingAirportRegion="",frmMonth="";
        int toSelectYear = 0;
        ArrayList regionList=new ArrayList();
        ArrayList form8List=new ArrayList();
       
       
        
//      form8List=finReportDAO.rnfcForm8Report(selectedYear,month,sortedColumn,region,true,airportCode,pensionno);
       if(!month.equals("NO-SELECT")){
      	 try {
      		 frmMonth=commonUtil.converDBToAppFormat(month,"MM","MMM");
  		} catch (InvalidDataException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
       }
        if(!selectedYear.equals("Select One") && month.equals("NO-SELECT")){
        	   if(!selectedYear.equals("1995")){
        		   fromYear = "01-Apr-" + selectedYear;
        	   }else{
        		   fromYear = "01-May-" + selectedYear;
        	   }
        	  
               toSelectYear = Integer.parseInt(selectedYear) + 1;
               toYear = "01-Mar-" + toSelectYear;
        }else if(!selectedYear.equals("Select One") && !month.equals("NO-SELECT")){
        	fromYear = "01-"+frmMonth+"-" + selectedYear;
       	 	toYear=fromYear;
        }else{
        	
        	   fromYear = "01-Apr-1995";
               toYear = "01-Mar-2008" ;
        }
        if(region.equals("CHQIAD")){
        	if (!airportCode.equals("NO-SELECT")) {
        		mappingAirportRegion="IAD-"+airportCode;
        		region="";
        		region=mappingAirportRegion;
        	}
        }
        if (region.equals("NO-SELECT")) {   
        	regionList = commonUtil.getForm6Region();
        } else {
            regionList.add(region);
        }
        ArrayList list=new ArrayList();
        ArrayList form8Datalist=new ArrayList();
        Form8Bean form8Bean=new Form8Bean();
        Form8RemittanceBean remittanceInfo=new Form8RemittanceBean();
/*        if(Integer.parseInt(selectedYear)>=2008){
        	region=region;
          	form8Bean=new Form8Bean();
          	remittanceInfo=new Form8RemittanceBean();
          	form8List=finReportDAO.getRnfcForm8PSInfo(fromYear,toYear,region,pensionno,false,false,formType);
            log.info(region+"==="+form8List.size());
           	remittanceInfo=finReportDAO.getForm8RemittanceInfo(fromYear,toYear,region,pensionno,false,false,formType);
           	
            form8Bean.setForm8List(form8List);
          	form8Bean.setRemittanceBean(remittanceInfo);
          	list.add(form8Bean);
          	form8Datalist=finReportDAO.getRnfcForm68PS(fromYear,toYear,region,pensionno,"Form-8",fromYear);
          	 form8Bean.setForm8List((ArrayList)form8Datalist.get(0));
           	form8Bean.setRemittanceBean((Form8RemittanceBean)form8Datalist.get(1));
           	list.add(form8Bean);
        }else{*/

          	form8Datalist=finReportDAO.getRnfcForm68PS(fromYear,toYear,region,pensionno,"Form-8",fromYear,sortedColumn,adjFlag,frmName );
          	 form8Bean.setForm8List((ArrayList)form8Datalist.get(0));
           	form8Bean.setRemittanceBean((Form8RemittanceBean)form8Datalist.get(1));
           	list.add(form8Bean);
       /* }*/
      
 
        return list;
    }
    public Form8RemittanceBean remittanceform6Report(String selectedYear,String sortedColumn,String region,String pensionno){
    	String fromYear = "", toYear = "";
    	String[] dateLst=selectedYear.split(",");
    	fromYear=dateLst[0];
    	toYear=dateLst[1];
       
    	log.info("fromYear"+fromYear+"toYear"+toYear);
    	Form8RemittanceBean remittanceInfo=new Form8RemittanceBean();
    	remittanceInfo=finReportDAO.getForm8RemittanceInfo(fromYear,toYear,region,pensionno,false,false,"FORM-8-PS");
        return remittanceInfo;
    }
    
    public Form8RemittanceBean remittanceReport(String selectedYear,String sortedColumn,String region,String pensionno){
    	String fromYear = "", toYear = "";
        int toSelectYear = 0;
      
        fromYear = "01-Apr-" + selectedYear;
        toSelectYear = Integer.parseInt(selectedYear) + 1;
        toYear = "01-Mar-" + toSelectYear;
    	Form8RemittanceBean remittanceInfo=new Form8RemittanceBean();
    	remittanceInfo=finReportDAO.getForm8RemittanceInfo(fromYear,toYear,region,pensionno,false,false,"FORM-8-PS");
        return remittanceInfo;
    }
    public Form8RemittanceBean remittanceform6Report(String selectedYear,String sortedColumn,String region,String pensionno ,boolean formFlag,boolean sndFormFlag){
    	String fromYear = "", toYear = "";
    	String[] dateLst=selectedYear.split(",");
    	fromYear=dateLst[0];
    	toYear=dateLst[1];
    	CommonDAO commonDAO =new CommonDAO();
    	if(fromYear.equals(toYear)){
    		toYear=commonDAO.getLastDayOfMonth(toYear);
    	}
    	Form8RemittanceBean remittanceInfo=new Form8RemittanceBean();
    	remittanceInfo=finReportDAO.getForm6RemittanceInfo(fromYear,toYear,region,pensionno,formFlag,sndFormFlag);
        return remittanceInfo;
    }
    public int updatePCReport(String finYear,String region,String airportcode,String selectedMonth,String empserialNO,String cpfAccno,String transferFlag,String mappingFlag,String pfIDStrip,String chkMappingFlag){
        String fromYear="",toYear="",tempToYear="",finMonth="";
        String[] finMnthYearList=new String[5];
        boolean monthFlag=false;
        int totalRc=0;
        ArrayList PensionContributionList=new ArrayList();
        finMnthYearList=finYear.split(",");
        if(finMnthYearList.length>1){
            finMonth=finMnthYearList[0];
            finYear=finMnthYearList[1];
        }
        if(!selectedMonth.equals("NO-SELECT")){
            finMonth=selectedMonth;
            monthFlag=true;
        }
        String[] finYearList;
        finYearList=finYear.split("-");
        if(!finYear.equals("NO-SELECT") && monthFlag==false){
            
            log.info("From Year"+finYearList[0]+"To Year"+finYearList[1]+"finMonth"+finMonth);
            fromYear="01-04-"+finYearList[0];
            try {
                fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MM-yyyy","dd-MMM-yyyy");
                log.info("fromYear"+fromYear);
                if(Integer.parseInt(finYearList[1].trim())>=95 && Integer.parseInt(finYearList[1].trim())<=99 ){
                    toYear="01-"+finMonth+"-"+finYearList[1];
                }else{
                    toYear="01-"+finMonth+"-"+finYearList[1];
                }
                toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
        }else if(!finYear.equals("NO-SELECT") && monthFlag==true){
            try {
            	 fromYear="01-04-"+finYearList[0];	
            if(Integer.parseInt(finMonth)>=4 && Integer.parseInt(finMonth)<=12){
            	toYear="01-"+finMonth+"-"+finYearList[1];
              
            }else if(Integer.parseInt(finMonth)>=1 && Integer.parseInt(finMonth)<=3){
                if(Integer.parseInt(finYearList[1].trim())>=95 && Integer.parseInt(finYearList[1].trim())<=99 ){
                	toYear="01-"+finMonth+"-"+finYearList[1];
                }else{
                	toYear="01-"+finMonth+"-"+finYearList[1];
                }
               
            }
            fromYear=commonUtil.converDBToAppFormat(fromYear,"dd-MM-yyyy","dd-MMM-yyyy");
            toYear=commonUtil.converDBToAppFormat(toYear,"dd-MM-yyyy","dd-MMM-yyyy");
            } catch (InvalidDataException e) {
                log.printStackTrace(e);
            }
       
            //toYear=fromYear;
        }else{
            fromYear="01-Apr-1995";
            tempToYear=commonUtil.getCurrentDate("yyyy");
            toYear="01-Apr-"+tempToYear;
            toYear="01-Mar-2008";
        }
        
        log.info("fromYear====="+fromYear+"toYear======"+toYear+"transferFlag"+transferFlag);
        totalRc=finReportDAO.updatePCReport(fromYear,toYear,region,airportcode,empserialNO,cpfAccno,transferFlag,pfIDStrip,chkMappingFlag,"N");
        return totalRc;
		
		
	}
    public int updateOBCardReport(String range,String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo,String chkPendingFlag,String statioName){
        int obTotalInrted=0;
        String uploadFilePath="",fileName="";
        
        FileWriter fw = null;
        ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.DbProperties");
        uploadFilePath = bundle.getString("upload.folder.path");
        File filePath = new File(uploadFilePath);
        if (!filePath.exists()) {
     	   	filePath.mkdirs();
        }
        fileName="//PCOB-"+region+"-"+range+".txt";
        try{
        	 fw = new FileWriter(new File(filePath + fileName));
             obTotalInrted=finReportDAO.updatePFCardReport(range,region,selectedYear,flag,employeeName,sortedColumn,pensionNo,fw,chkPendingFlag,true,statioName);
        }catch (IOException e) {
     		// TODO Auto-generated catch block
     		log.printStackTrace(e);
     	}
       
  
        return obTotalInrted;
    }
    public ArrayList financeForm3ByPFID(String monthyear,String region,String airportcode,String formTypeFlag,String sortingOrder){
    	ArrayList list=new ArrayList();
    	try {
			list=finReportDAO.financeForm3ByPFID(monthyear,region,airportcode,formTypeFlag,sortingOrder);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }
    
    public ArrayList form7ZeroPrintingReport(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName,String adjFlag){
        ArrayList form8List=new ArrayList();
        form8List=finReportDAO.rnfcForm7ZerosReport(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName,adjFlag);
        return form8List;
    }
    public ArrayList personalReportByPFID(String monthyear,String region,String airportcode,String SortingOrder,String frmSeperationReason,boolean reasonFlag){
    	ArrayList list=new ArrayList();
    	try {
			list=finReportDAO.financePersonalByPFID(monthyear,region,airportcode,SortingOrder,frmSeperationReason,reasonFlag);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }
    public ArrayList pfCardReportPrinting(String range,String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo,String lastmonthFlag,String lastmonthYear,String stationName,String bulkFlag){
        ArrayList pfCardLst=new ArrayList();
        
       pfCardLst=finReportDAO.empPFCardReportPrint(range,region,selectedYear,flag,employeeName,sortedColumn,pensionNo,lastmonthFlag,lastmonthYear,stationName,bulkFlag);
       //finReportDAO. updatePFCardReport(region,selectedYear,flag,employeeName,sortedColumn,pensionNo);
        return pfCardLst;
    }
    public int updatePCTrans(String range,String region,String selectedYear,String chkPendingFlag){
        String infoOnUpdtedRec="",uploadFilePath="",fileName="";
        int cntr=0;
        FileWriter fw = null;
        ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.DbProperties");
        uploadFilePath = bundle.getString("upload.folder.path");
        File filePath = new File(uploadFilePath);
        if (!filePath.exists()) {
     	   	filePath.mkdirs();
        }
        try {
        if(chkPendingFlag.equals("true")){
        	fileName="//PCInformation-Pending"+region+"-"+range+".txt";
        }else{
        	fileName="//PCInformation"+region+"-"+range+".txt";
        }
     	
 		fw = new FileWriter(new File(filePath + fileName));
 		infoOnUpdtedRec=finReportDAO.updatePCTrans(range,region,selectedYear,fw,chkPendingFlag);
 	     
 	} catch (IOException e) {
 		// TODO Auto-generated catch block
 		log.printStackTrace(e);
 	}
     
        return cntr;
     }
    public ArrayList loadgetTrustPCReport(String range,String division,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		ArrayList regionList=new ArrayList();
		String regionDec="";
		HashMap regionHashmap = new HashMap();
		HashMap sortedRegionMap = new LinkedHashMap();

		Iterator regionIterator = null;
		regionHashmap = commonUtil.getRegion();

		Set keys = regionHashmap.keySet();
		regionIterator = keys.iterator();
		String rgn = "";
		TrustPCRegionBean trustRegionBean=null;
		ArrayList trustList=new ArrayList();
		if(region.equals("NO-SELECT")){
			while (regionIterator.hasNext()) {
				regionDec=regionHashmap.get(regionIterator.next()).toString();
				trustRegionBean=new TrustPCRegionBean();
				form3List=finReportDAO.getTrustPCReport(range, division,regionDec, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
				//log.info("loadgetTrustPCReport::regionDec"+regionDec+"Size"+form3List.size());
				trustRegionBean.setRegionList(form3List);
				trustRegionBean.setRegionName(regionDec);
				form3List=new ArrayList();
				trustList.add(trustRegionBean);
			}
		}else{
			trustRegionBean=new TrustPCRegionBean();
			form3List=finReportDAO.getTrustPCReport(range, division,region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
			trustRegionBean.setRegionList(form3List);
			trustRegionBean.setRegionName(regionDec);
			trustList.add(trustRegionBean);
		}
		
		
		return trustList;
	}
    public ArrayList form7PrintingIndexReport(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName){
        ArrayList form7IndexList=new ArrayList();
        form7IndexList=finReportDAO.rnfcForm7IndexReport(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName);
        return form7IndexList;
    }
    
    public int preProcessAdjOB(String pfid){
    	int updateStatus=0;
    	updateStatus=finReportDAO.preProcessAdjOB(pfid);
    	return updateStatus;
    }
    public ArrayList summaryPCReport(String fromDate,String toDate,String sortedColumn,String region,String airportCode,String empFlag,String empName,String pensionno){
        ArrayList formPCList=new ArrayList();
  
       	formPCList=finReportDAO.getSummaryPCReport(fromDate,toDate,region,airportCode,sortedColumn,empFlag,empName,pensionno);
        return formPCList;
    }
    
    public ArrayList allYearsForm7PrintOutForPFID(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName,String formType,String formRevisedFlag,String adjFlag,String frmName,String pcFlag){
        ArrayList form8List=new ArrayList();
        form8List=finReportDAO.getAllYearsForm7PrintOut(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName,formType,formRevisedFlag,adjFlag,frmName,pcFlag);
        return form8List;
    }
    public ArrayList getAllYearsForm8PrintOut(String selectedYear, String month,
			String sortedColumn, String region,
			String airportCode, String pensionNo,String formType ) {
		ArrayList totalYearForm8List=new ArrayList();
		ArrayList form8List=new ArrayList();
		int currentYear=Integer.parseInt(commonUtil.getCurrentDate("yyyy"));
		int fromOldYear=1995;
		int totalSpan=currentYear-fromOldYear;
		String message="",adjFlag="false",frmName="";
		int messageFromYear=0,messageToYear=0;
		Form7MultipleYearBean multipleYearBean=null;
		if(selectedYear.equals("Select One")){
			for(int i=0;i<totalSpan;i++){
				 multipleYearBean=new Form7MultipleYearBean();
				form8List=this.form8Report(Integer.toString(fromOldYear+i), month,
						sortedColumn, region,
						 airportCode, pensionNo, formType,adjFlag,frmName);
				if(form8List.size()!=0){
					messageFromYear=fromOldYear+i;
					messageToYear=messageFromYear+1;
					message="Mar-" + messageFromYear + "  To Feb-" + messageToYear;
					multipleYearBean.setEachYearList(form8List);
					multipleYearBean.setMessage(message);
					totalYearForm8List.add(multipleYearBean);
				}
			
			}
		}
		return totalYearForm8List;
	}
    public void editFinalDate(String finalsettlementDate,String pfid,String userId){
        finReportDAO.editFinalDate(finalsettlementDate,pfid,userId);
    }
    public void editInterestCalc(String interestcalcDate,String pfid,String userId,String recoverieTable){
        finReportDAO.editInterestCalcDate(interestcalcDate,pfid,userId,recoverieTable);
    }
    public void editSettlementInfo(String pfid,String seperationdate,String intrestcalcfinaldate,String interestcalcDate,String resettlementdate ,String reintrestcalcdate,String userId,String recoverieTable,String clientname,String remarks,String seperationreason,String oldintrestcacfinaldate,String oldresettlementdate,String oldintrestcalcdate){
    	log.info("FinanceReportService:editSettlementInfo:---Entering method");
    	finReportDAO.editSettlementInfo(pfid,seperationdate, intrestcalcfinaldate, interestcalcDate, resettlementdate ,reintrestcalcdate,userId,recoverieTable,clientname,remarks,seperationreason,oldintrestcacfinaldate,oldresettlementdate,oldintrestcalcdate);
        log.info("FinanceReportService:editSettlementInfo:---Leaving method");
    }
    public void editReInterestCalc(String interestcalcDate,String pfid,String userId,String recoverieTable){
        finReportDAO.editReInterestCalc(interestcalcDate,pfid,userId,recoverieTable);
    }
    public int updatePFCardProcessing(String range,String region,String selectedYear,String flag,String employeeName,String sortedColumn,String pensionNo,String chkPendingFlag,String statioName){
        int obTotalInrted=0;
        String uploadFilePath="",fileName="";
        
        FileWriter fw = null;
        ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.DbProperties");
        uploadFilePath = bundle.getString("upload.folder.path");
        File filePath = new File(uploadFilePath);
        if (!filePath.exists()) {
     	   	filePath.mkdirs();
        }
        fileName="//PFCard-"+commonUtil.getCurrentDate("ddMMyyyyhhmmss")+".txt";
        try{
        	 fw = new FileWriter(new File(filePath + fileName));
            finReportDAO.pfCardProcessing(range,region,selectedYear,flag,employeeName,sortedColumn,pensionNo,fw,statioName);
        }catch (IOException e) {
     		// TODO Auto-generated catch block
     		log.printStackTrace(e);
     	}
       
  
        return obTotalInrted;
    }
  
    public ArrayList getStatementWagesPCReport(String range,String region,String month,String selectedYear,String sortedColumn,String pensionNo,String stationName){
        ArrayList pfCardLst=new ArrayList();
        
       pfCardLst=finReportDAO.getStatementOfWagePension(range,region,selectedYear,month,sortedColumn,pensionNo,stationName);

        return pfCardLst;
    }
    public ArrayList TransferInOutPrinting(String range,String region,String frmSelectedDts,String flag,String employeeName,String sortedColumn,String pensionNo,String lastmonthFlag,String lastmonthYear,String stationName){
        ArrayList empinfo=new ArrayList();
        
        empinfo=finReportDAO.TransferInOutPrinting(range,region,frmSelectedDts,flag,employeeName,sortedColumn,pensionNo,lastmonthFlag,lastmonthYear,stationName);
       //finReportDAO. updatePFCardReport(region,selectedYear,flag,employeeName,sortedColumn,pensionNo);
        return empinfo;
    }
    public ArrayList remittanceform68Report(String selectedYear,String sortedColumn,String region,String pensionno ,boolean formFlag,boolean sndFormFlag,String formType){
    	String fromYear = "", toYear = "";
    	ArrayList list=new ArrayList();
    	ArrayList datalist=new ArrayList();
    	String[] dateLst=selectedYear.split(",");
    	fromYear=dateLst[0];
    	toYear=dateLst[1];
    	CommonDAO commonDAO =new CommonDAO();
    	if(fromYear.equals(toYear)){
    		toYear=commonDAO.getLastDayOfMonth(toYear);
    	}
    	Form8RemittanceBean remittanceInfo=new Form8RemittanceBean();
    	//remittanceInfo=finReportDAO.getForm6RemittanceInfo(fromYear,toYear,region,pensionno,formFlag,sndFormFlag);
    /*	list.add(datalist);
    	list.add(remittanceInfo);*/
    	String dates=finReportDAO.getForm6CompDates(fromYear,toYear);
    	String[] dateList=dates.split(",");
    	String fromDate="",toDate="",adjFlag="false",frmName="";
    	fromDate=dateList[0];
    	toDate=dateList[1];
    	list=finReportDAO.getRnfcForm68PS(fromDate,toDate,region,pensionno,formType,fromYear,sortedColumn,adjFlag,frmName);
    	
        return list;
    }
    public void processingPFCards(){
    	finReportDAO.processingPFCards();
    }
    public ArrayList getRPFCForm8IndivReport(String selectedYear, String month,
			String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName,String formType,String formTypeRevisedFlag,String adjFlag,String frmName, String pcFlag) {
		ArrayList totalYearForm8List=new ArrayList();
		ArrayList form8List=new ArrayList();
		String dispFinnYear="";
		int currentYear=0, fromOldYear=1995,totalSpan=0,minRevisedYear=0,maxRevisedYear=0;
		log.info("selectedYear======getRPFCForm8IndivReport======="+selectedYear);
		if(selectedYear.equals("NO-SELECT") || selectedYear.equals("Select One")){
			if(formTypeRevisedFlag.trim().equals("Y")){
				String minMaxYear=this.getMinMaxYearsForArrearBreakup(pensionNo);
				log.info("======minMaxYear======="+minMaxYear);
				if(!minMaxYear.equals("")){
					String[] minMaxYearList=minMaxYear.split("-");
					minRevisedYear=Integer.parseInt(minMaxYearList[0]);
					maxRevisedYear=Integer.parseInt(minMaxYearList[1]);
				}else{
					minRevisedYear=2006;
					maxRevisedYear=2007;
				}
				 currentYear=maxRevisedYear;
				 fromOldYear=minRevisedYear;
				 totalSpan=currentYear-fromOldYear;
			}else{
				 currentYear=Integer.parseInt(commonUtil.getCurrentDate("yyyy"))+1;
				 fromOldYear=1995;
				 totalSpan=currentYear-fromOldYear;
			}
			
		}else{
			fromOldYear=Integer.parseInt(selectedYear);
			currentYear=fromOldYear+1;
			totalSpan=currentYear-fromOldYear;
		}

		String message="";
		int messageFromYear=0,messageToYear=0;
		Form7MultipleYearBean multipleYearBean=null;
		
			for(int i=0;i<totalSpan;i++){
				
				form8List=finReportDAO.getRfcForm8IndivReport(Integer.toString(fromOldYear+i), "NO-SELECT",sortedColumn, region, formFlag,airportCode,  pensionNo,  range,  empflag,
						 empName, formType,formTypeRevisedFlag,adjFlag,frmName,pcFlag);
				log.info("fromOldYear"+fromOldYear+"form8List"+form8List.size());
				if(form8List.size()!=0){
					 multipleYearBean=new Form7MultipleYearBean();
					messageFromYear=fromOldYear+i;
					messageToYear=messageFromYear+1;
					message="Mar-" + messageFromYear + "  To Feb-" + messageToYear;
					dispFinnYear=messageFromYear+"-"+messageToYear;
					multipleYearBean.setDispFinYear(dispFinnYear);
					multipleYearBean.setEachYearList(form8List);
					multipleYearBean.setMessage(message);
					totalYearForm8List.add(multipleYearBean);
				}
			
			
		}
		return totalYearForm8List;
    }

public ArrayList getRPFCForm8PFIDIndivReport(String selectedYear, String month,
			String sortedColumn, String region, boolean formFlag,
			String airportCode, String pensionNo, String range, String empflag,
			String empName,String formType,String formTypeRevisedFlag,String adjFlag,String frmName,String pcFlag) {
		ArrayList totalYearForm8List=new ArrayList();
		ArrayList form8List=new ArrayList();
		int currentYear=0, fromOldYear=1995,totalSpan=0;
		log.info("selectedYear======getRPFCForm8PFIDIndivReport======="+selectedYear);
		fromOldYear=Integer.parseInt(selectedYear);
		currentYear=fromOldYear+1;
		totalSpan=currentYear-fromOldYear;
		String message="";
		int messageFromYear=0,messageToYear=0;
		Form7MultipleYearBean multipleYearBean=null;
		multipleYearBean=new Form7MultipleYearBean();
		form8List=finReportDAO.getRfcForm8RangeReport(Integer.toString(fromOldYear), "NO-SELECT",sortedColumn, region, formFlag,airportCode,  pensionNo,  range,  empflag,
						 empName, formType,formTypeRevisedFlag,adjFlag,frmName,pcFlag);
		

		return form8List;
    }
    public ArrayList rnfcForm78SummaryReport(String selectedYear,String pensionno,String range,String empflag,String empName,String formRevisedFlag,String adjFlag,String frmName,String pcFlag){
        ArrayList form78List=new ArrayList();
        form78List=finReportDAO.rnfcForm78SummaryReport(selectedYear,pensionno,range,empflag,empName,formRevisedFlag,adjFlag,frmName,pcFlag);
        return form78List;
    }

	public ArrayList getStatementOfWagePensionReport(String range, String region,
			String sortedColumn, String pensionno,
			String airportcode, String adjFlag , String frmName) {
		ArrayList list=new ArrayList();
		list=finReportDAO.getStatementOfWagePensionReport(range,region,sortedColumn,pensionno,airportcode,adjFlag ,frmName);
		return list;
	}
	public String getMinMaxYearsForArrearBreakup(String pensionno){
		return finReportDAO.getMinMaxYearsForArrearBreakup(pensionno);
	}
		public double getInterestforNoofMonths(String interestCalcUpto){
			return finReportDAO.getInterestforNoofMonths(interestCalcUpto);
		}
		public String finalintrestdate(String interestCalcUpto,String empserialNO){
			return finReportDAO.finalintrestdate(interestCalcUpto,empserialNO);
		}
		public String reIntrestDate(String empserialNO){
			return finReportDAO.reIntrestDate(empserialNO);
		}
		public String reSettlementdate(String empserialNO){
			return finReportDAO.reSettlementdate(empserialNO);
		}	
		public double interestforfinalsettleMonths(String reinterestcalcdate,String empserialNO){
			return finReportDAO.interestforfinalsettleMonths(reinterestcalcdate,empserialNO);
		}
	public ActiveInactivBean getFinalPaymentPendingReport(String activity,String financeYear){
		return finReportDAO.getFinalPaymentPendingReport(activity,financeYear);
	}
	public int insertedDeactiveEmp(String pfidStrip,String finyear){
		return finReportDAO.insertedDeactiveEmp(pfidStrip,finyear);
	}
	public int deleteDeactiveEmp(String pfidStrip,String finyear) throws SQLException{
		return finReportDAO.deleteDeactiveEmp(pfidStrip,finyear);
	}
	 
	 public void runPFCardScheduler(){
	    	//finReportDAO.runPFCardScheduler();
	    }
	 public ArrayList getStatementOfWagePensionRevisedReport(String range, String region,
				String sortedColumn, String pensionno,
				String airportcode, String adjFlag , String frmName) {
			ArrayList list=new ArrayList();
			list=finReportDAO.getStatementOfWagePensionRevisedReport(range,region,sortedColumn,pensionno,airportcode,adjFlag ,frmName);
			return list;
		}
	 public String chkPfidHavngPCAfterSeperation(String pfId ,PensionBean empBean) {
			String chkPfid = "";		 
			chkPfid = finReportDAO.chkPfidHavngPCAfterSeperation(pfId, empBean);
			return chkPfid;
		}
	 
	
	 public ArrayList retdListReportByPFID(String monthYear, String region, String airportcode) throws InvalidDataException {
			ArrayList list=new ArrayList();
	    
				list=finReportDAO.retdPersonalByPFID(monthYear,region,airportcode);
		
			return list;
		}
	 public int editPensionProcessInfo(String pensionNumber,String clientname,String remarks,String cpfaccno,String region ,String employeeName,String desig,String dateofbirth,String dateofJoining,String airportCode,String fathername,String employeeCode,String gender,String pensionOption,String pensiontype,String docchq,String mblno,String emailid){
	    	int result=0;
	    	log.info("FinanceReportService:editSettlementInfo:---Entering method");
	    	result=finReportDAO.editPensionProcessInfo1(pensionNumber,clientname, remarks, cpfaccno, region ,employeeName,desig,dateofbirth,dateofJoining,airportCode,fathername,employeeCode,gender,pensionOption,pensiontype,docchq,mblno,emailid);
	        log.info("FinanceReportService:editSettlementInfo:---Leaving method");
	        return result;
	    }
	public String getIndRegion(String pensionno) {
		
		return finReportDAO.getIndRegion(pensionno);
	}
	public String getIndStation(String pensionno) {
		return finReportDAO.getIndStation(pensionno);
	}
}
