<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
 <base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>AAI</title>
</head>
<body class="body1" onload='callOnLoad()'>
<html>

<head>
<meta http-equiv="msthemecompatible" content="no">
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aimsfinancestyle.css" type="text/css">


<script language="Javascript">

<% 
String from =(String)request.getAttribute("from");
String id=request.getAttribute("monthID").toString();
String paths="";



%>


	<%=request.getAttribute("monthID").toString()%>
<% if(request.getAttribute("from").equals("validatePension")){ 
	String monthID="",monthName="",effectiveYear="",airportID="",inputParams="",effectiveDate="";
	
	if(request.getAttribute("monthID")!=null){
	monthID=request.getAttribute("monthID").toString();

	
	}
	if(request.getAttribute("monthName")!=null){
	monthName=request.getAttribute("monthName").toString();

	
	}
	if(request.getAttribute("effectiveDate")!=null){
	effectiveDate=request.getAttribute("effectiveDate").toString();

	
	}
	
	if(request.getAttribute("effectiveYear")!=null){
	effectiveYear=request.getAttribute("effectiveYear").toString();

	
	}
	if(request.getAttribute("airportID")!=null){
	airportID=request.getAttribute("airportID").toString();
	
	
	}
	if(request.getAttribute("inputParams")!=null){
	inputParams=request.getAttribute("inputParams").toString();
	
	
	}
   paths  = basePath+"validatefinance?method=getPensionValidate&effectDT="+effectiveYear+"&unitCD="+airportID+"&monthID="+monthID+"&monthName="+monthName;
%>
	

<% } %>




function directReqTo() {

	var from ='<%=from%>';
	
	var URL='<%=paths%>';
	if(from =='validatePension') {
		
		 
		opener.document.forms[0].action=URL;
		opener.document.forms[0].method="post";
		opener.document.forms[0].submit();
		window.close(); return
	}
		

}


function callOnLoad() {
	setTimeout('directReqTo()', 3000);
}

</script>
</head>
<% if(request.getAttribute("messg")!=null){ %>
<br>
<br>
<br>
<br>
<body>
<table align='center'>
<tr>
	<td>
		<h2><b><%=request.getAttribute("messg")%></b></h2>
	</td>
</tr>
</table>
<% } %>
</BODY>
</html>
