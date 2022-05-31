package aims.bean;

import java.io.Serializable;

public class FinalPaymentsBean implements Serializable{
	private String pensionno,employeename,dateofbirth,dateofjoining,wetheroption,obYear,designation,activityRemarks,activity;
	private String pensionchecklist;
	private String finalsettlemntdate,aainetob,empnetob;
	public String getAainetob() {
		return aainetob;
	}
	public void setAainetob(String aainetob) {
		this.aainetob = aainetob;
	}
	public String getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public String getDateofjoining() {
		return dateofjoining;
	}
	public void setDateofjoining(String dateofjoining) {
		this.dateofjoining = dateofjoining;
	}
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	public String getEmpnetob() {
		return empnetob;
	}
	public void setEmpnetob(String empnetob) {
		this.empnetob = empnetob;
	}
	public String getFinalsettlemntdate() {
		return finalsettlemntdate;
	}
	public void setFinalsettlemntdate(String finalsettlemntdate) {
		this.finalsettlemntdate = finalsettlemntdate;
	}
	public String getObYear() {
		return obYear;
	}
	public void setObYear(String obYear) {
		this.obYear = obYear;
	}
	public String getPensionno() {
		return pensionno;
	}
	public void setPensionno(String pensionno) {
		this.pensionno = pensionno;
	}
	public String getWetheroption() {
		return wetheroption;
	}
	public void setWetheroption(String wetheroption) {
		this.wetheroption = wetheroption;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getActivityRemarks() {
		return activityRemarks;
	}
	public void setActivityRemarks(String activityRemarks) {
		this.activityRemarks = activityRemarks;
	}
	public String getPensionchecklist() {
		return pensionchecklist;
	}
	public void setPensionchecklist(String pensionchecklist) {
		this.pensionchecklist = pensionchecklist;
	}
	
}
