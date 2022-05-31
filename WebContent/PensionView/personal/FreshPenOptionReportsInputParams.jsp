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
                    
                    String[] year = {"2012-13","2013-14","2014-15"};
                    String[] userYears = {"2012-13","2013-14","2014-15"};	
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
	var newParams =params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
	
 
 function formload() {
			process.style.display="none";
	 		populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
		}

	

function callReport(){
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var formType='',reportType='';
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
			//formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
			formType=document.getElementById('select_formType').value;
			var region=document.getElementById('select_region').value;
			var airportcode=document.getElementById('select_airport').value;
			
			if(reportType=='Select One'){
				alert('Please Select the Report Type');
				document.forms[0].select_reportType.focus();
				return false;
			}		
			params ="&frm_reportType="+reportType+"&frm_formType="+formType+"&select_region="+region+"&select_airport="+airportcode;
			url="<%=basePath%>psearch?method=loadInterfaceReport"+params;
	
	//alert(url);
		
				if(reportType=='html' || reportType=='Html'){
		
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet'){
   	 				 		wind1 = window.open(url,"Report","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
	}
	function getAirports() {	    
			var region=document.forms[0].select_region.value;
			createXMLHttpRequest();	
		    var url ="<%=basePath%>validatefinance?method=getAirports&region="+region;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
			xmlHttp.send(null);
	  	}

	  	function createXMLHttpRequest(){
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 		}else if (window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
			}
		}
		function getAirportsList(){				
			if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
				process.style.display="block";
			}
			if(xmlHttp.readyState ==4){
				if(xmlHttp.status == 200){ 
					var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');						
					process.style.display="none";
					if(stype.length==0){
				 		var obj1 = document.getElementById("select_airport");
					  	obj1.options.length=0; 
						obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}else{
					   	var obj1 = document.getElementById("select_airport");
					  	obj1.options.length = 0;
				 		for(i=0;i<stype.length;i++){
		  					if(i==0){
								obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
							}
							obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
						}
			  		}
				}
			}
		}
		function getNodeValue(obj,tag){
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   		}	
	

</script>
	</HEAD>
	<body onload="formload()" >
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
		<table  width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
		<tr>
			<td>
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
			</td>
		</tr>
		<tr>
			<td>
			<table width="55%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						  Pension Option Revision Interface Report Params
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
			<td class="label" align="right" nowrap>
									Region: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<SELECT NAME="select_region" id="select_region" onChange="javascript:getAirports()" style="width:130px">
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
			<td class="label" align="right" nowrap>
									Airport Code: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td>
									<select name="select_airport" id="select_airport" style="width:130px">
										<option value="NO-SELECT">
											[Select One]
										</option>
									</select>
								</td>
			</tr>
			<tr>
			<tr>
			<td width="50%"  class=label align="right" nowrap>Form Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
			<td width="50%"><select name='select_formType' id="select_formType" style='width:120px'>
						<option value='Report A'>Master Report</option>
						<option value='Report A-A'>Report A-A</option>
						<option value='Report A-B'>Report A-B</option>
						<option value='Summary Report'>Summary Report</option>
						</select>
						</td>
						
			</tr>
			
					<tr>
							<td class="label" align="right">Report Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> 
							
										<td><select name="select_reportType" id="select_reportType" Style='width:120px' >
											 	
											 <option value="Html">Html</option>
											 <option value="ExcelSheet">Excel Sheet</option>	              					 
										</select>
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
				
					
					<td>
						&nbsp;
					</td>
					<td align="left" >
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:callReport();">
					
					<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
					</td>
					
				</tr>
				<tr>
					<td align="center"></td>
				</tr>
			</table>
			
			
			
		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center">
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle" />
			<SPAN class="label">Processing.......</SPAN>
		</div>
		</body>
</html>
