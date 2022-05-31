<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String finYear = request.getAttribute("finYear").toString();
String empstatus = request.getAttribute("empstatus").toString();
String status="",adjTypeStatus = "";
if(empstatus.equals("A")){
 status="(Active ";
 }else{
    status="Deactive ";
   }
String adjType = request.getAttribute("adjType").toString();
 if(adjType.equals("S")){
 adjTypeStatus="System Corrections )";
 }else{
    adjTypeStatus="Manual Corrections )";
   }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Form 2 Adjusmnts Justification</title>
</head>

<body>
<%
 	  
    ArrayList adjOBList=new ArrayList();
    if(request.getAttribute("adjOBList")!=null){
     adjOBList=(ArrayList)request.getAttribute("adjOBList");
    }	
     
     
   System.out.println("--in jsp--"+adjOBList.size());
  DecimalFormat df = new DecimalFormat("#########0");  
  double adjEmpSubAmtGrandTot=0.00,adjEmpsubIntrstAmtGrandTot=0.00,adjEmpSubAmtTotlaGrandTot =0.00,
  adjPensionAmtGrandTot=0.0, adjPensionIntrstGrandTot =0.00, adjAAIContriTotGrandTot=0.00,PensionContriAdjGrandTot =0.00;
            
 
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
        <td height="24" colspan="19"  align="center">SCHEDULE OF PRIOR PERIOD    ADJUSTMENT IN OPEING BALANCE AS ON 1st April&nbsp;  <%=finYear%> <%=status%><%=adjTypeStatus%></td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
 
  <tr>
    <td colspan="19"><table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td  colspan="4">Unit Name:-  All Regions</td>
        <td  colspan="14">&nbsp;</td>
         <td align="right">Form no: AAIEPF-2 Adjustmsnts Summary</td>
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
        <td  rowspan="2"><div align="center">Adjustment in Pension Contribution </div></td>
        
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
        AaiEpfForm11Bean form2AdjInfo=new AaiEpfForm11Bean();
    	  int slno = 0;
    	if(request.getAttribute("adjOBList")!=null){
        if(adjOBList.size()!=0){
  		for(int i=0;i<adjOBList.size();i++){
  		form2AdjInfo=(AaiEpfForm11Bean)adjOBList.get(i);
          slno++;
         
        
        adjEmpSubAmtGrandTot =  adjEmpSubAmtGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSub()));
        adjEmpsubIntrstAmtGrandTot = adjEmpsubIntrstAmtGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSubInterest()));
        adjEmpSubAmtTotlaGrandTot = adjEmpSubAmtTotlaGrandTot+ Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSubTotal()));
        adjPensionAmtGrandTot = adjPensionAmtGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getAdjPension()));
        adjPensionIntrstGrandTot = adjPensionIntrstGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getAdjPensionInt()));
        adjAAIContriTotGrandTot = adjAAIContriTotGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getAdjAaiContr()));
        PensionContriAdjGrandTot = PensionContriAdjGrandTot + Math.round(Double.parseDouble(form2AdjInfo.getPensionContriAdj()));
        %>
        
         <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2AdjInfo.getPensionNo()%></td>        
        <td class="Data" nowrap="nowrap"><%=form2AdjInfo.getObYear()%></td>        
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSub()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSubInterest()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjEmpSubTotal()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjPension()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjPensionInt()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getAdjAaiContr()))%></td>
        <td class="Data"><%=Math.round(Double.parseDouble(form2AdjInfo.getPensionContriAdj()))%></td>
        </tr>
        
      <%}%>
      <tr>
        <td  colspan="3" align="right">Grand Total</td>        
        <td  class="Data"><%=Math.round(adjEmpSubAmtGrandTot)%></td>
        <td  class="Data"><%=Math.round(adjEmpsubIntrstAmtGrandTot)%></td>
        <td  class="Data"><%=Math.round(adjEmpSubAmtTotlaGrandTot)%></td>
        <td  class="Data"><%=Math.round(adjPensionAmtGrandTot)%></td>
        <td  class="Data"><%=Math.round(adjPensionIntrstGrandTot)%></td>
        <td  class="Data"><%=Math.round(adjAAIContriTotGrandTot)%></td>
        <td  class="Data"><%=Math.round(PensionContriAdjGrandTot)%></td>
       </tr> 
      <%}else{
        
        %>
        <tr>
        
          <td  align="center"  colspan="10">
          <strong>No Records Found </strong>
          </td>
          </tr>
           
        <%
        }}
      
      %>
         
  
 
</table>
</body>
</html>
