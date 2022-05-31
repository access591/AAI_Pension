
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="aims.bean.cashbook.VoucherInfo"%>
<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
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

		<title>AAI - Cashbook - Forms - Voucher Approval</title>

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

		    function chkDelete(field){
			    var val = "";
				for (var i=0; i < document.forms[0].deleteRecord.length; i++) {  				 	
						if (document.forms[0].deleteRecord[i].checked)  {	
						  val= val + document.forms[0].deleteRecord[i].value + "|";
			     	}    		       
	            }
                document.forms[0].keynos.value=val;                  
                document.forms[0].action = "<%=basePathBuf%>Voucher?method=deleteVoucher";
                document.forms[0].submit();
		    }
		  
		  </script>
	</head>

	<body class="BodyBackground1" onload="yearSelect();">
		<form action="<%=basePathBuf%>Voucher?method=getVoucherAppRecords" method="post" action="">
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
									Voucher Approval [Search] &nbsp;&nbsp;<font color="red"> </font>
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
											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Type of Voucher 
											</td>
											<td align="left">
												<select name='voucherType' onchange="checkParty();" style='width:80px'>
													<option value="">
														Select One
													</option>
													<option value='R'>
														Receipt
													</option>
													<option value='P'>
														Payment
													</option>
													<option value='C'>
														Contra
													</option>
												</select>
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
							<tr>
								<td align=center class=label colspan=2>
									<font color=red size="2"><%=(request.getParameter("error")==null || "null".equals(request.getParameter("error"))?"":"Error : "+request.getParameter("error"))%>
									
								</td>
							</tr>
							<input type="checkbox" name="deleteRecord" value="" style="display:none;" >
<%
	if(request.getAttribute("dataList")!=null){
%>
							<tr>
								<td align="center" width="100%">
									<input type=hidden name="keynos" />
									<display:table keepStatus="true" export="false" sort="list" name="dataList" name="requestScope.dataList" pagesize="<%=gridLength%>" id='searchData' requestURI="/Voucher?method=getVoucherAppRecords" summary="fdgdfg">
										<display:column title="<img src='./images/delete1.gif' border='0' alt='Delete' onclick='chkDelete(document.forms[0].deleteRecord)'>" media="html">
											<input type="checkbox" name="deleteRecord" value="<%=((VoucherInfo)pageContext.getAttribute("searchData")).getKeyNo()%>" >
										</display:column>
										<display:column property="bankName" sortable="true" headerClass="sortable" title="Bank Name" />
										<display:column property="finYear" sortable="true" title="Financial Year" />
										<display:column property="voucherType" sortable="true" title="Voucher Type" />
										<display:column property="partyType" sortable="true" title="Party Type" />
										<display:column media="html" title="<img src='./images/nominee4.gif'>">
											<a href='<%=basePathBuf%>Voucher?method=getReport&&keyNo=<%=((VoucherInfo)pageContext.getAttribute("searchData")).getKeyNo()%>&&type=approve'> 										
											 <img src='./images/nominee4.gif' border='0' alt='Report' onclick=""/> </a></display:column>
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

