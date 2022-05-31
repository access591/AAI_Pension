<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.AaiEpfForm11Bean" %>
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

    String region="",empsrlNo="",dataFlag="",frmName="",message="", accessCode="",userType="",user="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
	session.removeAttribute("getSearchBean1");
	
	}
	 
if(request.getAttribute("empsrlNo")!=null){
	empsrlNo = (String)request.getAttribute("empsrlNo");
	}
	if(request.getAttribute("dataFlag")!=null){
	dataFlag = (String)request.getAttribute("dataFlag");
	}
	if(request.getAttribute("frmName")!=null){
	frmName = (String)request.getAttribute("frmName");
	}
	if(request.getAttribute("message")!=null){
   message=(String)request.getAttribute("message");
  }
  if(request.getAttribute("accessCode")!=null){
   accessCode=(String)request.getAttribute("accessCode");
  }
  if(session.getAttribute("usertype")!=null){
	userType = (String)session.getAttribute("usertype");
	
	}
	if(session.getAttribute("usertype")!=null){
	user = (String)session.getAttribute("userid");
	
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" /> 
    	<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
    	<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />
    	
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
	    <script type="text/javascript">  
	     var accessCode='<%=accessCode%>',userType='<%=userType%>',user='<%=user%>';
	function testSS() {
		var    url='', empsrlNo='' ;
		var swidth=screen.Width-10;
		var sheight=screen.Height-150; 
        var empserialNO = document.forms[0].empsrlNo.value;  
    //alert(formType);
   		 
		url="<%=basePath%>pcreportservlet?method=searchRevisionOptionPC&empsrlNo="+empserialNO+"&frmName="+'<%=frmName%>';
		
	 // alert(url); 
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
		
	}

	function editRecord(empserialNO,employeeName,status,secstatus) {
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
        var pageType="Page";
      
        if(status=="Approved" && user!='GOPALPAL'){
        alert(" Already Edit Done, User doesn't have Permissions to Edit TransactionData");
        return false;
        }
       if(secstatus=="Approved"){
        alert(" Already 2nd level Edit Done, User doesn't have Permissions to Edit TransactionData");
        return false;
        }
		 if(user=='GOPALPAL'){
		 	url="<%=basePath%>pcreportservlet?method=loadRevisionOptionPCSecLvl&accessCode="+accessCode+"&frm_pensionno="+empserialNO+"&pageType="+pageType+"&frmName=edit";
		 }else{
		//params = "&frm_region="+regionID+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&frmAirportCode="+airportcode+"&frm_pfids="+frm_pfids+"&adjFlag="+adjFlag+"&frmName="+frmName;
		url="<%=basePath%>pcreportservlet?method=loadRevisionOptionPC&accessCode="+accessCode+"&frm_pensionno="+empserialNO+"&pageType="+pageType+"&frmName=edit";
		}
	 //alert(url);
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
		
	}
	
     
    
   function getlogs(empserialno,adjobyear){
   alert('Under Processing........');
  /* var url="";
  		url ="<%=basePath%>reportservlet?method=getadjemolumentslog&empserialno="+empserialno+"&adjobyear="+adjobyear;
		document.forms[0].action=url;		
		document.forms[0].method="post";
		document.forms[0].submit();*/
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
  //alert("===empsrlNo=="+empsrlNo+"==message=="+message);
  	  
  	    empsrlNo = document.forms[0].empsrlNo.value;   
  	  if(empsrlNo!='') {
  	  if(message!=""){
  	  if(message!="U Don't Have Privilages to Access"){
  	   alert(message);
  	    return false;
  	  }
  	  }
   
  }
    var dataFlag='<%=dataFlag%>',empNo='<%=empsrlNo%>';
    //alert("--flag--"+dataFlag+"-empNo--"+empNo);
    if(empNo!=''){
     document.getElementById("norec").style.display="block";
    }else{
     document.getElementById("norec").style.display="none";
    }
   } 
   function resetVals(){
	//document.forms[0].empName.value="";
	document.forms[0].empsrlNo.value="";
	//document.forms[0].dob.value="";
	//document.forms[0].doj.value="";
	     
	}
	function  statemntwagesreport(empSerialNo,pfidin78ps,reportFlag){ 

 	var url="",reportType="";
 	var reportFormatType="";
 	var pageType="Report";
 	var swidth=screen.Width-10;
 	var sheight=screen.Height-150;
 	if(pfidin78ps==""||pfidin78ps==null){
 	
 	alert("Please Edit & Approve  and then Generate Report");
 	
 	
 	return false;
 	}
 	if(reportFlag=='NEWREPORT'){
 		reportFormatType=reportFlag;
 	reportFlag='Normal';
 
 	}
 //alert(reportFormatType);
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}	
 	url ="<%=basePath%>pcreportservlet?method=getReportRevPenContr&empserialNO="+empSerialNo+"&frm_formType=edit&frm_reportType="+reportType+"&pageType="+pageType+"&reportFlag="+reportFlag+"&reportFormatType="+reportFormatType;
	  //alert(url);
		 
   	 				 		wind1 = window.open(url,"12MnthStatementReport","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 		 
  		  
 
   }
   function  statemntwagesreport2(empSerialNo,pfidin78ps,reportFlag){ 
//alert(pfidin78ps);
 	var url="",reportType="";
 	var pageType="Report";
 	var swidth=screen.Width-10;
 	var sheight=screen.Height-150;
 	if(pfidin78ps==""||pfidin78ps==null){
 	
 	alert("Please Edit & Approve  and then Generate Report");
 	
 	
 	return false;
 	}
 
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}	
 	url ="<%=basePath%>pcreportservlet?method=getReportRevPenContr&empserialNO="+empSerialNo+"&frm_formType=edit&frm_reportType="+reportType+"&pageType="+pageType+"&reportFlag=SEC";
	 // alert(url);
		 
   	 				 		wind1 = window.open(url,"12MnthStatementReport","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 		 
  		  
 
   }
</script>
	</head>

	<body class="BodyBackground" >
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
   		<%boolean flag = false;%>
				 
						<table width="70%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="5" align="center" class="ScreenMasterHeading">
						 	Revision Option PC Corrections 
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
							<tr>
							 
								 <td   class="label" align="center">
									  PFID: 	<input type="text"  size="20" name="empsrlNo" value="<%=empsrlNo%>"/>
								</td>
							</tr>
							 <tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="center" colspan="5">
								 
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();"/>
									<input type="button" class="btn" value="Reset" onclick="javascript:resetVals();" class="btn"/>
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
								</td>

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
			 <table align="center">
							<tr>
							 
							<td> &nbsp;</td>
							</tr>
                         </table> 
			 <table width="70%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"> 
				   <% if(user.equals("GOPALPAL") ){%>	
				   	  <tr>							         
								 
								<td align="center" width="95%">
														
									<display:table  style="width: 750px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadRevisedForm78PsAdjObCrtn" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>')" />
								    </display:column>	
    								 
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="dateofBirth" title="DateOfBith"/>	
									
									<display:column property="region"  title="Region" />	
									<display:column property="station"  title="Station" />									
									<display:column property="chkPfidIn78PS" title="1st level Status"/>		
								 	<display:column title="1st level Report" media="html">
									 <a href="#" onclick="statemntwagesreport('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','Normal')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>
								    <display:column property="approvedStatus" title="2nd level Status"/>
								    <display:column title="2nd level Report" media="html">
									 <a href="#" onclick="statemntwagesreport2('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','Normal')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>	
									</display:table>
								</td>
							</tr>
							<% }
                              else if(user.equals("RAJESH") ){%>	
				   	  <tr>							         
								 
								<td align="center" width="95%">
														
									<display:table  style="width: 750px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadRevisedForm78PsAdjObCrtn" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>')" />
								    </display:column>	
    								 
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="dateofBirth" title="DateOfBith"/>	
									
									<display:column property="region"  title="Region" />	
									<display:column property="station"  title="Station" />									
									<display:column property="chkPfidIn78PS" title="1st level Status"/>		
								 	<display:column title="1st level Report" media="html">
									 <a href="#" onclick="statemntwagesreport('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','Normal')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>
								    <display:column property="approvedStatus" title="2nd level Status"/>
								    <display:column title="2nd level Report" media="html">
									 <a href="#" onclick="statemntwagesreport2('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getApprovedStatus()%>','Normal')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>	
									</display:table>
								</td>
							</tr>
							<% }
							else{%>
							 <tr>							         
								 
								<td align="center" width="95%">
														
									<display:table  style="width: 750px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./reportservlet?method=loadRevisedForm78PsAdjObCrtn" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>')" />
								    </display:column>	
    								 
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="dateofBirth" title="DateOfBith"/>	
									
									<display:column property="region"  title="Region" />	
									<display:column property="station"  title="Station" />									
									<display:column property="chkPfidIn78PS" title="Status"/>		
								 	<display:column title="Report" media="html">
									 <a href="#" onclick="statemntwagesreport('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','Normal')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>
								    
								    <display:column title="New Report" media="html">
									 <a href="#" onclick="statemntwagesreport('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getChkPfidIn78PS()%>','NEWREPORT')"><img src="./PensionView/images/viewDetails.gif" border="0"  /></a>
								    </display:column>
								    	
									</display:table>
								</td>
							</tr>
							<% }%>
						</table>  
					<%}%>     
			</form>
	</body>
</html>
