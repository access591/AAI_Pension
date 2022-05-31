
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
        <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
      <tr>
        <td width="38%">&nbsp;</td>
        <td width="55%" class="reportlabel">Final Payments Pending</td>
 
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
    	List cardReportList=new ArrayList();
    	
    	
  	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="";
  	
  	

 %>


    <%
		if(request.getAttribute("paymentlist")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAI_Final_Payments_Pending_Report";
					fileName=filePrefix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  	cardReportList=(List)request.getAttribute("paymentlist");
  	
		
	
  %>
  <tr>
  	<td>&nbsp;</td>
  </tr>
    <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
     
        <td width="3%"  class="reportsublabel">Sl.No</td>
        <td width="8%"  class="reportsublabel">PF ID </td>
		<td width="12%"  class="reportsublabel">Name of the Member </td>
       	<td width="4%"  class="reportsublabel" nowrap="nowrap">Date of birth</td>
		<td width="4%" " class="reportsublabel" nowrap="nowrap">Date Of Joining</td>
        <td width="4%"  class="reportsublabel">Option</td>
        <td  align="center" class="reportsublabel">Interest Rate <br/>calucaltion upto</td>
        <td  align="center" class="reportsublabel">OB Date </td>
        <td align="center"  class="reportsublabel">Employee Subscription</td>
        <td align="center"  class="reportsublabel">Employer Contribution</td>
        <td align="center"  class="reportsublabel">Activity/Deactivity</td>
      </tr>
     
  
     <tr></tr>
     <%
     	FinalPaymentsBean epfForm3Bean=new FinalPaymentsBean();
     	int srlno=0;
     	String activeInfo="";
     	for(int cardList=0;cardList<cardReportList.size();cardList++){
		epfForm3Bean=(FinalPaymentsBean)cardReportList.get(cardList);
		srlno++;
		if(epfForm3Bean.getActivity().equals("N")){
		activeInfo="Inactive";
		}else{
		activeInfo="Active";
		}
		
		
     %>
      <tr>
        <td  class="NumData"><%=srlno%></td>
        <td class="Data"><%=epfForm3Bean.getPensionno()%></td>
       
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEmployeename()%></td>
       
        
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getDateofbirth()%></td>
       <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getDateofjoining()%></td>
       <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getWetheroption()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getFinalsettlemntdate()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getObYear()%></td>
         <td class="NumData" ><%=epfForm3Bean.getEmpnetob()%></td>
         <td class="NumData" ><%=epfForm3Bean.getAainetob()%></td>
         <td class="NumData" ><%=activeInfo%></td>
      </tr>
	<%
	
	
	}%>
	
   
      
	 <tr>
	  	<td colspan="20" class="Data">&nbsp;</td>
  	</tr>

    

    </table></td>
  </tr>
  <%}%>
  
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
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
