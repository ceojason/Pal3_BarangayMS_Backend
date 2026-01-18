package com.javaguides.bms.enums;

import lombok.Getter;

@Getter
public enum SystemStatusEnum {

    INACTIVE(0, "Inactive", false),
    ACTIVE(1, "Active", true),
    UNDER_GRADUATE(2, "Undergraduate", true),
    GRADUATE(3, "Graduate", true),
    LOGGED_IN(4, "Logged in", true),
    LOGGED_OUT(5, "Logged Out", true),
    ;

    private final Integer key;
    private final String dscp;
    private final Boolean hasAccessToSystem;

    SystemStatusEnum(Integer key, String dscp, Boolean hasAccessToSystem) {
        this.key = key;
        this.dscp = dscp;
        this.hasAccessToSystem = hasAccessToSystem;
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
