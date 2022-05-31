<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String dispYear = request.getAttribute("dispYear").toString();
String dispYear_prev = request.getAttribute("dispYear_prev").toString();
String dispRegion = request.getAttribute("dispRegion").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
</head>

<body>
<%
String reportType="",fileName="",filePrefix="",filePostfix="";
 double frezedGrandTotal=0.00,curntGrandTotal=0.00,diffAmtGrandTotal=0.00; 
        
if(request.getAttribute("summaryInfoList")!=null){
 	ArrayList summaryInfoList = new ArrayList();  	
   	ArrayList form2AsummaryList = new ArrayList();
   	ArrayList form2Asummary = new ArrayList();   
   	AaiEpfForm11Bean form2Data = new AaiEpfForm11Bean();
summaryInfoList=(ArrayList)request.getAttribute("summaryInfoList");
	  	
	  	  
  		   if(summaryInfoList.size()!=0){
  		for(int i=0;i<summaryInfoList.size();i++){
  		form2Data=(AaiEpfForm11Bean)summaryInfoList.get(i);
  		form2Asummary = form2Data.getForm2ASummaryList();  		 
	  	}
	  	  } 
	  	  
  		   
  		 
if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "Form2ASummary_Report";
					fileName=filePrefix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
%>
<table width="1351" border="0">
  <tr>
    <td width="110">&nbsp;</td>
    <td width="66">&nbsp;</td>
    <td width="90">&nbsp;</td>
    <td width="52">&nbsp;</td>
    <td width="99">&nbsp;</td>
    <td width="17">&nbsp;</td>
    <td width="117">&nbsp;</td>
    <td width="25">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="35">&nbsp;</td>
    <td width="212">&nbsp;</td>
    <td colspan="3">&nbsp;</td>
    <td width="20">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="8">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="241">&nbsp;</td>
  </tr>
  <tr>
    <td>
    <table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
       
        <td width="556" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif"  width="75" height="55" align="right" /></td>
    <td nowrap="nowrap" rowspan="2">AIRPORTS AUTHORITY OF INDIA</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
      </tr>
    
  </table>
  </td>
  </tr>
  
  <tr>
    <td colspan="19"><table width="1351"  cellspacing="0" cellpadding="0">
      <tr>
        <td height="24" colspan="19" align="center">EMPLOYEES PROVIDENT FUND</td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
    </table>
   </td>
    
  </tr>
 
  
   <tr>
    <td colspan="19"><table width="1351"  cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td height="24" colspan="19"  align="center">ADJUSTMENTS DIFFERNCE For Financial Year <%=dispYear%></td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
 
  <tr>
    <td colspan="19"><table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td  colspan="4">Unit Name:- <%=dispRegion%></td>
        <td  colspan="14">&nbsp;</td>
         <td align="right">Form no: AAIEPF-2A Summary</td>
      </tr>
    </table></td>    
  </tr>
  <tr>
    <td colspan="19"><table width="1351" border="1" cellpadding="0" cellspacing="0">
      <tr>
        <td><div align="center">Sl No</div></td>
        <td><div align="center">PF ID</div></td>
        <td><div align="center">Employee Name</div></td>        
        <td><div align="center">Designation</div></td>
        <td><div align="center">Freezed Pension Total </div></td>
        <td><div align="center">Current Pension Total </div></td>
        <td><div align="center">Difference Amount </div></td>
        
      </tr>
       
      <tr>
        <td><div align="center">1</div></td>
        <td><div align="center">2</div></td>        
        <td><div align="center">3</div></td>
        <td><div align="center">4</div></td>
        <td><div align="center">5</div></td>
        <td><div align="center">6</div></td>
        <td><div align="center">7</div></td>
        
         
      </tr>
      
         <%
         DecimalFormat df = new DecimalFormat("#########0"); 
        if(form2Asummary.size()!=0){        
        AaiEpfForm11Bean form2ASummaryInfo=new AaiEpfForm11Bean();
    	  int slno = 0;
        for(int i=0;i<form2Asummary.size();i++){        
        form2ASummaryInfo=(AaiEpfForm11Bean)form2Asummary.get(i);
         slno++;
        
         frezedGrandTotal = frezedGrandTotal+Double.parseDouble(form2ASummaryInfo.getFreezdAdjTotal());
         curntGrandTotal = curntGrandTotal+Double.parseDouble(form2ASummaryInfo.getCurrentAdjTotal());
         diffAmtGrandTotal = diffAmtGrandTotal+Double.parseDouble(form2ASummaryInfo.getDiffAdjAmt());
       
        %>
        
         <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2ASummaryInfo.getPensionNo()%></td> 
        <td class="Data"><%=form2ASummaryInfo.getEmployeeName()%></td> 
        <td class="Data"><%=form2ASummaryInfo.getDesignation()%></td>                    
        <td class="Data"><%=Math.round(Double.parseDouble(form2ASummaryInfo.getFreezdAdjTotal()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2ASummaryInfo.getCurrentAdjTotal()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2ASummaryInfo.getDiffAdjAmt()))%></td>
        </tr>
        
      <%}}%>
         
       <tr>
        <td  colspan="4" align="right">Grand Total</td>             
        <td  class="Data"><%=Math.round(frezedGrandTotal)%></td>
        <td  class="Data"><%=Math.round(curntGrandTotal)%></td>
        <td  class="Data"><%=Math.round(diffAmtGrandTotal)%></td>
       </tr>
        
      <%}%>
 
</table>
</body>
</html>
