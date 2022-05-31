<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
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
			var dates=new Date();
			var months;		
			var  monthfields=dates.getMonth();
			var arr = new Array(); 

			if(monthfields==0)	months="Jan";
			if(monthfields==1)	months="Feb";
			if(monthfields==2)  months="Mar";
			if(monthfields==3)  months="Apr";
			if(monthfields==4)  months="May";
			if(monthfields==5)	months="Jun";
			if(monthfields==6)  months="Jul";
			if(monthfields==7)	months="Aug";
			if(monthfields==8)	months="Sep";
			if(monthfields==9)	months="Oct";
			if(monthfields==10) months="Nov";
			if(monthfields==11) months="Dec";
			
			
			 var ph=/^[0-9,-]+$/;
			 var form=dates.getDate()+"/"+months+"/"+dates.getYear()+":"+"08:00am";
        
			 if(document.forms[0].username.value=="")
			 {
			  alert("Please Enter User Name");
			  document.forms[0].username.focus();
			  return false;
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
	         
			  if (document.forms[0].email.value!="") 
			  {				
				 if(!mail.test(document.forms[0].email.value))
				 {
			 	    alert("Invalid Email Id");
			     	document.forms[0].email.select();
				    return false;
				 }
			 }	
			 
			 if(document.forms[0].usertype.value=="")
			 {
			  alert("Please Select User Type");			 
			  return false;
			 }	
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
			   
             if(document.forms[0].expirydate.value==""){
              alert("Please Enter ExpiryDate");
			  document.forms[0].expirydate.focus();
			  return false;
             }
             if(document.forms[0].expiretime.value==""){
              alert("Please Enter Expiry Time");
			  document.forms[0].expiretime.focus();
			  return false;
             }
              
             if(document.forms[0].expirydate.value!="" && document.forms[0].expiretime.value!="")
			 {											
				if(!convert_date(document.forms[0].expirydate) || !ValidateTime(document.forms[0].expiretime.value))
			 		return false;			 			 	
			 }
			 var datetime=document.forms[0].expirydate.value+":"+document.forms[0].expiretime.value;
			 
			 if (compareDates(datetime,form)=="smaller")
		     {
				alert("Expiry Date should be greater than todays's date");
			   document.forms[0].expirydate.focus();
			   return false;
		     }		
		    
		     if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User"){
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
        function testSS(){
        
		if(!validate())
			return false;				 
		
		document.forms[0].action="<%=basePath%>PensionLogin?method=newuser";
		document.forms[0].method="post";
		document.forms[0].submit();
		
   	   }  	
	   function getDate(){
			 var date=new Date();
			 var month;		

			 var  monthfield=date.getMonth();

			if(monthfield==0)  month="Jan";
			if(monthfield==1)  month="Feb";
			if(monthfield==2)  month="Mar";
			if(monthfield==3)  month="Apr";
			if(monthfield==4)  month="May";
			if(monthfield==5)  month="Jun";
			if(monthfield==6)  month="Jul";
			if(monthfield==7)  month="Aug";
			if(monthfield==8)  month="Sep";
			if(monthfield==9)  month="Oct";
			if(monthfield==10) month="Nov";
			if(monthfield==11) month="Dec";
			
			 var format=date.getDate()+"/"+month+"/"+(date.getYear()+1);
			 var timeformat="05:00pm";
			
			document.forms[0].expirydate.value=format;
			document.forms[0].expiretime.value=timeformat;
			document.forms[0].gridlength.value="10";
	   }

	  function selectedcategory(){	
	         var categoryname="";
	  
	         if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="[Select One]"){
				  alert("Please Select User Type");
				  
				 if(document.forms[0].aaicategory1.checked)
			     {
			        document.forms[0].aaicategory1.checked=false;
			     }
			     if(document.forms[0].aaicategory2.checked)
			     {
			     document.forms[0].aaicategory2.checked=false;
			     }			     
				  return false;
			 }
			 
			 if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User" && document.forms[0].aaicategory1.checked && document.forms[0].aaicategory2.checked ){
	
			  document.forms[0].aaicategory1.checked=false;
	          document.forms[0].aaicategory2.checked=false;	
	          alert("User has to select only one Category");	         
	          document.getElementById("rg").style.display="none";
			  document.getElementById("rg1").style.display="none";			  
			  return false;				  
			  }	  
	         if(document.forms[0].usertype[document.forms[0].usertype.options.selectedIndex].text=="User"){	        
				document.getElementById("rg").style.display="block";
				document.getElementById("rg1").style.display="block";
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
		   
		   var url ="<%=basePath%>PensionLogin?method=getregions&categoryname="+categoryname;	
	       sendURL(url ,"resp");		  
       }
       function sendURL(url ,response){	  
	         
				 if(window.ActiveXObject)  // for IE  
				{
					 httpRequest=new ActiveXObject("Microsoft.XMLHTTP");
					
				}
				else if(XMLHttpRequest)  // for other browsers  
				{
					  httpRequest=new XMLHttpRequest();
				}
			
				if(httpRequest)
				{
                   
					httpRequest.open("POST",url,true); //2nd arg is url with name/value pairs, true specifies asynchronus communication	

					httpRequest.onreadystatechange=eval(response); 
					httpRequest.send(null);
				}	 
    }
     function resp(){
		              
			if (httpRequest.readyState == 4) 
			{ 					   
			   if(httpRequest.status == 200)	
			   { 				 			   
				   if(httpRequest.responseXML.getElementsByTagName("RegionDetails")[0]!=null){
					
					var node = httpRequest.responseXML.getElementsByTagName("RegionDetails")[0];					
					document.forms[0].region.options.length=0;
				    var i;
                 
					for(i=0;i<parseInt(node.childNodes.length);i++) 
			       {
					var node1 = node.childNodes[i];					
					
					document.forms[0].region.options[document.forms[0].region.options.length]=new Option( node1.getElementsByTagName("RegionName")[0].childNodes[0].nodeValue,node1.getElementsByTagName("RegionName")[0].childNodes[0].nodeValue);					
					
			       }
			   }
	       }
	      }
  }
 
