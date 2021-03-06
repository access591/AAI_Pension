<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*,aims.common.CommonUtil,java.text.DecimalFormat"%>
<%@ page import="aims.common.CommonUtil" %>

<%@ page buffer="64kb"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
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
				
					fileName = "CPFDAta_Report_"+fileHead+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}    					
    	   		
	%>
	
		<%
		String dispYear="";
		CommonUtil commonUtil=new CommonUtil();
		if(request.getAttribute("dspYear")!=null){
		dispYear=request.getAttribute("dspYear").toString();
		}
						ArrayList dataList=new ArrayList();
						ArrayList pensionDataList=new ArrayList();
						int count=0,size=0;
						EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
						DecimalFormat df = new DecimalFormat("#########0");
						DecimalFormat df1 = new DecimalFormat("#########0.00");					
						Form7MultipleYearBean form7MulBean=null;
						String arrearBreakUpData="";
						double totalDueEmoluments=0.00,totalDueArrears=0.00;
						ArrayList allYearForm8List=new ArrayList();
						if(request.getAttribute("form8List")!=null){
						allYearForm8List=(ArrayList)request.getAttribute("form8List");							
					for(int cntYear=0;cntYear<allYearForm8List.size();cntYear++){
						form7MulBean=(Form7MultipleYearBean)allYearForm8List.get(cntYear);
						dataList=form7MulBean.getEachYearList();
								dispYear=form7MulBean.getMessage();
							  if(dataList.size()!=0){
						
							for(int i=0;i<dataList.size();i++){
							
							personalInfo=(EmployeePersonalInfo)dataList.get(i);
							
							
							
							pensionDataList=personalInfo.getPensionList();
							
							
							
							if(pensionDataList.size()!=0){
							
							
			%>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
			<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">

	
  <tr>
    <td width="16%">&nbsp;</td>
    <td colspan="3" class="reportlabel"><table width="70%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="20%" rowspan="3"><img src="<%=basePath%>PensionView/images/logoani.gif" width="87" height="50" align="right" /></td>
        <td width="80%" class="reportlabel"  align="center">CPF DATA </td>
      </tr>
      <tr>
        <td nowrap="nowrap" align="center" class="reportsublabel">(FOR EXEMPTED ESTABLISHMENT ONLY)</td>
      </tr>
      <tr>
        <td nowrap="nowrap" align="center" class="reportsublabel">THE EMPLOYEES' PENSION SCHEME, 1995</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table></td>
  </tr>


  <tr>
    <td>&nbsp;</td>
    <td width="27%">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="4"><table width="100%" border="2" bordercolor="gray" cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label" colspan="1" >1. Name of employee: </td>
        <td  class="Data" colspan="3" ><%=personalInfo.getEmployeeName().toUpperCase()%></td>
       	<td  class="label" colspan="1" >2. Designation: </td>
        <td  class="Data" colspan="3" ><%=personalInfo.getDesignation()%></td>
        <td  class="label" colspan="1">3. DOJ: </td>
        <td  class="Data" colspan="3"><%=personalInfo.getDateOfJoining()%></td>
      </tr>
      <tr>        
        <td  class="label" colspan="1">4. EPS Account No.: </td>
        <td  class="Data" colspan="3">DL/36478/<%=personalInfo.getPensionNo()%></td>
        <td  class="label" colspan="1">5. CPF Acc.No(Old): </td>
        <td  class="Data" colspan="3"><%=personalInfo.getCpfAccno()%></td>
        <td  class="label" colspan="1">6. DOE(If Any): </td>
        <td  class="Data" colspan="3"><%=personalInfo.getDateOfSaparation()%></td>
      </tr><tr>
       
      </tr>
    </table></td>
 
   
  </tr>
  <tr>
    <td colspan="4"><table width="100%" border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
      <tr>
        <td width="10%" align="center" class="label">Month</td>
        <td width="12%" align="center" class="label">BASIC PAY + DA </td>
        <td width="12%" align="center" class="label">CPF CONTRIBUTION</td>
        <td width="12%" align="center" class="label"> INTEREST</td>
           <td width="15%" align="center" class="label">EpfoPension</td>
        <td width="15%" align="center" class="label">EpfoIntrest</td>
        <td width="12%" align="center" class="label">Additional </td>
       	<td width="12%" align="center" class="label">NetEPf </td>       
        
     
        
      </tr>
      
      <tr>
        <td class="label" width="12%" align="center">GPF Share given by Govt.</td>
        <td class="label" width="12%" align="center"> - </td>
        <td class="label" width="12%" align="center"> - </td>
        <td class="label" width="12%" align="center"> - </td>
        <td class="label" width="12%"  align="center"> - </td>
        <td class="label" width="12%"  align="center"> - </td>
        <td class="label" width="12%"  align="center"> - </td>
        <td class="label" width="12%"  align="center"> - </td>
      </tr>
   
  
    <%
     	Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null;
     	double arrearEmoluemntsAmount=0.00,arrearContriAmount=0.00;
     	boolean arrearflags=false;
		ArrayList obList=new ArrayList();
  		String obFlag="",calYear="";
    	CadPensionInfo cardInfo=new CadPensionInfo();
    	String form7NarrationRemarks="",finalRemarks="";
    	for(int j=0;j<pensionDataList.size();j++){
    	cardInfo=(CadPensionInfo)pensionDataList.get(j);
    %>

      <tr>
        <td class="Data" width="12%" align="center" ><%=cardInfo.getYear()%></td>
        <td class="Data" width="12%" align="right"><%=cardInfo.getEmoluments()%></td>
        <td class="Data" width="12%" align="right"><%=cardInfo.getCONTRIBUTION()%></td>
        <td class="Data" width="12%" align="right"><%=cardInfo.getCPFIntrest()%></td>  
         <td class="Data" width="12%" align="right"><%=cardInfo.getEpfoPension()%></td>
         <td class="Data" width="12%" align="right"><%=cardInfo.getEpfoIntrest()%></td> 
        <td class="Data" width="12%" align="right"><%=cardInfo.getAdditional()%></td>
        <td class="Data" width="12%" align="right"><%=cardInfo.getNetEPf()%></td> 
        
      </tr>
      <%
            }      
      %>
   

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
<%
}%>
				
						<%}}}%>
						
							<%}%>
						
					
						<%%>
	</body>
</html>
