<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.EPFFormsReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
</head>

<body>
<%
String reportType="",fileName="",filePrefix="",filePostfix="",Year="2012";
	CommonUtil commonUtil=new CommonUtil();
	 
	String username = session.getAttribute("userid").toString();
if(request.getAttribute("empBean")!=null){
	EmpMasterBean empBean =null;
 empBean=(EmpMasterBean)request.getAttribute("empBean"); 
if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAIEPF-2_"+empBean.getPfid();
					 
					fileName=filePrefix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			String form2id=empBean.getForm2id();
			//out.println(form2id);
			
%>
<table width="1351" border="0">
  <tr>
    <td width="110">&nbsp;</td>
    <td width="66">&nbsp;</td>
    <td width="90">&nbsp;</td>
    <td width="52">&nbsp;</td>
    <td width="99">&nbsp;</td>
    <td width="17">&nbsp;</td>
    <td width="117">&nbsp;</td>
    <td width="25">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="35">&nbsp;</td>
    <td width="212">&nbsp;</td>
    <td colspan="3">&nbsp;</td>
    <td width="20">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="8">&nbsp;</td>
    <td width="3">&nbsp;</td>
    <td width="241">&nbsp;</td>
  </tr>
  <tr>
    <td>
    <table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
       
        <td width="556" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif"  width="75" height="55" align="right" /></td>
    <td nowrap="nowrap" rowspan="2">AIRPORTS AUTHORITY OF INDIA</td>
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
    <td colspan="19"><table width="1351"  cellspacing="0" cellpadding="0">
      <tr>
        <td height="24" colspan="19" align="center">EMPLOYEES PROVIDENT FUND</td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
    </table></td>
    
  </tr>
 
  
   <tr>
    <td colspan="19"><table width="1351"  cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td height="24" colspan="19"  align="center">SCHEDULE OF PRIOR PERIOD    ADJUSTMENT IN OPEING BALANCE AS ON 1st April&nbsp;  <%=Year%></td>
      </tr>
    </table></td>
    
    
    
    
  </tr>
 
  <tr>
    <td colspan="25"><table width="1351" cellspacing="0" cellpadding="0" border="0">
      <tr>
		
         <td colspan="16" align="right">Form no: AAIEPF-2</td>
          <td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>
      </tr>
    </table></td>    
  </tr>
  <tr>
    <td colspan="25"><table width="1351" border="1" cellpadding="0" cellspacing="0">
      <tr>
        <td  rowspan="2"><div align="center">Sl No</div></td>
        <td  rowspan="2"><div align="center">PF ID</div></td>
        <td  rowspan="2"><div align="center">CPF A/c No (Old)</div></td>
        <td  rowspan="2"><div align="center">Emp No</div></td>
        <td  rowspan="2"><div align="center">Name of the Member </div></td>
        <td  rowspan="2"><div align="center">Desig</div></td>
        <td  rowspan="2"><div align="center">Father's Name/ Husband name in case of Married women</div></td>
        <td  rowspan="2"><div align="center">Date of birth</div></td>
        <td  rowspan="2"><div align="center">Period of adjustment</div></td>
        <td  rowspan="2"><div align="center">Emoluments Amount</div></td>
        <td colspan="3"><div align="center">Adjustment In opening balances Subscription </div></td>
        <td colspan="3"><div align="center">Adjustment in opening Balances Employer Contribution </div></td>
        <td  rowspan="2"><div align="center">Adjustment in Pension Contri </div></td>
        <td  rowspan="2"><div align="center">Adjustment in CPF Adv outstanding Balance </div></td>
        <td rowspan="2"><div align="center">Station </div></td>
         <td  rowspan="2"><div align="center">User</div></td>
         <td  rowspan="2"><div align="center">Form2ID</div></td>
         <td  rowspan="2"><div align="center">JVNO</div></td>
        <td  rowspan="2"><div align="center">Remarks</div></td>
         <td  rowspan="2"><div align="center">Flag</div></td>
      </tr>
      <tr>
        <td width="79"><div align="center">Subscription amount</div></td>
        <td width="65"><div align="center">Interest thereon</div></td>
        <td width="51"><div align="center">Total       (10+11)</div></td>
        <td width="79"><div align="center">Contribution amount</div></td>
        <td width="89"><div align="center">interest thereon</div></td>
        <td width="106"><div align="center">Total    (13+14)</div></td>
        </tr>
      <tr>
        <td><div align="center">1</div></td>
        <td><div align="center">2</div></td>
        <td><div align="center">2A</div></td>
        <td><div align="center">3</div></td>
        <td><div align="center">4</div></td>
        <td><div align="center">5</div></td>
        <td><div align="center">6</div></td>
        <td><div align="center">7</div></td>
        <td><div align="center">8</div></td>
        <td><div align="center">9</div></td>
        <td><div align="center">10</div></td>
        <td><div align="center">11</div></td>
        <td><div align="center">12</div></td>
        <td><div align="center">13</div></td>
        <td><div align="center">14</div></td>
        <td><div align="center">15</div></td>
        <td><div align="center">16</div></td>
        <td><div align="center">17</div></td>
        <td><div align="center">18</div></td>
        <td><div align="center">19</div></td>
        <td><div align="center">20</div></td>
        <td><div align="center">21</div></td>
        <td><div align="center">22</div></td>
        <td><div align="center">23</div></td>
      </tr>
      
         <%
         
        int slno=1; 
        %>
        
          <tr>
        <td class="Data"><%=slno%></td>
        <td class="Data"><%=empBean.getPfid()%></td>        
        <td class="Data" nowrap="nowrap"><%=empBean.getCpfAcNo() %></td>
        <td class="Data"><%=empBean.getEmpNumber() %></td>
        <td class="Data"><%=empBean.getEmpName() %></td>
        <td class="Data"><%=empBean.getDesegnation() %></td>
        <td class="Data"><%=empBean.getFhName() %></td>
        <td class="Data" nowrap="nowrap"><%=empBean.getDateofBirth()%></td>
        <td class="Data" nowrap="nowrap"> <%=empBean.getReportYear()%></td>
        <td class="Data">&nbsp;</td>
        <td class="NumData"> <%=empBean.getEmpSubTot()%></td>
        <td class="NumData"><%=empBean.getEmpSubInt()%></td>
         <%if(empBean.getEmpSubTot().equals("") && empBean.getEmpSubInt().equals("")){%>  
        <td class="NumData">&nbsp;</td>
        <%}else{%>
       <td class="NumData"><%=(Double.parseDouble(empBean.getEmpSubTot())+Double.parseDouble(empBean.getEmpSubInt())) %></td>
        <%}%>
        <td class="NumData"><%=empBean.getAaiContriTot() %></td>
        <td class="NumData"><%=empBean.getAaiContriInt() %></td>
        <%if(empBean.getAaiContriTot().equals("") && empBean.getAaiContriInt().equals("")){%>
        <td class="Data">&nbsp;</td>
        <%}else{%>
        <td class="NumData"><%=(Double.parseDouble(empBean.getAaiContriTot())+Double.parseDouble(empBean.getAaiContriInt())) %></td>
        <%}%>
        <%if(empBean.getPensionTot().equals("") && empBean.getPensionInt().equals("")){%>
        <td class="Data">&nbsp;</td>
        <%}else{%>
        <td class="NumData"><%=(Integer.parseInt(empBean.getPensionTot())+Integer.parseInt(empBean.getPensionInt())) %></td>
        <%}%>
        <td class="Data">&nbsp;</td>
        <td class="Data">&nbsp;</td> 
        <td class="Data"><%=username%></td>
        <td class="NumData"><%=empBean.getForm2id()%></td>
        <td class="NumData"><%=empBean.getJvNo() %></td>  
         <td class="Data"><%=empBean.getRemarks() %></td>
           <td class="Data">&nbsp;</td>
      </tr>
        
      
        <%
        }else{
        
       %>
       <tr>
       <td calspan="15"><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td align="center"> No Records found </td></tr></table> 
       </td>
       </tr>
       
        
    <%}%>
    
    </table></td>
  </tr>
   
  
  
 
</table>
</body>
</html>
 