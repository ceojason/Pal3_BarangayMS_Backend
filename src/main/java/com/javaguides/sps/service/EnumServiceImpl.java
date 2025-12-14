package com.javaguides.sps.service;

import com.javaguides.sps.enums.*;
import com.javaguides.sps.model.modelhelper.InitializeEnumModel;
import org.springframework.stereotype.Service;

@Service
public class EnumServiceImpl implements EnumService {

    @Override
    public InitializeEnumModel getServiceList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setServiceList(ServicesEnum.getServicesList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getServiceListForNav() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setServiceListForNav(ServicesEnum.getServicesListForNav());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getSystemUserListForLogin() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setSystemUserList(SystemUserEnum.getSystemUserEnumList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getGenderListStr() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setGenderListStr(GenderEnum.getGenderListStr());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getYearlevelList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setYearlevelList(YearlevelEnum.getYearlevelList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getEnrolleeTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setEnrolleeTypeList(EnrolleeTypeEnum.getEnrolleeTypeList());
        return enumModel;
    }

}
