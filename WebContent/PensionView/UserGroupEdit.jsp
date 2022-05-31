<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.UserBean"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="./PensionView/scripts/sample.css">
		<SCRIPT type="text/javascript" src="./scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime.js"></SCRIPT>
		<script type="text/javascript">  		

		 function validate(){

			  if(document.forms[0].selectgroup.checked)
		   {
				 document.forms[0].selectgroup.value='Y';				
				//alert( document.forms[0].selectgroup.value);				
		   }
		   else if(!document.forms[0].selectgroup.checked)
		   {
			    document.forms[0].selectgroup.value='N';				
				//alert( document.forms[0].selectgroup.value);	

		   }		 
		  return true;
	    }
		
         function testSS(){
		
		if(!validate()){
			return false;
		}
		
		document.forms[0].action="<%=basePath%>PensionLogin?method=GroupUserUpdate";		
		document.forms[0].method="post";
		document.forms[0].submit();		
			
   	   }  	
	
</script>
	</head>
	<body class="BodyBackground">
		<form>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>

				<tr>
					<td>
						&nbsp;
					</td>
				</tr>

				<tr>
					<td>
						<table width="45%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
								  Group[Edit]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>						

							<tr>
								<td height="15%">
								  <%
						String  getdate="";
						int crate=0;
						int groupid=0;

						String  groupname="",remarks="",flagval="";
		
					if (request.getAttribute("EditBean") != null) {						

				    UserBean bean1 = (UserBean) request.getAttribute("EditBean");

                         groupid=bean1.getGroupId();					
					     groupname=bean1.getGroupName();						
						 remarks=bean1.getRemarks();						
						 flagval=bean1.getSelectGroup();							  
						
					}				
				%>
									<table align="center">										
										<tr>
											<td class="label">
												Group Name:
											</td>
											<td>
												<input type="text" name="groupname" value="<%=groupname%>" >
											</td>
										</tr>
										<tr>
											<td class="label">
												Remarks:
											</td>
											<td>												
												<input type="text" name="remarks" value="<%=remarks%>" >
											</td>
										</tr>
										<tr>
											<td class="label">
												Group Enable/Disable
											</td>
											<td>
											<%
												if(flagval.equals("Y")){
											%>
											    <INPUT TYPE="checkbox" NAME="selectgroup" checked>	
												<%}else{%>
												 <INPUT TYPE="checkbox" NAME="selectgroup" >	
											<%}%>
											</td>
										</tr>													

										<tr><td><INPUT TYPE="hidden" name="groupid" value="<%=groupid%>"></td></tr>
	
										<tr>
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
										<tr>
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td align="center">
												<input type="button" class="btn" value="Update" class="btn" onclick="testSS()">
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
											</td>
										</tr>

									</table>
								</td>
							</tr>


						</table>
						</form>
	</body>
</html>
