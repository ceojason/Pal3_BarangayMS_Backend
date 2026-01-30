package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.DocumentReturnModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DocumentService {
    DocumentReturnModel validateRequest(DocumentRequest documentRequest, HttpSession session);

    DocumentReturnModel saveRequest(DocumentRequest documentRequest);

    String previewRequest(DocumentRequest requestObj, HttpSession session);

    Page<DocumentReturnModel> searchRequests(MainSearchRequest searchRequest, PageRequest pageRequest);

    DocumentReturnModel getRequestById(String id);

    DocumentReturnModel processDocument(DocumentRequest request);
}
