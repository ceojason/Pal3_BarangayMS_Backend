package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModelStr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum SystemConfigEnum {

//    GLOBAL_SETTINGS("CONFIG_GLOBAL", "Global/System Settings"),
    PRICING_SETTINGS("CONFIG_PRICING", "Service Pricing"),
    BRGY_SETTINGS("CONFIG_BRGY_SETTINGS", "Barangay Details"),
    EMERGENCY_HOTLINES("CONFIG_HOTLINES", "Emergency Hotlines")
    ;

    private final String code;
    private final String desc;

    SystemConfigEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<KeyValueModelStr> configList() {
        List<KeyValueModelStr> list = new ArrayList<>();
        for (SystemConfigEnum val : values()) {
            list.add(new KeyValueModelStr(val.getCode(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModelStr::getValue));
        return list;
    }
}
