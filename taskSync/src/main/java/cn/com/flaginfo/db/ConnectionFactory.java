package cn.com.flaginfo.db;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectionFactory {

	private static ConnectionFactory factory ;
	
	public synchronized static ConnectionFactory getInstance(){
		if(factory==null){
			factory = new ConnectionFactory();
		}
		return factory;
	}
	
	public Connection getConnection(String dbname){
		Connection conn = null;
		try {
			//Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			//conn = DriverManager.getConnection("proxool." + dbname);
		    
			Class.forName(JDBCMessage.getString(dbname+".jdbc.driver"));
			conn = DriverManager.getConnection(JDBCMessage.getString(dbname+".db_url"),
			        JDBCMessage.getString(dbname+".user_name"),JDBCMessage.getString(dbname+".password"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	

}
