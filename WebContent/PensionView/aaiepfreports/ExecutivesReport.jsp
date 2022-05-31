
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
        <td colspan="3" align="center" class="reportlabel">MONTHLY SCHEDULE OF EXECUTIVE ASSESSMENT REPORT FOR THE MONTH <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
   	ArrayList cardReportList=new ArrayList();
    ArrayList missingPFIDsList=new ArrayList();
    ArrayList epfSuppliList_Reg=new ArrayList();
    ArrayList epfSuppliList_Prev=new ArrayList();     
    ArrayList epfArrearList=new ArrayList();
     
   	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="";
  	CommonUtil commonUtil=new CommonUtil();
  	DecimalFormat df = new DecimalFormat("#########0");
  	
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
   </tr>
     
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
       
      </tr>
     
    </table></td>
  </tr>
    <%
 	  double emolumentTotal=0,EPFTotal=0,VPFTotal=0,PFAdvTotal=0,PFAdvIntTotal=0,subGrandTotal=0,PFNetTotal=0,PFContTotal=0,contGrandTotal=0,outstandGrandTotal=0;
 	  double emolumentTotal_suppli_reg=0,EPFTotal_suppli_reg=0,VPFTotal_suppli_reg=0,PFAdvTotal_suppli_reg=0,PFAdvIntTotal_suppli_reg=0,subGrandTotal_suppli_reg=0,PFNetTotal_suppli_reg=0,PFContTotal_suppli_reg=0,contGrandTotal_suppli_reg=0,outstandGrandTotal_suppli_reg=0;
 	  double emolumentTotal_suppli_prev=0,EPFTotal_suppli_prev=0,VPFTotal_suppli_prev=0,PFAdvTotal_suppli_prev=0,PFAdvIntTotal_suppli_prev=0,subGrandTotal_suppli_prev=0,PFNetTotal_suppli_prev=0,PFContTotal_suppli_prev=0,contGrandTotal_suppli_prev=0,outstandGrandTotal_suppli_prev=0;
 	  double emolumentTotal_arrear=0,EPFTotal_arrear=0,VPFTotal_arrear=0,PFAdvTotal_arrear=0,PFAdvIntTotal_arrear=0,subGrandTotal_arrear=0,PFNetTotal_arrear=0,PFContTotal_arrear=0,contGrandTotal_arrear=0,outstandGrandTotal_arrear=0;
      double emolumentTot=0,EPFTot=0,VPFTot=0,PFAdvTot=0,PFAdvIntTot=0,subGrandTot=0,PFNetTot=0,PFContTot=0,contGrandTot=0,outstandGrandTot=0;
     
     double addContri_Total=0,addContri_suppli_reg=0,addContri_suppli_prev=0,addContri_arrear=0;
     
  	 	if(request.getAttribute("cardList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "Executives_Report";
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
        <td width="8%" rowspan="2" class="reportsublabel">Old Emp No</td>
        <td width="8%" rowspan="2" class="reportsublabel">  New Emp No </td>
        <td width="8%" rowspan="2" class="reportsublabel">PF ID </td>
        <td width="8%" rowspan="2" class="reportsublabel">Name </td>
		<td width="8%" rowspan="2" class="reportsublabel">Designation </td>
		<td width="8%" rowspan="2" class="reportsublabel">Level </td>
		<td width="8%" rowspan="2" class="reportsublabel">MONTH Begin Date </td>
		<td width="8%" rowspan="2" class="reportsublabel">MONTH End Date </td>
		<td width="8%" rowspan="2" class="reportsublabel">Discipline </td>
		<td width="8%" rowspan="2" class="reportsublabel">Basic Master </td>
		<td width="8%" rowspan="2" class="reportsublabel">Basic </td>
		<td width="8%" rowspan="2" class="reportsublabel">Prof. Allow. Drawn </td>
		<td width="8%" rowspan="2" class="reportsublabel">PRP PAID </td>
		<td width="8%" rowspan="2" class="reportsublabel">Date of joining </td>
		<td width="8%" rowspan="2" class="reportsublabel">Date of birth</td>
		<td width="8%" rowspan="2" class="reportsublabel">Date of Separation </td>
		<td width="8%" rowspan="2" class="reportsublabel">Date of Promotion </td>
		<td width="8%" rowspan="2" class="reportsublabel">Level on Promotion </td>
		<td width="8%" rowspan="2" class="reportsublabel">No. of EOL </td>
		<td width="8%" rowspan="2" class="reportsublabel">No. of Unauth. Leave </td>
		<td width="8%" rowspan="2" class="reportsublabel">Suspension Begin Date </td>
		<td width="8%" rowspan="2" class="reportsublabel">Suspension End Date </td>
		<td width="8%" rowspan="2" class="reportsublabel">Date of Termination </td>
		<td width="8%" rowspan="2" class="reportsublabel">Profit Center </td>
		<td width="8%" rowspan="2" class="reportsublabel">Profit Center Text </td>
		<td width="8%" rowspan="2" class="reportsublabel">Region </td>
		<td width="8%" rowspan="2" class="reportsublabel">Current Payroll Area </td>
		<td width="8%" rowspan="2" class="reportsublabel">Action Type </td>
		<td width="8%" rowspan="2" class="reportsublabel">Reason for Action </td>
       	<td width="8%" rowspan="2" class="reportsublabel">Emoluments </td>
        <td width="8%" rowspan="2" class="reportsublabel">Basicdarete </td>
		<td width="8%" rowspan="2" class="reportsublabel">Darate </td>
		
		
		
		
		
		
     </tr>
  
     <tr></tr>
     <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0;
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
		epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList);
		srlno++;
		if(epfForm3Bean.getArrearflag().equals("Y")){
	 %>
	 
	 
      <tr bgcolor="orange">
        <td  class="NumData"><%=srlno%></td>
         <% if(epfForm3Bean.getEmployeeNo()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmployeeNo() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
         <% if(epfForm3Bean.getEmpCode()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmpCode() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
      
      <td class="Data"><%=epfForm3Bean.getPfID()%></td>
        <td class="Data"><%=epfForm3Bean.getEmployeeName() %></td>
        
        <% if(epfForm3Bean.getCurrentDesg()!=""){%>
         <td class="Data"><%=epfForm3Bean.getCurrentDesg() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
            <% if(epfForm3Bean.getEmplevel()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmplevel() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
          <td class="Data"><%=epfForm3Bean.getMonthyear() %></td>
           <td class="Data">&nbsp;</td>
           
           <% if(epfForm3Bean.getDiscipline()!=""){%>
         <td class="Data"><%=epfForm3Bean.getDiscipline() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
          <td class="Data">&nbsp;</td>
          <td class="NumData"><%=epfForm3Bean.getBasic()%></td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data"><%=epfForm3Bean.getDateofJoining() %></td>
          
        <td class="Data"><%=epfForm3Bean.getDateOfBirth()%></td>
        
        <% if(epfForm3Bean.getSeperationDate()!=""){%>
         <td class="Data"><%=epfForm3Bean.getSeperationDate() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        
        <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
        
         <% if(epfForm3Bean.getStation()!=""){%>
         <td class="Data"><%=epfForm3Bean.getStation() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        
         <% if(epfForm3Bean.getRegion()!=""){%>
         <td class="Data"><%=epfForm3Bean.getRegion() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          
        <% if(epfForm3Bean.getSeperationreason()!=""){%>
         <td class="Data"><%=epfForm3Bean.getSeperationreason() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
         
     
      
        <td class="NumData"><%=epfForm3Bean.getEmoluments()%></td>
        <td class="NumData"><%=epfForm3Bean.getBasicRate()%></td>
        <td class="NumData"><%=epfForm3Bean.getDarate() %></td>
         
         
      
        
        
         
         
      
        
        
     
     
        
      </tr>
      
      <% }else{%>
            <tr >
        <td  class="NumData"><%=srlno%></td>
         <% if(epfForm3Bean.getEmployeeNo()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmployeeNo() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
         <% if(epfForm3Bean.getEmpCode()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmpCode() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
      
      <td class="Data"><%=epfForm3Bean.getPfID()%></td>
        <td class="Data"><%=epfForm3Bean.getEmployeeName() %></td>
        
        <% if(epfForm3Bean.getCurrentDesg()!=""){%>
         <td class="Data"><%=epfForm3Bean.getCurrentDesg() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
            <% if(epfForm3Bean.getEmplevel()!=""){%>
         <td class="Data"><%=epfForm3Bean.getEmplevel() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
          <td class="Data"><%=epfForm3Bean.getMonthyear() %></td>
           <td class="Data">&nbsp;</td>
           
           <% if(epfForm3Bean.getDiscipline()!=""){%>
         <td class="Data"><%=epfForm3Bean.getDiscipline() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
          <td class="Data">&nbsp;</td>
          <td class="NumData"><%=epfForm3Bean.getBasic()%></td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data"><%=epfForm3Bean.getDateofJoining() %></td>
          
        <td class="Data"><%=epfForm3Bean.getDateOfBirth()%></td>
        
        <% if(epfForm3Bean.getSeperationDate()!=""){%>
         <td class="Data"><%=epfForm3Bean.getSeperationDate() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        
        <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
        
         <% if(epfForm3Bean.getStation()!=""){%>
         <td class="Data"><%=epfForm3Bean.getStation() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        
         <% if(epfForm3Bean.getRegion()!=""){%>
         <td class="Data"><%=epfForm3Bean.getRegion() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
        <td class="Data">&nbsp;</td>
          <td class="Data">&nbsp;</td>
          
        <% if(epfForm3Bean.getSeperationreason()!=""){%>
         <td class="Data"><%=epfForm3Bean.getSeperationreason() %></td>
         <%}else{ %>
        <td class="Data">&nbsp;</td>
        <%} %>
         
     
      
        <td class="NumData"><%=epfForm3Bean.getEmoluments()%></td>
        <td class="NumData"><%=epfForm3Bean.getBasicRate()%></td>
        <td class="NumData"><%=epfForm3Bean.getDarate() %></td>
         
         
      
        
        
         
         
      
        
        
     
     
        
      </tr>
      <%} %>
	
   <% }} %> 
    
	 
 

</body>
</html>
