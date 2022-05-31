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
        <td colspan="3" align="center" class="reportlabel">SCHEDULE OF CPF ACCRETION, PENSION CONTRIBUTION & INSPECTION CHARGES  FOR  THE MONTH OF <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
        <%}else{%>
         <td colspan="3" align="center" class="reportlabel">SCHEDULE OF CPF ACCRETION, PENSION CONTRIBUTION & INSPECTION CHARGES <font style="text-decoration: underline"><%=request.getAttribute("dspYear")%></font>(FY<%=request.getAttribute("dspYear")%>.)</td>
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
      double emolumentTotal=0,EPFTotal=0,VPFTotal=0,PFAdvTotal=0,PFAdvIntTotal=0,subGrandTotal=0,PFNetTotal=0,PFContTotal=0,contGrandTotal=0,outstandGrandTotal=0,calcemolumentsGrandTotal=0,inspectionchargesgrandtotal=0,subtotal=0,subsupplitotal=0,subarreartotal=0;
      double emolumentSupplTotal=0,EPFSupplTotal=0,VPFSupplTotal=0,PFAdvSupplTotal=0,PFAdvIntSupplTotal=0,subGrandSupplTotal=0,PFNetSupplTotal=0,PFContSupplTotal=0,contGrandSupplTotal=0,outstandGrandSupplTotal=0,calcemolumentsGrandSupplTotal=0,inspectionchargesgrandSuppltotal=0;     
      double emolumentArrTotal=0,EPFArrTotal=0,VPFArrTotal=0,PFAdvArrTotal=0,PFAdvIntArrTotal=0,subGrandArrTotal=0,PFNetArrTotal=0,PFContArrTotal=0,contGrandArrTotal=0,outstandGrandArrTotal=0,calcemolumentsGrandArrTotal=0,inspectionchargesgrandarrtotal=0,total=0;     
     int totalsupplSubscribers=0,totalpcnoofsupplemployees=0,totalepfSubscribers=0,totalpcnoofepfemployees=0,totalArrSubscribers=0,totalpcnoofArremployees=0;
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
   <td class="reportsublabel" align="left"><u>AAIEPF-3 ACCRETAION:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr >     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="3%" rowspan="2" class="reportsublabel">Region</td>		
		<td width="8%" rowspan="2" class="reportsublabel">UNIT NAME</td>		
	    <td width="8%" rowspan="2" class="reportsublabel">MONTH</td>
	    <td colspan="2"><div align="center" class="reportsublabel">SYNDICATE BANK</div></td>
	    <td colspan="4"><div align="center" class="reportsublabel">HDFC BANK</div></td>
	    </tr>
	    <tr>	 
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>       
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
       <td width="4%"  class="reportsublabel">TOTAL</td>    
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
        <td width="8%"  class="reportsublabel">9 </td>
        <td width="8%"  class="reportsublabel">10=(8+9) </td>       
</tr>
        <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0,rauTotal=0;
     	String tempAccountType="";String tempRegion="";String tempAccount="";String tempRegionType="";
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
     		 if(cardList>0){
     			epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList-1);	 
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();
 				}
		epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList);		
     %>
     <%  
total=Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())+(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018));
%>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	 
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
   <%if(rautotalSubscribers!=0){
      %>
    <tr> 
    <%if(tempAccountType.equals("RAU")){
    srlno++;
    %>
     <td align="right" class="Data" ><%=srlno%></td>
     <td class="Data" nowrap="nowrap"><%=tempRegion.trim()%></td>
      <td class="Data" nowrap="nowrap">RHQ</td>    
    <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
    <td  align="right" class="Data" > <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>        
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri+rauTotalInspectionCharges)%></td>
    </tr>
    <%}}}%> 
    
   </tr> 
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
   tempAccount=epfForm3Bean.getAccountType();
  tempRegionType=epfForm3Bean.getRegion();  
  }
	  if(tempAccount.trim().equals(epfForm3Bean.getAccountType()) || tempRegionType.trim().equals(epfForm3Bean.getRegion())) {
	  	 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
		 rauTotal=rauTotal+total;
	  } 
