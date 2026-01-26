package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.service.AdminEnrollmentService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/administrator")
@AllArgsConstructor
public class AdminEnrollmentController {

    private AdminEnrollmentService adminEnrollmentService;

    @PostMapping("/validateEnrollment")
    public ApiResponseModel validateEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(adminEnrollmentService.validateEnrollment(requestObj));
    }

    @GetMapping("/{userId}")
    public ApiResponseModel findById(@PathVariable String userId) {
        return new ApiResponseModel(adminEnrollmentService.findByUserId(userId));
    }

    @PostMapping("/{userId}/profile-image")
    public ApiResponseModel uploadProfileImage(@PathVariable String userId, @RequestParam("profileImage") MultipartFile file) {
        adminEnrollmentService.saveProfileImage(userId, file);
        return new ApiResponseModel("Your profile photo was uploaded successfully.");
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String userId) {
        Resource image = adminEnrollmentService.loadProfileImage(userId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @PostMapping("/update")
    public ApiResponseModel update(@RequestBody EnrollmentRequest requestObj, HttpSession session) {
        return new ApiResponseModel(adminEnrollmentService.update(requestObj, session));
    }
}
