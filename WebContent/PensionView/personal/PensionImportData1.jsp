
<%@ page import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.ReadLabels"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<base href="<%=basePath%>">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
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
		
		 var detailArray = new Array();
		 var count=0;	
		 var xlsLables = "";
		 var dbLables = "";
		
			 	
		var arr = new Array();

   		 function saveDetails()
		   {			
			if(checkData()){
				save();
				showValues();
				clearDetails();
			}
		}//end of the function saveDetails().
		
		function checkData() {
	   var errors = "";
	  xlsLables = document.getElementById('xlsLables').value;
	  dbLables = document.getElementById('dbLables').value;
	  
	  
	  if(xlsLables == "")
	   errors = "Select xlsLable \n";
	   if(dbLables== "")
	     errors = errors+"Select dbLables\n";
	      if(errors != ""){
	       alert(errors);
	       return false;
	       }
	 return true;
	}
	
	function save()
		{ 
			detailArray[detailArray.length]=[xlsLables,dbLables];
		}
		function showValues(){
	
	var totalAmount=0;
	var nettotal=0;
	
	  var str='<TABLE border=0 align="center"   cellspacing=0 width=100%>';
			for(var i=0;i<detailArray.length;i++)
			{

				str+='<TR><td>&nbsp;</td>';

				if(detailArray[i][0].length<10)
					str+='<td  class=tcn align=center style="width:190px">'+detailArray[i][0]+'</TD>';
				else
				{
					str+="<td  class=tcn align=center style='width:190px'>"+detailArray[i][0].substring(0,10);
					str+="<a class=tbl href=javascript:void(0) onMouseOver=\"overlib('"+detailArray[i][0]+"\')\" ";
					str+="onMouseOut='nd()'>...</a></TD>";
				}
				if(detailArray[i][1].length<10)
					str+='<td  class=tcn align=center style="width:120px">'+detailArray[i][1]+'</TD>';
				else
				{
					str+="<td class=tcn align=center style='width:120px'>"+detailArray[i][1].substring(0,10);
					str+="<a class=tbl href=javascript:void(0) onMouseOver=\"overlib('"+detailArray[i][1]+"\')\" ";
					str+="onMouseOut='nd()'>...</a></TD>";
				}
						
					   
				str+='<TD align=center ><a href=javascript:void(0) onclick=del('+i+')>';
				str+='<img src="./PensionView/images/delete.gif"  alt=Delete border=0 width=20 height=20></TD>';
				str+='</TR>';
				
				
			}
			str+='</TABLE>';
			document.all['detailsTable'].innerHTML = str;
			
	}
	function clearDetails()
		{ 
			document.forms[0].xlsLables.value = "";
			document.forms[0].dbLables.value = "";
			}
	function del(index)
		{
			 
			var temp=new Array();
			var j=1;
		
			for(var i=0;i<detailArray.length;i++)
			{
				if(i!=index)
				{  
					temp[temp.length]=[detailArray[i][0],detailArray[i][1]];
					j++;
				}

			}

			detailArray=temp;
			showValues();
			clearDetails(j);
			return false;
		}
	
	
		 
   		 function fnUpload(){
   		 	var fileUploadVal="";
   		 alert("naga");
   		    <%	String fileName1="";
   		     if (request.getAttribute("fileName") != null) {
   		     fileName1=request.getAttribute("fileName").toString();
				}%>
				
				document.forms[0].uploadfile.value='<%=fileName1%>';
				alert(document.forms[0].uploadfile.value);
   		 	    fileUploadVal=document.forms[0].uploadfile.value;
   		 	if(fileUploadVal==''){
   		 		alert('Select a Upload File');
   		 		document.forms[0].uploadfile.focus();
   		 		
   		 	}
   		 	else{
   		 		
   		 		document.forms[0].action="<%=basePath%>PensionView/personal/PensionFileUpload1.jsp";
				document.forms[0].method="post";
				document.forms[0].submit();
				
   		 	}
	   	
   		 }
   		 </script>
	</head>

	<body class="BodyBackground">
		<form enctype="multipart/form-data">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>


					


					</td>
				</tr>
				<tr>
					<td align="center">
						<b>&nbsp;</b>
					</td>
				</tr>

				<tr>
					<td colspan="3">
						<table width="75%" height="45%" align="center" cellpadding="0" cellspacing="0" class="tbborder">

							<tr>
								<td align="center" class="tbheader">
									<b>Select a file to upload</b>
								</td>
							</tr>
							<tr>
								<td align="center">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center">
									<input type="file" name="uploadfile" value="<%=fileName1%>" size="50">
								</td>
							</tr>
							<tr>
								<td align="center">

									<input type="button" class="btn" name="Submit" value="Upload" onclick="javascript:fnUpload()">
									<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
								</td>
							</tr>
							<tr>
								<td align="center">
									&nbsp;&nbsp;&nbsp;&nbsp;
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
				<%if (request.getAttribute("fileName") != null) {
				out.println(request.getAttribute("fileName").toString());
				}%>
				
				<%if (request.getAttribute("labels") != null) {%>
				<tr><td><table width="75%" height="245%" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr><td>&nbsp;</td></tr>
				<%}%>
					<%System.out.println("xlsSize" + request.getAttribute("xlsSize"));
			String updateMessage = "", invalidTxtFileSize = "", invalidDataSize = "";
			ReadLabels lablesBean = new ReadLabels();
			if (request.getAttribute("labels") != null) {
				if (request.getAttribute("labels") != null) {
					lablesBean = (ReadLabels) request.getAttribute("labels");

				} else {
					updateMessage = "";
				}
				String[] xls = lablesBean.getXlsLables();
				String[] dblables = lablesBean.getDbLables();

				%>

					<td align="left" >
						<font color="red"></td>
					<td align="center" class="label">
						Sheet Columns
						<select name="xlsLables">
							<%for (int i = 0; i < xls.length; i++) {

					%>
							<option value="<%=xls[i]%>">
								<%=xls[i]%>
							</option>
							<%}

				%>
						</select>
						<br>
						
					</td>
					<td align="left"  class="label">
						<font color="red">
					<td align="center" class="Data">
						Table Columns
						<select name="dbLables">
							<%for (int i = 0; i < dblables.length; i++) {

					%>
							<option value="<%=dblables[i]%>">
								<%=dblables[i]%>
							</option>
							<%}

			%>
						</select>
						<br>
						</font>
					</td>
					
					<TR>
												<td colspan="5">
												<div id="detailsTable"></div>
												</td>
											</TR>
					<tr><td>&nbsp;</td></tr>
				    <tr>
								<td colspan="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>

								<td align="center">
									<input type="button" class="btn" value="Add" class="btn" onclick="saveDetails();">
									
								</td>
							</tr>
							
							<td align="center">
									<input type="button" class="btn" value="Submit" class="btn" onclick="javascript:fnUpload();">
									
								</td>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr><td>&nbsp;</td></tr>
				
				</td></table>
					<%}%>
				
			</table>
		</form>

	</body>
</html>
