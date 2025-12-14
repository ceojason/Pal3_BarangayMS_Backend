package com.javaguides.sps.controller;

import com.javaguides.sps.enums.SystemUserEnum;
import com.javaguides.sps.model.DummyModel;
import com.javaguides.sps.helper.ApiResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/testingApi")
public class DummyController {

    @GetMapping("/test")
    public String testingApi() {
        return "THIS IS A RESPONSE FROM BACKEND";
    }

    @GetMapping("/getDummyUser")
    public ApiResponseModel getDummyUser() {
        DummyModel dummyModel = new DummyModel();
        dummyModel.setId("DUMMYUSER");
        dummyModel.setFirstName("GOJO");
        dummyModel.setLastName("SATORU");
        dummyModel.setFullName(dummyModel.getDummyFullNm());
        dummyModel.setRole(SystemUserEnum.SYSTEM_ADMIN.getKey());
        dummyModel.setRoleDscp(SystemUserEnum.SYSTEM_ADMIN.getDscp());
        dummyModel.setLastLoginDt(new Date());
        dummyModel.setLastLoginDtString(dummyModel.getLastLoginDtString());
        return new ApiResponseModel(dummyModel);
    }

}
