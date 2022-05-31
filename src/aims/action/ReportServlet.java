/**
 * File       : ReportServlet.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */
package aims.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import aims.bean.ActiveInactivBean;
import aims.bean.DashBoardDetails;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.EmployeePensionCardInfo;
import aims.bean.FinaceDataAvailable;
import aims.bean.Form8Bean;
import aims.bean.PensionBean;
import aims.bean.PensionContBean;
import aims.bean.RegionBean;
import aims.bean.adjcrnt.AdjCrntSaveDtlBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.common.OptionCahngeProcess;
import aims.dao.AdjCrtnDAO;
import aims.dao.CommonDAO;
import aims.dao.FinancialReportDAO;
import aims.service.AdjCrtnService;
import aims.service.DashBoardService;
import aims.service.EPFFormsReportService;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PensionService;
import aims.service.PersonalService;
/* 
##########################################
#Date					Developed by			Issue description
#14-Jun-2012			Prasad					Adding new report for EDLI Inspection Charges
#07-Apr-2012            Prasanthi               For Intrest caliculation 9.5 to 8.25
#07-Mar-2012            Radha                   For Passing DOb  while editing record from Adj Screen
#29-Feb-2012            Prasad                  Adding new report for impact cal. log report
#23-Feb-2012			Radha					For Records inserting into  transactions table & seperating methods related to AdjCalc Screen
#23-Feb-2012            Prasad                  For implementing the uploaded data when the uploaded data was deleted through screen.
#16-Feb-2012			Radha					Generating Over All Report for Modified Pfid Thru Adj Cal Screen
#14-Feb-2012			Prasad					Implementaing Mapping in Adj Cal Screen
#04-Feb-2012			Radha					Stage wise segrigartion of Adj Cal Screen
#########################################
*/
public class ReportServlet extends HttpServlet {
	private static final int BYTES_DOWNLOAD = 1024;

	Log log = new Log(ReportServlet.class);

	FinancialService financeService = new FinancialService();

	PersonalService personalService = new PersonalService();

	FinancialReportService finReportService = new FinancialReportService();

	PensionService pensionService = new PensionService();
	DashBoardService dbService = new DashBoardService();
	
	AdjCrtnService  adjCrtnService = new AdjCrtnService();
	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();
	
	AdjCrtnDAO adjCrtnDAO = new AdjCrtnDAO();
	
