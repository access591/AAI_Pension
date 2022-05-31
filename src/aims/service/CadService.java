package aims.service;

import java.util.ArrayList;

import com.grid.Pagenation;

import aims.bean.BottomGridNavigationInfo;
import aims.bean.EmpMasterBean;
import aims.bean.Form7MultipleYearBean;
import aims.bean.SearchInfo;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CadReportDAO;
import aims.dao.CommonDAO;
import aims.dao.FinancialDAO;
import aims.dao.FinancialReportDAO;

public class CadService {
	Log log = new Log(PensionService.class);
	
	
	CadReportDAO cadDAO = new CadReportDAO();	
	CommonUtil commonUtil = new CommonUtil();
	Pagenation paging = new Pagenation();

	public void formsDisable(String pfid, String formsstatus,
			String pcreportstatus, String pensionclaimstatus, String userName,
			String password) {
		cadDAO.formsDisable(pfid, formsstatus, pcreportstatus,
				pensionclaimstatus, userName, password);
	}

	public double getPensionContributionTotal(String PFID) {
		return cadDAO.getPensionContributionTotal(PFID);
	}

	public double getfinalrevoveryintrest(String PFID) {
		return cadDAO.getfinalrevoveryintrest(PFID);
	}

	public String getEditedPensionno(String PFID) {
		return cadDAO.getEditedPensionno(PFID);
	}

	public double getInterestforNoofMonths(String interestCalcUpto) {
		return cadDAO.getInterestforNoofMonths(interestCalcUpto);
	}

	public String reIntrestDate(String empserialNO) {
		return cadDAO.reIntrestDate(empserialNO);
	}

	public double interestforfinalsettleMonths(String reinterestcalcdate,
			String empserialNO) {
		return cadDAO.interestforfinalsettleMonths(reinterestcalcdate,
				empserialNO);
	}

	public String finalintrestdate(String interestCalcUpto, String empserialNO) {
		return cadDAO.finalintrestdate(interestCalcUpto, empserialNO);
	}

	public String reSettlementdate(String empserialNO) {
		return cadDAO.reSettlementdate(empserialNO);
	}
	 public void editTransactionData(String cpfAccno,String monthyear,String emoluments,String vpf,String principle,String interest,String contri,String advance,String loan,String aailoan,String pfid,String region,String airportcode,String username,String computername,String from7narration,String pcheldamt,String noofmonths,String arrearflag,String duputationflag,String pensionoption,String recoverieTable,String epf,String benfundcontr,String benfundPension,String gratuityInterest,String epfoPension,String addContri){
	        cadDAO.editTransactionData(cpfAccno,monthyear,emoluments,vpf,principle,interest,contri,advance,loan,aailoan,pfid,region,airportcode,username,computername,from7narration,pcheldamt,noofmonths,arrearflag,duputationflag,pensionoption,recoverieTable,epf,benfundcontr,benfundPension,gratuityInterest,epfoPension,addContri);
	    }
	 public int preProcessAdjOB(String pfid){
	    	int updateStatus=0;
	    	updateStatus=cadDAO.preProcessAdjOB(pfid);
	    	return updateStatus;
	    }
	 public void deleteTransactionData(String cpfAccno,String monthyear,String region,String airportcode,String ComputerName,String Username,String pfid){
	        cadDAO.deleteTransactionData(cpfAccno,monthyear,region,airportcode,ComputerName,Username,pfid);
	    }
	 
	   public void editReInterestCalc(String interestcalcDate,String pfid,String userId,String recoverieTable){
	        cadDAO.editReInterestCalc(interestcalcDate,pfid,userId,recoverieTable);
	    }
	   
	   public void editInterestCalc(String interestcalcDate,String pfid,String userId,String recoverieTable){
	        cadDAO.editInterestCalcDate(interestcalcDate,pfid,userId,recoverieTable);
	    }
	   
	   public void editFinalDate(String finalsettlementDate,String pfid,String userId){
	        cadDAO.editFinalDate(finalsettlementDate,pfid,userId);
	    }
	 
