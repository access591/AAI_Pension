package aims.bean;

public class StationWiseRemittancebean {
	private String station="";
	private String region="";
	private String accType="";
	private String orders="";
	private String noofemppc="";
	private String uploadpc="";
	private String pc="",pf="",inspCharges="";
	private String pcRemitDate="",pfRemitDate="",inspremitDate="";
	private String chqPc="",chqPf="",chqInspCharges="";
	private String chqPcRemitDate="",chqPfRemitDate="",chqInspremitDate="";
	private String cpfPc="",arrearPc="",suppliPc="";
	private String cpfPf="",arrearPf="",suppliPf="";
	private String cpfInspCharges="",arrearInspCharges="",suppliInspCharges="";
	private double pcTotal=0.0,pfTotal=0.0,inspTotal=0.0,addContriTotal=0.0;

	private String notes="";
	private String cpfEpf="",cpfVpf="",cpfRefAdv="",cpfAdvInt="",aaipc="",cpfTotal="";
	private String aEpf="",aVpf="",aRefAdv="",aAdvInt="",apc="",aTotal="";
	private String sEpf="",sVpf="",sRefAdv="",sAdvInt="",spc="",sTotal="";
	private String cpfaddcontri="",aaddcontri="",saddcontri="";
	private String pccode="";
	
