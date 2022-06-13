/**
 * File       : AAIEPFFormsReportServlet.java
 * Date       : 02/09/2010
 * Author     : AIMS 
 * Description: 
 * Copyright (2009-2010) by the Navayuga Infotech, all rights reserved.
 * 
 */
package aims.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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

import aims.bean.EmpMasterBean;
import aims.bean.epfforms.AAIEPFReportBean;
import aims.bean.epfforms.AaiEpfForm11Bean;
import aims.bean.epfforms.AaiEpfform3Bean;
import aims.bean.epfforms.ControlAccountForm2Info;
import aims.bean.epfforms.RemittanceBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.service.EPFFormsReportService;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.RemittanceService;

public class AAIEPFFormsReportServlet extends HttpServlet {
	Log log = new Log(ReportServlet.class);
	RemittanceService remittanceService = new RemittanceService();

	EPFFormsReportService epfformsService = new EPFFormsReportService();
	FinancialService financeService = new FinancialService();
	FinancialReportService finReportService = new FinancialReportService();
	CommonUtil commonUtil = new CommonUtil();
	CommonDAO commonDAO = new CommonDAO();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		System.out.println("******************AAI EPF Forms Report Servlet*******************");
		
		
		if (request.getParameter("method").equals("loadob") || request.getParameter("method").equals("loadepf2")
				|| request.getParameter("method").equals("loadepf3")
				|| request.getParameter("method").equals("loadepf4")
				|| request.getParameter("method").equals("loadEPF-8")
				|| request.getParameter("method").equals("loadepf5")
				|| request.getParameter("method").equals("loadepf7")
				|| request.getParameter("method").equals("loadepf11")
				|| request.getParameter("method").equals("loadepf6")
				|| request.getParameter("method").equals("loadepf12")
				|| request.getParameter("method").equals("loadepf") || request.getParameter("method").equals("loadAdj")
				|| request.getParameter("method").equals("loadAccretionParam")
				|| request.getParameter("method").equals("loadcpfAccretion")
				|| request.getParameter("method").equals("summaryReports")
				|| request.getParameter("method").equals("loadStationWiseRemittance")
				|| request.getParameter("method").equals("loadStationWiseReport")
				|| request.getParameter("method").equals("executive")) {

			log.info("1");
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();
			if (request.getParameter("method").equals("loadFinContri")
					|| request.getParameter("method").equals("loadUpdatePC")) {
				log.info("2");
				penYearList = finReportService.getFinanceYearList();
				request.setAttribute("yearList", penYearList);
			} else {
				log.info("3");
				request.setAttribute("yearIterator", yearIterator);

			}
			log.info("4");
			monthMap = commonUtil.getMonthsList();
			
			log.info("month map from common utill : " + monthMap);
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			monthIterator1 = monthset.iterator();
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("monthToIterator", monthIterator1);
			HashMap regionHashmap = new HashMap();

			String[] regionLst = null;
			String rgnName = "";
			if (session.getAttribute("region") != null) {
				log.info("5");
				regionLst = (String[]) session.getAttribute("region");
				log.info("Region list : " + regionLst);
			}
			
			
			log.info("Regions List : " + regionLst.length);
			
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				log.info("6");
				// Adding Cond on NAGARAJ ON 20-Jul-2012 For not loading all Regions names
				if (rgnName.equals("ALL-REGIONS") && (session.getAttribute("usertype").toString().equals("Admin"))
						|| (session.getAttribute("usertype").toString().equals("NormalUser")
								&& session.getAttribute("userid").toString().equals("NAGARAJ"))) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					log.info("7");
					break;
				} else {
					log.info("8");
					regionHashmap.put(new Integer(i), rgnName);
				}

			}

			log.info("8");
			request.setAttribute("regionHashmap", regionHashmap);

			RequestDispatcher rd = null;

			if (request.getParameter("method").equals("loadob")) {
				log.info("9");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-1InputParams.jsp");
			}
			else if (request.getParameter("method").equals("loadepf2")) {
				log.info("10");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-2InputParams.jsp");

			}
			else if (request.getParameter("method").equals("loadepf3") && request.getParameter("page") != null
					&& request.getParameter("page").equalsIgnoreCase("remitencescreen")) {
				System.out.println(request.getParameter("page"));
				log.info("11");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/RemittanceScreen.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf3") && request.getParameter("page") != null
					&& request.getParameter("page").equalsIgnoreCase("remitencereport")) {
				log.info("12");
				System.out.println(request.getParameter("page"));
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/RemittanceReportInputParam.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf3")) {
			
				log.info("13");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-3InputParams.jsp");
			} 
			else if (request.getParameter("method").equals("executive")) {
				log.info("14");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/ExecutiveInputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf4")) {
				log.info("15");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-4InputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf5")) {
				log.info("16");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-5InputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf6")) {
				log.info("17");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-6InputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf7")) {
				log.info("18");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-7InputParams.jsp");

			} 
			else if (request.getParameter("method").equals("loadEPF-8")) {
				log.info("19");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-8ReportInputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf11")) {
				log.info("20");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-11ReportInputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf12")) {
				log.info("21");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-12ReportInputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadepf")) {
				log.info("22");
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-InputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadAdj")) {
				log.info("23");
				rd = request.getRequestDispatcher("./PensionView/AdjCpfRecoveries.jsp");
			} 
			else if (request.getParameter("method").equals("loadAccretionParam")) {
				log.info("24");
				System.out.println(request.getParameter("page"));
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AccretionReportInputParams.jsp");
			} 
			else if (request.getParameter("method").equals("loadcpfAccretion")) {
				log.info("25");
				System.out.println(request.getParameter("page"));
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/CpfAccretionReportInputParams.jsp");
			} 
			
			
			else if (request.getParameter("method").equals("summaryReports")) {
				log.info("26");
				String formType = "summaryReports", serialNo = "";
				ArrayList list = new ArrayList();
				list = commonDAO.checkControlAccntStatus(formType, "");
				serialNo = (String) list.get(0);
				request.setAttribute("serialNo", serialNo);
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/SummaryReportInputParams.jsp");
			} 
			
			else if (request.getParameter("method").equals("loadStationWiseReport")) {
				log.info("27");
				String year = "", month = "", region = "", flag = "", monthYear = "", station = "", form = "";
				String currdate = "", currYear = "", selCurrYear = "";
				int records = 0, year1 = 0;
				ArrayList stationList = null;

				if (request.getParameter("year") != null) {
					log.info("28");
					year = request.getParameter("year");
				}
				if (request.getParameter("month") != null) {
					log.info("29");
					month = request.getParameter("month");
				}
				if (request.getParameter("region") != null) {
					log.info("30");
					region = request.getParameter("region");
				}
				if (request.getParameter("flag") != null) {
					log.info("31");
					flag = request.getParameter("flag");
				}
				if (request.getParameter("form") != null) {
					log.info("32");
					form = request.getParameter("form");
				}
				if (request.getParameter("year") != null) {
					log.info("33");
					if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 3) {
						year1 = Integer.parseInt(year.substring(0, 4)) + 1;
						log.info("34");
					} else {
						log.info("35");
						year1 = Integer.parseInt(year.substring(0, 4));
					}
				}
				log.info("36");
				monthYear = "01-" + month + "-" + year1;
				currdate = commonUtil.getCurrentDate("MM");
				log.info("monthYear==<<<>>===" + monthYear);
				currYear = commonUtil.getCurrentDate("yyyy");
				log.info("currdate" + currdate);
				if (Integer.parseInt(currdate) < 4) {
					log.info("37");
					currYear = ("" + (Integer.parseInt(currYear) - 1)).trim();

				}
				log.info("currYear" + currYear);
				selCurrYear = currYear + "-" + (Integer.parseInt(currYear.substring(2, 4)) + 1);

				try {

					if (flag.equals("F")) {
						log.info("28");
						if (!form.equals("chq")) {
							log.info("29");
							if (region.equals("CHQIAD")) {
								log.info("30");
								log.info("station" + station);
								station = session.getAttribute("station").toString();
								log.info("station" + station);
							}
						}
						log.info("31");
						monthYear = commonUtil.converDBToAppFormat(monthYear, "dd-MM-yyyy", "dd-MMM-yyyy");
						stationList = epfformsService.getStationList(region, monthYear, station);
						request.setAttribute("month", month);
						request.setAttribute("region", region);
					} else {
						log.info("32");
						request.setAttribute("month", currdate);
						request.setAttribute("year", selCurrYear);
					}
					// log.info("stationList"+stationList);
				} catch (Exception e) {
					log.info("33");
					e.printStackTrace();
				}

				log.info("34");
				request.setAttribute("monthYear", monthYear);
				request.setAttribute("stationList", stationList);
				// if(session.getAttribute("usertype").toString().equals("Admin")){
				rd = request.getRequestDispatcher(
						"./PensionView/aaiepfreports/StationWiseRemittanceReportInputParamsForChqNad.jsp");
				// }
			} 
			
	
			else if (request.getParameter("method").equals("loadStationWiseRemittance")) {
				
				log.info("35");
				String year = "", month = "", region = "", flag = "", monthYear = "", station = "", form = "";
				String currdate = "", currYear = "", selCurrYear = "";
				int records = 0;
				int year1 = 0;
				ArrayList stationList = null;

				if (request.getParameter("year") != null) {
					log.info("36");
					year = request.getParameter("year");
				}
				if (request.getParameter("month") != null) {
					log.info("37");
					month = request.getParameter("month");
				}
				if (request.getParameter("region") != null) {
					log.info("38");
					region = request.getParameter("region");
				}
				if (request.getParameter("flag") != null) {
					log.info("39");
					flag = request.getParameter("flag");
				}
				if (request.getParameter("form") != null) {
					log.info("40");
					form = request.getParameter("form");
				}
				log.info("40");
				currdate = commonUtil.getCurrentDate("MM");
				currYear = commonUtil.getCurrentDate("yyyy");
				log.info("currdate" + currdate);
				if (Integer.parseInt(currdate) < 4) {
					log.info("40");
					currYear = ("" + (Integer.parseInt(currYear) - 1)).trim();

				}
				log.info("41");
				log.info("currYear" + currYear);
				selCurrYear = currYear + "-" + (Integer.parseInt(currYear.substring(2, 4)) + 1);
				log.info("year....." + year);
				if (request.getParameter("year") != null) {
					log.info("42");
					if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 3) {
						year1 = Integer.parseInt(year.substring(0, 4)) + 1;
						log.info("43");
					} else {
						log.info("44");
						year1 = Integer.parseInt(year.substring(0, 4));
					}
				}
				monthYear = "01-" + month + "-" + year1;
				log.info("monthYear" + monthYear);

				log.info("45");
				try {

					log.info("46");
					if (flag.equals("F")) {
						log.info("47");
						if (!form.equals("chq")) {
							log.info("48");
							if (region.equals("CHQIAD")) {
								log.info("49");
								log.info("station" + station);
								station = session.getAttribute("station").toString();
								log.info("station" + station);
							}
						}
						log.info("50");
						monthYear = commonUtil.converDBToAppFormat(monthYear, "dd-MM-yyyy", "dd-MMM-yyyy");
						stationList = epfformsService.getStationList(region, monthYear, station);
						request.setAttribute("year", year);
						request.setAttribute("month", month);
						request.setAttribute("region", region);
					} else {
						log.info("51");
						request.setAttribute("month", currdate);
						request.setAttribute("year", selCurrYear);
					}
					// log.info("stationList"+stationList);
				} catch (Exception e) {
					log.info("52");
					e.printStackTrace();
				}

				request.setAttribute("monthYear", monthYear);
				request.setAttribute("stationList", stationList);
				if (session.getAttribute("usertype").toString().equals("Admin")) {
					log.info("53");
					rd = request.getRequestDispatcher(
							"./PensionView/aaiepfreports/StationWiseRemittanceInputParamsForChqNad.jsp");
				} else {
					log.info("54");
					rd = request
							.getRequestDispatcher("./PensionView/aaiepfreports/StationWiseRemittanceInputParams.jsp");
				}
			}

			log.info("55");
			rd.forward(request, response);
			log.info("55-1");
		} 
		
		
		
		
		else if (request.getParameter("method").equals("downloadexcel")) {
			log.info("56");
			String filename = "", folderfilePath = "", withoutfolderPathname = "";
			if (request.getParameter("filepath") != null) {
				log.info("57");
				filename = request.getParameter("filepath");

			}
			log.info("58");
			withoutfolderPathname = filename;
			ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
			folderfilePath = bundle.getString("cntrlacc.folder.path");
			filename = folderfilePath + "\\" + filename;
			File f = new File(filename);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + withoutfolderPathname + "\"");
			InputStream in = new FileInputStream(f);
			ServletOutputStream outs = response.getOutputStream();
			int bit = 256;
			try {
				log.info("59");
				while ((bit) >= 0) {
					log.info("60");
					bit = in.read();
					outs.write(bit);
				}
			} catch (IOException ioe) {
				log.info("61");
				ioe.printStackTrace(System.out);
			}
			log.info("62");
			outs.flush();
			outs.close();
			in.close();

		} 
		
		
		else if (request.getParameter("method").equals("updateStationWiseRemittance")) {
			log.info("63");
			String pfRemitDate = "", inspRemitDate = "", pcRemitDate = "", pf = "", insp = "", pc = "", editid = "",
					station = "", region = "", remittanceDate = "", accType = "", username = "", flag = "",
					remarks = "";

			if (request.getParameter("flag") != null) {
				log.info("64");
				flag = request.getParameter("flag");
			}
			if (request.getParameter("pfRemitDate") != null) {
				log.info("65");
				pfRemitDate = request.getParameter("pfRemitDate");
			}
			if (request.getParameter("inspRemitDate") != null) {
				log.info("66");
				inspRemitDate = request.getParameter("inspRemitDate");
			}
			if (request.getParameter("pcRemitDate") != null) {
				log.info("67");
				pcRemitDate = request.getParameter("pcRemitDate");
			}
			if (request.getParameter("pf") != null) {
				log.info("68");
				pf = request.getParameter("pf");
			}
			if (request.getParameter("insp") != null) {
				log.info("69");
				insp = request.getParameter("insp");
			}
			if (request.getParameter("pc") != null) {
				log.info("70");
				pc = request.getParameter("pc");
			}
			if (request.getParameter("editid") != null) {
				log.info("71");
				editid = request.getParameter("editid");
			}
			if (request.getParameter("monthyear") != null) {
				log.info("72");
				remittanceDate = request.getParameter("monthyear");
			}
			if (request.getParameter("station") != null) {
				log.info("73");
				station = request.getParameter("station");
			}
			if (request.getParameter("region") != null) {
				log.info("74");
				region = request.getParameter("region");
			}
			if (request.getParameter("acctype") != null) {
				log.info("75");
				accType = request.getParameter("acctype");
			}
			if (request.getParameter("remarks") != null) {
				log.info("76");
				remarks = request.getParameter("remarks");
			}
			if (session.getAttribute("userid") != null) {
				log.info("77");
				username = (String) session.getAttribute("userid");
			}
			log.info("78");
			epfformsService.updateStationWiseRemittance(pfRemitDate, inspRemitDate, pcRemitDate, pf, insp, pc, editid,
					remittanceDate, station, region, accType, username, flag, remarks);

			log.info(editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);
		} else if (request.getParameter("method").equals("loadStationWiseRemittanceReport")) {
			String monthyear = "", region = "", formType = "", flag = "", station = "", form = "", reportFlag = "",
					year = "", monthyear1 = "", month = "";
			log.info("79");
			RequestDispatcher rd = null;
			ArrayList stationList = null;
			int year1 = 0;

			log.info("80");
			if (request.getParameter("monthYear") != null) {
				log.info("81");
				monthyear = request.getParameter("monthYear");
			}
			if (request.getParameter("region") != null) {
				log.info("82");
				region = request.getParameter("region");
			}
			if (request.getParameter("formtype") != null) {
				log.info("83");
				formType = request.getParameter("formtype");
			}
			if (request.getParameter("flag") != null) {
				log.info("84");
				flag = request.getParameter("flag");
			}
			if (request.getParameter("form") != null) {
				log.info("84");
				form = request.getParameter("form");
			}
			if (request.getParameter("reportFlag") != null) {
				log.info("85");
				reportFlag = request.getParameter("reportFlag");
			}
			if (reportFlag.equals("R")) {
				log.info("86");
				if (request.getParameter("month") != null) {
					log.info("87");
					month = request.getParameter("month");
				}
				if (request.getParameter("year") != null) {
					log.info("88");
					year = request.getParameter("year");
				}
				if (request.getParameter("year") != null) {
					log.info("89");
					if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 3) {
						year1 = Integer.parseInt(year.substring(0, 4)) + 1;
						log.info("90");
					} else {
						year1 = Integer.parseInt(year.substring(0, 4));
						log.info("91");
					}
				}
				log.info("922");
				monthyear1 = "01-" + month + "-" + year1;
				try {
					log.info("93");
					log.info(monthyear1);
					monthyear = commonUtil.converDBToAppFormat(monthyear1, "dd-MM-yyyy", "dd-MMM-yyyy");
					log.info(monthyear);
				} catch (InvalidDataException e) {
					log.info("94");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				log.info("95");
				if (!form.equals("chq")) {
					log.info("96");
					if (region.equals("CHQIAD")) {
						log.info("97");
						station = session.getAttribute("station").toString();
					}
				}
				if (flag.equals("A")) {
					log.info("98");
					stationList = remittanceService.getStationList(region, monthyear, station);
				} else {
					log.info("99");
					stationList = epfformsService.getStationList(region, monthyear, station);
				}
			} catch (Exception e) {
				log.info("100");
				e.printStackTrace();
			}
			
			log.info("101");
			request.setAttribute("reportType", formType);
			request.setAttribute("region", region);
			request.setAttribute("monthYear", monthyear);
			request.setAttribute("stationList", stationList);
			if (flag.equals("N")) {

				log.info("102");
				rd = request
						.getRequestDispatcher("./PensionView/aaiepfreports/StationWiseRemittanceReportForChqNad.jsp");

			} else if (flag.equals("C")) {
				log.info("103");
				request.setAttribute("station", station);
				rd = request.getRequestDispatcher(
						"./PensionView/aaiepfreports/StationWiseRemittanceComponentWiseReport.jsp");
			} else if (flag.equals("A")) {
				log.info("104");
				request.setAttribute("station", station);
				rd = request
						.getRequestDispatcher("./PensionView/aaiepfreports/StationWiseRemittanceAirportWiseReport.jsp");
			} else {

				log.info("105");
				request.setAttribute("station", station);
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/StationWiseRemittanceReport.jsp");
			}
			log.info("105");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("loadStationWiseRemittanceReportForAllAirports")) {
			log.info("106");
			String monthyear = "", region = "", formType = "", flag = "", station = "", form = "", reportFlag = "",
					year = "", monthyear1 = "", month = "";
			RequestDispatcher rd = null;
			ArrayList stationList = null;
			int year1 = 0;

			if (request.getParameter("monthYear") != null) {
				log.info("107");
				monthyear = request.getParameter("monthYear");
			}
			if (request.getParameter("region") != null) {
				log.info("108");
				region = request.getParameter("region");
			}
			if (request.getParameter("formtype") != null) {
				log.info("109");
				formType = request.getParameter("formtype");
			}
			if (request.getParameter("flag") != null) {
				log.info("110");
				flag = request.getParameter("flag");
			}
			if (request.getParameter("form") != null) {
				log.info("111");
				form = request.getParameter("form");
			}
			if (request.getParameter("reportFlag") != null) {
				log.info("112");
				reportFlag = request.getParameter("reportFlag");
			}
			if (reportFlag.equals("R")) {
				log.info("113");
				if (request.getParameter("month") != null) {
					log.info("114");
					month = request.getParameter("month");
				}
				if (request.getParameter("year") != null) {
					log.info("115");
					year = request.getParameter("year");
				}
				if (request.getParameter("year") != null) {
					log.info("116");
					if (Integer.parseInt(month) >= 1 && Integer.parseInt(month) <= 3) {
						year1 = Integer.parseInt(year.substring(0, 4)) + 1;
						log.info("117");
					} else {
						log.info("118");
						year1 = Integer.parseInt(year.substring(0, 4));
					}
				}
				log.info("118");
				monthyear1 = "01-" + month + "-" + year1;
				try {
					log.info("119");
					log.info(monthyear1);
					monthyear = commonUtil.converDBToAppFormat(monthyear1, "dd-MM-yyyy", "dd-MMM-yyyy");
					log.info(monthyear);
				} catch (InvalidDataException e) {
					log.info("120");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				log.info("121");
				if (!form.equals("chq")) {
					log.info("122");
					if (region.equals("CHQIAD")) {
						log.info("123");
						station = session.getAttribute("station").toString();
					}
				}
				log.info("124");
				stationList = epfformsService.getStationListForAllAirports(region, monthyear, station);

			} catch (Exception e) {
				log.info("125");
				e.printStackTrace();
			}
			log.info("126");
			request.setAttribute("reportType", formType);
			request.setAttribute("region", region);
			request.setAttribute("monthYear", monthyear);
			request.setAttribute("stationList", stationList);
			log.info("127");
			rd = request
					.getRequestDispatcher("./PensionView/aaiepfreports/StationWiseRemittanceReportForAllAirports.jsp");
			log.info("128");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("epf3report")) {
			log.info("129");
			String region = "", regionDesc = "", year = "", month = "", monthDesc = "", toYear = "", shnYear = "";
			String toMonth = "", suppliFlag = "S", arrearFlag = "A";
			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();

			ArrayList epfForm3SuppliList = new ArrayList();
			ArrayList epfForm3SuppliList_Prev = new ArrayList();
			ArrayList epfForm3SuppliList_Reg = new ArrayList();
			ArrayList epfForm3ArrearList = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				log.info("130");
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				log.info("131");
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				log.info("132");
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_to_month") != null) {
				log.info("133");
				toMonth = request.getParameter("frm_to_month");
			}

			if (request.getParameter("frm_airportcode") != null) {
				log.info("134");
				airportcode = request.getParameter("frm_airportcode");
			}

			if (request.getParameter("frm_region") != null) {
				log.info("135");
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_pfids") != null) {
				log.info("136");
				pfidString = request.getParameter("frm_pfids");
			} else {
				log.info("137");
				pfidString = "";
			}
			if (request.getParameter("sortingOrder") != null) {
				log.info("138");
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				log.info("139");
				sortingOrder = "PENSIONNO";
			}

			if (request.getParameter("frm_emp_flag") != null) {
				log.info("140");
				empflag = request.getParameter("frm_emp_flag");
			}
			if (request.getParameter("frm_empnm") != null) {
				log.info("141");
				empName = request.getParameter("frm_empnm");
			}
			if (request.getParameter("frm_pensionno") != null) {
				log.info("142");
				pensionno = request.getParameter("frm_pensionno");
			}
			if (request.getParameter("frm_reportType") != null) {
				log.info("143");
				reportType = request.getParameter("frm_reportType");
			}
			try {
				log.info("144");
				// frmSelectedDts=this.getFromToDates(year,toYear,month,toMonth);
				frmSelectedDts = this.getFromToDatesForForm3(year, toYear, month, toMonth);

			} catch (InvalidDataException e) {
				log.info("145");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info("146");
			epfForm3List = epfformsService.loadEPFForm3Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedDts, sortingOrder, pensionno);
			if (year.equals("2008") || (toYear.equals("2008"))) {
				log.info("147");
				missingPFIDsList = epfformsService.loadEPFMissingTransPFIDs(pfidString, region, airportcode, empName,
						empflag, frmSelectedDts, sortingOrder, pensionno);
			}
			log.info("148");
			epfForm3SuppliList = epfformsService.loadEPFForm3SippliBlockReport(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts, sortingOrder, pensionno, suppliFlag);
			epfForm3ArrearList = epfformsService.loadEPFForm3SippliBlockReport(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts, sortingOrder, pensionno, arrearFlag);
			// Seperation of Regular Salary Submitted as Suppli and Normal Suppli For Prev
			// Months
			for (int i = 0; i < epfForm3SuppliList.size(); i++) {
				log.info("149");
				epfForm3SuppliList_Reg = (ArrayList) epfForm3SuppliList.get(0);
				epfForm3SuppliList_Prev = (ArrayList) epfForm3SuppliList.get(1);
			}

			log.info("150");
			request.setAttribute("cardList", epfForm3List);
			request.setAttribute("suppliList_reg", epfForm3SuppliList_Reg);
			request.setAttribute("suppliList_prev", epfForm3SuppliList_Prev);
			request.setAttribute("arrearList", epfForm3ArrearList);
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
			String dispFromYear = "", dispToYear = "", getFromDate = "", getToDate = "";
			String[] selectedYears = frmSelectedDts.split(",");
			getFromDate = selectedYears[0];
			getToDate = selectedYears[1];
			/*
			 * try { monthDesc=commonUtil.converDBToAppFormat("01-"+month+"-"+commonUtil.
			 * getCurrentDate("yyyy"),"dd-MM-yyyy","MMM"); } catch (InvalidDataException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			try {
				dispFromYear = commonUtil.converDBToAppFormat(getFromDate, "dd-MMM-yyyy", "MMM-yyyy");
				dispToYear = commonUtil.converDBToAppFormat(getToDate, "dd-MMM-yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monthDesc = "From " + dispFromYear + " To " + dispToYear;
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("totalSub", totalSub);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", monthDesc);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-3Report.jsp");
			rd.forward(request, response);
		}

		// ---------------venkatesh

		else if (request.getParameter("method").equals("epf5CADreport")) {

			log.info("satya===============>1111");
			String region = "", regionDesc = "", year = "", month = "", monthDesc = "", toYear = "", shnYear = "";
			String toMonth = "", suppliFlag = "S", arrearFlag = "A";
			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();
			ArrayList epfForm5CadList = new ArrayList();
			ArrayList epfForm3SuppliList = new ArrayList();
			ArrayList epfForm3SuppliList_Prev = new ArrayList();
			ArrayList epfForm3SuppliList_Reg = new ArrayList();
			ArrayList epfForm3ArrearList = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_to_month") != null) {
				toMonth = request.getParameter("frm_to_month");
			}

			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
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
				// frmSelectedDts=this.getFromToDates(year,toYear,month,toMonth);
				frmSelectedDts = this.getFromToDatesForForm3(year, toYear, month, toMonth);

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// epfForm3List=epfformsService.loadEPFForm3Report(pfidString,region,airportcode,empName,empflag,frmSelectedDts,sortingOrder,pensionno);
			// epfForm5CadList=epfformsService.loadEPFForm5CadReport(pfidString,region,airportcode,empName,empflag,frmSelectedDts,sortingOrder,pensionno);

			if (year.equals("2008") || (toYear.equals("2008"))) {
				missingPFIDsList = epfformsService.loadEPFMissingTransPFIDs(pfidString, region, airportcode, empName,
						empflag, frmSelectedDts, sortingOrder, pensionno);
			}
			// epfForm3SuppliList=epfformsService.loadEPFForm3SippliBlockReport(pfidString,region,airportcode,empName,empflag,frmSelectedDts,sortingOrder,pensionno,suppliFlag);
			// epfForm3ArrearList=epfformsService.loadEPFForm3SippliBlockReport(pfidString,region,airportcode,empName,empflag,frmSelectedDts,sortingOrder,pensionno,arrearFlag);
			// Seperation of Regular Salary Submitted as Suppli and Normal Suppli For Prev
			// Months
			for (int i = 0; i < epfForm3SuppliList.size(); i++) {
				epfForm3SuppliList_Reg = (ArrayList) epfForm3SuppliList.get(0);
				epfForm3SuppliList_Prev = (ArrayList) epfForm3SuppliList.get(1);
			}

			request.setAttribute("cardList", epfForm5CadList);
			request.setAttribute("suppliList_reg", epfForm3SuppliList_Reg);
			request.setAttribute("suppliList_prev", epfForm3SuppliList_Prev);
			request.setAttribute("arrearList", epfForm3ArrearList);
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
			String dispFromYear = "", dispToYear = "", getFromDate = "", getToDate = "";
			String[] selectedYears = frmSelectedDts.split(",");
			getFromDate = selectedYears[0];
			getToDate = selectedYears[1];
			/*
			 * try { monthDesc=commonUtil.converDBToAppFormat("01-"+month+"-"+commonUtil.
			 * getCurrentDate("yyyy"),"dd-MM-yyyy","MMM"); } catch (InvalidDataException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			try {
				dispFromYear = commonUtil.converDBToAppFormat(getFromDate, "dd-MMM-yyyy", "MMM-yyyy");
				dispToYear = commonUtil.converDBToAppFormat(getToDate, "dd-MMM-yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monthDesc = "From " + dispFromYear + " To " + dispToYear;
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("totalSub", totalSub);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", monthDesc);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-5-CadReport.jsp");
			rd.forward(request, response);
		}

		// ------------venkatesh

		else if (request.getParameter("method").equals("executivereport")) {
			String region = "", regionDesc = "", year = "", month = "", monthDesc = "", toYear = "", shnYear = "";
			String toMonth = "", suppliFlag = "S", arrearFlag = "A";
			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();

			ArrayList epfForm3SuppliList = new ArrayList();
			ArrayList epfForm3SuppliList_Prev = new ArrayList();
			ArrayList epfForm3SuppliList_Reg = new ArrayList();
			ArrayList epfForm3ArrearList = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_to_month") != null) {
				toMonth = request.getParameter("frm_to_month");
			}

			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
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
				// frmSelectedDts=this.getFromToDates(year,toYear,month,toMonth);
				frmSelectedDts = this.getFromToDatesForForm3(year, toYear, month, toMonth);

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			epfForm3List = epfformsService.loadExecutiveReport(pfidString, region, airportcode, empName, empflag,
					frmSelectedDts, sortingOrder, pensionno);

			request.setAttribute("cardList", epfForm3List);

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
			String dispFromYear = "", dispToYear = "", getFromDate = "", getToDate = "";
			String[] selectedYears = frmSelectedDts.split(",");
			getFromDate = selectedYears[0];
			getToDate = selectedYears[1];
			/*
			 * try { monthDesc=commonUtil.converDBToAppFormat("01-"+month+"-"+commonUtil.
			 * getCurrentDate("yyyy"),"dd-MM-yyyy","MMM"); } catch (InvalidDataException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */
			try {
				dispFromYear = commonUtil.converDBToAppFormat(getFromDate, "dd-MMM-yyyy", "MMM-yyyy");
				dispToYear = commonUtil.converDBToAppFormat(getToDate, "dd-MMM-yyyy", "MMM-yyyy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			monthDesc = "From " + dispFromYear + " To " + dispToYear;
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("totalSub", totalSub);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", monthDesc);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/ExecutivesReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF1Report")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "",
					disMonthYear = "";
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "", nextyear = "", toYear = "",
					frmSelectedDts = "";
			AAIEPFReportBean AAIEPFBean = new AAIEPFReportBean();
			ArrayList list1 = new ArrayList();
			ArrayList list2 = new ArrayList();
			ArrayList pendinglist1 = new ArrayList();
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
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
				sortingOrder = "pensionno";
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
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

			try {
				frmSelectedDts = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info("===AAIEPF1Report=====" + region + "year" + year + "empflag=========" + empflag + "pfidString"
					+ pfidString + "pensionno" + pensionno);

			AAIEPFBean = epfformsService.AAIEPFForm1Report(pfidString, region, airportcode, frmSelectedDts, empflag,
					empName, sortingOrder, pensionno);

			if (AAIEPFBean.getReportList1() != null) {
				list1 = AAIEPFBean.getReportList1();
			}
			if (AAIEPFBean.getReportList2() != null) {
				list2 = AAIEPFBean.getReportList2();
			}
			if (AAIEPFBean.getReportList3() != null) {
				pendinglist1 = AAIEPFBean.getReportList3();
			}

			if (list1.size() > 0)
				request.setAttribute("AAIEPF1List", list1);
			log.info("---------list1  SIZE in Action-----------" + list1.size());

			if (list2.size() > 0)
				request.setAttribute("AAIEPF1List2", list2);
			log.info("---------list2  SIZE in Action-----------" + list2.size());
			if (pendinglist1.size() > 0)
				request.setAttribute("AAIEPF1PendingList1", pendinglist1);
			if (!year.equals("NO-SELECT")) {
				nextyear = year.substring(2, 4);

				nextyear = Integer.toString(Integer.parseInt(nextyear) + 1);

				if (Integer.parseInt(nextyear) <= 9)
					nextyear = "0" + nextyear;
			} else {
				year = "1995";
				nextyear = commonUtil.getCurrentDate("yy");
			}
			String dispOBYear = "01.04." + year;
			year = year + "-" + nextyear;

			request.setAttribute("finYear", year);
			request.setAttribute("dispOBYear", dispOBYear);
			if (!region.equals("NO-SELECT")) {
				request.setAttribute("region", region);
			} else {
				request.setAttribute("region", "-----");
			}
			request.setAttribute("reportType", reportType);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-1Report.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF2Report")) {
			String region = "", year = "", month = "", adjtype = "", selectedDate = "", frmMonthYear = "",
					displayDate = "", disMonthYear = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "", nextyear = "", toYear = "",
					frmSelectedDts = "", statusFlag = "N", statusType = "";

			ArrayList list = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}

			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}

			if (request.getParameter("frm_adjtype") != null) {
				adjtype = request.getParameter("frm_adjtype");
			} else {
				adjtype = "";
			}
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			} else {
				pfidString = "";
			}
			if (!request.getParameter("sortingOrder").equals("")) {
				sortingOrder = request.getParameter("sortingOrder");
			} else {
				sortingOrder = "pensionno";
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
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
			if (request.getParameter("statusFlag") != null) {
				statusFlag = request.getParameter("statusFlag");
			}
			if (request.getParameter("statusType") != null) {
				statusType = request.getParameter("statusType");
			}

			try {
				frmSelectedDts = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info("===AAIEPF1Report=====" + region + "year" + year + "empflag=========" + empflag + "pfidString"
					+ pfidString + "pensionno" + pensionno);

			list = epfformsService.AAIEPFForm2Report(pfidString, region, airportcode, frmSelectedDts, empflag, empName,
					sortingOrder, pensionno, adjtype, statusFlag, statusType);

			if (list.size() > 0)
				request.setAttribute("AAIEPF2List", list);

			if (year.equals("NO-SELECT")) {
				year = "---";
			}
			request.setAttribute("adjtype", adjtype);
			request.setAttribute("finYear", year);

			if (!region.equals("NO-SELECT")) {
				request.setAttribute("region", region);
			} else {
				request.setAttribute("region", "-----");
			}
			request.setAttribute("reportType", reportType);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-2Report.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("AAIEPF4Report")) {

			String region = "", regionDesc = "", year = "", month = "", monthDesc = "", toYear = "", shnYear = "",
					finYear = "";
			String toMonth = "", select_month = "";
			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm4List = new ArrayList();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_to_month") != null) {
				toMonth = request.getParameter("frm_to_month");
			}

			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
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
			if (request.getParameter("select_month") != null) {
				select_month = request.getParameter("select_month");
			}
			if (select_month.equals("NO-SELECT")) {
				select_month = "";
			}
			try {
				// frmSelectedDts=this.getFromToDates(year,toYear,month,toMonth);
				frmSelectedDts = this.getFromToDatesForForm3(year, toYear, month, toMonth);

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			epfForm4List = epfformsService.loadEPFForm4Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedDts, sortingOrder, pensionno, select_month);

			request.setAttribute("cardList", epfForm4List);

			if (!year.equals("NO-SELECT")) {
				shnYear = year + "-" + toYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}

			if (epfForm4List.size() != 0) {
				totalSub = Integer.toString(epfForm4List.size());
			} else {
				totalSub = "0";
			}
			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}
			String dispFromYear = "", dispToYear = "", getFromDate = "", getToDate = "";
			String[] selectedYears = frmSelectedDts.split(",");
			getFromDate = selectedYears[0];
			getToDate = selectedYears[1];
			/*
			 * try { monthDesc=commonUtil.converDBToAppFormat("01-"+month+"-"+commonUtil.
			 * getCurrentDate("yyyy"),"dd-MM-yyyy","MMM"); } catch (InvalidDataException e)
			 * { // TODO Auto-generated catch block e.printStackTrace(); }
			 */

			finYear = year + "-" + toYear;
			request.setAttribute("finYear", finYear);

			log.info("--getFromDate---" + getFromDate + "-getToDate-----" + getToDate);
			try {
				if (!select_month.equals("")) {
					if (Integer.parseInt(select_month) > 3) {
						dispFromYear = commonUtil.converDBToAppFormat(select_month + "-" + year, "MM-yyyy", "MMM-yyyy");
					} else {
						dispFromYear = commonUtil.converDBToAppFormat(select_month + "-" + toYear, "MM-yyyy",
								"MMM-yyyy");
					}
				} else {
					dispFromYear = commonUtil.converDBToAppFormat(getFromDate, "dd-MMM-yyyy", "MMM-yyyy");
					dispToYear = commonUtil.converDBToAppFormat(getToDate, "dd-MMM-yyyy", "MMM-yyyy");
				}
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("--dispFromYear---" + dispFromYear + "-dispToYear-----" + dispToYear);
			if (!select_month.equals("")) {
				monthDesc = "OF " + dispFromYear;
			} else {
				monthDesc = "From " + dispFromYear + " To " + dispToYear;
			}
			log.info("monthDesc:::::" + monthDesc);

			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("totalSub", totalSub);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", monthDesc);
			request.setAttribute("reportType", reportType);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-4Report.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("AAIEPF8Report")) {
			String region = "", year = "", month = "", selectedDate = "", frmMonthYear = "", displayDate = "",
					disMonthYear = "";
			ArrayList airportList = new ArrayList();
			String airportcode = "", reportType = "", sortingOrder = "", pfidString = "", nextyear = "", toYear = "",
					frmSelectedDts = "", formType = "", path = "";

			ArrayList list = new ArrayList();
			ArrayList form8SummaryList = new ArrayList();
			AAIEPFReportBean aaiEPFBean = new AAIEPFReportBean();

			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_ToYear") != null) {
				toYear = request.getParameter("frm_ToYear");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
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
				sortingOrder = "pensionno";
			}
			String empName = "", monthDesc = "";
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
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_formType") != null) {
				formType = request.getParameter("frm_formType");
			}
			try {
				frmSelectedDts = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log.info("===AAIEPF8Report=====" + region + "year" + year + "empflag=========" + empflag + "pfidString"
					+ pfidString + "pensionno" + pensionno);

			if (formType.equals("AAI EPF-8") || formType.equals("AAI EPF-8ExcludePC")) {
				aaiEPFBean = epfformsService.AAIEPFForm8Report(pfidString, region, airportcode, frmSelectedDts, empflag,
						empName, sortingOrder, pensionno);

				if (!aaiEPFBean.equals(""))
					request.setAttribute("aaiEPFBean", aaiEPFBean);

				log.info("---------aaiEPFBean in Action-----------" + aaiEPFBean);
				if (formType.equals("AAI EPF-8")) {
					path = "./PensionView/aaiepfreports/AAIEPF-8Report.jsp";
				} else {
					path = "./PensionView/aaiepfreports/AAIEPF-8ExcludePCReport.jsp";
				}
			} else {
				log.info("=================FORM 8 SUMMARY REPORT =======");
				// call procedure
				form8SummaryList = epfformsService.AAIEPFForm8SummaryReport(pfidString, region, airportcode,
						frmSelectedDts, empflag, empName, sortingOrder, pensionno);
				if (form8SummaryList.size() > 0) {
					request.setAttribute("form8SummaryList", form8SummaryList);
				}
				path = "./PensionView/aaiepfreports/AAIEPF-8SummaryReport.jsp";

			}

			if (!year.equals("NO-SELECT")) {
				nextyear = year.substring(2, 4);

				nextyear = Integer.toString(Integer.parseInt(nextyear) + 1);

				if (Integer.parseInt(nextyear) <= 9)
					nextyear = "0" + nextyear;
			} else {
				year = "1995";
				nextyear = commonUtil.getCurrentDate("yy");
			}

			year = year + "-" + nextyear;
			request.setAttribute("finYear", year);

			if (!region.equals("NO-SELECT")) {
				request.setAttribute("region", region);
			} else {
				request.setAttribute("region", "-----");
			}

			try {
				monthDesc = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
						"dd-MM-yyyy", "MMM");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!month.equals("NO-SELECT")) {
				request.setAttribute("month", monthDesc);
			} else {
				request.setAttribute("month", "-----");
			}
			log.info("===========Path======" + path);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF5Report")) {

			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "",
					frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", monthDesc = "", pfidString = "",
					empName = "", empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList epfForm5SuppliList = new ArrayList();
			ArrayList epfForm5ArrearList = new ArrayList();

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
			try {
				frmSelectedDts = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("===AAI EPF-5=====" + region + "year" + frmSelectedDts + "empflag=========" + empflag
					+ "pfidString" + pfidString + "pensionno" + pensionno);
			epfForm3List = epfformsService.loadEPFForm5Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedDts, sortingOrder, pensionno);
			request.setAttribute("cardList", epfForm3List);
			epfForm5SuppliList = epfformsService.loadEPFForm5SuppliBlockReport(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts, sortingOrder, pensionno);
			request.setAttribute("suppliList", epfForm5SuppliList);
			epfForm5ArrearList = epfformsService.loadArrearEPF5BlockReport(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts, sortingOrder, pensionno);
			request.setAttribute("arrearList", epfForm5ArrearList);

			if (!year.equals("NO-SELECT")) {
				shnYear = year + "-" + toYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}
			if (!month.equals("NO-SELECT")) {
				try {
					monthDesc = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
							"dd-MM-yyyy", "MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("dspMonth", monthDesc);
			}
			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}

			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);

			request.setAttribute("dspYear", shnYear);

			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-5Report.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF11Report")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "",
					frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "", status = "", reportChkType = "";
			String username = "", computername = "", dbFlag = "insert", formType = "Form-11", trackingId = "";

			AaiEpfForm11Bean epfForm11Bean = new AaiEpfForm11Bean();
			ArrayList epfForm3List = new ArrayList();

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
			if (request.getParameter("frm_statusType") != null) {
				status = request.getParameter("frm_statusType");
			}
			if (request.getParameter("frm_reportChkType") != null) {
				reportChkType = request.getParameter("frm_reportChkType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			String[] yearList = year.split("-");
			String frmSelectedYear = yearList[0] + "-" + (Integer.parseInt(yearList[0]) + 1);

			username = (String) session.getAttribute("userid");
			epfForm11Bean.setRegion(region);
			epfForm11Bean.setAirportCode(airportcode);
			epfForm11Bean.setUserName(username);
			epfForm11Bean.setComputerName(computername);
			epfForm11Bean.setReportChkType(reportChkType);
			epfForm11Bean.setSelectedYear(frmSelectedYear);
			epfForm11Bean.setFormType(formType);
			trackingId = commonDAO.getCtrlTrackingId(epfForm11Bean, dbFlag);
			log.info("===AAI EPF-11=====" + region + "year" + frmSelectedDts + "empflag=========" + empflag
					+ "pfidString" + pfidString + "pensionno" + pensionno);
			log.info("sortingOrder===================" + sortingOrder);

			log.info("===AAI EPF-11=====Year" + frmSelectedYear);
			epfForm3List = epfformsService.loadEPFForm11Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedYear, sortingOrder, pensionno, status);
			request.setAttribute("cardList", epfForm3List);
			if (epfForm3List.size() != 0) {
				totalSub = Integer.toString(epfForm3List.size());
			} else {
				totalSub = "0";
			}
			if (!year.equals("NO-SELECT")) {
				shnYear = frmSelectedYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}

			if (!month.equals("NO-SELECT")) {
				try {
					month = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
							"dd-MM-yyyy", "MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("dspMonth", month);
			}
			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}

			request.setAttribute("totalSub", totalSub);
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("reportType", reportType);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("trackingId", trackingId);
			request.setAttribute("reportChkType", reportChkType);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-11Report.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF12Report")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "",
					frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "", status = "";
			String username = "", computername = "", dbFlag = "insert", formType = "Form-12", trackingId = "",
					reportChkType = "";
			AaiEpfForm11Bean epfForm11Bean = new AaiEpfForm11Bean();
			ArrayList epfForm3List = new ArrayList();

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
			if (request.getParameter("frm_statusType") != null) {
				status = request.getParameter("frm_statusType");
			}
			if (request.getParameter("frm_reportChkType") != null) {
				reportChkType = request.getParameter("frm_reportChkType");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			String[] yearList = year.split("-");
			String frmSelectedYear = yearList[0] + "-" + (Integer.parseInt(yearList[0]) + 1);

			username = (String) session.getAttribute("userid");
			epfForm11Bean.setRegion(region);
			epfForm11Bean.setAirportCode(airportcode);
			epfForm11Bean.setUserName(username);
			epfForm11Bean.setComputerName(computername);
			epfForm11Bean.setReportChkType(reportChkType);
			epfForm11Bean.setFormType(formType);
			epfForm11Bean.setSelectedYear(frmSelectedYear);

			trackingId = commonDAO.getCtrlTrackingId(epfForm11Bean, dbFlag);
			log.info("===AAI EPF-12=====" + region + "year" + frmSelectedDts + "empflag=========" + empflag
					+ "pfidString" + pfidString + "pensionno" + pensionno);
			log.info("sortingOrder===================" + sortingOrder);

			log.info("===AAI EPF-12=====Year" + frmSelectedYear);
			epfForm3List = epfformsService.loadEPFForm11Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedYear, sortingOrder, pensionno, status);
			request.setAttribute("cardList", epfForm3List);
			if (epfForm3List.size() != 0) {
				totalSub = Integer.toString(epfForm3List.size());
			} else {
				totalSub = "0";
			}
			if (!year.equals("NO-SELECT")) {
				shnYear = frmSelectedYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}

			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}
			if (!month.equals("NO-SELECT")) {
				try {
					month = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
							"dd-MM-yyyy", "MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("dspMonth", month);
			}

			request.setAttribute("totalSub", totalSub);
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("reportType", reportType);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("frmYear", yearList[0]);
			request.setAttribute("trackingId", trackingId);
			request.setAttribute("reportChkType", reportChkType);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-12Report.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("AAIEPF6Report")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "",
					frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", frmSelectedDts1 = "", reportType = "", totalSub = "", sortingOrder = "",
					pfidString = "", empName = "", empflag = "false", pensionno = "", frmSelectedDts = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			frmSelectedDts = year + "-" + Integer.toString(Integer.parseInt(year) + 1);
			toYear = Integer.toString(Integer.parseInt(year) + 1);
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
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

			log.info("===AAI EPF-6=====" + region + "year" + frmSelectedDts + "empflag=========" + empflag
					+ "pfidString" + pfidString + "pensionno" + pensionno);
			epfForm3List = epfformsService.loadEPFForm6Report(pfidString, region, airportcode, empName, empflag,
					frmSelectedDts, sortingOrder, pensionno);
			try {
				frmSelectedDts1 = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
			}
			missingPFIDsList = epfformsService.loadEPFMissingTransPFIDs(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts1, sortingOrder, pensionno);
			request.setAttribute("cardList", epfForm3List);
			request.setAttribute("missingPFIDsList", missingPFIDsList);
			if (!year.equals("NO-SELECT")) {
				shnYear = frmSelectedDts;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}
			if (!month.equals("NO-SELECT")) {
				try {
					month = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
							"dd-MM-yyyy", "MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("dspMonth", month);
			}

			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}

			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);

			request.setAttribute("dspYear", shnYear);

			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-6Report.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("AAIEPF7Report")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", selectedDate = "",
					frmMonthYear = "", shnYear = "", displayDate = "", disMonthYear = "";

			String airportcode = "", reportType = "", totalSub = "", sortingOrder = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			String frmSelectedDts1 = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList missingPFIDsList = new ArrayList();
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			frmSelectedDts = year + "-" + Integer.toString(Integer.parseInt(year) + 1);
			toYear = Integer.toString(Integer.parseInt(year) + 1);
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
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

			log.info("===AAI EPF-7=====" + region + "year" + frmSelectedDts + "empflag=========" + empflag
					+ "pfidString" + pfidString + "pensionno" + pensionno);
			epfForm3List = epfformsService.loadEPFForm7Report(region, airportcode, frmSelectedDts, sortingOrder);
			try {
				frmSelectedDts1 = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
			}
			missingPFIDsList = epfformsService.loadEPFMissingTransPFIDs(pfidString, region, airportcode, empName,
					empflag, frmSelectedDts1, sortingOrder, pensionno);
			request.setAttribute("missingPFIDsList", missingPFIDsList);
			request.setAttribute("cardList", epfForm3List);
			if (!year.equals("NO-SELECT")) {
				shnYear = frmSelectedDts;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}
			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}
			try {
				month = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
						"dd-MM-yyyy", "MMM");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("dspYear", shnYear);
			request.setAttribute("dspMonth", month);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AAIEPF-7Report.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("remitancescreen")) {
			String region = "", year = "", month = "", airportcode = "";
			String salaryMonth = "";
			ArrayList remitanceList = new ArrayList();
			ArrayList rauRemList = new ArrayList();
			ArrayList remitancetableList = new ArrayList();
			ArrayList rauRemitancetableList = new ArrayList();
			ArrayList noofEmployeesList = new ArrayList();
			ArrayList rauNoofEmployeesList = new ArrayList();
			String remitanceType = "", accountType = "", vAcccountNO = "";
			ArrayList airportList = new ArrayList();
			double epf8Totals = 0.00;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("remitancetype") != null) {
				remitanceType = request.getParameter("remitancetype");
			}
			if (request.getParameter("accounttype") != null) {
				accountType = request.getParameter("accounttype");
			}
			if (accountType.equals("RAU")) {
				airportList = commonDAO.getAirportsByPersonalTbl(region, accountType, "");
			}

			try {
				salaryMonth = commonUtil.converDBToAppFormat("01-" + month + "-" + year, "dd-MM-yyyy", "dd-MMM-yyyy");
				if (accountType.equals("SAU")) {
					airportList = commonDAO.getAirportsByPersonalTbl(region, accountType, airportcode);
					if (airportList.size() >= 0) {
						EmpMasterBean empmstbean = (EmpMasterBean) airportList.get(0);
						vAcccountNO = empmstbean.getVAccountNo();
					}

					remitanceList = epfformsService.loadRemitanceInfo(salaryMonth, region, airportcode, remitanceType,
							accountType);
					noofEmployeesList = epfformsService.getNoOfEmployees(salaryMonth, region, airportcode,
							remitanceType);
					remitancetableList = epfformsService.loadRemitanceTableInfo(salaryMonth, region, airportcode,
							remitanceType, "screen", accountType);
					epf8Totals = epfformsService.getAaiEpf8totals(salaryMonth, region, airportcode);
				} else if (airportList.size() > 0 && accountType.equals("RAU")) {
					for (int i = 0; i < airportList.size(); i++) {
						EmpMasterBean empmstbean = (EmpMasterBean) airportList.get(i);
						vAcccountNO = empmstbean.getVAccountNo();
						rauRemList = epfformsService.loadRemitanceInfo(salaryMonth, region, empmstbean.getStation(),
								remitanceType, accountType);
						remitanceList.addAll(rauRemList);
						rauNoofEmployeesList = epfformsService.getNoOfEmployees(salaryMonth, region,
								empmstbean.getStation(), remitanceType);
						noofEmployeesList.addAll(rauNoofEmployeesList);
						rauRemitancetableList = epfformsService.loadRemitanceTableInfo(salaryMonth, region,
								empmstbean.getStation(), remitanceType, "screen", accountType);
						remitancetableList.addAll(rauRemitancetableList);
						epf8Totals = epfformsService.getAaiEpf8totals(salaryMonth, region, empmstbean.getStation());
						// log.info(" Remittance list" +remitanceList.size());
					}

				}
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// holding the airport, region attributes

			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			ArrayList penYearList = new ArrayList();
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
			request.setAttribute("monthIterator", monthIterator);
			HashMap regionHashmap = new HashMap();

			String[] regionLst = null;
			String rgnName = "";
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS") && session.getAttribute("usertype").toString().equals("Admin")) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					break;
				} else {
					regionHashmap.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("salaryMonth", salaryMonth);
			request.setAttribute("region", region);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("remitanceType", remitanceType);
			request.setAttribute("regionHashmap", regionHashmap);
			request.setAttribute("remitanceList", remitanceList);
			request.setAttribute("remitancetableList", remitancetableList);
			request.setAttribute("noofEmployeesList", noofEmployeesList);
			request.setAttribute("epf8Totals", String.valueOf(epf8Totals));
			request.setAttribute("accountType", accountType);
			request.setAttribute("vAcccountNO", vAcccountNO);

			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/RemittanceScreen.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("remitanceReport")) {
			String region = "", reportType = "", year = "", month = "", airportcode = "";
			String salaryMonth = "";
			ArrayList remitanceList = null;
			ArrayList remitancetableList = null;
			ArrayList grandtotalsLIst = null;
			ArrayList noofEmployeesList = null;
			String remitanceType = "", accountType = "";
			double epf8Totals = 0.0;
			if (request.getParameter("frm_year") != null) {
				year = request.getParameter("frm_year");
			}
			if (request.getParameter("frm_month") != null) {
				month = request.getParameter("frm_month");
			}
			if (request.getParameter("frm_airportcode") != null) {
				airportcode = request.getParameter("frm_airportcode");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("remitancetype") != null) {
				remitanceType = request.getParameter("remitancetype");
			}
			if (request.getParameter("accounttype") != null) {
				accountType = request.getParameter("accounttype");
			}

			try {
				salaryMonth = commonUtil.converDBToAppFormat("01-" + month + "-" + year, "dd-MM-yyyy", "dd-MMM-yyyy");
				// remitanceList=epfformsService.loadRemitanceInfo(salaryMonth,region,airportcode,remitanceType);
				remitancetableList = epfformsService.loadRemitanceTableInfo(salaryMonth, region.trim(),
						airportcode.trim(), remitanceType, "report", accountType);
				// epf8Totals=epfformsService.getAaiEpf8totals(salaryMonth,region,airportcode);
				// grandtotalsLIst=epfformsService.getGrandtotalsRegionwise(salaryMonth,region.trim(),airportcode.trim(),remitanceType);
				System.out.println("remitancetableList size" + remitancetableList.size());

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// holding the airport, region attributes
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			ArrayList penYearList = new ArrayList();
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
			request.setAttribute("monthIterator", monthIterator);
			HashMap regionHashmap = new HashMap();

			String[] regionLst = null;
			String rgnName = "";
			if (session.getAttribute("region") != null) {
				regionLst = (String[]) session.getAttribute("region");
			}
			log.info("Regions List" + regionLst.length);
			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS") && session.getAttribute("usertype").toString().equals("Admin")) {
					regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					break;
				} else {
					regionHashmap.put(new Integer(i), rgnName);
				}

			}
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("salaryMonth", salaryMonth);
			request.setAttribute("region", region);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("remitanceType", remitanceType);
			request.setAttribute("regionHashmap", regionHashmap);
			request.setAttribute("remitanceList", remitanceList);
			request.setAttribute("remitancetableList", remitancetableList);
			request.setAttribute("grandtotalsLIst", grandtotalsLIst);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/aaiepfreports/RemittanceReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("remitancedataUpdate")) {
			RemittanceBean rbean = new RemittanceBean();

			if (request.getParameter("vrno") != null) {
				rbean.setVrNo(request.getParameter("vrno"));
			}
			if (request.getParameter("vrdt") != null) {
				rbean.setVrDt(request.getParameter("vrdt"));
			}
			try {
				if (rbean.getVrDt().indexOf("-") == -1) {
					throw new Exception("Invalid Date");
				}
			} catch (Exception e) {
				log.info(e.getMessage());
			}

			if (request.getParameter("pfaccretion") != null) {
				rbean.setPfAccretion(Double.parseDouble(request.getParameter("pfaccretion")));
			}
			if (request.getParameter("pfremitterbanknm") != null) {
				rbean.setPfRemitterBankNM(request.getParameter("pfremitterbanknm"));
			}
			if (request.getParameter("pfremitterbankacno") != null) {
				rbean.setPfRemitterBankACNo(request.getParameter("pfremitterbankacno"));
			}
			if (request.getParameter("pfnoofemployees") != null) {
				rbean.setPfnoofEmployees(Integer.parseInt(request.getParameter("pfnoofemployees")));
			}

			if (request.getParameter("emoluments") != null) {
				rbean.setEmoluments(Double.parseDouble(request.getParameter("emoluments")));
			}
			if (request.getParameter("inspectioncharges") != null) {
				rbean.setInspectionCharges(Double.parseDouble(request.getParameter("inspectioncharges")));
			}
			if (request.getParameter("pcnoofemployees") != null) {
				rbean.setPcNoofEmployees(Integer.parseInt(request.getParameter("pcnoofemployees")));
			}

			if (request.getParameter("pensionemoluments") != null) {
				rbean.setPcEmoluments(Double.parseDouble(request.getParameter("pensionemoluments")));
			} else {
				rbean.setPcEmoluments(0.00);
			}
			if (request.getParameter("pensioncontribution") != null) {
				rbean.setPensionContribution(Double.parseDouble(request.getParameter("pensioncontribution")));
			}
			if (request.getParameter("pcremitterbanknm") != null) {
				rbean.setPcRemitterBankNM(request.getParameter("pcremitterbanknm"));
			}
			if (request.getParameter("pcremitterbankacno") != null) {
				rbean.setPcRemitterBankAcNo(request.getParameter("pcremitterbankacno"));
			}
			if (request.getParameter("salarymonth") != null) {
				rbean.setSalMonth(request.getParameter("salarymonth").trim());
			}
			if (request.getParameter("region") != null) {
				rbean.setRegion(request.getParameter("region"));
			}
			if (request.getParameter("airportcode") != null) {
				rbean.setAirportcode(request.getParameter("airportcode"));
			}

			if (request.getParameter("epf3accretion") != null) {
				rbean.setEpf3Accretion(Double.parseDouble(request.getParameter("epf3accretion")));
			}
			if (request.getParameter("epf3pensioncontri") != null) {
				rbean.setEpf3Pensioncontri(Double.parseDouble(request.getParameter("epf3pensioncontri")));
			}
			if (request.getParameter("epf3Emoluments") != null) {
				rbean.setEpf3Emoluments(Double.parseDouble(request.getParameter("epf3Emoluments")));
			}
			if (request.getParameter("remitancetype") != null) {
				rbean.setRemitanceType(request.getParameter("remitancetype"));
			}
			if (request.getParameter("billrefno") != null) {
				rbean.setBillRefno(request.getParameter("billrefno"));
			}
			if (request.getParameter("billdate") != null) {
				rbean.setBilldate(request.getParameter("billdate"));
			}
			if (request.getParameter("chequenofrom") != null) {
				rbean.setChequenofrom(request.getParameter("chequenofrom"));
			}
			if (request.getParameter("chequenoto") != null) {
				rbean.setChequenoto(request.getParameter("chequenoto"));
			}
			if (request.getParameter("chequedt") != null) {
				rbean.setChequedt(request.getParameter("chequedt"));
			}
			if (request.getParameter("preparedby") != null) {
				rbean.setPreparedby(request.getParameter("preparedby"));
			}
			if (request.getParameter("checkedby") != null) {
				rbean.setCheckedby(request.getParameter("checkedby"));
			}
			if (request.getParameter("passedby") != null) {
				rbean.setPassedby(request.getParameter("passedby"));
			}
			if (request.getParameter("receivedby") != null) {
				rbean.setReceivedby(request.getParameter("receivedby"));
			}
			log.info("RemitaneType " + rbean.getRemitanceType());
			String username = (String) session.getAttribute("userid");
			String computername = request.getRemoteAddr();
			epfformsService.inserRemittanceInfo(rbean, username, computername);

			RequestDispatcher rd = request
					.getRequestDispatcher("./aaiepfreportservlet?method=loadepf3&page=remitencescreen");
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("AccretionReport")) {
			String region = "", regionDesc = "", year = "", month = "", toYear = "", shnYear = "", screen = "";
			String airportcode = "", reportType = "", sortingOrder = "", monthDesc = "", pfidString = "", empName = "",
					empflag = "false", pensionno = "", frmSelectedDts = "";
			String accounttype = "", remitancetype = "";
			ArrayList epfForm3List = new ArrayList();
			ArrayList epfForm3supplList = new ArrayList();
			ArrayList epfForm3ArrList = new ArrayList();
			ArrayList form4DeputationList = new ArrayList();
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
			if (request.getParameter("accounttype") != null) {
				accounttype = request.getParameter("accounttype");
			}
			if (request.getParameter("remitancetype") != null) {
				remitancetype = request.getParameter("remitancetype");
			}
			if (request.getParameter("screen") != null) {
				screen = request.getParameter("screen");
			}
			try {
				frmSelectedDts = this.getFromToDates(year, toYear, month, month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// log.info("===AAI EPF-3=====" + region + "year" +
			// frmSelectedDts+"empflag========="+empflag+"pfidString"+pfidString+"pensionno"+pensionno);
			if (remitancetype.equals("aaiepf")) {
				epfForm3List = epfformsService.loadAccrationReport(pfidString, region, airportcode, empName, empflag,
						frmSelectedDts, sortingOrder, pensionno, accounttype, "aaiepf3");
				epfForm3supplList = epfformsService.loadAccrationReport(pfidString, region, airportcode, empName,
						empflag, frmSelectedDts, sortingOrder, pensionno, accounttype, "aaiepf3Suppl");
				epfForm3ArrList = epfformsService.loadAccrationReport(pfidString, region, airportcode, empName, empflag,
						frmSelectedDts, sortingOrder, pensionno, accounttype, "aaiepf3Arr");
				form4DeputationList = epfformsService.loadAccrationReport(pfidString, region, airportcode, empName,
						empflag, frmSelectedDts, sortingOrder, pensionno, accounttype, "form4deputation");
			} else {
				epfForm3List = epfformsService.loadAccrationReport(pfidString, region, airportcode, empName, empflag,
						frmSelectedDts, sortingOrder, pensionno, accounttype, remitancetype);
			}
			log.info("cardList" + epfForm3List.size());
			log.info("epfForm3supplList" + epfForm3supplList.size());
			if (remitancetype.equals("aaiepf")) {
				request.setAttribute("cardList", epfForm3List);
				request.setAttribute("epfForm3supplList", epfForm3supplList);
				request.setAttribute("epfForm3ArrList", epfForm3ArrList);
				request.setAttribute("form4DeputationList", form4DeputationList);
			} else {
				request.setAttribute("cardList", epfForm3List);
			}
			if (!year.equals("NO-SELECT")) {
				shnYear = year + "-" + toYear;
			} else {
				shnYear = "1995-" + commonUtil.getCurrentDate("yyyy");
			}
			if (!month.equals("NO-SELECT")) {
				try {
					monthDesc = commonUtil.converDBToAppFormat("01-" + month + "-" + commonUtil.getCurrentDate("yyyy"),
							"dd-MM-yyyy", "MMM");
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("dspMonth", monthDesc);
			}

			if (!region.equals("NO-SELECT")) {
				regionDesc = region;
			} else {
				regionDesc = "";
			}

			request.setAttribute("regionDesc", regionDesc);
			request.setAttribute("airportcode", airportcode);

			request.setAttribute("dspYear", shnYear);
			request.setAttribute("remitancetype", remitancetype);
			request.setAttribute("reportType", reportType);
			RequestDispatcher rd = null;
			if (screen.equals("accretion")) {
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/AccretionReport.jsp");
			} else if (screen.equals("cpfaccretion")) {
				rd = request.getRequestDispatcher("./PensionView/aaiepfreports/CpfAccretionReport.jsp");
			}
			rd.forward(request, response);
		} /*
			 * else if (request.getParameter("method").equals("getform3regionwise")) {
			 * ArrayList summaryList= new ArrayList(); ArrayList summaryList_CHQIAD= new
			 * ArrayList(); String finYear="",selectedYear="",reportType="",empStatus="";
			 * RequestDispatcher rd=null;
			 * 
			 * if (request.getParameter("frm_finyear") != null) { finYear =
			 * request.getParameter("frm_finyear"); } if
			 * (request.getParameter("frm_reportType") != null) { reportType =
			 * request.getParameter("frm_reportType"); } if
			 * (request.getParameter("frm_status") != null) { empStatus =
			 * request.getParameter("frm_status"); } try { summaryList=
			 * epfformsService.getForm3SummaryReport(finYear,"ALL",empStatus);
			 * summaryList_CHQIAD=
			 * epfformsService.getForm3SummaryReport(finYear,"CHQIAD",empStatus);
			 * 
			 * 
			 * } catch (Exception e) { e.printStackTrace(); }
			 * request.setAttribute("summaryList",summaryList);
			 * request.setAttribute("summaryList_CHQIAD",summaryList_CHQIAD);
			 * request.setAttribute("dispYear",finYear); request.setAttribute("reportType",
			 * reportType);
			 * 
			 * rd = request.getRequestDispatcher(
			 * "./PensionView/ControlAccounts/Form3SummaryReport.jsp"); rd.forward(request,
			 * response); }
			 */else if (request.getParameter("method").equals("getsummaryinformation")) {
			ArrayList summaryInfoList = new ArrayList();

			String finYear = "", selectedYear = "", reportType = "", finYear_prev = "", finYear_next = "",
					formType = "", includeFormType = "", region = "", dispRegion = "", serialNo = "", crtlAccFlag = "",
					empStatus = "", reportpurpose = "";
			int fromYear_prev = 0, toYear_prev = 0, fromYear_next = 0, toYear_next = 0;
			String years[] = null;
			RequestDispatcher rd = null;

			if (request.getParameter("frm_finyear") != null) {
				finYear = request.getParameter("frm_finyear");
			}

			years = finYear.split("-");
			fromYear_prev = Integer.parseInt(years[0]) - 1;
			toYear_prev = Integer.parseInt(years[1]) - 1;
			finYear_prev = fromYear_prev + "-" + toYear_prev;

			fromYear_next = Integer.parseInt(years[0]) + 1;
			toYear_next = Integer.parseInt(years[1]) + 1;
			finYear_next = fromYear_next + "-" + toYear_next;

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}

			if (request.getParameter("frm_frmType") != null) {
				formType = request.getParameter("frm_frmType");
			}
			log.info("-----frm_frmType-----" + formType);

			if (formType.equals("form2summary")) {
				if (request.getParameter("frm_region") != null) {
					region = request.getParameter("frm_region");
				}
				if (region.equals("NO-SELECT")) {
					dispRegion = "All Regions";
				} else {
					dispRegion = region;
				}
			} else {
				region = "NO-SELECT";
			}

			if (request.getParameter("serialNo") != null) {
				serialNo = request.getParameter("serialNo");
			}
			if (request.getParameter("crtlAccFlag") != null) {
				crtlAccFlag = request.getParameter("crtlAccFlag");
			}
			if (request.getParameter("frm_status") != null) {
				empStatus = request.getParameter("frm_status");
			}
			if (request.getParameter("frm_reportpurpose") != null) {
				reportpurpose = request.getParameter("frm_reportpurpose");
			}

			try {
				summaryInfoList = epfformsService.getControlAccSummaryReport(finYear, formType, region, serialNo,
						crtlAccFlag, empStatus, reportpurpose);

			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("summaryInfoList", summaryInfoList);
			// request.setAttribute("controlACCList",controlACCList);
			// request.setAttribute("controlACCList_Prev",controlACCList_Prev);

			request.setAttribute("dispYear", finYear);
			request.setAttribute("dispYear_prev", finYear_prev);
			request.setAttribute("dispYear_next", finYear_next);
			request.setAttribute("dispRegion", dispRegion);
			request.setAttribute("reportType", reportType);
			request.setAttribute("empStatus", empStatus);

			if (formType.equals("form8summary")) {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form8SummaryReport.jsp");
			} else if (formType.equals("form2summary")) {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form2RegionwiseSummaryReport.jsp");
			} else if (formType.equals("form2Asummary")) {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form2ASummaryReport.jsp");
			} else {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/SummaryInformationReport.jsp");
			}

			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getregionwisesummary")) {

			log.info("inside getregionwisesummery method of epfformreport");
			String region = "", regionDesc = "", finyear = "", frmSelctedYear = "", toYear = "", shnYear = "",
					displayDate = "", disMonthYear = "", empStatus = "";

			String airportcode = "", reportType = "", totalSub = "";
			ArrayList epfForm3List = new ArrayList();

			if (request.getParameter("frm_finyear") != null) {
				finyear = request.getParameter("frm_finyear");
			}

			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_status") != null) {
				empStatus = request.getParameter("frm_status");
			}
			epfForm3List = epfformsService.getControlAccSummaryRegionWiseReport(finyear, empStatus);
			log.info("getregionwisesummery ->epfForm3List size:" + epfForm3List.size());
			request.setAttribute("cardList", epfForm3List);

			request.setAttribute("totalSub", totalSub);
			request.setAttribute("reportType", reportType);
			request.setAttribute("dspYear", finyear);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ControlAccounts/ControlAccountsRegionWiseReport.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method").equals("summaryAdj")) {
			log.info("inside summaryAdj method");
			String aaitotal = "", subtotal = "", contotal = "", empstatus = "", finyear = "", flag = "", adjType = "";

			ControlAccountForm2Info adjSummaryInfo = new ControlAccountForm2Info();

			if (request.getParameter("frm_finyear") != null) {
				finyear = request.getParameter("frm_finyear");
			}
			if (request.getParameter("empstatus") != null) {
				empstatus = request.getParameter("empstatus");
			}
			if (request.getParameter("aaitotal") != null) {
				aaitotal = request.getParameter("aaitotal");
			}
			if (request.getParameter("subtotal") != null) {
				subtotal = request.getParameter("subtotal");
			}
			if (request.getParameter("contotal") != null) {
				contotal = request.getParameter("contotal");
			}

			System.out.println("reportservelet:" + empstatus + "---aaitotal--" + aaitotal + "--subtotal--" + subtotal);
			adjSummaryInfo = epfformsService.getControlAccSummaryAdjReport(finyear, empstatus);
			request.setAttribute("adjSummaryInfo", adjSummaryInfo);
			request.setAttribute("aaitotal", aaitotal);
			request.setAttribute("subtotal", subtotal);
			request.setAttribute("contotal", contotal);
			request.setAttribute("empstatus", empstatus);
			request.setAttribute("finYear", finyear);
			session.removeAttribute("manualList");
			session.removeAttribute("systemList");
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ControlAccounts/ControlAccountsAdjSummaryReport.jsp");

			rd.forward(request, response);

		} else if (request.getParameter("method").equals("form2AdjReport")) {
			log.info("inside form2AdjReport method");
			String empstatus = "", finyear = "", flag = "", adjType = "", frm_AmtFlag = "", frm_attri = "";

			ControlAccountForm2Info adjSummaryInfo = new ControlAccountForm2Info();

			ArrayList manualList = new ArrayList();
			ArrayList systemList = new ArrayList();

			if (request.getParameter("frm_finyear") != null) {
				finyear = request.getParameter("frm_finyear");
			}
			if (request.getParameter("empstatus") != null) {
				empstatus = request.getParameter("empstatus");
			}
			if (request.getParameter("frm_adjType") != null) {
				adjType = request.getParameter("frm_adjType");
			}
			if (session.getAttribute("manualList") != null) {
				manualList = (ArrayList) session.getAttribute("manualList");
			}
			if (session.getAttribute("systemList") != null) {
				systemList = (ArrayList) session.getAttribute("systemList");
			}
			if (request.getParameter("frm_AmtFlag") != null) {
				frm_AmtFlag = request.getParameter("frm_AmtFlag");
			}
			if (request.getParameter("frm_attri") != null) {
				frm_attri = request.getParameter("frm_attri");
			}

			request.setAttribute("empstatus", empstatus);
			request.setAttribute("finYear", finyear);
			request.setAttribute("adjType", adjType);
			request.setAttribute("amtFlag", frm_AmtFlag);
			request.setAttribute("attribute", frm_attri);

			if (adjType.equals("M")) {
				request.setAttribute("adjOBList", manualList);
			} else if (adjType.equals("S")) {
				request.setAttribute("adjOBList", systemList);
			}
			RequestDispatcher rd = null;

			if (frm_AmtFlag.equals("")) {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form2AdjstmntsReport.jsp");
			} else {
				rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form2OBReport.jsp");
			}

			rd.forward(request, response);

		} else if (request.getParameter("method").equals("epfForm8SummaryDetails")) {
			log.info("inside epfForm8SummaryDetails method");
			String accounthead = "", finyear = "", keynoStatus = "", path = "";
			AAIEPFReportBean form8SummaryDetails = new AAIEPFReportBean();
			ArrayList witKeyNoList = new ArrayList();
			ArrayList witOutKeyNoList = new ArrayList();
			if (request.getParameter("frm_finyear") != null) {
				finyear = request.getParameter("frm_finyear");
			}
			if (request.getParameter("accounthead") != null) {
				accounthead = request.getParameter("accounthead");
			}
			if (request.getParameter("keynoStatus") != null) {
				keynoStatus = request.getParameter("keynoStatus");
			}
			if (keynoStatus.equals("witkey")) {
				witKeyNoList = epfformsService.epfForm8SummaryDetailsWitKeyNo(finyear, accounthead);
				path = "./PensionView/aaiepfreports/AAIEPFForm8WitKeyNoSummaryDetails.jsp";
			} else if (keynoStatus.equals("witoutkey")) {
				witOutKeyNoList = epfformsService.epfForm8SummaryDetailsWitOutKeyNo(finyear, accounthead);
				path = "./PensionView/aaiepfreports/AAIEPFForm8WitOutKeyNoSummaryDetails.jsp";
			}
			log.info("===========path=====" + path);
			request.setAttribute("accounthead", accounthead);
			request.setAttribute("finYear", finyear);
			request.setAttribute("witKeyNoList", witKeyNoList);
			request.setAttribute("witOutKeyNoList", witOutKeyNoList);
			RequestDispatcher rd = null;
			rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method").equals("getform3regionwise")) {
			Map summaryMap = new LinkedHashMap();
			Map summarySupMap = new LinkedHashMap();
			Map summaryArrMap = new LinkedHashMap();
			Map form4Map = new LinkedHashMap();
			String finYear = "", selectedYear = "", reportType = "", empStatus = "";
			RequestDispatcher rd = null;
			if (request.getParameter("frm_finyear") != null) {
				finYear = request.getParameter("frm_finyear");
			}
			if (request.getParameter("frm_reportType") != null) {
				reportType = request.getParameter("frm_reportType");
			}
			if (request.getParameter("frm_status") != null) {
				empStatus = request.getParameter("frm_status");
			}
			try {

				summaryMap = epfformsService.getForm3SummaryReport(finYear, "CPF", empStatus);
				summarySupMap = epfformsService.getForm3SummaryReport(finYear, "SUP", empStatus);
				summaryArrMap = epfformsService.getForm3SummaryReport(finYear, "ARR", empStatus);
				form4Map = epfformsService.getForm3SummaryReport(finYear, "FORM4", empStatus);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("form4Map", form4Map);
			request.setAttribute("summaryMap", summaryMap);
			request.setAttribute("summarySupMap", summarySupMap);
			request.setAttribute("summaryArrMap", summaryArrMap);
			request.setAttribute("dispYear", finYear);
			request.setAttribute("status", empStatus);
			request.setAttribute("reportType", reportType);

			rd = request.getRequestDispatcher("./PensionView/ControlAccounts/Form3SummaryReport.jsp");
			rd.forward(request, response);
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	public String getFromToDates(String fromYear, String toYear, String fromMonth, String toMonth)
			throws InvalidDataException {
		String fromDate = "", toDate = "";
		StringBuffer buffer = new StringBuffer();

		log.info("fromYear" + fromYear + "toYear" + toYear + "fromMonth" + fromMonth + "toMonth " + toMonth);

		if (!fromYear.equals("NO-SELECT")) {

			if (!fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-" + fromMonth.trim() + "-" + fromYear;
				if (Integer.parseInt(toMonth.trim()) >= 1 && Integer.parseInt(toMonth.trim()) <= 3) {
					fromDate = "01-" + toMonth.trim() + "-" + toYear;
					toDate = "01-" + toMonth.trim() + "-" + toYear;
				} else {
					toDate = "01-" + toMonth.trim() + "-" + fromYear;
				}

			} else if (fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-04-" + fromYear;
				if (Integer.parseInt(toMonth) >= 1 && Integer.parseInt(toMonth) <= 3) {
					fromDate = "01-" + toMonth + "-" + toYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else {
					toDate = "01-" + toMonth + "-" + fromYear;
				}

			} else if (!fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-" + fromMonth + "-" + fromYear;
				if (Integer.parseInt(toMonth) >= 1 && Integer.parseInt(toMonth) <= 3) {
					toDate = "01-" + toMonth + "-" + toYear;
				} else {
					toDate = "01-03-" + toYear;
				}

			} else if (fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")) {
				fromDate = "01-04-" + fromYear;
				toDate = "01-03-" + toYear;
			}

		} else {
			fromDate = "01-04-1995";
			toDate = "01-03-" + commonUtil.getCurrentDate("yyyy");
		}
		buffer.append(commonUtil.converDBToAppFormat(fromDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		buffer.append(",");
		buffer.append(commonUtil.converDBToAppFormat(toDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		return buffer.toString();
	}

	public String getFromToDatesForForm3(String fromYear, String toYear, String fromMonth, String toMonth)
			throws InvalidDataException {
		String fromDate = "", toDate = "";
		StringBuffer buffer = new StringBuffer();

		log.info("fromYear" + fromYear + "toYear" + toYear + "fromMonth" + fromMonth + "toMonth " + toMonth);

		if (!fromYear.equals("NO-SELECT")) {
			if (!fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-" + fromMonth.trim() + "-" + fromYear;
				toDate = "01-" + toMonth.trim() + "-" + toYear;

			} /*
				 * else if(fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")){
				 * fromDate="01-04-"+fromYear; if(Integer.parseInt(toMonth)>=1 &&
				 * Integer.parseInt(toMonth)<=3){ fromDate="01-"+fromMonth+"-"+fromYear;
				 * toDate="01-"+toMonth+"-"+toYear; }else{ toDate="01-"+toMonth+"-"+fromYear; }
				 * 
				 * }
				 */
		}

		buffer.append(commonUtil.converDBToAppFormat(fromDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		buffer.append(",");
		buffer.append(commonUtil.converDBToAppFormat(toDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		return buffer.toString();
	}

	public String getFromToDates(String fromYear, String toYear, String fromMonth, String toMonth, String form3)
			throws InvalidDataException {
		String fromDate = "", toDate = "";
		StringBuffer buffer = new StringBuffer();

		log.info("fromYear" + fromYear + "toYear" + toYear + "fromMonth" + fromMonth);

		if (!fromYear.equals("NO-SELECT") && !fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
			fromDate = "01-" + fromMonth + "-" + fromYear;
			toDate = "01-" + toMonth + "-" + toYear;
		} else {
			fromDate = "01-04-1995";
			toDate = "01-03-" + commonUtil.getCurrentDate("yyyy");
		}
		buffer.append(commonUtil.converDBToAppFormat(fromDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		buffer.append(",");
		buffer.append(commonUtil.converDBToAppFormat(toDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		return buffer.toString();
	}

}
