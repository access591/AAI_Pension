package aims.action;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import aims.common.CommonUtil;
import aims.common.ScheduleTask;





public class SchedulerServlet extends GenericServlet{
	CommonUtil commonUtil = new CommonUtil();
	public void init(ServletConfig servletConfig) throws ServletException { 
		super.init(servletConfig);
		System.out.println("SchedulerServlet.init()");
		Timer timer = new Timer();
		Calendar date = Calendar.getInstance();		 	 
		ResourceBundle bundle = ResourceBundle
		.getBundle("aims.resource.ApplicationResouces");		 
		int scheduleHour = 0, scheduleMinute = 0,scheduleSecond =0, scheduleMilliSec=0 ;
		scheduleHour = Integer.parseInt(bundle.getString("schedule.time.hour"));
		scheduleMinute = Integer.parseInt(bundle.getString("schedule.time.minute"));
		scheduleSecond = Integer.parseInt(bundle.getString("schedule.time.second"));
		scheduleMilliSec = Integer.parseInt(bundle.getString("schedule.time.millisecond"));
		
		 
		//date.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		date.set(Calendar.HOUR_OF_DAY, scheduleHour);
		date.set(Calendar.MINUTE, scheduleMinute);
		date.set(Calendar.SECOND, scheduleSecond);
		date.set(Calendar.MILLISECOND, scheduleMilliSec);
		timer.schedule(new ScheduleTask(), date.getTime(), 1000 * 60 * 60 * 24
				* 7);
		
	}
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
