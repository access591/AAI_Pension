<%@ page language="java"%>
<%@ page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@ page import="aims.service.PensionService"%>
<%@ page import="aims.common.InvalidDataException"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="java.util.ResourceBundle"%>
<%@page import="org.apache.log4j.Logger"%>
<%@ page buffer="200kb"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td><jsp:include page="/PensionView/PensionMenu.jsp" /></td>
	</tr>
</table>
<tr>
	<td>
		<%
			if (request.getParameter("message") != null) {
		%> <font color="red"><%=request.getParameter("message")%></font>
		<%
			}
		%>&nbsp;
	</td>
</tr>


<%
	Logger log = Logger.getLogger(request.getRequestURI());
	String contentType = request.getContentType();
	CommonUtil commonUtil = new CommonUtil();
	System.out.println("Content type is :: *****" + contentType);
	String saveFile = "", dir = "", file = "", filePath = "", slashsuffix = "";
	int start = 0, end = 0;
	String pageName = "";

	if (request.getParameter("uploadfile") != null) {
		filePath = request.getParameter("uploadfile");
		out.println("filepath is" + filePath);
		log.info("PensionFileUpload.jsp =====filepath" + filePath);
	}

	if (request.getParameter("page") != null) {
		pageName = request.getParameter("page");
		System.out.println("pageName is" + pageName);
		log.info("PensionFileUpload.jsp =====page name" + pageName);
	}

	String fileType = request.getParameter("frm_formtype");
	ResourceBundle bundle = ResourceBundle.getBundle("aims.resource.DbProperties");
	ResourceBundle appbundle = ResourceBundle.getBundle("aims.resource.ApplicationResouces");
	String appfolderPath = appbundle.getString("upload.folder.path.epf");
	slashsuffix = appbundle.getString("upload.folder.path.slashsuffix");
	String folderPath = bundle.getString("upload.folder.path");

	log.info("appfolderPath ==" + appfolderPath);
	log.info("folderPath ==" + folderPath);

	if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
		DataInputStream in = new DataInputStream(request.getInputStream());
		log.info("if block 1 ==");
		int formDataLength = request.getContentLength();
		log.info("if block 2 ==");
		byte dataBytes[] = new byte[formDataLength];
		log.info("if block 3 ==");
		int byteRead = 0;
		log.info("if block 4 ==");
		int totalBytesRead = 0;
		while (totalBytesRead < formDataLength) {
			log.info("while block 1 ==");
			byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
			//	System.out.println("byteRead"+byteRead);
			log.info("while block 2 ==");
			totalBytesRead += byteRead;
			log.info("while block 3 ==");
		}
		log.info("1 : "+dataBytes.length);
		file = new String(dataBytes);
		log.info("1");
		//start = file.indexOf("filename=\"")+10;
		start = file.indexOf("filename=\"") + 10;
		System.out.println(start);
		end = file.indexOf(".xls");
		System.out.println(end);

		if (file.indexOf(".mdb") != -1) {
			end = file.indexOf(".mdb");
			System.out.println(end);
		}

		log.info("2");
		file = new String(dataBytes);
		saveFile = file.substring(file.indexOf("filename=\"") + 10);
		
		saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
		saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));
		
		//we are added datetime as suffix in the file name

		
		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1, contentType.length());
		//out.println(boundary);
		int pos;
		pos = file.indexOf("filename=\"");
		log.info("pos1 : "+pos);
		pos = file.indexOf("\n", pos) + 1;
		log.info("pos2 : "+pos);
		pos = file.indexOf("\n", pos) + 1;
		log.info("pos3 : "+pos);
		pos = file.indexOf("\n", pos) + 1;
		log.info("pos4 : "+pos);

		int boundaryLocation = file.indexOf(boundary, pos) - 4;
		log.info("boundaryLocation : "+boundaryLocation);
		String region = "", year = "", month = "", airportcode = "";
		
		int startPos = ((file.substring(0, pos)).getBytes()).length;
		
		log.info("==start position " + startPos);
		int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
		log.info("==end position " + endPos);
		//new code Start
		RequestDispatcher rd = null;
		//new code end
		try {

			log.info("try block ");
			PensionService ps = new PensionService();
			String lengths = "", xlsSize = "", insertedSize = "", txtFileSize = "", invalidTxtSize = "";
			String[] temp;
			String userName = (String) session.getAttribute("userid");
			String ipAddress = (String) session.getAttribute("computername");

			if (request.getParameter("frm_region") != null) {
				region = request.getParameter("frm_region").toString();
			}
			if (request.getParameter("airPortCode") != null) {
				airportcode = request.getParameter("airPortCode").toString();
			}
			System.out.println("airportcode " + airportcode);
			if (request.getParameter("year") != null) {
				year = request.getParameter("year").toString();
			}
			if (request.getParameter("month") != null) {
				month = request.getParameter("month").toString();
			}

			System.out.println("saveFilePath" + folderPath);
			File saveFilePath = new File(folderPath);
			System.out.println("saveFilePath" + saveFilePath.getAbsolutePath());

			log.info("saveFilePath====" + saveFilePath);

			if (!saveFilePath.exists()) {
				File saveDir = new File(folderPath);
				if (!saveDir.exists())
					saveDir.mkdirs();

			}

			//This is used for segregated  folder struture baseed on AAI EPF forms
			if (fileType.equals("AAIEPF-2") || fileType.equals("AAIEPF-2B")) {

				log.info("if block -1 ");
				File saveFilePath1 = new File(appfolderPath);
				
				System.out.println("saveFilePath" + saveFilePath1);
				if (!saveFilePath1.exists()) {
					File saveDir = new File(appfolderPath);
					if (!saveDir.exists())
						saveDir.mkdirs();

				}
				String date = commonUtil.getDateTime("ddMMyy_hhmmss");
				saveFile = saveFile.substring(0, saveFile.indexOf(".")) + "_" + date + ".xls";
				folderPath = appfolderPath;
			} else {
				log.info("else block 11111 ");
				folderPath = commonUtil.getUploadFolderPath(fileType, year, month, appbundle);
				log.info("else block folderPath " + folderPath);
			}

			String fileName = folderPath + slashsuffix + saveFile;

			System.out.println("fileName " + fileName);

			log.info("====file Name " + fileName);
			
			FileOutputStream fileOut = new FileOutputStream(fileName);
			log.info("outputstream start 1");
			log.info("file write end position" + (endPos - startPos));
			log.info("databyyte length : " + dataBytes.length);
			
			fileOut.write(dataBytes, startPos, (boundaryLocation - startPos));
			
			log.info("outputstream start 2");
			//fileOut.flush();
			
			fileOut.close();
			log.info("outputstream start");
			String message = ps.readImportData(fileName, userName, ipAddress, region, year, month, airportcode);
			temp = lengths.split(",");
			log.info("message======" + message);
			System.out.println("message" + message);

			int importMonthwiseTRData = message.indexOf("AAIEPF-3");
			request.setAttribute("lengths", lengths);
			String txtFileName = "recordMissing.txt";
			ArrayList pensionList = new ArrayList();
			ArrayList taransactionList = new ArrayList();

			if (fileType.equals("AAIEPF-3")) {
				log.info("==========AAIEPF-3");

				pensionList = ps.getform3deviation(region, year, month, airportcode);

				if (importMonthwiseTRData == -1) {
					taransactionList = ps.getStationInOutRecords(region, year, month, airportcode);
					log.info("11");
				}
			}
			//HashMap regionHashmap = new HashMap();
			//regionHashmap = commonUtil.getRegionsForComparativeReport();
			// request.setAttribute("regionHashmap",regionHashmap);
			request.setAttribute("message", message);
			request.setAttribute("Penlist", pensionList);
			request.setAttribute("transList", taransactionList);
			String[] regionLst = null;
			String rgnName = "";
			log.info("12");
			HashMap map = new HashMap();
			if (session.getAttribute("region") != null) {
				log.info("13");
				regionLst = (String[]) session.getAttribute("region");
			}

			for (int i = 0; i < regionLst.length; i++) {
				rgnName = regionLst[i];
				if (rgnName.equals("ALL-REGIONS")
						&& session.getAttribute("usertype").toString().equals("Admin")) {
					map = new HashMap();
					log.info("14");
					map = commonUtil.getRegion();
					break;
				} else {
					log.info("15");
					map.put(new Integer(i), rgnName);
				}

			}

			log.info("16");
			Map monthMap = new LinkedHashMap();
			Iterator monthIterator = null;
			monthMap = commonUtil.getMonthsList();
			Set monthset = monthMap.entrySet();
			monthIterator = monthset.iterator();
			Map formsMap = new LinkedHashMap();
			if (pageName.equals("PensionImportNavayuga")) {
				formsMap = commonUtil.getFormsListNavayuga(session.getAttribute("userid").toString());
			} else {
				formsMap = commonUtil.getFormsList(session.getAttribute("userid").toString());
			}
			log.info("17");
			Iterator formsIterator = null;
			Set formsset = formsMap.entrySet();
			formsIterator = formsset.iterator();
			log.info("18");
			request.setAttribute("regionHashmap", map);
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("formsListMap", formsIterator);
			if (pageName.equals("PensionImportNavayuga")) {
				rd = request.getRequestDispatcher("./PensionImportDataNavayuga.jsp");

			} else {
				if (pensionList != null && pensionList.size() != 0) {
					rd = request.getRequestDispatcher("./cpfImport/PensionImportDeviation.jsp");
				} else {
					if (taransactionList != null && taransactionList.size() != 0) {
						rd = request.getRequestDispatcher("./cpfImport/PensionTransferInOutReportnavayuga.jsp");
					} else {
						rd = request.getRequestDispatcher("./PensionImportData.jsp");
					}
				}
			}
		} catch (Exception e) {
			log.info("19 Catch block ");
			log.info("=======error " + e.toString());
			System.out.println("in exception e " + e.getMessage());
			HashMap regionHashmap = new HashMap();
			Map monthMap = new LinkedHashMap();
			Map formsMap = new LinkedHashMap();
			Iterator monthIterator = null;
			Iterator formsIterator = null;
			monthMap = commonUtil.getMonthsList();
			if (pageName.equals("PensionImportNavayuga")) {
				formsMap = commonUtil.getFormsListNavayuga(session.getAttribute("userid").toString());
			} else {
				formsMap = commonUtil.getFormsList(session.getAttribute("userid").toString());
			}
			Set monthset = monthMap.entrySet();
			Set formsset = formsMap.entrySet();
			monthIterator = monthset.iterator();
			formsIterator = formsset.iterator();
			regionHashmap = commonUtil.getRegion();

			request.setAttribute("fileName", saveFile.substring(0, saveFile.indexOf("_")));
			request.setAttribute("regionHashmap", regionHashmap);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("monthIterator", monthIterator);
			request.setAttribute("formsListMap", formsIterator);
			request.setAttribute("region", region);
			request.setAttribute("airportcode", airportcode);
			request.setAttribute("year", year);
			if (pageName.equals("PensionImportNavayuga")) {
				rd = request.getRequestDispatcher("./PensionImportDataNavayuga.jsp");

			} else {
				rd = request.getRequestDispatcher("./PensionImportData.jsp");

			}
		}
		log.info("request forward ");
		rd.forward(request, response);
	}
%>
