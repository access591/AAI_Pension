package aims.bean;

import java.io.Serializable;



public class AdjExtractData implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -723031898604221518L;
private String sheetname,sheettype,xlsdocument;

public String getXlsdocument() {
	return xlsdocument;
}
public void setXlsdocument(String xlsdocument) {
	this.xlsdocument = xlsdocument;
}
public String getSheetname() {
	return sheetname;
}
public void setSheetname(String sheetname) {
	this.sheetname = sheetname;
}
public String getSheettype() {
	return sheettype;
}
public void setSheettype(String sheettype) {
	this.sheettype = sheettype;
}


}
