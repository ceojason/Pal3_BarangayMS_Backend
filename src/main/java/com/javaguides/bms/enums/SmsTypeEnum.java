package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SmsTypeEnum {

    NEW_USER_SMS(0, "User Registration"),
    ANNOUNCEMENT_SMS(1, "Announcement"),
    ALARM_WARNING_SMS(2, "Alarm/Warning"),
    RESET_USER(3, "Reset User"),
    DOCUMENT_REQUEST(4, "Document Request")
    ;
    private final Integer key;
    private final String desc;

    SmsTypeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static List<KeyValueModel> getTypeList() {
        List<KeyValueModel> list = new ArrayList<>();
        List<Integer> keysToRemove = List.of(NEW_USER_SMS.key, RESET_USER.key, DOCUMENT_REQUEST.key);
        for (SmsTypeEnum val : values()) {
            if (!keysToRemove.contains(val.getKey())) list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        return list;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (SmsTypeEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc();
            }
        }
        return "";
    }
}
