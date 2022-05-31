<%
 // ##########################################
 // #Date					Developed by			Issue description
 // #09-Dec-2011        	Prasanthi			    For all the Form the Link Name Should get End as Standard Format
 // #########################################
 %>
<% String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009"};
 %>
<%@ page language="java" import="java.util.*" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
<link href="<%=basePath%>PensionView/css/tooltip.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript"><!--
function resetValue(usertype){
	   
	   if (usertype=='NODAL OFFICER' || usertype=='User'){
	   document.forms[0].action="<%=basePath%>PensionView/PensionMenu2.jsp";
	   }else if(usertype=='Admin'){
	    document.forms[0].action="<%=basePath%>PensionView/PensionMenu.jsp";
	   }
	  
	   
	 	document.forms[0].method="post";
		document.forms[0].submit();
	}
 
	
		function showTooltip(e,tooltipTxt)
		{
			
			var bodyWidth = Math.max(document.body.clientWidth,document.documentElement.clientWidth) - 20;
		
			if(!dhtmlgoodies_tooltip){
				dhtmlgoodies_tooltip = document.createElement('DIV');
				dhtmlgoodies_tooltip.id = 'dhtmlgoodies_tooltip';
				dhtmlgoodies_tooltipShadow = document.createElement('DIV');
				dhtmlgoodies_tooltipShadow.id = 'dhtmlgoodies_tooltipShadow';
				
				document.body.appendChild(dhtmlgoodies_tooltip);
				document.body.appendChild(dhtmlgoodies_tooltipShadow);	
				
				if(tooltip_is_msie){
					dhtmlgoodies_iframe = document.createElement('IFRAME');
					dhtmlgoodies_iframe.frameborder='5';
					dhtmlgoodies_iframe.style.backgroundColor='#FFFFFF';
					dhtmlgoodies_iframe.src = '#'; 	
					dhtmlgoodies_iframe.style.zIndex = 100;
					dhtmlgoodies_iframe.style.position = 'absolute';
					document.body.appendChild(dhtmlgoodies_iframe);
				}
				
			}
			
			dhtmlgoodies_tooltip.style.display='block';
			dhtmlgoodies_tooltipShadow.style.display='block';
			if(tooltip_is_msie)dhtmlgoodies_iframe.style.display='block';
			
			var st = Math.max(document.body.scrollTop,document.documentElement.scrollTop);
			if(navigator.userAgent.toLowerCase().indexOf('safari')>=0)st=0; 
			var leftPos = e.clientX + 10;
			
			dhtmlgoodies_tooltip.style.width = null;	// Reset style width if it's set 
			dhtmlgoodies_tooltip.innerHTML = tooltipTxt;
			dhtmlgoodies_tooltip.style.left = leftPos + 'px';
			dhtmlgoodies_tooltip.style.top = e.clientY + 10 + st + 'px';

			
			dhtmlgoodies_tooltipShadow.style.left =  leftPos + dhtmlgoodies_shadowSize + 'px';
			dhtmlgoodies_tooltipShadow.style.top = e.clientY + 10 + st + dhtmlgoodies_shadowSize + 'px';
			
			if(dhtmlgoodies_tooltip.offsetWidth>dhtmlgoodies_tooltipMaxWidth){	/* Exceeding max width of tooltip ? */
				dhtmlgoodies_tooltip.style.width = dhtmlgoodies_tooltipMaxWidth + 'px';
			}
			
			var tooltipWidth = dhtmlgoodies_tooltip.offsetWidth;		
			if(tooltipWidth<dhtmlgoodies_tooltipMinWidth)tooltipWidth = dhtmlgoodies_tooltipMinWidth;
			
			
			dhtmlgoodies_tooltip.style.width = tooltipWidth + 'px';
			dhtmlgoodies_tooltipShadow.style.width = dhtmlgoodies_tooltip.offsetWidth + 'px';
			dhtmlgoodies_tooltipShadow.style.height = dhtmlgoodies_tooltip.offsetHeight + 'px';		
			
			if((leftPos + tooltipWidth)>bodyWidth){
				dhtmlgoodies_tooltip.style.left = (dhtmlgoodies_tooltipShadow.style.left.replace('px','') - ((leftPos + tooltipWidth)-bodyWidth)) + 'px';
				dhtmlgoodies_tooltipShadow.style.left = (dhtmlgoodies_tooltipShadow.style.left.replace('px','') - ((leftPos + tooltipWidth)-bodyWidth) + dhtmlgoodies_shadowSize) + 'px';
			}
			
			if(tooltip_is_msie){
				dhtmlgoodies_iframe.style.left = dhtmlgoodies_tooltip.style.left;
				dhtmlgoodies_iframe.style.top = dhtmlgoodies_tooltip.style.top;
				dhtmlgoodies_iframe.style.width = dhtmlgoodies_tooltip.offsetWidth + 'px';
				dhtmlgoodies_iframe.style.height = dhtmlgoodies_tooltip.offsetHeight + 'px';
			
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
		var transferFlag,airportcode,regionID;
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
			transferFlag=document.forms[0].transferStatus.options[document.forms[0].transferStatus.selectedIndex].value;
			if(document.forms[0].select_airport.length>1){
				airportcode=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].value;
			}else{
				airportcode=document.forms[0].select_airport.value;
			}
			var url ="<%=basePath%>psearch?method=getPFIDList&frm_region="+regionID+"&frm_airportcode="+airportcode+"&frm_transflag="+transferFlag;
			xmlHttp.open("post", url, true);
			xmlHttp.onreadystatechange = getPFIDNavigationList;
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
		 process.style.display="none";
	}
	function generateForm(){
		var formType;
		formType=document.forms[0].select_formType.options[document.forms[0].select_formType.selectedIndex].text;
		if(formType=='Employee Wise'){
		}
	}
