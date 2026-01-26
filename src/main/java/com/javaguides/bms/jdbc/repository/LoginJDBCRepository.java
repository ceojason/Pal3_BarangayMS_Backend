package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.LoginCreds;

import java.util.List;

public interface LoginJDBCRepository {
    List<LoginCreds> getUserByCd(String userCd);

    List<LoginCreds> getUserById(String id);

    int update(LoginCreds model);

    int saveLoginCreds(LoginCreds loginCreds);

    int deleteByUserCd(String userCd);

    int deleteByUserId(String userId);

    int updateLoginDt(String userId);
}
