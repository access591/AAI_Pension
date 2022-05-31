package aims.bean;

import java.io.Serializable;

public class Form78SummaryReport implements Serializable,Comparable {
	String pensionno="",employeename="",dateofbirth="",dateofjoining="",wetheroption="",form7TransInfo="",form8TransInfo="";
	
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

	public String getForm7TransInfo() {
		return form7TransInfo;
	}

	public void setForm7TransInfo(String form7TransInfo) {
		this.form7TransInfo = form7TransInfo;
	}

	public String getForm8TransInfo() {
		return form8TransInfo;
	}

	public void setForm8TransInfo(String form8TransInfo) {
		this.form8TransInfo = form8TransInfo;
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
	public int compareTo(Object o1) {
        if (Integer.parseInt(this.pensionno) == Integer.parseInt(((Form78SummaryReport) o1).pensionno))
            return 0;
        else if ((Integer.parseInt(this.pensionno)) > Integer.parseInt(((Form78SummaryReport) o1).pensionno))
            return 1;
        else
            return -1;
    }



}
