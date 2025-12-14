package com.javaguides.sps.model.modelhelper;

import com.javaguides.sps.helper.KeyValueBoolModelStr;
import com.javaguides.sps.helper.KeyValueModel;
import com.javaguides.sps.helper.KeyValueModelStr;
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

}
