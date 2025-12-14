package com.javaguides.sps.service;

import com.javaguides.sps.enums.*;
import com.javaguides.sps.helper.StringMessagesUtils;
import com.javaguides.sps.jdbc.repository.*;
import com.javaguides.sps.model.*;
import com.javaguides.sps.helper.DateUtil;
import com.javaguides.sps.helper.KeyHasher;
import com.javaguides.sps.helper.KeyValueModelStr;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;
import com.javaguides.sps.model.requestmodel.searchrequest.StudentSearchRequest;
import com.javaguides.sps.model.returnmodel.InitializeSubjectModel;
import com.javaguides.sps.model.returnmodel.StudentReturnModel;
import com.javaguides.sps.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class StudentEnrollmentServiceImpl extends BaseServiceImpl implements StudentEnrollmentService {

    private SectionJDBCRepository sectionJDBCRepository;
    private StrandsJDBCRepository strandsJDBCRepository;
    private FacultyJDBCRepository facultyJDBCRepository;
    private SubjectsJDBCRepository subjectsJDBCRepository;
    private StudentJDBCRepository studentJDBCRepository;
    private LoginJDBCRepository loginJDBCRepository;
    private AuditLogService auditLogService;
    static final String IS_REQUIRED_SUFFIX = " is required.";

    @Override
    public List<KeyValueModelStr> getSectionListByKeys(String yearlevelKey, String sectionKey) {
        List<KeyValueModelStr> list = new ArrayList<>();
        List<SectionModel> sectionList = sectionJDBCRepository.getAllActiveSectionsByKey(yearlevelKey, sectionKey);
        if (sectionList!=null && !sectionList.isEmpty()){
            for (SectionModel section : sectionList) {
                list.add(new KeyValueModelStr(section.getId(), section.getSectionNm()));
            }
        }
        return list;
    }

    @Override
    public StudentReturnModel findByLrn(String lrn) {
        StudentModel studentObj = studentJDBCRepository.findByLrn(lrn);
        if (studentObj!=null){
            if (studentObj.getSectionId()!=null){
                SectionModel sectionObj = sectionJDBCRepository.findDtlsById(studentObj.getSectionId());
                if (sectionObj!=null){
                    studentObj.setSectionNm(sectionObj.getSectionNm());
                }
            }

            List<SubjectsModel> subjectsFromDb = subjectsJDBCRepository.getAllSubjectsByKey(
                    studentObj.getYearlevelKey(),
                    studentObj.getStrandKey()
            );

            Map<String, SubjectsModel> subMap = new HashMap<>();
            for (SubjectsModel sub : subjectsFromDb) {
                subMap.put(sub.getId(), sub);
            }

            List<EnrolledSubjectsModel> g11Subjects = subjectsJDBCRepository.findEnrolledSubjectsByLrn(studentObj.getLrn(), true);
            List<EnrolledSubjectsModel> g12Subjects = subjectsJDBCRepository.findEnrolledSubjectsByLrn(studentObj.getLrn(), false);

            studentObj.setSubjectNmListG11(new ArrayList<>());
            studentObj.setSubjectNmListG12(new ArrayList<>());

            if (g11Subjects != null) {
                for (EnrolledSubjectsModel esm : g11Subjects) {
                    SubjectsModel subject = subMap.get(esm.getSubjectId());
                    if (subject != null && subject.getSubjectNm() != null) {
                        studentObj.getSubjectNmListG11().add(subject.getSubjectNm());
                    } else {
                        System.out.println("Subject not found for G11: " + esm.getSubjectId());
                    }
                }
            }

            if (g12Subjects != null) {
                for (EnrolledSubjectsModel esm : g12Subjects) {
                    SubjectsModel subject = subMap.get(esm.getSubjectId());
                    if (subject != null && subject.getSubjectNm() != null) {
                        studentObj.getSubjectNmListG12().add(subject.getSubjectNm());
                    } else {
                        System.out.println("Subject not found for G12: " + esm.getSubjectId());
                    }
                }
            }
        }else{
            throwErrorMessage("An error occurred upon fetching data.");
        }
        return new StudentReturnModel(studentObj);
    }

    @Override
    public InitializeSubjectModel getSubjectListByKeys(String yearlevelKey, String strandKey) {
        InitializeSubjectModel subjectObjList = new InitializeSubjectModel();
        List<SubjectsModel> subjectsList = subjectsJDBCRepository.getAllSubjectsByKey(yearlevelKey, strandKey);
        if (subjectsList!=null && !subjectsList.isEmpty()) {
            for (SubjectsModel sub : subjectsList) {
                if (yearlevelKey.equals(YearlevelEnum.GRADE11.getKeyStr())) {
                    if (sub.getYearlevelKey().equals(YearlevelEnum.GRADE11.getKeyStr())) {
                        subjectObjList.getGrade11SubjectList().add(sub);
                    }
                    if  (sub.getYearlevelKey().equals(YearlevelEnum.GRADE12.getKeyStr())) {
                        subjectObjList.getGrade12SubjectList().add(sub);
                    }
                }
                if (yearlevelKey.equals(YearlevelEnum.GRADE12.getKeyStr())) {
                    if  (sub.getYearlevelKey().equals(YearlevelEnum.GRADE12.getKeyStr())) {
                        subjectObjList.getGrade12SubjectList().add(sub);
                    }
                }
            }
        }
        return subjectObjList;
    }

    @Override
    public List<KeyValueModelStr> getAllActiveStrandsList() {
        List<KeyValueModelStr> list = new ArrayList<>();
        List<StrandsModel> strandsList = strandsJDBCRepository.findAllActiveStrands();
        if (strandsList!=null && !strandsList.isEmpty()){
            for (StrandsModel strand : strandsList) {
                list.add(new KeyValueModelStr(strand.getStrandKey(), strand.getTrackStrandDscp()));
            }
        }
        return list;
    }

    @Override
    public FacultyModel getAssignedAdviser(String sectionId) {
        FacultyModel adviser = new FacultyModel();
        if (sectionId!=null){
            adviser = facultyJDBCRepository.getBySectionId(sectionId);
        }
        return adviser;
    }

    @Override
    public StudentModel validateEnrollment(EnrollmentRequest requestObj, boolean isInitial) {
        StudentModel studentModel = new StudentModel(requestObj);
        return validate(studentModel, isInitial);
    }

    public StudentModel validate(StudentModel modelObj, boolean isInitial) {
        List<String> errorList = new ArrayList<>();
        validateIsNotInitialFields(modelObj, isInitial, errorList);

        if (modelObj.getFirstNm()==null){
            errorList.add("First Name" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setFirstNm(modelObj.getFirstNm().trim().toUpperCase());
        }

        if (modelObj.getMiddleNm()!=null) {
            checkIfAlphanumericSp(modelObj.getMiddleNm(), "Middle Name", errorList);
            modelObj.setMiddleNm(modelObj.getMiddleNm().trim().toUpperCase());
        }

        if (modelObj.getLastNm()==null){
            errorList.add("Last Name" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setLastNm(modelObj.getLastNm().trim().toUpperCase());
        }

        if (modelObj.getGender()==null) {
            errorList.add("Gender" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setGenderDscp(GenderEnum.getGenderDscpFromKeyStr(modelObj.getGender()));
        }

        if (modelObj.getHomeAddress()==null) {
            errorList.add("Home Address" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setHomeAddress(modelObj.getHomeAddress().trim().toUpperCase());
        }

        if (modelObj.getMobileNo()==null) {
            errorList.add("Mobile Number" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfOnlyNumber(modelObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
        }

        if (modelObj.getEmailAddress()==null) {
            errorList.add("Email Address" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setEmailAddress(modelObj.getEmailAddress().trim());
        }

        if (modelObj.getGuardianFirstNm()==null) {
            errorList.add("Guardian First Name" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfAlphanumericSp(modelObj.getGuardianFirstNm(), "Guardian First Name", errorList);
            modelObj.setGuardianFirstNm(modelObj.getGuardianFirstNm().trim().toUpperCase());
        }

        if (modelObj.getGuardianMiddleNm()!=null) {
            checkIfAlphanumericSp(modelObj.getGuardianMiddleNm(), "Guardian Middle Name", errorList);
            modelObj.setGuardianMiddleNm(modelObj.getGuardianMiddleNm().trim().toUpperCase());
        }

        if (modelObj.getGuardianLastNm()==null) {
            errorList.add("Guardian Last Name" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfAlphanumericSp(modelObj.getGuardianLastNm(), "Guardian Last Name", errorList);
            modelObj.setGuardianLastNm(modelObj.getGuardianLastNm().trim().toUpperCase());
        }

        if (modelObj.getYearlevelKey()==null) {
            errorList.add("Year Level Key" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setYearlevelDscp(YearlevelEnum.getDscpByKeyStr(modelObj.getYearlevelKey()));
        }

        if (modelObj.getStrandKey()==null) {
            errorList.add("Strand Key" + IS_REQUIRED_SUFFIX);
        }else{
            StrandsModel strandsObj = strandsJDBCRepository.findDtlsByKey(modelObj.getStrandKey());
            if (strandsObj!=null) {
                modelObj.setStrandKeyDscp(strandsObj.getTrackStrandDscp());
            }else{
                errorList.add("Invalid strand.");
            }
        }

        if (modelObj.getSectionId()==null) {
            errorList.add("Section" + IS_REQUIRED_SUFFIX);
        }else{
            SectionModel sectionObj = sectionJDBCRepository.findDtlsById(modelObj.getSectionId());
            if (sectionObj!=null) {
                modelObj.setSectionNm(sectionObj.getSectionNmStrand());
            }
        }

        if (modelObj.getStatus()==null) {
            modelObj.setStatus(SystemStatusEnum.UNDER_GRADUATE.getKey());
            modelObj.setStatusDscp(SystemStatusEnum.getDscpByKey(modelObj.getStatus()));
        }else{
            modelObj.setStatusDscp(SystemStatusEnum.getDscpByKey(modelObj.getStatus()));
        }

        if (modelObj.getDateEnrolled()==null) {
            modelObj.setDateEnrolled(new Date());
            modelObj.setDateEnrolledDscp(DateUtil.getDateStringWithFormat(modelObj.getDateEnrolled(), DateFormatEnum.DT_FORMAT_7.getPattern()));
        }

        if (modelObj.getBday()==null) {
            errorList.add("Birthday" + IS_REQUIRED_SUFFIX);
        }else{
            boolean isValidDt = DateUtil.checkValidDateFrom(modelObj.getBday(), 10);
            if (isValidDt) {
                modelObj.setBdayDscp(DateUtil.getDateStringWithFormat(modelObj.getBday(), DateFormatEnum.DT_FORMAT_1.getPattern()));
            }else{
                errorList.add("Invalid birth date.");
            }
        }

        if (!errorList.isEmpty()) throwErrorMessages(errorList);

        return modelObj;
    }

    private void validateIsNotInitialFields(StudentModel modelObj, boolean isInitial,  List<String> errorList) {
        if (!isInitial) {
            if (modelObj.getEnrolleeTypeKey()==null) {
                errorList.add("Enrollee Type" + IS_REQUIRED_SUFFIX);
            }else{
                modelObj.setEnrolleeTypeDscp(EnrolleeTypeEnum.getEnrolleeTypeDscpByKey(modelObj.getEnrolleeTypeKey()));
            }

            if (modelObj.getLrn()!=null) {
                maxStringCharCounter(modelObj.getLrn(), 12, "LRN", errorList);
                minStringCharCounter(modelObj.getLrn(), 12, "LRN", errorList);
                checkIfOnlyNumber(modelObj.getLrn(), "LRN", errorList);
                if (studentJDBCRepository.countStudentByLrn(modelObj.getLrn())>0) {
                    errorList.add("The LRN was already enrolled in the system.");
                }
            }else{
                errorList.add("LRN" + IS_REQUIRED_SUFFIX);
            }

            if (modelObj.getEnrolledSubjects()!=null && !modelObj.getEnrolledSubjects().isEmpty()) {
                List<SubjectsModel> subjectsFromDb = subjectsJDBCRepository.getAllSubjectsByKey(modelObj.getYearlevelKey(), modelObj.getStrandKey());
                HashMap<String, SubjectsModel> subMap = new HashMap<>();
                for (SubjectsModel sub : subjectsFromDb) {
                    subMap.put(sub.getId(), sub);
                }
                modelObj.setSubjectObjList(new ArrayList<>());
                modelObj.setSubjectNmListG11(new ArrayList<>());
                modelObj.setSubjectNmListG12(new ArrayList<>());
                for (String key : modelObj.getEnrolledSubjects()) {
                    modelObj.getSubjectObjList().add(subMap.get(key));
                    if (subMap.get(key).getYearlevelKey().equals(YearlevelEnum.GRADE11.getKeyStr())) {
                        modelObj.getSubjectNmListG11().add(subMap.get(key).getSubjectNm());
                    } else if (subMap.get(key).getYearlevelKey().equals(YearlevelEnum.GRADE12.getKeyStr())) {
                        modelObj.getSubjectNmListG12().add(subMap.get(key).getSubjectNm());
                    }
                }
            }else{
                errorList.add(StringMessagesUtils.NO_SUBJECTS_WERE_FOUND);
            }
        }
    }

    @Override
    public StudentReturnModel saveEnrollment(EnrollmentRequest requestObj) {
        StudentReturnModel returnModelObj;
        StudentModel modelObj = validateEnrollment(requestObj, false);
        boolean hasSaved = studentJDBCRepository.saveEnrollment(modelObj)>0;

        if (hasSaved) {
            try {
                saveStudentLoginCreds(modelObj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (modelObj.getSubjectObjList()!=null && !modelObj.getSubjectObjList().isEmpty()) {
                List<EnrolledSubjectsModel> grade11Subs = new ArrayList<>();
                List<EnrolledSubjectsModel> grade12Subs = new ArrayList<>();

                for (SubjectsModel subModel : modelObj.getSubjectObjList()) {
                    EnrolledSubjectsModel esModel = new EnrolledSubjectsModel();
                    esModel.setLrn(modelObj.getLrn());
                    esModel.setSubjectId(subModel.getId());
                    esModel.setFirstGrade(null);
                    esModel.setSecondGrade(null);
                    esModel.setVersion(0);

                    if (subModel.getYearlevelKey().equals(YearlevelEnum.GRADE11.getKeyStr())) {
                        grade11Subs.add(esModel);
                    }else if(subModel.getYearlevelKey().equals(YearlevelEnum.GRADE12.getKeyStr())) {
                        grade12Subs.add(esModel);
                    }
                }

                if (!grade11Subs.isEmpty()) {
                    for (EnrolledSubjectsModel esModel : grade11Subs) {
                        subjectsJDBCRepository.saveSubjects(true, esModel);
                    }
                }
                if (!grade12Subs.isEmpty()) {
                    for (EnrolledSubjectsModel esModel : grade12Subs) {
                        subjectsJDBCRepository.saveSubjects(false, esModel);
                    }
                }
            }
        }

        modelObj.setAckMessage(StringMessagesUtils.formatMsgString(StringMessagesUtils.SAVED_SINGLE_SUFFIX, SystemUserEnum.STUDENT.getDscp()));
        modelObj.setRefNo(generateReferenceNumber(ServicesEnum.ADD_STUDENT.getCode()));
        modelObj.setCreatedBy("SYSTEM");
        modelObj.setTxnDscp(ServicesEnum.ADD_STUDENT.getServiceDscp());
        returnModelObj = new StudentReturnModel(modelObj);
        auditLogService.saveAuditLog(returnModelObj);

        return returnModelObj;
    }

    public void saveStudentLoginCreds(StudentModel modelObj) throws Exception {
        LoginCreds loginCreds = new LoginCreds();
        loginCreds.setUserId(modelObj.getId());
        loginCreds.setCd(modelObj.getLrn());
        loginCreds.setLoginStatus(SystemStatusEnum.LOGGED_OUT.getKey());
        loginCreds.setRole(SystemUserEnum.STUDENT.getKey());
        loginCreds.setSalt(KeyHasher.generateSalt());
        loginCreds.setPassword(KeyHasher.hashPassword(KeyHasher.generateDefaultPassword(), loginCreds.getSalt()));
        loginCreds.setUpdatedDt(null);
        loginJDBCRepository.saveLoginCreds(loginCreds);
    }

    @Override
    public StudentReturnModel deleteByLrn(String lrn) {
        StudentReturnModel returnObj =  new StudentReturnModel();
        List<String> errorList = new ArrayList<>();
        if (lrn!=null) {
            StudentModel studentObj = studentJDBCRepository.findByLrn(lrn);
            if (studentObj!=null) {
                studentJDBCRepository.deleteByLrn(lrn);
                loginJDBCRepository.deleteByUserCd(lrn);
                subjectsJDBCRepository.deleteByLrn(lrn, true);
                subjectsJDBCRepository.deleteByLrn(lrn, false);
            }else{
                errorList.add(StringMessagesUtils.formatMsgString(StringMessagesUtils.NO_STUDENT_LRN_WAS_FOUND, lrn));
            }
        }

        if (!errorList.isEmpty()) { throwErrorMessages(errorList); }

        returnObj.setTxnDscp(ServicesEnum.DELETE_STUDENT.getServiceDscp());
        returnObj.setAckMessage(StringMessagesUtils.formatMsgString(StringMessagesUtils.DELETED_SINGLE_SUFFIX, SystemUserEnum.STUDENT.getDscp()));
        returnObj.setRefNo(generateReferenceNumber(ServicesEnum.DELETE_STUDENT.getCode()));
        returnObj.setCreatedBy("SYSTEM");
        return returnObj;
    }

    @Override
    public Page<StudentReturnModel> searchStudent(StudentSearchRequest requestObj, PageRequest page) {
        Page<StudentModel> studentModels = studentJDBCRepository.searchStudent(requestObj, page);
        return studentModels.map(StudentReturnModel::new);
    }
}
