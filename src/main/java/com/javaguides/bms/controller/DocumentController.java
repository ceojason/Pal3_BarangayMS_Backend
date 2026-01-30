package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/validateRequest")
    public ApiResponseModel validateRequest(@RequestBody DocumentRequest requestObj, HttpSession session) {
        return new ApiResponseModel(documentService.validateRequest(requestObj, session));
    }

    @PostMapping("/saveRequest")
    public ApiResponseModel saveRequest(@RequestBody DocumentRequest requestObj) {
        return new ApiResponseModel(documentService.saveRequest(requestObj));
    }

    @PostMapping("/previewRequest")
    public ApiResponseModel previewRequest(@RequestBody DocumentRequest requestObj, HttpSession session) {
        return new ApiResponseModel(documentService.previewRequest(requestObj, session));
    }

    @PostMapping("/processDocument")
    public ApiResponseModel processDocument(@RequestBody DocumentRequest requestObj) {
        return new ApiResponseModel(documentService.processDocument(requestObj));
    }

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody MainSearchRequest searchRequest) {
        return new ApiResponseModel(documentService.searchRequests(searchRequest, searchRequest.getPageRequest()));
    }

    @GetMapping("/{id}")
    public ApiResponseModel findById(@PathVariable String id) {
        return new ApiResponseModel(documentService.getRequestById(id));
    }
}
