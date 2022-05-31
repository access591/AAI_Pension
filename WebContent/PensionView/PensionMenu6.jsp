<%@ page language="java" import="aims.bean.UserBean"
	pageEncoding="UTF-8"%>
<%@page errorPage="error.jsp"%>



<%String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("./PensionIndex.jsp");
                rd.forward(request, response);
            }
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

   
<head>

<title>AAI</title>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
    <meta http-equiv="description" content="This is my page"/>


 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/pmenubar.css" type="text/css"/>


</head>
<body>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
   <tr>
        <td height="3" align="left" valign="top" bgcolor="#000066"><img src="<%=basePath%>PensionView/images/spacer.gif" width="1" height="1"></td>
      </tr>
       <tr>
        <td align="left" valign="top"><table width="100%" height="32" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/navbg.gif"">
          <tr>
            <td align="left" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="92%" align="left" valign="middle">

				</td>
                  <td width="8%" align="right" valign="middle"><table width="60" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="left" valign="middle"><a href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img src="<%=basePath%>PensionView/images/password.gif" title="Change Password" width="16" height="16" border="0" /></a></td>
                    
                      <td align="center" valign="middle"><a href="<%=basePath%>PensionLogin?method=hrusermenu"><img src="<%=basePath%>PensionView/images/home.gif" title="Home" width="16" height="16" border="0" /></a></td>

                      <td align="right" valign="middle"><a href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img src="<%=basePath%>PensionView/images/logout.gif" title="Logout" width="14" height="16" border="0" /></a></td>
                    </tr>
                  </table></td>
                  <td align="right" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="15" height="15" /></td>
                </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
  <tr>
    <td height="100%" align="center" valign="middle" class="BodyContentMagins" >
      <table width="470" height="388" border="0" cellpadding="0" cellspacing="0" border="0">
      <tr>
     
        <td width="157" align="center" valign="top" >
        </td>
        <td width="157" align="center" valign="top"  class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <tr>
            <td align="center" valign="middle" ><a href="<%=basePath%>psearch?method=loadPerMstr"><font color="blue"> Search for PFID </font></a></td>
          </tr>
          <tr>  
          <%if (session.getAttribute("userid").equals("ANITAKHULLAR")) {%>   
            <td align="center" valign="middle" class="dots"> <a href="<%=basePath%>psearch?method=loadFreshPenOption" > <font color="blue"> Pension Option - Revision Interface</font> </a></td>
            <% }else{%>
            <td align="center" valign="middle" class="dots"> <a href="<%=basePath%>psearch?method=loadFreshPenOption" onclick="alert('Access Denied');return false;"  > <font color="blue"> Pension Option - Revision Interface</font> </a></td>
            <%}%>
          </tr>

         <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadFreshPenOptionReports" > <font color="blue"> Pension Option - Revision Interface Reports </font></a></td>
          </tr>
                   
        </table>
        </td>
        <td width="157" align="center" valign="top" >
        </td>
       
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>


