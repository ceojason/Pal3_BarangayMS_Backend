package com.javaguides.sps.service;

import com.javaguides.sps.model.FacultyModel;
import com.javaguides.sps.model.StudentModel;
import com.javaguides.sps.helper.KeyValueModelStr;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;
import com.javaguides.sps.model.requestmodel.searchrequest.StudentSearchRequest;
import com.javaguides.sps.model.returnmodel.InitializeSubjectModel;
import com.javaguides.sps.model.returnmodel.StudentReturnModel;
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
