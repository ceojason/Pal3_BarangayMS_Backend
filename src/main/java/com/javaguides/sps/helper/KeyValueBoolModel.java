package com.javaguides.sps.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeyValueBoolModel {

    private Integer key;
    private String value;
    private Boolean isChecked;

    public KeyValueBoolModel(Integer key, String serviceDscp, boolean isChecked) {
        this.key = key;
        this.value = serviceDscp;
        this.isChecked = isChecked;
    }

}
