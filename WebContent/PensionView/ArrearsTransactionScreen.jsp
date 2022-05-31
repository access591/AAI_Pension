<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="java.beans.Beans"%>
<%@ page import="aims.bean.ArrearsTransactionBean"%>
<%
String dateOfAnnuation="",pensionno="",pensionoption="";
if(request.getAttribute("dateOfAnnuation")!=null){
	dateOfAnnuation=request.getAttribute("dateOfAnnuation").toString();
	pensionno=request.getAttribute("pensionno").toString();
	pensionoption=request.getAttribute("wetheroption").toString();

}
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
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></SCRIPT>
	    <SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>


	<script type="text/javascript">

	function getEmoluements(dueContribtion,emoluemntsboxno){
	var dueEmoluments=Math.round(dueContribtion*100/8.33);
	document.getElementById(emoluemntsboxno).value=dueEmoluments;
	}
	
	function validate(listsize){
     alert("user doesn't have the permissions to edit data");
	 return false;
		var option='<%=pensionoption.trim()%>';
		if(option=="B"){
			for(var i=1;i<=listsize;i++){
				var arrearamtboxno="arrearamt"+i;
			 var arrearamtvalue=document.getElementById(arrearamtboxno).value;
			 if(arrearamtvalue>541){
				 alert("PensionContribution can't exceed 541 for Option B");
				 document.getElementsByName(arrearamtboxno)[0].focus();
				 return false;
				  }
		}
		}
	    document.forms[0].action="<%=basePath%>psearch?method=addArrearsData&listsize="+listsize;
	    document.forms[0].method="post";
		document.forms[0].submit();
    
	}

	function getArrearData(pensionno,dateOfAnnuation){
		
		 document.forms[0].action="<%=basePath%>psearch?method=getArrearsTransactionData&pensionno="+pensionno+"&dateOfAnnuation="+dateOfAnnuation;
		 document.forms[0].method="post";
		 document.forms[0].submit();
	}

	 var xmlHttp;
	 function getNodeValue(obj,tag){
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	   	}
		function createXMLHttpRequest(){
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		 	}else if (window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
			 }
		}
	function  arrearupdate(arreartotal,pensionno){
		 var answer="";
		 if(document.forms[0].arrearMonth.value==""){
			 alert("Please Enter ArrearDate");
			 document.forms[0].arrearMonth.focus();
		  	  return false;
		 }
		var arreardate=document.forms[0].arrearMonth.value;
			
		 if(!document.forms[0].arrearMonth.value==""){
	   		    var date1=document.forms[0].arrearMonth;
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		
		if(status=="N"){
		   answer =confirm('Are you sure, do you want add the ArrearAmount '+arreartotal+' to '+arreardate);
		}else{
			 answer =confirm('Are you sure, do you want add the ArrearAmount '+arreartotal+' to '+arreardate);
		}
		   if(answer){
			   var formsstatus=status;
		var url="<%=basePath%>psearch?method=updatearears&pensionno="+pensionno+"&arreartotal="+arreartotal+"&arreardate="+arreardate;
		 createXMLHttpRequest();	
	   	 	xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = updateStatus;
			xmlHttp.send(null);
		   }
	 }
	 function updateStatus()
		{
		if(xmlHttp.readyState ==4)
		{
		 alert(xmlHttp.responseText);
			}
		}
	</script>
		</head>
	<body>
		<form method="post">
          <table width="100%">
			<tr>
				<td>
				<jsp:include page="/PensionView/PensionMenu.jsp"/>
					</td>
				</tr>		
					<tr>
					<td height="5%" colspan="2" align="center" class="ScreenHeading">
						<br>Arrears Transaction [SEARCH]
					</td>

				</tr>
		
				<%boolean flag = false;%>
				<tr>
					<td height="15%">
						<table align="center" >
							
						</table>
					</td>

            </tr>
				<%
				

				if (request.getAttribute("empList") != null) {
					ArrayList dataList = new ArrayList();
					dataList =(ArrayList) request.getAttribute("empList");
					
			  
				if (dataList.size() == 0) {		
					%>
                   <tr >

					<td>
						<table align="center" id="norec">
							<tr>
							<td>No Records Found</td>
							</tr>
                         </table>
					 </td>
					</tr>

					<%

				}			  
			else {		

               
					%>
				<tr >

					<td>
						<table align="center" id="rec">
							
							<tr>
					<td height="25%">
					<table align="center" cellpadding=2 class="tbborder" cellspacing="0"  border="0">
 <input type="hidden" name="wetheroption" value='<%=pensionoption%>'%>
							<tr class="tbheader">
								<td class="tblabel">
									PensionNo
								</td>								
								<td class="tblabel">
									EmployeeName&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td class="tblabel">
									Monthyear
								</td>
							<td class="tblabel">
									Emoluments
								</td>
								<td class="tblabel">
									EmpPfStatuary
								</td>
								<td class="tblabel">
									Due Emoluments
								</td>
								<td class="tblabel">
									Due PensionContri 
								</td>
								
								<td>
							        <img src="<%=basePath%>PensionView/images/page_edit.png" onclick="edit()">
							  </td>
								
								<td></td>
									<%}%>
							</tr>
							<%
								int count = 0;
                             double arrearTotal=0;
							String wedate="";
							
							int j=0;
										
				    for (int i = 0; i < dataList.size(); i++) {
				    	j++;
					count++;
					ArrearsTransactionBean beans = (ArrearsTransactionBean) dataList.get(i);
					double emolumentsDue=0.00;
					if(beans.getDueEmoluments().equals("0")&& beans.getDueEmoluments()!=null){
					emolumentsDue=Math.round(Double.parseDouble(beans.getArrearAmount())*100/8.33);
					}else{
						emolumentsDue=Double.parseDouble(beans.getDueEmoluments());
					}
					arrearTotal=arrearTotal+Double.parseDouble(beans.getArrearAmount());
                 	if (count % 2 == 0) {

					%>
					
							<tr class="tbevenrow">
								<%} else {%>
								<tr class="tboddrow">
							
							<%}%>
							  <td class="Data">
							 <input type="text" name="pensionno" size="10" value='<%=beans.getPensionNumber()%>' readonly="readonly" />
							  </td>							  
							  <td class="Data">
							  <%=beans.getEmpName()%>
							  </td>
							  <td class="Data">
							  <input type="text" name="monthyear<%=j%>" size="10" value='<%=beans.getMonthYear()%>' readonly="readonly"/>
							  </td>
							<td class="Data">
							  <%=beans.getEmoluments()%>
							  </td>
							<td class="Data">
							  <%=beans.getEmployeePF()%>
							  </td>
  						<td class="Data">
							 <input type="text" name="emoluments<%=j%>" size="10" value='<%=emolumentsDue%>' />
							  </td>
							<td class="Data">
							 <input type="text" name="arrearamt<%=j%>" size="10" value='<%=beans.getArrearAmount()%>' onblur="getEmoluements(this.value,'emoluments<%=j%>');"/>
							  </td>
                         
							<%}%>

			<% if (dataList.size() != 0) {%>
  				
                  					
<tr><td colspan="6" align="right" class="tblabel" >Due Contrib Total</td><td><input type="text" size="10" name="arreartotal" value='<%=arrearTotal%>'/></td></tr>
<tr>
					<td colspan="6" align="right" class="tblabel" >ArrearDate &nbsp;</td>
					                   
					<td><input type="text" name="arrearMonth"  size="10" onkeyup="return limitlength(this, 20)">
										<a href="javascript:show_calendar('forms[0].arrearMonth');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" /></a>
<input type="button"  value="U" onclick="arrearupdate('<%=arrearTotal%>','<%=pensionno%>')" >
										</td>           
 <% if  (request.getAttribute("arrearsInfo") != null) {
					ArrayList arrearInfo = new ArrayList();
					arrearInfo =(ArrayList) request.getAttribute("arrearsInfo");
					 count=0;
					  for (int i = 0; i < arrearInfo.size(); i++) {
						  ArrearsTransactionBean beans = (ArrearsTransactionBean) arrearInfo.get(i);
				   System.out.println("arrearamount "+beans.getEmolumentsArrear());	
				   if (count % 1 == 0) {%>
				   <tr><td class="tblabel" >Arrears Received On:</td><td > <%=beans.getArrearMonth()%></td><td colspan="1"  class="tblabel" >Arrears Amount :</td><td ><%=beans.getEmolumentsArrear()%></td></tr>
                   
				    <%}}} %> 
<tr><td>&nbsp;</td></tr>
<tr><td align="center" colspan="7" >
									<input type="hidden" name="dateOfAnnuation" value='<%=dateOfAnnuation%>'/>
									<input type="button" class="btn" value="Submit" class="btn" onclick="validate('<%=dataList.size()%>');">		
									<input type="button" class="btn" value="Reset"  class="btn">
									<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1);" class="btn">
						            
</td></tr>
         <%}}%>

						</table> 
					</td>
				</tr>
        
			</table>

		</form>
	</body>
</html>
<%}%>
