package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.SectionModel;

import java.util.List;

public interface SectionJDBCRepository {
    List<SectionModel> getAllActiveSectionsByKey(String yearlevelKey, String strandKey);

    SectionModel findDtlsById(String sectionId);
}
