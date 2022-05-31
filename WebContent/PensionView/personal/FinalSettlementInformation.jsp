
<%@ page language="java" import="java.util.*,aims.common.*,aims.bean.SettlementInformationLogBean"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.PensionBean"%>
<%
 // ##########################################
 // #Date					Developed by			Issue description
 // #07-Dec-2011        	Prasanthi			    Scereen added for settlement date edit 
 // #########################################
 %>
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
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="3" class="reportlabel" style="text-decoration: underline" align="center">Edit Settlement Log</td>
  </tr>
</table>
</td>
</tr>
			<%SettlementInformationLogBean dbBeans = new SettlementInformationLogBean();
            if (request.getAttribute("FinalSettlementLog") != null) {
                ArrayList datalist = new ArrayList();
                int totalData = 0;
                datalist = (ArrayList) request.getAttribute("FinalSettlementLog");
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
						<tr>									
						<td class="label">
								PENSIONNO
							</td>
							<td class="label">
								OLDFINALSETTLMENTDT
							</td>
							<td class="label">
								FINALSETTLMENTDT
							</td>
							<td class="label">
								OLDINTERESTCALCDATE
							</td>														
							<td class="label">
								INTERESTCALCDATE
							</td>
							<td class="label">
								OLDRESETTLEMENTDATE
							</td>
							<td class="label">
								RESETTLEMENTDATE
							</td>
							<td class="label">
								LASTACTIVE
							</td>					
							<td class="label">
								USERNAME
							</td>							
						</tr>

						<%}%>
						<%int count = 0;              
                for (int i = 0; i < datalist.size(); i++) {
                 System.out.println("inside for loop dataList " + datalist.size());
                    count++;
                    SettlementInformationLogBean beans = (SettlementInformationLogBean) datalist.get(i);  
                    System.out.println("OldFinalSettlementDate"+beans.getOldFinalSettlementDate()); 
                    System.out.println("OldInterestCalcDate"+beans.getOldInterestCalcDate());  
                    System.out.println("OldReSettlementDate"+beans.getOldReSettlementDate());                
     %>						
     <tr>	
							<td class="Data" >
								<%=beans.getPensionNo()%>
							</td>							
							<td class="Data">																																
								<%=beans.getOldFinalSettlementDate()%>
							</td>
							<td class="Data" >
								<%=beans.getFinalSettlementDate()%>
							</td>
							<td class="Data">
								<%=beans.getOldInterestCalcDate()%>
							</td >							
							<td class="Data" >
								<%=beans.getInterestCalcDate()%>
							</td>
							<td class="Data">																																
								<%=beans.getOldReSettlementDate()%>
							</td>
							<td class="Data" >
								<%=beans.getReSettlementDate()%>
							</td>
							<td class="Data" >
								<%=beans.getLastActive()%>
							</td>				
							<td class="Data" >
								<%=beans.getUserName()%>
							</td>
						</tr>
						<%}}%>		
					</table>
				</td>
			</tr>
			</table>
			</form>
	</body>
</html>
