package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.FeePricingModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SystemConfigJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements SystemConfigJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public SystemConfigJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    //static final String tblConfig =
    static final String tblPricing = DbTableUtil.getTableName(FeePricingModel.class);
    static final String tblPricingAlias = DbTableUtil.getTableAlias(FeePricingModel.class);

    @Override
    public int updateFeePricing(FeePricingModel modelObj) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("docSubCatKey", modelObj.getDocSubCatKey());
        map.addValue("fee", modelObj.getProcessFee());

        StringBuilder qry = new StringBuilder()
                .append(" UPDATE ").append(tblPricing).append(" SET PROCESS_FEE =:fee ")
                .append(" WHERE DOC_SUB_CAT_KEY =:docSubCatKey ");
        return namedParameterJdbcTemplate.update(qry.toString(), map);
    }

    @Override
    public List<FeePricingModel> pricingList() {
        StringBuilder qry = new StringBuilder()
                .append(" SELECT * FROM ").append(tblPricing)
                .append(" ORDER BY ID ASC ");
        return namedParameterJdbcTemplate.query(qry.toString(), new EmptySqlParameterSource(), new BeanPropertyRowMapper<>(FeePricingModel.class));
    }

}
