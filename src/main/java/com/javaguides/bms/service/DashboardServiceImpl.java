package com.javaguides.bms.service;

import com.javaguides.bms.enums.SystemUserEnum;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.returnmodel.DashboardReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardServiceImpl extends BaseServiceImpl implements DashboardService {

    public UsersJDBCRepository usersJDBCRepository;

    @Override
    public DashboardReturnModel getDashboardData(Integer roleKey) {
        DashboardReturnModel modelObj = new DashboardReturnModel();

        if (roleKey!=null && SystemUserEnum.SYSTEM_ADMIN.getKey().equals(roleKey)) {
            // getting users count
            Integer users = usersJDBCRepository.getUsersCount();
            modelObj.setParamCount1(String.valueOf(users));
            modelObj.setParamLabel1("No. of registered users");

            modelObj.setParamCount2(String.valueOf(0));
            modelObj.setParamLabel2("No. of announcement sent today");

            modelObj.setParamCount3(String.valueOf(0));
            modelObj.setParamLabel3("No. of pending requests");
        }


        return modelObj;
    }

}
