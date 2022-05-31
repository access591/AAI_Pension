
<%@ page
	import="java.util.*,aims.common.CommonUtil,aims.service.FinancialService,aims.bean.EmployeeValidateInfo"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="java.util.ArrayList" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String[] year = {"2010", "2011", "2012" };
	ArrayList remitanceList=new ArrayList();
	String region1="",airportcode="",year1="",month="",region="",salaryMonth="",remitanceType="";
	ArrayList remitancetableList=new ArrayList();
	ArrayList grandtotalsLIst=new ArrayList();
	RemittanceBean rbean=new RemittanceBean();
	AaiEpfform3Bean epf3bean=new AaiEpfform3Bean();
	ArrayList noofEmployeeList=new ArrayList();
	double epf8Totals=0.00;
	 DecimalFormat df = new DecimalFormat("#########0");	
	if (request.getAttribute("region") != null) {
		region1 = (String) request.getAttribute("region");
	}
	if (request.getAttribute("airportcode") != null) {
		airportcode = (String) request.getAttribute("airportcode");
	}
	if (request.getAttribute("year") != null) {
		year1 = (String) request.getAttribute("year");
	}
	if (request.getAttribute("month") != null) {
		month = (String) request.getAttribute("month");
	}
	if (request.getAttribute("salaryMonth") != null) {
		salaryMonth = (String) request.getAttribute("salaryMonth");
	}
	if (request.getAttribute("remitanceType") != null) {
		remitanceType = (String) request.getAttribute("remitanceType");
	}
	if (request.getAttribute("remitancetableList") != null) {
		 remitancetableList = (ArrayList) request.getAttribute("remitancetableList");
		 if(remitancetableList.size()>0){
			 for(int i=0;i<remitancetableList.size();i++){
			 rbean=(RemittanceBean)remitancetableList.get(i);
			
		 }
	}}
	
	
	if (request.getAttribute("epf8Totals") != null) {
		 epf8Totals = Double.parseDouble(request.getAttribute("epf8Totals").toString());
	}
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>AAI</title>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
<script type="text/javascript">

 </script>
</head>

<%
	String monthID = "", yearDescr = "", monthNM = "", monthNM1 = "", monthID1 = "";
	ArrayList yearList = new ArrayList();
	Iterator regionIterator = null;
	Iterator monthIterator = null;
	HashMap hashmap = new HashMap();
	String reportType="",fileName="",stationName="",filePrefix="",filePostfix="";
	if (request.getAttribute("regionHashmap") != null) {
		hashmap = (HashMap) request.getAttribute("regionHashmap");
		Set keys = hashmap.keySet();
		regionIterator = keys.iterator();
	}
	if (request.getAttribute("monthIterator") != null) {
		monthIterator = (Iterator) request
				.getAttribute("monthIterator");
	}
	 if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "RemitenceReport";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
	
%>
<form name="validation" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
        <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
      <tr>
        <td width="38%">&nbsp;</td>
     </tr>
     <tr>
        <td colspan="3" align="center" class="reportlabel">
RAUs AND SAUs wise REMITTANCE DETAILS OF PF ACCRETION, INSPECTION CHARGES AND PENSION CONTRIBUTION FOR THE MONTH OF __<%=salaryMonth%>
<font style="text-decoration: underline"></font></td>
      </tr>
<tr>
        <td colspan="3" align="left"><b><%if(remitanceType.equals("aaiepf3")){%>
MONTHLY SALARY REPORT
<%}else{ %> 
SUPPLEMENTARY SALARY REPORT
   <%} %>
<font style="text-decoration: underline"></font></b></td>
      </tr>
    </table></td>
  </tr>
<tr>
    <td>&nbsp;</td>
  </tr>
<table>
<table width="100%" border="0" align="center" cellpadding="0"	cellspacing="0">
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
	</tr>
<table align="center" cellpadding=2 class="tbborder" cellspacing="0" border="">

</table>

<tr><td>&nbsp;</td></tr>
	<tr>
		<td>

		<table align="center" cellpadding=3 class="tbborder" cellspacing="0"
			border="">
			<tr class="HighlightData">
				<td rowspan="2">Region&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2">UnitCode&nbsp;</td>
				<td rowspan="2">Station&nbsp;&nbsp;&nbsp;</td>
				
				<td colspan="7" align="center">AS PER REMITTANCES MADE BY REGIONS</td>
				
				<td colspan="3" align="center"><%if(remitanceType.equals("aaiepf3")){%>
AS PER AAIEPF-3 EPIS
<%}else{ %> 
 AS PER AAIEPF-3 SUPPL.. EPIS
 <%} %>
