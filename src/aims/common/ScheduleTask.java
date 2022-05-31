package aims.common;

import java.util.TimerTask;

import aims.service.FinancialReportService;

public class ScheduleTask extends TimerTask{
	FinancialReportService finReportService = new FinancialReportService();
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Schedule working ");
		finReportService.runPFCardScheduler();
		
	}

}
