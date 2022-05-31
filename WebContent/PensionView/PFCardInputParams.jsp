<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    
                    String[] year = {"2008-09","2009-10","2010-11","2011-12","2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};
                    String[] userYears = {"2008-09","2009-10","2010-11","2011-12","2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};	
            String userType =session.getAttribute("usertype").toString();
            %>

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants,aims.bean.PensionBean" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>

<html>
	<HEAD>
<style>
		.black_overlay{
			display: none;
			position: fixed;
			top: 0%;
			left: 0%;
			width: 100%;
			height: 3000px;
			background-color: black;
			z-index:1001;
			-moz-opacity: 0.8;
			opacity:.80;
			filter: alpha(opacity=80);
		}
		.white_content {
			display: none;
			position: fixed;
			top: 25%;
			left: 25%;
			width: 50%;
			height: 50%;
			padding: 16px;
			border: 16px solid orange;
			background-color: white;
			z-index:1002;
			overflow: auto;
		}
	</style>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<script type="text/javascript">

		

 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].chk_empflag.checked=true;		
		}
	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadpfcardInput";
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
		/*retricting loading pfids list*/
		document.forms[0].select_region.value = "AllRegions";
		document.forms[0].select_airport.length = 1;
		document.forms[0].select_pfidlst.length=1;
		return true;
		}
   function PfProcess(){
	   if(document.forms[0].empserialNO.value==""){
		  alert("Please Select Employee Name");
		  return false;
	   }
	  document.forms[0].select_reportType.disabled="true";
	  document.forms[0].select_pfidlst.disabled="true";
	  document.forms[0].select_formType.disabled="true";
	   var empserialNO=document.forms[0].empserialNO.value;
	   process.style.display="block";
	   createXMLHttpRequest();
        var url="<%=basePath%>reportservlet?method=ProcessforAdjOb&pensionno="+empserialNO;
     	xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = runPfProcess;
		xmlHttp.send(null);
	   }

    function runPfProcess()
   {
 	if(xmlHttp.readyState ==4)
   	{
  	process.style.display="none";
	alert(xmlHttp.responseText);
	  document.forms[0].select_reportType.disabled=false;
	  document.forms[0].select_pfidlst.disabled=false;
	  document.forms[0].select_formType.disabled=false;
    }
   }
   var bulkPrintFlag='false';
	function bulkreport(userID){
		var regionID1='',airportcode1='';
		document.forms[0].chk_ltstmonthflag.value=true;
		var chkEmpName=document.forms[0].empName.value;
		if(chkEmpName!=''){
				alert('Bulk Print Cannot Process a Individual Records.Please click on Submit Button');
				document.forms[0].empName.focus();
				return false;
		}
		var bulkpfidlist=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].text;
		if(bulkpfidlist=='[Select One]'){
				alert('Please Select the PFID Printing List');
				document.forms[0].select_pfidlst.focus();
				return false;
		}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID1=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID1=document.forms[0].select_region.value;
		}
		if(document.forms[0].select_airport.selectedIndex>0){
			airportcode1=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
		}else{
			airportcode1=document.forms[0].select_airport.value;
		}
		if(regionID1=='CHQNAD' || regionID1=='West Region' || regionID1=='East Region' || regionID1=='North-East Region' || regionID1=='North Region'|| regionID1=='RAUSAP'|| regionID1=='South Region' || (regionID1=='CHQIAD'&&airportcode1=='OFFICE COMPLEX')|| (regionID1=='CHQIAD'&&airportcode1=='IGI IAD')|| (regionID1=='CHQIAD'&&airportcode1=='IGICargo IAD')){
			if(!(userID=='CHQFIN' || userID=='NRFIN' || userID=='ERFIN' || userID=='SRFIN' || userID=='NERFIN' || userID=='WRFIN' || userID=='SAPFIN'|| userID=='IGIFIN'|| userID=='CARGOFIN')){
				alert('Dont have Privileges for taking bulk print outs Other than Assistant Manager(F)/Manager(F) ');
				return false;
			}
		}
		bulkPrintFlag='true';
		validateForm();
	}
	function validateForm() {
		var regionID="",airportcode="",params="",sortingOrder="",empName="",reportType="",url="",yearID="",monthID="",yearDesc="",formType="",toYearID="",formType="",frm_signature="";	
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;

		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
		if(yearID=='NO-SELECT'){
			alert('Please Select the Year ');
			document.forms[0].select_year.focus();
			return false;
		}
		var splitYearID = yearID.split("-");
		yearID=splitYearID[0];
	
		if(reportType=='[Select One]'){
			alert('Please Select the Report Type');
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
		 
		var transMonthYear=document.forms[0].transMonthYear.value;
		var empserialNO=document.forms[0].empserialNO.value;
		//alert(empserialNO);
		if(empserialNO=='' ){
			
		pfidStrip=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].value;
	//	alert('User Should  Select Bulk Print Option');
	//	document.forms[0].select_pfidlst.focus();
	//	return false;
		}else{
		if(empserialNO!=''){
			pfidStrip='NO-SELECT'; 
		}else{
			pfidStrip='1 - 1'; 
		}
		}

	//	if (document.forms[0].chk_ltstmonthflag.checked==true){
	  //            frm_ltstmonthflag="true";
     //  	}else{
	 //      		  frm_ltstmonthflag="false";
     //  	}
       	frm_ltstmonthflag=document.forms[0].chk_ltstmonthflag.value;
       //	alert('frm_ltstmonthflag'+frm_ltstmonthflag);
		formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
		if(document.forms[0].empName.value!='' && document.forms[0].chk_empflag.checked==false){ 
			  		alert('Please Checked Employee Name');
			  		document.forms[0].empName.focus();
			  		return false;
		}
		if (document.forms[0].chk_empflag.checked==true){
		if(document.forms[0].empName.value==''){
			alert('Please Select the Employee');
			document.forms[0].empName.focus();
			return false;
		}
		document.forms[0].select_region.value = "AllRegions";
		document.forms[0].select_airport.length = 1;
		regionID = "AllRegions";
		airportcode = "";
		pfidStrip  = "NO-SELECT";
		document.forms[0].select_pfidlst.length=1;
		document.forms[0].select_pfidlst.value = "NO-SELECT";
			  			empName=document.forms[0].empName.value;
        				frm_empflag=true;
       	}else{
	       		  frm_empflag=false;
       	}
		if(airportcode=='NO-SELECT'){
				airportcode='';
		}
		if (document.forms[0].signature.checked==true){
  			frm_signature=true;
		}else{
			frm_signature=false;
		}
		 
		 if(yearID<2008){
		 alert("Access Denied From 1995-2007");
		 document.forms[0].select_year.focus();
		 return false;
		 }
		
		params = "&frm_region="+regionID+"&frm_year="+yearID+"&frm_reportType="+reportType+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+empserialNO+"&frm_pfids="+pfidStrip+"&sortingOrder="+sortingOrder+"&frm_ltstmonthflag="+frm_ltstmonthflag+"&transMonthYear="+transMonthYear+"&frmAirportCode="+airportcode+"&frm_blkprintflag="+bulkPrintFlag+"&frm_signature="+frm_signature;
		url="<%=basePath%>reportservlet?method=cardReport"+params;
		 
	//// alert(url);
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
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID="NO-SELECT";
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		createXMLHttpRequest();	
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getPFCardAirports&region="+regionID;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}else{
			if (document.forms[0].chk_empflag.checked==false){
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
	 //hard coded the below line as on 11-May-2011
	 //		yearID='2010-2011';
			var formPageSize=document.forms[0].frmpagesize.value;
			if(yearID=='NO-SELECT'){
				alert('Please Select the Year ');
				document.forms[0].select_year.focus();
				return false;
		    }
			if(regionID=='AllRegions'){
				regionID='NO-SELECT';
				}
			 
		   frm_ltstmonthflag="true";
       		
			bulkPrintFlag="false";
			var url ="<%=basePath%>psearch?method=getPFIDBulkPrintingList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_month="+monthID+"&frm_year="+yearID+"&frm_ltstmonthflag="+frm_ltstmonthflag+"&frm_pagesize="+formPageSize;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getPFIDNavigationList;
		}
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
		    //alert(stype.length);	
		  	obj1.options.length = 0;
		  	var chkLength=false;
		  	if(stype.length==1){
		  		chkLength=true;
		  	}else{
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		  	}
		  	
		  	for(i=0;i<stype.length;i++){
		  	
		       		if(chkLength==true){
		       			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		       		}else{
		       		

		       			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		       		}
					
			}
		  }
		}
	}

	 
}

