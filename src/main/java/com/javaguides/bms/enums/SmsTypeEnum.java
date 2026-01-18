package com.javaguides.bms.enums;

import lombok.Getter;

@Getter
public enum SmsTypeEnum {

    NEW_USER_SMS(0, "User Registration"),
    ANNOUNCEMENT_SMS(1, "Announcement"),
    ALARM_WARNING_SMS(2, "Alarm/Warning")
    ;
    private final Integer key;
    private final String desc;

    SmsTypeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
