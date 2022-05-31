<%@ page import="java.util.*,java.lang.*,aims.dao.CommonDAO" %>
<%@ page language="java" 	pageEncoding="UTF-8"%>
<%@ page buffer="16kb"%>
<%@ page import="aims.bean.PensionBean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String[] year = {"2008","2009","2010","2011","2012"};
String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
String region="",region1="",year1="",airportcode1="";
String userId = session.getAttribute("userid").toString();
//CommonDAO commonDAO = new CommonDAO();
//String currentdate=commonDAO.getdbDate();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%=basePath%>">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<title>AAI</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
<script type="text/javascript"> 
function createXMLHttpRequest()
{
if (window.XMLHttpRequest) {    
	xmlHttp = new XMLHttpRequest();   
} else if(window.ActiveXObject) {    
      try {     
       xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");    
       } catch (e) {     
       		try {      
       			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");     
       		} catch (e) {      
       			xmlHttp = false;     
       		}    
       	}   
       		
 } 
 }
		function getNodeValue(obj,tag)
		   {
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
		   }
		
		function getAirports()
	   { 
		var region=document.forms[0].select_region.value;
		createXMLHttpRequest();	
	    var url ="<%=basePath%>search1?method=getAirports&region="+region;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getAirportsList;
		xmlHttp.send(null);
	  }

		 function getMonth(){
			 formType=document.forms[0].select_proforma_type.value;
			 if(formType=='AAIEPF-1'){
              document.forms[0].select_month.value='04';
			 }else{
				  document.forms[0].select_month.value='';
			 }
		 }

	function getAirportsList()
	 {
		if(xmlHttp.readyState ==4)
		{
		 if(xmlHttp.status == 200)
			{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
			  if(stype.length==0){
			 	var obj1 = document.getElementById("airPortCode");
			   	obj1.options.length=0; 
			   	obj1.options[obj1.options.length]=new Option('Select One','','true');
			  }else{
			   	var obj1 = document.getElementById("airPortCode");
			   	obj1.options.length = 0;
			  	for(i=0;i<stype.length;i++){
			  if(i==0){	obj1.options[obj1.options.length]=new Option('Select One','','true');
			}
		obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		  	}
			  
				  }
			}
		}
	}
	 function frmLoad()
	  {
	 process.style.display="none";
	  
	     <%			
				if (request.getAttribute("region") != null) {
					region1 =request.getAttribute("region").toString();
					region =request.getAttribute("region").toString();
					}
	            if (request.getAttribute("year") != null) {
				year1 =request.getAttribute("year").toString();;
				}
	           if (request.getAttribute("airportcode") != null) {
	        	   airportcode1 =request.getAttribute("airportcode").toString();;
					}
				%>
				var region1='<%=region%>';
				
				if(region1==""){
				region1="Select One" ;
				}
				var year1='<%=year1%>';
				if(year1==""){
				year1="Select One" ;
				}
			// document.forms[0].airPortCode[document.forms[0].airPortCode.options.selectedIndex].text="Select One";
			 document.forms[0].select_region[document.forms[0].select_region.options.selectedIndex].text=region1;
		     document.forms[0].select_year[document.forms[0].select_year.options.selectedIndex].text=year1;
					
	  } 
    function fnUpload(){
           	var month="",region="",airPortCode="",year="";
           	var year
		 	year=document.forms[0].select_year.value.split("-");		 	
		 	region=document.forms[0].select_region.value;
		 	airPortCode=document.forms[0].airPortCode.value;
		 	 month=document.forms[0].select_month.value;
		 	process.style.display="block";
		    document.forms[0].action="<%=basePath%>validatefinance?method=getDeviationRecords&frm_region="+region+"&Year="+year+"&Month="+month+"&airPortCode="+airPortCode;
          	document.forms[0].method="post";
			document.forms[0].submit();			
		 	}
 	function getDate(date){	 
 	alert("date");
	  if(date.indexOf('-')!=-1){
		 elem = date.split('-'); 
	  }else{
		  elem = date.split('/'); 
	  }	  
		 day = elem[0];
		 mon1 = elem[1];
		 year = elem[2];
		 var month;
	   	 if((mon1 == "JAN") || (mon1 == "Jan")) month = 0;
        	else if(mon1 == "FEB" ||(mon1 == "Feb")) month = 1;
     	else if(mon1 == "MAR" || (mon1 == "Mar")) month = 2;
     	else if(mon1 == "APR" || (mon1 == "Apr")) month = 3;
     	else if(mon1 == "MAY" ||(mon1 == "May") ) month = 4;
     	else if(mon1 == "JUN" ||(mon1 == "Jun") ) month = 5;
     	else if(mon1 == "JUL"||(mon1 == "Jul")) month = 6;
     	else if(mon1 == "AUG" ||(mon1 == "Aug")) month = 7;
     	else if(mon1 == "SEP" ||(mon1 == "Sep")) month = 8;
     	else if(mon1 == "OCT"||(mon1 == "Oct")) month = 9;
     	else if(mon1 == "NOV" ||(mon1 == "Nov")) month = 10;
     	else if(mon1 == "DEC" ||(mon1 == "Dec")) month = 11;
	  var finaldate=new Date(year,month,day); 
	  return finaldate;	     	
  }
		 
   		 </script>
