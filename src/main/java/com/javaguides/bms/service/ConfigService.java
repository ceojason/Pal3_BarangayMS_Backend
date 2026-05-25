package com.javaguides.bms.service;

import com.javaguides.bms.model.FeePricingModel;
import com.javaguides.bms.model.requestmodel.ConfigRequest;
import com.javaguides.bms.model.returnmodel.ConfigReturnModel;

import java.util.List;

public interface ConfigService {
    List<FeePricingModel> feePricingList();

    ConfigReturnModel validateAndUpdate(ConfigRequest requestObj);
}
