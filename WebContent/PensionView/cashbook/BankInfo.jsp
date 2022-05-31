
<%@ page language="java" import="java.util.ArrayList,java.util.List"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.cashbook.BankMasterInfo"%>

<%
String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }
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
		   		var bankName=document.forms[0].bankName.value;
		   		var rem=document.forms[0].rem.value;
		   		var url ="<%=basePath%>BankMaster?method=getBankList&type="+rem+"&bankName="+bankName;		   		
		   		document.getElementById("bankInfo").innerHTML="<img src='<%=basePath%>PensionView/images/loading1.gif'>";
			    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
			}
	
	
			 function sendBankInfo(accNo,bankName,acccode,Particular,trustType){			 
			 	if(document.forms[0].rem.value == 'other'){
			 		window.opener.otherBankDetails(bankName,accNo,acccode,Particular);	
			 	}else{
				 window.opener.bankDetails(bankName,accNo,trustType);	
				}				 
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
									Bank Name:
								</td>
								<td>
									<input type="text" name="bankName" maxlength=25 />
									<input type="hidden" name="rem" value=<%=request.getParameter("type")%> />
									<input type="hidden" name="accountNo" value=<%=request.getParameter("accountNo")%> />
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
					<td id="bankInfo"  >
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
if (request.getAttribute("BankList") != null) {
	List bankList = (ArrayList) request.getAttribute("BankList");
    int listSize = bankList.size();
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
							Bank Master Info
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
								<td CLASS=LABEL> Bank Name</td>
								<td CLASS=LABEL> Bank Code</td>
								<td CLASS=LABEL> Account No.</td>
								<td CLASS=LABEL> IFSC Code</td>
								<td>
									<img src="./PensionView/images/page_edit.png"  />&nbsp;
								</td>
							</tr>						
<%
	}
	for(int cnt=0;cnt<listSize;cnt++){
		BankMasterInfo info = (BankMasterInfo)bankList.get(cnt);
%>
							<tr>
								<td CLASS=Data><%=info.getBankName()%>&nbsp;</td>
								<td CLASS=Data><%=info.getBankCode()%>&nbsp;</td>
								<td CLASS=Data><%=info.getAccountNo()%>&nbsp;</td>
								<td CLASS=Data><%=info.getIFSCCode()%>&nbsp;</td>
								<td>
								<input type="checkbox" name="accNo" value="<%=info.getAccountNo()%>" onclick="javascript:sendBankInfo('<%=info.getAccountNo()%>','<%=info.getBankName()%>','<%=info.getAccountCode()%>','<%=info.getParticular()%>','<%=info.getTrustType()==null?"":info.getTrustType()%>')" />
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
