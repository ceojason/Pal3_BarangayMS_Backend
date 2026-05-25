package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum DocumentSubCatEnum {

    // CLEARANCES
    BARANGAY_CLEARANCE(1, 1, "Barangay Clearance"),
    BUSINESS_CLEARANCE(2, 1, "Business Clearance"),
    WORKING_PERMIT_CLEARANCE(3, 1, "Working Permit Clearance"),

    // CERTIFICATIONS
    CERTIFICATE_OF_RESIDENCY(4, 2, "Certificate of Residency"),
    CERTIFICATE_OF_INDIGENCY(5, 2, "Certificate of Indigency"),
    CERTIFICATE_OF_HOUSEHOLD(6, 2, "Certificate of Household Membership"),
    CERTIFICATE_OF_COHABITATION(7, 2, "Certificate of Cohabitation"),
    SOLO_PARENT_CERTIFICATE(8, 2, "Solo Parent Certificate"),
    VOTERS_CERTIFICATE(9, 2, "Voter's Registration Certificate"),
    SENIOR_CITIZEN_CERTIFICATE(10, 2, "Senior Citizen Verification"),
    PWD_CERTIFICATE(11, 2, "PWD Verification Certificate"),
    FIRST_TIME_JOB_SEEKER(12, 2, "First Time Job Seeker Certificate"),

    // PERMITS
    EVENT_PERMIT(13, 3, "Barangay Event Permit"),
    PUBLIC_GATHERING_PERMIT(14, 3, "Public Gathering Permit"),
    NOISE_PERMIT(15, 3, "Noise Permit"),
    CURFEW_EXEMPTION(16, 3, "Curfew Exemption"),

    // BUSINESS DOCUMENTS
    BUSINESS_PERMIT_ENDORSEMENT(17, 4, "Business Permit Endorsement"),
    VENDOR_CLEARANCE(18, 4, "Vendor Clearance"),
    TRICYCLE_CLEARANCE(19, 4, "Tricycle Clearance"),

    // PROPERTY DOCUMENTS
    FENCE_CLEARANCE(20, 5, "Fence Clearance"),
    BUILDING_CLEARANCE(21, 5, "Building Clearance"),
    RENOVATION_CLEARANCE(22, 5, "Renovation Clearance"),
    ELECTRICAL_PERMIT_ENDORSEMENT(23, 5, "Electrical Permit Endorsement"),
    OCCUPANCY_CERTIFICATION(24, 5, "Occupancy Certification"),
    PROPERTY_OCCUPANCY(25, 5, "Property Occupancy Certification"),


    BRGY_ID(26, 6, "Barangay Identification Card")
    ;

    private final Integer key;
    private final Integer categoryKey;
    private final String value;

    DocumentSubCatEnum(Integer key, Integer categoryKey, String value) {
        this.key = key;
        this.categoryKey = categoryKey;
        this.value = value;
    }

    public static List<KeyValueModel> getDocuSubCatList(Integer key) {
        List<KeyValueModel> list = new ArrayList<>();

        if (key != null) {
            for (DocumentSubCatEnum val : values()) {
                if (val.getCategoryKey().equals(key)) {
                    list.add(new KeyValueModel(val.getKey(), val.getValue()));
                }
            }
            list.sort(Comparator.comparing(KeyValueModel::getValue));
        }

        return list;
    }

    public static Integer getCategoryByKey(Integer key) {
        if (key!=null) {
            for (DocumentSubCatEnum val : values()) {
                if (key.equals(val.getKey())) return val.getCategoryKey();
            }
        }
        return null;
    }

    public static String getDocuSubCatDescByKey(Integer key) {
        if (key!=null) {
            for (DocumentSubCatEnum val : values()) {
                if (key.equals(val.getKey())) return val.getValue();
            }
        }
        return "";
    }
}