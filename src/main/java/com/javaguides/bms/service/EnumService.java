package com.javaguides.bms.service;

import com.javaguides.bms.model.modelhelper.InitializeEnumModel;

public interface EnumService {
    InitializeEnumModel getServiceList();

    InitializeEnumModel getServiceListForNav();

    InitializeEnumModel getSystemUserListForLogin();

    InitializeEnumModel getGenderListStr();

    InitializeEnumModel getCivilStatusList();

    InitializeEnumModel getPhaseList();

    InitializeEnumModel getYesNoList();

    InitializeEnumModel getResidentTypeList();

    InitializeEnumModel getYearlevelList();

    InitializeEnumModel getEnrolleeTypeList();
}
