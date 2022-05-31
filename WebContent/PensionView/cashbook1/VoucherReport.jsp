<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.VoucherInfo"%>
<%@ page import="java.util.List"%>
<%@ page import="aims.bean.cashbook.VoucherDetails"%>
<%@ page import="aims.common.CommonUtil" %>
<%@ page import="java.text.DecimalFormat" %>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");
String basePath = basePathBuf.toString() + "PensionView/";
VoucherInfo info = (VoucherInfo)request.getAttribute("info");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>" />

		<title>AAI - CashBook - Forms - Voucher Report</title>

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
							<td>
								<img src="<%=basePath%>images/logoani.gif" width="87" height="50" align="right" alt="" />
							</td>
							<td class="reportlabel" align="center" valign="middle">
								AIRPORTS AUTHORITY OF INDIA
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<table>
									<tr>
										<td class="label">
											VOUCHER TYPE
										</td>
										<td class="label">
											:
											<%=info.getVoucherType().toUpperCase()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											<%=info.getPartyType().equals("E")?"EMPLOYEE CPFNO":info.getPartyType().equals("P")?"PARTY NAME":"BANK NAME"%>
										</td>
										<td class="label">
											:
											<%=info.getEmpPartyCode()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											<%=info.getPartyType().equals("E")?"EMPLOYEE NAME":info.getPartyType().toUpperCase().equals("B")?"ACCOUNT NO":""%>
										</td>
										<td class="label">
											<%=info.getPartyType().equals("P")?"":": "+info.getPartyDetails().toUpperCase()%>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<table align='right'>
									<tr>
										<td class="label">
											BANK NAME
										</td>
										<td class="label">
											:
											<%=info.getBankName().toUpperCase()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											FINANCIAL YEAR
										</td>
										<td class="label">
											:
											<%=info.getFinYear()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											VOUCHER NO
										</td>
										<td class="label">
											:
											<%=info.getVoucherNo()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											VOUCHER DATE
										</td>
										<td class="label">
											: <%=info.getVoucherDt()%>
										</td>
									</tr>
									<tr>
										<td class="label">
											TRUST TYPE
										</td>
										<td class="label">
											: <%=info.getTrustType()%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table align="center" cellpadding="0" cellspacing="0" border="1" width="100%" class='border'>
									<tr>
										<td class="tblabel" align="center">
											Account Code
										</td>
										<td class="tblabel" align="center">
											Particular
										</td>
										<td class="tblabel" align="center">
											Month/Year:
										</td>
										<td class="tblabel" align="center">
											Details (ifany)
										</td>
										<td class="tblabel" align="center">
											Passed/Debit(RS.)
										</td>
										<td class="tblabel" align="center">
											Deduction/Credit(RS.)
										</td>
									</tr>
									<% 
List voucherDts = info.getVoucherDetails();
int listSize = voucherDts.size();
double debit = 0.0;
double credit = 0.0;
DecimalFormat df = new DecimalFormat("####.##");
for(int i=0;i<listSize;i++ ){
	VoucherDetails details = (VoucherDetails)voucherDts.get(i);
%>
									<tr>
										<td class="Data" align="center">
											<%=details.getAccountHead()%>
										</td>
										<td class="Data" align="left">
											<%=details.getParticular()%>
										</td>
										<td class="Data" align="center">
											<%=details.getMonthYear()%>
										</td>
										<td class="Data" align="left">
											<%=details.getDetails()%>
										</td>
										<td class="Data" align="right">
											<%=df.format(details.getDebit())%>
										</td>
										<td class="Data" align="right">
											<%=df.format(details.getCredit())%>
										</td>
									</tr>

									<%
	debit +=  details.getDebit();
	credit += details.getCredit();
}
%>
									<tr>

										<td class="label" align="right" colspan="4">
											TOTAL&nbsp;&nbsp;
										</td>
										<td class="Data" align="right">
											<%=df.format(debit)%>
										</td>
										<td class="Data" align="right" >
											<%=df.format(credit)%>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td class="label" align="right" colspan="2" nowrap>
								NET PAYMENT :
								<%=df.format(debit-credit)%>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td width="45%" class="label" colspan="2" nowrap>
								Rupees (in words) <%=CommonUtil.ConvertInWords(debit-credit)%> Only.
							</td>
						</tr>
						<tr>
							<td align="center" class="Data" colspan="2">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<br />
								<br />
							</td>
						</tr>
						<tr>

							<td width="45%" class="label" colspan="2" nowrap>
								DETAILS : <%=info.getDetails()%>
							</td>
						</tr>
						<tr>
							<td>
								<br />
								<br />
							</td>
						</tr>
						<tr>
							<td>
								<br />
							</td>
						</tr>
						<tr>
							<td width="100%" colspan="2" align="center">
								<table width="100%" align="center">
									<tr>
										<td class="Data" class="label" nowrap>
											Prepared By :
											<%=info.getPreparedBy()%>
										</td>
										<td class="Data" class="label" nowrap>
											Checked By :
											<%=info.getCheckedBy()%>
										</td>
										<td class="Data" class="label" nowrap>
											<%="Approved By : "+info.getApprovedBy()%>
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
								<table width="90%" cellpadding="0" cellspacing="0">
									<tr>
										<td>
											&nbsp;
										</td>
									</tr>
									<tr>
										<td width="50%" class="label" nowrap>
											&nbsp;&nbsp;&nbsp;Voucher Prep No : <%=info.getKeyNo()%>
										</td>

										<td width="50%" class="label" nowrap>
											Received By: ________________________
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
