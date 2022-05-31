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
			var ph=/^[0-9,-]+$/;
			var dates=new Date();
			var months;		
			var  monthfields=dates.getMonth();

			if(monthfields==0)	months="Jan";
			else if(monthfields==1)	months="Feb";
			else if(monthfields==2)  months="Mar";
			else if(monthfields==3)  months="Apr";
			else if(monthfields==4)  months="May";
			else if(monthfields==5)	months="Jun";
			else if(monthfields==6)  months="Jul";
			else if(monthfields==7)	months="Aug";
			else if(monthfields==8)	months="Sep";
			else if(monthfields==9)	months="Oct";
			else if(monthfields==10) months="Nov";
			else if(monthfields==11) months="Dec";
			
			
			 var ph=/^[0-9,-]+$/;
			 var form=dates.getDate()+"/"+months+"/"+dates.getYear()+":"+"08:00am";
			 
			
			 
			 if (document.forms[0].email.value!="") 
			  {				
				 if(!mail.test(document.forms[0].email.value))
				 {
			 	    alert("Invalid Email Id");
			     	document.forms[0].email.select();
				    return false;
				 }
			 }		
			
			 if(document.forms[0].phno.value!="")
			  {
				  if (!ph.test(document.forms[0].phno.value))
		          { 
					 alert("Phone Number should be in digits");
					 document.forms[0].phno.select();
			          return (false);
		          }
	          }
	          if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User")
	          {
	         var count=0;
			 for(var i=0;i<document.forms[0].region.length;i++)
			 {
			 	if(document.forms[0].region.options[i].selected)
			 	{
			 		count++;
			 	}
			 }			
			 if(count>3){
			 alert("User has to select maximum 3 Regions");
			 return false;
			 }
			 }
			 
	         if(document.forms[0].expirydate.value==""){
              alert("Please Enter ExpiryDate");			
			  return false;
             } 	  
              if(document.forms[0].expiretime.value==""){
              alert("Please Enter Expiry Time");
			  document.forms[0].expiretime.focus();
			  return false;
             }
			 
			 if(document.forms[0].expirydate.value!=""  && document.forms[0].expiretime.value!="")
			 {			 
				if(!convert_date(document.forms[0].expirydate) || !ValidateTime(document.forms[0].expiretime.value))
			 		return false;				 			 	
			 }		        	  
	         
	         var datetime=document.forms[0].expirydate.value+":"+document.forms[0].expiretime.value;
	         
	         if (compareDates(datetime,form)=="smaller")
		     {
				alert("Expiry Date should be greater than todays's date");			   
			   return false;
		     }		        		
			
			 if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User"){
			     if(document.forms[0].aaicategory1.checked==false && document.forms[0].aaicategory2.checked==false)			
				 {
				  alert("Please Select Category");				  
				  return false;
				 }
				  if(document.forms[0].region.selectedIndex<0)			
				 {
				  alert("Please Select Region");				  
				  return false;
				 }
			 }
		
			 return true;
        }
          function  selectedUserType(){
	       if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="Admin"){	        
	        document.forms[0].aaicategory1.checked=true;
	        document.forms[0].aaicategory2.checked=true;
	         document.getElementById("rg").style.display="none";
			 document.getElementById("rg1").style.display="none";		
	       }else{
	        document.forms[0].aaicategory1.checked=false;
	        document.forms[0].aaicategory2.checked=false;
	       }	     
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
		   categoryname="ALL-CATEGORIES";
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

	<%ArrayList usertypelist = new ArrayList();
			usertypelist.add("User");
			usertypelist.add("Admin");

			ArrayList aailist = new ArrayList();
			aailist.add("METRO AIRPORT");
			aailist.add("NON-METRO AIRPORT");

			ArrayList modules = new ArrayList();
			modules.add("Personal");
			modules.add("Finance");

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
								<td height="20" class="ScreenHeading" colspan="2" align="center">
									<font color="red"> <%=request.getAttribute("message")%></font>
								</td>
							</tr>
							<tr></tr>
							<%}%>

							<tr>
								<td height="15%">
									<%int userid = 0;
			String username = "", usertype = "", expdate = "", phoneno = "", email = "", module = "";
			String region = "", gridlength = "", remarks = "", aaicategory = "", expiredate = "", expiretime = "";
			String[] exparray;

			if (request.getAttribute("EditBean") != null) {

				UserBean bean1 = (UserBean) request.getAttribute("EditBean");

				userid = bean1.getUserId();
				username = bean1.getUserName();
				region = bean1.getRegion();
				usertype = bean1.getUserType();
				if (bean1.getExpiryDate() != "") {
					expdate = bean1.getExpiryDate();
					System.out.println("........Expire Date.........."
							+ expdate);
					exparray = expdate.split("@");

					expiredate = exparray[0];
					expiretime = exparray[1];
				}

				gridlength = bean1.getGridLength();
				remarks = bean1.getRemarks();
				if (request.getAttribute("aaicategory") != null) {
					aaicategory = (String) request.getAttribute("aaicategory");
				} else
					aaicategory = bean1.getAaiCategory();
				module = bean1.getPrimaryModule();
				phoneno = bean1.getPhoneNo();
				email = bean1.getEmail();

			}

			%>
									<table align="center">
										<tr>
											<td class="label">
												User Name:
											</td>
											<td>
												<input type="text" name="username" value="<%=username%>" readonly />
											</td>

										</tr>

										<tr>
											<td class="label">
												User Type
											</td>

											<td>
												<select name="usertype" style="width:130px" onChange="selectedUserType()">

													<%for (int i = 0; i < usertypelist.size(); i++) {
				boolean exist = false;
				String user = (String) usertypelist.get(i);

				if (user.equals(usertype))
					exist = true;

				if (exist) {

					%>
													<option value="<%=usertype%>" <% out.println("selected");%>>
														<%=usertype%>
													</option>
													<%} else {%>
													<option value="<%=user%>">
														<%=user%>
													</option>
													<%}
			}

			%>

												</select>
											</td>
										</tr>

										<tr>
											<td class="label">
												AAI Category <font color="red">&nbsp;*</font>
											</td>
											<td>

												<%if (!(aaicategory.equals(""))) {
				System.out.println("..aaicategory....." + aaicategory);

				if (aaicategory.equals("METRO AIRPORT")) {

					%>
												<input type="checkbox" name="aaicategory1" value="<%=aaicategory%>" checked onClick="selectedcategory();" />
												METRO AIRPORT
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT" onClick="selectedcategory();" />
												NON-METRO AIRPORT
												<%} else if (aaicategory.equals("NON-METRO AIRPORT")) {%>
												<input type="checkbox" name="aaicategory1" value="METRO AIRPORT" onClick="selectedcategory();" />
												METRO AIRPORT
												<input type="checkbox" name="aaicategory2" value="<%=aaicategory%>" checked onClick="selectedcategory();" />
												NON-METRO AIRPORT
												<%}
				if (aaicategory.equals("ALL-CATEGORIES")) {

				%>
												<input type="checkbox" name="aaicategory1" value="METRO AIRPORT" checked onClick="selectedcategory();" />
												METRO AIRPORT
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT" checked onClick="selectedcategory();" />
												NON-METRO AIRPORT
												<%}
			} else if (usertype.equals("User") || usertype.equals("")) {
				System.out.println("....in else.....");%>
												<input type="checkbox" name="aaicategory1" value="METRO AIRPORT" onClick="selectedcategory();" />
												METRO AIRPORT
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT" onClick="selectedcategory();" />
												NON-METRO AIRPORT
												<%} else {%>
												<input type="checkbox" name="aaicategory1" value="METRO AIRPORT" checked onClick="selectedcategory();" />
												METRO AIRPORT
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT" checked onClick="selectedcategory();" />
												NON-METRO AIRPORT
												<%}%>
											</td>

											<%if (request.getAttribute("selectedRegionList") != null
					|| (!region.equals(""))) {
				searchBean = (SearchInfo) request
						.getAttribute("selectedRegionList");
				if (!aaicategory.equals("")) {

					%>
										</tr>
										<%String display = "";
					if (usertype.equals("Admin")) {
						display = "display:none";
					}

					%>
										<tr style=<%=display%>>
											<td class="label" id="rg">
												Region <font color="red">&nbsp;*</font>
											</td>

											<td id="rg1">

												<select name="region" multiple style="height:130px">
													<%if (!region.equals("")) {

						ArrayList dataList = new ArrayList();
						dataList = searchBean.getSearchList();
						String regions = "", reg = "";
						for (int i = 0; i < dataList.size(); i++) {
							boolean exist = false;
							UserBean beans = (UserBean) dataList.get(i);
							regions = beans.getRegion();

							String[] regs = region.split(",");
							for (int j = 0; j < regs.length; j++) {
								reg = regs[j];

								if (regions.equals(reg)) {
									exist = true;
									break;
								}
							}
							if (exist) {

								%>
													<option value="<%=reg%>" <% out.println("selected");%>>
														<%=reg%>
													</option>
													<%} else {%>

													<option value="<%=regions%>">
														<%=regions%>
														<%}
						}
					} else {%>


														<%ArrayList dataList = new ArrayList();
						dataList = searchBean.getSearchList();
						for (int i = 0; i < dataList.size(); i++) {
							UserBean beans = (UserBean) dataList.get(i);

							%>
													<option value="<%=beans.getRegion()%>">
														<%=beans.getRegion()%>
													</option>
													<%}
					}
				}
			}%>
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
												Module
											</td>
											<td>
												<select name="module" style="width:130px">
													<%for (int i = 0; i < modules.size(); i++) {
				boolean exist = false;
				String mod = (String) modules.get(i);

				if (mod.equals(module))
					exist = true;

				if (exist) {

					%>
													<option value="<%=module%>" <% out.println("selected");%>>
														<%=module%>
													</option>
													<%} else {%>
													<option value="<%=mod%>">
														<%=mod%>
													</option>
													<%}
			}

			%>
												</select>
											</td>
										</tr>

										<tr>
											<td class="label">
												Expiry Date<font color="red">&nbsp;*</font>
											</td>
											<td>
												<%if (session.getAttribute("usertype").equals("Admin")) {

				%>
												<input type="text" name="expirydate" value="<%=expiredate%>" maxlength=11 size="8" />
												<b>-</b>
												<input type="text" name="expiretime" value="<%=expiretime%>" maxlength=7 size="5" />
												<%} else {%>
												<input type="text" name="expirydate" value="<%=expiredate%>" maxlength=11 size="8" readonly="true" />
												<b>-</b>
												<input type="text" name="expiretime" value="<%=expiretime%>" maxlength=7 size="5" />
												<%}%>


											</td>
										</tr>
										<tr>
											<td class="label">
												Grid Length
											</td>
											<td>
												<input type="text" name="gridlength" value="<%=gridlength%>" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Remarks
											</td>
											<td>
												<textarea name="remarks" rows="3" cols="27">
													<%=remarks%>
												</textarea>

											</td>

										</tr>


										<tr>
											<td>
												<input type="hidden" name="userid" value="<%=userid%>" />
											</td>
										</tr>

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
												<input type="button" class="btn" value="Update" class="btn" onclick="testSS()" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
											</td>
										</tr>

									</table>
								</td>
							</tr>


						</table>
						</form>
	</body>
</html>
