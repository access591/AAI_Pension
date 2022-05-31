<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
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
		<link rel="stylesheet" type="text/css" href="./PensionView/scripts/sample.css">

		<SCRIPT type="text/javascript" src="./scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<script type="text/javascript">  		

		 function validate(){

		  var ex=/^[a-z,A-Z]+$/;

          if(document.forms[0].unitcode.value=="")
   		 {
   		  alert("Please Enter Unit Code");
   		  document.forms[0].unitcode.focus();
   		  return false;
   		  }

		  if (!ValidateAlphaNumeric(document.forms[0].unitcode.value))
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

		  if(document.forms[0].unitoption.selectedIndex<1)
   		 {			  
   		  alert("Please Select Unit Option");
   		  document.forms[0].unitoption.focus();
   		  return false;
   		  }

		  if(document.forms[0].region.selectedIndex<1)
   		 {
   		  alert("Please Select Region");
   		  document.forms[0].region.focus();
   		  return false;
   		  }   			
		  return true;
	 }

	   function checkwedate(){	  
		   var flag="unit";
		 	
		   var selectedIndex=document.forms[0].region.options.selectedIndex;
		   var unitRegion=document.forms[0].region[selectedIndex].text;
		
		// var url ="<%=basePath%>csearch?name="+document.forms[0].unitname.value+"&rate="+unitRegion+"&flag="+flag;
		var url ="<%=basePath%>csearch?code="+document.forms[0].unitcode.value+"&name="+document.forms[0].unitname.value+"&rate="+unitRegion+"&flag="+flag;
	       sendURL(url ,"resp");
	   }

	   function sendURL(url ,response)
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

					httpRequest.onreadystatechange=eval(getStatus); 
					httpRequest.send(null);
				}	 
      }

	  function getStatus()
      {
			//The readyState property holds the status of the server's response. Each time the readyState changes,
			  
              
			if (httpRequest.readyState == 4) // readyState of 4 signifies request is complete
			{ 				
			   if(httpRequest.status == 200)	// status of 200 signifies sucessful HTTP call
			   {  		  
				     //XML document is returned as a response.
					//XML tag name used in XML document is used to get the response			
					var node = httpRequest.responseXML.getElementsByTagName("CHECK")[0];
					

				    if(node)
					{										
						var ss=httpRequest.responseXML.getElementsByTagName("STATUS")[0].firstChild.nodeValue;	
						
						if(ss=="yes"){
							alert('Record already exist in the UnitMaster');
							document.forms[0].unitcode.focus();
							return false;
						}else{
							document.forms[0].action="<%=basePath%>csearch?method=unitadd";
							document.forms[0].method="post";
							document.forms[0].submit();
							return true;
						}
					}								
				}
           }         
      }	

	 function testSS(){
		  if(!validate()){
			return false;
		  }
		   checkwedate();
     }  
</script>
</head>
<%
    String region="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	System.out.println(".............keys................"+keys);

	Iterator it = keys.iterator();
	

  %>
<body>
		<form method="post">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
		</tr>
		<tr>
			<td>&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
		<td>
		<table width="97%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
		<tr>
			   <td height="5%" colspan="2" align="center" class="ScreenMasterHeading"> 
					 Unit Master[Add]
				</td>
		
		<tr>
			<td height="15%">
				<table align="center">
				     <tr>
						 <td class="label">
					    	<font color=red>*</font>Unit Code:
						</td>
						<td>
							<input type="text" name="unitcode">
						</td>
					</tr>	
					<tr>
						 <td class="label">
					    	<font color=red>*</font>Unit Name:
						</td>
						<td>
							<input type="text" name="unitname">
						</td>
					</tr>				   
					<tr>
						 <td class="label">
 						  Option:
						</td>
						<td>									
							<SELECT NAME="unitoption" style="width:130px">
							<option>Select</option>
							<option>Airport</option>
							<option>RHQ</option>									
							</SELECT>
						</td>
					</tr>
					<tr>
						 <td class="label">
 						  Region:
						</td>
						<td>									
							<SELECT NAME="region" style="width:130px">
							<option>Select</option>
							<%
							int i=0;
                            while(it.hasNext()){
							  region=hashmap.get(it.next()).toString();
							  i++;
							 %>
							  <option ="<%=i%>" ><%=region%></option>
	                       <% }
							%>
							</SELECT>
						</td>
					</tr>

					<tr>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>

					<tr>
					  <td colspan="2">
							<input type="button" class="btn" value="Add" class="btn" onclick="testSS()">
							<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
							<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">									
					  </td>
					</tr>	
					</table>
			</td>
		   </tr>
		   </table>
		   </td>
		   </tr>
		</table>			
	</form>
  </body>
</html>
<%}%>
