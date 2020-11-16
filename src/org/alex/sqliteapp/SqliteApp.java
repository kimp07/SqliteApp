package org.alex.sqliteapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.alex.sqlite.entity.Good;
import org.alex.sqliteapp.db.DBConnection;
import org.alex.sqliteapp.db.DataTableModel;
import org.alex.sqliteapp.util.EntityException;

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
		
		try {
			DataTableModel<Good> tbl = new DataTableModel<>(Good.class);
			System.out.println(tbl.getData().size());
		} catch (EntityException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
