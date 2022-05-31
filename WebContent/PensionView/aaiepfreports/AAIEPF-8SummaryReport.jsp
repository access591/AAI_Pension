<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	  
	 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>                
    <title>AAI</title>    
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
    <meta http-equiv="description" content="This is my page"/>   
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css"/>
    -->
    <link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css"/>
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

 
var wind='';
function OpenExcel()
{
    wind=window.open("c:\OB-Differce_12.xls","Report","toolbar=yes,scrollbars=yes,resizable=yes,top=0,left=0,menuBar=yes");
    wind.window.focus();
}

function closeWin() 
{
  if (wind && wind.open && !wind.closed) wind.close();
}
 
function showResults(finYear,accounthead,keynoStatus){
var url='';
var swidth=screen.Width-350;
	var sheight=screen.Height-450;	
url="<%=basePath%>aaiepfreportservlet?method=epfForm8SummaryDetails&frm_finyear="+finYear+"&accounthead="+accounthead+"&keynoStatus="+keynoStatus;
//alert(url);
 wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		 winOpened = true;
		 wind1.window.focus();
}
</script> 
  </head>
  
  <body>
  <form >         
   <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
   <% 
   			CommonUtil commonUtil=new CommonUtil();
	  	String reportType="",fileName="",dispYear=""; 
	  	  if(request.getAttribute("finYear")!=null){
	  	  dispYear =(String)request.getAttribute("finYear");
	  	  }
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = " ControlAccSummary("+dispYear+").xls";
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
        <td colspan="4" align="center" class="reportlabel">AAI EPF FORM-8 SUMMARY REPORT </td>
        
      </tr>
      <tr>
        <td colspan="4" align="center" class="reportlabel">For the Year  <%=dispYear%></td>
        
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
  <tr><td align="left" class="label" colspan="4"><font size="3"><b> Comparing Transactions in Cash Book with RPFC Modules </b></font></td></tr>   
  <tr> 
  
   		<td>
   			 <table width="100%" align="center" border="1" cellspacing="0" cellpadding="0"> 
   	  
   	  <tr>
         <td align="center" rowspan="2"  class="label"></td> 
          
      </tr>
   	  
      <tr>
     
        <td align="center" class="label" >ADVANCES</td>
        <td  align="center" class="label" colspan="2">LOANS</td>       
        <td align="center" class="label"  colspan="2">FINAL SETTLEMENT</td>
         
      </tr>
     <tr>
     	<td  align="center" class="label" >&nbsp;</td>
     	<td  align="center" class="label">Amount</td>
      	<td  align="center" class="label">Subscription</td>
        <td  align="center" class="label">Contribution</td>
         <td  align="center" class="label">Subscription</td>
        <td  align="center" class="label">Contribution</td>
     </tr>
     
      <%      
	double advanceAmnt = 0.00, advanceAmnt_withoutkey = 0.00, loanAmnt = 0.00,loanAmnt_withoutkey = 0.00,  finalsettlemntAmnt = 0.00 , finalsettlemntAmnt_withoutkey = 0.00;
	double  loanContriAmnt = 0.00,loanContriAmnt_withoutkey = 0.00,fpcontriamt=0.00,fpcontri_withoutkey=0.00;
	double cbamount= 0.00,cbpfwsubamount = 0.00,cbpfwcontriamount = 0.00,cbfpaymentsubamount = 0.00,cbfpaymentcontriamount= 0.00 ;
	double PFWAmntTot =0.00 , PFWAmntTot_withoutkey =0.00 ,finalSettlmntAmntTot =0.00,finalSettlmntAmntTot_withoutkey = 0.00,cbPFWAmntTot = 0.00 ,paymentsTot =0.00 , advanceAmnt_diff = 0.00 ,PFWSubAmt_diff = 0.00 , PFWContriAmt_diff = 0.00,  PFWAmt_diff =0.00 ,finalSettlmentSubAmnt_diff = 0.00, finalSettlmentContriAmnt_diff = 0.00,finalSettlmentAmnt_diff =0.00; 
	 DecimalFormat df = new DecimalFormat("#########0.00");
	 ArrayList form8SummaryList = new ArrayList();
	 if(request.getAttribute("form8SummaryList")!=null){ 
	 form8SummaryList = (ArrayList)request.getAttribute("form8SummaryList");
	 } 
      advanceAmnt =  Double.parseDouble((String)form8SummaryList.get(0));
      advanceAmnt_withoutkey =  Double.parseDouble((String)form8SummaryList.get(1));
       loanAmnt =  Double.parseDouble((String)form8SummaryList.get(2));
       loanAmnt_withoutkey = Double.parseDouble((String)form8SummaryList.get(3));       
       loanContriAmnt = Double.parseDouble((String)form8SummaryList.get(4));
       loanContriAmnt_withoutkey = Double.parseDouble((String)form8SummaryList.get(5));       
       PFWAmntTot = loanAmnt + loanContriAmnt;
       PFWAmntTot_withoutkey = loanAmnt_withoutkey + loanContriAmnt_withoutkey;
       
       finalsettlemntAmnt = Double.parseDouble((String)form8SummaryList.get(6));       
       finalsettlemntAmnt_withoutkey = Double.parseDouble((String)form8SummaryList.get(7));
       fpcontriamt = Double.parseDouble((String)form8SummaryList.get(8));
       fpcontri_withoutkey = Double.parseDouble((String)form8SummaryList.get(9));
       finalSettlmntAmntTot = finalsettlemntAmnt_withoutkey + fpcontriamt;
       finalSettlmntAmntTot_withoutkey = finalsettlemntAmnt_withoutkey + fpcontri_withoutkey;
       
       cbamount = Double.parseDouble((String)form8SummaryList.get(10));
       cbpfwsubamount = Double.parseDouble((String)form8SummaryList.get(11));       
       cbpfwcontriamount = Double.parseDouble((String)form8SummaryList.get(12));
       cbPFWAmntTot = cbpfwsubamount + cbpfwcontriamount;
       cbfpaymentsubamount =  Double.parseDouble((String)form8SummaryList.get(13));
       cbfpaymentcontriamount =  Double.parseDouble((String)form8SummaryList.get(14));
       paymentsTot = cbfpaymentsubamount + cbfpaymentcontriamount;
       
     
       advanceAmnt_diff = cbamount - advanceAmnt;
       PFWSubAmt_diff = cbpfwsubamount - loanAmnt;
       PFWContriAmt_diff = cbpfwcontriamount - loanContriAmnt;
       PFWAmt_diff = cbPFWAmntTot - PFWAmntTot;
       finalSettlmentSubAmnt_diff = cbfpaymentsubamount - finalsettlemntAmnt;
       finalSettlmentContriAmnt_diff = cbfpaymentcontriamount - fpcontriamt;
       finalSettlmentAmnt_diff = paymentsTot - finalSettlmntAmntTot;
      %>
       <tr>
         <td align="center"    class="label">Cash Book</td> 
         <td align="center"    class="data"><%=df.format(cbamount)%> [672.03]</td> 
         <td align="center"    class="data"><%=df.format(cbpfwsubamount)%> [672.04]</td>
         <td align="center"    class="data"><%=df.format(cbpfwcontriamount)%> [672.05]</td> 
         <td align="center"    class="data"><%=df.format(cbfpaymentsubamount)%> [672.06]</td> 
         <td align="center"    class="data"><%=df.format(cbfpaymentcontriamount)%> [672.07]</td>
          
      </tr>
       
      <tr>
         <td align="center"    class="label">RPFC</td> 
         <td align="center"    class="data"><%=df.format(advanceAmnt)%></td> 
         <td align="center"    class="data"><%=df.format(loanAmnt)%></td> 
          <td align="center"    class="data"><%=df.format(loanContriAmnt)%></td>
           <td align="center"    class="data"><%=df.format(finalsettlemntAmnt)%></td>
         <td align="center"    class="data"><%=df.format(fpcontriamt)%></td> 
      </tr>
    	<tr>
         <td align="center"    class="label">Difference</td> 
         <td align="center"    class="data"><a  title="Click the link to view Details" href="#" onclick="javascript:showResults('<%=dispYear%>','672.03','witkey');" ><%=df.format(advanceAmnt_diff)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.04','witkey');" ><%=df.format(PFWSubAmt_diff)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.05','witkey');" ><%=df.format(PFWContriAmt_diff)%></a></td>      
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.06','witkey');" ><%=df.format(finalSettlmentSubAmnt_diff)%></a></td>
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.07','witkey');" ><%=df.format(finalSettlmentContriAmnt_diff)%></a></td>
      </tr>
