<%@ page language="java" pageEncoding="UTF-8"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
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
					if(document.forms[0].mobileNo.value == ""){
						alert("Please Enter Mobile No Code (Mandatory)");
						document.forms[0].mobileNo.focus();
						return false;
					}
					if(!ValidatePh(document.forms[0].mobileNo.value)){
						alert("Please Enter Valid Mobile No. ");
						document.forms[0].mobileNo.focus();
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
				}
		</script>
	</head>

	<body class="BodyBackground1" onload="document.forms[0].bankName.focus();">
		<form name="bank" action="<%=basePathBuf%>BankMaster?method=addBankRecord" onsubmit="javascript : return checkRecord()" method="post" action="">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="5">
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
					<td align="center">
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									Bank Master[Add] &nbsp;&nbsp;<font color="red"> </font>
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%" align="center">
									<table align="center" border="0" cellspacing="5">
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Name
											</td>
											<td align="left">
												<input type="text" name="bankName" maxlength="25" tabindex="1" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Branch Name
											</td>
											<td align="left">
												<input type="text" name="branchName" maxlength="25" tabindex="2" />
											</td>
										</tr>
										<tr></tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Code
											</td>
											<td align="left">
												<input type="text" name="bankCode" maxlength="25" tabindex="3" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												Address:
											</td>
											<td align=left>
												<textarea name="address"  cols="26" tabindex="4" rows="" style='width=130px'></textarea>
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Phone No
											</td>
											<td align="left">
												<input type="text" name="phoneNo" maxlength="25" tabindex="5" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Fax No
											</td>
											<td align="left">
												<input type="text" name="faxNo" maxlength="25" tabindex="6" />
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c Code
											</td>
											<td align="left">
												<input type="text" name="accountCode" maxlength="25" size="15" readonly="readonly"  />
												&nbsp;
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" tabindex="7" onclick="popupWindow('<%=basePath%>cashbook/AccountInfo.jsp','AAI');" alt="Click The Icon to Select Bank Account Code Master Records" src="" alt="" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c No
											</td>
											<td align="left">
												<input type="text" name="accountNo" maxlength="25" tabindex="8" />
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												Type of Account 
											</td>
											<td align="left">
												<select name="accountType" tabindex="9" style="Width:80px">
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
												Trust Type &nbsp;
											</td>
											<td align="left">
												<select name='trusttype' style='width:80px'>
													<option value="">
														Select One
													</option>
													<option value='I'>
														IAAI ECPF
													</option>
													<option value='N'>
														NAA ECPF
													</option>
													<option value='A'>
														AAI EPF
													</option>
												</select>
											</td>
											</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>IFSC Code
											</td>
											<td align="left">
												<input type="text" name="ifscCode" maxlength="10" tabindex="10" />
											</td>
										<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font> NEFT/RTGS Code
											</td>
											<td align="left">
												<input type="text" name="neftCode" maxlength="11" tabindex="11" />
											</td>
										</tr>
										<tr>	
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>MICR No
											</td>
											<td align="left">
												<input type="text" name="micrNo" maxlength="12" tabindex="12" />
											</td>
										<td width="10px">

											</td >
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Contact Person Name
											</td>
											<td align="left">
												<input type="text" name="contactPerson" maxlength="13" tabindex="13" />
											</td>
											
											</tr>
										<tr>
											<td class="label"  align="right"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Mobile No
											</td>
											<td align="left">
												<input type="text" name="mobileNo" maxlength="14" tabindex="14" />
											</td>
											<td width="10px">

											</td>
											<td class="label" align="right">
												Unit Name &nbsp;
											</td>
											<td align="left">
												<select name='unitName' style='width:80px'>
													<option value="">
														Select One
													</option>
													<option value="hyderabad">
														Hyderabad
													</option>
													<option value='delhi'>
														Delhi
													</option>
													<option value='banglure'>
														Banglure
													</option>
													<option value='chennai'>
														Chennai
													</option>
												</select>
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>

											<td align="center" colspan="5">
												<input type="submit" class="btn" value="Submit" class="btn" tabindex="15"/>
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="16" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="17"/>
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