--></script>
	</HEAD>
	<body class="BodyBackground" onload="javascript:frmload()">
		<%String monthID = "", yearDescr = "", region = "", monthNM = "";

            ArrayList yearList = new ArrayList();
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

			<table width="40%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="1" align="center" class="ScreenMasterHeading">
						Download Standard Formats for CPF Data
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				
				  <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/AAIEPF_1.xls" target="_blank" title="Click Here To Download Opening Balance  Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-1</font> </a> </td>
               </tr>
               <tr><td>&nbsp;</td></tr>	             
                <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/AAIEPF_2.xls" target="_blank" title="Click Here To Download Adjustment In Opening Balance Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-2 </font></a> </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/AAIEPF_3.xls" target="_blank" title="Click Here To Download Monthly CPF Recovery Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-3</font></a>  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/AAIEPF-3-SUPPL.xls" target="_blank" title="Click Here To Download the AAIEPF-3-SUPPL Standard format" class="link"><font color="BLUE" size="2">AAIEPF-3-SUPPL</font></a> </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
 				 <tr>
                <td align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="PensionView/AAIEPFFormats/AAIEPF_3A.xls" target="_blank" title="Click Here To Download Adjustment of CPF Recovery of Previous Month  Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-3A </font></a> </td>
              </tr>
              <tr><td>&nbsp;</td></tr>
              <tr>
                <td align="center">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="PensionView/AAIEPFFormats/AAIEPF_4.xls" target="_blank" title="Click Here To Download CPF Received From Other Organisation  Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-4 </font></a> </td>
              </tr>
              <tr><td>&nbsp;</td></tr>
 				<tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/AAIEPF_8.xls" target="_blank" title="Click Here To Download Refundable Advance/NRFW/PFW & Final Settlement  Standard Format" class="link"><font color="BLUE" size="2">AAIEPF FORM-8 </font></a>  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/ARREARBREAKUP_UPLOAD_FORMAT.xls" target="_blank" title="Click Here To Download MonthWise ArrearBreakUp Standard Format" class="link"><font color="BLUE" size="2">MonthWise ArrearBreakUp Format </font></a>  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
               <tr>
                  <td align="center"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>PensionView/AAIEPFFormats/ARREARBREAKUP_SINGLELINEST_FORMAT.xls" target="_blank" title="Click Here To Download Single Line Statement Standard Format" class="link"><font color="BLUE" size="2">Single Line Format </font></a>  </td>
               </tr>
               <tr><td>&nbsp;</td></tr>
              <tr>
					<td align="center">&nbsp;</td>
				</tr>				
                 <TR><td align="right"><a href="#" onclick="resetValue('<%=(String)session.getAttribute("usertype")%>');"><img src="<%=basePath%>PensionView/images/viewBack.gif" border="0" alt="Back" ></a></td></TR>
				<tr>
					<td align="center">&nbsp;</td>
				</tr>
            
			</table>



		</form>
<div id="process" style="position: fixed;width: auto;height:35%;top: 200px;right: 0;bottom: 100px;left: 10em;" align="center" >
			<img src="<%=basePath%>PensionView/images/Indicator.gif" border="no" align="middle"/>
			
		</div>

	</body>
</html>
