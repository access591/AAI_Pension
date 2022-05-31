
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
  <%
  ArrayList cardReportList=new ArrayList();
  	String reportType="",fileName="",filePrefix="",filePostfix="";
    double totEmpsub=0,totEmpcontri=0;
  		DecimalFormat df1 = new DecimalFormat("#########0");
  	%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="100" height="50" /></td>
        <td>AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
    
    <br/>
        <td width="38%">&nbsp;</td>
        <td width="55%">EMPLOYEES PROVIDENT FUND</td>
      </tr>
	     <tr>
        <td colspan="3" align="center">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="3" align="center" class="reportlabel">Annual Statement of Contribution and Subscription Region wise from Members during the Year  <font style="text-decoration: underline"><%=request.getAttribute("dspYear")%></font></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">            
      
    </table></td>
  </tr>
    <tr>
    <td>&nbsp;</td>
  </tr>
  
   <%
	
  	
  	if(request.getAttribute("cardList")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  	cardReportList=(ArrayList)request.getAttribute("cardList");
  	
  	
		
	
  %>
  <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        
  	  <tr>
        <td width="13%" align="center" class="label">Region </td>
        <td width="25%" align="center" class="label">Station </td>
        
       	 <td width="30%" align="center" class="label">Employee Subscripiton </td>
          <td width="30%" align="center" class="label"> Employeer Contribution </td>
         <td width="34%" align="center" class="label">Remarks</td>
      </tr>   
    


  <% 
AaiEpfForm11Bean epfForm11Bean=new AaiEpfForm11Bean();
int cnt = 0;
for(int cardList=0;cardList<cardReportList.size();cardList++){

epfForm11Bean=(AaiEpfForm11Bean)cardReportList.get(cardList);
if(epfForm11Bean.getRegion().equals("CHQIAD")){
cnt++;
}
  		
  %>
    
       
   
   <tr ><%if(cnt==1 && epfForm11Bean.getRegion().equals("CHQIAD") ||(!epfForm11Bean.getRegion().equals("CHQIAD"))){%>
        <td width="13%" align="center" class="Data"><%=epfForm11Bean.getRegion()%></td>
        <%}else{%>
        <td width="13%" align="center" class="Data">&nbsp;</td>
        <%}%>
        <td width="25%" align="center" class="Data"><%=epfForm11Bean.getAirportCode()%>&nbsp;</td>
        <td width="30%" align="center" class="NumData"><%=epfForm11Bean.getObEmpSub()%></td>
        <td width="30%" align="center" class="NumData"><%=epfForm11Bean.getObAAISub()%></td>           
        <td width="34%" align="center" class="NumData"><%=epfForm11Bean.getRemarks()%></td>     
      </tr>
      
      <%totEmpsub=totEmpsub+Double.parseDouble(epfForm11Bean.getObEmpSub());
        totEmpcontri=totEmpcontri+Double.parseDouble(epfForm11Bean.getObAAISub());
      }%>
      <%}%>
 
     
        
      </tr>
     
  <tr>
 
	 <td width="15%" align="center"  class="label">Grand Total</td>	
	 <td class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="30%" align="center"  class="NumData"><%=df1.format(totEmpsub)%></td>
	 <td width="30%" align="center"  class="NumData"><%=df1.format(totEmpcontri)%></td>
	  <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 	 
 </tr>
  
</table>
</body>
</html>




	   
  
