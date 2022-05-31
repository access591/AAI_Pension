<%@ page import="java.util.*,aims.common.*"%>
<%@ page import="aims.bean.EmpMasterBean"%>
<%@ page import="aims.bean.*"%>
<%String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			String arrearInfoString1 = "";
			String regionFlag = "false";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>AAI</title>

		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<LINK rel="stylesheet" href="<%=basePath%>PensionView/css/aai.css" type="text/css">
		<SCRIPT type="text/javascript" src="./PensionView/scripts/calendar.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/CommonFunctions.js"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=basePath%>PensionView/scripts/DateTime1.js"></SCRIPT>
		<script type="text/javascript"> 
		
	
  </script>
	</head>

	<body class="BodyBackground" onload="document.forms[0].setEmpSerialNo.focus();hide();swap();retrimentDate();">

		<form method="post" action="<%=basePath%>psearch?method=personalUpdate">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>

				</tr>
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="tbborder">
							<tr>
								<td align="center" class="ScreenMasterHeading">
									Form3-2007-EmpPersonalInfo &nbsp;&nbsp;
								</td>
							</tr>
							<tr>
								<td align="center" class="label">
									<%if (request.getAttribute("ArrearInfo") != null) {
				arrearInfoString1 = (String) request.getAttribute("ArrearInfo");
				request.setAttribute("ArrearInfo", arrearInfoString1);
				System.out.println("arrearInfoString1****  "
						+ arrearInfoString1);

			}%>
									<%if (request.getAttribute("recordExist") != null) {%>
									<font color="red"><%=request.getAttribute("recordExist")%></font>
									<%}%>
								<td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td height="15%">
									<%String pensionNumber = "", editFrom = "";
			if (request.getAttribute("pensionNumber") != null) {
				pensionNumber = request.getAttribute("pensionNumber")
						.toString();
			}

			%>
									<%if (request.getAttribute("EditBean") != null) {
				EmpMasterBean bean1 = (EmpMasterBean) request
						.getAttribute("EditBean");
				String form2 = bean1.getForm2Nomination().trim();
				if (request.getAttribute("AirportList") != null) {
					ArrayList AirportList = (ArrayList) request
							.getAttribute("AirportList");
				}
				if (request.getAttribute("editFrom") != null) {
					editFrom = (String) request.getAttribute("editFrom");
				}

				%>

									<table align="center" border="0">
										<!--<%=bean1.getFamilyRow()%>
					        <%=bean1.getNomineeRow()%>
				
							-->
										<tr>
											<td class="label">
												<b>PF ID: <b>
											</td>
											<td>
												<input type="text" readonly="true" name="pfid" readonly="true" maxlength="35" tabindex="1" readonly="true" value='<%=bean1.getPfid()%>' />
												<input type="hidden" name="setEmpSerialNo" readonly="true" maxlength="35" tabindex="1" value='<%=bean1.getEmpSerialNo()%>' />
											</td>
											<td class="label">
												Employee Name:
											</td>

											<td>
												<input type="text" readonly="true" name="empName" maxlength="50" tabindex="5" onblur="getPensionNumber();" value='<%=bean1.getEmpName()%>' onkeyup="return limitlength(this,50)">
											</td>
										</tr>
										<tr>
											<td class="label">
												Old CPFACC.NO:
											</td>
											<td>
												<input type="text" readonly="true" name="cpfacnoNew" maxlength="25" tabindex="2" onBlur="charsCapsNumOnly();getPensionNumber();" value='<%=bean1.getCpfAcNo()%>' />
											</td>
											<!-- 	<input type="text" name="cpfacnoNew" maxlength="25" tabindex="1" onBlur="charsCapsNumOnly();getPensionNumber();" value='<%=bean1.getCpfAcNo()%>'>-->

											<td class="label">
												Old / New Airport Code:
											</td>
											<td>
												<input type="text" readonly="true" name="oldAirportCode" size="5" readonly="true" value='<%= bean1.getStation()%>'%>
												<select name="airPortCode" disabled="disabled" tabindex="3" style="width: 80px">
													<option value="">
														[Select One]
													</option>
													<%if (request.getAttribute("AirportList") != null) {
					ArrayList airpors = (ArrayList) request
							.getAttribute("AirportList");
					for (int i = 0; i < airpors.size(); i++) {
						boolean exist = false;
						EmpMasterBean airportBean = (EmpMasterBean) airpors
								.get(i);
						if (airportBean.getStation().equalsIgnoreCase(
								bean1.getStation()))
							exist = true;

						%>

													<%if (exist) {%>

													<option value="<%= bean1.getStation()%>" selected>
														<%=airportBean.getStationWithRegion()%>
													</option>

													<%} else {

							%>

													<option value="<%= airportBean.getStation()%>">
														<%=airportBean.getStationWithRegion()%>
													</option>

													<%}
					}
				}%>


												</select>
											</td>


											<input type="hidden" name="airportSerialNumber" value='<%=bean1.getAirportSerialNumber()%>' onkeyup="return limitlength(this, s0)">
											<input type="hidden" name="region" value='<%=bean1.getRegion()%>' onkeyup="return limitlength(this, s0)">
											<input type="hidden" name="editFrom" value='<%=editFrom%>' />
										</tr>

										<tr>
											<td class="label">
												Employee Code:
											</td>
											<td>
												<input type="text" readonly="true" name="employeeCode" maxlength="20" tabindex="4" value='<%=bean1.getEmpNumber()%>' onkeyup="return limitlength(this, 20)">
											</td>

											<td class="label">
												New Employee Code:
											</td>

											<td>
												<input type="text" readonly="true" name="employeeCode" maxlength="50" tabindex="5" onblur="getNewEmployeeNumber();" value='<%=bean1.getNewEmployeeNumber()%>' onkeyup="return limitlength(this,50)">
											</td>
										</tr>


										<tr>
											<td class="label">
												Father's / Husband's Name:
											</td>
											<td>
												<select name="select_fhname" disabled="disabled" tabindex="6" style="width: 55px">
													<option value='F' <%if(bean1.getFhFlag().trim().equals("F")){ out.println("selected");}%>>
														Father
													</option>
													<option value='H' <%if(bean1.getFhFlag().trim().equals("H")){ out.println("selected");}%>>
														Husband
													</option>
												</select>
												<input type="text" readonly="true" name="fhname" readonly="true" maxlength="50" tabindex="7" style="width: 95px" value='<%=bean1.getFhName()%>' onkeyup="return limitlength(this, 50)">
											</td>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

											<td class="label">
												Sex:
											</td>

											<td>
												<select name="sex" disabled="disabled" readonly="true" tabindex="8" style="width: 130px">
													<%String sex = bean1.getSex().trim();%>
													<option value="">
														[Select One]
													</option>
													<option value="M" <%if(sex.equals("M")){ out.println("selected");}%>>
														Male
													</option>
													<option value="F" <%if(sex.equals("F")){ out.println("selected");}%>>
														Female
													</option>

												</select>
											</td>
										</tr>
										<tr>
											<td class="label">
												Date of Birth:
											</td>
											<td>
												<input type="text" readonly="true" name="dateofbirth" tabindex="9" onblur="retrimentDate();getPensionNumber();" value='<%=bean1.getDateofBirth()%>' onkeyup="return limitlength(this, 20)">
											</td>
											<td class="label">
												Date of Joining:
											</td>
											<td>
												<input type="text" readonly="true" name="dateofjoining" tabindex="10" value='<%=bean1.getDateofJoining()%>' onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<tr>
											<td class="label">
												Employee Level:
											</td>
											<td>
												<%String emplevel = bean1.getEmpLevel().trim();%>
												<select name="emplevel" disabled="disabled" tabindex="11" style="width: 130px" onchange="getDesegnation(document.forms[0].emplevel.value)">
													<option value="">
														[Select One]
													</option>
													<option value='E-9' <%if(emplevel.equals("E-9")){ out.println("selected");}%>>
														E-9,Executive Directior
													</option>
													<option value='E-8' <%if(emplevel.equals("E-8")){ out.println("selected");}%>>
														E-8,General Manager
													</option>
													<option value='E-7' <%if(emplevel.equals("E-7")){ out.println("selected");}%>>
														E-7,Jt. General Manager
													</option>
													<option value='E-6' <%if(emplevel.equals("E-6")){ out.println("selected");}%>>
														E-6,Deputy General Manager
													</option>
													<option value='E-5' <%if(emplevel.equals("E-5")){ out.println("selected");}%>>
														E-5,Asst. General Manager
													</option>
													<option value='E-4' <%if(emplevel.equals("E-4")){ out.println("selected");}%>>
														E-4,Senior Manager
													</option>
													<option value='E-3' <%if(emplevel.equals("E-3")){ out.println("selected");}%>>
														E-3,Manager
													</option>
													<option value='E-2' <%if(emplevel.equals("E-2")){ out.println("selected");}%>>
														E-2,Assistant Manager
													</option>
													<option value='E-1' <%if(emplevel.equals("E-1")){ out.println("selected");}%>>
														E-1,Junior Executive
													</option>
													<option value='NE-1' <%if(emplevel.equals("NE-1")){ out.println("selected");}%>>
														NE-1,Jr. Attendant
													</option>
													<option value='NE-2' <%if(emplevel.equals("NE-2")){ out.println("selected");}%>>
														NE-2,Attendant
													</option>
													<option value='NE-3' <%if(emplevel.equals("NE-3")){ out.println("selected");}%>>
														NE-3,Senior Attendant
													</option>
													<option value='NE-4' <%if(emplevel.equals("NE-4")){ out.println("selected");}%>>
														NE-4,Jr. Assistant
													</option>
													<option value='NE-5' <%if(emplevel.equals("NE-5")){ out.println("selected");}%>>
														NE-5,Assistant
													</option>
													<option value='NE-6' <%if(emplevel.equals("NE-6")){ out.println("selected");}%>>
														NE-6,Senior Assistant
													</option>
													<option value='NE-7' <%if(emplevel.equals("NE-7")){ out.println("selected");}%>>
														NE-7,Supervisor
													</option>
													<option value='NE-8' <%if(emplevel.equals("NE-8")){ out.println("selected");}%>>
														NE-8,Superintendent
													</option>
													<option value='NE-9' <%if(emplevel.equals("NE-9")){ out.println("selected");}%>>
														NE-9,Sr. Superintendent
													</option>
													<option value='NE-10' <%if(emplevel.equals("NE-10")){ out.println("selected");}%>>
														NE-10,Sr. Superintendent(SG)
													</option>
													<option value='B1' <%if(emplevel.equals("B1")){ out.println("selected");}%>>
														B1,Chairman
													</option>
													<option value='B2' <%if(emplevel.equals("B2")){ out.println("selected");}%>>
														B2,Member
													</option>
												</select>
											</td>

											<td class="label">
												Designation:
											</td>
											<td>
												<input type="text" name="desegnation" readonly="true" readonly="true" tabindex="12" value='<%=bean1.getDesegnation()%>' onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<tr>
											<td class="label">
												Discipline:
											</td>
											<td>
												<%String department = bean1.getDepartment().trim();

				%>
												<select name="department" disabled="disabled" tabindex="13" style="width: 130px" style="display: inline;">
													<option value="">
														[Select One]
													</option>
													<%if (request.getAttribute("DepartmentList") != null) {
					ArrayList deptList = (ArrayList) request
							.getAttribute("DepartmentList");
					for (int i = 0; i < deptList.size(); i++) {
						boolean exist = false;
						EmpMasterBean deptBean = (EmpMasterBean) deptList
								.get(i);
						if (deptBean.getDepartment().equalsIgnoreCase(
								bean1.getDepartment().trim()))
							exist = true;
						if (exist) {

							%>
													<option value="<%=bean1.getDepartment().trim()%>" <% out.println("selected");%>>
														<%=bean1.getDepartment().trim()%>
													</option>
													<%} else {%>
													<option value="<%= deptBean.getDepartment()%>">
														<%=deptBean.getDepartment()%>
													</option>

													<%}
					}
				}%>

												</select>
											</td>
											<td class="label">
												Division:
											</td>
											<td>
												<%String division = bean1.getDivision().trim();

				%>
												<select name="division" disabled="disabled" tabindex="14" style="width: 130px">
													<option value="">
														[Select One]
													</option>
													<option value='NAD' <%if(division.equals("NAD")){ out.println("selected");}%>>
														NAD
													</option>
													<option value='IAD' <%if(division.equals("IAD")){ out.println("selected");}%>>
														IAD
													</option>
												</select>
											</td>
										</tr>
										<%--<tr>
											<td class="label">
												Transfer Status:
											</td>
											<td>
												<input type="checkbox" name="transferStatus" tabindex="30" onclick="showRegion();">
											</td>
										</tr>--%>
										<%ArrayList regionList = new ArrayList();
				String rgnName = "", region = "";
				HashMap map = new HashMap();
				CommonUtil commonUtil = new CommonUtil();
				String[] regionLst = null;
				Iterator regionIterator1 = null;
				// ArrayList airportList1=null;
				if (session.getAttribute("region") != null) {
					regionLst = (String[]) session.getAttribute("region");
				}

				for (int i = 0; i < regionLst.length; i++) {
					rgnName = regionLst[i];

					if (rgnName.equals("ALL-REGIONS")
							&& session.getAttribute("usertype").toString()
									.equals("Admin")) {
						map = new HashMap();
						map = commonUtil.getRegion();
						break;
					} else {
						map.put(new Integer(i), rgnName);

					}

				}
				Set keys = map.keySet();
				regionIterator1 = keys.iterator();

				%>
										<!--						<tr>
											<td colspan="6">
												<div id="divRegion">
													<table width="100%">
														<tr>
															<td colspan="2" class="label">
																&nbsp;&nbsp;&nbsp;Region:<font color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															</td>
															<td class="label">
																<SELECT NAME="select_region" onChange="javascript:getAirports()" style="width:130px">
																	<option value="">
																		[Select One]
																	</option>
																	<%while (regionIterator1.hasNext()) {
                    region = map.get(regionIterator1.next()).toString();

                    %>
																	<option value="<%=region%>">
																		<%=region%>
																	</option>
																	<%}%>
																</SELECT>
															</td>
															<td class="label">
																&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Airport Code:<font color="red">&nbsp;*</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															</td>
															<td>
																<select name="select_airport">
																	<option value="">
																		[Select One]
																	</option>
																</select>
															</td>
														</tr>
													</table>
												</div>
											</td>
										</tr>

										-->
										<tr>
											<td class="label">
												Separation Reason:
											</td>
											<td>
												<select name="reason" disabled="disabled" tabindex="15" id="mySelect" onChange="swap();" style="width: 130px" style="display: inline;">
													<%String reason = bean1.getSeperationReason().trim();

				%>
													<option value="">
														[Select One]
													</option>
													<option value='Retirement' <%if(reason.equals("Retirement")){ out.println("selected");}%>>
														Retirement
													</option>
													<option value='Death' <%if(reason.equals("Death")){ out.println("selected");}%>>
														Death
													</option>
													<option value='Resignation' <%if(reason.equals("Resignation")){ out.println("selected");}%>>
														Resignation
													</option>
													<option value='Termination' <%if(reason.equals("Termination")){ out.println("selected");}%>>
														Termination
													</option>
													<option value='Option for Early Pension' <%if(reason.equals("Option for Early Pension")){ out.println("selected");}%>>
														Option for Early Pension
													</option>
													<option value='VRS' <%if(reason.equals("VRS")){ out.println("selected");}%>>
														VRS
													</option>
													<option value='REMOVED' <%if(reason.equals("REMOVED")){ out.println("selected");}%>>
														REMOVED
													</option>
													<option value='Other' <%if(reason.equals("Other")){ out.println("selected");}%>>
														Other
													</option>
													<option value='CRS' <%if(reason.equals("CRS")){ out.println("selected");}%>>
														CRS
													</option>
												</select>
											</td>
											<td class="label">
												Date of Separation:
											</td>
											<td>
												<input type="text" name="seperationDate" readonly="true" tabindex="16" value='<%=bean1.getDateofSeperationDate()%>' onkeyup="return limitlength(this, 20)">
											</td>
										</tr>
										<!--<tr>
						<td>&nbsp;</td>
						<td><input type="text" name="Other" readyonly="true"
							value='<%=bean1.getOtherReason()%>' id="myText"
							style="display: none;" tabindex="17" onclick="swapback()"></td>
					</tr>-->
										<tr>

											<td class="label">
												Permanent Address:
											</td>

											<td>
												<TEXTAREA name="paddress" tabindex="18" readonly="true" style="width: 125px" <%=bean1.getPermanentAddress()%>>
							</TEXTAREA>
											</td>
											<td class="label">
												Temporary Address:
											</td>
											<td>
												<TEXTAREA name="taddress" readonly="true" size="150" maxlength="150" style="width: 125px" tabindex="19">	<%=bean1.getTemporatyAddress()%>
												</TEXTAREA>
											</td>
										</tr>
										<tr>
											<td class="label">
												Pension Option Received:
											</td>

											<td>
												<input type="text" name="wetherOption" readonly="true" value='<%=bean1.getWetherOption().trim()%>' tabindex="20" readonly="true">
												<!-- 	<select name='wetherOption' tabindex="20"  >
													<%String wetherOption = bean1.getWetherOption().trim();
                                                   %>
													<option value="">
														[Select One]
													</option>
													<option value="A" <%if(wetherOption.equals("A")){ out.println("selected");}%>>
														A
													</option>
													<option value="B" <%if(wetherOption.equals("B")) out.println("selected");%>>
														B
													</option>
													<option value="No" <%if(wetherOption.equals("No")){ out.println("selected");}%>>
														No
													</option> 
												</select> -->
											</td>
											<td class="label">
												Whether Form2 Nomination Received :
											</td>
											<td>
												<select name="form1" disabled="disabled" tabindex="21" style="width: 130px" onchange="show()">

													<%String form1 = bean1.getForm2Nomination().trim();

				%>
													<option value="">
														[Select One]
													</option>
													<option value="Yes" <%if(form1.equals("Yes")){ out.println("selected");}%>>
														Yes
													</option>
													<option value="No" <%if(form1.equals("No")){ out.println("selected");}%>>
														No
													</option>

												</select>
											</td>

										</tr>
										<tr>
											<input type="hidden" name="cpfacno" value='<%=bean1.getCpfAcNo()%>'>
											<input type="hidden" name="empOldName" value='<%=bean1.getEmpName()%>'>
											<input type="hidden" name="empOldNumber" value='<%=bean1.getEmpNumber()%>'>
											<td class="label">
												
												Date of Retirement:
											</td>
											<td>
												<input type="text" name="dateOfAnnuation" readonly="true" tabindex="23" value='<%=bean1.getDateOfAnnuation()%>'>
											</td>
											<td class="label">
												Email Id:
											</td>
											<td>
												<input type="text" name="emailId" readonly="true" maxlength="50" onkeyup="return limitlength(this,50)" value='<%=bean1.getEmailId()%>' tabindex="24">
											</td>
										</tr>

										<tr>
											<td class="label">
												Date of Super Annuation:
											</td>
											<td>
												<input type="text" name="dateOfRetirement" readonly="true" tabindex="27" value='<%=bean1.getDateOfRetirement()%>'>
											</td>

											<td class="label">
												Other Reason:
											</td>
											<td>
												<input type="text" readonly="true" name="otherrearson" maxlength="20" tabindex="4" value='<%=bean1.getOtherReason()%>' onkeyup="return limitlength(this, 20)">
											</td>
										</tr>

										<tr>
											<td class="label">
												Remarks:
											</td>

											<td>
												<TEXTAREA NAME="remarks" readonly="true" tabindex="25" style="width: 125px">
													<%=bean1.getRemarks()%>
												</TEXTAREA>
												<input type="hidden" name="ArrearInfo" value="<%=arrearInfoString1%>">
											</td>

											<td class="label">
												Marital Status:
											</td>
											<td>
												<%String maritalStatus = bean1.getMaritalStatus().trim();

				%>
												<select name="maritalStatus" disabled="disabled" tabindex="10" style="width: 130px">
													<option value="">
														[Select One]
													</option>
													<option value='Yes' <%if(maritalStatus.equals("Yes")){ out.println("selected");}%>>
														Yes
													</option>
													<option value='No' <%if(maritalStatus.equals("No")){ out.println("selected");}%>>
														No
													</option>
												</select>
											</td>

										</tr>
										<tr>
											<td class="label">
												Fresh Option
											</td>

											<td>
												<input type="text" name="wetherOption" readonly="true" value='<%=bean1.getFreshPenOption().trim()%>' tabindex="20" readonly="true">
											</td>
                                            <td class="label">
												Deferement
											</td>
									
									
											<td>
												<input type="text" name="deferement" readonly="true" value='<%=bean1.getDeferement().trim()%>' tabindex="20" readonly="true">
											</td>
									</tr>
									<% if(bean1.getDeferement().equals("Y")){ %>
									<tr>
											<td class="label">
												Deferement Pension
											</td>


											<td>
											<select name="deferementpension" disabled="disabled" tabindex="10" style="width: 130px">
													<option value='WithContribution' <%if(bean1.getDeferementpension().equals("Y")){ out.println("selected");}%>>
														With Contribution
													</option>
													<option value='withOutContribution ' <%if(bean1.getDeferementpension().equals("N")){ out.println("selected");}%>>
														With Out Contribution
													</option>
												</select>
											</td>
											<td class="label">
												Deferement Age
											</td>

											<td>
												<input type="text" name="deferementage" readonly="true" value='<%=bean1.getDeferementage().trim()%>' tabindex="20" readonly="true">
											</td>
											


											<td class="label">
											
											</td>
											<td>
												
											</td>

										</tr>
										<% }%>
										<br>
										<tr>
											<td class="ScreenSubHeading">
												Family Details
											</td>
										</tr>
										<tr>

											<td class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Name
											</td>
											<td class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Date of Birth
											</td>
											<td class="label">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Relation with member
											</td>
										</tr>

										<%int count = 0;
				String familyrows = bean1.getFamilyRow();

				ArrayList familyList = commonUtil.getTheList(familyrows, "***");

				String tempInfo[] = null;
				String tempData = "";
				String fMemberName = "", fDateofBirth = "", frelation = "", rowid = "";
				for (int i = 0; i < familyList.size(); i++) {

					tempData = familyList.get(i).toString();
					tempInfo = tempData.split("@");

					fMemberName = tempInfo[0];
					fDateofBirth = tempInfo[1];
					if (fDateofBirth.equals("xxx")) {
						fDateofBirth = "";
					}
					frelation = tempInfo[2];
					if (frelation.equals("xxx")) {
						frelation = "";
					}
					rowid = tempInfo[3];
					if (rowid.equals("xxx")) {
						rowid = "";
					}

					%>
										<tr id="name1<%=i%>">
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

												<input type="text" readonly="readonly" name="FName" tabindex="24" maxlength="50" value='<%=fMemberName%>'>
												<input type="hidden" readonly="readonly" name="empOldFRName" value='<%=fMemberName%>'>
											</td>


											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="text" readonly="readonly" name="Fdob" value='<%=fDateofBirth%>' tabindex="25">
											</td>
											<td>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<select name="Frelation" disabled="disabled" tabindex="26">

													<option value="">
														[Select One]
													</option>
													<option value='SPOUSE' <%if(frelation.equals("SPOUSE")){ out.println("selected");}%>>
														SPOUSE
													</option>
													<option value='SON' <%if(frelation.equals("SON")){ out.println("selected");}%>>
														SON
													</option>
													<option value='DAUGHTER' <%if(frelation.equals("DAUGHTER")){ out.println("selected");}%>>
														DAUGHTER
													</option>
													<option value='MOTHER' <%if(frelation.equals("MOTHER")){ out.println("selected");}%>>
														MOTHER
													</option>
													<option value='FATHER' <%if(frelation.equals("FATHER")){ out.println("selected");}%>>
														FATHER
													</option>
													<option value='SONS WIDOW' <%if(frelation.equals("SONS WIDOW")){ out.println("selected");}%>>
														SON'S WIDOW
													</option>
													<option value='WIDOWS DAUGHTER' <%if(frelation.equals("WIDOWS DAUGHTER")){ out.println("selected");}%>>
														WIDOW'S DAUGHTER
													</option>

													<option value='MOTHER-IN-LOW' <%if(frelation.equals("MOTHER-IN-LOW")){ out.println("selected");}%>>
														MOTHER-IN-LOW
													</option>
													<option value='FATHER-IN-LOW' <%if(frelation.equals("FATHER-IN-LOW")){ out.println("selected");}%>>
														FATHER-IN-LOW
													</option>

												</select>
											</td>
											<td>
												&nbsp;<b><img alt="Delete" src="<%=basePath%>PensionView/images/cancelIcon.gif" tabindex="27"></b>
											</td>
											<!-- 		<td>
												&nbsp;<b><img alt="" src="<%=basePath%>PensionView/images/cancelIcon.gif" onclick="deleteFamilyDetails('<%=fMemberName%>','<%=i%>')"> </b>
											</td> -->
										</tr>


										<%}

				%>
									</table>
									<div id="divFamily1">
										<table>
											<tr>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="text" readonly="readonly" name="FName" maxlength="50" tabindex="27">
												</td>
												<input type="hidden" readonly="readonly" name="empOldFRName" value="">
												<td>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="text" name="Fdob" id="Fdob0" readonly="readonly" tabindex="28">
												</td>
												<td>
													&nbsp;
													<select name="Frelation" disabled="disabled" tabindex="29">
														<option value="">
															[Select One]
														</option>
														<option value='SPOUSE'>
															SPOUSE
														</option>
														<option value='SON'>
															SON
														</option>
														<option value='DAUGHTER'>
															DAUGHTER
														</option>
														<option value='MOTHER'>
															MOTHER
														</option>
														<option value='FATHER'>
															FATHER
														</option>
														<option value='SONS WIDOW'>
															SON'S WIDOW
														</option>
														<option value='WIDOWS DAUGHTER'>
															WIDOW'S DAUGHTER
														</option>
														<option value='MOTHER-IN-LOW'>
															MOTHER-IN-LOW
														</option>
														<option value='FATHER-IN-LOW'>
															FATHER-IN-LOW
														</option>

													</select>
												</td>

											</tr>
										</table>
									</div>

									<div id="divFamily2"></div>

									<div id="divNomineeHead">
										<table align="center" width="95%">
											<tr>
												<td class="ScreenSubHeading">
													Nomination for PF
												</td>

											</tr>
											<tr>
												<td class="label">
													Name&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td class="label">
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Address&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td class="label">
													Dateof Birth&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												</td>
												<td class="label">
													Relation with Member&nbsp;&nbsp;
												</td>
												<td class="label">
													Name of Guardian&nbsp;&nbsp;&nbsp;
												</td>
												<td class="label">
													Address of Guardian&nbsp;&nbsp;&nbsp;
												</td>
												<td class="label">
													Total Share
													<BR>
													payable in %
												</td>
											</tr>

											<%int count1 = 0;
				String nomineeRows = bean1.getNomineeRow();

				CommonUtil commonUtil1 = new CommonUtil();
				ArrayList nomineeList = commonUtil.getTheList(nomineeRows,
						"***");

				String tempInfo1[] = null;
				String tempData1 = "";
				String nomineeAddress = "", nomineeDob = "", nomineeRelation = "", nameofGuardian = "", totalShare = "", addressofGuardian = "";
				for (int i = 0; i < nomineeList.size(); i++) {
					count1++;
					tempData = nomineeList.get(i).toString();
					tempInfo = tempData.split("@");
					String nomineeName = tempInfo[0];
					nomineeAddress = tempInfo[1];
					nomineeDob = tempInfo[2];
					nomineeRelation = tempInfo[3];
					nameofGuardian = tempInfo[4];
					addressofGuardian = tempInfo[5];
					totalShare = tempInfo[6];
					if (nomineeAddress.equals("xxx")) {
						nomineeAddress = "";
					}

					if (nomineeDob.equals("xxx")) {
						nomineeDob = "";
					}
					if (nomineeRelation.equals("xxx")) {
						nomineeRelation = "";
					}

					if (nameofGuardian.equals("xxx")) {
						nameofGuardian = "";
					}
					if (addressofGuardian.equals("xxx")) {
						addressofGuardian = "";
					}
					if (totalShare.equals("xxx")) {
						totalShare = "";
					}
					rowid = tempInfo[7];
					if (rowid.equals("xxx")) {
						rowid = "";
					}

					%>
											<tr id="name<%=i%>">
												<td>
													<input type="text" size="18" readonly="readonly" name="Nname" maxlength="50" value='<%=nomineeName%>' tabindex="31">
													<input type="hidden" readonly="readonly" name="empOldNname" value='<%=nomineeName%>'>
												</td>
												<td>
													<input type="text" size="16" readonly="readonly" name="Naddress" maxlength="150" value='<%=nomineeAddress%>' tabindex="32">
												</td>
												<td>
													<input type="text" readonly="readonly" size="16" name="Ndob" value='<%=nomineeDob%>' tabindex="33">
												</td>
												<td>
													<select name="Nrelation" tabindex="34" disabled="disabled">
														<option value="">
															[Select One]
														</option>
														<option value='SPOUSE' <%if(nomineeRelation.equals("SPOUSE")){ out.println("selected");}%>>
															SPOUSE
														</option>
														<option value='SON' <%if(nomineeRelation.equals("SON")){ out.println("selected");}%>>
															SON
														</option>
														<option value='DAUGHTER' <%if(nomineeRelation.equals("DAUGHTER")){ out.println("selected");}%>>
															DAUGHTER
														</option>
														<option value='MOTHER' <%if(nomineeRelation.equals("MOTHER")){ out.println("selected");}%>>
															MOTHER
														</option>
														<option value='FATHER' <%if(nomineeRelation.equals("FATHER")){ out.println("selected");}%>>
															FATHER
														</option>
														<option value='SONS WIDOW' <%if(nomineeRelation.equals("SONS WIDOW")){ out.println("selected");}%>>
															SON'S WIDOW
														</option>
														<option value='WIDOWS DAUGHTER' <%if(nomineeRelation.equals("WIDOWS DAUGHTER")){ out.println("selected");}%>>
															WIDOW'S DAUGHTER
														</option>

														<option value='MOTHER-IN-LOW' <%if(nomineeRelation.equals("MOTHER-IN-LOW")){ out.println("selected");}%>>
															MOTHER-IN-LOW
														</option>
														<option value='FATHER-IN-LOW' <%if(nomineeRelation.equals("FATHER-IN-LOW")){ out.println("selected");}%>>
															FATHER-IN-LOW
														</option>
													</select>
												</td>
												<td>
													<input type="text" size="16" readonly="true" name="guardianname" maxlength="50" value='<%=nameofGuardian%>' tabindex="35">
												</td>
												<td>
													<input type="text" size="16" readonly="true" name="gaddress" maxlength="150" value='<%=addressofGuardian%>' tabindex="36">
												</td>
												<td>
													<input type="text" size="5" readonly="true" name="totalshare" value='<%=totalShare%>' onkeypress="numsDotOnly()" tabindex="37">
												</td>
												<td>
													&nbsp;<b><img alt="Delete" src="<%=basePath%>PensionView/images/cancelIcon.gif" tabindex="27"></b>
												</td>


											</tr>
											<%}

			%>
											<%}

			%>

										</table>
										<div>
											<div id="divNominee1">
												<table>
													<tr>
														<td>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="text" size="18" name="Nname" readonly="readonly" maxlength="50" tabindex="39" onblur="dispOff()">
															&nbsp;
															
															<input type="hidden" name="empOldNname" value="">
														</td>

														<td>
															&nbsp;
															<input type="text" size="16" readonly="readonly" name="Naddress" maxlength="150" tabindex="40">
														</td>
														<td>
															&nbsp;&nbsp;
															<input type="text" readonly="readonly" size="16" name="Ndob" tabindex="41">
															&nbsp;

														</td>
														<td>
															&nbsp;
															<select name="Nrelation" tabindex="40" disabled="disabled">
																<option value="">
																	[Select One]
																</option>
																<option value='SPOUSE'>
																	SPOUSE
																</option>
																<option value='SON'>
																	SON
																</option>
																<option value='DAUGHTER'>
																	DAUGHTER
																</option>
																<option value='MOTHER'>
																	MOTHER
																</option>
																<option value='FATHER'>
																	FATHER
																</option>
																<option value='SONS WIDOW'>
																	SON'S WIDOW
																</option>
																<option value='WIDOWS DAUGHTER'>
																	WIDOW'S DAUGHTER
																</option>
																<option value='MOTHER-IN-LOW'>
																	MOTHER-IN-LOW
																</option>
																<option value='FATHER-IN-LOW'>
																	FATHER-IN-LOW
																</option>

															</select>
														</td>
														<td>
															&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="text" size="16" name="guardianname" readonly="true" maxlength="50" tabindex="42">
														</td>
														<td>
															&nbsp;&nbsp;
															<input type="text" readonly="true" size="16" name="gaddress" maxlength="150" tabindex="43">
															&nbsp;
														</td>
														<td>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<input type="text" readonly="true" size="5" name="totalshare" onkeypress="numsDotOnly()" tabindex="44">
														</td>
														<td>
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b></b>
														</td>

													</tr>
												</table>
											</div>

											<div id="divNominee2"></div>

											<input type="hidden" name="flagData" value="<%=request.getAttribute("flag")%>">
							<tr>
								<td>
							<tr>
								<td>
									<table align="center">
										<tr>
											<td align="center"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
			</table>

		</form>
	</body>
</html>



