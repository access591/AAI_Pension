<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    
                    String[] year = {"1995-96","1996-97","1997-98","1998-99","1999-00","2000-01","2001-02","2002-03","2003-04","2004-05","2005-06","2006-07","2007-08","2008-09","2009-10","2010-11"};
                    String[] userYears = {"1995-96","1996-97","1997-98","1998-99","1999-00","2000-01","2001-02","2002-03","2003-04","2004-05","2005-06","2006-07","2007-08","2008-09","2009-10","2010-11"};	
                    
     String frmName="",empSerialNo="",empName="",adjFlag="",accessCode="";
      if(request.getAttribute("frmName")!=null){
      frmName=(String)request.getAttribute("frmName");
      }  
      
    if(frmName.equals("adjcorrections")){
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
            %>

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>

<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript">
var adjFlag='<%=adjFlag%>',frmName='<%=frmName%>';
var accessCode='<%=accessCode%>';
 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
}
 
 	function test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,chkpfid){
			document.forms[0].empName.value=employeeName;
			document.forms[0].empserialNO.value=empSerialNo;
		  	document.forms[0].chk_empflag.checked=true;		
		  	document.forms[0].chkpfid.value = chkpfid;	
		}
	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput";
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
		   href=mylink+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag;
		   
		else
		href=mylink.href+"?transferStatus="+transfer+"&region="+regionID+"&adjPfidChkFlag="+adjPfidChkFlag;
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		/*retricting loading pfids list*/
		document.forms[0].select_region.value = "NO-SELECT";
		document.forms[0].select_airport.length = 1;
		document.forms[0].select_pfidlst.length=1;
		return true;
		}
 


	function validateForm() {
		var regionID="",airportcode="",params="",sortingOrder="",empName="",reportType="",formatType="",url="",yearID="",monthID="",yearDesc="",formType="",toYearID="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var transferFlag="",pfidStrip="";	
		reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		formatType=document.forms[0].select_formatType.options[document.forms[0].select_formatType.selectedIndex].text;



	
		if(reportType=='[Select One]'){
			alert('Please Select the Report Type');
			document.forms[0].select_reportType.focus();
			return false;
		}
		
		if(formatType=='[Select One]'){
			alert('Please Select the Format Type');
			document.forms[0].select_formatType.focus();
			return false;
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


		

		var empserialNO=document.forms[0].empserialNO.value;
		if(empserialNO=='' ){
			
		pfidStrip=document.forms[0].select_pfidlst.options[document.forms[0].select_pfidlst.selectedIndex].value;

		}else{
		if(empserialNO!=''){
			pfidStrip='NO-SELECT'; 
		}else{
			pfidStrip='1 - 1'; 
		}
		}


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
		pfidStrip='NO-SELECT';	 
		document.forms[0].select_pfidlst.length=1;
		document.forms[0].select_pfidlst.value = "NO-SELECT";
			  			empName=document.forms[0].empName.value;
        				frm_empflag=true;
       	}else{
	       		  frm_empflag=false;
       	}
		if(airportcode=='NO-SELECT'){
				airportcode='';
		}
		var normalForm7psFlag ="false";
		if(frmName==""){		 
		if(document.forms[0].chkpfid.value=="Exists"){
		var comfirmMsg=confirm("This Pfid is already done at Adj Calculation Screen.Click Ok For AdjCalculator Modified Statement Of wages and Cancel for Normal Statement Of wages Report");
			if (comfirmMsg== true){ 
	    		frmName ="adjcorrections" ;
    			adjFlag="true";
    		 }else{
		        normalForm7psFlag="true";
		     }
		} 
		}else{
		 if (frmName=="adjcorrections" || frmName=="form7/8psadjcrtn"){		 	  
    		  adjFlag="true";
		   }else{
		 	normalForm7psFlag="true";
		   }
		}
		 
	if( normalForm7psFlag=="true"){
	 	adjFlag="false";
		   frmName ="" ;
	}
	 bulkPrintFlag ="false";
		params = "&frm_region="+regionID+"&frm_reportType="+reportType+"&frm_formatType="+formatType+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+empserialNO+"&frm_pfids="+pfidStrip+"&sortingOrder="+sortingOrder+"&frmAirportCode="+airportcode+"&frm_blkprintflag="+bulkPrintFlag+"&adjFlag="+adjFlag+"&frmName="+frmName;
		url="<%=basePath%>reportservlet?method=statmentpcwagesreport"+params;
		//alert(url);
		 frmName ="";
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   		}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	   		
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
		var transferFlag,airportcode,regionID,yearID,frm_ltstmonthflag,monthID;
		if(document.forms[0].select_region.selectedIndex>0){
			regionID=document.forms[0].select_region.options[document.forms[0].select_region.selectedIndex].text;
		}else{
			regionID=document.forms[0].select_region.value;
		}
		createXMLHttpRequest();	
		if(param=='airport'){
			var url ="<%=basePath%>psearch?method=getPFCardAirports&region="+regionID;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getAirportsList;
		}else{
			if (document.forms[0].chk_empflag.checked==false){
			if(document.forms[0].select_airport.length>1){
				airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				airportcode=document.forms[0].select_airport.value;
			}
		
			var formPageSize=document.forms[0].frmpagesize.value;
		bulkPrintFlag="false";
			var url ="<%=basePath%>psearch?method=getPFIDList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_pagesize="+formPageSize;
		
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
		    //alert(stype.length);	
		  	obj1.options.length = 0;
		  	var chkLength=false;
		  	if(stype.length==1){
		  		chkLength=true;
		  	}else{
		  	obj1.options[obj1.options.length]=new Option('[Select One]','NO-SELECT','true');
		  	}
		  	
		  	for(i=0;i<stype.length;i++){
		  	
		       		if(chkLength==true){
		       			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		       		}else{
		       		

		       			obj1.options[obj1.options.length] = new Option(getNodeValue(stype[i],'airPortName'),getNodeValue(stype[i],'airPortName'));
		       		}
					
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
			      	var stype1 = xmlHttp.responseXML.getElementsByTagName('PARAMSTR');
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
	document.forms[0].chk_empflag.checked=false;	
	}
		 process.style.display="none";
	}
 
function  gotoback(pensionno,frname){ 
var frmName='<%=frmName%>'; 
var url;
if(frname=='adjcorrections'){
 if(accessCode=="PE040201"){
url= "<%=basePath%>reportservlet?method=loadPCReportForAdjDetails&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode;
}else if(accessCode=="PE040204"){
url= "<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040204&searchFlag=S&frmName="+frmName+"&empsrlNo="+pensionno+"&accessCode="+accessCode;
} 
 }else{
url= "<%=basePath%>reportservlet?method=loadRevisedForm78PsAdjObCrtn&frmName="+frmName+"&empsrlNo="+pensionno;
 

}
  	//alert(url);
  	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }
</script>
	</HEAD>
	<body class="BodyBackground" onload="javascript:frmload()">
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
		<form action="post">
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

			<table width="65%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						 Statement Of Wages & Pension Contribution Report Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>

		
				<tr>
																	<td class="label" align="right">
																		Region:<font color=red></font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_region" style="width:120px" onChange="javascript:getAirports('airport');">
																			<option value="NO-SELECT">
																				[Select One]
																			</option>
																			<%while (regionIterator.hasNext()) {
																			region = hashmap.get(regionIterator.next()).toString();

																			%>
																			<option value="<%=region%>" >
																				<%=region%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>
																</tr>
																<tr>
																	<td class="label" align="right">
																		Airport Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<SELECT NAME="select_airport" style="width:120px" >
																			<option value='NO-SELECT' Selected>
																				[Select One]
																			</option>
																		</SELECT>
																	</td>
																</tr>
				<tr>
					<td class="label" align="right">Page Size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td><input type="text" name="frmpagesize" tabindex="3" value="100"></td>
				</tr>
				<tr>
					<td class="label" align="right">
						Employee Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<input type="text" name="empName" readonly="readonly" tabindex="3">
						<img src="<%=basePath%>/PensionView/images/search1.gif" onclick="popupWindow('<%=basePath%>PensionView/PensionContributionMappedListWithoutTransfer.jsp','AAI');" alt="Click The Icon to Select EmployeeName" />
						<input type="checkbox" name="chk_empflag">
             	</tr>
             	
             	
             	<tr>
					<td class=label align="right" nowrap>
						Format Type:<font color=red>*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<SELECT NAME="select_formatType" style="width:88px" onChange="javascript:getAirports('data')">
								<option value='NO-SELECT' Selected>[Select One]</option>
							<option value="Normal">
								Normal
							</option>
							<option value="Arrear">
								Arrear
							</option>
							<option value="WithoutArrear">
								WithoutArrear
							</option>
						</SELECT>
					</td>
				</tr>
             	

				<tr>
					<td class=label align="right" nowrap>
						Report Type:<font color=red>*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</td>
					<td>
						<SELECT NAME="select_reportType" style="width:88px" onChange="javascript:getAirports('data')">
								<option value='NO-SELECT' Selected>[Select One]</option>
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
							<td class="label" align="right">PF ID Printing List:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>
							<td>									
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

				<input type="hidden" name="empserialNO" />
				<input type="hidden" name="chkpfid" />
				



				<tr>
					<td align="center" colspan="2">
						<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
						<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
						<%if(frmName.equals("adjcorrections") || frmName.equals("form7/8psadjcrtn")){%>
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="gotoback('<%=empSerialNo%>','<%=frmName%>');">
					   <%}else{%>
						<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
					  <%}%>
                   </td>
				</tr>
				<tr>
					<td align="center"></td>
				</tr>
			</table>



		</form>
		<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			<SPAN class="label" >Processing.......</SPAN>
		</div>

	</body>
</html>
