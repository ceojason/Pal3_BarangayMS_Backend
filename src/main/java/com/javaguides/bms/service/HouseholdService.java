package com.javaguides.bms.service;

import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.HouseholdReturnModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface HouseholdService {
    String saveNewHousehold(HouseholdModel household);

    Page<HouseholdReturnModel> search(MainSearchRequest searchRequest, PageRequest pageRequest);

    List<KeyValueModelStr> findAllActiveHouseholdForRegistration(String block, String lot, String street, Integer phaseKey);

    List<KeyValueModelStr> findMembersById(String id);

    HouseholdReturnModel update(EnrollmentRequest requestObj);
}
