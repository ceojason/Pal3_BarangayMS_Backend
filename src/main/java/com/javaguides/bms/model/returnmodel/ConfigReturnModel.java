package com.javaguides.bms.model.returnmodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ConfigReturnModel {

    private String ackMessage;
    private String refNo;

    private String barangayNm;
    private String municipalAddress;
    private String province;
    private String zipCode;
    private String region;
    private String country;

    private String string1;
    private String string2;
    private String string3;
    private String string4;
    private String string5;
    private String string6;
    private String string7;
    private String string8;
    private String string9;
    private String string10;

    private String configCd;

    private String configAddressPrefix;
}
