<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.adjcrnt.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.dao.FinancialReportDAO" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
CommonUtil commonUtil=new CommonUtil();
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
    <td colspan="22" class="reportlabel"  align="center">Statement Of Wages & Pension Contribution (Bifurcation sheet)</td>
  </tr>
    <tr>
    <td colspan="22">&nbsp;</td>
  </tr>
<tr><td align="right" colspan="6" class="Data">Date:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td></tr>

  <%    FinancialReportDAO reportDao=new FinancialReportDAO();
   		ArrayList cardReportList=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="",chkRegionString="",chkStationString="";
	  	String arrearInfo="",orderInfo="";
	  	int size=0,finalInterestCal=0,noOfmonthsForInterest=0;

	  	//CommonUtil commonUtil=new CommonUtil();
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
		
			  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("Excel")) {
					
						fileName = "Statement Of Wages & Pension Contribution_Bifurcation sheet_"+personalInfo.getPensionno()+".xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
   }%>
  <tr>
    <td colspan="22"><table width="90%" border="1"  align="center" cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label">Pf Id: </td>
        <td  class="Data"><%=personalInfo.getPensionno()%></td>
        <td class="label">EMP No: </td>
        <td class="Data"><%=personalInfo.getEmployeeno()%></td>
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">EMP Name:</td>
        <td class="Data"><%=personalInfo.getEmployeename()%></td>
        <td class="label">Statutory Rate of Contribution:</td>
        <td class="Data">1.16% and 8.33%</td>
		
      </tr>
      <tr>
        <td class="label">Father/ Husband'S Name:</td>
        <td class="Data" nowrap="nowrap"><%=personalInfo.getFhname()%></td>
        <td class="label">Date of Commencement of:</td>
        <td class="Data"><%=personalInfo.getDateofjoining()%></td>
		
      </tr>
       <tr>
        <td class="label">DATE OF BIRTH:</td>
        <td class="Data"><%=personalInfo.getDateofbirth()%></td>
        <td nowrap="nowrap" class="label">Unit:</td>
        <td class="Data">&nbsp;</td>
      </tr>
		
      <tr>

        <td nowrap="nowrap" class="label"> Name & Address of the Establishment:</td>
        <td class="Data">&nbsp;</td>
         <td class="label">Voluntary Higher rate of employees'cont.,if any:</td> 
         <td class="label">&nbsp;</td> 
      </tr>
	
    </table>
    </td>
  </tr>	
  <tr>
    <td colspan="22"><table width="90%" align="center"  border="1" cellspacing="0" cellpadding="0">
    
      <tr>
        <td width="4%" rowspan="2" class="label" align="center">Month</td>
        <td width="4%" rowspan="2" class="label" align="center">Year</td>
        <td width="8%" colspan="3" class="label" align="center">Amount of wages, retaining allowance</td>
        <td width="8%" colspan="3" class="label" align="center">Contribution to Pension Fund</td>
        <td width="4%" rowspan="2" class="label" align="center">No.of days/ period of non-contributing<br/> service if any</td>
        <td width="4%" rowspan="2" class="label" align="center">Remarks</td>
      </tr>
      <tr>
        <td  class="label" align="center">Revised</td>
        <td class="label" align="center">Previous</td>
        <td class="label" align="center">Difference</td>
        <td  class="label" align="center">Revised</td>
        <td class="label" align="center">Previous</td>
        <td class="label" align="center">Difference</td>  
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

      </tr>
        <%
        double totalnewemp=0.0,totaloldemp=0.0,totaldiffemp=0.0,totalnewpc=0.0,totalolspc=0.0,totaldiffpc=0.0,bifercationpc=0.0;
        for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(SuppPCBeanData)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		//years=personalInfo.getAdjobyear();
		dateCardList=cardReport.getPensionCardList();
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
		SuppPCBeanData pensionCardInfo=new SuppPCBeanData();
  		
  		double newbifercationemoluments=0.0,oldbifercationemoluments=0.0,newbifercationpc=0.0,oldbifercationpc=0.0,diffbifercationpc=0.0,diffbifercationemoluments=0.0;
  		
  		
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
		String newemp="",oldemp="";
  		for(int i=0;i<dateCardList.size();i++){
  		pensionCardInfo=(SuppPCBeanData)dateCardList.get(i);
  		
  		if(pensionCardInfo.getPensionOption().trim().equals("B")){
  		oldemp=reportDao.calWages(pensionCardInfo.getOldbifercationemoluments(),"01-"+pensionCardInfo.getBifercationmonthyear().substring(4)+"-"+pensionCardInfo.getFinyear(),
  		pensionCardInfo.getPensionOption().trim(),false,false,"1");
  		newemp=reportDao.calWages(pensionCardInfo.getNewbifercationemoluments(),"01-"+pensionCardInfo.getBifercationmonthyear().substring(4)+"-"+pensionCardInfo.getFinyear(),
  		pensionCardInfo.getPensionOption().trim(),false,false,"1");
  		oldbifercationemoluments=Double.parseDouble(oldemp);
  		newbifercationemoluments=Double.parseDouble(newemp);
  		newbifercationpc=Double.parseDouble(pensionCardInfo.getNewbifercationpc());
  		System.out.println("newbifercationpc:"+newbifercationpc);
  		//newbifercationpc=Math.round(newbifercationemoluments*8.33/100);
  		oldbifercationpc=Math.round(oldbifercationemoluments*8.33/100);
  		//oldbifercationpc=Double.parseDouble(pensionCardInfo.getOldbifercationpc());
  		diffbifercationpc=newbifercationpc-oldbifercationpc;
  		System.out.println("diffbifercationpc:"+diffbifercationpc);
  		diffbifercationemoluments=(newbifercationemoluments-oldbifercationemoluments);
  		totalnewemp+=newbifercationemoluments;
  		totaloldemp+=oldbifercationemoluments;
  		totalnewpc+=newbifercationpc;
  		totalolspc+=oldbifercationpc;
  		totaldiffpc+=diffbifercationpc;
  		totaldiffemp+=diffbifercationemoluments;
  		bifercationpc=Double.parseDouble(pensionCardInfo.getBifercationpc());
  		System.out.println("bifercationpc:"+bifercationpc+"totaldiffpc:"+totaldiffpc);
  		}else{
  		newbifercationemoluments=Double.parseDouble(pensionCardInfo.getNewbifercationemoluments());
  		oldbifercationemoluments=Double.parseDouble(pensionCardInfo.getOldbifercationemoluments());
  		newbifercationpc=Double.parseDouble(pensionCardInfo.getNewbifercationpc());
  		oldbifercationpc=Double.parseDouble(pensionCardInfo.getOldbifercationpc());
  		diffbifercationpc=Double.parseDouble(pensionCardInfo.getNewbifercationpc())-Double.parseDouble(pensionCardInfo.getOldbifercationpc());
  		diffbifercationemoluments=Double.parseDouble(pensionCardInfo.getDiffbifercationemoluments());
  		bifercationpc=Double.parseDouble(pensionCardInfo.getBifercationpc());
  		totalnewemp+=Double.parseDouble(pensionCardInfo.getNewbifercationemoluments());
  		totaloldemp+=Double.parseDouble(pensionCardInfo.getOldbifercationemoluments());
  		totalnewpc+=Double.parseDouble(pensionCardInfo.getNewbifercationpc());
  		totalolspc+=Double.parseDouble(pensionCardInfo.getOldbifercationpc());
  		totaldiffpc+=diffbifercationpc;
  		totaldiffemp+=Double.parseDouble(pensionCardInfo.getDiffbifercationemoluments());
  		}
		%>
  	<span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
<tr>
    <td align="center" class="Data"><%=pensionCardInfo.getBifercationmonthyear()%></td>
    <td align="center" class="Data"><%=pensionCardInfo.getFinyear()%></td>
    <td align="center" class="Data"><%=newbifercationemoluments%></td>
    <td align="center" class="Data"><%=oldbifercationemoluments%></td>
    <td align="center" class="Data"><%=diffbifercationemoluments%></td>
    <td align="center" class="Data"><%=newbifercationpc%></td>
    <td align="center" class="Data"><%=oldbifercationpc%></td>
    <td align="center" class="Data"><%=diffbifercationpc%></td>
    <td align="center" class="Data">---</td>
    <td align="center" class="Data">---</td>
</tr>
<%}}%>		
<%}%>
<tr>
	<td align="center" class="label" colspan=2 rowspan="3">TOTAL AMOUNT</td>
	<td align="center" class="label"><%=Math.round(totalnewemp) %></td>
	<td align="center" class="label"><%=Math.round(totaloldemp) %></td>
	<td align="center" class="label"><%=Math.round(totaldiffemp) %></td>
	<td align="center" class="label"><%=Math.round(totalnewpc) %></td>
	<td align="center" class="label"><%=Math.round(totalolspc) %></td>
	<td align="center" class="label"><%=Math.round(totaldiffpc) %></td>
	<td align="center" class="label">&nbsp;</td>
	<td align="center" class="label">&nbsp;</td>
