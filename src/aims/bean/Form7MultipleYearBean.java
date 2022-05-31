package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Form7MultipleYearBean implements Serializable{
ArrayList eachYearList=new ArrayList();
String message="",dispFinYear="";
public String getDispFinYear() {
	return dispFinYear;
}
public void setDispFinYear(String dispFinYear) {
	this.dispFinYear = dispFinYear;
}
public ArrayList getEachYearList() {
	return eachYearList;
}
public void setEachYearList(ArrayList eachYearList) {
	this.eachYearList = eachYearList;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
}
