
<%@ page language="java" import="java.util.ArrayList,java.util.List"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.cashbook.AccountingCodeInfo"%>

<%
StringBuffer basePath = new StringBuffer(request.getScheme());
	 basePath.append("://").append(request.getServerName()).append(":");
	 basePath.append(request.getServerPort()).append(request.getContextPath());
	 basePath.append("/");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI - CashBook - Accounting Code Info</title>
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
		<script type="text/javascript">    			
 	
		 	function testSS(){
		   		var particular=document.forms[0].particular.value;
		   		var url ="<%=basePath%>AccountingCode?method=getAccountList&&particular="+particular+"&status=INFO";		   		
		   		document.getElementById("Info").innerHTML="<img src='<%=basePath%>PensionView/images/loading1.gif'>";
			    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
			}	
	
			 function sendInfo(accCode,particular){			 
				 window.opener.details(accCode,particular);	
				 window.close();			 
			}
	 </script>
	</head>

	<body>
		<form action="">
			<table align="center" width="80%">
				<tr>
					<td align="center">
						<table align="center">
							<tr>
								<td class="label">
									Particular:
								</td>
								<td>
									<input type="text" name="particular" maxlength="25" />
									<input type="hidden" name="type" value="<%=request.getParameter("type")%>" />
									<input type="hidden" name="AccHead" value="<%=request.getParameter("AccHead")==null?"":request.getParameter("AccHead")%>" />
									&nbsp;
								</td>
								
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td id="Info">
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<%
if (request.getAttribute("AccList") != null) {
	List dataList = (ArrayList) request.getAttribute("AccList");
    int listSize = dataList.size();
	if (listSize == 0) {
%>
				<tr>
					<td class="ScreenSubHeading">
						<h3>
							No Records Found
						</h3>
					</td>
				</tr>
				<%
	}else{
%>
				<tr>
					<td class="ScreenSubHeading">
						Bank Account Code Info
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="1" class="border" cellpadding="0" cellspacing="0">
							<tr>
								<td class="LABEL">
									Account Code
								</td>
								<td class="LABEL">
									Particular
								</td>
								<td class="LABEL">
									Type
								</td>
								<td>
									<img src="./PensionView/images/page_edit.png" alt="" />
									&nbsp;
								</td>
							</tr>
							<%
	}
	for(int cnt=0;cnt<listSize;cnt++){
		AccountingCodeInfo info = (AccountingCodeInfo)dataList.get(cnt);
%>
							<tr>
								<td class="Data">
									<%=info.getAccountHead()%>
									&nbsp;
								</td>
								<td class="Data">
									<%=info.getParticular()%>
									&nbsp;
								</td>
								<td class="Data">
									<%=info.getType()%>
									&nbsp;
								</td>
								<td>
									<input type="checkbox" name="accNo" value="<%=info.getAccountHead()%>" onclick="javascript:sendInfo('<%=info.getAccountHead()%>','<%=info.getParticular()%>')" />
								</td>
							</tr>
							<%		
	}
}
%>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