function getPFIDNavigationList()
	{
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			 process.style.display="block";
		}
		if(xmlHttp.readyState ==4)
		{
			if(xmlHttp.status == 200)
				{ 
			      	var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
			      	var stype1 = xmlHttp.responseXML.getElementsByTagName('PARAMSTR');
					 process.style.display="none";
					  if(stype.length==0){
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_pfidlst");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_pfidlst");
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
			for(k=0;k<stype1.length;k++){
		 		document.forms[0].transMonthYear.value=getNodeValue(stype1[k],'PARAM1');
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
		<%
		
		
		String employeeName="",pfID="",doj="";
		if(session.getAttribute("usertype").equals("Employee")){
		if(session.getAttribute("empData")!=null){
			PensionBean bean=(PensionBean)session.getAttribute("empData");
			employeeName=bean.getEmployeeName();
			pfID=bean.getEmpSerialNumber();
			System.out.println("---------"+pfID);
			doj=bean.getDateofJoining();
		}
		}
		
		String monthID = "", yearDescr = "", region = "", monthNM = "";

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

			<table width="65%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						 PF Card Report Params
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
					<td class="label" align="right">
						Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:100px'>
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
						Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:100px'>
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
				 
				<input type="hidden" name="chk_ltstmonthflag">
				<tr>
																	<td class="label" align="right">
																		Region:<font color=red></font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_region" style="width:120px" onChange="javascript:getAirports('airport');">
																			<option value="AllRegions">
																				[Select One]
																			</option>
																			<%while (regionIterator.hasNext()) {
																			region = hashmap.get(regionIterator.next()).toString();

																			%>
																			<option value="<%=region%>" >
																				<%=region%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>
																</tr>
																<tr>
																	<td class="label" align="right">
																		Airport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_airport" style="width:120px" >
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
				<tr>
					<td class="label" align="right">Page Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td><input type="text" name="frmpagesize" tabindex="3" value="100"></td>
				</tr>
				<tr>
					<td class="label" align="right">
						Employee Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
					
					<% if(session.getAttribute("usertype").equals("Employee")){%>
						<input type="text" name="empName" value='<%=employeeName %>' readonly="true" tabindex="3">
					
					<input type="hidden" name="empserialNO" value='<%=pfID%>' >
					<input type="checkbox" name="chk_empflag" checked readonly="true" disabled>
						<% }else{
						%>
						<input type="hidden" name="empserialNO">
						<input type="text" name="empName" readonly="true" tabindex="3">
						<img src="<%=basePath%>/PensionView/images/search1.gif" onclick="popupWindow('<%=basePath%>PensionView/PensionContributionMappedListWithoutTransfer.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
						<input type="checkbox" name="chk_empflag">
						
                <!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="#"  onclick="return PfProcess()"  title="Click here to ReProcess PF" class="link"><font color="BLACK"> ReProcess PF&nbsp;&nbsp; </font></a> </td>
					<!-- 	<input type="button" class="btn" name="Submit" value="ReProcessPF"  onclick="javascript:PfProcess()"> -->
					<%} %>
</td>
				</tr>
				<tr>
					<td class=label align="right" nowrap>Form Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td><Select name='select_formType' Style='width:100px' align="left">
						<option value='PF Card'>PF Card</option>
						
						</SELECT>
					</td>
				</tr>
				<TR>
					<td class=label align="right" nowrap>
						Report Type:<font color=red>*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<SELECT NAME="select_reportType" style="width:100px" onChange="javascript:getAirports('data')">
								<option value='NO-SELECT' Selected>[Select One]</option>
							<option value="html">
								Html
							</option>
							<option value="ExcelSheet">
								Excel Sheet
							</option>
						</SELECT>
					</td>
				
				</TR>
					<tr>
							<td class="label" align="right">PF ID Printing List:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
							<td>									
								<SELECT NAME="select_pfidlst" style="width:120px" >
								
							  	<option value='NO-SELECT' Selected>[Select One]</option>
	                       		<%for (int pfid = 0; pfid < pfidList.size(); pfid++) {%>
									<option value='<%=pfidList.get(pfid)%>'><%=pfidList.get(pfid)%></option>
								<%}%>
								</SELECT>
							</td>
						</tr>
				<tr>
				<tr>
					<td class="label" align="right">Signature:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td align="left"><input type="checkbox" name="signature"></td>
                </tr>
				<tr>
					<td align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

				
				<input type="hidden" name="transMonthYear">



				<tr>
					<td align="center" colspan="2">
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
						<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
						<%if(!userType.equals("Employee")){ %>
					    <input type="button" class="btn" name="button" value="Bulk Print" onclick="javascript:bulkreport('<%=session.getAttribute("userid")%>')">
                   <%} %>
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
