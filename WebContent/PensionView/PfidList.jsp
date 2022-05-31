

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";%>

<%@ page language="java" import="java.util.*,java.sql.Connection,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.bean.RegionBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>
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
					<td align="center" colspan="5">
						<table border=0 cellpadding=3 cellspacing=0 width="100%" align="center" valign="middle">
							<tr>
								<td>
									<img src="<%=basePath%>PensionView/images/logoani.gif">
								</td>
								<td class="label" align=center valign="top" nowrap="nowrap">
									<font color='black' size='4' face='Helvetica'> AIRPORTS AUTHORITY OF INDIA </font>
								</td>

							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="5" align="center" width="100%">
						<table border=0 cellpadding=0 cellspacing=0 width="100%" align="center" valign="middle">
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td colspan="4" align="center" class="reportlabel">
									FORM-3
									<br />
									(paragraph 20(2) of the employees pension scheme, 1995)
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center" class="reportlabel">
									CONSOLIDATED RETURN OF EMPLOYEE WHO ARE ENTITLED AND REQUIRED TO BECOME
									<br />
									MEMBER OF THE PENSION FUND ON THE DATE THE PENSION SCHEME COMES INTO FORCE.
								</td>
							</tr>

						</table>
					</td>
				</tr>
			</table>
			<%String reportType = "", sortingOrder = "", airportCode = "", frmAirportCode = "", tempFileName = "", fileName = "";
			int srlno = 0;
			String region = "";
			ArrayList pfidList = new ArrayList();
			form3Bean formsetBean =new form3Bean();
			ArrayList airportList = new ArrayList();
			if (request.getAttribute("PFIDLIST") != null) {
				pfidList = (ArrayList) request.getAttribute("PFIDLIST");
			}
			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (!reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
					
					fileName = "PFIDLIST_report.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			
			FinancialService financeService = new FinancialService();
			System.out.println("PFID LiSt in jsp "+pfidList.size());
			 %>
	    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td> 
         <%	for (int rglist = 0; rglist < pfidList.size(); rglist++) {
				formsetBean = (form3Bean) pfidList.get(rglist);
				System.out.println(" "+formsetBean.getPfID());
			 %>
		
					<tr>						
								<td class="Data" width="12%">
									<%=formsetBean.getPfID()%>
								</td>
								<td class="Data" width="12%">
									<%=formsetBean.getCpfaccno()%>
								</td>
								<td class="Data" width="9%">
									<%=formsetBean.getEmployeeNo()%>
								</td>
								<td class="Data" width="20%">
									<%=formsetBean.getEmployeeName()%>
								</td>
								<td class="Data" width="12%">
									<%=formsetBean.getDesignation()%>
								</td>
								<td class="Data" width="15%">
									<%=formsetBean.getAirportCode()%>
								</td>
								<td class="Data" width="18%">
									<%=formsetBean.getDateOfBirth()%>
								</td>
													
								<td class="Data" width="3%">
									<%=formsetBean.getWetherOption()%>
								</td>
							</tr>	
				
                <% }%> 
					</td>
				</tr>		
			
			</table>
		</form>
	</body>
</html>
