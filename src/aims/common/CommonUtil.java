package aims.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import jxl.Cell;
import jxl.CellType;
import jxl.FormulaCell;
import jxl.Image;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.formula.FormulaException;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import oracle.sql.BLOB;
import aims.bean.EmpMasterBean;
import aims.bean.RegionBean;

/* 
##########################################
#Date					Developed by			Issue description
#10-Apr-2012        	Prasanthi			     adding the get finalsettlement info method
#10-Apr-2012        	Prasanthi			     checking exception in importing time(readDataSheet1)
#23-Feb-2012            Prasad                   For implementing  comparing two equal dates in compareTwoDates1 method.
#14-Feb-2012			Prasad					 Implementaing Uploading of Mapping Data 
#13-Jan-2012        	Prasanthi			     throw exception for in salary column have zeros(readDataSheet1)
#03-Jan-2012        	Prasanthi			     throw exception for in salary column have alphabets(readDataSheet1)
#29-Dec-2011        	Prasanthi			     adding the method getuserlist for edit settlement screen information
#17-Dec-2011        	Prasanthi			     throw exception for Uploading with other Extension of Files Other than . Xls(readDataSheet1,readDataSheet2,readExcelSheet)
#09-Dec-2011        	Prasanthi			     throw exception for Uploading with other Extension of Files Other than . Xls(readDataSheet1,readExcelSheet)
#########################################
*/
public class CommonUtil implements Constants {
	static Log log = new Log(CommonUtil.class);

