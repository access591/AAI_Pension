package aims.action.cashbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.cashbook.AccountingCodeInfo;
import aims.bean.cashbook.BankBook;
import aims.bean.cashbook.VoucherDetails;
import aims.bean.cashbook.VoucherInfo;
import aims.common.Log;
import aims.service.cashbook.VoucherService;

public class Voucher extends HttpServlet{
	
	Log log = new Log(Voucher.class);
	VoucherInfo info = new VoucherInfo();
	VoucherService service = new VoucherService();
	
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		log.info("Voucher : service : Entering Method");
		String dispatch=null;	
		String redirect = null;
		if ("addVoucher".equals(request.getParameter("method"))) {
			HttpSession session = request.getSession();
			info.setAccountNo(request.getParameter("accountNo"));
			info.setFinYear(request.getParameter("year"));
			info.setTrustType(request.getParameter("trusttype"));
			info.setVoucherType(request.getParameter("vouchertype"));			
			info.setEnteredBy((String)session.getAttribute("userid"));
			info.setPreparedBy((String)session.getAttribute("userid"));
			info.setDetails(request.getParameter("voucherDetails"));
			info.setPreparedDt(request.getParameter("prepDate"));
			String detailRecords[] = request.getParameterValues("detailRecords");
			int length = detailRecords.length;
			List voucherDts = new ArrayList();			
			VoucherDetails voucherDt = null;			
			for(int i=0;i < length; i++) {           				
				StringTokenizer st=new StringTokenizer(detailRecords[i],"|");				
				if(st.hasMoreTokens()){   		
					voucherDt = new VoucherDetails();
					voucherDt.setAccountHead(st.nextToken());						
					voucherDt.setMonthYear(st.nextToken());
					voucherDt.setDetails(st.nextToken());
					voucherDt.setChequeNo(st.nextToken());
					voucherDt.setDebit(Double.parseDouble(st.nextToken()));
					voucherDt.setCredit(Double.parseDouble(st.nextToken()));
					
				}
				voucherDts.add(voucherDt);
			}
			info.setVoucherDetails(voucherDts);
			if(!"C".equals(info.getVoucherType())){
				info.setPartyType(request.getParameter("party"));
				if("E".equals(info.getPartyType())){
					info.setEmpPartyCode(request.getParameter("epfid"));				
				}else if("P".equals(info.getPartyType())){
					info.setEmpPartyCode(request.getParameter("pName"));
				}
			}else {
				info.setPartyType("B");
				info.setEmpPartyCode(request.getParameter("contraAccountNo"));
			}
			try {
				service.addVoucherRecord(info);
				redirect = "./Voucher?method=searchRecords&&accountNo="+info.getAccountNo()+"&&voucherType="+info.getVoucherType();
			} catch (Exception e) {
				log.printStackTrace(e);
			}			
		}else if ("searchRecords".equals(request.getParameter("method"))) {
			info.setBankName(request.getParameter("bankName")==null?"":request.getParameter("bankName"));
			info.setFinYear(request.getParameter("year")==null?"":request.getParameter("year"));
			info.setVoucherType(request.getParameter("voucherType")==null?"":request.getParameter("voucherType"));
			info.setPartyType(request.getParameter("partyType")==null?"":request.getParameter("partyType"));
			info.setAccountNo(request.getParameter("accountNo")==null?"":request.getParameter("accountNo"));
			try {
				List dataList = service.searchRecords(info,"");
				request.setAttribute("dataList",dataList);
				dispatch = "./PensionView/cashbook/VoucherSearch.jsp";
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("getReport".equals(request.getParameter("method"))) {
			info.setKeyNo(request.getParameter("keyNo"));
			try {
				info = service.getReport(info);
				request.setAttribute("info",info);
				if("approve".equals(request.getParameter("type")))
					dispatch = "./PensionView/cashbook/ApprovedVoucher.jsp";
				else
				dispatch = "./PensionView/cashbook/VoucherReport.jsp";
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("getVoucherAppRecords".equals(request.getParameter("method"))) {
			info.setBankName(request.getParameter("bankName"));
			info.setFinYear(request.getParameter("year"));
			info.setVoucherType(request.getParameter("voucherType"));
			info.setPartyType(request.getParameter("partyType"));
			try {
				List dataList = service.searchRecords(info,"A");
				request.setAttribute("dataList",dataList);
				dispatch = "./PensionView/cashbook/VoucherApproval.jsp";
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("getVoucherApproval".equals(request.getParameter("method"))) {
			
			try {
				info.setVoucherDt(request.getParameter("voucherDate"));
				info.setKeyNo(request.getParameter("keyNo"));
				info.setCheckedBy(request.getParameter("checkedby"));
				info.setApprovedBy(request.getParameter("approved"));
				info.setStatus(request.getParameter("approvalstatus"));
				service.updateApprovalVoucher(info);
				dispatch =  "./PensionView/cashbook/VoucherApproval.jsp";
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("getBankBook".equals(request.getParameter("method"))) {
			info.setAccountNo(request.getParameter("accountNo"));
			info.setFromDate(request.getParameter("fromDate"));
			info.setToDate(request.getParameter("toDate"));
			try {
				BankBook book = service.getBankBook(info);
				request.setAttribute("book",book);
				dispatch = "./PensionView/cashbook/BankBook.jsp";
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("editVoucher".equals(request.getParameter("method"))) {
			
		     info.setKeyNo(request.getParameter("keyNo"));
				try {
					 VoucherInfo editInfo =service.getReport(info);
					request.setAttribute("einfo",editInfo);
					dispatch = "./PensionView/cashbook/VoucherEdit.jsp";
					
				} catch (Exception e) {
					log.printStackTrace(e);
				}	
				
			}else if ("updateVoucher".equals(request.getParameter("method"))) {				
				info.setKeyNo(request.getParameter("keyNo"));
				info.setAccountNo(request.getParameter("accountNo"));
				info.setFinYear(request.getParameter("year"));
				info.setTrustType(request.getParameter("trusttype"));
				info.setDetails(request.getParameter("voucherDetails"));
				info.setVoucherType(request.getParameter("vouchertype"));
				String detailRecords[] = request.getParameterValues("detailRecords");
				int length = detailRecords.length;
				List voucherDts = new ArrayList();			
				VoucherDetails voucherDt = null;			
				for(int i=0;i < length; i++) {           				
					StringTokenizer st=new StringTokenizer(detailRecords[i],"|");				
					if(st.hasMoreTokens()){   		
						voucherDt = new VoucherDetails();
						voucherDt.setAccountHead(st.nextToken());						
						voucherDt.setMonthYear(st.nextToken());
						voucherDt.setDetails(st.nextToken());
						voucherDt.setChequeNo(st.nextToken());
						voucherDt.setDebit(Double.parseDouble(st.nextToken()));
						voucherDt.setCredit(Double.parseDouble(st.nextToken()));
					}
					voucherDts.add(voucherDt);
				}
				info.setVoucherDetails(voucherDts);
				if(!"C".equals(info.getVoucherType())){
					info.setPartyType(request.getParameter("party"));
					if("E".equals(info.getPartyType())){
						info.setEmpPartyCode(request.getParameter("epfid"));				
					}else if("P".equals(info.getPartyType())){
						info.setEmpPartyCode(request.getParameter("pName"));
					}
				}else {
					info.setPartyType("B");
					info.setEmpPartyCode(request.getParameter("contraAccountNo"));
				}
				try {
					service.updateVoucherRecord(info);
					redirect = "./Voucher?method=searchRecords&&accountNo="+info.getAccountNo()+"&&voucherType="+request.getParameter("vouchertype");
				} catch (Exception e) {
					log.printStackTrace(e);
				}
			} else if ("deleteVoucher".equals(request.getParameter("method"))) {	
				String error = null;
				try {
					String keynos = request.getParameter("keynos");
					String codes = keynos.substring(0, keynos.length() - 1);
					service.deleteVoucher(codes);
				} catch (Exception e) {
					e.printStackTrace();
					error = "Record(s) can not be deleted";
				}
				info.setKeyNo("");
				List dataList;
				try {
					dataList = service.searchRecords(info,"A");
					request.setAttribute("dataList", dataList);
				} catch (Exception e) {
					e.printStackTrace();
				}
				dispatch = "./PensionView/cashbook/VoucherApproval.jsp?error="+ error;
			}
		log.info("Voucher : service : Leaving Method");
		if(redirect !=null){
			response.sendRedirect(redirect);
		}else if(dispatch !=null){
			RequestDispatcher rd = request.getRequestDispatcher(dispatch);
			rd.forward(request,response);
		}
	}
}
