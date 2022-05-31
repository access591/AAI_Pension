
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <title>AAI</title>
     <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
     <link rel="stylesheet" type="text/css" href="styles.css">
   </head>
   <body>
<%
String dispYear="",chkStationString="",chkRegionString="",reportType="",fileName="";
FinancialReportService reportService=new FinancialReportService();
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }
 if(request.getAttribute("date")!=null){
    dispYear=(String)request.getAttribute("date");
    }
 if (request.getAttribute("reportType") != null) {
		reportType = (String) request.getAttribute("reportType");
		if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					fileName = "TRANSFER_IN_OUT("+dispYear+").xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
	}
 %>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
   <td>
   <table width="100%" align="right"  cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td width="120"  rowspan="3" align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="80" height="50" align="right" /></td>
    <td class="reportlabel" colspan="3" nowrap="nowrap" >AIRPORTS AUTHORITY OF INDIA</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
     	<td width="92">&nbsp;</td>
     	<td width="92">&nbsp;</td>
     	<td width="92">&nbsp;</td>
  	 	<td nowrap="nowrap"  class="reportlabel">Employee's Provident Fund Trust</td>
  	 	<td>&nbsp;</td>
    	<td>&nbsp;</td>
    	<td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td nowrap="nowrap" class="reportlabel" style="text-decoration: underline">Station Wise Employees In/Out Report</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
<tr >
<td colspan="10" align="left" nowrap="nowrap" class="Data"><b>REGION:</b> <font style="text-decoration: underline"><%=chkRegionString%></font></td>
<td align="left" nowrap="nowrap" class="Data"><%if(chkRegionString.equals("CHQIAD")){%>
&nbsp;&nbsp;&nbsp;Airportcode :<%=chkStationString %>
<%}%> </td>
<td align="right" nowrap="nowrap" class="Data"><b>&nbsp;Selected Transfer Month:</b> <font style="text-decoration: underline"><%=dispYear%></font></td>
</tr>

  </table>
