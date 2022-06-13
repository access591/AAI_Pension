package aims.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import aims.bean.EmpMasterBean;
import aims.bean.PensionBean;
import aims.bean.PensionContBean;
import aims.bean.adjcrnt.AdjCrntSaveDtlBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.AdjCrtnDAO;
import aims.dao.CommonDAO;
import aims.service.AdjCrtnService;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PCService;
import aims.service.PensionService;

public class PCReportServlet extends HttpServlet {

	Log log = new Log(PCReportServlet.class);

	CommonUtil commonUtil = new CommonUtil();
	FinancialReportService finReportService = new FinancialReportService();
	PCService pcs = new PCService();
	PensionService ps = new PensionService();
	FinancialService financeService = new FinancialService();
	AdjCrtnService adjCrtnService = new AdjCrtnService();
	CommonDAO commonDAO = new CommonDAO();
	AdjCrtnDAO adjCrtnDAO = new AdjCrtnDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info("PCreportServlet : doPost ");
		HttpSession session = request.getSession();
		if (request.getParameter("method").equals("getReportRevPenContr")) {
			log.info("1");
			String region = "", finYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "",
					empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "", chkBulkPrint = "",
					sec_flag = "";
			String recoverieTable = "";
			String interestCalc = "", reinterestcalcdate = "";
			String getEditedPensionno = "", reportFormatType = "";
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
			log.info("reinterestcalc" + reinterestcalcdate);
			if (request.getParameter("frm_pfids") != null) {
				
				pfidString = request.getParameter("frm_pfids");
			}
			log.info("Search Servlet" + pfidString);
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
			log.info("region in servelt ***** " + request.getParameter("frm_year"));
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
				
				empserialNO = commonUtil.getSearchPFID1(request.getParameter("empserialNO").toString().trim());
			}
			// /
			// empserialNO=commonUtil.trailingZeros(empserialNO.toCharArray());
			if (request.getParameter("frm_reportType") != null) {
				
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("reportFormatType") != null) {
				
				reportFormatType = request.getParameter("reportFormatType");
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
			if (request.getParameter("reportFlag") != null) {
				
				sec_flag = request.getParameter("reportFlag");
			}
			// for cheking flag for which recoverie table to hit
			if (request.getParameter("finalrecoverytableFlag") != null) {
				
				recoverieTable = request.getParameter("finalrecoverytableFlag");
			}

			String userId = (String) session.getAttribute("userid");

			log.info("formType::::::::::" + formType + userId + sec_flag);

			if (formType.equals("edit")) {
				if (userId.equals("GOPALPAL") && sec_flag.equals("Normal")) {
					
					pensionContributionList = pcs.getRevisionPCReport(toYear, finYear, region, airportCode,
							selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
							"edit");
				} else if (userId.equals("GOPALPAL") || sec_flag.equals("SEC")) {
					
					pensionContributionList = pcs.getRevisionPCReportSecLvl(toYear, finYear, region, airportCode,
							selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
							"edit");
				} else {
					
					pensionContributionList = pcs.getRevisionPCReport(toYear, finYear, region, airportCode,
							selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
							"edit");
				}
			} else {
				if (sec_flag.equals("SEC")) {
					pensionContributionList = pcs.getRevisionPCReportSecLvl(toYear, finYear, region, airportCode,
							selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
							recoverieTable);
				} else {
					pensionContributionList = pcs.getRevisionPCReport(toYear, finYear, region, airportCode,
							selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
							recoverieTable);
				}
			}
			
			double interestforNoofMonths = pcs.getInterestforNoofMonths(interestCalc);
			
			String reIntrestDate = pcs.reIntrestDate(empserialNO);
			double interestforfinalsettleMonths = pcs.interestforfinalsettleMonths(reinterestcalcdate, empserialNO);
			
			String finalintrestdate = pcs.finalintrestdate(reinterestcalcdate, empserialNO);
			String reSettlementdate = pcs.reSettlementdate(empserialNO);
			log.info("reIntrestDate" + reIntrestDate);
			log.info("reSettlementdate" + reSettlementdate);
			double pctotal = 0.0;
			double intrest = 0.0;
			
			String retireddate = "";
			if (recoverieTable.equals("true")) {
				
				pctotal = financeService.getPensionContributionTotal(empserialNO);
				intrest = financeService.getfinalrevoveryintrest(empserialNO);
				getEditedPensionno = financeService.getEditedPensionno(empserialNO);
			}
			
			log.info("pensionContributionList " + pensionContributionList.size());
			
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
			request.setAttribute("interestforNoofMonths", String.valueOf(interestforNoofMonths));
			request.setAttribute("interestforfinalsettleMonths", String.valueOf(interestforfinalsettleMonths));
			
			log.info("----" + request.getAttribute("interestCalcfinal").toString());
			RequestDispatcher rd = null;
			if (formType.equals("edit")) {

				
				if (reportFormatType.equals("NEWREPORT")) {
					
					rd = request.getRequestDispatcher("./PensionView/RevisionOptionEditPCReportNew.jsp");
				} else {
					
					rd = request.getRequestDispatcher("./PensionView/RevisionOptionEditPCReport.jsp");
				}
			} else {
				
				rd = request.getRequestDispatcher("./PensionView/RevisionOptionPCReport.jsp");
			}
			rd.forward(request, response);

		} 
		
		
		
		
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getEmployeeMappedList")) {
			
			
			log.info("else if 269");
			
			
			log.info("getEmployeeMappedList () ");
			String path = "", screen = "";
			String region = request.getParameter("region").toString();
			if (region.equals("[Select One]")) {
				
				region = "";
			}
			
			String empName = request.getParameter("empName").toString().trim();
			String pfId = request.getParameter("pfId").toString().trim();
			if (request.getParameter("screen") != null) {
				;
				screen = request.getParameter("screen").toString().trim();
			}
			
			pfId = commonUtil.getSearchPFID1(pfId.toString().trim());
			String transferType = "", adjPfidChkFlag = "", chkpfid = "", statemntRevisdChkFlag = "",
					statemntRevisdChkPfid = "", frmName = "", PCAftrSepFlag = "", pcReportChkFlag = "";
			
			if (request.getParameter("transferType") != null) {
				
				transferType = request.getParameter("transferType").toString();
			}
			if (request.getParameter("adjPfidChkFlag") != null) {
				
				adjPfidChkFlag = request.getParameter("adjPfidChkFlag").toString();
			}
			if (request.getParameter("statemntRevisdChkFlag") != null) {
				
				statemntRevisdChkFlag = request.getParameter("statemntRevisdChkFlag").toString();
			}
			if (request.getParameter("frmName") != null) {
				
				frmName = request.getParameter("frmName").toString();
			}
			if (request.getParameter("pcReportChkFlag") != null) {
				
				pcReportChkFlag = request.getParameter("pcReportChkFlag").toString();
			}

			ArrayList mappedList = new ArrayList();
			ArrayList employeeinfolist = new ArrayList();
			boolean flag = true;

