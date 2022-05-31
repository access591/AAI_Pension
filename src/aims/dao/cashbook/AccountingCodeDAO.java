package aims.dao.cashbook;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aims.bean.SearchInfo;
import aims.bean.cashbook.AccountingCodeInfo;
import aims.bean.cashbook.BankMasterInfo;
import aims.common.CommonUtil;
import aims.common.DBUtils;
import aims.common.Log;

public class AccountingCodeDAO {

	Log log = new Log(AccountingCodeDAO.class);

	public void addAccountRecord(AccountingCodeInfo info) throws Exception {
		log.info("AccountingCodeDAO : addAccountRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(insertQuery);
            pst.setString(1,info.getAccountHead());
            pst.setString(2,info.getParticular());
            pst.setString(3,info.getType());
            pst.setString(4,info.getEnteredBy());
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
		log.info("AccountingCodeDAO : addAccountRecord : leaving method");		
	}
	
	public List getAccountList(AccountingCodeInfo info,String type) throws Exception {
		log.info("AccountingCodeDAO : getAccountList : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs= null;
		List accInfo = new ArrayList();
		try {
			con = DBUtils.getConnection();
			if("rem".equals(type)){
				pst = con.prepareStatement(remQuery);
				pst.setString(1,(info.getAccountHead()!=null?info.getAccountHead():""));
			}else if("edit".equals(type)){
				pst = con.prepareStatement(editQuery);				
	            pst.setString(1,(info.getAccountHead()!=null?info.getAccountHead():""));
			}else{
				pst = con.prepareStatement(selectQuery);
	            pst.setString(1,(info.getAccountHead()!=null?info.getAccountHead():"")+"%");
			}
            pst.setString(2,(info.getParticular()!=null?info.getParticular():"")+"%");
			pst.setString(3,(info.getType()!=null?info.getType():"")+"%");
            rs = pst.executeQuery();    
            while (rs.next()) {
				info = new AccountingCodeInfo();
				info.setAccountHead(rs.getString("ACCOUNTHEAD"));
				info.setParticular(rs.getString("PARTICULAR"));
				info.setType(rs.getString("TYPE"));
				accInfo.add(info);
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
		log.info("AccountingCodeDAO : getAccountList : leaving method");
		return accInfo;
	}
	
	public boolean exists(AccountingCodeInfo info) throws Exception {
		log.info("AccountingCodeDAO : exists : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs= null;
		boolean exists = false; 
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(countQuery);
            pst.setString(1,info.getAccountHead());
            rs = pst.executeQuery();    
            if (rs.next() && rs.getInt(1)>0) {
            	exists = true;
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
		log.info("AccountingCodeDAO : exists : leaving method");
		return exists;
	}

     public void updateAccountRecord(AccountingCodeInfo info) throws Exception {
    	 log.info("AccountingCodeDAO : updateAccountRecord : Entering method");
 		Connection con = null;
 		PreparedStatement pst = null;
 		try {
 			con = DBUtils.getConnection();
 			pst = con.prepareStatement(updateQuery);
             pst.setString(1,info.getParticular());
             pst.setString(2,info.getType());
             pst.setString(3,info.getAccountHead());
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
 		log.info("AccountingCodeDAO : updateAccountRecord : leaving method");	
		
	}
     public void deleteAccountRecord(String codes) throws Exception {
    	 log.info("AccountingCodeDAO : deleteAccountRecord : Entering method");
  		Connection con = null;
  		PreparedStatement pst = null;
  		try {
  			con = DBUtils.getConnection();
  			pst = con.prepareStatement(deleteQuery);
              pst.setString(1,codes);
              pst.executeUpdate();       
  		} catch (SQLException e) {
  			throw e;
  		} catch (Exception e) {	
  			throw e;
  		} finally {
  			try {
  				pst.close();
  				con.close();
  			} catch (SQLException e) {
  				log.printStackTrace(e);
  			}
  		}
  		log.info("AccountingCodeDAO : deleteAccountRecord : leaving method");	
 		
 		
 	}

	String insertQuery = "insert into ACCOUNTCODE_INFO(ACCOUNTHEAD,PARTICULAR,TYPE,ENTEREDBY) values(?,?,?,?)" ;
	String selectQuery = "select ACCOUNTHEAD,PARTICULAR,DECODE(TYPE,'A','Asset','L','Liability','I','Income','E','Expenditure','') TYPE from ACCOUNTCODE_INFO where ACCOUNTHEAD like ? and PARTICULAR like ? and Nvl(TYPE,' ') like ?  " ;
	String editQuery = "select ACCOUNTHEAD,PARTICULAR,nvl(TYPE,' ')TYPE from ACCOUNTCODE_INFO where ACCOUNTHEAD = ? and PARTICULAR like ? and Nvl(TYPE,' ') like ?  " ;
	String updateQuery = "Update ACCOUNTCODE_INFO set PARTICULAR=?,TYPE=? Where ACCOUNTHEAD=?";
	String deleteQuery = "Delete from ACCOUNTCODE_INFO  Where INSTR(upper(?),upper(ACCOUNTHEAD)) > 0";
	String remQuery = "select ACCOUNTHEAD,PARTICULAR,DECODE(TYPE,'A','Asset','L','Liability','I','Income','E','Expenditure','') TYPE from ACCOUNTCODE_INFO where INSTR(upper(?),upper(ACCOUNTHEAD)) = 0 and PARTICULAR like ? and Nvl(TYPE,' ') like ? " ;
	String countQuery = "select count(*) from ACCOUNTCODE_INFO where ACCOUNTHEAD = ?" ;


	
	
	
}
