package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.LoginCreds;

import java.util.List;
import java.util.Optional;

public interface LoginJDBCRepository {

    Optional<LoginCreds> getUserByCd(String userCd);

    Optional<LoginCreds> getUserById(String id);

    int update(LoginCreds model);

    int saveLoginCreds(LoginCreds loginCreds);

    int deleteByUserCd(String userCd);

    int deleteByUserId(String userId);

    Integer getActiveCount();

    int updateLoginDt(String userId);
}
