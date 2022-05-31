<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>

<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.DatabaseBean"%>
<jsp:useBean id="dbBean" class="aims.bean.DatabaseBean" scope="request">
	<jsp:setProperty name="dbBean" property="*" />
</jsp:useBean>
<%@page errorPage="error.jsp"%>
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
		<A href="./PensionFundSearch.jsp">Back</A>
		<%
		
		try{
		dbBean = (DatabaseBean) session.getAttribute("getSearchBean");
		}catch(Exception e){
		out.println(e);
		}
			int totalData = 0;

			ArrayList dataList = new ArrayList();
			dataList = dbBean.retriveByAll();
			//totalData = dbBean.totalData("select count(*) as count from emp_pension");
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4, 0, 0, 50, 50);

			PdfWriter writer = PdfWriter.getInstance(document, response
					.getOutputStream());
			//PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
			document.open();
			int headerwidths1[] = { 15, 15, 15, 15, 15, 15 };
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
			opaddt.setWidthPercentage(45);
			//opaddt.setWidths(headerwidths1);
			PdfPTable headers = new PdfPTable(6);
			headers.setWidthPercentage(100);
			headers.getDefaultCell().setPadding(5);
			headers.setWidths(headerwidths1);

			PdfPCell headerdatacell = new PdfPCell(new Phrase("Airport Code",
					DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);

			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Name", DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Designation", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Salary", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("PF", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headerdatacell.setNoWrap(true);
			headerdatacell.setFixedHeight(10);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Pension Fund", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("FromDate", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Todate", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			//end of data headers table

			//Data table     
			PdfPTable t = new PdfPTable(6);
			t.setWidthPercentage(100);
			t.getDefaultCell().setPadding(5);
			t.setWidths(headerwidths1);
			//headers table

			PdfPCell htc = new PdfPCell(new Phrase("Pension Data Report",
					DATA_HEAD_FONT));

			htc.setBorderWidth(0);
			htc.setPadding(5);
			opaddt.addCell(htc);
			document.add(opaddt);
			document.add(headers);
			document.add(maint);
			if (dataList.size() != 0) {
				int count = 0;
				String airportCode = "", empNM = "", salary = "", desig = "", pf = "", pensionFund = "", employeeCode = "";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					DatabaseBean beans = (DatabaseBean) dataList.get(i);
					empNM = beans.getEmpName();
					airportCode = beans.getAirPortCD();
					salary = beans.getSalary();
					desig = beans.getDesegination();
					pf = beans.getPf();
					employeeCode = beans.getEmployeeCode();
					pensionFund = beans.getPensionFund();

					PdfPCell datacell1 = new PdfPCell(new Phrase(airportCode,
							TEXT_FONT));

					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);

					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(empNM, TEXT_FONT));

					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(desig, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(salary, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(pf, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(pensionFund, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);

					t.addCell(datacell1);
					
					
				}
					
			}
document.add(t);
			document.close();

		%>
	
	</body>
</html>
