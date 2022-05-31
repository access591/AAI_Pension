<%@ page language="java" import="java.util.*,aims.common.CommonUtil" pageEncoding="UTF-8"%>
<%@ page import="aims.bean.epfforms.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.ArrayList" %>
<jsp:useBean id="nc" class="aims.common.CommonUtil" />
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.getAttribute("windowname");
System.out.println("windowname"+request.getAttribute("windowname"));
ArrayList remitancetableList = new ArrayList();
ArrayList remitanceList = new ArrayList();
RemittanceBean rbean = new RemittanceBean();
AaiEpfform3Bean epfForm3Bean = new AaiEpfform3Bean();
DecimalFormat df = new DecimalFormat("#########0");
session.getAttribute("remitancetableList");
System.out.println("remitancetableList"+session.getAttribute("remitancetableList"));
session.getAttribute("remitanceList");
session.getAttribute("salaryMonth");
System.out.println("salaryMonth"+session.getAttribute("salaryMonth"));
 String salaryMonth = (String) request.getAttribute("salaryMonth");
if (session.getAttribute("remitancetableList") != null) {
	remitancetableList = (ArrayList) session
			.getAttribute("remitancetableList");
	System.out.println("remitancetableList"+remitancetableList.size());
	if (remitancetableList.size() > 0) {
		for (int i = 0; i < remitancetableList.size(); i++) {
			rbean = (RemittanceBean) remitancetableList.get(i);

		}
	}
}
if (session.getAttribute("remitanceList") != null) {
	remitanceList = (ArrayList) session
			.getAttribute("remitanceList");
	System.out.println("remitancetableList"+remitancetableList.size());
	if (remitanceList.size() > 0) {
		for (int cardList = 0; cardList < remitanceList.size(); cardList++) {
			epfForm3Bean = (AaiEpfform3Bean) remitanceList.get(cardList);
		}
	}
}
%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>AAI</title>
 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
</head>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
   <td>
   <table width="100%" height="490" cellspacing="0" cellpadding="0" border="0">
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
     <td class="reportlabel" nowrap="nowrap" align="center">AIRPORTS AUTHORITY OF INDIA</td>
   </tr>
   <tr> 
     <td class="reportlabel" nowrap="nowrap" align="center">REGION:<%=epfForm3Bean.getRegion()%></td>
   </tr>
    <tr> 
     <td class="reportlabel" nowrap="nowrap" align="center">BANK PAYMENT VOUCHER</td>
   </tr>
  <tr>
    <td colspan="4"><table width="80%" border="0" align="center" cellspacing="0" cellpadding="0">
      <tr>
        <td  class="label" >VOUCHER No:&nbsp; </td>
        <td  class="Data"  align="left">&nbsp;<%=rbean.getVrNo()%></td>
       	<td  class="label"  align="right">VOUCHER DATE:&nbsp; </td>
        <td  class="Data"   align="left">&nbsp;<%=rbean.getVrDt()%></td>
      </tr>
      <tr>
        <td nowrap="nowrap" class="label">BANK NAME & CODE:</td>
        <td class="Data"></td>
      </tr>
      <tr>
        <td class="label" >DRAWN IN F/O:</td>
        <td class="Data"></td>
      </tr>
	  <tr>
    <td colspan="4"><table  border="0"  align="left" cellspacing="0" cellpadding="0">
	  <tr>
     <td class="label">&nbsp;&nbsp;&nbsp; 1.	AAIEPF TRUST:&nbsp;<%=rbean.getPfRemitterBankNM()%>&nbsp;/&nbsp;<%=rbean.getPfRemitterBankACNo()%></td>
	  </tr>
	  <tr>
	  <td class="label">&nbsp;&nbsp;&nbsp; 2.	AAIEPF TRUST:&nbsp;<%=rbean.getPcRemitterBankNM()%>&nbsp;/&nbsp;<%=rbean.getPfRemitterBankACNo()%></td>
	  </tr>
	  </table>
	  </td>
	  </tr>
	   <tr>
        
        <td class="label">BILL REF. NO.</td>
        <td class="Data"><%=rbean.getBillRefno()%></td>
         <td class="label" align="right">DATE:</td>
        <td class="Data"><%=rbean.getBilldate()%></td>
      </tr>
      <tr>
        <td class="label">BRIEF PARTICULARS</td>
        <td class="Data"></td>        
      </tr>
	  <tr>
    <td colspan="4"><table  border="0"  align="left" cellspacing="0" cellpadding="0">
	  <tr>
     <td class="label">PF ACCRETION/ PENSION CONTRIBUTION/ INSPECTION CHARGES FOR THE M/O ----<%=session.getAttribute("salaryMonth")%></td>
	  </tr>
	  </table>
	  </td>
	  </tr>
 <tr>
        <td nowrap="nowrap" class="label">STATION NAME:-</td>
        <td class="Data"><%=epfForm3Bean.getStation()%></td>
        </tr>
    </table></td>
  </tr>


  <tr>
    <td colspan="5"><table width="80%" align="center"  border="1" cellspacing="0" cellpadding="0">
    
      <tr>
        <td  class="label">PARTICULARS</td>
        <td  class="label">UNIT CODE</td>
        <td  class="label">ACCOUNT CODE</td>
        <td  class="label">AMT. DEBIT(RS.) </td>
        <td  class="label">AMT. CREDIT(RS.)</td>
     </tr>
    <tr>
        <td  class="label">EMPLOYEES PF</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">315:00</td>
        <td  class="label"><%=epfForm3Bean.getEmppfstatury()%> </td>
        <td  class="label">0.00</td>
     </tr>
	 <tr>
        <td  class="label">VPF</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">315.01</td>
        <td  class="label"><%=df.format(Double.parseDouble(epfForm3Bean.getEmpvpf()))%></td>
        <td  class="label">0.00</td>
     </tr>
	 <tr>
        <td  class="label">REFUND OF ADV(PRINCIPAL)</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">318:00</td>
        <td  class="label"><%=df.format(Double.parseDouble(epfForm3Bean.getPrincipal()))%></td>
        <td  class="label">0.00</td>
     </tr>
	 <tr>
        <td  class="label">REFUND OF INTT ON ADV</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">318:01</td>
        <td  class="label"><%=df.format(Double.parseDouble(epfForm3Bean.getInterest()))%></td>
        <td  class="label">0.00</td>
     </tr>
	 <tr>
        <td  class="label">AAI PF(NET OF PENSION)</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">316:00</td>
        <td  class="label"><%=df.format(Double.parseDouble(epfForm3Bean.getPf()))%></td>
        <td  class="label">0.00</td>
     </tr>
  	 <tr>
        <td  class="label">PENSION CONTRIBUTION</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">316:00</td>
        <td  class="label"><%=df.format(Double.parseDouble(epfForm3Bean.getPensionContribution()))%></td>
        <td  class="label">0.00</td>
     </tr>
	 <tr>
        <td  class="label">INSPECTION CHARGES</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">742.01</td>
        <td  class="label"><%=Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury()) * 100 / 12 * 0.0018)%></td>
        <td  class="label">0.00</td>
     </tr>
