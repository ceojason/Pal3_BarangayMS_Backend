package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum BrgyPositionEnum {

    // Elected Officials
    PUNONG_BARANGAY(1, "Punong Barangay"),
    BARANGAY_KAGAWAD(2, "Barangay Kagawad"),
    SK_CHAIRPERSON(3, "SK Chairperson"),

    // Appointed Officials
    BARANGAY_SECRETARY(4, "Barangay Secretary"),
    BARANGAY_TREASURER(5, "Barangay Treasurer"),

    // Peace & Order
    BARANGAY_TANOD(6, "Barangay Tanod"),
    LUPON_MEMBER(7, "Lupon Member"),
    LUPON_SECRETARY(8, "Lupon Secretary"),

    // Health & Social Services
    BARANGAY_HEALTH_WORKER(9, "Barangay Health Worker"),
    BARANGAY_NUTRITION_SCHOLAR(10, "Barangay Nutrition Scholar"),
    DAY_CARE_WORKER(11, "Day Care Worker"),
    SOCIAL_WELFARE_VOLUNTEER(12, "Social Welfare Volunteer"),

    // Administrative Staff
    BARANGAY_CLERK(13, "Barangay Clerk"),
    ADMIN_AIDE(14, "Administrative Aide"),
    ENCODER_IT_PERSONNEL(15, "Encoder / IT Personnel"),
    UTILITY_WORKER(16, "Utility Worker"),

    // Disaster & Safety
    BDRRMC_MEMBER(17, "BDRRMC Member"),
    EMERGENCY_RESPONSE_TEAM(18, "Emergency Response Team Member");

    private final int id;
    private final String description;

    BrgyPositionEnum(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static String getDescByKey(Integer id) {
        if (id!=null) {
            for (BrgyPositionEnum position : values()) {
                if (position.id == id) {
                    return position.getDescription();
                }
            }
        }
        return "";
    }

    public static List<KeyValueModel> getBrgyPositionList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (BrgyPositionEnum val : values()) {
            list.add(new KeyValueModel(val.getId(), val.getDescription()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getValue));
        return list;
    }
}