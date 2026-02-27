package com.javaguides.bms.controller;

import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.helper.JwtUtil;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemAdminJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SessionUserModel;
import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.service.LoginService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/login")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginJDBCRepository loginJDBCRepository;
    private final SystemAdminJDBCRepository systemAdminJDBCRepository;
    private final UsersJDBCRepository usersJDBCRepository;

    @PostMapping
    public ApiResponseModel login(@RequestBody LoginCredsRequest request) throws Exception {
        return new ApiResponseModel(loginService.login(request));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpSession session) {
//        session.invalidate(); // destroys the session
//        return ResponseEntity.ok("Logged out successfully");
//    }

    @GetMapping("/session-user")
    public ResponseEntity<?> getSessionUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Unauthorized: Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            Claims claims = JwtUtil.validate(token);

            String userId = claims.get("userId", String.class);
            Integer roleKey = claims.get("role", Integer.class);

            Optional<LoginCreds> loginObj = loginJDBCRepository.getUserById(userId);
            if (loginObj.isEmpty()) {
                return ResponseEntity.status(401).body("Unauthorized: Invalid token user");
            }

            LoginCreds loginCreds = loginObj.get();
            SessionUserModel sessionUser = new SessionUserModel();
            sessionUser.setCd(loginCreds.getCd());
            sessionUser.setUserId(loginCreds.getUserId());
            sessionUser.setLastLoginDt(loginCreds.getUpdatedDt());
            sessionUser.setRoleKey(loginCreds.getRole());

            if (roleKey.equals(SystemUserEnum.SYSTEM_ADMIN.getKey())) {
                Optional<SystemAdminModel> adminObj = systemAdminJDBCRepository.findById(userId);
                adminObj.ifPresent(a -> {
                    sessionUser.setFirstNm(a.getFirstNm());
                    sessionUser.setMiddleNm(a.getMiddleNm());
                    sessionUser.setLastNm(a.getLastNm());
                });
            } else if (roleKey.equals(SystemUserEnum.SYSTEM_USER.getKey())) {
                Optional<UsersModel> userObj = usersJDBCRepository.findById(userId);
                userObj.ifPresent(u -> {
                    sessionUser.setFirstNm(u.getFirstNm());
                    sessionUser.setMiddleNm(u.getMiddleNm());
                    sessionUser.setLastNm(u.getLastNm());
                });
            }

            return ResponseEntity.ok(sessionUser);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }

}