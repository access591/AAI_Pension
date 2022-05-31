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
<table width="100%" border="0"  cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
       <td width="2%" rowspan="0" class="reportlabel"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="center" />AIRPORTS AUTHORITY OF INDIA</td>
       <td>&nbsp;</td>
      </tr>
      <tr>
        <td width="38%">&nbsp;</td>
        <td width="55%" class="reportlabel">EMPLOYEES PROVIDENT FUND</td>
    
      <tr>
       <%if(request.getAttribute("dspMonth")!=null){%>
        <td colspan="3" align="center" class="reportlabel">SUMMARY OF PF ACCRETION, PENSION CONTRIBUTION & INSPECTION CHARGES <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
        <%}else{%>
         <td colspan="3" align="center" class="reportlabel">SUMMARY OF PF ACCRETION, PENSION CONTRIBUTION & INSPECTION CHARGES <font style="text-decoration: underline"><%=request.getAttribute("dspYear")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
         <%}%>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
    ArrayList cardReportList=new ArrayList();
  	ArrayList epfForm3supplReportList=new ArrayList();
   	ArrayList epfForm3ArrReportList=new ArrayList();
    ArrayList form4DeputationReportList=new ArrayList();
  	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="";
  	String formType="";
  	CommonUtil commonUtil=new CommonUtil();
  	
  	if(request.getAttribute("regionDesc")!=null){
  		region=(String)request.getAttribute("regionDesc");
  	}
  	if(request.getAttribute("airportcode")!=null){
  		stationName=(String)request.getAttribute("airportcode");
  	}
  	if(request.getAttribute("remitancetype")!=null){
  		formType=(String)request.getAttribute("remitancetype");
  	}
  	
  	if(formType.equals("aaiepf")){
  	formType="AAIEPF-3 ACCRETAION:";
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
      double emolumentTotal=0,EPFTotal=0,VPFTotal=0,PFAdvTotal=0,PFAdvIntTotal=0,AddContriNetTotal=0,subGrandTotal=0,PFNetTotal=0,PFContTotal=0,contGrandTotal=0,outstandGrandTotal=0,calcemolumentsGrandTotal=0,inspectionchargesgrandtotal=0;
      double emolumentSupplTotal=0,EPFSupplTotal=0,VPFSupplTotal=0,PFAdvSupplTotal=0,PFAdvIntSupplTotal=0,AddContriSupplTotal=0,subGrandSupplTotal=0,PFNetSupplTotal=0,PFContSupplTotal=0,contGrandSupplTotal=0,outstandGrandSupplTotal=0,calcemolumentsGrandSupplTotal=0,inspectionchargesgrandSuppltotal=0;     
      double emolumentArrTotal=0,EPFArrTotal=0,VPFArrTotal=0,PFAdvArrTotal=0,PFAdvIntArrTotal=0,AddContriArrTotal=0,subGrandArrTotal=0,PFNetArrTotal=0,PFContArrTotal=0,contGrandArrTotal=0,outstandGrandArrTotal=0,calcemolumentsGrandArrTotal=0,inspectionchargesgrandarrtotal=0;     
      
      double emolumentForm4DepTotal=0,EPFForm4DepTotal=0,VPFForm4DepTotal=0,PFAdvForm4DepTotal=0,PFAdvIntForm4DepTotal=0,subGrandForm4DepTotal=0,PFNetForm4DepTotal=0,AddConrtiForm4DepTotal=0,PFContForm4DepTotal=0,contGrandForm4DepTotal=0,outstandGrandForm4DepTotal=0,calcemolumentsGrandForm4DepTotal=0,inspectionchargesgrandForm4Deptotal=0;
      if(request.getAttribute("cardList")!=null){  		
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "PFACCRETION_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  	cardReportList=(ArrayList)request.getAttribute("cardList");	
  %>  
  


<tr>
   <td class="reportsublabel" align="left"><u><%=formType %></u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>     
        <td width="3%"  class="reportsublabel">Sl.No</td>
		<td width="3%"  class="reportsublabel">UnitCode</td>
		<td width="8%"  class="reportsublabel">UNIT NAME</td>
		<td width="3%"  class="reportsublabel">ACCOUNT No. </td>
	    <td width="8%"  class="reportsublabel">MONTH</td>
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="8%"  class="reportsublabel">EMOLUMENTS</td>
        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">REFUND_ADV</td>
        <td width="8%"  class="reportsublabel">INTEREST</td>
        <td width="5%"  class="reportsublabel">AAI CONT</td> 
        <td width="5%"  class="reportsublabel">ADDITIONAL CONTRI</td> 
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension Emoluments </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>
    
    </tr>
 <tr>
        <td width="3%" class="reportsublabel">1</td>
		<td width="8%" class="reportsublabel">2</td>
        <td width="8%" class="reportsublabel">3</td>
        <td width="12%" class="reportsublabel">4</td>
        <td width="8%" class="reportsublabel">5</td>
        <td width="6%"  class="reportsublabel">6</td>
        <td width="6%"  class="reportsublabel">7</td>
        <td width="8%"  class="reportsublabel">8</td>
        <td width="8%"  class="reportsublabel">9</td>
        <td width="5%"  class="reportsublabel">10</td> 
        <td width="4%"  class="reportsublabel">11</td>
        <td width="4%"  class="reportsublabel">12 </td>
        <td width="4%"  class="reportsublabel">12a </td>
        <td width="4%"  class="reportsublabel">13=&nbsp;(8&nbsp;to&nbsp;12) </td>
        <td width="4%"  class="reportsublabel">14 </td>
        <td width="4%"  class="reportsublabel">15 </td>
		<td width="4%"  class="reportsublabel">16 </td>
		<td width="4%"  class="reportsublabel">17 </td>
</tr>
        <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0,totalepfSubscribers=0,totalpcnoofepfemployees=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalAddContri=0,rauTotalPF=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0;
     	String tempAccountType="";
     	String tempRegion="";
     	String tempAccount="";
     	String tempRegionType="";
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
     		 if(cardList>0){
     			epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList-1);	 
     			tempAccountType=epfForm3Bean.getAccountType();     		
     			tempRegion=epfForm3Bean.getRegion();     			
 				}
		epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList);
		srlno++;
     %>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	 
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
    <tr>
    <td colspan="6" align="right" class="Data"> <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalVPF)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPrinciple)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInterest)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalAddContri)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>
    </tr>
    <%}%>
   <tr ><td colspan="2" class="HighlightData" align="center"><%=epfForm3Bean.getRegion()%> </td>
   <td colspan="16" class="HighlightData" align="left"> <%=epfForm3Bean.getAccountType()%>&nbsp;</td>
   </tr>  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalAddContri=0;rauTotalPF=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
  tempAccount=epfForm3Bean.getAccountType();
  tempRegionType=epfForm3Bean.getRegion();  
  }  
 if(tempAccount.trim().equals(epfForm3Bean.getAccountType()) || tempRegionType.trim().equals(epfForm3Bean.getRegion())) {
	  	 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalEmoluments=rauTotalEmoluments+Double.parseDouble(epfForm3Bean.getEmoluments());
		 rauTotalEPF=rauTotalEPF+Double.parseDouble(epfForm3Bean.getEmppfstatury());
		 rauTotalVPF=rauTotalVPF+Double.parseDouble(epfForm3Bean.getEmpvpf());
		 rauTotalPrinciple=rauTotalPrinciple+Double.parseDouble(epfForm3Bean.getPrincipal());
		 rauTotalInterest=rauTotalInterest+Double.parseDouble(epfForm3Bean.getInterest());
		 rauTotalPF=rauTotalPF+Math.round(Double.parseDouble(epfForm3Bean.getPf()));
		 rauTotalAddContri=rauTotalAddContri+Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri()));
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
	  } %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
	 <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getNewunitcode()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
		<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getVAccountNo()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
           <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContriEmoluments()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
       
    </tr>
 
       	<%	
       	
       	  totalepfSubscribers=totalepfSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
       		emolumentTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
	 		EPFTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
	 		VPFTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
	 		PFAdvTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
	 		PFAdvIntTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
	 		PFNetTotal+=Double.parseDouble(epfForm3Bean.getPf());
	 		AddContriNetTotal+=Double.parseDouble(epfForm3Bean.getAdditionalContri());
	 		subGrandTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 		inspectionchargesgrandtotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 		totalpcnoofepfemployees=totalpcnoofepfemployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 		calcemolumentsGrandTotal+=Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
	 		PFContTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			
	}%>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalepfSubscribers%></B></td>
        <td class="NumData" ><B><%=df.format(Math.round(emolumentTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(AddContriNetTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandtotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofepfemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContTotal))%></B></td>
        
      </tr>
    </table></td>

  <%}%>
