<!--
/*
  * File       : PensionAdd.jsp
  * Date       : 12/12/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->
<%@ page language="java" import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean"%>
<%@ page import="aims.dao.PensionDAO"%>
<%@ page import="aims.service.PensionService"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="javax.servlet.RequestDispatcher" %>
<%@ page import="aims.common.CommonUtil" %>
<jsp:useBean id="bean" class="aims.bean.EmpMasterBean" scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
	
		<%int result = 0;
			CommonUtil commonUtil=new CommonUtil();
			if (request.getParameter("empName") != null) {
				out.println(request.getParameter("empName"));
				bean.setEmpName(request.getParameter("empName").toString());
			} else {
				bean.setEmpName("");
			}
			if (request.getParameter("airPortCode") != null) {
				out.println(request.getParameter("airPortCode"));
				bean.setStation(request.getParameter("airPortCode").toString());
			} else {
				bean.setStation("");
			}
			if (request.getParameter("desegnation") != null) {
				out.println(request.getParameter("desegnation"));
				bean.setDesegnation(request.getParameter("desegnation")
						.toString());
			} else {
				bean.setDesegnation("");
			}
			if (request.getParameter("cpfacno") != null) {
				out.println(request.getParameter("cpfacno"));
				bean.setCpfAcNo(request.getParameter("cpfacno").toString());
			} else {
				bean.setCpfAcNo("");
			}
			if (request.getParameter("employeeCode") != null) {
				out.println(request.getParameter("employeeCode"));
				bean.setEmpNumber(request.getParameter("employeeCode")
						.toString());
			} else {
				bean.setEmpNumber("");
			}

			if (request.getParameter("airportSerialNumber") != null) {
				out.println(request.getParameter("airportSerialNumber"));
				bean.setAirportSerialNumber(request.getParameter("empName")
						.toString());
			} else {
				bean.setAirportSerialNumber("");
			}
			if (request.getParameter("emplevel") != null) {
				out.println(request.getParameter("emplevel"));
				bean.setEmpLevel(request.getParameter("emplevel").toString());
			} else {
				bean.setEmpLevel("");
			}
			if (request.getParameter("dateofbirth") != null) {
			String dateofBirth=request.getParameter("dateofbirth") ;
             System.out.println("dateofBirth"+dateofBirth);		
			   bean.setDateofBirth(commonUtil.converDBToAppFormat(
						request.getParameter("dateofbirth").toString(), "dd/MM/yyyy", "dd-MMM-yyyy"));
			
			
			}
			if (request.getParameter("dateofjoining") != null) {
				out.println(request.getParameter("dateofjoining"));
			   bean.setDateofJoining(commonUtil.converDBToAppFormat(
					request.getParameter("dateofjoining").toString(), "dd/MM/yyyy", "dd-MMM-yyyy"));
				
			} else {
				bean.setDateofJoining("");
			}
			if (request.getParameter("reason") != null) {
				out.println(request.getParameter("reason"));
				bean.setSeperationReason(request.getParameter("reason")
						.toString());
			} else {
				bean.setSeperationReason("");
			}

			if (request.getParameter("seperationDate") != null) {
				out.println(request.getParameter("seperationDate"));
				bean.setDateofSeperationDate(request.getParameter(
						"seperationDate").toString());
			} else {
				bean.setSeperationReason("");
			}

			if (request.getParameter("wetherOptionA") != null) {
				System.out.println(request.getParameter("wetherOptionA".trim()));
				bean.setWhetherOptionA(request.getParameter("wetherOptionA".trim())
						.toString());
			} else {
				bean.setWhetherOptionA("");
			}

			if (request.getParameter("wetherOptionNo") != null) {
				System.out.println(request.getParameter("wetherOptionNo"));
				bean.setWhetherOptionNO(request.getParameter("wetherOptionNo")
						.toString().trim());
			} else {
				bean.setWhetherOptionNO("");
			}
			if (request.getParameter("wetherOptionB") != null) {
				System.out.println(request.getParameter("wetherOptionB"));
				bean.setWhetherOptionB(request.getParameter("wetherOptionB")
						.toString().trim());
			} else {
				bean.setWhetherOptionB("");
			}

			if (request.getParameter("form1") != null) {
				out.println(request.getParameter("form1"));
				bean.setForm2Nomination(request.getParameter("form1")
						.toString().trim());
			} else {
				bean.setForm2Nomination("");
			}

			if (request.getParameter("remarks") != null) {
				out.println(request.getParameter("remarks"));
				bean.setRemarks(request.getParameter("remarks").toString().trim());
			} else {
				bean.setRemarks("");
			}

			if (request.getParameter("paddress") != null) {
				bean.setPermanentAddress(request.getParameter("paddress").toString().trim());
			} else {
				bean.setPermanentAddress("");
			}
			if (request.getParameter("taddress") != null) {
				bean.setTemporatyAddress(request.getParameter("taddress").toString().trim());
			} else {
				bean.setTemporatyAddress("");
			}
			if (request.getParameter("wetherOption") != null) {
				bean.setWetherOption(request.getParameter("wetherOption").toString().trim());
			} else {
				bean.setWetherOption("");
			}
			
			if (request.getParameter("sex") != null) {
				bean.setSex(request.getParameter("sex").toString().trim());
			} else {
				bean.setSex("");
			}
			if (request.getParameter("mstatus") != null) {
				bean.setMaritalStatus(request.getParameter("mstatus").toString().trim());
			} else {
				bean.setMaritalStatus("");
			}
			if (request.getParameter("fhname") != null) {
				bean.setFhName(request.getParameter("fhname").toString().trim());
			} else {
				bean.setFhName("");
			}
			if (request.getParameter("FName") != null) {
				bean.setFMemberName(request.getParameter("FName").toString().trim());
			} else {
				bean.setFMemberName("");
			}
			if (request.getParameter("Fdob") != null) {
			  bean.setFDateofBirth(commonUtil.converDBToAppFormat(
						request.getParameter("Fdob").toString(), "dd/MM/yyyy", "dd-MMM-yyyy"));
			
				//bean.setFDateofBirth(request.getParameter("Fdob").toString().trim());
				//System.out.println(request.getParameter("Fdob").toString());
			} else {
				bean.setFDateofBirth("");
			}
			if (request.getParameter("Frelation") != null) {
				bean.setFrelation(request.getParameter("Frelation").toString().trim());
			} else {
				bean.setFrelation("");
			}
			
			if (request.getParameter("Nname") != null) {
				bean.setNomineeName(request.getParameter("Nname").toString().trim());
			} else {
				bean.setNomineeName("");
			}
			if (request.getParameter("Naddress") != null) {
				bean.setNomineeAddress(request.getParameter("Naddress").toString().trim());
			} else {
				bean.setNomineeAddress("");
			}
			if (request.getParameter("Ndob") != null) {
			 bean.setNomineeDob(commonUtil.converDBToAppFormat(
						request.getParameter("Ndob").toString(), "dd/MM/yyyy", "dd-MMM-yyyy"));
			
				//bean.setNomineeDob(request.getParameter("Ndob").toString().trim());
			} else {
				bean.setNomineeDob("");
			}
			if (request.getParameter("Nrelation") != null) {
				bean.setNomineeRelation(request.getParameter("Nrelation").toString().trim());
			} else {
				bean.setNomineeRelation("");
			}
			if (request.getParameter("guardianname") != null) {
				bean.setNameofGuardian(request.getParameter("guardianname").toString().trim());
			} else {
				bean.setNameofGuardian("");
			}
			if (request.getParameter("totalshare") != null) {
				bean.setTotalShare(request.getParameter("totalshare").toString().trim());
			} else {
				bean.setTotalShare("");
			}
			
			if (request.getParameterValues("FName") != null) {
			String fname[]=request.getParameterValues("FName");
			
			if(request.getParameter("Other")!=null){
			bean.setOtherReason(request.getParameter("Other").toString().trim());
			}
			else bean.setOtherReason("");
			
			if(request.getParameter("division")!=null){
			bean.setDivision(request.getParameter("division").toString().trim());
			}
			else bean.setDivision("");
			if(request.getParameter("department")!=null){
			bean.setDepartment(request.getParameter("department").toString().trim());
			}
			else bean.setDepartment("");
			
			if(request.getParameter("emailId")!=null){
			bean.setEmailId(request.getParameter("emailId").toString().trim());
			}
			else bean.setEmailId("");
			
			if(request.getParameter("equalShare")!=null){
			bean.setEmpNomineeSharable(request.getParameter("equalShare").toString().trim());
			}
			else bean.setEmpNomineeSharable("");
			if(request.getParameter("region")!=null){
			bean.setRegion(request.getParameter("region").toString().trim());
			}
			else bean.setRegion("");
			
			
			
			String fdob[]=request.getParameterValues("Fdob");
			System.out.println("fdob.length"+fdob.length);
			String frelation[]=request.getParameterValues("Frelation");
			 StringBuffer familyRow = new StringBuffer();
			System.out.println(fname.length);
			 String fmDOB="",fRelation="";
			for(int i=0;i<fname.length;i++)
			 {
			    familyRow.append(fname[i].toString() + "@");
			    System.out.println("fdob"+fdob[i]);
			    if(fdob[i].equals("")){
			    fmDOB="XXX";
			    }else{
			    
			   fmDOB= commonUtil.converDBToAppFormat(
						fdob[i].toString(), "dd/MM/yyyy", "dd-MMM-yyyy");
			  
			    }
			    if(frelation[i].equals("")){
			    fRelation="XXX";
			    }else{
			    fRelation=frelation[i].toString();
			    }
			    
			    familyRow.append(fmDOB+ "@");
			    familyRow.append(fRelation);
			    familyRow.append("***");
			    System.out.println(fname[i].toString());
				System.out.println(fdob[i].toString());
				System.out.println(fRelation.toString());
				} 
				
			System.out.println("family data " +familyRow.toString());
			
			if (familyRow.toString() != null) {
				bean.setFamilyRow(familyRow.toString());
			} else {
				bean.setFamilyRow("");
			}
			
			}
			
			if (request.getParameterValues("Nname") != null) {
			String Nname[]=request.getParameterValues("Nname");
			String Naddress[]=request.getParameterValues("Naddress");
			String Ndob[]=request.getParameterValues("Ndob");
			String Nrelation[]=request.getParameterValues("Nrelation");
			String guardianname[]=request.getParameterValues("guardianname");
			String totalshare[]=request.getParameterValues("totalshare");
			System.out.println("totalshare "+totalshare.length);
			String gaddress[]=request.getParameterValues("gaddress");
			StringBuffer nomineeRow = new StringBuffer();
			System.out.println(Nname.length);
			String nAddress="",nDob="",nRelation="",nGuardianname="",nTotalshare="",gardaddressofNaminee="";
			for(int i=0;i<Nname.length;i++)
			 { 
			    nomineeRow.append(Nname[i].toString() + "@");
			    
			    
			   
			    if(Naddress[i].equals("")){
			    nAddress="XXX";
			    }else{
			    nAddress=Naddress[i].toString();
			    }
			    if(Nrelation[i].equals("")){
			    nRelation="XXX";
			    }else{
			    nRelation=Nrelation[i].toString();
			    }
			    
			     if(Ndob[i].equals("")){
			    nDob="XXX";
			    }else{
			    
			     nDob= commonUtil.converDBToAppFormat(
						Ndob[i].toString(), "dd/MM/yyyy", "dd-MMM-yyyy");
			  //  nDob=Ndob[i].toString();
			    }
			    if(guardianname[i].equals("")){
			    nGuardianname="XXX";
			    }else{
			    nGuardianname=guardianname[i].toString();
			    }
			    if(gaddress[i].equals("")){
			    gardaddressofNaminee="XXX";
			    }else{
			    gardaddressofNaminee=gaddress[i].toString();
			    }
			    
			    if(totalshare[i].equals("")){
			    nTotalshare="XXX";
			    }else{
			    nTotalshare=totalshare[i].toString();
			    }
			    
			    nomineeRow.append(nAddress + "@");
			    nomineeRow.append(nDob+"@");
			    nomineeRow.append(nRelation + "@");
			    nomineeRow.append(nGuardianname + "@");
			    nomineeRow.append(gardaddressofNaminee + "@");
			    nomineeRow.append(nTotalshare);
			    nomineeRow.append("***");
			   
			  
				} 
				
			System.out.println("Nominee data " +nomineeRow.toString());
			
			
			if (nomineeRow.toString() != null) {
				bean.setNomineeRow(nomineeRow.toString());
			} else {
				bean.setNomineeRow("");
			}
			
			}
			
			PensionService ps = new PensionService();
			boolean result1=ps.addPensionRecord(bean);		
	       if(result1){ 
	      response.sendRedirect("./PensionMasterSearch.jsp");
	      }else{
	       response.sendRedirect(basePath+"search1?method=getMaxCpfAccNo&recordExist=true");
	      }
	      %>


</html>

