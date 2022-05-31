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
	 var xmlHttp;
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
	function resetMaster(){
			document.forms[0].action="<%=basePath%>PensionView/PensionOptinChanged.jsp";
		document.forms[0].method="post";
		document.forms[0].submit();

} 
	function Search(){
		process.style.display="none"; 		
		var sortColumn="EMPLOYEENAME";
		var pfid=document.forms[0].pensionNO.value;
		var employeename=document.forms[0].empName.value;
	document.forms[0].action="<%=basePath%>psearch?method=loadPensionOption&pfid="+pfid+"&frm_sortingColumn="+sortColumn+"&employeename="+employeename;
  	document.forms[0].method="post";
	document.forms[0].submit();
	}
	function redirectPageNav(navButton,index,totalValue){      
		var sortColumn="EMPLOYEENAME";
		document.forms[0].action="<%=basePath%>psearch?method=personalnav&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&frm_sortingColumn="+sortColumn;
 		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function updatePensionOption(status,pensionno,empname,dateofbirth){
		process.style.display="none"; 
		 var answer="";
		if(status=="A"){
		   answer =confirm('Are you sure, do you want to Change The WetherOptin as A');
		}else{
		   answer =confirm('Are you sure, do you want to Change The WetherOptin as B');
		}
		   if(answer){
			   var changedPensionOption=status;
		var url="<%=basePath%>psearch?method=optionchange&option="+changedPensionOption+"&pensionno="+pensionno+"&dateofbirth="+dateofbirth+"&empname="+empname;
		 createXMLHttpRequest();	
	   	 	xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = pcreportverified1;
			xmlHttp.send(null);
		   }
	 }
	 function pcreportverified1()
		{
		 process.style.display="block";
		if(xmlHttp.readyState ==4)
			
		{
		 alert(xmlHttp.responseText);
		 process.style.display="none";
			}
		}
	function hidediv(){
		 process.style.display="none";
	}
</script>
</head>
<body class="BodyBackground" onLoad="hidediv() ">
<form name="personalMaster" action="" method="post">
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="5%" colspan="2" align="center" class="ScreenHeading">PF ID[Search]</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="15%">
				<table align="center">
<tr>
						<td class="label">Employee Name:</td>
						<td><input type="text" name="empName" onkeyup="return limitlength(this, 20)"></td>
						<td class="label">PF ID:</td>
						<td><input type="text" name="pensionNO" onkeyup="return limitlength(this, 20)"></td>
						
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
     int j=0;
					SearchInfo getSearchInfo = new SearchInfo();
					int totalData = 0,index = 0;
					if (request.getAttribute("searchBean") != null) {
					SearchInfo searchBean = new SearchInfo();
					ArrayList dataList = new ArrayList();
					BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
					searchBean = (SearchInfo) request.getAttribute("searchBean");
					dbBeans = (EmployeePersonalInfo) request.getAttribute("searchInfo");
					index = searchBean.getStartIndex();
					//out.println("index "+index);
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
								<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
								<td class="tblabel">Region</td>
                               	<td class="tblabel">Pension Option Change</td>
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
								<td class="Data"><%=personal.getAirportCode()%></td>
								<td class="Data" width="12%"><%=personal.getRegion()%></td>	
<td class="Data" width="10%" align="center"> <select name="pensionOption<%=j%>"  onchange="updatePensionOption(this.value,'<%=personal.getPfID()%>','<%=personal.getEmployeeName()%>','<%=personal.getDateOfBirth()%>');" style="width:50px"tabindex="6">
				
												<option value="A" <%if(personal.getWetherOption().trim().equals("A")){ out.println("selected");}%>>
														A
												</option>
												<option value="B" <%if(personal.getWetherOption().trim().equals("B")){ out.println("selected");}%>>
														B
												</option>
												</select>
                     </td>					 
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