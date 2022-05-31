
<%@ page language="java" pageEncoding="UTF-8"%>
<%
StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			 basePathBuf.append("://").append(request.getServerName()).append(":");
			 basePathBuf.append(request.getServerPort());
			 basePathBuf.append( request.getContextPath()).append("/");
			 
String basePath = basePathBuf.toString()+"PensionView/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI - CashBook - Voucher</title>

		<meta http-equiv="pragma" content="no-cache" content="" />
		<meta http-equiv="cache-control" content="no-cache" content="" />
		<meta http-equiv="expires" content="0" content="" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" content="" />
		<meta http-equiv="description" content="Voucher Page" content="" />

		<link rel="stylesheet" href="<%=basePath%>css/aai.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath%>scripts/calendar.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/CommonFunctions.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/DateTime.js" type=""></script>
		<script type="text/javascript" src="<%=basePath%>scripts/overlib.js" type=""></script>
		<script type="text/javascript" type="">
		
			function checkParty(){
				if(document.forms[0].vouchertype.value=="C"){   		      
	   				document.forms[0].party.disabled=true;
	   				divNominee.style.display="block";
		   		    divNominee2.style.display="none";
   					divNomineeHead.style.display="none"; 
   					divNomineeHead1.style.display="none"; 
				}  else{
					document.forms[0].party.disabled=false;
					showParty();
				}
			}
			
			function showParty(){			
				if(document.forms[0].party.value=="E" && document.forms[0].vouchertype.value=="C"){
					document.forms[0].party.value = "P";
					alert("If Voucher Type is Contra then \nParty Type cannot be selected as Employee");
					return false;
				}
				if(document.forms[0].party.value=="E"){ 
					 divNominee.style.display="none";  		    
		   		     divNomineeHead.style.display="block";
		   		     divNomineeHead1.style.display="block";
		   		     divNominee2.style.display="none";
		   		}
		   		if(document.forms[0].party.value=="P"){   	
		   			divNominee.style.display="none";  		  	    
					divNominee2.style.display="block";
					divNomineeHead.style.display="none"; 
					divNomineeHead1.style.display="none"; 
	   		    }
	   		    if(document.forms[0].party.value==""){
		   		    divNominee.style.display="none";  		  
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
				if(document.forms[0].empStatus.value=='Y'){
					document.forms[0].preparedBy.value=name;
					document.forms[0].prepepfid.value=pfid;
					document.forms[0].empStatus.value=='N';
				}else{
			   		document.forms[0].eName.value=name;
					document.forms[0].epfid.value=pfid;
					document.forms[0].edesignation.value=desig;
				}
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
				if(document.forms[0].vouchertype.value == ""){
					alert("Please Select Type of Voucher (Mandatory)");
					document.forms[0].vouchertype.focus();
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
				if(parseFloat(document.forms[0].preparedBy.value) == 0){
					alert("Please Enter Prepared By (Mandatory)");
					document.forms[0].preparedBy.focus();
					return false;
				}
				var len = detailsRec.length;
				if(len==0){
					alert("Please Enter Account Head Details and Save (Mandatory)");
					return false;
				}
				var temp = '';	

				for(var i=0;i<len;i++){
					if(detailsRec[i][3]=='')
						detailsRec[i][3] = ' ';
					temp = detailsRec[i][0]+'|'+detailsRec[i][2]+'|'+detailsRec[i][3]+'|'+detailsRec[i][4];
					document.forms[0].detailRecords.options[document.forms[0].detailRecords.options.length]=new Option('x',temp);
					document.forms[0].detailRecords.options[document.forms[0].detailRecords.options.length-1].selected=true;
				}				 
			}
			
			function yearSelect(){
				var date = new Date();
				var year = parseInt(date.getYear());
				for(cnt=2003;cnt<=year;cnt++){
					var yearEnd = (""+(cnt+1)).substring(2,4);
					document.forms[0].year.options.add(new Option(cnt+"-"+yearEnd,cnt+"-"+yearEnd)) ;				
				}
				document.forms[0].bankName.focus();
			}	
			
			function otherBankDetails(bankName,accountNo,accHead,particular){
				document.forms[0].contraBankName.value=bankName;
				document.forms[0].contraAccountNo.value=accountNo;
				document.forms[0].accHead.value=accHead;
		   		document.forms[0].particular.value=particular;	
			}
			function bankDetails(bankName,accountNo){
				document.forms[0].bankName.value=bankName;
				document.forms[0].accountNo.value=accountNo;		
			}
			function details(accHead,particular){	
		   		document.forms[0].accHead.value=accHead;
		   		document.forms[0].particular.value=particular;
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
				document.forms[0].monthYear.value='';
				document.forms[0].amount.value='';
				document.forms[0].details.value='';				
			}
			var detailsRec = new Array();
			function saveDetails(){
				if(document.forms[0].accHead.value == ""){
					alert("Please Select Account Head (Mandatory)");
					document.forms[0].accHead.focus();
					return false;
				}
				if(document.forms[0].monthYear.value == ""){
					alert("Please Enter Month/Year (Mandatory)");
					document.forms[0].monthYear.focus();
					return false;
				}
				if(!validate_monyear(document.forms[0].monthYear.value)){
					return false;
				}
				if(document.forms[0].amount.value == "" ){
					alert("Please Enter Amount (Mandatory)");
					document.forms[0].amount.focus();
					return false;
				}
				if(!ValidateNum(document.forms[0].amount.value)){
					alert("Please Enter A Valid  Amount");
					document.forms[0].amount.focus();
					return false;
				}
				if(parseFloat(document.forms[0].amount.value) == 0){
					alert("Please Enter  Amount");
					document.forms[0].amount.focus();
					return false;
				}
				detailsRec[detailsRec.length]=[document.forms[0].accHead.value,document.forms[0].particular.value,document.forms[0].monthYear.value,document.forms[0].details.value,document.forms[0].amount.value];
				var str='<table width=100% >';
				for(var i=0;i<detailsRec.length;i++){		
					str +='<TR>';
					for(var j=0;j<5;j++){	
						if(detailsRec[i][j].length<10){
							str+='<TD align=center   nowrap style=width:20%>'+detailsRec[i][j]+'</TD>';
						}else{
							str+="<TD align=center nowrap style=width:20%>"+detailsRec[i][j].substring(0,10);
							str+="<a class=data href=# onMouseOver = \"overlib('"+detailsRec[i][j]+"\')\" onMouseOut='nd()'>...</a></TD>";				
						}						
					}
					str +='<TD style=width:10%></TD></TR>';					
				}
				document.all['addDetails'].innerHTML = str+"<table>";
				detailsClear();
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
	 	</script>
	</head>
	<body class="BodyBackground1" onload="yearSelect();">
		<form name="vocher" action="<%=basePathBuf%>Voucher?method=addVoucher" onsubmit="javascript : return checkVoucher()" method="post" action="">

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

									Voucher[Add] &nbsp;&nbsp;
								</td>

							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%">
									<table align="center" border="0" width="70%">
										<tr>
											<td class="label" width="100px">
												Bank Name :&nbsp;
											</td>
											<td>
												<input type="text" name="bankName" size="13" maxlength="50" readonly="readonly" />
												<input type="hidden" name="accountNo" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/BankInfo.jsp','AAI');" alt="Click The Icon to Select Bank Master Records" src="" alt="" />
											</td>

											<td class="label">
												Financial Year :&nbsp;
											</td>
											<td>
												<select name='year' style='width:120px'>
													<option value="">
														Select One
													</option>
												</select>
											</td>
										</tr>
										<tr>
											<td class="label">
												Trust Type :&nbsp;
											</td>
											<td>
												<select name='trusttype' style='width:120px'>
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

											<td class="label">
												Type of Voucher:&nbsp;
											</td>
											<td>
												<select name='vouchertype' onchange="checkParty();" style='width:120px'>
													<option value="">
														Select One
													</option>
													<option value='R'>
														Receipt
													</option>
													<option value='P'>
														Payment
													</option>
													<option value='C'>
														Contra
													</option>
												</select>
											</td>
										</tr>
										<tr>
											<td class="label">
												Type of Party:&nbsp;
											</td>
											<td>
												<select name='party' onchange="showParty();" style='width:120px'>
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

										</tr>
										<tr>
											<td style="height: 5">
											</td>
										</tr>
										<tr id="divNomineeHead" style="display:none">
											<td class="label">
												PF ID:
											</td>
											<td>
												<input type="text" size="13" name="epfid" maxlength="50" onblur="dispOff()" width="150px" readonly="readonly" />

												<img src="<%=basePath%>images/search1.gif" onclick="document.forms[0].empStatus.value='N';popupWindow('<%=basePath%>cashbook/EmployeeInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
											</td>
										</tr>
										<tr id="divNomineeHead1" style="display:none">
											<td class="label">
												Employee Name:
											</td>
											<td>
												<input type="text" size="18" name="eName" maxlength="50" onblur="dispOff()" width="150px" readonly="readonly" />
											</td>

											<td class="label">
												Designation:
											</td>
											<td>
												<input type="text" size="18" name="edesignation" maxlength="50" onblur="dispOff()" width="150px" readonly="readonly" />

											</td>
										</tr>
										<tr id="divNominee2" style="display:none">
											<td class="label">
												Party Name:
											</td>
											<td>
												<input type="text" size="13" name="pName" maxlength="50" />
												<img src="<%=basePath%>images/search1.gif" onclick="popupWindow('<%=basePath%>cashbook/PartyInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
											</td>
										</tr>
										<tr id="divNominee" style="display:none">
											<td class="label" nowrap="nowrap">
												Transfer Bank Name:
											</td>
											<td>
												<input type="text" size="13" name="contraBankName" maxlength="50" />
												<input type="hidden" name="contraAccountNo" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif"
													onclick="if(document.forms[0].accountNo.value !=''){popupWindow('<%=basePath%>cashbook/BankInfo.jsp?type=other&amp;&amp;accountNo='+document.forms[0].accountNo.value,'AAI');}else{alert('Please Select the Bank Name');}"
													alt="Click The Icon to Select Bank Master Records" src="" alt="" />
											</td>
										</tr>
										<tr>
											<td class="label">
												Details :

											</td>
											<td>
												<input type="text" size="18" name="voucherDetails" maxlength="50" onblur="dispOff()" />
											</td>
											<td class="label">
												Prepared By:

											</td>
											<td>
												<input type="text" size="18" name="preparedBy" maxlength="50" onblur="dispOff()" />
												<input type="hidden" name="prepepfid" />
												<input type="hidden" name="empStatus" value='N' />
												<img src="<%=basePath%>images/search1.gif" onclick="document.forms[0].empStatus.value='Y';popupWindow('<%=basePath%>cashbook/EmployeeInfo.jsp','AAI');" alt="Click The Icon to Select EmployeeName" src="" alt="" />
												<select name='detailRecords' multiple="multiple" style="DISPLAY:NONE"></select>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="100%">
									<table width="100%" >
										<tr>
											<td class="label" align="center" width="20%">
												Account head:
											</td>
											<td class="label" align="center" width="20%">
												Particular:
											</td>
											<td class="label" align="center" width="20%">
												Month/Year:
												<br />
												<font color="blue" size="1px">(MON/YYYY) </font>
											</td>
											<td class="label" align="center" width="20%">
												Details:
											</td>
											<td class="label" align="center" width="20%">
												Amount:
											</td>
											<td class="label" align="center">

											</td>
										</tr>
										<tr>
											<td colspan="6" id='addDetails' name='addDetails'></td>

										</tr>
										<tr>
											<td nowrap="nowrap" align="center">
												<input type="text" size="10" name="accHead" maxlength="50" onblur="dispOff()" readonly="readonly" />
												<img style='cursor:hand' src="<%=basePath%>images/search1.gif" onclick="popUpAcc();" alt="Click The Icon to Select Bank Account Code Master Records" src="" alt="" />
												<img style='cursor:hand' src="<%=basePath%>images/add.gif" onclick="popupWindow('<%=basePath%>cashbook/AccountingCodePopup.jsp','AAI');" alt="Click The Icon to Select Bank Master Records" src="" alt="" />
											</td>
											<td align="center">
												<input type="text" size="18" name="particular" maxlength="50" onblur="dispOff()" readonly="readonly" />
											</td>
											<td class="label" align="center">
												<input type="text" size="8" name="monthYear" maxlength="8" onblur="dispOff()" />

											</td>
											<td align="center">
												<input type="text" size="14" name="details" maxlength="50" onblur="dispOff()" />
											</td>
											<td align="center">
												<input type="text" size="14" name="amount" maxlength="50" onblur="dispOff()" />
											</td>
											<td nowrap="nowrap">
												<img style='cursor:hand' src="<%=basePath%>images/saveIcon.gif" onclick='saveDetails()' alt="" />
												<img style='cursor:hand' src="<%=basePath%>images/cancelIcon.gif" onclick='detailsClear()' alt="" />
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

