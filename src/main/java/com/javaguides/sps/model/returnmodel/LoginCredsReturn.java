package com.javaguides.sps.model.returnmodel;

import com.javaguides.sps.model.LoginCreds;
import com.javaguides.sps.model.basemodel.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginCredsReturn extends BaseModel {

    private String userCd;

    public LoginCredsReturn(LoginCreds loginCredsObj) {
        setUserCd(loginCredsObj.getCd());
        setErrorList(loginCredsObj.getErrorList());
    }
}