	public SearchInfo financeDataSearch(EmpMasterBean empSearch,
			SearchInfo searchInfo, int gridLength, String empNameCheck,
			String reporttype, String pfidfrom) {
		ArrayList financeDataList = new ArrayList();
		int startIndex = 1;

		String unmappedFlag = empSearch.getUnmappedFlag();
		String allRecordsFlag = empSearch.getAllRecordsFlag();
		String navButton = "";
		if (searchInfo.getNavButton() != null) {
			navButton = searchInfo.getNavButton();
		}

		if (new Integer(searchInfo.getStartIndex()) != null) {
			startIndex = searchInfo.getStartIndex();
		}
		int totalRecords = cadDAO.totalCountPersonal(empSearch,
				gridLength, startIndex, empNameCheck, "total", unmappedFlag,
				allRecordsFlag, pfidfrom);
		// int totalUnmappedRecords=
		// cadDAO.totalCountPersonal(empSearch,gridLength
		// ,startIndex,empNameCheck,"",unmappedFlag);
		startIndex = paging.getPageIndex(navButton, startIndex, totalRecords,
				gridLength);
		financeDataList = cadDAO.financeDataSearch(empSearch, gridLength,
				startIndex, empNameCheck, unmappedFlag, reporttype,
				allRecordsFlag, pfidfrom);

		BottomGridNavigationInfo bottomGridNavigationInfo = new BottomGridNavigationInfo();
		bottomGridNavigationInfo = paging.searchPagination(totalRecords,
				startIndex, gridLength);
		searchInfo.setStartIndex(startIndex);
		searchInfo.setSearchList(financeDataList);

		searchInfo.setTotalRecords(totalRecords);
		searchInfo.setBottomGrid(bottomGridNavigationInfo);

		return searchInfo;
	}

	public SearchInfo financeDataReport(EmpMasterBean empSearch,
			SearchInfo searchInfo, int gridLength, String empNameCheck,
			String reporttype, String pfidfrom) {
		log.info("FinancialService:financeDataReport Entering method"
				+ reporttype);
		ArrayList financeDataList = new ArrayList();
		int startIndex = 1;

		String unmappedFlag = empSearch.getUnmappedFlag();
		String allRecordsFlag = empSearch.getAllRecordsFlag();
		if (new Integer(searchInfo.getStartIndex()) != null) {
			startIndex = searchInfo.getStartIndex();
		}
		financeDataList = cadDAO.financeDataReport(empSearch, gridLength,
				startIndex, empNameCheck, unmappedFlag, reporttype,
				allRecordsFlag, pfidfrom);
		searchInfo.setSearchList(financeDataList);
		return searchInfo;
	}
	
	public int insertEmployeeTransData(String pfId) throws Exception {
		int result = 0;
		result =  cadDAO.insertEmployeeTransData(pfId);		
		return result;
	}
	

