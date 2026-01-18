package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum CivilStatusEnum {

    SINGLE(0, "Single"),
    MARRIED(1, "Married"),
    SEPARATED(2, "Separated"),
    WIDOWED(3, "Widowed")
    ;

    private final Integer key;
    private final String desc;

    CivilStatusEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static List<KeyValueModel> getCivilStatusList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (CivilStatusEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.desc));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static String getCivilStatusDescByKey(Integer key) {
        if (key!=null) {
            for (CivilStatusEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc();
            }
        }

        return null;
    }
}
