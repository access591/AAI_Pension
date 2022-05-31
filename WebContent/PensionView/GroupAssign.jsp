<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.UserBean,aims.bean.SearchInfo"%>
<%
if(session.getAttribute("usertype").equals("User")){
String str=(String)session.getAttribute("userid");
%>
<script type="text/javascript"> 
    var msg="<%=str%>";    
	alert("Access Denied for '"+msg+"' user");
</script>
<jsp:include page="PensionMenu.jsp" flush="true" />
<%
}
else
{				       
												       
%>
<%

System.out.println("....................Group assign.jsp............................");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head> 
	    <title>Airports Authority of india</title>
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aimsfinancestyle.css" type="text/css">
    	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">		
		<SCRIPT  type="text/javascript" SRC="<%=basePath%>PensionView/scripts/GeneralFunctions.js"></SCRIPT>
		<script language="javascript">
          
		 function validate()
		 {
			 if(document.groupform.groupnames[document.groupform.groupnames.options.selectedIndex].value=="select")
			 {
				  alert("Please Select Group Name");
				  document.groupform.groupnames.focus();
				  return false;
			 }
			 return true;
		 }			
		function populate()
		{
			document.groupform.assign.value=document.groupform.usernames.value;
		}
		function selectedUsers(){	
		
			  var selectedIndex=document.groupform.groupnames.options.selectedIndex;			
		      var groupid=document.groupform.groupnames[selectedIndex].value;	 
			  
			  var selectedIndex=document.groupform.groupnames.options.selectedIndex;			
		      var groupname=document.groupform.groupnames[selectedIndex].text;	

			  document.forms[0].action="<%=basePath%>PensionLogin?method=assignedusers&groupid="+groupid+"&groupname="+groupname;
			  document.forms[0].method="post";
		      document.forms[0].submit();
              		  							  
       }
	   function testSS(){
		if(!validate())
			return false;		
		
			
	  for(var i=0; i< document.groupform.assign.length; i++)
	  {
		   document.groupform.assign[i].selected=true;
      }
	
		document.forms[0].action="<%=basePath%>PensionLogin?method=groupmap";
		document.forms[0].method="post";
		document.forms[0].submit();
		
   	   }  	

  </script>
  </head>
 <body>
	<form  name="groupform" method="post">
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
									<td>
										<jsp:include page="/PensionView/PensionMenu.jsp"/>
									</td>
						</tr>
						<tr>
									<td>&nbsp;&nbsp;&nbsp;</td>
						</tr>
						<tr>
									<td>
											<table width="50%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
											<tr>
												    <td height="5%" colspan="2" align="center" class="ScreenMasterHeading">Group Assignment</td>
											 </tr>
						
						                    <tr>
												<td height="15%">
													<table align="center">
											         <tr> 						
														 <td height="120" align="center" valign="top">								
															<TABLE align=center border=0 cellpadding=3 cellspacing=0 >											
																
																  <TR> 
																		 
																		 <th width="100%" class="tbb" colspan=3 align="center">Group Name
																		 
																				 <%
																				  SearchInfo searchBean = new SearchInfo();
																				  SearchInfo searchBean1 = new SearchInfo();
																				 String userId="";						      
																				  userId=(String) session.getAttribute("usertype");
																				  if(userId.equals("User"))
																				  {						  
																				   userId = (String) session.getAttribute("username");%><%=userId%>
																				  <INPUT TYPE="hidden" name="username" value="<%=userId%>">
																				  <%
																				  }else
																				  {%>
																				  <INPUT style=display:none TYPE="hidden" name="username" value="">
																				  <select name="groupnames" style="width:145px" onChange="selectedUsers()">
																				  
																				 <%

																					
																					 if(request.getAttribute("GroupList")!=null && request.getAttribute("GroupName")!=null)
																					 {
																					 searchBean = (SearchInfo) request.getAttribute("GroupList");

																					 ArrayList dataList = new ArrayList();
																					 dataList = searchBean.getSearchList();	
																					 int groupid=0;	String groupname="";

																					   for (int i = 0; i < dataList.size(); i++) {																						
																						 UserBean beans = (UserBean) dataList.get(i);
																						 groupname=beans.getGroupName();		
																						 groupid=beans.getGroupId();

																					

																					   }%>
																					   <option value="<%=request.getAttribute("GroupId")%>" selected ><%=request.getAttribute("GroupName")%></option>	
																					 <%}else{
																				    %>
																						<option value="select">[Select One]</option>															
																					<%
																						 if(request.getAttribute("GroupList")!=null){
																						
																				 																					
																							searchBean1 = (SearchInfo) request.getAttribute("GroupList");

																							ArrayList dataList = new ArrayList();
																							dataList = searchBean1.getSearchList();	
																							int groupid=0;	String groupname="";																						

																							for (int i = 0; i < dataList.size(); i++) {										
																							UserBean beans = (UserBean) dataList.get(i);								  

																					 %>  
																					 <option value="<%=beans.getGroupId()%>"><%=beans.getGroupName()%>
																					</option>

																				    <%
																					}//end for
																					}//end if
																					}//end else
																				   if(request.getAttribute("GroupList")!=null){
																				 																					
																					searchBean = (SearchInfo) request.getAttribute("GroupList");

																					 ArrayList dataList = new ArrayList();
																					 dataList = searchBean.getSearchList();	
																					 int groupid=0;	String groupname="";

																					   for (int i = 0; i < dataList.size(); i++) {																						
																						 UserBean beans = (UserBean) dataList.get(i);									

																					 try
																						{
																					 if(!(((String)request.getAttribute("GroupName")).equals(((String)beans.getGroupName())))){																				

																				  %>
																				  <option value="<%=beans.getGroupId()%>"><%=beans.getGroupName()%>
																				  </option>
																				  <%}

																						}
																						catch(Exception e){System.out.println("..........."+e);}
																				  }//end for
																					 }%>						 
																				  </select>
																				  <%}%>																		 
																				
																		</th>
																		
																  </TR>
																  <TR> 
																		  <td width="100%" colspan=3 align="center">&nbsp;</td>
																  </TR>
																  <TR> 
																			
																			<th width="30%" class="tbb" align=center> Existing Users </Th>								<td width="40%">&nbsp;</td>								
																			
																			<TD width="30%" class="tbb" align=center >&nbsp;Selected Users </TD>
																	
																 </TR>
																 <TR> 
																			<td  width="30%"> 
																			<SELECT NAME="usernames" multiple  style="height:160px" onkeypress='move(document.groupform.usernames,document.groupform.assign)' LANGUAGE=javascript onclick='return populate()'>								

																		   <%																		   

																			   if(request.getAttribute("UserList")!=null){

																				    searchBean = (SearchInfo) request.getAttribute("UserList");

																					 ArrayList dataList = new ArrayList();
																					 dataList = searchBean.getSearchList();	
																					 int groupid=0;	String groupname="";

																			         for (int i = 0; i < dataList.size(); i++) {													
																						 UserBean beans = (UserBean) dataList.get(i);								
																			
																			%>								
																			 <option  value="<%=beans.getUserId()%>"><%=beans.getUserName()%></option>							  
																			<%}
																		   }%>
																		   </SELECT></td>
																		   
																			
																			<TD width="40%"><strong><input type="button" class="btn" value=">" name="Next" 
																			 onClick="move(document.groupform.usernames,document.groupform.assign)"><br>
																			 <br>	 	  
																			
																			 <strong><input type="button" class="btn" value="<" name=" Pre"  onClick="move(document.groupform.assign,document.groupform.usernames)">
																			</TD>																		
																 
																		   <TD width="30%"> 
																			<SELECT NAME="assign" multiple style="height:160px" onkeypress="move(document.groupform.assign,document.groupform.usernames)">

																			 <%																					
																					if(request.getAttribute("selectedUsersList")!=null){
																		        	 searchBean = (SearchInfo) request.getAttribute("selectedUsersList");

																					 ArrayList dataList = new ArrayList();
																					 dataList = searchBean.getSearchList();	
																					 int groupid=0;	String groupname="";

																					   for (int i = 0; i < dataList.size(); i++) {													
																						 UserBean beans = (UserBean) dataList.get(i);				
		 
																			%>
																				  <option value="<%=beans.getUserId()%>"><%=beans.getUserName()%>
																				 </option>
																			<%}}%>						 
																				  </select>
																						</TD>
																		
																</TR>
																  <TR> 
																		  <td width="100%" colspan=3 align="center">&nbsp;</td>
																  </TR>
																
															 <TR> 
																 <TD width="30%"  align="center">&nbsp;</TD>
																<TD width="70%"  align="center" colspan=2 >
																<input type="button" class="btn" value="Submit" class="btn" onclick="testSS()">
																<input type="button" class="btn" value="Reset" onclick="javascript:document.groupform.reset()" class="btn">
																<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">	
																</TD>	
															</TR> 
															</TABLE>
													  </td>
											    </tr>
									      </table>
									</TD>
									<TD width=3>&nbsp;</TD>
								</TR>							
							</TABLE>
						</TD>
					</TR>			
			</TABLE>
</form>
</BODY>
</HTML>
<%}
%>
