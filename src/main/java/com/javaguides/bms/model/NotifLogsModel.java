package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_notif_logs")
@TableAlias("tal")
public class NotifLogsModel extends BaseModel {

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "RECIPIENT")
    private String recipient;

    @Column(name = "IS_SMS_EMAIL")
    private Integer isSmsEmail; // 1 == sms, 0 == email

    @Column(name = "SENT_DT")
    private Date sentDt;

    @Column(name = "TYPE")
    private Integer type;

}
