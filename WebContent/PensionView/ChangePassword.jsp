<!--
/*
  * File       : ChangePassword.jsp
  * Date       : 12/12/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->

<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.UserBean"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String usertype="";
if(session.getAttribute("usertype")!=null){
usertype=session.getAttribute("usertype").toString();
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
	    <title>Airports Authority of india</title>
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aimsfinancestyle.css" type="text/css">
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script language="javascript">
   function skip(){
      <% String userId1=session.getAttribute("userid").toString();%>
       if(<%=session.getAttribute("passwordChangeFlag").equals("Y")%>){
       javascript:history.back(-1);
       }else{
        document.forms[0].action="<%=basePath%>PensionLogin?method=updatePasswordFlag&userName="+'<%=userId1%>';
    	document.forms[0].method="post";
		document.forms[0].submit();
		}
   }

       function validate(){		   
		   if(document.forms[0].oldpwd.value=="")
		   {
			    alert("Please Enter Old Password");
				document.forms[0].oldpwd.focus();
				return false;
		   }
		   if(document.forms[0].newpwd.value=="")
		   {
			    alert("Please Enter New Password");
				document.forms[0].newpwd.focus();
				return false;
		   }
		      if(document.forms[0].newpwd.value.length<8 )
		   {
			    alert("Password Length should be 8 Character Minimum");
				document.forms[0].newpwd.focus();
				return false;
		   }
		   var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})");
		   if(document.forms[0].newpwd.value.match(strongRegex)){
		   
		  // alert("true");
		   
		   }else{
		   alert(" The Password must contain at least 1 lowercase, 1 uppercase alphabetical character,1 numaric character,1 special charecter and length must be 8 character or longer");
		   document.forms[0].newpwd.focus();
		   
		   return false;
		   }
		   if(document.forms[0].confirmpwd.value=="")
		   {
			    alert("Please Enter Confirm Password");
				document.forms[0].confirmpwd.focus();
				return false;
		   }
		    if((document.forms[0].newpwd.value)!=(document.forms[0].confirmpwd.value)){
				alert("New and Confirm Password should be same");
				document.forms[0].newpwd.select();
				return false;

			}
		   return true;
		}

		function checkoldpwd(){	
			if(document.forms[0].username.value!=null && document.forms[0].username.value!=""){
			 var url ="<%=basePath%>PensionLogin?method=checkoldpwd&username="+document.forms[0].username.value+"&oldpwd="+document.forms[0].oldpwd.value; 
			}			
			else{	
			  var selectedIndex=document.forms[0].usernames.options.selectedIndex;
		      var user=document.forms[0].usernames[selectedIndex].text;
		       
	         var url ="<%=basePath%>PensionLogin?method=checkoldpwd&username="+user+"&oldpwd="+document.forms[0].oldpwd.value;
			}
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
            
					httpRequest.open("POST",url,true); //2nd arg is url with name/value pairs, true specifies asynchronus communication	
					httpRequest.onreadystatechange=eval(response); 
					httpRequest.send(null);
				}	 
      }

	  function resp()
      {		
		  
          
			if (httpRequest.readyState == 4) // readyState of 4 signifies request is complete
			{ 		 		
				//alert("in readystate "+httpRequest.responseText);
			   if(httpRequest.status == 200)	// status of 200 signifies sucessful HTTP call
			   {  		  
				    var node = httpRequest.responseXML.getElementsByTagName("OLDPASSWORD")[0];
					
				    if(node)
					{		
						var state=httpRequest.responseXML.getElementsByTagName("STATUS")[0].firstChild.nodeValue;	
										
						if(state=="no"){
							alert('Enter Valid Old Password');
							document.forms[0].oldpwd.select();
							return false;
						}
						else
						{
							 if(document.forms[0].username.value!=null && document.forms[0].username.value!="")
							{
								var user=document.forms[0].username.value;
								document.forms[0].action="<%=basePath%>PensionLogin?method=changepassword&flag=changepwd&username="+user;
								document.forms[0].method="post";
								document.forms[0].submit();
							}			
							else
							{							
								var selectedIndex=document.forms[0].usernames.options.selectedIndex;
								var user=document.forms[0].usernames[selectedIndex].text;					
		  						
								document.forms[0].action="<%=basePath%>PensionLogin?method=changepassword&flag=changepwd&username="+user;
								document.forms[0].method="post";
								document.forms[0].submit();
						}						
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

			checkoldpwd();
			
		}	

  </script>
  </head>
  <body class="BodyBackground">
  	<form>
  			
   			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
				<td><%if(session.getAttribute("passwordChangeFlag").equals("N")){%>
				<td><table width="100%" height="53" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>/PensionView/images/topbg.gif">
      <tr>
        <td width="7%" align="left" valign="top"><img src="<%=basePath%>/PensionView/images/aai_logo.gif" width="55" height="48" hspace="10" /></td>
        <td width="76%" align="left" valign="middle"><img src="<%=basePath%>/PensionView/images/epis_title.gif" width="448" height="35" /></td>
        <td width="17%" align="right" valign="middle"><img src="<%=basePath%>/PensionView/images/epis_logo.gif" width="143" height="31" hspace="10" /></td>
        </tr></table>
        <%}%></td>
			
      </tr>
    </table>
				<tr>
					<td>
					<TABLE align="left" bordercolor="#000066" cellSpacing=0 cellPadding=0 border="0" width="100%"> <!--  <align="left" bordercolor="#000066" cellSpacing=0 cellPadding=0 border="1" width="100%"> -->
				<tr>
					<td>
						
					</td>
				</tr>
				</TABLE>
					</td>
				
				<tr>
					<td height="70"></td>
			
				</tr>
				<%
			     /*if(request.getAttribute("Message")!=null)
				 {
				 String message=(String)request.getAttribute("Message");*/
				 %>
				 <tr><td align="center"><font color="red"><%//out.println(message);%></font></td></tr>
				 <tr></tr>
				 <%//}%>
				
				
					
				<tr>
				<td >&nbsp;</td></tr>
				
						
					<TABLE align="center" border="0" width="40%">	
					<tr><td height="5%" colspan="2" align="center" class="ScreenHeading">Change Password</td>	</tr>				
						<tr>
							<td height="20" colspan="2"></td>
						</tr>
						<TR>
						  <TD  align="right" style="font: small-caption;">User Name </TD>
						
						  <td>
						  <% 
						      String userId="";
						       userId=(String) session.getAttribute("userid");%>
						
						  <INPUT TYPE="text" name="username" value="<%=userId%>" readonly="true">
						 
						 
						   
						 </td>
					  </TR>
						<TR>
							<TD  align="right" width="47%" style="font: small-caption;">Old Password</TD>
							<TD>
								<input type="password" name="oldpwd"/>
							</TD>
						</TR>
						<TR>
							<TD  align="right" width="47%" style="font: small-caption;">New Password</TD>
							<TD>
								<input type="password" name="newpwd"/>
							</TD>
						</TR>
						<TR>
							<TD  align="right" width="47%" style="font: small-caption;">Confirm Password</TD>
							<TD>
								<input type="password" name="confirmpwd"/>

								</TD>
						</TR>
						<TR>
							<TD colspan="2" align="center" valign="bottom" height="40px">
								<input type="button" class="btn"  value="Update"  onClick="testSS()">&nbsp;&nbsp;&nbsp;&nbsp;						
								<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" >&nbsp;&nbsp;&nbsp;&nbsp;
								<% if(!usertype.equals("Employee")){ %>
								<input type="button" class="btn" value="Skip" onclick="skip()" >
								<%} %>
								</TD>
						</TR>
					</TABLE>
		
			
	
		<tr>
			
		</tr>
		<tr>
			<td class="tdfooter">&nbsp; </td>
		</tr>
	</TABLE>
		</form>
	
  </body>
</html>
