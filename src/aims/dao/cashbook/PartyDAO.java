package aims.dao.cashbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aims.bean.cashbook.BankMasterInfo;
import aims.bean.cashbook.PartyInfo;
import aims.common.DBUtils;
import aims.common.Log;

public class PartyDAO {
	Log log = new Log(PartyDAO.class);

	public ArrayList getPartyList(PartyInfo info,String type) throws Exception {
		log.info("PartyDAO : getPartyList : Entering method");
		
		ArrayList partyInfo = new ArrayList();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
        
		try {
			con = DBUtils.getConnection();
            pst = con.prepareStatement(selectQuery);
            pst.setString(1,info.getPartyName()+("edit".equals(type)?"":"%"));
            rs = pst.executeQuery();
            
			while (rs.next()) {
				info = new PartyInfo();
				info.setPartyName(rs.getString("PARTYNAME"));
				info.setPartyDetail(rs.getString("PARTYDETAIL"));
				info.setMobileNo(rs.getString("MOBILENO"));
				info.setContactNo(rs.getString("CONTACTNO"));
				info.setFaxNo(rs.getString("FAXNO"));
				info.setEmailId(rs.getString("EMAIL"));
				BankMasterInfo bInfo = new BankMasterInfo();
				bInfo.setBankName(rs.getString("BANKNAME"));
				bInfo.setAccountNo(rs.getString("ACCOUNTNO"));
				bInfo.setBankCode(rs.getString("BANKCODE"));
				bInfo.setIFSCCode(rs.getString("IFSCCODE"));
				bInfo.setAccountCode(rs.getString("ACCOUNTCODE"));
				bInfo.setBranchName(rs.getString("BRANCHNAME"));
				bInfo.setAddress(rs.getString("ADDRESS"));
				bInfo.setPhoneNo(rs.getString("PHONENO"));
				bInfo.setFaxNo(rs.getString("BFAXNO"));
				bInfo.setAccountType(rs.getString("ACCOUNTTYPE"));
				bInfo.setNEFTRTGSCode(rs.getString("NEFT_RTGSCODE"));
				bInfo.setMICRNo(rs.getString("MICRNO"));
				bInfo.setContactPerson(rs.getString("CONTACTPERSON"));
				bInfo.setMobileNo(rs.getString("BMOBILENO"));
				info.setBankInfo(bInfo);
				partyInfo.add(info);
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
		log.info("PartyDAO :getPartyList() leaving method");
		return partyInfo;
	}
	
	public boolean exists(PartyInfo info) throws Exception {
		log.info("PartyDAO : exists : Entering method");
		boolean exists = false;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;        
		try {
			con = DBUtils.getConnection();
            pst = con.prepareStatement(countQuery);
            pst.setString(1,info.getPartyName());
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
				rs.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		log.info("PartyDAO : exists : Leaving method");
		return exists;
	}

	public void addPartyRecord(PartyInfo info) throws Exception {
		log.info("PartyDAO : addPartyRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;   
		try {
			con = DBUtils.getConnection();
            pst = con.prepareStatement(insertQuery);
            pst.setString(1,info.getPartyName());
            pst.setString(2,info.getPartyDetail());
            pst.setString(3,info.getFaxNo());
            pst.setString(4,info.getEmailId());
            pst.setString(5,info.getMobileNo());
            pst.setString(6,info.getEnteredBy());
            BankMasterInfo bInfo = info.getBankInfo();
            pst.setString(7,bInfo.getBankName());
            pst.setString(8,bInfo.getBranchName());
            pst.setString(9,bInfo.getBankCode());
            pst.setString(10,bInfo.getAddress());            
            pst.setString(11,bInfo.getPhoneNo());
            pst.setString(12,bInfo.getFaxNo());
            pst.setString(13,bInfo.getAccountCode());
            pst.setString(14,bInfo.getAccountNo());
            pst.setString(15,bInfo.getAccountType());
            pst.setString(16,bInfo.getIFSCCode());
            pst.setString(17,bInfo.getNEFTRTGSCode());
            pst.setString(18,bInfo.getMICRNo());
            pst.setString(19,bInfo.getContactPerson());
            pst.setString(20,bInfo.getMobileNo());
            pst.setString(21,info.getContactNo());
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
		log.info("PartyDAO : addPartyRecord : Leaving method");
	}
	public void updatePartyRecord(PartyInfo info) throws Exception {
		 log.info("PartyDAO : updatePartyRecord : Entering method");
	 		Connection con = null;
	 		PreparedStatement pst = null;
	 		try {
	 			con = DBUtils.getConnection();
	 			pst = con.prepareStatement(updateQuery);
	 			 pst.setString(1,info.getPartyDetail());
	             pst.setString(2,info.getFaxNo());	            
	             pst.setString(3,info.getMobileNo());
	             pst.setString(4,info.getEmailId());	             
	             BankMasterInfo bInfo = info.getBankInfo();
	             pst.setString(5,bInfo.getBankName());
	             pst.setString(6,bInfo.getBranchName());
	             pst.setString(7,bInfo.getBankCode());
	             pst.setString(8,bInfo.getAddress());            
	             pst.setString(9,bInfo.getPhoneNo());
	             pst.setString(10,bInfo.getFaxNo());
	             pst.setString(11,bInfo.getAccountCode());
	             pst.setString(12,bInfo.getAccountNo());
	             pst.setString(13,bInfo.getAccountType());
	             pst.setString(14,bInfo.getIFSCCode());
	             pst.setString(15,bInfo.getNEFTRTGSCode());
	             pst.setString(16,bInfo.getMICRNo());
	             pst.setString(17,bInfo.getContactPerson());
	             pst.setString(18,bInfo.getMobileNo());
	             pst.setString(19,info.getContactNo());
	             pst.setString(20,info.getPartyName());
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
	 		log.info("PartyDAO : updatePartyRecord : leaving method");	
			
	}

	public void deletePartyRecord(String codes) throws Exception {
		 log.info("PartyDAO : deletePartyRecord : Entering method");
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
	  		log.info("PartyDAO : deletePartyRecord : leaving method");	
	 		
	}

	
	
	String selectQuery = "select PARTYNAME,Nvl(PARTYDETAIL,' ') PARTYDETAIL,Nvl(CONTACTNO,' ') CONTACTNO,Nvl(FAXNO,' ') FAXNO,Nvl(EMAIL,' ') EMAIL,Nvl(MOBILENO,' ') MOBILENO,Nvl(ENTEREDBY,' ') ENTEREDBY,Nvl(BANKNAME,' ') BANKNAME,Nvl(BRANCHNAME,' ') BRANCHNAME,Nvl(BANKCODE,' ') BANKCODE,Nvl(ADDRESS,' ') ADDRESS,Nvl(PHONENO,' ') PHONENO,Nvl(BFAXNO,' ') BFAXNO,Nvl(ACCOUNTCODE,' ') ACCOUNTCODE,Nvl(ACCOUNTNO,' ') ACCOUNTNO,Nvl(ACCOUNTTYPE,' ') ACCOUNTTYPE,Nvl(IFSCCODE,' ') IFSCCODE,Nvl(NEFT_RTGSCODE,' ') NEFT_RTGSCODE,Nvl(MICRNO,' ') MICRNO,Nvl(CONTACTPERSON,' ') CONTACTPERSON,Nvl(BMOBILENO,' ') BMOBILENO from Party_info where Upper(PARTYNAME) like Upper(?)";
	String countQuery  = "select count(*) from Party_info where Upper(PARTYNAME) = ?";
	String insertQuery = "insert into Party_info (PARTYNAME,PARTYDETAIL,FAXNO,EMAIL,MOBILENO,ENTEREDBY,BANKNAME,BRANCHNAME,BANKCODE,ADDRESS,PHONENO,BFAXNO,ACCOUNTCODE,ACCOUNTNO,ACCOUNTTYPE,IFSCCODE,NEFT_RTGSCODE,MICRNO,CONTACTPERSON,BMOBILENO,CONTACTNO) values (upper(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    String updateQuery = "update Party_info set PARTYDETAIL=?,FAXNO=?,MOBILENO=?,EMAIL=?,BANKNAME=?,BRANCHNAME=?,BANKCODE=?,ADDRESS=?,PHONENO=?,BFAXNO=?,ACCOUNTCODE=?,ACCOUNTNO=?,ACCOUNTTYPE=?,IFSCCODE=?,NEFT_RTGSCODE=?,MICRNO=?,CONTACTPERSON=?,BMOBILENO=?,CONTACTNO=? where PARTYNAME=? ";
    String deleteQuery = "Delete from Party_info  Where INSTR(upper(?),upper(PARTYNAME)) > 0";
	
}
