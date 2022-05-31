<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<script t function
	redirectPageNav(navButton, index, totalValue) {
		docu
	ment.forms[0]
	.action="             search1?method=pensionDataMissingMonthnavigation&navButton="
	+ navButton + "&strtindx=" + index + "
	&total=" + totalValue;
		document.forms[0].method = "
	post";
		document.forms[0].submit();
	}
	function
	openURL(sURL) {
		window.open(sURL, "Window1",
				"menubar=no,width=430,height=360,toolbar=no
	");
	}
	function
	editPensionMaster(obj, employeeName, employeeCode) {

		var
	cpfacno=obj; var answer=confirm( 'Are you sure, do you want edit this
	record');
		if (answer) {
			var flag="true" ;
			docu
	ment.forms[0]
	.action="             search1?method=edit&cpfacno="
	+ cpfacno
					+ "&name="
					+ employeeName
					+ "
	&flag="
					+ flag
					+ "
	&empCode=" + employeeCode;

			document.forms[0].method = "
	post";
			document.forms[0].submit();
		}
	}

	function
	testSS() {

		if (!document.forms[0].fromDt.value== "") {
			var
	date1=document.forms[0].fromDt; var val1=convert_date(date1);
	if (val1== false) {
				return
	false;
			}
		}
		if (!document.forms[0].toDt.value== "") {
			var
	date1=document.forms[0].toDt; var val1=convert_date(date1); if (val1==
	false) {
				return false;
			}
		}

		docu
	ment.forms[0]
	.action="             psearch?method=searchPensionDataMissingMonth"
	;
		alert(document.forms[0].action);
		document.forms[0].method="post"
	;
		document.forms[0].submit();
	}

	function
	callReport() {

   	   windo
	w.open("             PensionView/PensionEmpInfoReport.jsp",
				"mywindow", "status=1,toolbar=1,channelmode=yes,resizable=yes");
  		 }
   		  
	

</script>
</head>

<body class="BodyBackground"
	onload="document.forms[0].airPortCode.focus();">

<form action="" method="post">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>


	<tr>
		<td height="5%" colspan="2" align="center" class="ScreenHeading">
		Finance Data Missing month</td>

		<%
			boolean flag = false;
		%>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td height="15%">
		<table align="center">
			<tr>
				<td class="label">FromDate:</td>
				<td><input type="text" name="fromDt"> <a
					href="javascript:show_calendar('forms[0].fromDt');"><img
					src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
				</td>
				<td class="label">ToDate:</td>
				<td><input type="text" name="toDt"> <a
					href="javascript:show_calendar('forms[0].toDt');"><img
					src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
				</td>

			</tr>
			<tr>
				<td class="label">Employee Name:</td>
				<td><input type="text" name="empName"
					onkeyup="return limitlength(this, 20)"></td>

			</tr>

			<tr>

				<td align="left">&nbsp;
				<td>
			</tr>

			<tr>

				<td align="left">
				<td><input type="button" class="btn" value="Search" class="btn"
					onclick="testSS();"> <input type="button" class="btn"
					value="Reset" onclick="javascript:document.forms[0].reset()"
					class="btn"> <input type="button" class="btn"
					value="Cancel" onclick="javascript:history.back(-1)" class="btn">
				</td>

			</tr>
		</table>
		</td>

	</tr>

	<tr>
		<td height="25%">
		<%
			EmpMasterBean dbBeans = new EmpMasterBean();
			SearchInfo getSearchInfo = new SearchInfo();
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				//dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
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
				<td colspan="3"></td>
				<td colspan="2" align="right"><input type="button" alt="first"
					value="|<" name=" First" disable=true
					onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusFirst()%>> <input type="button"
					alt="pre" value="<" name="
					Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusPrevious()%>> <input type="button"
					alt="next" value=">" name="Next"
					onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusNext()%>> <input type="button"
					value=">|" name="Last"
					onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')"
					<%=bottomGrid.getStatusLast()%>> <img
					src="./PensionView/images/printIcon.gif" alt="Report"
					onClick="callReport()"> <%
 	}
 %>
				</td>
			</tr>
		</table>
	</tr>
	<tr>
		<td height="25%">
		<table align="center" cellpadding=2 class="tbborder" cellspacing="0"
			border="0">

			<tr class="tbheader">

				<td class="tblabel">CPF ACC.NO</td>
				<td class="tblabel">Pension Number&nbsp;&nbsp;</td>
				<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
				<td class="tblabel">Employee Code&nbsp;&nbsp;</td>
				<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
				<td class="tblabel">Designation</td>

				<td><img src="./PensionView/images/page_edit.png" alt="edit" />&nbsp;

				</td>
				<td><img src="./PensionView/images/cross.png" alt="delete" /></td>

			</tr>
			<%
				int count = 0;
					String airportCode = "", employeeName = "", desegnation = "", employeeCode = "", cpfacno = "", pensionNumber = "";
					for (int i = 0; i < dataList.size(); i++) {
						count++;
						PensionBean beans = (PensionBean) dataList.get(i);
						cpfacno = beans.getCpfAcNo();
						airportCode = beans.getAirportCode();
						desegnation = beans.getDesegnation();
						employeeName = beans.getEmployeeName();
						employeeCode = beans.getEmployeeCode();
						pensionNumber = beans.getPensionnumber();
						if (count % 2 == 0) {
			%>
			<tr>
				<%
					} else {
				%>
			
			<tr>
				<%
					}
				%>

				<td class="Data"><%=cpfacno%></td>
				<td class="Data"><%=pensionNumber%></td  class="Data">

				<td class="Data"><%=airportCode%></td>
				<td class="Data"><%=employeeCode%></td>

				<td class="Data"><%=employeeName%></td>
				<td class="Data"><%=desegnation%></td>
				<td><input type="checkbox" name="cpfno" value="<%=cpfacno%>"
					onclick="javascript:editPensionMaster('<%=cpfacno%>','<%=employeeName%>','<%=employeeCode%>')" />
				</td>
				<td><input type="checkbox" name="cpfno" value="<%=cpfacno%>"
					onclick="javascript:deletePensionMaster('<%=cpfacno%>','<%=employeeName%>','<%=employeeCode%>')" />
				</td>
			</tr>
			<%
				}
			%>

			<%
				if (dataList.size() != 0) {
			%>
			<tr>

				<td class="Data"><font color="red"><%=index%></font>
				&nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
				</td>
			</tr>
			</tr>
			<%
				}
				}
			%>

		</table>
		</td>

	</tr>

</table>



</form>
</body>
</html>
