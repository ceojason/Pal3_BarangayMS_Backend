package com.javaguides.bms.helper;

import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    /**
     * Returns a 400 (Bad Request) if errors exist, otherwise 200 (OK).
     */
    public static ResponseEntity<ApiResponseModel> build(ApiResponseModel response) {
        if (response.getErrorList() != null && !response.getErrorList().isEmpty()) {
            response.setContent(null);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
