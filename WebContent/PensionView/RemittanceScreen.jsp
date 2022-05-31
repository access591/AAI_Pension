
<%@ page
	import="java.util.*,aims.common.CommonUtil,aims.service.FinancialService,aims.bean.EmployeeValidateInfo"
	pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012"};
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">

<title>AAI</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<script type="text/javascript">
   
    </script>
</head>
<body class="BodyBackground">
<%String monthID = "", yearDescr = "", region = "", monthNM = "",monthNM1="",monthID1="";

            ArrayList yearList = new ArrayList();
            ArrayList pfidList = new ArrayList();
            Iterator regionIterator = null;
            Iterator monthIterator = null;
            HashMap hashmap = new HashMap();
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
                Set keys = hashmap.keySet();
                regionIterator = keys.iterator();

            }
            if (request.getAttribute("monthIterator") != null) {
                monthIterator = (Iterator) request
                        .getAttribute("monthIterator");
               
            }
                        
            %>
<form name="validation" method="post">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>

	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table height="5%" align="center">
			<tr>
				<td class="ScreenHeading">Remittance Screen</td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table height="15%" align="center">
			<tr>
				
					<td class="label" align="right">
						From Year / Month:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:50px'>
							<option value='NO-SELECT'>
								Select One
							</option>
					<%for (int j = 0; j < year.length; j++) {%>
																			<option value='<%=year[j]%>'>
																				<%=year[j]%>
																			</option>
																			<%}%>
						</SELECT>
					
						<select name="select_from_month" style="width:50px">
							<Option Value='NO-SELECT'>
								Select One
							</Option>

				<%while (monthIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) monthIterator.next();
                monthID = mapEntry.getKey().toString();
                monthNM = mapEntry.getValue().toString();

                %>

							<option value="<%=monthID%>">
								<%=monthNM%>
							</option>
							<%}%>
						</select>
					</td>
				</tr>
			<tr>
																	<td class="label" align="right">
																		Region:<font color=red>*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_region" style="width:120px" onChange="javascript:getAirports('airport');">
																			<option value="NO-SELECT">
																				[Select One]
																			</option>
																			<%while (regionIterator.hasNext()) {
				region = hashmap.get(regionIterator.next()).toString();

				%>
																			<option value="<%=region%>">
																				<%=region%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>
																</tr>
																<tr>
																	<td class="label" align="right">
																		Aiport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_airport" style="width:120px">
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
			
				<tr>
		</table>
		</td>

	</tr>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;</td>
	</tr>
	<%
				%>
	<tr>

		<td>
		
	</tr>
	<tr>
		<td>
		<table align="center" cellpadding=2 class="tbborder" cellspacing="0"
			border="">


			<tr class="tbheader">
				<td rowspan="2">CPF ACC.NO&nbsp;&nbsp;</td>
				<td rowspan="2">Employee Name&nbsp;&nbsp;</td>
				<td rowspan="2">Pension<br />
				Option&nbsp;&nbsp;</td>
				<td rowspan="2">Emoluments&nbsp;&nbsp;</td>
				<td colspan="5" align="center">Employee subscription</td>
				<td colspan="3" align="center">AAI Contribution</td>
				<td colspan="4" align="center">Validate AAI Contribution</td>
			</tr>
			<tr class="tbheader">
				<td>PF<br />
				Statuary&nbsp;&nbsp;</td>
				<td>VPF&nbsp;&nbsp;</td>
				<td>Principal&nbsp;&nbsp;</td>
				<td>Interest&nbsp;&nbsp;</td>
				<td>Total&nbsp;&nbsp;</td>
				<td>PF&nbsp;&nbsp;</td>
				<td>Pension&nbsp;&nbsp;</td>
				<td>Total&nbsp;&nbsp;</td>
				<td>PF&nbsp;&nbsp;</td>
				<td>Pension&nbsp;&nbsp;</td>
				<td>Total&nbsp;&nbsp;</td>
				<td>&nbsp;&nbsp;</td>
			</tr>



		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
