package org.alex.sqliteapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;

import org.alex.sqlite.entity.GoodDataTableModel;
import org.alex.sqliteapp.db.DBConnection;
import org.alex.sqliteapp.ui.DataTableView;

public class SqliteApp {

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type ='table'");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GoodDataTableModel tbl = new GoodDataTableModel();
                DataTableView tableView = new DataTableView(tbl);
                tableView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                tableView.reloadDataTable();
                tableView.setVisible(true);
            }
        });
        
        
        
        
        //System.out.println(tbl.getData().size());

    }

}
