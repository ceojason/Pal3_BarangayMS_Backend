package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.helper.DbTableUtil;
import com.javaguides.bms.jdbc.repository.basejdbcrepository.BaseJDBCRepositoryImpl;
import com.javaguides.bms.model.SystemAdminModel;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SystemAdminJDBCRepositoryImpl extends BaseJDBCRepositoryImpl implements SystemAdminJDBCRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String tblAdmin = DbTableUtil.getTableName(SystemAdminModel.class);

    public SystemAdminJDBCRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int updateAdmin(SystemAdminModel modelObj) { return update(modelObj); }

    @Override
    public Optional<SystemAdminModel> findById(String id) {
        return super.findById(id, tblAdmin, SystemAdminModel.class);
    }

}
