package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.StudentModel;
import com.javaguides.bms.model.requestmodel.searchrequest.StudentSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StudentJDBCRepository {
    StudentModel findById(String id);

    StudentModel findByLrn(String lrn);

    Long countStudentByLrn(String lrn);

    int saveEnrollment(StudentModel modelObj);

    int deleteByLrn(String lrn);

    Page<StudentModel> searchStudent(StudentSearchRequest requestObj, PageRequest page);
}
