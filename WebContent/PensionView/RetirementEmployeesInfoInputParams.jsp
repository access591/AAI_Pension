<%@ page language="java" import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
	String forms[] = {"Retirement","Death","Resignation","Termination","Option for Early Pension","VRS","Other"};
	String[] year = {"2013","2014","2015","2016","2017","2018","2019","2020","2021","2022","2023"};							
					%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

<script type="text/javascript">

function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo){
			document.forms[0].txt_empname.value=employeeName;
			document.forms[0].frm_pensionno.value=empSerialNo;
			document.forms[0].chk_empflag.checked=true
		  			
		}

function popupWindow(mylink, windowname)
		{
		
		//var transfer=document.forms[0].transferStatus.value;
		var transfer="";
	
		var regionID="";
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID;
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID;
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		
		return true;
		}
 
function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=retirementEmployeesInfo";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	
	function validateForm() {
		var reportType="",url="",regionID='',airportcode="",yearID="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		

    monthID=document.forms[0].select_month.selectedIndex;
    if(document.forms[0].select_month.selectedIndex>0){
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
		monthID=document.forms[0].select_month.value;
		}
		if(monthID=='NO-SELECT'){
			alert('User should be select month');
			document.forms[0].select_month.focus();
			return false;
		}
			
       if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
		if(yearID=='NO-SELECT'){
			alert('Please Select the Year ');
			document.forms[0].select_year.focus();
			return false;
		}
	
	reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		if(reportType=='[Select One]'){
			alert('Please Select the Report Type');
			document.forms[0].select_reportType.focus();
			return false;
		}
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		
		if(document.forms[0].select_airport.length>1){
			airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
		}else{
			airportcode=document.forms[0].select_airport.value;
		}
			url = "<%=basePath%>reportservlet?method=retirementEmployeesReport&frm_region="+regionID+"&frm_year="+yearID+"&frm_reportType="+reportType+"&frm_month="+monthID+"&frm_airportcd="+airportcode;
			
			
			
			
            
				if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 						//alert("url "+url);	
   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
   	 				 		winOpened = true;
							wind1.window.focus();
   	 			}
			
	
			
			return false;
			
		
		//return false;
	}


	function callReport(){
	  
		var monthID,regionID,yearID,dateString,monthName,selectedInputParam,path;
		monthID=document.forms[0].select_month.selectedIndex;
		monthName=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		var splitYearID = yearID.split("-");
		yearID=splitYearID[0];
		if(monthID<10){
		monthID="0"+monthID;
		}
		path="<%=basePath%>reportservlet?method=getform3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID;
	//	alert(path);
		document.forms[0].action=path;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function createXMLHttpRequest()
	{
	if(window.ActiveXObject)
	 {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 }
	else if (window.XMLHttpRequest)
	 {
		xmlHttp = new XMLHttpRequest();
	 }
	 }
function getAirports(){	
		monthID=document.forms[0].select_month.selectedIndex;
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		monthID="0"+monthID;
		}
	createXMLHttpRequest();	
	var url ="<%=basePath%>reportservlet?method=getAirports&region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID;;
	
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getAirportsList;
	
	xmlHttp.send(null);
    }
 function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
function getAirportsList()
{
	
	if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 process.style.display="block";
	}
	if(xmlHttp.readyState ==4)
	{
		
		if(xmlHttp.status == 200)
		{ 
		var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		 process.style.display="none";
		  if(stype.length==0){
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_airport");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_airport");
		  //    alert(stype.length);	
		  	obj1.options.length = 0;
		  	
		  	for(i=0;i<stype.length;i++){
		  		if(i==0)
					{
				//	alert("inside if")
					obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}
		          //	alert("in else");
			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
			}
		  }
		}
	}
}
function frmload(){
 process.style.display="none";
}
</script>
</HEAD>
<body class="BodyBackground" onload="javascript:frmload()">
<%
	String monthID="",yearDescr="",region="",monthNM="";
	Iterator monthIterator=null;
  	Iterator yearIterator=null;
  	Iterator regionIterator=null;
  		HashMap hashmap=new HashMap();
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	
  	}
  	if(request.getAttribute("monthIterator")!=null){
  	monthIterator=(Iterator)request.getAttribute("monthIterator");
  	}
  	if(request.getAttribute("yearIterator")!=null){
  	yearIterator=(Iterator)request.getAttribute("yearIterator");
  	}
%>
<form action="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%">
						<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td >
						<table width="70%" border="0" cellpadding="0" cellspacing="0" class="tbborder" align="center">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">							 Monthly  Seperation EmployeeInfo Input Params
  
								</td>
							</tr>
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
																		Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<Select name='select_year' Style='width:120px'>
																			<option value='No-SELECT'>
																				[Select One]
																			</option>
																			<%for (int j = 0; j < year.length; j++) {%>
																			<option value='<%=year[j]%>'>
																				<%=year[j]%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>

																</tr>
																<tr>
																	<td class="label" align="right">
																		Month:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<select name="select_month" style="width:120px">
																			<option value="NO-SELECT">
																				[Select One]
																			</option>
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
																		Region: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_region" style="width:120px" onChange="javascript:getAirports()">
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
																	<td class=label align="right" nowrap>
																		Report Type: <font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																			<SELECT NAME="select_reportType" style="width:120px">
																		
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
					<td>
						&nbsp;
					</td>
				</tr>
																<tr>
																
														<td colspan="3" align="center">
																		
									 			<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
												<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
												<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
																</td>
																	
																</tr>



											
				


						</table>
		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>
</body>
</html>