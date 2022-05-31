
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
        <td colspan="3" align="center" class="reportlabel">MONTHLY SCHEDULE OF EMPLOYEE-WISE CPF  RECEIVED FORM OTHER ORGANIZATION FOR THE MONTH <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
   	ArrayList cardReportList=new ArrayList();
    ArrayList missingPFIDsList=new ArrayList();
    ArrayList epfSuppliList=new ArrayList();
    ArrayList epfArrearList=new ArrayList();
     
   	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="",finyear="";
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
  		if(!region.equals(""))
  		filePostfix="_"+stationName;
  	}else{
  		filePostfix="_"+region+"_"+stationName;
  	}
  	if(request.getAttribute("finYear")!=null){
  		finyear=(String)request.getAttribute("finYear");
  	}
  	
  	
  	%>
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
 	  double emolumentTotal_suppli=0,EPFTotal_suppli=0,VPFTotal_suppli=0,PFAdvTotal_suppli=0,PFAdvIntTotal_suppli=0,subGrandTotal_suppli=0,PFNetTotal_suppli=0,PFContTotal_suppli=0,contGrandTotal_suppli=0,outstandGrandTotal_suppli=0;
 	  double emolumentTotal_arrear=0,EPFTotal_arrear=0,VPFTotal_arrear=0,PFAdvTotal_arrear=0,PFAdvIntTotal_arrear=0,subGrandTotal_arrear=0,PFNetTotal_arrear=0,PFContTotal_arrear=0,contGrandTotal_arrear=0,outstandGrandTotal_arrear=0;
      double emolumentTot=0,EPFTot=0,VPFTot=0,PFAdvTot=0,PFAdvIntTot=0,subGrandTot=0,PFNetTot=0,PFContTot=0,contGrandTot=0,outstandGrandTot=0;
     
  	 	if(request.getAttribute("cardList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAI_EPF-4_Report_"+finyear;
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
		<td width="4%" rowspan="2" class="reportsublabel" nowrap="nowrap">Month <br/>mm-yyyy</td>
        <td width="4%" rowspan="2" class="reportsublabel">Salary drawn/ paid for<br/> CPF deduction Rs</td>
        <td colspan="7" align="center" class="reportsublabel">Received from the other organization</td>
        <td align="center" rowspan="2" class="reportsublabel">Station</td>
        <td align="center" rowspan="2" class="reportsublabel">Remarks</td>
      </tr>
      <tr>

        <td width="6%"  class="reportsublabel">EPF</td>
        <td width="6%"  class="reportsublabel">VPF</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Principal)</td>
        <td width="8%"  class="reportsublabel">PF Adv recovery<br/> (Interest)</td>
        <td width="8"   class="reportsublabel">Employer contribution </td>
        <td width="8%"  class="reportsublabel">Total <br/>(11 to 15)</td>         
        <td width="4%"  class="reportsublabel">Pension certificate amount</td>       
		
     </tr>
     
     <tr>
     
        <td width="3%" rowspan="2"  align="center"   class="reportsublabel">1</td>
        <td width="8%" rowspan="2" 	align="center" class="reportsublabel">2</td>
		<td width="8%" rowspan="2"  align="center" class="reportsublabel">3</td>
        <td width="8%" rowspan="2"  align="center" class="reportsublabel">4</td>
        <td width="12%" rowspan="2" align="center" class="reportsublabel">5</td>
        <td width="8%" rowspan="2"  align="center" class="reportsublabel">6</td>
		<td width="12%" rowspan="2" align="center" class="reportsublabel">7</td>
		<td width="4%" rowspan="2"  align="center" class="reportsublabel" >8</td>
		<td width="4%" rowspan="2"  align="center" class="reportsublabel" >9</td>
        <td width="4%" rowspan="2"  align="center" class="reportsublabel">10</td>           
        <td width="6%"  align="center" class="reportsublabel">11</td>
        <td width="6%" align="center"  class="reportsublabel">12</td>
        <td width="8%" align="center"  class="reportsublabel">13</td>
        <td width="8%" align="center"  class="reportsublabel">14</td>
        <td width="8"  align="center"  class="reportsublabel">15</td>
        <td width="8%" align="center"  class="reportsublabel">16</td>         
        <td width="4%" align="center" class="reportsublabel">17</td> 
        <td width="4%" align="center"  class="reportsublabel">18</td>    
        <td width="4%" align="center"  class="reportsublabel">19</td>          
		
     </tr>
  
  
     <tr></tr>
     <%
     	AaiEpfform3Bean epfForm4Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0;
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
		epfForm4Bean=(AaiEpfform3Bean)cardReportList.get(cardList);
		srlno++;
		diff=Double.parseDouble(epfForm4Bean.getFormDifference());
		
		
     %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data"><%=epfForm4Bean.getPfID()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getCpfaccno()%></td>
        <td class="Data"><%=epfForm4Bean.getEmployeeNo()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getDesignation()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getFhName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getDateOfBirth()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getMonthyear()%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getEmoluments()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getEmppfstatury()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getEmpvpf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getPrincipal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getInterest()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getPf()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getOtherOrgSubTotal()))%></td>
        <td class="NumData"><%=df.format(Double.parseDouble(epfForm4Bean.getPensionContribution()))%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm4Bean.getStation()%></td>
         
        <td class="Data">&nbsp;</td>
         
      </tr>
	<%
	
	 emolumentTotal+=Double.parseDouble(epfForm4Bean.getEmoluments());
	 EPFTotal+=Double.parseDouble(epfForm4Bean.getEmppfstatury());
	 VPFTotal+=Double.parseDouble(epfForm4Bean.getEmpvpf());
	 PFAdvTotal+=Double.parseDouble(epfForm4Bean.getPrincipal());
	 PFAdvIntTotal+=Double.parseDouble(epfForm4Bean.getInterest());	 
	 PFNetTotal+=Double.parseDouble(epfForm4Bean.getPf());
	 subGrandTotal+=Double.parseDouble(epfForm4Bean.getOtherOrgSubTotal());	 
	 PFContTotal+=Double.parseDouble(epfForm4Bean.getPensionContribution());
	 
	 grandDiffe+=diff;   
	
	}%>
	
    <tr>
    <td class="Data" colspan="9" align="right">Grand Total</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal))%></td>      
    <td class="Data"><%=df.format(Math.round(PFAdvTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal))%></td> 
    <td class="Data"><%=df.format(Math.round(PFNetTotal))%></td>   
    <td class="Data"><%=df.format(Math.round(subGrandTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFContTotal))%></td>       
    <td class="Data" >&nbsp;</td>     
    <td class="Data" >&nbsp;</td>    
    </tr>
    
	 <tr>
	  	<td colspan="20" class="Data">&nbsp;</td>
  	</tr>
  
</table>

 
  
 
<!--  <table width="100%" border="1" cellspacing="0" cellpadding="0">
 
 <tr>
    <td  class="reportsublabel" colspan="9" align="right">CPF Recoveries Totals</td>            
    <td class="Data"><%=df.format(Math.round(emolumentTotal))%></td>
    <td class="Data"><%=df.format(Math.round(EPFTotal))%></td>
    <td class="Data"><%=df.format(Math.round(VPFTotal))%></td>      
    <td class="Data"><%=df.format(Math.round(PFAdvTotal))%></td>  
    <td class="Data"><%=df.format(Math.round(PFAdvIntTotal))%></td>
  	<td class="Data"><%=df.format(Math.round(PFNetTotal))%></td>   
    <td class="Data"><%=df.format(Math.round(subGrandTotal))%></td>   
    <td class="Data"><%=df.format(Math.round(PFContTotal))%></td>  
    </tr>
   
 </table>-->
 <%}%>
 <!-- --> 
 

</body>
</html>
