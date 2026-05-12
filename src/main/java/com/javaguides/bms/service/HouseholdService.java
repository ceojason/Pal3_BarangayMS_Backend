package com.javaguides.bms.service;

import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.model.HouseholdModel;

import java.util.List;

public interface HouseholdService {
    String saveNewHousehold(HouseholdModel household);

    List<KeyValueModelStr> findAllActiveHouseholdForRegistration(String block, String lot, Integer phaseKey);
}
