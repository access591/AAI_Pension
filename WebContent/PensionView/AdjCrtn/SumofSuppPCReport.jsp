<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.adjcrnt.*"%>
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

function  GenareteForm4(empSerialNo,empName,remmonth,emoluemnts,diffpc,diffaddcontri,remarks,id,adjyear,totalpc,totalinterstpc){ 
 var url="",report="";
 var pfFlag="true";
 var swidth=screen.Width-10;
 var sheight=screen.Height-150;
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}
 	url ="<%=basePath%>pcreportservlet?method=generateform4report&empserialNO="+empSerialNo+"&empName="+empName+"&remmonth="+remmonth+"&emoluemnts="+emoluemnts+"&diffpc="+diffpc+"&diffaddcontri="+diffaddcontri+"&remarks="+remarks+"&id="+id+"&adjyear="+adjyear+"&report="+reportType+"&totalpc="+totalpc+"&totalinterstpc="+totalinterstpc;
	 	//alert(url);
   	 				 		wind1 = window.open(url,"PFCard","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();		  
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
 
   <tr>
   <td>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
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
    <td colspan="22" class="reportlabel"  align="center">SUMMARY OF SUPPLIMENTARY PC REPORT</td>
  </tr>

  <tr>
    <td colspan="22">&nbsp;</td>
  </tr>
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
	  	SuppPCBeanData cardReport=new SuppPCBeanData();
	  	size=cardReportList.size();

	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "SUMMARY_OF_SUPPLIMENTARY_PC_REPORT("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  	ArrayList dateCardList=new ArrayList();
  		SuppPCBean personalInfo=new SuppPCBean();
 
  			if(size!=0){
  			
  			
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(SuppPCBeanData)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		
   }%>
   <%if(!personalInfo.getUanno().equals("---")){ %>
  <tr>
    <td colspan="22"><table width="90%" border="1"  align="center" cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label">Pf Id: </td>
        <td  class="Data"><%=personalInfo.getPensionno()%></td>
       	<td  class="label">CPF Acc.No(Old): </td>
        <td  class="Data"><%=personalInfo.getCpfacno()%></td>
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">EMP Name:</td>
        <td class="Data"><%=personalInfo.getEmployeename()%></td>
        <td class="label">EMP No: </td>
        <td class="Data"><%=personalInfo.getEmployeeno()%></td>
		
      </tr>
      <tr>
        <td class="label">DATE OF BIRTH:</td>
        <td class="Data"><%=personalInfo.getDateofbirth()%></td>
        <td class="label">Designation:</td>
        <td class="Data"><%=personalInfo.getDesegnation()%></td>
		
      </tr>
       <tr>
        
        <td nowrap="nowrap" class="label">DATE OF JOINING:</td>
        <td class="Data"><%=personalInfo.getDateofjoining()%></td>
         <td class="label">Father/ Husband'S Name:</td>
        <td class="Data" nowrap="nowrap"><%=personalInfo.getFhname()%></td>
      </tr>
		
      <tr>

        <td nowrap="nowrap" class="label">DATE OF RETIRE:</td>
        <td class="Data"><%=personalInfo.getDateofseperation_date()%></td>
         <td class="label">Pension Option:</td> 
         <td class="label"><%=personalInfo.getWetheroption()%></td> 
      </tr>
	
    </table>
    </td>
  </tr>	
  <tr>
    <td colspan="22"><table width="90%" align="center"  border="1" cellspacing="0" cellpadding="0">
    
      <tr>
        <td width="4%" rowspan="2" class="label" nowrap>YEAR</td>
        <td width="4%" rowspan="2" class="label" nowrap>SALARY MONTH</td>
        <td width="8%" rowspan="2" class="label">OLD EMOLUMENT</td>
        <td width="4%" rowspan="2" class="label">REVISED EMOLUMENT</td>
        <td width="8%" rowspan="2" class="label">DIFFERENCE OF EMOLUMENT</td>
        <td width="8%" rowspan="2" class="label">OLD ADDITIONAL CONTRIBUTION</td>
        <td width="4%" rowspan="2" class="label">REVISED ADDITIONAL CONTRIBUTION</td>
        <td width="8%" rowspan="2" class="label">DIFFERENCE OF ADDITIONAL CONTRIBUTION</td>
        <td width="4%" rowspan="2" class="label">OLD PENSION CONTR.</td>
        <td width="8%" rowspan="2" class="label">REVISED PENSION CONTR.</td>
        <td width="4%" rowspan="2" class="label">DIFFERENCE OF PC</td>
        <td width="8%" rowspan="2" class="label">DUE MONTH FOR REMITTANCE TO RPFC</td>
        <td width="4%" rowspan="2" class="label" nowrap>ECR/PAID MONTH</td>
        <td width="8%" rowspan="2" class="label">TOTAL DELAY MONTHS( DIFF.OF ECR PAID MONTH AND DUE MONTH )</td>
        <td width="4%" rowspan="2" class="label">INTEREST THEREON (UPTO ECR/PAID MONTH)</td>
        <td width="8%" rowspan="2" class="label">Additional Contribution @1.16% exceeding salary Rs. 15000/- w.e.f. 01.09.2014</td>
        <td width="4%" rowspan="2" class="label">Interest on Addl Contribution @12% p.a.</td>
        <td width="40%" rowspan="2" class="label" align="center">Remarks</td>
        <td width="40%" rowspan="2" class="label" align="center">FormReport</td>
      </tr>
      <tr>
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
        <td class="Data" align="center">15</td>
        <td class="Data" align="center">16</td>
        <td class="Data" align="center">17</td>
        <td class="Data" align="center">18</td>
      </tr>
    
        <%
        double grandpc=0.00,grandintrest=0.00,grandaddcontri=0.0,grandintrestaddcontri=0.0;
		double grandTotals=0.00,intresttotal=0.0,diffaddpc=0.0;
		
		
	    String years="";
  		boolean flag=false;
        for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(SuppPCBeanData)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		//years=personalInfo.getAdjobyear();
		dateCardList=cardReport.getPensionCardList();
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
		SuppPCBeanData pensionCardInfo=new SuppPCBeanData();
  		int month=0,year=0,tempYear=0;
  		double grandEmoluemntsAmount=0.00,arrearContriAmount=0.00;
     	boolean arrearflags=false;
     	boolean yearflag=false;
     	int count=0;
     	int count1=0;
		String shownYear="",remarks="",leavedata="";
  		if(dateCardList.size()!=0){
  		double totalpc=0.0,totalinterstpc=0.0;
  	    double Diffpc1=0.0,Interstpc1=0.0;
		double Diffpc2=0.0,Interstpc2=0.0;
  		for(int i=0;i<dateCardList.size();i++){
  	    count1++;
  		pensionCardInfo=(SuppPCBeanData)dateCardList.get(i);
  		count=Integer.parseInt(pensionCardInfo.getCount());
  		System.out.println("count=="+count);
  		System.out.println("count1=="+count1);
  		if(years.equals(personalInfo.getAdjobyear())){
  		System.out.println("flag==111=="+yearflag);
  		yearflag=true;
  		}else{
  		yearflag=false;
  		//System.out.println("nnnnnnnnnnnn");
  		}
  		
  		years=personalInfo.getAdjobyear();
  		if(count==count1){
  		//System.out.println("mmmmmmmmmmm");
  		flag=true;
  		}
  		else{
  		flag=false;
  		}
  		grandpc+=Double.parseDouble(pensionCardInfo.getDiffpc())+Double.parseDouble(pensionCardInfo.getDiffpc1());
  		grandintrest+=Double.parseDouble(pensionCardInfo.getInterstpc())+Double.parseDouble(pensionCardInfo.getInterstpc1());
  		grandaddcontri+=Double.parseDouble(pensionCardInfo.getDiffaddpc())+Double.parseDouble(pensionCardInfo.getInterstaddpc());
  		grandintrestaddcontri+=Double.parseDouble(pensionCardInfo.getInterstaddpc())+Double.parseDouble(pensionCardInfo.getInterstpc1());
  		diffaddpc=Double.parseDouble(pensionCardInfo.getDiffaddpc());
  		System.out.println("flag=="+flag);
		if(flag == true){
		Diffpc1+=Double.parseDouble(pensionCardInfo.getDiffpc1());
  		Interstpc1+=Double.parseDouble(pensionCardInfo.getInterstpc1());
  		System.out.println("Diffpc1=="+Diffpc1+"====Interstpc1"+Interstpc1);
		}else{ 
		Diffpc2+=Double.parseDouble(pensionCardInfo.getDiffpc1());
  		Interstpc2+=Double.parseDouble(pensionCardInfo.getInterstpc1());
  		System.out.println("Diffpc2=="+Diffpc2+"====Interstpc2"+Interstpc2);
		} 
		totalpc=Diffpc1+Diffpc2;
		totalinterstpc=Interstpc1+Interstpc2;
		intresttotal=Double.parseDouble(pensionCardInfo.getInterstpc1())+Double.parseDouble(pensionCardInfo.getInterstaddpc());
		System.out.println("totalpc=="+totalpc+"====totalinterstpc"+totalinterstpc+"intresttotal==="+intresttotal+"form4id==="+personalInfo.getForm4id());
		%>
  	<span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
<%if(personalInfo.getAdjobyear().equals("1995-2008")){ %>
<tr><%if(yearflag == true){ %>
    <td align="center" class="Data">&nbsp;</td>
    <%}else{ %>
    <td align="center" class="Data" nowrap><%=years%></td>
    <%} %>
	<td align="center" class="Data" nowrap>Summary of PC report</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data"><%=pensionCardInfo.getOldpc()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getNewpc()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getDiffpc()%></td>
	<td align="center" class="Data" nowrap>15-Apr-2008</td>
	<td align="center" class="Data"><%=personalInfo.getRem_month()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getIntmonths()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getInterstpc()%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">Pension Contribution of Rs.<%=Double.parseDouble(pensionCardInfo.getDiffpc())%>/- due to editing PC Report and Interest of Rs.<%=Double.parseDouble(pensionCardInfo.getInterstpc())%>/- thereon on delayed payment being recovered from PF for remittance to RPFC</td>
    <td  class="Data" width="15%"><a href="#" onclick="GenareteForm4('<%=personalInfo.getPensionno()%>','<%=personalInfo.getEmployeename()%>','<%=personalInfo.getRem_month()%>','<%=pensionCardInfo.getEmoluments()%>','<%=Math.round(Double.parseDouble(pensionCardInfo.getDiffpc())+Double.parseDouble(pensionCardInfo.getInterstpc()))%>','0','Pension Contribution of Rs.<%=Double.parseDouble(pensionCardInfo.getDiffpc())%>/- due to editing PC Report and Interest of Rs.<%=Double.parseDouble(pensionCardInfo.getInterstpc())%>/- thereon on delayed payment being recovered from PF for remittance to RPFC','<%=personalInfo.getForm4id()%>','<%=personalInfo.getAdjobyear()%>','<%=pensionCardInfo.getDiffpc()%>','<%=pensionCardInfo.getInterstpc()%>') "><img src="./PensionView/images/form4.gif" border="0"  /></a>
	&nbsp;&nbsp; 
	</td> 
</tr>
<%}
else{ %>
<tr> 
    <%if(yearflag == true){ %>
    <td align="center" class="Data">&nbsp;</td>
    <%}else{ %>
    <td align="center" class="Data" nowrap><%=years%></td>
    <%} %>
	<td align="center" class="Data" nowrap><%=pensionCardInfo.getMonthyear()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getOldemol()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getNewemol()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getDiffemol()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getOldaddpc()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getNewaddpc()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getDiffaddpc()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getOldpc1()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getNewpc1()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getDiffpc1()%></td>
	<td align="center" class="Data">15-<%=pensionCardInfo.getEcrmonth()%></td>
	<td align="center" class="Data"><%=personalInfo.getRem_month()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getIntmonths1()%></td>
	<td align="center" class="Data"><%=pensionCardInfo.getInterstpc1()%></td>
	<%if(Integer.parseInt(personalInfo.getAdjobyear().substring(0, 4))>=2015){ %>
	<td align="center" class="Data"><%=pensionCardInfo.getInterstaddpc()%></td>
	<td align="center" class="Data"><%=grandintrestaddcontri%></td>
	<%}else{ %>
	<td align="center" class="Data">0</td>
	<td align="center" class="Data">0</td>
	<%}%>
	<td align="center" class="Data">Pension Contribution of Rs.<%=Double.parseDouble(pensionCardInfo.getDiffpc1())%>/- and  Additonal Contribution of Rs <%=diffaddpc%> due to editing PF Card <%=personalInfo.getAdjobyear()%> and Interest of Rs.<%=intresttotal%>/- thereon on delayed payment being recovered from PF for remittance to RPFC</td>
       <%if(flag == false){ %>
    <td align="center" class="Data">&nbsp;</td>
    <%}else{ %>
    <td  class="Data" width="15%" align="center"><a href="#" onclick="GenareteForm4('<%=personalInfo.getPensionno()%>','<%=personalInfo.getEmployeename()%>','<%=personalInfo.getRem_month()%>','<%=pensionCardInfo.getNewemol()%>','<%=Math.round(Double.parseDouble(pensionCardInfo.getDiffpc1())+Double.parseDouble(pensionCardInfo.getInterstpc1()))%>','<%=Math.round(Double.parseDouble(pensionCardInfo.getDiffaddpc())+Double.parseDouble(pensionCardInfo.getInterstaddpc()))%>','Pension Contribution of Rs.<%=totalpc%>/- and  Additonal Contribution of Rs <%=diffaddpc%> due to editing PF Card <%=personalInfo.getAdjobyear()%> and Interest of Rs.<%=totalinterstpc%>/- thereon on delayed payment being recovered from PF for remittance to RPFC','<%=personalInfo.getForm4id()%>','<%=personalInfo.getAdjobyear()%>','<%=totalpc %>','<%=totalinterstpc %>')"><img src="./PensionView/images/form4.gif" border="0"  /></a>
	&nbsp;&nbsp; 
	</td>     <%} %>

</tr>
<%}}}%>		
<%}%>
<tr>
	<td align="center" class="label">TOTAL AMOUNT</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="label">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="label"><%=Math.round(diffaddpc)%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="label"><%=grandpc%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="label"><%=grandintrest%></td>
	<td align="center" class="Data">&nbsp;</td>
	<%if(Integer.parseInt(personalInfo.getAdjobyear().substring(0, 4))>=2015){ %>
	<td align="center" class="label"><%=Math.round(grandintrestaddcontri)%></td>
	<%}else{ %>
	<td align="center" class="label">0</td>
	<%}%>
	
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
</tr>
   	</table>
   		</td>
	</tr>
   </table>
   <%}else{
   %>
   <td width="4%" align="right" class="label" nowrap><font color="red" size=3>This PFID has no UAN Number So please Map UAN Number for this PFID: <%=personalInfo.getPensionno() %></td></font>
   <%
   }
   }else{
%>

<td align="center" class="label">No Recornds</td>
	<%}} %>	
	
	
</table>

</body>
</html>
