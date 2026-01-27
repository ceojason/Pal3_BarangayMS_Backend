package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AnnouncementJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements AnnouncementJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String tblNm = DbTableUtil.getTableName(AnnouncementModel.class);
    static final String tblAlias = DbTableUtil.getTableAlias(AnnouncementModel.class);
    static final String tblUsers = DbTableUtil.getTableName(UsersModel.class);
    static final String tblUsersAlias = DbTableUtil.getTableAlias(UsersModel.class);

    public AnnouncementJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveRequest(AnnouncementModel modelObj) {
        return save(modelObj);
    }

    @Override
    public Integer getCount() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("today", new java.sql.Date(System.currentTimeMillis()));
        String sql = "SELECT COUNT(ID) FROM " + tblNm + " WHERE DATE(created_dt) = :today";
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public List<AnnouncementModel> findAnnouncementByUserId(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("today", new java.sql.Date(System.currentTimeMillis()));
        map.addValue("userId", userId);

        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause2(AnnouncementModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(AnnouncementModel.class))
                .append(" WHERE ").append(tblAlias).append(".USER_ID = :userId ")
                .append(" AND DATE(created_dt) = :today ")
                .append(" ORDER BY ").append(tblAlias).append(".CREATED_DT DESC");

        return namedParameterJdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(AnnouncementModel.class));
    }

    @Override
    public List<AnnouncementModel> findAnnouncementByUserIdGrouped(String userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);

        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause2(AnnouncementModel.class))
                .append(", DATE(").append(tblAlias).append(".CREATED_DT) AS CREATED_DATE ") // optional
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(AnnouncementModel.class))
                .append(" WHERE ").append(tblAlias).append(".USER_ID = :userId ")
                .append(" ORDER BY DATE(").append(tblAlias).append(".CREATED_DT) DESC, ")
                .append(tblAlias).append(".CREATED_DT DESC");

        return namedParameterJdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(AnnouncementModel.class));
    }


    @Override
    public Page<AnnouncementModel> searchAnnouncement(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<AnnouncementModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, AnnouncementModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToAnnouncementModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    private StringBuilder createWhereClause(MainSearchRequest request, MapSqlParameterSource map) {
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        if (request.getRefNo() != null && !request.getRefNo().isEmpty()) {
            map.addValue("refNo", request.getRefNo().trim().toUpperCase());
            conditions.add(tblAlias + ".REF_NO = :refNo");
        }

        if (request.getFirstNm() != null && !request.getFirstNm().isEmpty()) {
            map.addValue("firstNm", request.getFirstNm().trim().toUpperCase());
            conditions.add(tblUsersAlias + ".FIRST_NM = :firstNm");
        }

        if (request.getLastNm() != null && !request.getLastNm().isEmpty()) {
            map.addValue("lastNm", request.getLastNm().trim().toUpperCase());
            conditions.add(tblUsersAlias + ".LAST_NM = :lastNm");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder sql = new StringBuilder()
                .append( " SELECT ").append(count())
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(AnnouncementModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ")
                .append(tblAlias).append(".USER_ID = ")
                .append(tblUsersAlias).append(".ID")
                .append(whereClause);
        return sql.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(AnnouncementModel.class)).append(", ")
                .append(DbTableUtil.buildSelectClause(UsersModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(AnnouncementModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ")
                .append(tblAlias).append(".USER_ID = ")
                .append(tblUsersAlias).append(".ID")
                .append(whereClause);
        return query.toString();
    }

    public List<AnnouncementModel> mapResultToAnnouncementModel(List<Map<String, Object>> list) {
        List<AnnouncementModel> announcementModelList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            AnnouncementModel announcement = new AnnouncementModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("ta_")) {
                    String fieldName = key.substring(3);
                    try {
                        Field field = getField(AnnouncementModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(announcement, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tu_")) {
                    String fieldName = key.substring(3);
                    if (fieldName.equals("firstNm")) {
                        announcement.setFirstNm((String) row.get(key));
                    }
                    if (fieldName.equals("middleNm")) {
                        announcement.setMiddleNm((String) row.get(key));
                    }
                    if (fieldName.equals("lastNm")) {
                        announcement.setLastNm((String) row.get(key));
                    }
                    if (fieldName.equals("suffix")) {
                        announcement.setSuffix((String) row.get(key));
                    }
                }
            }
            announcementModelList.add(announcement);
        }

        return announcementModelList;
    }

}
