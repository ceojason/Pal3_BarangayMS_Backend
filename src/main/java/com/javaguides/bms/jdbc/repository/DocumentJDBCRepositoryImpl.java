package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.DocumentModel;
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
import java.util.Optional;

@Repository
public class DocumentJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements DocumentJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DocumentJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    static final String tblName = DbTableUtil.getTableName(DocumentModel.class);
    static final String tblNameAlias = DbTableUtil.getTableAlias(DocumentModel.class);
    static final String tblUser = DbTableUtil.getTableName(UsersModel.class);
    static final String tblUserAlias = DbTableUtil.getTableAlias(UsersModel.class);


    @Override
    public int saveRequest(DocumentModel modelObj) {
        return save(modelObj);
    }

    @Override
    public int updateDocument(DocumentModel modelObj) { return update(modelObj); }

    @Override
    public Integer getCount() {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("today", new java.sql.Date(System.currentTimeMillis()));
        map.addValue("status", SystemStatusEnum.REJECTED.getKey());
        String sql = "SELECT COUNT(ID) FROM " + tblName + " WHERE STATUS != :status ";
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public Optional<DocumentModel> findById(String id) {
        return super.findById(id, tblName, DocumentModel.class);
    }



    @Override
    public List<DocumentModel> findPendingRequestByUserIdAndKey(String userId, Integer key) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("documentType", key);

        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause2(DocumentModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(DocumentModel.class))
                .append(" WHERE ").append(tblNameAlias).append(".USER_ID = :userId ")
                .append(" AND ").append(tblNameAlias).append(".DOCUMENT_TYPE = :documentType ")
                .append(" ORDER BY ").append(tblNameAlias).append(".DATE_REQUESTED DESC");

        return namedParameterJdbcTemplate.query(query.toString(), map, new BeanPropertyRowMapper<>(DocumentModel.class));
    }

    @Override
    public Page<DocumentModel> searchRequests(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<DocumentModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, DocumentModel.class));

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
                .append(DbTableUtil.getTableNameWithAlias(DocumentModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ").append(tblNameAlias).append(".USER_ID = ").append(tblUserAlias).append(".ID ")
                .append(whereClause);
        return query.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(DocumentModel.class)).append(", ")
                .append(DbTableUtil.buildSelectClause(UsersModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(DocumentModel.class))
                .append(" LEFT JOIN ").append(DbTableUtil.getTableNameWithAlias(UsersModel.class))
                .append(" ON ")
                .append(tblNameAlias).append(".USER_ID = ")
                .append(tblUserAlias).append(".ID ")
                .append(whereClause);
        return query.toString();
    }

    public List<DocumentModel> mapResultToModel(List<Map<String, Object>> list) {
        List<DocumentModel> requestList = new ArrayList<>();
        for (Map<String, Object> row : list) {
            DocumentModel document = new DocumentModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("tdr_")) {
                    String fieldName = key.substring(4);
                    try {
                        Field field = getField(DocumentModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(document, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (key.startsWith("tu_")) {
                    String fieldName = key.substring(3);
                    if (fieldName.equals("userId")) {
                        document.setUserId((String) row.get(key));
                    }
                    if (fieldName.equals("firstNm")) {
                        document.setFirstNm((String) row.get(key));
                    }
                    if (fieldName.equals("middleNm")) {
                        document.setMiddleNm((String) row.get(key));
                    }
                    if (fieldName.equals("lastNm")) {
                        document.setLastNm((String) row.get(key));
                    }
                    if (fieldName.equals("suffix")) {
                        document.setSuffix((String) row.get(key));
                    }
                }
            }
            requestList.add(document);
        }

        return requestList;
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
            conditions.add(tblNameAlias + ".REF_NO = :refNo");
        }

        if (request.getIsPending()!=null && request.getIsPending().equals(YesOrNoEnum.YES.getBooleanVal())) {
            map.addValue("status", SystemStatusEnum.REJECTED.getKey());
            conditions.add(tblNameAlias + ".STATUS != :status");
        }else{
            map.addValue("status", SystemStatusEnum.REJECTED.getKey());
            conditions.add(tblNameAlias + ".STATUS = :status");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }
}
