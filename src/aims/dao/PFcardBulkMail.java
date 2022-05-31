package aims.dao;


import java.awt.Desktop;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import aims.service.Authentication;

//import com.itextpdf.text.DocumentException;

public class PFcardBulkMail 
extends HttpServlet  {
	private static final long serialVersionUID = 4L; 

private Connection con;
private MimeMessage message;
private Statement st;
private static String checkmail="";
private Map map;
private ArrayList idList;
private Map failMap;
private ArrayList failList;
private ServletContext ctx;
private String userName="",desig="";

private Properties prop;
private Properties apdetails;



public   Map  PFcardBulkMail(String reportName,String pdfpath ,HttpServletRequest ctx1,String rpath,Connection con,String mailpropspath,String billNo,String contractorCd,String billType,String billDate,String billTypes,String fromEmailID)  throws Exception{
	
	map = new TreeMap();
	
    try {
    	
       
    	map=doConversion(reportName, pdfpath,ctx1,rpath,con,mailpropspath,billNo,contractorCd,billType,billDate,billTypes,fromEmailID);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return map;
}

	
   	
	
public  Map  doConversion(String url, String pdfpath,HttpServletRequest ctx1,String rpath,Connection con,String mailpropspath,String billNo,String contractorCd,String billType,String billDate,String billTypes,String fromEmailID) 
    throws Exception {
	
	//HttpSession session = ctx1.getSession(); 
	// File output = new File(rpath+pdfpath+billNo.replaceAll("/", "_")+".pdf");   
	 File direcotory = new File(pdfpath); 
	 if (!direcotory.exists()) 
		 {
		 direcotory.mkdir();
		 
		 }
	 
	
	
	
   
    int userSpaceWidth=1300;
    int topValue=2;
    int leftValue=5;
    int bottomValue=0;  
    int rightValue=5;
    PD4ML pd4ml = new PD4ML();
    //System.setProperty("java.net.useSystemProxies", "true");
    //pd4ml.setCookie("JSESSIONID", session.getId());
    pd4ml.setHtmlWidth(userSpaceWidth);
    //pd4ml.setPageSize(pd4ml.changePageOrientation(PD4Constants.A4));
    //pd4ml.fitPageVertically();  
    pd4ml.setPageSize(PD4Constants.A4); 
    pd4ml.adjustHtmlWidth();
    //pd4ml.addStyle("../../css/Reportbillstyle.css", true);
    pd4ml.addStyle("./PensionView/css/aai.css", true);
    pd4ml.addStyle("<meta http-equiv=Content-Type content=text/html; charset=UTF-8/>",true);
    pd4ml.setPageInsetsMM(new Insets(topValue, leftValue, bottomValue, rightValue));
    
    //url="http://"+ctx1.getRemoteAddr()+":"+ctx1.getServerPort()+ctx1.getContextPath()+url;
//System.out.println("pdfffffff method111111111111"+url); 
         
url="http://nit523:82"+url;
    //url="http://nitlap112:7003/"+ctx1.getContextPath()+url;
    
   // url="C:/Users/prasadp/Desktop/2255.mht";
   
 // url="http://172.16.14.32:7777/"+ctx1.getContextPath()+url;
   
   // ServletContext ctx = config.getServletContext(); 
   System.out.println("url-----+++++++++-----"+url);
  
  
   
    //java.net.URL url1 = new URL(null, url,new sun.net.www.protocol.https.Handler());
  
    //java.net.HttpURLConnection  connection = (HttpsURLConnection) url1.openConnection();
  // System.out.println("pdfffffff method111111111111"+url);
   System.out.println("pdfffffff rpath+pdfpath"+rpath+pdfpath);
   //System.out.println("pdfffffff getSchemegetScheme"+ctx1.getScheme());

   File output = new File(pdfpath+rpath); 
   System.out.println("111111111111");
   java.io.FileOutputStream fos = new java.io.FileOutputStream(output); 
   System.out.println("2222222");
  
    pd4ml.render(url, fos);
    //System.out.println("pdfffffff method333333333");
    String subject="";
    subject="PF Card 2019-20";
    //String contractorCd="00882";
    /*if(billType.equals("SR")) {
    	subject=" CommercialBills:"+billNo;
    }else {
    	subject=""+billNo;
    }*/
    
    String path=pdfpath+rpath;
   
    	
    fos.close();
    //System.out.println("11111111111");
     //map=setBody(subject, contractorCd, billType, billNo, path, billDate,con, mailpropspath,null,billTypes,fromEmailID) ;
    
    return map;
}




public String checkNull(String str) {
	if(str==null)
		return "";
	else
		return str.trim();
}
public Map setBody(String subject,String contractorCd,String billType,String billNo,String path,String billdate,Connection con,String mailpropspath,HttpSession session,String billTypes,String fromEmailID) throws Exception {

	
	
	
	
	//System.out.println("sessionBean.getAirportCd()=="+sessionBean.getAirportCd());
	//System.out.println("sessionBean.getCurrModule()=="+sessionBean.getCurrModule());
			this.prop = prop;
			this.apdetails = apdetails;
			//this.mailpropspath = mailpropspath;
			//this.path = path;
			//this.sessionBean = sessionBean;
			this.ctx = ctx;
			//this.con = con;
			String mailstaus="";
			//st=con.createStatement();
			/*ResultSet desg= st.executeQuery("select * from am_user_mt ss ,am_designation_mt sd where ss.DESIGNATION_CD=sd.DESIGNATION_CD and ss.usrid='"+sessionBean.getUserId()+"'");
			//ResultSet desg= st.executeQuery("select * from am_user_mt ss ,am_designation_mt sd where ss.DESIGNATION_CD=sd.DESIGNATION_CD and ss.usrid='00003'");
			if(desg.next())
			{
			userName=desg.getString("NAME");
	        desig=desg.getString("DESCRIPTION");
			}*/

		
			/*sh = new SQLHelper();
			mc = new model.common.MoneyChanger();
			cf = new model.common.DBAccess();
*/
			ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.Mail");

			System.out.println(bundle.getString("host"));

			String host = bundle.getString("host");
			String authid = bundle.getString("authid");
			String authpwd = bundle.getString("authpwd");
			String port = bundle.getString("port");

			Properties prop1 = new Properties();

			

			// If using static Transport.send(),need to specify the mail server here
			prop1.put("mail.smtp.host", host);

			prop1.put("mail.smtp.auth", "true");
			prop1.put("mail.smtp.port", port);
	        //Setting Authentication Mail Server UserID and Password 
			Authenticator auth = new Authentication(authid,authpwd);

			

			/*// Get a session
			Session session1 = Session.getInstance(prop1,auth);
			message = new MimeMessage(session1);
*/
			//success=new Hashtable();
			
			//map = new TreeMap();
			failMap = new TreeMap();
			//fail=new Hashtable();
			idList = new ArrayList();
			failList = new ArrayList();
	
	
	
	/*
	 * Bill Disclaimer has been divided by module Codes.
	 * */
	//String moduleCode=checkNull(sessionBean.getCurrModule());

	
	/**
	 * Previously from mail id is 'aims@aai.aero' Now it is replaced as the Active  
	 * mail id given in masters From EmailID for Bills Mailing.
	 */
	
	
	java.sql.ResultSet rs = null;
	java.sql.ResultSet rs1 = null;
	
	fromEmailID = "epissupport@navayuga.com";
  
	/** Changed mail heading 'Commercial' to 'AAI' By Srilakshmi 21/08/08 */
	InternetAddress add=new InternetAddress(fromEmailID,"DONOTREPLY AAI "+"");

	/** End of 18/08/08 *******/
	message.setFrom(add);
	
	//rs=st.executeQuery("select email from cm_contractor_mt  where CONTRACTOR_CD='"+contractorCd+"'");

	String mail="prasad.pedipina@navayugainfotech.com";	

	String mailstatus = "Y";
	String testCon="";
	

	/*if(rs.next()){
		 mail=rs.getString("email");
	}*/
System.out.println("1111111111mail"+mail);
		
	if( ("AgeWise".equals(billType)?(mail!=null && !mail.equals("") && !checkmail.equals(mail)):(mail!=null && !mail.equals("")))){
		
		String[] billTypeDesc = subject.split("\\:");
		
		try {	
		
			//To send bill to multiple mails
			String[] recipients = mail.split("\\,");
			//System.out.println("recipients.mail---------"+mail);	
			String mailtemp=mail.replaceAll(",", "|");
		//System.out.println("recipients.length---------"+recipients.length);	
			InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
			for(int addrtoval = 0; addrtoval < recipients.length; addrtoval++) {
				addressTo[addrtoval] = new InternetAddress(recipients[addrtoval]);
				if(map.containsKey(billType+'$'+billNo)) {
					//System.out.println("recipients[addrtoval]"+recipients[addrtoval]);
					idList = (ArrayList)map.get(billTypes);
					idList.add(contractorCd+","+billNo+","+mailtemp+","+billTypeDesc[0]+","+mailstatus);
					map.put(billTypes+'$'+billNo,idList);
				}else{
					idList = new ArrayList();
					//System.out.println("recipients[addrtoval]else "+recipients[addrtoval]);
					idList.add(contractorCd+","+billNo+","+mailtemp+","+billTypeDesc[0]+","+mailstatus);
					map.put(billTypes+'$'+billNo,idList);
				}
				//idList.add(contractorCd+","+billNo+","+recipients[addrtoval]);
			}
			 //System.out.println("addressTo--1111---"+addressTo);
		  message.setRecipients(Message.RecipientType.TO, addressTo);
		
		 // System.out.println("subject setbody--"+subject+"==");
			message.setSubject(subject);
			message.setSentDate(new java.util.Date());

			String strSignatory = "";
			
			
		/*	String qryForDisclaimer1 = "select esignature ,SIGN_IMAGE,SIGNATORY,COPYTO from CM_SIGNATORY_MT  WHERE SHOW_STATUS='Y' and Billtype in ('A','"+billType+"')  and (EFFECTIVE_FROM is null OR EFFECTIVE_FROM<='"+billdate+"') and '"+billdate+"' between EFFECTIVE_FROM and EFFECTIVE_TO and upper(MODULE)=upper('"+moduleCode+"') ORDER BY SERIAL_NO asc";
			//System.out.println("qryForDisclaimer------IMG--------"+qryForDisclaimer);
			ResultSet rsSign = null;
			boolean flag=true;
				rsSign = st.executeQuery(qryForDisclaimer1);
				  int size=0;
				while(rsSign.next()) {
					flag=false;
					strSignatory=checkNull(rsSign.getString("SIGNATORY"));
					//copyTo=checkNull(rsSign.getString("COPYTO"));

					
			//System.out.println("size-----"+size);
				}*/
			
				
/*
			String qryForDisclaimer=" select nvl(SIGNATORY,'')SIGNATORY1 from CM_BILL_DISCLAIMER  WHERE SHOWSTATUS='Y' and Billtype in ('A','"+billType+"') and (EFFECTIVE_FROM is null OR EFFECTIVE_FROM<='"+billdate+"') and SIGNATORY is not null ";
			if(!moduleCode.equals("") || moduleCode!=null){
			qryForDisclaimer+=" and upper(MODULECD)=upper('"+moduleCode+"') ";
			}
			qryForDisclaimer+=" ORDER BY SERIAL_NO ASC ";
		  // System.out.println("qryForDisclaimer----"+qryForDisclaimer);
			ResultSet rsForDisclaimer=st.executeQuery(qryForDisclaimer);
            String tempSignatory="";
			if(rsForDisclaimer!=null) {
				while(rsForDisclaimer.next()) {
					tempSignatory=checkNull(rsForDisclaimer.getString("SIGNATORY1"));
				}
			}*/
			/*if(rsForDisclaimer != null)
				rsForDisclaimer.close();
			
			if(strSignatory.equals("")){
				strSignatory=tempSignatory;
				
				}*/

			/*rs = st.executeQuery("select AIRPORT_TYPE AIRPORT_TYPE,REGION_CD from tc_location_mt where  LOCATION_CD='"+sessionBean.getAirportCd()+"' ");
			
			if(rs.next()) {
				String region_cd = sh.checkNull(rs.getString("REGION_CD"));
				if(rs.getString("AIRPORT_TYPE").equals("I")) {
//System.out.println("AIRPORT_TYPE:--"+rs.getString("AIRPORT_TYPE"));
					if(strSignatory!=null && strSignatory.length()>0) {
						strSignatory = strSignatory;
					}else{
               //     System.out.println("KKKKK00000%%%%%%%%%%%%%%%");
						strSignatory+= "Dy.General Manager (Finance) <BR>";
						strSignatory+= "AIRPORTS AUTHORITY OF INDIA <BR>";
						strSignatory+= sessionBean.getAirportDetails();
					
						
					}
				}else{
					//System.out.println("AIRPORT_TYPE:--"+rs.getString("AIRPORT_TYPE"));
					if(strSignatory!=null && strSignatory.length()>0) {
						strSignatory = strSignatory;
					}else{
						strSignatory= "For Airport Director <BR>";
						strSignatory+= "AIRPORTS AUTHORITY OF INDIA <BR>";
						strSignatory+= sessionBean.getAirportDetails();
						
					}
				}
			}
			if(rs != null){
				rs.close();
			}
			//String htmlText = "<table><tr><td><font face='arial'>Dear Sir/Madam,<br><br>Please find the Attached bill for the period mentioned in the Subject.<BR>Kindly acknowledge the receipt of the Bill with this "+sh.getDescription("CM_CONTRACTOR_MT","OWNER_NAME","CONTRACTOR_CD",contractorCd,con)+"  name.<BR><BR>Best Regards<BR><BR></font></td></tr>";
			String mailsubject="";
			System.out.println("select subject from cm_mail_subject where '"+billdate+"' between fromdate and todate and upper(module_cd)=upper(nvl('"+moduleCode+"','CM')) ORDER BY slno asc");
           rs = st.executeQuery("select subject from cm_mail_subject where '"+billdate+"' between fromdate and todate and upper(module_cd)=upper(nvl('"+moduleCode+"','CM')) ORDER BY slno asc");
         while(rs.next()) {
			 mailsubject=sh.checkNull(rs.getString("subject"));
		 }
		 if(rs != null){
				rs.close();
		 }*/
			String htmlText = "<table><tr><td><font face='arial'>Dear Sir/Madam,<br><br>Please find the Attached "+billTypeDesc[0]+"  in respect of "+""+".<BR>"+"<BR><BR>Best Regards<BR><BR></font></td></tr>";

		  if(!subject.startsWith("StationAgeWiseOutstanding")) 			
			{ htmlText+="<tr><td><pre>"+strSignatory+"</pre></td></tr></table>";
			}
			else{
			htmlText+="<tr><td><pre>"+userName+"</pre></td></tr><tr><td><pre>"+desig+"</pre></td></tr></table>";
			}
			BodyPart bodyPart1 = new MimeBodyPart();
			bodyPart1.setContent(htmlText, "text/html ; charset=utf-8");
			
			
			//Adds Attechment:
			BodyPart bodyPart2 = new MimeBodyPart();
			//first attachment
        	
			//DataSource source = new FileDataSource(path+billNo.trim().replaceAll("/", "_")+".pdf");
			DataSource source = new FileDataSource(path);
			bodyPart2.setDataHandler(new DataHandler(source));
			//System.out.println("setbodypathpath"+path);
			//System.out.println("setbodybillNobillNobillNo"+billNo);
			bodyPart2.setFileName("2255.pdf"); 

			Multipart multipart = new MimeMultipart();
			//Create a related multi-part to combine the parts
			multipart.addBodyPart(bodyPart1);
			multipart.addBodyPart(bodyPart2);
			message.setContent(multipart);
			//Transport.send(message);
			mailstaus="Success";
			
			
		     
			
			
		}catch (MessagingException e) {
		   System.err.println("Can't send mail. for "+contractorCd+"   "+ e);
					  
		   if(failMap.containsKey(billType+'$'+billNo)) {
				failList = (ArrayList)failMap.get(billTypes);
				failList.add(contractorCd+","+billNo+","+mail+","+billTypeDesc[0]);
				//failMap.put(billTypes,idList);
				failMap.put(billTypes+'$'+billNo,idList);
				
			}else{
				failList = new ArrayList();
				failList.add(contractorCd+","+billNo+","+mail+","+billTypeDesc[0]);
				//failMap.put(billTypes,failList);
				failMap.put(billTypes+'$'+billNo,idList);
				
			}
		   //map.remove(billTypes+'$'+billNo,idList);
		   //fail.put(contractorCd,billNo+","+mail);
		  
		   mailstaus="Fail";
		   
		}
	}
	
	 
	 
	 session.setAttribute("success", map);
	 session.setAttribute("fail", failMap);
	
       //ctx.setAttribute("success",map);
	 // ctx.setAttribute("fail",failMap);
	 System.out.println("map-----"+mailstaus);	
		
	 /*if("AgeWise".equals(billType))
		 checkmail=mail;
	*/
	
	
    path=null;
    return map;
}
public  static void main( String[] args ) throws Exception
{

	HttpServletRequest request =null;
	PFcardBulkMail aa =new PFcardBulkMail();
	aa.PFcardBulkMail("/reportservlet?method=SumofSuppPCReport&frmName=SumofSuppPCReport&employeeNo=4245&reportType=html","D:/kk.pdf/",request,"2255.pdf",null,"","","","","","","");
}

}