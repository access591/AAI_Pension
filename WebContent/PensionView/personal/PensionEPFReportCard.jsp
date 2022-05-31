
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String userId = session.getAttribute("userid").toString();
	String signature= request.getAttribute("signature").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <script type="text/javascript">
    
    
	function showTip(txt,element){
	    var theTip = document.getElementById("spnTip");
			theTip.style.top= GetTop(element);
	   //alert(theTip.style.top);
	    if(txt=='')
	    {
	    txt='--';
	    }
	    theTip.innerHTML=""+txt;
		theTip.style.left= GetLeft(element) - theTip.offsetWidth;
	    
	    theTip.style.visibility = "visible";
	}

function hideTip()
{
	document.getElementById("spnTip").style.visibility = "hidden";
} 
	
function GetTop(elm){

	var  y = 0;
	y = elm.offsetTop;
	elm = elm.offsetParent;
	while(elm != null){
		y = parseInt(y) + parseInt(elm.offsetTop);
		elm = elm.offsetParent;
	}	
	return y;
}
function GetLeft(elm){

	var x = 0;
	x = elm.offsetLeft;
	elm = elm.offsetParent;
	while(elm != null){
		x = parseInt(x) + parseInt(elm.offsetLeft);
		elm = elm.offsetParent;
	}
	
	return x;
}	
 function high(obj)
 	{
	//obj.style.background = 'rgb(220,232,236)';
	}

