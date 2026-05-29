package com.javaguides.bms.controller;

import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.model.requestmodel.ConfigRequest;
import com.javaguides.bms.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@AllArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/getFeePricingList")
    public ApiResponseModel getFeePricing() {
        return new ApiResponseModel(configService.feePricingList());
    }

    @GetMapping("/getBarangayDetails")
    public ApiResponseModel getBarangayDetails() {
        return new ApiResponseModel(configService.getBarangayDetails());
    }

    @GetMapping("/getHotlines")
    public ApiResponseModel getHotlines() {
        return new ApiResponseModel(configService.getHotlines());
    }

    @GetMapping("/findConfigById/{id}")
    public ApiResponseModel findConfigById(@PathVariable String id) {
        return new ApiResponseModel(configService.findConfigById(id));
    }

    @PostMapping("/validateAndUpdate")
    public ApiResponseModel validateAndUpdate(@RequestBody ConfigRequest requestObj) {
        return new ApiResponseModel(configService.validateAndUpdate(requestObj));
    }
}
