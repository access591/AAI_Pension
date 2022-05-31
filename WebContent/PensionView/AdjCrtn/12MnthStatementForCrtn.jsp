<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String frmName="",accessCode="";
if (request.getAttribute("frmName") != null) {
				frmName = (String)request.getAttribute("frmName");
 }
 if(request.getAttribute("accessCode")!=null){
   	accessCode=(String)request.getAttribute("accessCode");
  }
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
 var xmlHttp;
var editFlag="false";
 function editWages(monthyeartextboxno,cpfaccno, region,airportcode,daysInMonthxtextboxno,emolumentstextboxno,dueemolumentstextboxno,pensiontextboxno,duepensiontextboxno,remarkstextboxno,pensionNo,pensionoption, dateOfBirth,editid,userId){  
    var  cpfaccno1=cpfaccno;
	var region2= region;
	 
	  if(cpfaccno1=="-NA-"){
		  cpfaccno1='AOB-'+pensionNo;
	  }
  
	 
	 var monthyear= document.getElementById(monthyeartextboxno).value; 
	 var emoluments= document.getElementById(emolumentstextboxno).value;
	 var dueemoluments= document.getElementById(dueemolumentstextboxno).value;
	 var pension=document.getElementById(pensiontextboxno).value;
	 var duepension=document.getElementById(duepensiontextboxno).value;
	 var remarks=document.getElementById(remarkstextboxno).value; 
	 
	 document.getElementsByName(emolumentstextboxno)[0].readOnly=false;
	 document.getElementsByName(emolumentstextboxno)[0].focus();
	 document.getElementsByName(emolumentstextboxno)[0].style.background='#FFFFCC';
	 if(pensionoption=="A"){
	 document.getElementsByName(dueemolumentstextboxno)[0].readOnly=false;
	 document.getElementsByName(dueemolumentstextboxno)[0].focus();
	 document.getElementsByName(dueemolumentstextboxno)[0].style.background='#FFFFCC';
	 }
	 document.getElementsByName(pensiontextboxno)[0].readOnly=false;
	 document.getElementsByName(pensiontextboxno)[0].focus();	
	 document.getElementsByName(pensiontextboxno)[0].style.background='#FFFFCC';
	 
	  if(pensionoption=="A"){	 
	 document.getElementsByName(duepensiontextboxno)[0].readOnly=false;
	 document.getElementsByName(duepensiontextboxno)[0].focus();	
	 document.getElementsByName(duepensiontextboxno)[0].style.background='#FFFFCC';
	 }
	 
	 document.getElementsByName(remarkstextboxno)[0].style.background='#FFFFCC';
	 document.getElementsByName(remarkstextboxno)[0].readOnly=false;
	   
	  
	
	 var buttonName=document.getElementsByName(editid)[0].value;
	 document.getElementsByName(editid)[0].value="S";
	 createXMLHttpRequest();	
	  var answer="";
	 
	 
	 if(buttonName=="S"){
		editFlag ="true";
		var ex=/^[A-Za-z0-9_.() &\-/]+$/;
		 
		if(remarks!=""){
		if(!ex.test(remarks)){
		alert("Please Do not use comma as seperator");
		document.getElementById(from7narration1).focus();
		return false;
		} }
		//default
		var editTransFlag="N",duputationflag="N";
			process.style.display="block";
		 document.getElementById('process').style.display='block';
		 document.getElementById('fade').style.display='block';  
		 document.getElementById('update').style.display='block';  
		 
		 if(remarks=='REVISED PAY'){
		 remarks='';
		 }
		var url="<%=basePath%>reportservlet?method=editTransactionDataFor78PsAdjCrtn&pensionNo="+pensionNo+"&cpfaccno="+cpfaccno1+"&emoluments="+emoluments+"&contri="+pension+"&dueemoluments="+dueemoluments+"&duepension="+duepension+"&monthyear="+monthyear+"&region="+region2+"&airportcode="+airportcode+"&duputationflag="+duputationflag+"&pensionoption="+pensionoption+"&editTransFlag="+editTransFlag+"&dateOfBirth="+dateOfBirth+"&editid="+editid+"&swremarks="+remarks+"&frmName="+'<%=frmName%>';
        
        // alert(url);
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
	//alert(buttonupdate);
	 process.style.display="none";
	 document.getElementById('process').style.display='none';
	 document.getElementById('fade').style.display='none';
	 document.getElementsByName(buttonupdate)[0].value="E";	
	
	 var rownum=buttonupdate.substring(4, buttonupdate.length);
	 var emolumentxtextboxno="dispTotalEmoluments"+rownum;
	 var dueemolumentstextboxno="dispTotalDueEmol"+rownum;	 	 
	 var pensionboxno="dispTotalPension"+rownum;
	 var duepensiontextboxno="dispTotalDuePension"+rownum;
	  var remarksboxno="remarks"+rownum;
	   
	  
	 document.getElementsByName(emolumentxtextboxno)[0].readOnly=true;
	 document.getElementsByName(emolumentxtextboxno)[0].style.background='none';
	 document.getElementsByName(dueemolumentstextboxno)[0].readOnly=true;
	 document.getElementsByName(dueemolumentstextboxno)[0].style.background='none';
	 document.getElementsByName(pensionboxno)[0].readOnly=true;
	 document.getElementsByName(pensionboxno)[0].style.background='none';
	 document.getElementsByName(duepensiontextboxno)[0].readOnly=true;
	 document.getElementsByName(duepensiontextboxno)[0].style.background='none';  
	 document.getElementsByName(remarksboxno)[0].readOnly=true;
	 document.getElementsByName(remarksboxno)[0].style.background='none'; 
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
   function frmload(){
   
	  if(process){
	  process.style.display="none";
	  document.getElementById('process').style.display='none';
	  }
	 document.getElementById('fade').style.display='none';
	   document.getElementById('update').style.display='none';  
	}
	
	 function  gotoback(pensionno){ 
	var  url=""; 
 	url= "<%=basePath%>reportservlet?method=searchFor12MnthStatemntCtrn&frmName="+'<%=frmName%>'+"&empsrlNo="+pensionno+"&accessCode="+'<%=accessCode%>';
  
  	 //alert(url);
  	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	  
 }
  function refresh(pensionno){ 
  
	var url="<%=basePath%>reportservlet?method=get12MnthStatmntForCrtn&frmName="+'<%=frmName%>'+"&frm_pensionno="+pensionno+"&accessCode="+'<%=accessCode%>';	
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();	
 }
</script>

</head>

<body class="BodyBackground" onload="frmload();">
<form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   		  	ArrayList cardReportList=new ArrayList(); 
	  	int size=0 ;

	  	CommonUtil commonUtil=new CommonUtil();
	  	if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="",chkRegionString="", chkStationString="";
 		String userId = session.getAttribute("userid").toString(); 
   
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }


   
	  	cardReportList=(ArrayList)request.getAttribute("cardList");
	  	EmployeeCardReportInfo cardReport=new EmployeeCardReportInfo();
	  	size=cardReportList.size(); 
	  	ArrayList dateCardList=new ArrayList(); 
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  			if(size!=0){
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		
		dateCardList=cardReport.getPensionCardList();

		System.out.println("======pfid"+personalInfo.getPfID()+"==="+personalInfo.getPensionNo());

       
   %>
   <tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
   <table width="100%" height="490" cellspacing="0" cellpadding="0"> 
  
  <tr>

    <td colspan="7" class="reportlabel"  align="center">12/60 Months Statement  Corrections 
  </td>
  </tr>

  <tr>
    <td colspan="7">&nbsp;</td>
  </tr>

  <tr>
    <td colspan="7"><table width="90%" border="1"  align="center" cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label">1. Account No.: </td>
        <td  class="Data">DL/36478/<%=personalInfo.getPensionNo()%></td>
       	<td  class="label">6. Emp No.: </td>
        <td  class="Data"><%=personalInfo.getEmployeeNumber()%></td>
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">2. Name/Surname:</td>
        <td class="Data"><%=personalInfo.getEmployeeName().toUpperCase()%></td>
        <td class="label">7. Statutory Rate of Contribution </td>
		
        <td class="Data">1.16% and 8.33%</td>
		
      </tr>
      <tr>
        <td class="label">3. Father's/Husband's Name:</td>
        <td class="Data"><%=personalInfo.getFhName().toUpperCase()%></td>
        <td class="label">8. Date of Commencement of<br/> membership of EPIS :</td>
		
        <td class="Data"><%=personalInfo.getDateOfEntitle()%></td>
		
      </tr>
       <tr>
        
        <td nowrap="nowrap" class="label">4. Date Of Birth:</td>
        <td class="Data"><%=personalInfo.getDateOfBirth()%></td>
         <td class="label">9.Unit:</td>
        <td class="Data" nowrap="nowrap"><%=personalInfo.getRegion().toUpperCase()%>/<%=personalInfo.getAirportCode().toUpperCase()%></td>
      </tr>
		
      <tr>
        
        <td nowrap="nowrap" class="label">5. Name &amp; Address of the Establishment:</td>
        <td class="Data">Airports Authority Of India,<br/>Rajiv Gandhi Bhawan,Safdarjung Airport,New Delhi-3</td>
         <td class="label">10. Voluntary Higher rate of employees'cont.,if any:</td> 
         <td class="label">&nbsp;</td> 
      </tr>
	
    </table></td>
  </tr>

  <tr>
    <div id="process" class="white_content">
	<img src="<%=basePath%>PensionView/images/Indicator.gif" border="0" align="right" />
	<span class="label">Processing.......</span>
        </div>
		<div id="fade" class="black_overlay"></div>
    <td colspan="7"> 
    
    <table width="90%" align="center"  border="1"  cellspacing="0" cellpadding="0">
    
      <tr>
        <td width="4%" rowspan="2" class="label" align="center" >Year</td>
        <td width="6%" rowspan="2" class="label" align="center" >Month</td>
        <td width="8%" colspan="3" class="label" align="center">Wages</td>
         
        <td width="5%" rowspan="2" class="label" align="center">Pension Contribution</td>   
        <td width="5%" rowspan="2" class="label" align="center">Arrear Amount</td>   
         <td width="10%" rowspan="2" class="label" align="center" >Remarks</td>    
        <td width="2%" rowspan="2" class="label" align="center" >Edit</td>           
        
      </tr>
      <tr>
       
        <td  class="label" width="4%"  align="center">No.Of Days</td>
        <td class="label" width="4%"  align="center">Amount</td>
       <td class="label" width="4%"  align="center">DueEmoluments</td>
       
       
      </tr>
      <tr>
        <td class="Data"  width="10%" align="center">1</td>
        <td class="Data" width="10%" align="center">2</td>
        <td class="Data" width="10%" align="center">3</td>
        <td class="Data" align="center">4</td>
        <td class="Data" align="center">5</td> 
        <td class="Data" align="center">6</td>
        <td class="Data" align="center">7</td>
        <td class="Data" align="center">8</td>
        <td class="Data" align="center">9</td>
      </tr>
    
        <%
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
		EmployeePensionCardInfo pensionCardInfo=new EmployeePensionCardInfo();
  		int month=0,year=0,tempYear=0;
  			double arrearEmoluemntsAmount=0.00,arrearContriAmount=0.00;
     	boolean arrearflags=false;
		String shownYear="",remarks="",leavedata="";
		double grandEmoluments=0.00,grandPension=0.00,	grandDueEmoluments=0.00,  grandDuePension=0.00;
		double dispTotalEmoluments=0.00,dispTotalPension=0.00,dispTotalDueEmol=0.00,dispTotalDuePension=0.00,dispTotalEmolumentsWitDueEmol=0.00,dispTotalPensionWitArrearAmnt=0.00;
  		if(dateCardList.size()!=0){
  		for(int i=0;i<dateCardList.size();i++){
  		pensionCardInfo=(EmployeePensionCardInfo)dateCardList.get(i);
  		year=Integer.parseInt(commonUtil.converDBToAppFormat(pensionCardInfo.getDispMonthyear(),"dd-MMM-yyyy","yyyy"));
		month=Integer.parseInt(commonUtil.converDBToAppFormat(pensionCardInfo.getDispMonthyear(),"dd-MMM-yyyy","MM"));
				if(tempYear==0){
					tempYear=year;
					shownYear=Integer.toString(tempYear);
				}else if(tempYear==year){
					shownYear="&nbsp;";
				}else if(tempYear!=year){
					shownYear=Integer.toString(year);
					tempYear=year;
				}
		//new15dec11		
	    leavedata=pensionCardInfo.getLeavedata();
	    //new15dec11	end
	 	dispTotalEmoluments=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()));
		dispTotalPension=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()));
		if(!pensionCardInfo.getDueemoluments().equals("0")){
		dispTotalDueEmol=Double.parseDouble(pensionCardInfo.getDueemoluments());
		dispTotalDuePension = Double.parseDouble(pensionCardInfo.getDuepensionamount());
		dispTotalEmolumentsWitDueEmol=dispTotalEmoluments+Double.parseDouble(pensionCardInfo.getDueemoluments());
		dispTotalPensionWitArrearAmnt=dispTotalPension+Double.parseDouble(pensionCardInfo.getDuepensionamount());
		
		remarks="REVISED PAY";
		}else{
		dispTotalDueEmol=0;
		dispTotalDuePension=0;
		remarks="---";
		}
		 
		
		if(!pensionCardInfo.getSWRemarks().equals("---")){
		if(remarks.equals("---")){
		remarks=  pensionCardInfo.getSWRemarks();
		}else{
		remarks= remarks + pensionCardInfo.getSWRemarks();
		} 
		}
		
		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
    		arrearEmoluemntsAmount=Double.parseDouble(pensionCardInfo.getOringalArrearAmnt());
    		arrearContriAmount=Double.parseDouble(pensionCardInfo.getOringalArrearContri());
    		dispTotalEmoluments=dispTotalEmoluments-arrearEmoluemntsAmount;
    		dispTotalPension=dispTotalPension-arrearContriAmount;
     		arrearflags=true;
    		remarks=pensionCardInfo.getOringalArrearAmnt()+","+pensionCardInfo.getOringalArrearContri()+" "+remarks;
    	}
		grandEmoluments=grandEmoluments+dispTotalEmoluments;
		grandPension=grandPension+dispTotalPension;
	
		grandDueEmoluments=grandDueEmoluments+dispTotalDueEmol;
		grandDuePension=grandDuePension+dispTotalDuePension;
  	%>
