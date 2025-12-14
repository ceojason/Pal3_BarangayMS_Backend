package com.javaguides.sps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguides.sps.jdbc.repository.AuditLogJDBCRepository;
import com.javaguides.sps.model.AuditLogs;
import com.javaguides.sps.service.baseservice.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl extends BaseServiceImpl implements AuditLogService {
    private final AuditLogJDBCRepository auditLogJDBCRepository;

    public AuditLogServiceImpl(AuditLogJDBCRepository auditLogJDBCRepository) {
        this.auditLogJDBCRepository = auditLogJDBCRepository;
    }

    @Override
    public int saveAuditLog(Object modelObj) {
        if (modelObj == null) {
            throw new IllegalArgumentException("Model object cannot be null");
        }

        try {
            // Extract required fields
            String refNo = getStringFieldValue(modelObj, "refNo");
            String txnDscp = getStringFieldValue(modelObj, "txnDscp");
            String createdBy = getStringFieldValue(modelObj, "createdBy");

            // Get timestamp now (or from model if needed)
            String dateProcessed = LocalDateTime.now().toString(); // or use a formatter

            // Optionally serialize the whole object for audit details
            String auditDtls = new ObjectMapper().writeValueAsString(modelObj);

            // Build the audit object
            AuditLogs auditLog = new AuditLogs();
            auditLog.setRefNo(refNo);
            auditLog.setTxnDscp(txnDscp);
            auditLog.setAuditDtls(auditDtls);
            auditLog.setCreatedBy(createdBy);
            auditLog.setDateProcessed(dateProcessed);

            return auditLogJDBCRepository.saveAuditLog(auditLog);

        } catch (Exception e) {
            throw new RuntimeException("Error saving audit log", e);
        }
    }

}
