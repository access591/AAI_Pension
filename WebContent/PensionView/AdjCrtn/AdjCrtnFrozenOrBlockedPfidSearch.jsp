
<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*,java.text.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%> 
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/tags-display" prefix="display"%>


<%
String  message="",userType="";

String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
		String accessCode="";			
	if(request.getAttribute("accessCode")!=null){
    accessCode=(String)request.getAttribute("accessCode");
  }	
  if(request.getAttribute("message")!=null){
	message=(String)request.getAttribute("message");
  }
  if(session.getAttribute("usertype")!=null){
	userType = (String)session.getAttribute("usertype");
	
	}			
					
    
  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />
    	<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />    	 
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		 
	<script type="text/javascript"> 
	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
	}
	function testSS(){  
    
    	var formType='',url='',pensionno='',empName='';
    	empsrlNo=    document.forms[0].empsrlNo.value;    	 
  
     	url="<%=basePath%>reportservlet?method=searchFrozenOrBlockedRecords&empsrlNo="+empsrlNo;
    	
    	// alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
   	 
   	 function resetVals(){
	 
	document.forms[0].empsrlNo.value="";
	 
	     
	}
   	 
   	   	function createXMLHttpRequest(){
		if(window.ActiveXObject){
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 	}else if (window.XMLHttpRequest){
			xmlHttp = new XMLHttpRequest();
		 }
	}
	function updateRecordBlocked(employeeNo,trackId,adjobyear,appType){
	
	//alert(adjobyear);
	
		
   		var formType='',url='',pensionno='',empName='',notes='',frozen='',blocked='';
    	
    	notes=document.getElementsByName("notes"+trackId)[0].value;
    	//frozen=document.getElementsByName("frozen"+trackId)[0].value;
    	blocked=document.getElementsByName("blocked"+trackId)[0].value;
   
    if(appType=='Initial-Processing'){
	alert("Approved Stage is processing...You Can't Blocked ");
	document.getElementsByName("blocked"+trackId)[0].value='N';
	return false;
	}
    	
    	createXMLHttpRequest();
    
    	url="<%=basePath%>reportservlet?method=updateApprovedRecordBlock&empsrlNo="+employeeNo+"&trackId="+trackId+"&notes="+notes+"&blocked="+blocked+"&frozen="+frozen+"&adjobyear="+adjobyear;
   	 	xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getBlockedList;
		xmlHttp.send(null);
   	 }

	function getBlockedList(){
	
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 	
		}
		if(xmlHttp.readyState ==4){
		alert("SuccessFully Blocked Updated");
		
		
			if(xmlHttp.status == 200){ 
			
				var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
				
				if(stype.length==0){
				 	var obj1 = document.getElementById("blocked");
				 	obj1.options.length=0; 
		  			obj1.options[obj1.options.length]=new Option('[Select One]','','true');
				}else{
		 		   	var obj1 = document.getElementById("blocked");
		 		  	obj1.options.length = 0;
				 	for(i=0;i<stype.length;i++){
		  				if(i==0){
							obj1.options[obj1.options.length]=new Option('[Select One]','','true');
						}
						obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'blocked'),getNodeValue(stype[i],'blocked'));
					} 
					alert("SuccessFully Updated");
					
		  		}
			}
		}
	}
	function updateRecordFrozen(employeeNo,trackId,adjobyear,appType){
	if(appType=='Initial-Processing'){
	alert("Approved Stage is processing...You Can't Frozen ");
	document.getElementsByName("frozen"+trackId)[0].value='N';
	return false;
	}
		
   		var formType='',url='',pensionno='',empName='',notes='',frozen='',blocked='';
    	
    	notes=document.getElementsByName("notes"+trackId)[0].value;
    	frozen=document.getElementsByName("frozen"+trackId)[0].value;
    	blocked=document.getElementsByName("blocked"+trackId)[0].value;
    	//alert(frozen+blocked);
    	
    	createXMLHttpRequest();
    
    	url="<%=basePath%>reportservlet?method=updateApprovedRecordFrozen&empsrlNo="+employeeNo+"&trackId="+trackId+"&notes="+notes+"&blocked="+blocked+"&frozen="+frozen+"&adjobyear="+adjobyear;
   	 	xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getFrozenList;
		xmlHttp.send(null);
   	 }
 	

	function getFrozenList(){
	
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 	
		}
		if(xmlHttp.readyState ==4){
		alert("SuccessFully Frozen Updated");
			if(xmlHttp.status == 200){ 
			
				var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
				
				if(stype.length==0){
				 	var obj1 = document.getElementById("frozen");
				  	obj1.options.length=0; 
		  			obj1.options[obj1.options.length]=new Option('[Select One]','','true');
				}else{
		 		   	var obj1 = document.getElementById("frozen");
		 		  	obj1.options.length = 0;
				 	for(i=0;i<stype.length;i++){
		  				if(i==0){
							obj1.options[obj1.options.length]=new Option('[Select One]','','true');
						}
						obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'frozen'),getNodeValue(stype[i],'frozen'));
					}
		  		}
			}
		}
	}
	function loadDet(){ 
  var message="<%=message%>";
  var userType="<%=userType%>";
  var url,  empsrlNo;
 
  if(message!='' && message=="U Don't Have Privilages to Access"){ 
  var ee;   
  ee = alert(message);
if(ee!=''){ 
		if(userType=="Admin"){
		url="<%=basePath%>PensionView/PensionMenu.jsp";
		}else if(userType=="User"){
		url="<%=basePath%>PensionView/PensionMenu5.jsp";
	 	}else if(userType=="NODAL OFFICER"){
		url="<%=basePath%>PensionView/PensionMenu2.jsp";
	 	}
        document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
  } 
  return false;
  } 
  }
  function disableEnterKey(e)
{
     var key;      
     if(window.event)
          key = window.event.keyCode; //IE
     else
          key = e.which; //firefox      

     return (key != 13);
}
function blockedPfidsReport(){
	var url='',reportType='';
	reportType=document.forms[0].reportType.value;
	if(reportType==""){
	return false;
	}
	url="<%=basePath%>reportservlet?method=getBlockedPfidsReport&reporttype="+reportType;
	var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
}
	
