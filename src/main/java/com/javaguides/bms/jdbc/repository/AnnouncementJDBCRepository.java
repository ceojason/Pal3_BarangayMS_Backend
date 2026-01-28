package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AnnouncementJDBCRepository {
    int saveRequest(AnnouncementModel modelObj);

    int saveBatch(List<AnnouncementModel> list);

    Integer getCount();

    List<AnnouncementModel> findAnnouncementByUserId(String userId);

    List<AnnouncementModel> findAnnouncementByUserIdGrouped(String userId);

    Page<AnnouncementModel> searchAnnouncement(MainSearchRequest requestObj, PageRequest page);
}
