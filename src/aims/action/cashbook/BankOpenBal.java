package aims.action.cashbook;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aims.bean.cashbook.BankOpenBalInfo;
import aims.common.Log;
import aims.service.cashbook.BankOpenBalService;


public class BankOpenBal extends HttpServlet{
	Log log = new Log(BankOpenBal.class);
	BankOpenBalService service = new BankOpenBalService();
	BankOpenBalInfo info = new BankOpenBalInfo();
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		log.info("BankOpenBal : service : Entering Method");
		String dispatch = null;
		String redirect = null;
		if ("addOpenBalRecord".equals(request.getParameter("method"))) {			
			HttpSession session = request.getSession();
			info.setAccountNo(request.getParameter("accountNo"));
			info.setOpendate(request.getParameter("openedDate"));
			info.setAmount(Double.parseDouble("".equals(request.getParameter("amount").trim())?"0":request.getParameter("amount").trim()));
			info.setDetails(request.getParameter("details"));
			info.setEnteredBy((String)session.getAttribute("userid"));
			try {
				service.addOpenBalRecord(info);
				redirect = "./BankOpeningBal?method=searchRecords&&accountNo="+request.getParameter("accountNo");	
			} catch (Exception e) {
				log.printStackTrace(e);
			}			
		}else if ("searchRecords".equals(request.getParameter("method"))) {			
			info.setBankName(request.getParameter("bankName")==null?"":request.getParameter("bankName"));		
			info.setAccountNo(request.getParameter("accountNo")==null?"":request.getParameter("accountNo"));	
			try{
				List dataList = service.searchRecords(info);
				request.setAttribute("dataList",dataList);
				}catch (Exception e) {
					log.printStackTrace(e);
				}			
				dispatch = "./PensionView/cashbook/BankOpeningBalSearch.jsp";		
		}else if ("deleteBankOpenBalRecord".equals(request.getParameter("method"))) {			
			info.setAccountNo(request.getParameter("accNos"));		
			try{
				service.deleteBankOpenBalRecord(info);
				}catch (Exception e) {
					log.printStackTrace(e);
				}			
				dispatch = "./PensionView/cashbook/BankOpeningBalSearch.jsp";		
		}else if ("getRecord".equals(request.getParameter("method"))) {			
			info.setAccountNo(request.getParameter("accNo"));		
			try{
				info = service.getRecord(info);
				request.setAttribute("info",info);
				}catch (Exception e) {
					log.printStackTrace(e);
				}			
				dispatch = "./PensionView/cashbook/BankOpenBalEdit.jsp";		
		}else if ("updateOpenBalRecord".equals(request.getParameter("method"))) {
			HttpSession session = request.getSession();
			info.setAccountNo(request.getParameter("accountNo"));
			info.setOpendate(request.getParameter("openedDate"));
			info.setAmount(Double.parseDouble("".equals(request.getParameter("amount").trim())?"0.00":request.getParameter("amount").trim()));
			info.setDetails(request.getParameter("details"));
			info.setEnteredBy((String)session.getAttribute("userid"));
			try{
				service.updateOpenBalRecord(info);				
				}catch (Exception e) {
					log.printStackTrace(e);
				}			
				redirect = "./BankOpeningBal?method=searchRecords&&accountNo="+request.getParameter("accountNo");		
		}	
		log.info("BankOpenBal : service : Leaving Method");
		if(redirect !=null){
			response.sendRedirect(redirect);
		}else if(dispatch !=null){
			RequestDispatcher rd = request.getRequestDispatcher(dispatch);
			rd.forward(request,response);
		}
	}
}
