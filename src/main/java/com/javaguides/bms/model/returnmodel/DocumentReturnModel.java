package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.DocumentTypeEnum;
import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.DocumentModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class DocumentReturnModel {

    private String id;
    private String userId;
    private String purpose;
    private Integer documentType;
    private String documentTypeString;
    private Integer status;
    private String statusString;
    private String refNo;
    private Date dateRequested;
    private String dateRequestedString;
    private String ackMessage;

    private String header;
    private String body;
    private String footer;

    public DocumentReturnModel(DocumentModel model) {
        this.id = model.getId();
        this.userId = model.getUserId();
        this.purpose = model.getPurpose();
        this.documentType = model.getDocumentType();
        this.documentTypeString = DocumentTypeEnum.getDescByKey(documentType);
        this.status = model.getStatus();
        this.statusString = SystemStatusEnum.getDscpByKey(status);
        this.refNo = model.getRefNo();
        this.dateRequested = model.getDateRequested();
        this.dateRequestedString = DateUtil.getDateStringWithFormat(dateRequested, DateFormatEnum.DT_FORMAT_12.getPattern());
        this.ackMessage = model.getAckMessage();

        this.header = model.getHeader();
        this.body = model.getBody();
        this.footer = model.getFooter();
    }
}
