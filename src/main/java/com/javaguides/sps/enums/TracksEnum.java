package com.javaguides.sps.enums;

import lombok.Getter;

@Getter
public enum TracksEnum {

    ACADEMIC("ACADEMIC", "Academic Track"),
    TVL("TVL", "Technical-Vocational-Livelihood (TVL) Track"),
    SPORTS("SPORTS", "Sports Track"),
    ARTS_DESIGN("ARTS_DESIGN", "Arts and Design Track")
    ;

    private final String key;
    private final String dscp;

    TracksEnum(String key, String dscp) {
        this.key = key;
        this.dscp = dscp;
    }


}
