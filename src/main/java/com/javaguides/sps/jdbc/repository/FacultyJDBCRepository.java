package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.FacultyModel;

public interface FacultyJDBCRepository {
    FacultyModel getBySectionId(String sectionId);
}