<%
if(request.getAttribute("epfForm3supplList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "PFACCRETION_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  			epfForm3supplReportList=(ArrayList)request.getAttribute("epfForm3supplList");
   %>

  


<tr>
   <td class="reportsublabel" align="left"><u>AAIEPF-3SUPPL ACCRETAION:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>     
        <td width="3%"  class="reportsublabel">Sl.No</td>
		<td width="3%"  class="reportsublabel">UnitCode</td>
		<td width="8%"  class="reportsublabel">UNIT NAME</td>
		<td width="3%"  class="reportsublabel">ACCOUNT No. </td>
	    <td width="8%"  class="reportsublabel">MONTH</td>
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="8%"  class="reportsublabel">EMOLUMENTS</td>
        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">REFUND_ADV</td>
        <td width="8%"  class="reportsublabel">INTEREST</td>
        <td width="5%"  class="reportsublabel">AAI CONT</td>
        <td width="5%"  class="reportsublabel">ADDITIONAL CONTRI</td>  
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension Emoluments </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>    
    </tr>
 <tr>
        <td width="3%" class="reportsublabel">1</td>
		<td width="8%" class="reportsublabel">2</td>
        <td width="8%" class="reportsublabel">3</td>
        <td width="12%" class="reportsublabel">4</td>
        <td width="8%" class="reportsublabel">5</td>
        <td width="6%"  class="reportsublabel">6</td>
        <td width="6%"  class="reportsublabel">7</td>
        <td width="8%"  class="reportsublabel">8</td>
        <td width="8%"  class="reportsublabel">9</td>
        <td width="5%"  class="reportsublabel">10</td> 
        <td width="4%"  class="reportsublabel">11</td>
        <td width="4%"  class="reportsublabel">12 </td>
        <td width="4%"  class="reportsublabel">12a </td>
        <td width="4%"  class="reportsublabel">13=&nbsp;(8&nbsp;to&nbsp;12) </td>
        <td width="4%"  class="reportsublabel">14 </td>
        <td width="4%"  class="reportsublabel">15 </td>
		<td width="4%"  class="reportsublabel">16 </td>
		<td width="4%"  class="reportsublabel">17 </td>
</tr>
        <%        
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0,totalsupplSubscribers=0,totalpcnoofsupplemployees=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAddContri=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0;
     	String tempAccountType="";
     	String tempRegion="";
     	for(int epfForm3supplList=0;epfForm3supplList<epfForm3supplReportList.size();epfForm3supplList++){
     		 if(epfForm3supplList>0){
     			epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3supplList-1);	 
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();
 				}
		epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3supplList);
		srlno++;
     %>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
     <%if(rautotalSubscribers!=0){%>
    <tr><td colspan="6" align="right" class="Data"> <%=rautotalSubscribers%></td><td class="Data" align="right"><%=df.format(rauTotalEmoluments)%></td><td class="Data" align="right"><%=df.format(rauTotalEPF)%></td> <td class="Data" align="right"><%=df.format(rauTotalVPF)%></td><td class="Data" align="right"><%=df.format(rauTotalPrinciple)%></td><td class="Data" align="right"><%=df.format(rauTotalInterest)%></td><td class="Data" align="right"><%=df.format(rauTotalPF)%></td><td class="Data" align="right"><%=df.format(rauTotalAddContri)%></td> <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td><td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td><td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td><td class="Data" align="right"><%=df.format(rauTotalPensionEmoluments)%></td><td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td></tr>
    <%} }%>
   <tr ><td colspan="2" class="HighlightData" align="center"><%=epfForm3Bean.getRegion()%> </td><td colspan="16" class="HighlightData" align="left"> <%=epfForm3Bean.getAccountType()%> &nbsp;</td></tr>
  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0; rauTotalAddContri=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;}else{
	  if(tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || tempRegion.trim().equals(epfForm3Bean.getRegion())) {
		 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalEmoluments=rauTotalEmoluments+Double.parseDouble(epfForm3Bean.getEmoluments());
		 rauTotalEPF=rauTotalEPF+Double.parseDouble(epfForm3Bean.getEmppfstatury());
		 rauTotalVPF=rauTotalVPF+Double.parseDouble(epfForm3Bean.getEmpvpf());
		 rauTotalPrinciple=rauTotalPrinciple+Double.parseDouble(epfForm3Bean.getPrincipal());
		 rauTotalInterest=rauTotalInterest+Double.parseDouble(epfForm3Bean.getInterest());
		 rauTotalPF=rauTotalPF+Math.round(Double.parseDouble(epfForm3Bean.getPf()));
		 rauTotalAddContri=rauTotalAddContri+Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri()));
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
	  } %>

