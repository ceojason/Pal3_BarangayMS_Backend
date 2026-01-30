package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum YesOrNoEnum {

    YES(0, "Yes", true),
    NO(1, "No", false)
    ;

    private final Integer key;
    private final String desc;
    private final Boolean booleanVal;

    YesOrNoEnum(Integer key, String desc, Boolean booleanVal) {
        this.key = key;
        this.desc = desc;
        this.booleanVal = booleanVal;
    }

    public static List<KeyValueModel> getYesNoList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (YesOrNoEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (YesOrNoEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc();
            }
        }
        return "";
    }
}
