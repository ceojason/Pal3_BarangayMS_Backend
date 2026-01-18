package com.javaguides.bms.helper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponseModel {

    private Object content;
    private List<String> errorList;

    public ApiResponseModel(Object content) {
        this.content = content;
        this.errorList = null;
    }

    public ApiResponseModel(List<String> errorList) {
        this.errorList = errorList;
        this.content = null;
    }


    public ApiResponseModel() {

    }
}
