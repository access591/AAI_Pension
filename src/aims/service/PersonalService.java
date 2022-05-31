package aims.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import aims.bean.BottomGridNavigationInfo;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeeAdlPensionInfo;
import aims.bean.EmployeeFreshOptionBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.NomineeBean;
import aims.bean.NomineeForm2Info;
import aims.bean.RPFCForm9Bean;
import aims.bean.SearchInfo;
import aims.common.CommonUtil;
import aims.common.DBUtils;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.dao.ImportDao;
import aims.dao.PersonalDAO;

import com.grid.Pagenation;

public class PersonalService {
	Log log = new Log(PersonalService.class);

	PersonalDAO personalDAO = new PersonalDAO();

	Pagenation paging = new Pagenation();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();

	ImportDao importDAO = new ImportDao();

	public String autoProcPersonalInfo(String selectedDt, String retriedDate,
			String region, String airportCode, String userName, String IPAddress) {
		String message = "", regionMessage = "";
		int regionSize = 0, OrinialSize = 0;
		boolean chkMnthYearFlag = false;
		HashMap regionMap = new HashMap();
		log.info("OrinialSize======" + OrinialSize + "After Find out"
				+ regionSize);

		try {

			message = personalDAO.autoProcessingPersonalInfo(selectedDt,
					retriedDate, regionMap, airportCode, userName, IPAddress);
		} catch (IOException e) {
			log.printStackTrace(e);
		}

		return message;
	}

	public void insertPensionFreshOptionData(EmployeeFreshOptionBean freshBean) {
		personalDAO.insertPensionFreshOption(freshBean);
	}

	public String getMessageForFreshOption(EmployeePersonalInfo bean) {
		String message = "";
		message = personalDAO.getMessageForFreshOption(bean);
		return message;
	}

	public ArrayList searchPensionFreshOption(EmployeePersonalInfo empBean,
			int gridLength, String sortingColumn) {
		log
				.info("=============Enter searchPensionFreshOption====================");
		int startIndex = 1;
		ArrayList empInfo = new ArrayList();
		empInfo = personalDAO.searchPensionFreshOption(empBean, startIndex,
				gridLength, sortingColumn);
		log.info("EmpList size ========" + empInfo.size());
		log
				.info("=============End searchPensionFreshOption====================");
		return empInfo;
	}

	public SearchInfo searchPersonalMaster(EmployeePersonalInfo bean,
			SearchInfo searchInfo, boolean flag, int gridLength,
			String sortingColumn, String page) throws Exception {
		log.info("===========Enter searchPersonalMaster====================");
		int startIndex = 0;
		ArrayList empInfo = new ArrayList();
		startIndex = 1;
		empInfo = personalDAO.searchPersonal(bean, startIndex, gridLength,
				sortingColumn, "non-count", page);
		log.info("emp list size " + empInfo.size());
		int totalRecords = personalDAO.totalCountPersonal(bean, "count",
				sortingColumn, page);
		BottomGridNavigationInfo bottomGridNavigationInfo = new BottomGridNavigationInfo();
		bottomGridNavigationInfo = paging.searchPagination(totalRecords,
				startIndex, gridLength);
		searchInfo.setStartIndex(startIndex);
		searchInfo.setSearchList(empInfo);
		searchInfo.setTotalRecords(totalRecords);
		searchInfo.setBottomGrid(bottomGridNavigationInfo);
		log.info("=============EndsearchPersonalMaster====================");
		return searchInfo;

	}

	public SearchInfo navigationPersonalData(EmployeePersonalInfo empBean,
			SearchInfo searchInfo, int gridLength, String sortingColumn,
			String page) throws Exception {
		int startIndex = 1;
		String navButton = "";
		ArrayList hindiData = new ArrayList();
		BottomGridNavigationInfo bottomGridNavigationInfo = new BottomGridNavigationInfo();
		if (searchInfo.getNavButton() != null) {
			navButton = searchInfo.getNavButton();
		}
		if (new Integer(searchInfo.getStartIndex()) != null) {
			startIndex = searchInfo.getStartIndex();
		}
		if (sortingColumn.equals("")) {
			sortingColumn = "employeename";
		}
		int rowCount = personalDAO.totalCountPersonal(empBean, "count",
				sortingColumn, page);
		System.out.println("rowCount" + rowCount + "startIndex" + startIndex
				+ "navButton" + navButton);
		startIndex = paging.getPageIndex(navButton, startIndex, rowCount,
				gridLength);
		hindiData = personalDAO.searchPersonal(empBean, startIndex, gridLength,
				sortingColumn, "non-count", page);
		bottomGridNavigationInfo = paging.navigationPagination(rowCount,
				startIndex, false, false, gridLength);
		searchInfo.setStartIndex(startIndex);
		searchInfo.setSearchList(hindiData);
		searchInfo.setTotalRecords(rowCount);
		searchInfo.setBottomGrid(bottomGridNavigationInfo);
		return searchInfo;

	}

