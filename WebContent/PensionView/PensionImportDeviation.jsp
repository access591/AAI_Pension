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
    var buttonName;   
   function editCpf(prevemol,oldemoluments,oldepf,oldvpf,oldprincipal,oldintrest,pensionno,monthyear,airportcode,region,emolumentstextboxno,epftextboxno,vpftextboxno,principaltextboxno,intresttextboxno,remarkstypetextboxno,remarkstextboxno,editid,userId){
  	 var emoluments= document.getElementById(emolumentstextboxno).value;
  	 var epf= document.getElementById(epftextboxno).value;
	 var vpf= document.getElementById(vpftextboxno).value;	
	 var principal=document.getElementById(principaltextboxno).value;
	 var intrest=document.getElementById(intresttextboxno).value; 
	 var remarksType=document.getElementById(remarkstypetextboxno).value;
	 var remarks=document.getElementById(remarkstextboxno).value;
	 document.getElementsByName(remarkstypetextboxno)[0].disabled=false;
	 document.getElementsByName(remarkstypetextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(remarkstextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(remarkstextboxno)[0].readOnly=false; 
	 var buttonName=document.getElementsByName(editid)[0].value;
	 document.getElementsByName(editid)[0].value="S";
	 createXMLHttpRequest();	
	  var answer="";
	 if(buttonName=="S"){
	 if(remarksType == ''){
	  alert("Please select remarksType");
	 document.getElementById(remarkstypetextboxno).focus();
	   return false;
	 }
	if(remarks==''){
	 alert("Please select remarks");
	 document.getElementById(remarkstextboxno).focus();
	   return false;
	 }	  
	 	  if(remarksType == 'EOL'){	 
	   if(parseFloat(remarks)>31){
	    alert("EOL's cannot be  more than 31 days. for the PFID "+pensionno+"");
	    document.getElementsByName(remarkstextboxno)[0].focus();
	   return false;
	   }
	 if(parseFloat(emoluments)>parseFloat(prevemol)){	
	  alert("For the RemarkType EOL Emoluments cannot be greaterthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	 if(remarksType == 'PrevEOL'){
	  if(parseFloat(remarks)>31){
	    alert("PrevEOL's cannot be  more than 31 days. for the PFID "+pensionno+"");
	    document.getElementsByName(remarkstextboxno)[0].focus();
	   return false;
	   }
	 if(parseFloat(emoluments)<parseFloat(prevemol)){	
	  alert("For the RemarkType PrevEOL Emoluments cannot be lessthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	 if( remarksType == 'HPL'){
	 if(parseFloat(remarks)>31){
	    alert("HPL's cannot be  more than 31 days. for the PFID "+pensionno+"");
	    document.getElementsByName(remarkstextboxno)[0].focus();
	   return false;
	   }
	 if(parseFloat(emoluments)>parseFloat(prevemol)){	
	  alert("For the RemarkType HPL Emoluments cannot be greaterthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	 if(remarksType == 'PrevHPL'){
	 if(parseFloat(remarks)>31){
	    alert("PrevHPL's cannot be  more than 31 days. for the PFID "+pensionno+"");
	    document.getElementsByName(remarkstextboxno)[0].focus();
	   return false;
	   }
	 if(parseFloat(emoluments)<parseFloat(prevemol)){	
	  alert("For the RemarkType PrevHPL Emoluments cannot be lessthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	   if( remarksType == 'NonWageArrear'){	
	 if(parseFloat(emoluments)>parseFloat(prevemol)){	
	  alert("For the RemarkType Non Wage Arrear Emoluments cannot be greaterthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	  if(remarksType == 'PNWArrear'){
	 if(parseFloat(emoluments)<parseFloat(prevemol)){	
	  alert("For the Prev Non Wage Arrears Emoluments cannot be lessthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	   if(remarksType == 'PWarrear'){
	 if(parseFloat(emoluments)<parseFloat(prevemol)){	
	  alert("For the Prev  Wage Arrear Emoluments cannot be lessthan the previous Emoluments  for the PFID "+pensionno+"");
	 document.getElementsByName(remarkstypetextboxno)[0].focus();
	   return false;
	 }
	 } 
	 prevemoluments=parseInt(prevemol)+parseInt(5000);
	 prevemolume=parseInt(prevemol)-parseInt(1000);
	 if(remarksType == 'WageArrear'){
	 if(emoluments!=oldemoluments){
	if(emoluments>prevemoluments){	
	  alert("Emoluments is not matching with the Previous Month Salary drawn Amount for the PFID "+pensionno+"");
	 document.getElementsByName(emolumentstextboxno)[0].focus();
	   return false;
	 }
	 if(emoluments<prevemolume){	
	 alert("Emoluments is not matching with the Previous Month Salary drawn Amount for the PFID "+pensionno+"");
	 document.getElementsByName(emolumentstextboxno)[0].focus();
	   return false;
	 }	
	 }else{
	  alert("Please Enter actual Salary Emoluments , Arrears will be submitted in 3A format");
	 document.getElementsByName(emolumentstextboxno)[0].focus();
	   return false;	   
			}
			 var calcEpf=emoluments*12/100;
	 var diff=calcEpf-epf;	
				if(diff>=1 || diff<=-1){
					alert("EPF Is Not Equal to (Emoluments*12%) for the PFID "+pensionno+".");				
				 document.getElementsByName(emolumentstextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(emolumentstextboxno)[0].focus();
	 	 	 	 document.getElementsByName(emolumentstextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(epftextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(epftextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(principaltextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(principaltextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(intresttextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(intresttextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementById(emolumentstextboxno).value=oldemoluments
	 	 	 	 document.getElementById(epftextboxno).value=oldepf
	 	 	 	 document.getElementById(vpftextboxno).value=oldvpf
	 	 	 	 document.getElementById(principaltextboxno).value=oldprincipal
	 	 	 	 document.getElementById(intresttextboxno).value=oldintrest
					return false;
				}
			if(Math.round(emoluments)=='0' || emoluments=='' || Math.round(epf)=='0' || epf==''){
				alert("Emoluments/EPF Should not be 0 or blank for the PFID "+pensionno+".");				
				 document.getElementsByName(emolumentstextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(emolumentstextboxno)[0].focus();
	 	 	 	 document.getElementsByName(emolumentstextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(epftextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(epftextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(principaltextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(principaltextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementsByName(intresttextboxno)[0].readOnly=false;
	 	 	 	 document.getElementsByName(intresttextboxno)[0].style.background='#FFFFCC';
	 	 	 	 document.getElementById(emolumentstextboxno).value=oldemoluments
	 	 	 	 document.getElementById(epftextboxno).value=oldepf
	 	 	 	 document.getElementById(vpftextboxno).value=oldvpf
	 	 	 	 document.getElementById(principaltextboxno).value=oldprincipal
	 	 	 	 document.getElementById(intresttextboxno).value=oldintrest
				return false;
			}				
			}
		editFlag ="true";
		var ex=/^[A-Za-z0-9_.() &\-/]+$/;
		if(remarks!=""){
		if(!ex.test(remarks) || !ex.test(emoluments) || !ex.test(epf) ||!ex.test(vpf) || !ex.test(principal) ||!ex.test(intrest)){
		alert("Please Do not use comma as seperator");
		return false;
		} }		
		//default	
		array[array.length]=pensionno;	
			process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';  
		 document.getElementById('update').style.display='block';  
		 var url="<%=basePath%>validatefinance?method=editimportedprocess&pensionNo="+pensionno+"&monthyear="+monthyear+"&station="+airportcode+"&regn="+region+"&emol="+emoluments+"&empepf="+epf+"&empvpf="+vpf+"&princip="+principal+"&intrest="+intrest+"&remarksType="+remarksType+"&remarks="+remarks+"&editid="+editid+"&userid="+userId;
    	xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = updateWages;
		xmlHttp.send(null);
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
	 var emolumentstextboxno="dispEmoluments"+rownum;
	 var epftextboxno="dispEPF"+rownum;	 	 
	 var vpftextboxno="dispVPF"+rownum;
	 var principaltextboxno="dispprincipal"+rownum;
	   var intresttextboxno="dispintrest"+rownum;
	  var remarkstypetextboxno="dispremarkstype"+rownum;
	  var remarkstextboxno="remarks"+rownum;	  
	 document.getElementsByName(emolumentstextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentstextboxno)[0].style.background='none';
	 document.getElementsByName(epftextboxno)[0].readOnly=true;
	 document.getElementsByName(epftextboxno)[0].style.background='none';
	 document.getElementsByName(vpftextboxno)[0].readOnly=true;
	 document.getElementsByName(vpftextboxno)[0].style.background='none';
	 document.getElementsByName(principaltextboxno)[0].readOnly=true;
	 document.getElementsByName(principaltextboxno)[0].style.background='none';  
	 document.getElementsByName(intresttextboxno)[0].readOnly=true;
	 document.getElementsByName(intresttextboxno)[0].style.background='none'; 
	 document.getElementsByName(remarkstypetextboxno)[0].disabled=true;
	 document.getElementsByName(remarkstypetextboxno)[0].style.background='none';  
	 document.getElementsByName(remarkstextboxno)[0].readOnly=true;
	 document.getElementsByName(remarkstextboxno)[0].style.background='none'; 
	}
  }
  function changere(oldemoluments,oldepf,oldvpf,oldprincipal,oldintrest,emolumentstextboxno,epftextboxno,vpftextboxno,principaltextboxno,intresttextboxno,remarkstypetextboxno){
      var emoluments= document.getElementById(emolumentstextboxno).value;
  	 var epf= document.getElementById(epftextboxno).value;
	 var vpf= document.getElementById(vpftextboxno).value;
	 var principal=document.getElementById(principaltextboxno).value;
	 var intrest=document.getElementById(intresttextboxno).value; 
	  var remarksType=document.getElementById(remarkstypetextboxno).value; 
	  if(remarksType == 'WageArrear'){
	 document.getElementsByName(emolumentstextboxno)[0].readOnly=false;
	 document.getElementsByName(emolumentstextboxno)[0].focus();
	 document.getElementsByName(emolumentstextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(epftextboxno)[0].readOnly=false;
	 document.getElementsByName(epftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principaltextboxno)[0].readOnly=false;
	 document.getElementsByName(principaltextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(intresttextboxno)[0].readOnly=false;
	 document.getElementsByName(intresttextboxno)[0].style.background='#FFFFCC';
	 }else{
	 document.getElementsByName(emolumentstextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentstextboxno)[0].style.background='none';
	 document.getElementsByName(epftextboxno)[0].readOnly=true;
	 document.getElementsByName(epftextboxno)[0].style.background='none';
	 document.getElementsByName(vpftextboxno)[0].readOnly=true;
	 document.getElementsByName(vpftextboxno)[0].style.background='none';
	 document.getElementsByName(principaltextboxno)[0].readOnly=true;
	 document.getElementsByName(principaltextboxno)[0].style.background='none';  
	 document.getElementsByName(intresttextboxno)[0].readOnly=true;
	 document.getElementsByName(intresttextboxno)[0].style.background='none'; 
	 document.getElementById(emolumentstextboxno).value=oldemoluments;
  	 document.getElementById(epftextboxno).value=oldepf;
	 document.getElementById(vpftextboxno).value=oldvpf;
	 document.getElementById(principaltextboxno).value=oldprincipal;
	 document.getElementById(intresttextboxno).value= oldintrest;
	 }
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
  	
		 <tr>
		 	<td algin="center">
				<%if (request.getAttribute("message") != null) {%> 
				<font color="red" size="2"> <%=request.getAttribute("message")%></font>
				<%}%>				
				</td>
			</tr>
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
    	ArrayList cardReportList=new ArrayList(); 
	  	int size=0 ;
	  	String temp = "",editarray="";
	  	if(request.getAttribute("Penlist")!=null){		   
		String userId = session.getAttribute("userid").toString();   
 	  	cardReportList=(ArrayList)request.getAttribute("Penlist");
	  	EmployeeValidateInfo validateInfo=new EmployeeValidateInfo();
	size=cardReportList.size();
	System.out.println("size"+size);
	 if(size==0){%>
	
	 <tr>
	 <td class="label" align="center"><font  size="5"><b>No Records Found </b> </font></td>
	 </tr>
	<%}else if(size!=0){%>	
	
    <table width="90%" align="center"  border="1"  style="border-color: gray;"  cellspacing="0" cellpadding="0">
     <tr>
        <td  class="label" align="center" >PFID</td>
        <td  class="label" align="center" >Employee&nbsp;Name</td>
        <td class="label" align="center">dateofbirth</td>
        <td  class="label" align="center">Option</td> 
       <td  class="label" align="center">Station</td>
       <td  class="label" align="center">Prev&nbsp;Emol</td>                
        <td class="label" align="center">Emoluments</td>   
        <td  class="label" align="center">EPF</td>   
         <td class="label" align="center" >VPF</td>    
        <td  class="label" align="center" >Principal</td>           
        <td  class="label" align="center" >Intrest</td> 
         <td  class="label" align="center" >RemarksType</td> 
       <td  class="label" align="center" >Remarks</td> 
       <td class="label" align="center" >Edit</td> 
      </tr>    
      <tr>
        <td class="Data" align="center">1</td>
        <td class="Data" align="center">2</td>
        <td class="Data" align="center">3</td>
        <td class="Data" align="center">4</td>
        <td class="Data" align="center">5</td> 
        <td class="Data" align="center">6</td>
        <td class="Data" align="center">7</td>
        <td class="Data" align="center">8</td>
        <td class="Data" align="center">9</td>
        <td class="Data" align="center">10</td>
         <td class="Data" align="center">11</td>
          <td class="Data" align="center">12</td>
            <td class="Data" align="center">13</td>
             <td class="Data" align="center">14</td>
      </tr> <%	 
		for(int i=0;i<cardReportList.size();i++){
		validateInfo=(EmployeeValidateInfo)cardReportList.get(i);
		  temp += validateInfo.getPensionNumber() + ",";  
		   editarray=validateInfo.getDailyAllowance(); 
   %>  
      
<tr>
 
	<td class="Data" width="4%" align="center"> <%=validateInfo.getPensionNumber()%> </td> 
	<td class="Data" width="4%" align="center"> <%=validateInfo.getEmployeeName()%></td> 
	<td class="Data" width="12%" align="center"> <%=validateInfo.getDateOfBirth()%></td> 
	<td class="Data" width="4%" align="center"> <%=validateInfo.getWetherOption()%></td> 
	<td class="Data" width="4%" align="center"> <%=validateInfo.getAirportCD()%></td> 
	<td class="Data" width="4%" align="center"> <%=validateInfo.getBasic()%></td>
	 <input type="hidden" id="prevemoluments" value="<%=validateInfo.getBasic()%>"/> 
	<input type="hidden" id="oldemoluments" value="<%=validateInfo.getEmoluments()%>"/> 
	<input type="hidden" id="oldepf" value="<%=validateInfo.getEmpPFStatuary()%>"/>
	<input type="hidden" id="oldvpf" value="<%=validateInfo.getEmpVPF()%>"/>
	<input type="hidden" id="oldprincipal" value="<%=validateInfo.getEmpAdvRecPrincipal()%>"/>
	<input type="hidden" id="oldintrest" value="<%=validateInfo.getEmpAdvRecInterest()%>"/>	
	<td class="Data" width="4%" align="center"><input type="text"
					readonly="true" size="12" maxlength="10" name="dispEmoluments<%=i%>"  id="dispEmoluments<%=i%>"
					value='<%=validateInfo.getEmoluments()%>'					
					onkeypress="return isNumberKey(event)"/></td>
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="8" maxlength="8" name="dispEPF<%=i%>" id="dispEPF<%=i%>" 
					value='<%=validateInfo.getEmpPFStatuary()%>'
					onkeypress="return isNumberKey(event)"/></td>
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="8" maxlength="8" name="dispVPF<%=i%>" id="dispVPF<%=i%>"
					value='<%=validateInfo.getEmpVPF()%>'
					onkeypress="return isNumberKey(event)"/></td>						 
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="8" maxlength="8" name="dispprincipal<%=i%>" id="dispprincipal<%=i%>"
					value='<%=validateInfo.getEmpAdvRecPrincipal()%>'
					onkeypress="return isNumberKey(event)"/></td>
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="8" maxlength="8" name="dispintrest<%=i%>" id="dispintrest<%=i%>"
					value='<%=validateInfo.getEmpAdvRecInterest()%>'
					onkeypress="return isNumberKey(event)"/></td>
	<td class="Data" width="4%" align="right"><select disabled="disabled" onchange="changere('<%=validateInfo.getEmoluments()%>','<%=validateInfo.getEmpPFStatuary()%>','<%=validateInfo.getEmpVPF()%>','<%=validateInfo.getEmpAdvRecPrincipal()%>','<%=validateInfo.getEmpAdvRecInterest()%>','dispEmoluments<%=i%>','dispEPF<%=i%>','dispVPF<%=i%>','dispprincipal<%=i%>','dispintrest<%=i%>','dispremarkstype<%=i%>');" style="width:130px" 
	                            name="dispremarkstype<%=i%>" id="dispremarkstype<%=i%>">
	                            <option value="" <%if(validateInfo.getIpAddress().trim().equals("")){ out.println("selected");}%>>select one</option>
	                            <option value="EOL" <%if(validateInfo.getIpAddress().trim().equals("EOL")){ out.println("selected");}%>>EOL</option>
	                            <option value="PrevEOL" <%if(validateInfo.getIpAddress().trim().equals("PrevEOL")){ out.println("selected");}%>>Prev EOL</option>
								<option value="HPL" <%if(validateInfo.getIpAddress().trim().equals("HPL")){ out.println("selected");}%>>HPL</option>
								<option value="PrevHPL" <%if(validateInfo.getIpAddress().trim().equals("PrevHPL")){ out.println("selected");}%>>Prev HPL</option>
								<option value="NonWageArrear" <%if(validateInfo.getIpAddress().trim().equals("NonWageArrear")){ out.println("selected");}%>>Non Wage Arrear</option>
								<option value="PNWArrear" <%if(validateInfo.getIpAddress().trim().equals("PNWArrear")){ out.println("selected");}%>>Prev Non Wage Arrear</option>
								<option value="PWarrear" <%if(validateInfo.getIpAddress().trim().equals("PWarrear")){ out.println("selected");}%>>Prev  Wage Arrear</option>
								<option value="WageArrear" <%if(validateInfo.getIpAddress().trim().equals("WageArrear")){ out.println("selected");}%>>Wage Arrear</option>
 </select></td>		
	<td class="Data" width="10%" align="right"><input type="text"
					readonly="true" size="8" maxlength="8" name="remarks<%=i%>" id="remarks<%=i%>"
					value='<%=validateInfo.getRemarks()%>'onkeypress="return isNumberKey(event)" /></td>
   <td class="Data"><input type="button" name="edit<%=i%>"  width="2%" style = "cursor:hand;" 
					value="E"
					onclick="editCpf('<%=validateInfo.getBasic()%>','<%=validateInfo.getEmoluments()%>','<%=validateInfo.getEmpPFStatuary()%>','<%=validateInfo.getEmpVPF()%>','<%=validateInfo.getEmpAdvRecPrincipal()%>','<%=validateInfo.getEmpAdvRecInterest()%>','<%=validateInfo.getPensionNumber()%>','<%=validateInfo.getMonthYear()%>','<%=validateInfo.getAirportCD()%>','<%=validateInfo.getRegion()%>','dispEmoluments<%=i%>','dispEPF<%=i%>','dispVPF<%=i%>','dispprincipal<%=i%>','dispintrest<%=i%>','dispremarkstype<%=i%>','remarks<%=i%>','edit<%=i%>','<%=userId%>')" /></td>
 </tr>
  	
<%}%>
	</table>				
   <%}%>
    <tr>
    <td >&nbsp;</td>
     </tr>
       <table align="center" width="100%" cellpadding="1"  cellspacing="1" border="0">
      <tr>
       <input type="hidden" id="editpenno" value="<%=editarray%>"/> 
       <input type="hidden" id="h1" value="<%=temp %>"/> 
   <td class="btn" align="right"><input type="button" name="update" id="update" value="Finalize" onclick="insertCpfData('<%=validateInfo.getMonthYear()%>','<%=validateInfo.getAirportCD()%>','<%=validateInfo.getRegion()%>')"/></td>	
   <td>&nbsp;</td>
   <td>&nbsp;</td>
  <td class="btn" align="left"><input type="button" name="rollback" id="rollback" value="Delete" onclick="deleteCpfData('<%=validateInfo.getMonthYear()%>','<%=validateInfo.getAirportCD()%>','<%=validateInfo.getRegion()%>')"/></td>
   <td>&nbsp;</td>
   <td>&nbsp;</td>
   </tr>
   </table>
  <tr><td>&nbsp;</td></tr>  
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
