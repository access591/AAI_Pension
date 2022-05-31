<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="aims.bean.cashbook.BankOpenBalInfo" %>
<%@ page import="java.text.DecimalFormat" %>
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
		<title>AAI - Cashbook - Master - Bank Opening Balance Search</title>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>css/aimsfinancestyle.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>css/displaytagstyle.css" type="text/css" />
		<script language="javascript" >
		    function chkDelete(field){
		    	var val = "";
				for (var i=0; i < document.forms[0].deleteRecord.length; i++){
   					if (document.forms[0].deleteRecord[i].checked) {
    					  val= val + document.forms[0].deleteRecord[i].value + "|";
    		     	}
    		    }
                document.forms[0].accNos.value=val;
                document.forms[0].action = "<%=basePathBuf%>BankOpeningBal?method=deleteBankOpenBalRecord";
                document.forms[0].submit();
		    }
		  
		  </script>
	</head>

	<body class="BodyBackground1">
		<form action="<%=basePathBuf%>BankOpeningBal?method=searchRecords" method="post">
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

									Bank Opening Balance[Search]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%" width="75%">
									<table align="center" cellspacing="5">
										<tr>
											<td class="label" align="right">
												Bank Name
											</td>
											<td align="left">
												<input type="text" name="bankName" maxlength="20" tabindex="1" />
											</td>
										</tr>		
										<tr>
											<td height="5px">
												
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
									<input type="checkbox" name="deleteRecord"  style='display:none'/>
									<input type="hidden" name="accNos" />
									<display:table keepStatus="true" export="true" sort="list" name="dataList" name="requestScope.dataList" pagesize="<%=gridLength%>" id='searchData' requestURI="/BankOpeningBal?method=searchRecords">
										<display:setProperty name="export.amount" value="list" />
										<display:setProperty name="export.excel.filename" value="BankOpenBal.xls" />
										<display:setProperty name="export.pdf.filename" value="BankOpenBal.pdf" />
										<display:setProperty name="export.rtf.filename" value="BankOpenBal.rtf" />
										<display:column title="<img src='./images/delete1.gif' border='0' alt='Delete' onclick='chkDelete(document.forms[0].deleteRecord)'>" media="html">
											<input type="checkbox" name="deleteRecord" value='<%=((BankOpenBalInfo)pageContext.getAttribute("searchData")).getAccountNo()%>' />
										</display:column>
										<display:column property="accountNo" sortable="true" headerClass="sortable" title="Account No">
										</display:column>
										<display:column property="bankName" sortable="true" title="Bank Name" />
										<display:column property="opendate" sortable="true" title="Opened Date" />
										<display:column property="amount" sortable="true" title="Amount" decorator="aims.common.NumberFormatDecorator" format="$ {0,number,0,000.00}"/>
										<display:column title="<img src='./images/page_edit.png' border='0' alt='Edit'>" media="html">
											<a href='<%=basePathBuf%>BankOpeningBal?method=getRecord&&accNo=<%=((BankOpenBalInfo)pageContext.getAttribute("searchData")).getAccountNo()%>'> <img src='./images/img-edit.gif' border='0' alt='Edit' /> </a>
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
