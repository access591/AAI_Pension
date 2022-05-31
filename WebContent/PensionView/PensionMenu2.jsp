<%@ page language="java" import="aims.bean.UserBean"
	pageEncoding="UTF-8"%>
<%@page errorPage="error.jsp"%>



<%String userId = (String) session.getAttribute("userid");
            if (userId == null) {
                RequestDispatcher rd = request
                        .getRequestDispatcher("./PensionIndex.jsp");
                rd.forward(request, response);
            }
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>

   
<head>

<title>AAI</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">


 <LINK rel="stylesheet" href="<%=basePath%>PensionView/css/pmenubar.css" type="text/css">


</head>
<body>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
   <tr>
        <td height="3" align="left" valign="top" bgcolor="#000066"><img src="<%=basePath%>PensionView/images/spacer.gif" width="1" height="1"></td>
      </tr>
       <tr>
        <td align="left" valign="top"><table width="100%" height="32" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/navbg.gif"">
          <tr>
            <td align="left" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td width="92%" align="left" valign="middle">

				</td>
                  <td width="8%" align="right" valign="middle"><table width="60" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td align="left" valign="middle"><a href="<%=basePath%>PensionLogin?method=changepassword&flag=checkusername"><img src="<%=basePath%>PensionView/images/password.gif" title="Change Password" width="16" height="16" border="0" /></a></td>
                    
                      <td align="center" valign="middle"><a href="<%=basePath%>PensionLogin?method=home"><img src="<%=basePath%>PensionView/images/home.gif" title="Home" width="16" height="16" border="0" /></a></td>

                      <td align="right" valign="middle"><a href="<%=basePath%>PensionLogin?method=logoff" target="_parent"><img src="<%=basePath%>PensionView/images/logout.gif" title="Logout" width="14" height="16" border="0" /></a></td>
                    </tr>
                  </table></td>
                  <td align="right" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="15" height="15"></td>
                </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
  <tr>
    <td height="100%" align="center" valign="middle" class="BodyContentMagins" >
      <table width="795" height="388" border="0" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td width="162" align="center" valign="top" background="<%=basePath%>PensionView/images/menu1.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu1.gif" class="MainBt1">RPFC Forms</td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
         
                <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadrpfcform6a" target="_self" class="Link1">E-Challan </a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform7Input&form_type=FORM-7PS" target="_self" class="Link1">Form-7PS </a></td>
          </tr>
              <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform7Input&form_type=FORM-7PS-REVISED" target="_self" class="Link1">Form-7PS-Revised </a></td>
          </tr>
                    <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform7Input&form_type=FORM-7PS-SUMMARY" target="_self" class="Link1">Form-7PS-Summary </a></td>
          </tr>
                         <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform7Input&form_type=FORM-7PS-SUMMARY[REV]" target="_self" class="Link1">Form-7PS-Summary[REV] </a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform8params&form_type=FORM-8-PS" target="_self" class="Link1">Form-8PS</a></td>
          </tr>
         <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform8params&form_type=FORM-8-PS-REVISED" target="_self" class="Link1">Form-8PS-Revised</a></td>
          </tr>
          
          
        </table></td>
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu2.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu2.gif" class="MainBt2">Reports</td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadFinContri" class="Link2">Pension Contribution</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadpfcardInput" class="Link2">PF Card</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput" class="Link2">Statement of wages &amp;<br>
              Pension Contri.</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadrevisedstatmentpcwagesInput" onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Revised Statement Of Wages & Pension Contri.');return false" class="Link2">Revised Statement Of Wages & Pension Contri.</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadpcsummary"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Summary PCReport');return false" class="Link2">Annual Summary PCReport</a></td>
          </tr>
              <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadTransferINOUT" class="Link2">Station Wise Employees In/Out Report</a></td>
          </tr>
           <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadAccretionParam"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Click here to View the Remittance Report');return false" class="Link5">Accretion Report</a></td>
          </tr>
        </table></td>
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu3.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu3.gif" class="MainBt3">Data Porting<br>
             </td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadPerMstr" class="Link3">Search for PFID</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>search1?method=employeesearch" class="Link3">Edit Settlement Information</a></td>
          </tr>
      <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>validatefinance?method=importformats" class="Link3">Download Standard Formats For CPF Data</a></td>
          </tr>
        <%if(!((UserBean)session.getAttribute("userdata")).getUserType().equals("User")){%>
         <%if(!userId.trim().equals("TVMFIN")){%>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>validatefinance?method=loadimportedprocess" class="Link3">Import CPF Data</a></td>
          </tr>
          <%}%> 
         <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>validatefinance?method=getDeviationRecordsDirect"  class="Link3">Deviation Data</a></td>
          </tr>
          <%} %>
           <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu3.gif" class="MainBt3"><a  href="http://172.16.7.21:90/login.jsp" style =" border: none; outline: none;"  title="Loans & Advances">Loans & Advances</a>
             </td>
          </tr>
           <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <%if(((UserBean)session.getAttribute("userdata")).getUserType().equals("User")){%>
   			 <tr>
            <td height="35" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu5.gif" class="MainBt5" style="font-weight: bold;font-family: Arial, Helvetica, sans-serif;font-size: 10px;">Impact Calculate For PC</td>
          </tr>
   			        <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>pcreportservlet?method=loadAdjObCrtnforPc&accessCode=PE040201"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Edit</a></td>
          </tr>
                  <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode=PE040202&searchFlag=S&frmName=adjcorrections"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Approve</a></td>
          </tr>
           <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>pcreportservlet?method=searchAdjRecordsforPc&accessCode=PE040204&searchFlag=S&frmName=AdjCrtnApprovedSearch"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Approved</a></td>
          </tr>
           <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=form4inputparam"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Form4 Input Parameters</a></td>
          </tr>
   			<%}%>  
          
          
        </table></td>
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu4.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="25" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu4.gif" class="MainBt4">AAI EPF Forms</td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadob" class="Link4">AAI EPF-1</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf2" class="Link4">AAI EPF-2</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3" class="Link4">AAI EPF-3</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf4" class="Link4">AAI EPF-4</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf5" class="Link4">AAI EPF-5</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf6" class="Link4">AAI EPF-6</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf7" class="Link4">AAI EPF-7</a></td>
          </tr>
 
            <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadEPF-8" class="Link4">AAI EPF-8</a></td>
          </tr>
            <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf11" class="Link4">AAI EPF-11</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf12" class="Link4">AAI EPF-12</a></td>
          </tr>
        </table></td>
        <td width="162" align="center" valign="top" background="<%=basePath%>PensionView/images/menu5.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="35" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu5.gif" class="MainBt5">Adj.Calculator<br/>/Remittance Screens</td>
          </tr>
                <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadStationWiseRemittance"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Station Wise Remittance');return false" class="Link5">Station Wise Remittance</a></td>
          </tr>
            <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3&page=remitencescreen"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'View the Remittance Screen');return false" class="Link5">Remittance Screen</a></td>
          </tr>
             <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3&page=remitencereport"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'View the Remittance Report');return false" class="Link5">Remittance Report</a></td>
          </tr>
         
            <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>search1?method=verifiedpfidlist"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Edit-Final Settlement/Verified Cases</a></td>
          </tr>
    
      
      
          
          
          		  <%if(userId.trim().equals("ERFIN") || userId.trim().equals("NSCBFIN")){%>
						   <tr>
						 <td align="center" valign="middle" class="dots"><a
						href="<%=basePath%>PensionView/UniquePensionNumberSearchForAdjCrtnFrm1995toTillDate.jsp"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Revised form7/form8 with Monthly DATA Corrections');return false" class="Link5">Revised form7/form8 with Monthly Data Corrections</a> </td>
						  </tr>
						  <%}%>  
   			<%if(((UserBean)session.getAttribute("userdata")).getUserType().equals("User")){%>
   			 <tr>
            <td height="35" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu5.gif" class="MainBt5" style="font-weight: bold;font-family: Arial, Helvetica, sans-serif;font-size: 10px;">Calculate AdjOb on Monthly<br/> CPF Corrections</td>
          </tr>
   			        <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadAdjObCrtn&accessCode=PE040201"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Edit</a></td>
          </tr>
                  <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040202&searchFlag=S&frmName=adjcorrections"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Approve</a></td>
          </tr>
                  <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=searchAdjRecords&accessCode=PE040204&searchFlag=S&frmName=AdjCrtnApprovedSearch"  onmouseout="hideTooltip()" onmouseover="showTooltip(event,'Edit FinalSettlement /Verified Cases');return false" class="Link5">Approved</a></td>
          </tr>
           <tr>
            <td align="center" valign="middle" class="dots"><a	href="<%=basePath%>reportservlet?method=load12MnthStatmntSearchForCrtn&accessCode=PE0407&frmName=form7/8psadjcrtn" onmouseout="hideTooltip()" onmouseover="showTooltip(event,'12 Months Statement Corrections');return false" class="Link5">12/60 Months Statement Corrections</a></td>
          </tr>
   			<%}%>           
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>



</body>
</html>


