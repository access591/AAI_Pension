package aims.service;

import java.util.ArrayList;
import java.util.Map;

import aims.bean.epfforms.AAIEPFReportBean;
import aims.bean.epfforms.ControlAccountForm2Info;
import aims.bean.epfforms.RemittanceBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.EPFFormsReportDAO;
import aims.dao.FinancialReportDAO;

public class EPFFormsReportService {
	Log log = new Log(EPFFormsReportService.class);
	EPFFormsReportDAO epfformsReportDAO=new EPFFormsReportDAO();
	CommonUtil commonUtil=new CommonUtil();
	FinancialReportDAO finReportDAO=new FinancialReportDAO();
	public ArrayList AAIEPFForm2Report(String pfidString,String region,String airportcode,String frmSelectedDts,String flag,String employeeName,String sortedColumn,String pensionNo,String adjtype,String statusFlag,String  statusType){
        ArrayList AAIEPFLst=new ArrayList();
        AAIEPFLst=epfformsReportDAO.AAIEPFForm2Report(pfidString,region,airportcode, frmSelectedDts, flag,employeeName, sortedColumn, pensionNo,adjtype,statusFlag, statusType);
        return AAIEPFLst;
	}
	public ArrayList loadEPFForm3Report(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getEPFForm3Report(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	
	public ArrayList loadEPFForm5CadReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form5CadList=new ArrayList();
		
		//commenting code 
		//form5CadList=epfformsReportDAO.getEPFForm5CadReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form5CadList;
	}
	
	
	public ArrayList loadExecutiveReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getExecutiveReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	public ArrayList loadEPFForm4Report(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno,String select_month){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getEPFForm4Report(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno,select_month);
		return form3List;
	}
	public ArrayList loadEPFMissingTransPFIDs(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getMissingTransPFIDs(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	
	public AAIEPFReportBean AAIEPFForm1Report(String pfidString,String region,String airportcode,String frmSelectedDts,String flag,String employeeName,String sortedColumn,String pensionNo){
		AAIEPFReportBean AAIEPFreportbean=new AAIEPFReportBean();
		AAIEPFreportbean=epfformsReportDAO.AAIEPFForm1Report(pfidString,region,airportcode, frmSelectedDts, flag,employeeName, sortedColumn, pensionNo);
	    return AAIEPFreportbean;
	}
	public AAIEPFReportBean AAIEPFForm8Report(String pfidString,String region,String airportcode,String frmSelectedDts,String flag,String employeeName,String sortedColumn,String pensionNo){
		 AAIEPFReportBean AAIEPFBean=new AAIEPFReportBean();
		 AAIEPFBean=epfformsReportDAO.AAIEPFForm8Report(pfidString,region,airportcode, frmSelectedDts, flag,employeeName, sortedColumn, pensionNo);
	        return AAIEPFBean;
	}
	public ArrayList AAIEPFForm8SummaryReport(String pfidString,String region,String airportcode,String frmSelectedDts,String flag,String employeeName,String sortedColumn,String pensionNo){
		   ArrayList list =new ArrayList();
		   list = epfformsReportDAO.AAIEPFForm8SummaryReport(pfidString,region,airportcode, frmSelectedDts, flag,employeeName, sortedColumn, pensionNo);
	        return list;
	} 
	
	public ArrayList loadEPFForm5Report(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getEPFForm5Report(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	public ArrayList loadEPFForm6Report(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form6List=new ArrayList();
		form6List=epfformsReportDAO.getEPFForm6Report(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form6List;
	}
	public ArrayList loadEPFForm7Report(String region,String airprotcode,String frmSelctedYear,String sortingOrder){
		ArrayList form7List=new ArrayList();
		form7List=epfformsReportDAO.getEPFForm7Report(region, airprotcode, frmSelctedYear, sortingOrder);
		return form7List;
	}

	public ArrayList loadEPFForm6AReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getForm6AEChallanReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	public ArrayList loadEPFForm6AChallanReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getForm6AEChallanReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	
	public ArrayList loadEPFForm6AChallanReportEcr(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		
		//commenting code 
		//form3List=epfformsReportDAO.getForm6AEChallanReportEcr(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	
	
	public ArrayList loadDiffermentReportEcr(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno,String monthYear,String month,String year){
		ArrayList form3List=new ArrayList();
		//commenting code 
		//form3List=epfformsReportDAO.getDiffermentReportEcr(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno,monthYear,month,year);
		return form3List;
	}
	
	
	
	
	
	
	public ArrayList loadFreezedEPFForm6AChallanReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList form3List=new ArrayList();
		form3List=epfformsReportDAO.getFreezedForm6AEChallanReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3List;
	}
	public Map loadEDLIInspectionChargesReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		Map form3Map = null;
		form3Map=epfformsReportDAO.getEDLIInspectionChargesReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return form3Map;
	}

	public ArrayList loadEPFForm11Report(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno,String status){
			ArrayList form11List=new ArrayList();
			finReportDAO.finalUpdates();
			form11List=epfformsReportDAO.getEPFForm11Report(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno,status);
			return form11List;
		}
	public ArrayList loadRemitanceInfo(String salaryMonth,String region,String airportcode,String remitanceType,String accountType){
		ArrayList remitanceList=new ArrayList();
		remitanceList=epfformsReportDAO.loadRemitanceInfo(salaryMonth,region,airportcode,remitanceType,accountType);
		return remitanceList;
	}
	public ArrayList loadRemitanceTableInfo(String salaryMonth,String region,String airportcode,String remitanceType,String inputfrom,String accountType){
		ArrayList remitanceList=new ArrayList();
		remitanceList=epfformsReportDAO.loadRemitanceTableInfo(salaryMonth,region,airportcode,remitanceType,inputfrom,accountType);
		return remitanceList;
	}
	public ArrayList getNoOfEmployees(String salaryMonth,String region,String airportcode,String remitanceType){
		ArrayList noofEmployeesList=new ArrayList();
		noofEmployeesList=epfformsReportDAO.getNoOfEmployees(salaryMonth,region,airportcode,remitanceType);
		return noofEmployeesList;
	}
	public ArrayList getGrandtotalsRegionwise(String salaryMonth,String region,String airportcode,String remitanceType){
		ArrayList noofEmployeesList=new ArrayList();
		noofEmployeesList=epfformsReportDAO.getGrandtotalsRegionwise(salaryMonth,region,airportcode,remitanceType);
		return noofEmployeesList;
	}
	
	
	public void inserRemittanceInfo(RemittanceBean rbean,String username,String computername){
		epfformsReportDAO.inserRemittanceInfo(rbean,username,computername);
	}
	public double getAaiEpf8totals(String salaryMonth,String region,String airportcode){
		double epf8Totals=0.00;
		epf8Totals=epfformsReportDAO.getAaiEpf8totals(salaryMonth,region,airportcode);
		return epf8Totals;	
	}
	public ArrayList loadAccrationReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno,String accountType,String remitancetype){
		ArrayList loadAccrationReportList=new ArrayList();
		loadAccrationReportList=epfformsReportDAO.loadAccrationReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno,accountType,remitancetype);
		return loadAccrationReportList;
	}
	public ArrayList loadEPFForm3SippliBlockReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno,String suppliFlag){
		ArrayList suppliList=new ArrayList();
		suppliList=epfformsReportDAO.getEPFForm3SippliBlockReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno,suppliFlag);
		return suppliList;
	}
 
	public ArrayList loadEPFForm5SuppliBlockReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList suppliForm5List=new ArrayList();
		suppliForm5List=epfformsReportDAO.getEPFForm5SuppliBlockReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return suppliForm5List;
	}
	public ArrayList loadArrearEPF5BlockReport(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		ArrayList arrearForm5List=new ArrayList();
		arrearForm5List=epfformsReportDAO.getEPFForm5ArrearBlockReport(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		return arrearForm5List;
		}
	/*public ArrayList getForm3SummaryReport(String year,String flag,String empStatus) {
		ArrayList form3summaryInfo = null;
		form3summaryInfo = epfformsReportDAO.getForm3SummaryReport(year,flag,empStatus);
		return form3summaryInfo;
	}*/
	public ArrayList getControlAccSummaryReport(String finyear,String formType,String region,String serialNo,String crtlAccFlag,String frm_status,String reportpurpose) {
		ArrayList cntrlAccSummaryList = null;
		cntrlAccSummaryList = epfformsReportDAO.getControlAccSummaryReport(finyear,formType,region,serialNo,crtlAccFlag,frm_status,reportpurpose);
		return cntrlAccSummaryList;
	}
	public ArrayList getControlAccSummaryRegionWiseReport(String finyear,String empStatus){
		ArrayList form11List=new ArrayList();		 
		form11List=epfformsReportDAO.getControlAccSummaryRegionWiseReport(finyear,empStatus);
		return form11List;
	}
	public ControlAccountForm2Info getControlAccSummaryAdjReport(String finyear,String empstatus){
		ControlAccountForm2Info summaryAdjinfo=new ControlAccountForm2Info();		 
		summaryAdjinfo=epfformsReportDAO.getControlAccSummaryAdjReport(finyear,empstatus);
		return summaryAdjinfo;
	}
	public ArrayList epfForm8SummaryDetailsWitKeyNo(String finyear,String accounthead){
		ArrayList witKeyNoList=new ArrayList();		 
		witKeyNoList=epfformsReportDAO.epfForm8SummaryDetailsWitKeyNo(finyear,accounthead);
		return witKeyNoList;
	}
	public ArrayList epfForm8SummaryDetailsWitOutKeyNo(String finyear,String accounthead){
		ArrayList witOutKeyNoList=new ArrayList();		 
		witOutKeyNoList=epfformsReportDAO.epfForm8SummaryDetailsWitOutKeyNo(finyear,accounthead);
		return witOutKeyNoList;
	} 
	public String generateEPFForm6AChallan(String range,String region,String airprotcode,String empName,String empNameFlag,String frmSelctedYear,String sortingOrder,String frmPensionno){
		String path="";
		try {
			path=epfformsReportDAO.rpfcForm6ECR(range, region, airprotcode, empName, empNameFlag, frmSelctedYear, sortingOrder,frmPensionno);
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path;
	}
	public Map getForm3SummaryReport(String year,String flag,String empStatus) {
		Map form3summaryInfo = null;
		form3summaryInfo = epfformsReportDAO.getForm3SummaryReport(year,flag,empStatus);
		return form3summaryInfo;
	}
	public ArrayList getStationList(String region,String monthyear,String station){
		ArrayList stationList = null;
		stationList = epfformsReportDAO.getStationList(region,monthyear,station);
		return stationList;
	}
	public ArrayList getStationListForAllAirports(String region,String monthyear,String station){
		ArrayList stationList = null;
		
		//commenting code 
		//stationList = epfformsReportDAO.getStationListForAllAirports(region,monthyear,station);
		return stationList;
	}
	public int updateStationWiseRemittance(String pfRemitDate,String inspRemitDate,String pcRemitDate,String pf,String insp,String pc,String editid,String remittanceDate,String station,String region,String accType,String username,String flag,String remarks){
		int result=0;
		result=epfformsReportDAO.getupdateStationWiseRemittance(pfRemitDate,inspRemitDate,pcRemitDate,pf,insp,pc,editid,remittanceDate,station,region,accType,username,flag,remarks);
		return result;
		
	}
	
	
	public Map ProformaEcr(String month, String monthyear) {
		Map total = null;
		//commenting code 
		//total=epfformsReportDAO.getProformaEcr(month,monthyear);
		return total;
	}

	
	
}
