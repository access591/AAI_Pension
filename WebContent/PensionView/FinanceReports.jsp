<%@ page language="java"%>
<%@ page  import="java.util.*,aims.bean.EmployeeValidateInfo,aims.common.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@page errorPage="error.jsp"%>
<%

String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    
	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
			<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
 	<SCRIPT type="text/javascript">
 	function reports(){
 	
 	var reportID,reportIndex,path,fromDate="",toDate="",cpfaccno="",employeeno="",region="";
 	reportID=document.forms[0].select_Report.options[document.forms[0].select_Report.selectedIndex].value;
 	//alert(reportID);
 	if(reportID=='1'){
 	 var fromDtVal=convert_date(document.forms[0].frmFromDate);
 	 var toDtVal=convert_date(document.forms[0].frmToDate);
   		if(fromDtVal==false)
   		     {
   		      return false;
   		     }
   		   	if(toDtVal==false)
   		     {
   		      return false;
   		     }
 	fromDate=document.forms[0].frmFromDate.value;
 	toDate=document.forms[0].frmToDate.value;
 	path="<%=basePath%>PensionView/FinanceFundReport.jsp?frm_fromDate="+fromDate+"&frm_toDate="+toDate;
 	//alert(path);
	window.open (path,"mywindow","location=1,status=1,toolbar=1,width=700,height=450,channelmode=yes,resizable=yes");
 	}else if(reportID=='2'){
 	path="<%=basePath%>PensionView/FinanceFundReportBySearch.jsp";
 	//alert(path);
	window.open (path,"mywindow","location=1,status=1,toolbar=1,width=700,height=450,channelmode=yes,resizable=yes");
 	}else if(reportID=='3'){
 	cpfaccno=document.forms[0].frmCpfaccno.value;
 	employeeno=document.forms[0].frmEmployeeno.value;
 	
 	if(cpfaccno=='' && employeeno==''){
 		alert('CPF Acc.no or Employee no should be enter');
 		ocument.forms[0].frmCpfaccno.focus();
 		return false;
 	}else{
 		path="<%=basePath%>PensionView/FinanceFundReportByEachEmployee.jsp?frm_cpfaccno="+cpfaccno+"&frm_empno="+employeeno;
 	
		window.open (path,"mywindow","status=1,toolbar=1,resizable=yes");
 		
 	}
 	
 	}else if(reportID=='4'){
 		region=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
 		//alert(region);
 		path="<%=basePath%>PensionView/FinanceFundReportByRegion.jsp?frm_region="+region;
 		window.open (path,"mywindow","width=800,height=600,menubar=yes,toolbar=yes,scrollbars=yes");
 	}else if(reportID=='5'){
 		region=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
 		path="<%=basePath%>validatefinance?method=missingMonthsReport&frm_region="+region;
 	//	path="<%=basePath%>PensionView/FinanceReportMissingMonths.jsp?frm_region="+region;
 	
 		window.open (path,"mywindow","width=800,height=600,menubar=yes,toolbar=yes,scrollbars=yes");
 	}
 	 
 	}
	function get_pensioninfo(){
    	var reportID,reportIndex,path;
		reportIndex=document.forms[0].select_Report.selectedIndex;
		//reportIndex=document.forms[0].select_Report.value;
		//alert("reportIndex " +reportIndex);
		reportID=document.forms[0].select_Report.options[document.forms[0].select_Report.selectedIndex].value;
		if(reportID=='1'){
		
		document.getElementById("fecilities").style.display="block";
		document.getElementById("fecilities1").style.display="block";
		document.getElementById("fecilities2").style.display="block";
		document.getElementById("fecilities3").style.display="block";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		document.getElementById("fecilities8").style.display="none";
		document.getElementById("fecilities9").style.display="none";
		}else if(reportID=='2'){
		document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		}else if(reportID=='3'){
		document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="block";
		document.getElementById("fecilities5").style.display="block";
		document.getElementById("fecilities6").style.display="block";
		document.getElementById("fecilities7").style.display="block";
		document.getElementById("fecilities8").style.display="none";
		document.getElementById("fecilities9").style.display="none";
		}else if(reportID=='4'){
		document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		document.getElementById("fecilities8").style.display="block";
		document.getElementById("fecilities9").style.display="block";
		}else if(reportID=='5'){
		document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		document.getElementById("fecilities8").style.display="block";
		document.getElementById("fecilities9").style.display="block";
		}else{
		document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		document.getElementById("fecilities8").style.display="none";
		document.getElementById("fecilities9").style.display="none";
		}
		
	
	
    }
    
    function loadScreen(){
    	document.forms[0].select_Report.focus();
    	document.getElementById("fecilities").style.display="none";
		document.getElementById("fecilities1").style.display="none";
		document.getElementById("fecilities2").style.display="none";
		document.getElementById("fecilities3").style.display="none";
		document.getElementById("fecilities4").style.display="none";
		document.getElementById("fecilities5").style.display="none";
		document.getElementById("fecilities6").style.display="none";
		document.getElementById("fecilities7").style.display="none";
		document.getElementById("fecilities8").style.display="none";
		document.getElementById("fecilities9").style.display="none";
    }
		function resetValidate(){
			document.pensionvaledit.pfDeduction.value="";
			document.pensionvaledit.pfStatury.value="";
		
		}
		function closeValidate(){
			window.close();
		}
 	</SCRIPT>
  </head>
  <%
   	String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
  %>
  <body class="BodyBackground" onload="javascript:loadScreen()">
   <form name="pensionvaledit" method="post">
   <table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	   			<tr>
		  				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  			</tr>
		  	
		 <tr>
				<td>
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">	Reports </td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>

   		<tr>
		  <td>
		  		<table height="10%" align="center" width="100%"> 
		  			<tr>
		  				<td class="label">Select Report</td>
		  				<td ><select name="select_Report" onChange="javascript:get_pensioninfo()">
  								 <Option Value='' Selected>Select One</Option>
  								 <Option Value='1' >Finance Data By Dates</Option>
  								  <Option Value='2' >Finance Data By Search</Option>
  								  <Option Value='3' >Finance Data By Each Employee</Option>
  								   <Option Value='4' >All Employee Information By Region Wise</Option>
  								   <Option Value='5' >All Employees Missing Information By Region Wise</Option>
		  				</td>	
		  			</tr>
		  			<tr> 
							<td id="fecilities">From Date</td>
							<td id="fecilities1"><input name="frmFromDate" type="text"> </td>
				    </tr>
				    <tr>
							<td id="fecilities2">To Date</td>
							<td id="fecilities3"><input name="frmToDate" type="text"> </td>
					</tr>		 						
							
		  			<tr> 
							<td id="fecilities4">CPF ACC.NO</td>
							<td id="fecilities5"><input name="frmCpfaccno" type="text"> </td>
				    </tr>
				    <tr>
							<td id="fecilities6">Employee No</td>
							<td id="fecilities7"><input name="frmEmployeeno" type="text"> </td>
					</tr>	
					    <tr>
							<td id="fecilities8">Region</td>
							<td id="fecilities9">									
							<SELECT NAME="select_region" style="width:130px">
							<%
							int j=0;
                            while(it.hasNext()){
							  region=hashmap.get(it.next()).toString();
							  j++;
							 %>
							  <option ="<%=region%>" ><%=region%></option>
	                       <% }
							%>
							</SELECT>
						</td>
					</tr>	
					
				  	<tr>
				  	<td colspan="5" align="center">
				  	<table width="100%">
				  	<tr>
				  
				  	<td align="center"><input type="button" class="btn" name="btnUpdate" value="Submit" onclick="javascript:reports()">
				  	<input type="button" class="btn" name="btnReset" value="Reset" onclick="javascript:resetValidate()">
				  	<input type="button" class="btn" name="btnCancel" value="Cancel" onclick="javascript:closeValidate()"></td>
					</tr>		  				
		  			</table>
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
