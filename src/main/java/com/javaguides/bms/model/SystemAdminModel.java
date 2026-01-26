package com.javaguides.bms.model;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_admin")
public class SystemAdminModel extends BaseModel {

    @Column(name = "FIRST_NM")
    private String firstNm;

    @Column(name = "MIDDLE_NM")
    private String middleNm;

    @Column(name = "LAST_NM")
    private String lastNm;

    @Column(name = "SUFFIX")
    private String suffix;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "HOME_ADDRESS")
    private String homeAddress;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "DATE_ENROLLED")
    private Date dateEnrolled;

    @Column(name = "PHASE_KEY")
    private Integer phaseKey;

    @Transient
    private Integer role;

    @Transient
    private String cd;

    @Transient
    private String password;

    public String getDateEnrolledString() {
        return dateEnrolled!=null
                ? DateUtil.getDateStringWithFormat(dateEnrolled, DateFormatEnum.DT_FORMAT_1.getPattern()) : "";
    }

    public String getFullNm() {
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

    @Transient
    private String formattedMobileNo;

    @Transient
    private String genderDscp;

    @Transient
    private String bdayDscp;

    public String getAdminFullNm() {
        StringBuilder fullNm = new StringBuilder();
        if (firstNm!=null) fullNm.append(firstNm);
        if (middleNm!=null) {
            fullNm.append(" ");
            fullNm.append(middleNm);
        }
        if (lastNm!=null) {
            fullNm.append(" ");
            fullNm.append(lastNm);
        }
        return fullNm.toString();
    }

    public SystemAdminModel(EnrollmentRequest request) {
        if (request!=null) {
            setId(request.getId());
            setFirstNm(request.getFirstNm());
            setMiddleNm(request.getMiddleNm());
            setLastNm(request.getLastNm());
            setGender(request.getGender());
            setHomeAddress(request.getHomeAddress());
            setMobileNo(request.getMobileNo());
            setEmailAddress(request.getEmailAddress());
            setStatus(request.getStatus());
            setDateEnrolled(request.getDateEnrolled());
            setCd(request.getCd());
            setPassword(request.getPassword());
            setStatus(request.getStatus());
            setSuffix(request.getSuffix());
            setPhaseKey(request.getPhaseKey());
        }
    }

}