%>
<%if(!epfForm3Bean.getTotalSubscribers().equals("0") && epfForm3Bean.getAccountType().equals("SAU")){
srlno++;
%>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
     	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()))+Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018))%></td>       
    </tr>
 <%}%>
       	<%	
       	
       	  totalepfSubscribers=totalepfSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
 	 		subGrandTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 		inspectionchargesgrandtotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 		totalpcnoofepfemployees=totalpcnoofepfemployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 		PFContTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			subtotal+=total;
			System.out.println("subarreartotal"+subarreartotal);
	}%>
      <tr>
      <td class="HighlightData" colspan="4" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalepfSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofepfemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandtotal))%></B></td>
        <td class="NumData"><B><%=df.format(subtotal)%></B></td>        
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
   <td class="reportsublabel" align="left"><u>AAIEPF-SUPPL ACCRETAION:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr >     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="3%" rowspan="2" class="reportsublabel">Region</td>		
		<td width="8%" rowspan="2" class="reportsublabel">UNIT NAME</td>		
	    <td width="8%" rowspan="2" class="reportsublabel">MONTH</td>
	    <td colspan="2"><div align="center" class="reportsublabel">SYNDICATE BANK</div></td>
	    <td colspan="4"><div align="center" class="reportsublabel">HDFC BANK</div></td>
	    </tr>
	    <tr>	 
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>       
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
       <td width="4%"  class="reportsublabel">TOTAL</td>    
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
        <td width="8%"  class="reportsublabel">9 </td>
        <td width="8%"  class="reportsublabel">10=(8+9) </td>       
</tr>
        <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0,rauTotal=0;
     	String tempAccountType="";String tempRegion="";String tempAccount="";String tempRegionType="";
     	for(int epfForm3supplList=0;epfForm3supplList<epfForm3supplReportList.size();epfForm3supplList++){
     		 if(epfForm3supplList>0){
     		 epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3supplList-1);
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();
 				}
		epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3supplList);		
     %>
     <%  
total=Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())+(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018));
%>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	 
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
   <%if(rautotalSubscribers!=0){
      %>
    <tr> 
    <%if(tempAccountType.equals("RAU")){
    srlno++;
    %>
     <td align="right" class="Data" ><%=srlno%></td>
     <td class="Data" nowrap="nowrap"><%=tempRegion.trim()%></td>
      <td class="Data" nowrap="nowrap">RHQ</td>    
    <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
    <td  align="right" class="Data" > <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>        
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri+rauTotalInspectionCharges)%></td>
    </tr>
    <%}}}%> 
    
   </tr>  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
   tempAccount=epfForm3Bean.getAccountType();
  tempRegionType=epfForm3Bean.getRegion();  
  }
	  if(tempAccount.trim().equals(epfForm3Bean.getAccountType()) || tempRegionType.trim().equals(epfForm3Bean.getRegion())) {
	  	 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
		 rauTotal=rauTotal+total;
	  } 
%>
<%if(!epfForm3Bean.getTotalSubscribers().equals("0") && epfForm3Bean.getAccountType().equals("SAU")){
srlno++;
%>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
     	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()))+Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018))%></td>       
    </tr>
 <%}%>
       	<%	
       	
       	  totalsupplSubscribers=totalsupplSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
 	 		subGrandSupplTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 		inspectionchargesgrandSuppltotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 		totalpcnoofsupplemployees=totalpcnoofsupplemployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 		PFContSupplTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			subsupplitotal+=total;
	}%>
      <tr>
      <td class="HighlightData" colspan="4" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalsupplSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandSupplTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofsupplemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContSupplTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandSuppltotal))%></B></td>
        <td class="NumData"><B><%=df.format(subsupplitotal)%></B></td>        
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
   <td class="reportsublabel" align="left"><u>AAIEPF-ARREAR ACCRETAION:</u></td>
</tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr >     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="3%" rowspan="2" class="reportsublabel">Region</td>		
		<td width="8%" rowspan="2" class="reportsublabel">UNIT NAME</td>		
	    <td width="8%" rowspan="2" class="reportsublabel">MONTH</td>
	    <td colspan="2"><div align="center" class="reportsublabel">SYNDICATE BANK</div></td>
	    <td colspan="4"><div align="center" class="reportsublabel">HDFC BANK</div></td>
	    </tr>
	    <tr>	 
        <td width="8%" class="reportsublabel">No of subscribers</td>
        <td width="4%"  class="reportsublabel">Total Accretion</td>
        <td width="4%"  class="reportsublabel">No of Subscribers Of Pension </td>
        <td width="4%"  class="reportsublabel">Pension contri </td>       
        <td width="4%"  class="reportsublabel">Inspection Charges</td>
       <td width="4%"  class="reportsublabel">TOTAL</td>    
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
        <td width="8%"  class="reportsublabel">9 </td>
        <td width="8%"  class="reportsublabel">10=(8+9) </td>       
