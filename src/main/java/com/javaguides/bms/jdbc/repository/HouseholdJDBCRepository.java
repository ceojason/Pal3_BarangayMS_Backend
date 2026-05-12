package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.HouseholdModel;

import java.util.List;
import java.util.Optional;

public interface HouseholdJDBCRepository {
    String save(HouseholdModel modelObj);

    Optional<HouseholdModel> findDuplicateHousehold(String householdDesc);

    Optional<HouseholdModel> findById(String id);

    List<HouseholdModel> findHouseholdByHeadAndStatus(Integer status, String block, String lot, Integer phaseKey);
}
