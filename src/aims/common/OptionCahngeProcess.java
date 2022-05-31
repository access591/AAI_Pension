package aims.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import aims.dao.FinancialReportDAO;
import aims.dao.ImportDao;



public class OptionCahngeProcess {
	Log log = new Log(OptionCahngeProcess.class);
	DBUtils commonDB = new DBUtils();
	//	By Radha On 28-JSep-2012 implementing upto 2012-13 Fin Year
	//By Radha On 09-Jan-2012 implementing upto 2011-12 Fin Year
	public static void runOptionchangeProcess()throws Exception{
		FinancialReportDAO reportDAO=new FinancialReportDAO();
		
		OptionCahngeProcess op = new OptionCahngeProcess();
		Log log = new Log(OptionCahngeProcess.class);
		DBUtils commonDB = new DBUtils();
		//String pensionno[]={"771","1743","2320","2558","2622","2759","2792","3213","3263","3302","3713","3726","4276","4649","6020","6354","6378","6743","6829","7447","7874","8081","8195","8410","8435","8443","8499","8504","8515","8547","8926","8932","8976","9430","9553","9869","9939","10159","10337","10944","11041","11447","11515","11521","11556","12985","13194","13235","13729","13939","14127","15890","17326","18977","18978","18979","18980","18988","18996","19016","19026","19032","19052","19080","21302","22119","22568","23026","18994","11520","22461","3108","19031","19057","19059","22487","19003","19017","295","17409","22129","19039","1058","1403","1761","2471","2687","3928","4107","7091","7456","8010","8545","8569","9125","9269","9380","9385","9541","9556","9808","11993","12464","13093","13099","13340","13368","13911","14618","16997","18992","19000","19053","22138","22718","22801","23250","18970","11293","8479","11485","252","9320","12605","19530","19577","19672","19730","19740","19751","19770","20939","20965","20990","21511","21564","21648","21696","21733","21756","21828","21875","21878","21896","21907","21955","22028","22032","22041","22061","22095","22143","22148","22163","22167","22290","22404","22443","22446","22458","22461","22514","22515","22544","22982","23163"};
		//String pensionno[]={"533", "832", "2338", "5412", "7158", "7502", "8041", "8440", "8558", "19033", "9267", "9279", "9728", "10053", "10327", "11177", "12380", "15776", "17541", "17744", "18026", "18057", "18396", "18428", "18562", "18722", "19033", "20254", "21048", "21686", "21784", "21895", "21933", "22020", "22164", "22181", "22457", "22935"};
		//String pensionno[]={"5267", "7877", "4501", "5117", "12431", "5555", "7457", "7508", "7870", "7584", "6120", "9610", "9220", "8249", "8289", "8318", "8310", "8382", "8242", "8380", "8401", "8339", "8360", "21077", "21076", "8384", "8238", "8236", "8373", "8228", "21080", "21094", "8342", "14605", "14813", "1399", "5884", "22520", "23126", "23507", "22408", "962", "5093", "14886", "21434", "9205", "21432", "21418", "2198", "4877"};
	
		ImportDao IDAO=new ImportDao();
		Connection con=null;
		ResultSet rs =null;
		Statement st =null;
		String pfids = "",message="",update_employee_info="",update_employee_personal_info="",insert_gen_adj="";
		String pensionno[] = null;		
		con=DBUtils.getConnection();		
		st=con.createStatement();
		pfids = op.getOptionChangePfids(con);
		try {	
			if(!pfids.equals("")){
			pensionno = pfids.split(",");		 
			for (int i=0;i<pensionno.length;i++){
				String pfid=pensionno[i],newWETHEROPTION="";
				 
				update_employee_info = "update employee_info set wetheroption=(select NEWWETHEROPTION from employee_pension_option where status= 'N' and pensionno ="+pfid+") where empserialnumber='" + pfid + "' ";
				update_employee_personal_info = "update employee_personal_info set wetheroption=(select NEWWETHEROPTION from employee_pension_option where status= 'N' and pensionno ="+pfid+") where pensionno='" + pfid + "' ";
				insert_gen_adj="insert into EMPLOYEE_GENRTED_PEND_ADJ_0B(PENSIONNO,EMPLOYEENAME,DATEOFBIRTH,DATEOFJOINING,WETHEROPTION,AIRPORTCODE,Region,REQGENDATE,STATUS,REMARKS) select PENSIONNO,EMPLOYEENAME,DATEOFBIRTH,DATEOFJOINING,WETHEROPTION,AIRPORTCODE,Region,sysdate,'N','Option Revised' from employee_personal_info where pensionno="+pfid;	        	
				log.info("runOptionchangeProcess::update_employee_info()--"+update_employee_info);
				log.info("runOptionchangeProcess::update_employee_personal_info()--"+update_employee_personal_info);
				log.info("runOptionchangeProcess::insert_gen_adj()--"+insert_gen_adj);					 
				st.executeUpdate(update_employee_info);
				st.executeUpdate(update_employee_personal_info);			 
				st.executeUpdate(insert_gen_adj);
				String getWetheroption="select NEWWETHEROPTION from employee_pension_option where pensionno ="+pfid;
				log.info("getWetheroption query"+getWetheroption);
				rs = st.executeQuery(getWetheroption);				
				while(rs.next()){					
					if (rs.getString("NEWWETHEROPTION") != null) {
						newWETHEROPTION = rs.getString("NEWWETHEROPTION") ;	
                	}		
              }				
				if(newWETHEROPTION.trim().equals("B")){				
				String deletearrearbreakup="delete from employee_arrear_breakup where pensionno="+pfid+"";
				String updatevalidate="update employee_pension_validate set arrearsbreakup = 'N',dueemoluments='',ARREARAMOUNT='' where pensionno="+pfid+" and arrearsbreakup = 'Y'";
				String updatearrear="update employee_pension_arrear set Originalarrearamount='' ,Originalarrearcontribution='',SINGLELINEAMOUNT='',SINGLELINECONTRIBUTION='' where pensionno ="+pfid+" and SINGLELINEAMOUNT is not null";
				log.info("deletearrearbreakup query"+deletearrearbreakup);
				log.info("updatevalidate query"+updatevalidate);
				log.info("updatearrear query"+updatearrear);
				st.executeQuery(deletearrearbreakup);
				st.executeUpdate(updatevalidate);
				st.executeUpdate(updatearrear);
				}
			IDAO.pentionContributionProcessAfterOptionChange(pfid);
			IDAO.pentionContributionProcessAfterOptionChangeforarrearbreakup(pfid);
			reportDAO.updatePCReport("1-Apr-1995", "1-May-2008","","",pfid,"","","1 - 1","true","N");
			st.executeUpdate(insert_gen_adj);
			reportDAO.updatePCReport("1-Apr-2008", "31-Mar-2012", "", "", pfid, "", "",
					"1 - 1", "true", "Y");
			
			String update_status =" update employee_pension_option set status='Y' where pensionno ="+pfid;				
			st.executeUpdate(update_status);
			log.info("OptionCahngeProcess::runOptionchangeProcess()--------"+update_status);
		}
		message =pensionno.length+" Pfids are Processed";
		}else{
			message ="Nothing to Process";
		}
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception e) {
			log.printStackTrace(e);
		} finally {
			commonDB.closeConnection(con, st, rs);
		}	   
	}
	
	 //New Method
	public String getOptionChangePfids(Connection con){
		Statement st = null;
		ResultSet rs = null;
		String sqlQuery = "";
		String  pfid = "";
		StringBuffer pfidString =  new StringBuffer();
		sqlQuery="select pensionno from employee_pension_option where status ='N'  order by pensionno";
		log.info("OptionCahngeProcess::getOptionChangePfids()--------"+sqlQuery);
		try{
		st=con.createStatement();		
		rs = st.executeQuery(sqlQuery);
		while(rs.next()){	
			
			if (rs.getString("pensionno") != null) {
				pfid = rs.getString("pensionno") ;	
				 
			}		
			pfidString.append(pfid);
			pfidString.append(",");
			 
		}
		
		} catch (SQLException e) {
			log.printStackTrace(e);
		} catch (Exception ex) {
			log.printStackTrace(ex);
		} finally {
			commonDB.closeConnection(null, st, rs);
		}
		log.info("--pfidString- "+pfidString.toString());
		return pfidString.toString();
	} 
	
}
