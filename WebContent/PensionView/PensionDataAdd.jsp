<%@ page language="java"%>
<%@ page  import="java.util.*,aims.common.*"%>

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
		<script type="text/javascript"> 
    function checkUniCharacter(e){ 
	var nChar=e.charCode? e.charCode : e.keyCode;
	alert(nChar);
	if (nChar>=2309 && nChar<=2799){
	 alert('valid Character entry');
	}else{
	 alert(nChar);
	 return false;
	} 
	}
	
	function limitlength(obj, length){
	var maxlength=length
	if (obj.value.length>maxlength)
	obj.value=obj.value.substring(0, maxlength)
	}
	function checknumdotonly()
         {
	          if(((event.keyCode>=48)&&(event.keyCode<=57))||event.keyCode==46)
	               {
		           event.keyCode=event.keyCode;
	               }
	          else
	               {
		           event.keyCode=0;
	               }
         }
	
function checknum()
         {
	          if((event.keyCode>=48)&&(event.keyCode<=57))
	               {
		           event.keyCode=event.keyCode;
	               }
	          else
	               {
		           event.keyCode=0;
	               }
         }
         
         function testSS(){
          if(document.forms[0].airPortCD.value=="")
   		 {
   		  alert("Please Enter Airportcode");
   		  document.forms[0].airPortCD.focus();
   		  return false;
   		  }
   		 if(document.forms[0].empName.value=="")
   		 {
   		  alert("Please Enter Employee Name");
   		   document.forms[0].empName.focus();
   		  return false;
   		  }
       
    	document.forms[0].action="<%=basePath%>PensionView/PensionAddSucces.jsp"
		document.forms[0].method="post";
		document.forms[0].submit();
   		 }
</script>
	</head>

	<body class="BodyBackground" onload="document.forms[0].airPortCD.focus();">
	<form method="post" >
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
					<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>

				<tr>
					<td>&nbsp;</td>
				</tr>
		
				<tr>
				<td>
				<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">Pension Information[Add]	</td>

				</tr>
					<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td height="15%">
						<table align="center">
							<tr>
								 <td class="label">
									Airport Code:
								</td>
								<td>
									<input type="text" name="airPortCD" onkeyup="return limitlength(this, 20)">
								</td>
								 <td class="label">
									Emplyee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
								</td>
							</tr>
							<tr>
								 <td class="label">
									Salary:
								</td>
								<td>
									<input type="text" name="salary" onkeypress="checknum()" onkeyup="return limitlength(this, 20)">
								</td>
								 <td class="label">
									Pension Fund:
								</td>
								<td>
									<input type="text" name="pensionFund" value="8.33" onkeypress="checknumdotonly()" onkeyup="return limitlength(this, 20)">
									%
								</td>
							</tr>

							<tr>
								 <td class="label">
									From Date:
								</td>
								<td>
									<input type="text" name="fromDt"  >
									&nbsp; <a href="javascript:show_calendar('forms[0].fromDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>

								 <td class="label">
									To Date:
								</td>
								<td>
									<input type="text" name="toDt" onkeyup="return limitlength(this, 25)" >
									<a href="javascript:show_calendar('forms[0].toDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								</tr>
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="center">
									<input type="button" class="btn" value="Add" class="btn" onclick="testSS()">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>
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
