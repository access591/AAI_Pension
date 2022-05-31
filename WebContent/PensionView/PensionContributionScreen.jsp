<%@ page import="java.util.*,java.lang.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="aims.common.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ArrayList a = new ArrayList();
	String color = "yellow";
	String noofMonths="",arrearFlag="";
	String recoverieTable="";
	System.out.println("this is starting...");
	 String reIntrestcalcDate=request.getAttribute("reIntrestcalcDate").toString();
	 System.out.println("reSettlementdate");
	 System.out.println("reSettlementdate"+request.getAttribute("resettledate").toString());
	 String reSettlementdate=request.getAttribute("resettledate").toString();
%>
<%!ArrayList blockList = new ArrayList();
	String breakYear = "";%>
<%
	ArrayList PensionContributionList = new ArrayList();
	ArrayList pensionList = new ArrayList();
	CommonUtil commonUtil = new CommonUtil();
	String fullWthrOptionDesc = "", genderDescr = "", mStatusDec = "";
	String employeeNm = "", pensionNo = "", doj = "", dob = "", cpfacno = "", employeeNO = "", designation = "", fhName = "", gender = "", fileName = "";
	String reportType = "", whetherOption = "", dateOfEntitle = "", empSerialNo = "", mStatus = "", region1 = "", cpfaccno1 = "";
	
	if (request.getAttribute("reportType") != null) {
		reportType = (String) request.getAttribute("reportType");
		if (reportType.equals("Excel Sheet")
				|| reportType.equals("ExcelSheet")) {
			fileName = "Pension_Contribution_report.xls";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=" + fileName);
		}
	}
	PensionContributionList = (ArrayList) request
			.getAttribute("penContrList");
	recoverieTable=request.getParameter("recoverieTable");
	
	String cntFlag = "";
	int size = 0;
	size = PensionContributionList.size();
	for (int i = 0; i < PensionContributionList.size(); i++) {
		PensionContBean contr = (PensionContBean) PensionContributionList
				.get(i);
		employeeNm = contr.getEmployeeNM();
		pensionNo = contr.getPensionNo();
		empSerialNo = contr.getEmpSerialNo();
		doj = contr.getEmpDOJ();
		dob = contr.getEmpDOB();
		cpfacno = StringUtility.replaces(
				contr.getCpfacno().toCharArray(), ",=".toCharArray(),
				",").toString();

		if (cpfacno.indexOf(",=") != -1) {
			cpfacno = cpfacno.substring(1, cpfacno.indexOf(",="));
		} else if (cpfacno.indexOf(",") != -1) {
			cpfacno = cpfacno.substring(cpfacno.indexOf(",") + 1,
					cpfacno.length());
		}
		whetherOption = contr.getWhetherOption();
		if (whetherOption.toUpperCase().trim().equals("A")) {
			fullWthrOptionDesc = "Full Pay";
		} else if (whetherOption.toUpperCase().trim().equals("B")
				|| whetherOption.toUpperCase().trim().equals("NO")) {
			fullWthrOptionDesc = "Ceiling Pay";
		} else {
			fullWthrOptionDesc = whetherOption;
		}
		employeeNO = contr.getEmployeeNO();
		designation = contr.getDesignation();
		fhName = contr.getFhName();
		gender = contr.getGender();
		region1 = contr.getEmpRegion();
		cpfaccno1 = contr.getEmpCpfaccno();
	    
		String discipline = contr.getDepartment();
		String finalsettlmentdate="";
		finalsettlmentdate=contr.getFinalSettlementDate();
		//out.println(finalsettlmentdate);
		String interestdate=contr.getInterestCalUpto();
		String dateofseperationDt=contr.getDateofSeperationDt();
	
		finalsettlmentdate.replaceAll("-","/");
		if (gender.trim().toLowerCase().equals("m")) {
			genderDescr = "Male";
		} else if (gender.trim().toLowerCase().equals("f")) {
			genderDescr = "Female";
		} else {
			genderDescr = gender;
		}
		mStatus = contr.getMaritalStatus().trim();

		if (mStatus.toLowerCase().equals("m")
				|| (mStatus.toLowerCase().trim().equals("yes"))) {
			mStatusDec = "Married";
		} else if (mStatus.toLowerCase().equals("u")
				|| (mStatus.toLowerCase().trim().equals("no"))) {
			mStatusDec = "Un-married";
		} else if (mStatus.toLowerCase().equals("w")) {
			mStatusDec = "Widow";
		} else {
			mStatusDec = mStatus;
		}
		dateOfEntitle = contr.getDateOfEntitle();
		cntFlag = contr.getCountFlag();
		pensionList = contr.getEmpPensionList();
		blockList = contr.getBlockList();
		String userId = session.getAttribute("userid").toString();
		
	
		
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
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css"
	type="text/css">
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>

<script type="text/javascript">
function checkdataFreeze(monthyear,region,airportcode){

 var url="<%=basePath%>reportservlet?method=checkDataFreeze&region="+region+"&airportcode="+airportcode+"&monthyear="+monthyear;
     createXMLHttpRequest();
     xmlHttp.open("GET", url, true);
	 xmlHttp.onreadystatechange = checkdataFreezefromRemitanceinfo;
	 xmlHttp.send(null);
}
function checkdataFreezefromRemitanceinfo()
{  var result;
	//alert(xmlHttp.responseText);
	if(xmlHttp.readyState ==4)
	{ 	if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		
		  if(stype.length==0){
		 	var obj1 = document.getElementById("count");
		 	obj1.options.length=0; 
		 	  var count = getNodeValue(stype[0],'count');
			  }else{
		   var count = getNodeValue(stype[0],'count');
		   if(parseInt(count)>0){
		    alert("REMITTANCE DATA already frozen for the Edited Month"); 
		    document.forms[0].status.value="true";
		    return false;
		   }else{
			   document.forms[0].status.value="false";
		   }
		 	  		   
		  }
		}
	}
}

