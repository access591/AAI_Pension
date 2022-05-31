<%@ page language="java" import="aims.service.FinancialService" pageEncoding="UTF-8"%>
<%
 FinancialService finance = new FinancialService();
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
ArrayList a=new ArrayList();
String dispDesignation="";
System.out.println("this is starting...");
 String interestCalc=request.getAttribute("interestCalcfinal").toString();
 System.out.println("the values...."+interestCalc);
 String finalintrdate=request.getAttribute("finalintdate").toString();
 System.out.println("the values...."+finalintrdate);
 String reIntrestcalcDate=request.getAttribute("reIntrestcalcDate").toString();
 System.out.println("reIntrestcalcDate"+reIntrestcalcDate);
 String station=request.getAttribute("regionStr").toString();
 String region=request.getAttribute("regionStr").toString();
 String ComputerName = session.getAttribute("computername").toString();
 String username = session.getAttribute("userid").toString();
%>
<%@ page import="aims.common.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.action.FinanceServlet" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <script type="text/javascript">
    function deleteTransaction(monthyear,cpfaccno,region,airportcode){
  //	alert("month year"+monthyear +"cpfaccno " +cpfaccno +"region" +region +"airportcode"+airportcode);
    var answer =confirm('Are you sure, do you want  delete this record');
    var pfid=document.forms[0].pfid.value;
    var page="PensionContribution";
   
    var mappingFlag="true";
	if(answer){
	//alert("inside true");
    document.forms[0].action="<%=basePath%>reportservlet?method=deleteTransactionData&cpfaccno="+cpfaccno+"&monthyear="+monthyear+"&region="+region+"&pfid="+pfid+"&page="+page+"&airportcode="+airportcode+"&mappingFlag="+mappingFlag;;
	document.forms[0].method="post";
	//document.forms[0].submit();
    }
    }  
    function showIntrest(intrest,pensionno,employeenm,employeeno,designation,difftotals,revisedpc,difcontri){
    alert("hiiiii");
    var interestCalc='<%=interestCalc%>';
    var station='<%=station%>';
    var region='<%=region%>';
     document.forms[0].action="<%=basePath%>reportservlet?method=saveIntrest&intrest="+intrest+"&pensionno="+pensionno+"&employeenm="+employeenm+"&designation="+designation+"&station="+station+"&region="+region+"&interestCalc="+interestCalc+"&difftotals="+difftotals+"&revisedpc="+revisedpc+"&difcontri="+difcontri;
	document.forms[0].method="post";
	document.forms[0].submit();
    }   
    function test(){
       //alert('This pension contribution report');
    }
  
    </script>
  </head>
  <body onload="javascript:test()">
<%!
	ArrayList blockList=new ArrayList();
	String breakYear="";
