<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>

<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>


		<%
	
		
			int totalData = 0;
			String fromDate="",toDate="";
			if(request.getParameter("frm_fromDate")!=null){
			fromDate=request.getParameter("frm_fromDate");
			}
			if(request.getParameter("frm_toDate")!=null){
			toDate=request.getParameter("frm_toDate");
			}
			ArrayList dataList = new ArrayList();
			FinancialService service=new FinancialService();
			dataList = service.getFinanceListAll(fromDate,toDate);
			//totalData = dbBean.totalData("select count(*) as count from emp_pension");
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4, 0, 0, 50, 50);
			
			ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

			//PdfWriter writer = PdfWriter.getInstance(document,response.getOutputStream());
			document.open();
			 int headerwidths1[] = {10, 10, 10, 10, 10, 10, 10,10,10,10,10};
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
			PdfPCell htc = new PdfPCell(new Phrase("Finance Data Report",
					DATA_HEAD_FONT));
			
			htc.setBorderWidth(0);
			htc.setPadding(5);
			opaddt.addCell(htc);
			
			
			PdfPTable headers = new PdfPTable(11);
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

			headerdatacell = new PdfPCell(new Phrase("Employee Name", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Designation", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

		
			
			headerdatacell = new PdfPCell(new Phrase("Emoluments", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			//EMPPFSTATUARY
			headerdatacell = new PdfPCell(new Phrase("CPF", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			//vpf
			headerdatacell = new PdfPCell(new Phrase("AdCPF", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			//EMPTOTAL
			headerdatacell = new PdfPCell(new Phrase("Emp Total", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("AAI Total", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			headerdatacell = new PdfPCell(new Phrase("Financed Date", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Region", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			//end of data headers table
			//headers table

			
			document.add(opaddt);
			document.add(headers);
			document.add(maint);
			//Data table     
			
			
			
			PdfPTable t = new PdfPTable(11);
			t.setWidthPercentage(100);
			t.getDefaultCell().setPadding(5);
				t.setWidths(headerwidths1);
			
			if (dataList.size() != 0) {
				int count = 0;
				String airportCode = "", empNM = "", basic = "", desig = "", dailyAllowance = "", pensionFund = "", region = "";
				String specialBasic="",monthYear="",aaiTotal="",cpfaccno="";
				String emoulments="",pfStatury="",vpf="",empTotal="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmployeeValidateInfo beans = (EmployeeValidateInfo) dataList.get(i);
					cpfaccno=beans.getCpfaccno();
					empNM = beans.getEmployeeName();
					System.out.println("cpfaccno====================="+cpfaccno);
					airportCode = beans.getAirportCD();
					basic= beans.getBasic();
					desig = beans.getDesegnation();
					dailyAllowance = beans.getDailyAllowance();
					region = beans.getRegion();
					monthYear= beans.getEffectiveDate();
					specialBasic=beans.getSpecialBasic();
					emoulments=beans.getEmoluments();
					pfStatury=beans.getEmpPFStatuary();
					vpf=beans.getEmpVPF();
					empTotal=beans.getEmptotal();
					aaiTotal=beans.getAaiTotal();
					PdfPCell datacell1 = new PdfPCell(new Phrase(airportCode,
							TEXT_FONT));

					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);

					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(cpfaccno, TEXT_FONT));
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

				
					
					datacell1 = new PdfPCell(new Phrase(emoulments, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
						
					datacell1 = new PdfPCell(new Phrase(pfStatury, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(vpf, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(empTotal, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					
	
					
					datacell1 = new PdfPCell(new Phrase(aaiTotal, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);
					datacell1 = new PdfPCell(new Phrase(monthYear, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					t.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(region, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);

					t.addCell(datacell1);
					
					
				}
					
			}
			document.add(t);
			document.close();

		%>
	