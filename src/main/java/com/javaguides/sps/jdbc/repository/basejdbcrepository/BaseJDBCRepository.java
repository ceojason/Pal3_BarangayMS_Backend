package com.javaguides.sps.jdbc.repository.basejdbcrepository;

import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;

public interface BaseJDBCRepository {
    int save(Object entity);

    int save(Object entity, String tableName);

    String getOrderBy(PageRequest page, Class<?> modelClass);

    String count();

    Field getField(Class<?> clazz, String fieldName);
}
