

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
			ArrayList regionList = new ArrayList();
			RegionBean regionBean=new RegionBean();
			ArrayList airportList = new ArrayList();
			
			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				 
					fileName = "form-3_All_Regions_report.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			
			ArrayList dataList = new ArrayList();
				form3Bean formsetBean = null;
	
			dataList=(ArrayList)request.getAttribute("form3List");
					if (dataList.size() != 0) {

						%>

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">

							<tr>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">

										
										<tr>
											<td>
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td class="reportsublabel" width="10%">
															Region/Airport:
														</td>
														<td class="reportdata">
															<%=region%>
															/
															<%=airportCode%>
														</td>
													</tr>
												</table>
											</td>

										</tr>
									
										
										<tr>
											
											<td class="reportsublabel" width="10%">
												Month/Year:
											</td>
											<td class="reportdata" align="left">
												<%=request.getAttribute("displayDate")%>
											</td>
											

										</tr>
										
									</table>
								</td>
							</tr>
							
							<tr>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
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
								</td>
							</tr>

							<tr>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Industry in which the
												<br />
												Establishment is engaged :
											</td>
											<td class="reportdata">
												Civil Aviation
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="reportsublabel" align="right" width="28%">
												Code No. of the
												<br />
												Establishment:
											</td>
											<td class="reportdata">
												&nbsp;&nbsp;&nbsp;T.N./DL/36478
											</td>
										</tr>
								
										<tr>
											<td class="reportsublabel">
												Registration No. of the
												<br>
												Establishment :
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

							
							<jsp:include page="/PensionView/PensionForm3ReportSubHeading.jsp"></jsp:include>
							

							<%for (int k = 0; k < dataList.size(); k++) {
							formsetBean = (form3Bean) dataList.get(k);
							srlno++;
							
							%>
							<tr>
								<td class="Data" width="2%">
									<%=srlno%>
								</td>
								<td class="Data" width="12%">
									<%=formsetBean.getPensionNumber()%>
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
									<%=formsetBean.getFamilyMemberName().trim()%>
								</td>
								<td class="Data" width="10%">
									<%=formsetBean.getEmoluments()%>
								</td>
								<td class="Data" width="10%">
									<%=formsetBean.getAgeAtEntry()%>
								</td>
								<td class="Data" width="18%">
									<%=formsetBean.getDateOfBirth()%>
								</td>
								<td class="Data" width="18%">
									<%=formsetBean.getDateOfJoining()%>
								</td>
								<td class="Data" width="3%">
									<%=formsetBean.getSex()%>
								</td>
								<td class="Data" width="15%">
									<%=formsetBean.getDateOfEntitle()%>
								</td>
								<td class="Data" width="3%">
									<%=formsetBean.getWetherOption()%>
								</td>
								<td class="Data" width="15%">
									<%=formsetBean.getRemarks()%>
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
				<%}%>
				<tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>
				
				<tr>
					<td>
						<table width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td class="label">
									Date:
								</td>
							</tr>
							<tr>
								<td colspan="4" align="right" class="label">
									Signature of the employer or other
									</br>
									authorised officer of the Establishment
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>



			</table>
		</form>
	</body>
</html>
