
<%@ page language="java" import="java.util.*,aims.common.*,java.text.DecimalFormat"%>
<%@ page isErrorPage="true"%>
<%@ page import="aims.bean.EmolumentslogBean"%>
<%@ page import="aims.bean.EmployeeValidateInfo"%>
<%@ page import="aims.bean.JustificationAdjOB"%>

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
	function testSS(){
  	  	
   		var pfId = document.forms[0].pfId.value;
   		statusdisplay();      		
   		var url ="<%=basePath%>search1?method=getEmolumentslog&pfId="+pfId;  
   		    
	    document.forms[0].action=url;
	    document.forms[0].method="post";
		document.forms[0].submit();			
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
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="3" class="reportlabel" style="text-decoration: underline" align="center">
Adj Log</td>
  </tr>
</table>
</td>
</tr>




<%EmolumentslogBean logbean = new EmolumentslogBean();

            if (request.getAttribute("adjLoginfo") != null) {
                ArrayList adjlists = new ArrayList();
                int totalData = 0;
                adjlists = (ArrayList) request.getAttribute("adjLoginfo");
                System.out.println("dataList " + adjlists.size());

                if (adjlists.size() == 0) {

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

			<%} else if (adjlists.size() != 0) {%>
			<tr>
				<td height="25%">
					<table align="center" width="100%" cellpadding="1"  cellspacing="1" border="1">
						<tr>
						<td colspan=3>&nbsp;</td>
						<td colspan=3 class="label" nowrap align="center">EMPLOYEES SUBSCRIPTION</td>
                        <td colspan=3 class="label" nowrap align="center">AAI CONTRIBUTION</td>
						<td colspan=3>&nbsp;</td>
						</tr>
						<tr ><td class="label">
                          SR NO
                        </td>
                            <td class="label">
								PENSIONNO&nbsp;&nbsp;
							</td>						
							<td class="label">
								ADJOBYEAR&nbsp;&nbsp;
							</td>
                            <td class="label">EMPCONTRI
							</td>
                            <td class="label">EmpInterest
							</td>
                            <td class="label">EmpTotal
							</td>
							<td class="label">
								AAICONTRI
							</td>
                            <td class="label">
								AaiInterest
							</td>
                            <td class="label">AAITotal
							</td>
                             <td class="label">
								PensionContri
							</td>
							  <td class="label">
								REMARKS
							</td>
				<td class="label">
								Adjustment Justification
							</td>
					</tr>

						<%}%>
						<%int count = 0;
               
                for (int i = 0; i < adjlists.size(); i++) {
                    count++;
                    EmolumentslogBean adjlog = (EmolumentslogBean) adjlists.get(i);

                   String PENSIONNO = adjlog.getPfId();
                   String ADJOBYEAR = adjlog.getAdjObYear();
                   String EMPCONTRI = adjlog.getEmpContri();
                   String Empinterest = adjlog.getEmpinterest();
                   String Emptotal = adjlog.getEmptotal();
                   String AAICONTRI = adjlog.getAaiContri();
                   String Aaiinterest = adjlog.getAaiinterest();
                   String Aaitotal = adjlog.getAaitotal();
                   String EMPCONTRIDEVIATION = adjlog.getEmpContriDeviation();
                   String AAICONTIDEVIATION = adjlog.getAaiContriDeviation();
                   String REMARKS = adjlog.getRemarks();
                   String adjJustUrl=adjlog.getAdjUrl();
                 
                   if (count % 2 == 0) {

                       %>
   						<tr>
   							<%} else {%>
   						<tr>
   							<%}%>
							
							<td class="Data" width="6%">
								<%=count%>
							</td>
							<td class="Data" width="10%">
								<%=PENSIONNO%>
							</td>
							<td class="Data" width="12%">
								<%=ADJOBYEAR%>
							</td>
							<td class="Data" width="12%">
								<%=EMPCONTRI%>
							</td>
                            <td class="Data" width="12%">
								<%=Empinterest%>
							</td>
                             <td class="Data" width="12%">
								<%=Emptotal%>
							</td>
							<td class="Data" width="12%">
                              <%=AAICONTRI%>
                            </td>
                          <td class="Data" width="12%">
								<%=Aaiinterest%>
							</td>
                            <td class="Data" width="12%">
								<%=Aaitotal%>
							</td>
							<td class="Data" width="12%">
								<%=EMPCONTRIDEVIATION%>
							</td>
							
                           <td class="Data" width="12%">
								<%=REMARKS%>
							</td>      

<td class="Data" width="12%">
     <%if(adjJustUrl!=""){ %>
							<a href="<%=adjJustUrl%>" >Click the link to view Adj Justification </a>
    <%} %>
							</td>                      						
</tr>
						<%}%>


						<%}%>

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
      
    <td colspan="6" class="reportlabel" style="text-decoration: underline" align="center">Justification For AdjOB </td>
  </tr>
</table>
</td>
</tr>
<% JustificationAdjOB freeadj = new JustificationAdjOB();

            if (request.getAttribute("freezAdjOB") != null) {
            DecimalFormat df = new DecimalFormat("#########0");
                ArrayList adjlists = new ArrayList();
                int totalData = 0;
                adjlists = (ArrayList) request.getAttribute("freezAdjOB");
               

                if (adjlists.size() == 0) {

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

			<%} else if (adjlists.size() != 0) {	%>
			<tr>
				<td height="25%">
					<table align="center" width="100%" cellpadding="1"  cellspacing="1" border="1">
						<tr>
						<td  class="label" rowspan=2>
								ADJOBYEAR&nbsp;&nbsp;
							</td>
						<td  colspan=6  class="label" align=center>
								CALCULATION OF  ADJUSTMENT IN OPENING BALANCE &nbsp;&nbsp;
							</td>
							
							<td class="label" rowspan=2>
								CURRENT ADJOB
							</td>
							<td class="label" rowspan=2>
								PENSION CONTR
							</td>
							<td class="label" rowspan=2>
								REMARKS
							</td>
						</tr>
						
						<tr>
							 <td class="label" >
                           OUTSTANDADV
							</td>
                            <td class="label" >
                           EMPSUBTOTAL
							</td>
                            <td class="label" >
                            PENSIONTOTAL
							</td>
                            <td class="label" >
								DIFFERENCE
							</td> 
                           
				      <td class="label" >
								RATEOFINTEREST
							</td>
							<td class="label" >
								ADJOB
							</td>
							
					</tr>

						<%}%>
						<%int count = 0;
		                String OUTSTANDADV="",EMPSUBTOTAL="",PENSIONTOTAL="",pensionContri="",pfPenTot="";
		                for (int i = 0; i < adjlists.size(); i++) {
		                    count++;
	                   freeadj = (JustificationAdjOB) adjlists.get(i);
	                   String ADJOBYEAR = freeadj.getYear();
	                   if(freeadj.getAdjoutstandadv()!=null){
	                     OUTSTANDADV = freeadj.getAdjoutstandadv();
	                   }else{
	                   OUTSTANDADV= "0";
	                   }
	                   if(freeadj.getAdjempsubtotal()!=null){
	                     EMPSUBTOTAL = freeadj.getAdjempsubtotal();
	                   }else{
	                   EMPSUBTOTAL= "0";
	                   }
	                   if(freeadj.getAdjpensioncontri()!=null){
	                     pensionContri = freeadj.getAdjpensioncontri();
	                   }else{
	                   pensionContri= "0";
	                   }
	                    if(freeadj.getPfPensionTot()!=null){
	                     pfPenTot = freeadj.getPfPensionTot();
	                   }else{
	                   pfPenTot= "0";
	                   }
	                   PENSIONTOTAL = freeadj.getTotal();
	                   String LASTACTIVE = freeadj.getLastActive();
	                   String DtFreez=freeadj.getDtfreez();
	                   String lastdate=freeadj.getRemarks();
	                   
	                    
	                 	double diff=freeadj.getDiff();
	                 	double adjTot=freeadj.getAdjTot();
	                 	double rateOfInt=freeadj.getRateOfInt();
	                 	
					      if (count % 2 == 0) {
						 %>
   						<tr>
   							<%} else {%>
   						<tr>
   							<%}%>
   							<%if(DtFreez.equals("PC")){
							ADJOBYEAR=ADJOBYEAR+"(Current year PC on " +lastdate+")";
							
							}
							else{
							ADJOBYEAR=ADJOBYEAR+"(Frozed on "+ lastdate+")";
							}
							%>
							<td class="Data" width="12%">
								<%=ADJOBYEAR%>
							</td>
							<td class="Data" width="12%">
								<%=OUTSTANDADV%>
							</td>
							<td class="Data" width="12%">
								<%=EMPSUBTOTAL%>
							</td>
							<td class="Data" width="12%">
								<%=PENSIONTOTAL%>
							</td>
							<td class="Data" width="12%">
								<%=df.format(diff)%>
							</td>
							<td class="Data" width="12%">
								<%=df.format(rateOfInt)%>
							</td>
							<td class="Data" width="12%">
								<%=df.format(adjTot)%>
							</td>
							<td class="Data" width="12%">
								<%=pfPenTot%>
							</td>							
							<td class="Data" width="12%">
								<%=pensionContri%>
							</td>
							<td class="Data" width="12%">
								<%=LASTACTIVE%>
							</td>
							
							</tr>
							
						<%
						}
						}
						%>


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
      
    
  </tr>
</table>
</td>
</tr>

			 
					</table>
				</td>

			</tr>
</table>
  
				</td>

			</tr>
			</table>

			
				</form>
	</body>
</html>
