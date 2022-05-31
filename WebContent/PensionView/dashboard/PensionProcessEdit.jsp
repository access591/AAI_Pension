
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeePersonalInfo" %>
<%@ page import="aims.bean.*" %>
<%@ page import="java.util.ArrayList" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String frmName="",message="", loginUsrRegion="";
	if(request.getAttribute("frmName") != null){
		frmName=(String)request.getAttribute("frmName");
	} 
	if(request.getAttribute("message") != null){
		message=(String)request.getAttribute("message");
	} 
	
	if(session.getAttribute("loginUsrRegion")!=null){
		loginUsrRegion = (String) session.getAttribute("loginUsrRegion");	
	}
			
	String reg="";
  	CommonUtil common=new CommonUtil();    

   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();

	Iterator it = keys.iterator();
	if(request.getAttribute("searchInfo")!=null) {
		EmployeePersonalInfo beanInfo = (EmployeePersonalInfo)request.getAttribute("searchInfo");
		request.setAttribute("SetSearchBean",beanInfo);
		
	}	
	
	String employeeName="",employeeNo="",cpfaccNo="",dateOfBirth="",region="",airportcode="",fhName="",gender="",designation="",dateOfJoining="",whetherOption="",processId="",remarks="",fileName="";
 
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>AAI</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
   	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">	
    <SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
	<script type="text/javascript"> 
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
	 		populatedropdown("daydropdown", "monthdropdown", "yeardropdown");
		}
		function resetParams(pensioNo,processid,verifiedBy){	 
	    	document.forms[0].action="<%=basePath%>psearch?method=editPensionProcess&pensionno="+pensioNo+"&processid="+processid+"&verifiedby="+verifiedBy;
			document.forms[0].method="post";
			document.forms[0].submit();				
		}		
		function validate(){
			var ex=/^[0-9]+$/;
			var mail=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
			process.style.display="none";
			var pensionNo=document.getElementById("pensionNo").value;
			var procesid=document.getElementById("proces_id").value;
   			var pensionType=document.getElementById("pensiontype").value;
   			var chqDate=document.getElementById("docchq").value;
   			var emailID=document.getElementById("emailid").value;
   			var mobileNum=document.getElementById("mblno").value;
   			var remarks=document.getElementById("remarks").value;

		 	if(chqDate==""){
	   			alert("Please Enter Date of case sent to CHQ");
				document.forms[0].docchq.focus();
				return false;
   		    }	
   		    if(!chqDate==""){
	   			var date1=document.forms[0].docchq;
		   		var val1=convert_date(date1);
	   	   	    if(val1==false) {
	  		    	return false;
	   		    }
   		    }		 		 
			if(emailID==''){
				alert("Please Enter Email ID");
				document.forms[0].emailid.focus();
				return false;
			}
			if(!mail.test(emailID)) {
				alert("Invalid Email ID..Pl Enter again");
				document.forms[0].emailid.select();
				return false;
			}
			if(mobileNum==''){
				alert("Please Enter Mobile Number");
				document.forms[0].mblno.focus();
				return false;
			}
			if(!ex.test(mobileNum)) {
				alert("Invalid Mobile Number.Characters are not allowed");
				document.forms[0].mblno.focus();
				return false;
			}
			if(mobileNum.length!=10 ){
				alert("Invalid Mobile Number. only 10 digits allowed");
				document.forms[0].mblno.focus();
				return false;
			}			
			var url="<%=basePath%>psearch?method=updatepensionprocess&pensionType="+pensionType+"&chqDate="+chqDate+"&emailid="+emailID+"&mobileNo="+mobileNum+"&remarks="+remarks+"&pensionno="+pensionNo+"&processID="+procesid;
			//alert(url);			
			document.forms[0].action=url;
	  		document.forms[0].method="post";
			document.forms[0].submit();		
		}				
	</script>
	</head>
    
