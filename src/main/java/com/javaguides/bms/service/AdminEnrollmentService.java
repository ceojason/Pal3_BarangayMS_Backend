package com.javaguides.bms.service;

import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.returnmodel.AdminReturnModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AdminEnrollmentService {
    SystemAdminModel validateEnrollment(EnrollmentRequest requestObj);

    AdminReturnModel findByUserId(String userId);

    void saveProfileImage(String userId, MultipartFile file);

    Resource loadProfileImage(String userId);
}
