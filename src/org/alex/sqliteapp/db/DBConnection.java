package org.alex.sqliteapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author zamdirit
 */
public class DBConnection {

    private static Connection connection;
    private static final String CONNECTION_URL = "jdbc:sqlite:msalers.sqlite";

    private static final Logger LOG = Logger.getLogger(DBConnection.class);

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(CONNECTION_URL);
        } catch (ClassNotFoundException | SQLException e) {
            LOG.log(Level.ERROR, e);
        }
    }

    /**
     * Constructor
     */
    private DBConnection() {
    }

    /**
     *
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }
}