</head>
<%
    String monthID="",monthNM="";
    String formID="",formNM="";
  	Iterator regionIterator=null;
  	Iterator monthIterator=null;
  	HashMap hashmap=new HashMap();
  	Iterator formIterator=null;
  	String fileName="";
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	}
  	if(request.getAttribute("monthIterator")!=null){
  	  	monthIterator=(Iterator)request.getAttribute("monthIterator");
  	  	}
	if(request.getAttribute("formsListMap")!=null){
  		formIterator=(Iterator)request.getAttribute("formsListMap");
  	  	}
  	
  	if(request.getAttribute("fileName")!=null){
  		fileName=request.getAttribute("fileName").toString();
  		System.out.println("fileName" +fileName);
  	  	}

  
  	%>
<body class="BodyBackground" onload="frmLoad();getAirports()">
<form enctype="multipart/form-data">
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
		<table align="center" width="70%" align="center" cellpadding="0"
			cellspacing="0" class="tbborder">
			<tr>

				<td height="5%" align="center" class="ScreenMasterHeading">Import
				Deviation Data</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="8">
				<table align="center" border="0" width="100%" align="center"
					cellpadding="1" cellspacing="0">					
					<tr>
						<td class="label" align="right">Region:<font color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><SELECT NAME="select_region" style="width: 130px"
							onchange="getAirports()">
							<option value="">Select One</option>
							<%int k = 0;
			boolean exist = false;
            while (regionIterator.hasNext()) {
			region = hashmap.get(regionIterator.next()).toString();
			k++;
				
				if (region.equalsIgnoreCase(region1)) {

					%>
							<option value="<%=region1%>" <% out.println("selected");%>>
							<%=region1%></option>


							<%} else {%>
							<option value="<%=region%>"><%=region%></option>
							<%}

			                %>

							<%}	%>
						</SELECT></td>
					</tr>
					<tr>
						<td class="label" align="right">Airport
						Name:<font color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><select name="airPortCode" id="airPortCode">
							<option value="">Select One</option>

							<% try{if (request.getAttribute("airportList") != null) {
							ArrayList airpors = (ArrayList) request
							.getAttribute("airportList");
						for (int i = 0; i < airpors.size(); i++) {
						PensionBean airportBean = (PensionBean) airpors
								.get(i);
								System.out.println(airportBean.getAirportCode());

						%>
							<option value="<%=airportBean.getAirportCode()%>"><%=airportBean.getAirportCode()%></option>
							<%} }						
							}   catch (Exception e){
                  out.println("An exception occurred: " + e.getMessage());
                 }
							%>
						</select></td>
					</tr>
					<tr>
						<td class="label" align="right">Year:<font color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td><Select name='select_year' Style='width: 100px'>
							<option value="">Select One</option>
                <%for (int j = 0; j < year.length; j++)  {
			     String year2 = year[j].toString();
			    	if (year2.equalsIgnoreCase(year1)) {
				%>   
							<option value="<%=year1%>" <% out.println("selected");%>>
							<%=year1%></option>


							<%} else {%>
							<option value="<%=year2%>"><%=year2%></option>
							<%}

			%>

							<%}	%>
							
						</SELECT></td>
					</tr>
					<tr>
						<td class="label" align="right">Salary Month:<font
							color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><Select name='select_month' Style='width: 100px'>
							<option value="">Select One</option>
							<%while (monthIterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) monthIterator.next();
				monthID = mapEntry.getKey().toString();
				monthNM = mapEntry.getValue().toString();

				%>
				<option value="<%=monthID%>"><%=monthNM%></option>
							<%}%>
						</SELECT></td>
					</tr>					
					<tr>
						<td class="label"></td>
						<td><input type="button" class="btn" name="Submit" id="sub"
							value="Submit"
							onclick="javascript:fnUpload()">
							<input type="button" class="btn" name="Submit" id="can" value="Cancel" onclick="javascript:history.back(-1)">
							</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td algin="center">
				<%if (request.getAttribute("message") != null) {%> 
				<font color="red"><%=request.getAttribute("message")%></font>
				<%}%>
				<%if (request.getAttribute("errorMessage") != null) {
				String message=request.getAttribute("errorMessage").toString();
				String[] temp;
				temp = message.split("-->");
				for(int i =0; i < temp.length ; i++){%>
                  <b><ul><font color="red"><li><%=temp[i]%><br></li></font></ul></b> 
                  <%}}%>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<%
  System.out.println("xlsSize"+request.getAttribute("xlsSize"));
  String updateMessage="",invalidTxtFileSize="",invalidDataSize="";
  if(request.getAttribute("lengths")!=null  ){
  	if(request.getAttribute("lengths")!=null){
  updateMessage = request.getAttribute("lengths").toString();
   }
  else {
  updateMessage="";
  }
 
  %>
	<td align="center" class="Data"><font color="red"> <%=request.getAttribute("lengths")%>
	<br>

	</font></td>
	<%}%>
	</tr>
</table>
</form>
<div id="process"
	style="position: fixed; width: auto; height:60%; top: 300px; right: 0; bottom: 100px; left: 10em;"
	align="center"><img
	src="<%=basePath%>PensionView/images/Indicator.gif" border="no"
	align="middle" /> <SPAN class="label">Processing.......</SPAN></div>
</body>
</html>
