package com.javaguides.bms.service;

import com.javaguides.bms.model.modelhelper.InitializeEnumModel;

public interface EnumService {
    InitializeEnumModel getServiceList();

    InitializeEnumModel getServiceListForNav();

    InitializeEnumModel getSystemUserListForLogin();

    InitializeEnumModel getGenderListStr();

    InitializeEnumModel getPriorityList();

    InitializeEnumModel getStatusListForCommReport();

    InitializeEnumModel getStatusListForHousehold();

    InitializeEnumModel getCommReportTypeList();

    InitializeEnumModel getCivilStatusList();

    InitializeEnumModel getPhaseList();

    InitializeEnumModel getYesNoList();

    InitializeEnumModel getConfigList();

    InitializeEnumModel getRegionList();

    InitializeEnumModel getResidentTypeList();

    InitializeEnumModel getBrgyPositionList();

    InitializeEnumModel getAllResidentTypeList();

    InitializeEnumModel getYearlevelList();

    InitializeEnumModel getEnrolleeTypeList();

    InitializeEnumModel getSmsTypeList();

    InitializeEnumModel getAlertStatusList();

    InitializeEnumModel getChannelList();

    InitializeEnumModel getDocumentList();

    InitializeEnumModel getPurposeKeyList();

    InitializeEnumModel getDocuCategoryList();

    InitializeEnumModel getDocuSubCatListByKey(Integer key);
}
