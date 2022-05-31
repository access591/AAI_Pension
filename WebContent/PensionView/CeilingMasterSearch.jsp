<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.CeilingUnitBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="java.beans.Beans"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
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
		<SCRIPT type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>

	</head>
	<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
	
   		document.forms[0].action="<%=basePath%>csearch?method=navigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function edit()
	{
		//alert("00000");
	}
	function openURL(sURL) { 
	//alert(sURL);
		window.open(sURL,"Window1","menubar=no,width='100%',height='100%',toolbar=no,fullscreen=yes");

	} 
	function hiLite(imgDocID, imgObjName, comment) {
	if (browserVer == 1) {
	document.images[imgDocID].src = eval(imgObjName + ".src");
	window.status = comment; return true;
	}}
	
	
	 function validate(){
       
		  if (!convert_date(document.forms[0].wedatefrom))
		  {
			document.forms[0].wedatefrom.focus();
			return false;
		  }	  
	   
	  if (!convert_date(document.forms[0].wedateto))
		  {
			document.forms[0].wedateto.focus();
			return false;
		  }	 
				
		if(document.forms[0].wedatefrom.value!="" &&  document.forms[0].wedateto.value!="")
	   {
			var cmp1=compareDates(document.forms[0].wedatefrom.value,document.forms[0].wedateto.value);
			 if (cmp1 =="larger")
			{
				alert(" To Date should  be greater than From Date");
				document.forms[0].wedatefrom.focus();
				return false;
			}	
	   }
	 return true;
	 }

 function testSS()
	{
		if(!validate())
			return false;
    	document.forms[0].action="<%=basePath%>csearch?method=csearch";
    	
		document.forms[0].method="post";
	
		document.forms[0].submit();
	
  }
  function setreset()
  {
	    document.forms[0].action="<%=basePath%>/PensionView/CeilingMasterSearch.jsp";    	
     	document.forms[0].method="post";
		document.forms[0].submit();   

  }   	
 function editCeilingMaster(obj){

	   var ceilingcd=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	    if(answer){
		document.forms[0].action="<%=basePath%>csearch?method=edit&ceilingcd="+ceilingcd;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	</script>
	<body>
		<form method="post">
			<table width="100%">
				<tr>
					<td>
						<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>

				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						<br>
						<h2>
							Ceiling Master
						</h2>
					</td>

				</tr>
				<%boolean flag = false;%>
				<tr>
					<td height="15%">
						<table align="center" width="100%">
							<tr>
								<td class="label" align="center">
									From W.E. Date:
								</td>
								<td>
									<input type="text" name="wedatefrom">


										&nbsp; 
									<a href="javascript:show_calendar('forms[0].wedatefrom');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>

							</tr>
							<tr>
								<td class="label" align="center">
									To W.E. Date:
								</td>
								<td>
									<input type="text" name="wedateto">
										&nbsp; 
									<a href="javascript:show_calendar('forms[0].wedateto');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
							</tr>


							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center" >
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
									<input type="button" class="btn" value="Reset" onclick="setreset();" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
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
				<%DatabaseBean dabBeans = new DatabaseBean();

            CeilingUnitBean dbBeans = new CeilingUnitBean();
            SearchInfo getSearchInfo = new SearchInfo();

            System.out.println("--------------------"
                    + request.getAttribute("searchBean"));

            if (request.getAttribute("searchBean") != null) {
                int totalData = 0;
                SearchInfo searchBean = new SearchInfo();
                flag = true;
                ArrayList dataList = new ArrayList();
                BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
                searchBean = (SearchInfo) request.getAttribute("searchBean");
                dbBeans = (CeilingUnitBean) request.getAttribute("searchInfo");
                int index = searchBean.getStartIndex();
                session.setAttribute("getSearchBean", dbBeans);
                dataList = searchBean.getSearchList();
                totalData = searchBean.getTotalRecords();
                bottomGrid = searchBean.getBottomGrid();

                if (dataList.size() == 0) {

                %>
				<tr>

					<td>
						<table align="center">
							<tr>
								<td>
									No Records Found
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<%} else {

                    %>
				<tr>

					<td>
						<table align="center" width="100%">
							<tr>

								<td colspan="2" align="center">
									<input type="button" alt="first" value="|<" name=" First" disable=true onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<strong><input type="button"  value="<" name=" Pre"  onClick="redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>> <strong><input type="button" value=">" name="Next"
												onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>> <input type="button"  value=">|" name="Last" onClick="redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								</td>

							</tr>
							<tr>
								<td height="25%">
									<table align="center" cellpadding=2 class="tbborder" cellspacing="0" border="0">

										<tr class="tbheader">
											<td class="tblabel">
												With Effect From Date &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="tblabel">
												With Effect To Date &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="tblabel">
												Rate &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>

											<td></td>
											<%}%>
										</tr>
										<%int count = 0;

                String wefromdate = "", wetodate = "";
                int ceilingcd = 0;
                double rate = 0.0, interestrate = 0.0;

                for (int i = 0; i < dataList.size(); i++) {
                    count++;
                    CeilingUnitBean beans = (CeilingUnitBean) dataList.get(i);

                    ceilingcd = beans.getCeilingCode();
                    System.out.println("ceilingcd..------------------...."
                            + ceilingcd);
                    wefromdate = beans.getFromWeDate();
                    wetodate = beans.getToWeDate();
                    rate = beans.getRate();
                    interestrate = beans.getInterestRate();

                    if (count % 2 == 0) {

                    %>

										<tr class="tbevenrow">
											<%} else {%>
										<tr class="tboddrow">

											<%}%>
											<td class="Data">
												<%=wefromdate%>
											</td>
											<td class="Data">
												<%=wetodate%>
											</td>
											<td class="Data">
												<%=rate%>
											</td>


											<td>
												<input type="checkbox" name="ceilingcd" value="<%=ceilingcd%>" onclick="javascript:editCeilingMaster('<%=ceilingcd%>')" />
											</td>


											<%}
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
