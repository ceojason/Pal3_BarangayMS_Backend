package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.EnrolleeTypeEnum;
import com.javaguides.bms.enums.GenderEnum;
import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.model.StudentModel;
import com.javaguides.bms.model.SubjectsModel;
import com.javaguides.bms.helper.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StudentReturnModel {


    private String id;
    private String lrn;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Date bday;
    private String gender;
    private String homeAddress;
    private String mobileNo;
    private String emailAddress;
    private String guardianFirstNm;
    private String guardianMiddleNm;
    private String guardianLastNm;
    private String guardianMobileNo;
    private String yearlevelKey;
    private String strandKey;
    private String sectionId;
    private Integer status;
    private Integer enrolleeTypeKey;
    private List<String> enrolledSubjects;
    private Date dateEnrolled;

    private String guardianFullNm;
    private String studentFullNm;
    private String genderDscp;
    public String statusDscp;
    public String sectionNm;
    public String strandKeyDscp;
    public String dateEnrolledDscp;
    public String bdayDscp;
    public String yearlevelDscp;
    public List<String> subjectNmListG11;
    public List<String> subjectNmListG12;
    public List<SubjectsModel> subjectObjList;
    public String enrolleeTypeDscp;

    public String createdBy;
    public String txnDscp;

    public String refNo;
    public String ackMessage;

    public StudentReturnModel(StudentModel studentModel) {
        this.id = studentModel.getId();
        this.lrn = studentModel.getLrn();
        this.firstNm = studentModel.getFirstNm();
        this.middleNm = studentModel.getMiddleNm();
        this.lastNm = studentModel.getLastNm();
        this.bday = studentModel.getBday();
        this.gender = studentModel.getGender();
        this.homeAddress = studentModel.getHomeAddress();
        this.mobileNo = studentModel.getMobileNo();
        this.emailAddress = studentModel.getEmailAddress();
        this.guardianFirstNm = studentModel.getGuardianFirstNm();
        this.guardianMiddleNm = studentModel.getGuardianMiddleNm();
        this.guardianLastNm = studentModel.getGuardianLastNm();
        this.guardianMobileNo = studentModel.getGuardianMobileNo();
        this.yearlevelKey = studentModel.getYearlevelKey();
        this.strandKey = studentModel.getStrandKey();
        this.sectionId = studentModel.getSectionId();
        this.status = studentModel.getStatus();
        this.enrolleeTypeKey = studentModel.getEnrolleeTypeKey();
        this.enrolledSubjects = studentModel.getEnrolledSubjects();
        this.dateEnrolled = studentModel.getDateEnrolled();
        this.guardianFullNm = studentModel.getGuardianFullNm();
        this.studentFullNm = studentModel.getStudentFullNm();
        this.genderDscp = GenderEnum.getGenderDscpFromKeyStr(gender);
        this.strandKeyDscp = studentModel.getStrandKeyDscp();
        this.dateEnrolledDscp = DateUtil.getDateStringWithFormat(dateEnrolled, DateFormatEnum.DT_FORMAT_1.getPattern());
        this.bdayDscp = DateUtil.getDateStringWithFormat(bday, DateFormatEnum.DT_FORMAT_1.getPattern());
        this.statusDscp = SystemStatusEnum.getDscpByKey(status);
        this.sectionNm = studentModel.getSectionNm();
        this.strandKeyDscp = studentModel.getStrandKeyDscp();
        this.yearlevelDscp =  studentModel.getYearlevelDscp();
        this.subjectNmListG11 = studentModel.getSubjectNmListG11();
        this.subjectNmListG12 = studentModel.getSubjectNmListG12();
        this.subjectObjList = studentModel.getSubjectObjList();
        this.enrolleeTypeDscp = EnrolleeTypeEnum.getEnrolleeTypeDscpByKey(enrolleeTypeKey);
        this.createdBy = studentModel.getCreatedBy();
        this.txnDscp = studentModel.getTxnDscp();
        this.refNo = studentModel.getRefNo();
        this.ackMessage = studentModel.getAckMessage();
    }
}
