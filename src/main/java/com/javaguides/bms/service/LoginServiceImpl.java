package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.StudentJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemAdminJDBCRepository;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.StudentModel;
import com.javaguides.bms.helper.KeyHasher;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.requestmodel.LoginCredsRequest;
import com.javaguides.bms.model.returnmodel.LoginCredsReturn;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    private LoginJDBCRepository loginJDBCRepository;
    private StudentJDBCRepository studentJDBCRepository;
    private SystemAdminJDBCRepository systemAdminJDBCRepository;

    @Override
    public LoginCredsReturn login(LoginCredsRequest loginRequest, HttpSession session) throws Exception {
        LoginCreds loginCredsObj = validateLoginRequestFromForm(loginRequest, session);
        return new LoginCredsReturn(loginCredsObj);
    }

    public LoginCreds validateLoginRequestFromForm(LoginCredsRequest loginRequest, HttpSession session) throws Exception {
        LoginCreds obj = new LoginCreds(loginRequest);
        obj.setErrorList(new ArrayList<>());
        if (obj.getCd()!=null) {
            obj.setCd(obj.getCd().trim().toUpperCase());
        } else {
            obj.getErrorList().add("User ID/LRN is required.");
        }

        if (obj.getPassword()==null || obj.getPassword().isEmpty()) {
            obj.getErrorList().add("Password is required.");
        }

        if (obj.getErrorList()!=null && !obj.getErrorList().isEmpty()) throwErrorMessages(obj.getErrorList());

        List<LoginCreds> lcList = loginJDBCRepository.getUserByCd(obj.getCd());
        if (lcList==null || lcList.isEmpty()) {
            obj.getErrorList().add("The user ID or password is incorrect. Please try again.");
        } else if (lcList.size() > 1) {
            obj.getErrorList().add("There is an error upon login. Please contact the system administrator for assistance.");
        } else {
            LoginCreds loginCreds = lcList.get(0);
            String passwordFromDb = loginCreds.getPassword();
            byte[] salt = loginCreds.getSalt();
            String hashPassword = KeyHasher.hashPassword(obj.getPassword(), salt);
            if (!passwordFromDb.equals(hashPassword)) {
                obj.getErrorList().add("The password is incorrect. Please try again.");
            } else {
                getSessionByRoleKey(loginCreds, session);
                loginJDBCRepository.updateLoginDt(loginCreds.getUserId());
            }
        }
        if (obj.getErrorList()!=null && !obj.getErrorList().isEmpty()) {
            throwErrorMessages(obj.getErrorList());
        }

        return obj;
    }


    private void getSessionByRoleKey(LoginCreds lcObj, HttpSession session) {
        session.setAttribute("user", lcObj);
    }

}
