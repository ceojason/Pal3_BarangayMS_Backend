package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.DocumentModel;

import java.util.List;
import java.util.Optional;

public interface DocumentJDBCRepository {
    int saveRequest(DocumentModel modelObj);

    Integer getCount();

    Optional<DocumentModel> findById(String id);

    List<DocumentModel> findPendingRequestByUserIdAndKey(String userId, Integer key);
}
