<!--
/*
  * File       : FinanceDataMapping.jsp
  * Date       : 29/01/2010
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
<title>AAI</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
<script type="text/javascript"> 
		
	
  	</script>
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
				<td>AIRPORTS AUTHORITY OF INDIA</td>
			</tr>
			<tr>
				<td width="38%">&nbsp;</td>
				<td width="55%">Finance Data Mapping</td>
			</tr>
			<tr>
				<td colspan="3" align="center">&nbsp;</td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>


	<tr>
		<td>&nbsp;</td>
	</tr>


	<%boolean flag = false;%>
	<tr>
		<td>
		<%
						FinacialDataBean dbBeans = new FinacialDataBean();
						SearchInfo getSearchInfo = new SearchInfo();
			
			if (request.getAttribute("financeDatalist") != null) {
				 int totalData = 0,index=0,totalUnmappedRecords=0;
				 String empNameCheck="";
				SearchInfo searchBean = new SearchInfo();
				EmpMasterBean empSerach = new EmpMasterBean();
				PensionBean  pensionBean= new PensionBean();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dataList = (ArrayList) request.getAttribute("financeDatalist");
				empSerach = (EmpMasterBean) request.getAttribute("empSerach");
				
				session.setAttribute("getSearchBean1", empSerach);
				empNameCheck=empSerach.getEmpNameCheak();
				System.out.println("empNameCheck "+empNameCheck);
				totalData = searchBean.getTotalRecords();
				totalUnmappedRecords=searchBean.getTotalUnmappedRecords();
			    bottomGrid = searchBean.getBottomGrid();
	            index = searchBean.getStartIndex();
				region=(String)request.getAttribute("region");
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
</tr>
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
<tr>
	<td>

	<table width="95%" align="center" cellpadding="0" border="1"
		bordercolor="gray" cellspacing="0" border="0">

		<tr>
			
			<td class="reportsublabel">PFID&nbsp;&nbsp;</td>
			<td class="reportsublabel">CPFAC.No&nbsp;&nbsp;</td>
			<td class="reportsublabel">EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td class="reportsublabel">EmployeeNo&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="reportsublabel">Designation&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="reportsublabel">DateOfBirth&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="reportsublabel">
			DateOfJoining&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="reportsublabel">Airport Code &nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td class="reportsublabel">Region&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td class="reportsublabel">Total No of
			Months&nbsp;&nbsp;&nbsp;&nbsp;</td>




			<%int count = 0;
				  for (int i = 0; i < dataList.size(); i++) {
						count++;
						PensionBean beans = (PensionBean) dataList.get(i);%>
				<% if(beans.getSeperationReason().equals("Death") || beans.getSeperationReason().equals("Retirement")) {%>
				 <tr bgcolor="yellow"  title='<%=beans.getSeperationReason()%>'>
					<td class="Data">
					<%if(beans.getPensionnumber().equals("")){%> <input type="text"
						name="empserialNO<%=i%>" size="10"
						value='<%=beans.getPensionnumber()%>'> <%}else{ %> <input
						type="text" name="empserialNO<%=i%>" readonly="true" size="10"
						value='<%=beans.getPensionnumber()%>'> <%} %>
					</td>
					<td class="Data"><%=beans.getCpfAcNo()%></td>
					<td class="Data"><%=beans.getEmployeeName()%> &nbsp;&nbsp;</td>
					<td class="Data"><%=beans.getEmployeeCode()%> &nbsp;&nbsp;</td>
					<td class="Data"><%=beans.getDesegnation()%></td>
					<td class="Data"><%=beans.getDateofBirth()%></td>
					<td class="Data"><%=beans.getDateofJoining()%></td>
					<td class="Data"><%=beans.getAirportCode()%></td>
					<td class="Data"><%=beans.getRegion()%></td>
					<td class="Data"><%=beans.getTotalRecrods()%></td>
					<td class="Data"><a href="#"
						onClick="validateForm('<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>')"><img
						src="./PensionView/images/viewDetails.gif" border="0"
						alt="PFReport"></a></td>
					<td><input type="checkbox" name="mappingflag"
						value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>'"></input></td>
					<td class="Data"><input type="checkbox" name="mappingUpdate"
						value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>','empserialNO<%=i%>'"></input>
					</td>
					<td class="Data"><a href="#"> </a></td>

				</tr> <%} else{
				%><tr><td class="Data">
					<%if(beans.getPensionnumber().equals("")){%> <input type="text"
						name="empserialNO<%=i%>" size="10"
						value='<%=beans.getPensionnumber()%>'> <%}else{ %> <input
						type="text" name="empserialNO<%=i%>" readonly="true" size="10"
						value='<%=beans.getPensionnumber()%>'> <%} %>
					</td>
					<td class="Data"><%=beans.getCpfAcNo()%></td>
					<td class="Data"><%=beans.getEmployeeName()%> &nbsp;&nbsp;</td>
					<td class="Data"><%=beans.getEmployeeCode()%> &nbsp;&nbsp;</td>
					<td class="Data"><%=beans.getDesegnation()%></td>
					<td class="Data"><%=beans.getDateofBirth()%></td>
					<td class="Data"><%=beans.getDateofJoining()%></td>
					<td class="Data"><%=beans.getAirportCode()%></td>
					<td class="Data"><%=beans.getRegion()%></td>
					<td class="Data"><%=beans.getTotalRecrods()%></td>
					<td class="Data"><a href="#"
						onClick="validateForm('<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>')"><img
						src="./PensionView/images/viewDetails.gif" border="0"
						alt="PFReport"></a></td>
					<td><input type="checkbox" name="mappingflag"
						value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>'"></input></td>
					<td class="Data"><input type="checkbox" name="mappingUpdate"
						value="'<%=beans.getPensionnumber()%>','<%=beans.getCpfAcNo()%>','<%=beans.getRegion()%>','empserialNO<%=i%>'"></input>
					</td>
					<td class="Data"><a href="#"> </a></td>

				</tr>
				<%}}%>


		<%if (dataList.size() != 0) {%>
		<tr>
			<td></td>
		</tr>
		<%}}}
			    %>

	</table>
	</td>
</tr>
</table>

</form>
</body>
</html>
