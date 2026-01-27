package com.javaguides.bms.service;

import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.AnnouncementReturnModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    AnnouncementModel validateRequest(EnrollmentRequest requestObj);

    AnnouncementReturnModel saveRequest(EnrollmentRequest request);

    Page<AnnouncementReturnModel> searchAnnouncement(MainSearchRequest searchRequest, PageRequest pageRequest);

    Map<String, List<AnnouncementModel>> getAnnouncementListGrouped(Integer roleKey, HttpSession session);
}
