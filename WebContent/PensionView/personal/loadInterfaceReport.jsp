
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
				<td  class="reportlabel" align="left" > Master Report For Fresh Pension Option</td>
				
				
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
					
					String fileName = "FreshpensionOptionMasterReport.xls";
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
			<td class="label">			
			PF ID
			</td>
			<td class="label">			
			UAN No
			</td>
			<td class="label">			
			Employee Code
			</td>
			<td class="label">			
			Employee Name
			</td>
			<td class="label">			
			Father/Husband's Name
			</td>
			<td class="label">			
			Date Of Birth
			</td>
			<td class="label">			
			Date Of Joining
			</td>
			<td class="label">			
			Wether Option
			</td>
			<td class="label">			
			Fresh Option
			</td>
			<td class="label">			
			AirportCode
			</td>
			<td class="label">			
			Region
			</td>
			</tr>
			<% 
			
			if(request.getAttribute("dataList")!=null) {
				dataList = (ArrayList)request.getAttribute("dataList");
				if(dataList.size()==0) {						
				%>
				<tr>
				<td class="label" colspan="11" align="center">
				<b> No Records Found </b>
				</td>
				</tr>
				<% 
			}
			else {
			int j=1;
				for(int i=0;i<dataList.size();i++) {
					EmployeePersonalInfo personal =(EmployeePersonalInfo) dataList.get(i);
					if(personal.isFreshOPFalg()){
				%>
				<tr bgcolor="#FFCCCC">
				<td class="Data" >
				<%=j++ %>
				</td>
				<td class="Data" >
				<%= personal.getPensionNo() %>
				</td>
				<td class="Data" >
				<%= personal.getUanno() %>
				</td>
				<td class="Data" >
				<%= personal.getEmployeeNumber() %>
				</td>
				<td class="Data">
				<%= personal.getEmployeeName() %>
				</td>
				<td class="Data">
				<%= personal.getFhName() %>
				</td>
				<td class="Data">
				<%= personal.getDateOfBirth() %>
				</td>
				<td class="Data">
				<%= personal.getDateOfJoining() %>
				</td>
				<td class="Data">
				<%= personal.getWetherOption() %>
				</td>
				<td class="Data">
				<%=personal.getFreshPensionOption()  %>
				</td>
				
			<td class="Data">
				<%= personal.getAirportCode() %>
				</td>
				<td class="Data">
				<%=personal.getRegion()%>
				</td>
			</tr>
				<%
				}else{ %>
						<tr>
				<td class="Data" >
				<%=j++ %>
				</td>
				<td class="Data" >
				<%= personal.getPensionNo() %>
				</td>
				<td class="Data" >
				<%= personal.getUanno() %>
				</td>
				<td class="Data" >
				<%= personal.getEmployeeNumber() %>
				</td>
				<td class="Data">
				<%= personal.getEmployeeName() %>
				</td>
				<td class="Data">
				<%= personal.getFhName() %>
				</td>
				<td class="Data">
				<%= personal.getDateOfBirth() %>
				</td>
				<td class="Data">
				<%= personal.getDateOfJoining() %>
				</td>
				<td class="Data">
				<%= personal.getWetherOption() %>
				</td>
				<td class="Data">
				<%= personal.getFreshPensionOption() %>
				</td>
				
			<td class="Data">
				<%= personal.getAirportCode() %>
				</td>
				<td class="Data">
				<%=personal.getRegion()%>
				</td>
			</tr>
				<%}
				}
			}
			}
			 %>
			
		
				
	</table>
	
	</td>
	</tr>
	
	</table>




 
 </body>
 </html>
