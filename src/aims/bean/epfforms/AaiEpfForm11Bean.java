package aims.bean.epfforms;

import java.io.Serializable;
import java.util.ArrayList;

public class AaiEpfForm11Bean implements Serializable{
	
	String pensionNo="",cpfAccno="",employeeNo="",employeeName="",designation="",fhName="",dateOfBirth="",dateofJoining="";
	String airportCode="",region="",subscriptionAmt="",contributionamt="",obYear="",outStndAdv="",wetherOption="",pfID="";
	String emoluments="",emppfstatury="",empvpf="",principal="",interest="",emptotal="",interestCredited="",aaiClosingBal="",aaiIntersetCredited="",addcontri="";
	String obEmpSub="",obAAISub="",obPrincipal="",adjEmpSub="",adjAaiContr="",tnsfrOthrOrgEmpShare="",finalPayment="",closingBal="",remarks="";
	String pensinonContr="",pf="",aaiTotal="",adjCPF="",adjCPFInt="",adjPension="",adjPensionInt="",adjPF="",adjPFInt="",pfwSubscr="",pfwContr="",refAdvPFW="";
	String advanceAmnt="",adjEmpSubTotal="",adjEmpSubInterest="",finalEmpSubscr="",finalAAIContr="",highlightedColor="";
	String totEmpClosingBalance="",totAAIClosingBalance="",totLoanAmt="",totFinalsettlmentAmt="",PensionContriAdj="";
	String   empNetOBTot = "",AAINetOBTot = "",empAdjOBTot = "",AAIAdjOBTot = "",empSubTotalTot = "",emplrPFTot = "",empPFWTotalTot = "",AAIPFWTotalTot = "",empSubIntrstTot = "",AAIContriIntrstTot ="",FinalPaymentSubTot = "",FinalPaymentContrTot = "",empCLBalTot="",AAICLBalTot="";
	String  adjEmpSubAmtGrandTot  = "",adjEmpsubIntrstAmtGrandTot = "",adjEmpSubAmtTotlaGrandTot="",adjPensionAmtGrandTot = "",adjPensionIntrstGrandTot = "",adjAAIContriTotGrandTot = "",outStndAdvGrandTot = "";
	String   grandTotadvanceAmt  = "", grandTotEmpLoanAmt = "", grandTotAAILoanAmt  = "", grandTotLoanAmt = "", grandTotFinEmpAmt = "",grandTotFinAAIAmt = "",grandTotPensoinContri = "",grandtotFinalsettlmentAmt ="";
	String FreezdAdjTotal="",CurrentAdjTotal="",DiffAdjAmt="",adjEmpSubAmtFlag="",adjAaiContrAmtFlag="",pensionContriAdjAmtFalg="";
	String userName = "",computerName = "",reportChkType = "",formType = "",trackingId = "", selectedYear = "";
	String jid="",jDescription="",jPath="";
	ArrayList CBOBList=new ArrayList();
	ArrayList controlAccountSummaryList=new ArrayList();
	ArrayList controlAccountSummaryList_prev=new ArrayList();
	ArrayList form8SummaryList=new ArrayList();
	ArrayList form2SummaryList=new ArrayList();	
	ArrayList form2ASummaryList=new ArrayList();
	ArrayList crtlAccJustificationList = new ArrayList();
	public String getAddcontri() {
		return addcontri;
	}
	public void setAddcontri(String addcontri) {
		this.addcontri = addcontri;
	}
	public ArrayList getCrtlAccJustificationList() {
		return crtlAccJustificationList;
	}
	public void setCrtlAccJustificationList(ArrayList crtlAccJustificationList) {
		this.crtlAccJustificationList = crtlAccJustificationList;
	}
	public String getAAIAdjOBTot() {
		return AAIAdjOBTot;
	}
	public void setAAIAdjOBTot(String adjOBTot) {
		AAIAdjOBTot = adjOBTot;
	}
	public String getAAICLBalTot() {
		return AAICLBalTot;
	}
	public void setAAICLBalTot(String balTot) {
		AAICLBalTot = balTot;
	}
	public String getAAIContriIntrstTot() {
		return AAIContriIntrstTot;
	}
	public void setAAIContriIntrstTot(String contriIntrstTot) {
		AAIContriIntrstTot = contriIntrstTot;
	}
	public String getAAINetOBTot() {
		return AAINetOBTot;
	}
	public void setAAINetOBTot(String netOBTot) {
		AAINetOBTot = netOBTot;
	}
	public String getAAIPFWTotalTot() {
		return AAIPFWTotalTot;
	}
	public void setAAIPFWTotalTot(String totalTot) {
		AAIPFWTotalTot = totalTot;
	}
	public String getAdjAAIContriTotGrandTot() {
		return adjAAIContriTotGrandTot;
	}
	public void setAdjAAIContriTotGrandTot(String adjAAIContriTotGrandTot) {
		this.adjAAIContriTotGrandTot = adjAAIContriTotGrandTot;
	}
	public String getAdjEmpSubAmtGrandTot() {
		return adjEmpSubAmtGrandTot;
	}
	public void setAdjEmpSubAmtGrandTot(String adjEmpSubAmtGrandTot) {
		this.adjEmpSubAmtGrandTot = adjEmpSubAmtGrandTot;
	}
	public String getAdjEmpSubAmtTotlaGrandTot() {
		return adjEmpSubAmtTotlaGrandTot;
	}
	public void setAdjEmpSubAmtTotlaGrandTot(String adjEmpSubAmtTotlaGrandTot) {
		this.adjEmpSubAmtTotlaGrandTot = adjEmpSubAmtTotlaGrandTot;
	}
	public String getAdjEmpsubIntrstAmtGrandTot() {
		return adjEmpsubIntrstAmtGrandTot;
	}
	public void setAdjEmpsubIntrstAmtGrandTot(String adjEmpsubIntrstAmtGrandTot) {
		this.adjEmpsubIntrstAmtGrandTot = adjEmpsubIntrstAmtGrandTot;
	}
	public String getAdjPensionAmtGrandTot() {
		return adjPensionAmtGrandTot;
	}
	public void setAdjPensionAmtGrandTot(String adjPensionAmtGrandTot) {
		this.adjPensionAmtGrandTot = adjPensionAmtGrandTot;
	}
	public String getAdjPensionIntrstGrandTot() {
		return adjPensionIntrstGrandTot;
	}
	public void setAdjPensionIntrstGrandTot(String adjPensionIntrstGrandTot) {
		this.adjPensionIntrstGrandTot = adjPensionIntrstGrandTot;
	}
	public ArrayList getCBOBList() {
		return CBOBList;
	}
	public void setCBOBList(ArrayList list) {
		CBOBList = list;
	}
	public ArrayList getControlAccountSummaryList() {
		return controlAccountSummaryList;
	}
	public void setControlAccountSummaryList(ArrayList controlAccountSummaryList) {
		this.controlAccountSummaryList = controlAccountSummaryList;
	}
	public ArrayList getControlAccountSummaryList_prev() {
		return controlAccountSummaryList_prev;
	}
	public void setControlAccountSummaryList_prev(
			ArrayList controlAccountSummaryList_prev) {
		this.controlAccountSummaryList_prev = controlAccountSummaryList_prev;
	}
	public String getEmpAdjOBTot() {
		return empAdjOBTot;
	}
	public void setEmpAdjOBTot(String empAdjOBTot) {
		this.empAdjOBTot = empAdjOBTot;
	}
	public String getEmpCLBalTot() {
		return empCLBalTot;
	}
	public void setEmpCLBalTot(String empCLBalTot) {
		this.empCLBalTot = empCLBalTot;
	}
	public String getEmplrPFTot() {
		return emplrPFTot;
	}
	public void setEmplrPFTot(String emplrPFTot) {
		this.emplrPFTot = emplrPFTot;
	}
	public String getEmpNetOBTot() {
		return empNetOBTot;
	}
	public void setEmpNetOBTot(String empNetOBTot) {
		this.empNetOBTot = empNetOBTot;
	}
	public String getEmpPFWTotalTot() {
		return empPFWTotalTot;
	}
	public void setEmpPFWTotalTot(String empPFWTotalTot) {
		this.empPFWTotalTot = empPFWTotalTot;
	}
	public String getEmpSubIntrstTot() {
		return empSubIntrstTot;
	}
	public void setEmpSubIntrstTot(String empSubIntrstTot) {
		this.empSubIntrstTot = empSubIntrstTot;
	}
	public String getEmpSubTotalTot() {
		return empSubTotalTot;
	}
	public void setEmpSubTotalTot(String empSubTotalTot) {
		this.empSubTotalTot = empSubTotalTot;
	}
	public String getFinalPaymentContrTot() {
		return FinalPaymentContrTot;
	}
	public void setFinalPaymentContrTot(String finalPaymentContrTot) {
		FinalPaymentContrTot = finalPaymentContrTot;
	}
	public String getFinalPaymentSubTot() {
		return FinalPaymentSubTot;
	}
	public void setFinalPaymentSubTot(String finalPaymentSubTot) {
		FinalPaymentSubTot = finalPaymentSubTot;
	}
	public ArrayList getForm2SummaryList() {
		return form2SummaryList;
	}
	public void setForm2SummaryList(ArrayList form2SummaryList) {
		this.form2SummaryList = form2SummaryList;
	}
	public ArrayList getForm8SummaryList() {
		return form8SummaryList;
	}
	public void setForm8SummaryList(ArrayList form8SummaryList) {
		this.form8SummaryList = form8SummaryList;
	}
	public String getGrandTotAAILoanAmt() {
		return grandTotAAILoanAmt;
	}
	public void setGrandTotAAILoanAmt(String grandTotAAILoanAmt) {
		this.grandTotAAILoanAmt = grandTotAAILoanAmt;
	}
	public String getGrandTotadvanceAmt() {
		return grandTotadvanceAmt;
	}
	public void setGrandTotadvanceAmt(String grandTotadvanceAmt) {
		this.grandTotadvanceAmt = grandTotadvanceAmt;
	}
	public String getGrandTotEmpLoanAmt() {
		return grandTotEmpLoanAmt;
	}
	public void setGrandTotEmpLoanAmt(String grandTotEmpLoanAmt) {
		this.grandTotEmpLoanAmt = grandTotEmpLoanAmt;
	}
	public String getGrandTotFinAAIAmt() {
		return grandTotFinAAIAmt;
	}
	public void setGrandTotFinAAIAmt(String grandTotFinAAIAmt) {
		this.grandTotFinAAIAmt = grandTotFinAAIAmt;
	}
	public String getGrandtotFinalsettlmentAmt() {
		return grandtotFinalsettlmentAmt;
	}
	public void setGrandtotFinalsettlmentAmt(String grandtotFinalsettlmentAmt) {
		this.grandtotFinalsettlmentAmt = grandtotFinalsettlmentAmt;
	}
	public String getGrandTotFinEmpAmt() {
		return grandTotFinEmpAmt;
	}
	public void setGrandTotFinEmpAmt(String grandTotFinEmpAmt) {
		this.grandTotFinEmpAmt = grandTotFinEmpAmt;
	}
	public String getGrandTotLoanAmt() {
		return grandTotLoanAmt;
	}
	public void setGrandTotLoanAmt(String grandTotLoanAmt) {
		this.grandTotLoanAmt = grandTotLoanAmt;
	}
	public String getGrandTotPensoinContri() {
		return grandTotPensoinContri;
	}
	public void setGrandTotPensoinContri(String grandTotPensoinContri) {
		this.grandTotPensoinContri = grandTotPensoinContri;
	}
	public String getOutStndAdvGrandTot() {
		return outStndAdvGrandTot;
	}
	public void setOutStndAdvGrandTot(String outStndAdvGrandTot) {
		this.outStndAdvGrandTot = outStndAdvGrandTot;
	}
	public String getTotAAIClosingBalance() {
		return totAAIClosingBalance;
	}
	public void setTotAAIClosingBalance(String totAAIClosingBalance) {
		this.totAAIClosingBalance = totAAIClosingBalance;
	}
	public String getTotEmpClosingBalance() {
		return totEmpClosingBalance;
	}
	public void setTotEmpClosingBalance(String totEmpClosingBalance) {
		this.totEmpClosingBalance = totEmpClosingBalance;
	}
	public String getTotFinalsettlmentAmt() {
		return totFinalsettlmentAmt;
	}
	public void setTotFinalsettlmentAmt(String totFinalsettlmentAmt) {
		this.totFinalsettlmentAmt = totFinalsettlmentAmt;
	}
	public String getTotLoanAmt() {
		return totLoanAmt;
	}
	public void setTotLoanAmt(String totLoanAmt) {
		this.totLoanAmt = totLoanAmt;
	}
	public String getHighlightedColor() {
		return highlightedColor;
	}
	public void setHighlightedColor(String highlightedColor) {
		this.highlightedColor = highlightedColor;
	}
	public String getFinalEmpSubscr() {
		return finalEmpSubscr;
	}
	public void setFinalEmpSubscr(String finalEmpSubscr) {
		this.finalEmpSubscr = finalEmpSubscr;
	}
	public String getFinalAAIContr() {
		return finalAAIContr;
	}
	public void setFinalAAIContr(String finalAAIContr) {
		this.finalAAIContr = finalAAIContr;
	}
	
