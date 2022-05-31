<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/tags-display" prefix="display"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String frmName="",message="";
	ArrayList empSearchList = new ArrayList();
	if(request.getAttribute("frmName") != null){
		frmName=(String)request.getAttribute("frmName");
	} 
	if(request.getAttribute("searchList") != null){
		empSearchList =  (ArrayList)request.getAttribute("searchList");
		System.out.println("======"+frmName);
	}	
	String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	System.out.println(".............keys................"+keys);

	Iterator it = keys.iterator();
	if(request.getAttribute("searchInfo")!=null) {
		EmployeePersonalInfo beanInfo = (EmployeePersonalInfo)request.getAttribute("searchInfo");
		request.setAttribute("SetSearchBean",beanInfo);
		
	}
	
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
   <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />
	<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />    	   	 
	<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
	<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
	<script type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></script>
	 
	<script type="text/javascript"> 
		var frmName ='<%=frmName%>';
	    function getNodeValue(obj,tag){
			return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   		}
	   	var xmlHttp;
   		function createXMLHttpRequest(){
			if (window.XMLHttpRequest) {    
				xmlHttp = new XMLHttpRequest();   
			} else if(window.ActiveXObject) {    
			      try {     
		    	  	xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");    
			       } catch (e) {     
			       		try {      
			       			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");     
		    	   		} catch (e) {      
		       				xmlHttp = false;     
		       			}    
		       		}   			       		
		 	} 
		 }
		function getAirports(){	
			var regionID;
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
			createXMLHttpRequest();	
			var url ="<%=basePath%>psearch?method=getAirports&region="+regionID;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
			xmlHttp.send(null);
   		}
		function getAirportsList(){
			if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			 	process.style.display="block";
			}
			if(xmlHttp.readyState ==4){
				if(xmlHttp.status == 200){ 
					var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					process.style.display="none";
					if(stype.length==0){
					 	var obj1 = document.getElementById("select_airport");
					  	obj1.options.length=0; 
			  			obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}else{
			 		   	var obj1 = document.getElementById("select_airport");
			 		  	obj1.options.length = 0;
					 	for(i=0;i<stype.length;i++){
			  				if(i==0){
								obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
							}
							obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
						}
			  		}
				}
			}
		}
		function frmload(){
 			process.style.display="none";
 			//populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
		}
		function resetSearch(){
		    document.forms[0].action="<%=basePath%>reportservlet?method=pensionProcessInfo";
			document.forms[0].method="post";
			document.forms[0].submit();			
		}
		function Search(){
			var dob=document.forms[0].dob.value;
			var doj=document.forms[0].doj.value;
		    if(!document.forms[0].dob.value==""){
		    	var date1=document.forms[0].dob;
		   		var val1=convert_date(date1);	   	   
	   		    if(val1==false) {
	 		    	return false;
	   		    }
	   		}
			if(!document.forms[0].doj.value==""){
	   			var date1=document.forms[0].doj;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)  {
					return false;
	   		    }
	   		}
			var employeeNo=document.forms[0].employeeCode.value;		 
	   		var regionID;
			if(document.forms[0].select_region.selectedIndex>0){
				regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
			}else{
				regionID=document.forms[0].select_region.value;			
			}
			if(document.forms[0].select_airport.selectedIndex>0){
				airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
			}else{
				airportID=document.forms[0].select_airport.value;			
			}
			var empName=document.forms[0].empName.value;
			var pensionno=document.forms[0].pensionno.value;
			var penoption=document.forms[0].penoption.value;
			// alert("in search" +"<%=basePath%>psearch?method=searchPersonal&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&employeeNo="+employeeNo);
			url ="<%=basePath%>psearch?method=searchForPensionProcess&region="+regionID+"&airPortCode="+airportID+"&empName="+empName+"&dob="+dob+"&doj="+doj+"&employeeNo="+employeeNo+"&pensionno="+pensionno+"&penoption="+penoption+"&empName="+empName+"&frmName="+'<%=frmName%>';		
			//alert(url);
			document.forms[0].action=url;
		  	document.forms[0].method="post";
			document.forms[0].submit();
		}
		function returnNew(){ 
			document.forms[0].action="<%=basePath%>psearch?method=loadPensionProcessForm&frmName=PensionProcessNewForm";
		  	document.forms[0].method="post";
			document.forms[0].submit();
		
		}
		function editCheckedRecord(){ 		
	    	var value_data=getCheckedValue(document.forms[0].editradio);
	    	if(value_data=="") {
				alert("Please check radio button of the pfid that you want to edit");
				return false;
			}	
	    	var check=new Array();
	    	check=value_data.split(":");
	    	var pensioNo=check[0];
	    	var process_id=check[1];
	    	var verifiedBy=check[2];
	    	if(!verifiedBy=="") {
	    		alert("Cannot edit the data..CHQ HR has already verified!!");
	    		return false;
	    	}
	       // 	var process_id=document.getElementById("process_id").value;
	    	//alert(pensioNo+":"+process_id);
			var url="<%=basePath%>psearch?method=editPensionProcess&pensionno="+pensioNo+"&processid="+process_id+"&verifiedby="+verifiedBy;
		 	//alert(url);
		 	document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();	
		}
		function deleteRecord() {
			var value_data=getCheckedValue(document.forms[0].editradio);
	    	if(value_data=="") {
				alert("Please check radio button of the pfid that you want to delete");
				return false;
			}	
	    	var check=new Array();
	    	check=value_data.split(":");
	    	var pensioNo=check[0];
	    	var process_id=check[1];
	    	var verifiedBy=check[2];
	    	if(verifiedBy=="HRINIT") {
	    		alert("Cannot Delete the data..CHQ HR has already verified!!");
	    		return false;
	    	}
			return false;
		}		
		function getCheckedValue(radioObj) {
			if(!radioObj)
				return "";
			var radioLength = radioObj.length;
			if(radioLength == undefined)
				if(radioObj.checked)
					return radioObj.value;
				else
					return "";
			for(var i = 0; i < radioLength; i++) {
				if(radioObj[i].checked) {
					return radioObj[i].value;
				}
			}
			return "";
		}
    </script>
  </head>   
  <body onload="javascript:frmload()">
   <form name="personalMaster" action="" method="post">
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
					 
		<tr>
			<td>&nbsp;</td>
		</tr>
		<%if(!message.equals("")){%>
				<tr><td><font size="3" color ="red"><%=message%></font></td></tr>
				<%}%>
		</table>
	 
				<table  width="70%" border="0" cellpadding="1" align="center" cellspacing="0" class="tbborder">
				<tr>
				<td  colspan="5"  height="4%"   class="ScreenMasterHeading">Pension Process Search 
				</td>
			</tr>
			<tr><td>&nbsp;</td>	</tr>
					<tr>
						<td class="label">Region:</td>
						<td>
							<select name="select_region" onchange="javascript:getAirports()"  style="width:130px">
							<option value="NO-SELECT">[Select One]</option>
								<%
						     	   while(it.hasNext()){			
									 boolean exist = false;
									  reg=hashmap.get(it.next()).toString();  	
							 	%>
							  	<option value="<%=reg%>" ><%=reg%></option>
	                       		<%}%>
							</select>
						</td>
						<td class="label">Airport Code:</td>
						<td>
							<select name="select_airport" id="select_airport" style="width:130px" >
								<option value="NO-SELECT">[Select One]</option>
						    </select>
						</td>
					</tr>
					<tr>
						<td class="label">Employee Name:</td>
						<td><input type="text" name="empName" onkeyup="return limitlength(this, 20)"/></td>
						<td class="label">Employee Code</td>
						<td><input type="text" name="employeeCode" onkeyup="return limitlength(this, 20)" /></td>
						
					</tr>
                    
					<tr>
					 <td class="label">Date Of Birth:</td>
						<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->
                    
					<td><input type="text" name="dob" tabindex="9"  onkeyup="return limitlength(this, 20)"/>
										<a href="javascript:show_calendar('forms[0].dob');"><img src="<%=basePath%>PensionView/images/calendar.gif" alt="Calender" border="no" /></a>
					</td>

                   <td class="label">
									Date of Joining:</td>
				<td>
									
								    <input type="text" name="doj" tabindex="9"  onkeyup="return limitlength(this, 20)"/>
										<a href="javascript:show_calendar('forms[0].doj');"><img src="<%=basePath%>PensionView/images/calendar.gif"  alt="Calender" border="no" /></a>
				</td>

