package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.AnnouncementModel;

public interface AnnouncementJDBCRepository {
    int saveRequest(AnnouncementModel modelObj);
}
