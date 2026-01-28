package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.helper.GenericRowMapper;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class NotifLogsJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements NotifLogsJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String tblNotifLogs = DbTableUtil.getTableName(NotifLogsModel.class);
    static final String tblNotifLogsAlias = DbTableUtil.getTableAlias(NotifLogsModel.class);

    public NotifLogsJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveNotifLogs(NotifLogsModel model) {
        return save(model);
    }

    @Override
    public int saveBatch(List<NotifLogsModel> list) {
        return batchSave(list);
    }

    @Override
    public Page<NotifLogsModel> searchNotifLogs(MainSearchRequest requestObj, PageRequest page) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        List<NotifLogsModel> list = new ArrayList<>();
        StringBuilder whereClause = createWhereClause(requestObj, map);
        whereClause.append(getOrderBy(page, NotifLogsModel.class));

        Integer count = namedParameterJdbcTemplate.queryForObject(countQry(whereClause), map, Integer.class);
        if (count!=null && count>0) {
            list = mapResultToNotifLogsModel(namedParameterJdbcTemplate.query(selectQry(whereClause), map, new GenericRowMapper()));
        }else{
            count=0;
        }
        return new PageImpl<>(list, page, count);
    }

    private String countQry(StringBuilder whereClause) {
        StringBuilder sql = new StringBuilder()
                .append( " SELECT ").append(count()).append(" FROM ").append(DbTableUtil.getTableNameWithAlias(NotifLogsModel.class)).append(whereClause);
        return sql.toString();
    }

    private String selectQry(StringBuilder whereClause) {
        StringBuilder query = new StringBuilder()
                .append(" SELECT ").append(DbTableUtil.buildSelectClause(NotifLogsModel.class))
                .append(" FROM ").append(DbTableUtil.getTableNameWithAlias(NotifLogsModel.class))
                .append(whereClause);
        return query.toString();
    }

    private StringBuilder createWhereClause(MainSearchRequest request, MapSqlParameterSource map) {
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        if (request.getRecipient()!=null && !request.getRecipient().isEmpty()) {
            map.addValue("recipient", "%" + request.getRecipient().trim().toUpperCase() + "%");
            conditions.add(tblNotifLogsAlias + ".RECIPIENT LIKE :recipient");
        }

        if (request.getRefNo()!=null && !request.getRefNo().isEmpty()) {
            map.addValue("refNo", request.getRefNo().trim());
            conditions.add(tblNotifLogsAlias + ".REF_NO = :refNo");
        }

        if (!conditions.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        return where;
    }

    public List<NotifLogsModel> mapResultToNotifLogsModel(List<Map<String, Object>> list) {
        List<NotifLogsModel> notifLogs = new ArrayList<>();
        for (Map<String, Object> row : list) {
            NotifLogsModel users = new NotifLogsModel();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("tal_")) {
                    String fieldName = key.substring(4);
                    try {
                        Field field = getField(NotifLogsModel.class, fieldName);
                        field.setAccessible(true);
                        field.set(users, entry.getValue());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            notifLogs.add(users);
        }

        return notifLogs;
    }

}
