/**
 * File       : SearchPensionMasterServlet.java
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.EmpMasterBean;
import aims.bean.EmployeePersonalInfo;
import aims.bean.PensionBean;
import aims.bean.PensionContBean;
import aims.bean.SearchInfo;
import aims.bean.FinalSettlementBean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.PensionDAO;
import aims.service.AdjCrtnService;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PensionService;
/* 
##########################################
#Date					Developed by			Issue description
#30-Apr-2012			Prasanthi				For getting the finyear for resettlament in getEmployeeFilasettledateinfo
#23-Feb-2012			Radha					For Seperating methods related to AdjCalc Screen
#27-Jan-2012            Prasanthi               geting the finalsettlementinformation and finalsettlementlog for editsettlementinfo screen(getfinalsettlementlog,getfinalsettlementDetails)
#17-Jan-2012            Radha               	Chking PFId whethe changes done thru AdjScreen or not(getEmployeeMappedList)
#13-Jan-2012            Prasanthi               Changes in editsettlementinfo regarding the dateofseperation
#07-Dec-2011        	Prasanthi			    Method Added for Edit SettlementDate (getEmployeeFilasettledateinfo,editSettlementInfo)
#29-Dec-2011        	Prasanthi			    Changes for editsettlement information(editSettlementInfo)
#########################################
*/
public class SearchPensionMasterServlet extends HttpServlet {

