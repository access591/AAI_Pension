/**
 * File       : LoginServlet.java
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

import aims.bean.PensionBean;
import aims.bean.SearchInfo;
import aims.bean.UserBean;
import aims.common.InvalidDataException;
import aims.common.JspPageConstants;
import aims.common.Log;
import aims.common.UserAccessRights;
import aims.service.PensionService;
import aims.service.UserService;
/* 
##########################################
#Date					Developed by			Issue description
#14-Feb-2012			Prasad					Implementaing Mapping in Adj Cal Screen
#04-Feb-2012			Radha					Getting aditiona User Information(loginpage) 
#########################################
*/
public class LoginServlet extends HttpServlet {
	Log log = new Log(LoginServlet.class);

	UserService userService = new UserService();

	PensionService ps = new PensionService();
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(request.getMethod());
		log.info("LoginServlet : service() Entering method");
		HttpSession session = request.getSession();

		SearchInfo navSearchBean = new SearchInfo();
	/*	if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getData")) {
			UserBean dbBeans = new UserBean();
			ArrayList airportList = new ArrayList();
			String airport = request.getParameter("airport");
			StringBuffer sb = new StringBuffer();
			try {
				if (airport != null) {
					dbBeans.setAirport(request.getParameter("airport")
							.toString());
				} else {
					dbBeans.setAirport("");
				}
				airportList = userService.getEmployee(dbBeans);
				sb.append("<AirportLists>");

				for (int i = 0; airportList != null && i < airportList.size(); i++) {
					String name = "";
					UserBean bean = (UserBean) airportList.get(i);

					sb.append("<AirportList>");
					sb.append("<AirportName>");
					if (bean.getEmployee() != null)
						name = bean.getEmployee();
					sb.append(name);
					sb.append("</AirportName>");
					sb.append("</AirportList>");
				}
				sb.append("</AirportLists>");

				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} 

	else  if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getDetails")) {
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();
			ArrayList detailsList = new ArrayList();
			StringBuffer sb1 = new StringBuffer();
			try {
				if (request.getParameter("emp") != null) {
					dbBeans.setEmployee(request.getParameter("emp").toString());
				} else {
					dbBeans.setEmployee("");
				}
				if (request.getParameter("airport") != null) {
					dbBeans.setAirport(request.getParameter("airport")
							.toString());
				} else {
					dbBeans.setAirport("");
				}

				detailsList = ns.getDetails(dbBeans);
				sb1.append("<EmpDetails>");

				for (int i = 0; detailsList != null && i < detailsList.size(); i++) {
					String desig = "", dept = "", pensionno = "", dob = "", email = "", division = "";
					UserBean bean = (UserBean) detailsList.get(i);
					sb1.append("<Detail>");
					sb1.append("<Designation>");
					if (bean.getDesignation() != null)
						desig = bean.getDesignation();
					sb1.append(desig);
					sb1.append("</Designation>");
					sb1.append("<Department>");
					if (bean.getDepartment() != null)
						dept = bean.getDepartment();
					sb1.append(dept);
					sb1.append("</Department>");
					sb1.append("<PensionNo>");
					if (bean.getPensionNo() != null)
						pensionno = bean.getPensionNo();
					sb1.append(pensionno);
					sb1.append("</PensionNo>");
					sb1.append("<DOB>");
					if (bean.getDOB() != null)
						dob = bean.getDOB();
					sb1.append(dob);
					sb1.append("</DOB>");
					sb1.append("<Email>");
					if (bean.getEmail() != null)
						email = bean.getEmail();
					sb1.append(email);
					sb1.append("</Email>");
					sb1.append("<Division>");
					if (bean.getDivision() != null)
						division = bean.getDivision();
					sb1.append(division);
					sb1.append("</Division>");
					sb1.append("</Detail>");
				}
				sb1.append("</EmpDetails>");

				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb1.toString());
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} */
		/*
		 * New User
		 */

