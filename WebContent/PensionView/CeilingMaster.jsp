<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
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


		<SCRIPT type="text/javascript" src="./scripts/calendar.js"></SCRIPT>
		<SCRIPT  type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">  		
  	 		
		function checknumdotonly()
         {
	          if(((event.keyCode>=48)&&(event.keyCode<=57))||event.keyCode==46)
	               {
		           event.keyCode=event.keyCode;
	               }
	          else
	               {
		           event.keyCode=0;
	               }
         }
       function validate(){			   

	 var exp=/^\d+(\.)?(\d)?(\d)?$/

		 if(document.forms[0].wedatefrom.value=="")
   		 {
   		  alert("Please Enter With Effect From Date");
   		  document.forms[0].wedatefrom.focus();
   		  return false;
   		  }	
		  
		 if(document.forms[0].wedateto.value=="")
   		 {
   		  alert("Please Enter With Effect To Date");
   		  document.forms[0].wedateto.focus();
   		  return false;
   		  }	

		 if (!convert_date(document.forms[0].wedatefrom))
		{
			document.forms[0].wedatefrom.focus();
			return false;
		}
				
		 if (!convert_date(document.forms[0].wedateto))
		{
			document.forms[0].wedateto.focus();
			return false;
		}  		
		
		if(document.forms[0].wedatefrom.value!="" &&  document.forms[0].wedateto.value!="")
	   {
			var cmp1=compareDates(document.forms[0].wedatefrom.value,document.forms[0].wedateto.value);
			 if (cmp1 =="larger")
			{
				alert(" To Date should  be greater than From Date");
				document.forms[0].wedatefrom.focus();
				return false;
			}	
	   }
       
		if (!exp.test(document.forms[0].rate.value) && document.forms[0].rate.value!="")
	    {
		  alert("Rate shoud not contain more than 2 decimals");
		 document.forms[0].rate.focus();
		 return false;
	    }

		var interestrate=document.forms[0].interestrate.value;
		if(interestrate.indexOf(".")>=3 || interestrate.indexOf(".")==-1)
       {
			if(interestrate.substring(0,3)>100){
			alert("Interest Rate should not exceed 100%");
			document.forms[0].interestrate.focus();
		    return false;
		    }
		}
		else if (!exp.test(document.forms[0].interestrate.value) && document.forms[0].interestrate.value!="")
	    {
		 alert("Interest Rate shoud not contain more than 2 decimals");
		 document.forms[0].interestrate.focus();
		 return false;
	    } 	
	    	
   		

		return true;
	   }

	   function checkwedate(){	  
	   	    var flag="celling";
           var url ="<%=basePath%>csearch?code="+document.forms[0].wedatefrom.value+"&todate="+document.forms[0].wedateto.value+"&rate="+document.forms[0].rate.value+"&interestrate="+document.forms[0].interestrate.value+"&flag="+flag;			 
		
	       sendURL(url,"resp");
	   }

	   function sendURL(url,response)
      {	  
	        
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
                   
					httpRequest.open("GET",url,true); //2nd arg is url with name/value pairs, true specifies asynchronus communication	

					httpRequest.onreadystatechange=eval(response); 
					httpRequest.send(null);
				}	 
      }
		var ss="";
	  function resp()
      {		
          
			if (httpRequest.readyState == 4) // readyState of 4 signifies request is complete
			{ 		 		
			   if(httpRequest.status == 200)	// status of 200 signifies sucessful HTTP call
			   {  		  
				    var node = httpRequest.responseXML.getElementsByTagName("CHECK")[0];
					

				    if(node)
					{		
						ss=httpRequest.responseXML.getElementsByTagName("STATUS")[0].firstChild.nodeValue;	
					
						if(ss=="yes"){
								alert('Record already exist in the date');
								document.forms[0].wedate.focus();
								return false
						}else{
								document.forms[0].action="<%=basePath%>csearch?method=ceilingadd";
								document.forms[0].method="post";
								document.forms[0].submit();
								return true;
						}
					}								
				}else {
				alert("Error loading at Sub Type Details \n"+ httpRequest.status +":"+ httpRequest.statusText); 
			 }
           }         
      }

	    function testSS(){
	    
		if(!validate()){
			return false;
		}
         
		checkwedate();

   		 }  	

</script>
</head>
<body>
		<form method="post">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
					<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
		</tr>
				<tr>
			 <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr>
		<td>
		<table width="55%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
		<tr>
			   <td height="5%" colspan="2" align="center" class="ScreenMasterHeading"> 
					 Ceiling Master[Add]
				</td>

				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;
				</tr>
				<tr>
					<td height="15%">
						<table align="center">
							<tr>
								 <td class="label">
									W.E. Date From:
								</td>
								<td>
									<input type="text" name="wedatefrom" tabindex="1">
									&nbsp; <a href="javascript:show_calendar('forms[0].wedatefrom');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								
							</tr>
							<tr>
								 <td class="label">
									W.E. Date To:
								</td>
								<td>
									<input type="text" name="wedateto" tabindex="2">
									&nbsp; <a href="javascript:show_calendar('forms[0].wedateto');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								
							</tr>
							<tr>
								 <td class="label">
									Ceiling Rate:
								</td>
								<td>
									<input type="text" name="rate" onkeypress="checknumdotonly()" tabindex="3">&nbsp;&nbsp;
								</td>
							</tr>			
							<tr>
								<td>
									<input type="hidden" name="interestrate" onkeypress="checknumdotonly()">&nbsp;&nbsp;
								</td>
							</tr>			

 							
							<tr>
								<td>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								
								<td colspan="2">
									<input type="button" class="btn" value="Add" class="btn" onclick="testSS()">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">									
								</td>
							</tr>
					</table>
			</td>
		   </tr>
		   			</table>
			</td>
		   </tr>
		</table>			
	</form>
  </body>
</html>
