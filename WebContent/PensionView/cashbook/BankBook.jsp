
<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.BankBook"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DecimalFormat"%>
<%StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");

String basePath = basePathBuf.toString() + "PensionView/";
BankBook book = (BankBook) request.getAttribute("book");
SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
DecimalFormat df = new DecimalFormat("#######.##");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>" />

		<title>AAI</title>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
	</head>

	<body background="body">
		<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center">
					<table border="0" cellpadding="3" cellspacing="0" align="center" valign="middle">
						<tr>
							<td rowspan="4">
								<img src="<%=basePath%>images/logoani.gif" width="87" height="50" align="right" alt="" />
							</td>
							<td class="reportlabel" align="center" valign="middle">
								INTERNATIONAL AIRPORTS AUTHORITY OF INDIA
							</td>
						</tr>
						<tr>
							<td class="reportlabel" align="center" valign="middle">
								EMPLOYEES CONTRIBUTRY PROVIDENT FUND
							</td>
						</tr>
						<tr>
							<td class="reportlabel" align="center" valign="middle">
								OPERATIONAL OFFICE,GURGAON ROAD
							</td>
						</tr>
						<tr>
							<td class="reportlabel" align="center" valign="middle">
								NEW DELHI-110037
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
						<tr>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>

							<td align="center">
								BANK BOOK
							</td>

						</tr>
						<tr>
							<td align="right">
								<table>
									<tr>
										<td class="tblabel" align="right">
											Bank Name 
										</td>
										<td class="label" align="left">
											: <%=book.getBankName().toUpperCase()%>
										</td>
									</tr>

									<tr>

										<td class="tblabel" align="right">
											Date
										</td>
										<td class="label" align="left">
											: <%=sdf.format(new Date())%>
										</td>

									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<br />
							</td>
						</tr>
						<tr>

							<td>
								<table cellpadding="0" cellspacing="0" border="0" width="100%" class="border">
									<tr>
										<td class="label" align="center">
											Voucher NO
										</td>
										<td class="label" align="center">
											Code
										</td>
										<td class="label" align="center">
											G/L Head
										</td>
										<td class="label" align="center">
											Party Name
										</td>
										<td class="label" align="center">
											Description
										</td>
										<td class="label" align="center">
											Cheque
										</td>
										<td class="label" align="center">
											Payments
										</td>
										<td class="tblabel" align="center">
											Receipts
										</td>
									</tr>
									<tr>

										<td class="Data" align="center">

										</td>
										<td class="Data" align="center"></td>
										<td class="Data" align="center"></td>
										<td class="Data" align="center"></td>
										<td class="Data" align="center">
											OPENING BALANCE
										</td>
										<td class="Data" align="center">
											-
										</td>
										<td class="Data" align="center">
											-
										</td>
										<td class="Data" align="right">
											<%=df.format(book.getOpeningBalAmt())%>
										</td>
									</tr>
									<%
List bookList = book.getBankBookList();
int size = bookList.size();
double payments =0.00;
double receipts =book.getOpeningBalAmt();
for(int i=0;i<size;i++){
	book = (BankBook)bookList.get(i);	
	payments += book.getPayments();
	receipts += book.getReceipts(); 
%>
									<tr>

										<td class="Data" align="center">
											<%=book.getVoucherno()%>
										</td>
										<td class="Data" align="center">
											<%=book.getAccountHead()%>
										</td>
										<td class="Data">
											<%=book.getParticular()%>
										</td>
										<td class="Data">
											<%=book.getPartyName()%>
										</td>
										<td class="Data">
											<%=book.getDescription()%>
										</td>
										<td class="Data">
											<%=book.getChequeNo()%>
										</td>
										<td class="Data" align="right">
											<%=df.format(book.getPayments())%>
										</td>
										<td class="Data" align="right">
											<%=df.format(book.getReceipts())%>
										</td>
									</tr>
<%
}
if(size==0){
	%>
									<tr>
										<td colspan=8 class="label" align="center">
											No Transactions!
										</td>
									</tr>									
	<%
}
%>

									<tr>
										<td class="label" align="right" colspan="6">
											TOTAL
										</td>
										<td class="Data" align="right">
											<%=df.format(payments)%>
										</td>
										<td class="Data" align="right">
											<%=df.format(receipts)%>
										</td>
									</tr>									
									<tr>
										<td class="label" align="right" colspan="6">
											CLOSING BALANCE
										</td>
										<td>
											&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td class="Data" align="right">
											<%=df.format(receipts-payments)%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="Data" align="center">
								<br />
								<br />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table>
						<tr>
							<td class="label" align="right">
								Balance Of Rs :&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
							<td class="Data" align="left">
								<%=df.format(receipts-payments)%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table width="75%" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<br />
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td class="Data" align="center" width='33%'>
								Certified
							</td>
							<td class="Data" align="center" width='33%'>
								Cashier
							</td>
							<td class="Data" align="center" width='33%'>
								Manager(Finance)
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr></tr>

		</table>

	</body>
</html>
