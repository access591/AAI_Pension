<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
            
            %>
<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		
	</HEAD>
	<body>
		<table width="100%" cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td class="label" align="center"><%=request.getAttribute("message")%></td>
			</tr>
			<tr>
				<td colspan ="5" class="label" align="right"><img src="<%=basePath%>/PensionView/images/viewBack.gif" onclick="javascript:history.back(-1)"></td>
				</tr>
			
		</table>
	</body>
</html>
