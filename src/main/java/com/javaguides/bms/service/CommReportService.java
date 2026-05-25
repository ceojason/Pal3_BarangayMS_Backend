package com.javaguides.bms.service;

import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.CommReportReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface CommReportService {
    CommReportReturn validateRequest(EnrollmentRequest requestObj, String userId) throws IOException;

    CommReportReturn saveRequest(EnrollmentRequest requestObj, String userId) throws IOException;

    Page<CommReportReturn> searchRequests(MainSearchRequest searchRequest, PageRequest pageRequest);

    List<KeyValueModelStr> getBrgyOfficialList();

    CommReportReturn update(EnrollmentRequest request);
}
