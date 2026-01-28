package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.DocumentModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DocumentJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements DocumentJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DocumentJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static final String tblName = DbTableUtil.getTableName(DocumentModel.class);
    static final String tblNameAlias = DbTableUtil.getTableAlias(DocumentModel.class);

    @Override
    public int saveRequest(DocumentModel modelObj) {
        return save(modelObj);
    }

    @Override
    public Integer getCount() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("today", new java.sql.Date(System.currentTimeMillis()));
        String sql = "SELECT COUNT(ID) FROM " + tblName + " WHERE DATE(date_requested) = :today";
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public Optional<DocumentModel> findById(String id) {
        return super.findById(id, tblName, DocumentModel.class);
    }

    @Override
    public List<DocumentModel> findPendingRequestByUserIdAndKey(String userId, Integer key) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("documentType", key);

        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause2(DocumentModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(DocumentModel.class))
                .append(" WHERE ").append(tblNameAlias).append(".USER_ID = :userId ")
                .append(" ORDER BY ").append(tblNameAlias).append(".DATE_REQUESTED DESC");

        return namedParameterJdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(DocumentModel.class));
    }
}
