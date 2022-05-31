
<%@ page language="java" pageEncoding="UTF-8"%>
<%
String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }

StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");

String basePath = basePathBuf.toString() + "PensionView/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>" />
		<title>AAI - Cashbook - Master - Bank Opening Balance Master</title>
		<meta http-equiv="pragma" content="no-cache" content="" />
		<meta http-equiv="cache-control" content="no-cache" content="" />
		<meta http-equiv="expires" content="0" content="" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" content="" />
		<meta http-equiv="description" content="This is my page" content="" />
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/calendar.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/DateTime1.js" type=""></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" type="">
			function checkAccount(){
				if(document.forms[0].bankName.value == ""){
					alert("Please Enter Bank Name (Mandatory)");
					document.forms[0].bankName.focus();
					return false;
				}
				if(document.forms[0].openedDate.value == ""){
					alert("Please Enter Date (Mandatory)");
					document.forms[0].openedDate.focus();
					return false;
				}
				if(!convert_date(document.forms[0].openedDate)){
					return false;
				}
   		  		if(document.forms[0].amount.value == ""){
					alert("Please Enter Amount (Mandatory)");
					document.forms[0].amount.focus();
					return false;
				}
				if(!ValidateFloatPoint(document.forms[0].amount.value,15,4)){
					alert("Please Enter A Valid Amount (Mandatory)");
					document.forms[0].amount.focus();
					return false;
				}
			}
			function popupWindow(mylink, windowname){
				document.forms[0].bankName.value="";
				if (! window.focus){
					return true;
				}
				var href;
				if (typeof(mylink) == 'string'){
				   href=mylink;
				} else {
					href=mylink.href;
				}
				progress=window.open(href, windowname, 'width=750,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
				return true;
			}
			function bankDetails(bankName,accountNo){	
		   		document.forms[0].bankName.value=bankName;
		   		document.forms[0].accountNo.value=accountNo;
			}
		</script>
	</head>
	<body class="BodyBackground1" onload="document.forms[0].bankImage.focus();">
		<form name="account" action="<%=basePathBuf%>BankOpeningBal?method=addOpenBalRecord" onsubmit="javascript : return checkAccount()" method="post" action="">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									Bank Opening Balance[Add] &nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td height="10px">
								</td>
							</tr>
							<tr>
								<td height="15%">
									<table align="center" cellspacing="5">
										<tr>
											<td class="label" width="100px" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Name
											</td>
											<td align="left">
												<input type="text" name="bankName" size="18" maxlength="50" readonly="readonly" />
												<input type="hidden" name="accountNo" />
												<img style='cursor:hand' name="bankImage" src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/BankInfo.jsp?type=rem','AAI');" alt="Click The Icon to Select Bank Master Records" tabindex=1 />
											</td>
										</tr>
										<tr>
											<td class="label" width="100px" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Date
											</td>
											<td align="left">
												<input name='openedDate' maxlength="11" size="18" tabindex=2/>
												&nbsp; <a href="javascript:show_calendar('forms[0].openedDate')"> <img name="calendar" alt='calendar' border="0" src="<%=basePath%>/images/calendar.gif" src="" alt="" /></a>
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Amount
											</td>
											<td align="left">
												<input type="text" name="amount" maxlength="10" tabindex="3" size="18" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Details 
											</td>
											<td align="left">
												<input type="text" name="details" maxlength="150" tabindex="4" size="18" />
											</td>
										</tr>
										<tr>
											<td height="10px">
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<input type="submit" class="btn" value="Submit" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
											</td>
										</tr>
										<tr>
											<td height="10px">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
