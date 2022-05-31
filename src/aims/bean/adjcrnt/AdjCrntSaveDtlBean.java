package aims.bean.adjcrnt;

import java.io.Serializable;
import java.util.List;

public class AdjCrntSaveDtlBean implements Serializable{
	private List previouseGrndList;
	private String finalizedFlag="";
	public String getFinalizedFlag() {
		return finalizedFlag;
	}
	public void setFinalizedFlag(String finalizedFlag) {
		this.finalizedFlag = finalizedFlag;
	}
	public List getPreviouseGrndList() {
		return previouseGrndList;
	}
	public void setPreviouseGrndList(List previouseGrndList) {
		this.previouseGrndList = previouseGrndList;
	}
	

}
