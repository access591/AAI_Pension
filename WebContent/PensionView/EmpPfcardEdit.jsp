<!--
/*
  * File       : EmpPfcardEdit.jsp
  * Date       : 07/04/2010
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->

<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
					
    String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	

  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<script type="text/javascript"> 
		
	function openURL(sURL) { 
	//alert(sURL);
		window.open(sURL,"Window1","menubar=no,width='100%',height='100%',toolbar=no,fullscreen=yes");

	} 
		
	
	 function formSubmit(pfid){
		 if( pfid==""){
			 alert('Please Enter  PF ID ');
			 document.forms[0].pfid.focus();
			 return false;
			 }
		 if(document.forms[0].year.value==""){
			alert('User should select Year');
			document.forms[0].year.focus();
			return false;
		 }
		 var year=document.forms[0].year.value;
		 var region=document.forms[0].region.value;
		params = "&frm_region="+region+"&frm_year="+year+"&pfid="+pfid;
		url="<%=basePath%>reportservlet?method=cardEdit"+params;
	 //  url="<%=basePath%>reportservlet?method=cardReport"+params;
		document.forms[0].action="<%=basePath%>reportservlet?method=cardEdit"+params;
    	document.forms[0].method="post";
		document.forms[0].submit();
   		 }
		 

	 function Search(){
			
	   		var airportID="",sortColumn="EMPLOYEENAME";
			var cpfaccno=document.forms[0].cpfaccno.value;
			var pfid=document.forms[0].pfid.value;
	   		var regionID=document.forms[0].region.value;
	   		dob="",doj="",employeeNo="";
	   		var page="EmpPfcardEdit";
			document.forms[0].action="<%=basePath%>psearch?method=searchPersonal&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&cpfaccno="+cpfaccno+"&employeeNo="+employeeNo+"&pensionNO="+pfid+"&page="+page;
		  	document.forms[0].method="post";
			document.forms[0].submit();
		}

		 function updateMonthlyCpf(){
			 alert("in update ");
			var url="<%=basePath%>reportservlet?method=updateMonthlyCpf";
			alert(url);
			 document.forms[0].action=url;
			 document.forms[0].method="post";
			 document.forms[0].submit();
		 }
		 
   			
	</script>
	</head>
	<body class="BodyBackground">
		<form  >

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>

				<tr>

					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						Employee FinalSettlement 
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<%boolean flag = false;%>
				<tr>
					<td>
						<table align="center">
							<tr>
								<td class="label">
									PF ID :
								</td>
								<td>
									<input type="text" name="pfid">
								</td>
				<td class="label">
 						  Region:
						</td>
						<td>									
							<SELECT NAME="region" style="width:130px">
                            <option value=""> Select One</option>
							<%
							int j=0;
                            while(it.hasNext()){
							  region=hashmap.get(it.next()).toString();
							  j++;
							 %>
							  <option ="<%=j%>" ><%=region%></option>
	                       <% }
							%>
							</SELECT>
						</td>
							</tr>
							<tr>
								<td class="label">
									CPFAcno:
								</td>
								<td>
						<input type="text" name="cpfaccno">
								</td>
 						 <td class="label">Finacial Year</td>
								<td>									
							<SELECT NAME="year" style="width:130px">
                            <option value="2008">2008-09</option>
							<option value="2009">2009-10</option></SELECT></td>
														
							</tr>
							
							
							
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>

								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="Search();">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>

						<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%	EmployeePersonalInfo dbBeans = new EmployeePersonalInfo();
					SearchInfo getSearchInfo = new SearchInfo();
					int totalData = 0,index = 0;
					if (request.getAttribute("searchBean") != null) {
					SearchInfo searchBean = new SearchInfo();
					ArrayList dataList = new ArrayList();
					BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
					searchBean = (SearchInfo) request.getAttribute("searchBean");
					dbBeans = (EmployeePersonalInfo) request.getAttribute("searchInfo");
					index = searchBean.getStartIndex();
				//	out.println("index "+index);
					session.setAttribute("getSearchBean1", dbBeans);
					dataList = searchBean.getSearchList();
					totalData = searchBean.getTotalRecords();
					bottomGrid = searchBean.getBottomGrid();
					if (dataList.size()!= 0) {

				%>
				<tr>
					<td><table align="center">
						<tr>
						<td colspan="3"></td>
							<td colspan="2" align="right">
								<input type="button"  alt="first" value="|<" name=" First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
								<input type="button"  alt="pre" value="<" name=" Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
								<input type="button" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
								<input type="button"  value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								<SELECT NAME="select_reportType" style="width:110px" onChange="javascript:callReport()">
									<option value="" >[Select Report]</option>
									<option value="html">Html</option>
									<option value="ExcelSheet">Excel Sheet</option>
								</SELECT>
							</td>
						</tr>
						</table>
					  </td>
				</tr>
				<tr>
					<td height="25%">
						<table align="center" width="100%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
							<tr class="tbheader">
								<td class="tblabel">View</td>
								<td class="tblabel">PF ID&nbsp;&nbsp;</td>
								<td class="tblabel">Old <br/>CPFACC.No</td>
								<td class="tblabel">Employee Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
								<td class="tblabel">Designation</td>
								<td class="tblabel">D.O.B</td>
								<td class="tblabel">Pension Option&nbsp;</td>
								<td class="tblabel">Division&nbsp;</td>
								<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
								<td class="tblabel">Region</td>
                               	<td><img src="./PensionView/images/page_edit.png" alt="FinalSettlement Edit"  border="0" />&nbsp;</td>
                               	<td><img src="./PensionView/images/nominee4.gif" alt="Nominee Report"  border="0" /></td>
								<td><img src="./PensionView/images/nominee4.gif" alt="Form9 Report"  border="0" /></td>
								<td>&nbsp;&nbsp;&nbsp;</td>
								<td>&nbsp;&nbsp;&nbsp;</td>
							</tr>
							<%	int count = 0;
								for (int i = 0; i < dataList.size(); i++) {
									count++;
									EmployeePersonalInfo personal = (EmployeePersonalInfo) dataList.get(i);
							%>
							<tr>
								<td class="Data" width="5%">
								<a href="#" onClick="javascript:viewPersonalDetails('<%=personal.getOldPensionNo()%>','<%=personal.getCpfAccno()%>','<%=personal.getEmployeeName()%>','<%=personal.getEmployeeNumber()%>','<%=personal.getRegion()%>','<%=personal.getAirportCode()%>','<%=index%>','<%=totalData%>')"><img src="<%=basePath%>PensionView/images/viewDetails.gif" border="0" alt="Click to view the PersonalInfo"></a>
								</td>
								<td class="HighlightData"><%=personal.getPfID()%></td>
								<td class="Data" width="12%"><%=personal.getCpfAccno()%></td>
								<td class="Data"><%=personal.getEmployeeNumber()%></td>
								<td class="Data" width="12%"><%=personal.getEmployeeName()%></td>
								<td class="Data"><%=personal.getDesignation()%></td>
								<td class="Data" width="10%"><%=personal.getDateOfBirth()%></td>
								<td class="Data" width="5%"><%=personal.getWetherOption()%></td>
								<td class="Data"><%=personal.getDivision()%></td>
								<td class="Data"><%=personal.getAirportCode()%></td>
								<td class="Data" width="12%"><%=personal.getRegion()%></td>
							  <%if(session.getAttribute("userid").equals("NAGARAJ")|| session.getAttribute("userid").equals("navayuga")){%>
	                           <td>
									<a href='#' onclick="javascript:formSubmit('<%=personal.getOldPensionNo()%>')"><img src='./PensionView/images/page_edit.png' border='0' alt='Edit' > </a>
								</td>
                             <%}else{%>
								<td>
									<a href="javascript:alert('Permissions Denied to Edit PFID')"><img src='./PensionView/images/page_edit.png' border='0' alt='Edit' > </a>
								</td>
                                <%} %>
								<td>
									<a href='#' onClick="javascript:form2Report('<%=personal.getPensionNo()%>','<%=personal.getCpfAccno()%>','<%=personal.getEmployeeName()%>','<%=personal.getEmployeeNumber()%>','<%=personal.getRegion()%>','<%=personal.getAirportCode()%>','<%=personal.getDateOfBirth()%>')"><img src='./PensionView/images/nominee4.gif' border='0' alt='Nominee Report' > </a>
								</td>
								<td>
									<a href='#' onClick="javascript:form9Report('<%=personal.getPensionNo()%>','<%=personal.getCpfAccno()%>','<%=personal.getEmployeeName()%>','<%=personal.getEmployeeNumber()%>','<%=personal.getRegion()%>','<%=personal.getAirportCode()%>','<%=personal.getDateOfBirth()%>')"><img src='./PensionView/images/nominee4.gif' border='0' alt='Form-9 Report' > </a>
								</td>
								<td>
									
									<input type="hidden" name="cpfnostring<%=personal.getCpfAccno()%>" value="<%=personal.getCpfAccno()%>,<%=personal.getEmployeeName()%>,<%=personal.getEmployeeNumber()%>,<%=personal.getRegion()%>,<%=personal.getAirportCode()%>">
								</td>
							</tr>
						<%}%>
							<tr>

								<td class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
						<%}else if(dataList.size()==0){%>
						<tr>

					<td>
						<table align="center" id="norec">
							<tr>
								<br>
								<td>
									<b> No Records Found </b>
								</td>
							</tr>
						</table>
					</td>
				</tr>
						<%}}%>

	 
					
</table>

						</table>
					</td>
				</tr>
			</table>

		</form>
	</body>
</html>
