<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					
	String forms[] = {"FORM-8-PS","FORM-8","FORM-8-PS-VERFICATION","FORM-8-PS-VERFICATION[REV]","FORM-8-PS-REVISED"};
	String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021","2022"};				
	String frmName="",empSerialNo="",empName="",adjFlag="",accessCode="";
      if(request.getAttribute("frmName")!=null){
      frmName=(String)request.getAttribute("frmName");
      }  
      
    if((frmName.equals("adjcorrections")) ||(frmName.equals("form7/8psadjcrtn"))){
    adjFlag="true";
    }else{
    adjFlag="false";
    }
       if(request.getAttribute("empSerialNo")!=null){
      empSerialNo=(String)request.getAttribute("empSerialNo");
      }   
           if(request.getAttribute("empName")!=null){
      empName=(String)request.getAttribute("empName");
      }  
       if(request.getAttribute("accessCode")!=null){
   accessCode=(String)request.getAttribute("accessCode");
  } 
       String rptformtype="";
	   if(request.getAttribute("rptformType")!=null){
		   rptformtype=(String)request.getAttribute("rptformType");
		   System.out.println("======================="+rptformtype);
	   }
	     System.out.println("======================="+frmName+"=="+empSerialNo+"==empName=="+empName);
      		%>

<%@ page language="java" import="java.util.*,java.sql.Connection,aims.bean.DatabaseBean,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.EmployeePersonalInfo"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.bean.*"%>
<html>
<HEAD>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

<script type="text/javascript">
var adjFlag='<%=adjFlag%>',frmName='<%=frmName%>';
var accessCode='<%=accessCode%>';
function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,chkpfid,dateofJoining,pcreportverified,statemntRevisdChkPfid,PCAftrSepFlag){
			document.forms[0].txt_empname.value=employeeName;
			document.forms[0].frm_pensionno.value=empSerialNo;
			document.forms[0].chk_empflag.checked=true;
		    document.forms[0].chkpfid.value = chkpfid;	
		    document.forms[0].PCAftrSepFlag.value = PCAftrSepFlag;	
		  			
		}

