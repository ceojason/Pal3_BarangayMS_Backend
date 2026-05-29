package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemConfigEnum;
import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.jdbc.repository.*;
import com.javaguides.bms.model.*;
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
    private NotifLogsJDBCRepository notifLogsJDBCRepository;
    private CommReportJDBCRepository commReportJDBCRepository;
    private ConfigService configService;

    @Override
    public DashboardReturnModel getDashboardData(Integer roleKey, String userId) {
        DashboardReturnModel modelObj = new DashboardReturnModel();

        if (SystemUserEnum.SYSTEM_ADMIN.getKey().equals(roleKey)) {
            // getting users count
            Integer users = usersJDBCRepository.getUsersCount();
            modelObj.setParamCount1(String.valueOf(users));
            modelObj.setParamLabel1("No. of registered residents");

            // getting announcement count for today
            Integer announcement = announcementJDBCRepository.getCount();
            modelObj.setParamCount2(String.valueOf(announcement));
            modelObj.setParamLabel2("No. of announcement sent today");

            // getting pending requests
            modelObj.setParamCount3(documentJDBCRepository.getCount().toString());
            modelObj.setParamLabel3("No. of pending requests");

            // getting pending requests
            modelObj.setParamCount4(commReportJDBCRepository.getCount().toString());
            modelObj.setParamLabel4("No. of in-progress and pending reports");

            modelObj.setLogsList(notifLogsJDBCRepository.findRecentResidentLogs(null, 5));
            modelObj.setSystemActiveCount(loginJDBCRepository.getActiveCount());
        }

        if (SystemUserEnum.SYSTEM_USER.getKey().equals(roleKey)) {
            if (userId!=null) {
                Optional<LoginCreds> loginObj = loginJDBCRepository.getUserById(userId);
                if (loginObj.isEmpty()) {
                    return null;
                }else{
                    LoginCreds loginCreds = loginObj.get();
                    modelObj.setAnnouncementList(announcementJDBCRepository.findAnnouncementByUserId(loginCreds.getUserId(), 5));
                    modelObj.setLogsList(notifLogsJDBCRepository.findRecentResidentLogs(loginCreds.getUserId(), 5));

                }
            }
        }

        Optional<ConfigModel> configObj = configService.findConfigById(SystemConfigEnum.EMERGENCY_HOTLINES.getCode());
        if (configObj.isPresent()) {
            ConfigModel configObjMain = configObj.get();
            List<KeyValueModelStr> hotlineMap = new ArrayList<>();
            hotlineMap.add(new KeyValueModelStr("National Emergency", configObjMain.getString_1()));
            hotlineMap.add(new KeyValueModelStr("Dasmariñas City Police Station", configObjMain.getString_2()));
            hotlineMap.add(new KeyValueModelStr("Dasmariñas City Fire Station", configObjMain.getString_3()));
            hotlineMap.add(new KeyValueModelStr("Ambulance Center", configObjMain.getString_4()));
            hotlineMap.add(new KeyValueModelStr("CDRRMO Dasmariñas", configObjMain.getString_5()));
            hotlineMap.add(new KeyValueModelStr("Pagamutan ng Dasmariñas", configObjMain.getString_6()));
            hotlineMap.add(new KeyValueModelStr("Dasmariñas City Hall", configObjMain.getString_7()));
            hotlineMap.add(new KeyValueModelStr("Meralco Business Center - Dasmariñas Branch", configObjMain.getString_8()));
            hotlineMap.add(new KeyValueModelStr("Barangay Rescue / Emergency Operations Center", configObjMain.getString_9()));
            hotlineMap.add(new KeyValueModelStr("Water Utility (PrimeWater Dasmariñas)", configObjMain.getString_10()));
            modelObj.setHotlineList(hotlineMap);
        }

        return modelObj;
    }

}
