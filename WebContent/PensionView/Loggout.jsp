
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<title>:: AAI - Employees Pension Information System ::</title>
<style type="text/css">
<!--
body {
	margin: 0px;
	padding: 0px;
}
.bg {
	background-image: url(images/bg.jpg);
	background-repeat: no-repeat;
}
.text {
	font: normal 12px Arial, Helvetica, sans-serif;
	color: #E4E4E4;
}
A.link {
	font: bold 12px Arial, Helvetica, sans-serif;
	color:#FFFFFF;
	text-decoration:none;
}
A.link:hover {
	color: #FFCC00;
	font: bold 12px Arial, Helvetica, sans-serif;
	text-decoration:none;
}
.aims {


	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 18px;
	font-weight: bold;
	font-style: normal;
	color: #000000;
}
-->
</style>

</head>

<body background="<%=basePath%>/PensionView/images/bg.jpg">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="top"><table width="100%" height="53" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>/PensionView/images/topbg.gif">
      <tr>
        <td width="7%" align="left" valign="top"><img src="<%=basePath%>/PensionView/images/aai_logo.gif" width="55" height="48" hspace="10" /></td>
        <td width="76%" align="left" valign="middle"><img src="<%=basePath%>/PensionView/images/epis_title.gif" width="497" height="35" /></td>
        <td width="17%" align="right" valign="middle"><img src="<%=basePath%>/PensionView/images/epis_logo.gif" width="105" height="31" hspace="10" /></td>
      </tr>
    </table></td>
  </tr>
  
  <tr>
    <td align="left" valign="top">
         <table width="100%" height="510" border="0" cellpadding="0" cellspacing="0" class="bg">

         	<tr>
         	   <td>
         		<table width="100%" cellpadding="0" cellspacing="0">
         			<tr>
                     	<td colspan="4" align="center" class="aims">
                     		Logged Out Succesfully !!!
                     	</td>
                      </tr>
                         <tr>
                         
                     	<td align="center">
                     			<a href="<%=basePath%>">Re Login<% if(session!=null){ session.invalidate();}%></a> &nbsp;&nbsp;&nbsp;
                     	
                     			<a href="#" onclick="javascript: self.close ()">Close Window</a>
                     	</td>
                     
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
