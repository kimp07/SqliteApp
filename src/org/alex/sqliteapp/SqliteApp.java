package org.alex.sqliteapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.alex.sqlite.entity.GoodDataTableModel;
import org.alex.sqliteapp.db.DBConnection;
import org.alex.sqliteapp.util.EntityThrowable;

public class SqliteApp {

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        try {
            ResultSet rs = conn
                    .prepareStatement("SELECT name FROM sqlite_master WHERE type ='table'")
                    .executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        GoodDataTableModel tbl = new GoodDataTableModel();
        Thread thread = new Thread(() -> {
            try {
                tbl.initializeData();
            } catch (EntityThrowable e) {
                System.out.println(e.getMessage());
            }
        });

        thread.start();
        String processVisual = "Loading data...";
        do {
            System.out.println(processVisual);
        } while (thread.isAlive());

        System.out.println(tbl.getData().size());

    }

}