<tr>
 
	<td class="Data" width="4%" align="right"> <%=shownYear%> 
		 <input type="hidden"   name="shownYear<%=i%>"	id="shownYear<%=i%>" value='<%=shownYear%>'	/>  </td>
	
	<td class="Data" width="4%" align="right"> <%=pensionCardInfo.getShnMnthYear()%>
		 <input type="hidden"   name="shnMnthYear<%=i%>" id="shnMnthYear<%=i%>"	value='<%=pensionCardInfo.getMonthyear()%>'/>  </td>
		 
	<td class="Data" width="4%" align="right"> <%=commonUtil.GetDaysInMonth(month,year)%>
		 <input type="hidden"   name="daysInMonth<%=i%>" id="daysInMonth<%=i%>"	value='<%=commonUtil.GetDaysInMonth(month,year)%>'/>  </td>
		 				    
	 
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="12" maxlength="8" name="dispTotalEmoluments<%=i%>" id="dispTotalEmoluments<%=i%>"
					value='<%=dispTotalEmoluments%>'
					onkeypress="numsDotOnly()"/></td>
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="12" maxlength="8" name="dispTotalDueEmol<%=i%>" id="dispTotalDueEmol<%=i%>"
					value='<%=dispTotalDueEmol%>'
					onkeypress="numsDotOnly()"/></td>
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="12" maxlength="8" name="dispTotalPension<%=i%>" id="dispTotalPension<%=i%>"
					value='<%=dispTotalPension%>'
					onkeypress="numsDotOnly()"/></td>						 
	<td class="Data" width="4%" align="right"><input type="text"
					readonly="true" size="12" maxlength="8" name="dispTotalDuePension<%=i%>" id="dispTotalDuePension<%=i%>"
					value='<%=dispTotalDuePension%>'
					onkeypress="numsDotOnly()"/></td>
	<td class="Data" width="10%" align="right"><input type="text"
					readonly="true" size="40" maxlength="40" name="remarks<%=i%>" id="remarks<%=i%>"
					value='<%=remarks%>' /></td>
   <td class="Data"><input type="button" name="edit<%=i%>"  id="edit<%=i%>" width="2%" style = "cursor:hand;" 
					value="E"
					onclick="editWages('shnMnthYear<%=i%>','<%=pensionCardInfo.getCpfAccno()%>','<%=pensionCardInfo.getRegion()%>','<%=pensionCardInfo.getStation()%>','daysInMonth<%=i%>','dispTotalEmoluments<%=i%>','dispTotalDueEmol<%=i%>','dispTotalPension<%=i%>','dispTotalDuePension<%=i%>','remarks<%=i%>','<%=personalInfo.getPensionNo()%>','<%=personalInfo.getWetherOption().trim()%>','<%=personalInfo.getDateOfBirth()%>','edit<%=i%>','<%=userId%>')" /></td>
 	
	 
				
