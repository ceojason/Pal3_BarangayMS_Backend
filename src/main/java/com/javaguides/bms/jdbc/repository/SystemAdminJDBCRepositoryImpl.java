package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SystemAdminJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements SystemAdminJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String tblAdmin = DbTableUtil.getTableName(SystemAdminModel.class);

    public SystemAdminJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int updateAdmin(SystemAdminModel modelObj) { return update(modelObj); }

    @Override
    public Optional<SystemAdminModel> findById(String id) {
        return super.findById(id, tblAdmin, SystemAdminModel.class);
    }

    @Override
    public SystemAdminModel findUserInResetNoSession(MainSearchRequest searchRequest) {
        MapSqlParameterSource map = new MapSqlParameterSource();

        map.addValue("firstNm", searchRequest.getFirstNm());
        map.addValue("lastNm", searchRequest.getLastNm());
        map.addValue("mobileNo", searchRequest.getMobileNo());

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ")
                .append(" FROM ").append(tblAdmin)
                .append(" WHERE ");

        if (searchRequest.getFirstNm() != null && !searchRequest.getFirstNm().isEmpty()) {
            sql.append(" first_nm = :firstNm ");
        }

        if (searchRequest.getLastNm() != null && !searchRequest.getLastNm().isEmpty()) {
            sql.append(" AND last_nm = :lastNm ");
        }

        if (searchRequest.getMobileNo() != null && !searchRequest.getMobileNo().isEmpty()) {
            sql.append(" AND mobile_no = :mobileNo ");
        }

        try {
            return namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(SystemAdminModel.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
