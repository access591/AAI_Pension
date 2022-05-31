
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.FinalPaymentsBean"%>

<%@ taglib uri="/WEB-INF/tld/displaytag.tld" prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
<head>

 <title>AAI</title>

   	<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">	
   	<link rel="stylesheet" href="<%=basePath%>PensionView/css/displaytagstyle.css" type="text/css">

	<SCRIPT type="text/javascript">
		function btn_search(){
			var finyear='',activeflag='';
		
			if(document.forms[0].select_employee.selectedIndex>0){
				activeflag=document.forms[0].select_employee.options[document.forms[0].select_employee.selectedIndex].value;
			}else{
				activeflag=document.forms[0].select_employee.value;
			}
			if(document.forms[0].select_fin_year.selectedIndex>0){
				finyear=document.forms[0].select_fin_year.options[document.forms[0].select_fin_year.selectedIndex].value;
			}else{
				finyear=document.forms[0].select_fin_year.value;
			}
		
			document.forms[0].action="<%=basePath%>reportservlet?method=finalempscreen&frm_financeyear="+finyear+"&frm_active_flag="+activeflag;
			document.forms[0].method="post";
			document.forms[0].submit();
			
		}
		function btn_print(){
			var finyear='',activeflag='',url='';
		
			if(document.forms[0].select_employee.selectedIndex>0){
				activeflag=document.forms[0].select_employee.options[document.forms[0].select_employee.selectedIndex].value;
			}else{
				activeflag=document.forms[0].select_employee.value;
			}
			if(document.forms[0].select_fin_year.selectedIndex>0){
				finyear=document.forms[0].select_fin_year.options[document.forms[0].select_fin_year.selectedIndex].value;
			}else{
				finyear=document.forms[0].select_fin_year.value;
			}
		
			url="<%=basePath%>reportservlet?method=finalpaymentspending&frm_financeyear="+finyear+"&frm_active_flag="+activeflag;
			LoadWindow(url);
		}
		 function LoadWindow(params){
			    var newParams =params;
				winHandle = window.open(newParams,"Utility","menubar=yes,toolbar= yes,statusbar=1,scrollbars=yes,resizable=yes");
				winOpened = true;
				winHandle.window.focus();
			}
		function btn_reset(){
			document.forms[0].action="<%=basePath%>reportservlet?method=loadfinalempscreen";
			document.forms[0].method="post";
			document.forms[0].submit();
		}
		function btn_edit(){
			var pfidstrip="";
			var mypension_array,indexpensionlist;
			var chklist=document.forms[0].checklist.value;
			if(document.forms[0].activityflag.length!=undefined){
				for(i=0;i<document.forms[0].activityflag.length;i++){
							if(document.forms[0].activityflag[i].checked==true){
									pensionno=document.forms[0].activityflag[i].value;
									mypension_array=pensionno.split("-");
									chklist=chklist.replace(mypension_array[0],'');
									if(mypension_array[1]=='Y'){
										if(pfidstrip==''){
											pfidstrip=mypension_array[0];
										}else{
											pfidstrip=pfidstrip+','+mypension_array[0];
										
										}
									}
							}
					}
			}else{
						if(document.forms[0].activityflag.checked==true){
									pensionno=document.forms[0].activityflag.value;
									chklist=chklist.replace(mypension_array[0],'');
									if(mypension_array[1]=='Y'){
										pfidstrip=mypension_array[0];
									}
							}
			}
			
			if(pfidstrip=='' && chklist==''){
				alert('User should be select any one pfid');
			}else{
					var finyear='',activeflag='';
		
			if(document.forms[0].select_employee.selectedIndex>0){
				activeflag=document.forms[0].select_employee.options[document.forms[0].select_employee.selectedIndex].value;
			}else{
				activeflag=document.forms[0].select_employee.value;
			}
			if(document.forms[0].select_fin_year.selectedIndex>0){
				finyear=document.forms[0].select_fin_year.options[document.forms[0].select_fin_year.selectedIndex].value;
			}else{
				finyear=document.forms[0].select_fin_year.value;
			}
					document.forms[0].action="<%=basePath%>reportservlet?method=saveempscreen&pfidstrips="+pfidstrip+"&frm_financeyear="+finyear+"&frm_active_flag="+activeflag+"&frm_deletedlist="+chklist;
					document.forms[0].method="post";
					document.forms[0].submit();
			}
			
		}
		function checkedall(){
					for(i=0;i<document.forms[0].activityflag.length;i++){
							document.forms[0].activityflag[i].checked=true;
					}
		}
		function uncheckedall(){
					for(i=0;i<document.forms[0].activityflag.length;i++){
							document.forms[0].activityflag[i].checked=false;
				}
		}
		function fmload(financeyear,flag,message){
			
			if(message!='null' && message!=''){
				alert(message);
			}
			if(financeyear!=null){
		
				for(i=0;i<document.forms[0].select_fin_year.length;i++){
						if(document.forms[0].select_fin_year.options[i].value==financeyear){
							document.forms[0].select_fin_year.options[i].selected=true;
						}				
					}
		
				
			}
			
			if(flag!=null){
					
					for(i=0;i<document.forms[0].select_employee.length;i++){
						if(document.forms[0].select_employee.options[i].value==flag){
							document.forms[0].select_employee.options[i].selected=true;
						}				
					}

					
				
			
			}		
		}
	</SCRIPT>
