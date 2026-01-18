package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.model.StrandsModel;
import com.javaguides.bms.helper.DbTableUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StrandsJDBCRepositoryImpl implements StrandsJDBCRepository {

    private static final String tblStrands = DbTableUtil.getTableName(StrandsModel.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StrandsJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<StrandsModel> findAllActiveStrands() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("status", SystemStatusEnum.ACTIVE.getKey());
        StringBuilder sql = new StringBuilder()
                .append(" SELECT * FROM ").append(tblStrands).append(" WHERE STATUS = :status ")
                .append(" ORDER BY TRACK_KEY ASC, STRAND_DSCP ASC ");

        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(StrandsModel.class));
    }

    @Override
    public StrandsModel findDtlsByKey(String key) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("strandKey", key);
        StringBuilder sql = new StringBuilder()
                .append(" SELECT * FROM ").append(tblStrands)
                .append(" WHERE STRAND_KEY = :strandKey ");
        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(StrandsModel.class))
                .stream().findFirst().orElse(null);
    }

}
