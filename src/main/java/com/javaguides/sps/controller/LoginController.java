package com.javaguides.sps.controller;

import com.javaguides.sps.helper.ApiResponseModel;
import com.javaguides.sps.model.requestmodel.LoginCredsRequest;
import com.javaguides.sps.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ApiResponseModel login(@RequestBody LoginCredsRequest loginCredsRequest, HttpSession session) throws Exception {
        return new ApiResponseModel(loginService.login(loginCredsRequest, session));
    }


}
