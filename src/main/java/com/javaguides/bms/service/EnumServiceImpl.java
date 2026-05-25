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
    public InitializeEnumModel getPriorityList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setReportPriorityList(PriorityEnum.getPriorityList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getStatusListForCommReport() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setStatusListForCommReport(SystemStatusEnum.getStatusListForCommReport());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getStatusListForHousehold() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setStatusListForHousehold(SystemStatusEnum.getStatusListForHousehold());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getCommReportTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setReportTypeList(CommReportTypeEnum.getCommReportTypeList());
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
    public InitializeEnumModel getConfigList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setSystemConfigList(SystemConfigEnum.configList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getRegionList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setRegionList(RegionEnum.regionList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getResidentTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setResidentTypeList(ResidentClassificationEnum.getResidentClassList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getBrgyPositionList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setBrgyPositionList(BrgyPositionEnum.getBrgyPositionList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getAllResidentTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setAllResidentTypeList(ResidentClassificationEnum.getAllResidentClassList());
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

    @Override
    public InitializeEnumModel getSmsTypeList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setSmsTypeList(LogsTypeEnum.getTypeList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getAlertStatusList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setAlertStatusList(AlertStatusEnum.getTypeList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getChannelList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setChannelList(ChannelEnum.getChannelList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getDocumentList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setDocumentTypeList(DocumentTypeEnum.getDocumentList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getPurposeKeyList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setPurposeKeyList(DocumentPurposeEnum.getPurposeKeyList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getDocuCategoryList() {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setDocuCategoryList(DocumentCategoryEnum.getDocumentCatList());
        return enumModel;
    }

    @Override
    public InitializeEnumModel getDocuSubCatListByKey(Integer key) {
        InitializeEnumModel enumModel = new InitializeEnumModel();
        enumModel.setDocuSubCategoryList(DocumentSubCatEnum.getDocuSubCatList(key));
        return enumModel;
    }
}
