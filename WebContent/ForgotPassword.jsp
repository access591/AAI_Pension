 
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
	
	String message="";
	if(session.getAttribute("msg")!=null){
	message=session.getAttribute("msg").toString();
	session.invalidate();
	}
	System.out.println("message:"+message);
	
%>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<title>:: AAI - Employees Pension Information System ::</title>
<link rel="stylesheet" href="<%=basePath%>PensionView/css/epas.css" type="text/css">
<link href="<%=basePath%>PensionView/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>PensionView/css/date/datepicker.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>PensionView/css/font-awesome/css/font-awesome.min.css"
			rel="stylesheet" type="text/css" />
			
		<link href="<%=basePath%>PensionView/css/style-metronic.css" rel="stylesheet"
			type="text/css" />	
			<link href="<%=basePath%>PensionView/css/style-responsive.css" rel="stylesheet"
			type="text/css" />
			<link href="<%=basePath%>PensionView/css/uniform.default.min.css"
			rel="stylesheet" type="text/css" />
			
			
  <link rel="stylesheet" href="<%=basePath%>PensionView/css/bootstrap.min.css" type="text/css">
  <script src="<%=basePath%>PensionView/scripts/jquery.min.js"></script>
  <script src="<%=basePath%>PensionView/scripts/popper.min.js"></script>
  <script src="<%=basePath%>PensionView/scripts/bootstrap.min.js"></script>
  <script src="<%=basePath%>PensionView/scripts/app.js"></script>
  <script type="text/javascript" src="<%=basePath%>PensionView/scripts/date/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>PensionView/scripts/date/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="<%=basePath%>PensionView/scripts/date/clockface.js"></script>
    <script type="text/javascript" src="<%=basePath%>PensionView/scripts/date/bootstrap-daterangepicker/moment.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>PensionView/scripts/date/bootstrap-daterangepicker/daterangepicker.js"></script>
 
  <style type="text/css">
  .modal-footer {   border-top: 0px; }
  </style>
  
   <script type="text/javascript">
            $(function () {
                $(".date-picker").datepicker({autoclose:true});
            });
        </script>
 <script language="javascript">


function validate()
		{ 
			
			 if(document.forms[0].user.value=="")
			{
				alert("Please Enter User Name");
				document.forms[0].user.focus();
				return false;
			}
			document.forms[0].user.value=parseInt(document.forms[0].user.value);
			 if(document.forms[0].doj.value=="")
			{
				alert("Please Enter Date of Joining");
				document.forms[0].doj.focus();
				return false;
			}
			  
		document.forms[0].action="<%=basePath%>PensionLogin?method=forgotpassword" 
				document.forms[0].method="post";
				document.forms[0].submit();    
   
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
	<style type="text/css">
	.form-gap {
    padding-top: 70px;
}
fieldset {
    min-width: 100% !important;
    padding: 20px !important;
    margin: 0 !important;
    border: solid 1px #7cb1e6 !important;
    border-radius: 12px !important;
    background: #cfe3f7 !important;
}
label {
    display: inline-block;
    margin-bottom: .5rem;
    text-align: right;
}
.btn.default {
    color: #333333;
    padding: 17px;
    height: 37px;
    text-shadow: none;
    background-color: #e5e5e5;
}
.required
{color:red;}
</style>
		
</head>

<body onLoad="show();document.forms[0].username.focus();" >
<form>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="68" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><table width="100%" height="58" border="0" cellpadding="0" cellspacing="0" background="PensionView/images/topbg.gif">
            <tr>
              <td width="55%" align="left" valign="top"><img src="PensionView/images/title.gif" width="586" height="63"></td>
              <td width="45%" align="right" valign="middle"><img src="PensionView/images/logo-epis.gif" width="99" height="33" hspace="20"></td>
            </tr>
        </table></td>
      </tr>
      <tr>
        <td height="5" align="left" valign="top" background="PensionView/images/line.gif"><img src="PensionView/images/spacer.gif" width="1" height="1"></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="100%" align="center" valign="middle" class="BodyContent">
    <!-- <div class="row">
    <div class="col-md-12">
    <h3 class="text-center">Forgot Password?</h3>
    <div class="breadcrumb breadcrumb"> </div>
    </div>
     </div> -->
    
    
    <div class="form-gap"></div>
  
<div class="container">
<fieldset>
<div class="row"> 
<div class="col-md-12"> <font color="red" style="font-size: 15px; font-weight: 600; letter-spacing: 1px;"> <%=message %></font></div>
</div>
	<div class="row">
	<div class="col-md-4"></div>
		<div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
              <div class="panel-body">
                <div class="text-center">
                  <i class="fa fa-lock fa-4x" style="color:#0567d0;"></i>
                  <h3 class="text-center">Forgot Password?</h3>
                  <p>You can reset your password here.</p>
                  <div class="panel-body">
    
                    <form id="register-form" role="form" autocomplete="off" class="form" method="post">
    <div class="row">
    <div class="col-md-12">
                      <div class="form-group">
                        <div class="input-group">
                         <label class="control-label col-md-6">User Name <span class="required">*</span> :</label> 
                         <div class="col-md-6">                        
                          <input id="user" name="user" placeholder="User Name" class="form-control"  type="text" style="width:237px;">
                        </div>
                        </div>
                      </div>
                      </div>
                 </div>     
                      
                         <!-- <div class="col-md-6">                       
                         <div class="input-group input-medium date date-picker col-md-12"  data-date-format="dd-M-yyyy" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input type="text" name="dob" value=""class="form-control" readonly>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" style="padding: 4px 14px;"><i class="fa fa-calendar"></i></button>
                                                        </span>
                                                    </div>
                        </div>-->
                        
                        <!--  <div class="col-md-6">
                                                    <div class="input-group input-medium date date-picker col-md-12"  data-date-format="dd-M-yyyy" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input type="text" name="doj" class="form-control"  readonly>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" style="padding: 4px 14px;"><i class="fa fa-calendar"></i></button>
                                                        </span>
                                                    </div>
                                                </div>-->
                                                
                                                
                                                 <div class="row">
                                                 <div class="col-md-12">
                      <div class="form-group">
                        <div class="input-group">
                         <label class="control-label col-md-6"> Date of Joining <span class="required">*</span> :</label> 
                         <div class="col-md-6">                        
                        <div class="input-group input-medium date date-picker"  data-date-format="dd-M-yyyy" data-date-viewmode="years" data-date-minviewmode="months">
                                                        <input type="text" name="doj"  placeholder="Date of Joining" id="doj" class="form-control" readonly>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" style="padding: 4px 14px;"><i class="fa fa-calendar"></i></button>
                                                        </span>
                                                    </div>
                        </div>
                        </div>
                      </div>
                      </div> 
                      </div>   
                  </form>
    
                  </div>
                </div>
              </div>
              </div></div></div>
              <div class="row">
              <div class="col-md-12">
               <div class="form-group">
               <a class="btn btn-secondary" href="<%=basePath%>Login.jsp" role="button" style='height: 43px; color: #fff; width: 86px;'>Back</a>
                         
                        <input name="recover-submit" class="btn btn-primary" value="Reset Password" type="submit" onclick="return validate()" style='height: 43px; color: #fff;'>
                      </div>
              </div>
              </div>
              </fieldset>
              </div>
              
    </td>
  </tr>
</table>
       
                          
</form>
</body> 
</html>
