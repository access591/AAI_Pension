<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>

<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.FinacialDataBean"%>
<%@ page import="aims.bean.SearchInfo"%>


		<%
	
		
			ArrayList dataList=new ArrayList();
			
			SearchInfo getterFinanceBean=new SearchInfo();
			FinacialDataBean bean=new FinacialDataBean();
			if(session.getAttribute("reportsearchBean")!=null){
			bean=(FinacialDataBean)session.getAttribute("reportsearchBean");
		
			}
			String heading="";
			if(!bean.getCpfAccNo().equals("") && !bean.getEmployeeNewNo().equals("")){
				heading="Finance Report By CPF Acc.no "+bean.getCpfAccNo()+" And EmployeeNo "+bean.getEmployeeNewNo();
			}else if(!bean.getCpfAccNo().equals("") && bean.getEmployeeNewNo().equals("")){
					heading="Finance Report By CPF Acc.no "+bean.getCpfAccNo();
			}else if(bean.getCpfAccNo().equals("") && !bean.getEmployeeNewNo().equals("")){
				heading="Finance Report By  EmployeeNo "+bean.getEmployeeNewNo();
			}else{
				heading="Finance Report";
			}
			
			if(session.getAttribute("reportfinancebean")!=null){
			getterFinanceBean=(SearchInfo)session.getAttribute("reportfinancebean");
			}
			dataList = getterFinanceBean.getSearchList();
			//totalData = dbBean.totalData("select count(*) as count from emp_pension");
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4, 0, 0, 50, 50);
			
			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			//PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
			document.open();
			
			
			Font DATA_HEAD_FONT = new Font(Font.HELVETICA, 18, Font.BOLD,
					new Color(0, 0, 128));
			
			Font DATA_HEAD_FONT2 = new Font(Font.HELVETICA, 8, Font.NORMAL,
					new Color(0, 0, 128));
			
			Font TEXT_FONT = new Font(Font.HELVETICA, 7, Font.NORMAL,
					Color.black);
			 int headerwidths1[] = {15, 15, 15, 15, 15, 15};

			//main table
			PdfPTable maint = new PdfPTable(1);
			maint.getDefaultCell().setBorderColor(new Color(0, 0, 128));
			maint.setWidthPercentage(90);
			maint.getDefaultCell().setBorderWidth(0.0f);
			maint.setHeaderRows(1);

			PdfPTable opaddt = new PdfPTable(1);
			opaddt.setWidthPercentage(80);
			//opaddt.setWidths(headerwidths1);
			PdfPCell htc = new PdfPCell(new Phrase(heading,
					DATA_HEAD_FONT));
			
			htc.setBorderWidth(0);
			htc.setPadding(5);
			opaddt.addCell(htc);
			
			
			PdfPTable headers = new PdfPTable(6);
			headers.setWidthPercentage(100);
			headers.getDefaultCell().setPadding(5);
		

			PdfPCell headerdatacell = new PdfPCell(new Phrase("Airport Code",
					DATA_HEAD_FONT2));

			headerdatacell.setBorderColor(new Color(0, 0, 128));
		
			headerdatacell.setBorderWidth(0);

			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("CPF ACC.NO", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Employee Number", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Employee Name", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Designation", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

		
			
			
			headerdatacell = new PdfPCell(new Phrase("Financed Date", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

		
			//end of data headers table

			
			
			//headers table
	document.add(opaddt);
			document.add(headers);
					document.add(maint);
	
	
			//Data table     
			
			
			
			PdfPTable dataTbl = new PdfPTable(6);
			dataTbl.setWidthPercentage(100);
			dataTbl.getDefaultCell().setPadding(5);
			dataTbl.setWidths(headerwidths1);
			
			if (dataList.size() != 0) {
				int count = 0;
				String airportCode = "", empNM = "",employeeNumber="";
				String monthYear="",desig="",cpfaccno="";
				
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					FinacialDataBean beans = (FinacialDataBean) dataList.get(i);
					cpfaccno=beans.getCpfAccNo();
					empNM = beans.getEmployeeName();
					
					employeeNumber= beans.getEmployeeNewNo();
					airportCode = beans.getAirportCode();
					desig = beans.getDesignation();
					monthYear= beans.getMonthYear();
				
					
			PdfPCell datacell = new PdfPCell(new Phrase(airportCode,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(cpfaccno,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(employeeNumber,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(empNM,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(desig,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);	

			datacell = new PdfPCell(new Phrase(monthYear,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);			
					
					
				}
						
			}
			document.add(dataTbl);	
				
	
			document.close();

		%>
	