	public String getAdjEmpSubInterest() {
		return adjEmpSubInterest;
	}
	public void setAdjEmpSubInterest(String adjEmpSubInterest) {
		this.adjEmpSubInterest = adjEmpSubInterest;
	}
	public String getAdjEmpSubTotal() {
		return adjEmpSubTotal;
	}
	public void setAdjEmpSubTotal(String adjEmpSubTotal) {
		this.adjEmpSubTotal = adjEmpSubTotal;
	}
	public String getAaiClosingBal() {
		return aaiClosingBal;
	}
	public void setAaiClosingBal(String aaiClosingBal) {
		this.aaiClosingBal = aaiClosingBal;
	}
	public String getAdvanceAmnt() {
		return advanceAmnt;
	}
	public void setAdvanceAmnt(String advanceAmnt) {
		this.advanceAmnt = advanceAmnt;
	}
	public String getAaiTotal() {
		return aaiTotal;
	}
	public void setAaiTotal(String aaiTotal) {
		this.aaiTotal = aaiTotal;
	}
	public String getPensinonContr() {
		return pensinonContr;
	}
	public void setPensinonContr(String pensinonContr) {
		this.pensinonContr = pensinonContr;
	}
	public String getPf() {
		return pf;
	}
	public void setPf(String pf) {
		this.pf = pf;
	}
	public String getAdjEmpSub() {
		return adjEmpSub;
	}
	public void setAdjEmpSub(String adjEmpSub) {
		this.adjEmpSub = adjEmpSub;
	}

