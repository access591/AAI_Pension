
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
	String status=request.getAttribute("status").toString();
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
   		Map map=new LinkedHashMap();
   		ArrayList list=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="",statusType="";
	  	 
	  	CommonUtil commonUtil=new CommonUtil();
	  	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	//for cpf
       	double totalEmoluments=0.0,totalpfStaturary=0.0,totalempVpf=0.0,totalPrinciple=0.0,totalInterest=0.0,empEmpTotal=0.0,totalPF=0.0,totalPensionContri=0.0,totalAAINet=0.0;
       	double grandTotEmoluments=0.0,grandTotEPF=0.0,grandTotVPF=0.0,grandTotPrinciple=0.0,grandTotInterest=0.0,grandempTotal=0.0,grandTotPF=0.0,grandTotPensionContri=0.0,grandTotAAINet=0.0;
       	//for sup
       	double totalSupEmoluments=0.0,totalSuppfStaturary=0.0,totalSupempVpf=0.0,totalSupPrinciple=0.0,totalSupInterest=0.0,empEmpTotalSup=0.0,totalSupPF=0.0,totalSupPensionContri=0.0,totalSupAAINet=0.0;
       	double grandSupTotEmoluments=0.0,grandSupTotEPF=0.0,grandSupTotVPF=0.0,grandSupTotPrinciple=0.0,grandSupTotInterest=0.0,grandSupempTotal=0.0,grandSupTotPF=0.0,grandSupTotPensionContri=0.0,grandSupTotAAINet=0.0;
       	//for arr
       	double totalArrEmoluments=0.0,totalArrpfStaturary=0.0,totalArrempVpf=0.0,totalArrPrinciple=0.0,totalArrInterest=0.0,empEmpTotalArr=0.0,totalArrPF=0.0,totalArrPensionContri=0.0,totalArrAAINet=0.0;
       	double grandArrTotEmoluments=0.0,grandArrTotEPF=0.0,grandArrTotVPF=0.0,grandArrTotPrinciple=0.0,grandArrTotInterest=0.0,grandArrempTotal=0.0,grandArrTotPF=0.0,grandArrTotPensionContri=0.0,grandArrTotAAINet=0.0;
       	//for form-4
       	double totalForm4Emoluments=0.0,totalForm4pfStaturary=0.0,totalForm4empVpf=0.0,totalForm4Principle=0.0,totalForm4Interest=0.0,empEmpTotalForm4=0.0,totalForm4PF=0.0,totalForm4PensionContri=0.0,totalForm4AAINet=0.0;
       	double grandForm4TotEmoluments=0.0,grandForm4TotEPF=0.0,grandForm4TotVPF=0.0,grandForm4TotPrinciple=0.0,grandForm4TotInterest=0.0,grandForm4empTotal=0.0,grandForm4TotPF=0.0,grandForm4TotPensionContri=0.0,grandForm4TotAAINet=0.0;
       	
       
	  	
	  	//if(request.getAttribute("summaryList_CHQIAD")!=null){	 
	  	//summaryListCHQIAD=(ArrayList)request.getAttribute("summaryList_CHQIAD");
	  //	}
	  //	if(request.getAttribute("summaryList")!=null){	 
	  //	summaryList=(ArrayList)request.getAttribute("summaryList");
	  	 
	  	if(request.getAttribute("summaryMap")!=null){
	  
		if(status.equals("A")){
		statusType="Active";
		}else if(status.equals("D")){
		statusType="DeActive";
		}else if(status.equals("")){
		statusType="Active & DeActive";
		}								
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "Summary_Form 3("+dispYear+").xls";
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
  	 	<td width="384"  class="reportsublabel">FORM 3 SUMMARY REPORT (<%=statusType%>)</td>
  	 	<td width="87">&nbsp;</td>
    	<td width="272">&nbsp;</td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>    
     <td>&nbsp;</td> 
    <td  align="center" nowrap="nowrap" class="reportsublabel">FOR THE YEAR <font style="text-decoration: underline"><%=dispYear%></td>
    <td>&nbsp;</td>
    <td align="right" nowrap="nowrap" class="Data">Date : <%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>    
  </tr>
  <tr>
    <td colspan="7">&nbsp;</td>
  </tr>  
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr>
       	<td colspan="11" align=left>CPF Recoveries Details :
     	</td>
  	  </tr>
      <tr>
        <td colspan="3">&nbsp;</td>
        <td colspan="5" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td> 
      </tr>
      <tr>
        <td width="4%"  class="label">Region</td>
        <td width="4%"   class="label">Unit Name</td>
        <td width="8%"   class="label">Salary drawn/ paid for CPF deduction Rs. </td>
        <td width="8%"   class="label"><div align="center">EPF</div></td>
        <td width="3%" class="label"><div align="center">VPF</div></td>
        <td width="3%"  ><div align="center" class="label">PF Adv recovery (Principal</div></td>
        <td width="6%"    class="label">PF Adv recovery (Interest) </td>
        <td width="3%"   class="label">TOTAL </td>
        <td width="3%" align="center"   class="label">PF(NET)</td>        
        <td width="3%" class="label"  >PENSION<br/>CONTR. </td>
        <td width="12%" class="label" >Total </td>
      </tr>
        <%
        map=(Map)request.getAttribute("summaryMap");
	  	Set regionset = map.entrySet();
				Iterator itr = regionset.iterator();
       	while (itr.hasNext()) {
				Map.Entry entry = (Map.Entry) itr.next();  
       	list=(ArrayList)entry.getValue();
        String k=entry.getKey().toString();
  		totalEmoluments = Double.parseDouble(list.get(1).toString());
  		totalpfStaturary = Double.parseDouble(list.get(2).toString());
  		totalempVpf = Double.parseDouble(list.get(3).toString());
  		totalPrinciple = Double.parseDouble(list.get(4).toString());
  		totalInterest = Double.parseDouble(list.get(5).toString());
  		empEmpTotal =   totalpfStaturary + totalempVpf + totalPrinciple + totalInterest;
  		totalPF = Double.parseDouble(list.get(6).toString());
  		totalPensionContri = Double.parseDouble(list.get(7).toString());
  		totalAAINet =  totalPF + totalPensionContri;	
  		grandTotEmoluments = grandTotEmoluments+ totalEmoluments;
  		grandTotEPF = grandTotEPF + totalpfStaturary;  		
  		grandTotVPF = grandTotVPF + totalempVpf;
  		grandTotPrinciple = grandTotPrinciple + totalPrinciple;
  		grandTotInterest = grandTotInterest + totalInterest;
  		grandempTotal = grandempTotal + empEmpTotal;
  		grandTotPF = grandTotPF + totalPF;
  		grandTotPensionContri =grandTotPensionContri + totalPensionContri;
  		grandTotAAINet = grandTotAAINet + totalAAINet;
  	%>
	 <tr>
	<%if(k.indexOf("A-")!=-1){%>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <%}else{%>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <%}%>
	 <td width="8%" class="NumData"><%=df1.format(totalEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(totalpfStaturary)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalempVpf)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(totalPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(totalInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(empEmpTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(totalPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(totalPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(totalAAINet)%></td>
	 </tr> 
    <%
    } 
  }
  %>
  <tr>
	 <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td> 
	 <td width="4%" nowrap="nowrap" class="label">Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandempTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandTotAAINet)%></td> 
	 </tr>
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
  <%if(request.getAttribute("summarySupMap")!=null){%>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
       <tr>
       	<td colspan="11" align=left>Supplimentary Details :
     	</td>
  	  </tr>
      <tr>
        <td colspan="3">&nbsp;</td>
        <td colspan="5" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td>   
      </tr>
      <tr>
        <td width="4%"  class="label">Region</td>
        <td width="4%"   class="label">Unit Name</td>
        <td width="8%"   class="label">Salary drawn/ paid for CPF deduction Rs. </td>
        <td width="8%"   class="label"><div align="center"><abbr title="EmpStaturary">EPF</abbr></div></td>
        <td width="3%" class="label"><div align="center">VPF</div></td>
        <td width="3%"  ><div align="center" class="label">PF Adv recovery (Principal</div></td>
        <td width="6%"    class="label">PF Adv recovery (Interest) </td>
        <td width="3%"   class="label">TOTAL </td>
        <td width="3%" align="center"   class="label">PF(NET)</td>        
        <td width="3%" class="label"  >PENSION<br/>CONTR. </td>
        <td width="12%" class="label" >Total </td>  
      </tr>

        <%
        map=(Map)request.getAttribute("summarySupMap");
	  	Set regionset = map.entrySet();
				Iterator itr = regionset.iterator();	
       	while (itr.hasNext()) {
						Map.Entry entry = (Map.Entry) itr.next(); 
       	list=(ArrayList)entry.getValue();
        String k=entry.getKey().toString();
  		totalSupEmoluments = Double.parseDouble(list.get(1).toString());
  		totalSuppfStaturary = Double.parseDouble(list.get(2).toString());
  		totalSupempVpf = Double.parseDouble(list.get(3).toString());
  		totalSupPrinciple = Double.parseDouble(list.get(4).toString());
  		totalSupInterest = Double.parseDouble(list.get(5).toString());
  		empEmpTotalSup =   totalSuppfStaturary + totalSupempVpf + totalSupPrinciple + totalSupInterest;
  		totalSupPF = Double.parseDouble(list.get(6).toString());
  		totalSupPensionContri = Double.parseDouble(list.get(7).toString());
  		totalSupAAINet =  totalSupPF + totalSupPensionContri;
  		grandSupTotEmoluments = grandSupTotEmoluments+ totalSupEmoluments;
  		grandSupTotEPF = grandSupTotEPF + totalSuppfStaturary;  		
  		grandSupTotVPF = grandSupTotVPF + totalSupempVpf;
  		grandSupTotPrinciple = grandSupTotPrinciple + totalSupPrinciple;
  		grandSupTotInterest = grandSupTotInterest + totalSupInterest;
  		grandSupempTotal = grandSupempTotal + empEmpTotalSup;
  		grandSupTotPF = grandSupTotPF + totalSupPF;
  		grandSupTotPensionContri =grandSupTotPensionContri + totalSupPensionContri;
  		grandSupTotAAINet = grandSupTotAAINet + totalSupAAINet;
  	%>
  
	 <tr>
	<%if(k.indexOf("A-")!=-1){%>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <%}else{%>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <%}%>
	 <td width="8%" class="NumData"><%=df1.format(totalSupEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(totalSuppfStaturary)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalSupempVpf)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(totalSupPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(totalSupInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(empEmpTotalSup)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(totalSupPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(totalSupPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(totalSupAAINet)%></td>
	 </tr> 
    <%
    } 
  }
  %>
  <tr>
	 <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td> 
	 <td width="4%" nowrap="nowrap" class="label">Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandSupTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandSupTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandSupTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandSupTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandSupTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandSupempTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandSupTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandSupTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandSupTotAAINet)%></td> 
	 </tr>
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
  <%if(request.getAttribute("summaryArrMap")!=null){%>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
       <tr>
       	<td colspan="11" align=left>Arrear Details :
     	</td>
  	  </tr>
      <tr>
        <td colspan="3">&nbsp;</td>
        <td colspan="5" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td>  
      </tr>
      <tr>
        <td width="4%"  class="label">Region</td>
        <td width="4%"   class="label">Unit Name</td>
        <td width="8%"   class="label">Salary drawn/ paid for CPF deduction Rs. </td>
        <td width="8%"   class="label"><div align="center">EPF</div></td>
        <td width="3%" class="label"><div align="center">VPF</div></td>
        <td width="3%"  ><div align="center" class="label">PF Adv recovery (Principal</div></td>
        <td width="6%"    class="label">PF Adv recovery (Interest) </td>
        <td width="3%"   class="label">TOTAL </td>
        <td width="3%" align="center"   class="label">PF(NET)</td>        
        <td width="3%" class="label"  >PENSION<br/>CONTR. </td>
        <td width="12%" class="label" >Total </td>  
      </tr>
        <%
        map=(Map)request.getAttribute("summaryArrMap");
	  	Set regionset = map.entrySet();
				Iterator itr = regionset.iterator();
       	while (itr.hasNext()) {
		Map.Entry entry = (Map.Entry) itr.next();  
       	list=(ArrayList)entry.getValue();
        String k=entry.getKey().toString();
  		totalArrEmoluments = Double.parseDouble(list.get(1).toString());
  		totalArrpfStaturary = Double.parseDouble(list.get(2).toString());
  		totalArrempVpf = Double.parseDouble(list.get(3).toString());
  		totalArrPrinciple = Double.parseDouble(list.get(4).toString());
  		totalArrInterest = Double.parseDouble(list.get(5).toString());
  		empEmpTotalArr =   totalArrpfStaturary + totalArrempVpf + totalArrPrinciple + totalArrInterest;
  		totalArrPF = Double.parseDouble(list.get(6).toString());
  		totalArrPensionContri = Double.parseDouble(list.get(7).toString());
  		totalArrAAINet =  totalArrPF + totalArrPensionContri;
  		grandArrTotEmoluments = grandArrTotEmoluments+ totalArrEmoluments;
  		grandArrTotEPF = grandArrTotEPF + totalArrpfStaturary;  		
  		grandArrTotVPF = grandArrTotVPF + totalArrempVpf;
  		grandArrTotPrinciple = grandArrTotPrinciple + totalArrPrinciple;
  		grandArrTotInterest = grandArrTotInterest + totalArrInterest;
  		grandArrempTotal = grandArrempTotal + empEmpTotalArr;
  		grandArrTotPF = grandArrTotPF + totalArrPF;
  		grandArrTotPensionContri =grandArrTotPensionContri + totalArrPensionContri;
  		grandArrTotAAINet = grandArrTotAAINet + totalArrAAINet;
  	%>
	 <tr>
	<%if(k.indexOf("A-")!=-1){%>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <%}else{%>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <%}%>
	 <td width="8%" class="NumData"><%=df1.format(totalArrEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(totalArrpfStaturary)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalArrempVpf)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(totalArrPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(totalArrInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(empEmpTotalArr)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(totalArrPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(totalArrPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(totalArrAAINet)%></td>
	 </tr>
    <%
    } 
  }
  %>
  <tr>
	 <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td> 
	 <td width="4%" nowrap="nowrap" class="label">Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandArrTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandArrTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandArrTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandArrTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandArrTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandArrempTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandArrTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandArrTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandArrTotAAINet)%></td>
	 </tr>
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
  

  <%if(request.getAttribute("form4Map")!=null){%>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
       <tr>
       	<td colspan="11" align=left>AAIEPF Form-4 Details :
     	</td>
  	  </tr>
      <tr>
        <td colspan="3">&nbsp;</td>
        <td colspan="5" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td>  
      </tr>
      <tr>
        <td width="4%"  class="label">Region</td>
        <td width="4%"   class="label">Unit Name</td>
        <td width="8%"   class="label">Salary drawn/ paid for CPF deduction Rs. </td>
        <td width="8%"   class="label"><div align="center">EPF</div></td>
        <td width="3%" class="label"><div align="center">VPF</div></td>
        <td width="3%"  ><div align="center" class="label">PF Adv recovery (Principal</div></td>
        <td width="6%"    class="label">PF Adv recovery (Interest) </td>
        <td width="3%"   class="label">TOTAL </td>
        <td width="3%" align="center"   class="label">PF(NET)</td>        
        <td width="3%" class="label"  >PENSION<br/>CONTR. </td>
        <td width="12%" class="label" >Total </td>  
      </tr>
        <%
        map=(Map)request.getAttribute("form4Map");
	  	Set regionset = map.entrySet();
				Iterator itr = regionset.iterator();
       	while (itr.hasNext()) {
		Map.Entry entry = (Map.Entry) itr.next();  
       	list=(ArrayList)entry.getValue();
        String k=entry.getKey().toString();
  		totalForm4Emoluments = Double.parseDouble(list.get(1).toString());
  		totalForm4pfStaturary = Double.parseDouble(list.get(2).toString());
  		totalForm4empVpf = Double.parseDouble(list.get(3).toString());
  		totalForm4Principle = Double.parseDouble(list.get(4).toString());
  		totalForm4Interest = Double.parseDouble(list.get(5).toString());
  		empEmpTotalForm4 =   totalForm4pfStaturary + totalForm4empVpf + totalForm4Principle + totalForm4Interest;
  		totalForm4PF = Double.parseDouble(list.get(6).toString());
  		totalForm4PensionContri = Double.parseDouble(list.get(7).toString());
  		totalForm4AAINet =  totalForm4PF + totalForm4PensionContri;
  		grandForm4TotEmoluments = grandForm4TotEmoluments+ totalForm4Emoluments;
  		grandForm4TotEPF = grandForm4TotEPF + totalForm4pfStaturary;  		
  		grandForm4TotVPF = grandForm4TotVPF + totalForm4empVpf;
  		grandForm4TotPrinciple = grandForm4TotPrinciple + totalForm4Principle;
  		grandForm4TotInterest = grandForm4TotInterest + totalForm4Interest;
  		grandForm4empTotal = grandForm4empTotal + empEmpTotalForm4;
  		grandForm4TotPF = grandForm4TotPF + totalForm4PF;
  		grandForm4TotPensionContri =grandForm4TotPensionContri + totalForm4PensionContri;
  		grandForm4TotAAINet = grandForm4TotAAINet + totalForm4AAINet;
  	%>
	 <tr>
	<%if(k.indexOf("A-")!=-1){%>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <%}else{%>
	 <td width="4%" nowrap="nowrap" class="Data">---</td>
	 <td width="4%" nowrap="nowrap" class="Data"><%=list.get(0) %></td>
	 <%}%>
	 <td width="8%" class="NumData"><%=df1.format(totalForm4Emoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(totalForm4pfStaturary)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalForm4empVpf)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(totalForm4Principle)%></td>
	 <td width="7%" class="NumData"><%=df1.format(totalForm4Interest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(empEmpTotalForm4)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(totalForm4PF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(totalForm4PensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(totalForm4AAINet)%></td>
	 </tr>
    <%
    } 
  }
  %>
  <tr>
	 <td width="4%" nowrap="nowrap" class="Data">&nbsp;</td> 
	 <td width="4%" nowrap="nowrap" class="label">Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandForm4TotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandForm4TotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandForm4TotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandForm4empTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandForm4TotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandForm4TotAAINet)%></td>
	 </tr>
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
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr>
	 <td width="4%" nowrap="nowrap" colspan="2" class="label">CPF Recoveries Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandempTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandTotAAINet)%></td>
	 </tr>
    <tr>
	 <td width="4%" nowrap="nowrap" colspan="2" class="label">Supplimentary Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandSupTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandSupTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandSupTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandSupTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandSupTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandSupempTotal)%></td> 
 	 <td width="8%" class="NumData"><%=df1.format(grandSupTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandSupTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandSupTotAAINet)%></td>
	 </tr>
    <tr>
	 <td width="4%" nowrap="nowrap" colspan="2" class="label">Arrear Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandArrTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandArrTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandArrTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandArrTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandArrTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandArrempTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandArrTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandArrTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandArrTotAAINet)%></td>
	 </tr>
	 <tr>
	 <td width="4%" nowrap="nowrap" colspan="2" class="label">Form-4 Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandForm4TotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandForm4TotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandForm4TotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandForm4empTotal)%></td>
 	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandForm4TotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandForm4TotAAINet)%></td>
	 </tr>
	 <tr>
	 <td width="4%" nowrap="nowrap" colspan="2" class="label"> Grand Totals</td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEmoluments+grandArrTotEmoluments+grandSupTotEmoluments+grandTotEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotEPF+grandArrTotEPF+grandSupTotEPF+grandTotEPF)%></td>
	 <td width="3%" class="NumData"><%=df1.format(grandForm4TotVPF+grandArrTotVPF+grandSupTotVPF+grandTotVPF)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(grandForm4TotPrinciple+grandArrTotPrinciple+grandSupTotPrinciple+grandTotPrinciple)%></td>
	 <td width="7%" class="NumData"><%=df1.format(grandForm4TotInterest+grandArrTotInterest+grandSupTotInterest+grandTotInterest)%></td>
	 <td width="6%" class="NumData"><%=df1.format(grandForm4empTotal+grandArrempTotal+grandSupempTotal+grandempTotal)%></td> 
 	 <td width="8%" class="NumData"><%=df1.format(grandForm4TotPF+grandArrTotPF+grandSupTotPF+grandTotPF)%></td> 
	 <td width="9%" class="NumData"><%=df1.format(grandForm4TotPensionContri+grandArrTotPensionContri+grandSupTotPensionContri+grandTotPensionContri)%></td>
	 <td width="12%" class="NumData"><%=df1.format(grandForm4TotAAINet+grandArrTotAAINet+grandSupTotAAINet+grandTotAAINet)%></td>
	 </tr>
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
</table>
</td>
</tr>

<tr>
  	<td>&nbsp;</td>
  </tr>
    <tr>
  	<td>&nbsp;</td>
  </tr>
  
					
</table>

  </body>
</html>
