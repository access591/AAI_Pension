package aims.bean;

import java.io.Serializable;

public class DesignationBean implements Serializable{
private String emplevel="",desingation="";

public String getDesingation() {
	return desingation;
}

public void setDesingation(String desingation) {
	this.desingation = desingation;
}

public String getEmplevel() {
	return emplevel;
}

public void setEmplevel(String emplevel) {
	this.emplevel = emplevel;
}
}
