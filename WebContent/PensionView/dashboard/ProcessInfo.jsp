
<%@ page language="java" import="java.util.*,aims.common.*,aims.bean.FinalSettlementBean"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.PensionBean"%>

<%String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
            String region = "",chkpfid="";
            String totalResults="";
            CommonUtil common = new CommonUtil();
            HashMap hashmap = new HashMap();            
            hashmap = common.getRegion();
            String fndRegion="";
			if(request.getAttribute("fndregion")!=null){
				fndRegion=(String)request.getAttribute("fndregion");
			}else{
				fndRegion="";
			}
			String finallist = "";  
			if(request.getAttribute("finyearList")!=null){
				finallist=(String)request.getAttribute("finyearList");
			}			
			if(request.getAttribute("chkpfid")!=null){
				chkpfid=(String)request.getAttribute("chkpfid");
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
			var results=new Array();  
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
  					alert("Please Enter Employee Name or PFID");
		 			document.forms[0].empName.focus();
	  				return false;
		  	  	}  	 
		   		var empName=document.forms[0].empName.value;
		   		var pfId = document.forms[0].pfId.value;
		   		var url ="<%=basePath%>search1?method=getEmployeeMappedList&region="+region1+"&empName="+empName+"&pfId="+pfId+"&pagename=processInfo";   		   		
		   		statusdisplay();       
			    document.forms[0].action=url;
			    document.forms[0].method="post";
				document.forms[0].submit();
			}	
			function editPensionMaster(cpfaccno,region,pensionNumber,employeeName,desig,dateofbirth,dateofJoining,airportCode,fathername,employeeCode,gender,pensionOption){		
				window.opener.test(cpfaccno,region,pensionNumber,employeeName,desig,dateofbirth,dateofJoining,airportCode,fathername,employeeCode,gender,pensionOption);	
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

							<%}else{

            %>
           
            <option value="<%=fndRegion%>">
								<%=fndRegion%>
							</option>
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
					</td>&nbsp;
				<td class="label">
						PFID:
					</td>
					<td>
						<input type="text" name="pfId" onkeyup="return limitlength(this, 20)">
						&nbsp;
					</td>&nbsp;	
					
					
										
			<td>		<input type="button" class="btn" value="Search" class="btn" onclick="return testSS();">
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
						<td class="tblabel">
								Serial No
							</td>
							<td class="tblabel">
								CPF NO
							</td>
							<td class="tblabel">
								Pension No
							</td>
							<td class="tblabel">
								Region
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
                String airportCode = "", employeeName = "", cpfacno = "", pensionNumber = "",desig="",fathername="",employeeCode="",gender="",pensionOption="";
                String dateofBirth = "", empSerialNo = "", dateofJoining = "";
                String finalsetdate= "", resetdate = "", intcalcdate="", reintcalcdate="",remarks="";
                String seperationDate="",seperationReason="",settlementClient="";
                for (int i = 0; i < datalist.size(); i++) {
                    count++;
                    PensionBean beans = (PensionBean) datalist.get(i);
                    cpfacno = beans.getCpfAcNo();
                    employeeName = beans.getEmployeeName();
                    desig= beans.getDesegnation();                    
                    pensionNumber = beans.getEmpSerialNumber();
                    dateofBirth = beans.getDateofBirth();
                    dateofJoining = beans.getDateofJoining();
                    region = beans.getRegion();                    
                    pensionOption = beans.getPensionOption();
                    airportCode=beans.getAirportCode();
                    finalsetdate=beans.getFinalSettlementDate();
                    resetdate=beans.getReSettlementDate();
                    intcalcdate=beans.getIntrestCalcDate();
                    reintcalcdate=beans.getReIntrestCalcDate();
                    seperationDate=beans.getSeperationDate();
                    seperationReason=beans.getSeperationReason();
                    remarks=beans.getRemarks();
                    fathername=beans.getFHName();
                    employeeCode=beans.getEmployeeCode();
                    gender=beans.getSex();
                    settlementClient=beans.getSettlementClient(); 
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
								<%=pensionNumber%>
							</td>
							<td class="Data" width="15%">
								<%=region%>
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
								<input type="checkbox" name="cpfno" value="<%=cpfacno%>" onclick="editPensionMaster('<%=cpfacno%>','<%=region%>','<%=pensionNumber%>','<%=employeeName%>','<%=desig%>','<%=dateofBirth%>','<%=dateofJoining%>','<%=airportCode%>','<%=fathername%>','<%=employeeCode%>','<%=gender%>','<%=pensionOption%>');" />
							</td>
						</tr>
						<%}%>
						<%}%>
					</table>
				</td>
			</tr>
			</form>
	</body>
</html>
