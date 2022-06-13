/**
 * File       : SearchServlet.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */

package aims.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.DatabaseBean;
import aims.bean.EmpMasterBean;
import aims.bean.EmployeeAdlPensionInfo;
import aims.bean.EmployeeFreshOptionBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinacialDataBean;
import aims.bean.NomineeBean;
import aims.bean.NomineeForm2Info;
import aims.bean.RPFCForm9Bean;
import aims.bean.SearchInfo;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PensionService;
import aims.service.PersonalService;

public class SearchServlet extends HttpServlet {
	Log log = new Log(SearchPensionMasterServlet.class);

	CommonDAO commonDAO = new CommonDAO();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PensionService ps = new PensionService();
		FinancialService fs = new FinancialService();
		CommonUtil commonUtil = new CommonUtil();
		HttpSession session = request.getSession();
		PersonalService personalService = new PersonalService();
		int gridLength = Integer.parseInt((String) session
				.getAttribute("gridlength"));

		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {
			String region = "", stationName = "", accountType = "", airportLoadedByAccType = "";

			region = request.getParameter("region");

			if (request.getParameter("airportacctype") != null) {
				airportLoadedByAccType = request.getParameter("airportacctype");
			} else {
				airportLoadedByAccType = "";
			}
			if (request.getParameter("accounttype") != null) {
				accountType = request.getParameter("accounttype");
			} else {
				accountType = "";
			}
			ArrayList ServiceType = null;
			String username = session.getAttribute("userid").toString();
			String finalusername = username.substring(username.length() - 3,
					username.length());
			CommonUtil cutil = new CommonUtil();
			PensionService ps1 = new PensionService();
			boolean value = cutil.checkIfNumber(finalusername);
			if (session.getAttribute("station") != null) {
				stationName = (String) session.getAttribute("station");
			}
			if (value == true) {
				ServiceType = ps1.getUserAirports(region, username, "");
			} else if (!stationName.equals("")) {
				ServiceType = new ArrayList();
				EmpMasterBean masterBean = new EmpMasterBean();
				masterBean.setStation(stationName);
				ServiceType.add(masterBean);
			} else {

				if (airportLoadedByAccType.equals("Y")) {
					ServiceType = commonDAO.getAirportsByAccntType(region,
							accountType, "");
				} else {
					ServiceType = commonDAO.getAirportsByPersonalTbl(region,
							accountType, "");
				}

			}

			log.info("airport list " + ServiceType.size());
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
			log.info("Searchservlet");
			getSearchInfo = fs.financeDataReport(empSerach, getSearchInfo, 100,
					empSerach.getEmpNameCheak(), reporttype, empSerach
							.getPfidfrom());
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("reportType", reporttype);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/VerificationFinanceDataReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getPFIDForm78RevisedList")) {
			String region = "", airportcode = "", transferFlag = "", pageSize = "";
			int pereachpage = 0;
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_transflag") != null) {
				transferFlag = request.getParameter("frm_transflag");
			}
			if (request.getParameter("frm_pagesize") != null) {
				pageSize = request.getParameter("frm_pagesize");
			}
			log.info("==getPFIDList==" + region + "airportcode==="
					+ airportcode + "transferFlag===" + transferFlag);
			ArrayList ServiceType = null;
			ArrayList rangeList = new ArrayList();
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			int totalSize = 0;

