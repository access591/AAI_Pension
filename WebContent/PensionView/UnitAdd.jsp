
<%@ page language="java" import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.DatabaseBean"%>
<%@ page import="aims.dao.CeilingUnitDAO"%>
<%@ page import="aims.service.CeilingUnitService"%>
<%@ page import="aims.bean.CeilingUnitBean"%>
<%@ page import="javax.servlet.RequestDispatcher" %>

<jsp:useBean id="bean" class="aims.bean.CeilingUnitBean" scope="request">
	<jsp:setProperty name="bean" property="*" />
</jsp:useBean>

<html>
         
	    <%
                System.out.println("------------------unitadd.jsp-----------------------");
		%>
		<%int result = 0;

			if (request.getParameter("unitcode") != null) {
				
				bean.setUnitCode(request.getParameter("unitcode"));
			} else {
				bean.setUnitCode("");
			}

			if (request.getParameter("unitname") != null) {
				
				bean.setUnitName(request.getParameter("unitname"));
			} else {
				bean.setUnitName("");
			}
		
			if (request.getParameter("unitoption") != null) {
				
				bean.setUnitOption(request.getParameter("unitoption"));
			} else {
				bean.setUnitOption("");
			}

			if (request.getParameter("region") != null) {
				
				bean.setRegion(request.getParameter("region"));
			} else {
				bean.setRegion("");
			}
					
			CeilingUnitService cs = new CeilingUnitService();
			cs.addUnitRecord(bean);		
	
	response.sendRedirect("./UnitMasterSearch.jsp");
		%>

      
</html>

