<!--
/*
  * File       : DuplicateBlockedorFrozenReport.jsp
  * Date       : 23/10/2012
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
				if (request.getAttribute("duplicateBlockedorFrozenList") != null) {
		
				ArrayList dataList = new ArrayList();
				
				dataList = (ArrayList) request.getAttribute("duplicateBlockedorFrozenList");
			
			 
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
				<td >&nbsp;</td>
				<td colspan="4" class="reportlabel" > DUPLICATE BLOCKED/ADJ FROZEN PFIDS</td>
			</tr>
			<tr>
				<td >&nbsp;</td>
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
		<td>&nbsp;</td>
	</tr>

	
		
		
</table>



	
	<table align="center">
		<tr>
			<td colspan="3"></td>
			<td colspan="2" align="right">
			<%if(request.getAttribute("reportType")!=null){
			String	reportType=(String)request.getAttribute("reportType");
				
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
					
					String fileName = "Blocked/FrozenPfidsreport.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}%>
			</td>
		</tr>
	</table>


	
	<table width="95%" align="center" cellpadding=2 
			cellspacing="0" border="1"  bordercolor ="gray">

			<tr >
				<td rowspan="2" class="label">S.No.&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">PFID&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Employee Name&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Date of Birth&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Date of Joining&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td  colspan="2" class="label">Period of Data Restriction(Blocked/Frozen)&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Restriction Type&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Date of Restriction&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Restricted by&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Region&nbsp;&nbsp;</td>
				<td rowspan="2" class="label">Station&nbsp;&nbsp;</td>
			</tr>	
			<tr >
				<td class="label">FromDate&nbsp;&nbsp;</td>
				<td class="label">&nbsp;&nbsp;ToDate&nbsp;&nbsp;</td>
				
				
			</tr>
				<%
				String prvPenNo="",region="";
				int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
					count++;
					
					CrtnsMadeInPcBean beans = (CrtnsMadeInPcBean) dataList.get(i);
					%>

				<tr>
 				<td class="Data"><%=count%></td>
				<td class="Data"><%=beans.getPensionno() %></td>
				<td class="Data"><%=beans.getEmployeeName()%></td>
				<td nowrap class="Data"><%=beans.getDateOfBirth()%> &nbsp;&nbsp;</td>
				<td nowrap class="Data"><%=beans.getDateOfJoining()%>&nbsp;&nbsp;</td>
				<td class="Data"><%=beans.getFromDate()%></td>
				<td class="Data"><%=beans.getToDate()%></td>
				<td class="Data"><%=beans.getPurpose() %></td>
				<td class="Data"><%=beans.getUpdatedDate() %></td>
				<td class="Data"><%=beans.getUsername() %></td>
				<td class="Data"><%=beans.getRegion() %></td>
				<td class="Data"><%=beans.getStationOrRegion() %></td>
				
                     
		</tr>
		<%
	
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