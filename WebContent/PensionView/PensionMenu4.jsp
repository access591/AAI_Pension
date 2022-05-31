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
<!--
.text {
	font: normal 12px Arial, Helvetica, sans-serif;
	color: #E4E4E4;
}

A.link {
	font: bold 12px Arial, Helvetica, sans-serif;
	color: #FFFFFF;
	text-decoration: none;
}

A.link:hover {
	color: #FFCC00;
	font: bold 12px Arial, Helvetica, sans-serif;
	text-decoration: none;
}

.box {
	background-image: url(./PensionView/images/table_top.gif);
	background-repeat: no-repeat;
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #FFFFFF;
	font-size: 12px;
}

.head2 {
	background-image: url(./PensionView/images/head_img.gif);
	background-repeat: no-repeat;
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #FFFFFF;
	font-size: 12px;
}

.line {
	border-right: 1px solid #233F53;
	border-bottom: 1px solid #233F53;
	border-left: 1px solid #233F53;
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	font-size: 12px;
}

.textfld {
	background-color: #FFFFFF;
	border: 1px solid #7F9DB9;
	width: 150px;
	font-family: Arial, Verdana, Tahoma, Helvetica, Arial, sans-serif;
	font-size: 12px;
	color: #333333;
}

.textfld1 {
	width: 154px;
	font-family: Arial, Verdana, Tahoma, Helvetica, Arial, sans-serif;
	font-size: 12px;
	color: #333333;
}

.button {
	height: 23px;
	width: 68px;
	border: 1px none #333333;
	background-image: url(images/button.gif);
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: bold;
	color: #FFFFFF;
}
-->
</style>
<head>
<TITLE>AAI</TITLE>
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
		<%if (session.getAttribute("usertype").equals("Admin")) {%>
			ddlevelsmenu.setup("ddtopmenubar", "topbar",'<%=basePath%>') //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
			<%}%>
		</script>
