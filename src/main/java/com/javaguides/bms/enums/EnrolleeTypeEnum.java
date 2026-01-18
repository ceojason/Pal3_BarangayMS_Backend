package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum EnrolleeTypeEnum {

    DEFAULT(0, "", false),
    NEW_ENROLLEE(1, "New Enrollee", true),
    OLD_ENROLLEE(2, "Old Enrollee", false),
    TRANSFEREE(3, "Transferee Enrollee", false),
    FOREIGN(4, "Foreign Enrollee", true),
    ;

    private final Integer key;
    private final String dscp;
    private final Boolean isRegular;

    EnrolleeTypeEnum(Integer key, String dscp, boolean isRegular) {
        this.key = key;
        this.dscp = dscp;
        this.isRegular = isRegular;
    }

    public static List<KeyValueModel> getEnrolleeTypeList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (EnrolleeTypeEnum val : values()) {
            if (!val.getKey().equals(DEFAULT.getKey())) {
                list.add(new KeyValueModel(val.getKey(), val.dscp));
            }
        }
        list.sort(Comparator.comparing(KeyValueModel::getValue));
        return list;
    }

    public static String getEnrolleeTypeDscpByKey(Integer key) {
        for (EnrolleeTypeEnum val : values()) {
            if (val.getKey().equals(key)) {
                return val.dscp;
            }
        }
        return null;
    }

}
