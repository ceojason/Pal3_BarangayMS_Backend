package com.javaguides.sps.enums;

import com.javaguides.sps.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SystemUserEnum {

    STUDENT(0, "Student", "Students"),
    FACULTY(1, "Faculty", "Teachers"),
    SYSTEM_ADMIN(2, "System Administrator", "System Administrator")
    ;

    private final Integer key;
    private final String dscp;
    private final String pluralDscp;

    SystemUserEnum(Integer key, String dscp, String pluralDscp) {
        this.key = key;
        this.dscp = dscp;
        this.pluralDscp = pluralDscp;
    }

    public static List<KeyValueModel> getSystemUserEnumList() {
        List<KeyValueModel> list = new ArrayList<>();
        list.add(new KeyValueModel(STUDENT.getKey(), STUDENT.getDscp()));
        list.add(new KeyValueModel(FACULTY.getKey(), FACULTY.getDscp()));
        //list.add(new KeyValueModel(SYSTEM_ADMIN.getKey(), SYSTEM_ADMIN.getDscp()));
        return list;
    }
}
