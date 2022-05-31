<%@ page import="java.util.*,java.lang.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="aims.common.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.dao.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ArrayList a = new ArrayList();
	String color = "yellow";
	String noofMonths="",arrearFlag="";
	String recoverieTable="";
	 String adjOBYear=""; 
	 
	 
	 if(request.getAttribute("dspYear")!=null){
	 adjOBYear = (String)request.getAttribute("dspYear");
	 }
%>
<%!ArrayList blockList = new ArrayList();
	String breakYear = "";%>
<%
	FinancialReportDAO financialReportDAO = new FinancialReportDAO();
	ArrayList PensionContributionList = new ArrayList();
	ArrayList pensionList = new ArrayList();
	CommonUtil commonUtil = new CommonUtil();
	String fullWthrOptionDesc = "", genderDescr = "", mStatusDec = "";
	String employeeNm = "", pensionNo = "", doj = "", dob = "", cpfacno = "", employeeNO = "", designation = "", fhName = "", gender = "", fileName = "";
	String reportType = "", whetherOption = "", dateOfEntitle = "", empSerialNo = "", mStatus = "", region1 = "", cpfaccno1 = "";
	String  cpfTotal= "",PenContriTotal= "",PFTotal="",emolumentsTotal="",empSubTotal = "",	AAIContriTotal = "";
	 
	double totalEmoluments = 0.0, pfStaturary = 0.0, totalPension = 0.0, empVpf = 0.0, principle = 0.0, interest = 0.0, pfContribution = 0.0;
	double grandEmoluments = 0.0, grandCPF = 0.0, grandPension = 0.0, grandPFContribution = 0.0;
	double cpfInterest = 0.0, pensionInterest = 0.0, pfContributionInterest = 0.0,empSubInterest = 0.0, aaiContriInterest = 0.0;
	double grandCPFInterest = 0.0, grandPensionInterest = 0.0, grandPFContributionInterest = 0.0;
	double cumPFStatury = 0.0, cumPension = 0.0, cumPfContribution = 0.0;
	double cpfOpeningBalance = 0.0, penOpeningBalance = 0.0, pfOpeningBalance = 0.0, empSubOpeningBalance = 0.0, aaiContriOpeningBalance =0.0; 
	double percentage = 0.0,advanceAmt =0.00, pfwSubAmt =0.00,pfwContriAmt = 0.00, subTotal=0.00,empSubscri=0.00,aaiContri = 0.00,pf = 0.00,cumempSubscri = 0.00, cumAAiContri=0.00, aaiContriTot =0.00,  empSubscriTot=0.00;
	double  grandEmpSub =0.00,grandEmpSubInterest =0.00,grandAAIContri=0.00,grandAAIContriInterest=0.00;
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
  function editEmoluments(monthyear,cpfaccno,region,airportcode,pensionNo,emolumentxtextboxno,vpftextboxno,principletextboxno,interesttextboxno,advancetextboxno,loanboxno,aailoanboxno,aaicontribox,contributionboxno,editid,userId,freezflag,from7narration1,duputationflag,epftextboxno,adjOBYear){
	// alert("Permission Denied to Edit the PCReport Details");
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
	// if(recoverieTable!="true"){
	/*if((userId1!="NIMESH")&&(userId1!="SEHGAL")){
		//alert("This screen is temporarily blocked to edit the data");
		//return false;
	}*/
	
	/*if((parseInt(month)>=3 && parseInt(month)<=11 && (parseInt(year1)==1995||parseInt(year1)==1996||parseInt(year1)==1997||parseInt(year1)==1998||parseInt(year1)==1999||parseInt(year1)==2000||parseInt(year1)==2001||parseInt(year1)==2002||parseInt(year1)==2003||parseInt(year1)==2004||parseInt(year1)==2005||parseInt(year1)==2006||parseInt(year1)==2007||parseInt(year1)==2008 ||parseInt(year1)==2009 ||parseInt(year1)==2010) )|| (parseInt(month) >=0 && parseInt(month)<=2 && (parseInt(year1)==1996||parseInt(year1)==1997||parseInt(year1)==1998||parseInt(year1)==1999||parseInt(year1)==2000||parseInt(year1)==2001||parseInt(year1)==2002||parseInt(year1)==2003||parseInt(year1)==2004||parseInt(year1)==2005||parseInt(year1)==2006||parseInt(year1)==2007||parseInt(year1)==2008||parseInt(year1)==2009||(parseInt(year1)==2010)||(parseInt(year1)==2011)))){
		 alert(" Edit is not permitted for the Financial Year  between 01-Apr-1995 and 31-Mar-2011");
		 return false;
		}*/
	/*if(((userId1!="CAFIN")&&(userId1!="SAPFIN")&&(userId1!="NIMESH")&&(userId1!="SEHGAL")&&(userId1!="WADHVA")&&(userId1!="ERFIN")&&(userId1!="SRFIN")&&(userId1!="NRFIN")&&(userId1!="NERFIN")&&(userId1!="WRFIN")&&(userId1!="MALKEET")&&(userId1!="CHQFIN") && (userId1!="IGIFIN") && (userId1!="CAPFIN") && (userId1!="CSIFIN")&& (userId1!="NSCBFIN")&& (userId1!="TVMFIN") && parseInt(month)>=3 && parseInt(month)<=11 && (parseInt(year1)==1995||parseInt(year1)==2008 ||parseInt(year1)==2009||parseInt(year1)==2010 ) )|| ((userId1!="NIMESH")&&((userId1!="SAPFIN")&& userId1!="SEHGAL")&&(userId1!="WADHVA")&&(userId1!="ERFIN")&&(userId1!="SRFIN")&&(userId1!="NRFIN")&&(userId1!="NERFIN")&&(userId1!="WRFIN")&&(userId1!="MALKEET")&&(userId1!="CHQFIN") && (userId1!="IGIFIN") && (userId1!="CAPFIN") && (userId1!="CSIFIN")&& (userId1!="NSCBFIN")&& (userId1!="TVMFIN")&&(userId1!="CAFIN") && parseInt(month) >=0 && parseInt(month)<=2 && (parseInt(year1)==1996||parseInt(year1)==2009||(parseInt(year1)==2010)||(parseInt(year1)==2011)))){
	 alert(" Edit is not permitted for the Financial Year Apr 2008 to Mar-2011");
	 return false;
	}*/
	/*if(pfsettled=="Y" && userId1!="navayuga" && userId1!="WADHVA"&& userId1!="SEHGAL"){
		 alert(" Edit is not permitted for this PF ID as Final settlement process is completed");
			return false;
		}
	  }*/
	
	

	
	
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
	 var advance=document.getElementById(advancetextboxno).value;
	 var loan=document.getElementById(loanboxno).value;
	 var aailoan=document.getElementById(aailoanboxno).value;
	  var aaicontri=document.getElementById(aaicontribox).value;
	 var from7narration=document.getElementById(from7narration1).value;
	// var pcheldamt=document.getElementById(pcheldamt1).value;
	 //var noofmonths=document.getElementById(noofmonths1).value;
	// var arrearFlag=document.getElementById(arrearFlag1).value;
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=false;
	 document.getElementsByName(emolumentxtextboxno)[0].focus();
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(epftextboxno)[0].readOnly=false;
	 document.getElementsByName(epftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(vpftextboxno)[0].readOnly=false;
	 document.getElementsByName(vpftextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(principletextboxno)[0].readOnly=false;
	 document.getElementsByName(interesttextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(interesttextboxno)[0].readOnly=false;
	 document.getElementsByName(advancetextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(advancetextboxno)[0].readOnly=false;
	 
	  document.getElementsByName(loanboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(loanboxno)[0].readOnly=false;
	 document.getElementsByName(aailoanboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(aailoanboxno)[0].readOnly=false;
	 document.getElementsByName(aaicontribox)[0].style.background='#FFFFCC';
	 document.getElementsByName(aaicontribox)[0].readOnly=false;
	 document.getElementsByName(contributionboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(contributionboxno)[0].readOnly=false;
	  
	 document.getElementsByName(from7narration1)[0].style.background='#FFFFCC';
	 document.getElementsByName(from7narration1)[0].readOnly=false;
	// document.getElementsByName(pcheldamt1)[0].style.background='#FFFFCC';
	// document.getElementsByName(pcheldamt1)[0].readOnly=false;
	// document.getElementsByName(noofmonths1)[0].style.background='#FFFFCC';
	// document.getElementsByName(noofmonths1)[0].disabled=false;
	// document.getElementsByName(arrearFlag1)[0].style.background='#FFFFCC';
	// document.getElementsByName(arrearFlag1)[0].disabled=false;
	
	 var buttonName=document.getElementsByName(editid)[0].value;
	 document.getElementsByName(editid)[0].value="S";
	 createXMLHttpRequest();	
	  var answer="";
	var reportYear='<%=adjOBYear%>';
	 if(buttonName=="S"){
	 
		 process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';
		var url="<%=basePath%>reportservlet?method=editTransactionDataFor78PsAdjCrtn&pensionNo="+pensionNo+"&cpfaccno="+cpfaccno1+"&emoluments="+emoluments+"&monthyear="+monthyear+"&region="+region2+"&airportcode="+airportcode+"&editid="+editid+"&vpf="+vpf+"&principle="+principle+"&interest="+interest+"&contri="+contri+"&advance="+advance+"&loan="+loan+"&aailoan="+aailoan+"&from7narration="+from7narration+"&duputationflag="+duputationflag+"&pensionoption="+pensionoption+"&recoverieTable="+recoverieTable+"&epf="+epf+"&adjOBYear="+reportYear;
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
	 var epfboxno="epf"+rownum;
	 var vpfboxno="vpf"+rownum;
	 var principleboxno="principle"+rownum;
	 var interestboxno="interest"+rownum;
	 var contriboxno="contr"+rownum;
	 var loantxtboxno="loan"+rownum;
	 var aailoanboxno="aailoan"+rownum;
	 var aaicontriboxno="aaicontri"+rownum;
	// var pcheldamtboxno="pcheldamt"+rownum;
	 //var noofmonthsboxno="noofmonths"+rownum;
	// var pcheldamtboxno="pcheldamt"+rownum;
	 var form7narrationboxno="form7narration"+rownum;
	// var arrearflagboxno="arrearflag"+rownum;
	 var  advanceboxno="advance"+rownum;
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='none';
	 document.getElementsByName(contriboxno)[0].readOnly=true;
	 document.getElementsByName(contriboxno)[0].style.background='none';
	  document.getElementsByName(epfboxno)[0].readOnly=true;
	 document.getElementsByName(epfboxno)[0].style.background='none';
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
	  document.getElementsByName(aaicontriboxno)[0].readOnly=true;
	 document.getElementsByName(aaicontriboxno)[0].style.background='none';
	// document.getElementsByName(noofmonthsboxno)[0].disabled=true;
	// document.getElementsByName(noofmonthsboxno)[0].style.background='none';
//	 document.getElementsByName(pcheldamtboxno)[0].readOnly=true;
	// document.getElementsByName(pcheldamtboxno)[0].style.background='none';
	 document.getElementsByName(form7narrationboxno)[0].readOnly=true;
	 document.getElementsByName(form7narrationboxno)[0].style.background='none';
	// document.getElementsByName(arrearflagboxno)[0].disabled=true;
	// document.getElementsByName(arrearflagboxno)[0].style.background='none';
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
    
    	function validateForm(empserialNO,adjOBYear) {
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
        var page = "report";
		var params = "&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&transferStatus="+transferStatus+"&finalrecoverytableFlag="+recoverieTable+"&reportYear="+adjOBYear+"&page="+page
		var url="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtnFrm1995toTillDate"+params;
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
   //function editFinalsettledate(pensionNo,editdate){ }
  //function editInterestDate(pensionNo,editdate){}

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
  
  
 function finalresult(empSerialNo){	
  var url ="",page="screen",flag="refresh"; 
 //alert('here'+empserialNO+"--"+Emoluments+"--"+cpfTotal+"--"+PenContriTotal+"--"+PFTotal);
 url ="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtnFrm1995toTillDate&empserialNO="+empSerialNo+"&page="+page+"&flag="+flag;
 	//alert(url);
 	document.forms[0].action=url;
   	document.forms[0].method="post";  	 
  	 document.forms[0].submit();
 
 } 
 function  gotoback(pensionno){
  
 var url= "<%=basePath%>reportservlet?method=loadPCReportForAdjDetails&frmName=form7/8psadjcrtn&empsrlNo="+pensionno;
  	
  	//alert(url);
  	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
 
    </script>
</head>

<body class="BodyBackground" onload="frmload();">


<form >
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
               String heading="Revised Form7/Form8 With Monthly Data Corrections";
                 %>
                
					<td>&nbsp;&nbsp;</td>
					 
					  <td   class="reportlabel" style="text-decoration: underline" align="center"><%=heading%></td>
					 	  <td class="Data" width="15%"><a href="#"
						onClick="validateForm('<%=empSerialNo%>','<%=adjOBYear%>')"><img
						src="./PensionView/images/printIcon.gif" border="0" alt="Print"/></a></td>      
					
					 	<td align="right">
					 
				</tr>
				<tr>
				<td>&nbsp;</td>
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
					<input type="hidden" name="pfid" value="<%=empSerialNo%>"/>
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
 
			</table>
			</td>
		</tr>

		<%
			if (pensionList.size() != 0) {
		%>

<tr> 
		<td >
		  
		<div id="process" class="white_content">
<img src="<%=basePath%>PensionView/images/Indicator.gif" border="0" align="right" />
<span class="label">Processing.......</span>
        </div>
		<div id="fade" class="black_overlay"></div>
		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="0" width="100%" align="center">

			<tr>
				<td class="label" width="10%" align="center">Month</td>
				<td class="label" width="10%" align="center">Emolument</td>
				<td class="label" width="8%" align="center">EPF</td>
				<td class="label" width="8%" align="center">VPF</td>
				<td class="label" width="8%" align="center">PRINCIPLE</td>
				<td class="label" width="8%" align="center">INTEREST</td>				
				<td class="label" width="8%" align="center">Advances</td>
				<td class="label" width="5%" align="center">PFW SUB_AMT</td>
				<td class="label" width="8%" align="center">Total</td>
				
			 	<td class="label" width="8%" align="center">PF</td>
				<td class="label" width="5%" align="center">PFW CONT_AMT</td>
			 	<td class="label" width="8%" align="center">AAI Contribution</td>		
				<td class="label" width="10%" align="center">Pension
				Contribution<br></br>(1.16%X2)&8.33%</td>
				
			
				<td class="label" width="8%" align="center">Station</td>				
                <td class="label" width="5%" align="center">Edit</td>
				<%
					if (cntFlag.equals("true")) {
				%>
				<td class="label" width="8%" align="center">Delete</td>
				<%
					}
				%>				 
				<td class="label" width="10%" align="center">Remarks</td>
			</tr>
			<%
				
						
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
									 
 						//	System.out.println("------bean.getAdvAmount------"+bean.getAdvAmount()+"---monthyear---"+bean.getMonthyear());
							pf =Math.round(Double.parseDouble(bean.getAaiPFCont()));
							advanceAmt = Math.round(Double.parseDouble(bean.getAdvAmount()));							
							pfwSubAmt =	Math.round(Double.parseDouble(bean.getEmployeeLoan()));
							pfwContriAmt =  Math.round(Double.parseDouble(bean.getAaiLoan()));
							//System.out.println("----bean.getEmpVPF()-----"+bean.getEmpVPF());						 
								
							subTotal = Math.round(Double.parseDouble(bean.getCpf()))+Math.round(Double.parseDouble(bean.getEmpVPF()))+
										Math.round(Double.parseDouble(bean.getEmpAdvRec()))+Math.round(Double.parseDouble(bean.getEmpInrstRec()));
							//System.out.println("-------	subTotal----"+subTotal);
								
							empSubscri = subTotal -(advanceAmt+pfwSubAmt);
							//System.out.println("-------	empSubscri----"+empSubscri);	
							empSubscriTot = empSubscriTot + empSubscri;
							cumempSubscri = cumempSubscri + empSubscriTot;
							
							aaiContri = pf - pfwContriAmt; 
							aaiContriTot = aaiContriTot + aaiContri;
							cumAAiContri= cumAAiContri + aaiContriTot;  
							
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
				  //System.out.println(bean.getRecordCount());
								if (bean.getRecordCount().equals("Single")) {
			%>
			<tr>

				<td class="Data" width="6%" align="center"><%=monthYear%></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" size="5"   maxlength="8"name="emoluments<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmoluments()))%>'
					onkeypress="numsDotOnly()"/></td>
				
                <td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="epf<%=j%>"
					size="5"  maxlength="8"
					value='<%=Math.round(Double.parseDouble(bean
												.getCpf()))%>'/></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="vpf<%=j%>"
					size="5"  maxlength="8"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpVPF()))%>' /></td>
				
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="principle<%=j%>"
					size="5"  maxlength="8"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpAdvRec()))%>' /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="interest<%=j%>"
					size="5"  maxlength="8"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmpInrstRec()))%>' /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="advance<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getAdvAmount()))%>'
					size="5"  maxlength="8" /></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="loan<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getEmployeeLoan()))%>'
					size="5"   maxlength="8"/></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="empsub<%=j%>"
					value='<%=empSubscri%>'
					size="5"   maxlength="8"/></td>
				<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean
												.getAaiPFCont()))%></td>
				 									
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="aailoan<%=j%>"
					value='<%=Math.round(Double.parseDouble(bean
												.getAaiLoan()))%>'
					size="5"   maxlength="8"/></td>
				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="aaicontri<%=j%>"
					value='<%=aaiContri%>'
					size="5"   maxlength="8"/></td>
									
				   <td class="Data" width="6%" align="right"><input type="text"
					readonly="true" onkeypress="numsDotOnly()" name="contr<%=j%>"
					size="5"  maxlength="8"
					value='<%=Math.round(Double.parseDouble(bean
												.getDbPensionCtr()))%>'/></td>
				
				<td class="Data" nowrap="nowrap"><%=bean.getStation()%></td>
				
        
				<td class="Data"><input type="button" name="edit<%=j%>"
					value="E"
					onclick="editEmoluments('<%=bean.getMonthyear()%>','<%=bean.getTransCpfaccno()%>','<%=bean.getRegion()%>','<%=bean.getStation()%>','<%=pensionNo%>','emoluments<%=j%>','vpf<%=j%>','principle<%=j%>','interest<%=j%>','advance<%=j%>','loan<%=j%>','aailoan<%=j%>','aaicontri<%=j%>','contr<%=j%>','edit<%=j%>','<%=userId%>','<%=bean.getDataFreezFlag()%>','form7narration<%=j%>','<%=bean.getDeputationFlag()%>','epf<%=j%>','<%=adjOBYear%>')" /></td>
 				<td class="Data" width="6%" align="right"><input type="text"
					readonly="true"  name="form7narration<%=j%>"
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
					type="button" name="E" value="E" onclick="editEmoluments() " /></font></td>
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
				<td class="HighlightData" align="right"><%=df.format(empVpf)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubscriTot)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContribution)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriTot)%></td>
				<td class="HighlightData" align="right"><%=df.format(totalPension)%></td>
				
				 
		 
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
										empSubInterest = Math
												.round((cumempSubscri
														* rateOfInterest / 100) / 12)
												+ Math.round(empSubOpeningBalance
														* rateOfInterest / 100);
										aaiContriInterest = Math
												.round((cumAAiContri
														* rateOfInterest / 100) / 12)
												+ Math.round(aaiContriOpeningBalance
														* rateOfInterest / 100);
														
									 
									 
							  
				%>   

				<td class="HighlightData" align="center">Interest(<%=rateOfInterest%>%)</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfInterest)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContributionInterest)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pensionInterest)%></td>
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
										empSubOpeningBalance= Math.round(empSubscriTot
												+ empSubInterest
												+ Math.round(empSubOpeningBalance));
										aaiContriOpeningBalance= Math.round(aaiContriTot
												+ aaiContriInterest
												+ Math.round(aaiContriOpeningBalance));
													%>

				<td class="HighlightData" align="center">CL BAL</td>
				<td class="HighlightData">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfOpeningBalance)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>				 
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfOpeningBalance)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(penOpeningBalance)%></td>
				 


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
											
									grandEmpSub = grandEmpSub + empSubscriTot;									
									grandEmpSubInterest = grandEmpSubInterest + empSubInterest;
									
									grandAAIContri = grandAAIContri + aaiContriTot;
									grandAAIContriInterest = grandAAIContriInterest + aaiContriInterest;
									
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
									empSubscriTot = 0;
									empSubInterest = 0;
									aaiContriTot = 0;
									aaiContriInterest = 0;
								}
			%>
			<%
				dispYearFlag = false;
							}
						}
					
				emolumentsTotal = df.format(grandEmoluments);
				cpfTotal = df.format(grandCPF + grandCPFInterest);
				PenContriTotal = df.format(grandPension + grandPensionInterest);
				PFTotal = df.format(grandPFContribution + grandPFContributionInterest);
				empSubTotal = df.format(grandEmpSub + grandEmpSubInterest);
				AAIContriTotal = df.format(grandAAIContri + grandAAIContriInterest);
				
			%> 
  <input type="hidden" name="status" >


		</table>
	</td>
	</tr>
	 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td>
				<table align="center" width="100%" cellpadding="0" cellspacing="0"
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
						<td class="HighlightData">Emp Subscription</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">AAI Contribution</td>
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
						<td class="HighlightData" align="right"><%=df.format(grandEmpSub)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandEmpSubInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandAAIContri)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandAAIContriInterest)%></td>
						</tr>
					<tr>
						<td class="HighlightData" align="center">Grand Total</td>
						<td class="HighlightData" align="right"><%=df.format(grandEmoluments)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandCPF + grandCPFInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPension + grandPensionInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPFContribution
									+ grandPFContributionInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandEmpSub+ grandEmpSubInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandAAIContri+ grandAAIContriInterest)%></td>
						
					</tr>
				 
				</table>
				</td>
			</tr>
	 
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
		 <tr><td>&nbsp;</td></tr>
		

		<%
			}
		%>
		
	</table>

	</tr>
<tr> 
			<td>
				<table border="0" style="border-color: gray;" cellpadding="2"
						cellspacing="0" width="100%" align="center">		
					<tr>
						<td align="center" colspan="160"> &nbsp;</td>						
					<td align="center"> <input type="button" class="btn"
						value="Back" class="btn" onclick="gotoback('<%=empSerialNo%>');"/> 
					 <input type="button"  
						value="Update " class="btn" onclick="finalresult('<%=empSerialNo%>');"/>
					</td>
						<td align="center">  &nbsp;</td>
						<td align="center">  &nbsp;</td>
						<td align="center">  &nbsp;</td>
			 </tr>
			</table>
			</td>
			
		</tr>
</table>
</form>
</body>
</html>
 