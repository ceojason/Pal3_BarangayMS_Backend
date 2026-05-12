package com.javaguides.bms.model.basemodel;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.helper.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserModel {

    private String userId;
    private String cd;
    private String password;
    private Integer roleKey;
    private String roleString;

    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffix;

    private String homeAddress;
    private String birthDtString;
    private String genderString;
    private String civilStatusString;
    private String mobileNo;


    private Date lastLoginDt;

    public String getLastLoginDtString() {
        if (lastLoginDt!=null) {
            return DateUtil.getDateStringWithFormat(lastLoginDt, DateFormatEnum.DT_FORMAT_7.getPattern());
        }
        return null;
    }

    public String getRoleDscp() {
        return SystemUserEnum.getRoleDscpByKey(roleKey);
    }

    public String getUserFullNm() {
        StringBuilder fullNm = new StringBuilder();
        if (lastNm!=null) {
            fullNm.append(lastNm);
        }
        if (firstNm!=null) {
            fullNm.append(", ")
                    .append(firstNm);
        }
        if (middleNm!=null) {
            fullNm.append(", ")
                    .append(middleNm);
        }
        if (suffix!=null) {
            fullNm.append(" ")
                    .append(suffix);
        }
        return fullNm.toString();
    }

    public String getDisplayNmInNav() {
        StringBuilder name = new StringBuilder();
        name.append(firstNm).append(" ").append(lastNm);
        return name.toString();
    }

    public String getInitialsInNav() {
        StringBuilder name = new StringBuilder();
        return name.append(firstNm.charAt(0)).append(lastNm.charAt(0)).toString();
    }

    public String getCurrentDtAndTime() {
        return DateUtil.getDateStringWithFormat(new Date(), DateFormatEnum.DT_FORMAT_5.getPattern());
    }
}
