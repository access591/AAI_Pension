 
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


ServletContext ctx = config.getServletContext();

	basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<title>:: AAI - Employees Pension Information System ::</title>
<link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css" type="text/css">
<link href="<%=basePath%>PensionView/css/style.css" rel="stylesheet" type="text/css" />
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="<%=basePath%>PensionView/css/bootstrap.min.css" type="text/css">
  <script src="<%=basePath%>PensionView/scripts/jquery.min.js"></script>
  <script src="<%=basePath%>PensionView/scripts/popper.min.js"></script>
  <script src="<%=basePath%>PensionView/scripts/bootstrap.min.js"></script>
 
  <style type="text/css">
  .modal-footer {   border-top: 0px; }
  a {
    color: blue !important;
   cursor: pointer !important;
    /* background-color: transparent; */
}
a;hover {
    color: #fff;
     text-decoration: underline !important;
     cursor: pointer !important;
    /* background-color: transparent; */
}
  </style>
 <script language="javascript">
function testSS()
		{ 
			if(document.forms[0].username.value=="")
			{
				alert("Please Enter User Name");
				document.forms[0].username.focus();
				return false;
			}
			document.forms[0].username.value=parseInt(document.forms[0].username.value);
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
function show(){
document.getElementById('light4').style.display='block';
document.getElementById('fade').style.display='block'
}
		
		</script>
		
</head>

<body onLoad="show();document.forms[0].username.focus();" >
<form>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="68" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><table width="100%" height="58" border="0" cellpadding="0" cellspacing="0" background="PensionView/images/topbg.gif">
            <tr>
              <td width="55%" align="left" valign="top"><img src="PensionView/images/pf-title.jpg" width="586" height="63"></td>
              <td width="45%" align="right" valign="middle"><a href="PFCARDSOP.pdf">Help? </a><img src="PensionView/images/logo-epis.gif" width="99" height="33" hspace="20"></td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td height="5" align="left" valign="top" background="PensionView/images/line.gif"><img src="PensionView/images/spacer.gif" width="1" height="1"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100%" align="left" valign="middle" class="BodyContent"><table width="100%" height="416" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="82%" align="right" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="right" valign="middle" background="PensionView/images/img_bg11.jpg" style="background-position:left; background-repeat:repeat-x;">
             <table width="708" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="28" align="left" valign="middle"><table width="100%" height="204" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td>&nbsp;</td>
                    </tr>
                </table></td>
                   <td width="213" align="left" valign="middle"><img src="PensionView/images/Untitled-2.png" width="437" height="316"></td>
                <td width="460" align="left" valign="middle"><table width="467" height="204" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="center" background="PensionView/images/img_loginbg1.jpg"></td>
                    </tr>
                </table></td>
                
              </tr>
            </table></td>
          </tr>
        </table></td>
        <td width="9" align="left" valign="middle"><img src="PensionView/images/spacer.gif" width="9" height="9">
        
        </td>
        <td width="28%" align="left" valign="middle"><table width="100%" height="204" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left" valign="top" style="background:url(PensionView/images/02.png); background-repeat:no-repeat; height:358px;">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
              <td style="height:120px;" colspan="2">
              </td></tr>
                            <tr><td colspan="6">   
				<%
				System.out.println(request.getAttribute("message"));
				if (request.getAttribute("message") != null){%>
				
				  <font color="red" size="2"><%=request.getAttribute("message")%></font>
			    <%}%> </td></tr>
			    
			    <tr>
  <td width="40px"></td>
   <td style="font-family:Verdana, Geneva, sans-serif; font-size:12px; font-weight:600; color:#fff;  text-align:left; height:20px;">User Name   :</td>    
  </tr>
  <tr>
   <td width="40px"></td>
  <td style="width:150px; color:#FFF; font-size:12px; font-weight:600; font-family:Verdana, Geneva, sans-serif; font-size:12px;">  
   <input  name="username" type="text" id="username" style="width:180px !important; height:30px; font-family:Verdana, Geneva, sans-serif; font-size:12px; font-weight:600"> </td>
  </tr>
  <tr>
  <td colspan="3" style="height:5px;"></td>
  </tr>
    <tr>
  <td width="40px"></td>
   <td style="font-family:Verdana, Geneva, sans-serif; font-size:12px; font-weight:600; color:#fff;  text-align:left; height:20px;">Password   :</td>    
  </tr>
   
  <tr>
   <td width="40px"></td>
  <td style="width:150px; color:#FFF; font-size:12px; font-weight:600; font-family:Verdana, Geneva, sans-serif; font-size:12px;">  
   <input name="password" type="password" id="password"  style="width:180px !important; height:30px; font-family:Verdana, Geneva, sans-serif; font-size:12px; font-weight:600"> </td>
  <input name="usertype" type="hidden" value="Employee" />
  </tr>
  <tr>
  <td colspan="2" height="30px">
  </td></tr>
  <tr>
  <td colspan="2" style="text-align:center"> <button style="height:30px; width:60px;" class="button button green"  onclick="testSS()"> Login </button>
   </td>
  </tr>
  <tr>
  <td colspan="2" style="text-align:center">


  
  <a href="ForgotPassword.jsp" ><font color="yellow">Forgot my password?</font></a>


   </td>
  </tr>
       
</table>
</td>

          </tr>
        </table>
        </td>
      </tr>
      <tr>
  <td style="text-align:right" colspan="3"><img src="PensionView/images/nit.gif" width="176" height="54">
  </td>
  </tr>
     <tr>
  <td colspan="3" style="font-family:Verdana, Geneva, sans-serif; font-size:14px; color:#003; font-weight:600; height:40px;"><marquee>email id: epissupport@navayuga.com </marquee>
  </td>
  </tr>
    </table></td>
  </tr>
</table>
       
                          
</form>
</body> 
</html>
