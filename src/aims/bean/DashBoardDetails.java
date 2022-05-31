package aims.bean;
import java.util.ArrayList;
public class DashBoardDetails {
	private String monthyear;
	private String finyear;
	private String paymentStatus;
	private String finalPaymentStatus;
	private long emolumentstot;
	private long epftot;
	private long vpftot;
	private long principal;
	private long interest;
	private long pfwsubamt;
	private long finalsubamtTot;
	private long finalcontriamtTot;
	private long pfwcontriamt;
	private long advanceTot;
	private ArrayList list;
	
	public ArrayList getList() {
		return list;
	}
	public void setList(ArrayList list) {
		this.list = list;
	}
	public long getEmolumentstot() {
		return emolumentstot;
	}
	public void setEmolumentstot(long emolumentstot) {
		this.emolumentstot = emolumentstot;
	}
	public long getEpftot() {
		return epftot;
	}
	public void setEpftot(long epftot) {
		this.epftot = epftot;
	}
	public String getFinyear() {
		return finyear;
	}
	public void setFinyear(String finyear) {
		this.finyear = finyear;
	}
	public long getInterest() {
		return interest;
	}
	public void setInterest(long interest) {
		this.interest = interest;
	}
	public String getMonthyear() {
		return monthyear;
	}
	public void setMonthyear(String monthyear) {
		this.monthyear = monthyear;
	}
	public long getPrincipal() {
		return principal;
	}
	public void setPrincipal(long principal) {
		this.principal = principal;
	}
	public long getVpftot() {
		return vpftot;
	}
	public void setVpftot(long vpftot) {
		this.vpftot = vpftot;
	}
	public long getPfwcontriamt() {
		return pfwcontriamt;
	}
	public void setPfwcontriamt(long pfwcontriamt) {
		this.pfwcontriamt = pfwcontriamt;
	}
	public long getPfwsubamt() {
		return pfwsubamt;
	}
	public void setPfwsubamt(long pfwsubamt) {
		this.pfwsubamt = pfwsubamt;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public long getAdvanceTot() {
		return advanceTot;
	}
	public void setAdvanceTot(long advanceTot) {
		this.advanceTot = advanceTot;
	}
	public long getFinalcontriamtTot() {
		return finalcontriamtTot;
	}
	public void setFinalcontriamtTot(long finalcontriamtTot) {
		this.finalcontriamtTot = finalcontriamtTot;
	}
	public String getFinalPaymentStatus() {
		return finalPaymentStatus;
	}
	public void setFinalPaymentStatus(String finalPaymentStatus) {
		this.finalPaymentStatus = finalPaymentStatus;
	}
	public long getFinalsubamtTot() {
		return finalsubamtTot;
	}
	public void setFinalsubamtTot(long finalsubamtTot) {
		this.finalsubamtTot = finalsubamtTot;
	}
	

}
