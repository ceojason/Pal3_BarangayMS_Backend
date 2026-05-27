package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.ConfigModel;
import com.javaguides.bms.model.FeePricingModel;

import java.util.List;
import java.util.Optional;

public interface SystemConfigJDBCRepository {
    int updateConfig(ConfigModel modelObj);

    Optional<ConfigModel> findById(String id);

    int updateFeePricing(FeePricingModel modelObj);

    List<FeePricingModel> pricingList();
}
