<!--
/*
  * File       : PensionCommon.jsp
  * Date       : 13/07/2009
  * Author     : AIMS 
  * Description: 
  * Copyright (2009) by the Navayuga Infotech, all rights reserved.
  */
-->
<%@ page language="java" import="java.util.*,aims.bean.UserBean" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>AAI</title>
     <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="pragma" content="no-cache">
   
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
     <link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css" type="text/css" > 
  </head>
  <body>
  <form>
  <table width="100%" border="0"   class="footer">
   	<tr>

   	
   	<td align="left" valign="top"><font color="white" size="1" face="Arial">User:<%=((String)request.getParameter("uname"))%></font></td>  
    <td align="left" valign="top"><img src="<%=basePath%>PensionView/images/divider.gif"/></td>
	<td align="left" valign="top"><font color="white" size="1" face="Arial">LoginTime: <%=((UserBean)session.getAttribute("userdata")).getLoginTime()%></font></td>
   	<td align="right" valign="top"><font color="white" size="1" face="Arial">@ Copyright 2009, All rights reserved</font></td>
   	<tr>
   </table>
   </form>
    </body>
</html>
