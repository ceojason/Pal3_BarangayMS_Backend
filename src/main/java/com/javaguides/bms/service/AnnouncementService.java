package com.javaguides.bms.service;

import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.AnnouncementReturnModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AnnouncementService {
    AnnouncementModel validateRequest(EnrollmentRequest requestObj);

    AnnouncementReturnModel saveRequest(EnrollmentRequest request);

    Page<AnnouncementReturnModel> searchAnnouncement(MainSearchRequest searchRequest, PageRequest pageRequest);
}
