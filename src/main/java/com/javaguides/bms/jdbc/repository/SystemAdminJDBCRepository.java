package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.SystemAdminModel;

import java.util.Optional;

public interface SystemAdminJDBCRepository {
    int updateAdmin(SystemAdminModel modelObj);

    Optional<SystemAdminModel> findById(String id);
}
