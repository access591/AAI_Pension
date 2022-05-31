
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	  
	String dispYear = request.getAttribute("dispYear").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <script type="text/javascript">
    
    
	function showTip(txt,element){
	    var theTip = document.getElementById("spnTip");
			theTip.style.top= GetTop(element);
	   //alert(theTip.style.top);
	    if(txt=='')
	    {
	    txt='--';
	    }
	    theTip.innerHTML=""+txt;
		theTip.style.left= GetLeft(element) - theTip.offsetWidth;
	    
	    theTip.style.visibility = "visible";
	}

function hideTip()
{
	document.getElementById("spnTip").style.visibility = "hidden";
} 
	
function GetTop(elm){

	var  y = 0;
	y = elm.offsetTop;
	elm = elm.offsetParent;
	while(elm != null){
		y = parseInt(y) + parseInt(elm.offsetTop);
		elm = elm.offsetParent;
	}	
	return y;
}
function GetLeft(elm){

	var x = 0;
	x = elm.offsetLeft;
	elm = elm.offsetParent;
	while(elm != null){
		x = parseInt(x) + parseInt(elm.offsetLeft);
		elm = elm.offsetParent;
	}
	
	return x;
}	
 function high(obj)
 	{
	//obj.style.background = 'rgb(220,232,236)';
	}

