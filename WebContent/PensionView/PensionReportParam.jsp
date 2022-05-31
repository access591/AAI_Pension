
<%@ page language="java" errorPage="/view/common/ErrorPage.jsp" import="java.sql.*,java.io.*,java.util.*" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%!
	public String checkNull( String str ) {
		if( str == null )
			return "";
		else
			return str.trim();
	}
%>
<% 	
	String[] year = {"1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009"};

	String month[] = {"January","febraury","March","April","May","June","July","August","September","October","November","December"};

	String monthNo[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};

	String forms[] = {"FORM-1","FORM-2","FORM-3","FORM-4","FORM-5","FORM-6"};

	String regions[]={"W","S","N","E","NE"};

	String regionNames[]={"West Region","South Region","North Region","East Region","North East Region"};
	
%>



<HTML>
<HEAD>
<link rel='stylesheet' href='/css/aai.css' type='text/css'>
<link rel="stylesheet" href="/css/reportstyle.css" type="text/css">

<script language="javascript">
	var wind1="";
	function clear_form(){
		document.forms[0].reset();
		//document.forms[0].billFrom.focus();
	}
	function validateForm() {
		var swidth=screen.Width-10;
		var sheight=screen.Height-150;

		if(document.forms[0].formType.value==''){
			alert('Please Select Form Type (Mandatory)');
			document.forms[0].formType.select();
			return false;
		}else{
			
			var params = "?region="+document.forms[0].region.value+"&month="+document.forms[0].month.value+"&year="+document.forms[0].year.value;

			if(document.forms[0].formType.value=='FORM-4'){
			
				url = "<%=basePath%>PensionView/PensionReportForm-4.jsp";
			}else if(document.forms[0].formType.value=='FORM-5'){
				url = "/PensionView/PensionReportForm-5.jsp";
			}else {
				url = "/PensionView/PensionReportForm-5.jsp";
			}
			wind1 = window.open(url+params,"winComps","toolbar=yes,scrollbars=yes,menubar=yes,resizable=yes,width="+swidth+",height="+sheight+",top=0,left=0");
			winOpened = true;
			wind1.window.focus();
			return false;
		}
		//return false;
	}
	
	
	
</script>
</HEAD>

<!--onLoad="clear_form1();"-->
<BODY onUnload="closeWin()" text=#000000  leftMargin=0 topMargin=0 
link="white" vlink="white" >
<FORM name="pensionRpt" method="post"  onSubmit="return validateForm();">
<TABLE borderColor=#000066 cellSpacing=0 cellPadding=0 align=center width="100%" height="100%"  border=0>

<TBODY>
	<TR>
		<TD vAlign=top width=0  colSpan=4 height=0>
		<TABLE cellSpacing=0 cellPadding=0 width="50%" height="100%" border=0 align="center" valign="middle">
		<TBODY>

			<TR>

			<TD align=center width="94%"  height=275>
			<table border="0" cellspacing="0" cellpadding="0" align="center" valign="middle">
			<tr>
				<td width="10"><IMG height=20 src="/images/gui/curver_tl.gif" width=10></td>
				<td bgcolor="#000066" class="Param">Pension Fund Report [Parameter]</td>
				<td width="10"><IMG height=20 src="/images/gui/curver_tr.gif" width=10></td>
			</tr>
			<tr>
			<td width="10" background="/images/gui/line_l.gif" valign="bottom">&nbsp;</td>
			<td align="middle" valign="top">

			<table align="center" valign="middle" border=0 cellpadding=3 cellspacing=0 >
			<tr>
				<TH class="tbb" ALIGN=RIGHT colspan=6></th>
			</tr>

			<TR>
				<td class=tbb align="right" nowrap >Year</td>
				<td><Select name='year' Style='width:100px'>
					<option value='' >[Select One]</option>
					<% for(int j=0;j<year.length;j++) {%>
					<option value='<%=year[j]%>' ><%=year[j]%></option>
					<%}%>
					</SELECT>
				</td>
				<td colspan=1></td>
			</TR>

			<TR>
			   
				<td class=tbb align="right" nowrap >Month</td>
				<td>
				<Select name='month' Style='width:100px'>
					<option value=''>[Select One]</option>
					<% for(int j=0;j<month.length;j++) {%>
					<option value='<%=monthNo[j]%>' ><%=month[j]%></option>
					<%}%>
				</SELECT>
				</td>&nbsp;&nbsp;
			</TR>
			<tr>
				<Th class="tbb" ALIGN=RIGHT>Region</th>
				<Td>
				<Select name='region' Style='width:120px'>
					<option value='' >[Select One]</option>
					<% for(int j=0;j<regionNames.length;j++) {%>
					<option value='<%=regionNames[j]%>' ><%=regionNames[j]%></option>
					<%}%>
				</SELECT>
				</td>
			</tr>
			<TR>
				<td class=tbb align="right" nowrap ><font color=red>*</font>&nbsp;Form Type</td>
				<td>
				<Select name='formType' Style='width:100px'>
					<option value='' >[Select One]</option>
					<% for(int j=0;j<forms.length;j++) {%>
					<option value='<%=forms[j]%>' ><%=forms[j]%></option>
					<%}%>
				</SELECT>
				</td>&nbsp;&nbsp;
			</TR>
			<tr>
				<TH class="tbb" ALIGN=RIGHT colspan=6>&nbsp;</th>
			</tr>
			<TR>
				<td colspan=4 align="right">&nbsp;
				<a href="javascript:void(0)" onClick="clear_form()" ><IMG border=0
				src="/images/action/clearBtn.gif" alt="Clear"></a>
				<input type=image border=0 src="/images/action/okBtn.gif" alt="Ok">&nbsp;&nbsp;
				</TD>
			</TR>
			</table>
			</td>
			<td width="10" height=5 background="/images/gui/line_r.gif">&nbsp;</td>
			</tr>
			<tr> 
				<td><img src="/images/gui/curver_bla.gif"></td>
				<td width="0" background="/images/gui/bluebg.gif"></td>
				<td><img src="/images/gui/curver_bra.gif"></td>
			</tr>
		</table>
		<input type="hidden" name="userOption" value="search">
		</TD>

	</TR>

		</TBODY>
		</TABLE>
	</TD>
	</TR>
</FORM>
</TBODY>
</TABLE>
</BODY>
</HTML>