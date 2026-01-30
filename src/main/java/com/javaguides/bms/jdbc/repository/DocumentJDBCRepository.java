package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.DocumentModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface DocumentJDBCRepository {
    int saveRequest(DocumentModel modelObj);

    int updateDocument(DocumentModel modelObj);

    Integer getCount();

    Optional<DocumentModel> findById(String id);

    List<DocumentModel> findPendingRequestByUserIdAndKey(String userId, Integer key);

    Page<DocumentModel> searchRequests(MainSearchRequest requestObj, PageRequest page);
}
