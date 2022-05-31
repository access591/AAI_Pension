
/**
 * File       : CeilingSearchServlet.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */
package aims.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.CeilingUnitBean;
import aims.bean.FinancialYearBean;
import aims.bean.SearchInfo;
import aims.common.CommonUtil;
import aims.common.Log;
import aims.service.CeilingUnitService;

public class CeilingSearchServlet extends HttpServlet {
	Log log = new Log(CeilingSearchServlet.class);
	CeilingUnitService cs = new CeilingUnitService();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	CeilingUnitBean dbBeans = new CeilingUnitBean();
		String sb = "", cellingUnitParam = "";
		String unitcode = request.getParameter("code");
		cellingUnitParam = request.getParameter("rate");
		String flag = request.getParameter("flag");
		String todate = request.getParameter("todate");
		String unitname = request.getParameter("name");
       
		try {
			/*
			 * Unit Master
			 */
			if (unitcode != null && flag.equals("unit")) {
				if (unitcode != null) {
					dbBeans
							.setUnitCode(request.getParameter("code")
									.toString());
				} else {
					dbBeans.setUnitCode("");
				}

				if (unitname != null) {
					dbBeans
							.setUnitName(request.getParameter("name")
									.toString());
				} else {
					dbBeans.setUnitName("");
				}

				if (cellingUnitParam != null || !cellingUnitParam.equals("")) {
					dbBeans.setRegion(cellingUnitParam);
				} else {
					dbBeans.setRegion("");
				}

				sb = cs.checkUnitCode(dbBeans);
				System.out.println("......................." + sb);
			} else if (unitcode != null && flag.equals("celling")) {
				/*
				 * Celling master
				 */
                 log.info("-------flag=ceiling in servlet----------");
				if (unitcode != null && flag.equals("celling")) {
					dbBeans.setFromWeDate(request.getParameter("code")
							.toString());
				} else {
					dbBeans.setFromWeDate("");
				}

				if (todate != null && flag.equals("celling")) {
					dbBeans.setToWeDate(request.getParameter("todate")
							.toString());
				} else {
					dbBeans.setToWeDate("");
				}

				if (!cellingUnitParam.equals("")) {
					dbBeans.setRate(Double.parseDouble(cellingUnitParam));
				} else {
					dbBeans.setRate(0.0);
				}
                
				sb = cs.checkWeDate(dbBeans);
				log.info("----------sb in servlet-----------------"+sb);
			} else if (unitcode != null && flag.equals("interest")) {
				/*
				 * Interest master
				 */

				if (unitcode != null && flag.equals("interest")) {
					dbBeans.setFromWeDate(request.getParameter("name")
							.toString());
				} else {
					dbBeans.setFromWeDate("");
				}

				if (todate != null && flag.equals("interest")) {
					dbBeans.setToWeDate(request.getParameter("todate")
							.toString());
				} else {
					dbBeans.setToWeDate("");
				}
				if (!cellingUnitParam.equals("")) {
					dbBeans.setInterestRate(Double
							.parseDouble(cellingUnitParam));
				} else {
					dbBeans.setInterestRate(0.0);
				}
				sb = cs.checkInterstWeDate(dbBeans);
			}

			StringBuffer sbf = new StringBuffer();
			sbf.append("<CHECK>");
			sbf.append("<STATUS>" + sb + "</STATUS>");
			sbf.append("</CHECK>");

			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			System.out.println("=========" + sbf.toString());

			PrintWriter out = response.getWriter();
			out.write(sbf.toString());
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		
		/*
		 * Financial Year
		 */
		try{
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getFinancialYear")) {			
			
			SearchInfo searchBean = new SearchInfo();
			SearchInfo getSearchInfo = new SearchInfo();
			ArrayList clist = new ArrayList();
			try {
				clist = cs.getFinancialYear();				
			} catch (Exception e) {
				log.printStackTrace(e);
			}
              
			if(clist.size()>0)
			request.setAttribute("FinancialYear", clist);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinancialYearNew.jsp");
			rd.forward(request, response);
		}		
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financialYearSearch")) {
            searchFinanceYearData(request,response);
            			
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinancialYearSearch.jsp");
			rd.forward(request, response);
			
		}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int gridLength = Integer.parseInt((String) session
				.getAttribute("gridlength"));

