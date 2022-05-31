<%@ page import="java.util.*,aims.common.*"%>


<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String arrearInfoString1 = "";
			String regionFlag = "false";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%String pensionNumber = "", editFrom = "";
			if (request.getAttribute("pensionNumber") != null) {
				pensionNumber = request.getAttribute("pensionNumber")
						.toString();
			}

			%>
<%if (request.getAttribute("EditBean") != null) {
				EmpMasterBean bean1 = (EmpMasterBean) request
						.getAttribute("EditBean");
				EmployeeAdlPensionInfo adlPensionInfo = (EmployeeAdlPensionInfo) request
						.getAttribute("adlPensionInfo");
				String form2 = bean1.getForm2Nomination().trim();
				if (request.getAttribute("AirportList") != null) {
					ArrayList AirportList = (ArrayList) request
							.getAttribute("AirportList");
				}
				if (request.getAttribute("editFrom") != null) {
					editFrom = (String) request.getAttribute("editFrom");
				}

				%>
<html>
	<head>
		<title>AAI</title>

		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
		<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/GeneralFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script language = "Javascript">
/**
 * DHTML date validation script for dd/mm/yyyy. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("The date format should be : dd/mm/yyyy")
		return false
	}
return true
}



</script>
		<script type="text/javascript"> 
		window.attachEvent("onload",loadSchemeDetls); 
		window.attachEvent("onload",loadDocument); 
	function deleteFamilyDetils(cpfaccno,fmemberName,rowcolumn,rowid)
{  
  createXMLHttpRequest();	
   var rowcolumn=rowcolumn;
   var deleterow = document.getElementById(rowcolumn);
    document.getElementById(rowcolumn).style.display = 'none'; 
    document.getElementById(rowcolumn).innerText = "";
	var fname=fmemberName;
	var cpfaccno=cpfaccno;
	var region=document.forms[0].region.value;
	var url ="<%=basePath%>search1?method=deleteFamilyDetails&fmemberName="+fname+"&cpfaccno="+cpfaccno+"&region="+region+"&rowid="+rowid;
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = deleteFamilyDetails1;
	xmlHttp.send(null);
 }
 function deleteNomineeDetils(cpfaccno,nomineeName,rowcolumn,rowid)
  {
  createXMLHttpRequest();	
  var rowcolumn=rowcolumn;
  var deleterow = document.getElementById(rowcolumn);
    document.getElementById(rowcolumn).style.display = 'none'; 
    document.getElementById(rowcolumn).innerText = "";
	var nomineeName=nomineeName;
	var cpfaccno=cpfaccno;
	var region=document.forms[0].region.value;
	var url ="<%=basePath%>search1?method=deleteNomineeDetils&nomineeName="+nomineeName+"&cpfaccno="+cpfaccno+"&region="+region+"&rowid="+rowid;
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = deleteNomineeDetils1;
	xmlHttp.send(null);
}
 function deleteNomineeDetils1()
 {	if(xmlHttp.readyState ==4)
	{  	if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  if(stype.length==0){
		 	var obj1 = document.getElementById("deletestatus");
		 	obj1.options.length=0; 
		   }else{
		   var deletestatus = getNodeValue(stype[0],'deletestatus');
		   var fmemberName=getNodeValue(stype[0],'nomineeName');
		   //alert(fmemberName +" "+deletestatus);
		   }
		}
	}
}

function deleteFamilyDetails1()
{   
	if(xmlHttp.readyState ==4)
	{  
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		
		  if(stype.length==0){
		 	var obj1 = document.getElementById("deletestatus");
		 	obj1.options.length=0; 
		  
		  }else{
		   var deletestatus = getNodeValue(stype[0],'deletestatus');
		   var fmemberName=getNodeValue(stype[0],'fmemberName');
		   //alert(fmemberName +" "+deletestatus);
		 	
		   
		  }
		}
	}
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
	function dispOff()
   { 
     var totalshare=document.getElementsByName("totalshare");
      var nomineeName=document.getElementsByName("Nname");
   		
   		  var k;
   		  var finalShare=0;
   	
	if(window.document.forms[0].equalShare.checked)
	  {   
	  if(totalshare.length==1){
	  var totalShare =100/((totalshare.length));
		// alert("totalShare "+totalShare);
		  totalshare[0].value=totalShare;
		  var finalShare=finalShare+parseInt(totalshare[0].value);
		 
		 // alert(finalShare);
		   totalshare[0].readOnly=true;
	  }
   		 for(k=0;k<(totalshare.length)-1;k++){
   		 var totalShare =100/((totalshare.length)-1);
		// alert("totalShare "+totalShare);
		  totalshare[k].value=totalShare;
		  var finalShare=finalShare+parseInt(totalshare[k].value);
		 
		 // alert(finalShare);
		   totalshare[k].readOnly=true;
		//document.getElementsByName("totalshare").value=totalShare;
	}
	}
	else
	{
	for(k=0;k<(totalshare.length)-1;k++){
	
	//alert("inside else ");
	//window.document.forms[0].totalshare.disabled=false;
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
		function swap() { 
			if(document.getElementById('mySelect').value == 'Other'){ 
			document.getElementById('mySelect').style.display = 'block'; 
			document.getElementById('myText').style.display = 'block'; 
			document.getElementById('myText').focus(); 
			
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
	 
	 function hide(){
			 
			
			 divNominee1.style.display="block";
			 divNomineeHead.style.display="block";
			 
			 
			}
			
			function show(){
			// alert("hit"+document.forms[0].form1.value);
			 if(document.forms[0].form1.value=="Yes")
   		     {
   		     divNominee1.style.display="block";
   		      divNomineeHead.style.display="block";
   		   //   document.forms[0].nomineeflag.value="true";
   		     }
   		      else 
   		     {
   		      divNominee1.style.display="none";
   		      divNomineeHead.style.display="none";
   		
   		     }
			}
	 

	
	  
	
	function limitlength(obj, length){
	var maxlength=length
	if (obj.value.length>maxlength)
	obj.value=obj.value.substring(0, maxlength)
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
  	 // alert(j[j.length-1].onclick);
  	  
  	  var k=document.getElementsByName("Frelation");
  	  k[k.length-1].value="";
  	
  	 }
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
  	//  alert(k[k.length-1].onclick);
   	 var l=document.getElementsByName("Nrelation");
  	 l[l.length-1].value="";
  	  var m=document.getElementsByName("guardianname");
  	 m[m.length-1].value="";
  	  var n=document.getElementsByName("option_flag");
  	 n[n.length-1].value="";
  	 var sa=document.getElementsByName("saving_acc_no");
  	 sa[sa.length-1].value="";
  	 }
 function removeWthDrwls(tr){
  	
 	if(tr.hasChildNodes()==true){
 		var oChildNodes = tr.childNodes;	
 		var deleteTagID,deletedNodeId,childNodesLength;
 		childNodesLength=oChildNodes.length;
 		deleteTagID=oChildNodes[1].lastChild.id;
 		deletedNodeId=deleteTagID.substring(findIndexDigits(deleteTagID),deleteTagID.length);
 		
 		if(deleteID==''){
 			deleteID=deletedNodeId;
 		}else{
 			deleteID=deleteID+','+deletedNodeId;
 		}
 		
 	}
	tr.parentNode.removeChild(tr);
	
} 	 
var len=0,deleteID='',reclength=0;
var finalJSList=new Array();
var finalDupJSList=new Array();
var delList=new Array();
var docueclsedlen=0,docueclseddeleteID='',docueclsedreclength=0;
var docueclsedfinalJSList=new Array();

var docueclseddelList=new Array();
function callDocuments()
{   
	//alert('-------docueclsedreclength.length-------'+docueclsedreclength+'delList----'+docueclseddelList+'deleteID----'+docueclseddeleteID);
    if(docueclsedreclength>=1){
	   if(validateDocumentDtls(docueclsedreclength)!=true){
			return false;
	    }
    }
    docueclsedreclength++;           
	var table = document.getElementById('documentenclose');	
    var tr    = document.createElement('TR');
	var td1   = document.createElement('TD');
	var td2   = document.createElement('TD');
	var inp  = document.createElement('INPUT');
	
	inp.setAttribute("Name", "documentattname");
	inp.setAttribute("id", "documentattname"+docueclsedreclength); 
	inp.setAttribute("size", "75"); 
	inp.setAttribute("maxlength", "75"); 
	var deleteIcon     = document.createElement('IMG');
	deleteIcon.setAttribute('src', '<%=basePath%>PensionView/images/cancelIcon.gif');
	deleteIcon.onclick = function(){
		removeDocuments(tr);
	}
	
    var space1=document.createTextNode('  ');
    var space2=document.createTextNode('  ');
	table.appendChild(tr);
	tr.appendChild(td1);
	tr.appendChild(td2);
	td1.appendChild(inp);
	td2.appendChild(space1);
	td2.appendChild(deleteIcon);
}
function removeDocuments(tr){
  	
 	if(tr.hasChildNodes()==true){
 		var docChildNodes = tr.childNodes;	
 		var docdeleteTagID,docdeletedNodeId,docchildNodesLength;
 		docchildNodesLength=docChildNodes.length;
 		for(var i=0;i<docchildNodesLength;i++){
 			docdeleteTagID=docChildNodes[i].lastChild.id;
 			if(docdeleteTagID!=''){
	 			//alert('deleted id'+docdeleteTagID);
 				break;
 			}
 			
 		}
 		docdeletedNodeId=docdeleteTagID.substring(findIndexDigits(docdeleteTagID),docdeleteTagID.length);
 		//alert('deletedNodeId'+docdeletedNodeId);
 		if(docueclseddeleteID==''){
 			docueclseddeleteID=docdeletedNodeId;
 		}else{
 			docueclseddeleteID=docueclseddeleteID+','+docdeletedNodeId;
 		}
 		
 	}
 
	tr.parentNode.removeChild(tr);
	
}

function validateDocumentDtls(docueclsedreclength)
{

 	if(document.getElementById("documentattname"+docueclsedreclength).value==''){
	 alert('Please Enter Document Name');
	  document.getElementById("documentattname"+docueclsedreclength).focus();
	  return false;
	}	
	return true;
}
function createDocList(sortedDeletList){
	var sortedltdid,lastdeletedId=0;

	for(var j=1;j<=docueclsedreclength;j++){
	  	if(sortedDeletList.length!=0){
	  		for(var sndlst=0;sndlst<sortedDeletList.length;sndlst++){
			sortedltdid=sortedDeletList[sndlst];
			lastdeletedId=sortedltdid;
			if(sortedltdid==j){
				sortedDeletList.splice(sndlst,1);
				break;
			}else{
				docueclsedfinalJSList.push(j);
			}
		}
	  	}else{
	  		
	  		if(lastdeletedId!=docueclsedreclength){
	  			docueclsedfinalJSList.push(j);
	  		}
	  	}
	}
	
	return removeDuplicateElement(docueclsedfinalJSList);
}	
function callScheme()
{   
	//alert('-------reclength.length-------'+reclength+'delList----'+delList+'deleteID----'+deleteID);
    if(reclength>=1){
	    if(validateSchemeDtls(reclength)!=true){
			return false;
	    }
    }
    reclength++;           
	var table = document.getElementById('schemecontrs');	
    var tr    = document.createElement('TR');
	var td1   = document.createElement('TD');
	var td2   = document.createElement('TD');
	var inp1  = document.createElement('INPUT');
	var inp2  = document.createElement('INPUT');
	inp1.setAttribute("Name", "schemecertino");
	inp1.setAttribute("id", "schemecertino"+reclength); 
	inp1.setAttribute("size", "45"); 
	inp1.setAttribute("maxlength", "45"); 
	 
	inp2.setAttribute("Name", "schemecertiauthority");
	inp2.setAttribute("id", "schemecertiauthority"+reclength);  
	inp2.setAttribute("size", "65"); 
	inp2.setAttribute("maxlength", "65"); 
	
	var deleteIcon     = document.createElement('IMG');
	deleteIcon.setAttribute('src', '<%=basePath%>PensionView/images/cancelIcon.gif');
	deleteIcon.onclick = function(){
		removeWthDrwls(tr);
	}
	
    var space1=document.createTextNode('  ');
    var space2=document.createTextNode('  ');
    
	table.appendChild(tr);
	tr.appendChild(td1);
	tr.appendChild(td2);
	td1.appendChild(inp1);
	td2.appendChild(inp2);
	td2.appendChild(space1);
	td2.appendChild(deleteIcon);
}

function loadSchemeDetls(){   
	//alert('This is load Scheme Dtls');
	var schemeInfo='<%=bean1.getSchemeList()%>';
	var tempSchemeInfo = new Array();
	
	var schemeno,schemeauthority,schemeSrno,schemeCntr;
	//alert('Before'+schemeInfo);
	if(schemeInfo.indexOf('[')!=-1 && schemeInfo.lastIndexOf(']')!=-1){
		
		schemeInfo=schemeInfo.substring(schemeInfo.indexOf('[')+1,schemeInfo.lastIndexOf(']'));
		
	}
	//alert('After'+schemeInfo);
	var tempSchemeInfo1 = schemeInfo.split(',');

	if(tempSchemeInfo1!=0){
		var table = document.getElementById('schemecontrs');
	for(var i=0;i<tempSchemeInfo1.length;i++){
		var schemeData=	tempSchemeInfo1[i];
	
		tempSchemeInfo = schemeData.split('@');
		
		if(tempSchemeInfo.length>1){
				reclength++;
			schemeno=trim(tempSchemeInfo[0]);
			schemeauthority=trim(tempSchemeInfo[1]);
			schemeSrno=trim(tempSchemeInfo[2]);
			//alert(schemeno+'------------'+schemeauthority);
			 var tr    = document.createElement('TR');
			 var tr    = document.createElement('TR');
			var td1   = document.createElement('TD');
			var td2   = document.createElement('TD');
			var inp1  = document.createElement('INPUT');
			var inp2  = document.createElement('INPUT');
			inp1.setAttribute("Name", "schemecertino");
			inp1.setAttribute("id", "schemecertino"+reclength); 
			inp1.setAttribute("size", "45"); 
			inp1.setAttribute("maxlength", "45"); 
			inp1.setAttribute("value", schemeno); 
			 
			inp2.setAttribute("Name", "schemecertiauthority");
			inp2.setAttribute("id", "schemecertiauthority"+reclength);  
			inp2.setAttribute("size", "65"); 
			inp2.setAttribute("maxlength", "65"); 
			inp2.setAttribute("value", schemeauthority); 
			
			var deleteIcon     = document.createElement('IMG');
			deleteIcon.setAttribute('src', '<%=basePath%>PensionView/images/cancelIcon.gif');
			deleteIcon.onclick = function(){
				removeWthDrwls(tr);
			}
			
		    var space1=document.createTextNode('  ');
		    var space2=document.createTextNode('  ');
		    
			table.appendChild(tr);
			tr.appendChild(td1);
			tr.appendChild(td2);
			td1.appendChild(inp1);
			td2.appendChild(inp2);
			td2.appendChild(space1);
			td2.appendChild(deleteIcon);
		
		}
		
		
	}
	}
	
  
}
function loadDocument(){   
	//alert('This is loadDocument Dtls');
	var documentInfo='<%=adlPensionInfo.getDocumentInfo()%>';
	var tempSchemeInfo = new Array();
	
	var schemeno,schemeauthority,schemeSrno,schemeCntr;
	var tempSchemeInfo1 = documentInfo.split(':');

	if(tempSchemeInfo1!=0){
		var table = document.getElementById('documentenclose');
		for(var i=0;i<tempSchemeInfo1.length-1;i++){
			var documentData=tempSchemeInfo1[i];
			docueclsedreclength++;           
	
		    var tr    = document.createElement('TR');
			var td1   = document.createElement('TD');
			var td2   = document.createElement('TD');
			var inp  = document.createElement('INPUT');
			
			inp.setAttribute("Name", "documentattname");
			inp.setAttribute("id", "documentattname"+docueclsedreclength); 
			inp.setAttribute("size", "75"); 
			inp.setAttribute("maxlength", "75"); 
			inp.setAttribute("value", documentData); 
			var deleteIcon     = document.createElement('IMG');
			deleteIcon.setAttribute('src', '<%=basePath%>PensionView/images/cancelIcon.gif');
			deleteIcon.onclick = function(){
				removeDocuments(tr);
			}
			
		    var space1=document.createTextNode('  ');
		    var space2=document.createTextNode('  ');
			table.appendChild(tr);
			tr.appendChild(td1);
			tr.appendChild(td2);
			td1.appendChild(inp);
			td2.appendChild(space1);
			td2.appendChild(deleteIcon);
		
		
		}
	}
	
  
}
function createList(sortedDeletList){
	var sortedltdid,lastdeletedId;
	//alert('createList===Before deleted list'+sortedDeletList+"======reclength====="+reclength);
	for(var j=1;j<=reclength;j++){
	  	if(sortedDeletList.length!=0){
	  		for(var sndlst=0;sndlst<sortedDeletList.length;sndlst++){
			sortedltdid=sortedDeletList[sndlst];
			lastdeletedId=sortedltdid;
			if(sortedltdid==j){
				sortedDeletList.splice(sndlst,1);
				//alert('createList===after deleted list'+sortedDeletList);
				break;
			}else{
				finalJSList.push(j);
			}
		}
	  	}else{
	  		//alert('last deleted Id'+lastdeletedId);
	  		if(lastdeletedId!=reclength){
	  			finalJSList.push(j);
	  		}
	  		
	  	}
		
	}
	return removeDuplicateElement(finalJSList);
}


function validateSchemeDtls(reclength)
{
   
	
	
	if(document.getElementById("schemecertino"+reclength).value==''){
	  alert('Please Enter Scheme No');
	  document.getElementById("schemecertino"+reclength).focus();
	  return false;
	}	
		
	if(document.getElementById("schemecertiauthority"+reclength).value==''){
	  alert('Please enter Scheme Authority');
	  document.getElementById("schemecertiauthority"+reclength).focus();
	  return false;
	}	 
	
	return true;
}

function findIndexDigits(str){
	var index=0;
	for(var i=0;i<str.length;i++){
		if(str.charAt(i)>='0' && str.charAt(i)<='9'){
			
			index=i;
			break;
		}
	}
	return index;
}
function removeWthDrwls(tr){
 	if(tr.hasChildNodes()==true){
 		var oChildNodes = tr.childNodes;	
 		var deleteTagID,deletedNodeId,childNodesLength;
 		childNodesLength=oChildNodes.length;
 		for(var i=0;i<childNodesLength;i++){
 			deleteTagID=oChildNodes[i].lastChild.id;
 			if(deleteTagID!=''){
	 			//alert('deleted id'+deleteTagID);
 				break;
 			}
 		}
 		deletedNodeId=deleteTagID.substring(findIndexDigits(deleteTagID),deleteTagID.length);
 		//alert('deletedNodeId'+deletedNodeId);
 		if(deleteID==''){
 			deleteID=deletedNodeId;
 		}else{
 			deleteID=deleteID+','+deletedNodeId;
 		}
 	}
 
	tr.parentNode.removeChild(tr);
}

function charsCapsNumOnly(){
	
	if(event.keyCode >=97 || event.keyCode<=122)    
      event.keyCode = event.keyCode - 32;
    if(((event.keyCode >=65 && event.keyCode<=90))||(event.keyCode==32)||
                                (event.keyCode==46)||(event.keyCode==44))  
      event.keyCode = event.keyCode;
    else
     event.keyCode=0;
}
	function numOrdA(a, b){ return (a-b); }

 function removeDuplicateElement(arrayName)
 {
        var newArray=new Array();
        label:for(var i=0; i<arrayName.length;i++ )
        {  
          for(var j=0; j<newArray.length;j++ )
          {
            if(newArray[j]==arrayName[i]) 
            continue label;
          }
          newArray[newArray.length] = arrayName[i];
        }
        return newArray;
}

	 function updatePers(index,totalData){
	 	var finaldltdID,finalDocDltdID;
	 	var schemeinfo='',documentInfo='';
		var strlen=reclength;
		var deletedSortedList=new Array();
		var deletedDocSortedList=new Array();
		
		if(deleteID!=''){
			var deletedList=deleteID.split(',');  
			deletedSortedList=deletedList.sort(numOrdA);
   			var afterDeletedList=createList(deletedSortedList);	
 		}else{
 			//alert(deletedSortedList);
			var afterDeletedList=createList(deletedSortedList);	
		}
		
		 if(afterDeletedList.length!=0){
		
			for(var i=0;i<afterDeletedList.length;i++){	 	 
				finaldltdID=afterDeletedList[i];
				if(reclength>=1){
				    if(validateSchemeDtls(reclength)!=true){
						return false;
				    }
			    }
		       	schemeinfo+=document.getElementById("schemecertino"+finaldltdID).value+"#"+document.getElementById("schemecertiauthority"+finaldltdID).value+":";	
					
	    	}
	    	}
	  //  alert(schemeinfo);
	    var schmeAfterencode=encodeURIComponent(schemeinfo);
	  // alert(schmeAfterencode);
	    
   		if(docueclseddeleteID!=''){
			var deletedList1=docueclseddeleteID.split(',');  
			deletedDocSortedList=deletedList1.sort(numOrdA);
   			var afterDeletedList1=createDocList(deletedDocSortedList);	
 		}else{
			var afterDeletedList1=createDocList(deletedDocSortedList);	
		}
		
		 if(afterDeletedList1.length!=0){
			for(var i=0;i<afterDeletedList1.length;i++){	 	 
				finalDocDltdID=afterDeletedList1[i];
				//alert(finalDocDltdID);
				if(reclength>=1){
				    if(validateDocumentDtls(docueclsedreclength)!=true){
						return false;
				    }
			    }
		       	documentInfo+=document.getElementById("documentattname"+finalDocDltdID).value+":";	
					
	    	}
	    	}
	     
	     var documentAfterencode=encodeURIComponent(documentInfo);
	     //alert(documentAfterencode);	
	    	
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
   		 if(document.forms[0].height_feet.value!="" && document.forms[0].height_inches.value==""){
   		  alert("Please Enter Height With inches");
   		  document.forms[0].height_inches.focus();
   		  return false;
   		  }
	 	if(document.forms[0].height_feet.value=="" && document.forms[0].height_inches.value!=""){
   		  alert("Please Enter Height With Feet");
   		  document.forms[0].height_inches.focus();
   		  return false;
   		  }
   		  
   		 
			var frmSubmitdt=document.forms[0].form10DSubmitDate;
			if (isDate(frmSubmitdt.value)==false){
				frmSubmitdt.focus();
				return false
			}
  
	   	
  	   	  if(!document.forms[0].pposanctionorderdate.value==""){
  	   	 	var sanctionorderdate=document.forms[0].pposanctionorderdate;
			if (isDate(sanctionorderdate.value)==false){
				sanctionorderdate.focus();
				return false
			}
  	   	 
  	   	  }
	 	  var quantumOptionVal=document.forms[0].quantum_option.options[document.forms[0].quantum_option.selectedIndex].value;
	 	  var rtrnOptionVal=document.forms[0].select_return_captial.options[document.forms[0].select_return_captial.selectedIndex].value;
	 	  //alert('quantumOptionVal'+quantumOptionVal+'rtrnOptionVal'+rtrnOptionVal);
   			if((quantumOptionVal=='Y' ||quantumOptionVal=='N') && document.forms[0].quantumamount.value==''){
   				alert("Quantum Amount Should be Enter");
				document.forms[0].quantumamount.focus();
				return false;
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

  	    
  	   
  	    if(!document.forms[0].emailId.value==""){
  	     	if(ValidateEMail(document.forms[0].emailId.value)==false){
				document.forms[0].emailId.focus();
				return false;}
  	   		}
   		 var ndob1=document.getElementsByName("Ndob");
   		 var nname=document.getElementsByName("Nname");
   		 var gaddress =document.getElementsByName("gaddress");
   		 var guardianname= document.getElementsByName("guardianname");
   		 var optionFlag=document.getElementsByName("option_flag");
   		 
   		 var k;
   		 var chkOptionFlag=0,cntrFlag=0;
   		  for(k=0;k<ndob1.length;k++){
   		  	 var val1=convert_date(ndob1[k]);
   		     if(val1==false)
   		     {
   		     return false;
   		     }
   		  			if(ndob1[k].value!=""){
   		  				var s=ndob1[k].value; 
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
			  		 if(nname[k].value=="") {
          				  nname[k].focus();
            			 alert("Please Enter Nominee Name");
             			 return false;
          	 		}
          	 	//	alert("guardianname length "+guardianname[k].length)
					if(years<18 && guardianname[k].value=="") {
          				  guardianname[k].focus();
            			  alert("Please Enter Guardian Name");
             			 return false;
          	 		}
          	 	
          	 		if(optionFlag[k].value=='Y'){
          	 			chkOptionFlag++;
          	 		}
          	 		cntrFlag++;
          	 		if(chkOptionFlag<1 && ndob1.length-1==cntrFlag && rtrnOptionVal=='Y'){
          	 			 alert("Should be select any nominee name for Return Captial");
          	 		  	return false;
          	 		}
          	 		if(years<18 && gaddress[k].value=="") {
          				  gaddress[k].focus();
            			  alert("Please Enter Guardian Address");
             			 return false;
          	 		}
         
          }
          }
      var nomineeName=document.getElementsByName("Nname");      
   
     document.forms[0].action="<%=basePath%>psearch?method=form10dUpdate&startIndex="+index+"&totalData="+totalData+"&frm_schemeinfo="+schmeAfterencode+"&frm_documentinfo="+documentInfo;
   
     document.forms[0].method="post";
	 document.forms[0].submit();
   		 }
   		 
   	 function getPensionNumber() {
	var date1=document.forms[0].dateofbirth;
   	var val1=convert_date(date1);
   	var empName =document.forms[0].empName.value;
	var cpfacno =document.forms[0].cpfacnoNew.value;
	var uniquenumber = cpfacno.substring(0, cpfacno.length);
	var formEmpName=document.forms[0].empName.value;
	var newFormEmpName="";
	var finalKey="";
	var isNotSpace=false;
	var dateofbirth =document.forms[0].dateofbirth.value;
	var withoutSlash=replaceAll(dateofbirth,"/","***");
	var withoutDt_array=withoutSlash.split("***");
	var day=withoutDt_array[0];
	var month=convert_month(withoutDt_array[1]);
	var year=withoutDt_array[2].substring(2,4);
	
	var finalDay=day+month+year;
	//alert("finaday "+finalDay);
	
	if(formEmpName.indexOf(" ")!=-1){
		newFormEmpName=replaceAll(formEmpName," ","***");
	}else if(formEmpName.indexOf(".")!=-1){
		newFormEmpName=replaceAll(formEmpName,".","***");
	}else{
		isNotSpace=true;
	}


	if(isNotSpace==true){
		finalKey=formEmpName.substring(0,2);
	}else{
    //alert('new Format'+newFormEmpName);
	var mytool_array=newFormEmpName.split("***");
	//alert(mytool_array);
	
	
	finalKey=mytool_array[0].substring(0,1);
	//alert(mytool_array.length);
	finalKey=finalKey+mytool_array[mytool_array.length-1].substring(0,1);
	}
	var finalName=finalDay+finalKey+uniquenumber;
   	document.forms[0].pensionNumber.value=finalName;
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
   		   		
   		 
		 function deleteFamilyDetails(familyMemberName,rowid)
		{ 
				createXMLHttpRequest();	
			var url ="<%=basePath%>search1?method=deleteFamilyDetails&familyMemberName="+familyMemberName+"&rowid="+rowid;
			//alert("URL "+url);
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
			       document.forms[0].desegnation.value=designation
				  }
				}
			}
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

	<body class="BodyBackground" onload="document.forms[0].pfid.focus();hide();swap();">

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
									Form-10D Application Form&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center" class="label">
									<%CommonUtil commonUtil = new CommonUtil();
				if (request.getAttribute("ArrearInfo") != null) {
					arrearInfoString1 = (String) request
							.getAttribute("ArrearInfo");
					request.setAttribute("ArrearInfo", arrearInfoString1);
					System.out.println("arrearInfoString1****  "
							+ arrearInfoString1);

				}%>
									<%if (request.getAttribute("recordExist") != null) {%>
									<font color="red"><%=request.getAttribute("recordExist")%></font>
									<%}%>
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%">


									<table align="center">



										<tr>
											<td class="label">
												<b>PF ID:</b>
											</td>
											<td>
												<input type="text" name="pfid" readonly="readonly" maxlength="35" tabindex="1" value='<%=bean1.getPfid()%>' />

											</td>

											<td class="label">
												Airport Code:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="airPortCode" maxlength="25" tabindex="2" value='<%= bean1.getStation()%>' readonly="readonly" />

											</td>
										</tr>
										<input type="hidden" name="setEmpSerialNo" readonly="true" maxlength="35" value='<%=bean1.getEmpSerialNo()%>' />
										<tr>
											<td class="label">
												Old CPFACC.NO:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="cpfacnoNew" maxlength="25" tabindex="3" readonly="readonly" onblur="charsCapsNumOnly();getPensionNumber();" value='<%=bean1.getCpfAcNo()%>' />
											</td>
											<!-- 	<input type="text" name="cpfacnoNew" maxlength="25" tabindex="1" onBlur="charsCapsNumOnly();getPensionNumber();" value='<%=bean1.getCpfAcNo()%>'>-->
											<td class="label">
												Employee Code:
											</td>
											<td>
												<input type="text" name="employeeCode" readonly="readonly" maxlength="20" tabindex="4" value='<%=bean1.getEmpNumber()%>' onkeyup="return limitlength(this, 20)" />
											</td>

											<input type="hidden" name="airportSerialNumber" value='<%=bean1.getAirportSerialNumber()%>' />
											<input type="hidden" name="region" value='<%=bean1.getRegion()%>' />
											<input type="hidden" name="editFrom" value='<%=editFrom%>' />
										</tr>
										<tr>


											<td class="label">
												Employee Name:<font color="red">&nbsp;*</font>
											</td>

											<td>
												<input type="text" name="empName" readonly="readonly" maxlength="50" tabindex="5" onblur="getPensionNumber();" value='<%=bean1.getEmpName()%>' onkeyup="return limitlength(this,50)" />
											</td>
											<td class="label">
												Sex:
											</td>

											<td>
												<select name="sex" tabindex="6">
													<%String sex = bean1.getSex().trim();%>
													<option value="">
														[Select One]
													</option>
													<option value="M" <%if(sex.equals("M")){ out.println("selected");}%>='<%if(sex.equals("M")){ out.println("selected");}%>'>
														Male
													</option>
													<option value="F" <%if(sex.equals("F")){ out.println("selected");}%>='<%if(sex.equals("F")){ out.println("selected");}%>'>
														Female
													</option>

												</select>


											</td>
										</tr>
										<tr>
											<td class="label">
												Designation:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="desegnation" readonly="readonly" tabindex="7" value='<%=bean1.getDesegnation()%>' onkeyup="return limitlength(this, 20)" />

											</td>
											<td class="label">
												Father's / Husband's Name:
											</td>
											<td>
												<select name="select_fhname" tabindex="8" style="width:64px">
													<option value='F' <%if(bean1.getFhFlag().trim().equals("F")){ out.println("selected");}%>='<%if(bean1.getFhFlag().trim().equals("F")){ out.println("selected");}%>'>
														Father
													</option>
													<option value='H' <%if(bean1.getFhFlag().trim().equals("H")){ out.println("selected");}%>='<%if(bean1.getFhFlag().trim().equals("H")){ out.println("selected");}%>'>
														Husband
													</option>
												</select>
												<input type="text" name="fhname" maxlength="50" tabindex="9" value='<%=bean1.getFhName()%>' onkeyup="return limitlength(this, 50)" />
											</td>


										</tr>
										<tr>
											<td class="label">
												Date of Birth:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="dateofbirth" tabindex="10" readonly="readonly" value='<%=bean1.getDateofBirth()%>' onkeyup="return limitlength(this, 20)" />

											</td>
											<td class="label">
												Date of Joining:
											</td>
											<td>
												<input type="text" name="dateofjoining" tabindex="11" readonly="readonly" value='<%=bean1.getDateofJoining()%>' onkeyup="return limitlength(this, 20)" />

											</td>
										</tr>
										<tr>



										</tr>

										<tr>
											<td class="label">
												Reason of Leaving Service:
											</td>
											<td>
												<input type="text" name="reason" tabindex="12" readonly="readonly" value='<%=bean1.getSeperationReason().trim()%>' />
												<%if (bean1.getSeperationReason().trim().equals("Other")) {%>
												/
												<input type="text" name="Other" value='<%=bean1.getOtherReason()%>' id="myText" style="display:none;" tabindex="17" onclick="swapback()" />
												<%}%>
											</td>
											<td class="label">
												Date of Leaving Service:
											</td>
											<td>
												<input type="text" name="seperationDate" tabindex="13" readonly="readonly" value='<%=bean1.getDateofSeperationDate()%>' onkeyup="return limitlength(this, 20)" />
												<a href="javascript:show_calendar('forms[0].seperationDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>
											</td>
										</tr>

										<tr>
											<td class="label">
												Pension Option Received:
											</td>

											<td>
												<input type="text" name="wetherOption" value='<%=bean1.getWetherOption().trim()%>' tabindex="14" readonly="readonly" />

											</td>

											<td class="label">
												Email Id:
											</td>
											<td>
												<input type="text" name="emailId" maxlength="50" onkeyup="return limitlength(this,50)" value='<%=bean1.getEmailId()%>' tabindex="15" />
											</td>
										</tr>
										<tr>

											<td class="label">
												Permanent Address:
											</td>

											<td>
												<TEXTAREA name="paddress" size="150" maxlength="150" tabindex="16" cols="" rows=""><%=bean1.getPermanentAddress()%></TEXTAREA>
											</td>
											<td class="label">
												Temporary Address:
											</td>
											<td>
												<TEXTAREA name="taddress" size="150" maxlength="150" tabindex="17" cols="" rows=""><%=bean1.getTemporatyAddress()%></TEXTAREA>


											</td>
										</tr>
										<tr>

											<td class="label">
												Phone No:
											</td>

											<td>
												<input type="text" name="perPhoneNumber" value='<%=bean1.getPhoneNumber().trim()%>' maxlength="21" size="15" tabindex="18"  />
											</td>
											
										</tr>
										<tr>

											<td class="label">
												Current posting:
											</td>

											<td>
													<input type="text" name="currentPostingStation" value='<%=adlPensionInfo.getCurrentPostingStation().trim()%>' tabindex="19"  />
											</td>
											
										</tr>
										<tr>

											<td class="label">
												Date of submission of Form10-D To RPFC:
											</td>

											<td>
													<input type="text" name="form10DSubmitDate" value="<%=adlPensionInfo.getForm10DSubmsionDate()%>" tabindex="20" size="11" maxlength="11"/>
														<a href="javascript:show_calendarFormat('forms[0].form10DSubmitDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt=""  /><label style="HighlightData">(Format:dd/MM/yyyy)</label></a>
											</td>
													<td class="label">
												RPFC Station:
											</td>

											<td>
													<input type="text" name="rpfcStation" value="<%=adlPensionInfo.getForm10DRpfcStation()%>" tabindex="21" />
											</td>
										</tr>
										<tr>

											<td class="label">
												PPO/Sanction order NO:
											</td>

											<td>
													<input type="text" name="pposanctionorderno" value="<%=adlPensionInfo.getSanctionOrderNo()%>" tabindex="22" />
											</td>
												<td class="label">
												PPO/Sanction order Dated:
											</td>

											<td>
													<input type="text" name="pposanctionorderdate" value="<%=adlPensionInfo.getSanctionOrderDate()%>" tabindex="23" />
													<a href="javascript:show_calendarFormat('forms[0].pposanctionorderdate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /><label style="HighlightData">(Format:dd/MM/yyyy)</label>
											</td>
											
										</tr>
										<tr>

											<td class="label">
												Nationality:
											</td>

											<td>
												<input type="text" name="nationality" tabindex="24" onkeyup="return limitlength(this, 20)" value="<%=bean1.getNationality()%>" />
											</td>
											<td class="label">
												Height:
											</td>
											<%
												String height="",inches="";
												if(!bean1.getHeightWithInches().equals("")){
													height=bean1.getHeightWithInches().substring(0,bean1.getHeightWithInches().indexOf("."));
													inches=bean1.getHeightWithInches().substring(bean1.getHeightWithInches().indexOf(".")+1,bean1.getHeightWithInches().length());
												}else{
													height="";
													inches="";
												}
											%>
											
											<td>
												<input type="text" name="height_feet" tabindex="25" onkeyup="return limitlength(this, 6)" value="<%=height%>" maxlength="6" size="6"/>(feet)<input type="text" name="height_inches" tabindex="20" onkeyup="return limitlength(this, 6)" value="<%=inches%>" maxlength="6" size="6"/>(inches)


											</td>
										</tr>
										<tr>

											<td class="label">
												In Case of Reduced Pension (Early Pension):
											</td>

											<td>
												<select name="early_Pension_flag" style="width:45px"  tabindex="26">
													<option value="Y" <%if(adlPensionInfo.getEarlyPensionTaken().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getEarlyPensionTaken().equals("Y")){ out.println("selected");}%>">
														Yes
													</option>
													<option value="N" <%if(adlPensionInfo.getEarlyPensionTaken().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getEarlyPensionTaken().equals("N")){ out.println("selected");}%>">
														No
													</option>

												</select>
											</td>
											<td class="label">
												Date of Option for Commencement of Pension:
											</td>

											<td>
												<input type="text" name="earlypension_date" tabindex="27" onkeyup="return limitlength(this, 20)" value="<%=adlPensionInfo.getEarlyPensionDate()%>" />
												<a href="javascript:show_calendar('forms[0].earlypension_date');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>
											</td>



										</tr>
										<tr>
											<td class="label">
												Date of submission of Form-10D
											</td>
											<td>
												<input type="text" name="earlypension_form10Ddate" tabindex="28" onkeyup="return limitlength(this, 20)" value="<%=adlPensionInfo.getEpForm10DSubDate()%>" />
												<a href="javascript:show_calendar('forms[0].earlypension_form10Ddate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>
											</td>
										</tr>
										<tr>
											<td class="label">
												Option for Commutation of 1/3 of Quantum
											</td>
											<td>
												<select name="quantum_option" tabindex="29">
													<option value="Y" <%if(adlPensionInfo.getQuantum1By3Option().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getQuantum1By3Option().equals("Y")){ out.println("selected");}%>">
														Yes
													</option>
													<option value="N" <%if(adlPensionInfo.getQuantum1By3Option().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getQuantum1By3Option().equals("N")){ out.println("selected");}%>">
														No
													</option>

												</select>
											</td>
											<td class="label">
												Amount for Commutation of 1/3 of Quantum
											</td>
											<td>
												<input type="text" name="quantumamount" value="<%=adlPensionInfo.getQuantum1By3Amount()%>" onkeyup="return limitlength(this,50)" tabindex="30" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Option of Return Capital
											</td>
											<td>
												<select name="select_return_captial" tabindex="31">
													<option value="Y" <%if(adlPensionInfo.getOptionReturnCaptial().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getOptionReturnCaptial().equals("Y")){ out.println("selected");}%>">
														Yes
													</option>
													<option value="N" <%if(adlPensionInfo.getOptionReturnCaptial().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getOptionReturnCaptial().equals("N")){ out.println("selected");}%>">
														No
													</option>

												</select>
											</td>
											<td class="label">
												Date Of Death of Member
											</td>
											<td>
												<input type="text" name="deathofmember_date" tabindex="32" onkeyup="return limitlength(this, 20)" value="<%=adlPensionInfo.getMemberDeathDate()%>" />
												<a href="javascript:show_calendar('forms[0].deathofmember_date');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>
											</td>
										</tr>
										<tr>
											<td colspan="4" classe="ScreenSubHeading">
												Details of Saving Account
											</td>
										</tr>
										<tr>
											<td class="label">
												Saving Account Opened At
											</td>
											<td>
												<select name="select_saving_account" tabindex="33">
													<option value="B" <%if(adlPensionInfo.getPaymentInfoType().equals("B")){ out.println("selected");}%>="<%if(adlPensionInfo.getPaymentInfoType().equals("B")){ out.println("selected");}%>">
														Bank
													</option>
													<option value="P" <%if(adlPensionInfo.getPaymentInfoType().equals("P")){ out.println("selected");}%>="<%if(adlPensionInfo.getPaymentInfoType().equals("P")){ out.println("selected");}%>">
														Post Office
													</option>

												</select>
											</td>
											<td class="label">
												Name of the the Branch
											</td>
											<td>
												<input type="text" name="savingbankingname" onkeyup="return limitlength(this,50)" tabindex="34" value="<%=adlPensionInfo.getNameofPaymentBranch()%>" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Full post all address
											</td>
											<td>
												<TEXTAREA name="savingaccpostaladdr" size="200" maxlength="200" tabindex="35" cols="" rows=""><%=adlPensionInfo.getAddressofPayementBranch()%></TEXTAREA>
											</td>
											<td class="label">
												Pincode
											</td>
											<td>
												<input type="text" name="savingbankpincode" size="6"  maxlength="6" onkeyup="return limitlength(this,6)" tabindex="36" value="<%=adlPensionInfo.getPaymentBranchPincode()%>" />
											</td>
										</tr>

										<div id="divNomineeHead">
											<table align="center" width="100%" cellpadding="2" cellspacing="2">



												<tr>
													<td class="label" width="13%" align="center">
														Name
													</td>
													<td class="label" width="13%" align="center">
														Address
													</td>
													<td class="label" width="16%" align="center">
														Dateof Birth
													</td>
													<td class="label" width="18%" align="center">
														Relation with Member
													</td>
													<td class="label" width="12%" nowrap="nowrap">
														Name of Guardian
													</td>
													<td class="label" width="13%" nowrap="nowrap">
														Address of Guardian
													</td>
													<td class="label" width="11%" nowrap="nowrap">
														Account No
													</td>
													<td class="label" width="10%" nowrap="nowrap">
														Return
														<br />
														of Captial
													</td>
													<td width="20%">
														&nbsp;<b><img alt="Add New Record" src="<%=basePath%>PensionView/images/addIcon.gif" onclick="callNominee();dispOff()" tabindex="38" /></b>
													</td>


												</tr>

												<%int count1 = 0;
				String nomineeRows = bean1.getNomineeRow();
				String tempData = "", rowid = "";
				;
				String tempInfo[] = null;
				ArrayList nomineeList = commonUtil.getTheList(nomineeRows,
						"***");

				String tempInfo1[] = null;
				String tempData1 = "";
				String nomineeAddress = "", nomineeRetOptFlag = "", nomineeSavingAcc = "", nomineeDob = "", nomineeRelation = "", nameofGuardian = "", totalShare = "", addressofGuardian = "";
				for (int i = 0; i < nomineeList.size(); i++) {
					count1++;
					tempData = nomineeList.get(i).toString();
					tempInfo = tempData.split("@");
					String nomineeName = tempInfo[0];
					nomineeAddress = tempInfo[1];
					nomineeDob = tempInfo[2];
					nomineeRelation = tempInfo[3];
					nameofGuardian = tempInfo[4];
					addressofGuardian = tempInfo[5];
					totalShare = tempInfo[6];
					nomineeRetOptFlag = tempInfo[7];
					nomineeSavingAcc = tempInfo[8];
					rowid = tempInfo[9];
					if (nomineeAddress.equals("xxx")) {
						nomineeAddress = "";
					}

					if (nomineeDob.equals("xxx")) {
						nomineeDob = "";
					}
					if (nomineeRelation.equals("xxx")) {
						nomineeRelation = "";
					}

					if (nameofGuardian.equals("xxx")) {
						nameofGuardian = "";
					}
					if (addressofGuardian.equals("xxx")) {
						addressofGuardian = "";
					}
					if (totalShare.equals("xxx")) {
						totalShare = "";
					}
					if (nomineeRetOptFlag.equals("xxx")) {
						nomineeRetOptFlag = "N";
					}
					if (nomineeSavingAcc.equals("xxx")) {
						nomineeSavingAcc = "";
					}

					%>
												<tr id="name<%=i%>">
													<td width="13%">
														<input type="text" size="18" name="Nname" maxlength="50" value='<%=nomineeName%>' onblur="dispOff()" tabindex="36" />
														<input type="hidden" name="empOldNname" value='<%=nomineeName%>' />
													</td>
													<td width="13%">
														<input type="text" size="16" name="Naddress" maxlength="150" value='<%=nomineeAddress%>' tabindex="37" />
													</td>
													<input type="hidden" name="nomineerowid" value='<%=rowid%>' />
													<td width="15%">
														<input type="text" size="16" name="Ndob" value='<%=nomineeDob%>' tabindex="38" />
														<a href="javascript:show_calendar('forms[0].Ndob');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>
													</td>
													<td width="15%" nowrap="nowrap">

														<select name="Nrelation" tabindex="39" style="width:106px">
															<option value="">
																[Select One]
															</option>
															<option value='SPOUSE' <%if(nomineeRelation.equals("SPOUSE")){ out.println("selected");}%>='<%if(nomineeRelation.equals("SPOUSE")){ out.println("selected");}%>'>
																SPOUSE
															</option>
															<option value='SON' <%if(nomineeRelation.equals("SON")){ out.println("selected");}%>='<%if(nomineeRelation.equals("SON")){ out.println("selected");}%>'>
																SON
															</option>
															<option value='DAUGHTER' <%if(nomineeRelation.equals("DAUGHTER")){ out.println("selected");}%>='<%if(nomineeRelation.equals("DAUGHTER")){ out.println("selected");}%>'>
																DAUGHTER
															</option>
															<option value='MOTHER' <%if(nomineeRelation.equals("MOTHER")){ out.println("selected");}%>='<%if(nomineeRelation.equals("MOTHER")){ out.println("selected");}%>'>
																MOTHER
															</option>
															<option value='FATHER' <%if(nomineeRelation.equals("FATHER")){ out.println("selected");}%>='<%if(nomineeRelation.equals("FATHER")){ out.println("selected");}%>'>
																FATHER
															</option>
															<option value='SONS WIDOW' <%if(nomineeRelation.equals("SONS WIDOW")){ out.println("selected");}%>='<%if(nomineeRelation.equals("SONS WIDOW")){ out.println("selected");}%>'>
																SON'S WIDOW
															</option>
															<option value='WIDOWS DAUGHTER' <%if(nomineeRelation.equals("WIDOWS DAUGHTER")){ out.println("selected");}%>='<%if(nomineeRelation.equals("WIDOWS DAUGHTER")){ out.println("selected");}%>'>
																WIDOW'S DAUGHTER
															</option>

															<option value='MOTHER-IN-LOW' <%if(nomineeRelation.equals("MOTHER-IN-LOW")){ out.println("selected");}%>='<%if(nomineeRelation.equals("MOTHER-IN-LOW")){ out.println("selected");}%>'>
																MOTHER-IN-LAW
															</option>
															<option value='FATHER-IN-LOW' <%if(nomineeRelation.equals("FATHER-IN-LOW")){ out.println("selected");}%>='<%if(nomineeRelation.equals("FATHER-IN-LOW")){ out.println("selected");}%>'>
																FATHER-IN-LAW
															</option>
														</select>
													</td>
													<td width="10%">
														<input type="text" size="16" name="guardianname" maxlength="50" value='<%=nameofGuardian%>' tabindex="40" />
													</td>
													<td width="8%">
														<input type="text" size="16" name="gaddress" maxlength="150" value='<%=addressofGuardian%>' tabindex="41" />
													</td>
													<td width="10%">
														<input type="text" size="16" name="saving_acc_no" maxlength="150" value='<%=nomineeSavingAcc%>' tabindex="42" />
													</td>
													<td width="8%">
														<select name="option_flag" style="width:45px" tabindex="43">
															<option value="Y" <%if(nomineeRetOptFlag.equals("Y")){ out.println("selected");}%>="<%if(nomineeRetOptFlag.equals("Y")){ out.println("selected");}%>">
																Yes
															</option>
															<option value="N" <%if(nomineeRetOptFlag.equals("N")){ out.println("selected");}%>="<%if(nomineeRetOptFlag.equals("N")){ out.println("selected");}%>">
																No
															</option>

														</select>
													</td>
													<td>
														&nbsp;<b><img alt="Delete" src="<%=basePath%>PensionView/images/cancelIcon.gif" onclick="deleteNomineeDetils('<%=bean1.getCpfAcNo()%>','<%=nomineeName%>','name<%=i%>','<%=rowid%>');" tabindex="27" /></b>
													</td>


												</tr>
												<%}

				%>
		</table>
										</div>
										<div id="divNominee1">
											<table align="center" width="100%" cellpadding="2" cellspacing="2">
												<input type="hidden" name="empOldNname" value="" />
												<input type="hidden" name="nomineerowid" />
												<tr>
													<td width="13%">
														<input type="text" size="18" name="Nname" maxlength="50" tabindex="37" onblur="dispOff()" />
													</td>

													<td width="13%">

														<input type="text" size="16" name="Naddress" maxlength="150" tabindex="38" />
													</td>

													<td width="15%">

														<input type="text" size="16" name="Ndob" tabindex="39" />
														<a href="#" name="cal1" onclick="javascript:call_calender('forms[0].Ndob')"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="" /></a>&nbsp;

													</td>
													<td width="15%">

														<select name="Nrelation" tabindex="40" style="width:106px">
															<option value="">
																[Select One]
															</option>
															<option value='SPOUSE'>
																SPOUSE
															</option>
															<option value='SON'>
																SON
															</option>
															<option value='DAUGHTER'>
																DAUGHTER
															</option>
															<option value='MOTHER'>
																MOTHER
															</option>
															<option value='FATHER'>
																FATHER
															</option>
															<option value='SONS WIDOW'>
																SON'S WIDOW
															</option>
															<option value='WIDOWS DAUGHTER'>
																WIDOW'S DAUGHTER
															</option>
															<option value='MOTHER-IN-LOW'>
																MOTHER-IN-LAW
															</option>
															<option value='FATHER-IN-LOW'>
																FATHER-IN-LAW
															</option>

														</select>

													</td>
													<td width="10%">

														<input type="text" size="16" name="guardianname" maxlength="50" tabindex="41" />
													</td>
													<td width="8%">

														<input type="text" size="16" name="gaddress" maxlength="150" tabindex="42" />

													</td>
													<td width="10%">

														<input type="text" size="16" name="saving_acc_no" maxlength="50" tabindex="43" />
													</td>
													<td width="8%">
														<select name="option_flag" style="width:45px">
																<option value="Y">
																	Yes
																</option>
																<option value="N" selected="selected">
																	No
																</option>

															</select>
														

													</td>

													

												</tr>
											</table>
										</div>

										<div id="divNominee2">
										</div>

										<input type="hidden" name="flagData" value='<%=request.getAttribute("flag")%>' />

										
										<tr>
											<td>
												<table cellpadding="2" align="center" cellspacing="2" border="0" width="80%">
													<tr>
														<td class="label" colspan="4">
															If the claim is preferred by nominee,indicates his/her
														</td>
													</tr>
													<tr>
														<td class="label">
															Name
														</td>
														<td>
															<input type="text" name="claimRefNominee" value="<%=adlPensionInfo.getClaimNomineeRefName()%>"  tabindex="44"/>
														</td>


														<td class="label">
															Relationship with the deceased Member
														</td>
														<td>
															<input type="text" name="claimRefNomineeRel" value="<%=adlPensionInfo.getClaimNomineeRefRelation()%>" tabindex="45"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
											<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>
												<table cellpadding="2" align="center" cellspacing="2" border="0" width="80%">
													<tr>
														<td class="label" colspan="4">
															Details of Scheme Certificate
														</td>
													</tr>
													<tr>
														<td class="label">
															Scheme certificate received & enclosed
														</td>
														<td>
															<select name="scheme_cert_rec" tabindex="46">
																<option value="Y" <%if(adlPensionInfo.getSchemeCertificateRecEncl().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getSchemeCertificateRecEncl().equals("Y")){ out.println("selected");}%>">
																	Yes
																</option>
																<option value="N" <%if(adlPensionInfo.getSchemeCertificateRecEncl().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getSchemeCertificateRecEncl().equals("N")){ out.println("selected");}%>">
																	No
																</option>

															</select>
														</td>

														<td class="label">
															Already in possession of the Member,if any
														</td>
														<td>
															<select name="possession_member" tabindex="47">
																<option value="Y" <%if(adlPensionInfo.getPossesionMember().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getPossesionMember().equals("Y")){ out.println("selected");}%>">
																	Yes
																</option>
																<option value="N" <%if(adlPensionInfo.getPossesionMember().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getPossesionMember().equals("N")){ out.println("selected");}%>">
																	No
																</option>

															</select>
														</td>
													</tr>
													<tr>
														<td class="label">
															Full post all address
														</td>
														<td>
															<TEXTAREA name="nomineePostalAddr" size="150" maxlength="150" tabindex="48" cols="" rows="" value="<%=adlPensionInfo.getNomineePostalAddrss()%>"></TEXTAREA>
														</td>
														<td class="label">
															Pincode
														</td>
														<td>
															<input type="text" name="nomineePostalPincode" tabindex="49" value="<%=adlPensionInfo.getNomineePincode()%>" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>
												<table cellpadding="3" align="center" cellspacing="3" width="80%">
													<tbody id="schemecontrs">
														<tr>
															<td class="label" align="center">
																Scheme Certificate Control No.
															</td>
															<td class="label" align="center">
																Authority who issue the Scheme Certificate
															</td>
															<td>
																<img alt="Add Record" src="<%=basePath%>PensionView/images/addIcon.gif" onclick="return callScheme()" tabindex="38" />
															</td>
														</tr>
														<tr>
															<!-- <td><input type="text"  size="45" maxlength=45 name="schemecertino" id="schemecertino"/></td>
												<td><input type="text" size="65" maxlenght="65" name="schemecertiauthority" id="schemecertiauthority"/></td>
												-->
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>
												<table cellpadding="2" align="center" cellspacing="2" width="90%">

													<tr>
														<td class="label" align="center">
															If Pension is being drawn Under E.P.S.,1995
														</td>
														<td>
															<select name="select_pension_drwn_1995" tabindex="51">
																<option value="Y" <%if(adlPensionInfo.getPensionDrwnFrom1995().equals("Y")){ out.println("selected");}%>="<%if(adlPensionInfo.getPensionDrwnFrom1995().equals("Y")){ out.println("selected");}%>">
																	Yes
																</option>
																<option value="N" <%if(adlPensionInfo.getPensionDrwnFrom1995().equals("N")){ out.println("selected");}%>="<%if(adlPensionInfo.getPensionDrwnFrom1995().equals("N")){ out.println("selected");}%>">
																	No
																</option>

															</select>
														</td>
														<td>
															PPO No.issued by
														</td>
														<td>
															<select name="select_ppo_issue_by" tabindex="52">
																<option value="RO" <%if(adlPensionInfo.getPponoIssuedBy().equals("RO")){ out.println("selected");}%>="<%if(adlPensionInfo.getPponoIssuedBy().equals("RO")){ out.println("selected");}%>">
																	RO
																</option>
																<option value="SRO" <%if(adlPensionInfo.getPponoIssuedBy().equals("SRO")){ out.println("selected");}%>="<%if(adlPensionInfo.getPponoIssuedBy().equals("SRO")){ out.println("selected");}%>">
																	SRO
																</option>

															</select>
														</td>
													</tr>
													<tr>

													</tr>

												</table>
											</td>
										</tr>
											<tr>
											<td>&nbsp;</td>
										</tr>
										<tr>
											<td>
												<table cellpadding="0" align="center" cellspacing="0" width="80%">
													<tbody id="documentenclose">
														<tr>
															<td class="label" align="left">
																Documents enclosed(Indicate as Per Instructions)
															</td>

															<td>
																<img alt="Add Record" src="<%=basePath%>PensionView/images/addIcon.gif" onclick="return callDocuments()" tabindex="38" />
															</td>
														</tr>
														<tr>
															<!-- <td><input type="text"  size="45" maxlength=45 name="schemecertino" id="schemecertino"/></td>
												<td><input type="text" size="65" maxlenght="65" name="schemecertiauthority" id="schemecertiauthority"/></td>
												-->
														</tr>
													</tbody>
												</table>
											</td>
										</tr>

										<tr>
											<td>
												<table align="center">
													<tr>
														<td align="center">
															<input type="button" class="btn" value="Update" class="btn" onclick="updatePers('<%=request.getAttribute("startIndex")%>','<%=request.getAttribute("TotalData")%>')" tabindex="46" />
															<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="47" />
															<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="48" />
														</td>
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
			<%}%>
		</form>
	</body>
</html>



