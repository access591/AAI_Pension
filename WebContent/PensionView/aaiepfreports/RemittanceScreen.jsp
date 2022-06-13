
<%@ page
	import="java.util.*,aims.common.CommonUtil,aims.service.FinancialService,aims.bean.EmployeeValidateInfo"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String[] year = {"2010", "2011", "2012","2013","2014","2015","2016","2017","2018","2019"};
	ArrayList remitanceList = new ArrayList();
	String region1 = "", airportcode = "", year1 = "", month = "", region = "", salaryMonth = "", selSalaryMonth = "", remitanceType = "";
	ArrayList remitancetableList = new ArrayList();
	ArrayList noofEmployeeList = new ArrayList();
	RemittanceBean rbean = new RemittanceBean();
	AaiEpfform3Bean epf3bean = new AaiEpfform3Bean();
	DecimalFormat df = new DecimalFormat("#########0");
	String accountType = "";
	String[] remitanceTypeList = {"aaiepf3", "aaiepf3Suppl","aiepf3Arr"};
	double epf8Totals = 0.00;
	if (request.getAttribute("region") != null) {
		region1 = (String) request.getAttribute("region");
	}
	if (request.getAttribute("airportcode") != null) {
		airportcode = (String) request.getAttribute("airportcode");
	}
	if (request.getAttribute("remitanceType") != null) {
		remitanceType = (String) request.getAttribute("remitanceType");
	}

	if (request.getAttribute("year") != null) {
		year1 = (String) request.getAttribute("year");
	}
	if (request.getAttribute("month") != null) {
		month = (String) request.getAttribute("month");
	}
	if (request.getAttribute("salaryMonth") != null) {
		salaryMonth = (String) request.getAttribute("salaryMonth");
		selSalaryMonth = (String) request.getAttribute("salaryMonth");
		String date1[] = selSalaryMonth.split("-");
		selSalaryMonth = date1[1] + "-" + date1[2];
	}
	if (request.getAttribute("remitancetableList") != null) {
		remitancetableList = (ArrayList) request
				.getAttribute("remitancetableList");
		if (remitancetableList.size() > 0) {
			for (int i = 0; i < remitancetableList.size(); i++) {
				rbean = (RemittanceBean) remitancetableList.get(i);

			}
		}
	}

	if (request.getAttribute("epf8Totals") != null) {
		epf8Totals = Double.parseDouble(request.getAttribute(
				"epf8Totals").toString());
	}
	if (request.getAttribute("accountType") != null) {
		accountType = request.getAttribute("accountType").toString();
	}

	if (request.getAttribute("remitanceList") != null) {
		remitanceList = (ArrayList) request
				.getAttribute("remitanceList");

		AaiEpfform3Bean epfForm3Bean = new AaiEpfform3Bean();
		int srlno = 0;
		double diff = 0, grandDiffe = 0, epfaccretion = 0;
		for (int cardList = 0; cardList < remitanceList.size(); cardList++) {
			epfForm3Bean = (AaiEpfform3Bean) remitanceList
					.get(cardList);
			if (remitanceType.equals("aaiepf3")) {
				epfaccretion = Double.parseDouble(epfForm3Bean
						.getEpf3Accretion().toString())
						- epf8Totals;
			} else {
				epfaccretion = Double.parseDouble(epfForm3Bean
						.getEpf3Accretion().toString());
				epf8Totals = 0;
			}
			System.out.println(accountType);
			if(!accountType.equals("RAU")){
			rbean.setEmoluments(Math
					.round(Double.parseDouble(epfForm3Bean
							.getEmppfstatury()) * 100 / 12));
			rbean.setPfAccretion(epfaccretion);
			rbean.setPcEmoluments(Math.round(Double
					.parseDouble(epfForm3Bean
							.getPensionContriEmoluments())));
			rbean.setPensionContribution(Math
					.round(Double.parseDouble(epfForm3Bean
							.getPensionContribution())));
		}}
	}
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
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>