	public String getAirportCode() {
		return airportCode;
	}
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}
	public String getClosingBal() {
		return closingBal;
	}
	public void setClosingBal(String closingBal) {
		this.closingBal = closingBal;
	}
	public String getContributionamt() {
		return contributionamt;
	}
	public void setContributionamt(String contributionamt) {
		this.contributionamt = contributionamt;
	}
	public String getCpfAccno() {
		return cpfAccno;
	}
	public void setCpfAccno(String cpfAccno) {
		this.cpfAccno = cpfAccno;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDateofJoining() {
		return dateofJoining;
	}
	public void setDateofJoining(String dateofJoining) {
		this.dateofJoining = dateofJoining;
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
	public String getFinalPayment() {
		return finalPayment;
	}
	public void setFinalPayment(String finalPayment) {
		this.finalPayment = finalPayment;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getInterestCredited() {
		return interestCredited;
	}
	public void setInterestCredited(String interestCredited) {
		this.interestCredited = interestCredited;
	}
	public String getObEmpSub() {
		return obEmpSub;
	}
	public void setObEmpSub(String obEmpSub) {
		this.obEmpSub = obEmpSub;
	}
	public String getObYear() {
		return obYear;
	}
	public void setObYear(String obYear) {
		this.obYear = obYear;
	}

	public String getPensionNo() {
		return pensionNo;
	}
	public void setPensionNo(String pensionNo) {
		this.pensionNo = pensionNo;
	}
	public String getPfID() {
		return pfID;
	}
	public void setPfID(String pfID) {
		this.pfID = pfID;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSubscriptionAmt() {
		return subscriptionAmt;
	}
	public void setSubscriptionAmt(String subscriptionAmt) {
		this.subscriptionAmt = subscriptionAmt;
	}
	public String getTnsfrOthrOrgEmpShare() {
		return tnsfrOthrOrgEmpShare;
	}
	public void setTnsfrOthrOrgEmpShare(String tnsfrOthrOrgEmpShare) {
		this.tnsfrOthrOrgEmpShare = tnsfrOthrOrgEmpShare;
	}
	public String getWetherOption() {
		return wetherOption;
	}
	public void setWetherOption(String wetherOption) {
		this.wetherOption = wetherOption;
	}
	public String getObAAISub() {
		return obAAISub;
	}
	public void setObAAISub(String obAAISub) {
		this.obAAISub = obAAISub;
	}
	public String getOutStndAdv() {
		return outStndAdv;
	}
	public void setOutStndAdv(String outStndAdv) {
		this.outStndAdv = outStndAdv;
	}
	public String getObPrincipal() {
		return obPrincipal;
	}
	public void setObPrincipal(String obPrincipal) {
		this.obPrincipal = obPrincipal;
	}
	public String getAdjCPF() {
		return adjCPF;
	}
	public void setAdjCPF(String adjCPF) {
		this.adjCPF = adjCPF;
	}
	public String getAdjCPFInt() {
		return adjCPFInt;
	}
	public void setAdjCPFInt(String adjCPFInt) {
		this.adjCPFInt = adjCPFInt;
	}
	public String getAdjPension() {
		return adjPension;
	}
	public void setAdjPension(String adjPension) {
		this.adjPension = adjPension;
	}
	public String getAdjPensionInt() {
		return adjPensionInt;
	}
	public void setAdjPensionInt(String adjPensionInt) {
		this.adjPensionInt = adjPensionInt;
	}
	public String getAdjPF() {
		return adjPF;
	}
	public void setAdjPF(String adjPF) {
		this.adjPF = adjPF;
	}
	public String getAdjPFInt() {
		return adjPFInt;
	}
	public void setAdjPFInt(String adjPFInt) {
		this.adjPFInt = adjPFInt;
	}

	public String getPfwContr() {
		return pfwContr;
	}
	public void setPfwContr(String pfwContr) {
		this.pfwContr = pfwContr;
	}
	public String getPfwSubscr() {
		return pfwSubscr;
	}
	public void setPfwSubscr(String pfwSubscr) {
		this.pfwSubscr = pfwSubscr;
	}
	public String getRefAdvPFW() {
		return refAdvPFW;
	}
	public void setRefAdvPFW(String refAdvPFW) {
		this.refAdvPFW = refAdvPFW;
	}
	public String getAdjAaiContr() {
		return adjAaiContr;
	}
	public void setAdjAaiContr(String adjAaiContr) {
		this.adjAaiContr = adjAaiContr;
	}

	public String getAaiIntersetCredited() {
		return aaiIntersetCredited;
	}
	public void setAaiIntersetCredited(String aaiIntersetCredited) {
		this.aaiIntersetCredited = aaiIntersetCredited;
	}
	public String getComputerName() {
		return computerName;
	}
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getReportChkType() {
		return reportChkType;
	}
	public void setReportChkType(String reportChkType) {
		this.reportChkType = reportChkType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTrackingId() {
		return trackingId;
	}
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	public String getSelectedYear() {
		return selectedYear;
	}
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}
	public ArrayList getForm2ASummaryList() {
		return form2ASummaryList;
	}
	public void setForm2ASummaryList(ArrayList form2ASummaryList) {
		this.form2ASummaryList = form2ASummaryList;
	}
	public String getCurrentAdjTotal() {
		return CurrentAdjTotal;
	}
	public void setCurrentAdjTotal(String currentAdjTotal) {
		CurrentAdjTotal = currentAdjTotal;
	}
	public String getDiffAdjAmt() {
		return DiffAdjAmt;
	}
	public void setDiffAdjAmt(String diffAdjAmt) {
		DiffAdjAmt = diffAdjAmt;
	}
	public String getFreezdAdjTotal() {
		return FreezdAdjTotal;
	}
	public void setFreezdAdjTotal(String freezdAdjTotal) {
		FreezdAdjTotal = freezdAdjTotal;
	}
	public String getPensionContriAdj() {
		return PensionContriAdj;
	}
	public void setPensionContriAdj(String pensionContriAdj) {
		PensionContriAdj = pensionContriAdj;
	}
	public String getAdjEmpSubAmtFlag() {
		return adjEmpSubAmtFlag;
	}
	public void setAdjEmpSubAmtFlag(String adjEmpSubAmtFlag) {
		this.adjEmpSubAmtFlag = adjEmpSubAmtFlag;
	}
	public String getAdjAaiContrAmtFlag() {
		return adjAaiContrAmtFlag;
	}
	public void setAdjAaiContrAmtFlag(String adjAaiContrAmtFlag) {
		this.adjAaiContrAmtFlag = adjAaiContrAmtFlag;
	}
	public String getPensionContriAdjAmtFalg() {
		return pensionContriAdjAmtFalg;
	}
	public void setPensionContriAdjAmtFalg(String pensionContriAdjAmtFalg) {
		this.pensionContriAdjAmtFalg = pensionContriAdjAmtFalg;
	}
	public String getJDescription() {
		return jDescription;
	}
	public void setJDescription(String description) {
		jDescription = description;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getJPath() {
		return jPath;
	}
	public void setJPath(String path) {
		jPath = path;
	}
	 
}
