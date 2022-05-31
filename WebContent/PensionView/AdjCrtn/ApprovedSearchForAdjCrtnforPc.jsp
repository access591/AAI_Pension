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

    String region="",empsrlNo="",dataFlag="",message="", accessCode="",reportYear="",userType="",processedStage="",chkPfidTracking="",userId="";
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
	  
	  
	System.out.println("==message==="+message+"==userType=="+userType); 
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
	var  chkPfidTracking='<%=chkPfidTracking%>'; 
	function resetVals(){
	 
	document.forms[0].empsrlNo.value="";
	 document.forms[0].adjobyear.value="";
	     
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
     	  
     	url="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode="+accessCode+"&reportYear="+adjobyear+"&searchFlag=S&frmName=adjcorrections";
    	 
    	 // alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
   	function getlogs(empserialno,adjobyear){
   //alert('Under Processing........');
    var url="",logsFlag='<%=logsFlag%>';
    var swidth=screen.Width-350;
	var sheight=screen.Height-450;	 
	if(logsFlag == "true"){
	alert("We cannot display Logs  for this pfid "+empserialno+" due to some issues");
	}else{
  		url ="<%=basePath%>pcreportservlet?method=getadjemolumentslogforPc&empserialno="+empserialno+"&adjobyear="+adjobyear+"&frmName=adjcorrections";
		 wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		 winOpened = true;
		 wind1.window.focus();
		 }
   }
   

   function callReports(empsrlNo,empName,rownum){
   var url='',formType='';
    
    var formTypeboxno = "formType"+rownum;
 	//alert(formTypeboxno);
	formType =  document.getElementsByName(formTypeboxno)[0].value;
   if(formType=="form7ps"){
    url="<%=basePath%>reportservlet?method=loadform7Input&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;
    
      }
   if(formType=="form8ps"){
    url="<%=basePath%>reportservlet?method=loadform8params&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;
   
   }
   if(formType=="statementofwages"){
    url="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;   
   } 
  // alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   
   }
   	 function getpcreport(empserialNO,adjOBYear) {
   	 	 
       	var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;		 
		regionID="NO-SELECT"; 
		var pfidStrip='1 - 1'; 
		yearID="NO-SELECT";
		monthID="NO-SELECT"; 
        var page = "report";
		 
		//alert(url);
	 
		var comfirmMsg = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
		if (comfirmMsg== true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}
		var params = "&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&reportYear="+adjOBYear+"&page="+page;
		var url="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc"+params;
		  //alert(url);
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 				 		wind1 = window.open(url,"PCReport","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
   	  
		
	 
	
	}
   
      	function getpcreport1(){ 
        alert(" Under Processing....");
    	 return false;
   	 }
      	function pfcard1(){ 
        alert(" Under Processing....");
    	 return false;
   	 }	
 function  pfcard(empSerialNo,empName,adjOBYear){  
 var url="",reportType="";
 var pfFlag="true";
 var swidth=screen.Width-10;
 var sheight=screen.Height-150;
 
 
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}	
 	url ="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc&empserialNO="+empSerialNo+"&reportYear="+adjOBYear+"&pfFlag="+pfFlag+"&empName="+empName+"&frm_reportType="+reportType;
	 // alert(url);
		 
   	 				 		wind1 = window.open(url,"PFCard","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 		 
  		  
 
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

   
	  
    
function  getForm2Report(repName,pensionno,form2Status,adjobyear,form2id){       
 if(form2Status=="M"){
	 alert("We aren't maintain the previous Form-2 adjustment Status");
 	return false;
 }
 	var url="";
 url = "<%=basePath%>reportservlet?method=uploadToForm2&frmName=adjcorrections&pensionno="+pensionno+"&adjobyear="+adjobyear+"&form2id="+form2id; 
  	//alert(url);
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
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
 document.forms[0].adjobyear.value='<%=reportYear%>';
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
  
        if(accessCode=="PE040202"){
         	 if(verifiedby==""){ 
                  alert("Not Entered in Initial Stage");
              		 return false;
              }
            }
  }
    var dataFlag='<%=dataFlag%>',empNo='<%=empsrlNo%>';
    //alert("--flag--"+dataFlag+"-empNo--"+empNo);
    if(dataFlag=='NoData' && empNo!=''){
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
						 	Impact Calculate For PC[Approved Search]
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
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"><!--
				   	
				 			--><tr>							
								 	<% int j =0;%>	
								<td align="center" width="90%">
														
								<display:table  style="width: 930px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./pcreportservlet?method=loadPCReportForAdjDetailsforPc" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    											 
    								<display:column property="pfid" title=" PF ID " class="datanowrap"/>
    								<% j++;%>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="reportYear" title="Year" class="datanowrap"/>	
									<display:column property="empSubTot" title="Adj Emp Sub"/>	
									<display:column property="aaiContriTot"  title="Adj AAI Contri" />									
									<display:column property="pensionTot"  title="Adj Pension contri" />																		 
									<display:column property="approverName"  title="Approved By" class="datanowrap"/> 
									<display:column property="form2StatusDesc"  title="Form4Status" /> 
									<display:column title="Logs" media="html">
									<a href="#" onclick="getlogs('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>')"><img src="./PensionView/images/viewDetails.gif" border="0" alt="Logs" /></a>
									</display:column>	
									<%if(((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear().equals("1995-2008")){%>
									<display:column title="Report" media="html">
									<a href="#" onclick="getpcreport('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>')"><img src="./PensionView/images/pc_report.gif" border="0"  /></a>
								    </display:column>	
								  <%}else{%>
								    <display:column title="Report" media="html">
									 <a href="#" onclick="pfcard('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getReportYear()%>')"><img src="./PensionView/images/pfcard_report.gif" border="0"  /></a>
								     </display:column>	
								  <%}%>
								    </display:table>
								</td>
							</tr> 
						</table>  
					<%}%>
					
		</form>
	</body>
</html>
