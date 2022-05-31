
<%@ page language="java" import="java.util.ArrayList,java.util.List"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.cashbook.PartyInfo" %>

<%
StringBuffer basePath = new StringBuffer(request.getScheme());
	 basePath.append("://").append(request.getServerName()).append(":");
	 basePath.append(request.getServerPort()).append(request.getContextPath());
	 basePath.append("/");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI - CashBook - Bank Info</title>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript">    			
 	
		 	function testSS(){
		   		var partyName=document.forms[0].partyName.value;
		   		var url ="<%=basePath%>Party?method=getPartyList&pName="+partyName;		   		
		   		document.getElementById("info").innerHTML="<img src='<%=basePath%>PensionView/images/loading1.gif'>";
			    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
			}
	
	
			 function sendInfo(partyName,detail){			 
				 window.opener.partyDetails(partyName,detail);	 
				 window.close();			 
			}
	 </script>
	</head>

	<body>
		<form>
			<table align="center" width=80%>
				<tr>
					<TD align="center">
						<TABLE align="center">
							<TR>
								<td class="label">
									Party Name:
								</td>
								<td>
									<input type="text" name="partyName" maxlength=25 />
									&nbsp;
								</td>&nbsp;					
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
								</td>
							</TR>
						</TABLE>
					</TD>
				</tr>
				<tr>
					<td id="info"  >
					</td>
				</tr>
				<tr>
					<td >
						&nbsp;
					</td>
				</tr>
				<tr>
					<td >
						&nbsp;
					</td>
				</tr>
<%
if (request.getAttribute("PartyList") != null) {
	List dataList = (ArrayList) request.getAttribute("PartyList");
    int listSize = dataList.size();
	if (listSize == 0) {
%>
				<tr>
					<td  class="ScreenSubHeading">
						<H3> No Records Found </H3>
					</td>
				</tr>				
<%
	}else{
%>
				<tr>
					<td  class="ScreenSubHeading">
							Party Master Info
					</td>
				</TR>
				<tr>
					<td >
						&nbsp;
					</td>
				</tr>
				<TR>
					<TD >
						<TABLE width=100% border=1 class=border cellpadding="0" cellspacing="0" >
							<tr>
								<td CLASS=LABEL> Party Name</td>
								<td CLASS=LABEL> Party Detail</td>
								<td CLASS=LABEL> Mobile No.</td>
								<td CLASS=LABEL> Mail ID</td>
								<td>
									<img src="./PensionView/images/page_edit.png"  />&nbsp;
								</td>
							</tr>						
<%
	}
	for(int cnt=0;cnt<listSize;cnt++){
		PartyInfo info = (PartyInfo)dataList.get(cnt);
%>
							<tr>
								<td CLASS=Data><%=info.getPartyName()%>&nbsp;</td>
								<td CLASS=Data><%=info.getPartyDetail()%>&nbsp;</td>
								<td CLASS=Data><%=info.getMobileNo()%>&nbsp;</td>
								<td CLASS=Data><%=info.getEmailId()%>&nbsp;</td>
								<td>
								<input type="checkbox" name="partyName" value="<%=info.getPartyName()%>" onclick="javascript:sendInfo('<%=info.getPartyName()%>','<%=info.getPartyDetail()%>')" />
								</td>
							</tr>
<%		
	}
}
%>
						</TABLE>
					</TD>
				</tr>
			</table>
		</form>
	</body>
</html>
