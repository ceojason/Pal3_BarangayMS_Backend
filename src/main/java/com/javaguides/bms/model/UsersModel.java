package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.ResidentClassificationEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_users")
@TableAlias("tu")
public class UsersModel extends BaseModel {

    @Column(name = "FIRST_NM")
    private String firstNm;

    @Column(name = "MIDDLE_NM")
    private String middleNm;

    @Column(name = "LAST_NM")
    private String lastNm;

    @Column(name = "SUFFIX")
    private String suffix;

    @Column(name = "BIRTH_DT")
    private Date birthDt;

    @Column(name = "BIRTH_PLACE")
    private String birthPlace;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "CIVIL_STATUS")
    private Integer civilStatusKey;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "PUROK_KEY")
    private Integer phaseKey;

    @Column(name = "HOME_ADDRESS")
    private String homeAddress;

    @Column(name = "HOUSEHOLD_KEY")
    private Integer householdKey;

    @Column(name = "OCCUPATION")
    private String occupation;

    @Column(name = "RELIGION")
    private String religion;

    @Column(name = "IS_REGISTERED_VOTER")
    private Integer isRegisteredVoter;

    @Column(name = "CLASSIFICATION_KEY")
    private String classificationKey;

    @Column(name = "DATE_ENROLLED")
    private Date dateEnrolled;


    @Transient
    private String formattedMobileNo;

    @Transient
    private List<Integer> residentClassKeys;

    @Transient
    private String genderDscp;

    @Transient
    private String birthDtString;

    @Transient
    private String civilStatusString;

    @Transient
    private String phaseString;

    @Transient
    private String householdString;

    @Transient
    private String isRegisteredVoterString;

    public String getDateEnrolledString() {
        return dateEnrolled!=null
                ? DateUtil.getDateStringWithFormat(dateEnrolled, DateFormatEnum.DT_FORMAT_1.getPattern()) : "";
    }

    public String getClassificationKeyString() {
        StringBuilder sb = new StringBuilder();
        if (residentClassKeys!=null && !residentClassKeys.isEmpty()) {
            for (Integer i : residentClassKeys) {
                if (!sb.isEmpty()) sb.append("|");
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public String getClassificationKeyStringForDisplay() {
        StringBuilder sb = new StringBuilder();
        if (residentClassKeys!=null && !residentClassKeys.isEmpty()) {
            for (Integer i : residentClassKeys) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(ResidentClassificationEnum.getDescByKey(i));
            }
        }
        return sb.toString();
    }

    public String getFullNm() {
        StringBuilder fullNm = new StringBuilder();
        fullNm.append(lastNm).append(", ").append(firstNm).append(", ")
                .append(middleNm!=null ? middleNm : "").append(" ").append(suffix!=null ? suffix : "");
        return fullNm.toString();
    }

    public UsersModel(EnrollmentRequest request) {
        if (request!=null) {
            setId(request.getId());
            setFirstNm(request.getFirstNm());
            setMiddleNm(request.getMiddleNm());
            setLastNm(request.getLastNm());
            setSuffix(request.getSuffix());
            setBirthDt(request.getBirthDt());
            setBirthPlace(request.getBirthPlace());
            setGender(request.getGender());
            setCivilStatusKey(request.getCivilStatusKey());
            setMobileNo(request.getMobileNo());
            setFormattedMobileNo(request.getFormattedMobileNo());
            setEmailAddress(request.getEmailAddress());
            setPhaseKey(request.getPhaseKey());
            setHomeAddress(request.getHomeAddress());
            setHouseholdKey(request.getHouseholdKey());
            setOccupation(request.getOccupation());
            setReligion(request.getReligion());
            setIsRegisteredVoter(request.getIsRegisteredVoter());
            setResidentClassKeys(request.getResidentClassKeys());
        }
    }
}