%>
  <form action="method">
			<table width="100%" border="0" 	 align="center" cellpadding="0" cellspacing="0">
	  <%
  	 ArrayList PensionContributionList=new ArrayList();
  	 ArrayList pensionList=new ArrayList();
  	 CommonUtil commonUtil=new CommonUtil();
  	 boolean countFlag=true;
	 String fullWthrOptionDesc="",genderDescr="",mStatusDec="";
  	 String employeeNm="",pensionNo="",doj="",dob="",cpfacno="",employeeNO="",designation="",fhName="",gender="",fileName="",mStatus="",discipline="";
  	 String reportType ="",chkRegionString="",dispSignPath="",dispSignStation="",chkStationString="",chkBulkPrint="",whetherOption="",dateOfEntitle="",empSerialNo="";
  	 String mangerName="";
  	 String getEditpensionno="";
  	 double pctotal=0.0,recoverydifftotals=0.0;
  	  String recoverieTable="";
	  	 if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					fileName = "Pension_Contribution_report.xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
                if (reportType.equals("DBF")) {
			
					fileName = "Pension_Contribution_report.dbf";
					response.setContentType("application/x-wais-source");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
		 if (request.getAttribute("pctotal") != null) {
			 pctotal=Double.parseDouble(request.getAttribute("pctotal").toString());
		 }
		 if (request.getAttribute("intrest") != null) {
			 recoverydifftotals=Double.parseDouble(request.getAttribute("intrest").toString());
		 }
		 if (request.getAttribute("recoverieTable") != null) {
			 recoverieTable=request.getAttribute("recoverieTable").toString();
			 }
		 double interestforNoofMonths=0;
		 if (request.getAttribute("interestforNoofMonths") != null) {
			 interestforNoofMonths=Double.parseDouble(request.getAttribute("interestforNoofMonths").toString());
			 System.out.println(interestforNoofMonths);
		 }
		 if (request.getAttribute("getEditPenno") != null) {
			 getEditpensionno=request.getAttribute("getEditPenno").toString();			 
		 }else{
		 getEditpensionno="";
		 }
		 double interestforfinalsettleMonths=0;
		 if (request.getAttribute("interestforfinalsettleMonths") != null) {
			 interestforfinalsettleMonths=Double.parseDouble(request.getAttribute("interestforfinalsettleMonths").toString());
			 System.out.println(interestforfinalsettleMonths);
		 }
	 int size=0;
 	 PensionContributionList=(ArrayList)request.getAttribute("penContrList");
 	 String cntFlag="",buildFinyear="",tempBuildFinYear="";
 	  if(request.getAttribute("blkprintflag")!=null){
      chkBulkPrint=(String)request.getAttribute("blkprintflag");
    }
     if(request.getAttribute("regionStr")!=null){
    	chkRegionString=(String)request.getAttribute("regionStr");
    }
    if(request.getAttribute("stationStr")!=null){
      chkStationString=(String)request.getAttribute("stationStr");
    }
    if(PensionContributionList.size()>0){
 	 for(int i=0;i<PensionContributionList.size();i++){
			PensionContBean contr=(PensionContBean)PensionContributionList.get(i);
			employeeNm=contr.getEmployeeNM();
			pensionNo=contr.getPensionNo();
			empSerialNo=contr.getEmpSerialNo();
			doj=contr.getEmpDOJ();
			
			dob=contr.getEmpDOB();
			cpfacno=StringUtility.replaces(contr.getCpfacno().toCharArray(),",=".toCharArray(),",").toString();
			System.out.println("cpfacno "+cpfacno);
			if(cpfacno.indexOf(",=")!=-1){
						cpfacno=cpfacno.substring(1,cpfacno.indexOf(",="));
			}else if(cpfacno.indexOf(",")!=-1){
				cpfacno=cpfacno.substring(cpfacno.indexOf(",")+1,cpfacno.length());
			}
			whetherOption=contr.getWhetherOption();
			if(whetherOption.toUpperCase().trim().equals("A")){
			fullWthrOptionDesc="Full Pay";
			}else if(whetherOption.toUpperCase().trim().equals("B") || whetherOption.toUpperCase().trim().equals("NO")){
			fullWthrOptionDesc="Ceiling Pay";
			}else{
			fullWthrOptionDesc=whetherOption;
			}
			employeeNO=contr.getEmployeeNO();
			designation=contr.getDesignation();
			fhName=contr.getFhName();
			gender=contr.getGender();
			
			if(gender.trim().toLowerCase().equals("m")){
				genderDescr="Male";
			}else if(gender.trim().toLowerCase().equals("f")){
				genderDescr="Female";
			}else{
				genderDescr=gender;
			}
			
			mStatus	=contr.getMaritalStatus().trim();
			
			if(mStatus.toLowerCase().equals("m")||(mStatus.toLowerCase().trim().equals("yes"))){
				mStatusDec="Married";
			}else if(mStatus.toLowerCase().equals("u")||(mStatus.toLowerCase().trim().equals("no"))){
				mStatusDec="Un-married";
			}else if(mStatus.toLowerCase().equals("w")){
				mStatusDec="Widow";
			}else{
				mStatusDec=mStatus;
			}
			dateOfEntitle=contr.getDateOfEntitle();
			tempBuildFinYear=commonUtil.converDBToAppFormat(dateOfEntitle,"dd-MMM-yyyy","yyyy");
			buildFinyear=tempBuildFinYear+"-"+commonUtil.converDBToAppFormat(Integer.toString(Integer.parseInt(tempBuildFinYear)+1),"yyyy","yy");
			discipline=contr.getDepartment();
			cntFlag=contr.getCountFlag();
			pensionList=contr.getEmpPensionList();
			blockList=contr.getBlockList();
			 size=pensionList.size();
  			if(size!=0){
  			boolean signatureFlag=false;
  			   if(chkBulkPrint.equals("true")){
   		   			 if(chkRegionString.equals("North Region")){
   		   			signatureFlag=true;
   	    				dispSignStation="";
   	    				mangerName="(Anil Kumar Jain)";
   	     				dispDesignation="Asstt.General Manager(Fin), AAI, NR";
		    			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
		    		 }else if(chkRegionString.equals("South Region")){
		    			signatureFlag=true;
		    			dispSignStation="South Region";
		    			dispDesignation="Deputy General Manager(F&A)";
		    			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
		    		 }else if(chkRegionString.equals("North-East Region")){
		    			signatureFlag=true;
		    			dispSignStation="";
		    			mangerName="(G.S Mohapatra)";
		     			dispDesignation="Joint General Manager(Fin), AAI, NER,Guwahati";
		    			dispSignPath=basePath+"PensionView/images/signatures/G.SMohapatra.gif";
		    		 }else if(chkRegionString.equals("RAUSAP")){
		      			signatureFlag=true;
		      			dispSignStation="";
		      			mangerName="(Monika Dembla)";
		      			dispDesignation="Manager(F & A), AAI, RAU,SAP ";
		      			dispSignPath=basePath+"PensionView/images/signatures/Monika Dembla.gif";	
   		 			}else if(chkRegionString.equals("West Region")){
		    			signatureFlag=true;
		    			dispSignStation="";
		    			mangerName="(Shri S H Kaswankar)";
     					dispDesignation="Sr. Manager(Fin), AAI, WR, Mumbai";
		    			dispSignPath=basePath+"PensionView/images/signatures/Kaswankar.gif";	
   		 			}else if(chkRegionString.equals("CHQIAD")){
    					if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
     						signatureFlag=true;
     						dispSignStation="";
     						mangerName="(Arun Kumar)";
     						dispDesignation="Sr. Manager(F&A), AAI,IGICargo IAD";
     						dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
    		 			}else if(chkStationString.toLowerCase().equals("IGI IAD".toLowerCase())){
		     				signatureFlag=true;
		     				dispSignStation="";
		     				mangerName="(Arun Kumar)";
		     				dispDesignation="Sr. Manager(F&A), AAI,IGI IAD";
		     				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";	
    		 			}
    			  }
   		 		}
  %>
    <%!
		String getBlockYear(String year)
		{
		String bYear="";
		
		for(int by=0;by<blockList.size();by++){
			bYear=(String)blockList.get(by);
			String[] bDate=bYear.split(",");
			
			if(year.equals(bDate[1])){
				breakYear=bDate[1];
				breakYear=bYear;
				break;
			}else{
				breakYear="03-96";
			}
		}
	//	System.out.println("breakYear"+breakYear);
		//blockList.remove(breakYear);
		return breakYear;
		}
%>
								<tr>
					<td align="center" colspan="5" >
						<table border=0 cellpadding=3 cellspacing=0 width="40%" align="center" valign="middle">
							<tr>
								<td>
									<img src="<%=basePath%>PensionView/images/logoani.gif" >
								</td>
								<td class="label" align="center" valign="top" nowrap="nowrap">
									<font color='black' size='4' face='Helvetica'> AIRPORTS AUTHORITY OF INDIA </font>
								</td>

							</tr>
						
						</table>
					</td>
				</tr>
            <%
               String heading="CPF REPORT FOR THE PERIOD";
                if(recoverieTable.equals("true")){%>
               
                 <% heading="CPF REPORT FOR THE PERIOD";} %>
					<tr>
						<td>
								<table border="0"    cellpadding="2" cellspacing="0" width="100%" align="center">
								<tr>
								<td align="center" class="reportsublabel" colspan="2"> <%=heading%> <%=buildFinyear%> TO 2017-18</td>	
								</tr>
								</table>
								</td>
							</tr>
				<tr >
					<td>
						<table border="1" style="border-color:gray"    cellpadding="2" cellspacing="0" width="95%" align="center" 	>
							<tr >
								
								<td class="reportsublabel">PF ID</td>
								<td class="reportdata"><%=pensionNo%></td>	
									<input type="hidden" name="pfid" value="<%=empSerialNo%>">			
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
				
				
				<tr>
					<td  colspan="5" >
					
						<table border="1" style="border-color:gray;"    cellpadding="2" cellspacing="0" width="95%" align="center" >
							
							<tr>
								<td class="label" width="10%" align="center">Month</td>
								<td class="label" width="10%" align="center">Emolument</td>
								<td class="label" width="10%" align="center">EPF</td>
								<td class="label" width="10%" align="center">Pension Contribution </td>
								<td class="label" width="10%" align="center">NET EPF</td>
								<td class="label" width="10%" align="center">Station</td>
								<td class="label" width="10%" align="center">Remarks</td>
							
							</tr>
							<%
							double totalEmoluments=0.0,pfStaturary=0.0,totalPension=0.0,empVpf=0.0,principle=0.0,interest=0.0,pfContribution=0.0;
							double grandEmoluments=0.0,grandCPF=0.0,grandPension=0.0,grandPFContribution=0.0;
							double cpfInterest=0.0,pensionInterest=0.0,pfContributionInterest=0.0;
							double grandCPFInterest=0.0,grandPensionInterest=0.0,grandPFContributionInterest=0.0;
							double cumPFStatury=0.0,cumPension=0.0,cumPfContribution=0.0;
							double cpfOpeningBalance=0.0,penOpeningBalance=0.0,pfOpeningBalance=0.0;
							double percentage=0.0;
							boolean openFlag=false;
							int count=0;
							int chkMnths=0;
							boolean flag=false;
							String findMnt="";
							int countMnts=0;
							DecimalFormat df = new DecimalFormat("#########0");
							String dispFromYear="",dispToYear="",totalYear="";
							boolean dispYearFlag=false;
							double rateOfInterest=0;
							String monthInfo="",getMnthYear="";
							int noofmonths=0;
							for(int j=0;j<pensionList.size();j++){
								TempPensionTransBean bean=(TempPensionTransBean)pensionList.get(j);
								if(bean!=null){
								String dateMontyYear=bean.getMonthyear();
								
								if(dispYearFlag==false){
									if(dispFromYear.equals("")){
										dispFromYear=commonUtil.converDBToAppFormat(dateMontyYear,"dd-MMM-yyyy","yy");
									}
								
									getMnthYear=commonUtil.converDBToAppFormat(dateMontyYear,"dd-MMM-yyyy","MM-yy");
								
									String monthInterestInfo=getBlockYear(getMnthYear);
									String [] monthInterestList=monthInterestInfo.split(",");
									if(monthInterestList.length==2){
											monthInfo=monthInterestList[1];
									
									rateOfInterest=new Double(monthInterestList[0]).doubleValue();
									}
							
									dispYearFlag=true;
								
									breakYear="";
								}
								
								noofmonths++;
						        String monthYear=bean.getMonthyear().substring(dateMontyYear.indexOf("-")+1,dateMontyYear.length());
						       // if(bean.getInterestRate()!=null || !bean.getInterestRate().equals(" ")){
						   //     	rateOfInterest=new Double(bean.getInterestRate()).doubleValue();
						     //   }
						        findMnt=commonUtil.converDBToAppFormat(bean.getMonthyear(),"dd-MMM-yyyy","MM-yy");
						     
						 //       System.out.println("findMnt==="+findMnt+"monthInfo==="+monthInfo+"dispYearFlag***********************"+dispYearFlag);
						       
								if(findMnt.equals(monthInfo)){
									flag=true;
								
									breakYear="";
								}
						
								count++;
							
								totalEmoluments= new Double(df.format(totalEmoluments+Math.round(Double.parseDouble(bean.getEmoluments())))).doubleValue();
								pfStaturary= new Double(df.format(pfStaturary+Math.round(Double.parseDouble(bean.getCpf())))).doubleValue();
								cumPFStatury=cumPFStatury+pfStaturary;
								empVpf = new Double(df.format(empVpf+Math.round(Double.parseDouble(bean.getEmpVPF())))).doubleValue();
								principle =new Double(df.format(principle+Math.round(Double.parseDouble(bean.getEmpAdvRec())))).doubleValue();
								interest =new Double(df.format(interest+Math.round(Double.parseDouble(bean.getEmpInrstRec())))).doubleValue();
								totalPension=new Double(df.format(totalPension+Math.round(Double.parseDouble(bean.getPensionContr())))).doubleValue();
								cumPension=cumPension+totalPension;
						        pfContribution= new Double(df.format(pfContribution+Math.round( Double.parseDouble(bean.getAaiPFCont())))).doubleValue();
						        cumPfContribution=cumPfContribution+pfContribution;
						        
						       
							
							%>
						
					         <% 
					   //      System.out.println(bean.getRecordCount());
					       if(bean.getRecordCount().equals("Single")){
								%>
							<% if(!bean.getEditedDate().trim().equals("N")&& (recoverieTable.equals("true"))){%>
         				
         				
          			<tr bgcolor="orange" >
 			 <%
 			 }else{ %>
  			<tr bgcolor="white">
			  <%} %>
							
								<td class="Data" align="center"><%=monthYear%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getEmoluments()))%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getCpf()))%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getAaiPFCont()))%></td>
								<td class="Data" nowrap="nowrap"><%=bean.getStation()%></td>
								<td class="Data"><%=bean.getForm7Narration()%></td>
							</tr><%}else if(bean.getRecordCount().equals("Duplicate")){%>
								<tr bgcolor="yellow" >
								<td class="Data"  width="12%" align="center"><font color="red"><%=monthYear%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getEmoluments()))%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getCpf()))%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getAaiPFCont()))%></font></td>
								<td class="Data" width="12%"><font color="red"><%=bean.getStation()%></font></td>
								<td class="Data" width="12%"><font color="red"><%=bean.getForm7Narration()%></font></td>
							    
							</tr>
								<%}%>
							 	  <%
							 
							  	if(flag==true){
							     if(noofmonths>12){
							    	 noofmonths=12;
							     }
							  	dispToYear=commonUtil.converDBToAppFormat(dateMontyYear,"dd-MMM-yyyy","yy");
							  	//System.out.println("dispFromYear=="+dispFromYear+"flag==="+flag+"dispToYear"+dispToYear+"rateOfInterest"+rateOfInterest);
							  	if(dispFromYear.equals(dispToYear)){
							  	if(dispFromYear.equals("00")){
							  		 	dispFromYear="99";
							  	}
							 
							  	if(dispFromYear.trim().length()<2){
							  	dispFromYear="0"+dispFromYear;
							  	}
							  	dispToYear=Integer.toString(Integer.parseInt(dispToYear)+1);
							  	if(dispToYear.trim().length()<2){
							  		dispToYear="0"+dispToYear;
							  	}
							  	}
							  	totalYear=dispFromYear+"-"+dispToYear;
						
							  	
							  	dispFromYear="";
							  %>
							 <tr>
								<td class="HighlightData" align="center">Total <%=totalYear%></td>
								<td class="HighlightData" align="right"><%=df.format(totalEmoluments)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfStaturary)%></td>
								<td class="HighlightData" align="right"><%=df.format(totalPension)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfContribution)%></td>
								<td class="HighlightData">---</td>
								<td class="HighlightData">---</td>
							
							
							</tr>
							<tr>
								<%
									//System.out.println("rateOfInterest"+rateOfInterest+"No of months"+noofmonths);
								    if(noofmonths<12){
								    	cpfInterest=Math.round((cumPFStatury*rateOfInterest/100)/12)+Math.round((cpfOpeningBalance*rateOfInterest/100)*noofmonths/12);
										pensionInterest=Math.round((cumPension*rateOfInterest/100)/12)+Math.round((penOpeningBalance*rateOfInterest/100)*noofmonths/12);
										pfContributionInterest=Math.round((cumPfContribution*rateOfInterest/100)/12)+Math.round((pfOpeningBalance*rateOfInterest/100)*noofmonths/12);
								    }else{
								    	cpfInterest=Math.round((cumPFStatury*rateOfInterest/100)/12)+Math.round(cpfOpeningBalance*rateOfInterest/100);
										pensionInterest=Math.round((cumPension*rateOfInterest/100)/12)+Math.round(penOpeningBalance*rateOfInterest/100);
										pfContributionInterest=Math.round((cumPfContribution*rateOfInterest/100)/12)+Math.round(pfOpeningBalance*rateOfInterest/100);
							  		}
								  //  System.out.println("cumPFStatury"+cumPFStatury+"cumPension"+cumPension);
								
								%>
								
								<td class="HighlightData" align="center">Interest(<%=rateOfInterest%>%)</td>
								<td class="HighlightData">---</td>
								<td class="HighlightData" align="right"><%=df.format(cpfInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(pensionInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfContributionInterest)%></td>
								<td class="HighlightData">---</td>
								<td class="HighlightData">---</td>
							</tr>
							<tr>
								<%
									flag=false;
									openFlag=true;
									noofmonths=0;
									cpfOpeningBalance=Math.round(pfStaturary+cpfInterest+Math.round(cpfOpeningBalance));
									penOpeningBalance=Math.round(totalPension+pensionInterest+Math.round(penOpeningBalance));
									pfOpeningBalance=Math.round(pfContribution+pfContributionInterest+Math.round(pfOpeningBalance));
								%>
								<td class="HighlightData" align="center">CL BAL</td>
								<td class="HighlightData">---</td>
								<td class="HighlightData" align="right"><%=df.format(cpfOpeningBalance)%></td>
								<td class="HighlightData" align="right"><%=df.format(penOpeningBalance)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfOpeningBalance)%></td>
								<td class="HighlightData">---</td>
								<td class="HighlightData">---</td>
							</tr>
							<%
							grandEmoluments=grandEmoluments+totalEmoluments;
							grandCPF=grandCPF+pfStaturary;
							grandPension=grandPension+totalPension;
							grandPFContribution=grandPFContribution+pfContribution;
							
							grandCPFInterest=grandCPFInterest+cpfInterest;
							grandPensionInterest=grandPensionInterest+pensionInterest;
							grandPFContributionInterest=grandPFContributionInterest+pfContributionInterest;
							cumPFStatury=0.0;cumPension=0.0;cumPfContribution=0.0;
							totalEmoluments=0;pfStaturary=0;totalPension=0;pfContribution=0;
							cpfInterest=0;pensionInterest=0;pfContributionInterest=0;
							}%>
							<%	  	dispYearFlag=false;}}%>
							<tr>
								<td colspan="8">
									<table align="center" width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="gray">
							<tr>
								<td class="HighlightData"></td>
								<td class="HighlightData">Emolument</td>
								<td class="HighlightData">CPF</td>
								<td class="HighlightData">Interest</td>
								<td class="HighlightData">Pension Contribution</td>
								<td class="HighlightData">Interest</td>
								<td class="HighlightData">PF Contribution</td>
								<td class="HighlightData">Interest</td>
							</tr>
							<tr>
								<td class="HighlightData" align="center">Grand  Total of <%=count%> months </td>
								<td class="HighlightData"></td>
								<td class="HighlightData" align="right"><%=df.format(grandCPF)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandCPFInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPension)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPensionInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPFContribution)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPFContributionInterest)%></td>
							</tr>
							<tr>
								<td class="HighlightData" align="center">Grand Total</td>
								<td class="HighlightData" align="right"><%=df.format(grandEmoluments)%></td>
								<td class="HighlightData"  colspan="2" align="right"><%=df.format(grandCPF+grandCPFInterest)%></td>
							
								<td class="HighlightData" colspan="2" align="right"><%=df.format(grandPension+grandPensionInterest)%></td>
							
								<td class="HighlightData"  colspan="2" align="right"><%=df.format(grandPFContribution+grandPFContributionInterest)%></td>
							</tr>
					
    </table></td>
							</tr>
