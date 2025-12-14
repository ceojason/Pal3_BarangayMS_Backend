package com.javaguides.sps.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeyValueModel {

    private Integer key;
    private String value;

    public KeyValueModel(Integer code, String serviceDscp) {
        this.key = code;
        this.value = serviceDscp;
    }
}
