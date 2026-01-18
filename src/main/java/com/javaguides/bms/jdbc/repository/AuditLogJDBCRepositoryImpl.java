package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.AuditLogs;
import com.javaguides.bms.helper.DbTableUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements AuditLogJDBCRepository {

    private static final String tblAudit = DbTableUtil.getTableName(AuditLogs.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuditLogJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveAuditLog(AuditLogs auditObj) {
        return save(auditObj);
    }

}
