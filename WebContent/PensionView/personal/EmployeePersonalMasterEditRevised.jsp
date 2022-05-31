<%@ page import="java.util.*,aims.common.*"%>


<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
            String arrearInfoString1 = "";
            String regionFlag = "false";
            String region1="";
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>

		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript"> 
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
{	
  //  alert(obj);
	createXMLHttpRequest();	
	 var selectedIndex=document.forms[0].emplevel.options.selectedIndex;
	 var empLevelseleted=document.forms[0].emplevel[selectedIndex].text;
	// alert("selected level "+empLevelseleted);
	 var temp = new Array();
	 temp = empLevelseleted.split(',');
	 
	 var desegnation = temp[1];
	 
	 document.forms[0].desegnation.value=desegnation;
	
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
	function limitlength(obj, length){
	var maxlength=length
	if (obj.value.length>maxlength)
	obj.value=obj.value.substring(0, maxlength)
	}
	 var count =0;  	
  	 function call_calender(dobValue){
  	  show_calendar(dobValue);
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
      var l=document.getElementsByName("Nrelation");
  	 l[l.length-1].value="";
  	  var m=document.getElementsByName("guardianname");
  	 m[m.length-1].value="";
  	  var n=document.getElementsByName("totalshare");
  	 n[n.length-1].value="";
  	 }
	 function testSS(index,totalData){	
   		if(document.forms[0].cpfacnoNew.value=="")
   		 {
   		  alert("Please Enter CpfAccount Number");
   		  document.forms[0].cpfacnoNew.focus();
   		  return false;
   		  }
   		 if(document.forms[0].airPortCode.value=="")
   		 {
   		  alert("Please Enter Airportcode");
   		  document.forms[0].airPortCode.focus();
   		  return false;
   		  }
   		 if(document.forms[0].empName.value=="")
   		 {
   		  alert("Please Enter Employee Name");
   		   document.forms[0].empName.focus();
   		  return false;
   		  }
   		if(document.forms[0].empName.value.length<3)
  		 {
  		  alert("Please Enter Employee Name atleast 3 Characters");
  		  document.forms[0].empName.focus();
  		  return false;
  		  }
   		
   		  if(!ValidateName(document.forms[0].empName.value))
   		 {
   		  alert("Numeric/Invalid characters are not allowed");
   		   document.forms[0].empName.focus();
   		  return false;
   		  }
   		  
   		  if(document.forms[0].desegnation.value=="")
   		 {
   		  alert("Please Enter Designation");
   		  document.forms[0].desegnation.focus();
   		  return false;
   		  }
   		  
   	 var selectedIndex=document.forms[0].emplevel.options.selectedIndex;
	 var empLevelseleted=document.forms[0].emplevel[selectedIndex].text;
	 var temp = new Array();
	 temp = empLevelseleted.split(',');
	 var desegnation = temp[1];
	if(document.forms[0].desegnation.value!=desegnation){
	alert("Emp Level with Designation are not equal");
	document.forms[0].emplevel.focus();
   	 return false;
	}
	 
	 
   		  if(document.forms[0].dateofbirth.value=="")
   		 {
   		  alert("Please Enter Date of Birth");
   		  document.forms[0].dateofbirth.focus();
   		  return false;
   		  }
   		 if(document.forms[0].dateofjoining.value=="")
   		 {
   		  alert("Please Enter Date of Joining");
   		  document.forms[0].dateofjoining.focus();
   		  return false;
   		  }
   		  if(!document.forms[0].dateofbirth.value==""){
   		   var date1=document.forms[0].dateofbirth;
   	       var val1=convert_date(date1);
   		   
   		   var now = new Date();
   		   var birthday1= document.forms[0].dateofbirth.value;
   		   var dt1   = birthday1.substring(0,2);
   		   var mon1  = birthday1.substring(3,6);
  		   var year1=birthday1.substring(birthday1.lastIndexOf("/")+1,birthday1.length);
  		  
  		    if(mon1 == "JAN") month = 0;
           	else if(mon1 == "FEB") month = 1;
        	else if(mon1 == "MAR") month = 2;
        	else if(mon1 == "APR") month = 3;
        	else if(mon1 == "MAY") month = 4;
        	else if(mon1 == "JUN") month = 5;
        	else if(mon1 == "JUL") month = 6;
        	else if(mon1 == "AUG") month = 7;
        	else if(mon1 == "SEP") month = 8;
        	else if(mon1 == "OCT") month = 9;
        	else if(mon1 == "NOV") month = 10;
        	else if(mon1 == "DEC") month = 11;
        	var birthDate=new Date(year1,month,dt1);   
        	
   		  if(birthDate > now){
		       alert("DateofBirth cannot be greater than Currentdate");
			   	document.forms[0].dateofbirth.focus();
		       return false;
			}
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
   		  
   		 if (!ValidateName(document.forms[0].fhname.value))
	  	{
		alert("Numeric/Invalid characters are not allowed");
		document.forms[0].fhname.focus();
		return false;
  	   }
  	   if(document.forms[0].paddress.value.length>150){
  	    alert("Permanent Address Exceeds the Limit");
		document.forms[0].paddress.focus();
		return false;
  	   }
  	  
   	  	if(!ValidateTextArea(document.forms[0].paddress.value)){
  	    alert("Please Enter valid data");
		document.forms[0].paddress.focus();
		return false;
  	  }
  	 
  	  if(document.forms[0].taddress.value.length>150){
  	    alert("Temporary Address Exceeds the Limit");
		document.forms[0].taddress.focus();
		return false;
  	   }
  	   if(!ValidateTextArea(document.forms[0].taddress.value)){
  	    alert("Please Enter valid data");
		document.forms[0].taddress.focus();
		return false;
  	   }
  	   
  	   if(document.forms[0].division.value==""){
  	    alert("Please Select division");
		document.forms[0].division.focus();
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
   	   if(document.forms[0].remarks.value.length>150){
  	    alert("Remarks Exceeds the Limit");
		document.forms[0].remarks.focus();
		return false;
  	   }
  	     var fdob1=document.getElementsByName("Fdob");
  	      var fname=document.getElementsByName("Fname")
  	      var j;
   		  	for(j=0;j<fdob1.length;j++){
   		  			var s=fdob1[j].value; 
  	     if(!fdob1[j].value==""){
   		  var date1=fdob1[j].value;
   		 
   	        var val1=convert_date(fdob1[j]);
   		  
   		    if(val1==false)
   		    {
   		    return false;
   		    }
   		     if(fname[j].value==""){
   		      alert("Please Enter Family Member Name");
   		      fname[j].focus();
   		      return false;
   		      }
   		      }
   		      }
 	   	document.forms[0].action="<%=basePath%>psearch?method=personalUpdateRevised&startIndex="+index+"&totalData="+totalData;
 		document.forms[0].method="post";
		document.forms[0].submit();
   		 }   	 
function replaceAll(OldString, FindString, ReplaceString) {
  var SearchIndex = 0;
  var NewString = ""; 
  while (OldString.indexOf(FindString,SearchIndex) != -1)    {
    NewString += OldString.substring(SearchIndex,OldString.indexOf(FindString,SearchIndex));
    NewString += ReplaceString;
    SearchIndex = (OldString.indexOf(FindString,SearchIndex) + FindString.length);         
  }
  NewString += OldString.substring(SearchIndex,OldString.length);
  return NewString;
}
 function getJsDate1(dateStr, objFlag)
			{   var dateArr = dateStr.split("/");
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
			
				}
				if (objFlag == true)
					return new Date(Date.parse(jsDate));
				else
					return jsDate;
			}
 	 function getAirports(){
		var regionID;
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		createXMLHttpRequest();	
		var url ="<%=basePath%>reportservlet?method=getAirports&region="+regionID;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getAirportsList;
		xmlHttp.send(null);
    }
    function getAirportsList(){
		if(xmlHttp.readyState ==4){
			if(xmlHttp.status == 200){ 
				var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					if(stype.length==0){
				 	var obj1 = document.getElementById("select_airport");
				  	obj1.options.length=0; 
		  			obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
				}else{
		 		   	var obj1 = document.getElementById("select_airport");
		 		  	obj1.options.length = 0;
				 	for(i=0;i<stype.length;i++){
		  				if(i==0){
							obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
						}
						obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
					}
		  		}
			}
		}
	}
  </script>
	</head>

	<body class="BodyBackground" onload="retrimentDate();">

		<form method="post" action="<%=basePath%>psearch?method=personalUpdate">
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
				<tr>
					<td>
						<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td align="center" class="ScreenMasterHeading">
									Form3-2007-EmpPersonalInfo[Edit]&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center" class="label">
				<%if (request.getAttribute("ArrearInfo") != null) {
                arrearInfoString1 = (String) request.getAttribute("ArrearInfo");
                request.setAttribute("ArrearInfo", arrearInfoString1);
                System.out.println("arrearInfoString1****  "+ arrearInfoString1);
              }
               if (request.getAttribute("recordExist") != null) {%>
									<font color="red"><%=request.getAttribute("recordExist")%></font>
									<%}%>
								<td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td height="15%">
			<%String pensionNumber = "", editFrom = "";
            if (request.getAttribute("pensionNumber") != null) {
                pensionNumber = request.getAttribute("pensionNumber")
                        .toString();
            } if (request.getAttribute("EditBean") != null) {
                EmpMasterBean bean1 = (EmpMasterBean) request
                        .getAttribute("EditBean");
                String form2 = bean1.getForm2Nomination().trim();
                if (request.getAttribute("AirportList") != null) {
                    ArrayList AirportList = (ArrayList) request
                            .getAttribute("AirportList");
                     }
                if (request.getAttribute("editFrom") != null) {
                    editFrom = (String) request.getAttribute("editFrom");
                 }%><table align="center">
                 <tr>
					<td class="label"><b>PF ID:<b></td>
					<td>
						<input type="text" name="pfid" readonly="true" maxlength="35" tabindex="1" readonly="true" value='<%=bean1.getPfid()%>'/>
						<input type="hidden" name="setEmpSerialNo" readonly="true" maxlength="35" tabindex="1" value='<%=bean1.getEmpSerialNo()%>'/>
					</td>
					<td class="label">Old CPFACC.NO:</td>
					<td>
						<input type="text" name="cpfacnoNew" readonly="true" maxlength="25" tabindex="2"  value='<%=bean1.getCpfAcNo()%>'/>
					</td>
				</tr>
				<tr>
				    <td class="label">Airportcde:</td>
					<td>
						<input type="text" name="airPortCode" readonly="true" maxlength="25" tabindex="3" value='<%= bean1.getStation()%>'/>
					</td>											
					<td class="label">Region:</td>
					<td>
						<input type="text" name="airPortCode" readonly="true" maxlength="25" tabindex="4" value='<%= bean1.getRegion()%>'/>
					</td>
						<input type="hidden" name="airportSerialNumber" value='<%=bean1.getAirportSerialNumber()%>' onkeyup="return limitlength(this, s0)">
						<input type="hidden" name="region" value='<%=bean1.getRegion()%>' onkeyup="return limitlength(this, s0)">
						<input type="hidden" name="editFrom" value='<%=editFrom%>' />
				</tr>
				<tr>
					<td class="label">Employee Code:</td>
					<td>
						<input type="text" name="employeeCode" maxlength="20" tabindex="5" value='<%=bean1.getEmpNumber()%>' onkeyup="return limitlength(this, 20)" onkeypress="return numsDotOnly()" />
					</td>
					<td class="label">Employee Name:</td>
						<td>
							<input type="text" name="empName" maxlength="50"  readonly="true" tabindex="6"  value='<%=bean1.getEmpName()%>' onkeyup="return limitlength(this,50)"/>
						</td>
				</tr>
				<tr><td class="label">Father's / Husband's Name:</td>
					<td>
						<!-- <select name="select_fhname" tabindex="7" style="width:64px">
						<option value='F' <%if(bean1.getFhFlag().trim().equals("F")){ out.println("selected");}%>>Father</option>
						<option value='H' <%if(bean1.getFhFlag().trim().equals("H")){ out.println("selected");}%>>Husband</option>
						</select>-->
						<%String shName="";
						if(bean1.getFhFlag().trim().equals("F")){
						 shName="Father";
						}else if(bean1.getFhFlag().trim().equals("H")){
						shName="Husband";
					}
						%>
						<input type="text" name="select_fhname" maxlength="50" readonly="true" style="width:54px" tabindex="8" value='<%=shName%>' />
						
						<input type="text" name="fhname" maxlength="50" readonly="true" tabindex="8" value='<%=bean1.getFhName()%>' onkeyup="return limitlength(this, 50)"/>
					</td>
					<td class="label">Sex:</td>
					<td>
					<%String sex ="";
					if(bean1.getSex().trim().equals("M")){
					sex="Male";
					}else if(bean1.getSex().trim().equals("F")){
					sex="Female";
					}
					%>
					<input type="text" name="sex" maxlength="50" style="width:54px" tabindex="9" value='<%=sex%>' />
						<!-- <select name="sex" tabindex="9" style="width:130px">
						<option value="">[Select One]</option>
						<option value="M" <%if(sex.equals("M")){ out.println("selected");}%>>Male</option>
						<option value="F" <%if(sex.equals("F")){ out.println("selected");}%>>Female</option>
						</select>-->
					</td>
				</tr>
				<tr>
					<td class="label">Date of Birth:</td>
					<td>
						<input type="text" name="dateofbirth" readonly="true" tabindex="10" value='<%=bean1.getDateofBirth()%>' />
						<!--  <a href="javascript:show_calendar('forms[0].dateofbirth');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>-->
					</td>
					<td class="label">Date of Joining:</td>
					<td>
						<input type="text" name="dateofjoining" readonly="true" tabindex="11" value='<%=bean1.getDateofJoining()%>'  onkeyup="return limitlength(this, 20)" />
						<!--<a href="javascript:show_calendar('forms[0].dateofjoining');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>-->
					</td>
				</tr>
				<tr>
					<td class="label">Employee Level:</td>
					<td><%String emplevel = bean1.getEmpLevel().trim();%>
					<select name="emplevel" tabindex="12" style="width:130px" onchange="getDesegnation(document.forms[0].emplevel.value)">
						<option value="">[Select One]</option>
						<option value='E-9' <%if(emplevel.equals("E-9")){ out.println("selected");}%>>E-9,Executive Directior</option>
						<option value='E-8' <%if(emplevel.equals("E-8")){ out.println("selected");}%>>E-8,General Manager</option>
						<option value='E-7' <%if(emplevel.equals("E-7")){ out.println("selected");}%>>E-7,Jt. General Manager</option>
						<option value='E-6' <%if(emplevel.equals("E-6")){ out.println("selected");}%>>E-6,Deputy General Manager</option>
						<option value='E-5' <%if(emplevel.equals("E-5")){ out.println("selected");}%>>E-5,Asst. General Manager</option>
						<option value='E-4' <%if(emplevel.equals("E-4")){ out.println("selected");}%>>E-4,Senior Manager</option>
						<option value='E-3' <%if(emplevel.equals("E-3")){ out.println("selected");}%>>E-3,Manager</option>
						<option value='E-2' <%if(emplevel.equals("E-2")){ out.println("selected");}%>>E-2,Assistant Manager</option>
						<option value='E-1' <%if(emplevel.equals("E-1")){ out.println("selected");}%>>E-1,Junior Executive</option>
						<option value='NE-1' <%if(emplevel.equals("NE-1")){ out.println("selected");}%>>NE-1,Jr. Attendant</option>
						<option value='NE-2' <%if(emplevel.equals("NE-2")){ out.println("selected");}%>>NE-2,Attendant</option>
						<option value='NE-3' <%if(emplevel.equals("NE-3")){ out.println("selected");}%>>NE-3,Senior Attendant</option>
						<option value='NE-4' <%if(emplevel.equals("NE-4")){ out.println("selected");}%>>NE-4,Jr. Assistant</option>
						<option value='NE-5' <%if(emplevel.equals("NE-5")){ out.println("selected");}%>>NE-5,Assistant</option>
						<option value='NE-6' <%if(emplevel.equals("NE-6")){ out.println("selected");}%>>NE-6,Senior Assistant</option>
						<option value='NE-7' <%if(emplevel.equals("NE-7")){ out.println("selected");}%>>NE-7,Supervisor</option>
						<option value='NE-8' <%if(emplevel.equals("NE-8")){ out.println("selected");}%>>NE-8,Superintendent</option>
						<option value='NE-9' <%if(emplevel.equals("NE-9")){ out.println("selected");}%>>NE-9,Sr. Superintendent</option>
						<option value='NE-10' <%if(emplevel.equals("NE-10")){ out.println("selected");}%>>NE-10,Sr. Superintendent(SG)</option>
						<option value='B1' <%if(emplevel.equals("B1")){ out.println("selected");}%>>B1,Chairman</option>
						<option value='B2' <%if(emplevel.equals("B2")){ out.println("selected");}%>>B2,Member</option>
					   </select>
					</td>
					<td class="label">Designation:</td>
					<td>
						<input type="text" name="desegnation" readonly="true" tabindex="13" value='<%=bean1.getDesegnation()%>' onkeyup="return limitlength(this, 20)" />
                    </td>
				</tr>
				<tr>
					<td class="label">Discipline:</td>
					<td><%String department = bean1.getDepartment().trim();%>
			<input type="text" name="department" tabindex="14" style="width:130px" readonly="true"  value='<%=department%>' />		
					<!-- <select name="department" tabindex="14"  style="width:130px" style=display:inline;>
						<option value="">[Select One]</option>
					<%if (request.getAttribute("DepartmentList") != null) {
                    ArrayList deptList = (ArrayList) request
                            .getAttribute("DepartmentList");
                    for (int i = 0; i < deptList.size(); i++) {
                        boolean exist = false;
                        EmpMasterBean deptBean = (EmpMasterBean) deptList
                                .get(i);
                        if (deptBean.getDepartment().equalsIgnoreCase(
                                bean1.getDepartment().trim()))
                            exist = true;
                        if (exist) {
                            %>
						<option value="<%=bean1.getDepartment().trim()%>" <% out.println("selected");%>><%=bean1.getDepartment().trim()%></option>
						<%} else {%>
						<option value="<%= deptBean.getDepartment()%>"><%=deptBean.getDepartment()%></option>
              <%}}}%>
                 </select>-->
			    </td>
				<td class="label">Division:</td>
				<td><%String division = bean1.getDivision().trim(); %>
				<input type="text" name="division" tabindex="15" style="width:130px" readonly="true"  value='<%=division%>' />
				    <!-- <select name="division" tabindex="15" style="width:130px">
					<option value="">[Select One]</option>
					<option value='NAD' <%if(division.equals("NAD")){ out.println("selected");}%>>NAD</option>
					<option value='IAD' <%if(division.equals("IAD")){ out.println("selected");}%>>IAD</option>
				    </select>-->
				</td>
			</tr>										
			<%ArrayList regionList = new ArrayList();
                String rgnName = "", region = "";
                HashMap map = new HashMap();
                CommonUtil commonUtil = new CommonUtil();
                String[] regionLst = null;
                Iterator regionIterator1 = null;
                if (session.getAttribute("region") != null) {
                    regionLst = (String[]) session.getAttribute("region");
                }
               for (int i = 0; i < regionLst.length; i++) {
                    rgnName = regionLst[i];
                    if (rgnName.equals("ALL-REGIONS")
                            && session.getAttribute("usertype").toString()
                                    .equals("Admin")) {
                        map = new HashMap();
                        map = commonUtil.getRegion();
                        break;
                    } else {
                        map.put(new Integer(i), rgnName);
                    }
                }
                Set keys = map.keySet();
                regionIterator1 = keys.iterator();
                %>
                <tr>
					<td class="label">Separation Reason:</td>
					<td>
					<input type="text" name="reason" tabindex="16" id="mySelect" value='<%=bean1.getSeperationReason()%>' readonly="true" />
					</td>
					<td class="label">Date of Separation:</td>
					<td>
					<input type="text" name="seperationDate" tabindex="17" value='<%=bean1.getDateofSeperationDate()%>' readonly="true" onkeyup="return limitlength(this, 20)">
					<!-- <a href="javascript:show_calendar('forms[0].seperationDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>-->
					</td>
				</tr>
				<!-- <tr>
					<td>&nbsp;</td>
					<td>
					<input type="text" name="Other" value='<%=bean1.getOtherReason()%>' id="myText" style="display:none;" tabindex="18" onclick="swapback()">
					</td>
				</tr>-->
				<tr>
					<td class="label">Permanent Address:</td>
					<td>
					<TEXTAREA name="paddress" size="150" maxlength="150" tabindex="19" style="width:130px" readonly="true"><%=bean1.getPermanentAddress()%></TEXTAREA>
					</td>
					<td class="label">Temporary Address:</td>
					<td>
					<TEXTAREA name="taddress" size="150" maxlength="150" tabindex="20" style="width:130px" readonly="true"><%=bean1.getTemporatyAddress()%></TEXTAREA>
					</td>
				</tr>
				<tr>
					<td class="label">Pension Option Received:</td>
					<td>
                    <input type="text" name="wetherOption" value='<%=bean1.getWetherOption().trim()%>' tabindex="21" readonly="true" />
					</td>
					<td class="label">Whether Form2 Nomination Received :</td>
					<td>
					<input type="text" name="form1" tabindex="22" value='<%=bean1.getForm2Nomination().trim()%>'readonly="true"/>
					</td>
				</tr>
				<tr>
					<input type="hidden" name="cpfacno" value='<%=bean1.getCpfAcNo()%>'>
					<input type="hidden" name="empOldName" value='<%=bean1.getEmpName()%>'>
					<input type="hidden" name="empOldNumber" value='<%=bean1.getEmpNumber()%>'>
					<td class="label">Date of Super Annuation:</td>
					<td>
					<input type="text" name="dateOfAnnuation" readonly="true" tabindex="23" value='<%=bean1.getDateOfAnnuation()%>'>
					</td>
					<td class="label">Email Id:</td>
					<td>
					<input type="text" name="emailId" maxlength="50" onkeyup="return limitlength(this,50)" value='<%=bean1.getEmailId()%>' tabindex="24">
					</td>
				</tr>
				<tr>
				<td class="label">Date of Retirement:</td>
					<td>
					<input type="text" name="dateOfRetirement" readonly="true" tabindex="27" value='<%=bean1.getDateOfRetirement()%>'>
					</td>
					
                   <td class="label">Option Forms Received :</td>
				   <td>
				   <input type="text" name="optionform" tabindex="26" value='<%=bean1.getOptionForm().trim()%>'readonly="true"/>
 				   </td>
				</tr>
				<tr>
					<td class="label">Remarks:</td>
					<td>
					<TEXTAREA NAME="remarks" tabindex="25"><%=bean1.getRemarks()%></TEXTAREA>
					<input type="hidden" name="ArrearInfo" value="<%=arrearInfoString1%>">
					</td>
					<td class="label"></td>
					<td>
					
					</td>
				</tr>
			   <br>              
		</table>
  				<%}%>    
    <table align="center">
			<input type="hidden" name="flagData" value="<%=request.getAttribute("flag")%>">
				<tr>
					<td>
						<table align="center">
							<tr>
								<td align="center">
									<input type="button" class="btn" value="Update" class="btn" onclick="testSS('<%=request.getAttribute("startIndex")%>','<%=request.getAttribute("TotalData")%>')" tabindex="46">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="47">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="48">
								</td>
							 </tr>
						</table>
					</td>
				</tr>
				</table>
       </table>
			</table>
		</form>
	</body>
</html>



