
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.dao.*"%>
<%@ page import="aims.service.AdjCrtnService" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String userId = "";
	String signature= "",wetheroption="",emolumentsmnths="",region="",accessCode="",processedStage ="",form2Status="";
	String adjOBYear ="",grandTotDiffShowFlag="",finlaizedFlag=""; 
	CommonDAO  commonDAO =  new aims.dao.CommonDAO();
	AdjCrtnService adjCrtnService = new AdjCrtnService();
	if(request.getAttribute("finlaizedFlag")!=null){
   	finlaizedFlag=(String)request.getAttribute("finlaizedFlag");
  }
	if(request.getAttribute("grandTotDiffShowFlag")!=null){
   	grandTotDiffShowFlag=(String)request.getAttribute("grandTotDiffShowFlag");
  }
	if(request.getAttribute("reportYear")!=null){
	adjOBYear=(String) request.getAttribute("reportYear");
	}
	  if(request.getAttribute("form2Status")!=null){
   	form2Status=(String)request.getAttribute("form2Status");
  }
 userId = session.getAttribute("userid").toString();
 
  if(request.getAttribute("accessCode")!=null){
   	accessCode=(String)request.getAttribute("accessCode");
  }
	if(accessCode.equals("PE040201")){
	processedStage="Initial";
	}else if(accessCode.equals("PE040202")){
		processedStage="Approved";
	  }
 
 System.out.println("===accessCode=="+accessCode+"===processedStage==="+processedStage+"===adjOBYear==="+adjOBYear);
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
    <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css"/>
     
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css" 
	type="text/css"/>
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
    
    <script type="text/javascript">
    
    var finlaizedFlag='<%=finlaizedFlag%>';
	function showTip(txt,element){
	    var theTip = document.getElementById("spnTip");
			theTip.style.top= GetTop(element);
	   //alert(theTip.style.top);
	    if(txt=='')
	    {
	    txt='--';
	    }
	    theTip.innerHTML=""+txt;
		theTip.style.left= GetLeft(element) - theTip.offsetWidth;
	    
	    theTip.style.visibility = "visible";
	}

function hideTip()
{
	document.getElementById("spnTip").style.visibility = "hidden";
} 
	
function GetTop(elm){

	var  y = 0;
	y = elm.offsetTop;
	elm = elm.offsetParent;
	while(elm != null){
		y = parseInt(y) + parseInt(elm.offsetTop);
		elm = elm.offsetParent;
	}	
	return y;
}
function GetLeft(elm){

	var x = 0;
	x = elm.offsetLeft;
	elm = elm.offsetParent;
	while(elm != null){
		x = parseInt(x) + parseInt(elm.offsetLeft);
		elm = elm.offsetParent;
	}
	
	return x;
}	
 function high(obj)
 	{
	//obj.style.background = 'rgb(220,232,236)';
	}

function low(obj) {
	///obj.style.background='#EFEFEF';	
}

  
  
  function getContribution(monthyear,pfid,emolumentsbox,region,pensionoption,emolmnts)
  {	
 
  createXMLHttpRequest();	
    var emoluments= document.getElementById(emolumentsbox).value;           
   	var url ="" ;
    
   	 url ="<%=basePath%>reportservlet?method=getContributionForAdjCRTN&monthyear="+monthyear+"&emoluments="+emoluments+"&pfid="+pfid+"&wetheroption="+pensionoption+"&region="+region+"&emolumentsmnths="+emolmnts;
  //alert(url);
  	xmlHttp.open("post", url, true);
  	xmlHttp.onreadystatechange = getContributionValue;
  	xmlHttp.send(null);
  }
    function loadadjlog(pensionno,station,finyear,formtype){
  		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		
		var url="<%=basePath%>search1?method=getEmolumentslog&pfId="+pensionno+"&airportcode="+station+"&dispYear="+finyear+"&frmName="+formtype;
		
  		wind1 = window.open(url,"AdjLog","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		winOpened = true;
		wind1.window.focus();
  	}
  function getContributionValue()
  {
  	if(xmlHttp.readyState ==4)
  	{ 	if(xmlHttp.status == 200)
  		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
  		
  		  if(stype.length==0){
  		 	var obj1 = document.getElementById("pensionContribution");
  		 	obj1.options.length=0; 
  		  
  		  }else{
  		    var pensionContribution = getNodeValue(stype[0],'pensionContribution'); 
  		     document.forms[0].pensionContriOldVal.value=pensionContribution;	
  		       alert(document.forms[0].pensionContriOldVal.value+'=----');
  		  		   
  		  }
  		}
  	}
  }
  var prevPensionContri='',prevemoluments='',prevepf='',prevvpf='',prevprinciple='',previntrst='',prevadv='',prevpfwsub='',prevpfwcontri,prevaaipf='';
  var editFlag="false";
