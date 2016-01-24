
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Execute.java

import common.db.DBUtil;
import common.db.QueryManager;
import info.DayPriceInfo;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.sql.RowSet;

public class Execute {

	public Execute() {
	}

	public static void main(String args[]) {
		if (args == null)
			System.out.println("args[0] end Date: yyyy-mm-dd");
		else if (args.length == 1)
			execute(args[0], false);
		else
			execute(args[0], true);
	}

	private static void execute(String argStopDate, boolean isInsertAvg) {
		Connection conn;
		QueryManager qm2;
		String stopDate;
		RowSet rs;
		qm2 = new QueryManager();
		stopDate = argStopDate.replaceAll("-", "");
		conn = null;
		rs = null;
		long stime = System.currentTimeMillis();
		try {
			conn = DBUtil.getMySQLConnection("stock_manager", "stockmanager", "managerstock");
			rs = qm2.select(conn, "select CODE from TBL_COMPANY");
			int count = 0;
			boolean isOK = false;
			while (rs.next()) {
				conn.setAutoCommit(false);
				QueryManager qm = new QueryManager();
				String code = rs.getString("CODE");
				Hashtable arr = SearchStock.dayPrice(code, stopDate);
				Enumeration em = arr.keys();
				System.out.println((new StringBuilder(String.valueOf(++count))).append(" - ").append(code).append(" : ")
						.append(arr.size()).toString());
				while (em.hasMoreElements()) {
					String key = (String) em.nextElement();
					DayPriceInfo info = (DayPriceInfo) arr.get(key);
					qm.set("CODE", code, false);
					qm.set("TRADE_DATE", "STR_TO_DATE(?,'%Y%m%d')", key, false);
					qm.set("OPEN_PRICE", info.getOpenPrice(), false);
					qm.set("CLOSING_PRICE", info.getClosePrice(), false);
					qm.set("GAP_PRICE", info.getGapPrice(), false);
					qm.set("HIGH_PRICE", info.getHighPrice(), false);
					qm.set("LOW_PRICE", info.getLowPrice(), false);
					qm.set("TRADE_VOLUME", info.getTradeVolume(), false);
					qm.insertAddSQL("TBL_PRICE_DAY");
					isOK = true;
				}
				qm.executeBatch(conn);
				conn.setAutoCommit(true);
				DBUtil.commit(conn);
				qm.clear();
				if (count > 10 && !isOK) {
					System.out.println(
							(new StringBuilder("[")).append(argStopDate).append("] data not found !!!").toString());
					break;
				}
			}
			if (isOK) {
				CallableStatement stmt = conn.prepareCall("{call SP_CREATE_AVG(?)}");
				stmt.setString(1, argStopDate);
				stmt.executeQuery();
				qm2.close(stmt);
				System.out.println("[SP1] CREATE SP_CREATE_AVG DATA SUCCESS ...");

				CallableStatement stmt2 = conn.prepareCall("{call SP_CREATE_TRADE_AVG(?)}");
				stmt2.setString(1, argStopDate);
				stmt2.executeQuery();
				qm2.close(stmt2);
				
				System.out.println("[SP1] CREATE SP_CREATE_TRADE_AVG DATA SUCCESS ...");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(conn);
		}

	}
}