<script type="text/javascript"><!--
  var xmlHttp;
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
		function getAirports(param){	
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID;
		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
	 if(yearID=='NO-SELECT'){
				alert('Please Select Year');
				document.forms[0].select_year.focus();
				document.forms[0].select_region.value="";
				return false;
			}
	 if(document.forms[0].select_from_month.selectedIndex>0){
			monthID=document.forms[0].select_from_month.options[document.forms[0].select_from_month.selectedIndex].value;
		}else{
			monthID=document.forms[0].select_from_month.value;
		}
		if(monthID=='NO-SELECT'){
			alert('Please select  Month');
			document.forms[0].select_from_month.focus();
			return false;
		}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		 if(regionID==""){
				alert('Please Select Region');
				document.forms[0].select_region.focus();
				return false;
			}
	      var accounttype=document.forms[0].accounttype.value;
		 createXMLHttpRequest();	
		 if(document.forms[0].select_airport.length>1){
				airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				airportcode=document.forms[0].select_airport.value;
			}
			
			
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getAAIEPFAirports&region="+regionID+"&year="+yearID+"&accounttype="+accounttype;
            xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}
		xmlHttp.send(null);
		
        if(accounttype=="RAU"){
		getData();
		  }
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
	 remitance.style.display="none";
	 
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
    // document.forms[0].remitancetype[document.forms[0].remitancetype.options.selectedIndex].text=remitanceType;
	 
	 }

	 function getData(){
		 remitance.style.display="none";
			   if(document.forms[0].select_year.selectedIndex>0){
				yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
				}else{
				yearID=document.forms[0].select_year.value;
				}
			
		   if(yearID=='NO-SELECT'){
				alert('Please select FromYear');
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
			
			if(document.forms[0].select_from_month.selectedIndex>0){
			monthID=document.forms[0].select_from_month.options[document.forms[0].select_from_month.selectedIndex].value;
		    }else{
			monthID=document.forms[0].select_from_month.value;
		   }
		  if(monthID=='NO-SELECT'){
			alert('Please Select Month');
			document.forms[0].select_from_month.focus();
			return false;
		  }
		  var remitanceType=document.forms[0].remitancetype.value;
		 params = "&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcode="+airportcode+"&remitancetype="+remitanceType;
	 	 url = "<%=basePath%>aaiepfreportservlet?method=remitancescreen"+params;
		 document.forms[0].action=url;
		 document.forms[0].method="post";
		 document.forms[0].submit();
		// remitance.style.display="block";
		 }
	 function submitData(){
		
		 var salmonth='<%=selSalaryMonth%>';
				
		 if(document.forms[0].vrno.value==""){
	         alert("Please Enter the Voucher No ");
	         document.forms[0].vrno.focus();
	         return false;
			 }
		 if(document.forms[0].vrdt.value==""){
         alert("Please Enter the Voucher Date ");
         document.forms[0].vrdt.focus();
         return false;
		 }
		 var remitanceInput=document.forms[0].vrdt.value;
		var elem = remitanceInput.split('-'); 
		remitanceInput=elem[1]+"-"+elem[2];
		/* if(salmonth!=remitanceInput){
			  alert("Voucher Date should match with Remittance MonthYear ");
		         document.forms[0].vrdt.focus();
		         return false;
		 }*/

		 var date1=document.forms[0].vrdt;
		 var val1=convert_date(date1);		   	   
   		    if(val1==false)
   		     {
   		      return false;
   		     }
		 var epf3accretion=document.forms[0].epf3accretion.value;
		 var pfaccretion=document.forms[0].pfaccretion.value;
		 if(parseFloat(epf3accretion)!=parseFloat(pfaccretion)){
	         alert("EPF-3 EPFAccertion "+epf3accretion+" is Not Matching with PFAccretion "+pfaccretion+" Entered");
	         document.forms[0].pfaccretion.focus();
	         return false;
			 }
		 if(document.forms[0].pfremitterbanknm.value==""){
	         alert("Please Enter the PF Remitter Bank Name ");
	         document.forms[0].pfremitterbanknm.focus();
	         return false;
			}
		 if(document.forms[0].pfremitterbankacno.value==""){
	         alert("Please Enter the PF Remitter Bank A/C No ");
	         document.forms[0].pfremitterbankacno.focus();
	         return false;
			}
		 
	   var epf3pfNoofemployees=document.forms[0].pfNoEmp.value;
	   //alert(epf3pfNoofemployees);
	   var pfnoofemployees=document.forms[0].pfnoofemployees.value;
	   if(pfnoofemployees!=epf3pfNoofemployees){
		   alert("PFNoofEmployees Should Match With Epf3 PFNo of Employees "+epf3pfNoofemployees);
		   document.forms[0].pfnoofemployees.focus();
		   return false;
	   }
	   
		 if(document.forms[0].pfaccretion.value=="0.0"){
		      alert("Please Enter the PFAccretion Amount");
		      document.forms[0].pfaccretion.focus();
		      return false;
			 }
		
		 
		 if(document.forms[0].emoluments.value=="0.0"){
	      alert("Please Enter the PF Emoluments Amount");
	      document.forms[0].emoluments.focus();
	      return false;
		 }
		 var epf3Emoluments=document.forms[0].epf3Emoluments.value;
		 var pfemoluments=document.forms[0].emoluments.value;
		 if(parseFloat(epf3Emoluments)!=parseFloat(pfemoluments)){
         alert("EPF-3 Total Emoluments "+epf3Emoluments+" is Not Matching with PF Emoluments "+pfemoluments+" Entered");
         document.forms[0].emoluments.focus();
         return false;
		 }	
		 if(document.forms[0].inspectioncharges.value=="0.0"){
	       alert("Please Enter the Inspection Charges Amount");
	       document.forms[0].inspectioncharges.focus();
	       return false;
		 }
		 var epf3pcNoofemployees =document.forms[0].pcNoEmp.value;
		 //alert("epf3pcNoofemployees"+epf3pcNoofemployees);
		   var pcnoofemployees=document.forms[0].pcnoofemployees.value;
		   if(pcnoofemployees!=epf3pcNoofemployees){
			   alert("PCNoofEmployees Should Match With Epf3 PCNo of Employees "+epf3pcNoofemployees);
			   document.forms[0].pcnoofemployees.focus();
			   return false;
		   }
		var epf3pensionemoluments=document.forms[0].epf3pensionemoluments.value;
		var pensionemoluments=document.forms[0].pensionemoluments.value;
		 if(parseFloat(epf3pensionemoluments)!=parseFloat(pensionemoluments)){
	         alert("EPF-3 PensionContri Emoluments "+epf3pensionemoluments+" is Not Matching with PC Emoluments "+pensionemoluments+" Entered");
	         document.forms[0].pensionemoluments.focus();
	         return false;
			 }	
			
		 if(document.forms[0].pensioncontribution.value=="0.0"){
	      alert("Please Enter the Pension Contribution Amount ");
	      document.forms[0].pensioncontribution.focus();
	      return false;
		 }
		 var epf3PensionContri= document.forms[0].epf3pensioncontri.value;
		 var pensioncontribution= document.forms[0].pensioncontribution.value;
		 if(parseFloat(epf3PensionContri)!=parseFloat(pensioncontribution)){
	         alert("EPF-3 Total PensionContri "+epf3PensionContri+" is Not Matching with PensionContribution "+pensioncontribution+" Entered");
	         document.forms[0].pensioncontribution.focus();
	         return false;
			 }
		 if(document.forms[0].pcremitterbanknm.value==""){
		      alert("Please Enter the PensionContribution Remitter Bank Name ");
		      document.forms[0].pcremitterbanknm.focus();
		      return false;
			 }
		 if(document.forms[0].pcremitterbankacno.value==""){
		      alert("Please Enter the PensionContribution Remitter Bank A/C No. ");
		      document.forms[0].pcremitterbankacno.focus();
		      return false;
			 }
		 	 
		 <%if (remitancetableList.size() > 0) {%>
		 alert("Data Already Frozen for  <%=airportcode%> , <%=region1%> for the year <%=salaryMonth%>");
		 return false;
		 <%}%>
		 var url = "<%=basePath%>aaiepfreportservlet?method=remitancedataUpdate";
		 document.forms[0].action=url;
		 document.forms[0].method="post";
		 document.forms[0].submit();
	 }

	  function numsDotOnly()
		{
	  if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46) ||(event.keyCode>=44 && event.keyCode<=47))
	        {
	           event.keyCode=event.keyCode;
	        }
	        else
	        {
				 event.keyCode=0;
	        }
	  }
	  function getInspecCharges(){
		var emoluments=document.forms[0].emoluments.value;
		var inspectioncharges=Math.round(parseFloat(emoluments*.0018));
		document.forms[0].inspectioncharges.value=inspectioncharges;
	  }
	  function refreshFields(){
		  document.forms[0].select_region.value="";  
		  document.forms[0].select_airport.value="NO-SELECT";
		  
	  }
	  function editcal()
	    {
		var buttonname=document.forms[0].vrdt.value;
				
		 document.forms[0].vrdt.value=document.forms[0].vrdt.value,show_calendar('forms[0].vrdt');
							
		
	}
	  function getReport(){
			 var reportType="html";
			 url = "<%=basePath%>PensionView/aaiepfreports/BankPaymentVocher.jsp";

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
				function editcaldate()
			    {
				var buttonname=document.forms[0].billdate.value;
						
				 document.forms[0].billdate.value=document.forms[0].billdate.value,show_calendar('forms[0].billdate');						
				
			}
			function editdate()
			    {
				var buttonname=document.forms[0].chequedt.value;
						
				 document.forms[0].chequedt.value=document.forms[0].chequedt.value,show_calendar('forms[0].chequedt');
									
				
			}
	 
    --></script>
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
		<td>
		<table height="5%" align="center">
			<tr>
				<td class="ScreenHeading">REMITTANCE SCREEN</td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table height="15%" align="center">
			<tr>

				<td class="label" align="right">From Year / Month:<font
					color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><Select name='select_year' Style='width: 70px'>
					<option value='NO-SELECT'>Select One</option>

					<%
						for (int j = 0; j < year.length; j++) {
					%>
					<%
						if (year[j].equalsIgnoreCase(year1)) {
					%>
					<option value="<%=year1%>" <%out.println("selected");%>>
					<%=year1%></option>


					<%
						} else {
					%>
					<option value='<%=year[j]%>'><%=year[j]%></option>
					<%
						}
						}
					%>
				</SELECT> <select name="select_from_month" style="width: 60px"
					onchange="refreshFields();">
					<Option Value='NO-SELECT'>Select One</Option>

					<%
						while (monthIterator.hasNext()) {
							Map.Entry mapEntry = (Map.Entry) monthIterator.next();
							monthID = mapEntry.getKey().toString();
							monthNM = mapEntry.getValue().toString();
					%>
					<%
						if (monthID.equalsIgnoreCase(month)) {
					%>
					<option value="<%=monthID%>" <%out.println("selected");%>>
					<%=monthNM%></option>
					<%
						} else {
					%>
					<option value="<%=monthID%>"><%=monthNM%></option>
					<%
						}
						}
					%>
				</select></td>
			</tr>
			<tr>
				<td class="label" align="right">Remittance Type: <font
					color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><SELECT NAME="remitancetype" style="width: 134px"
					onchange="refreshFields();">
					<%
						for (int i = 0; i < remitanceTypeList.length; i++) {
							String remitacelist = remitanceTypeList[i];
							if (remitacelist.equals(remitanceType)) {
					%>
					<option value='<%=remitanceType%>' <%out.println("selected");%>><%=remitanceType%></option>
					<%
						} else {
					%>
					<option value='<%=remitacelist%>'><%=remitacelist%></option>
					<%
						}
						}
					%>
				</SELECT></td>
			</tr>
			<tr>
				<td class="label" align="right">Region / Account Type:<font
					color=red>*</font> &nbsp;&nbsp;</td>
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
				<td class="label" align="right">Aiport Name:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><SELECT NAME="select_airport" style="width: 134px"
					onchange="getData();">
					<option value='NO-SELECT' Selected>[Select One]</option>
				</SELECT></td>
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
		<%
			if (request.getAttribute("remitanceList") != null) {
				remitanceList = (ArrayList) request
						.getAttribute("remitanceList");
				AaiEpfform3Bean epfForm3Bean = new AaiEpfform3Bean();
				int srlno = 0;
				double diff = 0, grandDiffe = 0, epfaccretion = 0,totalVpf=0,totalPrinciple=0,totalInterest=0,totalPf=0,totalEpf3Accertion=0;
				double totalEmolumentsforPension=0,totalPensionContri=0,totalPfNoofEmp=0,totalPcNoofEmp=0,totalEmolumentsforEPF=0,totalInspectionCharges=0;
				// for noofemployeeList Loading
				noofEmployeeList = (ArrayList) request
						.getAttribute("noofEmployeesList");
				
				double totalEmoluments=0,totalEpf=0;
				if (remitanceList.size() > 1) {
					{
		%>
		<tr>
			<td>&nbsp;&nbsp;Grand&nbsp;Totals&nbsp;As&nbsp;Per AAIEPF-3</td>
			<td colspan="10"><font color="Red"></font> &nbsp;<b><font
				color="Red"><%=region1%></font></b></td>
		</tr>
		<tr>
			<td class="tbheader" colspan="10" align="right">Airport&nbsp;Code
			:&nbsp;<font color="Red"></font></td>
			<td class="tbheader" align="right">Emoluments</td>
			<td class="tbheader" align="right">EPF<br>
			(a)</td>
			<td class="tbheader" align="right">VPF<br>
			(b)</td>
			<td class="tbheader" align="right">Principle<br>
			(c)</td>
			<td class="tbheader" align="right">Interest<br>
			(d)</td>
			<td class="tbheader" align="right">PF <br>
			(e)</td>
			<td class="tbheader">Total Accretion<br>
			( a to e - f )</td>
			<td class="tbheader">Emoluments<br>
			For Pension</td>
			<td class="tbheader" align="right">PensionContri</td>
			<td class="tbheader" align="right">Pf No.<br>
			Employees</td>
			<td class="tbheader">Pc No.<br>
			Employees</td>
			<td class="tbheader">Emoluments<br>
			For EPF</td>
			<td class="tbheader">Inspection<br>
			Charges</td>
		</tr>
		<%
			}
					for (int cardList = 0; cardList < remitanceList.size(); cardList++) {
						epfForm3Bean = (AaiEpfform3Bean) remitanceList
								.get(cardList);
						epf3bean = (AaiEpfform3Bean) noofEmployeeList
								.get(cardList);
						if (remitanceType.equals("aaiepf3")) {
							epfaccretion = Double.parseDouble(epfForm3Bean
									.getEpf3Accretion().toString())
									- epf8Totals;
						} else {
							epfaccretion = Double.parseDouble(epfForm3Bean
									.getEpf3Accretion().toString());
							epf8Totals = 0;
						}
						totalEmoluments=totalEmoluments+Double.parseDouble(epfForm3Bean.getEmoluments());
						totalEpf=totalEpf+Double.parseDouble(epfForm3Bean.getEmppfstatury());
						totalVpf=totalVpf+Double.parseDouble(epfForm3Bean.getEmpvpf());
						totalPrinciple=totalPrinciple+Double.parseDouble(epfForm3Bean.getPrincipal());
						totalInterest=totalInterest+Double.parseDouble(epfForm3Bean.getInterest());
						totalPf=totalPf+Double.parseDouble(epfForm3Bean.getPf());
					    totalEpf3Accertion=totalEpf3Accertion+epfaccretion;
					    totalEmolumentsforPension=totalEmolumentsforPension+Double.parseDouble(epfForm3Bean
								.getPensionContriEmoluments());
					    totalPensionContri=totalPensionContri+Double.parseDouble(epfForm3Bean
								.getPensionContribution());
					    totalPfNoofEmp=totalPfNoofEmp+epf3bean.getEpf3PfNoofEmployees();
					    totalPcNoofEmp=totalPcNoofEmp+epf3bean.getEpf3PcNoofEmployees();
					    totalEmolumentsforEPF=totalEmolumentsforEPF+Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury()) * 100 / 12);
					    totalInspectionCharges=totalInspectionCharges+Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury()) * 100 / 12 * 0.0018);
		%>
		<tr>
			<td class="tbheader" colspan="10" align="right">&nbsp; <%=epfForm3Bean.getStation()%>
			</font>&nbsp;</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getEmoluments()))%></td>
			<td class="Data" nowrap="nowrap" align="right"><%=epfForm3Bean.getEmppfstatury()%></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getEmpvpf()))%></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPrincipal()))%></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getInterest()))%></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPf()))%></td>
			<td class="Data" align="right"><b><%=df.format(epfaccretion)%></b></font></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPensionContriEmoluments()))%></td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPensionContribution()))%></td>
			<td class="Data" align="right"><%=df.format(epf3bean.getEpf3PfNoofEmployees())%></td>
			<td class="Data" align="right"><%=df.format(epf3bean.getEpf3PcNoofEmployees())%></td>
			<td align="right" align="right"><%=Math.round(Double.parseDouble(epfForm3Bean
										.getEmppfstatury()) * 100 / 12)%>
			<td align="right" align="right"><%=Math
												.round(Double
														.parseDouble(epfForm3Bean
																.getEmppfstatury()) * 100 / 12 * 0.0018)%></td>
			
		</tr>
         
		<%
			}%>
      <tr>
      <td calss="tbheader" colspan="10" align="right">&nbsp;&nbsp;<b><font color="red">GrandTotals</font></td>
      <td class="Data" align="right"> <%=df.format(totalEmoluments) %></td>
      <td class="Data" align="right"> <%=df.format(totalEpf) %></td>
      <td class="Data" align="right"> <%=df.format(totalVpf) %></td>
      <td class="Data" align="right"> <%=df.format(totalPrinciple) %></td>
      <td class="Data" align="right"> <%=df.format(totalInterest) %></td>
      <td class="Data" align="right"> <%=df.format(totalPf) %></td>
      <td class="Data" align="right"> <%=df.format(totalEpf3Accertion) %></td>
      <td class="Data" align="right"> <%=df.format(totalEmolumentsforPension) %></td>
      <td class="Data" align="right"> <%=df.format(totalPensionContri) %></td>
      <td class="Data" align="right"> <%=df.format(totalPfNoofEmp) %></td>
      <td class="Data" align="right"> <%=df.format(totalPcNoofEmp) %></td>
      <td class="Data" align="right"> <%=df.format(totalEmolumentsforEPF) %></td>
      <td class="Data" align="right"> <%=df.format(totalInspectionCharges) %></td>
      <input type="hidden" name="epf3Emoluments" value="<%=df.format(totalEmoluments) %>">
	  <input type="hidden" name="epf3accretion" value="<%=df.format(totalEpf3Accertion) %>">
	  <input type="hidden" name="epf3pensionemoluments" value="<%=df.format(totalEmolumentsforPension)%>">
	  <input type="hidden" name="epf3pensioncontri" value="<%=df.format(totalPensionContri)%>">
      <input type="hidden" name="pcNoEmp" value="<%=df.format(totalPcNoofEmp)%>">
      <input type="hidden" name="pfNoEmp" value="<%=df.format(totalPfNoofEmp)%>">
          <%  
            System.out.println("totalEpf3Accertion"+totalEpf3Accertion);
            %>
      </tr>
     <%} else {
		%>
		<%
			for (int cardList = 0; cardList < remitanceList.size(); cardList++) {
						epfForm3Bean = (AaiEpfform3Bean) remitanceList
								.get(cardList);
						epf3bean = (AaiEpfform3Bean) noofEmployeeList
								.get(cardList);
						if (remitanceType.equals("aaiepf3")) {
							epfaccretion = Double.parseDouble(epfForm3Bean
									.getEpf3Accretion().toString())
									- epf8Totals;
						} else {
							epfaccretion = Double.parseDouble(epfForm3Bean
									.getEpf3Accretion().toString());
							epf8Totals = 0;
						}
		%>

		<tr>
			<td class="tbheader" colspan="10" align="right">
			<%
				if (remitanceType.equals("aaiepf3")) {
			%> Grand Totals As Per AAIEPF-3
			:&nbsp;<%
				} else {
			%> Grand Totals As Per AAIEPF-3 Supplimentory Data <%
				}
			%>
			<font color="Red"><%=epfForm3Bean.getRegion()%> &nbsp; - <%=epfForm3Bean.getStation()%>
			</font>&nbsp;</td>
		</tr>
		<tr>
			<font color="Yellow">
			<td class="tbheader" align="right">PF ACCRETION</td>
			</font>
			<td class="tbheader" align="right">Amount</td>
			<td class="tbheader" align="right">PENSION CONTRIBUTION</td>
			<td class="tbheader" align="right">Amount</td>
		</tr>
		<tr>
			<td align="right">Emoluments</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getEmoluments()))%></td>
			<td align="right">EMOLUMENTS FOR PENSION</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPensionContriEmoluments()))%></td>
		</tr>
		<tr>
			<td align="right">EPF &nbsp;(a)</td>
			<td class="Data" nowrap="nowrap" align="right"><%=epfForm3Bean.getEmppfstatury()%></td>
			<td align="right">PENSION CONTRIBUTION</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPensionContribution()))%></td>
		</tr>
		<tr>
			<td align="right">EMPVPF &nbsp;(b)</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getEmpvpf()))%></td>
			<td align="right">NO. OF EMPLOYEES</td>
			<td class="Data" align="right"><b><%=epf3bean.getEpf3PcNoofEmployees()%></b></font></td>
		</tr>
		<tr>
			<td align="right">Principle &nbsp;(c)</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPrincipal()))%></td>
			<td align="right" class="tbheader">&nbsp;INSPECTION CHARGES</td>
			<td class="tbheader">Amount</td>
		</tr>
		<tr>
			<td align="right">Interest &nbsp;(d)</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getInterest()))%></td>
			<td align="right">&nbsp;EMOLUMENTS FOR EPF</td>
			<td align="right"><%=Math.round(Double.parseDouble(epfForm3Bean
										.getEmppfstatury()) * 100 / 12)%></td>
		</tr>
		<tr>
			<td align="right">PF &nbsp;(e)</td>
			<td class="Data" align="right"><%=df.format(Double.parseDouble(epfForm3Bean
										.getPf()))%></td>
			<td align="right">INSPECTION CHARGES</td>
			<td align="right"><%=Math
												.round(Double
														.parseDouble(epfForm3Bean
																.getEmppfstatury()) * 100 / 12 * 0.0018)%></td>
		</tr>
		<tr>
			<td align="right">Epf8Totals &nbsp;(f)</td>
			<td class="data" align="right"><%=df.format(epf8Totals)%></td>
			<td align="right">NO. OF EMPLOYEES</td>
			<td class="Data" align="right"><b><%=epf3bean.getEpf3PfNoofEmployees()%></b></font></td>
		</tr>
		<tr>
			<td class="tbheader">TOTAL ACCRETION ( a to e - f )</td>
			<td class="Data" align="right"><b><%=df.format(epfaccretion)%></b></font></td>
		</tr>
		<tr>
			<td align="right">NO. OF EMPLOYEES</td>
			<td class="Data" align="right"><b><%=epf3bean.getEpf3PfNoofEmployees()%></b></font></td>

			<input type="hidden" name="epf3Emoluments" value="<%=Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury()) * 100 / 12)%>">
			<input type="hidden" name="epf3accretion" value="<%=epfaccretion%>">
			<input type="hidden" name="epf3pensionemoluments" value="<%=epfForm3Bean.getPensionContriEmoluments()%>">
			<input type="hidden" name="epf3pensioncontri" value="<%=epfForm3Bean.getPensionContribution()%>">
            <input type="hidden" name="pcNoEmp" value="<%=epf3bean.getEpf3PcNoofEmployees() %>">
            <input type="hidden" name="pfNoEmp" value="<%=epf3bean.getEpf3PfNoofEmployees() %>">
		</tr>


		<%
			}
				}
			} else {
		%>

		<tr>
			<td class="tbheader">There is No Records for the Selected Month.
			</td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		if (epf3bean != null
				&& request.getAttribute("remitanceList") != null) {
	%>
	<table>
		<tr>
			<td>&nbsp;</td>
		</tr>


		<%
			}
		%>
	</table>
	<table align="center">

	</table>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<div id="remitance">
		<table align="center" cellpadding=2 class="tbborder" cellspacing="0" border="">
			<tr class="tbheader">
				<td rowspan="2">VR.No<br />
				&nbsp;&nbsp;</td>
				<td rowspan="2">VR.Dt&nbsp;<br>
				(dd-Mon-YYYY)</td>
				<td colspan="4" align="center">PF Accretion</td>
				<td colspan="2" align="center">Inspection Charges</td>
				<td colspan="5" align="center">Pension Remittance</td>

			</tr>
			<tr class="tbheader">

				<td>PF Accretion&nbsp;&nbsp;</td>
				<td>Remitter Bank Name&nbsp;&nbsp;</td>
				<td>Remitter Bank A/C No.</td>
				<td>No. Of &nbsp;&nbsp;<br>
				Employees</td>
				<td>Emoluments&nbsp;&nbsp;</td>
				<td>Inspection Charges&nbsp;&nbsp;</td>
				<td>No.Of Employees&nbsp;&nbsp;</td>
				<td>Emoluments&nbsp;&nbsp;</td>
				<td>Pension Contribution&nbsp;&nbsp;</td>
				<td>Remitter Bank Name</td>
				<td>Remitter Bank A/C No.</td>
			</tr>
			<tr>


				<td><input type="text" name="vrno" value="<%=rbean.getVrNo()%>"
					style="width: 50px"></td>
				<td><input type="text" name="vrdt" value="<%=rbean.getVrDt()%>"
					style="width: 50px"><a onclick="editcal();"><img
					src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
				</td>
				<td><input type="text" name="pfaccretion"
					value="<%=rbean.getPfAccretion()%>" style="width: 60px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="pfremitterbanknm"
					value="<%=rbean.getPfRemitterBankNM()%>" style="width: 60px"></td>
				<td><input type="text" name="pfremitterbankacno"
					value="<%=rbean.getPfRemitterBankACNo()%>" style="width: 60px"></td>
				<td><input type="text" name="pfnoofemployees"
					value="<%=rbean.getPfnoofEmployees()%>" style="width: 60px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="emoluments" style="width: 70px"
					value="<%=df.format(rbean.getEmoluments())%>"
					onkeypress="numsDotOnly()" onblur="getInspecCharges();"></td>
				<td><input type="text" name="inspectioncharges"
					value="<%=rbean.getInspectionCharges()%>" style="width: 60px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="pcnoofemployees"
					value="<%=rbean.getPcNoofEmployees()%>" style="width: 60px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="pensionemoluments"
					value="<%=df.format(rbean.getPcEmoluments())%>" style="width: 60px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="pensioncontribution"
					value="<%=rbean.getPensionContribution()%>" style="width: 70px"
					onkeypress="numsDotOnly()"></td>
				<td><input type="text" name="pcremitterbanknm"
					value="<%=rbean.getPcRemitterBankNM()%>" style="width: 60px"></td>
				<td><input type="text" name="pcremitterbankacno"
					value="<%=rbean.getPcRemitterBankAcNo()%>" style="width: 60px"></td>
			</tr>
		</table>		
	<table align="center" cellpadding=2 class="tbborder" cellspacing="0" border="">
<tr class="tbheader">
				<td>Bill Ref.No</td>
               <td>Bill Date</td>
				<td>Cheque No.From</td>
				<td>Cheque No.To</td>
				<td>Date</td>
				<td>Prepared BY</td>
				<td>Checked BY</td>
				<td>Passed BY</td>
				<td>Received</td>				
</tr>
<tr>
  <td><input type="text" name="billrefno" value="<%=rbean.getBillRefno()%>" style="width: 60px"></td>
 <td><input type="text" name="billdate"	value="<%=rbean.getBilldate()%>" style="width: 60px"><a onclick="editcaldate();"><img
					src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a></td>
  <td><input type="text" name="chequenofrom" value="<%=rbean.getChequenofrom()%>" style="width: 100px"></td>
  <td><input type="text" name="chequenoto" value="<%=rbean.getChequenoto()%>" style="width: 90px"></td>
  <td><input type="text" name="chequedt" value="<%=rbean.getChequedt()%>" style="width: 60px"><a onclick="editdate();"><img
					src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a></td>
  <td><input type="text" name="preparedby" value="<%=rbean.getPreparedby()%>" style="width: 80px"></td>
  <td><input type="text" name="checkedby" value="<%=rbean.getCheckedby()%>" style="width: 80px"></td>
  <td><input type="text" name="passedby" value="<%=rbean.getPassedby()%>" style="width: 60px"></td>
  <td><input type="text" name="receivedby" value="<%=rbean.getReceivedby()%>" style="width: 60px"></td>
</tr>
</table>
</td>
	</tr>
	<table align="center">
		<tr>
		<td align="right">
       <input type="button" class="btn" name="Submit" value="Submit" onclick="submitData();">
       <input type="button" class="btn" name="Reset" value="Reset" onclick="getData()">
      <input type="button" class="btn" name="Submit" value="Cancel"	onclick="javascript:history.back(-1)">
<% if (rbean.getBillRefno()!="") {%>
 <input type="button" class="btn" name="BVReprt" value="BVReprt" onclick="getReport();"  alt="Click The Icon to get BankPaymentVocher Report"/>
 <% 
  session.setAttribute("remitancetableList", remitancetableList);
  session.setAttribute("remitanceList", remitanceList);
  session.setAttribute("salaryMonth", request.getAttribute("salaryMonth"));
  %>
<%} %></tr>
		</div>
	</table>
</table>
<input type="hidden" name="salarymonth" value="<%=salaryMonth%>">
<input type="hidden" name="region" value="<%=region1%>"> <input
	type="hidden" name="airportcode" value="<%=airportcode%>"></form>
<div id="process"
	style="position: fixed; width: auto; height: 35%; top: 200px; right: 0; bottom: 100px; left: 10em;"
	align="center"><img
	src="<%=basePath%>PensionView/images/Indicator.gif" border="no"
	align="middle" /> <SPAN class="label">Processing.......</SPAN></div>
</body>
</html>
