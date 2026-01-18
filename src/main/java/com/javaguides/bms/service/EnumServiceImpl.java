package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.model.modelhelper.InitializeEnumModel;
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
    public InitializeEnumModel getCivilStatusList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setCivilStatusList(CivilStatusEnum.getCivilStatusList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getPhaseList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setPhaseList(PhaseEnum.getPhaseList());
        enumModel.setPurokList(PhaseEnum.getPurokList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getYesNoList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setYesNoList(YesOrNoEnum.getYesNoList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getResidentTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setResidentTypeList(ResidentClassificationEnum.getResidentClassList());
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
