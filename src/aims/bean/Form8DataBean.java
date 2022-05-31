package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Form8DataBean implements Serializable{
	Form8RemittanceBean remittanceBean=new Form8RemittanceBean();
	String employeeName="",pensionNo="",remarks="",dateofbirth="",wetheroption="";
	ArrayList Form8List=new ArrayList();
	public String getEmployeeName() {
		return employeeName;
	}
	public String getDateofbirth() {
		return dateofbirth;
	}
	public void setDateofbirth(String dateofbirth) {
		this.dateofbirth = dateofbirth;
	}
	public String getWetheroption() {
		return wetheroption;
	}
	public void setWetheroption(String wetheroption) {
		this.wetheroption = wetheroption;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public ArrayList getForm8List() {
		return Form8List;
	}
	public void setForm8List(ArrayList form8List) {
		Form8List = form8List;
	}
	public String getPensionNo() {
		return pensionNo;
	}
	public void setPensionNo(String pensionNo) {
		this.pensionNo = pensionNo;
	}
	public Form8RemittanceBean getRemittanceBean() {
		return remittanceBean;
	}
	public void setRemittanceBean(Form8RemittanceBean remittanceBean) {
		this.remittanceBean = remittanceBean;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
