<!--
/*
  * File       :PensionAddSuccess.jsp
  * Date       : 12/12/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->

<%@ page language="java" import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean"%>
<%@ page import="aims.dao.PensionDAO"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>



<jsp:useBean id="bean" class="aims.bean.DatabaseBean" scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<%String firstNM = "", lastNM = "", middleNM = "", projectNM = "", manNM = "";
			int result = 0;
			if (request.getParameter("empName") != null) {
				out.println(request.getParameter("empName"));
				bean.setEmpName(request.getParameter("empName").toString());
			} else {
				bean.setEmpName("");
			}
			if (request.getParameter("airPortCD") != null) {
				out.println(request.getParameter("airPortCD"));
				bean.setAirPortCD(request.getParameter("airPortCD").toString());
			} else {
				bean.setAirPortCD("");
			}
			if (request.getParameter("salary") != null) {
				out.println(request.getParameter("salary"));
				bean.setSalary(request.getParameter("salary").toString());
			} else {
				bean.setSalary("");
			}
			if (request.getParameter("pensionFund") != null) {
				out.println(request.getParameter("pensionFund"));
				bean.setPensionFund(request.getParameter("pensionFund")
						.toString());
			} else {
				bean.setPensionFund("");
			}
			if (request.getParameter("fromDt") != null) {
				out.println(request.getParameter("fromDt"));

				bean.setFromDt(request.getParameter("fromDt").toString());
			} else {
				bean.setFromDt("");
			}
			if (request.getParameter("toDt") != null) {
				out.println(request.getParameter("toDt"));

				bean.setToDt(request.getParameter("toDt").toString());
			} else {
				bean.setToDt("");
			}
		aims.dao.PensionDAO dao=new aims.dao.PensionDAO();
			result=dao.insertData(bean);

			%>




		<title>AddSuccess</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
	</head>

	<body>
		<FONT color="red"><%=result%> is Added successfully</FONT>
		<A href="<%=basePath%>PensionView/PensionFundSearch.jsp">Back</A>
	</body>
</html>
