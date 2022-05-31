package aims.bean.cashbook;

import java.io.Serializable;

public class PartyInfo implements Serializable{
	
	private String partyName;
	private String partyDetail;
	private String faxNo;
	private String emailId;
	private String mobileNo;
	private String enteredBy; 
	private String enteredDate;
	private String contactNo;
	private BankMasterInfo bankInfo;
	
	public BankMasterInfo getBankInfo() {
		return bankInfo;
	}
	public void setBankInfo(BankMasterInfo bankInfo) {
		this.bankInfo = bankInfo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getEnteredDate() {
		return enteredDate;
	}
	public void setEnteredDate(String enteredDate) {
		this.enteredDate = enteredDate;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPartyDetail() {
		return partyDetail;
	}
	public void setPartyDetail(String partyDetail) {
		this.partyDetail = partyDetail;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
}
