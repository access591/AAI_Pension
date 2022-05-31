<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegionsForComparativeReport();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
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
	<script type="text/javascript">
	function redirectPageNav(navButton,index,totalValue){      
		//alert('Redirct'+index);
		var flag="compare";
		document.forms[0].action="<%=basePath%>search1?method=compartivenavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&flag="+flag;
		//alert(document.forms[0].action)
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function openURL(sURL) { 
//	alert(sURL);
		window.open(sURL,"Window1","menubar=no,width=430,height=360,toolbar=no");

	} 
	function editPensionMaster(obj,employeeName,employeeCode,region,airportCode){
	 
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
	
	function deletePensionMaster(obj,employeeName,employeeCode,region,airportCode){
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want delete this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=delete&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode+"&region="+region+"&airportCode="+airportCode;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
 function testSS(){
 
       <% if(session.getAttribute("usertype").equals("Admin")){
       %>
             var index=document.forms[0].region.selectedIndex;
              
               if(document.forms[0].region.selectedIndex<1)
   		       {
			   		  alert("Please Select Region");
			   		  document.forms[0].region.focus();
			   		  return false;
   		  		}
   		  		if(document.forms[0].region.selectedIndex==6 ){
		   		  alert("Developement Activities are in Progress");
		   		  return false;
		   		} 
		   	    var flag="false";
		    	document.forms[0].action="<%=basePath%>search1?method=searchCompartiveMaster";
				document.forms[0].method="post";
				document.forms[0].submit();
  
  
        <%}else if(session.getAttribute("usertype").equals("User")){%>
        		
        var flag="false";
    	document.forms[0].action="<%=basePath%>search1?method=searchCompartiveMaster";
		document.forms[0].method="post";
		document.forms[0].submit();
        <%}
        %>           	  
   		 }
   		 
   	 function callReport()
   	 {
   	   window.open ("<%=basePath%>PensionView/PensionCompartiveReport.jsp","mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   		
   	 }
   	 
   	  function callReport1()
   	 {
   	   window.open ("<%=basePath%>PensionView/PensionEmpComparewestReport.jsp","mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   		
   	 }
	  function callReport2()
   	 {

		var Region=document.forms[0].reghidden.value;
	
   	   window.open ("<%=basePath%>PensionView/PensionCpfComparewestReport.jsp?region="+Region,"mywindow","toolbar=yes,statusbar=yes,scrollbars=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   		
   	 }
  function frmLoad()
  {// alert("hi");
  <% EmpMasterBean dbBeans = new EmpMasterBean();
			SearchInfo getSearchInfo = new SearchInfo();
			int index=0;
			if (request.getAttribute("searchBean") != null) {
				int totalData = 0;
				SearchInfo searchBean = new SearchInfo();
				//boolean flag = true;
				ArrayList dataList = new ArrayList();
				BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
				dbBeans = (EmpMasterBean) request.getAttribute("searchInfo");
				region =dbBeans.getRegion();
				System.out.println("region is "+region);
				}%>
				var region1='<%=region%>';
				if(region1==""){
			    region1="Select One" ;
			  }
	/*   ***** Validations ****** */
	document.forms[0].region[document.forms[0].region.options.selectedIndex].text=region1;
	 
    
	
  }
	
function reset(){
document.forms[0].region.value="";
}
</script>
	</head>

	<body class="BodyBackground" onload="frmLoad();">

		<form name="test" action="" method="get">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>
			<tr>
			  <td>&nbsp;</td>
			  </tr> 
		</table> 
			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
					Comparative Master					
					</td>
					
					<%boolean flag = false;%>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td height="15%">
						<table align="center">
													
						<tr>					
						<td class="label">
 						  Region:&nbsp;&nbsp;&nbsp;
						</td>						
						
						<%			
						ArrayList regionlist=new ArrayList();			
						if (session.getAttribute("usertype") != null) {
						
						if (session.getAttribute("usertype").equals("Admin")) {												
						 
						%>		
						    <td>				
							<select name="region" style="width:130px">
							<option value="">	[Select One]</option>
							<%
									
							if(request.getAttribute("regionslist")!=null)
							{			  
							    regionlist=(ArrayList)request.getAttribute("regionslist");
					
								for(int i=0;i<regionlist.size();i++){ 
								
								PensionBean beans = (PensionBean) regionlist.get(i);
							
							 	String reg=beans.getRegion();
							%>
							  <option value="<%=reg%>"><%=reg%></option>        
							<%}}%>
						
							</select>
						</td>
						
						<%						
						}else if(session.getAttribute("usertype").equals("User")) {													
						%>					
						<td>
						<select name="region" style="width:130px">
						<option value="">[Select One]</option>
						<%if(session.getAttribute("region")!=null){
						String [] reglist=(String[])session.getAttribute("region");												
						for(int i=0;i<reglist.length;i++){
						System.out.println(".......Region........."+reglist[i]);
						%>
					
						<option value="<%=reglist[i]%>"><%=reglist[i]%></option>					
						<%}%>
			            </select>
			            </td>
						<%}
						}
						}
						%>							
								
							</tr>
							<tr>
								<td align="left">&nbsp;
								<td>
								</tr>
							<tr> 
								<td align="center" colspan="2">
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();" />
									<input type="button" class="btn" value="Reset" onclick="javascript:reset();" class="btn" />
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
							
								</td>

							</tr>
						</table>
					</td>

				</tr>
</table>
			<table align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td height="25%">
			<%  dbBeans = new EmpMasterBean();
			   getSearchInfo = new SearchInfo();
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
				System.out.println("Search reagion is "+dbBeans.getRegion());
				dataList = searchBean.getSearchList();
				totalData = searchBean.getTotalRecords();
				bottomGrid = searchBean.getBottomGrid();
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
									<input type="button" alt="first" value="|<" name="First" disable=true onclick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%> />
									<input type="button"  alt="pre" value="<" name="Pre"  onclick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%> />
									<input type="button"  alt="next" value=">" name="Next" onclick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%> />
									<input type="button"  value=">|" name="Last" onclick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%> />     
								    <!--    <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
								   <img src="./PensionView/images/printIcon.gif" alt="Comparative <%=dbBeans.getRegion()%>"  onClick="callReport1()"> -->
									<img src="./PensionView/images/printIcon.gif" alt="Comparative <%=dbBeans.getRegion()%>"  onClick="callReport2()">
									
								</td>
							</tr>
						</table>
					
				</tr>
				</table>
				 
						<table align="center"  width="75%" cellpadding=2 class="tbborder" cellspacing="0"  border="0">

							<tr class="tbheader">

								<td class="tblabel">CPF ACC.NO</td>								
								<td class="tblabel">Airport Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Code&nbsp;&nbsp;</td>
								<td class="tblabel">Employee Name&nbsp;&nbsp;</td>
								<td class="tblabel">Designation </td>
								<td class="tblabel">D.O.B </td>
								<td class="tblabel">D.O.J </td>
								<td class="tblabel">Sex&nbsp; </td>
								<td class="tblabel">F/H Name	</td>
					
							</tr>
							<%}%>
							<%int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", employeeCode = "", cpfacno = "";
				String dateofBirth="",sex="", fhname="",dateofJoining="",reg="";
				for (int i = 0; i < dataList.size(); i++) {
					count++;
					PensionBean beans = (PensionBean) dataList.get(i);
					cpfacno = beans.getCpfAcNo();
					airportCode = beans.getAirportCode();
					desegnation = beans.getDesegnation();
					employeeName = beans.getEmployeeName();
					employeeCode =beans.getEmployeeCode();                   
                    dateofBirth=beans.getDateofBirth(); 
					sex=beans.getSex(); 				
                    dateofJoining=beans.getDateofJoining();
					fhname=beans.getFHName(); 
					reg=beans.getRegion(); 
				           
                    
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
									<%=airportCode%>
								</td>
								<td  class="Data">
									<%=employeeCode%>
								</td>
								
								<td  class="Data" width="12%">
									<%=employeeName%>
								</td>
								<td  class="Data">
									<%=desegnation%>
								</td>
								
								<td  class="Data" width="10%">
									<%=dateofBirth%>
								</td>
								
								<td  class="Data" width="10%">
									<%=dateofJoining%>
								</td>
								<td  class="Data" width="5%">
									<%=sex%>
								</td>
								<td  class="Data">
									<%=fhname%>
								</td>								
								
								
							</tr>
							<%}%>
							<input TYPE="hidden" name="reghidden" value="<%=reg%>" />

							<%if (dataList.size() != 0) {%>
							<tr>
							
								<td colspan="3" class="Data">
									<font color="red"><%=index%></font> &nbsp;Of&nbsp;&nbsp;<font color="red"><%=totalData%></font>&nbsp;Records
								</td>
							</tr>
							
							<% }	
							 }%>

						</table>
					 



		</form>
	</body>
</html>
