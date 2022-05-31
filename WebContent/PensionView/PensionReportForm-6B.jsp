<%
 	String path = request.getContextPath(); 
 	String basePath =
	request.getScheme() + "://" + request.getServerName() + ":" +
	request.getServerPort() + path + "/";
	
	String labelAry[] = null;
	String subLabelAry[] = null;

	String note = "";
	String signatory = "Signature of the employer or other <br> authorised officer of the Establishment";
	
	note = "Note: Schedule may be reconciled with the abstract";

	labelAry = new String[]{"Sl.No","Airport/Unit","No of Subscribers","Salary drawn/paid for CPF deduction","Employees Subscription","PF Advance Recovery","PF rec.from previous employer","Total","Employer contribution towards","Remarks(if any)"};

	subLabelAry = new String[]{"(Statutory)","(Optional)","Pricipal","Interest","Total","Employee subs","Employer contr","Pension certificate","PF(Net)","Pension (as per option)","Total"};
	
%> 

<%@ page language="java"
import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,java.text.DecimalFormat"
contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%> 

<%@ page import="aims.bean.EmployeeValidateInfo"%> 
<%@ page import="aims.bean.EmployeePersonalInfo"%> 
<%@ page import="aims.service.FinancialService"%> 
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<html>
<HEAD>
<title>PensionReportForm-6B</title>
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
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	
	<%
		DecimalFormat df = new DecimalFormat("#########0.00");
		String reportType="",sortingOrder="cpfaccno";
		if(request.getAttribute("airportList")!=null){
		int srlno=0;
		String region="";
		ArrayList regionList = new ArrayList();
		ArrayList airportList = new ArrayList();
		if(request.getAttribute("region")!=null){
		regionList = (ArrayList) request.getAttribute("region");
		region = (String) regionList.get(0);	
		}
		 if (request.getParameter("frm_reportType") != null) {
				reportType = (String) request.getParameter("frm_reportType");
				if (reportType.equals("Excel Sheet")|| reportType.equals("ExcelSheet")) {
					String fileName = "form-6B-report.xls";

					response.setContentType("application/vnd.ms-excel");

					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);

				}

			}
		FinancialService financeService=new FinancialService();
		ArrayList list=new ArrayList();
		ArrayList dataList=new ArrayList();
		form3Bean formsetBean=null;
		String formDate="",prevDate="";
		formDate=(String)request.getAttribute("selectedDate");
		prevDate=(String)request.getAttribute("prevDate");
		list=(ArrayList)request.getAttribute("airportList");
		String airportCode="";
		System.out.println("list.size() is ::::::::::::::::: "+list.size());
		System.out.println("prevDate is ::::::::::::::::: "+prevDate);
		int[] dataAry = financeService.financeForm6BCountData(airportCode,formDate,region,prevDate);
		
		System.out.println("dataAry is ::::::::::::::::: "+dataAry[0]+"============"+dataAry[1]);
	%>
	<tr>
		<td colspan="2">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"
			align="center">
			<tr>
				<td  colspan=2 class="reportlabel" align=center>SCHEDULE-6B</td>
			</tr>
			<tr>
				<td  colspan=2 class="reportlabel"  align=left>AIRPORTWISE CPF RECOVERY & PENSION CONTRIBUTION FOR THE MONTH OF <font color=blue><%=prevDate.substring(2,prevDate.length())%></font></td>
			</tr>
			<tr>
				<td height=10>&nbsp;</td>
			</tr>
			<tr>
				<td colspan=2 width=75%>&nbsp;</td> 
				<td class="label" align=left></td>
			</tr>
			<tr>
				<td >&nbsp;</td> 
				<td class="label" align=left>Total No.of suscribers________________</td>
			</tr>
			<tr>
				<td>&nbsp;</td> 
				<td class="label" align=left>Statutory rate PF Contribution 12.00%</td>
			</tr>
			<tr>
				<td>&nbsp;</td> 
				<td class="label" align=left>Statutory rate Pension Contribution 8.33%</td>
			</tr>
			<tr>
				<td>&nbsp;</td> 
				<td  class="label" align=left>Region:<font color=blue><%=region%></font></td>
			</tr>
			<tr>
				<td colspan=2 height=20>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="label"></td>
				<td class="label" align="right">Date of Coverage:<font class="reportdata">01-Apr-1995</font></td>
			</tr>
		</table>
		</td>
	</tr>
	
	<tr>
		<td>
		<table width="100%" border="1" bordercolor="gray" cellpadding="2"
			cellspacing="0">
			<tr>
			<%
				for(int i=0;i<labelAry.length;i++){
					if(i==4)
						out.println("<th class='label' colspan=2>"+labelAry[i]+"</th>");
					else if (i==5 || i==6 || i==8)
						out.println("<th class='label' colspan=3>"+labelAry[i]+"</th>");
					else
						out.println("<th class='label' rowspan=2>"+labelAry[i]+"</th>");
				}
			%>
			</tr>
			<tr>
			<%
				for(int i=0;i<subLabelAry.length;i++){
					out.println("<th class='label'>"+subLabelAry[i]+"</th>");
				}
			%>
			</tr>
			<tr>
			<%
				for(int i=0;i<17;i++){
					out.println("<th class='label'>"+(i+1)+"</th>");
				}
			%>
			</tr>

			<% 
			int totalCnt = 0;
			ArrayList alist = null;
			for (int rglist = 0; rglist < regionList.size(); rglist++) {
				region = (String) regionList.get(rglist);	
			for(int ii=0;ii<list.size();ii++){
				airportCode=(String)list.get(ii);
				dataList=financeService.financeForm6BReport(airportCode,formDate,region,sortingOrder);
				//String aptName = "";
				String remarks = "";
				
				alist = new ArrayList();
				
				double drawnSal = 0.0;
				double empSubStatutory = 0.0,empSubOptional = 0.0;
				double principle = 0.0,interest = 0.0,pfAdvRecTotal=0.0;
				double empContrPF = 0.0,empContrPension = 0.0,empContrTotal = 0.0;
				int cnt = 0;
				srlno++;
				for(int k=0;k<dataList.size();k++){
					formsetBean=(form3Bean)dataList.get(k);
					
					if(!alist.contains(formsetBean.getCpfaccno())){
						cnt++;
						if(!formsetBean.getCpfaccno().equals("--"))
							alist.add(formsetBean.getCpfaccno());
						drawnSal = drawnSal + Double.parseDouble(formsetBean.getEmoluments());
						empSubStatutory = empSubStatutory + formsetBean.getEmpSubStatutory();
						empSubOptional = empSubOptional + formsetBean.getEmpSubOptional();
						principle = principle + formsetBean.getPrinciple();
						interest = interest+ formsetBean.getInterest();
						pfAdvRecTotal = pfAdvRecTotal + formsetBean.getPfAdvRecTotal();
						empContrPF = empContrPF + formsetBean.getEmpContrPF();
						empContrPension = empContrPension + formsetBean.getEmpContrPension();
						empContrTotal = empContrTotal + formsetBean.getEmpContrTotal();
						remarks = formsetBean.getRemarks();
			
					}
				}
				totalCnt = totalCnt + cnt;
			%>
			<tr>
				
				<td class="Data" width="2%"><%=srlno%></td>
				<td class="Data" width="12%"><%=airportCode%></td>
				<td class="Data" width="12%"><%=cnt%></td>
				<td class="Data" width="10%"><%=df.format(drawnSal)%></td>
				<td class="Data" width="10%"><%=df.format(empSubStatutory)%></td>
				<td class="Data" width="10%"><%=df.format(empSubOptional)%></td>
				<td class="Data" width="19%"><%=df.format(principle)%></td>
				<td class="Data" width="15%"><%=df.format(interest)%></td>
				<td class="Data" width="15%"><%=df.format(pfAdvRecTotal)%></td>
				<td class="Data" width="15%">--</td>
				<td class="Data" width="15%">--</td>
				<td class="Data" width="15%">--</td>
				<td class="Data" width="15%"><%=df.format(empSubStatutory+empSubOptional+pfAdvRecTotal)%></td>
				<td class="Data" width="15%"><%=df.format(empContrPF)%></td>
				<td class="Data" width="15%"><%=df.format(empContrPension)%></td>
				<td class="Data" width="15%"><%=df.format(empContrTotal)%></td>
				<td class="Data" width="15%"><%=remarks%></td>
				
			</tr>
			<%}}%>
		
		</table>

		</td>
	</tr>
	<tr>
		<td height=50>&nbsp;Total:--- <%=totalCnt%></td>
	</tr>
	
	<tr>
		<td>
			<table width="70%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="reportdata" width=40%><b>No of Subscribers as per last month's return</B>  </td>
					<td class="data"><u> <%=dataAry[2]%></u>______________</td>
				</tr>
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Add: (i) No.of New Subscribers vide Form4(PS)</B></td>
					<td class="data"><u> <%=dataAry[0]%></u>______________</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(ii) No.of transfer in cases vide Sch-5A(PS)</B> </td>
					<td class="data">_________________________</td>
				</tr>
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Less: (i) No.of New Subscribers left service vide Form5(PS)</B></td>
					<td class="data"><u> <%=dataAry[1]%></u>______________</td>
				</tr>
				<tr>
					<td class="label" width=40%><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(ii) No.of transfer out cases vide Sch-5B(PS) </B></td>
					<td class="data">_________________________</td>
				</tr>
				<tr>
					<td height=10>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=40%><b>Net Total</B></td>
					<td class="data"><u><%=(dataAry[2]+dataAry[0]-dataAry[1])%>_____________</u></td>
				</tr>
				<tr>
					<td height=30>&nbsp;</td>
				</tr>
				<tr>
					<td class="reportdata" width=50%><b>(This should tally with the figures given in column 3)</B></td>
				</tr>
				<tr>
					<td height=30>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td>
		<table width="100%" cellpadding="0" cellspacing="0">
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
