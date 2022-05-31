<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.UserBean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
	    <title>Airports Authority of india</title>
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aimsfinancestyle.css" type="text/css">
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script language="javascript">


       function validate(){		   
		   if(document.forms[0].regionname.value=="")
		   {
			    alert("Please Enter Region Name");
				document.forms[0].regionname.focus();
				return false;
		   }
		   if(document.forms[0].aaicategory.value=="")
		   {
			    alert("Please Enter AAI Category");
				document.forms[0].aaicategory.focus();
				return false;
		   }
		  
		   return true;
		}	
		
	   function checkregionname(){	  
		   var flag="regionmaster";		 
		 	
		   var regionname=document.forms[0].regionname.value;
		   var aaicategory=document.forms[0].aaicategory.value;
		  
		
		   var url ="<%=basePath%>PensionLogin?method=checkregion&region="+document.forms[0].regionname.value+"&aaicategory="+document.forms[0].aaicategory.value;
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
							alert('Record already exist  with that Region Name');
							document.forms[0].regionname.focus();
							return false;
						}else{
							document.forms[0].action="<%=basePath%>PensionLogin?method=addregion";
							document.forms[0].method="post";
							document.forms[0].submit();		
							return true;
						}
					}									
				}
           }         
      }	
	  function testSS(){
		if(!validate())
			return false;					
	  checkregionname();	
	 }  	
	
  </script>
  </head>
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
		<table width="55%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
		<tr>
			   <td height="5%" colspan="2" align="center" class="ScreenMasterHeading"> 
					 Region Master[Add]
				</td>
		
		<tr>
			<td height="15%">
				<table align="center">
				<%if (request.getAttribute("groupmessage") != null) {

				           %>
							<tr>
								<td  height="20" class="ScreenHeading"  colspan="2"  align="center"><font color="red">
									<%=request.getAttribute("groupmessage")%></font>
								</td>
							</tr>
							<tr></tr>
							<%}%>

				    <TR>
						  <TD  align="right" style="font: small-caption;">Region Name </TD>
						
						  <td>
						    <INPUT type="text" name="regionname">						
						 </td>
					  </TR>
					  
					   <TR>
						  <TD  align="right" style="font: small-caption;">AAI Category</TD>
						
						  <td>
						  
						    <select name="aaicategory" style="150px">	
						    <option value="METRO AIRPORT">METRO AIRPORT</option>					
						    <option value="NON-METRO AIRPORT">NON-METRO AIRPORT</option>
						    </select>
						 </td>
					  </TR>

					  <TR>
						  <TD  align="right" style="font: small-caption;">Remarks</TD>
						
						  <td>
						    <TEXTAREA NAME="remarks" ROWS="4" COLS="27"></TEXTAREA>				
						 </td>
					  </TR>
					
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
