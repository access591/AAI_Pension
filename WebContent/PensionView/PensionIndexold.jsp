<!--
/*
  * File       : Pensionindex.jsp
  * Date       : 11/06/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->
<%@ page language="java" %>
<%@ page errorPage="error.jsp"%>
<% response.setHeader("Cache-Control","no-cache"); %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<html>
  <head> 
	    <title>AAI</title>
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
   <meta http-equiv="expires" content="now">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
 
  </head>
  <script language="javascript">
  function testSS()
		{ 
			if(document.forms[0].username.value=="")
			{
				alert("Please Enter User Name");
				document.forms[0].username.focus();
				return false;
			}
			 if(document.forms[0].password.value=="")
			{
				alert("Please Enter Password");
				document.forms[0].password.focus();
				return false;
			}
			 else{ 
			 	document.forms[0].action="<%=basePath%>PensionLogin?method=loginpage" 
				document.forms[0].method="post";
				document.forms[0].submit();
			}
		}
  </script>


  <body onload="document.forms[0].username.focus();" class="BodyBackground">	
  		
   			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
					<TABLE align="left" bordercolor="#000066" cellSpacing=0 cellPadding=0 border="0" width="100%"> <!--  <align="left" bordercolor="#000066" cellSpacing=0 cellPadding=0 border="1" width="100%"> -->
				<TR>
					<TD width="9%" align="right">
					
					<img src="<%=basePath%>PensionView/images/header2.jpg" border="0"/>
					</TD>
				</TR>
				</TABLE>
					</td>
				</tr>
				<tr>
					<td height="70"></td>
				</tr>
				<tr><td  height="20" class="ScreenHeading"    align="center"><font color="red">
				<%
				
				if (request.getAttribute("userExist") != null){
				//out.println(request.getAttribute("userExist"));
				out.println("Invalid UserName / Password");
			    }%></font> </td>
					
				</tr>
				<%							
				if((String)session.getAttribute("expmsg")!=null && !((String)session.getAttribute("expmsg")).equals("")){
				 %>
					<tr><td  height="20" class="ScreenHeading"  colspan="2"  align="center"><font color="red"><%=session.getAttribute("expmsg")%>&nbsp;!</font></td></tr>
							 		
					  <%session.setAttribute("expmsg","");} %>
				<tr>
					<td class="ScreenHeading" colspan="2" align="center">LOGIN</td>
				</tr>
				<tr>
				<td>
				<form >
					<TABLE align="center" border="0" width="40%">
						
						<tr>
							<td height="20" colspan="2"></td>
							</tr>
											 
						<TR>
						  <TD  align="right" style="font: small-caption;">User Name </TD>
						  <TD class="label"><label>
						   <input type="text" name="username"/>
						  </label></TD>
					  </TR>
						<TR>
							<TD  align="right" width="47%" style="font: small-caption;">Password</TD>
							<TD class="label">
								<input type="password" name="password"/>
								</TD>
						</TR>
						<TR>
							<TD colspan="2" align="center" valign="bottom" height="40px">
								<input type="submit"  value="Login" onclick="testSS()"/>
								</TD>
						</TR>
					</TABLE>
			
			</TD>
		</TR>
		<tr>
			<TD height="120"></TD>
		</tr>
		<tr>
			<td class="tdfooter">&nbsp; </td>
		</tr>
	</TABLE>
	</form>
	
  </body>
</html>