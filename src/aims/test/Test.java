package aims.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import aims.common.CommonUtil;

import aims.common.DBUtils;
import aims.dao.FinancialReportDAO;
import aims.dao.ImportDao;
import aims.security.DESCryptographer;
import aims.dao.CommonDAO;
/**This class is testing purpose */
public  class Test {
	//static  Fibano f;
	public interface add{
		class wewe{
			
		}
		
	}
	////reportService.insertCLdata(personalInfo.getPensionNo(),dispYear,finalEmpNetOB,finalAaiNetOB,remaininInt,remainAaiInt);
 	//reportService.insertCLB(personalInfo.getPensionNo(),dispYear,df1.format(Double.parseDouble(df1.format(finalPrincipalOB)) - aprPrincipal),df1.format(Double.parseDouble(df1.format(netcloseEmpNet+remaininInt))+aprEmpNet),df1.format(Double.parseDouble(df1.format(netcloseNetAmount+remainAaiInt)) + aprAaiNet),df1.format(Double.parseDouble(df1.format(finalPensionOB))+ aprpc));
	
	public static void main(String[]s) throws FileNotFoundException{
		Test t=new Test();
		t.compare();
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String no="pp";
		//System.out.println(Integer.parseInt(no));
		//Object obj=t.getObject();
		DESCryptographer deCryp = new DESCryptographer();
		CommonUtil c = new CommonUtil();
		CommonDAO cdao =new CommonDAO();
		FinancialReportDAO fo= new FinancialReportDAO();
		ImportDao idao= new ImportDao();
		System.out.println("addition:"+idao.pensionAdditionalFormsCalculation("01-Jun-2015", "16000", "A", "South Region", "1", ""));
		System.out.println("Flag::::::::::::"+c.compareTwoDates("Sep-2012","Jan-2013"));
		//System.out.println(c.getDateDifference("01-Aug-2013","02-Aug-2013"));
		//System.out.println("months:::::::::"+cdao.getMonths(con,"28-Feb-2014","31-Aug-2014"));
		//System.out.println("months:::::::::"+cdao.compareFinalSettlementDates("01-Apr-2013","31-Mar-2014","30-Apr-2012"));
		int n=300;
		byte b=(byte)n;
		String soap="Rex@Ren";
		//System.out.println("soapsoapsoapsoapsoapsoapsoap"+soap.contains("@"));
		//System.out.println(b);
		String y="01-Apr-2013";
		String calEmoluments="";
		char[] chararry=y.toCharArray();
		System.out.println(y.substring(3));
		System.out.println(y.substring(y.length()-4));
		System.out.println(y.substring(y.length()-4, y.length()));
		if(String.valueOf('l').equalsIgnoreCase(String.valueOf('L'))){
			//System.out.println("it's working..!");
		}
		
		//System.out.println(c.getDateDifference("01-Apr-2010",
				//"01-Apr-2013"));
		
		for(int i=0;i<=chararry.length;i++){
			//System.out.println((int)chararry[i]);
			
		}
	
		//System.out.println("dddddddddddd:"+y.charAt(3));
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		date = sdf.format(new Date());
		//System.out.println(date);
		
		//System.out.println(c.GetWords(214748364));
		 
	    String bb ="'A','B'";
	
		
	  String newString =bb.replaceAll("\'", "\"");
		System.out.println(newString);
		
	
		
		Random rnd = new Random(); 
		int ran = 100000 + rnd. nextInt(900000);
		
		System.out.println("random:"+ran);
	
		try {
			//fo.runPFCardScheduler();
			//fo.PFCardProcess("616",true);
		/*	boolean be=c.compareTwoDates("Mar-2011", c
					.converDBToAppFormat("01-Apr-2013", "dd-MMM-yyyy",
							"MMM-yyyy"));*/
			//System.out.println("prasad:::::::"+be);
		
			con = DBUtils.getConnection();
			//con.setAutoCommit(false);
			//System.out.println("ooooooooooooo0000000ooooo"+c.compareTwoDatesUsingDB(con,"02-Apr-2014","01-Apr-2013"));
			
			st = con.createStatement();
			//rs=st.executeQuery("select pensionno,wetheroption from employee_personal_info where pensionno in(286,9512,1420,21612,9805,8253,6809,17711,3720,13990,5109,11748,8227,8818,22480,11937,11993,12677,5630,13705,18590,18735,6876,4680,6298,6753)");
			
			//System.out.println("rs;;;;;;;;;;;;;;;;"+rs.getString("pensionno")+rs.getString("wetheroption"));
			
			/*while(rs.next()){
				//System.out.println("rsop");
				//System.out.println("rs"+rs.getString("pensionno")+rs.getString("wetheroption"));
				//if(rs.getString("wetheroption").trim().equals("A")){
					//System.out.println("weatheoption=========="+rs.getString("wetheroption").trim());
				//}
			}*/
			
		/*	calEmoluments = fo.calWages("5780",
					"01-Jun-1995", "A",
					false, false, "1");*/
			//System.out.println("navagartala======="+deCryp.doEncrypt("agartala@1"));
			//System.out.println("navagatti======="+deCryp.doDecrypt(deCryp.doEncrypt("agatti@2")));
			
			//System.out.println("password"+deCryp.doDecrypt("gy47wrdBcC1+QigidzZmwA==") );
			//System.out.println("rssssssssssssssssssyesssss"+calEmoluments);
			
			
			rs=st.executeQuery("select username,password,airportcode,region from employee_user where "+
					"usertype!='Employee'");
			FileWriter fw = new FileWriter(new File("D://prasad.txt"));
			String user="";
			//rs=st.executeQuery("select pensionno,password,nvl(to_char(dateofjoining,'ddmmyyyy'),'01011900') as doj,airportcode,region from employee_personal_info where pensionno=10311 order by pensionno");
			while(rs.next()){
				
			/*	System.out.print(rs.getString("pensionno")+" "+deCryp.doDecrypt(rs.getString("password"))+
						" "+rs.getString("airportcode")+" "+rs.getString("region")+
						System.getProperty("line.separator"));
				
				user=rs.getString("pensionno")+"\t "+deCryp.doDecrypt(rs.getString("doj"))+
				" \t"+rs.getString("airportcode")+" \t"+rs.getString("region")+
				System.getProperty("line.separator")*/;
				System.out.print("UserName/Password : "+rs.getString("username")+"/"+deCryp.doDecrypt(rs.getString("password"))+
						"/"+rs.getString("airportcode")+rs.getString("region")+
						System.getProperty("line.separator"));
				user=rs.getString("username")+"\t "+deCryp.doDecrypt(rs.getString("password"))+
				" \t"+rs.getString("airportcode")+" \t"+rs.getString("region")+
				System.getProperty("line.separator");
				System.out.println("4959/"+deCryp.doDecrypt("AtD7nQTDrXc="));
				//System.out.println("21882"+deCryp.doEncrypt("14072008"));
				//System.out.println("23932"+deCryp.doEncrypt("18012011"));
				//System.out.println("26233"+deCryp.doEncrypt("05092016"));
		fw.write(user);
				user="";
			}
			
			fw.flush();
			fw.close();
			//System.out.print("UserName/Password"+deCryp.doDecrypt("q+TThD7wE4Z+QigidzZmwA=="));
		}catch(Exception e){
			e.printStackTrace();
		}
		/*Scanner q =new Scanner(new File("D:/Prasad.txt")); 
		 //System.out.println(q.);
		 //System.out.println(q.toString());
		while(q.hasNextLine())
			System.out.println(q.nextLine());*/
		/*Date d = new Date();
		System.out.print("d:"+d.clone());*/
		
		//System.out.println("Prasad "+c.compareTwoDates("Mar-2008","Mar-2008"));
		char[] helloArray = { 'h', 'e', 'l', 'l', 'o', '.'};
	      String helloString = new String(helloArray);  
	     // System.out.println( helloString.intern() );

	
		try{
		//Test.f.mmm("Prasad is a good Boy");
		
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	public void insertCLdata(int penno,String dispYear,double finalEmpNetOB,double finalAaiNetOB,double remaininInt,double remainAaiInt){
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		boolean flag=false;
		try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs=st.executeQuery("select 'X' as flag from Finalsettlement_data where pensionno="+penno+" and dispyear='"+dispYear+"'");
		if(rs.next()){
			flag=true;
		}
		if(flag){
		st.executeQuery("update Finalsettlement_data set empsub="+finalEmpNetOB+",aainet="+finalAaiNetOB+"	where pensionno="+penno+" and dispyear='"+dispYear+"'");
		}else{
		st.executeQuery("insert into Finalsettlement_data values("+penno+",'"+dispYear+"',"+finalEmpNetOB+","+finalAaiNetOB+")");
		}}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	public void insertCLB(int pensionno,String finYear,double rateOfInterest,double finalEmpNetOBIntrst,double finalAaiNetOBIntrst,double e,double a){
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try{
		con = DBUtils.getConnection(); 
		st = con.createStatement();
		st.executeQuery("insert into one values('"+pensionno+"','"+finYear+"','"+rateOfInterest+"','"+finalEmpNetOBIntrst+"','"+finalAaiNetOBIntrst+"','"+e+"','"+a+"')");
		}catch(Exception x){
			x.printStackTrace();
			
		}
	}

	public void compare(){
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		String var="";
		try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		rs=st.executeQuery("select * from epis_freeze_year_details order by rowid");
		if(rs.next()){
			var=rs.getString(4);
		}
		while(rs.next()){
			if(var.equals(rs.getString(4))){
				System.out.println("prasad");
			}else{
				System.out.println(var+":::"+rs.getString(4));
			}
			var=rs.getString(4);
		}
		}catch(Exception x){
			x.printStackTrace();
			
		}
	}
	public void insertPCdata(int penno,String year, double emoluements,double pca,double emoluements_b,double pcb,double diff_pc,double int_pcdiff) throws SQLException{
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		
		  // create table  employee_freshop_ATOBrev(Pensionno number(5),Year varchar2(12),Emoluments number(15,2),Pc_a number(15,2),Emoluments_b number(15,2),Pc_b number(15,2),Diff_Pc number(15,2),Int_Diff number(15,2));
		//st.executeQuery("insert into EMPLOYEE_FRESHOP_PC values("+penno+",'"+fpc+"',"+fint+","+cpc+","+cint+")");
		st.executeQuery("insert into employee_freshop_ATOBrev(Pensionno,Year,Emoluments,Pc_a,Emoluments_b,Pc_b,Diff_Pc,Int_Diff) " +
				"values("+penno+",'"+year+"',"+emoluements+","+pca+",'"+emoluements_b+"',"+pcb+","+diff_pc+","+int_pcdiff+")");
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			st.close();
			con.close();
		}
	}
	public void insertPCtotaldata(int pensionno,double pensiona,double pensiona_int,double pensionb,double pensionb_int,double pen_diff,double pen_diffint) throws SQLException{
		Connection con = null;
		ResultSet rs = null;
		Statement st = null;
		try{
		con = DBUtils.getConnection();
		st = con.createStatement();
		//st.executeQuery("insert into EMPLOYEE_FRESHOP_PC values("+penno+",'"+fpc+"',"+fint+","+cpc+","+cint+")");
		st.executeQuery("insert into EMPLOYEE_FRESHOP_PC " +
				"values("+pensionno+","+pensiona+","+pensiona_int+","+pensionb+","+pensionb_int+","+pen_diff+","+pen_diffint+")");
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			st.close();
			con.close();
		}
	}
	
}


