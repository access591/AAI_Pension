<%@ page language="java" import="java.util.*,java.lang.*"
	pageEncoding="UTF-8"%>
<%@page errorPage="error.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="bean" class="aims.service.PensionService"
	scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>

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
<style type="text/css">



</style>
<head>

<title>AAI</title>
   <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-topbar.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/ddlevelsmenu-sidebar.css" />
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>PensionView/css/aai.css">
<script type="text/javascript"
	src="<%=basePath%>PensionView/scripts/ddlevelsmenu.js"> </script>
<script type="text/javascript">
	<%if (session.getAttribute("usertype").equals("User")) {%>
			ddlevelsmenu.setup("ddtopmenubar", "topbar",'<%=basePath%>') ;
		<%}%>	
			
			
	
		</script>
</head>
<body class="BodyBackground">
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		
		<td align="left" valign="center">
		<table width="100%" height="42" cellpadding="0" cellspacing="0">
			<tr>
				<td nowrap>
				<div id="ddtopmenubar" class="mattblackmenu"
					style="height:42px; background-image: url(<%=basePath%>PensionView/images/navbg.gif)">
				<ul>
					<li
						style="background-image: url(<%=basePath%>PensionView/images/img3.gif)">
					
					<li><a href="#" rel="ddsubmenu1">HR Dept</a></li>
					<li><a href="#" rel="ddsubmenu2">Data Correction & Impact Calculator</a></li>
					<li><a href="#" rel="ddsubmenu3">Reports</a></li>
					
					
					
				</ul>
				</div>				
				<!--Top Drop Down PersonalInfo  Menu 1-->
				<ul id="ddsubmenu1" class="ddsubmenustyle">
					<li><a href="#">PF ID </a>
					<ul>
						
						<li><a href="<%=basePath%>psearch?method=loadPerMstr">Search</a></li>													
					</ul>
				   </li>
				   
				</ul>
			
				<ul id="ddsubmenu2" class="ddsubmenustyle">
				<li><a href="#">Calculate AdjOb on Monthly CPF Corrections</a>
					<ul>
						<li><a	href="<%=basePath%>reportservlet?method=loadAdjObCrtn&accessCode=PE040201">Edit</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040202&searchFlag=S&frmName=adjcorrections">Approve</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040204&searchFlag=S&frmName=AdjCrtnApprovedSearch">Approved</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=getFrozenOrBlockedRecords&accessCode=PE040205&frmName=AdjCrtnFrznBlockSearch">Frozen/Blocked PFID Search</a></li>
						<li><a	href="<%=basePath%>reportservlet?method=getImpacCalcLogReport&accessCode=PE040203&frmName=ImpacCalcLogReport">Impact CalC Log Report</a></li>
				  	 </ul>
				   </li>
				
			
				
				
				</ul>
				<!--Top Drop Down PersonalInfo  Menu4 END-->
				<ul id="ddsubmenu3" class="ddsubmenustyle">			  
			  <li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf" onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Click   to Verify Imported CPF Data.');return false" class="link">AAI - EPF - FORMS</a>
				
				</li>
             <li><a href="#">RPFC Forms</a>
			    <ul>
				   <li><a href="<%=basePath%>reportservlet?method=loadform3params">Form-3</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform4">Form-4</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform5">Form-5</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadForm6Cmp">Form-6 Comprehensive</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadrpfcform6a">Form-6A</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadform7Input">Form-7PS</a></li>
				   <li><a href="<%=basePath%>reportservlet?method=loadform8params">Form-8</a>				   
				   <li><a href="<%=basePath%>reportservlet?method=payArrears">Pay Arrears (DBF-RPFC) </a></li>
				   <li><a href="<%=basePath%>psearch?method=loadForm10D">Form-10D</a></li>
				</ul>
			</li>
            <li><a href="<%=basePath%>reportservlet?method=loadFinContri">Pension Contribution Stmt</a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadpfcardInput">PF Card</a></li>
			<li><a href="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput">Statement Of wages & Pension Contri. </a></li>
			
				  
				
				</ul>
				 		 
				
				</td>
			</tr>
		</table>
		</td>
		
		<td align="left" valign="center">
		<table width="100%" height="42" border="0" cellpadding="0"
			cellspacing="0"
			background="<%=basePath%>PensionView/images/navbg.gif">
			<tr>
			<td align="right" valign="middle">
			<table width="5"><tr></tr></table></td>
			<td width="84" align="right" valign="middle">
				<table width="84" height="26" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/img2.gif">
					<tr>
						<td width="5" align="left" valign="top"><img
							src="<%=basePath%>PensionView/images/img1.gif" width="5"
							height="26" /></td>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img
							src="<%=basePath%>PensionView/images/password.gif"
							alt="Change Password" width="18" height="18" border="0" /></a></td>
						
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu5.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img
							src="<%=basePath%>PensionView/images/logout.gif" alt="Logout"
							width="16" height="18" border="0" /></a></td>
						<td width="5" align="right" valign="top"><img
							src="<%=basePath%>PensionView/images/img3.gif" width="5"
							height="26" /></td>
					</tr>
				</table>
				</td>
				<td width="10" align="left" valign="middle"><img
					src="<%=basePath%>PensionView/images/spacer.gif" width="10"
					height="10" /></td>

			</tr>
		</table>
		</td>

	</tr>
</table>



</body>
</html>


