package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.model.returnmodel.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginCredsRequest loginRequest) throws Exception;
}
