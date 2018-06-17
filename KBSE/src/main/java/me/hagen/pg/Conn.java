package me.hagen.pg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {

	private static String url = "jdbc:postgresql://10.77.40.41/es";  
	private  static String username = "postgres";  
	private  static String password = "postgres500";  
	public static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.postgresql.Driver");  
		Connection conn = DriverManager.getConnection(url, username, password);   
		return conn;
	}

}