</head>
<body class="BodyBackground">
<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<%if (session.getAttribute("usertype").equals("Admin")) {

                %>
		<td align="left" valign="top">

		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<div id="ddtopmenubar" class="mattblackmenu"
					style="height:42px;background-image: url(<%=basePath%>PensionView/images/navbg.gif)">
				<ul>
					<li
						style="background-image: url(<%=basePath%>PensionView/images/img3.gif)">
					<a href="#" rel="ddsubmenu1" target="mainbody1">Personnel</a></li>

					<li><a href="#" rel="ddsubmenu2">Finance</a></li>
					<li><a href="#" rel="ddsubmenu3">Employee Personal Info</a></li>
					<li><a href="#" rel="ddsubmenu4">Reports</a></li>

				</ul>
				</div>
				<!--Top Drop Down Persional Menu -->
				<ul id="ddsubmenu1" class="ddsubmenustyle">
					<li><a href="#">Masters</a>
					<ul>
						<li><a href="#">Employee</a>
						<ul>
							<li><a href="<%=basePath%>search1?method=getMaxCpfAccNo">New</a>
							</li>
							<li><a
								href="<%=basePath%>PensionView/PensionMasterSearch.jsp">Search</a>
							</li>
							<li><a
								href="<%=basePath%>PensionView/PensionMasterInvalidRecords.jsp">Employee
							Invalid Data</a></li>

						</ul>
						</li>


						<!--<li>
												<a href="#">Pension</a>
												<ul>
													<li>
														<a href="<%=basePath%>PensionView/PensionDataAdd.jsp">New</a>
													</li>
													<li>
														<a href="<%=basePath%>PensionView/PensionFundSearch.jsp">Search</a>
													</li>
												</ul>
											</li>-->

						<li><a href="#">Unit </a>
						<ul>
							<li><a href="<%=basePath%>PensionView/UnitMaster.jsp">New</a>
							</li>
							<li><a href="<%=basePath%>PensionView/UnitMasterSearch.jsp">Search</a>
							</li>
						</ul>
						</li>
						<li><a href="#">User </a>
						<ul>
							<li><a
								href="<%=basePath%>PensionLogin?method=getAaiCategory">New</a></li>
							<li><a href="<%=basePath%>PensionView/NewUserSearch.jsp">Search</a>
							</li>
							<li><a
								href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"
								target="mainbody1">Change Password</a></li>
							<li><a href="<%=basePath%>PensionView/LoginHistory.jsp">Login
							History</a></li>

							<li><a href="#">Access Rights</a></li>
						</ul>
						</li>

						<li><a href="#">Group </a>
						<ul>
							<li><a href="<%=basePath%>PensionView/UserGroupNew.jsp">New</a>
							</li>
							<li><a href="<%=basePath%>PensionView/UserGroupSearch.jsp">Search</a>
							</li>
							<li><a href="<%=basePath%>PensionLogin?method=groupassign">Assign
							Group</a></li>
						</ul>
						</li>
						<li><a href="#">Region </a>
						<ul>
							<li><a href="<%=basePath%>PensionView/RegionMaster.jsp">New</a>
							</li>
							<li><a href="<%=basePath%>PensionView/RegionSearch.jsp">Search</a>
							</li>
						</ul>
						</li>
					</ul>

					</li>

				</ul>



				<!--Top Drop Down Finance  Menu 2 -->

				<ul id="ddsubmenu2" class="ddsubmenustyle">
					<li><a href="#">Masters</a>
					<ul>
						<li><a href="#">Ceiling </a>
						<ul>
							<li><a href="<%=basePath%>PensionView/CeilingMaster.jsp">New</a>
							</li>
							<li><a
								href="<%=basePath%>PensionView/CeilingMasterSearch.jsp">Search</a>
							</li>
						</ul>
						</li>
						<li><a href="#">FinancialYear</a>
						<ul>
							<li><a href="<%=basePath%>csearch?method=getFinancialYear">New</a>
							</li>
							<li><a
								href="<%=basePath%>csearch?method=financialYearSearch">Search</a>
							</li>

						</ul>
						</li>
						<li><a href="#">Financial Detail</a>
						<ul>
							<li><a
								href="<%=basePath%>PensionView/AddFinancialDetail.jsp">New</a></li>
							<li><a href="<%=basePath%>PensionView/FinanceDataSearch.jsp">Search</a>
							</li>

						</ul>
						</li>
						<li><a href="<%=basePath%>PensionView/ArrearsSearchScreen.jsp">Arrears Data </a></li>
						<li><a href="<%=basePath%>PensionView/EmpPfcardEdit.jsp">Final
						Settlement Edit</a></li>

					</ul>

					</li>

					<li><a href="<%=basePath%>/PensionView/ImportFormats.jsp">Download
					Standard Formats For CPF Data </a></li>
					<li><a
						href="<%=basePath%>validatefinance?method=loadimportedprocess">Import
					CPF Data</a></li>
					<li><a href="<%=basePath%>PensionView/PensionValidation.jsp">Pension
					Validation</a></li>
					<li><a
						href="<%=basePath%>pfinance?method=loadmonthlycpfdatasearch">Monthly
					CPF Data (%)Deviation Statement</a></li>
					<li><a href="<%=basePath%>pfinance?method=loadtransfersearch">Missing
					/ New employees deviation statement </a></li>
					<li><a
						href="<%=basePath%>pfinance?method=loadyearlyempcountsearch">Year
					CPF Data Deviation Statement</a></li>
					<li><a href="#">Arrear Finance Data Mapping</a>
					<ul>
						<li><a
							href="<%=basePath%>PensionView/UniquePensionNumberGeneration.jsp">New</a>
						</li>
						<!-- <li>
														<a href="<%=basePath%>PensionView/UniquePensionNumberSearch.jsp">PensionNumberSearch</a>
													</li> -->
						<li><a
							href="<%=basePath%>PensionView/UniquePensionNumberSearch.jsp">Search</a>
						</li>
						<li><a href="<%=basePath%>aaiepfreportservlet?method=loadAdj">Adj
						in CPFRecoveries</a></li>
						<!--   <li>
														<a href="<%=basePath%>PensionView/PensionHistory.jsp">PensionHistory</a>
													</li> 	-->

					</ul>
					</li>
					<li><a href="#"> Finance Data Mapping</a>
					<ul>
						<li><a href="<%=basePath%>PensionView/FinanceDataMapping.jsp">Search</a>
						</li>
					</li>

				</ul>
				</ul>
				<ul id="ddsubmenu3" class="ddsubmenustyle">
					<li><a href="#">PF ID </a>
					<ul>



						<li>
						<%if(session.getAttribute("userid").equals("NAGARAJ")){%> <a
							href="<%=basePath%>psearch?method=loadAdd">New</a> <%}else{%> <a
							href="javascript:alert('Permissions Denied to Create New PFID')">New</a>
						<%} %>
						</li>
						<li><a href="<%=basePath%>psearch?method=loadPerMstr">Search</a>
						<li><a
							href="<%=basePath%>PensionView/VerificationofPCReport.jsp">Verification
						of PC Report</a></li>

					</ul>
					<!-- 		<li>
										<a href="<%=basePath%>psearch?method=getPerData">Import Form-3-2007<br> Personal Info </a>
									</li>-->
				</ul>
				<ul id="ddsubmenu4" class="ddsubmenustyle">
					<li><a href="<%=basePath%>reportservlet?method=loadform3">Forms
					</a> <!-- <li><a href="<%=basePath%>PensionView/PensionMasterSearchComparativeSt.jsp">Comparative Statements </a> -->
					</li>
					<li><a href="#">AAI EPF Forms</a>
					<ul>

					<a href="<%=basePath%>aaiepfreportservlet?method=loadob">AAI
						EPF-1</a>
					</li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf2">AAI
					EPF-2</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3">AAI
					EPF-3</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf5">AAI
					EPF-5</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf6">AAI
					EPF-6</a></li>
					<li><a href="<%=basePath%>aaiepfreportservlet?method=loadepf7">AAI
					EPF-7</a></li>
					<li><a
						href="<%=basePath%>aaiepfreportservlet?method=loadEPF-8">AAI
					EPF-8</a></li>
					<li><a
						href="<%=basePath%>aaiepfreportservlet?method=loadepf11">AAI
					EPF-11</a></li>
					<li><a
						href="<%=basePath%>aaiepfreportservlet?method=loadepf12">AAI
					EPF-12</a></li>



				</ul>
				</li>

				<li><a href="<%=basePath%>reportservlet?method=loadform3params">Form-3</a>
				</li>
				</li>
				<li><a href="<%=basePath%>reportservlet?method=loadrpfcform4">Form-4</a>
				</li>
				<li><a href="<%=basePath%>reportservlet?method=loadrpfcform5">Form-5</a>
				</li>
				<li><a
					href="<%=basePath%>reportservlet?method=loadpersonalreport">Employee
				Info -Death/Retd./Resigned </a></li>

				<li><a href="<%=basePath%>reportservlet?method=loadFinContri">Pension
				Contribution </a></li>
				<li><a href="<%=basePath%>reportservlet?method=loadForm6Cmp">Form-6
				Comprehensive</a></li>
				<li><a href="<%=basePath%>reportservlet?method=loadrpfcform6a">Form-6A</a></li>
				<li><a href="<%=basePath%>reportservlet?method=loadform7Input">Form-7
				PS</a></li>
				<li><a href="<%=basePath%>reportservlet?method=loadform8params">Form-8</a>
				</li>
				<li><a href="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput">Statement Of wages & Pension Contri.
				</a></li>
				<li><a href="<%=basePath%>reportservlet?method=payArrears">Pay Arrears (DBF-RPFC)
				</a></li>
				<li><a href="<%=basePath%>psearch?method=loadForm10D">Form-10D</a>
				</li>
				<li><a href="<%=basePath%>reportservlet?method=loadpfcardInput">PF
				Card</a></li>
				<li><a href="<%=basePath%>search1?method=comparativestatement">Comparative
				Statements </a></li>
				<li><a
					href="<%=basePath%>reportservlet?method=loadtrustpcInput">Trust
				Wise PCReport </a></li>
				<li><a	href="<%=basePath%>reportservlet?method=loadTransferINOUT">Transfer In/Out Report </a></li>
				<li><a href="#">Processing</a>
				<ul>
					<li><a href="<%=basePath%>reportservlet?method=loadob">OB
					Process </a></li>
					<li><a href="<%=basePath%>reportservlet?method=loadUpdatePC">Adj
					Bal Process </a></li>
					<li><a
						href="<%=basePath%>reportservlet?method=loadUpdatePensionContribut">Pensnion
					Contri. Process </a></li>
<li><a
						href="<%=basePath%>reportservlet?method=PFCardProc">PFCard Process</a></li>
				</ul>
				</li>
				</td>
			</tr>
		</table>
		</td>
		<%}%>

		<td align="left" valign="top">
		<table width="100%" height="42" border="0" cellpadding="0"
			cellspacing="0"
			background="<%=basePath%>PensionView/images/navbg.gif">
			<tr>
				<td align="right" valign="middle">
				<table width="350" height="26" border="0" cellpadding="0"
					cellspacing="0"
					background="<%=basePath%>PensionView/images/img2.gif">
					<tr>
						<td width="5" align="left" valign="top"><img
							src="<%=basePath%>PensionView/images/img1.gif" width="5"
							height="26" /></td>
						<td width="130" align="center" valign="middle" class="text">User:
						<%=session.getAttribute("userid")%></td>
						<td width="2" align="left" valign="middle"><img
							src="<%=basePath%>PensionView/images/divider.gif" width="2"
							height="16" /></td>
						<td width="208" align="center" valign="middle" class="text">LoginTime
						: <%=session.getAttribute("currDateTime")%>&nbsp;</td>
						<td width="5" align="right" valign="top"><img
							src="<%=basePath%>PensionView/images/img3.gif" width="5"
							height="26" /></td>
					</tr>
				</table>
				</td>

				<td width="84" align="right" valign="middle">
				<table width="84" height="26" border="0" cellpadding="0"
					cellspacing="0"
					background="<%=basePath%>PensionView/images/img2.gif">
					<tr>
						<td width="5" align="left" valign="top"><img
							src="<%=basePath%>PensionView/images/img1.gif" width="5"
							height="26" /></td>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img
							src="<%=basePath%>PensionView/images/password.gif"
							alt="Change Password" width="18" height="18" border="0" /></a></td>
						<%if(session.getAttribute("usertype").equals("Admin")){%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else if(session.getAttribute("usertype").equals("User")) {%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=home"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else{%><td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu3.jsp"><img
							src="<%=basePath%>PensionView/images/home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}%>
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


