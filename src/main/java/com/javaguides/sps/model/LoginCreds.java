package com.javaguides.sps.model;

import com.javaguides.sps.model.basemodel.BaseModel;
import com.javaguides.sps.model.requestmodel.LoginCredsRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_login_creds")
public class LoginCreds extends BaseModel {

    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "CD")
    private String cd;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "SALT")
    private byte[] salt;
    @Column(name = "LOGIN_STATUS")
    private Integer loginStatus;
    @Column(name = "ROLE")
    private Integer role;
    @Column(name = "UPDATED_DT")
    private Integer updatedDt;

    public LoginCreds(LoginCredsRequest loginRequest) {
        if (loginRequest!=null) {
            setCd(loginRequest.getCd());
            setPassword(loginRequest.getPassword());
        }
    }
}
