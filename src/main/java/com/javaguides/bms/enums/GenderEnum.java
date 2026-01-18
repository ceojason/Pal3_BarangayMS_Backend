package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModelStr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum GenderEnum {

    MALE(0, "M", "Male"),
    FEMALE(1, "F", "Female");

    private final Integer key;
    private final String keyStr;
    private final String dscp;

    GenderEnum(Integer key, String keyStr, String dscp) {
        this.key = key;
        this.keyStr = keyStr;
        this.dscp = dscp;
    }

    public static List<KeyValueModelStr> getGenderListStr() {
        List<KeyValueModelStr> list = new ArrayList<>();
        list.add(new KeyValueModelStr(MALE.keyStr, MALE.dscp));
        list.add(new KeyValueModelStr(FEMALE.keyStr, FEMALE.dscp));
        return  list;
    }

    public static String getGenderDscpFromKeyStr(String keyStr) {
        if (keyStr!=null) {
            for (GenderEnum val : GenderEnum.values()) {
                if (val.keyStr.equals(keyStr)) {
                    return val.dscp;
                }
            }
        }
        return null;
    }
}
