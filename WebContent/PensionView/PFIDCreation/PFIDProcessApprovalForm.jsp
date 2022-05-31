
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>
<%@ page import="java.util.ArrayList" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String frmName="",message="", loginUsrRegion="";
	if(request.getAttribute("frmName") != null){
	frmName=(String)request.getAttribute("frmName");
	} 
	if(request.getAttribute("message") != null){
	message=(String)request.getAttribute("message");
	} 
	
	if(session.getAttribute("loginUsrRegion")!=null){
				loginUsrRegion = (String) session.getAttribute("loginUsrRegion");	
	}
		
		System.out.println("===frmName====="+frmName);
	String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	System.out.println(".............keys................"+keys);

	Iterator it = keys.iterator();
	
	
String employeeName="",employeeNo="",cpfaccNo="",dateOfBirth="",region="",airportcode="",fhName="",gender="",designation="",dateOfJoining="",whetherOption="",processId="",remarks="";
   if(request.getAttribute("empList")!=null){
   ArrayList empList = new ArrayList();
   EmployeePersonalInfo  empInfo = new EmployeePersonalInfo();
   empList = (ArrayList)request.getAttribute("empList");
   for(int i=0;i<empList.size();i++){
   empInfo = (EmployeePersonalInfo)empList.get(i);
   employeeName=empInfo.getEmployeeName();
   employeeNo=empInfo.getEmployeeNumber();
   cpfaccNo=empInfo.getCpfAccno();
   region=empInfo.getRegion();
   airportcode = empInfo.getAirportCode();
   fhName=empInfo.getFhName();
   gender=empInfo.getGender();
   whetherOption=empInfo.getWetherOption().trim();    
   processId=empInfo.getProcessID();
   dateOfBirth= empInfo.getDateOfBirth();
   dateOfJoining = empInfo.getDateOfJoining();
   designation = empInfo.getDesignation();
   remarks = empInfo.getRemarks();
    
   }
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
	 
	function frmload(){
 		process.style.display="none";
 		 document.forms[0].employeeCode.value='<%=employeeNo%>';
		 document.forms[0].cpfaccno.value = '<%=cpfaccNo%>';
		 document.forms[0].empName.value = '<%=employeeName%>';
   		 document.forms[0].empDesegnation.value = '<%=designation%>';
   		 document.forms[0].remarks.value = '<%=remarks%>';
 		 document.forms[0].gender.value= '<%=gender%>';
 		 document.forms[0].gender.selected= true;
		 document.forms[0].fhname.value = '<%=fhName%>';
		 document.forms[0].pensionOption.value = '<%=whetherOption%>';		 
		 document.forms[0].pensionOption.selected= true;
		 document.forms[0].processId.value = '<%=processId%>';
 		 document.forms[0].dob.value = '<%=dateOfBirth%>';
 		 document.forms[0].doj.value = '<%=dateOfJoining%>';
 		 document.forms[0].select_region.value = '<%=region%>';
 		 document.forms[0].select_airport.value = '<%=airportcode%>';
 		
 		populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
	}
	function resetParams(processid){	 
     				document.forms[0].action="<%=basePath%>psearch?method=editPFIDProcess&processid="+processid+"&frmName="+'<%=frmName%>';
					document.forms[0].method="post";
					document.forms[0].submit();
			
		}
		
	function validate(){

		process.style.display="none";
   		 var empName="",airportID="",desegnation="",day="",month="",year="",regionID="",gender="",option="",fhname="",remarks="",processid="";
	
		
   		var dob=document.forms[0].dob.value;
		var doj=document.forms[0].doj.value;
		 if(!document.forms[0].dob.value==""){
	   		    var date1=document.forms[0].dob;
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].doj.value==""){
	   		    var date1=document.forms[0].doj;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 var employeeNo=document.forms[0].employeeCode.value;
		 var cpfaccno=document.forms[0].cpfaccno.value;
		 empName=document.forms[0].empName.value;
   		desegnation=document.forms[0].empDesegnation.value;
   		remarks=document.forms[0].remarks.value;
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
			
		}
		if(document.forms[0].select_airport.selectedIndex>0){
			airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
		}else{
			airportID=document.forms[0].select_airport.value;
			
		}
		
		if(regionID=='NO-SELECT'){
		alert("Please select Region");
		document.forms[0].select_region.focus();
		return false;
		}
		if(airportID=='NO-SELECT'){
		alert("Please select Station");
		document.forms[0].select_airport.focus();
		return false;
		}
		if(document.forms[0].empName.value==''){
		alert("Please Enter Employee Name");
		document.forms[0].empName.focus();
		return false;
		}
		if(document.forms[0].dob.value==''){
		alert("Please Select Employee Date Of Birth");
		document.forms[0].dob.focus();
		return false;
		}
		if(document.forms[0].doj.value==''){
		alert("Please Select Employee Date Of Joining");
		document.forms[0].doj.focus();
		return false;
		}
		if(document.forms[0].gender.value==''){
		alert("Please Select Employee Gende");
		document.forms[0].gender.focus();
		return false;
		}
		if(document.forms[0].pensionOption.value==''){
		alert("Please Select Employee Pension Option");
		document.forms[0].pensionOption.focus();
		return false;
		}
		
		var dob=document.forms[0].dob.value;
		var doj=document.forms[0].doj.value;
		 if(!document.forms[0].dob.value==""){
	   		    var date1=document.forms[0].dob;	   		    
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].doj.value==""){
	   		    var date1=document.forms[0].doj;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
	   		    
		gender=document.forms[0].gender.value;
		fhname=document.forms[0].fhname.value;
		option=document.forms[0].pensionOption.value;
		processid=document.forms[0].processId.value; 
			 
		url="<%=basePath%>psearch?method=updatePFIDProcess&region="+regionID+"&airPortCode="+airportID+"&empName="+empName+"&frm_dateOfBirth="+dob+"&doj="+doj+"&cpfaccno="+cpfaccno+"&employeeNo="+employeeNo+"&gender="+gender+"&fhname="+fhname+"&option="+option+"&desegnation="+desegnation+"&remarks="+remarks+"&processid="+processid+"&frmName="+'<%=frmName%>';
		document.forms[0].action=url;
	  	document.forms[0].method="post";
		document.forms[0].submit();
				 
		 
		}
		 
	</script>
	</head>
    
