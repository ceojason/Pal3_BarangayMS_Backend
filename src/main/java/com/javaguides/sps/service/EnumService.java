package com.javaguides.sps.service;

import com.javaguides.sps.model.modelhelper.InitializeEnumModel;

public interface EnumService {
    InitializeEnumModel getServiceList();

    InitializeEnumModel getServiceListForNav();

    InitializeEnumModel getSystemUserListForLogin();

    InitializeEnumModel getGenderListStr();

    InitializeEnumModel getYearlevelList();

    InitializeEnumModel getEnrolleeTypeList();
}
