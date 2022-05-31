package aims.dao.cashbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aims.bean.cashbook.BankBook;
import aims.bean.cashbook.VoucherDetails;
import aims.bean.cashbook.VoucherInfo;
import aims.common.DBUtils;
import aims.common.Log;

public class VoucherDAO {

	Log log = new Log(VoucherDAO.class);

	DBUtils commonDB = new DBUtils();

	public void addVoucherRecord(VoucherInfo info) throws Exception {
		log.info("VoucherDAO : addVoucherRecord() entering method");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = commonDB.getConnection();
			pst = conn.prepareStatement(keyNoGenQuery);		
			pst.setString(1, info.getPreparedDt());
			pst.setString(2, info.getPreparedDt());	
			rs = pst.executeQuery();
			while(rs.next()){
				info.setKeyNo(rs.getString(1));
			}
		} catch (Exception e) {
			log.info("VoucherDAO : Exception : " + e.toString());
			throw e;

		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(insertQuery);			
			pst.setString(1, info.getKeyNo());
			log.info("key :" + info.getKeyNo());
			pst.setString(2, info.getAccountNo());
			log.info("accno :" +info.getAccountNo() );
			pst.setString(3, info.getFinYear());	
			log.info("finyear :"+info.getFinYear() );
			pst.setString(4, info.getTrustType());	
			log.info("trust type :"+info.getTrustType() );
			pst.setString(5, info.getVoucherType());	
			log.info("getVoucherType :"+info.getVoucherType() );
			pst.setString(6, info.getPartyType());	
			log.info("getPartyType :"+info.getPartyType() );
			pst.setString(7, info.getEmpPartyCode());	
			log.info("getEmpPartyCode :"+info.getEmpPartyCode() );
			pst.setString(8, info.getPreparedBy());	
			log.info("getPreparedBy :"+info.getPreparedBy() );
			pst.setString(9, info.getDetails());	
			log.info("getDetails :"+info.getDetails() );
			pst.setString(10, info.getPreparedDt());	
			pst.executeUpdate();
				
			
		} catch (Exception e) {
			conn.rollback();
			log.info("VoucherDAO : Exception : " + e.toString());
			throw e;

		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			pst = conn.prepareStatement(insertDtQuery);
			List voucherDts = info.getVoucherDetails();
			int length = voucherDts.size();
			for (int i = 0; i < length; i++) {
				VoucherDetails voucherDt = (VoucherDetails) voucherDts.get(i);
				pst.setString(1, info.getKeyNo());
				pst.setString(2, voucherDt.getAccountHead());
				pst.setString(3, voucherDt.getMonthYear());
				pst.setDouble(4, voucherDt.getDebit());
				pst.setString(5, voucherDt.getDetails());
				pst.setDouble(6, voucherDt.getCredit());
				pst.setString(7, voucherDt.getChequeNo());				
				pst.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			log.info("VoucherDAO : Exception : " + e.toString());
			throw e;

		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		log.info("VoucherDAO : addVoucherRecord() leaving method");
	}

	public List searchRecords(VoucherInfo info, String type) throws Exception {
		log.info("VoucherDAO : searchRecords : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List voucherInfo = new ArrayList();
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement("A".equals(type) ? selectAppQuery
					: selectQuery);
			if ("edit".equals(type)) {
				pst = con.prepareStatement(editQuery);
				pst.setString(1, (info.getKeyNo() != null ? info.getKeyNo()
						: ""));
			}
			pst.setString(1, info.getBankName() + "%");
			pst.setString(2, info.getFinYear() + "%");
			pst.setString(3, info.getVoucherType() + "%");
			pst.setString(4, info.getAccountNo() + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				info = new VoucherInfo();
				info.setKeyNo(rs.getString("KEYNO"));
				info.setBankName(rs.getString("BANKNAME"));
				info.setFinYear(rs.getString("FYEAR"));
				info.setVoucherType(rs.getString("VOUCHERTYPE"));
				info.setPartyType(rs.getString("PARTYTYPE"));
				info.setVoucherNo(rs.getString("voucherNo"));
				voucherInfo.add(info);
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
		log.info("VoucherDAO : searchRecords : leaving method");
		return voucherInfo;
	}

	public VoucherInfo getReport(VoucherInfo info) throws Exception {
		log.info("VoucherDAO : getReport : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = DBUtils.getConnection();
			log.info("VoucherDAO : getReport : Report Parent Table");
			pst = con.prepareStatement(reportQuery);			
			pst.setString(1, info.getKeyNo());
			rs = pst.executeQuery();
			if (rs.next()) {
				info = new VoucherInfo();
				info.setKeyNo(rs.getString("KEYNO"));
				info.setBankName(rs.getString("BANKNAME"));
				info.setAccountNo(rs.getString("accountNo"));
				info.setFinYear(rs.getString("FYEAR"));
				info.setVoucherType(rs.getString("VOUCHERTYPE"));
				info.setPartyType(rs.getString("PARTYTYPE"));
				info.setTrustType(rs.getString("trusttype"));
				info.setEmpPartyCode(rs.getString("emp_party_code"));
				info.setPartyDetails(rs.getString("partyDetails"));
				info.setVoucherNo(rs.getString("voucherno"));
				info.setCheckedBy(rs.getString("checkedby"));
				info.setApprovedBy(rs.getString("approvedby"));
				info.setDetails(rs.getString("details"));
				info.setVoucherDt(rs.getString("VOUCHER_DT"));
				info.setStatus(rs.getString("APPROVAL"));
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
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		try {
			log.info("VoucherDAO : getReport : Report Child Table");
			pst = con.prepareStatement(reportDtQuery);
			pst.setString(1, info.getKeyNo());
			rs = pst.executeQuery();
			List voucherDts = new ArrayList();
			VoucherDetails voucherDt = null;
			while (rs.next()) {
				voucherDt = new VoucherDetails();
				if(rs.getString("ACCOUNTHEAD")!=null){
				voucherDt.setAccountHead(rs.getString("ACCOUNTHEAD"));
				}
				if(rs.getString("PARTICULAR")!=null){
				voucherDt.setParticular(rs.getString("PARTICULAR"));
				}
				if(rs.getString("MONTH_YEAR")!=null){
				voucherDt.setMonthYear(rs.getString("MONTH_YEAR"));
				}
				if(rs.getString("details")!=null){
				voucherDt.setDetails(rs.getString("details"));
				}
				if(rs.getString("chequeno")!=null){
				voucherDt.setChequeNo(rs.getString("chequeno"));
				}
				if(rs.getString("debit")!=null){
				voucherDt.setDebit(Double.parseDouble(rs.getString("debit")));
				}
				if(rs.getString("credit")!=null){
				voucherDt.setCredit(Double.parseDouble(rs.getString("credit")));
				}
				voucherDts.add(voucherDt);
			}
			info.setVoucherDetails(voucherDts);
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
		log.info("VoucherDAO : getReport : leaving method");
		return info;
	}

	public void updateApprovalVoucher(VoucherInfo info) throws Exception {
		log.info("VoucherDAO : searchRecords : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(updateQuery);
			pst.setString(1, info.getVoucherDt());
			pst.setString(2, info.getCheckedBy());
			pst.setString(3, info.getStatus());
			pst.setString(4, info.getApprovedBy());
			pst.setString(5, getVoucherNo(info.getKeyNo()));
			pst.setString(6, info.getKeyNo());
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
	}

	public BankBook getBankBook(VoucherInfo info) throws Exception {
		log.info("VoucherDAO : getBankBook : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		BankBook book = new BankBook();
		BankBook bankBook = null;
		List bankBookList = new ArrayList();
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(bankOpenBalQuery);			
			pst.setString(1, info.getAccountNo());
			pst.setString(2, info.getAccountNo());
			pst.setString(3, info.getAccountNo());
			pst.setString(4, info.getAccountNo());
			pst.setString(5, info.getAccountNo());
			pst.setString(6, info.getAccountNo());
			pst.setString(7, info.getFromDate());

			rs = pst.executeQuery();

			if (rs.next()) {				
				book.setOpeningBalAmt(rs.getDouble(1));
				book.setBankName(rs.getString("bankname"));
				book.setAccountNo(info.getAccountNo());
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
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(bankBookQuery);
			pst.setString(1, info.getAccountNo());
			pst.setString(2, info.getAccountNo());
			pst.setString(3, info.getAccountNo());
			pst.setString(4, info.getAccountNo());
			pst.setString(5, info.getFromDate());
			pst.setString(6, info.getToDate());

			rs = pst.executeQuery();		
				
			while (rs.next()) {
				bankBook = new BankBook();
				bankBook.setAccountHead(rs.getString("accounthead"));
				bankBook.setVoucherno(rs.getString("voucherno"));
				bankBook.setParticular(rs.getString("particular"));
				bankBook.setPartyName(rs.getString("partyname"));					
				bankBook.setDescription(rs.getString("details"));
				bankBook.setPayments(rs.getDouble("debit"));
				bankBook.setReceipts(rs.getDouble("credit"));
				bankBook.setChequeNo(rs.getString("chequeno"));
				bankBookList.add(bankBook);
			} 
			book.setBankBookList(bankBookList);
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
		log.info("VoucherDAO : getBankBook : leaving method");
		return book;
	}

	public void updateVoucherRecord(VoucherInfo info) throws Exception {
		log.info("VoucherDAO : updateVoucherRecord : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(updateVoucherQry);
			pst.setString(1, info.getAccountNo());
			pst.setString(2, info.getFinYear());
			pst.setString(3, info.getTrustType());
			pst.setString(4, info.getPartyType());
			pst.setString(5, info.getEmpPartyCode());
			pst.setString(6, info.getPreparedBy());
			pst.setString(7, info.getDetails());
			pst.setString(8, info.getKeyNo());
			pst.executeUpdate();
		} catch (SQLException e) {
			con.rollback();
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			con.rollback();
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();

			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		try {
			pst = con.prepareStatement(deleteDtQuery);
			pst.setString(1, info.getKeyNo());
			pst.executeUpdate();
		} catch (SQLException e) {
			con.rollback();
			throw e;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			pst = con.prepareStatement(insertDtQuery);
			List voucherDts = info.getVoucherDetails();
			int length = voucherDts.size();

			for (int i = 0; i < length; i++) {
				VoucherDetails voucherDt = (VoucherDetails) voucherDts.get(i);
				pst.setString(1, info.getKeyNo());
				pst.setString(2, voucherDt.getAccountHead());
				pst.setString(3, voucherDt.getMonthYear());
				pst.setDouble(4, voucherDt.getCredit());
				pst.setString(5, voucherDt.getDetails());
				pst.setDouble(6, voucherDt.getDebit());	
				pst.setString(7, voucherDt.getChequeNo());	
				pst.executeUpdate();
			}
			con.commit();
		} catch (Exception e) {
			con.rollback();
			log.info("VoucherDAO : Exception : " + e.toString());
			throw e;

		} finally {
			try {
				pst.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		log.info("VoucherDAO : updateVoucherRecord : leaving method");

	}

	public String getVoucherNo(String keyno) throws Exception {
		log.info("VoucherDAO : getVoucherNo : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String voucherNo = null;
		try {
			con = DBUtils.getConnection();
			pst = con.prepareStatement(voucherNoQuery);
			pst.setString(1, keyno);
			rs = pst.executeQuery();
			if (rs.next()) {
				voucherNo = rs.getString(1);
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
		log.info("VoucherDAO : getVoucherNo : leaving method");
		return voucherNo;
	}
	
	public void deleteVoucher(String codes) throws Exception {
		log.info("VoucherDAO : deleteVoucher : Entering method");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBUtils.getConnection();
			con.setAutoCommit(false);
			pst = con.prepareStatement(deleteVDTQuery);
			pst.setString(1, codes);
			pst.executeUpdate();
		} catch (SQLException e) {
			con.rollback();
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			con.rollback();
			log.printStackTrace(e);
			throw e;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				log.printStackTrace(e);
			}
		}
		try {
			pst = con.prepareStatement(deleteVQuery);
			pst.setString(1, codes);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			con.rollback();
			log.printStackTrace(e);
			throw e;
		} catch (Exception e) {
			con.rollback();
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
		log.info("VoucherDAO : deleteVoucher : leaving method");
	}
	
	String insertQuery 	 	= "insert into VOUCHER_INFO(KEYNO,ACCOUNTNO,FYEAR,TRUSTTYPE,VOUCHERTYPE,PARTYTYPE,EMP_PARTY_CODE,PREPAREDBY,details,preperation_dt) values (?,?,?,?,?,?,?,?,?,?)";

	String insertDtQuery 	= "insert into VOUCHER_DETAILS(KEYNO,ACCOUNTHEAD,MONTH_YEAR,credit,Details,debit,chequeno) values (?,?,?,?,?,?,?)";

	String selectQuery 	 	= "select voucherNo,KEYNO,BANKNAME,FYEAR,Decode(VOUCHERTYPE,'P','Payment','R','Receipt','C','Contra','') VOUCHERTYPE,Decode(PARTYTYPE,'E','Employee','P','Party','B','Bank','') PARTYTYPE from  VOUCHER_INFO voucher,BANK_INFO bank where bank.ACCOUNTNO = voucher.ACCOUNTNO and upper(BANKNAME) like upper(?) and FYEAR like ? and VOUCHERTYPE like ? and upper(voucher.ACCOUNTNO) like upper(?)";

	String selectAppQuery 	= "select voucherNo,KEYNO,BANKNAME,FYEAR,Decode(VOUCHERTYPE,'P','Payment','R','Receipt','C','Contra','') VOUCHERTYPE,Decode(PARTYTYPE,'E','Employee','P','Party','B','Bank','') PARTYTYPE from  VOUCHER_INFO voucher,BANK_INFO bank where bank.ACCOUNTNO = voucher.ACCOUNTNO and BANKNAME like ? and FYEAR like ? and VOUCHERTYPE like ? and voucherno is null and upper(voucher.ACCOUNTNO) like upper(?)";

	String reportQuery 		= "SELECT Nvl(voucher.voucherNo,' ') voucherNo,voucher.accountNo accountNo,keyno, bankname, fyear,DECODE (vouchertype,'P', 'Payment','R', 'Receipt','C', 'Contra',' ') vouchertype,Nvl(partytype,' ') partytype,DECODE ( voucher.trusttype,'I', 'IAAI ECPF','N', 'NAA ECPF','A', 'AAI EPF',' ') trusttype,(CASE WHEN partytype = 'B' THEN  (SELECT bankname FROM bank_info WHERE accountno = emp_party_code) else emp_party_code  END ) emp_party_code,(CASE WHEN partytype = 'E'   THEN (SELECT employeename FROM employee_personal_info WHERE pensionno = emp_party_code) ELSE  emp_party_code  END ) partyDetails,(CASE  WHEN partytype = 'E' and EMP_PARTY_CODE=null THEN  (SELECT employeename   FROM VOUCHER_INFO    WHERE ecpfacno = ecpfacno)    ELSE    emp_party_code  END)partyDetails,  Nvl(preparedby,' ') preparedby, Nvl(voucherno,' ') voucherno, Nvl(checkedby,' ') checkedby, Nvl(approvedby,' ') approvedby,Nvl(details,' ') details,Nvl(to_char(VOUCHER_DT,'dd/Mon/YYYY'),' ') VOUCHER_DT,APPROVAL FROM voucher_info voucher, bank_info bank WHERE bank.accountno = voucher.accountno AND keyno = ? ";

	String reportDtQuery 	= "SELECT voucher.ACCOUNTHEAD ACCOUNTHEAD,acc.PARTICULAR PARTICULAR,MONTH_YEAR,details,Nvl(credit,0.0) credit,Nvl(debit,0.0) debit,Nvl(chequeno,'0') chequeno FROM VOUCHER_DETAILS voucher, ACCOUNTCODE_INFO acc WHERE voucher.ACCOUNTHEAD = acc.ACCOUNTHEAD AND keyno = ? ";

	String updateQuery 		= "update VOUCHER_INFO set VOUCHER_DT=?,CHECKEDBY=?,APPROVAL=?,APPROVEDBY=?,voucherno=? where KEYNO=?";
	
	String bankOpenBalQuery = "select (nvl((select amount from bankopeningbal_info where accountno = ?),0) + nCredit - nDebit) OpeningBanace,(select bankname from bank_info where accountno = ? )bankname from( select sum(decode(?,info.accountno, nvl(debit, 0), 0)) nDebit, sum(decode(?, info.emp_party_code, nvl(debit, 0),  nvl(credit, 0))) nCredit from voucher_details dt, (select keyno,accountno, vouchertype, voucher_dt,emp_party_code from voucher_info info where (info.accountno = ? or (info.emp_party_code = ? and info.vouchertype = 'C')) and voucher_dt is not null and info.voucher_dt < ?) info where dt.keyno = info.keyno)";

	String bankBookQuery 	= "SELECT info.keyno, dt.accounthead, acc.particular, credit, debit, details, vouchertype, partyname,info.voucherno,Nvl(chequeno,'0') chequeno FROM voucher_details dt,(SELECT keyno, vouchertype,(CASE WHEN partytype = 'E' THEN (SELECT employeename FROM employee_personal_info WHERE pensionno = emp_party_code) WHEN vouchertype = 'C' AND emp_party_code != ? THEN (SELECT bankname FROM bank_info WHERE accountno = emp_party_code) WHEN partytype = 'B' THEN (SELECT bankname FROM bank_info WHERE accountno = info.accountno) ELSE  emp_party_code END ) partyname, ? accountno,voucherno FROM voucher_info info WHERE  APPROVAL='Y' and (accountno = ? OR (vouchertype = 'C' AND emp_party_code = ?))and voucherno is not null and voucher_dt between ? and ?) info,accountcode_info acc WHERE info.keyno = dt.keyno and acc.ACCOUNTHEAD = dt.accounthead ";

	String editQuery 		= "select * from  VOUCHER_INFO voucher,BANK_INFO bank where bank.ACCOUNTNO = voucher.ACCOUNTNO and  KEYNO=?";

	String updateVoucherQry = "update  VOUCHER_INFO set ACCOUNTNO=?,FYEAR=?,TRUSTTYPE=?,PARTYTYPE=?,EMP_PARTY_CODE=?,PREPAREDBY=?,details=? where KEYNO=?";	

	String deleteDtQuery 	= " delete from  VOUCHER_DETAILS  where KEYNO=?";
	
	String deleteVQuery 	= " delete from  VOUCHER_INFO where INSTR(upper(?),upper(KEYNO)) > 0";
	
	String deleteVDTQuery 	= " delete from  VOUCHER_DETAILS  where INSTR(upper(?),upper(KEYNO)) > 0";

	String voucherNoQuery 	= "SELECT   voucher.vouchertype||'V/'|| bankcode||'/'||vinfo.fyear || '/'||LPAD(MAX(Nvl( SUBSTR (voucher.voucherno, INSTR (voucher.voucherno, '/', -1) + 1),0)) + 1, 4, 0) FROM (SELECT fyear,vouchertype, bankcode FROM voucher_info info, bank_info bank WHERE info.accountno = bank.accountno AND keyno = ?) vinfo,voucher_info voucher WHERE voucher.vouchertype = vinfo.vouchertype GROUP BY voucher.vouchertype, bankcode,vinfo.fyear";
	
	String keyNoGenQuery  	= "select to_char(to_date(?,'dd/mon/yyyy'),'ddmmyy')||Lpad(Nvl(max(substr(KEYNO,7)),0)+1,6,'0') voucherNo from VOUCHER_INFO where to_char(to_date(?,'dd/mon/yyyy'),'ddmmyy')=substr(KEYNO,0,6)";
}
