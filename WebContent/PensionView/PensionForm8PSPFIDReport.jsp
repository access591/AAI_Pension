
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
		String emoluments="",pension="",disPension="",pensionHding="";
		  DecimalFormat df1 = new DecimalFormat("#########0.00");
		  ArrayList pensionList=new ArrayList();
		if (request.getAttribute("region") != null) {
			region=(String)request.getAttribute("region");
			if(region.equals("NO-SELECT")){
			fileHead="ALL_REGIONS";
			}else{
			fileHead=region;
			}
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
				
					fileName = "Form_8_Report_"+fileHead+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
	    ArrayList form8allYearsList=new ArrayList();
	    int totalYearssize=0;
		if(request.getAttribute("form8List")!=null){
		Form7MultipleYearBean multipleBean=null;
			form8allYearsList=(ArrayList)request.getAttribute("form8List");
			totalYearssize=form8allYearsList.size();
			System.out.println("totalYearssize========================================"+totalYearssize);
			for(int getallYear=0;getallYear<form8allYearsList.size();getallYear++){
			multipleBean=(Form7MultipleYearBean)form8allYearsList.get(getallYear);
			
	%>
		<%
						ArrayList dataList=new ArrayList();
						int count=0;
							double totalEmoluments=0.0,totalPension=0.0,dispTotalPension=0.0;
						EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
						boolean regionFlag=false;
						String dispRegion="",finalRemarks="",remarks="",arrearMonth="";
						String arrearMonthApr="";
						String arrearMonthMay="";
						String arrearMonthJun="";
						String arrearMonthJul="";
						String arrearMonthAug="";
						String arrearMonthSep="";
						String arrearMonthOct="";
						String arrearMonthNov="";
						String arrearMonthDec="";
						String arrearMonthJan="";
						String arrearMonthFeb="";
						String arrearMonthMar="";
						
						Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null;
						String obFlag="",calYear="",adjYear="";
						double arrearEmoluemntsAmount=0.00,arrearContriAmount=0.00;
						
						double arrearEmoluemntsAmountApr=0.00,arrearContriAmountApr=0.00;
						double arrearEmoluemntsAmountMay=0.00,arrearContriAmountMay=0.00;
						double arrearEmoluemntsAmountJun=0.00,arrearContriAmountJun=0.00;
						double arrearEmoluemntsAmountJul=0.00,arrearContriAmountJul=0.00;
						double arrearEmoluemntsAmountAug=0.00,arrearContriAmountAug=0.00;
						double arrearEmoluemntsAmountSep=0.00,arrearContriAmountSep=0.00;
						double arrearEmoluemntsAmountOct=0.00,arrearContriAmountOct=0.00;
						double arrearEmoluemntsAmountNov=0.00,arrearContriAmountNov=0.00;
						double arrearEmoluemntsAmountDec=0.00,arrearContriAmountDec=0.00;
						double arrearEmoluemntsAmountJan=0.00,arrearContriAmountJan=0.00;
						double arrearEmoluemntsAmountFeb=0.00,arrearContriAmountFeb=0.00;
						double arrearEmoluemntsAmountMar=0.00,arrearContriAmountMar=0.00;
     					boolean arrearflags=false;
						ArrayList obList=new ArrayList();
						double grandTotalPensionContr=0,grandTotalEmoluments=0,totalAprContribution=0.0,totalMayContribution=0.0;
						double totalJunContribution=0.0,totalJulContribution=0.0,totalAugContribution=0.0,totalSepContribution=0.0,totalOctContribution=0.0,totalNovContribution=0.0,totalDecContribution=0.0,totalJanContribution=0.0,totalFebContribution=0.0,totalMarContribution=0.0;
						double totalAprAdjobContribution=0.0,grantTotalRemtPension=0.0;
						Form8Bean form8Bean=new Form8Bean();
						Form8RemittanceBean remittanceBean=new Form8RemittanceBean();
						ArrayList form8List=new ArrayList();
						
							form8List=multipleBean.getEachYearList();
						    for(int j=0;j<form8List.size();j++){
						    
							form8Bean=(Form8Bean)form8List.get(j);
							dataList=form8Bean.getForm8List();
							remittanceBean=form8Bean.getRemittanceBean();
								
							totalAprContribution=totalAprContribution+remittanceBean.getTotalAprContribution();
							totalMayContribution=totalMayContribution+remittanceBean.getTotalMayContribution();
							totalJunContribution=totalJunContribution+remittanceBean.getTotalJunContribution();
							totalJulContribution=totalJulContribution+remittanceBean.getTotalJulContribution();
							totalAugContribution=totalAugContribution+remittanceBean.getTotalAugContribution();
							totalSepContribution=totalSepContribution+remittanceBean.getTotalSepContribution();
							totalOctContribution=totalOctContribution+remittanceBean.getTotalOctContribution();
							totalNovContribution=totalNovContribution+remittanceBean.getTotalNovContribution();
							totalDecContribution=totalDecContribution+remittanceBean.getTotalDecContribution();
							totalJanContribution=totalJanContribution+remittanceBean.getTotalJanContribution();
							totalFebContribution=totalFebContribution+remittanceBean.getTotalFebContribution();
							totalMarContribution=totalMarContribution+remittanceBean.getTotalMarContribution();
							grantTotalRemtPension=totalAprContribution+totalMayContribution+totalJunContribution+totalJulContribution+totalAugContribution+totalSepContribution+totalOctContribution+totalNovContribution+totalDecContribution+totalJanContribution+totalFebContribution+totalMarContribution;
							
							for(int i=0;i<dataList.size();i++){
						
							personalInfo=(EmployeePersonalInfo)dataList.get(i);
							if(personalInfo!=null){
								
							pensionList=personalInfo.getPensionList();
							EmployeePensionCardInfo cardInfo=new EmployeePensionCardInfo();
							
							for(int r=0;r<pensionList.size();r++){
							cardInfo=(EmployeePensionCardInfo)pensionList.get(r);
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
             						}
          
  								}
  								//System.out.println("form-8================================================"+cardInfo.getTransArrearFlag());
  							 	if(cardInfo.getTransArrearFlag().equals("Y")){
    								System.out.println("form-8==============="+arrearContriAmountMay+"==============="+cardInfo.getOringalArrearContri()+"=================="+arrearContriAmountApr);
     								
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Apr")){
     								arrearMonthApr=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountApr=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountApr=Double.parseDouble(cardInfo.getOringalArrearContri());
    								//System.out.println("form-8========Apr========================================"+arrearContriAmountApr);
     								
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("May")){
     								arrearMonthMay=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountMay=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountMay=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Jun")){
     								arrearMonthJun=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountJun=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountJun=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Jul")){
     								arrearMonthJul=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountJul=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountJul=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Aug")){
     								arrearMonthAug=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountAug=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountAug=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Sep")){
     								arrearMonthSep=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountSep=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountSep=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Oct")){
     								arrearMonthOct=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountOct=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountOct=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Nov")){
     								arrearMonthNov=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountNov=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountNov=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Dec")){
     								arrearMonthDec=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountDec=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountDec=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Jan")){
     								arrearMonthJan=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountJan=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountJan=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Feb")){
     								arrearMonthFeb=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountFeb=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountFeb=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								if(commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM").equals("Mar")){
     								arrearMonthMar=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
     								arrearEmoluemntsAmountMar=Double.parseDouble(cardInfo.getOringalArrearAmnt());
    								arrearContriAmountMar=Double.parseDouble(cardInfo.getOringalArrearContri());
     								}
     								
     								
     								
     								arrearEmoluemntsAmount=arrearEmoluemntsAmountApr+arrearEmoluemntsAmountMay+arrearEmoluemntsAmountJun+arrearEmoluemntsAmountJul
     								+arrearEmoluemntsAmountAug+arrearEmoluemntsAmountSep+arrearEmoluemntsAmountOct+arrearEmoluemntsAmountNov+arrearEmoluemntsAmountDec
     								+arrearEmoluemntsAmountJan+arrearEmoluemntsAmountFeb+arrearEmoluemntsAmountMar;
    								arrearContriAmount=arrearContriAmountApr+arrearContriAmountMay+arrearContriAmountJun+arrearContriAmountJul+arrearContriAmountAug
    								+arrearContriAmountSep+arrearContriAmountOct+arrearContriAmountNov+arrearContriAmountDec+arrearContriAmountJan+arrearContriAmountFeb
    								+arrearContriAmountMar;
     								arrearflags=true;
     								//arrearMonth=commonUtil.converDBToAppFormat(cardInfo.getMonthyear(),"dd-MMM-yyyy","MMM-yyyy");
    								finalRemarks=arrearEmoluemntsAmount+","+arrearContriAmount;
    						  }
							dispRegion=cardInfo.getRegion();
							totalEmoluments=totalEmoluments+Math.round(Double.parseDouble(cardInfo.getEmoluments()));
							totalPension=totalPension+Math.round(Double.parseDouble(cardInfo.getPensionContribution()));
							
							}
						
				   			if(arrearflags==true){
				   			System.out.println("form-8=========================888=======88================"+remittanceBean.getNovMnth()+arrearMonthNov+arrearContriAmountNov);
					      		totalEmoluments=totalEmoluments-arrearEmoluemntsAmount;
					    		totalPension=totalPension-arrearContriAmount;
					    		
					    		if(remittanceBean.getAprMnth().equals(arrearMonthApr)){
					    			System.out.println("totalNovContribution:::apr"+totalNovContribution);
					    			totalAprContribution=totalAprContribution-arrearContriAmountApr;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountApr;
					    		} if(remittanceBean.getMayMnth().equals(arrearMonthMay)){
					    			System.out.println("totalNovContribution:::may"+totalNovContribution);
					    			totalMayContribution=totalMayContribution-arrearContriAmountMay;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountMay;
					    			
					    		} if(remittanceBean.getJunMnth().equals(arrearMonthJun)){
					    			System.out.println("totalNovContribution:::jun"+totalNovContribution);
					    			totalJunContribution=totalJunContribution-arrearContriAmountJun;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountJun;
					    		} if(remittanceBean.getJulMnth().equals(arrearMonthJul)){
					    			System.out.println("totalNovContribution:::jul"+totalNovContribution);
					    			totalJulContribution=totalJulContribution-arrearContriAmountJul;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountJul;
					    		} if(remittanceBean.getAugMnth().equals(arrearMonthAug)){
					    		System.out.println("totalNovContribution:::aug"+totalNovContribution);
					    			totalAugContribution=totalAugContribution-arrearContriAmountAug;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountAug;
					    		} if(remittanceBean.getSepMnth().equals(arrearMonthSep)){
					    		System.out.println("totalNovContribution:::sep"+totalNovContribution);
					    		
					    			totalSepContribution=totalSepContribution-arrearContriAmountSep;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountSep;
					    		} if(remittanceBean.getOctMnth().equals(arrearMonthOct)){
					    		System.out.println("totalNovContribution:::oct"+arrearMonthOct);
					    			totalOctContribution=totalOctContribution-arrearContriAmountOct;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountOct;
					    		} if(remittanceBean.getNovMnth().equals(arrearMonthNov)){
					    			totalNovContribution=totalNovContribution-arrearContriAmountNov;
					    			System.out.println("totalNovContribution:::nov"+totalNovContribution);
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountNov;
					    		} if(remittanceBean.getDecMnth().equals(arrearMonthDec)){
					    			totalDecContribution=totalDecContribution-arrearContriAmountDec;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountDec;
					    		} if(remittanceBean.getJanMnth().equals(arrearMonthJan)){
					    			totalJanContribution=totalJanContribution-arrearContriAmountJan;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountJan;
					    		} if(remittanceBean.getFebMnth().equals(arrearMonthFeb)){
					    			totalFebContribution=totalFebContribution-arrearContriAmountFeb;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountFeb;
					    		} if(remittanceBean.getMarMnth().equals(arrearMonthMar)){
					    			totalMarContribution=totalMarContribution-arrearContriAmountMar;
					    			grantTotalRemtPension=grantTotalRemtPension-arrearContriAmountMar;
					    		}
					    		System.out.println("totalNovContribution:::"+totalNovContribution+remittanceBean.getNovMnth().equals(arrearMonthNov));
      						}
      						//System.out.println("arrearMonth"+arrearMonth+"remittanceBean.getSepMnth()"+remittanceBean.getSepMnth()+"totalSepContribution"+totalSepContribution);
      						if(!finalRemarks.equals("")){
      							remarks=finalRemarks+"<br/>"+personalInfo.getRemarks();
      						}else{
      						remarks=personalInfo.getRemarks();
      						}
						if(adjPensionContri!=null){
									totalPension=totalPension+adjPensionContri.longValue();
									totalEmoluments=totalEmoluments+Math.round(adjPensionContri.longValue()*100/8.33);
									grantTotalRemtPension=grantTotalRemtPension+adjPensionContri.longValue();
									totalAprAdjobContribution=adjPensionContri.longValue();
							}
					
							
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
								<font style="text-decoration: underline">Period From <%=multipleBean.getMessage()%></font>
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
						
					
						<%if(regionFlag==false){%>
						<tr>
							<td class="label" colspan=8><font color=blue>Name Of Region :&nbsp;<%=dispRegion%></font></td>
							
						</tr>
						<tr>
							<td width="6%" class="reportsublabel">
								Sl. No.
							</td>
						
							<td width="17%" class="reportsublabel">
								Account No.
							</td>
						
							<td width="22%" class="reportsublabel">
								Name of Member
								<br>
								(in block letters)</span>
							</td>
							<td width="25%" class="reportsublabel">
								Wages, retaining allowance,if any &amp; DA paid during the currency period
							</td>
							<%if(pensionHding.equals("true")){%>
							<td width="11%" class="reportsublabel">
								Contribution to Pension Fund 8.33% / 2*1.16%
							</td>
							<%}else{%>
								<td width="11%" class="reportsublabel">
								Contribution to Pension Fund 8.33% 
							</td>
							<%}%>
							
						

							<td width="10%" class="reportsublabel">
								Remarks
							</td>
							
						</tr>
						<%regionFlag=true;}
						
							count++;
						%>
						<tr>
							<td class="Data"><%=count%></td>
							<td class="Data"><%="DL/36478/"+commonUtil.leadingZeros(5,personalInfo.getPensionNo())%></td>
						
							<td class="Data"><%=personalInfo.getEmployeeName()%></td>
							<td class="NumData"><%=Math.round(totalEmoluments)%></td>
							<td class="NumData"><%=Math.round(totalPension)%></td>
							
							<td class="Data"><%=remarks%></td>
						</tr>
						
					
						<tr>
							<td class="NumData"><%=count%></td>
							<td class="Data" colspan="2">&nbsp;</td>
							<td class="NumData"><%=Math.round(totalEmoluments)%></td>
							<td class="NumData"><%=Math.round(totalPension)%></td>
							
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
								<span class="reportsublabel">Reconciliation of Remittances as </span>____________________ <span class="reportsublabel">Total</span>________________ <span class="reportsublabel">Rs</span><%=Math.round(totalPension)%>
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
								Rs.<%=Math.round(totalAprContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of April 
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
								Rs.<%=Math.round(totalAprAdjobContribution)%> (<%=multipleBean.getDispFinYear()%>)Adj.ob for All members for month of April 
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
								Rs.<%=Math.round(totalMayContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of May
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
								Rs.<%=Math.round(totalJunContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of June 
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
								Rs.<%=Math.round(totalJulContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of July
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
								Rs.<%=Math.round(totalAugContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of August 
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
								Rs.<%=Math.round(totalSepContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of September
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
								Rs.<%=Math.round(totalOctContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of October
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
								Rs.<%=Math.round(totalNovContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of November
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
								Rs.<%=Math.round(totalDecContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of December
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
								Rs.<%=Math.round(totalJanContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of January
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
								Rs.<%=Math.round(totalFebContribution)%> (<%=multipleBean.getDispFinYear()%>)for All members for month of February
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
								Rs.<%=Math.round(totalMarContribution)%>(<%=multipleBean.getDispFinYear()%>)for All members for month of March
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
				<tr>
				<td class="label" align="right" colspan=7>&nbsp;</td>
							
			</tr>
			<tr>
				<td class="label" align="right" colspan=7>Signature of the Employer<br/>(With office seal)</td>
							
			</tr>
		</table>
			<%}}}%>
		 <%if(totalYearssize-1!=getallYear){%>
						<br style='page-break-after:always;'>
	<%}%>	
		<%}}%>
	</body>
</html>
