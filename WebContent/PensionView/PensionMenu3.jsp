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
                    
                      <td align="center" valign="middle"><a href="<%=basePath%>PensionLogin?method=normalusermenu"><img src="<%=basePath%>PensionView/images/home.gif" title="Home" width="16" height="16" border="0" /></a></td>

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
      <table width="470" height="388" border="0" cellpadding="0" cellspacing="0" border="0">
      <tr>
     
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu2.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu2.gif" class="MainBt2">RPFC Forms</td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>

        
       		<tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadform3params" class="Link2">RPFC Form-3</a></td>
          </tr>
               <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadrpfcform4" class="Link2">RPFC Form-4</a></td>
          </tr>
               <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadrpfcform5" class="Link2">RPFC Form-5</a></td>
          </tr>
                  <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=payArrears" class="Link2">Pay Arrears (DBF-RPFC)</a></td>
          </tr>
                  <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadForm10D" class="Link2">Form-10D</a></td>
          </tr>
                     <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadrpfcform6a" class="Link2">EDLI Inspection Charges</a></td>
          </tr>
             <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadTransferINOUT" class="Link2">Transfer In/Out Report</a></td>
          </tr>
   
          
        </table></td>
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu3.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu3.gif" class="MainBt3">Personnel<br>
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
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadpersonalreport" class="Link3">EmployeeInfo -Death/Retd./Resigned</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>validatefinance?method=importformats" class="Link3">Download Standard Formats For CPF Data</a></td>
          </tr>
          <%if(userId.equals("ADITI")){%>
           <tr>
            <td height="30" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu3.gif" class="MainBt3">PFID CREATION<br>
             </td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadPFIDProcessSearchForm&frmName=PFIDProcessSearch" class="Link3">NEW</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadPFIDProcessSearchForm&frmName=PFIDRecommendationSearch" class="Link3">Recommendation</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadPFIDProcessSearchForm&frmName=PFIDApprovalSearch" class="Link3">Approval</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>psearch?method=loadPFIDProcessSearchForm&frmName=PFIDApprovedSearch" class="Link3">Approved</a></td>
          </tr>
          <%}%>
        </table></td>
        <td width="157" align="center" valign="top" background="<%=basePath%>PensionView/images/menu4.png" class="MenuPadding"><table width="128" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="25" align="center" valign="middle" background="<%=basePath%>PensionView/images/button_menu4.gif" class="MainBt4">Reports</td>
          </tr>
          <tr>
            <td height="5" align="center" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="10" height="10"></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadFinContri" class="Link4">Pension Contribution Stmt</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadpfcardInput" class="Link4">PF Card</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=loadstatmentpcwagesInput" class="Link4">Statement Of wages & Pension Contri.</a></td>
          </tr>
             <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf3" class="Link4">AAI EPF-3</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>aaiepfreportservlet?method=loadepf4" class="Link4">AAI EPF-4</a></td>
          </tr>
          <tr>
            <td align="center" valign="middle" class="dots"><a href="<%=basePath%>reportservlet?method=retirementEmployeesInfo" class="Link4">Monthly Seperation Employee Info</a></td>
          </tr>
 
        </table></td>
       
      </tr>
    </table></td>
  </tr>
</table>



</body>
</html>