var xmlHttp;
  function editEmoluments(monthyear,cpfaccno,region,airportcode,pensionNo,emolumentxtextboxno,vpftextboxno,principletextboxno,interesttextboxno,advancetextboxno,contributionboxno,advanceboxno,loanboxno,aailoanboxno,editid,pfsettled,userId,freezflag,from7narration1,pcheldamt1,noofmonths1,arrearFlag1,duputationflag,epftextboxno){
	//alert("Permission Denied to Edit the Report Details");	
       var dt1   = monthyear.substring(0,2);
	   var mon1  = monthyear.substring(3,6);
	   var year1=monthyear.substring(7,monthyear.length);
	   var duputationflag=duputationflag;
	   var pensionoption='<%=whetherOption%>';
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
	 var recoverieTable='<%=recoverieTable%>';
	 if(recoverieTable!="true"){
	 alert("Permission Denied to Edit the Report Details");
		 return false;
	/*if((userId1!="NIMESH")&&(userId1!="SEHGAL")){
		//alert("This screen is temporarily blocked to edit the data");
		//return false;
	}*/	
	 var mydate=new Date();    
	   var curyear=mydate.getYear();
	    var nexyear=mydate.getYear()+1;
	if((parseInt(month)>=3 && parseInt(month)<=11 && (parseInt(year1)>=1995 && parseInt(year1)<=curyear))|| (parseInt(month) >=0 && parseInt(month)<=2 && (parseInt(year1)>=1996 && parseInt(year1)<=nexyear))){
		/* alert(" Edit is not permitted for the Financial Year  between 01-Apr-1995 and 31-Mar-2011");*/
		alert("You dont have the Previliges to Edit the Record");
		 return false;
		}
	/*if(((userId1!="CAFIN")&&(userId1!="SAPFIN")&&(userId1!="NIMESH")&&(userId1!="SEHGAL")&&(userId1!="WADHVA")&&(userId1!="ERFIN")&&(userId1!="SRFIN")&&(userId1!="NRFIN")&&(userId1!="NERFIN")&&(userId1!="WRFIN")&&(userId1!="MALKEET")&&(userId1!="CHQFIN") && (userId1!="IGIFIN") && (userId1!="CAPFIN") && (userId1!="CSIFIN")&& (userId1!="NSCBFIN")&& (userId1!="TVMFIN") && parseInt(month)>=3 && parseInt(month)<=11 && (parseInt(year1)==1995||parseInt(year1)==2008 ||parseInt(year1)==2009||parseInt(year1)==2010 ) )|| ((userId1!="NIMESH")&&((userId1!="SAPFIN")&& userId1!="SEHGAL")&&(userId1!="WADHVA")&&(userId1!="ERFIN")&&(userId1!="SRFIN")&&(userId1!="NRFIN")&&(userId1!="NERFIN")&&(userId1!="WRFIN")&&(userId1!="MALKEET")&&(userId1!="CHQFIN") && (userId1!="IGIFIN") && (userId1!="CAPFIN") && (userId1!="CSIFIN")&& (userId1!="NSCBFIN")&& (userId1!="TVMFIN")&&(userId1!="CAFIN") && parseInt(month) >=0 && parseInt(month)<=2 && (parseInt(year1)==1996||parseInt(year1)==2009||(parseInt(year1)==2010)||(parseInt(year1)==2011)))){
	 alert(" Edit is not permitted for the Financial Year Apr 2008 to Mar-2011");
	 return false;
	}*/
	if(pfsettled=="Y" && userId1!="navayuga" && userId1!="WADHVA"&& userId1!="SEHGAL"){
		 alert(" Edit is not permitted for this PF ID as Final settlement process is completed");
			return false;
		}
	  }
	
	

	
	
	checkdataFreeze(monthyear,region,airportcode);
	if(document.forms[0].status.value=="true"){
    return false;
	}	
    var  cpfaccno1=cpfaccno;
	var region2= region;
	  if(cpfaccno1=="-NA-"){
		  cpfaccno1='<%=cpfaccno1%>';
	  }

	
	  if(region2=="-NA-"){
		 if(userId1=="SRFIN"){
			  region2="South Region";
		  }else if(userId1=="NERFIN"){
			  region2="North-East Region";
		  }else if(userId1=="WRFIN"){
			  region2="West Region";
		  }else if(userId1=="SAPFIN"){
			  region2="RAUSAP";
				airportcode="RAUSAP";
		  }else if(userId1=="NRFIN"){
 					 region2="North Region";
		  }else if(userId1=="ERFIN"){
 					 region2="East Region";
		  }else if(userId1=="CHQFIN"){
 					 region2="CHQNAD";
		  }else if(userId1=="IGIFIN"){
 					 region2="CHQIAD";
				airportcode="IGI IAD";
		  }else if(userId1=="CAFIN"){
 					 region2="CHQIAD";
					airportcode="CHENNAI IAD";
		  }else if(userId1=="CAPFIN"){
 					 region2="CHQIAD";
					airportcode="CAP IAD";
		  }else if(userId1=="CSIFIN"){
 					 region2="CHQIAD";
					airportcode="CSIA IAD";
		  }else if(userId1=="NSCBFIN"){
 					 region2="CHQIAD";
					airportcode="KOLKATA IAD";
		  }else if(userId1=="TVMFIN"){
					region2="CHQIAD";
					airportcode="TRIVANDRUM IAD";
		  }else{
			 region2='<%=region1%>';
	        }
     }
	  
	 
	 var emoluments= document.getElementById(emolumentxtextboxno).value;
	 var epf=document.getElementById(epftextboxno).value;
	 var vpf= document.getElementById(vpftextboxno).value;
	 var principle=document.getElementById(principletextboxno).value;
	 var interest=document.getElementById(interesttextboxno).value;
	 var contri=document.getElementById(contributionboxno).value;
	 var advance=document.getElementById(advanceboxno).value;
	 var loan=document.getElementById(loanboxno).value;
	 var aailoan=document.getElementById(aailoanboxno).value;
	 var from7narration=document.getElementById(from7narration1).value;
	 var pcheldamt=document.getElementById(pcheldamt1).value;
	 var noofmonths=document.getElementById(noofmonths1).value;
	 var arrearFlag=document.getElementById(arrearFlag1).value;
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=false;
	 document.getElementsByName(emolumentxtextboxno)[0].focus();
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].readOnly=false;
	 document.getElementsByName(interesttextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(interesttextboxno)[0].readOnly=false;
	 document.getElementsByName(advancetextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(advancetextboxno)[0].readOnly=false;
	 if((parseInt(year1)>=2008 &&parseInt(month)>=3 && parseInt(month)<=11)||(parseInt(year1)>=2009 &&(parseInt(month)>=0))){
	 document.getElementsByName(contributionboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(contributionboxno)[0].readOnly=false;
	 }
	 document.getElementsByName(advanceboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(advanceboxno)[0].readOnly=false;
	 document.getElementsByName(loanboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(loanboxno)[0].readOnly=false;
	 document.getElementsByName(aailoanboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(aailoanboxno)[0].readOnly=false;
	 document.getElementsByName(from7narration1)[0].style.background='#FFFFCC';
	 document.getElementsByName(from7narration1)[0].readOnly=false;
	 document.getElementsByName(pcheldamt1)[0].style.background='#FFFFCC';
	 document.getElementsByName(pcheldamt1)[0].readOnly=false;
	 document.getElementsByName(noofmonths1)[0].style.background='#FFFFCC';
	 document.getElementsByName(noofmonths1)[0].disabled=false;
	 document.getElementsByName(arrearFlag1)[0].style.background='#FFFFCC';
	 document.getElementsByName(arrearFlag1)[0].disabled=false;
	
	 var buttonName=document.getElementsByName(editid)[0].value;
	 document.getElementsByName(editid)[0].value="S";
	 createXMLHttpRequest();	
	  var answer="";
		
	 if(buttonName=="S"){
	 if(arrearFlag=="N" && monthyear=="01-SEP-2009"){
		   answer =confirm('Are you sure, is it Arrear Transacion Data');
		    if(answer && monthyear=="01-SEP-2009"){
		   arrearFlag="Y";
		    document.getElementsByName(arrearFlag1)[0].value="Y";
		   }else{
		    arrearFlag="N";
		    document.getElementsByName(arrearFlag1)[0].value="N";
		   }
		}
		
		if(arrearFlag=="Y" && document.getElementById(contributionboxno).value=="0"){
		alert("Please Enter The Pension Contribution Amount");
		document.getElementById(contributionboxno).focus();
		return false;
		}
		 process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';
		var url="<%=basePath%>reportservlet?method=editTransactionData&pensionNo="+pensionNo+"&cpfaccno="+cpfaccno1+"&emoluments="+emoluments+"&monthyear="+monthyear+"&region="+region2+"&airportcode="+airportcode+"&editid="+editid+"&vpf="+vpf+"&principle="+principle+"&interest="+interest+"&contri="+contri+"&advance="+advance+"&loan="+loan+"&aailoan="+aailoan+"&from7narration="+from7narration+"&pcheldamt="+pcheldamt+"&noofmonths="+noofmonths+"&arrearflag="+arrearFlag+"&duputationflag="+duputationflag+"&pensionoption="+pensionoption+"&recoverieTable="+recoverieTable+"&epf="+epf;
      // alert(url);
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
	 var vpfboxno="vpf"+rownum;
	 var principleboxno="principle"+rownum;
	 var interestboxno="interest"+rownum;
	 var contriboxno="contr"+rownum;
	 var loantxtboxno="loan"+rownum;
	 var aailoanboxno="aailoan"+rownum;
	 var pcheldamtboxno="pcheldamt"+rownum;
	 var noofmonthsboxno="noofmonths"+rownum;
	 var pcheldamtboxno="pcheldamt"+rownum;
	 var form7narrationboxno="form7narration"+rownum;
	 var arrearflagboxno="arrearflag"+rownum;
	 var  advanceboxno="advance"+rownum;
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='none';
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
	 document.getElementsByName(loantxtboxno)[0].readOnly=true;
	 document.getElementsByName(loantxtboxno)[0].style.background='none';
	 document.getElementsByName(aailoanboxno)[0].readOnly=true;
	 document.getElementsByName(aailoanboxno)[0].style.background='none';
	 document.getElementsByName(noofmonthsboxno)[0].disabled=true;
	 document.getElementsByName(noofmonthsboxno)[0].style.background='none';
	 document.getElementsByName(pcheldamtboxno)[0].readOnly=true;
	 document.getElementsByName(pcheldamtboxno)[0].style.background='none';
	 document.getElementsByName(form7narrationboxno)[0].readOnly=true;
	 document.getElementsByName(form7narrationboxno)[0].style.background='none';
	 document.getElementsByName(arrearflagboxno)[0].disabled=true;
	 document.getElementsByName(arrearflagboxno)[0].style.background='none';
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
   function backTransaction(){
   document.forms[0].action="<%=basePath%>search1?method=verifiedpfidlist";
	 	document.forms[0].method="post";
		document.forms[0].submit();   
   }
   function deleteTransaction(){
	   var userId='<%=userId%>';
	 
	   if((userId!="NIMESH")&&(userId!="SEHGAL")){
		   alert("You dont have the Previliges to Delete the Record");
		   return false;
	   }
   var answer =confirm('Are you sure, do you want  delete this record');
   var pfid=document.forms[0].pfid.value;
    var page="PensionContributionScreen";
   var mappingFlag="true";
	if(answer){
  	document.forms[0].action="<%=basePath%>reportservlet?method=deleteTransactionData&page="+page;
   	document.forms[0].method="post";
  	alert("You dont have the Previliges to Delete the Record");
  	//document.forms[0].submit();
      }
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
		var recoverieTable='<%=recoverieTable%>';
        var interestcalcUpto=document.forms[0].interestCalc.value;
        
        var reinterestcalc=document.forms[0].reinterestCalc.value;
      //  alert("reinterestcalc"+reinterestcalc);
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&frm_pfids="+pfidStrip+"&transferStatus="+transferStatus+"&finalrecoverytableFlag="+recoverieTable+"&interestcalcUpto="+interestcalcUpto+"&reinterestcalc="+reinterestcalc;
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
  function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
  }
  function frmload(){
	  if(process){
	  process.style.display="none";
	  document.getElementById('process').style.display='none';
	  }
	 document.getElementById('fade').style.display='none';
	}

  function getContribution(monthyear,pfid,emolumentsbox,dob,wetheroption,contributionbox)
  {	createXMLHttpRequest();	
    var emoluments= document.getElementById(emolumentsbox).value;
   	var url ="<%=basePath%>reportservlet?method=getContribution&monthyear="+monthyear+"&emoluments="+emoluments+"&dob="+dob+"&wetheroption="+wetheroption+"&contributionbox="+contributionbox;
  	xmlHttp.open("post", url, true);
  	xmlHttp.onreadystatechange = getContributionValue;
  	xmlHttp.send(null);
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
  		    var contributionboxno = getNodeValue(stype[0],'contributionbox');
  		    alert(contributionboxno);
  		 //   document.getElementsByName(contributionboxno)[0].value=pensionContribution;	
  		  		   
  		  }
  		}
  	}
  }
  function numsDotOnly()
	{
    if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46) ||(event.keyCode>=44 && event.keyCode<=47))
        {
           event.keyCode=event.keyCode;
        }
        else
        {
			 event.keyCode=0;
        }
  }
  function editFinalsettledate(pensionNo,editdate){
	  document.forms[0].finalsettle.readOnly=false;
	 var finalsettledate=document.forms[0].finalsettle.value;
  	 var buttonName=document.forms[0].editdate.value;
  document.forms[0].editdate.value="S";
	 document.forms[0].finalsettle.style.background='#FFFFCC';
		 createXMLHttpRequest();
		 if(buttonName=="S"){ 
			 var date1=document.forms[0].finalsettle;
			 var val1=convert_date(date1);		   	   
	   		   if(val1==false)
	   		    {
	   		     return false;
	   		   }
	   		  	if(finalsettledate==""){
				alert("please enter the final settle date");
				return false;
		} 
			 var date = document.forms[0].finalsettle.value;
			 var finaldate=getDate(date);
			 date1=new Date(2008,03,01); 
			 var date2=new Date(2010,02,31);

             //**********
			 var finaldateAlreadyExist='<%=dateofseperationDt%>';
			finaldateAlreadyExist=getDate(finaldateAlreadyExist);
			if(finaldateAlreadyExist!="" && finaldateAlreadyExist >= date1 && finaldateAlreadyExist <= date2){
		 	 alert("FinalSettlement date Already Exist as Per the frozen date,You can't edit this Date i.e PFCard already frozen");
			  return false;
				} 
				//***************
			 
			if(finaldate >= date1 && finaldate <= date2){
	   	   alert("FinalSettlement date can't be edited between 01-Apr-2008 and 31-Mar-2010. i.e PFCARD 2008-09 and 2009-10 already freezed");
		  return false;
		}    		
	 	 var url="<%=basePath%>reportservlet?method=editFinalDate&pensionNo="+pensionNo+"&finalsettlementDate="+finalsettledate;
	     xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = updateButtonStatus;
		xmlHttp.send(null);
		 }
  }
  function editInterestDate(pensionNo,editdate){  
   var userId1='<%=userId%>';
   if(userId1=="CSIFIN"){
   alert("You don't have previlages To change FinalSettlementDate.Please Contact Epis Support");
   return false;
   }
	  document.forms[0].interestCalc.readOnly=false;
	  var interestCalcdate=document.forms[0].interestCalc.value;
  	  var buttonName=document.forms[0].editinterestdate.value;
	  document.forms[0].editinterestdate.value="S";
	  document.forms[0].interestCalc.style.background='#FFFFCC';
	  var recoverieTable='<%=recoverieTable%>';
		 createXMLHttpRequest();
		 if(buttonName=="S"){ 
			 var date1=document.forms[0].interestCalc;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(interestCalcdate==""){
				alert("please enter the InterestCalculaion upto date");
				return false;
			} 
			 var date = document.forms[0].interestCalc.value;
			 var finaldate=getDate(date);
			 var redate = document.forms[0].reinterestCalc.value;
			 var resettledate=getDate(redate);
			 if(recoverieTable!="true"){
			 date1=new Date(1995,03,01); 
			 var date2=new Date(2011,02,31);
			 } else {
			 date1=new Date(1995,03,01);
			 var date2=new Date(2008,02,31);  
			 }		 
             //**********
             var settlementDate='<%=finalsettlmentdate%>';
			 var finaldateAlreadyExist='<%=finalsettlmentdate%>';
			 finaldateAlreadyExist=getDate(finaldateAlreadyExist);
			 var finalsetdate='<%=contr.getFinalSettlementDate()%>';	
			 //alert("finalsetdate"+finalsetdate);
			  var resetdate='<%=reSettlementdate%>';
			  if(finaldate>resettledate && recoverieTable!="true"){
			   alert("ReSettlement Date  Should be gretthan final settlemnt date");
			       }
			if(resetdate!="" && recoverieTable!="true"){
			 	    alert("ReSettlement Date  "+resetdate+" Already Exist. You can't edit this Date ");
			        return false;
			 	 }	  
			 if(finalsetdate!="" && recoverieTable!="true"){
			 	    alert("FinalSettlement Date  "+settlementDate+" Already Exist. You can't edit this Date ");
			        return false;
			 	 }	
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
         if (finaldate>currentdt)
        {
        if(recoverieTable!="true"){
        alert("Please ensure that Final Settlement Date "+interestCalcdate+" is less than or equal to current month");
         return false;
         }else{
         alert("Please ensure that IntrestCalc Date "+interestCalcdate+" is less than or equal to current month");
         return false;
         }
        }
			 	 	 		
			  if(finaldateAlreadyExist!="" && finaldateAlreadyExist >= date1 && finaldateAlreadyExist <= date2 && recoverieTable!="true"){
		 	  alert("FinalSettlement Date  "+settlementDate+" Already Exist as Per the frozen date,You can't edit this Date i.e PFCard already frozen");
			  return false;
					} 
				//***************
			 if(finaldate >= date1 && finaldate <= date2 &&recoverieTable=="true"){
	   	   		alert("intrestcalc date date can't be edited between 01-Apr-1995 and 31-Mar-2008.");
		       return false;
				} 
			 if(finaldate >= date1 && finaldate <= date2 &&recoverieTable!="true"){
	   	   		alert("FinalSettlement date can't be edited between 01-Apr-1995 and 31-Mar-2011. i.e PC Report 1995-2008 and PFCARD 2008-11 already frozen");
		       return false;
				} 			  		
	 	 var url="<%=basePath%>reportservlet?method=editInterestCalcuptoDate&pensionNo="+pensionNo+"&interestCalcdate="+interestCalcdate+"&recoverieTable="+recoverieTable;
            xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = updateButtonStatus;
			xmlHttp.send(null);
		 }
  }
    function editReInterestupto(pensionNo,editdate){
	  document.forms[0].reinterestCalc.readOnly=false;
	  var reinterestCalcdate=document.forms[0].reinterestCalc.value;
  	  var buttonName=document.forms[0].editreinterestdate.value;
	  document.forms[0].editreinterestdate.value="S";
	  document.forms[0].reinterestCalc.style.background='#FFFFCC';
	  createXMLHttpRequest();
	   var recoverieTable='<%=recoverieTable%>';
		 if(buttonName=="S"){ 
			 var date1=document.forms[0].reinterestCalc;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(reinterestCalcdate==""){
				alert("please enter the REInterestCalculaion upto date");
				return false;
			} 
		var resetdate='<%=reSettlementdate%>';				
			 if(resetdate!="" && recoverieTable!="true"){
			 	    alert("Resettlement Date  "+resetdate+" Already Exist. You can't edit this Date ");
			        return false;
			 	 }	
			 	 var fidate = document.forms[0].interestCalc.value;
			 var finalsettdate=getDate(fidate);
			 var redate = document.forms[0].reinterestCalc.value;
			 var resettledate=getDate(redate);
			 if(resettledate<finalsettdate && recoverieTable!="true"){
			   alert("ReSettlement Date  Should be gretthan final settlemnt date");
			       }
			       if(resettledate<finalsettdate && recoverieTable=="true"){
			   alert("Reintrestcalc Date  Should be gretthan final intrestcalc upto date");
			       }
			 	  var date = document.forms[0].reinterestCalc.value;
			 var finaldate=getDate(date);
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
           if(recoverieTable!="true"){
			 date1=new Date(1995,03,01); 
			 var date2=new Date(2011,02,31);
			 } else {
			 date1=new Date(1995,03,01);
			 var date2=new Date(2008,02,31);  
			 }
			 if(finaldate >= date1 && finaldate <= date2 &&recoverieTable!="true"){
	   	   		alert("ReSettlement date can't be edited between 01-Apr-1995 and 31-Mar-2011. i.e PC Report 1995-2008 and PFCARD 2008-11 already frozen");
		       return false;
				} 
				if(finaldate >= date1 && finaldate <= date2 &&recoverieTable=="true"){
	   	   		alert("Reintrestcalc date date can't be edited between 01-Apr-1995 and 31-Mar-2008.");
		       return false;
				} 
       if (finaldate>currentdt)
        {
       alert("Please ensure that Recalc Date "+reinterestCalcdate+" is less than or equal to current month");
       return false;
        }	 	
          var url="<%=basePath%>reportservlet?method=editReInterestcalcupto&pensionNo="+pensionNo+"&reinterestCalcdate="+reinterestCalcdate+"&recoverieTable="+recoverieTable;
             //alert(url);
	        xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = updateButtonStatus1;
			xmlHttp.send(null);
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
	function updateButtonStatus1()
	{
	  document.forms[0].editreinterestdate.value="E";
	  document.forms[0].reinterestCalc.style.background='none';
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
  function interestCal()
  {
  var userId1='<%=userId%>';
   if(userId1=="CSIFIN"){
   alert("You don't have previlages To change FinalSettlementDate.Please Contact Epis Support");
   return false;
   }
    var buttonname=document.forms[0].interestCalc.value;
		if(buttonname=="E"){
			alert("Please Click the Edit button");
			return false;
		}
		else 
		{
        document.forms[0].interestCalc.style.background='#FFFFCC';	
        document.forms[0].interestCalc.value=document.forms[0].interestCalc.value,show_calendar('forms[0].interestCalc');
		}
 }
 function reinterestCal()
  {
  
    var buttonname=document.forms[0].reinterestCalc.value;
		if(buttonname=="E"){
			alert("Please Click the Edit button");
			return false;
		}
		else 
		{
        document.forms[0].reinterestCalc.style.background='#FFFFCC';	
        document.forms[0].reinterestCalc.value=document.forms[0].reinterestCalc.value,show_calendar('forms[0].reinterestCalc');
		}
 }
 function resettlement()
  {
    var buttonname=document.forms[0].resettlementdate.value;
		if(buttonname=="E"){
			alert("Please Click the Edit button");
			return false;
		}
		else 
		{
        document.forms[0].resettlementdate.style.background='#FFFFCC';	
        document.forms[0].resettlementdate.value=document.forms[0].resettlementdate.value,show_calendar('forms[0].resettlementdate');
		}
 }  
    </script>
</head>
<body class="BodyBackground" onload="frmload();">
<form action="method">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tr>
			<td align="center" colspan="5">
			<table border=0 cellpadding=3 cellspacing=0 width="100%"
				align="center" valign="middle">

				<tr>
               <%
               String heading="STATEMENT OF PENSION	CONTRIBUTION";
                if(recoverieTable.equals("true")){%>
               
                 <% heading="REVISED STATEMENT OF PENSION	CONTRIBUTION";} %>
					<td>&nbsp;&nbsp;</td>
					<td align="center" class="ScreenHeading"><%=heading%></td>
					<td class="Data" width="15%"><a href="#"
						onClick="validateForm()"><img
						src="./PensionView/images/printIcon.gif" border="0" alt="Print"></a></td>
					<td align="right">
					<td colspan="5"><a href="#"
						onclick="backTransaction();"><img
						src="<%=basePath%>PensionView/images/viewBack.gif" border="0"
						alt="Back"></a></td>
				</tr>
			</table>
			</td>
		</tr>

		<%!String getBlockYear(String year) {
		String bYear = "";

		for (int by = 0; by < blockList.size(); by++) {
		
			bYear = (String) blockList.get(by);
			String[] bDate = bYear.split(",");

			if (year.equals(bDate[1])) {
				breakYear = bDate[1];
				breakYear = bYear;
				break;
			} else {
				breakYear = "03-96";
			}
		}
		//	System.out.println("breakYear"+breakYear);
		//blockList.remove(breakYear);
		return breakYear;
	}%>
		<tr>
			<td>
			<table border="1" style="border-color: gray" cellpadding="2"
				cellspacing="0" width="100%" align="center">
				<tr>

					<td class="reportsublabel">PF ID</td>
					<td class="reportdata"><%=pensionNo%></td>
					<input type="hidden" name="pfid" value="<%=empSerialNo%>">
					<td class="reportsublabel">NAME</td>
					<td class="reportdata"><%=employeeNm%></td>

				</tr>
				<tr>
					<td class="reportsublabel">EMP NO</td>
					<td class="reportdata"><%=employeeNO%></td>
					<td class="reportsublabel">DESIGNATION</td>
					<td class="reportdata"><%=designation%></td>


				</tr>
				<tr>
					<td class="reportsublabel">CPF NO</td>
					<td class="reportdata"><%=cpfacno%></td>
					<td class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
					<td class="reportdata"><%=fhName%></td>


				</tr>
				<tr>
					<td class="reportsublabel">DATE OF BIRTH</td>
					<td class="reportdata"><%=dob%></td>
					<td class="reportsublabel">GENDER</td>
					<td class="reportdata"><%=genderDescr%></td>

				</tr>
				<tr>
					<td class="reportsublabel">DATE OF JOINING</td>
					<td class="reportdata"><%=doj%></td>
					<td class="reportsublabel">DISCIPLINE</td>
					<td class="reportdata"><%=discipline%></td>

				</tr>
				<tr>
					<td class="reportsublabel">DATE OF MEMBERSHIP</td>
					<td class="reportdata"><%=dateOfEntitle%></td>
					<td class="reportsublabel">PENSION OPTION</td>
					<td class="reportdata"><%=fullWthrOptionDesc%></td>

				</tr>

		
	 
            <tr>
                   
<%if(!recoverieTable.equals("true")){ %>
           <td class="reportsublabel">INTEREST CALCULATION UPTO (Final Settlement)</td>
					<td class="reportdata"><%=contr.getFinalSettlementDate()%></td>
<%}else{ %>
     <td class="reportsublabel">INTEREST CALCULATION UPTO </td>
     <td class=""> <input type="text"  size="10"  name="interestCalc"  value='<%=contr.getInterestCalUpto()%>'>
     <a onclick="interestCal();" href="javascript:show_calendar('forms[0].interestCalc');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
                    <input type="button"  name="editinterestdate"  value="E" style = "cursor:hand;" onclick="editInterestDate('<%=pensionNo%>','interestCal')" /></td>
<%} %>
                    
					<!--  <td class="reportsublabel">DATE OF FINALSETTLEMENT </td>
					
					<td class="reportdata">
                    <input type="text"  size="10" readonly="true" name="finalsettle" value='<%=contr.getDateofSeperationDt()%>'>
                    <a onclick="editcal();"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
                    <input type="button"  name="editdate"  value="E" onclick="editFinalsettledate('<%=pensionNo%>','editdate')" /></td>-->
 <%if(!recoverieTable.equals("true")){ %>
           <td class="reportsublabel">RESETTLEMENT DATE</td>
					<td class="reportdata"><%=reSettlementdate%></td>
  <%}else if(recoverieTable.equals("true")){ %>
     <td class="reportsublabel">REINTEREST CALCULATION UPTO</td>
     <td class=""> <input type="text"  size="10"  name="reinterestCalc"  value='<%=reIntrestcalcDate%>'>
     <%if(userId.equals("WADHVA")){ %>
      <a onclick="reinterestCal();"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
     <input type="button"  name="editreinterestdate"  value="E" style = "cursor:hand;" onclick="editReInterestupto('<%=pensionNo%>','reinterestCalc')" /></td>
    <%}%>
<%} %>
                   <!--  <a onclick="reinterestCal();"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
                    <input type="button"  name="editreinterestdate"  value="E" onclick="editReInterestupto('<%=pensionNo%>','reinterestCalc')" /></td>-->
                    
              </tr> 
			</table>
			</td>
		</tr>

		<%
			if (pensionList.size() != 0) {
		%>


		<td colspan="5">
		<div id="process" class="white_content">
<img src="<%=basePath%>PensionView/images/Indicator.gif" border="0" align="right" />
<SPAN class="label">Processing.......</SPAN>
        </div>
		<div id="fade" class="black_overlay"></div>
		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="0" width="100%" align="center">

			<tr>
				<td class="label" width="10%" align="center">Month</td>
				<td class="label" width="10%" align="center">Emolument</td>
				<td class="label" width="8%" align="center">EPF</td>
				<td class="label" width="10%" align="center">Pension
				Contribution<br>
				(1.16%X2)&8.33%</td>
				<td class="label" width="8%" align="center">NET EPF</td>
				<td class="label" width="8%" align="center">Station</td>
				<td class="label" width="8%" align="center">VPF</td>
				<td class="label" width="8%" align="center">PRINCIPLE</td>
				<td class="label" width="8%" align="center">INTEREST</td>
				<td class="label" width="8%" align="center">Advances</td>
				<td class="label" width="5%" align="center">PFW SUB_AMT</td>
				<td class="label" width="5%" align="center">PFW CONT_AMT</td>
				
                <td class="label" width="5%" align="center">NoofMonths/Days</td>
				<td class="label" width="5%" align="center">Arrears Received</td>
				<td class="label" width="5%" align="center">Edit</td>
				<%
					if (cntFlag.equals("true")) {
				%>
				<td class="label" width="8%" align="center">Delete</td>
				<%
					}
				%>
				<td class="label" width="8%" align="center">PCHeldAmt</td>
				<td class="label" width="10%" align="center">Remarks</td>
			</tr>
			<%
				double totalEmoluments = 0.0, pfStaturary = 0.0, totalPension = 0.0, empVpf = 0.0, principle = 0.0, interest = 0.0, pfContribution = 0.0;
						double grandEmoluments = 0.0, grandCPF = 0.0, grandPension = 0.0, grandPFContribution = 0.0;
						double cpfInterest = 0.0, pensionInterest = 0.0, pfContributionInterest = 0.0;
						double grandCPFInterest = 0.0, grandPensionInterest = 0.0, grandPFContributionInterest = 0.0;
						double cumPFStatury = 0.0, cumPension = 0.0, cumPfContribution = 0.0;
						double cpfOpeningBalance = 0.0, penOpeningBalance = 0.0, pfOpeningBalance = 0.0;
						double percentage = 0.0;
						boolean openFlag = false;
						int count = 0;
						int chkMnths = 0;
						boolean flag = false;
						String findMnt = "";
						int countMnts = 0;
						DecimalFormat df = new DecimalFormat("#########0");
						String dispFromYear = "", dispToYear = "", totalYear = "";
						boolean dispYearFlag = false;
						double rateOfInterest = 0;
						String monthInfo = "", getMnthYear = "";

						for (int j = 0; j < pensionList.size(); j++) {
							TempPensionTransBean bean = (TempPensionTransBean) pensionList
									.get(j);
							if (bean != null) {
								String dateMontyYear = bean.getMonthyear();
                                
								if (dispYearFlag == false) {
									if (dispFromYear.equals("")) {
										dispFromYear = commonUtil
												.converDBToAppFormat(dateMontyYear,
														"dd-MMM-yyyy", "yy");
									}

									getMnthYear = commonUtil.converDBToAppFormat(
											dateMontyYear, "dd-MMM-yyyy", "MM-yy");

									String monthInterestInfo = getBlockYear(getMnthYear);
									String[] monthInterestList = monthInterestInfo
											.split(",");
									if (monthInterestList.length == 2) {
										monthInfo = monthInterestList[1];

										rateOfInterest = new Double(
												monthInterestList[0]).doubleValue();
									}

									dispYearFlag = true;

									breakYear = "";
								}

								String monthYear = bean.getMonthyear().substring(
										dateMontyYear.indexOf("-") + 1,
										dateMontyYear.length());
								findMnt = commonUtil.converDBToAppFormat(bean
										.getMonthyear(), "dd-MMM-yyyy", "MM-yy");

								if (findMnt.equals(monthInfo)) {
									flag = true;

									breakYear = "";
								}

								count++;
  
								totalEmoluments = new Double(df
										.format(totalEmoluments
												+ Math.round(Double
														.parseDouble(bean
																.getEmoluments()))))
										.doubleValue();
								pfStaturary = new Double(df.format(pfStaturary
										+ Math.round(Double.parseDouble(bean
												.getCpf())))).doubleValue();
								cumPFStatury = cumPFStatury + pfStaturary;
								empVpf = new Double(df.format(empVpf
										+ Math.round(Double.parseDouble(bean
												.getEmpVPF())))).doubleValue();
								principle = new Double(df.format(principle
										+ Math.round(Double.parseDouble(bean
												.getEmpAdvRec())))).doubleValue();
								interest = new Double(df.format(interest
										+ Math.round(Double.parseDouble(bean
												.getEmpInrstRec())))).doubleValue();
								totalPension = new Double(df.format(totalPension
										+ Math.round(Double.parseDouble(bean
												.getPensionContr()))))
										.doubleValue();
								cumPension = cumPension + totalPension;
								pfContribution = new Double(df
										.format(pfContribution
												+ Math.round(Double
														.parseDouble(bean
																.getAaiPFCont()))))
										.doubleValue();
								cumPfContribution = cumPfContribution
										+ pfContribution;
								
								//System.out.println(bean.getPensionContr()	+ "==========" + bean.getDbPensionCtr());
			%>

			<%
				//  System.out.println(bean.getRecordCount());
								if (bean.getRecordCount().equals("Single")) {
			%>
			<tr>

				<td class="Data" width="6%" align="center"><%=monthYear%></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" size="5" name="emoluments<%=j%>" id="emoluments<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmoluments()))%>'
					onkeypress="numsDotOnly()"></td>
				
                <td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="epf<%=j%>" id="epf<%=j%>"
					size="5"
					value='<%=Math.round(Double.parseDouble(bean
												.getCpf()))%>'</td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="contr<%=j%>" id="contr<%=j%>"
					size="5" value='<%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%>'</td>
				<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getAaiPFCont()))%></td>
				<td class="Data" nowrap="nowrap"><%=bean.getStation()%></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="vpf<%=j%>" id="vpf<%=j%>"
					size="5"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpVPF()))%>' /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="principle<%=j%>" id="principle<%=j%>"
					size="5"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpAdvRec()))%>' /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="interest<%=j%>" id="interest<%=j%>"
					size="5"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpInrstRec()))%>' /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="advance<%=j%>" id="advance<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getAdvAmount()))%>'
					size="5" /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="loan<%=j%>" id="loan<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmployeeLoan()))%>'
					size="5" /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="aailoan<%=j%>" id="aailoan<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getAaiLoan()))%>'
					size="5" /></td>
				
                    <td class="Data" width="6%" align="right"> <select name="noofmonths<%=j%>"  id="noofmonths<%=j%>" disabled="disabled" style="width:70px"tabindex="6">
				<% noofMonths =bean.getNoofMonths().trim();
				//System.out.println("no of Months"+noofMonths);

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
       <td class="Data" width="7%" align="right"> <select name="arrearflag<%=j%>" id="arrearflag<%=j%>" disabled="disabled" style="width:30px" >
				<% arrearFlag =bean.getArrearFlag().trim();
			//	System.out.println("no of Months"+noofMonths);

                %>
												
													<option value="N" <%if(arrearFlag.equals("N")){ out.println("selected");}%>>
														No
													</option>
													<option value="Y" <%if(arrearFlag.equals("Y")){ out.println("selected");}%>>
														Yes
													</option>
 </select>
 </td>
				<td class="Data"><input type="button" name="edit<%=j%>" id="edit<%=j%>"
					value="E" style = "cursor:hand;" 
					onclick="editEmoluments('<%=bean.getMonthyear()%>','<%=bean.getTransCpfaccno()%>','<%=bean.getRegion()%>','<%=bean.getStation()%>','<%=pensionNo%>','emoluments<%=j%>','vpf<%=j%>','principle<%=j%>','interest<%=j%>','advance<%=j%>','contr<%=j%>','advance<%=j%>','loan<%=j%>','aailoan<%=j%>','edit<%=j%>','<%=contr.getPfSettled()%>','<%=userId%>','<%=bean.getDataFreezFlag()%>','form7narration<%=j%>','pcheldamt<%=j%>','noofmonths<%=j%>','arrearFlag<%=j%>','<%=bean.getDeputationFlag()%>','epf<%=j%>')" /></td>
