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
String dashBoardFlag=(String)request.getAttribute("DashBoardFlag");
System.out.println("dashBoardFlagCommon"+dashBoardFlag);
request.setAttribute("DashBoardFlag",dashBoardFlag);
%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
     <link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css" type="text/css" > 
  </head>  
  		  		<frameset rows="10%,*,3%" border="2" bordercolor="red">
		<FRAME src="<%=basePath%>PensionView/common/Pensionheader.jsp" scrolling="no" noresize="noresize" frameborder="0" marginheight="0" marginwidth="0">
<%
		String jsppath="",username="";
		username=(String)request.getAttribute("usernames");
		System.out.println("username=================PensionFooter====================="+username);
		if(session.getAttribute("userdata")!=null){
			System.out.println("username=================PensionFooter====================="+((UserBean)session.getAttribute("userdata")).getUserType());
			if(((UserBean)session.getAttribute("userdata")).getUserType().equals("NODAL OFFICER")||((UserBean)session.getAttribute("userdata")).getUserType().equals("User")){
				jsppath=basePath+"PensionView/PensionMenu2.jsp";
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("Admin")){				  
				jsppath=basePath+"PensionView/PensionMenu.jsp?DashBoardFlag="+dashBoardFlag;
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("SUPER USER")){
				jsppath=basePath+"PensionView/PensionMenu1.jsp";
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("User")){
				jsppath=basePath+"PensionView/PensionMenu5.jsp";
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("HRUser")){	
				System.out.println("HRUser in commonpage");		
				jsppath=basePath+"PensionView/PensionMenu6.jsp";
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("NormalUser")){			
				jsppath=basePath+"PensionView/PensionMenu3.jsp";
			}else if(((UserBean)session.getAttribute("userdata")).getUserType().equals("Employee")){			
				jsppath=basePath+"PensionView/EmployeeMenu.jsp";
			} else{
			   jsppath=basePath+"PensionView/common/PensionErrorPage.jsp";
			}
			System.out.println(jsppath+((UserBean)session.getAttribute("userdata")).getUserType());
		}%>	
		<FRAME src="<%=jsppath%>"name="mainbody" noresize="noresize" noresize="noresize" frameborder="0" marginheight="0" marginwidth="0"></FRAME>
		<FRAME src="<%=basePath%>PensionView/common/PensionFooter.jsp?uname=<%=username%>" noresize="noresize" frameborder="0" marginheight="0" marginwidth="0" scrolling="no">
	</frameset>  
</html>
