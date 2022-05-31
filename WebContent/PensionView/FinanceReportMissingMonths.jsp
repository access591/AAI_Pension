

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";%>

<%@ page language="java" import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
<script type="text/javascript">
    function redirectPageNav(navButton,index,region,totalValue){   
    	
		document.forms[0].action="<%=basePath%>validatefinance?method=missingMonthsReportNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&frm_region="+region;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
</script>
</HEAD>
<body>
<form action="method">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
					<table  border=0 cellpadding=3 cellspacing=0 width="40%" align="center" valign="middle">
					<tr>
					<td colspan="3"><img src="<%=basePath%>PensionView/images/logoani.gif" width="65" height="70">
					</td>
				
					<td colspan=4 class="label" align="center"	>
					<table border=0 cellpadding=3 cellspacing=0 width="100%" align="center" valign="middle">
					<tr>
						<td class="label" align=center nowrap><font color='black' size='4' face='Helvetica'>
						AIRPORTS AUTHORITY OF INDIA
						</font></td>
						
					</tr>
					<tr>
					<td align="center" class="reportlabel">Missing Months Report</td>
					</tr>
					<tr>
						<td class="reportsublabel" align="center" >Based on</td>
						<td class="reportdata" align="left" nowrap><%=request.getAttribute("region")%></td>
					</tr>
					</table>
					
					</td>
					
					</tr>
					</table>
					</td>
					</tr>
					<%
					if(request.getAttribute("searchBean")!=null){
					SearchInfo searchBean = new SearchInfo();
					String region="";
					EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
					int totalData = 0;
					FinancialService financeService=new FinancialService();
					searchBean=(SearchInfo)request.getAttribute("searchBean");
					BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
					ArrayList financePersonlList=new ArrayList();
					ArrayList dataList=new ArrayList();
					financePersonlList=searchBean.getSearchList();
					totalData = searchBean.getTotalRecords();
					bottomGrid = searchBean.getBottomGrid();
					region=(String)request.getAttribute("region");
					int index = searchBean.getStartIndex();
					%>
			<tr>
				<td>
						<table align="center"  >
							<tr>
								
								<td  align="right">
									<input type="button" class="btn" alt="first" value="|<" name="First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=region%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" class="btn" alt="pre" value="<" name="Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=region%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" class="btn" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=region%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" class="btn" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=region%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								</td>
							</tr>
						</table>
					</td>
				</tr>
					
				<tr>
					<td>
						<table width="100%"  border="1" cellpadding="0" cellspacing="0">
							<tr>
								<td class="label" width="16%">Employee</br> Number</td>
								<td class="label" width="19%">CPF ACC No</td>
								<td class="label" width="19%">Employee Name</td>
								<td class="label" width="19%">Aiport Code</td>
								<td class="label">Missing Info</td>
								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="1" cellpadding="0" cellspacing="0">
		<%
			
			

			String employeeNo="",dbCPFaccno="";
			for(int j=0;j<financePersonlList.size();j++){
			 personalInfo=(EmployeePersonalInfo)financePersonlList.get(j);
			 employeeNo=personalInfo.getEmployeeNumber();
	         dbCPFaccno=personalInfo.getCpfAccno();
     		dataList=financeService.financeReportMissingMonthsByEachEmp(dbCPFaccno,employeeNo,region);%>
				<%	if (dataList.size() != 0) {
									int count = 0;
									String missinInfo="",employeeName="",airportCode="",cpfaccno="",employeeNumber="";
									for (int i = 0; i < dataList.size(); i++) {
										count++;
										EmployeeValidateInfo beans = (EmployeeValidateInfo) dataList.get(i);
										missinInfo=beans.getMissingInfo();
										if(!missinInfo.equals("")){
										
										cpfaccno=beans.getCpfaccno();
										airportCode=beans.getAirportCD();
										employeeName=beans.getEmployeeName();
										employeeNumber=beans.getEmployeeNo();
										
										%>
			
							<tr>
								<td class="Data" width="16%"><%=employeeNumber.trim()%></td>
								<td class="Data" width="19%"><%=cpfaccno%></td>
								<td class="Data" width="19%"><%=employeeName.trim()%></td>
								<td class="Data" width="19%"> <%=airportCode.trim()%></td>
								<td class="Data"><%=missinInfo.trim()%></td>
							</tr>
							<%}}}%>
					
						<%}	%>
						</table>
					</td>
				</tr>		
				<%}%>
			</table>
		</form>
</body>
</html>