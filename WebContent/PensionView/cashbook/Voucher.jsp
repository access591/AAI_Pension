
<%@ page language="java" pageEncoding="UTF-8"%>

<%
String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("/PensionIndex.jsp");
                rd.forward(request, response);
            }
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
String type = request.getParameter("type");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI - CashBook - Voucher</title>

		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="Voucher Page" />

		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath%>scripts/calendar.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/DateTime1.js"></script>
		<script type="text/javascript" src="<%=basePath%>scripts/overlib.js"></script>
		<script type="text/javascript">
		
			function showParty(){			
				if(document.forms[0].party.value=="E"){ 
	    
		   		     divNomineeHead.style.display="block";
		   		     divNomineeHead1.style.display="block";
		   		     divNominee2.style.display="none";
		   		}
		   		if(document.forms[0].party.value=="P"){   	
	  	    
					divNominee2.style.display="block";
					divNomineeHead.style.display="none"; 
					divNomineeHead1.style.display="none"; 
	   		    }
	   		    if(document.forms[0].party.value==""){
 
	   		    	divNominee2.style.display="none";
					divNomineeHead.style.display="none"; 
					divNomineeHead1.style.display="none";
	   		    }
			}
			
			function popupWindow(mylink, windowname){
				if (! window.focus){
					return true;
				}
				var href;
				if (typeof(mylink) == 'string'){
				   href=mylink;
				} else {
					href=mylink.href;
				}
				progress=window.open(href, windowname, 'width=750,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
				return true;
			}
		
	
			function empDetails(pfid,name,desig){
			   		document.forms[0].eName.value=name;
					document.forms[0].epfid.value=pfid;
					document.forms[0].edesignation.value=desig;
					suggestions.style.display="none";	
			}
			
			function partyDetails(pName,pCode) {   	 
				document.forms[0].pName.value=pName;
				document.forms[0].pCode.value=pCode;
			}
			
			function checkVoucher(){
				
				if(document.forms[0].bankName.value == ""){
					alert("Please Enter Bank Name (Mandatory)");
					document.forms[0].bankName.focus();
					return false;
				}
				if(document.forms[0].year.value == ""){
					alert("Please Select Financial Year (Mandatory)");
					document.forms[0].year.focus();
					return false;
				}
				if(document.forms[0].trusttype.value == ""){
					alert("Please Select Trust type (Mandatory)");
					document.forms[0].trusttype.focus();
					return false;
				}
	
				if(document.forms[0].vouchertype.value != "C"){
					if(document.forms[0].party.value == ""){
						alert("Please Select Party (Mandatory)");
						document.forms[0].party.focus();
						return false;
					}
					if(document.forms[0].party.value == "E"){
						if(document.forms[0].eName.value == ""){
							alert("Please Enter Employee Name (Mandatory)");
							document.forms[0].eName.focus();
							return false;
						}
					}else if(document.forms[0].party.value == "P"){
						if(document.forms[0].pName.value == ""){
							alert("Please Enter Party Name (Mandatory)");
							document.forms[0].pName.focus();
							return false;
						}
					}						
				}else {
					if(document.forms[0].contraBankName.value == ""){
						alert("Please Select Bank Name (Mandatory)");
						document.forms[0].contraBankName.focus();
						return false;
					}
					if(document.forms[0].accountNo.value == document.forms[0].contraAccountNo.value){
						alert("Please Select Another Bank Name \n The Accounts selected  are same");					
						document.forms[0].contraBankName.focus();
						return false;
					}
				}		
				if(document.forms[0].prepDate.value == ""){
					alert("Please Enter To Date (Mandatory)");
					document.forms[0].prepDate.focus();
					return false;
				}
				if(!convert_date(document.forms[0].prepDate)){
					return false;
				}
				convert_date(document.forms[0].prepDate);						
				var len = detailsRec.length;
				if(len==0){
					alert("Please Enter Account Head Details and Save (Mandatory)");
					return false;
				}
				var temp = '';	
				var debit = 0;
				var credit = 0;
				for(var i=0;i<len;i++){
					if(detailsRec[i][3]=='')
						detailsRec[i][3] = ' ';
					if(detailsRec[i][4]=='')
						detailsRec[i][4] = ' ';
					temp = detailsRec[i][0]+'|'+detailsRec[i][2]+'|'+detailsRec[i][3]+'|'+detailsRec[i][4]+'|'+detailsRec[i][5]+'|'+detailsRec[i][6];
					debit += parseFloat(detailsRec[i][5]);
					credit += parseFloat(detailsRec[i][6]);
					document.forms[0].detailRecords.options[document.forms[0].detailRecords.options.length]=new Option('x',temp);
					document.forms[0].detailRecords.options[document.forms[0].detailRecords.options.length-1].selected=true;
				}
				if(document.forms[0].vouchertype.value=='P'){
					var amount = debit-credit;
					if(amount < 0){
						alert("For Payment Voucher Debit Amount Should be Greater than Credit Amount");
						return false;
					}
				}else if(document.forms[0].vouchertype.value=='R'){
					var amount = credit - debit;
					if(amount < 0){
						alert("For Payment Voucher Credit Amount Should be Greater than Debit Amount");
						return false;
					}
				}
				
			}
			
			function yearSelect(){
				var date = new Date();
				var year = parseInt(date.getYear());				
				var month = parseInt(date.getMonth())+1;	
				var year1 = parseInt((month<=3)?(year-1):(year));				
				for(cnt=2003;cnt<=year1 ;cnt++){					
					var yearEnd = (""+(cnt+1)).substring(2,4);
					document.forms[0].year.options.add(new Option(cnt+"-"+yearEnd,cnt+"-"+yearEnd)) ;				
				}
				document.forms[0].year.options[document.forms[0].year.options.length-1].selected=true;
				document.forms[0].bankName.focus();
				var month_values = new Array ("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
				for(cnt=0;cnt<12 ;cnt++){					
					document.forms[0].dmonth.options.add(new Option(month_values[cnt],month_values[cnt])) ;	
				/*	if(cnt+1==month){
						document.forms[0].dmonth.options[document.forms[0].dmonth.options.length-1].selected=true;
					}*/
				}					
				for(cnt=2003;cnt<=year ;cnt++){					
					document.forms[0].dyear.options.add(new Option(cnt,cnt)) ;				
				}
				//document.forms[0].dyear.options[document.forms[0].dyear.options.length-1].selected=true;				
			}	
			
			function otherBankDetails(bankName,accountNo,accHead,particular){
				document.forms[0].contraBankName.value=bankName;
				document.forms[0].contraAccountNo.value=accountNo;
				document.forms[0].accHead.value=accHead;
		   		document.forms[0].particular.value=particular;	
		   		suggestions.style.display="none";	
			}
			function bankDetails(bankName,accountNo,trustType){
				document.forms[0].bankName.value=bankName;
				document.forms[0].accountNo.value=accountNo;	
				document.forms[0].trusttype.value=(trustType=="null"?"":trustType);	
				suggestions.style.display="none";	
				
			}
			function details(accHead,particular){	
				if(document.forms[0].accType.value=='Y'){
		   			document.forms[0].accHead1.value=accHead;
		   			document.forms[0].particular1.value=particular;
		   		}else{
		   			document.forms[0].accHead.value=accHead;
		   			document.forms[0].particular.value=particular;
		   		}
			}
			function partyDetails(partyName,detail){
		   		document.forms[0].pName.value=partyName;
			}
			function validate_monyear(monYear){
				var monYear = document.forms[0].monthYear.value;
				var mon =  monYear.substr(0,monYear.indexOf("/"));
				var year = parseFloat(monYear.substr(monYear.indexOf("/")+1,monYear.length));
				if(mon.length<3){
					alert("Please Enter Month/Year in the format of 'Mon/YYYY'");
					document.forms[0].monthYear.focus();
					return false;
				}
				mon = mon.toUpperCase(); 
				var bool = false;
				var month_values = new Array ("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
				for ( var i=0; i<12; i++ ) {
   					if ( mon == month_values[i]){
   						bool = true;
   						break;
   					}
   				}   				
   				if(!bool){
					alert("Please Enter Valid Month");
					document.forms[0].monthYear.focus();
					return false;
				}
				if(year < 1900){
					alert("Please Enter Valid Year");
					document.forms[0].monthYear.focus();
					return false;
				}
				return true;
			}
			function detailsClear(){
				document.forms[0].accHead.value='';
				document.forms[0].particular.value='';
				document.forms[0].dmonth.value='';
				document.forms[0].dyear.value='';
				document.forms[0].debit.value='';
				document.forms[0].cheque.value='';
				document.forms[0].credit.value='';
				document.forms[0].details.value='';				
			}
			var detailsRec = new Array();
			function saveDetails(){
				if(document.forms[0].accHead.value == ""){
					alert("Please Select Account Head (Mandatory)");
					document.forms[0].accHead.focus();
					return false;
				}
				if(document.forms[0].vouchertype.value !='C' && document.forms[0].party.value == ""){
					alert("Please Select Party (Mandatory)");
					document.forms[0].party.focus();
					return false;
				}
				if(document.forms[0].vouchertype.value !='C' && document.forms[0].party.value == "E"){
					if(document.forms[0].dmonth.value == ""){
						alert("Please Select Month (Mandatory)");
						document.forms[0].dmonth.focus();
						return false;
					}
					if(document.forms[0].dyear.value == ""){
						alert("Please Select Year (Mandatory)");
						document.forms[0].dyear.focus();
						return false;
					}
				}
				if(document.forms[0].debit.value == "" ){
					document.forms[0].debit.value = "0";
				}
				if(!ValidateNum(document.forms[0].debit.value)){
					alert("Please Enter A Valid Debit Amount");
					document.forms[0].debit.focus();
					return false;
				}
				if(document.forms[0].credit.value == "" ){
					document.forms[0].credit.value = "0";
				}
				if(!ValidateNum(document.forms[0].credit.value)){
					alert("Please Enter A Valid Credit Amount");
					document.forms[0].credit.focus();
					return false;
				}
				if(parseFloat(document.forms[0].credit.value) == 0 && parseFloat(document.forms[0].debit.value) == 0){
					alert("Please Enter Credit or Debit Amount");
					document.forms[0].debit.focus();
					return false;
				}
				
				detailsRec[detailsRec.length]=[document.forms[0].accHead.value,document.forms[0].particular.value,(document.forms[0].dmonth.value==document.forms[0].dyear.value?"":document.forms[0].dmonth.value+"/"+document.forms[0].dyear.value),document.forms[0].details.value,document.forms[0].cheque.value,document.forms[0].debit.value,document.forms[0].credit.value];
				showValues(-1);
				detailsClear();
			}
			
			function showValues(index){  
				var str='<TABLE border=1 cellpadding=0 cellspacing=0  bordercolor=snow width="100%">';
				for(var i=0;i<detailsRec.length;i++){  
					str+='<TR>';
					if(i==index)	{ 
						str+='<TD align=center >';
						str+='<input type=text name=accHead1 value=\''+detailsRec[i][0]+'\' style=width:60px maxlength=15 readonly=readonly><img style="cursor:hand" src="<%=basePath%>images/search1.gif" onclick="document.forms[0].accType.value=\'Y\';popUpAcc();" alt="Click The Icon to Select Bank Account Code Master Records" /><img style="cursor:hand" src="<%=basePath%>images/add.gif" onclick="popupWindow(\'<%=basePath%>cashbook/AccountingCodePopup.jsp\',\'AAI\');" alt="Click The Icon to Select Bank Master Records" /></TD>';
						str+='<TD align=center >';
						str+='<input type=text name=particular1 value=\''+detailsRec[i][1]+'\' style=width:70px maxlength=25></TD>';
						str+='<TD align=center>';
						str+='<select name=dmonth1 style="width:15mm"><option value="">Select </option></select><select name=dyear1 style="width:15mm"><option value="">Select </option></select></TD>';
						str+='<TD align=center >';
						str+='<input type=text name=details1 value=\''+detailsRec[i][3]+'\' style=width:70px maxlength=20></TD>';
						str+='<TD align=center >';
						str+='<input type=text name=cheque1 value=\''+detailsRec[i][4]+'\' style=width:70px maxlength=10></TD>';
						str+='<TD align=center >';
						str+='<input type=text name=debit1 value=\''+detailsRec[i][5]+'\' style=width:70px maxlength=10></TD>';
						str+='<TD align=center >';
						str+='<input type=text name=credit1 value=\''+detailsRec[i][6]+'\' style=width:70px maxlength=10></TD>';
						str+='<TD align=center nowrap><a href=# onclick="editDetails('+i+');">';
						str+='&nbsp;<img src="<%=basePath%>/images/saveIcon.gif" border=0 alt=save></a>';
						str+='<a href=# onclick=del('+i+')>';
						str+='<img src="<%=basePath%>/images/cancelIcon.gif" alt=delete border=0 width=20 height=20></a></TD>';
						document.all['addDetails'].innerHTML = str;												
					} else { 
						str +='<TR>';
						for(var j=0;j<7;j++){	
							if(detailsRec[i][j].length<10){
								str+='<TD align=center   nowrap style=width:15%>'+detailsRec[i][j]+'</TD>';
							}else{
								str+="<TD align=center nowrap style=width:15%>"+detailsRec[i][j].substring(0,10);
								str+="<a class=data href=# onMouseOver = \"overlib('"+detailsRec[i][j]+"\')\" onMouseOut='nd()'>...</a></TD>";				
							}						
						}
						str +='<TD><a href=# onclick=showValues('+i+')><img src="<%=basePath%>/images/editGridIcon.gif" alt="edit" border=0></a><a href=# onclick=del('+i+')><img src="<%=basePath%>/images/cancelIcon.gif" alt=delete border=0 width=20 height=20></a></TD></TR>';								
					}	
				}
			 	str+='</TABLE>';      
				document.all['addDetails'].innerHTML = str;	
				if(index!=-1){
					var month_values = new Array ("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
					var date = new Date();
					var year = parseInt(date.getYear());
					for(var cnt=0;cnt<12 ;cnt++){					
						document.forms[0].dmonth1.options.add(new Option(month_values[cnt],month_values[cnt])) ;					
					}					
					for(var cnt=2003;cnt<=year ;cnt++){					
						document.forms[0].dyear1.options.add(new Option(cnt,cnt)) ;				
					}	
					document.forms[0].dmonth1.value=detailsRec[index][2].substring(0,3);
					document.forms[0].dyear1.value=detailsRec[index][2].substring(4,8);
				}
			}
			function editDetails(index) {
			 	if(document.forms[0].accHead1.value == ""){
					alert("Please Select Account Head (Mandatory)");
					document.forms[0].accHead.focus();
					return false;
				}
				if(document.forms[0].vouchertype.value !='C' && document.forms[0].party.value == ""){
					alert("Please Select Party (Mandatory)");
					document.forms[0].party.focus();
					return false;
				}
				if(document.forms[0].vouchertype.value !='C' && document.forms[0].party.value == "E"){
					if(document.forms[0].dmonth1.value == ""){
						alert("Please Select Month (Mandatory)");
						document.forms[0].dmonth.focus();
						return false;
					}
					if(document.forms[0].dyear1.value == ""){
						alert("Please Select Year (Mandatory)");
						document.forms[0].dyear.focus();
						return false;
					}
				}
				if(document.forms[0].debit1.value == "" ){
					document.forms[0].debit1.value = "0";
				}
				if(!ValidateNum(document.forms[0].debit1.value)){
					alert("Please Enter A Valid Debit Amount");
					document.forms[0].debit1.focus();
					return false;
				}
				if(document.forms[0].credit1.value == "" ){
					document.forms[0].credit1.value = "0";
				}
				if(!ValidateNum(document.forms[0].credit1.value)){
					alert("Please Enter A Valid Credit Amount");
					document.forms[0].credit1.focus();
					return false;
				}
				if(parseFloat(document.forms[0].credit1.value) == 0 && parseFloat(document.forms[0].debit1.value) == 0){
					alert("Please Enter Credit or Debit Amount");
					document.forms[0].debit1.focus();
					return false;
				}
				detailsRec[index]=[document.forms[0].accHead1.value,document.forms[0].particular1.value,(document.forms[0].dmonth1.value==document.forms[0].dyear1.value?"":document.forms[0].dmonth1.value+"/"+document.forms[0].dyear1.value),document.forms[0].details1.value,document.forms[0].cheque.value,document.forms[0].debit1.value,document.forms[0].credit1.value];
				showValues(-1);
			}
			function del(index) {
				var temp=new Array();
				for(var i=0;i<detailsRec.length;i++)
				{
					if(i!=index)
						temp[temp.length]=detailsRec[i];
		
				}
				detailsRec=temp;
				showValues(-1);
				return false;
			}
			function popUpAcc(){
				var accHeads = '';
				var len = detailsRec.length;
				for(var i=0;i<len;i++){		
					accHeads += "|"+detailsRec[i][0]+"|";
				}
				if(len==0){
					popupWindow('<%=basePath%>cashbook/AccountInfo.jsp','AAI');
				}else{
					popupWindow('<%=basePath%>cashbook/AccountInfo.jsp?type=rem&&AccHead='+accHeads,'AAI');
				}
			}
			var xmlHttp;
			function createXMLHttpRequest(){
				if(window.ActiveXObject) {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			 	} else if (window.XMLHttpRequest) {
					xmlHttp = new XMLHttpRequest();
			 	}
			}
			function getNodeValue(obj,tag) { 
				if(obj.getElementsByTagName(tag)[0].firstChild){
					return obj.getElementsByTagName(tag)[0].firstChild.nodeValue;
				}else return "";
			}
			var banktype="";
			
			function getBankDetails(type){
				var bankName='';
				banktype = type;
				if(type=='B')
	      	 		bankName=document.forms[0].bankName.value; 
	      	 	else
	      	 		bankName=document.forms[0].contraBankName.value; 
      	 		createXMLHttpRequest();	
      	 		var url ="<%=basePathBuf%>BankMaster?method=getBankList&&type=ajax&&bankName="+bankName;
    			xmlHttp.open("post", url, true);
				xmlHttp.onreadystatechange = getBankList;
				xmlHttp.send(null);
			}
			function getBankList(){
				if(xmlHttp.readyState ==4){		
					if(xmlHttp.status == 200){ 
						var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					  	if(stype.length==0){
					 		var bankName = document.getElementById("bankName");					   	  
					  	}else{
							suggestions.style.display="block";
							suggestions.innerHTML="";
					  		var str="";
					  		str+="<table  class=border border=1 width=100% ><tr><td>Account No.</td><td>Bank Name</td></tr>"; 
					 		for(i=0;i<stype.length;i++){
					 			if(banktype=='B'){
						 			var bankName= getNodeValue(stype[i],'bankName');						 		
							 		var accountNo= getNodeValue(stype[i],'accountNo');
							 		var trustType= getNodeValue(stype[i],'trustType');
					 	 	 	 	str+="<tr><td><a href='#' onclick=\"bankDetails('"+bankName+"','"+accountNo+"','"+trustType+"');\" >"+accountNo+"</a></td><td><a href='#' onclick=\"bankDetails('"+bankName+"','"+accountNo+"','"+trustType+"');\" >"+bankName+"</a></td></tr>";					 				
					 	 	 	 }else if(document.forms[0].accountNo.value!=getNodeValue(stype[i],'accountNo')){
							 		var bankName= getNodeValue(stype[i],'bankName');						 		
							 		var accountNo= getNodeValue(stype[i],'accountNo');
							 		var accHead= getNodeValue(stype[i],'accHead');
							 		var particular= getNodeValue(stype[i],'particular');
					 	 	 	 	str+="<tr><td><a href='#' onclick=\"otherBankDetails('"+bankName+"','"+accountNo+"','"+accHead+"','"+particular+"');\" >"+accountNo+"</a></td><td><a href='#' onclick=\"otherBankDetails('"+bankName+"','"+accountNo+"','"+accHead+"','"+particular+"');\" >"+bankName+"</a></td></tr>";					 					
					 	 	 	 }
				 	 		}
							str+="</table>";
				 	    	suggestions.innerHTML=str;				 	    	
						}
					}
				}
			}
			function getEmpDetails(){
      	 		var eName=document.forms[0].eName.value; 
      	 		createXMLHttpRequest();	
      	 		var url ="<%=basePathBuf%>Employee?method=getEmployeeList&&type=ajax&&empName="+eName;
    			xmlHttp.open("post", url, true);
				xmlHttp.onreadystatechange = getEmpList;
				xmlHttp.send(null);
			}
			function getEmpList(){
				if(xmlHttp.readyState ==4){		
					if(xmlHttp.status == 200){ 
						var stype = xmlHttp.responseXML.getElementsByTagName('ServiceType');
					  	if(stype.length==0){
					 		var bankName = document.getElementById("bankName");					   	  
					  	}else{
							suggestions.style.display="block";
							suggestions.innerHTML="";
					  		var str="";
					  		str+="<table  class=border border=1 width=100% ><tr><td>PF ID</td><td>Name</td><td>Desig.</td></tr>"; 
					 		for(i=0;i<stype.length;i++){
						 		var eName= getNodeValue(stype[i],'eName');
						 		var epfid= getNodeValue(stype[i],'epfid');
						 		var edesignation= getNodeValue(stype[i],'edesignation');
				 	 	 	 	str+="<tr><td><a href='#' onclick=\"empDetails('"+epfid+"','"+eName+"','"+edesignation+"');\" >"+epfid+"</a></td><td><a href='#' onclick=\"empDetails('"+epfid+"','"+eName+"','"+edesignation+"');\" >"+eName+"</a></td><td><a href='#' onclick=\"empDetails('"+epfid+"','"+eName+"','"+edesignation+"');\" >"+edesignation+"</a></td></tr>";
				 	 		}
							str+="</table>";
				 	    	suggestions.innerHTML=str;				 	    	
						}
					}
				}
			}
	 	</script>
	</head>
	<body class="BodyBackground1" onload="yearSelect();document.forms[0].searchImage.focus();suggestions.style.display='none'; ">
		<form name="vocher" action="<%=basePathBuf%>Voucher?method=addVoucher" onsubmit="javascript : return checkVoucher()" method="post" action="">
			<input type=hidden name=accType value="N" />
			<input type="hidden" name='vouchertype' value='<%=type%>' />
			<div id="overDiv" style="position:absolute; visibility:hide;z-index:1;"></div>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">

				<tr>
					<td>
						<jsp:include page="/PensionView/cashbook/PensionMenu.jsp"  />
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">

									<%="C".equals(type)?"Contra  ":"P".equals(type)?"Payment  ":"Receipt  "%>Voucher[Add] &nbsp;&nbsp;
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%">
									<table align="center" border="0" width="70%" cellspacing="5">
										<tr>
											<td class="label" width="100px" align="right">
												Bank Name &nbsp;
											</td>
											<td align="left">
												<input type="text" name="bankName" size="13" maxlength="50" onkeyup="getBankDetails('B')" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/BankInfo.jsp','AAI');" alt="Click The Icon to Select Bank Master Records" name='searchImage' />
											</td>
											<td class="label" width="100px" align="right">
												Account No &nbsp;
											</td>
											<td align="left">
												<input type="text" name="accountNo" size="13" maxlength="50" readonly="readonly" />

											</td>
										</tr>
										<tr>
											<td class="label" align="right">
												Financial Year &nbsp;
											</td>
											<td align="left">
												<select name='year' style='width:80px'>
													<option value="">
														Select One
													</option>
												</select>
											</td>

											<td class="label" align="right">
												Trust Type &nbsp;
											</td>
											<td align="left">
												<select name='trusttype' style='width:80px'>
													<option value="">
														Select One
													</option>
													<option value='I'>
														IAAI ECPF
													</option>
													<option value='N'>
														NAA ECPF
													</option>
													<option value='A'>
														AAI EPF
													</option>
												</select>
											</td>
										</tr>
										
									<%if(!"C".equals(type)){%>
										<tr>
											<td class="label" align="right">
												Type of Party&nbsp;
											</td>
											<td align="left">
												<select name='party' onchange="showParty();" style='width:80px'>
													<option value="">
														Select One
													</option>
													<option value='E'>
														Employee
													</option>
													<option value='P'>
														Party
													</option>
												</select>
											</td>
											<td class="label" width="100px" align="right">
												Preparation Date
											</td>
											<td align="left" >
												<input name='prepDate' maxlength="11" size="13" tabindex=2/>
												&nbsp; <a href="javascript:show_calendar('forms[0].prepDate')"> <img name="calendar" alt='calendar' border="0" src="<%=basePath%>/images/calendar.gif" src="" alt="" /></a>
												
											</td>
										</tr>
										<tr>
											<td style="height: 5">
											
											</td>
										</tr>
										<tr id="divNomineeHead" style="display:none">
											<td class="label" align="right">
												PF ID
											</td>
											<td align="left">
												<input type="text" size="13" name="epfid" maxlength="50"  width="150px" readonly="readonly" />

												<img src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/EmployeeInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
											</td>
										</tr>
										<tr id="divNomineeHead1" style="display:none">
											<td class="label" align="right">
												Employee Name
											</td>
											<td align="left">
												<input type="text" size="18" name="eName" readonly="readonly" maxlength="50"  width="150px" />
											</td>

											<td class="label" align="right">
												Designation
											</td>
											<td align="left">
												<input type="text" size="18" name="edesignation" maxlength="50"  width="150px" readonly="readonly" />

											</td>
										</tr>
										<tr id="divNominee2" style="display:none">
											<td class="label" align="right">
												Party Name
											</td>
											<td align="left">
												<input type="text" size="13" name="pName" maxlength="50" readonly="readonly" />
												<img src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/PartyInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
											</td>
										</tr>
										<%}else {%>
										<tr >
											<td class="label" nowrap="nowrap" align="right">
												Transfer Bank Name
											</td>
											<td align="left">
												<input type="text" size="13" name="contraBankName" maxlength="50" onkeyup="getBankDetails('T')" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif"
													onclick="if(document.forms[0].accountNo.value !=''){popupWindow('<%=basePath%>cashbook/BankInfo.jsp?type=other&amp;&amp;accountNo='+document.forms[0].accountNo.value,'AAI');}else{alert('Please Select the Bank Name');}"
													alt="Click The Icon to Select Bank Master Records" />
											</td>
											<td class="label" nowrap="nowrap" align="right">
												Account No
											</td>
											<td align="left">
												<input type="text" size="13" name="contraAccountNo" maxlength="50" readonly="readonly" />
											</td>
										</tr>
										<%}%>
									</table>
								</td>
							</tr>

							<tr>
								<td width="100%">
									<table width="100%">
										<tr>
											<td class="label" align="center" width="20%">
												Account head
											</td>
											<td class="label" align="center" width="20%">
												Particular
											</td>
											<td class="label" align="center" width="20%">
												Month/Year
												<br />
												<font color="blue" size="1px">(MON/YYYY) </font>
											</td>
											<td class="label" align="center" width="20%">
												Details
											</td>
											<td class="label" align="center" width="20%">
												Cheque No.
											</td>
											<td class="label" align="center" width="20%">
												Passed/
												<br />
												Debit (Rs.)
											</td>
											<td class="label" align="center" width="20%">
												Deduction/
												<br />
												Credit (Rs.)
											</td>
											<td class="label" align="center">

											</td>
										</tr>
										<tr>
											<td colspan="8" id='addDetails' name='addDetails' width="100%"></td>

										</tr>
										<tr>
											<td nowrap="nowrap" align="center">
												<input type="text" size="10" name="accHead" maxlength="50"  readonly="readonly" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" onclick="popUpAcc();" alt="Click The Icon to Select Bank Account Code Master Records" src="" alt="" />
												<img style='cursor:hand' src="<%=basePath%>images/add.gif" onclick="popupWindow('<%=basePath%>cashbook/AccountingCodePopup.jsp','AAI');" alt="Click The Icon to Select Bank Master Records"  />
											</td>
											<td align="center">
												<input type="text" size="18" name="particular" maxlength="50"  readonly="readonly" />
											</td>
											<td class="label" align="center" nowrap>
												
												<select name=dmonth style='width:15mm'><option value=''>Select </option></select>
												<select name=dyear style='width:15mm'><option value=''>Select </option></select>												
											</td>
											<td align="center">
												<input type="text" size="14" name="details" maxlength="50"  />
											</td>
											<td align="center">
												<input type="text" size="14" name="cheque" maxlength="50"  />
											</td>
											<td align="center">
												<input type="text" size="14" name="debit" maxlength="10"  />
											</td>
											<td align="center">
												<input type="text" size="14" name="credit" maxlength="10"  />
											</td>
											<td nowrap="nowrap">
												<img style='cursor:hand' src="<%=basePath%>images/saveIcon.gif" onclick='saveDetails()'  />
												<img style='cursor:hand' src="<%=basePath%>images/cancelIcon.gif" onclick='detailsClear()'  />
											</td>
										</tr>

									</table>
								</td>
							</tr>
							<tr>
								<td>
									<select name='detailRecords' multiple="multiple" style="DISPLAY:NONE"></select>
									&nbsp;
									<div id="suggestions" class="containdiv1"></div>
								</td>
							</tr>
							<tr>
								<td align="center">
									<table>
										<tr>
											<td class="label" align="right">
												Narration
											</td>
											<td align="left" colspan="4">
												<textarea cols="90" rows="3" name="voucherDetails"></textarea>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table align="center">
										<tr>
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td align="center">

												<input type="submit" class="btn" value="Submit" />
												<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn" />
												<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn" />
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
	</body>
</html>

