package org.alex.sqliteapp.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.alex.sqliteapp.util.EntityThrowable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract class DataTableModel<T extends Object> {

    private static final Logger LOG = Logger.getLogger(DataTableModel.class);

    private final Class<T> entityClass;
    private final List<String> columnTitles;
    private static final int DEFAULT_VISIBLE_ROWS_COUNT = 300;
    //private final int CACHED_ROWS_COUNT = 15;
    private String querySelectAll;
    private String queryCountAll;

    private List<T> data;
    private Map<String, String> entityFields;
    private long totalRowsCount;
    private int currentFirstRowNumber;
    private int visibleRowsCount;

    public DataTableModel(Class<T> entityClass, List<String> columnTitles) {
        this.entityClass = entityClass;
        this.visibleRowsCount = DEFAULT_VISIBLE_ROWS_COUNT;
        this.currentFirstRowNumber = 0;
        this.columnTitles = columnTitles;
    }

    public void initializeData() throws EntityThrowable {
        querySelectAll = "";
        queryCountAll = "";
        if (entityClass.isAnnotationPresent(EntityObject.class)) {
            querySelectAll = entityClass.getAnnotation(EntityObject.class).query();
            queryCountAll = entityClass.getAnnotation(EntityObject.class).countQuery();
            if (querySelectAll.isEmpty()) {
                throw new EntityThrowable("Query `SELECT ALL` is empty");
            }
            if (queryCountAll.isEmpty()) {
                throw new EntityThrowable("Query `SELECT count(*) ALL` is empty");
            }
        } else {
            throw new EntityThrowable(entityClass.getCanonicalName() + " is not @EntityObject");
        }
        entityFields = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(EntityField.class)) {
                String fieldName = field.getName();
                String fieldDbName = field.getAnnotation(EntityField.class).fieldName().trim();
                if (fieldName != null && !fieldName.isEmpty()) {
                    entityFields.put(fieldName, fieldDbName);
                }
            }
        }
        if (entityFields.isEmpty()) {
            throw new EntityThrowable(entityClass.getCanonicalName() + " has no fields annotated @EntityField");
        }
        data = new ArrayList<>();
        readData();
    }

    private void fillDataFromResultSet(ResultSet rs, boolean insertTop) {
        try {
            int index = 0;
            while (rs.next()) {
                try {
                    T entityObject = entityClass.getConstructor().newInstance();
                    for (Map.Entry<String, String> entry : entityFields.entrySet()) {
                        String fieldName = entry.getKey();
                        String fieldDbName = entry.getValue();

                        if (rs.getObject(fieldDbName) != null) {
                            Field field = entityObject.getClass().getDeclaredField(fieldName);
                            Class<?> fieldType = field.getType();
                            field.setAccessible(true);
                            Object dataValue = rs.getObject(fieldDbName);
                            if (fieldType.equals(Long.class)) {
                                field.set(entityObject, Long.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Float.class)) {
                                field.set(entityObject, Float.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Double.class)) {
                                field.set(entityObject, Double.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Integer.class)) {
                                field.set(entityObject, Integer.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Short.class)) {
                                field.set(entityObject, Short.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Byte.class)) {
                                field.set(entityObject, Byte.valueOf(dataValue.toString()));
                            } else if (fieldType.equals(Boolean.class)) {
                                field.set(entityObject, Boolean.valueOf(dataValue.toString()));
                            } else {
                                field.set(entityObject, String.valueOf(dataValue));
                            }
                        }
                    }
                    if (!insertTop) {
                        data.add(entityObject);
                    } else {
                        data.add(index++, entityObject);
                    }

                } catch (NoSuchMethodException | InvocationTargetException | NoSuchFieldException | IllegalAccessException | InstantiationException e) {
                    LOG.log(Level.ERROR, e);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.ERROR, e);
        }
    }

    public void readData() {
        String query = querySelectAll + " LIMIT " + visibleRowsCount;
        data.clear();
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(queryCountAll);
                totalRowsCount = resultSet.getLong(1);
            } catch (SQLException e) {
                LOG.log(Level.ERROR, e);
            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                fillDataFromResultSet(resultSet, false);
            } catch (SQLException e) {
                LOG.log(Level.ERROR, e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                LOG.log(Level.ERROR, e);
            }

        }
    }

    public int getVisibleRowsCount() {
        return visibleRowsCount;
    }

    public void setVisibleRowsCount(int visibleRowsCount) {
        this.visibleRowsCount = visibleRowsCount;
    }

    public int getCurrentFirstRowNumber() {
        return currentFirstRowNumber;
    }

    public void setCurrentFirstRowNumber(int currentFirstRowNumber) {
        this.currentFirstRowNumber = currentFirstRowNumber;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public List<T> getData() {
        return data;
    }

    public long getTotalRowsCount() {
        return totalRowsCount;
    }

    public DefaultTableModel getModel() {

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnTitles.stream().toArray());
        try {
            T entityObject = entityClass.getConstructor().newInstance();

            for (T item : data) {
                Object[] row = new Object[entityFields.size()];
                int pos = 0;
                for (Map.Entry<String, String> fieldName : entityFields.entrySet()) {
                    Field field = entityObject.getClass().getDeclaredField(fieldName.getKey());
                    field.setAccessible(true);
                    row[pos] = field.get(item);
                    pos++;
                }
                model.addRow(row);
            }
        } catch (NoSuchMethodException e) {
            LOG.error(e);
        } catch (NoSuchFieldException e) {
            LOG.error(e);
        } catch (IllegalAccessException e) {
            LOG.error(e);
        } catch (InstantiationException | InvocationTargetException e) {
            LOG.error(e);
        }
        return model;
    }

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public abstract void scrollData();
}
