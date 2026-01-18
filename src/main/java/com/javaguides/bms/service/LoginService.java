package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.model.returnmodel.LoginCredsReturn;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    LoginCredsReturn login(LoginCredsRequest loginRequest, HttpSession session) throws Exception;
}
