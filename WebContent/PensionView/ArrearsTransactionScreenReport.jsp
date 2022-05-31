

<%@ page language="java" import="java.util.*,aims.common.CommonUtil"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
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
<base href="http://localhost:8080/PensionFinal/">
<LINK rel="stylesheet" href="http://localhost:8080/PensionFinal/PensionView/css/aai.css"
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
  	CommonUtil commonUtil=new CommonUtil();
   if (request.getAttribute("empList") != null) {
	   ArrayList dataList = new ArrayList();
	 	dataList =(ArrayList) request.getAttribute("empList");
	 	int count = 0;
	  	String username="",region="",expdate="";
	 for (int i = 0; i < dataList.size(); i++) {
	   count++;
	  ArrearsTransactionBean beans = (ArrearsTransactionBean) dataList.get(i);
	 %>

	<tr>
		<td align="center" colspan="5">
		<table border=0  cellpadding=3 cellspacing=0 width="40%" align="center"
			valign="middle">
			<tr>
				<td><img src="http://localhost:8080/PensionFinal/PensionView/images/logoani.gif">
				</td>
				<td class="label" align="center" valign="top" nowrap="nowrap">
				<font color='black' size='4' face='Helvetica'> AIRPORTS
				AUTHORITY OF INDIA </font></td>

			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="0" cellpadding="2" cellspacing="0" width="100%"
			align="center">
			<tr>
				<td align="center" class="reportsublabel" colspan="2">STATEMENT
				OF ARREAR OF PENSION CONTRIBUTION DUE TO REVISION OF PAY SCALES</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table border="1" style="border-color: gray" cellpadding="2"
			cellspacing="0" width="95%" align="center">
			
			<tr>
				<td class="reportsublabel">PF ID</td>
								<td class="reportdata"><%=beans.getPensionNumber()%></td>												
									<td class="reportsublabel">EMPNO</td>
									<td class="reportdata">--</td>		
												
							</tr>
							<tr>
								<td class="reportsublabel">EMP NAME</td>
								<td class="reportdata"><%=beans.getEmpName()%></td>
							<td class="reportsublabel">DATE OF BIRTH</td>
								<td class="reportdata"><%=beans.getDateofBirth()%></td>
														
								
							</tr>
							<tr>								
									<td class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
								<td class="reportdata"><%=beans.getFhName()%></td>
								<td class="reportsublabel">DOS (58 YEARS)</td>
								<td class="reportdata"><%=beans.getDateOfAnnuation()%></td>
								
   </tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<td colspan="5">

		<tr cellspacing="0">
		

		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="0" width="95%" align="center">

			<tr>
			<td width="20.5%">MONTH </td>
			<td  width="40%"colspan="3" align="center" class="label">SALARY
				</td>
			<td width="40%" colspan="3" align="center" class="label">PENSION
				CONTRIBUTIN</td>
				</td>
				</tr>
			
			<tr>

			<td width="20.5%">&nbsp; </td>		
				<td width="13%" class="label">DUE</td>
				<td  width="13%" class="label">DRAWN</td>
				<td width="13%"class="label">DIFFERENCE</td>
				
				
				<td width="13%" class="label">DUE</td>
				<td width="13%" class="label">DRAWN</td>
				<td width="13%" class="label">DIFFERENCE</td>
				
			</tr>
 <%}} %>
			<%
				if (request.getAttribute("transacitondata") != null) {
					ArrayList dataList1 = new ArrayList();
					dataList1 = (ArrayList) request.getAttribute("transacitondata");
					double pensioncontridrawntotal=0,pensioncontridifftotal=0,pensioncontriduetotal=0;
					double saldifftotal=0,saldrawtotal=0,salduetotal=0;
					for (int k = 0; k < dataList1.size(); k++) {
						double saldiff =0.00,pensioncontridrawn=0.0,pensioncontridiff=0.0,pensioncontridue=0.0;
						ArrearsTransactionBean beans1 = (ArrearsTransactionBean) dataList1.get(k);
						 pensioncontridrawn = Math.round(Double
								.parseDouble((beans1.getEmoluments())) * 0.0833);
						 pensioncontridiff = Math.round(Double
								.parseDouble(beans1.getArrearAmount()));
						 if(beans1.getDueEmoluments().equals("0")&& beans1.getDueEmoluments()!=null){
							 saldiff=Math.round(Double.parseDouble(beans1.getArrearAmount())*100/8.33);
								}else{
									saldiff=Double.parseDouble(beans1.getDueEmoluments());
								}
										
						double saldraw = Math.round(Double.parseDouble(beans1
								.getEmoluments()));
						double saldue =0.0;
					if(saldiff>0){
						 saldue = saldraw + saldiff;
						 pensioncontridue = pensioncontridrawn
							+ pensioncontridiff;
						}else{
						//	saldue=0.0;
						//	pensioncontridue=0.0;
							 saldue = saldraw + saldiff;
							 pensioncontridue = pensioncontridrawn
								+ pensioncontridiff;
						}
						
						pensioncontridrawntotal=pensioncontridrawntotal+pensioncontridrawn;	
						pensioncontridifftotal=pensioncontridifftotal+pensioncontridiff;
						pensioncontriduetotal=pensioncontriduetotal+pensioncontridue;
						saldifftotal=saldifftotal+saldiff;
						saldrawtotal=saldrawtotal+saldraw;
						salduetotal=salduetotal+saldue;
						
			%>
			<tr class="tboddrow">
				<td width="20.5%" class="Data" align="center"><%=beans1.getMonthYear().substring(beans1.getMonthYear().indexOf("-")+1,beans1.getMonthYear().length())%></td>
				<td width="14%" class="Data" align="right"><%=saldue%></td>
				<td width="14%" class="Data" align="right"><%=saldraw%></td>
				<td width="14%" class="Data" align="right"><%=saldiff%></td>
				<td width="14%" class="Data" align="right"><%=pensioncontridue%></td>
				<td width="14%" class="Data" align="right"><%=pensioncontridrawn%></td>
				<td width="14%" class="Data" align="right"><%=pensioncontridiff%></td>
			</tr>
			<%}%>
<tr class="tboddrow">
<tr cellspacing="0" class="tboddrow"/>
<td  class="HighlightData"  align="center">TOTAL</td>
				<td class="HighlightData" align="right"><%=salduetotal%></td>
				<td class="HighlightData" align="right"><%=saldrawtotal%></td>
				<td class="HighlightData" align="right"><%=saldifftotal%></td>
				<td class="HighlightData" align="right"><%=pensioncontriduetotal%></td>
				<td class="HighlightData" align="right"><%=pensioncontridrawntotal%></td>
				<td class="HighlightData" align="right"><%=pensioncontridifftotal%></td>
</tr>

			<%}%>
			

		</table>
		</td>
	</tr>


</table>

</body>
</html>

