 <%@ page language="java"
	import="java.util.*,aims.common.CommonUtil,aims.common.Constants"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

 
<%@ page import="java.util.ArrayList"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    
                    String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012"};
                    String[] userYears = {"1995-96","1996-97","1997-98","1998-99","1999-00","2000-01","2001-02","2002-03","2003-04","2004-05","2005-06","2006-07","2007-08","2008-09","2009-10","2010-11","2011-12"};		
    String crtlAccSeqNo="";
     if(request.getAttribute("serialNo")!=null){ 
     crtlAccSeqNo = (String)request.getAttribute("serialNo");
     }   
        
        
            %>
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">

<script type="text/javascript">

 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"FINALACCOUNTS","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].chk_empflag.checked=true;		
		}
	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=summaryReports";
		document.forms[0].method="post";
		document.forms[0].submit();
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

	var status='',serialNo='<%=crtlAccSeqNo%>',url="",frmType="",frmStatus="";
	var swidth=screen.Width-10;
	var sheight=screen.Height-150;
	var  params="",reportType="",finyear="",formName="",formType="",region=""; 
	function validateForm() { 
		formType=document.forms[0].select_formType.value;
		region=document.forms[0].select_region.value;
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		if(document.forms[0].finyear.selectedIndex>0){
		finyear=document.forms[0].finyear.options[document.forms[0].finyear.selectedIndex].value;
		}else{
		finyear=document.forms[0].finyear.value;
		}
		 
		if(finyear=='NO-SELECT'){
			alert('User should  select Financial Year');
			document.forms[0].finyear.focus();
			return false;
		}
		 
		/*if(formType=='form2aregionwise' && finyear='2008-2009'){
			alert('Please Select Any Other Financial Year ecxept 2008-2009');
		} */
		
		if(formType=='NO-SELECT'){
			alert('User should be select Form Type');
			document.forms[0].select_formType.focus();
			return false;
		}
		 
		if(reportType=='Select One'){
			alert('User should be select Report Type');
			document.forms[0].select_reportType.focus();
			return false;
		}
		frmStatus =  document.forms[0].select_status.value;
		params = "&frm_finyear="+finyear+"&frm_reportType="+reportType+"&serialNo="+serialNo+"&frm_status="+frmStatus;
		 
		if(formType=='form3regionwise'){		
		url="<%=basePath%>aaiepfreportservlet?method=getform3regionwise"+params;		
		}
		if(formType=='form2regionwise'){
		frmType = "form2summary";
		url="<%=basePath%>aaiepfreportservlet?method=getsummaryinformation&frm_frmType=form2summary&frm_region="+region+params;		
		}
		
		if(formType=='form2aregionwise'){
		frmType = "form2Asummary";
		url="<%=basePath%>aaiepfreportservlet?method=getsummaryinformation&frm_frmType=form2Asummary&frm_region="+region+params;		
		}
		if(formType=='form8regionwise'){
		frmType = "form8summary";
		url="<%=basePath%>aaiepfreportservlet?method=getsummaryinformation&frm_frmType=form8summary"+params;		
		}
		if(formType=='summaryinformation'){
		frmType ="summaryinformation";
		url="<%=basePath%>aaiepfreportservlet?method=getsummaryinformation"+params;		
		}
		if(formType=='regionwisecontrolaccountsummary'){
		url="<%=basePath%>aaiepfreportservlet?method=getregionwisesummary"+params;		
		}
		
		if((formType=='form3regionwise') || (formType=='regionwisecontrolaccountsummary')){
			if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
		}else{
		if(serialNo==''){
			alert("Please  execute Control Account Procedure ...........");
		}else{ 
   			 getStatus();
    	  }
      }
     //alert('--after cond---'+document.forms[0].seqno.value);
  
	 
	}
	 function LoadWindow(params){
    var newParams =params;
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
    }
	
	 
	
	function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
	
	function createXMLHttpRequest(){
		if(window.ActiveXObject){
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 	}else if (window.XMLHttpRequest){
			xmlHttp = new XMLHttpRequest();
		 }
	}
	
	function getStatus()
{	  
	 var formType=document.forms[0].select_formType.value;
	createXMLHttpRequest();	
	var seqNo='<%=crtlAccSeqNo%>';
    var url ="<%=basePath%>psearch?method=checkControlAccntStatus&formType="+frmType+"&serialNo="+seqNo;
    //alert(url);
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getCntrlAccStatus;
	xmlHttp.send(null);
}