</tr>
        <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	int rautotalSubscribers=0;
     	double rauTotalEmoluments=0,rauTotalEPF=0,rauTotalVPF=0,rauTotalPrinciple=0,rauTotalInterest=0,rauTotalPF=0,rauTotalAccretion=0,rauTotalInspectionCharges=0,rauTotalPcnoofEmp=0,rauTotalPensionEmoluments=0,rauTotalPensionContri=0,rauTotal=0;
     	String tempAccountType="";String tempRegion="";String tempAccount="";String tempRegionType="";
     	for(int epfForm3ArrList=0;epfForm3ArrList<epfForm3ArrReportList.size();epfForm3ArrList++){
     		 if(epfForm3ArrList>0){
     			epfForm3Bean=(AaiEpfform3Bean)epfForm3supplReportList.get(epfForm3ArrList-1);	
     			tempAccountType=epfForm3Bean.getAccountType();
     			tempRegion=epfForm3Bean.getRegion();
 				}
		epfForm3Bean=(AaiEpfform3Bean)epfForm3ArrReportList.get(epfForm3ArrList);	
     %>
     <%  
total=Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())+(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018));
%>
	<%
   if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){
	%>	 
    <%if(!tempAccountType.trim().equals(epfForm3Bean.getAccountType()) || !tempRegion.trim().equals(epfForm3Bean.getRegion())){%>
   <%if(rautotalSubscribers!=0){
      %>
    <tr> 
    <%if(tempAccountType.equals("RAU")){
    srlno++;
    %>
     <td align="right" class="Data" ><%=srlno%></td>
     <td class="Data" nowrap="nowrap"><%=tempRegion.trim()%></td>
      <td class="Data" nowrap="nowrap">RHQ</td>    
    <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
    <td  align="right" class="Data" > <%=rautotalSubscribers%></td>
    <td class="Data" align="right"><%=df.format(rauTotalAccretion)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPcnoofEmp)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri)%></td>        
    <td class="Data" align="right"><%=df.format(rauTotalInspectionCharges)%></td>
    <td class="Data" align="right"><%=df.format(rauTotalPensionContri+rauTotalInspectionCharges)%></td>
    </tr>
    <%}}}%> 
    
   </tr>  
  <%rautotalSubscribers=0;rauTotalEmoluments=0;rauTotalEPF=0;rauTotalVPF=0;rauTotalPrinciple=0;rauTotalInterest=0;rauTotalPF=0;rauTotalAccretion=0;rauTotalInspectionCharges=0;rauTotalPcnoofEmp=0;rauTotalPensionEmoluments=0;rauTotalPensionContri=0;
   tempAccount=epfForm3Bean.getAccountType();
  tempRegionType=epfForm3Bean.getRegion();  
  }
	  if(tempAccount.trim().equals(epfForm3Bean.getAccountType()) || tempRegionType.trim().equals(epfForm3Bean.getRegion())) {
	  	 rautotalSubscribers=rautotalSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
		 rauTotalAccretion=rauTotalAccretion+Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
		 rauTotalInspectionCharges=rauTotalInspectionCharges+Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
		 rauTotalPcnoofEmp=rauTotalPcnoofEmp+epfForm3Bean.getEpf3PcNoofEmployees();
		 rauTotalPensionEmoluments=rauTotalPensionEmoluments+Double.parseDouble(epfForm3Bean.getPensionContriEmoluments());
		 rauTotalPensionContri=rauTotalPensionContri+Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()));
		 rauTotal=rauTotal+total;
	  } 
%>
<%if(!epfForm3Bean.getTotalSubscribers().equals("0") && epfForm3Bean.getAccountType().equals("SAU")){
srlno++;
%>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
     	<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
      	<td  class="NumData"><%=epfForm3Bean.getTotalSubscribers()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=epfForm3Bean.getEpf3PcNoofEmployees()%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution())))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018)%></td>
        <td class="NumData"><%=df.format(Math.round(Double.parseDouble(epfForm3Bean.getPensionContribution()))+Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018))%></td>       
    </tr>
 <%}%>
       	<%	
        	  totalArrSubscribers=totalArrSubscribers+Integer.parseInt(epfForm3Bean.getTotalSubscribers());
 	 		subGrandArrTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 		inspectionchargesgrandarrtotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury())*100/12*0.0018;
	 		totalpcnoofArremployees=totalpcnoofArremployees+epfForm3Bean.getEpf3PcNoofEmployees();
	 		PFContArrTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
			subarreartotal+=total;
			System.out.println("subarreartotal"+subarreartotal);
	}
	
	%>
	
      <tr>
      <td class="HighlightData" colspan="4" alilgn="right"><B>Grand Total</B></td>      
       	<td class="NumData" ><B><%=totalArrSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandArrTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofArremployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContArrTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandarrtotal))%></B></td>
        <td class="NumData"><B><%=subarreartotal%></B></td>        
      </tr>
    </table></td>
  <%}%>
