
<%@ page
	import="java.util.*,aims.common.CommonUtil,aims.service.FinancialService,aims.bean.EmployeeValidateInfo"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String[] year = {"2010", "2011", "2012","2013","2014","2015" };
	ArrayList remitanceList=new ArrayList();
	String region1="",airportcode="",year1="",month="",region="",salaryMonth="",remitanceType="",accountType="";
	ArrayList remitancetableList=new ArrayList();
	ArrayList noofEmployeeList=new ArrayList();
	RemittanceBean rbean=new RemittanceBean();
	AaiEpfform3Bean epf3bean=new AaiEpfform3Bean();
	String[] remitanceTypeList={"aaiepf3","aaiepf3Suppl"};
	if (request.getAttribute("region") != null) {
		region1 = (String) request.getAttribute("region");
	}
	if (request.getAttribute("airportcode") != null) {
		airportcode = (String) request.getAttribute("airportcode");
	}
	if (request.getAttribute("year") != null) {
		year1 = (String) request.getAttribute("year");
	}
	if (request.getAttribute("month") != null) {
		month = (String) request.getAttribute("month");
	}
	if (request.getAttribute("salaryMonth") != null) {
		salaryMonth = (String) request.getAttribute("salaryMonth");
	}
	if (request.getAttribute("remitanceType") != null) {
		remitanceType = (String) request.getAttribute("remitanceType");
	}
	if (request.getAttribute("remitancetableList") != null) {
		 remitancetableList = (ArrayList) request.getAttribute("remitancetableList");
		 if(remitancetableList.size()>0){
			 for(int i=0;i<remitancetableList.size();i++){
			 rbean=(RemittanceBean)remitancetableList.get(i);
			
		 }
	}}
	if (request.getAttribute("noofEmployeesList") != null) {
		noofEmployeeList = (ArrayList) request.getAttribute("noofEmployeesList");
		 if(noofEmployeeList.size()>0){
			 for(int i=0;i<noofEmployeeList.size();i++){
			 epf3bean=(AaiEpfform3Bean)noofEmployeeList.get(i);
			
		 }
	}}
	
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">

<title>AAI</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">

