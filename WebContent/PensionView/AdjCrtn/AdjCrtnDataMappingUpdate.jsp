

<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page
	import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.PensionBean,aims.bean.EmpMasterBean"%>
<%String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }
 String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					String pensionno="",cpfaccno="",employeeName="",region="",
					designation="",employeeCode="",pensionOption="",dateOfBirth="",dateOfJoinning="";
					
		if(request.getParameter("pensionno")!=null){
		pensionno=request.getParameter("pensionno");
		}
		if(request.getParameter("cpfaccno")!=null){
		cpfaccno=request.getParameter("cpfaccno");
		}
		if(request.getParameter("employeeName")!=null){
		employeeName=request.getParameter("employeeName");
		}
		if(request.getParameter("region")!=null){
		region=request.getParameter("region");
		}
		if(request.getParameter("designation")!=null){
		designation=request.getParameter("designation");
		}
		if(request.getParameter("employeeCode")!=null){
		employeeCode=request.getParameter("employeeCode");
		}
		if(request.getParameter("pensionOption")!=null){
		pensionOption=request.getParameter("pensionOption").trim();
		}
		if(request.getParameter("dateOfBirth")!=null){
		dateOfBirth=request.getParameter("dateOfBirth");
		}
		if(request.getParameter("dateOfJoinning")!=null){
		dateOfJoinning=request.getParameter("dateOfJoinning");
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
<SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
	<script type="text/javascript">
	
function update(employeeCode,cpfaccno,employeeName,region,pensionno,designation){

if(document.forms[0].dateOfBirth.value==""||document.forms[0].dateOfBirth.value=="---"){
alert("Please Enter the DateOfBirth");
document.forms[0].dateOfBirth.focus();
return false;

}
if(document.forms[0].dateOfJoining.value==""||document.forms[0].dateOfJoining.value=="---"){
alert("Please Enter the DateOfJoining");
document.forms[0].dateOfJoining.focus();
return false;
}
if(!document.forms[0].dateOfBirth.value==""){
	   		    var date1=document.forms[0].dateOfBirth;
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].dateOfJoining.value==""){
	   		    var date1=document.forms[0].dateOfJoining;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }

	var url="";
	url="<%=basePath%>psearch?method=adjMappingUpdate&employeeCode="+employeeCode+"&cpfaccno="+cpfaccno+"&employeeName="
	+employeeName+"&region="+region+"&pensionno="+pensionno+"&designation="+designation+"";
	
	var pensionOption=document.forms[0].pensionOption.value;
	var dateOfBirth=document.forms[0].dateOfBirth.value;
	var dateOfJoining=document.forms[0].dateOfJoining.value;
	
		window.opener.submit_mapping_update(employeeCode,cpfaccno,employeeName,region,pensionno,designation,pensionOption,dateOfBirth,dateOfJoining);
		window.close();
	

}
function load(){
 document.forms[0].pensionOption.value ='<%=pensionOption%>';
 //alert(document.forms[0].pensionOption.value);
}

</script>

</head>
<body class="BodyBackground" onload="load();">
<form method="post">
<!--
action="<%=basePath%>psearch?method=financeDataSearch"

-->
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td></td>
	</tr>
	
	
	
	<tr>
		<td>&nbsp;</td>
	</tr>
	

	<tr>
	<tr>

		<td height="5%" colspan="2" align="center" class="ScreenHeading">
		Adj.ob Mapping Corrections</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>

		<table width="95%" align="center" cellpadding=1 class="tbborder"
			cellspacing="0" border="0">

			<tr>
			<td class="label">Employee No. :</td>
				<td><%=employeeCode%></td>
				<td class="label">CPFAcno :</td>
				<td align=left><%=cpfaccno%></td>
				</tr>
				<tr>
				<td class="label">Employee Name :</td>
				<td><%=employeeName%></td>
				<td class="label">Region :</td>
				<td align=left><%=region%></td>
				</tr>
				<tr>
				<td class="label">PfId :</td>
				<td><%=pensionno%></td>
				<td class="label">Designation :</td>
				<td align=left><%=designation%></td>
				</tr>
				
			
				<tr>
				<td class="label">DateOfBirth :</td>
				<td><input type="text" size="11" name="dateOfBirth" id="dateOfBirth" value='<%=dateOfBirth%>'>
				<a href="javascript:show_calendar('forms[0].dateOfBirth');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a></td>
				<td class="label">DateOfJoining :</td>
				<td align=left><input type="text" size="11" name="dateOfJoining" value='<%=dateOfJoinning%>'>
				<a href="javascript:show_calendar('forms[0].dateOfJoining');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a></td>
				
				</tr>
				<tr>
				<td class="label">pensionOption :</td>
				<td><SELECT NAME="pensionOption" style="width:40px">
					<option value="A">A</option>
					<option value="B">B</option>
					</select>
				</td>
				<td colspan=2>&nbsp;</td>
			
				</tr>

			 <tr>
			
				
			</tr>  
			<tr>
				<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;</td>

				<td><input type="button" class="btn" value="Update" class="btn"
					onclick="update('<%=employeeCode%>','<%=cpfaccno%>','<%=employeeName%>','<%=region%>','<%=pensionno%>','<%=designation%>');"> <input type="button" class="btn"
					value="Reset" onclick="javascript:document.forms[0].reset()"
					class="btn"> <input type="button" class="btn"
					value="Cancel" onclick="javascript:window.close();" class="btn">
				</td>
			</tr>

		</table>
		</td>
	</tr>
	</table>
	</form>
	</body>
	</html>