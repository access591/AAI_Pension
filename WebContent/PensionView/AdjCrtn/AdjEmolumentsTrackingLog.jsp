
<%@ page language="java" import="java.util.*,aims.common.*,java.text.DecimalFormat"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.EmolumentslogBean"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.JustificationAdjOB"%>
<%@ page import="aims.common.CommonUtil" %>

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
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<script type="text/javascript"> 
  
  	
  	function statusdisplay(){
  	
  		  document.getElementById("suggestions").style.display="block";
		  document.getElementById("suggestions").innerHTML="";
		   var str="";
		   str+="<table>"; 
   		  
   		   str+="<tr><td>&nbsp;</td></tr><tr><td colspan='5' id='status'><img src='<%=basePath%>PensionView/images/loading1.gif'></td></tr><tr><td>&nbsp;</td></tr>";
		   str+="</table>";
		  document.getElementById("suggestions").innerHTML=str;
		 
  	
  	}
  	function load(filename,windname,chkflag){
  		var swidth=screen.Width-10;
		var sheight=screen.Height-150;
		var url="<%=basePath%>search1?method=viewExcel&url="+ filename+"&flag="+chkflag;
	
  	wind1 = window.open(url,windname,"toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
						winOpened = true;
						wind1.window.focus();
  	}
  	function decode(str) {
     var result = "";

     for (var i = 0; i < str.length; i++) {
          if (str.charAt(i) == "+") result += " ";
          else result += str.charAt(i);

          return unescape(result);
     }
}
  	
	function GetPCReport(){ 
	 		  var count=0;
		      var str=new Array();
		      var pensionno,cpfaccno,batchid,batchTime,updateDate,reportYear;
	if(document.forms[0].chk.length!=undefined){    
		  
		      for(var i=0;i<document.forms[0].chk.length;i++){
		      	  
			      if (document.forms[0].chk[i].checked){
			        count++;
			        str=document.forms[0].chk[i].value.split('*');
			      }
		      }
		      if(count==0){
		      	alert('User Should select One Group');		     
		       				return false;
		      }
		     }else{
		     	 	if(document.forms[0].chk.checked){
				 		str=document.forms[0].chk.value.split('*');
				 	}else{
				 		   alert('User Should select One Group');		     
		       				return false;
				 	}
		     }
		     
		       for(var j=0;j<str.length;j++){			      
			          pensionno=str[0];
			          cpfaccno=str[1];
			          batchid=str[2];
			          updateDate =str[3];
			          batchTime=str[4];	
			          reportYear=str[5];
			         		          
			        }
			
			   if(!reportYear==""){			     
			   		if(reportYear!="1995-2008"){
			   		   alert("Log Report is Available  only for 1995-2008");
			   		   return false;
			   		}
			   } else{
			   alert("Sorry We Can't Display the Report Due to Data Problem");
			   return false;
			   }
   		 
	    	var swidth=screen.Width-100;
			var sheight=screen.Height-250; 
			
			var url ="<%=basePath%>reportservlet?method=getPCReportAdjUserRequired&pensionno="+pensionno+"&cpfaccno="+cpfaccno+"&batchid="+batchid+"&batchtime="+encodeURIComponent(batchTime)+"&updatedate="+updateDate+"&reportYear=1995-2008"; 
	    
			wind1 = window.open(url,"AdjLogPcReport","toolbar=yes,statusbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
	   	 	winOpened = true;
			wind1.window.focus();
	    		
	}
	
		 
	 </script>
	</head>

	<body>
	<form>
		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
   <td>
   <table width="100%"  cellspacing="0" cellpadding="0">
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
   
</table>
</td>
</tr>
 		</table>
			 
			
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
 		<tr>
   <td>
   <table width="100%"  cellspacing="0" cellpadding="0">
<tr>
    <td>&nbsp;</td>
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
      
    <td colspan="6" class="reportlabel" style="text-decoration: underline" align="center">Edit Emoluments Log  </td>
  </tr>
</table>
</td>
</tr>

			<%EmolumentslogBean dbBeans = new EmolumentslogBean();
			String dateCompare ="",batchCompare ="",pensionNumber="",cpfAccNo = "",OLDEMOLUMENTS= "",NEWEMOLUMENTS="",OLDEMPPFSTATUARY="",NEWEMPPFSTATUARY="",MONTHYEAR="",UPDATEDDATE="",USERNAME="",reportYear="";
               String REGION="",batchId="",batchTime = "";
               
            if (request.getAttribute("adjEmolTrackingLog") != null) {
                ArrayList datalist = new ArrayList();
                int totalData = 0;
                datalist = (ArrayList) request.getAttribute("adjEmolTrackingLog");
                System.out.println("dataList " + datalist.size());

                if (datalist.size() == 0) {

                %>
			<tr>

				<td>
					<table align="center" id="norec">
						<tr>
							<br>
							<td>
								<b> No Records Found </b>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<%} else if (datalist.size() != 0) {%>
			<tr>
				<td height="25%">
					<table align="center" width="100%" cellpadding="1"  cellspacing="1" border="1">
						<tr ><td class="label">
                          SR NO
                        </td>
                           <td class="label">
								PENSION NO&nbsp;&nbsp;
							</td>
							<td class="label">
								MONTHYEAR&nbsp;&nbsp;
							</td>
						<td class="label">OLDEMOLUMENTS
							</td>
							<td class="label">
								NEWEMOLUMENTS
							</td>
							<td class="label">
								OLDEMPPFSTATUARY
							</td>
							<td class="label">
								NEWEMPPFSTATUARY
							</td> 
							<td class="label">
								REGION
							</td>	
							 		
</tr>

						<%}%>
						<%int count = 0;
                for (int i = 0; i < datalist.size(); i++) {
                   
                    EmolumentslogBean beans = (EmolumentslogBean) datalist.get(i);
					 
                     if(!beans.getOldEmoluments().equals("")){
                     OLDEMOLUMENTS = beans.getOldEmoluments();
                     }else{
                     OLDEMOLUMENTS= "&nbsp;";
                     }
                   // System.out.println("- beans.getOldEmoluments()-----"+ beans.getOldEmoluments());
                     NEWEMOLUMENTS = beans.getNewEmoluments();
                     pensionNumber = beans.getPensionNo();
                     cpfAccNo = beans.getCpfAcno();
                     if(!beans.getOldEmppfstatury().equals("")){
                     OLDEMPPFSTATUARY = beans.getOldEmppfstatury();
                     }else{
                     OLDEMPPFSTATUARY= "&nbsp;";
                     }
                     NEWEMPPFSTATUARY = beans.getNewEmppfstatury();                    
                     MONTHYEAR = beans.getMonthYear();
                     UPDATEDDATE = beans.getUpdatedDate();
                     REGION = beans.getRegion();
                     batchId = beans.getBatchId();
                     batchTime =  beans.getBatchTime();
                     USERNAME= beans.getUserName();
                     reportYear = beans.getAdjObYear();
                     
                //  System.out.println("----dateCompare------"+dateCompare+"====UPDATEDDATE="+UPDATEDDATE);
               //   System.out.println("----batchCompare------"+batchCompare+"====batchId="+batchId);
                   %>
                   <%if(!dateCompare.equals(UPDATEDDATE)){%>
                   <tr>
                   <td colspan="10" align="center"><%=UPDATEDDATE%></td>
                   </tr>
                   <%}%>
                   <%if(!batchCompare.equals(batchId)){
                    count=1;
                    %>
                   <tr>
                   <td colspan="10" align="left"  class="label">  UPDATED BY   <font size="2"><%=USERNAME%></font>     UPDATED TIME  <font size="2"><%=batchTime%></font> <input type="radio" name="chk"  value ="<%=pensionNumber%>*<%=cpfAccNo%>*<%=batchId%>*<%=UPDATEDDATE%>*<%=batchTime%>*<%=reportYear%>"/>
                   <img src="./PensionView/images/edit.gif" border="0"   alt="PCReport"  onclick="javascript:GetPCReport();"/>   
                   </td>
                    
                   </tr>
                   <% 
                   }else{
                   
                    count++;
                  } %>                                                                           
                  
   						<tr>  
                            <td class="Data" width="6%">
								<%=count%>
							</td>
							<td class="Data" width="10%">
                              <%=pensionNumber%>
                            </td>
							<td class="Data" width="12%">
								<%=MONTHYEAR%>
							</td>
							<td class="Data" width="12%">
								<%=OLDEMOLUMENTS%>
							</td>
							<td class="Data" width="12%">
								<%=NEWEMOLUMENTS%>
							</td>
							<td class="Data" width="12%">
								<%=OLDEMPPFSTATUARY%>
							</td>
							<td class="Data" width="12%">
								<%=NEWEMPPFSTATUARY%>
							</td> 
                            <td class="Data" width="12%">
								<%=REGION%>
							</td>	
							  					

						</tr>
						<%
						 dateCompare=UPDATEDDATE;
						 batchCompare = batchId;
						 
						}%>


						<%}%>

					</table>
				</td>

			</tr>
			
			 
			<tr><td>&nbsp;</td></tr>
			<!-- <tr> 
			<td>
				<table border="0" style="border-color: gray;" cellpadding="2"
						cellspacing="0" width="100%" align="center">		
					<tr>
						 					
					<td align="center">
					 <input type="button"   id="report" class="btn"
						value="PC Report"   style = "height: 23px;	width: 68px;	border: 1px none #333333;font-family: Arial, Helvetica, sans-serif;	font-size: 12px;	font-weight: bold;	color: #FFFFFF;"    onclick="GetPCReport();"/>
					</td>
			 </tr>
			</table>
			</td>           
			  
		</tr>-->           
</table>
  
				 
			
				</form>
	</body>
</html>
