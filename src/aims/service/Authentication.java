package aims.service;

import javax.mail.*;  
public class Authentication extends javax.mail.Authenticator
{
	public String username;
	public String password;

    public Authentication()
	{

	}

	public Authentication(String uname,String pwd)
	{
		System.out.println("uname:"+uname+"pwd:"+pwd);
		username=uname;
		password=pwd;
	}


public PasswordAuthentication getPasswordAuthentication()
{ 
return new PasswordAuthentication(username,password);
}
}
