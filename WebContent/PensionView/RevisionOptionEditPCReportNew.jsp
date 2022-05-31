<%@ page language="java" import="aims.service.FinancialService,java.util.HashMap,aims.test.Test" pageEncoding="UTF-8"%>
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
  	 String employeeNm="",pensionNo="",doj="",dob="",doe="",cpfacno="",employeeNO="",designation="",fhName="",gender="",fileName="",mStatus="",discipline="";
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
			doe=contr.getDateofSeperationDt();
			System.out.println("doe=============="+doe);
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
               String heading="REVISED PENSION CONTRIBUTION(Refund Claim) REPORT FOR THE PERIOD";
                if(recoverieTable.equals("true")){%>
               
                 <% heading="REVISED PENSION CONTRIBUTION REPORT FOR THE PERIOD";} %>
					<tr>
						<td>
								<table border="0"    cellpadding="2" cellspacing="0" width="100%" align="center">
								<tr>
								<td align="center" class="reportsublabel" colspan="2"> <%=heading%> <%=buildFinyear%> TO 2019-2020</td>	
								</tr>
								</table>
								</td>
							</tr>
				<tr >
					<td>
						<table border="1" style="border-color:gray"    cellpadding="2" cellspacing="0" width="100%" align="center" 	>
							<tr >
								<td class="reportsublabel">NAME</td>
									<td class="reportdata"><%=employeeNm%></td>	
								<td class="reportsublabel">Year</td>
								<td class="reportdata"><%=buildFinyear%> TO 2019-2020</td>	
								<td class="reportsublabel">DATE OF JOINING</td>
								<td class="reportdata"><%=doj%></td>
									
										
												
							</tr>
							<!--<tr>
								<td class="reportsublabel">EMP NO</td>
								<td class="reportdata"><%=employeeNO%></td>
							<td class="reportsublabel">DESIGNATION</td>
								<td class="reportdata"><%=designation%></td>
														
								
							</tr>
							--><!--<tr>
								<td class="reportsublabel">CPF NO</td>
								<td class="reportdata"><%=cpfacno%></td>
									<td class="reportsublabel">FATHER'S/HUSBAND'S NAME</td>
								<td class="reportdata"><%=fhName%></td>
								
								
							</tr>
							--><!--<tr>
								<td class="reportsublabel">DATE OF BIRTH</td>
								<td class="reportdata"><%=dob%></td>
								<td class="reportsublabel">GENDER</td>
								<td class="reportdata"><%=genderDescr%></td>
								
							</tr>
							--><tr>
								<td class="reportsublabel">PF ID</td>
								<td class="reportdata"><%=pensionNo%></td>	
								
								
									<input type="hidden" name="pfid" value="<%=empSerialNo%>">		
							<td></td>
							<td></td>
								
									<td class="reportsublabel">DATE OF ENDING</td>
								<td class="reportdata"><%=doe%></td>
								
								<!--<td class="reportsublabel">DISCIPLINE</td>
								<td class="reportdata"><%=discipline%></td>
									
							--></tr>
							<tr>
								<!--<td class="reportsublabel">DATE OF MEMBERSHIP</td>
								<td class="reportdata"><%=dateOfEntitle%></td>
								<td class="reportsublabel">PENSION OPTION</td>
								<td class="reportdata"><%=fullWthrOptionDesc%></td>
							
									
							-->
						
							
							</tr>
						</table>
					</td>
				</tr>
				
				
				<tr>
					<td  colspan="5" >
					
						<table border="1" style="border-color:gray;"    cellpadding="2" cellspacing="0" width="100%" align="center" >
							
							<tr>
								<td class="label" width="10%" align="center">Month</td>
								<td class="label" width="10%" align="center">Emolument</td><!--
								<td class="label" width="10%" align="center">EPF</td>
								--><td class="label" width="10%" align="center">Pension Contribution(Full Pay) </td>
								<td class="label" width="10%" align="center">Emolument(B)</td>
								<td class="label" width="10%" align="center">Pension Contribution(Ceiling Pay)</td>
								<td class="label" width="10%" align="center">Difference</td>
								<!--<td class="label" width="10%" align="center">Station</td>
								<td class="label" width="10%" align="center">Remarkss</td>
								--><!--<td class="label" width="10%" align="center">Month</td>
								--><td class="label" width="10%" align="center">Diffrence intrest</td>
							
							</tr>
							<%
							
								         
						   HashMap intrestrate=new HashMap();
						intrestrate.put("1995-1996","12"); 
						intrestrate.put("1996-1997","12");
						intrestrate.put("1997-1998","12"); 
						intrestrate.put("1998-1999","12");
						intrestrate.put("1999-2000","12");  
						intrestrate.put("2000-2001","11.25");	   
						intrestrate.put("2001-2002","9.5");
						intrestrate.put("2002-2003","9.5");
						intrestrate.put("2003-2004","9.5");
						intrestrate.put("2004-2005","9.5");
						intrestrate.put("2005-2006","8.5");	  
						intrestrate.put("2006-2007","8.5");
						intrestrate.put("2007-2008","8.5");
						intrestrate.put("2008-2009","8.5");
						intrestrate.put("2009-2010","8.5");
						intrestrate.put("2010-2011","9.5");
						intrestrate.put("2011-2012","8.25");
						intrestrate.put("2012-2013","8.5");
						intrestrate.put("2013-2014","8.75");
						intrestrate.put("2014-2015","8.75");
						intrestrate.put("2015-2016","8.80");
						intrestrate.put("2016-2017","8.65");
						intrestrate.put("2017-2018","8.55");
						intrestrate.put("2018-2019","8.65");
						intrestrate.put("2019-2020","8.65");
						
							
							
							
							
							
							
							
							String finTotalYear="";
							 String monthy="0";
							
							double totalEmoluments=0.0,pfStaturary=0.0,totalPension=0.0,empVpf=0.0,principle=0.0,interest=0.0,pfContribution=0.0,diffContri=0.0;
							double grandEmoluments=0.0,grandCPF=0.0,grandPension=0.0,grandPFContribution=0.0;
							double cpfInterest=0.0,pensionInterest=0.0,pfContributionInterest=0.0,diffContriInt=0.0;
							double grandCPFInterest=0.0,grandPensionInterest=0.0,grandPFContributionInterest=0.0,grandDiffContri=0.0,grandDiffContriInt=0.0;
							double cumPFStatury=0.0,cumPension=0.0,cumPfContribution=0.0;
							double cpfOpeningBalance=0.0,penOpeningBalance=0.0,pfOpeningBalance=0.0,diffContriOB=0.0;
							double percentage=0.0;
							double emolumentsB=0.0;
							double grandEmolumentsB=0.0,totalEmolumentsB=0.0,grandtotintrest=0.0;
							double differ=0.0,diffintrest=0.0,totdiffintrest=0.0,intrestvalue=0.0,diffnextintrest,newintrest=0.0,obnewbal=0.0,newintrest2=0.0,obnewbal2=0.0,newintrest3=0.0,newintrest4=0.0,obnewbal3=0.0,obnewbal4=0.0,totalint=0.0;
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
								
								
							HashMap month=new HashMap();
							month.put("DEC","3");
							month.put("JAN","2");
							month.put("FEB","1");
							month.put("MAR","0");
							month.put("APR","11");
							month.put("MAY","10");
							month.put("JUN","9");
							month.put("JUL","8");
							month.put("AUG","7");
							month.put("SEP","6");
							month.put("OCT","5");
							month.put("NOV","4");
						        System.out.println(monthYear+":::::::::::::::::::::::::::::::::::::");
						 
						         monthy=month.get(monthYear.substring(0,3).toUpperCase()).toString();
						
					
						
						
						String finYear=commonUtil.getFinYear(bean.getMonthyear());
					  
						     double intRate=Double.parseDouble(intrestrate.get(finYear).toString());    
							
								totalEmoluments= new Double(df.format(totalEmoluments+Math.round(Double.parseDouble(bean.getEmoluments())))).doubleValue();
								pfStaturary= new Double(df.format(pfStaturary+Math.round(Double.parseDouble(bean.getCpf())))).doubleValue();
								cumPFStatury=cumPFStatury+pfStaturary;
								empVpf = new Double(df.format(empVpf+Math.round(Double.parseDouble(bean.getEmpVPF())))).doubleValue();
								principle =new Double(df.format(principle+Math.round(Double.parseDouble(bean.getEmpAdvRec())))).doubleValue();
								interest =new Double(df.format(interest+Math.round(Double.parseDouble(bean.getEmpInrstRec())))).doubleValue();
								totalPension=new Double(df.format(totalPension+Math.round(Double.parseDouble(bean.getPensionContr())))).doubleValue();
								cumPension=cumPension+totalPension;
						        pfContribution= new Double(df.format(pfContribution+Math.round( Double.parseDouble(bean.getPensionContriB())))).doubleValue();
						        cumPfContribution=cumPfContribution+pfContribution;
						        diffContri=totalPension-pfContribution;
						        differ=(Double.parseDouble(bean.getDbPensionCtr()))-Math.round(Double.parseDouble(bean.getPensionContriB()));
						        
						        diffintrest=(differ*intRate/100/12)*Integer.parseInt(monthy);
						        
						        
						        
						        
						       
						        System.out.println("contidiff+previosyearint"+(totdiffintrest+diffContriOB));
								diffnextintrest=(totdiffintrest+diffContriOB)*intRate/100;
								System.out.println("diffnextintrest=========="+diffnextintrest+"totdiffintrest===="+totdiffintrest+"monthy=="+monthy);
						        if(commonUtil.converDBToAppFormat(bean.getMonthyear(),"dd-MMM-yyyy","MM").equals("04")){
						        totdiffintrest=totdiffintrest+diffnextintrest;
						        }
						        
						         totdiffintrest=totdiffintrest+diffintrest;
						        
						       
							
							%>
						
					         <% 
					   //      System.out.println(bean.getRecordCount());
					       if(bean.getRecordCount().equals("Single")){
					       			if(Double.parseDouble(bean.getEmolumentsB())!=0){
				 emolumentsB=Double.parseDouble(bean.getEmolumentsB());
				 }else if(Double.parseDouble(bean.getPensionContriB())==541){
				 emolumentsB=6500;
				 }else if(Double.parseDouble(bean.getPensionContriB())==1250){
				 emolumentsB=15000;}
				 else {
				 emolumentsB=Math.round(Double.parseDouble(bean
												.getPensionContriB())*100/8.33);
				 }
				 totalEmolumentsB=totalEmolumentsB+emolumentsB;
								%>
							<% if(!bean.getEditedDate().trim().equals("N")&& (recoverieTable.equals("true"))){
							
				
							
							%>
         				
         				
          			<tr bgcolor="orange" >
 			 <%
 			 }else{ %>
  			<tr bgcolor="white">
			  <%} %>
							
								<td class="Data" align="center"><%=monthYear%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getEmoluments()))%></td>
								<!--<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getCpf()))%></td>
								--><td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%></td>
								<td class="Data" align="right"><%=Math.round(emolumentsB)%></td>
								<td class="Data" align="right"><%=Math.round(Double.parseDouble(bean.getPensionContriB()))%></td>
								<td class="Data" align="right"><%=Math.round(differ)%></td>
								<!--<td class="Data" nowrap="nowrap"><%=bean.getStation()%></td>
								<td class="Data"><%=bean.getForm7Narration()%></td>
								--><!--<td class="Data"><%=monthy%></td>
								--><!--<td class="Data"><%=diffintrest%></td>
							
							
							--><td class="Data"></td>
							</tr><%}else if(bean.getRecordCount().equals("Duplicate")){%>
								<tr bgcolor="yellow" >
								<td class="Data"  width="12%" align="center"><font color="red"><%=monthYear%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getEmoluments()))%></font></td>
								<!--<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getCpf()))%></font></td>
								--><td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getDbPensionCtr()))%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(Double.parseDouble(bean.getPensionContriB()))%></font></td>
								<td class="Data" width="12%" align="right"><font color="red"><%=Math.round(differ)%></font></td>
								<td class="Data" width="12%"><font color="red"><%=bean.getStation()%></font></td>
								<td class="Data" width="12%"><font color="red"><%=bean.getForm7Narration()%></font></td><!--
							    <td class="Data"><%=monthy%></td>
							    --><!--<td class="Data"><%=Math.round(diffintrest)%></td>
							
							--><td class="Data"></td>
							
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
								<!--<td class="HighlightData" align="right"><%=df.format(pfStaturary)%></td>
								--><td class="HighlightData" align="right"><%=df.format(totalPension)%></td>
								<td class="HighlightData" align="right"><%=df.format(totalEmolumentsB)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfContribution)%></td>
								<td class="HighlightData" align="right"><%=df.format(diffContri)%></td>
								<td class="HighlightData">---</td>
								<!--
								<td class="HighlightData">
								<td class="HighlightData" align="right"><%=df.format(totdiffintrest)%></td>
							
							
							--></tr>
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
								//diffContriInt=pensionInterest-pfContributionInterest;
								
								diffContriInt=totdiffintrest;
								
								
								%>
								
						
							
							
								<td class="HighlightData" align="center">Interest(<%=intRate%>%)</td>
								<td class="HighlightData" align="right" >---</td>
								<td class="HighlightData" align="right">---</td>
								<td class="HighlightData" align="right">---</td>
							
								<td class="HighlightData" align="right">---</td>
								<td class="HighlightData" align="right"><%=df.format(diffContriInt)%></td>
								<td class="HighlightData">---</td>
								
						
							
							
							
							
							
							</tr>
							<tr>
								<%
									flag=false;
									openFlag=true;
									noofmonths=0;
									pensionInterest=0;
									cpfOpeningBalance=Math.round(pfStaturary+cpfInterest+Math.round(cpfOpeningBalance));
									penOpeningBalance=Math.round(totalPension);
									pfOpeningBalance=Math.round(pfContribution);
									//pfOpeningBalance=Math.round(pfContribution+pfContributionInterest+Math.round(pfOpeningBalance));
									//diffContriOB=penOpeningBalance-pfOpeningBalance;
									totalEmoluments=Math.round(totalEmoluments);
									totalEmolumentsB=Math.round(totalEmolumentsB);
									diffContriOB=diffContriOB+Math.round((diffContri+diffContriInt));
									
									
								%>
								<% 
								totalint=totalint+Math.round(diffContriInt);
							%>
								
								<td class="HighlightData" align="center">CL BAL</td>
								<td class="HighlightData" align="right" ><%=df.format(totalEmoluments)%></td>
							
								<td class="HighlightData" align="right"><%=df.format(penOpeningBalance)%></td>
								<td class="HighlightData" align="right"><%=df.format(totalEmolumentsB)%></td>
								<td class="HighlightData" align="right"><%=df.format(pfOpeningBalance)%></td>
								<td class="HighlightData" align="right"><%=df.format(diffContriOB)%></td>
							
																
								<td class="HighlightData"><%=df.format(totdiffintrest)%></td>
								
								
								
							</tr><!--
						<tr>	
						<td class="HighlightData">venki   <%=df.format(totalint)%></td>
							</tr>
							
							
							--><%
							totdiffintrest=0;
							
							grandEmoluments=grandEmoluments+totalEmoluments;
							grandCPF=grandCPF+pfStaturary;
							grandPension=grandPension+totalPension;
							grandPFContribution=grandPFContribution+pfContribution;
							grandEmolumentsB=grandEmolumentsB+totalEmolumentsB;
							
							grandCPFInterest=grandCPFInterest+cpfInterest;
							grandPensionInterest=grandPensionInterest+pensionInterest;
							grandPFContributionInterest=grandPFContributionInterest+pfContributionInterest;
							cumPFStatury=0.0;cumPension=0.0;cumPfContribution=0.0;
							totalEmoluments=0; totalEmolumentsB=0;pfStaturary=0;totalPension=0;pfContribution=0;
							cpfInterest=0;pensionInterest=0;pfContributionInterest=0;
							grandDiffContri=grandPension-grandPFContribution;
							grandDiffContriInt=grandPensionInterest-grandPFContributionInterest;
							
							
							}%>
							<%	  	dispYearFlag=false;}}%>
							
							
						
						
						
							<tr>
								
							<td class="HighlightData" align="center">OB 2016-2017</td>
								
								<td align="right" class="Data" colspan="6" ><%=df.format(diffContriOB)%></td>
							</tr>
							<%
							  	newintrest=(diffContriOB*8.65/100);
								
								%>
							
							<td class="HighlightData" align="center">Intrest 2016-2017 (8.65%)</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(newintrest)%></td>
							</tr>
							
							
								<tr>
								
							<%
							  
								obnewbal=(newintrest+diffContriOB);
							
							
								%>
							
							
							<td class="HighlightData" align="center">Closing Balance</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(obnewbal)%></td>
								
							</tr>
							<tr>
							
							<td class="HighlightData" align="center">OB 2017-2018							</td>
								
								<td colspan="6"  class="Data" align="right" ><%=df.format(obnewbal)%></td>
								
							</tr>
							
							
							<%
							  
								newintrest2=(obnewbal*8.55/100);
							
							
								%>
							
							
							<td class="HighlightData" align="center">Intrest 2017-2018 (8.55%)</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(newintrest2)%></td>
								
							</tr>
							
							
									<tr>
								
							<%
							  
								obnewbal2=(newintrest2+obnewbal);
							
							
								%>
							
							
							<td class="HighlightData" align="center">Closing Balance</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(obnewbal2)%></td>
								
							</tr>
							
							<tr>
							
							<td class="HighlightData" align="center">OB 2018-2019							</td>
								
								<td colspan="6"  class="Data" align="right" ><%=df.format(obnewbal2)%></td>
								
							</tr>
							
							
							
							<%
							  
								newintrest3=(obnewbal2*8.65/100);
							
							
								%>
							
							
							<td class="HighlightData" align="center">Intrest 2018-2019 (8.65%)</td>
								
								<td colspan="6" class="Data"  align="right" ><%=df.format(newintrest3)%></td>
								
							</tr>
							
							
							
							
							<%
							  
								obnewbal3=(newintrest3+obnewbal2);
							
							
								%>
							
							
							<td class="HighlightData" align="center">Closing Balance</td>
								
								<td colspan="6" class="Data"  align="right" ><%=df.format(obnewbal3)%></td>
								
							</tr>
							
							<tr>
							
							<td class="HighlightData" align="center">OB 2019-2020							</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(obnewbal3)%></td>
								
							</tr>
							
							
							
									<%
							  
								newintrest4=(obnewbal3*8.65/100*8/12);
							
							
								%>
							<tr>
							
							<td class="HighlightData" align="center">Intrest 2019-2020 (8.65%)</td>
								
								<td colspan="6" class="Data"  align="right" ><%=df.format(newintrest4)%></td>
								
							</tr>
							
								<%
							  
								obnewbal4=(newintrest4+obnewbal3);
							
							
								%>
							<tr>
							
							<td class="HighlightData" align="center">Closing Balance</td>
								
								<td colspan="6" class="Data" align="right" ><%=df.format(obnewbal4)%></td>
								
							</tr>
							
							
							
								<%
								grandtotintrest=Math.round(totalint+newintrest+newintrest4+newintrest2+newintrest3);
								%>
							
							
							
							
							
							<tr>
								<td colspan="10" align="left">
									<table align="left" width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="gray">
							<tr>
								<td class="HighlightData"></td>
								<td class="HighlightData">Emolument</td>
								<!--<TD CLASS="HIGHLIGHTDATA">CPF</TD>
								<TD CLASS="HIGHLIGHTDATA">INTEREST</TD>
								--><td class="HighlightData">Pension Contribution (Full Pay)</td>
								<!--<td class="HighlightData">Interest</td>
								<td class="HighlightData">Emolument</td>
								--><td class="HighlightData">Ceiling  Wage</td>
								<td class="HighlightData">Pension Contribution  (Ceiling)</td>
								<td class="HighlightData">Pension Contribution Difference</td>
								<td class="HighlightData">Interest</td>
							
							</tr>
							<!--
							<tr>
								<td class="HighlightData" align="center">Grand  Total of <%=count%> months </td>
								<td class="HighlightData"></td>
								<td class="HighlightData" align="right"><%=df.format(grandCPF)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandCPFInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPension)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPensionInterest)%></td>
								<td class="HighlightData"></td>
								<td class="HighlightData" align="right"><%=df.format(grandPFContribution)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPFContributionInterest)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandDiffContri)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandDiffContriInt)%></td>
								
							</tr>
							--><tr>
								<td class="HighlightData" align="center">Grand Total</td>
								<td class="HighlightData" align="right"><%=df.format(grandEmoluments)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPension)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandEmolumentsB)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandPFContribution)%></td>
								<td class="HighlightData" align="right"><%=df.format(grandDiffContri)%></td>
							<td class="HighlightData"  colspan="1" align="right"><%=df.format(grandtotintrest)%></td>
								
							</tr>
					<% Test t =new Test();
							try{
							t.insertPCdata(Integer.parseInt(empSerialNo),"2019-2020",grandEmoluments,grandPension,grandEmolumentsB,grandPFContribution,grandDiffContri,grandtotintrest);
							}catch(Exception e){
							e.printStackTrace();
							}
							%>
    </table></td>
							</tr>
<tr>
	 <td colspan="10"><table width="100%" border="0" cellspacing="0" cellpadding="0">
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
             <td class="label"></td>
             </tr>
  
          <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2"></td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2"></td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2"></td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2"></td>
            </tr>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;" colspan="2">
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
