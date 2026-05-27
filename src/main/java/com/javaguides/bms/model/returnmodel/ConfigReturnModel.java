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

    private String configCd;

    private String configAddressPrefix;
}