	FinancialReportDAO financeDAO = new FinancialReportDAO();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {
			String region = "";
			region = request.getParameter("region");
			CommonUtil commonUtil = new CommonUtil();
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
		} else if (request.getParameter("method").equals(
				"optionrevisedmulitple")) {
			try {
				OptionCahngeProcess.runOptionchangeProcess();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("message",
					"Option revised completed successfully");
			RequestDispatcher rd = null;
			rd = request.getRequestDispatcher("./PensionView/Message.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadfinalempscreen")) {
			log.info("finalpayment pending Screen");

			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/AAIFinalPaymentsProcessing.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("saveempscreen")) {
			log.info("finalpayment pending Screen");
			String pfidstrip = "", finanaceYear = "", empactivity = "N", deletedList = "";
			int count = 0, deletecount = 0;
			String message = "";
			if (request.getParameter("pfidstrips") != null) {
				pfidstrip = request.getParameter("pfidstrips");
			}
			if (request.getParameter("frm_financeyear") != null) {
				finanaceYear = request.getParameter("frm_financeyear");
			}
			if (request.getParameter("frm_active_flag") != null) {
				empactivity = request.getParameter("frm_active_flag");
			}
			if (request.getParameter("frm_deletedlist") != null) {
				deletedList = request.getParameter("frm_deletedlist");
			}
			log.info("servlet" + deletedList);
			if (!pfidstrip.equals("")) {
				count = finReportService.insertedDeactiveEmp(pfidstrip,
						finanaceYear);
			}
			if (!(deletedList.equals("") || deletedList.trim().equals(","))) {
				try {
					deletecount = finReportService.deleteDeactiveEmp(
							deletedList, finanaceYear);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			message = "Total " + count + " are updated for " + finanaceYear;
			message = message + " " + "Total  " + deletecount
					+ "re deleted for" + finanaceYear;
			request.setAttribute("finanaceYear", finanaceYear);
			request.setAttribute("empactivity", empactivity);
			request.setAttribute("message", message);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/AAIFinalPaymentsProcessing.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("finalempscreen")) {
			log.info("finalpayment pending Screen");
			ActiveInactivBean activeList = new ActiveInactivBean();
			String finanaceYear = "", empactivity = "N", chkedList = "";
			if (request.getParameter("frm_financeyear") != null) {
				finanaceYear = request.getParameter("frm_financeyear");
			}
			if (request.getParameter("frm_active_flag") != null) {
				empactivity = request.getParameter("frm_active_flag");
			}
			List list = new ArrayList();
			activeList = finReportService.getFinalPaymentPendingReport(
					empactivity, finanaceYear);
			list = activeList.getActivInativeList();
			chkedList = activeList.getChecklist();
			request.setAttribute("emplist", list);

			request.setAttribute("chkedList", chkedList);
			request.setAttribute("finanaceYear", finanaceYear);
			request.setAttribute("empactivity", empactivity);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/AAIFinalPaymentsProcessing.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method")
				.equals("finalpaymentspending")) {
			log.info("finalpayment pending calling");
			String finanaceYear = "", empactivity = "N", checklistPfdis = "";
			ActiveInactivBean activeInactiveBean = new ActiveInactivBean();
			if (request.getParameter("frm_financeyear") != null) {
				finanaceYear = request.getParameter("frm_financeyear");
			}
			if (request.getParameter("frm_active_flag") != null) {
				empactivity = request.getParameter("frm_active_flag");
			}
			log.info("finalpaymentspending::finanaceYear" + finanaceYear);
			List list = new ArrayList();
			activeInactiveBean = finReportService.getFinalPaymentPendingReport(
					empactivity, finanaceYear);
			list = activeInactiveBean.getActivInativeList();
			checklistPfdis = activeInactiveBean.getChecklist();
			request.setAttribute("paymentlist", list);
			request.setAttribute("reportType", "");
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/AAIFinalPaymentsPending.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadform3")
				|| request.getParameter("method").equals("loadform3params")
				|| request.getParameter("method").equals("loadform8params")
				|| request.getParameter("method").equals("loadpcsummary")
				|| request.getParameter("method").equals("loadUpdatePC")
				|| request.getParameter("method").equals("loadob")
				|| request.getParameter("method").equals("loadFinContri")
				|| request.getParameter("method").equals("loadRevPenContri")
				|| request.getParameter("method").equals("loadForm6Cmp")
				|| request.getParameter("method").equals("loaduserforms")
				|| request.getParameter("method").equals("loadpersonalreport")
				|| request.getParameter("method").equals("loadTransferINOUT")) {
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();
			ArrayList pfidList = new ArrayList();
			/*
			 * yearMap=financeService.getPensionYearList(); Set yearSet =
			 * yearMap.entrySet(); yearIterator = yearSet.iterator();
			 */

			if (request.getParameter("method").equals("loadFinContri")
					|| request.getParameter("method").equals("loadUpdatePC")) {
				penYearList = finReportService.getFinanceYearList();
				request.setAttribute("yearList", penYearList);
			} else {
				request.setAttribute("yearIterator", yearIterator);

			}
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			monthIterator1 = monthset.iterator();
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("monthToIterator", monthIterator1);
			HashMap regionHashmap = new HashMap();

			if (request.getParameter("method").equals("loadForm6Cmp")) {
				regionHashmap = commonUtil.getRegion();
			} else {
				if (request.getParameter("method").equals("loaduserforms")
						|| request.getParameter("method").equals(
								"loadFinContri")
						|| request.getParameter("method").equals(
								"loadRevPenContri")
						|| request.getParameter("method").equals(
								"loadpersonalreport")
						|| request.getParameter("method").equals(
								"loadTransferINOUT")) {
					String[] regionLst = null;
					String rgnName = "";
					if (session.getAttribute("region") != null) {
						regionLst = (String[]) session.getAttribute("region");
					}
					log.info("Regions List" + regionLst.length);
					for (int i = 0; i < regionLst.length; i++) {
						rgnName = regionLst[i];
						if (rgnName.equals("ALL-REGIONS")
								&& session.getAttribute("usertype").toString()
										.equals("Admin")) {
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

			}

			if (request.getParameter("method").equals("loadform8params")) {
				int pereachpage = 0;
				ResourceBundle bundle = ResourceBundle
						.getBundle("aims.resource.ApplicationResouces");
				int totalSize = 0;
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle
							.getString("common.pension.pagesize"));
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
						name = Integer.toString(startIndex) + " - "
								+ Integer.toString(endIndex);
					}
					pfidList.add(name);
				}
			}
			request.setAttribute("regionHashmap", regionHashmap);
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadform3")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm3ReportInputParms.jsp");
			} else if (request.getParameter("method").equals("loadform3params")) {
				rd = request
						.getRequestDispatcher("./PensionView/Form3ReportInputParms.jsp");

			} else if (request.getParameter("method").equals("loadpcsummary")) {
				rd = request
						.getRequestDispatcher("./PensionView/pcsummary/PCReportInputParms.jsp");
			} else if (request.getParameter("method").equals("loadform8params")) {
				request.setAttribute("pfidList", pfidList);
				String frmName = "", empSerialNo = "", empName = "",accessCode = "",rptformType="";
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
				}else{
					rptformType="---";
				}
			
				if (empName.equals("")) {
					// empName= commonDAO.getEmployeeName(employeeNo);
					ArrayList empList = new ArrayList();
					try {
						PensionBean bean = new PensionBean();
						empList = pensionService.getEmployeeMappedList("", "",
								"", empSerialNo,"");
						for (int i = 0; i < empList.size(); i++) {
							bean = (PensionBean) empList.get(i);
						}
						empName = bean.getEmployeeName();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				request.setAttribute("rptformType", rptformType);
				request.setAttribute("empSerialNo", empSerialNo);
				request.setAttribute("empName", empName);
				request.setAttribute("frmName", frmName);
				request.setAttribute("accessCode", accessCode);
				rd = request
						.getRequestDispatcher("./PensionView/Form8ReportInputParms.jsp");
			} else if (request.getParameter("method").equals("loaduserforms")) {
				rd = request
						.getRequestDispatcher("./PensionView/FormReportInputParmsUser.jsp");
			} else if (request.getParameter("method").equals(
					"loadpersonalreport")) {
				rd = request
						.getRequestDispatcher("./PensionView/PersonalReportInputParams.jsp");
			} else if (request.getParameter("method").equals("loadob")) {
				if (request.getParameter("message") != null) {
					request.setAttribute("success", request
							.getParameter("message"));
				}
				rd = request
						.getRequestDispatcher("./PensionView/OBPFCardInputParms.jsp");

			} else if (request.getParameter("method").equals("loadUpdatePC")) {
				if (request.getParameter("message") != null) {
					request.setAttribute("success", request
							.getParameter("message"));
				}
				rd = request
						.getRequestDispatcher("./PensionView/UpdatePCInputParams.jsp");
			} else if (request.getParameter("method").equals("loadFinContri")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionContributionInputParams.jsp");
			}else if (request.getParameter("method").equals("loadRevPenContri")) {
				rd = request
				.getRequestDispatcher("./PensionView/RevisionOptionPCInputParams.jsp");
			}else if (request.getParameter("method").equals("loadForm6Cmp")) {

				rd = request
						.getRequestDispatcher("./PensionView/PensionReportForm-6CompresiveInputParams.jsp");
			} else if (request.getParameter("method").equals(
					"loadTransferINOUT")) {

				rd = request
						.getRequestDispatcher("./PensionView/TransferInTransferOutInputParams.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("getDeleteAllRecords")) {
			String pfid = "", frmName = "", message = "", path = "",reportYear="",form2Status="",chqFlag="",empSubTot="0",aaiContriTot="0",pensionTot="0";
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
			if(chqFlag.equals("true")){
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
				message = adjCrtnService.getDeleteAllRecords(pfid,reportYear,frmName,username,
						ipaddress,"",chqFlag,empSubTot,aaiContriTot,pensionTot);
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

		} else if (request.getParameter("method").equals("getform3")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", reportType = "", sortingOrder = "", formType = "";
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfaccno";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			String empName = "";
			String empflag = "false";
			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			log.info("region==========getform3=====" + region + "year" + year
					+ "month" + month);
			String fullMonthName = "";
			if (!month.equals("00")) {
				frmMonthYear = "%" + "-" + month + "-" + year;
				disMonthYear = month + "-" + year;
				try {
					displayDate = commonUtil.converDBToAppFormat(disMonthYear,
							"MM-yyyy", "MMM-yyyy");
					int months = 0;
					months = Integer.parseInt(month) - 1;
					log.info("months=================" + months
							+ "month=========" + month);
					if (months < 10) {
						month = "0" + months;
					} else {
						month = Integer.toString(months);
					}
					disMonthYear = month + "-" + year;
					fullMonthName = commonUtil.converDBToAppFormat(
							disMonthYear, "MM-yyyy", "MMMM-yyyy");
					selectedDate = "%-" + displayDate;
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}

			} else {
				selectedDate = year;
				displayDate = year;
			}
			ArrayList regionList = new ArrayList();
			if (region.equals("NO-SELECT")) {
				regionList = commonUtil.loadRegions();
			} else {
				RegionBean bean = new RegionBean();
				bean.setRegion(region);
				bean.setAirportcode("-NA-");
				regionList.add(bean);
			}

			log.info("selectedDate==========getform3=====" + selectedDate
					+ "airportList==Size==" + region);
			request.setAttribute("empName", empName);
			request.setAttribute("empNameFlag", empflag);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("selectedDate", selectedDate);
			request.setAttribute("displayDate", fullMonthName);
			request.setAttribute("region", regionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("sortingOrder", sortingOrder);
			RequestDispatcher rd = null;
			if (formType.equals("FORM-3")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm3Report.jsp");
			} else if (formType.equals("FORM-3-PS")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm3PSReport.jsp");
			}

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("exportData")) {
			String region = "", formType = "", frmyear = "", frmMonth = "", reportType = "", frmMonthYear = "", disMonthYear = "";
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region").toString();
			}
			if (request.getParameter("frm_formtype") != null) {
				formType = request.getParameter("frm_formtype").toString();
			}
			if (request.getParameter("frm_year") != null) {
				frmyear = request.getParameter("frm_year").toString();
			}
			if (request.getParameter("frm_month") != null) {
				frmMonth = request.getParameter("frm_month").toString();
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_month").toString();
			}

			ArrayList dataList = new ArrayList();
			dataList = financeService.exportPFIDSReport(frmyear, frmMonth,
					region);
			request.setAttribute("PFIDLIST", dataList);
			request.setAttribute("reportType", reportType.trim());

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PfidList.jsp");
			rd.forward(request, response);
			// dataList = financeService.exportDataReport(airportCode,formDate,
			// region, retiredDate, sortingOrder,empName,empNameFlag);
		} else if (request.getParameter("method").equals("getform6")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "", prevMnthDate = "";
			int prevMonth = 0;
			String type = "", airportCode = "", reportType = "", sortingOrder = "", form6Flag = "";
			if (request.getParameter("form6Flag") != null) {
				form6Flag = request.getParameter("form6Flag");
			}
			ArrayList airportList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			try {
				ArrayList regionList = new ArrayList();
				if (region.equals("NO-SELECT")) {

					HashMap regionHashmap = new HashMap();
					HashMap sortedRegionMap = new LinkedHashMap();

					Iterator regionIterator = null;
					regionHashmap = commonUtil.getRegion();

					Set keys = regionHashmap.keySet();
					regionIterator = keys.iterator();
					String rgn = "";
					while (regionIterator.hasNext()) {
						rgn = regionHashmap.get(regionIterator.next())
								.toString();

						regionList.add(rgn);
					}

				} else {
					regionList.add(region);
				}

				if (request.getParameter("frm_year") != null) {
					year = request.getParameter("frm_year");
				}
				if (request.getParameter("frm_month") != null) {
					month = request.getParameter("frm_month");
				}
				if (request.getParameter("type") != null) {
					type = request.getParameter("type");
				}
				if (request.getParameter("sortingOrder") != null) {
					sortingOrder = request.getParameter("sortingOrder");
				}

				if (request.getParameter("frm_reportType") != null) {
					reportType = request.getParameter("frm_reportType");
				}
				if (request.getParameter("frm_airportcd") != null) {
					airportCode = request.getParameter("frm_airportcd");
				}

				log.info("region==========getform6A=====" + region + "year"
						+ year + "month" + month);
				if (!month.equals("00") || !month.equals("0")) {
					frmMonthYear = "%" + "-" + month + "-" + year;
					disMonthYear = month + "-" + year;
					prevMonth = Integer.parseInt(month) - 1;
					if (prevMonth == 0) {
						prevMonth = 12;
						prevMnthDate = prevMonth + "-"
								+ (Integer.parseInt(year) - 1);
					} else {
						prevMnthDate = prevMonth + "-" + year;
					}
					try {

						displayDate = commonUtil.converDBToAppFormat(
								disMonthYear, "MM-yyyy", "MMM-yyyy");
						prevMnthDate = "%-"
								+ commonUtil.converDBToAppFormat(prevMnthDate,
										"MM-yyyy", "MMM-yyyy");
						selectedDate = "%-" + displayDate;
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						log.printStackTrace(e);
					}
				} else {
					selectedDate = year;
					displayDate = year;
					prevMnthDate = "%-" + (Integer.parseInt(year) - 1);
				}

				if (!type.equals("no")) {
					if (airportCode.equals("NO-SELECT")) {
						log.info("regionList.size() " + regionList.size());
						for (int i = 0; i < regionList.size(); i++) {
							region = (String) regionList.get(i);
							airportList = financeService.getPensionAirportList(
									region, selectedDate);
							// airportList.addAll(airportList);
							log.info("airport list " + airportList.size());

						}
						request.setAttribute("airportList", airportList);

					} else {
						airportList.add(airportCode);
						request.setAttribute("airportList", airportList);
					}
				}

				log.info("selectedDate==========getform6A=====" + selectedDate
						+ "airportListSize==" + airportList.size());

				request.setAttribute("selectedDate", selectedDate);
				request.setAttribute("displayDate", displayDate);
				request.setAttribute("prevDate", prevMnthDate);
				request.setAttribute("region", regionList);
				request.setAttribute("reportType", reportType);
				request.setAttribute("sortingOrder", sortingOrder);
				request.setAttribute("airportcode", airportCode);
				RequestDispatcher rd = null;
				if (type.equals("A")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionReportForm-6A.jsp");
				} else if (type.equals("B")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionReportForm-6B.jsp");
				} else {
					if (form6Flag.equals("Y")) {
						rd = request
								.getRequestDispatcher("./PensionView/PensionReportForm-6_All_Regions.jsp");
					} else {
						rd = request
								.getRequestDispatcher("./PensionView/PensionReportForm-6.jsp");
					}

				}
				rd.forward(request, response);

			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if (request.getParameter("method").equals(
				"financeDataAvailableCheck")) {
			ArrayList list = new ArrayList();
			FinaceDataAvailable availableBean = new FinaceDataAvailable();
			ArrayList availableList = new ArrayList();
			ArrayList unAvailableList = new ArrayList();
			String reportType = "";
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			availableBean = financeService.financeDataAvailableCheck();

			availableList = availableBean.getAvailableList();
			log.info("availableList size" + availableList.size());
			unAvailableList = availableBean.getUnAvailableList();
			log.info("unavailableList size" + unAvailableList.size());
			log.info("reportType " + reportType);
			request.setAttribute("reportType", reportType);
			request.setAttribute("availableList", availableList);
			request.setAttribute("unAvailableList", unAvailableList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionAvailableCheckReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadformPF6AInput")
				|| request.getParameter("method").equals(
						"loadUpdatePensionContribut")
				|| request.getParameter("method").equals("loadform7Input")
				|| request.getParameter("method").equals("loadpfcardInput")
				|| request.getParameter("method").equals(
						"loadstatmentpcwagesInput")
				|| request.getParameter("method").equals("loadtrustpcInput")
				|| request.getParameter("method").equals("payArrears")
				|| request.getParameter("method").equals("loadrevisedstatmentpcwagesInput")
				|| request.getParameter("method").equals("retirementEmployeesInfo")
				|| request.getParameter("method").equals("pensionProcessInfo")
				|| request.getParameter("method").equals("penprocessCHQHRInitial")
				|| request.getParameter("method").equals("penprocessCHQFinApprove")
				|| request.getParameter("method").equals("penprocessdatesub")
				|| request.getParameter("method").equals("penprocessdatereturn"))  {
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();
			int pereachpage = 0;
			if (request.getParameter("method").equals("loadform7Input")
					|| request.getParameter("method").equals(
							"loadformPF6AInput")
					|| request.getParameter("method").equals(
							"loadUpdatePensionContribut")) {
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

			if (request.getParameter("method").equals("loadtrustpcInput")
					|| request.getParameter("method").equals("loadpfcardInput")
					|| request.getParameter("method").equals(
							"loadstatmentpcwagesInput")
					|| request.getParameter("method").equals(
							"loadUpdatePensionContribut")
					|| request.getParameter("method").equals(
							"loadformPF6AInput")
					|| request.getParameter("method").equals("loadform7Input")
					|| request.getParameter("method").equals("loadrevisedstatmentpcwagesInput")
					|| request.getParameter("method").equals("retirementEmployeesInfo")
					|| request.getParameter("method").equals("pensionProcessInfo")
					|| request.getParameter("method").equals("penprocessCHQHRInitial")
					|| request.getParameter("method").equals("penprocessCHQFinApprove")
					|| request.getParameter("method").equals("penprocessdatesub")
					|| request.getParameter("method").equals("penprocessdatereturn")) {
				String[] regionLst = null;
				String rgnName = "";
				if (session.getAttribute("region") != null) {
					regionLst = (String[]) session.getAttribute("region");
				}
								for (int i = 0; i < regionLst.length; i++) {
					rgnName = regionLst[i];
									if (rgnName.equals("ALL-REGIONS")
							&& session.getAttribute("usertype").toString()
									.equals("Admin")) {
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
			if (request.getParameter("method").equals("loadtrustpcInput")
					|| request.getParameter("method").equals("loadform7Input")
					|| request.getParameter("method").equals(
							"loadformPF6AInput")
					|| request.getParameter("method").equals("loadpfcardInput")
					|| request.getParameter("method").equals(
							"loadstatmentpcwagesInput")
					|| request.getParameter("method").equals(
							"loadUpdatePensionContribut")
					|| request.getParameter("method").equals(
							"loadrevisedstatmentpcwagesInput")) {

				ResourceBundle bundle = ResourceBundle
						.getBundle("aims.resource.ApplicationResouces");
				int totalSize = 0;
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle
							.getString("common.pension.pagesize"));
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
						name = Integer.toString(startIndex) + " - "
								+ Integer.toString(endIndex);
					}
					pfidList.add(name);
				}
			}
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadform7Input")) {
				request.setAttribute("pfidList", pfidList);
				String frmName = "", empSerialNo = "", empName = "" ,accessCode = "",rptformType="";
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
				}else{
					rptformType="---";
				}
				if (empName.equals("")) {
					// empName= commonDAO.getEmployeeName(employeeNo);
					ArrayList empList = new ArrayList();
					try {
						PensionBean bean = new PensionBean();
						empList = pensionService.getEmployeeMappedList("", "",
								"", empSerialNo,"");
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
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm7PSInputParams.jsp");
			} else if (request.getParameter("method").equals(
					"loadformPF6AInput")) {

				rd = request
						.getRequestDispatcher("./PensionView/FormPF6AInputParams.jsp");
				request.setAttribute("pfidList", pfidList);
			}else if (request.getParameter("method").equals(
					"loadstatmentpcwagesInput")) { 
				String frmName = "", empSerialNo = "", empName = "",accessCode = "";
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
				if (empName.equals("")) {
					// empName= commonDAO.getEmployeeName(employeeNo);
					ArrayList empList = new ArrayList();
					try {
						PensionBean bean = new PensionBean();
						empList = pensionService.getEmployeeMappedList("", "",
								"", empSerialNo,"");
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
				request.setAttribute("empName", empName);
				request.setAttribute("frmName", frmName);
				request.setAttribute("accessCode", accessCode);
				rd = request
						.getRequestDispatcher("./PensionView/StatementWagesPensionInputParams.jsp");
				request.setAttribute("pfidList", pfidList);

			} else if (request.getParameter("method").equals("loadpfcardInput")) {

				rd = request
						.getRequestDispatcher("./PensionView/PFCardInputParams.jsp");
				request.setAttribute("pfidList", pfidList);
			} else if (request.getParameter("method")
					.equals("loadtrustpcInput")) {

				rd = request
						.getRequestDispatcher("./PensionView/TrustWisePensionInputParams.jsp");
				request.setAttribute("pfidList", pfidList);

			} else if (request.getParameter("method").equals(
					"loadUpdatePensionContribut")) {
				if (request.getParameter("message") != null) {
					request.setAttribute("success", request
							.getParameter("message"));
				}
				rd = request
						.getRequestDispatcher("./PensionView/UpdatePCTransInputParams.jsp");
				request.setAttribute("pfidList", pfidList);
			} else if (request.getParameter("method").equals("payArrears")) {
				request.setAttribute("pfidList", pfidList);
				rd = request
						.getRequestDispatcher("./PensionView/PensionPayArrearsInputParams.jsp");
			} else if (request.getParameter("method").equals(
					"loadTransferINOUT")) {

				rd = request
						.getRequestDispatcher("./PensionView/TransferInTransferOutInputParams.jsp");
			}else if (request.getParameter("method").equals(
					"loadrevisedstatmentpcwagesInput")) {				 
				rd = request
						.getRequestDispatcher("./PensionView/StatementWagesRevisedInputParams.jsp");
			}else if (request.getParameter("method").equals(
			"retirementEmployeesInfo")) {				 
				rd = request
				.getRequestDispatcher("./PensionView/RetirementEmployeesInfoInputParams.jsp");
			}else if (request.getParameter("method").equals("pensionProcessInfo")) {				 
				rd = request
				.getRequestDispatcher("./PensionView/dashboard/PensionProcessSearchInputParams.jsp");
			}
			
			else if(request.getParameter("method").equals("penprocessCHQHRInitial")) {
				rd = request
				.getRequestDispatcher("./PensionView/dashboard/PensionProcessCHQHRInputParams.jsp");
			}
			else if(request.getParameter("method").equals("penprocessCHQFinApprove")) {
				rd = request
				.getRequestDispatcher("./PensionView/dashboard/PensionProcessCHQFinInputParams.jsp");
			}
			
			else if(request.getParameter("method").equals("penprocessdatesub")) {
				rd = request
				.getRequestDispatcher("./PensionView/dashboard/PensionProcesssDOSInputParams.jsp");
			}
			
			else if(request.getParameter("method").equals("penprocessdatereturn")) {
				rd = request
				.getRequestDispatcher("./PensionView/dashboard/PensionProcessDORInputParams.jsp");
			}
			rd.forward(request, response);

		}else if (request.getParameter("method").equals("retirementEmployeesReport")) {
			String region = "", year = "", month = "" ;
			String  reportType = "", airportcode = "";

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (airportcode.equals("[Select One]")) {
				airportcode = "";
			} else if (airportcode.equals("NO-SELECT")) {
				airportcode = "";
			}
			if (region.equals("[Select One]")) {
				region = "";
			} else if (region.equals("NO-SELECT")) {
				region = "";
			}
			
			
			String monthYear = "";
				
				
						
						monthYear = "02-"+month + "-" + year;
						try {
							monthYear=	commonUtil.converDBToAppFormat(monthYear,
									"dd-MM-yyyy", "dd-MMM-yyyy");
						} catch (InvalidDataException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
			ArrayList retdList = new ArrayList();
			try{
			retdList = finReportService.retdListReportByPFID(monthYear,
					region, airportcode );
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
			request.setAttribute("displayDate", monthYear);
		
			
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			request.setAttribute("airportCode", airportcode);
			request.setAttribute("retdList", retdList);
			
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/RetirementEmployeesInfo.jsp");
			
			
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("dupform3")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String reportType = "", sortingOrder = "";

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfaccno";
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			String fullMonthName = "";
			if (!month.equals("00")) {
				frmMonthYear = "%" + "-" + month + "-" + year;
				disMonthYear = month + "-" + year;
				try {
					displayDate = commonUtil.converDBToAppFormat(disMonthYear,
							"MM-yyyy", "MMM-yyyy");
					int months = 0;
					months = Integer.parseInt(month) - 1;
					
					if (months < 10) {
						month = "0" + months;
					} else {
						month = Integer.toString(months);
					}
					disMonthYear = month + "-" + year;
				
					fullMonthName = commonUtil.converDBToAppFormat(
							disMonthYear, "MM-yyyy", "MMMM-yyyy");
					selectedDate = "%-" + displayDate;
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}

			} else {
				selectedDate = year;
				displayDate = year;
			}

			request.setAttribute("selectedDate", selectedDate);
			request.setAttribute("displayDate", fullMonthName);
			request.setAttribute("reportType", reportType);
			request.setAttribute("sortingOrder", sortingOrder);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionForm3DuplicateDataReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("getReportPenContr")) {
			String region = "", finYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "", empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "", chkBulkPrint = "";
			String recoverieTable = "";
			String interestCalc = "", reinterestcalcdate = "";
			String getEditedPensionno="";
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
			log.info("region in servelt ***** "
					+ request.getParameter("frm_year"));
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

			if (request.getParameter("frm_bulkprint") != null) {
				chkBulkPrint = request.getParameter("frm_bulkprint");
			}
			// for cheking flag for which recoverie table to hit
			if (request.getParameter("finalrecoverytableFlag") != null) {
				recoverieTable = request.getParameter("finalrecoverytableFlag");
			}
			pensionContributionList = finReportService
					.getPensionContributionReport(toYear, finYear, region,
							airportCode, selectedMonth, empserialNO, cpfAccno,
							transferFlag, mappingFlag, pfidString,
							chkBulkPrint, recoverieTable);
			double interestforNoofMonths = finReportService
					.getInterestforNoofMonths(interestCalc);
			String reIntrestDate = finReportService.reIntrestDate(empserialNO);
			double interestforfinalsettleMonths = finReportService
					.interestforfinalsettleMonths(reinterestcalcdate,
							empserialNO);
			String finalintrestdate = finReportService.finalintrestdate(
					reinterestcalcdate, empserialNO);
			String reSettlementdate = finReportService
					.reSettlementdate(empserialNO);
		
			double pctotal = 0.0;
			double intrest = 0.0;
			String retireddate = "";
			if (recoverieTable.equals("true")) {
				pctotal = financeService.getPensionContributionTotal(empserialNO);
				intrest = financeService.getfinalrevoveryintrest(empserialNO);
				getEditedPensionno=financeService.getEditedPensionno(empserialNO);
			}
			
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
			log.info("----"
					+ request.getAttribute("interestCalcfinal").toString());
			if (page.equals("")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionContributionReport.jsp");
				rd.forward(request, response);
			} else if (page.equals("PensionContribution")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionContributionReport.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionContributionScreen.jsp?recoverieTable="
								+ recoverieTable);
				rd.forward(request, response);
			}
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
				finReportService.deleteTransactionData(cpfAccno, monthYear,
						region, airportcode, ComputerName, username, pfid);
			}
			int insertedRec = finReportService.preProcessAdjOB(pfid);
			// log.info("deleteTransactionData=============Current
			// Date========="
			// +
			// commonUtil.getCurrentDate("dd-MMM-yyyy")+"insertedRec"+insertedRec
			// );
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region = "NO-SELECT";
			String pfidStrip = "1 - 1";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip;

			String url = "./reportservlet?method=getReportPenContr" + params;
			log.info("url is " + url);
			// RequestDispatcher rd =
			// request.getRequestDispatcher(
			// "./search1?method=searchRecordsbyEmpSerailNo");
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("editTransactionData")) {

			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00", contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "";
			String pensionoption = "";
			String recoverieTable = "";
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
			if (request.getParameter("recoverieTable") != null) {
				recoverieTable = request.getParameter("recoverieTable");
			}

			finReportService.editTransactionData(cpfAccno, monthYear,
					emoluments, vpf, principle, interest, contri, advance,
					loan, aailoan, pfid, region, airportcode, username,
					ComputerName, from7narration, pcheldamt, noofmonths,
					arrearflag, duputationflag, pensionoption, recoverieTable,
					epf);

			int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec"
					+ insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip + "&page=" + page + "&mappingFlag="
					+ mappingFlag;

			String url = "./reportservlet?method=getReportPenContr" + params;
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
			double pensionContribution = commonDAO.pensionCalculation(monthYear,
					emoluments, penionOption, "", "");
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

		} else if (request.getParameter("method").equals("cardEdit")) {
			String region = "", year = "";
			String reportType = "";
			ArrayList list = new ArrayList();
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			String pensionno = "";
			if (request.getParameter("pfid") != null) {
				pensionno = request.getParameter("pfid");
			}
			if (!pensionno.equals("")) {
				list = finReportService.pfCardEdit(region, year, pensionno);
			}
			request.setAttribute("cardList", list);
			String shnYear = "";
			request.setAttribute("reportType", reportType);
			request.setAttribute("dspYear", shnYear);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/EmpFinalSettlementEdit.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updateMonthlyCpf")) {
			String region = "", monthYear = "", emoluments = "", cpf = "", vpf = "", principle = "", interest = "", pfid = "", wetherOption = "";
			String[] updatedMonths = {};
			String[] updateTransactions = {};
			if (request.getParameterValues("monthInfo") != null) {
				updatedMonths = request.getParameterValues("monthInfo");
				for (int i = 0; i < updatedMonths.length; i++) {
					log.info(updatedMonths[i]);
					updateTransactions = updatedMonths[i].split(",");
					for (int j = 0; j < updateTransactions.length; j++) {
						monthYear = updateTransactions[0].substring(1,
								updateTransactions[0].length() - 1);
						emoluments = updateTransactions[1].substring(1,
								updateTransactions[1].length() - 1);
						cpf = updateTransactions[2].substring(1,
								updateTransactions[2].length() - 1);
						vpf = updateTransactions[3].substring(1,
								updateTransactions[3].length() - 1);
						principle = updateTransactions[4].substring(1,
								updateTransactions[4].length() - 1);
						interest = updateTransactions[5].substring(1,
								updateTransactions[5].length() - 1);
						pfid = updateTransactions[6].substring(1,
								updateTransactions[6].length() - 1);
						wetherOption = updateTransactions[7].substring(1,
								updateTransactions[7].length() - 1);

						pfid = commonUtil.getSearchPFID1(pfid);
						finReportService.updateMonthlyCpfRecoverieData(pfid,
								monthYear, emoluments, cpf, vpf, principle,
								interest, wetherOption);
					}

				}

			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/EmpPfcardEdit.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("cardReport")) {
			String region = "", year = "";
			String reportType = "", sortingOrder = "", pfidString = "", lastmonthFlag = "", lastmonthYear = "", frmName = "";
			ArrayList list = new ArrayList();
			String airportCode = "", bulkPrintFlag = "", signature = "",mailStatus="";

			if (request.getParameter("mailStatus") != null) {
				mailStatus = request.getParameter("mailStatus");
			}
			if (request.getParameter("frm_signature") != null) {
				signature = request.getParameter("frm_signature");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("frm_blkprintflag") != null) {
				bulkPrintFlag = request.getParameter("frm_blkprintflag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("frmAirportCode") != null) {
				airportCode = request.getParameter("frmAirportCode");
			}

			if (frmName.equals("CPFDeviation")) {
				if (request.getParameter("frm_year") != null) {
					year = request.getParameter("frm_year");
				}

				String[] arr = year.split("-");
				String fromYear = arr[0];
				String toYear = arr[1].substring(2, 4);
				year = fromYear;

			} else {
				if (request.getParameter("frm_year") != null) {
					year = request.getParameter("frm_year");
				}
			}
			if (request.getParameter("frm_region") != null
					&& request.getParameter("frm_region") != "") {
				region = request.getParameter("frm_region");
			}
			log.info("region " + region);
			if (request.getParameter("frm_pfids") != null
					&& request.getParameter("frm_pfids") != "") {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
			String empName = "",fromYear="",toYear="";
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
			if (request.getParameter("transMonthYear") != null) {
				lastmonthYear = request.getParameter("transMonthYear");
			}
			if (request.getParameter("frm_ltstmonthflag") != null) {
				lastmonthFlag = request.getParameter("frm_ltstmonthflag");
			}
			log.info("===cardReport=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "lastmonthYear=="
					+ lastmonthYear);
			if (pfidString.equals("")) {
			System.out.println("pfCardReport");
				list = finReportService.pfCardReport(region, year, empflag,
						empName, sortingOrder, pensionno);
			} else {
				System.out.println("pfCardReportPrinting");
				list = finReportService.pfCardReportPrinting(pfidString,
						region, year, empflag, empName, sortingOrder,
						pensionno, lastmonthFlag, lastmonthYear, airportCode,
						bulkPrintFlag);
			}
			if (!year.equals("Select One")) {
				fromYear = "01-Apr-" + year;
				int toSelectYear = 0;
				toSelectYear = Integer.parseInt(year) + 1;
				toYear = "01-Mar-" + toSelectYear;
			} else {
				fromYear = "01-Apr-2011";
				toYear = "31-Mar-2012";
			}
			request.setAttribute("cardList", list);

			String nextYear = "", shnYear = "";
			if (!year.equals("Select One")) {
				int nxtYear = Integer.parseInt(year.substring(2, 4)) + 1;
				if (nxtYear >= 1 && nxtYear <= 9) {
					nextYear = "0" + nxtYear;
				} else {
					log.info("==========nextYear=====" + nextYear.length());
					nextYear = Integer.toString(nxtYear);

					if (nextYear.length() == 3) {
						nextYear = "00";
					}
				}
				shnYear = year + "-" + nextYear;
			} else {
				shnYear = "1995-2010";
			}
			request.setAttribute("reportType", reportType);
			request.setAttribute("fromYear", fromYear);
			request.setAttribute("toYear", toYear);
			String regionString = "", stationString = "";
			if (region.equals("NO-SELECT")) {
				regionString = "";
			} else {
				regionString = region;
			}
			if (airportCode.equals("NO-SELECT")) {
				stationString = "";
			} else {
				if (region.equals("CHQIAD")) {
					stationString = airportCode;
				} else {
					stationString = "";
				}

			}
			
			String userType =session.getAttribute("usertype").toString();
			if(userType.equals("Employee") && Integer.parseInt(year)==2020){
				signature="true";
				bulkPrintFlag="true";
				regionString=finReportService.getIndRegion(pensionno);
				stationString=finReportService.getIndStation(pensionno);
			}
			if(mailStatus.equals("true") && Integer.parseInt(year)==2020){
				signature="true";
				bulkPrintFlag="true";
				regionString=finReportService.getIndRegion(pensionno);
				stationString=finReportService.getIndStation(pensionno);
			}
			
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", regionString);
			request.setAttribute("airportCode", stationString);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("blkprintflag", bulkPrintFlag);
			request.setAttribute("signature", signature);
			String path="";
			if(Integer.parseInt(year)>=2015) {
				path="./PensionView/NewPensionEPFReportCard.jsp";
			}else {
				path="./PensionView/PensionEPFReportCard.jsp";
			}
			log.info("path =="+path+"year =="+year);
			RequestDispatcher rd = request
					.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("allRegionForm3")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "";
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfaccno";
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			String empName = "";
			String empflag = "false";
			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			log.info("region==========getform3=====" + region + "year" + year
					+ "month" + month);
			String fullMonthName = "";
			if (!month.equals("00")) {
				frmMonthYear = "%" + "-" + month + "-" + year;
				disMonthYear = month + "-" + year;
				try {
					displayDate = commonUtil.converDBToAppFormat(disMonthYear,
							"MM-yyyy", "MMM-yyyy");
					int months = 0;
					months = Integer.parseInt(month) - 1;
					log.info("months=================" + months
							+ "month=========" + month);
					if (months < 10) {
						month = "0" + months;
					} else {
						month = Integer.toString(months);
					}
					disMonthYear = month + "-" + year;
					log.info("disMonthYear=================" + disMonthYear
							+ "month=========" + month);
					fullMonthName = commonUtil.converDBToAppFormat(
							disMonthYear, "MM-yyyy", "MMMM-yyyy");
					selectedDate = "%-" + displayDate;
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}

			} else {
				selectedDate = year;
				displayDate = year;
			}
			ArrayList form3List = new ArrayList();

			form3List = financeService.financeForm3ReportAll(selectedDate,
					region, fullMonthName, sortingOrder, empName, empflag);

			log.info("selectedDate==========getform3=====" + selectedDate
					+ "form3==Size==" + form3List.size());

			request.setAttribute("displayDate", fullMonthName);
			request.setAttribute("form3List", form3List);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = null;

			rd = request
					.getRequestDispatcher("./PensionView/PensionForm3ReportAll.jsp");

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("form3pfid")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";

			String formType = "", reportType = "", airportcode = "", sortingOrder = "";

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			}
			log.info("region==========getform3=====" + region + "year" + year
					+ "month" + month + "airportcode" + airportcode
					+ "sortingOrder====" + sortingOrder);
			String fullMonthName = "";
			if (!month.equals("00")) {
				frmMonthYear = "%" + "-" + month + "-" + year;
				disMonthYear = month + "-" + year;
				try {
					displayDate = commonUtil.converDBToAppFormat(disMonthYear,
							"MM-yyyy", "MMM-yyyy");
					int months = 0;
					months = Integer.parseInt(month) - 1;
					log.info("months=================" + months
							+ "month=========" + month);
					if (months < 10) {
						month = "0" + months;
					} else {
						month = Integer.toString(months);
					}
					disMonthYear = month + "-" + year;
					log.info("disMonthYear=================" + disMonthYear
							+ "month=========" + month);
					fullMonthName = commonUtil.converDBToAppFormat(
							disMonthYear, "MM-yyyy", "MMMM-yyyy");
					selectedDate = "01-" + displayDate;
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}

			} else {
				selectedDate = year;
				displayDate = year;
			}

			ArrayList form3PSList = new ArrayList();
			form3PSList = finReportService.financeForm3ByPFID(selectedDate,
					region, airportcode, reportType, sortingOrder);
			request.setAttribute("form3PSList", form3PSList);
			request.setAttribute("displayDate", displayDate);
			request.setAttribute("selectedDate", selectedDate);
			request.setAttribute("reportType", reportType);
			log.info("displayDate" + displayDate + "formType============="
					+ formType);
			RequestDispatcher rd = null;
			if (formType.equals("FORM-3-PFID")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm3ByPFIDReport.jsp");
			} else if (formType.equals("FORM-3-PS-PFID")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm3PSByPFIDReport.jsp");
			}

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadForm6Comp")) {
			String fromYear = "", toYear = "", fromMonth = "", toMonth = "", region = "", reporType = "", formType = "";
			ArrayList regionList = new ArrayList();
			if (request.getParameter("frm_year") != null) {
				fromYear = request.getParameter("frm_year");
			}
			if (request.getParameter("to_year") != null) {
				toYear = request.getParameter("to_year");
			}
			if (request.getParameter("frm_month") != null) {
				fromMonth = request.getParameter("frm_month");
			}
			if (request.getParameter("to_month") != null) {
				toMonth = request.getParameter("to_month");
			}
			if (request.getParameter("frm_reportType") != null) {
				reporType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}

			if (region.equals("NO-SELECT")) {

				HashMap regionHashmap = new HashMap();
				HashMap sortedRegionMap = new LinkedHashMap();

				Iterator regionIterator = null;
				regionHashmap = commonUtil.getRegion();

				Set keys = regionHashmap.keySet();
				regionIterator = keys.iterator();
				String rgn = "";
				while (regionIterator.hasNext()) {
					rgn = regionHashmap.get(regionIterator.next()).toString();

					regionList.add(rgn);
				}

			} else {
				regionList.add(region);
			}
			String fromDate = "", toDate = "";

			if (!fromYear.equals("NO-SELECT") && !toYear.equals("NO-SELECT")) {
				if (!fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;

				} else if (fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-04-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else if (!fromMonth.equals("NO-SELECT")
						&& toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-03-" + toYear;
				} else {
					fromDate = "01-04-" + fromYear;
					toDate = "01-03-" + toYear;
				}
			} else if (fromYear.equals("NO-SELECT")
					&& !toYear.equals("NO-SELECT")) {
				fromYear = "1995";
				if (!fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else if (fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-04-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else if (!fromMonth.equals("NO-SELECT")
						&& toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-03-" + toYear;
				} else {
					fromDate = "01-04-" + fromYear;
					toDate = "01-03-" + toYear;
				}
			} else if (!fromYear.equals("NO-SELECT")
					&& toYear.equals("NO-SELECT")) {
				toYear = "2008";
				if (!fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else if (fromMonth.equals("NO-SELECT")
						&& !toMonth.equals("NO-SELECT")) {
					fromDate = "01-04-" + fromYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else if (!fromMonth.equals("NO-SELECT")
						&& toMonth.equals("NO-SELECT")) {
					fromDate = "01-" + fromMonth + "-" + fromYear;
					toDate = "01-03-" + toYear;
				} else {
					fromDate = "01-04-" + fromYear;
					toDate = "01-03-" + toYear;
				}
			} else if (fromYear.equals("NO-SELECT")
					&& toYear.equals("NO-SELECT")) {
				fromDate = "01-04-1995";
				toDate = "01-03-2008";
			}
			int totalYears = 0;
			ArrayList dateList = new ArrayList();
			int fromYears = 0, toYears = 0;
			String dispFromYear = "", dispToYear = "", frmSeltYear = "", toSeltYear = "", finDspMnthYear = "";
			try {
				String fromSelYear = commonUtil.converDBToAppFormat(fromDate,
						"dd-MM-yyyy", "yyyy");
				dispFromYear = commonUtil.converDBToAppFormat(fromDate,
						"dd-MM-yyyy", "dd-MMM-yyyy");
				dispToYear = commonUtil.converDBToAppFormat(toDate,
						"dd-MM-yyyy", "dd-MMM-yyyy");
				String fromSelWithOutYear = commonUtil.converDBToAppFormat(
						fromDate, "dd-MM-yyyy", "dd-MMM-");
				String toSelYear = commonUtil.converDBToAppFormat(toDate,
						"dd-MM-yyyy", "yyyy");
				String toSelWithOutYear = commonUtil.converDBToAppFormat(
						toDate, "dd-MM-yyyy", "dd-MMM-");
				totalYears = Integer.parseInt(toSelYear)
						- Integer.parseInt(fromSelYear);

				StringBuffer buffer = new StringBuffer();
				if (totalYears != 0) {
					boolean yearFlag = false;
					for (int i = 0; i < totalYears; i++) {
						fromYears = Integer.parseInt(fromSelYear) + i;
						toYears = fromYears + 1;
						frmSeltYear = fromSelWithOutYear + fromYears;

						/*
						 * if(!formType.equals("FORM-6-PS-1995") &&
						 * yearFlag==false){ String[]
						 * mnthSel=frmSeltYear.split("-"); frmSeltYear="";
						 * String days="",mnths="Dec",yers=""; days=mnthSel[0];
						 * yers=mnthSel[2]; frmSeltYear=days+"-"+mnths+"-"+yers;
						 * yearFlag=true; }
						 */
						toSeltYear = toSelWithOutYear + toYears;
						log.info("frmSeltYear" + frmSeltYear + "Select Year"
								+ toSeltYear);
						buffer.append(frmSeltYear);
						buffer.append(",");
						buffer.append(toSeltYear);
						dateList.add(buffer.toString());
						fromYears = 0;
						toYears = 0;
						frmSeltYear = "";
						toSeltYear = "";
						buffer = null;
						buffer = new StringBuffer();
					}
				} else if (totalYears == 0) {
					fromYears = Integer.parseInt(fromSelYear);
					toYears = fromYears;
					frmSeltYear = fromSelWithOutYear + fromYears;
					toSeltYear = toSelWithOutYear + toYears;
					buffer.append(frmSeltYear);
					buffer.append(",");
					buffer.append(toSeltYear);
					dateList.add(buffer.toString());
				}

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				finDspMnthYear = commonUtil.converDBToAppFormat(dispFromYear,
						"dd-MMM-yyyy", "dd-MM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] fromYearArry = finDspMnthYear.split("-");
			int finFromMnh = 0, finFromYear = 0, finToYear = 0, selYear = 0;
			finFromMnh = Integer.parseInt(fromYearArry[1]);
			selYear = Integer.parseInt(fromYearArry[2]);
			if (finFromMnh >= 3 && finFromMnh <= 12) {
				finFromYear = selYear;
				selYear = selYear + 1;
				finToYear = selYear;
			} else if (finFromMnh >= 1 && finFromMnh <= 3) {
				finToYear = selYear;
				selYear = selYear - 1;
				finFromYear = selYear;
			}

			String diplayMessage = "Mar-" + finFromYear + " To " + "Feb-"
					+ finToYear;
			request.setAttribute("diplayMessage", diplayMessage);
			request.setAttribute("fromDate", dispFromYear);
			request.setAttribute("toDate", dispToYear);
			request.setAttribute("dateList", dateList);
			request.setAttribute("region", regionList);
			RequestDispatcher rd = null;
			if (formType.equals("FORM-6-PS-1995")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionReportForm-6Comp1995.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/PensionReportForm-6Comp.jsp");
			}

			rd.forward(request, response);

		} else if (request.getParameter("method").equals("loadForm7")) {
			String region = "", year = "";

			String airportcode = "", reportType = "", sortingOrder = "", formPSFlag = "";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_formPSFlag") != null) {
				formPSFlag = request.getParameter("frm_formPSFlag");
			}
			log.info("formPSFlag======================================"
					+ formPSFlag);
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
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
			if (request.getParameter("frm_airportcd") != null) {
				airportCode = request.getParameter("frm_airportcd");
			}

			list = finReportService.form7Report(year, sortingOrder, region,
					airportCode, pensionno);
			request.setAttribute("form8List", list);
			log.info("==========Form-7=====" + region + "year" + year);
			String nextYear = "";
			int nxtYear = Integer.parseInt(year.substring(2, 4)) + 1;
			log.info("==========Form-7=====" + nxtYear);
			if (nxtYear >= 1 && nxtYear <= 9) {
				nextYear = year.substring(0, 2) + "0" + nxtYear;
			} else {
				log.info("==========nextYear===Length==" + nextYear.length());
				nextYear = Integer.toString(nxtYear);

				if (nextYear.length() == 3) {
					nextYear = Integer.toString(Integer.parseInt(year
							.substring(0, 2)) + 1)
							+ "00";
				} else {
					nextYear = year.substring(0, 2) + nxtYear;
				}
			}
			String shnYear = "01-Apr-" + year + " To Mar-" + nextYear;
			request.setAttribute("chkYear", year);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
		
			RequestDispatcher rd = null;
			if (formPSFlag.equals("yes")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm7PSReport.jsp");
			} else if (formPSFlag.equals("no")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionForm7Report.jsp");
			}

			rd.forward(request, response);

		} else if (request.getParameter("method").equals("form7report")) {
			String region = "", year = "", adjFlag = "";
			String airportcode = "", reportType = "", sortingOrder = "pensionno", pfidString = "", formType = "", fileheadname = "", frmName = "", pcFlag="false";
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
			String formArrRvsFormFlag = "";

			boolean chkMultipleYearFlag = false;
			log.info("pfidString" + pfidString + "year" + year + "sortingOrder"
					+ sortingOrder + "region" + region + "airportCode"
					+ airportCode + "pensionno" + pensionno+"formType"+formType);
			String formVal = "";
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

			if (formType.equals("FORM-78PS-SUMMARY")) {
				list = finReportService.rnfcForm78SummaryReport(year,
						pensionno, pfidString, empflag, empName, "N", adjFlag,
						frmName,pcFlag);
			} else {
				list = finReportService.allYearsForm7PrintOutForPFID(
						pfidString, year, sortingOrder, region, airportCode,
						pensionno, empflag, empName, formVal,
						formArrRvsFormFlag, adjFlag, frmName,pcFlag);
				String minMaxYear = "";
				if (formVal.equals("Summary") || formVal.equals("Revised")) {
					if ((year.equals("NO-SELECT") || year.equals("Select One"))) {
						minMaxYear = finReportService
								.getMinMaxYearsForArrearBreakup(pensionno);
						if (minMaxYear.equals("")) {
							minMaxYear = "2006";
						}
					} else {
						if (!((year.equals("NO-SELECT") || year
								.equals("Select One")))) {
							if (year.indexOf("-") != -1) {
								String[] minMaxYearList = year.split("-");
								minMaxYear = minMaxYearList[0];
							} else {
								minMaxYear = year;
							}
						}
					}
					log.info("minMaxYear========" + minMaxYear);
					revisedDataList = finReportService.allYearsForm7PrintOutForPFID(pfidString,
									minMaxYear, sortingOrder, region,
									airportCode, pensionno, empflag, empName,
									formVal, "Y", adjFlag, frmName,pcFlag);
					if (formVal.equals("Revised")) {
						list = revisedDataList;
					} else {
						list.addAll(revisedDataList);
					}

				}
				log.info("formVal" + formVal + "revisedDataList"
						+ revisedDataList.size());
				chkMultipleYearFlag = true;

				year = "1995";

			}

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
				if (!pensionno.equals("") && !fileheadname.equals("NO-SELECT")) {
					fileNameMessage = "PFID_" + pensionno + "_(" + fileheadname
							+ "-" + (Integer.parseInt(fileheadname) + 1) + ")";
				} else if (!pensionno.equals("")) {
					fileNameMessage = "PFID_" + pensionno;
				}
				request.setAttribute("fileNameMessage", fileNameMessage);
				if (chkMultipleYearFlag == true) {
					if (formType.equals("FORM-7PS-SUMMARY")) {
						rd = request
								.getRequestDispatcher("./PensionView/PensionForm7PSPFIDSummaryReport.jsp");

					} else if (formType.equals("FORM-7PS-SUMMARY[REV]")) {
						rd = request
								.getRequestDispatcher("./PensionView/PensionForm7PSPFIDSummaryREVReport.jsp");
					} else if (formType.equals("FORM-7PS-REVISED")) {
						rd = request
								.getRequestDispatcher("./PensionView/PensionForm7PSRevisedBulkReport.jsp");
					} else {
						rd = request
								.getRequestDispatcher("./PensionView/PensionForm7PSPFIDReport.jsp");
					}
				} else if (formType.equals("FORM-78PS-SUMMARY")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm78PSreport.jsp");
				} else if (formType.equals("FORM-7PS-REVISED")) {
					request.setAttribute("xlsPurspose", xlsPurspose);
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm7PSRevisedReport.jsp");
				} else {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm7PSReport.jsp");
				}
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
		} else if (request.getParameter("method").equals("loadForm8")) {
			String region = "", year = "", month = "", adjFlag = "", frmName = "";
			Form8Bean form8Bean = new Form8Bean();
			String airportcode = "", reportType = "", sortingOrder = "", formType = "", range = "",pcFlag="false";
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_pfids") != null) {
				range = request.getParameter("frm_pfids");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
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
			if (request.getParameter("frm_airportcd") != null) {
				airportCode = request.getParameter("frm_airportcd");
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
			String formTypeRevisedFlag = "N";
			if (formType.equals("FORM-8-PS-REVISED")) {
				formTypeRevisedFlag = "Y";
			} else if (formType.equals("FORM-8-PS-VERFICATION")
					|| formType.equals("FORM-8-PS-VERFICATION[REV]")) {
				formTypeRevisedFlag = "";
			} else {
				formTypeRevisedFlag = "N";
			}
			ArrayList form8List = new ArrayList();
			boolean chkMultipleYearFlag = false, bulkPFIDsType = false, otherReport = false, chkYearFlag = false;

			log.info("range=======================" + range);

			if (!pensionno.equals("")
					&& (range.equals("NO-SELECT") || (range.equals("1 - 1")))) {
				if (year.equals("NO-SELECT") || year.equals("Select One")) {
					chkYearFlag = true;
				} else {
					chkYearFlag = false;
				}
				bulkPFIDsType = false;
			} else if (pensionno.equals("")
					&& (!(year.equals("NO-SELECT") || year.equals("Select One")))
					&& (range.equals("NO-SELECT") || (range.equals("1 - 1")))) {
				bulkPFIDsType = true;
				chkYearFlag = true;
			} else {
				bulkPFIDsType = true;
			}
/*
			log.info("bulkPFIDsType" + bulkPFIDsType + "chkYearFlag"
					+ chkYearFlag + "formType" + formType);*/
			if (bulkPFIDsType == true && chkYearFlag == false) {
				form8List = finReportService.getRPFCForm8PFIDIndivReport(year,
						"NO-SELECT", sortingOrder, region, false, airportCode,
						pensionno, range, empflag, empName, formType,
						formTypeRevisedFlag, adjFlag, frmName,pcFlag);
				chkMultipleYearFlag = false;
			} else if ((bulkPFIDsType == false && chkYearFlag == false)
					|| (bulkPFIDsType == false && chkYearFlag == true)) {
				form8List = finReportService.getRPFCForm8IndivReport(year,
						"NO-SELECT", sortingOrder, region, false, airportCode,
						pensionno, "NO-SELECT", empflag, empName, formType,
						formTypeRevisedFlag, adjFlag, frmName, pcFlag);
				chkMultipleYearFlag = false;
			} else {
				//Not Implemented pcFlag for this Method
				form8List = finReportService.form8Report(year, month,
						sortingOrder, region, airportCode, pensionno, formType,
						adjFlag, frmName );
			}

			// request.setAttribute("form8List", form8Bean);
			request.setAttribute("form8List", form8List);

			/*log.info("==========Form-8=====" + region + "year" + year);*/

			String nextYear = "", finalNextYear = "", shnYear = "", remtYears = "", pensHdingFlag = "", screenHeading = "";
			int years = 0;
			if (chkMultipleYearFlag == false) {
				if (!year.equals("Select One")) {
					int nxtYear = Integer.parseInt(year) + 1;
					nextYear = Integer.toString(nxtYear);
					shnYear = "Mar-" + year + " To Feb-" + nextYear;
					remtYears = year + "-" + nextYear;
				} else {
					shnYear = "Mar-1995 To Feb-2008";
					remtYears = "1995-2008";
				}
			} else {
				shnYear = "";
			}

			request.setAttribute("dspYear", shnYear);
			request.setAttribute("pensionHding", pensHdingFlag);
			request.setAttribute("dspRemtYears", remtYears);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			if (formType.trim().equals("FORM-8-PS")) {
				screenHeading = "FORM-8 (PS)";
			} else {
				screenHeading = "FORM-8 PS (REVISED)";
			}
			request.setAttribute("screenHeading", screenHeading);
			request.setAttribute("range", range);
			RequestDispatcher rd = null;
			log.info("bulkPFIDsType" + bulkPFIDsType + "chkYearFlag"
					+ chkYearFlag + "formType" + formType);
			if (bulkPFIDsType == true && chkYearFlag == false) {
				if (formType.trim().equals("FORM-8-PS-REVISED")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSRevisedReport.jsp");
				} else if (formType.equals("FORM-8-PS-VERFICATION")) {
					request.setAttribute("range", range);
					log.info("formType=================================="
							+ formType);
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSBulkPFIDSVerifyReport.jsp");
				} else if (formType.equals("FORM-8-PS-VERFICATION[REV]")) {
					request.setAttribute("range", range);
					log.info("formType=================================="
							+ formType);
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSBulkPFIDSVerifyREVReport.jsp");

				} else {

					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSBulkPFIDSReport.jsp");
				}

			} else if ((bulkPFIDsType == false && chkYearFlag == false)
					|| (bulkPFIDsType == false && chkYearFlag == true)) {
				if (formType.trim().equals("FORM-8-PS-REVISED")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSRevisedBulkPFIDSReport.jsp");
				} else {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSPFIDReport.jsp");
				}
			} else {
				if (formType.equals("FORM-8")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8Report.jsp");
				} else if (formType.equals("FORM-8-PS-VERFICATION")) {
					request.setAttribute("range", range);
					log.info("formType=================================="
							+ formType);
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSBulkPFIDSVerifyReport.jsp");
				} else if (formType.equals("FORM-8-PS")
						|| formType.equals("FORM-8-PS-Arrear")) {
					rd = request
							.getRequestDispatcher("./PensionView/PensionForm8PSReport.jsp");
				}
			}

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("payarrears")) {
			String region = "", year = "", adjFlag = "";
			String airportcode = "", reportType = "", sortingOrder = "pensionno", pfidString = "", formType = "", frmName = "",pcFlag="false";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
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
			boolean chkMultipleYearFlag = false;
			log.info("pfidString" + pfidString + "year" + year + "sortingOrder"
					+ sortingOrder + "region" + region + "airportCode"
					+ airportCode + "pensionno" + pensionno);
			String formVal = "";
			if (formType.equals("FORM-7PS-SUMMARY")) {
				formVal = "Summary";
			} else {
				formVal = "NonSummary";
			}
			//pcFlag is for Getting PC after Seperation Only For Form 7& 8 PS Purpose
			if (year.equals("NO-SELECT") && !pensionno.equals("")) {

				list = finReportService.allYearsForm7PrintOutForPFID(
						pfidString, year, sortingOrder, region, airportCode,
						pensionno, empflag, empName, formVal, "N", adjFlag,
						frmName,pcFlag);
				chkMultipleYearFlag = true;
				year = "1995";
			} else {
				list = finReportService.form7PrintingReport(pfidString, year,
						sortingOrder, region, airportCode, pensionno, empflag,
						empName, formVal, "N", adjFlag, frmName,pcFlag);
			}

			request.setAttribute("form8List", list);
			/*log.info("==========Form-7=====" + region + "year" + year);*/
			String nextYear = "", shnYear = "";
			if (chkMultipleYearFlag == false) {
				int nxtYear = Integer.parseInt(year) + 1;
				nextYear = Integer.toString(nxtYear);
				shnYear = "01-Apr-" + year + " To Mar-" + nextYear;
			} else {
				shnYear = "";
			}

			request.setAttribute("chkYear", year);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			RequestDispatcher rd = null;

			rd = request
					.getRequestDispatcher("./PensionView/PensionPayArrears.jsp");

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updateTrans")) {
			CommonDAO commonDao = new CommonDAO();
			commonDao.updateTransPensionData();
		} else if (request.getParameter("method").equals("updatePCReport")) {
			String region = "", finYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "", empserialNO = "";
			String cpfAccno = "", page = "";
			String transferFlag = "", pfidString = "", chkMappingFlag = "";
			int totalRecords = 0;

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
			String mappingFlag = "";
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
				empserialNO = commonUtil.getSearchPFID(request.getParameter(
						"empserialNO").toString().trim());
			}
			// /
			// empserialNO=commonUtil.trailingZeros(empserialNO.toCharArray());
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_chkMappingFlag") != null) {
				chkMappingFlag = request.getParameter("frm_chkMappingFlag");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportCode = request.getParameter("frm_airportcode");

			} else {
				airportCode = "NO-SELECT";
			}
			if (selectedMonth.equals("NO-SELECT")) {
				selectedMonth = "03";
			}
			/*log.info("region====" + region + "finYear" + finYear + "reportType"
					+ reportType + "airportCode==" + airportCode + "Month==="
					+ selectedMonth + "chkMappingFlag" + chkMappingFlag);*/
			totalRecords = finReportService.updatePCReport(finYear, region,
					airportCode, selectedMonth, empserialNO, cpfAccno,
					transferFlag, mappingFlag, pfidString, chkMappingFlag);

			String txtMessage = "Total" + totalRecords
					+ "records are inserted successfully";
			RequestDispatcher rd = request
					.getRequestDispatcher("./reportservlet?method=loadUpdatePC&message="
							+ txtMessage);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updateOB")) {
			String region = "", year = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", sortingOrder = "", pfidString = "", chkPendingFlag = "", stationName = "";
			ArrayList list = new ArrayList();
			int totaolPFIDupdated = 0;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (!request.getParameter("frm_airportcode").equals("")) {
				stationName = request.getParameter("frm_airportcode");
			} else {
				stationName = "";
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}
			String empName = "";
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
			if (request.getParameter("frm_chkPendingFlag") != null) {
				chkPendingFlag = request.getParameter("frm_chkPendingFlag");
			}
			/*log.info("===cardReport=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "chkPendingFlag"
					+ chkPendingFlag);
*/
			int totalIns = finReportService.updateOBCardReport(pfidString,
					region, year, empflag, empName, sortingOrder, pensionno,
					chkPendingFlag, stationName);
			String txtMessage = "Total" + totalIns
					+ "records are inserted successfully";
			RequestDispatcher rd = request
					.getRequestDispatcher("./reportservlet?method=loadUpdatePensionContribut&message="
							+ txtMessage);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("form7reportzero")) {
			String region = "", year = "", adjFlag = "";
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "";
			ArrayList list = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
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
			if (request.getParameter("adjFlag") != null) {
				adjFlag = request.getParameter("adjFlag");
			}
			log.info("pfidString" + pfidString + "year" + year + "sortingOrder"
					+ sortingOrder + "region" + region + "airportCode"
					+ airportCode + "pensionno" + pensionno);
			list = finReportService.form7ZeroPrintingReport(pfidString, year,
					sortingOrder, region, airportCode, pensionno, empflag,
					empName, adjFlag);
			request.setAttribute("form8List", list);
			String nextYear = "";
			int nxtYear = Integer.parseInt(year.substring(2, 4)) + 1;
			
			if (nxtYear >= 1 && nxtYear <= 9) {
				nextYear = year.substring(0, 2) + "0" + nxtYear;
			} else {
				
				nextYear = Integer.toString(nxtYear);

				if (nextYear.length() == 3) {
					nextYear = Integer.toString(Integer.parseInt(year
							.substring(0, 2)) + 1)
							+ "00";
				} else {
					nextYear = year.substring(0, 2) + nxtYear;
				}
			}
			String shnYear = "01-Apr-" + year + " To Mar-" + nextYear;
			request.setAttribute("chkYear", year);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/PensionForm7PSReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("rpfcform6areport")) {

			String region = "", regionDesc = "", year = "", month = "", monthDesc = "", toYear = "", shnYear = "",ECRStatus="",monthYear="";
			String path="",redirectPath="",filenames="", airportcode = "",formType="" ,reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "", empflag = "false", pensionno = "", frmSelectedDts = "",prevFrmSelectedDts="";
			String currdate="",currYear="",selCurrYear="";
			String [] monthYearStr= null;
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();
			ArrayList prevEpfForm3List = new ArrayList();
			Map map = new LinkedHashMap();
			
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
				
				System.out.println("year==="+year);
				
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
				System.out.println("toYear==="+toYear);
				
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
				System.out.println("month==="+month);
			}
			
			 monthYear="01-"+month+"-"+year;
			  currdate=commonUtil.getCurrentDate("MM");
			  log.info("monthYear==<<<>>==="+monthYear);
			  currYear=commonUtil.getCurrentDate("yyyy");
				 log.info("currdate"+currdate);
				 if(Integer.parseInt(currdate)<4){
					 currYear=(""+(Integer.parseInt(currYear)-1)).trim();
					 
				 }
				 log.info("currYear"+currYear);
				 selCurrYear=currYear+"-"+(Integer.parseInt(currYear.substring(2,4))+1);
			
			
			
			
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
				System.out.println("airportcode==="+airportcode);
			}
			
			if (request.getParameter("frm_formtype") != null) {
				formType = request.getParameter("frm_formtype");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "PENSIONNO";
			}

			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			try {
				 monthYear=commonUtil.converDBToAppFormat(monthYear, "dd-MM-yyyy","dd-MMM-yyyy");
				frmSelectedDts = commonUtil.getFromToDates(year, toYear, month,
						month);
				
	
				/*int month1=(Integer.parseInt(month)-1);
				prevFrmSelectedDts=commonUtil.getFromToDates(year, toYear, "",
						month);*/
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("===Form-6A=====" + region + "year" + frmSelectedDts
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno);
			
			monthYearStr  =  frmSelectedDts.split(",");
			ECRStatus = commonDAO.ChkFreezedMnthinECR(monthYearStr[0]);
			
			EPFFormsReportService epfformsService = new EPFFormsReportService();
			if(reportType.equals("CSV")){
				path = epfformsService.generateEPFForm6AChallan(pfidString,
						region, airportcode, empName, empflag, frmSelectedDts,
						sortingOrder, pensionno);
			}else{
				if(formType.equals("EDLI-FORM")) {
					map  = epfformsService.loadEDLIInspectionChargesReport(pfidString,
							region, airportcode, empName, empflag, frmSelectedDts,
							sortingOrder, pensionno);
					/*prevEpfForm3List = epfformsService.loadEPFForm6AChallanReport(pfidString,
							region, airportcode, empName, empflag, frmSelectedDts,
							sortingOrder, pensionno);*/
				}else if(formType.equals("ECR-FORM-6A")){
					if(ECRStatus.equals("Freezed")){
						epfForm3List = epfformsService.loadFreezedEPFForm6AChallanReport(pfidString,
								region, airportcode, empName, empflag, frmSelectedDts,
								sortingOrder, pensionno);	
						redirectPath="./PensionView/rpfcforms/RPFCFreezedEcrform6AReport.jsp";
					}else{
					epfForm3List = epfformsService.loadEPFForm6AChallanReport(pfidString,
							region, airportcode, empName, empflag, frmSelectedDts,
							sortingOrder, pensionno);	
					redirectPath="./PensionView/rpfcforms/RPFCEcrform6AReport.jsp";
					}
				}if(formType.equals("NEW-ECR")){
			
					epfForm3List = epfformsService.loadEPFForm6AChallanReportEcr(pfidString,
							region, airportcode, empName, empflag, frmSelectedDts,
							sortingOrder, pensionno);	
					redirectPath="./PensionView/rpfcforms/RPFCEcrform6AReportNewEcr.jsp";
					
				}else if(formType.equals("DIFFERMENT-ECR")){
			
					epfForm3List = epfformsService.loadDiffermentReportEcr(pfidString,
							region, airportcode, empName, empflag, frmSelectedDts,
							sortingOrder, pensionno,monthYear,month,year);	
					
				
					
					redirectPath="./PensionView/rpfcforms/DiffermentEcrform6AReport.jsp";
					
				}else{
					
						epfForm3List = epfformsService.loadEPFForm6AReport(pfidString,
								region, airportcode, empName, empflag, frmSelectedDts,
								sortingOrder, pensionno);
						missingPFIDsList = epfformsService.loadEPFMissingTransPFIDs(
								pfidString, region, airportcode, empName, empflag,
								frmSelectedDts, sortingOrder, pensionno);
					}
				
				
			}
			
			 request.setAttribute("monthYear",monthYear);
			log.info("===Form-6A=======missingPFIDsList==="
					+ missingPFIDsList.size());
			request.setAttribute("cardList", epfForm3List);
			//request.setAttribute("prevEpfForm3List",prevEpfForm3List);
			request.setAttribute("missingPFIDsList", missingPFIDsList);

			if (!year.equals("NO-SELECT")) {
				shnYear = year + "-" + toYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}

			if (epfForm3List.size() != 0) {
				totalSub = Integer.toString(epfForm3List.size());
			} else {
				totalSub = "0";
			}
			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}
			try {
				monthDesc = commonUtil.converDBToAppFormat("01-" + month + "-"
						+ commonUtil.getCurrentDate("yyyy"), "dd-MM-yyyy",
						"MMM");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("map",map);
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("totalSub", totalSub);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", monthDesc);
			request.setAttribute("reportType", reportType);
		
			if(reportType.equals("CSV")){
				response.setContentType("text/plain");
				filenames=path.substring(path.indexOf("ECR_"),path.length());
				response.setHeader("Content-Length", String.valueOf(new File(path).length())); 
				response.setHeader("Content-Disposition",
						"attachment;filename="+filenames);
				log.info("Path"+path);
						File file = new File(path);
						FileInputStream fileIn = new FileInputStream(file);
						ServletOutputStream out = response.getOutputStream();
						 
						byte[] outputByte = new byte[1024];
//						copy binary contect to output stream
						while(fileIn.read(outputByte, 0, 1024) != -1)
						{
							out.write(outputByte, 0, 1024);
						}
						fileIn.close();
						out.flush();
						out.close();
			

			}else{
				RequestDispatcher rd ;
				if(formType.equals("ECR-FORM-6A")){
					rd = request
					.getRequestDispatcher(redirectPath);	
					
					
				}else if(formType.equals("NEW-ECR")){
			
					rd = request
					.getRequestDispatcher(redirectPath);	
					
				}else if(formType.equals("DIFFERMENT-ECR")){
				
					rd = request
					.getRequestDispatcher(redirectPath);
					
				}
				else if(formType.equals("EDLI-FORM")){
					rd = request
					.getRequestDispatcher("./PensionView/rpfcforms/RPFCEDLIFormReport.jsp");
				}else{
					rd = request
					.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm6AReport.jsp");	
				}
				rd.forward(request, response);
		
			}
			
		} else if (request.getParameter("method").equals("personalreport")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String formType = "", reportType = "", airportcode = "", sortingOrder = "", seperationResaon = "";

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			}
			if (request.getParameter("frm_reason") != null) {
				seperationResaon = request.getParameter("frm_reason");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (airportcode.equals("[Select One]")) {
				airportcode = "";
			} else if (airportcode.equals("NO-SELECT")) {
				airportcode = "";
			}
			if (region.equals("[Select One]")) {
				region = "";
			} else if (region.equals("NO-SELECT")) {
				region = "";
			}
			if (seperationResaon.equals("[Select One]")) {
				seperationResaon = "NO-SELECT";
			} else if (seperationResaon.equals("NO-SELECT")) {
				seperationResaon = "NO-SELECT";
			}
			log.info("region==========getform3=====" + region + "year" + year
					+ "month" + month + "airportcode" + airportcode);
			String fullMonthName = "";
			if (!year.equals("Select One") || !year.equals("0-[Select One]")) {
				if (!month.equals("0")) {
					frmMonthYear = "%" + "-" + month + "-" + year;
					disMonthYear = month + "-" + year;
					try {
						displayDate = commonUtil.converDBToAppFormat(
								disMonthYear, "MM-yyyy", "MMM-yyyy");
						int months = 0;
						months = Integer.parseInt(month) - 1;
						log.info("months=================" + months
								+ "month=========" + month);
						if (months < 10) {
							month = "0" + months;
						} else {
							month = Integer.toString(months);
						}
						disMonthYear = month + "-" + year;
						log.info("disMonthYear=================" + disMonthYear
								+ "month=========" + month);
						fullMonthName = commonUtil.converDBToAppFormat(
								disMonthYear, "MM-yyyy", "MMMM-yyyy");
						selectedDate = "01-" + displayDate;
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						log.printStackTrace(e);
					}

				} else {
					selectedDate = "01-Mar-" + year;
					displayDate = "01-Mar-" + year;
				}
			}
			log.info("selectedDate=================" + selectedDate);

			ArrayList form3PSList = new ArrayList();
			ArrayList form3RetrList = new ArrayList();
			form3PSList = finReportService.personalReportByPFID(selectedDate,
					region, airportcode, sortingOrder, seperationResaon, false);
			if (seperationResaon.equals("Retirement")) {
				form3RetrList = finReportService.personalReportByPFID(
						selectedDate, region, airportcode, sortingOrder,
						seperationResaon, true);
				request.setAttribute("form3RetrList", form3RetrList);
			}
			// displayDate = "01-Mar-2009";
			// selectedDate = "01-Mar-2009";
			request.setAttribute("form3PSList", form3PSList);
			request.setAttribute("displayDate", selectedDate);
			request.setAttribute("selectedDate", selectedDate);
			if (seperationResaon.equals("NO-SELECT")) {
				seperationResaon = "";
			}
			request.setAttribute("seperationResaon", seperationResaon);
			request.setAttribute("reportType", reportType);
			log.info("displayDate" + displayDate + "formType============="
					+ formType);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/PersonalReportByPFID.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updatePensionContri")) {
			String region = "", year = "";
			String sortingOrder = "", pfidString = "", chkPendingFlag = "";
			int totaolPFIDupdated = 0;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
			String empName = "";
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
			if (request.getParameter("frm_chkPendingFlag") != null) {
				chkPendingFlag = request.getParameter("frm_chkPendingFlag");
			}
			log.info("===cardReport=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "chkPendingFlag"
					+ chkPendingFlag);

			totaolPFIDupdated = finReportService.updatePCTrans(pfidString,
					region, year, chkPendingFlag);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionEPFReportCard.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updatePFID")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "";
			ArrayList list = new ArrayList();
			int totaolPFIDupdated = 0;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
			String empName = "";
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

			log.info("===cardReport=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno);

			totaolPFIDupdated = personalService.updatePFIDTrans(pfidString,
					region, year, empflag, empName, pensionno);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionEPFReportCard.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("updatePFPensProcess")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "", checkPendingFlag = "";
			ArrayList list = new ArrayList();
			int totaolPFIDupdated = 0;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
			String empName = "";
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
			if (request.getParameter("frm_chkPendingFlag") != null) {
				checkPendingFlag = request.getParameter("frm_chkPendingFlag");
			}

			log.info("===cardReport=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "checkPendingFlag===="
					+ checkPendingFlag);

			totaolPFIDupdated = personalService.updatePFPensionTrans(
					pfidString, region, year, airportcode, pensionno,
					checkPendingFlag);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionEPFReportCard.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("trustreport")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "", frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", division = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "", empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList truswistList = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_division") != null) {
				division = request.getParameter("frm_division");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "PENSIONNO";
			}

			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (year.equals("NO-SELECT") && month.equals("NO-SELECT")) {
				selectedDate = "";
			} else {
				if (!month.equals("NO-SELECT")) {
					selectedDate = "01-" + month + "-" + year;
				} else {
					selectedDate = "01-03-" + year;
				}

			}

			log.info("===Trust PC Report=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "selectedDate=" + selectedDate);

			try {
				truswistList = finReportService.loadgetTrustPCReport(
						pfidString, division, region, airportcode, empName,
						empflag, commonUtil.converDBToAppFormat(selectedDate,
								"dd-MM-yyyy", "dd-MMM-yyyy"), sortingOrder,
						pensionno);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			request.setAttribute("cardList", truswistList);

			if (!year.equals("NO-SELECT")) {
				shnYear = year + "-" + toYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}

			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}

			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", month);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/TrustWisePCReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadrpfcform5")) {

			Map monthMap = new LinkedHashMap();
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
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
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					break;
				} else {
					regionHashmap.put(new Integer(i), rgnName);
				}

			}

			request.setAttribute("regionHashmap", regionHashmap);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm5InputParams.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("loadrpfcform5")
				|| request.getParameter("method").equals("loadrpfcform4")
				|| request.getParameter("method").equals("loadrpfcform6a")) {

			Map monthMap = new LinkedHashMap();

			Iterator monthIterator = null;
			Iterator monthIterator1 = null;

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
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					break;
				} else {
					regionHashmap.put(new Integer(i), rgnName);
				}

			}

			request.setAttribute("regionHashmap", regionHashmap);
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadrpfcform4")) {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm4InputParams.jsp");
			} else if (request.getParameter("method").equals("loadrpfcform5")) {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm5InputParams.jsp");
			} else if (request.getParameter("method").equals("loadrpfcform6a")) {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm6AInputParams.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("getrpfcform4")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "", formType = "", pensionno = "", toYear = "";
			ArrayList form4List = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}

			String currentDate = "", prevDate = "";
			if (Integer.parseInt(month) >= 4 && Integer.parseInt(month) <= 12) {
				currentDate = "01/" + month + "/" + year;
				prevDate = "01/" + (Integer.parseInt(month) - 1) + "/" + year;
			} else {
				currentDate = "01/" + month + "/" + toYear;
				if (Integer.parseInt(month) - 1 == 0) {
					prevDate = "01/12/" + year;
				} else {
					prevDate = "01/" + (Integer.parseInt(month) - 1) + "/"
							+ toYear;
				}

			}
			try {
				currentDate = commonUtil.converDBToAppFormat(currentDate,
						"dd/MM/yyyy", "dd/MMM/yyyy");
				prevDate = commonUtil.converDBToAppFormat(prevDate,
						"dd/MM/yyyy", "dd/MMM/yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			form4List = financeService.rpfcForm4Report(airportcode, prevDate,
					currentDate, region, pensionno, sortingOrder);

			log.info("region=====form4List=====" + region + "From year" + year
					+ "month" + month + "pensionno===" + pensionno
					+ "toYear=====" + toYear);
			log.info("Current Date=====getform5=====" + currentDate
					+ "prevDate" + prevDate + "airportcode" + airportcode);
			ArrayList regionList = new ArrayList();
			if (region.equals("NO-SELECT")) {
				regionList = commonUtil.loadRegions();
			} else {
				RegionBean bean = new RegionBean();
				bean.setRegion(region);
				bean.setAirportcode("-NA-");
				regionList.add(bean);
			}

			try {
				selectedDate = commonUtil.converDBToAppFormat(prevDate,
						"dd/MMM/yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("selectedDate", selectedDate);
			request.setAttribute("dataList", form4List);
			request.setAttribute("region", regionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("sortingOrder", sortingOrder);
			RequestDispatcher rd = null;
			if (formType.equals("Form4PS")) {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm4PSReport.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm4Report.jsp");
			}

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("getrpfcform5")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "", formType = "", pensionno = "", toYear = "";
			ArrayList form5List = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}

			String currentDate = "", prevDate = "";
			if (Integer.parseInt(month) >= 4 && Integer.parseInt(month) <= 12) {
				currentDate = "01/" + month + "/" + year;
				prevDate = "01/" + (Integer.parseInt(month) - 1) + "/" + year;
			} else {
				currentDate = "01/" + month + "/" + toYear;
				if (Integer.parseInt(month) - 1 == 0) {
					prevDate = "01/12/" + year;
				} else {
					prevDate = "01/" + (Integer.parseInt(month) - 1) + "/"
							+ toYear;
				}

			}
			try {
				currentDate = commonUtil.converDBToAppFormat(currentDate,
						"dd/MM/yyyy", "dd/MMM/yyyy");
				prevDate = commonUtil.converDBToAppFormat(prevDate,
						"dd/MM/yyyy", "dd/MMM/yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}

			form5List = financeService.rpfcForm5Report(currentDate, prevDate,
					region, pensionno, airportcode, sortingOrder);

			log.info("region=====getform5=====" + region + "From year" + year
					+ "month" + month + "pensionno===" + pensionno
					+ "toYear=====" + toYear);
			log.info("Current Date=====getform5=====" + currentDate
					+ "prevDate" + prevDate + "airportcode" + airportcode);
			ArrayList regionList = new ArrayList();
			if (region.equals("NO-SELECT")) {
				regionList = commonUtil.loadRegions();
			} else {
				RegionBean bean = new RegionBean();
				bean.setRegion(region);
				bean.setAirportcode("-NA-");
				regionList.add(bean);
			}

			try {
				selectedDate = commonUtil.converDBToAppFormat(prevDate,
						"dd/MMM/yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("selectedDate", selectedDate);
			request.setAttribute("dataList", form5List);
			request.setAttribute("region", regionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("sortingOrder", sortingOrder);
			RequestDispatcher rd = null;
			if (formType.equals("Form5PS")) {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm5PSReport.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/rpfcforms/RPFCForm5Report.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("ProcessforAdjOb")) {
			String pensionno = "";
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			financeService.ProcessforAdjOb(pensionno);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write("ReProcess of PF Completed for PFID " + pensionno);
		} else if (request.getParameter("method").equals(
				"loadMonthlyRecoveries")) {
			String year = "", month = "", region = "", aiportcode = "", pfid = "", toYear = "", cpfacno = "", frmSelectedDts = "", pccontrib = "";

			if (request.getParameter("select_month") != "NO-SELECT") {
				month = request.getParameter("select_month").toString();
			}
			/*
			 * if (request.getParameter("select_region") != "NO-SELECT") {
			 * region = request.getParameter("select_region").toString(); }
			 */
			if (request.getParameter("select_airport") != "NO-SELECT") {
				aiportcode = request.getParameter("select_airport").toString();
			}
			if (request.getParameter("empserialNO") != "") {
				pfid = request.getParameter("empserialNO").toString();
			}
			if (request.getParameter("cpfacno") != "") {
				cpfacno = request.getParameter("cpfacno").toString();
			}
			if (request.getParameter("region") != "") {
				region = request.getParameter("region").toString();
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("to_year") != null) {
				toYear = request.getParameter("to_year");
			}

			ArrayList a = new ArrayList();
			AAIEPFFormsReportServlet epfServelet = new AAIEPFFormsReportServlet();
			try {
				frmSelectedDts = epfServelet.getFromToDates(year, toYear,
						month, month);
			} catch (Exception e) {

			}

			a = financeService.getCpfData(pfid, frmSelectedDts, month, region,
					cpfacno);
			log.info("list " + a.size());
			request.setAttribute("cpfRecoveryList", a);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			for (int i = 0; a.size() != 0 && i < a.size(); i++) {
				String name = "", code = "";
				EmployeePensionCardInfo cardInfo = (EmployeePensionCardInfo) a
						.get(i);
				sb.append("<ServiceType>");
				sb.append("<pensionno>");
				sb.append(cardInfo.getPensionNo());
				sb.append("</pensionno>");
				sb.append("<monthyear>");
				sb.append(cardInfo.getMonthyear());
				sb.append("</monthyear>");
				sb.append("<emoluments>");
				sb.append(cardInfo.getEmoluments().trim());
				sb.append("</emoluments>");
				sb.append("<cpf>");
				sb.append(cardInfo.getEmppfstatury().trim());
				sb.append("</cpf>");
				sb.append("<vpf>");
				sb.append(cardInfo.getEmpvpf().trim());
				sb.append("</vpf>");
				sb.append("<principle>");
				sb.append(cardInfo.getPrincipal().trim());
				sb.append("</principle>");
				sb.append("<interest>");
				sb.append(cardInfo.getInterest().trim());
				sb.append("</interest>");
				sb.append("<PCContrib>");
				sb.append(cardInfo.getPensionContribution().trim());
				sb.append("</PCContrib>");
				sb.append("<region>");
				sb.append(cardInfo.getRegion());
				sb.append("</region>");
				sb.append("<advAmount>");
				sb.append(cardInfo.getAdvancesAmount());
				sb.append("</advAmount>");
				sb.append("<loan_sub_amt>");
				sb.append(cardInfo.getAdvancePFWPaid());
				sb.append("</loan_sub_amt>");
				sb.append("<loan_cont_amt>");
				sb.append(cardInfo.getAaiNet());
				sb.append("</loan_cont_amt>");
				sb.append("<cpfacno>");
				sb.append(cpfacno);
				sb.append("</cpfacno>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());

		} else if (request.getParameter("method").equals("saveEmoluments")) {
			String emoluments = "0.00", cpf = "0.00", vpf = "0.00", principle = "0.00", interest = "0.00", pensionno = "", month = "", fromyear = "", toyear = "", transmonthyear = "";
			String advAmount = "0.00", loan_sub_amt = "0.00", loan_cont_amt = "0.00", pccontrib = "0.00", frmSelectedDts = "", region = "";
			String cpfacno = "";
			if (request.getParameter("emoluments") != "") {
				emoluments = request.getParameter("emoluments").toString();
			}
			if (request.getParameter("cpf") != "") {
				cpf = request.getParameter("cpf").toString();
			}
			if (request.getParameter("vpf") != "") {
				vpf = request.getParameter("vpf").toString();
			}
			if (request.getParameter("principle") != "") {
				principle = request.getParameter("principle").toString();
			}
			if (request.getParameter("interest") != "") {
				interest = request.getParameter("interest").toString();
			}
			if (request.getParameter("empserialNO") != "") {
				pensionno = request.getParameter("empserialNO").toString();
			}
			if (request.getParameter("cpfacno") != "") {
				cpfacno = request.getParameter("cpfacno").toString();
			}
			if (request.getParameter("select_month") != "") {
				month = request.getParameter("select_month").toString();
			}
			if (request.getParameter("frm_year") != null) {
				fromyear = request.getParameter("frm_year");
			}
			if (request.getParameter("to_year") != null) {
				toyear = request.getParameter("to_year");
			}
			if (request.getParameter("transmonthyear") != null) {
				transmonthyear = request.getParameter("transmonthyear")
						.toString();
			}
			if (request.getParameter("pccontrib") != null) {
				pccontrib = request.getParameter("pccontrib");
			}

			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("advAmount") != null) {
				advAmount = request.getParameter("advAmount");
			}
			if (request.getParameter("loan_sub_amt") != null) {
				loan_sub_amt = request.getParameter("loan_sub_amt");
			}
			if (request.getParameter("loan_cont_amt") != null) {
				loan_cont_amt = request.getParameter("loan_cont_amt");
			}

			AAIEPFFormsReportServlet epfServelet = new AAIEPFFormsReportServlet();
			try {
				frmSelectedDts = epfServelet.getFromToDates(fromyear, toyear,
						month, month);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			String computerName = session.getAttribute("computername")
					.toString();
			String username = session.getAttribute("userid").toString();
			ArrayList a = financeService.saveCpfAdjustMents(emoluments, cpf,
					vpf, principle, interest, pensionno, month, frmSelectedDts,
					pccontrib, transmonthyear, computerName, username, region,
					advAmount, loan_sub_amt, loan_cont_amt, cpfacno);
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			for (int i = 0; a.size() != 0 && i < a.size(); i++) {
				String name = "", code = "";
				EmployeePensionCardInfo cardInfo = (EmployeePensionCardInfo) a
						.get(i);
				sb.append("<ServiceType>");
				sb.append("<pensionno>");
				sb.append(cardInfo.getPensionNo());
				sb.append("</pensionno>");
				sb.append("<monthyear>");
				sb.append(cardInfo.getMonthyear());
				sb.append("</monthyear>");
				sb.append("<emoluments>");
				sb.append(cardInfo.getEmoluments().trim());
				sb.append("</emoluments>");
				sb.append("<cpf>");
				sb.append(cardInfo.getEmppfstatury().trim());
				sb.append("</cpf>");
				sb.append("<vpf>");
				sb.append(cardInfo.getEmpvpf().trim());
				sb.append("</vpf>");
				sb.append("<principle>");
				sb.append(cardInfo.getPrincipal().trim());
				sb.append("</principle>");
				sb.append("<interest>");
				sb.append(cardInfo.getInterest().trim());
				sb.append("</interest>");
				sb.append("<PCContrib>");
				sb.append(cardInfo.getPensionContribution().trim());
				sb.append("</PCContrib>");
				sb.append("<region>");
				sb.append(region);
				sb.append("</region>");
				sb.append("<advAmount>");
				sb.append(cardInfo.getAdvancesAmount());
				sb.append("</advAmount>");
				sb.append("<loan_sub_amt>");
				sb.append(cardInfo.getAdvancePFWPaid());
				sb.append("</loan_sub_amt>");
				sb.append("<loan_cont_amt>");
				sb.append(cardInfo.getAaiNet());
				sb.append("</loan_cont_amt>");

				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method").equals("form7Indexreport")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "pensionno", pfidString = "";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_airportcd") != null) {
				airportcode = request.getParameter("frm_airportcd");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
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

			log.info("pfidString" + pfidString + "year" + year + "sortingOrder"
					+ sortingOrder + "region" + region + "airportCode"
					+ airportCode + "pensionno" + pensionno);
			list = finReportService.form7PrintingIndexReport(pfidString, year,
					sortingOrder, region, airportCode, pensionno, empflag,
					empName);

			request.setAttribute("form7IndexList", list);
			log.info("==========Form-7 Index Page=====" + region + "year"
					+ year);
			String nextYear = "";
			int nxtYear = Integer.parseInt(year.substring(2, 4)) + 1;
			log.info("==========Form-7 Index Page=====" + nxtYear);
			if (nxtYear >= 1 && nxtYear <= 9) {
				nextYear = year.substring(0, 2) + "0" + nxtYear;
			} else {
				log.info("==========nextYear===Length==" + nextYear.length());
				nextYear = Integer.toString(nxtYear);

				if (nextYear.length() == 3) {
					nextYear = Integer.toString(Integer.parseInt(year
							.substring(0, 2)) + 1)
							+ "00";
				} else {
					nextYear = year.substring(0, 2) + nxtYear;
				}
			}
			String shnYear = "01-Apr-" + year + " To Mar-" + nextYear;
			request.setAttribute("chkYear", year);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", region);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/PensionForm7PSIndexPageReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("PCSummary")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			Form8Bean form8Bean = new Form8Bean();
			String airportcode = "", reportType = "", sortingOrder = "", formType = "";
			String empName = "", airportCode = "", empflag = "false", pensionno = "";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("frm_emp_flag") != null) {
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				empName = request.getParameter("frm_empnm");
			}
			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_airportcd") != null) {
				airportCode = request.getParameter("frm_airportcd");
			}
			ArrayList form8List = new ArrayList();
			String frmMonth = "", fromYear = "", toYear = "";
			if (!month.equals("00")) {
				try {
					frmMonth = commonUtil.converDBToAppFormat(month, "MM",
							"MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					log.printStackTrace(e);
				}
			}
			if (month.equals("00")) {
				fromYear = "01-Apr-" + year;
				toYear = "01-Mar-" + (Integer.parseInt(year) + 1);
			} else if (!month.equals("NO-SELECT")) {
				fromYear = "01-" + frmMonth + "-" + year;
				toYear = fromYear;
			}
			String regionString = "", airportcodeString = "";
			if (!region.equals("NO-SELECT")) {
				regionString = region;
			} else {
				regionString = "";
			}
			if (!airportCode.equals("NO-SELECT")) {
				airportcodeString = airportCode;
			} else {
				airportcodeString = "";
			}
			form8List = finReportService.summaryPCReport(fromYear, toYear,
					sortingOrder, regionString, airportcodeString, empflag,
					empName, pensionno);

			log.info("==========Form-8=====" + form8List.size() + "Region"
					+ region + "year" + year);

			String shnYear = "";
			shnYear = fromYear + " To " + toYear;

			if (!region.equals("NO-SELECT")) {
				regionString = region;
			} else {
				regionString = "All Regions";
			}
			request.setAttribute("form8List", form8List);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("reportType", reportType);

			request.setAttribute("region", region);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/pcsummary/SummaryPCReport.jsp");

			rd.forward(request, response);
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
			finReportService.editFinalDate(finalsettlementDate, pfid, userId);

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
			finReportService.editInterestCalc(interestcalcDate, pfid, userId,
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
			finReportService.editReInterestCalc(reinterestCalcdate, pfid,
					userId, recoverieTable);

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

		} else if (request.getParameter("method").equals("PFCardProc")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "", chkPendingFlag = "", stationName = "";

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("frm_airportcode") != null) {
				stationName = request.getParameter("frm_airportcode");
			} else {
				stationName = "";
			}

			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}
			String empName = "";
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
			if (request.getParameter("frm_chkPendingFlag") != null) {
				chkPendingFlag = request.getParameter("frm_chkPendingFlag");
			}
			log.info("===pfCardProcessing=====" + region + "year" + year
					+ "empflag=========" + empflag + "pfidString" + pfidString
					+ "pensionno" + pensionno + "chkPendingFlag"
					+ chkPendingFlag);

			finReportService.processingPFCards();
			String txtMessage = "records are inserted successfully";
			RequestDispatcher rd = request
					.getRequestDispatcher("./reportservlet?method=loadUpdatePensionContribut&message="
							+ txtMessage);
			
		}else if(request.getParameter("method").equals("PFCardMailProc")){
			PFcardBulkMail mail=new PFcardBulkMail();
			System.out.println("PFCardMailProc===");
				RequestDispatcher rd = request
				.getRequestDispatcher("./PFcardmail");
				//mail.PFcardBulkMail("/reportservlet?method=SumofSuppPCReport&frmName=SumofSuppPCReport&employeeNo=4245&reportType=html","D:/kk.pdf/",request,"2255.pdf",null,"","","","","","","");
			    rd.forward(request, response);
		}else if (request.getParameter("method").equals(
		
				"statmentpcwagesreport")) {
			String reportType = "", formatType="",sortingOrder = "", pensionno = "", range = "", pfidString = "", airportCode = "", month = "", region = "", year = "",adjFlag ="",frmName="";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			
			if (request.getParameter("frm_formatType") != null) {
				formatType = request.getParameter("frm_formatType");
			}
			
			
			
			if (request.getParameter("frm_monthID") != null) {
				month = request.getParameter("frm_monthID");
				if (!month.equals("NO-SELECT")) {
					try {
						month = commonUtil.converDBToAppFormat(request
								.getParameter("frm_monthID"), "MM", "MMM");
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frmAirportCode") != null) {
				airportCode = request.getParameter("frmAirportCode");
			}

			if (request.getParameter("frm_region") != null
					&& request.getParameter("frm_region") != "") {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_pfids") != null
					&& request.getParameter("frm_pfids") != "") {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}

			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("adjFlag") != null) {
				adjFlag = request.getParameter("adjFlag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			log.info("===statmentpcwagesreport=====" + region + "year" + year
					+ "Month" + month + "pfidString" + pfidString + "pensionno"
					+ pensionno);

			list = finReportService.getStatementOfWagePensionReport(pfidString,
					region, sortingOrder, pensionno, airportCode,adjFlag ,frmName);

			request.setAttribute("cardList", list);

			request.setAttribute("reportType", reportType);
			request.setAttribute("formatType", formatType);
			String regionString = "", stationString = "";
			if (region.equals("NO-SELECT")) {
				regionString = "";
			} else {
				regionString = region;
			}
			if (airportCode.equals("NO-SELECT")) {
				stationString = "";
			} else {
				if (region.equals("CHQIAD")) {
					stationString = airportCode;
				} else {
					stationString = "";
				}

			}
		
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", regionString);
			request.setAttribute("airportCode", stationString);
			RequestDispatcher rd =null;
			
			if(formatType.equals("Normal")){

				 rd = request
				.getRequestDispatcher("./PensionView/RPFCStatementWagesReport.jsp");
				 
			 
			 }else if(formatType.equals("Arrear")){
					request.setAttribute("formatType", formatType);	 
						 rd = request.
					getRequestDispatcher("./PensionView/RPFCStatementWagesReportNew.jsp");
			 }
			
		 else if(formatType.equals("WithoutArrear")){
				request.setAttribute("formatType", formatType);	 
					 rd = request.
				getRequestDispatcher("./PensionView/RPFCStatementWagesWithoutArrearReport.jsp");
		 }
			
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("TransferInOutReport")) {
			String region = "", year = "", month = "";
			String reportType = "", sortingOrder = "", pfidString = "", lastmonthFlag = "", lastmonthYear = "", frmName = "";
			ArrayList empList = new ArrayList();
			ArrayList cntList = new ArrayList();
			ArrayList TotalMonthslist1 = new ArrayList();
			String airportCode = "", bulkPrintFlag = "";
			String frmSelectedDts = "", toYear = "";
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("frmAirportCode") != null) {
				airportCode = request.getParameter("frmAirportCode");
			}

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}

			if (request.getParameter("frm_region") != null
					&& request.getParameter("frm_region") != "") {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}
			String empName = "";
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

			if (request.getParameter("transMonthYear") != null) {
				lastmonthYear = request.getParameter("transMonthYear");
			}

			if (request.getParameter("frm_ltstmonthflag") != null) {
				lastmonthFlag = request.getParameter("frm_ltstmonthflag");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}

			try {
				if (!request.getParameter("frm_month").trim().equals(
						"NO-SELECT")) {
					frmSelectedDts = commonUtil.getFromToDates(year, toYear,
							month, month);
					TotalMonthslist1 = finReportService.TransferInOutPrinting(
							pfidString, region, frmSelectedDts, empflag,
							empName, sortingOrder, pensionno, lastmonthFlag,
							frmSelectedDts, airportCode);
					if(TotalMonthslist1.size()>0){
					empList = (ArrayList)TotalMonthslist1.get(0);
					cntList = (ArrayList)TotalMonthslist1.get(1);
					}
				} else {
					for (int i = 1; i <= 12; i++) {
						month = "0" + i;
						frmSelectedDts = commonUtil.getFromToDates(year,
								toYear, month, month);
						TotalMonthslist1 = finReportService
								.TransferInOutPrinting(pfidString, region,
										frmSelectedDts, empflag, empName,
										sortingOrder, pensionno, lastmonthFlag,
										frmSelectedDts, airportCode);
						log.info(i + " month list " + TotalMonthslist1.size());
						//emplist.addAll(TotalMonthslist1);
						if(TotalMonthslist1.size()>0){
						empList.addAll((ArrayList)TotalMonthslist1.get(0));
						cntList.addAll((ArrayList)TotalMonthslist1.get(1));
						}
					}

				}
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (toYear.equals("")) {
				toYear = String.valueOf(Integer.parseInt(year) + 1);
			}
			if (!year.equals("NO-SELECT")) {
				if (Integer.parseInt(month) >= 1
						&& Integer.parseInt(month) <= 3) {
					year = "01-" + month + "-" + toYear;

				} else {
					year = "01-" + month + "-" + year;
				}

			}

			request.setAttribute("empinfo", empList);
			request.setAttribute("cntinfo", cntList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("date", year);
			String regionString = "", stationString = "";
			if (region.equals("NO-SELECT")) {
				regionString = "";
			} else {
				regionString = region;
			}
			if (airportCode.equals("NO-SELECT")) {
				stationString = "";
			} else {
				if (region.equals("CHQIAD")) {
					stationString = airportCode;
				} else {
					stationString = "";
				}

			}
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", regionString);
			request.setAttribute("airportCode", stationString);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/TransferInOutRegionReport.jsp");
			rd.forward(request, response);
		}
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getVefifiedPFIDList")) {
			String fromId = "";
			fromId = request.getParameter("fromId");
			String toId = "";
			toId = request.getParameter("toId");
			CommonUtil commonUtil = new CommonUtil();
			ArrayList pfidlist = null;
			pfidlist = commonUtil.getVefifiedPFIDList(fromId, toId);
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");

			for (int i = 0; pfidlist != null && i < pfidlist.size(); i++) {
				String pfid = "";
				EmpMasterBean bean = (EmpMasterBean) pfidlist.get(i);
				sb.append("<ServiceType>");
				sb.append("<pfid>");
				if (bean.getPfid() != null)
					sb.append(bean.getPfid());
				sb.append("</pfid>");
				sb.append("<empname>");
				sb.append(bean.getEmpName());
				sb.append("</empname>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		}
		if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("movedatafrom95to2008")) {
			String PFID = (String) request.getParameter("PFID");
			String computerName = session.getAttribute("computername")
					.toString();
			String userName = session.getAttribute("userid").toString();
			financeService.movedatafrom95to2008(PFID, computerName, userName);
			String toYear = "31-Mar-2008";
			String finYear = "1995", region = "", airportCode = "", selectedMonth = "", empserialNO = PFID, cpfAccno = "", transferFlag = "", mappingFlag = "", pfidString = "", chkBulkPrint = "", frm_month = "03", frm_toyear = "2008";
			String page = "PensionContributionScreen";
			String finalrecoverytableFlag = "true";
			RequestDispatcher rd = request
					.getRequestDispatcher("./reportservlet?method=getReportPenContr&empserialNO="
							+ PFID
							+ "&frm_year="
							+ finYear
							+ "&frm_month="
							+ frm_month
							+ "&frm_toyear="
							+ frm_toyear
							+ "&page="
							+ page
							+ "&finalrecoverytableFlag="
							+ finalrecoverytableFlag);
			rd.forward(request, response);

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

		}else if (request.getParameter("method").equals("loadAdjObCrtn")) { 
			String loginUserId="", accessCode="",privilages="",accountType="",message="",profileType="",result="",path="";
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
			log.info("yyyyyyyyyyyyyyyyy"+loginUserId+"xxxxxxxxxxxxxxxxxxxxxxx"+accessCode);
			if(profileType.equals("U") || profileType.equals("R") || profileType.equals("C")){ 				 
				if(privilages.equals("NP")){					 
					message="U Don't Have Privilages to Access";
				}else{
					 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
					 log.info("==result==="+result);
					 if(result.equals("NotHaving")){
						 message="U Don't Have Privilages to Access";
					 }
				}
			}
			if(accessCode.equals("PE040201")){
				path="./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtn.jsp";
			}else if(accessCode.equals("PE040202")){
				path="./PensionView/AdjCrtn/ApproverSearchForAdjCrtn.jsp";
			}
			
			log.info("==profileType==="+profileType+"==privilages=="+privilages+"==message=="+message);
			request.setAttribute("privilages",privilages);
			request.setAttribute("accountType",accountType);
			request.setAttribute("accessCode",accessCode);
			request.setAttribute("message",message);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals(
				"searchAdjRecords")) {
			log.info("searchAdjRecords () ");
			String employeeName = "", frmName = "", path = "";
			String cpfAccnos = "",searchFlag="",username = "", ipaddress = "" , chkpfid="",enterdPfid="",employeeNo= "",reportYear="",status="";
			String accessCode="",privilages="",accountType="",message="",profileType="",errorFlag="false",userStation="",loginUserId="",result="",verifiedby="";
			String  userRegion="",adjObYears="";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList empPCAdjDiffTot = new ArrayList();
			
			if (request.getParameter("empsrlNo") != null) {			 
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter(
				"empsrlNo").toString().trim()));
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
				 	log.info("privilages=="+privilages+"employeeNo=="+employeeNo);
					log.info("userRegion=="+userRegion+"==Employee=="+empInfo.getRegion());
					log.info("userStation=="+userStation+"==Employee=="+empInfo.getStation());
					 		
					if(privilages.equals("P")){
						 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
						 log.info("Screen Access=="+result);
						 if(result.equals("NotHaving")){
							 message="U Don't Have Privilages to Access";
						 }else{
					
							 searchList = adjCrtnService.searchAdjctrn(userRegion,userStation,profileType,accessCode,accountType,employeeNo,reportYear,status);  
							if(!employeeNo.equals("")){
							 adjObYears = adjCrtnDAO.getNotFinalizedAdjObYear(employeeNo,frmName);
							}
						 }	
				}else{
					errorFlag="true";
					message ="U Don't Have Privilages to Access";
				}
					log.info("--------errorFlag----" + errorFlag);
					if(errorFlag.equals("false")){ 
						
						
					} 
				 
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("adjcorrections")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtn.jsp";
			} else {
				request.setAttribute("searchInfo", empBean);
				request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp";
			}
			
			if(accessCode.equals("PE040201")){
				path="./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtn.jsp";
			}else if(accessCode.equals("PE040202")){
				path="./PensionView/AdjCrtn/ApproverSearchForAdjCrtn.jsp";
				
				request.setAttribute("searchOnFinYear",reportYear);
				request.setAttribute("searchOnStatus",status);
			}else if(accessCode.equals("PE040204")){
				path="./PensionView/AdjCrtn/ApprovedSearchForAdjCrtn.jsp";
			}else if(accessCode.equals("PE04020601")){
				path="./PensionView/AdjCrtn/CHQApproverSearchForAdjCrtn.jsp";
			}else if(accessCode.equals("PE04020602")){
				path="./PensionView/AdjCrtn/CHQApprovedSearchForAdjCrtn.jsp";
			}
			 if(searchList.size()>0){
			request.setAttribute("searchList",searchList);
			 }
			request.setAttribute("adjObYears",adjObYears);
			request.setAttribute("reportYear",reportYear);
			request.setAttribute("message", message);
			request.setAttribute("accessCode",accessCode);
			request.setAttribute("verifiedby",verifiedby);	
			request.setAttribute("empsrlNo",enterdPfid);	
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals(
				"loadPCReportForAdjDetails")) {
			log.info("loadPCReportForAdjDetails () ");
			String employeeName = "", frmName = "", path = "",empRegion="",empStation="",adjObYears="";
			String cpfAccnos = "",searchFlag="",username = "", ipaddress = "" , chkpfid="",enterdPfid="",employeeNo= "",chkPfidTracking="",blockedYears="";
			String accessCode="",privilages="",accountType="",message="",profileType="",errorFlag="false",userRegion="", userStation="",loginUserId="",result="",verifiedby="";
			String chqFlag="",empSubTot="0",aaiContriTot="0",pensionTot="0";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList empPCAdjDiffTot = new ArrayList();			 			 
			// new code for status disply
			if (request.getParameter("cpfnumbers") != null) {
				cpfAccnos = request.getParameter("cpfnumbers");
				request.setAttribute("cpfAccnos", cpfAccnos);

			}
			if (request.getParameter("empName") != null)

				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			if (request.getParameter("dob") != null) {
				empBean.setDateofBirth(request.getParameter("dob").toString()
						.trim());
			} else
				empBean.setDateofBirth("");
			if (request.getParameter("empsrlNo") != null) {			 
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter(
				"empsrlNo").toString().trim()));
				employeeNo = empBean.getEmpSerialNo();
				enterdPfid = request.getParameter("empsrlNo");
			} else {
				empBean.setEmpSerialNo("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateofJoining((String) request.getParameter("doj")
						.toString());
			} else
				empBean.setDateofJoining("");

			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			} else {
				frmName = "";
			}
			
			log.info("Name=="+empBean.getEmpName()+"DOB="+empBean.getDateofBirth()+"DOJ=="+empBean.getDateofJoining()+"No=="+empBean.getEmpSerialNo());
			
			log.info("cpfAccnos--------- " + cpfAccnos + "===--------"
					+ empBean.getEmpSerialNo());

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
			/*if (session.getAttribute("accountType") != null) {
				accountType = session.getAttribute("accountType").toString();
			}*/
			if (session.getAttribute("privilages") != null) {
				privilages = session.getAttribute("privilages").toString();
			}
			if (session.getAttribute("profileType") != null) {
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("station") != null) {
				userStation = ((String)session.getAttribute("station")).toLowerCase().trim();
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
					empList = commonDAO.getEmployeePersonalInfo(empBean
							.getEmpSerialNo());
					if (empList.size() > 0) {
						empInfo = (EmpMasterBean) empList.get(0);
						BeanUtils.copyProperties(empBean, empInfo);
						employeeName = empInfo.getEmpName();
						log.info(employeeName);
					}else{
						message ="No Records Found";
						errorFlag="true"; 
					}
					empRegion=empInfo.getRegion().toLowerCase().trim();
					empStation=empInfo.getStation().toLowerCase().trim();
					log.info("privilages=="+privilages);
					log.info("userRegion=="+userRegion+"==Employee=="+empRegion);
					log.info("userStation=="+userStation+"==Employee=="+empStation);
					
				if (empList.size() > 0) { 		
					if(privilages.equals("P")){
						 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
						 log.info("Screen Access=="+result);
						 if(result.equals("NotHaving")){
							 message="U Don't Have Privilages to Access";
						 }else{ 
							 verifiedby = commonDAO.chkStageWiseprocessinAdjCalc(empBean.getEmpSerialNo()); 
							  if(accessCode.equals("PE040202")){
					         	  if(verifiedby.equals("")){ 
					         		 errorFlag="true";
					              }
					        }  
							 
							 log.info("----verifiedby-- in Action-----"+verifiedby+"==accessCode==="+accessCode);
					if(profileType.equals("U")){						 
						if((!empRegion.equals(userRegion))  || (!empStation.equals(userStation))) {
							message ="This Pfid belongs to "+empInfo.getRegion()+"/"+empInfo.getStation()+". You Can't Process";
						 	errorFlag="true"; 
						}  
					}else if(profileType.equals("R")){					 
						if(!(empRegion.equals(userRegion))) {
							message ="This Pfid belongs to "+empInfo.getRegion()+". You Can't Process";
							errorFlag="true";
						}  else if(!(empStation.equals(userStation))) {
							accountType= commonDAO.getAccountType(empRegion,empStation);
							if(!accountType.equals("")){
								//For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 07-Jun-2012
							if(empRegion.toUpperCase().equals("CHQIAD") && accountType.equals("SAU")){
								message ="This Pfid belongs to "+empInfo.getRegion()+"/"+empInfo.getStation()+". You Can't Process";
							 	errorFlag="true";
							 
							}
							//Commented on 14-May-2012 as per Instruction given By Sehgal 
							/*if(accountType.equals("SAU")){
								message ="This Pfid belongs to "+accountType+" Account Type. You Can't Process";
							 	errorFlag="true"; 
							}*/
						}else{
							message =" This Pfid does not belongs to Any Account Type. You Can't Process";
						 	errorFlag="true";
						}
						} 
						 
					  }
					}	
				}else{
					errorFlag="true";
					message ="U Don't Have Privilages to Access";
				}
				}
					if(errorFlag.equals("false")){
					if (frmName.equals("adjcorrections")) {
						log.info("--------frmName----" + frmName
								+ "----employeeName---" + employeeName + "=");
						if (!employeeName.equals("")) { 
							String adjOBYear = "1995-2008";
							
							chkpfid = adjCrtnService.chkPfidinAdjCrtn(empBean.getEmpSerialNo(), frmName);
							log.info("--------chkpfid-------"+chkpfid);
							
							if (chkpfid.equals("NotExists")) {	
				/*			pensionService.insertEmployeeTransData(empBean
									.getEmpSerialNo(), frmName, username,
									ipaddress,searchFlag,"","","");*/
								String chkUploadPfid = adjCrtnService.chkPfidinAdjCrtnTrackingForUpload(empBean.getEmpSerialNo(), frmName);
								log.info("--------chkUploadPfid-------"+chkUploadPfid);
								/*
								 * Below changes the are done by prasad for the purpose of uploading/Mapping
								 */
								if(chkUploadPfid.equals("Exists")){
									ArrayList prePcTotals=new ArrayList();
									ArrayList currPcTotals=new ArrayList();
									adjCrtnService.insertEmployeeTransData(empBean
										.getEmpSerialNo(), frmName, username,
										ipaddress,searchFlag,"U","","","N");
								prePcTotals=adjCrtnService.updatePCAdjCorrections("01-Apr-1995", "31-Mar-2008", "",
										"", empBean
										.getEmpSerialNo(), "","");
							
								int result1 =adjCrtnService.savePrePctoalsTemp(empBean.getEmpSerialNo(),"1995-2008",prePcTotals);
								adjCrtnService.getDeleteAllRecords(empBean.getEmpSerialNo() ,"","",username,
										ipaddress,"U",chqFlag,empSubTot,aaiContriTot,pensionTot);
								adjCrtnService.insertEmployeeTransData(empBean
										.getEmpSerialNo(), frmName, username,
										ipaddress,searchFlag,"U","","","U");
								currPcTotals=adjCrtnService.updatePCAdjCorrections("01-Apr-1995", "31-Mar-2008", "",
										"", empBean.getEmpSerialNo(), "","");
							
							//	result1 =adjCrtnService.insertRecordForAdjCtrnTracking(empBean.getEmpSerialNo(), "", "1995-2008", "Upload", username, ipaddress);
								
								result1 =adjCrtnService.saveCurrPctoals(empBean.getEmpSerialNo(),"1995-2008",currPcTotals);
								
								
								
								}else{
									adjCrtnService.insertEmployeeTransData(empBean
											.getEmpSerialNo(), frmName, username,
											ipaddress,searchFlag,"","","","");
								}
							} 
							empPCAdjDiffTot = adjCrtnService.getPCAdjDiff(empBean
									.getEmpSerialNo(), adjOBYear);
							request.setAttribute("empPCAdjDiffTot",
									empPCAdjDiffTot);
							request.setAttribute("empsrlNo", employeeNo);

							// request.setAttribute("searchBean", searchBean);
							request.setAttribute("searchInfo", empBean);
						} else {
							request.setAttribute("empsrlNo", employeeNo);
							request.removeAttribute("searchInfo");
						}
					}
					//for Form2 
					PensionContBean PersonalInfo = new PensionContBean();				 
					copyToBean(empInfo,PersonalInfo);
					
					session.setAttribute("PersonalInfo",PersonalInfo);
					
					chkPfidTracking = adjCrtnService.chkPfidinAdjCrtnTracking(employeeNo,frmName);
					
					adjObYears = adjCrtnDAO.getNotFinalizedAdjObYear(employeeNo,frmName);
					
					blockedYears= adjCrtnDAO.getblockedAdjObYears(employeeNo,frmName);
					}
				}
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("adjcorrections")) {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtn.jsp";
			} else {
				request.setAttribute("searchInfo", empBean);
				request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp";
			}
			log.info("--------message-------"+message);
			String blockOrFrozenflag="";
			
			blockOrFrozenflag=adjCrtnService.chkPfidBRF(empBean.getEmpSerialNo());
			log.info("--------message-------"+blockOrFrozenflag);
			request.setAttribute("blockOrFrozenflag",blockOrFrozenflag);
			request.setAttribute("message", message);
			request.setAttribute("accessCode",accessCode);
			request.setAttribute("verifiedby",verifiedby);	
			request.setAttribute("empsrlNo",enterdPfid);	
			request.setAttribute("chkPfidTracking",chkPfidTracking);	
			request.setAttribute("adjObYears",adjObYears);	
			request.setAttribute("blockedYears",blockedYears);	
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		}else if (request.getParameter("method").equals("updateAdjCrtnStatus")) {  
			RequestDispatcher rd = null;
			String stageStatus="",approvemonth="", accessCode="",processedStage="",pensionno="",url="" ,params="",username="",loginUserId="",userRegion="",loginUsrDesgn="",loginUsrStation="",reportYear="",form2Status="",jvno="";
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
			
			if(stageStatus.equals("Y")){
				adjCrtnService.updateStageWiseStatusInAdjCtrn( pensionno, processedStage,reportYear,form2Status,jvno,username,loginUserId,userRegion,loginUsrStation,loginUsrDesgn); 
			} 
			
			request.setAttribute("accessCode", accessCode); 
			
			params = "&accessCode="+accessCode+"&empsrlNo="+pensionno+"&reportYear="+reportYear+"&searchFlag=S&frmName=adjcorrections"; 
			if(accessCode.equals("PE040201")){
				url = "./reportservlet?method=loadPCReportForAdjDetails" + params;
			}else if(accessCode.equals("PE040202") ||  accessCode.equals("PE04020601")){
				url = "./reportservlet?method=searchAdjRecords" + params;	
			} 
			 
			log.info("========url========"+url);
			rd = request.getRequestDispatcher(url);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals(
				"getReportPenContrForAdjCrtn")) {
			String region = "", frmYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "", empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", transferFlag = "", pfidString = "NO-SELECT", chkBulkPrint = "", grandTotDiffShowFlag = "";
			String recoverieTable = "", reportYear = "", path = "", adjObYear = "",loginUserId="",loginUsrDesgn="";
			String empflag = "true", lastmonthYear = "", lastmonthFlag = "", empName = "", sortingOrder = "", bulkPrintFlag = "", updateFlag = "false";
			String interestCalc = "", ReportStatus = "", batchid = "",username ="",ipaddress="",stageStatus="N",processedStage="",accessCode="";;
			String[] years = null;
			String notFianalizetransID="",  notFianalizetransIDForPrev="",finlaizedFlag="true",transIdToGetPrevData="",form2Status="",enterdPfid="";
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
				empserialNO = commonUtil.getSearchPFID1(request.getParameter(
						"empserialNO").toString().trim());
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

			log.info("----frmYear-" + frmYear + "------toYear-- " + toYear
					+ "--grandTotDiffShowFlag---" + grandTotDiffShowFlag
					+ "pfcard" + pfcard);

			if (reportYear.equals("1995-2008")) {
				pensionContributionList = adjCrtnService
						.getPensionContributionReportForAdjCRTN(frmYear,
								toYear, region, airportCode, empserialNO,
								cpfAccno, batchid, ReportStatus);
			} else {

				list = adjCrtnService.pfCardReportForAdjCrtn(pfidString,
						region, years[0], empflag, empName, sortingOrder,
						empserialNO, lastmonthFlag, lastmonthYear, airportCode,
						bulkPrintFlag);
				request.setAttribute("cardList", list);
				request.setAttribute("dspYear", reportYear);
				request.setAttribute("reportYear", reportYear);
			}


			 
				log.info("adjEmolList in session "
						+ session.getAttribute("adjEmolumentsList")+"================================sssssssssssssssssss==================================grandTotDiffShowFlag"+grandTotDiffShowFlag); 
			if (session.getAttribute("adjEmolumentsList") != null) {
				adjEmolList = (ArrayList) session
						.getAttribute("adjEmolumentsList");
				log.info("adjEmolList "
						+ adjEmolList.size()); 
				} 
			AdjCrntSaveDtlBean afterSaveDtlBean=new AdjCrntSaveDtlBean();
				if (!grandTotDiffShowFlag.equals("")) {				
			
					log.info("--------entrees--------");
					double EmolumentsTot = 0.00, cpfTotal = 0.00, cpfIntrst = 0.00, PenContriTotal = 0.00, PensionIntrst = 0.00, PFTotal = 0.00, PFIntrst = 0.00;
					double EmpSub = 0.00, EmpSubInterest = 0.00, AAIContri = 0.00, AAIContriInterest = 0.00, adjAAiContriInterest=0.00, adjPensionContriInterest=0.00, adjEmpSubInterest =0.00;
					
					/*if (request.getParameter("EmolumentsTot") != null) {
						EmolumentsTot = Double.parseDouble(request
								.getParameter("EmolumentsTot"));
					}
					if (request.getParameter("cpfTotal") != null) {
						cpfTotal = Double.parseDouble(request
								.getParameter("cpfTotal"));
					}  
					if (request.getParameter("cpfIntrst") != null) {
						cpfIntrst = Double.parseDouble(request
								.getParameter("cpfIntrst"));
					} 
					if (request.getParameter("PenContriTotal") != null) {
						PenContriTotal = Double.parseDouble(request
								.getParameter("PenContriTotal"));
					}
					if (request.getParameter("PensionIntrst") != null) {
						PensionIntrst = Double.parseDouble(request
								.getParameter("PensionIntrst"));
					}
					if (request.getParameter("PFTotal") != null) {
						PFTotal = Double.parseDouble(request
								.getParameter("PFTotal"));
					}
					if (request.getParameter("PFIntrst") != null) {
						PFIntrst = Double.parseDouble(request
								.getParameter("PFIntrst"));
					}
					if (request.getParameter("EmpSub") != null) {
						EmpSub = Double.parseDouble(request.getParameter("EmpSub"));
					}
					if (request.getParameter("EmpSubInterest") != null) {
						EmpSubInterest = Double.parseDouble(request
								.getParameter("EmpSubInterest"));
					}
					if (request.getParameter("adjEmpSubInterest") != null) {
						adjEmpSubInterest = Double.parseDouble(request
								.getParameter("adjEmpSubInterest"));
					}
					if (request.getParameter("AAIContri") != null) {
						AAIContri = Double.parseDouble(request
								.getParameter("AAIContri"));
					}
					if (request.getParameter("AAIContriInterest") != null) {
						AAIContriInterest = Double.parseDouble(request
								.getParameter("AAIContriInterest"));
					}
					if (request.getParameter("adjAAiContriInterest") != null) {
						adjAAiContriInterest = Double.parseDouble(request
								.getParameter("adjAAiContriInterest")); 
					}*/
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
				String reasonForInsert="Edited";
				try {
					afterSaveDtlBean=adjCrtnService.saveAdjCrntDetails(empserialNO,cpfAccno,  reportYear,
							 EmolumentsTot,  cpfTotal,  cpfIntrst,
							 PenContriTotal,  PensionIntrst,  PFTotal,
							 PFIntrst,  EmpSub,  EmpSubInterest, adjEmpSubInterest,
							 AAIContri,  AAIContriInterest, adjAAiContriInterest, adjPensionContriInterest, grandTotDiffShowFlag, reasonForInsert, username, ipaddress,adjEmolList,batchid);
					
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				session.removeAttribute("adjEmolumentsList");
			}else{
				afterSaveDtlBean=adjCrtnService.getFinzdPreviouseGrndTotals(empserialNO,reportYear,batchid );
			}
			
		   prevGrandTotalsList=afterSaveDtlBean.getPreviouseGrndList();
		    finlaizedFlag=afterSaveDtlBean.getFinalizedFlag();
			log.info("pensionContributionList "
					+ pensionContributionList.size());
			request.setAttribute("penContrList", pensionContributionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("stationStr", airportCode);
			request.setAttribute("regionStr", region);
			request.setAttribute("accessCode", accessCode);
			request.setAttribute("grandTotDiffShowFlag", grandTotDiffShowFlag);
			request.setAttribute("prevGrandTotalsList",prevGrandTotalsList);
			request.setAttribute("finlaizedFlag", finlaizedFlag);
			request.setAttribute("form2Status", form2Status);
			

			if (page.equals("report") || pfcard.equals("true")){
				finalizedTotals = adjCrtnService.getAdjCrtnFinalizedTotals(empserialNO,reportYear);
				request.setAttribute("finalizedTotals", finalizedTotals);
				
			}

			if (page.equals("report")) {				
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTN.jsp";				

			}else if(Integer.parseInt(years[0])>=2015){
				path = "./PensionView/AdjCrtn/PensionContributionReportForAdjCRTNAfter2015.jsp";	
				log.info("path after 2015------" + path);
			}
			else {

				if (reportYear.equals("1995-2008")) {
					path = "./PensionView/AdjCrtn/PensionContributionScreenForAdjCRTN.jsp?recoverieTable="
							+ recoverieTable;

				} else {

					if (pfcard.equals("true")) {

						path = "./PensionView/AdjCrtn/PFCardForAdjCtrn.jsp";

					} else {

						path = "./PensionView/AdjCrtn/PensionContributionScreenForAdjCRTN2008-2009.jsp";
					}
				}
			}
			log.info("---page---" + page + "---path------" + path
					+ "----------" + reportYear + "--reportType-------"
					+ reportType);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
			
			
		} else if (request.getParameter("method").equals(
				"editTransactionDataForAdjCrtn")) {

			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00",addcon="0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00", contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "", adjOBYear = "", url = "", editTransFlag = "", dateOfBirth="";
			String pensionoption = "", empnetob = "", aainetob = "";
			String empnetobFlag = "";
			ArrayList adjEmolumentsList = new ArrayList();
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
			log.info("------in Action ---contri-------" + contri
					+ "------region--" + region);
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
			String ComputerName = session.getAttribute("computername")
					.toString();
			String username = session.getAttribute("userid").toString();
			pfid = commonUtil.trailingZeros(pfid.toCharArray());
			if (request.getParameter("pensionoption") != null) {
				pensionoption = request.getParameter("pensionoption")
						.toString();
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
				adjEmolumentsList = (ArrayList) session
						.getAttribute("adjEmolumentsList");
				log.info("----adjEmolumentsList---------"
						+ adjEmolumentsList.size());
			}
			adjEmolumentsList = adjCrtnService.editTransactionDataForAdjCrtn(
					cpfAccno, monthYear, emoluments,addcon,epf, vpf, principle,
					interest, advance, loan, aailoan, contri, noofmonths,pfid, region,
					airportcode, username, ComputerName, from7narration,
					duputationflag, pensionoption, empnetob, aainetob,
					empnetobFlag, adjOBYear, editTransFlag,dateOfBirth);
			 
			log.info("----adjEmolumentsList after res---------"
					+ adjEmolumentsList.size());
			int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec"
					+ insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip + "&page=" + page + "&mappingFlag="
					+ mappingFlag + "&reportYear=" + adjOBYear;

			url = "./reportservlet?method=getReportPenContrForAdjCrtn" + params;

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

		} else if (request.getParameter("method").equals("loadRevisedForm78PsAdjObCrtn")) {
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method").equals(
				"getReportPenContrForAdjCrtnFrm1995toTillDate")) {
			String region = "", frmYear = "", reportType = "", airportCode = "", formType = "", selectedMonth = "", empserialNO = "";
			String cpfAccno = "", page = "", toYear = "", flag = "", transferFlag = "", pfidString = "NO-SELECT", chkBulkPrint = "", grandTotDiffShowFlag = "";
			String recoverieTable = "false", reportYear = "", path = "", adjObYear = "", frmName = "";
			String empflag = "true", lastmonthYear = "", lastmonthFlag = "", empName = "", sortingOrder = "", bulkPrintFlag = "", employeeName = "";
			String interestCalc = "",searchFlag="N";
			String[] years = null;
			ArrayList pensionContributionList = new ArrayList();
			ArrayList prevGrandTotalsList = new ArrayList();
			ArrayList empList = new ArrayList();
			EmpMasterBean empInfo = new EmpMasterBean();
			String username = "", ipaddress = "";
			if (request.getParameter("transferStatus") != null) {
				transferFlag = request.getParameter("transferStatus");
			}
			String mappingFlag = "true";
			if (request.getParameter("mappingFlag") != null) {
				mappingFlag = request.getParameter("mappingFlag");

			}

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
				// region="NO-SELECT";
			} else {
				region = "NO-SELECT";
			}

			if (request.getParameter("empserialNO") != null) {
				empserialNO = commonUtil.getSearchPFID1(request.getParameter(
						"empserialNO").toString().trim());
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

			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page");
			}
			if (request.getParameter("flag") != null) {
				flag = request.getParameter("flag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}

			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}
			empList = commonDAO.getEmployeePersonalInfo(empserialNO);
			if (empList.size() > 0) {
				empInfo = (EmpMasterBean) empList.get(0);
			}

			if (!empInfo.getEmpName().equals("")) {

				if (!flag.equals("refresh")) {

					try {
						adjCrtnService.insertEmployeeTransData(empserialNO,
								frmName, username, ipaddress,searchFlag,"","","","");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.printStackTrace(e);
					}

				}

				frmYear = "01-Apr-1995";
				toYear = commonUtil.getCurrentDate("dd-MMM-yyyy");

				reportYear = "1995-" + commonUtil.getCurrentDate("yyyy");

				log.info("----frmYear-" + frmYear + "------toYear-- " + toYear
						+ "-- reportYear---" + reportYear);

				pensionContributionList = adjCrtnService
						.getPCReportFor78PsAdjCRTN(frmYear, toYear, region,
								airportCode, selectedMonth, empserialNO,
								cpfAccno, transferFlag, mappingFlag,
								pfidString, chkBulkPrint, recoverieTable);

				request.setAttribute("dspYear", reportYear);

				double pctotal = 0.0;
				if (recoverieTable.equals("true")) {
					pctotal = financeService
							.getPensionContributionTotal(empserialNO);
				}

				log.info("pensionContributionList "
						+ pensionContributionList.size());
				request.setAttribute("penContrList", pensionContributionList);
				request.setAttribute("reportType", reportType);
				request.setAttribute("blkprintflag", chkBulkPrint);
				request.setAttribute("stationStr", airportCode);
				request.setAttribute("regionStr", region);
				request.setAttribute("pctotal", new Double(pctotal));
				request.setAttribute("recoverieTable", recoverieTable);

				if (page.equals("report")) {

					path = "./PensionView/AdjCrtn/PCReportForAdjCRTNFrm1995toTillDate.jsp";
				} else {
					path = "./PensionView/AdjCrtn/PensionContributionScreenForAdjCRTNFrm1995toTillDate.jsp";

				}

			} else {
				path = "./PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp";
				request.setAttribute("dataFlag", "NoData");
				request.setAttribute("empsrlNo", empserialNO);
			}

			
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals(
				"editTransactionDataFor78PsAdjCrtn")) {

			String cpfAccno = "", monthYear = "", region = "", pfid = "", airportcode = "", from7narration = "";
			log.info("Edit items  " + request.getParameterValues("cpfno"));
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00", contri = "0.00", loan = "0.00", aailoan = "0.00", advance = "0.00", pcheldamt = "0.00";
			String epf = "0.00",dueemoluments="0.00",duepension="0.00";
			String noofmonths = "", arrearflag = "", duputationflag = "", adjOBYear = "", url = "", editTransFlag = "";
			String pensionoption = "", empnetob = "", aainetob = "",statemntWagesRemarks="",frmName="";
			String empnetobFlag = "";
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
			if (request.getParameter("from7narration") != null) {
				from7narration = request.getParameter("from7narration");
			}
			if (request.getParameter("swremarks") != null) {
				statemntWagesRemarks = request.getParameter("swremarks");
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

			/*
			 * if (request.getParameter("pcheldamt") != null) { pcheldamt =
			 * request.getParameter("pcheldamt"); } if
			 * (request.getParameter("noofmonths") != null) { noofmonths =
			 * request.getParameter("noofmonths"); } if
			 * (request.getParameter("arrearfalg") != "") { arrearflag =
			 * request.getParameter("arrearflag"); }
			 */
			if (request.getParameter("adjOBYear") !=null  && request.getParameter("adjOBYear")!="") {
				adjOBYear = request.getParameter("adjOBYear");
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

			if (request.getParameter("editTransFlag") != null) {
				editTransFlag = request.getParameter("editTransFlag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			
			if (request.getParameter("dueemoluments") != null) {
				dueemoluments = request.getParameter("dueemoluments");
			}
			if (request.getParameter("duepension") != null) {
				duepension = request.getParameter("duepension");
			}
			
			adjCrtnService.editTransactionDataFor78PsAdjCrtn(cpfAccno,
					monthYear, emoluments, epf, vpf, principle, interest,
					advance, loan, aailoan, contri, pfid, region, airportcode,
					username, ComputerName, from7narration,statemntWagesRemarks, duputationflag,
					pensionoption, adjOBYear, editTransFlag,dueemoluments,duepension);

			/*int insertedRec = finReportService.preProcessAdjOB(pfid);

			log.info("deleteTransactionData=============Current Date========="
					+ commonUtil.getCurrentDate("dd-MMM-yyyy") + "insertedRec"
					+ insertedRec);
			String reportType = "Html";
			String yearID = "NO-SELECT";
			// region="NO-SELECT";
			String pfidStrip = "1 - 1";
			String page = "PensionContributionScreen";
			String mappingFlag = "true";
			String params = "&frm_region=" + region + "&frm_airportcode="
					+ airportcode + "&frm_year=" + yearID + "&frm_reportType="
					+ reportType + "&empserialNO=" + pfid + "&frm_pfids="
					+ pfidStrip + "&page=" + page + "&mappingFlag="
					+ mappingFlag + "&reportYear=" + adjOBYear;

			url = "./reportservlet?method=getReportPenContrForAdjCrtnFrm1995toTillDate"
					+ params;

			log.info("url is " + url);*/
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

		} else if (request.getParameter("method").equals("getadjemolumentslog")) {

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
				emolumentsList = adjCrtnService.getAdjEmolumentsLog(pfid,
						adjOBYear, frmName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			path = "./PensionView/AdjCrtn/AdjEmolumentsTrackingLog.jsp";
			request.setAttribute("adjEmolTrackingLog", emolumentsList);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals(
				"getPCReportAdjUserRequired")) {
			String region = "", frmYear = "", toYear = "", reportType = "Html", airportCode = "", pensionno = "";
			String cpfAccno = "", formType = "", reportYear = "", path = "", adjObYear = "", batchid = "", batchtime = "", updatedate = "";
			String ReportStatus = "userRequired";
			String[] years = null;
			ArrayList pensionContributionList = new ArrayList();
			ArrayList prevGrandTotalsList = new ArrayList();
			ArrayList list = new ArrayList();
			ArrayList adjEmolList = new ArrayList();

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno").trim();
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfAccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("batchid") != null) {
				batchid = request.getParameter("batchid");
			}

			if (request.getParameter("batchtime") != null) {
				batchtime = request.getParameter("batchtime");
			}
			if (request.getParameter("updatedate") != null) {
				updatedate = request.getParameter("updatedate");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}

			log.info("------reportYear-----" + reportYear);
			years = reportYear.split("-");
			frmYear = "01-Apr-" + Integer.parseInt(years[0]);
			toYear = "31-Mar-" + years[1];

			if (reportYear.equals("1995-2008")) {
				pensionContributionList = adjCrtnService
						.getPensionContributionReportForAdjCRTN(frmYear,
								toYear, region, airportCode, pensionno,
								cpfAccno, batchid, ReportStatus);
			}

			log.info("pensionContributionList "
					+ pensionContributionList.size());
			request.setAttribute("penContrList", pensionContributionList);
			request.setAttribute("reportType", reportType);
			request.setAttribute("stationStr", airportCode);
			request.setAttribute("regionStr", region);
			request.setAttribute("updatedate", updatedate);
			request.setAttribute("batchtime", batchtime);

			try {
				prevGrandTotalsList = adjCrtnService
						.getPrevPCGrandTotalsForAdjCrtn(pensionno, reportYear,
								batchid,"");
				request
						.setAttribute("prevGrandTotalsList",
								prevGrandTotalsList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			path = "./PensionView/AdjCrtn/PCReportForAdjCRTNUserReuired.jsp";
					RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if(request.getParameter("method").equals("adjCrtnDataMapping")){
			String path="",employeeNo="",frmName="",chkpfid="",username="",ipaddress="",searchFlag="P";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList prePcTotals=new ArrayList();
			if (request.getParameter("empserialNo") != null) {			 
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter(
				"empserialNo").toString().trim()));
				employeeNo = request.getParameter("empserialNo");
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
			try{
				
		/*	By Radha On 30-Jul-2012 for Mapping the Data Ex pfid:22878 
		 * 
		 * 	chkpfid = adjCrtnService.chkPfidinAdjCrtn(empBean.getEmpSerialNo(), frmName);
			log.info("--------chkpfid--Mapping-----"+chkpfid);
			
			if (chkpfid.equals("NotExists")) {	
				adjCrtnService.insertEmployeeTransData(empBean
					.getEmpSerialNo(), frmName, username,
					ipaddress,searchFlag,"","","","");
			} */
			prePcTotals=adjCrtnService.updatePCAdjCorrections("01-Apr-1995", "31-Mar-2008", "",
					"", empBean.getEmpSerialNo(), "","");
			int result = 0;
			result =adjCrtnService.savePrePctoalsTemp(empBean.getEmpSerialNo(),"1995-2008",prePcTotals);
			//result =finReportService.insertRecordForAdjCtrnTracking(empBean.getEmpSerialNo(), "", "1995-2008", "Mapped", username, ipaddress);
			session.setAttribute("mpensionno",empBean.getEmpSerialNo());
			}catch(Exception e){
				e.printStackTrace();
			}
			path = "./PensionView/AdjCrtn/AdjCrtnDataMapping.jsp";
		
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
			
		}else if (request.getParameter("method").equals("adjCrtnMappingUpdate")) {

			String pfid = "", cpfaccno = "", region = "",updatePfid="",reportYear="",frmName="",chqFlag="",empSubTot="0",aaiContriTot="0",pensionTot="0";
			ArrayList currPcTotals = new ArrayList();
			log.info("Mapped items  "
					+ request.getParameterValues("adjCrtnMappingUpdate"));
			String[] unmappedRecords = {};
			String[] updateTransactions = {};
			if (request.getParameterValues("adjCrtnMappingUpdate") != null) {
				unmappedRecords = (String[]) request
				.getParameterValues("adjCrtnMappingUpdate");
				
			}
			FinancialReportService finReportService = new FinancialReportService();
			String pfid1 = "";
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			for (int i = 0; i < unmappedRecords.length; i++) {
				updateTransactions = unmappedRecords[i].split(",");
				
						pfid = pfid1;
						cpfaccno = updateTransactions[1].substring(1,
								updateTransactions[1].length() - 1);
						region = updateTransactions[2].substring(1,
								updateTransactions[2].length() - 1);
						String pensionnotextbox=updateTransactions[3].substring(1,
								updateTransactions[3].length() - 1);
						String airportCode=updateTransactions[4].substring(1,
								updateTransactions[4].length() - 1);
						log.info("pensionnotextbox"+pensionnotextbox);
						pfid=request.getParameter(pensionnotextbox);
						
                     	
							
				updatePfid = adjCrtnService.adjCrtnMappingUpdate(pfid, cpfaccno, region, userName,
						ipAddress,airportCode);
			}
			 
			 adjCrtnService.getDeleteAllRecords(pfid,"1995-2008","adjcorrections",userName,
					  ipAddress,"",chqFlag,empSubTot,aaiContriTot,pensionTot);
			try {
				adjCrtnService.insertRecordForAdjCtrnTracking(pfid, "", "1995-2008", "Mapped", userName, ipAddress);
				
				/*adjCrtnService.insertEmployeeTransData(pfid, "adjcorrections", userName,
						ipAddress,"","M",cpfaccno,region,"");*/
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			currPcTotals=adjCrtnService.updatePCAdjCorrections("01-Apr-1995", "31-Mar-2008", "",
					"", pfid, "","");
			adjCrtnService.saveCurrPctoals(pfid,"1995-2008",currPcTotals);
	
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/AdjCrtn/AdjCrtnDataMapping.jsp");
			rd.forward(request, response);

		}else if (request.getParameter("method").equals("adjCrtnMappingUpdateOnly")) {

			String pfid = "", cpfaccno = "", region = "",updatePfid="";
			ArrayList currPcTotals = new ArrayList();
			log.info("Mapped items  "
					+ request.getParameterValues("adjCrtnMappingUpdate"));
			String[] unmappedRecords = {};
			String[] updateTransactions = {};
			if (request.getParameterValues("adjCrtnMappingUpdate") != null) {
				unmappedRecords = (String[]) request
				.getParameterValues("adjCrtnMappingUpdate");
				
			}
			FinancialReportService finReportService = new FinancialReportService();
			String pfid1 = "";
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			for (int i = 0; i < unmappedRecords.length; i++) {
				updateTransactions = unmappedRecords[i].split(",");
			
						pfid = pfid1;
						cpfaccno = updateTransactions[1].substring(1,
								updateTransactions[1].length() - 1);
						region = updateTransactions[2].substring(1,
								updateTransactions[2].length() - 1);
						String pensionnotextbox=updateTransactions[3].substring(1,
								updateTransactions[3].length() - 1);
						String airportCode=updateTransactions[4].substring(1,
								updateTransactions[4].length() - 1);
						log.info("pensionnotextbox"+pensionnotextbox);
						pfid=request.getParameter(pensionnotextbox);
						
							
				updatePfid = adjCrtnService.adjCrtnMappingUpdate(pfid, cpfaccno, region, userName,
						ipAddress,airportCode);
			}
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("crtnMadeInPCReport")){String reportType="";
				String accessCode="",fromDate="",toDate="";
				ArrayList crtnsMadeInPcList=new ArrayList();
				if (request.getParameter("accessCode") != null) {
					accessCode = request.getParameter("accessCode");
				}
				if (request.getParameter("fromDate") != null) {
					fromDate = request.getParameter("fromDate");
				}  
				if (request.getParameter("toDate") != null) {
					toDate = request.getParameter("toDate");
				}  
				if (request.getParameter("reportType") != null) {
					reportType = request.getParameter("reportType");
				}  
				
		 
				crtnsMadeInPcList=adjCrtnService.crtnsMadeInPCAndPFCard(fromDate,toDate);
				request.setAttribute("reportType", reportType);
				request.setAttribute("crtnsMadeInPcList", crtnsMadeInPcList);
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/AdjCrtn/CrtnsTrackingReport.jsp");
				rd.forward(request, response);
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("DuplicateBlockedorFrozenReport")){
				String reportType="";
				String fromDate="",toDate="";
				ArrayList duplicateBlockedorFrozenList=new ArrayList();
				
				if (request.getParameter("fromDate") != null) {
					fromDate = request.getParameter("fromDate");
				}  
				if (request.getParameter("toDate") != null) {
					toDate = request.getParameter("toDate");
				}  
				if (request.getParameter("reportType") != null) {
					reportType = request.getParameter("reportType");
				}  
				
		 
				duplicateBlockedorFrozenList=finReportService.duplicateBlockedorFrozen(fromDate,toDate);
				request.setAttribute("reportType", reportType);
				request.setAttribute("duplicateBlockedorFrozenList", duplicateBlockedorFrozenList);
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/DuplicateBlockedorFrozenReport.jsp");
				rd.forward(request, response);
		}else if(request.getParameter("method").equals(
		"dashBoard")){
			String path="",date="",monthyear="--",pfwmonthyear="--",paymentStatus="Y",finalmonthyear="--",finalpaymentStatus="Y";
			int month=0,year=0;
			String finyear="",pfwfinyear="",finalfinyear="";
			DashBoardDetails dbinfo = new DashBoardDetails();
			DashBoardDetails dbpfwinfo = new DashBoardDetails();
			DashBoardDetails dbFinalinfo = new DashBoardDetails();
			if(request.getParameter("finyear")!=null ){
				finyear=request.getParameter("finyear");
			}else{
			 month=Integer.parseInt(commonUtil.getCurrentDate("MM"));
			 year=Integer.parseInt(commonUtil.getCurrentDate("yyyy"));
			 if(month<4){
				year=year-1;
			 }else{
				 year=year;
			 }
			 finyear=year+"-"+(year+1);
			}
			
			if(request.getParameter("monthYear")!=null ){
				monthyear=request.getParameter("monthYear");
			}
			//for block 2
			if(request.getParameter("pfwfinyear")!=null ){
				pfwfinyear=request.getParameter("pfwfinyear");
			}else{
			 month=Integer.parseInt(commonUtil.getCurrentDate("MM"));
			 year=Integer.parseInt(commonUtil.getCurrentDate("yyyy"));
			 if(month<4){
				year=year-1;
			 }else{
				 year=year;
			 }
			 pfwfinyear=year+"-"+(year+1);
			}
			if(request.getParameter("pfwmonthYear")!=null ){
				pfwmonthyear=request.getParameter("pfwmonthYear");
			}
			if(request.getParameter("paymentStatus")!=null ){
				paymentStatus=request.getParameter("paymentStatus");
			}
			//for block3
			if(request.getParameter("finalfinyear")!=null ){
				finalfinyear=request.getParameter("finalfinyear");
			}else{
			 month=Integer.parseInt(commonUtil.getCurrentDate("MM"));
			 year=Integer.parseInt(commonUtil.getCurrentDate("yyyy"));
			 if(month<4){
				year=year-1;
			 }else{
				 year=year;
			 }
			 finalfinyear=year+"-"+(year+1);
			}
			if(request.getParameter("finalmonthyear")!=null ){
				finalmonthyear=request.getParameter("finalmonthyear");
			}
			if(request.getParameter("finalpaymentStatus")!=null ){
				finalpaymentStatus=request.getParameter("finalpaymentStatus");
			}
			
			 log.info("finyear"+finyear+ monthyear);
			 dbinfo=dbService.getDashBoardInfo(finyear,monthyear);
			 log.info("pfwfinyear"+pfwfinyear+ pfwmonthyear+paymentStatus);
			 dbpfwinfo=dbService.getDashBoardPFWInfo(pfwfinyear,pfwmonthyear,paymentStatus);
			 log.info("finalfinyear"+finalfinyear+ finalmonthyear+finalpaymentStatus);
			 dbFinalinfo=dbService.getDashBoardFinalInfo(finalfinyear,finalmonthyear,finalpaymentStatus);
			 
			 request.setAttribute("dbinfo",dbinfo);
			 request.setAttribute("dbpfwinfo",dbpfwinfo);
			 request.setAttribute("dbFinalinfo",dbFinalinfo);
			path="./PensionView/dashboard/DashBordInputparams.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
			
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("getFrozenOrBlockedRecords")){
			String accessCode="",accountType="",privilages="",profileType="",loginUserId="",message="",result="";
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
			
			if(profileType.equals("U") || profileType.equals("R") || profileType.equals("C")){ 				 
				if(privilages.equals("NP")){					 
					message="U Don't Have Privilages to Access";
				}else{
					 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
					 log.info("==result==="+result);
					 if(result.equals("NotHaving")){
						 message="U Don't Have Privilages to Access";
					 }
				}
			}
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/AdjCrtn/AdjCrtnFrozenOrBlockedPfidSearch.jsp");
			request.setAttribute("message", message);
			request.setAttribute("accessCode",accessCode);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals(
				"searchFrozenOrBlockedRecords")) {
			log.info("searchApprovedRecords () ");
			String employeeName = "", empsrlNo="",path="";
			ArrayList searchList = new ArrayList();
	 
			if (request.getParameter("empsrlNo") != null) {			 
				
				empsrlNo = request.getParameter("empsrlNo");
			} else {
				empsrlNo="";
			}
	
			searchList=adjCrtnService.getApprovedRecords(empsrlNo);  
			path = "./PensionView/AdjCrtn/AdjCrtnFrozenOrBlockedPfidSearch.jsp"; 
			request.setAttribute("searchList",searchList);
			
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("updateApprovedRecordBlock")){ 

			String frozen="",blocked="",trackId="",empserialNo="",notes="",adjobYear="",loginUserId="",userName="";
			if (request.getParameter("empsrlNo") != null) {
				empserialNo = request.getParameter("empsrlNo");
			} else {
				empserialNo = "";
			}
			if (request.getParameter("trackId") != null) {
				trackId = request.getParameter("trackId");
			} else {
				trackId = "";
			}
			if (request.getParameter("notes") != null) {
				notes = request.getParameter("notes");
			} else {
				notes = "";
			}
			if (request.getParameter("blocked") != null) {
				blocked = request.getParameter("blocked");
			} else {
				blocked = "";
			}
		
			if (request.getParameter("adjobyear") != null) {
				adjobYear = request.getParameter("adjobyear");
			} else {
				frozen = "";
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			if (session.getAttribute("userid") != null) {
			 userName = (String) session.getAttribute("userid");
			}
			int result=adjCrtnService.updateApprovedRecord(empserialNo,trackId,notes,"BLOCKED",blocked,loginUserId,adjobYear);
				ArrayList searchList = new ArrayList();
			
			searchList=adjCrtnService.getApprovedRecords(empserialNo); 
	
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			for (int i = 0; searchList.size() != 0 && i <searchList.size(); i++) {
				String name = "", code = "";
				EmpMasterBean empBean = (EmpMasterBean) searchList
						.get(i);
				sb.append("<ServiceType>");
			
				sb.append("<block>");
				sb.append(empBean.getBlock());
				sb.append("</block>");
			

				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("updateApprovedRecordFrozen")){
			String frozen="",blocked="",trackId="",empserialNo="",notes="",loginUserId="",adjobYear="";
			if (request.getParameter("empsrlNo") != null) {
				empserialNo = request.getParameter("empsrlNo");
			} else {
				empserialNo = "";
			}
			if (request.getParameter("trackId") != null) {
				trackId = request.getParameter("trackId");
			} else {
				trackId = "";
			}
			if (request.getParameter("notes") != null) {
				notes = request.getParameter("notes");
			} else {
				notes = "";
			}
		
			if (request.getParameter("frozen") != null) {
				frozen = request.getParameter("frozen");
			} else {
				frozen = "";
			}
			if (request.getParameter("adjobyear") != null) {
				adjobYear = request.getParameter("adjobyear");
			} else {
				adjobYear = "";
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			
			int result=adjCrtnService.updateApprovedRecord(empserialNo,trackId,notes,"FROZEN",frozen,loginUserId,adjobYear);
				ArrayList searchList = new ArrayList();
			
			searchList=adjCrtnService.getApprovedRecords(""); 
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			for (int i = 0; searchList.size() != 0 && i <searchList.size(); i++) {
				String name = "", code = "";
				EmpMasterBean empBean = (EmpMasterBean) searchList
						.get(i);
				sb.append("<ServiceType>");
			
				sb.append("<Frozen>");
				sb.append(empBean.getFrozen());
				sb.append("</Frozen>");
			
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
			
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("employeesInAdjCrtnReport")){
			String reporttype="", privilages="", profileType="", userStation="", userRegion="", loginUserId="", accessCode="", accountType="" , employeeNo="";
			EmpMasterBean empSerach = new EmpMasterBean();
			ArrayList searchList = new ArrayList();
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
				userStation = ((String)session.getAttribute("station")).toLowerCase().trim();
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				userRegion = ((String) session.getAttribute("loginUsrRegion")).toLowerCase().trim();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}

			if (request.getParameter("reportType") != null) {
				reporttype=request.getParameter("reportType")
						.toString().trim();
			}
			 
			log.info("ReportServlet::employeesInAdjCrtnReport()");
			//searchList = adjCrtnService.searchAdjctrn(userRegion,userStation,profileType,accessCode,accountType,employeeNo); 
		 	 
			request.setAttribute("financeDatalist", searchList
					 );
			request.setAttribute("reportType", reporttype);
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/VerificationFinanceDataReport.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method")!=null
				&&request.getParameter("method").equals("getImpacCalcLogReport")){
			String reportType="";
			String accessCode="",privilages="",accountType="",message="",profileType="",errorFlag="false",userStation="",loginUserId="",result="",verifiedby="";
			ArrayList crtnsMadeInPcList=new ArrayList();
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
			if (request.getParameter("frm_reportType") != null) {
				reportType=request.getParameter("frm_reportType")
						.toString().trim();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			if(profileType.equals("U") || profileType.equals("R") || profileType.equals("C")){ 				 
				if(privilages.equals("NP")){					 
					message="U Don't Have Privilages to Access";
				}else{
					 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
					 log.info("==result==="+result);
					 if(result.equals("NotHaving")){
						 message="U Don't Have Privilages to Access";
					 }
				}
			}  
			
			String currentDate=commonUtil.getCurrentDate("dd/MMM/yyyy");
			request.setAttribute("currentDate", currentDate);
			request.setAttribute("message", message);
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/AdjCrtn/ImpactCalcLogReportInputParams.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("uploadToForm2")) {
			log.info("uploadToForm2()");
			EmpMasterBean empBean=new EmpMasterBean();
			PensionContBean PersonalInfo = new PensionContBean();
			EmployeePersonalInfo emppersonalInfo=new EmployeePersonalInfo();
			String reportType = "ExcelSheet",pensionno="",frmName="",adjobyear="",form2id="";
			if(request.getParameter("pensionno")!=null){
				pensionno=request.getParameter("pensionno");
			}else{
				pensionno="";
			}
			if(request.getParameter("frmName")!=null){
				frmName=request.getParameter("frmName");
			}else{
				frmName="";
			}
			if(request.getParameter("adjobyear")!=null){
				adjobyear=request.getParameter("adjobyear");
			}else{
				adjobyear="";
			}
			if(request.getParameter("form2id")!=null){
				form2id=request.getParameter("form2id");
			}else{
				form2id="";
			}
			if(form2id.equals("") ){
				//form2id=adjCrtnService.getForm2id(pensionno,adjobyear);
			}
			
			if(frmName.equals("CHQApproved")){
				empBean=adjCrtnService.getImpCalCForm2CHQData(pensionno,adjobyear);	
			}else{
				
				log.info("form2id"+form2id);
			
			empBean=adjCrtnService.getImpCalCForm2Data(pensionno,frmName,adjobyear,form2id);
			}
			request.setAttribute("empBean",empBean);
			request.setAttribute("reportType",reportType);
			 
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/AdjCrtn/AdjtsForForm2.jsp");
				rd.forward(request, response);
		}else if (request.getParameter("method").equals(
		"getBlockedPfidsReport")) {
			log.info("getBlockedPfidsReport () ");
			String employeeName = "", reporttype="",path="";
			ArrayList searchList = new ArrayList();
	 
			if (request.getParameter("reporttype") != null) {			 
				
				reporttype = request.getParameter("reporttype");
			} else {
				reporttype="";
			}

			searchList=adjCrtnService.getBlockedPfidsReport();  
			path = "./PensionView/AdjCrtn/AdjCrtnBlockedPfidsReport.jsp"; 
			request.setAttribute("searchList",searchList);
			request.setAttribute("reporttype",reporttype);
			
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		}else if (request.getParameter("method").equals("load12MnthStatmntSearchForCrtn")) {
			String  frmName="",accountType="",userRegion="",profileType="" ,userStation="",loginUserId="",result="",employeeNo="",dataFlag="", accessCode="",privilages="" ,message="" ,path="";
		 
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			} else {
				frmName = "";
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
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			
			if(profileType.equals("U") || profileType.equals("R") || profileType.equals("C")){ 				 
				if(privilages.equals("NP")){					 
					message="U Don't Have Privilages to Access";
				}else{
					 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
					 log.info("==result==="+result);
					 if(result.equals("NotHaving")){
						 message="U Don't Have Privilages to Access";
					 }
				}
			}
			 
				path="./PensionView/AdjCrtn/12MnthStatemenSearchtForCrtn.jsp";
			 
			log.info("==profileType==="+profileType+"==privilages=="+privilages+"==message=="+message);
			request.setAttribute("privilages",privilages);
			request.setAttribute("accountType",accountType);
			request.setAttribute("accessCode",accessCode);
			request.setAttribute("message",message);
			request.setAttribute("frmName", frmName); 
			RequestDispatcher rd = request.getRequestDispatcher(path); 
			rd.forward(request, response); 
		} else if (request.getParameter("method").equals(
		"form4inputparam")) {
			String path="./PensionView/AdjCrtn/form4inputparam.jsp";
			System.out.println("path====="+path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		}else if (request.getParameter("method").equals(
		"SumofSuppPCReport")) {
			String path="",reportType = "", sortingOrder = "", pensionno = "", searchFlag = "", pfidString = "NO-SELECT", airportCode = "", month = "", region = "NO-SELECT", year = "",adjFlag ="true",frmName="";
			String  pageType="Page",reportFlag="",accessCode="";
			ArrayList list = new ArrayList(); 
			if (request.getParameter("employeeNo") != null) {
				pensionno = request.getParameter("employeeNo");
			}
			if (request.getParameter("frm_Name") != null) {
				frmName = request.getParameter("frm_Name");
			}
			if (request.getParameter("reportType") != null) {
				reportType = request.getParameter("reportType");
			}
			list = adjCrtnService.SumofSuppPCReport(pensionno);

			request.setAttribute("cardList", list);
			path="./PensionView/AdjCrtn/SumofSuppPCReport.jsp";
			System.out.println("path====="+path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			request.setAttribute("reportType",reportType);
			rd.forward(request, response);
		}else if (request.getParameter("method").equals("searchFor12MnthStatemntCtrn")) {
			String  frmName="",accessCode="",privilages="",accountType="",userRegion="",profileType="" ,userStation="",loginUserId="",result="",employeeNo="",dataFlag="",errorFlag="false";
			String employeeName="",empRegion="",empStation="",enterdPfid="",message="";
			ArrayList empList = new ArrayList();
			ArrayList dataList = new ArrayList();
			EmpMasterBean empBean = new EmpMasterBean();
			EmpMasterBean empInfo = new EmpMasterBean(); 
			if (request.getParameter("empsrlNo") != null) {			 
				empBean.setEmpSerialNo(commonUtil.getSearchPFID(request.getParameter(
				"empsrlNo").toString().trim()));
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
				userRegion =   session.getAttribute("loginUsrRegion").toString().toLowerCase();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			if (!empBean.getEmpSerialNo().equals("")) {
				empList = commonDAO.getEmployeePersonalInfo(empBean
						.getEmpSerialNo());
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
				}else{
					message ="No Records Found";
					errorFlag="true"; 
				}
				empRegion=empInfo.getRegion().toLowerCase().trim();
				empStation=empInfo.getStation().toLowerCase().trim();
				log.info("privilages=="+privilages+"===profileType==="+profileType+"==");
				log.info("userRegion=="+userRegion+"==Employee=="+empRegion);
				log.info("userStation=="+userStation+"==Employee=="+empStation);
				
			if (empList.size() > 0) { 		
				if(privilages.equals("P")){
					 result = commonDAO.chkScreenAccessRights(loginUserId,accessCode);
					 log.info("Screen Access=="+result);
					 if(result.equals("NotHaving")){
						 message="U Don't Have Privilages to Access";
					 }else{ 
						 
				if(profileType.equals("U")){						 
					if((!empRegion.equals(userRegion))  || (!empStation.equals(userStation))) {
						message ="This Pfid belongs to "+empInfo.getRegion()+"/"+empInfo.getStation()+". You Can't Process";
					 	errorFlag="true"; 
					}  
				}else if(profileType.equals("R")){					 
					if(!(empRegion.equals(userRegion))) {
						message ="This Pfid belongs to "+empInfo.getRegion()+". You Can't Process";
						errorFlag="true";
					}  else if(!(empStation.equals(userStation))) {
						accountType= commonDAO.getAccountType(empRegion,empStation);
						if(!accountType.equals("")){
							//For Restricting  Rigths to RAU of CHQIAD to SAU Accounts on 07-Jun-2012
						if(empRegion.toUpperCase().equals("CHQIAD") && accountType.equals("SAU")){
							message ="This Pfid belongs to "+empInfo.getRegion()+"/"+empInfo.getStation()+". You Can't Process";
						 	errorFlag="true";
						 
						}
						//Commented on 14-May-2012 as per Instruction given By Sehgal 
						/*if(accountType.equals("SAU")){
							message ="This Pfid belongs to "+accountType+" Account Type. You Can't Process";
						 	errorFlag="true"; 
						}*/
					}else{
						message =" This Pfid does not belongs to Any Account Type. You Can't Process";
					 	errorFlag="true";
					}
					} 
					 
				  }
				}	
			}else{
				errorFlag="true";
				message ="U Don't Have Privilages to Access";
			}
			}
				
			}
				
			
			if(errorFlag.equals("false")){ 
				
				dataList = adjCrtnService.searchFor12MnthStatemntCtrn(userRegion,userStation,profileType,accountType,empBean.getEmpSerialNo());  
				
			}	
			
			
			
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/AdjCrtn/12MnthStatemenSearchtForCrtn.jsp");
			request.setAttribute("searchInfo", empBean);
			request.setAttribute("empsrlNo", empBean.getEmpSerialNo());
			if(dataList.size()>0){
			request.setAttribute("searchList", dataList);
			}else{
				dataFlag = "NoData";
				request.setAttribute("dataFlag",dataFlag);
			}
			request.setAttribute("frmName", frmName);
			request.setAttribute("message", message);
			request.setAttribute("accessCode",accessCode);			 
			request.setAttribute("empsrlNo",enterdPfid);
			rd.forward(request, response);
		}else if (request.getParameter("method").equals(
				"get12MnthStatmntForCrtn")) {
			String chkpfid="",reportType = "", sortingOrder = "", pensionno = "", searchFlag = "", pfidString = "NO-SELECT", airportCode = "", month = "", region = "NO-SELECT", year = "",adjFlag ="true",frmName="";
			String  pageType="Page",path="",reportFlag="",accessCode="";
			ArrayList list = new ArrayList(); 
			 
			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("adjFlag") != null) {
				adjFlag = request.getParameter("adjFlag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("reportFlag") != null) {
				reportFlag = request.getParameter("reportFlag");
			}
			if (request.getParameter("pageType") != null) {
				pageType = request.getParameter("pageType");
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			
			
			
			try {
				
				if(pageType.equals("Page")){
					chkpfid = adjCrtnService.chkPfidinAdjCrtn78PSTracking(pensionno, frmName);
					
					
					if (chkpfid.equals("NotExists")) {	
					adjCrtnService.insertEmployeeTransData(pensionno, frmName, userName,
							ipAddress,searchFlag,"","","","");
					}
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			            
			
			list = finReportService.getStatementOfWagePensionReport(pfidString,
					region, sortingOrder, pensionno, airportCode,adjFlag ,frmName);

			request.setAttribute("cardList", list);

			 
			String regionString = "", stationString = "";
			if (region.equals("NO-SELECT")) {
				regionString = "";
			} else {
				regionString = region;
			}
			if (airportCode.equals("NO-SELECT")) {
				stationString = "";
			} else {
				if (region.equals("CHQIAD")) {
					stationString = airportCode;
				} else {
					stationString = "";
				}

			}
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", regionString);
			request.setAttribute("reportType", reportType);			
			request.setAttribute("airportCode", stationString);
			request.setAttribute("frmName", frmName);
			request.setAttribute("accessCode",accessCode);	
			
			if(pageType.equals("Page")){				
				path="./PensionView/AdjCrtn/12MnthStatementForCrtn.jsp";
			}else{
				if(reportFlag.equals("Log")){
				path="./PensionView/AdjCrtn/12MnthStatementLogReportForCrtn.jsp";
				}else{
					path="./PensionView/AdjCrtn/12MnthStatementReportForCrtn.jsp";
				}
			}
		
			RequestDispatcher rd = request
					.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getCHQDeletion")) {
			String pfid = "", frmName = "", message = "", path = "",reportYear="",form2Status="",chqFlag="",empSubTot="0",aaiContriTot="0",pensionTot="0";
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
			 
			try {
				message = adjCrtnService.getCHQDeletion(pfid,reportYear,frmName,username,
						ipaddress,form2Status,chqFlag,empSubTot,aaiContriTot,pensionTot);
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

		}else if (request.getParameter("method").equals("getCHQApproverEditStatus")) {
			String pfid = "", frmName = "", message = "", path = "",reportYear="",editStatus="";
			StringBuffer sb = new StringBuffer();
			// new
			String username = "", ipaddress = "";
			if (session.getAttribute("userid") != null) {
				username = session.getAttribute("userid").toString();
			}
			if (session.getAttribute("computername") != null) {
				ipaddress = session.getAttribute("computername").toString();
			}
			if (request.getParameter("pensionno") != null) {
				pfid = request.getParameter("pensionno");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			if (request.getParameter("reportYear") != null) {
				reportYear = request.getParameter("reportYear");
			}
			 
			try {
				editStatus = adjCrtnService.getCHQApproverEditStatus(pfid,reportYear,frmName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			sb.append("<ServiceType>");
			sb.append("<EditStatus>");
			sb.append(editStatus);			 
			sb.append("</EditStatus>");
			sb.append("</ServiceType>");
		 

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());

		}  else if (request.getParameter("method").equals(
				"statmentpcwagesrevisedreport")) {
			String reportType = "", sortingOrder = "", pensionno = "", range = "", pfidString = "", airportCode = "", month = "", region = "", year = "",adjFlag ="",frmName="";
			ArrayList list = new ArrayList();

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_monthID") != null) {
				month = request.getParameter("frm_monthID");
				if (!month.equals("NO-SELECT")) {
					try {
						month = commonUtil.converDBToAppFormat(request
								.getParameter("frm_monthID"), "MM", "MMM");
					} catch (InvalidDataException e) {
						
						e.printStackTrace();
					}
				}

			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}

			if (request.getParameter("frmAirportCode") != null) {
				airportCode = request.getParameter("frmAirportCode");
			}

			if (request.getParameter("frm_region") != null
					&& request.getParameter("frm_region") != "") {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_pfids") != null
					&& request.getParameter("frm_pfids") != "") {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "cpfacno";
			}

			if (request.getParameter("frm_pensionno") != null) {
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("adjFlag") != null) {
				adjFlag = request.getParameter("adjFlag");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			log.info("===statmentpcwagesreport=====" + region + "year" + year
					+ "Month" + month + "pfidString" + pfidString + "pensionno"
					+ pensionno);

			list = finReportService.getStatementOfWagePensionRevisedReport(pfidString,
					region, sortingOrder, pensionno, airportCode,adjFlag ,frmName);

			request.setAttribute("cardList", list);

			request.setAttribute("reportType", reportType);
			String regionString = "", stationString = "";
			if (region.equals("NO-SELECT")) {
				regionString = "";
			} else {
				regionString = region;
			}
			if (airportCode.equals("NO-SELECT")) {
				stationString = "";
			} else {
				if (region.equals("CHQIAD")) {
					stationString = airportCode;
				} else {
					stationString = "";
				}

			}
			request.setAttribute("reportType", reportType);
			request.setAttribute("region", regionString);
			request.setAttribute("airportCode", stationString);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/RPFCStatementWagesRevisedReport.jsp");
			rd.forward(request, response);

		}else if (request.getParameter("method").equals(
				"loadBlockedorFrozenInput")) {
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/DuplicateBlockedORFrozenParams.jsp");
			rd.forward(request, response);
			
		}else if (request.getParameter("method").equals("editPensionInfo")) {
			log.info("reportservlet:editPensionInfo:---Entering method");
			String pensionNumber = "",clientname="",remarks="",cpfaccno="",region="",employeeName="",desig="",dateofbirth="",dateofJoining="",airportCode="",fathername="",employeeCode="",gender="",pensionOption="",pensiontype="",docchq="",mblno="",emailid="";
			//FormFile uploadFile="";
			int result=0;
			String message="";
			if (request.getParameter("pensionNumber") != null) {
				pensionNumber = request.getParameter("pensionNumber");
			}
			log.info("pfid"+pensionNumber);
			if (request.getParameter("clientname") != null) {
				clientname = request.getParameter("clientname");
			}
			log.info("clientname"+clientname);
			if (request.getParameter("remarks") != null) {
				remarks = request.getParameter("remarks");
			}
			log.info("remarks"+remarks);
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno");
			}
			log.info("cpfaccno"+cpfaccno);
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			log.info("region"+region);
			if (request.getParameter("employeeName") != null) {
				employeeName = request.getParameter("employeeName");
			}
			log.info("employeeName"+employeeName);
			if (request.getParameter("desig") != null) {
				desig = request.getParameter("desig");
			}
			log.info("desig"+desig);
			if (request.getParameter("dateofbirth") != null) {
				dateofbirth = request.getParameter("dateofbirth");
			}
			log.info("dateofbirth"+dateofbirth);
			if (request.getParameter("dateofJoining") != null) {
				dateofJoining = request.getParameter("dateofJoining");
			}
			log.info("dateofJoining"+dateofJoining);
			if (request.getParameter("airportCode") != null) {
				airportCode = request.getParameter("airportCode");
			}
			log.info("airportCode"+airportCode);
			if (request.getParameter("fathername") != null) {
				fathername = request.getParameter("fathername");
			}
			log.info("fathername"+fathername);
			if (request.getParameter("employeeCode") != null) {
				employeeCode = request.getParameter("employeeCode");
			}
			log.info("employeeCode"+employeeCode);
			if (request.getParameter("gender") != null) {
				gender = request.getParameter("gender");
			}
			log.info("gender"+gender);
			if (request.getParameter("pensionOption") != null) {
				pensionOption = request.getParameter("pensionOption");
			}
			log.info("pensionOption"+pensionOption);
			if (request.getParameter("pensiontype") != null) {
				pensiontype = request.getParameter("pensiontype");
			}
			log.info("pensiontype"+pensiontype);
			if (request.getParameter("docchq") != null) {
				docchq = request.getParameter("docchq");
			}
			log.info("docchq"+docchq);
			if (request.getParameter("mblno") != null) {
				mblno = request.getParameter("mblno");
			}
			log.info("mblno"+mblno);
			if (request.getParameter("emailid") != null) {
				emailid = request.getParameter("emailid");
			}
			if(request.getParameter("uploadedFile")!=null) {
				
			}
			
			pensionNumber = commonUtil.trailingZeros(pensionNumber.toCharArray());
			String userId = session.getAttribute("userid").toString();
			result=finReportService.editPensionProcessInfo(pensionNumber,clientname, remarks, cpfaccno, region ,employeeName,desig,dateofbirth,dateofJoining,airportCode,fathername,employeeCode,gender,pensionOption,pensiontype,docchq,mblno,emailid);
			if(result==0){
				message="Data Not Saved Successfully";
			}else{
				message="Data Saved Successfully";
			}		
			request.setAttribute("msg", message);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/dashboard/PensionProcessNewInputParams.jsp");
			rd.forward(request, response);
			log.info("reportservlet:editPensionInfo:---leaving method");
		}

	}
	public PensionContBean copyToBean(EmpMasterBean empBean,PensionContBean pBean){
		
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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

}
