 <!--newpage-->
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="aims.dao.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String userId = "";
	String signature= "",wetheroption="",emolumentsmnths="",region="";
	String adjOBYear ="";
	if(request.getAttribute("reportYear")!=null){
	adjOBYear=(String) request.getAttribute("reportYear");
	}
	System.out.println("adjOBYear"+adjOBYear);
	 
 userId = session.getAttribute("userid").toString();
 CommonDAO  commonDAO =  new aims.dao.CommonDAO();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

 
  <head>
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css"/>
     
<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai1.css" 
	type="text/css"/>
<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
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
     
  </head>
  
 <body>
  <form >
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
   <%
   			ArrayList cardReportList=new ArrayList();
	  	String reportType="",fileName="",dispDesignation="";
	  	String arrearInfo="",orderInfo="";
	  	int size=0,finalInterestCal=0,noOfmonthsForInterest=0,noOfAfterfnlrtmntmonhts=0;
		String fromdate="",todate="",obMessage="";
		int fromYear=0,toYear=0; 
		String Years[]=null;
		Years = adjOBYear.split("-"); 
		CommonUtil commonUtil=new CommonUtil();
		fromdate="01-Apr-"+Years[0];
		todate="01-Mar-"+Years[1];
		fromYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				fromdate, "dd-MMM-yyyy", "yyyy"));
		toYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				todate, "dd-MMM-yyyy", "yyyy"));	 
	  	
	  	if(request.getAttribute("cardList")!=null){
		   
	    String dispYear="",pfidString="",chkBulkPrint="",pfcardNarration="",chkRegionString="",chkStationString="",dispSignStation="",dispSignPath="";
 		String mangerName="",ArrearInFSPeriod="false";
    	boolean check=false,signatureFlag=false;
	   Long finalEmpNetOB=null,finalAaiNetOB=null,finalPensionOB=null,finalPrincipalOB=null,finalStlmentEmpNet=null,finalStlmentNetAmount=null,finalStlmentAAICon=null,finalStlmentPensDet=null;
	   Long finalEmpNetOBIntrst=null,finalAaiNetOBIntrst=null,finalPensionOBIntrst=null;
	    long tempFinalPensionOB=0;
	 FinancialReportDAO financialReportDAO = new FinancialReportDAO();	
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
    		}else if(chkStationString.toLowerCase().equals("KOLKATA".toLowerCase())){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
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
	  	ArrayList dateCardList1=new ArrayList();
	  	ArrayList dataPTWList=new ArrayList();
	  	ArrayList dataFinalSettlementList=new ArrayList();
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  		
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		System.out.println("PF ID String"+personalInfo.getPfIDString()+"Adj Date"+personalInfo.getAdjDate());
		dateCardList=cardReport.getPensionCardList();
		dateCardList1=cardReport.getPensionCardList1();
		dataPTWList=cardReport.getPtwList();
		noOfmonthsForInterest=cardReport.getNoOfMonths();
		intMonths=cardReport.getInterNoOfMonths();
		arrearMonths=cardReport.getArrearNoOfMonths();
		dataFinalSettlementList=cardReport.getFinalSettmentList();
        arrearInfo=cardReport.getArrearInfo();
        orderInfo=cardReport.getOrderInfo();
        String[] arrearData=arrearInfo.split(",");
        double arrearAmount=0.00,arrearContri=0.00;
        boolean alreadyfinal=false,finalrateofinterest=false,isFrozedSeperation=false;        
        String arrearDate="";
        
        isFrozedSeperation=personalInfo.isFrozedSeperationAvail();
        String finalsettlmentdt=personalInfo.getFinalSettlementDate(); 
        
       if(!personalInfo.getFinalSettlementDate().equals("---")){
        	if ((!commonDAO.compareFinalSettlementDates(fromdate,todate,personalInfo.getFinalSettlementDate())) && personalInfo.isCrossfinyear()==false && fromYear>=2011){
        		finalsettlmentdt=personalInfo.getResttlmentDate();
        		alreadyfinal=true;
    		}
        	System.out.println("fromYear============================"+fromYear+"toYear"+toYear);
        	 //   For  Making rateofintrst as 9.5  over all 2010-2011  So 
        	 //We Removed Final Settlement cases rateof intrst Condition 
        
        }        
        arrearDate=arrearData[0];
        arrearAmount=Double.parseDouble(arrearData[2]);
        arrearContri=Double.parseDouble(arrearData[3]);
       
       wetheroption=personalInfo.getWetherOption();
       System.out.println("=======wetheroption--="+wetheroption);
       
       
       
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
    <td colspan="3" class="reportlabel" style="text-decoration: underline" align="center">EDITED PF CARD </td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   

    <td colspan="3" align="center" nowrap="nowrap" class="reportsublabel">FOR THE YEAR <font style="text-decoration: underline"><%=dispYear%></td>
    <!--<td align="right" nowrap="nowrap" class="Data">Dt:<%=commonUtil.getCurrentDate("dd-MM-yyyy HH:mm:ss")%></td> -->
    
  </tr>
  <tr>
    <td colspan="7">&nbsp;</td>
  </tr> 
   
   <!--<tr>  
    
  <td>&nbsp;</td>
 <td   class="reportlabel" style="text-decoration: underline" align="center">MONTHLY CPF CORRECTIONS</td>
  
    <td colspan="2"> &nbsp;</td>
     <td>&nbsp;</td>
      <td>&nbsp;</td>
  </tr> 
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
   

    <td colspan="3" align="center" nowrap="nowrap" class="reportsublabel">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <font style="text-decoration: underline"><%=dispYear%></td>
    <td align="right" nowrap="nowrap" class="Data">&nbsp;</td>
    
  </tr>-->
  <!--<tr>    
   
 <td   class="reportlabel" style="text-decoration: underline" align="center">MONTHLY CPF CORRECTIONS</td>
  <td align="right">  </td>
  <td align="right">  </td>
						
  </tr>--> 
  <tr>
    <td colspan="6">&nbsp;</td>
  </tr>
  
  
 
  <%


  %>
  <tr>
    <td colspan="7"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
        <td width="48%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="36%" class="reportsublabel">Pf Id:</td>
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
        <td colspan="8" align="center" class="label">EMPLOYEES SUBSCRIPTION</td>
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
         <td width="5%" rowspan="2" class="label">PFW SUB</td>
        <td width="8%" class="label">NET </td>
      
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
        <td class="label">(7-(8+9))</td>
      
        <td class="label" >(11-12)</td>
       
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
        <td class="label">14</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
    
        <%
       	DecimalFormat df = new DecimalFormat("#########0.00");
       	DecimalFormat df1 = new DecimalFormat("#########0");
       	double totalEmoluments=0.0,pfStaturary=0.0,empVpf=0.0,principle=0.0,interest=0.0,empTotal=0.0,totalEmpNet=0.0,totalEmpIntNet=0.0,dispTotalEmpNet=0.0,dispTotalAAINet=0.0,totalAaiIntNet=0.0;
       	double totalAAIPF=0.0,totalPFDrwn=0.0,totalAAINet=0.0,advancePFWPaid=0.0,empNetInterest=0.0,aaiNetInterest=0.0,dispTotalPensionContr=0.0,totalPensionContr=0.0,pensionInterest=0.0,pensionArrearInterest=0.0,arrearEmpNetInterest=0.0,arrearAaiNetInterest=0.0;
       	double totalAdvances=0,totalGrandArrearEmpNet=0.0,totalGrandArrearAaiNet=0.0,advanceAmountPaid=0.0, PFWSubPaid =0.0,pensionContriTot = 0.0,pensionContriArrearTot=0.0;
       	double totalArrearEmpNet=0.0,totalArrearAAINet=0.0,totalArrearPCNet=0.0;
       	double aftrFinstlmntAAINetTot=0.0,aftrFinstlmntEmpNetTot= 0.0,aftrFinstlmntPCNetTot=0.0, aftrFinstlmntAAINetIntrst=0.0,aftrFinstlmntEmpNetIntrst= 0.0,aftrFinstlmntPCNetIntrst=0.0;
       	double remaininInt =0.0,remainAaiInt=0.0;
       	double aprEmpNet=0.0,aprAaiNet=0.0,aprPrincipal=0.0,aprpc=0.0;
       	double grandaprEmpNet=0.0,grandaprAaiNet=0.0,grandaprPrincipal=0.0,grandaprpc=0.0;
       	String arrearFlag="N",arrearRemarks="---",adjNarration="";
  		EmployeePensionCardInfo pensionCardInfo=new EmployeePensionCardInfo();
  		EmployeePensionCardInfo pensionCardInfo1=new EmployeePensionCardInfo();
		Long empNetOB=null,aaiNetOB=null,pensionOB=null,principalOB=null;
		Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null,adjOutstandadv=null;
		double rateOfInterest=0,emoluments =0.0;
  		String obFlag="",calYear="",datestartedFalg="false";
  		boolean closeFlag=false;
  		ArrayList obList=new ArrayList();
  		ArrayList closingOBList=new ArrayList();
  		int monthsCntAfterFinstlmnt =0;
  		
  		if(dateCardList.size()!=0){
  		for(int i=0;i<dateCardList.size();i++){
  		pensionCardInfo=(EmployeePensionCardInfo)dateCardList.get(i);
  		obFlag=pensionCardInfo.getObFlag();
  		if(obFlag.equals("Y")){
  			 calYear=commonUtil.converDBToAppFormat(pensionCardInfo.getShnMnthYear(),"MMM-yy","yyyy");
  			 System.out.println("--------calYear ------"+calYear);
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
  		
  		//added on 06-Jan-2012 for calcuting intrst for data after finalsettlemnt from the starting mnth onwards
  		emoluments  = Double.parseDouble(pensionCardInfo.getEmoluments());
  		System.out.println("======DataAfterFinalsettlemnt()====="+pensionCardInfo.getDataAfterFinalsettlemnt());
  		if(pensionCardInfo.getDataAfterFinalsettlemnt().equals("true")){
  		if(emoluments>0){  	
  		datestartedFalg = "true";
  		aftrFinstlmntEmpNetTot= aftrFinstlmntEmpNetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntEmpNetTot()));
  		aftrFinstlmntAAINetTot = aftrFinstlmntAAINetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntAAINetTot()));
  		aftrFinstlmntPCNetTot= aftrFinstlmntPCNetTot + Math.round(Double.parseDouble(pensionCardInfo.getAftrFinstlmntPCNetTot()));  		 
  		} 
  		if(datestartedFalg.equals("true")){
  			monthsCntAfterFinstlmnt++;
  		}
  		}
  		
  	//	System.out.println("final settlement date" +personalInfo.getFinalSettlementDate());
  		
  		if(((Integer.parseInt(calYear)>=2010 && dataFinalSettlementList.size()==0) && pensionCardInfo.getTransArrearFlag().equals("Y"))||((pensionCardInfo.getTransArrearFlag().equals("N")) ||(pensionCardInfo.getTransArrearFlag().equals("P")))||(pensionCardInfo.getTransArrearFlag().equals("Y")&&(personalInfo.getChkArrearFlag().equals("N")))){
  		totalEmoluments= new Double(df.format(totalEmoluments+Math.round(Double.parseDouble(pensionCardInfo.getEmoluments())))).doubleValue();
		pfStaturary= new Double(df.format(pfStaturary+Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury())))).doubleValue();
		empVpf = new Double(df.format(empVpf+Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf())))).doubleValue();
		principle =new Double(df.format(principle+Math.round(Double.parseDouble(pensionCardInfo.getPrincipal())))).doubleValue();
		interest =new Double(df.format(interest+Math.round(Double.parseDouble(pensionCardInfo.getInterest())))).doubleValue();
		empTotal=new Double(df.format(empTotal+Math.round(Double.parseDouble(pensionCardInfo.getEmptotal())))).doubleValue();
		
		
	    advancePFWPaid= new Double(df.format(advancePFWPaid+Math.round( Double.parseDouble(pensionCardInfo.getAdvancePFWPaid())))).doubleValue();
	    
	    advanceAmountPaid= new Double(df.format(advanceAmountPaid+Math.round( Double.parseDouble(pensionCardInfo.getAdvancesAmount())))).doubleValue();
	     PFWSubPaid= new Double(df.format(PFWSubPaid+Math.round( Double.parseDouble(pensionCardInfo.getPFWSubscri())))).doubleValue();
	     
	    if(!pensionCardInfo.getGrandCummulative().equals("")){
	    		if(pensionCardInfo.isYearFlag()==true){
	    		totalEmpNet=totalEmpNet+new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();
	       		}else{
	    		totalEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandCummulative()))).doubleValue();
	    
	    		}
	   }
	    
	    System.out.println("GrandArrearEmpNetCummulative()"+pensionCardInfo.getGrandArrearEmpNetCummulative());

	   
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
	    System.out.println("GrandArrearEmpNetCummulative()"+pensionCardInfo.getGrandArrearEmpNetCummulative());
	
	   // totalAAINet= new Double(df.format(totalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiCummulative())))).doubleValue();
	    dispTotalAAINet= new Double(df.format(dispTotalAAINet+Math.round( Double.parseDouble(pensionCardInfo.getAaiNet())))).doubleValue();
	    
	     dispTotalPensionContr=new Double(df.format(dispTotalPensionContr+Math.round( Double.parseDouble(pensionCardInfo.getPensionContribution())))).doubleValue();
		 pensionContriTot  =  new Double(df.format(pensionContriTot + Double.parseDouble(pensionCardInfo.getPensionContriAmnt()))).doubleValue();
		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
			 arrearRemarks="ARREARS";
		}else   arrearRemarks="---";
	     
  		}else{
  				 totalArrearEmpNet=totalArrearEmpNet+Math.round( Double.parseDouble(pensionCardInfo.getEmpNet()));
  				 totalArrearAAINet=totalArrearAAINet+Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()));
  				 totalArrearPCNet = totalArrearPCNet+Math.round(Double.parseDouble(pensionCardInfo.getPensionContriArrearAmnt()));
       			 arrearFlag="Y";
       			 arrearRemarks="ARREARS";
  		}
  			    
			if((!pensionCardInfo.getGrandArrearEmpNetCummulative().equals("") && totalArrearEmpNet!=0.00) ||(!pensionCardInfo.getGrandArrearEmpNetCummulative().equals("") && isFrozedSeperation==true)){
	    	
	    	totalGrandArrearEmpNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearEmpNetCummulative()))).doubleValue();
    		}
  		    if((!pensionCardInfo.getGrandArrearAAICummulative().equals("") && totalArrearEmpNet!=0.00) ||(!pensionCardInfo.getGrandArrearAAICummulative().equals("") && isFrozedSeperation==true) ){
	    	
	    	totalGrandArrearAaiNet= new Double(df.format(Double.parseDouble(pensionCardInfo.getGrandArrearAAICummulative()))).doubleValue();
    		} 
			
  		  if(pensionCardInfo.getMergerflag().equals("Y") ){
      		if(!arrearRemarks.equals("---")){
      		arrearRemarks=arrearRemarks+","+pensionCardInfo.getMergerremarks();
      		}else{
      			arrearRemarks=pensionCardInfo.getMergerremarks();
      		}
  			
  		}
  	 
  			//By Radha on 18-Dec-2012 as per sehgal request
  			pfcardNarration = pensionCardInfo.getPfcardNarration();
  			if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y") || pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
  			if(!arrearRemarks.equals("---")){
  			arrearRemarks = arrearRemarks+","+pfcardNarration;
  			}else{
  			arrearRemarks = pfcardNarration;
  			}  			
  			}
  			
  	%>
 <span id="spnTip" style="position: absolute; visibility: hidden; background-color: #ffedc8; border: 1px solid #000000; padding-left: 15px; padding-right: 15px; font-weight: normal; padding-top: 5px; padding-bottom: 5px; margin-left: 25px;"></span>
 
  	<%if(obFlag.equals("Y")){%>
  	  <tr>
        <td colspan="3" class="label">OPENING BALANCE (OB)</td>
        <td>&nbsp;</td>        
        <td class="NumData"><%=principalOB%></td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>       
        <td>&nbsp;</td> 
        <td class="NumData" width="6%" align="right"><%=empNetOB%>
		</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>       
        <td class="NumData" width="6%" align="right"><%=aaiNetOB%>
		</td>
         <td class="NumData">0</td>
        			
        <td>&nbsp;</td>
     
      </tr>
  <% System.out.println("===========totalEmpNet"+totalEmpNet+"totalAAINet"+pensionAdjOB);%>
       <tr>
        <td colspan="3" class="label">ADJ  IN OB</td>
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
 <td class="NumData"><a  title="Click the link to view Transaction log" target="new" href="./search1?method=getEmolumentslog&pfId=<%=personalInfo.getPfID()%>&airportcode=<%=adjStation%>"><%=pensionAdjOB%></a></td>
 <%} else{%>
 <td class="NumData"><%=pensionAdjOB%></td>
<%} %>
        <td class="NumData"><%=adjPensionContri%></td>
        <td>&nbsp;</td>
           <%if (!adjNarration.equals("---")&& !adjNarration.trim().equals("")){%>
	   <td width="2%" class="NumData"  onmouseover="showTip('<%=adjNarration%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=adjNarration.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="NumData">&nbsp;</td>
	 <%}%>
       
       
      </tr>
      <%}%>
      
	 <tr>
	 
	 <td width="4%" nowrap="nowrap" class="NumData"><%=pensionCardInfo.getShnMnthYear()%></td>
	 <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()))%>
	</td>
	  <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmppfstatury()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpvpf()))%>
	</td>				
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getPrincipal()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getInterest()))%>
	</td>
	<td width="6%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmptotal()))%></td>
	
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getAdvancesAmount()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getPFWSubscri()))%>
	</td>
	 <td width="5%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmpNet()))%></td>
	 
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiPF()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getPFWContri()))%>
	</td>
	 <td width="9%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo.getAaiNet()))%></td>
	 
	 <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%>
		<input type="hidden"   name="emolumentsmnts<%=i%>"  size="1"  value='<%=pensionCardInfo.getEmolumentMonths()%>' />  
	 </td>
	 
	 <!-- <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getEmoluments()))%></td>
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

	 <td width="12%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%></td> -->  
	 
 				  
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getStation()%></td>
	 
	  <%if (!arrearRemarks.equals("---")){%>
	   <td width="2%" class="Data"  onmouseover="showTip('<%=arrearRemarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=arrearRemarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="Data"><%=arrearRemarks %></td>
	 <%}%>
	 </tr>
	
	 <%
	  System.out.println("===========totalEmpNet"+totalEmpNet+"totalAAINet"+totalAAINet+"==========");
		
	 	if(pensionCardInfo.getCbFlag().equals("Y")){
	 		
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
	 				}else if(Integer.parseInt(calYear)>=2012 && Integer.parseInt(calYear)<2014){
	 					rateOfInterest=9.5;
	 				}else if(Integer.parseInt(calYear)>=2014 && Integer.parseInt(calYear)<2015){
	 					rateOfInterest=9.25;
	 				}else if(Integer.parseInt(calYear)>=2015 && Integer.parseInt(calYear)<2016){
	 					rateOfInterest=9.15;
	 				}
	 				if(!personalInfo.getFinalSettlementDate().equals("---")){
			        	
						if(Integer.parseInt(calYear)>=2014 && Integer.parseInt(calYear)<=2015){
							//if ((commonDAO.compareFinalSettlementDates("01-Apr-2014","31-Mar-2015",personalInfo.getFinalSettlementDate())==true)){
			            		//finalrateofinterest=true;
			        		//}else 
			        		if(!personalInfo.getResttlmentDate().equals("---")){
			        			if ((commonDAO.compareFinalSettlementDates("01-Apr-2014","31-Mar-2015",personalInfo.getResttlmentDate())==true)){
			                		finalrateofinterest=true;
			        			}
			        		}
			        	}
			        }
			         if(finalrateofinterest==true){
					  rateOfInterest = 8.25;
				  }
	 				
	 				
					 if (intMonths!=0){
	 					 empNetInterest=new Double(df.format(((totalEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
	 				    aaiNetInterest=new Double(df.format(((totalAAINet*rateOfInterest)/100/intMonths))).doubleValue();
	 				    pensionInterest=new Double(Math.round(((pensionContriTot*rateOfInterest)/100/intMonths))).doubleValue();
	 				  
	 				}else{
	 					empNetInterest=0.0;
	 					aaiNetInterest=0.0;
	 					pensionInterest=0.0;
	 				}
	 				if(arrearMonths!=0 && isFrozedSeperation==true){
	 					 arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 empNetInterest=empNetInterest+arrearEmpNetInterest;
						 aaiNetInterest=aaiNetInterest+arrearAaiNetInterest;
						 System.out.println("----rateOfInterest------"+rateOfInterest+"arrearEmpNetInterest"+arrearEmpNetInterest+"----totalGrandArrearEmpNet---ssss---"+totalGrandArrearEmpNet+"arrearMonths"+arrearMonths);
	 				}
				    
				      System.out.println("isArreerintflag"+personalInfo.isArreerintflag()+"isFrozedSeperation"+isFrozedSeperation);
				     
					
				    if(totalGrandArrearEmpNet!=0.0 && totalGrandArrearAaiNet!=0.0 && personalInfo.isArreerintflag()!=true && isFrozedSeperation==false){
				    arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
				    arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/intMonths))).doubleValue();
				     pensionArrearInterest =new Double(df.format(((pensionContriArrearTot*rateOfInterest)/100/intMonths))).doubleValue();
				    }else{
				    	arrearEmpNetInterest=0.0;
				    	arrearAaiNetInterest=0.0;
				    	pensionArrearInterest=0.0;
				    }
				     System.out.println("arrearEmpNetInterest"+arrearEmpNetInterest+"arrearAaiNetInterest"+arrearAaiNetInterest+"noOfmonthsForInterest"+noOfmonthsForInterest);
				    //For Pension Contribution attribute,we are not cummlative
				    // pensionInterest=new Double(Math.round(((pensionContriTot*rateOfInterest)/100/intMonths))).doubleValue();
				    totalEmpIntNet=new Double(Math.round(empNetInterest+totalEmpNet)).doubleValue();
				    totalAaiIntNet=new Double(Math.round(totalAAINet+aaiNetInterest)).doubleValue();
				    System.out.println("totalNoOfMonths"+noOfmonthsForInterest+"Cummulative Interest Months"+intMonths+"empNetInterest"+empNetInterest+"aaiNetInterest"+aaiNetInterest);
				 	closingOBList=reportService.calClosingOB(rateOfInterest,obList,Math.round(dispTotalAAINet),Math.round(aaiNetInterest),Math.round(dispTotalEmpNet),Math.round(empNetInterest),Math.round(totalPensionContr),Math.round(pensionInterest),totalAdvances,principle,noOfmonthsForInterest,personalInfo.isObInterst(),personalInfo.isAdjObInterst());
				 	System.out.println("=============suresh==============="+personalInfo.isArreerintflag());															
	 				if(closingOBList.size()!=0){
					 	finalEmpNetOB=(Long)closingOBList.get(0);
					 	finalAaiNetOB=(Long)closingOBList.get(1);
					 	finalPensionOB=(Long)closingOBList.get(2);
					 	tempFinalPensionOB=new Double(dispTotalPensionContr).longValue() +new Double(pensionInterest).longValue();
					 	finalEmpNetOBIntrst=(Long)closingOBList.get(4);
					 	finalAaiNetOBIntrst=(Long)closingOBList.get(5);
					 	finalPensionOBIntrst=(Long)closingOBList.get(6);
					 	finalPrincipalOB=(Long)closingOBList.get(7);
					 	
					 	//Cond for Not Calc Interest on PC after Sep+3 Years
				  if(finalEmpNetOBIntrst.intValue()==0 && finalAaiNetOBIntrst.intValue() ==0){
				  		pensionInterest =0;
				  	}
				 	tempFinalPensionOB= new Double(dispTotalPensionContr).longValue() + new Double(pensionInterest).longValue()+ new Double(totalArrearPCNet).longValue();
					 	
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
	 <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(advanceAmountPaid)%></td>
	  <td width="5%" nowrap="nowrap" class="NumData"><%=df1.format(PFWSubPaid)%></td>
	 <td width="3%"  nowrap="nowrap" class="NumData"><%=df1.format(dispTotalEmpNet)%></td>
	 <td width="3%" class="NumData"><%=df1.format(totalAAIPF)%></td>
	 <td width="6%" class="NumData"><%=df1.format(totalPFDrwn)%></td>
	 <td width="3%" class="NumData"><%=df1.format(dispTotalAAINet)%></td>
	 <td width="12%" class="NumData"><%=df1.format(dispTotalPensionContr+totalArrearPCNet)%></td>
	  <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 </tr>
	  <tr>
	 <td width="50" nowrap="nowrap" class="label">INTEREST</td>
	 <td width="116" class="NumData"><%=rateOfInterest%></td>

	  <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOBIntrst)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOBIntrst)%></td>
	   <td width="3%" class="NumData" nowrap="nowrap"><%=new Double(pensionInterest).longValue()%></td>
	  <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalPrincipalOB)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOB)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOB)%></td>
	 
	 <td width="12%" class="NumData"><%=tempFinalPensionOB%></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%
	if(dataFinalSettlementList.size()!=0){
	finalStlmentEmpNet=new Long(Long.parseLong((String)dataFinalSettlementList.get(5)));
	finalStlmentAAICon=new Long(Long.parseLong((String)dataFinalSettlementList.get(6)));
	finalStlmentPensDet=new Long(Long.parseLong((String)dataFinalSettlementList.get(7)));
	finalStlmentNetAmount=new Long(Long.parseLong((String)dataFinalSettlementList.get(8)));
	finalInterestCal=12-noOfmonthsForInterest;
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==false){
		noOfAfterfnlrtmntmonhts=12-noOfmonthsForInterest;
	}
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==true ){
		finalInterestCal=0;
	}
	
	System.out.println("dataFinalSettlementList"+(String)dataFinalSettlementList.get(8)+"finalStlmentEmpNet=="+finalStlmentEmpNet+"finalStlmentNetAmount"+finalStlmentNetAmount);
	long netcloseEmpNet=(finalEmpNetOB.longValue())+(-finalStlmentEmpNet.longValue());
	long netcloseNetAmount=(finalAaiNetOB.longValue())+(-finalStlmentAAICon.longValue());
	double remaininInt1=arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
	double remainAaiInt1=arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	double remainPCInt=0.0;
	  
	System.out.println("personalInfo.getFinalSettlementDate()"+personalInfo.getFinalSettlementDate()+"Resettlemnt date"+personalInfo.getResttlmentDate()+netcloseEmpNet+"finalInterestCal"+finalInterestCal);
	System.out.println("totalArrearEmpNet"+totalArrearEmpNet+"arrearAaiNetInterest"+arrearAaiNetInterest+netcloseEmpNet);
	System.out.println("finalInterestCal"+finalInterestCal+"noOfAfterfnlrtmntmonhts-----------"+noOfAfterfnlrtmntmonhts+"========isFrozedSeperation========="+isFrozedSeperation+"noOfmonthsForInterest"+noOfmonthsForInterest);
	if(!personalInfo.getFinalSettlementDate().equals("---")){
			if(!personalInfo.getResttlmentDate().equals("---")){
				 remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			 remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			  remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
			}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
				 remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*finalInterestCal);
			}
	}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
			 	 remainPCInt=totalArrearPCNet+pensionArrearInterest+Math.round((tempFinalPensionOB*rateOfInterest/100/12)*finalInterestCal);
	
	} 	
	 
	
	System.out.println("=====tempFinalPensionOB========"+tempFinalPensionOB+"=="+(tempFinalPensionOB*rateOfInterest/100/12)*finalInterestCal);
					  
	 					  double finalEmpNetClosingOB =0.0,finalAAINetClosingOB =0.0,finalPCNetClosingOB=0.0;
					  finalEmpNetClosingOB = Double.parseDouble(String.valueOf(netcloseEmpNet)) + remaininInt ; 					
					  finalAAINetClosingOB = Double.parseDouble(String.valueOf(netcloseNetAmount)) + remainAaiInt;
					   //By Radha On 27-Apr-2012 For restricting of  calc of intrst amnt for PensionContri 
					   finalPCNetClosingOB =  Double.parseDouble(String.valueOf(tempFinalPensionOB))  ;  
					   finalEmpNetOB =   new Long(new Double(Double.toString(finalEmpNetClosingOB)).longValue());
					   //finalEmpNetOBIntrst =  new Long(new Double(Double.toString(remaininInt)).longValue());					   	
					   finalAaiNetOB =  new Long(new Double(Double.toString(finalAAINetClosingOB)).longValue());				   
					  // finalAaiNetOBIntrst =  new Long(new Double(Double.toString(remainAaiInt)).longValue());
					  // pensionInterest = 	 remainPCInt ;	
					   tempFinalPensionOB = 	new Double(Double.toString(finalPCNetClosingOB)).longValue();		 
					   System.out.println("==***==="+finalEmpNetOBIntrst+"==="+finalEmpNetOB+"==="+finalAaiNetOB+"==="+finalAaiNetOBIntrst+"pensionInterest==="+pensionInterest); 	
					
	
 %>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">FINAL SETTLEMENT (<%=(String)dataFinalSettlementList.get(11)%>)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=-finalStlmentEmpNet.longValue()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=-finalStlmentAAICon.longValue()%></td>
	 <td colspan="5" class="Data"><%=orderInfo%></td>
 </tr>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NET CLOSING (A)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(totalArrearEmpNet)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(totalArrearAAINet)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (C)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt1)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt1)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>

 <%}else{%>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=(String)dataFinalSettlementList.get(12)%> (B)</td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt)%></td>
	 <td colspan="5" class="Data">&nbsp;</td>
 </tr>
 	
