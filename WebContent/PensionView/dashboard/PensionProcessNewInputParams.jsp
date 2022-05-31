
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String frmName="",message="", loginUsrRegion="";
	if(request.getAttribute("frmName") != null){
		frmName=(String)request.getAttribute("frmName");
	} 
	 
	if(session.getAttribute("loginUsrRegion")!=null){
		loginUsrRegion = (String) session.getAttribute("loginUsrRegion");	
	}
			
	String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();
	String msg ="";
	Set keys = hashmap.keySet();	

	Iterator it = keys.iterator();
	if(request.getAttribute("searchInfo")!=null) {
		EmployeePersonalInfo beanInfo = (EmployeePersonalInfo)request.getAttribute("searchInfo");
		request.setAttribute("SetSearchBean",beanInfo);
		
	}
	
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>AAI</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
   	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">	
    <SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
	<script type="text/javascript"> 		
		function getNodeValue(obj,tag){
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	   	}
	   	function createXMLHttpRequest(){
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		 	}else if (window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
			 }
		}
		function test(cpfaccno,region,pensionNumber,employeeName,desig,dateofbirth,dateofJoining,airportCode,fathername,employeeCode,gender,pensionOption){	
		
			document.forms[0].empName.value=employeeName;
			document.forms[0].pensionNo.value=pensionNumber;
			document.forms[0].empDesegnation.value=desig;
			document.forms[0].cpfaccno.value=cpfaccno;
			document.forms[0].dob.value=dateofbirth;
			document.forms[0].doj.value=dateofJoining;
			document.forms[0].airportcode.value=airportCode;
			document.forms[0].regionname.value=region;
			document.forms[0].fhname.value=fathername;
			document.forms[0].employeeCode.value=employeeCode;
			document.forms[0].gender.value=gender;
			document.forms[0].pensionOption.value=pensionOption;
				  
		}
		
		function popupWindow(mylink,windowname)
			{
			document.getElementById("process").style.display='none';
			if (! window.focus)return true;
			var href;
			if (typeof(mylink) == 'string')
			   href=mylink;
			else
			href=mylink.href;
			progress=window.open(href, windowname, 'width=750,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
			
			return true;
			}
		function frmload(){
	 		process.style.display="none";
	 		populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
		}
		function resetPage(){
	     	document.forms[0].action="<%=basePath%>psearch?method=loadPensionProcessForm&frmName=PensionProcessNewForm";
			document.forms[0].method="post";
			document.forms[0].submit();
				
		}		
		function validate(){
			var todayDate=getCurrentDate();
			var ex=/^[0-9]+$/;
			var mail=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			var emailid=document.getElementById('emailid').value;
			var mobileno=document.getElementById('mblno').value;
			var pensionNo=document.forms[0].pensionNo.value;
			var chqDate=document.forms[0].docchq.value;
			if(pensionNo==""){
				alert("Please select the PFID using search Icon");
				document.forms[0].searchbut.focus();				
				return false;
			}	
			if(document.getElementById('pensiontype').value=="NO-SELECT"){
				alert("Please select PensionType");
				document.forms[0].pensiontype.focus();
				return false;
			}				
			if(chqDate==""){
				alert("Please Enter Date Of Case Sent to CHQ");
				document.forms[0].docchq.focus();
				return false;
			}	
			if( !chqDate=="") {			
				var val1=convert_date(document.forms[0].docchq);
	   		       if(val1==false){
	   		       	return false;
	   		   	}	
			}	
			var result=compareDates(chqDate,todayDate);
			if(result=='larger') {
				alert("Invalid Date Of Case Sent to CHQ..");
				document.forms[0].docchq.select();
				return false;
			}	
			if(mobileno==""){
				alert("Please Enter Mobile Number");
				document.forms[0].mblno.focus();
				return false;
			}
			if(!ex.test(mobileno)){
				alert("Phoneno should be numeric");
				document.forms[0].mblno.select();
				return false;
			}
			if(mobileno.length<10) {				
				alert("Pl Enter 10 digit mobile Number");
				document.forms[0].mblno.select();
				return false;
			}			
			if(emailid==""){
				alert(" Please Enter Email Id");
				document.forms[0].emailid.focus();
				return false;
			}
			if(!mail.test(emailid)){
				alert(" Invalid Email Id");
				document.forms[0].emailid.select();
				return false;
			}			   		   
			
			var employeeName=document.forms[0].empName.value;
			var pensionNumber=document.forms[0].pensionNo.value;    
			var desig=document.forms[0].empDesegnation.value;
			var cpfaccno=document.forms[0].cpfaccno.value;
			var dateofbirth=document.forms[0].dob.value;	
			var dateofJoining=document.forms[0].doj.value;
			var airportCode=document.forms[0].airportcode.value;
			var region=document.forms[0].regionname.value;
			var fathername=document.forms[0].fhname.value;
			var employeeCode=document.forms[0].employeeCode.value;
			var gender=	document.forms[0].gender.value;
			var pensionOption=document.forms[0].pensionOption.value;
			var pensiontype=document.forms[0].pensiontype.value
			var docchq=document.forms[0].docchq.value;
			var mblno=document.forms[0].mblno.value;
			var emailid=document.forms[0].emailid.value;
			var remarks=document.forms[0].remarks.value;
			var r = confirm("Are you sure to save these details");
			if(r==true){
				var url="<%=basePath%>reportservlet?method=editPensionInfo&employeeName="+employeeName+"&pensionNumber="+pensionNumber+"&desig="+desig +"&cpfaccno="+cpfaccno+"&dateofbirth="+dateofbirth+"&dateofJoining="+dateofJoining+"&airportCode="+airportCode+"&region="+region+"&fathername="+fathername+"&employeeCode="+employeeCode+"&gender="+gender+"&pensionOption="+pensionOption+"&pensiontype="+pensiontype+"&docchq="+docchq+"&mblno="+mblno+"&remarks="+remarks+"&emailid="+emailid;
				//alert(url);
				document.forms[0].action=url;
		  	  	document.forms[0].method="post";
				document.forms[0].submit();
			}else{
				return 	false;
			}			
	  	}
	  	function numsDotOnly() {
		    if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46) ||(event.keyCode>=44 && event.keyCode<47)) {
        	    event.keyCode=event.keyCode;
        	}
	        else {       
				event.keyCode=0;
        	}
  		}
			
	</script>
	</head>
   
