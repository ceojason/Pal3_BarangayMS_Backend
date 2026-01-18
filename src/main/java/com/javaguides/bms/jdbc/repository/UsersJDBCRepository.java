package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.UsersModel;

public interface UsersJDBCRepository {

    int saveEnrollment(UsersModel modelObj);
}