<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="pcheldamt<%=j%>" id="pcheldamt<%=j%>"
					value='<%=bean.getPcHeldAmt()%>'
					size="5" /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true"  name="form7narration<%=j%>" id="form7narration<%=j%>"
					value='<%=bean.getForm7Narration()%>'
					size="5" /></td>
			</tr>
			<%
				} else if (bean.getRecordCount()
										.equals("Duplicate")) {
			%>
			<tr bgcolor="yellow">
				<td class="Data" width="10%" align="center"><font color="red"><%=monthYear%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getEmoluments()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getCpf()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getEmpVPF()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getPensionContr()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getAaiPFCont()))%></font></td>
				<td class="Data" width="10%"><font color="red"><%=bean.getStation()%></font></td>
				<td class="Data" width="10%"><font color="red"> <input
					type="button" name="E"  value="E" style = "cursor:hand;" onclick="editEmoluments() " /></font></td>
				<td class="Data" width="12%"><input type="checkbox"
					name="cpfno"
					value="'<%=bean.getMonthyear()%>','<%=bean.getTransCpfaccno()%>','<%=bean.getRegion()%>','<%=bean.getStation()%>'" />

				</td>
			</tr>

			<%
				}
			%>
			<%
				if (flag == true) {
									dispToYear = commonUtil.converDBToAppFormat(
											dateMontyYear, "dd-MMM-yyyy", "yy");
									if (dispFromYear.equals(dispToYear)) {
										if (dispFromYear.equals("00")) {
											dispFromYear = "99";
										}

										if (dispFromYear.trim().length() < 2) {
											dispFromYear = "0" + dispFromYear;
										}
										dispToYear = Integer.toString(Integer
												.parseInt(dispToYear) + 1);
										if (dispToYear.trim().length() < 2) {
											dispToYear = "0" + dispToYear;
										}
									}
									totalYear = dispFromYear + "-" + dispToYear;

									dispFromYear = "";
			%>
			<tr>
				<td class="HighlightData" align="center">Total <%=totalYear%></td>
				<td class="HighlightData" align="right"><%=df.format(totalEmoluments)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfStaturary)%></td>
				<td class="HighlightData" align="right"><%=df.format(totalPension)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContribution)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empVpf)%></td>
				<td class="HighlightData" align="right"><%=df.format(principle)%></td>
				<td class="HighlightData" align="right"><%=df.format(interest)%></td>

			</tr>
			<tr>
				<%
					cpfInterest = Math.round((cumPFStatury
												* rateOfInterest / 100) / 12)
												+ Math.round(cpfOpeningBalance
														* rateOfInterest / 100);
										pensionInterest = Math.round((cumPension
												* rateOfInterest / 100) / 12)
												+ Math.round(penOpeningBalance
														* rateOfInterest / 100);
										pfContributionInterest = Math
												.round((cumPfContribution
														* rateOfInterest / 100) / 12)
												+ Math.round(pfOpeningBalance
														* rateOfInterest / 100);
				%>

				<td class="HighlightData" align="center">Interest(<%=rateOfInterest%>%)</td>
				<td class="HighlightData">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pensionInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContributionInterest)%></td>
				<td class="HighlightData">---</td>
				<td class="HighlightData" align="right"><%=df.format(empVpf)%></td>
				<td class="HighlightData" align="right"><%=df.format(principle)%></td>
				<td class="HighlightData" align="right"><%=df.format(interest)%></td>
			</tr>
			<tr>
				<%
					flag = false;
										openFlag = true;
										cpfOpeningBalance = Math.round(pfStaturary
												+ cpfInterest
												+ Math.round(cpfOpeningBalance));
										penOpeningBalance = Math.round(totalPension
												+ pensionInterest
												+ Math.round(penOpeningBalance));
										pfOpeningBalance = Math.round(pfContribution
												+ pfContributionInterest
												+ Math.round(pfOpeningBalance));
				%>

				<td class="HighlightData" align="center">CL BAL</td>
				<td class="HighlightData">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(penOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfOpeningBalance)%></td>
				<td class="HighlightData">---</td>
				<td class="HighlightData">---</td>


			</tr>
			<%
				grandEmoluments = grandEmoluments
											+ totalEmoluments;
									grandCPF = grandCPF + pfStaturary;
									grandPension = grandPension + totalPension;
									grandPFContribution = grandPFContribution
											+ pfContribution;

									grandCPFInterest = grandCPFInterest
											+ cpfInterest;
									grandPensionInterest = grandPensionInterest
											+ pensionInterest;
									grandPFContributionInterest = grandPFContributionInterest
											+ pfContributionInterest;
									cumPFStatury = 0.0;
									cumPension = 0.0;
									cumPfContribution = 0.0;
									totalEmoluments = 0;
									pfStaturary = 0;
									totalPension = 0;
									pfContribution = 0;
									cpfInterest = 0;
									pensionInterest = 0;
									pfContributionInterest = 0;
								}
			%>
			<%
				dispYearFlag = false;
							}
						}
			%>
			<tr>
				<td colspan="13">
				<table align="center" width="97%" cellpadding="0" cellspacing="0"
					border="1" bordercolor="gray">

					<tr>
						<td class="HighlightData"></td>
						<td class="HighlightData">Emolument</td>
						<td class="HighlightData">CPF</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">Pension Contribution</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">PF Contribution</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">VPF</td>
						<td class="HighlightData">Principle</td>
						<td class="HighlightData">Interest</td>
					</tr>
					<tr>
						<td class="HighlightData" align="left">Grand Total of <%=count%>
						months</td>
						<td class="HighlightData"></td>
						<td class="HighlightData" align="right"><%=df.format(grandCPF)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandCPFInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPension)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPensionInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPFContribution)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPFContributionInterest)%></td>
						<td class="HighlightData" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="HighlightData" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="HighlightData" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td class="HighlightData" align="center">Grand Total</td>
						<td class="HighlightData" align="right"><%=df.format(grandEmoluments)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandCPF + grandCPFInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPension + grandPensionInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPFContribution
									+ grandPFContributionInterest)%></td>

					</tr>
				</table>
				</td>
			</tr>

  <input type="hidden" name="status" >


		</table>
		<%
			if (size - 1 != i) {
		%> <br style='page-break-after: always;'>
		<%
			}
		%>
		</td>
		</tr>

		<%
			}
		%>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>

		<%
			}
		%>		
		<tr>
			<td align="right"><a href="#"
				onclick="backTransaction();"><img
				src="<%=basePath%>PensionView/images/viewBack.gif" border="0"
				alt="Back"></a></td>
		</tr>
	</table>

	</tr>
</table>
</body>
</html>
 