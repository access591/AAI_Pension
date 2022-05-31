
<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
        <td>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="100" height="50" /></td>
        <td>AIRPORTS AUTHORITY OF INDIA</td>
      </tr>
       <tr>
        <td width="38%">&nbsp;</td>
        <td width="55%">&nbsp;</td>
      </tr>
    
      <tr>
        <td colspan="3" align="center" class="reportlabel">MONTHLY SCHEDULE OF EMPLOYEE-WISE EDLI INSPECTION CHARGES FOR THE MONTH <font style="text-decoration: underline"><%=request.getAttribute("dspMonth")%></font> (FY<%=request.getAttribute("dspYear")%>.)</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <%
  Map map=new LinkedHashMap();
    	ArrayList stillInServiceList=new ArrayList();
    	ArrayList exitOfServiceList=new ArrayList();
    	ArrayList newJoinList=new ArrayList();
    	
  	String reportType="",fileName="",region="",stationName="",filePrefix="",filePostfix="";
  	CommonUtil commonUtil=new CommonUtil();
  	 DecimalFormat df = new DecimalFormat("########0.000");
  	
  	if(request.getAttribute("regionDesc")!=null){
  		region=(String)request.getAttribute("regionDesc");
  	}
  	if(request.getAttribute("airportcode")!=null){
  		stationName=(String)request.getAttribute("airportcode");
  	}
  	if("NO-SELECT".equals(stationName)){
  		stationName=region;
  		filePostfix=stationName;
  	}else{
  		filePostfix=region+"_"+stationName;
  	}
  	%>
 
    <%

  	  double nonCPFEmolumentTotal=0,nonCPFContGrandTotal=0,emolumentTotal=0,EPFTotal=0,VPFTotal=0,PFAdvTotal=0,PFAdvIntTotal=0,subGrandTotal=0,PFNetTotal=0,PFContTotal=0,contGrandTotal=0,outstandGrandTotal=0;
  	  double insPecChargesTot=0,edliEmolumentsTotal=0;
  	  double sisEmolumentTot=0,sisPcTot=0,sisInsPectionCharges=0,sisEdliEmoluments=0;
  	  double  eosEmolumentTot=0,eosPcTot=0,eosInsPectionCharges=0,eosEdliEmoluments=0;
  	  double newJEmolumentTot=0,newJPcTot=0,newJInsPectionCharges=0,newJEdliEmoluments=0;
  	  
  	if(request.getAttribute("map")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "AAI_EDLI_InspectionCharges_Report";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
			 map=(Map)request.getAttribute("map");
	  	Set existSet = map.entrySet();
				Iterator itr = existSet.iterator();
       	while (itr.hasNext()) {
		Map.Entry entry = (Map.Entry) itr.next();  
    if(entry.getKey().toString().equals("StillInService")){
  	stillInServiceList=(ArrayList)entry.getValue();
  	}else if(entry.getKey().toString().equals("ExitOfService")){
  	exitOfServiceList=(ArrayList)entry.getValue();
  	}else if(entry.getKey().toString().equals("newJoin")){
	newJoinList=(ArrayList)entry.getValue();	
	}
	}
	if(stillInServiceList!=null){
  %>
  <tr>
  	<td>&nbsp;</td>
  </tr>
    <tr>
  	<td>&nbsp;</td>
  </tr>
    <tr>
  	<td>Still In Service Employees:</td>
  </tr>
  <tr>
    <td align="center"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
		<td width="3%"  class="reportsublabel">Sl.No</td>
        <td width="8%"  class="reportsublabel">Member ID </td>
		<td width="8%"  class="reportsublabel" nowrap="nowrap">Member Name</td>
		<td width="8%"  class="reportsublabel">Date Of Birth </td>
		<td width="8%"  class="reportsublabel">Date Of Joining </td>
		<td align="center"  class="reportsublabel">Pension Option</td>            
        <td width="8%"  class="reportsublabel">Emoluments</td>
        <td width="8%"  class="reportsublabel">EDLI Emoluments</td>
        <td width="8%"  class="reportsublabel">PC</td>
        <td width="8%"  class="reportsublabel">Inspection Charges</td>

          <td align="center"  class="reportsublabel">Date Of Seperation</td>
        
          <td align="center"  class="reportsublabel">Reason For Leaving</td>
         
          <td align="center"  class="reportsublabel">Airport Code</td>
           <td align="center"  class="reportsublabel">Region</td>
           <td align="center"  class="reportsublabel">Remarks</td>
      </tr>

  
     <tr></tr>
     <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0,emolumentsByEpf=0,emolBypensionContri=0,pensionContri=0,epf=0,noofDays=0;
     	//add
     	
     	double emolumentTot=0,pcTot=0,insPectionCharges=0,edliEmoluments=0,nonCpfEmoluments=0;
     	String[] dobList=null;
     	String  recoveryStatus="",seperationReason="";
     	for(int sisList=0;sisList<stillInServiceList.size();sisList++){
     	String remarks="---";
		epfForm3Bean=(AaiEpfform3Bean)stillInServiceList.get(sisList);
		//srlno++;
		//System.out.println(epfForm3Bean.getPfID()+epfForm3Bean.getFormDifference());
		
		diff=Double.parseDouble(epfForm3Bean.getFormDifference());
		if(epfForm3Bean.getWetherOption().trim().equals("A")){
		if((Double.parseDouble(epfForm3Bean.getPensionAge())>58) && (epfForm3Bean.getSeperationreason().equals("Death") || epfForm3Bean.getSeperationreason().equals("Resignation") || epfForm3Bean.getSeperationreason().equals("Retirement") || epfForm3Bean.getSeperationreason().equals("VRS"))){
		epf=0;
		pensionContri=0;
		} else{
		epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
		pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		if(Double.parseDouble(epfForm3Bean.getPensionAge())==58){
		dobList = epfForm3Bean.getDateOfBirth().split("-");
		noofDays = Integer.parseInt(dobList[0])-1;
		epf= Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())* noofDays / Double.parseDouble(epfForm3Bean.getDaysBymonth()));
		pensionContri= Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		
		
		}
		//System.out.println("========option====="+epfForm3Bean.getWetherOption().trim()+"=====");
		if( epfForm3Bean.getWetherOption().trim().equals("B")|| epfForm3Bean.getWetherOption().toLowerCase().trim().equals("no")){
		    epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
			if(Double.parseDouble(epfForm3Bean.getPensionContribution())==541){ 
				pensionContri=541.45;
			}else{
			pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
			}
		}  
		
		/* if( 
		
		commonUtil.leadingZeros(5,epfForm3Bean.getPensionno()).equals("23245")){
		System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"-pensionContri-"+pensionContri);
		System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments()))+"==epf=="+epf);
		} */
		 emolumentsByEpf=Math.round((epf)*100 /12);			
		 emolBypensionContri = Math.round((pensionContri)*100 /8.33);
		 if(!epfForm3Bean.getRecoveryStatus().equals("")){
		 recoveryStatus = epfForm3Bean.getRecoveryStatus();	
		 seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;	
		 if(!epfForm3Bean.getSeperationreason().equals("---")){
		  seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;
		 }else{
		 seperationReason = recoveryStatus;
		 }
		 }else{
		  seperationReason = epfForm3Bean.getSeperationreason();
		  }
		//System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments())));
	  	// System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"--"+epfForm3Bean.getPensionContribution());
	 	emolumentTot=Double.parseDouble(epfForm3Bean.getOriginalEmoluments())+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	pcTot=Double.parseDouble(epfForm3Bean.getPensionContribution())+Double.parseDouble(epfForm3Bean.getNonCPFPC());
	 	
	 	nonCpfEmoluments=Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	System.out.println("===============B==="+epfForm3Bean.getPensionno()+"======"+epfForm3Bean.getEmoluments()+"====="+nonCpfEmoluments);
	 	//if(nonCpfEmoluments!=0){
	 	//remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 //	}else{
	 //	remarks="---";
	 //	}
	 	edliEmoluments=emolumentTot;
	 	if(epfForm3Bean.getWetherOption().trim().equals("B")){
	 	System.out.println("===============B========="+epfForm3Bean.getPensionno());
	 
	 	if(pcTot>541){
	 	pcTot=541;
	 	}
	 	}
	 	if(edliEmoluments>6500){
	 	edliEmoluments=6500;
	 	}
	 	
	 	if(edliEmoluments<0){
	 	edliEmoluments=0;
	 	pcTot=0;
	 	remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 	}
	 	insPectionCharges=(edliEmoluments*0.005/100);
	
	 	if(epfForm3Bean.getType().equals("StillInService")){
	 	srlno++;
     %>
      
      <tr <%=epfForm3Bean.getHighlightedColor()%>>
        <td  class="NumData"><%=srlno %></td>
        <td class="Data"><%=commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfBirth() %></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfJoining()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getWetherOption()%></td> 
        <td class="NumData" nowrap="nowrap" ><%=emolumentTot%></td> 
        <td class="NumData" nowrap="nowrap" ><%=edliEmoluments%></td>
        <td class="NumData" nowrap="nowrap"><%=pcTot%></td> 
        <td class="NumData" nowrap="nowrap"><%=df.format(insPectionCharges)%></td>   
       
            
          <%if (!epfForm3Bean.getSeperationDate().equals("---")){%>
        <td class="Data" nowrap="nowrap"><%=commonUtil.converDBToAppFormat(epfForm3Bean.getSeperationDate(),"dd-MMM-yyyy","dd/MM/yyyy")%></td>
        <%}else{%> 
            <td class="Data" nowrap="nowrap">---</td>
        <%}%>
        <td class="Data" nowrap="nowrap"><%=seperationReason%></td>
         
            <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
 			<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
 			<td class="Data" nowrap="nowrap"><%=remarks%></td>
      </tr>
      <%}
      
      
     
      sisInsPectionCharges+=Double.parseDouble(df.format(insPectionCharges));
	 sisEmolumentTot+=emolumentTot;
	
	 sisPcTot+=pcTot;
	 
	sisEdliEmoluments+=edliEmoluments;
      
	insPecChargesTot+=Double.parseDouble(df.format(insPectionCharges));
	 emolumentTotal+=emolumentTot;
	
	 contGrandTotal+=pcTot;
	 
	edliEmolumentsTotal+=edliEmoluments;
	}%>
	
    <tr>
    <td class="Data" colspan="6" align="right">Total</td>   
            
    <td class="NumData"><%=Math.round(sisEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(sisEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(sisPcTot)%></td> 
     <td class="NumData"><%=Math.round(sisInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
    
   
    
    </tr>
      
    </table></td>
  </tr>
  <%}
  	if(exitOfServiceList!=null){
  %>
    <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
  	<td>Death/Resigned/Retirement/Termination  Employees:</td>
  </tr>
  
  <tr>
    <td align="center"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
     
        <td width="3%"  class="reportsublabel">Sl.No</td>
        <td width="8%"  class="reportsublabel">Member ID </td>
		<td width="8%"  class="reportsublabel" nowrap="nowrap">Member Name</td>
		<td width="8%"  class="reportsublabel">Date Of Birth </td>
		<td width="8%"  class="reportsublabel">Date Of Joining </td>
		<td align="center"  class="reportsublabel">Pension Option</td>            
        <td width="8%"  class="reportsublabel">Emoluments</td>
        <td width="8%"  class="reportsublabel">EDLI Emoluments</td>
        <td width="8%"  class="reportsublabel">PC</td>
        <td width="8%"  class="reportsublabel">Inspection Charges</td>

          <td align="center"  class="reportsublabel">Date Of Seperation</td>
        
          <td align="center"  class="reportsublabel">Reason For Leaving</td>
         
          <td align="center"  class="reportsublabel">Airport Code</td>
           <td align="center"  class="reportsublabel">Region</td>
           <td align="center"  class="reportsublabel">Remarks</td>
      </tr>

  
     <tr></tr>
     <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0,emolumentsByEpf=0,emolBypensionContri=0,pensionContri=0,epf=0,noofDays=0;
     	//add
     	
     	double emolumentTot=0,pcTot=0,insPectionCharges=0,edliEmoluments=0,nonCpfEmoluments=0;
     	String[] dobList=null;
     	String  recoveryStatus="",seperationReason="";
     	for(int eosList=0;eosList<exitOfServiceList.size();eosList++){
     	String remarks="---";
		epfForm3Bean=(AaiEpfform3Bean)exitOfServiceList.get(eosList);
		//srlno++;
		//System.out.println(epfForm3Bean.getPfID()+epfForm3Bean.getFormDifference());
		
		diff=Double.parseDouble(epfForm3Bean.getFormDifference());
		if(epfForm3Bean.getWetherOption().trim().equals("A")){
		if((Double.parseDouble(epfForm3Bean.getPensionAge())>58) && (epfForm3Bean.getSeperationreason().equals("Death") || epfForm3Bean.getSeperationreason().equals("Resignation") || epfForm3Bean.getSeperationreason().equals("Retirement") || epfForm3Bean.getSeperationreason().equals("VRS"))){
		epf=0;
		pensionContri=0;
		} else{
		epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
		pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		if(Double.parseDouble(epfForm3Bean.getPensionAge())==58){
		dobList = epfForm3Bean.getDateOfBirth().split("-");
		noofDays = Integer.parseInt(dobList[0])-1;
		if(epfForm3Bean.getDaysBymonth().equals("")){
			epfForm3Bean.setDaysBymonth("0");
		}
		epf= Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())* noofDays / Double.parseDouble(epfForm3Bean.getDaysBymonth()));
		pensionContri= Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		
		
		}
		//System.out.println("========option====="+epfForm3Bean.getWetherOption().trim()+"=====");
		if( epfForm3Bean.getWetherOption().trim().equals("B")|| epfForm3Bean.getWetherOption().toLowerCase().trim().equals("no")){
		    epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
			if(Double.parseDouble(epfForm3Bean.getPensionContribution())==541){ 
				pensionContri=541.45;
			}else{
			pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
			}
		}  
		
		/* if( 
		
		commonUtil.leadingZeros(5,epfForm3Bean.getPensionno()).equals("23245")){
		System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"-pensionContri-"+pensionContri);
		System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments()))+"==epf=="+epf);
		} */
		 emolumentsByEpf=Math.round((epf)*100 /12);			
		 emolBypensionContri = Math.round((pensionContri)*100 /8.33);
		 if(!epfForm3Bean.getRecoveryStatus().equals("")){
		 recoveryStatus = epfForm3Bean.getRecoveryStatus();	
		 seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;	
		 if(!epfForm3Bean.getSeperationreason().equals("---")){
		  seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;
		 }else{
		 seperationReason = recoveryStatus;
		 }
		 }else{
		  seperationReason = epfForm3Bean.getSeperationreason();
		  }
		//System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments())));
	  	// System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"--"+epfForm3Bean.getPensionContribution());
	 	emolumentTot=Double.parseDouble(epfForm3Bean.getOriginalEmoluments())+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	pcTot=Double.parseDouble(epfForm3Bean.getPensionContribution())+Double.parseDouble(epfForm3Bean.getNonCPFPC());
	 	
	 	nonCpfEmoluments=Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	System.out.println("===============B==="+epfForm3Bean.getPensionno()+"======"+epfForm3Bean.getEmoluments()+"====="+nonCpfEmoluments);
	 	//if(nonCpfEmoluments!=0){
	 	//remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 //	}else{
	 //	remarks="---";
	 //	}
	 	edliEmoluments=emolumentTot;
	 	if(epfForm3Bean.getWetherOption().trim().equals("B")){
	 	System.out.println("===============B========="+epfForm3Bean.getPensionno());
	 
	 	if(pcTot>541){
	 	pcTot=541;
	 	}
	 	}
	 	if(edliEmoluments>6500){
	 	edliEmoluments=6500;
	 	}
	 	
	 	if(edliEmoluments<0){
	 	edliEmoluments=0;
	 	pcTot=0;
	 	remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 	}
	 	insPectionCharges=(edliEmoluments*0.005/100);
	
	 	//if(epfForm3Bean.getType().equals("StillInService")){
	 	srlno++;
     %>
      
      <tr <%=epfForm3Bean.getHighlightedColor()%>>
        <td  class="NumData"><%=srlno %></td>
        <td class="Data"><%=commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfBirth() %></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfJoining()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getWetherOption()%></td> 
        <td class="NumData" nowrap="nowrap" ><%=emolumentTot%></td> 
        <td class="NumData" nowrap="nowrap" ><%=edliEmoluments%></td>
        <td class="NumData" nowrap="nowrap"><%=pcTot%></td> 
        <td class="NumData" nowrap="nowrap"><%=df.format(insPectionCharges)%></td>   
       
            
          <%if (!epfForm3Bean.getSeperationDate().equals("---")){%>
        <td class="Data" nowrap="nowrap"><%=commonUtil.converDBToAppFormat(epfForm3Bean.getSeperationDate(),"dd-MMM-yyyy","dd/MM/yyyy")%></td>
        <%}else{%> 
            <td class="Data" nowrap="nowrap">---</td>
        <%}%>
        <td class="Data" nowrap="nowrap"><%=seperationReason%></td>
         
            <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
 			<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
 			<td class="Data" nowrap="nowrap"><%=remarks%></td>
      </tr>
      <%
      //}
      
      
      
      eosInsPectionCharges+=Double.parseDouble(df.format(insPectionCharges));
	 eosEmolumentTot+=emolumentTot;
	
	 eosPcTot+=pcTot;
	 
	eosEdliEmoluments+=edliEmoluments;
      
	insPecChargesTot+=Double.parseDouble(df.format(insPectionCharges));
	 emolumentTotal+=emolumentTot;
	
	 contGrandTotal+=pcTot;
	 
	edliEmolumentsTotal+=edliEmoluments;
	}%>
	
    <tr>
    <td class="Data" colspan="6" align="right">Total</td>   
            
    <td class="NumData"><%=Math.round(eosEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(eosEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(eosPcTot)%></td> 
     <td class="NumData"><%=Math.round(eosInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
    
   
    
    </tr>
      
    </table></td>
  </tr>
  <%}
  if(newJoinList!=null){
  System.out.println("newJoinList=========="+newJoinList.size());
  %>
    <tr>
  	<td>&nbsp;</td>
  </tr>
  <tr>
  	<td>NewJoines:</td>
  </tr>
  
  <tr>
    <td align="center"><table width="100%" border="1" cellspacing="0" cellpadding="0">
      <tr>
     
        <td width="3%"  class="reportsublabel">Sl.No</td>
        <td width="8%"  class="reportsublabel">Member ID </td>
		<td width="8%"  class="reportsublabel" nowrap="nowrap">Member Name</td>
		<td width="8%"  class="reportsublabel">Date Of Birth </td>
		<td width="8%"  class="reportsublabel">Date Of Joining </td>
		<td align="center"  class="reportsublabel">Pension Option</td>            
        <td width="8%"  class="reportsublabel">Emoluments</td>
        <td width="8%"  class="reportsublabel">EDLI Emoluments</td>
        <td width="8%"  class="reportsublabel">PC</td>
        <td width="8%"  class="reportsublabel">Inspection Charges</td>

          <td align="center"  class="reportsublabel">Date Of Seperation</td>
        
          <td align="center"  class="reportsublabel">Reason For Leaving</td>
         
          <td align="center"  class="reportsublabel">Airport Code</td>
           <td align="center"  class="reportsublabel">Region</td>
           <td align="center"  class="reportsublabel">Remarks</td>
      </tr>

  
     
     <%
     	AaiEpfform3Bean epfForm3Bean=new AaiEpfform3Bean();
     	int srlno=0;
     	double diff=0,grandDiffe=0,emolumentsByEpf=0,emolBypensionContri=0,pensionContri=0,epf=0,noofDays=0;
     	//add
     	
     	double emolumentTot=0,pcTot=0,insPectionCharges=0,edliEmoluments=0,nonCpfEmoluments=0;
     	String[] dobList=null;
     	String  recoveryStatus="",seperationReason="";
     	for(int newJList=0;newJList<newJoinList.size();newJList++){
     	String remarks="---";
		epfForm3Bean=(AaiEpfform3Bean)newJoinList.get(newJList);
		//srlno++;
		//System.out.println(epfForm3Bean.getPfID()+epfForm3Bean.getFormDifference());
		
		diff=Double.parseDouble(epfForm3Bean.getFormDifference());
		if(epfForm3Bean.getWetherOption().trim().equals("A")){
		if((Double.parseDouble(epfForm3Bean.getPensionAge())>58) && (epfForm3Bean.getSeperationreason().equals("Death") || epfForm3Bean.getSeperationreason().equals("Resignation") || epfForm3Bean.getSeperationreason().equals("Retirement") || epfForm3Bean.getSeperationreason().equals("VRS"))){
		epf=0;
		pensionContri=0;
		} else{
		epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
		pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		if(Double.parseDouble(epfForm3Bean.getPensionAge())==58){
		dobList = epfForm3Bean.getDateOfBirth().split("-");
		noofDays = Integer.parseInt(dobList[0])-1;

		if(epfForm3Bean.getDaysBymonth().equals("")){
			epfForm3Bean.setDaysBymonth("0");
		}

		epf= Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury())* noofDays / Double.parseDouble(epfForm3Bean.getDaysBymonth()));
		pensionContri= Double.parseDouble(epfForm3Bean.getPensionContribution());
		} 
		
		
		}
		//System.out.println("========option====="+epfForm3Bean.getWetherOption().trim()+"=====");
		if( epfForm3Bean.getWetherOption().trim().equals("B")|| epfForm3Bean.getWetherOption().toLowerCase().trim().equals("no")){
		    epf =Double.parseDouble(epfForm3Bean.getEmppfstatury()); 
			if(Double.parseDouble(epfForm3Bean.getPensionContribution())==541){ 
				pensionContri=541.45;
			}else{
			pensionContri =Double.parseDouble(epfForm3Bean.getPensionContribution());
			}
		}  
		
		/* if( 
		
		commonUtil.leadingZeros(5,epfForm3Bean.getPensionno()).equals("23245")){
		System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"-pensionContri-"+pensionContri);
		System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments()))+"==epf=="+epf);
		} */
		 emolumentsByEpf=Math.round((epf)*100 /12);			
		 emolBypensionContri = Math.round((pensionContri)*100 /8.33);
		 if(!epfForm3Bean.getRecoveryStatus().equals("")){
		 recoveryStatus = epfForm3Bean.getRecoveryStatus();	
		 seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;	
		 if(!epfForm3Bean.getSeperationreason().equals("---")){
		  seperationReason = epfForm3Bean.getSeperationreason()+"-"+recoveryStatus;
		 }else{
		 seperationReason = recoveryStatus;
		 }
		 }else{
		  seperationReason = epfForm3Bean.getSeperationreason();
		  }
		//System.out.println("======emolumentsByEpf=="+emolumentsByEpf+"=Normal=="+epfForm3Bean.getEmoluments()+"==diff=="+(emolumentsByEpf-Double.parseDouble(epfForm3Bean.getEmoluments())));
	  	// System.out.println("pensionno=="+commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())+"==Normal=="+epfForm3Bean.getEmoluments()+"======emolBypensionContri=="+emolBypensionContri+"==diff=="+(emolBypensionContri-Double.parseDouble(epfForm3Bean.getEmoluments()))+"--"+epfForm3Bean.getPensionContribution());
	 	emolumentTot=Double.parseDouble(epfForm3Bean.getOriginalEmoluments())+Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	pcTot=Double.parseDouble(epfForm3Bean.getPensionContribution())+Double.parseDouble(epfForm3Bean.getNonCPFPC());
	 	
	 	nonCpfEmoluments=Double.parseDouble(epfForm3Bean.getNonCPFEmoluments());
	 	System.out.println("===============B==="+epfForm3Bean.getPensionno()+"======"+epfForm3Bean.getEmoluments()+"====="+nonCpfEmoluments);
	 	//if(nonCpfEmoluments!=0){
	 	//remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 //	}else{
	 //	remarks="---";
	 //	}
	 	edliEmoluments=emolumentTot;
	 	if(epfForm3Bean.getWetherOption().trim().equals("B")){
	 	System.out.println("===============B========="+epfForm3Bean.getPensionno());
	 
	 	if(pcTot>541){
	 	pcTot=541;
	 	}
	 	}
	 	if(edliEmoluments>6500){
	 	edliEmoluments=6500;
	 	}
	 	
	 	if(edliEmoluments<0){
	 	edliEmoluments=0;
	 	pcTot=0;
	 	remarks="Arrear/Suppli: "+nonCpfEmoluments;
	 	}
	 	insPectionCharges=(edliEmoluments*0.005/100);
	
	 	//if(epfForm3Bean.getType().equals("StillInService")){
	 	srlno++;
     %>
      
      <tr <%=epfForm3Bean.getHighlightedColor()%>>
        <td  class="NumData"><%=srlno %></td>
        <td class="Data"><%=commonUtil.leadingZeros(5,epfForm3Bean.getPensionno())%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEmployeeName()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfBirth() %></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getEdliDateOfJoining()%></td>
        <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getWetherOption()%></td> 
        <td class="NumData" nowrap="nowrap" ><%=emolumentTot%></td> 
        <td class="NumData" nowrap="nowrap" ><%=edliEmoluments%></td>
        <td class="NumData" nowrap="nowrap"><%=pcTot%></td> 
        <td class="NumData" nowrap="nowrap"><%=df.format(insPectionCharges)%></td>   
       
            
          <%if (!epfForm3Bean.getSeperationDate().equals("---")){%>
        <td class="Data" nowrap="nowrap"><%=commonUtil.converDBToAppFormat(epfForm3Bean.getSeperationDate(),"dd-MMM-yyyy","dd/MM/yyyy")%></td>
        <%}else{%> 
            <td class="Data" nowrap="nowrap">---</td>
        <%}%>
        <td class="Data" nowrap="nowrap"><%=seperationReason%></td>
         
            <td class="Data" nowrap="nowrap"><%=epfForm3Bean.getStation()%></td>
 			<td class="Data" nowrap="nowrap"><%=epfForm3Bean.getRegion()%></td>
 			<td class="Data" nowrap="nowrap"><%=remarks%></td>
      </tr>
      <%
      //}
       
     newJInsPectionCharges+=Double.parseDouble(df.format(insPectionCharges));
	 newJEmolumentTot+=emolumentTot;
	
	 newJPcTot+=pcTot;
	 
	newJEdliEmoluments+=edliEmoluments;
       
	insPecChargesTot+=Double.parseDouble(df.format(insPectionCharges));
	 emolumentTotal+=emolumentTot;
	
	 contGrandTotal+=pcTot;
	 
	edliEmolumentsTotal+=edliEmoluments;
	}%>
	
    <tr>
    <td class="Data" colspan="6" align="right">Total</td>   
            
    <td class="NumData"><%=Math.round(newJEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(newJEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(newJPcTot)%></td> 
     <td class="NumData"><%=Math.round(newJInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
      
    </tr>
    
    </table></td>
  </tr>
  <tr>
    <td> &nbsp;</td>
    </tr>
  <%}%>
  <tr>
    <td> &nbsp;</td>
    </tr>
  <tr>
  <td align="center">
  <table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td class="Data" colspan="6" align="right">Still In Service Employees Total</td>   
            
    <td class="NumData"><%=Math.round(sisEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(sisEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(sisPcTot)%></td> 
     <td class="NumData"><%=Math.round(sisInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
    
   
    
    </tr>
  <tr>
    <td class="Data" colspan="6" align="right">Death/Resigned/Retirement/Termination  Employees Total</td>   
            
    <td class="NumData"><%=Math.round(eosEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(eosEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(eosPcTot)%></td> 
     <td class="NumData"><%=Math.round(eosInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
    
   
    
    </tr>
  
     <tr>
    <td class="Data" colspan="6" align="right">NewJoines Total</td>   
            
    <td class="NumData"><%=Math.round(newJEmolumentTot)%></td>
    <td class="NumData"><%=Math.round(newJEdliEmoluments)%></td>
      
    <td class="NumData"><%=Math.round(newJPcTot)%></td> 
     <td class="NumData"><%=Math.round(newJInsPectionCharges)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
      
    </tr>
  <tr>
    <td class="Data" colspan="6" align="right">Grand Total</td>   
            
    <td class="NumData"><%=Math.round(emolumentTotal)%></td>
    <td class="NumData"><%=Math.round(edliEmolumentsTotal)%></td>
      
    <td class="NumData"><%=Math.round(contGrandTotal)%></td> 
     <td class="NumData"><%=Math.round(insPecChargesTot)%></td> 
       
    <td class="Data" colspan="6">&nbsp;</td> 
    
   
    
    </tr>
    </table>
    </td>
    </tr>
  
 <%}%>


  
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
    <tr>
    <td>Note:-This report shown upto  60 Years</td>
    </tr>
    <tr>    
    <td>Note:-Inspection Charges Calculated on 0.005% of EDLI Emoluments </td>
     
  </tr>           
</table>
</body>
</html>
