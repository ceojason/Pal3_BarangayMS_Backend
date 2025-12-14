package com.javaguides.sps.model;

import com.javaguides.sps.customannotations.TableAlias;
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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_students")
@TableAlias("tst")
public class StudentModel extends BaseModel {

    @Column(name = "LRN")
    private String lrn;

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

    @Column(name = "GUARDIAN_FIRST_NM")
    private String guardianFirstNm;

    @Column(name = "GUARDIAN_MIDDLE_NM")
    private String guardianMiddleNm;

    @Column(name = "GUARDIAN_LAST_NM")
    private String guardianLastNm;

    @Column(name = "GUARDIAN_MOBILE_NO")
    private String guardianMobileNo;

    @Column(name = "YEARLEVEL_KEY")
    private String yearlevelKey;

    @Column(name = "STRAND_KEY")
    private String strandKey;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "ENROLLEE_TYPE")
    private Integer enrolleeTypeKey;

    @Column(name = "ENROLLMENT_STATUS")
    private Integer status;

    @Column(name = "DATE_ENROLLED")
    private Date dateEnrolled;

    @Transient
    public String statusDscp;
    @Transient
    public String sectionDscp;
    @Transient
    public String strandKeyDscp;
    @Transient
    public String dateEnrolledDscp;
    @Transient
    public String genderDscp;
    @Transient
    public String bdayDscp;
    @Transient
    public String yearlevelDscp;
    @Transient
    public List<String> enrolledSubjects;
    @Transient
    public List<SubjectsModel> subjectObjList;
    @Transient
    public List<String> subjectNmListG11;
    @Transient
    public List<String> subjectNmListG12;
    @Transient
    public String enrolleeTypeDscp;
    @Transient
    public String sectionNm;


    public String getStudentFullNm(){
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

    public String getSectionNmStrand() {
        if (strandKey!=null && sectionNm!=null) return strandKey + " | "  + sectionNm;
        return sectionNm;
    }

    public String getGuardianFullNm(){
        StringBuilder fullNm = new StringBuilder();
        if (guardianFirstNm!=null) fullNm.append(guardianFirstNm);
        if (guardianMiddleNm!=null) {
            fullNm.append(" ");
            fullNm.append(guardianMiddleNm);
        }
        if (guardianLastNm!=null) {
            fullNm.append(" ");
            fullNm.append(guardianLastNm);
        }
        return fullNm.toString();
    }

    public StudentModel(EnrollmentRequest request) {
        if (request!=null) {
            setId(request.getId());
            setLrn(request.getLrn());
            setFirstNm(request.getFirstNm());
            setMiddleNm(request.getMiddleNm());
            setLastNm(request.getLastNm());
            setBday(request.getBday());
            setGender(request.getGender());
            setHomeAddress(request.getHomeAddress());
            setMobileNo(request.getMobileNo());
            setEmailAddress(request.getEmailAddress());
            setGuardianFirstNm(request.getGuardianFirstNm());
            setGuardianMiddleNm(request.getGuardianMiddleNm());
            setGuardianLastNm(request.getGuardianLastNm());
            setGuardianMobileNo(request.getGuardianMobileNo());
            setYearlevelKey(request.getYearlevelKey());
            setStrandKey(request.getStrandKey());
            setSectionId(request.getSectionId());
            setStatus(request.getStatus());
            setEnrolleeTypeKey(request.getEnrolleeTypeKey());
            setEnrolledSubjects(request.getEnrolledSubjects());
            setDateEnrolled(request.getDateEnrolled());
        }
    }

}
