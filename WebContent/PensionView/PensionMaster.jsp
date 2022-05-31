<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%String userId = (String) session.getAttribute("userid");
			if (userId == null) {
				RequestDispatcher rd = request
						.getRequestDispatcher("./PensionIndex.jsp");
				rd.forward(request, response);
			}
			%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
	String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>AAI</title>
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">

<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
<SCRIPT type="text/javascript"
	src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
<script type="text/javascript">
		
 	 function dispOff()
   	{ 
     var totalshare=document.getElementsByName("totalshare");
     var nomineeName=document.getElementsByName("Nname");
   		  var k;
   		  var finalShare=0;
   	if(window.document.forms[0].equalShare.checked==true)
	  {   
   		 for(k=0;k<totalshare.length;k++){
   		 var totalShare =100/totalshare.length;
		  totalshare[k].value=totalShare;
		  var finalShare=finalShare+parseInt(totalshare[k].value);
	     totalshare[k].readOnly=true;
	 }
	}
	else
	{
	for(k=0;k<totalshare.length;k++){
	 totalshare[k].value=="";
	 var l=document.getElementsByName("totalshare");
	 for(i=0;i<l.length;i++){
  	 l[i].value="";
  	  totalshare[k].readOnly=false;
  	 }
	}
	}
  }
  
function ValidateEMail(emailStr) 
 {
	var emailPat=/^(.+)@(.+)$/
	var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]"
	var validChars="\[^\\s" + specialChars + "\]"
	var quotedUser="(\"[^\"]*\")"
	var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/
	var atom=validChars + '+'
	var word="(" + atom + "|" + quotedUser + ")"
	var userPat=new RegExp("^" + word + "(\\." + word + ")*$")
	var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$")
	var matchArray=emailStr.match(emailPat)
	if (matchArray==null) 
	{
		alert("Email address seems incorrect (check @ and .'s)")
		return false
	}
	var user=matchArray[1]
	var domain=matchArray[2]

	if (user.match(userPat)==null) 
	{
		alert("The username doesn't seem to be valid.")
		return false
	}

	var IPArray=domain.match(ipDomainPat)
	if (IPArray!=null) 
	{
		for (var i=1;i<=4;i++) 
		{
			if (IPArray[i]>255) 
			{
				alert("Destination IP address is invalid!")
				return false
			}
		}
	    return true
	}

	var domainArray=domain.match(domainPat)
	if (domainArray==null) 
	{
		alert("The domain name doesn't seem to be valid.")
		return false
	}

	var atomPat=new RegExp(atom,"g")
	var domArr=domain.match(atomPat)
	var len=domArr.length
	if (domArr[domArr.length-1].length<2 || 
		domArr[domArr.length-1].length>4) 
		{
		   alert("The address must end in a three-letter domain, or two letter country.")
		   return false
		}

	if (len<2) 
	{
		var errStr="This address is missing a hostname!"
		alert(errStr)
		return false
	}
	return true;
}
		
   	
   	 function numsDotOnly()
	         {
	            if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46))
	            {
	               event.keyCode=event.keyCode;
	            }
	            else
	            {
				event.keyCode=0;
                }
	       }	
   	function limitlength(obj, length){
		var maxlength=length;
		if (obj.value.length>maxlength)
			obj.value=obj.value.substring(0, maxlength);
		}
			
	var xmlHttp;

	function createXMLHttpRequest()
	{
	 if(window.ActiveXObject)
	 {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 }
	 else if (window.XMLHttpRequest)
	 {
		xmlHttp = new XMLHttpRequest();
	 }
	}
	function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
	
function getDesegnation(obj)
{	createXMLHttpRequest();	
	var empLevel = obj.selectedIndex!=-1?obj.options[obj.selectedIndex].value:0;
	
	var url ="<%=basePath%>search1?method=getDesegnation&empLevel="+empLevel;
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getDesegnationName;
	xmlHttp.send(null);
}