			if (Integer.parseInt(pageSize) != 0) {
				totalSize = Integer.parseInt(pageSize);
			} else {
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle
							.getString("common.pension.pagesize"));
				} else {
					totalSize = 100;
				}
			}
			rangeList = commonDAO.getPFIDsForm78RevisedNaviagtionList(region,
					airportcode, transferFlag, Integer.toString(totalSize));
			log.info("pereachpage " + rangeList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String rangeString = "";
			for (int i = 0; i < rangeList.size(); i++) {

				rangeString = (String) rangeList.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				sb.append(rangeString);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getPFIDList")) {

			String region = "", airportcode = "", transferFlag = "", selectedSize = "";
			int pereachpage = 0;
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_transflag") != null) {
				transferFlag = request.getParameter("frm_transflag");
			}
			if (request.getParameter("frm_pagesize") != null) {
				selectedSize = request.getParameter("frm_pagesize");
			}
			log.info("==getPFIDList==" + region + "airportcode==="
					+ airportcode + "transferFlag===" + transferFlag);
			ArrayList ServiceType = null;
			ArrayList rangeList = new ArrayList();
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			int totalSize = 0;
			if (!selectedSize.equals("")) {
				totalSize = Integer.parseInt(selectedSize);
			} else {
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle
							.getString("common.pension.pagesize"));
				} else {
					totalSize = 100;
				}
			}

			rangeList = commonDAO.getPFIDsNaviagtionList(region, airportcode,
					transferFlag, Integer.toString(totalSize));
			log.info("pereachpage " + rangeList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String rangeString = "";
			for (int i = 0; i < rangeList.size(); i++) {
				rangeString = (String) rangeList.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				sb.append(rangeString);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getPFIDListWthoutTrnFlag")) {
			String region = "", monthYear = "", pageSize = "0", airportcode = "", lastmonthFlag = "", month = "", fromYear = "", toYear = "", year = "";
			ArrayList rangeList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}

			if (request.getParameter("frm_ltstmonthflag") != null) {
				lastmonthFlag = request.getParameter("frm_ltstmonthflag");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_pagesize") != null) {
				pageSize = request.getParameter("frm_pagesize");
			}
			String[] sltedYear = year.split("-");
			if (sltedYear.length == 2) {
				fromYear = sltedYear[0];
				toYear = sltedYear[1];
				String prefixFrmYear = fromYear.substring(0, 2);
				if (prefixFrmYear.equals("19")
						&& Integer.parseInt(fromYear.substring(0, 2))
								- Integer.parseInt(toYear) == 1) {
					toYear = prefixFrmYear + toYear;
				} else {
					toYear = "20" + toYear;
				}
				log.info("==getPFIDListWthoutTrnFlag=region=" + region
						+ "airportcode===" + airportcode + "lastmonthFlag"
						+ lastmonthFlag + "month" + month + "year" + year);
				log.info("==getPFIDListWthoutTrnFlag=fromYear=" + fromYear
						+ "toYear===" + toYear);

			} else {
				fromYear = sltedYear[0];
				toYear = Integer.toString(Integer.parseInt(fromYear) + 1);
			}
			if (!month.equals("NO-SELECT")) {
				if (Integer.parseInt(month) >= 4
						&& Integer.parseInt(month) <= 12) {
					monthYear = "-" + month + "-" + fromYear;
				} else if (Integer.parseInt(month) >= 1
						&& Integer.parseInt(month) <= 3) {
					monthYear = "-" + month + "-" + toYear;
				}
			} else {
				monthYear = commonDAO.getLatestTrnsDate(region);
			}
			log.info("==getPFIDListWthoutTrnFlag=fromYear=" + fromYear
					+ "toYear===" + toYear);
			ArrayList ServiceType = null;
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			int totalSize = 0;

			if (Integer.parseInt(pageSize) != 0) {
				totalSize = Integer.parseInt(pageSize);
			} else {
				if (bundle.getString("common.pension.pagesize") != null) {
					totalSize = Integer.parseInt(bundle
							.getString("common.pension.pagesize"));
				} else {
					totalSize = 100;
				}
			}

			rangeList = commonDAO.navgtionPFIDsList(region, airportcode,
					Integer.toString(totalSize), monthYear, lastmonthFlag);

			log.info("getPFIDListWthoutTrnFlag::Total List Size "
					+ rangeList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String rangeString = "";
			for (int i = 0; i < rangeList.size(); i++) {
				rangeString = (String) rangeList.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				sb.append(rangeString);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("<PARAMSTR>");
			sb.append("<PARAM1>");
			sb.append(monthYear);
			sb.append("</PARAM1>");
			sb.append("</PARAMSTR>");
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financesearch")) {
			log.info("SearchPensionMasterServlet : dopost() ");
			FinacialDataBean bean = new FinacialDataBean();
			if (request.getParameter("airPortCD") != null) {
				bean.setAirportCode(request.getParameter("airPortCD")
						.toString());
			} else {
				bean.setAirportCode("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(commonUtil.getSearchPFID1(request
						.getParameter("pfid").toString()));
			} else {
				bean.setPensionNumber("");
			}
			if (request.getParameter("employeeCode") != null) {

				bean.setEmployeeNewNo(request.getParameter("employeeCode")
						.toString());
			} else {
				bean.setEmployeeNewNo("");
			}
			String contextPath = request.getContextPath().toString();
			bean.setContextPath(contextPath);

			if (request.getParameter("cpfaccno") != null) {
				bean.setCpfAccNo(request.getParameter("cpfaccno").toString());
			} else {
				bean.setCpfAccNo("");
			}
			if (!request.getParameter("fromDt").equals("")) {
				try {
					bean.setFromDate(commonUtil.converDBToAppFormat(request
							.getParameter("fromDt").toString(), "dd/MM/yyyy",
							"dd/MMM/yyyy"));
				} catch (Exception e) {
					log.printStackTrace(e);
				}
			} else {
				bean.setFromDate("01/Apr/1995");
			}
			if (!request.getParameter("toDt").equals("")) {
				try {
					bean.setToDate(commonUtil.converDBToAppFormat(request
							.getParameter("toDt").toString(), "dd/MM/yyyy",
							"dd/MMM/yyyy"));
				} catch (Exception e) {
					log.printStackTrace(e);
				}
			} else {
				bean.setToDate(this.getCurrentDate("dd/MMM/yyyy"));
			}

			if (request.getParameter("missingPF") != null) {
				bean.setMissingPF(request.getParameter("missingPF").toString());
			} else {
				bean.setMissingPF("");
			}

			if (request.getParameter("region") != null) {
				bean.setRegion(request.getParameter("region").toString());
			} else {
				bean.setRegion("");
			}
			log.info("missing Flag" + bean.getMissingPF());
			log.info("region " + bean.getRegion());

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			try {
				log.info("From Date" + bean.getFromDate() + "To Date"
						+ bean.getToDate());
				searchBean = fs.searchFinanceData(bean, getSearchInfo,
						gridLength);
				log.info("Size of bean=========="
						+ searchBean.getSearchList().size());
				session.setAttribute("reportfinancebean", searchBean);
				session.setAttribute("reportsearchBean", bean);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("searchBean", searchBean);
			request.setAttribute("region", bean.getRegion().trim());

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financenavigation")) {
			log.info("SearchPensionMasterServlet : dopost() inside else");
			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();

			FinacialDataBean dbBeans = new FinacialDataBean();
			if (request.getParameter("navButton") != null) {

				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
			}
			System.out.println("strtIndex" + request.getParameter("strtindx"));
			if (request.getParameter("strtindx") != null) {

				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				System.out.println("strtIndex" + strtIndex);
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				log.info("totalRecords  " + totalRecords);
				searchListBean.setTotalRecords(totalRecords);

			}

			if (session.getAttribute("getSearchBean") != null) {
				dbBeans = (FinacialDataBean) session
						.getAttribute("getSearchBean");
			}
			try {
				navSearchBean = fs.searchNavigationFinanceData(dbBeans,
						searchListBean, gridLength);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataSearch.jsp");
			rd.forward(request, response);
		}

		if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchPensionDataMissingMonth")) {
			log.info("SearchPensionMasterServlet : dopost() ");
			DatabaseBean bean = new DatabaseBean();
			if (request.getParameter("empName") != null) {
				bean.setEmpName(request.getParameter("empName").toString());
			} else {
				bean.setEmpName("");
			}

			if (request.getParameter("fromDt") != null) {
				bean.setFromDt(request.getParameter("fromDt").toString());
			} else {
				bean.setFromDt("");
			}
			if (request.getParameter("toDt") != null) {
				bean.setToDt(request.getParameter("toDt").toString());
			} else {
				bean.setToDt("");
			}

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			try {
				searchBean = ps.searchPensionDataMissingMonth(bean,
						getSearchInfo, gridLength);
				request.setAttribute("searchBean", searchBean);
			} catch (Exception e) {

			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"pensionDataMissingMonthnavigation")) {
			log.info("SearchPensionMasterServlet : dopost() inside else");
			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();

			DatabaseBean dbBeans = new DatabaseBean();
			if (request.getParameter("navButton") != null) {

				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
			}
			if (request.getParameter("strtindx") != null) {
				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				searchListBean.setTotalRecords(totalRecords);

			}

			if (session.getAttribute("getSearchBean") != null) {
				dbBeans = (DatabaseBean) session.getAttribute("getSearchBean");
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionFundSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("navigation")) {
			log.info("SearchPensionMasterServlet : dopost() inside else");
			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();

			DatabaseBean dbBeans = new DatabaseBean();
			if (request.getParameter("navButton") != null) {

				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
			}
			System.out.println("strtIndex" + request.getParameter("strtindx"));
			if (request.getParameter("strtindx") != null) {

				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				System.out.println("strtIndex" + strtIndex);
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				searchListBean.setTotalRecords(totalRecords);
			}

			if (session.getAttribute("getSearchBean") != null) {
				dbBeans = (DatabaseBean) session.getAttribute("getSearchBean");
			}

			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionFundSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("getPerData")) {
			log.info("==============getPerData====================");
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			ArrayList penYearList = new ArrayList();
			request.setAttribute("yearIterator", yearIterator);
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			request.setAttribute("monthIterator", monthIterator);

			HashMap regionHashmap = new HashMap();
			regionHashmap = commonUtil.getRegion();
			request.setAttribute("regionHashmap", regionHashmap);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/PersonalDataMappingParams.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadPerData")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "", disMonthYear = "";
			String airportcode = "";
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
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			log.info("region==========loadPerData=====" + region + "year"
					+ year + "month" + month);
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

			// String message=personalService.autoProcPersonalInfo(selectedDate,
			// displayDate,region,airportcode,userName,ipAddress);
			String message = personalService.autoUpdatePersonalInfo(
					selectedDate, displayDate, region, airportcode, userName,
					ipAddress);
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			ArrayList penYearList = new ArrayList();
			request.setAttribute("yearIterator", yearIterator);
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			request.setAttribute("monthIterator", monthIterator);

			HashMap regionHashmap = new HashMap();
			regionHashmap = commonUtil.getRegion();
			request.setAttribute("regionHashmap", regionHashmap);
			request.setAttribute("message", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/PersonalDataMappingParams.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadPerMstr")
				|| request.getParameter("method").equals("loadForm10D")
				|| request.getParameter("method").equals("loadFreshPenOption")
				|| request.getParameter("method").equals("loadPensionProcess")
				|| request.getParameter("method").equals(
						"loadFreshPenOptionReports")) {
			ArrayList regionList = new ArrayList();
			String rgnName = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadPerMstr")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalMaster.jsp");
			} else if (request.getParameter("method").equals(
					"loadFreshPenOption")) {
				log.info("loadFreshPenOption------");
				rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeeFreshPensionOption.jsp");

			} else if (request.getParameter("method").equals(
					"loadFreshPenOptionReports")) {

				rd = request
						.getRequestDispatcher("./PensionView/personal/FreshPenOptionReportsInputParams.jsp");
			} else if (request.getParameter("method").equals(
					"loadPensionProcess")) {
				log.info("loadFreshPenOption------");
				rd = request
						.getRequestDispatcher("./PensionView/PensionProcess/PensionProcessSearch.jsp");

			} else if (request.getParameter("method").equals("loadForm10D")) {
				rd = request
						.getRequestDispatcher("./PensionView/form10D/Form-10DSearch.jsp");
			}

			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchPensionFreshOption")) {

			String rgnName = "", sortingColumn = "", page = "", message = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				empBean.setAirportCode(request.getParameter("airPortCode")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			log
					.info("pensionno"
							+ request.getParameter("pensionNO").toString());
			if (request.getParameter("pensionNO") != null) {
				empBean.setPensionNo(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				empBean.setPensionNo("");
			}
			log.info("pensionno " + empBean.getPensionNo());
			if (request.getParameter("frm_dateOfBirth") != null
					|| request.getParameter("frm_dateOfBirth") != "") {
				empBean.setDateOfBirth(request.getParameter("frm_dateOfBirth")
						.toString().trim());
			} else {
				empBean.setDateOfBirth("");
			}
			log.info("Date Of Birth====" + empBean.getDateOfBirth());
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAccno(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAccno("");
			}
			if (request.getParameter("uanNo") != null) {
				empBean.setUanno(request.getParameter("uanNo").toString()
						.trim());
			} else {
				empBean.setUanno("");
			}
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}
			ArrayList searchList = new ArrayList();
			try {
				searchList = personalService.searchPensionFreshOption(empBean,
						gridLength, sortingColumn);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			log
					.info("size--" + searchList.size() + "::"
							+ empBean.getPensionNo() + "++"
							+ empBean.getEmployeeName());
			if (searchList.size() < 1
					&& (!empBean.getPensionNo().equals("") || !empBean
							.getEmployeeName().equals(""))) {
				message = personalService.getMessageForFreshOption(empBean);
			} else {
				message = "No Records Found";
			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchList", searchList);
			request.setAttribute("searchInfo", empBean);
			request.setAttribute("message", message);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeeFreshPensionOption.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchPensionFreshOptionReport")) {
			String rgnName = "", sortingColumn = "", page = "", formType = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				empBean.setAirportCode(request.getParameter("airPortCode")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			log
					.info("pensionno"
							+ request.getParameter("pensionNO").toString());
			if (request.getParameter("pensionNO") != null) {
				empBean.setPensionNo(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				empBean.setPensionNo("");
			}
			log.info("pensionno " + empBean.getPensionNo());
			if (request.getParameter("frm_dateOfBirth") != null
					|| request.getParameter("frm_dateOfBirth") != "") {
				empBean.setDateOfBirth(request.getParameter("frm_dateOfBirth")
						.toString().trim());
			} else {
				empBean.setDateOfBirth("");
			}
			log.info("Date Of Birth====" + empBean.getDateOfBirth());
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAccno(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAccno("");
			}
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}
			if (request.getParameter("formtype") != null) {
				formType = request.getParameter("formtype");
			}
			ArrayList searchList = new ArrayList();
			try {
				searchList = personalService.searchPensionFreshOption(empBean,
						gridLength, sortingColumn);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("reportType", formType);
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchList", searchList);
			request.setAttribute("searchInfo", empBean);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeeFreshPensionOptionReport.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"editTransactionPenOptionData")) {
			log
					.info("Inside SearchServlet : editTransactionPenOptionData========");
			gridLength = Integer.parseInt((String) session
					.getAttribute("gridlength"));
			EmployeeFreshOptionBean freshOption = null;
			String appDate = "", rgnName = "";
			freshOption = new EmployeeFreshOptionBean();
			String[] regionLst = null;
			EmployeePersonalInfo info = new EmployeePersonalInfo();
			if (request.getParameter("pensionNo") != null) {
				freshOption.setPensionNo(request.getParameter("pensionNo"));
				// info.setPensionNo(freshOption.getPensionNo());
			}
			try {
				if (request.getParameter("appDate") != null) {					
					appDate=request.getParameter("appDate");
					log.info("Dates : "+appDate);
					freshOption.setApplicationDate(commonUtil.converDBToAppFormat(appDate, "dd/MM/yyyy", "dd-MMM-yyyy"));
				}
				if (request.getParameter("dob") != null) {
					log.info("Dates : "+request.getParameter("dob"));
					freshOption.setDateOfBirth(commonUtil.converDBToAppFormat(request.getParameter("dob"), "dd/MM/yyyy", "dd-MMM-yyyy"));
				}
				if (request.getParameter("doj") != null) {
					log.info("Dates : "+request.getParameter("doj"));
					freshOption.setDateOfJoining(commonUtil.converDBToAppFormat(request.getParameter("doj"), "dd/MM/yyyy", "dd-MMM-yyyy"));
				}
				log.info("Dates : "+freshOption.getApplicationDate()+""+freshOption.getDateOfBirth()+""+freshOption.getDateOfJoining());
			}catch (Exception e) {
				// TODO: handle exception
				log.printStackTrace(e);
			}
			
			if (request.getParameter("oldPensionOption") != null) {
				freshOption.setOldPensionOption(request
						.getParameter("oldPensionOption"));
			}
			if (request.getParameter("fhName") != null) {
				freshOption.setFhName(request.getParameter("fhName"));
			}
			if (request.getParameter("pensionOption") != null) {
				freshOption.setNewPensionOption(request
						.getParameter("pensionOption"));
			}
			if (request.getParameter("region") != null) {
				freshOption.setRegion(request.getParameter("region"));
			}
			if (request.getParameter("designation") != null) {
				freshOption.setDesignation(request.getParameter("designation"));
			}
			if (request.getParameter("airportCode") != null) {
				freshOption.setAirportCode(request.getParameter("airportCode"));

			}
			if (request.getParameter("cpfAcno") != null) {
				freshOption.setCpfAcno(request.getParameter("cpfAcno"));
			}
			if (request.getParameter("uanno") != null) {
				freshOption.setUanno(request.getParameter("uanno"));
			}			
			if (request.getParameter("empCode") != null) {
				freshOption.setEmployeeNo(request
						.getParameter("empCode"));
			}
			
			if (request.getParameter("empName") != null) {
				freshOption.setEmployeeName(request
						.getParameter("empName"));
			}
			if (request.getParameter("userName") != null) {
				freshOption.setUserName(request.getParameter("userName"));
			}
			String ipAddress = (String) session.getAttribute("computername");
			freshOption.setIpAddress(ipAddress);
			personalService.insertPensionFreshOptionData(freshOption);
			log.info("pensionno" + freshOption.getPensionNo()
					+ "applicationdate" + freshOption.getApplicationDate()
					+ "oldpensionOption" + freshOption.getOldPensionOption()
					+ "newpensionOption" + freshOption.getNewPensionOption()
					+ "region" + freshOption.getRegion() + "airportcode"
					+ freshOption.getAirportCode() + "employename"
					+ freshOption.getEmployeeName() + "empno"
					+ freshOption.getEmployeeNo() + "cpfAcno"
					+ freshOption.getCpfAcno() + "designation"
					+ freshOption.getDesignation());
			log
					.info("End of SearchServlet : editTransactionPenOptionData========");
			ArrayList searchList = new ArrayList();
			if (request.getParameter("searchRegion") != null) {
				info.setRegion(request.getParameter("searchRegion"));
			}
			if (request.getParameter("searchAirport") != null) {
				info.setAirportCode(request.getParameter("searchAirport"));
			}
			if (request.getParameter("searchPensionno") != null) {
				info.setPensionNo(request.getParameter("searchPensionno"));
			}
			// info.setAppDate(appDate);
			try {
				searchList = personalService.searchPensionFreshOption(info,
						gridLength, "EMPLOYEENAME");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			Map map = new HashMap();
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchList", searchList);
			request.setAttribute("searchInfo", info);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeeFreshPensionOption.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("searchPersonal")) {
			String rgnName = "", sortingColumn = "", page = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				empBean.setAirportCode(request.getParameter("airPortCode")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			log
					.info("pensionno"
							+ request.getParameter("pensionNO").toString());
			if (request.getParameter("pensionNO") != null) {
				empBean.setPensionNo(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				empBean.setPensionNo("");
			}
			log.info("pensionno " + empBean.getPensionNo());
			if (request.getParameter("page") != null) {
				page = request.getParameter("page").toString();
			}
			if (request.getParameter("frm_dateOfBirth") != null
					|| request.getParameter("frm_dateOfBirth") != "") {
				empBean.setDateOfBirth(request.getParameter("frm_dateOfBirth")
						.toString().trim());
			} else {
				empBean.setDateOfBirth("");
			}
			log.info("Date Of Birth====" + empBean.getDateOfBirth());
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAccno(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAccno("");
			}
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}

			if (request.getParameter("uan") != null) {
				empBean.setUanno(request.getParameter("uan")
						.toString().trim());
			} else {
				empBean.setUanno("");
			}
			// code added for empname blanks search
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {

				searchBean = personalService.searchPersonalMaster(empBean,
						getSearchInfo, flag, gridLength, sortingColumn, "");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);
			if (page != "") {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/EmpPfcardEdit.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalMaster.jsp");
				rd.forward(request, response);
			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("personalnav")) {
			String navgationBtn = "";
			String rgnName = "", sortingColumn = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();

			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = (String) session.getAttribute("navButton");
				searchListBean.setNavButton(navgationBtn);
			}

			if (request.getParameter("strtindx") != null) {

				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				System.out.println("strtIndex" + strtIndex);
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				searchListBean.setTotalRecords(totalRecords);
			}
			if (session.getAttribute("getSearchBean1") != null) {
				personalInfo = (EmployeePersonalInfo) session
						.getAttribute("getSearchBean1");
			}
			try {
				navSearchBean = personalService.navigationPersonalData(
						personalInfo, searchListBean, gridLength,
						sortingColumn, "");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", personalInfo);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeePersonalMaster.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("personalEdit")) {

			String empName = "", empCode = "", region = "";
			String editFrom = "";
			String empSerailNumber1 = "", dateofbirth1 = "", dob1 = "", view = "";
			String employeeSearchName = "";
			String arrearInfo = "", cpfacno = "";
			String pfid = "";
			pfid = request.getParameter("pfid").toString();
			cpfacno = request.getParameter("cpfacno").trim();
			String startIndex = "", totalData = "";
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			if (request.getParameter("view") != null) {
				view = request.getParameter("view");
			}
			if (request.getParameter("arrearInfo") != null) {
				arrearInfo = request.getParameter("arrearInfo").toString();
			} else {
				arrearInfo = "";
			}
			if (request.getParameter("empSerailNumber") != null) {
				empSerailNumber1 = request.getParameter("empSerailNumber");
				session.setAttribute("empSerailNumber1", empSerailNumber1);
			}
			if (request.getParameter("dateofbirth") != null) {
				dateofbirth1 = request.getParameter("dateofbirth");
				session.setAttribute("dateofbirth1", dateofbirth1);
			}
			if (request.getParameter("empName") != null) {
				employeeSearchName = request.getParameter("empName");
				session.setAttribute("employeeSearchName", employeeSearchName);
			}
			if (request.getParameter("dob1") != null) {
				dob1 = request.getParameter("dob1");
				session.setAttribute("dob1", dob1);
			}
			if (session.getAttribute("empBean1") != null) {
				log.info("session " + session.getAttribute("empBean1"));
			}
			if (request.getParameter("name") != null) {
				empName = request.getParameter("name");
			}
			if (request.getParameter("empCode") != null) {
				empCode = request.getParameter("empCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region").trim();
			}
			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}

			boolean flag;
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;

			} else {
				flag = true;
			}
			if (request.getParameter("recordExist") != null) {
				request.setAttribute("recordExist", request.getParameter(
						"recordExist").toString());
			}
			ArrayList airportList = new ArrayList();
			ArrayList departmentList = new ArrayList();
			EmpMasterBean editBean = new EmpMasterBean();
			try {
				editBean = personalService.empPersonalEdit(cpfacno, empName,
						flag, empCode, region.trim(), pfid);

				airportList = commonUtil.getAirports(region);
				departmentList = ps.getDepartmentList();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("EditBean", editBean);
			request.setAttribute("AirportList", airportList);
			request.setAttribute("DepartmentList", departmentList);
			request.setAttribute("flag", Boolean.toString(flag));
			request.setAttribute("startIndex", startIndex);
			request.setAttribute("TotalData", totalData);
			request.setAttribute("editFrom", editFrom);
			request.setAttribute("ArrearInfo", arrearInfo);
			if (!view.equals("true")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalMasterEditRevised.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/personal/EmployeePersonalMasterView.jsp");
				rd.forward(request, response);

			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("personalUpdate")) {
			log.info("SearchServlet : dopost() ");
			EmpMasterBean bean = new EmpMasterBean();
			String flag = "", empName = "";
			String startIndex = "", totalData = "";
			String editFrom = "";
			String arrearInfo = "";

			if (request.getParameter("ArrearInfo") != null) {
				arrearInfo = request.getParameter("ArrearInfo").toString();
			} else {
				arrearInfo = "";
			}
			if (request.getParameter("setEmpSerialNo") != null) {
				bean.setEmpSerialNo(request.getParameter("setEmpSerialNo")
						.trim().toString());
			}
			if (request.getParameter("select_fhname") != null) {
				bean.setFhFlag(request.getParameter("select_fhname").trim()
						.toString());
			}
			log.info("arrear info" + arrearInfo.toString());
			request.setAttribute("ArrearInfo", arrearInfo);
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			String airportWithRegion = request
					.getParameter("airportWithRegion");
			String region1 = airportWithRegion.substring(airportWithRegion
					.indexOf("-") + 1, airportWithRegion.length());
			bean.setNewRegion(region1);

			// New Patameters added while Changing employee information from one
			// region to other
			if (request.getParameter("select_region") != null) {
				bean.setChangedRegion(request.getParameter("select_region")
						.trim().toString());
			} else {
				bean.setChangedRegion("");
			}
			if (request.getParameter("select_airport") != null) {
				bean.setChangedStation(request.getParameter("select_airport")
						.trim().toString());
			} else {
				bean.setChangedStation("");
			}

			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}
			if (request.getParameter("flagData") != null) {
				flag = request.getParameter("flagData");
			} else {
				flag = "true";
			}
			if (request.getParameter("empName") != null) {
				bean.setEmpName(request.getParameter("empName").trim()
						.toString());
			} else {
				bean.setEmpName("");
			}
			if (request.getParameter("empOldName") != null) {
				bean.setEmpOldName(request.getParameter("empOldName").trim()
						.toString());
			} else {
				bean.setEmpOldName("");
			}
			if (request.getParameter("empOldNumber") != null) {
				bean.setEmpOldNumber(request.getParameter("empOldNumber")
						.trim().toString());
			} else {
				bean.setEmpOldNumber("");
			}

			if (request.getParameter("airPortCode") != null) {
				bean.setStation(request.getParameter("airPortCode").toString());
			} else {
				bean.setStation("");
			}
			if (request.getParameter("desegnation") != null) {
				bean.setDesegnation(request.getParameter("desegnation")
						.toString());
			} else {
				bean.setDesegnation("");
			}
			if (request.getParameter("cpfacno") != null) {
				bean.setCpfAcNo(request.getParameter("cpfacno").toString());
			} else {
				bean.setCpfAcNo("");
			}
			log.info("new cpfacno " + request.getParameter("cpfacnoNew"));
			if (request.getParameter("cpfacnoNew") != null) {
				bean.setNewCpfAcNo(request.getParameter("cpfacnoNew")
						.toString());
			} else {
				bean.setNewCpfAcNo("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(request.getParameter("pfid").toString());
			} else {
				bean.setPensionNumber("");
			}
			if (request.getParameter("employeeCode") != null) {
				bean.setEmpNumber(request.getParameter("employeeCode").trim()
						.toString());
			} else {
				bean.setEmpNumber("");
			}
			if (request.getParameter("airportSerialNumber") != null) {
				bean.setAirportSerialNumber(request.getParameter(
						"airportSerialNumber").trim().toString());
			} else {
				bean.setAirportSerialNumber("");
			}
			if (request.getParameter("emplevel") != null) {
				bean.setEmpLevel(request.getParameter("emplevel").toString());
			} else {
				bean.setEmpLevel("");
			}
			if (request.getParameter("dateofbirth") != null) {
				bean.setDateofBirth(request.getParameter("dateofbirth"));
			} else {
				bean.setDateofBirth("");
			}
			if (request.getParameter("dateofjoining") != null) {
				bean.setDateofJoining(request.getParameter("dateofjoining")
						.toString());
			} else {
				bean.setDateofJoining("");
			}
			if (request.getParameter("reason") != null) {
				bean.setSeperationReason(request.getParameter("reason")
						.toString());
			} else {
				bean.setSeperationReason("");
			}
			if (request.getParameter("Other") != null) {
				bean.setOtherReason(request.getParameter("Other").toString());
			} else {
				bean.setOtherReason("");
			}
			if (request.getParameter("seperationDate") != null) {
				bean.setDateofSeperationDate(request.getParameter(
						"seperationDate").toString());
			} else {
				bean.setSeperationReason("");
			}
			if (request.getParameter("emailId") != null) {
				bean.setEmailId(request.getParameter("emailId").toString());
			} else {
				bean.setEmailId("");
			}
			if (request.getParameter("equalShare") != null) {
				bean.setEmpNomineeSharable(request.getParameter("equalShare")
						.toString().trim());
			} else
				bean.setEmpNomineeSharable("");
			if (request.getParameter("form1") != null) {
				String form1 = request.getParameter("form1");
				bean.setForm2Nomination(request.getParameter("form1")
						.toString());
			} else {
				bean.setForm2Nomination("");
			}
			if (request.getParameter("remarks") != null) {
				bean.setRemarks(request.getParameter("remarks").toString());
			} else {
				bean.setRemarks("");
			}
			if (request.getParameter("optionform") != null) {
				bean.setOptionForm(request.getParameter("optionform")
						.toString());
			} else {
				bean.setOptionForm("");
			}
			if (request.getParameter("region") != null) {
				bean
						.setRegion(request.getParameter("region".trim()
								.toString()));
			} else {
				bean.setRegion("");
			}
			if (request.getParameter("division") != null) {
				bean.setDivision(request.getParameter("division").toString());
			} else {
				bean.setDivision("");
			}
			if (request.getParameter("department") != null) {
				bean.setDepartment(request.getParameter("department")
						.toString());
			} else {
				bean.setDepartment("");
			}
			if (request.getParameter("paddress") != null) {
				bean.setPermanentAddress(request.getParameter("paddress")
						.toString().trim());
			} else {
				bean.setPermanentAddress("");
			}
			if (request.getParameter("taddress") != null) {
				bean.setTemporatyAddress(request.getParameter("taddress")
						.toString().trim());
			} else {
				bean.setTemporatyAddress("");
			}
			if (request.getParameter("wetherOption") != null) {
				bean.setWetherOption(request.getParameter("wetherOption")
						.toString().trim());
			} else {
				bean.setWetherOption("");
			}

			if (request.getParameter("sex") != null) {
				bean.setSex(request.getParameter("sex").toString().trim());
			} else {
				bean.setSex("");
			}
			if (request.getParameter("mstatus") != null) {
				bean.setMaritalStatus(request.getParameter("mstatus")
						.toString().trim());
			} else {
				bean.setMaritalStatus("");
			}
			if (request.getParameter("fhname") != null) {
				bean
						.setFhName(request.getParameter("fhname").toString()
								.trim());
			} else {
				bean.setFhName("");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				bean.setDateOfAnnuation(request.getParameter("dateOfAnnuation")
						.toString().trim());
			} else {
				bean.setDateOfAnnuation("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(commonUtil.getSearchPFID(request
						.getParameter("pfid").toString().trim()));
			} else {
				bean.setPensionNumber("");
			}

			log.info("pensionno " + bean.getPensionNumber());
			/* for Family Details */

			if (request.getParameterValues("FName") != null) {

				String fname[] = request.getParameterValues("FName");
				String empOldFRName[] = request
						.getParameterValues("empOldFRName");
				String fdob[] = request.getParameterValues("Fdob");
				String frelation[] = request.getParameterValues("Frelation");
				StringBuffer familyRow = new StringBuffer();
				System.out.println(fname.length);
				for (int i = 0; i < fname.length; i++) {
					String fmDOB = "", fRelation = "";
					String frOldName = "XXX";
					if (!fname[i].equals("")) {
						familyRow.append(fname[i].toString() + "@");
						System.out.println("fdob" + fdob[i]);

						if (fdob[i].equals("")) {
							fmDOB = "XXX";
						} else {

							fmDOB = fdob[i].toString();
							// fmDOB = commonUtil.converDBToAppFormat(fmDOB,
							// "dd/MM/yyyy", "dd-MMM-yyyy");
						}
						if (frelation[i].equals("")) {
							fRelation = "XXX";
						} else {
							fRelation = frelation[i].toString();
						}

						if (empOldFRName[i].equals("")) {
							frOldName = "XXX";
						} else {
							frOldName = empOldFRName[i].toString().trim();
						}
						familyRow.append(fmDOB + "@");
						familyRow.append(fRelation + "@");
						familyRow.append(frOldName);
						familyRow.append("***");
					}

				}

				System.out.println("family data " + familyRow.toString());

				if (familyRow.toString() != null) {
					bean.setFamilyRow(familyRow.toString());
				} else {
					bean.setFamilyRow("");
				}

			}
			/* end of Family Details */
			/* for Nominee Details */
			if (request.getParameterValues("Nname") != null) {
				String nomineeRowID[] = request
						.getParameterValues("nomineerowid");
				String Nname[] = request.getParameterValues("Nname");
				String empOldNname[] = request
						.getParameterValues("empOldNname");
				String Naddress[] = request.getParameterValues("Naddress");
				String Ndob[] = request.getParameterValues("Ndob");
				String Nrelation[] = request.getParameterValues("Nrelation");
				String guardianname[] = request
						.getParameterValues("guardianname");
				String gaddress[] = request.getParameterValues("gaddress");
				String totalshare[] = request.getParameterValues("totalshare");
				StringBuffer nomineeRow = new StringBuffer();
				System.out.println(Nname.length);
				String nAddress = "", nDob = "", nRowid = "", nRelation = "", nGuardianname = "", nTotalshare = "", gardaddressofNaminee = "";
				String empOldNomineeName = "";
				for (int i = 0; i < Nname.length; i++) {
					if (!Nname[i].equals("")) {
						nomineeRow.append(Nname[i].toString() + "@");
						if (Naddress[i].equals("")) {
							nAddress = "XXX";
						} else {
							nAddress = Naddress[i].toString();
						}
						if (Nrelation[i].equals("")) {
							nRelation = "XXX";
						} else {
							nRelation = Nrelation[i].toString();
						}

						if (Ndob[i].equals("")) {
							nDob = "XXX";
						} else {
							nDob = Ndob[i].toString();
						}
						if (guardianname[i].equals("")) {
							nGuardianname = "XXX";
						} else {
							nGuardianname = guardianname[i].toString();
						}
						if (gaddress[i].equals("")) {
							gardaddressofNaminee = "XXX";
						} else {
							gardaddressofNaminee = gaddress[i].toString()
									.trim();
						}

						if (empOldNname[i].equals("")) {
							empOldNomineeName = "XXX";
						} else {
							empOldNomineeName = empOldNname[i].toString()
									.trim();
						}
						if (totalshare[i].equals("")) {
							nTotalshare = "XXX";
						} else {
							nTotalshare = totalshare[i].toString();
						}

						if (!nomineeRowID[i].equals("")) {
							nRowid = nomineeRowID[i].toString();
						} else {
							nRowid = "XXX";
						}
						nomineeRow.append(nAddress + "@");
						nomineeRow.append(nDob + "@");
						nomineeRow.append(nRelation + "@");
						nomineeRow.append(nGuardianname + "@");
						nomineeRow.append(gardaddressofNaminee + "@");
						nomineeRow.append(empOldNomineeName + "@");
						nomineeRow.append(nTotalshare + "@");
						nomineeRow.append(nRowid);
						nomineeRow.append("***");
					}

				}

				System.out.println("Nominee data " + nomineeRow.toString());

				if (nomineeRow.toString() != null) {
					bean.setNomineeRow(nomineeRow.toString());
				} else {
					bean.setNomineeRow("");
				}

			}
			String userName = (String) session.getAttribute("userid");
			String computerName = (String) session.getAttribute("computername");
			bean.setUserName(userName);
			bean.setComputerName(computerName);

			/* endof Nominee Details */

			int count = 0;
			String message = "";
			try {
				count = personalService.updatePensionMaster(bean, flag);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				// TODO Auto-generated catch block
				message = e.getMessage();

			}
			String cpfacno = bean.getCpfAcNo();
			RequestDispatcher rd = null;
			log.info("=======cpfacno" + cpfacno + "flag==" + flag + "name="
					+ empName + "count" + count);
			if (count == 0) {
				if (flag.equals("false")) {
					flag = "invflag";
				}

				rd = request
						.getRequestDispatcher("./search1?method=edit&cpfacno="
								+ cpfacno + "&recordExist=" + message
								+ "&flag=" + flag + "&name=" + empName);

			} else if (flag.equals("true")
					&& editFrom.equals("uniqueNumberGen")) {
				log.info("inside uniqueNumberGen " + arrearInfo);
				rd = request
						.getRequestDispatcher("./search1?method=searchRecordsbyDobandName");

			} else if (flag.equals("true")
					&& editFrom.equals("getProcessUnprocessList")) {
				log.info("inside getProcessUnprocessList ");
				String empSerailNumber1 = "", dateofbirth = "";
				if (session.getAttribute("dateofbirth1") != null) {
					dateofbirth = (String) session.getAttribute("dateofbirth1");
				}
				if (session.getAttribute("empSerailNumber1") != null) {
					empSerailNumber1 = (String) session
							.getAttribute("empSerailNumber1");
				}

				log.info("dateofbirth" + dateofbirth + " empSerailNumber1"
						+ empSerailNumber1);

			} else {
				rd = request
						.getRequestDispatcher("./psearch?method=personalnav&strtindx="
								+ startIndex + "&total" + totalData);
			}

			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"personalUpdateRevised")) {
			log.info("SearchServlet : dopost() ");
			EmpMasterBean bean = new EmpMasterBean();
			String flag = "", empName = "";
			String startIndex = "", totalData = "";
			String editFrom = "";
			String arrearInfo = "";

			if (request.getParameter("ArrearInfo") != null) {
				arrearInfo = request.getParameter("ArrearInfo").toString();
			} else {
				arrearInfo = "";
			}
			if (request.getParameter("setEmpSerialNo") != null) {
				bean.setEmpSerialNo(request.getParameter("setEmpSerialNo")
						.trim().toString());
			}
			if (request.getParameter("select_fhname") != null) {
				if (request.getParameter("select_fhname").trim().toString()
						.equals("Father")) {
					bean.setFhFlag("F");
				} else {
					bean.setFhFlag("H");
				}
			}
			log.info("arrear info" + arrearInfo.toString());
			request.setAttribute("ArrearInfo", arrearInfo);
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			/*
			 * String airportWithRegion =
			 * request.getParameter("airportWithRegion"); String region1 =
			 * airportWithRegion.substring(airportWithRegion .indexOf("-") + 1,
			 * airportWithRegion.length()); bean.setNewRegion(region1);
			 */
			// New Patameters added while Changing employee information from one
			// region to other
			if (request.getParameter("select_region") != null) {
				bean.setChangedRegion(request.getParameter("select_region")
						.trim().toString());
			} else {
				bean.setChangedRegion("");
			}
			if (request.getParameter("select_airport") != null) {
				bean.setChangedStation(request.getParameter("select_airport")
						.trim().toString());
			} else {
				bean.setChangedStation("");
			}

			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}
			if (request.getParameter("flagData") != null) {
				flag = request.getParameter("flagData");
			} else {
				flag = "true";
			}
			if (request.getParameter("empName") != null) {
				bean.setEmpName(request.getParameter("empName").trim()
						.toString());
			} else {
				bean.setEmpName("");
			}
			if (request.getParameter("empOldName") != null) {
				bean.setEmpOldName(request.getParameter("empOldName").trim()
						.toString());
			} else {
				bean.setEmpOldName("");
			}
			if (request.getParameter("empOldNumber") != null) {
				bean.setEmpOldNumber(request.getParameter("empOldNumber")
						.trim().toString());
			} else {
				bean.setEmpOldNumber("");
			}

			if (request.getParameter("airPortCode") != null) {
				bean.setStation(request.getParameter("airPortCode").toString());
			} else {
				bean.setStation("");
			}
			if (request.getParameter("desegnation") != null) {
				bean.setDesegnation(request.getParameter("desegnation")
						.toString());
			} else {
				bean.setDesegnation("");
			}
			if (request.getParameter("cpfacno") != null) {
				bean.setCpfAcNo(request.getParameter("cpfacno").toString());
			} else {
				bean.setCpfAcNo("");
			}
			log.info("new cpfacno " + request.getParameter("cpfacnoNew"));
			if (request.getParameter("cpfacnoNew") != null) {
				bean.setNewCpfAcNo(request.getParameter("cpfacnoNew")
						.toString());
			} else {
				bean.setNewCpfAcNo("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(request.getParameter("pfid").toString());
			} else {
				bean.setPensionNumber("");
			}
			if (request.getParameter("employeeCode") != null) {
				bean.setEmpNumber(request.getParameter("employeeCode").trim()
						.toString());
			} else {
				bean.setEmpNumber("");
			}
			if (request.getParameter("airportSerialNumber") != null) {
				bean.setAirportSerialNumber(request.getParameter(
						"airportSerialNumber").trim().toString());
			} else {
				bean.setAirportSerialNumber("");
			}
			if (request.getParameter("emplevel") != null) {
				bean.setEmpLevel(request.getParameter("emplevel").toString());
			} else {
				bean.setEmpLevel("");
			}
			if (request.getParameter("dateofbirth") != null) {
				bean.setDateofBirth(request.getParameter("dateofbirth"));
			} else {
				bean.setDateofBirth("");
			}
			if (request.getParameter("dateofjoining") != null) {
				bean.setDateofJoining(request.getParameter("dateofjoining")
						.toString());
			} else {
				bean.setDateofJoining("");
			}
			if (request.getParameter("reason") != null) {
				bean.setSeperationReason(request.getParameter("reason")
						.toString());
			} else {
				bean.setSeperationReason("");
			}
			if (request.getParameter("Other") != null) {
				bean.setOtherReason(request.getParameter("Other").toString());
			} else {
				bean.setOtherReason("");
			}
			if (request.getParameter("seperationDate") != null) {
				bean.setDateofSeperationDate(request.getParameter(
						"seperationDate").toString());
			} else {
				bean.setSeperationReason("");
			}
			if (request.getParameter("emailId") != null) {
				bean.setEmailId(request.getParameter("emailId").toString());
			} else {
				bean.setEmailId("");
			}
			if (request.getParameter("equalShare") != null) {
				bean.setEmpNomineeSharable(request.getParameter("equalShare")
						.toString().trim());
			} else
				bean.setEmpNomineeSharable("");
			if (request.getParameter("form1") != null) {
				String form1 = request.getParameter("form1");
				bean.setForm2Nomination(request.getParameter("form1")
						.toString());
			} else {
				bean.setForm2Nomination("");
			}
			if (request.getParameter("remarks") != null) {
				bean.setRemarks(request.getParameter("remarks").toString());
			} else {
				bean.setRemarks("");
			}
			if (request.getParameter("optionform") != null) {
				bean.setOptionForm(request.getParameter("optionform")
						.toString());
			} else {
				bean.setOptionForm("");
			}
			if (request.getParameter("region") != null) {
				bean
						.setRegion(request.getParameter("region".trim()
								.toString()));
			} else {
				bean.setRegion("");
			}
			if (request.getParameter("division") != null) {
				bean.setDivision(request.getParameter("division").toString());
			} else {
				bean.setDivision("");
			}
			if (request.getParameter("department") != null) {
				bean.setDepartment(request.getParameter("department")
						.toString());
			} else {
				bean.setDepartment("");
			}
			if (request.getParameter("paddress") != null) {
				bean.setPermanentAddress(request.getParameter("paddress")
						.toString().trim());
			} else {
				bean.setPermanentAddress("");
			}
			if (request.getParameter("taddress") != null) {
				bean.setTemporatyAddress(request.getParameter("taddress")
						.toString().trim());
			} else {
				bean.setTemporatyAddress("");
			}
			if (request.getParameter("wetherOption") != null) {
				bean.setWetherOption(request.getParameter("wetherOption")
						.toString().trim());
			} else {
				bean.setWetherOption("");
			}

			if (request.getParameter("sex") != null) {
				if (request.getParameter("sex").toString().trim()
						.equals("Male")) {
					bean.setSex("M");
				} else if (request.getParameter("sex").toString().trim()
						.equals("Female")) {
					bean.setSex("F");
				}
			} else {
				bean.setSex("");
			}
			if (request.getParameter("mstatus") != null) {
				bean.setMaritalStatus(request.getParameter("mstatus")
						.toString().trim());
			} else {
				bean.setMaritalStatus("");
			}
			if (request.getParameter("fhname") != null) {
				bean
						.setFhName(request.getParameter("fhname").toString()
								.trim());
			} else {
				bean.setFhName("");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				bean.setDateOfAnnuation(request.getParameter("dateOfAnnuation")
						.toString().trim());
			} else {
				bean.setDateOfAnnuation("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(commonUtil.getSearchPFID(request
						.getParameter("pfid").toString().trim()));
			} else {
				bean.setPensionNumber("");
			}

			log.info("pensionno " + bean.getPensionNumber());
			/* for Family Details */

			/*
			 * if (request.getParameterValues("FName") != null) {
			 * 
			 * String fname[] = request.getParameterValues("FName"); String
			 * empOldFRName[] = request .getParameterValues("empOldFRName");
			 * String fdob[] = request.getParameterValues("Fdob"); String
			 * frelation[] = request.getParameterValues("Frelation");
			 * StringBuffer familyRow = new StringBuffer();
			 * System.out.println(fname.length); for (int i = 0; i <
			 * fname.length; i++) { String fmDOB = "", fRelation = ""; String
			 * frOldName = "XXX"; if (!fname[i].equals("")) {
			 * familyRow.append(fname[i].toString() + "@");
			 * System.out.println("fdob" + fdob[i]);
			 * 
			 * if (fdob[i].equals("")) { fmDOB = "XXX"; } else {
			 * 
			 * fmDOB = fdob[i].toString(); // fmDOB =
			 * commonUtil.converDBToAppFormat(fmDOB, // "dd/MM/yyyy",
			 * "dd-MMM-yyyy"); } if (frelation[i].equals("")) { fRelation =
			 * "XXX"; } else { fRelation = frelation[i].toString(); }
			 * 
			 * if (empOldFRName[i].equals("")) { frOldName = "XXX"; } else {
			 * frOldName = empOldFRName[i].toString().trim(); }
			 * familyRow.append(fmDOB + "@"); familyRow.append(fRelation + "@");
			 * familyRow.append(frOldName); familyRow.append("***"); }
			 *  }
			 * 
			 * System.out.println("family data " + familyRow.toString());
			 * 
			 * if (familyRow.toString() != null) {
			 * bean.setFamilyRow(familyRow.toString()); } else {
			 * bean.setFamilyRow(""); }
			 *  }
			 */
			/* end of Family Details */
			/* for Nominee Details */
			/*
			 * if (request.getParameterValues("Nname") != null) { String
			 * nomineeRowID[] = request .getParameterValues("nomineerowid");
			 * String Nname[] = request.getParameterValues("Nname"); String
			 * empOldNname[] = request .getParameterValues("empOldNname");
			 * String Naddress[] = request.getParameterValues("Naddress");
			 * String Ndob[] = request.getParameterValues("Ndob"); String
			 * Nrelation[] = request.getParameterValues("Nrelation"); String
			 * guardianname[] = request .getParameterValues("guardianname");
			 * String gaddress[] = request.getParameterValues("gaddress");
			 * String totalshare[] = request.getParameterValues("totalshare");
			 * StringBuffer nomineeRow = new StringBuffer();
			 * System.out.println(Nname.length); String nAddress = "", nDob =
			 * "", nRowid = "", nRelation = "", nGuardianname = "", nTotalshare =
			 * "", gardaddressofNaminee = ""; String empOldNomineeName = ""; for
			 * (int i = 0; i < Nname.length; i++) { if (!Nname[i].equals("")) {
			 * nomineeRow.append(Nname[i].toString() + "@"); if
			 * (Naddress[i].equals("")) { nAddress = "XXX"; } else { nAddress =
			 * Naddress[i].toString(); } if (Nrelation[i].equals("")) {
			 * nRelation = "XXX"; } else { nRelation = Nrelation[i].toString(); }
			 * 
			 * if (Ndob[i].equals("")) { nDob = "XXX"; } else { nDob =
			 * Ndob[i].toString(); } if (guardianname[i].equals("")) {
			 * nGuardianname = "XXX"; } else { nGuardianname =
			 * guardianname[i].toString(); } if (gaddress[i].equals("")) {
			 * gardaddressofNaminee = "XXX"; } else { gardaddressofNaminee =
			 * gaddress[i].toString() .trim(); }
			 * 
			 * if (empOldNname[i].equals("")) { empOldNomineeName = "XXX"; }
			 * else { empOldNomineeName = empOldNname[i].toString() .trim(); }
			 * if (totalshare[i].equals("")) { nTotalshare = "XXX"; } else {
			 * nTotalshare = totalshare[i].toString(); }
			 * 
			 * if (!nomineeRowID[i].equals("")) { nRowid =
			 * nomineeRowID[i].toString(); } else { nRowid = "XXX"; }
			 * nomineeRow.append(nAddress + "@"); nomineeRow.append(nDob + "@");
			 * nomineeRow.append(nRelation + "@");
			 * nomineeRow.append(nGuardianname + "@");
			 * nomineeRow.append(gardaddressofNaminee + "@");
			 * nomineeRow.append(empOldNomineeName + "@");
			 * nomineeRow.append(nTotalshare + "@"); nomineeRow.append(nRowid);
			 * nomineeRow.append("***"); }
			 *  }
			 * 
			 * System.out.println("Nominee data " + nomineeRow.toString());
			 * 
			 * if (nomineeRow.toString() != null) {
			 * bean.setNomineeRow(nomineeRow.toString()); } else {
			 * bean.setNomineeRow(""); }
			 *  }
			 */
			String userName = (String) session.getAttribute("userid");
			String computerName = (String) session.getAttribute("computername");
			bean.setUserName(userName);
			bean.setComputerName(computerName);

			/* endof Nominee Details */

			int count = 0;
			String message = "";
			try {
				count = personalService.updatePensionMasterRevised(bean, flag);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				// TODO Auto-generated catch block
				message = e.getMessage();

			}
			String cpfacno = bean.getCpfAcNo();
			RequestDispatcher rd = null;
			log.info("=======cpfacno" + cpfacno + "flag==" + flag + "name="
					+ empName + "count" + count);
			if (count == 0) {
				if (flag.equals("false")) {
					flag = "invflag";
				}

				rd = request
						.getRequestDispatcher("./search1?method=edit&cpfacno="
								+ cpfacno + "&recordExist=" + message
								+ "&flag=" + flag + "&name=" + empName);

			} else if (flag.equals("true")
					&& editFrom.equals("uniqueNumberGen")) {
				log.info("inside uniqueNumberGen " + arrearInfo);
				rd = request
						.getRequestDispatcher("./search1?method=searchRecordsbyDobandName");

			} else if (flag.equals("true")
					&& editFrom.equals("getProcessUnprocessList")) {
				log.info("inside getProcessUnprocessList ");
				String empSerailNumber1 = "", dateofbirth = "";
				if (session.getAttribute("dateofbirth1") != null) {
					dateofbirth = (String) session.getAttribute("dateofbirth1");
				}
				if (session.getAttribute("empSerailNumber1") != null) {
					empSerailNumber1 = (String) session
							.getAttribute("empSerailNumber1");
				}

				log.info("dateofbirth" + dateofbirth + " empSerailNumber1"
						+ empSerailNumber1);

			} else {
				rd = request
						.getRequestDispatcher("./psearch?method=personalnav&strtindx="
								+ startIndex + "&total" + totalData);
			}

			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("frmForm9Report")) {
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();

			if (request.getParameter("frm_dateOfBirth") != null) {
				personalInfo.setDateOfBirth(request
						.getParameter("frm_dateOfBirth"));
			} else {
				personalInfo.setDateOfBirth("---");
			}
			if (request.getParameter("cpfacno") != null) {
				personalInfo.setCpfAccno(request.getParameter("cpfacno"));
			} else {
				personalInfo.setCpfAccno("");
			}
			if (request.getParameter("name") != null) {
				personalInfo.setEmployeeName(request.getParameter("name"));
			} else {
				personalInfo.setEmployeeName("");
			}
			if (request.getParameter("empCode") != null) {
				personalInfo.setEmployeeNumber(request.getParameter("empCode"));
			} else {
				personalInfo.setEmployeeNumber("");
			}
			if (request.getParameter("region") != null) {
				personalInfo.setRegion(request.getParameter("region"));
			} else {
				personalInfo.setRegion("");
			}
			if (request.getParameter("frm_PensionNo") != null) {
				personalInfo
						.setPensionNo(request.getParameter("frm_PensionNo"));
			} else {
				personalInfo.setPensionNo("");
			}
			RPFCForm9Bean personaList = new RPFCForm9Bean();
			personaList = personalService.form9Report(personalInfo);
			request.setAttribute("form9Report", personaList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeeRPFCForm9Report.jsp");
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
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("loadAdd")) {
			ArrayList departmentList = new ArrayList();
			ArrayList desginationList = new ArrayList();
			String rgnName = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			// ArrayList airportList1=null;
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions Length" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];

				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			String message = "";
			if (request.getAttribute("mesg") != null) {
				message = (String) request.getAttribute("mesg");
				request.setAttribute("mesg", message);
			}
			log.info("Regions Name" + map.size());
			departmentList = personalService.getDepartmentList();
			desginationList = personalService.getDesginationList();

			log.info("departmentList " + departmentList.size());
			request.setAttribute("desginationList", desginationList);
			request.setAttribute("DepartmentList", departmentList);
			request.setAttribute("regionMap", map);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeePersonalAdd.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("verifypcrpt")) {
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/VerificationofPCReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("addToPersonal")) {
			EmployeePersonalInfo personal = new EmployeePersonalInfo();
			if (request.getParameter("select_fhname") != null) {
				personal.setFhFlag(request.getParameter("select_fhname")
						.toString().trim());
			} else {
				personal.setFhFlag("");
			}

			if (request.getParameter("empName") != null) {
				personal.setEmployeeName(request.getParameter("empName")
						.toString());
			} else {
				personal.setEmployeeName("");
			}

			if (request.getParameter("select_airport") != null) {
				personal.setAirportCode(request.getParameter("select_airport")
						.toString());
			} else {
				personal.setAirportCode("");
			}
			if (request.getParameter("desegnation") != null) {
				personal.setDesignation(request.getParameter("desegnation")
						.toString());
			} else {
				personal.setDesignation("");
			}
			if (request.getParameter("cpfacno") != null) {
				personal
						.setCpfAccno(request.getParameter("cpfacno").toString());
			} else {
				personal.setCpfAccno("");
			}
			if (request.getParameter("employeeCode") != null) {
				personal.setEmployeeNumber(request.getParameter("employeeCode")
						.toString());
			} else {
				personal.setEmployeeNumber("");
			}

			if (request.getParameter("airportSerialNumber") != null) {
				personal.setAirportSerialNumber(request.getParameter(
						"airportSerialNumber").toString());
			} else {
				personal.setAirportSerialNumber("");
			}
			if (request.getParameter("emplevel") != null) {
				log.info("Personal Desgination Level"
						+ request.getParameter("emplevel").toString());
				String empDesgLevel[] = request.getParameter("emplevel").split(
						" - ");
				log.info("Emp Level" + empDesgLevel[0] + "Desgnation"
						+ empDesgLevel[1]);
				personal.setEmpDesLevel(empDesgLevel[0]);
				personal.setDesignation(empDesgLevel[1]);
			} else {
				personal.setEmpDesLevel("");
			}

			try {
				if (request.getParameter("dateofbirth") != null) {
					personal.setDateOfBirth(commonUtil.converDBToAppFormat(
							request.getParameter("dateofbirth").toString(),
							"dd/MM/yyyy", "dd-MMM-yyyy"));
				}
				if (request.getParameter("dateofjoining") != null) {
					personal.setDateOfJoining(commonUtil.converDBToAppFormat(
							request.getParameter("dateofjoining").toString(),
							"dd/MM/yyyy", "dd-MMM-yyyy"));
				} else {
					personal.setDateOfJoining("");
				}
				if (request.getParameter("dateOfAnnuation") != null) {
					personal.setDateOfAnnuation(commonUtil.converDBToAppFormat(
							request.getParameter("dateOfAnnuation").toString(),
							"dd/MM/yyyy", "dd-MMM-yyyy"));

				} else {
					personal.setDateOfAnnuation("");
				}
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (request.getParameter("reason") != null) {
				personal.setSeperationReason(request.getParameter("reason")
						.toString());
			} else {
				personal.setSeperationReason("");
			}

			if (request.getParameter("seperationDate") != null) {
				try {
					personal.setSeperationDate(commonUtil.converDBToAppFormat(
							request.getParameter("seperationDate").toString(),
							"dd/MM/yyyy", "dd-MMM-yyyy"));
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				personal.setSeperationDate("");
			}

			if (request.getParameter("wetherOption") != null) {
				personal.setWetherOption(request.getParameter(
						"wetherOption".trim()).toString());
			} else {
				personal.setWetherOption("");
			}
			if (request.getParameter("form1") != null) {
				personal.setForm2Nominee(request.getParameter("form1")
						.toString().trim());
			} else {
				personal.setForm2Nominee("");
			}

			if (request.getParameter("remarks") != null) {
				personal.setRemarks(request.getParameter("remarks").toString()
						.trim());
			} else {
				personal.setRemarks("");
			}

			if (request.getParameter("paddress") != null) {
				personal.setPerAddress(request.getParameter("paddress")
						.toString().trim());
			} else {
				personal.setPerAddress("");
			}
			if (request.getParameter("taddress") != null) {
				personal.setTempAddress(request.getParameter("taddress")
						.toString().trim());
			} else {
				personal.setTempAddress("");
			}
			if (request.getParameter("sex") != null) {
				personal.setGender(request.getParameter("sex").toString()
						.trim());
			} else {
				personal.setGender("");
			}
			if (request.getParameter("mstatus") != null) {
				personal.setMaritalStatus(request.getParameter("mstatus")
						.toString().trim());
			} else {
				personal.setMaritalStatus("");
			}
			if (request.getParameter("fhname") != null) {
				personal.setFhName(request.getParameter("fhname").toString()
						.trim());
			} else {
				personal.setFhName("");
			}
			if (request.getParameter("Other") != null) {
				personal.setOtherReason(request.getParameter("Other")
						.toString().trim());
			} else {
				personal.setOtherReason("");
			}
			if (request.getParameter("division") != null) {
				personal.setDivision(request.getParameter("division")
						.toString().trim());
			} else {
				personal.setDivision("");
			}
			if (request.getParameter("department") != null) {
				personal.setDepartment(request.getParameter("department")
						.toString().trim());
				if (personal.getDepartment().equals("NO-SELECT")) {
					personal.setDepartment("");
				}
			} else {
				personal.setDepartment("");
			}
			if (request.getParameter("emailId") != null) {
				personal.setEmailID(request.getParameter("emailId").toString()
						.trim());
			} else {
				personal.setEmailID("");
			}
			if (request.getParameter("region") != null) {
				personal.setRegion(request.getParameter("region").toString()
						.trim());
			} else {
				personal.setRegion("");
			}
			if (request.getParameter("equalShare") != null) {
				personal.setEmpNomineeSharable(request.getParameter(
						"equalShare").toString().trim());
			} else {
				personal.setEmpNomineeSharable("");
			}

			NomineeBean nomineeInfo = new NomineeBean();
			if (request.getParameterValues("FName") != null) {
				String fname[] = request.getParameterValues("FName");
				String fdob[] = request.getParameterValues("Fdob");
				String frelation[] = request.getParameterValues("Frelation");
				StringBuffer familyRow = new StringBuffer();
				String fmDOB = "", fRelation = "";
				log.info("fdob.length" + fdob.length + "fname.length"
						+ fname.length);
				for (int i = 0; i < fname.length; i++) {
					familyRow.append(fname[i].toString() + "@");
					System.out.println("fdob" + fdob[i]);
					try {
						if (fdob[i].equals("")) {
							fmDOB = "XXX";
						} else {
							fmDOB = commonUtil.converDBToAppFormat(fdob[i]
									.toString(), "dd/MM/yyyy", "dd-MMM-yyyy");
						}
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (frelation[i].equals("")) {
						fRelation = "XXX";
					} else {
						fRelation = frelation[i].toString();
					}

					familyRow.append(fmDOB + "@");
					familyRow.append(fRelation);
					familyRow.append("***");
					log.info("Family Member Name" + fname[i].toString() + "DOB"
							+ fdob[i].toString() + "Relation"
							+ fRelation.toString());
				}
				log.info("family data " + familyRow.toString());

				if (familyRow.toString() != null) {
					nomineeInfo.setFamilyDetail(familyRow.toString());
				} else {
					nomineeInfo.setFamilyDetail("");
				}

			}

			if (request.getParameterValues("Nname") != null) {
				String Nname[] = request.getParameterValues("Nname");
				String Naddress[] = request.getParameterValues("Naddress");
				String Ndob[] = request.getParameterValues("Ndob");
				String Nrelation[] = request.getParameterValues("Nrelation");
				String guardianname[] = request
						.getParameterValues("guardianname");
				String totalshare[] = request.getParameterValues("totalshare");
				System.out.println("totalshare " + totalshare.length);
				String gaddress[] = request.getParameterValues("gaddress");
				StringBuffer nomineeRow = new StringBuffer();
				System.out.println(Nname.length);
				String nAddress = "", nDob = "", nRelation = "", nGuardianname = "", nTotalshare = "", gardaddressofNaminee = "";
				for (int i = 0; i < Nname.length; i++) {
					nomineeRow.append(Nname[i].toString() + "@");
					if (Naddress[i].equals("")) {
						nAddress = "XXX";
					} else {
						nAddress = Naddress[i].toString();
					}
					if (Nrelation[i].equals("")) {
						nRelation = "XXX";
					} else {
						nRelation = Nrelation[i].toString();
					}
					try {
						if (Ndob[i].equals("")) {
							nDob = "XXX";
						} else {
							nDob = commonUtil.converDBToAppFormat(Ndob[i]
									.toString(), "dd/MM/yyyy", "dd-MMM-yyyy");
						}
					} catch (InvalidDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (guardianname[i].equals("")) {
						nGuardianname = "XXX";
					} else {
						nGuardianname = guardianname[i].toString();
					}
					if (gaddress[i].equals("")) {
						gardaddressofNaminee = "XXX";
					} else {
						gardaddressofNaminee = gaddress[i].toString();
					}

					if (totalshare[i].equals("")) {
						nTotalshare = "XXX";
					} else {
						nTotalshare = totalshare[i].toString();
					}

					nomineeRow.append(nAddress + "@");
					nomineeRow.append(nDob + "@");
					nomineeRow.append(nRelation + "@");
					nomineeRow.append(nGuardianname + "@");
					nomineeRow.append(gardaddressofNaminee + "@");
					nomineeRow.append(nTotalshare);
					nomineeRow.append("***");

				}
				log.info("Nominee data " + nomineeRow.toString());
				if (nomineeRow.toString() != null) {
					nomineeInfo.setNomineeDetail(nomineeRow.toString());
				} else {
					nomineeInfo.setNomineeDetail("");
				}
			}

			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			String uniqueID = "";
			try {
				uniqueID = personalService.addPersonalInfo(personal,
						nomineeInfo, userName, ipAddress);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String message = "";
			if (!uniqueID.equals("")) {
				message = "Employee PF ID Credentials " + uniqueID
						+ " added Sucessfully";
			} else {
				message = "Fail! To add Employee PF ID Credentials";
			}

			request.setAttribute("mesg", message);
			RequestDispatcher rd = request
					.getRequestDispatcher("./psearch?method=loadAdd");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("chkEmpPersonal")) {
			ArrayList checkList = new ArrayList();
			EmployeePersonalInfo personal = new EmployeePersonalInfo();
			if (request.getParameter("frmEmpName") != null) {
				personal.setEmployeeName(request.getParameter("frmEmpName")
						.toString());
			} else {
				personal.setEmployeeName("");
			}
			try {
				if (request.getParameter("frmDob") != null) {

					personal.setDateOfBirth(commonUtil.converDBToAppFormat(
							request.getParameter("frmDob").toString(),
							"dd/MM/yyyy", "dd-MMM-yyyy"));
				} else {
					personal.setDateOfBirth("");
				}
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			log.info("Employeename" + request.getParameter("frmEmpName")
					+ "DOB" + request.getParameter("frmDob"));
			boolean empflag = false, dobflag = false;
			String reqMessage = "";
			if (request.getParameter("empflag") != null) {
				if (request.getParameter("empflag").equals("true")) {
					empflag = true;
					reqMessage = "Employees list with same Employee Name";
				}
			}
			if (request.getParameter("dobflag") != null) {
				if (request.getParameter("dobflag").equals("true")) {
					dobflag = true;
					reqMessage = "Employees list with same Date of Birth";
				}
			}
			request.setAttribute("title", reqMessage);

			checkList = personalService.checkPersonalInfo(personal, empflag,
					dobflag);
			request.setAttribute("personalChkList", checkList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/CheckEmployeePersonalAddInfo.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("frmForm2Report")) {
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();

			if (request.getParameter("frm_dateOfBirth") != null) {
				personalInfo.setDateOfBirth(request
						.getParameter("frm_dateOfBirth"));
			} else {
				personalInfo.setDateOfBirth("---");
			}
			if (request.getParameter("cpfacno") != null) {
				personalInfo.setCpfAccno(request.getParameter("cpfacno"));
			} else {
				personalInfo.setCpfAccno("");
			}
			if (request.getParameter("name") != null) {
				personalInfo.setEmployeeName(request.getParameter("name"));
			} else {
				personalInfo.setEmployeeName("");
			}
			if (request.getParameter("empCode") != null) {
				personalInfo.setEmployeeNumber(request.getParameter("empCode"));
			} else {
				personalInfo.setEmployeeNumber("");
			}
			if (request.getParameter("region") != null) {
				personalInfo.setRegion(request.getParameter("region"));
			} else {
				personalInfo.setRegion("");
			}
			if (request.getParameter("frm_PensionNo") != null) {
				personalInfo
						.setPensionNo(request.getParameter("frm_PensionNo"));
			} else {
				personalInfo.setPensionNo("");
			}
			NomineeForm2Info form2Report = new NomineeForm2Info();
			form2Report = personalService.form2Report(personalInfo);
			request.setAttribute("form2Report", form2Report);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/personal/EmployeeForm2Report.jsp");
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
			getSearchInfo = fs.financeDataSearch(empSerach, getSearchInfo, 100,
					empNameCheck, empSerach.getReportType(), empSerach
							.getPfidfrom());
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
							.getRequestDispatcher("./PensionView/VerificationofPCReport.jsp");
				}
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financePfidUpdate")) {
			String pfid = "", empNameCheck = "";
			EmpMasterBean empSerach = new EmpMasterBean();

			if (request.getParameter("employeeName") != null) {
				empSerach.setEmpName(request.getParameter("employeeName")
						.toString().trim());
			}
			if (request.getParameter("region") != null) {
				empSerach.setRegion(request.getParameter("region").toString()
						.trim());
			}
			if (request.getParameter("cpfacno") != null) {
				empSerach.setCpfAcNo(request.getParameter("cpfacno").toString()
						.trim());
			}
			if (request.getParameter("empCode") != null) {
				empSerach.setEmpNumber(request.getParameter("empCode")
						.toString().trim());
			}
			if (request.getParameter("pfid") != null) {
				pfid = request.getParameter("pfid").toString().trim();
			}
			if (request.getParameter("empNameCheak") != null) {
				empNameCheck = request.getParameter("empNameCheak").toString()
						.trim();
			}
			if (request.getParameter("unmappedFlag") != null) {
				empSerach.setUnmappedFlag(request.getParameter("unmappedFlag")
						.toString().trim());
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
			fs.financePfidUpdate(empSerach.getCpfAcNo(), empSerach
					.getEmpNumber(), empSerach.getRegion(), pfid);
			SearchInfo getSearchInfo = new SearchInfo();

			getSearchInfo = fs.financeDataSearch(empSerach, getSearchInfo, 100,
					empNameCheck, empSerach.getReportType(), "");
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");
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
				getSearchInfo = fs.financeDataSearch(empSerach, getSearchInfo,
						100, empNameCheck, empSerach.getReportType(), empSerach
								.getPfidfrom());

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
						.getRequestDispatcher("./PensionView/VerificationofPCReport.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"updatePFIDListWthoutTrnFlag")) {
			String region = "", airportcode = "";
			int pereachpage = 0;
			ArrayList rangeList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}

			log.info("==getPFIDListWthoutTrnFlag=region=" + region
					+ "airportcode===" + airportcode);
			ArrayList ServiceType = null;
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			int totalSize = 0;

			if (bundle.getString("common.pension.pagesize") != null) {
				totalSize = Integer.parseInt(bundle
						.getString("common.pension.pagesize"));
			} else {
				totalSize = 100;
			}
			totalSize = 1000;
			rangeList = commonDAO.navgtionPFIDsList(region, airportcode,
					Integer.toString(totalSize), "", "false");
			log.info("getPFIDListWthoutTrnFlag::Total List Size "
					+ rangeList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String rangeString = "";
			for (int i = 0; i < rangeList.size(); i++) {
				rangeString = (String) rangeList.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				sb.append(rangeString);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method").equals("updateMappingFlag")) {
			String pfid = "", cpfaccno = "", region = "";
			log.info("delete items  "
					+ request.getParameterValues("mappingflag"));
			String[] unmappedRecords = {};
			String[] deleteTransactions = {};
			if (request.getParameterValues("mappingflag") != null) {
				unmappedRecords = (String[]) request
						.getParameterValues("mappingflag");
			}

			FinancialReportService finReportService = new FinancialReportService();
			for (int i = 0; i < unmappedRecords.length; i++) {
				log.info(unmappedRecords[i]);
				deleteTransactions = unmappedRecords[i].split(",");
				for (int j = 0; j < deleteTransactions.length; j++) {
					pfid = deleteTransactions[0].substring(1,
							deleteTransactions[0].length() - 1);
					cpfaccno = deleteTransactions[1].substring(1,
							deleteTransactions[1].length() - 1);
					region = deleteTransactions[2].substring(1,
							deleteTransactions[2].length() - 1);
					fs.updateMappingFlag(pfid, cpfaccno, region);
					finReportService.preProcessAdjOB(pfid);
				}

			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("formsDisable")) {

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
			FinancialReportService finReportService = new FinancialReportService();
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			fs.formsDisable(pensionno, formsstatus, pcreportstatus,
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
		} else if (request.getParameter("method").equals("mappingUpdate")) {

			String pfid = "", cpfaccno = "", region = "";
			log.info("Mapped items  "
					+ request.getParameterValues("mappingUpdate"));
			String[] unmappedRecords = {};
			String[] updateTransactions = {};
			if (request.getParameterValues("mappingUpdate") != null) {
				unmappedRecords = (String[]) request
						.getParameterValues("mappingUpdate");
			}
			FinancialReportService finReportService = new FinancialReportService();
			String pfid1 = "";
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");
			for (int i = 0; i < unmappedRecords.length; i++) {
				updateTransactions = unmappedRecords[i].split(",");
				for (int j = 0; j < updateTransactions.length; j++) {

					updateTransactions = unmappedRecords[i].split(",");

					pfid = pfid1;
					cpfaccno = updateTransactions[1].substring(1,
							updateTransactions[1].length() - 1);
					region = updateTransactions[2].substring(1,
							updateTransactions[2].length() - 1);
					String pensionnotextbox = updateTransactions[3].substring(
							1, updateTransactions[3].length() - 1);
					pfid = request.getParameter(pensionnotextbox);
					fs.updateMapping(pfid, cpfaccno, region, userName,
							ipAddress);
					finReportService.preProcessAdjOB(pfid);
				}
			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"form10DsearchPersonal")) {
			ArrayList regionList = new ArrayList();
			String rgnName = "", sortingColumn = "", page = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				empBean.setAirportCode(request.getParameter("airPortCode")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			if (request.getParameter("pensionNO") != null) {
				empBean.setPensionNo(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				empBean.setPensionNo("");
			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page").toString();
			}
			if (request.getParameter("frm_dateOfBirth") != null
					|| request.getParameter("frm_dateOfBirth") != "") {
				empBean.setDateOfBirth(request.getParameter("frm_dateOfBirth")
						.toString().trim());
			} else {
				empBean.setDateOfBirth("");
			}
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAccno(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAccno("");
			}
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}

			// code added for empname blanks search
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {

				searchBean = personalService.searchPersonalMaster(empBean,
						getSearchInfo, flag, gridLength, sortingColumn, page);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/form10D/Form-10DSearch.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("form10Dpersonalnav")) {
			String navgationBtn = "", rgnName = "", sortingColumn = "", page = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();

			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = (String) session.getAttribute("navButton");
				searchListBean.setNavButton(navgationBtn);
			}

			if (request.getParameter("strtindx") != null) {

				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				System.out.println("strtIndex" + strtIndex);
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page").toString();
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				searchListBean.setTotalRecords(totalRecords);
			}
			if (session.getAttribute("getSearchBean1") != null) {
				personalInfo = (EmployeePersonalInfo) session
						.getAttribute("getSearchBean1");
			}
			try {
				navSearchBean = personalService.navigationPersonalData(
						personalInfo, searchListBean, gridLength,
						sortingColumn, page);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", personalInfo);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/form10D/Form-10DSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("form10DSubmitForm")) {
			String empName = "", empCode = "", region = "";
			String editFrom = "";
			String empSerailNumber1 = "", dateofbirth1 = "", dob1 = "", view = "";
			String employeeSearchName = "", arrearInfo = "", cpfacno = "";
			String pfid = "";
			pfid = request.getParameter("pfid").toString();
			cpfacno = request.getParameter("cpfacno").trim();
			String startIndex = "", totalData = "";
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			if (request.getParameter("view") != null) {
				view = request.getParameter("view");
			}
			if (request.getParameter("empSerailNumber") != null) {
				empSerailNumber1 = request.getParameter("empSerailNumber");
				session.setAttribute("empSerailNumber1", empSerailNumber1);
			}
			if (request.getParameter("dateofbirth") != null) {
				dateofbirth1 = request.getParameter("dateofbirth");
				session.setAttribute("dateofbirth1", dateofbirth1);
			}
			if (request.getParameter("empName") != null) {
				employeeSearchName = request.getParameter("empName");
				session.setAttribute("employeeSearchName", employeeSearchName);
			}
			if (request.getParameter("dob1") != null) {
				dob1 = request.getParameter("dob1");
				session.setAttribute("dob1", dob1);
			}
			if (session.getAttribute("empBean1") != null) {
				log.info("session " + session.getAttribute("empBean1"));
			}
			if (request.getParameter("name") != null) {
				empName = request.getParameter("name");
			}
			if (request.getParameter("empCode") != null) {
				empCode = request.getParameter("empCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region").trim();
			}
			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}
			boolean flag;
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;

			} else {
				flag = true;
			}
			if (request.getParameter("recordExist") != null) {
				request.setAttribute("recordExist", request.getParameter(
						"recordExist").toString());
			}
			ArrayList airportList = new ArrayList();
			ArrayList departmentList = new ArrayList();
			EmpMasterBean editBean = new EmpMasterBean();
			EmployeeAdlPensionInfo adlPensionInfo = new EmployeeAdlPensionInfo();
			try {
				editBean = personalService.empForm10DPersonalEdit(cpfacno,
						empName, region.trim(), pfid);
				adlPensionInfo = personalService.getPensionForm10DDtls(pfid);
				airportList = commonUtil.getAirports(region);
				departmentList = ps.getDepartmentList();
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			log.info("Search Servelt::====Submision Date"
					+ adlPensionInfo.getForm10DSubmsionDate() + "Sanction Date"
					+ adlPensionInfo.getSanctionOrderDate());
			request.setAttribute("EditBean", editBean);
			request.setAttribute("adlPensionInfo", adlPensionInfo);
			request.setAttribute("AirportList", airportList);
			request.setAttribute("DepartmentList", departmentList);
			request.setAttribute("flag", Boolean.toString(flag));
			request.setAttribute("startIndex", startIndex);
			request.setAttribute("TotalData", totalData);
			request.setAttribute("editFrom", editFrom);
			request.setAttribute("ArrearInfo", arrearInfo);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/form10D/Form10DSubmissionForm.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("frmForm10DReport")) {
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
			ArrayList form10DList = new ArrayList();
			if (request.getParameter("frm_dateOfBirth") != null) {
				personalInfo.setDateOfBirth(request
						.getParameter("frm_dateOfBirth"));
			} else {
				personalInfo.setDateOfBirth("---");
			}
			if (request.getParameter("cpfacno") != null) {
				personalInfo.setCpfAccno(request.getParameter("cpfacno"));
			} else {
				personalInfo.setCpfAccno("");
			}
			if (request.getParameter("name") != null) {
				personalInfo.setEmployeeName(request.getParameter("name"));
			} else {
				personalInfo.setEmployeeName("");
			}
			if (request.getParameter("empCode") != null) {
				personalInfo.setEmployeeNumber(request.getParameter("empCode"));
			} else {
				personalInfo.setEmployeeNumber("");
			}
			if (request.getParameter("region") != null) {
				personalInfo.setRegion(request.getParameter("region"));
			} else {
				personalInfo.setRegion("");
			}
			if (request.getParameter("frm_PensionNo") != null) {
				personalInfo
						.setPensionNo(request.getParameter("frm_PensionNo"));
			} else {
				personalInfo.setPensionNo("");
			}

			form10DList = personalService.form10DInfo(personalInfo
					.getPensionNo());
			request.setAttribute("form10DList", form10DList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/form10D/Form10DMainPageReport.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("form10dUpdate")) {
			log.info("SearchServlet : form1dUpdate");
			EmpMasterBean bean = new EmpMasterBean();
			EmployeeAdlPensionInfo addtionalPensionInfo = new EmployeeAdlPensionInfo();
			String flag = "", empName = "";
			String startIndex = "", totalData = "";
			String editFrom = "";
			String arrearInfo = "";

			if (request.getParameter("setEmpSerialNo") != null) {
				bean.setEmpSerialNo(request.getParameter("setEmpSerialNo")
						.trim().toString());
			}
			if (request.getParameter("select_fhname") != null) {
				bean.setFhFlag(request.getParameter("select_fhname").trim()
						.toString());
			}

			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");

			// New Patameters added while Changing employee information from one
			// region to other
			if (request.getParameter("region") != null) {
				bean
						.setRegion(request.getParameter("region").trim()
								.toString());
			} else {
				bean.setRegion("");
			}
			if (request.getParameter("perPhoneNumber") != null) {
				bean.setPhoneNumber(request.getParameter("perPhoneNumber"
						.trim().toString()));
			} else {
				bean.setPhoneNumber("");
			}

			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}
			if (request.getParameter("flagData") != null) {
				flag = request.getParameter("flagData");
			} else {
				flag = "true";
			}
			if (request.getParameter("empName") != null) {
				bean.setEmpName(request.getParameter("empName").trim()
						.toString());
			} else {
				bean.setEmpName("");
			}

			if (request.getParameter("employeeCode") != null) {
				bean.setEmpNumber(request.getParameter("employeeCode").trim()
						.toString());
			} else {
				bean.setEmpNumber("");
			}

			if (request.getParameter("airPortCode") != null) {
				bean.setStation(request.getParameter("airPortCode").toString());
			} else {
				bean.setStation("");
			}
			if (request.getParameter("desegnation") != null) {
				bean.setDesegnation(request.getParameter("desegnation")
						.toString());
			} else {
				bean.setDesegnation("");
			}
			log.info("old cpfacno " + request.getParameter("cpfacno"));

			if (request.getParameter("cpfacno") != null) {
				bean.setCpfAcNo(request.getParameter("cpfacno").toString());
			} else {
				bean.setCpfAcNo("");
			}
			log.info("new cpfacno " + request.getParameter("cpfacnoNew"));
			if (request.getParameter("cpfacnoNew") != null) {
				bean.setNewCpfAcNo(request.getParameter("cpfacnoNew")
						.toString());
			} else {
				bean.setNewCpfAcNo("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPfid(request.getParameter("pfid").toString());
			} else {
				bean.setPfid("");
			}
			if (request.getParameter("employeeCode") != null) {
				bean.setEmpNumber(request.getParameter("employeeCode").trim()
						.toString());
			} else {
				bean.setEmpNumber("");
			}
			if (request.getParameter("airportSerialNumber") != null) {
				bean.setAirportSerialNumber(request.getParameter(
						"airportSerialNumber").trim().toString());
			} else {
				bean.setAirportSerialNumber("");
			}

			if (request.getParameter("dateofbirth") != null) {
				bean.setDateofBirth(request.getParameter("dateofbirth"));
			} else {
				bean.setDateofBirth("");
			}

			if (request.getParameter("dateofjoining") != null) {
				bean.setDateofJoining(request.getParameter("dateofjoining")
						.toString());
			} else {
				bean.setDateofJoining("");
			}
			if (request.getParameter("reason") != null) {
				bean.setSeperationReason(request.getParameter("reason")
						.toString());
			} else {
				bean.setSeperationReason("");
			}
			if (request.getParameter("Other") != null) {
				bean.setOtherReason(request.getParameter("Other").toString());
			} else {
				bean.setOtherReason("");
			}

			if (request.getParameter("seperationDate") != null) {
				bean.setDateofSeperationDate(request.getParameter(
						"seperationDate").toString());
			} else {
				bean.setSeperationReason("");
			}

			if (request.getParameter("emailId") != null) {
				bean.setEmailId(request.getParameter("emailId").toString());
			} else {
				bean.setEmailId("");
			}

			if (request.getParameter("region") != null) {
				bean
						.setRegion(request.getParameter("region".trim()
								.toString()));
			} else {
				bean.setRegion("");
			}

			if (request.getParameter("paddress") != null) {
				bean.setPermanentAddress(request.getParameter("paddress")
						.toString().trim());
			} else {
				bean.setPermanentAddress("");
			}
			if (request.getParameter("taddress") != null) {
				bean.setTemporatyAddress(request.getParameter("taddress")
						.toString().trim());
			} else {
				bean.setTemporatyAddress("");
			}

			if (request.getParameter("wetherOption") != null) {
				bean.setWetherOption(request.getParameter("wetherOption")
						.toString().trim());
			} else {
				bean.setWetherOption("");
			}
			String heightInFeet = "", heightInInches = "";
			if (request.getParameter("height_feet") != null) {
				heightInFeet = request.getParameter("height_feet");
			} else {
				heightInFeet = "";
			}
			if (request.getParameter("height_inches") != null) {
				heightInInches = request.getParameter("height_inches");
			} else {
				heightInInches = "";
			}
			bean.setHeightWithInches(heightInFeet + "." + heightInInches);
			if (request.getParameter("nationality") != null) {
				bean.setNationality(request.getParameter("nationality")
						.toString().trim());
			} else {
				bean.setNationality("");
			}
			if (request.getParameter("currentPostingStation") != null) {
				addtionalPensionInfo.setCurrentPostingStation(request
						.getParameter("currentPostingStation").toString()
						.trim());
			} else {
				addtionalPensionInfo.setCurrentPostingStation("");
			}

			if (request.getParameter("form10DSubmitDate") != null) {
				try {
					addtionalPensionInfo.setForm10DSubmsionDate(commonUtil
							.converDBToAppFormat(request.getParameter(
									"form10DSubmitDate").toString().trim(),
									"dd/MM/yyyy", "dd-MMM-yyyy"));
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				addtionalPensionInfo.setForm10DSubmsionDate("");
			}
			if (request.getParameter("rpfcStation") != null) {
				addtionalPensionInfo.setForm10DRpfcStation(request
						.getParameter("rpfcStation").toString().trim());
			} else {
				addtionalPensionInfo.setForm10DRpfcStation("");
			}

			if (request.getParameter("pposanctionorderno") != null) {
				addtionalPensionInfo.setSanctionOrderNo(request.getParameter(
						"pposanctionorderno").toString().trim());
			} else {
				addtionalPensionInfo.setSanctionOrderNo("");
			}

			if (request.getParameter("pposanctionorderdate") != null) {
				try {
					addtionalPensionInfo.setSanctionOrderDate(commonUtil
							.converDBToAppFormat(request.getParameter(
									"pposanctionorderdate").toString().trim(),
									"dd/MM/yyyy", "dd-MMM-yyyy"));
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				addtionalPensionInfo.setSanctionOrderDate("");
			}

			if (request.getParameter("sex") != null) {
				bean.setSex(request.getParameter("sex").toString().trim());
			} else {
				bean.setSex("");
			}
			if (request.getParameter("mstatus") != null) {
				bean.setMaritalStatus(request.getParameter("mstatus")
						.toString().trim());
			} else {
				bean.setMaritalStatus("");
			}
			if (request.getParameter("fhname") != null) {
				bean
						.setFhName(request.getParameter("fhname").toString()
								.trim());
			} else {
				bean.setFhName("");
			}
			if (request.getParameter("select_fhname") != null) {
				bean
						.setFhFlag(request.getParameter("fhname").toString()
								.trim());
			} else {
				bean.setFhFlag("");
			}
			if (request.getParameter("quantum_option") != null) {
				addtionalPensionInfo.setQuantum1By3Option(request.getParameter(
						"quantum_option").toString().trim());
			} else {
				addtionalPensionInfo.setQuantum1By3Option("");
			}

			if (request.getParameter("quantumamount") != null) {
				addtionalPensionInfo.setQuantum1By3Amount(request.getParameter(
						"quantumamount").toString().trim());
			} else {
				addtionalPensionInfo.setQuantum1By3Amount("");
			}
			if (request.getParameter("select_return_captial") != null) {
				addtionalPensionInfo.setOptionReturnCaptial(request
						.getParameter("select_return_captial").toString()
						.trim());
			} else {
				addtionalPensionInfo.setOptionReturnCaptial("");
			}
			if (request.getParameter("deathofmember_date") != null) {
				addtionalPensionInfo.setMemberDeathDate(request.getParameter(
						"deathofmember_date").toString().trim());
			} else {
				addtionalPensionInfo.setMemberDeathDate("");
			}
			if (request.getParameter("select_saving_account") != null) {
				addtionalPensionInfo.setPaymentInfoType(request.getParameter(
						"select_saving_account").toString().trim());
			} else {
				addtionalPensionInfo.setPaymentInfoType("");
			}

			if (request.getParameter("savingbankingname") != null) {
				addtionalPensionInfo.setNameofPaymentBranch(request
						.getParameter("savingbankingname").toString().trim());
			} else {
				addtionalPensionInfo.setNameofPaymentBranch("");
			}
			if (request.getParameter("savingaccpostaladdr") != null) {
				addtionalPensionInfo.setAddressofPayementBranch(request
						.getParameter("savingaccpostaladdr").toString().trim());
			} else {
				addtionalPensionInfo.setAddressofPayementBranch("");
			}
			if (request.getParameter("savingbankpincode") != null) {
				addtionalPensionInfo.setPaymentBranchPincode(request
						.getParameter("savingbankpincode").toString().trim());
			} else {
				addtionalPensionInfo.setPaymentBranchPincode("");
			}
			if (request.getParameter("claimRefNominee") != null) {
				addtionalPensionInfo.setClaimNomineeRefName(request
						.getParameter("claimRefNominee").toString().trim());
			} else {
				addtionalPensionInfo.setClaimNomineeRefName("");
			}
			if (request.getParameter("claimRefNomineeRel") != null) {
				addtionalPensionInfo.setClaimNomineeRefRelation(request
						.getParameter("claimRefNomineeRel").toString().trim());
			} else {
				addtionalPensionInfo.setClaimNomineeRefRelation("");
			}
			if (request.getParameter("scheme_cert_rec") != null) {
				addtionalPensionInfo.setSchemeCertificateRecEncl(request
						.getParameter("scheme_cert_rec").toString().trim());
			} else {
				addtionalPensionInfo.setSchemeCertificateRecEncl("");
			}
			if (request.getParameter("possession_member") != null) {
				addtionalPensionInfo.setPossesionMember(request.getParameter(
						"possession_member").toString().trim());
			} else {
				addtionalPensionInfo.setPossesionMember("");
			}
			if (request.getParameter("nomineePostalAddr") != null) {
				addtionalPensionInfo.setNomineePostalAddrss(request
						.getParameter("nomineePostalAddr").toString().trim());
			} else {
				addtionalPensionInfo.setNomineePostalAddrss("");
			}
			if (request.getParameter("nomineePostalPincode") != null) {
				addtionalPensionInfo.setNomineePincode(request.getParameter(
						"nomineePostalPincode").toString().trim());
			} else {
				addtionalPensionInfo.setNomineePincode("");
			}

			if (request.getParameter("select_ppo_issue_by") != null) {
				addtionalPensionInfo.setPponoIssuedBy(request.getParameter(
						"select_ppo_issue_by").toString().trim());
			} else {
				addtionalPensionInfo.setPponoIssuedBy("");
			}
			if (request.getParameter("select_pension_drwn_1995") != null) {
				addtionalPensionInfo.setPensionDrwnFrom1995(request
						.getParameter("select_pension_drwn_1995").toString()
						.trim());
			} else {
				addtionalPensionInfo.setPensionDrwnFrom1995("");
			}

			if (request.getParameter("select_pension_drwn_1995") != null) {
				addtionalPensionInfo.setPensionDrwnFrom1995(request
						.getParameter("select_pension_drwn_1995").toString()
						.trim());
			} else {
				addtionalPensionInfo.setPensionDrwnFrom1995("");
			}

			if (request.getParameter("early_Pension_flag") != null) {
				addtionalPensionInfo.setEarlyPensionTaken(request.getParameter(
						"early_Pension_flag").toString().trim());
			} else {
				addtionalPensionInfo.setEarlyPensionTaken("N");
			}

			if (request.getParameter("earlypension_date") != null) {
				addtionalPensionInfo.setEarlyPensionDate(request.getParameter(
						"earlypension_date").toString().trim());
			} else {
				addtionalPensionInfo.setEarlyPensionDate("");
			}
			if (request.getParameter("earlypension_form10Ddate") != null) {
				addtionalPensionInfo.setEpForm10DSubDate(request.getParameter(
						"earlypension_form10Ddate").toString().trim());
			} else {
				addtionalPensionInfo.setEpForm10DSubDate("");
			}

			/* for Nominee Details */
			if (request.getParameterValues("Nname") != null) {
				String nomineeRowID[] = request
						.getParameterValues("nomineerowid");
				String Nname[] = request.getParameterValues("Nname");
				String empOldNname[] = request
						.getParameterValues("empOldNname");
				String Naddress[] = request.getParameterValues("Naddress");
				String Ndob[] = request.getParameterValues("Ndob");
				String Nrelation[] = request.getParameterValues("Nrelation");
				String guardianname[] = request
						.getParameterValues("guardianname");
				String gaddress[] = request.getParameterValues("gaddress");
				String savingaccount[] = request
						.getParameterValues("saving_acc_no");
				String returnCaptial[] = request
						.getParameterValues("option_flag");
				StringBuffer nomineeRow = new StringBuffer();
				log.info("Nominee Length" + Nname.length + "Return Of Captial"
						+ returnCaptial.length + "Saving Account"
						+ savingaccount.length);
				String nAddress = "", nDob = "", nRowid = "", nRelation = "", nGuardianname = "", nAccountNo = "", gardaddressofNaminee = "";
				String returnCaptialFlag = "";
				String empOldNomineeName = "";
				boolean returnCaptialLenFlag = false;
				for (int i = 0; i < Nname.length; i++) {

					if (i == returnCaptial.length) {
						returnCaptialLenFlag = true;
					}
					if (!Nname[i].equals("")) {
						nomineeRow.append(Nname[i].toString() + "@");
						if (Naddress[i].equals("")) {
							nAddress = "XXX";
						} else {
							nAddress = Naddress[i].toString();
						}
						if (Nrelation[i].equals("")) {
							nRelation = "XXX";
						} else {
							nRelation = Nrelation[i].toString();
						}

						if (Ndob[i].equals("")) {
							nDob = "XXX";
						} else {
							nDob = Ndob[i].toString();
						}
						if (guardianname[i].equals("")) {
							nGuardianname = "XXX";
						} else {
							nGuardianname = guardianname[i].toString();
						}
						if (gaddress[i].equals("")) {
							gardaddressofNaminee = "XXX";
						} else {
							gardaddressofNaminee = gaddress[i].toString()
									.trim();
						}

						if (empOldNname[i].equals("")) {
							empOldNomineeName = "XXX";
						} else {
							empOldNomineeName = empOldNname[i].toString()
									.trim();
						}
						if (returnCaptialLenFlag == false) {
							if (!"".equals(returnCaptial[i])) {
								returnCaptialFlag = returnCaptial[i].toString();
							} else {
								returnCaptialFlag = "XXX";

							}
						} else {
							returnCaptialFlag = "XXX";
						}

						if (!"".equals(savingaccount[i])) {
							nAccountNo = savingaccount[i].toString();
						} else {
							nAccountNo = "XXX";

						}

						if (!nomineeRowID[i].equals("")) {
							nRowid = nomineeRowID[i].toString();
						} else {
							nRowid = "XXX";
						}
						nomineeRow.append(nAddress + "@");
						nomineeRow.append(nDob + "@");
						nomineeRow.append(nRelation + "@");
						nomineeRow.append(nGuardianname + "@");
						nomineeRow.append(gardaddressofNaminee + "@");
						nomineeRow.append(empOldNomineeName + "@");
						nomineeRow.append(returnCaptialFlag + "@");
						nomineeRow.append(nAccountNo + "@");
						nomineeRow.append(nRowid);
						nomineeRow.append("***");
						log.info("Nominee" + nomineeRow);
					}

				}
				String schmeInfo = "";
				if (request.getParameter("frm_schemeinfo") != null) {
					schmeInfo = request.getParameter("frm_schemeinfo");
					ArrayList schemeList = new ArrayList();
					if (!schmeInfo.equals("")) {
						String[] extractSchmeInfo = schmeInfo.split(":");
						log.info("SearchServelt::form10dUpdate"
								+ extractSchmeInfo.length + schmeInfo);
						if (extractSchmeInfo.length != 0) {

							for (int schme = 0; schme < extractSchmeInfo.length; schme++) {

								schemeList.add(extractSchmeInfo[schme]);
							}
							bean.setSchemeList(schemeList);
						}
					} else {
						bean.setSchemeList(schemeList);
					}

				}
				if (request.getParameter("frm_documentinfo") != null) {
					addtionalPensionInfo.setDocumentInfo(request
							.getParameter("frm_documentinfo"));
				} else {
					addtionalPensionInfo.setDocumentInfo("");
				}

				log.info("form10dUpdate::Nominee data " + nomineeRow.toString()
						+ "schmeInfo data " + schmeInfo + "DocumentInfo"
						+ request.getParameter("frm_documentinfo"));

				if (nomineeRow.toString() != null) {
					bean.setNomineeRow(nomineeRow.toString());
				} else {
					bean.setNomineeRow("");
				}

			}
			String userName = (String) session.getAttribute("userid");
			String computerName = (String) session.getAttribute("computername");
			bean.setUserName(userName);
			bean.setComputerName(computerName);

			int count = 0;

			try {
				count = personalService.updateForm10DInfo(bean,
						addtionalPensionInfo);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				// TODO Auto-generated catch block

			}
			String cpfacno = bean.getCpfAcNo();

			RequestDispatcher rd = null;
			log.info("form10dUpdate::PensionNo" + bean.getEmpSerialNo()
					+ "flag==" + flag + "name=" + empName + "count" + count);
			rd = request
					.getRequestDispatcher("./psearch?method=form10Dpersonalnav&strtindx="
							+ startIndex
							+ "&total"
							+ totalData
							+ "&page=form10D");

			rd.forward(request, response);
		} else if (request.getParameter("method").equals("form7Indexreport")) {
			String region = "", year = "";
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
			FinancialReportService finReportService = new FinancialReportService();
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
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getPFCardAirports")) {
			String region = "", stationName = "";
			region = request.getParameter("region");
			ArrayList ServiceType = null;

			if (session.getAttribute("station") != null) {
				stationName = (String) session.getAttribute("station");
			}
			if (!stationName.equals("")) {
				ServiceType = new ArrayList();
				EmpMasterBean masterBean = new EmpMasterBean();
				masterBean.setStation(stationName);
				ServiceType.add(masterBean);
			} else {
				ServiceType = commonDAO.getAirportsByBulkPFIDSTbl(region);
			}
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

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getPFIDBulkPrintingList")) {
			String region = "", monthYear = "", airportcode = "", lastmonthFlag = "", month = "", fromYear = "", toYear = "", year = "";
			int totalSize = 0;
			ArrayList rangeList = new ArrayList();
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}

			if (request.getParameter("frm_ltstmonthflag") != null) {
				lastmonthFlag = request.getParameter("frm_ltstmonthflag");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			} else {
				year = "2008";
			}
			if (request.getParameter("frm_pagesize") != null) {
				totalSize = Integer.parseInt(request
						.getParameter("frm_pagesize"));
			} else {
				totalSize = 100;
			}
			if (year.indexOf("-") != -1) {
				String[] sltedYear = year.split("-");
				if (sltedYear.length == 2) {
					fromYear = sltedYear[0];
					toYear = Integer.toString(Integer.parseInt(fromYear) + 1);
				}
			} else {
				fromYear = year;
				toYear = Integer.toString(Integer.parseInt(fromYear) + 1);
			}
			String finyear = fromYear + "-" + toYear;
			/*
			 * String[] sltedYear = year.split("-"); if (sltedYear.length == 2) {
			 * fromYear = sltedYear[0]; toYear = sltedYear[1]; String
			 * prefixFrmYear = fromYear.substring(0, 2); if
			 * (prefixFrmYear.equals("19") &&
			 * Integer.parseInt(fromYear.substring(0, 2)) -
			 * Integer.parseInt(toYear) == 1) { toYear = prefixFrmYear + toYear; }
			 * else { toYear = "20" + toYear; }
			 * log.info("==getPFIDListWthoutTrnFlag=region=" + region +
			 * "airportcode===" + airportcode + "lastmonthFlag" + lastmonthFlag +
			 * "month" + month + "year" + year);
			 * log.info("==getPFIDListWthoutTrnFlag=fromYear=" + fromYear +
			 * "toYear===" + toYear);
			 *  } else { fromYear = sltedYear[0]; toYear =
			 * Integer.toString(Integer.parseInt(fromYear) + 1); } if
			 * (!month.equals("NO-SELECT")) { if (Integer.parseInt(month) >= 4 &&
			 * Integer.parseInt(month) <= 12) { monthYear = "-" + month + "-" +
			 * fromYear; } else if (Integer.parseInt(month) >= 1 &&
			 * Integer.parseInt(month) <= 3) { monthYear = "-" + month + "-" +
			 * toYear; } } else { monthYear =
			 * commonDAO.getLatestTrnsDate(region); }
			 */
			log.info("==getPFIDListWthoutTrnFlag=fromYear=" + fromYear
					+ "toYear===" + toYear);
			ArrayList ServiceType = null;
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");
			rangeList = commonDAO.bulkNavgtionPFIDsList(region, airportcode,
					Integer.toString(totalSize), monthYear, lastmonthFlag,
					finyear);

			log.info("getPFIDListWthoutTrnFlag::Total List Size "
					+ rangeList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String rangeString = "";
			for (int i = 0; i < rangeList.size(); i++) {
				rangeString = (String) rangeList.get(i);
				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				sb.append(rangeString);
				sb.append("</airPortName>");
				sb.append("</ServiceType>");
			}
			sb.append("<PARAMSTR>");
			sb.append("<PARAM1>");
			sb.append(monthYear);
			sb.append("</PARAM1>");
			sb.append("</PARAMSTR>");
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getArrearsTransactionData")) {
			System.out.println(request.getQueryString());
			;
			ArrayList arrearsInfo = new ArrayList();
			ArrayList transactionList = new ArrayList();
			String pensionno = "", employeename = "";
			String dateOfAnnuation = "", wetheroption = "";
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				dateOfAnnuation = request.getParameter("dateOfAnnuation");
			}
			if (request.getParameter("wetheroption") != null) {
				wetheroption = request.getParameter("wetheroption");
			}
			if (request.getParameter("employeename") != null) {
				employeename = request.getParameter("employeename");
			}

			transactionList = fs.getArrearsTransactionData(pensionno,
					dateOfAnnuation, "", employeename);
			arrearsInfo = fs.getArrearsInfo(pensionno);
			request.setAttribute("arrearsInfo", arrearsInfo);
			request.setAttribute("empList", transactionList);
			request.setAttribute("pensionno", pensionno);
			request.setAttribute("wetheroption", wetheroption);
			request.setAttribute("dateOfAnnuation", dateOfAnnuation);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ArrearsTransactionScreen.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("addArrearsData")) {
			ArrayList empList = new ArrayList();

			String pensionno = "", dateOfAnnuation = "", wetheroption = "";
			int listsize = 0;
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				dateOfAnnuation = request.getParameter("dateOfAnnuation");
			}
			if (request.getParameter("wetheroption") != null) {
				wetheroption = request.getParameter("wetheroption");
			}
			if (request.getParameter("listsize") != null) {
				listsize = Integer.parseInt(request.getParameter("listsize")
						.toString());
			}
			for (int i = 1; i <= listsize; i++) {
				String monthyear = request.getParameter("monthyear" + i);
				String dueArrearamt = request.getParameter("arrearamt" + i);
				String dueEmoluments = request.getParameter("emoluments" + i);
				fs.addArrearsData(pensionno, monthyear, dueArrearamt,
						dueEmoluments);
			}

			empList = fs.getArrearsTransactionData(pensionno, dateOfAnnuation,
					"", "");
			request.setAttribute("dateOfAnnuation", dateOfAnnuation);
			request.setAttribute("pensionno", pensionno);
			request.setAttribute("empList", empList);
			request.setAttribute("wetheroption", wetheroption);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ArrearsTransactionScreen.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("updatearears")) {
			String pensionno = "", arreartotal = "", arreardate = "";
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("arreartotal") != null) {
				arreartotal = request.getParameter("arreartotal");
			}
			if (request.getParameter("arreardate") != null) {
				arreardate = request.getParameter("arreardate");
			}

			fs.updateArearData(pensionno, arreartotal, arreardate);
			StringBuffer sb = new StringBuffer();
			sb.append("upate success");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getArrearsTransactionDataReport")) {
			System.out.println(request.getQueryString());
			log.info("getArrearsTransactionDataReport Entering method");
			ArrayList empList = new ArrayList();
			ArrayList tranactiondata = new ArrayList();
			ArrayList arrearsInfo = new ArrayList();
			String pensionno = "";
			String dateOfAnnuation = "";
			int strtIndex = 1;
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				dateOfAnnuation = request.getParameter("dateOfAnnuation");
			}
			empList = fs.getArrearsData(pensionno, strtIndex, gridLength);
			request.setAttribute("empList", empList);
			arrearsInfo = fs.getArrearsInfo(pensionno);
			tranactiondata = fs.getArrearsTransactionData(pensionno,
					dateOfAnnuation, "", "");
			request.setAttribute("transacitondata", tranactiondata);
			request.setAttribute("pensionno", pensionno);
			request.setAttribute("arrearsInfo", arrearsInfo);
			request.setAttribute("dateOfAnnuation", dateOfAnnuation);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ArrearsTransactionScreenReport.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getArrears12MonthsTransactionDataReport")) {
			ArrayList transactionList = new ArrayList();
			ArrayList empList = new ArrayList();
			String pensionno = "";
			String dateOfAnnuation = "";
			String wetheroption = "";
			int strtIndex = 1;
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("dateOfAnnuation") != null) {
				dateOfAnnuation = request.getParameter("dateOfAnnuation");
			}
			if (request.getParameter("wetheroption") != null) {
				wetheroption = request.getParameter("wetheroption");
			}
			log.info("wetheroption" + wetheroption);
			String flag = "12Months";
			empList = fs.getArrearsData(pensionno, strtIndex, gridLength);
			transactionList = fs.getArrearsTransactionData(pensionno,
					dateOfAnnuation, flag, "");
			request.setAttribute("empList", empList);
			request.setAttribute("transactionList", transactionList);
			request.setAttribute("pensionno", pensionno);
			request.setAttribute("wetheroption", wetheroption);
			request.setAttribute("dateOfAnnuation", dateOfAnnuation);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ArrearsTransaction12MonthsReport.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("arrearsnav")) {
			String navgationBtn = "", empName = "";
			ArrayList empList = null;
			int strtIndex = 1, totalRecords = 0;
			String pensionno = "", pensioncontriadj = "";
			SearchInfo searchListBean = new SearchInfo();
			if (request.getParameter("pensioncontriadj").toString() != null) {
				pensioncontriadj = request.getParameter("pensioncontriadj")
						.toString();
			}
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = "|<";
			}
			if (request.getParameter("strtindx") != null) {
				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				System.out.println("strtIndex" + strtIndex);
				searchListBean.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				searchListBean.setTotalRecords(totalRecords);
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			SearchInfo searchInfo = null;
			try {
				searchInfo = fs.navigationArrearsData(navgationBtn, pensionno,
						strtIndex, gridLength, empName, pensioncontriadj);
				request.setAttribute("empList", searchInfo.getSearchList());
				request.setAttribute("pensionno", pensionno);
				request.setAttribute("searchBean", searchInfo);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			RequestDispatcher rd = null;
			if (pensioncontriadj.equals("true")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionContributionAdj.jsp");
				rd.forward(request, response);
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/ArrearsSearchScreen.jsp");
				rd.forward(request, response);
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAAIEPFAirports")) {
			String region = "", stationName = "", selectedfinyear = "", finyear = "";
			String accountType = "";
			region = request.getParameter("region");
			ArrayList ServiceType = null;
			selectedfinyear = request.getParameter("year");
			String[] yearList = selectedfinyear.split("-");
			finyear = yearList[0] + "-" + (Integer.parseInt(yearList[0]) + 1);
			if (session.getAttribute("station") != null) {
				stationName = (String) session.getAttribute("station");
			}
			PensionService ps1 = new PensionService();
			String username = session.getAttribute("userid").toString();
			String finalusername = username.substring(username.length() - 3,
					username.length());
			log.info("username" + username);
			log.info("finalusername" + finalusername);
			boolean value = CommonUtil.checkIfNumber(finalusername);
			if (request.getParameter("accounttype") != null) {
				accountType = request.getParameter("accounttype");
			}
			if (!stationName.equals("")) {
				ServiceType = new ArrayList();
				EmpMasterBean masterBean = new EmpMasterBean();
				masterBean.setStation(stationName);
				ServiceType.add(masterBean);
			} else {
				// ServiceType =
				// commonDAO.getAirportsByLatestPersonalTbl(region,finyear);
				if (ServiceType == null || ServiceType.size() == 0) {
					// ServiceType = commonDAO.getAirportsByPersonalTbl(region);
					if (value == true) {
						ServiceType = ps1.getUserAirports(region, username,
								accountType);
					} else {
						ServiceType = commonDAO.getAirportsByPersonalTbl(
								region, accountType, "");
					}
				}

			}
			log.info("airport list " + ServiceType.size());
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
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("loadPensionOption")) {
			HashMap map = new HashMap();
			String sortingColumn = "";
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("frm_sortingColumn") != null) {
				sortingColumn = request.getParameter("frm_sortingColumn");
			}
			log
					.info("pensionno"
							+ request.getParameter("pensionNO").toString());
			if (request.getParameter("pensionNO") != null) {
				empBean.setPensionNo(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				empBean.setPensionNo("");
			}
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {

				searchBean = personalService.searchWetherOption(empBean,
						getSearchInfo, flag, gridLength, sortingColumn, "");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionOptionChange.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("optionchange")) {

			String pensionno = "", optionChange = "", dateofbirth = "", empname = "";
			String[] unmappedRecords = {};
			String[] deleteTransactions = {};
			if (request.getParameter("option") != null) {
				optionChange = request.getParameter("option");
			}
			if (request.getParameter("dateofbirth") != null) {
				dateofbirth = request.getParameter("dateofbirth");
			}
			if (request.getParameter("empname") != null) {
				empname = request.getParameter("empname");
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}

			FinancialReportService finReportService = new FinancialReportService();
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");

			fs.optionChange(pensionno, optionChange, dateofbirth, empname,
					userName, ipAddress);
			StringBuffer sb = new StringBuffer();
			if (optionChange != "") {
				sb.append("WetherOption Changed to " + optionChange);
			}
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"checkControlAccntStatus")) {
			String status = "", formType = "", serialNo = "", result = "", formStatus = "";
			ArrayList resultList = new ArrayList();
			if (request.getParameter("formType") != null) {
				formType = request.getParameter("formType");
			}
			if (request.getParameter("serialNo") != null) {
				serialNo = request.getParameter("serialNo");
			}
			log.info("-----Form Type ------" + formType);
			resultList = (ArrayList) commonDAO.checkControlAccntStatus(
					formType, serialNo);
			result = (String) resultList.get(0);
			formStatus = (String) resultList.get(1);
			if ((formStatus != null) && (!formStatus.equals(""))) {

			} else {
				formStatus = "N";
			}
			if (result.equals("")) {
				status = "exists";
			} else {
				status = result;
			}
			log.info("-----status in Action  ------" + status + "="
					+ formStatus + "--");
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			sb.append("<ServiceType>");
			sb.append("<Status>");
			sb.append(status);
			sb.append("</Status>");
			sb.append("<FormStatus>");
			sb.append(formStatus);
			sb.append("</FormStatus>");

			sb.append("</ServiceType>");

			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			log.info(sb.toString());
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("adjCrtnDataSearch")) {
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

			if (request.getParameter("select_airport") != null) {
				empSerach.setStation(request.getParameter("select_airport")
						.toString().trim());
			}

			SearchInfo getSearchInfo = new SearchInfo();
			getSearchInfo = fs.adjCrtnDataSearch(empSerach, getSearchInfo,
					empNameCheck);
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("empNameChecked", empNameCheck);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/AdjCrtn/AdjCrtnDataMapping.jsp");

			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("adjMappingUpdate")) {

			String employeeCode = "", cpfaccno = "", employeeName = "", region = "", pensionno = "", designation = "", dateOfBirth = "", dateOfJoining = "", pensionOption = "";
			EmpMasterBean empSerach = new EmpMasterBean();

			if (request.getParameter("employeeCode") != null
					&& request.getParameter("employeeCode") != "---") {
				employeeCode = request.getParameter("employeeCode");
			}
			if (request.getParameter("cpfaccno") != null
					&& request.getParameter("cpfaccno") != "---") {
				cpfaccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("employeeName") != null
					&& request.getParameter("employeeName") != "---") {
				employeeName = request.getParameter("employeeName");
			}
			if (request.getParameter("region") != null
					&& request.getParameter("region") != "---") {
				region = request.getParameter("region");
			}
			if (request.getParameter("pensionno") != null
					&& request.getParameter("pensionno") != "---") {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("designation") != null
					&& request.getParameter("designation") != "---") {
				designation = request.getParameter("designation");
			}
			if (request.getParameter("dateOfBirth") != null
					&& request.getParameter("dateOfBirth") != "---") {
				dateOfBirth = request.getParameter("dateOfBirth");
			}
			if (request.getParameter("dateOfJoining") != null
					&& request.getParameter("dateOfJoining") != "---") {
				dateOfJoining = request.getParameter("dateOfJoining");
			}
			if (request.getParameter("pensionOption") != null
					&& request.getParameter("pensionOption") != "---") {
				pensionOption = request.getParameter("pensionOption");
			}

			if (session.getAttribute("getSearchBean1") != null) {
				empSerach = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}
			log.info("employeeCode" + employeeCode + "cpfaccno" + cpfaccno
					+ "employeeName" + employeeName + "region" + region
					+ "pensionno" + pensionno + "designation" + designation
					+ "dateOfBirth" + dateOfBirth + "dateOfJoining"
					+ dateOfJoining + "pensionOption" + pensionOption);
			fs.adjMappingUpdate(employeeCode, cpfaccno, employeeName, region,
					pensionno, designation, dateOfBirth, dateOfJoining,
					pensionOption);
			SearchInfo getSearchInfo = new SearchInfo();

			getSearchInfo = fs.adjCrtnDataSearch(empSerach, getSearchInfo,
					empSerach.getEmpNameCheak());
			request.setAttribute("searchBean", getSearchInfo);
			request.setAttribute("empSerach", empSerach);
			request.setAttribute("financeDatalist", getSearchInfo
					.getSearchList());
			request.setAttribute("empNameChecked", empSerach.getEmpNameCheak());
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/AdjCrtn/AdjCrtnDataMapping.jsp");

			rd.forward(request, response);

		} else if (request.getParameter("method").equals(
				"loadPFIDProcessSearchForm")
				|| request.getParameter("method").equals(
						"loadPFIDProcessNewForm")) {
			String path = "", frmName = "";
			RequestDispatcher rd = null;
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			request.setAttribute("frmName", frmName);

			if (frmName.equals("PFIDProcessSearch")) {
				path = "./PensionView/PFIDCreation/PFIDProcessSearch.jsp";
			} else if (frmName.equals("PFIDRecommendationSearch")) {
				path = "./PensionView/PFIDCreation/PFIDRecommendationSearch.jsp";
			} else if (frmName.equals("PFIDApprovalSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovalSearch.jsp";
			} else if (frmName.equals("PFIDApprovedSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovedSearch.jsp";
			} else if (frmName.equals("PFIDProcessNewForm")) {
				path = "./PensionView/PFIDCreation/PFIDProcessNew.jsp";
			}
			System.out.println("==frmName=====" + frmName + "==path=" + path);
			rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadPensionProcess")
				|| request.getParameter("method").equals(
						"loadPensionProcessNewForm")) {
			String path = "", frmName = "";
			RequestDispatcher rd = null;
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			request.setAttribute("frmName", frmName);

			if (frmName.equals("PFIDProcessSearch")) {
				path = "./PensionView/PFIDCreation/PFIDProcessSearch.jsp";
			} else if (frmName.equals("PFIDRecommendationSearch")) {
				path = "./PensionView/PFIDCreation/PFIDRecommendationSearch.jsp";
			} else if (frmName.equals("PFIDApprovalSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovalSearch.jsp";
			} else if (frmName.equals("PFIDApprovedSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovedSearch.jsp";
			} else if (frmName.equals("PensionProcessNewForm")) {
				path = "./PensionView/PensionProcess/PensionProcessNew.jsp";
			}
			System.out.println("==frmName=====" + frmName + "==path=" + path);
			rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("savePFIDProcessInfo")) {
			String region = "", airPortCode = "", empName = "", frm_dateOfBirth = "", doj = "", cpfaccno = "", employeeNo = "", gender = "", fhname = "", option = "", designation = "", verifiedby = "", remarks = "", frmName = "", processId = "", valEmpNameAndDOB = "", fileName = "";
			String emailid = "", userName = "", ipAddress = "", loginUserId = "", loginUsrStation = "", loginUsrDesgn = "", loginUsrRegion = "", path = "", message = "";
			int result = 0;
			ArrayList perInfoList = new ArrayList();
			EmployeePersonalInfo empInfo = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				airPortCode = request.getParameter("airPortCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName").trim();
			}
			if (request.getParameter("frm_dateOfBirth") != null) {
				frm_dateOfBirth = request.getParameter("frm_dateOfBirth");
			}
			if (request.getParameter("doj") != null) {
				doj = request.getParameter("doj");
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("employeeNo") != null) {
				employeeNo = request.getParameter("employeeNo");
			}
			if (request.getParameter("gender") != null) {
				gender = request.getParameter("gender");
			}
			if (request.getParameter("fhname") != null) {
				fhname = request.getParameter("fhname");
			}
			if (request.getParameter("option") != null) {
				option = request.getParameter("option");
			}
			if (request.getParameter("desegnation") != null) {
				designation = request.getParameter("desegnation");
			}
			if (request.getParameter("verifiedby") != null) {
				verifiedby = request.getParameter("verifiedby");
			}
			if (request.getParameter("remarks") != null) {
				remarks = request.getParameter("remarks");
			}
			if (request.getParameter("fileName") != null) {
				fileName = request.getParameter("fileName");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}

			empInfo.setRegion(region);
			empInfo.setAirportCode(airPortCode);
			empInfo.setEmployeeName(empName);
			empInfo.setDateOfBirth(frm_dateOfBirth);
			empInfo.setDateOfJoining(doj);
			empInfo.setCpfAccno(cpfaccno);
			empInfo.setEmployeeNumber(employeeNo);
			empInfo.setGender(gender);
			empInfo.setFhName(fhname);
			empInfo.setWetherOption(option);
			empInfo.setDesignation(designation);
			empInfo.setRemarks(remarks);
			empInfo.setFileName(fileName);
			empInfo.setFrmName(frmName);

			if (session.getAttribute("userid") != null) {
				userName = (String) session.getAttribute("userid");
			}
			if (session.getAttribute("computername") != null) {
				ipAddress = (String) session.getAttribute("computername");
			}
			if (session.getAttribute("station") != null) {
				loginUsrStation = (String) session.getAttribute("station");
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				loginUsrRegion = (String) session
						.getAttribute("loginUsrRegion");
			}
			if (session.getAttribute("emailid") != null) {
				emailid = (String) session.getAttribute("emailid");
			}

			if (session.getAttribute("loginUsrDesgn") != null) {
				loginUsrDesgn = (String) session.getAttribute("loginUsrDesgn");
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = (String) session.getAttribute("loginUserId");
			}
			try {

				String contentType = request.getContentType();

				log.info("Content type is :: " + contentType);
				String saveFile = "", dir = "", file = "", filePath = "", slashsuffix = "";
				int start = 0, end = 0;

				ResourceBundle appbundle = ResourceBundle
						.getBundle("aims.resource.ApplicationResouces");
				slashsuffix = appbundle
						.getString("upload.folder.path.slashsuffix");
				String folderPath = appbundle
						.getString("upload.folder.path.pfidFiles");

				File filePath1 = new File(folderPath);
				log.info("=======folder path " + filePath1.getAbsolutePath());
				System.out.println("=======folder path " + filePath1.getAbsolutePath());
				if (!filePath1.exists()) {
					filePath1.mkdirs();
				}

				if ((contentType != null)
						&& (contentType.indexOf("multipart/form-data") >= 0)) {

					DataInputStream in = new DataInputStream(request
							.getInputStream());

					int formDataLength = request.getContentLength();

					byte dataBytes[] = new byte[formDataLength];
					int byteRead = 0;
					int totalBytesRead = 0;
					while (totalBytesRead < formDataLength) {
						byteRead = in.read(dataBytes, totalBytesRead,
								formDataLength);
						// log.info("byteRead"+byteRead);
						totalBytesRead += byteRead;
					}
					file = new String(dataBytes);
					// start = file.indexOf("filename=\"")+10;
					start = file.indexOf("filename=\"") + 10;
					log.info("=start=" + start);
					end = file.indexOf(".xls");
					log.info("=end=" + end);

					if (file.indexOf(".mdb") != -1) {
						end = file.indexOf(".mdb");
						log.info("=end=" + end);
					}

					file = new String(dataBytes);
					saveFile = file.substring(file.indexOf("filename=\"") + 10);
					saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
					saveFile = saveFile.substring(
							saveFile.lastIndexOf("\\") + 1, saveFile
									.indexOf("\""));
					// we are added datetime as suffix in the file name

					log.info("File Name" + saveFile);
					int lastIndex = contentType.lastIndexOf("=");
					String boundary = contentType.substring(lastIndex + 1,
							contentType.length());
					// out.println(boundary);
					int pos;
					pos = file.indexOf("filename=\"");
					pos = file.indexOf("\n", pos) + 1;
					pos = file.indexOf("\n", pos) + 1;
					pos = file.indexOf("\n", pos) + 1;

					log.info("lastIndex==" + lastIndex + "=boundary="
							+ boundary + "pose" + pos);

					int boundaryLocation = file.indexOf(boundary, pos) - 4;

					int startPos = ((file.substring(0, pos)).getBytes()).length;
					int endPos = ((file.substring(0, boundaryLocation))
							.getBytes()).length;
					log.info("boundaryLocation==" + boundaryLocation
							+ "=startPos=" + startPos + "endPos" + endPos);
					// new code Start
					RequestDispatcher rd = null;
					// new code end
					try {

						String lengths = "", xlsSize = "", insertedSize = "", txtFileSize = "", invalidTxtSize = "";
						String[] temp;

						log.info("saveFilePath" + folderPath);
						File saveFilePath = new File(folderPath);
						log.info("saveFilePath" + saveFilePath);
						if (!saveFilePath.exists()) {
							File saveDir = new File(folderPath);
							if (!saveDir.exists())
								saveDir.mkdirs();

						}

						// This is used for segregated folder struture baseed on
						// AAI EPF forms

						String fileName1 = folderPath + slashsuffix + saveFile;

						log.info("fileName " + fileName);
						FileOutputStream fileOut = new FileOutputStream(
								fileName1);
						fileOut.write(dataBytes, startPos, (endPos - startPos));
						// fileOut.flush();
						// fileOut.close();

					} catch (Exception e) {
						log.info("in exception e " + e.getMessage());

					}

				}

				processId = personalService.savePfidProcessInfo(empInfo,
						userName, ipAddress, loginUserId, loginUsrStation,
						loginUsrRegion, emailid, loginUsrDesgn);

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (frmName.equals("PFIDProcessNewForm")) {
				message = " Record Saved Successfully With Tracking Id "
						+ processId;
			}
			request.setAttribute("message", message);
			request.setAttribute("frmName", frmName);
			path = "./PensionView/PFIDCreation/PFIDProcessSearch.jsp";

			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("updatePFIDProcess")) {
			String region = "", airPortCode = "", empName = "", frm_dateOfBirth = "", doj = "", cpfaccno = "", result = "", employeeNo = "", gender = "", fhname = "", option = "", designation = "", remarks = "", frmName = "", processId = "", fileName = "";
			String emailid = "", userName = "", ipAddress = "", loginUserId = "", loginUsrStation = "", loginUsrDesgn = "", loginUsrRegion = "", path = "", message = "";

			ArrayList perInfoList = new ArrayList();
			EmployeePersonalInfo empInfo = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				airPortCode = request.getParameter("airPortCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			if (request.getParameter("frm_dateOfBirth") != null) {
				frm_dateOfBirth = request.getParameter("frm_dateOfBirth");
			}
			if (request.getParameter("doj") != null) {
				doj = request.getParameter("doj");
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("employeeNo") != null) {
				employeeNo = request.getParameter("employeeNo");
			}
			if (request.getParameter("gender") != null) {
				gender = request.getParameter("gender");
			}
			if (request.getParameter("fhname") != null) {
				fhname = request.getParameter("fhname");
			}
			if (request.getParameter("option") != null) {
				option = request.getParameter("option");
			}
			if (request.getParameter("desegnation") != null) {
				designation = request.getParameter("desegnation");
			}
			if (request.getParameter("processid") != null) {
				processId = request.getParameter("processid");
			}
			if (request.getParameter("remarks") != null) {
				remarks = request.getParameter("remarks");
			}
			if (request.getParameter("fileName") != null) {
				fileName = request.getParameter("fileName");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			empInfo.setProcessID(processId);
			empInfo.setRegion(region);
			empInfo.setAirportCode(airPortCode);
			empInfo.setEmployeeName(empName);
			empInfo.setDateOfBirth(frm_dateOfBirth);
			empInfo.setDateOfJoining(doj);
			empInfo.setCpfAccno(cpfaccno);
			empInfo.setEmployeeNumber(employeeNo);
			empInfo.setGender(gender);
			empInfo.setFhName(fhname);
			empInfo.setWetherOption(option);
			empInfo.setDesignation(designation);
			empInfo.setRemarks(remarks);
			empInfo.setFileName(fileName);
			empInfo.setFrmName(frmName);

			if (session.getAttribute("userid") != null) {
				userName = (String) session.getAttribute("userid");
			}
			if (session.getAttribute("computername") != null) {
				ipAddress = (String) session.getAttribute("computername");
			}
			if (session.getAttribute("station") != null) {
				loginUsrStation = (String) session.getAttribute("station");
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				loginUsrRegion = (String) session
						.getAttribute("loginUsrRegion");
			}
			if (session.getAttribute("emailid") != null) {
				emailid = (String) session.getAttribute("emailid");
			}

			if (session.getAttribute("loginUsrDesgn") != null) {
				loginUsrDesgn = (String) session.getAttribute("loginUsrDesgn");
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = (String) session.getAttribute("loginUserId");
			}
			try {
				if (frmName.equals("editPFIDProcessForm")) {
					String contentType = request.getContentType();

					log.info("Content type is :: " + contentType);
					String saveFile = "", dir = "", file = "", filePath = "", slashsuffix = "";
					int start = 0, end = 0;

					ResourceBundle appbundle = ResourceBundle
							.getBundle("aims.resource.ApplicationResouces");
					slashsuffix = appbundle
							.getString("upload.folder.path.slashsuffix");
					String folderPath = appbundle
							.getString("upload.folder.path.pfidFiles");

					File filePath1 = new File(folderPath);
					log.info("========folder path -2 " + filePath1.getAbsolutePath());
					System.out.println("=======folder path -2" + filePath1.getAbsolutePath());
					if (!filePath1.exists()) {
						filePath1.mkdirs();
					}

					if ((contentType != null)
							&& (contentType.indexOf("multipart/form-data") >= 0)) {

						DataInputStream in = new DataInputStream(request
								.getInputStream());

						int formDataLength = request.getContentLength();

						byte dataBytes[] = new byte[formDataLength];
						int byteRead = 0;
						int totalBytesRead = 0;
						while (totalBytesRead < formDataLength) {
							byteRead = in.read(dataBytes, totalBytesRead,
									formDataLength);
							// log.info("byteRead"+byteRead);
							totalBytesRead += byteRead;
						}
						file = new String(dataBytes);
						// start = file.indexOf("filename=\"")+10;
						start = file.indexOf("filename=\"") + 10;
						log.info("=start=" + start);
						end = file.indexOf(".xls");
						log.info("=end=" + end);

						if (file.indexOf(".mdb") != -1) {
							end = file.indexOf(".mdb");
							log.info("=end=" + end);
						}

						file = new String(dataBytes);
						saveFile = file
								.substring(file.indexOf("filename=\"") + 10);
						saveFile = saveFile
								.substring(0, saveFile.indexOf("\n"));
						saveFile = saveFile.substring(saveFile
								.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
						// we are added datetime as suffix in the file name

						log.info("File Name" + saveFile);
						int lastIndex = contentType.lastIndexOf("=");
						String boundary = contentType.substring(lastIndex + 1,
								contentType.length());
						// out.println(boundary);
						int pos;
						pos = file.indexOf("filename=\"");
						pos = file.indexOf("\n", pos) + 1;
						pos = file.indexOf("\n", pos) + 1;
						pos = file.indexOf("\n", pos) + 1;

						log.info("lastIndex==" + lastIndex + "=boundary="
								+ boundary + "pose" + pos);

						int boundaryLocation = file.indexOf(boundary, pos) - 4;

						int startPos = ((file.substring(0, pos)).getBytes()).length;
						int endPos = ((file.substring(0, boundaryLocation))
								.getBytes()).length;
						log.info("boundaryLocation==" + boundaryLocation
								+ "=startPos=" + startPos + "endPos" + endPos);
						// new code Start
						RequestDispatcher rd = null;
						// new code end
						try {

							String lengths = "", xlsSize = "", insertedSize = "", txtFileSize = "", invalidTxtSize = "";
							String[] temp;

							log.info("saveFilePath" + folderPath);
							File saveFilePath = new File(folderPath);
							log.info("saveFilePath" + saveFilePath);
							if (!saveFilePath.exists()) {
								File saveDir = new File(folderPath);
								if (!saveDir.exists())
									saveDir.mkdirs();

							}

							// This is used for segregated folder struture
							// baseed on AAI EPF forms

							String fileName1 = folderPath + slashsuffix
									+ saveFile;

							log.info("fileName " + fileName);
							FileOutputStream fileOut = new FileOutputStream(
									fileName1);
							fileOut.write(dataBytes, startPos,
									(endPos - startPos));
							// fileOut.flush();
							// fileOut.close();

						} catch (Exception e) {
							log.info("in exception e " + e.getMessage());

						}
					}

					result = personalService.savePfidProcessEditInfo(empInfo,
							userName, ipAddress, loginUserId, loginUsrStation,
							loginUsrRegion, emailid, loginUsrDesgn);
				} else {
					result = personalService.updatePFIDProcess(empInfo,
							userName, ipAddress, loginUserId, loginUsrStation,
							loginUsrRegion, emailid, loginUsrDesgn);
				}

			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("editPFIDProcessForm")) {
				frmName = "PFIDProcessSearch";
				path = "./PensionView/PFIDCreation/PFIDProcessSearch.jsp";
			} else if (frmName.equals("PFIDProcessRecommendation")) {
				frmName = "PFIDRecommendationSearch";
				path = "./PensionView/PFIDCreation/PFIDRecommendationSearch.jsp";
			} else if (frmName.equals("PFIDProcessApproval")) {
				frmName = "PFIDApprovalSearch";
				path = "./PensionView/PFIDCreation/PFIDApprovalSearch.jsp";
			}
			request.setAttribute("frmName", frmName);

			System.out.println("=in Action========" + frmName);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("searchForPfidProcess")) {
			String rgnName = "", frmName = "", privilages = "", userStation = "", profileType = "", accessCode = "", accountType = "", userRegion = "", loginUserId = "", path = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();

			if (request.getParameter("airPortCode") != null) {
				empBean.setAirportCode(request.getParameter("airPortCode")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}

			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
				empBean.setFrmName(request.getParameter("frmName").toString());
			} else {
				empBean.setFrmName("");
			}
			if (request.getParameter("frm_dateOfBirth") != null
					|| request.getParameter("frm_dateOfBirth") != "") {
				empBean.setDateOfBirth(request.getParameter("frm_dateOfBirth")
						.toString().trim());
			} else {
				empBean.setDateOfBirth("");
			}
			log.info("Date Of Birth====" + empBean.getDateOfBirth()
					+ "===frmName =====" + empBean.getFrmName());
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAccno(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAccno("");
			}
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}
			if (session.getAttribute("privilages") != null) {
				privilages = session.getAttribute("privilages").toString();
			}
			if (session.getAttribute("profileType") != null) {
				profileType = session.getAttribute("profileType").toString();
			}
			if (session.getAttribute("station") != null) {
				userStation = ((String) session.getAttribute("station"))
						.toLowerCase().trim();
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				userRegion = ((String) session.getAttribute("loginUsrRegion"))
						.toLowerCase().trim();
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = session.getAttribute("loginUserId").toString();
			}
			if (request.getParameter("accessCode") != null) {
				accessCode = request.getParameter("accessCode");
			}
			if (session.getAttribute("accountType") != null) {
				accountType = session.getAttribute("accountType").toString();
			}

			// code added for empname blanks search

			ArrayList searchList = new ArrayList();
			boolean flag = true;
			try {

				searchList = personalService.searchForPfidProcess(empBean,
						userRegion, userStation, profileType, accessCode,
						accountType);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			request.setAttribute("searchList", searchList);
			request.setAttribute("frmName", frmName);
			if (frmName.equals("PFIDProcessSearch")) {
				path = "./PensionView/PFIDCreation/PFIDProcessSearch.jsp";
			} else if (frmName.equals("PFIDRecommendationSearch")) {
				path = "./PensionView/PFIDCreation/PFIDRecommendationSearch.jsp";
			} else if (frmName.equals("PFIDApprovalSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovalSearch.jsp";
			} else if (frmName.equals("PFIDApprovedSearch")) {
				path = "./PensionView/PFIDCreation/PFIDApprovedSearch.jsp";
			} else if (frmName.equals("PFIDProcessNewForm")) {
				path = "./PensionView/PFIDCreation/PFIDProcessNew.jsp";
			}

			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("editPFIDProcess")) {
			String rgnName = "", frmName = "", processid = "", path = "";
			ArrayList empList = new ArrayList();
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}

			if (request.getParameter("processid") != null) {
				processid = request.getParameter("processid");
			}
			boolean flag = true;
			try {

				empList = personalService.editPfidProcessInfo(processid,
						frmName);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			request.setAttribute("empList", empList);
			request.setAttribute("frmName", frmName);
			if (frmName.equals("PFIDProcessRecommendation")) {
				path = "./PensionView/PFIDCreation/PFIDProcesRecommedationForm.jsp";
			} else if (frmName.equals("PFIDProcessApproval")) {
				path = "./PensionView/PFIDCreation/PFIDProcessApprovalForm.jsp";
			} else {
				path = "./PensionView/PFIDCreation/PFIDProcessEdit.jsp";
			}
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("createPFIDProcess")) {
			String region = "", airPortCode = "", empName = "", frm_dateOfBirth = "", doj = "", cpfaccno = "", result = "", employeeNo = "", gender = "", fhname = "", option = "", designation = "", remarks = "", frmName = "", processId = "";
			String emailid = "", userName = "", ipAddress = "", loginUserId = "", loginUsrStation = "", loginUsrDesgn = "", loginUsrRegion = "", path = "", message = "";

			EmployeePersonalInfo empInfo = new EmployeePersonalInfo();
			if (request.getParameter("airPortCode") != null) {
				airPortCode = request.getParameter("airPortCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName");
			}
			if (request.getParameter("frm_dateOfBirth") != null) {
				frm_dateOfBirth = request.getParameter("frm_dateOfBirth");
			}
			if (request.getParameter("doj") != null) {
				doj = request.getParameter("doj");
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno");
			}
			if (request.getParameter("employeeNo") != null) {
				employeeNo = request.getParameter("employeeNo");
			}
			if (request.getParameter("gender") != null) {
				gender = request.getParameter("gender");
			}
			if (request.getParameter("fhname") != null) {
				fhname = request.getParameter("fhname");
			}
			if (request.getParameter("option") != null) {
				option = request.getParameter("option");
			}
			if (request.getParameter("desegnation") != null) {
				designation = request.getParameter("desegnation");
			}
			if (request.getParameter("processid") != null) {
				processId = request.getParameter("processid");
			}
			if (request.getParameter("remarks") != null) {
				remarks = request.getParameter("remarks");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
			empInfo.setProcessID(processId);
			empInfo.setRegion(region);
			empInfo.setAirportCode(airPortCode);
			empInfo.setEmployeeName(empName);
			empInfo.setDateOfBirth(frm_dateOfBirth);
			empInfo.setDateOfJoining(doj);
			empInfo.setCpfAccno(cpfaccno);
			empInfo.setEmployeeNumber(employeeNo);
			empInfo.setGender(gender);
			empInfo.setFhName(fhname);
			empInfo.setWetherOption(option);
			empInfo.setDesignation(designation);
			empInfo.setRemarks(remarks);
			empInfo.setFrmName(frmName);

			if (session.getAttribute("userid") != null) {
				userName = (String) session.getAttribute("userid");
			}
			if (session.getAttribute("computername") != null) {
				ipAddress = (String) session.getAttribute("computername");
			}
			if (session.getAttribute("station") != null) {
				loginUsrStation = (String) session.getAttribute("station");
			}
			if (session.getAttribute("loginUsrRegion") != null) {
				loginUsrRegion = (String) session
						.getAttribute("loginUsrRegion");
			}
			if (session.getAttribute("emailid") != null) {
				emailid = (String) session.getAttribute("emailid");
			}

			if (session.getAttribute("loginUsrDesgn") != null) {
				loginUsrDesgn = (String) session.getAttribute("loginUsrDesgn");
			}
			if (session.getAttribute("loginUserId") != null) {
				loginUserId = (String) session.getAttribute("loginUserId");
			}
			try {
				result = personalService.createPFIDProcess(empInfo, userName,
						ipAddress, loginUserId, loginUsrStation,
						loginUsrRegion, emailid, loginUsrDesgn);

			} catch (Exception e) {
				log.printStackTrace(e);
			}

			if (frmName.equals("PFIDCreation")) {
				frmName = "PFIDApprovalSearch";
			}
			request.setAttribute("frmName", frmName);
			path = "./PensionView/PFIDCreation/PFIDApprovalSearch.jsp";
			System.out.println("=in Action========" + frmName);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("chkForDuplicateEntry")) {
			String region = "", airPortCode = "", empName = "", dob = "", frmName = "", message = "", valEmpNameAndDOB = "";

			if (request.getParameter("airPortCode") != null) {
				airPortCode = request.getParameter("airPortCode");
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region");
			}
			if (request.getParameter("empName") != null) {
				empName = request.getParameter("empName").trim();
			}
			if (request.getParameter("dob") != null) {
				dob = request.getParameter("dob");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}

			try {
				valEmpNameAndDOB = personalService.chkForDuplicateEntry(
						empName, dob);
				StringBuffer sb = new StringBuffer();
				sb.append("<ServiceTypes>");
				sb.append("<ServiceType>");
				sb.append("<PensionNo>");
				sb.append(valEmpNameAndDOB);
				sb.append("</PensionNo>");
				sb.append("</ServiceType>");

				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				log.info(sb.toString());
				out.write(sb.toString());

			} catch (Exception e) {
				log.printStackTrace(e);
			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getPFIDFiles")) {
			String fileurl = "", url = "", fileName = "", processid = "", type = "";
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.ApplicationResouces");

			if (request.getParameter("fileName") != null) {
				fileName = request.getParameter("fileName");
			}
			if (request.getParameter("processid") != null) {
				processid = request.getParameter("processid");
			}
			if (request.getParameter("type") != null) {
				type = request.getParameter("type");
			}
			fileurl = personalService.readPFIDFiles(processid, fileName);

			log.info("fileurl======================" + fileurl);

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			File file = new File(url);
			FileInputStream fileIn = new FileInputStream(fileurl);
			ServletOutputStream out = response.getOutputStream();

			byte[] outputByte = new byte[4096];
			// copy binary contect to output stream
			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
			fileIn.close();
			out.flush();
			out.close();

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchForPensionProcess")) {
			String rgnName = "", page = "", formName = "", path = "";
			HashMap map = new HashMap();
			String[] regionLst = null;
			EmployeePersonalInfo empBean = new EmployeePersonalInfo();
			if (request.getParameter("frmName") != null) {
				formName = request.getParameter("frmName");
			}
			log.info("frm name----------" + formName);
			if (request.getParameter("airportID") != null) {
				empBean.setAirportCode(request.getParameter("airportID")
						.toString().trim());
				if (empBean.getAirportCode().equals("NO-SELECT")) {
					empBean.setAirportCode("");
				}
			}
			if (request.getParameter("empName") != null) {
				empBean.setEmployeeName(request.getParameter("empName")
						.toString().trim());
			}
			if (request.getParameter("page") != null) {
				page = request.getParameter("page").toString();
			}
			if (request.getParameter("pensionno") != null) {
				empBean.setPensionNo(request.getParameter("pensionno")
						.toString().trim());
			} else {
				empBean.setPensionNo("");
			}
			log.info("pensionno " + empBean.getPensionNo());

			if (request.getParameter("dob") != null
					|| request.getParameter("dob") != "") {
				empBean.setDateOfBirth(request.getParameter("dob").toString()
						.trim());
			} else {
				empBean.setDateOfBirth("");
			}
			log.info("Date Of Birth====" + empBean.getDateOfBirth());
			if (request.getParameter("regionID") != null) {
				empBean.setRegion(request.getParameter("regionID").toString()
						.trim());
				if (empBean.getRegion().equals("NO-SELECT")) {
					empBean.setRegion("");
				}
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateOfJoining(request.getParameter("doj").toString()
						.trim());
			} else {
				empBean.setDateOfJoining("");
			}
			if (request.getParameter("penoption") != null) {
				empBean.setWetherOption(request.getParameter("penoption")
						.toString().trim());
			} else {
				empBean.setWetherOption("");
			}
			if (request.getParameter("employeeCode") != null) {
				empBean.setEmployeeNumber(request.getParameter("employeeCode")
						.toString().trim());
			} else {
				empBean.setEmployeeNumber("");
			}

			// code added for empname blanks search
			SearchInfo getSearchInfo = new SearchInfo();
			ArrayList dataList = new ArrayList();
			boolean flag = true;
			try {
				dataList = personalService.searchPensionProcess(empBean,
						formName);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", dataList);
			request.setAttribute("searchInfo", empBean);
			if (formName.equals("chqHRInitial")) {
				path = "./PensionView/dashboard/PensionProcessCHQHRInputParams.jsp";
			} else if (formName.equals("chqFinance")) {
				path = "./PensionView/dashboard/PensionProcessCHQFinInputParams.jsp";
			} else if (formName.equals("RPFCSubmission")) {
				path = "./PensionView/dashboard/PensionProcesssDOSInputParams.jsp";
			} else if (formName.equals("RPFCReturn")) {
				path = "./PensionView/dashboard/PensionProcessDORInputParams.jsp";
			} else {
				path = "./PensionView/dashboard/PensionProcessSearchInputParams.jsp";
			}

			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("editPensionProcess")) {
			String pensionno = "", process_id = "", formName = "", path = "", verifiedBy = "";
			EmployeePersonalInfo empInfo = new EmployeePersonalInfo();
			log.info("Inside edit" + formName);
			if (request.getParameter("processid") != null) {
				process_id = request.getParameter("processid");
			}
			if (request.getParameter("pensionno") != null) {
				pensionno = request.getParameter("pensionno");
			}
			if (request.getParameter("frmName") != null) {
				formName = request.getParameter("frmName");
			}
			if (request.getParameter("verifiedby") != null) {
				verifiedBy = request.getParameter("verifiedby");
			}
			if (request.getAttribute("SetSearchBean") != null) {
				request.setAttribute("searchInfo", request
						.getAttribute("SetSearchBean"));
			}
			try {
				empInfo = personalService.editPensionProcessInfo(process_id,
						pensionno, verifiedBy);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			request.setAttribute("empInfo", empInfo);
			if (formName.equals("chqHRInitial")) {
				path = "./PensionView/dashboard/PensionProcessCHQHRInitialEdit.jsp";
			} else if (formName.equals("chqFinance")) {
				path = "./PensionView/dashboard/PensionProcessCHQHRFinEdit.jsp";
			} else if (formName.equals("RPFCSubmission")) {
				path = "./PensionView/dashboard/PensionProcessRPFCSubmissionEdit.jsp";
			} else if (formName.equals("RPFCReturn")) {
				path = "./PensionView/dashboard/PensionProcessRPFCReturnEdit.jsp";
			} else {
				path = "./PensionView/dashboard/PensionProcessEdit.jsp";
			}
			log.info("path----" + path);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"loadPensionProcessForm")) {
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/dashboard/PensionProcessNewInputParams.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("updatepensionprocess")) {
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
			EmployeePersonalInfo searchBeanInfo = new EmployeePersonalInfo();
			ArrayList updatedList = new ArrayList();
			HashMap map = new HashMap();
			String[] regionLst = null;
			String rgnName = "", formName = "";
			if (request.getParameter("pensionType") != null) {
				personalInfo
						.setPensiontype(request.getParameter("pensionType"));
			}
			if (request.getParameter("chqDate") != null) {
				personalInfo.setDostochq(request.getParameter("chqDate"));
			}
			if (request.getParameter("emailid") != null) {
				personalInfo.setEmailID(request.getParameter("emailid"));
			}
			if (request.getParameter("mobileNo") != null) {
				personalInfo.setPhoneNumber(request.getParameter("mobileNo"));
			}
			if (request.getParameter("remarks") != null) {
				personalInfo.setRemarks(request.getParameter("remarks"));
			}
			if (request.getParameter("pensionno") != null) {
				personalInfo.setPensionNo(request.getParameter("pensionno"));
			}
			if (request.getParameter("frmName") != null) {
				formName = request.getParameter("frmName");
			}
			if (request.getParameter("processID") != null) {
				personalInfo.setProcessID(request.getParameter("processID"));
			}
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			if (request.getAttribute("SetSearchBean") != null) {
				searchBeanInfo = (EmployeePersonalInfo) request
						.getAttribute("SetSearchBean");
				request.setAttribute("searchInfo", request
						.getAttribute("SetSearchBean"));
			}
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals(
								"Admin")) {
					map = new HashMap();
					map = commonUtil.getRegion();
					break;
				} else {
					map.put(new Integer(i), rgnName);
				}
			}
			try {
				personalService
						.updatePensionProcessInfo(personalInfo, formName);
				updatedList = personalService.searchPensionProcess(
						searchBeanInfo, formName);

			} catch (Exception e) {
				// TODO: handle exception
				log.printStackTrace(e);
			}
			request.setAttribute("regionHashmap", map);
			request.setAttribute("searchBean", updatedList);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/dashboard/PensionProcessSearchInputParams.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("loadInterfaceReport")) {

			String reportType = "", formType = "", frmName = "", region="" , airportCode = "";
			ArrayList penList = new ArrayList();

			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frmName") != null) {
				frmName = request.getParameter("frmName");
			}
		
			if(request.getParameter("select_region")!=null) {
				region=request.getParameter("select_region");
			}
			if(request.getParameter("select_airport")!=null) {
				airportCode=request.getParameter("select_airport");
			}
			//log.info("region :"+region +"airportCode : "+airportCode+"formType :"+formType);
			
			if (formType.equals("Report A")) {
				penList = personalService.freshOptionReport(formType, region, airportCode);
			} else if(formType.equals("Report A-A") || formType.equals("Report A-B") ){
				penList = personalService.allFreshOptionReport(formType, region, airportCode);
			}else {				
				penList = personalService.freshOptionSummaryReport();
				
				
			}
			request.setAttribute("dataList", penList);

			request.setAttribute("reportType", reportType);
			log.info("fffffff" + formType);
			RequestDispatcher rd = null;
			if (formType.equals("Report A")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/loadInterfaceReport.jsp");
			} else if (formType.equals("Report A-A")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/loadInterfaceReport A-A.jsp");
			} else if (formType.equals("Report A-B")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/loadInterfaceReportab.jsp");
			} else if (formType.equals("Summary Report")) {
				rd = request
						.getRequestDispatcher("./PensionView/personal/loadInterfaceSummaryReport.jsp");
			}

			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& (request.getParameter("method").equals(
						"updatepensionprocessCHQHRInitial")
						|| request.getParameter("method").equals(
								"updatepensionprocessCHQFinance")
						|| request.getParameter("method").equals(
								"updatepensionprocessRPFCDateofSubmission") || request
						.getParameter("method").equals(
								"updatepensionprocessRPFCDateofReturn"))) {
			EmployeePersonalInfo personalInfo = new EmployeePersonalInfo();
			EmployeePersonalInfo searchBeanInfo = new EmployeePersonalInfo();
			ArrayList updatedCHQHRList = new ArrayList();
			String formName = "", path = "";
			if (request.getParameter("pensionno") != null) {
				personalInfo.setPensionNo(request.getParameter("pensionno"));
			}
			if (request.getParameter("processID") != null) {
				personalInfo.setProcessID(request.getParameter("processID"));
			}
			if (request.getParameter("chqHRFinDate") != null) {
				personalInfo.setDatetoCHQHR(request
						.getParameter("chqHRFinDate"));
			}
			if (request.getParameter("chqFinDate") != null) {
				personalInfo.setDatetoCHQHR(request.getParameter("chqFinDate"));
			}
			if (request.getParameter("rpfcSubmisionDate") != null) {
				personalInfo.setDatetoCHQHR(request
						.getParameter("rpfcSubmisionDate"));
			}
			if (request.getParameter("rpfcSubmisionRemarks") != null) {
				personalInfo.setRemarks(request
						.getParameter("rpfcSubmisionRemarks"));
			}

			if (request.getParameter("rpfcReturnDate") != null) {
				personalInfo.setDatetoCHQHR(request
						.getParameter("rpfcReturnDate"));
			}
			if (request.getParameter("rpfcReturnRemarks") != null) {
				personalInfo.setRemarks(request
						.getParameter("rpfcReturnRemarks"));
			}

			if (request.getParameter("chqFinremarks") != null) {
				personalInfo.setRemarks(request.getParameter("chqFinremarks"));
			}
			if (request.getParameter("chqHrInitialremarks") != null) {
				personalInfo.setRemarks(request
						.getParameter("chqHrInitialremarks"));
			}
			if (session.getAttribute("computername") != null) {
				personalInfo.setIpAddress(session.getAttribute("computername")
						.toString());
			}
			if (session.getAttribute("userid") != null) {
				personalInfo.setUserName(session.getAttribute("userid")
						.toString());
			}
			if (request.getParameter("regionname") != null) {
				personalInfo.setRegion(request.getParameter("regionname"));
			}
			log.info("region==" + personalInfo.getRegion() + "username=="
					+ personalInfo.getUserName());
			if (request.getParameter("airportcode") != null) {
				personalInfo
						.setAirportCode(request.getParameter("airportcode"));
			}
			if (request.getParameter("frmName") != null) {
				formName = request.getParameter("frmName");
			}
			if (request.getAttribute("SetSearchBean") != null) {
				searchBeanInfo = (EmployeePersonalInfo) request
						.getAttribute("SetSearchBean");
				request.setAttribute("searchInfo", request
						.getAttribute("SetSearchBean"));
			}
			log.info("airportCode==" + personalInfo.getAirportCode()
					+ "frmName==" + formName);
			try {
				personalService.updatePensionProcessCHQ(personalInfo, formName);
				updatedCHQHRList = personalService.searchPensionProcess(
						searchBeanInfo, formName);
			} catch (Exception e) {
				// TODO: handle exception
				log.printStackTrace(e);
			}
			if (formName.equals("chqHRInitial")) {
				path = "./PensionView/dashboard/PensionProcessCHQHRInputParams.jsp";
			} else if (formName.equals("chqFinance")) {
				path = "./PensionView/dashboard/PensionProcessCHQFinInputParams.jsp";
			} else if (formName.equals("RPFCSubmission")) {
				path = "./PensionView/dashboard/PensionProcesssDOSInputParams.jsp";
			} else if (formName.equals("RPFCReturn")) {
				path = "./PensionView/dashboard/PensionProcessDORInputParams.jsp";
			}

			request.setAttribute("searchBean", updatedCHQHRList);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
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
