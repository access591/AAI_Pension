<%@ page language="java" import="java.util.*,aims.bean.cashbook.*" buffer="16kb"%>
<%StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");

String basePath = basePathBuf.toString() + "PensionView/";
PartyInfo partyInfo = (PartyInfo) request
					.getAttribute("einfo");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
	<HEAD>
		<BASE HREF="<%=basePath%>">
		<TITLE>AAI</TITLE>
		<META HTTP-EQUIV="pragma" CONTENT="no-cache">
		<META HTTP-EQUIV="cache-control" CONTENT="no-cache">
		<META HTTP-EQUIV="expires" CONTENT="0">
		<META HTTP-EQUIV="keywords" CONTENT="keyword1,keyword2,keyword3">
		<META HTTP-EQUIV="description" CONTENT="This is my page">
		<LINK REL="stylesheet" HREF="<%=basePath%>css/aai.css" TYPE="text/css">
		<SCRIPT TYPE="text/javascript" SRC="<%=basePath%>scripts/EMail and Password.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT TYPE="text/javascript" TYPE="text/javascript">
			function checkParty(){
				
				if(document.forms[0].mobileNo.value == ""){
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
				}
			}
		</SCRIPT>
	</HEAD>
	<BODY CLASS="BodyBackground1" onload="document.forms[0].partyName.focus();">
		<FORM action="<%=basePathBuf%>Party?method=updatePartyRecord" onsubmit="javascript: return checkParty()" method="post">
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
				
				<TR>
					<TD>
						<TABLE WIDTH="75%" BORDER="0" ALIGN="center" CELLPADDING="0" CELLSPACING="0" CLASS="tbborder">
							<TR>
								<TD HEIGHT="5%" COLSPAN="2" ALIGN="center" CLASS="ScreenMasterHeading">

									Party Master[Edit] &nbsp;&nbsp;<FONT COLOR="red"> </FONT>
								</TD>

							</TR>
							<TR>
								<TD>
									&nbsp;
								</TD>
							</TR>
							<TR>
								<TD HEIGHT="15%">
									<TABLE ALIGN="center">
										<TD CLASS="label">
											Party Name:
										</TD>
										<TD>
										    <input type=hidden name="partyName" value="<%=partyInfo.getPartyName()%>">
											<%=partyInfo.getPartyName()%>
										</TD>
										<TR>
											<TD CLASS="label">
												Party Detail:
											</TD>
											<TD>
												<TEXTAREA NAME="partyDetail" MAXLENGTH="150" COLS="20" TABINDEX="17" ROWS=""><%=partyInfo.getPartyDetail()%></TEXTAREA>
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Mobile No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="mobileNo" MAXLENGTH="25" TABINDEX="1" value="<%=partyInfo.getMobileNo()%>">
											</TD>
										</TR>
										<TR>
											<TD CLASS="label">
												Fax No:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="faxNo" MAXLENGTH="25" TABINDEX="1" value="<%=partyInfo.getFaxNo()%>">
											</TD>
										</TR>
										<TR></TR>
										<TR>
											<TD CLASS="label">
												Email:<FONT COLOR="red">&nbsp;*</FONT>
											</TD>
											<TD>
												<INPUT TYPE="text" NAME="email" MAXLENGTH="25" TABINDEX="1" value="<%=partyInfo.getEmailId()%>">
											</TD>
										</TR>
										<TR>
											<TD ALIGN="center" COLSPAN="2">
												<INPUT TYPE="submit" CLASS="btn" VALUE="Submit">
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
