
<%@ page language="java"
	import="java.util.*,aims.common.CommonUtil,aims.common.Constants"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
               
            %>

<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">

<script type="text/javascript">
	var xmlHttp;
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
	 function gettoId(){
		var fromId=document.forms[0].fromId.value;
        if(fromId!=''){
		document.forms[0].toId.value=parseInt(fromId)+50;
        }
	 }
	 function numsDotOnly()
		{
	  if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46) ||(event.keyCode>=44 && event.keyCode<=47))
	        {
	           event.keyCode=event.keyCode;
	        }
	        else
	        {
				 event.keyCode=0;
	        }
	  }
	function getPFIDRange(){	
	
		var fromId;
		var toId;
		fromId=document.forms[0].fromId.value;
		toId=document.forms[0].toId.value;
		createXMLHttpRequest();	
		var url ="<%=basePath%>reportservlet?method=getVefifiedPFIDList&fromId="+fromId+"&toId="+toId;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getVefifiedPFIDList;
		xmlHttp.send(null);
    }
	
	function getVefifiedPFIDList(){
		 	if(xmlHttp.readyState ==4){
			if(xmlHttp.status == 200){ 
				var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
				if(stype.length==0){
				 	var obj1 = document.getElementById("select_pfidlst");
				  	obj1.options.length=0; 
		  			obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
				}else{
		 		   	var obj1 = document.getElementById("select_pfidlst");
		 		   	var obj2=document.getElementById("empname");
		 		  	obj1.options.length = 0;
				 	for(i=0;i<stype.length;i++){
		  				if(i==0){
						obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
						}
						var idname=getNodeValue(stype[i],'pfid')+" - "+getNodeValue(stype[i],'empname')
						obj1.options[obj1.options.length] = new Option(idname);
											
						}
		  		}
			}
		}
	}
	function getNodeValue(obj,tag)
    { 
	     if(obj.getElementsByTagName(tag)[0].firstChild){
	  return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	 }else return "";
   }
  function movedata(){
	if(document.forms[0].fromId.value==''){
	alert(" Please Select the PFID From");
		 document.forms[0].fromId.focus();
	     return false;	
	}
	var selectedIndex=document.forms[0].select_pfidlst.options.selectedIndex;
	var pfid=document.forms[0].select_pfidlst[selectedIndex].text ;
	var pfidname=pfid.split("-");
	 pfid=pfidname[0];
	if(document.forms[0].select_pfidlst.value=='NO-SELECT'){
     alert(" Please Select the PFID to Edit the Recoveries");
     document.forms[0].select_pfidlst.focus();
     return false;
	}	
   document.forms[0].action="<%=basePath%>reportservlet?method=movedatafrom95to2008&PFID="+pfid;
  document.forms[0].method="post";
  document.forms[0].submit();

  }
  function resetValues(){
	  document.forms[0].fromId.value="";
	  document.forms[0].toId.value="";
  }
	   
	</script>
</HEAD>
<body class="BodyBackground" onload="javascript:frmload()">
<%String monthID = "", yearDescr = "", region = "", monthNM = "";

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
            if (request.getAttribute("yearList") != null) {
                yearList = (ArrayList) request.getAttribute("yearList");
            }
            if (request.getAttribute("pfidList") != null) {
            	pfidList = (ArrayList) request.getAttribute("pfidList");
            }
            
            %>
<form action="post">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<table width="75%" border="0" align="center" cellpadding="0"
	cellspacing="0" class="tbborder">
	<tr>
		<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
		Edit - Final Settlement / Verified </td>
	</tr>
	<tr>
	<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="label" align="right">PFID From / To:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td><input type="text" name="fromId"
			onblur="gettoId();getPFIDRange(this.value)" onkeypress="numsDotOnly()"
			style="width: 57px" /> <input type="text" name="toId"
			style="width: 57px" onkeypress="numsDotOnly();"
			onblur="getPFIDRange()" /></td>
	</tr>
	<tr>
	<td class="label" align="right">PFID to Edit Recoveries:<font
			color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
  
		<td><SELECT NAME="select_pfidlst" style="width: 120px" >
			<option value='' Selected>[Select One]</option>
		   </SELECT></td>
	</tr>
  
	<tr>
	<tr>
		<td align="center">&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
	<tr>
		<td align="right"><input type="button" class="btn" name="Submit"
			value="Submit" onclick="javascript:movedata();"> <input
			type="button" class="btn" name="Reset" value="Reset"
			onclick="resetValues();"> <input type="button" class="btn"
			name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
		</td>
	</tr>
	<tr>
		<td align="center"></td>
	</tr>
</table>
</form>
</body>
</html>
