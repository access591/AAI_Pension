
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% 			DashBoardDetails dbDetails = new DashBoardDetails();
			dbDetails=(DashBoardDetails)request.getAttribute("dbinfo");
			ArrayList dbinfoList = new ArrayList();
			dbinfoList=(ArrayList)dbDetails.getList();
			
			
			DashBoardDetails dbPFWDetails = new DashBoardDetails();
			dbPFWDetails=(DashBoardDetails)request.getAttribute("dbpfwinfo");
			ArrayList dbpfwinfoList = new ArrayList();
			dbpfwinfoList =(ArrayList)dbPFWDetails.getList();
			
			DashBoardDetails dbFinalDetails = new DashBoardDetails();
			dbFinalDetails=(DashBoardDetails)request.getAttribute("dbFinalinfo");
			ArrayList dbfinalinfoList = new ArrayList();
			dbfinalinfoList =(ArrayList)dbFinalDetails.getList();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>EPIS - Dashboard</title>
<style type="text/css">
.black_overlay {
	display: none;
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 3000px;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
}
.white_content {
	display: none;
	position: fixed;
	top: 25%;
	left: 25%;
	width: 50%;
	height: 50%;
	padding: 16px;
	border: 16px solid orange;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
body {
	margin: 0px;
	padding: 0px;
	background-color: #FFFFFF;
}
.topbg {
	background: url(./PensionView/dashboard/images/topbg.gif) repeat-x left top;
}
.bg {
	background: #033C6C url(./PensionView/dashboard/images/bg.jpg) center top;
}
.footer {
	background: url(./PensionView/dashboard/images/footer.jpg) center top;
}
.tbl_shade {
	background: url(./PensionView/dashboard/images/tbl_img2.gif) no-repeat;
}
.tbl_rowbg {
	background: #F2F8FC;
	padding-left: 20px;
}
.tbl_rowbg-r {
	background: #F2F8FC;
	padding-right: 20px;
}
.tbl_rowpad {
	padding-left: 20px;
}
.tbl_rowpad-r {
	padding-right: 20px;
}
td {
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
}
.txthead {
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: bold;
	color:#003399;
	padding-left:8px;
}
.txtfield {	
	height:18px;
	width:70px;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color:#666;
}
.txtfield2 {	
	height:18px;
	width:80px;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color:#666;
}
.txtfield-month {	
	height:18px;
	width:75px;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color:#666;
}
.txtfield1{	
	height:18px;
	width:75px;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color:#666;
}
.footer-text {	
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-weight: normal;
	color:#FFFFFF;
}
</style>
   <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css"/>
     
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css" 
	type="text/css"/>
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
    
  
<script type="text/javascript">
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
		 
<!--	function loadForm5(motnyear,region,finyear){-->
<!--   var url='',formType='';-->
<!--   formType = document.forms[0].formType.value;-->
<!--    url="<%=basePath%>reportservlet?method=loadform7Input&frmName=adjcorrections&employeeNo="+empsrlNo+"&empName="+empName;-->
<!--       	document.forms[0].action=url;-->
<!--		document.forms[0].method="post";-->
<!--		document.forms[0].submit();-->
<!--   -->
<!--   }	 -->
		 
		 
 function calyear(){
	var myear=0;
 var year=document.forms[0].select.value;
  year1=year.split("-");
 var month=document.forms[0].month.value;
 
 if(month!="--"){
 if(month=="Jan"||month=="Feb"||month=="Mar"){
 var myear=parseInt(year1[0]);
 
 var monthYear="01-"+month+"-"+(myear+1);
 }else{
 var monthYear="01-"+month+"-"+year1[0];
 }
 }
 else{
 var monthYear="--";
 }

  var pfwyear=document.forms[0].select1.value;
  year2=pfwyear.split("-");
  var pfwtype=document.forms[0].type.value;

 var month1=document.forms[0].month1.value;
 
 if(month1!="--"){
  if(month1=="Jan"||month1=="Feb"||month1=="Mar"){
 var myear1=parseInt(year2[0]);
 
 var pfwmonthYear="01-"+month1+"-"+(myear1+1);
 }else{
 var pfwmonthYear="01-"+month1+"-"+year2[0];
 }
 
 }else{
 var pfwmonthYear="--";
 }
 //alert(pfwmonthYear);
 //block3
var finalyear=document.forms[0].select2.value;
  year3=finalyear.split("-");
  var fptype=document.forms[0].type1.value;
 
 var month2=document.forms[0].month2.value;
 
 if(month2!="--"){
 if(month2=="Jan"||month2=="Feb"||month2=="Mar"){
 var myear2=parseInt(year3[0]);

 var finalmonthYear="01-"+month2+"-"+(myear2+1);
 }else{
 var finalmonthYear="01-"+month2+"-"+year3[0];
 }
 }else{
 var finalmonthYear="--";
 }
 //alert(finalmonthYear);
 //createXMLHttpRequest();
 var url ="<%=basePath%>reportservlet?method=dashBoard&finyear="+year+"&monthYear="+monthYear+"&pfwfinyear="+pfwyear+"&pfwmonthYear="+pfwmonthYear+"&paymentStatus="+pfwtype+"&finalpaymentStatus="+fptype+"&finalmonthyear="+finalmonthYear+"&finalfinyear="+finalyear;
	document.forms[0].action=url;
	document.forms[0].method="post";
	document.forms[0].submit();
	//xmlHttp.open("post", url, true);
	//xmlHttp.onreadystatechange = getAirportsList;
}
 function calyear1(){

 var pfwyear=document.forms[0].select1.value;
  year2=pfwyear.split("-");
  var pfwtype=document.forms[0].type.value;
  //alert(pfwtype);
 var month1=document.forms[0].month1.value;
 //alert(month1);
 if(month1!="--"){
 var pfwmonthYear="01-"+month1+"-"+year2[0];
 }else{
 var pfwmonthYear="--";
 }
//alert(pfwyear+" "+pfwmonthYear);
//block1
var year=document.forms[0].select.value;
  year1=year.split("-");
 var month=document.forms[0].month.value;
 //alert(month);
 if(month!="--"){
 var monthYear="01-"+month+"-"+year1[0];
 }else{
 var monthYear="--";
 }
 //block3
var finalyear=document.forms[0].select2.value;
  year3=finalyear.split("-");
  var fptype=document.forms[0].type1.value;
  //alert(pfwtype);
 var month2=document.forms[0].month2.value;
 //alert(month1);
 if(month2!="--"){
 var finalmonthYear="01-"+month2+"-"+year3[0];
 }else{
 var finalmonthYear="--";
 }
 //createXMLHttpRequest();
 var url ="<%=basePath%>reportservlet?method=dashBoard&pfwfinyear="+pfwyear+"&pfwmonthYear="+pfwmonthYear+"&paymentStatus="+pfwtype+"&finyear="+year+"&monthYear="+monthYear+"&finalpaymentStatus="+fptype+"&finalmonthyear="+finalmonthYear+"&finalfinyear="+finalyear;
	document.forms[0].action=url;
	document.forms[0].method="post";
	document.forms[0].submit();

}
			


function frmload(){

//alert();
document.forms[0].select.value = '<%=dbDetails.getFinyear() %>';
document.forms[0].month.value = '<%=dbDetails.getMonthyear() %>';

document.forms[0].select1.value = '<%=dbPFWDetails.getFinyear() %>';
document.forms[0].month1.value = '<%=dbPFWDetails.getMonthyear()%>';
document.forms[0].type.value = '<%=dbPFWDetails.getPaymentStatus()%>';

document.forms[0].select2.value = '<%=dbFinalDetails.getFinyear() %>';
document.forms[0].month2.value = '<%=dbFinalDetails.getMonthyear()%>';
document.forms[0].type1.value = '<%=dbFinalDetails.getFinalPaymentStatus() %>';

}
	


</script>


</head>

<body onload="javascript:frmload();">
<form name="test" action=""  >
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="74" align="center" valign="top" class="topbg"><table width="990" height="70" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="left" valign="top"><img src="./PensionView/dashboard/images/dashboard-title.jpg" width="625" height="70" /></td>
        <td width="20" align="right" valign="middle"><img src="./PensionView/dashboard/images/epis-logo.gif" width="131" height="41" hspace="20" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>

    <td align="center" valign="top" class="bg" style="padding-top:30px;"><table width="948" border="0" cellpadding="0" cellspacing="0" >
      <tr>
        <td width="462" align="left" valign="top"><table width="462" border="0"    cellspacing="0" cellpadding="0">
          <tr>
            <td height="27" align="left" valign="middle" background="./PensionView/dashboard/images/tbl_img1.gif"><table width="450"  border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="208" class="txthead">CPF Recoveries Financial Year:</td>
                <td width="70"><select name="select" class="txtfield" id="select" onchange="calyear();">
						<option value="2011-2012">2011-12</option>
						<option value="2012-2013">2012-13</option>
                </select></td>
                <td width="44" class="txthead">Month:</td>
                <td width="128"><select name="month" class="txtfield-month" id="month" onchange="calyear();">
                  <option value="--">Select One</option>
                  <option value="Jan">January</option>
                  <option value="Feb">February</option>
                  <option value="Mar">March</option>
                  <option value="Apr">April</option>
                  <option value="May">May</option>
                  <option value="Jun">June</option>
                  <option value="Jul">July</option>
                  <option value="Aug">August</option>
                  <option value="Sep">September</option>
                  <option value="Oct">October</option>
                  <option value="Nov">November</option>
                  <option value="Dec">December</option>
                </select></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td align="left" valign="top" background="./PensionView/dashboard/images/tbl_img3.gif"><table width="462" border="0"  cellspacing="0" cellpadding="0">
              <tr>
                <td height="167" align="center" valign="top" class="tbl_shade"><table width="446" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="24" align="center" valign="middle"><strong>In the Year : <%=dbDetails.getFinyear() %></strong></td>
                  </tr>
                  <tr>
                    <td align="left" valign="top"><table width="435" border="0" align="center" cellspacing=0" cellpadding="0">
                      <tr class="tbl_rowbg">
                        <td width="80" height="18" align="left" valign="middle" ><strong>Region</strong></td>
                        <td width="45" align="center" valign="middle" ><strong>EPF</strong></td>
                        <td width="55" align="center" valign="middle" ><strong>VPF</strong></td>
                        <td width="55" align="center" valign="middle" ><strong>Principle</strong></td>
                        <td width="55" align="center" valign="middle" ><strong>Interest</strong></td>  
                      </tr>
                  <%   DashBoardInfo dbinfo = new DashBoardInfo();
                  		for(int i=0; i<dbinfoList.size();i++){
						dbinfo=(DashBoardInfo)dbinfoList.get(i);
						String region=dbinfo.getRegion();
					
						if(region.equals("CHQIAD")){
						region="International Airports";
						}
						int j=i+1;
						if(j%2!=0){
%>
                      <tr class="tbl_rowpad">
                      <%}else{%>
                      <tr class="tbl_rowbg">
                      <%}%>
                        <td  align="left" valign="middle" nowrap="nowrap"><%=region%>  </td>
                        <td align="right" valign="middle" > <%=dbinfo.getEpf()%></td>
                        <td  align="right" valign="middle" > <%=dbinfo.getVpf()%></td>
                        <td  align="right" valign="middle" > <%=dbinfo.getPrincipal()%></td>
                        <td  align="right" valign="middle" > <%=dbinfo.getInterest() %></td>
                      </tr>
<!--                      <a  title="Click the link to view AAIEPF-8" target="_self" href="javascript:void(0)" onclick="javascript:loadForm5('<%=dbDetails.getMonthyear()%>','<%=region%>','<%=dbDetails.getFinyear()%>');">-->
                      <%}
                      if(!dbinfo.getRemarks().equals("")){%>
                       <tr class="tbl_rowbg">
                        <td height="18" align="left" valign="middle"><strong></strong></td>
						<td align="left" valign="middle" colspan=3 ><strong>Note :</strong> <%=dbinfo.getRemarks() %></td>
                       <td colspan=2 ></td>
                      </tr>
                      <%}else{%>
                      <tr class="tbl_rowbg">
                        <td height="18" align="left" valign="middle"><strong>Total</strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbDetails.getEpftot() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbDetails.getVpftot() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbDetails.getPrincipal() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbDetails.getInterest() %></strong></td>
                      </tr>
                      <%}%>
                    </table></td>
                  </tr>
                </table></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="10" align="left" valign="top"background="./PensionView/dashboard/images/tbl_img4.gif"><img src="./PensionView/dashboard/images/spacer.gif" width="1" height="1" /></td>
          </tr>
        </table></td>
        <td width="24" align="left" valign="top">&nbsp;</td>
        <td width="462" align="left" valign="top"><table width="462" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="27" align="left" valign="middle" background="./PensionView/dashboard/images/tblr_img1.gif"><table width="450" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="200" class="txthead">Advances/PFW Year:</td>
<!--                <td width="44" class="txthead">Year:</td>-->
                <td width="50"><select name="select1" class="txtfield" id="select1" onchange="calyear();">
						<option value="2011-2012">2011-12</option>
						<option value="2012-2013">2012-13</option>
			    </select></td>
                <td width="34" class="txthead">Month:</td>
                <td width="60"><select name="month1" class="txtfield-month" id="month1" onchange="calyear();">
	                  <option value="--">Select One</option>
	                  <option value="Jan">January</option>
	                  <option value="Feb">February</option>
	                  <option value="Mar">March</option>
	                  <option value="Apr">April</option>
	                  <option value="May">May</option>
	                  <option value="Jun">June</option>
	                  <option value="Jul">July</option>
	                  <option value="Aug">August</option>
	                  <option value="Sep">September</option>
	                  <option value="Oct">October</option>
	                  <option value="Nov">November</option>
	                  <option value="Dec">December</option>
                </select></td>
                <td width="35" class="txthead">Type:</td>
                <td width="60"><select name="type" class="txtfield2" id="type" onchange="calyear();">
					  <option value="Y">Payment</option>
					  <option value="N">NotPayment</option>
			    </select></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td align="left" valign="top" background="./PensionView/dashboard/images/tbl_img3.gif"><table width="462" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="167" align="center" valign="top" class="tbl_shade"><table width="446" height="167" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="24" align="center" valign="middle"><strong>In the Year : <%=dbPFWDetails.getFinyear() %></strong></td>
                  </tr>
                  <tr>
                    <td align="left" valign="top"><table width="435" border="0" align="center" cellspacing=0" cellpadding="0">
                  <tr class="tbl_rowbg">
                        <td width="80" height="18" align="left" valign="middle" ><strong>Region</strong></td>
                        <td width="45" align="center" valign="middle" ><strong>Advance</strong></td>
                        <td width="45" align="center" valign="middle" ><strong>Subscription</strong></td>
                        <td width="55" align="center" valign="middle" ><strong>Contribution</strong></td>
                       
                      </tr>
                      
                      <%   	DashBoardInfo dbpfwinfo = new DashBoardInfo();
                  			for(int i=0; i<dbpfwinfoList.size();i++){
							dbpfwinfo=(DashBoardInfo)dbpfwinfoList.get(i);
							String region=dbpfwinfo.getRegion();
					
							if(region.equals("CHQIAD")){
							region="International Airports";
							}
							int j=i+1;
							if(j%2!=0){
						%>
                      <tr class="tbl_rowpad">
                      <%}else{%>
                      <tr class="tbl_rowbg">
                      <%}%>
                        <td  align="left" valign="middle" nowrap="nowrap"> <%=region%> </td>
                        <td align="right" valign="middle" > <%=dbpfwinfo.getAdvance() %></td>
                        <td align="right" valign="middle" > <%=dbpfwinfo.getPfwsubamt() %></td>
                        <td  align="right" valign="middle" > <%=dbpfwinfo.getPfwcontriamt() %> </td>
                      </tr>
                      <%}
                      if(!dbpfwinfo.getRemarks().equals("")){%>
                       <tr class="tbl_rowbg">
                        <td height="18" align="left" valign="middle"><strong></strong></td>
						<td align="left" valign="middle" colspan=3 ><strong>Note :</strong> <%=dbpfwinfo.getRemarks() %></td>
                       <td colspan=2 ></td>
                      </tr>
                      <%}else{%>
                      <tr class="tbl_rowpad">
                        <td height="18" align="left" valign="middle"><strong>Total</strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbPFWDetails.getAdvanceTot() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbPFWDetails.getPfwsubamt() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbPFWDetails.getPfwcontriamt() %></strong></td>
                      </tr>
                      <%}%>
                      </table>
                      </td>
                      <tr>
                  </table></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="10" align="left" valign="top" background="./PensionView/dashboard/images/tblr_img4.gif"><img src="./PensionView/dashboard/images/spacer.gif" width="1" height="1" /></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="24" align="left" valign="top">&nbsp;</td>
        <td align="left" valign="top">&nbsp;</td>
        <td align="left" valign="top">&nbsp;</td>
      </tr>
      <tr>
        <td align="left" valign="top"><table width="462" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="27" align="left" valign="middle" background="./PensionView/dashboard/images/tbl_img1.gif"><table width="450" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="226" class="txthead">Final Payments Year:</td>
               <td width="50"><select name="select2" class="txtfield" id="select2" onchange="calyear();">
						<option value="2011-2012">2011-12</option>
						<option value="2012-2013">2012-13</option>
			    </select></td>
                <td width="34" class="txthead">Month:</td>
                <td width="60"><select name="month2" class="txtfield-month" id="month2" onchange="calyear();">
	                  <option value="--">Select One</option>
	                  <option value="Jan">January</option>
	                  <option value="Feb">February</option>
	                  <option value="Mar">March</option>
	                  <option value="Apr">April</option>
	                  <option value="May">May</option>
	                  <option value="Jun">June</option>
	                  <option value="Jul">July</option>
	                  <option value="Aug">August</option>
	                  <option value="Sep">September</option>
	                  <option value="Oct">October</option>
	                  <option value="Nov">November</option>
	                  <option value="Dec">December</option>
                </select></td>
                <td width="35" class="txthead">Type:</td>
                <td width="60"><select name="type1" class="txtfield2" id="type1" onchange="calyear();">
					  <option value="Y">Payment</option>
					  <option value="N">NotPayment</option>
			    </select></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td align="left" valign="top" background="./PensionView/dashboard/images/tbl_img3.gif"><table width="462" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="167" align="center" valign="top" class="tbl_shade"><table width="446" height="167" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="24" align="center" valign="middle"><strong>In the Year : <%=dbFinalDetails.getFinyear() %></strong></td>
                  </tr>
                  <tr>
                    <td align="left" valign="top"><table width="435" border="0" align="center" cellspacing=0" cellpadding="0">
                  <tr class="tbl_rowbg">
                        <td width="80" height="18" align="left" valign="middle" ><strong>Region</strong></td>
                        <td width="45" align="center" valign="middle" ><strong>Subscription</strong></td>
                        <td width="55" align="center" valign="middle" ><strong>Contribution</strong></td>
                       
                      </tr>
                      
                      <%   	DashBoardInfo dbfinalinfo = new DashBoardInfo();
                  			for(int i=0; i<dbfinalinfoList.size();i++){
							dbfinalinfo=(DashBoardInfo)dbfinalinfoList.get(i);
							String region=dbfinalinfo.getRegion();
					
							if(region.equals("CHQIAD")){
							region="International Airports";
							}
							int j=i+1;
							if(j%2!=0){
						%>
                      <tr class="tbl_rowpad">
                      <%}else{%>
                      <tr class="tbl_rowbg">
                      <%}%>
                        <td  align="left" valign="middle" nowrap="nowrap"> <%=region%> </td>
                        <td align="right" valign="middle" > <%=dbfinalinfo.getFinalsubamt() %></td>
                        <td  align="right" valign="middle" > <%=dbfinalinfo.getFinalcontriamt() %> </td>
                      </tr>
                      <%}
                      if(!dbfinalinfo.getRemarks().equals("")){%>
                       <tr class="tbl_rowbg">
                        
						<td align="center" valign="middle" colspan=3 ><strong>Note :</strong> <%=dbfinalinfo.getRemarks() %></td>
                       
                      </tr>
                      <%}else{%>
                      <tr class="tbl_rowpad">
                        <td height="18" align="left" valign="middle"><strong>Total</strong></td>  
                        <td align="right" valign="middle"  ><strong><%=dbFinalDetails.getFinalsubamtTot() %></strong></td>
                        <td align="right" valign="middle"  ><strong><%=dbFinalDetails.getFinalcontriamtTot()%></strong></td>
                      </tr>
                      <%}%>
                      </table>
                      </td>
                      <tr>
                  </table></td>
              </tr>
            </table></td>
          </tr>
          
          
          <!--
          <tr>
            <td align="left" valign="top" background="./PensionView/dashboard/images/tbl_img3.gif"><table width="462" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="167" align="center" valign="top" class="tbl_shade"><table width="446" height="167" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="top">&nbsp;</td>
                  </tr>
                </table></td>
              </tr>
            </table></td>
          </tr>
          --><tr>
            <td height="10" align="left" valign="top" background="./PensionView/dashboard/images/tbl_img4.gif"><img src="images/spacer.gif" width="1" height="1" /></td>
          </tr>
        </table></td>
        <td align="left" valign="top">&nbsp;</td>
        <td align="left" valign="top"><table width="462" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="27" align="left" valign="middle" background="./PensionView/dashboard/images/tblr_img1.gif"><table width="350" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="212" class="txthead">Investment :</td>
                <td width="138"><input name="textfield" type="text" class="txtfield1" id="textfield" value="30/Apr/2011" /></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td align="left" valign="top" background="./PensionView/dashboard/images/tbl_img3.gif"><table width="462" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td height="167" align="center" valign="top" class="tbl_shade"><table width="446" height="167" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="top">&nbsp;</td>
                  </tr>
                </table></td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="10" align="left" valign="top" background="./PensionView/dashboard/images/tblr_img4.gif"><img src="./PensionView/dashboard/images/spacer.gif" width="1" height="1" /></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="34" align="center" valign="middle" class="footer"><table width="950" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="475" align="left" valign="middle" class="footer-text">© 2011 Airport Information Management System, All Rights Reserved.</td>
        <td width="475" align="right" class="footer-text">Designed &amp; Developed By: <strong>Navayuga Infotech</strong></td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
