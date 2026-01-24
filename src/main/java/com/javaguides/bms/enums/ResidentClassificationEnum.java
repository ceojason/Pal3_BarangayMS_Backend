package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum ResidentClassificationEnum {

    //REGULAR(0, "Regular"),
    SENIOR_CITIZEN(1, "Senior Citizen"),
    STUDENT(2, "Student"),
    YOUTH(3, "Youth/Minor"),
    ADULT(4, "Adult"),
    PWD(5, "Person with Disability"),
    SOLO_PARENT(6, "Solo Parent"),
    PREGNANT_WOMAN(7, "Pregnant Woman"),
    INDIGENT(8, "Indigent/Low Income"),
    FOUR_PS(9, "4PS Beneficiary"),
    OFW(10, "Overseas Filipino Worker (OFW)"),
    OFFICIAL(11, "Barangay Official"),
    ALL(12, "All Resident Type")
    ;

    private final Integer key;
    private final String desc;

    ResidentClassificationEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static List<KeyValueModel> getResidentClassList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (ResidentClassificationEnum val : values()) {
            if (!val.getKey().equals(ALL.getKey())) {
                list.add(new KeyValueModel(val.getKey(), val.getDesc()));
            }
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static List<KeyValueModel> getAllResidentClassList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (ResidentClassificationEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static String getDescByKey(Integer key) {
        if (key!=null) {
            for (ResidentClassificationEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc();
            }
        }
        return "";
    }

    public static List<Integer> getAllKeys() {
        List<Integer> keys = new ArrayList<>();
        for (ResidentClassificationEnum val : values()) {
            if (!val.getKey().equals(ALL.getKey())) {
                keys.add(val.getKey());
            }
        }
        return keys;
    }
}