</tr>
   	</table>
   		</td>
	</tr>
   </table>
   </td>
	</tr>
	 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td>
   <table width="90%" align="center"  border="1" cellspacing="0" cellpadding="0">

					<tr>
						<td class="label">PARTICULARS</td>
						<td class="label">Pension Contribution</td>
						<td class="label">Interest</td>
						<td class="label">Total</td>
					</tr>
					<tr>
						<td class="label" align="left">Total Remitted  Amount in ECR</td>
						<td class="label" align="right"><%=Math.round(totaldiffpc) %></td>
						<td class="label" align="right"><%=Math.round(bifercationpc-totaldiffpc) %></td>
						<td class="label" align="right"><%=Math.round(bifercationpc) %></td>
					</tr>
					
					 
					<tr>
					
					</tr>
				</table>
				 </td>
	</tr>
	 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td>
	  </td>
	</tr>
	 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td></td></tr>
				<tr>
						<td class="label" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NOTE:- Total amount of Rs. <%=Math.round(bifercationpc) %> has been remitted to RPFC through ECR for the month of &nbsp; _________ & ________</td>
						<td class="label" align="right"></td>
						<td class="label" align="right"></td>
						<td class="label" align="right"></td>
					</tr>
					 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td></td></tr>
	  <tr><td>&nbsp;</td></tr>
	<tr>
	 <td></td></tr>
				<tr>
					<td class="label" align="right">Signature of the Employer<br>(With office seal)</td>
				</tr>
   <%}else{
%>

<td align="center" class="label">No Recornds</td>
	<%}} %>	
	
	
</table>

</body>
</html>
