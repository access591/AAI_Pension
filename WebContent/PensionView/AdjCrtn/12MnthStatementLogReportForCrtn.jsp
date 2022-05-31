<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String frmName="",fileName="";
if (request.getAttribute("frmName") != null) {
				frmName = (String)request.getAttribute("frmName");
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
 
</script>

</head>

<body>
<form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   		  	ArrayList cardReportList=new ArrayList(); 
	  	int size=0 ;

	  	CommonUtil commonUtil=new CommonUtil();
	  	if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="",chkRegionString="", chkStationString="",reportType="";
 		String userId = session.getAttribute("userid").toString(); 
   
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }

	if (request.getAttribute("reportType") != null) {
		reportType = (String) request.getAttribute("reportType");
		if (reportType.equals("Excel Sheet")
				|| reportType.equals("ExcelSheet")) {
			fileName = "Pension_Contribution_report.xls"; 
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=" + fileName);
		}
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

		System.out.println("======pfid"+personalInfo.getPfID()+"==="+personalInfo.getPensionNo()+"==reportType=="+reportType+"=fileName="+fileName);

       
   %>
    
	<tr>
   <td>
   <table width="100%" height="490" cellspacing="0" cellpadding="0">
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
   
    <td width="120" rowspan="3" align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
    <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
    	<td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
     	<td width="96">&nbsp;</td>
     	<td width="95">&nbsp;</td>
     	<td width="85">&nbsp;</td>
  	 	<td width="384"  class="reportlabel">&nbsp;</td>
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

    <td colspan="7" class="reportlabel"  align="center">Statement Of Wages & Pension Contribution For The Period Of 12 Months Preceding The Date Of Leaving </td>
  </tr>     
  
  <tr>

    <td colspan="7" class="reportlabel"  align="center">&nbsp;
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
        <td class="label">8. Date of Commencement of<br/> membership of EPS :</td>
		
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
     
    <td colspan="8"> 
    
    <table width="90%" align="center"  border="1"  style="border-color: gray;"  cellspacing="0" cellpadding="0">
    
      <tr>
        <td width="4%" rowspan="2" class="label" align="center" >Year</td>
        <td width="6%" rowspan="2" class="label" align="center" >Month</td>
        <td width="8%" colspan="2" class="label" align="center">Wages</td>
        <td width="3%" rowspan="2" class="label" align="center">Pension Contribution</td>   
         <td width="10%" rowspan="2" class="label" align="center" >Remarks</td>    
                
        
      </tr>
      <tr>
       
        <td  class="label" width="4%"  align="center">No.Of Days</td>
        <td class="label" width="4%"  align="center">Amount</td>
         
       
       
      </tr>
      <tr>
        <td class="Data" align="center">1</td>
        <td class="Data" width="6%" align="center">2</td>
        <td class="Data" align="center">3</td>
        <td class="Data" align="center">4</td>
        <td class="Data" align="center">5</td> 
        <td class="Data" align="center">6</td>
         
      </tr>
    
        <%
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
		EmployeePensionCardInfo pensionCardInfo=new EmployeePensionCardInfo();
  		int month=0,year=0,tempYear=0;
  			double arrearEmoluemntsAmount=0.00,arrearContriAmount=0.00;
     	boolean arrearflags=false;
		String shownYear="",remarks="",leavedata="";
		double grandEmoluments=0.00,grandPension=0.00;
		double dispTotalEmoluments=0.00,dispTotalPension=0.00;
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
		
		dispTotalEmoluments=dispTotalEmoluments+Double.parseDouble(pensionCardInfo.getDueemoluments());
		dispTotalPension=dispTotalPension+Double.parseDouble(pensionCardInfo.getDuepensionamount());
		
		remarks="REVISED PAY";
		}else{
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
  	%>
   
  	<% if(!pensionCardInfo.getEditedDate().trim().equals("")){%> 
         				
          			<tr bgcolor="orange" >
 			 <%
 			 }else{ %>
  			<tr bgcolor="white">
			  <%} %> 
	<td class="Data" width="4%" align="right"> <%=shownYear%>  </td> 
	<td class="Data" width="4%" align="right"> <%=pensionCardInfo.getShnMnthYear()%>  </td> 
	<td class="Data" width="4%" align="right"> <%=commonUtil.GetDaysInMonth(month,year)%>  </td> 
	<td class="Data" width="4%" align="right">  <%=dispTotalEmoluments%> </td>
	<td class="Data" width="4%" align="right">  <%=dispTotalPension%> </td>
	<td class="Data" width="10%" align="right"> <%=remarks%> </td>
   
				
</tr>
  	
<%}%>
<tr>
	<td align="center" class="Data" colspan="3">Total</td>
	<td align="center"  class="Data"><%=df1.format(grandEmoluments)%></td>
	<td align="center" class="Data" ><%=df1.format(grandPension)%></td>
	<td align="center" class="Data" colspan="3">&nbsp;</td>
</tr>
<%}%>
	</table>
	</td>
	</tr>	
	</table>
	  	
   <%	}}}%>

	
					
</table>
</form>
</body>
</html>
