<%@ page language="java" import="java.sql.*,java.util.*"%> 
<jsp:useBean id="commonDB" scope="session" class="aims.common.DBUtils"></jsp:useBean>

<%
//if(sessionBean.getAirportCd()==null)
//{
///	response.sendRedirect("../index.jsp");
//}
%>
<%
String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%
String user="",loginUserId="";
String access="";
if (session.getAttribute("loginUserId")!=null)
	loginUserId=(String)session.getAttribute("loginUserId");
if (request.getParameter("user")!=null)
user=request.getParameter("user");

if (request.getParameter("access")!=null)
access=request.getParameter("access");

if(user.equals("")){
user=loginUserId;
}
%>
 

<%
	 
   Connection con;
   ResultSet rs;
	 ResultSet rs1;
	 ResultSet rs2;
	 ResultSet rs3;
	 ResultSet rs4;
	 ResultSet rs5;
   Statement st;
	 Statement st1;
	 Statement st2;
	 Statement st3;
	 Statement st4;
	 Statement st5;
    
  
   
   con=commonDB.getConnection();
   
   st=con.createStatement();
	 st1=con.createStatement();
	 st2=con.createStatement();
	 st3=con.createStatement();
	 st4=con.createStatement();
	 st5=con.createStatement();
   rs=st.executeQuery("SELECT USERID AS USERID,USERNAME AS USERNAME  FROM EMPLOYEE_USER WHERE USERID IS NOT NULL ORDER BY UPPER(USERID)");
    
   
 %>

<html>
<head>
<title>VRS Claims - User wise Access Rights [View]</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK rel="stylesheet" href="/view/css/aai.css"	type="text/css">
<SCRIPT type="text/javascript"	src="/view/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript"	src="/view/scripts/CommonFunctions.js"></SCRIPT>
<SCRIPT type="text/javascript"	src="/view/scripts/DateTime.js"></SCRIPT>
<SCRIPT type="text/javascript"	src="/view/scripts/EMail and Password.js"></SCRIPT>
  
<script language="javascript">
	var winHandle;
	var vWinCal;
	function submitCbo()
	{ 
 
	//alert(document.frmUserAccessView.user.value);
	 document.frmUserAccessView.action= "/PensionView/UserAccessRights.jsp";
		document.frmUserAccessView.submit();
		
	}

	function ree()
	{
		document.frmUserAccessView.reset();
	}
	
	function selectall()
	{
		if(document.frmUserAccessView.chk.checked==true)
		{
			for(var i=0; i< document.frmUserAccessView.access.length; i++)
			{
				document.frmUserAccessView.access[i].selected=true;
			}
		}
		else
		{
			for(var i=0; i< document.frmUserAccessView.access.length; i++)
			{
				document.frmUserAccessView.access[i].selected=false;
			}	
		}
	}

	function submitForm()
	{
		//TrimAll(0);
		if(document.frmUserAccessView.user.value=="")
		{
			alert("Please Select User Code(Mandatory)");
			document.frmUserAccessView.user.focus();
			return (false);
		}
		if(document.frmUserAccessView.access.value=="")
		{
			alert("Please Select Access Rights (Mandatory)");
			document.frmUserAccessView.access.focus();
			return (false);
		}
				
			return true;
 }
 
 function save(){
				 document.forms[0].action="<%=basePath%>PensionLogin?method=updateUserAccessRights";
				document.forms[0].method="post";
				document.forms[0].submit();		
 }
</SCRIPT>
</head>

<body onLoad='document.frmUserAccessView.user.focus();' bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form name=frmUserAccessView method="post"  onSubmit='return submitForm();'>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">

	<tr>
	<td>
		<jsp:include page="/PensionView/PensionMenu.jsp" />
    </td>
    <!-- <td width="68"><img src="<%=basePath%>view/images/bar3.jpg" width="68" height="32"></td>-->
  </tr>
  <tr>
  <td>&nbsp;</td>
  </tr> 
  <tr> 
    <td colspan="4">
      <table width="100" border="0"  align="center" cellspacing="0" cellpadding="0" class="tbborder">
          		<tr> 
                      <td height="5%" colspan="2" align="center"
					class="ScreenMasterHeading">User Access Rights&nbsp;&nbsp;<font
					color="red">
				 
				</font></td>
   </tr>
					
						<tr> 
							<!-- <td width="10" background="<%=basePath%>view/images/table_line_left.gif" >&nbsp;</td>-->
							<td height=140>
						<table align=center valign=middle cellpadding="5" border="0">

						<tr>
							<th class=tbb nowrap align=right><font color=red>* </font>User Code</th>
							<td>
							<% 
								out.println("<select name=user style=width:250 onChange=\"submitCbo()\" >");
								out.println("<option value=''>[Select One]</option>");
								while (rs.next())
								{	
									out.println("<option "); 
									if (rs.getString("USERID").trim().equals(user))
									out.println("selected");
									out.println("<option value="+rs.getString("USERID")+">");
									out.println(rs.getString("USERNAME"));
									out.println("</option>");
								} 
								if(rs!=null) rs.close();
								out.println("</select>");

							%>
						</td>
							
						</tr>
						
						<tr>
							<th class=tbb nowrap align=right valign=top><font color=red>* </font>Access Rights</th>
							<td><SELECT NAME=access STYLE=WIDTH:400 size=20 multiple>
									<% 
												rs4=st4.executeQuery("select AA.SCREEN_NAME,AA.ACCESS_CD,AA.ACCESS_LEVEL from PENSION_ACCESS_CODES_MT AA, EMPLOYEE_USER EU where EU.USERID='"+user+"'    order by UPPER(AA.ACCESS_CD)");
												while (rs4.next())
												{
													int i= 7 * Integer.parseInt(rs4.getString("ACCESS_LEVEL"));
													String getspace="";
													for(int j=0; j<i; j++)
													{
														getspace=getspace+"&nbsp;";
													}
												
													rs5=st5.executeQuery("select ACCESS_CD from PENSION_USER_ACCESS_MT where USERID='"+user+"' order by UPPER(ACCESS_CD)");
													while (rs5.next())
													{
														out.println("<option ");
														if (rs4.getString("ACCESS_CD").equals(rs5.getString("ACCESS_CD")))
														out.println("selected");
													}
													if(rs5!=null) rs5.close();
									
													out.println("<option value="+rs4.getString("ACCESS_CD")+">");
													out.println(getspace+rs4.getString("SCREEN_NAME"));
													out.println("</option>");
												}
												if(rs4!=null) rs4.close();
											 
											if(st!=null) st.close();
											if(st1!=null) st1.close();
											if(st2!=null) st2.close();
											if(st3!=null) st3.close();
											if(st4!=null) st4.close();
											if(st5!=null) st5.close();
											if(con!=null) con.close();
									%>
							</SELECT></td>
						</tr>
						
						
						
						<tr> 
							 <td align=left class=tbb><input type=checkbox name="chk" onclick=selectall()> <font color=brown>Select All</font></td>							  
							 <td align="right"><input type=hidden name=mode value='N'>							  
							 <input type="button" class="btn" value="Save" class="btn" onclick="save();"/>
							 <input type="button" class="btn" value="Reset" onclick="javascript:ree()" class="btn"/></td>
						</tr>
						</table>
		       </td>
                         </tr>
		    
                  
                </td>
               
              
               
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="4">&nbsp;</td>
  </tr>
</table> 
</form> 
</body>
</html>
 