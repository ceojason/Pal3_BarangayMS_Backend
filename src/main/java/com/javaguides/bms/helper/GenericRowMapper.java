package com.javaguides.bms.helper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenericRowMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> rowMap = new LinkedHashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int col = 1; col <= columnCount; col++) {
            // Get the exact column label (alias) as returned by the query
            String columnLabel = metaData.getColumnLabel(col);
            Object value = rs.getObject(col);
            rowMap.put(columnLabel, value);
        }

        return rowMap;
    }
}