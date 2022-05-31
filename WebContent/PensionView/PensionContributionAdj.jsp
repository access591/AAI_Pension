<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.UserBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.ArrearsTransactionBean"%>
<%
if(session.getAttribute("usertype").equals("User")){
String str=(String)session.getAttribute("userid");
%>
<script type="text/javascript"> 
    var msg="<%=str%>";    
	alert("Access Denied for '"+msg+"' user");
</script>
<jsp:include page="PensionMenu.jsp" flush="true" />
<%
}
else
{				       
												       
%>
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
	 <SCRIPT   type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
	 <SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>


	<script type="text/javascript">

	function redirectPageNav(navButton,index,totalValue){      
		var sortColumn="EMPLOYEENAME";
		document.forms[0].action="<%=basePath%>psearch?method=arrearsnav&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&frm_sortingColumn="+sortColumn;
      //  alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function validate(){
	     document.forms[0].action='<%=basePath%>psearch?method=arrearsnav&pensioncontriadj=true';
	     document.forms[0].method="post";
		 document.forms[0].submit();
  }

	function getArrearData(pensionno,dateOfAnnuation,pensionoption){
		
		 document.forms[0].action="<%=basePath%>psearch?method=getArrearsTransactionData&pensionno="+pensionno+"&dateOfAnnuation="+dateOfAnnuation+"&wetheroption="+pensionoption;
		 document.forms[0].method="post";
		 document.forms[0].submit();
	}
	</script>
		</head>
	<body>
		<form method="post">
          <table width="100%">
			<tr>
				<td>
				<jsp:include page="/PensionView/PensionMenu.jsp"/>
					</td>
				</tr>		
					<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						<br>Arrear PensionNo [SEARCH]
					</td>

				</tr>
		
				<%boolean flag = false;%>
				<tr>
					<td height="15%">
						<table align="center" >
						   <tr><td>&nbsp;</td></tr>
						    <tr>
						     <td class="label">
					    	 PensionNo:
						     </td>
						     <td>
							 <input type="text" name="pensionno">
						     </td>
						     				
                              <td class="label">
					    	 &nbsp;&nbsp; Employee Name:
						      </td>
						      <td>
							  <input type="text" name="empName">
						      </td>
                              </tr>
							 
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td colspan="2" align="left">
									<input type="button" class="btn" value="Search" class="btn" onclick="validate();">		
									<input type="button" class="btn" value="Reset" onclick="searchreset();" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1);" class="btn">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>

            </tr>
				<%
				int totalData = 0,index = 0;
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				ArrayList dataList = new ArrayList();
				if (request.getAttribute("searchBean") != null) {
					SearchInfo searchBean = new SearchInfo();
					searchBean = (SearchInfo) request.getAttribute("searchBean");
					index = searchBean.getStartIndex();
					//out.println("index "+index);
					dataList = searchBean.getSearchList();
					totalData = searchBean.getTotalRecords();
					bottomGrid = searchBean.getBottomGrid();
					
				}
				
				if (request.getAttribute("empList") != null) {
					
					dataList =(ArrayList) request.getAttribute("empList");
					  
				if (dataList.size() == 0) {		
					%>
                   <tr >

					<td>
						<table align="center" id="norec">
							<tr>
							<td>No Records Found</td>
							</tr>
                         </table>
					 </td>
					</tr>

					<%

				}			  
			else {		
                
               
					%>
               <% if (dataList.size() > 1) {%>
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
 <%} %>
				<tr >

					<td>
						<table align="center" id="rec">
							
							<tr>
					<td height="25%">
					<table align="center" cellpadding=2 class="tbborder" cellspacing="0"  border="0">
                  
							<tr class="tbheader">
							
								<td class="tblabel">
									PensionNo&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>								
								<td class="tblabel">
									EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Date of Birth
								</td>
							<td class="tblabel">
									Date of Annuation
								</td>
								<td class="tblabel">
									Region
								</td>
							<td class="tblabel">
									PensionOption
								</td>
						<td class="tblabel" >
							   <img src="<%=basePath%>PensionView/images/page_edit.png" >
							  </td>
							<td class="tblabel" >
							   <img src="<%=basePath%>PensionView/images/page_edit.png" >
							  </td>	
							<td class="tblabel" >
							   <img src="<%=basePath%>PensionView/images/page_edit.png" >
							  </td>		
								<td></td>
									<%}%>
							</tr>
							<%
								int count = 0;

							String wedate="";
							int rate=0,ceilingcd=0,userid=0;

							String username="",region="",expdate="";
										
				    for (int i = 0; i < dataList.size(); i++) {
					count++;
					ArrearsTransactionBean beans = (ArrearsTransactionBean) dataList.get(i);
       						
					region=beans.getRegion();				
					               
					
                 	if (count % 2 == 0) {

					%>
					
							<tr class="tbevenrow">
								<%} else {%>
								<tr class="tboddrow">
							
							<%}%>
					
							  <td class="Data">
							  <%=beans.getPensionNumber()%>
							  </td>							  
							  <td class="Data">
							  <%=beans.getEmpName()%>
							  </td>
							  <td class="Data">
							  <%=beans.getDateofBirth()%>
							  </td>
							<td class="Data">
							  <%=beans.getDateOfAnnuation()%>
							  </td>
							<td class="Data">
							  <%=beans.getRegion()%>
							  </td>
								<td class="Data">
							  <%=beans.getWetherOption()%>
							  </td>
   				<td  class="Data" ><a href="#" title="Click the link to Enter Arrears" onClick="getArrearData('<%=beans.getPensionNumber()%>','<%=beans.getDateOfAnnuation()%>','<%=beans.getWetherOption()%>')"><img src="./PensionView/images/editIcon.gif" border='0'  width='12' height='12' ></a>
									&nbsp;&nbsp;
								</td>
	<td  class="Data" ><a  title="Click the link to View ArrerTransactionReport" target="new" href="./psearch?method=getArrearsTransactionDataReport&pensionno=<%=beans.getPensionNumber()%>&dateOfAnnuation=<%=beans.getDateOfAnnuation()%>"><img src="./PensionView/images/viewDetails.gif" border="0"></a>
   				
									&nbsp;&nbsp;
								</td>
<td  class="Data" ><a  title="Click the link to View Arrer12MonthsTransactionReport" target="new" href="./psearch?method=getArrears12MonthsTransactionDataReport&pensionno=<%=beans.getPensionNumber()%>&dateOfAnnuation=<%=beans.getDateOfAnnuation()%>&wetheroption=<%=beans.getWetherOption()%>"><img src="./PensionView/images/viewDetails.gif" border="0"></a>
   				
									&nbsp;&nbsp;
								</td>

							<%								
							}%>
							
				    <tr>

					<td class="Data">
						<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
					</td>
				</tr>
			<%} %>

						</table> 
					</td>
				</tr>
        
			</table>

		</form>
	</body>
</html>
<%}%>
