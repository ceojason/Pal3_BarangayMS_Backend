package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.jdbc.repository.HouseholdJDBCRepository;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class HouseholdServiceImpl extends BaseServiceImpl implements HouseholdService {

    private final HouseholdJDBCRepository householdJDBCRepository;

    @Override
    public String saveNewHousehold(HouseholdModel household) {
        household.setCreatedDt(new Date());
        return householdJDBCRepository.save(household);
    }

    @Override
    public List<KeyValueModelStr> findAllActiveHouseholdForRegistration(String block, String lot, Integer phaseKey) {
        int status = SystemStatusEnum.ACTIVE.getKey();
        if (block!=null) {
            block = block.trim().toUpperCase();
        }
        if (lot!=null) {
            lot = lot.trim().toUpperCase();
        }
        List<HouseholdModel> householdList = householdJDBCRepository.findHouseholdByHeadAndStatus(status, block, lot, phaseKey);
        List<KeyValueModelStr> returnList = new ArrayList<>();
        if (householdList!=null && !householdList.isEmpty()) {
            for (HouseholdModel modelObj : householdList) {
                returnList.add(new KeyValueModelStr(modelObj.getId(), modelObj.getHouseholdWithHead()));
            }
            returnList.sort(Comparator.comparing(KeyValueModelStr::getValue));
        }
        return returnList;
    }
}
