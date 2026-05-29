package com.javaguides.bms.service;

import com.javaguides.bms.model.ConfigModel;
import com.javaguides.bms.model.FeePricingModel;
import com.javaguides.bms.model.requestmodel.ConfigRequest;
import com.javaguides.bms.model.returnmodel.ConfigReturnModel;

import java.util.List;
import java.util.Optional;

public interface ConfigService {
    List<FeePricingModel> feePricingList();

    String getAddressConfigObj();

    Optional<ConfigModel> findConfigById(String id);

    ConfigReturnModel getHotlines();

    ConfigReturnModel getBarangayDetails();

    ConfigReturnModel validateAndUpdate(ConfigRequest requestObj);
}