	public ArrayList getPensionContributionReport(String finToYear,
			String finYear, String region, String airportcode,
			String selectedMonth, String empserialNO, String cpfAccno,
			String transferFlag, String mappingFlag, String pfIDStrip,
			String bulkPrint, String recoverieTable) {
		
		try {
			log.info("  insert cad_validite table enter    "+empserialNO);
			this.insertEmployeeTransData(empserialNO);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fromYear = "", toYear = "", tempToYear = "", finMonth = "", selectedToYear = "";
		boolean recoverieTableCheck = false;
		String[] finMnthYearList = new String[5];
		boolean monthFlag = false;
		ArrayList PensionContributionList = new ArrayList();
		finMnthYearList = finYear.split(",");
		System.out.println(finMnthYearList);
		System.out.println(toYear);
		if (!finToYear.equals("NO-SELECT")) {
			selectedToYear = finToYear;
		} else {
			selectedToYear = "2018";
		}
		if (!selectedMonth.equals("NO-SELECT")) {
			finMonth = selectedMonth;
			monthFlag = true;
		}
		if (!finYear.equals("NO-SELECT") && monthFlag == false) {
			log.info("From Year" + finYear + "To Year" + selectedToYear
					+ "finMonth" + finMonth);
			fromYear = "01-Apr-" + finYear;
			try {
				fromYear = commonUtil.converDBToAppFormat(fromYear,
						"dd-MM-yyyy", "dd-MMM-yyyy");
				log.info("fromYear" + fromYear);
				if (Integer.parseInt(selectedToYear.trim()) >= 89
						&& Integer.parseInt(selectedToYear.trim()) <= 99) {
					toYear = "01-" + finMonth + "-" + selectedToYear;
				} else {
					toYear = "01-" + finMonth + "-" + selectedToYear;
				}
				toYear = commonUtil.converDBToAppFormat(toYear, "dd-MM-yyyy",
						"dd-MMM-yyyy");
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
			}
		} else if (!finYear.equals("NO-SELECT") && monthFlag == true) {
			try {
				if (Integer.parseInt(finMonth) >= 4
						&& Integer.parseInt(finMonth) <= 12) {
					fromYear = "01-Apr-" + finYear;
					toYear = "01-" + finMonth + "-" + selectedToYear;
					fromYear = commonUtil.converDBToAppFormat(fromYear,
							"dd-MMM-yyyy", "dd-MMM-yyyy");
					toYear = commonUtil.converDBToAppFormat(toYear,
							"dd-MM-yyyy", "dd-MMM-yyyy");
				//	log.info("check condition1" + toYear);
				} else if (Integer.parseInt(finMonth) >= 1
						&& Integer.parseInt(finMonth) <= 3) {
					if (Integer.parseInt(selectedToYear.trim()) >= 95
							&& Integer.parseInt(selectedToYear.trim()) <= 99) {
						fromYear = "01-Apr-" + finYear;
						toYear = "01-" + finMonth + "-" + selectedToYear;
					} else {
						fromYear = "01-Apr-" + finYear;
						toYear = "01-" + finMonth + "-" + selectedToYear;
					}
					fromYear = commonUtil.converDBToAppFormat(fromYear,
							"dd-MMM-yyyy", "dd-MMM-yyyy");
					toYear = commonUtil.converDBToAppFormat(toYear,
							"dd-MM-yyyy", "dd-MMM-yyyy");
					//log.info("check condition2" + toYear);
				}
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
			}

		} else {
			fromYear = "01-Apr-1989";
			tempToYear = commonUtil.getCurrentDate("yyyy");
			toYear = "01-Mar-2018";
			
		}		

		if (recoverieTable.equals("true")) {
			recoverieTableCheck = true;
		}
		if (mappingFlag.trim().equals("true")) {		
			PensionContributionList = cadDAO.pensionContributionReport(
					fromYear, toYear, region, airportcode, empserialNO,
					cpfAccno, transferFlag, mappingFlag, recoverieTableCheck);
		} else {
			
			PensionContributionList = cadDAO
					.pensionContributionReportAll(fromYear, toYear, region,
							airportcode, empserialNO, cpfAccno, transferFlag,
							pfIDStrip, bulkPrint);
		}

		return PensionContributionList;

	}
	  public void insertCadAllYear(String pfid,String year,String emoluments,String pfStaturary,String AdditionalContri,String empVpf,String cpfInterest,String pensionInterest,String totalPension){
		  log.info("inside insertCadAllYear Service ");
	        cadDAO.insertCadAllYear(pfid,year,emoluments,pfStaturary,AdditionalContri,empVpf,cpfInterest,pensionInterest,totalPension);
	    }
	  
	  public void updateCadTracking(String pfid,String apstatus){
		  log.info(" updateCadTracking  ");
	        cadDAO.updateCadTracking(pfid,apstatus);
	    }
	  
	  public void cadtracking(String pfid,String status,String approvedby){
		  log.info("inside insertCadAllYear Service ");
	        cadDAO.cadtracking(pfid,status,approvedby);
	    }
	  public String getCadTrackingStatus(String pfid){
		return cadDAO.getCadTrackingStatus(pfid);  
	  }
	  
		public ArrayList CpfData(String range,String selectedYear,String sortedColumn,String region,String airportCode,String pensionno,String empflag,String empName,String formType,String formRevisedFlag,String adjFlag,String frmName,String pcFlag,String selectyear){
	        ArrayList form8List=new ArrayList();
	        form8List=cadDAO.Cpfdata(selectedYear,"NO-SELECT",sortedColumn,region,false,airportCode,pensionno,range,empflag,empName,formType,formRevisedFlag,adjFlag,frmName,pcFlag,selectyear);
	        return form8List;
	    }
		
/*		public ArrayList getAllYearReport(String pensionno){			
				ArrayList totalYearForm8List = new ArrayList();
				ArrayList form8List = new ArrayList();
				String minYear = "", maxYear = "";
				boolean chkYears = false;
				String message = "";
				int messageFromYear = 0, messageToYear = 0;
				Form7MultipleYearBean multipleYearBean = null;
				int currentYear = 0, fromOldYear = 1995, totalSpan = 0;	
				
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
			
	    }*/
		
		
	  
}