</script>
	</head>
	<body class="BodyBackground" onLoad="getDate();">
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
									 User[Add]
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<%if (request.getAttribute("message") != null) {%>
							<tr>
								<td  height="20" class="ScreenHeading"  colspan="2"  align="center"><font color="red">
									<%=request.getAttribute("message")%></font>
								</td>
							</tr>
							<tr></tr>
							<%}%>
							<tr>
								<td height="15%">
									<table align="center">										
										<tr>
											<td class="label">
												User Name:<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="username"/>
											</td>
											</tr>
										<tr>	
											<td class="label">
												User Type<font color="red">&nbsp;*</font>
											</td>
											<td>
												<select name="usertype" style="width:130px" onChange="selectedUserType()">
													<option value="">[Select One]</option>
													<option value="User">User</option>
													<option value="Admin">Admin</option>
												</select>
											</td>
										</tr>	
										<tr>
											<td class="label">
												AAI Category
											</td>
											<td>
											    <input type="checkbox" name="aaicategory1" value="METRO AIRPORT" onClick="selectedcategory();"/>METRO AIRPORT												
												<input type="checkbox" name="aaicategory2" value="NON-METRO AIRPORT" onClick="selectedcategory();"/>NON-METRO AIRPORT
											</td>
										</tr>										
										<tr>										
											<td class="label" id="rg" style="display:none">
												Region
											</td>
											<td id="rg1" style="display:none">										
												<select name="region"  multiple style="width:130px">									
												</select>											
											</td>
										</tr>
										<tr>
										<td class="label">
												Module
											</td>
											<td>
												<select name="primarymodule" style="width:130px" onChange="primaryselect()">
													<option value="">[Select One]</option>
													<option value="Personal">Personal</option>
													<option value="Finance">Finance</option>
												</select>
											</td>		
										</tr>									
										<tr>
											<td class="label">
												Phone Number
											</td>
											<td>
												<input type="text" name="phno" maxlength=13/>												
											</td>
										</tr>
										<tr>										
											<td class="label">
												Email Id 
											</td>
											<td>
												<input type="text" name="email"/>
											</td>
										</tr>									
										
										<tr>
											<td class="label">
												Expiry Date & Time<font color="red">&nbsp;*</font>
											</td>
											<td>
												<input type="text" name="expirydate" size="8" maxlength=11/><b>-</b>
												<input type="text" name="expiretime" size="4" maxlength=7/>
											</td>
											</tr>
											<tr>
											<td class="label">
												Grid Length
											</td>
											<td>
												<input type="text" name="gridlength" />
											</td>
										</tr>
										<tr>											
											<td class="label">
												Remarks
											</td>
											<td>
												<textarea name="remarks" rows="3" cols="27"></textarea>
																						
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
												<input type="button" class="btn" value="Add" class="btn" onclick="testSS()"/>
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn"/>
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
											</td>
										</tr>

									</table>
								</td>
							</tr>
						</table>
						</form>
	</body>
</html>
<%
}
%>
