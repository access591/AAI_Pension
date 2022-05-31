
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":"
		+ request.getServerPort()+ path + "/";
	System.out.println("basePath is ========= "+basePath);
%>

<%@ page language="java" errorPage ="/view/common/ErrorPage.jsp" import="java.text.*,java.sql.*,java.io.*,java.util.*" %>
<%@ page import="aims.dao.FinancialDAO"%> 
<%@ page import="aims.service.FinancialService"%> 
<%@ page import="aims.bean.*"%>
<jsp:useBean id="commonDB" class="aims.common.DBUtils" />

<Html><Head><Title>Pension Fund Report </Title>
<link rel='stylesheet' href='<%=basePath%>PensionView/css/aai.css' type='text/css'>
<link rel="stylesheet" href="<%=basePath%>PensionView/css/reportstyle.css" type="text/css">

</Head>
<body text=#000000 leftMargin=0 topMargin=0 link="white" vlink="white" >

<table border=0 cellpadding=3 cellspacing=0 width="100%" align="center" valign="middle">

<%
	
	FinancialDAO dao=new FinancialDAO();
	FinancialService financeService=new FinancialService();
	form3Bean formsetBean=null;
	String labelAry[] = null;
	ArrayList airportList = null;

	String months[] = {"January","Febraury","March","April","May","June","July","August","September","October","November","December"};

	String monthNo[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
	String monthCd[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
	String year = "";
	String month = "";
	String region = "";
	String prevMonth = "";
	String monthName = "";
	String prevYear = "";

	String date = "";
	String prevDate = "";

	String formno = "";
	String heading = "";
	String title = "";
	String note = "";
	String signatory = "Signature of the employer or other <br> authorised officer of the Establishment";
	String dispPrevMnt="";
	year = request.getParameter("year");
	month = request.getParameter("month");
	int tempMonth=Integer.parseInt(month);
	month =Integer.toString(tempMonth);
	region = request.getParameter("region");
	String 	 reportType="";
	if (request.getParameter("frm_reportType") != null) {

				reportType = (String) request.getParameter("frm_reportType");

				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
		
					String fileName = "form-4-report.xls";

					response.setContentType("application/vnd.ms-excel");

					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);

				}

			}

	for(int i=0;i<monthNo.length;i++){
		if(month.equals(monthNo[i])){
			month = monthCd[i];
			prevMonth = (i==0?"Dec":monthCd[i-1]);
			prevYear = ((prevMonth.equals("Dec"))?(Integer.parseInt(year)-1)+"":year);
			monthName = months[i];
			dispPrevMnt=months[i-1];
			break;
		}
	}

	if(!prevMonth.equals("")){
		prevDate = "/"+(prevMonth)+"/"+prevYear;
		date = "/"+(month)+"/"+year;
	}

	formno = "FORM-4";
	heading = "(paragraph 20 of the employees pension scheme, 1995)";
	title = "RETURN OF EMPLOYEES ENTITLED FOR MEMBERSHIP OF THE PENSION FUND";
	note = "Note: New employee who has attained the age of 58 years and/or drawing Pension under the EPS, 1995 is not to be enrolled as a member.";

	labelAry = new String[]{"Sl.No","Pension A/c No(first time given by Hqrs)","CPF A/c No","Employee No","Name of the member","Designation","Father's name or Husband name(in case of married women)","Date of Birth","Sex","Date of entitlement for membership","Remarks(Previous A/c No and Particulars of previous service (enclose scheme certificate, if anyP)"};
	
	airportList=financeService.getAirportListByForm45Report(region);
	
%>
<tr>
	<td  colspan=2 class="reportlabel" align=center><%=formno%></td>
</tr>
<tr>
	<td  colspan=2 class="label" align=center><%=heading%></td>
</tr>
<tr>
	<td  colspan=2 class="reportlabel"  align=center><%=title%></td>
</tr>
<tr>
	<td  colspan=2 class="label" align=center>DURING THE MONTH OF <u><font color=blue><%=dispPrevMnt%>,&nbsp;<%=year%>&nbsp;</font></u></td>
</tr>
<tr>
	<td  colspan=2 class="label" align=center>Region:<%=region%></td>
