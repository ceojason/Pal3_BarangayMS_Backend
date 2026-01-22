package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.UsersModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UsersReturnModel {

    private String id;
    private String cd;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffix;
    private String fullNm;
    private Date birthDt;
    private String birthDtString;
    private String birthPlace;
    private String gender;
    private String genderDscp;
    private String homeAddress;
    private String mobileNo;
    private String formattedMobileNo;
    private String emailAddress;
    private String occupation;
    private String religion;
    private List<Integer> residentClassKeys;
    private Integer classificationTypeKey;
    private String classificationTypeString;
    private Integer phaseKey;
    private String phaseString;
    private Date dateEnrolled;
    private String dateEnrolledString;

    private Integer civilStatusKey;
    private String civilStatusString;

    private Integer status;
    private String statusString;

    private Integer isRegisteredVoter;
    private String isRegisteredVoterString;

    private String ackMessage;
    private String refNo;
    private String createdBy;
    private String txnString;

    public UsersReturnModel(UsersModel modelObj) {
        this.id = modelObj.getId();
        this.cd = modelObj.getCd();
        this.firstNm = modelObj.getFirstNm();
        this.middleNm = modelObj.getMiddleNm();
        this.lastNm = modelObj.getLastNm();
        this.fullNm = modelObj.getFullNm();
        this.suffix = modelObj.getSuffix();
        this.birthDt = modelObj.getBirthDt();
        this.birthDtString = DateUtil.getDateStringWithFormat(birthDt, DateFormatEnum.DT_FORMAT_1.getPattern());
        this.birthPlace = modelObj.getBirthPlace();
        this.gender = modelObj.getGender();
        this.genderDscp = GenderEnum.getGenderDscpFromKeyStr(gender);
        this.civilStatusKey = modelObj.getCivilStatusKey();
        this.civilStatusString = CivilStatusEnum.getCivilStatusDescByKey(civilStatusKey);
        this.mobileNo = modelObj.getMobileNo();
        this.formattedMobileNo = modelObj.getFormattedMobileNo();
        this.homeAddress = modelObj.getHomeAddress();
        this.emailAddress = modelObj.getEmailAddress();
        this.occupation = modelObj.getOccupation();
        this.religion = modelObj.getReligion();
        this.residentClassKeys = modelObj.getClassificationKeyList();
        this.classificationTypeString = modelObj.getClassificationKeyStringForDisplay();
        this.phaseKey = modelObj.getPhaseKey();
        this.dateEnrolled = modelObj.getDateEnrolled();
        this.dateEnrolledString = modelObj.getDateEnrolledString();

        this.status = modelObj.getStatus();
        this.statusString = SystemStatusEnum.getDscpByKey(status);

        this.phaseString = PhaseEnum.getDesc2ByKey(phaseKey);
        this.occupation = modelObj.getOccupation();
        this.religion = modelObj.getReligion();
        this.isRegisteredVoter = modelObj.getIsRegisteredVoter();
        this.isRegisteredVoterString = YesOrNoEnum.getDescByKey(isRegisteredVoter);
    }

}
