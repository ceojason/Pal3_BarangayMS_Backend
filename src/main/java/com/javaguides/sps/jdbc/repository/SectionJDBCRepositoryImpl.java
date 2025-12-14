package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.enums.SystemStatusEnum;
import com.javaguides.sps.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.sps.model.SectionModel;
import com.javaguides.sps.helper.DbTableUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SectionJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements SectionJDBCRepository {

    private static final String tblSection = DbTableUtil.getTableName(SectionModel.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SectionJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<SectionModel> getAllActiveSectionsByKey(String yearlevelKey, String strandKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("status", SystemStatusEnum.ACTIVE.getKey());
        map.addValue("yearlevelKey", yearlevelKey);
        map.addValue("strandKey", strandKey);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblSection).append(" WHERE ")
                .append(" STATUS = :status AND YEARLEVEL_KEY = :yearlevelKey AND STRAND_KEY = :strandKey ")
                .append(" ORDER BY SECTION_NM ASC ");
        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(SectionModel.class));
    }

    @Override
    public SectionModel findDtlsById(String sectionId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", sectionId);
        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblSection).append(" WHERE ")
                .append(" ID = :id ");
        return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(SectionModel.class));
    }

}
