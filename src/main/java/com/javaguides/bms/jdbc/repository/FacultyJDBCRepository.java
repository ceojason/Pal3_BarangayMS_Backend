package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.FacultyModel;

public interface FacultyJDBCRepository {
    FacultyModel getBySectionId(String sectionId);
}
