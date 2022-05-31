<%@ page language="java" 
	pageEncoding="UTF-8" errorPage="error.jsp"%>


<%String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("./PensionIndex.jsp");
                rd.forward(request, response);
            }%>

<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

<head>

<title>AAI</title>
   <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />
     <link rel="stylesheet" href="<%=basePath%>PensionView/css/pmenubar.css" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-topbar.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-sidebar.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/aai.css" />
<script type="text/javascript"
	src="<%=basePath%>PensionView/scripts/ddlevelsmenu.js"> </script>
<script type="text/javascript">
		<%if (session.getAttribute("usertype").equals("Admin")) {%>
			ddlevelsmenu.setup("ddtopmenubar", "topbar",'<%=basePath%>') //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
			<%}%>
			
			
	function LoadDashBoard(param){
	 //alert(param);
	if (param!="null"&&param=="Y"){
	
	var swidth=screen.Width;
 	var sheight=screen.Height;
	var newParams ="<%=basePath%>reportservlet?method=dashBoard";
	winHandle = window.open(newParams,"Utility","menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
	winOpened = true;
	winHandle.window.focus();
	}
    }	
    
   function doAction(){
    var comfirmMsg = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
		if (comfirmMsg== true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}
		 var swidth=screen.Width-10;
 		 var sheight=screen.Height-150;
		var params = "&accessCode=PE040203&frmName=adjcorrections&frm_reportType="+reportType;
		var url="<%=basePath%>reportservlet?method=crtnMadeInPCReport"+params;
		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
   }			
		</script>
</head>
<body class="BodyBackground" onload=LoadDashBoard('<%=request.getParameter("DashBoardFlag")%>');>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	    <tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<td height="3" align="left" valign="top" bgcolor="#000066"><img	src="<%=basePath%>PensionView/images/spacer.gif" width="1"	height="1"></td>
					</tr>
				</table>
			 </td>
		</tr>
		
		   <tr>
        <td align="left" valign="top"><table width="100%" height="32" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/navbg.gif"">
          <tr>
          <%if (session.getAttribute("usertype").equals("Admin")) { %>
            <td align="left" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="92%" align="left" valign="middle">
						<div id="ddtopmenubar" class="mattblackmenu"
					style=background-image: url(./PensionView/images/navbg.gif)">
				<ul>
					<li>
					<%if(!userId.trim().equals("Anju")&& !userId.trim().equals("AUDITOR") && !userId.trim().equals("Monika") && !userId.trim().equals("GAGANDEEP")){%> 
					<li><a href="#" rel="ddsubmenu1">HR Dept</a></li>
					<li><a href="#" rel="ddsubmenu2">Data Porting</a></li>
					<li><a href="#" rel="ddsubmenu3">Remittance / Accretion</a></li>
					<li><a href="#" rel="ddsubmenu4">Data Correction & Impact Calculator</a></li>
					<li><a href="#" rel="ddsubmenu5">Reports</a></li>
					<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("SRFIN")) || (session.getAttribute("userid").equals("SHIKHA")) || (session.getAttribute("userid").equals("SANJEEVKUMAR")) || (session.getAttribute("userid").equals("RAVIKANOJIA")) ){%> 
					<li><a href="#" rel="ddsubmenu6">Final Accounts</a></li>
					<%}%>
					<%}else{
					if(userId.trim().equals("Monika")){%>
					<li><a href="#" rel="ddsubmenu1">HR Dept</a></li>
					<li><a href="#" rel="ddsubmenu3">Remittance / Accretion</a></li>
					<li><a href="#" rel="ddsubmenu4">Data Correction & Impact Calculator</a></li>
					<%}%>					
					<li><a href="#" rel="ddsubmenu5">Reports</a></li>
					<%} %>
				</ul>
								
				<!--Top Drop Down PersonalInfo  Menu 1-->
				<ul id="ddsubmenu1" class="ddsubmenustyle">
					<li><a href="#">PF ID </a>
					<ul>
						<li>
						<%if(session.getAttribute("userid").equals("NAGARAJ")){%> 
						<a href="<%=basePath%>psearch?method=loadAdd">New</a> <%}else{%> 
						<a href="javascript:alert('Permissions Denied to Create New PFID')">New</a>
						<%} %>
						</li>
						<li><a href="<%=basePath%>psearch?method=loadPerMstr">Search</a></li>													
					</ul>
				   </li>
				   	<%if(!session.getAttribute("userid").equals("Monika")){%>				
					<li><a href="<%=basePath%>psearch?method=verifypcrpt">Verification of PC Report</a></li>
					
					
					
					<li><a href="<%=basePath%>reportservlet?method=loadpersonalreport">EmployeeInfo -Death/Retd./Resigned </a></li>
		            <li><a href="<%=basePath%>reportservlet?method=loadTransferINOUT">Station Wise Employees In/Out Report </a></li>
		            <li><a href="<%=basePath%>reportservlet?method=loadfinalempscreen">Active/Inactive Employee Info</a></li>
		            <%if(session.getAttribute("userid").equals("navayuga") ||session.getAttribute("userid").equals("RKBATRA") ){%>
		            <li><a href="<%=basePath%>CadServlet?method=verifypcrpt" > Cad Report </a></li>
 		             <!--<li><a a href="#">User</a> 
 		             <ul> 
 		                <li><a href="<%=basePath%>PensionLogin?method=getuserProfile">Add</a></li> 
 						<li><a href="<%=basePath%>PensionLogin?method=userProfileSearch">Search</a></li>																		
 					</ul>  NEMISHSHRIMALI
 		             </li>		            
		             --><li><a href="<%=basePath%>PensionLogin?method=loadUserAccessRights">User Wise Access Rights</a></li>
		            <%}} %>
		             <li><a href="<%=basePath%>reportservlet?method=retirementEmployeesInfo">Monthly  Seperation EmployeeInfo</a></li>
		              <%if((session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI")) || (session.getAttribute("userid").equals("JBBISWAS")) || (session.getAttribute("userid").equals("navayuga"))){%>
		             <li><a href="<%=basePath%>psearch?method=loadFreshPenOption" onclick="alert('Access Denied');return false;">Pension Option - Revision Interface</a></li>
		             <li><a href="<%=basePath%>psearch?method=loadFreshPenOptionReports" >Pension Option - Revision Interface Reports</a></li>     
		                   <li><a href="#">Pension Claim Process</a>
		             <ul>
		             <li><a href="<%=basePath%>reportservlet?method=pensionProcessInfo">Request Submission </a></li>
		             <li> <a href="<%=basePath%>reportservlet?method=penprocessCHQHRInitial">CHQ HR Approval</a> </li> 
		             <li> <a href="<%=basePath%>reportservlet?method=penprocessCHQFinApprove">CHQ Fin Approval</a> </li> 
		      		 <li><a href="<%=basePath%>reportservlet?method=penprocessdatesub">Submission to RPFC</a></li>
		             <li><a href="<%=basePath%>reportservlet?method=penprocessdatereturn">Reject/Return from RPFC</a></li>
		            		             
		              
		                  </ul>
		             <%} %>
		     
		            		         
		         
				</ul>
				<!--Top Drop Down PersonalInfo  Menu1 END-->
				<!--Top Drop Down PersonalInfo  Menu2-->
				<ul id="ddsubmenu2" class="ddsubmenustyle">
				<li><a href="<%=basePath%>validatefinance?method=importformats">Download Standard Formats For CPF Data </a></li>
				<li><a	href="<%=basePath%>validatefinance?method=loadimportedprocess">Import CPF Data</a></li>
				<%if(session.getAttribute("userid").equals("navayuga")){%> 
				<li><a	href="<%=basePath%>validatefinance?method=loadimportedprocessnavayuga">Import CPF Data Navayuga</a></li>
				<%}%>
				<li><a href="<%=basePath%>search1?method=uniquepnnosearch">Arrear Finance Data Mapping Search</a></li>
					<!--<ul>
						<li><a href="<%=basePath%>search1?method=uniquepnnogeneration">New</a></li>
						<li><a href="<%=basePath%>search1?method=uniquepnnosearch">Search</a></li>
					</ul>-->
			    <li><a href="<%=basePath%>search1?method=financedatamapping">Finance Data Mapping</a></li>			    
			    	</ul>
				<!--Top Drop Down PersonalInfo  Menu2 END-->
				<!--Top Drop Down PersonalInfo  Menu3-->
				<ul id="ddsubmenu3" class="ddsubmenustyle">
				<%if(!session.getAttribute("userid").equals("Monika")){%>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3&page=remitencescreen">Remittance Screen</a></li>
				<%}%>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3&page=remitencereport">Remittance Report</a></li>
		 		<li><a href="<%=basePath%>aaiepfreportservlet?method=loadAccretionParam">Accretion Report</a></li>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadcpfAccretion">CPF Accretion Report</a></li>
				<!--new code Start --->
				<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI")) || (session.getAttribute("userid").equals("MANOJKUMARSARMA")) || (session.getAttribute("userid").equals("SANJAYBANSAL")) || (session.getAttribute("userid").equals("MANISHKUMARK")) || (session.getAttribute("userid").equals("GAYATHRIV")) ){%>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadStationWiseRemittance">Station Wise Remittance</a></li>
				<%}%>
				<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("INDU"))||(session.getAttribute("userid").equals("MANISHKK")) || (session.getAttribute("userid").equals("MANOJKUMARSARMA")) || (session.getAttribute("userid").equals("SANJEEVKUMAR")) || (session.getAttribute("userid").equals("AJAYKANOJIA")) ){%>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadStationWiseReport">Station Wise Remittance Report</a></li>
				<%}%>
