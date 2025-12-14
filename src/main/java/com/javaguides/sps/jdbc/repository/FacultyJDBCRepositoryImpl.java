package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.enums.SystemStatusEnum;
import com.javaguides.sps.model.FacultyModel;
import com.javaguides.sps.helper.DbTableUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FacultyJDBCRepositoryImpl implements FacultyJDBCRepository {

    private static final String tblFaculty = DbTableUtil.getTableName(FacultyModel.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FacultyJDBCRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public FacultyModel getBySectionId(String sectionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("sectionId", sectionId);
        map.addValue("status", SystemStatusEnum.ACTIVE.getKey());

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ")
                .append(" FROM ").append(tblFaculty).append(" ")
                .append(" WHERE SECTION_ID = :sectionId AND STATUS = :status ");

        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(FacultyModel.class))
                .stream().findFirst().orElse(null);
    }
}
