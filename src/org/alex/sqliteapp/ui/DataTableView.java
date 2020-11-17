package org.alex.sqliteapp.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.alex.sqliteapp.db.DataTableModel;

import javax.swing.table.DefaultTableModel;
import org.alex.sqliteapp.util.EntityThrowable;

public class DataTableView extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -6001705584757283061L;

    private final transient DataTableModel<?> model;
    private JTable dataTable;

    /**
     * Create the panel.
     * @param model
     */
    public DataTableView(DataTableModel<?> model) {
        initGUI();        
        this.model = model;
    }

    private void initGUI() {
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        dataTable = new JTable();
        dataTable.setModel(new DefaultTableModel(
                new Object[][]{
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},},
                new String[]{
                    "", "", ""
                }
        ));
        scrollPane.setViewportView(dataTable);

    }

    public void reloadDataTable() {
        try {
            model.initializeData();
        } catch (EntityThrowable e) {            
        }
        
        dataTable.setModel(model.getModel());
    }

}
