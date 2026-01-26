package com.javaguides.bms.jdbc.repository.basejdbcrepository;

import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.util.Optional;

public interface BaseJDBCRepository {
    int save(Object entity);

    int save(Object entity, String tableName);

    String getOrderBy(PageRequest page, Class<?> modelClass);

    String count();

    Field getField(Class<?> clazz, String fieldName);

    <T> Optional<T> findById(String id, String tableName, Class<T> clazz);

    int update(Object entity);
}
