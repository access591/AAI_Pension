<!--
/*
  * File       : AdvanceInformationNext.jsp
  * Date       : 09/26/2009
  * Author     : Suresh Kumar Repaka 
  * Description: 
  * Copyright (2009) by the Navayuga Infotech, all rights reserved.
  */
-->
<%@ page language="java" import="java.util.*,com.aims.bean.advances.AdvanceBasicBean" pageEncoding="UTF-8"%>
<%StringBuffer basePathBuf = new StringBuffer(request.getScheme());
			basePathBuf.append("://").append(request.getServerName()).append(
					":");
			basePathBuf.append(request.getServerPort());
			basePathBuf.append(request.getContextPath()).append("/");

			String basePath = basePathBuf.toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>AAI</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<LINK rel="stylesheet" href="<%=basePath%>view/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="<%=basePath%>view/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>view/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>view/scripts/DateTime.js"></SCRIPT>

		

		<script type="text/javascript">
		function frmPrsnlBack(){
				url="<%=basePath%>loadAdvance.do?method=loadAdvanceFormBack";
	   		    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
		}
		 function saveAdvanceInfo(){
		 
		 		var ex=/^[0-9]+$/;
		 		var lodcheckedlngth='';
		 		var lodhba='',total='',i=0,advanceType='',purposeType='';
		 		advanceType=document.forms[0].advanceType.value;
		 		purposeType=document.forms[0].purposeType.value;
		 		var wthdrwlStatus="";
		 		
		 		if(advanceType=='pfw'){
		 			wthdrwlStatus=document.forms[0].chkwthdrwlinfo.options[document.forms[0].chkwthdrwlinfo.selectedIndex].value;
		 		}

		 		if(advanceType=='pfw' && purposeType=='Marriage'){
		 		
			 		if(document.getElementById("empfmlydtls").value=='NO-SELECT'){
			  			alert('Please Select Marriage Purposes');
			  			document.forms[0].empfmlydtls.focus();
			  			return false;
			  		}			  	
	                if(document.getElementById("empfmlydtls").value=="Son" || document.getElementById("empfmlydtls").value=="Daughter"){   
	                
	                      if(document.forms[0].fmlyage.value==""){
				  			alert('Please Enter Age');
				  			document.forms[0].fmlyage.focus();
				  			return false;
			  			  }
			  			  
			  			  if (!ex.test(document.forms[0].fmlyage.value) && document.forms[0].fmlyage.value!="")
					      {
							 alert("Age shoud be Numeric");
							 document.forms[0].fmlyage.select();
							 return false;
					      }
			  			              
	                      if(document.getElementById("empfmlydtls").value=="Son" && document.forms[0].fmlyage.value<21){
	                       alert('Son Age Should Be Greater Than or Equal To 21');
	                       document.forms[0].fmlyage.focus();
			  			   return false;
	                      }    
	                     
	                      if(document.getElementById("empfmlydtls").value=="Daughter" && document.forms[0].fmlyage.value<17){
	                       alert('Daughter Age Should Be Greater Than or Equal To 18');
	                       document.forms[0].fmlyage.focus();
			  			   return false;
	                      }                      
	                }
                }else if(advanceType=='pfw' && purposeType=='HBA'){
                    if(document.getElementById("hbapurposetype").value=='NO-SELECT'){
			  			alert('Please Select HBA Purposes');
			  			document.forms[0].hbapurposetype.focus();
			  			return false;
			  		}	
			  		if(document.getElementById("hbapurposetype").value!='NO-SELECT'){
			  		     if(document.getElementById("hbapurposetype").value=='repaymenthba'){
			  		         
			  		     }else{
			  		     	 if(document.forms[0].hbaownername.value==""){
			  		          alert("Please Enter Owner Name");
			  		          document.forms[0].hbaownername.focus();
			  				  return false;
			  		         }
			  		         
			  		         if(document.forms[0].hbaownerarea.value==""){
			  		          alert("Please Enter Area");
			  		          document.forms[0].hbaownerarea.focus();
			  				  return false;
			  		         }
			  		     }
			  		     
			  		}
                
                }else if((advanceType=='pfw' && purposeType=='HE') || (advanceType=='CPF' && purposeType=='education')){
                	 if(document.getElementById("pfwhetype").value=='NO-SELECT'){
                	     
                	    if(purposeType=='HE') 
			  			alert('Please Select Higher Education Purposes');
			  			else
			  			alert('Please Select Higher Education');
			  			
			  			document.forms[0].pfwhetype.focus();
			  			return false;
			  		 }	
			  		 
			  		 if(document.getElementById("pfwhetype").value!='NO-SELECT'){
			  		     if(document.forms[0].nmcourse.value==""){
			  		        alert("Please Enter Name Of Course");
			  		        document.forms[0].nmcourse.focus();
			  				return false;
			  		     }			  		 
			  		 }
			  		 
			  		 if(document.getElementById("pfwhetype").value!='NO-SELECT'){
			  		     if(document.forms[0].nminstitue.value==""){
			  		        alert("Please Enter Name of Institution");
			  		        document.forms[0].nminstitue.focus();
			  				return false;
			  		     }			  		 
			  		 }
			  		 
			  		 if(purposeType=='HE'){
				  		 if(document.getElementById("pfwhetype").value!='NO-SELECT'){
				  		     if(document.forms[0].curseduration.value==""){
				  		        alert("Please Enter Duration of course");
				  		        document.forms[0].curseduration.focus();
				  				return false;
				  		     }			  		 
				  		 }
			  		 }
			  		 
			  		 if(purposeType=='HE') {
				  		 if(document.getElementById("pfwhetype").value!='NO-SELECT'){			  		 
				  		     if(document.getElementById("helastexaminfo").value=='NO-SELECT'){
				  		        alert("Please Enter Last examination passed");
				  		        document.forms[0].helastexaminfo.focus();
				  				return false;
				  		     }			  		 
				  		 }
                     }
                
                }else if(advanceType=='CPF' && (purposeType=='cost' || purposeType=='illness')){
                        if(document.getElementById("advancecostexp").value=='NO-SELECT'){
                            if(purposeType=='cost')
				  			alert('Please Select Cost Of Passage');
				  			else
				  			alert('Please Select Illness Expanses');
				  			
				  			document.forms[0].advancecostexp.focus();
				  			return false;
			  		    }	
                }else if(advanceType=='CPF' && purposeType=='obligatory'){
                
                 		if(document.getElementById("advanceobligexp").value=='NO-SELECT'){				  		     
				  		        alert("Please Select Obligatory Expanses");
				  		        document.forms[0].advanceobligexp.focus();
				  				return false;				  		    		  		 
				  		 }
				  		 if(document.getElementById("advanceobligexp").value=='Marriage'){	
				  		       if(document.getElementById("cpfmarriageexp").value=='NO-SELECT'){			  		     
					  		        alert("Please Select Marriage Expanses");
					  		        document.forms[0].cpfmarriageexp.focus();
					  				return false;				  		    		  		 
				  				}
				  		 }    
				  		 if(document.getElementById("advanceobligexp").value=='OtherCeremonies'){	
				  		       if(document.forms[0].oblcermoney.value==""){			  		     
					  		        alert("Please Enter  Other Ceremonies Expanses");
					  		        document.forms[0].oblcermoney.focus();
					  				return false;				  		    		  		 
				  				}
				  		 }              
                
                }                
		  		
		  		if (!ValidateName(document.forms[0].bankempname.value)){
					alert("Numeric/Invalid characters are not allowed");
					document.forms[0].bankempname.focus();
					return false;
		  	   }
		  	   	if (!ValidateName(document.forms[0].bankname.value)){
					alert("Numeric/Invalid characters are not allowed");
					document.forms[0].bankname.focus();
					return false;
		  	   }
		  	   if(advanceType=='pfw'){
		  	  	if(document.forms[0].modeofpartyname.value!='' ){
		  	   	if (!ValidateName(document.forms[0].modeofpartyname.value)){
					alert("Numeric/Invalid characters are not allowed");
					document.forms[0].modeofpartyname.focus();
					return false;
		  	  	 }
		  	   }
		  	   }
			  	if(advanceType=='CPF' && !(purposeType=='defence'  || purposeType=='others' || purposeType=='obligatory') ){
			     if(document.forms[0].fmlyempname.value==""){
				  			
				  			if(purposeType=="education")
				  			alert('Please Enter Name Of Dependent');
				  			else
				  			alert('Please Enter Name');
				  			document.forms[0].fmlyempname.focus();
				  			return false;
			  		 }
		  	  
		  	   	if (!ValidateName(document.forms[0].fmlyempname.value)){
					alert("Numeric/Invalid characters are not allowed");
					document.forms[0].fmlyempname.focus();
					return false;
		  	  	 }
		  	   
		  	   }else if(advanceType=='pfw' && (purposeType=='Marriage') ){			  	   	  	
		  	   	  	 
		  	   	  	 if(document.forms[0].fmlyempname.value==""){
				  			alert('Please Enter Name');
				  			document.forms[0].fmlyempname.focus();
				  			return false;
			  		 }
			  			
		  	      	if (!ValidateName(document.forms[0].fmlyempname.value)){
					alert("Numeric/Invalid characters are not allowed");
					document.forms[0].fmlyempname.focus();
					return false;
		  	  	 }
		  	   }
		 
		  	   
		 		if(advanceType=='pfw' && purposeType=='HBA'){
		 			lodcheckedlngth=document.forms[0].LODHBA.length;
		 			for(i=0;i<lodcheckedlngth;i++){
		 				if(document.forms[0].LODHBA[i].checked==true){
		 					lodhba=document.forms[0].LODHBA[i].value;
		 					if(total==''){
		 						total=lodhba;
		 					}else{
		 						total=total+','+lodhba;
		 					}

		 				}
		 			}
		 		}
		 		var expanse="",flag;
		 		if(advanceType=='CPF' && purposeType!=''){		 		   
		 			if(purposeType=='obligatory'){		 			 
		 				if(document.forms[0].advanceobligexp.value!="undefined"){		 				  
		 					if(document.forms[0].advanceobligexp.value=='OtherCeremonies'){		 					
		 						flag==false;
		 					}else{		 					
		 						flag=true;
		 					}
		 				}
		 			}else if(purposeType=='defence' || purposeType=='others'){
		 			  flag=false;
		 			}else{
		 			  flag=true;
		 			}
		 				
		 			
		 			if(flag==true){
		 			var dt=document.forms[0].fmlydob.value;
		 			var age=document.forms[0].fmlyage.value;
		 		
		 		   if(dt=="") {
   		  			alert("Please Enter Date of Birth");
   		 			document.forms[0].fmlydob.focus();
   		  			return false;
   		  		   }
   		  		   var month="";
		   		  if(!dt==""){
		   		   var date1=document.forms[0].fmlydob;
		   	       var val1=convert_date(date1);
		   		    var now = new Date();
		   		   var birthday1= document.forms[0].fmlydob.value;
		   		   var dt1   = birthday1.substring(0,2);
		   		   var mon1  = birthday1.substring(3,6);
		  		   var year1=birthday1.substring(birthday1.lastIndexOf("/")+1,birthday1.length);
		          if(mon1 == "JAN") month = 0;
           			else if(mon1 == "FEB") month = 1;
		        	else if(mon1 == "MAR") month = 2;
        			else if(mon1 == "APR") month = 3;
		        	else if(mon1 == "MAY") month = 4;
        			else if(mon1 == "JUN") month = 5;
		        	else if(mon1 == "JUL") month = 6;
        		    else if(mon1 == "AUG") month = 7;
        	        else if(mon1 == "SEP") month = 8;
        	        else if(mon1 == "OCT") month = 9;
        	        else if(mon1 == "NOV") month = 10;
        			else if(mon1 == "DEC") month = 11;
        			var birthDate=new Date(year1,month,dt1);   
        			document.forms[0].fmlydob.value=birthday1;
        		   if(birthDate > now){
					    alert("DateofBirth cannot be greater than Currentdate");
						document.forms[0].fmlydob.focus();
					   return false;
				   }
   		    		if(val1==false){
   		    			return false;
   		    		}
   		    		
   		    			if (!convert_date(document.forms[0].fmlydob))
						{
							document.forms[0].fmlydob.focus();
							return false;
						}
		 			}
		 		
   		    			
		 		
   		        }
   		        
			}else if(advanceType=='pfw' && purposeType!='' ){
					if(purposeType=='HBA' || purposeType=='HE'){
						flag=false;
					}else{
						 flag=true;
					}
		 			
		 			if(flag==true){
		 				var dt=document.forms[0].fmlydob.value;
		 		   if(dt=="") {
   		  			alert("Please Enter Date of Birth");
   		 			document.forms[0].fmlydob.focus();
   		  			return false;
   		  		   }
   		  		   var month="";
		   		  if(!dt==""){
		   		   var date1=document.forms[0].fmlydob;
		   	       var val1=convert_date(date1);
		   		    var now = new Date();
		   		   var birthday1= document.forms[0].fmlydob.value;
		   		   var dt1   = birthday1.substring(0,2);
		   		   var mon1  = birthday1.substring(3,6);
		  		   var year1=birthday1.substring(birthday1.lastIndexOf("/")+1,birthday1.length);
		          if(mon1 == "JAN") month = 0;
           			else if(mon1 == "FEB") month = 1;
		        	else if(mon1 == "MAR") month = 2;
        			else if(mon1 == "APR") month = 3;
		        	else if(mon1 == "MAY") month = 4;
        			else if(mon1 == "JUN") month = 5;
		        	else if(mon1 == "JUL") month = 6;
        		    else if(mon1 == "AUG") month = 7;
        	        else if(mon1 == "SEP") month = 8;
        	        else if(mon1 == "OCT") month = 9;
        	        else if(mon1 == "NOV") month = 10;
        			else if(mon1 == "DEC") month = 11;
        			var birthDate=new Date(year1,month,dt1);   
        			document.forms[0].fmlydob.value=document.forms[0].fmlydob.value;
        		   if(birthDate > now){
					    alert("DateofBirth cannot be greater than Currentdate");
						document.forms[0].fmlydob.focus();
					   return false;
				   }
   		    		if(val1==false){
   		    			return false;
   		    		}
   		    		
		 			}
		 			
   		        }
			}
		 	if(advanceType=='CPF' && purposeType=='education'){
		 			lodcheckedlngth=document.forms[0].LODCPFHE.length;		 			
		 			for(i=0;i<lodcheckedlngth;i++){
		 				if(document.forms[0].LODCPFHE[i].checked==true){
		 					lodhba=document.forms[0].LODCPFHE[i].value;
		 					if(total==''){
		 						total=lodhba;
		 					}else{
		 						total=total+','+lodhba;
		 					}

		 				}
		 			}
		 	}
		 	if(wthdrwlStatus=='NO-SELECT'){
		  			alert('Please select Y/N for Similar Withdrawal Details field');
		  			document.forms[0].chkwthdrwlinfo.focus();
		  			return false;
		  		}
		  		if(wthdrwlStatus=='Y'){		  		
		  		    if(document.forms[0].wthdrwlamount.value==""){ 
			  			alert('Please Enter Amount of Withdrawal');
			  			document.forms[0].wthdrwlamount.focus();
			  			return false;
		  			}
		  			
		  			if(document.forms[0].wthdrwlamount!=""){		  		
			  		 	if(!ex.test(document.forms[0].wthdrwlamount.value) && document.forms[0].wthdrwlamount.value!=""){
				  			alert('Withdrawal Amount Should Be Numeric');
				  			document.forms[0].wthdrwlamount.focus();
				  			return false;
			  			}
		  		    }
		  		
		  			if(document.forms[0].wthdrwltrnsdt.value==""){ 
			  			alert('Please Enter Date of Withdrawal');
			  			document.forms[0].wthdrwltrnsdt.focus();
			  			return false;
		  			}
		  		}
		  		
		 		
		 	if((advanceType=='pfw' || advanceType=='CPF') && purposeType!=''){
		 	      if(document.forms[0].bankempname.value==""){
				  		alert('Please Enter Name(Appearing in the saving bank A/c)');
				  		document.forms[0].bankempname.focus();
				  		return false;
			  	  }
			  	  
			  	  if(document.forms[0].bankname.value==""){
				  		alert('Please Enter Bank name');
				  		document.forms[0].bankname.focus();
				  		return false;
			  	  }
			  	  if(document.forms[0].banksavingaccno.value==""){
				  		alert('Please Enter Saving bank A/c no');
				  		document.forms[0].banksavingaccno.focus();
				  		return false;
			  	  }
			  	  if(document.forms[0].bankemprtgsneftcode.value==""){
				  		alert('Please Enter RTGS/NEFT Code');
				  		document.forms[0].bankemprtgsneftcode.focus();
				  		return false;
			  	  }
			  	  if(document.forms[0].bankempmicrono.value==""){
				  		alert('Please Enter MICR No');
				  		document.forms[0].bankempmicrono.focus();
				  		return false;
			  	  }	 	
		 	      
		 	}		 	
		 	url="<%=basePath%>loadAdvance.do?method=saveAdvacneNextInfo&lodinfo="+total;
	   		document.forms[0].action=url;
			document.forms[0].method="post";
			document.forms[0].submit();
		 
		 }
		function chkWthDrwl(){
					var wthdrwlStatus="";
					wthdrwlStatus=document.forms[0].chkwthdrwlinfo.options[document.forms[0].chkwthdrwlinfo.selectedIndex].value;
		  			if(wthdrwlStatus=='Y'){   		    
		   		   		document.getElementById("chk_wthdrwn_dtl").style.display="block";
		   		    }else if(wthdrwlStatus=='N'){   	
						document.getElementById("chk_wthdrwn_dtl").style.display="none";
		   		    }
		}
		function loadNext(purposeType,advanceType){
		   
			if(purposeType=='HBA'){
				document.getElementById("repaymentHbaLoan").style.display="none";
				document.getElementById("repaymentHbaLoanInfo").style.display="none";
				document.getElementById("repaymentHbaLoanInfo2").style.display="none";
				document.getElementById("hbaAnyOthers").style.display="none";
				document.forms[0].hbapurposetype.focus();
				
			}else if(purposeType=='Marriage'){
				document.getElementById("repaymentHbaLoan").style.display="none";
				document.getElementById("repaymentHbaLoanInfo").style.display="none";
				document.getElementById("repaymentHbaLoanInfo2").style.display="none";
				document.getElementById("hbaAnyOthers").style.display="none";
					document.forms[0].empfmlydtls.focus();
			}else if(purposeType=='HE'){
			    //document.getElementById("repaymentHbaLoan").style.display="none";
				//document.getElementById("repaymentHbaLoanInfo").style.display="none";
				//document.getElementById("repaymentHbaLoanInfo2").style.display="none";
				//document.getElementById("hbaAnyOthers").style.display="none";
				
					document.forms[0].pfwhetype.focus();
			}else if(purposeType=='cost'){					
					document.forms[0].advancecostexp.focus();
			}else if(purposeType=='obligatory'){			
					document.forms[0].advanceobligexp.focus();
					document.getElementById("marriageexpan").style.display="none";
					document.getElementById("ceremoniesexpanse").style.display="none";
			}else if(purposeType=='illness'){
					document.forms[0].advancecostexp.focus();
			}else if(purposeType=='education'){			
					document.forms[0].pfwhetype.focus();
			}	
				
		}
	function chkhbapurposedtl(){
			var hbaOptions="";
			hbaOptions=document.forms[0].hbapurposetype.options[document.forms[0].hbapurposetype.selectedIndex].value;
			if(hbaOptions=='repaymenthba'){
				document.getElementById("repaymentHbaLoan").style.display="block";
				document.getElementById("repaymentHbaLoanInfo").style.display="block";
				document.getElementById("repaymentHbaLoanInfo2").style.display="block";
				document.getElementById("hbaAnyOthers").style.display="none";
			}else if(hbaOptions=='hbaothers'){
				document.getElementById("repaymentHbaLoan").style.display="none";
				document.getElementById("repaymentHbaLoanInfo").style.display="none";
				document.getElementById("repaymentHbaLoanInfo2").style.display="none";
				document.getElementById("hbaAnyOthers").style.display="block";
			}else{
				document.getElementById("repaymentHbaLoan").style.display="none";
				document.getElementById("hbaAnyOthers").style.display="none";
				document.getElementById("repaymentHbaLoanInfo").style.display="none";
				document.getElementById("repaymentHbaLoanInfo2").style.display="none";
			}
	}

	function chkobExpanse(){	
			var oblOptions="";
			oblOptions=document.forms[0].advanceobligexp.options[document.forms[0].advanceobligexp.selectedIndex].value;
			if(oblOptions=="Marriage"){   		    
   		  		document.getElementById("marriageexpan").style.display="block";
				document.getElementById("ceremoniesexpanse").style.display="none";
   		    }else if(oblOptions=="OtherCeremonies"){   		    
   		 		document.getElementById("marriageexpan").style.display="none";
				document.getElementById("ceremoniesexpanse").style.display="block";
   		    }
   }			  		   	
 </script>
	</head>
