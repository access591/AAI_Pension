
<%@ page language="java" import="java.util.*,aims.common.CommonUtil"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<title>AAI</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
</head>

<body>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<%
		CommonUtil commonUtil = new CommonUtil();
		if (request.getAttribute("empList") != null) {
			ArrayList dataList = new ArrayList();
			dataList = (ArrayList) request.getAttribute("empList");
			int count = 0;
			for (int i = 0; i < dataList.size(); i++) {
				count++;
				ArrearsTransactionBean beans = (ArrearsTransactionBean) dataList
						.get(i);
		%>

	<tr>
		<td align="center" colspan="5">
		<table border=0 cellpadding=3 cellspacing=0 width="40%" align="center"
			valign="middle">
			<tr>
				<td><img src="<%=basePath%>PensionView/images/logoani.gif">
				</td>
				<td class="label" align="center" valign="top" nowrap="nowrap">
				<font color='black' size='4' face='Helvetica'> AIRPORTS
				AUTHORITY OF INDIA</font></td>

			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellpadding="2" cellspacing="1" width="100%"
			align="center">
			<tr>
				<td align="center" class="reportsublabel"
					style="text-decoration: underline" colspan="2">STATEMENT OF
				WAGES & PENSION CONTRIBUTION FOR THE PERIOD OF 12 MONTHS PRECEDING
				THE DATE OF LEAVING SERVICE</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="1" style="border-color: gray" cellpadding="2"
			cellspacing="0" width="95%" align="center">
			<tr>

				<td width="25%" class="reportsublabel">PF ID</td>
				<td class="reportdata"><%=beans.getPensionNumber()%></td>
				<td width="25%" class="reportsublabel">EMPNO</td>
				<td class="reportdata"><%=beans.getEmpNumber()%></td>

			</tr>
			<tr>
				<td width="25%" class="reportsublabel">EMP NAME</td>
				<td class="reportdata"><%=beans.getEmpName()%></td>
				<td width="25%" class="reportsublabel">Statutory Rate of
				Contribution</td>
				<td class="reportdata">1.16% and 8.33%</td>


			</tr>
			<%
				Date transdate = null;
						DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
						transdate = df.parse(beans.getDateofJoining());
						System.out.println("transdate" + transdate);
						if (transdate.before(new Date("01-Apr-1995"))) {
							beans.setDateofJoining("01-Apr-1995");
						}
			%>
			<tr>
				<td width="25%" class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
				<td class="reportdata"><%=beans.getFhName()%></td>
				<td width="25%" class="reportsublabel">Date of Commencement of
				membership of EPS</td>
				<td class="reportdata"><%=beans.getDateofJoining()%></td>
			</tr>
			<tr>
				<td width="25%" class="reportsublabel">DATE OF BIRTH</td>
				<td class="reportdata"><%=beans.getDateofBirth()%></td>
				<td width="25%" class="reportsublabel">UNIT</td>
				<td class="reportdata"><%=beans.getRegion()%></td>
			</tr>
			<tr>
				<td width="25%" class="reportsublabel">Name & Address of the
				Establishment</td>
				<td class="reportdata">Airports Authority Of India,<br>
				Rajiv Gandhi Bhawan,<br>
				Safdarjung Airport,<br>
				New Delhi - 110 003.</td>
				<td width="25%" class="reportsublabel">Voluntary Higher rate of
				employees'cont.,if any</td>
				<td class="reportdata">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<%
		}
		}
	%>
	<tr>
		<td colspan="5">

		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="1" width="95%" align="center">

			<tr class="tboddrow">
				<td align="center" class="label">Year</td>
				<td align="center" class="label">Month</td>
				<td colspan="2" align="center" class="label">WAGES</td>
				<td align="center" class="label">PENSION<BR>
				CONTRIBUTION</td>
				<td colspan="2" align="center" class="label">Details of period
				of non_contributory service, if thereis no such period indicate
				"NIL"</td>
				<td align="center" class="label">REMARKS</td>
			</tr>
			<tr class="tboddrow">
				<td width="12%" class="label">&nbsp;</td>
				<td width="12%" class="label">&nbsp;</td>
				<td width="12%" rowspan="2" class="label" align="center">NO. OF
				DAYS</td>
				<td width="12%" rowspan="2" class="label" align="center">AMOUNT</td>
				<td width="12%" class="label">&nbsp;</td>
				<td width="12%" rowspan="2" class="label" align="center">YEAR</td>
				<td width="12%" rowspan="2" class="label" align="center">No. of
				days for which no wages were earned</td>
				<td width="12%" class="label">&nbsp;</td>
			</tr>
		</table>
		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="0" width="95%" align="center">
			<%
				if (request.getAttribute("transactionList") != null) {
					ArrayList dataList1 = new ArrayList();
					dataList1 = (ArrayList) request.getAttribute("transactionList");
					//String WetherOption=(String)request.getAttribute("wetheroption");
					// System.out.println("WetherOption in screen "+WetherOption);
					double amount = 0.0, pensioncontriemoluments = 0.0, DueEmoluments = 0.0, emoluments = 0.0, pensioncontri = 0.0;
					double amounttotal = 0.0, pensioncontritotal = 0.0;
					String remarks = "";
					for (int k = 0; k < dataList1.size(); k++) {
						ArrearsTransactionBean beans1 = (ArrearsTransactionBean) dataList1
								.get(k);
						String date = beans1.getMonthYear();
						String date1[] = date.split("-");
						System.out.println(date1[1]);
						String WetherOption = (String) request
								.getAttribute("wetheroption");
						System.out
								.println("WetherOption in screen " + WetherOption);
						emoluments = Math.round(Double.parseDouble(beans1
								.getEmoluments()));
						System.out.println("emoluments" + emoluments);
						DueEmoluments = Math.round(Double.parseDouble(beans1
								.getDueEmoluments()));
						System.out.println("pensioncontriemoluments"
								+ pensioncontriemoluments);
						if (beans1.getArrearAmount().equals("0")
								|| beans1.getArrearAmount().equals("")) {
							remarks = "PRE-REVISED PAY";
							amount = emoluments;
						} else {
							remarks = "REVISED PAY";
							amount = emoluments + DueEmoluments;
						}
						if (WetherOption.equals("A")) {
							pensioncontri = Math.round((amount * 8.33) / 100);
						} else {
							pensioncontri = 541;
						}

						amounttotal = amounttotal + amount;
						pensioncontritotal = pensioncontritotal + pensioncontri;
			%>
			<tr class="tboddrow">
				<td width="12%" class="Data" align="center"><%=date1[2]%></td>
				<td width="12%" class="Data" align="left"><%=date1[1]%></td>
				<td width="12%" class="Data" align="center"><%=date1[0]%></td>
				<td width="12%" class="Data" align="right"><%=amount%></td>
				<td width="12%" class="Data" align="right"><%=pensioncontri%></td>
				<td width="12%" class="Data" align="right">&nbsp;</td>
				<td width="12%" class="Data" align="center">NIL</td>
				<td width="12%" class="Data" align="left"><%=remarks%></td>
			</tr>
			<%
				}
			%>
			<tr class="tboddrow">
				<td colspan="3" class="HighlightData" align="center">TOTAL</td>
				<td class="HighlightData" align="right"><%=amounttotal%></td>
				<td class="HighlightData" align="right"><%=pensioncontritotal%></td>
				<td colspan="3" class="HighlightData" align="right">&nbsp;</td>

			</tr>

			<%
				}
			%>

		</table>
		</td>
	</tr>





</table>

</body>
</html>
