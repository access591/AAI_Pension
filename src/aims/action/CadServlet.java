package aims.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.SearchInfo;
import aims.common.CommonUtil;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.dao.FinancialReportDAO;
import aims.service.CadService;
import aims.service.PersonalService;

public class CadServlet extends HttpServlet {
	Log log = new Log(CadServlet.class);

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		CommonUtil commonUtil = new CommonUtil();
		CadService cadService = new CadService();
		PersonalService personalService = new PersonalService();
		HttpSession session = request.getSession();
		CommonDAO commonDAO = new CommonDAO();

		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("verifypcrpt")) {
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CadInputParam.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("mappingNavigation")) {
			String navgationBtn = "", empNameCheck = "";
			int strtIndex = 0, totalRecords = 0;
			EmpMasterBean empSerach = new EmpMasterBean();
			if (request.getParameter("employeeName") != null) {
				empSerach.setEmpName(request.getParameter("employeeName")
						.toString().trim());
			}
			if (request.getParameter("region") != null) {
				empSerach.setRegion(request.getParameter("region").toString()
						.trim());
			}
			if (request.getParameter("cpfaccno") != null) {
				empSerach.setCpfAcNo(request.getParameter("cpfaccno")
						.toString().trim());
			}
			if (request.getParameter("employeeCode") != null) {
				empSerach.setEmpNumber(request.getParameter("employeeCode")
						.toString().trim());
			}
			SearchInfo getSearchInfo = new SearchInfo();
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				getSearchInfo.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = (String) session.getAttribute("navButton");
				getSearchInfo.setNavButton(navgationBtn);
			}

			if (request.getParameter("strtindx") != null) {
				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				getSearchInfo.setStartIndex(strtIndex);
			}
			if (request.getParameter("pfidfrom") != null) {
				empSerach.setPfidfrom(request.getParameter("pfidfrom"));
			} else {
				empSerach.setPfidfrom("");
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				getSearchInfo.setTotalRecords(totalRecords);
			}
			if (session.getAttribute("getSearchBean1") != null) {
				empSerach = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}
			if (request.getParameter("empNameCheak") != null) {
				empNameCheck = request.getParameter("empNameCheak").toString()
						.trim();
			}
			if (request.getParameter("unmappedFlag") != null) {
				empSerach.setUnmappedFlag(request.getParameter("unmappedFlag")
						.toString().trim());
			}
			if (request.getParameter("allRecordsFlag") != null) {
				empSerach.setAllRecordsFlag(request.getParameter(
						"allRecordsFlag").toString().trim());
			}

			if (request.getParameter("reportType") != null) {
				empSerach.setReportType(request.getParameter("reportType")
						.toString().trim());
				request.setAttribute("reportType", empSerach.getReportType());
			}
			try {
				getSearchInfo = cadService.financeDataSearch(empSerach,
						getSearchInfo, 100, empNameCheck, empSerach
								.getReportType(), empSerach.getPfidfrom());

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("empNameChecked", empNameCheck);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");

			if (request.getParameter("allRecordsFlag") != null) {
				rd = request
						.getRequestDispatcher("./PensionView/CadInputParam.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financeDataReport")) {
			String reporttype = "";
			EmpMasterBean empSerach = new EmpMasterBean();

			if (session.getAttribute("getSearchBean1") != null) {
				empSerach = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}

			if (request.getParameter("reportType") != null) {
				reporttype = request.getParameter("reportType").toString()
						.trim();
			}
			SearchInfo getSearchInfo = new SearchInfo();
			log.info(" CadServlet ");
			getSearchInfo = cadService.financeDataReport(empSerach,
					getSearchInfo, 100, empSerach.getEmpNameCheak(),
					reporttype, empSerach.getPfidfrom());
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("reportType", reporttype);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/VerificationFinanceDataReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financeDataSearch")) {
			
			String empNameCheck = "",approvedStatus="";
			EmpMasterBean empSerach = new EmpMasterBean();

			if (request.getParameter("pfid") != null) {
				empSerach.setPfid(commonUtil.getSearchPFID1(request
						.getParameter("pfid").toString().trim()));
			}
			if (request.getParameter("employeeName") != null) {
				empSerach.setEmpName(request.getParameter("employeeName")
						.toString().trim());
			}
			if (request.getParameter("region") != null) {
				empSerach.setRegion(request.getParameter("region").toString()
						.trim());
			}
			if (request.getParameter("cpfaccno") != null) {
				empSerach.setCpfAcNo(request.getParameter("cpfaccno")
						.toString().trim());
			}
			if (request.getParameter("employeeCode") != null) {
				empSerach.setEmpNumber(request.getParameter("employeeCode")
						.toString().trim());
			}
			if (request.getParameter("empNameCheak") != null) {
				empNameCheck = request.getParameter("empNameCheak").toString()
						.trim();
				empSerach.setEmpNameCheak(empNameCheck);
			}
			if (request.getParameter("unmappedFlag") != null) {
				empSerach.setUnmappedFlag(request.getParameter("unmappedFlag")
						.toString().trim());
				request.setAttribute("unmappedFlag", empSerach
						.getUnmappedFlag());
			}
			if (request.getParameter("allRecordsFlag") != null) {
				empSerach.setAllRecordsFlag(request.getParameter(
						"allRecordsFlag").toString().trim());
			}
			if (request.getParameter("reportType") != null) {
				empSerach.setReportType(request.getParameter("reportType")
						.toString().trim());
				request.setAttribute("reportType", empSerach.getReportType());
			}
			if (request.getParameter("select_airport") != null) {
				empSerach.setStation(request.getParameter("select_airport")
						.toString().trim());
			}
			if (request.getParameter("pfidfrom") != null) {
				empSerach.setPfidfrom(request.getParameter("pfidfrom"));
			} else {
				empSerach.setPfidfrom("");
			}
			if (request.getParameter("pcverified") != null) {
				empSerach.setPcverified(request.getParameter("pcverified")
						.toString().trim());
			}
			if (request.getParameter("pensioncliamsProcess") != null) {
				empSerach.setClaimsprocess(request.getParameter(
						"pensioncliamsProcess").toString().trim());
			}
			
			if (request.getParameter("apStatus") != null) {
				approvedStatus = request.getParameter("apStatus");
			}
			
			if(approvedStatus.equals("Approved")){
				log.info(" inside Approved ");
				String pensionno = request.getParameter("pfid");
				cadService.updateCadTracking(pensionno, approvedStatus);	
				log.info("After Approved");
			}
			SearchInfo getSearchInfo = new SearchInfo();
			getSearchInfo = cadService.financeDataSearch(empSerach,
					getSearchInfo, 100, empNameCheck,
					empSerach.getReportType(), empSerach.getPfidfrom());
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("empNameChecked", empNameCheck);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CadInputParam.jsp");
			if (request.getParameter("reportType") != null
					&& request.getParameter("reportType") != "") {
				log.info(" report ");
				rd = request
						.getRequestDispatcher("./PensionView/CadInputParam.jsp");
			}
			if (request.getParameter("allRecordsFlag") != null) {
				if (request.getParameter("allRecordsFlag").equals("true")) {
					log.info(" allRecordsFlag ");
					rd = request
							.getRequestDispatcher("./PensionView/CadInputParam.jsp");
				}
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financeDataSearch")) {
			String empNameCheck = "";
			EmpMasterBean empSerach = new EmpMasterBean();

			if (request.getParameter("pfid") != null) {
				empSerach.setPfid(commonUtil.getSearchPFID1(request
						.getParameter("pfid").toString().trim()));
			}
			if (request.getParameter("employeeName") != null) {
				empSerach.setEmpName(request.getParameter("employeeName")
						.toString().trim());
			}
			if (request.getParameter("region") != null) {
				empSerach.setRegion(request.getParameter("region").toString()
						.trim());
			}
			if (request.getParameter("cpfaccno") != null) {
				empSerach.setCpfAcNo(request.getParameter("cpfaccno")
						.toString().trim());
			}
			if (request.getParameter("employeeCode") != null) {
				empSerach.setEmpNumber(request.getParameter("employeeCode")
						.toString().trim());
			}
			if (request.getParameter("empNameCheak") != null) {
				empNameCheck = request.getParameter("empNameCheak").toString()
						.trim();
				empSerach.setEmpNameCheak(empNameCheck);
			}
			if (request.getParameter("unmappedFlag") != null) {
				empSerach.setUnmappedFlag(request.getParameter("unmappedFlag")
						.toString().trim());
				request.setAttribute("unmappedFlag", empSerach
						.getUnmappedFlag());
			}
			if (request.getParameter("allRecordsFlag") != null) {
				empSerach.setAllRecordsFlag(request.getParameter(
						"allRecordsFlag").toString().trim());
			}
			if (request.getParameter("reportType") != null) {
				empSerach.setReportType(request.getParameter("reportType")
						.toString().trim());
				request.setAttribute("reportType", empSerach.getReportType());
			}
			if (request.getParameter("select_airport") != null) {
				empSerach.setStation(request.getParameter("select_airport")
						.toString().trim());
			}
			if (request.getParameter("pfidfrom") != null) {
				empSerach.setPfidfrom(request.getParameter("pfidfrom"));
			} else {
				empSerach.setPfidfrom("");
			}
			if (request.getParameter("pcverified") != null) {
				empSerach.setPcverified(request.getParameter("pcverified")
						.toString().trim());
			}
			if (request.getParameter("pensioncliamsProcess") != null) {
				empSerach.setClaimsprocess(request.getParameter(
						"pensioncliamsProcess").toString().trim());
			}
			SearchInfo getSearchInfo = new SearchInfo();
			getSearchInfo = cadService.financeDataSearch(empSerach,
					getSearchInfo, 100, empNameCheck,
					empSerach.getReportType(), empSerach.getPfidfrom());
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("empNameChecked", empNameCheck);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");
			if (request.getParameter("reportType") != null
					&& request.getParameter("reportType") != "") {
				rd = request
						.getRequestDispatcher("./PensionView/FinanceDataMappingReport.jsp");
			}
			if (request.getParameter("allRecordsFlag") != null) {
				if (request.getParameter("allRecordsFlag").equals("true")) {
					rd = request
							.getRequestDispatcher("./PensionView/CadInputParam.jsp");
				}
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("personalEmpReport")) {
			EmployeePersonalInfo personal = new EmployeePersonalInfo();
			ArrayList personalReportList = new ArrayList();
			String reportType = "", region = "", sortingColumn = "";
			if (session.getAttribute("getSearchBean1") != null) {
				personal = (EmployeePersonalInfo) session
						.getAttribute("getSearchBean1");
			}
			if (request.getParameter("reportType") != null) {
				reportType = request.getParameter("reportType");
			}
			if (!personal.getRegion().equals("")) {
				if (personal.getRegion().equals("NO-SELECT")) {
					region = "All Regions";
				} else {
					region = personal.getRegion();
				}
			}
			if (request.getParameter("frm_sortcolumn") != null) {
				sortingColumn = request.getParameter("frm_sortcolumn");
			}
			personalReportList = personalService.personalReport(personal,
					sortingColumn, reportType);
			request.setAttribute("reportList", personalReportList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			RequestDispatcher rd = null;
			if (reportType.equals("DBF")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalInfoReportDBF.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalInfoReport.jsp");
			}
			rd.forward(request, response);
		}

		else if (request.getParameter("method").equals("formsDisable")) {

			String pensionno = "", formsstatus = "", pcreportstatus = "", pensionclaimstatus = "";
			log.info("delete items  "
					+ request.getParameterValues("mappingflag"));
			String[] unmappedRecords = {};
			String[] deleteTransactions = {};
			if (request.getParameterValues("mappingflag") != null) {
				unmappedRecords = (String[]) request
						.getParameterValues("mappingflag");
			}
			if (request.getParameter("formsstatus") != null) {
				formsstatus = request.getParameter("formsstatus");
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("pcreportstatus") != null) {
				pcreportstatus = request.getParameter("pcreportstatus");
			}
			if (request.getParameter("pensionclaimstatus") != null) {
				pensionclaimstatus = request.getParameter("pensionclaimstatus");
			}

			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			cadService.formsDisable(pensionno, formsstatus, pcreportstatus,
					pensionclaimstatus, userName, ipAddress);

			StringBuffer sb = new StringBuffer();
			if (formsstatus != "") {
				sb.append("Form 6-7-8 Status Changed to " + formsstatus);
			}
			if (pensionclaimstatus != "") {
				sb.append("PensionClaim Process Status Changed to "
						+ pensionclaimstatus);
			}
			if (pcreportstatus != "") {
				sb.append("PCReport Verified Status Changed to "
						+ pcreportstatus);
			}
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method").equals("checkDataFreeze")) {
			String monthYear = "", airportcode = "", region = "";

			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}

			if (request.getParameter("airportcode") != null) {
				airportcode = request.getParameter("airportcode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}

			FinancialReportDAO fdao = new FinancialReportDAO();
			int count = fdao.checkDataFreeze(monthYear, airportcode, region);
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String designation = "";
			sb.append("<ServiceType>");
			sb.append("<count>");
			sb.append(count);
			sb.append("</count>");
			sb.append("</ServiceType>");

			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			System.out.println(sb.toString());
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method").equals("editTransactionData")) {
			log.info(" editTransactionData ");
			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00", contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "";
			String pensionoption = "",addContri="",benfundcontr="",benfundPension="",gratuityInterest="",epfoPension="";
			String recoverieTable = "";
			if (request.getParameter("addContri") != null) {
				addContri = request.getParameter("addContri");
			}
			
			if (request.getParameter("benfundcontr") != null) {
				benfundcontr = request.getParameter("benfundcontr");
			}
			
			if (request.getParameter("benfundPension") != null) {
				benfundPension = request.getParameter("benfundPension");
			}
			if (request.getParameter("gratuityInterest") != null) {
				gratuityInterest = request.getParameter("gratuityInterest");
			}
			if (request.getParameter("epfoPension") != null) {
				epfoPension = request.getParameter("epfoPension");
			}
			
			log.info(" AdditionalContribution "+ addContri +" BenfundPension "+ benfundPension +" GratuityInterestThereon "+gratuityInterest+" EpfoPension "+epfoPension);
			
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter(
						"pensionNo").toString());
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfAccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("emoluments") != null) {
				emoluments = request.getParameter("emoluments");
			}
			if (request.getParameter("epf") != null) {
				epf = request.getParameter("epf");
			}
				System.out.println(" epf ######## "+epf);
			if (request.getParameter("vpf") != null) {
				vpf = request.getParameter("vpf");
			}
			if (request.getParameter("principle") != null) {
				principle = request.getParameter("principle");
			}
			if (request.getParameter("interest") != null) {
				interest = request.getParameter("interest");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("airportcode") != null) {
				airportcode = request.getParameter("airportcode");
			}
			if (request.getParameter("contri") != null) {
				contri = request.getParameter("contri");
			}
			if (request.getParameter("advance") != null) {
				advance = request.getParameter("advance");
			}
			if (request.getParameter("loan") != null) {
				loan = request.getParameter("loan");
			}
			if (request.getParameter("aailoan") != null) {
				aailoan = request.getParameter("aailoan");
			}
			if (request.getParameter("from7narration") != null) {
				from7narration = request.getParameter("from7narration");
			}
			if (request.getParameter("pcheldamt") != null) {
				pcheldamt = request.getParameter("pcheldamt");
			}
			if (request.getParameter("noofmonths") != null) {
				noofmonths = request.getParameter("noofmonths");
			}
			if (request.getParameter("arrearfalg") != "") {
				arrearflag = request.getParameter("arrearflag");
			}

			if (request.getParameter("editid") != null) {
				editid = request.getParameter("editid");
			}
			if (request.getParameter("duputationflag") != null) {
				duputationflag = request.getParameter("duputationflag");
			}
			String ComputerName = session.getAttribute("computername")
					.toString();
			String username = session.getAttribute("userid").toString();
			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			if (request.getParameter("pensionoption") != null) {
				pensionoption = request.getParameter("pensionoption")
						.toString();
			}
			if (request.getParameter("contri") != null) {
				contri = request.getParameter("contri");
			}
				log.info(" contri @@ "+contri);
			if (request.getParameter("recoverieTable") != null) {
				recoverieTable = request.getParameter("recoverieTable");
			}
			cadService.editTransactionData(cpfAccno, monthYear, emoluments,
					vpf, principle, interest, contri, advance, loan, aailoan,
					pfid, region, airportcode, username, ComputerName,
					from7narration, pcheldamt, noofmonths, arrearflag,
					duputationflag, pensionoption, recoverieTable, epf,benfundcontr,benfundPension,gratuityInterest,epfoPension,addContri);

			int insertedRec = cadService.preProcessAdjOB(pfid);

			String reportType = "Html";
			String yearID = "NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip + "&page=" + page + "&mappingFlag="
					+ mappingFlag;

			String url = "./CadServlet?method=getReportPenContr" + params;
			log.info(" url is " + url + " editid " + editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);

		} else if (request.getParameter("method").equals(
				"deleteTransactionData")) {

			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "";
			log.info("delete items  " + request.getParameterValues("cpfno"));
			String[] deletedmonths = {};
			String[] deleteTransactions = {};
			if (request.getParameterValues("cpfno") != null) {
				deletedmonths = (String[]) request.getParameterValues("cpfno");
			}
			for (int i = 0; i < deletedmonths.length; i++) {
				log.info(deletedmonths[i]);
				deleteTransactions = deletedmonths[i].split(",");
				for (int j = 0; j < deleteTransactions.length; j++) {
					monthYear = deleteTransactions[0].substring(1,
							deleteTransactions[0].length() - 1);
					cpfAccno = deleteTransactions[1].substring(1,
							deleteTransactions[1].length() - 1);
					region = deleteTransactions[2].substring(1,
							deleteTransactions[2].length() - 1);
					airportcode = deleteTransactions[3].substring(1,
							deleteTransactions[3].length() - 1);

				}

				if (request.getParameter("pfid") != null) {
					pfid = request.getParameter("pfid");
				}
				String ComputerName = session.getAttribute("computername")
						.toString();
				String username = session.getAttribute("userid").toString();

				pfid = commonUtil.trailingZeros(pfid.toCharArray());
				cadService.deleteTransactionData(cpfAccno, monthYear, region,
						airportcode, ComputerName, username, pfid);
			}
			int insertedRec = cadService.preProcessAdjOB(pfid);

			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region = "NO-SELECT";
			String pfidStrip = "1 - 1";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip;

			String url = "./CadSservlet?method=getReportPenContr" + params;
			log.info("url is " + url);

			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getContribution")) {
			String monthYear = "", pfid = "", emoluments = "", dob = "", penionOption = "", contributionbox = "";
			if (request.getParameter("pfid") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter("pfid")
						.toString());
			}
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("emoluments") != null) {
				emoluments = request.getParameter("emoluments");
			}
			if (request.getParameter("dob") != null) {
				dob = request.getParameter("dob");
			}
			if (request.getParameter("wetheroption") != null) {
				penionOption = request.getParameter("wetheroption");
			}
			if (request.getParameter("pfid") != null) {
				pfid = request.getParameter("pfid");
			}
			if (request.getParameter("contributionbox") != null) {
				contributionbox = request.getParameter("contributionbox");
			}

			FinancialReportDAO fdao = new FinancialReportDAO();
			double pensionContribution = commonDAO.pensionCalculation(
					monthYear, emoluments, penionOption, "", "");
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String designation = "";
			sb.append("<ServiceType>");
			sb.append("<pensionContribution>");
			sb.append(Math.round(pensionContribution));
			sb.append("</pensionContribution>");
			sb.append("<contributionbox>");
			sb.append(contributionbox);
			sb.append("</contributionbox>");
			sb.append("</ServiceType>");

			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method").equals(
				"editReInterestcalcupto")) {

			String reinterestCalcdate = "", pfid = "", recoverieTable = "";
			String editid = "";
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter(
						"pensionNo").toString());
			}
			if (request.getParameter("reinterestCalcdate") != null) {
				reinterestCalcdate = request.getParameter("reinterestCalcdate");
			}
			if (request.getParameter("recoverieTable") != null) {
				recoverieTable = request.getParameter("recoverieTable");
			}

			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			String userId = session.getAttribute("userid").toString();
			cadService.editReInterestCalc(reinterestCalcdate, pfid, userId,
					recoverieTable);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			sb.append("<ServiceType>");
			sb.append("<pfid>");
			sb.append(pfid.trim());
			sb.append("</pfid>");
			sb.append("</ServiceType>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method").equals(
				"editInterestCalcuptoDate")) {

			String interestcalcDate = "", pfid = "", recoverieTable = "";
			String editid = "";
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter(
						"pensionNo").toString());
			}
			if (request.getParameter("interestCalcdate") != null) {
				interestcalcDate = request.getParameter("interestCalcdate");
			}
			if (request.getParameter("recoverieTable") != null) {
				recoverieTable = request.getParameter("recoverieTable");
			}

			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			String userId = session.getAttribute("userid").toString();
			cadService.editInterestCalc(interestcalcDate, pfid, userId,
					recoverieTable);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			sb.append("<ServiceType>");
			sb.append("<pfid>");
			sb.append(pfid.trim());
			sb.append("</pfid>");
			sb.append("</ServiceType>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method").equals("editFinalDate")) {

			String finalsettlementDate = "", pfid = "";
			String editid = "";
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter(
						"pensionNo").toString());
			}

			if (request.getParameter("finalsettlementDate") != null) {
				finalsettlementDate = request
						.getParameter("finalsettlementDate");
			}

			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			String userId = session.getAttribute("userid").toString();
			cadService.editFinalDate(finalsettlementDate, pfid, userId);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			sb.append("<ServiceType>");
			sb.append("<pfid>");
			sb.append(pfid.trim());
			sb.append("</pfid>");
			sb.append("</ServiceType>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method").equals("getReportPenContr")) {
			
			String region = "", finYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "", empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "", chkBulkPrint = "";
			String recoverieTable = "";
			String interestCalc = "", reinterestcalcdate = "",finalflag="";
			String getEditedPensionno = "",status="";
			ArrayList pensionContributionList = new ArrayList();
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			}
			if (request.getParameter("interestcalcUpto") != null) {
				interestCalc = request.getParameter("interestcalcUpto");
			}
			if (request.getParameter("reinterestcalc") != null) {
				reinterestcalcdate = request.getParameter("reinterestcalc");
			}
			// log.info("reinterestcalc" + reinterestcalcdate);
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			}
			// log.info("Search Servlet" + pfidString);
			if (request.getParameter("cpfAccno") != null) {
				cpfAccno = request.getParameter("cpfAccno");
			}
			if (request.getParameter("transferStatus") != null) {
				transferFlag = request.getParameter("transferStatus");
			}
			String mappingFlag = "true";
			if (request.getParameter("mappingFlag") != null) {
				mappingFlag = request.getParameter("mappingFlag");

			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
				// region="NO-SELECT";
			} else {
				region = "NO-SELECT";
			}
			
			if (request.getParameter("status") != null && request.getParameter("finalflag") != null) {
				status = request.getParameter("status");
				// region="NO-SELECT";
			} else {
				status = "";
			}
			log.info(" status "+status);
			log.info(" From Date ***** " + request.getParameter("frm_year"));
			log.info(" From finalize ***** " + request.getParameter("final_data"));
			if (request.getParameter("frm_year") != null) {
				finYear = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_month") != null) {
				selectedMonth = request.getParameter("frm_month");
			}
			if (request.getParameter("empserialNO") != null) {
				empserialNO = commonUtil.getSearchPFID1(request.getParameter(
						"empserialNO").toString().trim());
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportCode = request.getParameter("frm_airportcode");

			} else {
				airportCode = "NO-SELECT";
			}
			if (request.getParameter("frm_toyear") != null) {
				toYear = request.getParameter("frm_toyear");

			} else {
				toYear = "";
			}

			if (request.getParameter("frm_bulkprint") != null) {
				chkBulkPrint = request.getParameter("frm_bulkprint");
			}
			
			if (request.getParameter("final_data") != null) {
				finalflag = request.getParameter("final_data");
				log.info(" finalflag if "+finalflag);

			} else {
				finalflag = "";
				log.info(" finalflag  else "+finalflag);
			}
			
			log.info(" finalflag  "+finalflag);
			
			if (request.getParameter("finalrecoverytableFlag") != null) {
				recoverieTable = request.getParameter("finalrecoverytableFlag");
				log.info(" recoverieTable " + recoverieTable);
			}

			pensionContributionList = cadService.getPensionContributionReport(
					toYear, finYear, region, airportCode, selectedMonth,
					empserialNO, cpfAccno, transferFlag, mappingFlag,
					pfidString, chkBulkPrint, recoverieTable);
			double interestforNoofMonths = cadService
					.getInterestforNoofMonths(interestCalc);
			String reIntrestDate = cadService.reIntrestDate(empserialNO);
			double interestforfinalsettleMonths = cadService
					.interestforfinalsettleMonths(reinterestcalcdate,
							empserialNO);
			String finalintrestdate = cadService.finalintrestdate(
					reinterestcalcdate, empserialNO);
			String reSettlementdate = cadService.reSettlementdate(empserialNO);
			log.info("reIntrestDate" + reIntrestDate);
			log.info("reSettlementdate" + reSettlementdate);
			double pctotal = 0.0;
			double intrest = 0.0;
			if (recoverieTable.equals("true")) {
				pctotal = cadService.getPensionContributionTotal(empserialNO);
				intrest = cadService.getfinalrevoveryintrest(empserialNO);
				getEditedPensionno = cadService.getEditedPensionno(empserialNO);
			}
			log.info("pensionContributionList "
					+ pensionContributionList.size());
			request.setAttribute("penContrList", pensionContributionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("blkprintflag", chkBulkPrint);
			request.setAttribute("stationStr", airportCode);
			request.setAttribute("regionStr", region);
			request.setAttribute("pctotal", new Double(pctotal));
			request.setAttribute("intrest", new Double(intrest));
			request.setAttribute("recoverieTable", recoverieTable);
			request.setAttribute("interestCalcfinal", interestCalc);
			request.setAttribute("finalintdate", finalintrestdate);
			request.setAttribute("reIntrestcalcDate", reIntrestDate);
			request.setAttribute("getEditPenno", getEditedPensionno);
			request.setAttribute("resettledate", reSettlementdate);
			request.setAttribute("interestforNoofMonths", String
					.valueOf(interestforNoofMonths));
			request.setAttribute("interestforfinalsettleMonths", String
					.valueOf(interestforfinalsettleMonths));	
			request.setAttribute("finalizeFlag", finalflag);	
			
			log.info(" finaldata "+finalflag);
			if (page.equals("")) {
				log.info("-page-" + page);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/CadReport.jsp");
				rd.forward(request, response);
			} else if (page.equals("PensionContribution")) {
				log.info("-PensionContribution-" + page);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/CadReport.jsp");
				rd.forward(request, response);
			} 	else {
				log.info("  @@@ " + page);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/CardReportScreen.jsp?recoverieTable="
								+ recoverieTable);
				rd.forward(request, response);
			}
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("cadAllYearReport")) {
				
			String region = "", year = "", adjFlag = "",selectyear="";
			String airportcode = "", reportType = "", sortingOrder = "pensionno", pfidString = "", formType = "", fileheadname = "", frmName = "", pcFlag="false";
			ArrayList list = new ArrayList();
			ArrayList revisedList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_emp_flag") != null) {
				region = request.getParameter("frm_emp_flag");
			}

			
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
				selectyear = request.getParameter("frm_year");
				fileheadname = year;
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			}
			log.info("Search Servlet" + pfidString);

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			String empName = "", airportCode = "";
			String empflag = "", pensionno = "";
			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("adjFlag") != null) {
				adjFlag = request.getParameter("adjFlag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("pcFlag") != null) {
				pcFlag = request.getParameter("pcFlag");
			}
			String formArrRvsFormFlag = " ";

			boolean chkMultipleYearFlag = false;
			log.info("pfidString" + pfidString + "year" + year + "sortingOrder"
					+ sortingOrder + "region" + region + "airportCode"
					+ airportCode + " pensionno " + pensionno +"formType"+formType+" empflag "+empflag);
			String formVal = " ";
			
			if (formType.equals("FORM-7PS-SUMMARY")
					|| formType.equals("FORM-7PS-SUMMARY[REV]")) {
				formVal = "Summary";
				formArrRvsFormFlag = "N";
			} else if (formType.equals("FORM-7PS-REVISED")) {
				formVal = "Revised";
				formArrRvsFormFlag = "Y";

			} else {
				formVal = "NonSummary";
				formArrRvsFormFlag = "N";
			}
			ArrayList revisedDataList = new ArrayList();
            log.info("selectyear=="+selectyear);

				list = cadService.CpfData(
						pfidString, year, sortingOrder, region, airportCode,
						pensionno, empflag, empName, formVal,
						formArrRvsFormFlag, adjFlag, frmName,pcFlag,selectyear);
				String minMaxYear = "";
				
				log.info("formVal" + formVal + "revisedDataList"
						+ revisedDataList.size());
				chkMultipleYearFlag = true;

				year = "1995";


			request.setAttribute("form8List", list);
			log.info("==========Form-7=====" + region + "year" + year);
			String nextYear = "", shnYear = "", xlsPurspose = "";
			if (chkMultipleYearFlag == false) {
				int nxtYear = Integer.parseInt(year) + 1;
				nextYear = Integer.toString(nxtYear);
				shnYear = "01-Apr-" + year + " To Mar-" + nextYear;
				xlsPurspose = pfidString + "_" + year + "_" + nextYear;
			} else {
				shnYear = "";
				xlsPurspose = "";
			}

			request.setAttribute("chkYear", year);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			request.setAttribute("adjFlag", adjFlag);
			RequestDispatcher rd = null;
			log.info("formType==============" + formType
					+ "chkMultipleYearFlag" + chkMultipleYearFlag);
			if (list.size() != 0) {
				String fileNameMessage = "";
				/*if (!pensionno.equals("") && !fileheadname.equals("NO-SELECT")) {
					fileNameMessage = "PFID_" + pensionno + "_(" + fileheadname
							+ "-" + (Integer.parseInt(fileheadname) + 1) + ")";
				} else if (!pensionno.equals("")) {
					fileNameMessage = "PFID_" + pensionno;
				}*/
				
				request.setAttribute("fileNameMessage", fileNameMessage);
				rd = request.getRequestDispatcher("./PensionView/CadAllYearReport.jsp");

				} else {
				String messages = "";
				if (!pensionno.equals("")) {
					messages = "Pension no " + pensionno;
				} else if (!year.equals("")) {
					messages = "year " + year;
				} else if (!pensionno.equals("") && !year.equals("")) {
					messages = "Pension no " + pensionno + " for the FYI:"
							+ year;
				}
				messages = "No Records found for the " + messages;
				request.setAttribute("message", messages);
				rd = request.getRequestDispatcher("./PensionView/Message.jsp");
			}
			rd.forward(request, response);
		}	
		
		

		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {
			String region = "";
			region = request.getParameter("region");

			ArrayList ServiceType = null;
			ServiceType = commonUtil.getAirportsByFinanceTbl(region);
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");

			for (int i = 0; ServiceType != null && i < ServiceType.size(); i++) {
				String name = "";
				EmpMasterBean bean = (EmpMasterBean) ServiceType.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				if (bean.getStation() != null)
					name = bean.getStation().replaceAll("<", "&lt;")
							.replaceAll(">", "&gt;");
				sb.append(name);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		}

	}

	private String getCurrentDate(String format) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		date = sdf.format(new Date());
		return date;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
}
