package com.javaguides.bms.model.requestmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigRequest {

    @JsonProperty("FEE-001")
    private BigDecimal fee001;

    @JsonProperty("FEE-002")
    private BigDecimal fee002;

    @JsonProperty("FEE-003")
    private BigDecimal fee003;

    @JsonProperty("FEE-004")
    private BigDecimal fee004;

    @JsonProperty("FEE-005")
    private BigDecimal fee005;

    @JsonProperty("FEE-006")
    private BigDecimal fee006;

    @JsonProperty("FEE-007")
    private BigDecimal fee007;

    @JsonProperty("FEE-008")
    private BigDecimal fee008;

    @JsonProperty("FEE-009")
    private BigDecimal fee009;

    @JsonProperty("FEE-010")
    private BigDecimal fee010;

    @JsonProperty("FEE-011")
    private BigDecimal fee011;

    @JsonProperty("FEE-012")
    private BigDecimal fee012;

    @JsonProperty("FEE-013")
    private BigDecimal fee013;

    @JsonProperty("FEE-014")
    private BigDecimal fee014;

    @JsonProperty("FEE-015")
    private BigDecimal fee015;

    @JsonProperty("FEE-016")
    private BigDecimal fee016;

    @JsonProperty("FEE-017")
    private BigDecimal fee017;

    @JsonProperty("FEE-018")
    private BigDecimal fee018;

    @JsonProperty("FEE-019")
    private BigDecimal fee019;

    @JsonProperty("FEE-020")
    private BigDecimal fee020;

    @JsonProperty("FEE-021")
    private BigDecimal fee021;

    @JsonProperty("FEE-022")
    private BigDecimal fee022;

    @JsonProperty("FEE-023")
    private BigDecimal fee023;

    @JsonProperty("FEE-024")
    private BigDecimal fee024;

    @JsonProperty("FEE-025")
    private BigDecimal fee025;

    @JsonProperty("FEE-026")
    private BigDecimal fee026;

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

}
