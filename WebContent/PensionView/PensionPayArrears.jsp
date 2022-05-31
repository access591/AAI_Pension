
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*,aims.common.CommonUtil,java.text.DecimalFormat"%>
<%@ page import="aims.common.CommonUtil" %>

<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
					String dispYear="";
					if(request.getAttribute("dspYear")!=null){
					dispYear=request.getAttribute("dspYear").toString();
					}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
	</head>

	<body>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

	
 	 <tr>
     <td colspan="3" class="reportlabel"><table width="50%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="10%" rowspan="3"><img src="<%=basePath%>PensionView/images/logoani.gif" width="87" height="50" align="right" /></td>
        <td width="50%" class="reportlabel"  align="center">Pension PayArrears For The Year <%=dispYear%></td>
      </tr>
       <td>&nbsp;</td>
      </tr>
    </table></td>
  	</tr>
	<%
	
		String region="",fileHead="",reportType="",fileName="";
		if (request.getAttribute("region") != null) {
			region=(String)request.getAttribute("region");
			if(region.equals("NO-SELECT")){
			fileHead="ALL_REGIONS";
			}else{
			fileHead=region;
			}
		}
		
		if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					fileName = "PayArrears_Report_"+fileHead+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}else if (reportType.equals("DBF")) {
			    	fileName = "PayArrears_Report_"+fileHead+".dbf";
					response.setContentType("application/x-wais-source");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
	
	%>
	
		<%	
		
		CommonUtil commonUtil=new CommonUtil();
					ArrayList dataList=new ArrayList();
						ArrayList pensionDataList=new ArrayList();
						boolean seperationFlag=false;
						int count=0,size=0;
						EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
						String remarks="",monthYear="",shnMonthYear="",pensionRemarks="",rstchkYear="",seperationMnth="";
						DecimalFormat df = new DecimalFormat("#########0");
						if(request.getAttribute("chkYear")!=null){
							rstchkYear=(String)request.getAttribute("chkYear");
						}else{
							rstchkYear="1995";
						}
						 
						double totalEmoluments=0.0,totalPension=0.0,dispTotalPension=0.0;
						if(request.getAttribute("form8List")!=null){
							dataList=(ArrayList)request.getAttribute("form8List");
							 size=dataList.size();
							  if(size!=0){
							
							for(int i=0;i<dataList.size();i++){
							count++;
							personalInfo=(EmployeePersonalInfo)dataList.get(i);
							if(personalInfo.getRemarks().equals("")){
								remarks="---";
							}else{
								remarks=personalInfo.getRemarks();
								pensionRemarks=remarks;
								remarks="";
								
							}
							//out.println("retirement date is "+ personalInfo.getDateOfAnnuation());
							System.out.println("seperation date is : "+personalInfo.getSeperationDate());
							if(!personalInfo.getSeperationDate().equals("---")){
								seperationMnth=commonUtil.converDBToAppFormat(personalInfo.getSeperationDate(),"dd-MMM-yyyy","MMM-yyyy");
							}else{
								seperationMnth="---";
							}
							
							System.out.println("EmployeeName"+personalInfo.getEmployeeName()+"Date Of Seperation"+personalInfo.getSeperationDate()+"pensionRemarks=========================="+pensionRemarks);							
							pensionDataList=personalInfo.getPensionList();
							if(pensionDataList.size()!=0){
							
							
			%>
		

   
  <tr>
    <td>&nbsp;</td>
    <td width="27%">&nbsp;</td>
   
  </tr>
   
  <tr>
    <td colspan="2"><table width="80%"   border="1"  cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label">1. EST_CODE.: </td>
         <td  class="Data">DL/36478</td>
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">EST_EXTN:</td>
        <td class="Data">&nbsp;</td>
        	
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">EMP_NO:</td>
        <td class="Data" align="left"><%=commonUtil.trailingZeros(personalInfo.getPfID().substring(personalInfo.getPfID().length()-5,personalInfo.getPfID().length()).toCharArray())%></td>
        </tr>
      <tr>
        <td class="label">MEM_DET:</td>
        <td class="Data"><%=personalInfo.getEmployeeName().toUpperCase()%></td>
     </tr>
       <tr>
        <td nowrap="nowrap" class="label">VOL_PEN:</td>
        <td class="Data"><%=personalInfo.getWetherOption()%></td>
       </tr>		
      <tr>
       <td nowrap="nowrap" class="label">VOLCONTDT:</td>
         <td class="label"><%=personalInfo.getDateOfJoining()%></td> 
      </tr>
      <%
    	EmployeePensionCardInfo cardInfo=new EmployeePensionCardInfo();
    	String form7NarrationRemarks="",finalRemarks="",pensioncontributionVal="";
    	int k=0;
    	for(int j=0;j<pensionDataList.size();j++){
    		k++;
    	cardInfo=(EmployeePensionCardInfo)pensionDataList.get(j);
    	
    	if(monthYear.equals("Apr-1995")){
     	   	 totalEmoluments=0;
     	}else{
     		 totalEmoluments=totalEmoluments+Math.round(Double.parseDouble(cardInfo.getEmoluments()));
     	}
 		 totalPension=totalPension+Math.round(Double.parseDouble(cardInfo.getPensionContribution()));
 		dispTotalPension=totalPension;
     
     }%>
      <tr>
       <td nowrap="nowrap" class="label">TOTWAGES:</td>
         <td class="label" align="right"><%=totalEmoluments%></td> 
      </tr>
     <tr>
       <td nowrap="nowrap" class="label">PFT:</td>
       <td class="label" align="right"><%=dispTotalPension%></td> 
      </tr>
     <tr>
       <td nowrap="nowrap" class="label">BST:</td>
       <td class="label">&nbsp;</td> 
      </tr>
     <%
    	 cardInfo=new EmployeePensionCardInfo();
       	 k=0;
    	for(int j=0;j<pensionDataList.size();j++){
    		k++;
    	cardInfo=(EmployeePensionCardInfo)pensionDataList.get(j);
    	   	
    %>
    <tr>
        <td class="label" width="13%">ACTWG<%=k%></td>
        <%if(monthYear.equals("Apr-1995")){%>
         <td class="Data" width="20%" align="right">0</td>
         <%}else{%>
        <td class="Data" width="20%" align="right"><%=Math.round(Double.parseDouble(cardInfo.getOriginalEmoluments()))%></td>
        <%}}%>
      </tr>
  <% k=0;
  for(int j=0;j<pensionDataList.size();j++){
	k++;
   cardInfo=(EmployeePensionCardInfo)pensionDataList.get(j);
  %>
 <tr>
 <td nowrap="nowrap" class="label">PENWG<%=k%>:</td>
        <%if(monthYear.equals("Apr-1995")){%>
         <td class="Data" width="20%" align="right">0</td>
         <%}else{%>
        <td class="Data" width="20%" align="right"><%=Math.round(Double.parseDouble(cardInfo.getEmoluments()))%></td>
        <%}}%>
 </tr>
<% k=0;
  for(int j=0;j<pensionDataList.size();j++){
	k++;%>
<tr><td nowrap="nowrap" class="label">PENDIFF<%=k%>:</td> <td class="Data">&nbsp;</td></tr>
<%}%>
<% k=0;
  for(int j=0;j<pensionDataList.size();j++){
	k++;
  cardInfo=(EmployeePensionCardInfo)pensionDataList.get(j);
  %>

<tr><td nowrap="nowrap" class="label">PF<%=k%>:</td>
        <%if(monthYear.equals("Apr-1995")){%>
         <td class="Data" width="20%" align="right">0</td>
         <%}else{%>
        <td class="Data" width="20%" align="right"><%=Math.round(Double.parseDouble(cardInfo.getPensionContribution()))%></td>
        <%}}%>
 </tr>
    </table></td>
    
  </tr>
    </table>
	
</td>
  </tr>
</table>
</td>
</tr>

</table>
  <%if(size-1!=i){%>
						<br style='page-break-after:always;'>
	<%}%>	
<%totalEmoluments=0.0;totalPension=0.0;seperationFlag=false;remarks="";}%>
						
						<%}}}%>
	</body>
</html>
