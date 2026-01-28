package com.javaguides.bms.model.modelhelper;

import com.javaguides.bms.helper.KeyValueBoolModelStr;
import com.javaguides.bms.helper.KeyValueModel;
import com.javaguides.bms.helper.KeyValueModelStr;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class InitializeEnumModel {

    List<KeyValueModelStr> serviceList;
    List<KeyValueBoolModelStr> serviceListForNav;
    List<KeyValueModel> systemUserList;
    List<KeyValueModelStr> genderListStr;
    List<KeyValueModelStr> yearlevelList;
    List<KeyValueModel> enrolleeTypeList;
    List<KeyValueModel> civilStatusList;
    List<KeyValueModel> phaseList;
    List<KeyValueModel> purokList;
    List<KeyValueModel> yesNoList;
    List<KeyValueModel> residentTypeList;
    List<KeyValueModel> allResidentTypeList;
    List<KeyValueModel> alertStatusList;
    List<KeyValueModel> smsTypeList;
    List<KeyValueModel> channelList;
    List<KeyValueModel> documentTypeList;
}
