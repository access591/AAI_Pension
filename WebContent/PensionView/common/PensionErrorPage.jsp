<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<link rel="StyleSheet" href="<%=basePath%>PensionView/css/styles.css" type="text/css" media="screen">
		<SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">		
 </script>
	</head>
<body class="bodybackground" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" link="white" vlink="white" alink=white >
  <form name="form1" method="post">
  <table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
  <tr> 
    <td colspan="4">
      <table width="1000" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="100%" valign="top" align=center> 
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="100">&nbsp;</td>
                <td>&nbsp;</td>
                <td width="100">&nbsp;</td>
              </tr>
	      <tr> 
                <td width="100">&nbsp;</td>
                <td valign="top"> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr height=80>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr> 
                      <td width="284" colspan=2 valign=middle><font color="red" size="6">   Sorry...! Application is not accessed due to techincal problem.Please contact EPIS support team   </font></td>
                    </tr>
					<tr> 
                      <td width="284" height="30" valign=middle ><A href="<%=basePath%>PensionLogin?method=logoff" target="_parent" /><font color=#9999FF size=4>Back</font></A></td>
                    </tr>
                  </table>
                </td>
                <td width="100">&nbsp;</td>
              </tr>
              <tr> 
                <td width="100">&nbsp;</td>
                <td>&nbsp;</td>
                <td width="100">&nbsp;</td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="4">&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>