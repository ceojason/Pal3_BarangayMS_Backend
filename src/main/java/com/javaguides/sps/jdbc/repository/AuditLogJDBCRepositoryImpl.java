package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.sps.model.AuditLogs;
import com.javaguides.sps.helper.DbTableUtil;
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
