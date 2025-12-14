package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.sps.model.LoginCreds;
import com.javaguides.sps.helper.DbTableUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LoginJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements LoginJDBCRepository {
    private static final String tblLogin = DbTableUtil.getTableName(LoginCreds.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LoginJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<LoginCreds> getUserByCd(String userCd) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("cd", userCd);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ")
                .append(" FROM ")
                .append(tblLogin)
                .append(" WHERE ")
                .append(" CD = :cd ");
        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(LoginCreds.class));
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

}
