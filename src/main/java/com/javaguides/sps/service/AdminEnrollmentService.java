package com.javaguides.sps.service;

import com.javaguides.sps.model.SystemAdminModel;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;

public interface AdminEnrollmentService {
    SystemAdminModel validateEnrollment(EnrollmentRequest requestObj);
}
