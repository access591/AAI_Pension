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

import aims.bean.cashbook.AccountingCodeInfo;
import aims.bean.cashbook.BankMasterInfo;
import aims.bean.cashbook.PartyInfo;
import aims.common.Log;
import aims.service.cashbook.PartyService;


public class Party extends HttpServlet{

	Log log = new Log(Party.class);
	PartyService service = new PartyService();
	PartyInfo info = new PartyInfo();
	public void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {		
		log.info("Party : service : Entering Method");
		String error = null;
		String dispatch = null;
		String redirect = null;
		if ("getPartyList".equals(request.getParameter("method"))) {
			String pName = request.getParameter("pName");
			info.setPartyName(pName==null?"":pName.trim());
			try {
				List dataList = service.getPartyList(info,"");				
				request.setAttribute("PartyList",dataList);
				dispatch = "./PensionView/cashbook/PartyInfo.jsp";				
			} catch (Exception e) {
				log.printStackTrace(e);
			}
		}else if ("addPartyRecord".equals(request.getParameter("method"))) {
			info.setPartyName(request.getParameter("partyName"));
			info.setPartyDetail(request.getParameter("partyDetail"));
			info.setMobileNo(request.getParameter("mobileNo"));
			info.setFaxNo(request.getParameter("faxNo"));
			info.setContactNo(request.getParameter("contactNo"));
			info.setEmailId(request.getParameter("email"));
			HttpSession session = request.getSession();
			info.setEnteredBy((String)session.getAttribute("userid"));
			// Bank Party Detail
			BankMasterInfo bInfo = new BankMasterInfo();
			bInfo.setBankName(request.getParameter("bankName"));
			bInfo.setBranchName(request.getParameter("branchName"));
			bInfo.setBankCode(request.getParameter("bankCode"));
			bInfo.setPhoneNo(request.getParameter("phoneNo"));
			bInfo.setFaxNo(request.getParameter("bFaxNo"));
			bInfo.setAccountCode(request.getParameter("accountCode"));
			bInfo.setAccountNo(request.getParameter("accountNo"));
			bInfo.setIFSCCode(request.getParameter("ifscCode"));
			bInfo.setNEFTRTGSCode(request.getParameter("neftCode"));
			bInfo.setMICRNo(request.getParameter("micrNo"));
			bInfo.setContactPerson(request.getParameter("contactPerson"));
			bInfo.setMobileNo(request.getParameter("bMobileNo"));
			bInfo.setAddress(request.getParameter("address"));
			bInfo.setAccountType(request.getParameter("accountType"));
			info.setBankInfo(bInfo);
			try {
				if(!service.exists(info)){
					service.addPartyRecord(info);
					redirect = "./Party?method=searchRecords&&partyName="+info.getPartyName();
				}else {
					error = " Record Already Exists. ";
					dispatch = "./PensionView/cashbook/PartyMaster.jsp?error="+error;
				}				
			} catch (Exception e) {
				log.printStackTrace(e);
				if(e.getMessage().indexOf("CHECK_ACCNO")>0){
					dispatch = "./PensionView/cashbook/PartyMaster.jsp?error=Account Details Exists in Bank Master";
				}
			}
						
		}else if("searchRecords".equals(request.getParameter("method"))) {
			info.setPartyName(request.getParameter("partyName")==null?"":request.getParameter("partyName"));
			
			try{
				List dataList = service.getPartyList(info,"");
				request.setAttribute("dataList",dataList);
			}catch (Exception e) {
				log.printStackTrace(e);
			}			
			dispatch = "./PensionView/cashbook/PartyMasterSearch.jsp";
		}else if ("editPartyRecord".equals(request.getParameter("method"))) {
		     info.setPartyName( request.getParameter("pName"));
				try {
					ArrayList editList = (ArrayList)service.getPartyList(info,"edit");
					Iterator it = editList.iterator();
					while (it.hasNext()) {
						info= (PartyInfo)it.next();
					}
					request.setAttribute("einfo",info);
					dispatch = "./PensionView/cashbook/PartyEdit.jsp";
					
				} catch (Exception e) {
					log.printStackTrace(e);
				}						
			}else if ("updatePartyRecord".equals(request.getParameter("method"))) {
			    
				try {
					info.setPartyName(request.getParameter("partyName"));
					info.setPartyDetail(request.getParameter("partyDetail"));
					info.setMobileNo(request.getParameter("mobileNo"));
					info.setFaxNo(request.getParameter("faxNo"));
					info.setContactNo(request.getParameter("contactNo"));
					info.setEmailId(request.getParameter("email"));
//					 Bank Party Detail
					BankMasterInfo bInfo = new BankMasterInfo();
					bInfo.setBankName(request.getParameter("bankName"));
					bInfo.setBranchName(request.getParameter("branchName"));
					bInfo.setBankCode(request.getParameter("bankCode"));
					bInfo.setPhoneNo(request.getParameter("phoneNo"));
					bInfo.setFaxNo(request.getParameter("bFaxNo"));
					bInfo.setAccountCode(request.getParameter("accountCode"));
					bInfo.setAccountNo(request.getParameter("accountNo"));
					bInfo.setIFSCCode(request.getParameter("ifscCode"));
					bInfo.setNEFTRTGSCode(request.getParameter("neftCode"));
					bInfo.setMICRNo(request.getParameter("micrNo"));
					bInfo.setContactPerson(request.getParameter("contactPerson"));
					bInfo.setMobileNo(request.getParameter("bMobileNo"));
					bInfo.setAddress(request.getParameter("address"));
					bInfo.setAccountType(request.getParameter("accountType"));
					info.setBankInfo(bInfo);
					service.updatePartyRecord(info);
					info.setPartyName("");
					List dataList = service.getPartyList(info,"");
					request.setAttribute("dataList",dataList);
					redirect = "./Party?method=searchRecords&&partyName="+request.getParameter("partyName");
					
				} catch (Exception e) {
					log.printStackTrace(e);
					if(e.getMessage().indexOf("CHECK_ACCNO")>0){
						dispatch = "./PensionView/cashbook/PartyEdit.jsp?error=Account Details Exists in Bank Master";
						request.setAttribute("einfo",info);
					}
				}						
			}else if ("deletePartyRecord".equals(request.getParameter("method"))) {
			    
				try {
					 String partyName = request.getParameter("partyName");
					 String codes = partyName.substring(0,partyName.length()-1);
					service.deletePartyRecord(codes);
						
				} catch (Exception e) {
					error = "Record(s) can not be deleted";
				}	
				info.setPartyName("");
				List dataList;
				try {
					dataList = service.getPartyList(info,"");
					request.setAttribute("dataList",dataList);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				dispatch = "./PensionView/cashbook/PartyMasterSearch.jsp?error="+error;
			}
		log.info("Party : service : Leaving Method");
		if(redirect !=null){
			response.sendRedirect(redirect);
		}else if(dispatch !=null){
			RequestDispatcher rd = request.getRequestDispatcher(dispatch);
			rd.forward(request,response);
		}
	}
}
