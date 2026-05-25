package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.helper.DbTableUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public class LoginJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements LoginJDBCRepository {
    private static final String tblLogin = DbTableUtil.getTableName(LoginCreds.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LoginJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<LoginCreds> getUserByCd(String userCd) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("cd", userCd);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblLogin)
                .append(" WHERE ").append(" CD = :cd ");
        try {
            LoginCreds user = namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(LoginCreds.class));
            assert user!=null;
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<LoginCreds> getUserById(String id) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", id);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblLogin)
                .append(" WHERE ").append(" USER_ID = :id ");
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(LoginCreds.class)));
    }

    @Override
    public int update(LoginCreds model) {
        return super.update(model);
    }

    @Override
    public int saveLoginCreds(LoginCreds loginCreds) {
        return save(loginCreds);
    }

    @Override
    public int deleteByUserCd(String userCd) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("cd", userCd);
        StringBuilder sql = new StringBuilder()
                .append(" DELETE FROM ").append(tblLogin).append(" WHERE ").append(" CD = :cd ");
        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    @Override
    public int deleteByUserId(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        StringBuilder sql = new StringBuilder()
                .append(" DELETE FROM ").append(tblLogin).append(" WHERE ").append(" USER_ID = :userId ");
        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

    @Override
    public Integer getActiveCount() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("status", SystemStatusEnum.ACTIVE.getKey());
        String sql = "SELECT COUNT(ID) FROM " + tblLogin + " WHERE LOGIN_STATUS =:status ";
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public int updateLoginDt(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("dt", new Date());
        map.addValue("userId", userId);
        map.addValue("status", SystemStatusEnum.ACTIVE.getKey());

        StringBuilder sql = new StringBuilder()
                .append("UPDATE ").append(tblLogin)
                .append(" SET UPDATED_DT = :dt, LOGIN_STATUS =:status ")
                .append("WHERE USER_ID = :userId");

        return namedParameterJdbcTemplate.update(sql.toString(), map);
    }

}
