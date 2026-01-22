package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface NotifLogsJDBCRepository {
    int saveNotifLogs(NotifLogsModel model);

    Page<NotifLogsModel> searchNotifLogs(MainSearchRequest requestObj, PageRequest page);
}