	DBUtils commonDB = new DBUtils();
	static String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" };
	static String tenPlus[] = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
			"Eighteen", "Nineteen", "Twenty" };
	static String tens[] = { "", "Ten", "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

	/**
	 * @param args
	 */
	static Properties props = new Properties();

	/* from suresh */
	public int gridLength() {
		int gridLength = 0;
		LoadProperties getProperties = new LoadProperties();
		Properties prop = new Properties();
		prop = getProperties.loadFile(Constants.APPLICATION_PROPERTIES_FILE_NAME);
		if (prop.getProperty("common.gridlength") != null) {
			gridLength = Integer.parseInt(prop.getProperty("common.gridlength"));
		}
		return gridLength;
	}

	public String readExcelSheet(String fileName) throws BiffException, IOException, InvalidDataException {
		log.info("CommonUtil:readExcelSheet Entering method");
		String readExcelData = "";
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("en", "EN"));
		try {
			log.info("try block ..");
			log.info("File Name  .." + fileName);
			log.info("**************************************123");
			
			
			ws.setEncoding("Cp1252");
			
			log.info("**************************************");
			int readMonthlyTRData = fileName.indexOf("AAIEPF-3");
			log.info("==readMonthlyTRData " + readMonthlyTRData);
			int readMonthlyArrData = fileName.indexOf("AAIEPF-3A");
			log.info("==readMonthlyArrData " + readMonthlyArrData);
			int readMonthlySuppliData = fileName.indexOf("AAIEPF-3-SUPPL");
			log.info("==readMonthlySuppliData " + readMonthlySuppliData);
			int readYearlyOB = fileName.indexOf("AAIEPF-1");
			log.info("==readYearlyOB " + readYearlyOB);
			int readEmpOBData = fileName.indexOf("AAIEPF-2");
			log.info("==readEmpOBData " + readEmpOBData);
			int readAdvancePFW = fileName.indexOf("AAIEPF-8");
			log.info("==readAdvancePFW " + readAdvancePFW);
			int readsingleData = fileName.indexOf("AAIEPF-SINGLEVALUE");
			log.info("==readsingleData " + readsingleData);
			int readAaiepf4 = fileName.indexOf("AAIEPF-4");
			log.info("==readAaiepf4 " + readAaiepf4);
			
			
			log.info("readMonthlyTRData" + readMonthlyTRData);
			log.info("importMonthwiseSupplData" + readMonthlySuppliData);
			int readArearbreakup = fileName.indexOf("ARREARBREAKUP_UPLOAD");
			log.info("==readArearbreakup " + readArearbreakup);
			
			Sheet s = null;
			//org.apache.poi.hssf.usermodel.HSSFSheet sheet = null ;
			
			FileInputStream fi = new FileInputStream(fileName);
 			Workbook workbook = Workbook.getWorkbook(fi);
			
			s = workbook.getSheet(0);
			
//			if (readYearlyOB != -1 || readAdvancePFW != -1 || readArearbreakup != -1) {
//				
//				//readExcelData = readDataSheet2(s, fileName);
//				FileInputStream fis=new FileInputStream(new File(fileName));  
//				
//				org.apache.poi.hssf.usermodel.HSSFWorkbook wb=new org.apache.poi.hssf.usermodel.HSSFWorkbook(fis);   	
//				sheet=wb.getSheetAt(0);  
//				
//				
//			}else {
//				
//				Workbook workbook = Workbook.getWorkbook(new File(fileName), ws);
//				
//				s = workbook.getSheet(0);
//			}
			
		
			// Sheet s = workbook.getSheet(3);
			
			
			if (readMonthlySuppliData != -1) {
				log.info("1");
				readExcelData = readDataSheetSuppli(s);
			} else if (readMonthlyArrData != -1) {
				log.info("2");
				readExcelData = readDataSheetArrear(s);
			} else if (readAaiepf4 != -1 || readsingleData != -1) {
				log.info("3");
				readExcelData = readDataSheet4single(s);
			} else if (readMonthlyTRData != -1) {
				log.info("4");
			
				readExcelData = readDataSheetMonthly(s, fileName);
			} else if (readEmpOBData != -1) {
				log.info("5");
				readExcelData = readForm2Data(s);
			} 
			
			
			else if (readYearlyOB != -1 || readAdvancePFW != -1 || readArearbreakup != -1) {
				log.info("7");
				//readExcelData = myReadDataSheet(sheet, fileName);
				readExcelData = readDataSheet2(s, fileName);
				//readExcelData = readDataSheetArrear(s);
			} 
		
			else {
				log.info("6");	
				readExcelData = readDataSheet(s);
			}
			
		} catch (BiffException ex) {
			log.info("ëxception===> BiffException" + ex.toString());
			log.printStackTrace(ex);
			
			String msg = "The extension of the file is not allowed to be uploaded , Need to be  in  .xls Extension(97-2003 Workbook).";
			throw new InvalidDataException(msg);
		} catch (IOException e) {
			log.info("=====> IOEXception : " + e.toString());
			
			// TODO Auto-generated catch block
			log.printStackTrace(e);
			throw e;
		} catch (Exception ex) {
			log.info("=====> EXception : " + ex.toString());
			// TODO Auto-generated catch block
			throw new InvalidDataException(ex.getMessage());
		}
		log.info("CommonUtil:readExcelSheet leaving method");
		return readExcelData;
	}

	
	//my function for Arrear breakup 
	private String myReadDataSheet(HSSFSheet sheet, String fileName) {
		
		log.info("My CommonUtil:readDataSheet Entering method");
		//org.apache.poi.ss.usermodel.Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		//log.info("Columns" + sheet.getColumns() + "Rows" + s.getRows());
		String delimiter = "*", cellContent = "";
		Iterator<Row> rowIterator = sheet.iterator();
		
		while(rowIterator.hasNext()) {
			 Row row = rowIterator.next();
	         Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
	         
	         while(cellIterator.hasNext()) {
	        	
	        	 DataFormatter formatter = new DataFormatter();
                 
                 
	        	 org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();
	        	 String text = formatter.formatCellValue(cell);
                 
	        	 //String Tag=cell.getStringCellValue().toString();
                // log.info("============================================> Tag " + Tag);
                 
                
                 //cell = row.getCell(0+1);
               
                
                 
                
				//cell = s.getCell(i, j);
                 
                 if(text !=null) {
                	 log.info("11");
                	 if (!text.equals("")) {
                		 log.info("12");
     					cellContent = StringUtility.replace(text.toCharArray(), delimiter.toCharArray(), "")
     							.toString();
     					// System.out.println("Format"+cell.getType()+"Contents"+cell.getContents());
     					if (!cellContent.equals("")) {
     						 log.info("13");
     						eachRow.append(text + "@");
     					} else {
     						 log.info("14");
     						eachRow.append("XXX" + "@");
     					}
     				} else {
     					 log.info("15");
     					eachRow.append("XXX" + "@");
     				}
                	 
                 }
				
			}
			eachRow.append("***");
		}
		// log.info(eachRow.toString());
		log.info("my CommonUtil:readDataSheet Leaving method");
		return eachRow.toString();
		
	}

	public Map readMulitpleExcelSheets(String fileName) throws BiffException, IOException, InvalidDataException {

		log.info("CommonUtil:readMulitpleExcelSheets Entering method" + fileName);
		String calculsheetfolder = "", form2sheetfolder = "", epffolder = "", slashsuffix = "";
		Map multiplefilslst = new HashMap();
		HashMapComparable hcomp = new HashMapComparable();
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
			epffolder = bundle.getString("upload.folder.path.epf");
			calculsheetfolder = bundle.getString("upload.folder.path.epf.cal");
			form2sheetfolder = bundle.getString("upload.folder.path.epf.form2");
			slashsuffix = bundle.getString("upload.folder.path.slashsuffix");
			String calfilename = "", portedfilenames = "", form2sheetname = "";
			File file = new File(fileName);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("er", "ER"));
			Workbook workbook = Workbook.getWorkbook(file, ws);
			int numberOfSheets = workbook.getNumberOfSheets();
			// log.info("workbook"+workbook.getVersion());
			// log.info("workbook"+workbook.getSheet("AAIEPF2_REC"));

			CommonUtil util = new CommonUtil();
			WritableWorkbook wwk = null;
			log.info("readMulitpleExcelSheets::numberOfSheets" + numberOfSheets);
			calfilename = calculsheetfolder + slashsuffix + Constants.ADJ_CAL_FILE_NAME_PREFIX;
			form2sheetname = form2sheetfolder + slashsuffix + Constants.ADJ_FORM2_FILE_NAME_PREFIX;
			File epffolderexts = new File(epffolder);
			if (!epffolderexts.exists()) {
				File saveDir = new File(epffolder);
				if (!saveDir.exists())
					saveDir.mkdirs();

			}
			File calfolderexts = new File(calculsheetfolder);
			if (!calfolderexts.exists()) {
				File saveDir = new File(calculsheetfolder);
				if (!saveDir.exists())
					saveDir.mkdirs();

			}

			File form2folderexts = new File(form2sheetfolder);
			if (!form2folderexts.exists()) {
				File saveDir = new File(form2sheetfolder);
				if (!saveDir.exists())
					saveDir.mkdirs();

			}
			for (int shtcnt = 0; shtcnt < numberOfSheets; shtcnt++) {
				Sheet s = workbook.getSheet(shtcnt);
				log.info("Sheet name" + s.getName() + "Sheet Columns " + s.getColumns() + "Contants Columns"
						+ FORM2_NO_OF_COLUMNS);
				if (shtcnt == 0 || s.getName().equals("AAIEPF2_REC")) {
					if (s.getColumns() != Integer.parseInt(FORM2_NO_OF_COLUMNS)) {
						throw new InvalidDataException("Orignial Form-2 Sheet and Uploaded Sheet Columns is not match");
					}
					portedfilenames = form2sheetname + util.getCurrentDate("ddmmyy HHmmsssSSS") + "_"
							+ Constants.ADJ_CAL_FILE_NAME_SUFFIX;
					log.info("ppp00000000000" + portedfilenames);
				} else {
					portedfilenames = calfilename + util.getCurrentDate("ddmmyy HHmmsssSSS") + "_" + s.getName().trim()
							+ Constants.ADJ_CAL_FILE_NAME_SUFFIX;
					log.info("ppp11111111" + portedfilenames);
				}

				if (s.getNumberOfImages() == 0) {
					wwk = Workbook.createWorkbook(new File(portedfilenames));
					wwk.importSheet(s.getName(), 0, s);
					wwk.write();
					wwk.close();

					log.info("ppp2222222222" + portedfilenames);
				} else {
					Image i = s.getDrawing(0);
					calfilename = calfilename + util.getCurrentDate("ddmmyy HHmmsssSSS") + "_" + s.getName().trim()
							+ ".jpg";
					portedfilenames = calfilename;
					getImage(i, calfilename);
					log.info("ppp33333333333" + portedfilenames);
				}
				log.info("portedfilenames" + portedfilenames);
				multiplefilslst.put(s.getName().trim(), portedfilenames);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			throw new InvalidDataException("The Sheet can't Read,Please Use Another Excel Sheet");
		}
		return hcomp.sortHashMap(multiplefilslst);

	}

	public String createFile(String filename, String extension, String data) {
		String path = "", rpfcfolder = "", rpfcform6 = "", filenamewithpath = "", slashsuffix = "";
		CommonUtil util = new CommonUtil();
		FileWriter fw = null;
		ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
		rpfcfolder = bundle.getString("upload.folder.path.rpfc");
		rpfcform6 = bundle.getString("upload.folder.path.rpfc.form6");
		slashsuffix = bundle.getString("upload.folder.path.slashsuffix");
		File rpfcfolderexts = new File(rpfcfolder);
		if (!rpfcfolderexts.exists()) {
			File saveDir = new File(rpfcfolder);
			if (!saveDir.exists())
				saveDir.mkdirs();

		}
		File rpfcform6folder = new File(rpfcform6);
		if (!rpfcform6folder.exists()) {
			File saveDir = new File(rpfcform6);
			if (!saveDir.exists())
				saveDir.mkdirs();

		}

		filenamewithpath = rpfcform6folder + slashsuffix + filename + "_" + util.getCurrentDate("ddmmyy HHmmsssSSS")
				+ extension;

		try {
			fw = new FileWriter(new File(filenamewithpath));
			BufferedWriter bw = new BufferedWriter(fw, 32768);

			bw.write(data);
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = filenamewithpath;
		return path;
	}

	private String readDataSheet(Sheet s) {
		log.info("CommonUtil:readDataSheet Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
		String delimiter = "*", cellContent = "";
		for (int j = 1; j < s.getRows(); j++) {
			for (int i = 0; i < s.getColumns(); i++) {
				cell = s.getCell(i, j);
				if (!cell.getContents().equals("")) {
					cellContent = StringUtility.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "")
							.toString();
					// System.out.println("Format"+cell.getType()+"Contents"+cell.getContents());
					if (!cellContent.equals("")) {
						eachRow.append(cell.getContents() + "@");
					} else {
						eachRow.append("XXX" + "@");
					}
				} else {
					eachRow.append("XXX" + "@");
				}
			}
			eachRow.append("***");
		}
		// log.info(eachRow.toString());
		log.info("CommonUtil:readDataSheet Leaving method");
		return eachRow.toString();
	}

	private String readDataSheetMonthly(Sheet s, String fileName) throws BiffException, InvalidDataException {
		
		log.info("CommonUtil:readDataSheetMonthly Entering method");
		Cell cell = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		StringBuffer eachRow = new StringBuffer();
		ArrayList exceptionList = new ArrayList();

		try {
			log.info("rows" + s.getRows() + "cols" + s.getColumns());
			ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
			con = commonDB.getConnection();
			st = con.createStatement();
			String fileName1 = fileName.substring(
					fileName.lastIndexOf(bundle.getString("upload.folder.path.slashsuffix")) + 1, fileName.length());
			String importMonthStatus = "select lower(FILENAME) as FILENAME,IMPORTSTATUS FROM payment_recov_mnth_upload_log where  lower(filename)='"
					+ fileName1.toLowerCase() + "'";
			log.info(importMonthStatus);
			rs = st.executeQuery(importMonthStatus);
			while (rs.next()) {
				log.info("IMPORTSTATUS" + rs.getString("IMPORTSTATUS") + "FILENAME" + rs.getString("FILENAME").trim()
						+ "filename" + fileName1.toLowerCase() + s.getColumns());
				if (rs.getString("IMPORTSTATUS").equals("Y")
						&& rs.getString("FILENAME").trim().equals(fileName1.toLowerCase().trim())) {
					exceptionList.add("" + fileName1 + " Sheet Already Imported");
				}
			}
			if (s.getColumns() == 0 || s.getRows() == 0) {
				exceptionList.add("You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet");
			} else if (s.getRows() <= 11) {
				exceptionList.add(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet and Data must be from 12th Row onwards");
			}
			if (s.getColumns() < 22) {
				exceptionList.add(
						"Please Submit the Data in the New Standard sheet (Download it from the Download Standard sheet for CPF data)");
			}
			String delimiter = "*", cellContent = "", cellCont = "";
			String sss = "", blankRow = "", emolEpf = "", emol0Epf0 = "", monAc = "";
			int count = 0, count1 = 0, count3 = 0;
			for (int j = 10; j < s.getRows(); j++) {
				for (int i = 0; i < s.getColumns(); i++) {
					cell = s.getCell(i, j);
					if (j >= 11) {
						if (!s.getCell(i, j).getContents().equals("")) {
							if (i == 0 || i == 1) {
								if (cell.getContents().trim().equals("") || cell.getContents().trim().equals("0")
										|| cell.getContents().equals("0.00")) {
									count++;
									blankRow = "Sheet contain blank rows in between the Data in Row: "
											+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "";
								}
							}
							if (i == 8 || i == 18) {
								if (cell.getContents().trim().equals("")) {
									count3++;
									monAc = "Sorry...! Field cannot be empty Please enter the valid data in Row: "
											+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "";
								}
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						if (j >= 11) {
							if (i >= 9 && i <= 17) {
								cellCont = StringUtility
										.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "")
										.toString();
								double cellvalue = Double.parseDouble(cellCont);
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();
						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {
								exceptionList.add("The invalid data '" + cell.getContents() + "' exist in Row: "
										+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						// log.info("Test cellContent:"+cellContent);
						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}
					} else {
						eachRow.append("XXX" + "@");
					}
				}
				eachRow.append("***");
			}
			if (count != 0 || count1 != 0 || count3 != 0) {
				if (count > 1) {
					String message = "Sorry....! Sheet contain blank rows in SRNO/PENSIONNO Fields";
					exceptionList.add(message);
				} else {
					exceptionList.add(blankRow);
				}
				if (count1 > 1) {
					String message = "Sorry...! Field cannot be empty Please enter the valid data (Excepting only Numeric Values) exist in Emoluments/EPF";
					exceptionList.add(message);
				} else {
					exceptionList.add(emolEpf);
				}
				if (count3 > 1) {
					String message = "Sorry...! Field cannot be empty Please enter the valid data in Monthyear/Airportcode";
					exceptionList.add(message);
				} else {
					exceptionList.add(monAc);
				}
			}
			log.info("count" + count + "count1" + count1 + "count3" + count3);
			log.info("exceptionList" + exceptionList.size());
			if (exceptionList.size() > 0) {
				StringBuffer exceptionRecords = new StringBuffer();
				String mesg = "";
				Set set = new HashSet();
				StringBuffer newRecords = new StringBuffer();

				for (Iterator it = exceptionList.iterator(); it.hasNext();) {
					Object element = it.next();
					log.info("element" + element);
					if (!element.equals("")) {
						if (set.add(element))
							newRecords.append(element + "\r\n" + "-->");
					}
				}
				exceptionList.clear();
				exceptionRecords.append(newRecords);
				log.info("exceptionRecords" + exceptionRecords);
				mesg = "" + exceptionRecords;
				throw new InvalidDataException(mesg);
			}
		} catch (Exception e) {
			log.info(e.toString());
			String msg = "";
			String txt = e.toString();
			if (txt.indexOf("java.lang.ArrayIndexOutOfBoundsException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values)";
			}
			if (txt.indexOf("java.lang.NullPointerException") != -1) {
				msg = "sorry....! Row cannot be blank Please Enter Valid Data in row:" + cell.getRow() + 1 + "";
			}
			if (txt.indexOf("java.lang.NumberFormatException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values in the sheet in the row:" + cell.getRow()
						+ " and columns :" + cell.getColumn() + " )";
			}

			throw new InvalidDataException(msg + "" + e.getMessage());
		} finally {
			commonDB.closeConnection(con, st, rs);
		}
		log.info("CommonUtil:readDataSheetMonthly Leaving method");
		return eachRow.toString();
	}

	private String readDataSheetSuppli(Sheet s) throws BiffException, InvalidDataException {
		log.info("CommonUtil:readDataSheetSuppli Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		try {
			log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
			if (s.getColumns() == 0 || s.getRows() == 0) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet");
			} else if (s.getRows() <= 11) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet and Data must be from 12th Row onwards");
			}
			String delimiter = "*", cellContent = "", cellCont = "";
			String sss = "";
			for (int j = 10; j < s.getRows(); j++) {
				for (int i = 0; i < s.getColumns(); i++) {
					cell = s.getCell(i, j);
					if (j >= 11) {
						if (i == 0 || i == 1) {
							if (cell.getContents().trim().equals("") || cell.getContents().trim().equals("0")
									|| cell.getContents().equals("0.00")) {
								throw new InvalidDataException("Sheet contain blank rows in between the Data in Row: "
										+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						if (i == 9 || i == 10) {
							log.info("inside method" + cell.getContents());
							if (cell.getContents().trim().equals("")) {
								throw new InvalidDataException(
										"Sorry...! Field cannot be empty Please enter the valid data (Excepting only Numeric Values) exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
							if (cell.getContents().trim().equals("0") || cell.getContents().equals("0.00")) {
								throw new InvalidDataException(
										"Sorry...! Value cannot be Zero Please enter the valid data in exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						if (j >= 11) {
							if (i >= 9 && i <= 17) {
								cellCont = StringUtility
										.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "")
										.toString();
								double cellvalue = Double.parseDouble(cellCont);
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();
						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {
								throw new InvalidDataException(
										"The invalid data '" + cell.getContents() + "' exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						// log.info("Test cellContent:"+cellContent);
						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}
					} else {
						eachRow.append("XXX" + "@");
					}
				}
				eachRow.append("***");
			}
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			log.info(e.toString());
			String msg = "";
			String txt = e.toString();
			if (txt.indexOf("java.lang.ArrayIndexOutOfBoundsException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values)";
			}
			if (txt.indexOf("java.lang.NullPointerException") != -1) {
				msg = "sorry....! Row cannot be blank Please Enter Valid Data in row:" + s.getRows() + 1 + "";
			}
			if (txt.indexOf("java.lang.NumberFormatException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values in the sheet in the row:" + s.getRows()
						+ " and columns :" + s.getColumns() + " )";
			}

			throw new InvalidDataException(msg + "" + e.getMessage());
		}
		log.info("CommonUtil:readDataSheetSuppli Leaving method");
		return eachRow.toString();
	}

	private String readDataSheetArrear(Sheet s) throws BiffException, InvalidDataException {
		log.info("CommonUtil:readDataSheetArrear Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		try {
			log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
			if (s.getColumns() == 0 || s.getRows() == 0) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet");
			} else if (s.getRows() <= 11) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet and Data must be from 12th Row onwards");
			}
			String delimiter = "*", cellContent = "", cellCont = "";
			String sss = "";
			for (int j = 10; j < s.getRows(); j++) {
				for (int i = 0; i < s.getColumns(); i++) {
					cell = s.getCell(i, j);
					if (j >= 11) {
						if (i == 0 || i == 1) {
							if (cell.getContents().trim().equals("") || cell.getContents().trim().equals("0")
									|| cell.getContents().equals("0.00")) {
								throw new InvalidDataException("Sheet contain blank rows in between the Data in Row: "
										+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						if (i == 9 || i == 10) {
							log.info("inside method" + cell.getContents());
							if (cell.getContents().trim().equals("")) {
								throw new InvalidDataException(
										"Sorry...! Field cannot be empty Please enter the valid data (Excepting only Numeric Values) exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
							if (cell.getContents().trim().equals("0") || cell.getContents().equals("0.00")) {
								throw new InvalidDataException(
										"Sorry...! Value cannot be Zero Please enter the valid data in exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						if (j >= 11) {
							if (i >= 9 && i <= 17) {
								cellCont = StringUtility
										.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "")
										.toString();
								double cellvalue = Double.parseDouble(cellCont);
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();
						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {
								throw new InvalidDataException(
										"The invalid data '" + cell.getContents() + "' exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						// log.info("Test cellContent:"+cellContent);
						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}
					} else {
						eachRow.append("XXX" + "@");
					}
				}
				eachRow.append("***");
			}
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			log.info(e.toString());
			String msg = "";
			String txt = e.toString();
			if (txt.indexOf("java.lang.ArrayIndexOutOfBoundsException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values)";
			}
			if (txt.indexOf("java.lang.NullPointerException") != -1) {
				msg = "sorry....! Row cannot be blank Please Enter Valid Data in row:" + s.getRows() + 1 + "";
			}
			if (txt.indexOf("java.lang.NumberFormatException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values in the sheet in the row:" + s.getRows()
						+ " and columns :" + s.getColumns() + " )";
			}

			throw new InvalidDataException(msg + "" + e.getMessage());
		}
		log.info("CommonUtil:readDataSheetArrear Leaving method");
		return eachRow.toString();
	}

	private String readDataSheet4single(Sheet s) throws BiffException, InvalidDataException {
		log.info("CommonUtil:readDataSheet4 Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		try {
			log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
			if (s.getColumns() == 0 || s.getRows() == 0) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet");
			} else if (s.getRows() <= 11) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Data in Standard Format Sheet and Data must be from 12th Row onwards");
			}
			String delimiter = "*", cellContent = "", cellCont = "";
			String sss = "";
			for (int j = 10; j < s.getRows(); j++) {
				for (int i = 0; i < s.getColumns(); i++) {
					cell = s.getCell(i, j);
					if (j >= 11) {
						if (i == 0 || i == 1) {
							if (cell.getContents().trim().equals("") || cell.getContents().trim().equals("0")
									|| cell.getContents().equals("0.00")) {
								throw new InvalidDataException("Sheet contain blank rows in between the Data in Row: "
										+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						if (j >= 11) {
							if (i >= 9 && i <= 17) {
								cellCont = StringUtility
										.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "")
										.toString();
								double cellvalue = Double.parseDouble(cellCont);
							}
						}
					}
					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();
						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {
								throw new InvalidDataException(
										"The invalid data '" + cell.getContents() + "' exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						// log.info("Test cellContent:"+cellContent);
						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}
					} else {
						eachRow.append("XXX" + "@");
					}
				}
				eachRow.append("***");
			}
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			log.info(e.toString());
			String msg = "";
			String txt = e.toString();
			if (txt.indexOf("java.lang.ArrayIndexOutOfBoundsException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values)";
			}
			if (txt.indexOf("java.lang.NullPointerException") != -1) {
				msg = "sorry....! Row cannot be blank Please Enter Valid Data in row:" + s.getRows() + 1 + "";
			}
			if (txt.indexOf("java.lang.NumberFormatException") != -1) {
				msg = "Please Enter Valid Data ( Excepting only Numeric Values in the sheet in the row:" + s.getRows()
						+ " and columns :" + s.getColumns() + " )";
			}
			throw new InvalidDataException(msg + "" + e.getMessage());

		}
		log.info("CommonUtil:readDataSheet4 Leaving method");
		return eachRow.toString();
	}

	private String readDataSheet2(Sheet s, String fileName) throws BiffException, InvalidDataException {
		
		log.info("CommonUtil:readDataSheet2 Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
		String delimiter = "*", cellContent = "";
		try {
			if (s.getColumns() == 0 || s.getRows() == 0) {
				throw new InvalidDataException(
						"You can't upload the Blank Excel Sheet,Need to Have Advances/PFW/FinalSettlement Data in AAI EPF8 Sheet");
			}
			int jvalue = 0;
			if (fileName.indexOf("AAIEPF-8") != -1) {
				jvalue = 9;
				if (s.getRows() <= 10) {
					throw new InvalidDataException(
							"You can't upload the Blank Excel Sheet,Need to Have Advances/PFW/FinalSettlement Data in AAI EPF8 Sheet and Data must be from 11th Row onwards");
				}
			} else if (fileName.indexOf("ARREARBREAKUP_") != -1) {
				jvalue = 1;
			} else {
				jvalue = 7;
			}

			for (int j = jvalue; j < s.getRows(); j++) {
				for (int i = 0; i < s.getColumns(); i++) {
					cell = s.getCell(i, j);
					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();
						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {
								throw new InvalidDataException(
										"The invalid data '" + cell.getContents() + "' exist in Row: "
												+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1) + "");
							}
						}
						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}

					} else {
						eachRow.append("XXX" + "@");
					}

				}
				eachRow.append("***");
			}
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("CommonUtil:readDataSheet2 Leaving method");
		return eachRow.toString();
	}

	public ArrayList getTheList(String tableList, String delimeter) {
		// log.info("CommonUtil:getTheList Entering method");
		ArrayList tblList = new ArrayList();
		StringTokenizer strTokens = new StringTokenizer(tableList, delimeter);
		while (strTokens.hasMoreTokens()) {
			tblList.add(strTokens.nextToken());
		}
		// log.info("CommonUtil:getTheList Leaving method");
		return tblList;
	}

	public String escapeSingleQuotes(String strToEscape) {
		if (strToEscape.indexOf('\'') == -1)
			return strToEscape;
		StringBuffer strBuf = new StringBuffer(strToEscape);
		int j = 0;
		int i = 0;
		while (i <= strToEscape.length()) {
			i = strToEscape.indexOf('\'', i);
			if (i == -1)
				break;
			strBuf = strBuf.insert(i + j, '\'');
			i++;
			j++;
		}
		strToEscape = strBuf.toString();
		return strToEscape;
	}

	public void writeFile(String message, String className) {
		String fileName = "";
		fileName = "C://admin//" + generateFileName("AdminLog") + ".txt";
		log.info("Write File in Common Util" + fileName);
		log.info("Write File in Common Util" + message);
		File file = new File(fileName);
		boolean exists = (file).exists();
		try {
			if (!exists) {
				System.out.println("File Not Existed");
				file.createNewFile();
			}
			System.out.println("File Existed");
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
			String format = "\r\n" + this.getDateTime(Constants.APPLICATION_DATE_TIME_FORMAT_HYPEN) + ">> " + className
					+ ">> " + message;
			out.write(format);
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String generateFileName(String prefix) {
		String fileName = "";
		fileName = prefix + "_" + this.getDateTime(Constants.APPLICATION_DATE_FORMAT);
		return fileName;
	}

	public String getDateTime(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	public static Properties loadFile(String fileName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream inStream = null;
		try {
			inStream = loader.getResourceAsStream(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("input stream is--- " + inStream);
		try {
			props.load(inStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	public static String converDBToAppFormat(Date dbDate) {
		Calendar cal = Calendar.getInstance();
		String convertedDt = "";

		SimpleDateFormat fromDate = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat toDate = new SimpleDateFormat("dd-MMM-yyyy");
		convertedDt = toDate.format(dbDate);

		return convertedDt;
	}

	public static String converDBToAppFormat(Date dbDate, String fromDtFormat) {
		Calendar cal = Calendar.getInstance();
		String convertedDt = "";

		SimpleDateFormat fromDate = new SimpleDateFormat(fromDtFormat);
		SimpleDateFormat toDate = new SimpleDateFormat("dd-MMM-yyyy");
		convertedDt = toDate.format(dbDate);

		return convertedDt;
	}

	public String getDate(String st) {
		// SimpleDateFormat fullMonthFormat = new
		// SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat fullMonthFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat toMonthFormat = new SimpleDateFormat("dd-MMM-yyyy");

		String date2 = "";
		Date dateR;
		try {

			dateR = fullMonthFormat.parse(st);

			String date1 = fullMonthFormat.format(dateR);
			date2 = toMonthFormat.format(fullMonthFormat.parse(date1));
			System.out.println("date1=====" + date1);
			System.out.println("date2=====" + date2);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	/**
	 * It'll save the file at the given path for given input stream object.
	 * 
	 * @param is
	 * @param path
	 */
	public static void saveFile(InputStream is, String path) throws Exception {

		System.out.println("===========Save File MEthod");
		try {
			File saveFilePath = new File(path);
			if (!saveFilePath.exists()) {
				File saveDir = new File(saveFilePath.getParent());
				if (!saveDir.exists())
					saveDir.mkdirs();
				saveFilePath.createNewFile();
			}

			FileOutputStream fout = new FileOutputStream(saveFilePath);
			int c;
			while ((c = is.read()) != -1) {
				fout.write(c);
			}
			fout.close();
			is.close();
		} catch (IOException _IOEx) {

		}
	}

	public Map getMonthsList() {
		Map map = new LinkedHashMap();
		map.put("01", "January");
		map.put("02", "February");
		map.put("03", "March");
		map.put("04", "April");
		map.put("05", "May");
		map.put("06", "June");
		map.put("07", "July");
		map.put("08", "August");
		map.put("09", "September");
		map.put("10", "October");
		map.put("11", "November");
		map.put("12", "December");
		return map;

	}

	public Map getFormsList(String user) {
		log.info(user);
		Map map = new LinkedHashMap();
		map.put("AAIEPF-3", "AAIEPF-3 (Monthly CPF Recovery)");
		if (user.trim().equals("navayuga")) {
			map.put("AAIEPF-3-SUPPL", "AAIEPF-3-SUPPL(Supplimentory Recovery)");
			map.put("AAIEPF-3A", "AAIEPF-3A (Arrears Recovery)");
			map.put("AAIEPF-SINGLEVALUE", "AAIEPF-SINGLEVALUE(Supplimentory value Recovery)");
			map.put("AAIEPF-4", "AAIEPF-4 (CPF Received From Other Org..)");
			map.put("AAIEPF-8", "AAIEPF-8 (Advances/PFW/FinalSettlement)");
		}
		return map;
	}

	public Map getFormsListNavayuga(String user) {
		log.info(user);
		Map map = new LinkedHashMap();
		if (user.trim().equals("navayuga")) {
			// map.put("AAIEPF-1", "AAIEPF-1 (Opening Balance)");
			map.put("AAIEPF-2", "AAIEPF-2 (Adjustment in OpeningBalance)");
			map.put("AAIEPF-2B", " AAIEPF-2-Batch");
			map.put("ARREARBREAKUP_UPLOAD", "ARREARBREAKUP_UPLOAD");
			map.put("IMP_CALC_UPD", "CPFDATA_UPLOAD");
			map.put("OTHER", "OTHER");
		}
		return map;
	}

	public long getDateDifference(String date1, String date2) {
		long noOfDays = 0;
		int days = 0;
		Date validatDt1 = new Date();
		Date validatDt2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			validatDt1 = sdf.parse(date1);
			validatDt2 = sdf.parse(date2);
			noOfDays = (validatDt2.getTime() - validatDt1.getTime());
			noOfDays = (noOfDays / (1000L * 60L * 60L * 24L * 365));
			log.info("--" + validatDt2.getTime() + "--" + validatDt1.getTime() + "--" + noOfDays);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return noOfDays;
	}

	public String converDBToAppFormat(String dbDate, String fromFormat, String toFormat) throws InvalidDataException {
		String convertedDt = "";
		SimpleDateFormat fromDate = new SimpleDateFormat(fromFormat);
		SimpleDateFormat toDate = new SimpleDateFormat(toFormat);
		try {
			if (!dbDate.equals("")) {
				convertedDt = toDate.format(fromDate.parse(dbDate));
				/*
				 * log.info("Converted Date is(converDBToAppFormat) " + convertedDt);
				 */
			} else {
				convertedDt = "";
			}

		} catch (ParseException e) {
			throw new InvalidDataException(e.getMessage());
		}
		return convertedDt;
	}

	public boolean checkFormat(String dbDate) throws InvalidDataException {
		boolean chkFormat = false;
		String year = "", month = "";
		int yearLength = 0, monthLength = 0;
		char delimeter = '-';
		if (dbDate.length() > 8) {
			if (checkDateFormat(dbDate.toCharArray(), delimeter) == 2) {
				if (dbDate.indexOf("-") != -1) {
					int index = dbDate.indexOf("-");
					month = dbDate.substring(index + 1, dbDate.lastIndexOf("-"));
					monthLength = month.length();
				}
				if (dbDate.lastIndexOf("-") != -1) {
					int index = dbDate.lastIndexOf("-");
					year = dbDate.substring(index + 1, dbDate.length());
					yearLength = year.length();
				}
				if (monthLength == 3 && yearLength == 4) {
					chkFormat = true;
				}
			} else {
				throw new InvalidDataException("Invalid Date Format");
			}

		} else {
			throw new InvalidDataException("Invalid Date Format");
		}

		return chkFormat;

	}

	private int checkDateFormat(char[] dbDate, char delimeter) {
		int countDelimeters = 0;
		for (int i = 0; i < dbDate.length; i++) {
			if (dbDate[i] == delimeter) {
				countDelimeters++;
			}

		}

		return countDelimeters;

	}

	public int getDayLength(String dbDate) throws InvalidDataException {

		String day = "";
		int dayLength = 0;
		if (dbDate.indexOf("-") != -1) {
			int index = dbDate.indexOf("-");
			day = dbDate.substring(0, dbDate.indexOf("-"));
			dayLength = day.length();
		}

		return dayLength;

	}

	public String convertDateFormat(String date) {
		char[] delimiters = { '.', ' ', '-', ',', '/' };
		String newDateFormat = "";
		String delimiter = "";
		if (checkDelimeters(date.toCharArray()) == true) {
			if (date.indexOf(',') != -1) {
				delimiter = ",";
				date = StringUtility.replace(date.trim().toCharArray(), delimiter.toCharArray(), "").toString();
			} else if (date.indexOf('*') != -1) {
				delimiter = "*";
				date = StringUtility.replace(date.trim().toCharArray(), delimiter.toCharArray(), "").toString();
			} else if (date.indexOf('/') != -1) {
				date = StringUtility.replace(date.trim().toCharArray(), delimiter.toCharArray(), "-").toString();
			}
		}

		newDateFormat = StringUtility.replaces(date.trim().toCharArray(), delimiters, "-").toString();
		return newDateFormat;

	}

	private boolean checkDelimeters(char[] date) {
		boolean chkDelimeter = false;
		int count = 0;
		char[] delimiters = { '.', ' ', '-', ',', '/', '*' };
		for (int i = 0; i < date.length; i++) {
			for (int j = 0; j < delimiters.length; j++) {
				if (date[i] == delimiters[j]) {
					System.out.println("Delimeters" + delimiters[j]);
					count++;
				}
			}
		}

		if (count > 2) {
			chkDelimeter = true;
		}

		return chkDelimeter;

	}

	public String readTextfile(String fileName) {
		StringBuffer eachRow = new StringBuffer();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(fileName);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			// String strLine="avad $ sdasf $asdf";

			// Read File Line By Line
			String strLine;
			String tempInfo[] = null;
			while ((strLine = br.readLine()) != null) {

				tempInfo = strLine.split("$");
				// System.out.println("tempinfo length" + tempInfo.length);
				for (int i = 0; i <= tempInfo.length; i++) {
					eachRow.append(tempInfo[0]);
					// System.out.println("eachrow "+eachRow.toString());
					eachRow.append("***");
				}

			}
			// Print the content on the console
			// System.out.println(strLine);

			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return eachRow.toString();
	}

	public HashMap getRegion() {
		HashMap hashmap = new HashMap();
		hashmap.put("1", "East Region");
		hashmap.put("2", "West Region");
		hashmap.put("3", "South Region");
		hashmap.put("4", "North Region");
		hashmap.put("5", "North-East Region");
		hashmap.put("6", "CHQNAD");
		hashmap.put("7", "CHQIAD");
		// code commented to disable CHQ MASTERDATA ON Aug-06
		// hashmap.put("8", "CHQ");
		hashmap.put("8", "RAUSAP");

		return hashmap;
	}

	public TreeMap getRegion1() {

		TreeMap hashmap = new TreeMap();
		hashmap.put("1", "CHQNAD");
		hashmap.put("2", "North Region");
		hashmap.put("3", "East Region");
		hashmap.put("4", "West Region");
		hashmap.put("5", "South Region");
		hashmap.put("6", "North-East Region");
		hashmap.put("7", "RAUSAP");
		// code commented to disable CHQ MASTERDATA ON Aug-06
		// hashmap.put("8", "CHQ");
		hashmap.put("8", "CHQIAD");

		return hashmap;
	}

	public HashMap getRegionsForComparativeReport() {
		HashMap hashmap = new LinkedHashMap();
		hashmap.put("1", "East Region");
		hashmap.put("2", "West Region");
		hashmap.put("3", "South Region");
		hashmap.put("4", "North Region");
		hashmap.put("5", "North-East Region");
		hashmap.put("6", "CHQNAD");
		hashmap.put("7", "IAD-AmritsarIAD");
		hashmap.put("8", "IAD-CAP IAD");
		hashmap.put("9", "IAD-CHENNAI IAD");
		hashmap.put("10", "IAD-CSIA IAD");
		hashmap.put("11", "IAD-DPO IAD");
		hashmap.put("12", "IAD-IGI IAD");
		hashmap.put("13", "IAD-IGICargo IAD");
		hashmap.put("14", "IAD-KOLKATA");
		hashmap.put("15", "IAD-KOLKATA PROJ");
		hashmap.put("16", "IAD-OFFICE COMPLEX");
		hashmap.put("17", "IAD-TRIVANDRUM IAD");
		hashmap.put("18", "RAUSAP");

		return hashmap;
	}

	public String getFinYear(String monthYear) throws InvalidDataException {
		String fromYear = "", finYear = "", month = "";

		fromYear = this.converDBToAppFormat(monthYear, "dd-MMM-yyyy", "yyyy");
		month = this.converDBToAppFormat(monthYear, "dd-MMM-yyyy", "MM");
		if (Integer.parseInt(month) <= 3) {
			finYear = (Integer.parseInt(fromYear) - 1) + "-" + fromYear;
		} else {
			finYear = fromYear + "-" + (Integer.parseInt(fromYear) + 1);
		}

		return finYear;
	}

	private String validateAlphabetic(char[] frmtDt) {
		StringBuffer buff = new StringBuffer();
		StringBuffer digitBuff = new StringBuffer();
		StringBuffer finalBuff = new StringBuffer();
		String validDate = "";

		for (int i = 0; i < frmtDt.length; i++) {
			char c = frmtDt[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				buff.append(c);

			} else if (c >= '0' && c <= '9') {
				digitBuff.append(c);
			}

		}
		String validDt = buff.toString();
		// System.out.println("validateAlphaNumber=Before=(validDt)="+validDt);
		// System.out.println("validateAlphaNumber==="+validDate);
		return validDate;
	}

	public String validateNumber(char[] frmtDt) {
		StringBuffer buff = new StringBuffer();
		StringBuffer digitBuff = new StringBuffer();
		StringBuffer finalBuff = new StringBuffer();
		String validDate = "";

		for (int i = 0; i < frmtDt.length; i++) {
			char c = frmtDt[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				buff.append(c);

			} else if (c >= '0' && c <= '9') {
				digitBuff.append(c);
			}

		}
		String validDt = digitBuff.toString() + "," + buff.toString();
		System.out.println("validateAlphaNumber=Before=(validDt)=" + validDt);
		// System.out.println("validateAlphaNumber==="+validDate);
		return validDt;
	}

	public String validateEmployeeName(char[] frmtDt) {
		StringBuffer buff = new StringBuffer();
		StringBuffer digitBuff = new StringBuffer();
		StringBuffer finalBuff = new StringBuffer();
		String validDate = "";

		for (int i = 0; i < frmtDt.length; i++) {
			char c = frmtDt[i];
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				buff.append(c);
			} else if (c >= '0' && c <= '9') {
				buff.append(c);
			} else if (c >= ' ') {
				digitBuff.append(c);
			}

		}
		String employeeName = buff.toString();
		System.out.println("validateAlphaNumber=Before=(validDt)=" + employeeName);
		// System.out.println("validateAlphaNumber==="+validDate);
		return employeeName;
	}

	public static String getDatetoString(Date dt, String format) {

		// SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat to = new SimpleDateFormat(format);
		String convertDate = "";
		convertDate = to.format(dt);
		// log.info("Date is" + convertDate);

		return convertDate;
	}

	public String getCurrentDate(String format) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		date = sdf.format(new Date());
		return date;
	}

	public String replaceAllWords2(String original, String find, String replacement) {
		StringBuffer result = new StringBuffer(original.length());

		StringTokenizer st = new StringTokenizer(original);
		while (st.hasMoreTokens()) {
			String w = st.nextToken();
			if (w.equals(find)) {
				result.append(replacement);
			} else {
				result.append(w);
			}
		}
		return result.toString();
	}

	public int getYeareDifference(String date1, String date2) {
		long noOfDays = 0;
		int days = 0;
		String validatDt1 = "";
		String validatDt2 = "";
		SimpleDateFormat from = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

		try {
			validatDt1 = sdf.format(from.parse(date1));
			validatDt2 = sdf.format(from.parse(date2));
			days = Integer.parseInt(validatDt1) - Integer.parseInt(validatDt2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("no of days(getYeareDifference)" + noOfDays);

		return days;

	}

	public ArrayList getAirportsByFinanceTbl(String region) {
		Connection con = null;
		Statement st = null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "", unitCode = "";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "SELECT distinct AIRPORTCODE  FROM mv_employee_pension_airports where region='" + region
					+ "'";
			log.info("getAirportsByFinanceTbl==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("AIRPORTCODE") != null) {
					unitName = rs.getString("airportcode").trim();
					bean.setStation(unitName);
				} else {
					bean.setStation("");
				}

				airportList.add(bean);

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}

	public ArrayList getAirports(String region) {
		Connection con = null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "", unitCode = "";

		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			String sql = "select * from employee_unit_master where region in ('" + region
					+ "','CHQIAD') order by region";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				unitName = (String) rs.getString("unitname").toString().trim();

				if (!unitName.equals("")) {
					bean.setStation(unitName.toUpperCase());
				} else {
					bean.setStation("");
				}
				if (!unitName.equals("")) {
					bean.setStationWithRegion(unitName.toUpperCase() + " - " + rs.getString("region"));
				} else {
					bean.setStationWithRegion("");
				}

				unitCode = (String) rs.getString("unitcode").toString().trim();
				if (!unitCode.equals("")) {

					bean.setUnitCode(unitCode.toUpperCase());
				} else {
					bean.setUnitCode("");
				}

				airportList.add(bean);
				// bean=null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;
	}

	public ArrayList getModuleList() {
		Connection con = null;
		Statement st = null;
		ArrayList userList = new ArrayList();
		ResultSet rs = null;
		String modulecode = "", modulename = "";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "SELECT distinct modulecode,modulename FROM employee_rpfc_modules";
			log.info("getusername==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("modulename") != null) {
					modulecode = rs.getString("modulename").trim();
					bean.setUserName(modulecode);
				} else {
					bean.setUserName("");
				}
				if (rs.getString("modulecode") != null) {
					modulename = rs.getString("modulecode").trim();
					bean.setRemarks(modulename);
				} else {
					bean.setRemarks("");
				}
				userList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());
		}
		return userList;
	}

	public ArrayList getUserList() {
		Connection con = null;
		Statement st = null;
		ArrayList userList = new ArrayList();

		ResultSet rs = null;
		String username = "";

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "SELECT distinct username  FROM employee_user where usertype!='Employee' order by username";
			log.info("getusername==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("username") != null) {
					username = rs.getString("username").trim();
					bean.setUserName(username);
				} else {
					bean.setUserName("");
				}

				userList.add(bean);

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return userList;

	}

	public String duplicateWords(String words) {
		String tempWords = "";
		Set s = new HashSet();
		String[] wordList = words.split("=");
		for (int i = 0; i < wordList.length; i++) {
			if (!s.add(wordList[i]))
				log.info("Duplicate detected : " + wordList[i]);
		}
		Iterator It = s.iterator();
		int n = 0;
		while (It.hasNext()) {
			tempWords = tempWords + "=" + It.next();
		}
		return tempWords;
	}

	public int getRowCount(ResultSet set) throws SQLException {
		int rowCount;
		int currentRow = set.getRow(); // Get current row
		rowCount = set.last() ? set.getRow() : 0; // Determine number of rows
		if (currentRow == 0) // If there was no current row
			set.beforeFirst(); // We want next() to go to first row
		else
			// If there WAS a current row
			set.absolute(currentRow); // Restore it
		return rowCount;
	}

	/*
	 * public static Date getStringtoDate(String dt) {
	 * log.info("CommonUtil:getStringtoDate-- Entering Method"); SimpleDateFormat
	 * from = new SimpleDateFormat("dd/MMM/yyyy"); SimpleDateFormat to = new
	 * SimpleDateFormat("yyyy-MM-dd"); String str = ""; Date date = null; try { str
	 * = to.format(from.parse("01/07/06")); date = Date.parse(str); log.info("----"
	 * + date); } catch (ParseException e) { e.printStackTrace(); }
	 * log.info("CommonUtil:getStringtoDate-- Leaving Method"); return date; }
	 */

	/*
	 * public static String getDatetoString(Date dt,String format) {
	 * //log.info("CommonUtil:getDatetoString-- Entering Method"); //
	 * SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat
	 * to = new SimpleDateFormat(format); String convertDate = ""; convertDate =
	 * to.format(dt); // log.info("Date is" + convertDate);
	 * 
	 * //log.info("CommonUtil:getDatetoString-- Leaving Method"); return
	 * convertDate; } public static String getDatetoString1(Date dt) {
	 * //log.info("CommonUtil:getDatetoString-- Entering Method"); //
	 * SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat
	 * to = new SimpleDateFormat("dd/MMM/yy"); String convertDate = ""; convertDate
	 * = to.format(dt); log.info("Date is" + convertDate);
	 * 
	 * //log.info("CommonUtil:getDatetoString-- Leaving Method"); return
	 * convertDate; } public String getDate1(String st) { SimpleDateFormat
	 * fullMonthFormat = new SimpleDateFormat("dd/MM/yyyy"); SimpleDateFormat
	 * toMonthFormat = new SimpleDateFormat("dd-MMM-yyyy"); SimpleDateFormat df =
	 * new SimpleDateFormat("dd/MM/yyyy"); String date2 = ""; Date dateR; try { //
	 * Date data = df.parse("02/20/2003"); // String date1 =
	 * fullMonthFormat.format(data); dateR = fullMonthFormat.parse(st);
	 * 
	 * String date1 = fullMonthFormat.format(dateR); date2 =
	 * toMonthFormat.format(fullMonthFormat.parse(date1));
	 * System.out.println("date1=====" + date1); System.out.println("date2=====" +
	 * date2); } catch (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return date2; } public String converFormat(String
	 * date) { Calendar cal = Calendar.getInstance(); String convertedDt = ""; try {
	 * SimpleDateFormat fromDate = new SimpleDateFormat("yyyyMM"); //
	 * SimpleDateFormat fromDate = new SimpleDateFormat("mmmmm"); log.info("date" +
	 * date); SimpleDateFormat toDate = new SimpleDateFormat("dd-MMM-yy");
	 * convertedDt = toDate.format(fromDate.parse(date)); log.info("convertedDt" +
	 * convertedDt); } catch (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return convertedDt; }
	 * 
	 */
	/*
	 * public static String getStringtoDate(String dt) { SimpleDateFormat to = new
	 * SimpleDateFormat("dd/MMM/yyyy"); SimpleDateFormat from = new
	 * SimpleDateFormat("yyyy-MM-dd"); String str = ""; java.sql.Date date = null;
	 * try { str = to.format(from.parse(dt));
	 * 
	 * System.out.println("----" + str); } catch (ParseException e) { str = dt;
	 * e.printStackTrace(); } return str; }
	 * 
	 * public String converteFormToDateFormat(String date, String format) { String
	 * convertedDTFormat = ""; SimpleDateFormat from = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat to = new
	 * SimpleDateFormat(format); try { convertedDTFormat =
	 * to.format(from.parse(date)); } catch (ParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return convertedDTFormat; }
	 */

	public static String getExceptionMessage(String code) {
		String msg = "";
		try {
			if (code != null)
				msg = ResourceBundle.getBundle("aims.resources.exceptionCodes").getString(code.trim());
		} catch (Exception ex) {
			log.printStackTrace(ex);
		}

		return msg;
	}

	public ArrayList loadRegions() {
		Connection con = null;
		Statement st = null;
		ArrayList airportList = new ArrayList();
		ResultSet rs = null;
		RegionBean bean = null;
		String region = "", aaiCategory = "";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "SELECT REGIONNAME,AAICATEGORY FROM EMPLOYEE_REGION_MASTER WHERE SORTEDCOL IS NOT NULL ORDER BY SORTEDCOL";
			log.info("loadRegions==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				bean = new RegionBean();
				if (rs.getString("REGIONNAME") != null && rs.getString("AAICATEGORY") != null) {
					region = rs.getString("REGIONNAME").trim();
					aaiCategory = rs.getString("AAICATEGORY").trim();
					if (aaiCategory.equals("METRO AIRPORT")) {

						bean.setAirportcode(region);
						bean.setRegion("CHQIAD");
					} else {
						bean.setAirportcode("-NA-");
						bean.setRegion(region);
					}
					bean.setAaiCategory(aaiCategory);
					log.info("Region" + bean.getRegion() + "Airportcode" + bean.getAirportcode());

				}
				airportList.add(bean);
			}
		} catch (Exception e) {
			log.printStackTrace(e);
		}
		return airportList;

	}

	public String leadingZeros(int numOfLeadingZeros, String number) {
		int i = 0;
		i = number.length();
		if (i == numOfLeadingZeros) {
			return number;
		} else {
			int j = numOfLeadingZeros - i;
			for (int k = 0; k < j; k++) {
				number = "0" + number;
			}
			/* log.info("converted number is "+number); */
		}
		return number;
	}

	public String trailingZeros(char[] number) {
		StringBuffer buff = new StringBuffer();
		boolean flag = false;
		// System.out.println("trailingZeros=Before=(validDt)=" +
		// number.length);
		for (int i = 0; i < number.length; i++) {
			char c = number[i];
			if (c != '0' || flag == true) {
				flag = true;
				buff.append(c);
			}
		}
		String trailingNumber = buff.toString();
		// log.info("trailingNumber " +trailingNumber);
		return trailingNumber;
	}

	public String convertToLetterCase(String letter) {
		String tmpStr = "", tmpChar = "", preString = "", postString = "";
		int strLen = 0, index = 0;
		tmpStr = letter.toLowerCase();
		strLen = tmpStr.length();
		if (strLen > 0) {
			for (index = 0; index < strLen; index++) {
				if (index == 0) {
					tmpChar = tmpStr.substring(0, 1).toUpperCase();
					postString = tmpStr.substring(1, strLen);
					tmpStr = tmpChar + postString;
				} else {
					tmpChar = tmpStr.substring(index, index + 1);
					if (tmpChar.equals(" ") && index < (strLen - 1)) {
						tmpChar = tmpStr.substring(index + 1, index + 2).toUpperCase();
						preString = tmpStr.substring(0, index + 1);
						postString = tmpStr.substring(index + 2, strLen);
						tmpStr = preString + tmpChar + postString;
					}
				}
			}
		}
		return tmpStr;
	}

	public String getSearchPFID(String pfid) {
		String finalPFId = "";
		int startIndex = 0;
		startIndex = getStringIndex(pfid);
		if (startIndex == 0) {
			if (pfid.length() == 11) {
				startIndex = 5;
			} else {
				startIndex = -1;
			}
		}
		if (startIndex != 7) {
			startIndex = -1;
		}

		finalPFId = pfid.substring(startIndex + 1, pfid.length());
		finalPFId = this.trailingZeros(finalPFId.toCharArray());
		return finalPFId;
	}

	public int getStringIndex(String str) {
		int startIndex = 0;
		if (str == null)
			return 0;

		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i)))
				startIndex = i;
		}

		return startIndex;
	}

	public String AddMonth(String date) throws Exception {
		String datestring[] = date.split("-");
		String day = datestring[0];
		String month = datestring[1];
		month = String.valueOf(Integer.parseInt(month) + 1);
		if (Integer.parseInt(month) + 1 < 10) {
			month = "0" + month;
		} else {
			month = month;
		}
		if (Integer.parseInt(day) > 27) {
			day = "01";
		} else {
			day = day;
		}
		String year = datestring[2];

		return this.converDBToAppFormat(day + "-" + month + "-" + year, "dd-MM-yyyy", "dd-MMM-yyyy");

	}

	static String a1[] = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };

	static String a2[] = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

	static String a3[] = { "", "Eleven", "Twelve", "Thirteen", "Forteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
			"Nineteen" };

	static String a4[] = { "", "Hundred", "Thousand", "Lakh", "Crore" };

	public static String ConvertInWords(double d) {
		double t = 0, n = 0;
		String m = "", S = "";
		String InWords = "";

		if (d == 0) {
			S = "Zero";
			InWords = S;
		} else {
			if (d < 0) {
				d = -d;
				m = "Minus";
			}

			t = (float) (d - (int) d) * 100;
			if (t != 0)
				S = " Paise " + GetWords((int) t);

			n = (int) d;

			t = n % 100;
			S = GetWords((int) t) + S;

			n = (int) (n / 100);
			t = n % 10;
			if (t != 0)
				S = GetWords((int) t) + " Hundred " + (S.trim().equals("") ? "" : "and " + S);

			n = (int) (n / 10);
			t = n % 100;
			if (t != 0)
				S = GetWords((int) t) + " Thousand " + S;

			n = (int) (n / 100);
			t = n % 100;
			if (t != 0)
				S = GetWords((int) t) + " Lakh " + S;

			n = (int) (n / 100);
			t = n % 100;
			if (t != 0)
				S = GetWords((int) t) + " Crores " + S;

			n = (int) (n / 100);
			t = n % 10;
			if (t != 0)
				S = GetWords((int) t) + " Hundred " + (S.indexOf("Crores") > 0 ? S : "Crores" + S);

			n = (int) (n / 10);
			t = n % 100;
			if (t != 0)
				S = GetWords((int) t) + " Thousand " + (S.indexOf("Crores") > 0 ? S : "Crores" + S);

			InWords = m.equals("") ? m + " " + S : S;
		}
		return (InWords);
	}

	public static String GetWords(int n) {

		String GetWords = "";
		if (n % 10 == 0) {
			if (n >= 10)
				GetWords = a2[(int) (n / 10)].trim();
		} else {
			if (n < 10)
				GetWords = a1[n % 10].trim();
			else {
				if (n < 20)
					GetWords = a3[n % 10].trim();
				else
					GetWords = a2[(int) (n / 10)].trim() + " " + a1[n % 10].trim();
			}
		}
		return (GetWords);
	}

	public void createSheet(ArrayList finalDataList, String airport, WritableWorkbook workbook, ArrayList hdrsList)
			throws WriteException {
		String data = "";
		log.info("list size is " + finalDataList.size());
		ArrayList finalDtList = new ArrayList();
		WritableSheet s = workbook.createSheet(airport, 0);
		WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
		WritableCellFormat cf = new WritableCellFormat(wf);
		cf.setWrap(true);

		EmpMasterBean formBean = new EmpMasterBean();
		for (int i = 0; i < hdrsList.size(); i++) {

			s.addCell(new Label(i, 0, hdrsList.get(i).toString()));
		}
		WritableCellFormat cellFormat;

		for (int k = 0; k < finalDataList.size(); k++) {
			int row = k;
			log.info("inside for loop");
			formBean = (EmpMasterBean) finalDataList.get(k);
			WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.TEXT);
			// NumberCell fc1 = (NumberCell)
			// Integer.parseInt(formBean.getCpfaccno());
			// System.out.println(isInteger(formBean.getCpfaccno()));
			s.addCell(new Label(0, row, formBean.getEmpSerialNo()));
			s.addCell(new Label(1, row, formBean.getEmpNumber()));
			s.addCell(new Label(2, row, formBean.getEmpName()));
			/*
			 * if(isInteger(formBean.getEmployeeNo())==true){ s.addCell(new
			 * Number(0,row,Double.parseDouble(formBean.getEmployeeNo()))); }else{
			 * s.addCell(new Label(2,row,formBean.getEmployeeNo())); }
			 */

			s.addCell(new Label(3, row, formBean.getDesegnation()));
			// s.addCell(new Label(4,row,formBean.getEmoluments()));
			// s.addCell(new Label(5,row,formBean.getEmployeePF()));
			s.addCell(new Label(4, row, formBean.getPaidDate()));
			s.addCell(new Label(5, row, formBean.getEmoluments()));
			s.addCell(new Label(6, row, formBean.getBasic()));
			// row++;
		}

	}

	public ArrayList getForm6Region() {
		ArrayList hashmap = new ArrayList();
		hashmap.add("CHQNAD");
		hashmap.add("North Region");
		hashmap.add("East Region");
		hashmap.add("West Region");
		hashmap.add("South Region");
		hashmap.add("North-East Region");
		hashmap.add("RAUSAP");
		hashmap.add("CHQIAD");
		return hashmap;
	}

	public String getSearchPFID1(String pfid) {
		// log.info("inetial pfid " +pfid);
		String finalPFId = "";
		if (pfid.length() >= 5) {
			finalPFId = pfid.substring(pfid.length() - 5, pfid.length());
			finalPFId = this.trailingZeros(finalPFId.toCharArray());
		} else {
			finalPFId = pfid;
		}
		// log.info("finai pfId" +finalPFId);
		return finalPFId;
	}

	public String getMonthYear(String month, String year) {
		String fullMonthName = "", frmMonthYear = "", disMonthYear = "", displayDate = "";
		if (!month.equals("00")) {
			frmMonthYear = "%" + "-" + month + "-" + year;
			disMonthYear = month + "-" + year;
			try {
				displayDate = this.converDBToAppFormat(disMonthYear, "MM-yyyy", "MMM-yyyy");

			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
		}
		return displayDate;

	}

	public boolean compareTwoDates(String date1, String date2) {
		Date compareWthDt = new Date();
		Date comparedDt = new Date();
		boolean finalDateFlag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		try {
			compareWthDt = dateFormat.parse(date1);
			comparedDt = dateFormat.parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalDateFlag = comparedDt.after(compareWthDt);
		// System.out.println("compareWthDt" +date1 +"comparedDt "+date2);

		return finalDateFlag;
	}

	public boolean compareTwoDatesUsingDB(Connection con, String date1, String date2) {
		Statement st = null;
		ResultSet rs = null;
		boolean finalDateFlag = false;
		String qry = "select 'X' as flag from dual where to_date('" + date1 + "')>to_date('" + date2 + "')";
		log.info(qry);
		try {
			st = con.createStatement();
			rs = st.executeQuery(qry);
			if (rs.next()) {
				finalDateFlag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return finalDateFlag;
	}

	public String getFromToDates(String fromYear, String toYear, String fromMonth, String toMonth)
			throws InvalidDataException {
		String fromDate = "", toDate = "";
		StringBuffer buffer = new StringBuffer();
		if (toYear.equals("")) {
			toYear = String.valueOf(Integer.parseInt(fromYear) + 1);
		}

		if (!fromYear.equals("NO-SELECT")) {

			if (!fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-" + fromMonth + "-" + fromYear;
				if (Integer.parseInt(toMonth) >= 1 && Integer.parseInt(toMonth) <= 3) {
					fromDate = "01-" + toMonth + "-" + toYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else {
					toDate = "01-" + toMonth + "-" + fromYear;
				}

			} else if (fromMonth.equals("NO-SELECT") && !toMonth.equals("NO-SELECT")) {
				fromDate = "01-04-" + fromYear;
				if (Integer.parseInt(toMonth) >= 1 && Integer.parseInt(toMonth) <= 3) {
					fromDate = "01-" + toMonth + "-" + toYear;
					toDate = "01-" + toMonth + "-" + toYear;
				} else {
					toDate = "01-" + toMonth + "-" + fromYear;
				}

			} else if (!fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")) {
				fromDate = "01-" + fromMonth + "-" + fromYear;
				if (Integer.parseInt(toMonth) >= 1 && Integer.parseInt(toMonth) <= 3) {
					toDate = "01-" + toMonth + "-" + toYear;
				} else {
					toDate = "01-03-" + toYear;
				}

			} else if (fromMonth.equals("NO-SELECT") && toMonth.equals("NO-SELECT")) {
				fromDate = "01-04-" + fromYear;
				toDate = "01-03-" + toYear;
			}

		} else {
			fromDate = "01-04-1995";
			toDate = "01-03-" + this.getCurrentDate("yyyy");
		}
		buffer.append(this.converDBToAppFormat(fromDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		buffer.append(",");
		buffer.append(this.converDBToAppFormat(toDate, "dd-MM-yyyy", "dd-MMM-yyyy"));
		return buffer.toString();
	}

	public int GetDaysInMonth(int month, int year) {
		if (month < 1 || month > 12) {
			try {
				throw new InvalidDataException("month" + month + "month must be between 1 and 12");
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				log.printStackTrace(e);
			}
		}
		if (1 == month || 3 == month || 5 == month || 7 == month || 8 == month || 10 == month || 12 == month) {
			return 31;
		} else if (2 == month) {
			// Check for leap year
			if (0 == (year % 4)) {
				// If date is divisible by 400, it's a leap year.
				// Otherwise, if it's divisible by 100 it's not.
				if (0 == (year % 400)) {
					return 29;
				} else if (0 == (year % 100)) {
					return 28;
				}

				// Divisible by 4 but not by 100 or 400
				// so it leaps
				return 29;
			}
			// Not a leap year
			return 28;
		}
		return 30;
	}

	public static boolean checkIfNumber(String in) {

		try {

			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	public ArrayList getVefifiedPFIDList(String fromID, String toID) {
		Connection con = null;
		Statement st = null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String condition = "";
		if (!fromID.equals("")) {
			condition = "between '" + fromID + "' and '" + toID + "'";
		}
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "select pensionno,employeename,airportcode,region from employee_personal_info info where pensionno "
					+ condition + "  and (pcreportverified='Y' or finalsettlmentdt is not null)";
			log.info("==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				EmpMasterBean bean = new EmpMasterBean();
				if (rs.getString("pensionno") != null) {
					bean.setPfid(rs.getString("pensionno"));
				} else {
					bean.setPfid("");
				}
				if (rs.getString("employeename") != null) {
					bean.setEmpName(rs.getString("employeename"));
				} else {
					bean.setEmpName("");
				}
				airportList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return airportList;

	}

	public static String findWords(String digits) throws NumberFormatException {
		// this is the function to display numbers into words upto 16 digits.
		double croreDigits = 0;
		int lakhDigits = 0;
		String number = String.valueOf(digits);
		String crores = "";
		String lakhs = "";
		String paisa = "";
		String finalWords = "";
		int point = number.indexOf(".");
		if (point != -1) {
			int p = Integer.parseInt(number.substring(point + 1, number.length()));
			if (p != 0)
				paisa = " and " + uptoThreeDigit(p) + " Paise ";
			number = number.substring(0, point);
		}
		if (number.length() > 7) {
			try {
				croreDigits = Double.parseDouble(number.substring(0, (number.length() - 7)));
				lakhDigits = Integer.parseInt(number.substring((number.length() - 7), number.length()));
			} catch (NumberFormatException nf_ex) {
				throw nf_ex;
			}
			crores = aboveThreeUptoSevenDigits(croreDigits) + " crore(s) and ";
			lakhs = aboveThreeUptoSevenDigits(lakhDigits);
		} else {
			try {
				lakhDigits = Integer.parseInt(number.substring(0, number.length()));
				if (lakhDigits == 0)
					return "Zero Rupees Only";
			} catch (NumberFormatException nf_ex) {
				throw nf_ex;
			}
			lakhs = aboveThreeUptoSevenDigits(lakhDigits);
		}
		finalWords = ((crores + lakhs + " Rupees " + paisa + " Only").trim());
		return finalWords;
	}

	public static String findWords1(String digits) throws NumberFormatException {
		// this is the function to display numbers into words upto 16 digits.
		double croreDigits = 0;
		int lakhDigits = 0;
		String number = String.valueOf(digits);
		String crores = "";
		String lakhs = "";
		String paisa = "";
		String finalWords = "";
		int point = number.indexOf(".");
		if (point != -1) {
			int p = Integer.parseInt(number.substring(point + 1, number.length()));
			if (p != 0)
				paisa = " and Paise " + uptoThreeDigit(p);
			number = number.substring(0, point);
		}
		if (number.length() > 7) {
			try {
				croreDigits = Double.parseDouble(number.substring(0, (number.length() - 7)));
				lakhDigits = Integer.parseInt(number.substring((number.length() - 7), number.length()));
			} catch (NumberFormatException nf_ex) {
				throw nf_ex;
			}
			crores = aboveThreeUptoSevenDigits(croreDigits) + " crore(s) and ";
			lakhs = aboveThreeUptoSevenDigits(lakhDigits);
		} else {
			try {
				lakhDigits = Integer.parseInt(number.substring(0, number.length()));
				if (lakhDigits == 0)
					return "Rupees Zero Only";
			} catch (NumberFormatException nf_ex) {
				throw nf_ex;
			}
			lakhs = aboveThreeUptoSevenDigits(lakhDigits);
		}
		finalWords = (("Rupees " + crores + lakhs + "" + paisa + " Only").trim());
		return finalWords;
	}

	public static String aboveThreeUptoSevenDigits(double digits) {
		String words = "";
		if (digits > 9999999 && digits <= 999999999) {
			words = uptoThreeDigit((int) (digits / 10000000)) + " crore ";
			digits %= 10000000;
		}
		if (digits > 99999 && digits <= 9999999) {
			words += uptoThreeDigit((int) (digits / 100000)) + " Lakh ";
			digits %= 100000;
		}
		if (digits > 999 && digits <= 99999) {
			words += uptoThreeDigit((int) (digits / 1000)) + " Thousand ";
			digits %= 1000;
		}
		words += uptoThreeDigit((int) digits);
		return words;
	}

	public static String uptoThreeDigit(int digits) {
		String words = "";
		if (digits > 99 && digits <= 999) {
			words = units[digits / 100] + " Hundred ";
			digits %= 100;
		}
		if (digits > 9 && digits <= 20)
			words += tenPlus[digits - 10] + " ";
		else if (digits <= 99)
			words += " " + tens[digits / 10] + " " + units[digits % 10] + " ";
		return words;
	}

	public static long getDifferenceTwoDatesInDays(String retirmentDt, String currentDate) {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		long days = 0;
		try {
			Date oldDate = df.parse(retirmentDt);
			Date newDate = df.parse(currentDate);
			days = ((oldDate.getTime() - newDate.getTime()) / (24 * 60 * 60 * 1000));
			System.out.println(days);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return days;
	}

	public void renameFile(String oldFileName, String newFileName) {

		File originalFile = new File(oldFileName);
		/**
		 * Check if file exists
		 */
		boolean fileExists = originalFile.exists();

		/**
		 * Check if it is a directory
		 */
		boolean isDirectory = originalFile.isDirectory();

		/**
		 * If file does not exist, return
		 */
		if (!fileExists) {

			System.out.println("File does not exist: " + oldFileName);
			System.out.println("Rename Operation Aborted.");
			return;
		}
		/**
		 * If file is a directory, return
		 */
		if (isDirectory) {
			System.out.println("The parameter you have provided is a directory: " + oldFileName);
			System.out.println("Rename Operation Aborted.");
			return;
		}

		File renamedFile = new File(newFileName);

		boolean renamed = originalFile.renameTo(renamedFile);
		if (renamed) {
			System.out.println("File Has Been Renamed Successfully.");
			System.out.println("Original File Name: " + oldFileName);
			System.out.println("Rename File Name: " + newFileName);
		} else {
			System.out.println("Error occurred while trying to rename the file.");
		}

	}

	/*
	 * public List getExtractClobDataList(List extractData) throws
	 * FileNotFoundException { AdjExtractData extractBean = new AdjExtractData();
	 * List outExtractList=new LinkedList(); InputStream clobOutputStream = null;
	 * byte[] byteBuffer; int bytesRead = 0, totBytesRead = 0, totBytesWritten =
	 * 0,bufferSize = 0; String filename,calculsheetfolder,form2sheetfolder;
	 * ResourceBundle bundle=
	 * ResourceBundle.getBundle("aims.resource.ApplicationResouces");
	 * calculsheetfolder=bundle.getString("upload.folder.path.epf.cal");
	 * form2sheetfolder=bundle.getString("upload.folder.path.epf.form2");
	 * FileOutputStream fstream = null; for (int i = 0; i < extractData.size(); i++)
	 * { extractBean = (AdjExtractData) extractData.get(i);
	 * if(extractBean.getSheettype().equals("AAIEPF2_REC")){
	 * filename=form2sheetfolder+"\\"+extractBean.getSheetname(); }else{
	 * filename=calculsheetfolder+"\\"+extractBean.getSheetname(); }
	 * log.info("CommonUtil:getExtractClobData"+filename); try { bufferSize =
	 * extractBean.getXlsdocument().getBufferSize(); byteBuffer = new
	 * byte[bufferSize]; clobOutputStream = extractBean.getXlsdocument()
	 * .getAsciiStream(); fstream = new FileOutputStream(filename); while
	 * ((bytesRead = clobOutputStream.read(byteBuffer)) != -1) { // After reading a
	 * buffer from the text file, write the contents // of the buffer to the output
	 * stream using the write() // method.
	 * 
	 * fstream.write(byteBuffer, 0, bytesRead); totBytesRead += bytesRead;
	 * totBytesWritten += bytesRead; } clobOutputStream.close(); fstream.close();
	 * byteBuffer = null; bytesRead = 0; totBytesRead = 0; totBytesWritten = 0;
	 * outExtractList.add(filename); } catch (SQLException e) { // TODO
	 * Auto-generated catch block log.printStackTrace(e); } catch (IOException e) {
	 * // TODO Auto-generated catch block log.printStackTrace(e); } } return
	 * outExtractList; }
	 */
	public String getExtractClobData(BLOB xlsdocument, String sheetname, String type) throws FileNotFoundException {

		List outExtractList = new LinkedList();
		InputStream clobOutputStream = null;
		byte[] byteBuffer;
		int bytesRead = 0, totBytesRead = 0, totBytesWritten = 0, bufferSize = 0;
		long cloblength = 0;
		long position;
		String filename, calculsheetfolder, form2sheetfolder;
		ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
		calculsheetfolder = bundle.getString("upload.folder.path.epf.cal");
		form2sheetfolder = bundle.getString("upload.folder.path.epf.form2");
		BufferedWriter fstream = null;

		if (type.equals("AAIEPF2_REC")) {
			filename = form2sheetfolder + "\\" + sheetname;
		} else {
			filename = calculsheetfolder + "\\" + sheetname;
		}
		log.info("CommonUtil:getExtractClobData" + filename);
		try {
			cloblength = xlsdocument.length();
			bufferSize = xlsdocument.getChunkSize();
			byteBuffer = new byte[bufferSize];
			clobOutputStream = xlsdocument.getBinaryStream();

			// FileReader fw=new FileReader(new File(filename));

			fstream = new BufferedWriter(new FileWriter(filename));
			/*
			 * for ( position = 1; position <= cloblength; position += bufferSize) { //
			 * After reading a buffer from the text file, write the contents // of the
			 * buffer to the output stream using the write() // method.
			 * System.out.println(byteBuffer+"======="+bufferSize); bytesRead =
			 * xlsdocument.getChars(position,bufferSize,byteBuffer);
			 * fstream.write(bytesRead); totBytesRead += bytesRead; totBytesWritten +=
			 * bytesRead; }
			 */

			while ((bytesRead = clobOutputStream.read(byteBuffer)) != -1) {

				// After reading a buffer from the text file, write the contents
				// of the buffer to the output stream using the write()
				// method.
				System.out.println(byteBuffer + "=======" + bufferSize);
				// bytesRead =
				// xlsdocument.getChars(position,bufferSize,byteBuffer);
				fstream.write(bytesRead);
				totBytesRead += bytesRead;
				totBytesWritten += bytesRead;

			}

			clobOutputStream.close();
			fstream.close();
			byteBuffer = null;
			bytesRead = 0;
			totBytesRead = 0;
			totBytesWritten = 0;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		}

		return filename;
	}

	public String getUploadFolderPath(String fileType, String year, String month, ResourceBundle appbundle) {

		String path = "", form = "", finyear = "", monthYear = "", pathseperator = "", slashsuffix = "";
		String appfolderPath = appbundle.getString("upload.folder.path.epf.monthlyrecoveries");
		pathseperator = appbundle.getString("upload.folder.path.seperator");
		slashsuffix = appbundle.getString("upload.folder.path.slashsuffix");

		if (fileType.equals("AAIEPF-3")) {

			log.info("=====form - 3");
			form = "Form-3";

		} else if (fileType.equals("AAIEPF-3A")) {
			log.info("=====form - 3 (Arrear)");
			form = "Form-3(Arrear)";

		} else if (fileType.equals("AAIEPF-3-SUPPL")) {
			log.info("=====form - 3 (Suppl)");
			form = "Form-3(Suppl)";
		} else if (fileType.equals("AAIEPF-4")) {
			log.info("=====form - 4");
			form = "Form-4";
		} else if (fileType.equals("AAIEPF-8")) {
			log.info("=====form - 8");
			form = "Form-8";
		}
		if (fileType.equals("ARREARBREAKUP_UPLOAD")) {
			log.info("=====ARREARBREAKUP_UPLOAD");
			form = "ARREARBREAKUP_UPLOAD";
		} else if (fileType.toLowerCase().equals("other")) {
			log.info("=====Other");
			form = "Other";
		}

		if (fileType.equals("ARREARBREAKUP_UPLOAD") || fileType.toLowerCase().equals("other")) {
			String folderpath = appbundle.getString("upload.folder.path");
			path = folderpath + slashsuffix + form;
			log.info(" == ARREARBREAKUP_UPLOAD or  other =" + path);
		}

		if (fileType.equals("AAIEPF-3") || fileType.equals("AAIEPF-3A") || fileType.equals("AAIEPF-3-SUPPL")
				|| fileType.equals("AAIEPF-4") || fileType.equals("AAIEPF-8")) {
			if (Integer.parseInt(month) > 03) {
				finyear = year + pathseperator + (Integer.parseInt(year) + 1);
			} else {
				finyear = (Integer.parseInt(year) - 1) + pathseperator + year;
			}
			log.info("month  ==   " + month);
			if (month.equals("01")) {
				monthYear = "Jan";
			}
			if (month.equals("02")) {
				monthYear = "Feb";
			}
			if (month.equals("03")) {
				monthYear = "Marn";
			}
			if (month.equals("04")) {
				monthYear = "Apr";
			}
			if (month.equals("05")) {
				monthYear = "May";
			}
			if (month.equals("06")) {
				monthYear = "Jun";
			}
			if (month.equals("07")) {
				monthYear = "Jul";
			}
			if (month.equals("08")) {
				monthYear = "Aug";
			}
			if (month.equals("09")) {
				monthYear = "Sep";
			}
			if (month.equals("10")) {
				monthYear = "Oct";
			}
			if (month.equals("11")) {
				monthYear = "Nov";
			}
			if (month.equals("12")) {
				monthYear = "Dec";
			}
			log.info("monthYear==" + monthYear);
			path = appfolderPath + slashsuffix + finyear + slashsuffix + monthYear + slashsuffix + form;
			log.info("Path ====> " + path);
		}

		File filePath = new File(path);

		if (!filePath.exists()) {
			File saveDir = new File(path);
			if (!saveDir.exists())
				saveDir.mkdirs();

		}

		return path;

	}

	public void getImage(Image image, String path) {
		FileOutputStream outputFileOutputStream = null;
		File outputBinaryFile1 = new File(path);
		try {
			outputFileOutputStream = new FileOutputStream(outputBinaryFile1);
			outputFileOutputStream.write(image.getImageData());
			outputFileOutputStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		}

	}

	public String getImageFromDB(String imagepath, String name, String filenames) {
		String xlspath = "", tempPath, imagepaths = "";
		tempPath = imagepath.substring(0, imagepath.lastIndexOf("."));
		imagepaths = imagepath.substring(0, imagepath.lastIndexOf(".")) + ".png";
		filenames = filenames.substring(0, filenames.lastIndexOf(".")) + ".xls";
		xlspath = tempPath + ".xls";
		log.info("getImageFromDB" + xlspath + "tempPath===========" + tempPath + "=========name=========" + name);

		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(xlspath));
			WritableSheet sheet = workbook.createSheet(name, 0);
			/* Convert images file into byte array and passed into writable image */
			File file = new File(imagepath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
				// no doubt here is 0
				/*
				 * Writes len bytes from the specified byte array starting at offset off to this
				 * byte array output stream.
				 */
				System.out.println("read " + readNum + " bytes,");
			}

			WritableImage imgobj = new WritableImage(5, 7, 9, 60, bos.toByteArray());

			sheet.addImage(imgobj);
			sheet.setName(name);
			sheet.setProtected(true);
			workbook.write();
			workbook.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			log.printStackTrace(e);
		}
		return filenames;
	}

	// By Prasad on 04-Apr-2012
	// By Prasad on 06-Dec-2011 for handling validations on Excel Sheet
	// By Prasad on 05-Dec-2011 for handling exceptions from form2 & form 3
	// seperately
	private String readForm2Data(Sheet s) throws BiffException, InvalidDataException {
		log.info("CommonUtil:readForm2Data Entering method");
		Cell cell = null;
		StringBuffer eachRow = new StringBuffer();
		log.info("Columns" + s.getColumns() + "Rows" + s.getRows());
		String delimiter = "*", cellContent = "";

		for (int j = 10; j < s.getRows(); j++) {
			for (int i = 0; i < s.getColumns(); i++) {
				cell = s.getCell(i, j);
				if (s.getCell(1, j).getContents().equals("")) {
					if (!s.getCell(8, j).getContents().equals("") || !s.getCell(19, j).getContents().equals("")
							|| !s.getCell(23, j).getContents().equals("")) {
						try {
							throw new InvalidDataException(
									"Sorry....! PFID field cannot be empty. Please Enter the Valid PFID in the Column Number");
						} catch (InvalidDataException e) {
							// TODO Auto-generated catch block
							throw e;
						}
						/*
						 * if(j+1 < s.getRows()){ try { throw new
						 * InvalidDataException("Sheet contain blank rows in between the Data in Row: "+
						 * (j-1)+" and " +(j+1)); } catch (InvalidDataException e) { // TODO
						 * Auto-generated catch block throw e; } }
						 */

					}
					log.info("Row :" + j + " have Nodata");
				} else {
					if (cell.getType() == CellType.NUMBER_FORMULA) {
						FormulaCell numberFormulaCell = (FormulaCell) cell;

						try {
							throw new InvalidDataException(
									"The formula '" + numberFormulaCell.getFormula() + "' is available in row "
											+ ((cell.getRow()) + 1) + " and column " + (cell.getColumn()));
						} catch (FormulaException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvalidDataException e) {
							// TODO Auto-generated catch block
							throw e;
						}
					}

					if (!cell.getContents().trim().equals("")) {
						cellContent = StringUtility
								.replace(cell.getContents().toCharArray(), delimiter.toCharArray(), "").toString();

						// new code added for invalid data,i.e,validation for special characters
						if (cell.getType() == CellType.NUMBER) {
							if (cellContent.indexOf("(") != -1 || cellContent.indexOf(")") != -1
									|| cellContent.indexOf("}") != -1 || cellContent.indexOf("{") != -1
									|| cellContent.indexOf("*") != -1) {

								try {
									throw new InvalidDataException(
											"The invalid data '" + cell.getContents() + "' exist in Row: "
													+ (cell.getRow() + 1) + " and Column: " + (cell.getColumn() + 1));

								} catch (InvalidDataException e) {
									// TODO Auto-generated catch block
									throw e;
								}
							}
						}

						if (!cellContent.trim().equals("")) {
							eachRow.append(cell.getContents() + "@");
						} else {
							eachRow.append("XXX" + "@");
						}

					} else {
						eachRow.append("XXX" + "@");
					}
				}

			}

			eachRow.append("***");
		}
		log.info("CommonUtil:readForm2Data Leaving method");
		return eachRow.toString();
	}

	public boolean compareTwoDates1(String date1, String date2) {
		Date compareWthDt = new Date();
		Date comparedDt = new Date();
		boolean finalDateFlag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
		try {
			compareWthDt = dateFormat.parse(date1);
			comparedDt = dateFormat.parse(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finalDateFlag = comparedDt.equals(compareWthDt);
		// System.out.println("compareWthDt" +date1 +"comparedDt "+date2);

		return finalDateFlag;
	}

	public String getFinalSettlemetDate(String pfid) {
		Connection con = null;
		Statement st = null;
		String stdtList = "";
		ResultSet rs = null;
		String finyear = "", pensionno = "";
		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			String query = "SELECT FINYEAR   FROM employee_resettlement_dtl where pensionno='" + pfid
					+ "' order by FINYEAR";
			log.info("getusername==query===========" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				if (rs.getString("FINYEAR") != null) {
					finyear = rs.getString("FINYEAR").trim();

				} else {

				}
				stdtList += "," + finyear;

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return stdtList;

	}

	public long getDateDifferenceDays(String date1, String date2) {
		long noOfDays = 0;
		int days = 0;
		Date validatDt1 = new Date();
		Date validatDt2 = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try {
			validatDt1 = sdf.parse(date1);
			validatDt2 = sdf.parse(date2);
			noOfDays = (validatDt2.getTime() - validatDt1.getTime());
			noOfDays = (noOfDays / (1000L * 60L * 60L * 24L));
			log.info("--" + validatDt2.getTime() + "--" + validatDt1.getTime() + "--" + noOfDays);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return noOfDays;
	}

	public String getRAUAirports(String region) {
		Connection con = null;
		ArrayList airportList = new ArrayList();

		ResultSet rs = null;
		String unitName = "", unitCode = "";
		StringBuffer stations = new StringBuffer();
		try {
			con = commonDB.getConnection();
			Statement st = con.createStatement();
			String sql = "select unitname from employee_unit_master where upper(region)  ='" + region
					+ "' and accounttype='RAU' order by unitname";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if (rs.getString("unitname") != null) {
					unitName = (String) rs.getString("unitname").toString().trim();
					stations.append(unitName);
					stations.append("','");
				} else {
					stations.append("");
				}
			}
			airportList.add(stations);
			// log.info("=====stations========"+stations.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("error" + e.getMessage());

		}
		return stations.toString();

	}
	
	

}
