package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.helper.JwtUtil;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.DocumentService;
import com.javaguides.bms.service.ProcessFeeService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@AllArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final ProcessFeeService processFeeService;

    @GetMapping("/getProcessFeeByKey/{key}")
    public ApiResponseModel getProcessFeeByKey(@PathVariable Integer key) {
        return new ApiResponseModel(processFeeService.getProcessFeeByKey(key));
    }

    @PostMapping("/validateRequest")
    public ApiResponseModel validateRequest(@RequestBody DocumentRequest requestObj, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponseModel("Unauthorized");
        }
        String token = authHeader.substring(7);
        LoginCreds loginUser;

        try {
            loginUser = JwtUtil.parseToken(token);
        } catch (Exception e) {
            return new ApiResponseModel("Invalid token");
        }

        return new ApiResponseModel(documentService.validateRequest(requestObj, loginUser.getUserId()));
    }

    @PostMapping("/saveRequest")
    public ApiResponseModel saveRequest(@RequestBody DocumentRequest requestObj) {
        return new ApiResponseModel(documentService.saveRequest(requestObj));
    }

    @PostMapping("/previewRequest")
    public ApiResponseModel previewRequest(@RequestBody DocumentRequest requestObj, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponseModel("Unauthorized");
        }
        String token = authHeader.substring(7);
        LoginCreds loginUser;

        try {
            loginUser = JwtUtil.parseToken(token);
        } catch (Exception e) {
            return new ApiResponseModel("Invalid token");
        }

        return new ApiResponseModel(documentService.previewRequest(requestObj, loginUser.getUserId()));
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
