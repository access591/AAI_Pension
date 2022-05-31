
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*,java.text.DecimalFormat"%>
<%@ page import="aims.common.CommonUtil"%>

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
	   CommonUtil commonUtil=new CommonUtil();
		String region="",fileHead="",reportType="",fileName="",dispRemtSelYears="";
		String emoluments="",pension="",disPension="",pensionHding="",ranges="",displyyears="";
		  DecimalFormat df1 = new DecimalFormat("#########0.00");
		  ArrayList pensionList=new ArrayList();
		 	if (request.getAttribute("range") != null) {
			ranges=(String)request.getAttribute("range");
			}
			if (request.getAttribute("region") != null) {
			region=(String)request.getAttribute("region");
			if(region.equals("NO-SELECT")){
			fileHead="ALL_REGIONS";
			}else{
			fileHead=region;
			}
		}
		if(region.equals("NO-SELECT") && !ranges.equals("")){
			fileHead="PFID_WISE_"+ranges;
		}
		if (request.getAttribute("dspRemtYears") != null) {
			dispRemtSelYears=(String)request.getAttribute("dspRemtYears");
		}
		
		
		if (request.getAttribute("pensionHding") != null) {
			pensionHding=(String)request.getAttribute("pensionHding");
		}
		
		if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					fileName = "Form_8_Report[REV]_"+dispRemtSelYears+"_"+fileHead+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
	    ArrayList form8allYearsList=new ArrayList();
	    int totalYearssize=0;
	    double yearTotalEmoluments=0.0,yearTotalPension=0.0;
		if(request.getAttribute("form8List")!=null){
		Form7MultipleYearBean multipleBean=null;
			ArrayList dataList=new ArrayList();
			ArrayList form8List=new ArrayList();
			form8List=(ArrayList)request.getAttribute("form8List");
			totalYearssize=form8List.size();
			
			
						int count=0;
						double totalEmoluments=0.0,totalPension=0.0,dispTotalPension=0.0;
						Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri= new Long(0);;
						String obFlag="",calYear="",adjYear="",finalRemarks="",remarks="---";
						ArrayList obList=new ArrayList();
						boolean regionFlag=false;
						String dispRegion="";
						double arrearEmoluemntsAmount=0.00,arrearContriAmount=0.00;
     					boolean arrearflags=false;
						double grandTotalPensionContr=0,grandTotalEmoluments=0,totalAprContribution=0.0,totalMayContribution=0.0;
						double totalJunContribution=0.0,totalJulContribution=0.0,totalAugContribution=0.0,totalSepContribution=0.0,totalOctContribution=0.0,totalNovContribution=0.0,totalDecContribution=0.0,totalJanContribution=0.0,totalFebContribution=0.0,totalMarContribution=0.0;
						double totalAprAdjobContribution=0.0,grantTotalRemtPension=0.0;
						double previsedEmoluments=0.00,previsedPensioContri=0.00,dueEmoluemnts=0.00,dueContri=0.00;
						double previsedTotalEmoluments=0.00,previsedTotalPensioContri=0.00,dueTotalEmoluemnts=0.00,dueTotalContri=0.00;
						Form8DataBean personalInfo=new Form8DataBean();
						Form8RemittanceBean remittanceBean=new Form8RemittanceBean();
						String employeename="",pensionno="";
						System.out.println("form8List List Size=============="+form8List.size());
						
	%>
	
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td width="9%">
					&nbsp;
				</td>
				<td width="15%">
					&nbsp;
				</td>
				<td width="12%">
					&nbsp;
				</td>
				<td width="33%">
					&nbsp;
				</td>
				<td width="13%">
					&nbsp;
				</td>
				<td width="9%">
					&nbsp;
				</td>
				<td width="9%">
					&nbsp;
				</td>
			</tr>	
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				
							<td colspan="2" align="center" class="reportsublabel">FORM-8 (PS)</td>
						
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
						<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				
							<td colspan="2" align="center" class="reportsublabel">(For Exempted Establishments only)</td>
						
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				
							<td colspan="2" align="center" class="reportsublabel">THE EMPLOYEES PENSION SCHEME, 1995</td>
						
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>

			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td colspan="3" class="reportlabel" align="center">
					Annual Statement Of Contribution For The Currency
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="13%">
								&nbsp;
							</td>
							<td width="87%" class="reportsublabel" align="center">
								<font style="text-decoration: underline">Period From <%=request.getAttribute("dspYear")%></font>
							</td>
						</tr>
					</table>
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			<tr>
				<td colspan="4">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="36%" class="label">
								Name &amp; Address of the Establishment
							</td>
							<td width="64%">
								Airports Authority Of India,
								<br />
								Rajiv Gandhi Bhawan,Safdarjung Airport,New Delhi-3
							</td>
						</tr>
					</table>
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			<tr>
				<td colspan="4">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="36%" class="label">
								Code No. of the Establishment
							</td>
							<td width="64%">
								DL/36478
							</td>
						</tr>
					</table>
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			<tr>
				<td colspan="7">
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						
						<%
						
					    for(int j=0;j<form8List.size();j++){
						    
							personalInfo=(Form8DataBean)form8List.get(j);
							dataList=personalInfo.getForm8List();
							remittanceBean=personalInfo.getRemittanceBean(); 
							
							totalAprContribution=totalAprContribution+remittanceBean.getTotalAprContribution()+remittanceBean.getTotalAprDueContribution();
							totalMayContribution=totalMayContribution+remittanceBean.getTotalMayContribution()+remittanceBean.getTotalMayDueContribution();
							totalJunContribution=totalJunContribution+remittanceBean.getTotalJunContribution()+remittanceBean.getTotalJunDueContribution();
							totalJulContribution=totalJulContribution+remittanceBean.getTotalJulContribution()+remittanceBean.getTotalJulDueContribution();
							totalAugContribution=totalAugContribution+remittanceBean.getTotalAugContribution()+remittanceBean.getTotalAugDueContribution();
							totalSepContribution=totalSepContribution+remittanceBean.getTotalSepContribution()+remittanceBean.getTotalSepDueContribution();
							totalOctContribution=totalOctContribution+remittanceBean.getTotalOctContribution()+remittanceBean.getTotalOctDueContribution();
							totalNovContribution=totalNovContribution+remittanceBean.getTotalNovContribution()+remittanceBean.getTotalNovDueContribution();
							totalDecContribution=totalDecContribution+remittanceBean.getTotalDecContribution()+remittanceBean.getTotalDecDueContribution();
							totalJanContribution=totalJanContribution+remittanceBean.getTotalJanContribution()+remittanceBean.getTotalJanDueContribution();
							totalFebContribution=totalFebContribution+remittanceBean.getTotalFebContribution()+remittanceBean.getTotalFebDueContribution();
							totalMarContribution=totalMarContribution+remittanceBean.getTotalMarContribution()+remittanceBean.getTotalMarDueContribution();
							
							
							grantTotalRemtPension=totalAprContribution+totalMayContribution+totalJunContribution+totalJulContribution+totalAugContribution+totalSepContribution+totalOctContribution+totalNovContribution+totalDecContribution+totalJanContribution+totalFebContribution+totalMarContribution;
								
							System.out.println("Data List Size=============="+dataList.size());
							EmployeePensionCardInfo cardInfo=new EmployeePensionCardInfo();
							
							for(int r=0;r<dataList.size();r++){
							cardInfo=(EmployeePensionCardInfo)dataList.get(r);
									obFlag=cardInfo.getObFlag();
  								if(obFlag.equals("Y")){
  								      obList=cardInfo.getObList();
  								      if(obList.size()>3){
             							adjYear="Apr-"+(String)obList.get(4);
							             cpfAdjOB=(Long)obList.get(5);
							           	 pensionAdjOB=(Long)obList.get(6);
							             pfAdjOB=(Long)obList.get(7);
							             empSubOB=(Long)obList.get(8);
							             adjPensionContri=(Long)obList.get(9);
							             if(adjPensionContri==null){
							             adjPensionContri = new Long(0);
							             }
             						}
          
  								}
							dispRegion=cardInfo.getRegion();
							previsedEmoluments=previsedEmoluments+Math.round(Double.parseDouble(cardInfo.getEmoluments()));
							dueEmoluemnts=dueEmoluemnts+Math.round(Double.parseDouble(cardInfo.getDueemoluments()));
							previsedPensioContri=previsedPensioContri+Math.round(Double.parseDouble(cardInfo.getPensionContribution()));
							dueContri=dueContri+Math.round(Double.parseDouble(cardInfo.getDuepensionamount()));
							totalEmoluments=totalEmoluments+Math.round(Double.parseDouble(cardInfo.getEmoluments()))+Math.round(Double.parseDouble(cardInfo.getDueemoluments()));
							totalPension=totalPension+Math.round(Double.parseDouble(cardInfo.getPensionContribution()))+Math.round(Double.parseDouble(cardInfo.getDuepensionamount()));
							if(cardInfo.getTransArrearFlag().equals("Y")){
    								arrearEmoluemntsAmount=arrearEmoluemntsAmount + Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmount=arrearContriAmount + Double.parseDouble(cardInfo.getOringalArrearContri());
     								arrearflags=true;
     								//finalRemarks=arrearEmoluemntsAmount+","+arrearContriAmount;
    						  }	
							System.out.println("Pensionno"+personalInfo.getPensionNo()+"OrignalEmoluments======"+Double.parseDouble(cardInfo.getEmoluments())+"Emolumeents"+Double.parseDouble(cardInfo.getDueemoluments()));
							}
							
							System.out.println("Pensionno"+personalInfo.getPensionNo()+"totalEmoluments======"+totalEmoluments+"arrearEmoluemntsAmount"+arrearEmoluemntsAmount+"arrearflags"+arrearflags);
							if(arrearflags==true){
							finalRemarks=arrearEmoluemntsAmount+","+arrearContriAmount;
					      		totalEmoluments=totalEmoluments-arrearEmoluemntsAmount;
					      		dueEmoluemnts=dueEmoluemnts-arrearEmoluemntsAmount;
					      		dueContri=dueContri-arrearContriAmount;
					    		totalPension=totalPension-arrearContriAmount;
					    		remarks=finalRemarks+"<br/>"+personalInfo.getRemarks();
					    	
      							finalRemarks="";
      						}else{
      						remarks=personalInfo.getRemarks();
      						}
      						if(remarks.equals("")){
      						remarks="---";
      						}
      						arrearEmoluemntsAmount=0.00;arrearContriAmount=0.00;
							arrearflags=false;
							if(adjPensionContri!=null){
									totalPension=totalPension+adjPensionContri.longValue();
									totalEmoluments=totalEmoluments+Math.round(adjPensionContri.longValue()*100/8.33);
									grantTotalRemtPension=grantTotalRemtPension+adjPensionContri.longValue();
									
							}
							previsedTotalEmoluments=previsedTotalEmoluments+previsedEmoluments;
							previsedTotalPensioContri=previsedTotalPensioContri+previsedPensioContri;
							dueTotalEmoluemnts=dueTotalEmoluemnts+dueEmoluemnts;
							dueTotalContri=dueTotalContri+dueContri;
							grandTotalEmoluments=grandTotalEmoluments+totalEmoluments;
							grandTotalPensionContr=grandTotalPensionContr+totalPension;
						
					
							
						%>
						<%if(regionFlag==false){%>
						<tr>
							<td class="label" colspan=11><font color=blue>Name Of Region :&nbsp;<%=dispRegion%></font></td>
							
						</tr>
						<tr>
							<td width="6%" rowspan="2" class="reportsublabel">
								Sl. No.
							</td>
						
							<td width="10%" rowspan="2" class="reportsublabel">
								Account No.
							</td>
						
							<td width="22%" rowspan="2" class="reportsublabel">
								Name of Member
								<br>
								(in block letters)</span>
							</td>
								<td width="12%" rowspan="2" class="reportsublabel">
								Date Of Birth
							</td>
							<td width="17%" colspan="3"  class="reportsublabel">
								Wages, retaining allowance,if any &amp; DA paid during the currency period
							</td>
							<%if(pensionHding.equals("true")){%>
							<td width="11%" colspan="3"  class="reportsublabel">
								Contribution to Pension Fund 8.33% / 2*1.16%
							</td>
							<%}else{%>
								<td width="11%" colspan="3"  class="reportsublabel">
								Contribution to Pension Fund 8.33% 
							</td>
							<%}%>
							
						

							<td width="10%" rowspan="2" class="reportsublabel">
								Remarks
							</td>
							
						</tr>
						      <tr>
      	 	<td   align="center" class="label">Pre-revised </td>
      	  	<td   align="center" class="label">Arrears </td>
      	   	<td   align="center" class="label">Total </td>
      	   	<td   align="center" class="label">Pre-revised </td>
      	  	<td   align="center" class="label">Arrears </td>
      	  	<td   align="center" class="label">Total </td>
      </tr>
      <tr>
        <td class="label" width="6%" align="center">1</td>
        <td class="label" width="10%" align="center">2</td>
        <td class="label" width="23%" align="center">3</td>
        <td class="label" width="14%" align="center">4</td>
        <td class="label" width="10%"  align="center">5</td>
        <td class="label" width="10%" align="center">6</td>
        <td class="label" width="10%" align="center">7</td>
        <td class="label" width="10%"  align="center">8</td>
        <td class="label" width="10%"  align="center">9</td>
        <td class="label" width="10%"  align="center">10</td>
         <td class="label" width="10%"  align="center">11</td>
      </tr>
						<%regionFlag=true;}
						
							count++;
						%>
						<tr>
							<td class="Data"><%=count%></td>
							<td class="Data"><%="DL/36478/"+commonUtil.leadingZeros(5,personalInfo.getPensionNo())%></td>
						
							<td class="Data"><%=personalInfo.getEmployeeName()%></td>
						<td class="Data"><%=personalInfo.getDateofbirth()%></td>
							<td class="NumData"><%=Math.round(previsedEmoluments)%></td>
							<td class="NumData"><%=Math.round(dueEmoluemnts)%></td>
							<td class="NumData"><%=Math.round(totalEmoluments)%></td>
							<td class="NumData"><%=Math.round(previsedPensioContri)%></td>
							<td class="NumData"><%=Math.round(dueContri)%></td>
							<td class="NumData"><%=Math.round(totalPension)%></td>
							
							<td class="Data"><%=remarks%></td>
						</tr>
									<% previsedEmoluments=0.0;previsedPensioContri=0.0;dueEmoluemnts=0.0;dueContri=0.0;%>
						<%
						yearTotalEmoluments=yearTotalEmoluments+Math.round(totalEmoluments);
           				 yearTotalPension=yearTotalPension+adjPensionContri.longValue()+Math.round(totalPension);
						totalEmoluments=0;totalPension=0;}%>
						<tr>
							<td class="NumData"><%=count%></td>
							<td class="Data" colspan="3">&nbsp;</td>
						
							<td class="NumData"><%=Math.round(previsedTotalEmoluments)%></td>
							<td class="NumData"><%=Math.round(dueTotalEmoluemnts)%></td>
							<td class="NumData"><%=Math.round(grandTotalEmoluments)%></td>
							<td class="NumData"><%=Math.round(previsedTotalPensioContri)%></td>
							<td class="NumData"><%=Math.round(dueTotalContri)%></td>
							<td class="NumData"><%=Math.round(grandTotalPensionContr)%></td>
							
							<td class="Data" >&nbsp;</td>
						
							
						</tr>
					</table>
				</td>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>

			<tr>
				<td colspan="7">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<span class="reportsublabel">Reconciliation of Remittances as </span>____________________ <span class="reportsublabel">Total</span>________________ <span class="reportsublabel">Rs</span><%=Math.round(grandTotalPensionContr)%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<!--  New Code   For displaying monthly wise pension Data-->
			<tr>
				<td colspan="7">
					<table width="100%" border="1" cellspacing="0" cellpadding="0">
						<tr>
							<td width="12%" class="label">
								Sl. No.
							</td>
						
							<td width="27%" class="label">
								Month Pension Fund Contribution A/c No. 10
							</td>
							<td width="10%" class="label">
								Cheque No.
							</td>
							<td width="10%" class="label">
								Cheque Date
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
						
							<td class="Data">
								1
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalAprContribution)%> (<%=dispRemtSelYears%>)for All members for month of April 
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						
							<td rowspan="13" valign="top">
								<span class="Data">Certified that the difference between the figures of total Pension Fund Contributions remitted during the currency period and the total Pension Fund Contribution shown under Column (5) is solely due to the rounding of amounts to the nearest Rupee
								under the rules.</span>
								<br/>
								<span class="Data">(i) Total number of contribution cards enclosed in Form 7 (PS). (ii) Certified that the Form 7 (PS) completed of all the members listed in this statement are enclosed, except those already sent during the course of the currency
									period for the final settlement of the concerned members account &quot;Remarks&quot; furnished against the names of the respective members 1 above</span>.
							</td>
						</tr>
						<%if (totalAprAdjobContribution!=0.00){%>
						<tr>
						
							<td class="Data">
								1.1
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalAprAdjobContribution)%> (<%=dispRemtSelYears%>)Adj.ob for All members for month of April 
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						
							<td rowspan="13" valign="top">
								<span class="Data">Certified that the difference between the figures of total Pension Fund Contributions remitted during the currency period and the total Pension Fund Contribution shown under Column (5) is solely due to the rounding of amounts to the nearest Rupee
								under the rules.</span>
								<br/>
								<span class="Data">(i) Total number of contribution cards enclosed in Form 7 (PS). (ii) Certified that the Form 7 (PS) completed of all the members listed in this statement are enclosed, except those already sent during the course of the currency
									period for the final settlement of the concerned members account &quot;Remarks&quot; furnished against the names of the respective members 1 above</span>.
							</td>
						</tr>
						<%}%>
						<tr>
							<td class="Data">
								2
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalMayContribution)%> (<%=dispRemtSelYears%>)for All members for month of May
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						
						</tr>
						<tr >
							<td class="Data">
								3
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalJunContribution)%> (<%=dispRemtSelYears%>)for All members for month of June 
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								4
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalJulContribution)%> (<%=dispRemtSelYears%>)for All members for month of July
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								5
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalAugContribution)%> (<%=dispRemtSelYears%>)for All members for month of August 
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="Data">
								6
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalSepContribution)%> (<%=dispRemtSelYears%>)for All members for month of September
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								7
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalOctContribution)%> (<%=dispRemtSelYears%>)for All members for month of October
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								8
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalNovContribution)%> (<%=dispRemtSelYears%>)for All members for month of November
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td  class="Data">
								9
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalDecContribution)%> (<%=dispRemtSelYears%>)for All members for month of December
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								10
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalJanContribution)%> (<%=dispRemtSelYears%>)for All members for month of January
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								11
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalFebContribution)%> (<%=dispRemtSelYears%>)for All members for month of February
							</td>
							<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								12
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=Math.round(totalMarContribution)%>(<%=dispRemtSelYears%>)for All members for month of March
							</td>
								<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr >
							<td class="Data">
								Total
							</td>
							<td class="Data" nowrap="nowrap">
								Rs.<%=df1.format(grantTotalRemtPension)%>
							</td>
								<td>
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>  
			<tr>
				<td width="9%">
					&nbsp;
				</td>
				<td width="15%">
					&nbsp;
				</td>
				<td width="12%">
					&nbsp;
				</td>
				<td width="33%">
					&nbsp;
				</td>
				<td width="13%">
					&nbsp;
				</td>
				<td width="9%">
					&nbsp;
				</td>
				<td width="9%">
					&nbsp;
				</td>
			</tr>   
			<!-- -->
				<tr>
				<td class="label" align="right" colspan=7>&nbsp;</td>
							
			</tr>
			<tr>
				<td class="label" align="right" colspan=7>Signature of the Employer<br/>(With office seal)</td>
							
			</tr>
		</table>
		<%
		
		 grandTotalPensionContr=0;grandTotalEmoluments=0;totalAprContribution=0.0;totalMayContribution=0.0;
		
		totalJunContribution=0.0;totalJulContribution=0.0;totalAugContribution=0.0;totalSepContribution=0.0;totalOctContribution=0.0;totalNovContribution=0.0;totalDecContribution=0.0;totalJanContribution=0.0;totalFebContribution=0.0;totalMarContribution=0.0;
		grantTotalRemtPension=0.0;totalEmoluments=0.0;totalPension=0.0;dispTotalPension=0.0;
		
		%>
	
		<%}%>
		<br style='page-break-before:always;'>
							<table width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="gray">
					<tr>
					<td colspan="4" align="center" style="label">Total Years Grand Totals
					</td>
				<tr>
    
    <td class="label">Amount of wages, retaining allowance<br />
          if any &amp; DA paid during the month</td>
    <td class="label">Contribution to Pension Fund</td>
    
    
  </tr>
						      <tr>
   
    <td class="Data"><%=df1.format(yearTotalEmoluments)%></td>
    <td class="Data"><%=df1.format(yearTotalPension)%></td>
    
  </tr>
  </table>				
	</body>
</html>
