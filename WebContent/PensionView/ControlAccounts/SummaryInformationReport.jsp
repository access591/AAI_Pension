
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	  
	String dispYear = request.getAttribute("dispYear").toString();
	String dispYear_prev = request.getAttribute("dispYear_prev").toString();
	String dispYear_next= request.getAttribute("dispYear_next").toString();
	String empStatus=request.getAttribute("empStatus").toString(); 
	String reportStatus ="";
	if(empStatus.equals("A")){
	reportStatus = "(Active)";
	}else if(empStatus.equals("D")){
	reportStatus = "(De Active)";
	}else{
	reportStatus = "";
	}
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

function popupWindow(mylink, windowname)
		{ 
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink;
		   
		else
		href=mylink.href;
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		
		return true;
		}

</script>
<SCRIPT LANGUAGE="JAVASCRIPT">
var wind="";
function OpenExcel()
{
    wind=window.open("c:\OB-Differce_12.xls","Report","toolbar=yes,scrollbars=yes,resizable=yes,top=0,left=0,menuBar=yes");
    wind.window.focus();
}

function closeWin() 
{
  if (wind && wind.open && !wind.closed) wind.close();
}
</SCRIPT>

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
   <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
   <%
   			ArrayList summaryInfoList=new ArrayList();
   			ArrayList CBOBList=new ArrayList();
   			ArrayList contrlAccList=new ArrayList();
   			ArrayList contrlAccList_prev=new ArrayList();
   			ArrayList form8GrandTots_List=new ArrayList();
   			ArrayList form8summary_grndtots=new ArrayList();
   			ArrayList form2GrandTots_List=new ArrayList();
   			ArrayList form2summary_grndtots=new ArrayList();
   			ArrayList justifications_List = new ArrayList();
   			CommonUtil commonUtil=new CommonUtil();
	  	String reportType="",fileName="",dispDesignation="";
	   double  grandTotadvanceAmt = 0.0, grandTotEmpLoanAmt = 0.0, grandTotAAILoanAmt = 0.0, grandTotLoanAmt = 0.0, grandTotFinEmpAmt = 0.0,grandTotFinAAIAmt = 0.0,grandTotPensoinContri = 0.0,grandtotFinalsettlmentAmt = 0.0;
	   double  grandTotAdjEmpSubscription = 0.0,grandTotAdjContri = 0.0;
	  	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	
        
	  	AaiEpfForm11Bean summaryData = new AaiEpfForm11Bean();
	  	AaiEpfForm11Bean form8summary_GrndTots=new AaiEpfForm11Bean();
	  	AaiEpfForm11Bean form2summary_GrndTots=new AaiEpfForm11Bean();
	  	 
	  	if(request.getAttribute("summaryInfoList")!=null){	 
	  	summaryInfoList=(ArrayList)request.getAttribute("summaryInfoList");
	  	if(summaryInfoList.size()!=0){
  		for(int i=0;i<summaryInfoList.size();i++){
  		summaryData=(AaiEpfForm11Bean)summaryInfoList.get(i);
  		CBOBList = summaryData.getCBOBList();
  		contrlAccList = summaryData.getControlAccountSummaryList();
  		contrlAccList_prev = summaryData.getControlAccountSummaryList_prev();
  		form8GrandTots_List = summaryData.getForm8SummaryList();
  		form2GrandTots_List = summaryData.getForm2SummaryList();
  		justifications_List = summaryData.getCrtlAccJustificationList();
  		 
	  	}
	  	  }
	  	 } 
	  	 
	  	 
	  if(form8GrandTots_List.size()!=0){
  		for(int i=0;i<form8GrandTots_List.size();i++){  		 
  		form8summary_grndtots=(ArrayList)form8GrandTots_List.get(2);
  		   }
  		 }
      
    if(form8summary_grndtots.size()!=0){    
    System.out.println("-------form8summary_grndtots.size()------"+form8summary_grndtots.size());
  		for(int k=0;k<form8summary_grndtots.size();k++){  		 
  		 
  		form8summary_GrndTots=(AaiEpfForm11Bean)form8summary_grndtots.get(k);
  		grandTotadvanceAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotadvanceAmt()));
  		grandTotEmpLoanAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotEmpLoanAmt()));
  		grandTotAAILoanAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotAAILoanAmt()));
  		grandTotLoanAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotLoanAmt()));
  		grandTotFinEmpAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotFinEmpAmt()));
  		grandTotFinAAIAmt  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotFinAAIAmt()));
  		grandTotPensoinContri  = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandTotPensoinContri()));
  		grandtotFinalsettlmentAmt = Math.round(Double.parseDouble(form8summary_GrndTots.getGrandtotFinalsettlmentAmt()));
  		   }
  		 
	  	}
	  	
	  	
	  		 
	  if(form2GrandTots_List.size()!=0){
  		for(int i=0;i<form2GrandTots_List.size();i++){  		 
  		form2summary_grndtots=(ArrayList)form2GrandTots_List.get(1);
  		   }
  		 }
    
    if(form2summary_grndtots.size()!=0){    
    System.out.println("-------form2summary_grndtots.size()------"+form2summary_grndtots.size());
  		for(int k=0;k<form2summary_grndtots.size();k++){  		 
  		 
  		form2summary_GrndTots=(AaiEpfForm11Bean)form2summary_grndtots.get(k);
  		grandTotAdjEmpSubscription = Math.round(Double.parseDouble(form2summary_GrndTots.getAdjEmpSubAmtTotlaGrandTot()));
  		grandTotAdjContri  = Math.round(Double.parseDouble(form2summary_GrndTots.getAdjAAIContriTotGrandTot()));
  		  }
  		 
	  	}
	  	
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = " ControlAccSummary "+reportStatus+"("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  	 
       
   %>
   <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="100" height="50" /></td>
        <td>AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
      <tr>
        <td width="38%">&nbsp;</td>
        <td width="55%">EMPLOYEES PROVIDENT FUND</td>
      </tr>
	     <tr>
        <td colspan="3" align="center">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="4" align="center" class="reportlabel">Summarized Annual Statement of  Subscription/Contribution   during the Year  <font style="text-decoration: underline"><%=dispYear%></font><%=reportStatus%> </td>
      </tr>
      <tr>
    <td colspan="7">&nbsp;</td>
  </tr>
      <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    
    <td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>
    
  </tr>
  
    </table></td>
  </tr>
   <tr>
    <td>&nbsp;</td>
  </tr>
    
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr><td align="left" class="label" colspan="3"><font size="3"><b> Block  I :</b></font></td></tr>   
  <tr> 
  
   		<td>
   			 <table width="100%" align="center" border="1" cellspacing="0" cellpadding="0"> 
   	<tr>         
        <td colspan="3" class="label" align="center"> Comparing Form 11 & 12 <%=dispYear%> With Form 1 <%=dispYear_next%></td>
     </tr>  
      <tr>
        <td align="center" class="label" >EPF Forms</td>
        <td  align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td align="center" class="label">AAI CONTRIBUTION</td>
      </tr>
      <%
       	 
       	 
        
  		AaiEpfForm11Bean info=new AaiEpfForm11Bean();
  		//System.out.println("-------summaryInfoList.size()------"+summaryInfoList.size()); 
  		if(CBOBList.size()!=0){  
  		for(int i=0;i<CBOBList.size();i++){
  		info=(AaiEpfForm11Bean)CBOBList.get(i);
  		 
  	%>
  
	 <tr>
	 <td width="7%" class="NumData">Form1  <%=dispYear_next%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(info.getSubscriptionAmt()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(info.getContributionamt()))%></td>
	 </tr>
	 <tr>
	 <td width="6%" class="NumData">Form 11/Form 12  <%=dispYear%></td>	 
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(info.getTotEmpClosingBalance()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(info.getTotAAIClosingBalance()))%></td>
	 </tr>
  </table>
  </td>
  </tr>   
    <%} 
  }%>
    
  <% 	
  		double empNetOBTot = 0.0,AAINetOBTot = 0.0,empAdjOBTot = 0.0,AAIAdjOBTot = 0.0,empSubTotalTot = 0.0,emplrPFTot = 0.0,empPFWTotalTot = 0.0,AAIPFWTotalTot = 0.0,empSubIntrstTot = 0.0,AAIContriIntrstTot = 0.0,FinalPaymentSubTot = 0.0,FinalPaymentContrTot = 0.0,EMPClosingBalTot = 0.0, AAIClosingBaltot = 0.0;
  		double empNetOBTot_prev = 0.0,AAINetOBTot_prev = 0.0,empAdjOBTot_prev = 0.0,AAIAdjOBTot_prev = 0.0,empSubTotalTot_prev = 0.0,emplrPFTot_prev = 0.0,empPFWTotalTot_prev = 0.0,AAIPFWTotalTot_prev = 0.0,empSubIntrstTot_prev = 0.0,AAIContriIntrstTot_prev = 0.0,FinalPaymentSubTot_prev = 0.0,FinalPaymentContrTot_prev = 0.0,EMPClosingBalTot_prev = 0.0, AAIClosingBaltot_prev = 0.0;
  		
  		double empNetOBTot_diff = 0.0,AAINetOBTot_diff = 0.0,empAdjOBTot_diff = 0.0,AAIAdjOBTot_diff = 0.0,empSubTotalTot_diff = 0.0,emplrPFTot_diff = 0.0,empPFWTotalTot_diff = 0.0,AAIPFWTotalTot_diff = 0.0,empSubIntrstTot_diff = 0.0,AAIContriIntrstTot_diff = 0.0,FinalPaymentSubTot_diff = 0.0,FinalPaymentContrTot_diff = 0.0,EMPClosingBalTot_diff = 0.0, AAIClosingBaltot_diff = 0.0;
  		  
               
  		AaiEpfForm11Bean contrlinfo=new AaiEpfForm11Bean();  		 
  		if(contrlAccList.size()!=0){
  		for(int i=0;i<contrlAccList.size();i++){
  		contrlinfo=(AaiEpfForm11Bean)contrlAccList.get(i);
  		    
  		empNetOBTot = Math.round(Double.parseDouble(contrlinfo.getEmpNetOBTot()));
  		AAINetOBTot = Math.round(Double.parseDouble(contrlinfo.getAAINetOBTot()));
  		empAdjOBTot = Math.round(Double.parseDouble(contrlinfo.getEmpAdjOBTot()));
  		AAIAdjOBTot = Math.round(Double.parseDouble(contrlinfo.getAAIAdjOBTot()));
  		empSubTotalTot = Math.round(Double.parseDouble(contrlinfo.getEmpSubTotalTot()));
  		emplrPFTot = Math.round(Double.parseDouble(contrlinfo.getEmplrPFTot()));
  		empPFWTotalTot = Math.round(Double.parseDouble(contrlinfo.getEmpPFWTotalTot()));
  		AAIPFWTotalTot = Math.round(Double.parseDouble(contrlinfo.getAAIPFWTotalTot()));
  		empSubIntrstTot = Math.round(Double.parseDouble(contrlinfo.getEmpSubIntrstTot()));
  		AAIContriIntrstTot = Math.round(Double.parseDouble(contrlinfo.getAAIContriIntrstTot()));
  		FinalPaymentSubTot = Math.round(Double.parseDouble(contrlinfo.getFinalPaymentSubTot()));
  		FinalPaymentContrTot = Math.round(Double.parseDouble(contrlinfo.getFinalPaymentContrTot()));
  		EMPClosingBalTot = Math.round(Double.parseDouble(contrlinfo.getEmpCLBalTot()));
  		AAIClosingBaltot = Math.round(Double.parseDouble(contrlinfo.getAAICLBalTot()));
  					 }
  	 		 	}
  		 
       	         
  		AaiEpfForm11Bean contrlinfo_prev=new AaiEpfForm11Bean();
  		if(contrlAccList_prev.size()!=0){
  		for(int i=0;i<contrlAccList_prev.size();i++){
  		contrlinfo_prev=(AaiEpfForm11Bean)contrlAccList_prev.get(i);
  		    
  		empNetOBTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpNetOBTot()));
  		AAINetOBTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getAAINetOBTot()));
  		empAdjOBTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpAdjOBTot()));
  		AAIAdjOBTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getAAIAdjOBTot()));
  		empSubTotalTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpSubTotalTot()));
  		emplrPFTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmplrPFTot()));
  		empPFWTotalTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpPFWTotalTot()));
  		AAIPFWTotalTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getAAIPFWTotalTot()));
  		empSubIntrstTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpSubIntrstTot()));
  		AAIContriIntrstTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getAAIContriIntrstTot()));
  		FinalPaymentSubTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getFinalPaymentSubTot()));
  		FinalPaymentContrTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getFinalPaymentContrTot()));
  		EMPClosingBalTot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getEmpCLBalTot()));
  		AAIClosingBaltot_prev = Math.round(Double.parseDouble(contrlinfo_prev.getAAICLBalTot()));
  					 }
  	 		 	}
  			 
	   
	   
	  empAdjOBTot_diff = empAdjOBTot - grandTotAdjEmpSubscription;
	   AAIAdjOBTot_diff = AAIAdjOBTot - grandTotAdjContri;
	    
	   //empSubTotalTot_diff = empSubTotalTot - empSubTotalTot_prev;
	   //emplrPFTot_diff = emplrPFTot - emplrPFTot_prev;
	   empPFWTotalTot_diff = empPFWTotalTot - (grandTotadvanceAmt+grandTotEmpLoanAmt);
	   AAIPFWTotalTot_diff = AAIPFWTotalTot - grandTotAAILoanAmt;
	   empSubIntrstTot_diff = empSubIntrstTot - empSubIntrstTot_prev;
	   AAIContriIntrstTot_diff = AAIContriIntrstTot - AAIContriIntrstTot_prev;
	   FinalPaymentSubTot_diff = FinalPaymentSubTot - grandTotFinEmpAmt;
	   FinalPaymentContrTot_diff = FinalPaymentContrTot - (grandTotFinAAIAmt-grandTotPensoinContri);
	   
	   EMPClosingBalTot_diff = empNetOBTot - EMPClosingBalTot_prev;
	   AAIClosingBaltot_diff = AAINetOBTot - AAIClosingBaltot_prev;
   %>
   <tr>
   	<td>&nbsp;</td>
   </tr>
   <tr><td align="left" class="label" colspan="8"><font size="3"><b> Block  II :</b></font></td></tr>   
  <tr> 
  <tr>
   		<td>
   			 <table width="100%" align="center" border="1" cellspacing="0" cellpadding="0">
   	    <tr>         
        <td colspan="8" class="label" align="center">Summarized Control account <%=dispYear%></td>
        </tr>   
        
      <tr>
        <td align="center" class="label"  rowspan="2">AAI EPF-11</td>
        <td  align="center" class="label">OB.Employee Subscription</td>
        <td align="center" class="label">Adj.OB Employee Subscription</td>
        <td align="center" class="label">Employeer Subscription Total</td>
        <td align="center" class="label">Refundable advance/ PFW /NRFW</td>
        <td align="center" class="label">Employee Subscription Int</td>
        <td align="center" class="label">Final Payments</td>
        <td align="center" class="label">Closing Bal</td>
      </tr>
      <tr>        
        <td  align="center" class="label">1</td>
        <td align="center" class="label">2</td>
        <td align="center" class="label">3</td>
        <td align="center" class="label">4</td>
        <td align="center" class="label">5</td>
        <td align="center" class="label">6</td>
        <td align="center" class="label">7</td>
      </tr>
      
  	 
	 <tr>
	 <td width="7%" class="NumData" nowrap="nowrap"> <%=dispYear%></td>
	 <td width="8%" class="NumData"><%=df1.format(empNetOBTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(empAdjOBTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(empSubTotalTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(empPFWTotalTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(empSubIntrstTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(FinalPaymentSubTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(EMPClosingBalTot)%></td>
	 </tr>
	 <tr>
	 <td width="7%" class="NumData" nowrap="nowrap">Closing Balance of <%=dispYear_prev%></td>	 
	 <td width="8%" class="NumData"><%=df1.format(EMPClosingBalTot_prev)%></td> 
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new"   href="./aaiepfreportservlet?method=summaryAdj&frm_finyear=<%=dispYear%>&empstatus=<%=empStatus%>&subtotal=<%=df1.format(grandTotAdjEmpSubscription)%>&frm_flag=totalsreport"><%=df1.format(grandTotAdjEmpSubscription)%></a></td> 
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./aaiepfreportservlet?method=getsummaryinformation&frm_frmType=form8summary&frm_reportpurpose=view&frm_status=<%=empStatus%>&frm_finyear=<%=dispYear%>"><%=df1.format(grandTotadvanceAmt+grandTotEmpLoanAmt)%></a></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./aaiepfreportservlet?method=getsummaryinformation&frm_frmType=form8summary&frm_reportpurpose=view&frm_status=<%=empStatus%>&frm_finyear=<%=dispYear%>"><%=df1.format(grandTotFinEmpAmt)%></a></td> 
	 <td width="8%" class="NumData">&nbsp;</td>	 
	 </tr>
	 <tr>
	 <td width="7%" class="NumData">Grand Totals Other than OB</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 <tr> 
	 <td width="7%" class="NumData">Difference</td>
	<td width="8%" class="NumData"><%=df1.format(EMPClosingBalTot_diff)%></td>
	 <td width="8%" class="NumData"><%=df1.format(empAdjOBTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><%=df1.format(empPFWTotalTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><%=df1.format(FinalPaymentSubTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 <tr>
	 <td width="7%" class="NumData" nowrap="nowrap">Excess amount in closing bal <%=dispYear%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 <tr>
	 <td width="7%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 
	 
	 <tr>
        <td align="center" class="label" >AAI EPF-12</td>
        <td  align="center" class="label">OB.Employeer Contribution</td>
        <td align="center" class="label">Adj.OB Employeer Contribution</td>
        <td align="center" class="label">Employer contribution PF</td>
        <td align="center" class="label">PFW/ NRFW</td>
        <td align="center" class="label">Employeer Contribution Int.</td>
        <td align="center" class="label">Final Payment</td>
        <td align="center" class="label">Totals</td>
      </tr>
      
      
	 <tr>
	 <td width="7%" class="NumData" nowrap="nowrap">Amount of <%=dispYear%></td>	  
	 <td width="8%" class="NumData"><%=df1.format(AAINetOBTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(AAIAdjOBTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(emplrPFTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(AAIPFWTotalTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(AAIContriIntrstTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(FinalPaymentContrTot)%></td>
	 <td width="8%" class="NumData"><%=df1.format(AAIClosingBaltot)%></td>
	 
	 </tr>
	 
	 <tr>
	 <td width="7%" class="NumData" nowrap="nowrap">Closing Balance of <%=dispYear_prev%></td>	 
	 <td width="8%" class="NumData"><%=df1.format(AAIClosingBaltot_prev)%></td>
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./aaiepfreportservlet?method=summaryAdj&frm_finyear=<%=dispYear%>&empstatus=<%=empStatus%>&aaitotal=<%=df1.format(grandTotAdjContri)%>&frm_flag=totalsreport"><%=df1.format(grandTotAdjContri)%></a></td> 
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./aaiepfreportservlet?method=getsummaryinformation&frm_status=<%=empStatus%>&frm_frmType=form8summary&frm_reportpurpose=view&frm_finyear=<%=dispYear%>"><%=df1.format(grandTotAAILoanAmt)%></a></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./aaiepfreportservlet?method=getsummaryinformation&frm_status=<%=empStatus%>&frm_frmType=form8summary&frm_reportpurpose=view&frm_finyear=<%=dispYear%>"><%=df1.format(grandTotPensoinContri-grandTotFinAAIAmt)%></a></td> 
	 <td width="8%" class="NumData">&nbsp;</td>	  
	 </tr>
	  
	 <tr>
	 <td width="7%" class="NumData">Grand Totals Other than OB</td>
	 <td width="8%" class="NumData">&nbsp;</td>	
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	  
	 <tr>
	 <td width="7%" class="NumData">Difference</td> 
	 <td width="8%" class="NumData"><%=df1.format(AAIClosingBaltot_diff)%></td>
	 <td width="8%" class="NumData"><%=df1.format(AAIAdjOBTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><%=df1.format(AAIPFWTotalTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData"><%=df1.format(FinalPaymentContrTot_diff)%></td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 
	 <tr>
	 <td width="7%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 <td width="8%" class="NumData">&nbsp;</td>
	 </tr>
	 
	 
  </table>
  </td>
  </tr> 
  <% if(justifications_List.size()!=0){
  AaiEpfForm11Bean justificationInfo=new AaiEpfForm11Bean();
  int srlno =0;
  %> 	    
  <tr><td align="left" class="label" colspan="8"><font size="3"><b> Block  III :</b></font></td></tr> 
  <tr>
  	<td>
  		<table width="30%" align="left" border="1" cellspacing="0" cellpadding="0">
  			  <tr>         
       			 <td colspan="4" class="label" align="center">Justification <%=dispYear%></td>
        	</tr>
       		 <tr>
        		<td align="center" class="label" >Srl No</td>        		 
       		 	<td  align="center" class="label">Description</td>       		  
        	</tr>
        	<tr>
        	<%for( int k=0;k<justifications_List.size();k++){
        	justificationInfo=(AaiEpfForm11Bean)justifications_List.get(k);
            srlno++;
        	%>
       		
        		<td width="7%" class="NumData"><%=srlno%></td>        		 
	 			<td width="8%" class="NumData" nowrap="nowrap"><a href="<%=basePath%>aaiepfreportservlet?method=downloadexcel&filepath=<%=justificationInfo.getJPath()%>" ><%=justificationInfo.getJDescription()%></a></td>   		  
        	</tr>
     	 <%}%>
  		</table>
  	</td>
  </tr>  
 <%}%>  
				
</table>

  </body>
</html>
