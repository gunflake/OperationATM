package com.epozen.atm.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceManager {
	private static DataSourceManager manager;
	
	public DataSourceManager() throws ClassNotFoundException{
		String driverClass = "com.mysql.jdbc.Driver";
		Class.forName(driverClass);
		System.out.println("1. driver loading...");
	}
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		if(manager == null)
			new DataSourceManager();
		
		String url ="jdbc:mysql://localhost:3306/atm?autoReconnect=true&useSSL=false";
		String id = "root";
		String pw = "root";
		
		return DriverManager.getConnection(url, id, pw);
	}

}