function editEmoluments(monthyear,cpfaccno,region,airportcode,pensionNo,emolumentxtextboxno,epftextboxno,vpftextboxno,principletextboxno,interesttextboxno,advancetextboxno,pfwsubtextboxno,aaipfboxno,pfwcontriboxno,contributionboxno,emolumentsmntsboxno,pensionoption,editid,adjOBYear,dob,pfcardnarrationboxno){ 
  	// alert("Permission Denied to Edit the PCReport Details");
       var dt1   = monthyear.substring(0,2);
	   var mon1  = monthyear.substring(3,6);
	   var year1=monthyear.substring(7,monthyear.length);
	   var duputationflag=duputationflag;
	    
	  if(mon1 == "JAN") month = 0;
   	else if(mon1 == "FEB" ||mon1 == "Feb") month = 1;
	else if(mon1 == "MAR" ||mon1 ==  "Mar") month = 2;
	else if(mon1 == "APR" || mon1 == "Apr") month = 3;
	else if(mon1 == "MAY" || mon1 == "May" ) month = 4;
	else if(mon1 == "JUN" || mon1 == "Jun") month = 5;
	else if(mon1 == "JUL" || mon1 == "Jul") month = 6;
	else if(mon1 == "AUG" || mon1 == "Aug") month = 7;
	else if(mon1 == "SEP" || mon1 == "Sep" ) month = 8;
	else if(mon1 == "OCT" || mon1 == "Oct" ) month = 9;
	else if(mon1 == "NOV" || mon1 == "Nov") month = 10;
	else if(mon1 == "DEC" || mon1 == "Dec") month = 11;
	var userId1='<%=userId%>';
	 var noofmonths;
	 var emoluments= document.getElementById(emolumentxtextboxno).value;
	 var epf=document.getElementById(epftextboxno).value;
	 var vpf= document.getElementById(vpftextboxno).value;
	 var principle=document.getElementById(principletextboxno).value;
	 var interest=document.getElementById(interesttextboxno).value;
	 var contri=document.getElementById(contributionboxno).value;
	 //var advance=document.getElementById(advancetextboxno).value;
	// var pfwsub=document.getElementById(pfwsubtextboxno).value;
	// var pfwcontri=document.getElementById(pfwcontriboxno).value;
	 var aaipf=document.getElementById(aaipfboxno).value;
	 //alert(aaipfboxno+'--'+emolumentsmntsboxno+'--'+pensionoption);
	 //alert(dob);
	 if(pensionoption=="B"){
	   noofmonths=document.getElementById(emolumentsmntsboxno).value;
	 }else{
	 noofmonths = 1;
	 }
	 var pfcardnarration=document.getElementById(pfcardnarrationboxno).value;  
	// alert(pfcardnarrationboxno+"=="+emolumentxtextboxno);
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=false;
	 document.getElementsByName(emolumentxtextboxno)[0].focus();
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(epftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(epftextboxno)[0].readOnly=false;	 
	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].readOnly=false;
	 document.getElementsByName(interesttextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(interesttextboxno)[0].readOnly=false;
	 //document.getElementsByName(advancetextboxno)[0].style.background='#FFFFCC';
	// document.getElementsByName(advancetextboxno)[0].readOnly=false;
	// document.getElementsByName(pfwsubtextboxno)[0].style.background='#FFFFCC';
	// document.getElementsByName(pfwsubtextboxno)[0].readOnly=false;
	 
	 document.getElementsByName(aaipfboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(aaipfboxno)[0].readOnly=false;	 
	 //document.getElementsByName(pfwcontriboxno)[0].style.background='#FFFFCC';
	 //document.getElementsByName(pfwcontriboxno)[0].readOnly=false;
	 document.getElementsByName(contributionboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(contributionboxno)[0].readOnly=false;
	if(pensionoption=="B"){
	 document.getElementsByName(emolumentsmntsboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(emolumentsmntsboxno)[0].disabled=false;
	  }
	  
	 document.getElementsByName(pfcardnarrationboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(pfcardnarrationboxno)[0].readOnly=false;
	  
	 var buttonName=document.getElementsByName(editid)[0].value;
	 if(buttonName=="E")
	 {
	 prevPensionContri=contri;
	 prevemoluments=emoluments;
	 prevepf=epf;
	 prevvpf=vpf;
	 prevprinciple=principle;
	 previntrst=interest;
	 //prevadv=advance;
	 //prevpfwsub=pfwsub;
	// prevpfwcontri=pfwcontri;
	 prevaaipf=aaipf;
	 }
	 document.getElementsByName(editid)[0].value="S";
	 createXMLHttpRequest();	
	  var answer="",editTransFlag="";
		var adjOBYear='<%=adjOBYear%>';	
	 
	 if(buttonName=="S"){	 
	if(((prevemoluments==emoluments) &&(prevepf=epf)&&(prevvpf=vpf)
		&&(prevprinciple=principle)&&(previntrst=interest) &&(prevaaipf=aaipf)) &&(prevPensionContri!=contri) ){
		editTransFlag="Y";
		
		}else{ 
		editTransFlag="N";	
		 
		}
	 	editFlag ="true";
		 process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';
		 document.getElementById('update').style.display='block'; 
		  
		//var url="<%=basePath%>reportservlet?method=editTransactionDataForAdjCrtn&pensionNo="+pensionNo+"&cpfaccno="+cpfaccno+"&emoluments="+emoluments+"&monthyear="+monthyear+"&region="+region+"&airportcode="+airportcode+"&editid="+editid+"&epf="+epf+"&vpf="+vpf+"&principle="+principle+"&interest="+interest+"&contri="+contri+"&advance="+advance+"&loan="+pfwsub+"&aailoan="+pfwcontri+"&pensionoption="+pensionoption+"&noofmonths="+noofmonths+"&adjOBYear="+adjOBYear+"&editTransFlag="+editTransFlag+"&dateOfBirth="+dob;
  
		var url="<%=basePath%>reportservlet?method=editTransactionDataForAdjCrtn&pensionNo="+pensionNo+"&cpfaccno="+cpfaccno+"&emoluments="+emoluments+"&monthyear="+monthyear+"&region="+region+"&airportcode="+airportcode+"&editid="+editid+"&epf="+epf+"&vpf="+vpf+"&principle="+principle+"&interest="+interest+"&contri="+contri+"&pensionoption="+pensionoption+"&noofmonths="+noofmonths+"&adjOBYear="+adjOBYear+"&editTransFlag="+editTransFlag+"&dateOfBirth="+dob+"&from7narration="+pfcardnarration;
   //alert(url);
	 xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = updateemoluments;
		xmlHttp.send(null);
	   }
	 
}


function updateemoluments()
  {
	if(xmlHttp.readyState ==4)
  	{
	var buttonupdate=xmlHttp.responseText;
	 process.style.display="none";
	 document.getElementById('process').style.display='none';
	 document.getElementById('fade').style.display='none';
	 document.getElementsByName(buttonupdate)[0].value="E";	
	
	 var rownum=buttonupdate.substring(4, buttonupdate.length);
	 var emolumentxtextboxno="emoluments"+rownum;
	 var epfboxno="epf"+rownum;
	 var vpfboxno="vpf"+rownum;
	 var principleboxno="principle"+rownum;
	 var interestboxno="interest"+rownum;
	 var contriboxno="pencontri"+rownum;
	 var advanceboxno="advance"+rownum;
	 var pfwsubboxno="pfwsub"+rownum;
	 var aaipfboxno="aaipf"+rownum;
	 var pfwcontriboxno="pfwcontri"+rownum;
  	 var emolumentsmntsboxno="emolumentsmnts"+rownum;
  	 var pfcardnarrationboxno="pfcardnarration"+rownum;
	 
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='none';
	 document.getElementsByName(epfboxno)[0].readOnly=true;
	 document.getElementsByName(epfboxno)[0].style.background='none';
	 document.getElementsByName(contriboxno)[0].readOnly=true;
	 document.getElementsByName(contriboxno)[0].style.background='none';
	 document.getElementsByName(vpfboxno)[0].readOnly=true;
	 document.getElementsByName(vpfboxno)[0].style.background='none';
	 document.getElementsByName(principleboxno)[0].readOnly=true;
	 document.getElementsByName(principleboxno)[0].style.background='none';
	 document.getElementsByName(interestboxno)[0].readOnly=true;
	 document.getElementsByName(interestboxno)[0].style.background='none';
	 document.getElementsByName(advanceboxno)[0].readOnly=true;
	 document.getElementsByName(advanceboxno)[0].style.background='none';
	 document.getElementsByName(pfwsubboxno)[0].readOnly=true;
	 document.getElementsByName(pfwsubboxno)[0].style.background='none';
	 document.getElementsByName(aaipfboxno)[0].readOnly=true;
	 document.getElementsByName(aaipfboxno)[0].style.background='none';
	 document.getElementsByName(pfwcontriboxno)[0].readOnly=true;
	 document.getElementsByName(pfwcontriboxno)[0].style.background='none';
	 document.getElementsByName(emolumentsmntsboxno)[0].disabled=true;
	 document.getElementsByName(emolumentsmntsboxno)[0].style.background='none';
	 document.getElementsByName(pfcardnarrationboxno)[0].disabled=true;
	 document.getElementsByName(pfcardnarrationboxno)[0].style.background='none';
	 
	}
  }
	
	
	
	function editOBValues(pensionNo,empnetobtextboxno,aainetobtextboxno,adjobyear,editid){
	
	// alert("Permission Denied to Edit the PCReport Details");
   
	var userId1='<%=userId%>';
	 // alert(document.getElementById(empnetobtextboxno).value);
	 var empnetob= document.getElementById(empnetobtextboxno).value;
	 var aainetob=document.getElementById(aainetobtextboxno).value;
	 var adjOBYear= adjobyear;
	 
	// alert('---empnetob---'+empnetob+'--aainetob---'+aainetob);
	 document.getElementById(empnetobtextboxno).readOnly=false;
	 document.getElementById(empnetobtextboxno).focus();
	 document.getElementById(empnetobtextboxno).style.background='#FFFFCC';
	 document.getElementById(aainetobtextboxno).style.background='#FFFFCC';
	 document.getElementById(aainetobtextboxno).readOnly=false;	 
	  
	 var buttonName=document.getElementById(editid).value;
	 document.getElementById(editid).value="S";
	 createXMLHttpRequest();	
	  var answer="";
		 
	var empnetobFlag ="true";
	var emolumentsFlag ="false";	 
		 
	 if(buttonName=="S"){ 
		 process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';
		  
		var url="<%=basePath%>reportservlet?method=editTransactionDataForAdjCrtn&pensionNo="+pensionNo+"&empnetob="+empnetob+"&aainetob="+aainetob+"&adjOBYear="+adjOBYear+"&empnetobFlag="+empnetobFlag+"&emolumentsFlag="+emolumentsFlag;
 // alert(url);
	 xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = updateobvalues;
		xmlHttp.send(null);
	   }
	}
	
	function updateobvalues(){	
	
	if(xmlHttp.readyState ==4)
  	{
	 
	 process.style.display="none";
	 document.getElementById('process').style.display='none';
	 document.getElementById('fade').style.display='none';
	// document.getElementsByName(buttonupdate)[0].value="E";	
	
	 
	 var empnetobtxtextboxno="empnetob";
	 var aainetboxno="aainetob"; 
	 
	 
	  
	 
	 document.getElementById(empnetobtxtextboxno).readOnly=true;
	 document.getElementById(empnetobtxtextboxno).style.background='none';
	 document.getElementById(aainetboxno).readOnly=true;
	 document.getElementById(aainetboxno).style.background='none';
	 
	 
	}
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
   
    
  function validateForm() {
       	var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var empserialNO=document.forms[0].pfid.value;
		regionID="NO-SELECT";
		reportType="Html";
		var pfidStrip='1 - 1'; 
		yearID="NO-SELECT";
		monthID="NO-SELECT";
		var transferStatus="";
		var recoverieTable="";
        var interestcalcUpto=document.forms[0].interestCalc.value;
       
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&frm_pfids="+pfidStrip+"&transferStatus="+transferStatus+"&finalrecoverytableFlag="+recoverieTable+"&interestcalcUpto="+interestcalcUpto;
		var url="<%=basePath%>reportservlet?method=getReportPenContr"+params;
		//alert(url);
		
		
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
		 
	}	
	
function updateButtonStatus()
	{
	if(xmlHttp.readyState ==4)
	{
	 document.forms[0].editdate.value="E";	
	 document.forms[0].finalsettle.style.background='none';
	 }

      document.forms[0].editinterestdate.value="E";
	  document.forms[0].interestCalc.style.background='none';
	}
  function editcal()
	    {
		var buttonname=document.forms[0].editdate.value;
		if(buttonname=="E"){
		alert("Please Click the Edit button");
		return false;
		}
		else 
		{
		 document.forms[0].finalsettle.style.background='#FFFFCC';			
		 document.forms[0].finalsettle.value=document.forms[0].finalsettle.value,show_calendar('forms[0].finalsettle');
							
		}	
	}
	
	 function frmload(){
	 
	  if(process){
	  process.style.display="none";
	  document.getElementById('process').style.display='none';
	  }
	 document.getElementById('fade').style.display='none';
	 document.getElementById('update').style.display='none'; 
	 if(finlaizedFlag=="NotFinalize"){
		 document.getElementById('update').style.display='block'; 
	} else{
	 document.getElementById('update').style.display='none'; 
	}
	}
	
 
 function  finalresult(empSerialNo,empName,finalEmpNetOB,finalAdjEmpNetOBIntrst,finalAaiNetOB,finalAdjAaiNetOBIntrst,finalPensionOB,pensioninterest,adjOBYear,jval){	
 var diffFlag ="",url =""; 
 // alert('here'+empserialNO+"--"+Emoluments+"--"+cpfTotal+"--"+PenContriTotal+"--"+PFTotal);
 //alert('here'+finalEmpNetOB+"--"+finalAdjEmpNetOBIntrst+"--"+finalAaiNetOB+"--"+finalAdjAaiNetOBIntrst+"--"+finalPensionOB+"--"+pensioninterest);
 	 for(var j=0;j<jval;j++){
	var editvalue="edit"+j;
	//alert(editvalue);
		// alert(document.getElementsByName(editvalue)[0].value);
		
	if(document.getElementsByName(editvalue)[0].value=="S"){
	var monthyear=document.getElementsByName("monthyear"+j)[0].value;
	 alert("Data edited for the month of "+monthyear+" ,but yourn't clicked on 'S' button.Please save the changes.");
	 document.getElementsByName(editvalue)[0].focus();
	 return false;
 }
 }
 	
 	var comfirmMsg = confirm("Do You want Finalize these Changes. Click Ok For Fianlize and Cancel For Not Finalize");
		if (comfirmMsg== true){ 
		diffFlag ="true";	 
  			}else{
 		diffFlag ="false";
 		}
 	
 	url ="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtn&empserialNO="+empSerialNo+"&EmpSub="+finalEmpNetOB+"&AAIContri="+finalAaiNetOB+"&PenContriTotal="+finalPensionOB+"&adjEmpSubInterest="+finalAdjEmpNetOBIntrst+"&adjAAiContriInterest="+finalAdjAaiNetOBIntrst+"&reportYear="+adjOBYear+"&diffFlag="+diffFlag+"&empName="+empName+"&pensioninter="+pensioninterest+"&accessCode="+'<%=accessCode%>'+"&form2Status="+'<%=form2Status%>';
 	 // alert(url);
 	document.forms[0].action=url;
   	document.forms[0].method="post";  	 
  	 document.forms[0].submit();
 
 } 
 function  gotoback(pensionno){
 var accessCode='<%=accessCode%>',url="";  
 if(accessCode=="PE040201"){
 	 url= "<%=basePath%>reportservlet?method=loadPCReportForAdjDetails&frmName=adjcorrections&empsrlNo="+pensionno+"&accessCode="+'<%=accessCode%>';
 }else if(accessCode=="PE040202" || accessCode=="PE04020601" ){
  url= "<%=basePath%>reportservlet?method=searchAdjRecords&frmName=adjcorrections&empsrlNo="+pensionno+"&reportYear="+'<%=adjOBYear%>'+"&accessCode="+'<%=accessCode%>';
   }
  if(editFlag=="true"){
		alert("please  click on Update and then use Back Button");
	return false;
		} 	
  	// alert(url);
  	 
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
 function  reports(repName){ 
 var url="";
 
 url = "<%=basePath%>search1?method=uploadToForm2&frmName=adjcorrections&formtype=pfcard"; 
  
  	//alert(url);
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
  function  pfcard(empSerialNo,empName,finalEmpNetOB,finalAaiNetOB,finalPensionOB,adjOBYear){ 
 var url="",reportType="";
 var pfFlag="true";
 var swidth=screen.Width-10;
 var sheight=screen.Height-150;
		
 if(editFlag=="true"){
		alert("please  click on Update and then use PF Card");
		}else{ 
		 
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}	
 	url ="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtn&empserialNO="+empSerialNo+"&EmpSub="+finalEmpNetOB+"&AAIContri="+finalAaiNetOB+"&PenContriTotal="+finalPensionOB+"&reportYear="+adjOBYear+"&pfFlag="+pfFlag+"&empName="+empName+"&frm_reportType="+reportType;
	 	//alert(url);
		 
   	 				 		wind1 = window.open(url,"PFCard","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 		 
  		} 
 }
</script>
     
  </head>
  
  <body class="BodyBackground" onload="frmload();">
  <form >
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   		
   			ArrayList cardReportList=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="";
	  	String arrearInfo="",orderInfo="";
	  	String noofMonths="1";
	  	int size=0,finalInterestCal=0,noOfmonthsForInterest=0,countList=0,noOfAfterfnlrtmntmonhts=0;	  	
	  	String fromdate="",todate="",obMessage="";
		int fromYear=0,toYear=0; 
		String Years[]=null;
		Years = adjOBYear.split("-"); 
		CommonUtil commonUtil=new CommonUtil();
		fromdate="01-Apr-"+Years[0];
		todate="01-Mar-"+Years[1];
		fromYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				fromdate, "dd-MMM-yyyy", "yyyy"));
		toYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				todate, "dd-MMM-yyyy", "yyyy"));
	 
		if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="",pfidString="",chkBulkPrint="",pfcardNarration="",chkRegionString="",chkStationString="",dispSignStation="",dispSignPath="";
 		String mangerName="",ArrearInFSPeriod="false";
    	boolean check=false,signatureFlag=false;
	   Long finalEmpNetOB=null,finalAaiNetOB=null,finalPensionOB=null,finalPrincipalOB=null,finalStlmentEmpNet=null,finalStlmentNetAmount=null,finalStlmentAAICon=null,finalStlmentPensDet=null;
	   Long finalEmpNetOBIntrst=null,finalAaiNetOBIntrst=null,finalPensionOBIntrst=null;
	   long tempFinalPensionOB=0;
	   double tempFinalPensionOB1=0.0;
	   double finalEmpNetOB1=0.0,finalAaiNetOB1=0.0;
	 
	AdjCrtnDAO adjCrtnDAO = new AdjCrtnDAO();
    FinancialReportService reportService=new FinancialReportService();
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }
    if(request.getAttribute("blkprintflag")!=null){
      chkBulkPrint=(String)request.getAttribute("blkprintflag");
    }
  
    if(request.getAttribute("dspYear")!=null){
    dispYear=(String)request.getAttribute("dspYear");
    }
    if(signature.equals("true")){
    	if(userId.equals("SRFIN")){
    		signatureFlag=true;
			dispSignStation="South Region";
			dispDesignation="Deputy General Manager(F&A)";
			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
    	}else if(userId.equals("CHQFIN")){
    		signatureFlag=true;
			dispSignStation="CHQ";
			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
			dispDesignation="Assistant Manager(F)/Manager(F)";
    	}else if(userId.equals("CHQFIN")){
    		signatureFlag=true;
			dispSignStation="CHQ";
			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
			dispDesignation="Assistant Manager(F)/Manager(F)";
    	}else if(userId.equals("NERFIN")){
    		signatureFlag=true;
			dispSignStation="";
			mangerName="(G.S Mohapatra)";
 			dispDesignation="Joint General Manager(Fin), AAI, NER,Guwahati";
			dispSignPath=basePath+"PensionView/images/signatures/G.SMohapatra.gif";
    	}else if(userId.equals("WRFIN")){
    		signatureFlag=true;
 			dispSignStation="";
 			mangerName="(Shri S H Kaswankar)";
 			dispDesignation="Sr. Manager(Fin), AAI, WR, Mumbai";
 			dispSignPath=basePath+"PensionView/images/signatures/Kaswankar.gif";	
    	}else if(userId.equals("NRFIN")){
    		signatureFlag=true;
			dispSignStation="";
			mangerName="(Anil Kumar Jain)";
 			dispDesignation="Asstt.General Manager(Fin), AAI, NR";
			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
    	}else if(userId.equals("SAPFIN")){
    		signatureFlag=true;
 			dispSignStation="";
 			mangerName="(Monika Dembla)";
 			dispDesignation="Manager(F & A), AAI, RAU,SAP ";
 			dispSignPath=basePath+"PensionView/images/signatures/Monika Dembla.gif";
    	}else if(userId.equals("IGIFIN")){
    		signatureFlag=true;
				dispSignStation="";
				mangerName="(Arun Kumar)";
				dispDesignation="Sr. Manager(F&A), AAI,IGICargo IAD";
				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";
    	}else if(userId.equals("ERFIN")){
			signatureFlag=true;
			dispSignStation="Kolkata-700 052";
			dispSignPath=basePath+"PensionView/images/signatures/JBBISWAS.gif";
			dispDesignation="Asstt.General Manager(Fin.),A.A.I.,NSCBI Airport";
		}else if(userId.equals("NSCBFIN")){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
    	
    }
   if(chkBulkPrint.equals("true")){
   	   if(dispYear.equals("2008-09")){
   			 if(chkRegionString.equals("CHQNAD")){
    			signatureFlag=true;
    			dispSignStation="CHQ";
    			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
    			dispDesignation="Assistant Manager(F)/Manager(F)";
    		 }else if(chkRegionString.equals("North Region")){
    			signatureFlag=true;
    			dispSignStation="";
    			mangerName="(Anil Kumar Jain)";
     			dispDesignation="Asstt.General Manager(Fin), AAI, NR";
    			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
    		 }else if(chkRegionString.equals("North-East Region")){
    			signatureFlag=true;
    			dispSignStation="";
    			mangerName="(G.S Mohapatra)";
     			dispDesignation="Joint General Manager(Fin), AAI, NER,Guwahati";
    			dispSignPath=basePath+"PensionView/images/signatures/G.SMohapatra.gif";
    		 }else if(chkRegionString.equals("South Region")){
    			signatureFlag=true;
    			dispSignStation="South Region";
    			dispDesignation="Deputy General Manager(F&A)";
    			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
    		 }else if(chkRegionString.equals("RAUSAP")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(Monika Dembla)";
     			dispDesignation="Manager(F & A), AAI, RAU,SAP ";
     			dispSignPath=basePath+"PensionView/images/signatures/Monika Dembla.gif";	
    		 }else if(chkRegionString.equals("West Region")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(Shri S H Kaswankar)";
     			dispDesignation="Sr. Manager(Fin), AAI, WR, Mumbai";
     			dispSignPath=basePath+"PensionView/images/signatures/Kaswankar.gif";	
    		 }else if(chkRegionString.equals("CHQIAD")){
    			if(chkStationString.toLowerCase().equals("office complex")){
    				signatureFlag=true;
    				dispSignStation="Office Complex";
    				dispDesignation="Assistant Manager(F)/Manager(F)";
    				dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
    			}else if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="";
     				mangerName="(Arun Kumar)";
     				dispDesignation="Sr. Manager(F&A), AAI,IGICargo IAD";
     				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
    		 	}else if(chkStationString.toLowerCase().equals("IGI IAD".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="";
     				mangerName="(Arun Kumar)";
     				dispDesignation="Sr. Manager(F&A), AAI,IGI IAD";
     				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
    		 	}else if(chkStationString.toLowerCase().equals("KOLKATA".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
    		}
   		}else if(dispYear.equals("2009-10")){
   			if(chkRegionString.equals("East Region")){
    			signatureFlag=true;
    			dispSignStation="Kolkata-700 052";
    			dispSignPath=basePath+"PensionView/images/signatures/JBBISWAS.gif";
    			dispDesignation="Asstt.General Manager(Fin.),A.A.I.,NSCBI Airport";
    		}else if(chkStationString.toLowerCase().equals("KOLKATA".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
  			 if(chkRegionString.equals("CHQNAD")){
     			signatureFlag=true;
     			dispSignStation="CHQ";
     			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			dispDesignation="Assistant Manager(F)/Manager(F)";
     		 }else if(chkRegionString.equals("North Region")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(Anil Kumar Jain)";
      			dispDesignation="Asstt.General Manager(Fin), AAI, NR";
     			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
     		 }else if(chkRegionString.equals("South Region")){
     			signatureFlag=true;
     			dispSignStation="South Region";
     			dispDesignation="Deputy General Manager(F&A)";
     			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
     		 }else if(chkRegionString.equals("RAUSAP")){
      			signatureFlag=true;
      			dispSignStation="";
      			mangerName="(Monika Dembla)";
      			dispDesignation="Manager(F & A), AAI, RAU,SAP ";
      			dispSignPath=basePath+"PensionView/images/signatures/Monika Dembla.gif";	
     		 }else if(chkRegionString.equals("West Region")){
      			signatureFlag=true;
      			dispSignStation="";
      			mangerName="(Shri S H Kaswankar)";
      			dispDesignation="Sr. Manager(Fin), AAI, WR, Mumbai";
      			dispSignPath=basePath+"PensionView/images/signatures/Kaswankar.gif";	
     		 }else if(chkRegionString.equals("CHQIAD")){
     			if(chkStationString.toLowerCase().equals("office complex")){
     				signatureFlag=true;
     				dispSignStation="Office Complex";
     				dispDesignation="Assistant Manager(F)/Manager(F)";
     				dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			}else if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
      				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(Arun Kumar)";
      				dispDesignation="Sr. Manager(F&A), AAI,IGICargo IAD";
      				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
     		 	}else if(chkStationString.toLowerCase().equals("IGI IAD".toLowerCase())){
      				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(Arun Kumar)";
      				dispDesignation="Sr. Manager(F&A), AAI,IGI IAD";
      				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
     		 	}
     		}
    		
   		}
   }

   
	  	cardReportList=(ArrayList)request.getAttribute("cardList");
	  	EmployeeCardReportInfo cardReport=new EmployeeCardReportInfo();
	  	size=cardReportList.size();
	  	int intMonths=0,arrearMonths=0;
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "PF_CARD_Report_FYI("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  	if(size!=0){
	  	ArrayList dateCardList=new ArrayList();
	  	ArrayList dateCardList1=new ArrayList();
	  
	  	ArrayList dataPTWList=new ArrayList();
	  	ArrayList dataFinalSettlementList=new ArrayList();
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  		
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		session.setAttribute("PersonalInfo",personalInfo);
		System.out.println("PF ID String"+personalInfo.getPfIDString()+"Adj Date"+personalInfo.getAdjDate());
		dateCardList=cardReport.getPensionCardList();
		dateCardList1=cardReport.getPensionCardList1();
		dataPTWList=cardReport.getPtwList();
		noOfmonthsForInterest=cardReport.getNoOfMonths();
		intMonths=cardReport.getInterNoOfMonths();
		arrearMonths=cardReport.getArrearNoOfMonths();
		dataFinalSettlementList=cardReport.getFinalSettmentList();
        arrearInfo=cardReport.getArrearInfo();
        orderInfo=cardReport.getOrderInfo();
        String[] arrearData=arrearInfo.split(",");
        double arrearAmount=0.00,arrearContri=0.00;
        boolean alreadyfinal=false,finalrateofinterest=false,isFrozedSeperation=false;
        String arrearDate="";
        
        isFrozedSeperation=personalInfo.isFrozedSeperationAvail();
        String finalsettlmentdt=personalInfo.getFinalSettlementDate(); 
        
       if(!personalInfo.getFinalSettlementDate().equals("---")){
        	if ((!commonDAO.compareFinalSettlementDates(fromdate,todate,personalInfo.getFinalSettlementDate())) && personalInfo.isCrossfinyear()==false && fromYear>=2011){
        		finalsettlmentdt=personalInfo.getResttlmentDate();
        		alreadyfinal=true;
    		}
        	System.out.println("fromYear============================"+fromYear+"toYear"+toYear);
        	 //   For  Making rateofintrst as 9.5  over all 2010-2011  So 
        	 //We Removed Final Settlement cases rateof intrst Condition 
        
        }  
        arrearDate=arrearData[0];
        arrearAmount=Double.parseDouble(arrearData[2]);
        arrearContri=Double.parseDouble(arrearData[3]);
       
       wetheroption=personalInfo.getWetherOption();
       System.out.println("=======wetheroption--="+wetheroption);
       
       
       
   %>
   <tr>
   <td>
   <table width="100%" height="490" cellspacing="0" cellpadding="0">
  <!-- <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   
    <td width="120" rowspan="3" align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
    <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
    	<td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
     	<td width="96">&nbsp;</td>
     	<td width="95">&nbsp;</td>
     	<td width="85">&nbsp;</td>
  	 	<td width="384"  class="reportlabel">Employee's Provident Fund Trust</td>
  	 	<td width="87">&nbsp;</td>
    	<td width="272">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr> 
  <tr>  
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="3" class="reportlabel" style="text-decoration: underline" align="center">EPF &amp; PENSION CONTRIBUTION CARD </td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   

    <td colspan="3" align="center" nowrap="nowrap" class="reportsublabel">FOR THE YEAR <font style="text-decoration: underline"><%=dispYear%></td>
    <td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>
    
  </tr>
  <tr>
    <td colspan="7">&nbsp;</td>
  </tr> -->
   
  <!-- <tr>  
    
  <td>&nbsp;</td>
 <td   class="reportlabel" style="text-decoration: underline" align="center">MONTHLY CPF CORRECTIONS</td>
  
    <td colspan="2"> &nbsp;</td>
     <td>&nbsp;</td>
      <td>&nbsp;</td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   

    <td colspan="3" align="center" nowrap="nowrap" class="reportsublabel">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <font style="text-decoration: underline"><%=dispYear%></td>
    <td align="right" nowrap="nowrap" class="Data">&nbsp;</td>
    
  </tr>-->
  <tr>    
   
 <td   class="reportlabel" style="text-decoration: underline" align="center">MONTHLY CPF CORRECTIONS</td>
 	<!--<td align="right"> <input type="button" 
						value="PF Card" class="btn"   style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #FFFFFF;" onclick="pfcard('<%=personalInfo.getPensionNo()%>','<%=personalInfo.getEmployeeName()%>','<%=finalEmpNetOB%>','<%=finalAaiNetOB%>','<%=finalPensionOB%>','<%=adjOBYear%>');"/> </td>
 
  <td align="right"> <input type="button" 
						value="Form 2" class="btn"   style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #FFFFFF;" onclick="reports('loadForm2');"/> </td>
  -->
  </tr> 
  <tr>
    <td colspan="6">&nbsp;</td>
  </tr>
  
  
 
  <%
System.out.println("=personalInfo.getDateOfBirth()=="+personalInfo.getDateOfBirth());

  %>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td width="48%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="36%">Pf Id:</td>
            <td width="64%" class="Data"><%=personalInfo.getPfID()%></td>
          </tr>
		    <tr>
            <td class="reportsublabel">Name:</td>
            <td class="Data"><%=personalInfo.getEmployeeName()%></td>
          </tr>
		     <tr>
            <td class="reportsublabel">DATE OF BIRTH:</td>
            <td class="Data"><%=personalInfo.getDateOfBirth().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">DATE OF JOINING:</td>
            <td class="Data"><%=personalInfo.getDateOfJoining().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">DATE OF RETIRE:</td>
            <td class="Data"><%=personalInfo.getDateOfAnnuation().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">Pension Option</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getWhetherOptionDescr())%></td>
          </tr>
        </table></td>
        <td width="52%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="49%" class="reportsublabel">CPF Acc.No(Old):</td>
            <td width="51%" class="Data"><%=personalInfo.getCpfAccno().toUpperCase()%></td>
          </tr>
          <tr>
            <td width="49%" class="reportsublabel">EMP No:</td>
            <td width="51%" class="Data"><%=personalInfo.getEmployeeNumber().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">Designation:</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getDesignation())%></td>
          </tr>
		       <tr>
            <td nowrap="nowrap" class="reportsublabel">Father/ Husband'S Name:</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getFhName())%></td>
          </tr>
		   <tr>
            <td class="reportsublabel">Gender:</td>
            <td class="Data"><%=personalInfo.getGender().toUpperCase()%></td>
          </tr>
		    <tr>
            <td class="reportsublabel">Marital Status:</td>
            <td><%=commonUtil.convertToLetterCase(personalInfo.getMaritalStatus())%></td>
          </tr>
		    <tr>
            <td nowrap="nowrap" class="reportsublabel">Date Of Pension Membership:</td>
            <td class="Data"><%=personalInfo.getDateOfEntitle().toUpperCase()%></td>
          </tr>
        </table></td>
      </tr>
	  <tr>
	  		<td colspan="2">
	  			<table border="0" cellpadding="1" cellspacing="1">
	  				<tr>
	  					<td class="reportsublabel">Nominee Info</td>
	  					<%
	  						ArrayList nomineeList=new  ArrayList();
	  						nomineeList=personalInfo.getNomineeList();
	  						NomineeBean nomineeBean=new NomineeBean();
	  						for(int nl=0;nl<nomineeList.size();nl++){
	  						nomineeBean=(NomineeBean)nomineeList.get(nl);
	  					%>
	  					<td class="Data"><%=nomineeBean.getSrno()+". "+nomineeBean.getNomineeName()+" ("+nomineeBean.getNomineeRelation()+") "%></td>
	  					<%}%>
	  				</tr>
	  				
	  			</table>
	  		</td>
	  </tr>
    </table></td>
  </tr>

  <tr>
  	<div id="process" class="white_content">
<img src="<%=basePath%>PensionView/images/Indicator.gif" border="0" align="right" />
<span class="label">Processing.......</span>
        </div>
		<div id="fade" class="black_overlay"></div>


    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2">&nbsp;</td>
        <td colspan="8" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td>
        <td width="8%">&nbsp;</td>
        <td width="5%">&nbsp;</td>
        <td width="5%">&nbsp;</td>
      </tr>
      <tr>
        <td width="4%" rowspan="2" class="label">Month</td>
        <td width="8%" rowspan="2" class="label">Emolument</td>
        <td width="8%" rowspan="2" class="label"><div align="center">EPF</div></td>
        <td width="3%" rowspan="2" class="label"><div align="center">VPF</div></td>
        <td colspan="2"><div align="center" class="label">Refund Of ADV./PFW </div></td>
        <td width="6%" rowspan="2"  class="label">TOTAL </td>
        <td width="5%" rowspan="2" class="label">Advance<br/>PFW PAID</td>
         <td width="5%" rowspan="2" class="label">PFW SUB</td>
        <td width="8%" class="label">NET </td>
      
        <td width="3%" align="center" rowspan="2" class="label">AAI<br/>PF</td>
        <td width="6%" class="label" rowspan="2">PFW<br/>DRAWN</td>
        <td width="3%" class="label">NET</td>
        <td width="12%" class="label" rowspan="2">PENSION<br/>CONTR. </td>        
        <td class="label" width="5%" align="center">NoofMonths/Days</td>        
        <td class="label" width="5%" align="center">Edit</td>
        <td rowspan="2" class="label">Station</td>
        <td rowspan="2" class="label">Remarks</td>
      </tr>
      <tr>
       
        <td width="5%" class="label"><div align="center">Principal</div></td>
        <td width="7%" class="label"><div align="center">Interest</div></td>
        <td class="label">(7-(8+9))</td>
      
        <td class="label" >(11-12)</td>
       
      </tr>
      <tr>
        <td>1</td>
        <td>2</td>
        <td class="Data">3</td>
        <td class="Data">4</td>
        <td class="Data">5</td>
        <td class="Data">6</td>
        <td class="Data">7</td>
        <td class="Data">8</td>
        <td class="Data">9</td>
        <td class="Data">10</td>
        <td class="Data">11</td>
        <td class="Data">12</td>
        <td class="label">13</td>
        <td class="label">14</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    
        <%
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	double totalEmoluments=0.0,pfStaturary=0.0,empVpf=0.0,principle=0.0,interest=0.0,empTotal=0.0,totalEmpNet=0.0,totalEmpIntNet=0.0,dispTotalEmpNet=0.0,dispTotalAAINet=0.0,totalAaiIntNet=0.0;
       	double totalAAIPF=0.0,totalPFDrwn=0.0,totalAAINet=0.0,advancePFWPaid=0.0,empNetInterest=0.0,aaiNetInterest=0.0,dispTotalPensionContr=0.0,totalPensionContr =0.00,pensionInterest=0.0,pensionArrearInterest=0.0,arrearEmpNetInterest=0.0,arrearAaiNetInterest=0.0,arrearPCInterest=0.0;
       	double totalAdvances=0,totalGrandArrearEmpNet=0.0,totalGrandArrearAaiNet=0.0,advanceAmountPaid=0.0, PFWSubPaid =0.0,pensionContriTot = 0.0,pensionContriArrearTot=0.0;
       	double totalArrearEmpNet=0.0,totalArrearAAINet=0.0,totalArrearPCNet=0.0;
       	double aftrFinstlmntAAINetTot=0.0,aftrFinstlmntEmpNetTot= 0.0,aftrFinstlmntPCNetTot=0.0, aftrFinstlmntAAINetIntrst=0.0,aftrFinstlmntEmpNetIntrst= 0.0,aftrFinstlmntPCNetIntrst=0.0;
       	double 	remaininInt=0.0, remainAaiInt=0.0,remaininInt_Saved_Value=0.0, remainAaiInt_Saved_Value=0.0;
       	double aprEmpNet=0.0,aprAaiNet=0.0,aprPrincipal=0.0,aprpc=0.0;
        double grandaprEmpNet=0.0,grandaprAaiNet=0.0,grandaprPrincipal=0.0,grandaprpc=0.0;
       	String arrearFlag="N",arrearRemarks="---",adjNarration="";
  		EmployeePensionCardInfo pensionCardInfo=new EmployeePensionCardInfo();
  		EmployeePensionCardInfo pensionCardInfo1=new EmployeePensionCardInfo();
		Long empNetOB=null,aaiNetOB=null,pensionOB=null,principalOB=null;
		Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null,adjOutstandadv=null;
		double rateOfInterest=0,emoluments =0.0;
  		String obFlag="",calYear="",datestartedFalg="false";
  		boolean closeFlag=false;
  		ArrayList obList=new ArrayList();
  		ArrayList closingOBList=new ArrayList();
  		int monthsCntAfterFinstlmnt =0;
  		int countval=0;	
  		if(dateCardList.size()!=0){
  		for(int i=0;i<dateCardList.size();i++){
  		countval++;
		countList=countval;
  		pensionCardInfo=(EmployeePensionCardInfo)dateCardList.get(i);
  		//pensionCardInfo1=(EmployeePensionCardInfo)dateCardList.get(i);
  		obFlag=pensionCardInfo.getObFlag();
  		if(obFlag.equals("Y")){
  			 calYear=commonUtil.converDBToAppFormat(pensionCardInfo.getShnMnthYear(),"MMM-yy","yyyy");
  			 System.out.println("--------calYear ------"+calYear);
	  		 obList=pensionCardInfo.getObList();
	  		 empNetOB=(Long)obList.get(0);
           	 aaiNetOB=(Long)obList.get(1);
             pensionOB=(Long)obList.get(2);
             adjNarration=(String)obList.get(11);
             principalOB=(Long)obList.get(12);
             advancePFWPaid=0.0;
             totalPFDrwn=0.0;
             if(obList.size()>3){
             cpfAdjOB=(Long)obList.get(5);
           	 pensionAdjOB=(Long)obList.get(6);
             pfAdjOB=(Long)obList.get(7);
             empSubOB=(Long)obList.get(8);
             adjPensionContri=(Long)obList.get(9);
             adjOutstandadv=(Long)obList.get(10);
             }
          
  		}
  		String adjStation=pensionCardInfo.getStation();
  		
  		//added on 06-Jan-2012 for calcuting intrst for data after finalsettlemnt from the starting mnth onwards
  		emoluments  = Double.parseDouble(pensionCardInfo.getEmoluments());
  		//System.out.println("======DataAfterFinalsettlemnt()====="+pensionCardInfo.getDataAfterFinalsettlemnt());
  		if(pensionCardInfo.getDataAfterFinalsettlemnt().equals("true")){
  		if(emoluments>0){  	
  		datestartedFalg = "true";
  		aftrFinstlmntEmpNetTot= aftrFinstlmntEmpNetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntEmpNetTot()));
  		aftrFinstlmntAAINetTot = aftrFinstlmntAAINetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntAAINetTot()));
  		aftrFinstlmntPCNetTot= aftrFinstlmntPCNetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntPCNetTot()));  		 
  		} 
  		if(datestartedFalg.equals("true")){
  			monthsCntAfterFinstlmnt++;
  		}
  		}
  	 	//System.out.println("aftrFinstlmntAAINetTot"+aftrFinstlmntAAINetTot+"=aftrFinstlmntEmpNetTot="+aftrFinstlmntEmpNetTot+"====aftrFinstlmntPCNetTot=="+aftrFinstlmntPCNetTot);
  		
  		
  	//	System.out.println("final settlement date" +personalInfo.getFinalSettlementDate());
  		
  		if(((Integer.parseInt(calYear)>=2010 && dataFinalSettlementList.size()==0) && pensionCardInfo.getTransArrearFlag().equals("Y"))||((pensionCardInfo.getTransArrearFlag().equals("N")) ||(pensionCardInfo.getTransArrearFlag().equals("P")))||(pensionCardInfo.getTransArrearFlag().equals("Y")&&(personalInfo.getChkArrearFlag().equals("N")))){
  		totalEmoluments= new Double(df.format(totalEmoluments+Math.round(Double.parseDouble(pensionCardInfo.getEmoluments())))).doubleValue();
		pfStaturary= new Double(df.format(pfStaturary+Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury())))).doubleValue();
		empVpf = new Double(df.format(empVpf+Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf())))).doubleValue();
		principle =new Double(df.format(principle+Math.round(Double.parseDouble(pensionCardInfo.getPrincipal())))).doubleValue();
		interest =new Double(df.format(interest+Math.round(Double.parseDouble(pensionCardInfo.getInterest())))).doubleValue();
		empTotal=new Double(df.format(empTotal+Math.round(Double.parseDouble(pensionCardInfo.getEmptotal())))).doubleValue();		
	    advancePFWPaid= new Double(df.format(advancePFWPaid+Math.round( Double.parseDouble(pensionCardInfo.getAdvancePFWPaid())))).doubleValue();
	    advanceAmountPaid= new Double(df.format(advanceAmountPaid+Math.round( Double.parseDouble(pensionCardInfo.getAdvancesAmount())))).doubleValue();
	     PFWSubPaid= new Double(df.format(PFWSubPaid+Math.round( Double.parseDouble(pensionCardInfo.getPFWSubscri())))).doubleValue();
	     
	    
	     if(!pensionCardInfo.getGrandCummulative().equals("")){
	    		if(pensionCardInfo.isYearFlag()==true){
	    		totalEmpNet=totalEmpNet+new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();
	       		}else{
	    		totalEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();	    
	    			}
	    	    } 
	    System.out.println("GrandArrearEmpNetCummulative()"+pensionCardInfo.getGrandArrearEmpNetCummulative());

	   
	   	dispTotalEmpNet= new Double(df.format(dispTotalEmpNet+Math.round( Double.parseDouble(pensionCardInfo.getEmpNet())))).doubleValue();
	   	totalAdvances=new Double(df.format(totalAdvances+Math.round( Double.parseDouble(pensionCardInfo.getAdvancesAmount())))).doubleValue();
	   	
	    totalAAIPF=new Double(df.format(totalAAIPF+Math.round(Double.parseDouble(pensionCardInfo.getAaiPF())))).doubleValue();
	    totalPFDrwn= new Double(df.format(totalPFDrwn+Math.round( Double.parseDouble(pensionCardInfo.getPfDrawn())))).doubleValue();
	     
	    if(!pensionCardInfo.getGrandCummulative().equals("")){
	   	 	if(pensionCardInfo.isYearFlag()==true){
	    	 totalAAINet=totalAAINet+new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandAAICummulative()))).doubleValue();	   
	   		 }else{
	    	  totalAAINet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandAAICummulative()))).doubleValue();	   
	   		 }
	   }
	    System.out.println("GrandArrearEmpNetCummulative()"+pensionCardInfo.getGrandArrearEmpNetCummulative());
	
	   // totalAAINet= new Double(df.format(totalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiCummulative())))).doubleValue();
	    dispTotalAAINet= new Double(df.format(dispTotalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiNet())))).doubleValue();
	    
	    dispTotalPensionContr=new Double(df.format(dispTotalPensionContr+Math.round( Double.parseDouble(pensionCardInfo.getPensionContribution())))).doubleValue();
		pensionContriTot  =  new Double(df.format(pensionContriTot + Double.parseDouble(pensionCardInfo.getPensionContriAmnt()))).doubleValue();
		//System.out.println("==in JSp PC==========="+totalPensionContr);
		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
			 arrearRemarks="ARREARS";
		}else   arrearRemarks="---";
	     
  		}else{
  				 totalArrearEmpNet=totalArrearEmpNet+Math.round( Double.parseDouble(pensionCardInfo.getEmpNet()));
  				 totalArrearAAINet=totalArrearAAINet+Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()));
  				 totalArrearPCNet = totalArrearPCNet+Math.round(Double.parseDouble(pensionCardInfo.getPensionContriArrearAmnt()));
       			 arrearFlag="Y";
       			 arrearRemarks="ARREARS";
  		}
  			
  		 
  			if((!pensionCardInfo.getGrandArrearEmpNetCummulative().equals("") && totalArrearEmpNet!=0.00) ||(!pensionCardInfo.getGrandArrearEmpNetCummulative().equals("") && isFrozedSeperation==true)){
	    	
	    	totalGrandArrearEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearEmpNetCummulative()))).doubleValue();
    		}
  		    if((!pensionCardInfo.getGrandArrearAAICummulative().equals("") && totalArrearEmpNet!=0.00) ||(!pensionCardInfo.getGrandArrearAAICummulative().equals("") && isFrozedSeperation==true) ){
	    	
	    	totalGrandArrearAaiNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearAAICummulative()))).doubleValue();
    		} 
    	
    	 
  		  if(pensionCardInfo.getMergerflag().equals("Y") ){
      		if(!arrearRemarks.equals("---")){
      		arrearRemarks=arrearRemarks+","+pensionCardInfo.getMergerremarks();
      		}else{
      			arrearRemarks=pensionCardInfo.getMergerremarks();
      		}
  			
  		}
  		 
  			pfcardNarration = pensionCardInfo.getPfcardNarration();
  			if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y") || pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
  			if(!arrearRemarks.equals("---")){
  			arrearRemarks = arrearRemarks+","+pfcardNarration;
  			}else{
  			arrearRemarks = pfcardNarration;
  			}  			
  			}
  			 
  	%>
 <span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
  	<%if(obFlag.equals("Y")){%>
  	  <tr>
        <td colspan="3" class="label">OPENING BALANCE (OB)</td>
        <td>&nbsp;</td>        
        <td class="NumData"><%=principalOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>       
        <td>&nbsp;</td> 
        <td class="NumData" width="6%" align="right"><input type="text"	readonly="true" size="5"     maxlength="9"     name="empnetob" id="empnetob"
		value='<%=empNetOB%>'	onkeypress="numsDotOnly()"/>
		</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>       
        <td class="NumData" width="6%" align="right"><input type="text"	readonly="true" size="5"     maxlength="9"     name="aainetob" id="aainetob"
		value='<%=aaiNetOB%>'	onkeypress="numsDotOnly()"/>
		</td>
         <td class="NumData">0</td><!--
      <td class="Data"><input type="button" name="obedit"  value="E" style ="cursor:hand;"	onclick="editOBValues('<%=personalInfo.getPensionNo()%>','empnetob','aainetob','<%=adjOBYear%>','obedit')" />
	 	</td>	  			
        -->
        <td>&nbsp;</td>
        <td>&nbsp;</td>
     	<td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
  <% System.out.println("===========totalEmpNet"+totalEmpNet+"totalAAINet"+pensionAdjOB);%>
       <tr>
        <td colspan="3" class="label">ADJ  IN OB</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=adjOutstandadv%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=empSubOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        
<% 
     if(!pensionAdjOB.equals("0")&& Integer.parseInt(pensionAdjOB.toString())!=0 && (dispYear.equals("2009-10")||dispYear.equals("2010-11"))){%>
 <td class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./search1?method=getEmolumentslog&pfId=<%=personalInfo.getPfID()%>&airportcode=<%=adjStation%>"><%=pensionAdjOB%></a></td>
 <%} else{%>
 <td class="NumData"><%=pensionAdjOB%></td>
<%} %>
        <td class="NumData"><%=adjPensionContri%></td>
        <td>&nbsp;</td>
           <%if (!adjNarration.equals("---")&& !adjNarration.trim().equals("")){%>
	   <td width="2%" class="Data"  onmouseover="showTip('<%=adjNarration%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=adjNarration.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="Data">&nbsp;</td>
	 <%}%>
       
       
      </tr>
      <%}%>
      
	 <tr>
	 
	 <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getShnMnthYear()%></td>
	  <input type="hidden"
					 name="monthyear<%=i%>" value='<%=pensionCardInfo.getShnMnthYear()%>'/> 
	 <td class="Data" width="6%" align="right"><input type="text"	readonly="true" size="5"  maxlength="8"  name="emoluments<%=i%>" id="emoluments<%=i%>"
		value='<%=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()))%>'	onkeypress="numsDotOnly()"/>
	</td>
	  <td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="epf<%=i%>"  id="epf<%=i%>"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury()))%>'/>
	</td>
	<td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="vpf<%=i%>" id="vpf<%=i%>"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf()))%>' />
	</td>				
	<td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="principle<%=i%>"  id="principle<%=i%>"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo.getPrincipal()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="interest<%=i%>" id="interest<%=i%>"
		size="5"  maxlength="8"  value='<%=Math.round(Double.parseDouble(pensionCardInfo.getInterest()))%>' />
	</td>
	<td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmptotal()))%></td>
	
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="advance<%=i%>" id="advance<%=i%>"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo.getAdvancesAmount()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pfwsub<%=i%>" id="pfwsub<%=i%>"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo.getPFWSubscri()))%>' />
	</td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpNet()))%></td>
	 
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="aaipf<%=i%>" id="aaipf<%=i%>"
		size="5"  maxlength="8"  value='<%=Math.round(Double.parseDouble(pensionCardInfo.getAaiPF()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pfwcontri<%=i%>" id="pfwcontri<%=i%>"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo.getPFWContri()))%>' />
	</td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()))%></td>
	 
	 <td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pencontri<%=i%>" id="pencontri<%=i%>"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%>' />
		<!-- <input type="hidden"   name="emolumentsmnts<%=i%>"  size="1"  value='<%=pensionCardInfo.getEmolumentMonths()%>' />  -->
	 </td>
	  
	 <td class="Data" width="6%" align="right"> <select name="emolumentsmnts<%=i%>"  id="emolumentsmnts<%=i%>" disabled="disabled" style="width:70px"tabindex="6">
				<%  
				noofMonths =pensionCardInfo.getEmolumentMonths().trim();
				  //System.out.println("no of Months======="+noofMonths);
				  	// As Per instructioons by  Sehgal  on 14-Mar-2012 we  add 29,30 & 31st in noof months

                %>
												
													<option value="1" <%if(noofMonths.equals("1")){ out.println("selected");}%>>
														one Month
													</option>
													<option value="0.5" <%if(noofMonths.equals("0.5")){ out.println("selected");}%>>
														half Month
													</option>
                                                    <option value="3" <%if(noofMonths.equals("3")){ out.println("selected");}%>>
														3 Days
													</option>
  													<option value="4" <%if(noofMonths.equals("4")){ out.println("selected");}%>>
														4 Days
													</option>
												<option value="5" <%if(noofMonths.equals("5")){ out.println("selected");}%>>
														5 Days
													</option>
                                                   <option value="6" <%if(noofMonths.equals("6")){ out.println("selected");}%>>
														6 Days
													</option>
  													<option value="7" <%if(noofMonths.equals("7")){ out.println("selected");}%>>
														7 Days
													</option>
												<option value="8" <%if(noofMonths.equals("8")){ out.println("selected");}%>>
														8 Days
													</option>
												<option value="9" <%if(noofMonths.equals("9")){ out.println("selected");}%>>
														9 Days
													</option>
													<option value="10" <%if(noofMonths.equals("10")){ out.println("selected");}%>>
														10 Days
													</option>
													 <option value="11" <%if(noofMonths.equals("11")){ out.println("selected");}%>>
														11 Days
													</option>
  													<option value="12" <%if(noofMonths.equals("12")){ out.println("selected");}%>>
														12 Days
													</option>
												<option value="13" <%if(noofMonths.equals("13")){ out.println("selected");}%>>
														13 Days
													</option>
                                                   <option value="14" <%if(noofMonths.equals("14")){ out.println("selected");}%>>
														14 Days
													</option>
  													<option value="15" <%if(noofMonths.equals("15")){ out.println("selected");}%>>
														15 Days
													</option>
												<option value="16" <%if(noofMonths.equals("16")){ out.println("selected");}%>>
														16 Days
													</option>
												<option value="17" <%if(noofMonths.equals("17")){ out.println("selected");}%>>
														17 Days
													</option>
													<option value="18" <%if(noofMonths.equals("18")){ out.println("selected");}%>>
														18 Days
													</option>
													<option value="19" <%if(noofMonths.equals("19")){ out.println("selected");}%>>
														19 Days
													</option>
													 <option value="20" <%if(noofMonths.equals("20")){ out.println("selected");}%>>
														20 Days
													</option>
  													<option value="21" <%if(noofMonths.equals("21")){ out.println("selected");}%>>
														21 Days
													</option>
												<option value="22" <%if(noofMonths.equals("22")){ out.println("selected");}%>>
														22 Days
													</option>
                                                   <option value="23" <%if(noofMonths.equals("23")){ out.println("selected");}%>>
														23 Days
													</option>
  													<option value="24" <%if(noofMonths.equals("24")){ out.println("selected");}%>>
														24 Days
													</option>
												<option value="25" <%if(noofMonths.equals("25")){ out.println("selected");}%>>
														25 Days
													</option>
												<option value="26" <%if(noofMonths.equals("26")){ out.println("selected");}%>>
														26 Days
													</option>
													<option value="27" <%if(noofMonths.equals("27")){ out.println("selected");}%>>
														27 Days
													</option>
													<option value="28" <%if(noofMonths.equals("28")){ out.println("selected");}%>>
														28 Days
													</option>	
													<option value="29" <%if(noofMonths.equals("29")){ out.println("selected");}%>>
														29 Days
													</option>
													<option value="30" <%if(noofMonths.equals("30")){ out.println("selected");}%>>
														30 Days
													</option>
													<option value="31" <%if(noofMonths.equals("31")){ out.println("selected");}%>>
														31 Days
													</option>						
													<option value="2" <%if(noofMonths.equals("2")){ out.println("selected");}%>>
														2 Months
													</option>
													 <option value="90" <%if(noofMonths.equals("90")){ out.println("selected");}%>>
														3 Months
													</option>
                                                     <option value="120" <%if(noofMonths.equals("120")){ out.println("selected");}%>>
														4 Months
													</option>
												 <option value="150" <%if(noofMonths.equals("150")){ out.println("selected");}%>>
														5 Months
													</option>
												<option value="180" <%if(noofMonths.equals("180")){ out.println("selected");}%>>
														6 Months
													</option>
											<option value="210" <%if(noofMonths.equals("210")){ out.println("selected");}%>>
														7 Months
													</option>
										<option value="240" <%if(noofMonths.equals("240")){ out.println("selected");}%>>
														8 Months
													</option>
										<option value="270" <%if(noofMonths.equals("270")){ out.println("selected");}%>>
														9 Months
													</option>
										<option value="300" <%if(noofMonths.equals("300")){ out.println("selected");}%>>
														10 Months
													</option>
