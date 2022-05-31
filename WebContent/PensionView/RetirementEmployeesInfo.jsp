<%@ page language="java" import="java.util.*,java.sql.Connection,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.bean.RegionBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			CommonUtil commonUtil= new CommonUtil();
			%>


<html>
	<HEAD>
	   <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
</HEAD>
	<body>
		<form action="method">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">


				<tr>
					<td align="center" colspan="5">
						<table border=0 cellpadding=3 cellspacing=0 width="100%" align="center" valign="middle">
<tr>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
  <td>&nbsp;</td>
    <td colspan="7" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif"  width="75" height="55" align="right" /></td>
    <td nowrap="nowrap" rowspan="2"  >AIRPORTS AUTHORITY OF INDIA</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>

 </tr>
  <tr>
    <td nowrap="nowrap"></td>  
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
 
  </tr> 
  
  <tr>
    <td colspan="13"><table cellspacing="0" cellpadding="0" border="0">
      <tr>
      <td colspan="4" width="120"> &nbsp;</td>
        <td height="24" colspan="12" width="620" align="center">Monthly  Seperation Employee Info For The Month of <font><B><%= (request.getAttribute("displayDate")).toString().substring(3,11) %></B></font></td>
          
 
      </tr><tr>
      <%if(request.getAttribute("region")!=""){%>
      <td colspan="12" align="left" nowrap="nowrap" class="Data"><b>&nbsp;REGION:</b> <font style="text-decoration: underline"><%=(request.getAttribute("region"))%></font></td>

 <%}else {%>
 <td colspan="12" width="140"> &nbsp;</td>
  <%}%>
<td  align="right" nowrap="nowrap"  class="Data"></td>
<td align="right" nowrap="nowrap" class="Data">Date:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>

</tr>
<tr>
<%	if(request.getAttribute("airportCode")!="" ){%>
	 <td  align="left" nowrap="nowrap"  class="Data"><b>AIRPORTCODE:</b> <font style="text-decoration: underline"><%=(request.getAttribute("airportCode"))%></font></td>
	 <%}else {%>
	 <td colspan="4" width="120"> &nbsp;</td>
	 <%}%>
	 <td colspan="4" width="120"> &nbsp;</td>
	 <td colspan="4" width="120"> &nbsp;</td>
	<td align="right" nowrap="nowrap" class="Data"></td>

</tr>
    </table></td>
    
    
    
    
  </tr>
 
						

						</table>
					</td>
				</tr>
			</table>
			<%String reportType = "", sortingOrder = "", airportCode = "",region="", frmAirportCode = "", tempFileName = "", fileName = "";
			int srlno = 0;
			
			if(request.getAttribute("region")!=null){
    	region=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      airportCode=(String)request.getAttribute("airportCode");
    }
			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
						tempFileName = "All_Regions";
					fileName = "Retired " + tempFileName + "_report.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			
			

			
			

				
				
				ArrayList dataList = new ArrayList();
				ArrayList retireDataList = new ArrayList();
				EmployeePersonalInfo formsetBean = null;
				String formDate = "", retiredDate = "";
				
			

				retiredDate = (String) request.getAttribute("displayDate");
			
				if(request.getAttribute("retdList")!=null){
					retireDataList=(ArrayList)request.getAttribute("retdList");
				}
				
			

					if (retireDataList.size() == 0) {

                %>
			<tr>

				<td>
					<table align="center" id="norec">
						<tr>
							<br>
							<td>
								<b> No Records Found </b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<%} else if (retireDataList.size() != 0) { %>  
			

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">

							<tr>
								<td>
									
								</td>
							</tr>
					
							<tr>
								<td>
									<%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Name & Address of Establishment
											</td>
											<td class="reportdata">
												Airports Authority Of India,
												<br />
												Rajiv Gandhi Bhawan,Safdarjung Airport,New Delhi-3
											</td>
											<td class="reportsublabel" align="right" width="20%">
												Date of Coverage:
											</td>
											<td class="reportdata">
												01-Apr-1995
											</td>
										</tr>

									</table>
								--%></td>
							</tr>

							<tr>
								<td>
									<%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Industry in which the        
											</td>
											<td class="reportdata">
												Civil Aviation
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<br/>
											</td>
											<td class="reportsublabel" align="right" width="28%">
												Code No. of the
											</td>
											<td class="reportdata">
												&nbsp;&nbsp;&nbsp;T.N./DL/36478
												<br/>
											</td>
										</tr>
								
									
									</table>
								--%></td>
							</tr>
							<tr>
								<td>
									<%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Establishment is engaged :
											</td>
											<td class="reportdata" width="51%">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="reportsublabel"  width="29%">
												Establishment :
											</td>
											<td class="reportdata">
												&nbsp;&nbsp;&nbsp;
												<br/>
											</td>
										</tr>
								
									</table>
								--%></td>
							</tr>
								<tr>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												&nbsp;
											</td>
											<td class="reportdata">
											</td>
											
										</tr>
								
									</table>
								</td>
							</tr>
							
						</table>
					</td>


				</tr>
			</table>

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table width="100%" border="1" bordercolor="gray" cellpadding="2" cellspacing="0">

						
							<tr>
								<td class="label" >S.No.</td>
								<td class="label">Pension A/c No</td>
								<td class="label" >Emp</br> Code</td>
								<td class="label">Employee Name</td>
								<td class="label" >Designation</td>
								<td class="label" >Father's name or <br/>Husband name  <br/>( in case of married women)</td>
								<td class="label" nowrap="nowrap">Date of birth  </td>
								<td class="label" nowrap="nowrap">Date of Joining  </td>
								<td class="label" nowrap="nowrap">Date of Seperation </td>
								<td class="label" >Station</td>
								<td class="label" >Region</td>
								
							</tr>
							

							<%for (int k = 0; k < retireDataList.size(); k++) {
							formsetBean = (EmployeePersonalInfo) retireDataList.get(k);
							srlno++;
							
							%>
						<tr>
								<td class="Data" width="2%">
									<%=srlno%>
								</td>
								<td class="Data" width="12%">
									<%=formsetBean.getPensionNo() %>
								</td>
									<td class="Data" width="3%">
									<%=formsetBean.getEmployeeNumber()%>
								</td>
								<td class="Data" width="20%">
									<%=formsetBean.getEmployeeName()%>
								</td>
								<td class="Data" width="12%">
									<%=formsetBean.getDesignation()%>
								</td>
								<td class="Data" width="15%">
									<%=formsetBean.getFhName().trim()%>
								</td>
								<td class="Data" width="18%">
									<%=formsetBean.getDateOfBirth()%>
								</td>
								<td class="Data" width="18%">
									<%=formsetBean.getDateOfJoining()%>
								</td>
								<td class="Data" width="3%">
									<%=formsetBean.getSeperationDate()%>
								</td>
					
								
								<td class="Data" width="15%">
									<%=formsetBean.getAirportCode()%>
								</td>
								<td class="Data" width="15%">
									<%=formsetBean.getRegion()%>
								</td>
							</tr>
								<%}%>
						</table>

					</td>
				</tr>
				<%if(retireDataList.size()!=0){%>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
					
				<%}%>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				<%}%>
			
					<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
						</table>
				

			
		</form>
	</body>
</html>
