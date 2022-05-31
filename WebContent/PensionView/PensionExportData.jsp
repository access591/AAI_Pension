<%@ page language="java"%>
<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ page buffer="16kb"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009"};
%>
<%
	String monthID="",yearDescr="",monthNM="";
	Iterator monthIterator=null;
  	Iterator yearIterator=null;
  
  	if(request.getAttribute("monthIterator")!=null){
  	monthIterator=(Iterator)request.getAttribute("monthIterator");
  	}
  	if(request.getAttribute("yearIterator")!=null){
  	yearIterator=(Iterator)request.getAttribute("yearIterator");
  	}
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

   		 
    function fnUpload(){
           
		 	var fileUploadVal="",region="";
		 	region=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].value;
		 	
		 	if(region=='NO-SELECT'){
		 		alert('Please Select The Region');
		 		document.forms[0].select_region.focus();
		 		return false;
		 	}
		 var reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
	       	selectedRegion=region.replace(" ","");
	       var	monthID=document.forms[0].select_month.selectedIndex;
	       var 	yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;	
			var url="<%=basePath%>reportservlet?method=exportData&frm_region="+region+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType;
		   document.forms[0].action=url;
		    alert( document.forms[0].action);
			document.forms[0].method="post";
			document.forms[0].submit();
			
		 	}
   	
	
   
   		 </script>
</head>
<%
	String region="";
  	Iterator regionIterator=null;
  	HashMap hashmap=new HashMap();
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	}
  	%>
<body class="BodyBackground">
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
		<table align="center" width="80%" align="center" cellpadding="0"
			cellspacing="0" class="tbborder">
			<tr>
				<td height="5%" colspan="2" align="center" class="ScreenHeading">Uploading
				Monthly PF Proforma</td>
			</tr>
			<tr>
				<td colspan="8">
				<table align="center" border="3" width="100%" align="center"
					cellpadding="1" cellspacing="0">
					<tr>
						<td class="label" align="right">Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td><Select name='select_year' Style='width: 100px'>
							<option value='No-SELECT'>Select One</option>
							<%for (int j = 0; j < year.length; j++) {%>
							<option value='<%=year[j]%>'><%=year[j]%></option>
							<%}%>
						</SELECT></td>

					</tr>
					<tr>
						<td class="label" align="right">
						Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><select name="select_month" style="width: 100px">
							<Option Value='' Selected>Select One</Option>
							<%while (monthIterator.hasNext()) {
				Map.Entry mapEntry = (Map.Entry) monthIterator.next();
				monthID = mapEntry.getKey().toString();
				monthNM = mapEntry.getValue().toString();

				%>

							<option value="<%=monthID%>"><%=monthNM%></option>
							<%}%>
						</select></td>
					</tr>


					<tr>

						<td class="label" align="right">PF Proforma
						Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><SELECT NAME="select_proforma_type" style="width: 130px">
							<option value="RECOVERY">RECOVERY SCHEME</option>
						</SELECT></td>
					</tr>
					<tr>
						<td class="label" align="right">Region:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><SELECT NAME="select_region" style="width: 130px">
							<option value="NO-SELECT">[Select One]</option>
							<%while (regionIterator.hasNext()) {
								region = hashmap.get(regionIterator.next()).toString();%>
							<option value="<%=region%>"><%=region%></option>
							<%}%>
						</SELECT></td>
					</tr>
					<tr>
						<td class=label align="right" nowrap>Report Type: <font
							color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><SELECT NAME="select_reportType" style="width: 88px">
							<option value="">[Select One]</option>
							<option value="html">Html</option>
							<option value="ExcelSheet">Excel Sheet</option>

						</SELECT></td>
					</tr>
					<tr>
						<td class="label"></td>
						<td><input type="button" class="btn" name="Submit"
							value="Export" onclick="javascript:fnUpload()"> <input
							type="button" class="btn" name="Submit" value="Cancel"
							onclick="javascript:history.back(-1)"></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td algin="center">
				<%if (request.getAttribute("message") != null) {%> <font color="red"><%=request.getAttribute("message")%></font>
				<%}%> <%if (request.getAttribute("errorMessage") != null) {%> <b><font
					color="red"><%=request.getAttribute("errorMessage")%></font></b> <%}%>
				&nbsp;</td>
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

</body>
</html>
