package org.alex.sqliteapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.alex.sqlite.entity.Good;
import org.alex.sqlite.entity.GoodDataTableModel;
import org.alex.sqliteapp.db.DBConnection;

public class SqliteApp {

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        try {
            ResultSet rs = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type ='table'").executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        GoodDataTableModel tbl = new GoodDataTableModel();
        System.out.println(tbl.getData().size());

    }

}