function getDesegnationName()
{
	if(xmlHttp.readyState ==4)
	{ //alert("in readystate"+xmlHttp.responseText);
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		
		  if(stype.length==0){
		 	var obj1 = document.getElementById("designation");
		 	obj1.options.length=0; 
		  
		  }else{
		   var designation = getNodeValue(stype[0],'designation');
		 // alert(designation);
		 	 document.forms[0].desegnation.value=designation
		   
		  }
		}
	}
}

function getAirports()
{	   var index=document.forms[0].region.selectedIndex.value;
       var selectedIndex=document.forms[0].region.options.selectedIndex;
	   var region=document.forms[0].region[selectedIndex].text;
	createXMLHttpRequest();	
    var url ="<%=basePath%>search1?method=getAirports&region="+region;
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getAirportsList;
	xmlHttp.send(null);
}

function getAirportsList()
{
	if(xmlHttp.readyState ==4)
	{
	 	if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  if(stype.length==0){
		 	var obj1 = document.getElementById("airPortCode");
		   	obj1.options.length=0; 
		  
		  }else{
		   	var obj1 = document.getElementById("airPortCode");
		   	  	
		  	obj1.options.length = 0;
		  	for(i=0;i<stype.length;i++){
				obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
				var  selected = getNodeValue(stype[i],"selected");
				if(selected == 'true')
				{
				obj1.options[i].selected = true;
				}
		  	}
		  
			  }
		}
	}
}
			
			function swap() { 
			if(document.getElementById('mySelect').value == 'Other'){ 
			document.getElementById('mySelect').style.display = 'block'; 
			document.getElementById('myText').style.display = 'block'; 
			document.getElementById('myText').focus(); 
			//alert("hi");
			//alert(document.getElementById('myText'));
			} else{
			document.getElementById('mySelect').style.display = 'block'; 
			document.getElementById('myText').style.display = 'none'; 
			}
			} 
			function swapback() { 
			document.getElementById('mySelect').selectedIndex = 0; 
			document.getElementById('mySelect').style.display = 'block'; 
			document.getElementById('myText').style.display = 'none'; 
			} 
						
			function hide(){
		     divNominee1.style.display="none";
			 divNomineeHead.style.display="none";
			}
			function reasonOther(){
			// alert("hi"+document.forms[0].reason.value);
			 if(document.forms[0].reason.value=="Other")
			  reason1.style.display="block";
			  else   reason1.style.display="none";
			 }
			
			
			function show(){
			 if(document.forms[0].form1.value=="Yes")
   		     {
   		     divNominee1.style.display="block";
   		     divNomineeHead.style.display="block";
   		  
   		     }
   		      if(document.forms[0].form1.value=="No")
   		     {
   		      divNominee1.style.display="none";
   		      divNomineeHead.style.display="none";
   		    }
			}

		
   		 function retrimentDate(){
   	      var formDate=document.forms[0].dateofbirth.value;
	       if(formDate.lastIndexOf("-")!=-1 || formDate.lastIndexOf("/")!=-1){
			var year=formDate.substring(formDate.lastIndexOf("/")+1,formDate.length);
			if(year.length==4){
				var dateOfBirth=Number(year)+Number(60);
				dateOfBirth=formDate.replace(year,dateOfBirth);
				document.forms[0].dateOfAnnuation.value=dateOfBirth;
			}
		
		}
		
	 }
   		
   		
   		
 	 var count =0;
  	 function callFamily()
    	{ 
  	 count++;
     divFamily2.innerHTML+=divFamily1.innerHTML;
  	 var i=document.getElementsByName("FName");
  	 i[i.length-1].value="";
  	 var j=document.getElementsByName("Fdob");
  	  j[j.length-1].value="";
  	  j[j.length-1].id ="Fdob"+count;
  	 
  	  j = document.getElementsByName("cal");
  	  j[j.length-1].onclick=function(){call_calender("forms[0].Fdob"+count)};
  	  var k=document.getElementsByName("Frelation");
  	  k[k.length-1].value="";
  	
  	 }
  	 function call_calender(dobValue){
  	 	show_calendar(dobValue);
  	 }
  	 function retirement_calendar(dobValue){
  	   show_calendar(dobValue);
  		retrimentDate();
  		 }
     var count1 =0;
  	 function callNominee(){
  	  count1++;
  	 divNominee2.innerHTML+=divNominee1.innerHTML;
  	 var i=document.getElementsByName("Nname");
  	 i[i.length-1].value="";
  	  var j=document.getElementsByName("Naddress");
  	 j[j.length-1].value="";
  	  var k=document.getElementsByName("Ndob");
  	  k[k.length-1].value="";
  	  k[k.length-1].id ="Ndob"+count1;
  	 
  	  k = document.getElementsByName("cal1");
  	  k[k.length-1].onclick=function(){call_calender("forms[0].Ndob"+count1)};
  	//  alert(k[k.length-1].onclick);
  	  var l=document.getElementsByName("Nrelation");
  	 l[l.length-1].value="";
  	  var m=document.getElementsByName("guardianname");
  	 m[m.length-1].value="";
  	 var n=document.getElementsByName("totalshare");
  	 n[n.length-1].value="";
  	 }
		
   	function testSS(){
 
          
         if(document.forms[0].region.selectedIndex<1)
   		 {
   		  alert("Please Select Region");
   		  document.forms[0].region.focus();
   		  return false;
   		  } 
   		 if(document.forms[0].empName.value=="")
   		  {
   		  alert("Please Enter Employee Name");
   		   document.forms[0].empName.focus();
   		  return false;
   		  }
   		  
   		if (!ValidateName(document.forms[0].empName.value))
	  		{
		 alert("Numeric Values are not allowed");
		 document.forms[0].empName.focus();
		 return false;
  	    }
  	   if (!ValidateName(document.forms[0].fhname.value))
	  		{
		alert("Numeric Values are not allowed");
		document.forms[0].fhname.focus();
		return false;
  	  }
  	   if(document.forms[0].sex.value=="")
   		  {
   		  alert("Please Select one");
   		  document.forms[0].sex.focus();
   		  return false;
   		  }
   		  if(document.forms[0].emplevel.value=="")
   		 {
   		  alert("Please Select Employee level");
   		  document.forms[0].emplevel.focus();
   		  return false;
   		  }
   		  if(document.forms[0].dateofbirth.value=="")
   		 {
   		  alert("Please Enter Date of Birth");
   		  document.forms[0].dateofbirth.focus();
   		  return false;
   		  }
   		  if(!document.forms[0].dateofbirth.value==""){
   		//   alert(document.forms[0].dateofbirth.value);
   		   var date1=document.forms[0].dateofbirth;
   		   alert(date1);
   	       var val1=convert_date(date1);
   		  
   		    if(val1==false)
   		      {
   		      return false;
   		      }
   		    }
   		      
   		   if(!document.forms[0].dateofjoining.value==""){
   		    var date1=document.forms[0].dateofjoining;
   	        var val1=convert_date(date1);
   		    if(val1==false)
   		     {
   		      return false;
   		     }
   		    }
   		      
   		  if(!document.forms[0].dateOfAnnuation.value==""){
   		    var date1=document.forms[0].dateOfAnnuation;
   	        var val1=convert_date(date1);
   		    if(val1==false)
   		    {
   		    return false;
   		    }
   		   }
   		      
   		   if(!document.forms[0].seperationDate.value==""){
   		    var date1=document.forms[0].seperationDate;
   	        var val1=convert_date(date1);   		  
   		    if(val1==false)
   		    {
   		    return false;
   		    }
   		  }
   		      
   		if(!ValidateTextArea(document.forms[0].paddress.value)){
  	    alert("Please Enter valid data");
		document.forms[0].paddress.focus();
		return false;
  	   }
  	   if(!ValidateTextArea(document.forms[0].taddress.value)){
  	    alert("Please Enter valid data");
		document.forms[0].taddress.focus();
		return false;
  	   }
  	   
  	    if(!document.forms[0].emailId.value==""){
  	     if(ValidateEMail(document.forms[0].emailId.value)==false){
  	    	document.forms[0].emailId.focus();
		 return false;}
  	   }
  	    if(!ValidateTextArea(document.forms[0].remarks.value)){
  	   // alert("Please Enter valid data");
		document.forms[0].remarks.focus();
		return false;
  	   }
  	   if(document.forms[0].division.value==""){
  	    alert("Please Select division");
		document.forms[0].division.focus();
		return false;
  	   }
		
  	     var fdob1=document.getElementsByName("Fdob");
  	     var fname=document.getElementsByName("Fname")
  	      var j;
   		  	for(j=0;j<fdob1.length;j++){
   		  	if(!fdob1[j].value==""){
   		  var date1=fdob1[j].value;
   		 
   	        var val1=convert_date(fdob1[j]);
   		    if(val1==false){
   		    return false;
   		    } 
   		      if(fname[j].value==""){
   		      alert("Please Enter Family Member Name");
   		      fname[j].focus();
   		      return false;
   		      }
   		    }
   		 }
   		     
  	     
   		 var ndob1=document.getElementsByName("Ndob");
   		  var gaddress =document.getElementsByName("gaddress");
   		 var nname=document.getElementsByName("Nname");
   		 var guardianname= document.getElementsByName("guardianname");
   		 var totalshare=document.getElementsByName("totalshare");
   		 var k;
   		  
   		  for(k=0;k<ndob1.length;k++){
   		   var val1=convert_date(ndob1[k]);
   		     if(val1==false)
   		    {
   		    return false;
   		    }
   		  var s=ndob1[k].value; 
   		 if(ndob1[k].value!=""){
   		 var month = getJsDate1(s.toLowerCase(),false);
   		 var birthmotnth=month.substring(0,2);
   	     var birthday=month.substring(3,5);
   	     var birthyear=month.substring(month.lastIndexOf(",")+1,month.length);
   		 var birthDate = new Date(birthyear, birthmotnth, birthday); 
   		
   		  var currDate = new Date();
   		  var monthnumber = currDate.getMonth();
 		  var monthday    = currDate.getDate();
          var year        = currDate.getYear();

         var currentDate = new Date(year, monthnumber, monthday); 
         var daysDiff=(currentDate-birthDate)/86400000;
  		 var years=Math.round(daysDiff/365);
  		  
          }
          
          if(totalshare[k].value!="" ){
   		if(nname[k].value==""){
   		      alert("Please Enter Nominee Name");
   		      nname[k].focus();
   		      return false;
   		      }
   		
   		}
   		
  	      if(!ndob1[k].value==""){
   		  var date1=ndob1[k].value;
   		    var val1=convert_date(ndob1[k]);
   		     if(val1==false)
   		    {
   		    return false;
   		    }
   		     if(nname[k].value==""){
   		      alert("Please Enter Nominee Name");
   		      nname[k].focus();
   		      return false;
   		      }
   		      else if(!ValidateTextArea(document.forms[0].paddress.value)){
  	            alert("Please Enter valid data");
		        document.forms[0].paddress.focus();
		        return false;
  	         }
   		     else if(years<18 && guardianname[k].value=="")
          	 {
          	  guardianname[k].focus();
              alert("please enter guardianname");
              return false;
          	 }	
          	 if(years<18 && gaddress[k].value=="") {
          		gaddress[k].focus();
            	alert("Please Enter Guardian Address");
             	 return false;
          	 }
       	      }
   		      }
   		      
   	  var totalshare=document.getElementsByName("totalshare");
   	  var nomineeName=document.getElementsByName("Nname");
   	  var l;
   	  var finalShare=0;
	if(!window.document.forms[0].equalShare.checked && totalshare[0].value!=="")
	  {    
   		 for(l=0;l<totalshare.length;l++){
   		 if(nomineeName[l].value==""){
   		 alert("Please Enter Nominee Name");
   		 nomineeName[l].focus();
   		 return false;
   		 }
		//   alert("inside if ");
		  if(totalshare[l].value!=""){
		// alert("share is"+totalshare[l].value);
		  var finalShare=finalShare+parseInt(totalshare[l].value);
		//  alert(finalShare);
		   if(finalShare>100){
		    {
          	  totalshare[0].focus();
              alert("Total share shoud't Exceed 100");
              return false;
          	 }
		   }
		   }
	
	}
	}     
   		  	
   		   
   		document.forms[0].action="<%=basePath%>PensionView/PensionAdd.jsp"
		document.forms[0].method="post";
		document.forms[0].submit();
   		}
   		 
   		 function getJsDate1(dateStr, objFlag)
			{
			// alert("dateStr"+dateStr);
				var dateArr = dateStr.split("/");
				var jsDate = "";
				if (!isNaN(dateArr[1].charAt(0))){
					jsDate = dateArr[1] + " " + dateArr[0] + ", " + dateArr[2];
					
					}
				else
				{  var monthArr = [];
					monthArr["jan"] = "01";
					monthArr["feb"] = "02";
					monthArr["mar"] = "03";
					monthArr["apr"] = "04";
					monthArr["may"] = "05";
					monthArr["jun"] = "06";
					monthArr["jul"] = "07";
					monthArr["aug"] = "08";
					monthArr["sep"] = "09";
					monthArr["oct"] = "10";
					monthArr["nov"] = "11";
					monthArr["dec"] = "12";
					monthNum = monthArr[dateArr[1]];	
					jsDate = monthNum + " " + dateArr[0] + ", " + dateArr[2];
				//alert(jsDate);
				}
				if (objFlag == true)
					return new Date(Date.parse(jsDate));
				else
					return jsDate;
			}
   		 
   	function charsCapsSpaceDotOnly()
         {
              if(event.keyCode >=97 && event.keyCode<=122)    
                   event.keyCode = event.keyCode - 32;
              if(((event.keyCode >=65 && event.keyCode<=90))||(event.keyCode==32)||
                                (event.keyCode==46))  
                   event.keyCode = event.keyCode;
              else
                   event.keyCode=0;
         }
         
       function getmaxCpfacno1(){
        var url ="<%=basePath%>search1?method=getMaxCpfAccNo";
        document.forms[0].action=url;
     	document.forms[0].method="post";
		document.forms[0].submit();
		
		}
         
     
		
