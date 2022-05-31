package aims.test;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import aims.common.DBUtils;

public class Test1 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args)  {
		Connection con = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		Statement st = null;
		try{
		con = DBUtils.getConnection();
		FileWriter f=new FileWriter(new File("D:/Prasad.txt"));
	
		
		st = con.createStatement();
		for (int i = 25000; i < 27858; i++) {
			
		
		rs=st.executeQuery("select a.employeename,a.pensionno,round((select sum(emoluments)from employee_pension_validate where pensionno = a.pensionno),0) as emolum,to_char(a.dateofbirth,'dd-Mon-yyyy'),to_char(a.dateofjoining,'dd-Mon-yyyy'),(select r.aaipf from employee_req_data r where r.pensionno = a.pensionno) as aaipf, (select r.outstd from employee_req_data r where r.pensionno = a.pensionno) as outstand,(select max(s.advtransdate) from employee_pension_advances s where s.pensionno = a.pensionno) advdate,'adv' as type,(select r.gt from employee_req_data r where r.pensionno = a.pensionno) as netbal from (select * from employee_personal_info i where pensionno = "+i+") A ");
		while(rs.next()){
	//System.out.println(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getString(7)+"\t"+rs.getString(8)+"\t"+rs.getString(9)+"\t"+rs.getString(10));
	f.append(rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6)+"\t"+rs.getString(7)+"\t"+rs.getString(8)+"\t"+rs.getString(9)+"\t"+rs.getString(10)+"\n");		
		}
		
		rs=st.executeQuery("select s.pensionno, s.loandate, s.loanpurpose, s.sub_amt from employee_pension_loans s where pensionno = "+i);
		while(rs.next()){
			//System.out.println(" "+"\t"+rs.getString(1)+"\t"+" "+"\t"+" "+"\t"+" "+"\t"+" "+"\t"+rs.getString(4)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\n");
			f.append(" "+"\t"+rs.getString(1)+"\t"+" "+"\t"+" "+"\t"+" "+"\t"+" "+"\t"+rs.getString(4)+"\t"+rs.getString(2)+"\t"+rs.getString(3)+"\n");		
				}
		}
		f.flush();
		f.close();
	}catch(Exception e){
		e.printStackTrace();
	}
	}

}
