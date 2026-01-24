package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UsersJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements UsersJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    static final String tblUsers = DbTableUtil.getTableName(UsersModel.class);
    static final String tblUsersAlias = DbTableUtil.getTableAlias(UsersModel.class);
    static final String tblLogin = DbTableUtil.getTableName(LoginCreds.class);
    static final String tblLoginAlias = DbTableUtil.getTableAlias(LoginCreds.class);

    public UsersJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveEnrollment(UsersModel modelObj) {
        return save(modelObj);
    }

    @Override
    public Integer getUsersCount() {
        String sql = "SELECT COUNT(ID) FROM " + tblUsers;
        return namedParameterJdbcTemplate.queryForObject(sql, new EmptySqlParameterSource(), Integer.class);
    }

    @Override
    public Page<UsersModel> searchUsers(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<UsersModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, UsersModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToUsersModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(UsersModel.class)).append(", ")
                .append(DbTableUtil.buildSelectClause(LoginCreds.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(LoginCreds.class))
                .append(" ON ")
                .append(tblUsersAlias).append(".ID = ")
                .append(tblLoginAlias).append(".USER_ID ")
                .append(whereClause);
        return query.toString();
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder sql = new StringBuilder()
                .append( " SELECT ").append(count()).append(" FROM ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class)).append(whereClause);
        return sql.toString();
    }

    private StringBuilder createWhereClause(MainSearchRequest request, MapSqlParameterSource map) {
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        if (request.getFirstNm() != null && !request.getFirstNm().isEmpty()) {
            map.addValue("firstNm", request.getFirstNm().trim().toUpperCase());
            conditions.add("FIRST_NM = :firstNm");
        }

        if (request.getLastNm() != null && !request.getLastNm().isEmpty()) {
            map.addValue("lastNm", request.getLastNm().trim().toUpperCase());
            conditions.add("LAST_NM = :lastNm");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }

    public List<UsersModel> mapResultToUsersModel(List<Map<String, Object>> list) {
        List<UsersModel> usersList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            UsersModel users = new UsersModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("tu_")) {
                    String fieldName = key.substring(3);
                    try {
                        Field field = getField(UsersModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(users, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tlc_")) {
                    String fieldName = key.substring(4);
                    if (fieldName.equals("userId")) {
                        users.setUserId((String) row.get(key));
                    }
                    if (fieldName.equals("cd")) {
                        users.setCd((String) row.get(key));
                    }
                }
            }
            usersList.add(users);
        }

        return usersList;
    }

    @Override
    public List<UsersModel> findAllUsersByClassificationKeys(List<Integer> keys) {

        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        String regexGroup = keys.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        String regex = "(^|\\|)(" + regexGroup + ")(\\||$)";

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("keys", regex);

        String sql = " SELECT * FROM " + DbTableUtil.getTableName(UsersModel.class) + " WHERE CLASSIFICATION_KEY REGEXP :keys";

        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(UsersModel.class));
    }

    @Override
    public Optional<UsersModel> findById(String id) {
        return super.findById(id, tblUsers, UsersModel.class);
    }
}