<tr>&nbsp;</tr>
<tr>
&nbsp;
</tr>
<%
double totalinspectionchargesgrandtotal=0,emolumentGrandTot=0,EPFGrandTot=0,VPFGrandTot=0,PFAdvGrandTot=0,PFAdvIntGrandTot=0,subGrandGrandTot=0,PFNetGrandTot=0,PFContGrandTot=0,contGrandGrandTot=0;
   int grandtotalnoofsubscribers=0,grandtotalnoofpensionsubscribers=0 ;
   double grandtotalaccration=0,grandtotalpensioncontri=0,grandtotalinspectioncharges=0,grandtotal=0,garndsubtotal=0;
   grandtotalnoofsubscribers=totalepfSubscribers+totalsupplSubscribers+totalArrSubscribers;
   grandtotalnoofpensionsubscribers=totalpcnoofepfemployees+totalpcnoofsupplemployees+totalpcnoofArremployees;
    grandtotalaccration=Math.round(subGrandTotal)+Math.round(subGrandSupplTotal)+Math.round(subGrandArrTotal);
    grandtotalpensioncontri=Math.round(PFContTotal)+Math.round(PFContSupplTotal)+Math.round(PFContArrTotal);
    grandtotalinspectioncharges=Math.round(inspectionchargesgrandtotal)+Math.round(inspectionchargesgrandSuppltotal)+Math.round(inspectionchargesgrandarrtotal);
     garndsubtotal=Math.round(subtotal)+Math.round(subsupplitotal)+Math.round(subarreartotal);
	 %>
 <% if(request.getAttribute("epfForm3supplList")!=null || request.getAttribute("epfForm3ArrList")!=null) {%>
<tr>
<td>
 <table width="100%" border="1" cellspacing="0" cellpadding="0">
 <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B></B></td> 
       <td class="HighlightData"><B>Total No of subscribers</B></td>  
       <td class="HighlightData"><B>Total Accretion</B></td>  
         <td class="HighlightData"><B>Total No of subscribers of Pension</B></td> 
         <td class="HighlightData"><B>TotalPension contri</B></td>
        <td class="HighlightData"><B>Inspection Charges</B></td>       
        <td class="HighlightData"><B>TOTAL (10=8+9)</B></td>
        
      </tr>
 <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3Grand Total</B></td>
       	<td class="NumData" ><B><%=totalepfSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofepfemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandtotal))%></B></td>
        <td class="NumData"><B><%=df.format(subtotal)%></B></td>        
      
     </tr>
    <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3SULLGrand Total</B></td>      
        <td class="NumData" ><B><%=totalsupplSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandSupplTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofsupplemployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContSupplTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandSuppltotal))%></B></td>
        <td class="NumData"><B><%=df.format(subsupplitotal)%></B></td>  
      </tr>
      <tr>
      <td class="HighlightData" colspan="5" alilgn="right"><B>AAIEPF-3ARRGrand Total</B></td>      
        <td class="NumData" ><B><%=totalArrSubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(subGrandArrTotal))%></B></td>
        <td class="NumData"><B><%=totalpcnoofArremployees%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(PFContArrTotal))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(inspectionchargesgrandarrtotal))%></B></td>
        <td class="NumData"><B><%=df.format(subarreartotal)%></B></td>
      </tr>


<tr>
   <td class="HighlightData" colspan="5" alilgn="right"><B>GRAND TOTALS</B></td>          
     <td class="NumData" ><B><%=grandtotalnoofsubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(grandtotalaccration))%></B></td>
        <td class="NumData"><B><%=grandtotalnoofpensionsubscribers%></B></td>
        <td class="NumData"><B><%=df.format(Math.round(grandtotalpensioncontri))%></B></td>       
        <td class="NumData"><B><%=df.format(Math.round(grandtotalinspectioncharges))%></B></td>
        <td class="NumData"><B><%=df.format(garndsubtotal)%></B></td>
  </tr>
</table>
</td>
</tr>
<%} %>
  <tr>
    <td>&nbsp;</td>
  </tr>
<tr><td>&nbsp;</td></tr>
</table>
</body>
</html>
