package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.NumberFormatterUtil;
import com.javaguides.bms.model.DocumentModel;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class DocumentReturnModel {

    private String id;
    private String userId;

    private Integer docuCategoryKey;
    private Integer docuSubCategoryKey;
    private BigDecimal processFee;
    private Integer purposeKey;
    private String othPurpose;

    private String docuCategoryKeyString;
    private String docuSubCategoryKeyString;
    private String purposeKeyString;
    private String processFeeString;

    private Integer status;
    private String statusString;
    private String refNo;
    private Date dateRequested;
    private String dateRequestedString;
    private String ackMessage;
    private Date dateProcessed;
    private String dateProcessedString;
    private String homeAddress;

    private String requestor;
    private String fileNm;

    private String birthDtString;
    private String genderString;
    private String civilStatusString;
    private String mobileNo;

    private String fullName;

    public DocumentReturnModel(DocumentModel model) {
        this.id = model.getId();
        this.fullName = model.getRequestor();
        this.userId = model.getUserId();
        this.docuCategoryKey = model.getDocuCategoryKey();
        this.docuSubCategoryKey = model.getDocuSubCategoryKey();
        this.processFee = model.getProcessFee();
        this.purposeKey = model.getPurposeKey();
        this.othPurpose = model.getOthPurpose();
        this.docuCategoryKeyString = DocumentCategoryEnum.getDocuCatDescByKey(docuCategoryKey);
        this.docuSubCategoryKeyString = DocumentSubCatEnum.getDocuSubCatDescByKey(docuSubCategoryKey);
        this.purposeKeyString = DocumentPurposeEnum.getDocuPurposeDescByKey(purposeKey);
        this.processFeeString = NumberFormatterUtil.format(processFee);
        this.status = model.getStatus();
        this.statusString = SystemStatusEnum.getDscpByKey(status);
        this.refNo = model.getRefNo();
        this.homeAddress = model.getHomeAddress();
        this.dateRequested = model.getDateRequested();
        this.dateRequestedString = DateUtil.getDateStringWithFormat(dateRequested, DateFormatEnum.DT_FORMAT_5.getPattern() + " " + DateFormatEnum.DT_FORMAT_14.getPattern());
        this.dateProcessed = model.getDateProcessed();
        this.dateProcessedString = DateUtil.getDateStringWithFormat(dateProcessed, DateFormatEnum.DT_FORMAT_5.getPattern() + " " + DateFormatEnum.DT_FORMAT_14.getPattern());
        this.ackMessage = model.getAckMessage();

        this.birthDtString = DateUtil.getDateStringWithFormat(model.getBirthDt(), DateFormatEnum.DT_FORMAT_1.getPattern());
        this.genderString = GenderEnum.getGenderDscpFromKeyStr(model.getGender());
        this.civilStatusString = CivilStatusEnum.getCivilStatusDescByKey(model.getCivilStatusKey());
        this.mobileNo = model.getMobileNo();

        this.requestor = model.getFullNm();
        this.fileNm = model.fileNmString();
    }
}