<body onload="javascript:frmload()">
   <form name="personalMaster" action="" method="post">
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
				<td  colspan="4" align="center" class="tblabel">  
					PFID Approval Form 
			 </td>
			</tr>
<!--			<tr ><td style="hight:10px" colspan="4">&nbsp;</td></tr>-->
					<tr>
						<td class="label">Region:<font color="red">*</font></td>
						<td>
						<input type="text" name="select_region"   readonly="readonly">
						</td>
						<td class="label">Airport Code:<font color="red">*</font></td>
						<td>
						<input type="text" name="select_airport"   readonly="readonly">
						</td>
					</tr>
					<tr>
						<td class="label">Employee Name:<font color="red">*</font></td>
						<td><input type="text" name="empName"  onkeyup="return limitlength(this, 20)" readonly="readonly"></td>
						<td class="label">Desegnation:</td>
						<td><input type="text" name="empDesegnation"   onkeyup="return limitlength(this, 20)" readonly="readonly"></td>
						
					</tr> 
                    <tr>	<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="employeeCode"    onkeyup="return limitlength(this, 20)" readonly="readonly">
								</td>
								<td class="label">
									CPFAC No:
								</td>
								<td>
									<input type="text" name="cpfaccno"   readonly="readonly"/>
									<input type="hidden" name="processId" />
								</td>
							</tr>
							 
					<tr>
					 <td class="label">Date Of Birth:<font color="red">*</font></td>
						<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->
                    
					<td><input type="text" name="dob" tabindex="9"   onkeyup="return limitlength(this, 20)" readonly="readonly">
					</td>

                   <td class="label">
									Date of Joining:<font color="red">*</font></td>
				<td>
									
								    <input type="text" name="doj" tabindex="9"   onkeyup="return limitlength(this, 20)" readonly="readonly">
										<a href="javascript:show_calendar('forms[0].doj');"> </td>

					</tr>
								<tr>	
								<td class="label">
							 		Father's / Husband's Name:
									
								</td>
								<td>
									<input type="text" name="fhname"  onkeyup="return limitlength(this, 20)" readonly="readonly">
								</td>
								<td class="label">
									Pension Option:<font color="red" readonly="readonly">*</font>
								</td>
								<td>
								<input type="text" name="pensionOption"   readonly="readonly"/>
								 </td>
							</tr>
							<tr>
						<td class="label">Gender:<font color="red">*</font></td>
						<td colspan="3"><input type="text" name="gender"   readonly="readonly"/>
						 </td>
						</tr>
						
<tr><td class="label">
												Remarks:
											</td>
											<td colspan="3">
												<textarea name="remarks"  maxlength="150"  cols="80" tabindex="22" > </textarea></td>

</tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td>
								<input type="button" class="btn" value="Update" class="btn" onclick="validate();">
								<input type="button" class="btn" value="Reset" onclick="javascript:resetParams('<%=processId%>')" class="btn">
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