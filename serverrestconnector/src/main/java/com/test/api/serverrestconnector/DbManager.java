package com.test.api.serverrestconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.test.api.serverrestconnector.model.Employee;

public class DbManager{
	private static final Logger log;
	static String url = String.format("jdbc:mysql://DATABASE_NAME.mysql.database.azure.com:3306/employee_db?serverTimezone=UTC&verifyServerCertificate=true&useSSL=true&requireSSL=false");
	static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-7s] %5$s %n");
        log =Logger.getLogger(DbManager.class.getName());
    }
	
	public static Connection connectToDb() throws Exception{
		log.info("Connecting to the database");
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(url,"example@DATABASE_NAME","password"); 
		return conn;
	}
	
	public static void closeDbConnection(Connection conn) throws SQLException{
		log.info("Closing database connection");
        conn.close();
        AbandonedConnectionCleanupThread.uncheckedShutdown();
	}
	
	public static StringBuffer readDataFromDb(Connection conn) throws SQLException{
		log.info("Read data");
		PreparedStatement readStatement = conn.prepareStatement("SELECT * FROM Employee;");
		ResultSet resultSet = readStatement.executeQuery();
			StringBuffer sbTemp = new StringBuffer();
		while(resultSet.next()){
			Employee empTemp = new Employee();
			empTemp.setId(resultSet.getInt("emp_id"));
			empTemp.setName(resultSet.getString("name"));
			sbTemp.append(empTemp.toString());
			sbTemp.append(" ");
		}
		
		log.info("Data read from the database: " +sbTemp);
		return sbTemp;
	}
	
	public static void insertDataToDb(Employee emp,Connection conn) throws SQLException{
		log.info("Insert data");
		 PreparedStatement insertStatement = conn
		            .prepareStatement("INSERT INTO Employee (emp_id, name) VALUES (?, ?);");
		 
		 insertStatement.setInt(1, emp.getId());
		 insertStatement.setString(2, emp.getName());
		 insertStatement.executeUpdate();
	}
}
