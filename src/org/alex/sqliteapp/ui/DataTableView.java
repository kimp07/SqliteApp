package org.alex.sqliteapp.ui;

import java.awt.BorderLayout;
import java.awt.event.AdjustmentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollBar;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.alex.sqliteapp.db.AbstractDataTableModel;

import javax.swing.table.DefaultTableModel;
import org.alex.sqliteapp.util.EntityThrowable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DataTableView extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -6001705584757283061L;
    private static final Logger LOG = Logger.getLogger(DataTableView.class);

    private final transient AbstractDataTableModel<?> model;
    private JTable dataTable;
    private int oldVPos = 20;

    /**
     * Create the panel.
     *
     * @param model
     */
    public DataTableView(AbstractDataTableModel<?> model) {
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
        scrollPane.getVerticalScrollBar().addAdjustmentListener(
                e -> {
                    int vPos = scrollPane.getVerticalScrollBar().getValue();
                    if (vPos != oldVPos) {
                        if (vPos > oldVPos) {
                            scrollDown(e);
                        } else {
                            scrollUp(e);
                        }
                    }
                }
        );
    }

    private void scrollUp(AdjustmentEvent e) {
        JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
        int rowsFetch = - (e.getValue() - oldVPos) / dataTable.getRowHeight();        
        
        if (model.scrollDataUp((DefaultTableModel) dataTable.getModel(), rowsFetch)) {
            dataTable.updateUI();
            scrollBar.setValue(oldVPos);            
        }
    }

    private void scrollDown(AdjustmentEvent e) {
        JScrollBar scrollBar = (JScrollBar) e.getAdjustable();
        int rowsFetch = (e.getValue() - oldVPos) / dataTable.getRowHeight();        
        
        if (model.scrollDataDown((DefaultTableModel) dataTable.getModel(), rowsFetch)) {
            dataTable.updateUI();
            scrollBar.setValue(oldVPos);            
        }
    }

    public void reloadDataTable() {
        try {
            model.initializeData();
            model.readData();

        } catch (EntityThrowable e) {
            LOG.log(Level.ERROR, e.getMessage());
        }
        List<String> modelColumns = new ArrayList<>();
        modelColumns.add("id");
        modelColumns.add("name");
        modelColumns.add("fullName");
        modelColumns.add("vat");
        modelColumns.add("barCode");

        List<String> columnsTitle = new ArrayList<>();
        columnsTitle.add("ID");
        columnsTitle.add("Good name");
        columnsTitle.add("Description");
        columnsTitle.add("Vat, %");
        columnsTitle.add("Barcode");

        model.setColumnsTitle(columnsTitle);
        dataTable.setModel(model.getModel());
    }

}
