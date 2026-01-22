package com.javaguides.bms.service;

import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.returnmodel.AnnouncementReturnModel;

public interface AnnouncementService {
    AnnouncementModel validateRequest(EnrollmentRequest requestObj);

    AnnouncementReturnModel saveRequest(EnrollmentRequest request);
}
