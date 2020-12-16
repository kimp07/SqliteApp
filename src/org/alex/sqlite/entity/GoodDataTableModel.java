package org.alex.sqlite.entity;

import java.util.List;
import org.alex.sqliteapp.db.DataTableModel;

/**
 *
 * @author zamdirit
 *
 */
public class GoodDataTableModel extends DataTableModel<Good> {

    public GoodDataTableModel(List<String> columnTitles) {
        super(Good.class);
    }

    @Override
    public void scrollData() {

    }

}
