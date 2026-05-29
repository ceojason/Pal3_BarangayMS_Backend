package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum LogsTypeEnum {

    NEW_USER_SMS(0, "User Registration", "Resident - Registration", null),
    ANNOUNCEMENT_SMS(1, "Announcement", "Announcement - Sent", null),
    ALARM_WARNING_SMS(2, "Alarm/Warning", null, null),
    RESET_USER(3, "Reset User", "Reset User", "Reset User - Successful"),
    DOCUMENT_REQUEST(4, "Document Request", "New Document Request", null),
    COMM_REPORT(5, "Community Report", "New Community Report", "Community Report - Processed"),
    MY_PROFILE_UPDATE(6, "My Profile Update", null, null),
    HOUSEHOLD_UPDATE(7, "Household Detail Update", "Household Detail Update", "Household Head Update"),
    RESIDENT_UPDATE(7, "Resident Detail Update", "Resident Detail Update", "Resident Detail - Update")
    ;
    private final Integer key;
    private final String desc;
    private final String mainAction;
    private final String secAction;

    LogsTypeEnum(Integer key, String desc, String mainAction, String secAction) {
        this.key = key;
        this.desc = desc;
        this.mainAction = mainAction;
        this.secAction = secAction;
    }

    public static List<Integer> residentLogKeys() {
        List<Integer> keys = new ArrayList<>();
        keys.add(DOCUMENT_REQUEST.key);
        keys.add(COMM_REPORT.key);
        keys.add(MY_PROFILE_UPDATE.key);
        keys.add(RESET_USER.key);
        return keys;
    }

    public static List<KeyValueModel> getTypeList() {
        List<KeyValueModel> list = new ArrayList<>();
        List<Integer> keysToRemove = List.of(NEW_USER_SMS.key, RESET_USER.key, DOCUMENT_REQUEST.key, COMM_REPORT.key, MY_PROFILE_UPDATE.key, HOUSEHOLD_UPDATE.key, RESIDENT_UPDATE.key);
        for (LogsTypeEnum val : values()) {
            if (!keysToRemove.contains(val.getKey())) list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        return list;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (LogsTypeEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc();
            }
        }
        return "";
    }
}
