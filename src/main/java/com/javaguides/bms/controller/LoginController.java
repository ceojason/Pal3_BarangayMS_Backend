package com.javaguides.bms.controller;

import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.helper.ApiResponseModel;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemAdminJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SessionUserModel;
import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ApiResponseModel login(@RequestBody LoginCredsRequest loginCredsRequest, HttpSession session) throws Exception {
        return new ApiResponseModel(loginService.login(loginCredsRequest, session));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // destroys the session
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/session-user")
    public ResponseEntity<?> getSessionUser(HttpSession session) {
        Object userObj = session.getAttribute("user");
        if (userObj!=null) {
            LoginCreds user = (LoginCreds) userObj;
            List<LoginCreds> list = loginJDBCRepository.getUserByCd(user.getCd());
            if (list==null || list.isEmpty()) {
                return ResponseEntity.status(401).body("Unauthorized: No user in session");
            } else if (list.size()>1) {
                return ResponseEntity.status(401).body("Unauthorized: No user in session");
            } else {
                LoginCreds loginCreds = list.get(0);
                //##########################
                //      System Admin
                //##########################
                if (loginCreds.getRole().equals(SystemUserEnum.SYSTEM_ADMIN.getKey())) {
                    Optional<SystemAdminModel> adminObj = systemAdminJDBCRepository.findById(loginCreds.getUserId());
                    if (adminObj.isPresent()) {
                        SessionUserModel sessionUser = new SessionUserModel();
                        sessionUser.setCd(loginCreds.getCd());
                        sessionUser.setPassword(loginCreds.getPassword());
                        sessionUser.setFirstNm(adminObj.get().getFirstNm());
                        sessionUser.setMiddleNm(adminObj.get().getMiddleNm());
                        sessionUser.setLastNm(adminObj.get().getLastNm());
                        sessionUser.setUserId(adminObj.get().getId());
                        sessionUser.setLastLoginDt(loginCreds.getUpdatedDt());
                        sessionUser.setRoleKey(loginCreds.getRole());
                        return ResponseEntity.ok(sessionUser);
                    }else{
                        return ResponseEntity.status(401).body("Unauthorized: No user in session");
                    }
                }
                //##########################
                //      Resident
                //##########################
                else if (loginCreds.getRole().equals(SystemUserEnum.SYSTEM_USER.getKey())) {
                    Optional<UsersModel> adminObj = usersJDBCRepository.findById(loginCreds.getUserId());
                    if (adminObj.isPresent()) {
                        SessionUserModel sessionUser = new SessionUserModel();
                        sessionUser.setCd(loginCreds.getCd());
                        sessionUser.setPassword(loginCreds.getPassword());
                        sessionUser.setFirstNm(adminObj.get().getFirstNm());
                        sessionUser.setMiddleNm(adminObj.get().getMiddleNm());
                        sessionUser.setLastNm(adminObj.get().getLastNm());
                        sessionUser.setUserId(adminObj.get().getId());
                        sessionUser.setLastLoginDt(loginCreds.getUpdatedDt());
                        sessionUser.setRoleKey(loginCreds.getRole());
                        return ResponseEntity.ok(sessionUser);
                    }else{
                        return ResponseEntity.status(401).body("Unauthorized: No user in session");
                    }
                }
            }
        }
        return ResponseEntity.status(401).body("Unauthorized: No user in session");
    }
}