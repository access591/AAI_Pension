<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.BankMasterInfo"%>
<%@ page import="aims.common.CommonUtil" %>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");
String basePath = basePathBuf.toString() + "PensionView/";
CommonUtil util = new CommonUtil();
int gridLength = util.gridLength();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>AAI - Cashbook - Master - Account Code Search Master</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css">
		<link rel="stylesheet" href="<%=basePath%>css/aimsfinancestyle.css" type="text/css">
		<link rel="stylesheet" href="<%=basePath%>css/displaytagstyle.css" type="text/css">
		  <script language="javascript">
		    function chkDelete(field){
		     var val = "";
				for (var i=0; i < document.forms[0].deleteRecord.length; i++)
  				 {
   					if (document.forms[0].deleteRecord[i].checked)
    				  {
    					  val= val + document.forms[0].deleteRecord[i].value + "|";
    		     }
    		       
                 }
                  document.forms[0].accNo.value=val;
                  document.forms[0].action = "<%=basePathBuf%>BankMaster?method=deleteBankRecord";
                  document.forms[0].submit();
		    }
		  
		  </script>
	</head>
	
	<body class="BodyBackground1">
			<form action="<%=basePathBuf%>BankMaster?method=searchRecords" method="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<TR>
					<TD>

						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</TD>
				</TR>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenHeading" width="50%" >

									Bank Master[Search]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%" width="100%" align="center">
								<table align="center" border=0 width="100%">
									<tr>
										<td class="label" align=right width=50%>
											Bank Name:<font color="red">&nbsp;*</font>
										</td>
										<td align=left>
											<input type="text" name="bankname" maxlength="25" tabindex="1">
										</td>
									</tr>
									<TR>
										<td class="label" align=right width=50%>
											Branch Name:
										</td>
										<td align=left>
											<input type="text" name="branchname" maxlength="25" tabindex="2">
										</td>
									</TR>
									<tr>
										<td class="label" align=right width=50%>
											Bank Code :
										</td>
										<td align=left>
											<input type="text" name="bankcode" maxlength="25" tabindex="3">
										</td>
									</tr>
									<tr>
										<td class="label" align=right width=50%>
											Bank A/c No:
										</td>
										<td align=left>
											<input type="text" name="bankacno" maxlength="25" tabindex="4">
										</td>
									</tr>
									

												<tr>
													
													<td align="center" colspan=2>
														<input type="submit" class="btn" value="Search">
														<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
														<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
													</td>
										
							</tr>
						
							<tr>
					<td align=center class=label colspan=2>
						<font color=red size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%>
						
					</td>
				</tr>
<%
	if(request.getAttribute("BankList")!=null){
%>							
							<tr>							
								<td align="center" width="100%"  colspan=2 nowrap>
								   <input type=hidden name=accNo>
										<display:table style="width:500" keepStatus="true" export="true" sort="list" name="BankList" name="requestScope.BankList" pagesize="<%=gridLength%>" id='searchData' requestURI="/BankMaster?method=searchRecords">
												<display:setProperty name="export.amount" value="list" />
												<display:setProperty name="export.excel.filename" value="BankInfo.xls" />
												<display:setProperty name="export.pdf.filename" value="BankInfo.pdf" />
												<display:setProperty name="export.rtf.filename" value="BankInfo.rtf" />
												<display:column title="<img src='./images/delete1.gif' border='0' alt='Delete' onclick='chkDelete(document.forms[0].deleteRecord)'>" media="html">
											<input type="checkbox" name="deleteRecord" value="<%=((BankMasterInfo)pageContext.getAttribute("searchData")).getAccountNo()%>" >
										</display:column>
												<display:column property="bankName" sortable="true" headerClass="sortable" title="Bank Name">
												</display:column>
												<display:column property="accountNo" sortable="true" title="Account No." />
												<display:column property="bankCode" sortable="true" title="Bank Code" />
												<display:column property="IFSCCode" sortable="true" title="IFSC Code" />
												<display:column property="accountCode" sortable="true" title="Account Code" />
												<display:column property="particular" sortable="true" title="Particular" />
												<display:column title="<img src='./images/page_edit.png' border='0' alt='Edit'>"  media="html">
											<a href='<%=basePathBuf%>BankMaster?method=editBankRecord&&accNo=<%=((BankMasterInfo)pageContext.getAttribute("searchData")).getAccountNo()%>'> <img src='./images/img-edit.gif' border='0' alt='Edit'> </a>
										</display:column>
											</display:table>
								</td>
							</tr>
<%}%>							
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

