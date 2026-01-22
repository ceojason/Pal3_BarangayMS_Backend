package com.javaguides.bms.service;

import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.NotifLogsReturnModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface NotifLogsService {
    Page<NotifLogsReturnModel> searchNotifLogs(MainSearchRequest searchRequest, PageRequest pageRequest);
}