</tr>
<tr>
	<td colspan=2 height=20>&nbsp;</td>
</tr>
<tr>
								<td colspan="4">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Name & Address of Establishment:
											</td>
											<td class="reportdata">
												Airports Authority Of India,
												<br />
												Rajiv Gandhi Bhawan,Safdarjung Airport,New Delhi-3
											</td>
											<td class="reportsublabel" align="right" width="20%">
												Date of Coverage:
											</td>
											<td class="reportdata" nowrap="nowrap">
												01-Apr-1995
											</td>
										</tr>

									</table>
								</td>
							</tr>

							<tr>
								<td colspan="4">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
										<tr>
											<td class="reportsublabel" width="28%">
												Industry in which the Establishment is engaged :
											</td>
											<td class="reportdata">
												Civil Aviation
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="reportsublabel" align="right" width="28%">
												Code No. of the Establishment:
											</td>
											<td class="reportdata">
												T.N./DL/36478
											</td>
										</tr>
								
										<tr>
											<td class="reportsublabel">
												Registration No. of the
												<br>
												Establishment :
											</td>
										</tr>
									</table>
								</td>
							</tr>
<%
	String aptName = "";
	String airportCode = "";
	int srlno = 0;
	ArrayList alist = new ArrayList();
	ArrayList dataList = new ArrayList();
	for(int ii=0;ii<airportList.size();ii++){
	airportCode=(String)airportList.get(ii);
	//dataList=dao.financeForm4Report(airportCode,date,prevDate,region);
	dataList=financeService.financeForm4Report(airportCode,date,prevDate,region,"cpfaccno","");
%>
<tr>
	<td class="label">Name Of Unit :&nbsp;<font class="reportdata"><%=airportCode%></font></td>
</tr>
<tr>
	<td  colspan=2> 
		<table width=100% border=1 bordercolor="gray" cellpadding=2 cellspacing=0>
			<tr>
			<%
				for(int i=0;i<labelAry.length;i++){
					out.println("<th class='label'>"+labelAry[i]+"</th>");
				}
			%>
			</tr>
			<tr>
			<%
				for(int i=0;i<labelAry.length;i++){
					out.println("<th class='label'>"+(i+1)+"</th>");
				}
			%>
			</tr>
			
			<%
				int recCount = 0;
				for(int k=0;k<dataList.size();k++){
					formsetBean=(form3Bean)dataList.get(k);
					srlno++;
					recCount++;
			%>
				<tr>
					<td class="Data" width="2%"><%=srlno%></td>
					<td class="Data" width="5%"><%=formsetBean.getPensionNumber()%></td>
					<td class="Data" width="5%"><%=formsetBean.getCpfaccno()%></td>
					<td class="Data" width="5%"><%=formsetBean.getEmployeeNo()%></td>
					<td class="Data" width="10%"><%=formsetBean.getEmployeeName()%></td>
					<td class="Data" width="10%"><%=formsetBean.getDesignation()%></td>
					<td class="Data" width="10%"><%=formsetBean.getFamilyMemberName()%></td>
					<td class="Data" width="5%"><%=formsetBean.getDateOfBirth()%></td>
					<td class="Data" width="5%"><%=formsetBean.getSex()%></td>
					<td class="Data" width="5%"><%=formsetBean.getDateOfEntitle()%></td>
					<td class="Data" width="15%"><%=formsetBean.getRemarks()%></td>
				</tr>
			<%
				}
				
				if(recCount==0){
					out.println("<tr>");
					out.println("<Td Colspan='"+labelAry.length+"' class='data' align=center height=40><font color=blue> No Details Found</font></Td></Tr>");
					out.println("</tr>");
				}
			%>
		</table>
	</td>
</tr>
<%}%>
<tr>
	<td colspan=2 height=50>&nbsp;</td>
</tr>
<tr>
	<td colspan=2><HR></td>
</tr>
<tr>
	<td  colspan=2 class="tb" align=left><%=note%></td>
</tr>
<tr>
	<td colspan=2 height=50>&nbsp;</td>
</tr>
<tr>
	<td width=70%></td>
	<td class=""><%=signatory%></td>
</tr>
</TABLE>
</body >
</html>