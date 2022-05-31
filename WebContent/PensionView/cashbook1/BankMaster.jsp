<%@ page language="java" pageEncoding="UTF-8"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
	<HEAD>
		<BASE HREF="<%=basePath%>">

		<TITLE>AAI - Cashbook - Master - Bank Master</TITLE>

		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="cache-control" CONTENT="no-cache">
		<META HTTP-EQUIV="expires" CONTENT="0">
		<META HTTP-EQUIV="keywords" CONTENT="keyword1,keyword2,keyword3">
		<META HTTP-EQUIV="description" CONTENT="This is my page">
		<SCRIPT TYPE="text/javascript" SRC="<%=basePath%>scripts/CommonFunctions.js"></SCRIPT>
		<LINK REL="stylesheet" HREF="<%=basePath%>css/aai.css" TYPE="text/css">
		<SCRIPT TYPE="text/javascript">
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
		</SCRIPT>
	</HEAD>

	<BODY CLASS="BodyBackground1" onload="document.forms[0].bankName.focus();">
		<FORM NAME="bank" ACTION="<%=basePathBuf%>BankMaster?method=addBankRecord" ONSUBMIT="javascript : return checkRecord()" METHOD="post">
			<TABLE WIDTH="100%" BORDER="0" ALIGN="center" CELLPADDING="0" CELLSPACING="0">
				<TR>
					<TD>

						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</TD>
				</TR>
				<TR>
					<TD>
						&nbsp;
					</TD>
				</TR>
				<tr>
					<td align=center class=label>
						<font color=red size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%>
						<BR>
						&nbsp;
					</td>
				</tr>
				<TR>
					<TD ALIGN="center">
						<TABLE WIDTH="75%" BORDER="0" ALIGN="center" CELLPADDING="0" CELLSPACING="0" CLASS="tbborder">
							<TR>
								<TD HEIGHT="5%" COLSPAN="2" ALIGN="center" CLASS="ScreenMasterHeading">
									Bank Master[Add] &nbsp;&nbsp;<FONT COLOR="red"> </FONT>
								</TD>

							</TR>
							<TR>
								<TD>
									&nbsp;
								</TD>
							</TR>
							<TR>
								<TD HEIGHT="15%" ALIGN="center">
									<TABLE ALIGN="center" BORDER="0">
										<TR>
											<TD CLASS="label">
												Bank Name:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="bankName" MAXLENGTH="25" TABINDEX="1">
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												Branch Name:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="branchName" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR></TR>
										<TR>
											<TD CLASS="label">
												Bank Code:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="bankCode" MAXLENGTH="25" TABINDEX="1">
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												Address:
											</TD>
											<TD>
												<TEXTAREA NAME="address" MAXLENGTH="150" COLS="20" TABINDEX="17" ROWS="" STYLE='width=130px'></TEXTAREA>
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Phone No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="phoneNo" MAXLENGTH="25" TABINDEX="1">
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												Fax No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="faxNo" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Bank A/c Code:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="accountCode" MAXLENGTH="25" SIZE="15" READONLY>
												&nbsp;
												<IMG STYLE='cursor:hand' SRC="<%=basePath%>images/search1.gif" ONCLICK="popupWindow('<%=basePath%>cashbook/AccountInfo.jsp','AAI');" ALT="Click The Icon to Select Bank Account Code Master Records" />
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												Bank A/c No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="accountNo" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Type of Account :
											</TD>
											<TD>
												<SELECT NAME="accountType" TABINDEX="15" STYLE="Width:130px">
													<OPTION VALUE="S">
														saving
													</OPTION>
													<OPTION VALUE='C'>
														Current
													</OPTION>
												</SELECT>
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												IFSC Code.:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="ifscCode" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												NEFT/RTGS Code:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="neftCode" MAXLENGTH="25" TABINDEX="1">
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												MICR No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="micrNo" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Contact Person Name:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="contactPerson" MAXLENGTH="25" TABINDEX="1">
											</TD>
											<TD WIDTH="10px">

											</TD>
											<TD CLASS="label">
												Mobile No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="mobileNo" MAXLENGTH="25" TABINDEX="2">
											</TD>
										</TR>
										<TR>
											<TD>
												&nbsp;
											</TD>
										</TR>
										<TR>
											<TD>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</TD>
											<TD ALIGN="center">
												<INPUT TYPE="submit" CLASS="btn" VALUE="Submit" CLASS="btn">
												<INPUT TYPE="button" CLASS="btn" VALUE="Reset" ONCLICK="javascript:document.forms[0].reset()" CLASS="btn">
												<INPUT TYPE="button" CLASS="btn" VALUE="Cancel" ONCLICK="javascript:history.back(-1)" CLASS="btn">
											</TD>
										</TR>
									</TABLE>
								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</FORM>
	</BODY>
</HTML>
