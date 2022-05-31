
<%@ page language="java" import="java.util.*,aims.common.*"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.PensionBean"%>

<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
            String region = "";
            CommonUtil common = new CommonUtil();

            HashMap hashmap = new HashMap();
            hashmap = common.getRegion();

            Set keys = hashmap.keySet();
            Iterator it = keys.iterator();
             String transferStatus="",adjPfidChkFlag="",statemntRevisdChkFlag="",frmName="",pcReportChkFlag="false";
            if(request.getParameter("transferStatus")!=null){
             transferStatus=request.getParameter("transferStatus").toString();
           // out.println("transferStatus "+transferStatus);
            }else{
            transferStatus="";
            }
             if(request.getParameter("region")!=null){
             region=request.getParameter("region").toString();
           // out.println("region "+region);
            }else{
            	if(request.getAttribute("region")!=null){
            		 region=(String)request.getAttribute("region");
            	}else{
            		 region="";
            	}
           
            }  
            if(request.getParameter("adjPfidChkFlag")!=null){
             adjPfidChkFlag=request.getParameter("adjPfidChkFlag").toString();
           // out.println("transferStatus "+transferStatus);
            }else{
            adjPfidChkFlag="";
            }  
            if(request.getParameter("statemntRevisdChkFlag")!=null){
             statemntRevisdChkFlag=request.getParameter("statemntRevisdChkFlag").toString();
          // out.println("statemntRevisdChkPfid "+statemntRevisdChkPfid);
            }else{
            statemntRevisdChkFlag="";
            }            
           if(request.getParameter("frmName")!=null){
             frmName=request.getParameter("frmName").toString();           
            }else{
            frmName="";
            }  
          if(request.getParameter("pcReportChkFlag")!=null){
             pcReportChkFlag=request.getParameter("pcReportChkFlag").toString();
          
            }else{
            pcReportChkFlag="";
            }  
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>AAI</title>
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript"> 
  
  	
  	function statusdisplay(){
  	
  		  document.getElementById("suggestions").style.display="block";
		  document.getElementById("suggestions").innerHTML="";
		   var str="";
		   str+="<table>"; 
   		  
   		   str+="<tr><td>&nbsp;</td></tr><tr><td colspan='5' id='status'><img src='<%=basePath%>PensionView/images/loading1.gif'></td></tr><tr><td>&nbsp;</td></tr>";
		   str+="</table>";
		  document.getElementById("suggestions").innerHTML=str;
		 
  	
  	}
	function testSS(){
	
  	  var region1= document.forms[0].region1[document.forms[0].region1.options.selectedIndex].text;
  	  if(document.forms[0].empName.value=="" && document.forms[0].pfId.value==""){
  		 alert("Please Enter EmployeeName or PFID");
  		  document.forms[0].empName.focus();
  		  return false;
  	  }
        
   		var empName=document.forms[0].empName.value;
   		//var transferType="no";
   		var transferType="";
   		var pfId = document.forms[0].pfId.value;
   		statusdisplay();   
   		var pcreport="pcreport";
   		var url ="<%=basePath%>search1?method=getEmployeeMappedList&region="+region1+"&empName="+empName+"&transferType="+'<%=transferStatus%>'+"&pfId="+pfId+"&screen="+pcreport+"&adjPfidChkFlag="+'<%=adjPfidChkFlag%>'+"&statemntRevisdChkFlag="+'<%=statemntRevisdChkFlag%>'+"&frmName="+'<%=frmName%>'+"&pcReportChkFlag="+'<%=pcReportChkFlag%>';
   		//alert(url);
   		    
	    document.forms[0].action=url;
	    document.forms[0].method="post";
		document.forms[0].submit();
		
	}	
	 function editPensionMaster(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo){
	 var mappedInfo=cpfaccno+"@"+region+"@"+pensionNumber+"@"+employeeName+"@"+dateofbirth+"@"+empSerialNo;
	// var url="<%=basePath%>PensionView/UniquePensionNumberGeneration.jsp?mappedInfo="+mappedInfo;
	// alert("url"+url);
	//document.forms[0].action=url;
	//document.forms[0].method="post";
	//document.forms[0].submit();
	 window.opener.test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo);	
	 window.close();	 
	}
	 </script>
	</head>

	<body>
		<form>
			<table align="center">
				<tr>
					<td class="label">
						Region:
					</td>
					
					<td>
						<SELECT NAME="region1" style="width:100px">
					
							<%if (session.getAttribute("usertype").equals("Admin")) {%>
							<option value="">[Select One]</option>
              				<%int j = 0;
							boolean exist = false;
							while (it.hasNext()) {
							String region1 = hashmap.get(it.next()).toString();
							j++;
							if (region1.equalsIgnoreCase(region))
							exist = true;
							if (region1.equalsIgnoreCase(region)) {
							%>
							
							<option value="<%=region%>" <% out.println("selected");%>>
								<%=region%>
							</option>
							<%} else {%>
							<option value="<%=region1%>">
								<%=region1%>
							</option>

							<%}}}else{%>
                             
							<option value="<%=region%>"><%=region%></option>
                            <%if(!region.equals("AllRegions")) {%>
							<option value="AllRegions">AllRegions</option>
                             <%}%>
							<%}%>
						</SELECT>
					</td>
					<td class="label">
						Emp Name:
					</td>
					<td>
						<input type="text" name="empName" onkeyup="return limitlength(this, 20)">
						&nbsp;
					</td>
					<td class="label" align="right">
				
					</td>
				<td class="label">
						PFID:
					</td>
					<td>
						<input type="text" name="pfId" onkeyup="return limitlength(this, 20)">
						&nbsp;
					</td>&nbsp;
					
					&nbsp;
					<td>
						<input type="button" class="btn" value="Search" class="btn" onclick="testSS();">
					</td>
				</tr>

			</table>
				<div id="suggestions"></div>

			<%PensionBean dbBeans = new PensionBean();

            if (request.getAttribute("MappedList") != null) {
                ArrayList datalist = new ArrayList();
                int totalData = 0;
                datalist = (ArrayList) request.getAttribute("MappedList");
                System.out.println("dataList " + datalist.size());

                if (datalist.size() == 0) {

                %>
			<tr>

				<td>
					<table align="center" id="norec">
						<tr>
							<br>
							<td>
								<b> No Records Found </b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<%} else if (datalist.size() != 0) {%>
			<tr>
				<td height="25%">
					<table align="center" width="97%" cellpadding=2 class="tbborder" cellspacing="0" border="0">
						<tr class="tbheader">
						<td class="tblabel">Serial No
							</td>
							<td class="tblabel">
								CPF NO
							</td>
							<td class="tblabel">
								Region
							</td>
							<td class="tblabel">
								PFID
								<br>
								No&nbsp;&nbsp;
							</td>
							<td class="tblabel">
								Pension Number&nbsp;&nbsp;
							</td>
							<td class="tblabel">
								Employee Name&nbsp;&nbsp;
							</td>
							<td class="tblabel">
								D.O.B
							</td>

							<td>
								<img src="./PensionView/images/page_edit.png" alt="edit" />
								&nbsp;

							</td>


						</tr>

						<%}%>
						<%int count = 0;
                String airportCode = "", employeeName = "", cpfacno = "", pensionNumber = "";
                String dateofBirth = "", empSerialNo = "", dateofJoining = "";
                for (int i = 0; i < datalist.size(); i++) {
                    count++;
                    PensionBean beans = (PensionBean) datalist.get(i);

                    cpfacno = beans.getCpfAcNo();
                    employeeName = beans.getEmployeeName();
                    pensionNumber = beans.getPensionnumber();
                    dateofBirth = beans.getDateofBirth();
                    dateofJoining = beans.getDateofJoining();
                    region = beans.getRegion();
                    empSerialNo = beans.getEmpSerialNumber();
                    if (count % 2 == 0) {

                    %>
						<tr>
							<%} else {%>
						<tr>
							<%}%>
							
							<td class="Data" width="12%">
								<%=count%>
							</td>
							<td class="Data" width="12%">
								<%=cpfacno%>
							</td>
							<td class="Data" width="12%">
								<%=region%>
							</td>
							<td class="Data" width="12%">
								<%=empSerialNo%>
							</td>
							<td class="Data">
								<%=pensionNumber%>
							</td  class="Data">

							<td class="Data" width="12%">
								<%=employeeName%>
							</td>
							<td class="Data">
								<%=dateofBirth%>
							</td>

							<td>
								<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:editPensionMaster('<%=cpfacno%>','<%=region%>','<%=pensionNumber%>','<%=employeeName%>','<%=dateofBirth%>','<%=empSerialNo%>')" />
							</td>

						</tr>
						<%}%>


						<%}%>

					</table>
				</td>

			</tr>

			<table>
				</form>
	</body>
</html>
