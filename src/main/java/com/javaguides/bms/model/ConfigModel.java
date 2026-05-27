package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.SystemConfigEnum;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_config")
@TableAlias("tc")
public class ConfigModel extends BaseModel {

    @Column(name = "STRING_1")
    private String string_1;

    @Column(name = "STRING_2")
    private String string_2;

    @Column(name = "STRING_3")
    private String string_3;

    @Column(name = "STRING_4")
    private String string_4;

    @Column(name = "STRING_5")
    private String string_5;

    @Column(name = "STRING_6")
    private String string_6;

    @Column(name = "STRING_7")
    private String string_7;

    @Column(name = "STRING_8")
    private String string_8;

    @Column(name = "STRING_9")
    private String string_9;

    @Column(name = "STRING_10")
    private String string_10;

    public String configAddressPrefix() {
        StringBuilder address = new StringBuilder();
        if (string_1!=null && !string_1.isEmpty()) {
            address.append(string_1).append(" ");
        }
        if (string_2!=null && !string_2.isEmpty()) {
            address.append(string_2).append(", ");
        }
        if (string_3!=null && !string_3.isEmpty()) {
            address.append(string_3).append(" ");
        }
        if (string_4!=null && !string_4.isEmpty()) {
            address.append(string_4);
        }
        return address.toString();
    }

    @Transient
    private String barangayNm;
    @Transient
    private String municipalAddress;
    @Transient
    private String province;
    @Transient
    private String zipCode;
    @Transient
    private String region;
    @Transient
    private String country;

    public String getBarangayNmString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_1!=null ? string_1 : null);
        }
        return null;
    }

    public String getMunicipalAddressString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_2!=null ? string_2 : null);
        }
        return null;
    }

    public String getProvinceString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_3!=null ? string_3 : null);
        }
        return null;
    }

    public String getZipCodeString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_4!=null ? string_4 : null);
        }
        return null;
    }

    public String getRegionString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_5!=null ? string_5 : null);
        }
        return null;
    }

    public String getCountryString() {
        if (getId()!=null && SystemConfigEnum.BRGY_SETTINGS.getCode().equals(getId())) {
            return (string_6!=null ? string_6 : null);
        }
        return null;
    }

}
