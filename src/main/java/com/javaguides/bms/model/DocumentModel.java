package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.DocumentTypeEnum;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_document_request")
@TableAlias("tdr")
public class DocumentModel extends BaseModel {

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "DOCUMENT_TYPE")
    private Integer documentType;

    @Column(name = "PURPOSE")
    private String purpose;

    @Column(name = "DATE_REQUESTED")
    private Date dateRequested;

    @Column(name = "HEADER")
    private String header;

    @Column(name = "BODY")
    private String body;

    @Column(name = "FOOTER")
    private String footer;

    @Column(name = "DATE_PROCESSED")
    private Date dateProcessed;

    @Transient
    private String documentTypeString;

    @Transient
    private String firstNm;

    @Transient
    private String middleNm;

    @Transient
    private String lastNm;

    @Transient
    private String suffix;

    public String fileNmString() {
        return new StringBuilder()
            .append(lastNm!=null ? lastNm : "")
            .append("_")
            .append(documentTypeString!=null ? documentTypeString : DocumentTypeEnum.getDescByKey(documentType)).toString();
    }

    public String getFullNm() {
        StringBuilder fullNm = new StringBuilder();
        if (lastNm!=null) {
            fullNm.append(lastNm);
        }
        if (firstNm!=null) {
            fullNm.append(", ")
                    .append(firstNm);
        }
        if (middleNm!=null) {
            fullNm.append(", ")
                    .append(middleNm);
        }
        if (suffix!=null) {
            fullNm.append(" ")
                    .append(suffix);
        }
        return fullNm.toString();
    }

    public DocumentModel(DocumentRequest documentRequest) {
        if (documentRequest != null) {
            setId(documentRequest.getId());
            setRefNo(documentRequest.getRefNo());
            setUserId(documentRequest.getUserId());
            setPurpose(documentRequest.getPurpose());
            setDocumentType(documentRequest.getDocumentType());
            setDateRequested(documentRequest.getDateRequested());
            setHeader(documentRequest.getHeader());
            setBody(documentRequest.getBody());
            setFooter(documentRequest.getFooter());
            setStatus(documentRequest.getStatus());
        }
    }
}
