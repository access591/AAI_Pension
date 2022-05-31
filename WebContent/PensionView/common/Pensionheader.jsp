<!--
/*
  * File       : PensionCommon.jsp
  * Date       : 13/07/2009
  * Author     : AIMS 
  * Description: 
  * Copyright (2009) by the Navayuga Infotech, all rights reserved.
  */
-->
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
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
<link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css"
	type="text/css">
<body>
<table cellSpacing="0" cellPadding="0" border="0" width="100%">
	<tr>
		<td height="98" align="left" valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<table width="100%" height="63" border="0" cellpadding="0"
					cellspacing="0"
					background="<%=basePath%>PensionView/images/topbg.gif">
					<tr>
						<td width="55%" align="left" valign="top"><img
							src="<%=basePath%>PensionView/images/title.gif" width="586"
							height="63"></td>
						<td width="45%" align="right" valign="middle"><img
							src="<%=basePath%>PensionView/images/logo-epis.gif" width="99"
							height="33" hspace="20"></td>
					</tr>
				</table>
				</td>

			</tr>

		</table>
</body>
</html>
