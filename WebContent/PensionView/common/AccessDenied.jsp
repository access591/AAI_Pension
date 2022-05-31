 <%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
				if (session.getAttribute("usertype").equals("Admin")) {
				path ="PensionView/PensionMenu.jsp";
				} else if(session.getAttribute("usertype").equals("SUPER USER")) {
				path ="PensionView/PensionMenu1.jsp";
				}else if(session.getAttribute("usertype").equals("User")) {
				path = "PensionView/PensionMenu5.jsp";
				}else if(session.getAttribute("usertype").equals("NODAL OFFICER")) {
					path = "PensionView/PensionMenu2.jsp";
				}else{ 
				path = "PensionView/PensionMenu3.jsp";
				}
			%>
 <html>
<head>
<title>Inventory and Maintenance Management Application Package</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="css/aai.css" type="text/css">
</head>

<body class="bodybackground" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" link="white" vlink="white" alink=white >
		
   
  <table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>

	<tr>
		<td>&nbsp;</td>

	</tr>
</table>
  
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
                      <td width="284" colspan=2 valign=middle><font color="red" size="6">   Access Denied   </font></td>
                    </tr>
					<tr> 
                      <td width="284" height="30" valign=middle ><A href="<%=basePath%><%=path%>" /><font color=#9999FF size=4>Back</font></A></td>
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
</body>
</html>