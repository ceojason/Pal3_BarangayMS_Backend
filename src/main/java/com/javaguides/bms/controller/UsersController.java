package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.UsersSearchRequest;
import com.javaguides.bms.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private UsersService usersService;

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody UsersSearchRequest searchRequest) {
        return new ApiResponseModel(usersService.searchUsers());
    }

    @PostMapping("/validateEnrollment")
    public ApiResponseModel validateEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.validateEnrollment(requestObj));
    }

    @PostMapping("/saveEnrollment")
    public ApiResponseModel saveEnrollment(@RequestBody EnrollmentRequest requestObj, HttpSession session) {
        return new ApiResponseModel(usersService.saveEnrollment(requestObj, session));
    }
}
