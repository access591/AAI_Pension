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
	function redirectPageNav(navButton,index,totalValue){      
		//alert('Redirct'+index);
		var flag="invflag";
		document.forms[0].action="<%=basePath%>search1?method=navigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&flag="+flag;
		//alert(document.forms[0].action)
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function openURL(sURL) { 
//	alert(sURL);
		window.open(sURL,"Window1","menubar=no,width=430,height=360,toolbar=no");

	} 
	
	function editPensionMaster(obj,employeeName){
	
		var cpfacno=obj;
		var flag="invflag";
		var answer =confirm('Are you sure, do you want edit this record');
	if(answer){
	  
		document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	
	
 function testSS(){
     
    	document.forms[0].action="<%=basePath%>search1?method=searchPensionMasterInvalidRecords"
		document.forms[0].method="post";
		document.forms[0].submit();
   		 }
   		 
   		  function callReport()
   		 {
   		// alert("call repeport");
   	      window.open ("<%=basePath%>PensionView/PensionEmpInfoInvalidRecordReport.jsp","mywindow","status=1,toolbar=1,channelmode=yes,resizable=yes"); 

   		//	document.forms[0].action="./PensionEmpInfoReport.jsp"
		//	document.forms[0].method="post";
			//document.forms[0].submit();
   		
   		 }
	

</script>
	</head>

	<body class="BodyBackground" onload="document.forms[0].airPortCode.focus();">

		<form action="" method="post">
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
						<h2>
							Personnel Master[InvalidData]
						</h2>
					</td>
					<%boolean flag = false;%>
				</tr>
				<tr>
					<td height="15%">
						<table align="center">
							<tr>
								<td class="label">
									Airport Code:
								</td>
								<td>
									<input type="text" name="airPortCode" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
								</td>

							</tr>
							<tr>
								<td class="label">
									Designation:
								</td>
								<td>
									<input type="text" name="desegnation" onkeyup="return limitlength(this, 20)">
								</td>
								<td class="label">
									CPF ACC.No:
								</td>
								<td>
									<input type="text" name="cpfaccno">
								</td>
							</tr>
							<tr>
								<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="employeeCode" onkeyup="return limitlength(this, 20)">
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
			<% EmpMasterBean dbBeans = new EmpMasterBean();
			SearchInfo getSearchInfo = new SearchInfo();
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				int index = searchBean.getStartIndex();
				//	out.println("index "+index);
				session.setAttribute("getSearchBean1", dbBeans);
				dataList = searchBean.getSearchList();
				System.out.println("datalist "+dataList.size());
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
				if (dataList.size() != 0) {

					%>
				<tr>

					<td>
						<table align="center"  >
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="right">
									<input type="button" class="btn" alt="first" value="|<" name="First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" class="btn" alt="pre" value="<" name="Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" class="btn" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" class="btn" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								     <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
									
								</td>
							</tr>
						</table>
					
				</tr>
				<tr>
					<td height="25%">
						<table align="center"  cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">

								<td class="tblabel">CPF ACC.NO</td>
								<td class="tblabel">Pension Number&nbsp;&nbsp;</td>
								<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
								<td class="tblabel">Designation	</td>
								
								<td>
									<img src="./PensionView/images/page_edit.png" alt="edit"/>
								</td>
                            <%}%>
							</tr>
							<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", employeeCode = "", cpfacno = "",  pensionNumber="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					PensionBean beans = (PensionBean) dataList.get(i);

					cpfacno = beans.getCpfAcNo();
					airportCode = beans.getAirportCode();

					desegnation = beans.getDesegnation();
					employeeName = beans.getEmployeeName();
					employeeCode =beans.getEmployeeCode();
                   pensionNumber=beans.getPensionnumber();
					if (count % 2 == 0) {

					%>
							<tr >
								<%} else {%>
							<tr >
								<%}%>

								<td class="Data">
									<%=cpfacno%>
								</td>
								<td  class="Data">
									<%=pensionNumber%>
								</td  class="Data">
								
								<td  class="Data">
									<%=airportCode%>
								</td>
								<td  class="Data">
									<%=employeeCode%>
								</td>
								
								<td  class="Data">
									<%=employeeName%>
								</td>
								<td  class="Data">
									<%=desegnation%>
								</td>
								<td>
									<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:editPensionMaster('<%=cpfacno%>','<%=employeeName%>')" />
								</td>
							</tr>
							<%}%>

							<%if (dataList.size() != 0) {%>
							<tr>
							
								<td class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
							</tr>
							<%}
			}%>

						</table>
					</td>

				</tr>

			</table>



		</form>
	</body>
</html>
