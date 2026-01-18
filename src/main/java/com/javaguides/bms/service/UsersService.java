package com.javaguides.bms.service;

import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.returnmodel.UsersReturnModel;
import jakarta.servlet.http.HttpSession;

public interface UsersService {
    UsersReturnModel searchUsers();

    UsersReturnModel validateEnrollment(EnrollmentRequest requestObj);

    UsersReturnModel saveEnrollment(EnrollmentRequest requestObj, HttpSession session);
}
