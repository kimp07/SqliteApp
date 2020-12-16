package org.alex.sqliteapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

import org.alex.sqlite.entity.GoodDataTableModel;
import org.alex.sqliteapp.db.DBConnection;
import org.alex.sqliteapp.ui.DataTableView;

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
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                List<String> titles = new ArrayList<>();
                titles.add("id");
                titles.add("Name");
                titles.add("Description");
                titles.add("Vat");
                titles.add("Barcode");

                GoodDataTableModel tbl = new GoodDataTableModel(titles);
                DataTableView tableView = new DataTableView(tbl);
                tableView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                tableView.reloadDataTable();
                tableView.setVisible(true);
            }
        });
        
        
        
        
        //System.out.println(tbl.getData().size());

    }

}
