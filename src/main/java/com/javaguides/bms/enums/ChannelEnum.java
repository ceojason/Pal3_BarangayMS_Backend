package com.javaguides.bms.enums;

import com.javaguides.bms.helper.KeyValueModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum ChannelEnum {

    SMS(0, "SMS"),
    EMAIL(1, "Email"),
    ALL(2, "SMS/Email")
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

    public static String getStringByKey(Integer key) {
        if (key!=null) {
            for (ChannelEnum val : values()) {
                if (val.getKey().equals(key)) return val.getDesc();
            }
        }
        return "";
    }

    public static List<Integer> sendViaEmailKeys() {
        List<Integer> val = new ArrayList<>();
        val.add(EMAIL.getKey());
        val.add(ALL.getKey());
        return val;
    }

    public static List<Integer> sendViaSmsKeys() {
        List<Integer> val = new ArrayList<>();
        val.add(SMS.key);
        val.add(ALL.key);
        return val;
    }
}
