<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
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
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
       <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
        <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
      <tr>
        <td width="38%">&nbsp;</td>
        <td width="55%" class="reportlabel">EMPLOYEES PROVIDENT FUND</td>
    
      <tr>
       <%if(request.getAttribute("dspMonth")!=null){%>
        <td colspan="3" align="center" class="reportlabel">REGION/AIRPORT-WISE ABSTRACT OF CPF  RECOVERY FOR THE MONTH <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
        <%}else{%>
         <td colspan="3" align="center" class="reportlabel">REGION/AIRPORT-WISE ABSTRACT OF CPF  RECOVERY FOR THE YEAR <font style="text-decoration: underline"><%=request.getAttribute("dspYear")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
         <%}%>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
    	ArrayList cardReportList=new ArrayList();
  	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="";
  	CommonUtil commonUtil=new CommonUtil();
  	
  	if(request.getAttribute("regionDesc")!=null){
  		region=(String)request.getAttribute("regionDesc");
  	}
  	if(request.getAttribute("airportcode")!=null){
  		stationName=(String)request.getAttribute("airportcode");
  	}
  	if("NO-SELECT".equals(stationName)){
  		stationName=region;
  		filePostfix=stationName;
  	}else{
  		filePostfix=region+"_"+stationName;
  	}%>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="9%" class="reportsublabel">Unit Name:</td>
        <td width="91%"><font style="text-decoration: underline"><%=stationName%></font></td>
      </tr>
     
    </table></td>
  </tr>
      <%
