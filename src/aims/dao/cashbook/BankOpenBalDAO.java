package aims.dao.cashbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aims.bean.cashbook.BankOpenBalInfo;
import aims.common.DBUtils;
import aims.common.Log;

public class BankOpenBalDAO {

	Log log = new Log(BankOpenBalDAO.class);
	BankOpenBalInfo info = new BankOpenBalInfo();
	public void addOpenBalRecord(BankOpenBalInfo info) throws Exception {
		log.info("BankOpenBalDAO : addOpenBalRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(insertQuery);
            pst.setString(1,info.getAccountNo());
            pst.setString(2,info.getOpendate());
            pst.setDouble(3,info.getAmount());
            pst.setString(4,info.getDetails());
            pst.setString(5,info.getEnteredBy());
            pst.executeUpdate();
       
		} catch (SQLException e) {
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		log.info("BankOpenBalDAO : addOpenBalRecord : Leaving method");
	}
	
	public List searchRecords(BankOpenBalInfo info) throws Exception {
		log.info("BankOpenBalDAO : searchRecords : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List dataList = new ArrayList();

		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(selectQuery);
            pst.setString(1,info.getBankName()+"%");
            pst.setString(2,info.getAccountNo()+"%");
            rs = pst.executeQuery();
            while(rs.next()){
            	info = new BankOpenBalInfo();
            	info.setAccountNo(rs.getString("ACCOUNTNO"));
            	info.setBankName(rs.getString("bankName"));
            	info.setDetails(rs.getString("DETAILS"));
            	info.setOpendate(rs.getString("OPENEDDATE"));
            	info.setAmount(rs.getDouble("AMOUNT"));    
            	dataList.add(info);
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
		log.info("BankOpenBalDAO : searchRecords : Leaving method");
		return dataList;
	}
	
	public void deleteBankOpenBalRecord(BankOpenBalInfo info) throws Exception {
		log.info("BankOpenBalDAO : deleteBankOpenBalRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(deleteQuery);
            pst.setString(1,info.getAccountNo());
            pst.executeUpdate();       
		} catch (SQLException e) {
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}		
		log.info("BankOpenBalDAO : deleteBankOpenBalRecord : Leaving method");
	}
	public BankOpenBalInfo getRecord(BankOpenBalInfo info) throws Exception {
		log.info("BankOpenBalDAO : getRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(editQuery);
            pst.setString(1,info.getAccountNo());
            rs = pst.executeQuery();
            if(rs.next()){
            	info = new BankOpenBalInfo();
            	info.setAccountNo(rs.getString("ACCOUNTNO"));
            	info.setBankName(rs.getString("bankName"));
            	info.setDetails(rs.getString("DETAILS"));
            	info.setOpendate(rs.getString("OPENEDDATE"));
            	info.setAmount(rs.getDouble("AMOUNT"));                	
            }
		} catch (SQLException e) {
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}		
		log.info("BankOpenBalDAO : getRecord : Leaving method");
		return info;
	}
	
	public void updateOpenBalRecord(BankOpenBalInfo info) throws Exception {
		log.info("BankOpenBalDAO : updateOpenBalRecord : Entering method");		
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(updateQuery);     
			
            pst.setString(1,info.getOpendate());
            pst.setDouble(2,info.getAmount());
            pst.setString(3,info.getDetails());
            pst.setString(4,info.getEnteredBy());
            pst.setString(5,info.getAccountNo());
            pst.executeUpdate();       
		} catch (SQLException e) {
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		log.info("BankOpenBalDAO : updateOpenBalRecord : Leaving method");		
	}
	String insertQuery 	= " insert into BANKOPENINGBAL_INFO(ACCOUNTNO,OPENEDDATE,AMOUNT,DETAILS,ENTEREDBY) values(?,?,?,?,?)" ;
	String deleteQuery 	= " delete from BANKOPENINGBAL_INFO where  instr(upper(?),upper(ACCOUNTNO))>0 " ;
	String selectQuery 	= " select info.ACCOUNTNO,bankName,Nvl(to_char(OPENEDDATE,'dd/Mon/yyyy'),' ')OPENEDDATE,AMOUNT,Nvl(DETAILS,' ')DETAILS from BANKOPENINGBAL_INFO info,bank_info bank where bank.ACCOUNTNO=info.ACCOUNTNO and upper(bankname) like upper(?) and upper(info.ACCOUNTNO) like upper(?) " ;
	String editQuery 	= " select info.ACCOUNTNO,bankName,Nvl(to_char(OPENEDDATE,'dd/Mon/yyyy'),' ')OPENEDDATE,AMOUNT,Nvl(DETAILS,' ')DETAILS from BANKOPENINGBAL_INFO info,bank_info bank where bank.ACCOUNTNO=info.ACCOUNTNO and info.ACCOUNTNO = ?" ;
	String updateQuery 	= " update BANKOPENINGBAL_INFO set OPENEDDATE = ? , AMOUNT = ? , DETAILS = ?,ENTEREDBY=?  where ACCOUNTNO = ?" ;
	
}
