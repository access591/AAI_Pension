
<%@ page language="java" import="java.util.*,aims.common.*"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.*"%>

<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
            String region = "",chkpfid="",statemntRevisdChkPfid="",PCAftrSepFlag="";
            CommonUtil common = new CommonUtil();

            HashMap hashmap = new HashMap();
            
            hashmap = common.getRegion();
            String fndRegion="";
			if(request.getAttribute("fndregion")!=null){
				fndRegion=(String)request.getAttribute("fndregion");
			}else{
				fndRegion="";
			}
			if(request.getAttribute("chkpfid")!=null){
				chkpfid=(String)request.getAttribute("chkpfid");
			} 
			if(request.getAttribute("statemntRevisdChkPfid")!=null){
				statemntRevisdChkPfid=(String)request.getAttribute("statemntRevisdChkPfid");
			} 
			if(request.getAttribute("PCAftrSepFlag")!=null){
				PCAftrSepFlag=(String)request.getAttribute("PCAftrSepFlag");
			} 
            Set keys = hashmap.keySet();
            Iterator it = keys.iterator();
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
   		var pfId = document.forms[0].pfId.value;
   		var transferStatus="",pcreport="pcreport";
   		var url ="<%=basePath%>search1?method=getEmployeeMappedList&region="+region1+"&empName="+empName+"&transferType="+transferStatus+"&pfId="+pfId+"&screen="+pcreport;
   		
   		
   		statusdisplay();       
	    document.forms[0].action=url;
	    document.forms[0].method="post";
		document.forms[0].submit();
	}
	
	
	 function editPensionMaster(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,dateofJoining,pcreportverified){
	 
	 var mappedInfo=cpfaccno+"@"+region+"@"+pensionNumber+"@"+employeeName+"@"+dateofbirth+"@"+empSerialNo+"@"+dateofJoining;
	// var url="<%=basePath%>PensionView/UniquePensionNumberGeneration.jsp?mappedInfo="+mappedInfo;
	// alert("url"+url);
	//document.forms[0].action=url;
	//document.forms[0].method="post";
	//document.forms[0].submit();
	var chkpfid='<%=chkpfid%>';
	var statemntRevisdChkPfid ='<%=statemntRevisdChkPfid%>';
	var PCAftrSepFlag='<%=PCAftrSepFlag%>';
	 window.opener.test(cpfaccno,region,pensionNumber,employeeName,dateofbirth,empSerialNo,chkpfid,dateofJoining,pcreportverified,statemntRevisdChkPfid,PCAftrSepFlag);	
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
						<%if (session.getAttribute("usertype").equals("Admin")) {

                      %>
							<option value="">[Select One]</option>
							<%int j = 0;
                boolean exist = false;
                while (it.hasNext()) {
                    region = hashmap.get(it.next()).toString();
                    j++;

                    {%>
							<option value="<%=region%>">
								<%=region%>
							</option>

							<%}
                }

            %>

							<%}else{%>           
            <option value="<%=fndRegion%>">
								<%=fndRegion%>
							</option>
    <%if(!region.equals("AllRegions")){%>
	<option value="AllRegions">AllRegions</option>
    <%}%>
    <%}%>
		</SELECT>
			</td>
			<td class="label">Emp Name:</td>
			<td><input type="text" name="empName" onkeyup="return limitlength(this, 20)">&nbsp;</td>&nbsp;
				<td class="label">
						PFID:
					</td>
					<td><input type="text" name="pfId" onkeyup="return limitlength(this, 20)">&nbsp;</td>&nbsp;	
			<td><input type="button" class="btn" value="Search" class="btn" onclick="testSS();"></td>
               </tr>
			</table>
	         <div id="suggestions"></div>
			<%PensionBean dbBeans = new PensionBean();		
            if (request.getAttribute("MappedList") != null) {
                ArrayList datalist = new ArrayList();  
                 ArrayList emplist = new ArrayList();             
                int totalData = 0;
                datalist = (ArrayList) request.getAttribute("MappedList");
                emplist = (ArrayList) request.getAttribute("EmpInfoList");
                System.out.println("emplist----- " + emplist.size());
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
						<td class="tblabel">
								Serial No
							</td>
							<td class="tblabel">
								CPF NO
							</td>
							<td class="tblabel">
								Region
							</td>
							
							<td class="tblabel">
								Pension Number&nbsp;&nbsp;
							</td>
							<td class="tblabel">
								Emp Name&nbsp;&nbsp;
							</td>
							<td class="tblabel">
								D.O.B
							</td>
							<td class="tblabel">
								Pension Option
							</td>							
							<td>
								<img src="./PensionView/images/page_edit.png" alt="edit" />
								&nbsp;

							</td>
						</tr>
						<%}%>
						<%int count = 0;
                String airportCode = "", employeeName = "", cpfacno = "", pensionNumber = "";
                String dateofBirth = "", empSerialNo = "", dateofJoining = "",pensionOption="",pcreportverified="";             
                for (int i = 0; i < datalist.size(); i++) {
                                    count++;
                    PensionBean beans = (PensionBean) datalist.get(i);                    
         if(emplist.size()==0)
                {                	
                	 dateofJoining = "";
                }else{
                EmpMasterBean bean =(EmpMasterBean) emplist.get(i);
                 dateofJoining = bean.getDateofJoining();
                  pcreportverified = bean.getPcverified();
                }
                    cpfacno = beans.getCpfAcNo();
                    employeeName = beans.getEmployeeName();
                    pensionNumber = beans.getPensionnumber();
                    dateofBirth = beans.getDateofBirth();
                    region = beans.getRegion();
                    empSerialNo = beans.getEmpSerialNumber();
                    pensionOption = beans.getPensionOption();
                   
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
							<td class="Data" width="15%">
								<%=region%>
							</td>
							<td class="Data" width="12%">
								<%=empSerialNo%>
							</td>
							

							<td class="Data" width="17%">
								<%=employeeName%>
							</td>
							<td class="Data" width="17%">
								<%=dateofBirth%>
							</td>
							<td class="Data">
								<%=pensionOption%>
							</td>                            
							<td>
								<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="javascript:editPensionMaster('<%=cpfacno%>','<%=region%>','<%=pensionNumber%>','<%=employeeName%>','<%=dateofBirth%>','<%=empSerialNo%>','<%=dateofJoining%>','<%=pcreportverified%>')" />
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
