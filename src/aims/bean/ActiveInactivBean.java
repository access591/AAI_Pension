package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActiveInactivBean implements Serializable{
private List activInativeList=new ArrayList();
private String checklist;
public List getActivInativeList() {
	return activInativeList;
}
public void setActivInativeList(List activInativeList) {
	this.activInativeList = activInativeList;
}
public String getChecklist() {
	return checklist;
}
public void setChecklist(String checklist) {
	this.checklist = checklist;
}

}
