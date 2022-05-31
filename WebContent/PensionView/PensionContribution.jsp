
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%@ page import="aims.common.*"%>
<%@ page import="java.text.DecimalFormat"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
     
  </head>
  
  <body class="">

  
  <form action="method">
			<table width="100%" border="0" 	 align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" colspan="5" >
						<table border=0 cellpadding=3 cellspacing=0 width="100%" align="center" valign="middle">
							<tr>
								<td width="40%">
									<img src="<%=basePath%>PensionView/images/logoani.gif" width="65" height="70" align="right">
								</td>
								<td class="label" align="left" valign="top" nowrap="nowrap">
									<font color='black' size='4' face='Helvetica'> AIRPORTS AUTHORITY OF INDIA </font>
								</td>

							</tr>
							<tr>
								<td >&nbsp;&nbsp;</td>
								<td class="reportsublabel">STATEMENT OF PENSION CONTRIBUTION</td>
							</tr>
						</table>
					</td>
				</tr>
				  <%
  	 ArrayList PensionContributionList=new ArrayList();
  	 ArrayList pensionList=new ArrayList();
	
  	 String employeeNm="",pensionNo="",doj="",dob="",cpfacno="",employeeNO="",designation="",fhName="",gender="",fileName="";
  	 String reportType ="",whetherOption="",dateOfEntitle="",empSerialNo="";
	  
 	 PensionContributionList=(ArrayList)request.getAttribute("penContrList");
 	 for(int i=0;i<PensionContributionList.size();i++){
			PensionContBean contr=(PensionContBean)PensionContributionList.get(i);
			employeeNm=contr.getEmployeeNM();
			pensionNo=contr.getPensionNo();
			empSerialNo=contr.getEmpSerialNo();
			doj=contr.getEmpDOJ();
			dob=contr.getEmpDOB();
			String discipline=contr.getDepartment();
			cpfacno=StringUtility.replaces(contr.getCpfacno().toCharArray(),",=".toCharArray(),",").toString();
			if(cpfacno.indexOf(",=")!=-1){
						cpfacno=cpfacno.substring(1,cpfacno.indexOf(",="));
			}
			whetherOption=contr.getWhetherOption();
			employeeNO=contr.getEmployeeNO();
			designation=contr.getDesignation();
			fhName=contr.getFhName();
			gender=contr.getGender();
			dateOfEntitle=contr.getDateOfEntitle();
			pensionList=contr.getEmpPensionList();
	
  	
  %>
				<tr >
					<td>
						<table border="1" style="border-color:gray"    cellpadding="2" cellspacing="0" width="100%" align="center" 	>
							<tr >
								<td class="reportsublabel">PENSION NO</td>
								<td class="reportdata"><%=pensionNo%></td>
								<td class="reportsublabel">EMP.SERIAL NO</td>
								<td class="reportdata"><%=empSerialNo%></td>						
							</tr>
							<tr>
								<td class="reportsublabel">EMP NO</td>
								<td class="reportdata"><%=employeeNO%></td>
								<td class="reportsublabel">NAME</td>
									<td class="reportdata"><%=employeeNm%></td>
														
								
							</tr>
							<tr>
								<td class="reportsublabel">CPF NO</td>
								<td class="reportdata"><%=cpfacno%></td>
								<td class="reportsublabel">DESIGNATION</td>
								<td class="reportdata"><%=designation%></td>
								
								
							</tr>
							<tr>
								<td class="reportsublabel">DOB</td>
								<td class="reportdata"><%=dob%></td>
								<td class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
								<td class="reportdata"><%=fhName%></td>
								
								
							</tr>
							<tr>
								<td class="reportsublabel">DOJ</td>
								<td class="reportdata"><%=doj%></td>
								<td class="reportsublabel">GENDER</td>
								<td class="reportdata"><%=gender%></td>
									
							</tr>
							<tr>
								<td class="reportsublabel">Option</td>
								<td class="reportdata"><%=whetherOption%></td>
								<td class="reportsublabel">DOM</td>
								<td class="reportdata"><%=dateOfEntitle%></td>
									
							</tr>
						</table>
					</td>
				</tr>
				
				<%if (pensionList.size()!=0){%>
				<tr>
					<td  colspan="5" >
					
						<table border="1" style="border-color:gray;"    cellpadding="2" cellspacing="0" width="100%" align="center" >
							
							<tr>
								<td class="label" width="10%">Month</td>
								<td class="label" width="10%">Emolument</td>
								<td class="label" width="10%">CPF</td>
								<td class="label" width="10%">Pension Contribution</br>(1.16%X2)&8.33%</td>
								<td class="label" width="10%">Station</td>
								<td class="label" width="10%">Remarks</td>
							</tr>
							<%
							double totalEmoluments=0.0,pfStaturary=0.0,totalPension=0.0;
							int count=0;
							DecimalFormat df = new DecimalFormat("#########0.00");
							for(int j=0;j<pensionList.size();j++){
								TempPensionTransBean bean=(TempPensionTransBean)pensionList.get(j);
								
								if(bean!=null){
								count++;
								totalEmoluments= new Double(df.format(totalEmoluments+Double.parseDouble(bean.getEmoluments()))).doubleValue();
								pfStaturary= new Double(df.format(pfStaturary+Double.parseDouble(bean.getCpf()))).doubleValue();
								totalPension=new Double(df.format(totalPension+Double.parseDouble(bean.getPensionContr()))).doubleValue();
							%>
							
								<tr>
								<td class="Data"><%=bean.getMonthyear()%></td>
								<td class="Data"><%=bean.getEmoluments()%></td>
								<td class="Data"><%=bean.getCpf()%></td>
								<td class="Data"><%=bean.getPensionContr()%></td>
								<td class="Data"><%=bean.getStation()%></td>
								<td class="Data"><%=bean.getRemarks()%></td>
							</tr>
							<%}}%>
							
							
							<tr>
								<td class="reportsublabel">Total <%=count%></td>
								<td class="reportsublabel"><%=totalEmoluments%></td>
								<td class="reportsublabel"><%=pfStaturary%></td>
								<td class="reportsublabel"><%=totalPension%></td>
								<td class="reportsublabel"><%="---"%></td>
								<td class="reportsublabel"><%="---"%></td>
							</tr>
						</table>
					</td>
				</tr>
				<%}%>
				<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
				
				<%}%>
			</table>
			
  </body>
</html>
