package com.javaguides.sps.jdbc.repository;

import com.javaguides.sps.model.LoginCreds;

import java.util.List;

public interface LoginJDBCRepository {
    List<LoginCreds> getUserByCd(String userCd);

    int saveLoginCreds(LoginCreds loginCreds);

    int deleteByUserCd(String userCd);
}