	public String getPccode() {
		return pccode;
	}
	public void setPccode(String pccode) {
		this.pccode = pccode;
	}
	public double getAddContriTotal() {
		return addContriTotal;
	}
	public void setAddContriTotal(double addContriTotal) {
		this.addContriTotal = addContriTotal;
	}
	public String getCpfaddcontri() {
		return cpfaddcontri;
	}
	public void setCpfaddcontri(String cpfaddcontri) {
		this.cpfaddcontri = cpfaddcontri;
	}
	public String getAaddcontri() {
		return aaddcontri;
	}
	public void setAaddcontri(String aaddcontri) {
		this.aaddcontri = aaddcontri;
	}
	public String getSaddcontri() {
		return saddcontri;
	}
	public void setSaddcontri(String saddcontri) {
		this.saddcontri = saddcontri;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
	public String getUploadpc() {
		return uploadpc;
	}
	public void setUploadpc(String uploadpc) {
		this.uploadpc = uploadpc;
	}
	public String getNoofemppc() {
		return noofemppc;
	}
	public void setNoofemppc(String noofemppc) {
		this.noofemppc = noofemppc;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getArrearInspCharges() {
		return arrearInspCharges;
	}
	public void setArrearInspCharges(String arrearInspCharges) {
		this.arrearInspCharges = arrearInspCharges;
	}
	public String getArrearPc() {
		return arrearPc;
	}
	public void setArrearPc(String arrearPc) {
		this.arrearPc = arrearPc;
	}
	public String getArrearPf() {
		return arrearPf;
	}
	public void setArrearPf(String arrearPf) {
		this.arrearPf = arrearPf;
	}
	public String getCpfInspCharges() {
		return cpfInspCharges;
	}
	public void setCpfInspCharges(String cpfInspCharges) {
		this.cpfInspCharges = cpfInspCharges;
	}
	public String getCpfPc() {
		return cpfPc;
	}
	public void setCpfPc(String cpfPc) {
		this.cpfPc = cpfPc;
	}
	public String getCpfPf() {
		return cpfPf;
	}
	public void setCpfPf(String cpfPf) {
		this.cpfPf = cpfPf;
	}
	public String getInspCharges() {
		return inspCharges;
	}
	public void setInspCharges(String inspCharges) {
		this.inspCharges = inspCharges;
	}
	public String getInspremitDate() {
		return inspremitDate;
	}
	public void setInspremitDate(String inspremitDate) {
		this.inspremitDate = inspremitDate;
	}
	public double getInspTotal() {
		return inspTotal;
	}
	public void setInspTotal(double inspTotal) {
		this.inspTotal = inspTotal;
	}
	public String getPcRemitDate() {
		return pcRemitDate;
	}
	public void setPcRemitDate(String pcRemitDate) {
		this.pcRemitDate = pcRemitDate;
	}
	public double getPcTotal() {
		return pcTotal;
	}
	public void setPcTotal(double pcTotal) {
		this.pcTotal = pcTotal;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getPfRemitDate() {
		return pfRemitDate;
	}
	public void setPfRemitDate(String pfRemitDate) {
		this.pfRemitDate = pfRemitDate;
	}
	public double getPfTotal() {
		return pfTotal;
	}
	public void setPfTotal(double pfTotal) {
		this.pfTotal = pfTotal;
	}
	public String getSuppliInspCharges() {
		return suppliInspCharges;
	}
	public void setSuppliInspCharges(String suppliInspCharges) {
		this.suppliInspCharges = suppliInspCharges;
	}
	public String getSuppliPc() {
		return suppliPc;
	}
	public void setSuppliPc(String suppliPc) {
		this.suppliPc = suppliPc;
	}
	public String getSuppliPf() {
		return suppliPf;
	}
	public void setSuppliPf(String suppliPf) {
		this.suppliPf = suppliPf;
	}
	public String getChqInspCharges() {
		return chqInspCharges;
	}
	public void setChqInspCharges(String chqInspCharges) {
		this.chqInspCharges = chqInspCharges;
	}
	public String getChqInspremitDate() {
		return chqInspremitDate;
	}
	public void setChqInspremitDate(String chqInspremitDate) {
		this.chqInspremitDate = chqInspremitDate;
	}
	public String getChqPc() {
		return chqPc;
	}
	public void setChqPc(String chqPc) {
		this.chqPc = chqPc;
	}
	public String getChqPcRemitDate() {
		return chqPcRemitDate;
	}
	public void setChqPcRemitDate(String chqPcRemitDate) {
		this.chqPcRemitDate = chqPcRemitDate;
	}
	public String getChqPf() {
		return chqPf;
	}
	public void setChqPf(String chqPf) {
		this.chqPf = chqPf;
	}
	public String getChqPfRemitDate() {
		return chqPfRemitDate;
	}
	public void setChqPfRemitDate(String chqPfRemitDate) {
		this.chqPfRemitDate = chqPfRemitDate;
	}
	public String getAAdvInt() {
		return aAdvInt;
	}
	public void setAAdvInt(String advInt) {
		aAdvInt = advInt;
	}
	public String getAaipc() {
		return aaipc;
	}
	public void setAaipc(String aaipc) {
		this.aaipc = aaipc;
	}
	public String getAEpf() {
		return aEpf;
	}
	public void setAEpf(String epf) {
		aEpf = epf;
	}
	public String getApc() {
		return apc;
	}
	public void setApc(String apc) {
		this.apc = apc;
	}
	public String getARefAdv() {
		return aRefAdv;
	}
	public void setARefAdv(String refAdv) {
		aRefAdv = refAdv;
	}
	public String getATotal() {
		return aTotal;
	}
	public void setATotal(String total) {
		aTotal = total;
	}
	public String getAVpf() {
		return aVpf;
	}
	public void setAVpf(String vpf) {
		aVpf = vpf;
	}
	public String getCpfAdvInt() {
		return cpfAdvInt;
	}
	public void setCpfAdvInt(String cpfAdvInt) {
		this.cpfAdvInt = cpfAdvInt;
	}
	public String getCpfEpf() {
		return cpfEpf;
	}
	public void setCpfEpf(String cpfEpf) {
		this.cpfEpf = cpfEpf;
	}
	public String getCpfRefAdv() {
		return cpfRefAdv;
	}
	public void setCpfRefAdv(String cpfRefAdv) {
		this.cpfRefAdv = cpfRefAdv;
	}
	public String getCpfTotal() {
		return cpfTotal;
	}
	public void setCpfTotal(String cpfTotal) {
		this.cpfTotal = cpfTotal;
	}
	public String getCpfVpf() {
		return cpfVpf;
	}
	public void setCpfVpf(String cpfVpf) {
		this.cpfVpf = cpfVpf;
	}
	public String getSAdvInt() {
		return sAdvInt;
	}
	public void setSAdvInt(String advInt) {
		sAdvInt = advInt;
	}
	public String getSEpf() {
		return sEpf;
	}
	public void setSEpf(String epf) {
		sEpf = epf;
	}
	public String getSpc() {
		return spc;
	}
	public void setSpc(String spc) {
		this.spc = spc;
	}
	public String getSRefAdv() {
		return sRefAdv;
	}
	public void setSRefAdv(String refAdv) {
		sRefAdv = refAdv;
	}
	public String getSTotal() {
		return sTotal;
	}
	public void setSTotal(String total) {
		sTotal = total;
	}
	public String getSVpf() {
		return sVpf;
	}
	public void setSVpf(String vpf) {
		sVpf = vpf;
	}

}
