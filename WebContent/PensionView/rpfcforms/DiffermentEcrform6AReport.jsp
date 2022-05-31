
<%@ page language="java" import="java.util.*,aims.common.CommonUtil"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<title>AAI</title>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
			type="text/css">
	</head>

	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								&nbsp;
							</td>
							<td width="7%" rowspan="2">
								<img src="<%=basePath%>PensionView/images/logoani.gif"
									width="100" height="50" />
							</td>
							<td>
								AIRPORTS AUTHORITY OF INDIA
							</td>
						</tr>
						<tr>
							<td width="38%">
								&nbsp;
							</td>
							<td width="55%">
								&nbsp;
							</td>
						</tr>

						<tr>
							<td colspan="3" align="center" class="reportlabel">
								MONTHLY SCHEDULE OF EMPLOYEE-WISE PENSION CONTRIBUTION FOR THE
								MONTH
								<font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font>
								(FY<%=request.getAttribute("dspYear")%>.)
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
			<%
				ArrayList form3DataList = new ArrayList();

				String reportType = "", fileName = "", region = "", stationName = "", filePrefix = "", filePostfix = "";
				CommonUtil commonUtil = new CommonUtil();
				DecimalFormat df = new DecimalFormat("#########0");

				if (request.getAttribute("region") != null) {
					region = (String) request.getAttribute("region");
				}
				if (request.getAttribute("airportcode") != null) {
					stationName = (String) request.getAttribute("airportcode");
				}
				if ("NO-SELECT".equals(stationName)) {
					stationName = region;
					filePostfix = stationName;
				} else {
					filePostfix = region + "_" + stationName;
				}
			%>

			<%
				double grossTotal = 0, emolumentTotal = 0, addContriGrandTotal = 0, pensioncontributiontotal = 0;

				if (request.getAttribute("cardList") != null) {

					if (request.getAttribute("reportType") != null) {
						reportType = (String) request.getAttribute("reportType");
						if (reportType.equals("Excel Sheet")
								|| reportType.equals("ExcelSheet")) {

							filePrefix = "AAI_ECHallan-6A_Report";
							fileName = filePrefix + filePostfix + ".xls";
							response.setContentType("application/vnd.ms-excel");
							response.setHeader("Content-Disposition",
									"attachment; filename=" + fileName);
						}
					}
					form3DataList = (ArrayList) request.getAttribute("cardList");
			%>
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
			<tr>
				<td>
					<table width="100%" border="1" cellspacing="0" cellpadding="0"
						class="Sample">
						<tr>

							<td width="3%" class="reportsublabel">
								Sl.No
							</td>
							<td width="12%" class="reportsublabel">
								SAP Employee No.
							</td>
							<td width="8%" class="reportsublabel">
								PFID
							</td>
							<td width="12%" class="reportsublabel">
								UAN
							</td>
							<td width="15%" class="reportsublabel">
								EMPLOYEE NAME
							</td>
							<td width="8%" class="reportsublabel">
								GROSS
							</td>
							<td width="8%" class="reportsublabel">
								EMOLUMENTS
							</td>
							<td width="4%" class="reportsublabel">
								PENSION CONTRIBUTION
							</td>
							<td width="4%" class="reportsublabel">
								ADDITIONAL CONTRIBUTION
							</td>
							<td align="center" class="reportsublabel">
								WHETHER OPTION
							</td>
							<td align="center" class="reportsublabel">
								AIRPORT CODE
							</td>
							<td align="center" class="reportsublabel">
								REGION
							</td>


						</tr>


						<tr></tr>
						<%
							AaiEpfform3Bean epfForm3Bean = new AaiEpfform3Bean();
								int srlno = 0, sapempno=0;
								
								double diff = 0, grandDiffe = 0, emolumentsByEpf = 0, emolBypensionContri = 0, pensionContri = 0, epf = 0, noofDays = 0;

								for (int cardList = 0; cardList < form3DataList.size(); cardList++) {
									epfForm3Bean = (AaiEpfform3Bean) form3DataList
											.get(cardList);
									srlno++;
						%>

						<tr>
							<td class="NumData"><%=srlno%></td>
							<td class="NumData"><%=epfForm3Bean.getSapempno()%></td>
							<td class="NumData"><%=epfForm3Bean.getPensionno()%></td>
							<td class="NumData"><%=epfForm3Bean.getUanno()%></td>
							<td class="Data"><%=epfForm3Bean.getEmployeeName()%></td>
							<td class="NumData"><%=epfForm3Bean.getGross()%></td>
							<td class="NumData"><%=epfForm3Bean.getEmoluments()%></td>
							<td class="NumData"><%=epfForm3Bean.getPensionContribution()%></td>
							<td class="NumData"><%=epfForm3Bean.getAdditionalContri()%></td>
							<td class="Data"><%=epfForm3Bean.getWetherOption()%></td>
							<td class="Data"><%=epfForm3Bean.getRegion()%></td>
							<td class="Data"><%=epfForm3Bean.getStation()%></td>

						</tr>
						<%
							if (epfForm3Bean.getGross().equals("---")
											|| epfForm3Bean.getGross().equals("")) {
										epfForm3Bean.setGross("0.00");
									}

									grossTotal += Double.parseDouble(epfForm3Bean.getGross());

									emolumentTotal += Double.parseDouble(epfForm3Bean
											.getEmoluments());

									pensioncontributiontotal += Double.parseDouble(epfForm3Bean
											.getPensionContribution());

									addContriGrandTotal += Double.parseDouble(epfForm3Bean
											.getAdditionalContri());

								}
						%>



						<tr>

							<td class="NumData" colspan=2 align="right">
								No Of Employees
							</td>
							<td class="NumData"><%=Math.round(srlno)%></td>
							<td class="NumData" colspan=2 align="right">
								Grand Total
							</td>

							<td class="NumData"><%=Math.round(grossTotal)%></td>
							<td class="NumData"><%=Math.round(emolumentTotal)%></td>
							<td class="NumData"><%=Math.round(pensioncontributiontotal)%></td>
							<td class="NumData"><%=Math.round(addContriGrandTotal)%></td>








							<td class="DataRight" colspan="6">
								&nbsp;
							</td>



						</tr>




					</table>
				</td>
			</tr>
			<%
				}
			%>



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

		</table>
	</body>
</html>
