package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.SectionModel;

import java.util.List;

public interface SectionJDBCRepository {
    List<SectionModel> getAllActiveSectionsByKey(String yearlevelKey, String strandKey);

    SectionModel findDtlsById(String sectionId);
}
