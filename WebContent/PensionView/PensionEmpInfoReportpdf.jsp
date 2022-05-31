<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>
<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.service.PensionService"%>
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
<html>
	<head>
		<title>PensionFund Report</title>
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
		
		<%try {
				empBean = (aims.bean.EmpMasterBean) session.getAttribute("getSearchBean1");

			} catch (Exception e) {
				System.out.println("Error Message" + e.getMessage());
			}

			int totalData = 0;

			ArrayList dataList = new ArrayList();
			String region="";
			region=empBean.getRegion();
			PensionService pservice=new PensionService();
			dataList=pservice.retriveByAll(empBean,true);
//			dataList = empBean.retriveByAll(true);
			
			System.out.println("Size" + dataList.size());
			//totalData = dbBean.totalData("select count(*) as count from emp_pension");
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4.rotate(), 0, 0, 50, 50);

			PdfWriter writer = PdfWriter.getInstance(document, response
					.getOutputStream());
			//PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
			document.open();
			int headerwidths1[] = { 10 ,10, 10, 10, 20,10, 10, 20, 10,10};
			Font HEADLINE_FONT1 = new Font(Font.HELVETICA, 8, Font.BOLD,
					new Color(0, 0, 128));
			Font HEADLINE_FONT = new Font(Font.HELVETICA, 8, Font.BOLD,
					new Color(0, 0, 128));
			Font DATA_HEAD_FONT = new Font(Font.HELVETICA, 18, Font.BOLD,
					new Color(0, 0, 128));
			Font DATA_HEAD_FONT1 = new Font(Font.HELVETICA, 15, Font.NORMAL,
					new Color(0, 0, 128));
			Font DATA_HEAD_FONT2 = new Font(Font.HELVETICA, 8, Font.NORMAL,
					new Color(0, 0, 128));
			Font DATA_HEAD_FONT3 = new Font(Font.HELVETICA, 10, Font.BOLD,
					new Color(0, 0, 128));
			Font TEXT_FONT = new Font(Font.HELVETICA, 7, Font.NORMAL,
					Color.black);
			Font TEXT_FONT1 = new Font(Font.HELVETICA, 8, Font.NORMAL,
					new Color(0, 0, 128));
			Font TEXT_FONT2 = new Font(Font.HELVETICA, 7, Font.NORMAL,
					new Color(0, 0, 128));
			Font FOOTER_FONT = new Font(Font.TIMES_ROMAN, 5, Font.NORMAL,
					Color.black);

			//main table
			PdfPTable maint = new PdfPTable(1);
			maint.getDefaultCell().setBorderColor(new Color(0, 0, 128));
			maint.setWidthPercentage(90);
			maint.getDefaultCell().setBorderWidth(0.0f);
			maint.setHeaderRows(1);

			
			
			
			PdfPTable opaddt = new PdfPTable(1);
			opaddt.setWidthPercentage(60);
		//	opaddt.setWidths(headerwidths1);

			PdfPCell htc = new PdfPCell(new Phrase("Employee Personal Info Report By  "+region,
					DATA_HEAD_FONT));
			 out.println("<br>");		
			
		//	java.awt.Image image=null;
			
			
		//	imagePath="PensionView/images/logoani.gif" ;
			
			
		    htc.setBorderWidth(0);
			htc.setPadding(5);
			opaddt.addCell(htc);
			document.add(opaddt);

			PdfPTable headers = new PdfPTable(10);
			headers.setWidthPercentage(90);
			headers.setWidths(headerwidths1);
			
			PdfPCell headerdatacell = new PdfPCell(new Phrase("SrNO",
					DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);

			headers.addCell(headerdatacell);
			
			 headerdatacell = new PdfPCell(new Phrase("CPFACNO",
					DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);

			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("AirportCode", DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("EmployeeCode", DATA_HEAD_FONT2));
		//	headerdatacell.setBorderColor(new Color(0, 0, 0));
		//	headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("EmployeeName", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("DateofBirth", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("PensionOption", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);
			
			

			headerdatacell = new PdfPCell(new Phrase("Designation", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Last Active", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Remarks", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 0));
			headerdatacell.setBorderWidth(0.1f);
			
			headers.addCell(headerdatacell);



			htc.setBorderWidth(0);
			htc.setPadding(5);
			
			//Data table     
			PdfPTable t = new PdfPTable(10);
			t.setWidthPercentage(90);
			t.getDefaultCell().setPadding(9);
			t.setWidths(headerwidths1);
			
			
			document.add(headers);
			document.add(maint);
			if (dataList.size() != 0) {
				int count = 0;
				String cpfacno="",airportCode = "", empName = "", salary = "", desig = "",  employeeCode = "";
				String pensionOption="", dateofBirth="", remarks="",lastActive=""; 
				String srno="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					aims.bean.EmpMasterBean beans = (aims.bean.EmpMasterBean) dataList.get(i);
					srno=String.valueOf(count);
					empName = beans.getEmpName();
					airportCode = beans.getStation();
					desig = beans.getDesegnation();
					employeeCode = beans.getEmpNumber();
					cpfacno =beans.getCpfAcNo();
					
					dateofBirth=beans.getDateofBirth();
					pensionOption=beans.getWetherOption();
					remarks =beans.getRemarks();
					lastActive=beans.getLastActive();
					
			System.out.println("emoName "+empName+"cpfacno "+cpfacno +"dateofBirth "+dateofBirth +" pensionOption" + pensionOption + " remarks " +remarks);

					PdfPCell datacell1 = new PdfPCell(new Phrase(srno,
							TEXT_FONT));
                                        
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(cpfacno,
							TEXT_FONT));
                                        
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(airportCode, TEXT_FONT));

					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(employeeCode, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(empName, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					//datacell1.setFixedWidth(25);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(dateofBirth, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(pensionOption, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
								   
				
					
					datacell1 = new PdfPCell(new Phrase(desig, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(lastActive, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(remarks, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 0));
					datacell1.setBorderWidth(0.1f);
					
					
					t.addCell(datacell1);
					//session.removeAttribute("getSearchBean");
									
					
				}
					
			}
			

			document.add(t);
			document.close();
		
			//end of data headers table

		%>


	</body>
</html>
