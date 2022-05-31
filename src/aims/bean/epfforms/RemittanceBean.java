package aims.bean.epfforms;

public class RemittanceBean {
	String vrNo="",vrDt="",pfRemitterBankNM="",pfRemitterBankACNo="",pcRemitterBankNM="",pcRemitterBankAcNo="";
	String salMonth="",airportcode="",region="",accountType="",vAccountNo="",newUnitCode="";
	String billRefno="",billdate="",chequenofrom="",chequenoto="",chequedt="",preparedby="",checkedby="",passedby="",receivedby="";
	public String getBillRefno() {
		return billRefno;
	}
	public void setBillRefno(String billRefno) {
		this.billRefno = billRefno;
	}
	public String getBilldate() {
		return billdate;
	}
	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}
	public String getChequenofrom() {
		return chequenofrom;
	}
	public void setChequenofrom(String chequenofrom) {
		this.chequenofrom = chequenofrom;
	}
	public String getChequenoto() {
		return chequenoto;
	}
	public void setChequenoto(String chequenoto) {
		this.chequenoto = chequenoto;
	}
	public String getChequedt() {
		return chequedt;
	}
	public void setChequedt(String chequedt) {
		this.chequedt = chequedt;
	}
	public String getPreparedby() {
		return preparedby;
	}
	public void setPreparedby(String preparedby) {
		this.preparedby = preparedby;
	}
	public String getCheckedby() {
		return checkedby;
	}
	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}
	public String getPassedby() {
		return passedby;
	}
	public void setPassedby(String passedby) {
		this.passedby = passedby;
	}
	public String getReceivedby() {
		return receivedby;
	}
	public void setReceivedby(String receivedby) {
		this.receivedby = receivedby;
	}
	
	public String getVAccountNo() {
		return vAccountNo;
	}
	public void setVAccountNo(String accountNo) {
		vAccountNo = accountNo;
	}
	public String getNewUnitCode() {
		return newUnitCode;
	}
	public void setNewUnitCode(String newUnitCode) {
		this.newUnitCode = newUnitCode;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	double pfAccretion=0,emoluments=0 ,inspectionCharges=0 ,  pensionContribution=0,pcEmoluments=0,epf3Accretion=0,epf3Pensioncontri=0;
	double epf3Emoluments=0,epf3InspectionCharges=0;
	public double getEpf3InspectionCharges() {
		return epf3InspectionCharges;
	}
	public void setEpf3InspectionCharges(double epf3InspectionCharges) {
		this.epf3InspectionCharges = epf3InspectionCharges;
	}
	String remitanceType="";
	public double getEpf3Emoluments() {
		return epf3Emoluments;
	}
	public void setEpf3Emoluments(double epf3Emoluments) {
		this.epf3Emoluments = epf3Emoluments;
	}
	public String getRemitanceType() {
		return remitanceType;
	}
	public void setRemitanceType(String remitanceType) {
		this.remitanceType = remitanceType;
	}
	public double getEpf3Accretion() {
		return epf3Accretion;
	}
	public void setEpf3Accretion(double epf3Accretion) {
		this.epf3Accretion = epf3Accretion;
	}
	public double getEpf3Pensioncontri() {
		return epf3Pensioncontri;
	}
	public void setEpf3Pensioncontri(double epf3Pensioncontri) {
		this.epf3Pensioncontri = epf3Pensioncontri;
	}
	public double getPcEmoluments() {
		return pcEmoluments;
	}
	public void setPcEmoluments(double pcEmoluments) {
		this.pcEmoluments = pcEmoluments;
	}
	int pcNoofEmployees=0,pfnoofEmployees=0;
	
	
	public int getPfnoofEmployees() {
		return pfnoofEmployees;
	}
	public void setPfnoofEmployees(int pfnoofEmployees) {
		this.pfnoofEmployees = pfnoofEmployees;
	}
	public String getSalMonth() {
		return salMonth;
	}
	public String getAirportcode() {
		return airportcode;
	}
	public void setAirportcode(String airportcode) {
		this.airportcode = airportcode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setSalMonth(String salMonth) {
		this.salMonth = salMonth;
	}
	public String getVrNo() {
		return vrNo;
	}
	public void setVrNo(String vrNo) {
		this.vrNo = vrNo;
	}
	public String getVrDt() {
		return vrDt;
	}
	public void setVrDt(String vrDt) {
		this.vrDt = vrDt;
	}
	public String getPfRemitterBankNM() {
		return pfRemitterBankNM;
	}
	public void setPfRemitterBankNM(String pfRemitterBankNM) {
		this.pfRemitterBankNM = pfRemitterBankNM;
	}
	public String getPfRemitterBankACNo() {
		return pfRemitterBankACNo;
	}
	public void setPfRemitterBankACNo(String pfRemitterBankACNo) {
		this.pfRemitterBankACNo = pfRemitterBankACNo;
	}
	public String getPcRemitterBankNM() {
		return pcRemitterBankNM;
	}
	public void setPcRemitterBankNM(String pcRemitterBankNM) {
		this.pcRemitterBankNM = pcRemitterBankNM;
	}
	public String getPcRemitterBankAcNo() {
		return pcRemitterBankAcNo;
	}
	public void setPcRemitterBankAcNo(String pcRemitterBankAcNo) {
		this.pcRemitterBankAcNo = pcRemitterBankAcNo;
	}
	public double getPfAccretion() {
		return pfAccretion;
	}
	public void setPfAccretion(double pfAccretion) {
		this.pfAccretion = pfAccretion;
	}
	
	
	public int getPcNoofEmployees() {
		return pcNoofEmployees;
	}
	public void setPcNoofEmployees(int pcNoofEmployees) {
		this.pcNoofEmployees = pcNoofEmployees;
	}
	
	public double getEmoluments() {
		return emoluments;
	}
	public void setEmoluments(double emoluments) {
		this.emoluments = emoluments;
	}
	public double getInspectionCharges() {
		return inspectionCharges;
	}
	public void setInspectionCharges(double inspectionCharges) {
		this.inspectionCharges = inspectionCharges;
	}
	
	
	public double getPensionContribution() {
		return pensionContribution;
	}
	public void setPensionContribution(double pensionContribution) {
		this.pensionContribution = pensionContribution;
	}
	
}
