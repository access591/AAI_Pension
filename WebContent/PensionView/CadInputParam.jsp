<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page
	import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.PensionBean,aims.bean.EmpMasterBean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String region = "";
	CommonUtil common = new CommonUtil();
	HashMap hashmap = new HashMap();
	hashmap = common.getRegion();
	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	String empNameChecked = "", unmappedFlag = "";
	boolean empNameChecked1 = false;
	if (request.getAttribute("empNameChecked") != null) {
		empNameChecked = request.getAttribute("empNameChecked")
				.toString();
		empNameChecked1 = Boolean.getBoolean(empNameChecked);

	}
	if (request.getAttribute("unmappedFlag") != null) {
		unmappedFlag = request.getAttribute("unmappedFlag").toString();

	}
	String userId = session.getAttribute("userid").toString();
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
		<script type="text/javascript"><!-- 
		function redirectPageNav(navButton,index,totalValue){   
			 var empNameCheak="";
	 	 if(window.document.forms[0].empNameChecked.checked==true){
		 	empNameCheak=true;
	 		 }
	 		 else {
	 		 empNameCheak=false;
	 		 }   
	 	 var allRecordsFlag="true";
			document.forms[0].action="<%=basePath%>CadServlet?method=mappingNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&empNameCheak="+empNameCheak+"&allRecordsFlag="+allRecordsFlag;
           	document.forms[0].method="post";
			document.forms[0].submit();
		}
		
	function cadAllYearReport(empserialNO) {
			
		var params = "&frm_region=NO-SELECT&frm_year=NO-SELECT&frm_month=NO-SELECT&frm_reportType=Html&frm_emp_flag=true&frm_reportType=Html&frm_formType=FORM-7REVISION OPTION&frm_pensionno="+empserialNO+"&frm_pfids=NO-SELECT&frm_dob=20-Dec-1957&adjFlag=false&frmName=&pcFlag=true";			
		// alert(" params "+params);	
		var url="<%=basePath%>CadServlet?method=cadAllYearReport"+params;
		
		LoadWindow(url);
			// alert(" pc report "+url );
			//	document.forms[0].action=url;
		//	document.forms[0].method="post";
		//	document.forms[0].submit();		
		}
		
				
		function validateForm(empserialNO,cpfaccno,region) {
			
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
			
			var url="<%=basePath%>CadServlet?method=getReportPenContr"+params;
			// alert(" pc report "+url );
				document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();
			
		}
			 function getReport(){
				var allRecordsFlag="true";
	 		    var swidth=screen.Width-10;
	 			var sheight=screen.Height-150;
		   		var reportID="",sortColumn="EMPLOYEENAME";
		   	 	reportID=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		  		var url="<%=basePath%>CadServlet?method=financeDataReport&reportType="+reportID+"&allRecordsFlag="+allRecordsFlag;
		   	 	wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
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
 		 var pfidfrom=document.forms[0].pfidfrom.value;
 		 var pcverified=document.forms[0].pcverified.value;
 		 var allRecordsFlag="true";
 		 var url="<%=basePath%>CadServlet?method=financeDataSearch&empNameCheak="+empNameCheak+"&unmappedFlag="+unmappedFlag+"&allRecordsFlag="+allRecordsFlag+"&pfidfrom="+pfidfrom+"&pcverified="+pcverified;
 		 // alert(" url======= "+url);
 		
 		document.forms[0].action=url;
     
        document.forms[0].method="post";
		document.forms[0].submit();
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
	   	 var url="<%=basePath%>CadServlet?method=financeDataSearch&empNameCheak="+empNameCheak+"&unmappedFlag="+unmappedFlag+"&reportType="+reportID+"&employeeName="+employeeName+"&region="+region;
	  	   	 //	var url="<%=basePath%>CadServlet?method=personalEmpReport&reportType="+reportID+"&frm_sortcolumn="+sortColumn;
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
		var url ="<%=basePath%>CadServlet?method=getAirports&region="+regionID;
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
	function updateStatus(status,pensionno){
	  var user='<%=userId%>';
		 if(user!="SEHGAL" && user!="NIMESH"){
          alert("User "+user+" Doesn't have the Permission to change the PensionClaims status");
          return false;
		 }
		 var answer="";
		if(status=="N"){
		   answer =confirm('Are you sure, do you want Lock This Record from Form-6-7-8');
		}else{
		   answer =confirm('Are you sure, do you want Unlock This Record from Form-6-7-8');
		}
		   if(answer){
			   var formsstatus=status;
		var url="<%=basePath%>CadServlet?method=formsDisable&formsstatus="+formsstatus+"&pensionno="+pensionno;
		 createXMLHttpRequest();	
	   	 	xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = updateStatus1;
			xmlHttp.send(null);
		   }
	 }
	 function updateStatus1()
		{
		if(xmlHttp.readyState ==4)
		{
		 alert(xmlHttp.responseText);
			}
		}
	 function pcreportverified(status,pensionno){
      var user='<%=userId%>';
		 if(user!="SEHGAL" && user!="NIMESH"){
          alert("User "+user+" Doesn't have the Permission to change the PensionClaims status");
          return false;
		 }
		 var answer="";
		if(status=="N"){
		   answer =confirm('Are you sure, do you want to Change The Status of PCReport Verified as No');
		}else{
		   answer =confirm('Are you sure, do you want to Change The Status of PCReport Verified as Yes');
		}
		   if(answer){
			   var pcreportstatus=status;
		var url="<%=basePath%>CadServlet?method=formsDisable&pcreportstatus="+pcreportstatus+"&pensionno="+pensionno;
		// alert(" report url "+url);
		 createXMLHttpRequest();	
	   	 	xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = pcreportverified1;
			xmlHttp.send(null);
		   }
	 }
	 function pcreportverified1()
		{
		if(xmlHttp.readyState ==4)
		{
		 alert(xmlHttp.responseText);
			}
		}

	 function pensionClaims(status,pensionno){
	 
		 var user='<%=userId%>';
		 if(user!="SEHGAL" && user!="NIMESH"){
          alert("User "+user+" Doesn't have the Permission to change the PensionClaims status");
          return false;
		 }
		 var answer="";
		if(status=="Y"){
		   answer =confirm('Are you sure, do you want to Change The Status of PensionClaims Process as Yes');
		}else{
		   answer =confirm('Are you sure, do you want to Change The Status of PensionClaims Process as No');
		}
		   if(answer){
			   var pensionclaimstatus=status;
		var url="<%=basePath%>CadServlet?method=formsDisable&pensionclaimstatus="+pensionclaimstatus+"&pensionno="+pensionno;
		  createXMLHttpRequest();	
	   	 	xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = pensionclaims;
			xmlHttp.send(null);
		   }
	 }
	 function pensionclaims()
		{
		if(xmlHttp.readyState ==4)
		{
		 alert(xmlHttp.responseText);
			}
		}
  	--></script>
	</head>
	<body class="BodyBackground">
		<form method="post"
			action="<%=basePath%>CadServlet?method=financeDataSearch">

			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>

				<tr>

					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						Cad Input Param
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<%
					boolean flag = false;
				%>
				<tr>
					<td>
						<table align="center">

							<tr>
								<td class="label" align="right">
									Employee No:
								</td>
								<td>
									<input type="text" name="employeeCode">
								</td>
								<td class="label" align="right">
									CPFACC No:
								</td>
								<td>
									<input type="text" name="cpfaccno">
								</td>

								<!-- 	<td class="label">
									AirportCode:
								</td> -->
								<td>
									<input type="hidden" name="airPortCD">
								</td>
							</tr>


							<tr>
								<%
									EmpMasterBean empSerach = new EmpMasterBean();
									if (request.getAttribute("empSerach") != null) {
										empSerach = (EmpMasterBean) request.getAttribute("empSerach");
									}
								%>
								<td class="label" align="right">
									Employee Name:
								</td>
								<td>
									<input type="text" name="employeeName"
										value='<%=empSerach.getEmpName()%>'>
									&nbsp;
									
									<input name="empNameChecked" type="checkbox"
										<%=empNameChecked.equals("true")
					? "checked"
					: "unchecked"%> />
								</td>

								<td class="label" align="right">
									Region&nbsp;:
								</td>
								<td>
									<SELECT NAME="region" style="width: 130px"
										onChange="javascript:getAirports()">
										<option value="">
											[Select One]
										</option>

										<%
											int j = 0;
											boolean exist = false;
											while (it.hasNext()) {
												region = hashmap.get(it.next()).toString();
												j++;
												if (region.equalsIgnoreCase(empSerach.getRegion()))
													exist = true;
												if (region.equalsIgnoreCase(empSerach.getRegion())) {
										%>
										<option value="<%=empSerach.getRegion()%>"
											<%out.println("selected");%>><%=empSerach.getRegion()%>
										</option>


										<%
											} else {
										%>
										<option value="<%=region%>"><%=region%></option>

										<%
											}
										%>

										<%
											}
										%>

									</SELECT>
								</td>
							</tr>
							<tr>
								<td class="label" align="right">
									PFID:
								</td>
								<td>
									<input type="text" name="pfid"></input>
								</td>
								<td class="label" align="right">
									Airport Code:
								</td>
								<td>
									<select name="select_airport" style="width: 130px">
										<option value="">
											[Select One]
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="label" align="right">
									PFID From:
								</td>
								<td>
									<input type="text" name="pfidfrom"></input>
								</td>
								<td class="label" align="right">
									PC Report Verified:
								</td>
								<td>
									<select name="pcverified" style="width: 130px">
										<option value="">
											[Select One]
										</option>
										<option value="Y">
											Yes
										</option>
										<option value="N">
											No
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<!-- 	<td class="label">UnMapped Records List
			</tr>
			 <td><input type="checkbox" name="unmappedRecords"
				<%=unmappedFlag.equals("true")
					? "checked"
					: "unchecked"%> /></td>-->
							</tr>
							<!--  <td><input type="checkbox" name="allRecords"
				<%=unmappedFlag.equals("true")
					? "checked"
					: "unchecked"%> /></td>-->
							</tr>
							<tr>
								<td colspan="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>

								<td>
									<input type="button" class="btn" value="Search" class="btn"
										onclick="Search();">
									<input type="button" class="btn" value="Reset"
										onclick="javascript:document.forms[0
	].reset();
				<input type="button" class="btn" value="Cancel"
										onclick="javascript:history.back(-1)" class="btn">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>

						<%FinacialDataBean dbBeans = new FinacialDataBean();
			SearchInfo getSearchInfo = new SearchInfo();

			if (request.getAttribute("financeDatalist") != null) {
				int totalData = 0, index = 0, totalUnmappedRecords = 0;
				String empNameCheck = "";
				SearchInfo searchBean = new SearchInfo();
				//	EmpMasterBean empSerach = new EmpMasterBean();
				empSerach = (EmpMasterBean) request.getAttribute("empSerach");
				PensionBean pensionBean = new PensionBean();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dataList = (ArrayList) request.getAttribute("financeDatalist");

				session.setAttribute("getSearchBean1", empSerach);
				empNameCheck = empSerach.getEmpNameCheak();

				System.out.println("empNameCheck " + empNameCheck);
				totalData = searchBean.getTotalRecords();
				totalUnmappedRecords = searchBean.getTotalUnmappedRecords();
				bottomGrid = searchBean.getBottomGrid();
				index = searchBean.getStartIndex();
				region = (String) request.getAttribute("region");
				if (dataList.size() == 0) {%>

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

						<%} else if (dataList.size() != 0) {
					System.out.println("Size===After========="
							+ dataList.size());%>
						<tr>
							<td>
								<table align="center">
									<tr>
										<td colspan="3"></td>
										<td colspan="2" align="right">
											<input type="button" alt="first" value="|<" name="
												First" disable=true
												onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')"
												<%=bottomGrid.getStatusFirst()%>>
												<input type="button" alt="pre" value="<" name="
													Pre" onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')"
													<%=bottomGrid.getStatusPrevious()%>>
													<input type="button" alt="next" value=">" name="Next"
														onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')"
														<%=bottomGrid.getStatusNext()%>>
														<input type="button" value=">|" name="Last"
															onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')"
															<%=bottomGrid.getStatusLast()%>>
															<SELECT NAME="select_reportType" style="width: 110px"
																onChange="javascript:getReport()">
																<option value="">
																	[Select Report]
																</option>
																<option value="html">
																	Html
																</option>
																<option value="ExcelSheet">
																	Excel Sheet
																</option>
																<option value="dbf">
																	DBF
																</option>
															</SELECT>
										</td>
									</tr>
								</table>
						</tr>
				<tr>

					<td>
						<table align="center">
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
								<td class="tblabel">
									PFID&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									CPFAC.No&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Designation&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									DateofBirth&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									DateofJoining&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Airport Code &nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Region&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									PCReportVerified&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Form 6-7-8 Status&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									PensionClaims Processes&nbsp;&nbsp;
								</td>
								
									 
								<td class="tblabel">
									PCReport&nbsp;&nbsp;
								</td>
									
								<td class="tblabel">
									CadPCRStatus 
								</td>
								
								<td class="tblabel">
									CadReport
								</td>
						
								<td class="tblabel"></td>


								<%
									int count = 0;
											for (int i = 0; i < dataList.size(); i++) {
												count++;
												PensionBean beans = (PensionBean) dataList.get(i);
								%>

								<%
									String bgcolor = "";
												String title = "";
												if (beans.getSeperationReason().equals("Death")
														|| beans.getSeperationReason().equals(
																"Retirement")
														|| beans.getSeperationReason().equals(
																"Resignation")
														|| beans.getSeperationReason().equals(
																"Termination")
														|| beans.getSeperationReason().equals(
																"Resigned")) {
													bgcolor = "yellow";
													title = beans.getSeperationReason() + " on "
															+ beans.getSeperationDate();
												}
								%>
								<tr bgcolor='<%=bgcolor%>' title='<%=title%>'>
									<td class="Data"><%=beans.getPensionnumber()%></td>
									<td class="Data"><%=beans.getCpfAcNo()%></td>
									<td class="Data"><%=beans.getEmployeeName()%>
										&nbsp;&nbsp;
									</td>
									<td class="Data"><%=beans.getDesegnation()%></td>
									<td class="Data"><%=beans.getDateofBirth()%></td>
									<td class="Data"><%=beans.getDateofJoining()%></td>
									<td class="Data"><%=beans.getAirportCode()%></td>
									<td class="Data"><%=beans.getRegion()%></td>
									<td class="Data" width="10%" align="center">
										<select name="pcreportverified<%=j%>"
											onchange="pcreportverified(this.value,'<%=beans.getPensionnumber()%>');"
											style="width: 50px" tabindex="6">

											<option value="N"
												<%if (beans.getPcreportverified().equals("N")) {
							out.println("selected");
						}%>>
												No
											</option>
											<option value="Y"
												<%if (beans.getPcreportverified().equals("Y")) {
							out.println("selected");
						}%>>
												Yes
											</option>
										</select>
									</td>
									<td class="Data" width="10%" align="center">
										<select name="formsstatus<%=j%>"
											onchange="updateStatus(this.value,'<%=beans.getPensionnumber()%>');"
											style="width: 50px" tabindex="6">

											<option value="N"
												<%if (beans.getFormsdisable().equals("N")) {
							out.println("selected");
						}%>>
												No
											</option>
											<option value="Y"
												<%if (beans.getFormsdisable().equals("Y")) {
							out.println("selected");
						}%>>
												Yes
											</option>
										</select>
									</td>
									<td class="Data" width="10%" align="center">
										<select name="pensionClaims<%=j%>"
											onchange="pensionClaims(this.value,'<%=beans.getPensionnumber()%>');"
											style="width: 50px" tabindex="6">

											<option value="N"
												<%if (beans.getClaimsprocess().equals("N")) {
							out.println("selected");
						}%>>
												No
											</option>
											<option value="Y"
												<%if (beans.getClaimsprocess().equals("Y")) {
							out.println("selected");
						}%>>
												Yes
											</option>
										</select>
									</td>
									
									<%
									if(!beans.getStatus().equals("Approved")){
									 %>									 
									<td class="Data">
										<a href="#"
											onClick="validateForm('<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>')"><img
												src="./PensionView/images/page_edit.png" border="0"
												alt="PFReport">
										</a>
									</td>
									<%
									}else{
									 %>
									<td class="Data">
										<a href="#"
											onClick=""><img
												src="./PensionView/images/nominee.gif" border="0"
												alt="Locked">
										</a>
									</td>
									
									<%
									}
									 %>
									<td class="Data"><%=beans.getStatus()%></td>
									
									<%
									if(beans.getStatus().equals("Approved")){
									 %>	
									<td class="Data">
										<a href="#"
											onClick="cadAllYearReport('<%=beans.getPensionnumber()%>')"><img
												src="./PensionView/images/viewDetails.gif" border="0"
												alt="PFReport">
										</a>
									</td>
									
									<%
									}else{
									 %>
									 <td class="Data">
										
									</td>
									
									<%
									}
									 %>

									
									<input type="hidden" name="mappingflag"
										value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>'" />
									<td class="Data">
										<a href="#"> </a>
									</td>
								</tr>
								<%
									}
						}
			
									}
								%>
							
						</table>
					</td>
				</tr>
			</table>

		</form>
	</body>
</html>
