package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.DocumentSubCatEnum;
import com.javaguides.bms.enums.DocumentTypeEnum;
import com.javaguides.bms.enums.PhaseEnum;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Column(name = "DOCU_CAT_KEY")
    private Integer docuCategoryKey;

    @Column(name = "DOCU_SUBCAT_KEY")
    private Integer docuSubCategoryKey;

    @Column(name = "PROCESS_FEE")
    private BigDecimal processFee;

    @Column(name = "PURPOSE_KEY")
    private Integer purposeKey;

    @Column(name = "OTH_PURPOSE")
    private String othPurpose;

    @Column(name = "DATE_REQUESTED")
    private Date dateRequested;

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
    private String docuCategoryKeyString;

    @Transient
    private String docuSubCategoryKeyString;

    @Transient
    private String purposeKeyString;

    @Transient
    private String processFeeString;

    @Transient
    private String suffix;

    @Transient
    private String block;

    @Transient
    private String lot;

    @Transient
    private String street;

    @Transient
    private String requestor;

    @Transient
    private Date birthDt;

    @Transient
    private String gender;

    @Transient
    private Integer civilStatusKey;

    @Transient
    private String mobileNo;

    @Transient
    private Integer phaseKey;

    @Transient
    private String birthDtString;

    @Transient
    private String genderString;

    @Transient
    private String civilStatusString;

    @Transient
    private String addressFromConfig;


    public String getHomeAddress() {
        String defaultBrgyNmCityAndProv = "PALIPARAN III DASMARIÑAS CITY, CAVITE";
        String defaultZipCd = "4114";
        boolean appendDefaultAddressString = false;

        StringBuilder address = new StringBuilder();
        if (block!=null) address.append(block).append(" ");
        if (lot!=null) address.append(lot).append(" ");
        if (street!=null) address.append(street).append(" ");
        if (phaseKey!=null) address.append(PhaseEnum.getDesc2ByKey(phaseKey)).append(" ");

        if (appendDefaultAddressString) {
            address.append(defaultBrgyNmCityAndProv).append(" ");
            address.append(defaultZipCd);
        }else{
            address.append(addressFromConfig!=null ? addressFromConfig : "");
        }

        return address.toString();
    }

    public String fileNmString() {
        return new StringBuilder()
            .append(lastNm!=null ? lastNm : "")
            .append("_")
            .append(docuSubCategoryKey!=null ? DocumentSubCatEnum.getDocuSubCatDescByKey(docuSubCategoryKey) : "PAL3_File").toString();
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
            setDocuCategoryKey(documentRequest.getDocuCategoryKey());
            setDocuSubCategoryKey(documentRequest.getDocuSubCategoryKey());
            setProcessFee(documentRequest.getProcessFee());
            setPurposeKey(documentRequest.getPurposeKey());
            setOthPurpose(documentRequest.getOthPurpose());
            setDateRequested(documentRequest.getDateRequested());
            setStatus(documentRequest.getStatus());
        }
    }
}
