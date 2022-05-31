<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="java.beans.Beans"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>

	</head>
	<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
	
   		document.forms[0].action="<%=basePath%>psearch?method=navigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function openURL(sURL) { 
	//alert(sURL);
		window.open(sURL,"Window1","menubar=no,width='100%',height='100%',toolbar=no,fullscreen=yes");

	} 
	function hiLite(imgDocID, imgObjName, comment) {
	if (browserVer == 1) {
	document.images[imgDocID].src = eval(imgObjName + ".src");
	window.status = comment; return true;
	}}
	
	
	 function testSS(){

    	document.forms[0].action="<%=basePath%>psearch?method=search";
    	
		document.forms[0].method="post";
		document.forms[0].submit();
   		 }
   		  function callReport()
   		 {  window.open ("<%=basePath%>PensionView/PensionFundReport.jsp","mywindow","location=1,status=1,toolbar=1,width=700,height=450,channelmode=yes,resizable=yes"); 
   		 	//document.forms[0].action="./PensionFundReport.jsp"
			//document.forms[0].method="post";
			//document.forms[0].submit();
   		
   		 }
	</script>
	<body class="BodyBackground" onload="document.forms[0].empName.focus();">
		<form method="post" action="<%=basePath%>psearch?method=search">

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
					<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>


				<tr>
					<td>
						&nbsp;
					</td>
				</tr>


				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">Pension Information</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<%boolean flag = false;%>
				<tr>
					<td>
						<table align="center">

							<tr>
								<td class="label">
									Employee Name:
								</td>
								<td>
									<input type="text" name="empName">
								</td>
								<td class="label">
									Airport Code:
								</td>
								<td>
									<input type="text" name="airPortCD">
								</td>
							</tr>
							<tr>
								<td class="label">
									From Date:
								</td>
								<td>
									<input type="text" name="fromDt">
									<a href="javascript:show_calendar('forms[0].fromDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								<td class="label">
									To Date:
								</td>
								<td>
									<input type="text" name="toDt">
									<a href="javascript:show_calendar('forms[0].toDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" />
								</td>
							</tr>
							<tr>
								<td class="label">
									CPF Acno:
								</td>
								<td>
									<input type="text" name="cpfaccno">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>

								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>


				</tr>
				<%DatabaseBean dbBeans = new DatabaseBean();
			SearchInfo getSearchInfo = new SearchInfo();
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (DatabaseBean) request.getAttribute("searchInfo");
				int index = searchBean.getStartIndex();

				session.setAttribute("getSearchBean", dbBeans);
				dataList = searchBean.getSearchList();
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
				if (dataList.size() != 0) {

					%>
				<tr>

					<td>
						<table align="center">
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="right">
									<input type="button" alt="first" value="|<" name=" First" disable=true onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>

									<strong><input type="button" value="<" name=" Pre"  onClick="redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>> <strong><input type="button" value=">" name="Next"
												onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>> <input type="button" value=">|" name="Last" onClick="redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>> <img
												src="./PensionView/images/printIcon.gif" alt="Report" onClick="callReport()"> <!--
										
									    <img src="./images/first1.gif" alt="First"  onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
										<img src="./images/arrow-previous.gif" alt="Previous"  onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
										<img src="./images/arrow-next.gif" alt="Next" onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
										<img src="./images/last1.gif" alt="Last" onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
										--> <%}%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25%">
						<table align="center"  cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">
								<td>
									Airport Code
								</td>
								<td>
									Employee Name
								</td>
								<td>
									Designation
								</td>
								<td>
									Salary&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									PF&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									Pension Fund
								</td>
								<td>
									From Date
								</td>
								<td>
									To Date
								</td>

							</tr>
							<%int count = 0;
				String airportCode = "", empNM = "", salary = "", desig = "", pf = "", pensionFund = "", fromDT = "", toDT = "";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					DatabaseBean beans = (DatabaseBean) dataList.get(i);

					empNM = beans.getEmpName();
					airportCode = beans.getAirPortCD();
					salary = beans.getSalary();
					desig = beans.getDesegination();
					pf = beans.getPf();
					pensionFund = beans.getPensionFund();
					fromDT = beans.getFromDt();
					toDT = beans.getToDt();
					if (count % 2 == 0) {

					%>
							<tr >
								<%} else {%>
							<tr>
								<%}%>
								<td class="Data">
									<%=airportCode%>
								</td>
								<td class="Data">
									<%=empNM%>
								</td>
								<td class="Data">
									<%=desig%>
								</td>
								<td class="Data">
									<%=salary%>
								</td>
								<td class="Data">
									<%=pf%>
								</td >
								<td class="Data">
									<%=pensionFund%>
								</td>
								<td class="Data">
									<%=fromDT%>
								</td>
								<td class="Data">
									<%=toDT%>
								</td>
							</tr>
							<%}%>

							<%if (dataList.size() != 0) {%>
							<tr>
								<td>
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
							<%}
			}%>

						</table>
			
			</table>

		</form>
	</body>
</html>
