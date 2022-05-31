<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*,aims.bean.FinancialYearBean"%>
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
  	 	
		 function validate(){

		 var ex=/^[0-9]+$/;	 				 
		 
		 if(document.forms[0].month.selectedIndex<1)			
		  {
			  alert("Please Select Month");				  
			  return false;
		  }  
         if(document.forms[0].fromdate.value=="")
   		 {
   		  alert("Please Enter Financial Year From ");
   		  document.forms[0].fromdate.focus();
   		  return false;
   		  }
   		 if(document.forms[0].fromdate.value!="")
		  {		    
		      if (!ex.test(document.forms[0].fromdate.value))
		      { 
				 alert("Financial Year should be in digits");
				 document.forms[0].fromdate.select();
			     return (false);
		      }
	      }
	      	 		
   		 if(document.forms[0].todate.value=="")
   		 {
   		  alert("Please Enter Financial Year To ");
   		  document.forms[0].todate.focus();
   		  return false;
   		  }
   		  
   		 if(document.forms[0].todate.value!=""){
   		     if (!ex.test(document.forms[0].todate.value))
		      { 
				 alert("Financial Year should be in digits");
				 document.forms[0].todate.select();
			     return (false);
		      }
   		 }
   		 
   		 if(document.forms[0].fromdate.value!="" && document.forms[0].todate.value!=""){
   		 
   		   if((document.forms[0].todate.value)>(document.forms[0].fromdate.value)){
   		       if(((document.forms[0].todate.value)-(document.forms[0].fromdate.value))!=1){
   		        alert("Financial Year Difference should not exceed 1 year");
   		        document.forms[0].todate.select();
			    return (false);   		                        
   		       }   		   
   		   }else{
   		      alert("Financial Year To should be greater than Financial Year From");
   		      document.forms[0].todate.select();
			  return (false);
   		   }   		    
   		 }
   		
   		 if(document.forms[0].description.value=="")
   		 {
   		  alert("Please Enter Financial Description");
   		  document.forms[0].description.focus();
   		  return false;
   		  }
   		   		  
   		  return true;
   		}
	

	    function testSS(){
	    if(!validate()){
			return false;
		  }
		document.forms[0].action="<%=basePath%>csearch?method=financialyearadd";
		document.forms[0].method="post";
		document.forms[0].submit();     
	 }  	

</script>
</head>
<%
    String monthID="",monthName="",tempMonthVal="",tempMonthNameVal="";
  	CommonUtil common=new CommonUtil();    

   	Map map=new LinkedHashMap();
	map=common.getMonthsList();
	Set monthset = map.entrySet(); 
	Iterator it  = monthset.iterator(); 

  %>
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
					 Financial Year Master[Add]
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
									Financial Year From:
								</td>
								
								 <%  ArrayList al=new ArrayList();
								 String month="",monthno="",year="",century="",century2="";
								 int temp=0;
						         if(request.getAttribute("FinancialYear")!=null){
						            al=(ArrayList)request.getAttribute("FinancialYear");
						            FinancialYearBean fbean=new FinancialYearBean();
						            fbean=(FinancialYearBean)al.get(0);
						            month=fbean.getMonth();
						            monthno=fbean.getMonthnumber();
						            
						            century=fbean.getFromDate().substring(0,2);
						            temp=Integer.parseInt(century);
						            century2=fbean.getFromDate().substring(5);
						            if(century2.equals("00"))
						               century="20";
						            year=fbean.getFromDate().substring(5);
						            year=century+year;						            
						            %>
						             <td>	
						            <select name="month" style="width:94px">
						            <%         
						           
						             while(it.hasNext()) { 
						              boolean exist = false;
										Map.Entry me = (Map.Entry)it.next();
										monthID=me.getKey().toString();
										monthName=me.getValue().toString();	
										
										 if(monthID.equals(monthno))	
										  exist = true;  										  
									  	if (exist) {																               
									   
									    %>									   
									    <option value="<%=monthno%>" <% out.println("selected");%>><%=month%></option>
									    <%
									    }else{%>
									     <option value="<%=monthID%>"><%=monthName%></option>
									    <%}	}					           
						            %>
						           				            
									
									</select>
									&nbsp;
									<input type="text" name="fromdate" size="4" maxlength="4" tabindex="1" value=<%=year%>>
									</td>
						   
						         <% }else{
						        %>
								<td>	
								<select name="month" style="width:94px">
								<option>[Select Month]
								<%
								while(it.hasNext()) { 
								Map.Entry me = (Map.Entry)it.next();
								monthID=me.getKey().toString();
								monthName=me.getValue().toString();								
								%>								
						            <option value=<%=monthID%>><%=monthName%>
								<%}
								%>	
									</select>
									&nbsp;
									<input type="text" name="fromdate" size="4" maxlength="4" tabindex="1">
								</td>
							<%}%>	
							</tr>
							<tr>
								 <td class="label">
									Financial Year To:
								</td>
								<td>
									<input type="text" name="todate" size="4" maxlength="4" tabindex="2">									
								</td>
								
							</tr>
							<tr>
								 <td class="label">
									Financial Description:
								</td>
								<td>
									<input type="text" name="description">&nbsp;&nbsp;
								</td>
							</tr>	
							<tr>
								 <td class="label">
									Remarks:
								</td>
								<td>									
									<textarea name="remarks" rows="4" cols="27">
									</textarea>
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
