package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.helper.JwtUtil;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.CommReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/commReport")
@AllArgsConstructor
public class CommReportController {

    private CommReportService commReportService;

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody MainSearchRequest searchRequest) {
        return new ApiResponseModel(commReportService.searchRequests(searchRequest, searchRequest.getPageRequest()));
    }

    @PostMapping(value = "/validateRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseModel validateRequest(@ModelAttribute EnrollmentRequest requestObj, @RequestHeader("Authorization") String authHeader) throws IOException {
        if (authHeader==null || !authHeader.startsWith("Bearer ")) return new ApiResponseModel("Unauthorized!");
        String token = authHeader.substring(7);
        LoginCreds login;

        try {
            login = JwtUtil.parseToken(token);
        } catch (Exception e) {
            return new ApiResponseModel("Invalid token!");
        }

        return new ApiResponseModel(commReportService.validateRequest(requestObj, login.getUserId()));
    }

    @PostMapping(value = "/saveRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseModel saveRequest(@ModelAttribute EnrollmentRequest requestObj, @RequestHeader("Authorization") String authHeader) throws IOException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ApiResponseModel("Unauthorized!");
        }

        String token = authHeader.substring(7);
        LoginCreds login;

        try {
            login = JwtUtil.parseToken(token);
        } catch (Exception e) {
            return new ApiResponseModel("Invalid token!");
        }

        return new ApiResponseModel(commReportService.saveRequest(requestObj, login.getUserId()));
    }

    @GetMapping("/getBrgyOfficialList")
    public ApiResponseModel getBrgyOfficialList() {
        return new ApiResponseModel(commReportService.getBrgyOfficialList());
    }

    @PostMapping("/update")
    public ApiResponseModel update(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(commReportService.update(requestObj));
    }
}