<tr>
	 <td colspan="8"><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <%if(recoverieTable.equals("true")){%>          
	
   <tr>
	<td class="HighlightData" align="right"> Old PensionContribution Total : <%=pctotal%> </td>
 	</tr>
	<tr>
 	<td class="HighlightData" align="right">Revised PensionContribution Total : <%=df.format(grandPension+grandPensionInterest)%> </td>
	 </tr>
	<tr>
 	<td class="HighlightData" align="right">Deviation in PensionContribution : <%=(grandPension+grandPensionInterest)- (pctotal)%>  </td>
 	</tr>
<%if(interestforNoofMonths>0){
 System.out.println("interestforNoofMonths "+interestforNoofMonths);
 	 double interetYears=interestforNoofMonths/12;
	 String interestYears1=String.valueOf(interetYears);
	 double j=Math.floor(interetYears);
	 double m=0,n=0,r=0;
	 double fractionfinalInterest=0;
	  double fractionInterest=0;
	 double fractionofYears=interetYears-j;
	 double deviationAmount=(grandPension+grandPensionInterest)- pctotal;
	 System.out.println("aa"+deviationAmount);
	 double total=0.0;
	 
	if(j>=2){ 
	m=j-2;j=2;
	}
    if(m>=1){
        n=m-1;
        m=1;
    }
    if(m>=1){
        r=n-1;
        n=1;
    }     
	 for(int k=0;k<j;k++){
		 double interest1=Math.round(deviationAmount* 8.5/100);
		 System.out.println("fractionInterest2008-10 "+interest1);
		 deviationAmount=interest1+deviationAmount;
		 System.out.println(deviationAmount);
	 }
	  for(int l=0;l<m;l++){	
		 double interest1=Math.round(deviationAmount* 9.5/100);
		 System.out.println("fractionInterest2010-11 "+interest1);
		 deviationAmount=interest1+deviationAmount;
		 System.out.println(deviationAmount);
	 }
	 
	 for(int h=0;h<n;h++){	
	 System.out.println("getEditpensionno.trim()"+getEditpensionno.trim());
	  if(getEditpensionno.trim().equals("Y")){
	  double fractionInterest2009=Math.round(deviationAmount* 9.5/100);
	 System.out.println("fractionInterest2011-12 "+fractionInterest2009);
	 deviationAmount=deviationAmount+fractionInterest2009;
	 System.out.println(deviationAmount);
	 }else if(getEditpensionno.trim().equals("S")){
	 double fractionInterest2009=Math.round(deviationAmount* 8.25/100);
	 System.out.println("fractionInterest2011-12 "+fractionInterest2009);
	 deviationAmount=deviationAmount+fractionInterest2009;
	 System.out.println(deviationAmount);
	 }else{
	  double fractionInterest2009=Math.round(deviationAmount* 9.5/100);
	 System.out.println("fractionInterest2011-12 "+fractionInterest2009);
	 deviationAmount=deviationAmount+fractionInterest2009;
	 System.out.println(deviationAmount);
	 }
	 }
	  for(int p=0;p<r;p++){	 
	 double fractionInterest2010=Math.round(deviationAmount* 8.25/100);
	 System.out.println("fractionInterest2012-13 "+fractionInterest2010);
	 deviationAmount=deviationAmount+fractionInterest2010;
	 System.out.println(deviationAmount);
	 }
	 System.out.println("fractionofYears"+fractionofYears);	 ;
	  if(getEditpensionno.trim().equals("Y")){	  
	 fractionInterest=deviationAmount*9.5/100*fractionofYears;
	 System.out.println("fractionInterest 9.5 1"+fractionInterest);
	 }else if(getEditpensionno.trim().equals("S")){
	 fractionInterest=deviationAmount*8.25/100*fractionofYears;
	 System.out.println("fractionInterest 8.25 1"+fractionInterest);
	 }else{
	  fractionInterest=deviationAmount*8.25/100*fractionofYears;
	  System.out.println("fractionInterest 8.25 2"+fractionInterest);
	 }
	  System.out.println("fractionInterest "+fractionInterest);
	 deviationAmount=deviationAmount+fractionInterest;
	 System.out.println(deviationAmount);%>	
    <tr>
 	<td class="HighlightData" align="right">Interest : <%=Math.round(deviationAmount-((grandPension+grandPensionInterest)- pctotal))%></td>
 	</tr>
  <tr>
 	<td class="HighlightData" align="right" onclick="">Totals : <%=Math.round(deviationAmount)%>  </td>
 	</tr> 
 	<%if(deviationAmount!=0 && reIntrestcalcDate.equals("")){
 	 	finance.saveFinalRecoveryIntrest(String.valueOf(Math.round(deviationAmount-((grandPension+grandPensionInterest)- pctotal))),pensionNo,employeeNm,designation,station,region,reIntrestcalcDate,username,ComputerName,String.valueOf(Math.round(deviationAmount)),String.valueOf((grandPension+grandPensionInterest)- (pctotal)),df.format(grandPension+grandPensionInterest));
 	}%>	
 	<%if(recoverydifftotals>0 && !reIntrestcalcDate.equals("")){
	 double interetfinalYears=interestforfinalsettleMonths/12;
	 String interestfinalYears1=String.valueOf(interetfinalYears);
	 double l=Math.floor(interetfinalYears);	
	 double fractionoffinalYears=interetfinalYears-l;
	 double finaldeviationAmount=Math.round(deviationAmount)-(recoverydifftotals);
	 System.out.println("aa"+finaldeviationAmount);	
	 for(int k=0;k<l;k++){	
		 double finalinterest1=Math.round(finaldeviationAmount* 8.25/100);
		 System.out.println("finalinterest 8.25"+finalinterest1);
		 finaldeviationAmount=finalinterest1+finaldeviationAmount;
		 System.out.println(finaldeviationAmount);
	 }	 		
	 fractionfinalInterest=finaldeviationAmount*8.25/100*fractionoffinalYears;
	 finaldeviationAmount=finaldeviationAmount+fractionfinalInterest;
	 System.out.println(finaldeviationAmount);		
	  %> 
	   <tr>
 	<td class="HighlightData" align="right">Already adjusted in Final Settlement : <%=recoverydifftotals%></td>
 	</tr>
   <tr>
 	<td class="HighlightData" align="right">Balance: <%=Math.round(deviationAmount)-(recoverydifftotals)%></td>
 	</tr> 	 
	 <tr>
 	<td class="HighlightData" align="right">Interest on balance amount from(<%=finalintrdate%>) to (<%=reIntrestcalcDate%>) : <%=Math.round(finaldeviationAmount-(Math.round(deviationAmount)-(recoverydifftotals)))%></td>
 	</tr>	
 <%}}}%>
    <tr>
             <td class="label">NOTE:-</td>
             </tr>
  
          <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">1. Family Pension Fund (FPF) for the period 01.04.1995 to 15.11.1995 calculated @1.16% of PAY from Employee's & AAI's EPF.</td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">2. Pension Contribution(PC) for the period 16.11.1995 to 31.03.2008 calculated @8.33% of PAY from AAI Contribution to EPF.</td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">3. Interest on Pension Contribution has been calculated at the EPF Interest rate.</td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">4. The amount of Pension Contribution as calculated above has been adjusted against the opening balance of Employer share as on 01.04.2008.</td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">5. In case of any discrepancy the matter may be brought to the notice of CPF/Pension Cell within 15 days of issue of Statement otherwise the balance would be presumed to have been confirmed. 
