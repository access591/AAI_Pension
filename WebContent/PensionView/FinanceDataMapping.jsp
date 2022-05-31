<!--
/*
  * File       : FinanceDataMapping.jsp
  * Date       : 29/01/2010
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->

<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page
	import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.PensionBean,aims.bean.EmpMasterBean"%>
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
	String empNameChecked="",unmappedFlag="";
	boolean empNameChecked1=false;
	if(request.getAttribute("empNameChecked")!=null){
	  empNameChecked=request.getAttribute("empNameChecked").toString();
	  empNameChecked1 = Boolean.getBoolean(empNameChecked);
	 	
	}
	if(request.getAttribute("unmappedFlag")!=null){
		unmappedFlag=request.getAttribute("unmappedFlag").toString();
		
	}
	System.out.println("UnmappedFlag"+unmappedFlag);
	
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
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
<script type="text/javascript"> 
		function redirectPageNav(navButton,index,totalValue){   
			 var empNameCheak="";
	 	 if(window.document.forms[0].empNameChecked.checked==true){
		 	empNameCheak=true;
	 		 }
	 		 else {
	 		 empNameCheak=false;
	 		 }   
			document.forms[0].action="<%=basePath%>psearch?method=mappingNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&empNameCheak="+empNameCheak;
			document.forms[0].method="post";
			document.forms[0].submit();
		}
		
		function submit_Pension(cpfaccno,employeeno,region,empserialno){
			
			document.forms[0].action="<%=basePath%>psearch?method=financePfidUpdate&cpfacno="+cpfaccno+"&empCode="+employeeno+"&region="+region+"&pfid="+empserialno;
			document.forms[0].method="post";
			document.forms[0].submit();
			
		}
		 function editPensionMaster(cpfaccno,employeeName,employeeCode,region,airportCode){
				var flag="true";
				document.forms[0].action="<%=basePath%>psearch?method=personalEdit&cpfacno="+cpfaccno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode;
				document.forms[0].method="post";
				document.forms[0].submit();
		}
		function validateForm(empserialNO,cpfaccno,region,claimsProcess) {
			if(claimsProcess=="Y"){
		        alert("PensionClaim Process Already Done, User doesn't have Permissions to View/Edit TransactionData");
		        return false;
		        }
			var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="";
			
			var swidth=650;
			var sheight=500;
			reportType="Html";
			yearID="NO-SELECT";
          // code added on 09/03/2010
			var page="PensionContributionScreen";
			
			var mappingFlag="true";
			var pfidStrip='1 - 1'; 
			var params = "&frm_region="+region+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_reportType="+reportType+"&cpfAccno="+cpfaccno+"&mappingFlag="+mappingFlag+"&frm_pfids="+pfidStrip+"&empserialNO="+empserialNO+"&page="+page;
			
			var url="<%=basePath%>reportservlet?method=getReportPenContr"+params;
				if(reportType=='html' || reportType=='Html'){
		   	 			 LoadWindow(url);
	   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
	   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
								winOpened = true;
								wind1.window.focus();
	   	 			}
			
		}
		function LoadWindow(params){
		    var newParams =params;
			winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
			winOpened = true;
			winHandle.window.focus();
		   }
	 function Search(){
	   var empNameCheak="",unmappedFlag="";
 		 if(window.document.forms[0].empNameChecked.checked==true){
 		  empNameCheak=window.document.forms[0].empNameChecked.checked;
 		 }
 		 else {
 		 empNameCheak=false;
 		 }
 		 if(window.document.forms[0].unmappedRecords.checked==true){
 			unmappedFlag=window.document.forms[0].unmappedRecords.checked;
 	 		 }
 	 		 else {
 	 		unmappedFlag=false;
 	 		 }
	 		 var allRecordsFlag="";
 		/*if(window.document.forms[0].allRecords.checked==true){
 			allRecordsFlag=window.document.forms[0].allRecords.checked;
 	 		 }
 	 		 else {
 	 			allRecordsFlag=false;
 	 		 }*/
 		
       	document.forms[0].action="<%=basePath%>psearch?method=financeDataSearch&empNameCheak="+empNameCheak+"&unmappedFlag="+unmappedFlag+"&allRecordsFlag="+allRecordsFlag;
        document.forms[0].method="post";
		document.forms[0].submit();
   		 }

	 function updateStatus(){
		   var answer =confirm('Are you sure, do you want lock this records');
		   if(answer){
		  	document.forms[0].action="<%=basePath%>psearch?method=updateMappingFlag";
		   	document.forms[0].method="post";
			document.forms[0].submit();
		   }
	 }
	 function mappringRecords(){
		 var answer =confirm('Are you sure, do you want Map this records');
		   if(answer){
		  	document.forms[0].action="<%=basePath%>psearch?method=mappingUpdate";
		   	document.forms[0].method="post";
		   	alert("You don't have provision's to Map the record");
		 	return false;
			//document.forms[0].submit();
		   }
	 }
	
	 function callReport(){
		 var empNameCheak="",unmappedFlag="";
 		 if(window.document.forms[0].empNameChecked.checked==true){
 		  empNameCheak=window.document.forms[0].empNameChecked.checked;
 		 } 		
 		 else {
 		 empNameCheak=false;
 		 }
 		 var employeeName=window.document.forms[0].employeeName.value;
 		 var region=window.document.forms[0].region.value;
 		 if(window.document.forms[0].unmappedRecords.checked==true){
 			unmappedFlag=window.document.forms[0].unmappedRecords.checked;
 	 		 }
 	 		 else {
 	 		unmappedFlag=false;
 	 		 }
 		    var swidth=screen.Width-10;
 			var sheight=screen.Height-150;
	   		var reportID="",sortColumn="EMPLOYEENAME";
	   	 	reportID=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
	   	 var url="<%=basePath%>psearch?method=financeDataSearch&empNameCheak="+empNameCheak+"&unmappedFlag="+unmappedFlag+"&reportType="+reportID+"&employeeName="+employeeName+"&region="+region;
	  	   	 //	var url="<%=basePath%>psearch?method=personalEmpReport&reportType="+reportID+"&frm_sortcolumn="+sortColumn;
	   	 	wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
	   	 }
	 var xmlHttp;
	 function getNodeValue(obj,tag){
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	   	}
		function createXMLHttpRequest(){
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		 	}else if (window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
			 }
		}
	function getAirports(){	
		var regionID;
		regionID=document.forms[0].region.options[document.forms[0].region.selectedIndex].text;
		createXMLHttpRequest();	
		var url ="<%=basePath%>reportservlet?method=getAirports&region="+regionID;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getAirportsList;
		xmlHttp.send(null);
 }
	function getAirportsList(){
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 	
		}
		if(xmlHttp.readyState ==4){
			if(xmlHttp.status == 200){ 
				var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
				
				if(stype.length==0){
				 	var obj1 = document.getElementById("select_airport");
				  	obj1.options.length=0; 
		  			obj1.options[obj1.options.length]=new Option('[Select One]','','true');
				}else{
		 		   	var obj1 = document.getElementById("select_airport");
		 		  	obj1.options.length = 0;
				 	for(i=0;i<stype.length;i++){
		  				if(i==0){
							obj1.options[obj1.options.length]=new Option('[Select One]','','true');
						}
						obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
					}
		  		}
			}
		}
	}
  	</script>
