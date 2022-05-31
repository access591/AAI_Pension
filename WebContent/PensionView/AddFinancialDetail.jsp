<!--
/*
  * File       : AddFinancialDetail.jsp
  * Date       : 12/12/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->

<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.FinacialDataBean"%>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
	String region="",effectiveDate="";
  	CommonUtil common=new CommonUtil();    
   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();
	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
%>

     <%EmpMasterBean bean1 = new EmpMasterBean();
			String aaiInfo = "";
			if (request.getAttribute("cpfnoDeatails") != null) {
				bean1 = (EmpMasterBean) request.getAttribute("cpfnoDeatails");
			}
			FinacialDataBean bean=new FinacialDataBean();	
			if(session.getAttribute("reportsearchBean")!=null){
			bean=(FinacialDataBean)session.getAttribute("reportsearchBean");
			}
			if(request.getParameter("effectiveDate")!=null){
				effectiveDate=	request.getParameter("effectiveDate");
			
			CommonUtil commonUtil = new CommonUtil();
			effectiveDate=commonUtil.converDBToAppFormat(effectiveDate,"yyyy-MM-dd","dd/MMM/yyyy").toLowerCase();
		
			}

			%>
					
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Pension Master</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="./PensionView/scripts/sample.css">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<script type="text/javascript"> 
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
	
	
	function getNodeValue(obj,tag)
    { 
	     if(obj.getElementsByTagName(tag)[0].firstChild){
	  return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	 }else return "";
   }
   	function limitlength(obj, length){
				var maxlength=length;
			if (obj.value.length>maxlength)
				obj.value=obj.value.substring(0, maxlength);
			}
			
	function numsDotOnly()
	         {

	            if(((event.keyCode>=48)&&(event.keyCode<=57))||(event.keyCode==46))
	            {
	               event.keyCode=event.keyCode;
	            }

	            else
	            {
					
	              event.keyCode=0;

                }

	       }
	function testSS(){
   		alert ("user doesn't have permissions to Enter the Data");
   		return false;
   		 if(document.forms[0].cpfacno.value=="")
   		 {
   		  alert("Please Enter Cpfacno");
   		  document.forms[0].cpfacno.focus();
   		  return false;
   		  }
   		 
   		 if(document.forms[0].empName.value=="")
   		 {
   		  alert("Please Enter Employee Name");
   		   document.forms[0].empName.focus();
   		  return false;
   		  }
   		if(document.forms[0].fromDt.value==""){
   			alert("Please Enter Transaction Date");
     		document.forms[0].fromDt.focus();
     		 return false;
   		}
   		  if(document.forms[0].emoluments.value=="")
   		  {
   		  alert("Please Enter Emoluments For PF deduction");
   		  document.forms[0].emoluments.focus();
   		  return false;
   		  }
   		  
   		  if(document.forms[0].pf.value=="")
   		  {
   		  alert("Please Enter PF(Statutory");
   		  document.forms[0].pf.focus();
   		  return false;
   		  }
   		  if(document.forms[0].vpf.value=="")
   		  {
   		  alert("Please Enter VPF");
   		  document.forms[0].vpf.focus();
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
   		    if(document.forms[0].airPortCode.value==""){
   		     alert("Please Enter AirportCode");
   		  	document.forms[0].airPortCode.focus();
   		 	 return false;
   		    }
   		    if(document.forms[0].airPortCode.value==""){
   		     alert("Please Enter AirportCode");
   		  	document.forms[0].airPortCode.focus();
   		 	 return false;
   		    }
   		var cpfacno= document.forms[0].cpfacno.value;
   		var pensionnumber=document.forms[0].pensionnumber.value;
   		document.forms[0].action="<%=basePath%>validatefinance?method=addFinancialDetals&pensionnumber="+pensionnumber+"&cpfacno="+cpfacno;
   		document.forms[0].method="post";
		document.forms[0].submit();
   		 }	
	
	
	function getCpfacno(obj)
    {	
		var	cpfacno="";
		var pensionnumber="";
		if(document.forms[0].pensionnumber.value=="")
		{
			cpfacno=obj;
		}
		else
		{
			 pensionnumber=obj;
		}
	createXMLHttpRequest();	
	pensionnumber=document.forms[0].pensionnumber.value;
    cpfacno=document.forms[0].cpfacno.value;
	var region=document.forms[0].region.value;
	var url ="<%=basePath%>search1?method=getCpfacnoDetails&pensionnumber="+pensionnumber+"&cpfacno="+cpfacno+"&region="+region;
    //alert(url);
	xmlHttp.open("post", url, true);
	xmlHttp.onreadystatechange = getCpfDetails;
	xmlHttp.send(null);
  }	
  
  function getCpfDetails()
  {	if(xmlHttp.readyState ==4)
	{
		 //alert("in readystate"+xmlHttp.responseText)
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		 if(stype.length==0){
		  }
		  else{
		  	var employeeName = getNodeValue(stype[0],'employeeName');
		 	 document.forms[0].empName.value=employeeName;
		 	var pensionNumber =  getNodeValue(stype[0],'pensionNumber');
		 	 document.forms[0].pensionnumber.value=pensionNumber;
		 	var cpfacno =  getNodeValue(stype[0],'cpfacno');
		 	 document.forms[0].cpfacno.value=cpfacno;
		   	var employeeNumber = getNodeValue(stype[0],'employeeNumber');
		 	 document.forms[0].employeeCode.value=employeeNumber;
		 	var airportCode = getNodeValue(stype[0],'airportcode');
		 	 document.forms[0].airPortCode.value=airportCode;
		 	var region = getNodeValue(stype[0],'region');
		 	 document.forms[0].region.value=region;
		 	
		 	 
		  }
		}
	}
 }
			
			
		function getAAIContribution(obj){	
		if(document.forms[0].emoluments.value=="")
   		 {
   		  alert("Please Enter Emoluments For PF deduction");
   		  document.forms[0].emoluments.focus();
   		  return false;
   		  }
		 var emoluments=obj;
		 var birthdate=document.forms[0].dateofbirth.value;
		var url ="<%=basePath%>validatefinance?method=getFinancialDetals&birthdate="+birthdate+"&emoluments="+emoluments;
		xmlHttp.open("get", url, true);
		xmlHttp.onreadystatechange = getAAIContributionDetails;
		xmlHttp.send(null);
	  	}
	  	
	  function getAAIContributionDetails()
       {if(xmlHttp.readyState ==4)
	    { 
		if(xmlHttp.status == 200)
		{ var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		 if(stype.length==0){
		  }
		  else{
		  	 
		 	 var aaiPf = getNodeValue(stype[0],'aaiPf');
		    document.forms[0].aaiPf.value=aaiPf;
		 	var aaiPension =  getNodeValue(stype[0],'aaiPension');
		 	 document.forms[0].aaiPension.value=aaiPension;
		   	var total = getNodeValue(stype[0],'total');
		 	 document.forms[0].aaiTotal.value=total;
		   
		  }
		}
	}
}
			
</script>
	</head>

	<body  onload="document.forms[0].pensionnumber.focus();">
		<form method="post" action="./PensionAdd.jsp">

			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">

				<tr>
					<td>
					<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>
				<tr>
					<td>


					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
						<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									Financial Detail[Add]
								</td>
								<td height="5%" colspan="2" align="center">

								</td>

							</tr>


					<tr>
								<td height="15%">
									<table align="center">
										<tr><td class="label">
												PF ID:
											</td>
											<td>
												<input type="text" name="pensionnumber" value='<%=bean.getPensionNumber()%>' onblur="getCpfacno(this.value)" onkeyup="return limitlength(this, 20)">
											</td>
                                                <input type="hidden" name="pensionnumber1" value='<%=bean1.getPensionNumber()%>' onkeyup="return limitlength(this, 20)">
											<td class="label">
												Employee No
											</td>

											<td>
												<input type="text" name="employeeCode" onkeyup="return limitlength(this, 20) value='<%=bean1.getEmpNumber()%>'">
											</td>
										</tr>
										<tr>
											<td class="label">
												Cpf Accno:<font color="red">&nbsp;*</font>
											</td>
											<td>
                                                
												 <input type="text" name="cpfacno"  value='<%=bean.getCpfAccNo()%>' onblur="getCpfacno(this.value)" onkeyup="return limitlength(this, 20)"> 
											</td>
						                     <input type="hidden" name="cpfacno1" value='<%=bean1.getCpfAcNo()%>' onkeyup="return limitlength(this, 20)">
											<td class="label">
												Employee Name:
											</td>

											<td>
												<input type="text" name="empName" value="<%=bean1.getEmpName()%>" onkeyup="return limitlength(this, 20)">

											</td>
											<input type="hidden" name="dateofbirth" value='<%=bean1.getDateofBirth()%>'>
										</tr>
										<tr>
								<td class="label">
									From Date:
								</td>
								<td>
									<input type="text" name="fromDt" value='<%=effectiveDate %>'>
									<a href="javascript:show_calendar('forms[0].fromDt');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
														<td class="label">
 						  Region:
						</td>
						<td>									
							<SELECT NAME="region" style="width:130px">
							<%
							int j=0;
							boolean exist = false;
							 while(it.hasNext()){
								 System.out.println("bean.getRegion() "+bean.getRegion());
							  region=hashmap.get(it.next()).toString();
							  j++;
							   if (bean.getRegion().equalsIgnoreCase(region)){
               					exist = true;
               					if (exist) {%>
							 <option value="<%= bean.getRegion()%>" <% out.println("selected");%>><%= bean.getRegion()%></option>
							<% }}else{%>	
							  <option value ="<%=region%>"> <%=region%></option>
	                       <% }
	                       }
							%>
							</SELECT>
						</td>
						</tr>
									<tr>
										<td class="label">
												Basic:
											</td>
											<td>
												<input type="text" name="basic" onkeypress="numsDotOnly()"  onkeyup="return limitlength(this, 20)">
											</td>

											<td class="label">
											Daily Allowance :
											</td>

											<td>
												<input type="text" name="dailyAllowance" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">

											</td>
										
										</tr>
										<tr>
										<td class="label">
											Special Basic :
											</td>

											<td>
												<input type="text" name="splBasic" onkeypress="numsDotOnly()"  onkeyup="return limitlength(this, 20)">

											</td>
											<td class="label">
											Emoluments For PF deduction:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="emoluments" onkeypress="numsDotOnly()" value='<%=bean1.getEmoluments()%>' onblur="javascript:getAAIContribution(this.value);" onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<tr>
											

											<td class="label">
												PF (Statutory):<font color="red">&nbsp;*</font>
											</td>

											<td>
												<input type="text" name="pf" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
											<td class="label">
												VPF:<font color="red">&nbsp;*</font>
											</td>

											<td>
												<input type="text" name="vpf" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
										</tr>

										<tr>
										<td class="label">
												PFADV:
											</td>
											<td>
												<input type="text" name="pfAdv" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
											
											<td class="label">
												Principal:
											</td>
											<td>
												<input type="text" name="principal" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<tr>
										<td class="label">
												Advance Drawn:
											</td>
											<td>
											<input type="text" name="advDrawn" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
											
											<td class="label">
												Part Final:
											</td>
											<td>
												<input type="text" name="partfinal" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<tr>
											<td class="label">
												Interest:
											</td>
											<td>
												<input type="text" name="interest" onkeypress="numsDotOnly()" onkeyup="return limitlength(this, 20)">
											</td>
											<td class="label">
												AAI PF:
											</td>
											<td>
												<input type="text" name="aaiPf" readonly="true" value='<%=bean1.getAaiPf()%>' onkeyup"returnlimitlength(this, 20)">

											</td>
										</tr>
										<tr>

											<td class="label">
												AAI Pension:
											</td>
											<td>
												<input type="text" name="aaiPension" readonly="true" value='<%=bean1.getAaiPension()%>' onkeyup"returnlimitlength(this, 20)">

											</td>
											<td class="label">
												AAI Total:
											</td>
											<td>
												<input type="text" name="aaiTotal" readonly="true" value='<%=bean1.getAaiTotal()%>' onkeyup"returnlimitlength(this, 20)">

											</td>

										</tr>
										<tr>

											<td class="label">
												Airport code:
											</td>
											<td>
												<input type="text" name="airPortCode" readonly="true" onkeyup"returnlimitlength(this, 20)">

											</td>
											

										</tr>
							
										<table align="center">

											<tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td align="center">
													<input type="button" class="btn" value="Add" class="btn" onclick="testSS()">
													<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
													<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
												</td>
											</tr>
											
											
										</table>
									</table>
						</table>
						</form>
	</body>
</html>