</script>
	</head>

	<body class="BodyBackground" onload="javascript:loadDet();" onkeypress="return disableKeyPress(event)">
		<form name="test" action="" >
			 
					
				 
				
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
				<table width="90%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="4" align="center" class="ScreenMasterHeading">
						 	Blocking PFID's of Impact Calculator Screen
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				 
							<tr>
								
								<td class="label" align="right"> 
									Form3-2007-Sep- PFID:<font color=red></font>
								</td>
								<td>
									<input type="text" name="empsrlNo" value="" /> 
								</td>
							</tr>
	
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="center" colspan="5">
									<input type="button" class="btn" value="Search"   name="Submit" onclick="javascript:testSS();"/>
									<input type="button" class="btn" value="Reset"  name="Reset"  onclick="javascript:resetVals();" />
									<input type="button" class="btn" value="Cancel" name="Cancel"   onclick="javascript:history.back(-1);"/>
								</td>

							</tr>
						 
					</table>
					
					<%
			 
				       if (request.getAttribute("searchList") != null) {
				       ArrayList empSearchList=new ArrayList();		      
				       
				       
				       empSearchList=(ArrayList)request.getAttribute("searchList");
				         System.out.println("----------"+empSearchList.size()); 
						String bgcolor="";
				%>
				
				
				<table width="90%" border="0" align="center" cellpadding="1" cellspacing="0" >
							
							<tr>
							<td>
						 	&nbsp;
							</td>
							<td>
						 	&nbsp;
							</td>
						 
						</tr>
						
				 
							<tr>
								
								<td width="50%" class="label" align="right"> 
									Blocked PFID's Report:<font color=red></font>
								</td>
								<td>&nbsp;&nbsp;<SELECT NAME="reportType" style="width: 88px" onchange="blockedPfidsReport();">
									<option value="">Select One</option>
									<option value="html">Html</option>
									<option value="ExcelSheet">Excel Sheet</option>
									</SELECT>
								</td>
							</tr>
	
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							
						 
					</table>
				
			
				
				<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
									
				<tr>
								
														
								<td align="center" width="95%">
														
									<display:table  style="width: 800px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadPCReportForAdjDetails" >  
	
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    								
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="dateofBirth" title="DateofBirth"/>	
									<display:column property="dateofJoining" title="DateofJoining"/>
									<display:column property="reportYear" title="AdjOBYear"/>
									<display:column property="empSubTot" title="AdjOB Emp SubScription"/>
									<display:column property="aaiContriTot" title="AdjOB AAI Contri"/>	
									<display:column property="pensionTot" title="AdjOB Pension Contri"/>
									<display:column property="userName"  title="Approved By" />									
									<display:column property="verifiedBy"  title="Approved Status" />
								
									<display:column  title="Notes" ><input type="text" maxlength="250" name="notes<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getTrackingId()%>" value="<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getNotes() %>" /></display:column>
								
									<display:column    title="Blocked" ><select Style='width:45px'  id="blocked<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getTrackingId() %>" name="blocked<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getTrackingId() %>" onchange="updateRecordBlocked('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getTrackingId() %>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear() %>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getVerifiedBy() %>');">
										 <option value="Y" <%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getBlock().equals("Y")?"selected":"" %>>Yes</option>
										 <option value="N" <%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getBlock().equals("N")?"selected":"" %>>No</option>
										 </select></display:column>
						
									</display:table>
								</td>
							</tr>
							
		<%}%>
					
					 
			
  </body>
</html>

