
<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page
	import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.PensionBean,aims.bean.StationWiseRemittancebean,aims.bean.EmpMasterBean"%>
<%
	String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		CommonUtil common=new CommonUtil();    

  %>
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
				%>

		

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>AAI</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
</head>
<body>
<form>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td colspan="2">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="7%">&nbsp;</td>
				<td width="5%" rowspan="1"><img
					src="<%=basePath%>PensionView/images/logoani.gif" width="100"
					height="50" /></td>
				<td width="20%" nowrap align="left" class="reportlabel">AIRPORTS AUTHORITY OF INDIA</td>
			</tr>
			
			<tr>
				<td width="7%">&nbsp;</td>
				<td colspan="2" class="reportlabel">Station Wise Remittance Report For the Month of <U><%=month%></U> in <U><%=year%></U></td>
			</tr>
			<tr>
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
	<td width="85%" colspan="6" class="Data" align="right">Date:<%=common.getCurrentDate("dd-MMM-yyyy HH:mm:ss")%></td>
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
			cellspacing="0" border="1"  bordercolor ="gray">

			<tr>
			<td class="label">&nbsp;
			</td>
			<td class="label">&nbsp;
			</td>
			<td class="label" colspan="12" align="center">UPLOADED VALUES IN EPIS 

			</td>
			
			<td class="label" colspan="6" align="center">USER ENTRIES FOR REGION 

			</td>
			
			
			</tr>
			<tr>
			<td width="4%"  rowspan="2" class="label">S.No</td>
			<td width="4%"  rowspan="2" class="label">AIRPORT</td>
			<td width="4%" colspan="4" class="label" align="center">PF ACCRETION</td>
			<td width="4%" colspan="4" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%" colspan="4" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%" colspan="2" class="label" align="center">PF ACCRETION</td>
			<td width="4%" colspan="2" class="label" align="center">INSPECTION CHARGES</td>
			<td width="4%" colspan="2" class="label" align="center">PENSION CONTRIBUTION</td>
			<td width="4%" colspan="2" class="label" align="center">ADDITIONAL CONTRIBUTION</td>
			
			</tr>
			<tr>
	
			<td width="4%"  class="label">CPF</td>
			<td width="4%"  class="label">SUPPLI</td>
			<td width="4%"  class="label">ARREAR</td>
			<td width="4%"  class="label">TOTAL</td>
			<td width="4%"  class="label">CPF</td>
			<td width="4%"  class="label">SUPPLI</td>
			<td width="4%"  class="label">ARREAR</td>
			<td width="4%"  class="label">TOTAL</td>
			<td width="4%"  class="label">CPF</td>
			<td width="4%"  class="label">SUPPLI</td>
			<td width="4%"  class="label">ARREAR</td>
			<td width="4%"  class="label">TOTAL</td>
			
			<td width="8%"  class="label" > Remit Date</td>
			<td width="4%"  class="label">Amount</td>
			<td width="8%"  class="label" > Remit Date</td>
			<td width="4%" class="label">Amount</td>
			<td width="8%"  class="label"> Remit Date</td>
			<td width="4%" class="label">Amount</td>
			
			<tr>
				<%
				long diffPc=0,diffPcTotal=0,pcTotal=0,upLoadPcTotal=0,noOfemp=0,upLoadnoOfemp=0;
				StationWiseRemittancebean bean= new StationWiseRemittancebean();
				  for (int i = 0; i < stationList.size(); i++) {
				   bean= (StationWiseRemittancebean) stationList.get(i);
				  %>
			<tr>
			<td  class="NumData"><%=(i+1)%></td>
			<td class="Data"><%=bean.getStation() %></td>
			<td class="NumData"><%=bean.getCpfPf() %></td>
			<td class="NumData"><%=bean.getSuppliPf() %></td>
			<td class="NumData"><%=bean.getArrearPf() %></td>
			<td class="NumData"><%=bean.getPfTotal() %></td>
			<td class="NumData"><%=bean.getCpfInspCharges() %></td>
			<td class="NumData"><%=bean.getSuppliInspCharges()%></td>
			<td class="NumData"><%=bean.getArrearInspCharges() %></td>
			<td class="NumData"><%=bean.getInspTotal() %></td>
			<td class="NumData"><%=bean.getCpfPc() %></td>
			<td class="NumData"><%=bean.getSuppliPc()%></td>
			<td class="NumData"><%=bean.getArrearPc() %></td>
			<td class="NumData"><%=bean.getPcTotal() %></td>
			<%if(bean.getPfRemitDate()!=""){%>
			<td class="NumData"><%=bean.getPfRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			
			<td class="NumData"><%=bean.getPf() %></td>
				<%if(bean.getInspremitDate()!=""){%>
			<td class="NumData"><%=bean.getInspremitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
			
			<td class="NumData"><%=bean.getInspCharges() %></td>
				<%if(bean.getPcRemitDate()!=""){%>
			<td class="NumData"><%=bean.getPcRemitDate() %></td>
			<%}else{%>
			<td class="Data">---</td>
			<%}%>
	
			<td class="NumData"><%=bean.getPc() %></td>
			<td class="NumData"></td>
			
			
			
			</tr>
			
			<%}%>
			
		
				
	</table>
	<td/>
	</tr>
	</table>

</form>
</body>
 </html>
 <%}}%>