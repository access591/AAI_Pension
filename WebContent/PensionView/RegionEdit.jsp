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
				
        function testSS(){		
		document.forms[0].action="<%=basePath%>PensionLogin?method=RegionUpdate";		
		document.forms[0].method="post";
		document.forms[0].submit();			
   	   }  		
</script>
	</head>
	<%
	        ArrayList aailist=new ArrayList();
			aailist.add("METRO AIRPORT");
			aailist.add("NON-METRO AIRPORT");	
	%>
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
								  Region[Edit]
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
						int regionid=0;

						String  regionname="",remarks="",aaicategory="";
		
					if (request.getAttribute("EditBean") != null) {						

				    UserBean bean1 = (UserBean) request.getAttribute("EditBean");

                         regionid=bean1.getRegionId();					
					     regionname=bean1.getRegion();						 					
						 aaicategory=bean1.getAaiCategory();
						 remarks=bean1.getRemarks();								  
						
					}				
				%>
									<table align="center">										
										<tr>
											<td class="label">
												Region Name:
											</td>
											<td>
												<input type="text" name="regionname" value="<%=regionname%>" >
											</td>
										</tr>
										<tr>
											<td class="label">
												AAI Category:
											</td>
											<td>
												
												 <select name="aaicategory" style="width:130px">
										      <%
														for (int i = 0; i < aailist.size(); i++) {
															 boolean exist = false;
															 String mod=(String)aailist.get(i);														

															if(mod.equals(aaicategory))
															  exist = true;  

															if (exist) {																									
													%>													
													  <option value="<%=aaicategory%>" <% out.println("selected");%>><%=aaicategory%></option>				
								                    <% }else{%>
																<option value="<%=mod%>"><%=mod%></option>
													<%	
														}
													  }			                                     
													%>
										      </select>
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
																					

										<tr><td><INPUT TYPE="hidden" name="regionid" value="<%=regionid%>"></td></tr>
	
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
