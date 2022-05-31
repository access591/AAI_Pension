<%@ page language="java" pageEncoding="UTF-8"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");

String basePath = basePathBuf.toString() + "PensionView/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>" />
		<title>AAI</title>
		<meta http-equiv="pragma" content="no-cache" content="" />
		<meta http-equiv="cache-control" content="no-cache" content="" />
		<meta http-equiv="expires" content="0" content="" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" content="" />
		<meta http-equiv="description" content="This is my page" content="" />
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath%>scripts/EMail and Password.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<script type="text/javascript" type="text/javascript" type="">
			function checkParty(){
				if(document.forms[0].partyName.value == ""){
					alert("Please Enter Party Name (Mandatory)");
					document.forms[0].partyName.focus();
					return false;
				}
	/*			if(document.forms[0].mobileNo.value == ""){
					alert("Please Enter Mobile No. (Mandatory)");
					document.forms[0].mobileNo.focus();
					return false;
				}
				if(!ValidatePh(document.forms[0].mobileNo.value)){
					alert("Please Enter Valid Mobile No. ");
					document.forms[0].mobileNo.focus();
					return false;
				}
   		  		if(document.forms[0].faxNo.value == ""){
					alert("Please Enter Fax No. (Mandatory)");
					document.forms[0].faxNo.focus();
					return false;
				}
				if(!ValidatePh(document.forms[0].faxNo.value)){
					alert("Please Enter Valid Fax No. ");
					document.forms[0].faxNo.focus();
					return false;
				}
				if(document.forms[0].email.value == ""){
					alert("Please Enter Email ID (Mandatory)");
					document.forms[0].email.focus();
					return false;
				}
				if(!ValidateEMail(document.forms[0].email.value)){
					document.forms[0].email.focus();
					return false;
				}*/
				if(document.forms[0].bankName.value == ""){
						alert("Please Enter Bank Name (Mandatory)");
						document.forms[0].bankName.focus();
						return false;
					}
					if(document.forms[0].branchName.value == ""){
						alert("Please Enter Branch Name (Mandatory)");
						document.forms[0].branchName.focus();
						return false;
					}
					if(document.forms[0].bankCode.value == ""){
						alert("Please Enter Bank Code (Mandatory)");
						document.forms[0].bankCode.focus();
						return false;
					}
					if(document.forms[0].address.value == ""){
						alert("Please Enter Address (Mandatory)");
						document.forms[0].address.focus();
						return false;
					}
					if(document.forms[0].phoneNo.value == ""){
						alert("Please Enter Phone No. (Mandatory)");
						document.forms[0].phoneNo.focus();
						return false;
					}
					if(!ValidatePh(document.forms[0].phoneNo.value)){
						alert("Please Enter Valid Phone No. ");
						document.forms[0].phoneNo.focus();
						return false;
					}
					if(document.forms[0].bFaxNo.value == ""){
						alert("Please Enter Bank Fax No. (Mandatory)");
						document.forms[0].bFaxNo.focus();
						return false;
					}
					if(!ValidatePh(document.forms[0].bFaxNo.value)){
						alert("Please Enter Valid Bank Fax No. ");
						document.forms[0].bFaxNo.focus();
						return false;
					}
					if(document.forms[0].accountCode.value == ""){
						alert("Please Enter Account Code (Mandatory)");
						document.forms[0].accountCode.focus();
						return false;
					}
					if(document.forms[0].accountNo.value == ""){
						alert("Please Enter Account No. (Mandatory)");
						document.forms[0].accountNo.focus();
						return false;
					}
					if(document.forms[0].ifscCode.value == ""){
						alert("Please Enter IFSC Code (Mandatory)");
						document.forms[0].ifscCode.focus();
						return false;
					}
					if(document.forms[0].neftCode.value == ""){
						alert("Please Enter NEFT/RTGS Code (Mandatory)");
						document.forms[0].neftCode.focus();
						return false;
					}
					if(document.forms[0].micrNo.value == ""){
						alert("Please Enter MICR Code (Mandatory)");
						document.forms[0].micrNo.focus();
						return false;
					}
					if(document.forms[0].contactPerson.value == ""){
						alert("Please Enter Contact Person Code (Mandatory)");
						document.forms[0].contactPerson.focus();
						return false;
					}
					if(document.forms[0].bMobileNo.value == ""){
						alert("Please Enter Bank Mobile No Code (Mandatory)");
						document.forms[0].bMobileNo.focus();
						return false;
					}
					if(!ValidatePh(document.forms[0].bMobileNo.value)){
						alert("Please Enter Bank Valid Mobile No. ");
						document.forms[0].bMobileNo.focus();
						return false;
					}
			}
			function popupWindow(mylink, windowname){
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
				function details(accountCode,particular){	
		   			document.forms[0].accountCode.value=accountCode;
		   			document.forms[0].accountNo.focus();
				}
		</script>
	</head>
	<body class="BodyBackground1" onload="document.forms[0].partyName.focus();">
		<form action="<%=basePathBuf%>Party?method=addPartyRecord" onsubmit="javascript: return checkParty()" method="post">
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
					<td align="center" class="label">
						<font color="red" size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%> <br /> &nbsp; </font>
					</td>
				</tr>
				<tr>
					<td>
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">

									Party Master[Add] &nbsp;&nbsp;<font color="red"> </font>
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%">
									<table align="center" cellspacing="5">
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Party Name
											</td>
											<td align="left">
												<input type="text" name="partyName" maxlength="25" tabindex="1" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												Party Master
											</td>
											<td align="left">
												<textarea name="partyDetail" tabindex="2" cols="26" tabindex="17" rows=""></textarea>
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Mobile No
											</td>
											<td align="left">
												<input type="text" name="mobileNo" maxlength="25" tabindex="3" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												Fax No
											</td>
											<td align="left">
												<input type="text" name="faxNo" maxlength="25" tabindex="4" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Contact No
											</td>
											<td align="left">
												<input type="text" name="contactNo" maxlength="25" tabindex="4" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												Email
											</td>
											<td align="left">
												<input type="text" name="email" maxlength="25" tabindex="5" />
											</td>
										</tr>
										<tr>
											<td height="5%" colspan="5"  align="left" class="ScreenLeftHeading" bgcolor="#DBE4EC">			
												Party Bank Detail &nbsp;&nbsp;<font color="red"> </font>
											</td>
			
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Name
											</td>
											<td align="left">
												<input type="text" name="bankName" maxlength="25" tabindex="6" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Branch Name
											</td>
											<td align="left">
												<input type="text" name="branchName" maxlength="25" tabindex="7" />
											</td>
										</tr>
										<tr></tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Code
											</td>
											<td align="left">
												<input type="text" name="bankCode" maxlength="25" tabindex="8" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Address
											</td>
											<td align="left">
												<textarea name="address" cols="26" tabindex="9" rows="" style='width=130px'></textarea>
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Phone No
											</td>
											<td align="left">
												<input type="text" name="phoneNo" maxlength="25" tabindex="10" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Fax No
											</td>
											<td align="left">
												<input type="text" name="bFaxNo" maxlength="25" tabindex="11" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c Code
											</td>
											<td align="left">
												<input type="text" name="accountCode" maxlength="25" size="15" readonly="readonly" />
												&nbsp;
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" tabindex="12" onclick="popupWindow('<%=basePath%>cashbook/AccountInfo.jsp','AAI');" alt="Click The Icon to Select Bank Account Code Master Records" src="" alt="" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c No
											</td>
											<td align="left">
												<input type="text" name="accountNo" maxlength="25" tabindex="13" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Type of Account
											</td>
											<td align="left">
												<select name="accountType" tabindex="14" style="Width:80px">
													<option value="S">
														Savings
													</option>
													<option value='C'>
														Current
													</option>
												</select>
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>IFSC Code
											</td>
											<td align="left">
												<input type="text" name="ifscCode" maxlength="10" tabindex="15" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font> NEFT/RTGS Code
											</td>
											<td align="left">
												<input type="text" name="neftCode" maxlength="11" tabindex="16" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>MICR No
											</td>
											<td align="left">
												<input type="text" name="micrNo" maxlength="12" tabindex="17" />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Contact Person Name
											</td>
											<td align="left">
												<input type="text" name="contactPerson" maxlength="13" tabindex="18" />
											</td>

											<td width="10px">

											</td>
											<td class="label" align="right" align="right">
												<font color="red">&nbsp;*&nbsp;</font>Mobile No
											</td>
											<td align="left">
												<input type="text" name="bMobileNo" maxlength="14" tabindex="19" />
											</td>
										</tr>
										<tr>
											<td align="center" colspan="5">
												<input type="submit" class="btn" value="Submit" tabindex="20" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" tabindex="21" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" "tabindex="22" />
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
