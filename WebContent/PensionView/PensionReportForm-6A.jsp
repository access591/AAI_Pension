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

	labelAry = new String[]{"Sl.No","PFID","CPF A/c No","Employee No","Name of the member","Designation","Date Of Birth","Option","Salary drawn/paid for CPF deduction","Employees Subscription","PF Advance Recovery","PF rec.from previous employer","Total","Employer contribution towards","Remarks(if any)","Airport"};

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
<title>PensionReportForm-6A</title>
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
		DecimalFormat df = new DecimalFormat("#########0");
		String reportType="",sortingOrder="", region="",fileName="";
		
		ArrayList regionList = new ArrayList();
		ArrayList airportList = new ArrayList();
		if(request.getAttribute("region")!=null){
		regionList = (ArrayList) request.getAttribute("region");
		region = (String) regionList.get(0);	
		}
		if(request.getAttribute("reportType")!=null){
			reportType=(String)request.getAttribute("reportType");
			if(reportType.equals("Excel Sheet") || reportType.equals("ExcelSheet")){
				fileName="form-6A_"+region+"_report.xls";
			  response.setContentType("application/vnd.ms-excel");
			  response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		    }
		}
		if(request.getAttribute("sortingOrder")!=null){
		   sortingOrder=(String)request.getAttribute("sortingOrder");
		}
		if(request.getAttribute("airportList")!=null){
		int srlno=0;
		
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
		
	%>
	<tr>
		<td colspan="2">
		<table border="0" cellpadding="0" cellspacing="0" width="100%"
			align="center">
			<tr>
				<td  colspan=2 class="reportlabel" align=center>SCHEDULE-6A</td>
			</tr>
			<tr>
				<td  colspan=2 class="reportlabel"  align=left>SCHEDULE OF CPF RECOVERY & PENSION CONTRIBUTION FOR THE MONTH OF  <font color=blue><%=prevDate.substring(2,prevDate.length())%></font></td>
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

	
	
	<%
		String aptName = "";
		ArrayList alist = null;
		int cnt = 0;
		for (int rglist = 0; rglist < regionList.size(); rglist++) {
				region = (String) regionList.get(rglist);		
		for(int ii=0;ii<list.size();ii++){
			airportCode=(String)list.get(ii);
			dataList=financeService.financeForm6AReport(airportCode,formDate,region,sortingOrder);
	%>
	<tr>
		<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="label">Name Of Unit :&nbsp;<font class="reportdata"><%=airportCode%></font></td>
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
			<td class='label' rowspan=2>Sl.No</td>
			<td class='label' rowspan=2>PFID</td>
			<td class='label' rowspan=2>CPF A/c No</td>
			<td class='label' rowspan=2>Employee No</td>
			<td class='label' rowspan=2>Name of the member</td>
			<td class='label' rowspan=2>Designation</td>
			<td class='label' rowspan=2>Date Of Birth</td>
			<td class='label' rowspan=2>Option</td>
			<td class='label' rowspan=2>Salary drawn/paid<br/>for CPF deduction</td>
			
			<td class='label' colspan=2>Employees Subscription</td>
		
			
			<td class='label' colspan=3>PF Advance Recovery</td>
		
			<td class='label' colspan=3>PF rec.from previous employer</td>
			
			<td class='label' rowspan=2>Total</td>
			<td class='label' colspan=3>Employer contribution towards</td>
	
			<td class='label' rowspan=2>Remarks(if any)</td>
			<td class='label' rowspan=2>Airport</td>
			
		
			</tr>
			<tr>
			<td class='label' >(Statutory)</td>
			<td class='label' >(Optional)</td>
			<td class='label' >Pricipal</td>
			<td class='label' >Interest</td>
			<td class='label' >Total</td>
			<td class='label' >Employee subs</td>
			<td class='label' >Employer contr</td>
			<td class='label' >Pension certificate</td>
			<td class='label' >PF(Net)</td>
			<td class='label' >Pension (as per option)</td>
			<td class='label' >Total</td>
			</tr>
		
			</tr>

			<% 
				
					
					alist = new ArrayList();
					long totalEmoluments=0;
					double drawnSal = 0.0;
					double empTotSubStatutory = 0.0,empTotalSubOptional = 0.0;
					double totalPrinciple = 0.0,totalInterest = 0.0,pfAdvRecTotal=0.0,totalEmpSub=0.0;
					double totalempContrPF = 0.0,totalEmpContrPension = 0.0,totalEmpContrTotal = 0.0;
					
				for(int k=0;k<dataList.size();k++){
					formsetBean=(form3Bean)dataList.get(k);
					
				
					srlno++;
					
					if(!formsetBean.getCpfaccno().equals("--"))
						alist.add(formsetBean.getCpfaccno());
						
					
			%>
			<tr>
				<%
				totalEmoluments=Math.round(totalEmoluments+Double.parseDouble(formsetBean.getEmoluments()));
				empTotSubStatutory=Math.round(empTotSubStatutory+formsetBean.getEmpSubStatutory());
				empTotalSubOptional=Math.round(empTotalSubOptional+formsetBean.getEmpSubOptional());
				totalPrinciple=Math.round(totalPrinciple+formsetBean.getPrinciple());
				totalInterest=Math.round(totalInterest+formsetBean.getInterest());
				pfAdvRecTotal=Math.round(pfAdvRecTotal+formsetBean.getPfAdvRecTotal());
				totalEmpSub=Math.round(totalEmpSub+formsetBean.getEmpSubStatutory()+formsetBean.getEmpSubOptional()+formsetBean.getPfAdvRecTotal());
				totalempContrPF=Math.round(totalempContrPF+formsetBean.getEmpContrPF());
				totalEmpContrPension=Math.round(totalEmpContrPension+formsetBean.getEmpContrPension());
				totalEmpContrTotal=totalEmpContrTotal+formsetBean.getEmpContrTotal();
				%>
				
				<td class="Data" width="2%"><%=srlno%></td>
				<td class="Data" width="12%"><%=formsetBean.getPfID()%></td>
				<td class="Data" width="12%"><%=formsetBean.getCpfaccno()%></td>
				<td class="Data" width="12%"><%=formsetBean.getEmployeeNo()%></td>
				<td class="Data" width="20%"><%=formsetBean.getEmployeeName()%></td>
				<td class="Data" width="12%"><%=formsetBean.getDesignation()%></td>
				<td class="Data" width="20%" nowrap="nowrap"><%=formsetBean.getDateOfBirth()%></td>
				<td class="Data" width="5%"><%=formsetBean.getWetherOption()%></td>
				
				<td class="Data" width="10%"><%=df.format(Math.round(Double.parseDouble(formsetBean.getEmoluments())))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpSubStatutory()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpSubOptional()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getPrinciple()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getInterest()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getPfAdvRecTotal()))%></td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpSubStatutory()+formsetBean.getEmpSubOptional()+formsetBean.getPfAdvRecTotal()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpContrPF()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpContrPension()))%></td>
				<td class="Data" width="10%"><%=df.format(Math.round(formsetBean.getEmpContrTotal()))%></td>
				<td class="Data" width="15%">---</td>
				<td class="Data" width="15%"><%=formsetBean.getAirportCode()%></td>
				
			</tr>
			<%}%>
			<tr>
	
				
				<td class="Data" width="12%" colspan="2">Grand Total</td>
			
				<td class="Data" width="12%">---</td>
				<td class="Data" width="12%">---</td>
				<td class="Data" width="20%">---</td>
				<td class="Data" width="12%">---</td>
				<td class="Data" width="20%" nowrap="nowrap">---</td>
				<td class="Data" width="5%">---</td>
				
				<td class="Data" width="10%"><%=totalEmoluments%></td>
				<td class="Data" width="10%"><%=df.format(empTotSubStatutory)%></td>
				<td class="Data" width="10%"><%=df.format(empTotalSubOptional)%></td>
				<td class="Data" width="10%"><%=df.format(totalPrinciple)%></td>
				<td class="Data" width="10%"><%=df.format(totalInterest)%></td>
				<td class="Data" width="10%"><%=df.format(pfAdvRecTotal)%></td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%">--</td>
				<td class="Data" width="10%"><%=df.format(totalEmpSub)%></td>
				<td class="Data" width="10%"><%=df.format(totalempContrPF)%></td>
				<td class="Data" width="10%"><%=df.format(totalEmpContrPension)%></td>
				<td class="Data" width="10%"><%=df.format(totalEmpContrTotal)%></td>
				<td class="Data" width="15%">---</td>
				<td class="Data" width="15%">---</td>
				
			</tr>
			
		</table>

		</td>
	</tr>
	<tr>
		<td height="40">&nbsp;</td>
	</tr>
	<%}%>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="label"><%=note%></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="4" align="right" class="label">Signature of the
				employer or other <br>
				authorised officer of the Establishment</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	 <%}}%>

</table>
</form>
</body>
</html>
