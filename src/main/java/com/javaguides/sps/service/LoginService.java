package com.javaguides.sps.service;

import com.javaguides.sps.model.requestmodel.LoginCredsRequest;
import com.javaguides.sps.model.returnmodel.LoginCredsReturn;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    LoginCredsReturn login(LoginCredsRequest loginRequest, HttpSession session) throws Exception;
}
