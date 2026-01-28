package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.security.Key;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum DocumentTypeEnum {
    BARANGAY_CLEARANCE(0, "Barangay Clearance",
            "templates/documents/barangay_clearance.docx",
            "BARANGAY CLEARANCE",
            "This is to certify that ${RESIDENT_NAME}, residing at ${RESIDENT_ADDRESS}, has been a resident of this barangay since ${RESIDENCY_DATE} and is known to be of good moral character.\n\n\nPurpose: ${PURPOSE}\n\nIssued this ${DATE}.",
            "__________________________________\nPunong Barangay"
    ),
    BUSINESS_CLEARANCE(1, "Business Clearance",
            "templates/documents/business_clearance.docx",
            "Business Clearance",
            "This certifies that ${RESIDENT_NAME}, operating at ${RESIDENT_ADDRESS}, has complied with all barangay ordinances and is authorized to operate business in Barangay ${BARANGAY_NAME}, ${CITY}.\n\nPurpose: ${PURPOSE}\n\nIssued this ${DATE}.",
            "__________________________________\nBarangay Business Officer"
    ),
    CERT_OF_INDIGENCY(2, "Certificate of Indigency",
            "templates/documents/certificate_of_indigency.docx",
            "CERTIFICATE OF INDIGENCY",
            "This is to certify that ${RESIDENT_NAME}, residing at ${RESIDENT_ADDRESS}, is financially indigent and belongs to the low-income sector of Barangay ${BARANGAY_NAME}, ${CITY}.\n\nPurpose: ${PURPOSE}\n\nIssued this ${DATE}.",
            "__________________________________\nPunong Barangay"
    ),
    CERT_OF_RESIDENCY(3, "Certificate of Residency",
            "templates/documents/certificate_of_residency.docx",
            "CERTIFICATE OF RESIDENCY",
            "This certifies that ${RESIDENT_NAME}, residing at ${RESIDENT_ADDRESS}, has been residing in Barangay ${BARANGAY_NAME}, ${CITY} since ${RESIDENCY_DATE}.\n\nPurpose: ${PURPOSE}\n\nIssued this ${DATE}.",
            "__________________________________\nBarangay Secretary"
    ),
    BARANGAY_ID(4, "Barangay ID", "", "", "", "");

    private final Integer key;
    private final String desc;
    private final String templatePath;
    private final String headerTemplate;
    private final String bodyTemplate;
    private final String footerTemplate;

    DocumentTypeEnum(Integer key, String desc, String templatePath, String headerTemplate, String bodyTemplate, String footerTemplate) {
        this.key = key;
        this.desc = desc;
        this.templatePath = templatePath;
        this.headerTemplate = headerTemplate;
        this.bodyTemplate = bodyTemplate;
        this.footerTemplate = footerTemplate;
    }

    public static DocumentTypeEnum getByKey(Integer key) {
        if (key != null) {
            for (DocumentTypeEnum val : values()) {
                if (val.getKey().equals(key)) return val;
            }
        }
        return null;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (DocumentTypeEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc();
            }
        }
        return "";
    }

    public static String getTemplateByKey(Integer key) {
        for (DocumentTypeEnum e : values()) {
            if (e.key.equals(key)) return e.templatePath;
        }
        return null;
    }

    public static List<KeyValueModel> getDocumentList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (DocumentTypeEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getValue ));
        return list;
    }
}
