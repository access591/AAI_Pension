<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.UserBean,aims.bean.SearchInfo"%>
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
         
			var mail=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			 if (document.forms[0].email.value!="") 
			  {				
				 if(!mail.test(document.forms[0].email.value))
				 {
			 	    alert("Invalid Email Id");
			     	document.forms[0].email.select();
				    return false;
				 }
			 }			
			 return true;
        }
      
	    function selectedcategory(){	
	    
	       var categoryname="";
	       
	        if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User" && document.forms[0].aaicategory1.checked && document.forms[0].aaicategory2.checked ){
	
			  document.forms[0].aaicategory1.checked=false;
	          document.forms[0].aaicategory2.checked=false;	
	          alert("User has to select only one Category");	         
	          document.getElementById("rg").style.display="none";
			  document.getElementById("rg1").style.display="none";
			  
			  return false;	
			  
			  }
			  
			  
			  
			   
		 if(document.forms[0].aaicategory1.checked)
		   { 
		    var category1=document.forms[0].aaicategory1.value;
		   }else{
		   category1="not1";
		   }
		  				  
		   if(document.forms[0].aaicategory2.checked)
		   { 
		   var category2=document.forms[0].aaicategory2.value;
		   }else{
		   category2="not2";
		   }		 
		   
		   if(category1=="not1"){	
		   categoryname=document.forms[0].aaicategory2.value;
		   }else if(category2=="not2"){	
		   categoryname=document.forms[0].aaicategory1.value;
		   }else{
		   categoryname="all";
		   }
		   
		    if(category1=="not1"){		
		      if(category2=="not2"){
		        categoryname="";
		      }else{
		     categoryname=document.forms[0].aaicategory2.value;
		     }
		   }else if(category2=="not2"){	
		    if(category1=="not1"){
		        categoryname="";
		      }else{	   
		   categoryname=document.forms[0].aaicategory1.value;
		   }
		   }else{
		   categoryname="ALL-CATEGORIES";
		   }		
		   
		   
		   var flag="newuseredit";
		  
           document.forms[0].action="<%=basePath%>PensionLogin?method=getregion&flag="+flag+"&&categoryname="+categoryname;
		   document.forms[0].method="post";
		   document.forms[0].submit();            		  							  
       }	
       function testSS(){
       
        if(!validate())
			return false;	
					
		document.forms[0].action="<%=basePath%>PensionLogin?method=NewUserUpdate";		
		document.forms[0].method="post";
		document.forms[0].submit();			
   	   }  		 
   
