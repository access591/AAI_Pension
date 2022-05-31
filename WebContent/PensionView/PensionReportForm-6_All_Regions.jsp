<%
 	String path = request.getContextPath(); 
 	String basePath =
	request.getScheme() + "://" + request.getServerName() + ":" +
	request.getServerPort() + path + "/";
	
	String labelAry[] = null;
	String subLabelAry[] = null;
	
%> 

<%@ page language="java"
import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,java.text.DecimalFormat"
contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%> 

<%@ page import="aims.bean.EmployeeValidateInfo"%> 
<%@ page import="aims.bean.EmployeePersonalInfo"%> 
<%@ page import="aims.service.FinancialService"%> 
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.common.CommonUtil" %>
<html>
<HEAD>
<title>PensionReportForm-6</title>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

</HEAD>
<body>
<form action="method">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	
	<%
		String reportType="",sortingOrder="cpfaccno";
		DecimalFormat df = new DecimalFormat("#########0.00");
		//if(request.getAttribute("airportList")==null){
		if(request.getAttribute("region")!=null){
		int srlno=0;
		String region="";
		String frmAirportCode = "";
		CommonUtil commonUtil=new CommonUtil();
		ArrayList regionList = new ArrayList();
		ArrayList airportList = new ArrayList();
		if(request.getAttribute("region")!=null){
		regionList = (ArrayList) request.getAttribute("region");
		}
		
	 if (request.getParameter("frm_reportType") != null) {

				reportType = (String) request.getParameter("frm_reportType");

				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
		
					String fileName = "form-6-report.xls";

					response.setContentType("application/vnd.ms-excel");

					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);

				}
				

			}
		
		FinancialService financeService=new FinancialService();
		ArrayList list=new ArrayList();
		ArrayList dataList=new ArrayList();
		form3Bean formsetBean=null;
		String formDate="",prevDate="",loadYear="";
		formDate=(String)request.getAttribute("selectedDate");
		prevDate=(String)request.getAttribute("prevDate");
		
		String airportCode="",loadNextYear="";
		loadYear=commonUtil.validateNumber(formDate.toCharArray());
		String [] yearList=loadYear.split(",");
		loadYear=yearList[0];
		loadNextYear=Integer.toString(Integer.parseInt(yearList[0])+1);
		System.out.println("formDate is ::::::::::::::::: "+formDate);
	//	int[] dataAry = financeService.financeForm6BCountData(airportCode,formDate,region,prevDate);
		int[] dataAry ={};
		int lastMonthSub=0,totalForm4Joinee=0,totalForm5Retried=0;
		int region_lastMonthSub=0,region_totalForm4Joinee=0,region_totalForm5Retried=0;
		int totalSubscriber=0,region_totalSubscriber=0;
		double totalEmoluments=0.0,totalContribution=0.0,region_totalEmoluments=0.0,region_totalContribution=0.0;
				
	//	System.out.println("dataAry is ::::::::::::::::: "+dataAry[0]+"============"+dataAry[1]);
	%>
	<tr>
		<td colspan="2">
		<table border="0" cellpadding="0" cellspacing="0" width="80%"
			align="center">
			<tr>
				<td  colspan=2 class="reportlabel" align=center>FORM-6</td>
			</tr>
			<tr>
				<td  colspan=2 class="label" align=center>(Paragraph 20 of the employees pension scheme, 1995)</td>
			</tr>
			<tr>
				<td  colspan=2 class="label"  align=center>Total No. of Employees__________________</font></td>
			</tr>
			<tr>
				<td  colspan=2 class="reportlabel"  align=center>STATEMEMT OF CONTRIBUTION FOR THE MONTH OF <font color=blue><%=prevDate.substring(2,prevDate.length())%></font></td>
			</tr>
			<tr>
				<td class="label" colspan=2 align="right">&nbsp;</td>
			</tr>
			<tr>
			
				<td class="label" colspan=2 align="right">Total No.of suscribers________________</td>
			</tr>
			
			<tr>
				<td class="label" width="35%">Name & Address of Establishment:</td>
				<td class="Data" align="left">Rajiv Gandhi Bhawan,<br/>Safdarjung Airport,New Delhi-3</td>
			</tr>
			<tr>
				<td class="label" width="35%">Currency period from</td>
				<td class="Data" align="left">1st April,<%=loadYear%>to 31st March,<%=loadNextYear%></td>
				

			</tr>
			<tr>
				<td class="label">Code No. of the Establishment:</td>
				<td class="Data">36478</td>
				
			</tr>
		   <tr>
				<td class="label">Statutory Rate of Contribution:</td>
				<td class="Data"> 8.33%</td>
			</tr>
	
			<tr>
				<td colspan=2 height=20>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>

	
	
	<tr>
		<td align=center>
		<table width="80%" border="1" bordercolor="gray" cellpadding="2"
			cellspacing="0">
	

			<% 
			int totalCnt = 0;
			String airport="";
			ArrayList formairportList=new ArrayList();
			if (request.getAttribute("airportcode") != null) {
				frmAirportCode = (String) request.getAttribute("airportcode");
			}
			
			System.out.println("frmAirportCode "+frmAirportCode);
			for (int rglist = 0; rglist < regionList.size(); rglist++) {
				region = (String) regionList.get(rglist);
				if (frmAirportCode.equals("NO-SELECT")) {
					airportList = financeService.getPensionAirportList(region,"");
				} else {
					airportList.add(frmAirportCode);
				}
			for(int ii=0;ii<airportList.size();ii++){
				airportCode=(String)airportList.get(ii);
				System.out.println("airportcode is "+airportCode);
						
				dataAry = financeService.financeRpfcFormsCountData(airportCode,formDate,region,prevDate);
				dataList=financeService.financeForm6Report(airportCode,formDate,region,sortingOrder);
			
				String aptName = "";
				String remarks = "";
				lastMonthSub=lastMonthSub+dataAry[2];
				totalForm4Joinee=totalForm4Joinee+dataAry[0];
				totalForm5Retried=totalForm5Retried+dataAry[1];
				srlno++;
				for(int k=0;k<dataList.size();k++){
					formsetBean=(form3Bean)dataList.get(k);
					region_totalSubscriber=region_totalSubscriber+formsetBean.getSubScribersCount();
					region_totalEmoluments=region_totalEmoluments+Double.parseDouble(formsetBean.getEmoluments());
					region_totalContribution=region_totalContribution+formsetBean.getEmpContrPension();
					totalSubscriber=totalSubscriber+formsetBean.getSubScribersCount();
					totalEmoluments=totalEmoluments+Double.parseDouble(formsetBean.getEmoluments());
					totalContribution=totalContribution+formsetBean.getEmpContrPension();
			}

			
			}%>
			<tr>
				<td class="label" colspan=6><font color=blue>Name Of Region :&nbsp;<%=region%></font></td>
			</tr>
					<tr>
				<th class='label' >Total No.of subscribers</th>
				<th class='label' >Wages or which contrubutions are payable</th>
				<th class='label' >Amount of contribution due 8.33%</th>
				<th class='label' >Amount of contrubution remitted in A/c No 10(to be filled by CHQ)</th>
				<th class='label' >Date of remittence (Triplicate copy of the challan to be enclosed) to be filled by CHQ</th>
				<th class='label' >Name and address of the bank in which amount is remitted (to be filled by CHQ)</th>
			</tr>
			
			<tr>
			<%
				for(int i=1;i<=6;i++){
					out.println("<th class='label'>"+i+"</th>");
				}
			%>
			</tr>
			<%
			%>
		
			<tr>
				<td class="Data" width="10%"><%=region_totalSubscriber%></td>
				<td class="Data" width="15%"><%=df.format(region_totalEmoluments)%></td>
				<td class="Data" width="15%"><%=df.format(region_totalContribution)%></td>
				<%
				if(formsetBean.getRemittedAmt()>0){
				%>
				<td class="Data" width="15%"><%=df.format(formsetBean.getRemittedAmt())%></td>
				<td class="Data" width="15%"><%=formsetBean.getRemittedDate()%></td>
				<td class="Data" width="15%"><%=formsetBean.getBankName()+" / "+formsetBean.getBankAddr()%></td>
				<%
				}else{
				%>
				<td class="Data" width="50%" colspan=3><a href="<%=basePath%>PensionView/Form6RemittanceInfoNew.jsp?frm_region=<%=region%>&frm_month_year=<%=formDate.substring(2,formDate.length())%>&airport=<%=airportCode%>">Please Click to enter remittance values</a></td>
				<%
				}
				%>
			</tr>
			<%region_totalSubscriber=0;region_totalEmoluments=0;region_totalContribution=0;
			region_totalForm4Joinee=region_totalForm4Joinee+totalForm4Joinee;
			}%>
			<tr>
				<td class="Data" width="10%"><%=totalSubscriber%></td>
				<td class="Data" width="15%"><%=df.format(Math.round(totalEmoluments))%></td>
				<td class="Data" width="15%"><%=df.format(Math.round(totalContribution))%></td>
				<td class="Data" width="50%" colspan=3>---</td>
			</tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<td height=30>&nbsp;</td>
	</tr>
	
	<tr>
		<td align=center>
			<table width="80%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="reportdata" width=40%><b>No of Subscribers as per last month's return</B>  </td>
					<td class="data"><u> <%=lastMonthSub%></u>______________</td>
				</tr>
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Add No.of New Subscribers vide Form4(PS)</B></td>
					<td class="data"><u> <%=region_totalForm4Joinee%></u>______________</td>
				</tr>
			
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Less No.of New Subscribers left service vide Form5(PS)</B></td>
					<td class="data"><u> <%=totalForm5Retried%></u>______________</td>
				</tr>
				
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Net Total</B></td>
					<td class="data"><u><%=(lastMonthSub+totalForm4Joinee-totalForm5Retried)%>_____________</u></td>
				</tr>
				<tr>
					<td height=30>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=50%><b>(This should tally with the figures given in column 1)</B></td>
				</tr>
				<tr>
					<td height=30>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td align=center>
		<table width="80%" cellpadding="0" cellspacing="0" align=center>
			<tr>
				<td class="label" width=70%>Date:______________</td>
				<td colspan="4" align="right" class="label">Signature of the
				employer or other <br>
				authorised officer of the Establishment</td>
			</tr>
			<tr>
				<td class="label">Note:(1)If there is any substantial variation betweeb the wages and contribution shwon above and those shown in the last month's return, suitable explanation should be given the Remarks column.
				<br><br>
				(2) If any arrears of contribution or damages are included in the figures under Column 4 suitable details indicating the circumstances, amount, No.of Subscribers and the period involved should be furnished in the 'Remarks' column or on the reverse.
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	 <%}%>

</table>
</form>
</body>
</html>
