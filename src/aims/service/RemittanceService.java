package aims.service;

import java.util.ArrayList;

import aims.dao.RemittanceDAO;

public class RemittanceService {
	RemittanceDAO remDAO=new RemittanceDAO();
	
	public ArrayList getStationList(String region,String monthyear,String station){
		ArrayList stationList = null;
		stationList = remDAO.getStationList(region,monthyear,station);
		return stationList;
	}
}
