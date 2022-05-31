<%@ page language="java"%>
<%@ page import="aims.bean.*"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.bean.StationWiseRemittancebean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ taglib uri="/tags-display" prefix="display"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String airportCode2 = "", region2 = "", pensionno = "", employeeNo = "", cpfAcno = "", dob = "", doj = "", employeName = "", uanNo = "";
			String[] year = { "2012-13", "2013-14", "2014-15" };
			String[] userYears = { "2012-13", "2013-14", "2014-15" };
			String selYear = "";
			String userName = (String) session.getAttribute("userid");
			String accountType = "", region1 = "", monthyear = "", monthmm1 = "", selRegion = "";
			if (request.getAttribute("monthYear") != null) {
				monthyear = request.getAttribute("monthYear").toString();
				System.out.println("list"
						+ request.getAttribute("monthYear").toString());
			}
			if (request.getAttribute("month") != null) {
				monthmm1 = request.getAttribute("month").toString();
				System.out.println("month"
						+ request.getAttribute("month").toString());
			}
			if (request.getAttribute("region") != null) {
				selRegion = request.getAttribute("region").toString();
				System.out.println("selRegion"
						+ request.getAttribute("region").toString());
			}
			String userType = session.getAttribute("userid").toString();
			String AirportName = "";
			if (session.getAttribute("station") != null) {
				AirportName = session.getAttribute("station").toString();
			}

			if (request.getAttribute("year") != null) {
				selYear = request.getAttribute("year").toString();
				System.out.println("selYear"
						+ request.getAttribute("year").toString());
			}
			EmployeePersonalInfo employeeInfo = null;
			if (request.getAttribute("searchInfo") != null) {
				employeeInfo = (EmployeePersonalInfo) request
						.getAttribute("searchInfo");
				airportCode2 = employeeInfo.getAirportCode();
				region2 = employeeInfo.getRegion();
				pensionno = employeeInfo.getPensionNo();
				dob = employeeInfo.getDateOfBirth();
				doj = employeeInfo.getDateOfJoining();
				employeeNo = employeeInfo.getEmployeeNumber();
				employeName = employeeInfo.getEmployeeName();
				uanNo = employeeInfo.getUanno();
			}
			//request.setAttribute("searchAirportCode",airportCode2);
			//request.setAttribute("searchRegionCode",region2); 

			%>