DecimalFormat df = new DecimalFormat("#########0");
  	 double emolumentTotal=0,EPFTotal=0,VPFTotal=0,PFAdvTotal=0,PFAdvIntTotal=0,subGrandTotal=0,PFNetTotal=0,PFContTotal=0,contGrandTotal=0,outstandGrandTotal=0;
  	  double emolumentTotal_suppli=0,EPFTotal_suppli=0,VPFTotal_suppli=0,PFAdvTotal_suppli=0,PFAdvIntTotal_suppli=0,subGrandTotal_suppli=0,PFNetTotal_suppli=0,PFContTotal_suppli=0,contGrandTotal_suppli=0,outstandGrandTotal_suppli=0;
  	  double emolumentTotal_arrear=0,EPFTotal_arrear=0,VPFTotal_arrear=0,PFAdvTotal_arrear=0,PFAdvIntTotal_arrear=0,subGrandTotal_arrear=0,PFNetTotal_arrear=0,PFContTotal_arrear=0,contGrandTotal_arrear=0,outstandGrandTotal_arrear=0;
  	if(request.getAttribute("cardList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAI_EPF-5_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			
  	cardReportList=(ArrayList)request.getAttribute("cardList");
  	
		
	
  %>
  <tr>
  	<td>&nbsp;</td>
  </tr>
    <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
       
		<td width="8%" rowspan="2" class="reportsublabel">Airport/Unit</td>
        <td width="8%" rowspan="2" class="reportsublabel">Month</td>
        <td width="12%" rowspan="2" class="reportsublabel">No of subscribers</td>
        <td width="8%" rowspan="2" class="reportsublabel">Total salary drawn/paid for CPF deduction</td>
		<td colspan="5" align="center" class="reportsublabel">Employees subscription recovered by AAI</td>
        <td colspan="3" align="center" class="reportsublabel">AAI Contribution  </td>
        <td align="center" colspan="3" class="reportsublabel">Received from other organisation</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (6 to 9)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (11+12)</td>
        <td width="5%"  class="reportsublabel">Subscription</td>
        <td width="4%"  class="reportsublabel">Employer Contribution</td>
        <td width="4%"  class="reportsublabel">Pension  certificate amount</td>
		
     </tr>
  
     <tr></tr>
        <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0,totalSubscribers=0;
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
		epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList);
		srlno++;
     %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getContributionTotal()))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
       	<%	
       	
       		totalSubscribers=totalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
       		emolumentTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
	 		EPFTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
	 		VPFTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
	 		PFAdvTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
	 		PFAdvIntTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
	 		subGrandTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 		PFNetTotal+=Double.parseDouble(epfForm3Bean.getPf());
			PFContTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
	 		contGrandTotal+=Double.parseDouble(epfForm3Bean.getContributionTotal());
	}%>
      <tr>
      <td class="NumData" colspan="3">Grand Total</td>
      
       	<td class="NumData" ><%=totalSubscribers%></td>
        <td class="NumData" ><%=df.format(Math.round(emolumentTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(EPFTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(VPFTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvIntTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(subGrandTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(PFNetTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(PFContTotal))%></td>
        <td class="NumData"><%=df.format(Math.round(contGrandTotal))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
    </table></td>
  </tr>
  <%
  
   if(request.getAttribute("suppliList")!=null){
   ArrayList aa=new ArrayList(); 
  aa=(ArrayList)request.getAttribute("suppliList"); %>
  <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr><td width="3%" colspan="17" class="reportsublabel">Supplimentary Details</td>
      </tr>
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
       
		<td width="8%" rowspan="2" class="reportsublabel">Airport/Unit</td>
        <td width="8%" rowspan="2" class="reportsublabel">Month</td>
        <td width="12%" rowspan="2" class="reportsublabel">No of subscribers</td>
        <td width="8%" rowspan="2" class="reportsublabel">Total salary drawn/paid for CPF deduction</td>
		<td colspan="5" align="center" class="reportsublabel">Employees subscription recovered by AAI</td>
        <td colspan="3" align="center" class="reportsublabel">AAI Contribution  </td>
        <td align="center" colspan="3" class="reportsublabel">Received from other organisation</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (6 to 9)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (11+12)</td>
        <td width="5%"  class="reportsublabel">Subscription</td>
        <td width="4%"  class="reportsublabel">Employer Contribution</td>
        <td width="4%"  class="reportsublabel">Pension  certificate amount</td>
		
     </tr>
  
     <tr></tr>
        <%
     	AaiEpfform3Bean epfForm3Beans=new AaiEpfform3Bean();
     	 srlno=0;totalSubscribers=0;
     	for(int suppliList=0;suppliList<aa.size();suppliList++){
		epfForm3Beans=(AaiEpfform3Bean)aa.get(suppliList);
		srlno++;
     %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Beans.getStation()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Beans.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Beans.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getInterest()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Beans.getPf())))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Beans.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getContributionTotal()))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
       	<%	
       	
       		totalSubscribers=totalSubscribers+Integer.parseInt(epfForm3Beans.getTotalSubscribers());
       		emolumentTotal_suppli+=Double.parseDouble(epfForm3Beans.getEmoluments());
	 		EPFTotal_suppli+=Double.parseDouble(epfForm3Beans.getEmppfstatury());
	 		VPFTotal_suppli+=Double.parseDouble(epfForm3Beans.getEmpvpf());
	 		PFAdvTotal_suppli+=Double.parseDouble(epfForm3Beans.getPrincipal());
	 		PFAdvIntTotal_suppli+=Double.parseDouble(epfForm3Beans.getInterest());	 
	 		subGrandTotal_suppli+=Double.parseDouble(epfForm3Beans.getSubscriptionTotal());
	 		PFNetTotal_suppli+=Double.parseDouble(epfForm3Beans.getPf());
			PFContTotal_suppli+=Double.parseDouble(epfForm3Beans.getPensionContribution());
	 		contGrandTotal_suppli+=Double.parseDouble(epfForm3Beans.getContributionTotal());
	}%>
      <tr>
      <td class="NumData" colspan="3">Grand Total</td>
      
       	<td class="NumData" ><%=totalSubscribers%></td>
        <td class="NumData" ><%=df.format(Math.round(emolumentTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(EPFTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(VPFTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvIntTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(subGrandTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(PFNetTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(PFContTotal_suppli))%></td>
        <td class="NumData"><%=df.format(Math.round(contGrandTotal_suppli))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
    </table></td>
  </tr>
  
  
  <%}%>
  
 <% System.out.println("vvvvvvvvvvv"+request.getAttribute("arrearList"));
    if(request.getAttribute("arrearList")!=null){
   ArrayList aa=new ArrayList(); 
  aa=(ArrayList)request.getAttribute("arrearList"); %>
  <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr><td width="3%" colspan="17" class="reportsublabel">Arrear Details</td>
      </tr>
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
       
		<td width="8%" rowspan="2" class="reportsublabel">Airport/Unit</td>
        <td width="8%" rowspan="2" class="reportsublabel">Month</td>
        <td width="12%" rowspan="2" class="reportsublabel">No of subscribers</td>
        <td width="8%" rowspan="2" class="reportsublabel">Total salary drawn/paid for CPF deduction</td>
		<td colspan="5" align="center" class="reportsublabel">Employees subscription recovered by AAI</td>
        <td colspan="3" align="center" class="reportsublabel">AAI Contribution  </td>
        <td align="center" colspan="3" class="reportsublabel">Received from other organisation</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (6 to 9)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (11+12)</td>
        <td width="5%"  class="reportsublabel">Subscription</td>
        <td width="4%"  class="reportsublabel">Employer Contribution</td>
        <td width="4%"  class="reportsublabel">Pension  certificate amount</td>
		
     </tr>
  
     <tr></tr>
        <%
     	AaiEpfform3Bean epfForm3Beans=new AaiEpfform3Bean();
     	 srlno=0;totalSubscribers=0;
     	for(int arrearList=0;arrearList<aa.size();arrearList++){
		epfForm3Beans=(AaiEpfform3Bean)aa.get(arrearList);
		srlno++;
     %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Beans.getStation()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Beans.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Beans.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getInterest()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Beans.getPf())))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Beans.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Beans.getContributionTotal()))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
       	<%	
       	
       		totalSubscribers=totalSubscribers+Integer.parseInt(epfForm3Beans.getTotalSubscribers());
       		emolumentTotal_arrear+=Double.parseDouble(epfForm3Beans.getEmoluments());
	 		EPFTotal_arrear+=Double.parseDouble(epfForm3Beans.getEmppfstatury());
	 		VPFTotal_arrear+=Double.parseDouble(epfForm3Beans.getEmpvpf());
	 		PFAdvTotal_arrear+=Double.parseDouble(epfForm3Beans.getPrincipal());
	 		PFAdvIntTotal_arrear+=Double.parseDouble(epfForm3Beans.getInterest());	 
	 		subGrandTotal_arrear+=Double.parseDouble(epfForm3Beans.getSubscriptionTotal());
	 		PFNetTotal_arrear+=Double.parseDouble(epfForm3Beans.getPf());
			PFContTotal_arrear+=Double.parseDouble(epfForm3Beans.getPensionContribution());
	 		contGrandTotal_arrear+=Double.parseDouble(epfForm3Beans.getContributionTotal());
	}%>
      <tr>
      <td class="NumData" colspan="3">Grand Total</td>
      
       	<td class="NumData" ><%=totalSubscribers%></td>
        <td class="NumData" ><%=df.format(Math.round(emolumentTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(EPFTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(VPFTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(PFAdvIntTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(subGrandTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(PFNetTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(PFContTotal_arrear))%></td>
        <td class="NumData"><%=df.format(Math.round(contGrandTotal_arrear))%></td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
        <td class="Data">---</td>
     
      </tr>
    </table></td>
  </tr>
  
  
  <%}%>
  <tr>
     <table>
 <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
   
 </table> 
  <%
	  double emolumentGrandTot=0,EPFGrandTot=0,VPFGrandTot=0,PFAdvGrandTot=0,PFAdvIntGrandTot=0,subGrandGrandTot=0,PFNetGrandTot=0,PFContGrandTot=0,contGrandGrandTot=0;
	 emolumentGrandTot=Math.round(emolumentTotal)+Math.round(emolumentTotal_suppli)+Math.round(emolumentTotal_arrear);
	 EPFGrandTot=Math.round(EPFTotal)+Math.round(EPFTotal_suppli)+Math.round(EPFTotal_arrear);
	 VPFGrandTot=Math.round(VPFTotal)+Math.round(VPFTotal_suppli)+Math.round(VPFTotal_arrear);
	 PFAdvGrandTot=Math.round(PFAdvTotal)+Math.round(PFAdvTotal_suppli)+Math.round(PFAdvTotal_arrear);
	 PFAdvIntGrandTot=Math.round(PFAdvIntTotal)+Math.round(PFAdvIntTotal_suppli)+Math.round(PFAdvIntTotal_arrear);	 
	 subGrandGrandTot=Math.round(subGrandTotal)+Math.round(subGrandTotal_suppli)+Math.round(subGrandTotal_arrear);
	 PFNetGrandTot=Math.round(PFNetTotal)+Math.round(PFNetTotal_suppli)+Math.round(PFNetTotal_arrear);
	 PFContGrandTot=Math.round(PFContTotal)+Math.round(PFContTotal_suppli)+Math.round(PFContTotal_arrear);
	 contGrandGrandTot=Math.round(contGrandTotal)+Math.round(contGrandTotal_suppli)+Math.round(contGrandTotal_arrear);
	 
	%>
 
 <table width="100%" border="1" cellspacing="0" cellpadding="0">
 
 <tr>
    <td  class="reportsublabel" colspan="9" align="right">CPF Recoveries Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal))%></td>      
    <td class="Data"><%=df.format(Math.round(PFAdvTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal))%></td>    
     
      
    </tr>
    
    <tr>
    <td  class="reportsublabel" colspan="9" align="right">Supplimentary Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_suppli))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_suppli))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_suppli))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_suppli))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_suppli))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_suppli))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_suppli))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_suppli))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_suppli))%></td>    
       
 </tr>
 
 <tr>
    <td  class="reportsublabel" colspan="9" align="right">Arrear Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_arrear))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_arrear))%></td>    
      
 </tr>

<tr>
    <td  class="reportsublabel" colspan="9" align="right">Grand Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentGrandTot))%></td>
    <td class="Data"><%=df.format(Math.round(EPFGrandTot))%></td>
    <td class="Data"><%=df.format(Math.round(VPFGrandTot))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvGrandTot))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntGrandTot))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandGrandTot))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetGrandTot))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContGrandTot))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandGrandTot))%></td>    
     	  
 </tr>


 
 </table>
 <%}%>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