</tr>
<tr>
						<td class="label">Pensionno:</td>
						<td><input type="text" name="pensionno" onkeyup="return limitlength(this, 20)"/></td>
						<td class="label">Pension Option</td>
						<td><input type="text" name="penoption" onkeyup="return limitlength(this, 20)" /></td>
						
					</tr>
					<tr>
						<td colspan="4" align="center">
								<input type="button" class="btn" value="Search" class="btn" onclick="Search();"/>
								<input type="button" class="btn" value="Reset" onclick="javascript:resetSearch()" class="btn"/>
								<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn"/>
						</td>

					</tr>
			
			</table>
			<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>
		<table align="center" >
		 <tr><td>&nbsp;</td>	</tr>
          </table> 
		 
		<table  width="70%" border="0" align="center" class="tbborder">			 
				<tr >				
				 
				<td align="center"><input type="button"  value="New" class="btn" onclick="returnNew();" /></td>       
				<td align="center"><input type="button"  value="Edit" class="btn" onclick="editCheckedRecord();" /></td>
				<td align="center"><input type="button"  value="Delete" class="btn"  onclick="deleteRecord();" /></td>
				<!-- <td align="center"><input type="button"  value="Delete" class="btn" onclick="deleteRecords();" /></td> -->    
				 
				<td>&nbsp;</td>
			</tr>
			 </table> 
			<%System.out.println("==== empSearchList.size()==="+ empSearchList.size()+"==="+request.getAttribute("searchBean"));
					
			if (request.getAttribute("searchBean") != null   ) {
					 ArrayList arraylist=(ArrayList)request.getAttribute("searchBean");
					 int j =-1;
					 if(arraylist.size()==0){
					 }
					 else{
					 
					
					%>
					<table align="center" >
							<tr><td>&nbsp;</td>	</tr>
                         </table> 
				 	<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"> 
				   <tr>							
								 
								 	
								 	
								 									 	
								<td align="center" width="90%">
														
								<display:table  style="width: 750px"   pagesize="15" name="requestScope.searchBean" requestURI="./psearch?method=searchForPensionProcess" >   											
									<% 
									j++;
									String status="";
									EmployeePersonalInfo info=(EmployeePersonalInfo)arraylist.get(j);								
								 	if(info.getVerifiedBy().equals("")) {
								 		status="Initial";
								 	}else if(info.getVerifiedBy().equals("HRINIT")) {
								 		status="HR Approved";
								 	}else if(info.getVerifiedBy().equals("HRINIT/HRFIN")) {
								 		status="HR Finance Approved";
								 	}else if(info.getVerifiedBy().equals("HRINIT/HRFIN/HRAPPROVE")) {
								 		status="Submited to RPFC";
								 	}else if(info.getVerifiedBy().equals("HRINIT/HRFIN/HRAPPROVE/RPFC")) {
								 		status="Reject/Return from RPFC";
								 	}
								 	%>	
									
    								<display:column   title=""> 
    								<input type="radio" name="editradio" value="<%=info.getPensionNo()%>:<%=info.getProcessID()%>:<%=info.getVerifiedBy()%>" />
    								<input type="hidden" name="process_id" id="process_id" value=""/>
    								</display:column>
    								<display:column property="pensionNo"  title="PFID" />
									<display:column property="employeeName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
								 	<display:column property="designation" title="Designation"/>
								 	<display:column property="wetherOption" title="Option"/>			
								 	<display:column property="dateOfBirth" title="DateOfBirth"/>	
									<display:column property="airportCode"  title="Airport" />	
									<display:column property="region"  title="Region" />	
									<display:column value="<%=status%>" title="status"> </display:column>
								    </display:table>
								</td>
							</tr> 
						</table>  
					 <%}
					 
					 }
					 %>
	</form>
		
  </body>
</html>
	