<script type="text/javascript">
  var xmlHttp;	
	  function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"RemittenceReport","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
		function getAirports(param){	
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID;
		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
	 if(yearID=='NO-SELECT'){
				alert('Please select Year');
				document.forms[0].select_year.focus();
				document.forms[0].select_region.value="";
				return false;
			}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if(regionID==''){
				alert('User Should be select Region');
				document.forms[0].select_region.focus();
				return false;
			}
		var accounttype=document.forms[0].accounttype.value;
		createXMLHttpRequest();	
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getAAIEPFAirports&region="+regionID+"&year="+yearID+"&accounttype="+accounttype;;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}else{
			if(document.forms[0].select_airport.length>1){
				airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				airportcode=document.forms[0].select_airport.value;
			}
			if(document.forms[0].select_year.selectedIndex>0){
				yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
			}else{
				yearID=document.forms[0].select_year.value;
			}
			if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if(regionID==''){
				alert('User Should be select Region');
				document.forms[0].select_region.focus();
				return false;
			}
			if(yearID=='NO-SELECT'){
				alert('User Should be select Year');
				document.forms[0].select_year.focus();
				return false;
			}
			if(document.forms[0].select_from_month.selectedIndex>0){
				monthID=document.forms[0].select_from_month.options[document.forms[0].select_from_month.selectedIndex].value;
			}else{
				monthID=document.forms[0].select_from_month.value;
			}
			if(monthID=='NO-SELECT'){
				alert('Please select the Month');
				document.forms[0].select_from_month.focus();
				return false;
			}
			frm_ltstmonthflag="false";
			var url ="<%=basePath%>psearch?method=getPFIDListWthoutTrnFlag&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_month="+monthID+"&frm_year="+yearID+"&frm_ltstmonthflag="+frm_ltstmonthflag;;
		
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getPFIDNavigationList;
		}
		xmlHttp.send(null);
		}

		
		function getNodeValue(obj,tag)
		{
		return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
		}
		
	function getAirportsList()
	{
	if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 process.style.display="block";
		
	}
	if(xmlHttp.readyState ==4)
	{
		if(xmlHttp.status == 200)
		{ 
		var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		process.style.display="none";
		if(stype.length==0){
	 	var obj1 = document.getElementById("select_airport");
	   	obj1.options.length=0; 
	  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
	   }else{
	   	var obj1 = document.getElementById("select_airport");
	  	obj1.options.length = 0;
	  	for(i=0;i<stype.length;i++){
	  		if(i==0)
				{
				obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
				}
	     	obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		}
	  	}
		}
	  }
	
	 }
	 
	 function frmload(){
	 process.style.display="none";
		var region1='<%=region1%>';
		if(region1==""){
		region1="Select One" ;
		}else{
		remitance.style.display="block";
		}
		var year1='<%=year1%>';
		if(year1==""){
		year1="Select One" ;
		}
		var month1='<%=month%>';
		if(month1==""){
			month1="Select One" ;
		}
		var airportcode1='<%=airportcode%>';
		if(airportcode1==""){
			airportcode1="Select One" ;
		}
		var remitanceType='<%=remitanceType%>';
     document.forms[0].select_airport[document.forms[0].select_airport.options.selectedIndex].text=airportcode1;
     document.forms[0].select_region[document.forms[0].select_region.options.selectedIndex].text=region1;
     document.forms[0].select_year[document.forms[0].select_year.options.selectedIndex].text=year1;
   
	
	 
	 }
	  function submitdata(){
      var reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
          var swidth=screen.Width-10;
      var sheight=screen.Height-150;    
		   if(document.forms[0].select_year.selectedIndex>0){
			yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
			}else{
			yearID=document.forms[0].select_year.value;
			}		
	   if(yearID=='NO-SELECT'){
			alert('Please select the From Year');
			document.forms[0].select_year.focus();
			return false;
		}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if(document.forms[0].select_airport.length>1){
			airportcode=document.forms[0].select_airport.value;
			
		}else{
			airportcode=document.forms[0].select_airport.value;
		}
		if(airportcode=="NO-SELECT"){
			airportcode="";
		}
		
		if(document.forms[0].select_from_month.selectedIndex>0){
		monthID=document.forms[0].select_from_month.options[document.forms[0].select_from_month.selectedIndex].value;
	    }else{
		monthID=document.forms[0].select_from_month.value;
	   }
	  if(monthID=='NO-SELECT'){
		alert('Please select the Month');
		document.forms[0].select_from_month.focus();
		return false;
	  }
	   if(regionID==''){
		alert('Please select the Region');
		document.forms[0].select_region.focus();
		return false;
	  }
	  if(reportType=='[Select One]'){
			alert('Please select the Report Type');
			document.forms[0].select_reportType.focus();
			return false;
		}			 
	var remitanceType=document.forms[0].remitancetype.value;
	var accounttype=document.forms[0].accounttype.value;
	 params = "&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&remitancetype="+remitanceType+"&accounttype="+accounttype;
	 url = "<%=basePath%>aaiepfreportservlet?method=remitanceReport"+params;
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
	 function createXMLHttpRequest()
		{
		if (window.XMLHttpRequest) {    
			xmlHttp = new XMLHttpRequest();   
		} else if(window.ActiveXObject) {    
		      try {     
		       xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");    
		       } catch (e) {     
		       		try {      
		       			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");     
		       		} catch (e) {      
		       			xmlHttp = false;     
		       		}    
		       	}   
		       		
		 } 
		 }
	
    </script>
</head>
<body class="BodyBackground" onload="javascript:frmload()">
<%
	String monthID = "", yearDescr = "", monthNM = "", monthNM1 = "", monthID1 = "";
	ArrayList yearList = new ArrayList();
	Iterator regionIterator = null;
	Iterator monthIterator = null;
	HashMap hashmap = new HashMap();
	if (request.getAttribute("regionHashmap") != null) {
		hashmap = (HashMap) request.getAttribute("regionHashmap");
		Set keys = hashmap.keySet();
		regionIterator = keys.iterator();
	}
	if (request.getAttribute("monthIterator") != null) {
		monthIterator = (Iterator) request
				.getAttribute("monthIterator");
	}
	
%>
<form name="validation" method="post">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>
    <tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
			<tr>
				<td height="2%" colspan="2" align="center" class="ScreenMasterHeading">REMITTANCE REPORT</td>
			</tr>
<tr>
		<td>
		<table height="25%" align="center">
         <tr>
		<td>&nbsp;</td>
	   </tr>
			<tr>
           
				<td class="label" align="right">From Year / Month:<font
					color=red>*</font>&nbsp;</td>
				<td><Select name='select_year' Style='width: 60px'>
					<option value='NO-SELECT'>Select One</option>
                 
					<%
						for (int j = 0; j < year.length; j++) {
					%>
                   <%if (year[j].equalsIgnoreCase(year1)) {
					%>
							<option value="<%=year1%>" <% out.println("selected");%>>
							<%=year1%></option>


							<%}else{%>
					<option value='<%=year[j]%>'><%=year[j]%></option>
					<%
						}}
					%>
				</SELECT> <select name="select_from_month" style="width: 65px">
					<Option Value='NO-SELECT'>Select One</Option>

					<%
						while (monthIterator.hasNext()) {
							Map.Entry mapEntry = (Map.Entry) monthIterator.next();
							monthID = mapEntry.getKey().toString();
							monthNM = mapEntry.getValue().toString();
					%>
                     <%if (monthID.equalsIgnoreCase(month)) {
					 %>
					<option value="<%=monthID%>" <% out.println("selected");%>>
							<%=monthNM%></option>
							<%}else {%>
					<option value="<%=monthID%>"><%=monthNM%></option>
					<%
					 	}}
					%>
				</select></td>
			</tr>
    <tr>
				<td class="label" align="right">REMITTANCE TYPE:&nbsp;&nbsp;</td>
				<td><SELECT NAME="remitancetype" style="width: 135px" >
                   <% for(int i=0;i<remitanceTypeList.length;i++){
                	   String remitacelist=remitanceTypeList[i];
                	   if(remitacelist.equals(remitanceType)){ %>
					<option value='<%=remitanceType%>' <% out.println("selected");%>><%=remitanceType%></option>
                    <%}else{ %>
					<option value='<%=remitacelist%>'><%=remitacelist%></option>
                     <%}}%>
				</SELECT></td>
			</tr>
			<tr>
				<td class="label" align="right">Region / Account Type:<font
					color=red>*</font>&nbsp;</td>
				<td><SELECT NAME="select_region" style="width: 70px">
					<option value="">Select One</option>
					<%
						int k = 0;
						boolean exist = false;
						while (regionIterator.hasNext()) {
							region = hashmap.get(regionIterator.next()).toString();
							k++;

							if (region.equalsIgnoreCase(region1)) {
					%>
					<option value="<%=region1%>" <%out.println("selected");%>>
					<%=region1%></option>
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
				</SELECT> <SELECT NAME="accounttype" style="width: 60px"
					onChange="javascript:getAirports('airport');">
					<option value="">Select One</option>

					<option value="SAU"
						<%if (accountType.equals("SAU")) {
				out.println("selected");
			}%>>SAU</option>
					<option value="RAU"
						<%if (accountType.equals("RAU")) {
				out.println("selected");
			}%>>RAU</option>

				</SELECT></td>
			</tr>
			<tr>
				<td class="label" align="right">Airport Name:&nbsp;&nbsp;</td>
				<td><SELECT NAME="select_airport" style="width: 135px" >
					<option value="" Selected>[Select One]</option>
				</SELECT></td>
			</tr>
          <TR>
					<td class=label align="right" nowrap>
						Report Type: <font color=red>*</font>&nbsp;
					</td>
					<td>
						<SELECT NAME="select_reportType" style="width:135px">
								<option value='NO-SELECT' Selected>[Select One]</option>
							<option value="html">
								Html
							</option>
							<option value="ExcelSheet">
								Excel Sheet
							</option>
						</SELECT>
					</td>
					&nbsp;&nbsp;
				</TR>
		</table>
<table align="center">
   <tr>
		 <td align="right"><input type="button" class="btn"
					name="Submit" value="Submit" onclick="submitdata();"><input
					type="button" class="btn" name="Reset" value="Reset"
					onclick="getData();"><input
					type="button" class="btn" name="Submit" value="Cancel"
					onclick="javascript:history.back(-1)">
         </td>
	</tr>
</table>
		</td>
	</tr>
	

			
			
		</table>
		</td>

	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
	</tr>
	
	<tr>

	<td>
	</tr>

<table align="center" cellpadding=2 class="tbborder" cellspacing="0"
			border="">

</table>

<tr><td>&nbsp;</td></tr>
	<tr>
		<td>

</table>

</table>
<input type="hidden" name="salarymonth" value="<%=salaryMonth%>">
<input type="hidden" name="region" value="<%=region1%>">
<input type="hidden" name="airportcode" value="<%=airportcode%>">

</form>
<div id="process"
	style="position: fixed; width: auto; height: 35%; top: 200px; right: 0; bottom: 100px; left: 10em;"
	align="center"><img
	src="<%=basePath%>PensionView/images/Indicator.gif" border="no"
	align="middle" /> <SPAN class="label">Processing.......</SPAN></div>
</body>
</html>
