package com.javaguides.sps.model;

import com.javaguides.sps.model.basemodel.BaseModel;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_admin")
public class SystemAdminModel extends BaseModel {

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "FIRST_NM")
    private String firstNm;

    @Column(name = "MIDDLE_NM")
    private String middleNm;

    @Column(name = "LAST_NM")
    private String lastNm;

    @Column(name = "BIRTHDAY")
    private Date bday;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "HOME_ADDRESS")
    private String homeAddress;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "DATE_ENROLLED")
    private Date dateEnrolled;

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
            setBday(request.getBday());
            setGender(request.getGender());
            setHomeAddress(request.getHomeAddress());
            setMobileNo(request.getMobileNo());
            setEmailAddress(request.getEmailAddress());
            setStatus(request.getStatus());
            setDateEnrolled(request.getDateEnrolled());
        }
    }

}
