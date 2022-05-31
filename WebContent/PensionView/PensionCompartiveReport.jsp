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
			dataList = pdao.compartiveRetriveByAll(empBean,region);
			
			System.out.println("Size" + dataList.size());
				
					%>
				<tr>
					<td colspan="2">
						<table  border=0 cellpadding=0 cellspacing=0 width="100%" align="center" valign="middle">
						<tr>
							<td align="center" class="reportlabel">Comparative Statement -  <%=region%></td>
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
						<table width="100%"  border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
							<tr>
								
								<td class="label" >Serial</br> Number</td>
								<td class="label">CPF ACC No </td>
								<td class="label" >AirportCode</td>
								<td class="label" >Employee</br> Code</td>
								<td class="label">Employee Name</td>
								<td class="label" >Designation</td>
								<td class="label" >DateofBirth</td>
								<td class="label" > DateofJoining</td>
								<td class="label" >Sex  <br/></td>
								<td class="label" >F/H Name</td>	
						
							</tr>
						
				<%
				if (dataList.size() != 0) {
				int count = 0;
				String cpfacno="",airportCode = "", empName = "", salary = "", desig = "",  employeeCode = "";
				String fhname="", dateofBirth="", sex=""; 
				String srno="",dateofJoining="";
				
				for(int k=0;k<dataList.size();k++){
					count++;
					aims.bean.EmpMasterBean beans = (aims.bean.EmpMasterBean) dataList.get(k);
					srno=String.valueOf(count);
					cpfacno= beans.getCpfAcNo();
					empName = beans.getEmpName();
					airportCode = beans.getStation();
					desig = beans.getDesegnation();
					employeeCode = beans.getEmpNumber();					
					dateofBirth=beans.getDateofBirth();
				    sex=beans.getSex();
					fhname =beans.getFhName();
					dateofJoining=beans.getDateofJoining();
					//lastActive=beans.getDateofJoining();
				%>
				
		
			
							<tr>
								
								<td class="Data" width="2%"><%=srno%></td>
								<td class="Data" width="12%"><%=cpfacno%></td>
								<td class="Data" width="12%"><%=airportCode%></td>
								<td class="Data" width="12%"><%=employeeCode%></td>
								<td class="Data" width="20%"><%=empName%></td>
								<td class="Data" width="10%"> <%=desig%></td>
								<td class="Data" width="12%"><%=dateofBirth%></td>
								<td class="Data" width="35%"> <%=dateofJoining%></td>
								<td class="Data" width="15%"><%=sex%></td>								
								<td class="Data" width="35%"> <%=fhname%></td>
								
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