<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.FinancialYearBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="java.beans.Beans"%>
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
	 <SCRIPT   type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
      	<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
	
   		document.forms[0].action="<%=basePath%>csearch?method=financialYearNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
	} 		
   function editFinancialYearMaster(obj){

	   var financialId=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	    if(answer){
		document.forms[0].action="<%=basePath%>csearch?method=financialYearEdit&financialId="+financialId;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	</script>

	</head>
	<%
    String monthID="",monthName="",tempMonthVal="",tempMonthNameVal="";
  	CommonUtil common=new CommonUtil();    

   	Map map=new LinkedHashMap();
	map=common.getMonthsList();
	Set monthset = map.entrySet(); 
	Iterator it  = monthset.iterator(); 

  %>
	<body>
		<form method="post">
          <table>
		<tr>
					<td>			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>					
					</td>
				</tr>	
		
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						<br><h2>Financial Year Master</h2>
					</td>

				</tr>
				<%boolean flag = false;%>
				<tr>
					<td height="15%">
						<table align="center">							
						</table>
					</td>
				</tr>
				<%
				DatabaseBean dabBeans = new DatabaseBean();
				FinancialYearBean  dbBeans= new FinancialYearBean();
			    SearchInfo getSearchInfo = new SearchInfo();
			

		   	if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");				
				int index = searchBean.getStartIndex();				
				
				dataList = searchBean.getSearchList();				
				totalData = searchBean.getTotalRecords();				
				bottomGrid = searchBean.getBottomGrid();	

				if (dataList.size() == 0) {		
					%>
                   <tr>

					<td>
						<table align="center">
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
				<tr>

					<td>
						<table align="center">
							<tr>
								
								<td colspan="2" align="right">
									<input type="button" alt="first" value="|<" name=" First" disable=true onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>

									<strong><input type="button" value="<" name=" Pre"  onClick="redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>> <strong><input type="button" value=">" name="Next"
												onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>> <input type="button" value=">|" name="Last" onClick="redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>> 				
										
								</td>

							</tr>
							
							<tr>
				 	<td height="25%">
							<table align="center"  cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">
								<td class="tblabel">
									Financial Year &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Financial Month &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Description &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								
								<td></td>
								<%}%>
							</tr>
							<%
							   int count=0,financialId = 0;
							   String year="",month="",description="";							
				
				             for (int i = 0; i < dataList.size(); i++) {
					            count++;
					            FinancialYearBean beans = (FinancialYearBean) dataList.get(i);

                                year=beans.getFromDate();					
					            month=beans.getMonth();
							    description=beans.getDescription();
								financialId=beans.getFinancialId();					
                 				if (count % 2 == 0) {
					       %>					
							<tr class="tbevenrow">
								<%} else {%>
								<tr class="tboddrow">
							
							<%}%>
							  <td class="Data"><%=year%>
							  </td>
							   <td class="Data"><%=month%>
							   </td>
							  <td class="Data"><%=description%>
							  </td>
							  
							 <% 
							  ArrayList al=new ArrayList();
								 String financialYear="";
							  if(request.getAttribute("FinancialYear")!=null){
							     al=(ArrayList)request.getAttribute("FinancialYear");
						            FinancialYearBean fbean=new FinancialYearBean();
						            fbean=(FinancialYearBean)al.get(0);
						            financialYear=fbean.getFromDate();
						            
						            if(financialYear.equals(year)){
						            %>
						              <td><input type="checkbox" name="financialId" value="<%=financialId%>" onclick="javascript:editFinancialYearMaster('<%=financialId%>')" />									
								      </td>
						            <%}else{%>
						             <td><img src="./PensionView/images/lockIcon.gif" />	
									 </td>						            
						            <%}
							  }														
							}
							if (dataList.size() != 0) {%>
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
	</body>
</html>
