<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/tags-display" prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String frmName="",loginUsrRegion="";
ArrayList empSearchList = new ArrayList();
if(request.getAttribute("frmName") != null){
	frmName=(String)request.getAttribute("frmName");
	} 
if(request.getAttribute("searchList") != null){
empSearchList =  (ArrayList)request.getAttribute("searchList") ;
System.out.println("======"+frmName);
}
if(session.getAttribute("loginUsrRegion")!=null){
loginUsrRegion = (String) session.getAttribute("loginUsrRegion");	
	}
String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	System.out.println(".............keys................"+keys);

	Iterator it = keys.iterator();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
   <meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />
    	<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />    	 
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
     				document.forms[0].action="<%=basePath%>psearch?method=loadPFIDProcessSearchForm&frmName=PFIDApprovalSearch";
					document.forms[0].method="post";
					document.forms[0].submit();
			
		}
	function Search(){
		
		process.style.display="none";
   		 var empNameCheak="",airportID="",sortColumn="EMPLOYEENAME",day="",month="",year="";
	 
		
   		var dob=document.forms[0].dob.value;
		var doj=document.forms[0].doj.value;
		 if(!document.forms[0].dob.value==""){
		 
	   		    var date1=document.forms[0].dob;
	   		   //  alert("date1"+date1);
	   		    //alert(date1+"=="+convert_date(date1));
	   		   var val1=convert_date(date1);
	   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		    }
		 if(!document.forms[0].doj.value==""){
	   		    var date1=document.forms[0].doj;
	   	        var val1=convert_date(date1);
	   		    if(val1==false)
	   		     {
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
		// alert("in search" +"<%=basePath%>psearch?method=searchPersonal&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&employeeNo="+employeeNo);
		url ="<%=basePath%>psearch?method=searchForPfidProcess&region="+regionID+"&airPortCode="+airportID+"&frm_sortingColumn="+sortColumn+"&frm_dateOfBirth="+dob+"&doj="+doj+"&employeeNo="+employeeNo+"&frmName="+'<%=frmName%>';
		//alert(url);
		document.forms[0].action=url;
	  	document.forms[0].method="post";
		document.forms[0].submit();
	}
	function returnNew(){ 
		document.forms[0].action="<%=basePath%>psearch?method=loadPFIDProcessNewForm&frmName=PFIDProcessNewForm";
	  	document.forms[0].method="post";
		document.forms[0].submit();
	
	}
	function editCheckedRecord(){   
    	 
		     //alert(frmName);
		      var count=0;
		      var str=new Array();
		      var processid,employeeno,cpfaccno,verifiedBy;
		        
		if(document.forms[0].chk.length!=undefined){    
		  
		      for(var i=0;i<document.forms[0].chk.length;i++){		      	 
			      if (document.forms[0].chk[i].checked==true){
			        count++;
			        str=document.forms[0].chk[i].value.split(':');
			        
			      }
		      }
		      if(count==0){
		      	alert('User Should select PFID Process request');		     
		       				return false;
		      }
		     }else{
		     	 	if(document.forms[0].chk.checked){
				 		str=document.forms[0].chk.value.split(':');
				 	}else{
				 		   alert('User Should select PFID Process request');		     
		       				return false;
				 	}
		     }
		         for(var j=0;j<str.length;j++){			        
			          processid=str[0];
			          employeeno=str[1];
			          cpfaccno=str[2];
			          verifiedBy=str[3];
			   }
			       
			        //alert(verifiedBy);
			        
			         
			         editPFIDProcess(processid,verifiedBy,frmName);
		     	
		
		
   	 }
   	 
   	 function editPFIDProcess(processid,verifiedBy,frmName)
		{			
				   
			  		if((frmName=='PFIDRecommendationSearch' || frmName=='PFIDProcessSearch') && (verifiedBy=='PERSONAL,RHQ,CHQ')){
			         alert("PFID Process Request is approved by CHQ");
			        }else  if((frmName=='PFIDProcessSearch') && (verifiedBy=='PERSONAL,RHQ')){
			         alert("PFID Process Request is approved by RHQ");
			        }else{
					var url,editFrmName;
					if(frmName=='PFIDProcessSearch'){
					editFrmName = "editPFIDProcessForm";
					}else if(frmName=='PFIDRecommendationSearch'){
					editFrmName = "PFIDProcessRecommendation";
					}else if(frmName=='PFIDApprovalSearch'){
					editFrmName = "PFIDProcessApproval";
					}	
					url="<%=basePath%>psearch?method=editPFIDProcess&processid="+processid+"&frmName="+editFrmName;
				 	document.forms[0].action=url;
					document.forms[0].method="post";
					document.forms[0].submit();				
			        }		    
			  
		}	

function createPFID(){

  //alert(frmName);
		      var count=0;
		      var str=new Array();
		      var processid,employeeno,cpfaccno,verifiedBy,employeeName,dob,doj,designation,fhName,pensionOption,gender,region,airportcode;
		        
		if(document.forms[0].chk.length!=undefined){    
		  
		      for(var i=0;i<document.forms[0].chk.length;i++){		      	 
			      if (document.forms[0].chk[i].checked==true){
			        count++;
			        str=document.forms[0].chk[i].value.split(':');
			        
			      }
		      }
		      if(count==0){
		      	alert('User Should select PFID Process request');		     
		       				return false;
		      }
		     }else{
		     	 	if(document.forms[0].chk.checked){
				 		str=document.forms[0].chk.value.split(':');
				 	}else{
				 		   alert('User Should select PFID Process request');		     
		       				return false;
				 	}
		     }
		     
		     
		     
		       for(var j=0;j<str.length;j++){			        
			          processid=str[0];
			          employeeno=str[1];
			          cpfaccno=str[2];
			          verifiedBy=str[3];
			          employeeName=str[4];
			           dob=str[5];
			           doj=str[6];
			           designation=str[7];
			           fhName=str[8];
			           pensionOption=str[9];
			           gender=str[10];
			           region=str[11];
			           airportcode=str[12];
			   }
			       
			        //alert(verifiedBy); 
			         
			         createNewPFID(processid,verifiedBy,frmName,cpfaccno,employeeno,employeeName,dob,doj,designation,fhName,pensionOption,gender,region,airportcode); 
  }
function createNewPFID(processid,verifiedBy,frmName,cpfaccno,employeeno,employeeName,dob,doj,designation,fhName,pensionOption,gender,region,airportcode)
		{			
				 //  alert(verifiedBy);
			  		if(verifiedBy!='PERSONAL,RHQ,CHQ'){
			         alert("PFID Process Request is not approved by CHQ");
			        }else{
					var url=""; 
					 
					frmName = "PFIDCreation"; 
					url="<%=basePath%>psearch?method=createPFIDProcess&region="+region+"&airPortCode="+airportcode+"&empName="+employeeName+"&frm_dateOfBirth="+dob+"&doj="+doj+"&cpfaccno="+cpfaccno+"&employeeNo="+employeeno+"&gender="+gender+"&fhname="+fhName+"&option="+pensionOption+"&desegnation="+designation+"&processid="+processid+"&frmName="+frmName;
				 	document.forms[0].action=url;
					document.forms[0].method="post";
					document.forms[0].submit();				
			        }		    
			  
		}	

	function deleteRecords(){ 
		document.forms[0].action="<%=basePath%>psearch?method=loadPFIDProcessNewForm&frmName=PFIDProcessNewForm";
	  	document.forms[0].method="post";
		document.forms[0].submit();
	
	}
	function getPfidFiles()
		{		
		  //alert(frmName);
		      var count=0;
		      var str=new Array();
		      var processid,employeeno,cpfaccno,verifiedBy,fileName;
		        
		if(document.forms[0].chk.length!=undefined){    
		  
		      for(var i=0;i<document.forms[0].chk.length;i++){		      	 
			      if (document.forms[0].chk[i].checked==true){
			        count++;
			        str=document.forms[0].chk[i].value.split(':');
			        
			      }
		      }
		      if(count==0){
		      	alert('User Should select PFID Process request');		     
		       				return false;
		      }
		     }else{
		     	 	if(document.forms[0].chk.checked){
				 		str=document.forms[0].chk.value.split(':');
				 	}else{
				 		   alert('User Should select PFID Process request');		     
		       				return false;
				 	}
		     }
		         for(var j=0;j<str.length;j++){			        
			          processid=str[0];
			          employeeno=str[1];
			          cpfaccno=str[2];
			          verifiedBy=str[3];
			          fileName=str[13];
			   }
			       
			       // alert(fileName);
			        	
				    if(fileName==""){
				     		alert("No File is Uploaded for this ProcessId "+processid);		     
		       				return false;
				    }else{
					url="<%=basePath%>psearch?method=getPFIDFiles&processid="+processid+"&frmName="+'<%=frmName%>'+"&fileName="+fileName;
				 	document.forms[0].action=url;
					document.forms[0].method="post";
					document.forms[0].submit();				
			        }	
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
		<td>
		
		</td>
	</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="15%">
				<table  width="75%" border="0"  cellpadding="1" align="center" cellspacing="0" class="tbborder">
				<tr class="tbheader">
				<td  colspan="4" align="center" class="tblabel">PFID Approval Form[Search]</td>
			</tr>
			<tr>
			<td>&nbsp;</td>
			</tr>
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
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="15%">
		<table  width="75%" border="0" align="center" class="tbborder">
				<tr > 
				<td align="center"><input type="button"  value="Approve" class="btn" onclick="editCheckedRecord();" /> </td><!--
				  <td align="center"><input type="button"  value="Create PFID" class="btn" onclick="createPFID();" />      </td> 
				 --><td align="center"><input type="button"  value="Files" class="btn" onclick="getPfidFiles();" /></td>
				 
				<td>&nbsp;</td>
			</tr>
			 </table> 
			<%System.out.println("==== empSearchList.size()==="+ empSearchList.size()+"==="+request.getAttribute("searchList"));
					
			if (request.getAttribute("searchList") != null   ) {
					 
					 
					
					%>
					<table align="center" >
							<tr><td>&nbsp;</td>	</tr>
                         </table> 
				 	<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"> 
				   <tr>							
								 	<% int j =0;%>	
								<td align="center" width="95%">          
														
								<display:table  style="width: 830px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./psearch?method=searchForPfidProcess" >   											
									 <display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    								
    								<display:column title="" media="html">
								  	<input type="radio" name ="chk" value='<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getProcessID()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getEmployeeNumber()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getCpfAccno()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getVerifiedBy()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getEmployeeName()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getDateOfBirth()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getDateOfJoining()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getDesignation()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getFhName()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getWetherOption()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getGender()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getRegion()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getAirportCode()%>:<%=((EmployeePersonalInfo)pageContext.getAttribute("advanceList")).getFileName()%>' />
								    </display:column>			 
    								<display:column property="processID" title="Tracking Id" class="datanowrap"/>
    								<% j++;%>
									<display:column property="employeeName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
								 	<display:column property="employeeNumber"  title="EmployeeNumber" />	
								 	<display:column property="dateOfBirth" title="DateOfBirth"/>	
									<display:column property="dateOfJoining"  title="DateOfJoining" />									 
									<display:column property="airportCode"  title="Airport" />	
									<display:column property="region"  title="Region" />									 	
									<display:column property="transDt"  title="FormDate" class="datanowrap"/> 	
									<display:column property="enteredBy"  title="EnteredBy" class="datanowrap"/> 	
									<display:column property="recomendBy"  title="RecomendBy" class="datanowrap"/> 													 
									<display:column property="pfidProcessStatus"  title="Status" class="datanowrap"/> 
									 
								 
								    </display:table>
								</td>
							</tr> 
						</table>  
					 <%}%>
			
			</table>
			</td>
			</tr>
			</table>
	
	</form>
		
  </body>
</html>
	