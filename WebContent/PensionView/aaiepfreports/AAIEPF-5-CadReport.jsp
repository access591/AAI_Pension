
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
      
        <td colspan="3" align="center" class="reportlabel"> MONTHLY SCHEDULE OF EMPLOYEE-WISE AAIEPF form 5-CAD CPF  RECOVERY FOR THE MONTH <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font></td>
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
        <td width="73%">&nbsp;</td>
        <td width="27%" class="reportsublabel">Total No of subscribers<font style="text-decoration: underline"> <%=request.getAttribute("totalSub")%></font></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="reportsublabel">Statutory Rate PF Contribution 12.00%</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="reportsublabel">Statutory Rate pension Contribution 8.33%</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="9%" class="reportsublabel">Unit Name:</td>
        <td width="91%"><font style="text-decoration: underline"><%=stationName%></font></td>
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
				
					filePrefix = "AAI_EPF-3_Report";
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
        <td width="8%" rowspan="2" class="reportsublabel">PF ID </td>
		<td width="8%" rowspan="2" class="reportsublabel" nowrap="nowrap">CPF A/c No<br/> (Old)</td>
        <td width="8%" rowspan="2" class="reportsublabel">Emp No</td>
        <td width="12%" rowspan="2" class="reportsublabel">Name of the Member </td>
        <td width="8%" rowspan="2" class="reportsublabel">Desig</td>
		<td width="12%" rowspan="2" class="reportsublabel">Father's Name/ Husband name <br/>(in case of Married women)</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of birth</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of Joining</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Month <br/>mm-yyyy</td>
        <td width="4%" rowspan="2" class="reportsublabel">Salary drawn/ paid for<br/> CPF deduction Rs</td>
        <td colspan="6" align="center" class="reportsublabel">SUBSCRIPTION</td>
        <td colspan="3" align="center" class="reportsublabel">Employer contribution </td>
        <td align="center" rowspan="2" class="reportsublabel">Station</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
       <td width="6%"  class="reportsublabel">1.16%(Diverted from EPF towards PC)</td>   
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (11 to 15)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (16+17)</td>
       
		
     </tr>
  
     <tr></tr>
     <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0;
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
		epfForm3Bean=(AaiEpfform3Bean)cardReportList.get(cardList);
		srlno++;
		diff=Double.parseDouble(epfForm3Bean.getFormDifference());
		
		
     %>
      <tr <%=epfForm3Bean.getHighlightedColor()%>>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data"><%=epfForm3Bean.getPfID()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getCpfaccno()%></td>
        <td class="Data"><%=epfForm3Bean.getEmployeeNo()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getDesignation()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getFhName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getDateOfBirth()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getDateofJoining()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getMonthyear()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmppfstatury()))%></td>
        
          <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getAdditionalContri()))%></td>
        
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getSubscriptionTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContribution()))%></td><!--
        
        
        <%if(diff!=0){ 
          epfForm3Bean.setContributionTotal(Double.toString(Double.parseDouble(epfForm3Bean.getContributionTotal())+diff));
          
          }%>
        
        
        --><td class="NumData"><%=df.format(Double.parseDouble(epfForm3Bean.getContributionTotal()))%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
        
        <%if(diff!=0){ %>
         <td class="Data" nowrap="nowrap"><%=diff%></td>
        <%}else{ %>
        <td class="Data">---</td>
        <%}%>
      </tr>
	<%
	
	 emolumentTotal+=Double.parseDouble(epfForm3Bean.getEmoluments());
	 EPFTotal+=Double.parseDouble(epfForm3Bean.getEmppfstatury());
	 
	 addContri_Total+=Double.parseDouble(epfForm3Bean.getAdditionalContri());
	 
	 VPFTotal+=Double.parseDouble(epfForm3Bean.getEmpvpf());
	 PFAdvTotal+=Double.parseDouble(epfForm3Bean.getPrincipal());
	 PFAdvIntTotal+=Double.parseDouble(epfForm3Bean.getInterest());	 
	 subGrandTotal+=Double.parseDouble(epfForm3Bean.getSubscriptionTotal());
	 PFNetTotal+=Double.parseDouble(epfForm3Bean.getPf());
	 PFContTotal+=Double.parseDouble(epfForm3Bean.getPensionContribution());
	 contGrandTotal+=Double.parseDouble(epfForm3Bean.getContributionTotal());
	 grandDiffe+=diff;   
	
	}%>
	
    <tr>
    <td class="Data" colspan="10" align="right">Grand Total</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal))%></td>         
    <td class="Data"><%=df.format(Math.round(EPFTotal))%></td>
      
      <td class="Data"><%=df.format(Math.round(addContri_Total))%></td>
      
    <td class="Data"><%=df.format(Math.round(VPFTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal))%></td>    
    <td class="Data" >&nbsp;</td> 
    
    <td class="Data" ><%=Math.round(grandDiffe)%></td>     
   
    
    </tr>
    
	 <tr>
	  	<td colspan="22" class="Data">&nbsp;</td>
  	</tr>

    <%
    
    if(request.getAttribute("missingPFIDsList")!=null){
     	AaiEpfform3Bean missingPFIDsBean=new AaiEpfform3Bean();
     	int missSrlno=0;
     	missingPFIDsList=(ArrayList)request.getAttribute("missingPFIDsList");
     	if(missingPFIDsList.size()!=0){%>
     	  	 <tr>
  		<td colspan="20" class="Data">Missing PFID's</td>
	</tr>
    <%for(int missingPFIDs=0;missingPFIDs<missingPFIDsList.size();missingPFIDs++){
		missingPFIDsBean=(AaiEpfform3Bean)missingPFIDsList.get(missingPFIDs);
		missSrlno++;
     %>
      <tr>
        <td  class="NumData"><%=missSrlno%></td>
        <td class="Data"><%=missingPFIDsBean.getPfID()%></td>
        <td class="Data" nowrap="nowrap"><%=missingPFIDsBean.getCpfaccno()%></td>
        <td class="Data"><%=missingPFIDsBean.getEmployeeNo()%></td>
        <td class="Data" nowrap="nowrap"><%=missingPFIDsBean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=missingPFIDsBean.getDesignation()%></td>
        <td class="Data" nowrap="nowrap">---</td>
        <td class="Data" nowrap="nowrap">---</td>
        <td class="Data" nowrap="nowrap"><%=missingPFIDsBean.getMonthyear()%></td>
        <td class="NumData"><%=missingPFIDsBean.getEmoluments()%></td>
        <td class="NumData"><%=missingPFIDsBean.getEmppfstatury()%></td>
        <td class="NumData"><%=missingPFIDsBean.getEmpvpf()%></td>
        <td class="NumData"><%=missingPFIDsBean.getPrincipal()%></td>
        <td class="NumData"><%=missingPFIDsBean.getInterest()%></td>
        <td class="NumData"><%=missingPFIDsBean.getSubscriptionTotal()%></td>
        <td class="NumData"><%=missingPFIDsBean.getPf()%></td>
        <td class="NumData"><%=missingPFIDsBean.getPensionContribution()%></td>
        <td class="NumData"><%=missingPFIDsBean.getContributionTotal()%></td>
        <td class="Data" nowrap="nowrap"><%=missingPFIDsBean.getStation()%></td>
        <td class="Data">---</td>
      </tr>
      
      
	<%
	 emolumentTot+=Double.parseDouble(missingPFIDsBean.getEmoluments());
	 EPFTot+=Double.parseDouble(missingPFIDsBean.getEmppfstatury());
	 VPFTot+=Double.parseDouble(missingPFIDsBean.getEmpvpf());
	 PFAdvTot+=Double.parseDouble(missingPFIDsBean.getPrincipal());
	 PFAdvIntTot+=Double.parseDouble(missingPFIDsBean.getInterest());	 
	 subGrandTot+=Double.parseDouble(missingPFIDsBean.getSubscriptionTotal());
	 PFNetTot+=Double.parseDouble(missingPFIDsBean.getPf());
	 PFContTot+=Double.parseDouble(missingPFIDsBean.getPensionContribution());
	 contGrandTot+=Double.parseDouble(missingPFIDsBean.getContributionTotal());	    
	
	}%>
	
	 <tr>
    <td class="Data" colspan="10" align="right">Grand Total</td>            
    <td class="Data"><%=Math.round(emolumentTot)%></td>
    <td class="Data"><%=Math.round(EPFTot)%></td>
    <td class="Data"><%=Math.round(VPFTot)%></td>  
    
    <td class="Data"><%=Math.round(PFAdvTot)%></td>  
    <td class="Data"><%=Math.round(PFAdvIntTot)%></td>  
    <td class="Data"><%=Math.round(subGrandTot)%></td>  
    <td class="Data"><%=Math.round(PFNetTot)%></td>  
    <td class="Data"><%=Math.round(PFContTot)%></td>  
    <td class="Data"><%=Math.round(contGrandTot)%></td>     
   
    
    </tr>
    
    
    
    <%
    	System.out.println("==============================================================");
    	System.out.println("=====emolumentTot"+emolumentTot+"======EPFTot"+EPFTot);
    	System.out.println("=====VPFTot"+VPFTot+"======PFAdvTot"+PFAdvTot);
    	System.out.println("=====PFAdvIntTot"+PFAdvIntTot+"======subGrandTot"+subGrandTot);
    	System.out.println("=====PFNetTot"+PFNetTot+"======PFContTot"+PFContTot);
    	System.out.println("=====contGrandTot"+contGrandTot);
    	System.out.println("==============================================================");
    %>
	 <% }%>

    </table></td>
  </tr>
  <%}%>
  
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

