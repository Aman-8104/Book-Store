package com.ebook.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for managing database connections
 * Uses JDBC to connect to MySQL database
 */
public class DBConnection {
    
    // Database configuration - reads from environment variables for cloud deployment
    private static final String DB_HOST = System.getenv("MYSQLHOST") != null ? System.getenv("MYSQLHOST") : "localhost";
    private static final String DB_PORT = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
    private static final String DB_NAME = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : "ebook_management";
    private static final String USERNAME = System.getenv("MYSQLUSER") != null ? System.getenv("MYSQLUSER") : "root";
    private static final String PASSWORD = System.getenv("MYSQLPASSWORD") != null ? System.getenv("MYSQLPASSWORD") : "root";
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Singleton instance
    private static DBConnection instance;
    private Connection connection;
    
    /**
     * Private constructor to prevent direct instantiation
     */
    private DBConnection() {
        try {
            Class.forName(DRIVER);
            System.out.println("MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get the singleton instance of DBConnection
     * @return DBConnection instance
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Get database connection
     * Creates a new connection if current one is null or closed
     * @return Connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to create database connection!");
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * Get a new connection (for cases where multiple connections are needed)
     * @return New Connection object
     */
    public static Connection getNewConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to create new database connection!");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Close the connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection!");
            e.printStackTrace();
        }
    }
    
    /**
     * Close a specific connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Test database connection
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try {
            Connection testConn = getConnection();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("Database connection test: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database connection test: FAILED");
            e.printStackTrace();
        }
        return false;
    }
}
