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

    String region="",empsrlNo="",dataFlag="",message="", accessCode="",verifiedby="",userType="",processedStage="",chkPfidTracking="",userId="";
  	String notFinalisdYears="",searchOnFinYear="",searchOnStatus="";
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
	  if(request.getAttribute("searchOnStatus")!=null){
	searchOnStatus = (String)request.getAttribute("searchOnStatus");
	}
	  if(request.getAttribute("searchOnFinYear")!=null){
	searchOnFinYear = (String)request.getAttribute("searchOnFinYear");
	}
	 System.out.println("==== searchOnFinYear=="+searchOnFinYear+"==searchOnStatus=="+searchOnStatus); 
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
  if(request.getAttribute("adjObYears")!=null){
   notFinalisdYears=(String)request.getAttribute("adjObYears");
  System.out.println("=notFinalisdYears  =="+notFinalisdYears);
  }
   
   
	if(accessCode.equals("PE040201")){
	processedStage="Initial";
	}else if(accessCode.equals("PE040202")){
		processedStage="Approved";
	  }
	  
	  
	System.out.println("==verifiedby==="+verifiedby+"=="+request.getAttribute("verifiedby")+"==message==="+message+"==userType=="+userType); 
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
	    <script type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></script>
		 
	<script type="text/javascript"><!-- 
	
	var accessCode='<%=accessCode%>',userType='<%=userType%>',userId='<%=userId%>';
	var  verifiedby='<%=verifiedby%>',chkPfidTracking='<%=chkPfidTracking%>'; 
	function resetVals(){
	 
	document.forms[0].empsrlNo.value="";
	document.forms[0].adjobyear.value="";
	document.forms[0].status.value="";
	 
	     
	}
	
	function editRecord(empserialNO,employeeName,reportYear,approvedStatus) {
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
      
      //By Radha oN 19-Nov-2012 request  By Shikha
       /* if(reportYear=="2010-2011"){
        if(!(userId=="navayuga" || userId=="SHIKHA" )) {
        alert(" U Don't Have Privilages to Edit 2010-2011 Data");
        return false;
        }
        }*/
         
           //By Radha On 19-Dec-2012 as per Sehgal  Request thru mail
 		if(reportYear=="1995-2008" || reportYear=="2008-2009" || reportYear=="2009-2010"){
        if(userId=="KCARORA" || userId=="ANILKUMAR") {
        alert(" U Don't Have Privilages to Edit "+reportYear+" Data");
        return false;
        }
        }   
          if(approvedStatus=="processing" ||  approvedStatus=="Reject"){ 
                  alert("Initial Stage is processing...You Cant do Changes");
              		 return false;
         }else if(approvedStatus==""){ 
                  alert("Not Entered in Initial Stage");
              		 return false;
         }
           
             
		var frm_toyear=<%=year%>;
		var frm_month=<%=month%>;
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frm_year="+frm_year+"&frm_toyear="+frm_toyear+"&frm_month="+frm_month+"&reportYear="+reportYear+"&empName="+employeeName+"&accessCode="+accessCode;
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
     	status=    document.forms[0].status.value;
     	 
     	url="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode="+accessCode+"&reportYear="+adjobyear+"&status="+status+"&searchFlag=S&frmName=adjcorrections";
    	 
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
   function getDelete(employeeNo,reportYear,approvedStatus){ 
		var url ="";
		
		 //By Radha On 19-Dec-2012 as per Sehgal  Request thru mail
 		if(reportYear=="1995-2008" || reportYear=="2008-2009" || reportYear=="2009-2010"){
        if(userId=="KCARORA" || userId=="ANILKUMAR") {
        alert(" U Don't Have Privilages to Delete "+reportYear+" Data");
        return false;
        }
        }   
		
			if(approvedStatus=="Approved"){
              		alert("Approval Stage is Finalized.You Cant Delete it");
              		 return false;
              }else{
		
   var answer =confirm('Are you sure, Do you want to delete the year '+reportYear);
   createXMLHttpRequest();
   if(answer){
   var flag="true";
   url="<%=basePath%>reportservlet?method=getDeleteAllRecords&accessCode="+accessCode+"&empserialno="+employeeNo+ "&reportYear="+reportYear+"&frmName=adjcorrections";
			xmlHttp.open( "post", url, true);
			xmlHttp.onreadystatechange = getDeletemsg;
			
		}else{
			document.forms[0].formType.focus();
		}
		
		
		
		xmlHttp.send(null);
    }
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
    function RementenceMonths()
  {    
       document.forms[0].ApproverMonth.readOnly=false;
        document.forms[0].ApproverMonth.style.background='#FFFFCC';	
        document.forms[0].ApproverMonth.value=document.forms[0].ApproverMonth.value,show_calendar('forms[0].ApproverMonth');
      }
  function updateStatus(employeeNo,reportYear,approvedStatus,rownum){ 
 var status='',stageStatus="",processedStage="",jvnostatus="";
  var pfid_statusboxno = "pfid_status"+rownum;
  var jvno_statusboxno = "jvno"+rownum;
  //var ApproverMonthboxno ="ApproverMonth"+rownum;
 	//alert(document.getElementsByName(ApproverMonthboxno)[0].value);
	status =  document.getElementsByName(pfid_statusboxno)[0].value;
	//approvemonth=document.getElementsByName(ApproverMonthboxno)[0].value;
   //alert(approvemonth.substr(0,2)); 
  // if(approvemonth.substr(0,2)<15){
  // approvemonth='15'+approvemonth.substr(2,approvemonth.length);
   //}
   //alert(approvemonth);
   //alert(document.getElementById(pfid_status)[0].value);
  // alert(pfid_statusboxno);
   var notFinalisdYears= new Array();
   var notFinalisdYearsStr="",chkPfidTrackingResult ="";
   notFinalisdYearsStr='<%=notFinalisdYears%>';
   notFinalisdYears=notFinalisdYearsStr.split(":");
  // alert(str.length);
   //alert("You Can't Approve the PFID,Due to Control Accounts Processing....");
   //document.getElementsByName(pfid_statusboxno)[0].value="NO-SELECT";
    	 //return false;

    if(status!="NO-SELECT"){ 
    
    for(var j=0;j<notFinalisdYears.length;j++)
  		{ if(notFinalisdYears[j]==reportYear){
 			 notFinalisdYearsResult  ="true";
 			 notFinalisdYears.length=0;
  		}else{
  			 notFinalisdYearsResult  ="false";
  			}
  		}
    
    
    if(notFinalisdYearsResult == "true"){
   alert("Please Finalize the Year "+reportYear+" Before Closing the Stage");
   document.getElementById(pfid_status)[0].value="NO-SELECT";
   return false;
   }
    
    stageStatus="Y";
    if(status=="reject"){
    processedStage ="Reject";
    }else{
    processedStage='<%=processedStage%>'; 
    }
    
     jvnostatus =  document.getElementsByName(jvno_statusboxno)[0].value;
     if((reportYear=="2010-2011" ||reportYear=="2011-2012") && (processedStage!="Reject")&& (jvnostatus=="")){     
     alert("Please Enter the JV No");
     document.getElementsByName(pfid_statusboxno)[0].value="NO-SELECT";
     document.getElementsByName(jvno_statusboxno)[0].focus();
     return false;
     }
    
     if((reportYear=="2010-2011" ||reportYear=="2011-2012") && (processedStage!="Reject")&& (trim(jvnostatus)=="")){     
     alert("Please Enter Valid JV No");
     document.getElementsByName(pfid_statusboxno)[0].value="NO-SELECT";
     document.getElementsByName(jvno_statusboxno)[0].focus();
     return false;
     }
    
    
	var url = "<%=basePath%>reportservlet?method=updateAdjCrtnStatus&pensionno="+employeeNo+"&stageStatus="+stageStatus+"&processedStage="+processedStage+"&accessCode="+'<%=accessCode%>'+"&reportYear="+reportYear+"&jvno="+jvnostatus+"&frmName=adjcorrections";  
  	//alert(url);
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
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
	 	}else if(userType=="NODAL OFFICER"){
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
  document.forms[0].status.value='<%=searchOnStatus%>';
  document.forms[0].adjobyear.value='<%=searchOnFinYear%>';
  
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
--></script>
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
						 	Calculate AdjOB On Monthly CPF Corrections[Approve Search]
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
							<tr>
								<td class="label" align="right"> 
									Status: 
								</td>
								<td>
									<select name="status" style="width:110px">
									<option value="" >[Select Status]</option>
									<option value="Initial">Initial</option>
									<option value="Reject">Reject</option>								 
								</select>   
								</td>
							 
							 
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
								<td align="center" width="90%">
														
									<display:table  style="width: 920px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadPCReportForAdjDetails" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>')" />
								    </display:column>	
    								<% j++;%>
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="reportYear" title="Year"/>	
									<display:column property="empSubTot" title="Adj Emp Sub"/>	
									<display:column property="aaiContriTot"  title="Adj AAI Contri" />									
									<display:column property="pensionTot"  title="Adj Pension contri" />
									 
									<display:column property="approverName"  title="Edited By" /> 
									<%
								  	 if(((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus().equals("Reject")){
								   	 %>
								   	 <display:column title="Update Status" media="html">
								    <%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>
								    </display:column>	
								<%}else{%>
								<display:column title="Update Status" media="html">
									 <select name="pfid_status<%=j%>"  style="width: 80px;height:30px; align: center;" onchange="updateStatus('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','<%=j%>');">
									 <option value="NO-SELECT">Select One</option>
									 <option value="accept">Accept</option>	 
								  	 <option value="reject">Reject</option>	
								 </select> 
								  </display:column>	
								 <%}%> 
								 <display:column title="JV No" media="html">
							      <input type ="text" name="jvno<%=j%>" id="jvno"  value="<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getJvNo()%>"/>									 
								 </display:column>

								 <display:column title="Delete" media="html">
								 <a href="#" onclick="getDelete('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>')"><img src="./PensionView/images/delete.gif" border="0" alt="DELETE" /></a> 
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