</table>
</td>
</tr>
<tr><td align="left" class="label" colspan="4"><font size="3"><b>RPFC (Uploaded By Stations)</b></font></td></tr>   
  <tr> 
  
   		<td>
   			 <table width="100%" align="center" border="1" cellspacing="0" cellpadding="0"> 
   	  
   	  <tr>
         <td align="center" rowspan="2"  class="label"></td> 
          
      </tr>
   	  
      <tr>         
        <td align="center" class="label" >ADVANCES</td>
        <td  align="center" class="label" colspan="2">LOANS</td>
        <td align="center" class="label" colspan="2">FINAL SETTLEMENT</td>
      </tr> 
        <tr>
        <td  align="center" class="label">&nbsp;</td> 
     	<td  align="center" class="label" >Amount</td>     	 
      	<td  align="center" class="label">Subscription</td>
        <td  align="center" class="label">Contribution</td>
         <td  align="center" class="label">Subscription</td>
        <td  align="center" class="label">Contribution</td>
     </tr>
      <tr>
         <td align="center"    class="label">RPFC</td> 
         <td align="center"    class="data"><a  title="Click the link to view Details" href="#"  onclick="javascript:showResults('<%=dispYear%>','672.03','witoutkey');" ><%=df.format(advanceAmnt_withoutkey)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details" href="#"  onclick="javascript:showResults('<%=dispYear%>','672.04','witoutkey');" ><%=df.format(loanAmnt_withoutkey)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.05','witoutkey');" ><%=df.format(loanContriAmnt_withoutkey)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.06','witoutkey');" ><%=df.format(finalsettlemntAmnt_withoutkey)%></a></td> 
         <td align="center"    class="data"><a  title="Click the link to view Details"  href="#"  onclick="javascript:showResults('<%=dispYear%>','672.07','witoutkey');" ><%=df.format(fpcontri_withoutkey)%></a></td> 
       
</table>
</td>
</tr>
</table>
</form>
  </body>
</html>
