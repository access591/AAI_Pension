<%@ page import="java.util.*,java.lang.*"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="aims.common.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.dao.*"%>
<%@ page import="aims.action.cashbook.Employee" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ArrayList a = new ArrayList();
	String color = "yellow";
	String noofMonths="",arrearFlag="";
	String recoverieTable="";
	 String adjOBYear="1995-2008"; 
%>
<%!ArrayList blockList = new ArrayList();
	String breakYear = "";%>
<%
	FinancialReportDAO financialReportDAO = new FinancialReportDAO();
	ArrayList PensionContributionList = new ArrayList();
	ArrayList pensionList = new ArrayList();
	CommonUtil commonUtil = new CommonUtil();
	String fullWthrOptionDesc = "", genderDescr = "", mStatusDec = "";
	String employeeNm = "", pensionNo = "", doj = "", dob = "", cpfacno = "", employeeNO = "", designation = "", fhName = "", gender = "", fileName = "";
	String reportType = "", whetherOption = "", dateOfEntitle = "", empSerialNo = "", mStatus = "", region1 = "", cpfaccno1 = "";
	String  cpfTotal= "",PenContriTotal= "",PFTotal="",emolumentsTotal="",empSubTotal = "",	AAIContriTotal = "";
	 
	double totalEmoluments = 0.0, pfStaturary = 0.0, totalPension = 0.0, empVpf = 0.0, principle = 0.0, interest = 0.0, pfContribution = 0.0;
	double grandEmoluments = 0.0, grandCPF = 0.0, grandPension = 0.0, grandPFContribution = 0.0;
	double cpfInterest = 0.0, pensionInterest = 0.0, pfContributionInterest = 0.0,empSubInterest = 0.0, aaiContriInterest = 0.0;
	double grandCPFInterest = 0.0, grandPensionInterest = 0.0, grandPFContributionInterest = 0.0;
	double cumPFStatury = 0.0, cumPension = 0.0, cumPfContribution = 0.0;
	double cpfOpeningBalance = 0.0, penOpeningBalance = 0.0, pfOpeningBalance = 0.0, empSubOpeningBalance = 0.0, aaiContriOpeningBalance =0.0; 
	double percentage = 0.0,advanceAmt =0.00, pfwSubAmt =0.00,pfwContriAmt = 0.00, subTotal=0.00,empSubscri=0.00,aaiContri = 0.00,pf = 0.00,cumempSubscri = 0.00, cumAAiContri=0.00, aaiContriTot =0.00,  empSubscriTot=0.00;
	double  grandEmpSub =0.00,grandEmpSubInterest =0.00,grandAAIContri=0.00,grandAAIContriInterest=0.00;
	if (request.getAttribute("reportType") != null) {
		reportType = (String) request.getAttribute("reportType");
		if (reportType.equals("Excel Sheet")
				|| reportType.equals("ExcelSheet")) {
			fileName = "Pension_Contribution_report.xls";
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment; filename=" + fileName);
		}
	}
	PensionContributionList = (ArrayList) request
			.getAttribute("penContrList");
	 
	String cntFlag = "";
	int size = 0;
	size = PensionContributionList.size();
	for (int i = 0; i < PensionContributionList.size(); i++) {
		PensionContBean contr = (PensionContBean) PensionContributionList
				.get(i);
		employeeNm = contr.getEmployeeNM();
		pensionNo = contr.getPensionNo();
		empSerialNo = contr.getEmpSerialNo();
		doj = contr.getEmpDOJ();
		dob = contr.getEmpDOB();
		cpfacno = StringUtility.replaces(
				contr.getCpfacno().toCharArray(), ",=".toCharArray(),
				",").toString();

		if (cpfacno.indexOf(",=") != -1) {
			cpfacno = cpfacno.substring(1, cpfacno.indexOf(",="));
		} else if (cpfacno.indexOf(",") != -1) {
			cpfacno = cpfacno.substring(cpfacno.indexOf(",") + 1,
					cpfacno.length());
		}
		whetherOption = contr.getWhetherOption();
		if (whetherOption.toUpperCase().trim().equals("A")) {
			fullWthrOptionDesc = "Full Pay";
		} else if (whetherOption.toUpperCase().trim().equals("B")
				|| whetherOption.toUpperCase().trim().equals("NO")) {
			fullWthrOptionDesc = "Ceiling Pay";
		} else {
			fullWthrOptionDesc = whetherOption;
		}
		employeeNO = contr.getEmployeeNO();
		designation = contr.getDesignation();
		fhName = contr.getFhName();
		gender = contr.getGender();
		region1 = contr.getEmpRegion();
		cpfaccno1 = contr.getEmpCpfaccno();
	    
		String discipline = contr.getDepartment();
		String finalsettlmentdate="";
		finalsettlmentdate=contr.getFinalSettlementDate();
		String interestdate=contr.getInterestCalUpto();
		String dateofseperationDt=contr.getDateofSeperationDt();
	
		finalsettlmentdate.replaceAll("-","/");
		if (gender.trim().toLowerCase().equals("m")) {
			genderDescr = "Male";
		} else if (gender.trim().toLowerCase().equals("f")) {
			genderDescr = "Female";
		} else {
			genderDescr = gender;
		}
		mStatus = contr.getMaritalStatus().trim();

		if (mStatus.toLowerCase().equals("m")
				|| (mStatus.toLowerCase().trim().equals("yes"))) {
			mStatusDec = "Married";
		} else if (mStatus.toLowerCase().equals("u")
				|| (mStatus.toLowerCase().trim().equals("no"))) {
			mStatusDec = "Un-married";
		} else if (mStatus.toLowerCase().equals("w")) {
			mStatusDec = "Widow";
		} else {
			mStatusDec = mStatus;
		}
		dateOfEntitle = contr.getDateOfEntitle();
		cntFlag = contr.getCountFlag();
		pensionList = contr.getEmpPensionList();
		blockList = contr.getBlockList();
		String userId = session.getAttribute("userid").toString();
		
	
		
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<style>
.black_overlay {
	display: none;
	position: fixed;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 3000px;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity = 80);
}

