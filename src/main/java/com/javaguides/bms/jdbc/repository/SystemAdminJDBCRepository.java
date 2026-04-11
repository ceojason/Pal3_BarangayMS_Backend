package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;

import java.util.Optional;

public interface SystemAdminJDBCRepository {
    int updateAdmin(SystemAdminModel modelObj);

    Optional<SystemAdminModel> findById(String id);

    SystemAdminModel findUserInResetNoSession(MainSearchRequest searchRequest);
}
