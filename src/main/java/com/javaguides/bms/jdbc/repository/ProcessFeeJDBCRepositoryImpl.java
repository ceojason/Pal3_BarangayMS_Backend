package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.ProcessFeeModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessFeeJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements ProcessFeeJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public ProcessFeeJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static final String tblProcessFee = DbTableUtil.getTableName(ProcessFeeModel.class);
    static final String tblProcessFeeAlias = DbTableUtil.getTableAlias(ProcessFeeModel.class);

    @Override
    public ProcessFeeModel getProcessFeeByKey(Integer key) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("key", key);

        StringBuilder qry = new StringBuilder() .append(" SELECT * FROM ").append(tblProcessFee).append(" WHERE DOC_SUB_CAT_KEY =:key ");
        return namedParameterJdbcTemplate.queryForObject(qry.toString(), map, new BeanPropertyRowMapper<>(ProcessFeeModel.class));
    }

}
