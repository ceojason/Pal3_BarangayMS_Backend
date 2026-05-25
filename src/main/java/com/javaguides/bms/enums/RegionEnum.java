package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModelStr;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RegionEnum {

    NCR("NCR", "National Capital Region"),
    CAR("CAR", "Cordillera Administrative Region"),
    REGION_I("I", "Ilocos Region"),
    REGION_II("II", "Cagayan Valley"),
    REGION_III("III", "Central Luzon"),
    REGION_IV_A("IV-A", "CALABARZON"),
    REGION_IV_B("IV-B", "MIMAROPA"),
    REGION_V("V", "Bicol Region"),
    REGION_VI("VI", "Western Visayas"),
    REGION_VII("VII", "Central Visayas"),
    REGION_VIII("VIII", "Eastern Visayas"),
    REGION_IX("IX", "Zamboanga Peninsula"),
    REGION_X("X", "Northern Mindanao"),
    REGION_XI("XI", "Davao Region"),
    REGION_XII("XII", "SOCCSKSARGEN"),
    REGION_XIII("XIII", "Caraga"),
    BARMM("BARMM", "Bangsamoro Autonomous Region in Muslim Mindanao");

    private final String code;
    private final String desc;

    RegionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<String> getAllCodes() {
        List<String> list = new ArrayList<>();
        for (RegionEnum val : values()) {
            list.add(val.getCode());
        }
        return list;
    }

    public static List<KeyValueModelStr> regionList() {
        List<KeyValueModelStr> list = new ArrayList<>();
        for (RegionEnum val : values()) {
            list.add(new KeyValueModelStr(val.getCode(), val.getCode() + " - " + val.getDesc()));
        }
        return list;
    }

    public static List<String> getAllDescriptions() {
        List<String> list = new ArrayList<>();
        for (RegionEnum val : values()) {
            list.add(val.getDesc());
        }
        return list;
    }

    public static RegionEnum getByCode(String code) {
        for (RegionEnum val : values()) {
            if (val.getCode().equalsIgnoreCase(code)) {
                return val;
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        RegionEnum region = getByCode(code);
        return region != null ? region.getDesc() : "-";
    }
}