package aims.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import aims.bean.StationWiseRemittancebean;
import aims.common.CommonUtil;
import aims.common.DBUtils;
import aims.common.Log;

public class RemittanceDAO {
	Log log = new Log(RemittanceDAO.class);
	DBUtils commonDB = new DBUtils();

	CommonUtil commonUtil = new CommonUtil();

	CommonDAO commonDAO = new CommonDAO();

	public ArrayList getStationList(String region, String monthyear,
			String aiportcode) {

		log.info("monthyear====" + monthyear + "aiportcode" + aiportcode);
		ArrayList stationsList = null;
		StationWiseRemittancebean stationWiseRemittanceBean = null;
		String sqlQry = "", station = "", regionRegion = "", condition = "";

		double totalPC = 0.0, totalPF = 0.0, totalInspCharges = 0.0, totalAddContri = 0.0;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
	

		try {
			con = commonDB.getConnection();
			st = con.createStatement();
			if (!region.equals("All")) {
				if (region.equals("CHQIAD") && !aiportcode.equals("")) {
					condition = " and REGION='" + region
							+ "' and airportcode='" + aiportcode + "'";
				} else {
					condition = " and REGION='" + region + "'";
				}
			}

			sqlQry = "select d.region,(select distinct pccode from employee_unit_master m where m.unitname=d.airportcode  and m.region=d.region) as PCCODE, d.airportcode,       d.monthyear,nvl(d.pc, 0) as cpfpc,       nvl(d.apc, 0) as arrearpc,       nvl(d.spc, 0) as supplipc,       nvl(d.pf, 0) as cpfpf,       nvl(d.apf, 0) as arrearpf,       nvl(d.spf, 0) as supplipf,       nvl(d.cpfinspcharges, 0) as cpfinspcharges,       nvl(d.ainspcharges, 0) as ainspcharges,       nvl(d.sinspcharges, 0) as sinspcharges,       nvl(d.emppfstatuary, 0) as emppfstatuary,"
					+ "       nvl(d.aemppfstatuary, 0) as aemppfstatuary,       nvl(d.semppfstatuary, 0) as semppfstatuary,       nvl(d.cpfpf, 0) as ccpf,       nvl(d.aapf, 0) as aapf,       nvl(d.sspf, 0) as sspf,       nvl(d.cpfvpf, 0) as cpfvpf,       nvl(d.avpf, 0) as avpf,       nvl(d.svpf, 0) as svpf,       nvl(d.cpfadvprincipal, 0) as cpfadvprincipal,       nvl(d.aadvprincipal, 0) as aadvprincipal,       nvl(d.sadvprincipal, 0) as sadvprincipal, "
					+ "      nvl(d.cpfadvrecint, 0) as cpfadvrecint,       nvl(d.aadvrecint, 0) as aadvrecint,       nvl(d.sadvrecint, 0) as sadvrecint,       nvl(d.cpfaddcontri, 0) as cpfaddcontri,       nvl(d.aaddcontri, 0) as aaddcontri,       nvl(d.saddcontri, 0) as saddcontri,       nvl(d.emppfstatuary, 0) + nvl(d.cpfpf, 0) + nvl(d.cpfvpf, 0) +       nvl(d.cpfadvprincipal, 0) + nvl(d.cpfadvrecint, 0) as cpfacc,       nvl(d.aemppfstatuary, 0) + nvl(d.aapf, 0) + nvl(d.avpf, 0) + "
					+ "      nvl(d.aadvprincipal, 0) + nvl(d.aadvrecint, 0) as ArrearAcc,       nvl(d.semppfstatuary, 0) + nvl(d.sspf, 0) + nvl(d.svpf, 0) +       nvl(d.sadvprincipal, 0) + nvl(d.sadvrecint, 0) as suppliacc  from       MV_EMP_CPF_APWISE_REM d where  d.monthyear ='"
					+ monthyear
					+ "' "
					+ condition
					+ " order by region,airportcode";

			log.info("sqlQry=====" + sqlQry);
			rs = st.executeQuery(sqlQry);
			stationsList = new ArrayList();
			while (rs.next()) {
				stationWiseRemittanceBean = new StationWiseRemittancebean();
				if (rs.getString("region") != null) {
					regionRegion = rs.getString("region");
					stationWiseRemittanceBean.setRegion(regionRegion);
				}
				if (rs.getString("pccode") != null) {
					
					stationWiseRemittanceBean.setPccode(rs.getString("pccode"));
				}
				if (rs.getString("AIRPORTCODE") != null) {
					station = rs.getString("AIRPORTCODE");
					stationWiseRemittanceBean.setStation(station);
				}

				if (rs.getString("cpfpc") != null) {
					stationWiseRemittanceBean.setCpfPc(rs.getString("cpfpc"));
				} else {
					stationWiseRemittanceBean.setCpfPc("0");
				}
				if (rs.getString("arrearpc") != null) {
					stationWiseRemittanceBean.setArrearPc(rs
							.getString("arrearpc"));
				} else {
					stationWiseRemittanceBean.setArrearPc("0");
				}
				if (rs.getString("supplipc") != null) {
					stationWiseRemittanceBean.setSuppliPc(rs
							.getString("supplipc"));
				} else {
					stationWiseRemittanceBean.setSuppliPc("0");
				}
				if (rs.getString("cpfpf") != null) {
					stationWiseRemittanceBean.setCpfPf(rs.getString("cpfpf"));
				} else {
					stationWiseRemittanceBean.setCpfPf("0");
				}
				if (rs.getString("arrearpf") != null) {
					stationWiseRemittanceBean.setArrearPf(rs
							.getString("arrearpf"));
				} else {
					stationWiseRemittanceBean.setArrearPf("0");
				}
				if (rs.getString("supplipf") != null) {
					stationWiseRemittanceBean.setSuppliPf(rs
							.getString("supplipf"));
				} else {
					stationWiseRemittanceBean.setSuppliPf("0");
				}
				if (rs.getString("cpfinspcharges") != null) {
					stationWiseRemittanceBean.setCpfInspCharges(rs
							.getString("cpfinspcharges"));
				} else {
					stationWiseRemittanceBean.setCpfInspCharges("0");
				}
				if (rs.getString("ainspcharges") != null) {
					stationWiseRemittanceBean.setArrearInspCharges(rs
							.getString("ainspcharges"));
				} else {
					stationWiseRemittanceBean.setArrearInspCharges("0");
				}
				if (rs.getString("sinspcharges") != null) {
					stationWiseRemittanceBean.setSuppliInspCharges(rs
							.getString("sinspcharges"));
				} else {
					stationWiseRemittanceBean.setSuppliInspCharges("0");
				}
				if (rs.getString("emppfstatuary") != null) {
					stationWiseRemittanceBean.setCpfEpf(rs
							.getString("emppfstatuary"));
				} else {
					stationWiseRemittanceBean.setCpfEpf("0");
				}
				if (rs.getString("aemppfstatuary") != null) {
					stationWiseRemittanceBean.setAEpf(rs
							.getString("aemppfstatuary"));
				} else {
					stationWiseRemittanceBean.setAEpf("0");
				}
				if (rs.getString("semppfstatuary") != null) {
					stationWiseRemittanceBean.setSEpf(rs
							.getString("semppfstatuary"));
				} else {
					stationWiseRemittanceBean.setSEpf("0");
				}
				if (rs.getString("ccpf") != null) {
					stationWiseRemittanceBean.setAaipc(rs.getString("ccpf"));
				} else {
					stationWiseRemittanceBean.setAaipc("0");
				}
				if (rs.getString("aapf") != null) {
					stationWiseRemittanceBean.setApc(rs.getString("aapf"));
				} else {
					stationWiseRemittanceBean.setApc("0");
				}
				if (rs.getString("sspf") != null) {
					stationWiseRemittanceBean.setSpc(rs.getString("sspf"));
				} else {
					stationWiseRemittanceBean.setSpc("0");
				}
				if (rs.getString("cpfvpf") != null) {
					stationWiseRemittanceBean.setCpfVpf(rs.getString("cpfvpf"));
				} else {
					stationWiseRemittanceBean.setCpfVpf("0");
				}
				if (rs.getString("avpf") != null) {
					stationWiseRemittanceBean.setAVpf(rs.getString("avpf"));
				} else {
					stationWiseRemittanceBean.setAVpf("0");
				}
				if (rs.getString("svpf") != null) {
					stationWiseRemittanceBean.setSVpf(rs.getString("svpf"));
				} else {
					stationWiseRemittanceBean.setSVpf("0");
				}
				if (rs.getString("cpfadvprincipal") != null) {
					stationWiseRemittanceBean.setCpfRefAdv(rs
							.getString("cpfadvprincipal"));
				} else {
					stationWiseRemittanceBean.setCpfRefAdv("0");
				}
				if (rs.getString("aadvprincipal") != null) {
					stationWiseRemittanceBean.setARefAdv(rs
							.getString("aadvprincipal"));
				} else {
					stationWiseRemittanceBean.setARefAdv("0");
				}
				if (rs.getString("sadvprincipal") != null) {
					stationWiseRemittanceBean.setSRefAdv(rs
							.getString("sadvprincipal"));
				} else {
					stationWiseRemittanceBean.setSRefAdv("0");
				}
				if (rs.getString("cpfadvrecint") != null) {
					stationWiseRemittanceBean.setCpfAdvInt(rs
							.getString("cpfadvrecint"));
				} else {
					stationWiseRemittanceBean.setCpfAdvInt("0");
				}
				if (rs.getString("aadvrecint") != null) {
					stationWiseRemittanceBean.setAAdvInt(rs
							.getString("aadvrecint"));
				} else {
					stationWiseRemittanceBean.setAAdvInt("0");
				}
				if (rs.getString("sadvrecint") != null) {
					stationWiseRemittanceBean.setSAdvInt(rs
							.getString("sadvrecint"));
				} else {
					stationWiseRemittanceBean.setSAdvInt("0");
				}

				if (rs.getString("cpfaddcontri") != null) {
					stationWiseRemittanceBean.setCpfaddcontri(rs
							.getString("cpfaddcontri"));
				} else {
					stationWiseRemittanceBean.setCpfaddcontri("0");
				}
				if (rs.getString("aaddcontri") != null) {
					stationWiseRemittanceBean.setAaddcontri(rs
							.getString("aaddcontri"));
				} else {
					stationWiseRemittanceBean.setAaddcontri("0");
				}
				if (rs.getString("saddcontri") != null) {
					stationWiseRemittanceBean.setSaddcontri(rs
							.getString("saddcontri"));
				} else {
					stationWiseRemittanceBean.setSaddcontri("0");
				}

				if (rs.getString("cpfacc") != null) {
					stationWiseRemittanceBean.setCpfTotal(rs
							.getString("cpfacc"));
				} else {
					stationWiseRemittanceBean.setCpfTotal("0");
				}
				if (rs.getString("ArrearAcc") != null) {
					stationWiseRemittanceBean.setATotal(rs
							.getString("ArrearAcc"));
				} else {
					stationWiseRemittanceBean.setATotal("0");
				}
				if (rs.getString("suppliacc") != null) {
					stationWiseRemittanceBean.setSTotal(rs
							.getString("suppliacc"));
				} else {
					stationWiseRemittanceBean.setSTotal("0");
				}
				totalPC = Double.parseDouble(stationWiseRemittanceBean
						.getCpfPc())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getArrearPc())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getSuppliPc());
				totalPF = Double.parseDouble(stationWiseRemittanceBean
						.getCpfPf())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getArrearPf())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getSuppliPf());
				totalInspCharges = Double.parseDouble(stationWiseRemittanceBean
						.getCpfInspCharges())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getArrearInspCharges())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getSuppliInspCharges());
				totalAddContri = Double.parseDouble(stationWiseRemittanceBean
						.getCpfaddcontri())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getAaddcontri())
						+ Double.parseDouble(stationWiseRemittanceBean
								.getSaddcontri());
				stationWiseRemittanceBean.setPcTotal(totalPC);
				stationWiseRemittanceBean.setPfTotal(totalPF);
				stationWiseRemittanceBean.setInspTotal(totalInspCharges);
				stationWiseRemittanceBean.setAddContriTotal(totalAddContri);

				stationsList.add(stationWiseRemittanceBean);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			commonDB.closeConnection(con, st, rs);
		}

		return stationsList;

	}
}
