<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010"};
            %>

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants,aims.bean.UserBean,aims.bean.PensionBean" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>

<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		

		<script type="text/javascript">

 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 var chkpfid="";
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,chkpfid,dateofJoining,PCReportVerified){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].chkpfid.value = chkpfid;
		  	document.forms[0].dateofJoining.value=dateofJoining;
		  	document.forms[0].PCReportVerified.value = PCReportVerified;		  		
		}
		 var bulkPrintFlag='false';
	function bulkreport(userID){
		
		var regionID1='',airportcode1='';
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
		if(regionID1=='North Region' || regionID1=='South Region'|| regionID1=='North-East Region' || regionID1=='RAUSAP' || regionID1=='West Region'|| (regionID1=='CHQIAD'&&airportcode1=='IGI IAD')|| (regionID1=='CHQIAD'&&airportcode1=='IGICargo IAD')){
			if(!(userID=='NRFIN' || userID=='SRFIN' || userID=='NERFIN'|| userID=='WRFIN' || userID=='SAPFIN'|| userID=='IGIFIN'|| userID=='CARGOFIN')){
				alert('Dont have Privileges for taking bulk print outs Other than Assistant Manager(F)/Manager(F) ');
				return false;
			}
		}
		bulkPrintFlag='true';
		validateForm();
	}
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadFinContri";
		document.forms[0].method="post";
		document.forms[0].submit();
	}	
	function popupWindow(mylink, windowname)
		{		
		//var transfer=document.forms[0].transferStatus.value;
		var transfer="";
	
		var regionID="",adjPfidChkFlag="true",pcReportChkFlag="true";
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&pcReportChkFlag="+pcReportChkFlag;
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&pcReportChkFlag="+pcReportChkFlag;
		
		//alert(href);
	    progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		/*retricting loading pfids list*/
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		document.forms[0].select_pfidlst.length=1;
		return true;
		}

	
	function validateForm(){
		var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="",formType="",toYearID="",finalairportcode="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		
		
		
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		
      	if(document.forms[0].select_airport.length>1){
			finalairportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
		}else{
			finalairportcode=document.forms[0].select_airport.value;
		}
	
			airportcode=finalairportcode;
		
			if(reportType=='[Select One]'){
				alert('Please Select the Report Type');
				document.forms[0].select_reportType.focus();
				return false;
			}
		
		transferFlag=document.forms[0].transferStatus.options[document.forms[0].transferStatus.selectedIndex].value;
	
		if(transferFlag=='NO-SELECT'){
				transferFlag='';
		}
		
		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
		if(document.forms[0].select_year.selectedIndex>0){
			toYearID=document.forms[0].select_to_year.options[document.forms[0].select_to_year.selectedIndex].value;
		}else{
			toYearID=document.forms[0].select_to_year.value;
		}
		
		if(document.forms[0].select_month.selectedIndex>0){
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
		monthID=document.forms[0].select_month.value;
		}
	
		//monthID=document.forms[0].select_month.selectedIndex;
	//	if(monthID<10){
	//		monthID="0"+monthID;
	//	}
		
		var empserialNO=document.forms[0].empserialNO.value;
		if(empserialNO=='' ){
			pfidStrip=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].value;
			
		}else{
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		regionID = "NO-SELECT";
		airportcode = "NO-SELECT";
		pfidStrip  = "NO-SELECT";
		document.forms[0].select_pfidlst.length=1;
		pfidStrip='1 - 1'; 
		}
		//alert(empserialNO+','+pfidStrip);
	//	var	page="PensionContribution";
		var mappingFlag="false";
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&transferStatus="+transferFlag+"&frm_pfids="+pfidStrip+"&frm_toyear="+toYearID+"&mappingFlag="+mappingFlag+"&frm_bulkprint="+bulkPrintFlag;
		var url ="",params1="";
		 
		if(document.forms[0].chkpfid.value=="Exists"){
		var comfirmMsg=confirm("This Pfid is already done at Adj Calculation Screen.Click Ok for Adj Calculated PC Report and Cancel For Normal PC Report");
		// alert(comfirmMsg);
		if (comfirmMsg== true){ 
	    var page = "report",adjOBYear="1995-2008";
	      params1 = "&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&reportYear="+adjOBYear+"&page="+page;
		  url="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtn"+params1;
		}else{
		if(document.forms[0].dateofJoining.value!=""){		
		var doj = document.forms[0].dateofJoining.value;
		var finaldate=getDate(doj);
		var date1=new Date(2008,03,01); 
		if(finaldate >= date1){
		alert("This pfid joined after 31-Mar-2008.So You can generate PFCard");
		return false;
		}
		}
		if(document.forms[0].dateofJoining.value==""){
		alert("This pfid is not mapped with old data");
		return false;
		}
		
		  url="<%=basePath%>reportservlet?method=getReportPenContr"+params;		
		}
		}else{		
		
		if(document.forms[0].dateofJoining.value!=""){
		var doj = document.forms[0].dateofJoining.value;
		var finaldate=getDate(doj);
		var date1=new Date(2008,03,31);		
		if(finaldate >= date1){
		alert("this pfid have the date of joining "+doj+" so PC Report can not generated");
		return false;
		}		
		}
		if(document.forms[0].dateofJoining.value==""){
		alert("This PFID  not have mapped records");
		return false;
		}		 
	 
		/*if(document.forms[0].PCReportVerified.value=="N"){
		alert("PC Report is Disabled by CHQ.Please contact CHQ Officials");
		return false;
		}*/
		url="<%=basePath%>reportservlet?method=getReportPenContr"+params;
		}
	  //alert(url);
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet'||reportType=='DBF' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
		
	}
	function getDate(date){	 
	  if(date.indexOf('-')!=-1){
		 elem = date.split('-'); 
	  }else{
		  elem = date.split('/'); 
	  }	  
		 day = elem[0];
		 mon1 = elem[1];
		 year = elem[2];
		 var month;
	   	 if((mon1 == "JAN") || (mon1 == "Jan")) month = 0;
        	else if(mon1 == "FEB" ||(mon1 == "Feb")) month = 1;
     	else if(mon1 == "MAR" || (mon1 == "Mar")) month = 2;
     	else if(mon1 == "APR" || (mon1 == "Apr")) month = 3;
     	else if(mon1 == "MAY" ||(mon1 == "May") ) month = 4;
     	else if(mon1 == "JUN" ||(mon1 == "Jun") ) month = 5;
     	else if(mon1 == "JUL"||(mon1 == "Jul")) month = 6;
     	else if(mon1 == "AUG" ||(mon1 == "Aug")) month = 7;
     	else if(mon1 == "SEP" ||(mon1 == "Sep")) month = 8;
     	else if(mon1 == "OCT"||(mon1 == "Oct")) month = 9;
     	else if(mon1 == "NOV" ||(mon1 == "Nov")) month = 10;
     	else if(mon1 == "DEC" ||(mon1 == "Dec")) month = 11;
	  var finaldate=new Date(year,month,day); 
	   return finaldate;	     	
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
		var transferFlag,airportcode,regionID,finalairportcode;
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
		if(document.forms[0].empserialNO.value==''){
			transferFlag=document.forms[0].transferStatus.options[document.forms[0].transferStatus.selectedIndex].value;
			if(transferFlag=='NO-SELECT'){
				transferFlag='';
			}
			if(document.forms[0].select_airport.length>1){
				finalairportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				finalairportcode=document.forms[0].select_airport.value;
			}
		   if(finalairportcode=='NO-SELECT'){
				airportcode='';
			}else{
				airportcode=finalairportcode;
			}
			var formPageSize=document.forms[0].frmpagesize.value;
			bulkPrintFlag="false";
			var url ="<%=basePath%>psearch?method=getPFIDBulkPrintingList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_transflag="+transferFlag+"&frm_pagesize="+formPageSize;
			//var url ="<%=basePath%>psearch?method=getPFIDList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_transflag="+transferFlag;

				
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
		  }
		}
	}

	 
}
	function frmload(){
		 process.style.display="none";
	}
	function generateForm(){
		var formType;
		formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
		if(formType=='Employee Wise'){
		}
	}
