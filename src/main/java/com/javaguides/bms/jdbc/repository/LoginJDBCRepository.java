package com.javaguides.bms.jdbc.repository;

import com.javaguides.bms.model.LoginCreds;

import java.util.List;

public interface LoginJDBCRepository {
    List<LoginCreds> getUserByCd(String userCd);

    int saveLoginCreds(LoginCreds loginCreds);

    int deleteByUserCd(String userCd);

    int updateLoginDt(String userId);
}