<!--  New --->
   
 <table width="100%" border="1" cellspacing="0" cellpadding="0">  
    <%if((request.getAttribute("suppliList_reg")!=null) || (request.getAttribute("suppliList_prev")!=null)){
   epfSuppliList_Reg=(ArrayList)request.getAttribute("suppliList_reg");
   epfSuppliList_Prev=(ArrayList)request.getAttribute("suppliList_prev");
  if(epfSuppliList_Reg.size()!=0 || epfSuppliList_Prev.size()!=0){
    %>
    <tr>
  		<td colspan="23"   class="reportsublabel">Supplimentary Details </td>
	</tr>
    <% 	if(epfSuppliList_Reg.size()!=0){ %>
     	  	 <tr>
  		<td colspan="23"   class="reportsublabel">Regular Salary  Submitted as Supplimentary(Block I)</td>
	</tr>
    
      
      
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="8%" rowspan="2" class="reportsublabel">PF ID </td>
		<td width="8%" rowspan="2" class="reportsublabel" nowrap="nowrap">CPF A/c No<br/> (Old)</td>
        <td width="8%" rowspan="2" class="reportsublabel">Emp No</td>
        <td width="12%" rowspan="2" class="reportsublabel">Name of the Member </td>
        <td width="8%" rowspan="2" class="reportsublabel">Desig</td>
		<td width="12%" rowspan="2" class="reportsublabel">Father's Name/ Husband name <br/>(in case of Married women)</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of birth</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of Joining</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Month <br/>mm-yyyy</td>
        <td width="4%" rowspan="2" class="reportsublabel">Salary drawn/ paid for<br/> CPF deduction Rs</td>
        <td colspan="6" align="center" class="reportsublabel">SUBSCRIPTION</td>
        <td colspan="3" align="center" class="reportsublabel">Employer contribution </td>
        <td align="center" rowspan="2" class="reportsublabel">Station</td>
        <td align="center" rowspan="2" class="reportsublabel">Uploaded Date</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">1.16%(Diverted from EPF towards PC)</td> 
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (11 to 15)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (16+17)</td>
       
		
     </tr>
     <%}%>
     
      <% 
       if(request.getAttribute("suppliList_reg")!=null){
     	AaiEpfform3Bean epfSuppliBean=new AaiEpfform3Bean();
     	int suppliSrlno=0;
     	epfSuppliList_Reg=(ArrayList)request.getAttribute("suppliList_reg");
     	if(epfSuppliList_Reg.size()!=0){
     	
      for(int epfSuppli=0;epfSuppli<epfSuppliList_Reg.size();epfSuppli++){
		epfSuppliBean=(AaiEpfform3Bean)epfSuppliList_Reg.get(epfSuppli);
		suppliSrlno++;
     %>
      <tr>
        <td  width="6%"  class="NumData"><%=suppliSrlno%></td>
        <td	 width="8%"   class="Data"><%=epfSuppliBean.getPfID()%></td>
        <td  width="8%"   class="Data" nowrap="nowrap"><%=epfSuppliBean.getCpfaccno()%></td>
        <td  width="8%"  class="Data"><%=epfSuppliBean.getEmployeeNo()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getEmployeeName()%></td>
        <td width="8%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getDesignation()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getFhName()%></td>
        <td width="4%" class="Data" nowrap="nowrap"><%=epfSuppliBean.getDateOfBirth()%></td>
         <td width="4%" class="Data" nowrap="nowrap"><%=epfSuppliBean.getDateofJoining()%></td>
        <td width="4%"   class="Data" nowrap="nowrap"><%=epfSuppliBean.getMonthyear()%></td>
        <td width="4%"   class="NumData"><%=epfSuppliBean.getEmoluments()%></td>
        
        <td width="6%"  class="NumData"><%=epfSuppliBean.getEmppfstatury()%></td>
        <td width="6%"  class="NumData"><%=epfSuppliBean.getAdditionalContri()%></td>
        <td width="6%"  class="NumData"><%=epfSuppliBean.getEmpvpf()%></td>
        <td width="8%"  class="NumData"><%=epfSuppliBean.getPrincipal()%></td>
        <td width="8%"  class="NumData"><%=epfSuppliBean.getInterest()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getSubscriptionTotal()%></td>
        <td width="5%"  class="NumData"><%=epfSuppliBean.getPf()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getPensionContribution()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getContributionTotal()%></td>
        <td width="4%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getStation()%></td>
                <% if(!epfSuppliBean.getUploaddate().equals("")){%>
        <td width="4%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getUploaddate()%></td>
        <%}else{%>
        <td width="4%"  class="Data" nowrap="nowrap">---</td>
        <%}%>
        <td width="9%" class="Data" nowrap="nowrap">---</td>
      </tr>  
      
        
	<%
	  
	 emolumentTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getEmoluments());
	 EPFTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getEmppfstatury());
	 addContri_suppli_reg+=Double.parseDouble(epfSuppliBean.getAdditionalContri());
	 VPFTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getEmpvpf());
	 PFAdvTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getPrincipal());
	 PFAdvIntTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getInterest());	 
	 subGrandTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getSubscriptionTotal());
	 PFNetTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getPf());
	 PFContTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getPensionContribution());
	 contGrandTotal_suppli_reg+=Double.parseDouble(epfSuppliBean.getContributionTotal());
	 
	}%>
	<tr>
    <td class="Data" colspan="10" align="right">Block I Grand Total</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_suppli_reg))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_suppli_reg))%></td>
    <td class="Data"><%=df.format(Math.round(addContri_suppli_reg))%></td>
    <td class="Data"><%=df.format(Math.round(addContri_suppli_reg))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_suppli_reg))%></td>     
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_suppli_reg))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_suppli_reg))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_suppli_reg))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_suppli_reg))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_suppli_reg))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_suppli_reg))%></td>   
    <td class="Data" colspan="3" >&nbsp;</td> 
	  
     </tr>
     	  <%}%>
 
  <%}%>
  <% if(epfSuppliList_Prev.size()!=0){ %>
     	  	 <tr>
  		<td colspan="23"   class="reportsublabel">Previous Month Salary Submitted as Supplimentary Data(Block II)</td>
	</tr>
    
      
      
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="8%" rowspan="2" class="reportsublabel">PF ID </td>
		<td width="8%" rowspan="2" class="reportsublabel" nowrap="nowrap">CPF A/c No<br/> (Old)</td>
        <td width="8%" rowspan="2" class="reportsublabel">Emp No</td>
        <td width="12%" rowspan="2" class="reportsublabel">Name of the Member </td>
        <td width="8%" rowspan="2" class="reportsublabel">Desig</td>
		<td width="12%" rowspan="2" class="reportsublabel">Father's Name/ Husband name <br/>(in case of Married women)</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of birth</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of Joining</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Month <br/>mm-yyyy</td>
        <td width="4%" rowspan="2" class="reportsublabel">Salary drawn/ paid for<br/> CPF deduction Rs</td>
        <td colspan="6" align="center" class="reportsublabel">SUBSCRIPTION</td>
        <td colspan="3" align="center" class="reportsublabel">Employer contribution </td>
        <td align="center" rowspan="2" class="reportsublabel">Station</td>
        <td align="center" rowspan="2" class="reportsublabel">Uploaded Date</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
         <td width="6%"  class="reportsublabel">1.16%(Diverted from EPF towards PC)</td> 
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (11 to 15)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (16+17)</td>
       
		
     </tr>
     <%}%>
     
      <% 
       if(request.getAttribute("suppliList_prev")!=null){
     	AaiEpfform3Bean epfSuppliBean=new AaiEpfform3Bean();
     	int suppliSrlno=0;
     	epfSuppliList_Prev=(ArrayList)request.getAttribute("suppliList_prev");
     	if(epfSuppliList_Prev.size()!=0){
     	
      for(int epfSuppli=0;epfSuppli<epfSuppliList_Prev.size();epfSuppli++){
		epfSuppliBean=(AaiEpfform3Bean)epfSuppliList_Prev.get(epfSuppli);
		suppliSrlno++;
     %>
      <tr>
        <td  width="6%"  class="NumData"><%=suppliSrlno%></td>
        <td	 width="8%"   class="Data"><%=epfSuppliBean.getPfID()%></td>
        <td  width="8%"   class="Data" nowrap="nowrap"><%=epfSuppliBean.getCpfaccno()%></td>
        <td  width="8%"  class="Data"><%=epfSuppliBean.getEmployeeNo()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getEmployeeName()%></td>
        <td width="8%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getDesignation()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getFhName()%></td>
        <td width="4%" class="Data" nowrap="nowrap"><%=epfSuppliBean.getDateOfBirth()%></td>
        <td width="4%" class="Data" nowrap="nowrap"><%=epfSuppliBean.getDateofJoining()%></td>
        <td width="4%"   class="Data" nowrap="nowrap"><%=epfSuppliBean.getMonthyear()%></td>
        <td width="4%"   class="NumData"><%=epfSuppliBean.getEmoluments()%></td>
        
        <td width="6%"  class="NumData"><%=epfSuppliBean.getEmppfstatury()%></td>
        <td width="6%"  class="NumData"><%=epfSuppliBean.getAdditionalContri()%></td>
        <td width="6%"  class="NumData"><%=epfSuppliBean.getEmpvpf()%></td>
        <td width="8%"  class="NumData"><%=epfSuppliBean.getPrincipal()%></td>
        <td width="8%"  class="NumData"><%=epfSuppliBean.getInterest()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getSubscriptionTotal()%></td>
        <td width="5%"  class="NumData"><%=epfSuppliBean.getPf()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getPensionContribution()%></td>
        <td width="4%"  class="NumData"><%=epfSuppliBean.getContributionTotal()%></td>
        <td width="4%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getStation()%></td>
                <% if(!epfSuppliBean.getUploaddate().equals("")){%>
        <td width="4%"  class="Data" nowrap="nowrap"><%=epfSuppliBean.getUploaddate()%></td>
        <%}else{%>
        <td width="4%"  class="Data" nowrap="nowrap">---</td>
        <%}%>
        <td width="9%" class="Data" nowrap="nowrap">---</td>
      </tr>  
      
        
	<%
	  
	 emolumentTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getEmoluments());
	 EPFTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getEmppfstatury());
	 addContri_suppli_prev+=Double.parseDouble(epfSuppliBean.getAdditionalContri());
	 VPFTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getEmpvpf());
	 PFAdvTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getPrincipal());
	 PFAdvIntTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getInterest());	 
	 subGrandTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getSubscriptionTotal());
	 PFNetTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getPf());
	 PFContTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getPensionContribution());
	 contGrandTotal_suppli_prev+=Double.parseDouble(epfSuppliBean.getContributionTotal());
	 
	}%>
	<tr>
    <td class="Data" colspan="10" align="right">Block II Grand Total</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(addContri_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_suppli_prev))%></td>     
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_suppli_prev))%></td>    
    <td class="Data" colspan="3" >&nbsp;</td> 
	  
     </tr>
     	  <%}%>
 
  <%}%>
  	 <tr>
  		<td colspan="23"   class="reportsublabel">&nbsp;</td>
	</tr>
  <tr>
    <td class="Data" colspan="10" align="right">Grand Total</td>                 
    <td class="Data"><%=df.format(Math.round(emolumentTotal_suppli_reg)+Math.round(emolumentTotal_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_suppli_reg)+Math.round(EPFTotal_suppli_prev))%></td>
   
    <td class="Data"><%=df.format(Math.round(addContri_suppli_reg)+Math.round(addContri_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_suppli_reg)+Math.round(VPFTotal_suppli_prev))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_suppli_reg)+Math.round(PFAdvTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_suppli_reg)+Math.round(PFAdvIntTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_suppli_reg)+Math.round(subGrandTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_suppli_reg)+Math.round(PFNetTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_suppli_reg)+Math.round(PFContTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_suppli_reg)+Math.round(contGrandTotal_suppli_prev))%></td>       
    <td class="Data" colspan="3" >&nbsp;</td> 
	  
     </tr>
  
  
  <% }}%>
 </table>
 <table>
 <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
 </table>
 <!--  Arrear Block-->
 
  
 <table width="100%" border="1" cellspacing="0" cellpadding="0">  
   
    <%
     
    if(request.getAttribute("arrearList")!=null){
     	AaiEpfform3Bean epfArrearBean=new AaiEpfform3Bean();
     	int arrearSrlno=0;
     	epfArrearList=(ArrayList)request.getAttribute("arrearList");
     	if(epfArrearList.size()!=0){ 
     	 %>
     	  	 <tr>
  		<td colspan="22"   class="reportsublabel">Arrear Details</td>
	</tr>
     
      <tr>
     
        <td width="3%" rowspan="2" class="reportsublabel">Sl.No</td>
        <td width="8%" rowspan="2" class="reportsublabel">PF ID </td>
		<td width="8%" rowspan="2" class="reportsublabel" nowrap="nowrap">CPF A/c No<br/> (Old)</td>
        <td width="8%" rowspan="2" class="reportsublabel">Emp No</td>
        <td width="12%" rowspan="2" class="reportsublabel">Name of the Member </td>
        <td width="8%" rowspan="2" class="reportsublabel">Desig</td>
		<td width="12%" rowspan="2" class="reportsublabel">Father's Name/ Husband name <br/>(in case of Married women)</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of birth</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Date of Joining</td>
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Month <br/>mm-yyyy</td>
        <td width="4%" rowspan="2" class="reportsublabel">Salary drawn/ paid for<br/> CPF deduction Rs</td>
        <td colspan="6" align="center" class="reportsublabel">SUBSCRIPTION</td>
        <td colspan="3" align="center" class="reportsublabel">Employer contribution </td>
        <td align="center" rowspan="2" class="reportsublabel">Station</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">1.16%(Diverted from EPF towards PC)</td> 
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="4%"  class="reportsublabel">Total <br/> (11 to 14)</td>
        <td width="5%"  class="reportsublabel">PF (Net)</td>
        <td width="4%"  class="reportsublabel">Pension contri. </td>
        <td width="4%"  class="reportsublabel">Total Rs. (16+17)</td>
       
		
     </tr>
     <%}}%>
      <%
      if(request.getAttribute("arrearList")!=null){
     	AaiEpfform3Bean epfArrearBean=new AaiEpfform3Bean();
     	int arrearSrlno=0;
     	epfArrearList=(ArrayList)request.getAttribute("arrearList");
     	if(epfArrearList.size()!=0){ 
      
      for(int epfArrear=0;epfArrear<epfArrearList.size();epfArrear++){
		epfArrearBean=(AaiEpfform3Bean)epfArrearList.get(epfArrear);
		arrearSrlno++;
     %>
      <tr>
        <td  width="6%"  class="NumData"><%=arrearSrlno%></td>
        <td	 width="8%"   class="Data"><%=epfArrearBean.getPfID()%></td>
        <td  width="8%"   class="Data" nowrap="nowrap"><%=epfArrearBean.getCpfaccno()%></td>
        <td  width="8%"  class="Data"><%=epfArrearBean.getEmployeeNo()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfArrearBean.getEmployeeName()%></td>
        <td width="8%"  class="Data" nowrap="nowrap"><%=epfArrearBean.getDesignation()%></td>
        <td width="12%"  class="Data" nowrap="nowrap"><%=epfArrearBean.getFhName()%></td>
        <td width="4%" class="Data" nowrap="nowrap"><%=epfArrearBean.getDateOfBirth()%></td>
        <td width="4%" class="Data" nowrap="nowrap"><%=epfArrearBean.getDateofJoining()%></td>
        <td width="4%"   class="Data" nowrap="nowrap"><%=epfArrearBean.getMonthyear()%></td>
        <td width="4%"   class="NumData"><%=epfArrearBean.getEmoluments()%></td>        
        <td width="6%"  class="NumData"><%=epfArrearBean.getEmppfstatury()%></td>
        <td width="6%"  class="NumData"><%=epfArrearBean.getAdditionalContri()%></td>
        <td width="6%"  class="NumData"><%=epfArrearBean.getEmpvpf()%></td>
        <td width="8%"  class="NumData"><%=epfArrearBean.getPrincipal()%></td>
        <td width="8%"  class="NumData"><%=epfArrearBean.getInterest()%></td>
        <td width="4%"  class="NumData"><%=epfArrearBean.getSubscriptionTotal()%></td>
        <td width="5%"  class="NumData"><%=epfArrearBean.getPf()%></td>
        <td width="4%"  class="NumData"><%=epfArrearBean.getPensionContribution()%></td>
        <td width="4%"  class="NumData"><%=epfArrearBean.getContributionTotal()%></td>
        <td width="4%"  class="Data" nowrap="nowrap"><%=epfArrearBean.getStation()%></td>
        <td width="9%" class="Data" nowrap="nowrap">---</td>
      </tr>  
      
        
	<%
	  
	 emolumentTotal_arrear+=Double.parseDouble(epfArrearBean.getEmoluments());
	 EPFTotal_arrear+=Double.parseDouble(epfArrearBean.getEmppfstatury());
	 addContri_arrear+=Double.parseDouble(epfArrearBean.getAdditionalContri());
	 VPFTotal_arrear+=Double.parseDouble(epfArrearBean.getEmpvpf());
	 PFAdvTotal_arrear+=Double.parseDouble(epfArrearBean.getPrincipal());
	 PFAdvIntTotal_arrear+=Double.parseDouble(epfArrearBean.getInterest());	 
	 subGrandTotal_arrear+=Double.parseDouble(epfArrearBean.getSubscriptionTotal());
	 PFNetTotal_arrear+=Double.parseDouble(epfArrearBean.getPf());
	 PFContTotal_arrear+=Double.parseDouble(epfArrearBean.getPensionContribution());
	 contGrandTotal_arrear+=Double.parseDouble(epfArrearBean.getContributionTotal());
	 
	}%>
	<tr>
    <td class="Data" colspan="10" align="right">Grand Total</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_arrear))%></td>   
    <td class="Data"><%=df.format(Math.round(EPFTotal_arrear))%></td>
     <td class="Data"><%=df.format(Math.round(addContri_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_arrear))%></td>     
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_arrear))%></td>    
    <td class="Data" >&nbsp;</td>
     <td class="Data" >&nbsp;</td>  
	  
     </tr>
     	  <%}%>
 
  <%}%>
  
 </table>
 <!-- - --->
 <table>
 <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
   
 </table> 
 <%
	  double emolumentGrandTot=0,EPFGrandTot=0,VPFGrandTot=0,PFAdvGrandTot=0,PFAdvIntGrandTot=0,subGrandGrandTot=0,PFNetGrandTot=0,PFContGrandTot=0,contGrandGrandTot=0,addContriGrandTot=0;
	 emolumentGrandTot=Math.round(emolumentTotal)+Math.round(emolumentTotal_suppli_reg)+Math.round(emolumentTotal_suppli_prev)+Math.round(emolumentTotal_arrear);
	 EPFGrandTot=Math.round(EPFTotal)+Math.round(EPFTotal_suppli_reg)+Math.round(EPFTotal_suppli_prev)+Math.round(EPFTotal_arrear);
	 addContriGrandTot=Math.round(addContri_Total)+Math.round(addContri_suppli_reg)+Math.round(addContri_suppli_prev)+Math.round(addContri_arrear);
	 VPFGrandTot=Math.round(VPFTotal)+Math.round(VPFTotal_suppli_reg)+Math.round(VPFTotal_suppli_prev)+Math.round(VPFTotal_arrear);
	 PFAdvGrandTot=Math.round(PFAdvTotal)+Math.round(PFAdvTotal_suppli_reg)+Math.round(PFAdvTotal_suppli_prev)+Math.round(PFAdvTotal_arrear);
	 PFAdvIntGrandTot=Math.round(PFAdvIntTotal)+Math.round(PFAdvIntTotal_suppli_reg)+Math.round(PFAdvIntTotal_suppli_prev)+Math.round(PFAdvIntTotal_arrear);	 
	 subGrandGrandTot=Math.round(subGrandTotal)+Math.round(subGrandTotal_suppli_reg)+Math.round(subGrandTotal_suppli_prev)+Math.round(subGrandTotal_arrear);
	 PFNetGrandTot=Math.round(PFNetTotal)+Math.round(PFNetTotal_suppli_reg)+Math.round(PFNetTotal_suppli_prev)+Math.round(PFNetTotal_arrear);
	 PFContGrandTot=Math.round(PFContTotal)+Math.round(PFContTotal_suppli_reg)+Math.round(PFContTotal_suppli_prev)+Math.round(PFContTotal_arrear);
	 contGrandGrandTot=Math.round(contGrandTotal)+Math.round(contGrandTotal_suppli_reg)+Math.round(contGrandTotal_suppli_prev)+Math.round(contGrandTotal_arrear);
	 
	%>
 
 <table width="100%" border="1" cellspacing="0" cellpadding="0">
 
 <tr>
    <td  class="reportsublabel" colspan="11" align="right">CPF Recoveries Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal))%></td>
    
    <td class="Data"><%=df.format(Math.round(addContri_Total))%></td>
    
    <td class="Data"><%=df.format(Math.round(VPFTotal))%></td>      
    <td class="Data"><%=df.format(Math.round(PFAdvTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal))%></td>    
     
      
    </tr>
    
    <tr>
    <td  class="reportsublabel" colspan="11" align="right">Supplimentary Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_suppli_reg) + Math.round(emolumentTotal_suppli_prev))%></td>    
    <td class="Data"><%=df.format(Math.round(EPFTotal_suppli_reg) + Math.round(EPFTotal_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(addContri_suppli_reg)+ Math.round(addContri_suppli_prev))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_suppli_reg)+Math.round(VPFTotal_suppli_prev))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_suppli_reg)+Math.round(PFAdvTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_suppli_reg)+Math.round(PFAdvIntTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_suppli_reg)+Math.round(subGrandTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_suppli_reg) + Math.round(PFNetTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_suppli_reg) + Math.round(PFContTotal_suppli_prev))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_suppli_reg) + Math.round(contGrandTotal_suppli_prev))%></td>    
       
 </tr>
 
 <tr>
    <td  class="reportsublabel" colspan="11" align="right">Arrear Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(addContri_arrear))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal_arrear))%></td>    
    <td class="Data"><%=df.format(Math.round(PFAdvTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(subGrandTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFNetTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal_arrear))%></td>  
    <td class="Data"><%=df.format(Math.round(contGrandTotal_arrear))%></td>    
      
 </tr>

<tr>
    <td  class="reportsublabel" colspan="11" align="right">Grand Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentGrandTot))%></td>
    <td class="Data"><%=df.format(Math.round(EPFGrandTot))%></td>
     <td class="Data"><%=df.format(addContriGrandTot)%></td>
    
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
 <!-- --> 
 

</body>
</html>
