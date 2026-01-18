package com.javaguides.bms.helper;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorException extends RuntimeException {

    private final List<String> errorList;

    public ErrorException(List<String> errorList) {
        super();
        this.errorList = errorList;
    }

}
