<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
    String forms[] = {"FORM-6-PS-1995","FORM-6-PS"};
	String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013"};				
            %>

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>

<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<script type="text/javascript">

 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}

	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadForm6Cmp";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	

	
	function validateForm() {
		var regionID="",airportcode="",reportType="",fromYearID="",toYearID="",fromMonthID="",toMonthID="",yearDesc="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		formType=document.forms[0].formType.options[document.forms[0].formType.selectedIndex].text;
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		
     	reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		if(document.forms[0].select_year.selectedIndex>0){
		fromYearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		fromYearID=document.forms[0].select_year.value;
		}
		
		if(document.forms[0].select_to_year.selectedIndex>0){
		toYearID=document.forms[0].select_to_year.options[document.forms[0].select_to_year.selectedIndex].value;
		}else{
		toYearID=document.forms[0].select_to_year.value;
		}
		if(toYearID<fromYearID){
			alert('To year is less than from Year.Please select To Year');
			document.forms[0].select_to_year.focus();
			return false;
		}
		
		if(document.forms[0].select_month.selectedIndex>0){
		fromMonthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
		fromMonthID=document.forms[0].select_month.value;
		}
		if(document.forms[0].select_to_month.selectedIndex>0){
		toMonthID=document.forms[0].select_to_month.options[document.forms[0].select_to_month.selectedIndex].value;
		}else{
		toMonthID=document.forms[0].select_to_month.value;
		}
		
		if(fromYearID=="1995"){
		
			if(fromMonthID=="01" || fromMonthID=="02" || fromMonthID=="03"){
				alert("Doesn't have Transaction Data for the Selected Month");
				document.forms[0].select_month.focus();
				return false;
			}
		}
	
		if(toYearID=='1995'){
			if(toMonthID=="01"|| toMonthID=="02" || toMonthID<="03"){
				alert("Doesn't have Transaction Data for the Selected Month");
				document.forms[0].select_to_month.focus();
				return false;
			}
		}
	
		var params = "&frm_region="+regionID+"&frm_year="+fromYearID+"&to_year="+toYearID+"&frm_month="+fromMonthID+"&to_month="+toMonthID+"&frm_reportType="+reportType+"&frm_formType="+formType;
		var url="<%=basePath%>reportservlet?method=loadForm6Comp"+params;
		
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
		
	}
	
</script>
	</HEAD>
	<body class="BodyBackground" >
		<%String monthID = "", yearDescr = "", region = "", monthNM = "",monthToID="",monthToNM="";

           
            Iterator regionIterator = null;
            Iterator monthIterator = null;
               Iterator monthToIterator = null;
            HashMap hashmap = new HashMap();
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
                Set keys = hashmap.keySet();
                regionIterator = keys.iterator();

            }
            if (request.getAttribute("monthIterator") != null) {
                monthIterator = (Iterator) request
                        .getAttribute("monthIterator");
                 monthToIterator=(Iterator) request.getAttribute("monthToIterator");
            }
       

            %>
		<form action="post">
			<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
				<tr>
					<td>
						<jsp:include page="/PensionView/PensionMenu.jsp" />
				</td>
				</tr>

				<tr>
					<td>
						&nbsp;
					</td>

				</tr>
			</table>

			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						Form-6 (PS) Comprehensive Report Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="70%" border="0" align="center" cellpadding="1" cellspacing="1">
							<tr>
								<td width="50%" class="label" align="right">
									From Year:<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<Select name='select_year' Style='width:100px'>
										<option value='NO-SELECT'>
											Select One
										</option>
										<%for (int j = 0; j < year.length; j++) {%>
										<option value='<%=year[j]%>'>
											<%=year[j]%>
										</option>
										<%}%>
									</Select>
								</td>

							</tr>
							<tr>
								<td width="50%" class="label" align="right">
									From Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<select name="select_month" style="width:100px">
										<Option Value='NO-SELECT' Selected>
											Select One
										</Option>
										<%while (monthIterator.hasNext()) {
											Map.Entry mapEntry = (Map.Entry) monthIterator.next();
											monthID = mapEntry.getKey().toString();
											monthNM = mapEntry.getValue().toString();%>
										<option value="<%=monthID%>">
											<%=monthNM%>
										</option>
										<%}%>
									</select>
								</td>
							</tr>
								<tr>
								<td width="50%" class="label" align="right">
									To Year:<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<Select name='select_to_year' Style='width:100px'>
										<option value='NO-SELECT'>
											Select One
										</option>
										<%for (int j = 0; j < year.length; j++) {%>
										<option value='<%=year[j]%>'>
											<%=year[j]%>
										</option>
										<%}%>
									</Select>
								</td>

							</tr>
							<tr>
								<td width="50%" class="label" align="right">
									To Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<select name="select_to_month" style="width:100px">
										<Option Value='NO-SELECT' Selected>
											Select One
										</Option>
					
										<%while (monthToIterator.hasNext()) {
											Map.Entry mapEntry = (Map.Entry) monthToIterator.next();
											monthID = mapEntry.getKey().toString();
											monthNM = mapEntry.getValue().toString();%>
										<option value="<%=monthID%>">
											<%=monthNM%>
										</option>
										<%}%>
									</select>
								</td>
							</tr>
							<tr>
								<td  width="50%" class="label" align="right">
									Region:<font color="red">*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<SELECT NAME="select_region" style="width:100px" >
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
								<td width="50%" class="label" align="right" nowrap>Form Type:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								<td><Select name='formType' Style='width:100px' align="left">
									<option value=''>[Select One]</option>
									<%for (int j = 0; j < forms.length; j++) {%>
									<option value='<%=forms[j]%>'><%=forms[j]%></option>
									<%}%>
									</SELECT></td>
							</tr>							


							<tr>
								<td width="50%" class="label" align="right" nowrap>
									Report Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<SELECT NAME="select_reportType" style="width:88px">
										<option value="html">
											Html
										</option>
										<option value="ExcelSheet">
											Excel Sheet
										</option>
									</SELECT>
								</td>
							
							</tr>

							<tr>
								<td align="center">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>

							<tr>
								<td align="center" colspan="2">
									<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
									<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
									<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
								</td>
							</tr>
						</table>
					</td>
				</tr>


				<tr>
					<td align="center"></td>
				</tr>
			</table>



		</form>
		
	</body>
</html>
