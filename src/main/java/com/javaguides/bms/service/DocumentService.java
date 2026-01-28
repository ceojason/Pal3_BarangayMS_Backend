package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.returnmodel.DocumentReturnModel;
import jakarta.servlet.http.HttpSession;

public interface DocumentService {
    DocumentReturnModel validateRequest(DocumentRequest documentRequest, HttpSession session);

    DocumentReturnModel saveRequest(DocumentRequest documentRequest);

    String previewRequest(DocumentRequest requestObj, HttpSession session);
}
