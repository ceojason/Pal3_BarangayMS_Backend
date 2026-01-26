package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.UsersReturnModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UsersService {
    Page<UsersReturnModel> searchUsers(MainSearchRequest searchResult, PageRequest pageRequest);

    UsersReturnModel validateEnrollment(EnrollmentRequest requestObj);

    UsersReturnModel saveEnrollment(EnrollmentRequest requestObj, HttpSession session);

    UsersReturnModel update(EnrollmentRequest requestObj, HttpSession session);

    UsersReturnModel deleteUser(String userId);

    UsersReturnModel reset(EnrollmentRequest requestObj, HttpSession session);

    UsersReturnModel findByUserId(String userId);

    void saveProfileImage(String userId, MultipartFile file);

    Resource loadProfileImage(String userId);
}
