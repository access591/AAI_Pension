/**
 * File       : PensionValidationServlet.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */

package aims.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.EmpMasterBean;
import aims.bean.EmployeeValidateInfo;
import aims.bean.SearchInfo;
import aims.bean.form3Bean;
import aims.common.CommonUtil;
import aims.common.InvalidDataException;
import aims.common.Log;
import aims.dao.FinancialDAO;
import aims.service.FinancialReportService;
import aims.service.FinancialService;
import aims.service.PensionService;

/* 
##########################################
#Date					Developed by			Issue description
#17-Dec-2011        	Prasanthi			     Method added for navayuga importing(loadimportedprocessnavayuga)
#########################################
*/
public class PensionValidationServlet extends HttpServlet{
	Log log = new Log(PensionValidationServlet.class);
	FinancialService financeService=new FinancialService();
	PensionService ps = new PensionService();
	FinancialDAO financialDAO=new FinancialDAO();
	CommonUtil commonUtil = new CommonUtil();
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException{
		HttpSession session = request.getSession();
		int gridLength=Integer.parseInt((String)session.getAttribute("gridlength"));
		if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getMaxCpfAccNo")) {
			String maxCpfacno="";
			
			try{
			 maxCpfacno = ps.getMaxCpfAccNo();
			 
			 StringBuffer sb = new StringBuffer();
				sb.append("<ServiceTypes>");
				String designation="";
					sb.append("<ServiceType>");
					sb.append("<cpfAcno>");
					if(maxCpfacno!=null)
						
					sb.append(maxCpfacno);
					sb.append("</cpfAcno>");
					sb.append("</ServiceType>");
				
				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			
		}
		EmployeeValidateInfo employeeValidLoadEditBean=new EmployeeValidateInfo();
		if(request.getParameter("method").equals("getPensionValidate")){
			SearchInfo searchBean=new SearchInfo();
			String date="",unitCD="",year="",month="",monthID="",monthName="",selectInputParamVal="";
			int count=0;
			date=request.getParameter("effectDT");
			unitCD=request.getParameter("unitCD");
			monthID=request.getParameter("monthID");
			monthName=request.getParameter("monthName");
			selectInputParamVal=request.getParameter("selectedIParam");
			log.info("Effective Date"+date+"unitCD"+unitCD);
			searchBean=financeService.getFinancialData(date,unitCD,gridLength);
			log.info("================financalList=====getPensionValidate=======");
			request.setAttribute("searchBean",searchBean);
			year=date.substring(0,date.length()-2);
			month=date.substring(4,date.length());
			request.setAttribute("effectiveYear",year);
			request.setAttribute("effectiveMonth",month);
			request.setAttribute("monthID",monthID);
			request.setAttribute("monthName",monthName);
			log.info("================financalList=====getPensionValidate======="+monthID+"monthName========"+monthName);
			request.setAttribute("unitCD",unitCD);
			request.setAttribute("selectParams",selectInputParamVal);
			log.info("effectiveYear"+year+"unitCD"+unitCD+"month"+month);
			log.info("============getPensionValidate======================selectInputParamVal=="+selectInputParamVal);
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/PensionValidation.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("validateNavigation")){
			String date="",unitCD="",year="",monthID="",monthName="",month="",navButton="",selectInputParamVal="";
			int strtIndex=0,totalRecords=0;
			date=request.getParameter("effectDT");
			unitCD=request.getParameter("unitCD");
			monthID=request.getParameter("monthID");
			monthName=request.getParameter("monthName");
			selectInputParamVal=request.getParameter("selectedIParam");
			SearchInfo getSearchInfo=new SearchInfo();
			SearchInfo searchBean=new SearchInfo();
			if (request.getParameter("strtindx") != null) {
					
				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				//System.out.println("=============strtIndex=============validateNavigation===" + strtIndex);
				getSearchInfo.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				getSearchInfo.setTotalRecords(totalRecords);

			}
			if (request.getParameter("navButton") != null) {

				navButton = request.getParameter("navButton").toString();
				getSearchInfo.setNavButton(navButton);
			}
			
			searchBean=financeService.navigationFinancialData(date,unitCD,getSearchInfo,gridLength);
			
			log.info("Effective Date"+date+"unitCD"+unitCD);
			request.setAttribute("searchBean",searchBean);
			year=date.substring(0,date.length()-2);
			month=date.substring(4,date.length());
			request.setAttribute("effectiveYear",year);
			request.setAttribute("effectiveMonth",month);
			request.setAttribute("monthID",monthID);
			request.setAttribute("monthName",monthName);
			request.setAttribute("unitCD",unitCD);
			request.setAttribute("selectParams",selectInputParamVal);
			log.info("============validateNavigation======================selectInputParamVal=="+selectInputParamVal);
			log.info("effectiveYear"+year+"unitCD"+unitCD+"month"+month);
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/PensionValidation.jsp");
			rd.forward(request, response);
		}/*else if(request.getParameter("method").equals("validationReport")){
			SearchInfo searchBean=new SearchInfo();
			String date="",unitCD="",year="",month="",monthID="",monthName="",selectInputParamVal="",dateVal="";
			int count=0;
			date=request.getParameter("effectDT");
			unitCD=request.getParameter("unitCD");
			monthID=request.getParameter("monthID");
			monthName=request.getParameter("monthName");
			selectInputParamVal=request.getParameter("selectedIParam");
			log.info("Effective Date"+date+"unitCD"+unitCD);
			searchBean=financeService.getFinancialData(date,unitCD);
			CommonUtil commonUtil=new CommonUtil();
			try {
				dateVal=commonUtil.converDBToAppFormat(date,"yyyyMM","MMM-yy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("================financalList=====validationReport=======");
			request.setAttribute("searchBean",searchBean);
			request.setAttribute("aiportCD",unitCD);
			request.setAttribute("date",dateVal);
			year=date.substring(0,date.length()-2);
			month=date.substring(4,date.length());
		
		
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/PensionValidationReport.jsp");
			rd.forward(request, response);
		}*/
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("importformats")) {	
          RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/ImportFormats.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("validationReport")){
		
			String date="",unitCD="",year="", month="", monthID="",monthName="", selectInputParamVal="", dateVal="";
			int count=0;
			ArrayList financeReport=new ArrayList();
			date=request.getParameter("effectDT");
			unitCD=request.getParameter("unitCD");
			monthID=request.getParameter("monthID");
			monthName=request.getParameter("monthName");
			selectInputParamVal=request.getParameter("selectedIParam");
			log.info("Effective Date"+date+"unitCD"+unitCD);
			financeReport=financeService.financeValidateReport(date,unitCD);
			CommonUtil commonUtil=new CommonUtil();
			try {
				dateVal=commonUtil.converDBToAppFormat(date,"yyyyMM","MMM-yy");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("================financalList=====validationReport=======");
	
			request.setAttribute("aiportCD",unitCD);
			request.setAttribute("date",dateVal);
			request.setAttribute("financeReport",financeReport);
			year=date.substring(0,date.length()-2);
			month=date.substring(4,date.length());
		
		
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/PensionValidateReportBySearch.jsp");
			rd.forward(request, response);
		}
		else if(request.getParameter("method").equals("getFinanceDetailEdit")){
			EmployeeValidateInfo validateInfo=new EmployeeValidateInfo();
			String convertEffectivedate="",unitCD="",dateVal="",region="";
			String cpfacno="",effectiveDate="",employeeName="",employeeNO="",designation="",pensionno="";
			if(request.getParameter("cpfaccno")!=null){
				cpfacno=request.getParameter("cpfaccno");
			}else{
				cpfacno="";
			}
			if(request.getParameter("pensionno")!=null){
				pensionno=request.getParameter("pensionno");
			}else{
				pensionno="";
			}
			log.info("pensionno"+pensionno);
			if(request.getParameter("airportCD")!=null){
				unitCD=request.getParameter("airportCD");
			}else{
				unitCD="";
			}
			if(request.getParameter("effectiveDate")!=null){
				effectiveDate=request.getParameter("effectiveDate");
			}else{
				effectiveDate="";
			}
			if(request.getParameter("employeeNM")!=null){
				employeeName=request.getParameter("employeeNM");
			}else{
				employeeName="";
			}
			if(request.getParameter("employeeno")!=null){
				employeeNO=request.getParameter("employeeno");
			}else{
				employeeNO="";
			}
		
			if(request.getParameter("designation")!=null){
				designation=request.getParameter("designation");
			}else{
				designation="";
			}
			if(request.getParameter("region")!=null){
				region=request.getParameter("region");
			}else{
				region="";
			}
			CommonUtil commonUtil=new CommonUtil();
			try {
				if(!effectiveDate.equals("")){
                     
                     convertEffectivedate = commonUtil.converDBToAppFormat(effectiveDate, "yyyy-MM-dd", "MMM-yy");
	                   
				}
				log.info("cpfacno"+cpfacno+"pensionno"+pensionno+"unitCD"+unitCD+"effectiveDate"+effectiveDate+"designation"+designation+"employeeNO"+employeeNO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			validateInfo=financeService.getLoadFinancialEditDetails(cpfacno,pensionno,unitCD,convertEffectivedate,employeeNO,designation,employeeName,region);
			log.info("================financalList=====validationReport=======");
			request.setAttribute("loadFinValidationList",validateInfo);
			request.setAttribute("effectiveDate",effectiveDate);
			request.setAttribute("unitCode",unitCD);
			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/FinanceDataEdit.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("UpdateFinanceDetail")){
			EmployeeValidateInfo validateInfo=new EmployeeValidateInfo();
			String convertEffectivedate="",unitCD="",frmBasic="",frmDailyAllowance="",frmSpcBasic="",region="";
			String frmPFStatury="",frmPFAdvance="",frmVPF="",frmPartFinal="",frmAdvanceDrawn="",frmCPFinterest="";
			String pensionno="",cpfacno="",effectiveDate="",employeeName="",frmName="",employeeNO="",designation="",frmEmoluments;
			EmployeeValidateInfo updateValidateInfo=new EmployeeValidateInfo();
			if(request.getParameter("cpfaccno")!=null){
				cpfacno=request.getParameter("cpfaccno");
				updateValidateInfo.setCpfaccno(cpfacno);
			}else{
				updateValidateInfo.setCpfaccno("");
			}
			if(request.getParameter("frmName")!=null){
				frmName=request.getParameter("frmName");			
			}else{
				frmName="";
			}		
			if(request.getParameter("pfid")!=null){
				pensionno=request.getParameter("pfid");
				updateValidateInfo.setPensionNumber(pensionno);
			}else{
				updateValidateInfo.setPensionNumber("");
			}
			if(request.getParameter("airportCD")!=null){
				unitCD=request.getParameter("airportCD");
				
			}else{
				unitCD="";
			}
			if(request.getParameter("region")!=null){
				region=request.getParameter("region");
				
			}else{
				region="";
			}
			if(request.getParameter("effective_date")!=null){
				effectiveDate=request.getParameter("effective_date");
			}else{
				effectiveDate="";
			}
			if(request.getParameter("employee_name")!=null){
				employeeName=request.getParameter("employee_name");
				updateValidateInfo.setEmployeeName(employeeName);
			}else{
				updateValidateInfo.setEmployeeName("");
			}
			if(request.getParameter("txtbasic")!=null){
				frmBasic=request.getParameter("txtbasic");
				updateValidateInfo.setBasic(frmBasic);
			}else{
				updateValidateInfo.setBasic("0.0");
			}
			
			if(request.getParameter("txtEmoluments")!=null){
				frmEmoluments=request.getParameter("txtEmoluments");
				updateValidateInfo.setEmoluments(frmEmoluments);
			}else{
				updateValidateInfo.setEmoluments("0.0");
			}
			
			if(request.getParameter("txtspecialbasic")!=null){
				frmSpcBasic=request.getParameter("txtspecialbasic");
				updateValidateInfo.setSpecialBasic(frmSpcBasic);
			}else{
				updateValidateInfo.setSpecialBasic("0.0");
			}
			
			if(request.getParameter("txtpfStatury")!=null){
				frmPFStatury=request.getParameter("txtpfStatury");
				updateValidateInfo.setEmpPFStatuary(frmPFStatury);
			}else{
				updateValidateInfo.setEmpPFStatuary("0.0");
			}
			
			if(request.getParameter("txtVPF")!=null){
				frmVPF=request.getParameter("txtVPF");
				updateValidateInfo.setEmpVPF(frmVPF);
			}else{
				updateValidateInfo.setEmpVPF("0.0");
			}
			
			if(request.getParameter("txtPFAdv")!=null){
				frmPFAdvance=request.getParameter("txtPFAdv");
				updateValidateInfo.setPfAdvance(frmPFAdvance);
			}else{
				updateValidateInfo.setPfAdvance("0.0");
			}
			if(request.getParameter("txtCPFInterest")!=null){
				frmCPFinterest=request.getParameter("txtCPFInterest");
				updateValidateInfo.setCpfInterest(frmCPFinterest);
			}else{
				updateValidateInfo.setCpfInterest("0.00");
			}
			if(request.getParameter("txtAdvanceDrwn")!=null){
				frmAdvanceDrawn=request.getParameter("txtAdvanceDrwn");
				updateValidateInfo.setAdvanceDrawn(frmAdvanceDrawn);
			}else{
				updateValidateInfo.setAdvanceDrawn("0.0");
			}
			String remarks="";
			if(request.getParameter("txtnewremarks")!=null){
				remarks=request.getParameter("txtnewremarks");
				updateValidateInfo.setRemarks(remarks);
			}
			
			if(request.getParameter("txtPartFinal")!=null){
				frmPartFinal=request.getParameter("txtPartFinal");
				updateValidateInfo.setPartFinal(frmPartFinal);
			}else{
				updateValidateInfo.setPartFinal("0.0");
			}
			CommonUtil commonUtil=new CommonUtil();
			try {
				if(!effectiveDate.equals("")){
					convertEffectivedate=commonUtil.converDBToAppFormat(effectiveDate,"yyyy-MM-dd","MMM-yy");
				}
				log.info("pensionno"+pensionno+"cpfacno"+cpfacno+"unitCD"+unitCD+"convertEffectivedate"+convertEffectivedate);
				log.info("frmBasic"+frmBasic+"frmDailyAllowance"+frmDailyAllowance+"frmSpcBasic"+frmSpcBasic);
				
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
			String userName = (String) session.getAttribute("userid");
			String computerName = (String) session.getAttribute("computername");
			log.info("==========UpdateFinanceDetail===region===="+region+"frmName===="+frmName);
			int count=financeService.financialUpdateDetails(updateValidateInfo,unitCD,convertEffectivedate,pensionno,cpfacno,employeeName,userName,computerName,region);
			log.info("================financalList=====UpdateFinanceDetail======="+count+"frmName===="+frmName);
			request.setAttribute("loadFinValidationList",validateInfo);
			request.setAttribute("effectiveDate",effectiveDate);
			RequestDispatcher rd=null;
			if(frmName.equals("CPFDeviation")){
				rd = request.getRequestDispatcher("./PensionView/CPFDeviationSearch.jsp");
			}else{
				rd = request.getRequestDispatcher("./PensionView/FinanceDataSearch.jsp");
			}
			
			rd.forward(request, response);	
			}else if(request.getParameter("method").equals("getPensionDetail")){
			String cpfACCNo="",validateAAIPF="",validateAAIPension="",validateAAITotal="",unitCD="",userName="";
			String effectiveDate="",selectedParam="";
			ArrayList loadFinValidationList=new ArrayList();
			if(request.getParameter("cpfaccno")!=null){
				cpfACCNo=request.getParameter("cpfaccno");
				if(request.getParameter("airportCD")!=null){
					unitCD=request.getParameter("airportCD");
				}
				if(request.getParameter("selectedParam")!=null){
					selectedParam=request.getParameter("selectedParam");
				}
				
				if(request.getParameter("valAAIPF")!=null){
					validateAAIPF=request.getParameter("valAAIPF");
					
				}
				if(request.getParameter("valAAIPension")!=null){
					validateAAIPension=request.getParameter("valAAIPension");
				
					
				}
				if(request.getParameter("valAAITotal")!=null){
					validateAAITotal=request.getParameter("valAAITotal");
					
				}
				if(request.getParameter("effectiveDate")!=null){
					effectiveDate=request.getParameter("effectiveDate");
				}
				
				
				employeeValidLoadEditBean=financeService.getFinancialLoadEditDetails(cpfACCNo,unitCD,effectiveDate);
				request.setAttribute("validateAAITotal",validateAAITotal);
				request.setAttribute("validateAAIPension",validateAAIPension);
				request.setAttribute("validateAAIPF",validateAAIPF);
				request.setAttribute("effectiveDate",effectiveDate);
				request.setAttribute("loadFinValidationList",employeeValidLoadEditBean);
				request.setAttribute("unitCode",unitCD);
				request.setAttribute("selectedParam",selectedParam);
				log.info("============getPensionDetail======================selectedParam=="+selectedParam);

			
				System.out.println("validateAAITotal"+validateAAITotal+"validateAAIPension"+validateAAIPension+"validateAAIPF"+validateAAIPF);
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/PenionValidationEdit.jsp");
				rd.forward(request, response);
			}else{
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/PensionValidation.jsp");
				rd.forward(request, response);
			}
			}else if(request.getParameter("method").equals("updatePensionDetail")){
			String cpfACCNo="",unitCD="",validateAAIPF="",validateAAIPension="",validateAAITotal="",pfStatury="",pfDeduction="";
			String valPFStatury="",valPFDedcuntion="",userName="",effectiveDate="",inputParams="";
			if(request.getParameter("cpfaccno")!=null){
				cpfACCNo=request.getParameter("cpfaccno");
				if(request.getParameter("airportCD")!=null){
					unitCD=request.getParameter("airportCD");
				}
				
				if(request.getParameter("valAAIPF")!=null){
					validateAAIPF=request.getParameter("valAAIPF");
					
				}
				if(request.getParameter("valAAIPension")!=null){
					validateAAIPension=request.getParameter("valAAIPension");
				
					
				}
				if(request.getParameter("valAAITotal")!=null){
					validateAAITotal=request.getParameter("valAAITotal");
				}
				System.out.println("validateAAITotal"+validateAAITotal+"validateAAIPension"+validateAAIPension+"validateAAIPF"+validateAAIPF);
				if(request.getParameter("valPFStatury")!=null){
					valPFStatury=request.getParameter("valPFStatury");
					
				}
				if(request.getParameter("valPFDeduction")!=null){
					valPFDedcuntion=request.getParameter("valPFDeduction");
				}
				if(request.getParameter("effectiveSelDate")!=null){
					effectiveDate=request.getParameter("effectiveSelDate");
				}
				if(request.getParameter("selectParams")!=null){
					inputParams=request.getParameter("selectParams");
					
				}
				
				int insertVal=0;
				log.info("validateAAITotal"+validateAAITotal+"validateAAIPension"+validateAAIPension+"validateAAIPF"+validateAAIPF);
				insertVal=financeService.updateFinancialDetails(cpfACCNo,unitCD,validateAAIPF,validateAAIPension,validateAAITotal,valPFStatury,valPFDedcuntion,userName,effectiveDate);
				request.setAttribute("validateAAITotal",validateAAITotal);
				request.setAttribute("validateAAIPension",validateAAIPension);
				request.setAttribute("validateAAIPF",validateAAIPF);
			
				request.setAttribute("unitCode",unitCD);
				String tempInfo[] = null;
				log.info("==============updatePensionDetail==============inputParams"+inputParams);
				if(!inputParams.equals("")){
					tempInfo=inputParams.split("/");
					log.info("==============updatePensionDetail==.2============inputParams"+inputParams);
					request.setAttribute("monthID",tempInfo[0]);
					request.setAttribute("monthName",tempInfo[1]);
					request.setAttribute("effectiveYear",tempInfo[2]);
					request.setAttribute("airportID",tempInfo[3]);
				}
				request.setAttribute("inputParams",inputParams);
				request.setAttribute("effectiveDate",effectiveDate);
				
				request.setAttribute("from","validatePension");
				request.setAttribute("messg","Updated Successfully");
				
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/Result.jsp");
				rd.forward(request, response);
			}else{
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/PenionValidationEdit.jsp");
				
				rd.forward(request, response);
			}
		}else if(request.getParameter("method").equals("getFinancialDetals")){
			ArrayList financalList=new ArrayList();
			String cpfaccno="",birthdate="",empName="",pensionnumber="";
			cpfaccno=request.getParameter("cpfacno");
			birthdate=request.getParameter("birthdate");
			empName = request.getParameter("empName");
			pensionnumber= request.getParameter("pensionnumber");
			
			   SimpleDateFormat from = new   SimpleDateFormat("dd-MM-yyyy");
			   SimpleDateFormat to = new SimpleDateFormat("dd/MMM/yyyy");
			  try{
			//     birthdate = to.format(from.parse(birthdate));
			  }catch(Exception e){
				  
			  }
		
			log.info("cpfaccno "+cpfaccno +"birthdate"+birthdate);
			FinancialDAO fd=new FinancialDAO();
			String emoluments=request.getParameter("emoluments").toString();
			String AAicontribution =fd.validatePFPension(birthdate,emoluments,"A",birthdate);
			log.info("AAicontribution"+AAicontribution );
			EmpMasterBean ServiceType=new EmpMasterBean();
			ServiceType.setCpfAcNo(cpfaccno);
			ServiceType.setEmpName(empName);
			ServiceType.setPensionNumber(pensionnumber);
		    SimpleDateFormat too = new   SimpleDateFormat("dd/MMM/yyyy");
			 SimpleDateFormat fromm = new SimpleDateFormat("dd-MM-yyyy");
			  try{
			     birthdate = too.format(fromm.parse(birthdate));
			  }catch(Exception e){
			  }
		     
		    
		    String tempInfo[] = AAicontribution.split(",");
			 String aaiPf=tempInfo[0];
			 String aaiPension= tempInfo[1];
			 String total= tempInfo[2];
			 
			 ServiceType.setAaiPf(aaiPf);
			 ServiceType.setAaiPension(aaiPension);
			 ServiceType.setAaiTotal(total);
			 ServiceType.setDateofBirth(birthdate);
			 String  emoluments1=emoluments;
			 ServiceType.setEmoluments(emoluments1);
		    
			 StringBuffer sb = new StringBuffer();
				sb.append("<ServiceTypes>");
					String employeeName = "";
					sb.append("<ServiceType>");
					sb.append("<employeeName>");
					if(ServiceType.getEmpName()!=null)
					employeeName = ServiceType.getEmpName().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
					sb.append(employeeName);
					sb.append("</employeeName>");
					sb.append("<pensionNumber>");
					sb.append(ServiceType.getPensionNumber());
					sb.append("</pensionNumber>");				
					sb.append("<employeeNumber>");
					sb.append(ServiceType.getEmpNumber());
					sb.append("</employeeNumber>");	
					
					sb.append("<aaiPf>");
					sb.append(ServiceType.getAaiPf());
					sb.append("</aaiPf>");	
					sb.append("<aaiPension>");
					sb.append(ServiceType.getAaiPension());
					sb.append("</aaiPension>");	
					sb.append("<total>");
					sb.append(ServiceType.getAaiTotal());
					sb.append("</total>");	
					sb.append("</ServiceType>");
				
				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.write(sb.toString());
		}else if(request.getParameter("method").equals("addFinancialDetals")){
			ArrayList financalList=new ArrayList();
			EmpMasterBean bean1=new EmpMasterBean();
			String cpfaccno="",birthdate="";
			if(request.getParameter("pensionnumber")!=null){
				bean1.setPensionNumber(request.getParameter("pensionnumber"));
				}
			if(request.getParameter("cpfacno")!=null){
			bean1.setCpfAcNo(request.getParameter("cpfacno"));
			}
			if(request.getParameter("employeeCode")!=null){
				bean1.setEmpNumber(request.getParameter("employeeCode"));
				}
			log.info("pensionnumber===="+request.getParameter("pensionnumber").toString());
			log.info("cpfacno"+request.getParameter("cpfacno").toString());
			log.info("cpfacno"+bean1.getCpfAcNo());
			if(request.getParameter("birthdate")!=null){
				 SimpleDateFormat from = new   SimpleDateFormat("dd-MM-yyyy");
				  SimpleDateFormat to = new SimpleDateFormat("dd/MMM/yyyy");
				try{
				  birthdate = to.format(from.parse(request.getParameter("birthdate")));
				
				bean1.setDateofBirth(birthdate);
				}catch(Exception e){
					log.printStackTrace(e);
				}
			}
			
			if(request.getParameter("empName")!=null){
			bean1.setEmpName(request.getParameter("empName"));
			}	
			int emoluments=Integer.parseInt(request.getParameter("emoluments").toString());
			if(emoluments!=-1){
				bean1.setEmoluments(String.valueOf(emoluments));
			}
			if(request.getParameter("pf")!=null){
				bean1.setEmployeePF(request.getParameter("pf"));
			}	
				
			if(request.getParameter("vpf")!=null){
				bean1.setEmployeeVPF(request.getParameter("vpf"));
			}
			if(request.getParameter("vpf")!=null){
				bean1.setEmployeeVPF(request.getParameter("vpf"));
			}
			if(request.getParameter("interest")!=null){
				bean1.setInterest(request.getParameter("interest"));
			}
			if(request.getParameter("principal")!=null){
				bean1.setPrincipal(request.getParameter("principal"));
			}
			
			if(request.getParameter("aaiPf")!=null){
				bean1.setAaiPf(request.getParameter("aaiPf"));
			}
			if(request.getParameter("aaiPension")!=null){
				bean1.setAaiPension(request.getParameter("aaiPension"));
			}
			if(request.getParameter("aaiTotal")!=null){
				bean1.setAaiTotal(request.getParameter("aaiTotal"));
			}
			if(request.getParameter("interest")!=null){
				bean1.setInterest(request.getParameter("interest"));
			}
			
			if(request.getParameter("aaiTotal")!=null){
				bean1.setAaiTotal(request.getParameter("aaiTotal"));
			}
			if(request.getParameter("basic")!=null){
				bean1.setBasic(request.getParameter("basic"));
			}
			if(request.getParameter("dailyAllowance")!=null){
				bean1.setDailyAllowance(request.getParameter("dailyAllowance"));
			}
			if(request.getParameter("splBasic")!=null){
				bean1.setSpecialBasic(request.getParameter("splBasic"));
			}
			
			if(request.getParameter("pfAdv")!=null){
				bean1.setPfAdvance(request.getParameter("pfAdv"));
			}
			if(request.getParameter("advDrawn")!=null){
				bean1.setAdvDrawn(request.getParameter("advDrawn"));
			}
			if(request.getParameter("partfinal")!=null){
				bean1.setPartFinal(request.getParameter("partfinal"));
			}
			if(request.getParameter("fromDt")!=null){
				bean1.setFromDate(commonUtil.getDate(request.getParameter("fromDt").toString()));
			}
			if(request.getParameter("region")!=null){
				bean1.setRegion(request.getParameter("region"));
			}
			if(request.getParameter("airPortCode")!=null){
				bean1.setStation(request.getParameter("airPortCode"));
			}
			
			log.info("cpfaccno "+cpfaccno +"birthdate"+birthdate+"emoluments"+emoluments + "region"+bean1.getRegion());
			FinancialDAO fd=new FinancialDAO();
			FinancialService fs=new FinancialService();
			fs.addFinancialDetals(bean1);
			
			    
			//request.setAttribute("AAicontribution",AAicontribution);
		//RequestDispatcher rd = request.getRequestDispatcher("./PensionView/AddFinancialDetail.jsp");
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/FinanceDataSearch.jsp");
		    rd.forward(request, response);
		}
		else if(request.getParameter("method").equals("missingMonthsReport")){
			String frm_region="";
			if(request.getParameter("frm_region")!=null){
			frm_region=request.getParameter("frm_region");
			}
			SearchInfo searchBean=new SearchInfo();
		
			searchBean=financeService.getFinancialReport(frm_region,"missing",200);
			request.setAttribute("searchBean",searchBean);
			request.setAttribute("region",frm_region);
 			log.info("financePersonlList.size()==================="+searchBean.getSearchList().size());
 			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/FinanceReportMissingMonths.jsp");
			rd.forward(request, response);
		}
		else if(request.getParameter("method").equals("missingMonthsReportNavigation")){	
			String frm_region="",navButton="";
			int strtIndex=0,totalRecords=0;
		
			if(request.getParameter("frm_region")!=null){
			frm_region=request.getParameter("frm_region");
			}
			SearchInfo searchBean=new SearchInfo();
			SearchInfo getSearchInfo=new SearchInfo();
			if (request.getParameter("strtindx") != null) {
				strtIndex = Integer.parseInt(request.getParameter("strtindx")
						.toString());
				getSearchInfo.setStartIndex(strtIndex);
			}
			if (request.getParameter("total") != null) {
				totalRecords = Integer.parseInt(request.getParameter("total")
						.toString());
				getSearchInfo.setTotalRecords(totalRecords);

			}
			if (request.getParameter("navButton") != null) {
				navButton = request.getParameter("navButton").toString();
				getSearchInfo.setNavButton(navButton);
			}
			searchBean=financeService.getnavigationFinancialReport(frm_region,"missing",200,getSearchInfo);
			request.setAttribute("searchBean",searchBean);
			request.setAttribute("region",frm_region);
 			log.info("financePersonlList.size()==================="+searchBean.getSearchList().size());
 			RequestDispatcher rd = request
			.getRequestDispatcher("./PensionView/FinanceReportMissingMonths.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("loadimportedprocess")){
			log.info("Loading Imported Process");
		//	HashMap regionHashmap = new HashMap();
        //    regionHashmap = commonUtil.getRegionsForComparativeReport();
         //   request.setAttribute("regionHashmap",regionHashmap);
            String[]regionLst=null;
            String rgnName="";
            HashMap map=new HashMap();
            Map monthMap = new LinkedHashMap();
            Map formsMap = new LinkedHashMap();
            Iterator monthIterator = null;
            Iterator formsListMap=null;
            if(session.getAttribute("region")!=null){
                regionLst=(String[])session.getAttribute("region");
            }
            log.info("Regions List"+regionLst.length);
            for(int i=0;i<regionLst.length;i++){
                rgnName=regionLst[i];
                if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
                    map=new HashMap();
                    map=commonUtil.getRegion();
                    break;
                }else{
                    map.put(new Integer(i),rgnName);
                }
                
            }
            monthMap = commonUtil.getMonthsList();
            Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			
			formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());
			
			Set formSet=formsMap.entrySet();
            formsListMap=formSet.iterator();
			request.setAttribute("monthIterator", monthIterator);
            request.setAttribute("regionHashmap", map);
            request.setAttribute("formsListMap", formsListMap);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("loadimportedprocessnavayuga")){
			log.info("Loading Imported Process");
	            String[]regionLst=null;
	            String rgnName="";
	            HashMap map=new HashMap();
	            Map monthMap = new LinkedHashMap();
	            Map formsMap = new LinkedHashMap();
	            Iterator monthIterator = null;
	            Iterator formsListMap=null;
	            if(session.getAttribute("region")!=null){
	                regionLst=(String[])session.getAttribute("region");
	            }
	            log.info("Regions List"+regionLst.length);
	            for(int i=0;i<regionLst.length;i++){
	                rgnName=regionLst[i];
	                if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                    map=new HashMap();
	                    map=commonUtil.getRegion();
	                    break;
	                }else{
	                    map.put(new Integer(i),rgnName);
	                }	                
	            }
	            monthMap = commonUtil.getMonthsList();
	            Set monthset = monthMap.entrySet();
				monthIterator = monthset.iterator();				
				formsMap=commonUtil.getFormsListNavayuga(session.getAttribute("userid").toString());				
				Set formSet=formsMap.entrySet();
	            formsListMap=formSet.iterator();
				request.setAttribute("monthIterator", monthIterator);
	            request.setAttribute("regionHashmap", map);
	            request.setAttribute("formsListMap", formsListMap);
				RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportDataNavayuga.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("method").equals("loadexportProcess")){
			log.info("Loading Imported Process");
			//	HashMap regionHashmap = new HashMap();
	        //    regionHashmap = commonUtil.getRegionsForComparativeReport();
	         //   request.setAttribute("regionHashmap",regionHashmap);
	            String[]regionLst=null;
	            String rgnName="";
	            HashMap map=new HashMap();
	            Map monthMap = new LinkedHashMap();
	            Iterator monthIterator = null;
	            Iterator monthIterator1 = null;
	            if(session.getAttribute("region")!=null){
	                regionLst=(String[])session.getAttribute("region");
	            }
	            log.info("Regions List"+regionLst.length);
	            for(int i=0;i<regionLst.length;i++){
	                rgnName=regionLst[i];
	                if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                    map=new HashMap();
	                    map=commonUtil.getRegion();
	                    break;
	                }else{
	                    map.put(new Integer(i),rgnName);
	                }
	                
	            }
	            ArrayList penYearList = new ArrayList();
	            FinancialReportService finReportService = new FinancialReportService();
	            penYearList = finReportService.getFinanceYearList();
	            monthMap = commonUtil.getMonthsList();
	            Set monthset = monthMap.entrySet();
	            monthIterator = monthset.iterator();
	            monthIterator1= monthset.iterator();
	            request.setAttribute("monthIterator", monthIterator);
	            request.setAttribute("monthToIterator", monthIterator1);
                request.setAttribute("yearList", penYearList);
	            request.setAttribute("regionHashmap", map);
				RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionExportData.jsp");
				rd.forward(request, response);
			}
		else if(request.getParameter("method").equals("importedprocess")){
			String fileName="",region="",formType="",airportcode="";
			if (request.getParameter("frm_file") != null) {
			 fileName=request.getParameter("frm_file").toString();
			}
			if (request.getParameter("frm_region") != null) {
				 region=request.getParameter("frm_region").toString();
				}
			if (request.getParameter("frm_formtype") != null) {
				formType=request.getParameter("frm_formtype").toString();
				}
			if (request.getParameter("airPortCode") != null) {
				airportcode=request.getParameter("airPortCode").toString();
				}
			PensionService ps=new PensionService();
			try{
				String userName=(String)session.getAttribute("userid");
				String ipAddress=(String)session.getAttribute("computername");
				String year="",month="";
			String message=ps.readImportData(fileName, userName, ipAddress,region,year,month,airportcode);
			log.info("message in servlet " +message);
		 	HashMap regionHashmap = new HashMap();
            regionHashmap = commonUtil.getRegionsForComparativeReport();
            request.setAttribute("regionHashmap",regionHashmap);
			
			request.setAttribute("message", message);
		//	response.sendRedirect("./PensionView/PensionFileUpload.jsp?message="+message);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
	        rd.forward(request, response);	
			}catch(InvalidDataException e){
				HashMap regionHashmap = new HashMap();
	            regionHashmap = commonUtil.getRegionsForComparativeReport();
	            request.setAttribute("regionHashmap",regionHashmap);
				request.setAttribute("errorMessage", e.getMessage());
				RequestDispatcher rd = request
				.getRequestDispatcher("./PensionView/PensionImportData.jsp");
		        rd.forward(request, response);								
			}
			
		}
		
		// Srilakshmi
		
		else if(request.getParameter("method").equals("insertRemittanceInfo")){
			double amt = 0.0;
			String date = "", bankName="", bankAddr="", region="", monthYear="", airport="", year="", month="";
			String monthNo[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
			String monthCd[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			
			amt = Double.parseDouble(request.getParameter("remitAmount"));
			date = request.getParameter("remittedDate");
			bankName = request.getParameter("bankName");
			bankAddr = request.getParameter("bankAddr");
			region = request.getParameter("frm_region");
			monthYear = request.getParameter("frm_month_year");
			airport = request.getParameter("airport");
			
			System.out.println("region is ==== "+region);
			
			form3Bean formbean=new form3Bean();
			formbean.setAirportCode(airport);
			formbean.setRemittedAmt(amt);
			formbean.setRemittedDate(date);
			formbean.setBankName(bankName);
			formbean.setBankAddr(bankAddr);
			formbean.setRegion(region);
			formbean.setMonthYear(monthYear);
			int i = financeService.addRemittanceInfo(formbean);
			
			if(monthYear.length() > 4){
				year = monthYear.substring(monthYear.length()-4,monthYear.length());
				month = monthYear.substring(0,3);
			}else if(monthYear.length() > 4){
				year = monthYear;
			}
			
			for(int j= 0;j<monthCd.length;j++){
				if(month.equals(monthCd[j])){
					month = monthNo[j];
					break;
				}
			}
			
			if(i>0){
				//reportservlet?method=getform3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID;
				RequestDispatcher rd = request.getRequestDispatcher("./reportservlet?method=getform6&type=no&frm_region="+region+"&frm_year="+year+"&frm_month="+month);
				rd.forward(request, response);
			}
		}else if(request.getParameter("method").equals("loadimportedDeviationProcess")){
			log.info("Loading Imported Process");
			//	HashMap regionHashmap = new HashMap();
	        //    regionHashmap = commonUtil.getRegionsForComparativeReport();
	         //   request.setAttribute("regionHashmap",regionHashmap);
	            String[]regionLst=null;
	            String rgnName="";
	            HashMap map=new HashMap();
	            Map monthMap = new LinkedHashMap();
	            Map formsMap = new LinkedHashMap();
	            Iterator monthIterator = null;
	            Iterator formsListMap=null;
	            if(session.getAttribute("region")!=null){
	                regionLst=(String[])session.getAttribute("region");
	            }
	            log.info("Regions List"+regionLst.length);
	            for(int i=0;i<regionLst.length;i++){
	                rgnName=regionLst[i];
	                if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                    map=new HashMap();
	                    map=commonUtil.getRegion();
	                    break;
	                }else{
	                    map.put(new Integer(i),rgnName);
	                }
	                
	            }
	            monthMap = commonUtil.getMonthsList();
	            Set monthset = monthMap.entrySet();
				monthIterator = monthset.iterator();
				
				formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());
				
				Set formSet=formsMap.entrySet();
	            formsListMap=formSet.iterator();
				request.setAttribute("monthIterator", monthIterator);
	            request.setAttribute("regionHashmap", map);
	            request.setAttribute("formsListMap", formsListMap);
				RequestDispatcher rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionImportDeviationScreen.jsp");
				rd.forward(request, response);
			}else if(request.getParameter("method").equals("editimportedprocess")){
			String  monthYear = "", region = "", pfid = "", airportcode = "",dateofbirth="",wetheroption="";
			String emoluments = "0.00", editid = "", vpf = "0.00", principle = "0.00", interest = "0.00";
			String epf = "0.00";
			String url = "";
			String Remarks="";
			String remarkstype = "",userId="";
			
			if (request.getParameter("pensionNo") != null) {
				pfid = commonUtil.getSearchPFID1(request.getParameter(
						"pensionNo").toString());
			}
			if (request.getParameter("dob") != null) {
			dateofbirth=request.getParameter("dob");
			}
			if (request.getParameter("wopt") != null) {
				wetheroption=request.getParameter("wopt");
				}
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("emol") != null) {
				emoluments = request.getParameter("emol");
			}
			if (request.getParameter("empepf") != null) {
				epf = request.getParameter("empepf");
			}
			if (request.getParameter("empvpf") != null) {
				vpf = request.getParameter("empvpf");
			}
			if (request.getParameter("princip") != null) {
				principle = request.getParameter("princip");
			}
			if (request.getParameter("intrest") != null) {
				interest = request.getParameter("intrest");
			}
			if (request.getParameter("regn") != null) {
				region = request.getParameter("regn");
			}
			if (request.getParameter("station") != null) {
				airportcode = request.getParameter("station");
			}
			if (request.getParameter("remarksType") != null) {
				remarkstype = request.getParameter("remarksType");
			}			
			if (request.getParameter("remarks") != null) {
				Remarks = request.getParameter("remarks");
			}	
			if (request.getParameter("editid") != null) {
				editid = request.getParameter("editid");
			}
			if (request.getParameter("userid") != null) {
				userId = request.getParameter("userid");
			}
			pfid = commonUtil.trailingZeros(pfid.toCharArray());			
			ps.editimportedprocess(pfid,dateofbirth,wetheroption,
					monthYear, emoluments, epf, vpf, principle, interest,
					 region, airportcode,remarkstype,Remarks,userId);
			log.info(editid);
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.write(editid);

		}else if(request.getParameter("method").equals("edittransferstatus")){
				String  monthYear = "", region = "", pfid = "", airportcode = "",editid="";
				String remarkstype = "",userId="";				
				if (request.getParameter("pensionNo") != null) {
					pfid = commonUtil.getSearchPFID1(request.getParameter(
							"pensionNo").toString());
				}				
				if (request.getParameter("monthyear") != null) {
					monthYear = request.getParameter("monthyear");
				}				
				if (request.getParameter("remarksType") != null) {
					remarkstype = request.getParameter("remarksType");
				}	
				if (request.getParameter("editid") != null) {
					editid = request.getParameter("editid");
				}
				if (request.getParameter("userid") != null) {
					userId = request.getParameter("userid");
				}
				if (request.getParameter("station") != null) {
					airportcode = request.getParameter("station");
				}
				if (request.getParameter("regn") != null) {
					region = request.getParameter("regn");
				}
				pfid = commonUtil.trailingZeros(pfid.toCharArray());			
				ps.edittransferstatus(pfid,monthYear,remarkstype,userId,airportcode,region);
				log.info(editid);
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.write(editid);

			}else if(request.getParameter("method").equals("insertCpfData")){
			log.info("inside insertCpfData");
			String  monthYear = "", region = "",airportcode="",importmessage="";
			ArrayList empList = new ArrayList();
			ArrayList cntList = new ArrayList();
			ArrayList accList = new ArrayList();
			ArrayList transferList=new ArrayList();
			int count=0;
			String username=session.getAttribute("userid").toString();			
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("regn") != null) {
				region = request.getParameter("regn");
			}
			if (request.getParameter("station") != null) {
				airportcode = request.getParameter("station");
			}
			log.info("monthYear"+monthYear+"region"+region+"airportcode"+airportcode);
			transferList=ps.getTransferInOutRecordsDirect(username,monthYear);	
			RequestDispatcher rd = null;
			
