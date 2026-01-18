package com.javaguides.bms.helper;

import jakarta.persistence.Column;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GenericModelRowMapper<T> implements RowMapper<T> {

    private final Class<T> type;

    public GenericModelRowMapper(Class<T> type) {
        this.type = type;
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            T instance = type.getDeclaredConstructor().newInstance();

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);

                String columnName = getColumnName(field);
                Object value;

                if (columnName == null) continue;

                Class<?> fieldType = field.getType();

                if (fieldType == String.class) {
                    value = rs.getString(columnName);
                } else if (fieldType == int.class || fieldType == Integer.class) {
                    value = rs.getInt(columnName);
                } else if (fieldType == long.class || fieldType == Long.class) {
                    value = rs.getLong(columnName);
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    value = rs.getBoolean(columnName);
                } else if (fieldType == Date.class) {
                    value = rs.getTimestamp(columnName);
                } else {
                    value = rs.getObject(columnName); // Fallback for unknown types
                }

                field.set(instance, value);
            }

            return instance;
        } catch (Exception e) {
            throw new SQLException("Mapping error for class: " + type.getSimpleName(), e);
        }
    }

    private String getColumnName(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);
        if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
            return columnAnnotation.name();
        } else {
            return null;
        }
    }
}