</tr>
  	
<%}%>
<tr>
	<td align="center" class="Data" colspan="3">Total</td>
	<td align="center"  class="Data"><%=df1.format(grandEmoluments)%></td>
	<td align="center"  class="Data"><%=df1.format(grandDueEmoluments)%></td>
	<td align="center" class="Data" ><%=df1.format(grandPension)%></td>
	<td align="center" class="Data" ><%=df1.format(grandDuePension)%></td>
	<td align="center" class="Data" colspan="3">&nbsp;</td>
</tr>
<%}%>
	</table>
	</td>
	</tr>	
	</table>
	 <table border="0" style="border-color: gray;" cellpadding="2"
						cellspacing="0" width="100%" align="center">	
					<tr><td>&nbsp;</td></tr>	
					<tr>
						<td align="center" colspan="160"> &nbsp;</td>
												
					<td align="right"> <input type="button" class="btn"
						value="Back" class="btn" onclick="gotoback('<%=personalInfo.getPensionNo()%>');"/> 
					   <input type="button"   id="update"
						value="Update " class="btn" onclick="refresh('<%=personalInfo.getPensionNo()%>');"/>
						
					  </td>
						 
						<td align="center">  &nbsp;</td>
						<td align="center">  &nbsp;</td>
						 	 </tr>
			</table>		
   <%	}}}%>

	
					
</table>
</form>
</body>
</html>