.white_content {
	display: none;
	position: fixed;
	top: 25%;
	left: 25%;
	width: 50%;
	height: 50%;
	padding: 16px;
	border: 16px solid orange;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
</style>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css"
	type="text/css">
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css"
	type="text/css">
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>

<script type="text/javascript">
 
 
    </script>
</head>

<body   onload="frmload();">


<form >
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	 
	<tr>
		<td>&nbsp;</td>
	</tr>
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tr>
			<td align="center" colspan="5">
			<table border=0 cellpadding=3 cellspacing=0 width="100%"
				align="center" valign="middle">
				<tr>
					<td align="center" colspan="5" >
						<table border=0 cellpadding=3 cellspacing=0 width="40%" align="center" valign="middle">
							<tr>
								<td>
									<img src="<%=basePath%>PensionView/images/logoani.gif" />
								</td>
								<td class="label" align="center" valign="top" nowrap="nowrap">
									<font color='black' size='4' face='Helvetica'> AIRPORTS AUTHORITY OF INDIA </font>
								</td>

							</tr>
						
						</table>
					</td>
				</tr>
				</table>
			</td>
			</tr>
				<tr>
               <%
               String heading="EDITED PC REPORT";
                 %>
                
					<td>&nbsp;&nbsp;</td>
					 
					  <td   class="reportlabel" style="text-decoration: underline" align="center"><%=heading%></td>
					 	 
					 	<td align="right">
					 
				</tr>
				<tr>
				<td>&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>

		<%!String getBlockYear(String year) {
		String bYear = "";

		for (int by = 0; by < blockList.size(); by++) {
		
			bYear = (String) blockList.get(by);
			String[] bDate = bYear.split(",");

			if (year.equals(bDate[1])) {
				breakYear = bDate[1];
				breakYear = bYear;
				break;
			} else {
				breakYear = "03-96";
			}
		}
		//	System.out.println("breakYear"+breakYear);
		//blockList.remove(breakYear);
		return breakYear;
	}%>
		<tr>
			<td>
			<table border="1" style="border-color: gray" cellpadding="2"
				cellspacing="0" width="100%" align="center">
				<tr>

					<td class="reportsublabel">PF ID</td>
					<td class="reportdata"><%=pensionNo%></td>
					<input type="hidden" name="pfid" value="<%=empSerialNo%>"/>
					<td class="reportsublabel">NAME</td>
					<td class="reportdata"><%=employeeNm%></td>

				</tr>
				<tr>
					<td class="reportsublabel">EMP NO</td>
					<td class="reportdata"><%=employeeNO%></td>
					<td class="reportsublabel">DESIGNATION</td>
					<td class="reportdata"><%=designation%></td>


				</tr>
				<tr>
					<td class="reportsublabel">CPF NO</td>
					<td class="reportdata"><%=cpfacno%></td>
					<td class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
					<td class="reportdata"><%=fhName%></td>


				</tr>
				<tr>
					<td class="reportsublabel">DATE OF BIRTH</td>
					<td class="reportdata"><%=dob%></td>
					<td class="reportsublabel">GENDER</td>
					<td class="reportdata"><%=genderDescr%></td>

				</tr>
				<tr>
					<td class="reportsublabel">DATE OF JOINING</td>
					<td class="reportdata"><%=doj%></td>
					<td class="reportsublabel">DISCIPLINE</td>
					<td class="reportdata"><%=discipline%></td>

				</tr>
				<tr>
					<td class="reportsublabel">DATE OF MEMBERSHIP</td>
					<td class="reportdata"><%=dateOfEntitle%></td>
					<td class="reportsublabel">PENSION OPTION</td>
					<td class="reportdata"><%=fullWthrOptionDesc%></td>

				</tr>
 
			</table>
			</td>
		</tr>

		<%
			if (pensionList.size() != 0) {
		%>

<tr> 
		<td >
		  
	 
		<table border="1" style="border-color: gray;" cellpadding="2"
			cellspacing="0" width="100%" align="center">

			<tr>
				<td class="label" width="10%" align="center">Month</td>
				<td class="label" width="10%" align="center">Emolument</td>
				<td class="label" width="8%" align="center">EPF</td>
				<td class="label" width="8%" align="center">VPF</td>
				<td class="label" width="8%" align="center">PRINCIPLE</td>
				<td class="label" width="8%" align="center">INTEREST</td>				
				<td class="label" width="8%" align="center">Advances</td>
				<td class="label" width="5%" align="center">PFW SUB_AMT</td>
				<td class="label" width="8%" align="center">Total</td>
				
			 	<td class="label" width="8%" align="center">PF</td>
				<td class="label" width="5%" align="center">PFW CONT_AMT</td>
			 	<td class="label" width="8%" align="center">Total</td>		
				<td class="label" width="10%" align="center">Pension
				Contribution<br></br>(1.16%X2)&8.33%</td>
				
			
				<td class="label" width="8%" align="center">Station</td>				
               
				 			 
				<td class="label" width="10%" align="center">Remarks</td>
			</tr>
			<%
				
						
						boolean openFlag = false;
						int count = 0;
						int chkMnths = 0;
						boolean flag = false;
						String findMnt = "";
						int countMnts = 0;
						DecimalFormat df = new DecimalFormat("#########0");
						String dispFromYear = "", dispToYear = "", totalYear = "";
						boolean dispYearFlag = false;
						double rateOfInterest = 0;
						String monthInfo = "", getMnthYear = "";

						for (int j = 0; j < pensionList.size(); j++) {
							TempPensionTransBean bean = (TempPensionTransBean) pensionList
									.get(j);
							if (bean != null) {
								String dateMontyYear = bean.getMonthyear();
                                
								if (dispYearFlag == false) {
									if (dispFromYear.equals("")) {
										dispFromYear = commonUtil
												.converDBToAppFormat(dateMontyYear,
														"dd-MMM-yyyy", "yy");
									}

									getMnthYear = commonUtil.converDBToAppFormat(
											dateMontyYear, "dd-MMM-yyyy", "MM-yy");

									String monthInterestInfo = getBlockYear(getMnthYear);
									String[] monthInterestList = monthInterestInfo
											.split(",");
									if (monthInterestList.length == 2) {
										monthInfo = monthInterestList[1];

										rateOfInterest = new Double(
												monthInterestList[0]).doubleValue();
									}

									dispYearFlag = true;

									breakYear = "";
								}

								String monthYear = bean.getMonthyear().substring(
										dateMontyYear.indexOf("-") + 1,
										dateMontyYear.length());
								findMnt = commonUtil.converDBToAppFormat(bean
										.getMonthyear(), "dd-MMM-yyyy", "MM-yy");

								if (findMnt.equals(monthInfo)) {
									flag = true;

									breakYear = "";
								}

								count++;
  
								totalEmoluments = new Double(df
										.format(totalEmoluments
												+ Math.round(Double
														.parseDouble(bean
																.getEmoluments()))))
										.doubleValue();
								pfStaturary = new Double(df.format(pfStaturary
										+ Math.round(Double.parseDouble(bean
												.getCpf())))).doubleValue();
								cumPFStatury = cumPFStatury + pfStaturary;
								empVpf = new Double(df.format(empVpf
										+ Math.round(Double.parseDouble(bean
												.getEmpVPF())))).doubleValue();
								principle = new Double(df.format(principle
										+ Math.round(Double.parseDouble(bean
												.getEmpAdvRec())))).doubleValue();
								interest = new Double(df.format(interest
										+ Math.round(Double.parseDouble(bean
												.getEmpInrstRec())))).doubleValue();
								totalPension = new Double(df.format(totalPension
										+ Math.round(Double.parseDouble(bean
												.getPensionContr()))))
										.doubleValue();
									 
 
							pf =Math.round(Double.parseDouble(bean.getAaiPFCont()));
							advanceAmt = Math.round(Double.parseDouble(bean.getAdvAmount()));							
							pfwSubAmt =	Math.round(Double.parseDouble(bean.getEmployeeLoan()));
							pfwContriAmt =  Math.round(Double.parseDouble(bean.getAaiLoan()));
							//System.out.println("----bean.getEmpVPF()-----"+bean.getEmpVPF());						 
								
							subTotal = Math.round(Double.parseDouble(bean.getCpf()))+Math.round(Double.parseDouble(bean.getEmpVPF()))+
										Math.round(Double.parseDouble(bean.getEmpAdvRec()))+Math.round(Double.parseDouble(bean.getEmpInrstRec()));
							//System.out.println("-------	subTotal----"+subTotal);
								
							empSubscri = subTotal -(advanceAmt+pfwSubAmt);
							//System.out.println("-------	empSubscri----"+empSubscri);	
							empSubscriTot = empSubscriTot + empSubscri;
							cumempSubscri = cumempSubscri + empSubscriTot;
							
							aaiContri = pf - pfwContriAmt; 
							aaiContriTot = aaiContriTot + aaiContri;
							cumAAiContri= cumAAiContri + aaiContriTot;  
							
								cumPension = cumPension + totalPension;
								pfContribution = new Double(df
										.format(pfContribution
												+ Math.round(Double
														.parseDouble(bean
																.getAaiPFCont()))))
										.doubleValue();
								cumPfContribution = cumPfContribution
										+ pfContribution;
								
								//System.out.println(bean.getPensionContr()	+ "==========" + bean.getDbPensionCtr());
			%>

			<%
				  //System.out.println(bean.getRecordCount());
						 
								if(bean.getRecordCount().equals("Single")){
								%>
							<% if(!bean.getEditedDate().trim().equals("")){%>
         				
         				
          			<tr bgcolor="orange" >
 			 <%
 			 }else{ %>
  			<tr bgcolor="white">
			  <%} %>
		  
				<td class="Data" width="6%" align="center"><%=monthYear%></td>
				<td class="Data" width="6%" align="right"> 
				<%=Math.round(Double.parseDouble(bean.getEmoluments()))%>
				</td>				
                <td class="Data" width="6%" align="right"> 
                <%=Math.round(Double.parseDouble(bean.getCpf()))%>
                </td>
				<td class="Data" width="6%" align="right">
				 <%=Math.round(Double.parseDouble(bean.getEmpVPF()))%> 
				</td> 
				<td class="Data" width="6%" align="right">
				<%=Math.round(Double.parseDouble(bean.getEmpAdvRec()))%>
			    </td>
				<td class="Data" width="6%" align="right"> 
				<%=Math.round(Double.parseDouble(bean.getEmpInrstRec()))%> 
				</td>
				<td class="Data" width="6%" align="right"> <%=Math.round(Double.parseDouble(bean
												.getAdvAmount()))%> </td>
				<td class="Data" width="6%" align="right"> <%=Math.round(Double.parseDouble(bean
												.getEmployeeLoan()))%> </td>
				<td class="Data" width="6%" align="right"> <%=empSubscri%> </td>
				<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean
												.getAaiPFCont()))%></td>
				 									
				<td class="Data" width="6%" align="right"> 
				<%=Math.round(Double.parseDouble(bean.getAaiLoan()))%>
			    </td>
				<td class="Data" width="6%" align="right"> <%=aaiContri%> </td> 
				 <td class="Data" width="6%" align="right">
				 <%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%>
				 </td> 
			<td class="Data" nowrap="nowrap"><%=bean.getStation()%></td>
			 <td class="Data" width="6%" align="right"><%=bean.getForm7Narration()%></td>
			</tr>
			<%
				} else if (bean.getRecordCount()
										.equals("Duplicate")) {
			%>
			<tr bgcolor="yellow">
				<td class="Data" width="10%" align="center"><font color="red"><%=monthYear%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getEmoluments()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getCpf()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getEmpVPF()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getPensionContr()))%></font></td>
				<td class="Data" width="10%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean
												.getAaiPFCont()))%></font></td>
				<td class="Data" width="10%"><font color="red"><%=bean.getStation()%></font></td>
				<td class="Data" width="10%"><font color="red"> <input
					type="button" name="E" value="E" onclick="editEmoluments() " /></font></td>
				<td class="Data" width="12%"><input type="checkbox"
					name="cpfno"
					value="'<%=bean.getMonthyear()%>','<%=bean.getTransCpfaccno()%>','<%=bean.getRegion()%>','<%=bean.getStation()%>'" />

				</td>
			</tr>

			<%
				}
			%>
			<%
				if (flag == true) {
									dispToYear = commonUtil.converDBToAppFormat(
											dateMontyYear, "dd-MMM-yyyy", "yy");
									if (dispFromYear.equals(dispToYear)) {
										if (dispFromYear.equals("00")) {
											dispFromYear = "99";
										}

										if (dispFromYear.trim().length() < 2) {
											dispFromYear = "0" + dispFromYear;
										}
										dispToYear = Integer.toString(Integer
												.parseInt(dispToYear) + 1);
										if (dispToYear.trim().length() < 2) {
											dispToYear = "0" + dispToYear;
										}
									}
									totalYear = dispFromYear + "-" + dispToYear;

									dispFromYear = "";
			%>
			<tr>
				<td class="HighlightData" align="center">Total <%=totalYear%></td>
				<td class="HighlightData" align="right"><%=df.format(totalEmoluments)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfStaturary)%></td>
				<td class="HighlightData" align="right"><%=df.format(empVpf)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubscriTot)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContribution)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriTot)%></td>
				<td class="HighlightData" align="right"><%=df.format(totalPension)%></td>
				
				 
		 
			</tr>
			<tr>
				<%
					cpfInterest = Math.round((cumPFStatury
												* rateOfInterest / 100) / 12)
												+ Math.round(cpfOpeningBalance
														* rateOfInterest / 100);
										pensionInterest = Math.round((cumPension
												* rateOfInterest / 100) / 12)
												+ Math.round(penOpeningBalance
														* rateOfInterest / 100);
										pfContributionInterest = Math
												.round((cumPfContribution
														* rateOfInterest / 100) / 12)
												+ Math.round(pfOpeningBalance
														* rateOfInterest / 100);
										empSubInterest = Math
												.round((cumempSubscri
														* rateOfInterest / 100) / 12)
												+ Math.round(empSubOpeningBalance
														* rateOfInterest / 100);
										aaiContriInterest = Math
												.round((cumAAiContri
														* rateOfInterest / 100) / 12)
												+ Math.round(aaiContriOpeningBalance
														* rateOfInterest / 100);
														
									 
									 
							  
				%>   

				<td class="HighlightData" align="center">Interest(<%=rateOfInterest%>%)</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfInterest)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfContributionInterest)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriInterest)%></td>
				<td class="HighlightData" align="right"><%=df.format(pensionInterest)%></td>
			</tr>
			<tr>
				<%
					flag = false;
										openFlag = true;
										cpfOpeningBalance = Math.round(pfStaturary
												+ cpfInterest
												+ Math.round(cpfOpeningBalance));
										penOpeningBalance = Math.round(totalPension
												+ pensionInterest
												+ Math.round(penOpeningBalance));
										pfOpeningBalance = Math.round(pfContribution
												+ pfContributionInterest
												+ Math.round(pfOpeningBalance));
										empSubOpeningBalance= Math.round(empSubscriTot
												+ empSubInterest
												+ Math.round(empSubOpeningBalance));
										aaiContriOpeningBalance= Math.round(aaiContriTot
												+ aaiContriInterest
												+ Math.round(aaiContriOpeningBalance));
													%>

				<td class="HighlightData" align="center">CL BAL</td>
				<td class="HighlightData">---</td>
				<td class="HighlightData" align="right"><%=df.format(cpfOpeningBalance)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>				 
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(empSubOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(pfOpeningBalance)%></td>
				<td class="HighlightData" align="right">---</td>
				<td class="HighlightData" align="right"><%=df.format(aaiContriOpeningBalance)%></td>
				<td class="HighlightData" align="right"><%=df.format(penOpeningBalance)%></td>
				 


			</tr>
			<% 
				grandEmoluments = grandEmoluments
											+ totalEmoluments;
									grandCPF = grandCPF + pfStaturary;
									grandPension = grandPension + totalPension;
									grandPFContribution = grandPFContribution
											+ pfContribution;

									grandCPFInterest = grandCPFInterest
											+ cpfInterest;
									grandPensionInterest = grandPensionInterest
											+ pensionInterest;
									grandPFContributionInterest = grandPFContributionInterest
											+ pfContributionInterest;
											
									grandEmpSub = grandEmpSub + empSubscriTot;									
									grandEmpSubInterest = grandEmpSubInterest + empSubInterest;
									
									grandAAIContri = grandAAIContri + aaiContriTot;
									grandAAIContriInterest = grandAAIContriInterest + aaiContriInterest;
									
									cumPFStatury = 0.0;
									cumPension = 0.0;
									cumPfContribution = 0.0;
									cumAAiContri = 0.0;
									totalEmoluments = 0;
									pfStaturary = 0;
									totalPension = 0;
									pfContribution = 0;
									cpfInterest = 0;
									pensionInterest = 0;
									pfContributionInterest = 0;
									empSubscriTot = 0;
									empSubInterest = 0;
									aaiContriTot = 0;
									aaiContriInterest = 0;
								}
			%>
			<%
				dispYearFlag = false;
							}
						}
					
				emolumentsTotal = df.format(grandEmoluments);
				cpfTotal = df.format(grandCPF + grandCPFInterest);
				PenContriTotal = df.format(grandPension + grandPensionInterest);
				PFTotal = df.format(grandPFContribution + grandPFContributionInterest);
				empSubTotal = df.format(grandEmpSub + grandEmpSubInterest);
				AAIContriTotal = df.format(grandAAIContri + grandAAIContriInterest);
				
			%> 
  <input type="hidden" name="status" >


		</table>
	</td>
	</tr>
	 <tr><td>&nbsp;</td></tr>
	<tr>
	 <td>
				<table align="center" width="100%" cellpadding="0" cellspacing="0"
					border="1" bordercolor="gray">

					<tr>
						<td class="HighlightData"></td>
						<td class="HighlightData">Emolument</td>
						<td class="HighlightData">CPF</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">Pension Contribution</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">PF Contribution</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">Emp Subscription</td>
						<td class="HighlightData">Interest</td>
						<td class="HighlightData">AAI Contribution</td>
						<td class="HighlightData">Interest</td>
						 
					</tr>
					<tr>
						<td class="HighlightData" align="left">Grand Total of <%=count%>
						months</td>
						<td class="HighlightData"></td>
						<td class="HighlightData" align="right"><%=df.format(grandCPF)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandCPFInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPension)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPensionInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPFContribution)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandPFContributionInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandEmpSub)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandEmpSubInterest)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandAAIContri)%></td>
						<td class="HighlightData" align="right"><%=df.format(grandAAIContriInterest)%></td>
						</tr>
					<tr>
						<td class="HighlightData" align="center">Grand Total</td>
						<td class="HighlightData" align="right"><%=df.format(grandEmoluments)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandCPF + grandCPFInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPension + grandPensionInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPFContribution
									+ grandPFContributionInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandEmpSub+ grandEmpSubInterest)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(grandAAIContri+ grandAAIContriInterest)%></td>
						
					</tr>
					 <% ArrayList finalizedTotals = new ArrayList();
					 ArrayList prevGrandTotalsList  = new ArrayList();
					 double emolumentsTot_prev =0.00,cpfTot_prev =0.00,PenscontriTot_Prev = 0.00,PfTot_prev =0.00,empSubTot_prev=0.00,AAiContriTot_Prev=0.00;
					 double emolumentsTot_diff =0.00,cpfTot_diff =0.00,PenscontriTot_diff = 0.00,PfTot_diff =0.00,empSubTot_diff=0.00,AAiContriTot_diff=0.00;;
					 double emolumentsTot =0.00,empSubTot =0.00,empSubIntrst=0.00,AAiContriTot=0.0,aaiContriIntrst=0.00,PenscontriTot=0.0,PensionIntrst=0.00;
					
					 String transid="";
					
					 int result =0; 
					   if(request.getAttribute("prevGrandTotalsList")!=null){
					   prevGrandTotalsList = (ArrayList) request.getAttribute("prevGrandTotalsList");
					   
					    if(prevGrandTotalsList.size()!=0){
					   transid = prevGrandTotalsList.get(0).toString(); 
					   emolumentsTot= Double.parseDouble(prevGrandTotalsList.get(1).toString()) ;
					   cpfTot_prev = Double.parseDouble(prevGrandTotalsList.get(2).toString()) ;
					   PenscontriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(3).toString()) ;
					   PfTot_prev = Double.parseDouble(prevGrandTotalsList.get(4).toString()) ;
					   empSubTot_prev = Double.parseDouble(prevGrandTotalsList.get(5).toString()) ;
					   AAiContriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(6).toString()) ;
					    
					  emolumentsTot_diff = Double.parseDouble(emolumentsTotal) - emolumentsTot_prev;
					 cpfTot_diff = Double.parseDouble(cpfTotal) - cpfTot_prev;
					 PenscontriTot_diff = Double.parseDouble(PenContriTotal) -PenscontriTot_Prev ;
					 PfTot_diff = Double.parseDouble(PFTotal) - PfTot_prev;
					 empSubTot_diff=  Double.parseDouble(empSubTotal) - empSubTot_prev;
					 AAiContriTot_diff=  Double.parseDouble(AAIContriTotal) - AAiContriTot_Prev;
					   
					 
					 
					    EmployeePensionCardInfo  data = new EmployeePensionCardInfo();
					   if(request.getAttribute("finalizedTotals")!=null){
					   finalizedTotals = (ArrayList) request.getAttribute("finalizedTotals");
					   }
					  for(int j=0;j<finalizedTotals.size();j++){
					  data = (EmployeePensionCardInfo)finalizedTotals.get(j);
					  }
					  
					   empSubTot= Double.parseDouble(data.getEmpSub().toString()) ;
					   empSubIntrst = Double.parseDouble(data.getEmpSubInterest().toString()) ;
					   AAiContriTot = Double.parseDouble(data.getAaiContri().toString()) ;
					   aaiContriIntrst = Double.parseDouble(data.getAaiContriInterest().toString()) ;
					   PenscontriTot = Double.parseDouble(data.getPensionTotal().toString()) ;
					   PensionIntrst = Double.parseDouble(data.getPensionInterest().toString()) ;
					  
					  
				 	    
					 %>
					<tr>
						<td class="HighlightData" align="center">Previous Grand Totals</td>
						<td class="HighlightData" align="right"><%=df.format(emolumentsTot_prev)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(cpfTot_prev)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(PenscontriTot_Prev)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(PfTot_prev)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(empSubTot_prev)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(AAiContriTot_Prev)%></td>

					</tr>
					<tr>
						<td class="HighlightData" align="center">Difference </td>
						<td class="HighlightData" align="right"><%=df.format(emolumentsTot_diff)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(cpfTot_diff)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(PenscontriTot_diff)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(PfTot_diff)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(empSubTot_diff)%></td>
						<td class="HighlightData" colspan="2" align="right"><%=df.format(AAiContriTot_diff)%></td>

					</tr>
					 
					<tr>
						<td class="HighlightData" align="center">Form 2 Adjustments  </td>
						<td class="HighlightData"    align="right">&nbsp;</td>
						<td class="HighlightData" colspan="2" align="right">&nbsp;</td>
						<td class="HighlightData"   align="right"><%=df.format(PenscontriTot)%></td>
						<td class="HighlightData"   align="right"><%=df.format(PensionIntrst)%></td>						
						<td class="HighlightData" colspan="2" align="right">&nbsp;</td>
						<td class="HighlightData"   align="right"><%=df.format(empSubTot)%></td>
						<td class="HighlightData"   align="right"><%=df.format(empSubIntrst)%></td> 
						<td class="HighlightData"   align="right"><%=df.format(AAiContriTot)%></td>
						<td class="HighlightData"   align="right"><%=df.format(aaiContriIntrst)%></td>

					</tr>
					<%} }%>
				</table>
				</td>
			</tr>
	 
		<%
			if (size - 1 != i) {
		%> <br style='page-break-after: always;'>
		<%
			}
		%>
		</td>
		</tr>

		<%
			}
		%>
		 <tr><td>&nbsp;</td></tr>
		

		<%
			}
		%>
		
	</table>

	</tr>
 
</table>
</form>
</body>
</html>
 