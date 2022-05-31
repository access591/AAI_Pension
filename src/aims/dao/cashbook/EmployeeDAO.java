package aims.dao.cashbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aims.bean.EmployeePersonalInfo;
import aims.bean.cashbook.BankMasterInfo;
import aims.bean.cashbook.BankOpenBalInfo;
import aims.common.DBUtils;
import aims.common.Log;

public class EmployeeDAO {

	Log log = new Log(EmployeeDAO.class);
	
	public List getEmployeeList(EmployeePersonalInfo info) throws Exception {
		log.info("EmployeeDAO : getEmployeeList : Entering method");
		
		List empInfo = new ArrayList();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
        
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(selectQuery);
			pst.setString(1,info.getRegion()+"%");
			pst.setString(2,"%"+info.getEmployeeName()+"%");
            rs = pst.executeQuery();
            
			while (rs.next()) {
				info = new EmployeePersonalInfo();
				if(rs.getString("EMPLOYEENAME")!=null){
				info.setEmployeeName(rs.getString("EMPLOYEENAME"));
				}
				if(rs.getString("PENSIONNO")!=null){
				info.setPensionNo(rs.getString("PENSIONNO"));
				}
				if(rs.getString("DESEGNATION")!=null){
				info.setDesignation(rs.getString("DESEGNATION"));
				}
				if(rs.getString("EMPLOYEENO")!=null){
				info.setEmployeeNumber(rs.getString("EMPLOYEENO"));
				}
				empInfo.add(info);
			}            
		} catch (SQLException e) {
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				rs.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		log.info("EmployeeDAO : getEmployeeList : leaving method");
		return empInfo;
	}
	
	String selectQuery = "select * from EMPLOYEE_PERSONAL_INFO where Upper(REGION) like Upper(?) and Upper(EMPLOYEENAME) like upper(?) ";
}