</td>
            </tr>
               <%if(signatureFlag==true){%>
  <tr>
    <td colspan="2">
    	<table width="100%" cellpadding="2" cellspacing="2" align="right">
    		<tr>
    			<td colspan="2"  align="right"><img src="<%=dispSignPath%>" /></td>
    		 </tr>
    		 <tr>
    			<td colspan="2"  class="label" align="right"><%=mangerName%></td>
    		 </tr>
    		 <tr>
    		 	<td class="label" align="left">Date:</td>
    			<td  class="label" align="right"><%=dispDesignation%></td>
    		</tr>
    		<tr>
    			<td  colspan="2"  align="right" class="label"><%=dispSignStation%></td>
    		</tr>
    	</table>
    
    </td>
   
  </tr>
<%}else{%>
   <tr>
            <td class="label" align="left">Date:</td>
            <td class="label" align="right">Assistant Manager/ Manager (Finance)
            </td>
  </tr>
<%}%>
         
            
          <tr>
            <td>&nbsp;</td>
            </tr>
        </table></td>
      </tr>
							
									</table>
								</td>
							</tr>
							
						</table>
						<%if(size-1!=i){%>
						<br style='page-break-after:always;'>
						<%}%>
					</td>
				</tr>
					
				<%}%>
				
			
				<%}
				}else{%>
				
				
          <table align="center" width="100%">
          <tr>
          <td align="center">
          <strong>No Records Found</strong>
          </td>
          </tr>
          </table>
				<%}%>
			</table>
		</form>	
  </body>
</html>
