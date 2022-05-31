
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,aims.common.*,aims.bean.*" %>


<%@ page import="java.text.DecimalFormat"%>
<%@ page import="aims.bean.StationWiseRemittancebean"%>
<jsp:directive.page import="aims.bean.EmployeePersonalInfo"/>
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
  				
  				
				//if (request.getAttribute("searchList") != null) {
		
				ArrayList dataList = new ArrayList();
				
				
				
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
				<td  class="reportlabel" align="left" >SUMMARY REPORT FOR FRESH OPTION INTERFACE </td>
				
				
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
	<% if(!region.trim().equals("All")) { %>
	<td width="15%" class="reportlabel" align="center"><b> <%=region%> </b></td>
	
	<%}else{ %>
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
					
					String fileName = "SummaryReport.xls";
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
			<td nowrap class="label">	
			S No		
			</td>
			<td nowrap class="label">	
			REGION		
			</td>
			<td class="label">			
			TOTAL PFIDS WITH </br>EXISTING OPTION: A
			</td>
			<td class="label">			
			CHANGED FROM A To A
			</td>
			<td class="label">			
			CHANGED FROM A To B
			</td>
			<td class="label">			
			BALANCE PFIDS TO BE</br> UPDATED
			</td>
			
			</tr>
			<% 
			
			if(request.getAttribute("dataList")!=null) {
				dataList = (ArrayList)request.getAttribute("dataList");
				if(dataList.size()==0) {						
				
			}
			else {
			int j=1;
				for(int i=0;i<dataList.size();i++) {
					EmployeePersonalInfo personal =(EmployeePersonalInfo) dataList.get(i);
					int balancepfid=Integer.parseInt(personal.getOptiona())-
					(Integer.parseInt(personal.getA2a())+Integer.parseInt(personal.getA2b()));
					//System.out.println("aaaaaaaa"+balancepfid);
					%>
						<tr>
				<td class="Data" >
				<%=j++ %>
				</td>
				<td class="Data" >
				<%= personal.getRegion() %>
				</td>
				<td class="Data" >
				<%= personal.getOptiona() %>
				</td>
				<td class="Data" >
				<%= personal.getA2a() %>
				</td>
				<td class="Data" >
				<%= personal.getA2b() %>
				</td>
				<td class="Data" >
				<%= balancepfid %>
				</td>
				
			</tr>
				<%}
				}
			}
			
			 %>
			
		
				
	</table>
	
	</td>
	</tr>
	
	</table>




 
 </body>
 </html>
