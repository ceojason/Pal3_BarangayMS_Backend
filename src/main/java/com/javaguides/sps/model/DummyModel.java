package com.javaguides.sps.model;

import com.javaguides.sps.enums.DateFormatEnum;
import com.javaguides.sps.helper.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DummyModel {

    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private Integer role;
    private String roleDscp;
    private Date lastLoginDt;
    private String lastLoginDtString;

    public String getDummyFullNm() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.firstName.toUpperCase());
        //if (middleName!=null) {
            //sb.append(" ");
            //sb.append(middleName);
        //}
        sb.append(" ");
        sb.append(this.lastName.toUpperCase());
        return sb.toString();
    }

    public String getLastLoginDtString() {
        if (lastLoginDt!=null) {
            return DateUtil.getDateStringWithFormat(lastLoginDt, DateFormatEnum.DT_FORMAT_6.getPattern());
        }
        return "";
    }
}
