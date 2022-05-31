package aims.bean.epfforms;

import java.io.Serializable;

public class AaiEpfform3Bean implements Serializable{
	String monthyear="",shnMnthYear="",emoluments="",emppfstatury="",empvpf="",principal="",interest="",emptotal="",advancePFWPaid="";
	String empNet="",aaiPF="",pfDrawn="",aaiNet="",pensionContribution="",station="",empCPF="",empNetCummulative="",aaiCummulative="";
	String pensionno="",employeeName="",employeeNo="",dateofJoining="",dateofentitle="",cpfaccno="",fhName="",dateOfBirth="",designation="",wetherOption="",pfID="",pf="";
	String remarks="",subscriptionTotal="",contributionTotal="",region="",totalSubscribers="",highlightedColor="",formDifference="";
	int epf3PfNoofEmployees=0,epf3PcNoofEmployees=0,ncpDays=0,sapempno =0;
	String nonCPFEmoluments="",nonCPFemppfstatury="",nonCPFPC="",nonCPFPF="",seperationDate="",seperationreason="",originalEmoluments="";
	String pensionContriEmoluments="",accountType="",vAccountNo="",arrearflag="",gender="",fhflag="",newunitcode="",otherOrgSubTotal = "",paidDate="",uploaddate="",pensionAge="",daysBymonth="",recoveryStatus="",NCPDays="";
	String edliDateOfBirth="",edliDateOfJoining="",type="",regSalFlag="",additionalContri="",freshOption="",nonCpfAddcontri="",empCode="",basicRate="",darate="",basic="",currentDesg="",emplevel="",discipline="";
	String uanno="",gross="",newEMpCode="";
	public String getGross() {
		return gross;
	}

	public void setGross(String gross) {
		this.gross = gross;
	}

	public String getNewEMpCode() {
		return newEMpCode;
	}

	public void setNewEMpCode(String newEMpCode) {
		this.newEMpCode = newEMpCode;
	}

	public String getDiscipline() {
		return discipline;
	}

	public String getUanno() {
		return uanno;
	}

	public void setUanno(String uanno) {
		this.uanno = uanno;
	}

	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	public String getEmplevel() {
		return emplevel;
	}

