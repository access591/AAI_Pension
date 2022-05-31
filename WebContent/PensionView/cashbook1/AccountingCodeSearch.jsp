<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.AccountingCodeInfo"%>
<%@ page import="aims.common.CommonUtil"%>
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
		<base href="<%=basePath%>" />
		<title>AAI - Cashbook - Master - Account Code Search Master</title>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>css/aimsfinancestyle.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>css/displaytagstyle.css" type="text/css" />
		<script language="javascript" type="">
		    function chkDelete(field){
		     var val = "";
				for (var i=0; i < document.forms[0].deleteRecord.length; i++)
  				 {
   					if (document.forms[0].deleteRecord[i].checked)
    				  {
    					  val= val + document.forms[0].deleteRecord[i].value + "|";
    		     }
    		       
                 }
                  document.forms[0].accHeads.value=val;
                  document.forms[0].action = "<%=basePathBuf%>AccountingCode?method=deleteAccountRecord";
                  document.forms[0].submit();
		    }
		  
		  </script>
	</head>

	<body class="BodyBackground1">
		<form action="<%=basePathBuf%>AccountingCode?method=searchRecords" method="post">
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
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenHeading">

									Account code Master[Search]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%" width="75%">
									<table align="center">
										<tr>
											<td class="label">
												Account Code:
											</td>
											<td>
												<input type="text" name="accountCode" maxlength="10" tabindex="1" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Name of Account:
											</td>
											<td>
												<input type="text" name="accountName" maxlength="50" tabindex="1" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Type of Account :
											</td>
											<td>
												<select name="accountType" style='width:130px'>
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
											<td align="center" colspan="2">
												<input type="submit" class="btn" value="Search" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
											</td>
										</tr>
									</table>
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
							<%
	if(request.getAttribute("dataList")!=null){
%>
							<tr>
								<td align="center" width="100%">
									<input type="hidden" name="accHeads" />
									<display:table keepStatus="true" export="true" sort="list" name="dataList" name="requestScope.dataList" pagesize="<%=gridLength%>" id='searchData' requestURI="/AccountingCode?method=searchRecords">
										<display:setProperty name="export.amount" value="list" />
										<display:setProperty name="export.excel.filename" value="AccountingCodes.xls" />
										<display:setProperty name="export.pdf.filename" value="AccountingCodes.pdf" />
										<display:setProperty name="export.rtf.filename" value="AccountingCodes.rtf" />
										<display:column title="<img src='./images/delete1.gif' border='0' alt='Delete' onclick='chkDelete(document.forms[0].deleteRecord)'>" media="html">
											<input type="checkbox" name="deleteRecord" value='<%=((AccountingCodeInfo)pageContext.getAttribute("searchData")).getAccountHead()%>' />
										</display:column>
										<display:column property="accountHead" sortable="true" headerClass="sortable" title="Account Code">
										</display:column>
										<display:column property="particular" sortable="true" title="Account Name" />
										<display:column property="type" sortable="true" title="Account Type" />
										<display:column title="<img src='./images/page_edit.png' border='0' alt='Edit'>" media="html">
											<a href='<%=basePathBuf%>AccountingCode?method=editAccountRecord&&accHead=<%=((AccountingCodeInfo)pageContext.getAttribute("searchData")).getAccountHead()%>'> <img src='./images/img-edit.gif' border='0' alt='Edit' /> </a>
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