</td>
				<td colspan="3" align="center">DIFFERENCE (REMITTANCE & AAIEPF-3)</td>
			</tr>
			<tr class="HighlightData">
                <td >No. Of &nbsp;&nbsp;<br>Employees For  PF & IC
				</td>
				<td>PF Accretion&nbsp;&nbsp;</td>
				<td>Emoluments&nbsp;&nbsp;</td>
				<td>Inspection Charges(IC)&nbsp;&nbsp;</td>
				<td>No.Of Employees FOR PENSION&nbsp;&nbsp;</td>
				<td>Emoluments&nbsp;&nbsp;</td>
				<td>Pension Contribution&nbsp;&nbsp;</td>
                <td>PF Accretion&nbsp;&nbsp;</td>
				<td>Inspection Charges(IC)&nbsp;&nbsp;</td>
				<td>Pension Contribution&nbsp;&nbsp;</td>
				<td>PF Accretion&nbsp;&nbsp;</td>
				<td>Inspection Charges(IC)&nbsp;&nbsp;</td>
				<td>Pension Contribution&nbsp;&nbsp;</td>
				</tr> 
            <tr>
  <%if(remitancetableList.size()>0){ 
	 String tempregion="";
	 double totalEmoluments=0;
	 int totalPFnoofEmp=0,totalPCnoofEmp=0;;
	 double totalPfAccretion=0,totalPfEmoluments=00,totalInspectionCharges=0,pcEmoluments=0,pensionContribution=0;
	 double totalEPf3Accretion=0,totalEPF3PensionContri=0,totalEpf3Emoluments=0,totalDevitionofAccretion=0;
	 double deviationAccertion=0,deviationInspectionChanges=0,deviationPensionContri=0,totalDeviationInspectionChanges=0;;
			 for(int i=0;i<remitancetableList.size();i++){
				 if(i==0){
				rbean=(RemittanceBean)remitancetableList.get(0);	 
				tempregion=rbean.getRegion();
				}
				 rbean=(RemittanceBean)remitancetableList.get(i);
				 %>
<%
   if(!tempregion.trim().equals(rbean.getRegion().trim()) ){
	System.out.println("temp "+tempregion+ " present "+rbean.getRegion()); 
%>
<tr><td>&nbsp;</td><td>&nbsp;</td><b><td class="HighlightData" align="right">Total &nbsp;</td><td align="right"><%=totalPFnoofEmp%></td><td align="right"><%=df.format(totalPfAccretion) %></td><td align="right"><%=df.format(totalPfEmoluments) %></td><td align="right"><%=df.format(totalInspectionCharges) %></td><td align="right"><%=df.format(totalPCnoofEmp) %></td><td align="right"><%=df.format(pcEmoluments) %></td><td align="right"><%=df.format(pensionContribution) %></td><td align="right"><%=df.format(Math.round(totalEPf3Accretion)) %></td><td align="right"><%=df.format(Math.round(totalEPf3Accretion* .0018)) %></td><td align="right"><%=df.format(totalEPF3PensionContri) %></td><td align="right"><%=df.format(totalPfAccretion-totalEPf3Accretion) %></td><td align="right"><%=df.format(Math.round(totalDeviationInspectionChanges)) %></td><td align="right"><%=df.format(deviationPensionContri) %></td><b></tr>
  <% totalPFnoofEmp=0;
   totalPfAccretion=0;
   totalPfEmoluments=0;
   totalInspectionCharges=0;
   totalPCnoofEmp=0;
   pcEmoluments=0;
   pensionContribution=0;
   totalEPf3Accretion=0;
   totalEPF3PensionContri=0;
   totalEpf3Emoluments=0;
   deviationAccertion=0;
   deviationInspectionChanges=0;
   deviationPensionContri=0;
   
   totalPFnoofEmp=totalPFnoofEmp+rbean.getPfnoofEmployees();
   totalPfAccretion=totalPfAccretion+rbean.getPfAccretion();
   totalPfEmoluments=totalPfEmoluments+rbean.getEmoluments();
   totalInspectionCharges=totalInspectionCharges+rbean.getInspectionCharges();
   System.out.println(totalInspectionCharges);
   totalPCnoofEmp=totalPCnoofEmp+rbean.getPcNoofEmployees();
   pcEmoluments=pcEmoluments+rbean.getPcEmoluments();
   pensionContribution=pensionContribution+rbean.getPensionContribution();
   totalEPf3Accretion=totalEPf3Accretion+rbean.getEpf3Accretion();
   totalEPF3PensionContri=totalEPF3PensionContri+rbean.getEpf3Pensioncontri();
   totalEpf3Emoluments=totalEpf3Emoluments+rbean.getEpf3Emoluments();
   deviationAccertion=deviationAccertion+(totalPfAccretion-totalEPf3Accretion);
  
   deviationPensionContri=pensionContribution-totalEPF3PensionContri;
   totalDeviationInspectionChanges=totalDeviationInspectionChanges+(Math.round(rbean.getEmoluments()-rbean.getEpf3Emoluments())*0.0018);
   System.out.println("totalDeviationInspectionChanges "+totalDeviationInspectionChanges);
   %>
    

 <tr> <td > <%=rbean.getRegion()%>&nbsp;&nbsp;&nbsp;&nbsp;</td><td  width="10%" align="right"><%=rbean.getNewUnitCode()%></td><td  width="10%" align="left"><%=rbean.getAirportcode()%></td><td align="right"> <%=rbean.getPfnoofEmployees()%></td><td align="right"><%=df.format(rbean.getPfAccretion())%></td><td align="right"><%=df.format(rbean.getEmoluments())%></td><td align="right"><%=df.format(rbean.getInspectionCharges())%></td><td align="right"><%=rbean.getPcNoofEmployees()%></td><td align="right"><%=df.format(rbean.getEmoluments())%></td><td align="right"><%=rbean.getPensionContribution()%></td><td align="right"><%=df.format(rbean.getEpf3Accretion())%></td><td align="right"><%=df.format(Math.round(rbean.getEpf3Emoluments()* 0.0018))%></td><td align="right"><%=df.format(rbean.getEpf3Pensioncontri())%></td><td align="right"><%=df.format(Math.round(rbean.getPfAccretion()-rbean.getEpf3Accretion()))%></td><td align="right"><%=Math.round(totalDeviationInspectionChanges)%></td><td align="right"><%=df.format(rbean.getPensionContribution()-rbean.getEpf3Pensioncontri())%></td></tr>
 <%	}else{
	 
	 totalPFnoofEmp=totalPFnoofEmp+rbean.getPfnoofEmployees();
	 totalPfAccretion=totalPfAccretion+rbean.getPfAccretion();
	 totalPfEmoluments=totalPfEmoluments+rbean.getEmoluments();
	 totalInspectionCharges=totalInspectionCharges+rbean.getInspectionCharges();
	 totalPCnoofEmp=totalPCnoofEmp+rbean.getPcNoofEmployees();
	 pcEmoluments=pcEmoluments+rbean.getPcEmoluments();
	 pensionContribution=pensionContribution+rbean.getPensionContribution();
	 totalEPf3Accretion=totalEPf3Accretion+rbean.getEpf3Accretion();
	 totalEPF3PensionContri=totalEPF3PensionContri+rbean.getEpf3Pensioncontri();
	 totalEpf3Emoluments=totalEpf3Emoluments+rbean.getEpf3Emoluments();
	
	 deviationPensionContri=pensionContribution-totalEPF3PensionContri;
	 totalDeviationInspectionChanges=totalDeviationInspectionChanges+(Math.round(rbean.getEmoluments()-rbean.getEpf3Emoluments())*0.0018);
	 System.out.println("totalDeviationInspectionChanges "+totalDeviationInspectionChanges);
	 System.out.println("remitbean "+rbean.getEmoluments() +" epf3" +rbean.getEpf3Emoluments());
	 %>
<tr> <td width="12%"> <%=rbean.getRegion()%></td><td  width="10%" align="right"><%=rbean.getNewUnitCode()%></td><td  width="10%" align="left"><%=rbean.getAirportcode()%></td><td align="right"><%=rbean.getPfnoofEmployees()%></td><td align="right"><%=df.format(rbean.getPfAccretion())%></td><td align="right"><%=df.format(rbean.getEmoluments())%></td><td align="right"><%=df.format(rbean.getInspectionCharges())%></td><td align="right"><%=rbean.getPcNoofEmployees()%><td align="right"><%=df.format(rbean.getPcEmoluments())%></td><td align="right"><%=df.format(rbean.getPensionContribution())%></td><td align="right"><%=df.format(rbean.getEpf3Accretion()) %></td><td align="right"><%=df.format(Math.round(rbean.getEpf3Emoluments() * 0.0018))%></td><td align="right"><%=df.format(rbean.getEpf3Pensioncontri())%></td><td align="right"><%=df.format(Math.round(rbean.getPfAccretion()-rbean.getEpf3Accretion()))%></td><td align="right"><%=Math.round((rbean.getEmoluments()-rbean.getEpf3Emoluments())*0.0018)%></td><td align="right"><%=df.format(rbean.getPensionContribution()-rbean.getEpf3Pensioncontri())%></td></tr>
<%}rbean=(RemittanceBean)remitancetableList.get(i);
   tempregion=rbean.getRegion();
   if(i==(remitancetableList.size()-1)){%>
 <tr><td>&nbsp;</td><td>&nbsp;</td><b><td class="HighlightData" align="right">Total &nbsp;</td><td align="right"><%=totalPFnoofEmp%></td><td align="right"><%=df.format(totalPfAccretion) %></td><td align="right"><%=df.format(totalPfEmoluments) %></td><td align="right"><%=df.format(Math.round(totalInspectionCharges)) %></td><td align="right"><%=df.format(totalPCnoofEmp) %></td><td align="right"><%=df.format(pcEmoluments) %></td><td align="right"><%=df.format(pensionContribution) %></td><td align="right"><%=df.format(totalEPf3Accretion) %></td><td align="right"><%=df.format(Math.round(totalEpf3Emoluments * 0.0018)) %></td><td align="right"><%=df.format(totalEPF3PensionContri) %></td><td align="right"><%=df.format(totalPfAccretion-totalEPf3Accretion) %></td><td align="right"> <%=df.format(totalDeviationInspectionChanges) %></td><td align="right"><%=df.format(deviationPensionContri) %></td><b></tr>	   
   <%}}}%> 
		 
 
</tr>
 		</table>

		</td>
	</tr>

</table>


</form>

</body>
</html>
