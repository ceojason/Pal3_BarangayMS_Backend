package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;

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
    public int update(HouseholdModel modelObj) {
        modelObj.setUpdatedDt(new Date());
        return super.update(modelObj);
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
    public List<HouseholdModel> findDuplicateHouseholdList(String uniqKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("uniqKey", uniqKey);

        StringBuilder sql = new StringBuilder()
                .append(" SELECT * ").append(" FROM ").append(tblHousehold)
                .append(" WHERE HOUSEHOLD_UNIQ_KEY = :uniqKey ");

        return namedParameterJdbcTemplate.query(sql.toString(), map, new BeanPropertyRowMapper<>(HouseholdModel.class));
    }

    @Override
    public Page<HouseholdModel> search(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<HouseholdModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, HouseholdModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToHouseholdModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    public List<HouseholdModel> mapResultToHouseholdModel(List<Map<String, Object>> list) {
        List<HouseholdModel> requestList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            HouseholdModel household = new HouseholdModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("th_")) {
                    String fieldName = key.substring(3);
                    try {
                        Field field = getField(HouseholdModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(household, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tu_")) {
                    String fieldName = key.substring(3);
                    if (fieldName.equals("id")) {
                        household.setUserId((String) row.get(key));
                    }
                    if (fieldName.equals("firstNm")) {
                        household.setFirstNm((String) row.get(key));
                    }
                    if (fieldName.equals("middleNm")) {
                        household.setMiddleNm((String) row.get(key));
                    }
                    if (fieldName.equals("lastNm")) {
                        household.setLastNm((String) row.get(key));
                    }
                    if (fieldName.equals("suffix")) {
                        household.setSuffix((String) row.get(key));
                    }
                    if (fieldName.equals("block")) {
                        household.setBlock((String) row.get(key));
                    }
                    if (fieldName.equals("lot")) {
                        household.setLot((String) row.get(key));
                    }
                    if (fieldName.equals("street")) {
                        household.setStreet((String) row.get(key));
                    }
                    if (fieldName.equals("phaseKey")) {
                        household.setPhaseKey((Integer) row.get(key));
                    }
                    if (fieldName.equals("status")) {
                        household.setUserStatus((Integer) row.get(key));
                    }
                }
            }
            requestList.add(household);
        }

        return requestList;
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append( " SELECT ").append(count()).append(" FROM ")
                .append(DbTableUtil.getTableNameWithAlias(HouseholdModel.class))
                .append(whereClause);
        return query.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ")
                .append(DbTableUtil.buildSelectClause(HouseholdModel.class))
                .append(" FROM ")
                .append(DbTableUtil.getTableNameWithAlias(HouseholdModel.class))
                .append(whereClause);

        return query.toString();
    }

    private StringBuilder createWhereClause(MainSearchRequest request, MapSqlParameterSource map) {
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        if (request.getRequestor() != null && !request.getRequestor().isEmpty()) {
            String recipientParam = "%" + request.getRequestor().trim().toUpperCase() + "%";
            map.addValue("recipient", recipientParam);

            conditions.add("("
                    + tblUsersAlias + ".FIRST_NM LIKE :recipient OR "
                    + tblUsersAlias + ".MIDDLE_NM LIKE :recipient OR "
                    + tblUsersAlias + ".LAST_NM LIKE :recipient"
                    + ")");
        }

        if (request.getRefNo()!=null && !request.getRefNo().isEmpty()) {
            map.addValue("refNo", request.getRefNo().trim());
            conditions.add(tblHouseholdAlias + ".REF_NO = :refNo");
        }

        if (request.getUserId()!=null) {
            map.addValue("userId", request.getUserId());
            conditions.add(tblHouseholdAlias + ".USER_ID = :userId");
        }else{
//            map.addValue("isHouseholdHead", YesOrNoEnum.YES.getKey());
//            conditions.add(tblUsersAlias + ".IS_HOUSEHOLD_HEAD =:isHouseholdHead ");
//            if (request.getIsPending()!=null && request.getIsPending().equals(YesOrNoEnum.YES.getBooleanVal())) {
//                map.addValue("status", SystemStatusEnum.REJECTED.getKey());
//                conditions.add(tblHouseholdAlias + ".STATUS != :status");
//            }else{
//                map.addValue("status", SystemStatusEnum.REJECTED.getKey());
//                conditions.add(tblHouseholdAlias + ".STATUS = :status");
//            }
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
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

    @Override
    public List<HouseholdModel> findHousehold(Integer status, String block, String lot, Integer phaseKey) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("status", status);
        map.addValue("block", block);
        map.addValue("lot", lot);
        map.addValue("phaseKey", phaseKey);
        //map.addValue("isHouseholdHead", YesOrNoEnum.YES.getKey());

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
                //.append(tblHouseholdAlias).append(".STATUS = :status AND ")
                //.append(tblUsersAlias).append(".IS_HOUSEHOLD_HEAD = :isHouseholdHead AND ")
                .append(tblUsersAlias).append(".BLOCK = :block AND ")
                .append(tblUsersAlias).append(".LOT = :lot AND ")
                .append(tblUsersAlias).append(".PHASE_KEY = :phaseKey ")
                ;

        return namedParameterJdbcTemplate.query(qry.toString(), map, new BeanPropertyRowMapper<>(HouseholdModel.class));
    }

}