		//PensionService ps = new PensionService();
		CeilingUnitService cs = new CeilingUnitService();
		CommonUtil commonUtil = new CommonUtil();

		SearchInfo navSearchBean = new SearchInfo();
		CeilingUnitBean dbBeans = new CeilingUnitBean();
		String unitcd = "", reg = "";
		
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("csearch")) {
			log.info("CeilingSearchServlet : dopost() ");
			CeilingUnitBean bean = new CeilingUnitBean();
			try {

				if (request.getParameter("wedatefrom") != null) {
					bean.setFromWeDate(request.getParameter("wedatefrom")
							.toString());
				} else {
					bean.setFromWeDate("");
				}

				if (request.getParameter("wedateto") != null) {
					bean.setToWeDate(request.getParameter("wedateto")
							.toString());
				} else {
					bean.setToWeDate("");
				}
				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();

				searchBean = cs.searchCeilingData(bean, getSearchInfo,
						gridLength);
				request.setAttribute("searchBean", searchBean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CeilingMasterSearch.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("navigation")) {		

			String navgationBtn = "";

			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();

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
				dbBeans = (CeilingUnitBean) session
						.getAttribute("getSearchBean");
			}
			try {
				navSearchBean = cs.searchNavigationCeilingData(dbBeans,
						searchListBean, gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CeilingMasterSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("ceilingadd")) {
			log.info("CeilingSearchServlet : dopost() ");

			String wedate = "";
			int rate = 0;
			CeilingUnitBean bean = new CeilingUnitBean();
			try {
				if (request.getParameter("wedatefrom") != null) {
					wedate = request.getParameter("wedatefrom").toString();
					bean.setFromWeDate(request.getParameter("wedatefrom")
							.toString());
				} else {
					bean.setFromWeDate("");
				}
				if (request.getParameter("wedateto") != null) {
					wedate = request.getParameter("wedateto").toString();
					bean.setToWeDate(request.getParameter("wedateto")
							.toString());
				} else {
					bean.setToWeDate("");
				}
				if (Double.parseDouble(request.getParameter("rate")) != 0.0) {
					bean.setRate(Double.parseDouble(request
							.getParameter("rate")));
				} else {
					bean.setRate(0.0);
				}
				cs.addCeilingRecord(bean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CeilingMasterSearch.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("interestadd")) {
			log.info("CeilingSearchServlet : dopost() ");
			String wedate = "";
			//int rate=0;
			CeilingUnitBean bean = new CeilingUnitBean();
			try {
				if (request.getParameter("wedatefrom") != null) {
					wedate = request.getParameter("wedatefrom").toString();
					bean.setFromWeDate(request.getParameter("wedatefrom")
							.toString());
				} else {
					bean.setFromWeDate("");
				}
				if (request.getParameter("wedateto") != null) {
					wedate = request.getParameter("wedateto").toString();
					bean.setToWeDate(request.getParameter("wedateto")
							.toString());
				} else {
					bean.setToWeDate("");
				}

				if (Double.parseDouble(request.getParameter("interestrate")) != 0.0) {
					bean.setInterestRate(Double.parseDouble(request
							.getParameter("interestrate")));
				} else {
					bean.setInterestRate(0.0);
				}
				cs.addInterestRecord(bean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/InterestMasterSearch.jsp");
			rd.forward(request, response);

		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("edit")) {

			int ceilingcd = 0;
			System.out.println(".............method=edit.......");
			//ArrayList<CeilingUnitBean>  list =null;
			ceilingcd = Integer.parseInt(request.getParameter("ceilingcd"));
			CeilingUnitBean editBean = new CeilingUnitBean();
			try {
				editBean = cs.editCeilingMaster(ceilingcd);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("EditBean", editBean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/CeilingMasterEdit.jsp");
			rd.forward(request, response);
		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("update")) {

			log.info(" method=update-----------------");
			CeilingUnitBean bean = new CeilingUnitBean();

			try {
				if (request.getParameter("wefromdate") != null) {

					bean.setFromWeDate(request.getParameter("wefromdate")
							.toString());
				} else {
					bean.setFromWeDate("");
				}
				if (request.getParameter("wetodate") != null) {
					bean.setToWeDate(request.getParameter("wetodate")
							.toString());
				} else {
					bean.setToWeDate("");
				}
				if (request.getParameter("cerate") != null) {

					bean.setRate(Double.parseDouble(request
							.getParameter("cerate")));
				} else {
					bean.setRate(0.0);
				}
				if (request.getParameter("ceilingcode") != null) {
					bean.setCeilingCode(Integer.parseInt(request
							.getParameter("ceilingcode")));
				} else {
					bean.setCeilingCode(0);
				}
				int count = 0;
				count = cs.updateCeilingMaster(bean);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/CeilingMasterSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}
		/*
		 * Unit Master 
		 * 
		 */
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitSearch")) {
			log.info("UnitSearchServlet : dopost() ");
			String unitname = "", unitoption = "";
			CeilingUnitBean bean = new CeilingUnitBean();
			gridLength = Integer.parseInt((String) session
					.getAttribute("gridlength"));
			log.info("gridLength " + gridLength);
			try {
				if (request.getParameter("unitcode") != null) {
					bean.setUnitCode(request.getParameter("unitcode")
							.toString());
				} else {
					bean.setUnitCode("");
				}

				if (request.getParameter("unitname") != null) {
					unitname = request.getParameter("unitname").toString();
					bean.setUnitName(request.getParameter("unitname")
							.toString());
				} else {
					bean.setUnitName("");
				}
				if (request.getParameter("unitoption") != null) {
					unitoption = request.getParameter("unitoption").toString();
					bean.setUnitOption(request.getParameter("unitoption")
							.toString());
				} else {
					bean.setUnitOption("");
				}
				if (request.getParameter("region") != null) {
					bean.setRegion(request.getParameter("region").toString());
				} else {
					bean.setRegion("");
				}

				log.info("bean.getUnitName()" + bean.getUnitName());

				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();
				searchBean = cs.searchUnitData(bean, getSearchInfo, gridLength);
				request.setAttribute("searchBean", searchBean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UnitMasterSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitNavigation")) {
			log.info("UnitSearchServlet : dopost() inside else");
			String navgationBtn = "";
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
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
				dbBeans = (CeilingUnitBean) session
						.getAttribute("getSearchBean");
			}
			try {
				navSearchBean = cs.searchNavigationUnitData(dbBeans,
						searchListBean, gridLength);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UnitMasterSearch.jsp");
			rd.forward(request, response);
		}
		
		/*
		 * Load a UnitMasterEdit page
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitEdit")) {
			unitcd = request.getParameter("unitcd");
			reg = request.getParameter("region");
			CeilingUnitBean editBean = new CeilingUnitBean();
			try {
				editBean = cs.editUnitMaster(unitcd, reg);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("EditBean", editBean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UnitMasterEdit.jsp");
			rd.forward(request, response);
		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitUpdate")) {

			log.info("SearchUnitMasterServlet : dopost() ");
			CeilingUnitBean bean = new CeilingUnitBean();
			if (request.getParameter("unitname") != null) {
				bean.setUnitName(request.getParameter("unitname").toString());
			} else {
				bean.setUnitName("");
			}
			if (request.getParameter("unitoption") != null) {
				bean.setUnitOption(request.getParameter("unitoption"));
			} else {
				bean.setUnitOption("");
			}
			if (request.getParameter("unitcode") != null) {
				bean.setUnitCode(request.getParameter("unitcode"));
			} else {
				bean.setUnitCode("");
			}
			if (request.getParameter("hiddenunitcode") != null) {
				bean.setHiddenUnitCode(request.getParameter("hiddenunitcode"));
			} else {
				bean.setHiddenUnitCode("");
			}
			if (request.getParameter("region") != null) {
				bean.setRegion(request.getParameter("region"));
			} else {
				bean.setRegion("");
			}
			if (request.getParameter("hiddenregion") != null) {
				bean.setHiddenRegion(request.getParameter("hiddenregion"));
			} else {
				bean.setHiddenRegion("");
			}
			try {
				int count = 0;
				count = cs.updateUnitMaster(bean);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UnitMasterSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitadd")) {

			log.info("UnitSearchServlet : dopost() ");
			CeilingUnitBean bean = new CeilingUnitBean();
			try {

				if (request.getParameter("unitcode") != null) {
					bean.setUnitCode(request.getParameter("unitcode"));
				} else {
					bean.setUnitCode("");
				}

				if (request.getParameter("unitoption") != null) {
					bean.setUnitOption(request.getParameter("unitoption"));
				} else {
					bean.setUnitOption("");
				}
				if (request.getParameter("unitname") != null) {
					bean.setUnitName(request.getParameter("unitname"));
				} else {
					bean.setUnitName("");
				}
				if (request.getParameter("region") != null) {
					bean.setRegion(request.getParameter("region"));
				} else {
					bean.setRegion("");
				}
				cs.addUnitRecord(bean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UnitMasterSearch.jsp");
			rd.forward(request, response);

		}
		
		/*
		 *  Financial Year Add
		 */
		
		else  if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financialyearadd")) {

			log.info("UnitSearchServlet : dopost() ");
			FinancialYearBean bean = new FinancialYearBean();
			try {

				if (request.getParameter("month") != null) {
					bean.setMonth(request.getParameter("month"));
				} else {
					bean.setMonth("");
				}

				if (request.getParameter("fromdate") != null) {
					bean.setFromDate(request.getParameter("fromdate"));
				} else {
					bean.setFromDate("");
				}
				if (request.getParameter("todate") != null) {
					bean.setToDate(request.getParameter("todate"));
				} else {
					bean.setToDate("");
				}
				if (request.getParameter("description") != null) {
					bean.setDescription(request.getParameter("description"));
				} else {
					bean.setDescription("");
				}
				if (request.getParameter("remarks") != null) {
					bean.setRemarks(request.getParameter("remarks"));
				} else {
					bean.setRemarks("");
				}
				cs.addFinancialYear(bean);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			
			searchFinanceYearData(request,response);
			
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinancialYearSearch.jsp");
			rd.forward(request, response);

		}	
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financialYearEdit")) {

			int financialId = 0;		
			financialId = Integer.parseInt(request.getParameter("financialId"));
			FinancialYearBean editBean = new FinancialYearBean();
			try {
				editBean = cs.editFinancialYear(financialId);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("EditBean", editBean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinancialYearMasterEdit.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financialYearUpdate")) {
			
			FinancialYearBean bean = new FinancialYearBean();

			try {
				if (request.getParameter("financialId") != null) {
					bean.setFinancialId(Integer.parseInt(request.getParameter("financialId").toString()));
				} else {
					bean.setFinancialId(0);
				}
				if (request.getParameter("month") != null) {
				    bean.setMonth(request.getParameter("month"));
			    } else {
			    	bean.setMonth("");
			    }
				if (request.getParameter("fromdate") != null) {
					bean.setFromDate(request.getParameter("fromdate"));
				} else {
					bean.setFromDate("");
				}
				if (request.getParameter("todate") != null) {
					bean.setToDate(request.getParameter("todate"));
				} else {
					bean.setToDate("");
				}
				if (request.getParameter("description") != null) {
					bean.setDescription(request.getParameter("description"));
				} else {
					bean.setDescription("");
				}
				if (request.getParameter("remarks") != null) {
					bean.setRemarks(request.getParameter("remarks"));
				} else {
					bean.setRemarks("");
				}
				int count = 0;
				count = cs.updateFinancialYear(bean);
				
				searchFinanceYearData(request,response);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/FinancialYearSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("financialYearNavigation")) {

			String navgationBtn = "";

			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			ArrayList clist = new ArrayList();

			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
			}
			System.out.println("strtIndex" + request.getParameter("strtindx"));
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
			FinancialYearBean dbBean=new FinancialYearBean();		
			try {
				navSearchBean = cs.searchNavigationFinancialYear(searchListBean, gridLength);
				clist = cs.getFinancialYear();		
				
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", navSearchBean);	
			request.setAttribute("FinancialYear", clist);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinancialYearSearch.jsp");
			rd.forward(request, response);
		}	

	}
	public void searchFinanceYearData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ArrayList clist = new ArrayList();
		try{
			int gl = Integer.parseInt((String) session.getAttribute("gridlength"));			
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();	
			
			searchBean= cs.searchFinancialYearData(getSearchInfo, gl);		
			request.setAttribute("searchBean", searchBean);
			
			clist = cs.getFinancialYear();		
			request.setAttribute("FinancialYear", clist);
			
			}catch (Exception e) {
				log.printStackTrace(e);
			}	

	}	

}
