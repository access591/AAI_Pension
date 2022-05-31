
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
				<td  class="reportlabel" align="left" >Station Wise Remittance Report For the Month of <U><%=month%></U> in <U><%=year%></U></td>
				
				
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
					
					String fileName = "StationWiseRemittanceReport.xls";
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
			<td class="label">&nbsp;
			</td>
			<td class="label">&nbsp;
			</td>
			<td class="label" colspan="4" align="center" >UPLOADED ENTRIES IN EPIS 

			</td>
			
			<td class="label" colspan="6" align="center"> ENTRIES BY STATION 

			</td>
			<td class="label" colspan="4" align="center"> COMPARING UPLOADED/USER ENTRIES  

			</td>
			<td class="label" colspan="6" align="center">ENTRIES BY CHQ 

			</td>
			<td class="label" colspan="3" align="center"> COMPARING STATION/CHQ

			</td>
			
			
			</tr>
			<tr>
			<td width="4%"  rowspan="2" class="label">S.No</td>
			<td width="4%"  rowspan="2" class="label">AIRPORT</td>
			<td width="4%"  rowspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%" rowspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  rowspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%"  rowspan="2" class="label" align="center">ADDITIONAL CONTRIBUTION</td>
			<td width="4%"  colspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%"  colspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  colspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%"  rowspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%"  rowspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  rowspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="8%"  rowspan="2" class="label" align="center">NOTES</td>
			<td width="4%"  colspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%"  colspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  colspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%"  rowspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%"  rowspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%"  rowspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			</tr>
			<tr>
			<td width="8%"   class="label" > Date Of Remittance</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Date Of Remittance</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Date Of Remittance</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Date Of Receipt</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Date Of Receipt</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Date Of Receipt</td>
			<td width="4%"  class="label">Amount</td>
			
			</tr>
			

				<%
				long pf=0,insp=0,pc=0,addPC=0,regionPf=0,regionInsp=0,regionPc=0,chqPf=0,chqInsp=0,chqPc=0,regionPfDiff=0,regionInspDiff=0,regionPcDiff=0;
				long chqPfDiff=0,chqInspDiff=0,chqPcDiff=0;
				StationWiseRemittancebean bean= new StationWiseRemittancebean();
				  for (int i = 0; i < stationList.size(); i++) {
				   bean= (StationWiseRemittancebean) stationList.get(i);
				  %>
				 <span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
			<tr>
			<td  class="NumData"><%=(i+1)%></td>
			<td class="Data"><%=bean.getStation() %></td>
			<td class="NumData"><%=df.format(bean.getPfTotal())  %></td>
			<td class="NumData"><%=df.format(bean.getInspTotal()) %></td>
			<td class="NumData"><%=df.format(bean.getPcTotal())  %></td>
			<td class="NumData"><%=df.format(bean.getAddContriTotal())  %></td>
			<%if(bean.getPfRemitDate()!=""){%>
			<td nowrap class="NumData"><%=bean.getPfRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getPf()%></td>
			<%if(bean.getInspremitDate()!=""){%>
			<td nowrap class="NumData"><%=bean.getInspremitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getInspCharges() %></td>
			<%if(bean.getPcRemitDate()!=""){%>
			<td nowrap class="NumData"><%=bean.getPcRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getPc() %></td>
			
			<%if((long)bean.getPfTotal()-Long.parseLong(bean.getPf())!=0){%>
			<td class="NumData"><font color="red" ><%=((long)bean.getPfTotal()-Long.parseLong(bean.getPf()))%></font></td>
			<%}else{%>
			<td class="NumData"><%=((long)bean.getPfTotal()-Long.parseLong(bean.getPf()))%></td>
			<%}%>
			<%if(((long)bean.getInspTotal()-Long.parseLong(bean.getInspCharges()))!=0){%>
			<td class="NumData"><font color="red" ><%=((long)bean.getInspTotal()-Long.parseLong(bean.getInspCharges())) %></font></td>
			<%}else{%>
			<td class="NumData"><%=((long)bean.getInspTotal()-Long.parseLong(bean.getInspCharges())) %></td>
			<%}%>
			<%if(((long)bean.getPcTotal()-Long.parseLong(bean.getPc()))!=0){%>
			<td class="NumData"><font color="red" ><%=((long)bean.getPcTotal()-Long.parseLong(bean.getPc())) %></font></td>
			<%}else{%>
			<td class="NumData"><%=((long)bean.getPcTotal()-Long.parseLong(bean.getPc())) %></td>
			<%}%>
			
			
			<%if(!bean.getNotes().equals("---")){%>
			
			<td width="2%" class="Data"  onmouseover="showTip('<%=bean.getNotes()%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=bean.getNotes().substring(0,5)+"..." %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<%if(bean.getChqPfRemitDate()!=""){%>
			<td nowrap class="NumData"><%=bean.getChqPfRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getChqPf() %></td>
			<%if(bean.getChqInspremitDate()!=""){%>
			<td  nowrap class="NumData"><%=bean.getChqInspremitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getChqInspCharges()%></td>
				<%if(bean.getChqPcRemitDate()!=""){%>
			<td  nowrap class="NumData"><%=bean.getChqPcRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			<td class="NumData"><%=bean.getChqPc()%></td>
			<%if((Long.parseLong(bean.getPf())-Long.parseLong(bean.getChqPf()))!=0){%>
			<td class="NumData"><font color="red" ><%=(Long.parseLong(bean.getPf())-Long.parseLong(bean.getChqPf()))%></font></td>
			<%}else{%>
			<td class="NumData"><%=(Long.parseLong(bean.getPf())-Long.parseLong(bean.getChqPf()))%></td>
			<%}%>
			<%if((Long.parseLong(bean.getInspCharges())-Long.parseLong(bean.getChqInspCharges()))!=0){%>
			<td class="NumData"><font color="red" ><%=(Long.parseLong(bean.getInspCharges())-Long.parseLong(bean.getChqInspCharges())) %></font></td>
			<%}else{%>
			<td class="NumData"><%=(Long.parseLong(bean.getInspCharges())-Long.parseLong(bean.getChqInspCharges())) %></td>
			<%}%>
			<%if((Long.parseLong(bean.getPc())-Long.parseLong(bean.getChqPc()))!=0){%>
			<td class="NumData"><font color="red" ><%=(Long.parseLong(bean.getPc())-Long.parseLong(bean.getChqPc())) %></font></td>
			<%}else{%>
			<td class="NumData"><%=(Long.parseLong(bean.getPc())-Long.parseLong(bean.getChqPc())) %></td>
			<%}%>

			
			
			</tr>
			
			<%
			pf=pf+(long)bean.getPfTotal();
			insp=insp+(long)bean.getInspTotal();
			pc=pc+(long)bean.getPcTotal();
			addPC=addPC+(long)bean.getAddContriTotal();
			regionPf= regionPf+Long.parseLong(bean.getPf());
			regionInsp=regionInsp+Long.parseLong(bean.getInspCharges());
			regionPc=regionPc+Long.parseLong(bean.getPc());
			chqPf=chqPf+Long.parseLong(bean.getChqPf());
			chqInsp=chqInsp+Long.parseLong(bean.getChqInspCharges());
			chqPc=chqPc+Long.parseLong(bean.getChqPc());
			
			
			regionPfDiff=regionPfDiff+((long)bean.getPfTotal()-Long.parseLong(bean.getPf()));
			regionInspDiff=regionInspDiff+((long)bean.getInspTotal()-Long.parseLong(bean.getInspCharges()));
			regionPcDiff=regionPcDiff+((long)bean.getPcTotal()-Long.parseLong(bean.getPc()));
			chqPfDiff=chqPfDiff+(Long.parseLong(bean.getPf())-Long.parseLong(bean.getChqPf()));
			chqInspDiff=chqInspDiff+(Long.parseLong(bean.getInspCharges())-Long.parseLong(bean.getChqInspCharges()));
			chqPcDiff=chqPcDiff+(Long.parseLong(bean.getPc())-Long.parseLong(bean.getChqPc()));
			
			
			}
			%>
			<tr>
			<td  class="NumData" colspan="2">Totals</td>
		
			<td class="NumData"><B><%=pf %></B></td>
			<td class="NumData"><B><%=insp %></B></td>
			<td class="NumData"><B><%=pc %></B></td>
			<td class="NumData"><B><%=addPC %></B></td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=regionPf %></B></td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=regionInsp %></B></td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=regionPc %></B></td>
			<%if(regionPfDiff!=0){%>
			<td class="NumData"><font color="red"><B><%=regionPfDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><B><%=regionPfDiff %></B></td>
			<%}%>
			<%if(regionInspDiff!=0){%>
			<td class="NumData"><font color="red"><B><%=regionInspDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><B><%=regionInspDiff %></B></td>
			<%}%>
			<%if(regionPcDiff!=0){%>
			<td class="NumData"><font color="red"><B><%=regionPcDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><B><%=regionPcDiff %></B></td>
			<%}%>
			
			
			
			<td class="Data">---</td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=chqPf %></B></td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=chqInsp %></B></td>
			<td class="Data">---</td>
			<td class="NumData"><B><%=chqPc %></B></td>
			<%if(chqPfDiff!=0){%>
			<td class="NumData"><font color="red"><B><%=chqPfDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><%=chqPfDiff %></td>
			<%}%>
			<%if(chqInspDiff!=0){%>
			<td class="NumData"><font color="red"><B><%=chqInspDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><B><%=chqInspDiff %></td>
			<%}%>
			<%if(chqPcDiff!=0){%> 
			<td class="NumData"><font color="red"><B><%=chqPcDiff %></B></font></td>
			<%}else{%>
			<td class="NumData"><B><%=chqPcDiff %></B></td>
			<%}%>
			
			</tr>
		
				
	</table>
	<td/>
	</tr>
	</table>



 <%}}%>
 
 </body>
 </html>
