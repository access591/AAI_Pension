<%@ page language="java" import="java.util.*,java.lang.*" pageEncoding="UTF-8"%>

<%@ page buffer="16kb"%>
<%@ page import="aims.bean.PensionBean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String[] year = {"2009","2010"};
String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <title>AAI </title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script type="text/javascript"> 
	
		
		function createXMLHttpRequest(){
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		 	}else if (window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
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
              document.forms[0].select_month.value='Apr';
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
			  
			  }else{
			   	var obj1 = document.getElementById("airPortCode");
			   	obj1.options.length = 0;
			  	for(i=0;i<stype.length;i++){
			  if(i==0){	obj1.options[obj1.options.length]=new Option('[Select One]','','true');
							}
					obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
					
			  	}
			  
				  }
			}
		}
	}
   		 
    function fnUpload(user){
       
		 	var fileUploadVal="",region="",formType="",airPortCode="";
		 	fileUploadVal=document.forms[0].uploadfile.value;
		 	formType=document.forms[0].select_proforma_type.value;
		 	region=document.forms[0].select_region.value;
		 	 airPortCode=document.forms[0].airPortCode.value;
		 	
		 	 if(formType=='AAIEPF-3' || formType=='AAIEPF-1' ||formType=='AAIEPF-8' ||formType=='AAIEPF-4'){
		 	if(region=='NO-SELECT'){
		 		alert('Please Select The Region');
		 		document.forms[0].select_region.focus();
		 		return false;
		 	}
		 	if(airPortCode=="" && region=='CHQIAD'){
		 		alert('Please Select The Airport');
		 		document.forms[0].airPortCode.focus();
		 		return false;
		 	}
		 	var year=document.forms[0].select_year.value;
			if(year==""){
				alert("Please select Year");
				document.forms[0].select_year.focus();
				return false;
			}
			var month=document.forms[0].select_month.value;
			if(month==""){
				alert("Please select Month");
				document.forms[0].select_month.focus();
				return false;
			}
		 	if(fileUploadVal==''){
		 		alert('Select a Upload File');
		 		document.forms[0].uploadfile.focus();
		 		return false;
		 	}else{
		 		var iChars = " !=*,";
		    var fileNameWithDrvieNm=document.forms[0].uploadfile.value;
	        fileNameWithDrvieNm=fileNameWithDrvieNm.substring(fileNameWithDrvieNm.lastIndexOf("\\")+1,fileNameWithDrvieNm.lastIndexOf("."));
             
          	for (var i = 0; i < fileNameWithDrvieNm.length; i++) {
		  	if (iChars.indexOf(fileNameWithDrvieNm.charAt(i)) != -1){
			   	alert("File Name like "+formType+"_REGIONNAME.xls");
		  		return false;
		  		}
			 }	
			// alert(fileNameWithDrvieNm);
			
			var checkfileinfo=fileNameWithDrvieNm.split("_");
			var chckproformatype="",chckregioname="",selectedRegion="",chckAirportCode="",selectedAirportCode="";
			chckproformatype=checkfileinfo[0];
			chckregioname=checkfileinfo[1];
			if(checkfileinfo.length==3){
			chckAirportCode=checkfileinfo[2];
			}else{
				chckAirportCode="";
			}
			
			if(chckproformatype!=formType){
				alert("File Name Sholud be Starts with "+formType);
				
				document.forms[0].select_proforma_type.focus();
		  		return false;
			}
				selectedRegion=region.replace(" ","");
				if(chckregioname.toLowerCase()!='allregions'){
					if(chckregioname.toLowerCase()!=selectedRegion.toLowerCase()){
						alert("File Name  like "+formType+"_"+selectedRegion);
						document.forms[0].select_region.focus();
				  		return false;
					}
				}
				selectedAirportCode=airPortCode.replace(" ","");
				
				if(chckAirportCode.toLowerCase()!=selectedAirportCode.toLowerCase()){
					alert("File Name like "+formType+"_"+selectedRegion+"_"+selectedAirportCode);
					document.forms[0].airPortCode.focus();
			  		return false;
				}
		 	}
		 	 }
		 	if(fileUploadVal==''){
		 		alert('Select a Upload File');
		 		document.forms[0].uploadfile.focus();
		 		return false;
		 	}
		 	if(user!='CAPFIN' && user!='navayuga' && user!='NSCBFIN' && user!='CHQFIN' &&user!='TVMFIN' && user!='CSIFIN' && user!='BALWANT'){
		          alert("' "+user+" ' Has no permissions to Import");
		          return false;
		         }
			
		    document.forms[0].action="<%=basePath%>PensionView/PensionFileUpload.jsp?frm_file="+fileUploadVal+"&frm_region="+region+"&frm_formtype="+formType+"&year="+year+"&month="+month+"&airPortCode="+airPortCode;
           	document.forms[0].method="post";
			document.forms[0].submit();
			
		 	}
   	
		 
   		 </script>
  </head>
  <%
	String region="";
  	Iterator regionIterator=null;
  	Iterator monthIterator=null;
  	HashMap hashmap=new HashMap();
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	}

  
  	%>
  <body class="BodyBackground">
   		<form  enctype="multipart/form-data">  
 		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
		<tr>
			 <td><jsp:include page="/PensionView/PensionMenu.jsp"/></td> 
	   </tr>  
	
		<tr>
			<td>&nbsp;</td>
		</tr>
       <tr>  
       	<td>
       		<table align="center" width="70%" align="center" cellpadding="0" cellspacing="0" class="tbborder">
       			<tr>

				<td height="5%"  align="center" class="ScreenMasterHeading">Import CPF Data</td>
				</tr>
                 <tr> <td>&nbsp;</td>
                 </tr>
				<tr>
					<td colspan="8">
					<table align="center" border="0" width="100%" align="center" cellpadding="1" cellspacing="0">
						<tr>
						
       					<td class="label"  align="right">AAIEPF FORMAT:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>
							<SELECT NAME="select_proforma_type" style="width:210px" onchange="getMonth()">
							<option value="AAIEPF-3">AAIEPF-3  (Monthly CPF Recovery)</option>
							<option value="AAIEPF-1">AAIEPF-1  (Opening Balance)</option>
							<option value="AAIEPF-4">AAIEPF-4  (CPF Received From Other Org..)</option>
							<option value="AAIEPF-8">AAIEPF-8  (Advances/PFW/Final Settlement)</option>
							
							<option value="OTHER">OTHER</option>
							</SELECT>
						</td>
       			</tr>
       			<tr>
       				<td class="label" align="right">Region:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td>
							<SELECT NAME="select_region" style="width:130px" onchange="getAirports()">
							<option value="NO-SELECT">[Select One]</option>
							<%while (regionIterator.hasNext()) {
								region = hashmap.get(regionIterator.next()).toString();%>
							<option value="<%=region%>"><%=region%></option>
							<%}%>
							</SELECT>
						</td>
       			</tr>
               <tr>
   						<td class="label" align="right">
									Airport Code:
								</td>
								<td>
									<select name="airPortCode" >
									<option value="">[Select One]</option>
									
					 <% try{if (request.getAttribute("airportList") != null) {
							ArrayList airpors = (ArrayList) request
							.getAttribute("airportList");
						for (int i = 0; i < airpors.size(); i++) {

						PensionBean airportBean = (PensionBean) airpors
								.get(i);
								System.out.println(airportBean.getAirportCode());

						%>
							<option  value="<%=airportBean.getAirportCode()%>"><%=airportBean.getAirportCode()%></option>
							<%} }						
							}   catch (Exception e){
                  out.println("An exception occurred: " + e.getMessage());
                 }
							%>
						    	</select>
						    	
								</td>
               </tr>
               <tr>
					<td class="label" align="right">
						Year:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_year' Style='width:100px'>
							<option value="">
								Select One
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
						Salary Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<Select name='select_month' Style='width:100px'>
							<option value="">
								Select One
							</option>
					<%for (int j = 0; j < month.length; j++) {%>
																			<option value='<%=month[j]%>'>
																				<%=month[j]%>
																			</option>
																			<%}%>
						</SELECT>
					</td>
				</tr>
				
				
       			<tr>
       				<td class="label" align="right">File to Upload:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td><input type="file" name="uploadfile" size="50"> </td>
       			</tr>
       			<tr>
       				<td class="label"></td>
					<td> <input type="button" class="btn"  name="Submit" value="Upload" onclick="javascript:fnUpload('<%=session.getAttribute("userid")%>')"> <input type="button" class="btn"  name="Submit" value="Cancel" onclick="javascript:history.back(-1)" ></td>
       			</tr>
					</table>
					</td>
				</tr>
       <tr><td>&nbsp;</td></tr>
      <tr>
			<td algin="center"><%if (request.getAttribute("message") != null) {%>
	 		<font color="red"><%=request.getAttribute("message")%></font>
     		<%}%>
		<%if (request.getAttribute("errorMessage") != null) {%>
		<b><font color="red" ><%=request.getAttribute("errorMessage")%></font></b>
		<%}%>
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
  	<td align="center" class="Data"><font color="red"> <%=request.getAttribute("lengths")%>	<br>
  													   
  													   </font>
  	</td>
  	<%}%>
  </tr>
  </table>  
</form>  

  </body>
</html>
