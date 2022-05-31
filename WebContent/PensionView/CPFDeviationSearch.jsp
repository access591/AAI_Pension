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
			 
			  if(document.forms[0].toDt.value==""){
			   alert("Please Enter To Date");
			   document.forms[0].toDt.focus();
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
   		     if(!document.forms[0].toDt.value==""){
   		    var date1=document.forms[0].toDt;
   	        var val1=convert_date(date1);
   		    if(val1==false)
   		     {
   		      return false;
   		     }
   		    }   		

    	document.forms[0].action="<%=basePath%>pfinance?method=deviationsearch";
    	
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
   		
    
   		 function submit_Pension(effectiveDt,employeeName,cpfaccno,pensionno,employeeno,designation,unitCD,region){
				 
				
		
		  	path="<%=basePath%>validatefinance?method=getFinanceDetailEdit&frmName=CPFDeviation&cpfaccno="+cpfaccno+"&pensionno="+pensionno+"&airportCD="+unitCD+"&effectiveDate="+effectiveDt+"&employeeNM="+employeeName+"&employeeno="+employeeno+"&designation="+designation+"&region="+region;
		
	

	document.forms[0].action=path;
	document.forms[0].method="post";
	document.forms[0].submit();	
}


 
function callReport()
{
	
 document.forms[0].action="<%=basePath%>pfinance?method=loadcpfdeviationInput";
 document.forms[0].method="post";
 document.forms[0].submit();
      		
}

   	 
	</script>
	</head>
	<body class="BodyBackground">
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
						CPF Deviation Statement
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
									PF ID :
								</td>
								<td>
									<input type="text" name="pfid">
								</td>
							</tr>
							
							
							<tr>
								<td class="label">
									From Date:<font color="red">&nbsp;*</font>
								</td>
								<td>
									<input type="text" name="fromDt">
									<a href="javascript:show_calendar('forms[0].fromDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
									<td class="label">
									To Date:<font color="red">&nbsp;*</font>
								</td>
								<td>
									<input type="text" name="toDt">
									<a href="javascript:show_calendar('forms[0].toDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
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
				       if (request.getAttribute("searchlist") != null) {
				       ArrayList searchDeviationList=new ArrayList();
				     
				       searchDeviationList=(ArrayList)request.getAttribute("searchlist");
				         System.out.println("----------"+searchDeviationList.size());
				         
				         session.setAttribute("searchDeviationList", searchDeviationList);
				       
				       
				%>
				  <tr>
				  <td align="center">
				  <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
				  </td>
				  </tr>
				  
				 
							<tr>							
								<td align="center">
								
									<display:table style="width: 900px"  export="true" sort="list" id="advanceList" sort="list"  pagesize="10" name="requestScope.searchlist" requestURI="./pfinance?method=deviationsearch" >											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />
    								    							
    								
    								<display:column property="pensionNumber"  title="pension Number" class="datanowrap"/>
									<display:column property="monthYear" title="monthYear" class="datanowrap"/>
    								<display:column property="cpfAccNo"  headerClass="sortable" title="cpfAccNo" class="datanowrap"/>
									<display:column property="employeeNewNo"  title="employeeNewNo" class="datanowrap"/>
									<display:column property="employeeName" title="employeeName"/>								
									<display:column property="emoluments"  title="emoluments" class="datanowrap"/>									
									<display:column property="empPfStatuary" title="empPfStatuary" />
									<display:column property="cpfArriers"  title="Calc CPF" class="datanowrap"/>
									<display:column property="cpfDifference"  title="Diff CPF" class="datanowrap"/>
									<display:column property="airportCode"  title="Airport Code" class="datanowrap"/>
									<display:column property="region"  title="Region" class="datanowrap"/>
									
									
									<display:column title="Report" media="html">
								  	<a href="javascript:submit_Pension('<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getMonthYear()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getEmployeeName()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getCpfAccNo()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getPensionNumber()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getEmployeeNewNo()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getDesignation()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getAirportCode()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRegion()%>')">
										<img src="./PensionView/images/edit.gif" border='0' alt='Form-2'>
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
	</body>
</html>
