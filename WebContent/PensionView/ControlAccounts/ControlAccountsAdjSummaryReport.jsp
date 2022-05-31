 
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
 <script type="text/javascript"> 
	function popupWindow(mylink, windowname)
		{
		//alert(mylink);
		 
		if (! window.focus)return true;
		var href;
		if (typeof(mylink) == 'string')
		   href=mylink;
		   
		else
		href=mylink.href;
	    
		progress=window.open(href, windowname, 'width=700,height=500,statusbar=yes,scrollbars=yes,resizable=yes');
		
		return true;
		}
	</script>
</head>

  
  <body>
 	<% ControlAccountForm2Info forminfo= new ControlAccountForm2Info();
 		String reportType="",fileName="",filePrefix="",filePostfix="";
 	  
  		DecimalFormat df1 = new DecimalFormat("#########0");
		String status="";
 	
 	%>
 	     <%if(request.getAttribute("adjSummaryInfo")!=null){
  			if (request.getAttribute("reportType") != null) {
				reportType = (String) request.getAttribute("reportType");
				if (reportType.equals("Excel Sheet")
						|| reportType.equals("ExcelSheet")) {
				
					filePrefix = "";
					fileName=filePrefix+filePostfix+".xls";
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + fileName);
				}
			}
  	forminfo=(ControlAccountForm2Info)request.getAttribute("adjSummaryInfo");
  	String empstatus=(String)request.getAttribute("empstatus");
  	String finYear=(String)request.getAttribute("finYear");
  	System.out.println("jsp page:"+empstatus);
  	%>
 	
 	<table width="100%" border="0" cellspacing="0" cellpadding="0">
 	
  <tr> <td align=center>&nbsp;</td>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td align=center width=30%>&nbsp;</td>
        <td width="7%" rowspan="2"><img src="<%=basePath%>PensionView/images/logoani.gif" width="100" height="50" /></td>
        <td align=left>AIRPORTS AUTHORITY OF INDIA</td>
        
       <% if(empstatus.equals("A")){
       status="Active ";
       }else{
       status="Deactive ";
       }
       %>
       <tr>
          <td align=center width=30%>&nbsp;</td>
       <td align=left> Form-2 Justification : <%=status%> Cases 
       </td>
       <td> 
       </td>
       </tr>
       
       <tr>
          <td align=center width=30%>&nbsp;</td>
       <td align=center> 
       </td>
       <td align=center>
       </td>
       </tr>
        
     
      </table>
      </td>
      </tr>
      <tr>
       <tr><td>&nbsp;</td>
       <td >Block1: Justification For Adjustment OB</td>
       <td> &nbsp;</td>
       </tr>
      
    
 
  	<tr>
  	<td align=center>&nbsp;</td>
  	
     <td align=center ><table width="65%" border="1" cellspacing="0" cellpadding="0">
    
     
       <tr>
      
      <td width ="80%" align="center" class="label" colspan=2>
        Adj.AAIContri <%=finYear%>
      </td>
      
      </tr>
      <tr>
      
      <td width =80% align=center class="label">
        Description

      </td>
      <td width=60% align=center class="label">
      AdJ.Amount
      
      </td>
      </tr> 
      <% 
      	double msub=0,maai=0,mpcontri=0,msubne=0,maaine=0,mpcontrine=0;
     	double nonsub=0,nonaai=0,noncontri=0,nonsubne=0,nonaaine=0,noncontrine=0;
      	ArrayList manuallist=new ArrayList();
      	ArrayList nonmanuallist=new ArrayList();
     	
     	
      manuallist=(ArrayList)forminfo.getManualList();
     nonmanuallist=(ArrayList)forminfo.getNonManualList();
      
     session.setAttribute("manualList",manuallist) ;
     session.setAttribute("systemList",nonmanuallist) ;
     
   	AaiEpfForm11Bean GrdTotinfo =new AaiEpfForm11Bean();
 

	   //GrdTotinfo=(AaiEpfForm11Bean)manuallist.get(0);
	 
	  
	    
		
	
		//System.out.println("msub"+forminfo.getManaulAaiContriGrandTotal());
		msub=Double.parseDouble(forminfo.getManaulempSubGrandTotal());
		maai=Double.parseDouble(forminfo.getManaulAaiContriGrandTotal());
		mpcontri=Double.parseDouble(forminfo.getManualPContriAdjTotal());
		
		msubne=Double.parseDouble(forminfo.getManaulempSubGrandTotalNe());
		maaine=Double.parseDouble(forminfo.getManaulAaiContriGrandTotalNe());
		mpcontrine=Double.parseDouble(forminfo.getManualPContriAdjTotalNe());


	  // GrdTotinfo=(AaiEpfForm11Bean)nonmanuallist.get(0);
		
		nonsub=Double.parseDouble(forminfo.getNonManaulempSubTotal());
		nonaai=Double.parseDouble(forminfo.getNonManaulAaiGrandTotal());
		noncontri=Double.parseDouble(forminfo.getNonManualPContriAdjTotal());
		
		nonsubne=Double.parseDouble(forminfo.getNonManaulempSubTotalNe());
		nonaaine=Double.parseDouble(forminfo.getNonManaulAaiGrandTotalNe());
		noncontrine=Double.parseDouble(forminfo.getNonManualPContriAdjTotalNe());

       %>
       
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S','CrtlAccAdjSummary');"> <%=df1.format(nonaai+nonaaine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new"  href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M','CrtlAccAdjSummary');"><%=df1.format(maai+maaine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(maai+nonaai+maaine+nonaaine)%></td>
       </tr>

       <tr>
       <td colspan=2>&nbsp;</td>
       </tr>
       <tr>
  
      <td width ="80%" align="center" class="label" colspan=2>
        Adj.Emp Subscription <%=finYear%>	

      </td>
      
      </tr>
      <tr>
      
      <td width =80% align=center class="label">
        Description

      </td>
      <td width=60% align=center class="label">
      AdJ.Amount
      
      </td>
      </tr> 
      
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S','CrtlAccAdjSummary');"> <%=df1.format(nonsub+nonsubne)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new"  href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M','CrtlAccAdjSummary');"><%=df1.format(msub+msubne)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(msub+nonsub+msubne+nonsubne)%></td>
       </tr>
       
       
       <tr>
       </tr>
       <tr>
       <td colspan=2>&nbsp;</td>
       </tr>
       
     
      <tr>
      <td width ="80%" align="center" class="label" colspan=2>
        Adj.Pension Contri <%=finYear%>	
      </td>
      
      </tr>
      <tr>
      
      <td width =80% align=center class="label">
        Description

      </td>
      <td width=60% align=center class="label">
      AdJ.Amount
      
      </td>
      </tr> 
      <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S','CrtlAccAdjSummarySystem');"> <%=df1.format(noncontri+noncontrine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new"  href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M','CrtlAccAdjSummaryManual');"><%=df1.format(mpcontri+mpcontrine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(mpcontri+noncontri+mpcontrine+noncontrine)%></td>
       </tr>
       
       </table> 
       
       
       <tr>
       <td>&nbsp;</td>
       <td>&nbsp;</td>
       <td>&nbsp;</td>
       </tr>
       <tr><td>&nbsp;</td>
       <td > Block2: Adjustmen OB Seperation  With -/+ ve Signs </td>
       <td>&nbsp;</td>
       </tr>
       
       
       <tr>
  	<td align=center>&nbsp;</td>
     <td align=center ><table width="90%" border="1" cellspacing="0" cellpadding="0">
    <tr>
    <td><table width="100%" border="1" cellspacing="0" cellpadding="0">
       		<tr>
      
		      <td width ="80%" align="center" class="label" colspan=2>
		        Adj.AAIContri -/+ Ve [<%=status %>]
		      </td>
      
      		</tr>
      		<tr>
      		<td width =80% align=center class="label">
	        Description
	        </td>
      		<td width=60% align=center class="label">
      		AdJ.Amount
 			</td>
      </tr> 
      
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(+ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj AAIContri&frm_AmtFlag=P','AdjAAIContriSysPostive');"><%=df1.format(nonaai)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(-ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj AAIContri&frm_AmtFlag=N','AdjAAIContriSysNegative');"><%=df1.format(nonaaine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(nonaai+nonaaine)%></td>
       </tr>
       <tr>
       <td colspan=2>&nbsp;
       </td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(+ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj AAIContri&frm_AmtFlag=P','AdjAAIContriManualPostive');"><%=df1.format(maai)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(-ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj AAIContri&frm_AmtFlag=N','AdjAAIContriManualNegative');"><%=df1.format(maaine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(maaine+maai)%></td>
       </tr>
       </table>
      </td>
       
       <td>
       <table width="100%" border="1" cellspacing="0" cellpadding="0">
       
     
       <tr>
       
      
      <td width ="80%" align="center" class="label" colspan=2>
        Adj.Emp Subscription -/+ Ve [<%=status%>]	

      </td>
      
      </tr>
      <tr>
      
      <td width =80% align=center class="label">
        Description

      </td>
      <td width=60% align=center class="label">
      AdJ.Amount
      
      </td>
      </tr> 
      
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(+ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj EmpSub&frm_AmtFlag=P','AdjEmpSubSysPostive');"><%=df1.format(nonsub)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(-ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj EmpSub&frm_AmtFlag=N','AdjEmpSubSysNegative');"><%=df1.format(nonsubne)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(nonsub+nonsubne)%></td>
       </tr>
         <tr>
       <td colspan=2>&nbsp;
       </td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(+ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj EmpSub&frm_AmtFlag=P','AdjEmpSubManualPostive');"><%=df1.format(msub)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(-ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj EmpSub&frm_AmtFlag=N','AdjEmpSubManualNegative');"><%=df1.format(msubne)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(msubne+msub)%></td>
       </tr>
       </table>
       
       </td>
      
       
      
       
     <td>
       
     <table width="100%" border="1" cellspacing="0" cellpadding="0">

       <tr>
      
      <td width ="80%" align="center" class="label" colspan=2>
        Adj.Pension Contri -/+ Ve [<%=status%>]	
      </td>
      
      </tr>
      <tr>
      
      <td width =80% align=center class="label">
        Description

      </td>
      <td width=60% align=center class="label">
      AdJ.Amount
      
      </td>
      </tr> 
      <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(+ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj PenContri&frm_AmtFlag=P','AdjPenContriSysPositive');"><%=df1.format(noncontri)%></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and System Corrections(-ve)
       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=S&frm_attri=Adj PenContri&frm_AmtFlag=N','AdjPenContriSysNegative');"><%=df1.format(noncontrine)%></td>
       </tr>
        <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(noncontri+noncontrine)%></td>
       </tr>
         <tr>
       <td colspan=2>&nbsp;
       </td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(+ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj PenContri&frm_AmtFlag=P','AdjPenContriManualPositive');"><%=df1.format(mpcontri)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data"><%=status%> and Manual Corrections(-ve)

       </td>
        <td width="30%" align="center" class="NumData"><a  title="Click the link to view Transaction log" target="new" href="#" onclick ="popupWindow('<%=basePath%>aaiepfreportservlet?method=form2AdjReport&frm_finyear=<%=finYear%>&empstatus=<%=empstatus%>&frm_adjType=M&frm_attri=Adj PenContri&frm_AmtFlag=N','AdjPenContriManualNegative');"><%=df1.format(mpcontrine)%></a></td>
       </tr>
       <tr>
       <td width="25%" align="center" class="Data">Total

       </td>
        <td width="30%" align="center" class="NumData"><%=df1.format(mpcontrine+mpcontri)%></td>
       </tr>
       </table>
       </td>
       </tr>
       </table> 
       
       </tr>
       <%}%>
      </table>
      
  </body>
</html>
 