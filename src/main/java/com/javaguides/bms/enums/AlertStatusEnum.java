package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AlertStatusEnum {

    Normal(0, "Normal", "Low Alert", "Informational"),
    Moderate(1, "Moderate", "Medium", "Warning"),
    High(2, "High", "Urgent", "Critical"),
    Emergency(3, "Severe", "Fatal", "Emergency")
    ;
    private final Integer key;
    private final String desc1;
    private final String desc2;
    private final String desc3;

    AlertStatusEnum(Integer key, String desc1, String desc2, String desc3) {
        this.key = key;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
    }

    public static List<KeyValueModel> getTypeList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (AlertStatusEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc3()));
        }
        return list;
    }

    public static String getDesc1ByKey(Integer key) {
        if (key!=null) {
            for (AlertStatusEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc1();
            }
        }
        return "";
    }

    public static String getDesc2ByKey(Integer key) {
        if (key!=null) {
            for (AlertStatusEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc2();
            }
        }
        return "";
    }

    public static String getDesc3ByKey(Integer key) {
        if (key!=null) {
            for (AlertStatusEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc3();
            }
        }
        return "";
    }
}
