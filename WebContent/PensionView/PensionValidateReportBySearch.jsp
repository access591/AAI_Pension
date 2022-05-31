<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>

<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.SearchInfo"%>


		<%
	
		
			ArrayList dataList=new ArrayList();
			
			
	
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
			String aiportCode="",monthYear="";
			if(request.getAttribute("aiportCD")!=null){
			aiportCode=request.getAttribute("aiportCD").toString();
			}
			
			if(request.getAttribute("date")!=null){
			monthYear=request.getAttribute("date").toString();
			}
			
			if(request.getAttribute("financeReport")!=null){
			dataList=(ArrayList)request.getAttribute("financeReport");
			}
			
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			
			document.open();
			
			
			Font DATA_HEAD_FONT = new Font(Font.HELVETICA, 14, Font.BOLD,
					new Color(0, 0, 128));
			
			Font DATA_HEAD_FONT2 = new Font(Font.HELVETICA, 8, Font.NORMAL,
					new Color(0, 0, 128));
			
			Font TEXT_FONT = new Font(Font.HELVETICA, 7, Font.NORMAL,
					Color.black);
			 int headerwidths1[] = {15, 15, 35, 30, 15, 15,15, 15, 15, 15, 15, 15,15,15};

			//main table
			PdfPTable maint = new PdfPTable(1);
			maint.getDefaultCell().setBorderColor(new Color(0, 0, 128));
			maint.setWidthPercentage(90);
			maint.getDefaultCell().setBorderWidth(0.0f);
			maint.setHeaderRows(1);

			// Heading Table
			PdfPTable mainheaders = new PdfPTable(1);
			mainheaders.setWidthPercentage(40);
			//opaddt.setWidths(headerwidths1);
			PdfPCell htc = new PdfPCell(new Phrase("AIRPORTS AUTHORITY OF INDIA ,"+aiportCode,
					DATA_HEAD_FONT));
			
			htc.setBorderWidth(0);
			htc.setPadding(5);
			mainheaders.addCell(htc);
			document.add(mainheaders);
			
			PdfPTable secondheaders = new PdfPTable(1);
			secondheaders.setWidthPercentage(80);
			//opaddt.setWidths(headerwidths1);
			PdfPCell secondheadingcell = new PdfPCell(new Phrase("SCHEDULE FOR CPF RECOVERY & PENSION CONTRIBUTION FOR THE MONTH OF "+monthYear,
					DATA_HEAD_FONT));
			
			secondheadingcell.setBorderWidth(0);
			secondheadingcell.setPadding(5);
			secondheaders.addCell(secondheadingcell);
			document.add(secondheaders);
			
			
			PdfPTable spaceheaders1 = new PdfPTable(1);
			spaceheaders1.setWidthPercentage(100);
		
			spaceheaders1.getDefaultCell().setPadding(5);
			
			PdfPCell spaceheadercell1 = new PdfPCell(new Phrase("",
					DATA_HEAD_FONT2));
			spaceheadercell1.setBorderColor(new Color(0, 0, 128));
			spaceheadercell1.setBorderWidth(0);
			spaceheadercell1.setFixedHeight(2.0f);
			spaceheaders1.addCell(spaceheadercell1);
			document.add(spaceheaders1);
			
			PdfPTable headers = new PdfPTable(14);
			headers.setWidthPercentage(100);
			headers.setWidths(headerwidths1);
			headers.getDefaultCell().setPadding(5);
		

			PdfPCell headerdatacell = new PdfPCell(new Phrase("CPF Acc.no",
					DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Employee No", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Name of employee", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setNoWrap(true);
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Designation", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

			headerdatacell = new PdfPCell(new Phrase("Pension Option", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
		
			headerdatacell = new PdfPCell(new Phrase("Emoluments", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
		
			
				
			headerdatacell = new PdfPCell(new Phrase("PF Statuary", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("VPF", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			
			
			headerdatacell = new PdfPCell(new Phrase("Principal", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Interest", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("Total", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("AAI PF", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("AAI Pension", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);
			
			headerdatacell = new PdfPCell(new Phrase("AAI Total", DATA_HEAD_FONT2));
			headerdatacell.setBorderColor(new Color(0, 0, 128));
			headerdatacell.setBorderWidth(0);
			headers.addCell(headerdatacell);

		
			//end of data headers table

			
			
			//headers table
			
			document.add(headers);
			document.add(maint);
	
	
			//Data table     
			
			PdfPTable dataTbl = new PdfPTable(14);
			dataTbl.setWidthPercentage(100);
			dataTbl.getDefaultCell().setPadding(5);
			dataTbl.setWidths(headerwidths1);
			System.out.println("dataList.size()============"+dataList.size());
			if (dataList.size() != 0) {
				int count = 0;
				String pfStatury = "",vpf="", empNM = "",employeeNumber="",emoluments="";
				String desig="",cpfaccno="",principal="",interset="",empTotal="";
				String aaiPF="",aaiPension="",aaiTotal="";
				
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmployeeValidateInfo beans = (EmployeeValidateInfo) dataList.get(i);
					cpfaccno=beans.getCpfaccno();
					
					empNM = beans.getEmployeeName();
					emoluments=beans.getEmoluments();
					employeeNumber= beans.getEmployeeNo();
					pfStatury= beans.getEmpPFStatuary();
					vpf=beans.getEmpVPF();
					empTotal=beans.getEmptotal();
					principal=beans.getEmpAdvRecPrincipal();
					interset=beans.getEmpAdvRecInterest();
					desig = beans.getDesegnation();
					aaiTotal= beans.getAaiTotal();
					aaiPension= beans.getAaiconPension();
					aaiPF=beans.getAaiconPF();
				
					
			PdfPCell datacell = new PdfPCell(new Phrase(cpfaccno,TEXT_FONT));
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
		
			//Need to put a Pension Option column
			datacell = new PdfPCell(new Phrase("",TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);	
		
			datacell = new PdfPCell(new Phrase(emoluments,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(pfStatury,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);

			datacell = new PdfPCell(new Phrase(vpf,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(principal,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(interset,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			
			datacell = new PdfPCell(new Phrase(empTotal,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
				datacell = new PdfPCell(new Phrase(aaiPF,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
				datacell = new PdfPCell(new Phrase(aaiPension,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);
			
			datacell = new PdfPCell(new Phrase(aaiTotal,TEXT_FONT));
			datacell.setBorderColor(new Color(0, 0, 128));
			datacell.setBorderWidth(0);
			dataTbl.addCell(datacell);			
					
					
				}
						
			}
			document.add(dataTbl);	
			
			
				
	
			document.close();

		%>
	