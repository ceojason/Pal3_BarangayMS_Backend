package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SystemStatusEnum {

    INACTIVE(0, "Inactive", false),
    ACTIVE(1, "Active", true),
    UNDER_GRADUATE(2, "Undergraduate", true),
    GRADUATE(3, "Graduate", true),
    LOGGED_IN(4, "Logged in", true),
    LOGGED_OUT(5, "Logged Out", true),
    SENT(6, "Sent successfully", true),
    NOT_SENT(7, "Sent error", true),
    PENDING(8, "Pending", true),
    PROCESSED(9, "Processed", true),
    REJECTED(10, "Rejected", true),
    IN_PROGRESS(11, "In Progress", true),
    CLOSED(12, "Closed", true)
    ;

    private final Integer key;
    private final String dscp;
    private final Boolean hasAccessToSystem;

    SystemStatusEnum(Integer key, String dscp, Boolean hasAccessToSystem) {
        this.key = key;
        this.dscp = dscp;
        this.hasAccessToSystem = hasAccessToSystem;
    }

    public static List<KeyValueModel> getStatusListForCommReport() {
        List<KeyValueModel> list = new ArrayList<>();
        list.add(new KeyValueModel(IN_PROGRESS.key, IN_PROGRESS.dscp));
        list.add(new KeyValueModel(CLOSED.key, CLOSED.dscp));
        return list;
    }

    public static List<KeyValueModel> getStatusListForHousehold() {
        List<KeyValueModel> list = new ArrayList<>();
        list.add(new KeyValueModel(ACTIVE.getKey(), ACTIVE.getDscp()));
        list.add(new KeyValueModel(INACTIVE.getKey(), INACTIVE.getDscp()));
        return list;
    }

    public static String getDscpByKey(Integer key) {
        if (key!=null) {
            for (SystemStatusEnum systemStatusEnum : SystemStatusEnum.values()) {
                if (systemStatusEnum.getKey().equals(key)) {
                    return systemStatusEnum.dscp;
                }
            }
        }
        return null;
    }

}
