<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";


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
	<script type="text/javascript">
	
	
	
    function testSS(){
       	document.forms[0].action="<%=basePath%>search1?method=pensionHistory"
     //  	alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
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
			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
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
					Employee Pension Histroy 
					</td>
					
					<%boolean flag = false;%>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td height="15%">
						<table align="center">
							<tr>
								<td class="label">
									Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									Employee Serial No:
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
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
			    //dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				//index = searchBean.getStartIndex();
				//	out.println("index "+index);
				session.setAttribute("getSearchBean1", dbBeans);
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dataList = searchBean.getSearchList();
				System.out.println("data list size  " +dataList.get(1));
				//totalData = searchBean.getTotalRecords();
			//	bottomGrid = searchBean.getBottomGrid();
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

								
								<td class="tblabel">CpfaccNO&nbsp;&nbsp;</td>
								<td class="tblabel">EmployeeName&nbsp;&nbsp;</td>
								<td class="tblabel">EmployeeNo </td>
								<td class="tblabel">Region</td>
								<td class="tblabel">Emoluments </td>
								<td class="tblabel">EmpPfStatuary</td>
								<td class="tblabel">MonthYear</td>
															
								<td>
						
						</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "", employeeCode = "", cpfacno = "", empSerailNumber="";
				String region="",emoluments="",empPfStatuary="",monthYear="";
					for (int i = 0; i < dataList.size(); i++) {
					count++;
					FinacialDataBean beans = (FinacialDataBean) dataList.get(i);
					employeeName = beans.getEmployeeName();
				    employeeCode = beans.getEmployeeNewNo();
                    region=beans.getRegion();
                    emoluments = beans.getEmoluments();              
                    empPfStatuary=beans.getEmpPfStatuary();
                    cpfacno = beans.getCpfAccNo();
                    monthYear = beans.getMonthYear();
					if (count % 2 == 0) {

					%>
							<tr >
								<%} else {%>
							<tr >
								<%}%>

								<td  class="Data" width="12%">
									<%=cpfacno%>
								</td>								
								<td  class="Data" width="18%">
									<%=employeeName%>
								</td>
								<td  class="Data" width="12%">
									<%=employeeCode%>
								</td>
								
								<td  class="Data" width="15%">
									<%=region%>
								</td>
								
								<td  class="Data" width="15%">
									<%=emoluments%>
								</td>
								<td  class="Data" width="15%">
									<%=empPfStatuary%>
								</td>
								<td  class="Data" width="24%">
									<%=monthYear%>
								</td>
													
								
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