	if (request.getParameter("method") != null
				&& request.getParameter("method").equals("newuser")) {

			UserBean bean = new UserBean();
			UserService ns = new UserService();
			String reg = "", category1 = "", category2 = "", res = "";
			
			
			if (request.getParameter("username") != null) {
				bean.setUserName(request.getParameter("username"));
			} else {
				bean.setUserName("");
			}
			if (request.getParameter("phno") != null) {
				bean.setPhoneNo(request.getParameter("phno").toString());
			} else {
				bean.setPhoneNo("");
			}
			if (request.getParameter("email") != null) {

				bean.setEmail(request.getParameter("email"));
			} else {
				bean.setEmail("");
			}

			if (request.getParameter("primarymodule") != null) {
				bean.setPrimaryModule(request.getParameter("primarymodule"));
			} else {
				bean.setPrimaryModule("");
			}
			if (request.getParameter("usertype") != null) {

				bean.setUserType(request.getParameter("usertype"));
			} else {
				bean.setUserType("");
			}
			String expdate = "";

			if (request.getParameter("expirydate") != null) {
				expdate = request.getParameter("expirydate").toString().trim();
			}
			if (request.getParameter("expiretime") != null) {
				expdate += "@"
						+ request.getParameter("expiretime").toString().trim();
			}
			if (!expdate.equals("")) {
				bean.setExpiryDate(expdate);
			} else {
				bean.setExpiryDate("");
			}
			if (request.getParameter("gridlength") != null) {
				bean.setGridLength(request.getParameter("gridlength"));
			} else {
				bean.setGridLength("");
			}
			if (request.getParameter("remarks") != null) {
				bean.setRemarks(request.getParameter("remarks"));
			} else {
				bean.setRemarks("");
			}
			if (request.getParameter("aaicategory1") != null) {
				category1 = request.getParameter("aaicategory1").toString();
			}
			if (request.getParameter("aaicategory2") != null) {
				category2 = request.getParameter("aaicategory2").toString();
			}
			if ((category1.equals("")) && (!category2.equals(""))) {
				res = category2;
			} else if ((!category1.equals("")) && (category2.equals(""))) {
				res = category1;
			} else
				res = "ALL-CATEGORIES";

			if (!res.equals("")) {
				bean.setAaiCategory(res);
			} else {
				bean.setAaiCategory("");
			}

			if (request.getParameterValues("region") != null) {
				String region[] = request.getParameterValues("region");
				try {
					for (int i = 0; i < region.length; i++) {
						if (i < region.length - 1) {
							reg += region[i] + ",";
						} else {
							reg += region[i];
						}
					}
				} catch (Exception e) {
					log.printStackTrace(e);
				}
				bean.setRegion(reg);
			} else {
				bean.setRegion("ALL-REGIONS");
			}

			try {
				ns.addNewUser(bean);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				request.setAttribute("message", e.getMessage());
				getAiirportName(request, response);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/NewUserSearch.jsp");
			rd.forward(request, response);

		}

		/*
		 * New User Search
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("searchuser")) {
			UserBean bean = new UserBean();
			UserService ns = new UserService();
			try {
				if (request.getParameter("username") != null) {
					bean.setUserName(request.getParameter("username")
							.toString());
				} else {
					bean.setUserName("");
				}
				if (request.getParameter("region") != null) {
					bean.setRegion(request.getParameter("region").toString());
				} else {
					bean.setRegion("");
				}

				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();

				log.info("gridlength " + session.getAttribute("gridlength"));
				int gridLength = 0;
				if (session.getAttribute("gridlength") != "") {
					gridLength = Integer.parseInt(session.getAttribute(
							"gridlength").toString());
				}

				searchBean = ns.searchNewUserData(bean, getSearchInfo,
						gridLength);
				request.setAttribute("searchBean", searchBean);
			}

			catch (Exception e) {
				log.printStackTrace(e);

			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/NewUserSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unitNavigation")) {

			String navgationBtn = "";
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();

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
				dbBeans = (UserBean) session.getAttribute("getSearchBean");
			}
			try {
				int gl = Integer.parseInt((String) session
						.getAttribute("gridlength"));
				navSearchBean = ns.searchNavigationUserData(dbBeans,
						searchListBean, gl);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/NewUserSearch.jsp");
			rd.forward(request, response);
		}

		/*
		 * Load a UserMasterEdit page
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("NewUserEdit")) {
			UserService ns = new UserService();
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();

			int userid = 0;
			String username = "";
			userid = Integer.parseInt(request.getParameter("userid"));
			username = request.getParameter("username");
			
			log.info("userid " + userid + "userName " + username);
			UserBean editBean = new UserBean();
			UserBean bean = new UserBean();
			try {
				editBean = ns.editUserMaster(userid, username);
				getAiirportNames(request, response);
				request.setAttribute("EditBean", editBean);
				log.info(" AaiCategory=========" + editBean.getAaiCategory());

				if (editBean.getAaiCategory() != null) {
					bean.setAaiCategory(editBean.getAaiCategory());
				} else {
					bean.setAaiCategory("");
				}
            	searchBean = ns.getRegions(bean, getSearchInfo);
				request.setAttribute("selectedRegionList", searchBean);

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/NewUserAdminEdit.jsp");
			rd.forward(request, response);

		}
		/*
		 * Update UserMaster page
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("NewUserUpdate")) {
			log.info("LoginServlet --method=NewUserUpdate");
			UserBean bean = new UserBean();
			UserService ns = new UserService();
			String reg = "", category1 = "", category2 = "", res = "";

			try {
				if (request.getParameter("module") != null) {
					bean.setPrimaryModule(request.getParameter("module"));
				} else {
					bean.setPrimaryModule("");
				}
				if (request.getParameter("userid") != null) {
					bean.setUserId(Integer.parseInt(request.getParameter(
							"userid").toString()));
				} else {
					bean.setUserId(0);
				}
				if (request.getParameter("remarks") != null) {
					bean.setRemarks(request.getParameter("remarks"));
				} else {
					bean.setRemarks("");
				}

				if (request.getParameter("gridlength") != null) {
					bean.setGridLength(request.getParameter("gridlength")
							.toString());
				} else {
					bean.setGridLength("");
				}
				if (request.getParameter("region") == null) {
					bean.setRegion("ALL-REGIONS");
				} else if (request.getParameterValues("region") != null) {
					String region[] = request.getParameterValues("region");
					try {
						for (int i = 0; i < region.length; i++) {
							if (i < region.length - 1) {
								reg += region[i] + ",";

							} else {
								reg += region[i];

							}
						}
					} catch (Exception e) {
						log.printStackTrace(e);
					}
					bean.setRegion(reg);
				} else {
					bean.setRegion("");
				}

				if (request.getParameter("aaicategory1") != null) {
					category1 = request.getParameter("aaicategory1").toString();
				}

				if (request.getParameter("aaicategory2") != null) {
					category2 = request.getParameter("aaicategory2").toString();
				}
				if ((category1.equals("")) && (!category2.equals(""))) {
					res = category2;
				} else if ((!category1.equals("")) && (category2.equals(""))) {
					res = category1;
				} else
					res = "ALL-CATEGORIES";

				if (!res.equals("")) {
					bean.setAaiCategory(res);
				} else {
					bean.setAaiCategory("");
				}

				String expdate = "";

				if (request.getParameter("expirydate") != null) {
					expdate = request.getParameter("expirydate").toString()
							.trim();
				}
				if (request.getParameter("expiretime") != null) {
					expdate += "@"
							+ request.getParameter("expiretime").toString()
									.trim();
				}

				if (!expdate.equals("")) {
					log.info("======EXPIRE DATE========" + expdate);
					bean.setExpiryDate(expdate);
				} else {
					bean.setExpiryDate("");
				}

				if (request.getParameter("usertype") != null) {
					bean.setUserType(request.getParameter("usertype")
							.toString());
				} else {
					bean.setUserType("");
				}
				if (request.getParameter("phno") != null) {
					bean.setPhoneNo(request.getParameter("phno").toString());
				} else {
					bean.setPhoneNo("");
				}

				if (request.getParameter("email") != null) {

					bean.setEmail(request.getParameter("email"));
				} else {
					bean.setEmail("");
				}

				int count = 0;
				count = ns.updateUserMaster(bean);

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/NewUserSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("checkregion")) {
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();
			StringBuffer sb1 = new StringBuffer();
			String flag = "";

			try {
				if (request.getParameter("region") != null) {
					dbBeans
							.setRegion(request.getParameter("region")
									.toString());
				} else {
					dbBeans.setRegion("");
				}
				if (request.getParameter("aaicategory") != null) {
					dbBeans.setAaiCategory(request.getParameter("aaicategory")
							.toString());
				} else {
					dbBeans.setAaiCategory("");
				}
				flag = ns.checkRegion(dbBeans);
				StringBuffer sbf = new StringBuffer();

				sbf.append("<CHECK>");
				sbf.append("<STATUS>" + flag + "</STATUS>");
				sbf.append("</CHECK>");

				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				System.out.println("=========" + sbf.toString());

				PrintWriter out = response.getWriter();
				out.write(sbf.toString());

			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}

		/*
		 * Region Master
		 * 
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("addregion")) {

			log.info("Entering into method=addregion");

			UserBean bean = new UserBean();
			UserService ns = new UserService();

			if (request.getParameter("regionname") != null) {
				bean.setRegion(request.getParameter("regionname"));
			} else {
				bean.setRegion("");
			}
			if (request.getParameter("aaicategory") != null) {
				bean.setAaiCategory(request.getParameter("aaicategory"));
			} else {
				bean.setAaiCategory("");
			}

			if (request.getParameter("remarks") != null) {
				bean.setRemarks(request.getParameter("remarks"));
			} else {
				bean.setRemarks("");
			}

			try {
				ns.addNewRegion(bean);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				request.setAttribute("groupmessage", e.getMessage());
				getAiirportNames(request, response);

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UserGroupNew.jsp");
				rd.forward(request, response);
			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/RegionSearch.jsp");
			rd.forward(request, response);

		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("searchregion")) {

			UserBean bean = new UserBean();
			UserService ns = new UserService();
			try {
				if (request.getParameter("regionname") != null) {
					bean.setRegion(request.getParameter("regionname")
							.toString().trim());
				} else {
					bean.setRegion("");
				}
				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();
				int gl = Integer.parseInt((String) session
						.getAttribute("gridlength"));
				searchBean = ns.searchRegion(bean, getSearchInfo, gl);
				request.setAttribute("searchBean", searchBean);
			}

			catch (Exception e) {
				log.printStackTrace(e);

			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/RegionSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("regionNavigation")) {

			String navgationBtn = "";
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
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

			if (session.getAttribute("getSearchBean") != null) {
				dbBeans = (UserBean) session.getAttribute("getSearchBean");
			}
			try {
				int gl = Integer.parseInt((String) session
						.getAttribute("gridlength"));
				navSearchBean = ns.searchNavigationRegionData(dbBeans,
						searchListBean, gl);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/RegionSearch.jsp");
			rd.forward(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("RegionEdit")) {
			UserService ns = new UserService();
			int regionid = 0;
			String regionname = "";
			regionid = Integer.parseInt(request.getParameter("regionid"));
			regionname = request.getParameter("regionname");
			UserBean editBean = new UserBean();
			try {
				editBean = ns.editRegionMaster(regionid, regionname);
				// getAiirportNames(request,response);
				request.setAttribute("EditBean", editBean);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/RegionEdit.jsp");
			rd.forward(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("RegionUpdate")) {
			log.info("LoginServlet --------method=RegionUpdate----------");
			UserBean bean = new UserBean();
			UserService ns = new UserService();
			try {
				if (request.getParameter("regionid") != null) {
					bean.setRegionId(Integer.parseInt(request.getParameter(
							"regionid").toString()));
				} else {
					bean.setRegionId(0);
				}
				if (request.getParameter("regionname") != null) {
					bean.setRegion(request.getParameter("regionname"));
				} else {
					bean.setRegion("");
				}

				if (request.getParameter("aaicategory") != null) {
					bean.setAaiCategory(request.getParameter("aaicategory"));
				} else {
					bean.setAaiCategory("");
				}
				if (request.getParameter("remarks") != null) {
					bean.setRemarks(request.getParameter("remarks"));
				} else {
					bean.setRemarks("");
				}

			} catch (Exception e) {
				System.out.println(e);
			}
			try {
				int count = 0;
				count = ns.updateRegionMaster(bean);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/RegionSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getregion")) {

			UserService ns = new UserService();
			UserBean bean = new UserBean();
			String  aaicategory = "";
			SearchInfo searchBean = new SearchInfo();
			String flag = "";

			try {
				SearchInfo getSearchInfo = new SearchInfo();
				aaicategory = request.getParameter("categoryname");
				if (request.getParameter("flag") != null) {
					flag = request.getParameter("flag");
				}

				if (request.getParameter("categoryname") != null) {
					bean.setAaiCategory(request.getParameter("categoryname")
							.toString());
				} else {
					bean.setAaiCategory("");
				}
				searchBean = ns.getRegions(bean, getSearchInfo);

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			UserBean editBean = new UserBean();

			if (!flag.equals("newuser")) {
				int userid = 0;
				String username = "", usertype = "";
				userid = Integer.parseInt(request.getParameter("userid"));
				username = request.getParameter("username");
				usertype = request.getParameter("usertype");
				try {
					editBean = ns.editUserMaster(userid, username);
					getAiirportNames(request, response);
					request.setAttribute("EditBean", editBean);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				request.setAttribute("aaicategory", aaicategory);

			}

			request.setAttribute("selectedRegionList", searchBean);

			if (flag.equals("newuser")) {
				UserService us = new UserService();
				ArrayList clist = new ArrayList();

				try {
					clist = us.getAllAaiCategories();
				} catch (Exception e) {
					e.printStackTrace();
				}
				request.setAttribute("clist", clist);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/NewUser.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/NewUserAdminEdit.jsp");
				rd.forward(request, response);

			}
		}

		/*
		 * Change Password
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("changepassword")
				&& request.getParameter("flag").equals("checkusername")) {
String username=request.getParameter("username");
			UserService ns = new UserService();
			ArrayList usersList = new ArrayList();

			try {
				usersList = ns.getUserNames(username);
			} catch (Exception e) {
				System.out.println(e);
			}
			request.setAttribute("UsersList", usersList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/ChangePassword.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("changepassword")
				&& request.getParameter("flag").equals("changepwd")) {

			UserService ns = new UserService();
			UserBean bean = new UserBean();
			try {
				if (request.getParameter("username") != null) {
					bean.setUserName(request.getParameter("username")
							.toString());
				} else {
					bean.setUserName("");
				}
				if (request.getParameter("oldpwd") != null) {
					bean.setOldPwd(request.getParameter("oldpwd").toString());
				} else {
					bean.setOldPwd("");
				}
				if (request.getParameter("newpwd") != null) {
					bean.setNewPwd(request.getParameter("newpwd").toString());
				} else {
					bean.setNewPwd("");
				}

				ns.confirmPwd(bean);
				RequestDispatcher rd=null;
if(session.getAttribute("usertype").toString().equals("Employee")){
	 rd = request
		.getRequestDispatcher("./PensionView/EmployeeMenu.jsp");	 
}else{
	rd= request
	.getRequestDispatcher("./PensionView/PensionMenu.jsp");

	
}
rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("home")) {
			 log.info("home");
        		response.sendRedirect("./PensionView/PensionMenu2.jsp");
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("normalusermenu")) {
			 log.info("home");
        		response.sendRedirect("./PensionView/PensionMenu3.jsp");		
		 } else if (request.getParameter("method") != null
					&& request.getParameter("method").equals("superuser")) {
			 log.info("superuser");
     		response.sendRedirect("./PensionView/PensionMenu1.jsp");		
		 } else if (request.getParameter("method") != null
					&& request.getParameter("method").equals("hrusermenu")) {
			 log.info("hrusermenu");
     		response.sendRedirect("./PensionView/PensionMenu6.jsp");		
		 } 
		 else if (request.getParameter("method") != null
					&& request.getParameter("method").equals("employeemenu")) {
			 log.info("employeemenu");
  		response.sendRedirect("./PensionLogin?method=employeemenu");		
		 } 
		/*
		 * Access Rights
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("accessrights")) {

			UserService ns = new UserService();
			ArrayList usersList = new ArrayList();

			try {
				usersList = ns.getUserNames("");
			} catch (Exception e) {
				System.out.println(e);
			}
			request.setAttribute("UsersList", usersList);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/AccessRights.jsp");
			rd.forward(request, response);
		}

		/*
		 * Assigned users
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("assignedusers")) {

			UserService ns = new UserService();
			UserBean bean = new UserBean();
			ArrayList usersList = new ArrayList();
			SearchInfo searchBean = new SearchInfo();

			try {
				SearchInfo getSearchInfo = new SearchInfo();
				if (request.getParameter("groupid") != null) {
					bean.setGroupId(Integer.parseInt(request.getParameter(
							"groupid").toString()));
				} else {
					bean.setGroupId(0);
				}
				searchBean = ns.getAssignedUserNames(bean, getSearchInfo);

				log.info("......usersList  after calling...."
						+ usersList.size());
			} catch (Exception e) {
				System.out.println(e);
			}

			request.setAttribute("selectedUsersList", searchBean);
			getGroupUserNames(request, response);

			request
					.setAttribute("GroupName", request
							.getParameter("groupname"));
			request.setAttribute("GroupId", request.getParameter("groupid"));
			log.info("======request.getAttribute(GroupName) in servlet======="
					+ request.getAttribute("GroupName"));

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/GroupAssign.jsp");
			rd.forward(request, response);
		}

		/*
		 * Group Assignment
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("groupassign")) {

			log.info("Entering into method=groupassign..");

			getGroupUserNames(request, response);
			log.info("Leaving  method=groupassign..");
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/GroupAssign.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("usergroup")) {
			UserBean bean = new UserBean();
			UserService ns = new UserService();

			if (request.getParameter("groupname") != null) {

				bean.setGroupName(request.getParameter("groupname"));
			} else {
				bean.setGroupName("");
			}
			if (request.getParameter("remarks") != null) {

				bean.setRemarks(request.getParameter("remarks"));
			} else {
				bean.setRemarks("");
			}

			try {
				ns.addNewGroup(bean);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				request.setAttribute("groupmessage", e.getMessage());
				getAiirportNames(request, response);

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UserGroupNew.jsp");
				rd.forward(request, response);
			}
			if (request.getParameter("asssign") != null) {
				getGroupUserNames(request, response);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/GroupAssign.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UserGroupSearch.jsp");
				rd.forward(request, response);
			}
		}

		/*
		 * Group Search
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("searchgroup")) {

			UserBean bean = new UserBean();
			UserService ns = new UserService();
			try {
				if (request.getParameter("groupname") != null) {
					bean.setGroupName(request.getParameter("groupname")
							.toString());
				} else {
					bean.setGroupName("");
				}
				SearchInfo getSearchInfo = new SearchInfo();
				SearchInfo searchBean = new SearchInfo();
				int gl = Integer.parseInt((String) session
						.getAttribute("gridlength"));
				searchBean = ns.searchUserGroup(bean, getSearchInfo, gl);
				request.setAttribute("searchBean", searchBean);
			}

			catch (Exception e) {
			log.info("Exception in Search New User" + e);
			}
			request.setAttribute("searchInfo", bean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UserGroupSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("groupNavigation")) {

			String navgationBtn = "";
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();
			if (request.getParameter("navButton") != null) {

				navgationBtn = request.getParameter("navButton").toString();
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

			if (session.getAttribute("getSearchBean") != null) {
				dbBeans = (UserBean) session.getAttribute("getSearchBean");
			}
			try {
				int gl = Integer.parseInt((String) session
						.getAttribute("gridlength"));
				log.info("gl---------------------------------" + gl);

				navSearchBean = ns.searchNavigationGroupData(dbBeans,
						searchListBean, gl);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UserGroupSearch.jsp");
			rd.forward(request, response);
		}

		/*
		 * Load a GroupUserEdit page
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("GroupUserEdit")) {
			UserService ns = new UserService();
			int groupid = 0;
			String groupname = "";
			groupid = Integer.parseInt(request.getParameter("groupid"));
			groupname = request.getParameter("groupname");
			UserBean editBean = new UserBean();
			try {
				editBean = ns.editGroupMaster(groupid, groupname);
				// getAiirportNames(request,response);
				request.setAttribute("EditBean", editBean);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			// HttpSession session = request.getSession();
			// log.info("........................USER
			// TYPE.................................."+session.getAttribute("usertype"));

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UserGroupEdit.jsp");
			rd.forward(request, response);
		}

		/*
		 * Update UserGroupEdit page
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("GroupUserUpdate")) {

			log.info("LoginServlet----method=GroupUserUpdate----");
			UserBean bean = new UserBean();
			UserService ns = new UserService();
			try {
				if (request.getParameter("groupid") != null) {
					bean.setGroupId(Integer.parseInt(request.getParameter(
							"groupid").toString()));
				} else {
					bean.setGroupId(0);
				}

				if (request.getParameter("groupname") != null) {
					bean.setGroupName(request.getParameter("groupname"));
				} else {
					bean.setGroupName("");
				}

				if (request.getParameter("remarks") != null) {
					log.info("***************************"
							+ request.getParameter("remarks"));
					bean.setRemarks(request.getParameter("remarks"));
				} else {
					bean.setRemarks("");
				}

				if (request.getParameter("selectgroup") != null) {
					log.info("check box value-----"
							+ request.getParameter("selectgroup"));
					bean.setSelectGroup(request.getParameter("selectgroup"));
				} else {
					bean.setSelectGroup("N");
				}

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			try {
				int count = 0;
				count = ns.updateGroupMaster(bean);
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UserGroupSearch.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}

		/*
		 * Group Assignment
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("groupmap")) {

			log.info("Entering into method=groupmap");
			UserBean bean = new UserBean();
			UserService ns = new UserService();
			ArrayList al = new ArrayList();
			if (request.getParameter("groupnames") != null) {
				bean.setGroupId(Integer.parseInt(request.getParameter(
						"groupnames").toString()));
			} else {
				bean.setGroupId(0);
			}

			int temp = 0;

			if (request.getParameterValues("assign") != null) {
				try {
					String arr[] = request.getParameterValues("assign");
					for (int i = 0; i < arr.length; i++) {
						UserBean ubean = new UserBean();
						temp = Integer.parseInt(arr[i].toString());
						if (temp != 0) {
							ubean.setUserId(temp);
						} else {
							ubean.setUserId(0);
						}
						al.add(ubean);
					}

					ns.addNewGroupAssignment(bean, al);
				} catch (Exception e) {
					log.printStackTrace(e);
				}
			} else {
				log.info(".........inside else.......");
				try {
					ns.deleteAllUsers(bean);
				} catch (Exception e) {
					log.printStackTrace(e);
				}

			}
			getGroupUserNames(request, response);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/GroupAssign.jsp");
			rd.forward(request, response);

		}

		/*
		 * Check Old Password
		 */

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("checkoldpwd")) {
			UserService ns = new UserService();
			UserBean bean = new UserBean();

			if (request.getParameter("oldpwd") != null) {
				bean.setOldPwd(request.getParameter("oldpwd"));
			} else {
				bean.setOldPwd("");
			}
			if (request.getParameter("username") != null) {
				bean.setUserName(request.getParameter("username"));
			} else {
				bean.setUserName("");
			}
			try {
				String oldpwdstatus = "";
				oldpwdstatus = ns.checkOldPassword(bean);
				StringBuffer sbf = new StringBuffer();
				sbf.append("<OLDPASSWORD>");
				sbf.append("<STATUS>" + oldpwdstatus + "</STATUS>");
				sbf.append("</OLDPASSWORD>");
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				PrintWriter out = response.getWriter();
				System.out.println(sbf.toString());
				out.write(sbf.toString());
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}
		/*
		 * Retrieving Airports List
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirport")) {

			getAiirportName(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getLoginHistory")) {
			UserBean bean = new UserBean();
			String username = "", loginFromDate = "", loginToDate = "";
			if (request.getParameter("username") != "") {
				username = request.getParameter("username");
			}
			if (request.getParameter("loginFromDate") != "") {
				loginFromDate = request.getParameter("loginFromDate");
			}

			if (request.getParameter("loginToDate") != "") {
				loginToDate = request.getParameter("loginToDate");
			}

			bean.setUserName(username);
			bean.setLoginFromDate(loginFromDate);
			bean.setLoginToDate(loginToDate);
			ArrayList logList = new ArrayList();
			try {
				logList = ps.getLogList(bean);
				request.setAttribute("logList", logList);

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/LoginHistory.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("loginpage")) {
			String path = "";
			UserService ns = new UserService();
			String loginUserId="",username = "", password = "",form_usertype="",computername="",userType ="",gridlength ="",region="",passwordChangeFlag="",airportcode="",privilages="",accountType="",userProfile="";	
			String dashBoardFlag="",userDesignation="";
			username = request.getParameter("username");
			password = request.getParameter("password");
			form_usertype = request.getParameter("usertype");
			log.info("in login action username :" + username + " password :"
					+ password);
			
			if(form_usertype.equals("Employee")){
				path = "Login.jsp";
			}else{
			
			path = "PensionView/PensionIndex.jsp";
			}
			RequestDispatcher rd = null;
			
			UserBean ubean=new UserBean();
			computername = request.getRemoteAddr();
				try {
					ubean = ns.checkUser(username, password,computername,form_usertype);
					loginUserId=String.valueOf(ubean.getUserId());
					userType=ubean.getUserType();
					userDesignation=ubean.getDesignation();
					gridlength=ubean.getGridLength();				
					region=ubean.getRegion();	
					airportcode=ubean.getStationName();
					passwordChangeFlag=ubean.getPasswordChangeFlag();
					accountType=ubean.getUserAccType();
					privilages=ubean.getUserPrevilieges();
					userProfile=ubean.getUserProfile();
					dashBoardFlag=ubean.getDbFlag();
				    path = "/PensionView/common/PensionCommon.jsp";
					session.setAttribute("gridlength", gridlength);
					if (region != null) {
						log.info("*****USER REGION*******" + region);
						String[] reglist = region.split(",");
						session.setAttribute("region", reglist);
					}
					if(airportcode!=null){
						session.setAttribute("station", airportcode);
					}
					if (userType != null) {
						session.setAttribute("usertype", userType);
					}
					
					if(userType != null && userType.equals("Employee")){
						try{
					ArrayList	mappedList = ps.getEmployeeMappedList("", "",
								"", username,"");
						PensionBean  empData =  null;
						for(int i=0;i<mappedList.size();i++){
						   empData = (PensionBean)mappedList.get(i);
						}
						
						session.setAttribute("empData", empData);
						}catch(Exception e){
							e.printStackTrace();
							
						}
						if(passwordChangeFlag.equals("N")){
							path="PensionLogin?method=changepassword&flag=checkusername&username="+username;
						}
					}
					
					
					session.setAttribute("passwordChangeFlag", passwordChangeFlag);
					session.setAttribute("privilages", privilages);
					session.setAttribute("accountType", accountType);
					session.setAttribute("profileType", userProfile);
					session.setAttribute("loginUserId", loginUserId);
					session.setAttribute("loginUsrRegion", region);
					session.setAttribute("loginUsrDesgn", userDesignation);
					
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					request.setAttribute("message",e.getMessage());
					System.out.println(e.getMessage());
				}
				request.setAttribute("DashBoardFlag",dashBoardFlag);
				request.setAttribute("usernames",username.trim());
				session.setAttribute("computername", computername);
				session.setAttribute("userid", username.trim());
				log.info("Username=============="+ubean.getUsername());
				session.setAttribute("userdata",ubean);
				session.setAttribute("userinfo", username.trim()+"-"+session.getId());
		
			log.info("path " + path);
			log.info("LoginServlet : service() leaving method");
			
			
			rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		}
		/*
		 * Retrieving AAI Categories List
		 */
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAaiCategory")) {

			UserService us = new UserService();
			ArrayList clist = new ArrayList();
			try {
				clist = us.getAllAaiCategories();
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("clist", clist);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/NewUser.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getregions")) {
			UserBean dbBeans = new UserBean();
			UserService ns = new UserService();
			ArrayList detailsList = new ArrayList();
			StringBuffer sb1 = new StringBuffer();

			try {
				if (request.getParameter("categoryname") != null) {
					log.info("categoryname............"
							+ request.getParameter("categoryname"));
					dbBeans.setAaiCategory(request.getParameter("categoryname")
							.toString());
				} else {
					dbBeans.setAaiCategory("");
				}
				detailsList = ns.getRegionData(dbBeans);
				sb1.append("<RegionDetails>");

				for (int i = 0; detailsList != null && i < detailsList.size(); i++) {
					String regionname = "";
					UserBean bean = (UserBean) detailsList.get(i);
					sb1.append("<Detail>");
					sb1.append("<RegionName>");
					if (bean.getDepartment() != null)
						regionname = bean.getRegion();
					sb1.append(regionname);
					sb1.append("</RegionName>");
					sb1.append("</Detail>");
				}
				sb1.append("</RegionDetails>");

				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb1.toString());
				System.out.println("........" + sb1.toString());
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("logoff")) {
			String userName = (String)session.getAttribute("userid");
			 
			if (session != null) {
				request.getSession().removeAttribute("userid"); 
				request.getSession().removeAttribute("userdata"); 
				request.getSession().invalidate();
				request.getSession().setMaxInactiveInterval(0);
			}
			System.out.println("Prototcal"+request.getProtocol());;
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache"); 
			response.setDateHeader("Expires", 0); //prevents caching at the proxy server
			System.out.println("LoginServlet::logoof()--sessionId--"+session.getId()+request.isRequestedSessionIdFromCookie()+"ReuquesdValu"+request.isRequestedSessionIdValid());
			System.out.println("User "+userName+" Log Out Successfully");
			/*RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionIndex.jsp");
			rd.forward(request, response);*/
			response.sendRedirect("./PensionView/PensionIndex.jsp");
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("elogoff")) {
			String userName = (String)session.getAttribute("userid");
			 
			if (session != null) {
				request.getSession().removeAttribute("userid"); 
				request.getSession().removeAttribute("userdata"); 
				request.getSession().invalidate();
				request.getSession().setMaxInactiveInterval(0);
			}
			System.out.println("Prototcal"+request.getProtocol());;
			response.setHeader("Cache-Control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache"); 
			response.setDateHeader("Expires", 0); //prevents caching at the proxy server
			System.out.println("LoginServlet::logoof()--sessionId--"+session.getId()+request.isRequestedSessionIdFromCookie()+"ReuquesdValu"+request.isRequestedSessionIdValid());
			System.out.println("User "+userName+" Log Out Successfully");
			/*RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionIndex.jsp");
			rd.forward(request, response);*/
			response.sendRedirect("./Login.jsp");
		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("updatePasswordFlag")) {
			String userName=request.getParameter("userName").toString();
			try{
			userService.updatePasswordFlag(userName,"");
			 session.setAttribute("passwordChangeFlag","Y");
			}catch(Exception e){
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/common/PensionCommon.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("forgotpassword")) {
			String userName=request.getParameter("user").toString();
			String doj=request.getParameter("doj").toString();
			String msg="";
			try{
			msg=userService.forgotpassword(userName,doj);
			 session.setAttribute("msg",msg);
			}catch(Exception e){
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./ForgotPassword.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method").equals("loadUserAccessRights")) { 
		 	session=request.getSession(true);
			String path="",status="";
			session=request.getSession(true);
		
			status=UserAccessRights.getAccess((String)session.getAttribute("loginUserId"),JspPageConstants.USER_WISE_ACCESS_RIGHTS);
		  
			if(status.equals("N")){
				path="./PensionView/common/AccessDenied.jsp";
			}else{
				path="./PensionView/UserAccessRights.jsp";
			}
			 
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		
	   }else if (request.getParameter("method").equals("updateUserAccessRights")) { 
		String path="",user ="",mode ="";
		String access[] = null;
		if (request.getParameter("user") != null) {
			user = request.getParameter("user");
		}
		if (request.getParameter("mode") != null) {
			mode = request.getParameter("mode");
		}
		  access  = request.getParameterValues("access");
		 
		if(mode.equals("N")){ 
				try {
					userService.updateAccessRight(user,access);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		request.setAttribute("user", user);
		 
		RequestDispatcher rd = request.getRequestDispatcher("./PensionView/UserAccessRights.jsp");
		rd.forward(request, response);
	 }
	}

	public void getAiirportName(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList airportsList = new ArrayList();
		try {
			airportsList = userService.getAirports();
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		request.setAttribute("AirportsList", airportsList);
		RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/NewUser.jsp");
		rd.forward(request, response);
	}

	public void getAiirportNames(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList airportsList = new ArrayList();
		try {
			airportsList = userService.getAirports();
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		request.setAttribute("AirportsList", airportsList);

	}

	public void getGroupUserNames(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		UserService ns = new UserService();
		SearchInfo searchBean1 = new SearchInfo();
		SearchInfo searchBean2 = new SearchInfo();
		try {
			SearchInfo getSearchIn = new SearchInfo();
			SearchInfo getSearchInfo = new SearchInfo();
			searchBean1 = ns.getGroupNames(getSearchIn);
			searchBean2 = ns.getExistingUserNames(getSearchInfo);
		} catch (Exception e) {
			System.out.println(e);
		}
		request.setAttribute("GroupList", searchBean1);
		request.setAttribute("UserList", searchBean2);
	}

}