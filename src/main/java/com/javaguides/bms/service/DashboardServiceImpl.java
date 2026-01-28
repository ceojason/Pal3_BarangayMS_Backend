package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.jdbc.repository.AnnouncementJDBCRepository;
import com.javaguides.bms.jdbc.repository.DocumentJDBCRepository;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SessionUserModel;
import com.javaguides.bms.model.returnmodel.DashboardReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DashboardServiceImpl extends BaseServiceImpl implements DashboardService {

    private UsersJDBCRepository usersJDBCRepository;
    private AnnouncementJDBCRepository announcementJDBCRepository;
    private LoginJDBCRepository loginJDBCRepository;
    private DocumentJDBCRepository documentJDBCRepository;

    @Override
    public DashboardReturnModel getDashboardData(Integer roleKey, HttpSession session) {
        DashboardReturnModel modelObj = new DashboardReturnModel();

        if (SystemUserEnum.SYSTEM_ADMIN.getKey().equals(roleKey)) {
            // getting users count
            Integer users = usersJDBCRepository.getUsersCount();
            modelObj.setParamCount1(String.valueOf(users));
            modelObj.setParamLabel1("No. of registered users");

            // getting announcement count for today
            Integer announcement = announcementJDBCRepository.getCount();
            modelObj.setParamCount2(String.valueOf(announcement));
            modelObj.setParamLabel2("No. of announcement sent today");

            modelObj.setParamCount3(documentJDBCRepository.getCount().toString());
            modelObj.setParamLabel3("No. of pending requests");
        }

        if (SystemUserEnum.SYSTEM_USER.getKey().equals(roleKey)) {
            Object userObj = session.getAttribute("user");
            if (userObj!=null) {
                LoginCreds user = (LoginCreds) userObj;
                List<LoginCreds> list = loginJDBCRepository.getUserById(user.getUserId());
                if (list==null || list.isEmpty()) {
                    return null;
                } else if (list.size()>1) {
                    return null;
                } else {
                    LoginCreds loginCreds = list.get(0);
                    modelObj.setAnnouncementList(announcementJDBCRepository.findAnnouncementByUserId(loginCreds.getUserId()));
                }
            }
        }

        return modelObj;
    }

}