function popupWindow(mylink, windowname)
		{
		
		//var transfer=document.forms[0].transferStatus.value;
		var transfer="";
	
		var regionID="",adjPfidChkFlag="true";
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&frmName=form8ps";
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&frmName=form8ps";
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		/*retricting loading pfids list*/
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		document.forms[0].select_pfidlst.length=1;
		return true;
		}
 function LoadWindow(params){
    //alert("params"+params);
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
	
	function resetReportParams(){
		var url="";
		url = "<%=basePath%>reportservlet?method=loadform8params&frmName="+frmName;
		//alert(url);
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	
	function validateForm(user) {
		var reportType="",url="",empName="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		
       // if(user!='SEHGAL' && user!='navayuga'&&user!='BALWANT'&&user!='SWATI'&& user!='SHIKHA' && user!='NAGARAJ'){
       //   alert("' "+user+" ' User Not allowed to Access the Reports");
       //   return false;
      //   }
         if(document.forms[0].select_year.selectedIndex<1 && document.forms[0].formType.value!='FORM-8-PS-REVISED' && document.forms[0].formType.value!='FORM-8' &&  document.forms[0].formType.value!='FORM-8-PS')
   		 {
   		  alert("Please Select the Year");
   		  document.forms[0].select_year.focus();
   		  return false;
   		  } 
        
         var index=document.forms[0].select_region.selectedIndex;
      	 if(document.forms[0].select_region.selectedIndex<1 && document.forms[0].formType.value!='FORM-3-ALL ' && document.forms[0].formType.value!='FORM-3-PS-PFID' && document.forms[0].formType.value!='FORM-3' && document.forms[0].formType.value!='Duplicate FORM-3' && document.forms[0].formType.value!='FORM-6'&& document.forms[0].formType.value=='FORM-8')
   		 {
   		  alert("Please Select the Region");
   		  document.forms[0].select_region.focus();
   		  return false;
   		  } 
   		
    
       
		if(document.forms[0].formType.value==''){
			alert('Please Select Form Type');
			document.forms[0].formType.select();
			return false;
		 } 
		 
		 else{
	
			var airportcode="";
			
			if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
			}else{
			regionID=document.forms[0].select_region.value;
			
			}
		
			if(document.forms[0].select_airport.length>1){
			airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
			
			airportcode=document.forms[0].select_airport.value;
			}
			
			monthID=document.forms[0].select_month.selectedIndex;
			reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
			yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
			var sortingOrder=document.forms[0].sortingOrder.value;
			if(sortingOrder==''){
			sortingOrder="cpfaccno";
			}

			var params = "?region="+regionID+"&month="+monthID+"&year="+yearID+"&frm_reportType="+reportType+"&airportCD="+airportcode;

			var formtype=document.forms[0].formType.value;
			
			if(monthID==0){
				monthID='NO-SELECT';
			}
			
			var pensionno=document.forms[0].frm_pensionno.value;
			var formType=document.forms[0].formType.value;
			if(document.forms[0].txt_empname.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Please Checked Employee Name');
			  		document.forms[0].txt_empname.focus();
			  		return false;
			  }
			  if (document.forms[0].chk_empflag.checked==true){
			  
			  if(document.forms[0].txt_empname.value==''){
				alert('Please Select the Employee');
				document.forms[0].txt_empname.focus();
				return false;
				}
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		regionID = "NO-SELECT";
		airportcode = "NO-SELECT";
		pfidStrip  = "NO-SELECT";
		document.forms[0].select_pfidlst.length=1;
		document.forms[0].select_pfidlst.value = "NO-SELECT";
		
			  			empName=document.forms[0].txt_empname.value;
        				frm_empflag=true;
       		  }else{
	       		  frm_empflag=false;
       		  }
			
			  if(pensionno=='' && document.forms[0].select_region.selectedIndex<1 && document.forms[0].select_year.selectedIndex<1){
						  alert("Please Select Either Year or Region");
   		 				  document.forms[0].select_year.focus();
   		                  return false;
			}
			if(pensionno=='' ){
				pfidStrip=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].value;
			}else{
				if(pensionno!=''){
					pfidStrip='NO-SELECT'; 
				}else{
				     pfidStrip='1 - 1'; 
				}
			}
			 
			if(formType=='FORM-8-PS-VERFICATION'){			
			 if ((pensionno !='')||(frm_empflag==true && empName !='')){			  			 
			alert('This Report Only For Bulk Printing Purpose');
			document.forms[0].frm_pensionno.value='';
			document.forms[0].txt_empname.value='';
			document.forms[0].chk_empflag.checked=false;				 
			return false;
			}
			}
			
			if(formType!='FORM-8-PS-VERFICATION'){	
			if(pensionno=='' &&(pfidStrip=='1 - 1' || pfidStrip=='NO-SELECT')){
				alert("Please Select either PF ID or PF ID Printing List");
				return false;
			} 
			}else{
			 if(pfidStrip=='1 - 1' || pfidStrip=='NO-SELECT'){
				alert("Please Select  PF ID Printing List");
				return false;
			} 
			}
			
		var normalForm7psFlag ="false",pcFlag="false";
		if(frmName==""){		 
		if(document.forms[0].chkpfid.value=="Exists"){
		var comfirmMsg=confirm("This Pfid is already done at Adj Calculation Screen.Click Ok For AdjCalculator Modified Form 8 Ps and Cancel for Normal Form 8 Ps Report");
		//alert(comfirmMsg);
		if (comfirmMsg== true){ 
	    frmName ="adjcorrections" ;
    	adjFlag="true";
    	}else{
    	 pcFlag = "true";
		  normalForm7psFlag="true";
		}
		}else{
		 pcFlag = "true";
		 normalForm7psFlag="true";
		}
		
		if(pcFlag=="true"){			
			if(document.forms[0].PCAftrSepFlag.value=="Exists"){
				var comfirmMsg=confirm("This Pfid is Having Pension Contribution After Seperation .Click Ok For PC After Separation Form 8 Ps Report and Cancel for Normal Form 8 Ps Report");
				if (comfirmMsg== true){ 
				   
			    	pcFlag="true";
		    	}else{
				pcFlag="false";
				}
				}else{
				pcFlag="false";
				}
			}
		
		}else{
			if (frmName=="adjcorrections"){
		  		frmName ="adjcorrections" ;
    			adjFlag="true";
			}else if (frmName=="form7/8psadjcrtn"){
		  		frmName ="form7/8psadjcrtn" ;
    			adjFlag="true";
    		}else{
		 		normalForm7psFlag="true";
			}
		
		}
		 
	if( normalForm7psFlag=="true"){
	 	adjFlag="false";
		   frmName ="" ;
	}
			
			
			url = "<%=basePath%>reportservlet?method=loadForm8&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+pensionno+"&frm_airportcd="+airportcode+"&frm_formType="+formType+"&frm_pfids="+pfidStrip+"&adjFlag="+adjFlag+"&frmName="+frmName+"&pcFlag="+pcFlag;
			
			frmName ="";
			
       		 if(document.forms[0].txt_empname.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Employee Name Should be checked ');
			  		document.forms[0].txt_empname.focus();
			  		return false;
			  }
				
			//alert(url);	
				if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 						//alert("url "+url);	
   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
   	 				 		winOpened = true;
							wind1.window.focus();
   	 			}
			
	

			return false;
			
		}
		//return false;
	}


	function callReport(){
	  
		var monthID,regionID,yearID,dateString,monthName,selectedInputParam,path;
		monthID=document.forms[0].select_month.selectedIndex;
		monthName=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		monthID="0"+monthID;
		}
		path="<%=basePath%>reportservlet?method=getform3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID;
	//	alert(path);
		document.forms[0].action=path;
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	function createXMLHttpRequest()
	{
	if(window.ActiveXObject)
	 {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	 }
	else if (window.XMLHttpRequest)
	 {
		xmlHttp = new XMLHttpRequest();
	 }
	 }
	function getAirports(param){
		
		monthID=document.forms[0].select_month.options[document.forms[0].select_region.selectedIndex].value;
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		monthID="0"+monthID;
		}
	createXMLHttpRequest();	
	
	
	if(param=='airport'){
			var url ="<%=basePath%>reportservlet?method=getAirports&region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID;;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}else{
		if (document.forms[0].chk_empflag.checked==false){
			if(document.forms[0].select_year.selectedIndex>0){
				yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
			}else{
				yearID=document.forms[0].select_year.value;
			}
			monthID='NO-SELECT';
			if(document.forms[0].select_airport.length>1){
				airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				airportcode=document.forms[0].select_airport.value;
			}
			var formPageSize=document.forms[0].frmpagesize.value;
			frm_ltstmonthflag="false";
			formType=document.forms[0].formType.options[document.forms[0].formType.selectedIndex].text;
			if (formType=='FORM-8-PS-REVISED'){
			var url ="<%=basePath%>psearch?method=getPFIDForm78RevisedList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_month="+monthID+"&frm_year="+yearID+"&frm_ltstmonthflag="+frm_ltstmonthflag+"&frm_pagesize="+formPageSize;;
			}else{
			var url ="<%=basePath%>psearch?method=getPFIDListWthoutTrnFlag&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_month="+monthID+"&frm_year="+yearID+"&frm_ltstmonthflag="+frm_ltstmonthflag+"&frm_pagesize="+formPageSize;;
			}
			
			
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getPFIDNavigationList;
		}
		}
	xmlHttp.send(null);
    }
 function getNodeValue(obj,tag)
   {
	return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
   }
