package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum DocumentCategoryEnum {

    CLEARANCE(1, "Clearances"),
    CERTIFICATION(2, "Certifications"),
    PERMIT(3, "Permits"),
    BUSINESS(4, "Business Documents"),
    PROPERTY(5, "Property Documents"),
    ID(6, "Identification Card");

    private final Integer key;
    private final String value;

    DocumentCategoryEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<KeyValueModel> getDocumentCatList() {
        List<KeyValueModel> list = new ArrayList<>();

        for (DocumentCategoryEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getValue()));
        }

        list.sort(Comparator.comparing(KeyValueModel::getValue));
        return list;
    }

    public static String getDocuCatDescByKey(Integer key) {
        if (key!=null) {
            for (DocumentCategoryEnum val : values()) {
                if (key.equals(val.getKey())) return val.getValue();
            }
        }
        return "";
    }
}