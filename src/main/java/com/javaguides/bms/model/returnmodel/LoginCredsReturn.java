package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.basemodel.BaseModel;
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
