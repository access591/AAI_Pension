<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
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
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
		//alert('Redirct'+index);
		document.forms[0].action="<%=basePath%>search1?method=navigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		//alert(document.forms[0].action)
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function openURL(sURL) { 
//	alert(sURL);
		window.open(sURL,"Window1","menubar=no,width=430,height=360,toolbar=no");

	} 
	
	
	
	
	
	function getLogHistory(){
	      var userType='<%=session.getAttribute("usertype")%>';
	      var user='<%=session.getAttribute("userid")%>';
	      if(userType!='Admin'){
	      alert(user+" User not Allowed to Access this Page");
	      return false;
	      }
	  	  if(!document.forms[0].loginFromDate.value==""){
   		   var date1=document.forms[0].loginFromDate;
   	       var val1=convert_date(date1);
   		  
   		    if(val1==false)
   		      {
   		      return false;
   		      }
   		    }
   		    
   		      if(!document.forms[0].loginToDate.value==""){
   		   var date1=document.forms[0].loginToDate;
   	       var val1=convert_date(date1);
   		  
   		    if(val1==false)
   		      {
   		      return false;
   		      }
   		    }
	    document.forms[0].action="<%=basePath%>PensionLogin?method=getLoginHistory";
		document.forms[0].method="post";
	
		document.forms[0].submit();
	
	}
 

</script>
	</head>

	<body class="BodyBackground" >
		<form  method="get">
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
						Login History
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
									User Name:
								</td>
								<td>
									<input type="text" name="username" onkeyup="return limitlength(this, 20)">
								</td>
															
								<td class="label">
												Login FromDate:
											</td>
											<td>
												<input type="text" name="loginFromDate"  tabindex="11" onkeyup="return limitlength(this, 20)" >
												<a href="javascript:retirement_calendar('forms[0].loginFromDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no"  /></a>
											</td>

							</tr>
							<tr>
							<td class="label">
												Login ToDate:
											</td>
											<td>
												<input type="text" name="loginToDate"  tabindex="11" onkeyup="return limitlength(this, 20)" >
												<a href="javascript:retirement_calendar('forms[0].loginToDate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no"  /></a>
											</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								</tr>
							<tr>

								<td align="left">
								<td>
									<input type="button" class="btn" value="Search" class="btn" onclick="getLogHistory();">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>

							</tr>
						</table>
					</td>

				</tr>

				<tr>
					<td height="25%">
			<% UserBean  userbean = new UserBean();
			SearchInfo  getSearchInfo = new SearchInfo();
			int index = 0;
			 if (request.getAttribute("logList") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
			//	searchBean = (SearchInfo) request.getAttribute("searchBean");
			//	dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				
			//	index = searchBean.getStartIndex();
				//	out.println("index "+index);
			//	session.setAttribute("getSearchBean1", dbBeans);
				dataList =(ArrayList)request.getAttribute("logList");
			
				//totalData = searchBean.getTotalRecords();
			//	bottomGrid = searchBean.getBottomGrid();
				if (dataList.size() == 0) {

				%>
				<tr>

					<td>
						<table align="center" id="norec" width="60%" >
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
						<table align="center" width="60%" >
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="center">
									<!-- <input type="button" class="btn" alt="first" value="|<" name=" First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" class="btn" alt="pre" value="<" name=" Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button" class="btn" alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" class="btn" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
									
									<img src="./PensionView/images/printIcon.gif" alt="Report" onClick="callReport()"> -->
									<!-- <img src="./PensionView/images/printIcon.gif" alt="ComparativeWestDataReport"  onClick="callReport1()"> -->

								</td>
							</tr>
						</table>
				</tr>
				<tr>
					<td height="25%">
						<table align="center" width="60%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
							<tr class="tbheader">
								<td class="tblabel">
									User Name
								</td>
								<td class="tblabel">
									Login Time&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Computer Name&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Region &nbsp;&nbsp;
								</td>
							</tr>
							<%}%>
				<%int count = 0;
				String userName="",loginTime="",computerName="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					UserBean beans = (UserBean) dataList.get(i);

					userName = beans.getUsername();
					loginTime = beans.getLoginTime();
					computerName = beans.getComputerName();
					region = beans.getRegion();

					if (count % 2 == 0) {

					%>
							<tr>
								<%} else {%>
							<tr>
								<%}%>

								<td class="Data">
									<%=userName%>
								</td>
								<td class="Data">
									<%=loginTime%>
								</td  class="Data">
								<td class="Data">
									<%=computerName%>
								</td  class="Data">
								<td class="Data">
									<%=region%>
								</td  class="Data">
							</tr>
							<%}}%>

							
			

						</table>
					</td>

				</tr>

			</table>



		</form>
	</body>
</html>
<%}
%>