<body onload="frmload();" >
   <form enctype="multipart/form-data" action="" method="post">
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
				<table  width="70%" border="0" align="center" class="tbborder">
				
				<tr class="tbheader">
				<td  colspan="4" align="center" class="tblabel">  
				Pension Process Form[Edit]
				 </td>
			</tr>
			<%
			String pensionType="",pensionTypeVal="",pensionType2="",pensionTypeVal2="",pensionType3="",pensionTypeVal3="";
			EmployeePersonalInfo  empInfo = new EmployeePersonalInfo();
			
   			if(request.getAttribute("empInfo")!=null){    
			    empInfo = (EmployeePersonalInfo)request.getAttribute("empInfo");
   				pensionType=empInfo.getPensiontype();	
   				System.out.println("remarks:"+empInfo.getRemarks());
  				if(pensionType.equals("S")) {
  					pensionTypeVal="Self Pension";
  					pensionType2="F";
  					pensionType3="O";
  					pensionTypeVal2="Family Pension";
  					pensionTypeVal3="Orphan Pension";
  				}
  				else if(pensionType.equals("F")) {
  					pensionTypeVal="Family Pension";
  					pensionType2="S";
  					pensionType3="O";
  					pensionTypeVal2="Self Pension";
  					pensionTypeVal3="Orphan Pension";
  				}
  				else if(pensionType.equals("O")) {
  					pensionTypeVal="Orphan Pension";
  					pensionType2="F";
  					pensionType3="S";
  					pensionTypeVal2="Family Pension";
  					pensionTypeVal3="Self Pension";
  				}
 		
	
			 %>
			<tr ><td style="hight:10px" colspan="4">&nbsp;</td></tr>
					
<tr>
						<td class="label" align="left">
									Pensionno:&nbsp;
								</td>
								<td>
									<input type="text" name="pensionNo" value="<%=empInfo.getPensionNo()%>" readonly="readonly" tabindex="1"/>
									<input type="hidden" name="proces_id" value="<%=empInfo.getProcessID()%>" readonly="readonly" tabindex="1"/>
								</td>
						<td class="label">Old EmployeeNo:</td>
						<td><input type="text" name="oldempno" value="<%=empInfo.getOldPensionNo()%>" readonly="readonly" onkeyup="return limitlength(this, 20)"/>
					 
						</td>
						
					</tr>
					<tr>
						<td class="label">Region:</td>
						<td><input type="text" name="regionname" value="<%=empInfo.getRegion()%>" readonly="readonly" onkeyup="return limitlength(this, 20)"/>
						
						<td class="label">Airport Code:</td>
						<td><input type="text" name="airportcode" value="<%=empInfo.getAirportCode()%>" readonly="readonly" onkeyup="return limitlength(this, 20)"/>
						
					</tr>
					<tr>
						<td class="label">Employee Name:
						</td>						
						<td>
						<input type="text" name="empname" value="<%=empInfo.getEmployeeName()%>"  readonly="readonly" tabindex="1"/>
						</td>
						<td class="label">Designation:</td>
						<td><input type="text" name="empDesegnation" value="<%=empInfo.getDesignation()%>" readonly="readonly"  onkeyup="return limitlength(this, 20)"/>
					 
						</td>
						
					</tr>
                    <tr>	<td class="label">
									Employee Code:
								</td>
								<td>
									<input type="text" name="empcode" value="<%=empInfo.getEmployeeNumber()%>" readonly="readonly"  />
								</td>
								<td class="label">
									CPFAC No:
								</td>
								<td>
									<input type="text" name="cpfaccno" value="<%=empInfo.getCpfAccno()%>" readonly="readonly" />
								</td>
							</tr>
							 
					<tr>
					 <td class="label">Date Of Birth:</td>
						<!--<td><select id="daydropdown" name="dayField" style="width:50px"></select><select id="monthdropdown" name="monthField" style="width:50px"></select><select id="yeardropdown" name="yearField" style="width:50px"></select> </td> -->
                    
					<td><input type="text" name="dob" tabindex="9" value="<%=empInfo.getDateOfBirth()%>"  readonly="readonly" />
										
										</td>

                   <td class="label">
									Date of Joining:</td>
				<td>
									
								    <input type="text" name="doj" tabindex="9" value="<%=empInfo.getDateOfJoining()%>" readonly="readonly" />
										
				</td>

					</tr>
							<tr>	
								<td class="label">
							 		Father's / Husband's Name:
									
								</td>
								<td>
									<input type="text" name="fhname" value="<%=empInfo.getFhName()%>" readonly="readonly" />
								</td>
								<td class="label">
									Pension Option:
								</td>
								<td>
									<input type="text" name="pensionoption" value="<%=empInfo.getWetherOption()%>" readonly="readonly" />
								</td>
							</tr>
							<tr>
						<td class="label">Gender:</td>
						<td>
									<input type="text" name="gender" value="<%=empInfo.getGender()%>" readonly="readonly" />
								</td>
						<td class="label">Pension Type:<font color="red">*</font></td>
						<td ><select name="pensiontype" tabindex="7" style="width:130px">						
								<option value='<%=pensionType%>'><%=pensionTypeVal%></option>
								<option value='<%=pensionType2%>'><%=pensionTypeVal2%></option>
								<option value='<%=pensionType3%>'><%=pensionTypeVal3%></option>
							</select>
							
						</td>
						</tr>
						<tr>
						<td class="label">Date Of Case Sent to CHQ:<font color="red">*</font>
						</td>
						<td><input type="text" name="docchq" value="<%=empInfo.getDostochq()%>" onkeyup="return limitlength(this, 20)">
						<a href="javascript:show_calendar('forms[0].docchq');"><img src="<%=basePath%>PensionView/images/calendar.gif"  alt="Calender" border="no" /></a>
						</td>
						<td class="label">Mobile No:<font color=red>*</font></td>
						<td><input type="text" name="mblno" value="<%=empInfo.getPhoneNumber()%>" onkeypress="numsDotOnly()" maxlength="10" >
					 
						</td>
					</tr>
					<tr><td class="label">Email Id:<font color=red>*</font></td>
						<td>												
						<input type="text" name="emailid"  value="<%=empInfo.getEmailID()%>" style="width:130px">
					 <td class="label">	Remarks: 
											</td>
											<td colspan="3">
												<TEXTAREA  NAME="remarks"  maxlength="150"><%=empInfo.getRemarks()%></TEXTAREA></td>
					 
						
						</tr>
	<tr>
	<td >&nbsp;</td>
	</tr>

					<tr ><td >&nbsp;</td></tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td>
								<input type="button" class="btn" value="Update" class="btn" onclick="validate();">
								<input type="button" class="btn" value="Reset" onclick="javascript:resetParams('<%=empInfo.getPensionNo()%>','<%=empInfo.getProcessID()%>','<%=empInfo.getVerifiedBy()%>')" class="btn">
								<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
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
		</table>
	<% } %>
	</form>
		
  </body>
</html>