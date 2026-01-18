package com.javaguides.bms.service;

import com.javaguides.bms.model.FacultyModel;
import com.javaguides.bms.model.StudentModel;
import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.StudentSearchRequest;
import com.javaguides.bms.model.returnmodel.InitializeSubjectModel;
import com.javaguides.bms.model.returnmodel.StudentReturnModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface StudentEnrollmentService {
    List<KeyValueModelStr> getSectionListByKeys(String yearlevelKey, String sectionKey);

    StudentReturnModel findByLrn(String lrn);

    InitializeSubjectModel getSubjectListByKeys(String yearlevelKey, String strandKey);

    List<KeyValueModelStr> getAllActiveStrandsList();

    FacultyModel getAssignedAdviser(String sectionId);

    StudentModel validateEnrollment(EnrollmentRequest requestObj, boolean isInitial);

    StudentReturnModel saveEnrollment(EnrollmentRequest requestObj);

    StudentReturnModel deleteByLrn(String lrn);

    Page<StudentReturnModel> searchStudent(StudentSearchRequest requestObj, PageRequest pageRequest);
}
