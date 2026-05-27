package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.service.HouseholdService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/household")
@AllArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;

    @GetMapping("/findActiveHousehold")
    public ApiResponseModel findActiveHousehold(
            @RequestParam String block,
            @RequestParam String lot,
            @RequestParam(required = false) String street,
            @RequestParam Integer phaseKey) {

        return new ApiResponseModel(
                householdService.findAllActiveHouseholdForRegistration(block, lot, street, phaseKey)
        );
    }

    @PostMapping("/search")
    public ApiResponseModel search(@RequestBody MainSearchRequest searchRequest) {
        return new ApiResponseModel(householdService.search(searchRequest, searchRequest.getPageRequest()));
    }

    @GetMapping("/findMembersById/{id}")
    public ApiResponseModel findMembersById(@PathVariable String id) {
        return new ApiResponseModel(householdService.findMembersById(id));
    }

    @PostMapping("/update")
    public ApiResponseModel update(@RequestBody EnrollmentRequest requestObj) {
        return new ApiResponseModel(householdService.update(requestObj));
    }
}
