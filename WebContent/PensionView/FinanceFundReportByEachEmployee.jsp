<%@ page language="java" import="java.util.*,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page
	import="com.lowagie.text.pdf.*,com.lowagie.text.Phrase,com.lowagie.text.PageSize,com.lowagie.text.List,com.lowagie.text.ListItem,com.lowagie.text.Cell,com.lowagie.text.Table,com.lowagie.text.Section,com.lowagie.text.Font,com.lowagie.text.Chapter,com.lowagie.text.Paragraph,com.lowagie.text.Document,com.lowagie.text.FontFactory,java.io.*,com.lowagie.text.Image,java.awt.Color,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.Element,com.lowagie.text.Rectangle,java.sql.ResultSet,com.lowagie.text.Chunk,com.lowagie.text.HeaderFooter"%>

<%@ page import="com.lowagie.text.pdf.TextField"%>
<%@ page import="com.lowagie.text.pdf.PdfName"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.SearchInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="com.lowagie.text.pdf.PdfCell" %>
<%@ page import="com.lowagie.text.pdf.PdfLine" %>

		<%
	
		
			ArrayList dataList=new ArrayList();
			String cpfaccno="",employeeno="",heading="";
			EmployeeValidateInfo validateInfo=new EmployeeValidateInfo();
			if(!request.getParameter("frm_cpfaccno").toString().equals("")){
			cpfaccno=request.getParameter("frm_cpfaccno");
			heading="Fianance Report By CPF.Acc No: "+cpfaccno;
			
			}
			if(!request.getParameter("frm_empno").toString().equals("")){
			employeeno=request.getParameter("frm_empno");
			heading="Fianance Report By Employee No: "+employeeno ;
			}
			FinancialService financeService=new FinancialService();
			dataList=financeService.financeReportByEachEmp(cpfaccno,employeeno);
		
			response.setContentType("application/pdf");
			Document document = new Document(PageSize.A4.rotate(), 0, 0, 50, 50);
			String employeename="",employeeNo="",dbCPFaccno="",airportCD="",pensionNumber="",region="",designation="";
			for(int i=0;i<dataList.size();i++){
			 validateInfo=(EmployeeValidateInfo)dataList.get(i);
			 
			 if(!validateInfo.getCpfaccno().equals("")  || !validateInfo.getEmployeeNo().equals("")){
			 	employeename=validateInfo.getEmployeeName();
			 	employeeNo=validateInfo.getEmployeeNo();
			 	dbCPFaccno=validateInfo.getCpfaccno();
			 	airportCD=validateInfo.getAirportCD();
			 	designation=validateInfo.getDesegnation();
			 }
			
			}
			
			
			
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		
			document.open();
			
			
			Font DATA_HEAD_FONT = new Font(Font.HELVETICA, 18, Font.BOLD,
					new Color(0, 0, 128));
			
			Font DATA_HEAD_FONT2 = new Font(Font.HELVETICA, 8, Font.NORMAL,
					new Color(0, 0, 128));
			
			Font TEXT_FONT = new Font(Font.HELVETICA, 7, Font.NORMAL,
					Color.black);
			 int headerwidths1[] = {30, 15, 15, 15, 15, 15,15, 15, 15, 10, 10, 10,10,10,10};

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
			
			
			
			document.add(opaddt);
			
			PdfPTable spaceheaders = new PdfPTable(1);
			spaceheaders.setWidthPercentage(100);
			spaceheaders.getDefaultCell().setPadding(5);
			
			PdfPCell spaceheadercell = new PdfPCell(new Phrase("",
					DATA_HEAD_FONT2));
			spaceheadercell.setBorderColor(new Color(0, 0, 128));
			spaceheadercell.setBorderWidth(0);
			
			spaceheaders.addCell(spaceheadercell);
			document.add(spaceheaders);
			
			
			PdfPTable dataheaders = new PdfPTable(15);
			dataheaders.setWidthPercentage(100);
			dataheaders.getDefaultCell().setPadding(5);
			dataheaders.setWidths(headerwidths1);

			PdfPCell dataheadercell = new PdfPCell(new Phrase("Employee Name",
					DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Employee No", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Finance Date", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Basic", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Daily Allowance", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Special Basic", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Emoluments", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			//EMPPFSTATUARY
			dataheadercell = new PdfPCell(new Phrase("CPF", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			//vpf
			dataheadercell = new PdfPCell(new Phrase("AdCPF", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Principal", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Interest", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			//EMPTOTAL
			dataheadercell = new PdfPCell(new Phrase("Emp Total", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("PF", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("Pension", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			dataheadercell = new PdfPCell(new Phrase("AAI Total", DATA_HEAD_FONT2));
			dataheadercell.setBorderColor(new Color(0, 0, 128));
			dataheadercell.setBorderWidth(0);
			dataheaders.addCell(dataheadercell);
			
			document.add(dataheaders);
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			PdfPTable dataTbl = new PdfPTable(15);
			dataTbl.setWidthPercentage(100);
			dataTbl.getDefaultCell().setPadding(5);
			dataTbl.setWidths(headerwidths1);
			
			if (dataList.size() != 0) {
				int count = 0;
				String  basic = "", dailyAllowance = "", monthYear = "",specialBasic="",aaiTotal="";
				String principal="",interset="",aaiPF="",aaiPension="";
				String employeeName="",dataEmployeeNo="";
				boolean bckColor=false;
				String emoulments="",pfStatury="",vpf="",empTotal="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmployeeValidateInfo beans = (EmployeeValidateInfo) dataList.get(i);
					employeename=beans.getEmployeeName();
			 		dataEmployeeNo=beans.getEmployeeNo();
			 	
			 
					
					monthYear=beans.getEffectiveDate();
					basic= beans.getBasic();
				
					dailyAllowance = beans.getDailyAllowance();
					specialBasic=beans.getSpecialBasic();
					emoulments=beans.getEmoluments();
					pfStatury=beans.getEmpPFStatuary();
					vpf=beans.getEmpVPF();
					principal=beans.getEmpAdvRecPrincipal();
					interset=beans.getEmpAdvRecInterest();
					empTotal=beans.getEmptotal();
					aaiPF=beans.getAaiconPF();
					aaiPension=beans.getAaiconPension();
					aaiTotal=beans.getAaiTotal();
					
					if(basic.equals("0.0") && dailyAllowance.equals("0.0") && specialBasic.equals("0.0")){
					bckColor=true;
					}else{
					bckColor=false;
					}
					System.out.println("bckColor flag==========="+bckColor);
					PdfPCell datacell1 = new PdfPCell(new Phrase(employeename,
							TEXT_FONT));
					
					datacell1.setBorderColor(new Color(0, 0, 128));
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setBorderWidth(0);
					
					datacell1.setFixedHeight(10);

					dataTbl.addCell(datacell1);
					
					
					datacell1 = new PdfPCell(new Phrase(dataEmployeeNo, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(monthYear, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(basic, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(dailyAllowance, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(specialBasic, TEXT_FONT));

					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(emoulments, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);

				
					
					datacell1 = new PdfPCell(new Phrase(pfStatury, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
						
					datacell1 = new PdfPCell(new Phrase(vpf, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(principal, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(interset, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setNoWrap(true);
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(empTotal, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
					
	
					
					datacell1 = new PdfPCell(new Phrase(aaiPF, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);
					
					datacell1 = new PdfPCell(new Phrase(aaiPension, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					datacell1.setBorderWidth(0);
					datacell1.setNoWrap(true);
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setFixedHeight(10);
					dataTbl.addCell(datacell1);

					datacell1 = new PdfPCell(new Phrase(aaiTotal, TEXT_FONT));
					datacell1.setBorderColor(new Color(0, 0, 128));
					if(bckColor==true){
						datacell1.setBackgroundColor(new Color(255, 0, 0));
					}
					datacell1.setBorderWidth(0);

					dataTbl.addCell(datacell1);
					
					
				}
					
			}
			document.add(dataTbl);
			
			document.close();

		%>
	