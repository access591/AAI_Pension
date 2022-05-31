<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*,java.text.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%> 
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/tags-display" prefix="display"%>
<%
String path = request.getContextPath();
Calendar cal = Calendar.getInstance(); 
 		 int month = cal.get(Calendar.MONTH)+7;
 		 System.out.println(month);    
		 int year=cal.get(Calendar.YEAR);
		 System.out.println("month "+month +"year "+year);
		 if(month>=12){
			  month=month-12;
			  year = cal.get(Calendar.YEAR)+1; 
		 }
// System.out.println("after month "+month +"after year "+year);
 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String region="",empsrlNo="",dataFlag="",message="", accessCode="",verifiedby="",userType="",processedStage="",chkPfidTracking="",userId="",reportYear="",editStatus="CHQNotEdited";
  	String notFinalisdYears="";
  	CommonUtil common=new CommonUtil();	 
   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
	session.removeAttribute("getSearchBean1");	
	}
	if(session.getAttribute("usertype")!=null){
	userType = (String)session.getAttribute("usertype");	
	} 
  if(request.getAttribute("empsrlNo")!=null){
	empsrlNo = (String)request.getAttribute("empsrlNo");
	}
	if(request.getAttribute("reportYear")!=null){
	reportYear = (String)request.getAttribute("reportYear");
	}
	 
	if(request.getAttribute("searchInfo") == null){
	dataFlag="NoData";
	} 
  	
 String array[] ={"1570","9369","9605","10115","13772","21048"};
 boolean logsFlag = false;
 for(int i=0;i<array.length;i++){
 if(empsrlNo.equals(array[i])){
    logsFlag = true;
    i=array.length;
 }
 }
  if(request.getAttribute("message")!=null){
   message=(String)request.getAttribute("message");
  }
  if(request.getAttribute("accessCode")!=null){
   accessCode=(String)request.getAttribute("accessCode");
  }
  if(request.getAttribute("verifiedby")!=null){
   verifiedby=(String)request.getAttribute("verifiedby");
  }
   if(request.getAttribute("chkPfidTracking")!=null){
   chkPfidTracking=(String)request.getAttribute("chkPfidTracking");
  }
   
   
	if(accessCode.equals("PE040201")){
	processedStage="Initial";
	}else if(accessCode.equals("PE040202")){
		processedStage="Approved";
	  }else if(accessCode.equals("PE04020601")){
	  		processedStage="CHQApproved";
	  }
	  
	  