function getAirportsList()
{
	
	if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
		 process.style.display="block";
	}
	if(xmlHttp.readyState ==4)
	{
		
		if(xmlHttp.status == 200)
		{ 
		var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
		 process.style.display="none";
		  if(stype.length==0){
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_airport");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_airport");
		  //    alert(stype.length);	
		  	obj1.options.length = 0;
		  	
		  	for(i=0;i<stype.length;i++){
		  		if(i==0)
					{
				//	alert("inside if")
					obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}
		          //	alert("in else");
			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
			}
		  }
		}
	}
}
function getPFIDNavigationList()
	{
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			 process.style.display="block";
		}
		if(xmlHttp.readyState ==4)
		{
			if(xmlHttp.status == 200)
				{ 
			      	var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					 process.style.display="none";
					  if(stype.length==0){
		 //	alert("in if");
		 	var obj1 = document.getElementById("select_pfidlst");
		 
		 	
		  	obj1.options.length=0; 
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		 
		  
		  }else{
		   	var obj1 = document.getElementById("select_pfidlst");
		  //    alert(stype.length);	
		  	obj1.options.length = 0;
		  	
		  	for(i=0;i<stype.length;i++){
		  		if(i==0)
					{
				//	alert("inside if")
					obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
					}
		          //	alert("in else");
			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
			}
		  }
		}
	}

	 
}
function frmload(){
var  empSerialNo='',formName='',empName='';
	empSerialNo='<%=empSerialNo%>';
	formName='<%=frmName%>';
	empName='<%=empName%>';
	document.forms[0].frm_pensionno.value =  empSerialNo ;
	document.forms[0].txt_empname.value = empName;
	if(empName!=""){
	document.forms[0].chk_empflag.checked=true;		
	}
	 
 process.style.display="none";
}

