<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> New Document </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<script language="javascript" type="text/javascript">
        var test = "HIi/iiiiiiii/fd//fd";
        function change()
        {            
            test = test.replace("/", " ");
            if(test.indexOf("/") != -1)
                change();            
        }
        function change1()
        {
            change();
            alert(test);
        }
</script>
</HEAD>

<BODY onload="test();">
<%! int i=10;%>

<%out.println("nagarjun"); %>
</BODY>
</HTML>
