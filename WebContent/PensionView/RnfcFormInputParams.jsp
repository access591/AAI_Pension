<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
                    String forms[] = {"FORM-3","FORM-3-PS","FORM-3-ALL","FORM-6PS","PF CARD","FORM-6-ALL","FORM-8-PS"};
	String[] year = {"1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009"};				
            %>

<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.Constants" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>

<%@ page import="aims.bean.FinancialYearBean"%>
<%@ page import="aims.service.FinancialService"%>
<%@ page import="java.util.ArrayList"%>

<html>
	<HEAD>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">

		<script type="text/javascript">
		
		function divYears(){
	
		if(document.forms[0].formType.value=="FORM-3")
		{
		alert("if");
		 document.getElementById("years").style.display = "none"; 
		// document.getElementById("years").innerText = ""; 
		 //document.getElementById("years").innerText = "";
		}
		else{alert("else");
		 document.getElementById("years").style.display = "block"; 
		}
		
		
		}

 function LoadWindow(params){
	var newParams ="<%=basePath%>PensionView/Report.jsp?"+params
	winHandle = window.open(newParams,"Utility","menubar=yes,toolbar = yes,scrollbars=yes,resizable=yes");
	winOpened = true;
	winHandle.window.focus();
   }

	
	function resetReportParams(){
		document.forms[0].action="<%=basePath%>reportservlet?method=loadForm6Cmp";
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	
		
	
	function validateForm(user) {
		var reportType="",url="";
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		
        if(user!='SEHGAL' && user!='navayuga'&&user!='BALWANT'){
          alert("' "+user+" ' User Not allowed to Access the Reports");
          return false;
         }
         if(document.forms[0].select_year.selectedIndex<1  && document.forms[0].formType.value!='FORM-8' &&  document.forms[0].formType.value!='FORM-8-PS')
   		 {
   		  alert("Please Select Year");
   		  document.forms[0].select_year.focus();
   		  return false;
   		  } 
        
         var index=document.forms[0].select_region.selectedIndex;
       if(document.forms[0].select_region.selectedIndex<1 && document.forms[0].formType.value!='FORM-3-ALL' && document.forms[0].formType.value!='FORM-3' && document.forms[0].formType.value!='Duplicate FORM-3' && document.forms[0].formType.value!='FORM-6'&& document.forms[0].formType.value=='FORM-8')
   		 {
   		  alert("Please Select Region");
   		  document.forms[0].select_region.focus();
   		  return false;
   		  } 
    
          
		if(document.forms[0].formType.value==''){
			alert('Please Select Form Type (Mandatory)');
			document.forms[0].formType.select();
			return false;
		 }
		 
		 
		 else{
		//alert(document.forms[0].select_region.selectedIndex);
	    //  if(document.forms[0].select_region.selectedIndex==0 ||document.forms[0].select_region.selectedIndex==1 || document.forms[0].select_region.selectedIndex==3){
   		//  alert("Developement Activities are in Progress");
   		//  return false;
   		//  }
		//  alert("form tye"+document.forms[0].formType.value);
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
			//alert(document.forms[0].formType.value);
			//alert("sortingOrder  "+sortingOrder);
			var params = "?region="+regionID+"&month="+monthID+"&year="+yearID+"&frm_reportType="+reportType+"&airportCD="+airportcode;
		 //  alert("params "+params);
			
			if(document.forms[0].formType.value=='FORM-3' || document.forms[0].formType.value=='FORM-3-ALL' || document.forms[0].formType.value=='FORM-3-PS'){ 
			 var frm_empflag,empName;
			  if (document.forms[0].chk_empflag.checked==true){
			  			empName=document.forms[0].txt_empname.value;
        				frm_empflag=true;
       		  }else{
	       		  frm_empflag=false;
       		  }
       		if(document.forms[0].formType.value=='FORM-3-ALL'){
       		
       		sortingOrder="MASTER_EMPNAME";
       		url = "<%=basePath%>reportservlet?method=allRegionForm3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_formType="+formtype;
       		}else{
       			var formtype=document.forms[0].formType.value;
       			url = "<%=basePath%>reportservlet?method=getform3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_formType="+formtype;
       		}
       
       
       		}else if(document.forms[0].formType.value=='Duplicate FORM-3'){
			url = "<%=basePath%>reportservlet?method=dupform3&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder;
			}else if(document.forms[0].formType.value=='FORM-4'){
				url = "<%=basePath%>PensionView/PensionReportForm-4.jsp"+params;
			}else if(document.forms[0].formType.value=='FORM-5'){
				url = "<%=basePath%>PensionView/PensionReportForm-5.jsp"+params;
			}else if(document.forms[0].formType.value=='FORM-6A'){
				url="<%=basePath%>reportservlet?method=getform6&type=A&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder;
				//alert(url);
				//url = "<%=basePath%>PensionView/PensionReportForm-6A.jsp";
			}else if(document.forms[0].formType.value=='FORM-6B'){
				url="<%=basePath%>reportservlet?method=getform6&type=B&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder; 
				
				//alert(url);
			}else if(document.forms[0].formType.value=='FORM-6'){
				url="<%=basePath%>reportservlet?method=getform6&type=no&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder; 
			//alert(url);
			}else if(document.forms[0].formType.value=='FORM-7'){
				var pensionno=document.forms[0].frm_pensionno.value;
			  if(document.forms[0].txt_empname.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Should be checked Employee Name');
			  		document.forms[0].txt_empname.focus();
			  		return false;
			  }
			  if (document.forms[0].chk_empflag.checked==true){
			  			empName=document.forms[0].txt_empname.value;
        				frm_empflag=true;
       		  }else{
	       		  frm_empflag=false;
       		  }
       		
			url = "<%=basePath%>reportservlet?method=loadForm7&frm_region="+regionID+"&frm_year="+yearID+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+pensionno+"&frm_airportcd="+airportcode;
			
			
			}else if(document.forms[0].formType.value=='PF CARD'){
				var pensionno=document.forms[0].frm_pensionno.value;
			  if(document.forms[0].txt_empname.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Should be checked Employee Name');
			  		document.forms[0].txt_empname.focus();
			  		return false;
			  }
			  if (document.forms[0].chk_empflag.checked==true){
			  			empName=document.forms[0].txt_empname.value;
        				frm_empflag=true;
       		  }else{
	       		  frm_empflag=false;
       		  }
       		
			url = "<%=basePath%>reportservlet?method=cardReport&frm_region="+regionID+"&frm_year="+yearID+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+pensionno;
			
			}else if(document.forms[0].formType.value=='FORM-8'|| document.forms[0].formType.value=='FORM-8-PS'){
			var pensionno=document.forms[0].frm_pensionno.value;
			var formType=document.forms[0].formType.value;
			url = "<%=basePath%>reportservlet?method=loadForm8&frm_region="+regionID+"&frm_year="+yearID+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&frm_empnm="+empName+"&frm_emp_flag="+frm_empflag+"&frm_pensionno="+pensionno+"&frm_airportcd="+airportcode+"&frm_formType="+formType;
			}else if(document.forms[0].formType.value=='FORM-6'){
				
				url="<%=basePath%>reportservlet?method=getform6&type=no&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&form6Flag="+form6Flag; 
			}else if(document.forms[0].formType.value=='FORM-6-ALL'){
				form6Flag="Y";
				url="<%=basePath%>reportservlet?method=getform6&type=no&frm_region="+regionID+"&frm_year="+yearID+"&frm_month="+monthID+"&frm_airportcd="+airportcode+"&frm_reportType="+reportType+"&sortingOrder="+sortingOrder+"&form6Flag="+form6Flag; 
			}
		if(document.forms[0].formType.value=='FORM-3' || document.forms[0].formType.value=='FORM-6A' ||document.forms[0].formType.value=='FORM-4'||document.forms[0].formType.value=='FORM-5'||document.forms[0].formType.value=='FORM-6' ||document.forms[0].formType.value=='FORM-6A'||document.forms[0].formType.value=='FORM-6B' || document.forms[0].formType.value=='Duplicate FORM-3'
			|| document.forms[0].formType.value=='PF CARD' || document.forms[0].formType.value=='FORM-3-ALL' || document.forms[0].formType.value=='FORM-8' || document.forms[0].formType.value=='FORM-7' || document.forms[0].formType.value=='FORM-3-PS' ||document.forms[0].formType.value=='FORM-6-ALL' || document.forms[0].formType.value=='FORM-8-PS'){
			
        if(document.forms[0].txt_empname.value!='' && document.forms[0].chk_empflag.checked==false){
			  		alert('Employee Name Should be checked ');
			  		document.forms[0].txt_empname.focus();
			  		return false;
			  }
				
				if(reportType=='html' || reportType=='Html'){
	   	 			 LoadWindow(url);
   	 			}else if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 						//alert("url "+url);	
   	 				 		wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
   	 				 		winOpened = true;
							wind1.window.focus();
   	 			}
			
	
			}else{
			LoadWindow(url);
			if(reportType=='Excel Sheet' || reportType=='ExcelSheet' ){
   	 				alert('Developement Activities are in Progress');
   	 		}
			//wind1 = window.open(url,"winComps","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
			//winOpened = true;
			//wind1.window.focus();
			}
			return false;
			
		}
		//return false;
	}
	
