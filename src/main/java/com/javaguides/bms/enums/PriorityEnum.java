package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum PriorityEnum {

    LOW(1, "Low"),
    NORMAL(2, "Normal"),
    HIGH(3, "High"),
    URGENT(4, "Urgent"),
    EMERGENCY(5, "Emergency");

    private final Integer key;
    private final String value;

    PriorityEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<KeyValueModel> getPriorityList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (PriorityEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getValue()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (PriorityEnum val : values()) {
                if (key.equals(val.getKey())) return val.getValue();
            }
        }
        return null;
    }
}
