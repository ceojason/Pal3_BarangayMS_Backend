package com.javaguides.sps.controller;

import com.javaguides.sps.helper.ApiResponseModel;
import com.javaguides.sps.service.EnumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enumApi")
public class EnumController {
    private EnumService enumService;

    public EnumController(EnumService enumService) {
        this.enumService = enumService;
    }

    @GetMapping("/getServiceList")
    public ApiResponseModel getServiceList() {
        return new ApiResponseModel(enumService.getServiceList());
    }

    @GetMapping("/getEnrolleeTypeList")
    public ApiResponseModel getEnrolleeTypeList() {
        return new ApiResponseModel(enumService.getEnrolleeTypeList());
    }

    @GetMapping("/getServiceListForNav")
    public ApiResponseModel getServiceListForNav() {
        return new ApiResponseModel(enumService.getServiceListForNav());
    }

    @GetMapping("/getGenderListStr")
    public ApiResponseModel getGenderListStr() {
        return new ApiResponseModel(enumService.getGenderListStr());
    }

    @GetMapping("/getYearlevelList")
    public ApiResponseModel getYearlevelList() {
        return new ApiResponseModel(enumService.getYearlevelList());
    }
}
