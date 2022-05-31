<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>


<%@ page import="aims.bean.EmpMasterBean,aims.bean.CeilingUnitBean"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";

      System.out.println("------------------------back to edit.jsp-------------------------------------------");
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
		<link rel="stylesheet" type="text/css" href="./PensionView/css/sample.css">
		<LINK rel="stylesheet" href="./PensionView/css/aimsfinancestyle.css" type="text/css">
		<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT  type="text/javascript" SRC="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">    
	 function validate(){	  		   

		var exp=/^\d+(\.)?(\d)?(\d)?$/    

		 if(document.forms[0].wefromdate.value=="")
   		 {
   		  alert("Please Enter With Effect From Date");
   		  document.forms[0].wefromdate.focus();
   		  return false;
   		  }	
		  
		 if(document.forms[0].wetodate.value=="")
   		 {
   		  alert("Please Enter With Effect To Date");
   		  document.forms[0].wetodate.focus();
   		  return false;
   		  }	

		 if (!convert_date(document.forms[0].wefromdate))
		{
			document.forms[0].wefromdate.focus();
			return false;
		}
				
		 if (!convert_date(document.forms[0].wetodate))
		{
			document.forms[0].wetodate.focus();
			return false;
		}    
		
		if(document.forms[0].wefromdate.value!="" &&  document.forms[0].wetodate.value!="")
	    {
				var cmp1=compareDates(document.forms[0].wefromdate.value,document.forms[0].wetodate.value);
				if (cmp1 =="larger")
				{
						alert(" To Date should  be greater than From Date");
						document.forms[0].wefromdate.focus();
						return false;
				}	
	    }
		if (!exp.test(document.forms[0].cerate.value) && document.forms[0].cerate.value!="")
	    {
		 alert("Rate shoud not contain more than 2 decimals");
		 document.forms[0].cerate.focus();
		 return false;
	    }

		
		return true;
	 
   }
   function checkceilingdata(){	
	
   		 var flag="celling";
         var url ="<%=basePath%>csearch?code="+document.forms[0].wefromdate.value+"&todate="+document.forms[0].wetodate.value+"&rate="+document.forms[0].cerate.value+"&flag="+flag;		

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

	  function resp()
      {		
         
			if (httpRequest.readyState == 4) // readyState of 4 signifies request is complete
			{ 		 		
			   if(httpRequest.status == 200)	// status of 200 signifies sucessful HTTP call
			   {  		  
				    var node = httpRequest.responseXML.getElementsByTagName("CHECK")[0];
					
				    if(node)
					{		
						var ss=httpRequest.responseXML.getElementsByTagName("STATUS")[0].firstChild.nodeValue;		
									
						if(ss=="yes"){
							alert('Record already exist in the date');
							document.forms[0].getwefromdate.focus();
							return false;
						}
						else
						{
							document.forms[0].action="<%=basePath%>csearch?method=update";
							document.forms[0].method="post";
							document.forms[0].submit();
							return true;
						}
					}								
				}
           }         
      }	
	 function testSS(){	
	
		if(!validate()){
			return false;
		}
		checkceilingdata();
   	}  	

</script>
</head>

<body >
		<form method="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
						<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				
				<tr>
		<td>
		<table width="55%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
		<tr>
			   <td height="5%" colspan="2" align="center" class="ScreenMasterHeading"> 
					 Ceiling Master[Edit]
				</td>
				
				<tr>
					<td height="15%">
						<%
						String  getwefromdate="",getwetodate="";
						double crate=0.0;
						int ccd=0;


					if (request.getAttribute("EditBean") != null) {

				  CeilingUnitBean bean1 = (CeilingUnitBean) request.getAttribute("EditBean");

                 getwefromdate=bean1.getFromWeDate();	
				 getwetodate=bean1.getToWeDate();	
				 crate=bean1.getRate();
				 ccd=bean1.getCeilingCode();
				 

			
				%>

						<table align="center">
							<tr>
								<td>&nbsp;&nbsp;&nbsp;</td>
							</tr>
				
							<tr>
							
							   <td class="label">
									W.E.Date From:
								</td>
								<td>
									<input type="text" name='wefromdate' value='<%=getwefromdate%>' >
									<a href="javascript:show_calendar('forms[0].cpfacnoNew');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								</tr>
								<tr>
							
							   <td class="label">
									W.E.Date To:
								</td>
								<td>
									<input type="text" name='wetodate' value='<%=getwetodate%>' >
									<a href="javascript:show_calendar('forms[0].cpfacnoNew');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
								</td>
								</tr>							
								<tr>
								 <td class="label">
									Ceiling Rate:
								</td>
								<td align=left>
									<input type="text" name="cerate" value='<%=crate%>' >
								</td>
							</tr>		
												
					<input type="hidden" name="ceilingcode" value='<%=ccd%>' >


                   	<%} %>
							
						
						
							</table>			

				
		            	 <tr>
		            	 		<td align="center">
								<table align="center">
								
									<tr>
									<td>
								
									<input type="submit" value="Update" onclick="testSS();"class="btn" >
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
									<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
									</td>
									</tr>
									</table>
								</td>
							
							
							</td>
							</tr>
							</table>
							</td>
							</tr>
		            	</table>
		
		</form>
	</body>
</html>
