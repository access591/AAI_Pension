
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%
	String region = "";
	String monthYear = "";
	String airport = "";
	
	region = request.getParameter("frm_region");
	monthYear = request.getParameter("frm_month_year");
	airport = request.getParameter("airport");
	
	System.out.println("region==== "+region+" --- monthYear=== "+monthYear+":: airport------"+airport);
%>
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

<SCRIPT SRC="<%=basePath%>PensionView/scripts/CommonFunctions.js"> </SCRIPT>
<SCRIPT SRC="<%=basePath%>PensionView/scripts/GeneralFunctions.js"> </SCRIPT>
<SCRIPT SRC="<%=basePath%>PensionView/scripts/DateTime.js"> </SCRIPT>
<SCRIPT SRC="<%=basePath%>PensionView/scripts/calendar.js"> </SCRIPT>

<script type="text/javascript">
 
 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadform6";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	function validateForm() {
	 	var swidth=screen.Width-10;
		var sheight=screen.Height-150;

		if(document.forms[0].remittedAmount.value==''){
			alert('Please Enter Remitted Amout (Mandatory)');
			document.forms[0].remittedAmount.focus();
			return false;
		}else {
			if(!ValidateFloatPoint(document.forms[0].remittedAmount.value,14,2))
	        { 
				document.forms[0].remittedAmount.focus();
				return false;
	        }
		}
		if(document.forms[0].remittedDate.value==''){
			alert('Please Select Date of Remittance (Mandatory)');
			document.forms[0].remittedDate.focus();
			return false;
		}else {
			if(!convert_date(document.forms[0].remittedDate))
			{
				document.forms[0].remittedDate.focus();
				return(false);
			}
		}
		if(document.forms[0].bankName.value==''){
			alert('Please Select Bank Name (Mandatory)');
			document.forms[0].bankName.focus();
			return false;
		}
		if(document.forms[0].bankAddr.value==''){
			alert('Please Select Bank Address (Mandatory)');
			document.forms[0].bankAddr.focus();
			return false;
		}
		
		var params = '';
		params = "&remitAmount="+document.forms[0].remittedAmount.value+"&remittedDate="+document.forms[0].remittedDate.value+"&bankName="+document.forms[0].bankName.value+"&bankAddr="+document.forms[0].bankAddr.value;
		params = params + "&frm_region=<%=region%>&frm_month_year=<%=monthYear%>&airport=<%=airport%>";
		
		//alert(params);
		
		var url="<%=basePath%>validatefinance?method=insertRemittanceInfo"+params;
		LoadWindow(url);
		//document.forms[0].action=url;
		//document.forms[0].method="post";
		//document.forms[0].submit();
	
	}
	
</script>
</HEAD>
<body class="BodyBackground" onload="document.forms[0].remittedAmount.focus();">

<form action="method" >
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		
		<tr>
			<td height=60>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table align="center" width="75%" cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
								<tr>
									<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">	Form6 Remittance Info[New]</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td class="label" align="right">Remitted Amount</td>
									<td><input type="text" name="remittedAmount" ></td>
								</tr>
								<tr>
									<td class="label" align="right">Date of remittance</td>
									<td><input type="text" name="remittedDate" >
									&nbsp; <a href="javascript:show_calendar('forms[0].remittedDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a></td>
								</tr>
								<tr>
									<td class="label" align="right">Bank Name </td>
									<td><input type="text" name="bankName" ></td>
								</tr>
								<tr>
									<td class="label" align="right">Bank Address </td>
									<td><textarea name="bankAddr" ></textarea></td>
								</tr>
								<tr>  
		     						<td align="center">&nbsp;&nbsp;&nbsp;&nbsp;</td>  
		   						</tr>  		
								<tr>
								 <td align="right" >
		        					<input type="button" class="btn"  name="Submit" value="Submit" onclick="javascript:validateForm()"> 
		        					<input type="button" class="btn"  name="Reset" value="Reset" onclick="javascript:resetReportParams()" >
		        					<input type="button" class="btn"  name="Submit" value="Cancel" onclick="javascript:history.back(-1)" >
		       					 </td>
		       					 </tr>
		                    	<tr>  
		     						<td align="center"></td>  
		   						</tr>  	
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>