<%
double netPayment=0;
double accration=Math.round(Double.parseDouble(epfForm3Bean.getEmppfstatury()) * 100 / 12 * 0.0018);
netPayment=Double.parseDouble(epfForm3Bean.getEmppfstatury())
		+Double.parseDouble(epfForm3Bean.getEmpvpf())
		+Double.parseDouble(epfForm3Bean.getPrincipal())
		+Double.parseDouble(epfForm3Bean.getInterest())
		+Double.parseDouble(epfForm3Bean.getPf())
		+Double.parseDouble(epfForm3Bean.getPensionContribution())
		+accration;
		
%>
	  <tr>
        <td  class="label">GRAND TOTAL</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">&nbsp;</td>
        <td  class="label"><%=df.format(netPayment)%></td>
        <td  class="label">0.00</td>
     </tr>

	 <tr>
        <td  class="label">NET PAYMENT</td>
        <td  class="label">&nbsp;</td>
        <td  class="label">&nbsp;</td>
        <td  class="label"><%=df.format(netPayment)%></td>
        <td  class="label">0.00</td>
     </tr>
	</table>
	</td>
	</tr>
	 <tr>
    <td colspan="5"><table width="80%" align="center"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
     <td class="label">AMOUNT IN WORDS:-<%=nc.findWords(df.format(netPayment)) %></td>
	  </tr>
	  </table>
	  </td>
	  </tr>
	  <tr>
    <td colspan="5"><table width="80%" align="center"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
     <td  class="label" nowrap="nowrap">CHEQUE NO. FROM:&nbsp;<%=rbean.getChequenofrom()%>&nbsp;To &nbsp;<%=rbean.getChequenoto()%></td>
     <td  class="label"></td>
	 <td  class="label" nowrap="nowrap">DATE:</td>
	 <td  class="label"><%=rbean.getChequedt()%></td>
	  </tr>
	  <tr>
     <td  class="label" nowrap="nowrap">PREPARED BY:</td>
     <td  class="label" nowrap="nowrap"><%=rbean.getPreparedby()%></td>
	 <td  class="label">CHECKED BY:</td>
     <td  class="label" ><%=rbean.getCheckedby()%></td>
	 </tr>
	 <tr>
	 <td  class="label" nowrap="nowrap">PASSED BY:</td>
     <td  class="label" nowrap="nowrap"><%=rbean.getPassedby()%></td>
	 <td  class="label">RECEIVED:</td>
     <td  class="label" nowrap="nowrap"><%=rbean.getReceivedby()%></td>
	  </tr>
	  </table>
	  </td>
	  </tr>
	</table>
	</td>
	</tr>		
  				
</table>

</body>
</html>
