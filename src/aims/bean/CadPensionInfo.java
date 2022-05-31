package aims.bean;

import java.io.Serializable;

public class CadPensionInfo implements Serializable {

	String Pensionno = "",Year="", Emoluments = "", CONTRIBUTION = "", Additional = "",
			NetEPf = "", CPFIntrest = "", EpfoIntrest = "", EpfoPension = "";
	
	

	public String getYear() {
		return Year;
	}

	public void setYear(String year) {
		Year = year;
	}

	public String getPensionno() {
		return Pensionno;
	}

	public void setPensionno(String pensionno) {
		Pensionno = pensionno;
	}

	public String getEmoluments() {
		return Emoluments;
	}

	public void setEmoluments(String emoluments) {
		Emoluments = emoluments;
	}

	public String getCONTRIBUTION() {
		return CONTRIBUTION;
	}

	public void setCONTRIBUTION(String cONTRIBUTION) {
		CONTRIBUTION = cONTRIBUTION;
	}

	public String getAdditional() {
		return Additional;
	}

	public void setAdditional(String additional) {
		Additional = additional;
	}

	public String getNetEPf() {
		return NetEPf;
	}

	public void setNetEPf(String netEPf) {
		NetEPf = netEPf;
	}

	public String getCPFIntrest() {
		return CPFIntrest;
	}

	public void setCPFIntrest(String cPFIntrest) {
		CPFIntrest = cPFIntrest;
	}

	public String getEpfoIntrest() {
		return EpfoIntrest;
	}

	public void setEpfoIntrest(String epfoIntrest) {
		EpfoIntrest = epfoIntrest;
	}

	public String getEpfoPension() {
		return EpfoPension;
	}

	public void setEpfoPension(String epfoPension) {
		EpfoPension = epfoPension;
	}
	
	
}
