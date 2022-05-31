
<%@ page language="java" import="java.util.ArrayList,java.util.List"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.cashbook.PartyInfo"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>

<%
StringBuffer basePath = new StringBuffer(request.getScheme());
	 basePath.append("://").append(request.getServerName()).append(":");
	 basePath.append(request.getServerPort()).append(request.getContextPath());
	 basePath.append("/");
	 
	CommonUtil util = new CommonUtil();
	HashMap regions = util.getRegion();	
	Set keys = regions.keySet();
	Iterator it = keys.iterator();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI - CashBook - Bank Info</title>
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript">    			
 	
		 	function testSS(){
		 		var region=document.forms[0].region.value;
		 		if(region==''){
		 			alert('Please Select region');
		 			document.forms[0].region.focus();
		 			return false;
		 		}		   		
		   		if(region == 'AllRegions')
		   			region='';
		   		var empName=document.forms[0].empName.value;
		   		var url ="<%=basePath%>Employee?method=getEmployeeList&region="+region+"&empName="+empName;		   		
		   		document.getElementById("info").innerHTML="<img src='<%=basePath%>PensionView/images/loading1.gif'>";
			    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
				return false;
			}
	
	
			 function sendInfo(pfid,name,desig){			 
				 window.opener.empDetails(pfid,name,desig);	 
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
									Region Name:
								</td>
								<td>
									<select name="region" style="width:100px">
										<option value="">
											[Select One]
										</option>
										<option value="AllRegions">
											AllRegions
										</option>
<%
while (it.hasNext()) {
	String region = regions.get(it.next()).toString();
%>
										<option value="<%=region%>">
											<%=region%>
										</option>
<%
	}
%>
									</select>
								</td>
								<td class="label">
									Emp Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
									&nbsp;
								</td>
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td id="info">
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
if (request.getAttribute("EmpList") != null) {
	List dataList = (ArrayList) request.getAttribute("EmpList");
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
						Employee Info
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
									Employee Name
								</td>
								<td class="LABEL">
									Employee Number
								</td>
								<td class="LABEL">
									Pension Number
								</td>
								<td class="LABEL">
									Designation
								</td>
								<td>
									<img src="./PensionView/images/page_edit.png" alt="" />
									&nbsp;
								</td>
							</tr>
							<%
	}
	for(int cnt=0;cnt<listSize;cnt++){
		EmployeePersonalInfo info = (EmployeePersonalInfo)dataList.get(cnt);
%>
							<tr>
								<td class="Data">
									<%=info.getEmployeeName()%>
									&nbsp;
								</td>
								<td class="Data">
									<%=info.getEmployeeNumber()%>
									&nbsp;
								</td>
								<td class="Data">
									<%=info.getPensionNo()%>
									&nbsp;
								</td>
								<td class="Data">
									<%=info.getDesignation()%>
									&nbsp;
								</td>
								<td>
									<input type="checkbox" name="pfid" value="<%=info.getPensionNo()%>" onclick="javascript:sendInfo('<%=info.getPensionNo()%>','<%=info.getEmployeeName()%>','<%=info.getDesignation()%>')" />
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
