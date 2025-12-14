package com.javaguides.sps.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeyValueModelStr {

    private String key;
    private String value;

    public KeyValueModelStr(String code, String serviceDscp) {
        this.key = code;
        this.value = serviceDscp;
    }
}
