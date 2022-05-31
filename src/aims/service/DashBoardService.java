package aims.service;

import aims.bean.DashBoardDetails;
import aims.dao.DashBoardDAO;

public class DashBoardService {
	DashBoardDAO dbDAO = new DashBoardDAO();
	public DashBoardDetails  getDashBoardInfo(String  finyear,String monthyear){
		 DashBoardDetails dbinfo = new DashBoardDetails();
		 dbinfo =  dbDAO.getDashBoardInfo(finyear,monthyear);
		 return dbinfo;
	    }
	public DashBoardDetails  getDashBoardPFWInfo(String  finyear,String monthyear,String paymentstatus){
		 DashBoardDetails dbPFWInfo = new DashBoardDetails();
		 dbPFWInfo =  dbDAO.getDashBoardPFWInfo(finyear,monthyear,paymentstatus);
		 return dbPFWInfo;
	    }
	public DashBoardDetails  getDashBoardFinalInfo(String  finyear,String monthyear,String paymentstatus){
		 DashBoardDetails dbPFWInfo = new DashBoardDetails();
		 dbPFWInfo =  dbDAO.getDashBoardFinalInfo(finyear,monthyear,paymentstatus);
		 return dbPFWInfo;
	    }

}
