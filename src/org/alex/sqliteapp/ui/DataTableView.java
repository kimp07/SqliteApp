package org.alex.sqliteapp.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.alex.sqliteapp.db.DataTableModel;
import org.alex.sqliteapp.util.EntityException;

import javax.swing.table.DefaultTableModel;

public class DataTableView<T> extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6001705584757283061L;
	
	private DataTableModel<T> model;
	private JTable dataTable;
	/**
	 * Create the panel.
	 */
	public DataTableView(Class<T> entityClass) {
		try {
			model = new DataTableModel<>(entityClass);
		}
		catch (EntityException e) {
			e.printStackTrace();
		}
		initGUI();
		reloadDataTable();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		dataTable = new JTable();
		dataTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
				{null, null, null},
				{null, null, null},
			},
			new String[] {
				"New column", "New column", "New column"
			}
		));
		scrollPane.setViewportView(dataTable);
		
	}
	
	public void reloadDataTable() {
		dataTable.setModel(model.getModel());		
	}
	
}
