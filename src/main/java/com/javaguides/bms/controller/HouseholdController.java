package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.service.HouseholdService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/household")
@AllArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;

    @GetMapping("/findActiveHousehold/{block}/{lot}/{phaseKey}")
    public ApiResponseModel findActiveHousehold(@PathVariable String block, @PathVariable String lot, @PathVariable Integer phaseKey) {
        return new ApiResponseModel(householdService.findAllActiveHouseholdForRegistration(block, lot, phaseKey));
    }

}
