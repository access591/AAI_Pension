package aims.bean;

public class DashBoardInfo {
	private String finyear;
	private String month;
	private long emoluments;
	private long epf;
	private long vpf;
	private long interest;
	private long principal;
	private long pfwsubamt;
	private long pfwcontriamt;
	private long finalsubamt;
	private long finalcontriamt;
	private long advance;
	private String region;
	private String remarks;
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public long getEmoluments() {
		return emoluments;
	}
	public void setEmoluments(long emoluments) {
		this.emoluments = emoluments;
	}
	public long getEpf() {
		return epf;
	}
	public void setEpf(long epf) {
		this.epf = epf;
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
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public long getPrincipal() {
		return principal;
	}
	public void setPrincipal(long principal) {
		this.principal = principal;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public long getVpf() {
		return vpf;
	}
	public void setVpf(long vpf) {
		this.vpf = vpf;
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
	public long getAdvance() {
		return advance;
	}
	public void setAdvance(long advance) {
		this.advance = advance;
	}
	public long getFinalcontriamt() {
		return finalcontriamt;
	}
	public void setFinalcontriamt(long finalcontriamt) {
		this.finalcontriamt = finalcontriamt;
	}
	public long getFinalsubamt() {
		return finalsubamt;
	}
	public void setFinalsubamt(long finalsubamt) {
		this.finalsubamt = finalsubamt;
	}
	
}