</head>
<body class="BodyBackground">
<form method="post"
	action="<%=basePath%>psearch?method=financeDataSearch">

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>

		<td height="5%" colspan="2" align="center" class="ScreenHeading">
		Finance Data Mapping</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<%boolean flag = false;%>
	<tr>
		<td>
		<table align="center">

			<tr>
				<td class="label">Employee No. :</td>
				<td><input type="text" name="employeeCode"></td>
				<td class="label">CPFAcno:</td>
				<td><input type="text" name="cpfaccno"></td>

				<!-- 	<td class="label">
									AirportCode:
								</td> -->
				<td><input type="hidden" name="airPortCD"></td>
			</tr>


			<tr>
				<%    EmpMasterBean empSerach = new EmpMasterBean();
                     if(request.getAttribute("empSerach")!=null){
				           empSerach = (EmpMasterBean) request.getAttribute("empSerach"); } %>
				<td class="label">EmployeeName</td>
				<td><input type="text" name="employeeName"
					value='<%=empSerach.getEmpName()%>'>&nbsp; <input
					name="empNameChecked" type="checkbox"
					<%=empNameChecked.equals("true")?"checked":"unchecked" %> /></td>

				<td class="label">Region:</td>
				<td><SELECT NAME="region" style="width: 130px"
					onChange="javascript:getAirports()">
					<option value="">[Select One]</option>

					<%int j = 0;
			boolean exist = false;
			while (it.hasNext()) {
				region = hashmap.get(it.next()).toString();
				j++;
				if (region.equalsIgnoreCase(empSerach.getRegion()))
					exist = true;
				if (region.equalsIgnoreCase(empSerach.getRegion())) {

					%>
					<option value="<%=empSerach.getRegion()%>"
						<% out.println("selected");%>><%=empSerach.getRegion()%>
					</option>


					<%} else {%>
					<option value="<%=region%>"><%=region%></option>

					<%}

			%>

					<%}

			%>

				</SELECT></td>
			</tr>
			<tr>
				<td class="label">PFID</td>
				<td><input type="text" name="pfid"></input></td>
				<td class="label">Airport Code:</td>
				<td><select name="select_airport">
					<option value="">[Select One]</option>
				</select></td>
			</tr>
			<tr>
				<td class="label">UnMapped Records List
			</tr>
			<td><input type="checkbox" name="unmappedRecords"
				<%=unmappedFlag.equals("true")?"checked":"unchecked" %> /></td>
			
			<!-- 	<td class="label">All Record List
			</tr>
			<td><input type="checkbox" name="allRecords"
				<%=unmappedFlag.equals("true")?"checked":"unchecked" %> /></td> -->
			</tr>
			<tr>
				<td colspan="1">&nbsp;&nbsp;&nbsp;&nbsp;</td>

				<td><input type="button" class="btn" value="Search" class="btn"
					onclick="Search();"> <input type="button" class="btn"
					value="Reset" onclick="javascript:document.forms[0].reset()"
					class="btn"> <input type="button" class="btn"
					value="Cancel" onclick="javascript:history.back(-1)" class="btn">
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>

		<%
						FinacialDataBean dbBeans = new FinacialDataBean();
						SearchInfo getSearchInfo = new SearchInfo();
			
			if (request.getAttribute("financeDatalist") != null) {
				 int totalData = 0,index=0,totalUnmappedRecords=0;
				 String empNameCheck="",claimsprocess="";
				SearchInfo searchBean = new SearchInfo();
			//	EmpMasterBean empSerach = new EmpMasterBean();
				empSerach = (EmpMasterBean) request.getAttribute("empSerach");
				PensionBean  pensionBean= new PensionBean();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dataList = (ArrayList) request.getAttribute("financeDatalist");
				
				
				session.setAttribute("getSearchBean1", empSerach);
				empNameCheck=empSerach.getEmpNameCheak();
				
				System.out.println("empNameCheck "+empNameCheck);
				totalData = searchBean.getTotalRecords();
				totalUnmappedRecords=searchBean.getTotalUnmappedRecords();
			    bottomGrid = searchBean.getBottomGrid();
	            index = searchBean.getStartIndex();
				region=(String)request.getAttribute("region");
			   	if (dataList.size() == 0) {

				%>
		
	<tr>

		<td>
		<table align="center" id="norec">
			<tr>
				<br>
				<td><b> No Records Found </b></td>
			</tr>
		</table>
		</td>
	</tr>

	<%} else if (dataList.size() != 0) {
				 System.out.println("Size===After========="+dataList.size());
				%>
	<tr>
		<td>
		<table align="center">
			<tr>
				<td colspan="3"></td>
				<td colspan="2" align="right"><input type="button" alt="first"
					value="|<" name=" First" disable=true
					onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusFirst()%>> <input type="button"
					alt="pre" value="<" name="
					Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusPrevious()%>> <input type="button"
					alt="next" value=">" name="Next"
					onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusNext()%>> <input type="button"
					value=">|" name="Last"
					onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusLast()%>> <SELECT
					NAME="select_reportType" style="width: 110px"
					onChange="javascript:callReport()">
					<option value="">[Select Report]</option>
					<option value="html">Html</option>
					<option value="ExcelSheet">Excel Sheet</option>
				</SELECT></td>
			</tr>
		</table>
	</tr>
	<tr>

		<td>
		<table align="center" width="98%">
			<tr>
				<td colspan="3"></td>
				<td colspan="2" align="right"></td>
			</tr>
		</table>
	</tr>
	<tr>
		<td>

		<table width="95%" align="center" cellpadding=2 class="tbborder"
			cellspacing="0" border="0">

			<tr class="tbheader">
				<td class="tblabel">PFID&nbsp;&nbsp;</td>
				<td class="tblabel">CPFAC.No&nbsp;&nbsp;</td>
				<td class="tblabel">EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">EmployeeNo&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">Designation&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">DateofBirth&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">DateofJoining&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">Airport Code &nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">Region&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel">Total No of Months&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
				<td class="tblabel">PFReport&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tblabel"><input type="button" name="L" value="L"
					onClick="updateStatus(); "></td>
				<td><img src="./PensionView/images/page_edit.png"
					alt="Edit PFID" border="0" onClick="mappringRecords();" />&nbsp;</td>


				<td class="tblabel"></td>


				<%int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
					count++;
					PensionBean beans = (PensionBean) dataList.get(i);%>
			<% if(beans.getSeperationReason().equals("Death") || beans.getSeperationReason().equals("Retirement")) {%>
			 <tr bgcolor="yellow"  title='<%=beans.getSeperationReason()%>'>
				<td class="Data">
				<%if(beans.getPensionnumber().equals("")){%> <input type="text"
					name="empserialNO<%=i%>" size="10"
					value='<%=beans.getPensionnumber()%>'> <%}else{ %> <input
					type="text" name="empserialNO<%=i%>" readonly="true" size="10"
					value='<%=beans.getPensionnumber()%>'> <%} %>
				</td>
				<td class="Data"><%=beans.getCpfAcNo()%></td>
				<td class="Data"><%=beans.getEmployeeName()%> &nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getEmployeeCode()%> &nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getDesegnation()%></td>
				<td class="Data"><%=beans.getDateofBirth()%></td>
				<td class="Data"><%=beans.getDateofJoining()%></td>
				<td class="Data"><%=beans.getAirportCode()%></td>
				<td class="Data"><%=beans.getRegion()%></td>
				<td class="Data"><%=beans.getTotalRecrods()%></td>
				<td class="Data"><a href="#"
					onClick="validateForm('<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>')"><img
					src="./PensionView/images/viewDetails.gif" border="0"
					alt="PFReport"></a></td>
				<td><input type="checkbox" name="mappingflag"
					value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>'"></input></td>
				<td class="Data"><input type="checkbox" name="mappingUpdate"
					value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>','empserialNO<%=i%>'"></input>
				</td>
				<td class="Data"><a href="#"> </a></td>

			</tr> <%} else{
			%><tr><td class="Data">
				<%if(beans.getPensionnumber().equals("")){%> <input type="text"
					name="empserialNO<%=i%>" size="10"
					value='<%=beans.getPensionnumber()%>'> <%}else{ %> <input
					type="text" name="empserialNO<%=i%>" readonly="true" size="10"
					value='<%=beans.getPensionnumber()%>'> <%} %>
				</td>
				<td class="Data"><%=beans.getCpfAcNo()%></td>
				<td class="Data"><%=beans.getEmployeeName()%> &nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getEmployeeCode()%> &nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getDesegnation()%></td>
				<td class="Data"><%=beans.getDateofBirth()%></td>
				<td class="Data"><%=beans.getDateofJoining()%></td>
				<td class="Data"><%=beans.getAirportCode()%></td>
				<td class="Data"><%=beans.getRegion()%></td>
				<td class="Data"><%=beans.getTotalRecrods()%></td>
				<td class="Data"><a href="#"
					onClick="validateForm('<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>','<%=beans.getClaimsprocess()%>')"><img
					src="./PensionView/images/viewDetails.gif" border="0"
					alt="PFReport"></a></td>
				<td><input type="checkbox" name="mappingflag"
					value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>'"></input></td>
				<td class="Data"><input type="checkbox" name="mappingUpdate"
					value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>','empserialNO<%=i%>'"></input>
				</td>
				<td class="Data"><a href="#"> </a></td>

			</tr>
			<%}}%>
			<td class="Data" colspan="3"><font color="red"><%=index%></font>
			&nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
			&nbsp;&nbsp; &nbsp;&nbsp;</td>

			<%if (dataList.size() != 0) {%>
			<tr>
				<td></td>
			</tr>
			<%}}}
			    %>

		</table>
		</td>
	</tr>
</table>

</form>
</body>
</html>
