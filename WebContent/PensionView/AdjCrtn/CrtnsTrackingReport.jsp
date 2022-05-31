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
  <%
				if (request.getAttribute("crtnsMadeInPcList") != null) {
		
				ArrayList dataList = new ArrayList();
				
				dataList = (ArrayList) request.getAttribute("crtnsMadeInPcList");
			
			   //	if (dataList.size() == 0) {
			   //	} else if (dataList.size() != 0) {
				// System.out.println("Size===After========="+dataList.size());
				if (dataList.size() != 0) {
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
				<td colspan="2" class="reportlabel" >LIST OF CORRECTIONS MADE IN PC REPORT & PF CARD BY USERS  DURING 2011-12</td>
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
					
					String fileName = "CorrectionsMadeInPCardAndPFCardreport.xls";
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
				<td class="label">Periods of Corrections  &nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">AdjOB In PFCard 2011-2012&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">Date of Editing/Approval&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="label">User Name&nbsp;&nbsp;</td>
				<td class="label">Editor/Approver&nbsp;&nbsp;</td>
				<td class="label">Region&nbsp;&nbsp;</td>
				<td class="label">Station&nbsp;&nbsp;</td>
			</tr>	
				<%
				String prvPenNo="",region="";
				int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
					//count++;
					
					CrtnsMadeInPcBean beans = (CrtnsMadeInPcBean) dataList.get(i);
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
 			
 			<%if(prvPenNo.equals(beans.getPensionno())){
 			//System.out.println("pppppp"+prvPenNo);
 			%>

				<tr>
 				<td  colspan="5"> </td>
				<td class="Data"><%=beans.getPeriodOfCorrction()%></td>
				<td class="Data"><%=beans.getAdjObInPFCard() %></td>
				<td class="Data"><%=beans.getDateOfApproval() %></td>
				<td class="Data"><%=beans.getUsername() %></td>
				<td class="Data"><%=beans.getApproverOrEditor() %></td>
				<td class="Data"><%=region %></td>
				<td class="Data"><%=beans.getStationOrRegion() %></td>
				</tr>
				<%}else{
				count++;
				//System.out.println("qqqqqq"+prvPenNo);%>

				<tr>
 				<td class="Data"><%=count%></td>
				<td class="Data"><%=beans.getPensionno() %></td>
				<td class="Data"><%=beans.getEmployeeName()%></td>
				<td nowrap class="Data"><%=beans.getDateOfBirth()%> &nbsp;&nbsp;</td>
				<td nowrap class="Data"><%=beans.getDateOfJoining()%>&nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getPeriodOfCorrction()%></td>
				<td class="Data"><%=beans.getAdjObInPFCard() %></td>
				<td class="Data"><%=beans.getDateOfApproval() %></td>
				<td class="Data"><%=beans.getUsername() %></td>
				<td class="Data"><%=beans.getApproverOrEditor() %></td>
				<td class="Data"><%=region %></td>
				<td class="Data"><%=beans.getStationOrRegion() %></td>
				
                     
		</tr>
		<%}
		prvPenNo=beans.getPensionno();
		}
		}else{
		//}
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
	<%}
	}%>
	</table>


</form>
</body>
</html>