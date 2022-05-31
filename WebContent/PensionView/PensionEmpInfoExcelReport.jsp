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
<%@ page import="aims.dao.PensionDAO"%>
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
					<td ><img src="<%=basePath%>PensionView/images/logoani.gif" >
					</td>
					<td>&nbsp;</td>
					<td class="label" align=center valign="top" nowrap="nowrap"><font color='black' size='4' face='Helvetica'>
						AIRPORTS AUTHORITY OF INDIA
						</font></td>
						
					</tr>
					</table>
					</td>
				</tr>
					<%
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename=personal_master.xls");
				try {
				empBean = (aims.bean.EmpMasterBean) session.getAttribute("getSearchBean1");
				} catch (Exception e) {
				System.out.println("Error Message" + e.getMessage());
				}

			int totalData = 0;

			ArrayList dataList = new ArrayList();
			String region="";
			region=empBean.getRegion();
			PensionDAO pdao=new PensionDAO();
			dataList = pdao.retriveByAll(empBean,true);
			
			System.out.println("Size" + dataList.size());
				
					%>
				<tr>
					<td colspan="2">
						<table  border=0 cellpadding=0 cellspacing=0 width="80%" align="center" valign="middle">
						<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
							<td align="right" class="reportlabel">Employee Personnel Information  -  <%=region%></td>
						</tr>
						<tr>
							<td align="right" class="reportlabel"><br/></td>
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
						<table width="100%"  border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
							<tr>
								<td class="label" >Serial</br> Number</td>
								<td class="label">CPF ACC No </td>
								<td class="label">Region </td>
								<td class="label" >AirportCode</td>
								<td class="label" >Employee</br> Number</td>
								<td class="label">Employee Name</td>
								<td class="label" >DateofBirth</td>
								<td class="label" > DateofJoining</td>
								<td class="label" >Father's /Husband's Name</td>
								<td class="label" >PensionOption  <br/></td>
								<td class="label" >Designation</td>
								<td class="label" >Gender</td>
								<td class="label" >Division</td>
								
								<td class="label" nowrap="nowrap">Remarks  </td>
								</tr>
						
				<%
				if (dataList.size() != 0) {
				int count = 0;
				String cpfacno="",airportCode = "", empName = "", salary = "", desig = "",  employeeCode = "";
				String pensionOption="", dateofBirth="", remarks="",lastActive=""; 
				String srno="",dateofJoining="",fhName="",gender="",division="";
				
				for(int k=0;k<dataList.size();k++){
					count++;
					aims.bean.EmpMasterBean beans = (aims.bean.EmpMasterBean) dataList.get(k);
					srno=String.valueOf(count);
					empName = beans.getEmpName();
					airportCode = beans.getStation();
					desig = beans.getDesegnation();
					employeeCode = beans.getEmpNumber();
					cpfacno =beans.getCpfAcNo();
					fhName=beans.getFhName();
					dateofBirth=beans.getDateofBirth();
					pensionOption=beans.getWetherOption();
					remarks =beans.getRemarks();
					 dateofJoining=beans.getDateofJoining();
					  division=beans.getDivision();
					  gender = beans.getSex();
					  region=beans.getRegion();
					//lastActive=beans.getDateofJoining();
				%>
				
		
			
							<tr>
								
								<td class="Data" width="2%"><%=srno%></td>
								<td class="Data" width="12%"><%=cpfacno%></td>
								<td class="Data" width="15%"><%=region%></td>
								<td class="Data" width="12%"><%=airportCode%></td>
								<td class="Data" width="12%"><%=employeeCode%></td>
								<td class="Data" width="20%"><%=empName%></td>
								<td class="Data" width="12%"><%=dateofBirth%></td>
								<td class="Data" width="20%"> <%=dateofJoining%></td>
								<td class="Data" width="20%"> <%=fhName%></td>
								<td class="Data" width="15%"><%=pensionOption%></td>
								<td class="Data" width="10%"> <%=desig%></td>
								<td class="Data" width="10%"> <%=gender%></td>
								<td class="Data" width="10%"> <%=division%></td>
								<td class="Data" width="35%"> <%=remarks%></td>
								
							</tr>
							
					
						<%}}%>
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