	public ArrayList personalReport(EmployeePersonalInfo empBean,
			String sortingColumn, String reportType) {
		ArrayList personalReport = new ArrayList();
		personalReport = personalDAO.personalReport(empBean, "non-count", "",
				sortingColumn, reportType);
		return personalReport;

	}





	public ArrayList getDepartmentList() {
		ArrayList departmentList = commonDAO.getDepartmentList();
		return departmentList;
	}

	public ArrayList getDesginationList() {
		ArrayList departmentList = commonDAO.getDesignationList();
		return departmentList;
	}

	public EmpMasterBean empPersonalEdit(String cpfacno, String empName,
			boolean flag, String empCode, String region, String pfid)
			throws Exception {
		EmpMasterBean editbean = null;
		editbean = personalDAO.empPersonalEdit(cpfacno, empName, flag, empCode,
				region, pfid);
		return editbean;
	}

	public int updatePensionMaster(EmpMasterBean bean, String flag)
			throws InvalidDataException {

		int count = 0;
		String empLevel = "";
		EmpMasterBean desegBean = new EmpMasterBean();
		empLevel = bean.getEmpLevel();
		desegBean = personalDAO.getDesegnation(empLevel);
		log.info("bean.getDesegnation().trim()" + bean.getDesegnation());
		log.info("desegBean " + desegBean);
		/*
		 * if (!desegBean.getDesegnation().trim().equals(
		 * bean.getDesegnation().trim())) { throw new InvalidDataException( "Emp
		 * Level with respective Desegnation is not equal"); }
		 */
		System.out.println("flag=(updatePensionMaster)=" + flag);

		try {
			count = personalDAO.personalUpdate(bean, flag);
		} catch (InvalidDataException ide) {
			throw ide;
			// throw new InvalidDataException("PensionNumber Already Exist");
		}
		return count;
	}

	public int updatePensionMasterRevised(EmpMasterBean bean, String flag)
			throws InvalidDataException {

		int count = 0;
		String empLevel = "";
		EmpMasterBean desegBean = new EmpMasterBean();
		empLevel = bean.getEmpLevel();
		desegBean = personalDAO.getDesegnation(empLevel);
		log.info("bean.getDesegnation().trim()" + bean.getDesegnation());
		log.info("desegBean " + desegBean);
		/*
		 * if (!desegBean.getDesegnation().trim().equals(
		 * bean.getDesegnation().trim())) { throw new InvalidDataException( "Emp
		 * Level with respective Desegnation is not equal"); }
		 */
		System.out.println("flag=(updatePensionMaster)=" + flag);

		try {
			count = personalDAO.personalUpdateRevised(bean, flag);
		} catch (InvalidDataException ide) {
			throw ide;
			// throw new InvalidDataException("PensionNumber Already Exist");
		}
		return count;
	}

	public ArrayList checkPersonalInfo(EmployeePersonalInfo personalInfo,
			boolean empFlag, boolean dobFlag) {
		ArrayList list = new ArrayList();
		list = personalDAO.checkPersonalDtOfBirthInfo(personalInfo, empFlag,
				dobFlag);
		return list;
	}