<%}%> 
<tr>
 <% if(arrearFlag.equals("Y")){
 //Add By Radha On 31-Jan-2013 as per Sehgal Pfid: 8765
    ArrearInFSPeriod="true";
 %>
	 <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING (A+B+C)</td>
      <%}else{ %>
   <td colspan="2" nowrap="nowrap" class="label">REVISED NET CLOSING(A+B) </td>
     <%} %>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(netcloseEmpNet+remaininInt)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(netcloseNetAmount+remainAaiInt)%></td>
	  <td colspan="5" class="Data"><%=df1.format(tempFinalPensionOB)%></td>
 </tr>
 <%if(!personalInfo.getAdjDate().equals("---")){%>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label"><%=personalInfo.getAdjRemarks()%> </td>
 	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%}%>
   <!-- <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>-->
 <%}else{
  %>
 
 <% System.out.println("dispYear==="+dispYear);	
 if(dispYear.equals("2012-2013")){
 pensionCardInfo1=(EmployeePensionCardInfo)dateCardList1.get(0);
        aprEmpNet=Double.parseDouble(pensionCardInfo1.getEmpNet());
 		aprAaiNet=Double.parseDouble(pensionCardInfo1.getAaiNet());
 		aprPrincipal=Double.parseDouble(pensionCardInfo1.getPrincipal());
 		aprpc=Double.parseDouble(pensionCardInfo1.getPensionContribution());
 		grandaprEmpNet=Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal;
	    grandaprAaiNet=Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet;
	    grandaprPrincipal=Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet;
	    grandaprpc=Double.parseDouble(df1.format(tempFinalPensionOB))+ aprpc;
 		String aprremarks="---"; 
 		if(pensionCardInfo.getTransArrearFlag().equals("Y")){
 		aprremarks="ARREARS";
 		}else {
 		aprremarks="---";
 		}
 		 if(pensionCardInfo.getMergerflag().equals("Y") ){
 		 aprremarks=aprremarks+","+pensionCardInfo.getMergerremarks();
 		 }else{
 		 aprremarks=pensionCardInfo.getMergerremarks();
 		 }
 		 if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y") || pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
  			aprremarks = aprremarks+","+pensionCardInfo.getPfcardNarrationFlag();
  			
  			}
 		%>
  <tr>
	 
	 <td width="4%" nowrap="nowrap" class="NumData"><%=pensionCardInfo1.getShnMnthYear()%>(MarSal)</td>
	 <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmoluments()))%>
	</td>
	  <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmppfstatury()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpvpf()))%>
	</td>				
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPrincipal()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getInterest()))%>
	</td>
	<td width="6%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmptotal()))%></td>
	
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAdvancesAmount()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPFWSubscri()))%>
	</td>
	 <td width="5%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpNet()))%></td>
	 
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiPF()))%>
	</td>
	<td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPFWContri()))%>
	</td>
	 <td width="9%" class="Data"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiNet()))%></td>
	 
	 <td class="Data" width="6%" align="right"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPensionContribution()))%>
		<input type="hidden"   name="emolumentsmnts<%=i%>"  size="1"  value='<%=pensionCardInfo1.getEmolumentMonths()%>' />  
	 </td>		  
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getStation()%></td>
	 
	  <%if (!arrearRemarks.equals("---")){%>
	   <td width="2%" class="Data"  onmouseover="showTip('<%=arrearRemarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=arrearRemarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="2%" class="Data"><%=arrearRemarks %></td>
	 <%}%>
	 </tr>

	  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(grandaprEmpNet)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(grandaprAaiNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(grandaprPrincipal)%></td>
	 
	 <td width="12%" class="NumData"><%=df1.format(grandaprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
<!-- 	 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NEW CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet)%></td>
	 <td width="6%" class="NumData"><%=df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr> -->
 <% } %>
    <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB))+aprEmpNet+aprAaiNet)%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 
 <!--    <tr>
 
	 <td colspan="9" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>-->

 <!--    <tr>
 
	 <td colspan="9" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB)))%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>-->
 <%}%>
 <tr>
    <td colspan="16">&nbsp;</td>
 </tr>
  <%totalEmpNet=0;totalEmoluments=0;totalAAIPF=0;pfStaturary=0;totalPFDrwn=0;empTotal=0;totalAAINet=0;totalPensionContr=0;}%>
    </tr>

  <%}}%>
  
   <% ArrayList prevGrandTotalsList = new ArrayList();
   ArrayList finalizedTotals = new ArrayList();
					 double emolumentsTot_prev =0.00,cpfTot_prev =0.00,PenscontriTot_Prev = 0.00,PfTot_prev =0.00,empSubTot_prev=0.00,AAiContriTot_Prev=0.00, adjEmpSubIntrst_prev=0.00,  adjAAiContriIntrst_Prev=0.00,adjPesionContriIntrst_Prev=0.00,adjPensionContriIntrst_diff=0.00;
					 double emolumentsTot_diff =0.00,cpfTot_diff =0.00,PenscontriTot_diff = 0.00,PfTot_diff =0.00,empSubTot_diff=0.00,AAiContriTot_diff=0.00,adjEmpSubIntrst_diff=0.00, adjAAiContriIntrst_diff=0.00;
					 double grandEmoluments = 0.0,grandCPF=0.0,grandCPFInterest=0.0,grandPension=0.0,grandPensionInterest=0.0,grandPFContribution=0.00,grandPFContributionInterest=0.00,
					   					grandEmpSub =0.00 ,grandEmpSubInterest=0.00 ,grandAAIContri=0.00,grandAAIContriInterest=0.00,adjFSEmpSubIntrst_prev=0.00,adjFSContriIntrst_Prev=0.00;
					 String transid="";
					   double empSubTot=0.00,AAiContriTot=0.00,PenscontriTot=0.00,PensionIntrst=0.00,empSubIntrst=0.00,aaiContriIntrst=0.00;
					 int result =0; 
					   if(request.getAttribute("prevGrandTotalsList")!=null){
					   prevGrandTotalsList = (ArrayList) request.getAttribute("prevGrandTotalsList");
					    
					    if(prevGrandTotalsList.size()!=0){
					   transid = prevGrandTotalsList.get(0).toString();  
					   PenscontriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(3).toString()) ;					 
					   empSubTot_prev = Double.parseDouble(prevGrandTotalsList.get(5).toString()) ;
					   AAiContriTot_Prev = Double.parseDouble(prevGrandTotalsList.get(6).toString()) ;		    
					  adjEmpSubIntrst_prev = Double.parseDouble(prevGrandTotalsList.get(7).toString()) ;
					   adjAAiContriIntrst_Prev = Double.parseDouble(prevGrandTotalsList.get(8).toString()) ;
					   adjPesionContriIntrst_Prev= Double.parseDouble(prevGrandTotalsList.get(9).toString()) ;	
						//For Final Settlemnt cases on 27-Apr-2012 for  case pfid 17986	   
						adjFSEmpSubIntrst_prev= Double.parseDouble(prevGrandTotalsList.get(10).toString()) ;	
						adjFSContriIntrst_Prev= Double.parseDouble(prevGrandTotalsList.get(11).toString()) ;	
					
					 empSubTot_diff=  finalEmpNetOB.doubleValue()  - empSubTot_prev;
					 AAiContriTot_diff=  finalAaiNetOB.doubleValue() - AAiContriTot_Prev;					   
					   PenscontriTot_diff =  new Long(tempFinalPensionOB).doubleValue() -PenscontriTot_Prev ; 
					 //Add By Radha On 31-Jan-2013 as per Sehgal Pfid: 8765
					 
					 if(ArrearInFSPeriod.equals("true")){
					 adjEmpSubIntrst_diff= finalEmpNetOBIntrst.doubleValue() - adjEmpSubIntrst_prev +(remaininInt- totalArrearEmpNet- adjFSEmpSubIntrst_prev);
					 adjAAiContriIntrst_diff = finalAaiNetOBIntrst.doubleValue() - adjAAiContriIntrst_Prev + ( remainAaiInt- totalArrearAAINet- adjFSContriIntrst_Prev); 
					 
					 }else{
					 adjEmpSubIntrst_diff= finalEmpNetOBIntrst.doubleValue() - adjEmpSubIntrst_prev +(remaininInt - adjFSEmpSubIntrst_prev);
					 adjAAiContriIntrst_diff = finalAaiNetOBIntrst.doubleValue() - adjAAiContriIntrst_Prev + ( remainAaiInt - adjFSContriIntrst_Prev); 
					  
					 }
					 



							adjPensionContriIntrst_diff=pensionInterest-adjPesionContriIntrst_Prev;
					  
				 
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
						<td class="HighlightData" align="left" colspan="9">Previous Grand Totals</td>						 
						 
						<td class="HighlightData"   align="right"><%=df.format(empSubTot_prev)%></td>
						<td class="HighlightData"  colspan="2" align="right">&nbsp;</td>
						<td class="HighlightData"  align="right"><%=df.format(AAiContriTot_Prev)%></td>
						<td class="HighlightData"   align="right"><%=df.format(PenscontriTot_Prev)%></td>
						 <td class="HighlightData"  colspan="4" align="right">&nbsp;</td>
						 
						

					</tr>
					<tr>
						<td class="HighlightData" align="left" colspan="9">Difference </td>
						 
						 
						<td class="HighlightData"   align="right"><%=df.format(empSubTot_diff)%></td>
						<td class="HighlightData"  colspan="2" align="right">&nbsp;</td>										 
						<td class="HighlightData"  align="right"><%=df.format(AAiContriTot_diff)%></td>
						<td class="HighlightData"   align="right"><%=df.format(PenscontriTot_diff)%></td>	
						 <td class="HighlightData"  colspan="4" align="right">&nbsp;</td>
						 

					</tr>
					<tr>
						<td colspan="17">
							<table width="100%" border="1" cellspacing="0" cellpadding="0">
							<tr>
						<td class="HighlightData" align="left" nowrap="nowrap" colspan="13">AAIEPF FORM 4  </td>
						<td class="HighlightData"   align="right"  colspan="12">Subscription amount</td>
						<td class="HighlightData"   align="right">Interest thereon</td> 
						
						<td class="HighlightData"   align="right">Contribution amount</td>
						<td class="HighlightData"   align="right">Interest thereon</td>
						<td class="HighlightData"   align="right">Adjustment in Pension Contri</td>
						<!--<td class="HighlightData"   align="right">Interest thereon</td> 
						--></tr>
						<tr>
						<td class="HighlightData" align="left" nowrap="nowrap" colspan="13"> &nbsp;</td>
						<td class="HighlightData"   align="right" colspan="12"><%=Math.round(empSubTot)%></td>
						<td class="HighlightData"   align="right"><%=Math.round(empSubIntrst)%></td> 						
						<td class="HighlightData"   align="right"><%=Math.round(AAiContriTot)%></td>
						<td class="HighlightData"   align="right"><%=Math.round(aaiContriIntrst)%></td>
						 <td class="HighlightData"   align="right"><%=Math.round(PenscontriTot)%></td>
						 <!--<td class="HighlightData"   align="right"><%=Math.round(PensionIntrst)%></td> 
						 
						--></tr>
						 </table>	 
						 	</td>
					</tr>
					 
					<%}} %>
    </table>
    </td>
    
    
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
			<td>
				<table border="0" style="border-color: gray;" cellpadding="2"
						cellspacing="0" width="100%" align="center">		
					<tr>
					  <td>&nbsp;</td>
					<td colspan="230">&nbsp;</td>
					 	<td align="center" > 
						</td>
				<td class="NumData"></td>
			 </tr>
			</table>
			
			
		</tr> 
    	 
   
  
 
  <!--   <tr>
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
	<%}%>	-->         					
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
</form>
  
</html>
