<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.UserBean"%>
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
		   if(document.forms[0].groupname.value=="")
		   {
			    alert("Please Enter Group Name");
				document.forms[0].groupname.focus();
				return false;
		   }
		  
		   return true;
		}	
	  function testSS(){
		if(!validate())
			return false;				 
		
		document.forms[0].action="<%=basePath%>PensionLogin?method=usergroup";
		document.forms[0].method="post";
		document.forms[0].submit();
		
   	 }  	
	 function groupAssign()
	 {
		 document.forms[0].action="<%=basePath%>PensionLogin?method=groupassign";
		 document.forms[0].method="post";
		 document.forms[0].submit();
	 }
	 function testS(){
		if(!validate())
			return false;				 
		
		document.forms[0].action="<%=basePath%>PensionLogin?method=usergroup&asssign=group";
		document.forms[0].method="post";
		document.forms[0].submit();
		
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
					 Group Master[Add]
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
						  <TD  align="right" style="font: small-caption;">Group Name </TD>
						
						  <td>
						    <INPUT type="text" name="groupname">						
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
							<input type="button" class="btn" value="Add & Continue" onclick="testS()" class="btn">								
							<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
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