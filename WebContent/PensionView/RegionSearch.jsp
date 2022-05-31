<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.UserBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
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
	 <SCRIPT   type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>


	<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
	
   	  	document.forms[0].action="<%=basePath%>PensionLogin?method=regionNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function edit()
	{
		alert("00000");
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

	 if(document.forms[0].unitname.value=="")
   	 {
   		  alert("Please Enter Unit Name");
   		  document.forms[0].unitname.focus();
   		  return false;
   	  }   			
	 return true;
	 }


  function testSS(){	
   		document.forms[0].action="<%=basePath%>PensionLogin?method=searchregion";
		document.forms[0].method="post";
		document.forms[0].submit();
  } 

  function searchreset()
  {
	 document.forms[0].action="<%=basePath%>/PensionView/RegionSearch.jsp";    	
	 document.forms[0].method="post";	
	 document.forms[0].submit();         	   
  }   

 function editRegionMaster(obj1,obj2){	

		var regionid=obj1;
		var regionname=obj2;
		
		var answer =confirm('Are you sure, do you want edit this record');
	    if(answer){
		document.forms[0].action="<%=basePath%>PensionLogin?method=RegionEdit&regionid="+regionid+"&regionname="+regionname;		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	</script>
		</head>
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
						<br><h2>Region Master</h2>
					</td>

				</tr>
		
				<%boolean flag = false;%>
				<tr>
					<td height="15%">
						<table align="center" >
						    <tr>
						     <td class="label">
					    	  Region Name:
						     </td>
						     <td>
							 <input type="text" name="regionname">
						     </td>	                            
                             </tr>
							 
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>								
								<td  colspan="2" align="left">
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">		
									<input type="button" class="btn" value="Reset" onclick="searchreset();" class="btn">
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
				<%
				DatabaseBean dabBeans = new DatabaseBean();

				UserBean  dbBeans= new UserBean();
			    SearchInfo getSearchInfo = new SearchInfo();

			   if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				searchBean = (SearchInfo) request.getAttribute("searchBean");
				dbBeans = (UserBean) request.getAttribute("searchInfo");
				int index = searchBean.getStartIndex();	
				System.out.println("index  in NewUserSearch.jsp................................"+index);
				session.setAttribute("getSearchBean", dbBeans);
				dataList = searchBean.getSearchList();				
				totalData = searchBean.getTotalRecords();	
				bottomGrid = searchBean.getBottomGrid();	
			  
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
				<tr >

					<td>
						<table align="center" id="rec">
							<tr>
								
								<td colspan="2" align="center">
									<input type="button" alt="first" value="|<" name=" First" disable=true onClick="redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>

									<strong><input type="button" value="<" name=" Pre"  onClick="redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>> <strong><input type="button" value=">" name="Next"
												onClick="redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>> <input type="button" value=">|" name="Last" onClick="redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>> 
								</td>

							</tr>
							<tr>
					<td height="25%">
					<table align="center" cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">
								<td class="tblabel">
									Region Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									AAI Category&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Remarks&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>							
								<td>
							        <img src="<%=basePath%>PensionView/images/page_edit.png" onclick="edit()">
							  </td>
								
								<td></td>
									<%}%>
							</tr>
							<%
								int count = 0;

							
							int regionid=0;
							String regionname="",aaicategory="",remarks="";
										
				    for (int i = 0; i < dataList.size(); i++) {
					count++;
					UserBean beans = (UserBean) dataList.get(i);
                 			
					regionid=beans.getRegionId();
					System.out.println("..............regionid................"+regionid);
					regionname=beans.getRegion();
					System.out.println("regionname ..........................."+regionname);					
					aaicategory=beans.getAaiCategory();					
					remarks=beans.getRemarks();                 
					
                 	if (count % 2 == 0) {

					%>
					
							<tr class="tbevenrow">
								<%} else {%>
								<tr class="tboddrow">
							
							<%}%>
							  <td class="Data">
							  <%=regionname%>
							  </td>
							  <td class="Data">
							  <%=aaicategory%>
							  </td>
							  <td class="Data">
							  <%=remarks%>
							  </td>							
							  <td>
									<input type="checkbox" name="regioncd" value="<%=regionid%>" onclick="javascript:editRegionMaster('<%=regionid%>','<%=regionname%>')" />
								</td>				

							<%								
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