function getCntrlAccStatus()
{
	if(xmlHttp.readyState ==4)
	{
	 if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  
		 status = getNodeValue(stype[0],'Status');
		 document.forms[0].seqno.value=status;
		if(status=='exists'){ 
        //alert(url);
		if(formType=='summaryinformation'){	
	var comfirmMsg=confirm("Do You want to Finalize this Report? Select OK for Finalize & Cancel for Verification");
		//alert(comfirmMsg);
		if (comfirmMsg== true){	 
			var crtlAccFlag =  "F";
		}else{		 
		var crtlAccFlag =  "V";
		}
		url = url+"&crtlAccFlag="+crtlAccFlag
		}
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
      	
    }else{ 
       if(status=='Days Expired'){
       		alert("Days Exceed. Please  execute Control Account Procedure Again ...........");
       }else{   
       alert(status);
      	}
	}	  
		}
	}
}
	
	
	 
function getPFIDNavigationList()
	{ }
	function frmload(){
		 process.style.display="none";
		 document.getElementById("region").style.display="none";
		 
	}

function  showDet(){
var formType="";
formType=document.forms[0].select_formType.value;
if(formType=='form2regionwise'){
document.getElementById("region").style.display="block";
}else{
document.getElementById("region").style.display="none";
}
}

</script>
</HEAD>
<body class="BodyBackground" onload="javascript:frmload()">
 <%String region = ""; 
            Iterator regionIterator = null;        
            HashMap hashmap = new HashMap();
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
                Set keys = hashmap.keySet();
                regionIterator = keys.iterator();
            }
              
            %>
<form  action="post">
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
<table width="50%" border="0" align="center" cellpadding="1"
	cellspacing="1" class="tbborder">
	 
	<tr>
		<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
		 Control Account Summary Input Params</td>
	</tr>
	 
	<tr>
		<td>&nbsp;</td>
	</tr>
	 
	<tr>
		<td class="label" align="right">Financial Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		</td>
		 <td>
		 <select name="finyear" style="width: 80px">
			<Option value='NO-SELECT'>Select One</Option>			 		 
			<option value="2009-2010">2009-2010</option>
			<option value="2010-2011">2010-2011</option>
			 
		</select>
		</td>
		<input type="hidden" name="seqno" />
	</tr>
	 
	 <TR >
		<td class=label align="right" nowrap>Form Type:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
		<td><SELECT NAME="select_formType" style="width: 120px" onchange="showDet();">
			<option value='NO-SELECT'>Select One</option>
			<option value="summaryinformation">Control Account Summary</option>
			<option value="form2regionwise">Form 2 Region Wise</option>  
			<option value="form2aregionwise">Form 2 A Region Wise</option>
			<option value="form8regionwise">Form 8 Region Wise</option>
			<option value="form3regionwise">Form 3 Region Wise</option> 
			<option value="regionwisecontrolaccountsummary">Contol Accounts Region Wise</option> 
			 
		</SELECT></td>

	</tr>
	 
	  <tr id="region">
		 	<td class="label" align="right"> Region: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><SELECT NAME="select_region" style="width:120px" >
				<option value="NO-SELECT">[Select One]</option>
				<%while (regionIterator.hasNext()) {
				region = hashmap.get(regionIterator.next()).toString();
				%>
				<option value="<%=region%>"><%=region%></option>
				<%}%>
				</SELECT>
			</td>
	</tr>
	 <tr>
		 	<td class="label" align="right">Status: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><SELECT NAME="select_status" style="width:120px" >
				<option value="">Select One</option>	
				<option value="A">Active</option>				 
				<option value="D">De Active</option>
				 
				</SELECT>
			</td>
	</tr>			
									
	<TR>
		<td class=label align="right" nowrap>Report Type:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><SELECT NAME="select_reportType" style="width: 80px">			 
			<option value="html">Html</option>
			<option value="ExcelSheet">Excel Sheet</option>
		</SELECT></td>

	</tr>
	 
	 
	<tr>
		<td align="center">&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
 

	<tr>
	<td colspan="2" align="center">
		 <input type="button" class="btn" name="Submit"	 value="Submit" onclick="javascript:validateForm()"> 
		 <input	type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
		 <input type="button" class="btn" name="Submit" value="Cancel"	onclick="javascript:history.back(-1)">
	</td>
	</tr>
	<tr>
		<td align="center"></td>
	</tr>
</table>
</td>
</tr>
</table>


</form>
<div id="process"
	style="position: fixed; width: auto; height: 35%; top: 200px; right: 0; bottom: 100px; left: 10em;"
	align="center"><img
	src="<%=basePath%>PensionView/images/Indicator.gif" border="no"
	align="middle" /> <SPAN class="label">Processing.......</SPAN></div>

</body>
</html>
 