<html>
	<HEAD>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">
			function LoadWindow(params){
    			var newParams =params;
				winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
				winOpened = true;
				winHandle.window.focus();
			}
			function resetMaster(){
     			document.forms[0].action="<%=basePath%>psearch?method=loadFreshPenOption";
				document.forms[0].method="post";
				document.forms[0].submit();	
			}
			function createXMLHttpRequest(){
				if(window.ActiveXObject){
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 			}else if (window.XMLHttpRequest){
					xmlHttp = new XMLHttpRequest();
				 }
			}
			
			function getAirports() {
				var region=document.forms[0].select_region.value;
				createXMLHttpRequest();	
			    var url ="<%=basePath%>validatefinance?method=getAirports&region="+region;
				xmlHttp.open("post", url, true);
				xmlHttp.onreadystatechange = getAirportsList;
				xmlHttp.send(null);
			  }
			function getAirportsList(){				
				if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
				 	process.style.display="block";
				}
				if(xmlHttp.readyState ==4){
					if(xmlHttp.status == 200){ 
						var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');						
						process.style.display="none";
						if(stype.length==0){
					 		var obj1 = document.getElementById("select_airport");
						  	obj1.options.length=0; 
			  				obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
						}else{
			 			   	var obj1 = document.getElementById("select_airport");
			 			  	obj1.options.length = 0;
					 		for(i=0;i<stype.length;i++){
		  						if(i==0){
									obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
								}
								obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
							}
				  		}
					}
				}
			}	 
			function getNodeValue(obj,tag){
				return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   			}
			function frmload(){	
 				process.style.display="none";
	 			populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
	 			
			}
			function populatedropdown(dayfield, monthfield, yearfield){
				var today=new Date();
				var dayfield=document.getElementById(dayfield);
				var monthfield=document.getElementById(monthfield);
				var yearfield=document.getElementById(yearfield);
				for (var i=0; i<=31; i++){
					if(i==0){
						dayfield.options[i]=new Option("--","--")
					}else{
						dayfield.options[i]=new Option(i, i+1)
					}		
				}
				for (var m=0; m<=12; m++){
					monthfield.options[m]=new Option(monthtext[m], monthtext[m]);
				}
				//select today's month
				var thisyear=today.getFullYear();
				var beginyear=1939;
				var size=thisyear-beginyear;
				for (var y=0; y<=size; y++){
					yearfield.options[y]=new Option(beginyear, beginyear);
					beginyear+=1
				}
				yearfield.options[0]=new Option("--","--", true, true); //select today's year
			}
			
 			function Search() {
 				process.style.display="none";
 				var airportID="",sortColumn="EMPLOYEENAME",day="",month="",year="";
 				var dob=document.forms[0].dob.value;
				var doj=document.forms[0].doj.value;
				if(!document.forms[0].dob.value==""){
	   		    	var date1=document.forms[0].dob;
		   		    var val1=convert_date(date1);
	   	   		    if(val1==false) {
	   		        	return false;
	   		     	}
	   		    }
			    if(!document.forms[0].doj.value==""){
	   		        var date1=document.forms[0].doj;
		   	        var val1=convert_date(date1);
		   		    if(val1==false) {
	   		   			return false;
	   		   		}
	   		    }
	   		    var employeeNo=document.forms[0].employeeCode.value;
				//var cpfaccno=document.forms[0].cpfaccno.value;	
				var uanNo=document.forms[0].uanNo.value;			
				var regionID;
				if(document.forms[0].select_region.selectedIndex>0){
					regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
				}else{
					regionID=document.forms[0].select_region.value;			
				}
				if(document.forms[0].select_airport.selectedIndex>0){
					airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
				}else{
					airportID=document.forms[0].select_airport.value;
				}
				//alert("emp no "+employeeNo +"cpfaccno "+cpfaccno +"dob "+dob+"doj"+doj+"region ID"+regionID +"airport code"+airportID);
				document.forms[0].action="<%=basePath%>psearch?method=searchPensionFreshOption&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&uanNo="+uanNo+"&employeeNo="+employeeNo;
				document.forms[0].method="post";
				document.forms[0].submit();
				
  			}  		
			function editData(pensionNo,cpfno,uanNo,employeeNumber,employeeName,fhname,dateOfBirth,dateOfJoining,whetherOption,freshOption,appDate,airportCode,region,userName,designation) {											
				var searchPensionNo='<%=pensionno%>';
				var searchRegion='<%=region2%>';
				var searchAirportCode='<%=airportCode2%>';
				var params = "pensionNo="+pensionNo+"&cpfAccno="+cpfno+"&uanNo="+uanNo+"&empNumber="+employeeNumber+"&empName="+employeeName+"&fhName="+fhname+"&dob="+dateOfBirth+"&doj="+dateOfJoining+"&penOption="+whetherOption+"&freshPensionOption="+freshOption+"&applnDate="+appDate+"&airportcode="+airportCode+"&region="+region+"&username="+userName+"&designation="+designation+"&pensionNoback="+searchPensionNo+"&regionBack="+searchRegion+"&airportCodeback="+searchAirportCode;
				//alert(params);
				document.forms[0].action="<%=basePath%>PensionView/personal/EmployeeFreshOptionEdit.jsp?"+params;
				document.forms[0].method="post";
				document.forms[0].submit();
			}	
	
			function callReport(){
				//var url='';
				var url='',pensionno='',employeNo ='',employeeName='',uanNo='',dob='',doj='',region='',airportCode='',sortingColumn = 'EMPLOYEENAME';	
				pensionno= '<%=pensionno%>';
				employeNo = '<%=employeeNo%>';
				employeeName = '<%= employeName%>';
				uanNo = '<%=uanNo%>';
				dob = '<%= dob%>';
				doj = '<%= doj %>';
				region ='<%= region2%>';
				airportCode='<%=airportCode2 %>';
				formtype=document.forms[0].formType.options[document.forms[0].formType.selectedIndex].value;
				if(formtype==''){
				return false;
				}
				var swidth=screen.Width-10;
				var sheight=screen.Height-150;
			     url="<%=basePath%>psearch?method=searchPensionFreshOptionReport&&region="+region+"&airPortCode="+airportCode+"&frm_sortingColumn="+sortingColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&uanNo="+uanNo+"&employeeNo="+employeNo+"&empName="+employeeName+"&pensionNO="+pensionno+"&formtype="+formtype;
				//alert(formtype+" "+url);
				//return false;
				if(formtype=='html' || formtype=='Html'){
			   		 LoadWindow(url);
			 	}else if(formtype=='Excel Sheet' || formtype=='ExcelSheet'){
					wind1 = window.open(url,"Report","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
					winOpened = true;
					wind1.window.focus();
			  	}
			}
		
</script>
	</HEAD>

	<body onload="javascript:frmload()">

		<form>
			<%HashMap hashmap = new HashMap();
			String region = "";
			Iterator regionIterator = null;
			if (request.getAttribute("regionHashmap") != null) {
				hashmap = (HashMap) request.getAttribute("regionHashmap");
				Set keys = hashmap.keySet();
				regionIterator = keys.iterator();
			}

			%>
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


			<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="2%" colspan="2" align="center" class="ScreenMasterHeading">
						PFID Search for Fresh Pension Option
					</td>
				</tr>
				<tr>
					<td>
						<table width="70%" height="25%" align="center" border="0">
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>


							<tr>
								<td class="label">
									Region:
								</td>
								<td>
									<SELECT NAME="select_region" onChange="javascript:getAirports()" style="width:130px">
										<option value="NO-SELECT">
											[Select One]
										</option>
										<%while (regionIterator.hasNext()) {
				region = hashmap.get(regionIterator.next()).toString();

				%>

										<option value="<%=region%>">
											<%=region%>
										</option>
										<%}%>
									</SELECT>
								</td>
								<td class="label">
									Airport Code:
								</td>
								<td>
									<select name="select_airport" id="select_airport" style="width:130px">
										<option value="NO-SELECT">
											[Select One]
										</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="label">
									Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									PF ID:
								</td>
								<td>
									<input type="text" name="pensionNO" onkeyup="return limitlength(this, 20)">
								</td>

							</tr>
							<tr>
								<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="employeeCode" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									UAN No:
								</td>
								<td>
									<input type="text" name="uanNo">
								</td>
							</tr>
							<tr>
								<td class="label">
									Date Of Birth:
								</td>
								<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->

								<td>
									<input type="text" name="dob" tabindex="9" onkeyup="return limitlength(this, 20)">
									<a href="javascript:show_calendar('forms[0].dob');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a>
								</td>

								<td class="label">
									Date of Joining:
								</td>
								<td>

									<input type="text" name="doj" tabindex="9" onkeyup="return limitlength(this, 20)">
									<a href="javascript:show_calendar('forms[0].doj');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a>
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td align="right">
									<input type="button" class="btn" value="Search" class="btn" onclick="Search();">
									<input type="button" class="btn" value="Reset" onclick="javascript:resetMaster()" class="btn">
									</td><td><input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"></td>
							

							</tr>


						</table>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>

			<%ArrayList searchlist = new ArrayList();
			int i = -1;
			String message = "";
			if (request.getAttribute("searchList") != null) {
				searchlist = (ArrayList) request.getAttribute("searchList");
				if (searchlist.size() == 0) {
					if (!request.getAttribute("message").equals("")) {
						message = (String) request.getAttribute("message");

						%>
			<table align="center">
				<tr>

					<td>
						<b> <font color="red"> <%=message%> </font> </b>
					</td>
				</tr>
			</table>

			<%}
				} else {

					%>


			<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td>
						<table style="width: 500px;" align="center">
							<tr>
								<%--<td class="label" colspan="2" align="right">Region : </td><td class="label" colspan="3" align="left"> <%= region2 %> </td> 
			<td class="label" colspan="2" align="right">Airport Code : </td><td class="label" colspan="3" align="left"> <%= airportCode2 %></td> 
						--%>
								<td class="label" colspan="4" align="right">
									Report Type :
								</td>
								<td class="label" colspan="3" align="left">
									<select name="formType" id="formType" Style='width:80px' onchange="callReport();">
										<option value="">
											Select One
										</option>
										<option value="Html">
											Html
										</option>
										<option value="ExcelSheet">
											Excel Sheet
										</option>
									</select>
								</td>
							</tr>
						</table>
					</td>

				</tr>
				<tr>
					<td align="center">
						<display:table style="width: 850px;" sort="list" pagesize="15" name="requestScope.searchList" requestURI="./psearch?method=loadFreshPenOption">
							<tr>
								<td>

									<%i++;
					EmployeePersonalInfo personal = (EmployeePersonalInfo) searchlist
							.get(i);
							
					//	if(personal.getWetherOption().trim().equals("B")){
					//	wetherOptionReverse="A";
					//	}else{
					//	wetherOptionReverse="B";
					//	}

					%>				<%if(!personal.isFreshOPFalg()){%>
									<display:column title="">
										<input   type="radio" name="editbutton"
											onclick="editData('<%=personal.getPensionNo().trim()%>','<%=personal.getCpfAccno().trim()%>','<%=personal.getUanno().trim()%>','<%=personal.getEmployeeNumber().trim()%>','<%=personal.getEmployeeName().trim()%>','<%=personal.getFhName().trim()%>','<%=personal.getDateOfBirth().trim()%>','<%=personal.getDateOfJoining().trim()%>','<%=personal.getWetherOption().trim()%>' , '<%=personal.getFreshPensionOption().trim()%>' , '<%=personal.getAppDate().trim()%> ', '<%=personal.getAirportCode().trim()%>' , '<%=personal.getRegion().trim()%>', '<%=userName.trim()%>', '<%=personal.getDesignation().trim()%>');">
									</display:column>
									<%} else {%>
									<display:column title=""> <img src="<%=basePath%>PensionView/images/lockIcon.gif" border="no" align="middle" /></display:column>
									<%}%>
									<display:column property="pensionNo" title="PF ID" class="datanowrap" />
									<display:column property="uanno" title="UAN No" class="datanowrap" />
									<display:column property="employeeNumber" title="Employee Code" class="datanowrap" />
									<display:column property="employeeName" title="Employee Name" headerClass="sortable" class="datanowrap" />
									<display:column property="fhName" title="Father's Name" class="datanowrap" />
									<display:column property="dateOfBirth" title="Date Of Birth" class="datanowrap" />
									<display:column property="dateOfJoining" title="Date Of Joining" class="datanowrap" />
									<display:column property="wetherOption" title="Existing Option" class="datanowrap" />
									<display:column property="freshPensionOption" title="Revised Option" class="datanowrap" />
									<display:column property="appDate" title="Application Date" class="datanowrap" />
									<display:column property="airportCode" title="Station" class="datanowrap" />
									<display:column property="region" title="Region" class="datanowrap" />
									<display:column value="<%=userName%>" title="User" class="datanowrap" />
									<%--		
			<display:column title="Edit/Save">  <input type='button' name="edit<%=i%>" value=' E ' style="cursor: pointer;" onclick="return editData('<%=i %>','edit<%=i%>','image<%=i%>','pensionOption<%=i%>','changedDate<%=i%>','<%= personal.getPensionNo() %>','<%= personal.getCpfAccno() %>','<%= personal.getEmployeeNumber() %>','<%= personal.getEmployeeName() %>','<%= personal.getDesignation() %>','<%= personal.getDateOfBirth() %>','<%= personal.getDateOfJoining() %>','<%= personal.getWetherOption()%>' , '<%= personal.getRegion() %>','<%= personal.getAirportCode() %>');"  > </display:column>
			--%>

								</td>
							</tr>
						</display:table>
					</td>

				</tr>
			</table>
			<%}
			}

			%>


		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center">
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle" />
			<SPAN class="label">Processing.......</SPAN>
		</div>
	</body>
</html>
