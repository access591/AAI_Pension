<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

			String region = "";
			CommonUtil common = new CommonUtil();

			HashMap hashmap = new HashMap();
			hashmap = common.getRegion();

			Set keys = hashmap.keySet();
			Iterator it = keys.iterator();

			if (session.getAttribute("getSearchBean1") != null) {
				//session.removeAttribute("getSearchBean1");
				//session.setMaxInactiveInterval(1);
				//session.invalidate();
			}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<script type="text/javascript">
		
		
		
	function redirectPageNav(navButton,index,totalValue){      
		document.forms[0].action="<%=basePath%>search1?method=navigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function openURL(sURL) { 
//	alert(sURL);
		window.open(sURL,"Window1","menubar=no,width=430,height=360,toolbar=no");

	} 
	function editPensionMaster(obj,employeeName,employeeCode,region,airportCode,index,totalData,deleteFlag){
		if(document.forms[0].cpfno.length==undefined){
		if(document.forms[0].cpfno.checked){
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode+"&startIndex="+index+"&totalData="+totalData;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
    	}
		
		}
	if(deleteFlag=='N'){
	for (i = 0; i < document.forms[0].cpfno.length; i++){
			if(document.forms[0].cpfno[i].checked){
				var cpfacno=obj;
				var answer =confirm('Are you sure, do you want edit this record');
				if(answer){
					var flag="true";
					document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode+"&startIndex="+index+"&totalData="+totalData;
					document.forms[0].method="post";
					document.forms[0].submit();
				}
				else{
				document.forms[0].cpfno[i].checked=false;
				}
		  	}
		}}else {
		alert("User Doesn't Have Previliges To Enable The Deleted Record");
		return false;
		
		}
		}
	
	function deletePensionMaster(obj,employeeName,employeeCode,region,airportCode){
	 
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want  delete this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=delete&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	
	function deleteMultipule(){
		document.forms[0].action="<%=basePath%>search1?method=delete";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
	
	function createXMLHttpRequest(){
		if(window.ActiveXObject){
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 	}else if (window.XMLHttpRequest){
			xmlHttp = new XMLHttpRequest();
		 }
	}
	
	function getAirports()
{	 var index=document.forms[0].region.selectedIndex.value;
     var selectedIndex=document.forms[0].region.options.selectedIndex;
	 var region=document.forms[0].region[selectedIndex].text;
	createXMLHttpRequest();	
    var url ="<%=basePath%>search1?method=getAirports&region="+region;
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getAirportsList;
	xmlHttp.send(null);
}

function getAirportsList()
{
	if(xmlHttp.readyState ==4)
	{
	 if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  if(stype.length==0){
		 	var obj1 = document.getElementById("airPortCode");
		   	obj1.options.length=0; 
		  
		  }else{
		   	var obj1 = document.getElementById("airPortCode");
		   	obj1.options.length = 0;
		  	for(i=0;i<stype.length;i++){
		  if(i==0){	obj1.options[obj1.options.length]=new Option('[Select One]','','true');
						}
				obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
				
		  	}
		  
			  }
		}
	}
}
	
  function testSS(){
     var region1= document.forms[0].region[document.forms[0].region.options.selectedIndex].text;
         if(region1=="Select One")
   		 {
   		  alert("Please Select Region");
   		  document.forms[0].region.focus();
   		  return false;
   		  } 
   		  var empNameCheak="";
   		 
   		 if(window.document.test.empNameChecked.checked==true){
   		  empNameCheak=window.document.test.empNameChecked.checked;
   		 }
   		 else {
   		 empNameCheak=false;
   		 }
   	   	document.forms[0].action="<%=basePath%>search1?method=searchPensionMaster&region="+region1+"&empNameCheak="+empNameCheak;
	  	document.forms[0].method="post";
		document.forms[0].submit();
   		 }
   		 
   	 function callReport()
   	 {
   	  var empNameCheak="";
   		 
   		 if(window.document.test.empNameChecked.checked==true){
   		  empNameCheak=window.document.test.empNameChecked.checked;
   		 }
   		 else {
   		 empNameCheak=false;
   		 }
   		
   	 var reporttype="";
   	 	reporttype=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
   	 	if(reporttype=='html' || reporttype=='Html'){
	   	 	 window.open ("<%=basePath%>PensionView/PensionEmpInfoReport.jsp","mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   	 	}else if(reporttype=='Excel Sheet' || reporttype=='ExcelSheet' ){
   	 		 window.open ("<%=basePath%>PensionView/PensionEmpInfoExcelReport.jsp","mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   	 	}
   	  
   		
   	 }
   	 
   	  function callReport1()
   	 {
   	   if(document.forms[0].region.selectedIndex==1 ||document.forms[0].region.selectedIndex==4  || document.forms[0].region.selectedIndex==2){
   		  alert("Developement Activities are in Progress");
   		  return false;
   		  }
   	   window.open ("<%=basePath%>PensionView/PensionEmpComparewestReport.jsp","mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   		
   	 }
   	 
  	 function frmLoad()
  {
  process.style.display="none";
  
  <% EmpMasterBean dbBeans = new EmpMasterBean();
			SearchInfo getSearchInfo = new SearchInfo();
			int index=0;
			if (request.getAttribute("searchInfo") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				//boolean flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				
				region =dbBeans.getRegion();
				}%>
			var region1='<%=region%>';
			if(region1==""){
			region1="Select One" ;
			}
			//var myBoolean=new Boolean("false");
			//if(!myBoolean){
			//getAirports1();
			//myBoolean="true";
			//}
			document.forms[0].region[document.forms[0].region.options.selectedIndex].text=region1;
	/*   ***** Validations ****** */
	
	
  }
 


</script>
	</head>

	<body class="BodyBackground" onload="document.forms[0].airPortCode.focus();frmLoad();getAirports();">
		<form name="test" action="" method="get">
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


				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						Personnel Master[Search]
					</td>

					<%boolean flag = false;%>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td height="15%">
						<table align="center">
						<tr>
								<td class="label">
									Region:
								</td>
							<%	if (session.getAttribute("usertype").equals("Admin")) {						
						 
						    %>	
								<td>
				<SELECT NAME="region" onchange="getAirports()" style="width:130px">
					<option value="">[Select One]</option>
					<option value="AllRegions">	AllRegions</option>
										<%int j = 0;
			boolean exist = false;
			while (it.hasNext()) {
				region = hashmap.get(it.next()).toString();
				j++;
				if (region.equalsIgnoreCase(dbBeans.getRegion()))
					exist = true;
				if (region.equalsIgnoreCase(dbBeans.getRegion())) {

					%>
										<option value="<%=dbBeans.getRegion()%>" <% out.println("selected");%>>
											<%=dbBeans.getRegion()%>
										</option>


										<%} else {%>
										<option value="<%=region%>">
											<%=region%>
										</option>

										<%}

			%>

										<%}

			%>
									</SELECT>
								</td>

							<td class="label">
									Airport Code:
								</td>
								<td>
									<select name="airPortCode" >
									<option value="">[Select One]</option>
									
					 <% try{if (request.getAttribute("airportList") != null) {
							ArrayList airpors = (ArrayList) request
							.getAttribute("airportList");
						for (int i = 0; i < airpors.size(); i++) {

						EmpMasterBean airportBean = (EmpMasterBean) airpors
								.get(i);
								System.out.println(airportBean.getStation());

						%>
							<option  value="<%=airportBean.getStation()%>"><%=airportBean.getStation()%></option>
							<%} }						
							}   catch (Exception e){
           System.out.println("An exception occurred: " + e.getMessage());
           }
							%>
						    	</select>
						    	
								</td>
							</tr>
						<%						
						}else if(session.getAttribute("usertype").equals("User")||session.getAttribute("usertype").equals("NODAL OFFICER")) {													
						%>					
						<td>
						<select name="region" onchange="getAirports()" style="width:130px" >
						<option value="">[Select One]</option>
						<%if(session.getAttribute("region")!=null){
						String [] reglist=(String[])session.getAttribute("region");												
						for(int i=0;i<reglist.length;i++){
						%>
					
						<option value="<%=reglist[i]%>"><%=reglist[i]%></option>					
						<%}%>
			            </select>
			            </td>
			            
			            <td class="label">
									Airport Code:
								</td>
								<td>
									<select name="airPortCode" >
									<option value="">[Select One]</option>
									
					 <% try{if (request.getAttribute("airportList") != null) {
							ArrayList airpors = (ArrayList) request
							.getAttribute("airportList");
						for (int i = 0; i < airpors.size(); i++) {

						PensionBean airportBean = (PensionBean) airpors
								.get(i);
								%>
							<option  value="<%=airportBean.getAirportCode()%>"><%=airportBean.getAirportCode()%></option>
							<%} }						
							}   catch (Exception e){
                  out.println("An exception occurred: " + e.getMessage());
                 }
							%>
						    	</select>
						    	
								</td>
						<%}
							}						
						%>	
						
							<tr>
								<td class="label">
									Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)"> &nbsp;<input name="empNameChecked" type="checkbox"   />
								</td>
								<td class="label">
									Designation:
								</td>
								<td>
									<input type="text" name="desegnation" onkeyup="return limitlength(this, 20)">
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
									Old CpfAccno:
								</td>
								<td>
									<input type="text" name="cpfaccno">
								</td>
							</tr>
							
							<tr>
								<td class="label">
									Deleted Records:
								</td>
								<td>
									<select name="recordVerified" tabindex="8" style="width:130px">
										<option value='N'>
											No
										</option>
										<option value='Y'>
											Yes
										</option>
									</select>
								</td>
									
							</tr>
							<tr>

								<td align="left">
									&nbsp;
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
						<%dbBeans = new EmpMasterBean();
			getSearchInfo = new SearchInfo();
			index = 0;
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				index = searchBean.getStartIndex();
				session.setAttribute("getSearchBean1", dbBeans);
				dataList = searchBean.getSearchList();
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
				System.out.println("bottomGrid "+bottomGrid);
				System.out.println("dataList "+dataList.size());
				if (dataList.size() == 0) {

				%>
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

				<%} else if (dataList.size() != 0) {%>
				<tr>

					<td>
						<table align="center">
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="right">
									<input type="button" alt="first" value="|<" name=" First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button"  alt="pre" value="<" name=" Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
									<SELECT NAME="select_reportType" style="width:88px" onChange="javascript:callReport()">
									<option value="" >[Select Report]</option>
									  <option value="html">Html</option>
										<option value="ExcelSheet">Excel Sheet</option>
									</SELECT>
									<!-- <img src="./PensionView/images/printIcon.gif" alt="Report" onClick="callReport()"> -->
									<!-- <img src="./PensionView/images/printIcon.gif" alt="ComparativeWestDataReport"  onClick="callReport1()"> -->

								</td>
							</tr>
						</table>
				</tr>
				<tr>
					<td height="25%">
						<table align="center" width="97%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
							<tr class="tbheader">
								<td class="tblabel">
									CPF NO
								</td>
								<td class="tblabel">
									Region
								</td>
								<td class="tblabel">
									Pension Number&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Airport Code&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Employee Code&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Employee Name&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Designation
								</td>
								<td class="tblabel">
									D.O.B
								</td>
								<td class="tblabel">
									Pension Option&nbsp;
								</td>
								<td class="tblabel">
									Division&nbsp;
								</td>
								<!-- <td class="tblabel">
									Remarks
								</td> -->
								<td>
									<img src="./PensionView/images/page_edit.png" alt="edit"   border="0" />
									

								</td>
								<%if(session.getAttribute("usertype").equals("User")) {													
						          %>					
								<!--<td>
									<a href="<%=basePath%>search1?method=delete"> <img src="./PensionView/images/cross.png" alt="delete"  border="0"  onclick="deleteMultipule();" /></a>
								</td> -->
								<%}%>

							</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", employeeCode = "", cpfacno = "", pensionNumber = "";
				String dateofBirth = "", pensionOption = "", remarks = "", lastActive = "", dateofJoining = "",division="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					PensionBean beans = (PensionBean) dataList.get(i);
					cpfacno = beans.getCpfAcNo();
					airportCode = beans.getAirportCode();
					desegnation = beans.getDesegnation();
					employeeName = beans.getEmployeeName();
					employeeCode = beans.getEmployeeCode();
					pensionNumber = beans.getPensionnumber();
					dateofBirth = beans.getDateofBirth();
					pensionOption = beans.getPensionOption();
					remarks = beans.getRemarks();
					dateofJoining = beans.getDateofJoining();
					region = beans.getRegion();
					division=beans.getDivision();
					lastActive = beans.getLastActive();

					if (count % 2 == 0) {

					%>
							<tr>
								<%} else {%>
							<tr>
								<%}%>

								<td class="Data" width="12%">
									<%=cpfacno%>
								</td>
								<td class="Data" width="12%">
									<%=region%>
								</td>
								<td class="Data">
									<%=pensionNumber%>
								</td  class="Data">

								<td class="Data">
									<%=airportCode%>
								</td>
								<td class="Data">
									<%=employeeCode%>
								</td>

								<td class="Data" width="12%">
									<%=employeeName%>
								</td>
								<td class="Data">
									<%=desegnation%>
								</td>

								<td class="Data" width="10%">
									<%=dateofBirth%>
								</td>

								
								<td class="Data" width="5%">
									<%=pensionOption%>
								</td>
								<td class="Data">
									<%=division%>
								</td>
                                
								<td>
									<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:editPensionMaster('<%=cpfacno%>','<%=employeeName%>','<%=employeeCode%>','<%=region%>','<%=airportCode%>','<%=index%>','<%=totalData%>','<%=dbBeans.getRecordVerified()%>')" />
								
								</td>
								<!--<td><%if(session.getAttribute("usertype").equals("User")) {													
						            %>
									<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:deletePensionMaster('<%=cpfacno%>','<%=employeeName%>','<%=employeeCode%>','<%=region%>','<%=airportCode%>')" />
									<%}%>
								<input type="hidden" name="cpfnostring<%=cpfacno%>" value="<%=cpfacno%>,<%=employeeName%>,<%=employeeCode%>,<%=region%>,<%=airportCode%>"> 
								<input type="hidden" name="cpfnostring<%=cpfacno%>" value="<%=cpfacno%>,<%=employeeName%>,<%=employeeCode%>,<%=region%>,<%=airportCode%>"> 
								</td>-->
								
							</tr>
							<%}%>

							<%if (dataList.size() != 0) {%>
							<tr>

								<td class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>

							<%}
			}%>

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
