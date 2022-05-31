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
    

    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    

  </head>
<body  leftMargin="0" topMargin="0">
	<form>
	<table cellpadding="0" cellspacing="0" width="100%"  height="100%" >
	<tbody>
		<tr>
			<td valign="top">
				<table cellpadding="0" cellspacing="0" width="100%"  height="100%" >
					<tr>
						<td  height=30>
						<jsp:include page="/PensionView/common/PensionSubHeader.jsp" />
						</td>
						
					</tr>
					<tr>
									<td height="100%" >
										<IFrame name='menu' id='menu' src="<%=basePath%>PensionView/PensionMenu.jsp" width="100%" height="100%" scrolling="auto" frameborder=0 ALLOWTRANSPARENCY=TRUE>
										</IFrame>
									</td>
									
					</tr>	
				</table>
			</td>
		</tr>
	</tbody>
	</table>
	</form>
</body>
  	
	
</html>
