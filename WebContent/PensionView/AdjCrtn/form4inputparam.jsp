<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.AaiEpfForm11Bean" %>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
Calendar cal = Calendar.getInstance(); 
 		 int month = cal.get(Calendar.MONTH)+7;
 		 System.out.println(month);    
		 int year=cal.get(Calendar.YEAR);
		 System.out.println("month "+month +"year "+year);
		 if(month>=12){
			  month=month-12;
			  year = cal.get(Calendar.YEAR)+1; 
		 }
 System.out.println("after month "+month +"after year "+year);
 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String region="",empsrlNo="",dataFlag="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
	session.removeAttribute("getSearchBean1");
	
	}
if(request.getAttribute("empsrlNo")!=null){
	empsrlNo = (String)request.getAttribute("empsrlNo");
	}
	if(request.getAttribute("dataFlag")!=null){
	dataFlag = (String)request.getAttribute("dataFlag");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
	<script type="text/javascript">
	
	
	function testSS() {
		var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var empserialNO	=empserialNO;
		//reportType="ExcelSheet";
		reportType="Html";
		yearID="NO-SELECT";
		var page="PensionContributionScreen";
		var mappingFlag="true";
       // var frm_year="1995";
        var claimsprocess=claimsprocess;
        var empserialNO = document.forms[0].empsrlNo.value;
		//var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frmName=form7/8psadjcrtn";
	    var url='',formType='',empsrlNo='';
    formType = document.forms[0].formType.value;
    empsrlNo = document.forms[0].empsrlNo.value;
    reportType= document.forms[0].select_reportType.value;
   // alert(empsrlNo+formType+reportType);
   if(empsrlNo==""){
   alert("Please Enter Employee Number");
   document.forms[0].empsrlNo.focus();
   return false;
   } 
   if(formType=='NO-SELECT'){
	alert('Please Select the FormType Type');
	document.forms[0].formType.focus();
	return false;
		}   
   if(reportType=='NO-SELECT'){
	alert('Please Select the Report Type');
	document.forms[0].select_reportType.focus();
	return false;
   }
 
    url="<%=basePath%>reportservlet?method=SumofSuppPCReport&frm_Name="+formType+"&employeeNo="+empsrlNo+"&reportType="+reportType;
     
	//alert(url); 
   		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		winOpened = true;
		wind1.window.focus();
		
	}
	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
    }
   function resetVals(){
	document.forms[0].empsrlNo.value="";
	document.forms[0].formType.value="NO-SELECT";
	document.forms[0].select_reportType.value="NO-SELECT";
	     
	}
</script>
	</head>

	<body class="BodyBackground" onload="javascript:loadDet();">
		<form name="test" action="" >
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
   		<%boolean flag = false;%>
				 
						<table width="65%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="5" align="center" class="ScreenMasterHeading">
						 	Form4 Input Parameters
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
							<tr>
								  
								<td class="label" align="right">
									 PFID:
								 
									<input type="text" name="empsrlNo" value="<%=empsrlNo%>"/>
								</td>
							 
							<td class="label" align="center"> Report: 
							 <select name="formType"  style="width: 130px">
							 <option value="SumofSuppPCReport">Summary of Supplementary PC Report </option>	        					 
							 </select>
								</td>
					<td class=label align="right" nowrap>
						Report Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<SELECT NAME="select_reportType" style="width:88px" onChange="javascript:getAirports('data')">
							<option value="html">
								Html
							</option>
						</SELECT>
					</td>
				</tr>
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="center" colspan="5">
								 
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();"/>
									<input type="button" class="btn" value="Reset" onclick="javascript:resetVals();" class="btn"/>
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
								</td>

							</tr>
						</table>
		</form>
	</body>
</html>