			try {

				
				PensionBean empData = null;
				for (int i = 0; i < mappedList.size(); i++) {
					log.info("52");
					empData = (PensionBean) mappedList.get(i);
				}

				
				employeeinfolist = ps.getFreshOptionList(pfId);

				request.setAttribute("EmpInfoList", employeeinfolist);
				System.out.println("employeeinfolist----" + employeeinfolist.size());

				log.info("adjPfidChkFlag----" + adjPfidChkFlag + screen);
				if (adjPfidChkFlag.equals("true")) {
					
					if (pcReportChkFlag.equals("true")) {
						
						// For Checking exact Corrections in 1995-2008 period
						chkpfid = adjCrtnService.chkPfidStatusInAdjCrtnForPCReport(pfId);
					} else {
						
						chkpfid = adjCrtnService.chkPfidStatusInAdjCrtn(pfId);
					}
				}

				if (chkpfid.equals("Approved")) {
					
					chkpfid = "Exists";
				} else {
					
					chkpfid = "NotExists";
				}
				// For 12 Mnth Statement Chking purpose
				log.info("statemntRevisdChkFlag----" + statemntRevisdChkFlag);
				if (statemntRevisdChkFlag.equals("true")) {
					
					statemntRevisdChkPfid = adjCrtnService.chkPfidinAdjCrtn78PSTracking(pfId, "");
				}

				if (frmName.equals("form7ps") || frmName.equals("form8ps")) {
					
					PCAftrSepFlag = finReportService.chkPfidHavngPCAfterSeperation(pfId, empData);
				}
				request.setAttribute("MappedList", employeeinfolist);

				request.setAttribute("ArrearInfo", "");
				request.setAttribute("transferType", transferType);
				request.setAttribute("fndregion", region);
				request.setAttribute("chkpfid", chkpfid);
				request.setAttribute("statemntRevisdChkPfid", statemntRevisdChkPfid);
				request.setAttribute("PCAftrSepFlag", PCAftrSepFlag);
				log.info("transferType " + transferType);
				log.info("Mapped LIst " + mappedList.size());
				log.info("chkpfid " + chkpfid);
				log.info("statemntRevisdChkPfid " + statemntRevisdChkPfid);
				log.info("PCAftrSepFlag " + PCAftrSepFlag);

				path = "./PensionView/RevisionPCMappedList.jsp";

				RequestDispatcher rd = request.getRequestDispatcher(path);
				rd.forward(request, response);
			} catch (Exception e) {
				
				log.printStackTrace(e);
			}
		}
		
		
		
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("revisionOptionPC")) {
		
			log.info("else if : 3");
			String frmName = "", accountType = "", userRegion = "", profileType = "", userStation = "",
					loginUserId = "", result = "", employeeNo = "", dataFlag = "", accessCode = "", privilages = "",
					message = "", path = "";

			if (request.getParameter("frmName") != null) {
				
				frmName = request.getParameter("frmName");
			} else {
				
				frmName = "";
			}

			if (request.getParameter("accessCode") != null) {
				
				accessCode = request.getParameter("accessCode");
			}

			if (session.getAttribute("loginUserId") != null) {
				
				loginUserId = session.getAttribute("loginUserId").toString();
			}

			path = "./PensionView/RevisionOptionPCEditInputParams.jsp";

			log.info("==profileType===" + profileType + "==privilages==" + privilages + "==message==" + message);
			request.setAttribute("privilages", privilages);
			request.setAttribute("accountType", accountType);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("message", message);
			request.setAttribute("frmName", frmName);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} 
		
		
		else if (request.getParameter("method").equals("searchRevisionOptionPC")) {
		
			log.info("else block 4");
			String frmName = "", accessCode = "", privilages = "", accountType = "", userRegion = "", profileType = "",
					userStation = "", loginUserId = "", result = "", employeeNo = "", dataFlag = "",
					errorFlag = "false", user = "";
			String employeeName = "", empRegion = "", empStation = "", enterdPfid = "", message = "";
			ArrayList empList = new ArrayList();
			ArrayList dataList = new ArrayList();
			EmpMasterBean empBean = new EmpMasterBean();
			EmpMasterBean empInfo = new EmpMasterBean();
			if (request.getParameter("empsrlNo") != null) {
				
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter("empsrlNo").toString().trim()));
				enterdPfid = request.getParameter("empsrlNo");
			} else {
			
				empBean.setEmpSerialNo("");
			}

			if (request.getParameter("frmName") != null) {
				
				frmName = request.getParameter("frmName");
			} else {
				
				frmName = "";
			}

			if (session.getAttribute("accountType") != null) {
				
				accountType = session.getAttribute("accountType").toString();
			}
			if (session.getAttribute("privilages") != null) {
				
				privilages = session.getAttribute("privilages").toString();
			}
			if (request.getParameter("accessCode") != null) {
				
				accessCode = request.getParameter("accessCode");
			}
			if (session.getAttribute("profileType") != null) {
				
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("station") != null) {
				
				userStation = session.getAttribute("station").toString().toLowerCase();
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				
				userRegion = session.getAttribute("loginUsrRegion").toString().toLowerCase();
			}
			if (session.getAttribute("loginUserId") != null) {
				
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			if (session.getAttribute("userid") != null) {
				
				user = session.getAttribute("userid").toString();
			}
			if (!empBean.getEmpSerialNo().equals("")) {
				
				empList = commonDAO.getEmployeePersonalInfo(empBean.getEmpSerialNo());
				if (empList.size() > 0) {
					
					empInfo = (EmpMasterBean) empList.get(0);
					try {
						
						BeanUtils.copyProperties(empBean, empInfo);
					} catch (IllegalAccessException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					employeeName = empInfo.getEmpName();
					log.info(employeeName);
				}
				
				empRegion = empInfo.getRegion().toLowerCase().trim();
				empStation = empInfo.getStation().toLowerCase().trim();
				log.info("privilages==" + privilages + "===profileType===" + profileType + "==");
				log.info("userRegion==" + userRegion + "==Employee==" + empRegion);
				log.info("userStation==" + userStation + "==Employee==" + empStation + "uuuuuuuuuu" + user);

			}
			int n = 0;
			if (request.getParameter("flag") != null && request.getParameter("flag").equals("approve")) {
				n = pcs.getApprove(empBean.getEmpSerialNo());
			} else if (request.getParameter("flag") != null && request.getParameter("flag").equals("approveSec")) {
				n = pcs.getApproveSec(empBean.getEmpSerialNo());
			}

			if (errorFlag.equals("false")) {
				if (user.equals("GOPALPAL")) {
					dataList = pcs.searchForRevisionOptionPCSecLvl(userRegion, userStation, profileType, accountType,
							empBean.getEmpSerialNo());
				} else {
					dataList = pcs.searchForRevisionOptionPC(userRegion, userStation, profileType, accountType,
							empBean.getEmpSerialNo());
				}

			}

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/RevisionOptionPCEditInputParams.jsp");
			request.setAttribute("searchInfo", empBean);
			request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
			if (dataList.size() > 0) {
				request.setAttribute("searchList", dataList);
			} else {
				dataFlag = "NoData";
				request.setAttribute("dataFlag", dataFlag);
			}
			request.setAttribute("frmName", frmName);
			request.setAttribute("message", message);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("empsrlNo", enterdPfid);
			rd.forward(request, response);
		} 
		
		
		
		
		else if (request.getParameter("method").equals("loadRevisionOptionPC")) {
			
			log.info("else block -- 5");
			String region = "", frmYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "",
					empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "NO-SELECT",
					chkBulkPrint = "", grandTotDiffShowFlag = "";
			String recoverieTable = "", reportYear = "", path = "", adjObYear = "", loginUserId = "",
					loginUsrDesgn = "";
			String empflag = "true", lastmonthYear = "", lastmonthFlag = "", empName = "", sortingOrder = "",
					bulkPrintFlag = "", updateFlag = "false";
			String interestCalc = "", ReportStatus = "", batchid = "", username = "", ipaddress = "", stageStatus = "N",
					processedStage = "", accessCode = "";
			;
			String[] years = null;
			String notFianalizetransID = "", notFianalizetransIDForPrev = "", finlaizedFlag = "true",
					transIdToGetPrevData = "", form2Status = "", enterdPfid = "";
			int result = 0;
			ArrayList pensionContributionList = new ArrayList();
			List prevGrandTotalsList = new ArrayList();
			ArrayList list = new ArrayList();
			ArrayList adjEmolList = new ArrayList();
			ArrayList finalizedTotals = new ArrayList();
			String mappingFlag = "true";
			String pfcard = "";

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
				// region="NO-SELECT";
			} else {
				region = "NO-SELECT";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frmName") != null) {
				formType = request.getParameter("frmName");
			}
			if (request.getParameter("frm_month") != null) {
				selectedMonth = request.getParameter("frm_month");
			}
			selectedMonth = "03";
			if (request.getParameter("frm_pensionno") != null) {
				empserialNO = commonUtil.getSearchPFID1(request.getParameter("frm_pensionno").toString().trim());
				enterdPfid = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			// /
			// empserialNO=commonUtil.trailingZeros(empserialNO.toCharArray());
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

			if (request.getParameter("diffFlag") != null) {
				grandTotDiffShowFlag = request.getParameter("diffFlag");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = "1995-2015";
			}
			reportYear = "1995-2015";
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			if (request.getParameter("pfFlag") != null) {
				pfcard = request.getParameter("pfFlag");
			} else {
				pfcard = "";
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}

			if (request.getParameter("stageStatus") != null) {
				stageStatus = request.getParameter("stageStatus");
			}
			if (request.getParameter("form2Status") != null) {
				form2Status = request.getParameter("form2Status");
			}
			if (request.getParameter("processedStage") != null) {
				processedStage = request.getParameter("processedStage");
			}
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}

			log.info("------reportYear-----" + reportYear);
			years = reportYear.split("-");
			frmYear = "01-Apr-" + Integer.parseInt(years[0]);
			toYear = "31-Mar-" + years[1];

			log.info("----frmYear-" + frmYear + "------toYear-- " + toYear + "--grandTotDiffShowFlag---"
					+ grandTotDiffShowFlag + "pfcard" + pfcard);

			// if (reportYear.equals("1995-2015")) {
			/*
			 * pensionContributionList = adjCrtnService
			 * .getPensionContributionReportForAdjCRTN(frmYear, toYear, region, airportCode,
			 * empserialNO, cpfAccno, batchid, ReportStatus);
			 */
			try {
				pcs.insertEmployeeTransData(empserialNO, "adjcorrections", username, ipaddress, "", mappingFlag,
						cpfAccno, region, "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (formType.equals("edit")) {
				pensionContributionList = pcs.getRevisionPCReport(toYear, frmYear, region, airportCode, selectedMonth,
						empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint, "edit");
			} else {
				pensionContributionList = pcs.getRevisionPCReport(toYear, frmYear, region, airportCode, selectedMonth,
						empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint, "false");
			}

			// }

			log.info("adjEmolList in session " + session.getAttribute("adjEmolumentsList")
					+ "================================sssssssssssssssssss==================================grandTotDiffShowFlag"
					+ grandTotDiffShowFlag);
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("adjEmolList " + adjEmolList.size());
			}
			AdjCrntSaveDtlBean afterSaveDtlBean = new AdjCrntSaveDtlBean();
			if (!grandTotDiffShowFlag.equals("")) {

				log.info("--------entrees--------");
				double EmolumentsTot = 0.00, cpfTotal = 0.00, cpfIntrst = 0.00, PenContriTotal = 0.00,
						PensionIntrst = 0.00, PFTotal = 0.00, PFIntrst = 0.00;
				double EmpSub = 0.00, EmpSubInterest = 0.00, AAIContri = 0.00, AAIContriInterest = 0.00,
						adjAAiContriInterest = 0.00, adjPensionContriInterest = 0.00, adjEmpSubInterest = 0.00;

				/*
				 * if (request.getParameter("EmolumentsTot") != null) { EmolumentsTot =
				 * Double.parseDouble(request .getParameter("EmolumentsTot")); } if
				 * (request.getParameter("cpfTotal") != null) { cpfTotal =
				 * Double.parseDouble(request .getParameter("cpfTotal")); } if
				 * (request.getParameter("cpfIntrst") != null) { cpfIntrst =
				 * Double.parseDouble(request .getParameter("cpfIntrst")); } if
				 * (request.getParameter("PenContriTotal") != null) { PenContriTotal =
				 * Double.parseDouble(request .getParameter("PenContriTotal")); } if
				 * (request.getParameter("PensionIntrst") != null) { PensionIntrst =
				 * Double.parseDouble(request .getParameter("PensionIntrst")); } if
				 * (request.getParameter("PFTotal") != null) { PFTotal =
				 * Double.parseDouble(request .getParameter("PFTotal")); } if
				 * (request.getParameter("PFIntrst") != null) { PFIntrst =
				 * Double.parseDouble(request .getParameter("PFIntrst")); } if
				 * (request.getParameter("EmpSub") != null) { EmpSub =
				 * Double.parseDouble(request.getParameter("EmpSub")); } if
				 * (request.getParameter("EmpSubInterest") != null) { EmpSubInterest =
				 * Double.parseDouble(request .getParameter("EmpSubInterest")); } if
				 * (request.getParameter("adjEmpSubInterest") != null) { adjEmpSubInterest =
				 * Double.parseDouble(request .getParameter("adjEmpSubInterest")); } if
				 * (request.getParameter("AAIContri") != null) { AAIContri =
				 * Double.parseDouble(request .getParameter("AAIContri")); } if
				 * (request.getParameter("AAIContriInterest") != null) { AAIContriInterest =
				 * Double.parseDouble(request .getParameter("AAIContriInterest")); } if
				 * (request.getParameter("adjAAiContriInterest") != null) { adjAAiContriInterest
				 * = Double.parseDouble(request .getParameter("adjAAiContriInterest")); }
				 */
				if (request.getParameter("reportYear") != null) {
					reportYear = request.getParameter("reportYear");
				}
				if (request.getParameter("pensioninter") != null) {
					adjPensionContriInterest = Double.parseDouble(request.getParameter("pensioninter"));
				}
				if (session.getAttribute("loginUserId") != null) {
					loginUserId = session.getAttribute("loginUserId").toString();
				}
				if (session.getAttribute("loginUsrDesgn") != null) {
					loginUsrDesgn = session.getAttribute("loginUsrDesgn").toString();
				}
				String reasonForInsert = "Edited";
				/*
				 * try {
				 * afterSaveDtlBean=adjCrtnService.saveAdjCrntDetails(empserialNO,cpfAccno,
				 * reportYear, EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
				 * PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest, adjEmpSubInterest,
				 * AAIContri, AAIContriInterest, adjAAiContriInterest, adjPensionContriInterest,
				 * grandTotDiffShowFlag, reasonForInsert, username,
				 * ipaddress,adjEmolList,batchid);
				 * 
				 * } catch (InvalidDataException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				session.removeAttribute("adjEmolumentsList");
			} else {
				// afterSaveDtlBean=adjCrtnService.getFinzdPreviouseGrndTotals(empserialNO,reportYear,batchid
				// );
			}

			// prevGrandTotalsList=afterSaveDtlBean.getPreviouseGrndList();
			// finlaizedFlag=afterSaveDtlBean.getFinalizedFlag();
			String reinterestcalcdate = "";
			if (request.getParameter("interestcalcUpto") != null) {
				interestCalc = request.getParameter("interestcalcUpto");
			}
			;
			String finalintrestdate = pcs.finalintrestdate(reinterestcalcdate, empserialNO);
			if (request.getParameter("reinterestcalc") != null) {
				reinterestcalcdate = request.getParameter("reinterestcalc");
			}
			String getEditedPensionno = "";
			log.info("pensionContributionList " + pensionContributionList.size());
			/*
			 * request.setAttribute("interestCalcfinal", interestCalc);
			 * request.setAttribute("penContrList", pensionContributionList);
			 * request.setAttribute("reportType", reportType);
			 * request.setAttribute("stationStr", airportCode);
			 * request.setAttribute("regionStr", region); request.setAttribute("accessCode",
			 * accessCode); request.setAttribute("grandTotDiffShowFlag",
			 * grandTotDiffShowFlag);
			 * request.setAttribute("prevGrandTotalsList",prevGrandTotalsList);
			 * request.setAttribute("finlaizedFlag", finlaizedFlag);
			 * request.setAttribute("form2Status", form2Status);
			 */
			double pctotal = 0.0;
			double intrest = 0.0;
			String retireddate = "";
			if (recoverieTable.equals("true")) {
				pctotal = financeService.getPensionContributionTotal(empserialNO);
				intrest = financeService.getfinalrevoveryintrest(empserialNO);
				getEditedPensionno = financeService.getEditedPensionno(empserialNO);
			}
			String reIntrestDate = pcs.reIntrestDate(empserialNO);
			String reSettlementdate = pcs.reSettlementdate(empserialNO);
			double interestforNoofMonths = pcs.getInterestforNoofMonths(interestCalc);
			double interestforfinalsettleMonths = pcs.interestforfinalsettleMonths(reinterestcalcdate, empserialNO);
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
			request.setAttribute("interestforNoofMonths", String.valueOf(interestforNoofMonths));
			request.setAttribute("interestforfinalsettleMonths", String.valueOf(interestforfinalsettleMonths));

			if (page.equals("report") || pfcard.equals("true")) {
				// finalizedTotals =
				// adjCrtnService.getAdjCrtnFinalizedTotals(empserialNO,reportYear);
				request.setAttribute("finalizedTotals", finalizedTotals);

			}

			if (page.equals("report")) {
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTN.jsp";

			} else {

				path = "./PensionView/RevisionOptionEditScreen.jsp?recoverieTable=" + recoverieTable;

			}
			log.info("---page---" + page + "---path------" + path + "----------" + reportYear + "--reportType-------"
					+ reportType);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		}
		
		
		else if (request.getParameter("method").equals("loadRevisionOptionPCSecLvl")) {
			
			
			log.info("else block 6");
			String region = "", frmYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "",
					empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "NO-SELECT",
					chkBulkPrint = "", grandTotDiffShowFlag = "";
			String recoverieTable = "", reportYear = "", path = "", adjObYear = "", loginUserId = "",
					loginUsrDesgn = "";
			String empflag = "true", lastmonthYear = "", lastmonthFlag = "", empName = "", sortingOrder = "",
					bulkPrintFlag = "", updateFlag = "false";
			String interestCalc = "", ReportStatus = "", batchid = "", username = "", ipaddress = "", stageStatus = "N",
					processedStage = "", accessCode = "";
			;
			String[] years = null;
			String notFianalizetransID = "", notFianalizetransIDForPrev = "", finlaizedFlag = "true",
					transIdToGetPrevData = "", form2Status = "", enterdPfid = "";
			int result = 0;
			ArrayList pensionContributionList = new ArrayList();
			List prevGrandTotalsList = new ArrayList();
			ArrayList list = new ArrayList();
			ArrayList adjEmolList = new ArrayList();
			ArrayList finalizedTotals = new ArrayList();
			String mappingFlag = "true";
			String pfcard = "";

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
				// region="NO-SELECT";
			} else {
				region = "NO-SELECT";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frmName") != null) {
				formType = request.getParameter("frmName");
			}
			if (request.getParameter("frm_month") != null) {
				selectedMonth = request.getParameter("frm_month");
			}
			selectedMonth = "03";
			if (request.getParameter("frm_pensionno") != null) {
				empserialNO = commonUtil.getSearchPFID1(request.getParameter("frm_pensionno").toString().trim());
				enterdPfid = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			// /
			// empserialNO=commonUtil.trailingZeros(empserialNO.toCharArray());
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

			if (request.getParameter("diffFlag") != null) {
				grandTotDiffShowFlag = request.getParameter("diffFlag");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = "1995-2015";
			}
			reportYear = "1995-2015";
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			if (request.getParameter("pfFlag") != null) {
				pfcard = request.getParameter("pfFlag");
			} else {
				pfcard = "";
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}

			if (request.getParameter("stageStatus") != null) {
				stageStatus = request.getParameter("stageStatus");
			}
			if (request.getParameter("form2Status") != null) {
				form2Status = request.getParameter("form2Status");
			}
			if (request.getParameter("processedStage") != null) {
				processedStage = request.getParameter("processedStage");
			}
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}

			log.info("------reportYear-----" + reportYear);
			years = reportYear.split("-");
			frmYear = "01-Apr-" + Integer.parseInt(years[0]);
			toYear = "31-Mar-" + years[1];

			log.info("----frmYear-" + frmYear + "------toYear-- " + toYear + "--grandTotDiffShowFlag---"
					+ grandTotDiffShowFlag + "pfcard" + pfcard);

			// if (reportYear.equals("1995-2015")) {
			/*
			 * pensionContributionList = adjCrtnService
			 * .getPensionContributionReportForAdjCRTN(frmYear, toYear, region, airportCode,
			 * empserialNO, cpfAccno, batchid, ReportStatus);
			 */
			try {
				pcs.insertEmployeeTransDataSecLvl(empserialNO, "adjcorrections", username, ipaddress, "", mappingFlag,
						cpfAccno, region, "");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (formType.equals("edit")) {
				pensionContributionList = pcs.getRevisionPCReportSecLvl(toYear, frmYear, region, airportCode,
						selectedMonth, empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint,
						"edit");
			} else {
				pensionContributionList = pcs.getRevisionPCReport(toYear, frmYear, region, airportCode, selectedMonth,
						empserialNO, cpfAccno, transferFlag, mappingFlag, pfidString, chkBulkPrint, "false");
			}

			// }

			log.info("adjEmolList in session " + session.getAttribute("adjEmolumentsList")
					+ "================================sssssssssssssssssss==================================grandTotDiffShowFlag"
					+ grandTotDiffShowFlag);
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("adjEmolList " + adjEmolList.size());
			}
			AdjCrntSaveDtlBean afterSaveDtlBean = new AdjCrntSaveDtlBean();
			if (!grandTotDiffShowFlag.equals("")) {

				log.info("--------entrees--------");
				double EmolumentsTot = 0.00, cpfTotal = 0.00, cpfIntrst = 0.00, PenContriTotal = 0.00,
						PensionIntrst = 0.00, PFTotal = 0.00, PFIntrst = 0.00;
				double EmpSub = 0.00, EmpSubInterest = 0.00, AAIContri = 0.00, AAIContriInterest = 0.00,
						adjAAiContriInterest = 0.00, adjPensionContriInterest = 0.00, adjEmpSubInterest = 0.00;

				/*
				 * if (request.getParameter("EmolumentsTot") != null) { EmolumentsTot =
				 * Double.parseDouble(request .getParameter("EmolumentsTot")); } if
				 * (request.getParameter("cpfTotal") != null) { cpfTotal =
				 * Double.parseDouble(request .getParameter("cpfTotal")); } if
				 * (request.getParameter("cpfIntrst") != null) { cpfIntrst =
				 * Double.parseDouble(request .getParameter("cpfIntrst")); } if
				 * (request.getParameter("PenContriTotal") != null) { PenContriTotal =
				 * Double.parseDouble(request .getParameter("PenContriTotal")); } if
				 * (request.getParameter("PensionIntrst") != null) { PensionIntrst =
				 * Double.parseDouble(request .getParameter("PensionIntrst")); } if
				 * (request.getParameter("PFTotal") != null) { PFTotal =
				 * Double.parseDouble(request .getParameter("PFTotal")); } if
				 * (request.getParameter("PFIntrst") != null) { PFIntrst =
				 * Double.parseDouble(request .getParameter("PFIntrst")); } if
				 * (request.getParameter("EmpSub") != null) { EmpSub =
				 * Double.parseDouble(request.getParameter("EmpSub")); } if
				 * (request.getParameter("EmpSubInterest") != null) { EmpSubInterest =
				 * Double.parseDouble(request .getParameter("EmpSubInterest")); } if
				 * (request.getParameter("adjEmpSubInterest") != null) { adjEmpSubInterest =
				 * Double.parseDouble(request .getParameter("adjEmpSubInterest")); } if
				 * (request.getParameter("AAIContri") != null) { AAIContri =
				 * Double.parseDouble(request .getParameter("AAIContri")); } if
				 * (request.getParameter("AAIContriInterest") != null) { AAIContriInterest =
				 * Double.parseDouble(request .getParameter("AAIContriInterest")); } if
				 * (request.getParameter("adjAAiContriInterest") != null) { adjAAiContriInterest
				 * = Double.parseDouble(request .getParameter("adjAAiContriInterest")); }
				 */
				if (request.getParameter("reportYear") != null) {
					reportYear = request.getParameter("reportYear");
				}
				if (request.getParameter("pensioninter") != null) {
					adjPensionContriInterest = Double.parseDouble(request.getParameter("pensioninter"));
				}
				if (session.getAttribute("loginUserId") != null) {
					loginUserId = session.getAttribute("loginUserId").toString();
				}
				if (session.getAttribute("loginUsrDesgn") != null) {
					loginUsrDesgn = session.getAttribute("loginUsrDesgn").toString();
				}
				String reasonForInsert = "Edited";
				/*
				 * try {
				 * afterSaveDtlBean=adjCrtnService.saveAdjCrntDetails(empserialNO,cpfAccno,
				 * reportYear, EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal,
				 * PensionIntrst, PFTotal, PFIntrst, EmpSub, EmpSubInterest, adjEmpSubInterest,
				 * AAIContri, AAIContriInterest, adjAAiContriInterest, adjPensionContriInterest,
				 * grandTotDiffShowFlag, reasonForInsert, username,
				 * ipaddress,adjEmolList,batchid);
				 * 
				 * } catch (InvalidDataException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

				session.removeAttribute("adjEmolumentsList");
			} else {
				// afterSaveDtlBean=adjCrtnService.getFinzdPreviouseGrndTotals(empserialNO,reportYear,batchid
				// );
			}

			// prevGrandTotalsList=afterSaveDtlBean.getPreviouseGrndList();
			// finlaizedFlag=afterSaveDtlBean.getFinalizedFlag();
			String reinterestcalcdate = "";
			if (request.getParameter("interestcalcUpto") != null) {
				interestCalc = request.getParameter("interestcalcUpto");
			}
			;
			String finalintrestdate = pcs.finalintrestdate(reinterestcalcdate, empserialNO);
			if (request.getParameter("reinterestcalc") != null) {
				reinterestcalcdate = request.getParameter("reinterestcalc");
			}
			String getEditedPensionno = "";
			log.info("pensionContributionList " + pensionContributionList.size());
			/*
			 * request.setAttribute("interestCalcfinal", interestCalc);
			 * request.setAttribute("penContrList", pensionContributionList);
			 * request.setAttribute("reportType", reportType);
			 * request.setAttribute("stationStr", airportCode);
			 * request.setAttribute("regionStr", region); request.setAttribute("accessCode",
			 * accessCode); request.setAttribute("grandTotDiffShowFlag",
			 * grandTotDiffShowFlag);
			 * request.setAttribute("prevGrandTotalsList",prevGrandTotalsList);
			 * request.setAttribute("finlaizedFlag", finlaizedFlag);
			 * request.setAttribute("form2Status", form2Status);
			 */
			double pctotal = 0.0;
			double intrest = 0.0;
			String retireddate = "";
			if (recoverieTable.equals("true")) {
				pctotal = financeService.getPensionContributionTotal(empserialNO);
				intrest = financeService.getfinalrevoveryintrest(empserialNO);
				getEditedPensionno = financeService.getEditedPensionno(empserialNO);
			}
			String reIntrestDate = pcs.reIntrestDate(empserialNO);
			String reSettlementdate = pcs.reSettlementdate(empserialNO);
			double interestforNoofMonths = pcs.getInterestforNoofMonths(interestCalc);
			double interestforfinalsettleMonths = pcs.interestforfinalsettleMonths(reinterestcalcdate, empserialNO);
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
			request.setAttribute("interestforNoofMonths", String.valueOf(interestforNoofMonths));
			request.setAttribute("interestforfinalsettleMonths", String.valueOf(interestforfinalsettleMonths));

			if (page.equals("report") || pfcard.equals("true")) {
				// finalizedTotals =
				// adjCrtnService.getAdjCrtnFinalizedTotals(empserialNO,reportYear);
				request.setAttribute("finalizedTotals", finalizedTotals);

			}

			if (page.equals("report")) {
				log.info("if block ");
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTN.jsp";

			} else {

				log.info("else block ");
				path = "./PensionView/RevisionOptionEditScreen.jsp?recoverieTable=" + recoverieTable;

			}
			log.info("---page---" + page + "---path------" + path + "----------" + reportYear + "--reportType-------"
					+ reportType);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			log.info("forwardingg: ");
			rd.forward(request, response);
			log.info("finish...");

		} 
		
		
		else if (request.getParameter("method").equals("editTransactionDataForAdjCrtn")) {
			
			
			log.info("7  else block");

			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00",
					contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "", adjOBYear = "", url = "", editTransFlag = "",
					dateOfBirth = "";
			String pensionoption = "", empnetob = "", aainetob = "";
			String empnetobFlag = "";
			ArrayList adjEmolumentsList = new ArrayList();
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter("pensionNo").toString());
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
			log.info("------in Action ---contri-------" + contri + "------region--" + region);
			if (request.getParameter("from7narration") != null) {
				from7narration = request.getParameter("from7narration");
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

			if (request.getParameter("adjOBYear") != "") {
				adjOBYear = request.getParameter("adjOBYear");
			}
			if (request.getParameter("noofmonths") != null) {
				noofmonths = request.getParameter("noofmonths");
			}
			if (request.getParameter("editid") != null) {
				editid = request.getParameter("editid");
			}
			if (request.getParameter("duputationflag") != null) {
				duputationflag = request.getParameter("duputationflag");
			}
			String ComputerName = session.getAttribute("computername").toString();
			String username = session.getAttribute("userid").toString();
			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			if (request.getParameter("pensionoption") != null) {
				pensionoption = request.getParameter("pensionoption").toString();
			}
			if (request.getParameter("empnetobFlag") != null) {
				empnetobFlag = request.getParameter("empnetobFlag");
			}
			if (request.getParameter("editTransFlag") != null) {
				editTransFlag = request.getParameter("editTransFlag");
			}
			if (request.getParameter("dateOfBirth") != null) {
				dateOfBirth = request.getParameter("dateOfBirth");
			}
			if (empnetobFlag.equals("true")) {
				if (request.getParameter("empnetob") != null) {
					empnetob = request.getParameter("empnetob");
				}
				if (request.getParameter("aainetob") != null) {
					aainetob = request.getParameter("aainetob");
				}
			}
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolumentsList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("----adjEmolumentsList---------" + adjEmolumentsList.size());
			}
			adjEmolumentsList = pcs.editTransactionDataForAdjCrtn(cpfAccno, monthYear, emoluments, epf, vpf, principle,
					interest, advance, loan, aailoan, contri, noofmonths, pfid, region, airportcode, username,
					ComputerName, from7narration, duputationflag, pensionoption, empnetob, aainetob, empnetobFlag,
					adjOBYear, editTransFlag, dateOfBirth);

			log.info("----adjEmolumentsList after res---------" + adjEmolumentsList.size());
			int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec" + insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode=" + airportcode + "&frm_year=" + yearID
					+ "&frm_reportType=" + reportType + "&empserialNO=" + pfid + "&frm_pfids=" + pfidStrip + "&page="
					+ page + "&mappingFlag=" + mappingFlag + "&reportYear=" + adjOBYear;

			url = "./pcreportservlet?method=getReportRevPenContr" + params;

			log.info("url is " + url);
			// RequestDispatcher rd =
			// request.getRequestDispatcher(
			// "./search1?method=searchRecordsbyEmpSerailNo");
			// commented below two lines on 06-Apr-2010
			// RequestDispatcher rd = request.getRequestDispatcher(url);
			// rd.forward(request, response);
			log.info(editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);

		} else if (request.getParameter("method").equals("editTransactionDataForAdjCrtnSecLvl")) {

			log.info("8  else block");
			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00",
					contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "", adjOBYear = "", url = "", editTransFlag = "",
					dateOfBirth = "";
			String pensionoption = "", empnetob = "", aainetob = "";
			String empnetobFlag = "";
			ArrayList adjEmolumentsList = new ArrayList();
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter("pensionNo").toString());
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
			log.info("------in Action ---contri-------" + contri + "------region--" + region);
			if (request.getParameter("from7narration") != null) {
				from7narration = request.getParameter("from7narration");
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

			if (request.getParameter("adjOBYear") != "") {
				adjOBYear = request.getParameter("adjOBYear");
			}
			if (request.getParameter("noofmonths") != null) {
				noofmonths = request.getParameter("noofmonths");
			}
			if (request.getParameter("editid") != null) {
				editid = request.getParameter("editid");
			}
			if (request.getParameter("duputationflag") != null) {
				duputationflag = request.getParameter("duputationflag");
			}
			String ComputerName = session.getAttribute("computername").toString();
			String username = session.getAttribute("userid").toString();
			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			if (request.getParameter("pensionoption") != null) {
				pensionoption = request.getParameter("pensionoption").toString();
			}
			if (request.getParameter("empnetobFlag") != null) {
				empnetobFlag = request.getParameter("empnetobFlag");
			}
			if (request.getParameter("editTransFlag") != null) {
				editTransFlag = request.getParameter("editTransFlag");
			}
			if (request.getParameter("dateOfBirth") != null) {
				dateOfBirth = request.getParameter("dateOfBirth");
			}
			if (empnetobFlag.equals("true")) {
				if (request.getParameter("empnetob") != null) {
					empnetob = request.getParameter("empnetob");
				}
				if (request.getParameter("aainetob") != null) {
					aainetob = request.getParameter("aainetob");
				}
			}
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolumentsList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("----adjEmolumentsList---------" + adjEmolumentsList.size());
			}
			adjEmolumentsList = pcs.editTransactionDataForAdjCrtnSecLvl(cpfAccno, monthYear, emoluments, epf, vpf,
					principle, interest, advance, loan, aailoan, contri, noofmonths, pfid, region, airportcode,
					username, ComputerName, from7narration, duputationflag, pensionoption, empnetob, aainetob,
					empnetobFlag, adjOBYear, editTransFlag, dateOfBirth);

			log.info("----adjEmolumentsList after res---------" + adjEmolumentsList.size());
			int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec" + insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode=" + airportcode + "&frm_year=" + yearID
					+ "&frm_reportType=" + reportType + "&empserialNO=" + pfid + "&frm_pfids=" + pfidStrip + "&page="
					+ page + "&mappingFlag=" + mappingFlag + "&reportYear=" + adjOBYear;

			url = "./pcreportservlet?method=getReportRevPenContr" + params;

			log.info("url is " + url);
			// RequestDispatcher rd =
			// request.getRequestDispatcher(
			// "./search1?method=searchRecordsbyEmpSerailNo");
			// commented below two lines on 06-Apr-2010
			// RequestDispatcher rd = request.getRequestDispatcher(url);
			// rd.forward(request, response);
			log.info(editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);

		}
		// added by mohan for impact pc reports.
		else if (request.getParameter("method").equals("loadAdjObCrtnforPc")) {
			log.info("8  else block");
			String loginUserId = "", accessCode = "", privilages = "", accountType = "", message = "", profileType = "",
					result = "", path = "";
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}
			if (session.getAttribute("accountType") != null) {
				accountType = session.getAttribute("accountType").toString();
			}
			if (session.getAttribute("privilages") != null) {
				privilages = session.getAttribute("privilages").toString();
			}
			if (session.getAttribute("profileType") != null) {
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			log.info("yyyyyyyyyyyyyyyyy" + loginUserId + "xxxxxxxxxxxxxxxxxxxxxxx" + accessCode);
			if (profileType.equals("U") || profileType.equals("R") || profileType.equals("C")) {
				if (privilages.equals("NP")) {
					message = "U Don't Have Privilages to Access";
				} else {
					result = commonDAO.chkScreenAccessRights(loginUserId, accessCode);
					log.info("==result===" + result);
					if (result.equals("NotHaving")) {
						message = "U Don't Have Privilages to Access";
					}
				}
			}
			if (accessCode.equals("PE040201")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnforPc.jsp";
			} else if (accessCode.equals("PE040202")) {
				path = "./PensionView/AdjCrtn/ApproverSearchForAdjCrtnforPc.jsp";
			}

			log.info("==profileType===" + profileType + "==privilages==" + privilages + "==message==" + message);
			request.setAttribute("privilages", privilages);
			request.setAttribute("accountType", accountType);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("message", message);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadPCReportForAdjDetailsforPc")) {
			log.info("9  else block");
			log.info("loadPCReportForAdjDetailsforPc () ");
			String employeeName = "", frmName = "", path = "", empRegion = "", empStation = "", adjObYears = "";
			String cpfAccnos = "", searchFlag = "", username = "", ipaddress = "", chkpfid = "", enterdPfid = "",
					employeeNo = "", chkPfidTracking = "", blockedYears = "";
			String accessCode = "", privilages = "", accountType = "", message = "", profileType = "",
					errorFlag = "false", userRegion = "", userStation = "", loginUserId = "", result = "",
					verifiedby = "";
			String chqFlag = "", empSubTot = "0", aaiContriTot = "0", pensionTot = "0";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList empPCAdjDiffTot = new ArrayList();
			// new code for status disply
			if (request.getParameter("cpfnumbers") != null) {
				cpfAccnos = request.getParameter("cpfnumbers");
				request.setAttribute("cpfAccnos", cpfAccnos);

			}
			if (request.getParameter("empName") != null)

				empBean.setEmpName(request.getParameter("empName").toString().trim());
			if (request.getParameter("dob") != null) {
				empBean.setDateofBirth(request.getParameter("dob").toString().trim());
			} else
				empBean.setDateofBirth("");
			if (request.getParameter("empsrlNo") != null) {
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter("empsrlNo").toString().trim()));
				employeeNo = empBean.getEmpSerialNo();
				enterdPfid = request.getParameter("empsrlNo");
			} else {
				empBean.setEmpSerialNo("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateofJoining((String) request.getParameter("doj").toString());
			} else
				empBean.setDateofJoining("");

			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			} else {
				frmName = "";
			}

			log.info("Name==" + empBean.getEmpName() + "DOB=" + empBean.getDateofBirth() + "DOJ=="
					+ empBean.getDateofJoining() + "No==" + empBean.getEmpSerialNo());

			log.info("cpfAccnos--------- " + cpfAccnos + "===--------" + empBean.getEmpSerialNo());

			// SearchInfo searchBean = new SearchInfo();

			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}
			if (request.getParameter("searchFlag") != null) {
				searchFlag = request.getParameter("searchFlag");
			} else {
				searchFlag = "";
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}
			/*
			 * if (session.getAttribute("accountType") != null) { accountType =
			 * session.getAttribute("accountType").toString(); }
			 */
			if (session.getAttribute("privilages") != null) {
				privilages = session.getAttribute("privilages").toString();
			}
			if (session.getAttribute("profileType") != null) {
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("station") != null) {
				userStation = ((String) session.getAttribute("station")).toLowerCase().trim();
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				userRegion = ((String) session.getAttribute("loginUsrRegion")).toLowerCase().trim();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}

			EmpMasterBean empInfo = new EmpMasterBean();
			String pfidStrip = "";
			ArrayList empList = new ArrayList();
			boolean flag = true;
			try {
				if (!empBean.getEmpSerialNo().equals("")) {
					empList = commonDAO.getEmployeePersonalInfo(empBean.getEmpSerialNo());
					if (empList.size() > 0) {
						empInfo = (EmpMasterBean) empList.get(0);
						BeanUtils.copyProperties(empBean, empInfo);
						employeeName = empInfo.getEmpName();
						log.info(employeeName);
					} else {
						message = "No Records Found";
						errorFlag = "true";
					}
					empRegion = empInfo.getRegion().toLowerCase().trim();
					empStation = empInfo.getStation().toLowerCase().trim();
					log.info("privilages==" + privilages);
					log.info("userRegion==" + userRegion + "==Employee==" + empRegion);
					log.info("userStation==" + userStation + "==Employee==" + empStation);

					if (empList.size() > 0) {
						if (privilages.equals("P")) {
							result = commonDAO.chkScreenAccessRights(loginUserId, accessCode);
							log.info("Screen Access==" + result);
							if (result.equals("NotHaving")) {
								message = "U Don't Have Privilages to Access";
							} else {
								verifiedby = adjCrtnDAO.chkStageWiseprocessinAdjCalcforPc(empBean.getEmpSerialNo());
								if (accessCode.equals("PE040202")) {
									if (verifiedby.equals("")) {
										errorFlag = "true";
									}
								}

								log.info("----verifiedby-- in Action-----" + verifiedby + "==accessCode==="
										+ accessCode);
								if (profileType.equals("U")) {
									if ((!empRegion.equals(userRegion)) || (!empStation.equals(userStation))) {
										message = "This Pfid belongs to " + empInfo.getRegion() + "/"
												+ empInfo.getStation() + ". You Can't Process";
										errorFlag = "true";
									}
								} else if (profileType.equals("R")) {
									if (!(empRegion.equals(userRegion))) {
										message = "This Pfid belongs to " + empInfo.getRegion() + ". You Can't Process";
										errorFlag = "true";
									} else if (!(empStation.equals(userStation))) {
										accountType = commonDAO.getAccountType(empRegion, empStation);
										if (!accountType.equals("")) {
											// For Restricting Rigths to RAU of CHQIAD to SAU Accounts on 07-Jun-2012
											if (empRegion.toUpperCase().equals("CHQIAD") && accountType.equals("SAU")) {
												message = "This Pfid belongs to " + empInfo.getRegion() + "/"
														+ empInfo.getStation() + ". You Can't Process";
												errorFlag = "true";

											}
											// Commented on 14-May-2012 as per Instruction given By Sehgal
											/*
											 * if(accountType.equals("SAU")){ message
											 * ="This Pfid belongs to "+accountType+" Account Type. You Can't Process";
											 * errorFlag="true"; }
											 */
										} else {
											message = " This Pfid does not belongs to Any Account Type. You Can't Process";
											errorFlag = "true";
										}
									}

								}
							}
						} else {
							errorFlag = "true";
							message = "U Don't Have Privilages to Access";
						}
					}
					if (errorFlag.equals("false")) {
						if (frmName.equals("adjcorrections")) {
							log.info("--------frmName----" + frmName + "----employeeName---" + employeeName + "=");
							if (!employeeName.equals("")) {
								String adjOBYear = "1995-2008";

								chkpfid = adjCrtnService.chkPfidinAdjCrtnforPc(empBean.getEmpSerialNo(), frmName);
								log.info("--------chkpfid-------" + chkpfid);

								if (chkpfid.equals("NotExists")) {
									/*
									 * pensionService.insertEmployeeTransData(empBean .getEmpSerialNo(), frmName,
									 * username, ipaddress,searchFlag,"","","");
									 */
									String chkUploadPfid = adjCrtnService
											.chkPfidinAdjCrtnTrackingForUploadforPc(empBean.getEmpSerialNo(), frmName);
									log.info("--------chkUploadPfid-------" + chkUploadPfid);
									/*
									 * Below changes the are done by prasad for the purpose of uploading/Mapping
									 */
									if (chkUploadPfid.equals("Exists")) {
										ArrayList prePcTotals = new ArrayList();
										ArrayList currPcTotals = new ArrayList();
										adjCrtnService.insertEmployeeTransDataforPc(empBean.getEmpSerialNo(), frmName,
												username, ipaddress, searchFlag, "U", "", "", "N");
										prePcTotals = adjCrtnService.updatePCAdjCorrectionsforPc("01-Apr-1995",
												"31-Mar-2008", "", "", empBean.getEmpSerialNo(), "", "");

										int result1 = adjCrtnService.savePrePctoalsTempforPc(empBean.getEmpSerialNo(),
												"1995-2008", prePcTotals);
										adjCrtnService.getDeleteAllRecords(empBean.getEmpSerialNo(), "", "", username,
												ipaddress, "U", chqFlag, empSubTot, aaiContriTot, pensionTot);
										adjCrtnService.insertEmployeeTransDataforPc(empBean.getEmpSerialNo(), frmName,
												username, ipaddress, searchFlag, "U", "", "", "U");
										currPcTotals = adjCrtnService.updatePCAdjCorrectionsforPc("01-Apr-1995",
												"31-Mar-2008", "", "", empBean.getEmpSerialNo(), "", "");

										// result1
										// =adjCrtnService.insertRecordForAdjCtrnTracking(empBean.getEmpSerialNo(), "",
										// "1995-2008", "Upload", username, ipaddress);

										result1 = adjCrtnService.saveCurrPctoalsforPc(empBean.getEmpSerialNo(),
												"1995-2008", currPcTotals);

									} else {
										adjCrtnService.insertEmployeeTransDataforPc(empBean.getEmpSerialNo(), frmName,
												username, ipaddress, searchFlag, "", "", "", "");
									}
								}
								empPCAdjDiffTot = adjCrtnService.getPCAdjDiffforPc(empBean.getEmpSerialNo(), adjOBYear);
								request.setAttribute("empPCAdjDiffTot", empPCAdjDiffTot);
								request.setAttribute("empsrlNo", employeeNo);

								// request.setAttribute("searchBean", searchBean);
								request.setAttribute("searchInfo", empBean);
							} else {
								request.setAttribute("empsrlNo", employeeNo);
								request.removeAttribute("searchInfo");
							}
						}
						// for Form2
						PensionContBean PersonalInfo = new PensionContBean();
						copyToBean(empInfo, PersonalInfo);

						session.setAttribute("PersonalInfo", PersonalInfo);

						chkPfidTracking = adjCrtnService.chkPfidinAdjCrtnTrackingforPc(employeeNo, frmName);

						adjObYears = adjCrtnDAO.getNotFinalizedAdjObYearforPc(employeeNo, frmName);

						blockedYears = adjCrtnDAO.getblockedAdjObYearsforPc(employeeNo, frmName);
					}
				}
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("adjcorrections")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnforPc.jsp";
			} else {
				request.setAttribute("searchInfo", empBean);
				request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDateforPc.jsp";
			}
			log.info("--------message-------" + message);
			String blockOrFrozenflag = "";

			blockOrFrozenflag = adjCrtnService.chkPfidBRF(empBean.getEmpSerialNo());
			log.info("--------message-------" + blockOrFrozenflag);
			request.setAttribute("blockOrFrozenflag", blockOrFrozenflag);
			request.setAttribute("message", message);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("verifiedby", verifiedby);
			request.setAttribute("empsrlNo", enterdPfid);
			request.setAttribute("chkPfidTracking", chkPfidTracking);
			request.setAttribute("adjObYears", adjObYears);
			request.setAttribute("blockedYears", blockedYears);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getReportPenContrForAdjCrtnforPc")) {
			
			log.info("10  else block");
			String region = "", frmYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "",
					empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "NO-SELECT",
					chkBulkPrint = "", grandTotDiffShowFlag = "";
			String recoverieTable = "", reportYear = "", path = "", adjObYear = "", loginUserId = "",
					loginUsrDesgn = "";
			String empflag = "true", lastmonthYear = "", lastmonthFlag = "", empName = "", sortingOrder = "",
					bulkPrintFlag = "", updateFlag = "false";
			String interestCalc = "", ReportStatus = "", batchid = "", username = "", ipaddress = "", stageStatus = "N",
					processedStage = "", accessCode = "";
			;
			String[] years = null;
			String notFianalizetransID = "", notFianalizetransIDForPrev = "", finlaizedFlag = "true",
					transIdToGetPrevData = "", form2Status = "", enterdPfid = "";
			int result = 0;
			ArrayList pensionContributionList = new ArrayList();
			List prevGrandTotalsList = new ArrayList();
			ArrayList list = new ArrayList();
			ArrayList adjEmolList = new ArrayList();
			ArrayList finalizedTotals = new ArrayList();
			String mappingFlag = "true";
			String pfcard = "";

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
				// region="NO-SELECT";
			} else {
				region = "NO-SELECT";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_month") != null) {
				selectedMonth = request.getParameter("frm_month");
			}
			if (request.getParameter("empserialNO") != null) {
				empserialNO = commonUtil.getSearchPFID1(request.getParameter("empserialNO").toString().trim());
				enterdPfid = request.getParameter("empserialNO");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			// /
			// empserialNO=commonUtil.trailingZeros(empserialNO.toCharArray());
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

			if (request.getParameter("diffFlag") != null) {
				grandTotDiffShowFlag = request.getParameter("diffFlag");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			if (request.getParameter("pfFlag") != null) {
				pfcard = request.getParameter("pfFlag");
			} else {
				pfcard = "";
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}

			if (request.getParameter("stageStatus") != null) {
				stageStatus = request.getParameter("stageStatus");
			}
			if (request.getParameter("form2Status") != null) {
				form2Status = request.getParameter("form2Status");
			}
			if (request.getParameter("processedStage") != null) {
				processedStage = request.getParameter("processedStage");
			}
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}

			log.info("------reportYear-----" + reportYear);
			years = reportYear.split("-");
			frmYear = "01-Apr-" + Integer.parseInt(years[0]);
			toYear = "31-Mar-" + years[1];

			log.info("----frmYear-" + frmYear + "------toYear-- " + toYear + "--grandTotDiffShowFlag---"
					+ grandTotDiffShowFlag + "pfcard" + pfcard);

			if (reportYear.equals("1995-2008")) {
				log.info("if block");
				pensionContributionList = adjCrtnService.getPensionContributionReportForAdjCRTNforPc(frmYear, toYear,
						region, airportCode, empserialNO, cpfAccno, batchid, ReportStatus);
			} else {

				list = adjCrtnService.pfCardReportForAdjCrtnforPc(pfidString, region, years[0], empflag, empName,
						sortingOrder, empserialNO, lastmonthFlag, lastmonthYear, airportCode, bulkPrintFlag);
				request.setAttribute("cardList", list);
				request.setAttribute("dspYear", reportYear);
				request.setAttribute("reportYear", reportYear);
			}

			log.info("========================================================================================================");
			log.info("adjEmolList in session " + session.getAttribute("adjEmolumentsList")
					+ "================================sssssssssssssssssss==================================grandTotDiffShowFlag"
					+ grandTotDiffShowFlag);
			
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("adjEmolList " + adjEmolList.size());
			}
			AdjCrntSaveDtlBean afterSaveDtlBean = new AdjCrntSaveDtlBean();
			if (!grandTotDiffShowFlag.equals("")) {

				log.info("--------entrees--------");
				double EmolumentsTot = 0.00, cpfTotal = 0.00, cpfIntrst = 0.00, PenContriTotal = 0.00,
						PensionIntrst = 0.00, PFTotal = 0.00, PFIntrst = 0.00;
				double EmpSub = 0.00, EmpSubInterest = 0.00, AAIContri = 0.00, AAIContriInterest = 0.00,
						adjAAiContriInterest = 0.00, adjPensionContriInterest = 0.00, adjEmpSubInterest = 0.00;

				if (request.getParameter("reportYear") != null) {
					reportYear = request.getParameter("reportYear");
				}
				if (request.getParameter("pensioninter") != null) {
					adjPensionContriInterest = Double.parseDouble(request.getParameter("pensioninter"));
				}
				if (session.getAttribute("loginUserId") != null) {
					loginUserId = session.getAttribute("loginUserId").toString();
				}
				if (session.getAttribute("loginUsrDesgn") != null) {
					loginUsrDesgn = session.getAttribute("loginUsrDesgn").toString();
				}
				String reasonForInsert = "Edited";
				try {
					log.info("10 : try block ");
					afterSaveDtlBean = adjCrtnService.saveAdjCrntDetailsforPc(empserialNO, cpfAccno, reportYear,
							EmolumentsTot, cpfTotal, cpfIntrst, PenContriTotal, PensionIntrst, PFTotal, PFIntrst,
							EmpSub, EmpSubInterest, adjEmpSubInterest, AAIContri, AAIContriInterest,
							adjAAiContriInterest, adjPensionContriInterest, grandTotDiffShowFlag, reasonForInsert,
							username, ipaddress, adjEmolList, batchid);
					log.info("****************************************************************************");

				} catch (InvalidDataException e) {
					log.info(" invalida Data Exception ");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				session.removeAttribute("adjEmolumentsList");
			} else {
				log.info("adjcertn Dao : getFinzdPreviouseGrndTotalsforPc");
				afterSaveDtlBean = adjCrtnService.getFinzdPreviouseGrndTotalsforPc(empserialNO, reportYear, batchid);
				log.info("end..");
			}

			prevGrandTotalsList = afterSaveDtlBean.getPreviouseGrndList();
			finlaizedFlag = afterSaveDtlBean.getFinalizedFlag();
			log.info("pensionContributionList " + pensionContributionList.size());
			request.setAttribute("penContrList", pensionContributionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("stationStr", airportCode);
			request.setAttribute("regionStr", region);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("grandTotDiffShowFlag", grandTotDiffShowFlag);
			request.setAttribute("prevGrandTotalsList", prevGrandTotalsList);
			request.setAttribute("finlaizedFlag", finlaizedFlag);
			request.setAttribute("form2Status", form2Status);

			if (page.equals("report") || pfcard.equals("true")) {
				finalizedTotals = adjCrtnService.getAdjCrtnFinalizedTotalsforPc(empserialNO, reportYear);
				request.setAttribute("finalizedTotals", finalizedTotals);

			}

			if (page.equals("report")) {
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTNforPc.jsp";

			} else if (Integer.parseInt(years[0]) >= 2015) {
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTNAfter2015forPc.jsp";
				log.info("path after 2015------" + path);
			} else {

				if (reportYear.equals("1995-2008")) {
					path = "./PensionView/AdjCrtn/PensionContributionScreenForAdjCRTNforPc.jsp?recoverieTable="
							+ recoverieTable;

				} else {

					if (pfcard.equals("true")) {

						path = "./PensionView/AdjCrtn/PFCardForAdjCtrnforPc.jsp";

					} else {

						path = "./PensionView/AdjCrtn/PensionContributionScreenForAdjCRTN2008-2009forPc.jsp";
					}
				}
			}
			log.info("---page---" + page + "---path------" + path + "----------" + reportYear + "--reportType-------"
					+ reportType);
			
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("editTransactionDataForAdjCrtnforPc")) {

			log.info("11  else block");
			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00", addcon = "0.00", editid = "", vpf = "0.00", principle = "0.00",
					interest = "0.00", contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00",
					pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "", adjOBYear = "", url = "", editTransFlag = "",
					dateOfBirth = "";
			String pensionoption = "", empnetob = "", aainetob = "";
			String empnetobFlag = "";
			ArrayList adjEmolumentsList = new ArrayList();
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter("pensionNo").toString());
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
			if (request.getParameter("addcon") != null) {
				addcon = request.getParameter("addcon");
			}
			if (request.getParameter("epf") != null) {
				epf = request.getParameter("epf");
			}

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
			log.info("------in Action ---contri-------" + contri + "------region--" + region);
			if (request.getParameter("from7narration") != null) {
				from7narration = request.getParameter("from7narration");
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

			if (request.getParameter("adjOBYear") != "") {
				adjOBYear = request.getParameter("adjOBYear");
			}
			if (request.getParameter("noofmonths") != null) {
				noofmonths = request.getParameter("noofmonths");
			}
			if (request.getParameter("editid") != null) {
				editid = request.getParameter("editid");
			}
			if (request.getParameter("duputationflag") != null) {
				duputationflag = request.getParameter("duputationflag");
			}
			String ComputerName = session.getAttribute("computername").toString();
			String username = session.getAttribute("userid").toString();
			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			if (request.getParameter("pensionoption") != null) {
				pensionoption = request.getParameter("pensionoption").toString();
			}
			if (request.getParameter("empnetobFlag") != null) {
				empnetobFlag = request.getParameter("empnetobFlag");
			}
			if (request.getParameter("editTransFlag") != null) {
				editTransFlag = request.getParameter("editTransFlag");
			}
			if (request.getParameter("dateOfBirth") != null) {
				dateOfBirth = request.getParameter("dateOfBirth");
			}
			if (empnetobFlag.equals("true")) {
				if (request.getParameter("empnetob") != null) {
					empnetob = request.getParameter("empnetob");
				}
				if (request.getParameter("aainetob") != null) {
					aainetob = request.getParameter("aainetob");
				}
			}
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolumentsList = (ArrayList) session.getAttribute("adjEmolumentsList");
				log.info("----adjEmolumentsList---------" + adjEmolumentsList.size());
			}
			adjEmolumentsList = adjCrtnService.editTransactionDataForAdjCrtnforPc(cpfAccno, monthYear, emoluments,
					addcon, epf, vpf, principle, interest, advance, loan, aailoan, contri, noofmonths, pfid, region,
					airportcode, username, ComputerName, from7narration, duputationflag, pensionoption, empnetob,
					aainetob, empnetobFlag, adjOBYear, editTransFlag, dateOfBirth);

			log.info("----adjEmolumentsList after res---------" + adjEmolumentsList.size());
			int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec" + insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode=" + airportcode + "&frm_year=" + yearID
					+ "&frm_reportType=" + reportType + "&empserialNO=" + pfid + "&frm_pfids=" + pfidStrip + "&page="
					+ page + "&mappingFlag=" + mappingFlag + "&reportYear=" + adjOBYear;

			url = "./pcreportservlet?method=getReportPenContrForAdjCrtnforPc" + params;

			log.info("url is " + url);
			// RequestDispatcher rd =
			// request.getRequestDispatcher(
			// "./search1?method=searchRecordsbyEmpSerailNo");
			// commented below two lines on 06-Apr-2010
			// RequestDispatcher rd = request.getRequestDispatcher(url);
			// rd.forward(request, response);
			log.info(editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);

		} else if (request.getParameter("method").equals("getDeleteAllRecordsforPc")) {
			log.info("12  else block");
			String pfid = "", frmName = "", message = "", path = "", reportYear = "", form2Status = "", chqFlag = "",
					empSubTot = "0", aaiContriTot = "0", pensionTot = "0";
			StringBuffer sb = new StringBuffer();
			// new
			String username = "", ipaddress = "";
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}
			if (request.getParameter("empserialno") != null) {
				pfid = request.getParameter("empserialno");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}
			if (request.getParameter("chqFlag") != null) {
				chqFlag = request.getParameter("chqFlag");
			}
			if (chqFlag.equals("true")) {
				if (request.getParameter("empSubTot") != null) {
					empSubTot = request.getParameter("empSubTot");
				}
				if (request.getParameter("aaiContriTot") != null) {
					aaiContriTot = request.getParameter("aaiContriTot");
				}
				if (request.getParameter("pensionTot") != null) {
					pensionTot = request.getParameter("pensionTot");
				}
				if (request.getParameter("form2Status") != null) {
					form2Status = request.getParameter("form2Status");
				}
			}
			try {
				message = adjCrtnService.getDeleteAllRecordsforPc(pfid, reportYear, frmName, username, ipaddress, "",
						chqFlag, empSubTot, aaiContriTot, pensionTot);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sb.append("<ServiceTypes>");
			sb.append("<ServiceType>");
			sb.append("<airPortName>");
			sb.append("Deleted sucussfully");
			sb.append("<airPortName>");
			sb.append("</airPortName>");
			sb.append("</ServiceType>");
			sb.append("</ServiceTypes>");

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method").equals("updateAdjCrtnStatusforPc")) {
			log.info("13  else block");
			RequestDispatcher rd = null;
			String stageStatus = "", approvemonth = "", accessCode = "", processedStage = "", pensionno = "", url = "",
					params = "", username = "", loginUserId = "", userRegion = "", loginUsrDesgn = "",
					loginUsrStation = "", reportYear = "", form2Status = "", jvno = "";
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}

			if (request.getParameter("stageStatus") != null) {
				stageStatus = request.getParameter("stageStatus");
			}

			if (request.getParameter("processedStage") != null) {
				processedStage = request.getParameter("processedStage");
			}

			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}
			if (request.getParameter("form2Status") != null) {
				form2Status = request.getParameter("form2Status");
			}
			if (request.getParameter("jvno") != null) {
				jvno = request.getParameter("jvno");
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}

			if (session.getAttribute("loginUsrRegion") != null) {
				userRegion = (String) session.getAttribute("loginUsrRegion");
			}
			if (session.getAttribute("loginUsrDesgn") != null) {
				loginUsrDesgn = session.getAttribute("loginUsrDesgn").toString();
			}
			if (session.getAttribute("station") != null) {
				loginUsrStation = session.getAttribute("station").toString();
			}

			if (stageStatus.equals("Y")) {
				adjCrtnService.updateStageWiseStatusInAdjCtrnforPc(pensionno, processedStage, reportYear, form2Status,
						jvno, username, loginUserId, userRegion, loginUsrStation, loginUsrDesgn);
			}

			request.setAttribute("accessCode", accessCode);

			params = "&accessCode=" + accessCode + "&empsrlNo=" + pensionno + "&reportYear=" + reportYear
					+ "&searchFlag=S&frmName=adjcorrections";
			if (accessCode.equals("PE040201")) {
				url = "./pcreportservlet?method=loadPCReportForAdjDetailsforPc" + params;
			} else if (accessCode.equals("PE040202") || accessCode.equals("PE04020601")) {
				url = "./pcreportservlet?method=searchAdjRecordsforPc" + params;
			}

			log.info("========url========" + url);
			rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("searchAdjRecordsforPc")) {
			log.info("14  else block");
			log.info("searchAdjRecords () ");
			String employeeName = "", frmName = "", path = "";
			String cpfAccnos = "", searchFlag = "", username = "", ipaddress = "", chkpfid = "", enterdPfid = "",
					employeeNo = "", reportYear = "", status = "";
			String accessCode = "", privilages = "", accountType = "", message = "", profileType = "",
					errorFlag = "false", userStation = "", loginUserId = "", result = "", verifiedby = "";
			String userRegion = "", adjObYears = "";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList empPCAdjDiffTot = new ArrayList();

			if (request.getParameter("empsrlNo") != null) {
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter("empsrlNo").toString().trim()));
				employeeNo = empBean.getEmpSerialNo();
				enterdPfid = request.getParameter("empsrlNo");
			} else {
				empBean.setEmpSerialNo("");
			}

			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			} else {
				frmName = "";
			}

			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}
			if (request.getParameter("searchFlag") != null) {
				searchFlag = request.getParameter("searchFlag");
			} else {
				searchFlag = "";
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			} else {
				reportYear = "";
			}
			if (request.getParameter("status") != null) {
				status = request.getParameter("status");
			} else {
				status = "";
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}
			if (session.getAttribute("accountType") != null) {
				accountType = session.getAttribute("accountType").toString();
			}
			if (session.getAttribute("privilages") != null) {
				privilages = session.getAttribute("privilages").toString();
			}
			if (session.getAttribute("profileType") != null) {
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("station") != null) {
				userStation = session.getAttribute("station").toString();
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				userRegion = (String) session.getAttribute("loginUsrRegion");
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}

			EmpMasterBean empInfo = new EmpMasterBean();
			String pfidStrip = "";
			ArrayList searchList = new ArrayList();
			boolean flag = true;
			try {
				log.info("privilages==" + privilages + "employeeNo==" + employeeNo);
				log.info("userRegion==" + userRegion + "==Employee==" + empInfo.getRegion());
				log.info("userStation==" + userStation + "==Employee==" + empInfo.getStation());

				if (privilages.equals("P")) {
					result = commonDAO.chkScreenAccessRights(loginUserId, accessCode);
					log.info("Screen Access==" + result);
					if (result.equals("NotHaving")) {
						message = "U Don't Have Privilages to Access";
					} else {

						searchList = adjCrtnService.searchAdjctrnforPc(userRegion, userStation, profileType, accessCode,
								accountType, employeeNo, reportYear, status);
						if (!employeeNo.equals("")) {
							adjObYears = adjCrtnDAO.getNotFinalizedAdjObYearforPc(employeeNo, frmName);
						}
					}
				} else {
					errorFlag = "true";
					message = "U Don't Have Privilages to Access";
				}
				log.info("--------errorFlag----" + errorFlag);
				if (errorFlag.equals("false")) {

				}

			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("adjcorrections")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnforPc.jsp";
			} else {
				request.setAttribute("searchInfo", empBean);
				request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDateforPc.jsp";
			}

			if (accessCode.equals("PE040201")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnforPc.jsp";
			} else if (accessCode.equals("PE040202")) {
				path = "./PensionView/AdjCrtn/ApproverSearchForAdjCrtnforPc.jsp";

				request.setAttribute("searchOnFinYear", reportYear);
				request.setAttribute("searchOnStatus", status);
			} else if (accessCode.equals("PE040204")) {
				path = "./PensionView/AdjCrtn/ApprovedSearchForAdjCrtnforPc.jsp";
			} else if (accessCode.equals("PE04020601")) {
				path = "./PensionView/AdjCrtn/CHQApproverSearchForAdjCrtnforPc.jsp";
			} else if (accessCode.equals("PE04020602")) {
				path = "./PensionView/AdjCrtn/CHQApprovedSearchForAdjCrtnforPc.jsp";
			}
			if (searchList.size() > 0) {
				request.setAttribute("searchList", searchList);
			}
			request.setAttribute("adjObYears", adjObYears);
			request.setAttribute("reportYear", reportYear);
			request.setAttribute("message", message);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("verifiedby", verifiedby);
			request.setAttribute("empsrlNo", enterdPfid);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getadjemolumentslog")) {
			log.info("15  else block");

			String pfid = "", adjOBYear = "", frmName = "", path = "";
			ArrayList emolumentsList = new ArrayList();
			if (request.getParameter("empserialno") != null) {
				pfid = request.getParameter("empserialno");
			}
			if (request.getParameter("adjobyear") != null) {
				adjOBYear = request.getParameter("adjobyear");
			}

			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}

			try {
				emolumentsList = adjCrtnService.getAdjEmolumentsLogforPc(pfid, adjOBYear, frmName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			path = "./PensionView/AdjCrtn/AdjEmolumentsTrackingLogforPc.jsp";
			request.setAttribute("adjEmolTrackingLog", emolumentsList);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("generateform4report")) {
			log.info("16  else block");
			String path = "", diffpc = "", remarks = "", pensionno = "", empName = "", remmonth = "", emoluemnts = "",
					reportType = "", month = "", id = "", adjyear = "", year = "", adjFlag = "true", frmName = "";
			String pageType = "Page", reportFlag = "", accessCode = "", diffaddcontri = "";
			ArrayList list = new ArrayList();
			String totalpc = "", totalinterstpc = "";
			if (request.getParameter("totalpc") != null) {
				totalpc = request.getParameter("totalpc");
			}
			if (request.getParameter("totalinterstpc") != null) {
				totalinterstpc = request.getParameter("totalinterstpc");
			}
			if (request.getParameter("empserialNO") != null) {
				pensionno = request.getParameter("empserialNO");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			if (request.getParameter("remmonth") != null) {
				remmonth = request.getParameter("remmonth");
			}
			if (request.getParameter("emoluemnts") != null) {
				emoluemnts = request.getParameter("emoluemnts");
			}
			if (request.getParameter("diffpc") != null) {
				diffpc = request.getParameter("diffpc");
			}
			if (request.getParameter("diffaddcontri") != null) {
				diffaddcontri = request.getParameter("diffaddcontri");
			}
			if (request.getParameter("remarks") != null) {
				remarks = request.getParameter("remarks");
			}
			if (request.getParameter("report") != null) {
				reportType = request.getParameter("report");
			}
			if (request.getParameter("id") != null) {
				id = request.getParameter("id");
			}
			if (request.getParameter("adjyear") != null) {
				adjyear = request.getParameter("adjyear");
			}
			list = adjCrtnService.generateform4report(pensionno, empName, remmonth, emoluemnts, diffpc, diffaddcontri,
					remarks, id, adjyear, totalpc, totalinterstpc);

			request.setAttribute("cardList", list);
			path = "./PensionView/AdjCrtn/AAIEPF.jsp";
			System.out.println("path=====" + path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			request.setAttribute("reportType", reportType);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadStationWiseRemittance")) {
			log.info("17  else block");
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			monthIterator1 = monthset.iterator();
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("monthToIterator", monthIterator1);
			HashMap regionHashmap = new HashMap();

			String[] regionLst = null;
			String rgnName = "";
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				// Adding Cond on NAGARAJ ON 20-Jul-2012 For not loading all Regions names
				if (rgnName.equals("ALL-REGIONS") && (session.getAttribute("usertype").toString().equals("Admin"))
						|| (session.getAttribute("usertype").toString().equals("NormalUser")
								&& session.getAttribute("userid").toString().equals("NAGARAJ"))) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					break;
				} else {
					regionHashmap.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", regionHashmap);

			String year = "", month = "", region = "", flag = "", monthYear = "", station = "", form = "", path = "";
			String currdate = "", currYear = "", selCurrYear = "";
			int records = 0;
			int year1 = 0;
			ArrayList stationList = null;

			if (request.getParameter("year") != null) {
				year = request.getParameter("year");
			}
			if (request.getParameter("month") != null) {
				month = request.getParameter("month");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("flag") != null) {
				flag = request.getParameter("flag");
			}
			if (request.getParameter("form") != null) {
				form = request.getParameter("form");
			}
			currdate = commonUtil.getCurrentDate("MM");
			currYear = commonUtil.getCurrentDate("yyyy");
			log.info("currdate" + currdate);
			if (Integer.parseInt(currdate) < 4) {
				currYear = ("" + (Integer.parseInt(currYear) - 1)).trim();

			}
			log.info("currYear" + currYear);
			selCurrYear = currYear + "-" + (Integer.parseInt(currYear.substring(2, 4)) + 1);
			log.info("year....." + year);
			if (request.getParameter("year") != null) {
				if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 3) {
					year1 = Integer.parseInt(year.substring(0, 4)) + 1;
				} else {
					year1 = Integer.parseInt(year.substring(0, 4));
				}
			}
			monthYear = "15-" + month + "-" + year1;
			log.info("monthYear" + monthYear);

			try {

				if (flag.equals("F")) {
					if (!form.equals("chq")) {
						if (region.equals("CHQIAD")) {
							log.info("station" + station);
							station = session.getAttribute("station").toString();
							log.info("station" + station);
						}
					}
					monthYear = commonUtil.converDBToAppFormat(monthYear, "dd-MM-yyyy", "dd-MMM-yyyy");
					stationList = adjCrtnService.getStationList(region, monthYear, station);
					request.setAttribute("year", year);
					request.setAttribute("month", month);
					request.setAttribute("region", region);
				} else {
					request.setAttribute("month", currdate);
					request.setAttribute("year", selCurrYear);
				}
				// log.info("stationList"+stationList);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("monthYear", monthYear);
			request.setAttribute("stationList", stationList);
			path = "./PensionView/AdjCrtn/form4reportInputParams.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("generateform4report2")) {
			log.info("18  else block");
			String path = "", id = "", pensionno = "";
			String pageType = "Page", reportFlag = "", accessCode = "";
			ArrayList list = new ArrayList();
			if (request.getParameter("id") != null) {
				id = request.getParameter("id");
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			list = adjCrtnService.generateform4report2(id, pensionno);

			request.setAttribute("cardList", list);
			path = "./PensionView/AdjCrtn/AAIEPFReport.jsp";
			System.out.println("path=====" + path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("bifurcationReport")) {
			log.info("19  else block");
			String path = "./PensionView/AdjCrtn/bifurcationReportinputparam.jsp";
			System.out.println("path=====" + path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("BifurcationReport")) {
			log.info("20  else block");
			String path = "", reportType = "", sortingOrder = "", pensionno = "", searchFlag = "",
					pfidString = "NO-SELECT", airportCode = "", month = "", region = "NO-SELECT", year = "",
					adjFlag = "true", frmName = "";
			String pageType = "Page", reportFlag = "", accessCode = "";
			ArrayList list = new ArrayList();
			if (request.getParameter("employeeNo") != null) {
				pensionno = request.getParameter("employeeNo");
			}
			if (request.getParameter("reportType") != null) {
				reportType = request.getParameter("reportType");
			}
			list = adjCrtnService.BifurcationReport(pensionno);

			request.setAttribute("cardList", list);
			path = "./PensionView/AdjCrtn/BifurcationReport.jsp";
			System.out.println("path=====" + path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			request.setAttribute("reportType", reportType);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadform7revisionopInput")) {
			log.info("21  else block");
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();
			int pereachpage = 0;
			if (request.getParameter("method").equals("loadform7Input")
					|| request.getParameter("method").equals("loadformPF6AInput")
					|| request.getParameter("method").equals("loadUpdatePensionContribut")) {
				penYearList = finReportService.getFinanceYearList();
				request.setAttribute("yearList", penYearList);
			} else {
				request.setAttribute("yearIterator", yearIterator);

			}
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			monthIterator1 = monthset.iterator();
			ArrayList pfidList = new ArrayList();
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("monthToIterator", monthIterator1);
			HashMap regionHashmap = new HashMap();

			if (request.getParameter("method").equals("loadform7revisionopInput")) {
				String[] regionLst = null;
				String rgnName = "";
				if (session.getAttribute("region") != null) {
					regionLst = (String[]) session.getAttribute("region");
				}
				log.info("Regions List" + regionLst.length);
				for (int i = 0; i < regionLst.length; i++) {
					rgnName = regionLst[i];
					log.info("rgnName==================" + rgnName);
					if (rgnName.equals("ALL-REGIONS") && session.getAttribute("usertype").toString().equals("Admin")) {
						regionHashmap = new HashMap();
						regionHashmap = commonUtil.getRegion();
						break;
					} else {
						regionHashmap.put(new Integer(i), rgnName);
					}

				}
			} else {
				regionHashmap = commonUtil.getRegion();

			}
			request.setAttribute("regionHashmap", regionHashmap);
			if (request.getParameter("method").equals("loadform7revisionopInput")) {

				ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
				int totalSize = 0;
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle.getString("common.pension.pagesize"));
				} else {
					totalSize = 100;
				}
				pereachpage = commonDAO.getPFIDsNaviagtionListSize(totalSize);
				log.info("pereachpage==========" + pereachpage);
				int startIndex = 1, endIndex = totalSize;
				for (int i = 0; i < pereachpage; i++) {
					String name = "";
					if (i != 0) {
						startIndex = startIndex + totalSize;
						endIndex = endIndex + totalSize;
					}
					if (i == pereachpage - 1) {
						name = Integer.toString(startIndex) + " - END";
					} else {
						name = Integer.toString(startIndex) + " - " + Integer.toString(endIndex);
					}
					pfidList.add(name);
				}
			}
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadform7revisionopInput")) {
				request.setAttribute("pfidList", pfidList);
				String frmName = "", empSerialNo = "", empName = "", accessCode = "", rptformType = "";
				if (request.getParameter("frmName") != null) {
					frmName = request.getParameter("frmName");
				} else {
					frmName = "";
				}
				if (request.getParameter("employeeNo") != null) {
					empSerialNo = request.getParameter("employeeNo");
				} else {
					empSerialNo = "";
				}
				if (request.getParameter("empName") != null) {
					empName = request.getParameter("empName");
				} else {
					empName = "";
				}
				if (request.getParameter("accessCode") != null) {
					accessCode = request.getParameter("accessCode");
				}
				if (request.getParameter("form_type") != null) {
					rptformType = request.getParameter("form_type");
				} else {
					rptformType = "---";
				}
				if (empName.equals("")) {
					// empName= commonDAO.getEmployeeName(employeeNo);
					ArrayList empList = new ArrayList();
					try {
						PensionBean bean = new PensionBean();
						empList = ps.getEmployeeMappedList("", "", "", empSerialNo, "");
						for (int i = 0; i < empList.size(); i++) {
							bean = (PensionBean) empList.get(i);
						}
						empName = bean.getEmployeeName();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				request.setAttribute("empSerialNo", empSerialNo);
				request.setAttribute("rptformType", rptformType);
				request.setAttribute("empName", empName);
				request.setAttribute("frmName", frmName);
				request.setAttribute("accessCode", accessCode);
				rd = request.getRequestDispatcher("./PensionView/PensionForm7RevisionOptionInputParams.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("form7revionoptionreport")) {
			log.info("22  else block");
			String region = "", year = "", adjFlag = "";
			String airportcode = "", reportType = "", sortingOrder = "pensionno", pfidString = "", formType = "",
					fileheadname = "", frmName = "", pcFlag = "false";
			ArrayList list = new ArrayList();
			ArrayList revisedList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
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
			String empflag = "false", pensionno = "";
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
			log.info("pfidString" + pfidString + "year" + year + "sortingOrder" + sortingOrder + "region" + region
					+ "airportCode" + airportCode + "pensionno" + pensionno + "formType" + formType);
			String formVal = " ";

			if (formType.equals("FORM-7PS-SUMMARY") || formType.equals("FORM-7PS-SUMMARY[REV]")) {
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

			list = pcs.allYearsForm7PrintOutForPFID(pfidString, year, sortingOrder, region, airportCode, pensionno,
					empflag, empName, formVal, formArrRvsFormFlag, adjFlag, frmName, pcFlag);
			String minMaxYear = "";
			/*
			 * if (formVal.equals("Summary") || formVal.equals("Revised")) { if
			 * ((year.equals("NO-SELECT") || year.equals("Select One"))) { minMaxYear = pcs
			 * .getMinMaxYearsForArrearBreakup(pensionno); if (minMaxYear.equals("")) {
			 * minMaxYear = "2006"; } } else { if (!((year.equals("NO-SELECT") || year
			 * .equals("Select One")))) { if (year.indexOf("-") != -1) { String[]
			 * minMaxYearList = year.split("-"); minMaxYear = minMaxYearList[0]; } else {
			 * minMaxYear = year; } } } log.info("minMaxYear========" + minMaxYear);
			 * revisedDataList = pcs.allYearsForm7PrintOutForPFID(pfidString, minMaxYear,
			 * sortingOrder, region, airportCode, pensionno, empflag, empName, formVal, "Y",
			 * adjFlag, frmName,pcFlag); if (formVal.equals("Revised")) { list =
			 * revisedDataList; } else { list.addAll(revisedDataList); }
			 * 
			 * }
			 */
			log.info("formVal" + formVal + "revisedDataList" + revisedDataList.size());
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
			log.info("formType==============" + formType + "chkMultipleYearFlag" + chkMultipleYearFlag);
			if (list.size() != 0) {
				String fileNameMessage = "";
				if (!pensionno.equals("") && !fileheadname.equals("NO-SELECT")) {
					fileNameMessage = "PFID_" + pensionno + "_(" + fileheadname + "-"
							+ (Integer.parseInt(fileheadname) + 1) + ")";
				} else if (!pensionno.equals("")) {
					fileNameMessage = "PFID_" + pensionno;
				}
				request.setAttribute("fileNameMessage", fileNameMessage);
				if (chkMultipleYearFlag == true) {
					rd = request.getRequestDispatcher("./PensionView/PensionForm7RevionOptionPFIDReport.jsp");
				} else {
					rd = request

							.getRequestDispatcher("./PensionView/PensionForm7RevionOptionReport.jsp");
				}
			} else {
				String messages = "";
				if (!pensionno.equals("")) {
					messages = "Pension no " + pensionno;
				} else if (!year.equals("")) {
					messages = "year " + year;
				} else if (!pensionno.equals("") && !year.equals("")) {
					messages = "Pension no " + pensionno + " for the FYI:" + year;
				}
				messages = "No Records found for the " + messages;
				request.setAttribute("message", messages);
				rd = request.getRequestDispatcher("./PensionView/Message.jsp");
			}
			rd.forward(request, response);
		}
	}

	public PensionContBean copyToBean(EmpMasterBean empBean, PensionContBean pBean) {

		pBean.setEmpSerialNo(empBean.getEmpSerialNo());
		pBean.setEmpCpfaccno(empBean.getCpfAcNo());
		pBean.setEmployeeNO(empBean.getEmpNumber());
		pBean.setEmployeeNM(empBean.getEmpName());
		pBean.setDesignation(empBean.getDesegnation());
		pBean.setEmpDOB(empBean.getDateofBirth());
		pBean.setEmpDOJ(empBean.getDateofJoining());
		pBean.setFhName(empBean.getFhName());
		return pBean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

}