function low(obj) {
	///obj.style.background='#EFEFEF';	
}
</script>
    <title>AAI</title>
    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <body>
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   			ArrayList cardReportList=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="";
	  	String arrearInfo="",orderInfo="";
	  	int size=0,finalInterestCal=0,noOfmonthsForInterest=0,noOfAfterfnlrtmntmonhts=0;

	  	CommonUtil commonUtil=new CommonUtil();
	  	if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="",pfidString="",chkBulkPrint="",pfcardNarration="",chkRegionString="",chkStationString="",dispSignStation="",dispSignPath="";
 		String mangerName="";
    	boolean check=false,signatureFlag=false;
	   
    FinancialReportService reportService=new FinancialReportService();
    if(request.getAttribute("region")!=null){
    	chkRegionString=(String)request.getAttribute("region");
    }
    if(request.getAttribute("airportCode")!=null){
      chkStationString=(String)request.getAttribute("airportCode");
    }
    if(request.getAttribute("blkprintflag")!=null){
      chkBulkPrint=(String)request.getAttribute("blkprintflag");
    }
  
    if(request.getAttribute("dspYear")!=null){
    dispYear=(String)request.getAttribute("dspYear");
    }
    if(signature.equals("true")){
    	if(userId.equals("SRFIN")){
    		signatureFlag=true;
			dispSignStation="South Region";
			dispDesignation="Deputy General Manager(F&A)";
			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
    	}else if(userId.equals("CHQFIN")){
    		signatureFlag=true;
			dispSignStation="CHQ";
			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
			dispDesignation="Assistant Manager(F)/Manager(F)";
    	}else if(userId.equals("CHQFIN")){
    		signatureFlag=true;
			dispSignStation="CHQ";
			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
			dispDesignation="Assistant Manager(F)/Manager(F)";
    	}else if(userId.equals("NERFIN")){
    		signatureFlag=true;
			dispSignStation="";
			mangerName="(G.S Mohapatra)";
 			dispDesignation="Joint General Manager(Fin), AAI, NER,Guwahati";
			dispSignPath=basePath+"PensionView/images/signatures/G.SMohapatra.gif";
    	}else if(userId.equals("WRFIN")){
    		signatureFlag=true;
 			dispSignStation="";
 			mangerName="(Shri S H Kaswankar)";
 			dispDesignation="Sr. Manager(Fin), AAI, WR, Mumbai";
 			dispSignPath=basePath+"PensionView/images/signatures/Kaswankar.gif";	
    	}else if(userId.equals("NRFIN")){
    		signatureFlag=true;
			dispSignStation="";
			mangerName="(Anil Kumar Jain)";
 			dispDesignation="Asstt.General Manager(Fin), AAI, NR";
			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
    	}else if(userId.equals("SAPFIN")){
    		signatureFlag=true;
 			dispSignStation="";
 			mangerName="(Monika Dembla)";
 			dispDesignation="Manager(F & A), AAI, RAU,SAP ";
 			dispSignPath=basePath+"PensionView/images/signatures/Monika Dembla.gif";
    	}else if(userId.equals("IGIFIN")){
    		signatureFlag=true;
				dispSignStation="";
				mangerName="(Arun Kumar)";
				dispDesignation="Sr. Manager(F&A), AAI,IGICargo IAD";
				dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";
    	}else if(userId.equals("ERFIN")){
			signatureFlag=true;
			dispSignStation="Kolkata-700 052";
			dispSignPath=basePath+"PensionView/images/signatures/JBBISWAS.gif";
			dispDesignation="Asstt.General Manager(Fin.),A.A.I.,NSCBI Airport";
		}else if(userId.equals("NSCBFIN")){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
    	
    }
   if(chkBulkPrint.equals("true")){
   	   if(dispYear.equals("2008-09")){
   			 if(chkRegionString.equals("CHQNAD")){
    			signatureFlag=true;
    			dispSignStation="CHQ";
    			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
    			dispDesignation="Assistant Manager(F)/Manager(F)";
    		 }else if(chkRegionString.equals("North Region")){
    			signatureFlag=true;
    			dispSignStation="";
    			mangerName="(Anil Kumar Jain)";
     			dispDesignation="Asstt.General Manager(Fin), AAI, NR";
    			dispSignPath=basePath+"PensionView/images/signatures/AKJain.gif";
    		 }else if(chkRegionString.equals("North-East Region")){
    			signatureFlag=true;
    			dispSignStation="";
    			mangerName="(G.S Mohapatra)";
     			dispDesignation="Joint General Manager(Fin), AAI, NER,Guwahati";
    			dispSignPath=basePath+"PensionView/images/signatures/G.SMohapatra.gif";
    		 }else if(chkRegionString.equals("South Region")){
    			signatureFlag=true;
    			dispSignStation="South Region";
    			dispDesignation="Deputy General Manager(F&A)";
    			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
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
    			if(chkStationString.toLowerCase().equals("office complex")){
    				signatureFlag=true;
    				dispSignStation="Office Complex";
    				dispDesignation="Assistant Manager(F)/Manager(F)";
    				dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
    			}else if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
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
    		 	}else if(chkStationString.toLowerCase().equals("KOLKATA".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
    		}
   		}else if(dispYear.equals("2009-10")){
   			if(chkRegionString.equals("East Region")){
    			signatureFlag=true;
    			dispSignStation="Kolkata-700 052";
    			dispSignPath=basePath+"PensionView/images/signatures/JBBISWAS.gif";
    			dispDesignation="Asstt.General Manager(Fin.),A.A.I.,NSCBI Airport";
    		}else if(chkRegionString.equals("CHQNAD")){
     			signatureFlag=true;
     			dispSignStation="CHQ";
     			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			dispDesignation="Sr. Manager(Finance)";
     		 }else if(chkRegionString.equals("North Region")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(Arun Kumar)";
      			dispDesignation="Sr. Manager(F&A), AAI, NR";
     			dispSignPath=basePath+"PensionView/images/signatures/IAD_Arun Kumar.gif";
     		 }else if(chkRegionString.equals("South Region")){
     			signatureFlag=true;
     			dispSignStation="South Region";
     			dispDesignation="Deputy General Manager(F&A)";
     			dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
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
     			if(chkStationString.toLowerCase().equals("office complex")){
     				signatureFlag=true;
     				dispSignStation="Office Complex";
     				dispDesignation="Sr. Manager(Finance)";
     				dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			}else if(chkStationString.toLowerCase().equals("KOLKATA PROJ".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(B.Karmakar)";
      				dispDesignation="Sr. Manager(Fin)";
      				dispSignPath=basePath+"PensionView/images/signatures/karmakar.gif";
      			}else if(chkStationString.toLowerCase().equals("TRIVANDRUM IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(Sajisam)";
      				dispDesignation="Sr. Manager(F&A), AAI,TRIVANDRUM AIRPORT";
      				dispSignPath=basePath+"PensionView/images/signatures/sajsam.gif";
     			}else if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
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
      			}else if(chkStationString.toLowerCase().equals("KOLKATA".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Sr. Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   		}
     		}
    		
   		}
   }

   
	  	cardReportList=(ArrayList)request.getAttribute("cardList");
	  	EmployeeCardReportInfo cardReport=new EmployeeCardReportInfo();
	  	size=cardReportList.size();
	  	int intMonths=0,arrearMonths=0;
	  	if (request.getAttribute("reportType") != null) {
			reportType = (String) request.getAttribute("reportType");
			if (reportType.equals("Excel Sheet")
							|| reportType.equals("ExcelSheet")) {
					
						fileName = "PF_CARD_Report_FYI("+dispYear+").xls";
						response.setContentType("application/vnd.ms-excel");
						response.setHeader("Content-Disposition",
								"attachment; filename=" + fileName);
					}
		}
	  	if(size!=0){
	  	ArrayList dateCardList=new ArrayList();
	  	ArrayList dataPTWList=new ArrayList();
	  	ArrayList dataFinalSettlementList=new ArrayList();
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  		
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		System.out.println("PF ID String"+personalInfo.getPfIDString()+"Adj Date"+personalInfo.getAdjDate());
		dateCardList=cardReport.getPensionCardList();
		dataPTWList=cardReport.getPtwList();
		noOfmonthsForInterest=cardReport.getNoOfMonths();
		intMonths=cardReport.getInterNoOfMonths();
		arrearMonths=cardReport.getArrearNoOfMonths();
		dataFinalSettlementList=cardReport.getFinalSettmentList();
        arrearInfo=cardReport.getArrearInfo();
        orderInfo=cardReport.getOrderInfo();
        String[] arrearData=arrearInfo.split(",");
        double arrearAmount=0.00,arrearContri=0.00;
        String arrearDate="";
        arrearDate=arrearData[0];
        arrearAmount=Double.parseDouble(arrearData[2]);
        arrearContri=Double.parseDouble(arrearData[3]);
       
   %>
   <tr>
   <td>
   <table width="100%" height="490" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   
    <td width="120" rowspan="3" align="center"><img src="<%=basePath%>PensionView/images/logoani.gif" width="88" height="50" align="right" /></td>
    <td class="reportlabel" nowrap="nowrap">AIRPORTS AUTHORITY OF INDIA</td>
    	<td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
     	<td width="96">&nbsp;</td>
     	<td width="95">&nbsp;</td>
     	<td width="85">&nbsp;</td>
  	 	<td width="384"  class="reportlabel">Employee's Provident Fund Trust</td>
  	 	<td width="87">&nbsp;</td>
    	<td width="272">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="3" class="reportlabel" style="text-decoration: underline" align="center">EPF &amp; PENSION CONTRIBUTION CARD </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   

    <td colspan="3" align="center" nowrap="nowrap" class="reportsublabel">FOR THE YEAR <font style="text-decoration: underline"><%=dispYear%></td>
    <td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td>
    
  </tr>
  <tr>
    <td colspan="7">&nbsp;</td>
  </tr>
  <%


  %>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td width="48%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="36%">Pf Id:</td>
            <td width="64%" class="Data"><%=personalInfo.getPfID()%></td>
          </tr>
		    <tr>
            <td class="reportsublabel">Name:</td>
            <td class="Data"><%=personalInfo.getEmployeeName()%></td>
          </tr>
		     <tr>
            <td class="reportsublabel">DATE OF BIRTH:</td>
            <td class="Data"><%=personalInfo.getDateOfBirth().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">DATE OF JOINING:</td>
            <td class="Data"><%=personalInfo.getDateOfJoining().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">DATE OF RETIRE:</td>
            <td class="Data"><%=personalInfo.getDateOfAnnuation().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">Pension Option</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getWhetherOptionDescr())%></td>
          </tr>
        </table></td>
        <td width="52%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="49%" class="reportsublabel">CPF Acc.No(Old):</td>
            <td width="51%" class="Data"><%=personalInfo.getCpfAccno().toUpperCase()%></td>
          </tr>
          <tr>
            <td width="49%" class="reportsublabel">EMP No:</td>
            <td width="51%" class="Data"><%=personalInfo.getEmployeeNumber().toUpperCase()%></td>
          </tr>
		  <tr>
            <td class="reportsublabel">Designation:</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getDesignation())%></td>
          </tr>
		       <tr>
            <td nowrap="nowrap" class="reportsublabel">Father/ Husband'S Name:</td>
            <td class="Data"><%=commonUtil.convertToLetterCase(personalInfo.getFhName())%></td>
          </tr>
		   <tr>
            <td class="reportsublabel">Gender:</td>
            <td class="Data"><%=personalInfo.getGender().toUpperCase()%></td>
          </tr>
		    <tr>
            <td class="reportsublabel">Marital Status:</td>
            <td><%=commonUtil.convertToLetterCase(personalInfo.getMaritalStatus())%></td>
          </tr>
		    <tr>
            <td nowrap="nowrap" class="reportsublabel">Date Of Pension Membership:</td>
            <td class="Data"><%=personalInfo.getDateOfEntitle().toUpperCase()%></td>
          </tr>
        </table></td>
      </tr>
	  <tr>
	  		<td colspan="2">
	  			<table border="0" cellpadding="1" cellspacing="1">
	  				<tr>
	  					<td class="reportsublabel">Nominee Info</td>
	  					<%
	  						ArrayList nomineeList=new  ArrayList();
	  						nomineeList=personalInfo.getNomineeList();
	  						NomineeBean nomineeBean=new NomineeBean();
	  						for(int nl=0;nl<nomineeList.size();nl++){
	  						nomineeBean=(NomineeBean)nomineeList.get(nl);
	  					%>
	  					<td class="Data"><%=nomineeBean.getSrno()+". "+nomineeBean.getNomineeName()+" ("+nomineeBean.getNomineeRelation()+") "%></td>
	  					<%}%>
	  				</tr>
	  				
	  			</table>
	  		</td>
	  </tr>
    </table></td>
  </tr>

  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2">&nbsp;</td>
        <td colspan="7" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
        <td colspan="3" align="center" class="label">AAI CONTRIBUTION</td>
        <td width="8%">&nbsp;</td>
        <td width="5%">&nbsp;</td>
        <td width="5%">&nbsp;</td>
      </tr>
      <tr>
        <td width="4%" rowspan="2" class="label">Month</td>
        <td width="8%" rowspan="2" class="label">Emolument</td>
        <td width="8%" rowspan="2" class="label"><div align="center">EPF</div></td>
        <td width="3%" rowspan="2" class="label"><div align="center">VPF</div></td>
        <td colspan="2"><div align="center" class="label">Refund Of ADV./PFW </div></td>
        <td width="6%" rowspan="2"  class="label">TOTAL </td>
        <td width="5%" rowspan="2" class="label">Advance<br/>PFW PAID</td>
        <td width="3%" class="label">NET </td>
      
        <td width="3%" align="center" rowspan="2" class="label">AAI<br/>PF</td>
        <td width="6%" class="label" rowspan="2">PFW<br/>DRAWN</td>
        <td width="3%" class="label">NET</td>
        <td width="12%" class="label" rowspan="2">PENSION<br/>CONTR. </td>
        <td rowspan="2" class="label">Station</td>
        <td rowspan="2" class="label">Remarks</td>
      </tr>
      <tr>
       
        <td width="5%" class="label"><div align="center">Principal</div></td>
        <td width="7%" class="label"><div align="center">Interest</div></td>
        <td>(7-8)</td>
      
        <td class="label" >(10-11)</td>
       
      </tr>
      <tr>
        <td>1</td>
        <td>2</td>
        <td class="Data">3</td>
        <td class="Data">4</td>
        <td class="Data">5</td>
        <td class="Data">6</td>
        <td class="Data">7</td>
        <td class="Data">8</td>
        <td class="Data">9</td>
        <td class="Data">10</td>
        <td class="Data">11</td>
        <td class="Data">12</td>
        <td class="label">13</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    
        <%
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	double totalEmoluments=0.0,pfStaturary=0.0,empVpf=0.0,principle=0.0,interest=0.0,empTotal=0.0,totalEmpNet=0.0,totalEmpIntNet=0.0,dispTotalEmpNet=0.0,dispTotalAAINet=0.0,totalAaiIntNet=0.0;
       	double totalAAIPF=0.0,totalPFDrwn=0.0,totalAAINet=0.0,advancePFWPaid=0.0,empNetInterest=0.0,aaiNetInterest=0.0,totalPensionContr=0.0,pensionInterest=0.0,arrearEmpNetInterest=0.0,arrearAaiNetInterest=0.0;
       	double totalAdvances=0,totalGrandArrearEmpNet=0.0,totalGrandArrearAaiNet=0.0;
       	double totalArrearEmpNet=0.0,totalArrearAAINet=0.0;
       	String arrearFlag="N",arrearRemarks="---",adjNarration="";
  		EmployeePensionCardInfo pensionCardInfo=new EmployeePensionCardInfo();
		Long empNetOB=null,aaiNetOB=null,pensionOB=null,principalOB=null;
		Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null,adjOutstandadv=null;
		double rateOfInterest=0;
  		String obFlag="",calYear="";
  		boolean closeFlag=false;
  		ArrayList obList=new ArrayList();
  		ArrayList closingOBList=new ArrayList();
  		
  		if(dateCardList.size()!=0){
  		for(int i=0;i<dateCardList.size();i++){
  		pensionCardInfo=(EmployeePensionCardInfo)dateCardList.get(i);
  		obFlag=pensionCardInfo.getObFlag();
  		if(obFlag.equals("Y")){
  			 calYear=commonUtil.converDBToAppFormat(pensionCardInfo.getShnMnthYear(),"MMM-yy","yyyy");
	  		 obList=pensionCardInfo.getObList();
	  		 empNetOB=(Long)obList.get(0);
           	 aaiNetOB=(Long)obList.get(1);
             pensionOB=(Long)obList.get(2);
             adjNarration=(String)obList.get(11);
             principalOB=(Long)obList.get(12);
             advancePFWPaid=0.0;
             totalPFDrwn=0.0;
             if(obList.size()>3){
             cpfAdjOB=(Long)obList.get(5);
           	 pensionAdjOB=(Long)obList.get(6);
             pfAdjOB=(Long)obList.get(7);
             empSubOB=(Long)obList.get(8);
             adjPensionContri=(Long)obList.get(9);
             adjOutstandadv=(Long)obList.get(10);
             }
          
  		}
  		String adjStation=pensionCardInfo.getStation();
  		
  		//System.out.println("final settlement date" +personalInfo.getFinalSettlementDate());
  		//System.out.println("pensionCardInfo.getTransArrearFlag()" +pensionCardInfo.getTransArrearFlag()+"personalInfo.getChkArrearFlag()"+personalInfo.getChkArrearFlag().equals("N"));
  		if(((Integer.parseInt(calYear)>=2010 && dataFinalSettlementList.size()==0) && pensionCardInfo.getTransArrearFlag().equals("Y"))||pensionCardInfo.getTransArrearFlag().equals("N")||(pensionCardInfo.getTransArrearFlag().equals("Y")&&(personalInfo.getChkArrearFlag().equals("N")))){
  		totalEmoluments= new Double(df.format(totalEmoluments+Math.round(Double.parseDouble(pensionCardInfo.getEmoluments())))).doubleValue();
		pfStaturary= new Double(df.format(pfStaturary+Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury())))).doubleValue();
		empVpf = new Double(df.format(empVpf+Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf())))).doubleValue();
		principle =new Double(df.format(principle+Math.round(Double.parseDouble(pensionCardInfo.getPrincipal())))).doubleValue();
		interest =new Double(df.format(interest+Math.round(Double.parseDouble(pensionCardInfo.getInterest())))).doubleValue();
		empTotal=new Double(df.format(empTotal+Math.round(Double.parseDouble(pensionCardInfo.getEmptotal())))).doubleValue();
	    advancePFWPaid= new Double(df.format(advancePFWPaid+Math.round( Double.parseDouble(pensionCardInfo.getAdvancePFWPaid())))).doubleValue();
	    if(!pensionCardInfo.getGrandCummulative().equals("")){
	    		if(pensionCardInfo.isYearFlag()==true){
	    		totalEmpNet=totalEmpNet+new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();
	    
	    		}else{
	    		totalEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();
	    
	    		}
	    	    }
	

	   
	   	dispTotalEmpNet= new Double(df.format(dispTotalEmpNet+Math.round( Double.parseDouble(pensionCardInfo.getEmpNet())))).doubleValue();
	   	totalAdvances=new Double(df.format(totalAdvances+Math.round( Double.parseDouble(pensionCardInfo.getAdvancesAmount())))).doubleValue();
	   	
	    totalAAIPF=new Double(df.format(totalAAIPF+Math.round(Double.parseDouble(pensionCardInfo.getAaiPF())))).doubleValue();
	    totalPFDrwn= new Double(df.format(totalPFDrwn+Math.round( Double.parseDouble(pensionCardInfo.getPfDrawn())))).doubleValue();
	    if(!pensionCardInfo.getGrandCummulative().equals("")){
	    if(pensionCardInfo.isYearFlag()==true){
	     totalAAINet=totalAAINet+new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandAAICummulative()))).doubleValue();
	   
	    }else{
	      totalAAINet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandAAICummulative()))).doubleValue();
	   
	    }
	    	   }
	   
	
	   // totalAAINet= new Double(df.format(totalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiCummulative())))).doubleValue();
	    dispTotalAAINet= new Double(df.format(dispTotalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiNet())))).doubleValue();
	    
	    totalPensionContr=new Double(df.format(totalPensionContr+Math.round( Double.parseDouble(pensionCardInfo.getPensionContribution())))).doubleValue();
		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
			 arrearRemarks="ARREARS";
		}else   arrearRemarks="---";
	     
  		}else{
  				 totalArrearEmpNet=totalArrearEmpNet+Math.round( Double.parseDouble(pensionCardInfo.getEmpNet()));
  				 totalArrearAAINet=totalArrearAAINet+Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()));
       			 arrearFlag="Y";
       			 arrearRemarks="ARREARS";
  		}
  			    if(!pensionCardInfo.getGrandArrearEmpNetCummulative().equals("")){
	    	
	    	totalGrandArrearEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearEmpNetCummulative()))).doubleValue();
    	}
  		    if(!pensionCardInfo.getGrandArrearAAICummulative().equals("")){
	    	
	    	totalGrandArrearAaiNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearAAICummulative()))).doubleValue();
    	} 
  		  if(pensionCardInfo.getMergerflag().equals("Y") ){
      		if(!arrearRemarks.equals("---")){
      		arrearRemarks=arrearRemarks+","+pensionCardInfo.getMergerremarks();
      		}else{
      			arrearRemarks=pensionCardInfo.getMergerremarks();
      		}
  			
  		}
  		pfcardNarration = pensionCardInfo.getPfcardNarration();
  			if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y")){
  			arrearRemarks = arrearRemarks+","+pfcardNarration;
  			}
  	%>
 <span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
  	<%if(obFlag.equals("Y")){%>
  	  <tr>
        <td colspan="2" class="label">OPENING BALANCE (OB)</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=principalOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=empNetOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=aaiNetOB%></td>
         <td class="NumData">0</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
     
      </tr>
  <% System.out.println("===========totalEmpNet"+totalEmpNet+"totalAAINet"+pensionAdjOB);%>
       <tr>
        <td colspan="2" class="label">ADJ  IN OB</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=adjOutstandadv%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td class="NumData"><%=empSubOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
       
<% 
     if(!pensionAdjOB.equals("0")&& Integer.parseInt(pensionAdjOB.toString())!=0 && (dispYear.equals("2009-10")||dispYear.equals("2010-11"))){%>
 <td class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./search1?method=getEmolumentslog&pfId=<%=personalInfo.getPfID()%>&airportcode=<%=adjStation%>&dispYear=<%=dispYear%>"><%=pensionAdjOB%></a></td>
 <%} else{%>
 <td class="NumData"><%=pensionAdjOB%></td>
<%} %>
        <td class="NumData"><%=adjPensionContri%></td>
        <td>&nbsp;</td>
           <%if (!adjNarration.equals("---")&& !adjNarration.trim().equals("")){%>
	   <td width="2%" class="Data"  onmouseover="showTip('<%=adjNarration%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=adjNarration.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="Data">&nbsp;</td>
	 <%}%>
       
       
      </tr>
      <%}%>
	 <tr>
	 
	 <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getShnMnthYear()%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf()))%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPrincipal()))%></td>
	 <td width="7%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getInterest()))%></td>
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmptotal()))%></td>
	  <td width="2%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAdvancePFWPaid()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpNet()))%></td>
	
 	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiPF()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPfDrawn()))%></td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()))%></td>

	 <td width="12%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%></td>
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getStation()%></td>
	 
	  <%if (!arrearRemarks.equals("---")){%>
	   <td width="2%" class="Data"  onmouseover="showTip('<%=arrearRemarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=arrearRemarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="Data"><%=arrearRemarks %></td>
	 <%}%>
	 </tr>
	
	 <%
	  
		
	 	if(pensionCardInfo.getCbFlag().equals("Y")){
	 		Long finalEmpNetOB=null,finalAaiNetOB=null,finalPensionOB=null,finalPrincipalOB=null,finalStlmentEmpNet=null,finalStlmentNetAmount=null,finalStlmentAAICon=null,finalStlmentPensDet=null;
	 		
	 		Long finalEmpNetOBIntrst=null,finalAaiNetOBIntrst=null,finalPensionOBIntrst=null;
	 		
	 		if(obList.size()!=0){
	 				if(Integer.parseInt(calYear)>=1995 && Integer.parseInt(calYear)<2000){
		 				rateOfInterest=12;
	 				}else if(Integer.parseInt(calYear)>=2000 && Integer.parseInt(calYear)<2001){
	 					rateOfInterest=11;
	 				}else if(Integer.parseInt(calYear)>=2001 && Integer.parseInt(calYear)<2005){
	 					rateOfInterest=9.50;
	 				}else if(Integer.parseInt(calYear)>=2005 && Integer.parseInt(calYear)<2010){
	 					rateOfInterest=8.50;
	 				}else if(Integer.parseInt(calYear)>=2010 && Integer.parseInt(calYear)<2012){
	 					rateOfInterest=9.50;
	 				} 
	 				
	 				 
	 			    empNetInterest=new Double(df.format(((totalEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
				    aaiNetInterest=new Double(df.format(((totalAAINet*rateOfInterest)/100/intMonths))).doubleValue();
				     System.out.println("----rateOfInterest------"+rateOfInterest+"totalEmpNet"+totalEmpNet+"----empNetInterest------"+empNetInterest);
				    
				    if(totalGrandArrearEmpNet!=0.0 && totalGrandArrearAaiNet!=0.0 && personalInfo.isArreerintflag()!=true){
				    arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
				    arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/intMonths))).doubleValue();
				    }else{
				    	arrearEmpNetInterest=0.0;
				    	arrearAaiNetInterest=0.0;
				    }
				      System.out.println("----totalGrandArrearEmpNet------"+totalGrandArrearEmpNet+arrearEmpNetInterest+"totalGrandArrearAaiNet"+totalGrandArrearAaiNet+arrearAaiNetInterest);
				  //   System.out.println("arrearEmpNetInterest"+arrearEmpNetInterest+"arrearAaiNetInterest"+arrearAaiNetInterest+"noOfmonthsForInterest"+noOfmonthsForInterest);
				    //For Pension Contribution attribute,we are not cummlative
				    pensionInterest=new Double(df.format(((totalPensionContr*rateOfInterest)/100))).doubleValue();
				    totalEmpIntNet=new Double(Math.round(empNetInterest+totalEmpNet)).doubleValue();
				    totalAaiIntNet=new Double(Math.round(totalAAINet+aaiNetInterest)).doubleValue();
				   	closingOBList=reportService.calClosingOB(rateOfInterest,obList,Math.round(dispTotalAAINet),Math.round(aaiNetInterest),Math.round(dispTotalEmpNet),Math.round(empNetInterest),Math.round(totalPensionContr),Math.round(pensionInterest),totalAdvances,principle,noOfmonthsForInterest,personalInfo.isObInterst());
				 														
	 				if(closingOBList.size()!=0){
					 	finalEmpNetOB=(Long)closingOBList.get(0);
					 	finalAaiNetOB=(Long)closingOBList.get(1);
					 	finalPensionOB=(Long)closingOBList.get(2);
					 	finalEmpNetOBIntrst=(Long)closingOBList.get(4);
					 	finalAaiNetOBIntrst=(Long)closingOBList.get(5);
					 	finalPensionOBIntrst=(Long)closingOBList.get(6);
					 	finalPrincipalOB=(Long)closingOBList.get(7);
				 	}
	 		}

	 %>
	 
	 <tr>
	 <td  nowrap="nowrap" class="Data">YEAR TOTAL </td>
	 <td  nowrap="nowrap" class="NumData"><%=df1.format(totalEmoluments)%></td>
	 <td width="8%" class="NumData"><%=df1.format(pfStaturary)%></td>
  	 <td width="3%" class="NumData"><%=df1.format(empVpf)%></td>
     <td width="5%" class="NumData"><%=df1.format(principle)%></td>
	 <td width="7%" class="NumData"><%=df1.format(interest)%></td>
	 <td width="6%" nowrap="nowrap" class="NumData"><%=df1.format(empTotal)%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(advancePFWPaid)%></td>
	 <td width="3%"  nowrap="nowrap" class="NumData"><%=df1.format(dispTotalEmpNet)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalAAIPF)%></td>
	 <td width="6%" class="NumData"><%=df1.format(totalPFDrwn)%></td>
	 <td width="3%" class="NumData"><%=df1.format(dispTotalAAINet)%></td>
	 <td width="12%" class="NumData"><%=df1.format(totalPensionContr)%></td>
	  <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 </tr>
	  <tr>
	 <td width="50" nowrap="nowrap" class="label">INTEREST</td>
	 <td width="116" class="NumData"><%=rateOfInterest%></td>

	  <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOBIntrst)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOBIntrst)%></td>
	   <td width="3%" class="NumData" nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalPrincipalOB)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOB)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOB)%></td>
	 
	 <td width="12%" class="NumData"><%=finalPensionOB%></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%
	if(dataFinalSettlementList.size()!=0){
	finalStlmentEmpNet=new Long(Long.parseLong((String)dataFinalSettlementList.get(5)));
	finalStlmentAAICon=new Long(Long.parseLong((String)dataFinalSettlementList.get(6)));
	finalStlmentPensDet=new Long(Long.parseLong((String)dataFinalSettlementList.get(7)));
	finalStlmentNetAmount=new Long(Long.parseLong((String)dataFinalSettlementList.get(8)));
	finalInterestCal=12-noOfmonthsForInterest;
	System.out.println("dataFinalSettlementList"+(String)dataFinalSettlementList.get(8)+"finalStlmentEmpNet=="+finalStlmentEmpNet+"finalStlmentNetAmount"+finalStlmentNetAmount);
	long netcloseEmpNet=(finalEmpNetOB.longValue())+(-finalStlmentEmpNet.longValue());
	long netcloseNetAmount=(finalAaiNetOB.longValue())+(-finalStlmentAAICon.longValue());
	double remaininInt1=arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
	double remainAaiInt1=arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	double remaininInt=0.00,remainAaiInt=0.00;
	noOfAfterfnlrtmntmonhts=Integer.parseInt((String)dataFinalSettlementList.get(13));
	System.out.println("personalInfo.getFinalSettlementDate()"+personalInfo.getFinalSettlementDate()+"Resettlemnt date"+personalInfo.getResttlmentDate()+netcloseEmpNet+"finalInterestCal"+finalInterestCal);
	System.out.println("totalArrearEmpNet"+totalArrearEmpNet+"arrearAaiNetInterest"+arrearAaiNetInterest+netcloseEmpNet);
	if(!personalInfo.getFinalSettlementDate().equals("---")){
			if(!personalInfo.getResttlmentDate().equals("---")){
				 remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			 remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
			}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	
			}
	}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	
	}

 %>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">FINAL SETTLEMENT (<%=(String)dataFinalSettlementList.get(11)%>)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=-finalStlmentEmpNet.longValue()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=-finalStlmentAAICon.longValue()%></td>
	 <td colspan="5" class="Data"><%=orderInfo%></td>
 </tr>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NET CLOSING (A)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=netcloseEmpNet%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=netcloseNetAmount%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
   
 <%
 System.out.println("arrearFlag==================="+arrearFlag);
 if(arrearFlag.equals("Y")){%>
      <tr>
	 <td colspan="2" nowrap="nowrap" class="label">ARREARS AMOUNT (B)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(totalArrearEmpNet)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(totalArrearAAINet)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (C)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt1)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt1)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>

 <%}else{%>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (B)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 	
<%}%> 
<tr>
 <% if(arrearFlag.equals("Y")){%>
	 <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING (A+B+C)</td>
      <%}else{ %>
   <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING(A+B) </td>
     <%} %>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(netcloseEmpNet+remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(netcloseNetAmount+remainAaiInt)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%if(!personalInfo.getAdjDate().equals("---")){%>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label"><%=personalInfo.getAdjRemarks()%> </td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%}%>
   <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <%}else{%>
    <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <%}%>
 <tr>
    <td colspan="15">&nbsp;</td>
 </tr>
  <%totalEmpNet=0;totalEmoluments=0;totalAAIPF=0;pfStaturary=0;totalPFDrwn=0;empTotal=0;totalAAINet=0;totalPensionContr=0;}%>
    </tr>

  <%}}%>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
 
    		
   <%if(signatureFlag==true){%>
  <tr>
    <td colspan="7">
    	<table cellpadding="2" cellspacing="2" align="right">
    		<tr>
    			<td align="center"><img src="<%=dispSignPath%>" /></td>
    		 </tr>
    		 <tr>
    			 <td class="label" align="center"><%=mangerName%></td>
    		 	
    		 </tr>
    		 <tr>
    			<td  class="label" align="center"><%=dispDesignation%></td>
    		</tr>
    		<tr>
    			<td  class="label" align="center"><%=dispSignStation%></td>
    		</tr>
    	</table>
    
    </td>
   
  </tr>
<%}else{%>
	  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
<%}%>

  <tr>
    <td nowrap="nowrap" colspan="6" class="label">RATE OF SUBSCRIPTION   12.00%</td>
    
  </tr>
   <tr>
    <td nowrap="nowrap" colspan="6" class="label">RATE OF INTEREST&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=rateOfInterest%>%</td>
    
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
 
  <tr>
    <td colspan="7"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="53%"><table width="100%" border="1" cellspacing="0" cellpadding="0">
          <tr>
            <td colspan="5" class="reportsublabel"><div align="center">DETAILS OF PART FINAL WITHDRAWAL (PFW)</div></td>
            </tr>
          <tr>
            <td class="label">Sl No</td>
            <td class="label">Purpose</td>
            <td class="label">Date</td>
            <td class="label">Amount</td>
         
          </tr>
          <%
          		PTWBean ptwInfo=new PTWBean();
          		int count=0;
	          for(int k=0;k<dataPTWList.size();k++){
	          count++;
	          ptwInfo=(PTWBean)dataPTWList.get(k);
          %>
          <tr>
            <td class="label"><%=count%></td>
            <td class="label"><%=ptwInfo.getPtwPurpose()%></td>
            <td><%=ptwInfo.getPtwDate()%></td>
            <td><%=ptwInfo.getPtwAmount()%></td>
           
          </tr>
         <%}%>
        </table></td>
        <td width="47%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td class="label">NOTE:-</td>
             </tr>
           <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">1. CREDITS INCLUDES MARCH SALARY PAID IN APRIL TO FEBRUARY SALARY PAID IN MARCH.ADVANCES/PFW SHOWN IN THE MONTH IT IS PAID.</td>
            </tr>
          <tr>
            <td>&nbsp;</td>
            </tr>
          <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">2. IN CASE OF ANY DISCREPANCY IN THE BALANCES SHOWN ABOVE THE MATTER MAY BE BROUGHT TO THE NOTICE OF THE CPF CELL WITHIN 15 DAYS OF ISSUE OF THE STATEMENT, OTHERWISE THE BALANCES WOULD BE PRESUMED TO HAVE BEEN CONFIRMED.</td>
            </tr>
          <tr>
            <td>&nbsp;</td>
            </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
  <%if (!arrearDate.equals("NA")){%>
    <tr>
    <td colspan="7">
    	<table width="53%" border="1" cellspacing="0" cellpadding="0">
     		<tr>
            		
            		<td class="label">Arrear Date</td>
            		<td class="label">Arrear Amount</td>
            		<td class="label">Arrear Contribution</td>
         
          </tr>
         		<tr>
            		
            		<td class="Data"><%=arrearDate%></td>
            		<td class="Data"><%=arrearAmount%></td>
            		<td class="Data"><%=arrearContri%></td>
         
          </tr>
        </table></td>

  </tr>
  <%}%>
      <tr>
  
    <td nowrap="nowrap" colspan="6" class="label">Date</td>
    
  </tr>
    <tr>
    <td nowrap="nowrap" colspan="6" class="label">M=MARRIED, U=UNMARRIED, W=WIDOW/WIDOWER</td>
    
  </tr>
    <tr>
  
    <td nowrap="nowrap" colspan="6" class="label">RAJIV GANDHI BHAWAN, SAFDARJUNG AIRPORT, NEW DELHI-110003. PHONE 011-24632950, FAX 011-24610540.</td>
    
  </tr>
  
  

  <%if(size-1!=cardList){%>
						<br style='page-break-after:always;'>
	<%}%>						
	    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</td>
</tr>

				
   <%	}}}%>

	
					
</table>

  </body>
</html>
