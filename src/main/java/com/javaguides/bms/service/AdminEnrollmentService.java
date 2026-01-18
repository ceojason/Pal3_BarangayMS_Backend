package com.javaguides.bms.service;

import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;

public interface AdminEnrollmentService {
    SystemAdminModel validateEnrollment(EnrollmentRequest requestObj);
}
