package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.service.EnumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/getDocumentList")
    public ApiResponseModel getDocumentList() {
        return new ApiResponseModel(enumService.getDocumentList());
    }

    @GetMapping("/getPurposeKeyList")
    public ApiResponseModel getPurposeKeyList() {
        return new ApiResponseModel(enumService.getPurposeKeyList());
    }

    @GetMapping("/getDocuCategoryList")
    public ApiResponseModel getDocuCategoryList() {
        return new ApiResponseModel(enumService.getDocuCategoryList());
    }

    @GetMapping("/getDocuSubCatListByKey/{key}")
    public ApiResponseModel getDocuSubCatListByKey(@PathVariable Integer key) {
        return new ApiResponseModel(enumService.getDocuSubCatListByKey(key));
    }

    @GetMapping("/getServiceListForNav")
    public ApiResponseModel getServiceListForNav() {
        return new ApiResponseModel(enumService.getServiceListForNav());
    }

    @GetMapping("/getGenderListStr")
    public ApiResponseModel getGenderListStr() {
        return new ApiResponseModel(enumService.getGenderListStr());
    }

    @GetMapping("/getPriorityList")
    public ApiResponseModel getPriorityList() {
        return new ApiResponseModel(enumService.getPriorityList());
    }

    @GetMapping("/getStatusListForCommReport")
    public ApiResponseModel getStatusListForCommReport() {
        return new ApiResponseModel(enumService.getStatusListForCommReport());
    }

    @GetMapping("/getStatusListForHousehold")
    public ApiResponseModel getStatusListForHousehold() {
        return new ApiResponseModel(enumService.getStatusListForHousehold());
    }

    @GetMapping("/getReportTypeList")
    public ApiResponseModel getCommReportTypeList() {
        return new ApiResponseModel(enumService.getCommReportTypeList());
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

    @GetMapping("/getConfigList")
    public ApiResponseModel getConfigList() {
        return new ApiResponseModel(enumService.getConfigList());
    }

    @GetMapping("/getRegionList")
    public ApiResponseModel getRegionList() {
        return new ApiResponseModel(enumService.getRegionList());
    }

    @GetMapping("/getResidentTypeList")
    public ApiResponseModel getResidentTypeList() {
        return new ApiResponseModel(enumService.getResidentTypeList());
    }

    @GetMapping("/getBrgyPositionList")
    public ApiResponseModel getBrgyPositionList() {
        return new ApiResponseModel(enumService.getBrgyPositionList());
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
