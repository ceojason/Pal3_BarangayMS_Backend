package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.model.basemodel.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends BaseModel {

    private String token;
    private String userCd;
    private Integer role;

    public LoginResponse(String token, String userCd, Integer role) {
        this.token = token;
        this.userCd = userCd;
        this.role = role;
    }
}
