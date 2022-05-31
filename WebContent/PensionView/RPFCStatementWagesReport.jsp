<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
 <script type="text/javascript">
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

function hideTip(thi)
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
  
</script>
<title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
      <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   		  	ArrayList cardReportList=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="",chkRegionString="",chkStationString="";
	  	String arrearInfo="",orderInfo="";
	  	int size=0,finalInterestCal=0,noOfmonthsForInterest=0;

	  	CommonUtil commonUtil=new CommonUtil();
	  	if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="";
 	
    	
	   
   
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }


   
	  	cardReportList=(ArrayList)request.getAttribute("cardList");
	  	EmployeeCardReportInfo cardReport=new EmployeeCardReportInfo();
	  	size=cardReportList.size();

	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "Statement_Wages_PC_Report_FYI("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  
	  	ArrayList dateCardList=new ArrayList();
	
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  			if(size!=0){
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		
		dateCardList=cardReport.getPensionCardList();

		

       
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
	<% String months="12";
	if(personalInfo.getMnthsFlag().equals("Y")){
	months="60";
	}
	%>
    <td colspan="7" class="reportlabel"  align="center">Statement Of Wages & Pension Contribution For The Period Of <%=months%> Months Preceding The Date Of Leaving </td>
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
    <td colspan="7"><table width="90%" align="center"  border="1" cellspacing="0" cellpadding="0">
    
      <tr>
        <td width="4%" rowspan="2" class="label">Year</td>
        <td width="8%" rowspan="2" class="label">Month</td>
        <td width="8%" colspan="2" class="label" align="center">Wages</td>
        <td width="3%" rowspan="2" class="label" align="center">Pension Contribution</td>
        <td width="10%" colspan="2" class="label">Details of period of non_contributory service,if thereis no such period indicate "NIL"</td>
        <td width="10%" rowspan="2" class="label">Remarks</td>
      </tr>
      <tr>
       
        <td  class="label" align="center">No.Of Days</td>
        <td class="label" align="center">Amount</td>
        <td class="label" align="center">Year</td>
        <td  class="label" align="center">No. of days for  which no wages were earned</td>
       
       
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
		if(dispTotalPension<=0){
		dispTotalEmoluments=0;
		}
	
		if(!pensionCardInfo.getDueemoluments().equals("0")){
		
		dispTotalEmoluments=dispTotalEmoluments+Double.parseDouble(pensionCardInfo.getDueemoluments());
		dispTotalPension=dispTotalPension+Double.parseDouble(pensionCardInfo.getDuepensionamount());
		
		remarks="REVISED PAY";
		
		}else{
		remarks="---";
		}
		if(pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
		remarks=pensionCardInfo.getPfcardNarration();
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
		System.out.println("remarks==="+remarks);
  	%>
  	<span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
<tr>
	<td align="center" class="Data"><%=shownYear%> </td>
	<td align="center" class="Data"><%=pensionCardInfo.getShnMnthYear()%></td>
	<td align="center" class="Data"><%=commonUtil.GetDaysInMonth(month,year)%></td>
	<td align="center" class="Data"><%=dispTotalEmoluments%></td>
	<td align="center" class="Data"><%=dispTotalPension%></td>
	<td align="center" class="Data">&nbsp;</td>
	<%if(leavedata!=""){%>
	<td align="center" class="Data"><%=leavedata%></td>
	<%}else{%>
	<td align="center" class="Data">NIL</td>
	<%}%>
	<td align="center" class="Data"><%=pensionCardInfo.getForm7Narration()%></td>
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
	</td>
	</tr>		
   <%	}}}%>

	
					
</table>

</body>
</html>
