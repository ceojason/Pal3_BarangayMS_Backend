package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum PhaseEnum {

    PH1(0, "Phase 1", "Purok 1"),
    PH2(1, "Phase 2", "Purok 2"),
    PH3(2, "Phase 3", "Purok 3"),
    PH4(3, "Phase 4", "Purok 4"),
    PH5(4, "Phase 5", "Purok 5"),
    ;

    private final Integer key;
    private final String desc;
    private final String desc2;

    PhaseEnum(Integer key, String desc, String desc2) {
        this.key = key;
        this.desc = desc;
        this.desc2 = desc2;
    }

    public static List<KeyValueModel> getPhaseList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (PhaseEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static List<KeyValueModel> getPurokList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (PhaseEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc2()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getKey));
        return list;
    }

    public static String getDesc2ByKey(Integer key) {
        if (key!=null) {
            for (PhaseEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc2();
            }
        }
        return "";
    }
}
