<%@ page language="java" pageEncoding="UTF-8" buffer="16kb"%>
<%@ page import="aims.bean.cashbook.VoucherInfo"%>
<%@ page import="java.util.List"%>
<%@ page import="aims.bean.cashbook.VoucherDetails"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="java.text.DecimalFormat" %>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");
String basePath = basePathBuf.toString() + "PensionView/";
VoucherInfo info = (VoucherInfo) request.getAttribute("info");
DecimalFormat df = new DecimalFormat("####.##");
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
		<script type="text/javascript" src="<%=basePath%>scripts/calendar.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/DateTime1.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js" type=""></script>
		<script type="text/javascript">
			function empDetails(pfid,name,desig){
				if(document.forms[0].empHid.value=='Y'){
					document.forms[0].checkedby.value=name;
					document.forms[0].empHid.value=='N';
				}else{
			   		document.forms[0].approved.value=name;
				}
			}
	
		     function popupWindow(mylink, windowname){
				if (! window.focus){
					return true;
				}
				var href;
				if (typeof(mylink) == 'string'){
				   href=mylink;
				} else {
					href=mylink.href;
				}
				progress=window.open(href, windowname, 'width=750,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
				return true;
			}
		 	
		 	function checkApproval(){
		 		if(document.forms[0].voucherDate.value==''){
		 			alert("Please Enter Date (Mandatory)");
		 			document.forms[0].voucherDate.focus();
		 			return false;
		 		}
		 		if(!convert_date(document.forms[0].voucherDate)){
					return false;
				}				
		 		if(document.forms[0].approved.value==''){
		 			alert("Please Enter Approved/Rejected By (Mandatory)");
		 			document.forms[0].approved.focus();
		 			return false;
		 		}
		 	}
		</script>
	</head>

	<body class="BodyBackground1" onload='document.forms[0].voucherDate.focus()'>
		<form action="<%=basePathBuf%>Voucher?method=getVoucherApproval" method="post" onsubmit="javascript : return checkApproval()">		
			<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center">
						&nbsp;
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
												<%=info.getVoucherType()%>
											</td>
										</tr>
										<tr>
											<td class="label">
												<%=info.getPartyType().equals("E") ? "EMPLOYEE CPFNO"
					: info.getPartyType().equals("P") ? "PARTY NAME"
							: "BANK NAME"%>
											</td>
											<td class="label">
												:
												<%=info.getEmpPartyCode()%>
											</td>
										</tr>
										<tr>
											<td class="label">
												<%=info.getPartyType().equals("E") ? "EMPLOYEE NAME"
					: info.getPartyType().equals("B") ? "ACCOUNT NO" : ""%>
											</td>
											<td class="label">
												<%=info.getPartyType().equals("P") ? "" : ": "
					+ info.getPartyDetails()%>
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
												<%=info.getBankName()%>
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
												VOUCHER DATE
											</td>
											<td class="label">
												:
												<input name='voucherDate' maxlength="11" size="18" />
												&nbsp; <a href="javascript:show_calendar('forms[0].voucherDate')"> <img name="calendar" alt='calendar' border="0" src="<%=basePath%>/images/calendar.gif" src="" alt="" /></a>
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
										<%List voucherDts = info.getVoucherDetails();
			int listSize = voucherDts.size();
			double amount = 0.0;
			for (int i = 0; i < listSize; i++) {
				VoucherDetails details = (VoucherDetails) voucherDts.get(i);
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
												<%=details.getDebit()%>
											</td>
											<td class="Data" align="right">
												<%=details.getCredit()%>
											</td>
										</tr>

										<%amount += details.getCredit()-details.getDebit();}%>

										<tr>

											<td class="label" align="right" colspan="4">
												TOTAL&nbsp;&nbsp;
											</td>
											<td class="Data" align="right">
												<%="Payment".equals(info.getVoucherType()) ?df.format(amount)
					: "0.0"%>
											</td>
											<td class="Data" align="right">
												<%=!"Payment".equals(info.getVoucherType()) ? df.format(amount)
					: "0.0"%>
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
								<td class="label" align="right" colspan="2">
									NET PAYMENT :
									<%=df.format(amount)%>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>

							<tr>
								<td width="45%" class="label" colspan="2">
									Rupees (in words)
									<%=CommonUtil.ConvertInWords(amount)%> Only.
								</td>
							</tr>
							<tr>
								<td align="center" class="Data" colspan="2">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>

							<tr>

								<td width="45%" class="label" colspan="2">
									DETAILS :
									<%=info.getDetails()%>
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
									<table width="100%" align="center" border="0">
										<tr>
											<td class="label" nowrap="nowrap">
												Prepared By :
											</td>
											<td>
											</td>
											<td class="Data" align="left">
												<%=info.getEnteredBy()==null?"--":info.getEnteredBy()%>
											</td>
										</tr>
										<tr>
											<td class="label" nowrap="nowrap">
												Checked By :
											</td>
											<td>
											</td>
											<td class="Data" align="left">
												<input type="text" name="checkedby" />
												<input type="hidden" name="empHid" />
												<img src="<%=basePath%>images/search1.gif" onclick="document.forms[0].empHid.value='Y';popupWindow('<%=basePath%>cashbook/EmployeeInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
											</td>
											<td class="label" nowrap="nowrap" align="right">
												<input type="hidden" name="approvalstatus" value="Y" />
												Approved By
								
											</td>
											<td class="Data" align="left">
												<input type="text" name="approved" />
												<img src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/EmployeeInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
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
								<td width="100%" colspan="2" align="center">
									<table width="100%" cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>
											<td width="15%" class="label" nowrap="nowrap">
												Voucher Prep No :
											</td>
											<td class="data">
												<%=info.getKeyNo()%>
												<input type="hidden" name="keyNo" value="<%=info.getKeyNo()%>" />
											</td>
											<td width="30%">
												&nbsp;
											</td>
											<td width="15%" class="label" nowrap="nowrap">
												Received By:
											</td>
											<td class="data" align="left">
												________________________
											</td>
										</tr>
										<tr height="20">
											<td>
												&nbsp;
											</td>
										</tr>
										<tr>
											<td align="center" colspan="5">
												<input type="submit" class="btn" value="Submit" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
