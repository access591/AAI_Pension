package aims.action.cashbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.SearchInfo;
import aims.bean.cashbook.AccountingCodeInfo;
import aims.common.Log;
import aims.service.cashbook.AccountingCodeService;

public class AccountingCode extends HttpServlet {

	Log log = new Log(AccountingCode.class);

	AccountingCodeService service = new AccountingCodeService();

	AccountingCodeInfo info = new AccountingCodeInfo();

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String error = null;
		String dispatch = null;
		String redirect = null;
		log.info("AccountingCode : service : Entering Method");
		if ("addAccountRecord".equals(request.getParameter("method"))) {
			HttpSession session = request.getSession();
			info.setAccountHead(request.getParameter("accountCode"));
			info.setParticular(request.getParameter("accountName"));
			info.setType(request.getParameter("accountType"));
			info.setEnteredBy((String) session.getAttribute("userid"));
			String popUp = request.getParameter("popUp");
			try {
				if (!service.exists(info)) {
					service.addAccountRecord(info);
					if ("Y".equals(popUp)) {
						redirect = "./PensionView/cashbook/AccountingCodePopup.jsp?redirect=Y&&accHead="
								+ request.getParameter("accountCode")
								+ "&particular="
								+ request.getParameter("accountName");
					} else {
						redirect = "./AccountingCode?method=searchRecords&&accountCode="
								+ info.getAccountHead();
					}
				} else {
					error = " Record Already Exists. ";
					if ("Y".equals(popUp)) {
						dispatch = "./PensionView/cashbook/AccountingCodePopup.jsp?error="
								+ error;
					} else {
						dispatch = "./PensionView/cashbook/AccountingCode.jsp?error="
								+ error;
					}
				}
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if ("getAccountList".equals(request.getParameter("method"))) {
			info.setParticular(request.getParameter("particular"));
			info.setAccountHead(request.getParameter("AccHead"));
			String type = request.getParameter("type");
			try {
				List dataList = service.getAccountList(info, type);
				request.setAttribute("AccList", dataList);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			dispatch = "./PensionView/cashbook/AccountInfo.jsp";
		} else if ("searchRecords".equals(request.getParameter("method"))) {
			info.setAccountHead(request.getParameter("accountCode"));
			info.setParticular(request.getParameter("accountName"));
			info.setType(request.getParameter("accountType"));
			try {
				List dataList = service.getAccountList(info, "");
				request.setAttribute("dataList", dataList);
			} catch (Exception e) {
				log.printStackTrace(e);
			}
			dispatch = "./PensionView/cashbook/AccountingCodeSearch.jsp";
		}

		else if ("editAccountRecord".equals(request.getParameter("method"))) {
			info.setAccountHead(request.getParameter("accHead"));
			try {
				ArrayList editList = (ArrayList) service.getAccountList(info,
						"edit");
				Iterator it = editList.iterator();
				while (it.hasNext()) {
					info = (AccountingCodeInfo) it.next();
				}
				request.setAttribute("einfo", info);
				dispatch = "./PensionView/cashbook/AccountingEdit.jsp";

			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if ("updateAccountRecord".equals(request.getParameter("method"))) {

			try {
				info.setAccountHead(request.getParameter("accountCode"));
				info.setParticular(request.getParameter("accountName"));
				info.setType(request.getParameter("accountType"));
				service.updateAccountRecord(info);
				info.setAccountHead("");
				info.setParticular("");
				info.setType("");
				List dataList = service.getAccountList(info, "");
				request.setAttribute("dataList", dataList);
				redirect = "./AccountingCode?method=searchRecords&&accountCode="
						+ request.getParameter("accountCode");

			} catch (Exception e) {
				log.printStackTrace(e);
			}
		} else if ("deleteAccountRecord".equals(request.getParameter("method"))) {

			try {
				String accHeads = request.getParameter("accHeads");
				String codes = accHeads.substring(0, accHeads.length() - 1);
				service.deleteAccountRecord(codes);

			} catch (Exception e) {
				error = "Record(s) can not be deleted";
			}
			info.setAccountHead("");
			info.setParticular("");
			info.setType("");
			List dataList;
			try {
				dataList = service.getAccountList(info, "");
				request.setAttribute("dataList", dataList);
			} catch (Exception e) {
				e.printStackTrace();
			}

			dispatch = "./PensionView/cashbook/AccountingCodeSearch.jsp?error="
					+ error;
		}
		log.info("AccountingCode : service : Leaving Method");
		if (redirect != null) {
			response.sendRedirect(redirect);
		} else if (dispatch != null) {
			RequestDispatcher rd = request.getRequestDispatcher(dispatch);
			rd.forward(request, response);
		}
	}
}