--></script>
	</HEAD>
	<body class="BodyBackground" onload="javascript:frmload()">
		<%
		String employeeName="",pfID="",doj="";
		if(session.getAttribute("usertype").equals("Employee")){
		if(session.getAttribute("empData")!=null){
			PensionBean bean=(PensionBean)session.getAttribute("empData");
			employeeName=bean.getEmployeeName();
			pfID=bean.getEmpSerialNumber();
			doj=bean.getDateofJoining();
		}
		}
		
		String monthID = "", yearDescr = "", region = "", monthNM = "";

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
            if (request.getAttribute("yearList") != null) {
                yearList = (ArrayList) request.getAttribute("yearList");
            }

            %>
		<form action="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%">


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
						Pension Contribution Report Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td  width="50%" class="label" align="right">
						Finance From Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<Select name='select_year' Style='width:100px'>
							<option value='1995' selected="selected"> 1995 </option> 
						</SELECT>
					</td>
				</tr>
				<tr>
					<td  width="50%" class="label" align="right">
						Finance To Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<Select name='select_to_year' Style='width:100px'>
							<option value='2008' selected="selected">2008 </option> 
						</SELECT>
					</td>
				</tr>
				<tr>
					<td  width="50%" class="label" align="right">
						Finance To Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<select name="select_month" style="width:100px">
							<Option Value='03' selected="selected">
								March
							</Option> 
						</select>
					</td>
				</tr>
				<!-- 	<TR>
							<td class=label align="right" nowrap > Pension Form Type: <font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td>
								<SELECT NAME="select_formType" style="width:88px" onchange="generateForm()">
	                       					<option ="yearwise">Year Wise</option>
	                       					<option ="monthwise">Month Wise</option>
	                       					<option ="employeewise">Employee Wise</option>
								</SELECT>
										
							</td>&nbsp;&nbsp;
						</TR>-->
				<tr>
					<td  width="50%" class="label" align="right">
						Region:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					
					<td width="50%" >
						<SELECT NAME="select_region" style="width:130px" onChange="javascript:getAirports('airport');">
							<option value="NO-SELECT">
								[Select One]
							</option>
							<option value="AllRegions">
								AllRegions
							</option>
							<%int j = 0;
                boolean exist = false;
                while (regionIterator.hasNext()) {
                    region = hashmap.get(regionIterator.next()).toString();
                    j++;

                    {%>
							<option value="<%=region%>">
								<%=region%>
							</option>

							<%}
                }

            %>

							
						</SELECT>
					</td>
				</tr>
				<tr>
							<td width="50%"  class="label" align="right">Airport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
							<td width="50%" >									
								<SELECT NAME="select_airport" style="width:120px" onchange="javascript:getAirports('pfid')"  >
								
							  	<option value='NO-SELECT' Selected>[Select One]</option>
	                       		
								</SELECT>
							</td>
						</tr>
					<tr>
					<td  width="50%" class="label" align="right">Page Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td><input type="text" name="frmpagesize" tabindex="3" value="100"></td>
				</tr>
				<tr>
					<td width="50%" class="label" align="right">
						Employee Transfer Status:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<SELECT NAME="transferStatus" style="width:88px" >
							<option value="NO-SELECT">
								[Select One]
							</option>
							<option value="Y">
								Yes
							</option>
							<option value="N">
								No
							</option>
						</SELECT>
					</td>

				</tr>

				<tr>
					<td width="50%"  class="label" align="right">
						Employee Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						
						<% if(session.getAttribute("usertype").equals("Employee")){%>
						
						
						<input type="text" name="empName" value='<%=employeeName %>' readonly="true" tabindex="3">
					
						<input type="hidden" name="empserialNO" value='<%=pfID %>' readonly="true" tabindex="3" />
						<input type="hidden" name="chk_frmblkprint" value="false" />
						<input type="hidden" name="chkpfid" />
						<input type="hidden" name="dateofJoining" value='<%=doj %>' />
						<input type="hidden" name="PCReportVerified" />
					
						<% }else{
						%>
						<input type="text" name="empName" readonly="true" tabindex="3">
						<img src="<%=basePath%>/PensionView/images/search1.gif" onclick="popupWindow('<%=basePath%>PensionView/PensionContributionMappedListWithoutTransfer.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
						<input type="hidden" name="empserialNO" readonly="true" tabindex="3" />
						<input type="hidden" name="chk_frmblkprint" value="false" />
						<input type="hidden" name="chkpfid" />
						<input type="hidden" name="dateofJoining" />
						<input type="hidden" name="PCReportVerified" />
						<%} %>
					</td>

				</tr>
				<TR>
					<td  width="50%" class=label align="right" nowrap>
						Report Type:<font color=red>*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<SELECT NAME="select_reportType" style="width:88px" onChange="javascript:getAirports('pfid')">
							<option value="NO-SELECT">
								[Select One]
							</option>
							<option value="html">
								Html
							</option>
							<option value="ExcelSheet">
								Excel Sheet
							</option>
					     <option value="DBF">
								DBF
							</option>
						</SELECT>
					</td>
					 
				</TR>
					<tr>
							<td  width="50%" class="label" align="right">PF ID Printing List:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
							<td width="50%" >									
								<SELECT NAME="select_pfidlst" style="width:120px" >
								
							  	<option value='NO-SELECT' Selected>[Select One]</option>
	                       		
								</SELECT>
							</td>
						</tr>
				<tr>
				<tr>
					<td align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

				
				 
             	<tr>
					<td align="center" colspan="2">
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
						<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
						<input type="button" class="btn" name="bulk" value="Bulk Print" onclick="javascript:bulkreport('<%=session.getAttribute("userid")%>')">
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
