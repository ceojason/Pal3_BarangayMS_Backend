package com.javaguides.bms.helper;

import com.javaguides.bms.customannotations.TableAlias;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbTableUtil {
    public static String getTableName(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            return table.name();
        } else {
            return entityClass.getSimpleName();
        }
    }

    public static String getTableAlias(Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(TableAlias.class)) {
            TableAlias alias = entityClass.getAnnotation(TableAlias.class);
            return alias.value();
        }
        return "";
    }

    public static String getTableNameWithAlias(Class<?> entityClass) {
        String tableName;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            tableName = table.name();
        } else {
            tableName = entityClass.getSimpleName();
        }

        if (entityClass.isAnnotationPresent(TableAlias.class)) {
            TableAlias alias = entityClass.getAnnotation(TableAlias.class);
            return tableName + " " + alias.value();
        }

        return tableName;
    }

    public static String buildSelectClause(Class<?> modelClass) {
        StringBuilder select = new StringBuilder();
        String alias = getTableAlias(modelClass);

        List<Field> fields = new ArrayList<>();

        // Add declared fields from model class
        fields.addAll(Arrays.asList(modelClass.getDeclaredFields()));

        // Handle only 'id' field from superclass
        Class<?> superClass = modelClass.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            for (Field field : superClass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase("id")) {
                    fields.add(field); // Only add 'id'
                }
            }
        }

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if (field.getAnnotation(Transient.class) != null) continue;

            String columnName;
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                columnName = columnAnnotation.name();
            } else {
                columnName = toSnakeCase(field.getName()); // Default fallback
            }

            columns.add(alias + "." + columnName + " AS " + alias + "_" + field.getName());
        }

        select.append(String.join(", ", columns));
        return select.toString();
    }

    private static String toSnakeCase(String str) {
        // convert camelCase to SNAKE_CASE
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();
    }

    public static String buildSelectClause(List<String> fieldNames) {
        if (fieldNames == null || fieldNames.isEmpty()) {
            throw new IllegalArgumentException("Field names list cannot be null or empty.");
        }

        StringBuilder selectClause = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String field = fieldNames.get(i);
            selectClause.append(field).append(" AS ").append(field);

            if (i < fieldNames.size() - 1) {
                selectClause.append(", ");
            }
        }

        return selectClause.toString();
    }

    public static String buildSelectClause(List<String> fieldNames, String tableAlias) {
        if (fieldNames == null || fieldNames.isEmpty()) {
            throw new IllegalArgumentException("Field names list cannot be null or empty.");
        }

        StringBuilder selectClause = new StringBuilder();

        for (int i = 0; i < fieldNames.size(); i++) {
            String field = fieldNames.get(i);
            if (tableAlias != null && !tableAlias.isBlank()) {
                selectClause.append(tableAlias).append(".").append(field);
            } else {
                selectClause.append(field);
            }
            selectClause.append(" AS ").append(tableAlias+"_"+field);

            if (i < fieldNames.size() - 1) {
                selectClause.append(", ");
            }
        }

        return selectClause.toString();
    }

}
