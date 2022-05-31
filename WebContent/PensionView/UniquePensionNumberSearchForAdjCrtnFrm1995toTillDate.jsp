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
// System.out.println("after month "+month +"after year "+year);
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
        var frm_year="1995";
        var claimsprocess=claimsprocess;
        var empserialNO = document.forms[0].empsrlNo.value;
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frmName=form7/8psadjcrtn";
	    var url='',formType='',empsrlNo='';
   formType = document.forms[0].formType.value;
    empsrlNo = document.forms[0].empsrlNo.value;
   if(empsrlNo==""){
   alert("Please Enter Employee Number");
   document.forms[0].empsrlNo.focus();
   return false;
   }
    //alert(formType);
   if(formType=="form7ps"){
    url="<%=basePath%>reportservlet?method=loadform7Input&frmName=form7/8psadjcrtn&employeeNo="+empsrlNo;
      
      }else  if(formType=="form8ps"){
    url="<%=basePath%>reportservlet?method=loadform8params&frmName=form7/8psadjcrtn&employeeNo="+empsrlNo;
   
   }else{
    url="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtnFrm1995toTillDate"+params;
		  
   }
	// alert(url); 
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
		
	}

	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
    }
	
     
     	function editEmpSerailNumber(cpfacno,employeeName,region,airportCode,empSerailNumber,dateofBirth,empCode){
       
    	if(document.forms[0].cpfno.length==undefined){
		if(document.forms[0].cpfno.checked){
		var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth+"&empCode="+empCode;
		alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
    	}
		
		}
		
		for (i = 0; i < document.forms[0].cpfno.length; i++){
		if(document.forms[0].cpfno[i].checked){
				var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
	}
	}
	}
   	
   	 function selectMultipule(){
   	 document.getElementById("check1").checked
     var x=document.getElementsByName("cpfno");
      for(var i=0;i<x.length;i++){
     if(document.getElementById("check1").checked==true)
     document.getElementsByName("cpfno")[i].checked=true;
     else  
     document.getElementsByName("cpfno")[i].checked=false;
     }
     
    //  alert("checkBoxes " +checkboxes);
	//	document.forms[0].action="<%=basePath%>search1?method=delete";
	//	document.forms[0].method="post";
	//	document.forms[0].submit();
	}
	
 
   function getlogs(empserialno,adjobyear){
   alert('Under Processing........');
  /* var url="";
  		url ="<%=basePath%>reportservlet?method=getadjemolumentslog&empserialno="+empserialno+"&adjobyear="+adjobyear;
		document.forms[0].action=url;		
		document.forms[0].method="post";
		document.forms[0].submit();*/
   }
   
     function loadDet(){
  
    var dataFlag='<%=dataFlag%>',empNo='<%=empsrlNo%>';
   // alert("--flag--"+dataFlag+"-empNo--"+empNo);
    if(dataFlag=='NoData' && empNo!=''){
     document.getElementById("norec").style.display="block";
    }else{
     document.getElementById("norec").style.display="none";
    }
   } 
   function resetVals(){
	document.forms[0].empName.value="";
	document.forms[0].empsrlNo.value="";
	document.forms[0].dob.value="";
	document.forms[0].doj.value="";
	     
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
						 	Revised Form7/Form8 With Monthly Data Corrections
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
							<tr>
								<td class="label" align="right">
									Form3-2007-Sep Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)"/>
								</td>
								<td class="label" align="right">
									Form3-2007-Sep- PFID:
								</td>
								<td>
									<input type="text" name="empsrlNo" value="<%=empsrlNo%>"/>
								</td>
							</tr>
							<tr>
							<td class="label" align="right">
									Date of Birth:
								</td>
								<td>
								<input type="text" name="dob" onkeyup="return limitlength(this, 20)"/>
								</td>
							
								<td class="label" align="right">
									Date of Joining
								</td>
								<td>
									<input type="text" name="doj" onkeyup="return limitlength(this, 20)"/>
								</td>
							</tr>
							
							
							<tr>
							<td class="label" align="right">	Form 7/8 PS Report:
							 </td>
							 <td >
							 <select name="formType"  style="width: 130px">
							 <option value="NO-SELECT">Select One</option>
							 <option value="form7ps">FORM 7 PS</option>
							 <option value="form8ps">FORM 8 PS</option>		              					 
							 </select>
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
					 
				
				 <table align="center" id="norec">
							<tr>
							 
							<td><b> No Records Found </b></td>
							</tr>
                         </table> 

			 
     


		</form>
	</body>
</html>
