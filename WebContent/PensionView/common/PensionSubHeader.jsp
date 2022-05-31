<%@ page language="java" import="java.util.*,java.lang.*"
	pageEncoding="UTF-8"%>
<%@page errorPage="error.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<jsp:useBean id="bean" class="aims.service.PensionService"
	scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>

<%String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("./PensionIndex.jsp");
                rd.forward(request, response);
            }%>

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

</head>
<body >
<table width="100%" cellpadding="0" cellspacing="0" >
  <tr>
        <td align="left" valign="top"><table width="100%" height="32" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/menubg.gif">
	
			<tr>

			<td width="100%" align="right" valign="middle">
				<table width="84" height="26" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img
							src="<%=basePath%>PensionView/images/icon-password.gif"
							alt="Change Password" width="18" height="18" border="0" /></a></td>
						<%if(session.getAttribute("usertype").equals("Admin")){%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=mainbody"><img
							src="<%=basePath%>PensionView/images/icon-home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else if(session.getAttribute("usertype").equals("SUPER USER")) {%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=superuser"><img
							src="<%=basePath%>PensionView/images/icon-home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else if(session.getAttribute("usertype").equals("User")) {%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=home"><img
							src="<%=basePath%>PensionView/images/icon-home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}else{%><td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionView/PensionMenu3.jsp"><img
							src="<%=basePath%>PensionView/images/icon-home.gif" alt="Home"
							width="18" height="18" border="0" /></a></td>
						<%}%>
						<td width="24" align="center" valign="middle"><a
							href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img
							src="<%=basePath%>PensionView/images/icon-logout.gif" alt="Logout"
							width="16" height="18" border="0" /></a></td>
					
					</tr>
				</table>
				</td>
			

			</tr>
		</table>
		</td>

	</tr>

</table>



</body>
</html>