</td>
</tr>
 <%EmployeePersonalInfo dbBeans = new EmployeePersonalInfo();

            if (request.getAttribute("empinfo") != null) {
                ArrayList empinfo = new ArrayList();                
                ArrayList cntinfo= new ArrayList();
                int totalData = 0;
                empinfo = (ArrayList) request.getAttribute("empinfo");
                cntinfo = (ArrayList) request.getAttribute("cntinfo");
                System.out.println("empinfo " + empinfo.size()); 
				  
                if (empinfo.size() == 0) {

                %>
			<tr>

				<td>
					<table align="center" id="norec">
						<tr>
							<br>
							<td>
								<b> No Records Found </b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<%} else if (empinfo.size() != 0) {%>       
			<tr>
				<td height="25%">
					<table align="center" width="100%" cellpadding="1"  cellspacing="1" border="1">
						<tr ><td class="label">
                          SR NO
                        </td>
						<td class="label">PENSION NO
							</td>
							<td class="label">
								EMPLOYEE NAME
							</td>
							<td class="label">
								DATE OF BIRTH
							</td>
							<td class="label">
								DATE OF JOINING
							</td>
							<td class="label">
								WETHER OPTION
							</td>
							<td class="label">
								SALARY MONTH
							</td>
							<td class="label">
								STATUS
							</td>							
							<td class="label">
								CURRENT MONTH STATUS
							</td>
							<td class="label">
								STATIONNAME
							</td>							
							<td class="label">
								REGION
							</td>	
</tr>

						<%}%>
						<%int count = 0;  
                System.out.println("====================empinfo.size()========"+empinfo.size());
                for (int i = 0; i < empinfo.size(); i++) {
                    count++;
                   
                    EmployeePersonalInfo beans = (EmployeePersonalInfo) empinfo.get(i);

                   String PENSIONNO = beans.getPfID();
                   String EMPLOYEENAME = beans.getEmployeeName();
                   String DATEOFBIRTH = beans.getDateOfBirth();
                   String DATEOFJOINING = beans.getDateOfJoining();
                   String WETHEROPTION = beans.getWetherOption();   
                    String Status = beans.getSeperationReason();
                   String TRANSFERSTATUS = beans.getRemarks();
                   String REGION = beans.getRegion();
                   String salaryMonth=beans.getMonthyear();
                   String airportcode=beans.getAirportCode();
                   if(TRANSFERSTATUS.trim().equals("TIn")){
                   TRANSFERSTATUS="Transfer In";
                   }else if(TRANSFERSTATUS.trim().equals("New")){
                   TRANSFERSTATUS="New Joinee";
                   }else if(TRANSFERSTATUS.trim().equals("TOut")){
                   TRANSFERSTATUS="Transfer Out";
                   }else if(TRANSFERSTATUS.trim().equals("Death")){
                   TRANSFERSTATUS="Death";
                   }else if(TRANSFERSTATUS.trim().equals("Termination")){
                   TRANSFERSTATUS="Termination";
                   }else if(TRANSFERSTATUS.trim().equals("Resignation")){
                   TRANSFERSTATUS="Resignation";
                   }else if(TRANSFERSTATUS.trim().equals("VRS")){
                   TRANSFERSTATUS="VRS";
                   }else if(TRANSFERSTATUS.trim().equals("Retirement")){
                   TRANSFERSTATUS="Retirement";
                   }else if(TRANSFERSTATUS.trim().equals("salarynotDrawn")){
                   TRANSFERSTATUS="Salary Not Drawn";
                   }                   
                   if (count % 2 == 0) {

                       %>
   						<tr>
   							<%} else {%>
   						<tr>
   							<%}%>
							<td class="Data" width="5%">
								<%=count%>
							</td>
							<td class="Data" width="8%">
								<%=PENSIONNO%>
							</td>
							<td class="Data" width="15%">
								<%=EMPLOYEENAME%>
							</td>
							<td class="Data" width="12%">
								<%=DATEOFBIRTH%>
							</td>
							<td class="Data" width="12%">
								<%=DATEOFJOINING%>
							</td>
							<td class="Data" width="8%">
                              <%=WETHEROPTION%>
                            </td>
                            <td class="Data">
								<%=salaryMonth%>
							</td>
							<td class="Data" width="18%">
                              <%=Status%>
                            </td>
							<td class="Data" width="12%">
								<%=TRANSFERSTATUS%>
							</td>
								
							<td class="Data" width="12%">
								<%=airportcode%>
							</td>
							<td class="Data" width="12%">
								<%=REGION%>
							</td>					
						</tr>
			<%}%> 
					</table>
				</td>

			</tr>
		
		<%
		System.out.println("===========cntinfo======="+cntinfo.size());
		if(cntinfo.size()>0){
		
		
		for(int j=0;j<cntinfo.size();j++){		
		  EmployeePersonalInfo cntInfo = (EmployeePersonalInfo) cntinfo.get(j);
			%>
			<tr>
			<td>&nbsp;</td>
			</tr>       
		<tr>
				<td height="25%">
					<table align="left" width="100%" cellpadding="1"  cellspacing="1" border="1">
					<tr> 
						<td class="reportlabel" colspan="2" align="center">Comparison of No.of Employees in Current Month <%=cntInfo.getCurntMnth()%>  with Last Month <%=cntInfo.getPrevMnth()%>
						</td>
							 
					</tr>
						<tr> 
						<td class="label">No of Subscribers as per Last Month's Return(A)
						</td>
							<td class="Data">
								<%=cntInfo.getPrevMntCnt()%>
							</td>
						</tr>
							<tr>
							<td class="label">
								Add: No.of New Subscribers/Transfer In (B)  
							</td>
							<td class="Data">
								<%=cntInfo.getTransferInCnt()%>
							</td>
							</tr>
							<tr > 
						<td class="label">Less: No.of New Subscribers Left Service/Transfer Out
						</td>
							<td class="Data">
								<%=cntInfo.getTransferOutCnt()%>
							</td>
						</tr>
							<tr > 
						<td class="label">Net Total(A+B-C)
						</td> 
							<td class="Data">
								 <%=Integer.parseInt(cntInfo.getPrevMntCnt())%>+<%=Integer.parseInt(cntInfo.getTransferInCnt())%>-<%=Integer.parseInt(cntInfo.getTransferOutCnt())%>(Actually Uploaded Data Current Month <%=cntInfo.getCurntMntCnt()%>)
							</td>
							 
						</tr>	
 	
	
	</table>	
	</td>
	</tr>	
	<%}}%>
	
<%} %>	
	
					
</table>

  </body>
</html>
