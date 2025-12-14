package com.javaguides.sps.service;

import com.javaguides.sps.enums.SystemUserEnum;
import com.javaguides.sps.jdbc.repository.LoginJDBCRepository;
import com.javaguides.sps.jdbc.repository.StudentJDBCRepository;
import com.javaguides.sps.model.LoginCreds;
import com.javaguides.sps.model.StudentModel;
import com.javaguides.sps.helper.KeyHasher;
import com.javaguides.sps.model.requestmodel.LoginCredsRequest;
import com.javaguides.sps.model.returnmodel.LoginCredsReturn;
import com.javaguides.sps.service.baseservice.BaseServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl extends BaseServiceImpl implements LoginService {

    @Autowired
    private LoginJDBCRepository loginJDBCRepository;
    @Autowired
    private StudentJDBCRepository studentJDBCRepository;

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
            obj.getErrorList().add("There is an error upon login. Please try again.");
        } else if (lcList.size() > 1) {
            obj.getErrorList().add("There is an error upon login. Please contact the system administrator for assistance.");
        } else {
            LoginCreds loginCreds = lcList.get(0);
            String passwordFromDb = loginCreds.getPassword();
            byte[] salt = loginCreds.getSalt();
            if (!passwordFromDb.equals(KeyHasher.hashPassword(obj.getPassword(), salt))) {
                obj.getErrorList().add("Password is incorrect. Please try again.");
            } else {
                getSessionByRoleKey(loginCreds, session);
            }
        }
        if (obj.getErrorList()!=null && !obj.getErrorList().isEmpty()) {
            throwErrorMessages(obj.getErrorList());
        }

        return obj;
    }


    private void getSessionByRoleKey(LoginCreds lcObj, HttpSession session) {
        if (lcObj.getRole()!=null && lcObj.getRole().equals(SystemUserEnum.STUDENT.getKey())) {
            StudentModel modelObj = studentJDBCRepository.findById(lcObj.getUserId());
            if (modelObj!=null) session.setAttribute("user", modelObj);
        }
        else if (lcObj.getRole()!=null && lcObj.getRole().equals(SystemUserEnum.FACULTY.getKey())) {
            //todo
        }
        else if (lcObj.getRole()!=null && lcObj.getRole().equals(SystemUserEnum.SYSTEM_ADMIN.getKey())) {
            //todo
        }
    }

}
