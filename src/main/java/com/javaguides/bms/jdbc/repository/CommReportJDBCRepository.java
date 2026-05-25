package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.CommReportAttachmentModel;
import com.javaguides.bms.model.CommReportModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface CommReportJDBCRepository {
    String save(CommReportModel modelObj);

    int saveAttachments(List<CommReportAttachmentModel> modelObj);

    int updateReport(CommReportModel model);

    Optional<CommReportModel> findById(String id);

    List<CommReportAttachmentModel> findByMainIdList(List<String> ids);

    Integer getCount();

    Page<CommReportModel> searchRequests(MainSearchRequest requestObj, PageRequest page);
}
