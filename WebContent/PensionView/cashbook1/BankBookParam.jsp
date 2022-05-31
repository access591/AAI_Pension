<%@ page language="java" pageEncoding="UTF-8"%>
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
		<title>AAI - Cashbook - Master - Bank Master</title>

		<meta http-equiv="pragma" content="no-cache" content="" />
		<meta http-equiv="cache-control" content="no-cache" content="" />
		<meta http-equiv="expires" content="0" content="" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" content="" />
		<meta http-equiv="description" content="This is my page" content="" />
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js" type=""></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" type="">
			function checkRecord(){
				if(document.forms[0].bankName.value == ""){
					alert("Please Enter Bank Name (Mandatory)");
					document.forms[0].bankName.focus();
					return false;
				}
				var swidth=screen.Width-10;
				var sheight=screen.Height-150;
				var url = "<%=basePathBuf%>Voucher?method=getBankBook&&accountNo="+document.forms[0].accountNo.value;
				var wind1 = window.open(url,"BankBook","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
				return false;
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

	<body class="BodyBackground1" onload="document.forms[0].bankName.focus();">
		<form name="bank" action="#" method="post" action="">
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
					<td align="center">
						<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="left" class="ScreenMasterHeading">
									Bank Book[Param] &nbsp;&nbsp;<font color="red"> </font>
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%" align="center">
									<table align="center" border="0">
										<tr>
											<td class="label" width="100px">
												Bank Name:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="bankName" size="18" maxlength="50" readonly="readonly" />
												<input type="hidden" name="accountNo" />
												<img style='cursor:hand' name="bankImage" src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/BankInfo.jsp','AAI');" alt="Click The Icon to Select Bank Master Records" src="" alt="" />
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>

										<tr>
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td align="center">
												<input type="button" class="btn" value="Submit" class="btn" onclick="return checkRecord()" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
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
