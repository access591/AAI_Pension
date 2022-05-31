package aims.action.cashbook;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.cashbook.AccountingCodeInfo;
import aims.bean.cashbook.BankMasterInfo;
import aims.common.Log;
import aims.service.cashbook.BankMasterService;

public class BankMaster extends HttpServlet {

	Log log = new Log(BankMaster.class);

	BankMasterService service = new BankMasterService();

	BankMasterInfo info = new BankMasterInfo();

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("BankMaster : service : Entering Method");
		String error = null;
		String dispatch = null;
		String redirect = null;
		if ("addBankRecord".equals(request.getParameter("method"))) {
			HttpSession session = request.getSession();
			info.setBankName(request.getParameter("bankName"));
			info.setBranchName(request.getParameter("branchName"));
			info.setBankCode(request.getParameter("bankCode"));
			info.setPhoneNo(request.getParameter("phoneNo"));
			info.setFaxNo(request.getParameter("faxNo"));
			info.setAccountCode(request.getParameter("accountCode"));
			info.setAccountNo(request.getParameter("accountNo"));
			info.setIFSCCode(request.getParameter("ifscCode"));
			info.setNEFTRTGSCode(request.getParameter("neftCode"));
			info.setMICRNo(request.getParameter("micrNo"));
			info.setContactPerson(request.getParameter("contactPerson"));
			info.setMobileNo(request.getParameter("mobileNo"));
			info.setAddress(request.getParameter("address"));
			info.setEnteredBy((String) session.getAttribute("userid"));
			info.setAccountType(request.getParameter("accountType"));
			info.setTrustType(request.getParameter("trusttype"));
			info.setUnitName(request.getParameter("unitName"));
			try {
				if (!service.exists(info)) {
					service.addBankRecord(info);
					redirect = "./BankMaster?method=searchRecords&&bankacno="
							+ request.getParameter("accountNo");
				} else {
					error = " Record Already Exists. ";
					dispatch = "./PensionView/cashbook/BankMaster.jsp?error="
							+ error;
				}
			} catch (Exception e) {
				log.printStackTrace(e);
				if (e.getMessage().indexOf("CHECK_PACCNO") > 0) {
					dispatch = "./PensionView/cashbook/BankMaster.jsp?error=Account Details Exists in Party Master";
				} else {
					dispatch = "./PensionView/cashbook/BankMaster.jsp?error="
							+ e.getMessage();
				}
			}
		} else if ("getBankList".equals(request.getParameter("method"))) {
			String rem = request.getParameter("type");
			info.setAccountNo(request.getParameter("accountNo"));
			info.setBankName(request.getParameter("bankName"));
			List dataList = null;
			try {
				dataList = service.getBankList(info, rem);
				request.setAttribute("BankList", dataList);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			if ("ajax".equals(rem)) {
				StringBuffer sb = new StringBuffer("<ServiceTypes>");
				int listSize = dataList.size();
				for (int cnt = 0; cnt < listSize; cnt++) {
					BankMasterInfo info = (BankMasterInfo) dataList.get(cnt);
					sb.append("<ServiceType><bankName>").append(
							info.getBankName()).append("</bankName>");
					sb.append("<accountNo>").append(info.getAccountNo())
							.append("</accountNo>");
					sb.append("<accHead>").append(info.getAccountCode())
							.append("</accHead>");
					sb.append("<particular>").append(info.getParticular())
							.append("</particular>");
					sb.append("<trustType>").append(info.getTrustType())
							.append("</trustType></ServiceType>");
				}
				sb.append("</ServiceTypes>");
				response.setContentType("text/xml");
				PrintWriter out = response.getWriter();
				out.print(sb);

			} else {
				dispatch = "./PensionView/cashbook/BankInfo.jsp";
			}
		} else if ("searchRecords".equals(request.getParameter("method"))) {
			info.setBankName(request.getParameter("bankname") == null ? ""
					: request.getParameter("bankname"));
			info.setBranchName(request.getParameter("branchname") == null ? ""
					: request.getParameter("branchname"));
			info.setBankCode(request.getParameter("bankcode") == null ? ""
					: request.getParameter("bankcode"));
			info.setAccountNo(request.getParameter("bankacno") == null ? ""
					: request.getParameter("bankacno"));
			try {
				List dataList = service.getBankList(info, "");
				request.setAttribute("BankList", dataList);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			dispatch = "./PensionView/cashbook/BankMasterSearch.jsp";
		} else if ("editBankRecord".equals(request.getParameter("method"))) {
			info.setAccountNo(request.getParameter("accNo"));
			try {
				ArrayList editList = (ArrayList) service.getBankList(info,
						"edit");
				Iterator it = editList.iterator();
				while (it.hasNext()) {
					info = (BankMasterInfo) it.next();
				}
				request.setAttribute("binfo", info);
				dispatch = "./PensionView/cashbook/BankMasterEdit.jsp";

			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if ("updateBankRecord".equals(request.getParameter("method"))) {

			try {
				info.setBankName(request.getParameter("bankName"));
				info.setBranchName(request.getParameter("branchName"));
				info.setBankCode(request.getParameter("bankCode"));
				info.setPhoneNo(request.getParameter("phoneNo"));
				info.setFaxNo(request.getParameter("faxNo"));
				info.setAccountCode(request.getParameter("accountCode"));
				info.setAccountNo(request.getParameter("accountNo"));
				info.setIFSCCode(request.getParameter("ifscCode"));
				info.setNEFTRTGSCode(request.getParameter("neftCode"));
				info.setMICRNo(request.getParameter("micrNo"));
				info.setContactPerson(request.getParameter("contactPerson"));
				info.setMobileNo(request.getParameter("mobileNo"));
				info.setAddress(request.getParameter("address"));
				info.setAccountType(request.getParameter("accountType"));
				info.setUnitName(request.getParameter("unitName"));
				service.updateBankRecord(info);
				info.setBankName("");
				info.setBranchName("");
				info.setBankCode("");
				info.setAccountNo("");

				List dataList = service.getBankList(info, "");
				request.setAttribute("BankList", dataList);
				redirect = "./BankMaster?method=searchRecords&&bankacno="
						+ request.getParameter("accountNo");

			} catch (Exception e) {
				log.printStackTrace(e);
				if (e.getMessage().indexOf("CHECK_PACCNO") > 0) {
					dispatch = "./PensionView/cashbook/BankMasterEdit.jsp?error=Account Details Exists in Party Master";
				} else {
					dispatch = "./PensionView/cashbook/BankMasterEdit.jsp?error="
							+ e.getMessage();
				}
				request.setAttribute("binfo", info);
			}
		} else if ("deleteBankRecord".equals(request.getParameter("method"))) {

			try {
				String accNo = request.getParameter("accNo");
				String codes = accNo.substring(0, accNo.length() - 1);
				service.deleteBankRecord(codes);

			} catch (Exception e) {
				error = "Record(s) can not be deleted";
			}
			info.setBankName("");
			info.setBranchName("");
			info.setBankCode("");
			info.setAccountNo("");
			List dataList;
			try {
				dataList = service.getBankList(info, "");
				request.setAttribute("BankList", dataList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dispatch = "./PensionView/cashbook/BankMasterSearch.jsp?error="
					+ error;
		}
		log.info("BankMaster : service : Leaving Method");
		if (redirect != null) {
			response.sendRedirect(redirect);
		} else if (dispatch != null) {
			RequestDispatcher rd = request.getRequestDispatcher(dispatch);
			rd.forward(request, response);
		}
	}
}
