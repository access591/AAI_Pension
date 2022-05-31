/**
 * File       : FinaceDataAvailable.java
 * Date       : 08/28/2007
 * Author     : AIMS 
 * Description: 
 * Copyright (2008-2009) by the Navayuga Infotech, all rights reserved.
 * 
 */

package aims.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FinaceDataAvailable implements Serializable{

	/**
	 * @param args
	 */
	ArrayList availableList=new ArrayList();
	ArrayList unAvailableList=new ArrayList();
	public ArrayList getAvailableList() {
		return availableList;
	}
	public void setAvailableList(ArrayList availableList) {
		this.availableList = availableList;
	}
	public ArrayList getUnAvailableList() {
		return unAvailableList;
	}
	public void setUnAvailableList(ArrayList unAvailableList) {
		this.unAvailableList = unAvailableList;
	}

}