function getmaxCpfacno()
{	
    
	createXMLHttpRequest();	
	var url ="<%=basePath%>search1?method=getMaxCpfAccNo";
	//alert(url);
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getmaxCpfacnumber;
	xmlHttp.send(null);
}

function getmaxCpfacnumber()
{
	if(xmlHttp.readyState ==4)
	{ 	if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		
		  if(stype.length==0){
		 	var obj1 = document.getElementById("cpfAcno");
		 	obj1.options.length=0; 
		  
		  }else{
		  
		    var cpfAcno = getNodeValue(stype[0],'cpfAcno');
		   
		  }
		}
	}
}
      
        

</script>
</head>

<body class="BodyBackground"
	onload="document.forms[0].airPortCode.focus();hide();">

<form method="post" action="./PensionAdd.jsp">

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">

	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0" class="tbborder">
			<tr>
				<td height="5%" colspan="2" align="center"
					class="ScreenMasterHeading">Personnel Master[Add] &nbsp;&nbsp;<font
					color="red">
				<%if (request.getAttribute("recordExist") != null) {
				out.println("Record already exist");
			}%>
				</font></td>

			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<%if (request.getAttribute("MaxCpfacno") != null) {
				String cpfacno = (String) request.getAttribute("MaxCpfacno");

				%>

				<td height="15%">
				<table align="center">
					<tr>
						<td class="label">CPF ACC.NO:<font color="red">&nbsp;*</font>
						</td>
						<td><input type="text" name="cpfacno" maxlength="25"
							tabindex="1" value='<%=cpfacno%>'></td>
						<td class="label">Region:<font color="red">&nbsp;*</font></td>
						<td><SELECT NAME="region" style="width: 130px"
							onchange="getAirports()">
							<option value="">[Select One]</option>
							<%
							int j=0;
                            while(it.hasNext()){
							  region=hashmap.get(it.next()).toString();
							  j++;
							 %>
							<option="<%=j%>" ><%=region%></option>
							<% }
							%>
						</SELECT></td>


					</tr>
					<tr>
						<td class="label">Airport Code:<font color="red">&nbsp;*</font>
						</td>
						<td><select name="airPortCode" tabindex="2">
							<option value="">[Select One]</option>
							<%if (request.getAttribute("AirportList") != null) {
					ArrayList airpors = (ArrayList) request
							.getAttribute("AirportList");
					for (int i = 0; i < airpors.size(); i++) {

						EmpMasterBean airportBean = (EmpMasterBean) airpors
								.get(i);

						%>
							<option><%=airportBean.getStation()%></option>
							<%}} %>
						</select> <!-- <input type="text" name="airPortCode" onkeyup="return limitlength(this, 20)" tabindex="1">-->
						</td>
						<td class="label">Airport Serial Number:</td>
						<td><input type="text" name="airportSerialNumber "
							onkeyup="return limitlength(this, 20)" tabindex="3"></td>

					</tr>
					<tr>
						<td class="label">Employee Code:</td>
						<td><input type="text" name="employeeCode" maxlength="20"
							onkeyup="return limitlength(this, 20)" tabindex="4"></td>
						<td class="label">Employee Name:<font color="red">&nbsp;*</font>
						</td>

						<td><input type="text" name="empName" maxlength="50"
							onkeypress='ValidateAlphaNumericwithoutSpace()' tabindex="5"
							onkeyup="return limitlength(this, 50)"></td>

					</tr>
					<tr>
						<td class="label">Father's / Husband's Name:</td>
						<td><input type="text" name="fhname" maxlength="50"
							onkeyup="return limitlength(this, 50)" tabindex="6"></td>

						<td class="label">Sex:</td>

						<td><select name="sex" tabindex="7">

							<option value='M'>Male</option>
							<option value='F'>Female</option>
						</select></td>


					</tr>

					<tr>
						<td class="label">Marital Status:</td>
						<td><select name="mstatus" tabindex="8">

							<option value='Yes'>Yes</option>
							<option value='No'>No</option>
						</select></td>
						<td class="label">Employee Level:<font color="red">&nbsp;*</font>
						</td>
						<td><select name="emplevel" tabindex="9"
							onchange="getDesegnation(this)">
							<option value="">[Select One]</option>
							<option value='E-9'>E-9</option>
							<option value='E-8'>E-8</option>
							<option value='E-7'>E-7</option>
							<option value='E-6'>E-6</option>
							<option value='E-5'>E-5</option>
							<option value='E-4'>E-4</option>
							<option value='E-3'>E-3</option>
							<option value='E-2'>E-2</option>
							<option value='E-1'>E-1</option>
							<option value='NE-1'>NE-1</option>
							<option value='NE-2'>NE-2</option>
							<option value='NE-3'>NE-3</option>
							<option value='NE-4'>NE-4</option>
							<option value='NE-5'>NE-5</option>
							<option value='NE-6'>NE-6</option>
							<option value='NE-7'>NE-7</option>
							<option value='NE-8'>NE-8</option>
							<option value='NE-9'>NE-9</option>
							<option value='NE-10'>NE-10</option>
							<option value='B1'>B1</option>
							<option value='B2'>B2</option>
						</select></td>


					</tr>

					<tr>
						<td class="label">Designation:<font color="red">&nbsp;*</font>
						</td>
						<td><input type="text" name="desegnation" readonly="true"
							tabindex="10" onkeyup="return limitlength(this, 50)"></td>
						<td class="label">Date of Birth:<font color="red">&nbsp;*</font>
						</td>
						<td><input type="text" name="dateofbirth"
							onblur="javascript:retrimentDate()" tabindex="11"
							onkeyup="return limitlength(this, 20)"> <a
							href="javascript:retirement_calendar('forms[0].dateofbirth');"><img
							src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
						</td>


					</tr>
					<tr>
						<td class="label">Date of Joining:</td>
						<td><input type="text" name="dateofjoining"
							onkeyup="return limitlength(this, 20)" tabindex="12"> <a
							href="javascript:show_calendar('forms[0].dateofjoining');"><img
							src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
						</td>

						<td class="label">Date of Super Annuation:</td>
						<td><input type="text" name="dateOfAnnuation"
							onkeyup="return limitlength(this, 20)" tabindex="13"></td>

					</tr>
					<tr>
						<td class="label">Discipline:</td>
						<td><select name="department" tabindex="14"
							style="display: inline;">
							<option value="">[Select One]</option>
							<%if (request.getAttribute("DepartmentList") != null) {
					                                ArrayList departmentList = (ArrayList) request.getAttribute("DepartmentList");
				            	for (int i = 0; i < departmentList.size(); i++) {

					           	EmpMasterBean deptBean = (EmpMasterBean) departmentList.get(i);
                               	%>

							<option><%=deptBean.getDepartment()%></option>
							<%}
				}

				%>

						</select></td>
						<td class="label">Date of Separation:</td>
						<td><input type="text" name="seperationDate"
							onkeyup="return limitlength(this, 20)" tabindex="15"> <a
							href="javascript:show_calendar('forms[0].seperationDate');"><img
							src="<%=basePath%>PensionView/images/calendar.gif" border="no"
							tabindex="17" /></a></td>

					</tr>
					<tr>
						<td class="label">Separation Reason:</td>
						<td><select name="reason" tabindex="16" id="mySelect"
							onChange="swap();" style="display: inline;">
							<option value="">[Select One]</option>
							<option value='Retirement'>Retirement</option>
							<option value='Death'>Death</option>
							<option value='Resignation'>Resignation</option>
							<option value='Termination'>Termination</option>
							<option value='Option for Early Pension'>Option for
							Early Pension</option>
							<option value='Other'>Other</option>
						</select> <!-- <input type="text" name="reason" onkeyup="return limitlength(this, 20)">-->
						</td>
						<td>&nbsp;</td>
						<td><input type="text" name="Other" id="myText"
							style="display: none;" onclick="swapback()"></td>
					</tr>

					<tr>
						<td class="label">Email Id:</td>
						<td><input type="text" name="emailId" maxlength="100"
							onkeyup="return limitlength(this, 100)" tabindex="16"></td>

						<td class="label">Permanent Address:</td>

						<td><TEXTAREA NAME="paddress" maxlength="150" cols="20"
							tabindex="17"></TEXTAREA></td>

					</tr>

					<tr>
						<td class="label">Temporary Address:</td>
						<td><TEXTAREA NAME="taddress" maxlength="150" cols="20"
							tabindex="18"></TEXTAREA></td>
						<td class="label">Pension Option Received:</td>
						<td><select name="wetherOption" tabindex="19">
							<option value="">[Select One]</option>
							<option value='A'>A</option>
							<option value='B'>B</option>
							<option value='No'>No</option>
						</select></td>

					</tr>
					<tr>
						<td class="label">Whether Form2 <br>
						Nomination Received:</td>
						<td><select name="form1" tabindex="20" onchange="show();">
							<option value="">[Select One]</option>
							<option value='Yes'>Yes</option>
							<option value='No'>No</option>
						</select></td>

						<td class="label">Division:<font color="red">&nbsp;*</font></td>
						<td><select name="division" tabindex="21">
							<option value="">[Select One]</option>
							<option value='NAD'>NAD</option>
							<option value='IAD'>IAD</option>
						</select></td>



					</tr>
					<tr>
						<td class="label">Remarks:</td>
						<td><TEXTAREA NAME="remarks" maxlength="150" cols="20"
							tabindex="22"></TEXTAREA> <!--<input type="text" maxlength="150" name="remarks"  cols="17" >
								--></td>

					</tr>





					<tr>
						<td class="ScreenSubHeading">Family Details</td>
					</tr>

					<tr>

						<td class="label">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Name</td>
						<td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Date of Birth</td>
						<td class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Relation with member</td>


						<td>&nbsp;</td>

					</tr>
				</table>


				<div id="divFamily1">
				<table align="center">
					<tr>
						<td class="label"><input type="text" name="FName"
							maxlength="50" tabindex="24"></td>
						<td><input type="text" name="Fdob" id="Fdob0" tabindex="25">
						<a href="#" name="cal"
							onclick="javascript:call_calender('forms[0].Fdob0')"> <img
							src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
						</td>
						<td><select name="Frelation" tabindex="26">
							<option value="">[Select One]</option>
							<option value='SPOUSE'>SPOUSE</option>
							<option value='SON'>SON</option>
							<option value='DAUGHTER'>DAUGHTER</option>
							<option value='MOTHER'>MOTHER</option>
							<option value='FATHER'>FATHER</option>
							<option value='SONS WIDOW'>SON'S WIDOW</option>
							<option value='WIDOWS DAUGHTER'>WIDOW'S DAUGHTER</option>
							<option value='WIDOWS DAUGHTER'>WIDOW'S DAUGHTER</option>
							<option value='MOTHER-IN-LOW'>MOTHER-IN-LOW</option>
							<option value='FATHER-IN-LOW'>FATHER-IN-LOW</option>

						</select></td>
						<td>&nbsp;&nbsp;</td>
						<td><b><img alt=""
							src="<%=basePath%>PensionView/images/addIcon.gif"
							onclick="callFamily()" tabindex="27"></b></td>

						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>

				</div>

				<div id="divFamily2"></div>
				<div id="divNomineeHead">
				<table align="center" width="95%">
					<tr>
						<td class="ScreenSubHeading">Nomination for
						PF&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td class="label">&nbsp;&nbsp;Equal Share Check:&nbsp; <input
							type="checkbox" name="equalShare" onClick='dispOff()'
							value="true" tabindex="28"></td>
					</tr>

					<tr>

						<td class="label">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Name&nbsp;
						</td>
						<td class="label">Address</td>
						<td class="label">Date of Birth</td>
						<td class="label">Relation with Member</td>
						<td class="label">Name of <br>
						Guardian</td>
						<td class="label">&nbsp;Address of <br>
						Guardian</td>
						<td class="label">Total Share pay in %</td>
						<td>&nbsp;</td>

					</tr>
				</table>
				</div>
				<div id="divNominee1">
				<table align="center">
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="text"
							size="18" name="Nname" maxlength="50" onblur="dispOff()"
							tabindex="29"></td>
						<td><input type="text" size="16" name="Naddress"
							maxlength="150" tabindex="30"></td>
						<td><input type="text" size="16" name="Ndob" id="Ndob0"
							tabindex="31"> <a href="#" name="cal1"
							onclick="javascript:call_calender('forms[0].Ndob0');"><img
							src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>

						</td>
						<td><select name="Nrelation" tabindex="32">
							<option value="">[Select One]</option>
							<option value='SPOUSE'>SPOUSE</option>
							<option value='SON'>SON</option>
							<option value='DAUGHTER'>DAUGHTER</option>
							<option value='MOTHER'>MOTHER</option>
							<option value='FATHER'>FATHER</option>
							<option value='SONS WIDOW'>SON'S WIDOW</option>
							<option value='WIDOWS DAUGHTER'>WIDOW'S DAUGHTER</option>
							<option value='WIDOWS DAUGHTER'>WIDOW'S DAUGHTER</option>
							<option value='MOTHER-IN-LOW'>MOTHER-IN-LOW</option>
							<option value='FATHER-IN-LOW'>FATHER-IN-LOW</option>

						</select></td>
						<td><input type="text" size="16" maxlength="50"
							name="guardianname" tabindex="33"></td>
						<td><input type="text" size="16" maxlength="150"
							name="gaddress" tabindex="34"></td>
						<td><input type="text" size="16" name="totalshare"
							onkeypress="numsDotOnly()" tabindex="35"></td>
						<td>&nbsp;&nbsp;<b><img alt=""
							src="<%=basePath%>PensionView/images/addIcon.gif"
							onclick="callNominee();"></b></td>

						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</table>

				</div>

				<div id="divNominee2"></div>

				<table align="center">


					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td align="center"><input type="button" class="btn"
							value="Add" class="btn" onclick="testSS()" tabindex="36">
						<input type="button" class="btn" value="Reset"
							onclick="javascript:document.forms[0].reset()" class="btn"
							tabindex="37"> <input type="button" class="btn"
							value="Cancel" onclick="javascript:history.back(-1)" class="btn"
							tabindex="38"></td>
					</tr>
				</table>
		</table>
		</td>
	</tr>
	<%}%>
	</form>
</body>
</html>
