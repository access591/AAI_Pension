<%@ page language="java" import="java.util.*,aims.bean.cashbook.*" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.BankMasterInfo"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
 BankMasterInfo bank = (BankMasterInfo)request.getAttribute("binfo");
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
		<form name="bank" action="<%=basePathBuf%>BankMaster?method=updateBankRecord" onsubmit="javascript : return checkRecord()" method="post" action="">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>

						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp" />
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
					<td align="center" class="label">
						<font color="red" size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%> <br /> &nbsp; </font>
					</td>
				</tr>
				<tr>
					<td align="center">
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									Bank Master[Edit] &nbsp;&nbsp;<font color="red"> </font>
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
												<input type="text" name="bankName" maxlength="25" tabindex="1" value="<%=bank.getBankName()%>" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Branch Name
											</td>
											<td align="left">
												<input type="text" name="branchName" maxlength="25" tabindex="2" value="<%=bank.getBranchName()%>" />
											</td>
										</tr>
										<tr></tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank Code
											</td>
											<td align="left">
												<input type="text" name="bankCode" maxlength="25" tabindex="1" value="<%=bank.getBankCode()%>" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												Address
											</td>
											<td align="left">
												<textarea name="address"  cols="26" tabindex="17" ><%=bank.getAddress()==null?"":bank.getAddress().trim()%></textarea>
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Phone No
											</td>
											<td align="left">
												<input type="text" name="phoneNo" maxlength="25" tabindex="1" value="<%=bank.getPhoneNo()%>" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Fax No
											</td>
											<td align="left">
												<input type="text" name="faxNo" maxlength="25" tabindex="2" value="<%=bank.getFaxNo()%>" />
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c Code
											</td>
											<td align="left">
												<input type="text" name="accountCode" maxlength="25" size="15" value="<%=bank.getAccountCode()%>" readonly="readonly" />
												&nbsp;
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/AccountInfo.jsp','AAI');" alt="Click The Icon to Select Bank Account Code Master Records" src="" alt="" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Bank A/c No
											</td>
											<td align="left">
												<input type="text" name="accountNo" maxlength="25" tabindex="2" value="<%=bank.getAccountNo()%>" readonly="readonly" />
											</td>
										</tr>
										<tr>
											<td class="label"  align="right">
												Type of Account 
											</td>
											<td align="left">
												<select name="accountType" tabindex="15" style="Width:130px">
													<option value="S" <%="S".equals(bank.getAccountType())?"selected":""%>>
														Saving
													</option>
													<option value='C' <%="C".equals(bank.getAccountType())?"selected":""%>>
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
												<input type="text" name="ifscCode" maxlength="25" tabindex="2" value="<%=bank.getIFSCCode()%>" />
											</td>
											<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>NEFT/RTGS Code
											</td>
											<td align="left">
												<input type="text" name="neftCode" maxlength="25" tabindex="1" value="<%=bank.getNEFTRTGSCode()%>" />
											</td>
											</tr>
										<tr>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>MICR No
											</td>
											<td align="left"> 
												<input type="text" name="micrNo" maxlength="25" tabindex="2" value="<%=bank.getMICRNo()%>" />
											</td>
										<td width="10px">

											</td>
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Contact Person Name
											</td>
											<td align="left">
												<input type="text" name="contactPerson" maxlength="25" tabindex="1" value="<%=bank.getContactPerson()%>" />
											</td>
										</tr>
										<tr>	
											<td class="label"  align="right">
												<font color="red">&nbsp;*&nbsp;</font>Mobile No
											</td>
											<td align="left">
												<input type="text" name="mobileNo" maxlength="25" tabindex="2" value="<%=bank.getMobileNo()%>" />
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
													<option value="hyderabad" <%="hyderabad".equals(bank.getUnitName())?"Selected":""%>>
														Hyderabad
													</option>
													<option value='delhi' <%="delhi".equals(bank.getUnitName())?"Selected":""%>>
														Delhi
													</option>
													<option value='banglure' <%="banglure".equals(bank.getUnitName())?"Selected":""%>>
														Banglore
													</option>
													<option value='chennai' <%="chennai".equals(bank.getUnitName())?"Selected":""%>>
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

											<td align="center" colspan=5>
												<input type="submit" class="btn" value="Submit" class="btn" />
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
