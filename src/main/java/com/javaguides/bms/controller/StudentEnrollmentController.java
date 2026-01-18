package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.StudentSearchRequest;
import com.javaguides.bms.service.StudentEnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studentEnrollment")
@AllArgsConstructor
public class StudentEnrollmentController {

    private StudentEnrollmentService studentEnrollmentService;

    @GetMapping("/getSectionList/{yearlevelKey}/{strandKey}")
    public ApiResponseModel getSectionList(@PathVariable String yearlevelKey, @PathVariable String strandKey) {
        return new ApiResponseModel(studentEnrollmentService.getSectionListByKeys(yearlevelKey, strandKey));
    }

    @GetMapping("/getSubjectList/{yearlevelKey}/{strandKey}")
    public ApiResponseModel getSubjectList(@PathVariable String yearlevelKey, @PathVariable String strandKey) {
        return new ApiResponseModel(studentEnrollmentService.getSubjectListByKeys(yearlevelKey, strandKey));
    }

    @GetMapping("/getAssignedAdviser/{sectionId}")
    public ApiResponseModel getAssignedAdviser(@PathVariable String sectionId) {
        return new ApiResponseModel(studentEnrollmentService.getAssignedAdviser(sectionId));
    }

    @GetMapping("/findStudentByLrn/{lrn}")
    public ApiResponseModel findStudentByLrn(@PathVariable String lrn) {
        return new ApiResponseModel(studentEnrollmentService.findByLrn(lrn));
    }

    @GetMapping("/getStrandList")
    public ApiResponseModel getStrandsList() {
        return new ApiResponseModel(studentEnrollmentService.getAllActiveStrandsList());
    }

    @PostMapping("/validateEnrollment")
    public ApiResponseModel validateEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(studentEnrollmentService.validateEnrollment(requestObj, true));
    }

    @PostMapping("/validateEnrollmentNotInitial")
    public ApiResponseModel validateEnrollmentNotInitial(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(studentEnrollmentService.validateEnrollment(requestObj, false));
    }

    @PostMapping("/saveEnrollment")
    public ApiResponseModel saveEnrollment(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(studentEnrollmentService.saveEnrollment(requestObj));
    }

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody StudentSearchRequest searchRequest) {
        return new ApiResponseModel(studentEnrollmentService.searchStudent(searchRequest, searchRequest.getPageRequest()));
    }

    @DeleteMapping("/delete/{lrn}")
    public ApiResponseModel deleteStudent(@PathVariable String lrn) {
        return new ApiResponseModel(studentEnrollmentService.deleteByLrn(lrn));
    }

}
