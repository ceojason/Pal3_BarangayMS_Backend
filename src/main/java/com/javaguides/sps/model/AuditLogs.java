package com.javaguides.sps.model;

import com.javaguides.sps.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_audit_logs")
public class AuditLogs extends BaseModel {

    @Column(name = "REFNO")
    private String refNo;

    @Column(name = "TXN_DSCP")
    private String txnDscp;

    @Column(name = "CLOB")
    private String auditDtls;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DATE_PROCESSED")
    private String dateProcessed;

}
