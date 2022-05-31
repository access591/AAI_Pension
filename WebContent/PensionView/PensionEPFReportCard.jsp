
<%@ page language="java" import="java.util.*,aims.common.CommonUtil,aims.common.DBUtils,java.sql.Connection,aims.dao.CommonDAO" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.*"%>
<%@ page import="aims.service.FinancialReportService"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.sound.midi.SysexMessage" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String userId = session.getAttribute("userid").toString();
	String signature= request.getAttribute("signature").toString();
	CommonDAO  commonDAO =  new aims.dao.CommonDAO();
	FinalSettlementBean bean =new FinalSettlementBean();
	DBUtils commonDB = new DBUtils();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
    <script type="text/javascript">
 
    function load(pensionno,station,finyear){
  		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		
		var url="<%=basePath%>search1?method=getEmolumentslog&pfId="+pensionno+"&airportcode="+station+"&dispYear="+finyear;
		
  		wind1 = window.open(url,"PFCardLog","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
		winOpened = true;
		wind1.window.focus();
  	}
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
		String fromdate="",todate="",obMessage="";
		String multipleFinalSettlementFlag="";
		int fromYear=0,toYear=0;
		CommonUtil commonUtil=new CommonUtil();
		fromdate=(String)request.getAttribute("fromYear");
		todate=(String)request.getAttribute("toYear");
		fromYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				fromdate, "dd-MMM-yyyy", "yyyy"));
		toYear=Integer.parseInt(commonUtil.converDBToAppFormat(
				todate, "dd-MMM-yyyy", "yyyy"));
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
    /*if(signature.equals("true")){
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
			dispSignPath=basePath+"PensionView/images/signatures/nilabjaroy.gif";
			dispDesignation="JT.General Manager(Fin.),A.A.I.,NSCBI Airport";

		}else if(userId.equals("NSCBFIN")){
     				signatureFlag=true;
     				dispSignStation="Kolkata-700 052";
     				mangerName="(PRASANTA DAS)";
     				dispDesignation="Manager(Finance), AAI,NSCBI Airport";
     				dispSignPath=basePath+"PensionView/images/signatures/PRASANTADAS.gif";	
    	   }
    	
    }*/
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
    		
   		}else if(dispYear.equals("2010-11")){
   			if(chkRegionString.equals("East Region")){
    			signatureFlag=true;
    			dispSignStation="Kolkata-700 052";
    			dispSignPath=basePath+"PensionView/images/signatures/nilabjaroy.gif";
    			dispDesignation="JT.General Manager(Fin.),A.A.I.,NSCBI Airport";
    		}else if(chkRegionString.equals("CHQNAD")){
     			signatureFlag=true;
     			dispSignStation="CHQ";
     			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			dispDesignation="Sr. Manager(Finance)";
     		 }else if(chkRegionString.equals("North Region")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(V K Sachdeva)";
      			dispDesignation="Asst.General Manager(F&A), AAI, NR";
     			dispSignPath=basePath+"PensionView/images/signatures/VKSachdeva.gif";
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
				}else if(chkStationString.toLowerCase().equals("CAP IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(B.R.DEENADAYALAN)";
      				dispDesignation="Manager (F & A)";
      				dispSignPath=basePath+"PensionView/images/signatures/DEENADAYALAN.gif";
				}else if(chkStationString.toLowerCase().equals("CHENNAI IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(R.HAYAGRIVAN)";
      				dispDesignation="Sr. Manager(F&A)";
      				dispSignPath=basePath+"PensionView/images/signatures/HAYAGRIVAN.gif";
     			}else if(chkStationString.toLowerCase().equals("KOLKATA PROJ".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(B.Karmakar)";
      				dispDesignation="Sr. Manager(Fin)";
      				dispSignPath=basePath+"PensionView/images/signatures/karmakar.gif";
      			}else if(chkStationString.toLowerCase().equals("TRIVANDRUM IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(SMT PARIMALA GNANADEVI J)";
					dispDesignation="Jt.General Manager(F&A)";
     				dispSignPath=basePath+"PensionView/images/signatures/Parimala.gif";	
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
   		}else if(dispYear.equals("2011-12")){
   			if(chkRegionString.equals("East Region")){
    			signatureFlag=true;
    			dispSignStation="Kolkata-700 052";
    			dispSignPath=basePath+"PensionView/images/signatures/nilabjaroy.gif";
    			dispDesignation="JT.General Manager(Fin.),A.A.I.,NSCBI Airport";
    		}else if(chkRegionString.equals("CHQNAD")){
     			signatureFlag=true;
     			dispSignStation="CHQ";
     			dispSignPath=basePath+"PensionView/images/signatures/Shaka Jain.gif";
     			dispDesignation="Sr. Manager(Finance)";
     		 }else if(chkRegionString.equals("North Region")){
     			signatureFlag=true;
     			dispSignStation="";
     			mangerName="(HANSA CHIDAMBARAM)";
      			dispDesignation="Sr. Manager(F&A), AAI, NR";
     			dispSignPath=basePath+"PensionView/images/signatures/HansaChidambaram.gif";
     		 }else if(chkRegionString.equals("South Region")){
     			signatureFlag=true;
     			dispSignStation="South Region";
     			dispDesignation="Deputy General Manager(F&A)";
     			dispSignPath=basePath+"PensionView/images/signatures/SVENKATESAN.gif";	
     		 }else if(chkRegionString.equals("RAUSAP")){
      			signatureFlag=true;
      			dispSignStation="";
      			mangerName="(MANISH GUPTA)";
      			dispDesignation="Sr. Manager(F & A), AAI, RAU,SAP ";
      			dispSignPath=basePath+"PensionView/images/signatures/MANISHGUPTA.gif";	
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
				}else if(chkStationString.toLowerCase().equals("CAP IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(R.GOPALAKRISHNAN)";
      				dispDesignation="Manager (F&A)";
      				dispSignPath=basePath+"PensionView/images/signatures/RGOPALAKRISHNA.gif";
				}else if(chkStationString.toLowerCase().equals("CHENNAI IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(K.THILAGAVATHY)";
      				dispDesignation="Sr. Manager(F&A)";
      				dispSignPath=basePath+"PensionView/images/signatures/THILAGAVATHY.gif";
     			}else if(chkStationString.toLowerCase().equals("KOLKATA PROJ".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(B.Karmakar)";
      				dispDesignation="Sr. Manager(Fin)";
      				dispSignPath=basePath+"PensionView/images/signatures/karmakar.gif";
      			}else if(chkStationString.toLowerCase().equals("TRIVANDRUM IAD".toLowerCase())){
     				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(S.VENKATESAN)";
					dispDesignation="Deputy General Manager(F&A)";
     				dispSignPath=basePath+"PensionView/images/signatures/SVENKATESAN.gif";	
     			}else if(chkStationString.toLowerCase().equals("IGICargo IAD".toLowerCase())){
      				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(V K Sachdeva)";
      				dispDesignation="Asst.General Manager(F&A), AAI,IGICargo IAD";
      				dispSignPath=basePath+"PensionView/images/signatures/VKSachdeva.gif";	
     		 	}else if(chkStationString.toLowerCase().equals("IGI IAD".toLowerCase())){
      				signatureFlag=true;
      				dispSignStation="";
      				mangerName="(V K Sachdeva)";
      				dispDesignation="Asst.General Manager(F&A), AAI,IGI IAD";
      				dispSignPath=basePath+"PensionView/images/signatures/VKSachdeva.gif";	
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
	  	//pfcardchanges
	  	ArrayList dateCardList1=new ArrayList();
	  	ArrayList dataPTWList1=new ArrayList();
	  	
	  	ArrayList dataPTWList=new ArrayList();
	  	
	  	ArrayList dataFinalSettlementList=new ArrayList();
  		EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
  		
		for(int cardList=0;cardList<cardReportList.size();cardList++){
		cardReport=(EmployeeCardReportInfo)cardReportList.get(cardList);
		personalInfo=cardReport.getPersonalInfo();
		System.out.println("PF ID String"+personalInfo.getPfIDString()+"Adj Date"+personalInfo.getAdjDate());
		dateCardList=cardReport.getPensionCardList();
		dataPTWList=cardReport.getPtwList();
		//pfcardchanges
		dateCardList1=cardReport.getPensionCardList1();
		dataPTWList1=cardReport.getPtwList1();
		
		noOfmonthsForInterest=cardReport.getNoOfMonths();
		multipleFinalSettlementFlag=cardReport.getMutiplePaymentFlag();

		
		intMonths=cardReport.getInterNoOfMonths();

		arrearMonths=cardReport.getArrearNoOfMonths();
		System.out.println("noOfmonthsForInterest============================"+noOfmonthsForInterest+"intMonths"+intMonths+"arrearMonths"+arrearMonths);
		dataFinalSettlementList=cardReport.getFinalSettmentList();
        arrearInfo=cardReport.getArrearInfo();
        orderInfo=cardReport.getOrderInfo();
        String[] arrearData=arrearInfo.split(",");
        double arrearAmount=0.00,arrearContri=0.00;
        boolean alreadyfinal=false,finalrateofinterest=false,isFrozedSeperation=false;
        String arrearDate="";
        isFrozedSeperation=personalInfo.isFrozedSeperationAvail();
        String finalsettlmentdt=personalInfo.getFinalSettlementDate();
        String resettlmentdt="";
        
       
      
       
       if(!personalInfo.getFinalSettlementDate().equals("---")){
        	if ((!commonDAO.compareFinalSettlementDates(fromdate,todate,personalInfo.getFinalSettlementDate())) && personalInfo.isCrossfinyear()==false && fromYear>=2011){
        		finalsettlmentdt=personalInfo.getResttlmentDate();
        		alreadyfinal=true;
    		}
        	System.out.println("fromYear============================"+fromYear+"toYear"+toYear+personalInfo.getResttlmentDate());
        	if (multipleFinalSettlementFlag.equals("true")){
        		resettlmentdt=personalInfo.getResttlmentDate();
        		
    		}
        
        } 
    
        arrearDate=arrearData[0];
        arrearAmount=Double.parseDouble(arrearData[2]);
        arrearContri=Double.parseDouble(arrearData[3]);
         
        String[] sanctionOrderInfo = null;
        String sanctionOrder1 = null;
        String sanctionOrder2 = null;
        String []sanctionOrderInfo1 = null;
        String []sanctionOrderInfo2 = null;
        String sono="",sanctionDate=""; 
        String sono1="",sanctionDate1=""; 
         if(!orderInfo.equals("")){
         if(multipleFinalSettlementFlag.equals("true"))
         {
         if(orderInfo.indexOf("@")!=-1){
         sanctionOrderInfo = orderInfo.split("@");
         sanctionOrder1=sanctionOrderInfo[0];
         sanctionOrder2=sanctionOrderInfo[1];
         sanctionOrderInfo1=sanctionOrder1.split(",");
         sono = sanctionOrderInfo1[0];
         sanctionDate = sanctionOrderInfo1[1];
         sanctionOrderInfo2=sanctionOrder2.split(",");
         sono1 = sanctionOrderInfo2[0];
         sanctionDate1 = sanctionOrderInfo2[1];
         }
         }else{
         
         sanctionOrderInfo = orderInfo.split(",");
         sono = sanctionOrderInfo[0];
         sanctionDate = sanctionOrderInfo[1];
         }
         }
          
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
        <td width="6%" class="label" rowspan="2">PENSION<br/>CONTR. </td>
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
  		//pfcard changes
  		EmployeePensionCardInfo pensionCardInfo1=new EmployeePensionCardInfo();
  		
		Long empNetOB=null,aaiNetOB=null,pensionOB=null,principalOB=null;
		Long cpfAdjOB=null,pensionAdjOB=null,pfAdjOB=null,empSubOB=null,adjPensionContri=null,adjOutstandadv=null;
		String fsEmpSub="",fsAaicontri="",fsFinalPencontri="",fsFinalNetAmt="",fsinfo="",fsNoOfmnths="",fsmsg="";
		String fsEmpSub1="",fsAaicontri1="",fsFinalPencontri1="",fsFinalNetAmt1="",fsinfo1="",fsNoOfmnths1="",fsmsg1="";
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
             obMessage=(String)obList.get(18);
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
  	if(multipleFinalSettlementFlag.equals("true")){
  		for(int k=0;k<dataFinalSettlementList.size();k++){
  		System.out.println("  k value::::"+k);
  		bean=(FinalSettlementBean)dataFinalSettlementList.get(k);
  		
  		
  		
  		if(k==0){
  		fsEmpSub=bean.getFinEmp();
			fsAaicontri=bean.getFinAai();
			fsFinalPencontri=bean.getPenCon();
			fsFinalNetAmt=bean.getNetAmount();
			fsinfo=bean.getSettlementDate();
			fsNoOfmnths=bean.getNoofMonths();
			fsmsg=bean.getFinalSettlementMsg();
  		System.out.println(" 1st "+bean.getSettlementDate()+" "+bean.getFinEmp()+" "+bean.getFinalSettlementMsg());
  		}
  			if(k==1){
  			//System.out.println("emp suuuuuuuuuuuuuuuuubbbbbbbbbbbbbbb"+bean.getFinEmp());
  			fsEmpSub1=bean.getFinEmp();
			fsAaicontri1=bean.getFinAai();
			fsFinalPencontri1=bean.getPenCon();
			fsFinalNetAmt1=bean.getNetAmount();
			fsinfo1=bean.getSettlementDate();
			fsNoOfmnths1=bean.getNoofMonths();
			fsmsg1=bean.getFinalSettlementMsg();
  		System.out.println(" 2nd  "+bean.getSettlementDate()+" "+bean.getFinEmp()+" "+bean.getFinalSettlementMsg());
  		}
  		System.out.println("fsNoOfmnthsfsNoOfmnthsfsNoOfmnthsfsNoOfmnthsfsNoOfmnthsfsNoOfmnths"+fsNoOfmnths);
  		}
  		}
  		//System.out.println("final settlement date" +personalInfo.getFinalSettlementDate());
  		//System.out.println("pensionCardInfo.getTransArrearFlag()" +pensionCardInfo.getTransArrearFlag()+"personalInfo.getChkArrearFlag()"+personalInfo.getChkArrearFlag());
  		if(((Integer.parseInt(calYear)>=2010 && dataFinalSettlementList.size()==0) && pensionCardInfo.getTransArrearFlag().equals("Y"))||((pensionCardInfo.getTransArrearFlag().equals("N")) ||(pensionCardInfo.getTransArrearFlag().equals("P")))||(pensionCardInfo.getTransArrearFlag().equals("Y")&&(personalInfo.getChkArrearFlag().equals("N")))){
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
  				System.out.println("totalArrearEmpNet" +totalArrearEmpNet+"totalArrearAAINet"+totalArrearAAINet);
       			 arrearFlag="Y";
       			 arrearRemarks="ARREARS";
  		}
  		//System.out.println("totalArrearEmpNet===========================" +totalArrearEmpNet+"totalArrearAAINet"+totalArrearAAINet+"pensionCardInfo.getGrandArrearEmpNetCummulative()"+pensionCardInfo.getGrandArrearEmpNetCummulative()+"pensionCardInfo.getGrandArrearAAICummulative()"+pensionCardInfo.getGrandArrearAAICummulative());
  		
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
  		pfcardNarration = pensionCardInfo.getPfcardNarration();
  			if(!arrearRemarks.equals("---")||pensionCardInfo.getSupflag().equals("Y") || pensionCardInfo.getPfcardNarrationFlag().equals("Y")){
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
        <% 
     if(!adjOutstandadv.equals("0")&& Integer.parseInt(adjOutstandadv.toString())!=0 && (dispYear.equals("2009-10")|| dispYear.equals("2010-11") || dispYear.equals("2011-12")|| dispYear.equals("2012-13")|| dispYear.equals("2013-14") || dispYear.equals("2014-15"))){%>
        <td class="NumData"><a  title="Click the link to view Transaction log" target="_self" href="javascript:void(0)" onclick="javascript:load('<%=personalInfo.getPfID()%>','<%=adjStation%>','<%=dispYear%>');"><%=adjOutstandadv%></a></td>
       <% } else{ %>
        <td class="NumData"><%=adjOutstandadv%></td>
        <%}%>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <%if(!empSubOB.equals("0")&& Integer.parseInt(empSubOB.toString())!=0 && (dispYear.equals("2009-10")|| dispYear.equals("2010-11") || dispYear.equals("2011-12")|| dispYear.equals("2012-13")|| dispYear.equals("2013-14")|| dispYear.equals("2014-15"))){%>
        <td class="NumData"><a  title="Click the link to view Transaction log" target="_self" href="javascript:void(0)" onclick="javascript:load('<%=personalInfo.getPfID()%>','<%=adjStation%>','<%=dispYear%>');"><%=empSubOB%></a></td>
        <%} else{%>
        <td class="NumData"><%=empSubOB%></td>
        <%} %>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
       
<% 
     if(!pensionAdjOB.equals("0")&& Integer.parseInt(pensionAdjOB.toString())!=0 && (dispYear.equals("2009-10")|| dispYear.equals("2010-11") || dispYear.equals("2011-12")|| dispYear.equals("2012-13")|| dispYear.equals("2013-14")|| dispYear.equals("2014-15"))){%>
 <td class="NumData"><a  title="Click the link to view Transaction log" target="_self" href="javascript:void(0)" onclick="javascript:load('<%=personalInfo.getPfID()%>','<%=adjStation%>','<%=dispYear%>');"><%=pensionAdjOB%></a></td>
 <%} else{%>
 <td class="NumData"><%=pensionAdjOB%></td>
<%} %>
	<%if(!adjPensionContri.equals("0")&& Integer.parseInt(adjPensionContri.toString())!=0 && (dispYear.equals("2009-10")|| dispYear.equals("2010-11") || dispYear.equals("2011-12")|| dispYear.equals("2012-13")|| dispYear.equals("2013-14") || dispYear.equals("2014-15"))){%>
        <td class="NumData"><a  title="Click the link to view Transaction log" target="_self" href="javascript:void(0)" onclick="javascript:load('<%=personalInfo.getPfID()%>','<%=adjStation%>','<%=dispYear%>');"><%=adjPensionContri%></a></td>
         <%} else{%>
        <td class="NumData"><%=adjPensionContri%></td>
        <%} %>
        
        <td>&nbsp;</td>
           <%if (!adjNarration.equals("---")&& !adjNarration.trim().equals("")){%>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=adjNarration%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=adjNarration.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="8%" class="Data">&nbsp;</td>
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

	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo.getPensionContribution()))%></td>
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo.getStation()%></td>
	 
	  <%if (!arrearRemarks.equals("---")){
	  if(arrearRemarks.length()<7){
	  %>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=arrearRemarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=arrearRemarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=arrearRemarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=arrearRemarks.substring(0,7)+"..." %></td>
	   <%}}else{%>
	 	<td width="8%" class="Data"><%=arrearRemarks %></td>
	 <%}%>
	 </tr>
	
	 <%
	  
		
	 	if(pensionCardInfo.getCbFlag().equals("Y")){
	 		Long finalEmpNetOB=null,finalAaiNetOB=null,finalPensionOB=null,finalPrincipalOB=null,finalStlmentEmpNet=null,finalStlmentNetAmount=null,finalStlmentAAICon=null,finalStlmentPensDet=null;
	 		Long finalStlmentEmpNet1=null,finalStlmentAAICon1=null;
	 		
	 		Long finalEmpNetOBIntrst=null,finalAaiNetOBIntrst=null,finalPensionOBIntrst=null;
	 		
	 		if(obList.size()!=0){
	 		Connection con=null;
	 		con=commonDB.getConnection();
	 		String fromyear=dispYear.substring(0,4);
	 		String toyear=""+(Integer.parseInt(dispYear.substring(0,4))+1);
	 		
	 		
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
	 				} else if(Integer.parseInt(calYear)>=2012 && Integer.parseInt(calYear)<2013){
	 					rateOfInterest=9.50;
	 				} else if(Integer.parseInt(calYear)>=2013 && Integer.parseInt(calYear)<2014){
	 				if(commonDAO.getRateOfIntFlag(con,fromyear,toyear,personalInfo.getPensionNo())){
	 					rateOfInterest=8.25;
	 					}else{
	 					rateOfInterest=9.50;
	 					}
	 				} else if(Integer.parseInt(calYear)>=2014 && Integer.parseInt(calYear)<2015){
	 					rateOfInterest=9.25;
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
	 				double advint=0.0,pfwdrwn=0.0;
	 				//System.out.println("prasad=============="+totalEmpNet+"000"+totalAAINet);
	 				if(advancePFWPaid>0 && (dispYear.equals("2013-14")|| dispYear.equals("2014-15"))){
	 				advint= new Double(df.format((advancePFWPaid * rateOfInterest)/100/12)).doubleValue();
	 				//System.out.println("advint======="+advint);
	 				}
	 				if(totalPFDrwn>0 && (dispYear.equals("2013-14")|| dispYear.equals("2014-15"))){
	 				pfwdrwn= new Double(df.format((totalPFDrwn * rateOfInterest)/100/12)).doubleValue();
	 				}
	 				 //System.out.println("totalEmpNet=============="+totalEmpNet+"intMonths"+intMonths);
	 					 empNetInterest=new Double(df.format(((totalEmpNet*rateOfInterest)/100/intMonths))).doubleValue()-advint;
	 				    aaiNetInterest=new Double(df.format(((totalAAINet*rateOfInterest)/100/intMonths))).doubleValue()-pfwdrwn;
	 				    //System.out.println("empNetInterest=============="+empNetInterest+"aaiNetInterest"+aaiNetInterest);
	 				  
	 				}else{
	 					empNetInterest=0.0;
	 					aaiNetInterest=0.0;
	 				}
	 				if(arrearMonths!=0 && isFrozedSeperation==true){
	 					 arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/arrearMonths))).doubleValue();
						 empNetInterest=empNetInterest+arrearEmpNetInterest;
						 aaiNetInterest=aaiNetInterest+arrearAaiNetInterest;
						 //System.out.println("----rateOfInterest------"+rateOfInterest+"arrearEmpNetInterest"+arrearEmpNetInterest+"----totalGrandArrearEmpNet---ssss---"+totalGrandArrearEmpNet+"arrearMonths"+arrearMonths);
	 				}
	 				
				    //System.out.println("isArreerintflag"+personalInfo.isArreerintflag()+"isFrozedSeperation"+isFrozedSeperation);
				    if(totalGrandArrearEmpNet!=0.0 && totalGrandArrearAaiNet!=0.0 && personalInfo.isArreerintflag()!=true && isFrozedSeperation==false){
				    arrearEmpNetInterest=new Double(df.format(((totalGrandArrearEmpNet*rateOfInterest)/100/intMonths))).doubleValue();
				    arrearAaiNetInterest=new Double(df.format(((totalGrandArrearAaiNet*rateOfInterest)/100/intMonths))).doubleValue();
				    }else{
				    	arrearEmpNetInterest=0.0;
				    	arrearAaiNetInterest=0.0;
				    }
				    //System.out.println("----rateOfInterest------"+rateOfInterest+"totalEmpNet"+totalEmpNet+"----empNetInterest------"+empNetInterest+"intMonths"+intMonths);
     			      //System.out.println("----totalGrandArrearEmpNet------"+totalGrandArrearEmpNet+arrearEmpNetInterest+"totalGrandArrearAaiNet"+totalGrandArrearAaiNet+arrearAaiNetInterest);
				  //   System.out.println("arrearEmpNetInterest"+arrearEmpNetInterest+"arrearAaiNetInterest"+arrearAaiNetInterest+"noOfmonthsForInterest"+noOfmonthsForInterest);
				    //For Pension Contribution attribute,we are not cummlative
				    pensionInterest=new Double(df.format(((totalPensionContr*rateOfInterest)/100))).doubleValue();
				    totalEmpIntNet=new Double(Math.round(empNetInterest+totalEmpNet)).doubleValue();
				    totalAaiIntNet=new Double(Math.round(totalAAINet+aaiNetInterest)).doubleValue();
				    if(personalInfo.isNoInterest()){
				    	empNetInterest=0.0;
				    	aaiNetInterest=0.0;
				    	arrearEmpNetInterest=0.0;
				    	arrearAaiNetInterest=0.0;
				    	pensionInterest=0.0;
				    }
				    System.out.println("----personalInfo.isNoInterest()------"+personalInfo.isNoInterest()+personalInfo.isObInterst()+"personalInfo.isAdjObInterst()"+personalInfo.isAdjObInterst());
				    System.out.println("----"+rateOfInterest+obList+Math.round(dispTotalAAINet)+Math.round(aaiNetInterest)+Math.round(dispTotalEmpNet)+Math.round(empNetInterest)+Math.round(totalPensionContr)+Math.round(pensionInterest)+totalAdvances+principle+noOfmonthsForInterest+personalInfo.isObInterst()+personalInfo.isAdjObInterst());
				   	closingOBList=reportService.calClosingOB(rateOfInterest,obList,Math.round(dispTotalAAINet),Math.round(aaiNetInterest),Math.round(dispTotalEmpNet),Math.round(empNetInterest),Math.round(totalPensionContr),Math.round(pensionInterest),totalAdvances,principle,noOfmonthsForInterest,personalInfo.isObInterst(),personalInfo.isAdjObInterst());
				 														
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
	 <td width="6%" class="NumData"><%=df1.format(totalPensionContr)%></td>
	  <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 </tr>
	  <tr>
	 <td width="50" nowrap="nowrap" class="label">INTEREST</td>
	 <td width="116" class="NumData"><%=rateOfInterest%></td>

	  <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOBIntrst)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOBIntrst)%></td>
	  <% if (!obMessage.equals("")){%>
	   <td  colspan="4" class="Data" ><%=obMessage%> </td>
	  
	  <%}else{ %>
	  	   <td width="3%" class="NumData" nowrap="nowrap">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  <%}%>
	 
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalPrincipalOB)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(finalEmpNetOB)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(finalAaiNetOB)%></td>
	 
	 <td width="6%" class="NumData"><%=finalPensionOB%></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%
	if(dataFinalSettlementList.size()!=0){
	//finalStlmentEmpNet=new Long(Long.parseLong((String)dataFinalSettlementList.get(5)));
	//finalStlmentAAICon=new Long(Long.parseLong((String)dataFinalSettlementList.get(6)));
	//finalStlmentPensDet=new Long(Long.parseLong((String)dataFinalSettlementList.get(7)));
	//finalStlmentNetAmount=new Long(Long.parseLong((String)dataFinalSettlementList.get(8)));
	//noOfAfterfnlrtmntmonhts=Integer.parseInt((String)dataFinalSettlementList.get(13));
	///finalInterestCal=12-noOfmonthsForInterest;
	if(multipleFinalSettlementFlag.equals("true")){
	finalStlmentEmpNet1=new Long(Long.parseLong(fsEmpSub1));
	finalStlmentAAICon1=new Long(Long.parseLong(fsAaicontri1));
	
	finalStlmentEmpNet=new Long(Long.parseLong(fsEmpSub));
	finalStlmentAAICon=new Long(Long.parseLong(fsAaicontri));
	finalStlmentPensDet=new Long(Long.parseLong(fsFinalPencontri));
	finalStlmentNetAmount=new Long(Long.parseLong(fsFinalNetAmt));
	noOfAfterfnlrtmntmonhts=Integer.parseInt(fsNoOfmnths);
	finalInterestCal=12-noOfmonthsForInterest;
	}else{
	finalStlmentEmpNet=new Long(Long.parseLong((String)dataFinalSettlementList.get(5)));
	finalStlmentAAICon=new Long(Long.parseLong((String)dataFinalSettlementList.get(6)));
	finalStlmentPensDet=new Long(Long.parseLong((String)dataFinalSettlementList.get(7)));
	finalStlmentNetAmount=new Long(Long.parseLong((String)dataFinalSettlementList.get(8)));
	noOfAfterfnlrtmntmonhts=Integer.parseInt((String)dataFinalSettlementList.get(13));
	System.out.println("noOfAfterfnlrtmntmonhts:::"+noOfAfterfnlrtmntmonhts);
	fsmsg=(String)dataFinalSettlementList.get(12);
	fsinfo=(String)dataFinalSettlementList.get(11);
	if(dispYear.equals("2013-14")||dispYear.equals("2014-15")){
		if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==false){
	finalInterestCal=12-noOfmonthsForInterest;
	}else{
	finalInterestCal=noOfAfterfnlrtmntmonhts;
	}
	}else{
	finalInterestCal=12-noOfmonthsForInterest;

	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==false){
		noOfAfterfnlrtmntmonhts=12-noOfmonthsForInterest;

	}
	
	}
	}
	
	
	System.out.println("noOfAfterfnlrtmntmonhts:::"+noOfAfterfnlrtmntmonhts+"hgfh:::::"+isFrozedSeperation);
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==false){
		//noOfAfterfnlrtmntmonhts=12-noOfmonthsForInterest;
		System.out.println("noOfAfterfnlrtmntmonhts:::"+noOfAfterfnlrtmntmonhts);
	}
	if(noOfAfterfnlrtmntmonhts==0 && isFrozedSeperation==true ){
		finalInterestCal=0;
		//System.out.println("finalInterestCal:::"+finalInterestCal);
	}
	//System.out.println("dataFinalSettlementList"+(String)dataFinalSettlementList.get(8)+"finalStlmentEmpNet=="+finalStlmentEmpNet+"finalStlmentNetAmount"+finalStlmentNetAmount);
	long netcloseEmpNet=(finalEmpNetOB.longValue())+(-finalStlmentEmpNet.longValue());
	long netcloseNetAmount=(finalAaiNetOB.longValue())+(-finalStlmentAAICon.longValue());
	double remaininInt1=arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
	double remainAaiInt1=arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	double remaininInt=0.00,remainAaiInt=0.00;
	
	//System.out.println("personalInfo.getFinalSettlementDate()"+personalInfo.getFinalSettlementDate()+"Resettlemnt date"+personalInfo.getResttlmentDate()+netcloseEmpNet+"finalInterestCal"+finalInterestCal);
	//System.out.println("totalArrearEmpNet"+totalArrearEmpNet+"arrearAaiNetInterest"+arrearAaiNetInterest+netcloseEmpNet);
	//System.out.println("finalInterestCal"+finalInterestCal+"noOfAfterfnlrtmntmonhts-----------"+noOfAfterfnlrtmntmonhts+"========isFrozedSeperation========="+isFrozedSeperation+"noOfmonthsForInterest"+noOfmonthsForInterest);
	if(!personalInfo.getFinalSettlementDate().equals("---")){
	if(noOfAfterfnlrtmntmonhts<0)
	noOfAfterfnlrtmntmonhts=0;
	if((isFrozedSeperation==true && personalInfo.getChksepmnths()<=0)&& (dispYear.equals("2013-14")||dispYear.equals("2014-15"))){
	//System.out.println("dddddddddddddddddddddd"+personalInfo.getChksepmnths());
	noOfAfterfnlrtmntmonhts=0;
	finalInterestCal=0;
	}
			if(!personalInfo.getResttlmentDate().equals("---")){
				 remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			 remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
	 			
			}else{
			//increase fin year After mar month
			if(dispYear.equals("2014-15")){
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*noOfAfterfnlrtmntmonhts);
			
			}else{
				remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	
				}
			}
	}else{
			 	remaininInt=totalArrearEmpNet+arrearEmpNetInterest+Math.round((netcloseEmpNet*rateOfInterest/100/12)*finalInterestCal);
			 	remainAaiInt=totalArrearAAINet+arrearAaiNetInterest+Math.round((netcloseNetAmount*rateOfInterest/100/12)*finalInterestCal);
	
	}
	 if(personalInfo.isAfterFSInt()){
	 			 remaininInt=0;
			 	 remainAaiInt=0;
	 			 }
	 			 double remainempint2=0.0, remainaaiInt2=00;
	 			 if(multipleFinalSettlementFlag.equals("true")){
 	 	remainempint2=(((netcloseEmpNet+remaininInt)-finalStlmentEmpNet1.longValue())*rateOfInterest/100/12)*Integer.parseInt(fsNoOfmnths1);
     	remainaaiInt2=(((netcloseNetAmount+remainAaiInt)-finalStlmentAAICon1.longValue())*rateOfInterest/100/12)*Integer.parseInt(fsNoOfmnths1);
 }
 %>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">FINAL/RE SETTLEMENT (<%=finalsettlmentdt%>)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=-finalStlmentEmpNet.longValue()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=-finalStlmentAAICon.longValue()%></td>
	 <%if(!orderInfo.equals("")){%>
	 <td width="8%"  colspan ="3" class="Data"  nowrap="nowrap" onmouseover="showTip('Sanction order No: <%=sono%> Sanction Order Dt: <%=sanctionDate%> Payment.Dt: <%=fsinfo %>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";>Sanction Order Details...</td>
	 <%} else if(!fsinfo.equals("")){%>
	 <td width="8%"  colspan ="3" class="Data"  nowrap="nowrap" onmouseover="showTip('Sanction order No: -NA-,  Sanction Order Dt: -NA-,  Payment.Dt: <%=fsinfo %>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";>Sanction Order Details...</td>
	 <%}%>
 </tr>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NET CLOSING (A)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=netcloseEmpNet%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=netcloseNetAmount%></td>
	 <td colspan="5" class="Data" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
   
 <%
 System.out.println("arrearFlag==================="+arrearFlag);
 if(arrearFlag.equals("Y")){ %>
      <tr>
	 <td colspan="2" nowrap="nowrap" class="label">ARREARS AMOUNT (B)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(totalArrearEmpNet)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(totalArrearAAINet)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=fsmsg%> (C)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remaininInt1)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainAaiInt1)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>

 <% }else{ %>
  <tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=fsmsg%> (B)</td>
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
 <%
 if(multipleFinalSettlementFlag.equals("true")){ %>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label">RE SETTLEMENT (<%=resettlmentdt%>)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=-finalStlmentEmpNet1.longValue()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=-finalStlmentAAICon1.longValue()%></td>
	 <%if(!orderInfo.equals("")){%>
	 <td width="8%"  colspan ="3" class="Data"  nowrap="nowrap" onmouseover="showTip('Sanction order No: <%=sono1%> Sanction Order Dt: <%=sanctionDate1%> Payment.Dt: <%=fsinfo1 %>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";>Sanction Order Details...</td>
	 <%} else if(!fsinfo.equals("")){%>
	 <td width="8%"  colspan ="3" class="Data"  nowrap="nowrap" onmouseover="showTip('Sanction order No: -NA-,  Sanction Order Dt: -NA-,  Payment.Dt: <%=fsinfo1 %>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";>Sanction Order Details...</td>
	 <%}%>
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NET CLOSING (D)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format((netcloseEmpNet+remaininInt)-finalStlmentEmpNet1.longValue())%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format((netcloseNetAmount+remainAaiInt)-finalStlmentAAICon1.longValue())%></td>
	 <td colspan="5" class="Data" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
<tr>
	 <td colspan="2" nowrap="nowrap" class="label">INTEREST <%=fsmsg1%> (E)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(remainempint2)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(remainaaiInt2)%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">FINAL NET CLOSING (D+E)</td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format((netcloseEmpNet+remaininInt)-finalStlmentEmpNet1.longValue()+remainempint2)%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format((netcloseNetAmount+remainAaiInt)-finalStlmentAAICon1.longValue()+remainaaiInt2)%></td>
	 <td colspan="5" class="Data" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <%}
 
 if(!personalInfo.getAdjDate().equals("---")){ %>
   <tr>
	 <td colspan="2" nowrap="nowrap" class="label"><%=personalInfo.getAdjRemarks()%> </td>
 	 <td colspan="6" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=personalInfo.getAdjInt()%></td>
	 <td colspan="5" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr>
 <%}%>
 	<% 
 	double aprEmpNet=0l,aprAaiNet=0l,aprPrincipal=0l,aprpc=0l;
 	if(dispYear.equals("2012-13")){
 pensionCardInfo1=(EmployeePensionCardInfo)dateCardList1.get(0); 
 		aprEmpNet=Double.parseDouble(pensionCardInfo1.getEmpNet());
 		aprAaiNet=Double.parseDouble(pensionCardInfo1.getAaiNet());
 		aprPrincipal=Double.parseDouble(pensionCardInfo1.getPrincipal());
 		aprpc=Double.parseDouble(pensionCardInfo1.getPensionContribution());	
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
  			}														%>
 <tr>
	 
	 <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getShnMnthYear()%>(MarSal)</td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmoluments()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmppfstatury()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpvpf()))%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPrincipal()))%></td>
	 <td width="7%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getInterest()))%></td>
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmptotal()))%></td>
	 <td width="2%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAdvancePFWPaid()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpNet()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiPF()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPfDrawn()))%></td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiNet()))%></td>
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPensionContribution()))%></td>
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getStation()%></td>
	 
	   <%if (!aprremarks.equals("---")){ %>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=aprremarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=aprremarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="8%" class="Data"><%=aprremarks%></td>
	 <%}%>
	 </tr>
	 
	 
	<tr>
	 <td colspan="2" nowrap="nowrap" class="label">NEW CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+aprEmpNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)) + aprAaiNet)%></td>
	 <td width="6%" class="NumData"><%=df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr> 
 <% } %>
   <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	<%if(multipleFinalSettlementFlag.equals("true")){ %>
 	<td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(((netcloseEmpNet+remaininInt)-finalStlmentEmpNet1.longValue()+remainempint2)+((netcloseNetAmount+remainAaiInt)-finalStlmentAAICon1.longValue()+remainaaiInt2))%></td>
 	<%}else{%>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(personalInfo.getAdjInt())+Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)))%></td>
	 <%}%>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <% }else{ %>
 
 <% double aprEmpNet=0l,aprAaiNet=0l,aprPrincipal=0l,aprpc=0l;
 			
 if(dispYear.equals("2012-13")){
 pensionCardInfo1=(EmployeePensionCardInfo)dateCardList1.get(0);
 aprEmpNet=Double.parseDouble(pensionCardInfo1.getEmpNet());
 		aprAaiNet=Double.parseDouble(pensionCardInfo1.getAaiNet());
 		aprPrincipal=Double.parseDouble(pensionCardInfo1.getPrincipal());
 		aprpc=Double.parseDouble(pensionCardInfo1.getPensionContribution()); 
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
	 
	 <td width="4%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getShnMnthYear()%>(MarSal)</td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmoluments()))%></td>
	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmppfstatury()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpvpf()))%></td>
	 <td width="5%" nowrap="nowrap" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPrincipal()))%></td>
	 <td width="7%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getInterest()))%></td>
	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmptotal()))%></td>
	  <td width="2%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAdvancePFWPaid()))%></td>
	 <td width="5%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getEmpNet()))%></td>
	
 	 <td width="8%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiPF()))%></td>
	 <td width="3%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPfDrawn()))%></td>
	 <td width="9%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getAaiNet()))%></td>

	 <td width="6%" class="NumData"><%=Math.round(Double.parseDouble(pensionCardInfo1.getPensionContribution()))%></td>
	  <td width="8%" nowrap="nowrap" class="Data"><%=pensionCardInfo1.getStation()%></td>
	
	   <%if (!aprremarks.equals("---")){ %>
	   <td width="8%" class="Data"  onmouseover="showTip('<%=aprremarks%>', this);high(this);style.cursor='hand'"; onmouseout=hideTip() class=back";><%=aprremarks.substring(0,3)+"..." %></td>
	   <%}else{%>
	 	<td width="8%" class="Data"><%=aprremarks%></td>
	 <%}%>
	 </tr>
	 
	 <tr>
	 <td colspan="2" nowrap="nowrap" class="label">NEW CLOSING BALANCE</td>
 	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal)%></td>
 	 <td colspan="3" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+aprEmpNet) %></td>
	 <td colspan="2" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 <td width="3%" class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalAaiNetOB)) + aprAaiNet)%></td>
	 <td width="6%" class="NumData"><%=df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc) %></td>
	 <td colspan="4" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
 </tr> 
 <% } %>
    <tr>
 
	 <td colspan="8" nowrap="nowrap" class="label">Grand Total (Subscription+Contribution)</td>

 	
	 <td width="3%"  class="NumData" nowrap="nowrap"><%=df1.format(Double.parseDouble(df1.format(finalEmpNetOB))+Double.parseDouble(df1.format(finalAaiNetOB))+aprEmpNet+aprAaiNet)%></td>
	 <td colspan="7" class="Data">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	 
 </tr>
 <%}%>
 
 <tr>
    <td colspan="15">&nbsp;</td>
 </tr>
  <%totalEmpNet=0;totalEmoluments=0;totalAAIPF=0;pfStaturary=0;totalPFDrwn=0;empTotal=0;totalAAINet=0;totalPensionContr=0;}%>
  

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
	          if(personalInfo.getPfwdisableFlag().equals("Y")){%>
	          <tr>
            <td class="label"></td>
            <td class="label"></td>
            <td></td>
            <td></td>
           
          </tr>
	          <% 
	          }else{
          %>
          <tr>
            <td class="label"><%=count%></td>
            <td class="label"><%=ptwInfo.getPtwPurpose()%></td>
            <td><%=ptwInfo.getPtwDate()%></td>
            <td><%=ptwInfo.getPtwAmount()%></td>
           
          </tr>
         <%}}%>
        </table></td>
        <td width="47%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
           <tr>
             <td class="label">NOTE:-</td>
             </tr>
             <%if(dispYear.equals("2012-13")){%>
           <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">1. CREDITS INCLUDES MARCH SALARY PAID IN APRIL TO FEBRUARY SALARY PAID IN MARCH.ADVANCES/PFW SHOWN IN THE MONTH IT IS PAID.ALSO MARCH 13 SALARY CREDITS HAVE ALSO BEEN SHOWN . NO INTEREST HAS BEEN CALCULATED ON MARCH 13 SALARY CREDITS.</td>
            </tr>
            <%}else if(dispYear.equals("2013-14")||dispYear.equals("2014-15")){ %>
             <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">1. CREDITS INCLUDES APRIL TO MARCH SALARY . ADVANCES/PFW SHOWN IN THE MONTH IT IS PAID.</td>
            </tr>
            <%}else{%>
            <tr>
            <td class="label" style="text-align:justify;word-spacing: 5px;">1. CREDITS INCLUDES MARCH SALARY PAID IN APRIL TO FEBRUARY SALARY PAID IN MARCH.ADVANCES/PFW SHOWN IN THE MONTH IT IS PAID.</td>
            </tr>
            <%}%>
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
  <%if (!arrearDate.equals("NA")){ %>
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
