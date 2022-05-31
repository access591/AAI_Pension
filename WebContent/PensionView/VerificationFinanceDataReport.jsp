<!--
/*
  * File       : VerificationFinanceDataReport.jsp
  * Date       : 29/12/2011
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->


<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page
	import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.PensionBean,aims.bean.EmpMasterBean"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
									
	    String region="";
	  	CommonUtil common=new CommonUtil();    
	
	   	HashMap hashmap=new HashMap();
		hashmap=common.getRegion();
	
		Set keys = hashmap.keySet();
		Iterator it = keys.iterator();
		String empNameChecked="";
		if(request.getAttribute("empNameChecked")!=null){
		empNameChecked=request.getAttribute("empNameChecked").toString();
		System.out.println("empNameChecked  "+empNameChecked);
		
	}
	
  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

</head>
<body>
<form>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>&nbsp;</td>
				<td width="7%" rowspan="2"><img
					src="<%=basePath%>PensionView/images/logoani.gif" width="100"
					height="50" /></td>
				<td class="reportlabel" >AIRPORTS AUTHORITY OF INDIA</td>
			</tr>
			<tr>
				<td width="38%">&nbsp;</td>
				<td width="55%" class="reportlabel" >Verification Of PC Report</td>
			</tr>
			<tr>
				<td colspan="3" align="center">&nbsp;</td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
	
	<td width="55%" colspan="2" class="Data" align="right" >Date:<%=common.getCurrentDate("dd-MMM-yyyy HH:mm:ss")%></td>
	<td width="3%"></td>
	</tr>
	
	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>
		<%
				if (request.getAttribute("financeDatalist") != null) {
		
				ArrayList dataList = new ArrayList();
				
				dataList = (ArrayList) request.getAttribute("financeDatalist");
			
			   	if (dataList.size() == 0) {

				%>
		
	<tr>

		<td>
		<table align="center" id="norec">
			<tr>
				<br>
				<td><b> No Records Found </b></td>
			</tr>
		</table>
		</td>
	</tr>

	<%} else if (dataList.size() != 0) {
				 System.out.println("Size===After========="+dataList.size());
				%>
	<tr>
		<td></td>
	</tr>
</table>

<tr>

	<td>
	<table align="center">
		<tr>
			<td colspan="3"></td>
			<td colspan="2" align="right">
			<%if(request.getAttribute("reportType")!=null){
			String	reportType=(String)request.getAttribute("reportType");
				
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
					
					String fileName = "FinanceDataMappingSearch.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}%>
			</td>
		</tr>
	</table>
</tr>

	<td>
	<table width="95%" align="center" cellpadding=2 
			cellspacing="0" border="1"  bordercolor ="gray">

			<tr >
				<td class="label">S.No&nbsp;&nbsp;</td>
				<td class="label">PFID&nbsp;&nbsp;</td>
				<td class="label">CPFAC.No&nbsp;&nbsp;</td>
				<td class="label">EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Designation&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">DateofBirth&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">DateofJoining&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Airport Code &nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Region&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">PCReportVerified&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Form 6-7-8 Status&nbsp;&nbsp;</td>
				<td class="label">PensionClaims Processes&nbsp;&nbsp;</td>
				<%int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
					count++;
					PensionBean beans = (PensionBean) dataList.get(i);%>
				
			<% 
				String bgcolor="";
				String title="";
				if(beans.getSeperationReason().equals("Death") || beans.getSeperationReason().equals("Retirement") || beans.getSeperationReason().equals("Resignation") || beans.getSeperationReason().equals("Termination") || beans.getSeperationReason().equals("Resigned")) {
					 bgcolor="yellow";
					 title=beans.getSeperationReason()+" on "+beans.getSeperationDate();
				}
			%>
 			<tr bgcolor='<%=bgcolor%>'  title='<%=title%>'>
 				<td class="Data"><%=count%></td>
				<td class="Data"><%=beans.getPensionnumber()%></td>
				<%if(beans.getCpfAcNo().equals("")){%>
				<td class="Data">---</td>
				<%}else{%>
				<td class="Data"><%=beans.getCpfAcNo()%></td>
				<%}%>
				<td class="Data"><%=beans.getEmployeeName()%> &nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getDesegnation()%></td>
				<td class="Data"><%=beans.getDateofBirth()%></td>
				<td class="Data"><%=beans.getDateofJoining()%></td>
				<td class="Data"><%=beans.getAirportCode()%></td>
				<td class="Data" nowrap><%=beans.getRegion()%></td>
				<%if(beans.getPcreportverified().equals("N")){%>
			 	<td class="Data">No</td>
			 	<%}else{%>
			 	<td class="Data">Yes</td>
			 	<%}%>
			 	<%if(beans.getFormsdisable().equals("N")){%>
			 	<td class="Data">No</td>
			 	<%}else{%>
			 	<td class="Data">Yes</td>
			 	<%}%>
			 	<%if(beans.getClaimsprocess().equals("N")){%>
			 	<td class="Data">No</td>
			 	<%}else{%>
			 	<td class="Data">Yes</td>
			 	<%}%> 	
                     
		</tr>
		<%}
		}
		}
		%>
	</table>


</form>
</body>
</html>