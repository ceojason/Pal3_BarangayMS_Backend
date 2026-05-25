package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public enum CommReportTypeEnum {

    BLOTTER(1, "Blotter"),
    INCIDENT(2, "Incident"),
    INFRASTRUCTURE(3, "Infrastructure"),
    HEALTH(4, "Health Concern"),
    SANITATION(5, "Sanitation"),
    PUBLIC_SAFETY(6, "Public Safety"),
    NOISE_COMPLAINT(7, "Noise Complaint"),
    ANIMAL_CONCERN(8, "Animal Concern"),
    DISASTER_EMERGENCY(9, "Disaster / Emergency"),
    VANDALISM(10, "Vandalism"),
    STREET_LIGHT(11, "Street Light Issue"),
    ROAD_DAMAGE(12, "Road Damage"),
    DRAINAGE(13, "Drainage Problem"),
    WATER_SUPPLY(14, "Water Supply Issue"),
    ELECTRICAL(15, "Electrical Issue"),
    OTHER(99, "Others");

    private final Integer key;
    private final String value;

    CommReportTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<KeyValueModel> getCommReportTypeList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (CommReportTypeEnum val : values()) {
            if (!OTHER.key.equals(val.getKey())) list.add(new KeyValueModel(val.getKey(), val.getValue()));
        }
        list.sort(Comparator.comparing(KeyValueModel::getValue));
        list.add(new KeyValueModel(OTHER.key, OTHER.value));
        return list;
    }

    public static String getDescBKey(Integer key) {
        if (key!=null) {
            for (CommReportTypeEnum type : values()) {
                if (type.getKey().equals(key)) return type.value;
            }
        }
        return null;
    }
}