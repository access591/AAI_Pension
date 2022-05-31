<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
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

    String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
	session.removeAttribute("getSearchBean1");
	
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
	
	
	function validateForm(empserialNO,claimsprocess) {
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
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frm_year="+frm_year+"&frm_toyear="+frm_toyear+"&frm_month="+frm_month;
		var url="<%=basePath%>reportservlet?method=getReportPenContr"+params;
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
       	document.forms[0].action="<%=basePath%>search1?method=searchRecordsbyEmpSerailNo"
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
		//document.forms[0].submit();
		alert("user doesn't have Permissions to edit the Record");
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
		//document.forms[0].submit();
		alert("user doesn't have Permissions to edit the Record");
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
					Monthly Cpf Recoveries Data[Search]				
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
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									Form3-2007-Sep- PFID:
								</td>
								<td>
									<input type="text" name="empsrlNo">
								</td>
							</tr>
							<tr>
							<td class="label">
									Date of Birth:
								</td>
								<td>
								<input type="text" name="dob" onkeyup="return limitlength(this, 20)">
								</td>
							
								<td class="label">
									Date of Joining
								</td>
								<td>
									<input type="text" name="doj" onkeyup="return limitlength(this, 20)">
								</td>
							</tr>
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="left">
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>

							</tr>
						</table>
					</td>

				</tr>

				<tr>
					<td height="25%">
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
				<tr>

					<td>
						<table align="center"  >
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="right">
								<!--  <input type="button" class="btn" alt="first" value="|<" name="First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" class="btn" alt="pre" value="<" name="Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" class="btn" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" class="btn" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								     <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
								   <img src="./PensionView/images/printIcon.gif" alt="ComparativeWestDataReport"  onClick="callReport1()"> -->
									
								</td>
							</tr>
						</table>
					
				</tr>
				<tr>
					<td height="25%">
						<table align="center"  width="70%"  cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">

								<td class="tblabel">Old CpfAccno:</td>
								<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
								<td class="tblabel">Form3-2007-Sep- PFID&nbsp;&nbsp;</td>
								<td class="tblabel">D.O.B </td>
								<td class="tblabel">D.O.J </td>
								<td class="tblabel">Record Count </td>
								<td class="tblabel"></td>
							<!-- <td class="tblabel">PensionNumber </td>-->
																				
								<td>
							<%if(session.getAttribute("usertype").equals("Admin")){%> <img src="./PensionView/images/page_edit.png" alt="edit" /> <%} %>
						</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", empCode = "", cpfacno = "",  pensionNumber="",claimsprocess="";
				String dateofBirth="",pensionOption="", remarks="",lastActive="",dateofJoining="",empSerailNumber="",totalTrans="";
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
					if (count % 2 == 0) {

					%>
							<tr >
								<%} else {%>
							<tr >
								<%}%>
                              
								<td  class="Data" width="15%"><a href="#" onClick="validateForm('<%=empSerailNumber%>','<%=claimsprocess%>')"><img src="./PensionView/images/viewDetails.gif" border="0" alt="PFReport" ></a>
									&nbsp;&nbsp;<%=cpfacno%>
								</td>								
								<td  class="Data" width="20%">
									<%=employeeName%>
								</td>
								<td  class="Data" width="20%">
									<%=empSerailNumber%>
								</td>
								
								<td  class="Data" width="20%">
									<%=dateofBirth%>
								</td>
								
								<td  class="Data" width="20%">
									<%=dateofJoining%>
								</td>
								<td  class="Data" width="20%">
								<!--<%=totalTrans%>  -->	
								</td>
								<%if(session.getAttribute("usertype").equals("Admin")){%>
								<td>
							 	<input type="checkbox" name="cpfno"  value="<%=cpfacno%>"  onclick="javascript:editEmpSerailNumber('<%=cpfacno%>','<%=employeeName%>','<%=region%>','<%=airportCode%>','<%=empSerailNumber%>','<%=dateofBirth%>','<%=empCode%>')" />
								
								</td>	
                                <%} %>				
								
							</tr>
							
							
							<%}%>
                           <tr>&nbsp;</tr>
							<tr>
							<td align="center"></td><td></td><td> <td></td><td> </td>
							</tr>
							<%if (dataList.size() != 0) {%>
							<tr>
							
								<td class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
							
							<% }	
							 }%>

						</table>
					</td>

				</tr>
				
				 

			</table>
     <tr><td colspan="3">&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
							</tr>


		</form>
	</body>
</html>