<option value="330" <%if(noofMonths.equals("330")){ out.println("selected");}%>>
														11 Months
													</option>
<option value="360" <%if(noofMonths.equals("360")){ out.println("selected");}%>>
														12 Months
													</option>
<option value="390" <%if(noofMonths.equals("390")){ out.println("selected");}%>>
														13 Months
													</option>
<option value="420" <%if(noofMonths.equals("420")){ out.println("selected");}%>>
														14 Months
													</option>
<option value="450" <%if(noofMonths.equals("450")){ out.println("selected");}%>>
														15 Months
													</option>
												</select>
                     </td>
	  
	 <!-- <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury()))%></td>  
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf()))%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPrincipal()))%></td>
	 <td width="7%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getInterest()))%></td>     
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmptotal()))%></td>  
	  <td width="2%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAdvancePFWPaid()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpNet()))%></td>
	
 	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiPF()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPfDrawn()))%></td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()))%></td>

	 <td width="12%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%></td> -->  
	 <td class="Data"><input type="button" style ="cursor:hand;" name="edit<%=i%>"  id="edit<%=i%>" value="E"	onclick="editEmoluments('<%=pensionCardInfo.getMonthyear()%>','<%=pensionCardInfo.getCpfAccno()%>','<%=pensionCardInfo.getRegion()%>','<%=pensionCardInfo.getStation()%>','<%=personalInfo.getPfID()%>','emoluments<%=i%>','epf<%=i%>','vpf<%=i%>','principle<%=i%>','interest<%=i%>','advance<%=i%>','pfwsub<%=i%>','aaipf<%=i%>','pfwcontri<%=i%>','pencontri<%=i%>','emolumentsmnts<%=i%>','<%=personalInfo.getWetherOption().trim()%>','edit<%=i%>','<%=adjOBYear%>','<%=personalInfo.getDateOfBirth()%>','pfcardnarration<%=i%>')" />
	 </td>
 				  
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getStation()%></td>
	 
	  
	 	<td width="2%" class="Data"><input type="text"
					readonly="true"  name="pfcardnarration<%=i%>" id="pfcardnarration<%=i%>"
					value='<%=arrearRemarks%>'/>  </td>
	  
	 </tr>
	
	 <%
	  System.out.println("===========totalEmpNet"+totalEmpNet+"totalAAINet"+totalAAINet+"==========");
		
	 	if(pensionCardInfo.getCbFlag().equals("Y")){
	 		
	 		if(obList.size()!=0){
	 				if(Integer.parseInt(calYear)>=1995 && Integer.parseInt(calYear)<2000){
		 				rateOfInterest=12;
	 				}else if(Integer.parseInt(calYear)>=2000 && Integer.parseInt(calYear)<2001){
	 					rateOfInterest=11;
	 				}else if(Integer.parseInt(calYear)>=2001 && Integer.parseInt(calYear)<2005){
	 					rateOfInterest=9.50;
	 				}else if(Integer.parseInt(calYear)>=2005 && Integer.parseInt(calYear)<2010){
	 					rateOfInterest=8.50;
	 				}else if(Integer.parseInt(calYear)>=2010 && Integer.parseInt(calYear)<2012){
	 					rateOfInterest=9.50;
	 				}else if(Integer.parseInt(calYear)>=2012 && Integer.parseInt(calYear)<2014){
	 					rateOfInterest=9.5;
	 				}else if(Integer.parseInt(calYear)>=2014 && Integer.parseInt(calYear)<2015){
	 					rateOfInterest=9.25;
	 				}else if(Integer.parseInt(calYear)>=2015 && Integer.parseInt(calYear)<2016){
	 					rateOfInterest=9.15;
	 				}				
	 				 	   if(!personalInfo.getFinalSettlementDate().equals("---")){
			        	
						if(Integer.parseInt(calYear)>=2014 && Integer.parseInt(calYear)<=2015){
							//if ((commonDAO.compareFinalSettlementDates("01-Apr-2014","31-Mar-2015",personalInfo.getFinalSettlementDate())==true)){
			            		//finalrateofinterest=true;
			        		//}else 
			        		if(!personalInfo.getResttlmentDate().equals("---")){
			        			if ((commonDAO.compareFinalSettlementDates("01-Apr-2014","31-Mar-2015",personalInfo.getResttlmentDate())==true)){
			                		finalrateofinterest=true;
			        			}
			        		}
			        	}
			        }
			         if(finalrateofinterest==true){
					  rateOfInterest = 8.25;
				  }
	 				
				    if (intMonths!=0){
	 					 empNetInterest=new Double(df.format(((totalEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
	 				    aaiNetInterest=new Double(df.format(((totalAAINet*rateOfInterest)/100/intMonths))).doubleValue();
	 				    pensionInterest=new Double(Math.round(((pensionContriTot*rateOfInterest)/100/intMonths))).doubleValue();
	 				     System.out.println("totalEmpNet=="+totalEmpNet+"empNetInterest="+empNetInterest+"====pensionContriTot=="+pensionContriTot+"=intMonths=="+intMonths);
	 				}else{
	 					empNetInterest=0.0;
	 					aaiNetInterest=0.0;
	 					pensionInterest=0.0;
	 				}
	 				 System.out.println("===arrearMonths=="+arrearMonths+"=isFrozedSeperation=="+isFrozedSeperation);
	 				if(arrearMonths!=0 && isFrozedSeperation==true){
	 					 arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 arrearPCInterest=new Double(df.format(((pensionContriTot*rateOfInterest)/100/arrearMonths))).doubleValue();
						 empNetInterest=empNetInterest+arrearEmpNetInterest;
						 aaiNetInterest=aaiNetInterest+arrearAaiNetInterest;
						 pensionInterest=pensionInterest+arrearPCInterest;
						 System.out.println("----rateOfInterest------"+rateOfInterest+"arrearEmpNetInterest"+arrearEmpNetInterest+"----totalGrandArrearEmpNet---ssss---"+totalGrandArrearEmpNet+"arrearMonths"+arrearMonths+"-----arrearPCInterest---"+arrearPCInterest+"-----pensionContriTot---"+pensionContriTot);
	 				}
				    
				      System.out.println("isArreerintflag"+personalInfo.isArreerintflag()+"isFrozedSeperation"+isFrozedSeperation);
				     
				    if(totalGrandArrearEmpNet!=0.0 && totalGrandArrearAaiNet!=0.0 && personalInfo.isArreerintflag()!=true && isFrozedSeperation==false){
				    arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
				    arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/intMonths))).doubleValue();
				 	pensionArrearInterest =new Double(df.format(((pensionContriArrearTot*rateOfInterest)/100/intMonths))).doubleValue();
				    }else{
				    	arrearEmpNetInterest=0.0;
				    	arrearAaiNetInterest=0.0;
				    	pensionArrearInterest=0.0;
				    }
				     System.out.println("arrearEmpNetInterest"+arrearEmpNetInterest+"arrearAaiNetInterest"+arrearAaiNetInterest+"noOfmonthsForInterest"+noOfmonthsForInterest);
				    //For Pension Contribution attribute,we are not cummlative
				    System.out.println("===================");
				   // pensionInterest=new Double(Math.round(((pensionContriTot*rateOfInterest)/100/intMonths))).doubleValue();
				    totalEmpIntNet=new Double(Math.round(empNetInterest+totalEmpNet)).doubleValue();
				    totalAaiIntNet=new Double(Math.round(totalAAINet+aaiNetInterest)).doubleValue();
				    System.out.println("totalNoOfMonths"+noOfmonthsForInterest+"Cummulative Interest Months"+intMonths+"empNetInterest"+empNetInterest+"aaiNetInterest"+aaiNetInterest);
				 	
				 	  System.out.println("----obList Kp1 size=="+obList.size());
				 	closingOBList=reportService.calClosingOB(rateOfInterest,obList,Math.round(dispTotalAAINet),Math.round(aaiNetInterest),Math.round(dispTotalEmpNet),Math.round(empNetInterest),Math.round(totalPensionContr),Math.round(pensionInterest),totalAdvances,principle,noOfmonthsForInterest,personalInfo.isObInterst(),personalInfo.isAdjObInterst());
					 System.out.println("----obList Kp2 size=="+obList.size());
				 														
	 				if(closingOBList.size()!=0){
					 	finalEmpNetOB=(Long)closingOBList.get(0);
					 	finalAaiNetOB=(Long)closingOBList.get(1);
					 	finalPensionOB=(Long)closingOBList.get(2);
					 	
					 	finalEmpNetOBIntrst=(Long)closingOBList.get(4);
					 	finalAaiNetOBIntrst=(Long)closingOBList.get(5);
					 	finalPensionOBIntrst=(Long)closingOBList.get(6);
					 	finalPrincipalOB=(Long)closingOBList.get(7);
					 	System.out.println("====,finalEmpNetOBIntrst======"+finalEmpNetOBIntrst+"=finalAaiNetOBIntrst=="+finalAaiNetOBIntrst+"==finalPensionOBIntrst=="+finalPensionOBIntrst+"==pensionInterest=="+pensionInterest);
				 	//Cond for Not Calc Interest on PC after Sep+3 Years
				  if(finalEmpNetOBIntrst.intValue()==0 && finalAaiNetOBIntrst.intValue() ==0){
				  pensionInterest =0;
				  }
				 	tempFinalPensionOB= new Double(dispTotalPensionContr).longValue() + new Double(pensionInterest).longValue()+ new Double(totalArrearPCNet).longValue();;
				 	
				 	}
	 		}

	 %>
	  
	 <tr>
	 <td  nowrap="nowrap" class="Data">YEAR TOTAL </td>
	 <td  nowrap="nowrap" class="NumData"><%=df1.format(totalEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(pfStaturary)%></td>
  	 <td width="3%" class="NumData"><%=df1.format(empVpf)%></td>
     <td width="5%" class="NumData"><%=df1.format(principle)%></td>
	 <td width="7%" class="NumData"><%=df1.format(interest)%></td>
	 <td width="6%" nowrap="nowrap" class="NumData"><%=df1.format(empTotal)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(advanceAmountPaid)%></td>
	  <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(PFWSubPaid)%></td>
	 <td width="3%"  nowrap="nowrap" class="NumData"><%=df1.format(dispTotalEmpNet)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalAAIPF)%></td>
	 <td width="6%" class="NumData"><%=df1.format(totalPFDrwn)%></td>
	 <td width="3%" class="NumData"><%=df1.format(dispTotalAAINet)%></td>
	 <td width="12%" class="NumData"><%=df1.format(dispTotalPensionContr+totalArrearPCNet)%></td>
	  <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 </tr>
	  <tr>
	 <td width="50" nowrap="nowrap" class="label">INTEREST</td>
	 <td width="116" class="NumData"><%=rateOfInterest%></td>

	  <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOBIntrst)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOBIntrst)%></td>
	   <td width="3%" class="NumData" nowrap="nowrap"><%=new Double(pensionInterest).longValue()%></td>
	  <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalPrincipalOB)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOB)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOB)%></td>
	 
	 <td width="12%" class="NumData"><%=tempFinalPensionOB%></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%System.out.println("dataFinalSettlementList===pf"+dataFinalSettlementList.size());
	if(dataFinalSettlementList.size()!=0){
	finalStlmentEmpNet=new Long(Long.parseLong((String)dataFinalSettlementList.get(5)));
	finalStlmentAAICon=new Long(Long.parseLong((String)dataFinalSettlementList.get(6)));
	finalStlmentPensDet=new Long(Long.parseLong((String)dataFinalSettlementList.get(7)));
	finalStlmentNetAmount=new Long(Long.parseLong((String)dataFinalSettlementList.get(8)));
	finalInterestCal=12-noOfmonthsForInterest;
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==false){
		noOfAfterfnlrtmntmonhts=12-noOfmonthsForInterest;
	}
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==true ){
		finalInterestCal=0;
	}
	
	System.out.println("dataFinalSettlementList"+(String)dataFinalSettlementList.get(8)+"finalStlmentEmpNet=="+finalStlmentEmpNet+"finalStlmentNetAmount"+finalStlmentNetAmount);
	long netcloseEmpNet=(finalEmpNetOB.longValue())+(-finalStlmentEmpNet.longValue());
	long netcloseNetAmount=(finalAaiNetOB.longValue())+(-finalStlmentAAICon.longValue());
	double remaininInt1=arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
	double remainAaiInt1=arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	double remainPCInt =0.0;
	   
	System.out.println("personalInfo.getFinalSettlementDate()"+personalInfo.getFinalSettlementDate()+"Resettlemnt date"+personalInfo.getResttlmentDate()+netcloseEmpNet+"finalInterestCal"+finalInterestCal);
	System.out.println("totalArrearEmpNet"+totalArrearEmpNet+"arrearAaiNetInterest"+arrearAaiNetInterest+netcloseEmpNet);
	System.out.println("finalInterestCal"+finalInterestCal+"noOfAfterfnlrtmntmonhts-----------"+noOfAfterfnlrtmntmonhts+"========isFrozedSeperation========="+isFrozedSeperation+"noOfmonthsForInterest"+noOfmonthsForInterest);
	if(!personalInfo.getFinalSettlementDate().equals("---")){
			if(!personalInfo.getResttlmentDate().equals("---")){
				 remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			 remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			  remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
			}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
				 remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*finalInterestCal);
			}
	}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
			 	 remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*finalInterestCal);
	
	} 	
 	
	
	  // added on 06-Jan-2012 for Final Settlement Case 
					  
					  double finalEmpNetClosingOB =0.0,finalAAINetClosingOB =0.0 ,finalPCNetClosingOB =0.0;
					  finalEmpNetClosingOB = Double.parseDouble(String.valueOf(netcloseEmpNet)) + remaininInt ; 					
					  finalAAINetClosingOB = Double.parseDouble(String.valueOf(netcloseNetAmount)) + remainAaiInt;
					  //By Radha On 27-Apr-2012 For restricting of  calc of intrst amnt for PensionContri 
					  finalPCNetClosingOB =  Double.parseDouble(String.valueOf(tempFinalPensionOB))  ;
					 System.out.println("==***==***==finalPCNetClosingOB==="+tempFinalPensionOB+"remainPCInt"+remainPCInt);  
					 System.out.println("==*******==finalAAINetClosingOB==="+finalAAINetClosingOB+"=finalPCNetClosingOB=="+finalPCNetClosingOB);  
					   finalEmpNetOB =   new Long(new Double(Double.toString(finalEmpNetClosingOB)).longValue());
					 //  finalEmpNetOBIntrst =  new Long(new Double(Double.toString(remaininInt)).longValue());					   	
					   finalAaiNetOB =  new Long(new Double(Double.toString(finalAAINetClosingOB)).longValue());				   
					  // finalAaiNetOBIntrst =  new Long(new Double(Double.toString(remainAaiInt)).longValue());
					   //pensionInterest = 	 remainPCInt ;			 
					   tempFinalPensionOB = 	new Double(Double.toString(finalPCNetClosingOB)).longValue();
					    System.out.println("==*******====="+finalPCNetClosingOB);
 %>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">FINAL SETTLEMENT (<%=(String)dataFinalSettlementList.get(11)%>)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=-finalStlmentEmpNet.longValue()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=-finalStlmentAAICon.longValue()%></td>
	 <td colspan="5" class="Data"><%=orderInfo%></td>
 </tr>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NET CLOSING (A)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=netcloseEmpNet%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=netcloseNetAmount%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
   
 <%
 System.out.println("arrearFlag==================="+arrearFlag);
 if(arrearFlag.equals("Y")){%>
      <tr>
	 <td colspan="2" nowrap="nowrap" class="label">ARREARS AMOUNT (B)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(totalArrearEmpNet)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(totalArrearAAINet)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (C)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt1)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt1)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>

 <%}else{%>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (B)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt)%></td>
	 <td colspan="5" class="Data">&nbsp;</td>
 </tr>
 	
