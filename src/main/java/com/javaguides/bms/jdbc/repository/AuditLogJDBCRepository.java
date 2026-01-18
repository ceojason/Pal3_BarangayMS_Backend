package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.AuditLogs;

public interface AuditLogJDBCRepository {
    int saveAuditLog(AuditLogs auditObj);
}
