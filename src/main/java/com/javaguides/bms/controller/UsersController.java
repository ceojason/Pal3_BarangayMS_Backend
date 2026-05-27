package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.ResetUserRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody MainSearchRequest searchRequest) {
        return new ApiResponseModel(usersService.searchUsers(searchRequest, searchRequest.getPageRequest()));
    }

    @PostMapping("/validateEnrollment")
    public ApiResponseModel validateEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.validateEnrollment(requestObj));
    }

    @PostMapping("/createHouseholdForRegistration")
    public ApiResponseModel createHouseholdForRegistration(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.createHouseholdForRegistration(requestObj));
    }

    @PostMapping("/saveEnrollment")
    public ApiResponseModel saveEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.saveEnrollment(requestObj));
    }

    @PostMapping("/update")
    public ApiResponseModel update(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.update(requestObj));
    }

    @PostMapping("/updateResident")
    public ApiResponseModel updateResident(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.updateResident(requestObj));
    }

    @PostMapping("/reset")
    public ApiResponseModel reset(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.reset(requestObj));
    }

    @PostMapping("/resetNoSession")
    public ApiResponseModel resetNoSession(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(usersService.resetNoSession(requestObj));
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponseModel delete(@PathVariable String userId) {
        return new ApiResponseModel(usersService.deleteUser(userId));
    }

    @GetMapping("/{userId}")
    public ApiResponseModel findById(@PathVariable String userId) {
        return new ApiResponseModel(usersService.findByUserId(userId));
    }

    @PostMapping("/findUserByRequest")
    public ApiResponseModel findUserByRequest(@RequestBody ResetUserRequest requestObj) {
        return new ApiResponseModel(usersService.findUserByRequest(requestObj));
    }

    /* ===================== PROFILE IMAGE ===================== */

    @PostMapping("/{userId}/profile-image")
    public ApiResponseModel uploadProfileImage(@PathVariable String userId, @RequestParam("profileImage") MultipartFile file) {
        usersService.saveProfileImage(userId, file);
        return new ApiResponseModel("Your profile photo was uploaded successfully.");
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String userId) {
        Resource image = usersService.loadProfileImage(userId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}