<%
	AdvanceBasicBean basicBean=new AdvanceBasicBean();
	if(request.getAttribute("advanceBean")!=null){
		basicBean=(AdvanceBasicBean)request.getAttribute("advanceBean");
		request.setAttribute("advanceBasicBean",basicBean);
	}
%>

	<body class="BodyBackground" onload="loadNext('<bean:write property="purposeType" name="advanceBean"/>','<bean:write property="advanceType" name="advanceBean"/>');">
		<html:form  method="post" action="loadAdvance.do?method=continueWithPTWAdvanceOptions">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
					<td>
						<jsp:include page="PensionMenu.jsp"  />
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
								<td height="5%" colspan="4" align="center" class="ScreenMasterHeading">
                                  										
									<logic:equal property="advanceType" value="pfw" name="advanceForm" >								
									PFW (Part Final Withdraw) Form &nbsp;&nbsp;<font color="red"> </font>
									</logic:equal>
									<logic:notEqual property="advanceType" value="pfw" name="advanceForm" >								
									CPF Advance Form &nbsp;&nbsp;<font color="red"> </font>
									</logic:notEqual>
									
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							
							
							
							<tr>
								<td height="15%" colspan="4">
								<table cellpadding="1" cellspacing="1"  border="0" align="center" width="100%">
								
								<html:hidden property="pensionNo"/>
								<html:hidden property="advReqAmnt"/>
								<html:hidden property="emoluments"/>
								<html:hidden property="purposeType"/>
								<html:hidden property="advanceType"/>
								<html:hidden property="advReasonText"/>
								<html:hidden property="cpftotalinstall"/>
								<bean:define id="selPurposeType" name="advanceBean" property="purposeType" type="String"/>
								<logic:equal property="pfwPurHBA" value="<%=selPurposeType%>"  name="advanceForm">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1"   border="0" align="center"  width="100%">
									
										<tr>
											<td class="screenlabel" >
												HBA Purposes:
											</td>
											<td>
												<html:select  property="hbapurposetype" style="width:150px" onchange="chkhbapurposedtl();">  
														<html:option value="NO-SELECT">Select One</html:option>
														<html:option value='purchasesite'>Purchasing of dwelling site</html:option>
														<html:option value='purchasehouse'>Purchasing of dwelling house</html:option>
														<html:option value='constructionhouse'>Construction of dwelling house</html:option>
														<html:option value='acquireflat'>Acquiring a Flat</html:option>
														<html:option value='renovationhouse'>Renovation/Alteration/Addition of dwelling house</html:option>
														<html:option value='repaymenthba'>Repayment of HBA Loan taken from</html:option>
														<html:option value='hbaothers'>Any Others</html:option>
												</html:select>
												<!-- <select name='select_hba_purpose_dtl' style='width:130px' onchange="chkhbapurposedtl();">
													<option value="NO-SELECT">
														Select One
													</option>
													<option value='purchasesite'>
														Purchasing of dwelling site
													</option>
													<option value='purchasehouse'>
														Purchasing of dwelling house
													</option>
													<option value='constructionhouse'>
														Construction of dwelling house
													</option>
													<option value='acquireflat'>
														Acquiring a Flat
													</option>
													<option value='renovationhouse'>
														Renovation/Alteration/Addition of dwelling house
													</option>
													<option value='repaymenthba'>
														Repayment of HBA Loan taken from
													</option>
													<option value='hbaothers'>
														Any Others
													</option>
												</select>-->
											</td>
										
											
										</tr>
										<tr id="repaymentHbaLoan">
											<td class="screenlabel" >
												Repayment of HBA loan :
											</td>
											<td >
											<html:select  property="hbarepaymenttype" style="width:150px" onchange="chkhbapurposedtl();">  
													<html:option value="NO-SELECT"> Select One </html:option> 
													<html:option value='LOANFRMGVRMNT'> Loan from State government/Central Govt. </html:option>
													<html:option value='RGDCOPERATIVESOCIETY'> Registered Cooperative society </html:option> 
													<html:option value='SHB'> State Housing Board </html:option>
													<html:option value='NB'> Nationalized Bank </html:option> 
													<html:option value='PFI'> Public Financial Institution </html:option> 
													<html:option value='MC'> Municipal corporation </html:option>
											</html:select>
											
												<!-- <select name='repayment' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value='loan1'>
														Loan from State government/Central Govt.
													</option>
													<option value='regi'>
														Registered Cooperative society
													</option>
													<option value='houseing'>
														State Housing Board
													</option>
													<option value='nation'>
														Nationalized Bank
													</option>
													<option value='Pfi'>
														Public Financial Institution
													</option>
													<option value='muncipal'>
														Municipal corporation
													</option>
												</select>-->
											</td>
										</tr>
										<tr id="hbaAnyOthers">
											
											<td class="screenlabel">Reason:</td>
											<td>
												<html:text property="advReasonText"/>
											</td>
											<td colspan="2">&nbsp;</td>
										</tr>
										<tr id="repaymentHbaLoanInfo">
											<td class="screenlabel">
												Name:
											</td>
											<td>
												<html:text property="hbaloanname"/>
											
											</td>

											<td class="screenlabel">
												Address:
											</td>
											<td>
												<html:text property="hbaloanaddress"/>
												
											</td>
										</tr>
										<tr id="repaymentHbaLoanInfo2">
											<td class="screenlabel">
												Outstanding amount of Name<br/>loan with interest:
											</td>
											<td>
												<html:text property="osamountwithinterest"/>
												
											</td>

										
										</tr>
										<tr>
											<td class="screenlabel">Owner Name:</td>
											<td>
												<html:text property="hbaownername"/>
												
											</td>
											<td class="screenlabel">
												Owner address :
											</td>
											<td>
												<html:text property="hbaowneraddress"/>
												
											</td>

											
										</tr>
										<tr>
											<td class="screenlabel">
												Area:
											</td>
											<td>
												<html:text property="hbaownerarea"/>
												
											</td>
											<td class="screenlabel">
												Plot/House/Flat No :
											</td>
											<td>
												<html:text property="hbaownerplotno"/>
												
											</td>

										
										</tr>
										<tr>
											<td class="screenlabel">
												Locality:
											</td>
											<td>
												<html:text property="hbaownerlocality"/>
												
											</td>
											<td class="screenlabel">
												Name of Municipality :
											</td>
											<td>
												<html:text property="hbaownermuncipality"/>
												
											</td>

										
										</tr>
										<tr>
											<td class="screenlabel">
												City:
											</td>
											<td>
												<html:text property="hbaownercity"/>
												
											</td>
											<td colspan="2"></td>
										</tr>
										<tr>
											<td class="screenlabel">
												Whether HBA is drawn 											
												from AAI:
											</td>
											<td>
												<html:select  property="hbadrwnfrmaai" style="width:130px">  
														<html:option value='Y'>Yes</html:option>
														<html:option value='N'>No</html:option>
												</html:select>
												
											</td>
											<td class="screenlabel">
												Whether permission from AAI has <br/>been obtained:
											</td>
											<td>
												<html:select  property="hbapermissionaai" style="width:130px">  
														<html:option value='Y'>Yes</html:option>
														<html:option value='N'>No</html:option>
												</html:select>
												<!-- <select name='select_permission_aai' Style='width:130px'>
												
													<option value='Y' selected="selected">
														Yes
													</option>
													<option value='N'>
														No
													</option>
												</select>-->
											</td>
										</tr>
								
										
										
									</table>
									</td>
								</tr>
							
								</logic:equal>
								<!--Marriage Purpose  -->
								<logic:equal property="pfwPurMarriage" value="<%=selPurposeType%>"  name="advanceForm">
								<tr>
									<td colspan="4">
										<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%"> 
										
										<tr>
											<td class="screenlabel">
												Marriage Purposes:
											</td>
											<td>
													<html:select  property="empfmlydtls" style="width:130px">
														<html:option value='NO-SELECT'>Select One</html:option>  
														<html:option value='self'>Self</html:option>
														<html:option value='Son'>Son</html:option>
														<html:option value='Daughter'>Daughter</html:option>
														<html:option value='Dependent Brother'>Dependent Brother</html:option>
														<html:option value='Dependent Sister'>Dependent Sister</html:option>
													</html:select>
											<!-- 	<select name='select_marry_prs_dtl' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value='self'>
														Self
													</option>
													<option value='son'>
														Son
													</option>
													<option value='daughter'>
														Daughter
													</option>
													<option value='brother'>
														Dependent Brother
													</option>
													<option value='sister'>
														Dependent Sister
													</option>
												</select>-->
											</td>

											<td class="screenlabel">Name:</td>
											<td>
												<html:text property="fmlyempname"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">Date of Birth:</td>
											<td>
												<html:text property="fmlydob"/>&nbsp;&nbsp;&nbsp;<a href="javascript:show_calendar('forms[0].fmlydob');"><img src="<%=basePath%>view/images/calendar.gif" border="no" /></a>
											</td>

											<td class="screenlabel">Age:</td>
											<td>
												<html:text property="fmlyage"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">
												Birth certificate / age<br/>proved:
											</td>
											<td >
												<input type="checkbox" name="brthcertprove" value="Y">
											</td>
											<td colspan="2">&nbsp;&nbsp;&nbsp;</td>
										</tr>

									
										
										</table>
									</td>
								</tr>
								</logic:equal>
								<!--Higher Education Purpose  -->
								<logic:equal property="pfwPurHE" value="<%=selPurposeType%>" name="advanceForm" >
								<tr>
									<td colspan="4">
											<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%"> 
										<tr>
											<td class="screenlabel">
												Higher Education<br/>Purposes:
											</td>
											<td>
											<html:select  property="pfwhetype" style="width:130px">  
													<html:option value="NO-SELECT"> Select One </html:option> 
													<html:option value='Son'> Son </html:option>
													<html:option value='Daughter'> Daughter </html:option> 
												
											</html:select>
												<!-- <select name='select_he_prs_dtl' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value='sons'>
														Son
													</option>
													<option value='daughters'>
														Daughter
													</option>
												</select>-->
											</td>
											<td class="screenlabel">
												Name of Course:
											</td>
											<td>
												<html:text property="nmcourse"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">
												Name of Institution:
											</td>
											<td>
												<html:text property="nminstitue"/>
												
											</td>

											<td class="screenlabel">
												Address of Institution:
											</td>
											<td>
												<html:text property="addrsinstitue"/>
												
											</td>
										</tr>
										<tr>
											<td class="screenlabel">
												Duration of course:
											</td>
											<td>
												<html:text property="curseduration"/>
												
											</td>

											<td class="screenlabel">
												Last examination passed:
											</td>
											<td>
											<html:select  property="helastexaminfo" style="width:130px">  
													<html:option value="NO-SELECT"> Select One </html:option> 
													<html:option value='Higher secondary'> Higher secondary (10 th) </html:option>
													<html:option value='Senior secondary'> Senior secondary (10+2) </html:option> 
													<html:option value='Graduation'> Graduation </html:option> 
													<html:option value='Post Graduation'> Post Graduation </html:option> 	
											</html:select>
												<!-- <select name='lastexam' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value='higher'>
														Higher secondary (10 th)
													</option>
													<option value='senior'>
														Senior secondary (10+2)
													</option>
													<option value='grad'>
														Graduation
													</option>
													<option value='pg'>
														Post Graduation
													</option>
												</select>-->
											</td>
										</tr>

									</table>

										
									</td>
								</tr>
							</logic:equal>
							<logic:equal property="cpfAdvCOP" value="<%=selPurposeType%>" name="advanceForm" >
								<tr>
									<td colspan="4">
									<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%"> 
										<tr>
											<td class="screenlabel">
												Cost of Passage:
											</td>
											<td>
												<html:select  property="advancecostexp" style="width:130px">  
													<html:option value="NO-SELECT">Select One</html:option>
													<html:option value="Self">Self</html:option>
													<html:option value="spouse">Spouse</html:option>
													<html:option value="Son">Son</html:option>
													<html:option value="Daughter">Daughter</html:option>
												 </html:select>
												<!-- <select name='select_cop_prse_dtl' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value="selfs">
														Self
													</option>
													<option value="spouse">
														Spouse
													</option>
													<option value="son1">
														Son
													</option>
													<option value="daughter1">
														Daughter
													</option>
												</select>-->
											</td>
											<td class="screenlabel">Name:</td>
											<td>
												<html:text property="fmlyempname"/>
											</td>

										</tr>
										<tr>
											<td class="screenlabel">Date of Birth:</td>
											<td>
												<html:text property="fmlydob"/>&nbsp;&nbsp;&nbsp;<a href="javascript:show_calendar('forms[0].fmlydob');"><img src="<%=basePath%>view/images/calendar.gif" border="no" /></a>
											</td>

											<td class="screenlabel">Age:</td>
											<td>
												<html:text property="fmlyage"/>
											</td>
										</tr>
									</table>
									
									</td>
								</tr>
							</logic:equal>

								<logic:equal property="cpfAdvOE" value="<%=selPurposeType%>" name="advanceForm" >
								<tr>
									<td colspan="4">
									<table  align="center" border="0" cellpadding="1" cellspacing="1" width="100%"> 
									
												<tr>
													<td class="screenlabel" width="27%">Obligatory Expanses:</td>
													<td width="32%">
														<html:select  property="advanceobligexp" style="width:130px" onchange="chkobExpanse();">  
															<html:option value="NO-SELECT">Select One</html:option>
															<html:option value="Marriage">Marriage</html:option>
															<html:option value="OtherCeremonies">Other Ceremonies</html:option>
														</html:select>
														<!-- <select name='select_obl_prse_dtl' onchange="chkobExpanse();" Style='width:130px'>
															<option value="NO-SELECT">
																Select One
															</option>
															<option value="obmarriage">
																Marriage
															</option>
															<option value="obcermony">
																Other Ceremonies
															</option>
														</select>-->
													</td>
													<td colspan="2">&nbsp;&nbsp;</td>
												</tr>
									
										
										<tr id="marriageexpan">
											<td colspan="4">
											<table  align="center" border="0" cellpadding="1" cellspacing="1" width="100%"> 
												<tr>
											<td class="screenlabel" width="27%">
												Marriage Expanses:
											</td>
											<td>
													<html:select  property="cpfmarriageexp" style="width:130px" onchange="chkobExpanse();">  
															<html:option value="NO-SELECT">Select One</html:option>
															<html:option value="Self">Self Marriage</html:option>
															<html:option value="Son">Dependent Son</html:option>
															<html:option value="Daughter">Dependent Daughter</html:option>
															<html:option value="Sister">Dependent Sister</html:option>
													</html:select>
												<!-- <select name='select_obl_marrage_dtl' Style='width:130px'>
													<option value="NO-SELECT">
														Select One
													</option>
													<option value="selfmarrage">
														Self Marriage
													</option>
													<option value="depson">
														Dependent Son
													</option>
													<option value="depdaugh">
														Dependent Daughter
													</option>
													<option value="depsis">
														
													</option>
												</select>-->
											</td>
											<td class="screenlabel">
												Name:
											</td>
											<td>
												<html:text property="fmlyempname"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">
												Date of Birth:
											</td>
											<td>
												<html:text property="fmlydob"/>&nbsp;&nbsp;&nbsp;<a href="javascript:show_calendar('forms[0].fmlydob');"><img src="<%=basePath%>view/images/calendar.gif" border="no" /></a>
											</td>

											<td class="screenlabel">
												Age:
											</td>
											<td>
												<html:text property="fmlyage"/>
											</td>
										</tr>
											</table>	
											</td>
										</tr>
										
									</table>
									</td>
									</tr>
									<tr id="ceremoniesexpanse">
											<td class="screenlabel" width="27%">Other Ceremonies<br/>Expanses:</td>
											<td width="32%">
													<html:text property="oblcermoney"/>
											</td>
											<td colspan="2">&nbsp;&nbsp;</td>
									
									</tr>
									</logic:equal>	
							
							
								<logic:equal property="cpfAdvIE" value="<%=selPurposeType%>" name="advanceForm" >
								<tr>
									<td colspan="4">
									<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
										<tr>
											<td class="screenlabel">
												Illness Expanses:
											</td>
											<td>
												<html:select  property="advancecostexp" style="width:130px">  
													<html:option value="NO-SELECT">Select One</html:option>
													<html:option value="Self">Self</html:option>
													<html:option value="spouse">Spouse</html:option>
													<html:option value="Son">Son</html:option>
													<html:option value="Daughter">Daughter</html:option>
												 </html:select>
					
											</td>
											<td class="screenlabel">
												Name:
											</td>
											<td>
												<html:text property="fmlyempname"/>
											</td>

										</tr>
										<tr>
											<td class="screenlabel">
												Date of Birth:
											</td>
											<td>
												<html:text property="fmlydob"/>&nbsp;&nbsp;&nbsp;<a href="javascript:show_calendar('forms[0].fmlydob');"><img src="<%=basePath%>view/images/calendar.gif" border="no" /></a>
											</td>

											<td class="screenlabel">
												Age :
											</td>
											<td>
												<html:text property="fmlyage"/>
											</td>
										</tr> 
									</table>
									</td>
								</tr>
								</logic:equal>	
							
								<logic:equal property="cpfAdvEducation" value="<%=selPurposeType%>" name="advanceForm" >
								<tr>
									<td colspan="4">
									<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
										<tr>
											<td class="screenlabel">
												Higher Education:
											</td>
											<td>
											<html:select  property="pfwhetype" style="width:130px">  
													<html:option value="NO-SELECT"> Select One </html:option> 
													<html:option value='Son'> Son </html:option>
													<html:option value='Daughter'> Daughter </html:option> 
												
											</html:select>
											</td>
											<td class="screenlabel">Name of Dependent:</td>
											<td>
												<html:text property="fmlyempname"/>
											</td>

										</tr>
										<tr>
											<td class="screenlabel">DateOfBirth:</td>
											<td>
												<html:text property="fmlydob"/>&nbsp;&nbsp;&nbsp;<a href="javascript:show_calendar('forms[0].fmlydob');"><img src="<%=basePath%>view/images/calendar.gif" border="no" /></a>
											</td>

											<td class="screenlabel">
												Age:
											</td>
											<td>
												<html:text property="fmlyage"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">Name of Course:</td>
											<td>
												<html:text property="nmcourse"/>
											</td>

											<td class="screenlabel">
												Name of Institution:
											</td>
											<td>
												<html:text property="nminstitue"/>
											</td>
										</tr>
										<tr>
											<td class="screenlabel">
												Address of Institution:
											</td>
											<td>
												<html:text property="addrsinstitue"/>
											</td>

											<td class="screenlabel">
												Recognized:
											</td>
											<td>
													<html:select  property="herecog" style="width:130px">  
														<html:option value='Y'> Yes </html:option>
														<html:option value='N'> No</html:option> 
													</html:select>
											</td>
										</tr>
										<tr>
											<td colspan="4">
											<table align="left" cellpadding="0" cellspacing="0" > 
													<tr>
													<td class="ScreenSubHeading" align="align">List of Documents to be enclosed in Higher education</td>
												
												</tr>
											</table>
											</td>
										</tr>
										
									</table>
									</td>
								</tr>
								</logic:equal>
								<bean:define id="selAdvanceType" name="advanceBean" property="advanceType" type="String"/>
								<logic:equal property="advanceType" value="<%=selAdvanceType%>" name="advanceForm" >
									<tr>
										<td colspan="4">
											<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
												<tr>
													<td class="screenlabel" width="28%">Give Previous Similar Withdrawal Details:</td>
													
													<td>
														<html:select  property="chkwthdrwlinfo" style="width:130px" onchange="chkWthDrwl();">  
															<html:option value="NO-SELECT">Select One</html:option>
															<html:option value='Y'>Yes</html:option>
															<html:option value='N'>No</html:option>
														</html:select>
														<!-- <select name='select_wthdrwn_dtl' onchange="chkWthDrwl();" style='width:130px'>
															<option value="NO-SELECT">Select One</option>
															<option value='Y'>Yes</option>
															<option value='N'>No</option>
															
														</select>-->
													</td>
												</tr>
												<tr id="chk_wthdrwn_dtl">
													<td colspan="4">
														<table  align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
															<tr>
																<td class="ScreenSubHeading" align="left">Give Previous withdrawal Details</td>
															</tr>
															<tr>
																<td class="screenlabel">Amount of withdrawal:</td>
																<td><html:text property="wthdrwlamount"/></td>
																<td class="screenlabel">Date of Withdrawal:</td>
																<td><html:text property="wthdrwltrnsdt"/><font class="screenlabel">(DD-MMM-YYYY)</font></td>
															</tr>
																
														</table>
													</td>
												</tr>
												</table>
											</td>
										</tr>
								</logic:equal>
								<tr>
										<td colspan="4">
											<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
										
												<tr>
													<td colspan="4">
														<table align="left" cellpadding="1" cellspacing="1"  > 
															<tr>
																<td class="ScreenSubHeading" align="left">Bank Detail For Payment</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td colspan="4">
														<table  align="center" border="0" cellpadding="1" cellspacing="1" width="100%">
															<html:hidden property="chkbankinfo"/>
															<tr>
																<td class="screenlabel" width="28%">Name(Appearing in the saving bank A/c):</td>
																<td><html:text property="bankempname"/></td>
																<td class="screenlabel" >Address of Employee:</td>
																<td><html:text property="bankempaddrs"/></td>
															</tr>
															<tr>
																<td class="screenlabel">Bank name:</td>
																<td><html:text property="bankname"/></td>
																<td class="screenlabel">Branch address:</td>
																<td><html:text property="branchaddress"/></td>
															</tr>
															<tr>
																<td class="screenlabel">Saving bank A/c no:</td>
																<td><html:text property="banksavingaccno"/></td>
																<td class="screenlabel">RTGS/NEFT Code:</td>
																<td><html:text property="bankemprtgsneftcode"/></td>
															</tr>		
															<tr>
																<td class="screenlabel">Employee Mail ID:</td>
																<td><html:text property="empmailid"/></td>
																<td class="screenlabel">MICR No :</td>
																<td><html:text property="bankempmicrono"/></td>
															</tr>	
														</table>
													</td>
												</tr>
											</table>
											</td>
										</tr>
										<bean:define id="selAdvanceType1" name="advanceBean" property="advanceType" type="String"/>
										<logic:equal property="advanceType" value="<%=selAdvanceType1%>" name="advanceForm" >
										<tr>
											<td colspan="4">
											<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
							
												<tr>
													<td colspan="4">
														<table align="left" cellpadding="1" cellspacing="1"  > 
															<tr>
																<td class="ScreenSubHeading" align="left">Mode Of Party </td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td colspan="4">
														<table  align="center" border="0" cellpadding="1" cellspacing="1"  width="100%">
														
															<tr>
																<td class="screenlabel">Party Name:</td>
																<td><html:text property="modeofpartyname"/></td>
																<td class="screenlabel">Party Address:</td>
																<td><html:text property="modeofpartyaddrs"/></td>
															</tr>
														
														</table>
													</td>
												</tr>
											</table>
											</td>
										</tr>
								</logic:equal>
								<logic:equal property="pfwPurHBA" value="<%=selPurposeType%>"  name="advanceForm">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1"   border="0" align="center"  width="100%">
											<tr>
											<td colspan="4">
											<table align="left" cellpadding="0" cellspacing="0" > 
												<tr>
													<td class="ScreenSubHeading" align="left">List of Documents to be enclosed in HBA</td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td colspan="4">
											<table align="center" cellpadding="1" cellspacing="1"  > 
												<tr>
													<td class="label">1.Title Deed of proposed seller</td>
													<td><input type="checkbox" name="LODHBA" value="TDPS"></td>
												</tr>
												<tr>
													<td class="label">2.Non-encumbrance certificate in respect of the dwelling <br/>site/house to be purchased
													</td>
													<td><input type="checkbox" name="LODHBA" value="NEDSHP"></td>
												</tr>
												<tr>
													<td class="label">3.Agreement with the vendor for the purchase of site/house</td>
													<td><input type="checkbox" name="LODHBA" value="AVPSH"></td>
												</tr>
												<tr>
													<td class="label">4.Estimate of the cost of construction in the case of advance <br/>for the construction of the house.</td>
													<td><input type="checkbox" name="LODHBA" value="ECACH"></td>
												</tr>
												<tr>
													<td class="label">5.Sanctioned construction plan</td>
													<td><input type="checkbox" name="LODHBA" value="SCP"></td>
												</tr>
												<tr>
													<td class="label">6.Any others</td>
													<td><input type="checkbox" name="LODHBA" value="AO"></td>
												</tr>
											  </table>
											</td>
										</tr>
										</table>
									</td>
								</tr>
								</logic:equal>
								<logic:equal property="cpfAdvEducation" value="<%=selPurposeType%>"  name="advanceForm">
								<tr>
									<td>
										<table cellpadding="1" cellspacing="1"   border="0" align="center"  width="100%">
											<tr>
											<td colspan="4">
											<table align="left" cellpadding="0" cellspacing="0" > 
												<tr>
													<td class="ScreenSubHeading" align="left">List of Documents to be enclosed in Higher Education</td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td colspan="4">
											<table align="center" cellpadding="1" cellspacing="1"  > 
												<tr>
													<td class="label">1.Prospectus</td>
													<td><input type="checkbox" name="LODCPFHE" value="PROS"></td>
												</tr>
												<tr>
													<td class="label">2.Certificate from the institution for bonafied students. <br/>site/house to be purchased
													</td>
													<td><input type="checkbox" name="LODCPFHE" value="CIFBS"></td>
												</tr>
												<tr>
													<td class="label">3.Fee structure</td>
													<td><input type="checkbox" name="LODCPFHE" value="FS"></td>
												</tr>
												<tr>
													<td class="label">4.Copy of certificate of last examination passed.</td>
													<td><input type="checkbox" name="LODCPFHE" value="CCLEP"></td>
												</tr>
											
											  </table>
											</td>
										</tr>
										</table>
									</td>
								</tr>
								</logic:equal>
								<tr>
									<td colspan="4">
										<table align="center" cellpadding="1" cellspacing="1"  > 
											<tr>
												
												<td align="center">
	
													<input type="button" class="btn" value="Submit" onclick="javascript:saveAdvanceInfo();">
													<input type="button" class="btn" value="Reset" onclick="javascript:document.forms[0].reset()" class="btn">
													<input type="button" class="btn" value="Back" onclick="javascript:frmPrsnlBack();" class="btn">
												</td>
											</tr>
										</table>
									</td>
								</tr>

						</table>
		</html:form>
	</body>
</html>

