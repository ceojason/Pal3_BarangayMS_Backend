package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.StudentModel;
import com.javaguides.bms.model.UsersModel;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsersJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements UsersJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UsersJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int saveEnrollment(UsersModel modelObj) {
        return save(modelObj);
    }
}