</script>
	</HEAD>
	<body class="BodyBackground" >
		<%String monthID = "", yearDescr = "", region = "", monthNM = "",monthToID="",monthToNM="";

           
            Iterator regionIterator = null;
            Iterator monthIterator = null;
               Iterator monthToIterator = null;
            HashMap hashmap = new HashMap();
            if (request.getAttribute("regionHashmap") != null) {
                hashmap = (HashMap) request.getAttribute("regionHashmap");
                Set keys = hashmap.keySet();
                regionIterator = keys.iterator();

            }
            if (request.getAttribute("monthIterator") != null) {
                monthIterator = (Iterator) request
                        .getAttribute("monthIterator");
                 monthToIterator=(Iterator) request.getAttribute("monthToIterator");
            }
       

            %>
		<form action="post">
			<table width="100%" border="0" align="center" cellpadding="1" cellspacing="1">
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

			<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">
						RNFC Form Report Params
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="70%" border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>	<td class=label align="right" nowrap>
																	Form Type:<font color=red>*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	</td>
																	<td>
																		<Select name='formType' Style='width:100px' align="left" onchange="divYears();">
																			<option value=''>
																				[Select One]
																			</option>
																			<%for (int j = 0; j < forms.length; j++) {%>
																			<option value='<%=forms[j]%>'>
																				<%=forms[j]%>
																			</option>
																			<%}%>
																		</SELECT>
																	</td>
																</tr>
							
							
							<tr>
								<td class="label" align="right">
									Region:<font color="red">*</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<SELECT NAME="select_region" style="width:120px" >
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
								<td class="label" align="right" nowrap>
									Report Type: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<SELECT NAME="select_reportType" style="width:88px">
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
								<td align="right">
									&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>
                             <tr>
								<td class="label" align="right">
									From Year:<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<Select name='select_year' Style='width:100px'>
										<option value='NO-SELECT'>
											Select One
										</option>
										<%for (int j = 0; j < year.length; j++) {%>
										<option value='<%=year[j]%>'>
											<%=year[j]%>
										</option>
										<%}%>
									</Select>
								</td>
						</tr>
							<tr id="years">
								<td class="label" align="right">
									From Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<select name="select_month" style="width:100px">
										<Option Value='NO-SELECT' Selected>
											Select One
										</Option>
										<%while (monthIterator.hasNext()) {
											Map.Entry mapEntry = (Map.Entry) monthIterator.next();
											monthID = mapEntry.getKey().toString();
											monthNM = mapEntry.getValue().toString();%>
										<option value="<%=monthID%>">
											<%=monthNM%>
										</option>
										<%}%>
									</select>
								</td>
							</tr>
								<tr>
								<td class="label" align="right">
									To Year:<font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<Select name='select_to_year' Style='width:100px'>
										<option value='NO-SELECT'>
											Select One
										</option>
										<%for (int j = 0; j < year.length; j++) {%>
										<option value='<%=year[j]%>'>
											<%=year[j]%>
										</option>
										<%}%>
									</Select>
								</td>

							</tr>
							<tr>
								<td class="label" align="right">
									To Month:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
								<td align="left">
									<select name="select_to_month" style="width:100px">
										<Option Value='NO-SELECT' Selected>
											Select One
										</Option>
					
										<%while (monthToIterator.hasNext()) {
											Map.Entry mapEntry = (Map.Entry) monthToIterator.next();
											monthID = mapEntry.getKey().toString();
											monthNM = mapEntry.getValue().toString();%>
										<option value="<%=monthID%>">
											<%=monthNM%>
										</option>
										<%}%>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									<input type="button" class="btn" name="Submit" value="Submit" onclick="javascript:validateForm()">
									<input type="button" class="btn" name="Reset" value="Reset" onclick="javascript:resetReportParams()">
									<input type="button" class="btn" name="Submit" value="Cancel" onclick="javascript:history.back(-1)">
								</td>
							</tr>
							
						</table>
					</td>
				</tr>


				<tr>
					<td align="center"></td>
				</tr>
			</table>

                               
		</form>
		
	</body>
</html>
