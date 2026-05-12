package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.UsersModel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HouseholdJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements HouseholdJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String tblHousehold = DbTableUtil.getTableName(HouseholdModel.class);
    static final String tblHouseholdAlias = DbTableUtil.getTableAlias(HouseholdModel.class);
    static final String tblUsers = DbTableUtil.getTableName(UsersModel.class);
    static final String tblUsersAlias = DbTableUtil.getTableAlias(UsersModel.class);

    public HouseholdJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public String save(HouseholdModel modelObj) {
        super.save(modelObj);
        return modelObj.getId();
    }

    @Override
    public Optional<HouseholdModel> findDuplicateHousehold(String uniqKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("uniqKey", uniqKey);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblHousehold)
                .append(" WHERE ").append(" HOUSEHOLD_UNIQ_KEY = :uniqKey ");
        try {
            HouseholdModel household = namedParameterJdbcTemplate.queryForObject(sql.toString(), map, new BeanPropertyRowMapper<>(HouseholdModel.class));
            assert household!=null;
            return Optional.of(household);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<HouseholdModel> findById(String id) {
        return super.findById(id, tblHousehold, HouseholdModel.class);
    }

    @Override
    public List<HouseholdModel> findHouseholdByHeadAndStatus(Integer status, String block, String lot, Integer phaseKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("status", status);
        map.addValue("block", block);
        map.addValue("lot", lot);
        map.addValue("phaseKey", phaseKey);
        map.addValue("isHouseholdHead", YesOrNoEnum.YES.getKey());

        StringBuilder qry = new StringBuilder()
                .append(" SELECT ")
                .append(tblHouseholdAlias).append(".*, ")
                .append(tblUsersAlias).append(".FIRST_NM AS firstNm, ")
                .append(tblUsersAlias).append(".MIDDLE_NM AS middleNm, ")
                .append(tblUsersAlias).append(".LAST_NM AS lastNm, ")
                .append(tblUsersAlias).append(".SUFFIX AS suffix ")
                .append(" FROM ").append(tblHousehold).append(" ").append(tblHouseholdAlias)
                .append(" INNER JOIN ")
                .append(tblUsers).append(" ").append(tblUsersAlias).append(" ON ")
                .append(tblHouseholdAlias).append(".ID = ").append(tblUsersAlias).append(".HOUSEHOLD_KEY ")
                .append(" WHERE ")
                .append(tblHouseholdAlias).append(".STATUS = :status AND ")
                .append(tblUsersAlias).append(".IS_HOUSEHOLD_HEAD = :isHouseholdHead AND ")
                .append(tblUsersAlias).append(".BLOCK = :block AND ")
                .append(tblUsersAlias).append(".LOT = :lot AND ")
                .append(tblUsersAlias).append(".PHASE_KEY = :phaseKey ")
                ;

        return namedParameterJdbcTemplate.query(qry.toString(), map, new BeanPropertyRowMapper<>(HouseholdModel.class));
    }

}
