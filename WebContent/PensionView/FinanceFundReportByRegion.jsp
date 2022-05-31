
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";%>

<%@ page language="java" import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>


<%@ page import="aims.bean.EmployeeValidateInfo"%>

<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
</HEAD>
<body>
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
					<td align="center" class="reportlabel">
						All Employees Finance Report
						</td>
					</tr>
					<tr>
						<td class="reportsublabel" align="center" >Based on</td>
						<td class="reportdata" align="left" nowrap><%=request.getParameter("frm_region")%>
						</td>
					</tr>
				</table>
					
					</td>
					</tr>
					
					
				
		<%
	
		
			ArrayList dataList=new ArrayList();
			ArrayList financePersonlList=new ArrayList();
			String frm_region="";
			EmployeeValidateInfo validateInfo=new EmployeeValidateInfo();
			EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
			if(request.getParameter("frm_region")!=null){
			frm_region=request.getParameter("frm_region");
			}
			
			FinancialService financeService=new FinancialService();
			financePersonlList=financeService.getFinancalInfo(frm_region);
			String employeename="",employeeNo="",dbCPFaccno="",airportCD="",pensionNumber="",region="",designation="";
 			System.out.println("financePersonlList.size()==================="+financePersonlList.size());
			
			for(int j=0;j<financePersonlList.size();j++){
			 personalInfo=(EmployeePersonalInfo)financePersonlList.get(j);
			 employeename=personalInfo.getEmployeeName();
			 employeeNo=personalInfo.getEmployeeNumber();
			 System.out.println("employeeNo==================="+employeeNo);
			dbCPFaccno=personalInfo.getCpfAccno();
			designation=personalInfo.getDesignation();
			 region=personalInfo.getRegion();
			dataList=financeService.financeReportByEachEmp(dbCPFaccno,employeeNo);%>
			
			
			<table width="100%" border="1" align="center" cellpadding="0" cellspacing="0">
			
				<tr>
					<td>
						<table width="100%" border="1" cellpadding="1" cellspacing="1">
							<tr>
								<td class="label">Pension Number</td>
								<td class="reportdata"><%=pensionNumber%></td>
								<td class="label">Employee Number</td>
								<td class="reportdata"><%=employeeNo%></td>
								<td class="label">CPF ACC No</td>
								<td class="reportdata"><%=dbCPFaccno%></td>
							</tr>
							<tr>
								<td class="label">Employee Name</td>
								<td class="reportdata"><%=employeename%></td>
								<td class="label">Designation</td>
								<td class="reportdata"><%=designation%></td>
								<td class="label">Region/Airport</td>
								<td class="reportdata"><%=airportCD%></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="1" cellpadding="1" cellspacing="1"> 
							<tr>
								<td class="label">Finance Date</td>
								<td class="label">Basic</td>
								<td class="label">Daily Allowance</td>
								<td class="label">Special Basic</td>
								<td class="label">Emoluments</td>
								<td class="label">CPF</td>
								<td class="label">AdCPF</td>
								<td class="label">Principal</td>
								<td class="label">Interest</td>
								<td class="label">Emp Total</td>
								<td class="label">PF</td>
								<td class="label">Pension</td>
								<td class="label">AAI Total</td>
							</tr>
							<%	if (dataList.size() != 0) {
									int count = 0;
									String  basic = "", dailyAllowance = "", monthYear = "",specialBasic="",aaiTotal="";
									String principal="",interset="",aaiPF="",aaiPension="";
									boolean bckColor=false;
									String emoulments="",pfStatury="",vpf="",empTotal="";
									for (int i = 0; i < dataList.size(); i++) {
										count++;
										EmployeeValidateInfo beans = (EmployeeValidateInfo) dataList.get(i);
										
										
										monthYear=beans.getEffectiveDate();
										basic= beans.getBasic();
									
										dailyAllowance = beans.getDailyAllowance();
										specialBasic=beans.getSpecialBasic();
										emoulments=beans.getEmoluments();
										pfStatury=beans.getEmpPFStatuary();
										vpf=beans.getEmpVPF();
										principal=beans.getEmpAdvRecPrincipal();
										interset=beans.getEmpAdvRecInterest();
										empTotal=beans.getEmptotal();
										aaiPF=beans.getAaiconPF();
										aaiPension=beans.getAaiconPension();
										aaiTotal=beans.getAaiTotal();%>
							<tr>
							<td class="reportdata"><%=monthYear%></td>
							<td class="reportdata"><%=basic%></td>
							<td class="reportdata"><%=dailyAllowance%></td>
							<td class="reportdata"><%=specialBasic%></td>
							<td class="reportdata"><%=emoulments%></td>
							<td class="reportdata"><%=pfStatury%></td>
							<td class="reportdata"><%=vpf%></td>
							<td class="reportdata"><%=principal%></td>
							<td class="reportdata"><%=interset%></td>
							<td class="reportdata"><%=empTotal%></td>
							<td class="reportdata"><%=aaiPF%></td>
							<td class="reportdata"><%=aaiPension%></td>
							<td class="reportdata"><%=aaiTotal%></td>
							</tr>
							<%}}%>
						</table>
					</td>
				</tr>
			</table>
	<%}%>
	</table>
</body>
</html>