package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.CommReportAttachmentModel;
import com.javaguides.bms.model.CommReportModel;
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
import java.util.*;

@Repository
public class CommReportJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements CommReportJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public CommReportJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static final String tblCommReport = DbTableUtil.getTableName(CommReportModel.class);
    static final String tblCommReportAlias = DbTableUtil.getTableAlias(CommReportModel.class);
    static final String tblCommReportAttachment = DbTableUtil.getTableName(CommReportModel.class);
    static final String tblCommReportAttachmentAlias = DbTableUtil.getTableAlias(CommReportModel.class);
    static final String tblUser = DbTableUtil.getTableName(UsersModel.class);
    static final String tblUserAlias = DbTableUtil.getTableAlias(UsersModel.class);

    @Override
    public String save(CommReportModel modelObj) {
        modelObj.setCreatedDt(new Date());
        modelObj.setUpdatedDt(null);
        super.save(modelObj);
        return modelObj.getId();
    }

    @Override
    public int saveAttachments(List<CommReportAttachmentModel> modelObj) {
        return super.batchSave(modelObj);
    }

    @Override
    public int updateReport(CommReportModel model) {
        model.setUpdatedDt(new Date());
        return super.update(model);
    }

    @Override
    public Optional<CommReportModel> findById(String id) {
        return super.findById(id, tblCommReport, CommReportModel.class);
    }

    @Override
    public List<CommReportAttachmentModel> findByMainIdList(List<String> ids) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("ids", ids);

        String sql = " SELECT * FROM " + DbTableUtil.getTableName(CommReportAttachmentModel.class)
                + " WHERE REPORT_ID IN (:ids) ";
        return namedParameterJdbcTemplate.query(sql, map, new BeanPropertyRowMapper<>(CommReportAttachmentModel.class));
    }

    @Override
    public Integer getCount() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("today", new java.sql.Date(System.currentTimeMillis()));
        map.addValue("status", List.of(SystemStatusEnum.PENDING.getKey(), SystemStatusEnum.IN_PROGRESS.getKey()));
        String sql = "SELECT COUNT(ID) FROM " + tblCommReport + " WHERE STATUS IN (:status) ";
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public Page<CommReportModel> searchRequests(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<CommReportModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, CommReportModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append( " SELECT ").append(count()).append(" FROM ")
                .append(DbTableUtil.getTableNameWithAlias(CommReportModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ").append(tblCommReportAlias).append(".USER_ID = ").append(tblUserAlias).append(".ID ")
                .append(whereClause);
        return query.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(CommReportModel.class)).append(", ")
                .append(DbTableUtil.buildSelectClause(UsersModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(CommReportModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ")
                .append(tblCommReportAlias).append(".USER_ID = ")
                .append(tblUserAlias).append(".ID ")
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
                    + tblUserAlias + ".FIRST_NM LIKE :recipient OR "
                    + tblUserAlias + ".MIDDLE_NM LIKE :recipient OR "
                    + tblUserAlias + ".LAST_NM LIKE :recipient"
                    + ")");
        }

        if (request.getRefNo()!=null && !request.getRefNo().isEmpty()) {
            map.addValue("refNo", request.getRefNo().trim());
            conditions.add(tblCommReportAlias + ".REF_NO = :refNo");
        }

        if (request.getUserId()!=null) {
            map.addValue("userId", request.getUserId());
            conditions.add(tblCommReportAlias + ".USER_ID = :userId");
        }else{
            map.addValue("status", List.of(SystemStatusEnum.PENDING.getKey(), SystemStatusEnum.IN_PROGRESS.getKey(), SystemStatusEnum.PROCESSED.getKey(), SystemStatusEnum.CLOSED.getKey()));
            conditions.add(tblCommReportAlias + ".STATUS IN (:status) ");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }

    public List<CommReportModel> mapResultToModel(List<Map<String, Object>> list) {
        List<CommReportModel> requestList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            CommReportModel commReport = new CommReportModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("tcr_")) {
                    String fieldName = key.substring(4);
                    try {
                        Field field = getField(CommReportModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(commReport, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tu_")) {
                    String fieldName = key.substring(3);
                    if (fieldName.equals("userId")) {
                        commReport.setUserId((String) row.get(key));
                    }
                    if (fieldName.equals("firstNm")) {
                        commReport.setFirstNm((String) row.get(key));
                    }
                    if (fieldName.equals("middleNm")) {
                        commReport.setMiddleNm((String) row.get(key));
                    }
                    if (fieldName.equals("lastNm")) {
                        commReport.setLastNm((String) row.get(key));
                    }
                    if (fieldName.equals("suffix")) {
                        commReport.setSuffix((String) row.get(key));
                    }
                    if (fieldName.equals("mobileNo")) {
                        commReport.setMobileNo((String) row.get(key));
                    }
                    if (fieldName.equals("emailAddress")) {
                        commReport.setEmailAddress((String) row.get(key));
                    }
                }
            }
            requestList.add(commReport);
        }

        return requestList;
    }
}
