package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class EmployeeCardReportInfo implements Serializable{
	EmployeePersonalInfo personalInfo=new EmployeePersonalInfo();
	ArrayList pensionCardList=new ArrayList();
    ArrayList ptwList=new ArrayList();
    ArrayList finalSettmentList=new ArrayList();
    ArrayList pensionCardList1=new ArrayList();
    ArrayList ptwList1=new ArrayList();
    String arrearInfo="",orderInfo="",mutiplePaymentFlag="";
    ArrayList addContriList=new ArrayList();
    String additionalContri="",netEPF="",monthyear="";
    
    public String getMonthyear() {
		return monthyear;
	}
	public void setMonthyear(String monthyear) {
		this.monthyear = monthyear;
	}
	public String getAdditionalContri() {
		return additionalContri;
	}
	public void setAdditionalContri(String additionalContri) {
		this.additionalContri = additionalContri;
	}
	public String getNetEPF() {
		return netEPF;
	}
	public void setNetEPF(String netEPF) {
		this.netEPF = netEPF;
	}
	public ArrayList getAddContriList() {
		return addContriList;
	}
	public void setAddContriList(ArrayList addContriList) {
		this.addContriList = addContriList;
	}
	public String getArrearInfo() {
		return arrearInfo;
	}
	public void setArrearInfo(String arrearInfo) {
		this.arrearInfo = arrearInfo;
	}
	public String getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}
	/**
     * 
     */
    int interNoOfMonths=0,noOfMonths=0,arrearNoOfMonths=0;
    
	public int getArrearNoOfMonths() {
		return arrearNoOfMonths;
	}
	public void setArrearNoOfMonths(int arrearNoOfMonths) {
		this.arrearNoOfMonths = arrearNoOfMonths;
	}
	public int getInterNoOfMonths() {
		return interNoOfMonths;
	}
	public void setInterNoOfMonths(int interNoOfMonths) {
		this.interNoOfMonths = interNoOfMonths;
	}
	public int getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
	
	public ArrayList getFinalSettmentList() {
		return finalSettmentList;
	}
	public void setFinalSettmentList(ArrayList finalSettmentList) {
		this.finalSettmentList = finalSettmentList;
	}
	public ArrayList getPtwList() {
        return ptwList;
    }
    public void setPtwList(ArrayList ptwList) {
        this.ptwList = ptwList;
    }
    public ArrayList getPensionCardList() {
		return pensionCardList;
	}
	public void setPensionCardList(ArrayList pensionCardList) {
		this.pensionCardList = pensionCardList;
	}
	public EmployeePersonalInfo getPersonalInfo() {
		return personalInfo;
	}
	public void setPersonalInfo(EmployeePersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}
	public ArrayList getPensionCardList1() {
		return pensionCardList1;
	}
	public void setPensionCardList1(ArrayList pensionCardList1) {
		this.pensionCardList1 = pensionCardList1;
	}
	public ArrayList getPtwList1() {
		return ptwList1;
	}
	public void setPtwList1(ArrayList ptwList1) {
		this.ptwList1 = ptwList1;
	}
	public String getMutiplePaymentFlag() {
		return mutiplePaymentFlag;
	}
	public void setMutiplePaymentFlag(String mutiplePaymentFlag) {
		this.mutiplePaymentFlag = mutiplePaymentFlag;
	}

}
