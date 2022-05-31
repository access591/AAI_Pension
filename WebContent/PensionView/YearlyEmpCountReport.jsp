<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
<title>AAI</title>
</head>

<body>
<%
String reportType="",fileName="",filePrefix="",filePostfix="";
if(request.getAttribute("empCountList")!=null){
if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAI_EPF-1_Report";
					fileName=filePrefix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
%>
<table width="700" border="0" align="center">
  <tr>
    <td width="5">&nbsp;</td>
    <td width="1">&nbsp;</td>
    <td width="20">&nbsp;</td>
    <td width="135">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="38">&nbsp;</td>
    <td width="240">&nbsp;</td>
    <td width="14">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td colspan="2"><table cellspacing="0" cellpadding="0">
      <tr>
        <td height="21" colspan="2" width="177">&nbsp;</td>
      </tr>
    </table></td>
    <td width="360">&nbsp;</td>
  </tr>
  
  <tr>
    <td colspan="7" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif"  width="75" height="55" align="right" /></td>
    <td nowrap="nowrap" rowspan="2">AIRPORTS AUTHORITY OF INDIA</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>

  </tr>
  <tr>
    <td nowrap="nowrap"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
 
  </tr>
  
  <tr>
    <td colspan="13"><table cellspacing="0" cellpadding="0">
      <tr>
        <td height="24" colspan="8" width="420" align="right">Yearly Employee Count Report</td>
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
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="271">&nbsp;</td>

  </tr>
 
  <tr>
    <td colspan="13"><table width="700" border="1"  cellpadding="0" cellspacing="0" align="center">
      <tr>
        <td width="47"  valign="top"><table cellspacing="0" cellpadding="0">
          <col width="48" />
          <tr>
            <td width="48" valign="top" class="reportsublabel"><div align="center">Sl No</div></td>
          </tr>
          <tr> </tr>
        </table></td>
        <td   valign="top" class="reportsublabel"><div align="center">PF ID</div></td>        
        <td   valign="top" class="reportsublabel"><div align="center">Financial Year</div></td>        
        <td   valign="top" class="reportsublabel" align="center" >Airport Code</td>
        <td  valign="top" class="reportsublabel"><div align="center">Region</div></td>
        <td  valign="top" class="reportsublabel"><div align="center">Total Records</div></td>
      </tr>
    
      <%
          DecimalFormat df = new DecimalFormat("#########0");
        ArrayList empCountList=new ArrayList();
        FinacialDataBean finBean=new FinacialDataBean();
        
        
        empCountList=(ArrayList)request.getAttribute("empCountList");
        System.out.println("--------empCountList Size in JSP----------"+empCountList.size());
        int slno=1;
       
        
        for(int i=0;i<empCountList.size();i++){
        
        finBean=(FinacialDataBean)empCountList.get(i);        
        System.out.println("------Pension no--------"+finBean.getPensionNumber());        
        %>
         <tr>
        <td class="Data" ><%=slno%></td>
        <td class="Data" align="center"><%=finBean.getPensionNumber()%></td>         
        <td class="Data" align="center"><%=finBean.getMonthYear()%></td>             
        <td class="Data" align="center"><%=finBean.getAirportCode()%></td>
        <td class="Data" align="center"><%=finBean.getRegion()%></td>
        <td class="Data" align="center"><%=finBean.getRemarks()%></td>
       
      </tr>
        
        <%      
        slno++;
        }        
        %>
            
      
    </table>
      </td>
  </tr>
  <%}else{
        
        %>
        
          <table align="center" width="100%">
          <tr>
          <td align="center">
          <strong>No Records Found</strong>
          </td>
          </tr>
          </table>
        <%
        }
      
      %>
 
 
</table>
</body>
</html>