<%} %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
	 <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getNewunitcode()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
		<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getVAccountNo()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
         <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContriEmoluments()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
       
    </tr>
 
       	<%	
       	
       	totalsupplSubscribers=totalsupplSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
       	emolumentSupplTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
       	EPFSupplTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
       	VPFSupplTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
       	PFAdvSupplTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
       	PFAdvIntSupplTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
       	PFNetSupplTotal+=Double.parseDouble(epfForm3Bean.getPf());
       	AddContriSupplTotal+=Double.parseDouble(epfForm3Bean.getAdditionalContri());
	 	subGrandSupplTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 	inspectionchargesgrandSuppltotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 	totalpcnoofsupplemployees=totalpcnoofsupplemployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 	calcemolumentsGrandSupplTotal+=Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
	 	PFContSupplTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			
	}%>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalsupplSubscribers%></B></td>
        <td class="NumData" ><B><%=df.format(Math.round(emolumentSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(AddContriSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandSuppltotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofsupplemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContSupplTotal))%></B></td>
        
      </tr>
    </table></td>

  <%}%>
<%
if(request.getAttribute("epfForm3ArrList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "PFACCRETION_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  			epfForm3ArrReportList=(ArrayList)request.getAttribute("epfForm3ArrList");
   %>

  

<tr>
   <td class="reportsublabel" align="left"><u>AAIEPF-3ARR ACCRETAION:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>     
        <td width="3%"  class="reportsublabel">Sl.No</td>
		<td width="3%"  class="reportsublabel">UnitCode</td>
		<td width="8%"  class="reportsublabel">UNIT NAME</td>
		<td width="3%"  class="reportsublabel">ACCOUNT No. </td>
	    <td width="8%"  class="reportsublabel">MONTH</td>
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="8%"  class="reportsublabel">EMOLUMENTS</td>
        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">REFUND_ADV</td>
        <td width="8%"  class="reportsublabel">INTEREST</td>
        <td width="5%"  class="reportsublabel">AAI CONT</td> 
          <td width="5%"  class="reportsublabel">ADDITIONAL CONTRI</td> 
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension Emoluments </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>    
    </tr>
 <tr>
        <td width="3%" class="reportsublabel">1</td>
		<td width="8%" class="reportsublabel">2</td>
        <td width="8%" class="reportsublabel">3</td>
        <td width="12%" class="reportsublabel">4</td>
        <td width="8%" class="reportsublabel">5</td>
        <td width="6%"  class="reportsublabel">6</td>
        <td width="6%"  class="reportsublabel">7</td>
        <td width="8%"  class="reportsublabel">8</td>
        <td width="8%"  class="reportsublabel">9</td>
        <td width="5%"  class="reportsublabel">10</td> 
        <td width="4%"  class="reportsublabel">11</td>
        <td width="4%"  class="reportsublabel">12 </td>
        <td width="4%"  class="reportsublabel">12a </td>
        <td width="4%"  class="reportsublabel">13=&nbsp;(8&nbsp;to&nbsp;12) </td>
        <td width="4%"  class="reportsublabel">14 </td>
        <td width="4%"  class="reportsublabel">15 </td>
		<td width="4%"  class="reportsublabel">16 </td>
		<td width="4%"  class="reportsublabel">17 </td>
</tr>
        <%        
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0,totalArrSubscribers=0,totalpcnoofArremployees=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAddContri=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0;
     	String tempAccountType="";
     	String tempRegion="";
     	for(int epfForm3ArrList=0;epfForm3ArrList<epfForm3ArrReportList.size();epfForm3ArrList++){
     		 if(epfForm3ArrList>0){
     			epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3ArrList-1);	 
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();     			
 				}
		epfForm3Bean=(AaiEpfform3Bean)epfForm3ArrReportList.get(epfForm3ArrList);
		srlno++;
     %>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
    <%if(rautotalSubscribers!=0){ %>
    <tr><td colspan="6" align="right" class="Data"> <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalVPF)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPrinciple)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInterest)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalAddContri)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>
    </tr>
    <%}}%>
   <tr ><td colspan="2" class="HighlightData" align="center"><%=epfForm3Bean.getRegion()%> </td><td colspan="16" class="HighlightData" align="left"> <%=epfForm3Bean.getAccountType()%> &nbsp;</td></tr>
  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0;rauTotalAddContri=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
  }else{
	  if(tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || tempRegion.trim().equals(epfForm3Bean.getRegion())) {
		 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalEmoluments=rauTotalEmoluments+Double.parseDouble(epfForm3Bean.getEmoluments());
		 rauTotalEPF=rauTotalEPF+Double.parseDouble(epfForm3Bean.getEmppfstatury());
		 rauTotalVPF=rauTotalVPF+Double.parseDouble(epfForm3Bean.getEmpvpf());
		 rauTotalPrinciple=rauTotalPrinciple+Double.parseDouble(epfForm3Bean.getPrincipal());
		 rauTotalInterest=rauTotalInterest+Double.parseDouble(epfForm3Bean.getInterest());
		 rauTotalPF=rauTotalPF+Math.round(Double.parseDouble(epfForm3Bean.getPf()));
		 rauTotalAddContri=rauTotalAddContri+Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri()));
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
	  } %>

