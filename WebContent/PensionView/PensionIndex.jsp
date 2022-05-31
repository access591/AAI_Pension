 
<html >
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<title>:: AAI - Employees Pension Information System ::</title>
<link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css" type="text/css">
 
 <style type="text/css">
 .blinking{
    animation:blinkingText 1.2s infinite;
}
@keyframes blinkingText{
    0%{     color: #000;    }
    49%{    color: #000; }
    60%{    color: transparent; }
    99%{    color:transparent;  }
    100%{   color: #000;    }
}</style>
  
 <script language="javascript">
function testSS()
		{ 
			if(document.forms[0].username.value=="")
			{
				alert("Please Enter User Name");
				document.forms[0].username.focus();
				return false;
			}
			 if(document.forms[0].password.value=="")
			{
				alert("Please Enter Password");
				document.forms[0].password.focus();
				return false;
			}
			 else{ 
			 	document.forms[0].action="<%=basePath%>PensionLogin?method=loginpage" 
				document.forms[0].method="post";
				document.forms[0].submit();
			}
		}
		
		
function clock() {
		var date = new Date()
		var year = date.getYear()
		var month = date.getMonth()
		var day = date.getDate()
		var hour = date.getHours()
		var minute = date.getMinutes()
		var second = date.getSeconds()
			var ampm='AM'
		var months = new Array( "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

		var monthname = months[month];

		if (hour > 12) {
		hour = hour - 12
		ampm='PM';
		}

		if (minute < 10) {
		minute = "0" + minute
		}

		if (second < 10) {
		second = "0" + second
		}


		document.getElementById('clocktime').innerHTML =  monthname + " " + day + ", " + year + " - " + hour + ":" + minute + ":" + second+" "+ampm+"&nbsp;&nbsp;";

		setTimeout("clock()", 1000)

}
		
		</script>
</head>

<body onload="document.forms[0].username.focus();" >
<form>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="68" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><table width="100%" height="58" border="0" cellpadding="0" cellspacing="0" background="<%=basePath%>PensionView/images/topbg.gif">
            <tr>
              <td width="55%" align="left" valign="top"><img src="<%=basePath%>PensionView/images/title.gif" width="586" height="63"></td>
              <td width="45%" align="right" valign="middle"><img src="<%=basePath%>PensionView/images/logo-epis.gif" width="99" height="33" hspace="20"></td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td height="5" align="left" valign="top" background="<%=basePath%>PensionView/images/line.gif"><img src="<%=basePath%>PensionView/images/spacer.gif" width="1" height="1"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100%" align="left" valign="middle" class="BodyContent"><table width="100%" height="416" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="72%" align="right" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="right" valign="middle" background="<%=basePath%>PensionView/images/img_bg1.jpg" style="background-position:left; background-repeat:repeat-x;">
             <table width="708" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="28" align="left" valign="middle"><table width="100%" height="204" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td>&nbsp;</td>
                    </tr>
                </table></td>
                <td width="213" align="left" valign="middle"><img src="<%=basePath%>PensionView/images/img_support.jpg" width="213" height="422"></td>
                <td width="460"   align="left" valign="middle"><table width="467" height="204" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="center" background="<%=basePath%>PensionView/images/img_loginbg.jpg"><table width="320" border="0" cellpadding="0" cellspacing="4" class="text">
                          <tr>
                            <td align="right" valign="middle">&nbsp;</td>
                            <td align="left" valign="middle">&nbsp;</td>
                          </tr>
                          <tr>
                            <td align="right" valign="middle">&nbsp;</td>
                            <td align="left" valign="middle">&nbsp;</td>
                          </tr>
                            <tr><td colspan="6">   
				<%
				System.out.println(request.getAttribute("message"));
				if (request.getAttribute("message") != null){%>
				
				  <font color="red" size="2"><%=request.getAttribute("message")%></font>
			    <%}%> </td></tr>
                          <tr>
                            <td width="104" align="right" valign="middle">User Name: </td>
                            <td width="204" align="left" valign="middle"><label>
                              <input name="username" type="text" id="username" class="TxtField">
                            </label></td>
                          </tr>
                          <tr>
                            <td align="right" valign="middle">Password:</td>
                            <td align="left" valign="middle"><label>
                              <input name="password" type="password" id="password" class="TxtField">
                               <input name="usertype" type="hidden" value="Admin" >
                            </label></td>
                          </tr>
                          <tr>
                            <td align="right" valign="middle">&nbsp;</td>
                            <td height="30" align="left" valign="middle"><input name="Submit" type="image" value="Submit"  onclick="testSS()" src="<%=basePath%>PensionView/images/button_login.gif"></td>
                          </tr>
                      </table></td>
                    </tr>
                    
                </table></td>
              </tr>
            </table></td>
          </tr>
        </table></td>
        <td width="9" align="left" valign="middle"><img src="<%=basePath%>PensionView/images/spacer.gif" width="9" height="9"></td>
        <td width="28%" align="left" valign="middle" class="NITlogo"><table width="100%" height="204" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left" valign="top" background="<%=basePath%>PensionView/images/img_bg2.jpg" style="background-position:top; background-repeat:repeat-x;"><img src="<%=basePath%>PensionView/images/img_altservers.jpg" width="283" height="204" border="0" usemap="#Map" />
              <map name="Map">
                <area shape="rect" coords="97,164,185,187" href="http://172.16.2.93:3167/PensionView/PensionIndex.jsp" target="_self" title="Server -3">
                <area shape="rect" coords="97,114,185,138" href="http://172.16.7.21:81/PensionView/PensionIndex.jsp" target="_self" title="Server -1">
                <area shape="rect" coords="97,140,185,162" href="http://172.16.2.91/PensionView/PensionIndex.jsp" target="_self" title="Server -2">
              </map></td>
          </tr>
        </table></td>
      </tr>
      <tr>
      <td colspan="3" align="center"><img alt="New" src="<%=basePath%>PensionView/images/new.png" height="40px" width="50px">
      <span class="blinking"> For Individual PF Card Login click in the below link: </span>
      <br/><a href="http://epis.aai.aero/Login.jsp">Individual PF Card Login</a> 
      </td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body> 
</html>