	public String addPersonalInfo(EmployeePersonalInfo personalInfo,
			NomineeBean nomineeBean, String userName, String ipAddress)
			throws Exception {
		String addRecord = "";
		try {
			addRecord = personalDAO.addPersonalInfo(personalInfo, nomineeBean,
					userName, ipAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return addRecord;
	}

	public NomineeForm2Info form2Report(EmployeePersonalInfo personalInfo) {
		ArrayList personalList = new ArrayList();
		ArrayList nomineeList = new ArrayList();
		ArrayList familyList = new ArrayList();
		NomineeForm2Info form2Bean = new NomineeForm2Info();
		personalList = personalDAO.personalReport(personalInfo, "non-count",
				"", "PENSIONNO", "");
		form2Bean.setPersonalList(personalList);
		nomineeList = personalDAO.form2NomineeReport(personalInfo);
		form2Bean.setNomineeList(nomineeList);
		familyList = personalDAO.form2FamilyReport(personalInfo);
		form2Bean.setFamilyList(familyList);
		return form2Bean;

	}

	public RPFCForm9Bean form9Report(EmployeePersonalInfo personalInfo) {
		RPFCForm9Bean personalList = new RPFCForm9Bean();
		personalList = personalDAO.rpfcForm9Report(personalInfo, "non-count",
				"", "PENSIONNO");
		return personalList;
	}

	public String autoUpdatePersonalInfo(String selectedDt, String retriedDate,
			String region, String airportCode, String userName, String IPAddress) {
		String message = "", regionMessage = "";
		int regionSize = 0, OrinialSize = 0;
		boolean chkMnthYearFlag = false;
		HashMap regionMap = new HashMap();

		log.info("OrinialSize======" + OrinialSize + "After Find out"
				+ regionSize);

		try {

			message = personalDAO.autoUpdateProcessingPersonalInfo(selectedDt,
					retriedDate, regionMap, airportCode, userName, IPAddress);
		} catch (IOException e) {
			log.printStackTrace(e);
		}

		return message;
	}

	public int updatePFIDTrans(String range, String region,
			String selectedYear, String empflag, String empName,
			String pensionno) {
		String infoOnUpdtedRec = "", uploadFilePath = "", fileName = "", dispRange = "";
		int cntr = 0;
		FileWriter fw = null;
		ResourceBundle bundle = ResourceBundle
				.getBundle("aims.resource.DbProperties");
		uploadFilePath = bundle.getString("upload.folder.path");
		File filePath = new File(uploadFilePath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		if (region.equals("NO-SELECT")) {
			region = "";
		}
		if (range.equals("NO-SELECT")) {
			dispRange = "";
		} else {
			dispRange = range;
		}
		fileName = "//PFIDInformation" + region + "-" + dispRange + ".txt";
		try {
			fw = new FileWriter(new File(filePath + fileName));
			personalDAO.updatePFIDTransTbl(range, region, empflag, empName,
					pensionno, fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cntr;
	}

	public int updatePFPensionTrans(String range, String region,
			String selectedYear, String airportcode, String pensionno,
			String checkPendingFlag) {
		String infoOnUpdtedRec = "", uploadFilePath = "", fileName = "", dispRange = "", fromYear = "", toYear = "";
		int cntr = 0;
		FileWriter fw = null;
		ResourceBundle bundle = ResourceBundle
				.getBundle("aims.resource.DbProperties");
		uploadFilePath = bundle.getString("upload.folder.path");
		File filePath = new File(uploadFilePath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		if (region.equals("NO-SELECT")) {
			region = "";
		}
		if (range.equals("NO-SELECT")) {
			dispRange = "";
		} else {
			dispRange = range;
		}
		fileName = "//PFIDInformation" + region + "-" + dispRange + ".txt";
		if (!selectedYear.equals("Select One")) {
			fromYear = "01-Apr-" + selectedYear;
			int toSelectYear = 0;
			toSelectYear = Integer.parseInt(selectedYear) + 1;
			toYear = "01-Mar-" + toSelectYear;
		} else {
			fromYear = "01-Apr-2009";
			toYear = "01-Mar-2010";
		}
		try {
			fw = new FileWriter(new File(filePath + fileName));
			importDAO.pentionContributionProcess2008to11(range, region,
					airportcode, pensionno, fromYear, toYear, fw,
					checkPendingFlag);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cntr;
	}

	public ArrayList form10DInfo(String pensionNo) {
		ArrayList form10DList = new ArrayList();
		form10DList = personalDAO.getForm10DInfo(pensionNo);
		return form10DList;
	}

	public int updateForm10DInfo(EmpMasterBean bean,
			EmployeeAdlPensionInfo empAdlPensionInfo)
			throws InvalidDataException {

		int count = 0;

		try {
			count = personalDAO.updateForm10D(bean, empAdlPensionInfo);
		} catch (InvalidDataException ide) {
			throw ide;
			// throw new InvalidDataException("PensionNumber Already Exist");
		}
		return count;
	}

	public EmpMasterBean empForm10DPersonalEdit(String cpfacno, String empName,
			String region, String pfid) throws Exception {
		EmpMasterBean editbean = null;
		editbean = personalDAO.empForm10DPersonalEdit(cpfacno, empName, region,
				pfid);
		return editbean;
	}

	public EmployeeAdlPensionInfo getPensionForm10DDtls(String pfid) {
		EmployeeAdlPensionInfo addPensionInfo = new EmployeeAdlPensionInfo();
		addPensionInfo = personalDAO.getPensionForm10DDtls(pfid);
		return addPensionInfo;

	}

	public SearchInfo searchWetherOption(EmployeePersonalInfo bean,
			SearchInfo searchInfo, boolean flag, int gridLength,
			String sortingColumn, String page) throws Exception {
		log.info("===========Enter searchPersonalMaster====================");
		int startIndex = 0;
		ArrayList empInfo = new ArrayList();
		startIndex = 1;
		empInfo = personalDAO.searchPersonal(bean, startIndex, gridLength,
				sortingColumn, "non-count", page);
		log.info("emp list size " + empInfo.size());
		int totalRecords = personalDAO.totalCountPersonal(bean, "count",
				sortingColumn, page);
		BottomGridNavigationInfo bottomGridNavigationInfo = new BottomGridNavigationInfo();
		bottomGridNavigationInfo = paging.searchPagination(totalRecords,
				startIndex, gridLength);
		searchInfo.setStartIndex(startIndex);
		searchInfo.setSearchList(empInfo);
		searchInfo.setTotalRecords(totalRecords);
		searchInfo.setBottomGrid(bottomGridNavigationInfo);
		log.info("=============EndsearchPersonalMaster====================");
		return searchInfo;

	}

	public void executeProceure() {
		DBUtils commonDB = new DBUtils();
		String sSQLAddStep = "call example1";
		try {
			log
					.info(" ***********  stating of procedure execution *********** ");
			Connection con = commonDB.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sSQLAddStep);
			ResultSet rs = pstmt.executeQuery();

			log.info(" *********** end of procedure execution  ********** ");

		} catch (Exception e) {

		}
	}

	public ArrayList searchForPfidProcess(EmployeePersonalInfo empBean,
			String userRegion, String userStation, String profileType,
			String accessCode, String accountType) {
		ArrayList searchList = new ArrayList();
		searchList = personalDAO.searchForPfidProcess(empBean, userRegion,
				userStation, profileType, accessCode, accountType);
		return searchList;

	}

	public String chkForDuplicateEntry(String employeeName, String dateOfBirth) {
		String valEmpNameAndDOB = "";
		valEmpNameAndDOB = personalDAO.chkForDuplicateEntry(employeeName,
				dateOfBirth);
		return valEmpNameAndDOB;
	}

	public String savePfidProcessInfo(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		String processId = "";
		processId = personalDAO.savePfidProcessInfo(empInfo, userName,
				ipAddress, loginUserId, loginUsrStation, loginUsrRegion,
				emailid, loginUsrDesgn);
		return processId;
	}

	public ArrayList editPfidProcessInfo(String processid, String frmName) {
		ArrayList searchList = new ArrayList();
		searchList = personalDAO.editPfidProcessInfo(processid, frmName);
		return searchList;
	}

	public String savePfidProcessEditInfo(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		String processId = "";
		processId = personalDAO.savePfidProcessEditInfo(empInfo, userName,
				ipAddress, loginUserId, loginUsrStation, loginUsrRegion,
				emailid, loginUsrDesgn);
		return processId;
	}

	public String updatePFIDProcess(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		String processId = "";
		processId = personalDAO.updatePFIDProcess(empInfo, userName, ipAddress,
				loginUserId, loginUsrStation, loginUsrRegion, emailid,
				loginUsrDesgn);
		return processId;
	}

	public String createPFIDProcess(EmployeePersonalInfo empInfo,
			String userName, String ipAddress, String loginUserId,
			String loginUsrStation, String loginUsrRegion, String emailid,
			String loginUsrDesgn) {
		String processId = "";

		processId = personalDAO.createPFIDProcess(empInfo, userName, ipAddress,
				loginUserId, loginUsrStation, loginUsrRegion, emailid,
				loginUsrDesgn);

		return processId;
	}

	public String readPFIDFiles(String processid, String fileName) {
		String fileurl = "";
		try {
			fileurl = personalDAO.readPFIDFiles(processid, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileurl;
	}

	public EmployeePersonalInfo editPensionProcessInfo(String p_id,
			String pensionno, String verifiedby) {
		EmployeePersonalInfo searchList = new EmployeePersonalInfo();
		searchList = personalDAO.editPensionProcessInfo(p_id, pensionno,
				verifiedby);
		return searchList;
	}

	public void updatePensionProcessInfo(EmployeePersonalInfo info,
			String formName) {

		personalDAO.updatePensionProcessInfo(info);

	}

	public ArrayList searchPensionProcess(EmployeePersonalInfo bean,
			String formName) throws Exception {
		// log.info("===========Enter
		// searchPensionProcess====================");
		ArrayList empInfo = new ArrayList();
		empInfo = personalDAO.searchPension1(bean, formName);
		// log.info("=============EndsearchPersonalMaster====================" +
		// empInfo.size());
		return empInfo;

	}

	public void updatePensionProcessCHQ(EmployeePersonalInfo info,
			String formName) {

		personalDAO.updatePensionProcessCHQ(info, formName);
		
	}
	public ArrayList freshOptionReport(String frmName,String region,String airportCode) {
		ArrayList optionList = new ArrayList();
		optionList = personalDAO.freshOptionReports(frmName, region, airportCode);
		return optionList;
	}

	public ArrayList allFreshOptionReport(String frmName,String region,String airportCode) {
		ArrayList optionList1 = new ArrayList();
		optionList1 = personalDAO.allFreshOptionReports(frmName, region, airportCode);
		return optionList1;
	}
	 public ArrayList freshOptionSummaryReport() {
			ArrayList list = new ArrayList();
			list = personalDAO.freshSummaryReport();
			return list;
	}

}