<%}%> 
<tr>
 <% if(arrearFlag.equals("Y")){
 //Add By Radha On 31-Jan-2013 as per Sehgal Pfid: 8765
    ArrearInFSPeriod="true";
 %>
	 <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING (A+B+C)</td>
      <%}else{ %>
   <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING(A+B) </td>
     <%} %>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(netcloseEmpNet+remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(netcloseNetAmount+remainAaiInt)%></td>	 
	 <td colspan="5" class="Data"><%=df1.format(tempFinalPensionOB)%></td>
 </tr>
 <%if(!personalInfo.getAdjDate().equals("---")){%>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label"><%=personalInfo.getAdjRemarks()%> </td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%}%>
   <!-- <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>-->
 <%}else{ %>
 
 <% System.out.println("dispYear==="+dispYear);	
 if(dispYear.equals("2012-2013")){
 pensionCardInfo1=(EmployeePensionCardInfo)dateCardList1.get(0);
        aprEmpNet=Double.parseDouble(pensionCardInfo1.getEmpNet());
 		aprAaiNet=Double.parseDouble(pensionCardInfo1.getAaiNet());
 		aprPrincipal=Double.parseDouble(pensionCardInfo1.getPrincipal());
 		aprpc=Double.parseDouble(pensionCardInfo1.getPensionContribution());
 		grandaprEmpNet=Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal;
	    grandaprAaiNet=Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet;
	    grandaprPrincipal=Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet;
	    grandaprpc=Double.parseDouble(df1.format(tempFinalPensionOB))+ aprpc;
 		String aprremarks="---"; 
 		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
 		aprremarks="ARREARS";
 		}else {
 		aprremarks="---";
 		}
 		 if(pensionCardInfo.getMergerflag().equals("Y") ){
 		 aprremarks=aprremarks+","+pensionCardInfo.getMergerremarks();
 		 }else{
 		 aprremarks=pensionCardInfo.getMergerremarks();
 		 }
 		 if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y") || pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
  			aprremarks = aprremarks+","+pensionCardInfo.getPfcardNarrationFlag();
  			
  			}
 		%>
 <tr>
		 
	 <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getShnMnthYear()%>(MarSal)</td>
	  <input type="hidden"
					 name="monthyear<%=i%>" value='<%=pensionCardInfo1.getShnMnthYear()%>'/> 
	 <td class="Data" width="6%" align="right"><input type="text"	readonly="true" size="5"  maxlength="8"  name="emoluments" id="emoluments"
		value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getEmoluments()))%>'	onkeypress="numsDotOnly()"/>
	</td>
	  <td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="epf"  id="epf"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getEmppfstatury()))%>'/>
	</td>
	<td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="vpf" id="vpf"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpvpf()))%>' />
	</td>				
	<td class="Data" width="6%" align="right"><input type="text"	readonly="true" onkeypress="numsDotOnly()" name="principle"  id="principle"
		size="5"  maxlength="8"	value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getPrincipal()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="interest" id="interest"
		size="5"  maxlength="8"  value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getInterest()))%>' />
	</td>
	<td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmptotal()))%></td>
	
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="advance" id="advance"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getAdvancesAmount()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pfwsub" id="pfwsub"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getPFWSubscri()))%>' />
	</td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpNet()))%></td>
	 
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="aaipf" id="aaipf"
		size="5"  maxlength="8"  value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiPF()))%>' />
	</td>
	<td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pfwcontri" id="pfwcontri"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getPFWContri()))%>' />
	</td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiNet()))%></td>
	 
	 <td class="Data" width="6%" align="right"><input type="text"  readonly="true" onkeypress="numsDotOnly()" name="pencontri" id="pencontri"
		size="5"   maxlength="8" value='<%=Math.round(Double.parseDouble(pensionCardInfo1.getPensionContribution()))%>' />
	 </td>
	  
	 <td class="Data" width="6%" align="right"> <select name="emolumentsmnts"  id="emolumentsmnts" disabled="disabled" style="width:70px"tabindex="6">
				<%  
				noofMonths =pensionCardInfo1.getEmolumentMonths().trim();
                %>
												
													<option value="1" <%if(noofMonths.equals("1")){ out.println("selected");}%>>
														one Month
													</option>
													<option value="0.5" <%if(noofMonths.equals("0.5")){ out.println("selected");}%>>
														half Month
													</option>
                                                    <option value="3" <%if(noofMonths.equals("3")){ out.println("selected");}%>>
														3 Days
													</option>
  													<option value="4" <%if(noofMonths.equals("4")){ out.println("selected");}%>>
														4 Days
													</option>
												<option value="5" <%if(noofMonths.equals("5")){ out.println("selected");}%>>
														5 Days
													</option>
                                                   <option value="6" <%if(noofMonths.equals("6")){ out.println("selected");}%>>
														6 Days
													</option>
  													<option value="7" <%if(noofMonths.equals("7")){ out.println("selected");}%>>
														7 Days
													</option>
												<option value="8" <%if(noofMonths.equals("8")){ out.println("selected");}%>>
														8 Days
													</option>
												<option value="9" <%if(noofMonths.equals("9")){ out.println("selected");}%>>
														9 Days
													</option>
													<option value="10" <%if(noofMonths.equals("10")){ out.println("selected");}%>>
														10 Days
													</option>
													 <option value="11" <%if(noofMonths.equals("11")){ out.println("selected");}%>>
														11 Days
													</option>
  													<option value="12" <%if(noofMonths.equals("12")){ out.println("selected");}%>>
														12 Days
													</option>
												<option value="13" <%if(noofMonths.equals("13")){ out.println("selected");}%>>
														13 Days
													</option>
                                                   <option value="14" <%if(noofMonths.equals("14")){ out.println("selected");}%>>
														14 Days
													</option>
  													<option value="15" <%if(noofMonths.equals("15")){ out.println("selected");}%>>
														15 Days
													</option>
												<option value="16" <%if(noofMonths.equals("16")){ out.println("selected");}%>>
														16 Days
													</option>
												<option value="17" <%if(noofMonths.equals("17")){ out.println("selected");}%>>
														17 Days
													</option>
													<option value="18" <%if(noofMonths.equals("18")){ out.println("selected");}%>>
														18 Days
													</option>
													<option value="19" <%if(noofMonths.equals("19")){ out.println("selected");}%>>
														19 Days
													</option>
													 <option value="20" <%if(noofMonths.equals("20")){ out.println("selected");}%>>
														20 Days
													</option>
  													<option value="21" <%if(noofMonths.equals("21")){ out.println("selected");}%>>
														21 Days
													</option>
												<option value="22" <%if(noofMonths.equals("22")){ out.println("selected");}%>>
														22 Days
													</option>
                                                   <option value="23" <%if(noofMonths.equals("23")){ out.println("selected");}%>>
														23 Days
													</option>
  													<option value="24" <%if(noofMonths.equals("24")){ out.println("selected");}%>>
														24 Days
													</option>
												<option value="25" <%if(noofMonths.equals("25")){ out.println("selected");}%>>
														25 Days
													</option>
												<option value="26" <%if(noofMonths.equals("26")){ out.println("selected");}%>>
														26 Days
													</option>
													<option value="27" <%if(noofMonths.equals("27")){ out.println("selected");}%>>
														27 Days
													</option>
													<option value="28" <%if(noofMonths.equals("28")){ out.println("selected");}%>>
														28 Days
													</option>	
													<option value="29" <%if(noofMonths.equals("29")){ out.println("selected");}%>>
														29 Days
													</option>
													<option value="30" <%if(noofMonths.equals("30")){ out.println("selected");}%>>
														30 Days
													</option>
													<option value="31" <%if(noofMonths.equals("31")){ out.println("selected");}%>>
														31 Days
													</option>						
													<option value="2" <%if(noofMonths.equals("2")){ out.println("selected");}%>>
														2 Months
													</option>
													 <option value="90" <%if(noofMonths.equals("90")){ out.println("selected");}%>>
														3 Months
													</option>
                                                     <option value="120" <%if(noofMonths.equals("120")){ out.println("selected");}%>>
														4 Months
													</option>
												 <option value="150" <%if(noofMonths.equals("150")){ out.println("selected");}%>>
														5 Months
													</option>
												<option value="180" <%if(noofMonths.equals("180")){ out.println("selected");}%>>
														6 Months
													</option>
											<option value="210" <%if(noofMonths.equals("210")){ out.println("selected");}%>>
														7 Months
													</option>
										<option value="240" <%if(noofMonths.equals("240")){ out.println("selected");}%>>
														8 Months
													</option>
										<option value="270" <%if(noofMonths.equals("270")){ out.println("selected");}%>>
														9 Months
													</option>
										<option value="300" <%if(noofMonths.equals("300")){ out.println("selected");}%>>
														10 Months
													</option>
