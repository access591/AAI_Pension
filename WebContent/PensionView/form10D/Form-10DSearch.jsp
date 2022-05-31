
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>AAI</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
   	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">	
    <SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
	<script type="text/javascript"> 
    var monthtext=['--','Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sept','Oct','Nov','Dec'];
     var swidth=screen.Width-10;
	var sheight=screen.Height-150;
    
    function getNodeValue(obj,tag){
		return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   	}
   	function createXMLHttpRequest(){
		if(window.ActiveXObject){
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 	}else if (window.XMLHttpRequest){
			xmlHttp = new XMLHttpRequest();
		 }
	}
	function getAirports(){	
		var regionID;
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		createXMLHttpRequest();	
		var url ="<%=basePath%>reportservlet?method=getAirports&region="+regionID;
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
	function form10DReport(pensionNo,obj,employeeName,employeeCode,region,airportCode,dateOfBirth,index,totalData){
			
			var flag="true";
			
			var url="<%=basePath%>psearch?method=frmForm10DReport&cpfacno="+obj+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode+"&startIndex="+index+"&totalData="+totalData+"&frm_PensionNo="+pensionNo+"&frm_dateOfBirth="+dateOfBirth;
		
			wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		}
		
	function frmload(){
 		process.style.display="none";
 		//populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
	}
	 function Search(){
		
		process.style.display="none";
   		 var empNameCheak="",airportID="",sortColumn="EMPLOYEENAME",day="",month="",year="";
	
		
   		var dob=document.forms[0].dob.value;
		var doj=document.forms[0].doj.value;
		 if(!document.forms[0].dob.value==""){
	   		    var date1=document.forms[0].dob;
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].doj.value==""){
	   		    var date1=document.forms[0].doj;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 var employeeNo=document.forms[0].employeeCode.value;
		 var cpfaccno=document.forms[0].cpfaccno.value;
		// alert("emp no "+employeeNo +"cpfaccno "+cpfaccno +"dob "+dob+"doj"+doj);
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
		var formName="form10D";
		// alert("in search" +"<%=basePath%>psearch?method=searchPersonal&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&cpfaccno="+cpfaccno+"&employeeNo="+employeeNo);
		document.forms[0].action="<%=basePath%>psearch?method=form10DsearchPersonal&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&cpfaccno="+cpfaccno+"&employeeNo="+employeeNo+"&page="+formName;
	  	document.forms[0].method="post";
		document.forms[0].submit();
	}

	
		function callReport(){
   		var reportID="",sortColumn="EMPLOYEENAME";
   	 	reportID=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
   	 	var url="<%=basePath%>psearch?method=personalEmpReport&reportType="+reportID+"&frm_sortcolumn="+sortColumn;
   	 	wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
   	 }

     function editPensionMaster(pfid,obj,employeeName,employeeCode,region,airportCode,index,totalData){

					var flag="true";
					document.forms[0].action="<%=basePath%>psearch?method=form10DSubmitForm&cpfacno="+obj+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode+"&startIndex="+index+"&totalData="+totalData+"&pfid="+pfid;
					document.forms[0].method="post";
					document.forms[0].submit();
			
		}

     
		function resetMaster(){
     				document.forms[0].action="<%=basePath%>psearch?method=loadForm10D";
					document.forms[0].method="post";
					document.forms[0].submit();
			
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
		window.onload=function(){
			populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
		}
		
		function redirectPageNav(navButton,index,totalValue){      
		var sortColumn="EMPLOYEENAME";
		var formName="form10D";
		document.forms[0].action="<%=basePath%>psearch?method=form10Dpersonalnav&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&frm_sortingColumn="+sortColumn+"&page="+formName;;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
    </script>
  </head>
  <%
  	HashMap hashmap=new HashMap();
  	String region="";
  	Iterator regionIterator=null;
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	}
  %>
  <body onload="javascript:frmload()">
   <form name="personalMaster" action="" method="post">
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="5%" colspan="2" align="center" class="ScreenHeading">Form-10D[Search]</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="15%">
				<table align="center">
					<tr>
						<td class="label">Region:</td>
						<td>
							<SELECT NAME="select_region" onChange="javascript:getAirports()"  style="width:130px">
							<option value="NO-SELECT">[Select One]</option>
								<%
						     	  while(regionIterator.hasNext()){
							 	  region=hashmap.get(regionIterator.next()).toString();
							 	%>
							  	<option value="<%=region%>" ><%=region%></option>
	                       		<%}%>
							</SELECT>
						</td>
						<td class="label">Airport Code:</td>
						<td>
							<select name="select_airport" >
								<option value="NO-SELECT">[Select One]</option>
						    </select>
						</td>
					</tr>
					<tr>
						<td class="label">Employee Name:</td>
						<td><input type="text" name="empName" onkeyup="return limitlength(this, 20)"></td>
						<td class="label">PF ID:</td>
						<td><input type="text" name="pensionNO" onkeyup="return limitlength(this, 20)"></td>
						
					</tr>
                    <tr>	<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="employeeCode" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									Old CpfAccno:
								</td>
								<td>
									<input type="text" name="cpfaccno">
								</td>
							</tr>
					<tr>
					 <td class="label">Date Of Birth:</td>
						<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->
                    
					<td><input type="text" name="dob" tabindex="9"  onkeyup="return limitlength(this, 20)">
										<a href="javascript:show_calendar('forms[0].dob');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
										</td>

                   <td class="label">
									Date of Joining:</td>
				<td>
									
								    <input type="text" name="doj" tabindex="9"  onkeyup="return limitlength(this, 20)">
										<a href="javascript:show_calendar('forms[0].doj');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
				</td>

</tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td>
								<input type="button" class="btn" value="Search" class="btn" onclick="Search();">
								<input type="button" class="btn" value="Reset" onclick="javascript:resetMaster()" class="btn">
								<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
						</td>

					</tr>
			</table>
		</td>
	</tr>


			
		
				
				<%	EmployeePersonalInfo dbBeans = new EmployeePersonalInfo();
					SearchInfo getSearchInfo = new SearchInfo();
					int totalData = 0,index = 0;
					if (request.getAttribute("searchBean") != null) {
					SearchInfo searchBean = new SearchInfo();
					ArrayList dataList = new ArrayList();
					BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
					searchBean = (SearchInfo) request.getAttribute("searchBean");
					dbBeans = (EmployeePersonalInfo) request.getAttribute("searchInfo");
					index = searchBean.getStartIndex();
				//	out.println("index "+index);
					session.setAttribute("getSearchBean1", dbBeans);
					dataList = searchBean.getSearchList();
					totalData = searchBean.getTotalRecords();
					bottomGrid = searchBean.getBottomGrid();
					if (dataList.size()!= 0) {

				%>
					
				<tr>
					<td><table align="center">
						<tr>
						<td colspan="3"></td>
							<td colspan="2" align="right">
								<input type="button"  alt="first" value="|<" name=" First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
								<input type="button"  alt="pre" value="<" name=" Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
								<input type="button" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
								<input type="button"  value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
							
							</td>
						</tr>
						</table>
					  </td>
				</tr>
				<tr>
					<td height="25%">
						<table align="center" width="100%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
							<tr class="tbheader">
								
								<td class="tblabel">PF ID&nbsp;&nbsp;</td>
								<td class="tblabel">Old <br/>CPFACC.No</td>
								<td class="tblabel">Employee Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
								<td class="tblabel">Designation</td>
								<td class="tblabel">D.O.B</td>
								<td class="tblabel">Pension Option&nbsp;</td>
								<td class="tblabel">Division&nbsp;</td>
								<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
								<td class="tblabel">Region</td>
								<td><img src="./PensionView/images/page_edit.png" alt="Form-10D Form"  border="0" />&nbsp;</td>
                               	<td><img src="./PensionView/images/nominee4.gif" alt="Form-10D Report"  border="0" /></td>
							
						
							</tr>
							<%	int count = 0;
								for (int i = 0; i < dataList.size(); i++) {
									count++;
									EmployeePersonalInfo personal = (EmployeePersonalInfo) dataList.get(i);
							%>
							<tr>
							
								<td class="HighlightData"><%=personal.getPfID()%></td>
								<td class="Data" width="12%"><%=personal.getCpfAccno()%></td>
								<td class="Data"><%=personal.getEmployeeNumber()%></td>
								<td class="Data" width="12%"><%=personal.getEmployeeName()%></td>
								<td class="Data"><%=personal.getDesignation()%></td>
								<td class="Data" width="10%"><%=personal.getDateOfBirth()%></td>
								<td class="Data" width="5%"><%=personal.getWetherOption()%></td>
								<td class="Data"><%=personal.getDivision()%></td>
								<td class="Data"><%=personal.getAirportCode()%></td>
								<td class="Data" width="12%"><%=personal.getRegion()%></td>
				
								<td>
									<a href='#' onclick="javascript:editPensionMaster('<%=personal.getOldPensionNo()%>','<%=personal.getCpfAccno()%>','<%=personal.getEmployeeName()%>','<%=personal.getEmployeeNumber()%>','<%=personal.getRegion()%>','<%=personal.getAirportCode()%>','<%=index%>','<%=totalData%>')"><img src='./PensionView/images/page_edit.png' border='0' alt='Edit' > </a>
								</td>
								<td>
									<a href='#' onClick="javascript:form10DReport('<%=personal.getPensionNo()%>','<%=personal.getCpfAccno()%>','<%=personal.getEmployeeName()%>','<%=personal.getEmployeeNumber()%>','<%=personal.getRegion()%>','<%=personal.getAirportCode()%>','<%=personal.getDateOfBirth()%>')"><img src='./PensionView/images/nominee4.gif' border='0' alt='Form-10D Report' > </a>
								</td>
								
									
									<input type="hidden" name="cpfnostring<%=personal.getCpfAccno()%>" value="<%=personal.getCpfAccno()%>,<%=personal.getEmployeeName()%>,<%=personal.getEmployeeNumber()%>,<%=personal.getRegion()%>,<%=personal.getAirportCode()%>">
						
							</tr>
						<%}%>
							<tr>

								<td class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
						<%}else if(dataList.size()==0){%>
						<tr>

					<td>
						<table align="center" id="norec">
							<tr>
								<br>
								<td>
									<b> No Records Found </b>
								</td>
							</tr>
						</table>
					</td>
				</tr>
						<%}}%>
						</table>
					</td>

				</tr>

			</table>
		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>
  </body>
</html>