	public void setEmplevel(String emplevel) {
		this.emplevel = emplevel;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getCurrentDesg() {
		return currentDesg;
	}

	public void setCurrentDesg(String currentDesg) {
		this.currentDesg = currentDesg;
	}

	public String getDarate() {
		return darate;
	}

	public void setDarate(String darate) {
		this.darate = darate;
	}

	public String getBasicRate() {
		return basicRate;
	}

	public void setBasicRate(String basicRate) {
		this.basicRate = basicRate;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getNonCpfAddcontri() {
		return nonCpfAddcontri;
	}

	public void setNonCpfAddcontri(String nonCpfAddcontri) {
		this.nonCpfAddcontri = nonCpfAddcontri;
	}

	public String getFreshOption() {
		return freshOption;
	}

	public void setFreshOption(String freshOption) {
		this.freshOption = freshOption;
	}

	public String getvAccountNo() {
		return vAccountNo;
	}

	public void setvAccountNo(String vAccountNo) {
		this.vAccountNo = vAccountNo;
	}

	public String getAdditionalContri() {
		return additionalContri;
	}

	public void setAdditionalContri(String additionalContri) {
		this.additionalContri = additionalContri;
	}

	public String getEdliDateOfBirth() {
		return edliDateOfBirth;
	}

	public void setEdliDateOfBirth(String edliDateOfBirth) {
		this.edliDateOfBirth = edliDateOfBirth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEdliDateOfJoining() {
		return edliDateOfJoining;
	}

	public void setEdliDateOfJoining(String edliDateOfJoining) {
		this.edliDateOfJoining = edliDateOfJoining;
	}

	public String getNewunitcode() {
		return newunitcode;
	}

	public String getSeperationDate() {
		return seperationDate;
	}

	public void setSeperationDate(String seperationDate) {
		this.seperationDate = seperationDate;
	}

	public String getSeperationreason() {
		return seperationreason;
	}

	public void setSeperationreason(String seperationreason) {
		this.seperationreason = seperationreason;
	}

	public String getNonCPFEmoluments() {
		return nonCPFEmoluments;
	}

	public void setNonCPFEmoluments(String nonCPFEmoluments) {
		this.nonCPFEmoluments = nonCPFEmoluments;
	}

	public String getNonCPFemppfstatury() {
		return nonCPFemppfstatury;
	}

	public void setNonCPFemppfstatury(String nonCPFemppfstatury) {
		this.nonCPFemppfstatury = nonCPFemppfstatury;
	}

	public String getNonCPFPC() {
		return nonCPFPC;
	}

	public void setNonCPFPC(String nonCPFPC) {
		this.nonCPFPC = nonCPFPC;
	}

	public String getNonCPFPF() {
		return nonCPFPF;
	}

	public void setNonCPFPF(String nonCPFPF) {
		this.nonCPFPF = nonCPFPF;
	}

	public String getFhflag() {
		return fhflag;
	}

	public String getDateofentitle() {
		return dateofentitle;
	}

	public void setDateofentitle(String dateofentitle) {
		this.dateofentitle = dateofentitle;
	}

	public void setFhflag(String fhflag) {
		this.fhflag = fhflag;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setNewunitcode(String newunitcode) {
		this.newunitcode = newunitcode;
	}

	public String getVAccountNo() {
		return vAccountNo;
	}

	public String getArrearflag() {
		return arrearflag;
	}

	public void setArrearflag(String arrearflag) {
		this.arrearflag = arrearflag;
	}

	public void setVAccountNo(String accountNo) {
		vAccountNo = accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getPensionContriEmoluments() {
		return pensionContriEmoluments;
	}

	public void setPensionContriEmoluments(String pensionContriEmoluments) {
		this.pensionContriEmoluments = pensionContriEmoluments;
	}
	String epf3Accretion="";
	public String getEpf3Accretion() {
		return epf3Accretion;
	}

	public void setEpf3Accretion(String epf3Accretion) {
		this.epf3Accretion = epf3Accretion;
	}

	public int getEpf3PcNoofEmployees() {
		return epf3PcNoofEmployees;
	}

	public void setEpf3PcNoofEmployees(int epf3PcNoofEmployees) {
		this.epf3PcNoofEmployees = epf3PcNoofEmployees;
	}
	double totalEmoluments=0,totalInspectioncharges=0,totalPcnoofemployees=0,totalPensioncontribution=0;
	double totalPF=0;
	public double getTotalPF() {
		return totalPF;
	}

	public void setTotalPF(double totalPF) {
		this.totalPF = totalPF;
	}

	public double getTotalEmoluments() {
		return totalEmoluments;
	}

	public void setTotalEmoluments(double totalEmoluments) {
		this.totalEmoluments = totalEmoluments;
	}

	public double getTotalInspectioncharges() {
		return totalInspectioncharges;
	}

	public void setTotalInspectioncharges(double totalInspectioncharges) {
		this.totalInspectioncharges = totalInspectioncharges;
	}

	public double getTotalPcnoofemployees() {
		return totalPcnoofemployees;
	}

	public void setTotalPcnoofemployees(double totalPcnoofemployees) {
		this.totalPcnoofemployees = totalPcnoofemployees;
	}

	public double getTotalPensioncontribution() {
		return totalPensioncontribution;
	}

	public void setTotalPensioncontribution(double totalPensioncontribution) {
		this.totalPensioncontribution = totalPensioncontribution;
	}

	
	
	public int getEpf3PfNoofEmployees() {
		return epf3PfNoofEmployees;
	}

	public void setEpf3PfNoofEmployees(int epf3PfNoofEmployees) {
		this.epf3PfNoofEmployees = epf3PfNoofEmployees;
	}
	public String getFormDifference() {
		return formDifference;
	}
	public void setFormDifference(String formDifference) {
		this.formDifference = formDifference;
	}
	public String getTotalSubscribers() {
		return totalSubscribers;
	}
	public void setTotalSubscribers(String totalSubscribers) {
		this.totalSubscribers = totalSubscribers;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSubscriptionTotal() {
		return subscriptionTotal;
	}
	public void setSubscriptionTotal(String subscriptionTotal) {
		this.subscriptionTotal = subscriptionTotal;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAaiCummulative() {
		return aaiCummulative;
	}
	public void setAaiCummulative(String aaiCummulative) {
		this.aaiCummulative = aaiCummulative;
	}
	public String getAaiNet() {
		return aaiNet;
	}
	public void setAaiNet(String aaiNet) {
		this.aaiNet = aaiNet;
	}
	public String getAaiPF() {
		return aaiPF;
	}
	public void setAaiPF(String aaiPF) {
		this.aaiPF = aaiPF;
	}
	public String getAdvancePFWPaid() {
		return advancePFWPaid;
	}
	public void setAdvancePFWPaid(String advancePFWPaid) {
		this.advancePFWPaid = advancePFWPaid;
	}
	public String getCpfaccno() {
		return cpfaccno;
	}
	public void setCpfaccno(String cpfaccno) {
		this.cpfaccno = cpfaccno;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getEmoluments() {
		return emoluments;
	}
	public void setEmoluments(String emoluments) {
		this.emoluments = emoluments;
	}
	public String getEmpCPF() {
		return empCPF;
	}
	public void setEmpCPF(String empCPF) {
		this.empCPF = empCPF;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public String getEmpNet() {
		return empNet;
	}
	public void setEmpNet(String empNet) {
		this.empNet = empNet;
	}
	public String getEmpNetCummulative() {
		return empNetCummulative;
	}
	public void setEmpNetCummulative(String empNetCummulative) {
		this.empNetCummulative = empNetCummulative;
	}
	public String getEmppfstatury() {
		return emppfstatury;
	}
	public void setEmppfstatury(String emppfstatury) {
		this.emppfstatury = emppfstatury;
	}
	public String getEmptotal() {
		return emptotal;
	}
	public void setEmptotal(String emptotal) {
		this.emptotal = emptotal;
	}
	public String getEmpvpf() {
		return empvpf;
	}
	public void setEmpvpf(String empvpf) {
		this.empvpf = empvpf;
	}
	public String getFhName() {
		return fhName;
	}
	public void setFhName(String fhName) {
		this.fhName = fhName;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getMonthyear() {
		return monthyear;
	}
	public void setMonthyear(String monthyear) {
		this.monthyear = monthyear;
	}
	public String getPensionContribution() {
		return pensionContribution;
	}
	public void setPensionContribution(String pensionContribution) {
		this.pensionContribution = pensionContribution;
	}
	public String getPensionno() {
		return pensionno;
	}
	public void setPensionno(String pensionno) {
		this.pensionno = pensionno;
	}
	public String getPfDrawn() {
		return pfDrawn;
	}
	public void setPfDrawn(String pfDrawn) {
		this.pfDrawn = pfDrawn;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getShnMnthYear() {
		return shnMnthYear;
	}
	public void setShnMnthYear(String shnMnthYear) {
		this.shnMnthYear = shnMnthYear;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getWetherOption() {
		return wetherOption;
	}
	public void setWetherOption(String wetherOption) {
		this.wetherOption = wetherOption;
	}
	public String getPfID() {
		return pfID;
	}
	public void setPfID(String pfID) {
		this.pfID = pfID;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getContributionTotal() {
		return contributionTotal;
	}
	public void setContributionTotal(String contributionTotal) {
		this.contributionTotal = contributionTotal;
	}
	public String getHighlightedColor() {
		return highlightedColor;
	}
	public void setHighlightedColor(String highlightedColor) {
		this.highlightedColor = highlightedColor;
	}
	public String getDateofJoining() {
		return dateofJoining;
	}
	public void setDateofJoining(String dateofJoining) {
		this.dateofJoining = dateofJoining;
	}

	public String getOtherOrgSubTotal() {
		return otherOrgSubTotal;
	}

	public void setOtherOrgSubTotal(String otherOrgSubTotal) {
		this.otherOrgSubTotal = otherOrgSubTotal;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getUploaddate() {
		return uploaddate;
	}

	public void setUploaddate(String uploaddate) {
		this.uploaddate = uploaddate;
	}

	public String getPensionAge() {
		return pensionAge;
	}

	public void setPensionAge(String pensionAge) {
		this.pensionAge = pensionAge;
	}

	public String getDaysBymonth() {
		return daysBymonth;
	}

	public void setDaysBymonth(String daysBymonth) {
		this.daysBymonth = daysBymonth;
	}

	public String getRecoveryStatus() {
		return recoveryStatus;
	}

	public void setRecoveryStatus(String recoveryStatus) {
		this.recoveryStatus = recoveryStatus;
	}

	public String getNCPDays() {
		return NCPDays;
	}

	public void setNCPDays(String days) {
		NCPDays = days;
	}

	public String getOriginalEmoluments() {
		return originalEmoluments;
	}

	public void setOriginalEmoluments(String originalEmoluments) {
		this.originalEmoluments = originalEmoluments;
	}

	public String getRegSalFlag() {
		return regSalFlag;
	}

	public void setRegSalFlag(String regSalFlag) {
		this.regSalFlag = regSalFlag;
	}

	public int getNcpDays() {
		return ncpDays;
	}

	public void setNcpDays(int ncpDays) {
		this.ncpDays = ncpDays;
	}

}
