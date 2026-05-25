package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.FeePricingModel;

import java.util.List;

public interface SystemConfigJDBCRepository {
    int updateFeePricing(FeePricingModel modelObj);

    List<FeePricingModel> pricingList();
}
