<!--
/*
  * File       : FinanceDataSearch.jsp
  * Date       : 05/02/2009
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->


<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean,aims.bean.SearchInfo,aims.bean.BottomGridNavigationInfo"%>
<%@ page import="aims.bean.FinacialDataBean"%>

<%@ taglib uri="/tags-display" prefix="display"%>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
					
    String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	Iterator regionIterator = null;
            Iterator monthIterator = null;
          
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
               // Set keys = hashmap.keySet();
                regionIterator = keys.iterator();

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
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css">
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></script>
		<script type="text/javascript"> 
		function redirectPageNav(navButton,index,totalValue){      
	    alert('redirectPageNav');
   		document.forms[0].action="<%=basePath%>psearch?method=financenavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue;
		document.forms[0].method="post";
		document.forms[0].submit();
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
	
	
	 function testSS(){
			
			 if(document.forms[0].fromDt.value==""){
			   alert("Please Enter From Date");
			   document.forms[0].fromDt.focus();
			   return false;
			 }
			 
			 			 
	    if(!document.forms[0].fromDt.value==""){
   		    var date1=document.forms[0].fromDt;
   	        var val1=convert_date(date1);
   		    if(val1==false)
   		     {
   		      return false;
   		     }
   		    }
   		  	

    	document.forms[0].action="<%=basePath%>pfinance?method=transfersearch";
    	
		document.forms[0].method="post";
		document.forms[0].submit();
   		 }
   		 
   		 function callReport1()
   		 {  
   		 window.open ("<%=basePath%>PensionView/FinanceReports.jsp","mywindow","menubar=yes,status=yes,location=yes,toolbar=yes,scrollbars=yes,width=800,height=600,resizable=yes"); 
   		 	//document.forms[0].action="./PensionFundReport.jsp"
			//document.forms[0].method="post";
			//document.forms[0].submit();
   		
   		 }
   		
   		 
   		 function callReport1()
   		 {
   	    window.open ("<%=basePath%>PensionView/FinanceDataMissingMonthSearch.jsp","mywindow","menubar=yes,status=1,toolbar=1,channelmode=yes,resizable=yes"); 
   		
   		 }
   		
    
   		 function submit_Pension(effectiveDt,employeeName,cpfaccno,pensionno,employeeno,designation,unitCD,missingflag,region){
				 var path="";
				 if(missingflag=='N'){
				  
				 if(cpfaccno=='' || cpfaccno==null){
				    alert('cpfaccno Cannot be blank');
				    return false;
			 }
				 if(pensionno=='' || pensionno==null){
				    alert('pensionno Cannot be blank');
				    return false;
			 }
			
			
		  	
				 }
				
		if(missingflag=='N'){
		  	path="<%=basePath%>validatefinance?method=getFinanceDetailEdit&frmName=CPFDeviation&cpfaccno="+cpfaccno+"&pensionno="+pensionno+"&airportCD="+unitCD+"&effectiveDate="+effectiveDt+"&employeeNM="+employeeName+"&employeeno="+employeeno+"&designation="+designation+"&region="+region;
		}
	

	document.forms[0].action=path;
	document.forms[0].method="post";
	document.forms[0].submit();	
}


 
function callReport()
{
	
 document.forms[0].action="<%=basePath%>pfinance?method=loadtransferInput";
 document.forms[0].method="post";
 document.forms[0].submit();
      		
}

function getAirports(param){	
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID;
					if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		createXMLHttpRequest();	
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getAirports&region="+regionID;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}else{
		}
		
		
		
		xmlHttp.send(null);
    }
   		
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
	 
	 function getAirportsList()
	{
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			 process.style.display="block";
		}
		if(xmlHttp.readyState ==4)
		{
			if(xmlHttp.status == 200)
				{ 
			      	var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					 process.style.display="none";
					  if(stype.length==0){
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_airport");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_airport");
		    // alert(stype.length);	
		  	obj1.options.length = 0;
		  	
		  	for(i=0;i<stype.length;i++){
		  		if(i==0)
					{
				//	alert("inside if")
					obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}
		         // 	alert("in else");
			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
			}
		  }
		}
	}
	 
}

function getNodeValue(obj,tag)
   	{
		return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   	}

function frmload(){
		 process.style.display="none";
	}
	
	
	 function updateDetails(pensionNo,status,airportCode,region){	  
	 	 				
	 	 
			var url="<%=basePath%>pfinance?method=loadtransferdetails&frm_pensionno="+pensionNo+"&frm_status="+status+"&frm_airportcode="+airportCode+"&frm_region="+region;
			wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+750+",height="+500+",top=0,left=0");
	   	 	winOpened = true;
			wind1.window.focus();
		
		}
		
	</script>
	</head>
	<body class="BodyBackground" onload="javascript:frmload()">
		<form method="post" action="<%=basePath%>psearch?method=financesearch">

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
						Transfer In/Out
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<%boolean flag = false;%>
				<tr>
					<td>
						<table align="center">
							<tr>
								<td class="label">
									Region :
								</td>
								<td>
									<select  name="select_region" style="width:120px" onchange="javascript:getAirports('airport');">
																			<option value="NO-SELECT">
																				[Select One]
																			</option>
																			<%while (regionIterator.hasNext()) {
																			region = hashmap.get(regionIterator.next()).toString();
															
																			%>
																			<option value="<%=region%>">
																				<%=region%>
																			</option>
																			<%}%>
																		</select>
								</td>
								
									<td class="label">
									Airport Code :
								</td>
								<td>
									<SELECT NAME="select_airport" style="width:120px">
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
								</td>
							</tr>
							
							
							<tr>
								<td class="label">
									 Date:<font color="red">&nbsp;*</font>
								</td>
								<td>
									<input type="text" name="fromDt">
									<a href="javascript:show_calendar('forms[0].fromDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								
								<td class="label">
									PF ID :
								</td>
								<td>
									<input type="text" name="pfid">
								</td>
								
							</tr>
							
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center">
									<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>

						
			

				
				
				<%
				String previousMonth="";
				       if (request.getAttribute("transferInList") != null) {
				       ArrayList searchDeviationList=new ArrayList();
				       FinacialDataBean fBean=new FinacialDataBean();
				       
				       
				       searchDeviationList=(ArrayList)request.getAttribute("transferInList");
				         System.out.println("----------"+searchDeviationList.size());
				         
				        fBean=(FinacialDataBean)searchDeviationList.get(0);
				         
				          System.out.println("------11111-----"+fBean.getPreviousMonth());
				          previousMonth=fBean.getPreviousMonth();
				         session.setAttribute("searchDeviationList", searchDeviationList);
				       
				       
				%>
				  <tr>
				  <td align="center">
				  <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
				  </td>
				  </tr>
				
							
							<tr>							
														
								<td align="center" width="100%">
														
									<display:table style="width: 800px" id="advanceList" sort="list"  pagesize="1000" name="requestScope.transferInList" requestURI="./pfinance?method=transfersearch" > >											
														  
    								<display:caption>List of Additional Employess with respect to previous month i.e, <%=previousMonth%></display:caption>
															
    								
    								<display:column property="pensionNumber" title="PF ID" class="datanowrap"/>
									<display:column property="cpfAccNo"   headerClass="sortable" title="CPF A/c No" class="datanowrap"/>
									<display:column property="employeeName" title="Employee Name"/>	
									<display:column property="designation" title="Designation"/>	
									<display:column property="airportCode"  title="Airport Code" class="datanowrap"/>
									<display:column property="region"  title="Region" />
									<display:column property="remarks"  title="Reason" />
								
																
									<display:column title="View" media="html">
								  	<a href="javascript:updateDetails('<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getPensionNumber()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRemarks()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getAirportCode()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRegion()%>')">
								  	 	<img src="./PensionView/images/viewDetails.gif" border='0' alt='View'>
									</a>
									</display:column>
															
									</display:table>
								</td>
							</tr>
							
		
		
				<%
				  }
				%>
					
					<%
				       if (request.getAttribute("transferOutList") != null) {
				        ArrayList searchDeviationList=new ArrayList();
				       FinacialDataBean fBean=new FinacialDataBean();
				       
				       
				       searchDeviationList=(ArrayList)request.getAttribute("transferOutList");
				         System.out.println("----------"+searchDeviationList.size());
				         
				        fBean=(FinacialDataBean)searchDeviationList.get(0);
				         
				          System.out.println("------11111-----"+fBean.getPreviousMonth());
				          previousMonth=fBean.getPreviousMonth();
				         session.setAttribute("searchDeviationList", searchDeviationList);
				
				
				     %>
				     <tr>							
														
								<td align="center" width="100%">
														
									<display:table style="width: 800px" id="advanceList" sort="list"  pagesize="1000" name="requestScope.transferOutList" requestURI="./pfinance?method=transfersearch" > >											
									
									<display:caption>List of Missing Employess with respect to previous month i.e,<%=previousMonth%> </display:caption>
															
    								
    								<display:column property="pensionNumber" title="PF ID" class="datanowrap"/>
									<display:column property="cpfAccNo"   headerClass="sortable" title="CPF A/c No" class="datanowrap"/>
									<display:column property="employeeName" title="Employee Name"/>	
									<display:column property="designation" title="Designation"/>	
									<display:column property="airportCode"  title="Airport Code" class="datanowrap"/>
									<display:column property="region"  title="Region" />
									<display:column property="remarks"  title="Reason" />
									
																
									<display:column title="View" media="html">
								  	<a href="javascript:updateDetails('<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getPensionNumber()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRemarks()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getAirportCode()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRegion()%>')">
								  	 	<img src="./PensionView/images/viewDetails.gif" border='0' alt='View'>
									</a>
									</display:column>
															
									</display:table>
								</td>
							</tr>
				     <%
				     
				     }
				     %>


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
