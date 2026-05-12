package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum DocumentPurposeEnum {
    //to be used for tbl_config
    EMPLOYMENT(0, "Employment"),
    BUSINESS_PERMIT(1, "Business Permit"),
    LOAN_APP(2, "Loan Application"),
    SCHOOL_REQ(3, "School Requirement"),
    GOV_ID_REQ(4, "Government ID Requirement"),
    OTHERS(5, "Others")
    ;

    private final Integer key;
    private final String desc;

    DocumentPurposeEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static List<KeyValueModel> getPurposeKeyList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (DocumentPurposeEnum val : values()) {
            if (!val.getKey().equals(OTHERS.getKey())) list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getValue ));
        list.add(new KeyValueModel(OTHERS.getKey(), OTHERS.getDesc()));
        return list;
    }

    public static String getDocuPurposeDescByKey(Integer key) {
        if (key!=null) {
            for (DocumentPurposeEnum val : values()) {
                if (key.equals(val.getKey())) return val.getDesc();
            }
        }
        return "";
    }
}
