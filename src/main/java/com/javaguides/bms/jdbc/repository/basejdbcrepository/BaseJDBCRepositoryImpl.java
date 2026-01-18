package com.javaguides.bms.jdbc.repository.basejdbcrepository;

import com.javaguides.bms.helper.DbTableUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

@Repository
public class BaseJDBCRepositoryImpl implements BaseJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BaseJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int save(Object entity) {
        Class<?> clazz = entity.getClass();
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) throw new IllegalArgumentException("Class must be annotated with @Table");

        String tableName = table.name();

        // We'll use two maps to enforce order: ID first, others after
        Map<String, Object> idFieldMap = new LinkedHashMap<>();
        Map<String, Object> otherFieldMap = new LinkedHashMap<>();

        while (clazz != null) { // Handles superclass fields like in BaseModel
            for (Field field : clazz.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String columnName = column.name();
                    field.setAccessible(true);
                    try {
                        Object value = field.get(entity);

                        // 💡 Auto-generate 32-char ID if field or column is "ID" and it's null or blank
                        if ((field.getName().equalsIgnoreCase("id") || columnName.equalsIgnoreCase("id"))
                                && (value == null || String.valueOf(value).isBlank())) {
                            String uuid = UUID.randomUUID().toString().replace("-", "");
                            value = uuid;
                            field.set(entity, uuid); // Inject generated ID back into the object
                        }

                        // 👉 Put "ID" column first in insert order
                        if (columnName.equalsIgnoreCase("id")) {
                            idFieldMap.put(columnName, value);
                        } else {
                            otherFieldMap.put(columnName, value);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access field: " + field.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass(); // In case fields are inherited
        }

        // Combine ID first, then the rest
        Map<String, Object> finalColumnValues = new LinkedHashMap<>();
        finalColumnValues.putAll(idFieldMap);
        finalColumnValues.putAll(otherFieldMap);

        return genericSave(tableName, finalColumnValues);
    }

    @Override
    public int save(Object entity, String tableName) {
        if (tableName == null || tableName.isBlank()) {
            throw new IllegalArgumentException("Table name must not be null or blank");
        }

        Class<?> clazz = entity.getClass();
        Map<String, Object> columnValues = new LinkedHashMap<>();

        while (clazz != null) { // Handles superclasses like BaseModel
            for (Field field : clazz.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String columnName = column.name();
                    field.setAccessible(true);
                    try {
                        Object value = field.get(entity);

                        // 💡 Auto-generate 32-char ID if field name is "id" or column is "ID" and value is null/blank
                        if ((field.getName().equalsIgnoreCase("id") || columnName.equalsIgnoreCase("id"))
                                && (value == null || String.valueOf(value).isBlank())) {
                            String uuid = UUID.randomUUID().toString().replace("-", "");
                            value = uuid;
                            field.set(entity, uuid);
                        }

                        columnValues.put(columnName, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access field: " + field.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return genericSave(tableName, columnValues);
    }


    private int genericSave(String tableName, Map<String, Object> fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" (");

        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");

        for (String field : fields.keySet()) {
            columns.add(field);
            values.add(":" + field);
        }

        sql.append(columns).append(") VALUES (").append(values).append(")");
        return namedParameterJdbcTemplate.update(sql.toString(), fields);
    }

    @Override
    public String getOrderBy(PageRequest page, Class<?> modelClass) {
        if (page == null || page.getSort() == null || !page.getSort().isSorted()) {
            return "";
        }

        String alias = DbTableUtil.getTableAlias(modelClass);
        StringBuilder orderBy = new StringBuilder(" ORDER BY ");
        List<String> orders = new ArrayList<>();

        page.getSort().forEach(order -> {
            String property = order.getProperty();
            String direction = order.getDirection().isAscending() ? "ASC" : "DESC";
            orders.add(alias + "." + property + " " + direction);
        });
        if (orders.isEmpty()) {
            return "";
        }
        orderBy.append(String.join(", ", orders));
        return orderBy.toString();
    }

    @Override
    public String count() {
        return " COUNT(*) as COUNT ";
    }

    @Override
    public Field getField(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // Move to superclass
            }
        }
        return null;
    }

    @Override
    public <T> Optional<T> findById(String id, String tableName, Class<T> clazz) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            T result = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    params,
                    new BeanPropertyRowMapper<>(clazz)
            );
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            // No record found
            return Optional.empty();
        } catch (Exception e) {
            // Unexpected error
            System.err.println("Error in findById for table " + tableName + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
