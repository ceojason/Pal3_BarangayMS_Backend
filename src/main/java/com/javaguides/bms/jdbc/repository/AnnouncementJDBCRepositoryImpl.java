package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.AnnouncementModel;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnnouncementJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements AnnouncementJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AnnouncementJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveRequest(AnnouncementModel modelObj) {
        return save(modelObj);
    }

}
