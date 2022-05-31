<!--
/*
  * File       : CeilingAdd.jsp
  * Date       : 12/12/2008
  * Author     : AIMS 
  * Description: 
  * Copyright (2008) by the Navayuga Infotech, all rights reserved.
  */
-->


<%@ page language="java" import="java.util.*,aims.common.*"%>

<%@ page import="aims.service.CeilingUnitService"%>


<%@ page import="aims.common.CommonUtil" %>
<jsp:useBean id="bean" class="aims.bean.CeilingUnitBean" scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	
		<%int result = 0;
			CommonUtil commonUtil=new CommonUtil();
			if (request.getParameter("wedate") != null) {
				
				bean.setWeDate(request.getParameter("wedate").toString());
			} else {
				bean.setWeDate("");
			}
			if (request.getParameter("rate") != null) {
				
				bean.setRate(Integer.parseInt(request.getParameter("rate")));
			} else {
				bean.setRate(0);
			}	
						
			CeilingUnitService cs = new CeilingUnitService();
			cs.addCeilingRecord(bean);		
	
	response.sendRedirect("./CeilingMasterSearch.jsp");
		%>

      
</html>

