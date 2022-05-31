<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.EmpMasterBean,aims.bean.CeilingUnitBean"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

      System.out.println("------------------------back to edit.jsp-------------------------------------------");
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
		<link rel="stylesheet" type="text/css" href="./PensionView/css/sample.css">
		<LINK rel="stylesheet" href="./PensionView/css/aimsfinancestyle.css" type="text/css">
		<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		
		<script type="text/javascript"><!-- 
		
		function validate(){
		  var ex=/^[a-z,A-Z]+$/;
          if(document.forms[0].unitcode.value=="")
   		 {
   		  alert("Please Enter Unit Code");
   		  document.forms[0].unitcode.focus();
   		  return false;
   		  }

		  if (!ex.test(document.forms[0].unitcode.value))
		{
		 alert("Unit Code Should be Alphabetic ");
		 document.forms[0].unitcode.select();
		 return false;
		}

		 if(document.forms[0].unitname.value=="")
   		 {
   		  alert("Please Enter Unit Name");
   		  document.forms[0].unitname.focus();
   		  return false;
   		  }

		
		if (!ValidateName(document.forms[0].unitname.value))
	  		{
		 alert("Unit Name Should be Alphabetic ");
		 document.forms[0].unitname.focus();
		 return false;
  	    } 			
		  return true;
	 }	
	function checkunitcode(){	 
		  
		   var flag="unit";
		    var selectedIndex=document.forms[0].region.options.selectedIndex;
		   var unitRegion=document.forms[0].region[selectedIndex].text;
		 var url ="<%=basePath%>csearch?name="+document.forms[0].unitname.value+"&rate="+unitRegion+"&flag="+flag;
             
	       sendURL(url,"resp");
   }
   function sendURL(url,response)    
   {	  
	 
		 if(window.ActiveXObject)  // for IE  
				{
					 httpRequest=new ActiveXObject("Microsoft.XMLHTTP");
					
				}
				else if(XMLHttpRequest)  // for other browsers  
				{
					  httpRequest=new XMLHttpRequest();
				}
			
				if(httpRequest)
				{
            
					httpRequest.open("GET",url,true); //2nd arg is url with name/value pairs, true specifies asynchronus communication	
					httpRequest.onreadystatechange=eval(response); 
					httpRequest.send(null);
				}	 
      }

	  function resp()
      {		
          
			if (httpRequest.readyState == 4) // readyState of 4 signifies request is complete
			{ 		 		
			   if(httpRequest.status == 200)	// status of 200 signifies sucessful HTTP call
			   {  		  
				    var node = httpRequest.responseXML.getElementsByTagName("CHECK")[0];
					
				    if(node)
					{		
						var ss=httpRequest.responseXML.getElementsByTagName("STATUS")[0].firstChild.nodeValue;	
										
						
						  document.forms[0].action="<%=basePath%>csearch?method=unitUpdate";
						  document.forms[0].method="post";
						  document.forms[0].submit();
								return true;
						
					}								
				}
           }         
      }	
	 function testSS(){		
	
		if(!validate()){
		return false;
		}
		 document.forms[0].action="<%=basePath%>csearch?method=unitUpdate";
		 document.forms[0].method="post";
		 document.forms[0].submit();
	
	
	//	checkunitcode();
   	}  	
--></script>
</head>
<%
    String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	System.out.println(".............keys................"+keys);

	Iterator it = keys.iterator();
	

  %>
<body >

		<form method="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
							<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
					<tr>
		<td>
		<table width="55%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
		<tr>
			   <td height="5%" colspan="2" align="center" class="ScreenMasterHeading"> 
					 Unit Master[Edit]
				</td>
		</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td height="15%">
						<%
						String  getdate="";
						int crate=0;
						int ccd=0;

						String  unitcode="",hiddenunitcode="",unitname="",unitoption="",region="",hiddenregion="";
		
					if (request.getAttribute("EditBean") != null) {

				  CeilingUnitBean bean1 = (CeilingUnitBean) request.getAttribute("EditBean");

                 unitname=bean1.getUnitName();				
				 unitcode=bean1.getUnitCode();
				 hiddenunitcode=bean1.getUnitCode();
				 unitoption=bean1.getUnitOption();
				 region=bean1.getRegion();
				 hiddenregion=bean1.getRegion();
				%>

						<table align="center">	
				              <tr>							
							   <td class="label">
									Unit Code:
								</td>
								<td>
									<input type="text" name='unitcode'  readonly="true" value='<%=unitcode%>' >
									
								</td>
								</tr>

							  <tr>							
							   <td class="label">
									Unit Name:
								</td>
								<td>
									<input type="text" name='unitname' value='<%=unitname%>' >
									
								</td>
								</tr>
								<tr>
								<td class="label">
									Unit Option:
								</td>
								<td>
								     
								    <SELECT NAME="unitoption" style="width:130px">

									    <%
																									
										if((unitoption.trim()).equals("Airport"))
						                 {											
										%>							          
							            <option selected>Airport</option>
							            <option>RHQ</option>	
										<%}else{%>
										<option selected>RHQ</option>
							            <option>Airport</option>	
										<%}%>

							        </SELECT>							
								
								</td>
								
							<input type="hidden" name="hiddenunitcode" value='<%=hiddenunitcode%>' >
							<input type="hidden" name="hiddenregion" value='<%=hiddenregion%>' >

							</tr>	
							<tr>							
							   <td class="label">
									Region:
								</td>
								<td>
								<input type="text" readonly="true" name="region" value="<%=region%>">
							<!-- 	<SELECT NAME="region" style="width:130px">
									
									<%
									while(it.hasNext()){			
									 boolean exist = false;
									  reg=hashmap.get(it.next()).toString();  			

									  if(reg.equals(region))	
										  exist = true;  										  
									  	if (exist) {										
								   %>
								     <option value="<%=region%>" <% out.println("selected");%>><%=region%></option>								
								   <% }else{%>
								     <option value="<%=reg%>"><%=reg%></option>
								
								<%} }}%>							
										
								</SELECT>	-->								
								</td>
								

								</tr>
                   										
						   </table>					
		            	 <tr>
		            	 <td align="center">
								<table align="center">								
								<tr>
								<td>								
									<input type="submit" value="Update" onclick="testSS();" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
								</td>
							</tr>
							
							</table>
							</td>
							</tr>
							</tr>
							</table>
							</td>
							</tr>
		            	</table>
		
		</form>
	</body>
</html>
