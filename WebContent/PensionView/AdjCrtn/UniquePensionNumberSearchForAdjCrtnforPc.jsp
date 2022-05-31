<%@ page language="java"%>
<%@ page import="java.util.*,aims.common.*,java.text.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.EmployeePensionCardInfo"%>
<%@ page import="aims.bean.*"%> 
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/tags-display" prefix="display"%>
<%
String path = request.getContextPath();
Calendar cal = Calendar.getInstance(); 
 		 int month = cal.get(Calendar.MONTH)+7;
 		 System.out.println(month);    
		 int year=cal.get(Calendar.YEAR);
		 System.out.println("month "+month +"year "+year);
		 if(month>=12){
			  month=month-12;
			  year = cal.get(Calendar.YEAR)+1; 
		 }
// System.out.println("after month "+month +"after year "+year);
 String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String region="",empsrlNo="",dataFlag="",message="", accessCode="",verifiedby="",userType="",processedStage="",chkPfidTracking="",userId="",blockedYears="",blockOrFrozenflag="";
  	String adjObYears="";
  	CommonUtil common=new CommonUtil();    
	String  notFinalisdYears = "";
   	HashMap hashmap=new HashMap();
	hashmap=common.getRegion();

	Set keys = hashmap.keySet();
	Iterator it = keys.iterator();
	
	if(session.getAttribute("getSearchBean1")!=null){
	session.removeAttribute("getSearchBean1");
	
	}
	if(session.getAttribute("usertype")!=null){
	userType = (String)session.getAttribute("usertype");
	
	}
	 
	 
  if(request.getAttribute("empsrlNo")!=null){
	empsrlNo = (String)request.getAttribute("empsrlNo");
	}
	
	
	
	if(request.getAttribute("searchInfo") == null){
	dataFlag="NoData";
	} 
  	
 String array[] ={"1570","9369","9605","10115","13772","21048"};
 boolean logsFlag = false;
 for(int i=0;i<array.length;i++){
 if(empsrlNo.equals(array[i])){
    logsFlag = true;
    i=array.length;
 }
 }
  if(request.getAttribute("message")!=null){
   message=(String)request.getAttribute("message");
  }
  if(request.getAttribute("accessCode")!=null){
   accessCode=(String)request.getAttribute("accessCode");
  }
  if(request.getAttribute("verifiedby")!=null){
   verifiedby=(String)request.getAttribute("verifiedby");
  }
   if(request.getAttribute("chkPfidTracking")!=null){
   chkPfidTracking=(String)request.getAttribute("chkPfidTracking");
  }
 if(request.getAttribute("adjObYears")!=null){
   notFinalisdYears=(String)request.getAttribute("adjObYears");
  System.out.println("=notFinalisdYears  =="+notFinalisdYears);
  }
  
	if(accessCode.equals("PE040201")){
	processedStage="Initial";
	}else if(accessCode.equals("PE040202")){
		processedStage="Approved";
	  }
	  
 if(request.getAttribute("blockedYears")!=null){
   blockedYears=(String)request.getAttribute("blockedYears");
  }

 if(request.getAttribute("blockOrFrozenflag")!=null){
 blockOrFrozenflag=(String)request.getAttribute("blockOrFrozenflag");
 }
 
	System.out.println("==verifiedby==="+verifiedby+"=="+request.getAttribute("verifiedby")+"==message==="+message+"==userType=="+userType+"=chkPfidTracking="+chkPfidTracking+"brf flag"+blockOrFrozenflag); 
  userId = (String) session.getAttribute("userid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
		<meta http-equiv="description" content="This is my page"/>
		<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css" />
    	<link rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css" />    	 
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></script>
		 
	<script type="text/javascript"> 
	
	var accessCode='<%=accessCode%>',userType='<%=userType%>',userId='<%=userId%>',brfflag='<%=blockOrFrozenflag%>';
	var  verifiedby='<%=verifiedby%>',chkPfidTracking='<%=chkPfidTracking%>',blockedYears='<%=blockedYears%>';; 
	function resetVals(){
	 
	document.forms[0].empsrlNo.value="";
	 
	     
	}
	
	function validateForm(empserialNO,employeeName,dojFlag,reportYear,approvedStage,frozen) {
		//if(reportYear=='2012-2013'){
		//alert(" You Can't Edit 2012-2013 Slot,It Is Under Construction Phase....");
		//return false;
		//}
		var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="",blockedYearResult="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var empserialNO	=empserialNO;
		//reportType="ExcelSheet";
		reportType="Html";
		yearID="NO-SELECT";
		var page="PensionContributionScreen";
		var mappingFlag="true";
        var frm_year="1995";
        var claimsprocess=claimsprocess;
      	var blockedYearsArray = new Array();
        if(claimsprocess=="Y"){
        alert("PensionClaim Process Already Done, User doesn't have Permissions to View/Edit TransactionData");
        return false;
        } 
  		blockedYearsArray = blockedYears.split(":");
  		//alert(blockedYearsArray.length+"=="+blockedYears);
  			for(var i=0;i<blockedYearsArray.length;i++)
  				{
  				if(blockedYearsArray[i]==reportYear){
  				 blockedYearResult  ="Exists";
  				blockedYearsArray.length=0;
  				}else{
  			 blockedYearResult  ="NotExists";
  				}
  			} 
        
         if(blockedYearResult=="Exists"){
              		alert("Claim has Processed For This Pfid");
              		 return false;
              }       
        
        
        //alert(reportYear+"--"+dojFlag);
        if(reportYear=="1995-2008"){
        if(dojFlag=="true"){
        alert(" We cannot provide  add/edit records due to he/she joined after Mar 2008");
        return false;
        }
        }
       //By Radha On 19-Nov-2012 as per Shkiha Request 
    /* if(reportYear=="2010-2011" || reportYear=="2011-2012"){
        if(!(userId=="navayuga" || userId=="SHIKHA" || userId=="Monika" || userId=="SHIKHAJ" || userId=="SUNITAARYA" || userId=="PRADEEP")) {
        alert(" U Don't Have Privilages to Edit "+reportYear+" Data");
        return false;
        }
        }  */
          
             //By Radha On 19-Dec-2012 as per Sehgal  Request thru mail
 		if(reportYear=="1995-2008" || reportYear=="2008-2009" || reportYear=="2009-2010"){
        if(userId=="SURENDER" || userId=="BRIJ") {
        alert(" U Don't Have Privilages to Edit "+reportYear+" Data");
        return false;
        }
        }   
          
        if(accessCode=="PE040201"){
       if(frozen=="N"){
              if(approvedStage=="Initial"){
                    alert("Initial Stage is Finalized.You Cant do Changes");
                     return false;
              } else if(approvedStage=="Approved"){
              		alert("Approval Stage is Finalized.You Cant do Changes");
              		 return false;
              }      
           } 
        } else if(accessCode=="PE040202"){
         	if(verifiedby=="Approved"){
              		alert("Approval Stage is Finalized.You Cant do Changes");
              		 return false;
               }else if(verifiedby=="processing" ||  verifiedby=="Reject"){ 
                  alert("Initial Stage is processing...You Cant do Changes");
              		 return false;
              }else if(verifiedby==""){ 
                  alert("Not Entered in Initial Stage");
              		 return false;
              }
        }  
           
          
        if(frozen=="Y"){
        alert("Form2 already Submitted Please Submit Reverse Adjustment");
        return false;
        }
        //if(reportYear.substr(0,4)>=2016){
            // if(!(userId=="navayuga" || userId=="GOPALPAL")) {
            //alert(" 2016-17 Editing is under Proccessing");
          //  return false;
           //  }
       //alert("PC editing Proccessing");
       //return false;
		//} 
         
		var frm_toyear=<%=year%>;
		var frm_month=<%=month%>;
		var params = "&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_reportType="+reportType+"&empserialNO="+empserialNO+"&page="+page+"&mappingFlag="+mappingFlag+"&frm_year="+frm_year+"&frm_toyear="+frm_toyear+"&frm_month="+frm_month+"&reportYear="+reportYear+"&empName="+employeeName+"&accessCode="+accessCode;
		var url="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc"+params;
		 //alert(url);
		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
		
	}

	function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
    }
	
	function validateForm1(){ 
        alert(" You Can't Edit the Data,Due to Control Accounts Processing....");
    	 return false;
   	 }
	
    function testSS(){ 
        //alert(" You Can't Edit the Data,Due to Control Accounts Processing....");
		
    	 //return false;

    	var formType='',url='',pensionno='',empName='';
    	pensionno=    document.forms[0].empsrlNo.value;    	 
     if(accessCode=="PE040201"){
    	 if(document.forms[0].empsrlNo.value==''){
    	 //alert('Please Enter Employee PF Id ');
    	 document.forms[0].empsrlNo.focus();
    	 return false;
    	 }
    	}
    	 if(accessCode=="PE040201"){    	   
    	  url="<%=basePath%>pcreportservlet?method=loadPCReportForAdjDetailsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
     	}else  if(accessCode=="PE040202"){
     	url="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
    	}
    	 //alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
   	 
   	 function editRecord(employeeNo,empName){   
    	var formType='',url='',pensionno='',empName='';
    
    	url="<%=basePath%>pcreportservlet?method=loadPCReportForAdjDetailsforPc&empsrlNo="+employeeNo+"&empName="+empName+"&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
     	// alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 }
   	 
   	 
     	function editEmpSerailNumber(cpfacno,employeeName,region,airportCode,empSerailNumber,dateofBirth,empCode){
       
    	if(document.forms[0].cpfno.length==undefined){
		if(document.forms[0].cpfno.checked){
		var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth+"&empCode="+empCode;
		alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
    	}
		
		}
		
		for (i = 0; i < document.forms[0].cpfno.length; i++){
		if(document.forms[0].cpfno[i].checked){
				var cpfacno=cpfacno;
		var answer =confirm('Are you sure, do you want edit this record');
	
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=getProcessUnprocessList&cpfacno="+cpfacno+"&name="+employeeName+"&region="+region+"&airportCode="+airportCode+"&empSerailNumber="+empSerailNumber+"&dateofBirth="+dateofBirth;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	else{
		document.forms[0].cpfno[i].checked=false;
		}
	}
	}
	}
   	
   	 function selectMultipule(){
   	 document.getElementById("check1").checked
     var x=document.getElementsByName("cpfno");
      for(var i=0;i<x.length;i++){
     if(document.getElementById("check1").checked==true)
     document.getElementsByName("cpfno")[i].checked=true;
     else  
     document.getElementsByName("cpfno")[i].checked=false;
     }
     
    //  alert("checkBoxes " +checkboxes);
	//	document.forms[0].action="<%=basePath%>search1?method=delete";
	//	document.forms[0].method="post";
	//	document.forms[0].submit();
	}
	   function createXMLHttpRequest()
		{
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
 	function getDelete(empserialno,reportYear,approvedStatus){	
		
		var url ="";
		var blockedYearsArray = new Array();
		blockedYearsArray = blockedYears.split(":");
  		//alert(blockedYearsArray.length+"=="+blockedYears);
  			for(var i=0;i<blockedYearsArray.length;i++)
  				{
  				if(blockedYearsArray[i]==reportYear){
  				 blockedYearResult  ="Exists";
  				blockedYearsArray.length=0;
  				}else{
  			 blockedYearResult  ="NotExists";
  				}
  			} 
        
         if(blockedYearResult=="Exists"){
              		alert("Claim has Processed For This Pfid");
              		 return false;
              }     
		
		
		
        //By Radha On 19-Dec-2012 as per Sehgal  Request thru mail
 		if(reportYear=="1995-2008" || reportYear=="2008-2009" || reportYear=="2009-2010"){
        if(userId=="SURENDER" || userId=="BRIJ") {
        alert(" U Don't Have Privilages to Delete "+reportYear+" Data");
        return false;
        }
        }   
		
			if(approvedStatus=="Approved" || approvedStatus=="Initial"){
              		alert(approvedStatus+" Stage is Finalized.You Cant Delete it");
              		 return false;
              }else{
		
   var answer =confirm('Are you sure, Do you want to delete  the year '+reportYear);
   createXMLHttpRequest();
   if(answer){
   var flag="true";
   url="<%=basePath%>pcreportservlet?method=getDeleteAllRecordsforPc&accessCode="+accessCode+"&empserialno="+empserialno+ "&reportYear="+reportYear+"&frmName=adjcorrections";
			xmlHttp.open( "post", url, true);
			xmlHttp.onreadystatechange = getDeletemsg;
			
		}else{
			document.forms[0].formType.focus();
		}
		
		
		
		xmlHttp.send(null);
    }
    }
    	function getDeletemsg()
	{
	
		if(xmlHttp.readyState ==3 ||  xmlHttp.readyState ==2 ||  xmlHttp.readyState ==1){
			
		}
		if(xmlHttp.readyState ==4)
		{
			if(xmlHttp.status == 200)
				{ 
			      
		 
		 	
		  			alert("SuccessFully Deleted");
		 	  var url="";
		 	   
		     if(accessCode=="PE040201"){    	   
    		  url="<%=basePath%>pcreportservlet?method=loadPCReportForAdjDetailsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
     		}else  if(accessCode=="PE040202"){
     		url="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
    		}
		  	document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();
		  
	
		}
	}

	 
}
   
   function getlogs(empserialno,adjobyear){
   //alert('Under Processing........');
    var url="",logsFlag='<%=logsFlag%>';
    var swidth=screen.Width-350;
	var sheight=screen.Height-450;
		 logsFlag="true";
	if(logsFlag == "true"){
	alert("We cannot display Logs  for this pfid "+empserialno+" due to some issues");
	}else{
  		url ="<%=basePath%>pcreportservlet?method=getadjemolumentslogforPc&empserialno="+empserialno+"&adjobyear="+adjobyear+"&frmName=adjcorrections";
		 wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		 winOpened = true;
		 wind1.window.focus();
		 }
   }
   

   function callReports(empName){  
   var url='',formType='',empsrlNo='';
   formType = document.forms[0].formType.value;
    empsrlNo = document.forms[0].empsrlNo.value;  
    
    if(formType!="NO-SELECT"){
   if(verifiedby!="Approved"){
     alert("This pfid is not Approved.You Cant View Reports");
     return false;
   }else{
   
   if(formType=="form7ps"){
    url="<%=basePath%>pcreportservlet?method=loadform7InputforPc&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;
    
      }
   if(formType=="form8ps"){
    url="<%=basePath%>pcreportservlet?method=loadform8paramsforPc&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;
   
   }
   if(formType=="statementofwages"){
    url="<%=basePath%>pcreportservlet?method=loadstatmentpcwagesInputforPc&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName+"&accessCode="+accessCode;   
   } 
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   }
   }
   }
   
   
   function getpcreport(empserialNO,adjOBYear,dojFlag,approvedStatus) {
   //alert(approvedStatus);
       	var regionID="",airportcode="",reportType="",yearID="",monthID="",yearDesc="",formType="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var empserialNO=document.forms[0].empsrlNo.value;
		regionID="NO-SELECT"; 
		var pfidStrip='1 - 1'; 
		yearID="NO-SELECT";
		monthID="NO-SELECT"; 
        var page = "report";
		 var url="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc"+params;
		//alert(url);
		 if(approvedStatus!="Approved"){
    	 alert("This pfid is not Approved.You Cant View Reports");
   	 	 return false;
   		}else{
   		
   		if(adjOBYear=="1995-2008"){
        if(dojFlag=="true"){
        alert(" We cannot provide  PC Report due to he/she joined after Mar 2008");
        return false;
        }
        }
		
		var comfirmMsg = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
		if (comfirmMsg== true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}
		var params = "&frm_reportType="+reportType+"&frm_formType"+formType+"&empserialNO="+empserialNO+"&reportYear="+adjOBYear+"&page="+page;
		var url="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc"+params;
		//  alert(url);
		if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 				 		wind1 = window.open(url,"PCReport","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 			}
   	  
		
	}
	
	}
   	function getpcreport1(){ 
        alert(" Under Processing....");
    	 return false;
   	 }
      	function pfcard1(){ 
        alert(" Under Processing....");
    	 return false;
   	 }	 
   
 function  pfcard(empSerialNo,empName,adjOBYear,approvedStatus){ 
 var url="",reportType="";
 var pfFlag="true";
 var swidth=screen.Width-10;
 var sheight=screen.Height-150;
 	   if(adjOBYear.substr(0,4)>=2015){
      // alert("Pf editing Proccessing");
      //return false;
		} 
 if(approvedStatus!="Approved"){
    	 alert("This pfid is not Approved.You Cant View Reports");
   	 	 return false;
   		}else{
	var  sss = confirm("Do You want Report in Html/Excel Sheet Format. Click Ok For Html and Cancel For Excel Sheet");
	 	if(sss == true){ 
		reportType="Html";
		}else{
		reportType="Excel Sheet";
		}
	
 	url ="<%=basePath%>pcreportservlet?method=getReportPenContrForAdjCrtnforPc&empserialNO="+empSerialNo+"&reportYear="+adjOBYear+"&pfFlag="+pfFlag+"&empName="+empName+"&frm_reportType="+reportType;
	 	//alert(url);
		 
   	 				 		wind1 = window.open(url,"PFCard","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
							winOpened = true;
							wind1.window.focus();
   	 		 
  		  
 }
   }

function  getForm2Report(repName,approvedStatus,pensionno,form2Status,adjobyear,form2id){ 
 if(form2Status=="M"){
 alert("We aren't maintain the previous Form-2 adjustment Status");
 return false;
 }
 	var url="";
 	
 if(approvedStatus!="Approved"){
     alert("This pfid is not Approved.You Cant View Reports");
     return false;
   }else{
 url = "<%=basePath%>pcreportservlet?method=uploadToForm2forPc&frmName=adjcorrections&pensionno="+pensionno+"&adjobyear="+adjobyear+"&form2id="+form2id;; 
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
 }
 }  
  function updateStatus(employeeNo,adjOBYear,rownum){
 var status='',stageStatus="",processedStage="";
 var pfid_statusboxno = "pfid_status"+rownum;
 	//alert(pfid_statusboxno);
	status =  document.getElementsByName(pfid_statusboxno)[0].value;
   // alert('here'+status+"=="); 
   var notFinalisdYears= new Array();
   var notFinalisdYearsStr="",chkPfidTrackingResult ="";
   notFinalisdYearsStr='<%=notFinalisdYears%>';
   notFinalisdYears=notFinalisdYearsStr.split(":");
  // alert(str.length);
  
  var chkadjyears = new Array();
  chkadjyears = chkPfidTracking.split(":");
  //alert(chkadjyears.length+"=="+chkPfidTracking);
  for(var i=0;i<chkadjyears.length;i++)
  {
  if(chkadjyears[i]==adjOBYear){
  chkPfidTrackingResult  ="Exists";
  chkadjyears.length=0;
  }else{
   chkPfidTrackingResult  ="NotExists";
  }
  }
 // alert(status);
   if(chkPfidTrackingResult=="Exists"){  
    if(status!="NO-SELECT"){ 
    
    for(var j=0;j<notFinalisdYears.length;j++)
  		{ if(notFinalisdYears[j]==adjOBYear){
 			 notFinalisdYearsResult  ="true";
 			 notFinalisdYears.length=0;
  		}else{
  			 notFinalisdYearsResult  ="false";
  			}
  		}
     //alert("adjOBYear"+adjOBYear);
    // alert("notFinalisdYearsResult"+notFinalisdYears.length);
    
    if(notFinalisdYearsResult == "true"){
   alert("Please Finalize the Year "+adjOBYear+" Before Closing the Stage");
   document.getElementsByName(pfid_statusboxno)[0].value="NO-SELECT";
   return false;
   }
    
    stageStatus="Y";
    if(status=="reject"){
    processedStage ="Reject";
    }else{
    processedStage='<%=processedStage%>'; 
    }
	var url = "<%=basePath%>pcreportservlet?method=updateAdjCrtnStatusforPc&pensionno="+employeeNo+"&stageStatus="+stageStatus+"&processedStage="+processedStage+"&accessCode="+'<%=accessCode%>'+"&reportYear="+adjOBYear+"&frmName=adjcorrections";  
  	//alert(url);
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
}
 
}else{
alert("Please Do Changes for PFID Then Update Status");
 document.getElementsByName(pfid_statusboxno)[0].focus();
return false;

}
}

 function  gotoback(pensionno){ 
 var url=""; 
  if(accessCode=="PE040202"){  
   url= "<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
   }else if(accessCode=="PE040201"){
   url= "<%=basePath%>pcreportservlet?method=loadAdjObCrtnforPc&accessCode="+accessCode+"&frmName=adjcorrections";
   }
  	//alert(url); 
  		document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   	 
 }


   function loadDet(){
if(brfflag=='Exists'){
var url='';
alert("This Pfid is Blocked/Frozen ,U can't Process...");
resetVals();
 if(accessCode=="PE040201"){    	   
    	  url="<%=basePath%>pcreportservlet?method=loadPCReportForAdjDetailsforPc&accessCode="+accessCode+"&searchFlag=S&frmName=adjcorrections";
     	}
    	// alert(url);
       	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();

}
   
  var message="<%=message%>";
  var url,  empsrlNo;
 
  if(message!='' && message=="U Don't Have Privilages to Access"){ 
  var ee;   
  ee = alert(message);
if(ee!=''){ 
		if(userType=="Admin"){
		url="<%=basePath%>PensionView/PensionMenu.jsp";
		}else if(userType=="User"){
		url="<%=basePath%>PensionView/PensionMenu2.jsp";
	 	}else if(userType=="NODAL OFFICER"){
		url="<%=basePath%>PensionView/PensionMenu2.jsp";
	 	}
        document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
  } 
  return false;
  }
  
  	  
  	  empsrlNo = document.forms[0].empsrlNo.value;   
  	  if(empsrlNo!='') {
  	  if(message!=""){
  	  if(message!="U Don't Have Privilages to Access"){
  	   alert(message);
  	    return false;
  	  }
  	  }
  // alert("=accessCode="+accessCode+"verifiedby=="+verifiedby);
  
  	  /*if(accessCode=="PE040201"){  	   
         	  if(verifiedby=="Initial" ||verifiedby=="Approved"){
               		 document.getElementById("pfisstatustext").style.display="block";
              		 document.getElementById("pfisstatusselect").style.display="none"; 
              }else if(verifiedby=="" || verifiedby=="processing"|| verifiedby=="Reject" ){
              		document.getElementById("pfisstatustext").style.display="none";
              		 document.getElementById("pfisstatusselect").style.display="block";
              } 
           
        }  
  	  
      if(accessCode=="PE040202"){
         	 if(verifiedby==""){ 
                  alert("Not Entered in Initial Stage");
              		 return false;
              }else if(verifiedby=="Initial"){
               		document.getElementById("pfisstatustext").style.display="none";
              		 document.getElementById("pfisstatusselect").style.display="block";
              
              }else{
              		
              		  document.getElementById("pfisstatustext").style.display="block";
              		 document.getElementById("pfisstatusselect").style.display="none";
              }
        }  */
  }
    var dataFlag='<%=dataFlag%>',empNo='<%=empsrlNo%>';
    //alert("--flag--"+dataFlag+"-empNo--"+empNo);
    if(dataFlag=='NoData' && empNo!=''){
     document.getElementById("norec").style.display="block";
    }else{
     document.getElementById("norec").style.display="none";
    }
   }
 

function disableEnterKey(e)
{
     var key;      
     if(window.event)
          key = window.event.keyCode; //IE
     else
          key = e.which; //firefox      

     return (key != 13);
}


function Mapping(empserialNo){
if(userId=="navayuga"){
    var answer =confirm('Are you sure, Do you want to delete all the previous data');
   if(answer){
   var url="<%=basePath%>pcreportservlet?method=adjCrtnDataMappingforPc&frmName=adjcorrections&empserialNo="+empserialNo+"";
   	document.forms[0].action=url;
		document.forms[0].method="post";
		document.forms[0].submit();
   }
   }else{ 
   alert("U Don't Have Privilages to Mapping");
   return false;
   }
   }
   
    function getReport(){
				var allRecordsFlag="true";
	 		    var swidth=screen.Width-10;
	 			var sheight=screen.Height-150;
		   		var reportType="",sortColumn="EMPLOYEENAME";
		   	 	reportType=document.forms[0].select_reportType.options[document.forms[0].select_reportType.selectedIndex].text;
		  		var url="<%=basePath%>pcreportservlet?method=employeesInAdjCrtnReportforPc&reportType="+reportID+"&allRecordsFlag="+allRecordsFlag;
		   	 	wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		   	 }
   
 </script>
	</head>

	<body class="BodyBackground"  onload="javascript:loadDet();" onkeypress="return disableKeyPress(event)" >
		<form name="test" action="" >
			 
					<%boolean flag = false;%>
				 
				
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
				<table width="85%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
							<tr>
							<td height="5%" colspan="4" align="center" class="ScreenMasterHeading">
						 	Impact Calculate For PC
							</td>
						 
						</tr>
						<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				 
							<tr>
								<!-- <td class="label" align="right">
									Form3-2007-Sep Employee Name:
								</td>
								<td>
									<input type="text" name="empName" onkeyup="return limitlength(this, 20)"/>
								</td>-->
								<td class="label" align="right"> 
									 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;PFID:<font color=red>*</font>
								</td>
								<td>
									<input type="text" name="empsrlNo" value="<%=empsrlNo%>" onkeypress="return disableEnterKey(event)"/> 
								</td>
							</tr>
							<!-- <tr>
							<td class="label"  align="right">
									Date of Birth:
								</td>
								<td>
								<input type="text" name="dob" onkeyup="return limitlength(this, 20)"/>
								<a href="javascript:show_calendar('forms[0].dob');"><img src="<%=basePath%>/PensionView/images/calendar.gif" border="no" alt="" /></a>
								</td>
							
								<td class="label"  align="right">
									Date of Joining:
								</td>
								<td>
									<input type="text" name="doj" onkeyup="return limitlength(this, 20)"/>
									<a href="javascript:show_calendar('forms[0].doj');"><img src="<%=basePath%>/PensionView/images/calendar.gif" border="no" alt="" /></a>
								</td>
							</tr>-->
							
					     	<tr>
								<td align="left">&nbsp;
								<td>
								</tr>

							<tr>

								<td align="center" colspan="5">
									<input type="button" class="btn" value="Search"   name="Submit" onclick="javascript:testSS();"/>
									<input type="button" class="btn" value="Reset"  name="Reset"  onclick="javascript:resetVals();" />
									<input type="button" class="btn" value="Cancel" name="Cancel"   onclick="javascript:history.back(-1);"/>
								</td>

							</tr>
						 
					</table>
					<%
			 
				       if (request.getAttribute("searchList") != null) {
				       ArrayList empSearchList=new ArrayList();		      
				       
				       
				       empSearchList=(ArrayList)request.getAttribute("searchList");
				         System.out.println("----------"+empSearchList.size()); 
				%>
				<table>
					<tr>
						<td>&nbsp;</td>
					</tr> 
				</table> 
				
				<table width="85%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder"><!--
				   	
				   	<tr>
			 			<td  colspan="5"> 
			 			<table  align=center     cellpadding=0>                         
			 				 <tr>
							<td class="label" >Summarized Report:
							 
							 <select name="select_reportType" style="width:110px" onchange="javascript:getReport()">
									<option value="" >[Select Report]</option>
									<option value="html">Html</option>
									<option value="ExcelSheet">Excel Sheet</option>								 
								</select>  </td>
								</td>
							  
							</tr>  
						</table>
					</td>
				</tr> 	
				 			--><tr>							
														
								<td align="center" width="85%">
														
									<display:table  style="width: 730px" id="advanceList" sort="list"   pagesize="15" name="requestScope.searchList" requestURI="./pcreportservlet?method=loadPCReportForAdjDetailsforPc" >   											
									<display:setProperty name="export.amount" value="list" />
    								<display:setProperty name="export.excel.filename" value="AdvancesSearch.xls" />
    								<display:setProperty name="export.pdf.filename" value="AdvancesSearch.pdf" />
    								<display:setProperty name="export.rtf.filename" value="AdvancesSearch.rtf" />					  
    																			
    								<display:column title="" media="html">
								  	<input type="radio" onclick="javascript:editRecord('<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getPfid()%>','<%=((EmpMasterBean)pageContext.getAttribute("advanceList")).getEmpName()%>')" />
								    </display:column>	
    								<display:column property="pfid" title="PF ID" class="datanowrap"/>
									<display:column property="empName"   headerClass="sortable" title="Employee Name" class="datanowrap"/>
									<display:column property="dateofBirth" title="DateofBirth"/>	
									<display:column property="dateofJoining" title="DateofJoining"/>	
									<display:column property="userName"  title="EditedBy" />									
									<display:column property="verifiedBy"  title="Status" />
										
																												
								
															
									</display:table>
								</td>
							</tr>
							
		
		
				<%
				  }
				%>
				

						</table> 

			 
					 
			<% EmpMasterBean  empBean = new EmpMasterBean();
			  SearchInfo  getSearchInfo = new SearchInfo();
			 int   index=0;
			 String cpfaccnos="";
			 System.out.println("----searchInfo------"+request.getAttribute("searchInfo"));
			if (request.getAttribute("searchInfo") == null) {
					 
					%> 
                   
						<table align="center" id="norec">
							<tr>
							 
							<td><b> No Records Found </b></td>
							</tr>
                         </table> 
			
					<%} else {int totalData = 0;				 
				flag = true;
				empBean = (EmpMasterBean) request.getAttribute("searchInfo");
				  if (empBean !=null) {
				 
				 int count = 0;
				String airportCode = "", employeeName = "", desegnation = "", empCode = "", cpfacno = "",  pensionNumber="",claimsprocess="";
				String dateofBirth="",dateofJoining="",empSerailNumber="",totalTrans="",designation = "",pensionOption = "",dojFlag="false";
				 
					count++; 
					employeeName = empBean.getEmpName();				  
                    empSerailNumber=empBean.getEmpSerialNo();
                    dateofBirth =  empBean.getDateofBirth();                   
                    dateofJoining	=  empBean.getDateofJoining();    
                    designation = empBean.getDesegnation();
                    pensionOption = empBean.getWetherOption();
                    DateFormat df = new SimpleDateFormat("dd-MMM-yy");
                    Date doj = df.parse(dateofJoining);
					if(doj.after(new Date("31-Mar-08"))){
					dojFlag="true";                    
                    }
					 %>
					 <table width="85%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
						 	<tr class="tbheader">

								<td class="tblabel">PFID</td>	
								 
								<td class="tblabel">Employee Name</td>								
								<td class="tblabel">DateOfBirth</td>
								<td class="tblabel">DateOfJoining</td>
								<td class="tblabel">Designation</td>
								<td class="tblabel">WETHEROPTION</td>
								<!-- <td class="tblabel"></td>-->
							<!-- <td class="tblabel">PensionNumber </td>-->
																				
								<td>
							 
						</tr>
						<tr><td></td></tr>                 
					 
							<tr> 
								<td  class="Data" width="15%"><%=empSerailNumber%></td>								  
								<td  class="Data" width="20%" nowrap="nowrap"><%=employeeName%></td>
								<td  class="Data" width="20%"> <%=dateofBirth%>   </td>
								<td  class="Data" width="20%"><%=dateofJoining%></td>
							    <td  class="Data" width="20%"><%=designation%></td>
							    <td  class="Data" width="20%" align="center"><%=pensionOption%></td>
								<td  class="Data" width="15%">
								<a href="#" onclick="Mapping('<%=empSerailNumber%>')"><img src="./PensionView/images/mapping.gif" border="0"  /></a>
									&nbsp;&nbsp; 
								</td> 
							</tr>
							
							
							 
                           <tr>&nbsp;</tr>
							<tr>
							<td align="center"></td><td></td><td> <td></td><td> </td>
							</tr>
							
							
						 
						</table>
					 
					 
						<table width="85%" border="0" align="center" cellpadding="1" cellspacing="0" class="tbborder">
							
						<tr>
			 			<td  colspan="5"> 
			 			<table  align=center     cellpadding=0>                         
			 				 <tr>
							<td class="label" >	Form 7/8 PS Report:
							 
							 <select name="formType" onchange="callReports('<%=employeeName%>');">
							 <option value="NO-SELECT">Select One</option>
							 <option value="form7ps">FORM 7 PS</option>
							 <option value="form8ps">FORM 8 PS</option>	
							 <option value="statementofwages">Statement Of Wages & Pension Contri.</option>	              					 
							 </select>
								</td>
							  
							</tr>  
						</table>
					</td>
				</tr> 	
							
							
							<tr class="tbheader">

								<td class="tblabel">Years</td>	
								 
								<td class="tblabel">Adj Emp Sub&nbsp;&nbsp;</td>								
								<td class="tblabel">Adj AAI Contri&nbsp;&nbsp;</td>
								<td class="tblabel">Adj Pension contri</td>
								<td class="tblabel">Logs</td>
								<td class="tblabel">Delete</td>
								<td class="tblabel">Report</td>
								<td class="tblabel">Status</td>
								<!-- <td class="tblabel"></td>-->
							<!-- <td class="tblabel">PensionNumber </td>-->
																				
								<td>
							 
						</tr>
						
							
					<%
					ArrayList empPCAdjDiffTot = new ArrayList();
					if (request.getAttribute("empPCAdjDiffTot") != null) {
					empPCAdjDiffTot = (ArrayList)request.getAttribute("empPCAdjDiffTot");
				
				
						for (int j = 0; j < empPCAdjDiffTot.size(); j++) {
					EmployeePensionCardInfo diffBean  = (EmployeePensionCardInfo) empPCAdjDiffTot.get(j);
					 
					%>
							<tr> 
								<td  class="Data" width="15%"><a href="#" onclick="validateForm('<%=empSerailNumber%>','<%=employeeName%>','<%=dojFlag%>','<%=diffBean.getReportYear()%>','<%=diffBean.getApprovedStage()%>','<%=diffBean.getFrozen()%>')"><img src="./PensionView/images/edit.gif" border="0"   alt="Edit" /></a>
								<%=diffBean.getReportYear()%></td>
								  
								<td  class="Data" width="20%">
									<%=diffBean.getEmpSubTotDiff()%>
								</td>
								<td  class="Data" width="20%">
								 <%=diffBean.getAaiContriDiff()%>   
								</td>
								 <td  class="Data" width="20%">
									<%=diffBean.getPensionContriTotDiff()%>
								</td>
								
								 <td  class="Data" width="15%"><a href="#" onclick="getlogs('<%=empSerailNumber%>','<%=diffBean.getReportYear()%>')"><img src="./PensionView/images/viewDetails.gif" border="0" alt="Logs" /></a>
									&nbsp;&nbsp; 
								</td>
								  <td  class="Data" width="15%"><a href="#" onclick="getDelete('<%=empSerailNumber%>','<%=diffBean.getReportYear()%>','<%=diffBean.getApprovedStage()%>')"><img src="./PensionView/images/delete.gif" border="0" alt="DELETE" /></a>
									&nbsp;&nbsp; 
								</td>
								<%if(diffBean.getReportYear().equals("1995-2008")){%>
								 <td  class="Data" width="15%"><a href="#" onclick="getpcreport('<%=empSerailNumber%>','<%=diffBean.getReportYear()%>','<%=dojFlag%>','<%=diffBean.getApprovedStage()%>')"><img src="./PensionView/images/pc_report.gif" border="0"  /></a>
									&nbsp;&nbsp; 
								</td> 
								<%}else{%>
								 <td  class="Data" width="15%"><a href="#" onclick="pfcard('<%=empSerailNumber%>','<%=employeeName%>','<%=diffBean.getReportYear()%>','<%=diffBean.getApprovedStage()%>')"><img src="./PensionView/images/pfcard_report.gif" border="0"  /></a>
									&nbsp;&nbsp; 
								</td> 
								<%}%>
								
								<td class="Data" width="25%" align="center">
								<%
								System.out.println("==diffBean.getApprovedStage()==="+diffBean.getApprovedStage());
								   if((!diffBean.getApprovedStage().equals("")) && (accessCode.equals("PE040201")  && !diffBean.getApprovedStage().equals("Reject"))){
								  
								  %>
								<%=diffBean.getApprovedStage()%>
								<%}else{%>
								 <select name="pfid_status<%=j%>"  style="width: 80px;height:30px; align: center;" onchange="updateStatus('<%=empsrlNo%>','<%=diffBean.getReportYear()%>','<%=j%>');">
									 <option value="NO-SELECT">Select One</option>
									 <option value="accept">Accept</option>	
									 <%if(accessCode.equals("PE040202")){%>
								  	 <option value="reject">Reject</option>	
							 		 <%}%>	 					               					 
								 </select> 
								 <%}%> 
							 </td>
							</tr>
							
							
							 
                           <tr>&nbsp;</tr>
							<tr>
							<td align="center"></td><td></td><td> <td></td><td> </td>
							</tr>
							
							
							<% }}	
							 }%>
							 
					</table>		  
							
	
						
						 <table  align=center     cellpadding=0>                         
			 				 <tr>
			 				 <!--<td class="tblabel"  id="pfisstatustext" style="display:none">	Status:
							<font size="4" ><b>
							 <input type="text" readonly="readonly"  style="width: 100px"  name="pfid_status_text" value="<%=verifiedby%>"/>
							 </b>
							</font>
							 </td>
							 
							<td class="label" id="pfisstatusselect" style="display:none">	Status:							 
							 <select name="pfid_status" style="width: 80px" onchange="updateStatus('<%=empsrlNo%>');">
							 <option value="NO-SELECT">Select One</option>
							 <option value="accept">Accept</option>	
							 <%if(accessCode.equals("PE040202")){%>
							  <option value="reject">Reject</option>	
							  <%}%>	 					               					 
							 </select> 
							  <td>
							  -->
							  <!--<td><input type="button" class="btn" style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #FFFFFF;"
								value="Back" class="btn" onclick="gotoback('<%=empsrlNo%>');"/> 
							  </td>
							   
								--><td>&nbsp;</td>
							</tr>            
						</table>
						<%}%> 
					 
					
		</form>
	</body>
</html>
