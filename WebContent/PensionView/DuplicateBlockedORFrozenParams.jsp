
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String  message="",userType="";
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//String currentDate=request.getAttribute("currentDate").toString();
 if(request.getAttribute("message")!=null){
	//message=(String)request.getAttribute("message");
  }
  if(session.getAttribute("usertype")!=null){
	//userType = (String)session.getAttribute("usertype");
	
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />    	 
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		 
	<script type="text/javascript">

	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
	}
	function testSS(){ 
	
		if(document.forms[0].fromdate.value=="" && document.forms[0].todate.value!=""){
		alert("Please Enter the FromDate");
		document.forms[0].fromdate.focus();
		return false;
		
		}
		if(document.forms[0].fromdate.value!="" && document.forms[0].todate.value==""){
		alert("Please Enter the ToDate");
		document.forms[0].todate.focus();
		return false;
		}
		if(!document.forms[0].fromdate.value==""){
	   		    var date1=document.forms[0].fromdate;
	   		   var val1=convert_date(date1);
				 if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].todate.value==""){
	   		    var date1=document.forms[0].todate;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
	   		    
	   	     
    
    	var reportType='',url='',fromDate='',toDate='';
    	fromDate =document.forms[0].fromdate.value; 
    	toDate =document.forms[0].todate.value; 
    	if(fromDate!="" && toDate!=""){ 
    	var result = compareDates(fromDate, toDate);		 
			 if(result=="larger")
			{ 	alert(" To Date should  be greater than or equal to From Date ");
				document.forms[0].fromdate.focus();
				return false;
			}	 	 
			}
  		reportType=document.forms[0].reportType.value;
  		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
     	url="<%=basePath%>reportservlet?method=DuplicateBlockedorFrozenReport&fromDate="+fromDate+"&toDate="+toDate+"&reportType="+reportType;
    	
    	 //alert(url);
       	if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
      	}
	
	 function resetVals(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadBlockedorFrozenInput";
		document.forms[0].method="post";
		document.forms[0].submit();   
	}
	
  function disableEnterKey(e)
{
     var key;      
     if(window.event)
          key = window.event.keyCode; //IE
     else
          key = e.which; //firefox      

     return (key != 13);
}
    </script> 
  </head>
  
 
  <body class="BodyBackground"  onload="javascript:loadDet();" onkeypress="return disableKeyPress(event)">
		<form name="dbfaReport" action="" >
			 
					
				 
				
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>


						<jsp:include page="/PensionView/PensionMenu.jsp" />


					</td>
				</tr>

				<tr>
					<td>
						&nbsp;
					</td>

				</tr>
			</table>
				<table width="70%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="4" align="center" class="ScreenMasterHeading">
						 	DuplicateBlocked/FrozenAdj PFIDs Report Params
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				 
							<tr>
								
								<td class="label" align="right"> 
									From Date:<font color=red></font>
								</td>
								<td>&nbsp;
									<input  type="text" size="11" name="fromdate" id="fromdate" value="" /> 
								<a href="javascript:show_calendar('forms[0].fromdate');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a></td>
							</tr>
							<tr>
								
								<td class="label" align="right"> 
									To Date:<font color=red></font>
								</td>
								<td>&nbsp;
									<input type="text" size="11" name="todate" id="todate" value="" /> 
								<a href="javascript:show_calendar('forms[0].todate');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a></td>
		
							</tr>
							<tr>
								
								<td class="label" align="right"> 
									Report Type:<font color=red></font>
								</td>
								<td>&nbsp;&nbsp;<SELECT NAME="reportType" style="width: 88px">
									<option value="html">Html</option>
									<option value="ExcelSheet">Excel Sheet</option>
									</SELECT>
								</td>

							</tr>
	
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="center" colspan="5">
									<input type="button" class="btn" value="Submit"   name="Submit" onclick="javascript:testSS();"/>
									<input type="button" class="btn" value="Reset"  name="Reset"  onclick="javascript:resetVals();" />
									<input type="button" class="btn" value="Cancel" name="Cancel"   onclick="javascript:history.back(-1);"/>
								</td>

							</tr>
						 
					</table>
					
  </body>
</html>
