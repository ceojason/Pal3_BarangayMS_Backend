package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModelStr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum YearlevelEnum {

//    GRADE7(0, "Grade 7"),
//    GRADE8(1, "Grade 8"),
//    GRADE9(2, "Grade 9"),
//    GRADE10(3, "Grade 10"),
    GRADE11("GRADE11", "Grade 11"),
    GRADE12("GRADE12", "Grade 12"),
    ;

    private final String keyStr;
    private final String dscp;

    YearlevelEnum(String keyStr, String dscp) {
        this.keyStr = keyStr;
        this.dscp = dscp;
    }

    public static List<KeyValueModelStr> getYearlevelList() {
        List<KeyValueModelStr> list = new ArrayList<>();
        for (YearlevelEnum val : YearlevelEnum.values()) {
            list.add(new KeyValueModelStr(val.getKeyStr(), val.getDscp()));
        }
        list.sort(Comparator.comparing(KeyValueModelStr::getKey));
        return  list;
    }

    public static String getDscpByKeyStr(String keyStr) {
        for (YearlevelEnum val : YearlevelEnum.values()) {
            if (val.getKeyStr().equals(keyStr)) {
                return val.getDscp();
            }
        }
        return null;
    }
}
