package bankapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // 1. Define the connection parameters for XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/bank_db";
    private static final String USERNAME = "root"; // Default XAMPP username
    private static final String PASSWORD = "";     // Default XAMPP password is empty

    // 2. Create a method that returns an open pipeline connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Attempt to establish a link to the database server
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database Connection Failed! Error: " + e.getMessage());
        }
        return conn;
    }
}