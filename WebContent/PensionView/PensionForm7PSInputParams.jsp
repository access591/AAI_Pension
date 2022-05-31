
<%@ page language="java" import="java.util.*,java.util.ArrayList" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    //String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009"};
                    String[] year = {"1995-96","1996-97","1997-98","1998-99","1999-00","2000-01","2001-02","2002-03","2003-04","2004-05","2005-06","2006-07","2007-08","2008-09","2009-10","2010-11","2011-12","2012-13","2013-14","2014-15","2015-16","2016-17","2017-18","2018-19","2019-20","2020-21","2021-22","2022-23"};
    String frmName="",empSerialNo="",empName="",adjFlag="false",accessCode="";
      if(request.getAttribute("frmName")!=null){
      frmName=(String)request.getAttribute("frmName");
      } else{
      frmName="";
      } 
    System.out.println("----frmName---"+frmName);
    
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
	   }
	   System.out.println("======================="+frmName+"=="+empSerialNo+"==empName=="+empName);
            %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<script type="text/javascript"><!--
var adjFlag='<%=adjFlag%>',frmName='<%=frmName%>';
var accessCode='<%=accessCode%>';
 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,chkpfid,dateofJoining,pcreportverified,statemntRevisdChkPfid,PCAftrSepFlag){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].dateofbirth.value=dateofbirth;	
		  	document.forms[0].chk_empflag.checked=true;
		  	document.forms[0].chkpfid.value = chkpfid;	
		  	document.forms[0].PCAftrSepFlag.value = PCAftrSepFlag;		
		}
	
	function resetReportParams(){
	var url ="";
		url = "<%=basePath%>reportservlet?method=loadform7Input&frmName="+frmName;
		//alert(url);
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
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
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&frmName=form7ps";
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag+"&frmName=form7ps";
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		/*retricting loading pfids list*/
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		document.forms[0].select_pfidlst.length=1;
		return true;
		}

	
	function validateForm() {
		var regionID="",dob="",toYearID="",empName="",airportcode="",params="",reportType="",url="",yearID="",monthID="",yearDesc="",formType="",toYearID="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		var empserialNO=document.forms[0].empserialNO.value;
		if(document.forms[0].select_year.selectedIndex<1 && empserialNO=='')
   		 {
   		  	alert("Please Select the Year");
   		  	document.forms[0].select_year.focus();
   		  	return false;
   		 } 
		if(document.forms[0].select_year.selectedIndex>0){
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
		yearID=document.forms[0].select_year.value;
		}
		if(yearID!='NO-SELECT'){
			var splitYearID = yearID.split("-");
			yearID=splitYearID[0];
			if(yearID.substring(0,2)=='19' && yearID.substring(2,4)<'99'){
				toYearID=yearID.substring(0,2)+splitYearID[1]
			}else if(yearID.substring(0,2)=='20'){
				toYearID=yearID.substring(0,2)+splitYearID[1]
			}else if(yearID.substring(0,2)=='19' && yearID.substring(2,4)=='99'){
				toYearID=2000;
			}
		}else{
			if(yearID=='NO-SELECT' && empserialNO==''){
				alert('Please Select the Year');
				document.forms[0].select_year.focus();
				return false;
			}
		}
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
		
		if(document.forms[0].select_month.selectedIndex>0){
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
		monthID=document.forms[0].select_month.value;
		}

		
		
		if(empserialNO=='' ){
		pfidStrip=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].value;
		}else{
		if(empserialNO!=''){
			pfidStrip='NO-SELECT'; 
		}else{
		
			pfidStrip='1 - 1'; 
		}
		}

		dob=document.forms[0].dateofbirth.value;
		formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
			if(document.forms[0].empName.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Please Checked Employee Name');
			  		document.forms[0].empName.focus();
			  		return false;
		}
		if (document.forms[0].chk_empflag.checked==true){
		if(document.forms[0].empName.value==''){
			alert('Please Select the Employee');
			document.forms[0].empName.focus();
			return false;
		}
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		regionID = "NO-SELECT";
		airportcode = "NO-SELECT";
		pfidStrip  = "NO-SELECT";
		document.forms[0].select_pfidlst.length=1;
		document.forms[0].select_pfidlst.value = "NO-SELECT";
			  			empName=document.forms[0].empName.value;
        				frm_empflag=true;
       	}else{
	       		  frm_empflag=false;
       	}
		if(yearID=='NO-SELECT' && (formType=='FORM-78PS-SUMMARY' || formType=='FORM-7PS-NO-DATA')){
       		alert('Please Select the Year');
				document.forms[0].select_year.focus();
				return false;
       	}else if(yearID!='NO-SELECT' && (formType=='FORM-7PS-SUMMARY' || formType=='FORM-7PS-SUMMARY[REV]')){
       		yearID='NO-SELECT';
       		document.forms[0].select_year.selectedIndex=0;
		  	
       	}
       	 
		var normalForm7psFlag ="false",pcFlag="false";
		if(frmName==""){		 
			if(document.forms[0].chkpfid.value=="Exists"){
				var comfirmMsg=confirm("This Pfid is already done at Adj Calculation Screen.Click Ok For AdjCalculator Modified Form 7 Ps and Cancel for Normal Form 7 Ps Report");
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
			//alert(pcFlag+"=="+document.forms[0].PCAftrSepFlag.value);
			if(pcFlag=="true"){			
			if(document.forms[0].PCAftrSepFlag.value=="Exists"){
				var comfirmMsg=confirm("This Pfid is Having Pension Contribution After Seperation .Click Ok For PC After Separation Form 7 Ps Report and Cancel for Normal Form 7 Ps Report");
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
		params = "&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_reportType="+reportType+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_reportType="+reportType+"&frm_formType="+formType+"&frm_pensionno="+empserialNO+"&frm_pfids="+pfidStrip+"&frm_dob="+dob+"&adjFlag="+adjFlag+"&frmName="+frmName+"&pcFlag="+pcFlag;
	
		if(formType=='FORM-7PS'|| formType=='FORM-78PS-SUMMARY' || formType=='FORM-7PS-SUMMARY' || formType=='FORM-7PS-SUMMARY[REV]' || formType=='FORM-7PS-REVISED'){
			url="<%=basePath%>reportservlet?method=form7report"+params;
		
		}else if(formType=='FORM-7PS-NO-DATA'){
			url="<%=basePath%>reportservlet?method=form7reportzero"+params;
		}else if(formType=='FORM-7PS-INDEXPAGE'){
			url="<%=basePath%>reportservlet?method=form7Indexreport"+params;
		}else{
			url="<%=basePath%>reportservlet?method=payarrears"+params;
		}
		 frmName ="";
		 adjFlag="false";
	 	  //alert(url);
		
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet'||reportType=='DBF' ){
   				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
      	}
		
	}
	 function LoadWindow(params){
    var newParams =params;
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
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
		
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID,formType;
		if(document.forms[0].select_region.selectedIndex>0){
		regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
	}else{
		regionID=document.forms[0].select_region.value;
	}
	createXMLHttpRequest();	
	if(param=='airport'){
		var url ="<%=basePath%>psearch?method=getAirports&region="+regionID;
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = getAirportsList;
	}else{
	if (document.forms[0].chk_empflag.checked==false){
		if(document.forms[0].select_year.selectedIndex>0){
			yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].value;
		}else{
			yearID=document.forms[0].select_year.value;
		}
		
		var formPageSize=document.forms[0].frmpagesize.value;
	    
		if(document.forms[0].select_month.selectedIndex>0){
			monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		}else{
			monthID=document.forms[0].select_month.value;
		}
		if(document.forms[0].select_airport.length>1){
			airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
		}else{
			airportcode=document.forms[0].select_airport.value;
		}
		formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
		frm_ltstmonthflag="false";
		if (formType=='FORM-7PS-REVISED'){
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
	document.forms[0].empserialNO.value = empSerialNo;
	document.forms[0].empName.value = empName;
	if(empSerialNo!=""){
	document.forms[0].chk_empflag.checked=true;	
	}else{
	document.forms[0].empName.value = "";
	}
		 process.style.display="none";
		// alert('-empName--'+empName+'-empSerialNo---'+empSerialNo); 
	}
function  gotoback(pensionno){  
var frmName ='<%=frmName%>',url="",reportYear="",status="";
if(accessCode=="PE040201"){
url= "<%=basePath%>reportservlet?method=loadPCReportForAdjDetails&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode;
}else if(accessCode=="PE040204"){
url= "<%=basePath%>reportservlet?method=searchAdjRecords&searchFlag=S&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode+"&reportYear="+reportYear+"&status="+status;
}

if(frmName=="form7/8psadjcrtn"){
url ="<%=basePath%>PensionView/AdjCrtn/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp";
}
  	//alert(url);
  	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
--></script>
	</HEAD>
			<%String monthID = "", yearDescr = "", region = "", monthNM = "";

            ArrayList yearList = new ArrayList();
            ArrayList pfidList = new ArrayList();
            Iterator regionIterator = null;
            Iterator monthIterator = null;
            HashMap hashmap = new HashMap();
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
                Set keys = hashmap.keySet();
                regionIterator = keys.iterator();

            }
            if (request.getAttribute("monthIterator") != null) {
                monthIterator = (Iterator) request
                        .getAttribute("monthIterator");
            }
            if (request.getAttribute("yearList") != null) {
                yearList = (ArrayList) request.getAttribute("yearList");
            }
            if (request.getAttribute("pfidList") != null) {
            	pfidList = (ArrayList) request.getAttribute("pfidList");
            }
            
            %>
	<body class="BodyBackground" onload="javascript:frmload()">

		<form >
		<table  width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
		<tr>
			<td>
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
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table width="70%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						 Form-7 Report Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td  width="50%" class="label" align="right">
						Year:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<select name='select_year' style='width:100px'>
							<option value='NO-SELECT'>
								Select One
							</option>
					<%for (int j = 0; j < year.length; j++) {%>
																			<option value='<%=year[j]%>'>
																				<%=year[j]%>
																			</option>
																			<%}%>
						</select>
					</td>
				</tr>
				
				<tr>
					<td  width="50%" class="label" align="right">
						Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<select name="select_month" style="width:100px">
							<Option Value='NO-SELECT'>
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
																	<td  width="50%" class="label" align="right">
																		Region:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<select name="select_region" style="width:120px" onchange="javascript:getAirports('airport');">
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
																		</select>
																	</td>
																</tr>
																<tr>
																	<td  width="50%" class="label" align="right">
																		Airport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td width="50%" >
																		<SELECT NAME="select_airport" style="width:120px">
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
<tr>
					<td  width="50%" class="label" align="right">Page Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td width="50%" ><input type="text" name="frmpagesize" tabindex="3" value="100"></td>
				</tr>
			
				<tr>
					<td  width="50%" class="label" align="right">
						Employee Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<input type="text" name="empName" readonly="true" tabindex="3">
						<img src="<%=basePath%>/PensionView/images/search1.gif" onclick="popupWindow('<%=basePath%>PensionView/PensionContributionMappedListWithoutTransfer.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
						<input type="checkbox" name="chk_empflag" />
						<input type="hidden" name="empserialNO" />
						<input type="hidden" name="dateofbirth" />
						<input type="hidden" name="chkpfid" />
						<input type="hidden" name="PCAftrSepFlag" />
					</td>

				</tr>
				<tr>
					<td width="50%"  class=label align="right" nowrap>Form Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" ><select name='select_formType' style='width:150px' >
						<%if(rptformtype.equals("---")){
						%> 
						<option value='FORM-7PS'>FORM-7PS</option>
						<option value='FORM-7PS-SUMMARY'>FORM-7PS-SUMMARY</option>
						<option value='FORM-7PS-SUMMARY[REV]'>FORM-7PS-SUMMARY[REV]</option>
						<option value='FORM-7PS-NO-DATA'>FORM-7PS-NO-DATA</option>
						<option value='FORM-7PS-INDEXPAGE'>FORM-7PS-INDEXPAGE</option>
						<option value='FORM-7PS-REVISED'>FORM-7PS-REVISED</option>
						<option value='FORM-78PS-SUMMARY'>FORM-78PS-SUMMARY</option>
						<%}else{%>
						<option value="FORM-7PS" <%if(rptformtype.equals("FORM-7PS")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS")){ out.println("selected");}%>">FORM-7PS</option>
						<option value="FORM-7PS-SUMMARY" <%if(rptformtype.equals("FORM-7PS-SUMMARY")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS-SUMMARY")){ out.println("selected");}%>">FORM-7PS-SUMMARY</option>
						<option value="FORM-7PS-SUMMARY[REV]" <%if(rptformtype.equals("FORM-7PS-SUMMARY[REV]")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS-SUMMARY[REV]")){ out.println("selected");}%>">FORM-7PS-SUMMARY[REV]</option>
						<option value="FORM-7PS-REVISED" <%if(rptformtype.equals("FORM-7PS-REVISED")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS-REVISED")){ out.println("selected");}%>">FORM-7PS-REVISED</option>
						<option value="FORM-7PS-NO-DATA" <%if(rptformtype.equals("FORM-7PS-NO-DATA")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS-NO-DATA")){ out.println("selected");}%>">FORM-7PS-NO-DATA</option>
						<option value="FORM-7PS-INDEXPAGE" <%if(rptformtype.equals("FORM-7PS-INDEXPAGE")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-7PS-INDEXPAGE")){ out.println("selected");}%>">FORM-7PS-INDEXPAGE</option>
						<option value="FORM-78PS-SUMMARY" <%if(rptformtype.equals("FORM-78PS-SUMMARY")){ out.println("selected");}%>="<%if(rptformtype.equals("FORM-78PS-SUMMARY")){ out.println("selected");}%>">FORM-78PS-SUMMARY</option>												
			
						<%}%>
					</select>
					</td>
				</tr>
				<TR>
					<td width="50%" class=label align="right" nowrap>
						Report Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td width="50%" >
						<SELECT NAME="select_reportType" style="width:88px" onChange="javascript:getAirports('data')">
							<option value="html">Html</option>
							<option value="ExcelSheet">Excel Sheet</option>
							</SELECT>
					</td>
				 
				</TR>
					<tr>
							<td width="50%" class="label" align="right">PF ID Printing List:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
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
					<td align="center">
						&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
				</tr>

				

				<tr>
					<td colspan="3" align="center">
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
						<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
						<%if(frmName.equals("adjcorrections") ||frmName.equals("form7/8psadjcrtn") ){%>
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="gotoback('<%=empSerialNo%>');">
						<%}else{%>
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1);">
						<%}%>
					</td>
				</tr>
				<tr>
					<td align="center"></td>
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
