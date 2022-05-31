<%@ page language="java"%>

<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.PensionBean"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%
 // ##########################################
 // #Date					Developed by			Issue description
 // #07-Dec-2011        	Prasanthi			    Scereen added for settlement date edit 
 // #########################################
 %>
<%
String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    EmpMasterBean bean1=null;

            String region = "";
            CommonUtil common = new CommonUtil();

            HashMap hashmap = new HashMap();
            hashmap = common.getRegion();
            String userId = (String) session.getAttribute("userid");
            String message = (String) request.getAttribute("msg");
            Set keys = hashmap.keySet();
            Iterator it = keys.iterator();
            String empName1 = "",empSerialnumber="",pensionNumber="",cpfaccno="",dob="";
            String arrearinfo1 = ""; 
            if (request.getParameter("mappedInfo") != null) {
             String mappedinfo = request.getParameter("mappedInfo")
                        .toString();
                out.println("mappedinfo " + mappedinfo);
            }    
            %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<link rel="StyleSheet" href="<%=basePath%>PensionView/css/styles.css" type="text/css" media="screen">
		<SCRIPT type="text/javascript" src="<%=basePath%>/PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript">	
	var detailsArray=[];	
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
	
	
	function getNodeValue(obj,tag)
    { 
	     if(obj.getElementsByTagName(tag)[0].firstChild){
	  return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	 }else return "";
   }
	function loadlog(){	
	var pensionNo=document.forms[0].empSerialnumber.value;
	  	if(pensionNo==""){
				alert("Please select the PFID using search Icon to View Log");
				 document.forms[0].searchbut.focus();				
				return false;
			} 
  		var swidth=screen.Width-10;
		var sheight=screen.Height-200;
		var url="<%=basePath%>search1?method=getfinalsettlementlog&pfId="+pensionNo;
  		wind1 = window.open(url,"FinalSettlementInformation","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		winOpened = true;
		wind1.window.focus();
	}	 
	function load(){	
	createXMLHttpRequest();
	pensionno=document.forms[0].empSerialnumber.value
  		//var swidth=screen.Width-300;
		//var sheight=screen.Height-300;
		document.getElementById("suggestions").style.display = 'none'; 
		var url="<%=basePath%>search1?method=getfinalsettlementDetails&pfId="+pensionno;
		xmlHttp.open("post", url, true);
	    xmlHttp.onreadystatechange = getfinalDetails;
	    xmlHttp.send(null);
  		//wind1 = window.open(url,"FinalSettlementInformation","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		//winOpened = true;
		//wind1.window.focus();
  	}
  	function getfinalDetails()
  {
  if(xmlHttp.readyState ==4)
	{	
		if(xmlHttp.status == 200)
		{ 		
		var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');		
		if(stype.length==0){
		document.getElementById("suggestions").style.display = 'none'; 		
		showValues1();
		}		
		 for(i=0;i<stype.length;i++){		
		   fspensionno = getNodeValue(stype[i],'pensionno');		  		  
		  	 fspurpose =  getNodeValue(stype[i],'purpose');			  		 	 	 
		 	 fsemployeename =  getNodeValue(stype[i],'employeename');		 			 	
		   fsfinaai = getNodeValue(stype[i],'finaai');		   		 
		 	 fsfinemp = getNodeValue(stype[i],'finemp');		 			 	 
		 	 fspencon = getNodeValue(stype[i],'pencon');		 		
		 	 fsnetamount = getNodeValue(stype[i],'netamount');			 		 			 	 
		 	 fssettlementdate =  getNodeValue(stype[i],'settlementdate');	   
		 	 fsairportcode =  getNodeValue(stype[i],'airportcode');	 	 		 	 		 	
		    fsregion = getNodeValue(stype[i],'region');		   	 		   	 
	 detailsArray[detailsArray.length]=[fspensionno,fsemployeename,fspurpose,fsfinaai,fsfinemp,fspencon,fsnetamount,fssettlementdate,fsairportcode,fsregion];	
		    showValues();
		 	}		 	
		  }		  		 	  
		}				
	}	
 function showValues()
	{
var str='<tr>';
				str+='<td height="25%">';
				str+='<table align="center" width="97%" cellpadding=2 class="tbborder" cellspacing="0" border="0">';
				str+='<tr class="tbheader">';
					str+='<td class="tblabel">PENSIONNO </td>';
					str+='<td class="tblabel">EMPLOYEENAME</td>';
					str+='<td class="tblabel">PURPOSE</td>';
					str+='<td class="tblabel">FINEMP</td>';
					str+='<td class="tblabel">FINAAI</td>';
					str+='<td class="tblabel">PENCON</td>';
					str+='<td class="tblabel">NETAMOUNT</td>';
					str+='<td class="tblabel">SETTLEMENTDATE</td>';
				    str+='<td class="tblabel">AIRPORTCODE</td>';
				    str+='<td class="tblabel">REGION</td>';					
					str+='</tr>';		
				for(var i=0;i<detailsArray.length;i++)
				{				
					str+='<tr>';
					str+='<td class="Data">'+detailsArray[i][0]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][1]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][2]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][3]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][4]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][5]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][6]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][7]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][8]+'</TD>';
					str+='<td class="Data">'+detailsArray[i][9]+'</TD>';
					str+='</tr>';				
				}				
				str+='</TABLE>';
				str+='</td>';
				str+='</tr>';
				document.all['detailsTable'].innerHTML = str;	

}
function showValues1()
	{
var str='<tr>';
				str+='<td height="25%">';
				str+='<table align="center" width="97%" cellpadding=2 class="tbborder" cellspacing="0" border="0">';
				str+='<tr class="tbheader">';
					str+='<td class="tblabel">PENSIONNO </td>';
					str+='<td class="tblabel">EMPLOYEENAME</td>';
					str+='<td class="tblabel">PURPOSE</td>';
					str+='<td class="tblabel">FINEMP</td>';
					str+='<td class="tblabel">FINAAI</td>';
					str+='<td class="tblabel">PENCON</td>';
					str+='<td class="tblabel">NETAMOUNT</td>';
					str+='<td class="tblabel">SETTLEMENTDATE</td>';
				    str+='<td class="tblabel">AIRPORTCODE</td>';
				    str+='<td class="tblabel">REGION</td>';					
					str+='</tr>';		
					str+='<tr>';
					str+='<td class="Data" align="center">No Records Found</TD>';					
					str+='</tr>';		
				str+='</TABLE>';
				str+='</td>';
				str+='</tr>';
				document.all['detailsTable'].innerHTML = str;	

}
	function resetValue(){
	   document.forms[0].action="<%=basePath%>search1?method=employeesearch";
	 	document.forms[0].method="post";
		document.forms[0].submit();
	}
	function cancelVlaue(){
	   document.forms[0].action="<%=basePath%>PensionView/PensionMenu.jsp";
	 	document.forms[0].method="post";
		document.forms[0].submit();
	}			   
	function test(cpfaccno,region,pensionNumber,employeeName,desig,dateofbirth,dateofjoining,airportCode,finalsetdate,resetdate,intcalcdate,reintcalcdate,seperationDate,seperationReason,remarks,settlementClient,finalist){
		document.forms[0].empName1.value=employeeName;
		document.forms[0].empSerialnumber.value=pensionNumber;
		document.forms[0].desig.value=desig;
		document.forms[0].cpfaccno.value=cpfaccno;
		document.forms[0].dob.value=dateofbirth;
		document.forms[0].doj.value=dateofjoining;
		document.forms[0].interestCalcfinal.value=finalsetdate;
		document.forms[0].interestCalcfinal1.value=finalsetdate;
		document.forms[0].resettlement.value=resetdate;
		document.forms[0].resettlement1.value=resetdate;
		document.forms[0].interestCalc.value=intcalcdate;
		document.forms[0].interestCalc1.value=intcalcdate;
		document.forms[0].reinterestCalc.value=reintcalcdate;
		document.forms[0].reinterestCalc1.value=reintcalcdate;
		document.forms[0].airportcode.value=airportCode;
		document.forms[0].region.value=region;
		document.forms[0].sepdate.value=seperationDate;
		document.forms[0].sepdate1.value=seperationDate;
		document.forms[0].sepreason.value=seperationReason;
		document.forms[0].sepreason1.value=seperationReason;
		document.forms[0].remarks.value=remarks;
		document.forms[0].remarksold.value=remarks;
				
		if(settlementClient!=''){
		//document.forms[0].username.value=settlementClient;
		document.forms[0].username.value='<%=userId%>';
		}		
		if(settlementClient==''){
		document.forms[0].username.value='<%=userId%>';
		}
		document.forms[0].remarks.readOnly=false;
		var userId1='<%=userId%>';
		if (userId1=="navayuga" || userId1=="SEEMASETHI" || userId1=="SANJEEVKUMAR" || userId1=="sunita" || userId1=="AJAYKANOJIA" || userId1=="VEENAJAWA" ){
		document.forms[0].username.disabled = false;
		document.forms[0].sepreason.disabled = false;			 	
		}
		document.forms[0].settlementdate.value=finalist;
		 document.getElementById("suggestions").style.display = 'block';		  
		}
	function popupWindow(mylink,windowname)
		{
		document.getElementById("process").style.display='none';
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink;
		else
		href=mylink.href;
		progress=window.open(href, windowname, 'width=750,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		
		return true;
		}
	function LoadWindow(params){
    var newParams =params;
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
   } 
   var xmlHttp;
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
  function getNodeValue(obj,tag)
    { if(obj.getElementsByTagName(tag)[0].firstChild){
	  return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
	 }else return "";
   }   
    function testSS(){ 
        var pensionNo=document.forms[0].empSerialnumber.value;
	  	if(pensionNo==""){
				alert("Please select the PFID using search Icon");
				 document.forms[0].searchbut.focus();				
				return false;
			} 
	  var seperationDate=document.forms[0].sepdate.value;
	  var seperationReason=document.forms[0].sepreason.value;
	  var oldfinalsetdate=document.forms[0].interestCalcfinal1.value;
	   var userId1='<%=userId%>';
	  //alert("user"+userId1);
		if (userId1=="navayuga" || userId1=="SEEMASETHI" || userId1=="SANJEEVKUMAR" || userId1=="sunita" || userId1=="AJAYKANOJIA" || userId1=="VEENAJAWA" ){
		//alert("userId1"+userId1);
		if(seperationDate=="" || seperationReason==""){
	  alert("Need to have Separation Date & Reason. Please enter the Date of Seperation");
		document.forms[0].sepreason.focus();
		document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		document.forms[0].interestCalcfinal.style.background='none';
	  return  false;	
	  } 	
	  }else { 
	  if(seperationDate=="" || seperationReason==""){
	  alert("Need to have Separation Date & Reason. You don't have privileges  to add this attributes. Please forward a mail to EPIS support team to add attributes.");
		document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		document.forms[0].interestCalcfinal.style.background='none';
	  return  false;	  
	  }
	  if(oldfinalsetdate!=""){
			 	    alert("Final Settlement Date Already Exist for this Employee");
			         document.forms[0].interestCalcfinal.value=oldfinalsetdate;
			 	    document.forms[0].interestCalcfinal.style.background='none';
			 	       return false;
			 	 }	 
	      } 	
		 if(document.forms[0].sepreason.value!=document.forms[0].sepreason1.value || document.forms[0].sepdate.value!=document.forms[0].sepdate1.value){
		    if(document.forms[0].sepreason.value=="" && document.forms[0].sepdate.value!=""){
		    alert("Please enter the Separation Reason/Separation Date");
				 document.forms[0].sepreason.focus();
				return false;
		    } 
		     if(document.forms[0].sepdate.value=="" && document.forms[0].sepreason.value!=""){
		    alert("Please enter the seperationdate");
				 document.forms[0].sepdate.focus();
				return false;
		    } 
		    if(document.forms[0].sepdate1.value!="" && document.forms[0].sepreason1.value!=""){
		    alert("You do not have privileges to change the Separation Reason/Separation Date");				
			return false;
		    } 
		     var date = document.forms[0].sepdate.value;
			 var finaldate=getDate(date);
			 var datedob = document.forms[0].dob.value;
			 var dobdate=getDate(datedob);
			 var dobdt=dobdate.getDate();
			 var dobmt=dobdate.getMonth()-1;
			 var dobyr1=dobdate.getFullYear()+60;
			  var newdobdate1=new Date(dobyr1,dobmt,dobdt);
			 var dobyr=newdobdate1.getYear();			
			 var newdobdate=new Date(dobyr,dobmt,dobdt);
			 if(seperationReason=='Retirement'){
			 if(finaldate <= newdobdate){
			   alert("Please Ensure that Date of Separation must be greater than 60 years from the Date of birth for the Separation Reason :Retirement");
		       return false;		       
				}
				}
			 date1=new Date(1900,03,01); 
			 var date2=new Date(2014,03,31);
			 alert("dfdfsfsdff"+date2);
			  if(finaldate >= date1 && finaldate <= date2){
			   alert("Seperation date can't be edited between 01-Apr-1995 and 31-Mar-2012. i.e PC Report 1995-2008 and PFCARD 2008-11 to 2013-2014 already frozen");
		       return false;		       
				}				
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
         if (finaldate>currentdt)
        {             
         alert("Please ensure that Seperation date is less than or equal to current month");        
          return false;	
        }
			var recoverieTable='seperationdate';	
			var pensionNo=document.forms[0].empSerialnumber.value;
			var seperationdate=document.forms[0].sepdate.value;	
			var seperationreason=document.forms[0].sepreason.value;	
			var clientname=document.forms[0].username.value; 		
		 }	
    if(document.forms[0].interestCalcfinal.value!=document.forms[0].interestCalcfinal1.value){
    //alert("interestCalcfinal enteriing ");
    var userId1='<%=userId%>';
   if(userId1=="CSIFIN"){
   alert("Sorry.....! you don't have privileges to change Final Settlement Date. Please Contact EPIS Support Team.");
   return false;
   }
    var oldfinalsetdate=document.forms[0].interestCalcfinal1.value;
	  document.forms[0].interestCalcfinal.readOnly=true;	  
	  var pensionNo=document.forms[0].empSerialnumber.value;
	  var seperationDate=document.forms[0].sepdate.value;
	  var seperationReason=document.forms[0].sepreason.value;
	  var interestCalcfinaldate=document.forms[0].interestCalcfinal.value;
	  var userId1='<%=userId%>';
	  //alert("user"+userId1);
		if (userId1=="navayuga" || userId1=="SEEMASETHI" || userId1=="SANJEEVKUMAR" || userId1=="sunita" || userId1=="AJAYKANOJIA" || userId1=="VEENAJAWA"  ){
		//alert("userId1"+userId1);
		if(seperationDate=="" || seperationReason==""){
	  alert("Need to have Separation Date & Reason. Please enter the Date of Seperation");
		document.forms[0].sepreason.focus();
		document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		document.forms[0].interestCalcfinal.style.background='none';
	  return  false;	
	  }}else{
	  if(seperationDate=="" || seperationReason==""){
	  alert("Need to have Separation Date & Reason. You don't have privileges to add this attributes. Please forward a mail to EPIS support team to add attributes.");
		document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		document.forms[0].interestCalcfinal.style.background='none';
	  return  false;	  
	  }	
	  }  
 	 createXMLHttpRequest();
			 var date1=document.forms[0].interestCalcfinal;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(interestCalcfinaldate==""){
				alert("please enter the finalsettlment date upto date");
				 document.forms[0].interestCalcfinal.focus();
				return false;
			} 
			 var date = document.forms[0].interestCalcfinal.value;
			 var sepdate=document.forms[0].sepdate.value;
			 var finaldate=getDate(date);
			 var filasepdate=getDate(sepdate);
			 date1=new Date(1900,03,01); 
			 var date2=new Date(2019,03,31);
            var finaldateAlreadyExist=oldfinalsetdate;
            var oldfinaldate= getDate(finaldateAlreadyExist); 
 			 if(oldfinalsetdate!=""){
			 	    alert("FinalSettlement Date  "+finaldateAlreadyExist+" Already Exist. You can't edit this Date ");
			        document.forms[0].interestCalcfinal.value=oldfinalsetdate;
			 	    document.forms[0].interestCalcfinal.style.background='none';
			 	    return false;
			 	 }			 	 
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
         if (finaldate>currentdt)
        {       
        alert("Please ensure that Final Settlement Date "+interestCalcfinaldate+" is less than or equal to current month");
         document.forms[0].interestCalcfinal.value=oldfinalsetdate;
			 	    document.forms[0].interestCalcfinal.style.background='none';
         return false;
        }       
         if (finaldate<filasepdate)
        {       
        alert("Please ensure that Final Settlement Date "+interestCalcfinaldate+" is greater than to Seperation Date");
         document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		 document.forms[0].interestCalcfinal.style.background='none';
         return false;
        }  
         if (finaldate<=oldfinaldate)
        {       
        alert("Please ensure that Final Settlement Date "+interestCalcfinaldate+" is greater than Already Existed FinalSettlementDate");
         document.forms[0].interestCalcfinal.value=oldfinalsetdate;
		 document.forms[0].interestCalcfinal.style.background='none';
         return false;
        }        
			  if(finaldateAlreadyExist!="" && finaldateAlreadyExist >= date1 && finaldateAlreadyExist <= date2){
		 	  alert("FinalSettlement Date  "+settlementDate+" Already Exist as Per the frozen date,You can't edit this Date i.e PFCard already frozen");
			 document.forms[0].interestCalcfinal.value=oldfinalsetdate;
			 document.forms[0].interestCalcfinal.style.background='none';
			 return false;
					} 
			 if(finaldate >= date1 && finaldate <= date2){
	   	   		alert("FinalSettlement date can't be edited between 01-Apr-1995 and 31-Mar-2014. i.e PC Report 1995-2008 and PFCARD 2008-18 already frozen");
		        document.forms[0].interestCalcfinal.value=oldfinalsetdate;
			 	    document.forms[0].interestCalcfinal.style.background='none';
		       return false;		       
				}
			var recoverieTable='interestCalcfinal';	
			}  
   if(document.forms[0].interestCalc.value!=document.forms[0].interestCalc1.value){
    var oldinterestCalcdate=document.forms[0].interestCalc1.value;    
     var pensionNo=document.forms[0].empSerialnumber.value;
	  document.forms[0].interestCalc.readOnly=false;
	  var interestCalcdate=document.forms[0].interestCalc.value;  	
	 	 createXMLHttpRequest();

			 var date1=document.forms[0].interestCalc;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(interestCalcdate==""){
				alert("please enter the InterestCalculaion upto date");
				return false;
			} 
			 var date = document.forms[0].interestCalc.value;
			 var finaldate=getDate(date);			
			 date1=new Date(1900,03,01); 
			 var date2=new Date(2008,02,31);           
			 var finaldateAlreadyExist=oldinterestCalcdate;
			 finaldateAlreadyExist=getDate(finaldateAlreadyExist);		
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
         if (finaldate>currentdt)
        {       
         alert("Please ensure that IntrestCalc Date "+interestCalcdate+" is less than or equal to current month");
          document.forms[0].interestCalc.value=oldinterestCalcdate;
			 	    document.forms[0].interestCalc.style.background='none';
         return false;
        }
			 	 	 		
			  if(finaldateAlreadyExist!="" && finaldateAlreadyExist >= date1 && finaldateAlreadyExist <= date2){
		 	  alert("FinalSettlement Date  "+interestCalcdate+" Already Exist as Per the frozen date,You can't edit this Date i.e PCREPORT already frozen");
			  document.forms[0].interestCalc.value=oldinterestCalcdate;
			 	    document.forms[0].interestCalc.style.background='none';
			  return false;
					} 
				 if(finaldate >= date1 && finaldate <= date2){
	   	   		alert("intrestcalc date can't be edited between 01-Apr-1995 and 31-Mar-2008.");
		       document.forms[0].interestCalc.value=oldinterestCalcdate;
			   document.forms[0].interestCalc.style.background='none';
		       return false;
				}	
				var recoverieTable='interestCalc';					  		
		 }
		if(document.forms[0].resettlement.value!=document.forms[0].resettlement1.value){
		var oldresetdate=document.forms[0].resettlement1.value
		var pensionNo=document.forms[0].empSerialnumber.value;
	  document.forms[0].resettlement.readOnly=false;
	  var resettlement=document.forms[0].resettlement.value;
	  createXMLHttpRequest();
			 var date1=document.forms[0].resettlement;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(resettlement==""){
				alert("please enter the resettlement upto date");
				return false;
			} 		
		var resetdate=document.forms[0].resettlement1.value;		
			 	if(!(userId1=="WADHVA" || userId1=="SANJEEVKUMAR" || userId1=="VEENAJAWA" )){			 	
			 if(resetdate!=""){
			 	    alert("Resettlement Date  "+resetdate+" Already Exist. You can't edit this Date ");
			 	    document.forms[0].resettlement.value=resetdate;
			 	    document.forms[0].resettlement.style.background='none';
			        return false;
			 	 }}else if(userId1=="WADHVA" ){
			 	 var resdate = document.forms[0].resettlement.value;
			 var resettdate=getDate(resdate);
			 var finmonth=resettdate.getMonth();
			if(finmonth==0 || finmonth==1 || finmonth==2){
			var finyear1=resettdate.getYear()-1;
			 }else{
			 var finyear1=resettdate.getYear();
			 }	 
		   var finyear2=finyear1+1;
            var finyear=finyear1+"-"+finyear2;
            var settlementfinyear=document.forms[0].settlementdate.value;
            var	 settlfinyear=settlementfinyear.split(",");
             for (var i=0;i<settlfinyear.length;i++){             
   			 if(finyear==settlfinyear[i]){
 			 alert("Resettlement alredy done for this FINYEAR .You dont have the privileges for this FINYEAR");
			 document.forms[0].resettlement.value=resetdate;
			 	    document.forms[0].resettlement.style.background='none';
			        return false;
			 }
			 }
			  }		 	 
			 var redate = document.forms[0].resettlement.value;
			 var resettledate=getDate(redate);
			  var fidate = document.forms[0].interestCalcfinal.value;
			  var finalsettdate=getDate(fidate);
			   if(fidate==""){
			 	    alert("Please ensure that FinalSettlementDate can't be blank while entering the Resettlement Date");
			 	    document.forms[0].resettlement.value=resetdate;
			 	    document.forms[0].resettlement.style.background='none';
			        return false;
			 	 }
			 	 var resdate=getDate(resetdate);
			 	  if(resettledate<resdate){
			 	  alert("ReSettlement Date  Should be greater than already existed resettlement date");
			    document.forms[0].resettlement.value=resetdate;
			     document.forms[0].resettlement.style.background='none';
			     return false;
			 	  }
			 	 			 	 	
			 if(resettledate<finalsettdate){			         
			   alert("ReSettlement Date  Should be greater than final settlemnt date");
			    document.forms[0].resettlement.value=resetdate;
			     document.forms[0].resettlement.style.background='none';
			     return false;
			      }			  
			 	  var date = document.forms[0].resettlement.value;
			 var finaldate=getDate(date);
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
           date1=new Date(1900,03,01); 
		  var date2=new Date(2019,02,31);		 		  
			 if(finaldate >= date1 && finaldate <= date2 &&recoverieTable!="true"){
	   	   		alert("ReSettlement date can't be edited between 01-Apr-1995 and 31-Mar-2012. i.e PC Report 1995-2008 and PFCARD 2008-18 already frozen");
		       document.forms[0].resettlement.value=resetdate;
			   document.forms[0].resettlement.style.background='none';
		       return false;
				}
		//var resyear=finaldate.getFullYear();
        //var finalsettyear=finalsettdate.getFullYear();        		 
		///var resmonth=finaldate.getMonth();
       // var finalsettmonth=finalsettdate.getMonth();
      //var diffmnt=(resmonth+12*resyear)-(finalsettmonth+12*finalsettyear); 
        var one_day=1000*60*60*24; 
        var Diffdt=Math.ceil((finaldate.getTime()-finalsettdate.getTime())/(one_day));
        //alert( "Diffdt"+Diffdt);
          	if(Diffdt<30) {
        	alert("Please ensure that ReSettlement Date "+resettlement+" Must be greater than one month from Final Settlement Date and should not be greater than current month");
            document.forms[0].resettlement.value=resetdate;
	        document.forms[0].resettlement.style.background='none';
            return false;
        	}
       if (finaldate>currentdt)
       {
      alert("Please ensure that Resettlement Date "+resettlement+" must be greater than one month from Final Settlement Date and should not be greater than current month");
     document.forms[0].resettlement.value=resetdate;
	  document.forms[0].resettlement.style.background='none';
       return false;
        }        
        recoverieTable='resettlement';	
        }
		if(document.forms[0].reinterestCalc.value!=document.forms[0].reinterestCalc1.value){
		var oldreintrestcalc=document.forms[0].reinterestCalc1.value;	
		var pensionNo=document.forms[0].empSerialnumber.value;	
	  document.forms[0].reinterestCalc.readOnly=false;
	  var reinterestCalcdate=document.forms[0].reinterestCalc.value;
  	  createXMLHttpRequest();	 
			 var date1=document.forms[0].reinterestCalc;
			 var val1=convert_date(date1);		   	   
	   		    if(val1==false)
	   		     {
	   		      return false;
	   		     }
	   		  	if(reinterestCalcdate==""){
				alert("please enter the REInterestCalculaion upto date");
				return false;
			} 
			var reintdate=document.forms[0].reinterestCalc1.value;				
			 if(reintdate!=""){
			 	    alert("Reintrestcalc "+reintdate+" Already Exist. You can't edit this Date ");
			 	     document.forms[0].reinterestCalc.value=oldreintrestcalc;
	                 document.forms[0].reinterestCalc.style.background='none';
			        return false;
			 	 }	
			 	 var fidate = document.forms[0].interestCalc.value;
			 var finalsettdate=getDate(fidate);
			 var redate = document.forms[0].reinterestCalc.value;
			 var resettledate=getDate(redate);
			if(resettledate<finalsettdate){
			   alert("Reintrestcalc Date  Should be gretthan final intrestcalc upto date");
			document.forms[0].reinterestCalc.value=oldreintrestcalc;
	   document.forms[0].reinterestCalc.style.background='none';
	   return false;
			       }
			 	  var date = document.forms[0].reinterestCalc.value;
			 var finaldate=getDate(date);
		var today = new Date(); 
        var date=today.getDate();
        var month=today.getMonth();
         var year=today.getYear();
          var currentdt=new Date(year,month,31);
			 date1=new Date(1900,03,01);
			 var date2=new Date(2008,02,31);  
		      if(finaldate >= date1 && finaldate <= date2){
	   	   		alert("Reintrestcalc date date can't be edited between 01-Apr-1995 and 31-Mar-2008.");
	   	   		   document.forms[0].reinterestCalc.value=oldreintrestcalc;
	   document.forms[0].reinterestCalc.style.background='none';
		       return false;
				} 
       if (finaldate>currentdt)
        {
       alert("Please ensure that Recalc Date "+reinterestCalcdate+" is less than or equal to current month");
          document.forms[0].reinterestCalc.value=oldreintrestcalc;
	   document.forms[0].reinterestCalc.style.background='none';
       return false;
        }
         recoverieTable='reinterestCalc'; 
				}
				var remarks="";
			if(document.forms[0].remarks.value!=document.forms[0].remarksold.value){
				remarks=document.forms[0].remarks.value;
				}else{
				remarks="";
				}
	  var oldeseperationdate=document.forms[0].sepdate1.value;	  
	  var oldintrestcacfinaldate=document.forms[0].interestCalcfinal1.value;	 
	  var oldintrestcalcdate=document.forms[0].interestCalc1.value;	 
	  var oldresettlementdate=document.forms[0].resettlement1.value;	 
	  var oldreintrestcalcdate=document.forms[0].reinterestCalc1.value;	  
	  var seperationdate=document.forms[0].sepdate.value;	
	  var interestCalcfinaldate=document.forms[0].interestCalcfinal.value;	  
	  var interestCalcdate= document.forms[0].interestCalc.value;	 
	  var resettlement=document.forms[0].resettlement.value;
	  var reinterestCalcdate=document.forms[0].reinterestCalc.value;
	  var seperationreason=document.forms[0].sepreason.value;
	  var clientname=document.forms[0].username.value;
	  if(oldeseperationdate==seperationdate && oldintrestcacfinaldate == interestCalcfinaldate && oldintrestcalcdate ==interestCalcdate && oldresettlementdate==resettlement && oldreintrestcalcdate== reinterestCalcdate ){
	  alert("You Do not have changed any data");
	   return false;
	  }
	 document.forms[0].action="<%=basePath%>search1?method=editSettlementInfo&pensionNo="+pensionNo+"&oldeseperationdate="+oldeseperationdate+"&oldintrestcacfinaldate="+oldintrestcacfinaldate +"&oldintrestcalcdate="+oldintrestcalcdate+"&oldresettlementdate="+oldresettlementdate+"&oldreintrestcalcdate="+oldreintrestcalcdate+"&seperationdate="+seperationdate+"&interestCalcfinaldate="+interestCalcfinaldate+"&interestCalcdate="+interestCalcdate+"&resettlement="+resettlement+"&reinterestCalcdate="+reinterestCalcdate+"&remarks="+remarks+"&clientname="+clientname+"&seperationreason="+seperationreason;
      document.forms[0].method="post";
		document.forms[0].submit();
  }
  function getDate(date){	 
	  if(date.indexOf('-')!=-1){
		 elem = date.split('-'); 
	  }else{
		  elem = date.split('/'); 
	  }	  
		 day = elem[0];
		 mon1 = elem[1];
		 year = elem[2];
		 var month;
	   	 if((mon1 == "JAN") || (mon1 == "Jan")) month = 0;
        	else if(mon1 == "FEB" ||(mon1 == "Feb")) month = 1;
     	else if(mon1 == "MAR" || (mon1 == "Mar")) month = 2;
     	else if(mon1 == "APR" || (mon1 == "Apr")) month = 3;
     	else if(mon1 == "MAY" ||(mon1 == "May") ) month = 4;
     	else if(mon1 == "JUN" ||(mon1 == "Jun") ) month = 5;
     	else if(mon1 == "JUL"||(mon1 == "Jul")) month = 6;
     	else if(mon1 == "AUG" ||(mon1 == "Aug")) month = 7;
     	else if(mon1 == "SEP" ||(mon1 == "Sep")) month = 8;
     	else if(mon1 == "OCT"||(mon1 == "Oct")) month = 9;
     	else if(mon1 == "NOV" ||(mon1 == "Nov")) month = 10;
     	else if(mon1 == "DEC" ||(mon1 == "Dec")) month = 11;
	  var finaldate=new Date(year,month,day); 
	  return finaldate;	     	
  }
  function textLen()
{
if(document.forms[0].remarks.value.length>1500)
{
alert("Please data enter less or equal to 1500");
document.dataform.data.focus();
return false;
}
}
  function monthDiff(d1, d2) {
    var months;
    months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth() + 1;
    months += d2.getMonth();
    return months;
}
  function updateButtonStatus()
	{	
	 document.forms[0].interestCalcfinal.style.background='none';
	 document.forms[0].interestCalc.style.background='none';
	 document.forms[0].resettlement.style.background='none';
	 document.forms[0].reinterestCalc.style.background='none';
	 document.forms[0].sepdate.style.background='none';
	} 
	function sepdate()
  {   
       document.forms[0].sepdate.readOnly=false;
        document.forms[0].sepdate.style.background='#FFFFCC';	
        document.forms[0].sepdate.value=document.forms[0].sepdate.value,show_calendar('forms[0].sepdate');        
 }
 function interestCalfinal()
  {    
       document.forms[0].interestCalcfinal.readOnly=false;
        document.forms[0].interestCalcfinal.style.background='#FFFFCC';	
        document.forms[0].interestCalcfinal.value=document.forms[0].interestCalcfinal.value,show_calendar('forms[0].interestCalcfinal');
      }
  function interestCal()
  {  
        document.forms[0].interestCalc.readOnly=false;
        document.forms[0].interestCalc.style.background='#FFFFCC';	
        document.forms[0].interestCalc.value=document.forms[0].interestCalc.value,show_calendar('forms[0].interestCalc');
 }
 function reinterestCal()
  {
        document.forms[0].reinterestCalc.readOnly=false;
        document.forms[0].reinterestCalc.style.background='#FFFFCC';	
        document.forms[0].reinterestCalc.value=document.forms[0].reinterestCalc.value,show_calendar('forms[0].reinterestCalc');
		
 }
 function resettlementdate()
  { 
        document.forms[0].resettlement.readOnly=false;
        document.forms[0].resettlement.style.background='#FFFFCC';	
        document.forms[0].resettlement.value=document.forms[0].resettlement.value,show_calendar('forms[0].resettlement');
 } 
 function hideDiv() {
      document.getElementById("suggestions").style.display = 'none';   
      
    }
    function hidedetailsDiv() {   
     detailsArray=[];
     document.all['detailsTable'].innerHTML="";
 }
 </script>
	</head>
	<body class="BodyBackground" onload="hideDiv();">
		<form name="form1" method="post">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
				</tr>
				<tr><td>&nbsp;</td></tr>
			</table>
			<tr id="process" style="display:inline" align="center">
<%if(message!=null){%>
<td align="center" ><font color=red size="2"><%=message%>.....</font></td>
<%}%>
<%if(message==null){%>
<td align="center" ></td>
<%}%>
</tr>
		<table align="center" width="80%" align="center" border="0" cellpadding="0"
			cellspacing="0" class="tbborder">
			<tr>
				<td height="5%" align="center" class="ScreenMasterHeading">Settlement Information [Edit]</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<table align="center" border="0" width="100%" align="center"
					cellpadding="1" cellspacing="0">
							<tr>
							<td class="label" align="right">
									Form3-2007-Sep-PFID:<font color=red>*</font>&nbsp;
								</td>
								<td>
									<input type="text" name="empSerialnumber" readonly="true" tabindex="1">
									<a href="#" onClick="popupWindow('<%=basePath%>PensionView/personal/FinalSettlementInfo.jsp','AAI');hidedetailsDiv();"><img src="<%=basePath%>/PensionView/images/search1.gif" border="0" name="searchbut" alt="Click The Icon to Select FinanceData Mapped Records"/></a>
									<a href="#" onclick="loadlog();"><img src="<%=basePath%>/PensionView/images/viewDetails.gif" border="0" alt="Click the link to view SettlementInformation Log"/></a>
									<!-- <img src="<%=basePath%>/PensionView/images/search1.gif" name="searchbut" onclick="popupWindow('<%=basePath%>PensionView/personal/FinalSettlementInfo.jsp','AAI');hidedetailsDiv();"  alt="Click The Icon to Select FinanceData Mapped Records"/>
								<img src="./PensionView/images/viewDetails.gif" name="viewlog" onclick="loadlog();" alt="Click the link to view SettlementInformation Log" >-->
								</td>
								<td class="label" align="right">
									Form3-2007-Sep-Employee Name:&nbsp;
								</td>
								<td>
									<input type="text" name="empName1" onblur="" readonly="true" tabindex="2">
								</td>								
							</tr>							
							<tr>			
				           <td class="label" align="right">
									Old CPFACC.No:&nbsp;
								</td>
								<td>
									<input type="text" name="cpfaccno" readonly="true" tabindex="3">
								</td>
								<td class="label" align="right">
									Designation:&nbsp;
								</td>
									<td>
									<input type="text" name="desig" readonly="true" tabindex="4" >
									</td>
							</tr>
							<tr>
								<td class="label" align="right">
									Date of Birth:&nbsp;
								</td>
									<td>
										<input type="text" name="dob" readonly="true" tabindex="5">
                                    </td>
								<td class="label" align="right">
									Date of Joining:&nbsp;									
								</td>
								<td>									
								    <input type="text" name="doj" readonly="true" tabindex="6">	                                
	                         </td>
							</tr>
							<tr>
								<td class="label" align="right">
									Separation Reason:&nbsp;
								</td>
							 <%if(userId.equals("PRASSU")){ %>
						<td><SELECT NAME="sepreason" disabled="disabled" style="width:130px">
							<option value="NO-SELECT" >[Select One]</option>
							<option value='Retirement'>Retirement</option>
							<option value='Death'>Death</option>
							<option value='Resignation'>Resignation</option>
							<option value='Termination'>Termination</option>
							<option value='Option for Early Pension'>Option for Early Pension</option>
							<option value='VRS'>VRS</option>
							<option value='Other'>Other</option>
							</SELECT></td>
							<input type="hidden" tabindex="7" name="sepreason1">	
							<%} else {%>
							<input type="hidden" tabindex="7" name="sepreason1">
							<td><input type="text" name="sepreason"  readonly="true" tabindex="6"></td>
							<%}%>
								<td class="label" align="right">
									Date of Separation:&nbsp;									
								</td>
								<td>									
								    <input type="text" name="sepdate" readonly="true" tabindex="6">
								     <input type="hidden" tabindex="7" name="sepdate1">	
								    <%if(userId.equals("PRASSU")){%>
                                    <a onclick="sepdate();" href="javascript:show_calendar('forms[0].sepdate');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
                                    <%}%>								                                  
	                         </td>
							</tr>							               
                             <tr>
								<td class="label" align="right">								
									Final Settlement:<font color=red>*</font>&nbsp;
								</td>
                              <td>						
								    <input type="text" tabindex="7" readonly="true" name="interestCalcfinal">
								    <input type="hidden" tabindex="7" name="interestCalcfinal1">								    
                                    <a onclick="interestCalfinal();" href="javascript:show_calendar('forms[0].interestCalcfinal');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
                                    
                              </td>
                            <td class="label" align="right">									
									Resettlement Date:&nbsp;   
		                    </td>
                              <td>							
								    <input type="text" name="resettlement" readonly="true" tabindex="8" >
								    <input type="hidden" tabindex="7" name="resettlement1">   
								     <%if(userId.equals("SEEMASETHI") || userId.equals("RKBATRA") ||  userId.equals("navayuga") || userId.equals("sunita") || userId.equals("AJAYKANOJIA") || userId.equals("VEENAJAWA")|| userId.equals("Monika")){%>
								   <a onclick="resettlementdate();" href="javascript:show_calendar('forms[0].resettlement');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
								    <%}%>
                            </td>
							</tr>
							<tr>
								<td class="label" align="right">								
									Interest Calculation Upto:&nbsp;
								</td>
                              <td>							
                                    <input type="text"  tabindex="9" readonly="true" name="interestCalc">
                                    <input type="hidden"  tabindex="9" name="interestCalc1">
                                    <%if(userId.equals("SEEMASETHI") || userId.equals("RKBATRA")  || userId.equals("navayuga") || userId.equals("sunita") || userId.equals("AJAYKANOJIA") || userId.equals("VEENAJAWA")|| userId.equals("Monika")){%>
                                    <a onclick="interestCal();" href="javascript:show_calendar('forms[0].interestCalc');" alt="Calender"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
                                    <%}%>
                              </td>
                            <td class="label" align="right">Reinterest Caliculation Upto:&nbsp;</td>
                              <td>							
								    <input type="text" name="reinterestCalc" readonly="true" tabindex="10">
								    <input type="hidden" name="reinterestCalc1" tabindex="10">
								     <%if(userId.equals("SEEMASETHI") || userId.equals("RKBATRA")  || userId.equals("navayuga") || userId.equals("sunita") ||  userId.equals("AJAYKANOJIA") || userId.equals("VEENAJAWA")|| userId.equals("Monika")){%>
									<a onclick="reinterestCal();" href="javascript:show_calendar('forms[0].reinterestCalc');"><img src="<%=basePath%>PensionView/images/calendar.gif" border="no" alt="Calender"/></a>
									 <%}%>
        		             </td>
							</tr>
							<tr>
			
				           <td class="label" align="right">Airportcode:&nbsp;</td>
								<td><input type="text" name="airportcode" readonly="true" tabindex="11"></td>
								<td class="label" align="right">Region:&nbsp;</td>
								<td><input type="text" name="region" readonly="true" tabindex="12" ></td>
							</tr>
							<tr>	
						<td class="label" align="right">UserName:&nbsp;</td>
						  <%if(userId.equals("navayuga")){ %>
						<td><SELECT NAME="username" disabled="disabled" style="width:130px">
							<option value="NO-SELECT" >[Select One]</option>
							<%
							if(request.getAttribute("userNameList")!=null){
							ArrayList list=(ArrayList)request.getAttribute("userNameList");
							for(int i=0; i<list.size(); i++)
							{
						     	bean1 =(EmpMasterBean) list.get(i);
							 	%>
							  	<option value="<%=bean1.getUserName()%>" ><%=bean1.getUserName()%></option>
	                       		<%}}%>
							</SELECT>
							<%} else {%>
							<td><input type="text" name="username" readonly="true" tabindex="11"></td>
							<%}%>
					</tr>			
					<tr>
					<td class="label" align="right" nowrap>Remarks:&nbsp;	</td>					
						<td colspan=3><TEXTAREA NAME="remarks" style="font-size:12px; font-family:Arial; width:500px; height:50px;" onblur="textLen();" maxlength=1500  readonly="true" tabindex="12"></TEXTAREA>
						<input type="hidden" name="remarksold" tabindex="10">
						<input type="hidden" value="" name="settlementdate" id="settlementdate" tabindex="10">
						</td>
					</tr>
				</table>	
				<tr>
				<td>
					<table align="center" border="0" width="100%" align="center" cellpadding="1" cellspacing="0">
							<tr>								
								<td align="center">
									<input type="button" class="btn" value="Update" class="btn" onclick="testSS();">
									<input type="button" class="btn" value="Reset" onclick="resetValue();" class="btn">
									<input type="button" class="btn" value="Cancel" onclick="cancelVlaue();" class="btn">
								</td>
							</tr>						
			        </table>
					</td>
					</tr>    
					</table>
					<tr><td>&nbsp;</td></tr>
					<div id="suggestions">			
					<table align="center">					 
					<tr><td align="center" style="font-size:12px; font-family:Arial; "><a  title="Click the link to view FinalSettlement Log" target="_self" href="javascript:void(0)" onclick="javascript:load();">Final Settlement Details.......</a></td></tr>
					</table>
					</div>
					<div id="detailsDiv">	
			  <tr>
				<td height="25%">
					<div id='detailsTable'>
					</div>
					</div>
				</td>
			</tr>
			<div>								
		</form>
	</body>
</html>
