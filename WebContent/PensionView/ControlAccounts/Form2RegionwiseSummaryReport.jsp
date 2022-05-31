<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String dispYear = request.getAttribute("dispYear").toString();
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
if(request.getAttribute("summaryInfoList")!=null){
 	ArrayList summaryInfoList = new ArrayList();  	
   	ArrayList form2summaryList = new ArrayList();
   	ArrayList form2summary = new ArrayList();
   	ArrayList form2summary_grndtots = new ArrayList();
   	
   	AaiEpfForm11Bean form2Data = new AaiEpfForm11Bean();
summaryInfoList=(ArrayList)request.getAttribute("summaryInfoList");
	  	
	  	  
  		   if(summaryInfoList.size()!=0){
  		for(int i=0;i<summaryInfoList.size();i++){
  		form2Data=(AaiEpfForm11Bean)summaryInfoList.get(i);
  		form2summaryList = form2Data.getForm2SummaryList();  		 
	  	}
	  	  } 
	  	 if(form2summaryList.size()!=0){
  		for(int i=0;i<form2summaryList.size();i++){  		  
  		form2summary =(ArrayList)form2summaryList.get(0);
  		 form2summary_grndtots=(ArrayList)form2summaryList.get(1);
  		 
  		   }
  		   
  		 
	  	} 
if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "Form2RegionwiseSummary_Report";
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
        <td height="24" colspan="19"  align="center">SCHEDULE OF PRIOR PERIOD    ADJUSTMENT IN OPEING BALANCE AS ON 1st April&nbsp;  <%=request.getAttribute("dispYear")%></td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
 
  <tr>
    <td colspan="19"><table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td  colspan="4">Unit Name:- <%=dispRegion%></td>
        <td  colspan="14">&nbsp;</td>
         <td align="right">Form no: AAIEPF-2 Summary</td>
      </tr>
    </table></td>    
  </tr>
  <tr>
    <td colspan="19"><table width="1351" border="1" cellpadding="0" cellspacing="0">
      <tr>
        <td  rowspan="2"><div align="center">Sl No</div></td>
        <td  rowspan="2"><div align="center">PF ID</div></td>
        <td  rowspan="2"><div align="center">Period of adjustment</div></td>        
        <td colspan="3"><div align="center">Adjustment In opening balances Subscription </div></td>
        <td colspan="3"><div align="center">Adjustment in opening Balances Employer Contribution </div></td>
        <td  rowspan="2"><div align="center">Adjustment in CPF Adv outstanding Balance </div></td>
        
      </tr>
      <tr>
        <td width="79"><div align="center">Subscription amount</div></td>
        <td width="65"><div align="center">Interest thereon</div></td>
        <td width="51"><div align="center">Total       (10+11)</div></td>
        <td width="79"><div align="center">Contribution amount</div></td>
        <td width="89"><div align="center">interest thereon</div></td>
        <td width="106"><div align="center">Total    (13+14)</div></td>
        </tr>
      <tr>
        <td><div align="center">1</div></td>
        <td><div align="center">2</div></td>        
        <td><div align="center">3</div></td>
        <td><div align="center">4</div></td>
        <td><div align="center">5</div></td>
        <td><div align="center">6</div></td>
        <td><div align="center">7</div></td>
        <td><div align="center">8</div></td>
        <td><div align="center">9</div></td>
         <td><div align="center">10</div></td>
         
      </tr>
      
         <%
         DecimalFormat df = new DecimalFormat("#########0"); 
        if(form2summary.size()!=0){        
        AaiEpfForm11Bean form2SummaryInfo=new AaiEpfForm11Bean();
    	  int slno = 0;
        for(int i=0;i<form2summary.size();i++){        
        form2SummaryInfo=(AaiEpfForm11Bean)form2summary.get(i);
         slno++;
        %>
        
         <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2SummaryInfo.getPensionNo()%></td>        
        <td class="Data" nowrap="nowrap"><%=form2SummaryInfo.getObYear()%></td>        
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjEmpSub()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjEmpSubInterest()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjEmpSubTotal()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjPension()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjPensionInt()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getAdjAaiContr()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2SummaryInfo.getOutStndAdv()))%></td>
        </tr>
        
      <%}}%>
        <%System.out.println("---form2summary_grndtots------"+form2summary_grndtots.size());
        if(form2summary_grndtots.size()!=0){
        AaiEpfForm11Bean form2SummaryGrandInfo=new AaiEpfForm11Bean();    	  
        for(int i=0;i<form2summary_grndtots.size();i++){        
        form2SummaryGrandInfo=(AaiEpfForm11Bean)form2summary_grndtots.get(i);       
        System.out.println("- ---"+form2SummaryGrandInfo.getAdjEmpSubAmtGrandTot());
       %>
       <tr>
        <td  colspan="3" align="right">Grand Total</td>        
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjEmpSubAmtGrandTot()))%></td>
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjEmpsubIntrstAmtGrandTot()))%></td>
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjEmpSubAmtTotlaGrandTot()))%></td>
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjPensionAmtGrandTot()))%></td>
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjPensionIntrstGrandTot()))%></td>
        <td  class="Data"><%=Math.round(Double.parseDouble(form2SummaryGrandInfo.getAdjAAIContriTotGrandTot()))%></td>
       </tr>
        
    <%}}%>
    
    </table></td>
  </tr>
  <%}else{
        
        %>
        
          <table align="center" width="100%">
          <tr>
          <td align="center">
          <strong>No Records Found</strong>
          </td>
          </tr>
          </table>
        <%
        }
      
      %>
  
  
  
 
</table>
</body>
</html>
