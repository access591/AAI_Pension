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
					
		String[] year = {"1995-96","1996-97","1997-98","1998-99","1999-00","2000-01","2001-02","2002-03","2003-04","2004-05","2005-06","2006-07","2007-08","2008-09","2009-10","2010-11","2011-12","2012-13"};
        String[] userYears = {"2008-09","2009-10"};		
					
					
    String region="",monthID="",monthNM="";
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
	
	
	 function Search(){		 
	 
			 if(document.forms[0].select_year.value=='NO-SELECT'){
			   alert("Please Select Year");
			   document.forms[0].select_year.focus();
			   return false;
			 }
		document.getElementById('process').style.display='block';
    	document.forms[0].action="<%=basePath%>pfinance?method=yearlyempcountsearch&frm_name=search";    	
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
var swidth=screen.Width-10;
		var sheight=screen.Height-150;
	
 var url="<%=basePath%>pfinance?method=yearlyempcountsearch&frm_name=report";
 
 wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
 //document.forms[0].method="post";
// document.forms[0].submit();
      		
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
	function submit_Pension(effectiveDt,employeeName,cpfaccno,pensionno,employeeno,designation,unitCD,region){
					
		var sortingOrder="",frm_empflag="true",employeeName="NO-SELECT",pfidStrip="NO-SELECT";
		
		path="<%=basePath%>reportservlet?method=cardReport&frmName=CPFDeviation&frm_region="+region+"&frm_year="+effectiveDt+"&frm_empnm="+employeeName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+pensionno+"&sortingOrder="+sortingOrder+"&frm_pfids="+pfidStrip;
			
		wind1 = window.open(path,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+750+",height="+500+",top=0,left=0");
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

				</table>
				<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>

					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						PFID Wise No of Months Salary Drawn
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
								<td class="label" align="right" width="25%">
									Year:<font color="red">&nbsp;*</font>
								</td>
								<td>
									<select name='select_year' style='width:100px'>
										<option value='NO-SELECT'>
											Select One
										</option>
								<%for (int j = 0; j < year.length; j++) {%>
																						<option value='<%=year[j]%>'>
																							<%=year[j]%>
																						</option>
																						<%}%>
									</select>
								</td>
								
								<td class="label">
									PF ID :
								</td>
								<td>
									<input type="text" name="pfid" />
								</td>
								
							</tr>							
								
							 <tr>
								<td class="label" align="right" width="25%">
									Region :
								</td>
								<td>
									<select  name="select_region" style="width:100px" onchange="javascript:getAirports('airport');">
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
									<select name="select_airport" style="width:100px" >
																			<option value='NO-SELECT' selected>
																				[Select One]
																			</option>
																		</select>
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center">
									<input type="button" class="btn" value="Search" class="btn" onclick="Search();" />
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn"/>
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
								</td>
							</tr>
							 
						</table>

						</td>
					</tr>
					<tr>
					 <td>&nbsp;</td>
					</tr>
			 </table>
			<table align="center" width="75%">
			 <tr> 
			 <td colspan="6">&nbsp;<td>
				<td  colspan="18" align="center">
				<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
				<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
				<span class="label" >Processing.......</span> 
				</div>
			 </td>
			
			</tr>
			 </table>	 
				<%
				String previousMonth="";
				       if (request.getAttribute("empCountList") != null) {
				       ArrayList searchDeviationList=new ArrayList();
				       FinacialDataBean fBean=new FinacialDataBean();
				       
				       
				       searchDeviationList=(ArrayList)request.getAttribute("empCountList");
				         System.out.println("----------"+searchDeviationList.size());
				         
				     
				       
				       
				%>
				<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				  <tr>
				  <td align="center">
				  <img src="./PensionView/images/printIcon.gif" alt="Report"  onclick="callReport()" />
				  </td>
				  </tr>
				
							
							<tr>							
														
								<td align="center" width="100%">
														
									<display:table  style="width: 800px" id="advanceList" sort="list"   pagesize="150" name="requestScope.empCountList" requestURI="./pfinance?method=yearlyempcountsearch" > >											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								
    								<display:column property="pensionNumber" title="PF ID" class="datanowrap"/>
									<display:column property="monthYear"   headerClass="sortable" title="Finacial Year" class="datanowrap"/>
									<display:column property="airportCode" title="Airport Code"/>	
									<display:column property="region" title="Region"/>										
									<display:column property="remarks"  title="Total Records" />
										
									<display:column title="Report" media="html">
								  	<a href="javascript:submit_Pension('<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getMonthYear()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getEmployeeName()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getCpfAccNo()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getPensionNumber()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getEmployeeNewNo()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getDesignation()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getAirportCode()%>','<%=((FinacialDataBean)pageContext.getAttribute("advanceList")).getRegion()%>')">
										<img src="./PensionView/images/viewDetails.gif" border='0' alt='PF Card'/>
									</a>
									</display:column>																				
								
															
									</display:table>
								</td>
							</tr>
							
		
		
				<%
				  }
				%>
				

						</table>
					 
		</form>
			
	</body>
</html>
