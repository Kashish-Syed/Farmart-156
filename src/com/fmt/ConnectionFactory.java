package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

public class ConnectionFactory {

	/**
	 * Logging parameters that are necessary for logging system
	 */
	public static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ConnectionFactory.class);

	/**
	 * Connection parameters that are necessary for CSE's configuration
	 */
	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String USERNAME = "ksyed";
	public static final String PASSWORD = "JE2:Px";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;

	/**
	 * Method to make a connection to the database
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = null;
		ConnectionFactory.log.info("Opening connection...");
		try {
			conn = DriverManager.getConnection(ConnectionFactory.URL, ConnectionFactory.USERNAME,
					ConnectionFactory.PASSWORD);
		} catch (SQLException e) {
			ConnectionFactory.log.error("Failed to connect to the database", e);
		}
		ConnectionFactory.log.info("Connection success!");

		return conn;
	}

}