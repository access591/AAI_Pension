
<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%StringBuffer basePathBuf = new StringBuffer(request.getScheme());
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
		<title>AAI - Cashbook - Master -Account Code Master</title>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript">
			function checkAccount(){
				if(document.forms[0].accountCode.value == ""){
					alert("Please Enter Account Code (Mandatory)");
					document.forms[0].accountCode.focus();
					return false;
				}
				if(document.forms[0].accountName.value == ""){
					alert("Please Enter Particular(Mandatory)");
					document.forms[0].accountName.focus();
					return false;
				}
			}
		</script>
	</head>
	<body class="BodyBackground1" onload="document.forms[0].accountCode.focus();">
		<form name="account" action="<%=basePathBuf%>AccountingCode?method=addAccountRecord" onsubmit="javascript : return checkAccount()" method="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</td>
				</tr>
				<tr>
					<td align="center">
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="center" class="label">
						<font color="red" size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%> <br /> &nbsp; </font>
					</td>
				</tr>
				<tr>
					<td>
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									Accounting code Master[Add] &nbsp;&nbsp;
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
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font> Account Code
											</td>
											<td>
												<input type="text" name="accountCode" maxlength="10" tabindex="1" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Particular  
											</td>
											<td>
												<input type="text" name="accountName" maxlength="50" tabindex="2" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Type of Account 
											</td>
											<td>
												<select name="accountType" style='width:90px' tabindex="3">
													<option value="">
														[Select One]
													</option>
													<option value='A'>
														Asset
													</option>
													<option value='L'>
														Liability
													</option>
													<option value='I'>
														Income
													</option>
													<option value='E'>
														Expenditure
													</option>
												</select>
											</td>
										</tr>
										<tr>
											<td height="10px">
											</td>
										</tr>
										<tr>
											<td align="center" colspan="2">
												<input type="submit" class="btn" value="Submit" tabindex="4" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="5"/>
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="6"/>
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
