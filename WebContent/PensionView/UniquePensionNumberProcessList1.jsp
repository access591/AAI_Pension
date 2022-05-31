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
				session.removeAttribute("getSearchBean1");
				//session.setMaxInactiveInterval(1);
				//session.invalidate();
			}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<script type="text/javascript">
	
	function add(obj){
	 var x=document.getElementsByName("cpfno");
	 var uncheckList="";
	 for(var i=0;i<x.length;i++){
	 if( document.getElementsByName("cpfno")[i].checked==true){
		var cpfacnoString=document.forms[0].cpfno[i].value;
		temp = cpfacnoString.split(",");
		cpfacno = temp[0];
		region=temp[2];
		uncheckList+=","+cpfacno+"$"+region;
		
	//	alert("checkList "+uncheckList);
		document.forms[0].checklist.value=uncheckList;
	 }
	 }
	 }
	
	function addtoProcess(){
	document.forms[0].action="<%=basePath%>search1?method=addtoProcess"
	document.forms[0].method="post";
	document.forms[0].submit();
	}
	
	var xmlHttp;

	function createXMLHttpRequest()
	{
	 if(window.ActiveXObject)
	 {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 }
	 else if (window.XMLHttpRequest)
	 {
		xmlHttp = new XMLHttpRequest();
	 }
	}
	function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }

 function testSS(){
      
 		 if((document.forms[0].empName.value=="")&&(document.forms[0].dob.value=="")){
    	alert("Please Enter Any one of the field for Search");
    	document.forms[0].empName.focus();
        return false;
    	}
    	  var dateofbirth=document.forms[0].dob.value;
    	  var empName=document.forms[0].empName.value;             
    	 createXMLHttpRequest();	
    	  var url ="<%=basePath%>search1?method=searchRecordsbyDobandNameUnprocessList&dateofbirth="+dateofbirth+"&empName="+empName;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getSearchList;
		xmlHttp.send(null);
     	//document.forms[0].action="<%=basePath%>search1?method=searchRecordsbyDobandName"
		//document.forms[0].method="post";
	//	document.forms[0].submit();
   	 }

	function getSearchList()
	{
	if(xmlHttp.readyState ==4)
	{
	//  alert(xmlHttp.responseText);
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  if(stype.length==0){
		 	var cpfacno = document.getElementById("cpfacno");
		   	  
		  }else{
		  divlist.style.display="block"; 
		   divlist.innerHTML="";
		   showbuttons.innerHTML="";
		   divlist.innerHTML="<table ><tr><td height='5%' width='10%' colspan='6' align='center' class='ScreenHeading'>Unprocess List</td></tr></table>";
		   divlist.innerHTML=divlist.innerHTML+"<table align='center' width='100%' cellpadding=2 class='tbborder' cellspacing='0' border='0'><tr class='tbheader'><b><td class='tblabel'> Region</td><td class='tblabel'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CPFAccno</td><td class='tblabel'>EmployeeName</td><td class='tblabel'> DateofBirth</td><td class='tblabel'> DateofJoining</td></b><td class='tblabel'><img src='./PensionView/images/addIcon.gif' alt='Add' /></td></tr></table>";
		//   var str="";
		// divlist.innerHTML=divlist.innerHTML+"<table align='center' class='tbborder' width='70%'  cellpadding=2  cellspacing='0'  border='0'>";
	 		for(i=0;i<stype.length;i++){
	 	 	 var cpfacno = getNodeValue(stype[i],'cpfacno');
		     var employeename= getNodeValue(stype[i],'employeename');
		     var dateofbirth = getNodeValue(stype[i],'dateofbirth');
		     var dateofjoin= getNodeValue(stype[i],'dateofjoin');
		     var region= getNodeValue(stype[i],'region');
		   
		 	 divlist.innerHTML=divlist.innerHTML+"<table align='center'  width='100%'  cellpadding=1  cellspacing='0'  border='0'><tr><td  class='Data' width='20%'>"+region+"</td><td  class='Data' width='20%'>"+cpfacno+" </td><td  class='Data' width='20%'>"+employeename+"</td><td  class='Data' width='20%'>"+dateofbirth+" </td><td  class='Data' width='20%'>"+dateofjoin+"</td><td width='15%'><input type='checkbox' name='cpfno' value='"+cpfacno+","+employeename+","+region+","+dateofbirth+","+dateofjoin+"' onclick='add(this.value)'></td>   </tr></table>";
		  //   alert(divlist.innerHTML);
	 	 	}
	 	   showbuttons.innerHTML= showbuttons.innerHTML+"<table align='center'><td align='center'><input type='button' value='Add' class='btn' onclick='addtoProcess()'><input type='button' value='Reset' onclick='javascript:document.forms[0].reset()' class='btn'><input type='button' value='Cancel' onclick='javascript:history.back(-1)' class='btn'></td></tr></table>";
	 	   
	 	    }
	 			 
		}
	 }
	 }

	
    	
   	function deleteEmpSerailNumber(cpfacno,employeeName,region,airportCode,empSerailNumber){
	
	var answer =confirm('Are you sure, do you want delete this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=unprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&empSerailNumber="+empSerailNumber;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
   	
  function hideMe1()
  {
	divlist.style.display="none"; 
	}  	
   
</script>
	</head>

	<body class="BodyBackground" onload="hideMe1();">
		<form name="test" action="">
			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
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
						Processed List
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

					</td>

				</tr>

				<tr>
					<td height="25%">
						<%EmpMasterBean dbBeans = new EmpMasterBean();
			SearchInfo getSearchInfo = new SearchInfo();
			int index = 0;
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				index = searchBean.getStartIndex();
					System.out.println("index "+index);
				session.setAttribute("getSearchBean1", dbBeans);
				dataList = searchBean.getSearchList();
				System.out.println("dataLIst " + dataList.size());
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
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
									<!--  <input type="button" alt="first" value="|<" name="First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" alt="pre" value="<" name="Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								     <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
								   <img src="./PensionView/images/printIcon.gif" alt="ComparativeWestDataReport"  onClick="callReport1()"> -->

								</td>
							</tr>
						</table>
				</tr>
				<tr>
					<td height="25%">
						<table align="center" width="70%" cellpadding=2 class="tbborder" cellspacing="0" border="0">

							<tr class="tbheader">
								<td class="tblabel">
									Region&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Cpfaccno&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Employee Name&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Employee SerialNo&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									DateofBirth
								</td>
								<td class="tblabel">
									DateofJoining
								</td>

								<td>
									<img src="./PensionView/images/cross.png" alt="Delete" /></td>
							</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", employeeCode = "", cpfacno = "", pensionNumber = "";
				String dateofBirth = "", pensionOption = "", remarks = "", lastActive = "", dateofJoining = "", empSerailNumber = "";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmpMasterBean beans = (EmpMasterBean) dataList.get(i);
					cpfacno = beans.getCpfAcNo();

					employeeName = beans.getEmpName();
					dateofBirth = beans.getDateofBirth();
					empSerailNumber = beans.getEmpSerialNo();
					dateofJoining = beans.getDateofJoining();
					region = beans.getRegion();
					airportCode = beans.getStation();
					if (count % 2 == 0) {

					%>
							<tr>
								<%} else {%>
							<tr>
								<%}%>

								<td class="Data" width="15%">
									<%=region%>
								</td>
								<td class="Data" width="15%">
									<%=cpfacno%>
								</td>
								<td class="Data" width="20%">
									<%=employeeName%>
								</td>
								<td class="Data" width="15%">
									<%=empSerailNumber%>
									<input type="hidden" name="empSerailNumber" value="<%=empSerailNumber%>">
								</td>

								<td class="Data" width="15%">
									<%=dateofBirth%>
								</td>

								<td class="Data" width="15%">
									<%=dateofJoining%>
								</td>

								<td>
									<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:deleteEmpSerailNumber('<%=cpfacno%>','<%=employeeName%>','<%=region%>','<%=airportCode%>',<%=empSerailNumber%>)" />

								</td>

							</tr>


							<%}%>
							<tr>
								&nbsp;
							</tr>
							<tr>
								<td align="center"></td>
								<td></td>
								<td>
								<td></td>
								<td>
								</td>
							</tr>
							<%if (dataList.size() != 0) {%>
							<tr>


							</tr>

							<%}
			}%>

						</table>
					</td>

				</tr>
				<table align="center">
					<tr>
						<td>
							&nbsp;
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
							Date of Birth:
						</td>
						<td>
							<input type="text" name="dob" onkeyup="return limitlength(this, 20)">
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							<input type="button" value="Search" class="btn" onclick="testSS();">
						</td>
					</tr>

					<tr>
						<td align="left">
							&nbsp;
						<td>
					</tr>


				</table>
				<center>

				</center>

				<br>
			</table>
			<tr>
				<td colspan="3">
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
            
			<div id="divlist" align="left"  class="containdiv" >
			</div>
			<div id="showbuttons">
			</div>
			<input type="hidden" name="checklist">
			<!-- <table align="center">
		      <tr>
					<td>
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td align="center">
						<input type="button" value="Add" class="btn" onclick="addtoProcess()" tabindex="36">
						<input type="button" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="37">
						<input type="button" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="38">
					</td>
				</tr>
			</table>-->
		</form>



	</body>
</html>
