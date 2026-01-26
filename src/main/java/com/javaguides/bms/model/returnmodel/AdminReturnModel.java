package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.SystemAdminModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class AdminReturnModel {

    private String id;
    private String cd;

    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffix;
    private String gender;
    private String genderDscp;
    private String homeAddress;
    private String mobileNo;
    private String formattedMobileNo;
    private String emailAddress;
    private Integer phaseKey;
    private String phaseString;

    private Date dateEnrolled;
    private String dateEnrolledString;
    private Integer status;
    private String statusString;
    private String fullNm;

    private Date lastLoginDt;
    private String lastLoginDtString;
    private String refNo;
    private String ackMessage;

    public AdminReturnModel(SystemAdminModel modelObj) {
        this.id = modelObj.getId();
        this.cd = modelObj.getCd();
        this.firstNm = modelObj.getFirstNm();
        this.middleNm = modelObj.getMiddleNm();
        this.lastNm = modelObj.getLastNm();
        this.fullNm = modelObj.getFullNm();
        this.suffix = modelObj.getSuffix();
        this.mobileNo = modelObj.getMobileNo();
        this.formattedMobileNo = modelObj.getFormattedMobileNo();
        this.homeAddress = modelObj.getHomeAddress();
        this.emailAddress = modelObj.getEmailAddress();
        this.dateEnrolled = modelObj.getDateEnrolled();
        this.dateEnrolledString = modelObj.getDateEnrolledString();
        this.status = modelObj.getStatus();
        this.statusString = SystemStatusEnum.getDscpByKey(status);
        //this.refNo = modelObj.getRefNo();
    }
}
