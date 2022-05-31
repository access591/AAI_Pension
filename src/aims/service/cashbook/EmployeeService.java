package aims.service.cashbook;

import java.util.List;

import aims.bean.EmployeePersonalInfo;
import aims.bean.cashbook.BankOpenBalInfo;
import aims.common.Log;
import aims.dao.cashbook.BankOpenBalDAO;
import aims.dao.cashbook.EmployeeDAO;

public class EmployeeService {

	Log log = new Log(BankOpenBalService.class);
	EmployeeDAO dao = new EmployeeDAO();
	
	public List getEmployeeList(EmployeePersonalInfo info) throws Exception {
		log.info("EmployeeService : getEmployeeList : Entering Method");
		List empList = dao.getEmployeeList(info);		
		log.info("EmployeeService : getEmployeeList : Leaving Method");
		return empList;	
	}
}