</head>

<body onload="fmload('<%=request.getAttribute("finanaceYear")%>','<%=request.getAttribute("empactivity")%>','<%=request.getAttribute("message")%>')">
   <form name="personalMaster" action="" method="post">
	 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="5%" colspan="2" align="center" class="ScreenHeading">Active/Inactive Employee Information</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="15%">
				<table align="center"  cellpadding="0" cellspacing="0">
					<tr>
						<td class="label">Status:&nbsp;</td>
						<td>
							<SELECT NAME="select_employee" style="width:130px">
							<option value="NO-SELECT">[Select One]</option>
								<option value="Y" >Active Employees</option>
	                       		<option value="N" >Inactive Employees</option>
							</SELECT>
						</td>
						<td class="label">Finance Year:&nbsp;</td>
						<td>
							<select name="select_fin_year" style="width:130px" >
								<option value="2009-2010">2009-10</option>
								<option value="2010-2011">2010-11</option>
								<option value="2011-2012">2011-12</option>
								<option value="2012-2013">2012-13</option>
					
						    </select>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td>
								<input type="button" class="btn" value="Search" class="btn" onclick="javascript:btn_search()">
								<input type="button" class="btn" value="Reset" onclick="javascript:btn_reset()" class="btn">
								<input type="button" class="btn" value="Cancel" onclick="javascript:history.back(-1)" class="btn">
						</td>

					</tr>
				</table>
			</td>
		</tr>
		<tr>
						<td>&nbsp;</td>
					</tr>
		<tr>
			
			<td align="center">
				
					<input type="button" class="btn" value="Edit" class="btn" onclick="javascript:btn_edit()">
					<input type="button" class="btn" value="Print" class="btn" onclick="javascript:btn_print()">
			</td>
		</tr>
		<%if (request.getAttribute("emplist")!=null){%>
			<input type="hidden" name="checklist" value="<%=request.getAttribute("chkedList")%>">
							<tr>
								<td align="right"><a href="javascript:checkedall()">check</a> /<a href="javascript:uncheckedall()">un check</a> </td>
							</tr>
        					<tr>
								<td align="center" width="100%">
									
									<display:table style="width:100%;height:50px" cellspacing="0" cellpadding="0" class="displaytable" keepStatus="true"  sort="list" name="emplist" name="requestScope.emplist" pagesize="25" id='searchData' requestURI="/reportservlet?method=finalempscreen">
									
										<display:column value="" sortable="false" headerClass="sortable" title="Sl.No"/>
										<display:column property="pensionno" sortable="false" headerClass="sortable" title="PF ID"/>
										<display:column property="employeename" sortable="false" headerClass="sortable" title="Name of the Member"/>
										<display:column property="dateofbirth" sortable="false" headerClass="sortable" title="Date of birth"/>
										<display:column property="dateofjoining" sortable="false" headerClass="sortable" title="Date Of Joining"/>
										<display:column property="wetheroption" sortable="false" headerClass="sortable" title="Option"/>
										<display:column property="finalsettlemntdate" sortable="false" headerClass="sortable" title="Interest Rate <br/>calucaltion upto"/>
										<display:column property="empnetob" sortable="false" headerClass="sortable" title="Employee Subscription"/>
										<display:column property="aainetob" sortable="false" headerClass="sortable" title="Employer Contribution"/>
										<display:column title="" media="html">
											<input type="checkbox" name="activityflag" id="activityflag" value="<%=((FinalPaymentsBean)pageContext.getAttribute("searchData")).getPensionno()+"-"+((FinalPaymentsBean)pageContext.getAttribute("searchData")).getActivity()%>" <%=((FinalPaymentsBean)pageContext.getAttribute("searchData")).getActivityRemarks()%>/>
										</display:column>
	
									</display:table>
								</td>
							</tr>   
		<%}%>        
			
	</table>
  </form>
</body>
</html>
