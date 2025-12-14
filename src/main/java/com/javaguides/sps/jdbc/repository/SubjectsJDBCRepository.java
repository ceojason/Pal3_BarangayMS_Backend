package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.EnrolledSubjectsModel;
import com.javaguides.sps.model.SubjectsModel;

import java.util.List;

public interface SubjectsJDBCRepository {
    List<SubjectsModel> getAllSubjectsByKey(String yearlevelKey, String strandKey);

    List<EnrolledSubjectsModel> findEnrolledSubjectsByLrn(String lrn, boolean isGrade11);

    int saveSubjects(boolean isGrade11, EnrolledSubjectsModel enrolledSubjectsModel);

    int deleteByLrn(String lrn, boolean isGrade11);

    int deleteByIdList(List<String> idList, boolean isGrade11);
}
