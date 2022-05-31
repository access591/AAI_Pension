package aims.service;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.*;
import javax.mail.internet.*;



public class MailService {

	public String sendMailString(String mailid, int pass) throws Exception {

		ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.Mail");

		System.out.println(bundle.getString("host"));

		String host = bundle.getString("host");
		String authid = bundle.getString("authid");
		String authpwd = bundle.getString("authpwd");
		String port = bundle.getString("port");

		Properties prop1 = new Properties();

		prop1.put("mail.smtp.host", host);
		prop1.put("mail.smtp.port", port);

		prop1.put("mail.smtp.authid", "true");

		Authenticator auth = new Authentication(authid, authpwd);

		Session session1 = Session.getInstance(prop1, auth);

		MimeMessage message = new MimeMessage(session1);

		BodyPart messageBodyPart4 = new MimeBodyPart();

		String maildetails = "";

		String senderId = "";

		try {

			maildetails += ";";

			Multipart multipart = new MimeMultipart();
			String subject = "EPIS Password Reset- mail";

			String htmlText = "";

			htmlText += "<BR>Dear Sir/Madam,<BR>";
			htmlText += "<BR>&nbsp;&nbsp;     Please use the password as '"+pass+"'<BR>";
			htmlText += "<BR><BR>";
			
			htmlText += "<BR><BR>";
			htmlText += "<BR><BR>***Automatic Mail Generation from EPIS Portal<BR><BR>";

			if (senderId.equals(""))
				senderId = "aims@aai.aero";

			InternetAddress add = new InternetAddress(mailid,
					senderId);
			message.setFrom(add);

			//String toAddress = bundle.getString("mail.smtp.mailidto");
			InternetAddress addressTo = new InternetAddress(mailid);

			message.setRecipient(Message.RecipientType.TO, addressTo);
			message.setSubject(subject);
			message.setSentDate(new java.util.Date());

			messageBodyPart4.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart4);

			message.setContent(multipart);

			Transport.send(message);

			maildetails = "Y";

		} catch (MessagingException e) {
			maildetails = "N";
			System.err.println("Cant send mail. " + e.getMessage());
		}

		return maildetails;

	}

}
