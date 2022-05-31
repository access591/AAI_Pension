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
String amtFlag =	request.getAttribute("amtFlag").toString();
String attribute =	request.getAttribute("attribute").toString(); 
String status="",adjTypeStatus = "",amntType = "";
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
   
if(amtFlag.equals("P")){

amntType = "Positive";
}else{
 amntType = "Negative";
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Form 2 OB Positive/Negative Amnts Justification</title>
</head>

<body>
<%
    ArrayList adjOBList=new ArrayList();    
    if(request.getAttribute("adjOBList")!=null){
     adjOBList=(ArrayList)request.getAttribute("adjOBList");
    }	
     	
     
     
  DecimalFormat df = new DecimalFormat("#########0");  
  double adjEmpSubAmtGrandTot=0.00,adjEmpsubIntrstAmtGrandTot=0.00,adjEmpSubAmtTotlaGrandTot =0.00,
  adjPensionAmtGrandTot=0.0, adjPensionIntrstGrandTot =0.00, adjAAIContriTotGrandTot=0.00,PensionContriAdjGrandTot =0.00;
  double adjEmpSubAmt=0.00,adjEmpsubIntrstAmt=0.00,adjEmpSubAmtTot =0.00,
  adjPensionAmt=0.0, adjPensionIntrst =0.00, adjAAIContriTot=0.00,PensionContriAdj =0.00;
  String condition = "";
  if(attribute.equals("Adj EmpSub")){
  condition = "form2AdjObInfo.getAdjEmpSubAmtFlag()";
  }else  if(attribute.equals("Adj AAIContri")){
  condition = "form2AdjObInfo.getAdjAaiContrAmtFlag()";
  }else  if(attribute.equals("Adj PenContri")){
  condition = "form2AdjObInfo.getPensionContriAdjAmtFalg()";
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
        <td height="24" colspan="19"  align="center">SCHEDULE OF PRIOR PERIOD    ADJUSTMENT IN OPEING BALANCE AS ON 1st April&nbsp;  <%=finYear%>  <%=attribute%>  <%=amntType%> <%=status%><%=adjTypeStatus%></td>
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
         System.out.println("-----in jsp --adjOBList--"+adjOBList.size()+"--attribute"+attribute+"-");
        AaiEpfForm11Bean form2AdjObInfo=new AaiEpfForm11Bean();
    	  int slno = 0;
    	if(request.getAttribute("adjOBList")!=null){
        if(adjOBList.size()!=0){
  		for(int i=0;i<adjOBList.size();i++){
  		form2AdjObInfo=(AaiEpfForm11Bean)adjOBList.get(i);
         slno++;
        if(attribute.equals("Adj EmpSub")){
        		   System.out.println("--in if--");
         if(form2AdjObInfo.getAdjEmpSubAmtFlag().equals(amtFlag)){ 
         System.out.println("--in if con--");
         adjEmpSubAmt=Double.parseDouble(form2AdjObInfo.getAdjEmpSub());
         adjEmpsubIntrstAmt=Double.parseDouble(form2AdjObInfo.getAdjEmpSubInterest());
         adjEmpSubAmtTot = Double.parseDouble(form2AdjObInfo.getAdjEmpSubTotal());
  		  
        
        adjEmpSubAmtGrandTot =  adjEmpSubAmtGrandTot + adjEmpSubAmt ;
        adjEmpsubIntrstAmtGrandTot = adjEmpsubIntrstAmtGrandTot + adjEmpsubIntrstAmt;
        adjEmpSubAmtTotlaGrandTot = adjEmpSubAmtTotlaGrandTot+ adjEmpSubAmtTot;
        
         %>
        <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2AdjObInfo.getPensionNo()%></td>        
        <td class="Data" nowrap="nowrap"><%=form2AdjObInfo.getObYear()%></td>        
        <td class="Data"><%=Math.round(adjEmpSubAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpsubIntrstAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpSubAmtTot)%></td>
        <td class="Data"><%=Math.round(adjPensionAmt)%></td>
        <td class="Data"><%=Math.round(adjPensionIntrst)%></td>
        <td class="Data"><%=Math.round(adjAAIContriTot)%></td>
        <td class="Data"><%=Math.round(PensionContriAdj)%></td>
        </tr>
        <%}}else  if(attribute.equals("Adj AAIContri")){        
        
          if(form2AdjObInfo.getAdjAaiContrAmtFlag().equals(amtFlag)){
        
          
  		 adjPensionAmt=Double.parseDouble(form2AdjObInfo.getAdjPension());
         adjPensionIntrst = Double.parseDouble(form2AdjObInfo.getAdjPensionInt());
         adjAAIContriTot=Double.parseDouble(form2AdjObInfo.getAdjAaiContr());
          
         adjPensionAmtGrandTot = adjPensionAmtGrandTot + adjPensionAmt;
        adjPensionIntrstGrandTot = adjPensionIntrstGrandTot + adjPensionIntrst;
        adjAAIContriTotGrandTot = adjAAIContriTotGrandTot + adjAAIContriTot;
        %>
        <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2AdjObInfo.getPensionNo()%></td>        
        <td class="Data" nowrap="nowrap"><%=form2AdjObInfo.getObYear()%></td>        
        <td class="Data"><%=Math.round(adjEmpSubAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpsubIntrstAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpSubAmtTot)%></td>
        <td class="Data"><%=Math.round(adjPensionAmt)%></td>
        <td class="Data"><%=Math.round(adjPensionIntrst)%></td>
        <td class="Data"><%=Math.round(adjAAIContriTot)%></td>
        <td class="Data"><%=Math.round(PensionContriAdj)%></td>
        </tr>
        <%}}  else if (attribute.equals("Adj PenContri")){ 
        
          if(form2AdjObInfo.getPensionContriAdjAmtFalg().equals(amtFlag)){ 
        
         PensionContriAdj =Double.parseDouble(form2AdjObInfo.getPensionContriAdj());          
         PensionContriAdjGrandTot = PensionContriAdjGrandTot + PensionContriAdj;
         
         %>
        <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=form2AdjObInfo.getPensionNo()%></td>        
        <td class="Data" nowrap="nowrap"><%=form2AdjObInfo.getObYear()%></td>        
        <td class="Data"><%=Math.round(adjEmpSubAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpsubIntrstAmt)%></td>
        <td class="Data"><%=Math.round(adjEmpSubAmtTot)%></td>
        <td class="Data"><%=Math.round(adjPensionAmt)%></td>
        <td class="Data"><%=Math.round(adjPensionIntrst)%></td>
        <td class="Data"><%=Math.round(adjAAIContriTot)%></td>
        <td class="Data"><%=Math.round(PensionContriAdj)%></td>
        </tr>
        <%}}%> 
        
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
          <strong>No Records Found in Manual Entries</strong>
          </td>
          </tr>
           
        <%
        }}
      
      %>
       
  
  
 
</table>
</body>
</html>