<%} %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
	    <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getNewunitcode()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
		<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getVAccountNo()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContriEmoluments()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
       
    </tr>    
      
       	<%	
       
       	totalArrSubscribers=totalArrSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
       	emolumentArrTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
       	EPFArrTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
       	VPFArrTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
       	PFAdvArrTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
       	PFAdvIntArrTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
       	PFNetArrTotal+=Double.parseDouble(epfForm3Bean.getPf());
       	AddContriArrTotal+=Double.parseDouble(epfForm3Bean.getAdditionalContri());
	 	subGrandArrTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 	inspectionchargesgrandarrtotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 	totalpcnoofArremployees=totalpcnoofArremployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 	calcemolumentsGrandArrTotal+=Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
	 	PFContArrTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			
	}%>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalArrSubscribers%></B></td>
        <td class="NumData" ><B><%=df.format(Math.round(emolumentArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(AddContriArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandarrtotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofArremployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContArrTotal))%></B></td>
        
      </tr>
    </table></td>

  <%} %>
  
 
 <!-- --> 
 
 
 
 <%
if(request.getAttribute("form4DeputationList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "PFACCRETION_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  			form4DeputationReportList=(ArrayList)request.getAttribute("form4DeputationList");
   %>

  

<tr>
   <td class="reportsublabel" align="left"><u>FORM4-DEPUTATION LIST:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>     
        <td width="3%"  class="reportsublabel">Sl.No</td>
		<td width="3%"  class="reportsublabel">UnitCode</td>
		<td width="8%"  class="reportsublabel">UNIT NAME</td>
		<td width="3%"  class="reportsublabel">ACCOUNT No. </td>
	    <td width="8%"  class="reportsublabel">MONTH</td>
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="8%"  class="reportsublabel">EMOLUMENTS</td>
        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">REFUND_ADV</td>
        <td width="8%"  class="reportsublabel">INTEREST</td>
        <td width="5%"  class="reportsublabel">AAI CONT</td> 
         <td width="5%"  class="reportsublabel">ADDITIONAL CONTRI</td>
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension Emoluments </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>    
    </tr>
 <tr>
        <td width="3%" class="reportsublabel">1</td>
		<td width="8%" class="reportsublabel">2</td>
        <td width="8%" class="reportsublabel">3</td>
        <td width="12%" class="reportsublabel">4</td>
        <td width="8%" class="reportsublabel">5</td>
        <td width="6%"  class="reportsublabel">6</td>
        <td width="6%"  class="reportsublabel">7</td>
        <td width="8%"  class="reportsublabel">8</td>
        <td width="8%"  class="reportsublabel">9</td>
        <td width="5%"  class="reportsublabel">10</td> 
        <td width="4%"  class="reportsublabel">11</td>
        <td width="4%"  class="reportsublabel">12 </td>
        <td width="4%"  class="reportsublabel">12a </td>
        <td width="4%"  class="reportsublabel">13=&nbsp;(8&nbsp;to&nbsp;12) </td>
        <td width="4%"  class="reportsublabel">14 </td>
        <td width="4%"  class="reportsublabel">15 </td>
		<td width="4%"  class="reportsublabel">16 </td>
		<td width="4%"  class="reportsublabel">17 </td>
</tr>
        <%        
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0,totalForm4depSubscribers=0,totalpcnoofForm4depemployees=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAddContri=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0;
     	String tempAccountType="";
     	String tempRegion="";
     	for(int form4DeputationList=0;form4DeputationList<form4DeputationReportList.size();form4DeputationList++){
     		 if(form4DeputationList>0){
     			epfForm3Bean=(AaiEpfform3Bean)form4DeputationReportList.get(form4DeputationList-1);	 
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();     			
 				}
		epfForm3Bean=(AaiEpfform3Bean)form4DeputationReportList.get(form4DeputationList);
		srlno++;
     %>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
    <%if(rautotalSubscribers!=0){%>
    <tr><td colspan="6" align="right" class="Data"> <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalEPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalVPF)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPrinciple)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInterest)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPF)%></td> 
    <td class="Data" align="right"><%=df.format(rauTotalAddContri)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionEmoluments)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>
    </tr>
    <%}}%>
   <tr ><td colspan="2" class="HighlightData" align="center"><%=epfForm3Bean.getRegion()%> </td><td colspan="16" class="HighlightData" align="left"> <%=epfForm3Bean.getAccountType()%> &nbsp;</td></tr>
  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0;rauTotalAddContri=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
  }else{
	  if(tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || tempRegion.trim().equals(epfForm3Bean.getRegion())) {
		 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalEmoluments=rauTotalEmoluments+Double.parseDouble(epfForm3Bean.getEmoluments());
		 rauTotalEPF=rauTotalEPF+Double.parseDouble(epfForm3Bean.getEmppfstatury());
		 rauTotalVPF=rauTotalVPF+Double.parseDouble(epfForm3Bean.getEmpvpf());
		 rauTotalPrinciple=rauTotalPrinciple+Double.parseDouble(epfForm3Bean.getPrincipal());
		 rauTotalInterest=rauTotalInterest+Double.parseDouble(epfForm3Bean.getInterest());
		 rauTotalPF=rauTotalPF+Math.round(Double.parseDouble(epfForm3Bean.getPf()));
		 rauTotalAddContri=rauTotalAddContri+Math.round(Double.parseDouble(epfForm3Bean.getAdditionalContri()));
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
	  } %>

<%} %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
	    <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getNewunitcode()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
		<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getVAccountNo()%></td>
      	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPf())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContriEmoluments()))%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
       
    </tr>    
      
       	<%	
       
       	totalForm4depSubscribers=totalForm4depSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
       	emolumentForm4DepTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
       	EPFForm4DepTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
       	VPFForm4DepTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
       	PFAdvForm4DepTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
       	PFAdvIntForm4DepTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
       	PFNetForm4DepTotal+=Double.parseDouble(epfForm3Bean.getPf());
       	AddConrtiForm4DepTotal+=Double.parseDouble(epfForm3Bean.getAdditionalContri());
	 	subGrandForm4DepTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 	inspectionchargesgrandForm4Deptotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 	totalpcnoofForm4depemployees=totalpcnoofForm4depemployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 	calcemolumentsGrandForm4DepTotal+=Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
	 	PFContForm4DepTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			
	}%>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalForm4depSubscribers%></B></td>
        <td class="NumData" ><B><%=df.format(Math.round(emolumentForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(AddConrtiForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandForm4Deptotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofForm4depemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContForm4DepTotal))%></B></td>
        
      </tr>
    </table></td>

  <%} %>
 
 
 
 
 
 
 
 
 
 
 <!-- -->
 
