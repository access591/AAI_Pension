<%@ page language="java"%>

<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>

<%
	String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
	String queryString = request.getQueryString();
            
    String userId = (String) session.getAttribute("userid");            
    String wetherOptionReverse = "",searchAirportCode = "",searchPensionNo="",searchRegion="",searchEmployeeNum="",
            			searchEmpName="",searchDob="",searchDoj="",searchAppDate="",searchCpfAccno="",searchPensionOption="",searchUanno="",searchUserName="",searchFHName="",searchDesig="",searchFreshOption="";   
    String pensionBack="",regionBack="",airportCodeBack="";   
    String pensionopt="",reversepensionopt="";   			
    searchPensionNo=request.getParameter("pensionNo").trim();
    searchCpfAccno=request.getParameter("cpfAccno").trim();
    searchUanno=request.getParameter("uanNo").trim();
    searchEmployeeNum=request.getParameter("empNumber").trim();
    searchEmpName=request.getParameter("empName").trim();
    searchDob=request.getParameter("dob").trim();
    searchDoj=request.getParameter("doj").trim();
    searchAppDate=request.getParameter("applnDate").trim();
    searchPensionOption=request.getParameter("penOption").trim();
    searchFreshOption=request.getParameter("freshPensionOption").trim();
    searchFHName=request.getParameter("fhName").trim();
    searchAirportCode=request.getParameter("airportcode").trim();
    searchRegion=request.getParameter("region").trim();
    searchUserName=request.getParameter("username").trim();
    searchDesig=request.getParameter("designation").trim();
    pensionBack=request.getParameter("pensionNoback");
    regionBack=request.getParameter("regionBack");
    airportCodeBack=request.getParameter("airportCodeback");
    
    if(!searchFreshOption.equals("---")) {
    	pensionopt=searchFreshOption;
	   if(searchFreshOption.trim().equals("B")){
			reversepensionopt="A";
		}	
		else{
			reversepensionopt="B";
		}
	}
	else {
		pensionopt=searchPensionOption.trim();
 		if(searchPensionOption.trim().equals("B")){
			reversepensionopt="A";
		}	
		else{
			reversepensionopt="B";
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page" />
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
		<link rel="StyleSheet" href="<%=basePath%>PensionView/css/styles.css" type="text/css" media="screen"/>
		<script type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></script>
		<script type="text/javascript">	
		function convertDate(inputFormat) {							
	  			function pad(s) { return (s < 10) ? '0' + s : s; }
				var d = new Date(inputFormat);
				return [pad(d.getDate()), pad(d.getMonth()+1), d.getFullYear()].join('/');
			}
			function resetValue() {
				var geturl="<%=basePath%>PensionView/personal/EmployeeFreshOptionEdit.jsp";
				var querystring='<%=queryString%>';
				var url=geturl+"?"+querystring;
				//alert(url);
				document.forms[0].action=url;
				document.forms[0].method="post";
				document.forms[0].submit();
			}
			function testSS() {	
				var curent_date = new Date();				
				curent_date.setDate(curent_date.getDate()-1);
				var mydate = convertDate(curent_date);	
				var todayDate=getCurrentDate();				
				var pensionno=document.forms[0].pensionNo.value;
				//var cpfAcno=document.forms[0].cpfAccNo.value;
				var employeeNumber=document.forms[0].empCode.value;
				var employeeName=document.forms[0].empName.value;
				var uanNo=document.forms[0].uanno.value;
				var dob=document.forms[0].dob.value;
				var doj=document.forms[0].doj.value;
				var applnDate=document.forms[0].appDate.value;
				var fhname=document.forms[0].fhName.value;
				var username=document.forms[0].userName.value;
				var region=document.forms[0].region.value;
				var airportcode=document.forms[0].airportCode.value;
				var newPenOption=document.getElementById('pensionOption').value;	
				var freshOpt='<%=searchFreshOption%>';		
				var whetherOption='<%=searchPensionOption%>'; 
				var searchAirportCode='<%=airportCodeBack%>';
				var searchRegion='<%=regionBack%>';
				var searchPensioNum='<%=pensionBack%>';		
				var userName='<%=searchUserName%>';	
				var designation='<%=searchDesig%>';
				if(employeeName=="") {
					alert("Please Enter Employee Name");
					document.forms[0].empName.focus();
					return false;
				}
				if(!ValidateName(employeeName)) {
					alert("Numeric Values are not allowed");
					document.forms[0].empName.select();
					return false;
				}	
				if(dob=="") {
					alert("Please Enter Date of Birth");
					document.forms[0].dob.focus();
					return false;
				}
				if(!dob=="") {
					var val1=convert_date(document.forms[0].dob);
	   		        if(val1==false){
	   		        	return false;
	   		      	}				
				}	
				var dateOfBirth = document.forms[0].dob.value; 
				var resDOB=compareDates(dateOfBirth,mydate);
				var testCondDOB=compareDates(dateOfBirth,"01/Sep/1956");	
				if(resDOB=='larger') {				
					alert("Invalid Date of Birth..");
					dob.select();	
					return false;
				}
				if(testCondDOB=='smaller') {
					alert("Invalid DOB..Date Of Birth should be greater than 01/Sep/1956");	
					dob.select();	
					return false;
				}	
				
				if(doj=="") {
					alert("Please Enter Date of Joining");
					document.forms[0].doj.focus();
					return false;
				}
				if(!doj=="") {
					var val1=convert_date(document.forms[0].doj);
	   		        if(val1==false){
	   		        	return false;
	   		      	}				
				}
				var dateOfJoining = document.forms[0].doj.value; 
				var resDOJ=compareDates(dateOfJoining,mydate);
				var testCondDOJ=compareDates(dateOfJoining,dateOfBirth); 					
				if(resDOJ=='larger') {				
					alert("Invalid Date of Joining..");
					doj.select();
					return false;
				}
				if(testCondDOJ=='smaller') {
					alert("Invalid DOJ..Date Of Joining cannot be less than Date of Birth");
					doj.select();
					return false;
				}	
				if( (applnDate=="") || (applnDate=="---") ) {
					alert("Please Enter Application Date");
					document.forms[0].appDate.select();
					return false;
				}
				if( (!applnDate=="") || (!applnDate=="---") ) {			
					var val1=convert_date(document.forms[0].appDate);
	   		        if(val1==false){
	   		        	return false;
	   		      	}	
				}						
				var resAppDate=compareDates(applnDate,todayDate);
				if(resAppDate=='larger') {
					alert("Invalid Application Date..");
					document.forms[0].appDate.select();
					return false;
				}							
				if((newPenOption=="") && (freshOpt=="---")) {
					alert("Cannot update pension Option..Please select fresh option");
					return false;
				}						
				if(fhname=="") {
					alert("Please Enter Father/Husband Name");
					document.forms[0].fhName.focus();
					return false;
				}
				if(!ValidateName(fhname)) {
					alert("Numeric Values are not allowed");
					document.forms[0].fhName.select();
					return false;
				}
				if(userName=="") {
					alert("Pl Enter User's Name");
					document.forms[0].userName.focus();
					return false;
				}	
				
				var r = confirm("Are you sure to update these details");
				if(r==true){
				var url = "<%=basePath%>psearch?method=editTransactionPenOptionData&searchAirport="+searchAirportCode+"&searchRegion="+searchRegion+"&searchPensionno="+searchPensioNum+"&oldPensionOption="+whetherOption+"&designation="+designation;
				//alert(url);
				//return false;			
				document.forms[0].action=url;
			 	document.forms[0].method="post";
				document.forms[0].submit();
				}else{
			return 	false;
				}
				
						
				return false;
			}
		</script>
		</head>
		<body class="BodyBackground" >
			<form>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
				</table>

				<table align="center" width="80%" align="center" border="0" cellpadding="0"
						cellspacing="0" class="tbborder">
					<tr>
						<td height="5%" align="center" class="ScreenMasterHeading">Fresh Option [Edit]</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<table align="center" border="0" width="100%" align="center"
									cellpadding="1" cellspacing="0">
								<tr>
									<td class="label" align="right">
											PF ID :&nbsp;
									</td>
									<td>
										<input type="text" name="pensionNo" value="<%=searchPensionNo%>" readonly="readonly" tabindex="6"/>
									</td>
									<td class="label" align="right">
											 UAN No:&nbsp;
									</td>
									<td>
										<input type="text" name="uanno" value="<%=searchUanno%>" readonly="readonly" />
										<input type="hidden" name="cpfAcno" value="<%=searchCpfAccno%>" />
									</td>								
								</tr>							
								<tr>			
						           <td class="label" align="right">
											Employee Code :&nbsp;
									</td>
									<td>
										<input type="text" name="empCode" value="<%=searchEmployeeNum%>" readonly="readonly" />
									</td>
									<td class="label" align="right">
											Employee Name :&nbsp;
									</td>
									<td>
										<input type="text" name="empName" value="<%=searchEmpName%>" />
									</td>
								</tr>
								<tr>
									<td class="label" align="right">
											Date of Birth :&nbsp;
									</td>
									<td>
										<input type="text" name="dob" value="<%=searchDob%>" />
										<a href="javascript:show_calendar('forms[0].dob');" ><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
                                    </td>
									<td class="label" align="right">
										Date of Joining :&nbsp;									
									</td>
									<td>									
									    <input type="text" name="doj" value="<%=searchDoj%>" />
									    <a href="javascript:show_calendar('forms[0].doj');" ><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>	                                
	        		                 </td>
								</tr>
								<tr>
									<td class="label" align="right">
										Application Date :&nbsp;
									</td>
									<td>
										<input type="text" name="appDate" value="<%=searchAppDate%>" />
										<a href="javascript:show_calendar('forms[0].appDate');" ><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
									</td>
									<td class="label" align="right" >
										Pension Option :&nbsp;
									</td>
									<td >									
									    <select name="pensionOption" id="pensionOption" style="width: 80px">
									    <option value="">[Select One]</option>
										<option value="<%=pensionopt%>"> <%=pensionopt%> </option>
										<option value="<%=reversepensionopt%>"> <%=reversepensionopt%> </option>
										</select>
	                    		     </td>
								</tr>							               
    	                        <tr>
        	                     	<td class="label" align="right">
											Father's Name :&nbsp;
									</td>
									<td>
										<input type="text" name="fhName" value="<%=searchFHName%>" />
                                    </td>
                                    <td class="label" align="right">
											User Name :&nbsp;
									</td>
									<td>
										<input type="text" name="userName" value="<%=searchUserName%>" />
									</td>
								</tr>
								<tr>
									 <td class="label" align="right">
											Region :&nbsp;
									</td>
									<td>
										<input type="text"  name="region" value="<%=searchRegion%>" readonly />
									</td>
									<td class="label" align="right">
											Station :&nbsp;
									</td>
									<td>
										<input type="text" name="airportCode" value="<%=searchAirportCode%>" readonly/>
									</td>															
								</tr>
								<tr>					
								</tr>									
						</table>	
					<tr>
					</tr>
					<tr>
					<td>
						<table align="center" border="0" width="100%" align="center" cellpadding="1" cellspacing="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>								
								<td align="center">
									<input type="button" class="btn" value="Update" class="btn" onclick="testSS();"/>
									<input type="button" class="btn" value="Reset" onclick="resetValue();" class="btn"/>
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
								</td>
							</tr>													
				        </table>
					</td>
				</tr>    
			</table>											
		</form>
	</body>
</html>
