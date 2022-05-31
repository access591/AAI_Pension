<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.AaiEpfForm11Bean" %>
<%@ page import="java.util.ArrayList" %>
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

    String region="",empsrlNo="";
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
	<script type="text/javascript">
	function resetVals(){
	document.forms[0].empName.value="";
	document.forms[0].empsrlNo.value="";
	document.forms[0].dob.value="";
	document.forms[0].doj.value="";
	     
	}
	
	function validateForm(empserialNO,employeeName,reportYear) {
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
		var frm_toyear=<%=year%>;
		var frm_month=<%=month%>;
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frm_year="+frm_year+"&frm_toyear="+frm_toyear+"&frm_month="+frm_month+"&reportYear="+reportYear+"&empName="+employeeName;
		var url="<%=basePath%>reportservlet?method=getReportPenContrForAdjCrtn"+params;
		// alert(url);
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
    	var formType='',url='',pensionno='',empName='';
    	pensionno=    document.forms[0].empsrlNo.value;    	 
    	 
    	 if(document.forms[0].empsrlNo.value==''){
    	 alert('Please Enter Employee PF Id ');
    	 document.forms[0].empsrlNo.focus();
    	 return false;
    	 }
    	url="<%=basePath%>search1?method=searchRecordsbyEmpSerailNo&frmName=adjcorrections";
    	 
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
     	function editEmpSerailNumber(cpfacno,employeeName,region,airportCode,empSerailNumber,dateofBirth,empCode){
       
    	if(document.forms[0].cpfno.length==undefined){
		if(document.forms[0].cpfno.checked){
		var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth+"&empCode="+empCode;
		alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
    	}
		
		}
		
		for (i = 0; i < document.forms[0].cpfno.length; i++){
		if(document.forms[0].cpfno[i].checked){
				var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
	}
	}
	}
   	
   	 function selectMultipule(){
   	 document.getElementById("check1").checked
     var x=document.getElementsByName("cpfno");
      for(var i=0;i<x.length;i++){
     if(document.getElementById("check1").checked==true)
     document.getElementsByName("cpfno")[i].checked=true;
     else  
     document.getElementsByName("cpfno")[i].checked=false;
     }
     
    //  alert("checkBoxes " +checkboxes);
	//	document.forms[0].action="<%=basePath%>search1?method=delete";
	//	document.forms[0].method="post";
	//	document.forms[0].submit();
	}
	
 
   function getlogs(empserialno,adjobyear){
   alert('Under Processing........');
  /* var url="";
  		url ="<%=basePath%>reportservlet?method=getadjemolumentslog&empserialno="+empserialno+"&adjobyear="+adjobyear;
		document.forms[0].action=url;		
		document.forms[0].method="post";
		document.forms[0].submit();*/
   }
   
   function callReports(empName){
   var url='',formType='',empsrlNo='';
   formType = document.forms[0].formType.value;
    empsrlNo = document.forms[0].empsrlNo.value;
   
   if(formType=="form7ps"){
    url="<%=basePath%>reportservlet?method=loadform7Input&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName;
      }
   if(formType=="form8ps"){
    url="<%=basePath%>reportservlet?method=loadform8params&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName;
   
   }
    
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   
   }
   
</script>
	</head>

	<body class="BodyBackground" >
		<form name="test" action="" >
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
					
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
								
					</td>
				</tr>
			<tr>
			  <td>&nbsp;</td>
			  </tr>
				

				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
					Monthly Cpf Corrections 				
					</td>
					
					<%boolean flag = false;%>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td height="15%">
						<table align="center">
							<tr>
								<td class="label">
									Form3-2007-Sep Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)"/>
								</td>
								<td class="label">
									Form3-2007-Sep- PFID:
								</td>
								<td>
									<input type="text" name="empsrlNo" value="<%=empsrlNo%>"/>
								</td>
							</tr>
							<tr>
							<td class="label">
									Date of Birth:
								</td>
								<td>
								<input type="text" name="dob" onkeyup="return limitlength(this, 20)"/>
								</td>
							
								<td class="label">
									Date of Joining
								</td>
								<td>
									<input type="text" name="doj" onkeyup="return limitlength(this, 20)"/>
								</td>
							</tr>
							
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="left">
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();"/>
									<input type="button" class="btn" value="Reset" onclick="javascript:resetVals();" class="btn"/>
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1);" class="btn"/>
								</td>

							</tr>
						</table>
					</td>

				</tr>

			 
					 
			<% EmpMasterBean  dbBeans = new EmpMasterBean();
			  SearchInfo  getSearchInfo = new SearchInfo();
			 int   index=0;
			 String cpfaccnos="";
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				if(request.getAttribute("cpfAccnos")!=null){
				cpfaccnos=(String)request.getAttribute("cpfAccnos");
				}
				System.out.println("cpfaccnos  issssssssss"+cpfaccnos);
				index = searchBean.getStartIndex();
				//	out.println("index "+index);
				session.setAttribute("getSearchBean1", dbBeans);
				dataList = searchBean.getSearchList();
				System.out.println("dataLIst "+dataList.size());
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
				if (dataList.size() == 0) {		
					%>
                   <tr >

					<td>
						<table align="center" id="norec">
							<tr>
							<br>
							<td><b> No Records Found </b></td>
							</tr>
                         </table>
					 </td>
					</tr>

					<%}else if (dataList.size() != 0) {%>
				 
				<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", empCode = "", cpfacno = "",  pensionNumber="",claimsprocess="";
				String dateofBirth="",dateofJoining="",empSerailNumber="",totalTrans="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmpMasterBean beans = (EmpMasterBean) dataList.get(i);
					cpfacno=beans.getCpfAcNo();
					employeeName = beans.getEmpName();
				    dateofBirth=beans.getDateofBirth();
                    empSerailNumber=beans.getEmpSerialNo();
                    dateofJoining=beans.getDateofJoining();
                    region=beans.getRegion();               
                    airportCode=beans.getStation();
                    pensionNumber=beans.getPensionNumber();
                    empCode=beans.getEmpNumber();
                    totalTrans=beans.getTotalTrans();
                    claimsprocess=beans.getClaimsprocess().trim();
                       
					}%>
					
			<tr>
			 	<td >
			 		 
			 			<table  align=right  width="40%"  cellpadding=0 >
			 				<!--<tr>
							<td class="label" >	Form 7/8 PS Report:
							 
							 <select name="formType" onchange="callReports('<%=employeeName%>');">
							 <option value="NO-SELECT">Select One</option>
							 <option value="form7ps">FORM 7 PS</option>
							 <option value="form8ps">FORM 8 PS</option>		              					 
							 </select>
								</td>
							  
							</tr> -->
						</table>
					</td>
				</tr> 
				<tr>
					<td height="25%">
						<table align="center"  width="70%"  cellpadding=2 class="tbborder" cellspacing="0"  border="0">
							
							<tr class="tbheader">

								<td class="tblabel">Years</td>	
								 
								<td class="tblabel">Emp Sub Total Diff&nbsp;&nbsp;</td>								
								<td class="tblabel">AAI Contri Total Diff&nbsp;&nbsp;</td>
								<td class="tblabel">Pension Contri Diff</td>
								<td class="tblabel">Logs</td>
								<!-- <td class="tblabel"></td>-->
							<!-- <td class="tblabel">PensionNumber </td>-->
																				
								<td>
							 
						</tr>
						
							
					<%
					ArrayList empPCAdjDiffTot = new ArrayList();
					if (request.getAttribute("empPCAdjDiffTot") != null) {
					empPCAdjDiffTot = (ArrayList)request.getAttribute("empPCAdjDiffTot");
						for (int j = 0; j < empPCAdjDiffTot.size(); j++) {
					EmployeePensionCardInfo diffBean  = (EmployeePensionCardInfo) empPCAdjDiffTot.get(j);
					 
					%>
							<tr> 
								<td  class="Data" width="15%"><a href="#" onClick="validateForm('<%=empSerailNumber%>','<%=employeeName%>','<%=diffBean.getReportYear()%>')"><img src="./PensionView/images/report2.jpg" border="0"  width="20px" height="20px"  alt="PFReport" /></a>
									&nbsp;&nbsp;<%=diffBean.getReportYear()%>
								</td>
								  
								<td  class="Data" width="20%">
									<%=diffBean.getEmpSubTotDiff()%>
								</td>
								<td  class="Data" width="20%">
								 <%=diffBean.getAaiContriDiff()%>   
								</td>
								 <td  class="Data" width="20%">
									<%=diffBean.getPensionContriTotDiff()%>
								</td>
								
								 <td  class="Data" width="15%"><a href="#" onClick="getlogs('<%=empSerailNumber%>','<%=diffBean.getReportYear()%>')"><img src="./PensionView/images/viewDetails.gif" border="0" alt="PFReport" /></a>
									&nbsp;&nbsp; 
								</td>
								   	 
                               		
								
							</tr>
							
							
							 
                           <tr>&nbsp;</tr>
							<tr>
							<td align="center"></td><td></td><td> <td></td><td> </td>
							</tr>
							
							
							<% }}	
							 }%>
	<%}%>
						</table>
					</td>

				</tr>
				
				 

			</table>
     <tr><td colspan="3">&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
							</tr>


		</form>
	</body>
</html>
