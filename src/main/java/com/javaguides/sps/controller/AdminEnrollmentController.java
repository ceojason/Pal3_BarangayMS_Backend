package com.javaguides.sps.controller;

import com.javaguides.sps.helper.ApiResponseModel;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;
import com.javaguides.sps.service.AdminEnrollmentService;
import com.javaguides.sps.service.StudentEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adminEnrollment")
@AllArgsConstructor
public class AdminEnrollmentController {

    private AdminEnrollmentService adminEnrollmentService;

    @PostMapping("/validateEnrollment")
    public ApiResponseModel validateEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(adminEnrollmentService.validateEnrollment(requestObj));
    }

}
