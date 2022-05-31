
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
 <head>
		<title>AAI</title>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript">    			
 	
   function testSS()
           {
		   		 window.close();
		   }
    </script>
	</head>

  
  <body bgcolor=#eef2f6>
<a href="<%=basePath%>PensionView/cashbook/PensionMenu.jsp" target="mainbody" onclick="testSS();">
<font color=#000066 face="arial " size=3>
Cash Module
</a>
</font>
<br>
<a href="<%=basePath%>PensionView/PensionMenu.jsp" target="mainbody" onclick="testSS();">
<font color=#000066 face="arial" size=3>
Pension Module
</a>
</font>
</br>


</BODY>
</html>