System.out.println("==verifiedby==="+verifiedby+"=="+request.getAttribute("verifiedby")+"==message==="+message+"==userType=="+userType); 
 System.out.println("==processedStage==="+processedStage);
  userId = (String) session.getAttribute("userid");
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
	
	var accessCode='<%=accessCode%>',userType='<%=userType%>',userId='<%=userId%>';
	var  verifiedby='<%=verifiedby%>',chkPfidTracking='<%=chkPfidTracking%>'; 
	var CHQEditStatus="",employeeNo_temp,reportYear_temp,form2Status_temp,pfid_statusboxno_temp;
	function resetVals(){
	 
	document.forms[0].empsrlNo.value="";
	document.forms[0].adjobyear.value="";
	document.forms[0].status.value="";
	 
	     
	}
	
	function editRecord(empserialNO,employeeName,reportYear,approvedStatus,form2Status,frozen) {
	 	var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var empserialNO	=empserialNO;		 
		//reportType="ExcelSheet";
		reportType="Html";
		yearID="NO-SELECT";
		var page="PensionContributionScreen";
		var mappingFlag="true";
        var frm_year="1995";
        var claimsprocess=claimsprocess;
      
        if(claimsprocess=="Y"){
        alert("PensionClaim Process Already Done, User doesn't have Permissions to View/Edit TransactionData");
        return false;
        }
      
        if(reportYear=="2010-2011"){
        if(!(userId=="navayuga" || userId=="SHIKHA" )) {
        alert(" U Don't Have Privilages to Edit 2010-2011 Data");
        return false;
        }
        }
        
        if(form2Status=="B"){
        alert("Adjustments are in Processing.U can not Edit");
        return false;
        }
        
		if(form2Status=="M"){ 
        var result = confirm("As This is an Old Entry We don't know whether Form 2 submitted or not. If Submitted Click Ok Not For Cancel");
       	if(result){
       	form2Status ="Y";
       	}else{
       	form2Status ="N";
       	} 
        }
        
		if(frozen=="Y"){
        alert("Form2 already Submitted Please Submit Reverse Adjustment");
        return false;
        }  
    
		var frm_toyear=<%=year%>;
		var frm_month=<%=month%>;
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frm_year="+frm_year+"&frm_toyear="+frm_toyear+"&frm_month="+frm_month+"&reportYear="+reportYear+"&empName="+employeeName+"&accessCode="+accessCode+"&form2Status="+form2Status;
		var url="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtn"+params;
		 //alert(url);
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
		
	}

	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
    }
	
    function testSS(){ 
    	var formType='',url='',pensionno='',adjobyear='',status='';
    	pensionno=    document.forms[0].empsrlNo.value;    	 
     	adjobyear=    document.forms[0].adjobyear.value;
     	 
     	 
     	url="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode="+accessCode+"&reportYear="+adjobyear+"&searchFlag=S&frmName=adjcorrections";
    	 // alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
   	 
   
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
   function getDelete(employeeNo,reportYear,approvedStatus,form2Status,empSubTot,aaiContriTot,pensionTot){
  	var url ="",chqFlag="true";
 	 var answer =confirm('Are you sure, Do you want to delete the year '+reportYear);
   createXMLHttpRequest();
   if(answer){
   var flag="true";
   //Only for Form2 Submitted cases frozen flag Updations
   if(form2Status=="Y"){
   url="<%=basePath%>reportservlet?method=getCHQDeletion&accessCode="+accessCode+"&empserialno="+employeeNo+ "&reportYear="+reportYear+"&form2Status="+form2Status+"&chqFlag="+chqFlag+"&empSubTot="+empSubTot+"&aaiContriTot="+aaiContriTot+"&pensionTot="+pensionTot+"&frmName=adjcorrections";
	}else{
	url="<%=basePath%>reportservlet?method=getDeleteAllRecords&accessCode="+accessCode+"&empserialno="+employeeNo+ "&reportYear="+reportYear+"&form2Status="+form2Status+"&chqFlag="+chqFlag+"&empSubTot="+empSubTot+"&aaiContriTot="+aaiContriTot+"&pensionTot="+pensionTot+"&frmName=adjcorrections";
	  }
   		xmlHttp.open( "post", url, true);
			xmlHttp.onreadystatechange = getDeletemsg;
			
		}else{
			document.forms[0].formType.focus();
		}
		
		
		
		xmlHttp.send(null);
  
    }
    	function getDeletemsg()
	{
	
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			
		}
		if(xmlHttp.readyState ==4)
		{
			if(xmlHttp.status == 200)
				{ 
			      
		 
		 	
		  			alert("SuccessFully Deleted");
		 	  var url="";
		 	  if(accessCode=="PE040201"){
		 	   url="<%=basePath%>reportservlet?method=loadAdjObCrtn&accessCode="+accessCode+"&frmName=adjcorrections";
	 	    }else{
		   url="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
		    }
		  	document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();
		  
	
		}
	}

	 
}
   
   function getlogs(empserialno,adjobyear){
   //alert('Under Processing........');
    var url="",logsFlag='<%=logsFlag%>';
    var swidth=screen.Width-350;
	var sheight=screen.Height-450;	 
	if(logsFlag == "true"){
	alert("We cannot display Logs  for this pfid "+empserialno+" due to some issues");
	}else{
  		url ="<%=basePath%>reportservlet?method=getadjemolumentslog&empserialno="+empserialno+"&adjobyear="+adjobyear+"&frmName=adjcorrections";
		 wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		 winOpened = true;
		 wind1.window.focus();
		 }
   }
    function getNodeValue(obj,tag)
   	{
		return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   	}
 
 function getEditStatus()
	{
		if (xmlHttp.readyState==4 && xmlHttp.status==200){
    		var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		 	var editStatus = getNodeValue(stype[0],'EditStatus');	
		    var stageStatus="Y"; 
		    var  processedStage='<%=processedStage%>'; 
		    if(editStatus=="CHQEdited"){		 
			var url = "<%=basePath%>reportservlet?method=updateAdjCrtnStatus&pensionno="+employeeNo_temp+"&stageStatus="+stageStatus+"&processedStage="+processedStage+"&accessCode="+'<%=accessCode%>'+"&reportYear="+reportYear_temp+"&form2Status="+form2Status_temp+"&frmName=adjcorrections";  
 			
 			employeeNo_temp="";
 			reportYear_temp="";
 			form2Status_temp="";
 			pfid_statusboxno_temp="";
 			//alert(url);
	  		document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();
			}else{
			alert("Please Do Changes for PFID Then Update Status");
			document.getElementsByName(pfid_statusboxno_temp)[0].value="NO-SELECT";
		 	document.getElementsByName(pfid_statusboxno_temp)[0].focus();
			return false; 
			pfid_statusboxno_temp="";
		} 
		              
 }
}
  function updateStatus(employeeNo,reportYear,approvedStatus,form2Status,rownum){
  var status='',stageStatus="",processedStage="";
  var pfid_statusboxno = "pfid_status"+rownum; 	 
  status =  document.getElementsByName(pfid_statusboxno)[0].value;
 createXMLHttpRequest();
// alert(status);
    if(status!="NO-SELECT"){
    var stageStatus="Y"; 
    processedStage='<%=processedStage%>'; 
    if(form2Status=="Y"){
    employeeNo_temp=employeeNo;
    reportYear_temp=reportYear;
    form2Status_temp=form2Status;
   pfid_statusboxno_temp=pfid_statusboxno;
     
    var url ="<%=basePath%>reportservlet?method=getCHQApproverEditStatus&pensionno="+employeeNo+"&reportYear="+reportYear+"&frmName=adjcorrections";
		// alert(url);		
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getEditStatus
   xmlHttp.send();  
   }else{ 
    var url = "<%=basePath%>reportservlet?method=updateAdjCrtnStatus&pensionno="+employeeNo+"&stageStatus="+stageStatus+"&processedStage="+processedStage+"&accessCode="+'<%=accessCode%>'+"&reportYear="+reportYear+"&form2Status="+form2Status+"&frmName=adjcorrections";  
 	 
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
    } 
} 
 
}

 function  gotoback(pensionno){ 
 var url=""; 
  if(accessCode=="PE040202"){  
   url= "<%=basePath%>reportservlet?method=searchAdjRecords&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
   }else if(accessCode=="PE040201"){
   url= "<%=basePath%>reportservlet?method=loadAdjObCrtn&accessCode="+accessCode+"&frmName=adjcorrections";
   }
  	//alert(url); 
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }

   function loadDet(){ 
  var message="<%=message%>";
  var url,  empsrlNo;
 
  if(message!='' && message=="U Don't Have Privilages to Access"){ 
  var ee;   
  ee = alert(message);
if(ee!=''){ 
		if(userType=="Admin"){
		url="<%=basePath%>PensionView/PensionMenu.jsp";
		}else if(userType=="User"){
		url="<%=basePath%>PensionView/PensionMenu2.jsp";
	 	}
        document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
  } 
  return false;
  }
  
  	  
  	  empsrlNo = document.forms[0].empsrlNo.value;   
  	  if(empsrlNo!='') {
  	  if(message!=""){
  	  if(message!="U Don't Have Privilages to Access"){
  	   alert(message);
  	    return false;
  	  }
  	  }
  
         
  }
    document.forms[0].adjobyear.value='<%=reportYear%>';
    var dataFlag='<%=dataFlag%>',empNo='<%=empsrlNo%>';
    //alert("--flag--"+dataFlag+"-empNo--"+empNo);
    if(dataFlag=='NoData'){
     document.getElementById("norec").style.display="block";
    }else{
     document.getElementById("norec").style.display="none";
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
 </script>
	</head>

	<body class="BodyBackground"  onload="javascript:loadDet();" onkeypress="return disableKeyPress(event)" >
		<form name="test" action="" >
			 
					<%boolean flag = false;%>
				 
				
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
				<table width="85%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="4" align="center" class="ScreenMasterHeading">
						 	Calculate AdjOB On Monthly CPF Corrections[CHQ Approver Search]
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				 
							<tr>
								<td>
									<table width="50%" border="0" align="center" cellpadding="1" cellspacing="0" >
									<tr>
								<td class="label" align="right"> 
									 PFID: 
								</td>
								<td>
									<input type="text" name="empsrlNo" value="<%=empsrlNo%>" onkeypress="return disableEnterKey(event)"/> 
								</td>
							 
							<td class="label"  align="right">
									Report Year:
								</td>
								<td>
								<select name="adjobyear" style="width:110px">
									<option value="" >[Select Report]</option>
									<option value="1995-2008">1995-2008</option>
									<option value="2008-2009">2008-2009</option>
									<option value="2009-2010">2009-2010</option>
									<option value="2010-2011">2010-2011</option>
									<option value="2011-2012">2011-2012</option>								 
								</select></td>
							</tr>
							 
							</table>
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
				 
				<table>
					<tr>
						<td>&nbsp;</td>
					</tr> 
				</table> 
					<%if (request.getAttribute("searchList") == null) {
					 
					%> 
                   
						<table align="center" id="norec">
							<tr>
							 
							<td><b> No Records Found </b></td>
							</tr>
                         </table> 
			
					<%}else{%>
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"> 
				   	
				   	  <tr>							         
								 <% int j =0;%>	
								<td align="center" width="85%">
														
									<display:table  style="width: 920px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadPCReportForAdjDetails" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getForm2Status()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getFrozen()%>')" />
								    </display:column>	
    								<% j++;%>
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="reportYear"  title="Year"/>	
									<display:column property="empSubTot" title="Adj Emp Sub"/>	
									<display:column property="aaiContriTot"  title="Adj AAI Contri" />									
									<display:column property="pensionTot"  title="Adj Pension contri" />
									 
									<display:column property="approverName"  title="Approved By" /> 
									<display:column property="form2StatusDesc"  title="Form2Status" /> 
									<%
								  	 if(((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus().equals("Reject")){
								   	 %>
								   	 <display:column title="Update Status" media="html">
								    <%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>
								    </display:column>	
								<%}else{%>
								<display:column title="Update Status" media="html">
									 <select name="pfid_status<%=j%>"  style="width: 80px;height:30px; align: center;" onchange="updateStatus('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getForm2Status()%>','<%=j%>');">
									 <option value="NO-SELECT">Select One</option>
									 <option value="accept">Accept</option>	 
								  	  
								 </select> 
								  </display:column>	
								 <%}%> 
								 <display:column title="Delete" media="html">
								 <a href="#" onclick="getDelete('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getForm2Status()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpSubTot()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getAaiContriTot()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPensionTot()%>')"><img src="./PensionView/images/delete.gif" border="0" alt="DELETE" /></a> 
								 </display:column> 
								<display:column title="Logs" media="html">
									<a href="#" onclick="getlogs('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>')"><img src="./PensionView/images/viewDetails.gif" border="0" alt="Logs" /></a>
								 </display:column>
									</display:table>
								</td>
							</tr>
							
		
	 
				

						</table>  
					<%}%>
					
		</form>
	</body>
</html>
