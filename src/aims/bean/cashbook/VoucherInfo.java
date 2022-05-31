package aims.bean.cashbook;

import java.io.Serializable;
import java.util.List;

public class VoucherInfo implements Serializable{
	
	private String keyNo ;
	private String bankName;
	private String accountNo;
	private String finYear;
	private String trustType;
	private String voucherType;
	private String partyType;
	private String empPartyCode;	
	private String partyDetails;
	private String voucherNo;
	private String preparedBy;
	private String checkedBy;
	private String approvedBy;
	private String enteredBy; 
	private String enteredDate;
	private List voucherDetails;
	private String voucherDt;
	private String details;	
	private String status;
	private String fromDate;
	private String toDate;
	private String preparedDt;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getCheckedBy() {
		return checkedBy;
	}
	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getEmpPartyCode() {
		return empPartyCode;
	}
	public void setEmpPartyCode(String empPartyCode) {
		this.empPartyCode = empPartyCode;
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
	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public String getKeyNo() {
		return keyNo;
	}
	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}
	public String getPartyType() {
		return partyType;
	}
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}
	public String getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public String getTrustType() {
		return trustType;
	}
	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public List getVoucherDetails() {
		return voucherDetails;
	}
	public void setVoucherDetails(List voucherDetails) {
		this.voucherDetails = voucherDetails;
	}
	public String getPartyDetails() {
		return partyDetails;
	}
	public void setPartyDetails(String partyDetails) {
		this.partyDetails = partyDetails;
	}
	public String getVoucherDt() {
		return voucherDt;
	}
	public void setVoucherDt(String voucherDt) {
		this.voucherDt = voucherDt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPreparedDt() {
		return preparedDt;
	}
	public void setPreparedDt(String preparedDt) {
		this.preparedDt = preparedDt;
	}
}
