
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*,aims.common.CommonUtil,java.text.DecimalFormat"%>
<%@ page import="aims.common.CommonUtil" %>

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
				
					fileName = "Form_7_8(PS)_Summary_Report_"+fileHead+".xls";
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
						boolean seperationFlag=false;
						int count=0,size=0;
						EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
						String remarks="",monthYear="",shnMonthYear="",pensionRemarks="",rstchkYear="",seperationMnth="";
						DecimalFormat df = new DecimalFormat("#########0.00");
						if(request.getAttribute("chkYear")!=null){
							rstchkYear=(String)request.getAttribute("chkYear");
						}else{
							rstchkYear="1995";
						}
						Form78SummaryReport form78Bean=null;
						
						ArrayList form78List=new ArrayList();
						if(request.getAttribute("form8List")!=null){

						form78List=(ArrayList)request.getAttribute("form8List");
												
						
						
							
							
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
        <td width="80%" class="reportlabel"  align="center">&nbsp;</td>
      </tr>
      <tr>
        <td nowrap="nowrap" align="center" class="reportsublabel">(FOR EXEMPTED ESTABLISHMENT ONLY)</td>
      </tr>
      <tr>
        <td nowrap="nowrap" align="center" class="reportsublabel">THE EMPLOYEES' PENSION SCHEME, 1995</td>
      </tr>
 
    </table></td>
  </tr>
     <tr>
        <td colspan="4"  nowrap="nowrap" align="center" class="reportsublabel">Yearwise Comparative statement</td>
      </tr>
    <tr>
    <td>&nbsp;</td>
	
    <td colspan="3"  align="center" class="reportsublabel">CONTRIBUTION CARDS FOR MEMBERS FOR THE YEAR <%=dispYear%></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td width="27%">&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>

  <tr>
    <td colspan="4"><table width="100%" border="1" bordercolor="gray" cellpadding="2" cellspacing="0">
      <tr>
        <td  rowspan="2" align="center" class="label">Srl.No</td>
        <td  rowspan="2" align="center" class="label">Pension No</td>
       	<td  rowspan="2" align="center" class="label">Employee Name</td>
        <td  rowspan="2" align="center" class="label">Date Of Birth</td>
        <td  rowspan="2" align="center" class="label">Date Of Joining</td>
        <td colspan="2" align="center" class="label">Form 7(ps) </td> 
        <td colspan="2" align="center" class="label">Form 8(ps) </td> 
      </tr>
      <tr>
        <td  align="center" class="label">Emoluments</td>
        <td align="center" class="label">Pension Cont.</td>
        <td  align="center" class="label">Emoluments</td>
        <td  align="center" class="label">Pension Cont.</td>
        
      </tr>
      <tr>
        <td  class="label" align="center">1</td>
        <td  class="label" align="center">2</td>
        <td  class="label" align="center">3</td>
        <td  class="label" align="center">4</td>
        <td  class="label" align="center">5</td>
        <td  class="label" align="center">6</td>
        <td  class="label" align="center">7</td>
        <td  class="label" align="center">8</td>
        <td  class="label" align="center">9</td>
      </tr>
   
  
    <%
     	double form7emoluments=0.00,form7Pension=0.00,form8emoluments=0.00,form8Pension=0.00;
     	double totalform7emoluments=0.00,totalform7Pension=0.00,totalform8emoluments=0.00,totalform8Pension=0.00;
    	for(int j=0;j<form78List.size();j++){
    	form78Bean=(Form78SummaryReport)form78List.get(j);
    	
		
    	String[] form7Translist=form78Bean.getForm7TransInfo().split("-");
    	String[] form8Translist=form78Bean.getForm8TransInfo().split("-");
    	if(form7Translist.length==2){
    		form7emoluments=Double.parseDouble(form7Translist[0]);
    		form7Pension=Double.parseDouble(form7Translist[1]);
    	}else{
    		form7emoluments=0.00;
    		form7Pension=0.00;
    	}
    	if(form8Translist.length==2){
    		form8emoluments=Double.parseDouble(form8Translist[0]);
    		form8Pension=Double.parseDouble(form8Translist[1]);
    	}else{
    		form8emoluments=0.00;
    		form8Pension=0.00;
    	}
    	totalform7emoluments=totalform7emoluments+form7emoluments;
    	totalform7Pension=totalform7Pension+form7Pension;
    	totalform8emoluments=totalform8emoluments+form8emoluments;
    	totalform8Pension=totalform8Pension+form8Pension;
    	count++;
    %>

     <tr>
     	<td class="Data" align="center"><%=count%></td>
        <td class="Data" align="center"><%=form78Bean.getPensionno()%></td>
        <td class="Data" align="center"><%=form78Bean.getEmployeename()%></td>
        <td class="Data" align="center"><%=form78Bean.getDateofbirth()%></td>
        <td class="Data" align="center"><%=form78Bean.getDateofjoining()%></td>
        <td class="Data" align="center"><%=form7emoluments%></td>
        <td class="Data" align="center"><%=form7Pension%></td>
        <td class="Data" align="center"><%=form8emoluments%></td>
        <td class="Data" align="center"><%=form8Pension%></td>
      </tr>
     
   <%}%>
     <tr>
     	<td class="Data" align="center" colspan="5">&nbsp;</td>

        <td class="Data" align="center"><%=df.format(totalform7emoluments)%></td>
        <td class="Data" align="center"><%=df.format(totalform7Pension)%></td>
        <td class="Data" align="center"><%=df.format(totalform8emoluments)%></td>
        <td class="Data" align="center"><%=df.format(totalform8Pension)%></td>
      </tr>
   <tr bordercolor="white">
    
    <td colspan="9">&nbsp;</td>
    
  </tr>
    <tr bordercolor="white">
    <td class="Data">Dated :</td>
    <td colspan="7">&nbsp;</td>
    <td align="right" class="Data" nowrap="nowrap">Signature of the employer<br/>with office seal</td>
  </tr>
  <%}%>
   </table>
	
</td>
  </tr>
</table>
</td>
</tr>

</table>
		
						
						
	</body>
</html>
