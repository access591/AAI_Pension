<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>
<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="aims.bean.CompartiveReportForm3DataBean"%>
<%@ page import="aims.bean.CompartiveReportAaiDataBean"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>

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
					<td colspan="3">
					<table  border=0 cellpadding=3 cellspacing=0 width="40%" align="center" valign="middle">
					<tr>
					<td align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="65" height="70">
					</td>				
					<td class="label" align=center valign="top" nowrap="nowrap"><font color='black' size='4' face='Helvetica'>
						AIRPORTS AUTHORITY OF INDIA
						</font></td>
						
					</tr>
					</table>
					</td>
				</tr>
					<%
					String flag="";
                    CompartiveReportBean CompartiveReportBean=new CompartiveReportBean();
                 	CompartiveReportForm3DataBean compartiveReportform3info=new CompartiveReportForm3DataBean();
		            CompartiveReportAaiDataBean compartiveReportAaiinfo=new CompartiveReportAaiDataBean();

					FinancialService fs=new FinancialService();
					String reg=request.getParameter("region");
					String fileName = "Comaparative-Statement-report.xls";

					response.setContentType("application/vnd.ms-excel");

					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
					CompartiveReportBean=(CompartiveReportBean)fs.getPensionValidateData(reg);

					ArrayList commonList=new ArrayList();
					ArrayList commonAAIList=new ArrayList();
	                ArrayList AAIList=new ArrayList();
					ArrayList pensionList=new ArrayList();
					
                	commonList=CompartiveReportBean.getCommonList();
					AAIList=CompartiveReportBean.getAaiList();
                     if(!(CompartiveReportBean.getCommonAaiList()).equals("[]")){	
				      	commonAAIList=CompartiveReportBean.getCommonAaiList();	
					}
					pensionList=CompartiveReportBean.getForm3List();   
					flag=CompartiveReportBean.getFlag();
				  %>
				<tr>				    
					<td colspan="3">
						<table  border=0 cellpadding=0 cellspacing=0 width="100%" align="center" valign="middle">
						<tr>
							<td align="center" class="reportlabel">Comparative Statement For Month Of  August 2007</td>
							
						</tr>
						<tr>
							
							<td align="center" class="reportlabel"><%=reg%></td>
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
					<td colspan="3">
						<table width="100%"  border="2"  bordercolor="gray" cellspacing="0">
							<tr >													
								<td class="label">S.No </td>		
								<td class="label">Type </td>
								<td class="label">CPF ACC No </td>	
								<td class="label">Employee No</td>	
								<td class="label">Employee Name </td>	
								<td class="label">Designation </td>	
								<td class="label">Airport Code</td>	
								
								</tr>
						
				<%int count = 1,count1=0,count2=0;
				if (commonList.size() != 0) {
	
				for(int k=0;k<commonList.size();k++){
					
					if(flag.equals("pension"))
					{
					 
					    compartiveReportform3info= (CompartiveReportForm3DataBean) commonList.get(k);                       
					    compartiveReportAaiinfo=(CompartiveReportAaiDataBean)commonAAIList.get(k);

					}else if(flag.equals("aai"))
	                {
						compartiveReportAaiinfo= (CompartiveReportAaiDataBean) commonList.get(k);                       
					    compartiveReportform3info=(CompartiveReportForm3DataBean)commonAAIList.get(k);

					}
					else if(flag.equals("equal")) 
	                {             
	                   	compartiveReportform3info= (CompartiveReportForm3DataBean) commonList.get(k);                       
					    compartiveReportAaiinfo=(CompartiveReportAaiDataBean)commonAAIList.get(k);
					}
				%>
				            
							<tr>	
							    <td  width="10%" rowspan="2"><%=count%></td>
								<td class="Data" width="10%">Form3</td>
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3CPFaccno()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3EmpNo()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3EmpName()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3Designation()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3AirportCode()%></td>	
							</tr>
							<tr>
							 
								<td class="Data" width="10%"><%=reg%> Aug 2007</td>
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiCPFaccno()%></td>		
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiEmpNo()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiEmpName()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAai3Designation()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiAirportCode()%></td>		
							</tr>
							<tr>
							<td colspan="7">&nbsp;</td>
							</tr>
					
						<%
						count++;
						}}%>
						</table>
					</td>
					</tr>

					<tr>
						<td>&nbsp;</td>
					</tr>

					<tr>
				<td valign="top">
	
					<table width="100%"  border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
					<tr>
					<td colspan="6"  class="label" align="center">FORM3  Non Sync Data
					</td>
					</tr>
							<tr>												
							     <td class="label">S.No </td>		
							    <td class="label">CPF ACC No </td>
								<td class="label">Employee No</td>	
								<td class="label">Employee Name </td>	
								<td class="label">Designation </td>
								<td class="label">Airport Code</td>	
							
								</tr>
						
				<%
				if (pensionList.size() != 0) {
			
				count1=count;
				for(int k=0;k<pensionList.size();k++){
					
										
					 compartiveReportform3info=(CompartiveReportForm3DataBean)pensionList.get(k);
					
				%>
				
							<tr>							
						        <td class="Data" width="10%"><%=count1%></td>
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3CPFaccno()%></td>
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3EmpNo()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3EmpName()%></td>	
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3Designation()%></td>
								<td class="Data" width="10%"><%=compartiveReportform3info.getForm3AirportCode()%></td>
								
							</tr>							
					
						<%
						count1++;
						}}%>
						</table>
						
						</td>
						<td>&nbsp;</td>
						<td valign="top">
							<table width="100%"  border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
							<tr>
					<td colspan="6"  class="label" align="center"><%=reg%> Aug 2007  Non Sync Data
					</td>
					</tr>
							<tr>													
							     <td class="label">S.No </td>	
								<td class="label">CPF ACC No </td>
								<td class="label">Employee No</td>	
								<td class="label">Employee Name </td>	
								<td class="label">Designation </td>	
								<td class="label">Airport Code</td>	
								
								
							</tr>
						
				<%
				if (AAIList.size() != 0) {			
			
				count2=count;
				
				for(int k=0;k<AAIList.size();k++){
										
					 compartiveReportAaiinfo=(CompartiveReportAaiDataBean)AAIList.get(k);					
				%>
				
							<tr>				
							    <td class="Data" width="10%"><%=count2%></td>
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiCPFaccno()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiEmpNo()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiEmpName()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAai3Designation()%></td>	
								<td class="Data" width="10%"><%=compartiveReportAaiinfo.getAaiAirportCode()%></td>	
							</tr>
					
						<%
						count2++;
						}}%>
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