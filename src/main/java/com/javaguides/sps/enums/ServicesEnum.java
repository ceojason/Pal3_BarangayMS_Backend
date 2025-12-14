package com.javaguides.sps.enums;

import com.javaguides.sps.helper.KeyValueBoolModelStr;
import com.javaguides.sps.helper.KeyValueModelStr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ServicesEnum {

    DASHBOARD("DASHBOARD", "Dashboard", false, "/dashboard", null),
    ADD_STUDENT("ADD_STUDENT", "Add Student", false, "/studentAdd", "AST"),
    SEARCH_STUDENT("SEARCH_STUDENT", "Search Student", false, "/studentSearch", "SST"),
    DELETE_STUDENT("DELETE_STUDENT", "Delete Student", false, null, "DST"),
    ADD_FACULTY("ADD_FACULTY", "Add Faculty", false, "/facultyAdd", "AFA"),
    SEARCH_FACULTY("SEARCH_FACULTY", "Search Faculty", false, "/facultySearch", "SFA"),
    SYSTEM_SETTINGS("SYSTEM_SETTINGS", "System Settings", false,  "/systemSettings", null),
    ;

    private final String code;
    private final String serviceDscp;
    private final Boolean isActive;
    private final String path;
    private final String refNoCode;

    ServicesEnum(String code, String serviceDscp, boolean isActive, String path, String refNoCode) {
        this.code = code;
        this.serviceDscp = serviceDscp;
        this.isActive = isActive;
        this.path = path;
        this.refNoCode = refNoCode;
    }

    public static List<KeyValueModelStr> getServicesList() {
        List<KeyValueModelStr> list = new ArrayList<>();
        for (ServicesEnum val : ServicesEnum.values()) {
            list.add(new KeyValueModelStr(val.code, val.serviceDscp));
        }
        //list.sort(Comparator.comparing(KeyValueModelStr::getKey));
        return list;
    }

    public static String getRefNoCodeByCd(String cd) {
        if (cd!=null) {
            for (ServicesEnum se : values()) {
                if (cd.equals(se.getCode())) {
                    return se.getRefNoCode();
                }
            }
        }
        return null;
    }

    public static List<KeyValueBoolModelStr> getServicesListForNav() {
        List<KeyValueBoolModelStr> list = new ArrayList<>();
        for (KeyValueModelStr val : getServicesList()) {
            if (val.getKey().equals(DASHBOARD.code)) {
                list.add(new KeyValueBoolModelStr(val.getKey(), val.getValue(), true));
            }else{
                list.add(new KeyValueBoolModelStr(val.getKey(), val.getValue(), false));
            }
        }
        return list;
    }
}
