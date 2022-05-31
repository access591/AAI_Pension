<%@ page language="java"%>
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
		
		
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<script type="text/javascript">
		
		function validateForm(cpfaccno,region) {
		//alert("inside validate cpfacccno"+cpfaccno+"region"+region);
		var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		//var empserialNO	=empserialNO;
		reportType="ExcelSheet";
		yearID="NO-SELECT";
		var params = "&frm_region="+region+"&frm_airportcode="+airportcode+"&frm_year="+yearID+"&frm_reportType="+reportType+"&cpfAccno="+cpfaccno;
		var url="<%=basePath%>reportservlet?method=getReportPenContr"+params;
		//alert("url"+url);
				if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
		
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
    { if(obj.getElementsByTagName(tag)[0].firstChild){
	  return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	 }else return "";
   }
   
	function add(){

	 var x=document.getElementsByName("cpfno");
	 var uncheckList="";
	 var  checkedCount=0;
	 for(var i=0;i<x.length;i++){
	 if( document.getElementsByName("cpfno")[i].checked==true){
		var cpfacnoString=document.forms[0].cpfno[i].value;
		temp = cpfacnoString.split(",");
		cpfacno = temp[0];
		region=temp[2];
		employeeno=temp[6];
		//uncheckList+=","+cpfacno+"$"+region;
		uncheckList+=","+cpfacno+"$"+region+"$"+employeeno;
		//alert("checkList "+uncheckList);
		document.forms[0].checklist.value=uncheckList;
	 }
	
	 }
	 }
	
	function addtoProcess(){
	add();
	var x=document.getElementsByName("cpfno");
	var count=0;
	var checkedCount=0;
	 for(var i=0;i<x.length;i++){
	 if( document.getElementsByName("cpfno")[i].checked==false){
	   count++;
	    if(x.length==count){
	 	alert("Please Select Atleast One Cpfaccno");
        return false;
	    }
	   
	
	}
   if(x.length>=1){
   if( document.getElementsByName("cpfno")[i].checked==true){
	 checkedCount++;
	 var cpfacnoString=document.forms[0].cpfno[i].value;
	
		temp = cpfacnoString.split(",");
		cpfacno = temp[0];
		region=temp[2];
		dateofbirth=temp[3];
		pensionOption=temp[5];
		if(dateofbirth=="" || dateofbirth==" "){
		alert("One of the Selected Cpfaccno's doen't have DateofBirth, Please Click on CPFACC.NO to Edit DateofBirth") ;
		document.forms[0].cpfno[i].focus();
		return false;
		}
		if(pensionOption==""){
		alert("One of the Selected Cpfaccno's doen't have DateofBirth, Please Click on CPFACC.NO to Edit PensionOption") ;
		document.forms[0].cpfno[i].focus();
		return false;
		}
   }
	}
	
	}
	document.forms[0].action="<%=basePath%>search1?method=addtoProcess";
	document.forms[0].method="post";
	 alert("You don't have the  permissions to Map the Record");
	//document.forms[0].submit();
	}
	function setEmpsearchName(){
	<%if(session.getAttribute("employeeSearchName")!=null){%>
	document.forms[0].empName.value='<%=session.getAttribute("employeeSearchName")%>';
	<%}%>
	<%if(session.getAttribute("dob1")!=null){%>
	document.forms[0].dob.value='<%=session.getAttribute("dob1")%>';
	<%}%>
	
	//testSS();
	
	}
	
	function editPensionMaster(obj,employeeName,employeeCode,region,airportCode){
	    var empName="",dob1="";
	      empName=document.forms[0].empName.value;
    	  dob1=document.forms[0].dob.value;  
    //	 alert("empname "+empName+"dob "+dob1);
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	if(answer){
		var flag="true";
		var getProcessUnprocessList="getProcessUnprocessList";
		document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode+"&editFrom="+getProcessUnprocessList+"&empName="+empName+"&dob1="+dob1;
	//	alert("document.forms[0].action "+document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	}
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
      }

	function getSearchList()
	{
	if(xmlHttp.readyState ==4)
	{
	// alert(xmlHttp.responseText);
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		  if(stype.length==0){
		 	var cpfacno = document.getElementById("cpfacno");
		 	divlist.style.display="block"; 
		   	divlist.innerHTML="<table ><tr><td height='5%' width='10%' colspan='6' align='center'>No Records Found</td></tr></table>";  
		    showbuttons.innerHTML="";
		  }else{ divlist.style.display="block"; 
		   divlist.innerHTML="";
		   showbuttons.innerHTML="";
		   divlist.innerHTML="<table ><tr><td height='5%' width='14%' colspan='7' align='center' class='ScreenHeading'>Unprocess List</td></tr></table>";
		   divlist.innerHTML=divlist.innerHTML+"<table align='center' width='100%' cellpadding=2 class='tbborder' cellspacing='0' border='0'><tr class='tbheader'><b><td class='tblabel'>Old<br>CPFAccno</td><td class='tblabel'> &nbsp;&nbsp;Region</td><td class='tblabel'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EmployeeName</td><td class='tblabel'>&nbsp;&nbsp;&nbsp; DateofBirth</td><td class='tblabel'> DateofJoining</td><td class='tblabel'> PensionOption</td></b><td class='tblabel'>PFReport</td><td class='tblabel'><img src='./PensionView/images/addIcon.gif' alt='Add' /></td></tr></table>";
			for(i=0;i<=stype.length;i++){
			
			 var cpfacno="";
			  if( getNodeValue(stype[i],'cpfacno')!=null){
	 	 	 cpfacno = getNodeValue(stype[i],'cpfacno');
	 	 	 }else cpfacno="";
	 	 	 var employeename="";
	 	 	 if( getNodeValue(stype[i],'employeename')!=null){
		     employeename= getNodeValue(stype[i],'employeename');
		     }else employeename="";
		     var dateofbirth="";
		     if(( getNodeValue(stype[i],'dateofbirth')!=null) ||( getNodeValue(stype[i],'dateofbirth')!="")){
		       dateofbirth = getNodeValue(stype[i],'dateofbirth');
		     
		     } else dateofbirth="";
		     var dateofjoin="";
		     if( getNodeValue(stype[i],'dateofjoin')!=null){
		     dateofjoin= getNodeValue(stype[i],'dateofjoin');
		     }else dateofjoin="";
		      var region="";
		      if( getNodeValue(stype[i],'region')!=null){
		      region= getNodeValue(stype[i],'region');
		      }else region="";
		      var airportcode="";
		     if( getNodeValue(stype[i],'airport')!=null){
		      airportcode= getNodeValue(stype[i],'airport');
		      }else airportcode="";
		    
		     var employeeno="";
		     if( getNodeValue(stype[i],'employeeNo')!=null){
		      employeeno= getNodeValue(stype[i],'employeeNo');
		      }else employeeno="";
		
		  var pensionOption="";
		     if( getNodeValue(stype[i],'pensionOption')!=null){
		      pensionOption= getNodeValue(stype[i],'pensionOption');
		      }else pensionOption="";
		      
		     
		 	 divlist.innerHTML=divlist.innerHTML+"<table align='center'  width='100%'  cellpadding=1  cellspacing='0'  border='0'><tr><td  class='Data' width='14%'><a href='#' onClick=\"editPensionMaster('"+cpfacno+"','"+employeename+"','"+employeeno+"','"+region+"','"+airportcode+"');\">"+cpfacno+" </a> </td><td  class='Data' width='14%'>"+region+"</td><td  class='Data' width='20%'>"+employeename+"</td><td  class='Data' width='16%'>"+dateofbirth+" </td><td  class='Data' width='14%'>"+dateofjoin+"</td><td  class='Data' width='14%'>"+pensionOption+"</td><td  class='Data' width='14%'><a href='#' onClick=\"validateForm('"+cpfacno+"','"+region+"');\"><img src='./PensionView/images/viewDetails.gif' border='0' alt='PFReport' > </a></td><td width='14%'><input type='checkbox' name='cpfno' value='"+cpfacno+","+employeename+","+region+","+dateofbirth+","+dateofjoin+","+pensionOption+","+employeeno+"' onclick='add()'></td>   </tr></table>";
		 if(i==(stype.length)-1){
	
		 showbuttons.innerHTML= showbuttons.innerHTML+"<table align='center'><td align='center'><input type='button' value='Add' class='btn' onclick='addtoProcess()'><input type='button' value='Reset' onclick='javascript:document.forms[0].reset()' class='btn'><input type='button' value='Cancel' onclick='javascript:history.back(-1)' class='btn'></td></tr></table>";
	 	 	}
	 	 	}
	 	
		   }
	 			 
		}
	 }
	 }

	
    	
   	function deleteEmpSerailNumber(cpfacno,employeeName,region,airportCode,empSerailNumber,empCode){
	
	var answer =confirm('Are you sure, do you want delete this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=unprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&empSerailNumber="+empSerailNumber+"&empCode="+empCode;
		document.forms[0].method="post";
		document.forms[0].submit();
	}else{
	document.forms[0].cpfno.checked=false;
	}
	}
   	
  function hideMe1()
  {
	divlist.style.display="none"; 
	}  	
   
</script>
	</head>

	<body class="BodyBackground" onload="hideMe1();setEmpsearchName();">
		<form name="test" action="">
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
									Arrear Financial Data Mapping[Edit] 				
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
				//	out.println("index "+index);
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
						<table align="center" width="70%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
                         <tr><td align='center' class='ScreenHeading' width='10%' colspan='7'>Processed List</td></tr>
							<tr class="tbheader">
								<td class="tblabel">
									Old CpfAccno&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Region&nbsp;&nbsp;
								</td>
								
								<td class="tblabel">
									Employee Name&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Form3-2007-Sep- PFID&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									DateofBirth
								</td>
								<td class="tblabel">
									DateofJoining
								</td>
								<td class="tblabel">
									PensionNumber
								</td>

								<td>
									<img src="./PensionView/images/cross.png" alt="Delete" /></td>
							</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "",   cpfacno = "", pensionNumber = "",employeeCode="";
				String dateofBirth = "",dateofJoining = "", empSerailNumber = "";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					EmpMasterBean beans = (EmpMasterBean) dataList.get(i);
					cpfacno = beans.getCpfAcNo();
					airportCode = beans.getStation();
					
					employeeCode =beans.getEmpNumber();
					employeeName = beans.getEmpName();
					dateofBirth = beans.getDateofBirth();
					System.out.println("dateofBirth "+dateofBirth);
					empSerailNumber = beans.getEmpSerialNo();
					dateofJoining = beans.getDateofJoining();
					region = beans.getRegion();
					airportCode = beans.getStation();
					pensionNumber=beans.getPensionNumber();
				//	pensionNumber=pensionNumber.substring(0,8);
				//	pensionNumber=pensionNumber+""+empSerailNumber;
					if (count % 2 == 0) {

					%>
							<tr>
								<%} else {%>
							<tr>
								<%}%>
								<td class="Data" width="15%">
									<%=cpfacno%>
								</td>
								<td class="Data" width="15%">
									<%=region%>
								</td>
								<td class="Data" width="20%">
									<%=employeeName%>
								</td>
								<td class="Data" width="15%">
									<%=empSerailNumber%>
									<input type="hidden" name="empSerailNumber" value="<%=empSerailNumber%>">
									<input type="hidden" name="dateofbirth" value="<%=dateofBirth%>">
									<input type="hidden" name="pensionNumber" value="<%=pensionNumber%>">
								</td>
							
								
								

								<td class="Data" width="15%">
									<%=dateofBirth%>
								</td>

								<td class="Data" width="15%">
									<%=dateofJoining%>
								</td>
								<td class="Data" width="15%">
									<%=pensionNumber%>
								</td>
								

								<td>
									<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:deleteEmpSerailNumber('<%=cpfacno%>','<%=employeeName%>','<%=region%>','<%=airportCode%>','<%=empSerailNumber%>','<%=employeeCode%>')" />

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
							<input type="text" name="empName" >
						</td>
						<td class="label">
							Date of Birth:
						</td>
						<td>
							<input type="text" name="dob" >
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
						</td>
						<td ><a href="#" onclick="javascript:history.back(-1)"><img src="./PensionView/images/viewBack.gif" border="0" alt="Back" ></a>
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
						<input type="button" class="btn" value="Add" class="btn" onclick="addtoProcess()" tabindex="36">
						<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" tabindex="37">
						<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" tabindex="38">
					</td>
				</tr>
			</table>-->
		</form>



	</body>
</html>
