package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.service.EnumService;
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

    @GetMapping("/getSmsTypeList")
    public ApiResponseModel getSmsTypeList() {
        return new ApiResponseModel(enumService.getSmsTypeList());
    }

    @GetMapping("/getAlertStatusList")
    public ApiResponseModel getAlertStatusList() {
        return new ApiResponseModel(enumService.getAlertStatusList());
    }

    @GetMapping("/getChannelList")
    public ApiResponseModel getChannelList() {
        return new ApiResponseModel(enumService.getChannelList());
    }

    @GetMapping("/getServiceListForNav")
    public ApiResponseModel getServiceListForNav() {
        return new ApiResponseModel(enumService.getServiceListForNav());
    }

    @GetMapping("/getGenderListStr")
    public ApiResponseModel getGenderListStr() {
        return new ApiResponseModel(enumService.getGenderListStr());
    }

    @GetMapping("/getCivilStatusList")
    public ApiResponseModel getCivilStatusList() {
        return new ApiResponseModel(enumService.getCivilStatusList());
    }

    @GetMapping("/getPhaseList")
    public ApiResponseModel getPhaseList() {
        return new ApiResponseModel(enumService.getPhaseList());
    }

    @GetMapping("/getYesNoList")
    public ApiResponseModel getYesNoList() {
        return new ApiResponseModel(enumService.getYesNoList());
    }

    @GetMapping("/getResidentTypeList")
    public ApiResponseModel getResidentTypeList() {
        return new ApiResponseModel(enumService.getResidentTypeList());
    }

    @GetMapping("/getAllResidentTypeList")
    public ApiResponseModel getAllResidentTypeList() {
        return new ApiResponseModel(enumService.getAllResidentTypeList());
    }

    @GetMapping("/getYearlevelList")
    public ApiResponseModel getYearlevelList() {
        return new ApiResponseModel(enumService.getYearlevelList());
    }
}