	Log log = new Log(SearchPensionMasterServlet.class);
	FinancialReportService finReportService = new FinancialReportService();
	CommonUtil commonUtil = new CommonUtil();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("Search Pension Master Servlet . java ");
		PensionService ps = new PensionService();
		FinancialService fs = new FinancialService();
		AdjCrtnService adjCrtnService = new AdjCrtnService();
		String cpfacno = "", cpfaccno1 = "";
		HttpSession session = request.getSession();
		int gridLength = Integer.parseInt((String) session
				.getAttribute("gridlength"));
		log.info("gridLength " + gridLength);
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getMaxCpfAccNo")) {
			String maxCpfacno = "", recordExist = "";
			ArrayList airportList = new ArrayList();
			ArrayList departmentList = new ArrayList();
			// ArrayList airportList1=null;
			try {
				maxCpfacno = ps.getMaxCpfAccNo();
				String region = request.getParameter("region");
				airportList = ps.getAirports(region);
				departmentList = ps.getDepartmentList();
				if (request.getParameter("recordExist") != null) {
					recordExist = request.getParameter("recordExist");
					request.setAttribute("recordExist", recordExist);
				}
				log.info("airport list " + airportList.size());
				log.info("departmentList " + departmentList.size());

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			request.setAttribute("MaxCpfacno", maxCpfacno);
			request.setAttribute("AirportList", airportList);
			request.setAttribute("DepartmentList", departmentList);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMaster.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchPensionMasterInvalidRecords")) {
			log.info("SearchPensionMasterServlet : dopost() inside if");
			String airportCode = "", employeeName = "", desegnation = "";
			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("airPortCode") != null)
				airportCode = request.getParameter("airPortCode").toString()
						.trim();
			empBean.setStation(airportCode);
			log.info(airportCode);
			if (request.getParameter("empName") != null)

				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			if (request.getParameter("desegnation") != null)
				empBean.setDesegnation(request.getParameter("desegnation")
						.toString().trim());
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAcNo(request.getParameter("cpfaccno").toString());
			} else {
				empBean.setCpfAcNo("");
			}
			if (request.getParameter("employeeCode") != null) {
				empBean.setEmpNumber(request.getParameter("employeeCode")
						.toString().trim());
			} else
				empBean.setEmpNumber("");

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = false;
			try {
				searchBean = ps.searchPensionMaster(empBean, getSearchInfo,
						flag, gridLength, "");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			log.info("searchBean total records" + searchBean.getTotalRecords());
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchBean1", searchBean);
			request.setAttribute("searchInfo", empBean);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterInvalidRecords.jsp");
			rd.forward(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("edit")) {

			String empName = "", empCode = "", region = "";
			String editFrom = "";
			String empSerailNumber1 = "", dateofbirth1 = "", dob1 = "";
			String employeeSearchName = "";
			String arrearInfo = "";
			cpfacno = request.getParameter("cpfacno").trim();
			String startIndex = "", totalData = "";
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			if (request.getParameter("arrearInfo") != null) {
				arrearInfo = request.getParameter("arrearInfo").toString();
			} else {
				arrearInfo = "";
			}
			log.info("arrearInfo test " + arrearInfo);

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

			System.out.println("===edit=====empName==" + empName
					+ "cpfacno=====" + cpfacno + "flag========" + flag);
			ArrayList airportList = new ArrayList();
			ArrayList departmentList = new ArrayList();
			EmpMasterBean editBean = new EmpMasterBean();
			try {
				editBean = ps.editPensionMaster(cpfacno, empName, flag,
						empCode, region.trim());

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
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterEdit.jsp");
			rd.forward(request, response);

		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("delete")) {
			String empName = "", empCode = "", region = "", airportCode = "";

			boolean flag;
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;

			} else {
				flag = true;
			}

			String[] cpfaccdetails = {};
			log.info("delete items  " + request.getParameterValues("cpfno"));
			if (request.getParameterValues("cpfno") != null) {
				cpfaccdetails = (String[]) request.getParameterValues("cpfno");
			}
			for (int i = 0; i < cpfaccdetails.length; i++) {
				System.out
						.println("The Value is"
								+ request.getParameter("cpfnostring"
										+ cpfaccdetails[i]));
				String deleteCpfacnoDetails = (String) request
						.getParameter("cpfnostring" + cpfaccdetails[i]);

				String[] temp = null;
				temp = deleteCpfacnoDetails.split(",");
				cpfacno = temp[0];
				empName = temp[1];
				empCode = temp[2];
				region = temp[3];
				airportCode = temp[4];

				try {
					ps.deletePensionMaster(cpfacno, empName, flag, empCode,
							region, airportCode);

				} catch (Exception e) {
					log.printStackTrace(e);
				}
			}

			try {

			} catch (Exception e) {
				log.printStackTrace(e);
			}
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterSearch.jsp");
			rd.forward(request, response);

		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("process")) {
			String empName = "", empCode = "", region = "", airportCode = "", dateofBirth = "", dateofJoining = "";
			int sequenceNumber = 0;
			boolean flag;
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;

			} else {
				flag = true;
			}

			String arrearInfoString = "";
			if (request.getParameter("arrearInfoString") != null) {
				arrearInfoString = request.getParameter("arrearInfoString")
						.toString();
			}
			String temp1[] = arrearInfoString.split("-");
			String[] cpfaccdetails = {};
			if (request.getParameterValues("cpfno") != null) {
				cpfaccdetails = (String[]) request.getParameterValues("cpfno");
				//empSerialnumberList=(String[]) request.getParameterValues("empSerialnumber");
			}
			log.info("cpfaccdetails length " + cpfaccdetails.length);
			boolean firstTime = true;
			boolean firstTime_flag = true;
			String tempCPF = "";
			String tempDob = "";
			String tempEmpName = "";
			String cpfacnumbers = "";
			String newPensionNumber = "";
			ArrayList tempList = new ArrayList();
			ArrayList tempDobList = new ArrayList();
			for (int i = 0; i < cpfaccdetails.length; i++) {
				//	String selectCpfacnoDetails = (String) request.getParameter("cpfnostring" + cpfaccdetails[i]);
				String selectCpfacnoDetails = cpfaccdetails[i];
				System.out.println("selectCpfacnoDetails  "
						+ selectCpfacnoDetails);
				String[] temp = null;
				temp = selectCpfacnoDetails.split(",");
				cpfacno = temp[0];
				cpfacnumbers = cpfacnumbers + "," + cpfacno;
				empName = temp[1].trim();
				if (!temp[2].equals(null)) {
					empCode = temp[2];
				} else
					empCode = "";
				region = temp[3];
				if (!temp[4].equals(null)) {
					airportCode = temp[4];
				} else
					airportCode = "";
				log.info("temp 5 " + temp[5]);
				if (!temp[5].equals("")) {
					dateofBirth = temp[5].trim();
				} else
					dateofBirth = "";

				if (!temp[6].equals("")) {
					dateofJoining = temp[6];
				} else
					dateofJoining = "";
				//	String empSerialNumber =empSerialnumberList[i];

				/*if(empSerialNumber.contains("batch")){
				 sequenceNumber = ps.getSequenceNumber(empName, dateofBirth,false);
				 }*/
				if (tempCPF.equals("")) {
					tempCPF = cpfacno;
					tempDob = dateofBirth;
					tempEmpName = empName;
				}
				//  tempList.add(cpfacno);
				tempList.add(empName);
				tempDobList.add(dateofBirth);
				if (firstTime_flag == false) {
					log.info("Size============" + tempList.size());
					for (int k = 0; k < tempList.size(); k++) {
						tempEmpName = (String) tempList.get(k);
						tempDob = (String) tempDobList.get(k);
						if ((!tempEmpName.equals(empName) && (!tempEmpName
								.equals("") && !tempEmpName.equals("")))
								&& (!tempDob.equals(dateofBirth) && (!tempDob
										.equals("") && !tempDob.equals("")))) {
							firstTime = true;
							break;

						} else {
							firstTime = false;

						}
					}

				}

				if (firstTime) {
					firstTime_flag = false;
					sequenceNumber = ps.getSequenceNumber(empName, dateofBirth,
							true);
				}

				try {
					// generateUniquePensionNumber
					newPensionNumber = ps.generateUniquePensionNumber(
							sequenceNumber, cpfacno, empName, flag, empCode,
							region, airportCode, dateofBirth);

				} catch (Exception e) {
					log.printStackTrace(e);
				} finally {

				}
			}
			cpfacnumbers = cpfacnumbers.substring(1, cpfacnumbers.length());
			log.info("cpfacnos mapped is " + cpfacnumbers + "newPensionNumber"
					+ newPensionNumber);
			//	RequestDispatcher rd = request.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
			RequestDispatcher rd = request
					.getRequestDispatcher("./search1?method=searchRecordsbyEmpSerailNo&cpfnumbers="
							+ cpfacnumbers
							+ "&newPensionNumber="
							+ newPensionNumber);
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("process1")) {
			ArrayList list = null;
			String empName = "", empCode = "", region = "", airportCode = "", dateofBirth = "", dateofJoining = "";
			String uncheckList = "", arrearInfo = "", cpfaccno = "";
			int sequenceNumber = 0;
			Connection con = null;
			Statement st = null;
			boolean flag;
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;

			} else {
				flag = true;
			}
			String arrearInfoString = "";
			String pensionNumber = "";
			String empSerialnumber = "";
			String sequnceFlag = "N";
			log.info(request.getParameter("arrearInfoString"));
			if (request.getParameter("arrearInfoString") != null
					&& !request.getParameter("arrearInfoString").toString()
							.equals("")) {
				arrearInfoString = request.getParameter("arrearInfoString")
						.toString();

				String temp1[] = arrearInfoString.split("@");

				if (temp1.length == 6) {

					pensionNumber = temp1[3];
					empSerialnumber = temp1[4];
				} else {

					pensionNumber = temp1[3];
					empSerialnumber = temp1[5];
				}

				log.info("arrearInfoString length" + temp1.length);

				sequenceNumber = Integer.parseInt(empSerialnumber);
			} else {
				PensionService ps1 = new PensionService();
				sequenceNumber = Integer.parseInt(ps1.getSequenceNo());
				sequnceFlag = "Y";
			}

			String[] cpfaccdetails = {};

			if (request.getParameterValues("cpfno") != null) {
				cpfaccdetails = (String[]) request.getParameterValues("cpfno");
				//empSerialnumberList=(String[]) request.getParameterValues("empSerialnumber");
			}
			log.info("cpfaccdetails length " + cpfaccdetails.length);
			String cpfacnumbers = "";
			String newPensionNumber = "";
			ArrayList tempList = new ArrayList();
			ArrayList tempDobList = new ArrayList();
			for (int i = 0; i < cpfaccdetails.length; i++) {
				String selectCpfacnoDetails = cpfaccdetails[i];
				System.out.println("selectCpfacnoDetails  "
						+ selectCpfacnoDetails);
				String[] temp = null;
				temp = selectCpfacnoDetails.split(",");
				cpfacno = temp[0];
				cpfacnumbers = cpfacnumbers + "," + cpfacno;
				empName = temp[1].trim();
				if (!temp[2].equals(null)) {
					empCode = temp[2];
				} else
					empCode = "";
				region = temp[3];
				if (!temp[4].equals(null)) {
					airportCode = temp[4];
				} else
					airportCode = "";
				log.info("temp 5 " + temp[5]);
				if (!temp[5].equals("")) {
					dateofBirth = temp[5].trim();
				} else
					dateofBirth = "";

				if (!temp[6].equals("")) {
					dateofJoining = temp[6];
				} else
					dateofJoining = "";

				//  tempList.add(cpfacno);
				try {
					String computerName = session.getAttribute("computername")
							.toString();
					String lastactive = commonUtil
							.getCurrentDate("dd-MMM-yyyy");
					String username = session.getAttribute("userid").toString();
					ps.updateUniquePensionNumber(sequenceNumber, cpfacno,
							empName, flag, empCode, region, airportCode,
							dateofBirth, pensionNumber, sequnceFlag,
							computerName, lastactive, username);
					sequnceFlag = "N";
				} catch (Exception e) {
					log.printStackTrace(e);

				} finally {

				}
			}
			cpfacnumbers = cpfacnumbers.substring(1, cpfacnumbers.length());
			log.info("cpfacnos mapped is " + cpfacnumbers + "newPensionNumber"
					+ newPensionNumber);
			//  RequestDispatcher rd = request.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
			RequestDispatcher rd = request
					.getRequestDispatcher("./search1?method=searchRecordsbyEmpSerailNo&cpfnumbers="
							+ cpfacnumbers
							+ "&newPensionNumber="
							+ newPensionNumber);
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("addtoProcess")) {
			String uncheckList = "", empSerailNumber = "", pensionNumber = "";
			if (!request.getParameter("empSerailNumber").trim().equals("")) {
				empSerailNumber = request.getParameter("empSerailNumber")
						.trim();
			}
			if (!request.getParameter("pensionNumber").trim().equals("")) {
				pensionNumber = request.getParameter("pensionNumber").trim();
			}

			if (!request.getParameter("checklist").equals("")) {
				uncheckList = (String) request.getParameter("checklist");
				log.info("unchecklist " + uncheckList);
				uncheckList.substring(2, uncheckList.length());
			}
			String[] temp1 = null;
			temp1 = uncheckList.split(",");
			log.info("checklist length " + temp1.length);
			for (int i = 0; i < temp1.length; i++) {
				String uncheckCpfacno = temp1[i];

				try {
					if (!uncheckCpfacno.equals("")) {
						ps.updatePensionCheck(uncheckCpfacno, empSerailNumber,
								pensionNumber);
					}
				} catch (Exception e) {
					log.printStackTrace(e);
				}
			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UniquePensionNumberSearch.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("unprocessList")) {
			String cpfAccno = "", region = "";

			String empCode = "";
			String empSerailNumber = "", pensionNumber = "";
			if (!request.getParameter("cpfacno").trim().equals("")) {
				cpfAccno = request.getParameter("cpfacno").trim();
			}
			if (!request.getParameter("empCode").trim().equals("")) {
				empCode = request.getParameter("empCode").trim();
			}

			if (!request.getParameter("region").equals("")) {
				region = (String) request.getParameter("region");
			}
			String uncheckCpfacno = cpfAccno + "$" + region + "$" + empCode;
			empSerailNumber = "";

			try {
				ps.updatePensionCheck(uncheckCpfacno, empSerailNumber,
						pensionNumber);
			} catch (Exception e) {
				log.printStackTrace(e);

			}

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UniquePensionNumberSearch.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getPensionNumber")) {

			cpfacno = request.getParameter("cpfacno");
			String empName = request.getParameter("empName");
			String dateofbirth = request.getParameter("dateofbirth");
			PensionService ps1 = new PensionService();
			String pensionNumber = "";
			// pensionNumber=ps1.getPensionNumber(empName,dateofbirth,cpfacno);

			request.setAttribute("pensionNumber", pensionNumber);
			// EmpMasterBean bean1=new EmpMasterBean();
			// log.info(request.getAttribute("EditBean").toString());
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterEdit.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("employeesearch")) {
			ArrayList userList = new ArrayList();
			userList=commonUtil.getUserList();
			request.setAttribute("userNameList", userList);
	          RequestDispatcher rd = request.getRequestDispatcher("./PensionView/personal/EmployeeFinalsettlementDateEdit.jsp");
				rd.forward(request, response);
		}else if (request.getParameter("method").equals("editSettlementInfo")) {log.info("SerchPensionMasterServlet:editSettlementInfo:---Entering method");
		String pfid = "", recoverieTable = "",editid = "",clientname="",remarks="",seperationreason="";
		String seperationdate = "", intrestcalcfinaldate = "", interestcalcDate = "", resettlementdate = "",reintrestcalcdate = "";
		String oldeseperationdate = "", oldintrestcacfinaldate = "", oldintrestcalcdate = "", oldresettlementdate = "",oldreintrestcalcdate = "";
		if (request.getParameter("pensionNo") != null) {
			pfid = commonUtil.getSearchPFID1(request.getParameter(
					"pensionNo").toString());
		}
		log.info("pfid"+pfid);
		if (request.getParameter("seperationdate") != null) {
			seperationdate = request.getParameter("seperationdate");
		}
		log.info("seperationdate"+seperationdate);
		if (request.getParameter("interestCalcfinaldate") != null) {
			intrestcalcfinaldate = request.getParameter("interestCalcfinaldate");
		}
		log.info("intrestcalcfinaldate"+intrestcalcfinaldate);
		if (request.getParameter("interestCalcdate") != null) {
			interestcalcDate = request.getParameter("interestCalcdate");
		}
		log.info("interestcalcDate"+interestcalcDate);
		if (request.getParameter("resettlement") != null) {
			resettlementdate = request.getParameter("resettlement");
		}
		log.info("resettlementdate"+resettlementdate);
		if (request.getParameter("reinterestCalcdate") != null) {
			reintrestcalcdate = request.getParameter("reinterestCalcdate");
		}
		log.info("reintrestcalcdate"+reintrestcalcdate);
		if (request.getParameter("clientname") != null) {
			clientname = request.getParameter("clientname");
		}
		log.info("clientname"+clientname);
		if (request.getParameter("remarks") != null) {
			remarks = request.getParameter("remarks");
		}	
		log.info("remarks"+remarks);
		if (request.getParameter("seperationreason") != null) {
			seperationreason = request.getParameter("seperationreason");
		}
		log.info("seperationreason"+seperationreason);
		  oldeseperationdate=request.getParameter("oldeseperationdate");
		  oldintrestcacfinaldate=request.getParameter("oldintrestcacfinaldate");
		  oldintrestcalcdate=request.getParameter("oldintrestcalcdate");
		  oldresettlementdate=request.getParameter("oldresettlementdate");
		  oldreintrestcalcdate=request.getParameter("oldreintrestcalcdate"); 	  			
		if(seperationdate.equals(oldeseperationdate) && interestcalcDate.equals(oldintrestcalcdate) && resettlementdate.equals(oldresettlementdate) && reintrestcalcdate.equals(oldreintrestcalcdate) && !intrestcalcfinaldate.equals(oldintrestcacfinaldate)){
			recoverieTable="interestCalcfinal";
		}else if(seperationdate.equals(oldeseperationdate) && intrestcalcfinaldate.equals(oldintrestcacfinaldate) && resettlementdate.equals(oldresettlementdate) && reintrestcalcdate.equals(oldreintrestcalcdate) && !interestcalcDate.equals(oldintrestcalcdate)){
			recoverieTable="interestCalc";
		}else if(seperationdate.equals(oldeseperationdate) && intrestcalcfinaldate.equals(oldintrestcacfinaldate) && interestcalcDate.equals(oldintrestcalcdate) && reintrestcalcdate.equals(oldreintrestcalcdate) && !resettlementdate.equals(oldresettlementdate)){
			recoverieTable="resettlement";
		}else if(seperationdate.equals(oldeseperationdate) && intrestcalcfinaldate.equals(oldintrestcacfinaldate) && interestcalcDate.equals(oldintrestcalcdate) && resettlementdate.equals(oldresettlementdate) && !reintrestcalcdate.equals(oldreintrestcalcdate)){
			recoverieTable="reinterestCalc";
		}else if(intrestcalcfinaldate.equals(oldintrestcacfinaldate) && interestcalcDate.equals(oldintrestcalcdate) && resettlementdate.equals(oldresettlementdate) && reintrestcalcdate.equals(oldreintrestcalcdate) && !seperationdate.equals("")){
			recoverieTable="seperationdate";
		} else {
			recoverieTable="all";
		}
		
		pfid = commonUtil.trailingZeros(pfid.toCharArray());
		String userId = session.getAttribute("userid").toString();
		log.info("pfid" + pfid + "editInterestCalcDate" + interestcalcDate+"recoverieTable"+recoverieTable);
		
		finReportService.editSettlementInfo(pfid,seperationdate, intrestcalcfinaldate, interestcalcDate, resettlementdate ,reintrestcalcdate,userId,recoverieTable,clientname,remarks,seperationreason,oldintrestcacfinaldate,oldresettlementdate,oldintrestcalcdate);
		String message="Data saved Successfully";
		
		request.setAttribute("msg", message);
		 RequestDispatcher rd = request
			.getRequestDispatcher("./search1?method=employeesearch");
			rd.forward(request, response);
			log.info("SerchPensionMasterServlet:editSettlementInfo:---leaving method");
	   
}else if (request.getParameter("method") != null	&& request.getParameter("method").equals(
				"getEmployeeFilasettledateinfo")) {
	log.info("getEmployeeFilasettledateinfo () ");
	String region = request.getParameter("region").toString();
	if (region.equals("[Select One]")) {
		region = "";
	}
	String empName = request.getParameter("empName").toString().trim();
	String pfId = request.getParameter("pfId").toString().trim();
	pfId = commonUtil.getSearchPFID1(pfId.toString().trim());	
	ArrayList mappedList = new ArrayList();
	boolean flag = true;
	String finYearList = "";
	try {
		mappedList = ps.getEmployeeFilasettledateinfo(region, empName.trim(), pfId);
		finYearList= commonUtil.getFinalSettlemetDate(pfId);
		log.info("finYearList"+finYearList);		
		request.setAttribute("MappedList", mappedList);	
		request.setAttribute("finyearList", finYearList);	
		request.setAttribute("ArrearInfo", "");
		log.info("Mapped LIst " + mappedList.size());		
		RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/personal/FinalSettlementInfo.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		log.printStackTrace(e);
	}
}else if (request.getParameter("method") != null	&& request.getParameter("method").equals(
				"getfinalsettlementlog")) {
	log.info("getfinalsettlementinfo () ");
	String pfId = request.getParameter("pfId").toString().trim();
	pfId = commonUtil.getSearchPFID1(pfId.toString().trim());	
	ArrayList finalsettlementlog = new ArrayList();
	boolean flag = true;
	try {	
		finalsettlementlog = ps.finalsettlementlog(pfId);		
		request.setAttribute("FinalSettlementLog",finalsettlementlog);
		request.setAttribute("ArrearInfo", "");		
		log.info("FinalsettlementList " +finalsettlementlog.size());	
		RequestDispatcher rd = request.getRequestDispatcher("./PensionView/personal/FinalSettlementInformation.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		log.printStackTrace(e);
	}
}else if (request.getParameter("method") != null
		&& request.getParameter("method").equals("getfinalsettlementDetails")) {
	log.info("getfinalsettlementinfo () ");
	try{
	String pfId = request.getParameter("pfId").toString().trim();
	pfId = commonUtil.getSearchPFID1(pfId.toString().trim());	;
	PensionService ps1 = new PensionService();
	//FinalSettlementBean ServiceType = null;
	//ServiceType = ps1.getfinalsettlementDetails(pfId);
	ArrayList dataList = new ArrayList();
	dataList =ps1.getEmployeeFinalasettleminfo(pfId);	
	StringBuffer sb = new StringBuffer();
	sb.append("<ServiceTypes>");
	for (int i = 0; dataList != null && i < dataList.size(); i++) {				
		FinalSettlementBean ServiceType = (FinalSettlementBean) dataList.get(i);	
	sb.append("<ServiceType>");
	sb.append("<pensionno>");			
	sb.append(ServiceType.getPensionNo());
	sb.append("</pensionno>");
	sb.append("<purpose>");
	sb.append(ServiceType.getPurpose());
	sb.append("</purpose>");
	sb.append("<employeename>");
	sb.append(ServiceType.getEmployeeName());
	sb.append("</employeename>");
	sb.append("<finemp>");
	sb.append(ServiceType.getFinEmp());
	sb.append("</finemp>");
	sb.append("<finaai>");
	sb.append(ServiceType.getFinAai());
	sb.append("</finaai>");
	sb.append("<pencon>");
	sb.append(ServiceType.getPenCon());
	sb.append("</pencon>");
	sb.append("<netamount>");
	sb.append(ServiceType.getNetAmount());
	sb.append("</netamount>");
	sb.append("<settlementdate>");
	sb.append(ServiceType.getSettlementDate());
	sb.append("</settlementdate>");
	sb.append("<airportcode>");
	sb.append(ServiceType.getAirportCode());
	sb.append("</airportcode>");
	sb.append("<region>");
	sb.append(ServiceType.getRegion());
	sb.append("</region>");
	sb.append("</ServiceType>");
	}
	sb.append("</ServiceTypes>");
	response.setContentType("text/xml");
	PrintWriter out = response.getWriter();
	out.write(sb.toString());
	
	// request.setAttribute("cpfnoDeatails", bean);
	// RequestDispatcher rd =
	// request.getRequestDispatcher("./PensionView/AddFinancialDetail.jsp");
	// rd.forward(request, response);
} catch (Exception e) {
	log.printStackTrace(e);
}
}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {

			PensionService ps1 = new PensionService();
			ArrayList ServiceType = null;
			String region = (String) request.getParameter("region");
			String username = session.getAttribute("userid").toString();
			String finalusername = username.substring(username.length() - 3,
					username.length());
			boolean value = CommonUtil.checkIfNumber(finalusername);
			if (value == true) {
				ServiceType = ps1.getUserAirports(region, username, "");
			} else {
				ServiceType = ps1.getUserAirports(region, "", "");
			}
			//ServiceType = ps1.getAirports(region);
			log.info("airport list " + ServiceType.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			for (int i = 0; ServiceType != null && i < ServiceType.size(); i++) {
				String name = "", code = "";
				EmpMasterBean bean = (EmpMasterBean) ServiceType.get(i);

				sb.append("<ServiceType>");
				sb.append("<airPortName>");
				if (bean.getStation() != null)
					name = bean.getStation().replaceAll("<", "&lt;")
							.replaceAll(">", "&gt;");
				sb.append(name);
				sb.append("</airPortName>");
				sb.append("<airPortCode>");
				if (bean.getUnitCode() != null)
					code = bean.getUnitCode().replaceAll("<", "&lt;")
							.replaceAll(">", "&gt;");
				sb.append(code);
				sb.append("</airPortCode>");
				sb.append("<selected>");
				sb.append("true");
				sb.append("</selected>");
				sb.append("</ServiceType>");
			}
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

			// request.setAttribute("cpfnoDeatails", bean);
			// RequestDispatcher rd =
			// request.getRequestDispatcher("./PensionView/AddFinancialDetail.jsp");
			// rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("uniquepnnogeneration")) {	
          RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
			rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("uniquepnnosearch")) {	
	          RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/UniquePensionNumberSearch.jsp");
				rd.forward(request, response);
		}else if (request.getParameter("method") != null
					&& request.getParameter("method").equals("financedatamapping")) {	
		          RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/FinanceDataMapping.jsp");
					rd.forward(request, response);
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("verifiedpfidlist")) {	
	          RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/VerifiedPFIDList.jsp");
				rd.forward(request, response);
	}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchRecordsbyDobandNameUnprocessList")) {
			log.info("searchRecordsbyDobandNameUnprocessList () ");
			String employeeName = "";
			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("empName") != null) {
				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			} else {
				empBean.setEmpName("");
			}

			log.info(employeeName);
			if (request.getParameter("dateofbirth") != null) {
				empBean.setDateofBirth(request.getParameter("dateofbirth")
						.toString().trim());
			} else
				empBean.setDateofBirth("");
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;

			try {
				searchBean = ps.searchRecordsbyDobandName(empBean,
						getSearchInfo, gridLength);
				ArrayList dataList = new ArrayList();
				dataList = searchBean.getSearchList();
				log.info("Search list " + searchBean);
				StringBuffer sb = new StringBuffer();
				sb.append("<ServiceTypes>");
				for (int i = 0; dataList != null && i < dataList.size(); i++) {
					String name = "", code = "";
					PensionBean bean = (PensionBean) dataList.get(i);
					sb.append("<ServiceType>");
					sb.append("<cpfacno>");
					sb.append(bean.getCpfAcNo());
					sb.append("</cpfacno>");
					sb.append("<employeename>");
					sb.append(bean.getEmployeeName().trim());
					sb.append("</employeename>");
					sb.append("<dateofbirth>");
					if (bean.getDateofBirth().equals("")) {
						sb.append(" ");
					} else {
						sb.append(bean.getDateofBirth());
					}

					sb.append("</dateofbirth>");
					sb.append("<dateofjoin>");
					if (bean.getDateofJoining().equals("")) {
						sb.append(" ");
					} else {
						sb.append(bean.getDateofJoining());
					}

					sb.append("</dateofjoin>");
					sb.append("<region>");
					sb.append(bean.getRegion());
					sb.append("</region>");
					sb.append("<airport>");
					sb.append(bean.getAirportCode());
					sb.append("</airport>");
					sb.append("<employeeNo>");
					sb.append(bean.getEmployeeCode());
					sb.append("</employeeNo>");

					sb.append("<pensionOption>");
					sb.append(bean.getPensionOption());
					sb.append("</pensionOption>");
					sb.append("</ServiceType>");
				}
				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());

				//	ps.updatePensionCheck(empBean,getSearchInfo,gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getEmployeeList")) {
			log.info("getEmployeeList () ");
			String employeeName = "";

			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("empName") != null) {
				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			} else {
				empBean.setEmpName("");
			}

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;

			try {
				searchBean = ps.getEmployeeList(empBean, getSearchInfo,
						gridLength, "ajaxCall");
				ArrayList dataList = new ArrayList();
				dataList = searchBean.getSearchList();
				StringBuffer sb = new StringBuffer();

				sb.append("<ServiceTypes>");
				for (int i = 0; dataList != null && i < dataList.size(); i++) {
					PensionBean bean = (PensionBean) dataList.get(i);
					sb.append("<ServiceType>");
					sb.append("<employeename>");
					sb.append(bean.getEmployeeName().trim());
					sb.append("</employeename>");

					sb.append("</ServiceType>");
				}
				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());

				//	ps.updatePensionCheck(empBean,getSearchInfo,gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getPFID")) {
			log.info("getEmployeeList () ");

			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
			} else {
				empBean.setRegion("");
			}
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAcNo(request.getParameter("cpfaccno").toString()
						.trim());
			} else {
				empBean.setCpfAcNo("");
			}
			try {
				String pfId = "";
				//ps.getPfID(empBean);

				StringBuffer sb = new StringBuffer();

				sb.append("<ServiceTypes>");

				String name = "", code = "";
				sb.append("<ServiceType>");
				sb.append("<pfid>");
				sb.append(pfId.trim());
				sb.append("</pfid>");

				sb.append("</ServiceType>");

				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());

				//	ps.updatePensionCheck(empBean,getSearchInfo,gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getEmployeeMappedList")) {
			log.info("getEmployeeMappedList () ");
			String path="",screen="";
			String uanNo="";
			String region = request.getParameter("region").toString();
			if (region.equals("[Select One]")) {
				region = "";
			}
			String empName = request.getParameter("empName").toString().trim();
			String pfId = request.getParameter("pfId").toString().trim();
			if(request.getParameter("uanNo")!=null){
			uanNo = request.getParameter("uanNo").toString().trim();
			}
			if(request.getParameter("screen")!=null){
				screen=request.getParameter("screen").toString().trim();
				}
			pfId = commonUtil.getSearchPFID1(pfId.toString().trim());
			String transferType = "",adjPfidChkFlag="", chkpfid="", statemntRevisdChkFlag="",statemntRevisdChkPfid="",frmName="",PCAftrSepFlag="",pcReportChkFlag="";
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
				mappedList = ps.getEmployeeMappedList(region, empName.trim(),
						transferType, pfId,uanNo);
				PensionBean  empData =  null;
				for(int i=0;i<mappedList.size();i++){
				   empData = (PensionBean)mappedList.get(i);
				}
				if(screen.equals("pcreport")){
					employeeinfolist=ps.getEmployeeinfoMappedList(pfId);
					
						request.setAttribute("EmpInfoList", employeeinfolist);
						System.out.println("employeeinfolist----"+employeeinfolist.size());
					
					
				}
				log.info("adjPfidChkFlag----"+adjPfidChkFlag);
				log.info("screen----"+screen);
				if(adjPfidChkFlag.equals("true")){
					if(pcReportChkFlag.equals("true")){
						if(screen.equals("pcreport")){
						chkpfid = adjCrtnService.chkPfidStatusInAdjCrtnForPCReport1(pfId);
						}
						else{
						//For Checking exact Corrections in 1995-2008 period
						chkpfid = adjCrtnService.chkPfidStatusInAdjCrtnForPCReport(pfId);
						}
					}else{					
						chkpfid = adjCrtnService.chkPfidStatusInAdjCrtn(pfId);
					}
				} 
				
				if(chkpfid.equals("Approved")){
					chkpfid="Exists";
				}else{
					chkpfid="NotExists";	
				}
				//For 12 Mnth Statement Chking purpose
				log.info("statemntRevisdChkFlag----"+statemntRevisdChkFlag);
				if(statemntRevisdChkFlag.equals("true")){
					statemntRevisdChkPfid = adjCrtnService.chkPfidinAdjCrtn78PSTracking(pfId,"");
					} 
				
				if(frmName.equals("form7ps")||frmName.equals("form8ps")){
					PCAftrSepFlag = finReportService.chkPfidHavngPCAfterSeperation(pfId,empData);
					} 
				request.setAttribute("MappedList", mappedList);
				
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
				if(request.getParameter("pagename")!=null){
					if(request.getParameter("pagename").equals("processInfo")){
						path="./PensionView/dashboard/ProcessInfo.jsp";
					}
				}else{
					path="./PensionView/PensionContributionMappedList.jsp";
				}
				RequestDispatcher rd = request
						.getRequestDispatcher(path);
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getDesegnation")) {
			String empLevel = request.getParameter("empLevel");
			PensionService ps1 = new PensionService();
			EmpMasterBean ServiceType = null;
			ServiceType = ps1.getDesegnation(empLevel.toUpperCase().trim());
			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");
			String designation = "";
			sb.append("<ServiceType>");
			sb.append("<designation>");
			if (ServiceType.getDesegnation() != null)
				designation = ServiceType.getDesegnation().replaceAll("<",
						"&lt;").replaceAll(">", "&gt;");
			sb.append(designation);
			sb.append("</designation>");
			sb.append("</ServiceType>");

			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getCpfacnoDetails")) {
			cpfacno = request.getParameter("cpfacno");
			String pensionnumber = (String) request
					.getParameter("pensionnumber");
			String region = (String) request.getParameter("region");
			PensionService ps1 = new PensionService();
			EmpMasterBean ServiceType = null;
			ServiceType = ps1.getCpfacnoDetails(pensionnumber.toUpperCase(),
					cpfacno.toUpperCase(), region);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");

			String employeeName = "";
			sb.append("<ServiceType>");
			sb.append("<employeeName>");
			if (ServiceType.getEmpName() != null)
				employeeName = ServiceType.getEmpName().replaceAll("<", "&lt;")
						.replaceAll(">", "&gt;");
			sb.append(employeeName);
			sb.append("</employeeName>");
			sb.append("<pensionNumber>");
			sb.append(ServiceType.getPensionNumber());
			sb.append("</pensionNumber>");
			sb.append("<cpfacno>");
			sb.append(ServiceType.getCpfAcNo());
			sb.append("</cpfacno>");
			sb.append("<employeeNumber>");
			sb.append(ServiceType.getEmpNumber());
			sb.append("</employeeNumber>");
			sb.append("<region>");
			sb.append(ServiceType.getRegion());
			sb.append("</region>");
			sb.append("<airportcode>");
			sb.append(ServiceType.getStation());
			sb.append("</airportcode>");
			sb.append("</ServiceType>");

			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

			// request.setAttribute("cpfnoDeatails", bean);
			// RequestDispatcher rd =
			// request.getRequestDispatcher("./PensionView/AddFinancialDetail.jsp");
			// rd.forward(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("searchPensionMaster")) {
			log.info("SearchPensionMasterServlet : dopost() inside if");
			String airportCode = "", employeeName = "", desegnation = "";
			EmpMasterBean empBean = new EmpMasterBean();

			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("false")) {

			}
			if (request.getParameter("airPortCode") != null)

				empBean.setStation(request.getParameter("airPortCode")
						.toString().trim());

			if (request.getParameter("empName") != null)
				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			if (request.getParameter("desegnation") != null)
				empBean.setDesegnation(request.getParameter("desegnation")
						.toString().trim());
			if (request.getParameter("cpfaccno") != null) {

				empBean.setCpfAcNo(request.getParameter("cpfaccno").toString()
						.trim().toUpperCase());
			} else {
				empBean.setCpfAcNo("");
			}
			if (request.getParameter("employeeCode") != null) {
				empBean.setEmpNumber(request.getParameter("employeeCode")
						.toString());
			} else
				empBean.setEmpNumber("");

			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
			} else
				empBean.setRegion("");
			log.info("region is " + empBean.getRegion());

			if (request.getParameter("recordVerified") != null) {
				empBean.setRecordVerified(request
						.getParameter("recordVerified").toString().trim());
			} else
				empBean.setRecordVerified("N");

			// code added for empname blanks search
			String empNameCheak = "";
			if (request.getParameter("empNameCheak").equals("true")) {
				empNameCheak = request.getParameter("empNameCheak").toString();
			} else
				empNameCheak = "";
			empBean.setEmpNameCheak(empNameCheak);

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {
				searchBean = ps.searchPensionMaster(empBean, getSearchInfo,
						flag, gridLength, empNameCheak);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", searchBean);

			request.setAttribute("searchInfo", empBean);

			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("false")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearch.jsp");
				rd.forward(request, response);
			}

		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("searchPensionMaster1")) {
			log.info("SearchPensionMasterServlet : dopost() inside if");
			String airportCode = "", employeeName = "", desegnation = "";
			EmpMasterBean empBean = new EmpMasterBean();
			boolean flag = true;
			if (request.getAttribute("flag") != null
					&& request.getAttribute("flag").equals("false")) {
				flag = false;
			}
			empBean.setStation(airportCode);
			empBean.setEmpName(employeeName);
			empBean.setDesegnation(desegnation);
			empBean.setCpfAcNo("");
			PensionDAO pensionDAO = new PensionDAO();
			int totalRecords = pensionDAO.totalPensionMasterData(empBean, flag,
					"");

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			try {
				searchBean = ps.searchPensionMaster(empBean, getSearchInfo,
						flag, gridLength, "");
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			searchBean.setTotalRecords(totalRecords);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);
			request.setAttribute("searchInfo1", empBean);

			RequestDispatcher rd = null;
			if (flag == true) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearch.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterInvalidRecords.jsp");
			}

			rd.forward(request, response);
		}

		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("navigation")) {
			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			EmpMasterBean dbBeans = new EmpMasterBean();

			boolean flag = false;
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = (String) session.getAttribute("navButton");
				searchListBean.setNavButton(navgationBtn);
			}
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;
			} else {
				flag = true;
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
				dbBeans = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}
			try {
				navSearchBean = ps.searchNavigationPensionData(dbBeans,
						searchListBean, flag, gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = null;

			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("compare")) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
				rd.forward(request, response);

			} else if (flag == true) {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearch.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterInvalidRecords.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"uniquePensionNavigation")) {

			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			String arrearInfo = "";
			SearchInfo searchListBean = new SearchInfo();
			EmpMasterBean dbBeans = new EmpMasterBean();

			if (request.getParameter("arrearInfo") != null) {
				arrearInfo = request.getParameter("arrearInfo").toString();
			} else {
				arrearInfo = "";
			}

			boolean flag = false;
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
				session.setAttribute("navButton", navgationBtn);
			} else {
				navgationBtn = (String) session.getAttribute("navButton");
				searchListBean.setNavButton(navgationBtn);
			}
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("invflag")) {
				flag = false;
			} else {
				flag = true;
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
			if (session.getAttribute("getSearchBean1") != null) {
				dbBeans = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}
			try {
				navSearchBean = ps.searchRecordsbyDobandName(dbBeans,
						searchListBean, gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			request.setAttribute("ArrearInfo", arrearInfo);
			RequestDispatcher rd = null;

			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("compare")) {
				rd = request
						.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
				rd.forward(request, response);

			} else if (flag == true) {
				rd = request
						.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
			} else {
				rd = request
						.getRequestDispatcher("./PensionView/PensionMasterInvalidRecords.jsp");
			}
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("update")) {
			log.info("SearchPensionMasterServlet : dopost() ");
			EmpMasterBean bean = new EmpMasterBean();
			String flag = "", empName = "";
			String startIndex = "", totalData = "";
			String editFrom = "";
			String arrearInfo = "";
			//  log.info("arrear info"+request.getAttribute("ArrearInfo").toString());
			if (request.getParameter("ArrearInfo") != null) {
				arrearInfo = request.getParameter("ArrearInfo").toString();
			} else {
				arrearInfo = "";
			}
			log.info("arrear info" + arrearInfo.toString());
			request.setAttribute("ArrearInfo", arrearInfo);
			startIndex = request.getParameter("startIndex");
			totalData = request.getParameter("totalData");
			String airportWithRegion = request
					.getParameter("airportWithRegion");
			String region1 = airportWithRegion.substring(airportWithRegion
					.indexOf("-") + 1, airportWithRegion.length());
			bean.setNewRegion(region1.trim());
			if (request.getParameter("editFrom") != null) {
				editFrom = request.getParameter("editFrom");
			}
			if (request.getParameter("flagData") != null) {
				flag = request.getParameter("flagData");
			} else {
				flag = "true";
			}
			if (request.getParameter("pensionNO") != null) {
				bean.setPensionNumber(commonUtil.getSearchPFID(request
						.getParameter("pensionNO").toString().trim()));
			} else {
				bean.setPensionNumber("");
			}
			log.info("pensionno **** " + bean.getPensionNumber());
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

			if (request.getParameter("wetherOptionA") != null) {
				bean.setWhetherOptionA(request.getParameter("wetherOptionA")
						.toString());
			} else {
				bean.setWhetherOptionA("");
			}

			if (request.getParameter("wetherOptionNo") != null) {
				bean.setWhetherOptionNO(request.getParameter("wetherOptionNo")
						.toString());
			} else {
				bean.setWhetherOptionNO("");
			}
			if (request.getParameter("wetherOptionB") != null) {
				bean.setWhetherOptionB(request.getParameter("wetherOptionB")
						.toString());
			} else {
				bean.setWhetherOptionB("");
			}

			if (request.getParameter("emailId") != null) {
				bean.setEmailId(request.getParameter("emailId").toString());
			} else {
				bean.setEmailId("");
			}

			if (request.getParameter("recordVerified") != null) {
				bean.setRecordVerified(request.getParameter("recordVerified")
						.toString());
			} else {
				bean.setRecordVerified("");
			}

			if (request.getParameter("equalShare") != null) {
				bean.setEmpNomineeSharable(request.getParameter("equalShare")
						.toString().trim());
			} else
				bean.setEmpNomineeSharable("");

			if (request.getParameter("form1") != null) {
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

			if (request.getParameter("region") != null) {
				bean
						.setRegion(request.getParameter("region".trim()
								.toString()));
			} else {
				bean.setRegion("");
			}
			log.info("region is" + bean.getRegion());

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
			if (request.getParameter("pensionNumber") != null) {
				bean.setPensionNumber(request.getParameter("pensionNumber")
						.toString().trim());
				log.info("pension number is " + bean.getPensionNumber());
			} else {
				bean.setPensionNumber("");
			}

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
				String nAddress = "", nDob = "", nRelation = "", nGuardianname = "", nTotalshare = "", gardaddressofNaminee = "";
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
						nomineeRow.append(nAddress + "@");
						nomineeRow.append(nDob + "@");
						nomineeRow.append(nRelation + "@");
						nomineeRow.append(nGuardianname + "@");
						nomineeRow.append(gardaddressofNaminee + "@");
						nomineeRow.append(empOldNomineeName + "@");
						nomineeRow.append(nTotalshare);
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
			log.info("Befor called updatePensionMaster");
			try {
				count = ps.updatePensionMaster(bean, flag);
			} catch (InvalidDataException e) {
				log.printStackTrace(e);
				// TODO Auto-generated catch block
				message = e.getMessage();

			}
			cpfacno = bean.getCpfAcNo();
			log.info("Befor called updatePensionMaster" + count);

			RequestDispatcher rd = null;
			log.info("=======cpfacno" + cpfacno + "flag==" + flag + "name="
					+ empName + "count==========" + count);
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
				log.info("uniqueNumberGen ====insideuniqueNumberGen "
						+ arrearInfo);
				rd = request
						.getRequestDispatcher("./search1?method=searchRecordsbyDobandName");
			} else if (flag.equals("true")
					&& editFrom.equals("getProcessUnprocessList")) {
				log.info("getProcessUnprocessList ====insideuniqueNumberGen "
						+ arrearInfo);
				String empSerailNumber1 = "", dateofbirth = "";
				if (session.getAttribute("dateofbirth1") != null) {
					dateofbirth = (String) session.getAttribute("dateofbirth1");
				}
				if (session.getAttribute("empSerailNumber1") != null) {
					empSerailNumber1 = (String) session
							.getAttribute("empSerailNumber1");
					empSerailNumber1 = commonUtil
							.getSearchPFID(empSerailNumber1.toString().trim());
				}

				log.info("dateofbirth" + dateofbirth + " empSerailNumber1"
						+ empSerailNumber1);
				//   rd = request.getRequestDispatcher("./search1?method=searchRecordsbyDobandName");
				rd = request
						.getRequestDispatcher("./search1?method=getProcessUnprocessList&cpfacno=&name=K.R.RANGAIAH&region=&airportCode=&empSerailNumber="
								+ empSerailNumber1
								+ "&dateofBirth="
								+ dateofbirth);

			} else {
				log.info("else ====totalData " + totalData);
				rd = request
						.getRequestDispatcher("./search1?method=navigation&strtindx="
								+ startIndex + "&total" + totalData);
			}

			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirportsList")) {
			String region = "";
			ArrayList airportList = new ArrayList();
			if (request.getParameter("region") != null) {
				region = request.getParameter("region").toString().trim();
			}
			if (request.getParameter("region") == null) {
				region = session.getAttribute("region").toString().trim();
			}
			try {
				airportList = ps.getAirportsList(region);
				log.info("airportlist " + airportList.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			EmpMasterBean empBean = new EmpMasterBean();
			SearchInfo searchBean = new SearchInfo();

			empBean.setRegion(region);
			request.setAttribute("airportList", airportList);
			request.setAttribute("searchInfo", empBean);
			// request.setAttribute("searchBean", empBean);
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterSearch.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchCompartiveMaster")) {
			log.info("SearchPensionMasterServlet : dopost() inside if");
			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("region") != null) {
				empBean.setRegion(request.getParameter("region").toString()
						.trim());
			} else
				empBean.setRegion("");
			log.info("region is " + empBean.getRegion());

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			searchBean = ps.searchCompartiveMaster(empBean, getSearchInfo,
					gridLength);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);

			ArrayList region = new ArrayList();

			try {
				region = ps.getRegions();
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("regionslist", region);

			log.info("REGION LIST IN SERVLET.............." + region.size());
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("compartivenavigation")) {
			log.info("inside else");
			String navgationBtn = "";
			SearchInfo navSearchBean = new SearchInfo();
			int strtIndex = 0, totalRecords = 0;
			SearchInfo searchListBean = new SearchInfo();
			EmpMasterBean dbBeans = new EmpMasterBean();
			boolean flag = false;
			if (request.getParameter("navButton") != null) {
				navgationBtn = request.getParameter("navButton").toString();
				searchListBean.setNavButton(navgationBtn);
			}
			if (request.getParameter("region") != null) {
				dbBeans.setRegion(request.getParameter("region").toString()
						.trim());
			} else
				dbBeans.setRegion("");

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
			if (session.getAttribute("getSearchBean1") != null) {
				dbBeans = (EmpMasterBean) session
						.getAttribute("getSearchBean1");
			}
			navSearchBean = ps.searchNavigationCompartiveData(dbBeans,
					searchListBean, gridLength);
			request.setAttribute("searchBean", navSearchBean);
			request.setAttribute("searchInfo", dbBeans);
			RequestDispatcher rd = null;
			rd = request
					.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchRecordsbyDobandName")) {
			log.info("searchRecordsbyDobandName () ");
			String employeeName = "", arrearInfo = "";

			boolean getSessionflag = false;
			EmpMasterBean empBean = new EmpMasterBean();
			if (request.getParameter("frm_empnm") != null) {
				employeeName = request.getParameter("frm_empnm").toString()
						.trim();
				empBean.setEmpName(employeeName);
			} else {
				getSessionflag = true;
			}
			log.info(employeeName);
			if (request.getParameter("dob") != null) {
				empBean.setDateofBirth(request.getParameter("dob").toString()
						.trim());
			} else
				empBean.setDateofBirth("");
			if (request.getParameter("employeeNo") != null) {
				empBean.setEmpNumber(request.getParameter("employeeNo")
						.toString().trim());
			} else
				empBean.setEmpNumber("");
			if (request.getParameter("cpfaccno") != null) {
				empBean.setCpfAcNo(request.getParameter("cpfaccno").toString()
						.trim().toUpperCase());
			} else {
				empBean.setCpfAcNo("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateofJoining((String) request.getParameter("doj")
						.toString());
			} else
				empBean.setDateofJoining("");

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			log.info("searchRecordsbyDobandName======"
					+ session.getAttribute("empBean1"));
			if (session.getAttribute("empBean1") != null
					&& getSessionflag == true) {
				empBean = (EmpMasterBean) session.getAttribute("empBean1");
				session.setAttribute("empBean1", null);
				session.removeAttribute("empBean1");

			}
			if (request.getAttribute("ArrearInfo") != null) {
				arrearInfo = request.getAttribute("ArrearInfo").toString();
			}

			else if (request.getParameter("arrearInfo") != null
					&& !request.getParameter("arrearInfo").toString()
							.equals("")) {
				arrearInfo = request.getParameter("arrearInfo").toString();
				request.setAttribute("ArrearInfo", arrearInfo);
			} else {
				arrearInfo = "";
			}

			boolean flag = true;
			try {
				searchBean = ps.searchRecordsbyDobandName(empBean,
						getSearchInfo, 200);

				//	ps.updatePensionCheck(empBean,getSearchInfo,gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			session.setAttribute("empBean1", empBean);
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("false")) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
				rd.forward(request, response);

			} else {

				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/UniquePensionNumberGeneration.jsp");
				rd.forward(request, response);
			}

		}
		/*	else if (request.getParameter("method") != null
		 && request.getParameter("method").equals(
		 "pensionHistory")) {
		 log.info("pensionHistory () ");
		 String employeeName = "";
		 EmpMasterBean empBean = new EmpMasterBean();
		 if (request.getParameter("empName") != null)
		 employeeName = request.getParameter("empName").toString()
		 .trim();
		 empBean.setEmpName(employeeName);
		 log.info(employeeName);
		 if (request.getParameter("dob") != null) {
		 empBean.setDateofBirth(request.getParameter("dob").toString()
		 .trim());
		 } else
		 empBean.setDateofBirth("");
		 if (request.getParameter("empsrlNo") != null) {
		 empBean.setEmpSerialNo(request.getParameter("empsrlNo").toString()
		 .trim().toUpperCase());
		 } else {
		 empBean.setEmpSerialNo("");
		 }
		 if (request.getParameter("doj") != null) {
		 empBean.setDateofJoining((String) request.getParameter("doj")
		 .toString());
		 } else
		 empBean.setDateofJoining("");

		 SearchInfo getSearchInfo = new SearchInfo();
		 SearchInfo searchBeanList = new SearchInfo();
		 boolean flag = true;
		 try {
		 searchBeanList = ps.pensionHistory(empBean,
		 getSearchInfo, gridLength);
		 log.info("getting list" +searchBeanList.getSearchList().size());
		 //ps.updatePensionCheck(empBean,getSearchInfo,gridLength);
		 } catch (Exception e) {
		 log.printStackTrace(e);
		 }
		 request.setAttribute("searchBean", searchBeanList);
		 request.setAttribute("searchInfo", empBean);
		 RequestDispatcher rd = request
		 .getRequestDispatcher("./PensionView/PensionHistory.jsp");
		 rd.forward(request, response);
		 

		 } */else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"searchRecordsbyEmpSerailNo")) {
			log.info("searchRecordsbyEmpSerailNo () ");
			String employeeName = "", frmName = "", path = "";
			String cpfAccnos = "";
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList empPCAdjDiffTot = new ArrayList();
			//new code for status disply
			if (request.getParameter("cpfnumbers") != null) {
				cpfAccnos = request.getParameter("cpfnumbers");
				request.setAttribute("cpfAccnos", cpfAccnos);

			}
			if (request.getParameter("empName") != null)

				empBean.setEmpName(request.getParameter("empName").toString()
						.trim());
			log.info(employeeName);
			if (request.getParameter("dob") != null) {
				empBean.setDateofBirth(request.getParameter("dob").toString()
						.trim());
			} else
				empBean.setDateofBirth("");
			if (request.getParameter("empsrlNo") != null) {
				empBean.setEmpSerialNo(request.getParameter("empsrlNo")
						.toString().trim().toUpperCase());
			} else {
				empBean.setEmpSerialNo("");
			}
			if (request.getParameter("doj") != null) {
				empBean.setDateofJoining((String) request.getParameter("doj")
						.toString());
			} else
				empBean.setDateofJoining("");

			 
			log.info("cpfAccnos--------- " + cpfAccnos + "===--------"
					+ empBean.getEmpSerialNo());

			 	path = "./PensionView/UniquePensionNumberSearch.jsp";
			 
			String userType = session.getAttribute("usertype").toString();
			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {
				searchBean = ps.searchRecordsbyEmpSerailNo(empBean,
						getSearchInfo, gridLength, userType);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals(
						"getProcessUnprocessList")) {
			log.info("getProcessUnprocessList ");
			String employeeName = "";
			EmpMasterBean empBean = new EmpMasterBean();

			if (request.getParameter("cpfacno") != null) {
				empBean.setCpfAcNo(request.getParameter("cpfacno").toString()
						.trim());
			} else
				empBean.setCpfAcNo("");
			if (request.getParameter("name") != null)
				employeeName = request.getParameter("name").toString().trim();
			empBean.setEmpName(employeeName);
			log.info(employeeName);
			if (request.getParameter("dateofBirth") != null) {
				empBean.setDateofBirth(request.getParameter("dateofBirth")
						.toString().trim());
			} else
				empBean.setDateofBirth("");
			if (request.getParameter("empSerailNumber") != null) {
				empBean.setEmpSerialNo(request.getParameter("empSerailNumber")
						.toString().trim().toUpperCase());
			} else {
				empBean.setEmpSerialNo("");
			}
			empBean.setEmpSerialNo(commonUtil.getSearchPFID(empBean
					.getEmpSerialNo().toString().trim()));
			if (request.getParameter("empCode") != null) {
				empBean.setEmpNumber(request.getParameter("empCode").toString()
						.trim());
			} else
				empBean.setEmpNumber("");
			log.info("dateofbirth " + empBean.getDateofBirth() + " empSrlno "
					+ empBean.getEmpSerialNo());

			SearchInfo getSearchInfo = new SearchInfo();
			SearchInfo searchBean = new SearchInfo();
			boolean flag = true;
			try {
				searchBean = ps.getProcessUnprocessList(empBean, getSearchInfo,
						gridLength);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			request.setAttribute("searchBean", searchBean);
			request.setAttribute("searchInfo", empBean);

			// session.setAttribute("searchBean", searchBean);
			//  session.setAttribute("searchInfo", empBean);

			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/UniquePensionNumberProcessList.jsp");
			rd.forward(request, response);

		} else if (request.getParameter("method") != null
				&& request.getParameter("method")
						.equals("comparativestatement")) {
			log.info("SearchPensionMasterServlet : dopost() inside if");
			EmpMasterBean empBean = new EmpMasterBean();
			ArrayList region = new ArrayList();
			try {
				region = ps.getRegions();
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("regionslist", region);
			log
					.info("********region list size****************"
							+ region.size());
			RequestDispatcher rd = request
					.getRequestDispatcher("./PensionView/PensionMasterSearchComparativeSt.jsp");
			rd.forward(request, response);
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("deleteFamilyDetails")) {
			String familyMemberName = "", rowid = "", region = "", cpfaccno = "";
			if (request.getParameter("fmemberName") != null) {
				familyMemberName = request.getParameter("fmemberName")
						.toString();
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno").toString();
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region").toString();
			}
			if (request.getParameter("rowid") != null) {
				rowid = request.getParameter("rowid").toString();
			}
			ps.deleteFamilyDetails(familyMemberName, rowid, region, cpfaccno);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");

			String employeeName = "";
			sb.append("<ServiceType>");
			sb.append("<deletestatus>");
			sb.append("Deleted Successfully");
			sb.append("</deletestatus>");
			sb.append("<fmemberName>");
			sb.append(familyMemberName.toUpperCase());
			sb.append("</fmemberName>");
			sb.append("</ServiceType>");
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());

		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("deleteNomineeDetils")) {
			String nomineeName = "", rowid = "", region = "", cpfaccno = "";
			if (request.getParameter("nomineeName") != null) {
				nomineeName = request.getParameter("nomineeName").toString();
			}
			if (request.getParameter("cpfaccno") != null) {
				cpfaccno = request.getParameter("cpfaccno").toString();
			}
			if (request.getParameter("region") != null) {
				region = request.getParameter("region").toString();
			}
			if (request.getParameter("rowid") != null) {
				rowid = request.getParameter("rowid").toString();
			}
			ps.deleteNomineeDetils(nomineeName, rowid, region, cpfaccno);

			StringBuffer sb = new StringBuffer();
			sb.append("<ServiceTypes>");

			sb.append("<ServiceType>");
			sb.append("<deletestatus>");
			sb.append("Deleted Successfully");
			sb.append("</deletestatus>");
			sb.append("<nomineeName>");
			sb.append(nomineeName.toUpperCase());
			sb.append("</nomineeName>");
			sb.append("</ServiceType>");
			sb.append("</ServiceTypes>");
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			out.write(sb.toString());
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("viewExcel")) {
			String fileurl="",url="",filename="",flag="",type="",calculsheetfolder,form2sheetfolder;
			ResourceBundle bundle= ResourceBundle.getBundle("aims.resource.ApplicationResouces");
			calculsheetfolder=bundle.getString("upload.folder.path.epf.cal");
			form2sheetfolder=bundle.getString("upload.folder.path.epf.form2");
			if(request.getParameter("url")!=null){
				fileurl=request.getParameter("url");
			}
			if(request.getParameter("flag")!=null){
				flag=request.getParameter("flag");
			}
			if(request.getParameter("type")!=null){
				type=request.getParameter("type");
			}
			
			if(flag.equals("Y")){
				String findout=fileurl.substring(0,fileurl.indexOf("_"));
				log.info("findout======================"+findout);
				if(findout.equals("AAIEPF-2")){
					url=form2sheetfolder+bundle.getString("upload.folder.path.slashsuffix")+fileurl;
				}else{
					url=calculsheetfolder+bundle.getString("upload.folder.path.slashsuffix")+fileurl;
				}
				filename=fileurl;
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
				"attachment;filename="+filename);
				File file = new File(url);
				FileInputStream fileIn = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();
				 
				byte[] outputByte = new byte[4096];
//				copy binary contect to output stream
				while(fileIn.read(outputByte, 0, 4096) != -1)
				{
					out.write(outputByte, 0, 4096);
				}
				fileIn.close();
				out.flush();
				out.close();
			}else{
				url=fileurl;
				RequestDispatcher rd = request
				.getRequestDispatcher(url);
		rd.forward(request, response);
				
			}
			
			
		} else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getEmolumentslog")) {
			log.info("getEmolumentslog()");
			String path ="",frmName="",dispYear="";
			String pfId = request.getParameter("pfId").toString().trim();
			String airportcode = request.getParameter("airportcode").toString()
					.trim();
			
			if(request.getParameter("dispYear")!=null){
				dispYear = request.getParameter("dispYear");
			}else{
				dispYear="";
			}
			if(request.getParameter("frmName")!=null){
				frmName = request.getParameter("frmName");
			}else{
				frmName="";
			}
			
			log.info("pensionno" + pfId + "airportcode" + airportcode
					+ "dispYear" + dispYear);
			ArrayList adjLoginfo = new ArrayList();
			pfId = commonUtil.getSearchPFID1(pfId.toString().trim());
			ArrayList emolumentsloginfo = new ArrayList();
			ArrayList deleteloginfo = new ArrayList();
			ArrayList freezAdjOB = new ArrayList();
			try {
				
				adjLoginfo = ps.getAdjlog(pfId);
				freezAdjOB = ps.getAdjfreezAdjOB(pfId, dispYear);
				request.setAttribute("adjLoginfo", adjLoginfo);			
				request.setAttribute("freezAdjOB", freezAdjOB);
				
				
				if(frmName.equals("adjcorrections")){
					path = "./PensionView/AdjCrtn/AdjEmolumentslog.jsp";
				}else{
					emolumentsloginfo = ps.getEmolumentslog(pfId, airportcode);
					deleteloginfo = ps.getDeletelog(pfId, airportcode);
					request.setAttribute("deleteloginfo", deleteloginfo);
					request.setAttribute("emolumentsloginfo", emolumentsloginfo);
					log.info("Mapped LIst " + emolumentsloginfo.size());
					path = "./PensionView/Emolumentslog.jsp";
				}
				RequestDispatcher rd = request
						.getRequestDispatcher(path);
				rd.forward(request, response);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("uploadToForm2")) {
			log.info("uploadToForm2()");
			PensionContBean PersonalInfo = new PensionContBean();
			EmployeePersonalInfo emppersonalInfo=new EmployeePersonalInfo();
			String reportType = "ExcelSheet",formtype="";
			if(request.getParameter("formtype")!=null){
				formtype=request.getParameter("formtype");
			}else{
				formtype="NA";
			}
			if(!formtype.equals("NA")){
				if(session.getAttribute("PersonalInfo")!=null){
					emppersonalInfo = (EmployeePersonalInfo)session.getAttribute("PersonalInfo");
					copyToBean(emppersonalInfo,PersonalInfo);
				}  
			}else{
				if(session.getAttribute("PersonalInfo")!=null){
					PersonalInfo = (PensionContBean)session.getAttribute("PersonalInfo");
				}  
			}
			
			log.info("-----PersonalInfo------"+PersonalInfo.getEmpSerialNo());
			
			request.setAttribute("PersonalInfo",PersonalInfo);
			request.setAttribute("reportType",reportType);
			 
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionView/AdjCrtn/AdjtsForForm2.jsp");
				rd.forward(request, response);
			 
		}else if(request.getParameter("method") != null
				&& request.getParameter("method")
				.equals("pfcardSplRemarks")) {
			
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/personal/LoadPfcardSpecialRemarksParam.jsp");
			rd.forward(request, response);
			
		}else if (request.getParameter("method").equals("remarksInfo")) {
			String empSerialnumber = "",username="",remarks="",finyear="",doer="";
			int result=0;
			String message="";
		
		 String ipAddress=(String)session.getAttribute("computername");

		if (request.getParameter("empSerialnumber") != null) {
			empSerialnumber = request.getParameter("empSerialnumber");
		}
		log.info("pfid"+empSerialnumber);
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		log.info("clientname"+username);
		if (request.getParameter("remarks") != null) {
			remarks = request.getParameter("remarks");
		}
		log.info("remarks"+remarks);
		if (request.getParameter("finyear") != null) {
			finyear = request.getParameter("finyear");
		}
		log.info("finyear"+finyear);
		if (request.getParameter("doer") != null) {
			doer = request.getParameter("doer");
		}
		log.info("doer"+doer);
		
		
		
		empSerialnumber = commonUtil.trailingZeros(empSerialnumber.toCharArray());
		String userId = session.getAttribute("userid").toString();
		//result=finReportService.remarksProcessInfo(empSerialnumber,username, remarks,finyear,doer,ipAddress,userId);
		if(result==0){
			message="Remarks Not Updated Successfully";
		}else{
			message="Remarks Updated Successfully";
		}
		
		request.setAttribute("msg", message);
		 RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/personal/LoadPfcardSpecialRemarksParam.jsp");
			rd.forward(request, response);
			log.info("reportservlet:editPensionInfo:---leaving method");
		}

	}
	public PensionContBean copyToBean(EmployeePersonalInfo emppersonalInfo,PensionContBean pBean){
		
		pBean.setEmpSerialNo(emppersonalInfo.getPensionNo());
		pBean.setEmpCpfaccno(emppersonalInfo.getEmployeeNumber());
		pBean.setEmployeeNO(emppersonalInfo.getCpfAccno());
		pBean.setEmployeeNM(emppersonalInfo.getEmployeeName());
		pBean.setDesignation(emppersonalInfo.getDesignation());
		pBean.setEmpDOB(emppersonalInfo.getDateOfBirth());
		pBean.setEmpDOJ(emppersonalInfo.getDateOfJoining());
		pBean.setFhName(emppersonalInfo.getFhName());
		return pBean;
		
	}
}
