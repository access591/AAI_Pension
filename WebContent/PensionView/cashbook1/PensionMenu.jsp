<%StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");
			basePathBuf.append("PensionView").append("/");
String basePath = basePathBuf.toString();
String usertype = "Admin";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<style type="text/css" type="" type="">
<!--

.text {
	font: normal 12px Arial, Helvetica, sans-serif;
	color: #E4E4E4;
}
A.link {
	font: bold 12px Arial, Helvetica, sans-serif;
	color:#FFFFFF;
	text-decoration:none;
}
A.link:hover {
	color: #FFCC00;
	font: bold 12px Arial, Helvetica, sans-serif;
	text-decoration:none;
}
.box {
	background-image: url(./view/images/table_top.gif);
	background-repeat: no-repeat;
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	color: #FFFFFF;
	font-size: 12px;
}
.head2 {
	background-image: url(./view/images/head_img.gif);
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
	background-color:#FFFFFF;
	border:1px solid #7F9DB9;
	width:150px;
	font-family: Arial, Verdana, Tahoma, Helvetica, Arial, sans-serif;
	font-size: 12px;
	color: #333333;
}
.textfld1 {
	width:154px;
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
	</head>
	<head>
		<title>AAI</title>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/ddlevelsmenu-base.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/ddlevelsmenu-topbar.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/ddlevelsmenu-sidebar.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/aai.css" />
		<script type="text/javascript" src="<%=basePath%>scripts/ddlevelsmenu.js" type="" type="">
		</script>
		<script type="text/javascript" type="" type="">
		<%if (usertype.equals("Admin")) {%>
			ddlevelsmenu.setup("ddtopmenubar", "topbar",'<%=basePath%>') //ddlevelsmenu.setup("mainmenuid", "topbar|sidebar")
			<%}%>
		</script>
	</head>
	<body class="BodyBackground1">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>

				<td align="left" valign="top">

					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td id="ddtopmenubar" class="mattblackmenu" style="height:42px;;background-image: url(<%=basePath%>images/navbg.gif);">
								<ul>
									<li style="background-image: url(<%=basePath%>images/img3.gif)">
										<a href="#" rel="ddsubmenu1">Masters</a>
									</li>
									<li>
										<a href="#" rel="ddsubmenu2">Forms</a>
									</li>
									<li>
										<a href="#" rel="ddsubmenu3">Reports</a>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td >
								<!--Top Drop Down Masters Menu -->
								<ul id="ddsubmenu1" class="ddsubmenustyle">
									<li>
										<a href="#">Account Code Master</a>
										<ul>
											<li>
												<a href="<%=basePath%>cashbook/AccountingCode.jsp">New</a>
											</li>
											<li>
												<a href="<%=basePath%>cashbook/AccountingCodeSearch.jsp">Search</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="#">Bank Master</a>
										<ul>
											<li>
												<a href="<%=basePath%>cashbook/BankMaster.jsp">New</a>
											</li>
											<li>
												<a href="<%=basePath%>cashbook/BankMasterSearch.jsp">Search</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="#">Bank Opening Balance</a>
										<ul>
											<li>
												<a href="<%=basePath%>cashbook/BankOpeningBal.jsp">New</a>
											</li>
											<li>
												<a href="<%=basePath%>cashbook/BankOpeningBalSearch.jsp">Search</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="#">Party Master</a>
										<ul>
											<li>
												<a href="<%=basePath%>cashbook/PartyMaster.jsp">New</a>
											</li>
											<li>
												<a href="<%=basePath%>cashbook/PartyMasterSearch.jsp">Search</a>
											</li>

										</ul>
									</li>
								</ul>
								<!--Top Drop Down Forms Menu -->
								<ul id="ddsubmenu2" class="ddsubmenustyle">
									<li>
										<a href="#">Vouchers</a>
										<ul>
											<li>
												<a href="<%=basePath%>cashbook/Voucher.jsp">New</a>
											</li>
											<li>
												<a href="<%=basePath%>cashbook/VoucherSearch.jsp">Search</a>
											</li>
										</ul>
									</li>
									<li>
										<a href="<%=basePath%>cashbook/VoucherApproval.jsp">Voucher Approvals</a>
									</li>
								</ul>
								<!--Top Drop Down Reports Menu -->
								<ul id="ddsubmenu3" class="ddsubmenustyle">
									<li>
										<a href="<%=basePath%>cashbook/BankBookParam.jsp">Bank Book</a>
									</li>
								</ul>
							</td>
						</tr>
					</table>
				</td>

				<td align="left" valign="top">
					<table width="100%" height="42" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>images/navbg.gif">
						<tr>
							<td align="right" valign="middle">
								<table width="350" height="26" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>images/img2.gif">
									<tr>
										<td width="5" align="left" valign="top">
											<img src="<%=basePath%>images/img1.gif" width="5" height="26" alt="" src="" alt="" src="" alt="" />
										</td>
										<td width="130" align="center" valign="middle" class="text">
											User: Navayuga
										</td>
										<td width="2" align="left" valign="middle">
											<img src="<%=basePath%>images/divider.gif" width="2" height="16" alt="" src="" alt="" src="" alt="" />
										</td>
										<td width="208" align="center" valign="middle" class="text">
											LoginTime :&nbsp;
										</td>
										<td width="5" align="right" valign="top">
											<img src="<%=basePath%>images/img3.gif" width="5" height="26" alt="" src="" alt="" src="" alt="" />
										</td>
									</tr>
								</table>
							</td>

							<td width="84" align="right" valign="middle">
								<table width="84" height="26" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>images/img2.gif">
									<tr>
										<td width="5" align="left" valign="top">
											<img src="<%=basePath%>images/img1.gif" width="5" height="26" alt="" src="" alt="" src="" alt="" />
										</td>
										<td width="24" align="center" valign="middle">
											<a href="<%=basePath%>PensionLogin?method=changepassword&amp;flag=checkusername"><img src="<%=basePath%>images/password.gif" alt="Change Password" width="18" height="18" border="0" src="" alt="" src="" alt="" /></a>
										</td>

										<td width="24" align="center" valign="middle">
											<a href="<%=basePath%>PensionMenu.jsp"><img src="<%=basePath%>images/home.gif" alt="Home" width="18" height="18" border="0" src="" alt="" src="" alt="" /></a>
										</td>

										<td width="24" align="center" valign="middle">
											<a href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img src="<%=basePath%>images/logout.gif" alt="Logout" width="16" height="18" border="0" src="" alt="" src="" alt="" /></a>
										</td>
										<td width="5" align="right" valign="top">
											<img src="<%=basePath%>images/img3.gif" width="5" height="26" alt="" src="" alt="" src="" alt="" />
										</td>
									</tr>
								</table>
							</td>
							<td width="10" align="left" valign="middle">
								<img src="<%=basePath%>images/spacer.gif" width="10" height="10" alt="" src="" alt="" src="" alt="" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>


