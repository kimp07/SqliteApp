package org.alex.sqliteapp.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.alex.sqliteapp.util.EntityException;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public abstract class DataTableModel<T extends Object> {
    
    private static final Logger LOG = Logger.getLogger(DataTableModel.class);
    
    private final Class<T> entityClass;
    private final int DEFAULT_VISIBLE_ROWS_COUNT = 100;
    //private final int CACHED_ROWS_COUNT = 15;
    private String querySelectAll;
    private String queryCountAll;

    private ArrayList<T> data;
    private HashMap<String, String> entityFields;
    private long totalRowsCount;
    private int currentFirstRowNumber;
    private int visibleRowsCount;

    public DataTableModel(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.visibleRowsCount = DEFAULT_VISIBLE_ROWS_COUNT;
        this.currentFirstRowNumber = 0;
    }

    public void initializeData() throws EntityException {
        querySelectAll = "";
        queryCountAll = "";
        if (entityClass.isAnnotationPresent(EntityObject.class)) {
            querySelectAll = entityClass.getAnnotation(EntityObject.class).query();
            queryCountAll = entityClass.getAnnotation(EntityObject.class).countQuery();
            if (querySelectAll == null || querySelectAll.isEmpty()) {
                throw new EntityException("Query `SELECT ALL` is empty");
            }
            if (queryCountAll == null || queryCountAll.isEmpty()) {
                throw new EntityException("Query `SELECT count(*) ALL` is empty");
            }
        } else {
            throw new EntityException(entityClass.getCanonicalName() + " is not @EntityObject");
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
            throw new EntityException(entityClass.getCanonicalName() + " has no fields annotated @EntityField");
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
                    LOG.log(Priority.ERROR, e.getMessage());
                }
            }
        } catch (SQLException e) {
            LOG.log(Priority.ERROR, e.getMessage());
        }
    }

    public void readData() {
        String query = querySelectAll + " LIMIT " + visibleRowsCount;
        data.clear();
        Connection connection = DBConnection.getConnection();
        try {
            ResultSet rs = connection.prepareStatement(queryCountAll).executeQuery();
            totalRowsCount = rs.getLong(1);
            rs = connection.prepareStatement(query).executeQuery();
            fillDataFromResultSet(rs, false);
        } catch (SQLException e) {
            LOG.log(Priority.ERROR, e.getMessage());
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

    public ArrayList<T> getData() {
        return data;
    }

    public long getTotalRowsCount() {
        return totalRowsCount;
    }

    public DefaultTableModel getModel() {
        return null;
    }

    public abstract void scrollData();
}
