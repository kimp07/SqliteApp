package org.alex.sqliteapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;
    private static final String CONNECTION_URL = "jdbc:sqlite:msalers.sqlite";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(CONNECTION_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private DBConnection() {}
    
    public static Connection getConnection() {
        return connection;
    }
}