			if(transferList.size()>0){				
			accList=(ArrayList) transferList.get(0);					
			if(accList.size()>0){
				empList = (ArrayList)accList.get(0);
				cntList = (ArrayList)accList.get(1);
				 }	
			 log.info("transferList inside="+transferList.size());
			 request.setAttribute("empinfo", empList);
				request.setAttribute("cntinfo", cntList);
			 rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionTransferInOutReport.jsp");
			}else{
				 count=ps.loadimportedprocess(monthYear,region, airportcode);	
					String[]regionLst=null;
			         String rgnName="";
			         HashMap map=new HashMap();
			         if(session.getAttribute("region")!=null){
			             regionLst=(String[])session.getAttribute("region");
			         }	        
			         for(int i=0;i<regionLst.length;i++){
			             rgnName=regionLst[i];
			             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
			                 map=new HashMap();
			                 map=commonUtil.getRegion();
			                 break;
			             }else{
			                 map.put(new Integer(i),rgnName);
			             }	             
			         }	         
			        	 Map monthMap = new LinkedHashMap();
				         Iterator monthIterator = null;
						 monthMap = commonUtil.getMonthsList();
						 Set monthset = monthMap.entrySet();
						 monthIterator = monthset.iterator();
						 Map formsMap = new LinkedHashMap();				
					  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
						 Iterator formsIterator = null;
						 Set formsset=formsMap.entrySet();
						 formsIterator = formsset.iterator();
			             request.setAttribute("regionHashmap", map);
			     	     request.setAttribute("monthIterator", monthIterator);
			     	     request.setAttribute("formsListMap", formsIterator);	
			     	     log.info("count"+count);
					importmessage="Sheet Imported SuccessFully.Total Inserted Records -"+count+"";
					request.setAttribute("message", importmessage);
					 rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			}
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("loadimportsheet")){
			log.info("inside loadimportsheet");
			String  monthYear = "", region = "",airportcode="",importmessage="";
			int count=0;
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("regn") != null) {
				region = request.getParameter("regn");
			}
			if (request.getParameter("station") != null) {
				airportcode = request.getParameter("station");
			}
			log.info("monthYear"+monthYear+"region"+region+"airportcode"+airportcode);
			 count=ps.loadimportedprocess(monthYear,region, airportcode);	
			String[]regionLst=null;
	         String rgnName="";
	         HashMap map=new HashMap();
	         if(session.getAttribute("region")!=null){
	             regionLst=(String[])session.getAttribute("region");
	         }	        
	         for(int i=0;i<regionLst.length;i++){
	             rgnName=regionLst[i];
	             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                 map=new HashMap();
	                 map=commonUtil.getRegion();
	                 break;
	             }else{
	                 map.put(new Integer(i),rgnName);
	             }	             
	         }	         
	        	 Map monthMap = new LinkedHashMap();
		         Iterator monthIterator = null;
				 monthMap = commonUtil.getMonthsList();
				 Set monthset = monthMap.entrySet();
				 monthIterator = monthset.iterator();
				 Map formsMap = new LinkedHashMap();				
			  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
				 Iterator formsIterator = null;
				 Set formsset=formsMap.entrySet();
				 formsIterator = formsset.iterator();
	             request.setAttribute("regionHashmap", map);
	     	     request.setAttribute("monthIterator", monthIterator);
	     	     request.setAttribute("formsListMap", formsIterator);	
	     	     log.info("count"+count);
			importmessage="Sheet Imported SuccessFully.Total Inserted Records -"+count+"";
			request.setAttribute("message", importmessage);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("deleteimportsheet")){
			log.info("inside loadimportsheet");
			String  monthYear = "", region = "",airportcode="",importmessage="";
			int count=0;
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			if (request.getParameter("regn") != null) {
				region = request.getParameter("regn");
			}
			if (request.getParameter("station") != null) {
				airportcode = request.getParameter("station");
			}
			log.info("monthYear"+monthYear+"region"+region+"airportcode"+airportcode);
			 count=ps.deleteimportsheet(monthYear,region, airportcode);	
			String[]regionLst=null;
	         String rgnName="";
	         HashMap map=new HashMap();
	         if(session.getAttribute("region")!=null){
	             regionLst=(String[])session.getAttribute("region");
	         }	        
	         for(int i=0;i<regionLst.length;i++){
	             rgnName=regionLst[i];
	             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                 map=new HashMap();
	                 map=commonUtil.getRegion();
	                 break;
	             }else{
	                 map.put(new Integer(i),rgnName);
	             }	             
	         }	         
	        	 Map monthMap = new LinkedHashMap();
		         Iterator monthIterator = null;
				 monthMap = commonUtil.getMonthsList();
				 Set monthset = monthMap.entrySet();
				 monthIterator = monthset.iterator();
				 Map formsMap = new LinkedHashMap();				
			  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
				 Iterator formsIterator = null;
				 Set formsset=formsMap.entrySet();
				 formsIterator = formsset.iterator();
	             request.setAttribute("regionHashmap", map);
	     	     request.setAttribute("monthIterator", monthIterator);
	     	     request.setAttribute("formsListMap", formsIterator);	
	     	     log.info("count"+count);
			importmessage="Sheet Deleted  SuccessFully.Total Deleted Records -"+count+"";
			request.setAttribute("message", importmessage);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			
			rd.forward(request, response);
		}
		else if(request.getParameter("method").equals("insertCpfDataDirectly")){
			log.info("inside insertCpfDataDirectly");
			String  username = "", monthYear = "",airportcode="",importmessage="";
			ArrayList empList = new ArrayList();
			ArrayList cntList = new ArrayList();
			ArrayList transferList=new ArrayList();
			int count=0;
			if (request.getParameter("userid") != null) {
				username = request.getParameter("userid");
			}
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}
			log.info("username"+username+"monthYear"+monthYear+"airportcode"+airportcode);
			transferList=ps.getTransferInOutRecordsDirect(username,monthYear);
			RequestDispatcher rd=null;
			if(transferList.size()>0){
			 log.info("transferList=  inside directly"+transferList.size());
			 request.setAttribute("transferlist", transferList);
				//request.setAttribute("cntinfo", cntList);
			 rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionTransferInOutReportDirect.jsp");
			}else{
				count=ps.loadimportedprocessdirectly(username);
				String[]regionLst=null;
		         String rgnName="";
		         HashMap map=new HashMap();
		         if(session.getAttribute("region")!=null){
		             regionLst=(String[])session.getAttribute("region");
		         }	        
		         for(int i=0;i<regionLst.length;i++){
		             rgnName=regionLst[i];
		             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
		                 map=new HashMap();
		                 map=commonUtil.getRegion();
		                 break;
		             }else{
		                 map.put(new Integer(i),rgnName);
		             }	             
		         }	         
		        	 Map monthMap = new LinkedHashMap();
			         Iterator monthIterator = null;
					 monthMap = commonUtil.getMonthsList();
					 Set monthset = monthMap.entrySet();
					 monthIterator = monthset.iterator();
					 Map formsMap = new LinkedHashMap();				
				  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
					 Iterator formsIterator = null;
					 Set formsset=formsMap.entrySet();
					 formsIterator = formsset.iterator();
		             request.setAttribute("regionHashmap", map);
		     	     request.setAttribute("monthIterator", monthIterator);
		     	     request.setAttribute("formsListMap", formsIterator);	
		     	    log.info("count"+count);
				importmessage="Sheet Imported SuccessFully.Total Inserted Records -"+count+"";
				request.setAttribute("message", importmessage);
				rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			}
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("loadimportsheetdirectly")){
			log.info("inside loadimportsheetdirectly");
			String  username = "",importmessage="";
			int count=0;
			if (request.getParameter("userid") != null) {
				username = request.getParameter("userid");
			}			
			log.info("username"+username);
			count=ps.loadimportedprocessdirectly(username);
			String[]regionLst=null;
	         String rgnName="";
	         HashMap map=new HashMap();
	         if(session.getAttribute("region")!=null){
	             regionLst=(String[])session.getAttribute("region");
	         }	        
	         for(int i=0;i<regionLst.length;i++){
	             rgnName=regionLst[i];
	             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                 map=new HashMap();
	                 map=commonUtil.getRegion();
	                 break;
	             }else{
	                 map.put(new Integer(i),rgnName);
	             }	             
	         }	         
	        	 Map monthMap = new LinkedHashMap();
		         Iterator monthIterator = null;
				 monthMap = commonUtil.getMonthsList();
				 Set monthset = monthMap.entrySet();
				 monthIterator = monthset.iterator();
				 Map formsMap = new LinkedHashMap();				
			  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
				 Iterator formsIterator = null;
				 Set formsset=formsMap.entrySet();
				 formsIterator = formsset.iterator();
	             request.setAttribute("regionHashmap", map);
	     	     request.setAttribute("monthIterator", monthIterator);
	     	     request.setAttribute("formsListMap", formsIterator);	
	     	    log.info("count"+count);
			importmessage="Sheet Imported SuccessFully.Total Inserted Records -"+count+"";
			request.setAttribute("message", importmessage);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("deleteimportsheetdirect")){
			log.info("inside loadimportsheet");
			String  monthYear = "", username = "",importmessage="";
			int count=0;
			if (request.getParameter("monthyear") != null) {
				monthYear = request.getParameter("monthyear");
			}	
			if (request.getParameter("userid") != null) {
				username = request.getParameter("userid");
			}	
			log.info("monthYear"+monthYear+"username"+username);
			 count=ps.deleteimportsheetdirectly(monthYear,username);	
			String[]regionLst=null;
	         String rgnName="";
	         HashMap map=new HashMap();
	         if(session.getAttribute("region")!=null){
	             regionLst=(String[])session.getAttribute("region");
	         }	        
	         for(int i=0;i<regionLst.length;i++){
	             rgnName=regionLst[i];
	             if(rgnName.equals("ALL-REGIONS")&& session.getAttribute("usertype").toString().equals("Admin")){
	                 map=new HashMap();
	                 map=commonUtil.getRegion();
	                 break;
	             }else{
	                 map.put(new Integer(i),rgnName);
	             }	             
	         }	         
	        	 Map monthMap = new LinkedHashMap();
		         Iterator monthIterator = null;
				 monthMap = commonUtil.getMonthsList();
				 Set monthset = monthMap.entrySet();
				 monthIterator = monthset.iterator();
				 Map formsMap = new LinkedHashMap();				
			  	formsMap=commonUtil.getFormsList(session.getAttribute("userid").toString());			     
				 Iterator formsIterator = null;
				 Set formsset=formsMap.entrySet();
				 formsIterator = formsset.iterator();
	             request.setAttribute("regionHashmap", map);
	     	     request.setAttribute("monthIterator", monthIterator);
	     	     request.setAttribute("formsListMap", formsIterator);	
	     	     log.info("count"+count);
			importmessage="Sheet Deleted  SuccessFully.Total Deleted Records  -"+count+"";
			request.setAttribute("message", importmessage);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/PensionImportData.jsp");
			
			rd.forward(request, response);
		}else if(request.getParameter("method").equals("getDeviationRecords")){
			String  year = "",month="", region = "",airportcode="";			
			if (request.getParameter("Year") != null) {
				year = request.getParameter("Year");
			}
			if (request.getParameter("Month") != null) {
				month = request.getParameter("Month");
			}
			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region");
			}
			if (request.getParameter("airPortCode") != null) {
				airportcode = request.getParameter("airPortCode");
			}
			ArrayList pensionList=new ArrayList();
			try{
			pensionList=ps.getform3deviation(region,year,month,airportcode)	;
			request.setAttribute("Penlist", pensionList);
			RequestDispatcher rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionImportDeviation.jsp");
			rd.forward(request, response);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		else if(request.getParameter("method").equals("getDeviationRecordsDirect")){
			String username=session.getAttribute("userid").toString();
			ArrayList pensionList=new ArrayList();
			ArrayList transferList=new ArrayList();
			try{
			pensionList=ps.getform3deviationdirect(username);
			transferList=ps.getTransferInOutRecordsDirect(username,"");
			RequestDispatcher rd=null;		
			if(pensionList.size()>0){
				request.setAttribute("Penlist", pensionList);
			rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionImportDeviationDirectly.jsp");
			}else{
				 request.setAttribute("transferlist", transferList);
				rd = request.getRequestDispatcher("./PensionView/cpfImport/PensionTransferInOutReportDirect.jsp");
			}
			rd.forward(request, response);
			}catch(Exception e){
				log.printStackTrace(e);
			}
		}
		else if (request.getParameter("method") != null
				&& request.getParameter("method").equals("getAirports")) {
			PensionService ps1 = new PensionService();
			ArrayList ServiceType = null;
			String region = (String) request.getParameter("region");
			String username = session.getAttribute("userid").toString();
			String finalusername = username.substring(username.length() - 3,
					username.length());
			boolean value = CommonUtil.checkIfNumber(finalusername);
			log.info(finalusername);
			if(finalusername.trim().equals("FIN")){
				value=true;
			}if (value == true) {				
				ServiceType = ps1.getImportUserAirports(region, username);
			} else {				
				ServiceType = ps1.getImportUserAirports(region, "");
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
		}
	}
	public EmployeeValidateInfo getFinancialLoadEditDetails(String cpfaccno,String unitCode,String effectiveDate){
		String finSelectedDt="";
		EmployeeValidateInfo financialLodBean=new EmployeeValidateInfo();
		
		financialLodBean=financialDAO.getPensionValidateDetails(cpfaccno,unitCode,effectiveDate);
		return financialLodBean;
		
	}
	public int updateFinancialDetails(String cpfaccno,String unitCode,String valPF,String valPension,String valTotal,String pfStatury,String pfDedcution,String userName,String effectiveDate){
		int updateVal=0;
		updateVal=financialDAO.updatePensionValidateDetails(cpfaccno,unitCode,valPF,valPension,valTotal,pfStatury,pfDedcution,userName,effectiveDate);
		return updateVal;
		
	}
		
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException{
		this.doPost(request,response);
	}
	
	
	
	

}