<tr>
&nbsp;
</tr>
<%
double totalinspectionchargesgrandtotal=0,emolumentGrandTot=0,EPFGrandTot=0,VPFGrandTot=0,PFAdvGrandTot=0,PFAdvIntGrandTot=0,AddContriGrandTot=0,subGrandGrandTot=0,PFNetGrandTot=0,PFContGrandTot=0,contGrandGrandTot=0;
    emolumentGrandTot=Math.round(emolumentTotal)+Math.round(emolumentSupplTotal)+Math.round(emolumentArrTotal)+Math.round(emolumentForm4DepTotal);
	 EPFGrandTot=Math.round(EPFTotal)+Math.round(EPFSupplTotal)+Math.round(EPFArrTotal)+Math.round(EPFForm4DepTotal);
	 VPFGrandTot=Math.round(VPFTotal)+Math.round(VPFSupplTotal)+Math.round(VPFArrTotal)+Math.round(VPFForm4DepTotal);
	 PFAdvGrandTot=Math.round(PFAdvTotal)+Math.round(PFAdvSupplTotal)+Math.round(PFAdvArrTotal)+Math.round(PFAdvForm4DepTotal);
	 PFAdvIntGrandTot=Math.round(PFAdvIntTotal)+Math.round(PFAdvIntSupplTotal)+Math.round(PFAdvIntArrTotal)+Math.round(PFAdvIntForm4DepTotal);	 
	 subGrandGrandTot=Math.round(subGrandTotal)+Math.round(subGrandSupplTotal)+Math.round(subGrandArrTotal)+Math.round(subGrandForm4DepTotal);
	 AddContriGrandTot=Math.round(AddContriNetTotal)+Math.round(AddContriSupplTotal)+Math.round(AddContriArrTotal)+Math.round(AddConrtiForm4DepTotal);
	 PFNetGrandTot=Math.round(PFNetTotal)+Math.round(PFNetSupplTotal)+Math.round(PFNetArrTotal)+Math.round(PFNetForm4DepTotal);
	 totalinspectionchargesgrandtotal=Math.round(inspectionchargesgrandtotal)+Math.round(inspectionchargesgrandSuppltotal)+Math.round(inspectionchargesgrandarrtotal)+Math.round(inspectionchargesgrandForm4Deptotal);
	 PFContGrandTot=Math.round(calcemolumentsGrandTotal)+Math.round(calcemolumentsGrandSupplTotal)+Math.round(calcemolumentsGrandArrTotal)+Math.round(calcemolumentsGrandForm4DepTotal);
	 contGrandGrandTot=Math.round(PFContTotal)+Math.round(PFContSupplTotal)+Math.round(PFContArrTotal)+Math.round(PFContForm4DepTotal);
	 
	%>
 <% if(request.getAttribute("epfForm3supplList")!=null || request.getAttribute("epfForm3ArrList")!=null) {  %>
<tr>
<td>
 <table width="100%" border="1" cellspacing="0" cellpadding="0">
 <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B></B></td>  
        <td class="HighlightData" ><B>Emoluments</B></td>
        <td class="HighlightData"><B>EPF</B></td>
        <td class="HighlightData"><B>VPF</B></td>
        <td class="HighlightData"><B>REFUND_ADV</B></td>
        <td class="HighlightData"><B>INTEREST</B></td>
        <td class="HighlightData"><B>AAI CONT</B></td> 
        <td class="HighlightData"><B>ADDITIONAL CONTRI</B></td>
        <td class="HighlightData"><B>Total Accretion</B></td>     
        <td class="HighlightData"><B>Inspection Charges</B></td>
        <td class="HighlightData"><B>Pension Emoluments </B></td>
        <td class="HighlightData"><B>Pension contri</B></td>
        
      </tr>
 <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3Grand Total</B></td>  
        <td class="NumData" ><B><%=df.format(Math.round(emolumentTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetTotal))%></B></td>  
        <td class="NumData"><B><%=df.format(Math.round(subGrandTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandtotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContTotal))%></B></td>
     </tr>
    <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3SULLGrand Total</B></td>      
         <td class="NumData" ><B><%=df.format(Math.round(emolumentSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetSupplTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(PFNetSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandSupplTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandSuppltotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandSupplTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContSupplTotal))%></B></td>
      </tr>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3ARRGrand Total</B></td>      
         <td class="NumData" ><B><%=df.format(Math.round(emolumentArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetArrTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(AddContriArrTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(subGrandArrTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandarrtotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandArrTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContArrTotal))%></B></td>
      </tr>
      
      
       <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>FORM4-DeputationGrand Total</B></td>      
         <td class="NumData" ><B><%=df.format(Math.round(emolumentForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(EPFForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(VPFForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFAdvIntForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFNetForm4DepTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(AddConrtiForm4DepTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(subGrandForm4DepTotal))%></B></td> 
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandForm4Deptotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(calcemolumentsGrandForm4DepTotal))%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContForm4DepTotal))%></B></td>
      </tr>


<tr>
   <td class="HighlightData" colspan="5" alilgn="right"><B>GRAND TOTALS</B></td>          
    <td class="NumData" ><B><%=df.format(Math.round(emolumentGrandTot))%></B></td>
    <td class="NumData" ><B><%=df.format(Math.round(EPFGrandTot))%></B></td>
    <td class="NumData" ><B><%=df.format(Math.round(VPFGrandTot))%></B></td>    
    <td class="NumData" ><B><%=df.format(Math.round(PFAdvGrandTot))%></B></td>  
    <td class="NumData" ><B><%=df.format(Math.round(PFAdvIntGrandTot))%></B></td>     
    <td class="NumData" ><B><%=df.format(Math.round(PFNetGrandTot))%></B></td>
     <td class="NumData" ><B><%=df.format(Math.round(AddContriGrandTot))%></B></td>
    <td class="NumData" ><B><%=df.format(Math.round(subGrandGrandTot))%></B></td> 
   <td class="NumData" ><B><%=df.format(Math.round(totalinspectionchargesgrandtotal))%></B></td>   
    <td class="NumData" ><B><%=df.format(Math.round(PFContGrandTot))%></B></td>  
    <td class="NumData" ><B><%=df.format(Math.round(contGrandGrandTot))%></B></td>    
     	  
 </tr>
</table>
</td>
</tr>
<%} %>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