function low(obj) {
	///obj.style.background='#EFEFEF';	
}
</script>
    <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   			ArrayList summaryInfoList=new ArrayList();
   			ArrayList form8summaryList=new ArrayList();
   			ArrayList form8summary_all=new ArrayList();
   			ArrayList form8summary_chqiad=new ArrayList();
   			ArrayList form8summary_grndtots=new ArrayList();
   			AaiEpfForm11Bean form8Data = new AaiEpfForm11Bean();
	  	String reportType="",fileName="",dispDesignation="";
	  	 
	  	CommonUtil commonUtil=new CommonUtil();
	  	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	
       	double  advanceAmt = 0.0, EmpLoanAmt = 0.0, AAILoanAmt = 0.0, LoanAmt = 0.0, FinEmpAmt = 0.0,FinAAIAmt = 0.0,PensoinContri = 0.0,FinalsettlmentAmt = 0.0;
		double   totLoanAmt = 0.0, totFinalsettlmentAmt = 0.0;		
		double  grandTotadvanceAmt = 0.0, grandTotEmpLoanAmt = 0.0, grandTotAAILoanAmt = 0.0, grandTotLoanAmt = 0.0, grandTotFinEmpAmt = 0.0,grandTotFinAAIAmt = 0.0,grandTotPensoinContri = 0.0,grandtotFinalsettlmentAmt = 0.0;
		
       	double  advanceAmt_chq = 0.0, EmpLoanAmt_chq = 0.0, AAILoanAmt_chq = 0.0, LoanAmt_chq = 0.0, FinEmpAmt_chq = 0.0,FinAAIAmt_chq = 0.0,PensoinContri_chq = 0.0,FinalsettlmentAmt_chq = 0.0;
		double   totLoanAmt_chq = 0.0, totFinalsettlmentAmt_chq = 0.0;		
		 
	  	int cnt=0;
	  	 
	  	if(request.getAttribute("summaryInfoList")!=null){	 
	  	summaryInfoList=(ArrayList)request.getAttribute("summaryInfoList");
	  	
	  	  
  		   if(summaryInfoList.size()!=0){
  		for(int i=0;i<summaryInfoList.size();i++){
  		form8Data=(AaiEpfForm11Bean)summaryInfoList.get(i);
  		form8summaryList = form8Data.getForm8SummaryList();  		 
	  	}
	  	  }
  		   
  		   
	  	  
	  	  
	  	   System.out.println("-----form8summaryList.size()----"+form8summaryList.size());
	  	  if(form8summaryList.size()!=0){
  		for(int i=0;i<form8summaryList.size();i++){  		  
  		form8summary_all=(ArrayList)form8summaryList.get(0);
  		form8summary_chqiad=(ArrayList)form8summaryList.get(1);
  		form8summary_grndtots=(ArrayList)form8summaryList.get(2);
  		   }
  		   
	  	}
	  	   
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "Summary_Form 8("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  	 
       
   %>
   <tr>
   <td>
   <table width="100%" height="490" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   
    <td width="120" rowspan="3" align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
    <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
    	<td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
     	<td width="96">&nbsp;</td>
     	<td width="95">&nbsp;</td>
     	<td width="85">&nbsp;</td>
  	 	<td width="384"  class="reportlabel">FORM 8 SUMMARY REPORT</td>
  	 	<td width="87">&nbsp;</td>
    	<td width="272">&nbsp;</td>
  </tr>
   
   
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>    
    <td>&nbsp;</td> 
    <td  align="center" nowrap="nowrap" class="reportsublabel">FOR THE YEAR <font style="text-decoration: underline"><%=dispYear%></td>
    <td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>
    
  </tr>
  <tr>
    <td colspan="7">&nbsp;</td>
  </tr>
   

  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2">&nbsp;</td>
        <td colspan="1" align="center" class="label">Advances</td>
        <td colspan="3" align="center" class="label">Loans</td>
        <td colspan="4" align="center" class="label">FinalSettlement</td>
        
         
      </tr>
      <tr>
        <td width="4%"  class="label">Region</td>
        <td width="4%"   class="label">Unit Name</td>
        <td width="8%"   class="label">Amount </td>
        <td width="8%"   class="label"><div align="center">Employee Share</div></td>
        <td width="3%" class="label"><div align="center">AAI Share</div></td>
        <td width="3%"  ><div align="center" class="label">Total</div></td>
        <td width="6%"    class="label">Employee Share</td>
        <td width="3%"   class="label">AAI Share </td>      
        <td width="3%" align="center"   class="label">Pension Contribution<br/> deducted</td>        
        <td width="3%" class="label"  >Net Payment</td>
         
      </tr>
      
     
    
        <%
       	 
  		AaiEpfForm11Bean form8summary_ALL=new AaiEpfForm11Bean();
  		 
  		if(form8summary_all.size()!=0){
  		for(int i=0;i<form8summary_all.size();i++){
  		form8summary_ALL=(AaiEpfForm11Bean)form8summary_all.get(i);
  		 
  		advanceAmt = Math.round(Double.parseDouble(form8summary_ALL.getAdvanceAmnt()));
  		EmpLoanAmt = Math.round(Double.parseDouble(form8summary_ALL.getPfwSubscr()));
  		AAILoanAmt = Math.round(Double.parseDouble(form8summary_ALL.getPfwContr()));
  		totLoanAmt = Math.round(Double.parseDouble(form8summary_ALL.getTotLoanAmt()));
  		FinEmpAmt = Math.round(Double.parseDouble(form8summary_ALL.getFinalEmpSubscr()));
  		FinAAIAmt = Math.round(Double.parseDouble(form8summary_ALL.getFinalAAIContr()));
  		PensoinContri = Math.round(Double.parseDouble(form8summary_ALL.getPensinonContr()));
  		totFinalsettlmentAmt  =  Math.round(Double.parseDouble(form8summary_ALL.getTotFinalsettlmentAmt()));
  	 
  	%>
  
	 <tr>
	 
	 <td width="4%" nowrap="nowrap" class="Data"><%=form8summary_ALL.getRegion()%></td>
	 <td width="4%" nowrap="nowrap" class="Data"><%=form8summary_ALL.getAirportCode()%></td>
	 <td width="8%" class="NumData"><%=df1.format(advanceAmt)%></td>
	 <td width="8%" class="NumData"><%=df1.format(EmpLoanAmt)%></td>
	 <td width="3%" class="NumData"><%=df1.format(AAILoanAmt)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(totLoanAmt)%></td>
	 
	 <td width="7%" class="NumData"><%=df1.format(FinEmpAmt)%></td>
	 <td width="6%" class="NumData"><%=df1.format(FinAAIAmt)%></td>	  
 	 <td width="8%" class="NumData"><%=df1.format(PensoinContri)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(totFinalsettlmentAmt)%></td>
	  
	 </tr>
	
	<%}}%> 

  <% int j=0;  
   AaiEpfForm11Bean form8summary_CHQIAD=new AaiEpfForm11Bean(); 
      
    if(form8summary_chqiad.size()!=0){    
    System.out.println("-------form8summary_chqiad.size()------"+form8summary_chqiad.size());
  		for(int k=0;k<form8summary_chqiad.size();k++){
  		cnt++;
  		// System.out.println("----cnt---"+cnt);
  		form8summary_CHQIAD=(AaiEpfForm11Bean)form8summary_chqiad.get(k);
  		
  		advanceAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getAdvanceAmnt()));
  		EmpLoanAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getPfwSubscr()));
  		AAILoanAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getPfwContr()));
  		totLoanAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getTotLoanAmt()));
  		FinEmpAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getFinalEmpSubscr()));
  		FinAAIAmt_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getFinalAAIContr()));
  		PensoinContri_chq = Math.round(Double.parseDouble(form8summary_CHQIAD.getPensinonContr()));
  		totFinalsettlmentAmt_chq =  Math.round(Double.parseDouble(form8summary_CHQIAD.getTotFinalsettlmentAmt()));
  		
  		%>
     <tr>
	  <%if(cnt==1){%>
	 <td width="4%" nowrap="nowrap" class="Data"><%=form8summary_CHQIAD.getRegion()%></td>	
	 <%}else{%> 
	  <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td>	
	  <%}%> 
	 <td width="4%" nowrap="nowrap" class="Data"><%=form8summary_CHQIAD.getAirportCode()%></td>
	 <td width="8%" class="NumData"><%=df1.format(advanceAmt_chq)%></td>
	 <td width="8%" class="NumData"><%=df1.format(EmpLoanAmt_chq)%></td>
	 <td width="3%" class="NumData"><%=df1.format(AAILoanAmt_chq)%></td>
	  <td width="8%" class="NumData"><%=df1.format(totLoanAmt_chq)%></td> 
	  
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(FinEmpAmt_chq)%></td>	 
	 <td width="7%" class="NumData"><%=df1.format(FinAAIAmt_chq)%></td>
	 <td width="6%" class="NumData"><%=df1.format(PensoinContri_chq)%></td>	  
 	 <td width="8%" class="NumData"><%=df1.format(totFinalsettlmentAmt_chq)%></td> 
	 
	 </tr>
	  
    <%}}%>
      
     <%    
   AaiEpfForm11Bean form8summary_GrndTots=new AaiEpfForm11Bean(); 
      
    if(form8summary_grndtots.size()!=0){    
    System.out.println("-------form8summary_grndtots.size()------"+form8summary_grndtots.size());
  		for(int k=0;k<form8summary_grndtots.size();k++){
  		cnt++;
  		// System.out.println("----cnt---"+cnt);
  		form8summary_GrndTots=(AaiEpfForm11Bean)form8summary_grndtots.get(k);
  		grandTotadvanceAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotadvanceAmt()));
  		grandTotEmpLoanAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotEmpLoanAmt()));
  		grandTotAAILoanAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotAAILoanAmt()));
  		 grandTotLoanAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotLoanAmt()));
  		grandTotFinEmpAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotFinEmpAmt()));
  		grandTotFinAAIAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotFinAAIAmt()));
  		grandTotPensoinContri  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotPensoinContri()));
  		 grandtotFinalsettlmentAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandtotFinalsettlmentAmt()));
  		
  		%>
  <tr>
	 
	 <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td> 
	 <td width="4%" nowrap="nowrap" class="Data">Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotadvanceAmt)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotEmpLoanAmt)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandTotAAILoanAmt)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandTotLoanAmt)%></td>
	 
	 <td width="7%" class="NumData"><%=df1.format(grandTotFinEmpAmt)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandTotFinAAIAmt)%></td>	  
 	 <td width="8%" class="NumData"><%=df1.format(grandTotPensoinContri)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandtotFinalsettlmentAmt)%></td>
  
	 </tr>   
		  
    <%}}%> 
    </table></td>
  </tr>
  
 
       <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</td>
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
