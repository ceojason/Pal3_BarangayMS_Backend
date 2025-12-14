package com.javaguides.sps.exception;

import com.javaguides.sps.helper.ApiResponseModel;
import com.javaguides.sps.helper.ErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ApiResponseModel> handleErrorException(ErrorException ex) {
        ApiResponseModel response = new ApiResponseModel();
        response.setErrorList(ex.getErrorList());
        response.setContent(null);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // You can change this to 422 if needed
                .body(response);
    }

    // Optional: fallback for other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseModel> handleGeneralException(Exception ex) {
        ApiResponseModel response = new ApiResponseModel();
        response.setErrorList(java.util.List.of("Unexpected error occurred: " + ex.getMessage()));
        response.setContent(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
