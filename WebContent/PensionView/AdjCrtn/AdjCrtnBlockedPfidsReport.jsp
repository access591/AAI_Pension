<!--
/*
  * File       : CrtnsTrackingReport.jsp
  * Date       : 29/02/2012
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
<%@ page import="aims.bean.PensionBean,aims.bean.CrtnsMadeInPcBean,aims.bean.EmpMasterBean"%>
<%
	String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		CommonUtil common=new CommonUtil();    
		


  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>AAI</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
</head>
<body>
<form>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="7%">&nbsp;</td>
				<td width="5%" rowspan="1"><img
					src="<%=basePath%>PensionView/images/logoani.gif" width="100"
					height="50" /></td>
				<td width="20%" nowrap align="left" class="reportlabel" >AIRPORTS AUTHORITY OF INDIA</td>
			</tr>
			
			<tr>
				<td width="7%">&nbsp;</td>
				<td width="7%">&nbsp;</td>
				<td colspan="4" class="reportlabel" >Blocked PFIDS Report</td>
			</tr>
			<tr>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
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
				if (request.getAttribute("searchList") != null) {
		
				ArrayList dataList = new ArrayList();
				
				dataList = (ArrayList) request.getAttribute("searchList");
			
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
			<%if(request.getAttribute("reporttype")!=null){
			String	reportType=(String)request.getAttribute("reporttype");
			
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					String fileName = "Blocked PFIDS Report.xls";
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
				<td class="label">S.No.&nbsp;&nbsp;</td>
				<td class="label">PFID&nbsp;&nbsp;</td>
				<td class="label">Employee Name&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Date of Birth&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Date of Joining&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">AdjobYear  &nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">AdjOB Emp Subscription&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">AdjOB AAI Contri&nbsp;&nbsp;</td>
				<td class="label">AdjOB PenContri&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Blocked By&nbsp;&nbsp;</td>
				<td class="label">Blocked Date&nbsp;&nbsp;</td>
				<td class="label">Region&nbsp;&nbsp;</td>
				<td class="label">Station&nbsp;&nbsp;</td>
				<td class="label">Remarks&nbsp;&nbsp;</td>
				
			</tr>	
				<%
				String prvPenNo="",region="";
				int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
					count++;
					
					EmpMasterBean beans = (EmpMasterBean) dataList.get(i);
					%>

			<% 
				//String bgcolor="white";
				//String title="";
			//if(beans.getDateOfSeperationReason().equals("Death") || beans.getDateOfSeperationReason().equals("Retirement") || beans.getDateOfSeperationReason().equals("Resignation") || beans.getDateOfSeperationReason().equals("Termination") || beans.getDateOfSeperationReason().equals("Resigned")) {
				// bgcolor="yellow";
					// title=beans.getDateOfSeperationReason()+" on "+beans.getDateOfSeperationReason();
				//}
				if(beans.getRegion().equals("ALL-REGIONS")){
				
			 	region="CHQ";
				}else{
				region=beans.getRegion();
				}
			%>
 			
 			
				
				<tr>
 				<td class="Data"><%=count%></td>
				<td class="Data"><%=beans.getPfid() %></td>
				<td class="Data"><%=beans.getEmpName() %></td>
				<td nowrap class="Data"><%=beans.getDateofBirth() %> &nbsp;&nbsp;</td>
				<td nowrap class="Data"><%=beans.getDateofJoining() %>&nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getReportYear() %></td>
				<td class="Data"  align="right"><%=beans.getEmpSubTot() %></td>
				<td class="Data"  align="right"><%=beans.getAaiContriTot() %></td>
				<td class="Data"  align="right"><%=beans.getPensionTot() %></td>
				<td class="Data"><%=beans.getUserName() %></td>
				<td class="Data"><%=beans.getApprovedDate() %></td>
				<td class="Data"><%=region %></td>
				<td class="Data"><%=beans.getStation() %></td>
				<td class="Data"><%=beans.getNotes() %></td>
				
                     
		
	<%	}
		}
		}
		%>
	</table>


</form>
</body>
</html>