</script>
	</head>
	
	<%
            ArrayList usertypelist=new ArrayList();
			usertypelist.add("User");
			usertypelist.add("Admin");
			
		    ArrayList aailist=new ArrayList();
			aailist.add("METRO AIRPORT");
			aailist.add("NON-METRO AIRPORT");	
			
			SearchInfo searchBean = new SearchInfo();
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
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
									User[Edit]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<%if (request.getAttribute("message") != null) {

				%>
							<tr>
								<td  height="20" class="ScreenHeading"  colspan="2"  align="center"><font color="red">
									<%=request.getAttribute("message")%></font>
								</td>
							</tr>
							<tr></tr>
							<%}%>

							<tr>
								<td height="15%">
								  <%
						String  getdate="";
						int crate=0;
						int userid=0;
						String  username="",usertype="",expdate="",phoneno="",email="";;
						String region="",gridlength="",remarks="",aaicategory="";
		
					if (request.getAttribute("EditBean") != null) {						

				    UserBean bean1 = (UserBean) request.getAttribute("EditBean");

                         userid=bean1.getUserId();
					     username=bean1.getUserName();		
						 region=bean1.getRegion();		
						 usertype=bean1.getUserType();
						 expdate=bean1.getExpiryDate();						 
						 gridlength=bean1.getGridLength();
						 remarks=bean1.getRemarks();	
					 	if (request.getAttribute("aaicategory") != null) {
						 aaicategory=(String)request.getAttribute("aaicategory");
						 }else
						 aaicategory=bean1.getAaiCategory();
					 	 phoneno=bean1.getPhoneNo();	
						 email=bean1.getEmail();
						 
					    
					}				
				%>
									<table align="center">										
										<tr>
											<td class="label">
												User Name:
											</td>
											<td>
												<input type="text" name="username" value="<%=username%>" readonly>
											</td>										
											
										</tr>					
									
										<tr>
											<td class="label">
												User Type
											</td>
											<td>
											     <SELECT NAME="usertype" style="width:130px">	
												
													<%
														for (int i = 0; i < usertypelist.size(); i++) {
															 boolean exist = false;
															 String user=(String)usertypelist.get(i);														

															if(user.equals(usertype))
															  exist = true;  

															if (exist) {																									
													%>													
													  <option value="<%=usertype%>" <% out.println("selected");%>><%=usertype%></option>				
								                    <% }else{%>
																<option value="<%=user%>"><%=user%></option>
													<%	
														}
													  }			                                     
													%>
												
												</SELECT>										
											</td>
											
											
										</tr>
										<tr>
											<td class="label">
											     AAI Category <font color="red">&nbsp;*</font>											    
											</td>
											<td>
											    
											    <%  							       
											   
											    if(!(aaicategory.equals(""))){		
											    System.out.println("..aaicategory....."+aaicategory);
											    
											    if(aaicategory.equals("METRO AIRPORT")){
											    %>											    
											    <input type="checkbox" name="aaicategory1" value="<%=aaicategory%>"  checked onClick="selectedcategory();">METRO AIRPORT												
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT"   onClick="selectedcategory();">NON-METRO AIRPORT
												<%}else if(aaicategory.equals("NON-METRO AIRPORT")){%>
												<input type="checkbox" name="aaicategory2" value="<%=aaicategory%>"  checked onClick="selectedcategory();">NON-METRO AIRPORT
												<input type="checkbox" name="aaicategory1" value="METRO AIRPORT"   onClick="selectedcategory();">METRO AIRPORT												
											    <%}%>
													<%		
													 if(aaicategory.equals("all")){
													  System.out.println("..all....."+aaicategory);
													  %>			
													    <input type="checkbox" name="aaicategory1" value="METRO AIRPORT"  checked onClick="selectedcategory();">METRO AIRPORT												
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT"   checked  onClick="selectedcategory();">NON-METRO AIRPORT
											    <%}}%>
											    
											</td>
																				
										 <%																					
												 if(request.getAttribute("selectedRegionList")!=null || (!region.equals(""))){
										      	 searchBean = (SearchInfo) request.getAttribute("selectedRegionList");
										       	 if(!aaicategory.equals("")){
										  %>
										  </tr>
										  <%
										  String display="";
										  if(usertype.equals("Admin")){
										  display="display:none";
										  }
										  %>
										  <tr style=<%=display%>>										  
										  <td class="label" id="rg" >
										       Region <font color="red">&nbsp;*</font>
										 </td>
										 
										 <td id="rg1">
										   <SELECT NAME="region"  multiple style="height:160px">
										   <option value="">[Select One]</option>
										   									    
										  <%        if(!region.equals("")){
										  
										         ArrayList dataList = new ArrayList();
												 dataList = searchBean.getSearchList();	
												 int groupid=0;	String regions="",reg="";
	    										   for (int i = 0; i < dataList.size(); i++) {	
	    										
	    										             boolean exist = false;												
															 UserBean beans = (UserBean) dataList.get(i);															 
															 regions=beans.getRegion();
															 System.out.println(".....Outer Loop......."+regions);
															 
															 String [] regs=region.split(",");	
															 for(int j=0;j<regs.length;j++){
															  reg=regs[j];															 
															  System.out.println(".........Inner Loop.........."+reg);
															 if(regions.equals(reg)){
															  exist = true;
															  break;
															  }
															  }  

															if (exist) {
															System.out.println("......In if......");	
													 
										  %>
										   <option value="<%=reg%>" <% out.println("selected");%>><%=reg%></option>
										   <%}else{
										   System.out.println("......In else......");
										   %>	
										     	
										       <option value="<%=regions%>"><%=regions%>
										 <%}
										 } }else{%>          
												
										        
										<%		 ArrayList dataList = new ArrayList();
												 dataList = searchBean.getSearchList();	
												 int groupid=0;	
	    										   for (int i = 0; i < dataList.size(); i++) {													
															 UserBean beans = (UserBean) dataList.get(i);				
										 %>		
																		
											  <option value="<%=beans.getRegion()%>"><%=beans.getRegion()%>
											 </option>
											<%}}}}%>						 
										</select>
												
										</td>
										</tr>
										<tr>
											<td class="label">
												Phone Number
											</td>
											<td>
												<input type="text" name="phno" maxlength=13 value=<%=phoneno%>>												
											</td>
											</tr>
											<tr>
										
											<td class="label">
												Email Id 
											</td>
											<td>
												<input type="text" name="email" value=<%=email%>>
											</td>
										</tr>	
										<tr>
										<td class="label">
												Remarks 
											</td>
											<td>												
												<TEXTAREA NAME="remarks" ROWS="3" COLS="27"><%=remarks%></TEXTAREA>
											</td>
										</tr>	
										<tr>
											<td class="label">
												Expiry Date<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="expirydate" value="<%=expdate%>" readonly> 
											</td>
											</tr>
											<tr>
											<td class="label">
												Grid Length
											</td>
											<td>
												<input type="text" name="gridlength" value="<%=gridlength%>">
											</td>											
										</tr>
										

										<tr><td><INPUT TYPE="hidden" name="userid" value="<%=userid%>"></td></tr>
	
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
