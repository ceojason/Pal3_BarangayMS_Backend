package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ChannelEnum {

    SMS(0, "SMS"),
    EMAIL(1, "Email")
    ;

    private final Integer key;
    private final String desc;

    ChannelEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static List<KeyValueModel> getChannelList() {
        List<KeyValueModel> list = new ArrayList<>();
        for (ChannelEnum val : values()) {
            list.add(new KeyValueModel(val.getKey(), val.getDesc()));
        }
        return list;
    }

}
