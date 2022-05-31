package aims.service.cashbook;

import java.util.List;

import aims.bean.cashbook.BankBook;
import aims.bean.cashbook.VoucherInfo;
import aims.common.Log;
import aims.dao.cashbook.VoucherDAO;

public class VoucherService {

	Log log = new Log(VoucherService.class);
	VoucherDAO dao = new VoucherDAO();
	
	public void addVoucherRecord(VoucherInfo info) throws  Exception{
		log.info("VoucherService : addVoucherRecord() entering method");
		dao.addVoucherRecord(info);		
		log.info("VoucherService : addVoucherRecord() leaving method");
	}

	public List searchRecords(VoucherInfo info,String type) throws Exception {
		log.info("VoucherService : searchRecords() entering method");
		List dataList = dao.searchRecords(info,type);		
		log.info("VoucherService : searchRecords() leaving method");
		return dataList;
	}

	public VoucherInfo getReport(VoucherInfo info) throws Exception {
		log.info("VoucherService : getReport() entering method");
		info = dao.getReport(info);		
		log.info("VoucherService : getReport() leaving method");
		return info;
	}

	public BankBook getBankBook(VoucherInfo info) throws Exception {
		log.info("VoucherService : getBankBook() entering method");
		BankBook book = dao.getBankBook(info);		
		log.info("VoucherService : getBankBook() leaving method");
		return book;
	}
	
	public void updateApprovalVoucher(VoucherInfo info) throws Exception {
		log.info("VoucherService : updateApprovalVoucher() entering method");
		dao.updateApprovalVoucher(info);		
		log.info("VoucherService : updateApprovalVoucher() leaving method");
		
	}

	public void updateVoucherRecord(VoucherInfo info) throws Exception {
		log.info("VoucherService : updateVoucherRecord() entering method");
		dao.updateVoucherRecord(info);		
		log.info("VoucherService : updateVoucherRecord() leaving method");
	}

	public void deleteVoucher(String codes) throws Exception {
		log.info("VoucherService : deleteVoucher() entering method");
		dao.deleteVoucher(codes);		
		log.info("VoucherService : deleteVoucher() leaving method");
	}
}
