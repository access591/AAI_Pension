

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    
                    String[] year = {"2010-11","2011-12","2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};
                    String[] userYears = {"2010-11","2011-12","2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};		
                    String accountType="",region1="";
            %>
<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<script type="text/javascript">
	
 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Accretion","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].chk_empflag.checked=true;		
		}
	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>aaiepfreportservlet?method=loadAccretionParam";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	function popupWindow(mylink, windowname)
		{
		
		//var transfer=document.forms[0].transferStatus.value;
		var transfer="";
	
		
		var regionID="";
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
	
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID;
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID;
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		
		return true;
		}

	
	function validateForm() {
		var regionID="",airportcode="",params="",sortingOrder="",empName="",reportType="",url="",yearID="",monthID="",yearDesc="",formType="",toYearID="",formType="",screen="";
		screen="accretion";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;

		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
		if(yearID!='NO-SELECT'){
			var splitYearID = yearID.split("-");
			yearID=splitYearID[0];
			if(yearID.substring(0,2)=='19' && yearID.substring(2,4)<'99'){
				toYearID=yearID.substring(0,2)+splitYearID[1]
			}else if(yearID.substring(0,2)=='20'){
				toYearID=yearID.substring(0,2)+splitYearID[1]
			}else if(yearID.substring(0,2)=='19' && yearID.substring(2,4)=='99'){
				toYearID=2000;
			}
		}else{
			alert('Please select the Year');
			document.forms[0].select_year.focus();
			return false;
		}
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		if(monthID=='Select One'){
			alert('Please select the Month');
			document.forms[0].select_month.focus();
			return false;
		}
		if(reportType=='[Select One]'){
			alert('Please select the Report Type');
			document.forms[0].select_reportType.focus();
			return false;
		}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		
		if(document.forms[0].select_airport.length>1){
			airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
		}else{
			airportcode=document.forms[0].select_airport.value;
		}
		
		if(document.forms[0].select_month.selectedIndex>0){
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
		monthID=document.forms[0].select_month.value;
		}
		var empserialNO=document.forms[0].empserialNO.value;
		var accounttype=document.forms[0].accounttype.value;
		var remitanceType=document.forms[0].remitancetype.value;
		//alert('-------monthID-----'+monthID);
		params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_reportType="+reportType+"&frm_pensionno="+empserialNO+"&frm_ToYear="+toYearID+"&frm_month="+monthID+"&remitancetype="+remitanceType+"&accounttype="+accounttype+"&screen="+screen;
		url="<%=basePath%>aaiepfreportservlet?method=AccretionReport"+params;
		//params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_reportType="+reportType+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+empserialNO+"&frm_pfids="+pfidStrip+"&sortingOrder="+sortingOrder;
		//alert(url);
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
	if(window.ActiveXObject)
	 {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 }
	else if (window.XMLHttpRequest)
	 {
		xmlHttp = new XMLHttpRequest();
	 }
	 }
	function getAirports(param){	
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID;
					if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		var accounttype=document.forms[0].accounttype.value;
		createXMLHttpRequest();	
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getAirports&region="+regionID+"&accounttype="+accounttype;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
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
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_airport");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_airport");
		  //    alert(stype.length);	
		  	obj1.options.length = 0;
		  	
		  	for(i=0;i<stype.length;i++){
		  		if(i==0)
					{
				//	alert("inside if")
					obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}
		          //	alert("in else");
			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
			}
		  }
		}
	}

	 
}

	function frmload(){
		 process.style.display="none";
	}

</script>
	</HEAD>
	<body class="BodyBackground" onload="javascript:frmload()">
		<%String monthID = "", yearDescr = "", region = "", monthNM = "";

            ArrayList yearList = new ArrayList();
            ArrayList pfidList = new ArrayList();
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
            if (request.getAttribute("yearList") != null) {
                yearList = (ArrayList) request.getAttribute("yearList");
            }
            if (request.getAttribute("pfidList") != null) {
            	pfidList = (ArrayList) request.getAttribute("pfidList");
            }
            
            %>
		<form action="post">
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
			</table>

			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						 Accretion Report Input Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
					<%if (session.getAttribute("usertype").equals("Admin")) {

                %>
				<tr>
					<td width="50%" class="label" align="right">
						Year:<font color=red>*</font>&nbsp;&nbsp;
					</td>
					<td width="50%">
						<Select name='select_year' Style='width:135px'>
							<option value='NO-SELECT'>
								Select One
							</option>
					<%for (int j = 0; j < year.length; j++) {%>
																			<option value='<%=year[j]%>'>
																				<%=year[j]%>
																			</option>
																			<%}%>
						</SELECT>
					</td>
				</tr>
				<%}else{%>
							<tr>
					<td class="label" align="right">
						Year:<font color=red>*</font>&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:135px'>
							<option value='NO-SELECT'>
								Select One
							</option>
					<%for (int j = 0; j < userYears.length; j++) {%>
																			<option value='<%=userYears[j]%>'>
																				<%=userYears[j]%>
																			</option>
																			<%}%>
						</SELECT>
					</td>
				</tr>
				<%}%>
				<tr>
					<td class="label" align="right">
						Month:<font color=red>*</font>&nbsp;&nbsp;</td>
					<td>
						<select name="select_month" style="width:135px">
							<Option Value='NO-SELECT'>
								Select One
							</Option>

							<%while (monthIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) monthIterator.next();
                monthID = mapEntry.getKey().toString();
                monthNM = mapEntry.getValue().toString();

                %>

							<option value="<%=monthID%>">
								<%=monthNM%>
							</option>
							<%}%>
						</select>
					</td>
				</tr>
 <tr>
				<td class="label" align="right">REMITTANCE TYPE:
				&nbsp;&nbsp;&nbsp;</td>
				<td><SELECT NAME="remitancetype" style="width: 135px" >
<option value="aaiepf">AAIEPF-3/AAIEPF-3Suppl</option>
<option value="aaiepf3">AAIEPF-3</option>
<option value="aaiepf3Suppl">AAIEPF-3Supp</option>
<option value="aaiepf3Arr">AAIEPF-3Arr</option>
<option value="form4deputation">Form4-DeputationList</option>

</SELECT>
</td></tr>

<tr>
				<td class="label" align="right">Region / Account Type:&nbsp;&nbsp;&nbsp;</td>
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
																	<td class="label" align="right">
																		Airport Name:&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_airport" style="width:135px">
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
				<TR>
					<td class=label align="right" nowrap>
						Report Type: <font color=red>*</font>&nbsp;
					</td>
					<td>
						<SELECT NAME="select_reportType" style="width:135px" onChange="javascript:getAirports('data')">
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
				<tr>
					<td align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<input type="hidden" name="empserialNO" readonly="true" tabindex="3">
				<tr>
					<td align="center" colspan="2">
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
						<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
					</td>
				</tr>
				<tr>
					<td align="center"></td>
				</tr>
			</table>



		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>

	</body>
</html>
