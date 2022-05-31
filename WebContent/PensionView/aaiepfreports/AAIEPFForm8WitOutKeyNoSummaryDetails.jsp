<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%> 
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.nio.IntBuffer" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String finyear = "",accounthead="";
	int count=0;
	if(request.getAttribute("finYear")!=null){
	 finyear = (String)request.getAttribute("finYear");
	}
	if(request.getAttribute("accounthead")!=null){
	 accounthead = (String)request.getAttribute("accounthead");
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
   			CommonUtil commonUtil=new CommonUtil();
	  	String reportType="",fileName="",dispYear=""; 
	  	  if(request.getAttribute("finYear")!=null){
	  	  dispYear =(String)request.getAttribute("finYear");
	  	  }
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = " Form8Summary("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
		double advAmt = 0.00, subscriptionAmt = 0.00, contributionamt = 0.00,empShare = 0.00,  aaiShareAmnt = 0.00  ;
	 DecimalFormat df = new DecimalFormat("#########0.00");
	  AAIEPFReportBean summaryInfo = null; 
	   ArrayList witOutKeyNoList = new ArrayList(); 
	  if(request.getAttribute("witOutKeyNoList")!=null){
	 	witOutKeyNoList = (ArrayList)request.getAttribute("witOutKeyNoList");
	   
      
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
  <tr><td align="center" class="label" colspan="4"><font size="3"><b> Data UpLoaded By Stations <%=accounthead%> </b></font></td></tr>   
  <tr> 
  
   		<td>
   			 <table width="100%" align="center" border="1" cellspacing="0" cellpadding="0"> 
   	  
   	   
   	  
       
     <tr>
    	<td  align="center" class="label">Sr No</td>
     	<td  align="center" class="label">Pension No</td>
     	<td  align="center" class="label">Amount</td>     	 
      	<td  align="center" class="label">Subscription</td>
        <td  align="center" class="label">Contribution</td>
        <td  align="center" class="label">EmpShare</td>
        <td  align="center" class="label">AAIShare</td>
          
     </tr>
     
      <% 
       
      for(int i=0;i< witOutKeyNoList.size();i++){
      summaryInfo = (AAIEPFReportBean)witOutKeyNoList.get(i);
     count++;
       advAmt = advAmt + Double.parseDouble(summaryInfo.getAdvAmt());
       subscriptionAmt = subscriptionAmt + Double.parseDouble(summaryInfo.getSubscriptionAmt());
       contributionamt = contributionamt + Double.parseDouble(summaryInfo.getContributionamt());
       empShare = empShare + Double.parseDouble(summaryInfo.getEmpShare());
       aaiShareAmnt = aaiShareAmnt + Double.parseDouble(summaryInfo.getAAIShare());
        
      %>
       <tr>
        
         <td align="center"    class="data"><%=count%></td> 
         <td align="center"    class="data"><%=summaryInfo.getPensionNo()%></td>
         <td align="center"    class="data"><%=summaryInfo.getAdvAmt()%></td> 
         <td align="center"    class="data"><%=summaryInfo.getSubscriptionAmt()%></td> 
         <td align="center"    class="data"><%=summaryInfo.getContributionamt()%></td>
         <td align="center"    class="data"><%=summaryInfo.getEmpShare()%></td> 
         <td align="center"    class="data"><%=summaryInfo.getAAIShare()%></td>
          
      </tr>
       
  <%}%>    
    	<tr>
    	 
         <td align="center" colspan="2"    class="label">Total</td> 
         <td align="center"   class="data"><%=df.format(advAmt)%></td> 
         <td align="center"   class="data"><%=df.format(subscriptionAmt)%></td> 
         <td align="center"   class="data"><%=df.format(contributionamt)%></td> 
         <td align="center"   class="data"><%=df.format(empShare)%></td> 
         <td align="center"   class="data"><%=df.format(aaiShareAmnt)%></td> 
          
      </tr>
</table>
</td>
</tr>
   
   
       <%}else{%>
        <tr>
         <td align="center"  colspan="3"  class="label">No Records Found</td> 
         
         </tr>
       
       <%}%>

</table>
  </body>
</html>
