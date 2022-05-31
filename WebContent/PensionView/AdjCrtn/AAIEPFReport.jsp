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
	  	SuppPCBeanData cardReport=new SuppPCBeanData();
	  	size=cardReportList.size();
	    ArrayList dateCardList=new ArrayList();
  		SuppPCBean personalInfo=new SuppPCBean();
  		if(size!=0){
  		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(SuppPCBeanData)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
   }
   %>
   <tr>
   <td>
   <table width="100%" cellspacing="0" cellpadding="0">
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
    <td colspan="12" class="reportlabel"  align="center">AAIEPF-4</td>
  </tr>
  <tr>
    <td colspan="12">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="12"><table width="90%" align="center"  border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td width="4%" rowspan="2" class="label">Sl No</td>
        <td width="4%" rowspan="2" class="label">Pf Id</td>
        <td width="4%" rowspan="2" class="label">CPF A/c No (Old)</td>
        <td width="4%" rowspan="2" class="label">Emp No</td>
        <td width="4%" rowspan="2" class="label">Name of the Member</td>
        <td width="4%" rowspan="2" class="label">Designation</td>
        <td width="4%" rowspan="2" class="label">Father's Name/ Husband name in case of Married women</td>
        <td width="4%" rowspan="2" class="label">Date of birth</td>
        <td width="4%" rowspan="2" class="label">Salary Month(dd-Mon-yyyy)</td>
        <td width="4%" rowspan="2" class="label">Name & address of organisation </td>
        <td width="4%" rowspan="2" class="label">Salary drawn/ paid for CPF deduction Rs.</td>
        <td width="8%" colspan="9" class="label" align="center">Received from other organisation</td>
        <td width="4%" rowspan="2" class="label">Station</td>
        <td width="50%" rowspan="2" class="label" nowrap>Remarks (if any)</td>
        <td width="8%" rowspan="2" class="label">status of employee(DEPUTATIONIST,RETIRED,REGULAR)</td>
        <td width="8%" rowspan="2" class="label">Form4ID</td>
      </tr>
      <tr>
        <td  class="label" align="center">EPF</td>
        <td class="label" align="center">Addl. Cont. 1.16% of Pay above 15k</td>
        <td class="label" align="center">NET EPF (11-12)</td>
        <td  class="label" align="center">VPF</td>
        <td  class="label" align="center">PF Adv recovery (Principal)</td>
        <td class="label" align="center">PF Adv recovery (Interest)</td>
        <td class="label" align="center">Employer contribution</td>
        <td  class="label" align="center">Total  (13 to 17)</td>
        <td  class="label" align="center">Pension  certificate amount</td>    
      </tr>
      <tr>
        <td class="Data" align="center">1</td>
        <td class="Data" align="center">2</td>
        <td class="Data" align="center">2(A)</td>
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
        <td class="Data" align="center">19</td>
        <td class="Data" align="center">20</td>
        <td class="Data" align="center">21</td>
        <td class="Data" align="center">22</td>
        <td class="Data" align="center">23</td>
      </tr>
    
        <%
		double grandpc=0.00,grandintrest=0.00;
		double grandTotals=0.00;
		int count=1;double grandEmoluemntsAmount=0.00;
		double Diffpc1=0.0,Interstpc1=0.0;
		String  remarks="",form4id="";
		DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(SuppPCBeanData)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		dateCardList=cardReport.getPensionCardList();
		SuppPCBeanData pensionCardInfo=new SuppPCBeanData();

  		int month=0,year=0,tempYear=0;
  		
     	boolean arrearflags=false;
		String shownYear="",leavedata="";
		
  		if(dateCardList.size()!=0){
  		for(int i=0;i<dateCardList.size();i++){
  		pensionCardInfo=(SuppPCBeanData)dateCardList.get(i);
  		Diffpc1=Double.parseDouble(pensionCardInfo.getDiffpc1());
  		Interstpc1=Double.parseDouble(pensionCardInfo.getInterstpc1());
  		//count++;
  		//System.out.println("grandpc="+pensionCardInfo.getDiffpc()+"::::"+pensionCardInfo.getDiffpc1()+
  		//":grandTotals===="+pensionCardInfo.getInterstpc()+"::"+pensionCardInfo.getInterstpc1());
   		grandpc=Double.parseDouble(pensionCardInfo.getEcontri());
  		grandintrest=Double.parseDouble(pensionCardInfo.getPc());
  		grandEmoluemntsAmount=Math.round(Double.parseDouble(pensionCardInfo.getEmolumentsform4()));
  		//System.out.println("grandpc="+grandpc+":grandTotals===="+grandTotals+":==:"+grandintrest);
  		remarks=pensionCardInfo.getRemarks();
  		form4id=pensionCardInfo.getForm4id();
  	%>
  	<span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>	
<%}}}%>
   <tr>
    <td align="center" class="Data"><%=count%></td>
    <td align="center" class="Data"><%=personalInfo.getPensionno()%></td>
    <td align="center" class="Data"><%=personalInfo.getCpfacno()%></td>
    <td align="center" class="Data"><%=personalInfo.getEmployeeno()%></td>
    <td align="center" class="Data"><%=personalInfo.getEmployeename()%></td>
    <td align="center" class="Data"><%=personalInfo.getDesegnation()%></td>
    <td align="center" class="Data">&nbsp;</td>
    <td align="center" class="Data"><%=personalInfo.getDateofbirth()%></td>
    <td align="center" class="Data"><%=personalInfo.getRem_month()%></td>
    <td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data"><%=grandEmoluemntsAmount%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data" nowrap>-<%=(grandpc)%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data"><%=(grandintrest)%></td>
	<td align="center" class="Data"><%=personalInfo.getAirportcode()%></td>
	<td width="50%" align="center" class="Data"><%=(remarks)%></td>
	<td align="center" class="Data">&nbsp;</td>
	<td align="center" class="Data"><%=form4id%></td>
</tr>
	</table>
	</td>
	</tr>	

   <%}}%>

	
					
</table>
</table>
</body>
</html>
