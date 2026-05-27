package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface HouseholdJDBCRepository {
    String save(HouseholdModel modelObj);

    int update(HouseholdModel modelObj);

    Optional<HouseholdModel> findDuplicateHousehold(String householdDesc);

    List<HouseholdModel> findDuplicateHouseholdList(String uniqKey);

    Page<HouseholdModel> search(MainSearchRequest requestObj, PageRequest page);

    Optional<HouseholdModel> findById(String id);

    List<HouseholdModel> findHouseholdByHeadAndStatus(Integer status, String block, String lot, Integer phaseKey);

    List<HouseholdModel> findHousehold(Integer status, String block, String lot, Integer phaseKey);
}
