
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="aims.bean.cashbook.VoucherInfo"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
CommonUtil util = new CommonUtil();
int gridLength = util.gridLength();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>" />

		<title>AAI - Cashbook - Forms - Voucher</title>

		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />

		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath%>scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/DateTime.js"></script>
		<link rel="stylesheet" href="<%=basePath%>css/displaytagstyle.css" type="text/css" />
		<script type="text/javascript">
			function yearSelect(){
				var date = new Date();
				var year = parseInt(date.getYear());
				for(cnt=2003;cnt<=year;cnt++){
					var yearEnd = (""+(cnt+1)).substring(2,4);
					document.forms[0].year.options.add(new Option(cnt+"-"+yearEnd,cnt+"-"+yearEnd)) ;				
				}
				document.forms[0].bankName.focus();
			}
			var swidth=screen.Width-10;
			var sheight=screen.Height-150;
			function openReport(url){
				var wind1 = window.open(url,"Voucher","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
			}
		</script>
	</head>

	<body class="BodyBackground1" onload="yearSelect();document.forms[0].bankName.focus()">
		<form action="<%=basePathBuf%>Voucher?method=searchRecords" method="post" action="">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenHeading">
									Voucher[Search] &nbsp;&nbsp;<font color="red"> </font>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<table align="center" cellspacing="5">
										<tr>
											<td class="label" align="right">
												Bank Name 
											</td>
											<td align="left">
												<input type="text" size="18" name="bankName" maxlength="50"  />
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Financial Year 
											</td>
											<td align="left">
												<select name='year' style='width:80px'>
													<option value="">
														Select One
													</option>
												</select>
												<input type="hidden" name="voucherType" value='<%=request.getParameter("type")%>'/>
											</td>
										</tr>

										<tr>
											<td align="center" colspan="2">
												<input type="submit" class="btn" value="Search" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
<%
	if(request.getAttribute("dataList")!=null){
%>
							<tr>
								<td align="center" width="100%">
									<display:table keepStatus="true" export="false" sort="list" name="dataList" name="requestScope.dataList" pagesize="<%=gridLength%>" id='searchData' requestURI="/Voucher?method=searchRecords" summary="fdgdfg">
										<display:column property="bankName" sortable="true" headerClass="sortable" title="Bank Name" />
										<display:column property="finYear" sortable="true" title="Financial Year" />
										<display:column property="voucherType" sortable="true" title="Voucher Type" />
										<display:column property="partyType" sortable="true" title="Party Type" />
										<display:column property="voucherNo" sortable="true" title="Voucher No" />
										<display:column media="html" title="<img src='./images/page_edit.png' border='0' alt='Edit'>">
											<a href='<%=basePathBuf%>Voucher?method=editVoucher&&keyNo=<%=((VoucherInfo)pageContext.getAttribute("searchData")).getKeyNo()%>'> <img src='./images/editGridIcon.gif' border='0' alt='Edit' /> </a>											
										</display:column>
										
										<% if(!((VoucherInfo)pageContext.getAttribute("searchData")).getKeyNo().equals(null)) {%>
										<display:column media="html" title="<img src='./images/nominee4.gif'>">
											 <img src='./images/print.gif' border='0' alt='Report' onclick="openReport('<%=basePathBuf%>Voucher?method=getReport&&keyNo=<%=((VoucherInfo)pageContext.getAttribute("searchData")).getKeyNo()%>');"/> </a>
										</display:column>
										<%}%>
									</display:table>
								</td>
							</tr>
<%
	}
%>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>

