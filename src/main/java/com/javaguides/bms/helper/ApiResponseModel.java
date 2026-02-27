package com.javaguides.bms.helper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiResponseModel {

    private Object content;
    private List<String> errorList;
    private String code;

    public ApiResponseModel(Object content) {
        this.content = content;
        this.errorList = null;
        this.code = "00";
    }

    public ApiResponseModel(List<String> errorList) {
        this.errorList = errorList;
        this.content = null;
        this.code = "404";
    }


    public ApiResponseModel() {

    }
}