<body onload="javascript:frmload()">
   <form>
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
					<tr>
		<td>
		
		</td>
	</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		 
		<tr>
			<td height="15%">
			
				<table  width="70%" border="0" align="center" class="tbborder">
				
				<tr class="tbheader">
				<td  colspan="4" align="center" class="tblabel">Pension Process Form[New]</td>
			</tr>
			<tr style="display:inline" align="center">
			<%if(request.getAttribute("msg")!=null){
			msg=(String) request.getAttribute("msg");

			 %>	
		<td align="center" ><font color=red size="2"><%=msg%>.....</font></td>
		<%}%>

		<td align="center" ></td>

		</tr>
			<tr ><td style="hight:10px" colspan="4">&nbsp;</td></tr>
			<tr>
						<td class="label" align="left">
									Pensionno:<font color=red>*</font>&nbsp;
								</td>
								<td>
									<input type="text" name="pensionNo" readonly="true" tabindex="1">
									<a href="#" onClick="popupWindow('<%=basePath%>PensionView/dashboard/ProcessInfo.jsp','AAI');hidedetailsDiv();"><img src="<%=basePath%>/PensionView/images/search1.gif" border="0" name="searchbut" alt="Click The Icon to Select Personal Data Mapped Records"/></a>
									
									
								</td>
						<td class="label">Old EmployeeNo:</td>
						<td><input type="text" name="oldempno" readonly="readonly" onkeyup="return limitlength(this, 20)">
					 
						</td>
						
					</tr>
					<tr>
						<td class="label">Region:</td>
						<td><input type="text" name="regionname" readonly="readonly" onkeyup="return limitlength(this, 20)">
						
						<td class="label">Airport Code:</td>
						<td><input type="text" name="airportcode" readonly="readonly" onkeyup="return limitlength(this, 20)">
						
					</tr>
					<tr>
						<td class="label">Employee Name:
						</td>
						<td><input type="text" name="empName" readonly="readonly" onkeyup="return limitlength(this, 20)">
						</td>
						<td class="label">Designation:</td>
						<td><input type="text" name="empDesegnation" readonly="readonly" onkeyup="return limitlength(this, 20)">
					 
						</td>
						
					</tr>
                    <tr>	<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="employeeCode" readonly="readonly" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									CPFAC No:
								</td>
								<td>
									<input type="text" name="cpfaccno" readonly="readonly">
								</td>
							</tr>
							 
					<tr>
					 <td class="label">Date Of Birth:</td>
						<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->
                    
					<td><input type="text" name="dob" tabindex="9" readonly="readonly" onkeyup="return limitlength(this, 20)">
										
										</td>

                   <td class="label">
									Date of Joining:</td>
				<td>
									
								    <input type="text" name="doj" tabindex="9" readonly="readonly"  onkeyup="return limitlength(this, 20)">
										
				</td>

					</tr>
							<tr>	
								<td class="label">
							 		Father's / Husband's Name:
									
								</td>
								<td>
									<input type="text" name="fhname" readonly="readonly" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									Pension Option:
								</td>
								<td>
									<input type="text" name="pensionOption" readonly="readonly" onkeyup="return limitlength(this, 20)">
								</td>
							</tr>
							<tr>
						<td class="label">Gender:</td>
						<td>
									<input type="text" name="gender" readonly="readonly" onkeyup="return limitlength(this, 20)">
								</td>
						<td class="label">Pension Type:<font color="red">*</font></td>
						<td ><select name="pensiontype" tabindex="7" style="width:130px">
						<option value='NO-SELECT'>[Select One]</option>
								<option value='S'>Self Pension</option>
								<option value='F'>Family Pension</option>
								<option value='O'>Orphan Pension</option>
							</select>
							
						</td>
						</tr>
						<tr>
						<td class="label">Date Of Case Sent to CHQ:<font color="red">*</font></td>
						<td><input type="text" name="docchq" readonly="readonly" onkeyup="return limitlength(this, 20)">
						<a href="javascript:show_calendar('forms[0].docchq');"><img src="<%=basePath%>PensionView/images/calendar.gif"  alt="Calender" border="no" /></a>
						</td>
						<td class="label">Mobile No:<font color="red">*</font></td>
						<td><input type="text" name="mblno" maxlength="10" onkeypress="numsDotOnly()" /> </td>
					</tr>
					<tr><td class="label">Email Id:<font color="red">*</font></td>
						<td><input type="text" name="emailid" onkeyup="return limitlength(this, 20)" style="width:130px">
					 <td class="label">	Remarks:</td>
						<td colspan="3">
						<TEXTAREA NAME="remarks"  maxlength="75"  cols="30" tabindex="50"></TEXTAREA></td>
					 
						</tr>
						<tr>
						</tr>

					
					<tr>
						<td align="left">&nbsp;</td>
						<td>
								<input type="button" class="btn" value="Save" class="btn" onclick="validate();">
								<input type="button" class="btn" value=Reset onclick="javascript:resetPage();" class="btn">
								<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
						</td>

					</tr>
			</table>
			<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>
		<tr>
			<td>&nbsp;</td>
		</tr>
		</table>
	
	</form>
		
  </body>
</html>