package aims.action;

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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.EmpMasterBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.FinaceDataAvailable;
import aims.bean.FinacialDataBean;
import aims.bean.Form8Bean;
import aims.bean.RegionBean;
import aims.bean.SearchInfo;
import aims.bean.epfforms.AAIEPFReportBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.CommonDAO;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PersonalService;

public class FinanceServlet extends HttpServlet{
	Log log = new Log(FinanceServlet.class);
	FinancialService financeService = new FinancialService();
	PersonalService personalService = new PersonalService();
	FinancialReportService finReportService = new FinancialReportService();
	CommonUtil commonUtil = new CommonUtil();
	CommonDAO commonDAO=new CommonDAO();
	
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		int gridLength = Integer.parseInt((String) session
				.getAttribute("gridlength"));
		
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {
			String region = "";
			region = request.getParameter("region");
			CommonUtil commonUtil = new CommonUtil();
			ArrayList ServiceType = null;
			ServiceType = commonUtil.getAirportsByFinanceTbl(region);
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

			// request.setAttribute("cpfnoDeatails", bean);
			// RequestDispatcher rd =
			//request.getRequestDispatcher("./PensionView/AddFinancialDetail.jsp"
			// );
			// rd.forward(request, response);
		}else if (
				 request.getParameter("method").equals("loadcpfdeviationInput")
				||  request.getParameter("method").equals("loadtransfersearch")
				||  request.getParameter("method").equals("loadtransferInput")	
				||  request.getParameter("method").equals("loadmonthlycpfdatasearch")
				||  request.getParameter("method").equals("loadyearlyempcountsearch")){
			Map yearMap = new LinkedHashMap();
			Map monthMap = new LinkedHashMap();
			Iterator yearIterator = null;
			Iterator monthIterator = null;
			Iterator monthIterator1 = null;
			ArrayList penYearList = new ArrayList();

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
			regionHashmap = commonUtil.getRegion();
			
			request.setAttribute("regionHashmap", regionHashmap);
			RequestDispatcher rd = null;
			if (request.getParameter("method").equals("loadcpfdeviationInput")) {
				rd = request
				.getRequestDispatcher("./PensionView/CPFDeviationInputParms.jsp");	
			}else if (request.getParameter("method").equals("loadtransfersearch")) {
				rd = request
				.getRequestDispatcher("./PensionView/TransferSearch.jsp");	
			}else if (request.getParameter("method").equals("loadtransferInput")) {
				rd = request
				.getRequestDispatcher("./PensionView/TransferInputParms.jsp");	
			}else if (request.getParameter("method").equals("loadmonthlycpfdatasearch")) {
				rd = request
				.getRequestDispatcher("./PensionView/CPFDeviationSearch.jsp");	
			}else if (request.getParameter("method").equals("loadyearlyempcountsearch")) {
				rd = request
				.getRequestDispatcher("./PensionView/YearlyEmpCountSearch.jsp");	
			}
			rd.forward(request, response);
		}else if (request.getParameter("method").equals("deviationsearch")) {
			log.info("SearchPensionMasterServlet : dopost() ");
			FinacialDataBean bean = new FinacialDataBean();
			ArrayList searchList=new ArrayList();
			
			if (request.getParameter("airPortCD") != null) {
			bean.setAirportCode(request.getParameter("airPortCD").toString());
			} else {
				bean.setAirportCode("");
			}
			if (request.getParameter("pfid") != null) {
				bean.setPensionNumber(commonUtil.getSearchPFID1(request.getParameter("pfid").toString()));
			} else {
				bean.setPensionNumber("");
			}
			
			String contextPath = request.getContextPath().toString();
			bean.setContextPath(contextPath);

		
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

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			
			try {
				log.info("From Date" + bean.getFromDate() + "To Date"
						+ bean.getToDate());
				//searchBean = financeService.searchDeviationData(bean, getSearchInfo,gridLength);
				searchList=	financeService.searchDeviationData(bean, getSearchInfo,gridLength);
				
				log.info("Size of bean=========="
						+ searchBean.getSearchList().size());
							
				//session.setAttribute("reportfinancebean", searchBean);
				session.setAttribute("reportsearchBean", bean);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//request.setAttribute("searchBean", searchBean);
			request.setAttribute("region", bean.getRegion().trim());
			request.setAttribute("searchlist",searchList);
			
			log.info("///////////////////////");

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CPFDeviationSearch.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method").equals("deviationreport")) {
			String region = "", year = "", month = "";
			String airportcode = "", reportType = "", sortingOrder = "",pfidString="",nextyear="",toYear="",frmSelectedDts="";
			
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
			
			if (request.getParameter("frm_pfids") != null) {
				pfidString = request.getParameter("frm_pfids");
			}else{
				pfidString="";
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
				frmSelectedDts=this.getFromToDates(year,toYear,month,month);
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			log.info("===AAIEPF1Report=====" + region + "year" + year+"empflag========="+empflag+"pfidString"+pfidString+"pensionno"+pensionno);
			
			list = financeService.deviationReport(pfidString,region, airportcode,frmSelectedDts, empflag,
					empName, sortingOrder, pensionno);
			

			if(list.size()>0)
			request.setAttribute("AAIEPF1List", list);
			
			log.info("---------list  SIZE in Action-----------"+list.size());
			
			if(!year.equals("NO-SELECT")){
				 nextyear=year.substring(2,4);
							
				nextyear=Integer.toString(Integer.parseInt(nextyear)+1);
				
				if(Integer.parseInt(nextyear)<=9)
					nextyear="0"+nextyear;				
			}else{
				year="1995";
				nextyear=commonUtil.getCurrentDate("yy");
			}
				
			year=year+"-"+nextyear;
			request.setAttribute("finYear", year);
			
			if(!region.equals("NO-SELECT")){
				request.setAttribute("region", region);			
			}else{
				request.setAttribute("region", "-----");		
			}
			request.setAttribute("reportType", reportType);
			
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CPFDeviationReport.jsp");
			rd.forward(request, response);
			}else if (request.getParameter("method").equals("transfersearch")) {
				log.info("SearchPensionMasterServlet : dopost() ");
				FinacialDataBean bean = new FinacialDataBean();
				FinacialDataBean finacialDataBean=new FinacialDataBean();
				ArrayList transferInList = new ArrayList();
				ArrayList transferOutList = new ArrayList();
				
				HashMap regionHashmap = new HashMap();
				regionHashmap = commonUtil.getRegion();
				
				request.setAttribute("regionHashmap", regionHashmap);
				
				if (request.getParameter("select_region") != null) {
				bean.setRegion(request.getParameter("select_region").toString());
				} else {
					bean.setRegion("");
				}
				
				if (request.getParameter("select_airport") != null) {
					bean.setAirportCode(request.getParameter("select_airport").toString());
					} else {
						bean.setAirportCode("");
					}
				if (request.getParameter("pfid") != null) {
					bean.setPensionNumber(commonUtil.getSearchPFID1(request.getParameter("pfid").toString()));
				} else {
					bean.setPensionNumber("");
				}
				
				String contextPath = request.getContextPath().toString();
				bean.setContextPath(contextPath);

			
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
				

				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();
				
				try {
					log.info("From Date" + bean.getFromDate() + "To Date"
							+ bean.getToDate());
					//searchBean = financeService.searchDeviationData(bean, getSearchInfo,gridLength);
					finacialDataBean=	financeService.searcTransferData(bean, getSearchInfo,gridLength);
					
					log.info("Size of bean=========="
							+ searchBean.getSearchList().size());
								
					//session.setAttribute("reportfinancebean", searchBean);
					session.setAttribute("reportsearchBean", bean);
				} catch (Exception e) {
					e.printStackTrace();
				}

				//request.setAttribute("searchBean", searchBean);
				request.setAttribute("region", bean.getRegion().trim());
				transferInList=finacialDataBean.getTransferInList();
				transferOutList=finacialDataBean.getTransferOutList();
				if(transferInList.size()!=0){
				request.setAttribute("transferInList",transferInList);
				}
				if(transferOutList.size()!=0){
					request.setAttribute("transferOutList",transferOutList);
				}
				
				
				log.info("///////////////////////");

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/TransferSearch.jsp");
				rd.forward(request, response);
			}else if (request.getParameter("method").equals("transferreport")) {
				String region = "", year = "", month = "";
				ArrayList airportList = new ArrayList();
				String airportcode = "", reportType = "", sortingOrder = "",pfidString="",nextyear="",toYear="",frmSelectedDts="";
				
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
				
				if (!request.getParameter("sortingOrder").equals("")) {
					sortingOrder = request.getParameter("sortingOrder");
				} else {
					sortingOrder = "pensionno";
				}
				if (request.getParameter("frm_reportType") != null) {
					reportType = request.getParameter("frm_reportType");
				}
				
				String  pensionno = "";
				
				if (request.getParameter("frm_pensionno") != null) {
					pensionno = request.getParameter("frm_pensionno");
				}
				
				try {
					frmSelectedDts=this.getFromToDates(year,toYear,month,month);
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				log.info("===AAIEPF1Report=====" + region + "year" + year+"pensionno"+pensionno);
				
				list = financeService.transferReport(region, airportcode,frmSelectedDts,sortingOrder, pensionno);
				

				if(list.size()>0)
				request.setAttribute("AAIEPF1List", list);
				
				log.info("---------list  SIZE in Action-----------"+list.size());
				
				if(!year.equals("NO-SELECT")){
					 nextyear=year.substring(2,4);
								
					nextyear=Integer.toString(Integer.parseInt(nextyear)+1);
					
					if(Integer.parseInt(nextyear)<=9)
						nextyear="0"+nextyear;				
				}else{
					year="1995";
					nextyear=commonUtil.getCurrentDate("yy");
				}
					
				year=year+"-"+nextyear;
				request.setAttribute("finYear", year);
				
				if(!region.equals("NO-SELECT")){
					request.setAttribute("region", region);			
				}else{
					request.setAttribute("region", "-----");		
				}
				request.setAttribute("reportType", reportType);
				
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/TransferInOutReport.jsp");
				rd.forward(request, response);
				}else if (request.getParameter("method").equals("loadtransferdetails")) {
					String region = "", year = "";
					String airportcode = "";
					String  pensionno = "",status="";
					
					if (request.getParameter("frm_pensionno") != null) {
						pensionno = request.getParameter("frm_pensionno");
					}
					
					if (request.getParameter("frm_status") != null) {
						status = request.getParameter("frm_status");
					}
					
					if (request.getParameter("frm_airportcode") != null) {
						airportcode = request.getParameter("frm_airportcode");
					}
					
					if (request.getParameter("frm_region") != null) {
						region = request.getParameter("frm_region");
					}
														
					log.info("===AAIEPF1Report=====" + region + "year" + year+"pensionno"+pensionno);					
					
					request.setAttribute("pensionno", pensionno);
					request.setAttribute("status", status);
					request.setAttribute("newairportcode", airportcode);
					request.setAttribute("newregion", region);
					
					HashMap regionHashmap = new HashMap();
					regionHashmap = commonUtil.getRegion();
					
					request.setAttribute("regionHashmap", regionHashmap);
					
					RequestDispatcher rd = request
							.getRequestDispatcher("./PensionView/TransferDetailsUpdation.jsp");
					rd.forward(request, response);
					}else if (request.getParameter("method").equals("updatetransferdata")) {
						String region = "", year = "";
						String airportcode = "", reportType = "",status="", fromAirportCode = "",fromRegion="",pfidString="",nextyear="",toYear="",frmSelectedDts="";
						String  pensionno = "",seperationreason="",sepetaionDt="";
						
						if (request.getParameter("pensionno") != null) {
							pensionno = request.getParameter("pensionno");
						}
						
						if (request.getParameter("seperationreason") != null) {
							seperationreason = request.getParameter("seperationreason");
						}
																	
						if (request.getParameter("sepetaionDt") != null) {
							sepetaionDt = request.getParameter("sepetaionDt");
						}
						
						if (request.getParameter("airportcode") != null) {
							airportcode = request.getParameter("airportcode");
						}
						
						if (request.getParameter("region") != null) {
							region = request.getParameter("region");
						}
						
						if (request.getParameter("select_region") != null) {
							fromRegion = request.getParameter("select_region");
						}
						
						if (request.getParameter("select_airport") != null) {
							fromAirportCode = request.getParameter("select_airport");
						}
						
						if (request.getParameter("status") != null) {
							status = request.getParameter("status");
						}
						
						financeService.updateTransferData(pensionno,seperationreason,sepetaionDt,airportcode,region,fromAirportCode,fromRegion,status);
										
						log.info("===AAIEPF1Report=====" + region + "year" + year+"pensionno"+pensionno);
						
						
						HashMap regionHashmap = new HashMap();
						regionHashmap = commonUtil.getRegion();
						
						request.setAttribute("regionHashmap", regionHashmap);
						
						RequestDispatcher rd = request
								.getRequestDispatcher("./PensionView/TransferDetailsUpdation.jsp");
						rd.forward(request, response);
						}else if (request.getParameter("method").equals("yearlyempcountsearch")) {
							log.info("SearchPensionMasterServlet : dopost() ");
							String  year="";
																		
							ArrayList empCountList = new ArrayList();
							
							HashMap regionHashmap = new HashMap();
							regionHashmap = commonUtil.getRegion();
							FinacialDataBean bean=null;
							
							request.setAttribute("regionHashmap", regionHashmap);
							
							if (request.getParameter("frm_name") != null) {
								log.info("--------frm_name------"+request.getParameter("frm_name"));
								
								if(request.getParameter("frm_name").equals("search")){
									
									 bean = new FinacialDataBean();			
									
									if (request.getParameter("select_region") != null) {
										bean.setRegion(request.getParameter("select_region").toString());
										} else {
											bean.setRegion("");
										}
										
										if (request.getParameter("select_airport") != null) {
											bean.setAirportCode(request.getParameter("select_airport").toString());
											} else {
												bean.setAirportCode("");
											}
										
										log.info("--Region---"+bean.getRegion()+"---Airport code----"+bean.getAirportCode());
										
										
										if (request.getParameter("pfid") != null) {
											bean.setPensionNumber(commonUtil.getSearchPFID1(request.getParameter("pfid").toString()));
										} else {
											bean.setPensionNumber("");
										}
										
										if (request.getParameter("select_year") != null) {
											year = request.getParameter("select_year");
											bean.setMonthYear(year);
										}else{
											bean.setMonthYear(year);
										}
										
										session.setAttribute("empCountData",bean);
									
								}else if(request.getParameter("frm_name").equals("report")){
									bean = new FinacialDataBean();			
									
									bean=(FinacialDataBean)session.getAttribute("empCountData");
								}
							} 							
							
							String contextPath = request.getContextPath().toString();
							bean.setContextPath(contextPath);
						try {								
								empCountList=	financeService.searchYearlyEmpCount(bean);								
								
							} catch (Exception e) {
								e.printStackTrace();
							}

							//request.setAttribute("searchBean", searchBean);
							request.setAttribute("region", bean.getRegion().trim());
							
						
							if(empCountList.size()!=0){
								request.setAttribute("empCountList",empCountList);
							}														
							RequestDispatcher rd=null;
							
							if(request.getParameter("frm_name").equals("search")){
								 rd = request.getRequestDispatcher("./PensionView/YearlyEmpCountSearch.jsp");								
							}else if(request.getParameter("frm_name").equals("report")){
								 rd = request.getRequestDispatcher("./PensionView/YearlyEmpCountReport.jsp");								
							}
							
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
	
	private String getFromToDates(String fromYear,String toYear,String fromMonth,String toMonth) throws InvalidDataException{
		String fromDate="",toDate="";
		StringBuffer buffer=new StringBuffer();
		
		log.info("fromYear"+fromYear+"toYear"+toYear+"fromMonth"+fromMonth);
		
			if(!fromYear.equals("NO-SELECT")){
				
				if(!fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")){
					fromDate="01-"+fromMonth+"-"+fromYear;
					if(Integer.parseInt(toMonth)>=1 && Integer.parseInt(toMonth)<=3){
						fromDate="01-"+toMonth+"-"+toYear;
						toDate="01-"+toMonth+"-"+toYear;
					}else{
						toDate="01-"+toMonth+"-"+fromYear;
					}
					
				}else if(fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")){
					fromDate="01-04-"+fromYear;
					if(Integer.parseInt(toMonth)>=1 && Integer.parseInt(toMonth)<=3){
						fromDate="01-"+toMonth+"-"+toYear;
						toDate="01-"+toMonth+"-"+toYear;
					}else{
						toDate="01-"+toMonth+"-"+fromYear;
					}
					
				}else if(!fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")){
					fromDate="01-"+fromMonth+"-"+fromYear;
					if(Integer.parseInt(toMonth)>=1 && Integer.parseInt(toMonth)<=3){
						toDate="01-"+toMonth+"-"+toYear;
					}else{
						toDate="01-03-"+toYear;
					}
					
				}else if(fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")){
					fromDate="01-04-"+fromYear;
					toDate="01-03-"+toYear;
				}
				
			}else{
				fromDate="01-04-1995";
				toDate="01-03-"+commonUtil.getCurrentDate("yyyy");
			}
		buffer.append(commonUtil.converDBToAppFormat(fromDate,"dd-MM-yyyy","dd-MMM-yyyy"));
		buffer.append(",");
		buffer.append(commonUtil.converDBToAppFormat(toDate,"dd-MM-yyyy","dd-MMM-yyyy"));
		return buffer.toString();
	}
}