function  gotoback(pensionno){ 
var frmName='<%=frmName%>',url=""; 
 if(accessCode=="PE040201"){
url= "<%=basePath%>reportservlet?method=loadPCReportForAdjDetails&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode;
}else if(accessCode=="PE040204"){
url= "<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040204&searchFlag=S&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode;
}

if(frmName=="form7/8psadjcrtn"){
url ="<%=basePath%>PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp";
}
	//alert(url);
  	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
</script>
</HEAD>
<body class="BodyBackground" onload="javascript:frmload()">
<%
	String monthID="",yearDescr="",region="",monthNM="";
	Iterator monthIterator=null;
  	Iterator yearIterator=null;
  	ArrayList pfidList = new ArrayList();
  	Iterator regionIterator=null;
  		HashMap hashmap=new HashMap();
  	if(request.getAttribute("regionHashmap")!=null){
  	hashmap=(HashMap)request.getAttribute("regionHashmap");
  	Set keys = hashmap.keySet();
	regionIterator = keys.iterator();
  	
  	}
  	if(request.getAttribute("monthIterator")!=null){
  	monthIterator=(Iterator)request.getAttribute("monthIterator");
  	}
  	if(request.getAttribute("yearIterator")!=null){
  	yearIterator=(Iterator)request.getAttribute("yearIterator");
  	}
  	      if (request.getAttribute("pfidList") != null) {
            	pfidList = (ArrayList) request.getAttribute("pfidList");
            }
%>
			<form action="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td width="100%">
						<jsp:include page="/PensionView/PensionMenu.jsp" />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr> 				<!-- <tr>
											<td height="28" align="center" class="ScreenMasterHeading">
												Form - 8 Report Params
											</td>
										</tr>-->
									
										<tr>
											<td>
												<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
												
												<tr>
											<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
												Form - 8 Report Params
											</td>
										</tr>
										
													<tr>
														<td>
															<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
																<tr>
																	<td>
																	&nbsp;
																	</td>
																</tr>
																<tr>
																	<td width="50%" class="label" align="right">
																		Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%">
																		<Select name='select_year' Style='width:120px'>
																			<option value='No-SELECT'>
																				Select One
																			</option>
																			<%for (int j = 0; j < year.length; j++) {%>
																			<option value='<%=year[j]%>'>
																				<%=year[j]%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>

																</tr>
																<tr>
																	<td  width="50%" class="label" align="right">
																		Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<select name="select_month" style="width:120px">
																			<Option Value='' Selected>
																				Select One
																			</Option>
																			<%while (monthIterator.hasNext()) {
																			Map.Entry mapEntry = (Map.Entry) monthIterator.next();
																			monthID = mapEntry.getKey().toString();
																			monthNM = mapEntry.getValue().toString();

				%>

																			<option value="<%=monthID%>">
																				<%=monthNM%>
																			</option>
																			<%}%>
																		</select>
																	</td>
																</tr>
																<tr>
																	<td width="50%"  class="label" align="right">
																		Region:<font color=red>*</font>  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<SELECT NAME="select_region" style="width:120px" onChange="javascript:getAirports('airport');">
																			<option value="NO-SELECT">
																				[Select One]
																			</option>
																			<%while (regionIterator.hasNext()) {
																				region = hashmap.get(regionIterator.next()).toString();

																			%>
																			<option value="<%=region%>">
																				<%=region%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>
																</tr>
																<tr>
																	<td  width="50%" class="label" align="right">
																		Airport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<SELECT NAME="select_airport" Style='width:120px'>
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
<tr>
					<td  width="50%" class="label" align="right">Page Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td width="50%" ><input Style='width:120px' type="text" name="frmpagesize" tabindex="3" value="100"></td>
				</tr>
															<tr>
																	<td  width="50%" class="label" align="right">
																		Employee Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<input Style='width:120px' type="text" value="" name="txt_empname">
																		<input type="hidden" name="frm_pensionno">
																		<input type="hidden" name="chkpfid" />
																		<img src="<%=basePath%>/PensionView/images/search1.gif" onclick="popupWindow('<%=basePath%>PensionView/PensionContributionMappedListWithoutTransfer.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
																		<input type="checkbox" name="chk_empflag" >
																		<input type="hidden" name="PCAftrSepFlag" />
																	</td>
																</tr>
																<tr>
																	<td width="50%"  class=label align="right" nowrap>
																		Form Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<Select name='formType' Style='width:120px' align="left">		
																							<%if(rptformtype.equals("---")){
						%> 
	<option value='FORM-8'>FORM-8</option>
																		<option value='FORM-8-PS' selected="selected">FORM-8-PS</option>
																		<option value='FORM-8-PS-VERFICATION'>FORM-8-PS-VER</option>
																		<option value='FORM-8-PS-VERFICATION[REV]'>FORM-8-PS-VER[REV]</option>
																		<option value='FORM-8-PS-REVISED'>FORM-8-PS-REV</option>
						<%}else{%>
						<option value="FORM-8-PS" <%if(rptformtype.equals("FORM-8-PS")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-8-PS")){ out.println("selected");}%>">FORM-8-PS</option>
						<option value="FORM-8-PS-VERFICATION" <%if(rptformtype.equals("FORM-8-PS-VERFICATION")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-8-PS-VERFICATION")){ out.println("selected");}%>">FORM-8-PS-VER</option>
						<option value="FORM-8-PS-VERFICATION[REV]" <%if(rptformtype.equals("FORM-8-PS-VERFICATION[REV]")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-8-PS-VERFICATION[REV]")){ out.println("selected");}%>">FORM-8-PS-VER[REV]</option>
						<option value="FORM-8-PS-REVISED" <%if(rptformtype.equals("FORM-8-PS-REVISED")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-8-PS-REVISED")){ out.println("selected");}%>">FORM-8-PS-REV</option>
					
						<%}%>															  
																	
																		 	 
																		</SELECT>
																	</td>
																</tr>
																<tr>
																	<td  width="50%"  class=label align="right" nowrap>
																		Report Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																			<SELECT NAME="select_reportType" style="width:120px" onChange="javascript:getAirports('data')">
																			 
																			<option value="html">
																				Html
																			</option>
																			<option value="ExcelSheet">
																				Excel Sheet
																			</option>
																			
																		</SELECT>
																	
																	</td>
																</tr>
																<tr>
							<td  width="50%" class="label" align="right">PF ID Printing List:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
							<td width="50%" >									
								<SELECT NAME="select_pfidlst" style="width:120px" >
								
							  	<option value='NO-SELECT' Selected>[Select One]</option>
	                       		<%for (int pfid = 0; pfid < pfidList.size(); pfid++) {%>
									<option value='<%=pfidList.get(pfid)%>'><%=pfidList.get(pfid)%></option>
								<%}%>
								</SELECT>
							</td>
						</tr>
				<tr>
																<tr>
																	<td  width="50%" class=label align="right" nowrap>
																		Sorting Order: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<SELECT NAME="sortingOrder" style="width:120px">
																			<option value="">
																				[Select One]
																			</option>
																			<option value="cpfacno">
																				CpfAccno
																			</option>
																			<option value="employeename">
																				EmployeeName
																			</option>
																		
																			<option value="empserialnumber">
																				PFID
																			</option>
																		</SELECT>
																	</td>
																</tr>
																	
																	<tr>
																		<td>
																		&nbsp;
																		</td>
																	</tr>
																<tr>
																
																		<td colspan="3" align="center">
									 									<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm('<%=session.getAttribute("userid")%>')">
																		<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
																		<%if((frmName.equals("adjcorrections")) || (frmName.equals("form7/8psadjcrtn"))){%>
																		<input type="button" class="btn" name="Submit" value="Cancel" onclick="gotoback('<%=empSerialNo%>');">
																		<%}else{%>
																		<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1);">
																		<%}%>
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
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>
</body>
</html>