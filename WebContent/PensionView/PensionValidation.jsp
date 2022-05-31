
<%@ page import="java.util.*,aims.common.CommonUtil,aims.service.FinancialService,aims.bean.EmployeeValidateInfo" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <script type="text/javascript">
    function redirectPageNav(navButton,index,totalValue){   
    	var monthID,airportID,yearID,dateString,monthName,selectedInputParam;
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		
		monthName=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		dateString=yearID+"0"+monthID;
		}else{
		dateString=yearID+monthID;
		}
		
		selectedInputParam=monthID+"/"+monthName+"/"+dateString+"/"+airportID;
		document.forms[0].action="<%=basePath%>validatefinance?method=validateNavigation&navButton="+navButton+"&strtindx="+index+"&total="+totalValue+"&effectDT="+dateString+"&unitCD="+airportID+"&monthID="+monthID+"&monthName="+monthName+"&selectedIParam="+selectedInputParam;
	//	alert(document.forms[0].action)
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	/*function callReport(){
		alert('Under Construction');
	}*/
		function callReport(){
	
		var monthID,airportID,yearID,dateString,monthName,selectedInputParam;
		monthID=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].value;
		
		monthName=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		dateString=yearID+"0"+monthID;
		}else{
		dateString=yearID+monthID;
		}
		path="<%=basePath%>validatefinance?method=validationReport&effectDT="+dateString+"&unitCD="+airportID+"&monthID="+monthID+"&monthName="+monthName
		window.open (path,"mywindow","location=1,status=1,toolbar=1,width=700,height=450,resizable=yes");
		
	}
	function editPensionMaster(obj,employeeName,employeeCode){
	 	
		var cpfacno=obj;
		var answer =confirm('Are you sure, do you want edit this record');
	if(answer){
		var flag="true";
		document.forms[0].action="<%=basePath%>search1?method=edit&cpfacno="+cpfacno+"&name="+employeeName+"&flag="+flag+"&empCode="+employeeCode;
		
		document.forms[0].method="post";
		document.forms[0].submit();
	}
	}
    function get_pensioninfo(){
    	var monthID,airportID,yearID,dateString,monthName,selectedInputParam;
		monthID=document.forms[0].select_month.selectedIndex;
		monthName=document.forms[0].select_month.options[document.forms[0].select_month.selectedIndex].text;
		airportID=document.forms[0].select_airport.options[document.forms[0].select_airport.selectedIndex].text;
		yearID=document.forms[0].select_year.options[document.forms[0].select_year.selectedIndex].text;
		if(monthID<10){
		dateString=yearID+"0"+monthID;
		}else{
		dateString=yearID+monthID;
		}
			selectedInputParam=monthID+"/"+monthName+"/"+dateString+"/"+airportID;
			
	
		document.forms[0].action="<%=basePath%>validatefinance?method=getPensionValidate&effectDT="+dateString+"&unitCD="+airportID+"&monthID="+monthID+"&monthName="+monthName+"&selectedIParam="+selectedInputParam;
		//alert(document.forms[0].action);
		document.forms[0].method="post";
		document.forms[0].submit();
	
    }
    function submit_Pension(inputParam,effectiveDt,employeeName,whetheroption,dateOfBirth,emoluments,cpfaccno,aaiPF,aaiPension,aaiTotal,unitCD){
    var path="";
    
    if(emoluments=='' || emoluments==null){
     alert('Emoluments Cannot be blank');
    	return false;
    }
    if(employeeName=='' || employeeName==null){
    alert('Employee Name Cannot be blank');
    return false;
    }
    if(dateOfBirth=='' || dateOfBirth==null){
    alert('Date Of Birth Cannot be blank');
    return false;
    }
    if(whetheroption=='' || whetheroption==null ){
    alert('Whether Option Cannot be blank');
    return false;
    }
    
   // alert(cpfaccno+'aaiPF'+aaiPF+'aaiPension'+aaiPension+'aaiTotal'+aaiTotal+'unitCD'+unitCD);
    path="<%=basePath%>validatefinance?method=getPensionDetail&cpfaccno="+cpfaccno+"&valAAIPF="+aaiPF+"&valAAIPension="+aaiPension+"&valAAITotal="+aaiTotal+"&airportCD="+unitCD+"&effectiveDate="+effectiveDt+"&selectedParam="+inputParam;
    window.open (path,"","resizable=1,width=550,height=450"); 
  
    	
    }
    </script>
  </head>
  <%
  	CommonUtil common=new CommonUtil();
  	ArrayList finList=new ArrayList();
  	FinancialService financialService=new FinancialService();
  	String monthID="",monthNM="",yearID="",yearDescr="",untiCD="",airportName="",tempYearVal="",tempUnitVal="",tempMonthVal="",tempMonthNameVal="";
  	String inputParamValues="";
  	Iterator monthIterator=null;
  	Iterator yearIterator=null;
  	Iterator airportIterator=null;
  	  	Map yearMap=new LinkedHashMap();
  		yearMap=financialService.getPensionYearList();
  		Set yearSet = yearMap.entrySet(); 
		 yearIterator = yearSet.iterator(); 
  

	
  		Map map=new LinkedHashMap();
  		map=common.getMonthsList();
  		Set monthset = map.entrySet(); 
		monthIterator = monthset.iterator(); 
		
		  	Map airportMap=new LinkedHashMap();
  		airportMap=financialService.getPensionAirportList();
  		Set airportSet = airportMap.entrySet(); 
		 airportIterator = airportSet.iterator(); 
  	
  	if(request.getAttribute("unitCD")!=null){
  		untiCD=(String)request.getAttribute("unitCD");
  	}
  	if(request.getAttribute("selectParams")!=null){
  		inputParamValues=(String)request.getAttribute("selectParams");
  	}
  %>
  <body class="BodyBackground">
  	<form name="validation" method="post">
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
			
			
					<jsp:include page="/PensionView/PensionMenu.jsp"/>
				
					
					</td>
				</tr>
				
				<tr>
					<td >
						&nbsp;
					</td>
				</tr>
					<tr>
					<td >
						<table height="5%" align="center">
						<tr>
							<td class="ScreenHeading"> Pension Validation</td>
						</tr>
						
						  </table>
					</td>
				</tr>
				<tr>
					<td >
						<table  height="15%" align="center">
						<tr>
							<td class="label"> Year</td>
							
							<td >
								<select name="select_year">
  								 <Option Value='' Selected>Select One</Option>
			  					<%
			  					
			  					if(request.getAttribute("effectiveYear")!=null){%>
			  					<option value="<%=request.getAttribute("effectiveYear")%>" <% out.println("selected");%>><%=request.getAttribute("effectiveYear")%></option>
			  					<%}%>
  							    <%while(yearIterator.hasNext()) { 
								Map.Entry mapEntry = (Map.Entry)yearIterator.next();
								monthID=mapEntry.getKey().toString();
								yearDescr=mapEntry.getValue().toString();
								 %>
								<%if(request.getAttribute("effectiveYear")!=null){
									tempYearVal=request.getAttribute("effectiveYear").toString();
									if(!tempYearVal.equals(yearDescr)){%>
												<option value="<%=yearDescr%>" ><%=yearDescr%></option>
									<%}
								}else{%>
							 	<option value="<%=yearDescr%>" ><%=yearDescr%></option>
							 	<%}%>
							<%}%>

	  					</select>
	  					</td>
						</tr>
						<tr>
							<td class="label">Select Month</td>
							
							<td >
								<select name="select_month">
  								 <Option Value='' Selected>Select One</Option>
  							
  								<%if(request.getAttribute("monthID")!=null && request.getAttribute("monthName")!=null){%>
			  					<option value="<%=request.getAttribute("monthID")%>" <% out.println("selected");%>><%=request.getAttribute("monthName")%></option>
			  					<%}%>
			  					
			  					<%
  								 while(monthIterator.hasNext()) { 
								Map.Entry me = (Map.Entry)monthIterator.next();
								monthID=me.getKey().toString();
								monthNM=me.getValue().toString();
								 %>
							
								<%if(request.getAttribute("monthID")!=null && request.getAttribute("monthName")!=null){
									tempMonthVal=request.getAttribute("monthID").toString();
									tempMonthNameVal=request.getAttribute("monthName").toString();
									if(!tempMonthVal.equals(monthID) && !tempMonthNameVal.equals(monthNM)){%>
									<option value="<%=monthID%>" ><%=monthNM%></option>
									<%}
								}else{%>
							
					 		<option value="<%=monthID%>" ><%=monthNM%></option>
					 		<%}%>
					 			
							<%}%>

	  					</select>
	  					</td>
						</tr>
						<tr>
							<td class="label">Station</td>
							<td >
										<select name="select_airport" onChange="javascript:get_pensioninfo()" onBlur="javascript:get_pensioninfo()">
  								 <Option Value='' Selected>Select One</Option>
  								<%if(request.getAttribute("unitCD")!=null){%>
			  					<option value="<%=request.getAttribute("unitCD")%>" <% out.println("selected");%>><%=request.getAttribute("unitCD")%></option>
			  					<%}%>
			  					<%
  								 while(airportIterator.hasNext()) { 
								Map.Entry me = (Map.Entry)airportIterator.next();
								
								airportName=me.getValue().toString();
								 %>
									<%if(request.getAttribute("unitCD")!=null){
									tempUnitVal=request.getAttribute("unitCD").toString();
									if(!tempUnitVal.equals(airportName)){%>
													<option value="<%=airportName%>" ><%=airportName%></option>
									<%}
								}else{%>
					 		<option value="<%=airportName%>" ><%=airportName%></option>
					 		<%}%>
					 			
							<%}%>
						
	  					</select>
	  					
					
	  					</td>
						</tr>
						  </table>
					</td>
					
				</tr>
				<tr>
				<td>&nbsp;&nbsp;&nbsp;</td>
				</tr>
				<%
				SearchInfo getSearchInfo = new SearchInfo();
				if (request.getAttribute("searchBean") != null) {
					int totalData = 0;
					SearchInfo searchBean = new SearchInfo();
			
					ArrayList dataList = new ArrayList();
					BottomGridNavigationInfo bottomGrid = new BottomGridNavigationInfo();
					searchBean = (SearchInfo) request.getAttribute("searchBean");
					int index = searchBean.getStartIndex();
				
					dataList = searchBean.getSearchList();
					totalData = searchBean.getTotalRecords();
					bottomGrid = searchBean.getBottomGrid();
					if (dataList.size() != 0) {

					%>
				<tr>

					<td>
						<table align="center"  >
							<tr>
								<td colspan="3">
								</td>
								<td colspan="2" align="right">
									<input type="button" alt="first" value="|<" name="First" disable=true onClick="javascript:redirectPageNav('|<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusFirst()%>>
									<input type="button" alt="pre" value="<" name="Pre"  onClick="javascript:redirectPageNav('<','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusPrevious()%>>
									<input type="button"  alt="next" value=">" name="Next" onClick="javascript:redirectPageNav('>','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusNext()%>>
									<input type="button" value=">|" name="Last" onClick="javascript:redirectPageNav('>|','<%=index%>','<%=totalData%>')" <%=bottomGrid.getStatusLast()%>>
								     <img src="./PensionView/images/printIcon.gif" alt="Report"  onClick="callReport()">
									
								</td>
							</tr>
						</table>
					
				</tr>
				<tr >
					<td>
						<table align="center" cellpadding=2 class="tbborder" cellspacing="0"  border="">
					
						
							<tr class="tbheader">
			<td rowspan="2">CPF ACC.NO&nbsp;&nbsp;</td>
			<td rowspan="2">Employee Name&nbsp;&nbsp;</td>
			<td rowspan="2">Pension<br/>
						Option&nbsp;&nbsp;</td>
			<td rowspan="2">Emoluments&nbsp;&nbsp;</td>
			<td colspan="5" align="center">Employee subscription</td>
			<td colspan="3" align="center">AAI Contribution</td>
			<td colspan="4" align="center">Validate AAI Contribution</td>
		</tr>
		<tr class="tbheader">
			<td>PF<br />
				Statuary&nbsp;&nbsp;</td>
			<td>VPF&nbsp;&nbsp;</td>
			<td>Principal&nbsp;&nbsp;</td>
			<td>Interest&nbsp;&nbsp;</td>
			<td>Total&nbsp;&nbsp;</td>
			<td>PF&nbsp;&nbsp;</td>
			<td>Pension&nbsp;&nbsp;</td>
			<td>Total&nbsp;&nbsp;</td>
			<td>PF&nbsp;&nbsp;</td>
			<td>Pension&nbsp;&nbsp;</td>
			<td>Total&nbsp;&nbsp;</td>
			<td>&nbsp;&nbsp;</td>
		</tr>
							
							
							<%
								EmployeeValidateInfo empValidate=null;
								String backColor="",validBackColor="";
								for(int fnls=0;fnls<dataList.size();fnls++){
								empValidate=new EmployeeValidateInfo();
								empValidate=(EmployeeValidateInfo)dataList.get(fnls);
							%>
							<tr> 
								<td class="Data"><a href="javascript:editPensionMaster('<%=empValidate.getCpfaccno()%>','<%=empValidate.getEmployeeName()%>','<%=empValidate.getEmployeeNo()%>')"><%=empValidate.getCpfaccno()%></a></td>
								<td class="Data"><%=empValidate.getEmployeeName()%></td>
								<td class="Data"><%=empValidate.getWetherOption()%></td>
								<td class="Data"><%=empValidate.getEmoluments()%></td>
							
											<td class="Data"><%=empValidate.getEmpPFStatuary()%> &nbsp;&nbsp;</td>
											<td  class="Data"><%=empValidate.getEmpVPF()%>  &nbsp;&nbsp;</td>
											<td  class="Data"><%=empValidate.getEmpAdvRecPrincipal()%>  &nbsp;&nbsp;</td>
											<td  class="Data"><%=empValidate.getEmpAdvRecInterest()%> &nbsp;&nbsp; </td>
											<td  class="Data"><%=empValidate.getEmptotal()%> &nbsp;&nbsp; </td>
								
								
								<%if (empValidate.isValidateFlag()==true){
									backColor="style=background-color:red";
									validBackColor="style=background-color:green";
									}else{
									backColor="style=background-color:#f8f8ff";
									validBackColor="style=background-color:#f8f8ff";
									}
								%>
								
								
											<td class="Data" <%=backColor%>>
											<%=empValidate.getAaiconPF()%>&nbsp;&nbsp;
											</td>
											<td class="Data" <%=backColor%>>
											<%=empValidate.getAaiconPension()%>&nbsp;&nbsp;
											</td>
											<td class="Data" <%=backColor%>>
											<%=empValidate.getAaiTotal()%>&nbsp;&nbsp;
											</td>
										
								
											<td class="Data" <%=validBackColor%>><%=empValidate.getValAAIPF()%>&nbsp;&nbsp;</td>
											<td class="Data" <%=validBackColor%>><%=empValidate.getValAAIPension()%>&nbsp;&nbsp;</td>
											<td class="Data" <%=validBackColor%>><%=empValidate.getValAAITotal()%>&nbsp;&nbsp;</td>
										
								<td><img alt="" src="<%=basePath%>PensionView/images/addIcon.gif" onclick="javascript:submit_Pension('<%=inputParamValues%>','<%=empValidate.getEffectiveDate()%>','<%=empValidate.getEmployeeName()%>','<%=empValidate.getWetherOption()%>','<%=empValidate.getDateOfBirth()%>','<%=empValidate.getEmoluments()%>','<%=empValidate.getCpfaccno()%>','<%=empValidate.getValAAIPF()%>','<%=empValidate.getValAAIPension()%>','<%=empValidate.getValAAITotal()%>','<%=untiCD%>')"></td>
							</tr>
							
							<%}%>
							
							<%}}%>
						</table>
					</td>		
				</tr>
  	</table>
  	</form>
  </body>
</html>
