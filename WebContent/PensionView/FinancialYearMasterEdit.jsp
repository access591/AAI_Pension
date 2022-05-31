<%@ page import="java.util.*,aims.common.*"%>
<%@ page language="java"%>

<%@ page import="aims.bean.EmpMasterBean,aims.bean.FinancialYearBean"%>
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
		document.forms[0].action="<%=basePath%>csearch?method=financialYearUpdate";
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
					 Financial Year Master[Edit]
				</td>
				
				<tr>
					<td height="15%">
						<%
						String  month="",monthno="",todate="",fromdate="",description="",remarks="",century="",century2="";					
						int financialId=0,temp=0;;


					if (request.getAttribute("EditBean") != null) {

				  FinancialYearBean bean1 = (FinancialYearBean) request.getAttribute("EditBean");

                 financialId=bean1.getFinancialId();	
				 month=bean1.getMonth();	
				 monthno=bean1.getMonthnumber();				
				 description=bean1.getDescription();
				 remarks=bean1.getRemarks();
				 fromdate=bean1.getFromDate().substring(0,4);
				 
				 century=fromdate.substring(0,2);
				 temp=Integer.parseInt(century);
				 century2=bean1.getFromDate().substring(5);
				 if(century2.equals("00"))
				     century="20";
				     todate=bean1.getFromDate().substring(5);
				     todate=century+todate;
			
				%>

						<table align="center">
							<tr>
								<td>&nbsp;&nbsp;&nbsp;</td>
							</tr>
				
							<tr>
							
							   <td class="label">
									Financial Year From:
								</td>
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
									<input type="text" name="fromdate" size="4" maxlength="4" tabindex="1" value=<%=fromdate%> readonly>
									</td>
								
								</tr>
								<tr>
							
							   <td class="label">
									Financial Year To:
								</td>
								<td>
									<input type="text" name='todate' value='<%=todate%>' readonly>
								</td>
								</tr>							
								<tr>
								 <td class="label">
									Financial Description
								</td>
								<td align=left>
									<input type="text" name="description" value='<%=description%>' >
								</td>
							</tr>	
							<tr>
								 <td class="label">
									Remarks
								</td>
								<td align=left>
									<input type="text" name="remarks" value='<%=remarks%>' >
								</td>
							</tr>		
												
					<input type="hidden" name="financialId" value='<%=financialId%>' >


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
