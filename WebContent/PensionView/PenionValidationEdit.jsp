<%@ page language="java"%>
<%@ page  import="java.util.*,aims.bean.EmployeeValidateInfo" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
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
 	<SCRIPT type="text/javascript">
		function updateValidate(seletedParam,effectiveDate,cpfaccno,valpf,valpension,valtotal,unitCD){
		var path="";
 		  // alert(cpfaccno+'aaiPF'+aaiPF+'aaiPension'+aaiPension+'aaiTotal'+aaiTotal+'unitCD'+unitCD);
 		  if(isNaN(document.pensionvaledit.pfDeduction.value)==true){
 		  	alert('Invalid data');
 		  
 		  }else if(isNaN(document.pensionvaledit.pfStatury.value)==true){
 		  	alert('Invalid data');
 		  }else{
 		  	 var pfStatury=document.pensionvaledit.pfStatury.value;
 		  	 var pfDeduction=document.pensionvaledit.pfDeduction.value;
 		  	 path="<%=basePath%>validatefinance?method=updatePensionDetail&cpfaccno="+cpfaccno+"&valAAIPF="+valpf+"&valAAIPension="+valpension+"&valAAITotal="+valtotal+"&airportCD="+unitCD+"&valPFStatury="+pfStatury+"&valPFDeduction="+pfDeduction+"&effectiveSelDate="+effectiveDate+"&selectParams="+seletedParam;
		  	document.forms[0].action=path;
			document.forms[0].method="post";
			document.forms[0].submit();
 		  }
  	
		
		}
		function resetValidate(){
			document.pensionvaledit.pfDeduction.value="";
			document.pensionvaledit.pfStatury.value="";
		
		}
		function closeValidate(){
			window.close();
		}
 	</SCRIPT>
  </head>
  <%
  	
  	String validateAAIPF="",validateAAIPension="",validateAAITotal="",unitCD="",effectiveDate="",selectedParam="";
  	EmployeeValidateInfo employeeValidate=null;
	if(request.getAttribute("loadFinValidationList")!=null){
	employeeValidate=(EmployeeValidateInfo)request.getAttribute("loadFinValidationList");
		
	}
	if(request.getAttribute("validateAAIPF")!=null){
		validateAAIPF=(String)request.getAttribute("validateAAIPF");;
	}
	if(request.getAttribute("validateAAIPension")!=null){
		validateAAIPension=(String)request.getAttribute("validateAAIPension");
	}
	if(request.getAttribute("validateAAITotal")!=null){
		validateAAITotal=(String)request.getAttribute("validateAAITotal");
	}
	if(request.getAttribute("unitCode")!=null){
		unitCD=(String)request.getAttribute("unitCode");
	}
  	if(request.getAttribute("effectiveDate")!=null){
		effectiveDate=(String)request.getAttribute("effectiveDate");
	}
	if(request.getAttribute("selectedParam")!=null){
		selectedParam=(String)request.getAttribute("selectedParam");
	}
  %>
  <body class="BodyBackground" onload="document.forms[0].pfDeduction.focus();">
   <form name="pensionvaledit" method="post">
   <table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	   			<tr>
		  				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  			</tr>
		  	
		 <tr>
				<td>
				<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
				<tr>
					<td height="5%" colspan="2" align="center" class="ScreenMasterHeading">	Validation[Edit]</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
				</tr>

   		<tr>
		  <td>
		  		<table height="10%" align="center" width="100%"> 
		  			<tr>
		  				<td class="label">CPF ACC.NO</td>
		  				<td class="Data"><%=employeeValidate.getCpfaccno()%></td>	
		  				<td class="label">Employee No</td>
		  				<td class="Data"><%=employeeValidate.getEmployeeNo()%></td>
		  			</tr>
		  			<tr>
		  				<td class="label">Employee Name</td>
		  				<td class="Data"><%=employeeValidate.getEmployeeName()%></td>
		  				<td class="label">Designation</td>
		  				<td class="Data"><%=employeeValidate.getDesegnation()%></td>
		  			</tr>
		  			<tr>
		  				<td class="label">Emoluments For PF deduction</td>
		  				<td class="Data"><input type="text" name="pfDeduction" value="<%=employeeValidate.getEmoluments()%>"></td>
		  				
		  			</tr>
		  				<tr>
		  				<td class="label">PF Statuary</td>
		  				<td class="Data"><input type="text" name="pfStatury" value="<%=employeeValidate.getEmpPFStatuary()%>"></td>
		  			</tr>
		  			<tr>
		  				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  			</tr>
		  			<tr>
		  				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  			</tr>
		  			<tr>
		  			
		  			
		  			<td>
						<table align="center">
								<tr>
								
								 <td colspan="5" align="center" class="label">AAI Contribution</td>
										</tr>
										<tr>
											<td class="label">PF</td>
											<td class="label">Pension</td>
											<td class="label">Total</td>
										</tr>
									</table>
								</td>
								<td>
									<table>
										<tr>
											<td colspan="5" align="center" class="label">Validate AAI Contribution</td>
										</tr>
										<tr>
											<td class="label">PF</td>
											<td class="label">Pension</td>
											<td class="label">Total</td>
										</tr>
									</table>
								</td>
					</tr>
					
					<tr>
		  			
		  			
		  			<td>
						<table>
								
										<tr>
											<td class="Data"><%=employeeValidate.getAaiconPF()%></td>
											<td class="Data"><%=employeeValidate.getAaiconPension()%></td>
											<td class="Data"><%=employeeValidate.getAaiTotal()%></td>
										</tr>
									</table>
								</td>
								<td>
									<table>
										
										<tr>
											<td class="Data"><%=validateAAIPF%></td>
											<td class="Data"><%=validateAAIPension%></td>
											<td class="Data"><%=validateAAITotal%></td>
										</tr>
									</table>
								</td>
					</tr>
						<tr>
		  				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		  			</tr>
				  	<tr>
				  	<td colspan="5" align="center">
				  	<table width="100%">
				  	<tr>
				  
				  	<td align="center"><input type="button" class="btn" name="btnUpdate" value="Update" onclick="javascript:updateValidate('<%=selectedParam%>','<%=effectiveDate%>','<%=employeeValidate.getCpfaccno()%>','<%=validateAAIPF%>','<%=validateAAIPension%>','<%=validateAAITotal%>','<%=unitCD%>')">
				  	<input type="button" class="btn" name="btnReset" value="Reset" onclick="javascript:resetValidate()">
				  	<input type="button" class="btn" name="btnCancel" value="Cancel" onclick="javascript:closeValidate()"></td>
					</tr>		  				
		  			</table>
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