<!--new code End --->
				</ul>				
				<!--Top Drop Down PersonalInfo  Menu3 END-->								
				<!--Top Drop Down PersonalInfo  Menu4-->
				<ul id="ddsubmenu4" class="ddsubmenustyle">
				<%if(session.getAttribute("userid").equals("navayuga") ||session.getAttribute("userid").equals("RKBATRA") || session.getAttribute("userid").equals("SEEMASETHI") || (session.getAttribute("userid").equals("AJAYKANOJIA")) || session.getAttribute("userid").equals("MALKEET")|| session.getAttribute("userid").equals("sunita") || session.getAttribute("userid").equals("GAYATHRIV") || session.getAttribute("userid").equals("VEENAJAWA")|| session.getAttribute("userid").equals("Monika")){%>
				<li><a href="<%=basePath%>search1?method=employeesearch">Edit Settlement Information</a></li>
				<%}%>
				<li><a href="<%=basePath%>search1?method=verifiedpfidlist">Diff on PC for FinalSettlement/Verified cases</a></li>
				<!--<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("GOPALPAL")) ||(session.getAttribute("userid").equals("NEMISHSHRIMALI")) || (session.getAttribute("userid").equals("SHIKHA")) || (session.getAttribute("userid").equals("SANJEEVKUMAR")) || (session.getAttribute("userid").equals("RAVIKANOJIA"))){%>
				<li><a	href="<%=basePath%>reportservlet?method=loadAdjObCrtn">Calculate AdjOb on Monthly CPF Corrections</a></li>
				<%}%>
				<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI")) ){%>
				<li><a href="<%=basePath%>aaiepfreportservlet?method=loadStationWiseRemittance">Station Wise Remittance</a></li>
				<%}%>
				-->
				<li><a href="#">Calculate AdjOb on Monthly CPF Corrections</a>
					<ul>
						<li><a	href="<%=basePath%>reportservlet?method=loadAdjObCrtn&accessCode=PE040201">Edit</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040202&searchFlag=S&frmName=adjcorrections">Approve</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040204&searchFlag=S&frmName=AdjCrtnApprovedSearch">Approved</a></li>
						<li><a href="#">CHQ Approver</a>
							<ul>
								<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE04020601&searchFlag=S&frmName=chqApprover">Edit</a></li>
						 		<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE04020602&searchFlag=S&frmName=chqApprover">Approved</a></li>
						
 						</ul>
 						</li>
						<li><a	href="<%=basePath%>reportservlet?method=getFrozenOrBlockedRecords&accessCode=PE040205&frmName=AdjCrtnFrznBlockSearch">Frozen/Blocked PFID Search</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=getImpacCalcLogReport&accessCode=PE040203&frmName=ImpacCalcLogReport">Impact CalC Log Report</a></li>
				  	 </ul>
				   </li>
				
				<li><a	href="<%=basePath%>reportservlet?method=load12MnthStatmntSearchForCrtn&accessCode=PE0407&frmName=form7/8psadjcrtn">12/60 Months Statement Corrections</a></li>
				<%if((session.getAttribute("userid").equals("navayuga")) || ((session.getAttribute("userid").equals("GOPALPAL"))) || (session.getAttribute("userid").equals("NEMISHSHRIMALI")) ){%>
				<li> <a href="<%=basePath%>PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Revised Form7/Form8 with Monthly DATA Corrections');return false"  >Revised Form7/Form8 with Monthly Data Corrections</a></li>
				 <%}%>
			
				<% if((session.getAttribute("userid").equals("SHIKHA")) || (session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("SANJEEVKUMAR")) || (session.getAttribute("userid").equals("RAVIKANOJIA"))){%>
				 <li><a	href="<%=basePath%>search1?method=pfcardSplRemarks">PFCard SPL Remarks</a></li>
				 <%}%>
				 <% if((session.getAttribute("userid").equals("RAJESH")) || (session.getAttribute("userid").equals("navayuga"))|| (session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI"))){%>
				 <li><a	href="<%=basePath%>pcreportservlet?method=revisionOptionPC">Revision Option PC Corrections</a></li>
				 <%}%>
				  <% if((session.getAttribute("userid").equals("navayuga"))|| (session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI")) ||(session.getAttribute("userid").equals("GAYATRIBHATNAGAR"))||(session.getAttribute("userid").equals("KUSUM"))  ){%>
				 	<li><a href="#">Impact Calculate For PC</a>
					<ul>
						<li><a	href="<%=basePath%>pcreportservlet?method=loadAdjObCrtnforPc&accessCode=PE040201">Edit</a></li>
						<li><a	href="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode=PE040202&searchFlag=S&frmName=adjcorrections">Approve</a></li>
						<li><a	href="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode=PE040204&searchFlag=S&frmName=AdjCrtnApprovedSearch">Approved</a></li>
				  	 </ul>
				   </li>
				   <%}%> 
				 <li><a	href="<%=basePath%>reportservlet?method=form4inputparam">Form4 Input Parameters</li> 
				 <%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("GOPALPAL")) || (session.getAttribute("userid").equals("NEMISHSHRIMALI"))|| (session.getAttribute("userid").equals("MANOJKUMARSARMA")) || (session.getAttribute("userid").equals("SANJAYBANSAL")) || (session.getAttribute("userid").equals("MANISHKUMARK"))  ){%>
				<li><a href="<%=basePath%>pcreportservlet?method=loadStationWiseRemittance">Form4 Report</a></li>
				<%}%>
				<li><a href="<%=basePath%>pcreportservlet?method=bifurcationReport">Bifurcation Report</a></li>
				</ul>
				
				<!--Top Drop Down PersonalInfo  Menu4 END-->
				<ul id="ddsubmenu5" class="ddsubmenustyle">			  
			  <li><a href="#">AAI EPF Forms</a>
				<ul>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadob">AAI EPF-1</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf2">AAI EPF-2</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3">AAI EPF-3</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf4">AAI EPF-4</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf5">AAI EPF-5</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf6">AAI EPF-6</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf7">AAI EPF-7</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadEPF-8">AAI EPF-8</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf11">AAI EPF-11</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf12">AAI EPF-12</a></li>
 				</ul>
				</li>
             <li><a href="#">RPFC Forms</a>
			    <ul>
				   <li><a href="<%=basePath%>reportservlet?method=loadform3params">Form-3</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform4">Form-4</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform5">Form-5</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadForm6Cmp">Form-6 Comprehensive</a></li>
				  
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform6a">Form-6A</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadform7Input">Form-7PS</a></li>
				   <li><a href="<%=basePath%>pcreportservlet?method=loadform7revisionopInput">Form-7Revision Option</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadform8params">Form-8</a>				   
				   <li><a href="<%=basePath%>reportservlet?method=payArrears">Pay Arrears (DBF-RPFC) </a></li>
				   <li><a href="<%=basePath%>psearch?method=loadForm10D">Form-10D</a></li>
				</ul>
			</li>
            <li><a href="<%=basePath%>reportservlet?method=loadFinContri">Pension Contribution Stmt</a></li>            
             <li><a href="<%=basePath%>reportservlet?method=loadRevPenContri">PCR For Revision Option</a></li>            
             <%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("RKBATRA"))){%>
				 <li><a href="<%=basePath%>cpfservlet?method=loadFinContri">CPF Claim Report </a></li>
				<%}%>	
			<li><a href="<%=basePath%>reportservlet?method=loadpfcardInput">PF Card</a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput">Statement Of wages & Pension Contri. </a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadrevisedstatmentpcwagesInput">Statement Of Wages & Pension Contri Revised. </a></li>
			<li><a href="<%=basePath%>pfinance?method=loadyearlyempcountsearch">PFID Wise No of Months Salary Drawn</a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadtrustpcInput">Trust Wise PCReport </a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadpcsummary">Annual Summary PCReport </a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadBlockedorFrozenInput">Duplicate Blocked/Adj Frozen PFIDs</a></li>				
			<%if(session.getAttribute("userid").equals("navayuga")){%> 
				<li><a href="#">Processing</a>
				<ul>
					<li><a href="<%=basePath%>reportservlet?method=loadob">OB Process </a></li>
					<li><a href="<%=basePath%>reportservlet?method=loadUpdatePC">Adj Bal Process </a></li>
					<li><a href="<%=basePath%>reportservlet?method=optionrevisedmulitple">Option Revised(Multiple PFIDS)Process</a></li>
					<li><a href="<%=basePath%>reportservlet?method=loadUpdatePensionContribut">Pensnion	Contri. Process </a></li>
					<li><a href="<%=basePath%>reportservlet?method=PFCardProc">PFCard Prasanthi Process</a></li>
					<li><a href="<%=basePath%>reportservlet?method=PFCardMailProc">PFCard Mail Process</a></li>
				</ul>
				</li>				  
				<%}%>
				<%if(session.getAttribute("userid").equals("navayuga")||session.getAttribute("userid").equals("BHATTACHARYA")){%> 
				
				<li><a href="<%=basePath%>aaiepfreportservlet?method=executive">Executives Report</a></li>	
						  
				<%}%>
				</ul>				 		 
				<%if((session.getAttribute("userid").equals("navayuga")) || (session.getAttribute("userid").equals("SRFIN")) || (session.getAttribute("userid").equals("SHIKHA")) || (session.getAttribute("userid").equals("SANJEEVKUMAR")) || (session.getAttribute("userid").equals("RAVIKANOJIA"))){%> 
				<!--Top Drop Down Final Accounts Menu -->			 
			   <ul id="ddsubmenu6" class="ddsubmenustyle">				 
				 	<li><a href="<%=basePath%>aaiepfreportservlet?method=summaryReports">Control Accounts Summary</a></li>					 
				</ul>
				<%}%>
			
			</div>
				  </td>
				  <%}%> 
				<td>
				<table width="100%" height="32" border="0" cellpadding="0"	cellspacing="0">
			<tr>
			<td align="right" valign="middle">
			<table width="5"><tr></tr></table></td>
			<td width="84" align="right" valign="middle">
				<table width="84" height="26" border="0" cellpadding="0" cellspacing="0" >
					<tr>
						<td width="5" align="left" valign="top"></td>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img
							src="<%=basePath%>PensionView/images/password.gif"
							alt="Change Password" width="18" height="18" border="0" /></a></td>
						<%if(session.getAttribute("usertype").equals("Admin")){%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else if(session.getAttribute("usertype").equals("SUPER USER")) {%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=superuser"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else if(session.getAttribute("usertype").equals("NODAL OFFICER")||session.getAttribute("usertype").equals("User")) {%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=home"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						
						
						<%}	else if(session.getAttribute("usertype").equals("HRUser")){ 						
						%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu6.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>												
						<%}else if(session.getAttribute("usertype").equals("Employee")){ 						
						%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/EmployeeMenu.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>												
						<%}
						else{%><td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu3.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img
							src="<%=basePath%>PensionView/images/logout.gif" alt="Logout"
							width="16" height="18" border="0" /></a></td>
						<td width="5" align="right" valign="top"></td>
					</tr>
				</table>
			</td>
			</table>
			</td>
		</tr>
		</table>
				</td>
				</tr>
			</table>
			</td>
			</tr>
			</table>



</body>
</html>


