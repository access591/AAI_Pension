/**
 * File       : EmployeePersonalInfo.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */

package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class EmployeePersonalInfo implements Serializable{

	private String employeeNumber="",newEmployeeNumber="",employeeName="",airportCode="",region="",cpfAccno="",designation="",empDesLevel="",refPensionNumber="";
	private String recordVerified="",chkEmpNameFlag="false",pensionNo="",fhName="",seperationReason="",seperationDate="",emailID="",empNomineeSharable="";
	private String wetherOption="",dateOfBirth="",dateOfJoining="",remarks="",gender="",lastActive="",division="",maritalStatus="",otherReason="",department="",status="";
	private String perAddress="",tempAddress="",form2Nominee="",airportSerialNumber="",dateOfEntitle="",nationality="",heightWithInches="";
	private String dateOfAnnuation="",pfID="",fhFlag="",pensionInfo="",finalSettlementDate="",seperationFromDate="",lastatteained="";
	private String transferFlag="",oldPensionNo="",pfIDString="",whetherOptionDescr="",mappingFlag="",ageAtEntry="",basicWages="";
	private String resttlmentDate="",phoneNumber="",chkArrearFlag="",chkPaymntSprtinDtFlag="N";
	private String  adjAmount="",adjInt="",adjDate="",adjRemarks="",annuationfromdate="",annuationtodate="",frozedSeperationDate="";
	private String fromarrearreviseddate="",toarrearreviseddate="",chkarrearAdj="",monthyear="",arreatBreakupData="",calcPCUptoDate="",calcPCFromDate="";
	private String seperationReason_PC="",seperationDate_PC="",resettlementFlag="";
	private int age=0,frozedSeperationInterest=0,frozedSeperationArrearInt=0;
	private double currentage=0.00,chksepmnths=0.00;
	private String  arrearRevisedFlag="",prevMntCnt="",curntMntCnt="",prevMnth="",curntMnth="",transferInCnt="",transferOutCnt="";
	private boolean arreerintflag=false,frozedSeperationAvail=false;
	private boolean isObInterst=false,crossfinyear=false,isNoInterest=false,isAdjObInterst=false,afterFSInt=false;
	private String frmName="",pfidProcessStatus="",processID="",verifiedBy="",fileName="",transDt="",enteredBy="",recomendBy="",approvedBy="";
	private String appDate="";
	private String uanno="";
	private boolean freshOPFalg=false;
	private String freshPensionOption="";
	private String pensiontype="",dostochq="",datetoCHQHR="",datetoCHQFIN="",rpfcSubmissionDate="",rpfcReturnDate="";	
	private String chqHrRemarks="",chqFinRemarks="",rpfcSubmisionRemarks="",rpfcReturnRemarks="",userName="",ipAddress="";
	private String pfcardSpecialRemarks="",optiona="",a2a="",a2b="",doer="",finyear="",mnthsFlag="";
	private String freshPensionOptionDescr="",pfwdisableFlag="",dateOfSaparation="";
	private int count;
	private String deferment="",defermentOption="",deferementAge="";
	
	private String cad_flag="",cadOption="";
	




	public String getCadOption() {
		return cadOption;
	}
	public void setCadOption(String cadOption) {
		this.cadOption = cadOption;
	}
	public String getDeferementAge() {
		return deferementAge;
	}
	public void setDeferementAge(String deferementAge) {
		this.deferementAge = deferementAge;
	}
	public String getCad_flag() {
		return cad_flag;
	}
	public void setCad_flag(String cadFlag) {
		cad_flag = cadFlag;
	}
	public String getDeferment() {
		return deferment;
	}
	public void setDeferment(String deferment) {
		this.deferment = deferment;
	}
	public String getDefermentOption() {
		return defermentOption;
	}
	public void setDefermentOption(String defermentOption) {
		this.defermentOption = defermentOption;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDateOfSaparation() {
		return dateOfSaparation;
	}
	public void setDateOfSaparation(String dateOfSaparation) {
		this.dateOfSaparation = dateOfSaparation;
	}
	public String getPfwdisableFlag() {
		return pfwdisableFlag;
	}
	public void setPfwdisableFlag(String pfwdisableFlag) {
		this.pfwdisableFlag = pfwdisableFlag;
	}
	public String getFreshPensionOptionDescr() {
		return freshPensionOptionDescr;
	}
	public void setFreshPensionOptionDescr(String freshPensionOptionDescr) {
		this.freshPensionOptionDescr = freshPensionOptionDescr;
	}
	public String getA2a() {
		return a2a;
	}
	public void setA2a(String a2a) {
		this.a2a = a2a;
	}
	public String getA2b() {
		return a2b;
	}
	public void setA2b(String a2b) {
		this.a2b = a2b;
	}
	public String getOptiona() {
		return optiona;
	}
	public void setOptiona(String optiona) {
		this.optiona = optiona;
	}
	public String getPfcardSpecialRemarks() {
		return pfcardSpecialRemarks;
	}
	public void setPfcardSpecialRemarks(String pfcardSpecialRemarks) {
		this.pfcardSpecialRemarks = pfcardSpecialRemarks;
	}
	public String getFreshPensionOption() {
		return freshPensionOption;
	}
	public void setFreshPensionOption(String freshPensionOption) {
		this.freshPensionOption = freshPensionOption;
	}
	public boolean isFreshOPFalg() {
		return freshOPFalg;
	}
	public void setFreshOPFalg(boolean freshOPFalg) {
		this.freshOPFalg = freshOPFalg;
	}
	public String getFrmName() {
		return frmName;
	}
	public void setFrmName(String frmName) {
		this.frmName = frmName;
	}
	public String getPfidProcessStatus() {
		return pfidProcessStatus;
	}
	public void setPfidProcessStatus(String pfidProcessStatus) {
		this.pfidProcessStatus = pfidProcessStatus;
	}
	public String getProcessID() {
		return processID;
	}
	public void setProcessID(String processID) {
		this.processID = processID;
	}
	public String getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	public String getChkPaymntSprtinDtFlag() {
		return chkPaymntSprtinDtFlag;
	}
	public void setChkPaymntSprtinDtFlag(String chkPaymntSprtinDtFlag) {
		this.chkPaymntSprtinDtFlag = chkPaymntSprtinDtFlag;
	}
	public int getFrozedSeperationArrearInt() {
		return frozedSeperationArrearInt;
	}
	public void setFrozedSeperationArrearInt(int frozedSeperationArrearInt) {
		this.frozedSeperationArrearInt = frozedSeperationArrearInt;
	}
	public int getFrozedSeperationInterest() {
		return frozedSeperationInterest;
	}
	public void setFrozedSeperationInterest(int frozedSeperationInterest) {
		this.frozedSeperationInterest = frozedSeperationInterest;
	}

	public boolean isFrozedSeperationAvail() {
		return frozedSeperationAvail;
	}
	public void setFrozedSeperationAvail(boolean frozedSeperationAvail) {
		this.frozedSeperationAvail = frozedSeperationAvail;
	}
	public String getFrozedSeperationDate() {
		return frozedSeperationDate;
	}
	public void setFrozedSeperationDate(String frozedSeperationDate) {
		this.frozedSeperationDate = frozedSeperationDate;
	}
	public boolean isAdjObInterst() {
		return isAdjObInterst;
	}
	public void setAdjObInterst(boolean isAdjObInterst) {
		this.isAdjObInterst = isAdjObInterst;
	}
	public boolean isNoInterest() {
		return isNoInterest;
	}
	public void setNoInterest(boolean isNoInterest) {
		this.isNoInterest = isNoInterest;
	}
	public boolean isObInterst() {
		return isObInterst;
	}
	public void setObInterst(boolean isObInterst) {
		this.isObInterst = isObInterst;
	}
	public boolean isArreerintflag() {
		return arreerintflag;
	}
	public void setArreerintflag(boolean arreerintflag) {
		this.arreerintflag = arreerintflag;
	}
	public String getArrearRevisedFlag() {
		return arrearRevisedFlag;
	}
	public void setArrearRevisedFlag(String arrearRevisedFlag) {
		this.arrearRevisedFlag = arrearRevisedFlag;
	}
	public String getArreatBreakupData() {
		return arreatBreakupData;
	}
	public void setArreatBreakupData(String arreatBreakupData) {
		this.arreatBreakupData = arreatBreakupData;
	}
	public double getCurrentage() {
		return currentage;
	}
	public void setCurrentage(double currentage) {
		this.currentage = currentage;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAnnuationfromdate() {
		return annuationfromdate;
	}
	public void setAnnuationfromdate(String annuationfromdate) {
		this.annuationfromdate = annuationfromdate;
	}
	public String getAnnuationtodate() {
		return annuationtodate;
	}
	public void setAnnuationtodate(String annuationtodate) {
		this.annuationtodate = annuationtodate;
	}
	public String getChkarrearAdj() {
		return chkarrearAdj;
	}
	public void setChkarrearAdj(String chkarrearAdj) {
		this.chkarrearAdj = chkarrearAdj;
	}
	public String getFromarrearreviseddate() {
		return fromarrearreviseddate;
	}
	public void setFromarrearreviseddate(String fromarrearreviseddate) {
		this.fromarrearreviseddate = fromarrearreviseddate;
	}
	public String getToarrearreviseddate() {
		return toarrearreviseddate;
	}
	public void setToarrearreviseddate(String toarrearreviseddate) {
		this.toarrearreviseddate = toarrearreviseddate;
	}
	public String getMonthyear() {
		return monthyear;
	}
	public void setMonthyear(String monthyear) {
		this.monthyear = monthyear;
	}
	public String getAdjRemarks() {
		return adjRemarks;
	}
	public void setAdjRemarks(String adjRemarks) {
		this.adjRemarks = adjRemarks;
	}
	public String getAdjAmount() {
		return adjAmount;
	}
	public void setAdjAmount(String adjAmount) {
		this.adjAmount = adjAmount;
	}
	public String getAdjInt() {
		return adjInt;
	}
	public void setAdjInt(String adjInt) {
		this.adjInt = adjInt;
	}
	public String getAdjDate() {
		return adjDate;
	}
	public void setAdjDate(String adjDate) {
		this.adjDate = adjDate;
	}
	ArrayList nomineeList=new ArrayList();
   
	public ArrayList getNomineeList() {
		return nomineeList;
	}
	public void setNomineeList(ArrayList nomineeList) {
		this.nomineeList = nomineeList;
	}
	public String getChkArrearFlag() {
		return chkArrearFlag;
	}
	public void setChkArrearFlag(String chkArrearFlag) {
		this.chkArrearFlag = chkArrearFlag;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getResttlmentDate() {
		return resttlmentDate;
	}
	public void setResttlmentDate(String resttlmentDate) {
		this.resttlmentDate = resttlmentDate;
	}
	ArrayList pensionList=new ArrayList();

	public ArrayList getPensionList() {
		return pensionList;
	}

	public String getAgeAtEntry() {
		return ageAtEntry;
	}

	public void setAgeAtEntry(String ageAtEntry) {
		this.ageAtEntry = ageAtEntry;
	}

	public String getBasicWages() {
		return basicWages;
	}

	public void setBasicWages(String basicWages) {
		this.basicWages = basicWages;
	}

	public String getMappingFlag() {
		return mappingFlag;
	}

	public void setMappingFlag(String mappingFlag) {
		this.mappingFlag = mappingFlag;
	}

	public void setPensionList(ArrayList pensionList) {
		this.pensionList = pensionList;
	}

	public String getTransferFlag() {
		return transferFlag;
	}

	public void setTransferFlag(String transferFlag) {
		this.transferFlag = transferFlag;
	}

	public String getFhFlag() {
		return fhFlag;
	}

	public void setFhFlag(String fhFlag) {
		this.fhFlag = fhFlag;
	}

	public String getPfID() {
        return pfID;
    }

    public void setPfID(String pfID) {
        this.pfID = pfID;
    }

    public String getDateOfAnnuation() {
		return dateOfAnnuation;
	}

	public void setDateOfAnnuation(String dateOfAnnuation) {
		this.dateOfAnnuation = dateOfAnnuation;
	}

	public String getAirportSerialNumber() {
		return airportSerialNumber;
	}

	public void setAirportSerialNumber(String airportSerialNumber) {
		this.airportSerialNumber = airportSerialNumber;
	}

	public String getForm2Nominee() {
		return form2Nominee;
	}

	public void setForm2Nominee(String form2Nominee) {
		this.form2Nominee = form2Nominee;
	}

	public String getPerAddress() {
		return perAddress;
	}

	public void setPerAddress(String perAddress) {
		this.perAddress = perAddress;
	}

	public String getTempAddress() {
		return tempAddress;
	}

	public void setTempAddress(String tempAddress) {
		this.tempAddress = tempAddress;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getEmpDesLevel() {
		return empDesLevel;
	}

	public void setEmpDesLevel(String empDesLevel) {
		this.empDesLevel = empDesLevel;
	}

	public String getEmpNomineeSharable() {
		return empNomineeSharable;
	}

	public void setEmpNomineeSharable(String empNomineeSharable) {
		this.empNomineeSharable = empNomineeSharable;
	}

	public String getFhName() {
		return fhName;
	}

	public void setFhName(String fhName) {
		this.fhName = fhName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLastActive() {
		return lastActive;
	}

	public void setLastActive(String lastActive) {
		this.lastActive = lastActive;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getOtherReason() {
		return otherReason;
	}

	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}

	public String getRefPensionNumber() {
		return refPensionNumber;
	}

	public void setRefPensionNumber(String refPensionNumber) {
		this.refPensionNumber = refPensionNumber;
	}

	public String getSeperationDate() {
		return seperationDate;
	}

	public void setSeperationDate(String seperationDate) {
		this.seperationDate = seperationDate;
	}

	public String getSeperationReason() {
		return seperationReason;
	}

	public void setSeperationReason(String seperationReason) {
		this.seperationReason = seperationReason;
	}

	public String getPensionNo() {
		return pensionNo;
	}

	public void setPensionNo(String pensionNo) {
		this.pensionNo = pensionNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getWetherOption() {
		return wetherOption;
	}

	public void setWetherOption(String wetherOption) {
		this.wetherOption = wetherOption;
	}

	public String getChkEmpNameFlag() {
		return chkEmpNameFlag;
	}

	public void setChkEmpNameFlag(String chkEmpNameFlag) {
		this.chkEmpNameFlag = chkEmpNameFlag;
	}

	public String getRecordVerified() {
		return recordVerified;
	}

	public void setRecordVerified(String recordVerified) {
		this.recordVerified = recordVerified;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getCpfAccno() {
		return cpfAccno;
	}

	public void setCpfAccno(String cpfAccno) {
		this.cpfAccno = cpfAccno;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

    public String getOldPensionNo() {
        return oldPensionNo;
    }

    public void setOldPensionNo(String oldPensionNo) {
        this.oldPensionNo = oldPensionNo;
    }

    public String getPfIDString() {
        return pfIDString;
    }

    public void setPfIDString(String pfIDString) {
        this.pfIDString = pfIDString;
    }

    public String getWhetherOptionDescr() {
        return whetherOptionDescr;
    }

    public void setWhetherOptionDescr(String whetherOptionDescr) {
        this.whetherOptionDescr = whetherOptionDescr;
    }

	public String getPensionInfo() {
		return pensionInfo;
	}

	public void setPensionInfo(String pensionInfo) {
		this.pensionInfo = pensionInfo;
	}

	public String getDateOfEntitle() {
		return dateOfEntitle;
	}

	public void setDateOfEntitle(String dateOfEntitle) {
		this.dateOfEntitle = dateOfEntitle;
	}

	public String getFinalSettlementDate() {
		return finalSettlementDate;
	}

	public void setFinalSettlementDate(String finalSettlementDate) {
		this.finalSettlementDate = finalSettlementDate;
	}

	public String getSeperationFromDate() {
		return seperationFromDate;
	}

	public void setSeperationFromDate(String seperationFromDate) {
		this.seperationFromDate = seperationFromDate;
	}

	public String getHeightWithInches() {
		return heightWithInches;
	}

	public void setHeightWithInches(String heightWithInches) {
		this.heightWithInches = heightWithInches;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public boolean isCrossfinyear() {
		return crossfinyear;
	}
	public void setCrossfinyear(boolean crossfinyear) {
		this.crossfinyear = crossfinyear;
	}
	public String getCurntMntCnt() {
		return curntMntCnt;
	}
	public void setCurntMntCnt(String curntMntCnt) {
		this.curntMntCnt = curntMntCnt;
	}
	public String getCurntMnth() {
		return curntMnth;
	}
	public void setCurntMnth(String curntMnth) {
		this.curntMnth = curntMnth;
	}
	public String getPrevMntCnt() {
		return prevMntCnt;
	}
	public void setPrevMntCnt(String prevMntCnt) {
		this.prevMntCnt = prevMntCnt;
	}
	public String getPrevMnth() {
		return prevMnth;
	}
	public void setPrevMnth(String prevMnth) {
		this.prevMnth = prevMnth;
	}
	public String getTransferInCnt() {
		return transferInCnt;
	}
	public void setTransferInCnt(String transferInCnt) {
		this.transferInCnt = transferInCnt;
	}
	public String getTransferOutCnt() {
		return transferOutCnt;
	}
	public void setTransferOutCnt(String transferOutCnt) {
		this.transferOutCnt = transferOutCnt;
	}
	public String getCalcPCUptoDate() {
		return calcPCUptoDate;
	}
	public void setCalcPCUptoDate(String calcPCUptoDate) {
		this.calcPCUptoDate = calcPCUptoDate;
	}
	public String getSeperationDate_PC() {
		return seperationDate_PC;
	}
	public void setSeperationDate_PC(String seperationDate_PC) {
		this.seperationDate_PC = seperationDate_PC;
	}
	public String getSeperationReason_PC() {
		return seperationReason_PC;
	}
	public void setSeperationReason_PC(String seperationReason_PC) {
		this.seperationReason_PC = seperationReason_PC;
	}
	public String getCalcPCFromDate() {
		return calcPCFromDate;
	}
	public void setCalcPCFromDate(String calcPCFromDate) {
		this.calcPCFromDate = calcPCFromDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getTransDt() {
		return transDt;
	}
	public void setTransDt(String transDt) {
		this.transDt = transDt;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}	 
	public String getRecomendBy() {
		return recomendBy;
	}
	public void setRecomendBy(String recomendBy) {
		this.recomendBy = recomendBy;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLastatteained() {
		return lastatteained;
	}
	public void setLastatteained(String lastatteained) {
		this.lastatteained = lastatteained;
	}
	public boolean isAfterFSInt() {
		return afterFSInt;
	}
	public void setAfterFSInt(boolean afterFSInt) {
		this.afterFSInt = afterFSInt;
	}
	public String getNewEmployeeNumber() {
		return newEmployeeNumber;
	}
	public void setNewEmployeeNumber(String newEmployeeNumber) {
		this.newEmployeeNumber = newEmployeeNumber;
	}
	public double getChksepmnths() {
		return chksepmnths;
	}
	public void setChksepmnths(double chksepmnths) {
		this.chksepmnths = chksepmnths;
	}
	public String getResettlementFlag() {
		return resettlementFlag;
	}
	public void setResettlementFlag(String resettlementFlag) {
		this.resettlementFlag = resettlementFlag;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getDostochq() {
		return dostochq;
	}
	public void setDostochq(String dostochq) {
		this.dostochq = dostochq;
	}
	public String getPensiontype() {
		return pensiontype;
	}
	public void setPensiontype(String pensiontype) {
		this.pensiontype = pensiontype;
	}
	public String getChqFinRemarks() {
		return chqFinRemarks;
	}
	public void setChqFinRemarks(String chqFinRemarks) {
		this.chqFinRemarks = chqFinRemarks;
	}
	public String getChqHrRemarks() {
		return chqHrRemarks;
	}
	public void setChqHrRemarks(String chqHrRemarks) {
		this.chqHrRemarks = chqHrRemarks;
	}
	public String getDatetoCHQFIN() {
		return datetoCHQFIN;
	}
	public void setDatetoCHQFIN(String datetoCHQFIN) {
		this.datetoCHQFIN = datetoCHQFIN;
	}
	public String getDatetoCHQHR() {
		return datetoCHQHR;
	}
	public void setDatetoCHQHR(String datetoCHQHR) {
		this.datetoCHQHR = datetoCHQHR;
	}
	public String getRpfcReturnDate() {
		return rpfcReturnDate;
	}
	public void setRpfcReturnDate(String rpfcReturnDate) {
		this.rpfcReturnDate = rpfcReturnDate;
	}
	public String getRpfcReturnRemarks() {
		return rpfcReturnRemarks;
	}
	public void setRpfcReturnRemarks(String rpfcReturnRemarks) {
		this.rpfcReturnRemarks = rpfcReturnRemarks;
	}
	public String getRpfcSubmisionRemarks() {
		return rpfcSubmisionRemarks;
	}
	public void setRpfcSubmisionRemarks(String rpfcSubmisionRemarks) {
		this.rpfcSubmisionRemarks = rpfcSubmisionRemarks;
	}
	public String getRpfcSubmissionDate() {
		return rpfcSubmissionDate;
	}
	public void setRpfcSubmissionDate(String rpfcSubmissionDate) {
		this.rpfcSubmissionDate = rpfcSubmissionDate;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUanno() {
		return uanno;
	}
	public void setUanno(String uanno) {
		this.uanno = uanno;
	}
	public String getDoer() {
		return doer;
	}
	public void setDoer(String doer) {
		this.doer = doer;
	}
	public String getFinyear() {
		return finyear;
	}
	public void setFinyear(String finyear) {
		this.finyear = finyear;
	}
	public String getMnthsFlag() {
		return mnthsFlag;
	}
	public void setMnthsFlag(String mnthsFlag) {
		this.mnthsFlag = mnthsFlag;
	}
	



}
