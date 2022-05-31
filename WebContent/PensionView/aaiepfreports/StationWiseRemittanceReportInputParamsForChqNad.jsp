<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.bean.StationWiseRemittancebean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.DecimalFormat"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    
                    String[] year = {"2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};
                    String[] userYears = {"2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};	
                    String selYear="";	
                    String accountType="",region1="",monthyear="",monthmm1="",selRegion="";
                    if(request.getAttribute("monthYear")!=null){
                    monthyear=request.getAttribute("monthYear").toString();
                    System.out.println("list"+request.getAttribute("monthYear").toString());
                    }
                    if(request.getAttribute("month")!=null){
                    monthmm1=request.getAttribute("month").toString();
                    System.out.println("month"+request.getAttribute("month").toString());
                    }
                    if(request.getAttribute("region")!=null){
                    selRegion=request.getAttribute("region").toString();
                    System.out.println("selRegion"+request.getAttribute("region").toString());
                    }
                     if(request.getAttribute("year")!=null){
                    selYear=request.getAttribute("year").toString();
                    System.out.println("selYear"+request.getAttribute("year").toString());
                    }
            
            %>
<html>
	<HEAD>
	
		<style>
.black_overlay {
	display: none;
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 3000px;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
}

.white_content {
	display: none;
	position: fixed;
	top: 25%;
	left: 25%;
	width: 50%;
	height: 50%;
	padding: 16px;
	border: 16px solid orange;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
</style>
		
  <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />    	 
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">
		
	function LoadWindow(params){
    var newParams =params;
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 function frmload(){
	}

	

function callReport(){
	var yearId='',url='',formtype='',reqType='';
		yearId=document.forms[0].select_year.value;
		//alert("yearId"+yearId);
	if(yearId=='NO-SELECT'){
	alert("Please Select The Year");
	document.forms[0].select_year.focus();
	return false;
	}
	monthId=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
	//alert("monthId"+monthId);
	if(monthId=='NO-SELECT'){
	alert("Please Select The Month");
	document.forms[0].select_month.focus();
	return false;
	}
	regionId=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].value;
	
	//alert(regionId);
	
	formtype=document.forms[0].formType.options[document.forms[0].formType.selectedIndex].value;
	reqType=document.forms[0].reqFormType.options[document.forms[0].reqFormType.selectedIndex].value;
	//alert(reqType);
	if(formtype==''){
	alert("Please Select The Report Type");
	return false;
	}
	var swidth=screen.Width-10;
	var sheight=screen.Height-150;
	//alert(formtype);
	url = "<%=basePath%>aaiepfreportservlet?method=loadStationWiseRemittanceReport&month="+monthId+"&year="+yearId+"&region="+regionId+"&formtype="+formtype+"&flag="+reqType+"&form=chq&reportFlag=R";
		alert(url);
		if(formtype=='html' || formtype=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(formtype=='Excel Sheet' || formtype=='ExcelSheet'){
   	 				 		wind1 = window.open(url,"Report","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
	}
	

</script>
	</HEAD>
	<body class="BodyBackground" onload="javascript:frmload()">
		<% String monthID = "", yearDescr = "", region = "", monthNM = "";

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
		<form >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
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

			<table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="2%" colspan="2" align="center" class="ScreenMasterHeading">
						 Station Wise Remittance Report Params
					</td>
				</tr>
				<tr>
					<td>
				<table height="25%" align="center" border="0">
				<tr>
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
				</tr>	
				
				<tr>
					<td class="label" align="right">
						Year/Month:<font color=red>*</font>&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:65px'>
							
					<%for (int j = 0; j < userYears.length; j++) {%>
																			<option value='<%=userYears[j]%>'<%=userYears[j].equals(selYear)?"selected":""%>>
																				<%=userYears[j]%>
																			</option>
																			<%}%>
						</SELECT>
						<select name="select_month" style="width:65px">
							
							<%while (monthIterator.hasNext()) { 
                Map.Entry mapEntry = (Map.Entry) monthIterator.next();
                monthID = mapEntry.getKey().toString();
                monthNM = mapEntry.getValue().toString();

                %>

							<option value="<%=monthID%>" <%=monthID.equals(monthmm1)?"selected":""%> >
								<%=monthNM%>
							</option>
							
							<%}%>
						</select>
					</td>
					<td>
						&nbsp;
					</td>
					</tr>
					<tr>
			
				<td class="label" align="right">Region:&nbsp;&nbsp;&nbsp;</td>
				<td><SELECT NAME="select_region" style="width: 90px">
					<option value="All">All Regions</option>
					<%
					
						int k = 0;
						boolean exist = false;
						while (regionIterator.hasNext()) {
							region = hashmap.get(regionIterator.next()).toString();
							k++;

							if (region.equalsIgnoreCase(region1)) {
					%>
					<option value="<%=region1%>" <%=region1.equals(selRegion)?"selected":""%>>
					<%=region1%></option>


					<%
						} else {
					%>
					<option value="<%=region%>"<%=region.equals(selRegion)?"selected":""%>><%=region%></option>
					<%
						}
					%>
					<%
						}
					%>
				</SELECT> </td>
			
					<td>
						&nbsp;
					</td>
					</tr>
					<tr>
							<td class="label" align="right">Form Type:<font color=red></font></td> 
							
										<td><select name="reqFormType" Style='width:80px' >
											 	
											 <option value="N">Normal</option>
											 <option value="C">Component Wise</option>	
											  <option value="A">Airport Wise</option>	              					 
										</select>
							</td>
							<td>
						&nbsp;
					</td>
						</tr>
					<tr>
							<td class="label" align="right">Report Type:<font color=red>*</font></td> 
							
										<td><select name="formType" Style='width:80px' >
											 <option value="">Select One</option>	
											 <option value="Html">Html</option>
											 <option value="ExcelSheet">Excel Sheet</option>	              					 
										</select>
							</td>
							<td>
						&nbsp;
					</td>
						</tr>
						<tr>
								<td>
						&nbsp;
					</td>
							<td>
						&nbsp;
					</td>
							<td>
						&nbsp;
					</td>
						</tr>
					
					<tr>
				
					
					<td>
						&nbsp;
					</td>
					<td align="center" >
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:callReport();">
					
					<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td align="center"></td>
				</tr>
			</table>
			
			
			
		</form>
		</body>
</html>
