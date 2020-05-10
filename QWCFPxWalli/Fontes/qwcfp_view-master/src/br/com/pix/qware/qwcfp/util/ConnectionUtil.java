package br.com.pix.qware.qwcfp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionUtil {

	// String serverName = "SERV01:1433";
	static String url = "jdbc:oracle:thin:@172.16.253.77:1521:qwcfp";
	static String username = "qwcfpdes";
	static String password = "pix2000";

	private static Connection conn = null;

	public static Connection getConnection() {

		try {

			if (conn == null || conn.isClosed()) {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

				conn = DriverManager.getConnection(url, username, password);
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return conn;
		

	}
	
}
