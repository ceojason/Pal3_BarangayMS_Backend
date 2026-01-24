package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.AnnouncementService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcement")
@AllArgsConstructor
public class AnnouncementController {

    private AnnouncementService announcementService;

    @PostMapping("/validateRequest")
    public ApiResponseModel validateRequest(@RequestBody EnrollmentRequest request) {
        return new ApiResponseModel(announcementService.validateRequest(request));
    }

    @PostMapping("/saveRequest")
    public ApiResponseModel saveRequest(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(announcementService.saveRequest(requestObj));
    }

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody MainSearchRequest searchRequest) {
        return new ApiResponseModel(announcementService.searchAnnouncement(searchRequest, searchRequest.getPageRequest()));
    }

}
