<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>
<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<jsp:useBean id="empBean" class="aims.bean.EmpMasterBean" scope="request">
	<jsp:setProperty name="empBean" property="*" />
</jsp:useBean>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
					%>
<%@ page import="javax.sound.sampled.DataLine"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
<script type="text/javascript">
   
</script>
</HEAD>
<body>
<form action="method">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
					<table  border=0 cellpadding=3 cellspacing=0 width="40%" align="center" valign="middle">
					<tr>
					<td ><img src="<%=basePath%>PensionView/images/logoani.gif" width="65" height="70">
					</td>
					<td class="label" align=center valign="top" nowrap="nowrap"><font color='black' size='4' face='Helvetica'>
						AIRPORTS AUTHORITY OF INDIA
						</font></td>
						
					</tr>
					</table>
					</td>
				</tr>
					<%
					String reportType="";
			 if(request.getAttribute("reportType")!=null){
					reportType=(String)request.getAttribute("reportType");
					if(reportType.equals("Excel Sheet") || reportType.equals("ExcelSheet")){
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename=form-3_report.xls");
					}
								
					}	
			int totalData = 0;

			ArrayList availableList = new ArrayList();
			ArrayList unAvailableList = new ArrayList();
			String region="";
			region=empBean.getRegion();
			availableList = (ArrayList)request.getAttribute("availableList");
			unAvailableList =(ArrayList)request.getAttribute("unAvailableList");
			
					
					%>
				<tr>
					<td colspan="2">
						<table  border=0 cellpadding=0 cellspacing=0 width="100%" align="center" valign="middle">
						<tr>
							<td align="center" class="reportlabel">Employee Personnel Information  -  <%=region%></td>
						</tr>
						<tr>
							<td align="center" class="reportlabel"><br/></td>
						</tr>
				
						</table>
					</td>
				</tr>
					
				
				<tr>
					<td>
						<table width="100%"  border="0" cellpadding="0" cellspacing="0">
						
						<tr>
							<td >
								<table width="100%"  border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
									</tr>
									 </table>
							</td>
						</tr>
						
											
						</table>
					</td>
					
					
				</tr>
					
				<tr>
					<td>
						<table  border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
							<tr>
								<td class="label" >Serial</br> Number</td>
								<td class="label">CPF ACC No </td>
								<td class="label">Region </td>
								<td class="label" >AirportCode</td>
								<td class="label" >Employee</br> Number</td>
								<td class="label">Employee Name</td>
								<td class="label" >DateofBirth</td>
								<td class="label" >Father's /Husband's Name</td>
								<td class="label" >PensionOption  <br/></td>
								<td class="label" >Designation</td>
								<td class="label" >Gender</td>
								</tr>
						
				<%
				if (availableList.size() != 0) {
				out.println("<html>");
				out.println("<font color='red'><b>");
				out.println("FinaceDataAvailable List In PersonnelMaster");
				out.println("</b></font>");
				int count = 0;
				String cpfacno="",airportCode = "", empName = "", salary = "", desig = "",  employeeCode = "";
				String pensionOption="", dateofBirth="", remarks="",lastActive=""; 
				String srno="",dateofJoining="",fhName="",gender="",division="";
				
				for(int k=0;k<availableList.size();k++){
					count++;
					aims.bean.PensionBean beans = (aims.bean.PensionBean) availableList.get(k);
					srno=String.valueOf(count);
					empName = beans.getEmployeeName();
					airportCode = beans.getAirportCode();
					desig = beans.getDesegnation();
					employeeCode = beans.getEmployeeCode();
					cpfacno =beans.getCpfAcNo();
					fhName=beans.getFHName();
					dateofBirth=beans.getDateofBirth();
					pensionOption=beans.getPensionOption();
				//	remarks =beans.getRemarks();
				//	 dateofJoining=beans.getDateofJoining();
					 gender = beans.getSex();
					 region=beans.getRegion();
					// division=beans.getDivision();
					//lastActive=beans.getDateofJoining();
				%>
								<tr>
								<td class="Data" width="2%"><%=srno%></td>
								<td class="Data" width="12%"><%=cpfacno%></td>
								<td class="Data" width="20%"><%=region%></td>
								<td class="Data" width="12%"><%=airportCode%></td>
								<td class="Data" width="12%"><%=employeeCode%></td>
								<td class="Data" width="20%"><%=empName%></td>
								<td class="Data" width="20%"><%=dateofBirth%></td>
								<td class="Data" width="20%"> <%=fhName%></td>
								<td class="Data" width="15%"><%=pensionOption%></td>
								<td class="Data" width="10%"> <%=desig%></td>
								<td class="Data" width="10%"> <%=gender%></td>
															
							</tr>
							
					
						<%}} if(unAvailableList.size() > 0){
				out.println("<tr><td colspan='10' align='left'>");	
				out.println("<font color='red'><b>");
				out.println("FinaceData UnAvailable List In PersonnelMaster");
				out.println("</b></font>");
				out.println("</td></tr>");
				int count = 0;
				String cpfacno="",airportCode = "", empName = "",desig = "",  employeeCode = "";
				String pensionOption="", dateofBirth="";
				String srno="",fhName="",gender="";
				for(int l=0;l<unAvailableList.size();l++){
					count++;
					aims.bean.PensionBean beans = (aims.bean.PensionBean) unAvailableList.get(l);
					srno=String.valueOf(count);
					empName = beans.getEmployeeName();
					airportCode = beans.getAirportCode();
					desig = beans.getDesegnation();
					employeeCode = beans.getEmployeeCode();
					cpfacno =beans.getCpfAcNo();
					fhName=beans.getFHName();
					dateofBirth=beans.getDateofBirth();
					pensionOption=beans.getPensionOption();
				//	remarks =beans.getRemarks();
				//	 dateofJoining=beans.getDateofJoining();
					 gender = beans.getSex();
					 region=beans.getRegion();
					// division=beans.getDivision();
					//lastActive=beans.getDateofJoining();
				%>
				
		
			
							<tr>
								
								<td class="Data" width="2%"><%=srno%></td>
								<td class="Data" width="12%"><%=cpfacno%></td>
								<td class="Data" width="20%"><%=region%></td>
								<td class="Data" width="12%"><%=airportCode%></td>
								<td class="Data" width="12%"><%=employeeCode%></td>
								<td class="Data" width="20%"><%=empName%></td>
								<td class="Data" width="20%"><%=dateofBirth%></td>
								<td class="Data" width="20%"> <%=fhName%></td>
								<td class="Data" width="15%"><%=pensionOption%></td>
								<td class="Data" width="10%"> <%=desig%></td>
								<td class="Data" width="10%"> <%=gender%></td>
															
							</tr>
							
					
						<%}
						
						}%>
						</table>
						
						</td>
						</tr>
						<tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
						</tr>
						
						
						<tr>
							
						</tr>
						
				
				
			</table>
		</form>
</body>
</html>