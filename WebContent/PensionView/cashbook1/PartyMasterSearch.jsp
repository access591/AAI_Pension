<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.PartyInfo"%>
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
                  document.forms[0].partyName.value=val;
                  document.forms[0].action = "<%=basePathBuf%>Party?method=deletePartyRecord";
                  document.forms[0].submit();
		    }
		  
		  </script>
	</head>
	
	<body class="BodyBackground1">
		<form action="<%=basePathBuf%>Party?method=searchRecords" method="post" >
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
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenHeading">

									Party Master[Search]
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
												Party Name:
											</td>
											<td>
												<input type="text" name="partyName" maxlength="25" tabindex="1">
											</td>
										</tr>
									<tr>
								<td>
									&nbsp;
								</td>
							</tr>
										<tr>
											<td align="center" colspan="2">
												<input type="submit" class="btn" value="Search">
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
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
					<td align=center class=label>
						<font color=red size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%>
						<BR>
						&nbsp;
					</td>
				</tr>
<%
	if(request.getAttribute("dataList")!=null){
%>							
							<tr>							
								<td align="center" width="100%">
								   <input type=hidden name=partyName>
									<display:table keepStatus="true" export="true"  sort="list" name="dataList" name="requestScope.dataList" pagesize="<%=gridLength%>" id='searchData'  requestURI="/Party?method=searchRecords" >										
										<display:setProperty name="export.amount" value="list" />
    									<display:setProperty name="export.excel.filename" value="AccountingCodes.xls" />
    									<display:setProperty name="export.pdf.filename" value="AccountingCodes.pdf" />
    									<display:setProperty name="export.rtf.filename" value="AccountingCodes.rtf" />
    									<display:column title="<img src='./images/delete1.gif' border='0' alt='Delete' onclick='chkDelete(document.forms[0].deleteRecord)'>" media="html">
											<input type="checkbox" name="deleteRecord" value="<%=((PartyInfo)pageContext.getAttribute("searchData")).getPartyName()%>" >
										</display:column>
										<display:column property="partyName" sortable="true" headerClass="sortable" title="Party Name" >
										</display:column>
										<display:column property="partyDetail" sortable="true" title="Party Detail" />
										<display:column property="mobileNo" sortable="true" title="Mobile No." />
										<display:column property="emailId" sortable="true" title="EmailId" />
										<display:column title="<img src='./images/page_edit.png' border='0' alt='Edit'>"  media="html">
											<a href='<%=basePathBuf%>Party?method=editPartyRecord&&pName=<%=((PartyInfo)pageContext.getAttribute("searchData")).getPartyName()%>'> <img src='./images/img-edit.gif' border='0' alt='Edit'> </a>
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
