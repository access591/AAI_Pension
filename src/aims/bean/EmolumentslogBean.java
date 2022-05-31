/**
  * File       : PensionBean.java
  * Date       : 08/07/2007
  * Author     : AIMS 
  * Description: 
  * Copyright (2007) by the Navayuga Infotech, all rights reserved.
  */

package aims.bean;

import java.util.ArrayList;
import java.util.List;

public class EmolumentslogBean {
	private String oldEmoluments = "";
	private String newEmoluments = "";
	private String oldEmppfstatury = "";
	private String newEmppfstatury = "";
	private String monthYear = "";
	private String updatedDate="";
	private String pensionNo="";
	private String region="";
	private String userName="";
	private String computerName="";
	private String cpfAcno="";
	private String oldEmpvpf ="";
	private String newEmpvpf = "";
	private String oldPrincipal = "";
	private String newPrincipal = "";
	private String processDate = "";
	private String oldInterest = "";
	private String newInterest="";
	private String oldPensioncontri="";
	private String newPensioncontri="";
	private String adjObYear="";
	private String pfId="";
	private String empContri="";
	private String aaiContri="";
	private String empContriDeviation="";
	private String aaiContriDeviation="";
	private String remarks="";
	private String adjUrl="";
    private String empinterest="";
	private String aaiinterest="";	
	private String emptotal="";
	private String aaitotal="";
	private String chkAdjoburl="";
	private String form2url="";
	private String originalRecord = "";
	private String batchId = ""; 
	private String batchTime = "";
	private String addcon = "";
	public String getAddcon() {
		return addcon;
	}
	public void setAddcon(String addcon) {
		this.addcon = addcon;
	}
	public String getForm2url() {
		return form2url;
	}
	public void setForm2url(String form2url) {
		this.form2url = form2url;
	}
	public String getAaiinterest() {
		return aaiinterest;
	}
	public void setAaiinterest(String aaiinterest) {
		this.aaiinterest = aaiinterest;
	}
	public String getAaitotal() {
		return aaitotal;
	}
	public void setAaitotal(String aaitotal) {
		this.aaitotal = aaitotal;
	}
	public String getEmpinterest() {
		return empinterest;
	}
	public void setEmpinterest(String empinterest) {
		this.empinterest = empinterest;
	}
	public String getEmptotal() {
		return emptotal;
	}
	public void setEmptotal(String emptotal) {
		this.emptotal = emptotal;
	}
	public String getAdjUrl() {
		return adjUrl;
	}
	public void setAdjUrl(String adjUrl) {
		this.adjUrl = adjUrl;
	}
	public String getAdjObYear() {
		return adjObYear;
	}
	public void setAdjObYear(String adjObYear) {
		this.adjObYear = adjObYear;
	}
	public String getPfId() {
		return pfId;
	}
	public void setPfId(String pfId) {
		this.pfId = pfId;
	}
	public String getEmpContri() {
		return empContri;
	}
	public void setEmpContri(String empContri) {
		this.empContri = empContri;
	}
	public String getAaiContri() {
		return aaiContri;
	}
	public void setAaiContri(String aaiContri) {
		this.aaiContri = aaiContri;
	}
	public String getEmpContriDeviation() {
		return empContriDeviation;
	}
	public void setEmpContriDeviation(String empContriDeviation) {
		this.empContriDeviation = empContriDeviation;
	}
	public String getAaiContriDeviation() {
		return aaiContriDeviation;
	}
	public void setAaiContriDeviation(String aaiContriDeviation) {
		this.aaiContriDeviation = aaiContriDeviation;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getOldEmoluments() {
		return oldEmoluments;
	}
	public void setOldEmoluments(String oldEmoluments) {
		this.oldEmoluments = oldEmoluments;
	}
	public String getNewEmoluments() {
		return newEmoluments;
	}
	public void setNewEmoluments(String newEmoluments) {
		this.newEmoluments = newEmoluments;
	}
	public String getOldEmppfstatury() {
		return oldEmppfstatury;
	}
	public void setOldEmppfstatury(String oldEmppfstatury) {
		this.oldEmppfstatury = oldEmppfstatury;
	}
	public String getNewEmppfstatury() {
		return newEmppfstatury;
	}
	public void setNewEmppfstatury(String newEmppfstatury) {
		this.newEmppfstatury = newEmppfstatury;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
		public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
		public String getPensionNo() {
		return pensionNo;
	}
	public void setPensionNo(String pensionNo) {
		this.pensionNo = pensionNo;
	}
		public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getComputerName() {
		return computerName;
	}
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}
	public String getCpfAcno() {
		return cpfAcno;
	}
	public void setCpfAcno(String cpfAcno) {
		this.cpfAcno = cpfAcno;
	}
	public String getOldEmpvpf() {
		return oldEmpvpf;
	}
	public void setOldEmpvpf(String oldEmpvpf) {
		this.oldEmpvpf = oldEmpvpf;
	}
	public String getNewEmpvpf() {
		return newEmpvpf;
	}
	public void setNewEmpvpf(String newEmpvpf) {
		this.newEmpvpf = newEmpvpf;
	}
	public String getOldPrincipal() {
		return oldPrincipal;
	}
	public void setOldPrincipal(String oldPrincipal) {
		this.oldPrincipal = oldPrincipal;
	}
	public String getNewPrincipal() {
		return newPrincipal;
	}
	public void setNewPrincipal(String newPrincipal) {
		this.newPrincipal = newPrincipal;
	}
	public String getProcessDate() {
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	public String getOldInterest () {
		return oldInterest ;
	}
	public void setOldInterest (String oldInterest ) {
		this.oldInterest  = oldInterest ;
	}
	public String getNewInterest() {
		return newInterest;
	}
	public void setNewInterest(String newInterest) {
		this.newInterest = newInterest;
	}
	public String getOldPensioncontri() {
		return oldPensioncontri;
	}
	public void setOldPensioncontri(String oldPensioncontri) {
		this.oldPensioncontri = oldPensioncontri;
	}
	public String getNewPensioncontri() {
		return newPensioncontri;
	}
	public void setNewPensioncontri(String newPensioncontri) {
		this.newPensioncontri = newPensioncontri;
	}
	public String getChkAdjoburl() {
		return chkAdjoburl;
	}
	public void setChkAdjoburl(String chkAdjoburl) {
		this.chkAdjoburl = chkAdjoburl;
	}
	public String getOriginalRecord() {
		return originalRecord;
	}
	public void setOriginalRecord(String originalRecord) {
		this.originalRecord = originalRecord;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}	 
	public String getBatchTime() {
		return batchTime;
	}
	public void setBatchTime(String batchTime) {
		this.batchTime = batchTime;
	}

}
