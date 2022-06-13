<%@ page import="java.io.*"%>
<%@ page import="aims.service.PensionService"%>
<%@ page import="aims.common.CommonUtil"%>
<%@ page import="aims.bean.ReadLabels"%>
<%@ page import="java.util.ResourceBundle"%>


<%String contentType = request.getContentType();
			System.out.println("Content type is :: ====" + contentType);
			String saveFile = "", dir = "", file = "", filePath = "";
			int start = 0, end = 0;
		
			if (request.getParameter("uploadfile") != null) {
				filePath = request.getParameter("uploadfile");
				out.println("filepath is" +filePath);
			}
			ResourceBundle bundle = ResourceBundle
					.getBundle("aims.resource.DbProperties");
			String folderPath = bundle.getString("upload.folder.path");
			
			System.out.println("PensionFileUpload1 jsp : folder Path1 "+ folderPath);
			if ((contentType != null)
					&& (contentType.indexOf("multipart/form-data") >= 0)) {
				DataInputStream in = new DataInputStream(request
						.getInputStream());
				
				int formDataLength = request.getContentLength();

				byte dataBytes[] = new byte[formDataLength];
				int byteRead = 0;
				int totalBytesRead = 0;
				while (totalBytesRead < formDataLength) {
					byteRead = in.read(dataBytes, totalBytesRead,
							formDataLength);
							System.out.println("byteRead"+byteRead);
					totalBytesRead += byteRead;
				}

				file = new String(dataBytes);
				//start = file.indexOf("filename=\"")+10;
				start = file.indexOf("filename=\"")+10;
				System.out.println(start);
				end=file.indexOf(".xls");
				System.out.println(end);

				if(file.indexOf(".mdb")!=-1){
				end = file.indexOf(".mdb");
				System.out.println(end);
				}

			file = new String(dataBytes);
		saveFile =file.substring(file.indexOf("filename=\"") + 10);
		saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
		saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
		saveFile.indexOf("\""));
		System.out.println("File Name ========="+saveFile);
		int lastIndex = contentType.lastIndexOf("=");
		String boundary = contentType.substring(lastIndex + 1,
		contentType.length());
		//out.println(boundary);
		int pos;
		pos = file.indexOf("filename=\"");
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		pos = file.indexOf("\n", pos) + 1;
		
		int boundaryLocation = file.indexOf(boundary, pos) - 4;
		int startPos = ((file.substring(0, pos)).getBytes()).length;
		int endPos = ((file.substring(0,boundaryLocation)).getBytes()).length;
		try{
			System.out.println("saveFilePath"+folderPath);
			File saveFilePath = new File(folderPath);
			System.out.println("saveFilePath"+saveFilePath);
			if(!saveFilePath.exists()){
				File saveDir = new File(folderPath);
				if(!saveDir.exists())
					saveDir.mkdirs();
				
				}
		String fileName=folderPath+"\\"+saveFile;
		System.out.println("fileName "+fileName);
		FileOutputStream fileOut = new FileOutputStream(fileName);
		fileOut.write(dataBytes, startPos, (endPos - startPos));
		fileOut.flush();
		fileOut.close();
		PensionService ps = new PensionService();
		String lengths="",xlsSize="",insertedSize="",txtFileSize="",invalidTxtSize="";
		String[] temp;
		String userName=(String)session.getAttribute("userid");
		String ipAddress=(String)session.getAttribute("computername");
		ReadLabels labels=new ReadLabels();
		labels=ps.readHeaders(fileName,userName,ipAddress);
		temp = lengths.split(",");
		//xlsSize=temp[0].toString();
		//insertedSize=temp[1].toString();
		//System.out.println("xlsSize"+xlsSize+"insertedSize"+insertedSize);
		//request.setAttribute("xlsSize",xlsSize);
		//request.setAttribute("insertedSize",insertedSize);
		
		request.setAttribute("labels",labels);
		request.setAttribute("fileName",fileName);
		String txtFileName="recordMissing.txt";
		//String textFilePath=folderPath+"\\"+txtFileName;
		//lengths=ps.readImportData(fileName);
		//temp = lengths.split(",");
		//txtFileSize=temp[0].toString();
		//invalidTxtSize=temp[1].toString();
		//request.setAttribute("txtFileSize",txtFileSize);
		//request.setAttribute("invalidTxtSize",invalidTxtSize);
		RequestDispatcher rd = request.getRequestDispatcher("./PensionImportData1.jsp");
		rd.forward(request, response);
	
		}catch(Exception e){
		e.printStackTrace();
		}
					}%>
