package com.javaguides.bms.service;

import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.NotifLogsReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotifLogsServiceImpl extends BaseServiceImpl implements NotifLogsService {

    private final NotifLogsJDBCRepository notifLogsJDBCRepository;

    @Override
    public Page<NotifLogsReturnModel> searchNotifLogs(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<NotifLogsModel> notifLogs = notifLogsJDBCRepository.searchNotifLogs(searchRequest, pageRequest);
        return notifLogs.map(NotifLogsReturnModel::new);
    }

}
