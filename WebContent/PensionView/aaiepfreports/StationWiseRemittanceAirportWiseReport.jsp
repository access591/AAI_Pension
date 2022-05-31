
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,aims.common.*" %>


<%@ page import="java.text.DecimalFormat"%>
<%@ page import="aims.bean.StationWiseRemittancebean"%>
<%
	String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		CommonUtil common=new CommonUtil();    

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

function hideTip(thi)
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
<%			String region="",monthyear="",month="",year="";
  				if (request.getAttribute("region") != null) {
  					region=request.getAttribute("region").toString();
  					}
  				if (request.getAttribute("monthYear") != null) {
  					monthyear=request.getAttribute("monthYear").toString();
  					}	
  				
				if (request.getAttribute("stationList") != null) {
		
				ArrayList stationList = new ArrayList();
				
				stationList = (ArrayList) request.getAttribute("stationList");
		
				if (stationList.size() != 0) {
				month=monthyear.substring(3,6);
				year=monthyear.substring(7,11);
				DecimalFormat df = new DecimalFormat("#########0");
				%>


<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td colspan="2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="10%">&nbsp;</td>
				<td width="5%" rowspan="1" align="center"><img
					src="<%=basePath%>PensionView/images/logoani.gif" width="100"
					height="50" /></td>
					
				<td width="20%" nowrap align="left" class="reportlabel">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  AIRPORTS AUTHORITY OF INDIA</td>
				
			</tr>
			
			<tr>
				
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td  class="reportlabel" align="left" >Station Wise Remittance(with PC code) Report For the Month of <U><%=month%></U> in <U><%=year%></U></td>
				
				
			</tr>
			<tr>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				
				
			</tr>

		</table>
		</td>
	</tr>
	<tr>
	<% if(!region.trim().equals("All")){%>
	<td width="15%" class="reportlabel" align="center"><b> <%=region%> </b></td>
	
	<%}else{%>
	<td width="15%" class="reportlabel" align="center"></td>

	<%}%>
	<td width="85%" colspan="5" class="Data" align="right" >Date:<%=common.getCurrentDate("dd-MMM-yyyy HH:mm:ss")%></td>
	<td width="3%"></td>
	</tr>
	
	<tr>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td>
		
	

	
	<tr>
		<td></td>
	</tr>
	
	


<tr>

	<td>
	<table align="center">
		<tr>
			<td colspan="3"></td>
			<td colspan="2" align="right">
			<%if(request.getAttribute("reportType")!=null){
			String	reportType=(String)request.getAttribute("reportType");
			
				
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
					
					String fileName = "StationWiseRemittanceReport(PCCODE).xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}%>
			</td>
		</tr>
	</table>
</tr>
<tr>
	<td colspan="2">
	<table width="100%" align="center" cellpadding=2 
			cellspacing="0" border="1" >
			

	
			<tr>
			<td width="4%" rowspan="2" class="label">S.No</td>
			<td width="4%"  rowspan="2" class="label">AIRPORT</td>
			<td width="4%"  rowspan="2" class="label">PCCODE</td>
			<td width="4%"  colspan="6" class="label" align="center">PF ACCRETION</td>
			<td width="4%"  colspan="4" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  colspan="4" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%"  rowspan="2" class="label">ADDITIONAL CONTRI</td>
			
			</tr>
			
			<tr>
				<td width="4%"  class="label" align="center">EPF</td>
			<td width="4%"   class="label" align="center">VPF</td>
			<td width="4%"   class="label" align="center">REFUND_ADV</td>
			<td width="4%"   class="label" align="center">INTEREST</td>
				<td width="4%"   class="label" align="center">AAI CONTRI</td>
			<td width="4%"  class="label" align="center">TOTAL</td>
			
			<td width="4%" rowspan="1"  class="label" align="center">CPF</td>
			<td width="4%" rowspan="1"   class="label" align="center">ARREAR</td>
			<td width="4%"  rowspan="1" class="label" align="center">SUPPLI</td>
			<td width="4%" rowspan="1"  class="label" align="center">TOTAL</td>
			<td width="4%" rowspan="1"  class="label" align="center">CPF</td>
			<td width="4%"  rowspan="1"  class="label" align="center">ARREAR</td>
			<td width="4%"  rowspan="1" class="label" align="center">SUPPLI</td>
			<td width="4%" rowspan="1"  class="label" align="center">TOTAL</td>
			
			</tr>
			<tr>
			
		
			
			</tr>

				<%
				long pf=0,insp=0,pc=0,addPC=0;
				long cpfEpf=0,cpfVpf=0,cpfRefAdv=0,cpfAdvInt=0,aaiPc=0,cpfTotal=0,aEpf=0,aVpf=0,aRefAdv=0,aAdvInt=0,aPc=0,aTotal=0,sEpf=0,sVpf=0,sRefAdv=0,sAdvInt=0,sPc=0,sTotal=0;
				long cpfInsp=0,aInsp=0,sInsp=0;
				long cpfPc=0,arrearPc=0,suppliPc=0;
				StationWiseRemittancebean bean= new StationWiseRemittancebean();
				  for (int i = 0; i < stationList.size(); i++) {
				   bean= (StationWiseRemittancebean) stationList.get(i);
				  %>
				 <span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
			<tr>
			<td  class="NumData"><%=(i+1)%></td>
			<td class="Data"><%=bean.getStation() %></td>
			<td class="Data"><%=bean.getPccode() %></td>
			<td class="NumData"><%=Long.parseLong(bean.getCpfEpf())+Long.parseLong(bean.getAEpf())+Long.parseLong(bean.getSEpf()) %></td>
			<td class="NumData"><%=Long.parseLong(bean.getCpfVpf())+Long.parseLong(bean.getAVpf())+Long.parseLong(bean.getSVpf())  %></td>
			<td class="NumData"><%=Long.parseLong(bean.getCpfRefAdv())+Long.parseLong(bean.getARefAdv())+Long.parseLong(bean.getSRefAdv())  %></td>
			<td class="NumData"><%=Long.parseLong(bean.getCpfAdvInt())+Long.parseLong(bean.getAAdvInt())+Long.parseLong(bean.getSAdvInt())  %></td>
			<td class="NumData"><%=Long.parseLong(bean.getAaipc())+Long.parseLong(bean.getApc())+Long.parseLong(bean.getSpc())  %></td>
			
			

			
			
			<td class="NumData"><%=df.format(bean.getPfTotal())  %></td>
			<td class="NumData"><%=bean.getCpfInspCharges() %></td>
			<td class="NumData"><%=bean.getArrearInspCharges() %></td>
			<td class="NumData"><%=bean.getSuppliInspCharges()%></td>
			<td class="NumData"><%=df.format(bean.getInspTotal()) %></td>
			<td class="NumData"><%=bean.getCpfPc()%></td>
			<td class="NumData"><%=bean.getArrearPc()%></td>
			<td class="NumData"><%=bean.getSuppliPc() %></td>
			<td class="NumData"><%=df.format(bean.getPcTotal())  %></td>
			<td class="NumData"><%=df.format(bean.getAddContriTotal())  %></td>
			
			
			</tr>
			
			<%
			pf=pf+(long)bean.getPfTotal();
			insp=insp+(long)bean.getInspTotal();
			pc=pc+(long)bean.getPcTotal();
			addPC=addPC+(long)bean.getAddContriTotal();
			
			cpfEpf=cpfEpf+Long.parseLong(bean.getCpfEpf());
			cpfVpf=cpfVpf+Long.parseLong(bean.getCpfVpf());
			cpfRefAdv=cpfRefAdv+Long.parseLong(bean.getCpfRefAdv());
			cpfAdvInt=cpfAdvInt+Long.parseLong(bean.getCpfAdvInt());
			aaiPc=aaiPc+Long.parseLong(bean.getAaipc());
			cpfTotal=cpfTotal+Long.parseLong(bean.getCpfTotal());
			
			aEpf=aEpf+Long.parseLong(bean.getAEpf());
			aVpf=aVpf+Long.parseLong(bean.getAVpf());
			aRefAdv=aRefAdv+Long.parseLong(bean.getARefAdv());
			aAdvInt=aAdvInt+Long.parseLong(bean.getAAdvInt());
			aPc=aPc+Long.parseLong(bean.getApc());
			aTotal=aTotal+Long.parseLong(bean.getATotal());
			
			sEpf=sEpf+Long.parseLong(bean.getSEpf());
			sVpf=sVpf+Long.parseLong(bean.getSVpf());
			sRefAdv=sRefAdv+Long.parseLong(bean.getSRefAdv());
			sAdvInt=sAdvInt+Long.parseLong(bean.getSAdvInt());
			sPc=sPc+Long.parseLong(bean.getSpc());
			sTotal=sTotal+Long.parseLong(bean.getSTotal());
			
			cpfPc=cpfPc+Long.parseLong(bean.getCpfPc());
			arrearPc=arrearPc+Long.parseLong(bean.getArrearPc());
			suppliPc=suppliPc+Long.parseLong(bean.getSuppliPc());
			
			
			cpfInsp=cpfInsp+Long.parseLong(bean.getCpfInspCharges());
			aInsp=aInsp+Long.parseLong(bean.getArrearInspCharges());
			sInsp=sInsp+Long.parseLong(bean.getSuppliInspCharges());
			
			}
			%>
			<tr>
			<td  class="NumData" colspan="3">Totals</td>
			<td class="NumData"><B><%=cpfEpf+aEpf+sEpf %></B></td>
			<td class="NumData"><B><%=cpfVpf+aVpf+sVpf %></B></td>
			<td class="NumData"><B><%=cpfRefAdv+aRefAdv+sRefAdv %></B></td>
			<td class="NumData"><B><%=cpfAdvInt+aAdvInt+sAdvInt %></B></td>
			<td class="NumData"><B><%=aaiPc+aPc+sPc %></B></td>
			
			
			<td class="NumData"><B><%=pf %></B></td>
			<td class="NumData"><B><%=cpfInsp %></B></td>
			<td class="NumData"><B><%=aInsp %></B></td>
			<td class="NumData"><B><%=sInsp %></B></td>
			<td class="NumData"><B><%=insp %></B></td>
			<td class="NumData"><B><%=cpfPc %></B></td>
			<td class="NumData"><B><%=arrearPc %></B></td>
			<td class="NumData"><B><%=suppliPc %></B></td>
			<td class="NumData"><B><%=pc %></B></td>
			<td class="NumData"><B><%=addPC %></B></td>
			
			</tr>
		
				
	</table>
	<td/>
	</tr>
	</table>



 <%}}%>
 
 </body>
 </html>
