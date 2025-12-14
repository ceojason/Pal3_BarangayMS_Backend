package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.AuditLogs;

public interface AuditLogJDBCRepository {
    int saveAuditLog(AuditLogs auditObj);
}