<option value="330" <%if(noofMonths.equals("330")){ out.println("selected");}%>>
														11 Months
													</option>
<option value="360" <%if(noofMonths.equals("360")){ out.println("selected");}%>>
														12 Months
													</option>
<option value="390" <%if(noofMonths.equals("390")){ out.println("selected");}%>>
														13 Months
													</option>
<option value="420" <%if(noofMonths.equals("420")){ out.println("selected");}%>>
														14 Months
													</option>
<option value="450" <%if(noofMonths.equals("450")){ out.println("selected");}%>>
														15 Months
													</option>
												</select>
                     </td>
	 <td class="Data"><input type="button" style ="cursor:hand;" name="edit"  id="edit" value="E"	onclick="editEmoluments('<%=pensionCardInfo1.getMonthyear()%>','<%=pensionCardInfo1.getCpfAccno()%>','<%=pensionCardInfo1.getRegion()%>','<%=pensionCardInfo1.getStation()%>','<%=personalInfo.getPfID()%>','emoluments','epf','vpf','principle','interest','advance','pfwsub','aaipf','pfwcontri','pencontri','emolumentsmnts','<%=personalInfo.getWetherOption().trim()%>','edit','<%=adjOBYear%>','<%=personalInfo.getDateOfBirth()%>','pfcardnarration')" />
	 </td>
 				  
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getStation()%></td>
	 
	  
	 	<td width="2%" class="Data"><input type="text"
					readonly="true"  name="pfcardnarration" id="pfcardnarration"
					value='<%=arrearRemarks%>'/>  </td>
	  
	 </tr>
	
	
	<tr>
	 
	<!-- <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getShnMnthYear()%>(MarSal)</td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmoluments()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmppfstatury()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpvpf()))%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPrincipal()))%></td>
	 <td width="7%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getInterest()))%></td>
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmptotal()))%></td>
	  <td width="2%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAdvancePFWPaid()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpNet()))%></td>
	
 	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiPF()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPfDrawn()))%></td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiNet()))%></td>

	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPensionContribution()))%></td>
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getStation()%></td>-->
	
	   <%if (!aprremarks.equals("---")){ %>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=aprremarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=aprremarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="8%" class="Data"><%=aprremarks%></td>
	 <%}%>
	 </tr>

	  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(grandaprEmpNet)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(grandaprAaiNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(grandaprPrincipal)%></td>
	 
	 <td width="12%" class="NumData"><%=df1.format(grandaprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
<!-- 	 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NEW CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet)%></td>
	 <td width="6%" class="NumData"><%=df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr> -->
 <% } %>
    <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB))+aprEmpNet+aprAaiNet)%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 
 <!--    <tr>
 
	 <td colspan="9" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>-->
 <%}%>
 <tr>
    <td colspan="16">&nbsp;</td>
 </tr>
  <%totalEmpNet=0;totalEmoluments=0;totalAAIPF=0;pfStaturary=0;totalPFDrwn=0;empTotal=0;totalAAINet=0;totalPensionContr=0;}%>
    </tr>

  <%
  noofMonths="1";
  }}%>
  
   <%  
   System.out.println("==remaininInt="+remaininInt+"==remainAaiInt=="+remainAaiInt);
   
   if(ArrearInFSPeriod.equals("true")){
    remaininInt_Saved_Value= remaininInt - totalArrearEmpNet;
   remainAaiInt_Saved_Value= remainAaiInt - totalArrearAAINet ;
   }else{
   
   remaininInt_Saved_Value= remaininInt ;
   remainAaiInt_Saved_Value= remainAaiInt;
   
   }
   					grandaprEmpNet=Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal;
	                grandaprAaiNet=Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet;
	                grandaprPrincipal=Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet;
	                grandaprpc=Double.parseDouble(df1.format(tempFinalPensionOB))+ aprpc;
   System.out.println("grandaprAaiNet"+grandaprAaiNet);
   System.out.println("grandaprPrincipal"+grandaprPrincipal);
   System.out.println("grandaprpc"+grandaprpc);
   	adjCrtnService.saveprvadjcrtntotals(personalInfo.getPensionNo(),dispYear,form2Status,
							 0,  0,  0,
							   grandaprpc,  0,  0,
							 0,  grandaprAaiNet,  0, Double.parseDouble(finalEmpNetOBIntrst.toString()),
							 grandaprPrincipal,  0, Double.parseDouble(finalAaiNetOBIntrst.toString()), pensionInterest,remaininInt_Saved_Value ,remainAaiInt_Saved_Value);
							 
   ArrayList prevGrandTotalsList = new ArrayList();
					 double emolumentsTot_prev =0.00,cpfTot_prev =0.00,PenscontriTot_Prev = 0.00,PfTot_prev =0.00,empSubTot_prev=0.00,AAiContriTot_Prev=0.00,  adjEmpSubIntrst_prev=0.00,  adjAAiContriIntrst_Prev=0.00,  adjPesionContriIntrst_Prev=0.00;
					 double emolumentsTot_diff =0.00,cpfTot_diff =0.00,PenscontriTot_diff = 0.00,PfTot_diff =0.00,empSubTot_diff=0.00,AAiContriTot_diff=0.00, adjEmpSubIntrst_diff=0.00, adjAAiContriIntrst_diff=0.00, adjPensionContriIntrst_diff=0.00;
					 double grandEmoluments = 0.0,grandCPF=0.0,grandCPFInterest=0.0,grandPension=0.0,grandPensionInterest=0.0,grandPFContribution=0.00,grandPFContributionInterest=0.00,
					   					grandEmpSub =0.00 ,grandEmpSubInterest=0.00 ,grandAAIContri=0.00,grandAAIContriInterest=0.00,adjFSEmpSubIntrst_prev=0.00,adjFSContriIntrst_Prev=0.00;
					 String transid="";
					   double PensionIntrst=0.00,empSubIntrst=0.00,aaiContriIntrst=0.00;
					 int result =0; 
					    if(request.getAttribute("prevGrandTotalsList")!=null){
					   prevGrandTotalsList = (ArrayList) request.getAttribute("prevGrandTotalsList");
					    
					    if(prevGrandTotalsList.size()!=0){
					   transid = prevGrandTotalsList.get(0).toString();  
					   PenscontriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(3).toString()) ;					 
					   empSubTot_prev = Double.parseDouble(prevGrandTotalsList.get(5).toString()) ;
					   AAiContriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(6).toString()) ;		    
					   adjEmpSubIntrst_prev = Double.parseDouble(prevGrandTotalsList.get(7).toString()) ;
					   adjAAiContriIntrst_Prev = Double.parseDouble(prevGrandTotalsList.get(8).toString()) ;		    
					   adjPesionContriIntrst_Prev= Double.parseDouble(prevGrandTotalsList.get(9).toString()) ;
					 		
					 	//For Final Settlemnt cases on 27-Apr-2012 for  case pfid 17986	   
						adjFSEmpSubIntrst_prev= Double.parseDouble(prevGrandTotalsList.get(10).toString()) ;	
						adjFSContriIntrst_Prev= Double.parseDouble(prevGrandTotalsList.get(11).toString()) ;	
						
					 empSubTot_diff=  grandaprAaiNet  - empSubTot_prev;
					 AAiContriTot_diff=  grandaprPrincipal - AAiContriTot_Prev;					   
					 PenscontriTot_diff = grandaprpc -PenscontriTot_Prev ; 
					 System.out.println("PenscontriTot_diff"+PenscontriTot_diff);
					 System.out.println("PenscontriTot_Prev"+PenscontriTot_Prev);
					 System.out.println("tempFinalPensionOB"+grandaprpc);
					 
					 System.out.println("AAiContriTot_diff"+AAiContriTot_diff);
					 System.out.println("finalAaiNetOB.doubleValue()"+finalAaiNetOB.doubleValue());
					 System.out.println("AAiContriTot_Prev"+AAiContriTot_Prev);
					 //Add By Radha On 31-Jan-2013 as per Sehgal Pfid: 8765
					 
					 if(ArrearInFSPeriod.equals("true")){
					 adjEmpSubIntrst_diff= finalEmpNetOBIntrst.doubleValue() - adjEmpSubIntrst_prev +(remaininInt - totalArrearEmpNet - adjFSEmpSubIntrst_prev);
					 adjAAiContriIntrst_diff = finalAaiNetOBIntrst.doubleValue() - adjAAiContriIntrst_Prev + ( remainAaiInt - totalArrearAAINet - adjFSContriIntrst_Prev); 
					 
					 }else{
					 adjEmpSubIntrst_diff= finalEmpNetOBIntrst.doubleValue() - adjEmpSubIntrst_prev +(remaininInt - adjFSEmpSubIntrst_prev);
					 adjAAiContriIntrst_diff = finalAaiNetOBIntrst.doubleValue() - adjAAiContriIntrst_Prev + ( remainAaiInt - adjFSContriIntrst_Prev); 
					 
					 }
					 
					 
				 System.out.println("=adjAAiContriIntrst_diff="+adjAAiContriIntrst_diff);
				 System.out.println("**********finalAaiNetOBIntrst.doubleValue()="+finalAaiNetOBIntrst.doubleValue()+"adjAAiContriIntrst_Prev="+adjAAiContriIntrst_Prev+"==adjFSEmpSubIntrst_prev="+adjFSContriIntrst_Prev);
				
					 adjPensionContriIntrst_diff=pensionInterest-adjPesionContriIntrst_Prev;
					
				 double   pc_Intrst_2009_2010=0.00,pc_Intrst_2010_2011=0.00,pc_Intrst_2011_2012=0.00,pc_Intrst_2012_2013=0.00,pc_Intrst_2013_2014=0.00,pc_Intrst_2014_2015=0.00,pc_Intrst_2015_2016=0.00,pc_Intrst_2016_2017,pc_Intrst_2017_2018,pc_Intrst_2018_2019,pc_Intrst_2019_2020;
				 double   empSub_Intrst_2009_2010=0.00,empSub_Intrst_2010_2011=0.00,empSub_Intrst_2011_2012=0.00,empSub_Intrst_2012_2013=0.00,empSub_Intrst_2013_2014=0.00,empSub_Intrst_2014_2015=0.00,empSub_Intrst_2015_2016=0.00,empSub_Intrst_2016_2017=0.00,empSub_Intrst_2017_2018=0.00,empSub_Intrst_2018_2019=0.00,empSub_Intrst_2019_2020=0.00;
				 double   aaiContri_Intrst_2009_2010=0.00,aaiContri_Intrst_2010_2011=0.00,aaiContri_Intrst_2011_2012=0.00,aaiContri_Intrst_2012_2013=0.00,aaiContri_Intrst_2013_2014=0.00,aaiContri_Intrst_2014_2015=0.00,aaiContri_Intrst_2015_2016=0.00,aaiContri_Intrst_2016_2017=0.00,aaiContri_Intrst_2017_2018=0.00,aaiContri_Intrst_2018_2019=0.00,aaiContri_Intrst_2019_2020=0.00;
				
					
				if(adjOBYear.equals("2008-2009")){	  
				pc_Intrst_2009_2010 = Math.round(PenscontriTot_diff * ( 8.5 / 100));		
				pc_Intrst_2010_2011 =  Math.round( (pc_Intrst_2009_2010  + PenscontriTot_diff )* 9.5/100) ; 
				pc_Intrst_2011_2012 = 	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 + PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2012_2013 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+ PenscontriTot_diff )* 9.5/100) ; 	
				pc_Intrst_2013_2014 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+ PenscontriTot_diff )* 9.5/100) ; 
				pc_Intrst_2014_2015 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+PenscontriTot_diff )* 9.25/100) ;	
				pc_Intrst_2015_2016 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+PenscontriTot_diff )* 9.15/100) ;
				pc_Intrst_2016_2017 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+PenscontriTot_diff )* 9/100) ;
				pc_Intrst_2017_2018 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+PenscontriTot_diff )* 9.15/100) ;
				pc_Intrst_2018_2019 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018+PenscontriTot_diff )* 9.0/100) ;
				pc_Intrst_2019_2020 =	Math.round( (pc_Intrst_2009_2010 + pc_Intrst_2010_2011 +pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018+pc_Intrst_2018_2019+PenscontriTot_diff )* 8.75/100) ;
				
				PensionIntrst  = pc_Intrst_2009_2010 + pc_Intrst_2010_2011 + pc_Intrst_2011_2012 + pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018+pc_Intrst_2018_2019+pc_Intrst_2019_2020;
				
				empSub_Intrst_2009_2010 = Math.round(empSubTot_diff * ( 8.5 / 100));		
				empSub_Intrst_2010_2011 =  Math.round( (empSub_Intrst_2009_2010  + empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2011_2012 = 	Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSubTot_diff )* 9.5/100) ;
				empSub_Intrst_2012_2013 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 + empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2013_2014 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013 + empSubTot_diff )* 9.5/100) ; 	
				empSub_Intrst_2014_2015 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 + empSubTot_diff )* 9.25/100) ;
				empSub_Intrst_2015_2016 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;	
				empSub_Intrst_2016_2017 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;	
				empSub_Intrst_2017_2018 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSub_Intrst_2015_2016+ empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;	
				empSub_Intrst_2018_2019 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSub_Intrst_2015_2016+ empSub_Intrst_2016_2017+empSub_Intrst_2017_2018+ empSubTot_diff )* 9.0/100) ;	
				empSub_Intrst_2019_2020 = Math.round( (empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012+ empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSub_Intrst_2015_2016+ empSub_Intrst_2016_2017+empSub_Intrst_2017_2018+empSub_Intrst_2018_2019+ empSubTot_diff )* 8.75/100) ;	
				
				
				
				
				empSubIntrst  = empSub_Intrst_2009_2010 + empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 + empSub_Intrst_2012_2013+ empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018+empSub_Intrst_2018_2019+empSub_Intrst_2019_2020;
				
				
				aaiContri_Intrst_2009_2010 = Math.round(AAiContriTot_diff * ( 8.5 / 100));		
				aaiContri_Intrst_2010_2011 =  Math.round( (aaiContri_Intrst_2009_2010  + AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2011_2012 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2012_2013 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2013_2014 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2014_2015 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 + AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;		
				aaiContri_Intrst_2016_2017 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;		
				aaiContri_Intrst_2017_2018 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ aaiContri_Intrst_2015_2016+ aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;		
				aaiContri_Intrst_2018_2019 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ aaiContri_Intrst_2015_2016+ aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018+ AAiContriTot_diff )* 9.0/100) ;		
				aaiContri_Intrst_2019_2020 = Math.round( (aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012+ aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ aaiContri_Intrst_2015_2016+ aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018+aaiContri_Intrst_2018_2019+ AAiContriTot_diff )* 8.75/100) ;		
				
				
				
				
				aaiContriIntrst  = aaiContri_Intrst_2009_2010 + aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 + aaiContri_Intrst_2012_2013+ aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018+aaiContri_Intrst_2018_2019+aaiContri_Intrst_2019_2020; 
				
				 
				 
				 
				 System.out.print("aaiContri_Intrst_2009_2010"+aaiContri_Intrst_2009_2010+"aaiContri_Intrst_2010_2011"+aaiContri_Intrst_2010_2011+"aaiContri_Intrst_2011_2012"+aaiContri_Intrst_2011_2012
				 +"aaiContri_Intrst_2012_2013"+aaiContri_Intrst_2012_2013+"aaiContri_Intrst_2013_2014"+aaiContri_Intrst_2013_2014+"aaiContri_Intrst_2014_2015"+aaiContri_Intrst_2014_2015+"aaiContri_Intrst_2015_2016"+aaiContri_Intrst_2015_2016+"aaiContri_Intrst_2016_2017"+aaiContri_Intrst_2016_2017+"aaiContri_Intrst_2017_2018"+aaiContri_Intrst_2017_2018);
				}else if(adjOBYear.equals("2009-2010")){	
				 
				pc_Intrst_2010_2011 =  Math.round(PenscontriTot_diff *  ( 9.5 / 100)) ;
				pc_Intrst_2011_2012 =  Math.round(( pc_Intrst_2010_2011+ PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2012_2013 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+ PenscontriTot_diff )* 9.5/100) ;	
				pc_Intrst_2013_2014 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+pc_Intrst_2012_2013+ PenscontriTot_diff )* 9.5/100) ;	
				pc_Intrst_2014_2015 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+ PenscontriTot_diff )* 9.25/100) ;	
				pc_Intrst_2015_2016 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+ PenscontriTot_diff )* 9.25/100) ;					
				pc_Intrst_2016_2017 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+ PenscontriTot_diff )* 9/100) ;					
				pc_Intrst_2017_2018 =   Math.round(( pc_Intrst_2010_2011+pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+ PenscontriTot_diff )* 9.15/100) ;					
				
				
				
				PensionIntrst = pc_Intrst_2010_2011 + pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				empSub_Intrst_2010_2011 = Math.round(empSubTot_diff *  (9.5 / 100));
				empSub_Intrst_2011_2012 = Math.round((empSub_Intrst_2010_2011 + empSubTot_diff )* 9.5/100) ;
				empSub_Intrst_2012_2013 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 + empSubTot_diff )* 9.5/100) ;
				empSub_Intrst_2013_2014 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013 + empSubTot_diff )* 9.5/100) ;	
				empSub_Intrst_2014_2015 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014+ empSubTot_diff )* 9.25/100) ;	
				empSub_Intrst_2015_2016 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;						
				empSub_Intrst_2016_2017 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;						
				empSub_Intrst_2017_2018 = Math.round((empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;						
				
				
				empSubIntrst =   empSub_Intrst_2010_2011 + empSub_Intrst_2011_2012 + empSub_Intrst_2012_2013+ empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;
				 System.out.print("empSub_Intrst_2010_2011:"+empSub_Intrst_2010_2011+"empSub_Intrst_2011_2012:"+empSub_Intrst_2011_2012+"empSub_Intrst_2012_2013:"+empSub_Intrst_2012_2013
				 +"empSub_Intrst_2013_2014:"+empSub_Intrst_2013_2014+"empSub_Intrst_2014_2015:"+empSub_Intrst_2014_2015+"empSub_Intrst_2015_2016:"+empSub_Intrst_2015_2016+"empSub_Intrst_2016_2017:"+empSub_Intrst_2016_2017+"empSub_Intrst_2017_2018:"+empSub_Intrst_2017_2018);
				
				aaiContri_Intrst_2010_2011 =  Math.round(AAiContriTot_diff *  ( 9.5 / 100));
				aaiContri_Intrst_2011_2012 =  Math.round((aaiContri_Intrst_2010_2011 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2012_2013 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2013_2014 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013 + AAiContriTot_diff )* 9.5/100) ;
				aaiContri_Intrst_2014_2015 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 + AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;
				aaiContri_Intrst_2016_2017 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 = Math.round((aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;
				
				
				
				
				aaiContriIntrst =  aaiContri_Intrst_2010_2011 + aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018 ; 
				
				
				
				
				
				System.out.println("tttttttttttttt::::::::::"+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018);
				}else if(adjOBYear.equals("2010-2011")){
				pc_Intrst_2011_2012 =  Math.round(PenscontriTot_diff *  ( 9.5 / 100)) ;
				pc_Intrst_2012_2013 =  Math.round(( pc_Intrst_2011_2012+ PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2013_2014 =  Math.round(( pc_Intrst_2011_2012+pc_Intrst_2012_2013+ PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2014_2015 =  Math.round(( pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+ PenscontriTot_diff )* 9.25/100) ;
				pc_Intrst_2015_2016 =  Math.round(( pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+ PenscontriTot_diff )* 9.15/100) ;								
				pc_Intrst_2016_2017 =  Math.round(( pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+ PenscontriTot_diff )* 9/100) ;								
				pc_Intrst_2017_2018 =  Math.round(( pc_Intrst_2011_2012+pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+ PenscontriTot_diff )* 9.15/100) ;								
					
				
				PensionIntrst = pc_Intrst_2011_2012 + pc_Intrst_2012_2013+ pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				empSub_Intrst_2011_2012 = Math.round(empSubTot_diff *  (9.5 / 100));
				empSub_Intrst_2012_2013 = Math.round((empSub_Intrst_2011_2012 + empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2013_2014 = Math.round((empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013 + empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2014_2015 = Math.round((empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 + empSubTot_diff )* 9.25/100) ;
				empSub_Intrst_2015_2016 = Math.round((empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;								
				empSub_Intrst_2016_2017 = Math.round((empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;								
				empSub_Intrst_2017_2018 = Math.round((empSub_Intrst_2011_2012 +empSub_Intrst_2012_2013+empSub_Intrst_2013_2014 +empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;								
				
				
				
				empSubIntrst =   empSub_Intrst_2011_2012 + empSub_Intrst_2012_2013+ empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;
				
				aaiContri_Intrst_2011_2012 =  Math.round(AAiContriTot_diff *  ( 9.5 / 100));
				aaiContri_Intrst_2012_2013 =  Math.round((aaiContri_Intrst_2011_2012 + AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2013_2014 =  Math.round((aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013 + AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2014_2015 =  Math.round((aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 + AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 =  Math.round((aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;
				aaiContri_Intrst_2016_2017 =  Math.round((aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2011_2012 +aaiContri_Intrst_2012_2013+aaiContri_Intrst_2013_2014 +aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;
				
				
				
				aaiContriIntrst =  aaiContri_Intrst_2011_2012 + aaiContri_Intrst_2012_2013+ aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016 +aaiContri_Intrst_2016_2017 +aaiContri_Intrst_2017_2018  ; 	
				  
			
				
				}else if(adjOBYear.equals("2011-2012")){	
				  
				pc_Intrst_2012_2013 =  Math.round((  PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2013_2014 =  Math.round(( pc_Intrst_2012_2013+ PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2014_2015 =  Math.round(( pc_Intrst_2012_2013+pc_Intrst_2013_2014+ PenscontriTot_diff )* 9.25/100) ;	
				pc_Intrst_2015_2016 =  Math.round(( pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+ PenscontriTot_diff )* 9.15/100) ;
				pc_Intrst_2016_2017 =  Math.round(( pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+ PenscontriTot_diff )* 9/100) ;							
				pc_Intrst_2017_2018 =  Math.round(( pc_Intrst_2012_2013+pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+ PenscontriTot_diff )* 9.15/100) ;							
											
				PensionIntrst =  pc_Intrst_2012_2013+ pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				empSub_Intrst_2012_2013 = Math.round(( empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2013_2014 = Math.round((empSub_Intrst_2012_2013 + empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2014_2015 = Math.round((empSub_Intrst_2012_2013 +empSub_Intrst_2013_2014+ empSubTot_diff )* 9.25/100) ;	
				empSub_Intrst_2015_2016 = Math.round((empSub_Intrst_2012_2013 +empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;							
				empSub_Intrst_2016_2017 = Math.round((empSub_Intrst_2012_2013 +empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;							
				empSub_Intrst_2017_2018 = Math.round((empSub_Intrst_2012_2013 +empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;							
				
				
				empSubIntrst =   empSub_Intrst_2012_2013+ empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;
				
				aaiContri_Intrst_2012_2013 =  Math.round(( AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2013_2014 =  Math.round((aaiContri_Intrst_2012_2013 + AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2014_2015 =  Math.round((aaiContri_Intrst_2012_2013 +aaiContri_Intrst_2013_2014+ AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 =  Math.round((aaiContri_Intrst_2012_2013 +aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;
				aaiContri_Intrst_2016_2017 =  Math.round((aaiContri_Intrst_2012_2013 +aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2012_2013 +aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;
				
				
				aaiContriIntrst =   aaiContri_Intrst_2012_2013+ aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018; 	
				
				}else if(adjOBYear.equals("2012-2013")){	
				  
				pc_Intrst_2013_2014 =  Math.round((  PenscontriTot_diff )* 9.5/100) ;
				pc_Intrst_2014_2015 =  Math.round(( pc_Intrst_2013_2014+ PenscontriTot_diff )* 9.25/100) ;	
				pc_Intrst_2015_2016 =  Math.round(( pc_Intrst_2013_2014+pc_Intrst_2014_2015+ PenscontriTot_diff )* 9.15/100) ;							
				pc_Intrst_2016_2017 =  Math.round(( pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+ PenscontriTot_diff )* 9/100) ;							
				pc_Intrst_2017_2018 =  Math.round(( pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+ PenscontriTot_diff )* 9.15/100) ;							
				
				
				PensionIntrst =  pc_Intrst_2013_2014+pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				
				empSub_Intrst_2013_2014 = Math.round((empSubTot_diff )* 9.5/100) ; 
				empSub_Intrst_2014_2015 = Math.round((empSub_Intrst_2013_2014+ empSubTot_diff )* 9.25/100) ;	
				empSub_Intrst_2015_2016 = Math.round((empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;							
				empSub_Intrst_2016_2017 = Math.round((empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;							
				empSub_Intrst_2017_2018 = Math.round((empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;							
				
				
				
				empSubIntrst = empSub_Intrst_2013_2014+empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;
				
				
				aaiContri_Intrst_2013_2014 =  Math.round((AAiContriTot_diff )* 9.5/100) ; 
				aaiContri_Intrst_2014_2015 =  Math.round((aaiContri_Intrst_2013_2014+ AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 =  Math.round((aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;
			    aaiContri_Intrst_2016_2017 =  Math.round((aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;
				
				
				aaiContriIntrst =   aaiContri_Intrst_2013_2014+aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018; 	
				}else if(adjOBYear.equals("2013-2014")){	

				pc_Intrst_2014_2015 =  Math.round(( PenscontriTot_diff )* 9.25/100) ;	
				pc_Intrst_2015_2016 =  Math.round((pc_Intrst_2014_2015+ PenscontriTot_diff )* 9.15/100) ;
				pc_Intrst_2016_2017 =  Math.round((pc_Intrst_2014_2015+pc_Intrst_2015_2016+ PenscontriTot_diff )* 9/100) ;
				pc_Intrst_2017_2018 =  Math.round((pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+ PenscontriTot_diff )* 9.15/100) ;
				
											
				PensionIntrst =  pc_Intrst_2014_2015+pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				empSub_Intrst_2014_2015 = Math.round((empSubTot_diff )* 9.25/100) ;	
				empSub_Intrst_2015_2016 = Math.round((empSub_Intrst_2014_2015+ empSubTot_diff )* 9.15/100) ;	
				empSub_Intrst_2016_2017 = Math.round((empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+ empSubTot_diff )* 9/100) ;	
				empSub_Intrst_2017_2018 = Math.round((empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+ empSubTot_diff )* 9.15/100) ;	
				
										
				empSubIntrst = empSub_Intrst_2014_2015+empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;
				
				aaiContri_Intrst_2014_2015 =  Math.round((AAiContriTot_diff )* 9.25/100) ;
				aaiContri_Intrst_2015_2016 =  Math.round((aaiContri_Intrst_2014_2015+ AAiContriTot_diff )* 9.15/100) ;
				aaiContri_Intrst_2016_2017 =  Math.round((aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+ AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+ AAiContriTot_diff )* 9.15/100) ;
				
				
				aaiContriIntrst =   aaiContri_Intrst_2014_2015+aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018; 	
				}else if(adjOBYear.equals("2014-2015")){	
	
				pc_Intrst_2015_2016 =  Math.round((PenscontriTot_diff )* 9.15/100) ;
				pc_Intrst_2016_2017 =  Math.round((pc_Intrst_2015_2016+PenscontriTot_diff )* 9/100) ;
				pc_Intrst_2017_2018 =  Math.round((pc_Intrst_2015_2016+pc_Intrst_2016_2017+PenscontriTot_diff )* 9.15/100) ;							
				PensionIntrst =  pc_Intrst_2015_2016+pc_Intrst_2016_2017+pc_Intrst_2017_2018;

				empSub_Intrst_2015_2016 = Math.round((empSubTot_diff )* 9.15/100) ;							
				empSub_Intrst_2016_2017 =  Math.round((empSub_Intrst_2015_2016+empSubTot_diff )* 9/100) ;
				empSub_Intrst_2017_2018 =  Math.round((empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSubTot_diff )* 9.15/100) ;							
				empSubIntrst = empSub_Intrst_2015_2016+empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;

				aaiContri_Intrst_2015_2016 =  Math.round((AAiContriTot_diff )* 9.15/100) ;
				aaiContri_Intrst_2016_2017 =  Math.round((aaiContri_Intrst_2015_2016+AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+AAiContriTot_diff )* 9.15/100) ;							
			
				
				aaiContriIntrst =   aaiContri_Intrst_2015_2016+aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018; 	
				}
				else if(adjOBYear.equals("2015-2016")){
				
				
				pc_Intrst_2016_2017 =  Math.round((PenscontriTot_diff )* 9/100) ;
				pc_Intrst_2017_2018 =  Math.round((pc_Intrst_2016_2017+PenscontriTot_diff )* 9.15/100) ;
				PensionIntrst = pc_Intrst_2016_2017+pc_Intrst_2017_2018;
				
				empSub_Intrst_2016_2017 = Math.round((empSubTot_diff )* 9/100) ;							
				empSub_Intrst_2017_2018 =  Math.round((empSub_Intrst_2016_2017+empSubTot_diff )* 9.15/100) ;
				empSubIntrst = empSub_Intrst_2016_2017+empSub_Intrst_2017_2018;

				aaiContri_Intrst_2016_2017 =  Math.round((AAiContriTot_diff )* 9/100) ;
				aaiContri_Intrst_2017_2018 =  Math.round((aaiContri_Intrst_2016_2017+AAiContriTot_diff )* 9.15/100) ;
				
				
				aaiContriIntrst =   aaiContri_Intrst_2016_2017+aaiContri_Intrst_2017_2018; 	
				}
				else if(adjOBYear.equals("2016-2017")){
			
				pc_Intrst_2017_2018 =  Math.round((PenscontriTot_diff )* 9.15/100) ;
				PensionIntrst =   Math.round(pc_Intrst_2017_2018+PenscontriTot_diff) * ( 9/ 100) ;
					
				
				empSub_Intrst_2017_2018 =  Math.round((empSubTot_diff )* 9.15/100) ;
				empSubIntrst =   Math.round(empSub_Intrst_2017_2018+empSubTot_diff) * ( 9/ 100) ;
				
				
				aaiContri_Intrst_2017_2018 =  Math.round((AAiContriTot_diff )* 9.15/100) ;
				aaiContriIntrst =   Math.round(aaiContri_Intrst_2017_2018+AAiContriTot_diff) * ( 9/ 100) ;
				
					
				}if(adjOBYear.equals("2017-2018")){
			
			
				PensionIntrst =   Math.round(PenscontriTot_diff * ( 9.15 / 100)) ;
					
				empSubIntrst =    Math.round(empSubTot_diff * ( 9.15 / 100)) ;
				aaiContriIntrst =  Math.round(AAiContriTot_diff * ( 9.15 / 100)) ;
				
				}
				//form2 data
				
				double  resultEmpSTot=0.0,resultEmpIntrst=0.0,resultAAiContriTot=0.0,resultAAiContriIntrst=0.0, resultPensionTot=0.0,resultPensionIntrst =0.0;
				aftrFinstlmntEmpNetIntrst= Math.round((empSubTot_diff*rateOfInterest/100/12)*monthsCntAfterFinstlmnt);
 				//System.out.println("monthsCntAfterFinstlmnt====="+monthsCntAfterFinstlmnt+"===aftrFinstlmntAAINetTot====="+aftrFinstlmntAAINetTot);
				aftrFinstlmntAAINetIntrst=Math.round((AAiContriTot_diff*rateOfInterest/100/12)*monthsCntAfterFinstlmnt); 
				aftrFinstlmntPCNetIntrst=Math.round((PenscontriTot_diff*rateOfInterest/100/12)*monthsCntAfterFinstlmnt);
				 
				 System.out.println(" **********adjPensionContriIntrst_diff="+adjPensionContriIntrst_diff);
				
					resultEmpSTot  = empSubTot_diff - adjEmpSubIntrst_diff; 
					resultPensionTot = 	 PenscontriTot_diff-adjPensionContriIntrst_diff ;								 	
					resultAAiContriTot=	 AAiContriTot_diff -adjAAiContriIntrst_diff;
					
					/*if(dataFinalSettlementList.size()!=0){
						adjPensionContriIntrst_diff =0.0;
						adjAAiContriIntrst_diff=0.0;
				}*/
				System.out.println(" **********resultAAiContriTot="+resultAAiContriTot+"AAiContriTot_diff="+AAiContriTot_diff+"adjAAiContriIntrst_diff="+adjAAiContriIntrst_diff);
				System.out.println("**********aaiContriIntrst="+aaiContriIntrst+"adjAAiContriIntrst_diff="+adjAAiContriIntrst_diff+"aftrFinstlmntAAINetIntrst="+aftrFinstlmntAAINetIntrst);
					resultEmpIntrst =  empSubIntrst + adjEmpSubIntrst_diff+aftrFinstlmntEmpNetIntrst;
					resultAAiContriIntrst =	 aaiContriIntrst + adjAAiContriIntrst_diff+aftrFinstlmntAAINetIntrst  ;					
					resultPensionIntrst =  PensionIntrst+ adjPensionContriIntrst_diff+aftrFinstlmntPCNetIntrst ;
					

					System.out.println("resultPensionIntrst"+resultPensionIntrst);
					System.out.println("PensionIntrst"+PensionIntrst);
					System.out.println("adjPensionContriIntrst_diff"+adjPensionContriIntrst_diff+"==aftrFinstlmntPCNetIntrst==="+aftrFinstlmntPCNetIntrst);
					
					System.out.println("resultAAiContriIntrst"+resultAAiContriIntrst);
					System.out.println("aaiContriIntrst"+aaiContriIntrst);
					System.out.println("adjAAiContriIntrst_diff"+adjAAiContriIntrst_diff+"==aftrFinstlmntPCNetIntrst+==="+aftrFinstlmntPCNetIntrst);					
					
					System.out.println("==df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet====="+df1.format(grandaprAaiNet));
					System.out.println("==df1.format(Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet====="+df1.format(grandaprPrincipal));
					System.out.println("df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc"+df1.format(grandaprpc));
					
					
					System.out.println("==grandCPFInterest====="+new Long(tempFinalPensionOB).doubleValue());
					System.out.println("==grandPFContributionInterest====="+finalEmpNetOB.doubleValue());
					System.out.println("finalEmpNetOBIntrst.doubleValue()"+finalAaiNetOB.doubleValue());
					
				
					tempFinalPensionOB1=grandaprAaiNet+new Long(tempFinalPensionOB).doubleValue();
					finalEmpNetOB1= grandaprPrincipal+finalEmpNetOB.doubleValue();
					finalAaiNetOB1= grandaprpc+finalAaiNetOB.doubleValue();
					
					if(grandTotDiffShowFlag.equals("true")){
					  result =  adjCrtnDAO.savepcadjcrtnCurrenttotals(personalInfo.getPensionNo(),dispYear,transid,form2Status,grandEmoluments,grandCPF,grandCPFInterest,
					   										grandaprpc,grandPensionInterest,grandPFContribution,grandPFContributionInterest,
					   										grandaprAaiNet ,grandEmpSubInterest ,finalEmpNetOBIntrst.doubleValue(),
					   										grandaprPrincipal,grandAAIContriInterest,finalAaiNetOBIntrst.doubleValue(),pensionInterest,
					   										emolumentsTot_diff,cpfTot_diff ,Math.round(PenscontriTot_diff),PfTot_diff,
					   										Math.round(empSubTot_diff),Math.round(adjEmpSubIntrst_diff),Math.round(AAiContriTot_diff),Math.round(adjAAiContriIntrst_diff),
					   										Math.round(adjPensionContriIntrst_diff),Math.round(resultPensionIntrst-adjPensionContriIntrst_diff),Math.round(resultEmpIntrst-adjEmpSubIntrst_diff),
					   										Math.round(resultAAiContriIntrst-adjAAiContriIntrst_diff));
					  }

					 %>
					<tr>
						<td class="HighlightData" align="left" colspan="9">Previous Grand Totals</td>						 
						 
						<td class="HighlightData"   align="right"><%=df.format(empSubTot_prev)%></td>
						<td class="HighlightData"  colspan="2" align="right">&nbsp;</td>
						<td class="HighlightData"  align="right"><%=df.format(AAiContriTot_Prev)%></td>
						<td class="HighlightData"   align="right"><%=df.format(PenscontriTot_Prev)%></td>
						 <td class="HighlightData"  colspan="4" align="right">&nbsp;</td>
						 
						

					</tr>
					<tr>
						<td class="HighlightData" align="left" colspan="9">Difference </td>
						 
						 
						<td class="HighlightData"   align="right"><%=df.format(empSubTot_diff)%></td>
						<td class="HighlightData"  colspan="2" align="right">&nbsp;</td>										 
						<td class="HighlightData"  align="right"><%=df.format(AAiContriTot_diff)%></td>
						<td class="HighlightData"   align="right"><%=df.format(PenscontriTot_diff)%></td>	
						 <td class="HighlightData"  colspan="4" align="right">&nbsp;</td>
						 

					</tr>
					<tr>
						<td colspan="17">
							<table width="100%" border="1" cellspacing="0" cellpadding="0">
							<tr>
						<td class="HighlightData" align="left" nowrap="nowrap" colspan="13">Form 2 Adjustments  </td>
						<td class="HighlightData"   align="right"  colspan="12">Subscription amount</td>
						<td class="HighlightData"   align="right">Interest thereon</td> 
						
						<td class="HighlightData"   align="right">Contribution amount</td>
						<td class="HighlightData"   align="right">Interest thereon</td>
						<td class="HighlightData"   align="right">Adjustment in Pension Contri</td>
						<!--<td class="HighlightData"   align="right">Interest thereon</td> 
						--></tr>
						 <tr>
						<td class="HighlightData" align="left" nowrap="nowrap" colspan="13"> &nbsp;</td>
						<td class="HighlightData"   align="right" colspan="12"><%=Math.round(resultEmpSTot)%></td>
						<td class="HighlightData"   align="right"><%=Math.round(resultEmpIntrst)%></td> 						
						<td class="HighlightData"   align="right"><%=Math.round(resultAAiContriTot)%></td>
						<td class="HighlightData"   align="right"><%=Math.round(resultAAiContriIntrst)%></td>
						 <td class="HighlightData"   align="right"><%=Math.round(resultPensionTot)%></td><!--
						 <td class="HighlightData"   align="right"><%=Math.round(resultPensionIntrst)%></td> 
						 
						--></tr>
						 </table>	 
						 	</td>
					</tr>
					 
					<%}} %>
    </table>
    </td>
    
    
  </tr>
  
  
  
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
 
 
   <tr>
			<td>
				<table border="0" style="border-color: gray;" cellpadding="2"
						cellspacing="0" width="100%" align="center">		
					<tr>
					  <td>&nbsp;</td>
					<td colspan="230">&nbsp;</td>
					 	<td align="center" > <input type="button" class="btn" style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #000000;"
						value="Back" class="btn" onclick="gotoback('<%=personalInfo.getPensionNo()%>');"/> 
						 <input type="button"     id="update"
						value="Update " class="btn"  style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #000000;" onclick="finalresult('<%=personalInfo.getPensionNo()%>','<%=personalInfo.getEmployeeName()%>','<%=finalEmpNetOB1%>','<%=finalEmpNetOBIntrst%>','<%=finalAaiNetOB1%>','<%=finalAaiNetOBIntrst%>','<%=tempFinalPensionOB1%>','<%=pensionInterest%>','<%=adjOBYear%>','<%=countList%>');"/></td>
				<td class="NumData"><a  title="Click the link to view Adjustments log" target="_self" href="javascript:void(0)" onclick="javascript:loadadjlog('<%=personalInfo.getPensionNo()%>','','2011-2012','adjcorrections')">Adjustments Log</a></td>
			 </tr>
			</table>
			
			
		</tr> 
    	 
   
  
 
  <!--   <tr>
    <td colspan="7"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="53%"><table width="100%" border="1" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan="5" class="reportsublabel"><div align="center">DETAILS OF PART FINAL WITHDRAWAL (PFW)</div></td>
            </tr>
          <tr>
            <td class="label">Sl No</td>
            <td class="label">Purpose</td>
            <td class="label">Date</td>
            <td class="label">Amount</td>
         
          </tr>
          <%
          		PTWBean ptwInfo=new PTWBean();
          		int count=0;
	          for(int k=0;k<dataPTWList.size();k++){
	          count++;
	          ptwInfo=(PTWBean)dataPTWList.get(k);
          %>
          <tr>
            <td class="label"><%=count%></td>
            <td class="label"><%=ptwInfo.getPtwPurpose()%></td>
            <td><%=ptwInfo.getPtwDate()%></td>
            <td><%=ptwInfo.getPtwAmount()%></td>
           
          </tr>
         <%}%>
        </table></td>
        <td width="47%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td class="label">NOTE:-</td>
             </tr>
           <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">1. CREDITS INCLUDES MARCH SALARY PAID IN APRIL TO FEBRUARY SALARY PAID IN MARCH.ADVANCES/PFW SHOWN IN THE MONTH IT IS PAID.</td>
            </tr>
          <tr>
            <td>&nbsp;</td>
            </tr>
          <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">2. IN CASE OF ANY DISCREPANCY IN THE BALANCES SHOWN ABOVE THE MATTER MAY BE BROUGHT TO THE NOTICE OF THE CPF CELL WITHIN 15 DAYS OF ISSUE OF THE STATEMENT, OTHERWISE THE BALANCES WOULD BE PRESUMED TO HAVE BEEN CONFIRMED.</td>
            </tr>
          <tr>
            <td>&nbsp;</td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>  
  <%if (!arrearDate.equals("NA")){%>
    <tr>
    <td colspan="7">
    	<table width="53%" border="1" cellspacing="0" cellpadding="0">
     		<tr>
            		
            		<td class="label">Arrear Date</td>
            		<td class="label">Arrear Amount</td>
            		<td class="label">Arrear Contribution</td>
         
          </tr>
         		<tr>
            		
            		<td class="Data"><%=arrearDate%></td>
            		<td class="Data"><%=arrearAmount%></td>
            		<td class="Data"><%=arrearContri%></td>
         
          </tr>
        </table></td>

  </tr>
  <%}%>
      <tr>
  
    <td nowrap="nowrap" colspan="6" class="label">Date</td>
    
  </tr>
    <tr>
    <td nowrap="nowrap" colspan="6" class="label">M=MARRIED, U=UNMARRIED, W=WIDOW/WIDOWER</td>
    
  </tr>
    <tr>
  
    <td nowrap="nowrap" colspan="6" class="label">RAJIV GANDHI BHAWAN, SAFDARJUNG AIRPORT, NEW DELHI-110003. PHONE 011-24632950, FAX 011-24610540.</td>
    
  </tr>         
  
  

  <%if(size-1!=cardList){%>
						<br style='page-break-after:always;'>
	<%}%>	-->         					
	    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</td>


</tr>

				
   <%	}}}%>

	
					
</table>
</form>
  </body>
</html>
