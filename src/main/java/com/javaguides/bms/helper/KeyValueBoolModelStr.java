package com.javaguides.bms.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeyValueBoolModelStr {

    private String key;
    private String value;
    private Boolean isChecked;

    public KeyValueBoolModelStr(String code, String serviceDscp, boolean isChecked) {
        this.key = code;
        this.value = serviceDscp;
        this.isChecked = isChecked;
    }

}
