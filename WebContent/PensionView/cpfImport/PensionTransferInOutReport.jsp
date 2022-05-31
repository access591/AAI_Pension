<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">       
<head>
<style>
.black_overlay {
	display: none;
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 3000px;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
}

.white_content {
	display: none;
	position: fixed;
	top: 25%;
	left: 25%;
	width: 50%;
	height: 50%;
	padding: 16px;
	border: 16px solid orange;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" /> 
<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css"	type="text/css" />
<link type="text/javascript" href="<%=basePath%>PensionView/scripts/calendar.js" /> 
<link type="text/javascript" href="<%=basePath%>PensionView/scripts/DateTime1.js" /> 
<script type="text/javascript">
function isNumberKey(evt)
      {
          var charCode = (evt.which) ? evt.which : event.keyCode
         if (((charCode >47) && (charCode <58)) || (charCode==46))
evt.returnValue = true;
else 
evt.returnValue = false;
      }
   var xmlHttp;
   var editFlag="false";
      var array=new Array();
    var tempArr = new Array();
     var buttonArr = new Array();
    var buttonName;   
   function editCpf(dateofbirth,dateofjoining,pensionno,monthyear,airportcode,region,remarkstypetextboxno,editid,userId){
	 var remarksType=document.getElementById(remarkstypetextboxno).value;
	 document.getElementsByName(remarkstypetextboxno)[0].disabled=false;
	 document.getElementsByName(remarkstypetextboxno)[0].style.background='#FFFFCC';
	 var buttonName=document.getElementsByName(editid)[0].value;
	 document.getElementsByName(editid)[0].value="S";
	 var flag="yes";
	 createXMLHttpRequest();	
	  var answer="";
	  buttonArr.pop();	
	  buttonArr[buttonArr.length]=document.getElementsByName(editid)[0].value;
	 if(buttonName=="S"){
	 if(remarksType == ''){
	  alert("Please select remarksType");
	 document.getElementById(remarkstypetextboxno).focus();
	   return false;
	 } 
	 if(remarksType=='Retirement'){
	  var finaldate=getDate(monthyear);			
			 var dobdate=getDate(dateofbirth);
			 var dobdt=dobdate.getDate();
			 var dobmt=dobdate.getMonth();
			 var dobyr1=dobdate.getFullYear()+60;
			  var newdobdate1=new Date(dobyr1,dobmt,dobdt);
			 var dobyr=newdobdate1.getYear();			
			 var newdobdate=new Date(dobyr,dobmt,dobdt);
	if(finaldate <= newdobdate){
			   alert("Please Ensure that Retirement must be greater than 60 years from the Date of birth for the Remark Type : Retirement");
		       return false;		       
				}
	 }
	 	  if(remarksType=='New'){
 	  var finaldate=getDate(monthyear);	
 	        var dojdate= getDate(dateofjoining);
            var dojdt=dojdate.getDate();
			 var dojmt1=dojdate.getMonth()+2;
			 var dojyr=dojdate.getFullYear();
			  var newdojdate1=new Date(dojyr,dojmt1,dojdt);
			 var dojmt=newdojdate1.getMonth();			 			
			 var newdojdate=new Date(dojyr,dojmt,dojdt); 
 	if(finaldate >= newdojdate){
	var response1= confirm("For Remark Type : New Joinee, Date of Joining Must be within 2 Months From the Date of Porting, To Proceed Click on OK");
	if (response1) {
	flag="yes";
	}else{
	flag="No";
	 document.getElementById(remarkstypetextboxno).focus();
	 return false;
	 }       
	 }
	 }
	  if(remarksType=='salarynotDrawn'){
	  var response =confirm("For the Remark Type : Salary Not Drawn , Emlouments Would be displayed as Zero for the Month :"+monthyear+" in Form-3 Report");
	if (response) {
	flag="yes";
	}else{
	flag="No";
	 document.getElementById(remarkstypetextboxno).focus();
	   return false;
	 }
  }
	 array[array.length]=pensionno;	
	 if(flag=="yes"){	  
		 var url="<%=basePath%>validatefinance?method=edittransferstatus&pensionNo="+pensionno+"&monthyear="+monthyear+"&station="+airportcode+"&regn="+region+"&remarksType="+remarksType+"&editid="+editid+"&userid="+userId;
    	xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = updateWages;
		xmlHttp.send(null);
	   }
	   }	
  }   
     function updateWages()
  {
	if(xmlHttp.readyState ==4)
  	{
	var buttonupdate=xmlHttp.responseText;	
	 process.style.display="none";
	 document.getElementById('process').style.display='none';
	 document.getElementById('fade').style.display='none';
	  var rownum=buttonupdate.substring(4, buttonupdate.length);
	 document.getElementsByName(buttonupdate)[0].value="E";	
	 buttonArr.pop();	
	 	var remarkstypetextboxno="disptransfertype"+rownum;
	  document.getElementsByName(remarkstypetextboxno)[0].disabled=true;
	 document.getElementsByName(remarkstypetextboxno)[0].style.background='none';  
	}
  }
   function getDate(date){	 
	  if(date.indexOf('-')!=-1){
		 elem = date.split('-'); 
	  }else{
		  elem = date.split('/'); 
	  }	  
		 day = elem[0];
		 mon1 = elem[1];
		 year = elem[2];
		 var month;
	   	 if((mon1 == "JAN") || (mon1 == "Jan")) month = 0;
        	else if(mon1 == "FEB" ||(mon1 == "Feb")) month = 1;
     	else if(mon1 == "MAR" || (mon1 == "Mar")) month = 2;
     	else if(mon1 == "APR" || (mon1 == "Apr")) month = 3;
     	else if(mon1 == "MAY" ||(mon1 == "May") ) month = 4;
     	else if(mon1 == "JUN" ||(mon1 == "Jun") ) month = 5;
     	else if(mon1 == "JUL"||(mon1 == "Jul")) month = 6;
     	else if(mon1 == "AUG" ||(mon1 == "Aug")) month = 7;
     	else if(mon1 == "SEP" ||(mon1 == "Sep")) month = 8;
     	else if(mon1 == "OCT"||(mon1 == "Oct")) month = 9;
     	else if(mon1 == "NOV" ||(mon1 == "Nov")) month = 10;
     	else if(mon1 == "DEC" ||(mon1 == "Dec")) month = 11;
	  var finaldate=new Date(year,month,day); 
	  return finaldate;	     	
  }
 function insertCpfData(monthyear,airportcode,region){ 
 var tempArr=document.forms[0].editpenno.value.split(",");     
       for(var i = 0; i < tempArr.length; i++) {
       array[array.length+1]=tempArr[i];
       }
  var tempArrayList = document.forms[0].h1.value.split(","); 
  var result=removeDupes(tempArrayList, array);     
  if(result!=''){
  alert("Pfids "+result+" doesnot edited please Submit the remarks for all records");
  return false;
  }
   for(var j = 0; j < buttonArr.length; j++) {  
       if(buttonArr[j]=='S'){
       alert("Please Save the Records before Finalizing");
       return false;
       }       
       } 
  var url="<%=basePath%>validatefinance?method=loadimportsheet&monthyear="+monthyear+"&station="+airportcode+"&regn="+region;
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();	
  }
  function deleteCpfData(monthyear,airportcode,region){
   var agree=confirm("Are you sure you want to delete?");       
  if (agree){   
    var url="<%=basePath%>validatefinance?method=deleteimportsheet&monthyear="+monthyear+"&station="+airportcode+"&regn="+region;
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();	
		}
   }  
	function removeDupes(a1, a2) { 
  var index = {}, result = [], i, l;  
 for (i=0, l=a2.length; i<l; i++) {  
   index['-' + a2[i]] = ""; 
  }   
for (i=0, l=a1.length; i<l; i++) {
     if (index['-' + a1[i]] !== "")
 result.push(a1[i]);  
 }   return result; 
}
  function createXMLHttpRequest()
	{
	if (window.XMLHttpRequest) {    
		xmlHttp = new XMLHttpRequest();   
	} else if(window.ActiveXObject) {    
	      try {     
	       xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");    
	       } catch (e) {     
	       		try {      
	       			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");     
	       		} catch (e) {      
	       			xmlHttp = false;     
	       		}    
	       	}   
	       		
	 } 
	 }
   function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
   function frmload(){
   
	  if(process){
	  process.style.display="none";
	  document.getElementById('process').style.display='none';
	  }
	 document.getElementById('fade').style.display='none';
	 
	}
	
</script>

</head>

<body class="BodyBackground" onload="frmload();">
<form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">   
   <tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>  
 <tr><td>&nbsp;</td></tr>  
   <tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
   <table width="100%" cellspacing="0" cellpadding="0">   
  <tr>
   <td  class="reportlabel"  align="center">CPF Uploded Current Month Corrections
  </td>
  </tr>
  <tr>
    <td >&nbsp;</td>
     </tr>
  <tr>
    <div id="process" class="white_content">
	<img src="<%=basePath%>PensionView/images/Indicator.gif" border="0" align="right" />
	<span class="label">Processing.......</span>
        </div>
		<div id="fade" class="black_overlay"></div>
    <td colspan="8">  
 <%
           if (request.getAttribute("empinfo") != null) {
            	String temp = "",editarray="";
                String userId = session.getAttribute("userid").toString();           
                ArrayList empinfo = new ArrayList();                
                ArrayList cntinfo= new ArrayList();    
                EmployeePersonalInfo beans=new EmployeePersonalInfo();         
                empinfo = (ArrayList) request.getAttribute("empinfo");
                cntinfo = (ArrayList) request.getAttribute("cntinfo");
                System.out.println("empinfo " + empinfo.size()); 
               if (empinfo.size() == 0) {
               %>
			<tr>

				<td>
					<table align="center" id="norec">
						<tr>
							<br>
							<td>
								<b> No Records Found </b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<%} else if (empinfo.size() != 0) {%>       
			<tr>
				<td height="25%">
					<table align="center" width="100%" cellpadding="1"  cellspacing="1" border="1">
						<tr ><td class="label" align="center">SR NO</td>
						<td class="label" align="center">PENSION NO</td>
							<td class="label"  align="center">EMPLOYEE NAME</td>
							<td class="label" align="center">DATE OF BIRTH</td>
							<td class="label" align="center">DATE OF JOINING</td>
							<td class="label" align="center">WETHER OPTION</td>
							<td class="label" align="center">SALARY MONTH</td>
							<td class="label" align="center">AIRPORTCODE</td>
							<td class="label" align="center">REGION</td>
							<td class="label" align="center">CURRENT MONTH STATUS</td>
							<td class="label" align="center">REMARK TYPE</td>							
							<td class="label" align="center">EDIT</td>		
</tr>
						
						<%int count = 0;  
                System.out.println("====================empinfo.size()========"+empinfo.size());
                for (int i = 0; i < empinfo.size(); i++) {
                    count++;                   
                     beans = (EmployeePersonalInfo) empinfo.get(i);
                   String PENSIONNO = beans.getPfID();
                   String EMPLOYEENAME = beans.getEmployeeName();
                   String DATEOFBIRTH = beans.getDateOfBirth();
                   String DATEOFJOINING = beans.getDateOfJoining();
                   String WETHEROPTION = beans.getWetherOption();
                   String STATUS =beans.getSeperationReason();
                   String TRANSFERSTATUS = beans.getRemarks();
                   String REGION = beans.getRegion();
                   String salaryMonth=beans.getMonthyear();
                    String airportcode=beans.getAirportCode();
                     temp += PENSIONNO + ",";   
                 editarray=beans.getRefPensionNumber();	
                   %>
					<tr>
					<td class="Data" width="4%" align="center"> <%=count%> </td> 
	<td class="Data" width="4%" align="center"> <%=PENSIONNO%></td> 
	<td class="Data" width="20%" align="center"> <%=EMPLOYEENAME%></td> 
	<td class="Data" width="10%" align="center"> <%=DATEOFBIRTH%></td> 
	<td class="Data" width="10%" align="center"> <%=DATEOFJOINING%></td>		
	<td class="Data" width="4%" align="center"> <%=WETHEROPTION%></td> 
	<td class="Data" width="10%" align="center"> <%=salaryMonth%></td>
	<td class="Data" width="10%" align="center"> <%=airportcode%></td>
	<td class="Data" width="10%" align="center"> <%=REGION%></td>
	<td class="Data" width="18%" align="center"> <%=STATUS%></td>
	<input type="hidden" id="status" value="<%=STATUS%>"/>
    <td class="Data" width="4%" align="right"><select disabled="disabled"  style="width:110px" name="disptransfertype<%=i%>" id="disptransfertype<%=i%>">
        <option value="" <%if(TRANSFERSTATUS.trim().equals("")){ out.println("selected");}%>>select one</option>
       <%if(STATUS.equals("Newly Salary Drawn")){%>
       <option value="New" <%if(TRANSFERSTATUS.trim().equals("New")){ out.println("selected");}%>>New Joinee</option>
        <option value="TIn" <%if(TRANSFERSTATUS.trim().equals("TIn")){ out.println("selected");}%>>Transfer In</option>
         <option value="prevsalnotDrawn" <%if(TRANSFERSTATUS.trim().equals("prevsalnotDrawn")){ out.println("selected");}%>>PrevSalNotDrawn</option>
        <%}else{%>
        <option value="TOut" <%if(TRANSFERSTATUS.trim().equals("TOut")){ out.println("selected");}%>>Transfer Out</option>
        <option value="Death" <%if(TRANSFERSTATUS.trim().equals("Death")){ out.println("selected");}%>>Death</option>
        <option value="Termination" <%if(TRANSFERSTATUS.trim().equals("Termination")){ out.println("selected");}%>>Termination</option>
        <option value="Resignation" <%if(TRANSFERSTATUS.trim().equals("Resignation")){ out.println("selected");}%>>Resignation</option>
        <option value="VRS" <%if(TRANSFERSTATUS.trim().equals("VRS")){ out.println("selected");}%>>VRS</option>
        <option value="Retirement" <%if(TRANSFERSTATUS.trim().equals("Retirement")){ out.println("selected");}%>>Retirement</option>
        <option value="salarynotDrawn" <%if(TRANSFERSTATUS.trim().equals("salarynotDrawn")){ out.println("selected");}%>>Salary Not Drawn</option>
  <%}%>
 </select></td>		
   <td class="Data"><input type="button" name="edit<%=i%>"  width="2%" style = "cursor:hand;" value="E" onclick="editCpf('<%=DATEOFBIRTH%>','<%=DATEOFJOINING%>','<%=PENSIONNO%>','<%=salaryMonth%>','<%=airportcode%>','<%=REGION%>','disptransfertype<%=i%>','edit<%=i%>','<%=userId%>')" /></td>
 </tr>  	
<%}%>
	</table>	
	  <tr><td>&nbsp;</td></tr>
  <%
		System.out.println("===========cntinfo======="+cntinfo.size());
		if(cntinfo.size()>0){
		
		
		for(int j=0;j<cntinfo.size();j++){		
		  EmployeePersonalInfo cntInfo = (EmployeePersonalInfo) cntinfo.get(j);
			%>
			<tr>
			<td>&nbsp;</td>
			</tr>       
		<tr>
				<td height="25%">
					<table align="left" width="100%" cellpadding="1"  cellspacing="1" border="1">
					<tr> 
						<td class="reportlabel" colspan="2" align="center">Comparison of No.of Employees in Current Month <%=cntInfo.getCurntMnth()%>  with Last Month <%=cntInfo.getPrevMnth()%>
						</td>
							 
					</tr>
						<tr> 
						<td class="label" width="50%" align="left">No of Subscribers as per Last Month's Return(A)</td>
							<td class="Data" width="50%" align="left"><%=cntInfo.getPrevMntCnt()%></td>
						</tr>
							<tr>
							<td class="label" width="50%" align="left">No.of New Subscribers/Transfer In (B)</td>
							<td class="Data" width="50%" align="left"><%=cntInfo.getTransferInCnt()%></td>
							</tr>
							<tr> 
						<td class="label" width="50%" align="left"> No.of Subscribers Left Service/Transfer Out/Salary Not Drawn (c)</td>
							<td class="Data" width="50%" align="left"><%=cntInfo.getTransferOutCnt()%></td>
						</tr>
							<tr > 
						<td class="label" width="50%" align="left">Actually Uploaded Data Current Month(A+B-C)</td> 
						<td class="Data" width="50%" align="left"><%=cntInfo.getCurntMntCnt()%></td>
						</tr>	
 	</table>	
	</td>
	</tr>	  
  <%}}%>	
    <tr>
    <td >&nbsp;</td>
     </tr>
     <table align="center" width="100%" cellpadding="1"  cellspacing="1" border="0">
      <tr>
       <input type="hidden" id="editpenno" value="<%=editarray%>"/> 
      <input type="hidden" id="h1" value="<%=temp %>"/> 
       <td class="btn" align="right"><input type="button" name="update" id="update" value="Finalize" onclick="insertCpfData('<%=beans.getMonthyear()%>','<%=beans.getAirportCode()%>','<%=beans.getRegion()%>')"/></td>	
      <td width="1%">&nbsp;</td>
     <td class="btn" align="left"><input type="button" name="rollback" id="rollback" value="Delete" onclick="deleteCpfData('<%=beans.getMonthyear()%>','<%=beans.getAirportCode()%>','<%=beans.getRegion()%>')"/></td>
   
   
   </tr>
   </table>
   <%}%>  
<%}%>
   </table>	
   <tr><td>&nbsp;</td></tr>  
   <tr><td>&nbsp;</td></tr>
 	<tr>
 	<font color="red" size="2">Note&nbsp;:-&nbsp;Without finalization of devation records. You cannot process/displaying the form-3 report</font>
 	
 	</tr>			
</form>
</body>
</html>
